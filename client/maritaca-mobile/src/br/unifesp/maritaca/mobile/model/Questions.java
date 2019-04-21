package br.unifesp.maritaca.mobile.model;

import java.io.Serializable;
import java.util.List;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.ElementListUnion;
import org.simpleframework.xml.Root;

import br.unifesp.maritaca.mobile.model.components.form.AudioQuestion;
import br.unifesp.maritaca.mobile.model.components.form.BarCodeQuestion;
import br.unifesp.maritaca.mobile.model.components.form.CheckBoxQuestion;
import br.unifesp.maritaca.mobile.model.components.form.ComboBoxQuestion;
import br.unifesp.maritaca.mobile.model.components.form.DateQuestion;
import br.unifesp.maritaca.mobile.model.components.form.DecimalQuestion;
import br.unifesp.maritaca.mobile.model.components.form.DrawQuestion;
import br.unifesp.maritaca.mobile.model.components.form.GeoLocationQuestion;
import br.unifesp.maritaca.mobile.model.components.form.MoneyQuestion;
import br.unifesp.maritaca.mobile.model.components.form.NumberQuestion;
import br.unifesp.maritaca.mobile.model.components.form.PictureQuestion;
import br.unifesp.maritaca.mobile.model.components.form.RadioButtonQuestion;
import br.unifesp.maritaca.mobile.model.components.form.SliderQuestion;
import br.unifesp.maritaca.mobile.model.components.form.TextQuestion;
import br.unifesp.maritaca.mobile.model.components.form.VideoQuestion;

/**
 * 
 * @author Bruno G. Santos
 * @version 1.0.1
 * 
 */

@Root(name = "questions")
public class Questions implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@ElementListUnion({
			@ElementList(entry = "text", inline = true, type = TextQuestion.class, required = false),
			@ElementList(entry = "number", inline = true, type = NumberQuestion.class, required = false),
			@ElementList(entry = "decimal", inline = true, type = DecimalQuestion.class, required = false),
			@ElementList(entry = "money", inline = true, type = MoneyQuestion.class, required = false),
			@ElementList(entry = "barcode", inline = true, type = BarCodeQuestion.class, required = false),
			@ElementList(entry = "audio", inline = true, type = AudioQuestion.class, required = false),
			@ElementList(entry = "geolocation", inline = true, type = GeoLocationQuestion.class, required = false),
			@ElementList(entry = "picture", inline = true, type = PictureQuestion.class, required = false),
			@ElementList(entry = "video", inline = true, type = VideoQuestion.class, required = false),
			@ElementList(entry = "checkbox", inline = true, type = CheckBoxQuestion.class, required = false),
			@ElementList(entry = "radio", inline = true, type = RadioButtonQuestion.class, required = false),
			@ElementList(entry = "combobox", inline = true, type = ComboBoxQuestion.class, required = false),
			@ElementList(entry = "slider", inline = true, type = SliderQuestion.class, required = false),
			@ElementList(entry = "date", inline = true, type = DateQuestion.class, required = false),
			@ElementList(entry = "draw", inline = true, type = DrawQuestion.class, required = false)
	})
	private List<Question> questions;

	public List<Question> getQuestions() {
		return questions;
	}

	public void setQuestions(List<Question> questions) {
		this.questions = questions;
	}
}