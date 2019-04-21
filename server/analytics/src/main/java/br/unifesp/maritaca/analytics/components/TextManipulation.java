package br.unifesp.maritaca.analytics.components;

import java.util.ArrayList;

import br.unifesp.maritaca.analytics.util.ConstantsPipeline;

import com.mongodb.BasicDBObject;

public class TextManipulation {

	/*
	 * Recebe uma ou mais palavras procurando-as de maneira identifica em
	 * question textual
	 */
	public static BasicDBObject getIdenticText(String identifier,
			ArrayList<String> words) {
		BasicDBObject in = new BasicDBObject(ConstantsPipeline.IN, words);
		BasicDBObject idenText = new BasicDBObject(identifier, in);
		BasicDBObject match = new BasicDBObject(ConstantsPipeline.MATCH,
				idenText);
		return match;
	}

	/*
	 * Expressoes regulares trabalham com PCRE - Perl Compatible Regular
	 * Expressions
	 */

	/*
	 * Regular expression very simple without case sensitive
	 */
	public static BasicDBObject getSimpleRegex(String identifier, String regex) {
		BasicDBObject my_regex = new BasicDBObject(ConstantsPipeline.REGEX,
				regex);
		BasicDBObject field = new BasicDBObject(identifier, my_regex);
		BasicDBObject match = new BasicDBObject(ConstantsPipeline.MATCH, field);
		return match;
	}

	/*
	 * Regular expression with case sensitive
	 */
	public static BasicDBObject getRegexCaseSensitive(String identifier,
			String regex) {
		BasicDBObject my_regex = new BasicDBObject(ConstantsPipeline.REGEX,
				regex).append(ConstantsPipeline.OPTIONS, "i");
		BasicDBObject field = new BasicDBObject(identifier, my_regex);
		BasicDBObject match = new BasicDBObject(ConstantsPipeline.MATCH, field);
		return match;
	}
}