package br.unifesp.maritaca.persistence.converter;

import java.lang.reflect.Type;
import java.util.List;

import me.prettyprint.hom.PropertyMappingDefinition;
import me.prettyprint.hom.converters.Converter;

import br.unifesp.maritaca.persistence.entity.AnswerTimestamp;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class JSONAnswerTimestampConverter implements Converter<List<AnswerTimestamp>>{

	@Override
	public List<AnswerTimestamp> convertCassTypeToObjType(PropertyMappingDefinition md,
			byte[] value) {
		Gson gson = new Gson();
		Type type = new TypeToken<List<AnswerTimestamp>>(){}.getType();
		return gson.fromJson(new String(value), type);
	}

	@Override
	public byte[] convertObjTypeToCassType(List<AnswerTimestamp> value) {
		Gson gson = new Gson();
		return gson.toJson(value).getBytes();					
	}
}
