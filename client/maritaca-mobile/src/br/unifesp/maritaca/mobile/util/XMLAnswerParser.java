package br.unifesp.maritaca.mobile.util;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlSerializer;

import android.util.Xml;
import br.unifesp.maritaca.mobile.dataaccess.model.MaritacaAnswer;
import br.unifesp.maritaca.mobile.dataaccess.model.MaritacaGroup;
import br.unifesp.maritaca.mobile.model.components.ComponentType;

/**
 * 
 * @author Alvaro Mamani Aliaga
 * @author Jimmy Valverde S&aacute;nchez
 * @version 0.1.6
 * 
 */
public class XMLAnswerParser {
	
	private boolean isNotEmpty(String value) {
		if (value == null || "".equals(value))
			return false;
		return true;
	}
	
	public Map<String, String> getAnswerMap(MaritacaGroup group, String formId){
		Map<String, String> map = new HashMap<String, String>();
		try {
			JSONObject jsonObj = new JSONObject();
			XmlSerializer serializer = Xml.newSerializer();
			StringWriter writer = new StringWriter();
			serializer.setOutput(writer);
			serializer.startDocument("UTF-8", true);
			serializer.startTag("", Constants.ANSWER_RESPONSE);
			serializer.startTag("", Constants.ANSWER_FORMID);
			serializer.text(formId);
			serializer.endTag("", Constants.ANSWER_FORMID);
			serializer.startTag("", Constants.ANSWER_ANSWERS);

				serializer.startTag("", Constants.ANSWER_ANSWER);
				serializer.attribute("", Constants.XML_TIMESTAMP, "" + group.getTimestamp());
				for (MaritacaAnswer answer : group.getAnswers()) {					
					serializer.startTag("", Constants.ANSWER_QUESTION);
					serializer.attribute("", Constants.XML_ID, answer.getId().toString());
					if(answer.getType().equals(ComponentType.DRAW.getDescription())) {
						serializer.attribute("", Constants.XML_TYPE, ComponentType.PICTURE.getDescription());
					}
					else {
						serializer.attribute("", Constants.XML_TYPE, answer.getType());
						if(answer.getType().equals(ComponentType.BARCODE.getDescription()) || 
								answer.getType().equals(ComponentType.MONEY.getDescription())) {
							serializer.attribute("", Constants.XML_SUBTYPE, isNotEmpty(answer.getSubtype()) ? answer.getSubtype() : "");
						}
					}
					serializer.startTag("", Constants.XML_VALUE);
					if ((answer.getType().equals(ComponentType.PICTURE.getDescription()) || 
							answer.getType().equals(ComponentType.DRAW.getDescription())) && isNotEmpty(answer.getValue())) {
						try {
							String fileName = getFileName(answer.getValue());
							jsonObj.put(fileName, answer.getValue());
							serializer.text(fileName);
						} catch (JSONException e) {
							throw new RuntimeException(e);
						}						
					}
					else if(answer.getType().equals(ComponentType.AUDIO.getDescription()) && isNotEmpty(answer.getValue())) {
						try {
							String fileName = getFileName(answer.getValue());
							jsonObj.put(fileName, answer.getValue());
							serializer.text(fileName);
						} catch (JSONException e) {
							throw new RuntimeException(e);
						}
					}
					else if(answer.getType().equals(ComponentType.VIDEO.getDescription()) && isNotEmpty(answer.getValue())) {
						try {
							String fileName = getFileName(answer.getValue());
							jsonObj.put(fileName, answer.getValue());
							serializer.text(fileName);
						} catch (JSONException e) {
							throw new RuntimeException(e);
						}
					}
					else {
						serializer.text(isNotEmpty(answer.getValue()) ? answer.getValue() : "");
					}
					serializer.endTag("", Constants.XML_VALUE);
					serializer.endTag("", Constants.ANSWER_QUESTION);
				}
				serializer.endTag("", Constants.ANSWER_ANSWER);

			serializer.endTag("", Constants.ANSWER_ANSWERS);
			serializer.endTag("", Constants.ANSWER_RESPONSE);
			serializer.endDocument();
			
			map.put(Constants.MAP_XML, writer.toString());
			map.put(Constants.MAP_FILES, jsonObj.toString());
			
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		} catch (IllegalStateException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}		
		
		return map;
	}
	
	private String getFileName(String filePath){
		String[] tokens = filePath.split("/");
		int size = tokens.length;
        if(size > 0){
        	return tokens[size-1];
        }
        return null;		
	}
}