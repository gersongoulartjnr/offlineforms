package br.unifesp.maritaca.analytics.components;

import br.unifesp.maritaca.analytics.util.ConstantsPipeline;

import com.mongodb.BasicDBObject;

public class SimpleOperators {
	/*
	 * 
	 * Integer; Float; Date (ISODate).
	 */

	// x > value
	public static BasicDBObject getGT(String identifier, Object value) {
		BasicDBObject greater = new BasicDBObject(ConstantsPipeline.GT, value);
		BasicDBObject field = new BasicDBObject(identifier, greater);
		return new BasicDBObject(ConstantsPipeline.MATCH, field);
	}

	// x >= value
	public static BasicDBObject getGTE(String identifier, Object value) {
		BasicDBObject greaterEqual = new BasicDBObject(ConstantsPipeline.GTE,
				value);
		BasicDBObject field = new BasicDBObject(identifier, greaterEqual);
		return new BasicDBObject(ConstantsPipeline.MATCH, field);
	}

	// x < value
	public static BasicDBObject getLT(String identifier, Object value) {
		BasicDBObject less = new BasicDBObject(ConstantsPipeline.LT, value);
		BasicDBObject field = new BasicDBObject(identifier, less);
		return new BasicDBObject(ConstantsPipeline.MATCH, field);
	}

	// x <= value
	public static BasicDBObject getLTE(String identifier, Object value) {
		BasicDBObject lessEqual = new BasicDBObject(ConstantsPipeline.LTE,
				value);
		BasicDBObject field = new BasicDBObject(identifier, lessEqual);
		return new BasicDBObject(ConstantsPipeline.MATCH, field);
	}

	// x == value
	public static BasicDBObject getEqual(String identifier, Object value) {
		BasicDBObject equal = new BasicDBObject(ConstantsPipeline.EQ, value);
		BasicDBObject field = new BasicDBObject(identifier, equal);
		return new BasicDBObject(ConstantsPipeline.MATCH, field);
	}

	// x != value
	public static BasicDBObject getNE(String identifier, Object value) {
		BasicDBObject notEqual = new BasicDBObject(ConstantsPipeline.NE, value);
		BasicDBObject field = new BasicDBObject(identifier, notEqual);
		return new BasicDBObject(ConstantsPipeline.MATCH, field);
	}
}