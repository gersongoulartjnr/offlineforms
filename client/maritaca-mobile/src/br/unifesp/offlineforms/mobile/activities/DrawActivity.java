package br.unifesp.offlineforms.mobile.activities;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;
import br.unifesp.offlineforms.mobile.model.components.ColorPickerDialog;
import br.unifesp.offlineforms.mobile.model.components.ColorPickerDialog.OnColorChangedListener;
import br.unifesp.offlineforms.mobile.model.components.form.DrawQuestion;

/**
 * 
 * @author Walkirya Heto Silva
 * @version  0.1.10
 *
 */

public class DrawActivity extends Activity implements OnColorChangedListener{

	private static Paint paint;
	public static DrawQuestion DQ = null;
	private static Bitmap bitmap;
 	private static Canvas canvas;
 	private static int backgroundColor;
 	private static File maritacaDirectory;
 	private String picPath;
 	private Bitmap saveBitmap;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		Toast toast = Toast.makeText(this, "Draw here!", Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER|Gravity.CENTER_VERTICAL, 0, 0);
		toast.show();
	    
	    MyView view = new MyView(this);

	    setContentView(view);
	    	    
	    String maritacaFilePath = Environment.getExternalStorageDirectory().getPath() + File.separator + "Maritaca";
	   	maritacaDirectory = new File(maritacaFilePath);
	   	if (!(maritacaDirectory.exists())) {
	   		maritacaDirectory.mkdir();
	   	}
	   	
	   	backgroundColor = 0xffffffff;
	   	setPaint(new Paint());
	   	getPaint().setAntiAlias(true);
	   	getPaint().setDither(true);
	   	getPaint().setColor(0xff000000);
	   	getPaint().setStyle(Paint.Style.STROKE);
	   	getPaint().setStrokeJoin(Paint.Join.ROUND);
	   	getPaint().setStrokeCap(Paint.Cap.ROUND);
	   	getPaint().setStrokeWidth(15); 

	}

	public void setBackgroudColor(int color) {
		DrawActivity.backgroundColor = color;
	}	
	  
	public static class MyView extends View {
		private Path mPath;
		private Paint mBitmapPaint;
		private float mX, mY;
		private static final float TOUCH_TOLERANCE = 4;
		  
		public MyView(Context DrawActivity) {
			super(DrawActivity);
			mPath = new Path();
			mBitmapPaint = new Paint(Paint.DITHER_FLAG);	 
		}	
		
		@Override
		protected void onSizeChanged(int w, int h, int oldw, int oldh) {
			super.onSizeChanged(w, h, oldw, oldh);
			bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
			canvas = new Canvas(bitmap);
		}

		@Override
		protected void onDraw(Canvas canvas) {
			canvas.drawColor(backgroundColor);
			canvas.drawBitmap(bitmap, 0, 0, mBitmapPaint);
			canvas.drawPath(mPath, getPaint());   
		}

		private void touch_start(float x, float y) {
			mPath.reset();
			mPath.moveTo(x, y);
			mX = x;
			mY = y;
		}

		private void touch_move(float x, float y) {
			float dx = Math.abs(x - mX);
			float dy = Math.abs(y - mY);
			if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
				mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
		    	mX = x;
		    	mY = y;
		  	}
		}
	
		private void touch_up() {
			mPath.lineTo(mX, mY);
			canvas.drawPath(mPath, getPaint());
			mPath.reset();
		}

		@Override
		public boolean onTouchEvent(MotionEvent event) {
			float x = event.getX();
			float y = event.getY();

			switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					touch_start(x, y);
					invalidate();
				break;
				case MotionEvent.ACTION_MOVE:
					touch_move(x, y);
					invalidate();
				break;
				case MotionEvent.ACTION_UP:
					touch_up();
					invalidate();
				break;
			}
			return true;
		}    
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.drawer_menu, menu);
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		super.onPrepareOptionsMenu(menu);
		return true;
	}

	//@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		getPaint().setXfermode(null);
		getPaint().setAlpha(0xFF);

	    switch (item.getItemId()) {
	    case R.id.btnDrawColor:
	    	new ColorPickerDialog(this, this, getPaint().getColor()).show();
	    	
	      	return true;
	      	
	    case R.id.btnDrawEraser:
	    	paint.setColor(Color.WHITE);	
	    	return true;
	    	
	    case R.id.btnDrawClearScreen:
	    	super.setContentView(new MyView(this));
	    	DrawActivity.getPaint().setColor(Color.BLACK);
	        return true;
	    	
	    case R.id.btnDrawSave:
	    	try {
	    		String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date());
	    		picPath = (Environment.getExternalStorageDirectory().getPath() + File.separator	+ "Maritaca" + File.separator  +"draw_pic"+timeStamp+".jpg");
	            final FileOutputStream out = new FileOutputStream(new File(picPath));
	           
                saveBitmap = Bitmap.createBitmap(bitmap);
                Canvas c = new Canvas(saveBitmap);
                c.drawColor(0xFFFFFFFF);
                c.drawBitmap(bitmap,0,0,null);
                saveBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                saveBitmap.recycle();
	            Toast.makeText(this, "Saved with success!", Toast.LENGTH_SHORT).show();
	            this.DQ.setPath(picPath);	
	            super.finish();
	            
	        }catch (Exception e) {
	        	Toast.makeText(this, "Error while saving", Toast.LENGTH_LONG).show();
	            e.printStackTrace();
	        }
	    	
	    	return true;
	    default:
	    	return onOptionsItemSelected(item);
	    }	    
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
	    super.onConfigurationChanged(newConfig);
	    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}
	
	public static Paint getPaint() {
		return paint;
	}

	public static void setPaint(Paint paint) {
		DrawActivity.paint = paint;
	}

	@Override
	public void colorChanged(int color) {
		DrawActivity.paint.setColor(color);		
	}
}