package br.unifesp.offlineforms.mobile.model.components;

import br.unifesp.offlineforms.mobile.activities.R;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomDialog extends Dialog implements OnClickListener {

	private Context context;
	private Class<?> goTo;
	private Dialog dialog;
	private Button button;

	/**
	 * Create a dialog window that use the default frame style.
	 * @param context
	 * @param dialogType
	 * @param title
	 * @param msgText
	 * @param btnText
	 */
	public CustomDialog(Context context, Class<?> goTo, DialogType dialogType, String title, String msgText, String btnText) {
		super(context);
		this.context = context;
		this.goTo = goTo;
		this.createDialog(context, goTo, dialogType, title, msgText, btnText);		
	}
	
	private void createDialog(Context context, Class<?> goTo, DialogType dialogType, String title, String msgText, String btnText) {
		if(dialogType == null || msgText == null || "".equals(msgText))
			return;
		
		dialog = new Dialog(context);
		if(title == null || "".equals(title)) {
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); 
		}
		else {
			dialog.setTitle(title); 
		}
		
		dialog.setContentView(R.layout.custom_dialog);
		
		TextView textView = (TextView)dialog.findViewById(R.id.dlg_text);
		textView.setText(msgText);
		
		ImageView imageView = (ImageView)dialog.findViewById(R.id.dlg_image);
		if(dialogType == DialogType.INFO) { 
			imageView.setImageResource(R.drawable.info); 
		}
		else if(dialogType == DialogType.WARN) {
			imageView.setImageResource(R.drawable.warn);
		}
		else if(dialogType == DialogType.ERROR) {
			imageView.setImageResource(R.drawable.error);
		}
		
		button = (Button)dialog.findViewById(R.id.dlg_button);
		if(btnText!= null && !"".equals(btnText)) {
			button.setText(btnText);
		}
		else {
			button.setText(R.string.button_close); 
		}
		
		button.setOnClickListener(this);
		dialog.show();
	}

	@Override
	public void onClick(View v) {
		if(v == button) {
			dialog.dismiss();
			if(this.goTo != null && context instanceof Activity) {
				Intent intent = new Intent(context, goTo);				
				this.context.startActivity(intent);
				//((Activity) context).finish();
			}
		}
	}
}