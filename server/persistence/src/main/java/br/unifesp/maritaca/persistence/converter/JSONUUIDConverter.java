package br.unifesp.maritaca.persistence.converter;

import java.util.List;
import java.util.UUID;
import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import me.prettyprint.hom.PropertyMappingDefinition;
import me.prettyprint.hom.converters.Converter;

public class JSONUUIDConverter implements Converter<List<UUID>>{

	@Override
	public List<UUID> convertCassTypeToObjType(PropertyMappingDefinition md,
			byte[] value) {
		Gson gson = new Gson();
		Type type = new TypeToken<List<UUID>>(){}.getType();
		return gson.fromJson(new String(value), type);
	}

	@Override
	public byte[] convertObjTypeToCassType(List<UUID> value) {
		Gson gson = new Gson();
		return gson.toJson(value).getBytes();					
	}
}
