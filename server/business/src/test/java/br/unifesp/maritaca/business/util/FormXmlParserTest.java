package br.unifesp.maritaca.business.util;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.List;

import org.junit.Test;

import br.unifesp.maritaca.business.answer.FormXmlParser;
import br.unifesp.maritaca.business.exception.MaritacaException;

public class FormXmlParserTest {
	
	private static final String FIRST_QUESTION_LABEL = "label number";
	private static final String FIRST_CHECKBOX_VALUE = "Family";
	private static final String SECOND_CHECKBOX_VALUE = "Person";
	private static final String THIRD_CHECKBOX_VALUE = "With contact";
	private static final String FORM_XML = 
			"<form>" +
				"<title>testForm</title>" +
				"<questions>" +
					"<number id=\"0\" next=\"1\" required=\"true\" min=\"0\" max=\"10\">" + 
						"<label>" + FIRST_QUESTION_LABEL + "</label>" +
						"<help>help number</help>" +
						"<default>0</default>" +
					"</number>" +						
					"<number id=\"1\" next=\"2\" required=\"true\" min=\"0\" max=\"10\">" + 
						"<label>label number</label>" +
						"<help>help number</help>" +
						"<default>0</default>" +
					"</number>" +
					"<checkbox id=\"2\" next=\"-1\" required=\"false\">" +
						"<label>label checkbox</label>" +
						"<help></help>" +
						"<default></default>" +
						"<option value=\"1\" checked=\"false\">" + FIRST_CHECKBOX_VALUE + "</option>" +
						"<option value=\"2\" checked=\"false\">" + SECOND_CHECKBOX_VALUE + "</option>" +
						"<option value=\"3\" checked=\"false\">" + THIRD_CHECKBOX_VALUE + "</option>" +
					"</checkbox>" +
				"</questions>" +
			"</form>";
	
	@Test
	public void testParseQuestionsLabels() {
		List<String> titles = FormXmlParser.parseQuestionsLabels(FORM_XML);
		assertEquals(titles.get(0), FIRST_QUESTION_LABEL);
	}
	
	@Test(expected=MaritacaException.class)
	public void testGetValuesFromMultiChoiceQuestionInvalidId() {
		FormXmlParser.getValuesFromMultiChoiceQuestion(FORM_XML, 3);
	}
	
	@Test(expected=MaritacaException.class)
	public void testGetValuesFromMultiChoiceQuestionInvalidQuestion() {
		FormXmlParser.getValuesFromMultiChoiceQuestion(FORM_XML, 1);
	}
	
	@Test
	public void testGetValuesFromMultiChoiceQuestion() {
		HashMap<String, String> values = FormXmlParser.getValuesFromMultiChoiceQuestion(FORM_XML, 2);
		assertEquals(values.get("1"), FIRST_CHECKBOX_VALUE);
		assertEquals(values.get("2"), SECOND_CHECKBOX_VALUE);
		assertEquals(values.get("3"), THIRD_CHECKBOX_VALUE);
	}
	
}
