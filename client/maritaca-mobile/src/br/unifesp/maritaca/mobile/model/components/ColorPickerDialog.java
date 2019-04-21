package br.unifesp.maritaca.mobile.model.components;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

/**
 * 
 * @author Walkirya Heto Silva
 * @version  0.1.10
 *
 */

public class ColorPickerDialog extends Dialog {
	
	public ColorPickerDialog(Context context,OnColorChangedListener listener,int initialColor) {
	    super(context);
	    myListener = listener;
	    myInitialColor = initialColor;
	}

	public interface OnColorChangedListener {
		void colorChanged(int color);
	}

	private OnColorChangedListener myListener;
	private int myInitialColor;

	private static class ColorPickerView extends View {
	    private Paint paint;
	    private Paint centerPaint;
	    private final int[] colors;
	    private OnColorChangedListener listener;
	    private boolean trackingCenter;
	    private boolean highlightCenter;
	    private static final int CENTER_X = 100;
	    private static final int CENTER_Y = 100;
	    private static final int CENTER_RADIUS = 32;
	    private static final float PI = 3.1415926f;

	    ColorPickerView(Context c, OnColorChangedListener l, int color) {
	    	super(c);
	    	listener = l;
	    	colors = new int[] { 0xFFFF0000, 0xFFFF00FF,0xff000000, 0xFF0000FF,
	    			0xFF00FFFF, 0xFF00FF00, 0xFFFFFF00, 0xFFFF0000 };
	    	Shader s = new SweepGradient(0, 0, colors, null);
	    	paint = new Paint(Paint.ANTI_ALIAS_FLAG);
	    	paint.setShader(s);
	    	paint.setStyle(Paint.Style.STROKE);
	    	paint.setStrokeWidth(32);
	    	centerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	    	centerPaint.setColor(color);
	    	centerPaint.setStrokeWidth(5);
	    }

	    @Override
	    protected void onDraw(Canvas canvas) {
	    	float r = CENTER_X - paint.getStrokeWidth() * 0.5f;
	    	canvas.translate(CENTER_X, CENTER_X);
	    	canvas.drawOval(new RectF(-r, -r, r, r), paint);
	    	canvas.drawCircle(0, 0, CENTER_RADIUS, centerPaint);

	    	if (trackingCenter) {
	    		int c = centerPaint.getColor();
	    		centerPaint.setStyle(Paint.Style.STROKE);
	    		if (highlightCenter) {
	    			centerPaint.setAlpha(0xFF);
	    		} else {
	    			centerPaint.setAlpha(0x80);
	    		}
		        canvas.drawCircle(0, 0,CENTER_RADIUS + centerPaint.getStrokeWidth(),centerPaint);
		        centerPaint.setStyle(Paint.Style.FILL);
		        centerPaint.setColor(c);
	    	}
	    }

	    @Override
	    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
	    	setMeasuredDimension(CENTER_X * 2, CENTER_Y * 2);
	    }

	    private int ave(int s, int d, float p) {
	    	return s + java.lang.Math.round(p * (d - s));
	    }

	    private int interpColor(int colors[], float unit) {
	    	if (unit <= 0) {
	    		return colors[0];
	    	}
	    	if (unit >= 1) {
	    		return colors[colors.length - 1];
	    	}

	    	float p = unit * (colors.length - 1);
	    	int i = (int) p;
	    	p -= i;
	    	int c0 = colors[i];
	    	int c1 = colors[i + 1];
	    	int a = ave(Color.alpha(c0), Color.alpha(c1), p);
	    	int r = ave(Color.red(c0), Color.red(c1), p);
	    	int g = ave(Color.green(c0), Color.green(c1), p);
	    	int b = ave(Color.blue(c0), Color.blue(c1), p);

	    	return Color.argb(a, r, g, b);
	    }

	    @Override
	    public boolean onTouchEvent(MotionEvent event) {
	    	float x = event.getX() - CENTER_X;
	    	float y = event.getY() - CENTER_Y;
	    	boolean inCenter = java.lang.Math.sqrt(x * x + y * y) <= CENTER_RADIUS;

	    	switch (event.getAction()) {
	    		case MotionEvent.ACTION_DOWN:
	    			trackingCenter = inCenter;
	    			if (inCenter) {
	    				highlightCenter = true;
	    				invalidate();
	    				break;
	    			}
	    		case MotionEvent.ACTION_MOVE:
	    			if (trackingCenter) {
	    				if (highlightCenter != inCenter) {
	    					highlightCenter = inCenter;
	    					invalidate();
	    				}
	    			}else{
	    				float angle = (float) java.lang.Math.atan2(y, x);
	    				float unit = angle / (2 * PI);
	    				if (unit < 0) {
	    					unit += 1;
	    				}
	    				centerPaint.setColor(interpColor(colors, unit));
	    				invalidate();
	    			}
	    			break;
	    		case MotionEvent.ACTION_UP:
	    			if (trackingCenter) {
	    				if (inCenter) {
	    					listener.colorChanged(centerPaint.getColor());
	    				}
	    				trackingCenter = false;
	    				invalidate();
	    			}
	    			break;
	    		}
	    	return true;
	    }
	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		OnColorChangedListener l = new OnColorChangedListener() {
			public void colorChanged(int color) {
				myListener.colorChanged(color);
				dismiss();
			}
	    };
	    setContentView(new ColorPickerView(getContext(), l, myInitialColor));
	    setTitle("Pick a Color");
	}
}