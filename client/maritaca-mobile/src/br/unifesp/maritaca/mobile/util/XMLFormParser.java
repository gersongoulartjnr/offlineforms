package br.unifesp.maritaca.mobile.util;

import java.io.FileInputStream;
import java.io.InputStream;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import android.content.ContextWrapper;
import android.util.Log;
import br.unifesp.maritaca.mobile.model.Form;

public class XMLFormParser {
	
	private Form form;

	public XMLFormParser(InputStream is) {
		try {

			FileInputStream file = new FileInputStream(Constants.PATH_FORM);
			Serializer serializer = new Persister();
			this.form = serializer.read(Form.class, file);
		} catch (Exception e) {
			Log.e("Error", e.getMessage());
		}
	}
	
	public Form getForm() {
		return form;
	}
}