package br.unifesp.maritaca.analytics.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.unifesp.maritaca.analytics.MongoAnalytics;
import br.unifesp.maritaca.analytics.components.GPSManipulation;
import br.unifesp.maritaca.analytics.components.GeneralManipulation;
import br.unifesp.maritaca.analytics.components.SimpleOperators;
import br.unifesp.maritaca.analytics.enums.ComponentType;
import br.unifesp.maritaca.analytics.input.AnswerJSON;
import br.unifesp.maritaca.analytics.input.NextAnswers;
import br.unifesp.maritaca.analytics.json.AElement;
import br.unifesp.maritaca.analytics.json.AFilter;
import br.unifesp.maritaca.analytics.json.ATransformation;
import br.unifesp.maritaca.analytics.json.DataAnalytics;
import br.unifesp.maritaca.analytics.output.ComboComponent;
import br.unifesp.maritaca.analytics.output.DecimalComponent;
import br.unifesp.maritaca.analytics.output.MoneyComponent;
import br.unifesp.maritaca.analytics.output.NumberComponent;
import br.unifesp.maritaca.analytics.output.PipelineAggregation;
import br.unifesp.maritaca.analytics.output.PolyTransformation;
import br.unifesp.maritaca.analytics.output.SliderComponent;
import br.unifesp.maritaca.analytics.output.TextComponent;
import br.unifesp.maritaca.analytics.util.ConstantsPipeline;
import br.unifesp.maritaca.analytics.util.MongoDriver;
import br.unifesp.maritaca.persistence.dao.AnswerDAO;
import br.unifesp.maritaca.persistence.entity.Answer;
import br.unifesp.maritaca.persistence.entity.QuestionAnswer;

import com.google.gson.Gson;

@Service("mongoAnalytics")
public class MongoAnalyticsImpl implements MongoAnalytics {
	
	@Autowired AnswerDAO answerDAO;
	
	private static Logger logger = Logger.getLogger(MongoAnalyticsImpl.class);
		
	@Override
	public String processData(String mongoHost, String mongoPort, String mongoTimeout, 
			String userKey, String formKey, String formUrl, Integer numberOfCollects, String analyticsKey, String json, int itemId) {
		
		Map<String, String> map = null;
		
		//try {
			MongoDriver mongoDriver = new MongoDriver(mongoHost, mongoPort, mongoTimeout, "maritaca", "mar_".concat(formUrl));
			PipelineAggregation aggregation = new PipelineAggregation(mongoDriver);
			
			List<Answer> answers = answerDAO.findAnswersByFormKey(UUID.fromString(formKey), numberOfCollects, null);
			for(Answer a : answers){
				boolean flag = false;
				map = new HashMap<String, String>();
				
				a = answerDAO.findAnswerByKey(a.getKey());
				AnswerJSON aj = new AnswerJSON(mongoDriver, a.getKey().toString(), formKey.toString(), a.getUserData(), a.getCreationDate(), a.getUrl(), userKey.toString());
				for(QuestionAnswer qa : a.getQuestions()){
					if(!flag){
						map.put(qa.getId(), qa.getType());				
					}
					aj.addAnswer(new NextAnswers(qa));
					flag = true; 
				}
				aj.saveInDB();
			}
			
			DataAnalytics dObj = getDataAnalytics(analyticsKey, json);
			if(dObj.getElements().size() > itemId){				
				dObj.getElements().get(itemId);
				filterTransformations(formUrl, aggregation, dObj.getElements().get(itemId), map);				
				return aggregation.executeOperations().get("result").toString();
			}
		/*} catch (UnknownHostException e) {
			logger.error(e.getMessage());
		}*/
		return null;
	}
	
	private void filterTransformations(String formUrl, PipelineAggregation aggregation, AElement aElement, Map<String, String> map){
		AFilter filter = aElement.getFilter();
		
		if(filter.getGeolocation() != null){
			for(Map.Entry<String, String> m : map.entrySet()){
				if(m.getValue().equals(ComponentType.GEOLOCATION.getDescription())){
					Double[] points = new Double[] {filter.getGeolocation().getLatitude(), filter.getGeolocation().getLongitude()};
					aggregation.addOperation(GPSManipulation.getGPSDistance(aggregation.getDriver(), m.getKey(), filter.getGeolocation().getValue() , new ArrayList<Double>(Arrays.asList(points))));
					break;
				}
			}
		}
		
		if (!filter.getDate().equals("")) {
			DateTimeFormatter format = DateTimeFormat.forPattern(ConstantsPipeline.DATE_FORMAT);
			try {
				DateTime _from = format.parseDateTime(filter.getDate().getFrom());
				DateTime _to = format.parseDateTime(filter.getDate().getTo());
				_to = _to.plusDays(1);
				_to = _to.minusSeconds(1);
			
				aggregation.addOperation(SimpleOperators.getGTE("creationDate", _from.toDate()));
				aggregation.addOperation(SimpleOperators.getLTE("creationDate", _to.toDate()));
			} catch(Exception e){
				//TODO:
			}
		}
		
		if(!filter.getUsers().isEmpty()){
			aggregation.addOperation(GeneralManipulation.getUsers((ArrayList<String>) filter.getUsers()));
		}
		
		for(ATransformation t : aElement.getTransformations()) {
			executeTransformation(aggregation, t);	
		}
		if(!aElement.getSectransformations().isEmpty()){
			if(aElement.getSectransformations().size() > 1){
				logger.error("Sec transformations " + formUrl + " is greater than 1.");
			}
			
			executeTransformation(aggregation, aElement.getSectransformations().get(0));
		}
	}
	
	private void executeTransformation(PipelineAggregation aggregation, ATransformation t){
		PolyTransformation pt = null;
		
		if( (t.getQuestionType().equals(ComponentType.TEXT.getDescription()))){
			pt = new TextComponent();
			pt.setId(Integer.toString(t.getField()));
			pt.setTransformation(t);
			pt.setPipelineAggregation(aggregation);
			pt.createTransformation();				
		}
		
		if (t.getQuestionType().equals(ComponentType.RADIOBOX.getDescription()) || 
				t.getQuestionType().equals(ComponentType.COMBOBOX.getDescription())) {//TODO: CHECKBOX	
			pt = new ComboComponent();
			pt.setId(Integer.toString(t.getField()));
			pt.setTransformation(t);
			pt.setPipelineAggregation(aggregation);
			
		}
		
		else if(t.getQuestionType().equals(ComponentType.NUMBER.getDescription())){		
			pt = new NumberComponent();
			pt.setId(Integer.toString(t.getField()));
			pt.setTransformation(t);
			pt.setPipelineAggregation(aggregation);
			pt.createTransformation();	
		}
		
		else if(t.getQuestionType().equals(ComponentType.DECIMAL.getDescription())){	
			pt = new DecimalComponent();
			pt.setId(Integer.toString(t.getField()));
			pt.setTransformation(t);
			pt.setPipelineAggregation(aggregation);
			pt.createTransformation();					
		}
		
		else if(t.getQuestionType().equals(ComponentType.MONEY.getDescription())){	
			pt = new MoneyComponent();
			pt.setId(Integer.toString(t.getField()));
			pt.setTransformation(t);
			pt.setPipelineAggregation(aggregation);
			pt.createTransformation();					
		}
		
		else if(t.getQuestionType().equals(ComponentType.SLIDER.getDescription())){	
			pt = new SliderComponent();
			pt.setId(Integer.toString(t.getField()));
			pt.setTransformation(t);
			pt.setPipelineAggregation(aggregation);
			pt.createTransformation();					
		}
		
		else if (t.getQuestionType().equals(ComponentType.DATE.getDescription())) {
			pt = new MoneyComponent();
			pt.setId(Integer.toString(t.getField()));
			pt.setTransformation(t);
			pt.setPipelineAggregation(aggregation);
			pt.createTransformation();		
		}
	}
	
	private DataAnalytics getDataAnalytics(String analyticsKey, String jsonDoc){
		try{
			Gson gson = new Gson();
			DataAnalytics obj = gson.fromJson(jsonDoc, DataAnalytics.class);
			return obj;		
		} catch(Exception e){
			logger.error(analyticsKey + " : " +e.getMessage());
			throw new RuntimeException();
		}
	}
}