package br.unifesp.maritaca.analytics.components;

import com.mongodb.BasicDBObject;

public class MathManipulation {
	
	public static BasicDBObject getSummation(String identifier) {
		BasicDBObject query = new BasicDBObject();
		query.put("_id", "$_maritaca_folder");
		BasicDBObject sum = new BasicDBObject("$sum", "$"+identifier);
		query.put("result", sum);
		BasicDBObject group = new BasicDBObject("$group", query);
		return group;
	}
}