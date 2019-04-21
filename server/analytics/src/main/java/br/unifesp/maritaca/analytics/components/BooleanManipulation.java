package br.unifesp.maritaca.analytics.components;

import java.util.ArrayList;

import br.unifesp.maritaca.analytics.util.ConstantsPipeline;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;

public class BooleanManipulation {

	/*
	 * Recebe o campo de identificacao e uma lista de valores * lista contendo:
	 * 1,2,3,4,5 ou "a", "b", "c" etc.
	 */
	public static BasicDBObject getOR(String identifier,
			ArrayList<Object> values) {

		BasicDBList list = new BasicDBList();

		for (Object obj : values) {
			BasicDBObject next = new BasicDBObject(identifier, obj);
			list.add(next);
		}
		BasicDBObject or = new BasicDBObject(ConstantsPipeline.OR, list);
		return new BasicDBObject(ConstantsPipeline.MATCH, or);
	}

	/*
	 * Recebe o campo de identificacao e uma lista de valores * lista contendo:
	 * 1,2,3,4,5 ou "a", "b", "c" etc.
	 */
	public static BasicDBObject getAND(String identifier,
			ArrayList<Object> values) {

		BasicDBList list = new BasicDBList();

		for (Object obj : values) {
			BasicDBObject next = new BasicDBObject(identifier, obj);
			list.add(next);
		}
		BasicDBObject and = new BasicDBObject(ConstantsPipeline.AND, list);
		return new BasicDBObject(ConstantsPipeline.MATCH, and);
	}

	/*
	 * Recebe o campo de identificacao e uma lista de valores * lista contendo:
	 * 1,2,3,4,5 ou "a", "b", "c" etc.
	 */
	public static BasicDBObject getNOT(String identifier,
			ArrayList<Object> values) {

		BasicDBList list = new BasicDBList();

		for (Object obj : values) {
			BasicDBObject next = new BasicDBObject(identifier, obj);
			list.add(next);
		}
		BasicDBObject not = new BasicDBObject(ConstantsPipeline.NOT, list);
		System.out.println(new BasicDBObject(ConstantsPipeline.MATCH, not));
		return new BasicDBObject(ConstantsPipeline.MATCH, not);
	}
}