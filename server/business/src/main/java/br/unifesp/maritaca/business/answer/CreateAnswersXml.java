package br.unifesp.maritaca.business.answer;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.google.gson.Gson;

import br.unifesp.maritaca.business.answer.dto.AnswerDTO;
import br.unifesp.maritaca.business.answer.dto.AnswerListerDTO;
import br.unifesp.maritaca.business.answer.dto.QuestionAnswerDTO;
import br.unifesp.maritaca.business.enums.ComponentType;

public class CreateAnswersXml {
	
	public final static String XML_COLLECT	= "collect";
	public final static String XML_ANSWERS	= "answers";
	public final static String XML_ANSWER		= "answer";
	public final static String XML_USER		= "user";
	public final static String XML_CREATIONDATE = "creationDate";
	public final static String XML_QUESTIONS	= "questions";
	public final static String XML_QUESTION	= "question";
	public final static String XML_ID			= "id";
	public final static String XML_TITLE		= "title";
	public final static String XML_VALUE		= "value";
	public final static String XML_OPTION		= "option";
	public final static String XML_LATITUDE	= "latitude";
	public final static String XML_LONGITUDE	= "longitude";

	public static String getAnswersXML(AnswerListerDTO wrapper){
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			
			Document doc = docBuilder.newDocument();
			Element root = doc.createElement(XML_COLLECT);
			doc.appendChild(root);
			
			Element questions = doc.createElement(XML_QUESTIONS);
			root.appendChild(questions);
			
			Element question = doc.createElement(XML_QUESTION);
			questions.appendChild(question);

			for(String s : wrapper.getQuestions()){
				Element title = doc.createElement(XML_TITLE);
				title.appendChild(doc.createTextNode(s));
				question.appendChild(title);
			}
			
			Element answers = doc.createElement(XML_ANSWERS);
			root.appendChild(answers);
			
			for(AnswerDTO ans : wrapper.getAnswers()){			
				Element answer = doc.createElement(XML_ANSWER);
				answers.appendChild(answer);

				Element user = doc.createElement(XML_USER);
				user.appendChild(doc.createTextNode(ans.getAuthor()));
				answer.appendChild(user);
				Element creationDate = doc.createElement(XML_CREATIONDATE);
				creationDate.appendChild(doc.createTextNode(ans.getStrCreationDate()));
				answer.appendChild(creationDate);
				
				for(QuestionAnswerDTO qa : ans.getAnswers()){
					if(qa.getType().equals(ComponentType.CHECKBOX.getValue()) || qa.getType().equals(ComponentType.COMBOBOX.getValue()) || 
        					qa.getType().equals(ComponentType.RADIOBOX.getValue())){
						Element v = doc.createElement(XML_VALUE);
    					answer.appendChild(v);
    					
        				HashMap<String, String> jsonValues = (new Gson()).fromJson(qa.getValue(), HashMap.class);
        				for (Map.Entry<String, String> entry : jsonValues.entrySet()) {
        					Element o = doc.createElement(XML_OPTION);
        					o.appendChild(doc.createTextNode(entry.getValue()));
        					v.appendChild(o);
        				}
        			} 
					else if(qa.getType().equals(ComponentType.GEOLOCATION.getValue()) && qa.getValue() != null && !"".equals(qa.getValue())){
						Element v = doc.createElement(XML_VALUE);
    					answer.appendChild(v);
    					
						String[] tokens = qa.getValue().split(";");
						Element lat = doc.createElement(XML_LATITUDE);
    					lat.appendChild(doc.createTextNode(tokens[0]));
    					v.appendChild(lat);
    					
    					Element lon = doc.createElement(XML_LONGITUDE);
    					lon.appendChild(doc.createTextNode(tokens[1]));
    					v.appendChild(lon);						
					} else {
        				Element v = doc.createElement(XML_VALUE);
    					v.appendChild(doc.createTextNode(qa.getValue()));
    					answer.appendChild(v);
        			}
				}
			}
			StringWriter writer = new StringWriter();
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			transformer.transform(source, new StreamResult(writer));
			return writer.toString();			
		}catch (TransformerConfigurationException e) {
			e.printStackTrace();
		}
		catch (TransformerException e) {
			e.printStackTrace();
		}
		catch (ParserConfigurationException e) {
			e.printStackTrace();
		}		
		return null;	
	}	
}