package br.unifesp.maritaca.business.answer;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import br.unifesp.maritaca.business.enums.ComponentType;
import br.unifesp.maritaca.business.exception.MaritacaException;

public class FormXmlParser {

	private static final String VALUE = "value";
	private static final String OPTION = "option";

	/**
	 * Parse the question labels of the given form XML.
	 * @param formXml
	 * @return
	 * @author tiagobarabasz
	 */
	public static List<String> parseQuestionsLabels(String formXml) {
		List<String> questions = new ArrayList<String>();		
		Document     document  = xmlToDocument(formXml);		
		NodeList     list      = questionNodes(document);

		for (int i = 0; i < list.getLength(); i++) {
			Node node = list.item(i);
			String question = questionLabelFromNode(node);
			questions.add(question);
		}
		return questions;
	}

	/**
	 * Parse the answers to the questions of the given answer XML.
	 * @param formXml
	 * @return
	 * @author tiagobarabasz
	 */
	public static List<String> parseAnswers(String answerXml){
		List<String> answers  = new ArrayList<String>();
		Document     document = xmlToDocument(answerXml);
		NodeList     list     = answerNodes(document);
		
		for (int i = 0; i < list.getLength(); i++) {
			Node node = list.item(i);
			String answer = answerFromNode(node);
			answers.add(answer);
		}		
		return answers;
	}
	

	private static String answerFromNode(Node node) {
		return node.getFirstChild().getFirstChild().getNodeValue();
	}

	private static NodeList answerNodes(Document document) {
		return document.getFirstChild().getFirstChild().getNextSibling().getNextSibling().getFirstChild().getChildNodes();
	}

	private static String questionLabelFromNode(Node node) {
		return node.getFirstChild().getFirstChild().getNodeValue();
	}

	private static NodeList questionNodes(Document document) {
		return document.getFirstChild().getFirstChild().
				getNextSibling().getChildNodes();
	}

	//TODO Change to private when the RandomAnswerCreator is eliminated
	protected static Document xmlToDocument(String formXml) {
		try {						 			 			 						
			DocumentBuilderFactory factory  = DocumentBuilderFactory.newInstance();
			DocumentBuilder        dBuilder = factory.newDocumentBuilder();			
			InputSource            inStream = new InputSource(); 
			
			inStream.setCharacterStream(new StringReader(formXml));
			
			Document               document = dBuilder.parse(inStream);
			document.getDocumentElement().normalize();

			return document;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static HashMap<String, String> getValuesFromMultiChoiceQuestion(String formXml, int questionId) {
		Document     document  = xmlToDocument(formXml);		
		NodeList     list      = questionNodes(document);

		Node node = list.item(questionId);
		List<String> multiChoiceComponents = new ArrayList<String>();
		multiChoiceComponents.add(ComponentType.CHECKBOX.getValue());
		multiChoiceComponents.add(ComponentType.RADIOBOX.getValue());
		multiChoiceComponents.add(ComponentType.COMBOBOX.getValue());
		
		if (node == null || !multiChoiceComponents.contains(node.getNodeName())) {
			throw new MaritacaException("Invalid questionId OR Current question is not multichoice");
		}
		
		HashMap<String, String> keyValues = new HashMap<String, String>();
		NodeList childNodes = node.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); i++) {
			if (OPTION.equals(childNodes.item(i).getNodeName())) {
				String nodeValue = childNodes.item(i).getFirstChild().getNodeValue();
				NamedNodeMap attributes = childNodes.item(i).getAttributes();
				keyValues.put(attributes.getNamedItem(VALUE).getNodeValue(), nodeValue);
			}
		}
		return keyValues;
	}
}
