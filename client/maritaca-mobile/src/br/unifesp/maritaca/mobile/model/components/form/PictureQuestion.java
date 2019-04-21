package br.unifesp.maritaca.mobile.model.components.form;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.simpleframework.xml.Root;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import br.unifesp.maritaca.mobile.activities.ControllerActivity;
import br.unifesp.maritaca.mobile.activities.R;
import br.unifesp.maritaca.mobile.model.Question;
import br.unifesp.maritaca.mobile.model.components.ComponentType;
import br.unifesp.maritaca.mobile.util.Constants;

@Root(name = "picture")
public class PictureQuestion extends Question {

	private static final long serialVersionUID = 1L;
	private File picture;

    @Override
    public ComponentType getComponentType() {
        return ComponentType.PICTURE;
    }

	@Override
	public Integer getNext() {
		return next;
	}

    @Override
	public String getValue() {
		return value != null ? value.toString() : "";
	}

    @Override
    public View getLayout(final ControllerActivity activity) {
    	LinearLayout layout = new LinearLayout(activity);
    	layout.setOrientation(LinearLayout.VERTICAL);
        TableLayout.LayoutParams buttonParams = new TableLayout.LayoutParams(
				TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT);
        buttonParams.setMargins(20, 0, 0, 0);
        
        //TableLayout tableLayout = new TableLayout(activity);
        RelativeLayout buttonLayout = new RelativeLayout(activity);
        //TableRow firstRow = new TableRow(activity);
        Button btnPicture = new Button(activity);
        btnPicture.setText(R.string.button_take_picture);
        btnPicture.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startCameraActivity(activity);
            }
        });
        
        buttonLayout.addView(btnPicture);
        //tableLayout.addView(firstRow, params);
        layout.addView(buttonLayout, buttonParams);
        
        if(picture != null && picture.exists()) {
            //TableRow secondRow = new TableRow(activity);
            RelativeLayout pictureLayout = new RelativeLayout(activity);
            RelativeLayout.LayoutParams pictureParams = new RelativeLayout.LayoutParams(
            		RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.FILL_PARENT);
            pictureParams.setMargins(20, 10, 20, 10);
            ImageView imgView = new ImageView(activity);
            //Set width and height
            String imagePath = picture.getAbsolutePath();       
            //
            try {
            	BitmapFactory.Options options = new BitmapFactory.Options();
            	options.inSampleSize = 2;
            	activity.setBitmap(BitmapFactory.decodeFile(imagePath, options));
	            int w = activity.getBitmap().getWidth();
	            int h = activity.getBitmap().getHeight();
	            
	            ExifInterface exif = new ExifInterface(imagePath);
	            String orientString = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
	            int orientation = orientString != null ? Integer.parseInt(orientString) : ExifInterface.ORIENTATION_NORMAL;
	            int rotationAngle = 0;
	            if (orientation == ExifInterface.ORIENTATION_ROTATE_90) rotationAngle = 90;
	            if (orientation == ExifInterface.ORIENTATION_ROTATE_180) rotationAngle = 180;
	            if (orientation == ExifInterface.ORIENTATION_ROTATE_270) rotationAngle = 270;
	
	            Matrix matrix = new Matrix();
	            matrix.setRotate(rotationAngle, (float) w / 2, (float) h / 2);
	            activity.setRotatedBitmap(Bitmap.createBitmap(activity.getBitmap(), 0, 0, w, h, matrix, true));
	            imgView.setImageBitmap(activity.getRotatedBitmap());
	        } catch (IOException e) {
				throw new RuntimeException();
			}            
            imgView.setPadding(20, 10, 20, 10);
            pictureLayout.addView(imgView);
            layout.addView(pictureLayout);
        }     
        return layout;
    }

    @Override
	public boolean validate() {
		if(required) {
			if(!"".equals(getValue()))
				return true;
			return false;
		}
		return true;
	}

	@Override
	public void save(View answer) {
		if (picture != null && picture.exists()) {
			value = picture.getAbsolutePath();
		} else{
			value = null;
		}
	}

	protected void startCameraActivity(ControllerActivity activity) {
		String timeStamp = new SimpleDateFormat(Constants.DATE_FILE_FORMAT).format(new Date());
		String imageDirPath = Environment.getExternalStorageDirectory() + File.separator + Constants.FILES_DIR;
		String imagePath = imageDirPath + File.separator + "pic_" + timeStamp + ".jpg";
		picture = new File(imagePath);
		Uri outputFileUri = Uri.fromFile(picture);
		Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		activity.startActivityForResult(intent, Constants.PICTURE_REQUEST);
	}
}