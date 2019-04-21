package br.unifesp.maritaca.analytics.input;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

import br.unifesp.maritaca.persistence.entity.QuestionAnswer;
import br.unifesp.maritaca.persistence.entity.QuestionAnswerMultimedia;
import br.unifesp.maritaca.persistence.entity.QuestionAnswerSubType;

public class NextAnswers {
	
	private Object value;
	private String id;
	
	public static String changeDate(String date) {
		if (date.compareTo("") == 0 || date == null)
			return null;
		String[] dateSplit = date.split(" ");
		String month = dateSplit[1];
		String year = dateSplit[5];
		String day = dateSplit[2];

		return month + " " + day + ", " + year;
	}
	
	public static String getIndexes(String result) {
		@SuppressWarnings("unchecked")
		HashMap<String, String> map = (new Gson()).fromJson(result, HashMap.class);
		List<String> lst = new ArrayList<String>(map.size());
		for(Map.Entry<String, String> entry: map.entrySet()) {
			lst.add(entry.getKey());
		}
		String temp1 = lst.toString();
		String temp2 = temp1.substring(0, temp1.length() - 1);
		String str = temp2.substring(1, temp2.length());
		
		return str;
		/*String [] pipe = result.split(":");
		String y = pipe[0].replace("{", "");
		y = y.replace("\"", "");
		return y;*/
	}
	
	public NextAnswers(QuestionAnswer answer) {
		String type = answer.getType();
		this.id = answer.getId();
		
		if (answer.getValue() == null || "".equals(answer.getValue())) {
			this.value = null;
		}
		else if(type.equals("text")){
			this.value = answer.getValue();
		} else if(type.equals("date")){
			this.value = new Date(changeDate(answer.getValue()));
		} else if(type.equals("combobox")){
			this.value = getIndexes(answer.getValue());
		} else if(type.equals("radio")){
			this.value = getIndexes(answer.getValue());
		} else if(type.equals("checkbox")){
			this.value = getIndexes(answer.getValue());
		} else if(type.equals("number")){
			this.value = Integer.parseInt(answer.getValue());
		} else if(type.equals("decimal")){
			this.value = Double.parseDouble(answer.getValue());
		} else if(type.equals("money")){
			this.value = Double.parseDouble(answer.getValue());
		} else if(type.equals("picture")){
			ArrayList<String> picture = new ArrayList<String>();			
			QuestionAnswerMultimedia nextMultimidia = (QuestionAnswerMultimedia) answer;
			picture.add(answer.getValue());
			picture.add(nextMultimidia.getThumbnail());
			this.value = picture;
		} else if(type.equals("audio")){
			this.value = answer.getValue();
		} else if(type.equals("video")){
			ArrayList<String> video = new ArrayList<String>();			
			QuestionAnswerMultimedia nextMultimidia = (QuestionAnswerMultimedia) answer;
			video.add(answer.getValue());
			video.add(nextMultimidia.getThumbnail());
			this.value = video;		
		} else if(type.equals("geolocation")){
			String split[] = answer.getValue().split(";");
			if (answer.getValue().length() == 1) {
				this.value = null;
			} else {
				ArrayList<Double> geolocation = new ArrayList<Double>(2);
				geolocation.add(Double.parseDouble(split[0]));
				geolocation.add(Double.parseDouble(split[1]));
				this.value = geolocation;
			}
		} else if(type.equals("barcode")){
			ArrayList<String> barcode = new ArrayList<String>();
			QuestionAnswerSubType subtype = (QuestionAnswerSubType) answer;			
			barcode.add(answer.getValue());
			barcode.add(subtype.getSubtype());
			this.value = barcode;
		} else if(type.equals("slider")){
			this.value = Double.parseDouble(answer.getValue());
		} else if(type.equals("draw")){
			ArrayList<String> draw = new ArrayList<String>();			
			QuestionAnswerMultimedia nextMultimidia = (QuestionAnswerMultimedia) answer;
			draw.add(answer.getValue());
			draw.add(nextMultimidia.getThumbnail());
			this.value = draw;
		} else {
			throw new RuntimeException();
		}
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
}