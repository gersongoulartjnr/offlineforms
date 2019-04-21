package br.unifesp.maritaca.mobile.model.components.form;

import java.math.BigDecimal;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

import android.R;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import br.unifesp.maritaca.mobile.activities.ControllerActivity;
import br.unifesp.maritaca.mobile.model.Question;
import br.unifesp.maritaca.mobile.model.components.ComponentType;

/**
 * 
 * @author Leonardo Sueoka
 * @version 0.1.6
 * 
 */

@Root(name = "slider")
public class SliderQuestion extends Question {

	private static final long serialVersionUID = 1L;

	@Attribute(name = "max", required = false)
	private Integer maxValue = 100;

	private TextView text;
	private final int seekBarId = 1;

	@Override
	public ComponentType getComponentType() {
		return ComponentType.SLIDER;
	}

	@Override
	public Integer getNext() {
		int size = 0;
		if (getComparisons() != null && getComparisons().size() > 0)
			size = getComparisons().size();

		if (getComparisons() != null && size < 1)
			return next;

		for (int i = 0; i < size; i++) {
			BigDecimal value = this.getValue();
			if (getComparisons().get(i).evaluate(value)) {
				return getComparisons().get(i).getGoTo();
			}
		}
		return next;
	}

	@Override
	public BigDecimal getValue() {
		try {
			return (value != null && !"".equals(value)) ? new BigDecimal(
					value.toString()) : null;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * FIXME This function is making an error on GalaxyS3
	 */
	public void setProgressBarColor(ProgressBar progressBar, int newColor) {
		LayerDrawable ld = (LayerDrawable) progressBar.getProgressDrawable();
		ClipDrawable d1 = (ClipDrawable) ld.findDrawableByLayerId(R.id.progress);
		d1.setColorFilter(newColor, PorterDuff.Mode.SRC_IN);
	}

	@Override
	public View getLayout(ControllerActivity activity) {
		LinearLayout viewGroup = new LinearLayout(activity);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(20, 0, 0, 20);
        viewGroup.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
		viewGroup.setOrientation(1);

		LinearLayout seekBarLinearLayout = new LinearLayout(activity);
		seekBarLinearLayout.setLayoutParams(new LayoutParams(
				ViewGroup.LayoutParams.FILL_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT));
		seekBarLinearLayout.setOrientation(1);
		SeekBar seekBar = new SeekBar(activity);
		seekBar.setId(seekBarId);
		seekBar.setMax(maxValue);
		seekBar.setProgress(getSliderDefault());
		seekBar.setLayoutParams(new LayoutParams(
				ViewGroup.LayoutParams.FILL_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT));
		seekBar.setPadding(50, 0, 50, 0);
		seekBarLinearLayout.addView(seekBar);
		seekBarLinearLayout.setOrientation(1);
		//setProgressBarColor(seekBar, Color.rgb(145, 232, 66));
		ShapeDrawable thumb = new ShapeDrawable(new OvalShape());
		thumb.getPaint().setColor(Color.rgb(5, 92, 16));
		thumb.setIntrinsicHeight(65);
		thumb.setIntrinsicWidth(65);
		seekBar.setThumb(thumb);

		text = new TextView(activity);
		text.setText("Value: " + getSliderDefault() + "%");
		text.setLayoutParams(new LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT));
		text.setGravity(Gravity.CENTER);

		viewGroup.addView(seekBarLinearLayout);
		viewGroup.addView(text, params);
		seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

			public void onStopTrackingTouch(SeekBar seekBar) {
			}

			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				text.setText("Value: " + String.valueOf(progress) + "%");
			}
		});

		return viewGroup;
	}

	@Override
	public boolean validate() {
		return required ? (!(getValue() == null) ? true : false) : true;
	}

	@Override
	public void save(View answer) {
		SeekBar seek = (SeekBar) answer.findViewById(seekBarId);
		value = seek.getProgress();
	}

	public Integer getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(Integer maxValue) {
		this.maxValue = maxValue;
	}

	public TextView getText() {
		return text;
	}

	public void setText(TextView text) {
		this.text = text;
	}

	public int getSeekBarId() {
		return seekBarId;
	}

	public Integer getSliderDefault() {
		try {
			return Integer.parseInt(_default);
		} catch (NumberFormatException e) {
			Log.e("Slider", "Error parsing");
			return 0;
		}
	}
}