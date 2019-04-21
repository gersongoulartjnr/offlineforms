package br.unifesp.maritaca.analytics.components;

import java.util.ArrayList;

import br.unifesp.maritaca.analytics.util.ConstantsPipeline;

import com.mongodb.BasicDBObject;

public class GeneralManipulation {

	/* Filtra retornando as primeiras ocorrencias */
	public static BasicDBObject getLimit(Integer value) {
		return value <= 0 ? null : new BasicDBObject(ConstantsPipeline.LIMIT,
				value);
	}

	/*
	 * Similar ao metodo anterior, porem retorna apos value, ex: [1,2,3] skip 2
	 * (remove 2 primeiros) retorna 3
	 */
	public static BasicDBObject getSkip(Integer value) {
		return value <= 0 ? null : new BasicDBObject(ConstantsPipeline.SKIP,
				value);
	}

	/* Filtra pelas respostas em questao do autor */
	public static BasicDBObject getUsers(ArrayList<String> users) {
		BasicDBObject in = new BasicDBObject(ConstantsPipeline.IN, users);
		BasicDBObject author = new BasicDBObject(ConstantsPipeline.AUTHOR, in);
		BasicDBObject match = new BasicDBObject(ConstantsPipeline.MATCH, author);
		return match;
	}

	/*
	 * Recebe o identificador do JSON e se sera crescente: 1 ou decrescente: -1
	 */
	public static BasicDBObject getSorting(String identifier, Integer ascending) {
		BasicDBObject asc = new BasicDBObject(identifier, ascending);
		return new BasicDBObject(ConstantsPipeline.SORT, asc);
	}
}