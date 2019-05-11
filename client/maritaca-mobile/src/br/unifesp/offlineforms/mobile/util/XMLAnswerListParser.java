package br.unifesp.offlineforms.mobile.util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import br.unifesp.offlineforms.mobile.model.components.ComponentType;

public class XMLAnswerListParser {
	
	private String [][] questionAnswers;
	private String []   users;
	private String []   dates;
	
	public void parse(String answerXml){				
		try {			
			InputStream is = new ByteArrayInputStream(answerXml.getBytes(Constants.M_ENCODING));
			
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder        dBuilder  = dbFactory.newDocumentBuilder();
			Document               doc       = dBuilder.parse(is);			
			doc.getDocumentElement().normalize();
			
			Element   rootElement       = doc.getDocumentElement();			
			NodeList  answersQuestionNL = rootElement.getChildNodes();			
			NodeList  answersNL         = answersQuestionNL.item(0).getChildNodes();						
			String[][]parsedAnswers     = new String[answersNL.getLength()][];
			String[]  users             = new String[answersNL.getLength()];
			String[]  dates             = new String[answersNL.getLength()];
			
			for(int i=0; i<answersNL.getLength(); i++){
				parsedAnswers[i] = parseQuestionAnswers(answersNL.item(i).getChildNodes());
				users[i]         = parseEmail(answersNL.item(i));
				dates[i]         = parseDate(answersNL.item(i));
			}
			setQuestionAnswers(parsedAnswers);
			setUsers(users);
			setDates(dates);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}		
	}
	
	private String parseDate(Node answerNode) {
		return parseAttribute(answerNode, Constants.ANSW_LIST_DATE);
	}

	private String parseEmail(Node answerNode) {
		return parseAttribute(answerNode, Constants.ANSW_LIST_EMAIL);
	}
	
	private String parseAttribute(Node node, String attribName){
		if(!node.hasAttributes()){
			return null;
		}
		if(node.getAttributes().getNamedItem(attribName) == null){
			return null;
		}				
		return node.getAttributes().getNamedItem(attribName).getNodeValue();
	}

	private String[] parseQuestionAnswers(NodeList questionAnswerNL) {
		String[] questionAnswers = new String[questionAnswerNL.getLength()];
		Node node;
		for(int j=0; j<questionAnswerNL.getLength(); j++){
			if(questionAnswerNL.item(j)!=null && questionAnswerNL.item(j).getFirstChild()!=null){
				Element element = (Element) questionAnswerNL.item(j);
				node = questionAnswerNL.item(j).getFirstChild();
				
				if(element.getAttribute(Constants.XML_TYPE).equals(ComponentType.CHECKBOX.getDescription()) || 
						element.getAttribute(Constants.XML_TYPE).equals(ComponentType.RADIOBOX.getDescription()) || 
								element.getAttribute(Constants.XML_TYPE).equals(ComponentType.COMBOBOX.getDescription())){
					if(node.getFirstChild() != null){
						try {
							JSONObject jsonObj = new JSONObject(node.getFirstChild().getNodeValue());
	            			Iterator it = jsonObj.keys();
	            			String str = "";
	            			while (it.hasNext()) {
	            				String key = (String) it.next();
	            				str = jsonObj.getString(key) + ";";
	            			}
	            			if(str.endsWith(";")){
	            				questionAnswers[j] = str.substring(0, str.length()-1);
	            			}
						} catch(Exception ex) {
							throw new RuntimeException();
						}
					} else{
						questionAnswers[j] = "";
					}
					
				} else{
					questionAnswers[j] = node.getFirstChild() != null ? node.getFirstChild().getNodeValue() : "";					
				}
			}
		}
		return questionAnswers;
	}

	public void printDocNode(NodeList nodeList){
		if(nodeList==null){
			return;
		}
		for(int i=0; i<nodeList.getLength(); i++){
			Node node = nodeList.item(i);
			printDocNode(node.getChildNodes());
		}		
	}

	public String [][] getQuestionAnswers() {
		return questionAnswers;
	}
	public void setQuestionAnswers(String [][] questionAnswers) {
		this.questionAnswers = questionAnswers;
	}

	public String [] getUsers() {
		return users;
	}
	public void setUsers(String [] users) {
		this.users = users;
	}

	public String [] getDates() {
		return dates;
	}
	public void setDates(String [] dates) {
		this.dates = dates;
	}	
}