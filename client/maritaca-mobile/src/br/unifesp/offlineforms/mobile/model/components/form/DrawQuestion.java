package br.unifesp.offlineforms.mobile.model.components.form;

import java.io.File;

import org.simpleframework.xml.Root;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import br.unifesp.offlineforms.mobile.activities.ControllerActivity;
import br.unifesp.offlineforms.mobile.activities.DrawActivity;
import br.unifesp.offlineforms.mobile.activities.R;
import br.unifesp.offlineforms.mobile.model.Question;
import br.unifesp.offlineforms.mobile.model.components.ComponentType;

/**
 * 
 * @author Walkirya Heto Silva
 * @version  0.1.10
 *
 */

@Root(name = "draw")
public class DrawQuestion extends Question {

	private static final long serialVersionUID = 1L;
	private File drawPicture;
	private Intent intent;
	public String path;

	@Override
	public ComponentType getComponentType() {
		return ComponentType.DRAW;
	}

	@Override
	public View getLayout(final ControllerActivity activity) {
		DrawActivity.DQ = this;

		LinearLayout outputLayout = new LinearLayout(activity);
		outputLayout.setOrientation(LinearLayout.VERTICAL);
		LinearLayout buttonsLayout = new LinearLayout(activity);
		outputLayout.addView(buttonsLayout);
		LinearLayout picViewLayout = new LinearLayout(activity);

		Button btnDraw = new Button(activity);
		btnDraw.setText(R.string.button_click_draw);
		btnDraw.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				intent = new Intent(activity, DrawActivity.class);
				activity.startActivity(intent);

			}
		});
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,LinearLayout.LayoutParams.FILL_PARENT);
        params.setMargins(20, 0, 0, 0);
		buttonsLayout.addView(btnDraw, params);

		if (this.getPath() != null) {
			ImageView imgView = new ImageView(activity);
			String imagePath = getPath();
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 1;
			Bitmap bmp = BitmapFactory.decodeFile(imagePath);
			imgView.setImageBitmap(bmp);
			picViewLayout.addView(imgView);
			drawPicture = new File(imagePath);
		}
		outputLayout.addView(picViewLayout);

		return outputLayout;
	}

	@Override
	public boolean validate() {
		if (required) {
			if (!getValue().equals("") && getValue().length() > 0)
				return true;
			return false;
		}
		return true;
	}

	@Override
	public void save(View answer) {
		if (drawPicture != null && drawPicture.exists()) {
			value = drawPicture.getAbsolutePath();
		} else
			value = null;
	}

	@Override
	public Integer getNext() {
		return next;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@Override
	public String getValue() {
		return value != null ? value.toString() : "";
	}
}