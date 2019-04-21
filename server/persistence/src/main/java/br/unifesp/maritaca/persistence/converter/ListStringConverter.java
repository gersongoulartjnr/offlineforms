package br.unifesp.maritaca.persistence.converter;

import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import me.prettyprint.hom.PropertyMappingDefinition;
import me.prettyprint.hom.converters.Converter;

public class ListStringConverter implements Converter<List<String>> {

	@Override
	public List<String> convertCassTypeToObjType(PropertyMappingDefinition arg0,
			byte[] value) {
		Gson gson = new Gson();
		Type type = new TypeToken<List<String>>(){}.getType();
		return gson.fromJson(new String(value), type);
	}

	@Override
	public byte[] convertObjTypeToCassType(List<String> value) {
		Gson gson = new Gson();
		return gson.toJson(value).getBytes();	
	}
}