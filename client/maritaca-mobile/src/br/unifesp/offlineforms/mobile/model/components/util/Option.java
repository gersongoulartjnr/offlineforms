package br.unifesp.offlineforms.mobile.model.components.util;

import java.io.Serializable;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;

@Root(name = "option")
public class Option implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;

	@Text(required = false)
	private String text;

	@Attribute(name = "checked", required = false)
	private boolean checked;
	
	@Attribute(required = false)
	private String value;

	public Option() { }
	
	public Option(Integer id, String text, boolean checked, String value) {
		this.id = id;
		this.text = text;
		this.checked = checked;
		this.value = value;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public void toggleChecked() {
		checked = !checked;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}