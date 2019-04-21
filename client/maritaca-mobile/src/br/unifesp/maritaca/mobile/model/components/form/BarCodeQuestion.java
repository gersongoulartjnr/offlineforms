package br.unifesp.maritaca.mobile.model.components.form;

import jim.h.common.android.zxinglib.integrator.IntentIntegrator;

import org.simpleframework.xml.Root;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import br.unifesp.maritaca.mobile.activities.ControllerActivity;
import br.unifesp.maritaca.mobile.activities.R;
import br.unifesp.maritaca.mobile.model.Comparison;
import br.unifesp.maritaca.mobile.model.Question;
import br.unifesp.maritaca.mobile.model.components.BarCodeInformation;
import br.unifesp.maritaca.mobile.model.components.ComponentType;

/**
 * 
 * @author Bruno G. Santos
 * @version 0.1.7
 * 
 */

@Root(name = "barcode")
public class BarCodeQuestion extends Question {

	private static final long serialVersionUID = 1L;
	private TextView textCode;

	@Override
	public ComponentType getComponentType() {
		return ComponentType.BARCODE;
	}

	@Override
	public Integer getNext() {
		if (getComparisons() == null || getComparisons().size() < 1)
			return next;

		for (Comparison comp : getComparisons()) {
			BarCodeInformation barcode = getValue();
			String value = barcode!=null?barcode.getCode():"";
			if (comp.evaluate(value)) {
				return comp.getGoTo();
			}
		}
		return next;
	}

	@Override
	public BarCodeInformation getValue() {
		return (BarCodeInformation)value;
	}

	@Override
	public View getLayout(final ControllerActivity activity) {
		RelativeLayout layout = new RelativeLayout(activity);
		TableLayout tblLayout = new TableLayout(activity);		
		TableRow firstRow = new TableRow(activity);
		if(getValue() != null){
			TextView lblCode = new TextView(activity);
			lblCode.setText(R.string.button_code);
			textCode = new TextView(activity);
			textCode.setText(getValue().getCode());
			firstRow.addView(lblCode);
			firstRow.addView(textCode);
		}	
		
		TableRow secondRow = new TableRow(activity);
		Button scanButton = new Button(activity);
		scanButton.setText(R.string.button_scan);
		scanButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				IntentIntegrator.initiateScan(activity, R.layout.capture,
						R.id.viewfinder_view, R.id.preview_view, true);
			}
		});
		
		TableLayout.LayoutParams params = new TableLayout.LayoutParams(
				TableLayout.LayoutParams.FILL_PARENT,TableLayout.LayoutParams.FILL_PARENT);
        params.setMargins(20, 0, 0, 0);
		secondRow.addView(scanButton);
		secondRow.setLayoutParams(params);
		tblLayout.addView(firstRow);
		tblLayout.addView(secondRow);
		
		layout.addView(tblLayout);

		return layout;
	}

	@Override
	public boolean validate() {
		if (required) {
			if (getValue() != null)
				return true;
			return false;
		}
		return true;
	}

	@Override
	public void save(View answer) {
		value = getValue();		
	}
}