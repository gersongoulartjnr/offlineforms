package br.unifesp.maritaca.mobile.model;

import java.io.Serializable;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import br.unifesp.maritaca.mobile.model.Questions;

/**
 * 
 * @author Bruno G. Santos
 * @version 1.0.1
 * 
 */

@Root(name = "form")
public class Form implements Serializable {

	private static final long serialVersionUID = 1L;

	@Element(name = "title")
	private String title;

	@Element(name = "questions", required = false)
	private Questions questions;

	public Questions getQuestions() {
		return questions;
	}

	public void setQuestions(Questions questions) {
		this.questions = questions;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}