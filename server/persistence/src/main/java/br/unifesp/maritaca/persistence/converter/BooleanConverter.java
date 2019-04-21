package br.unifesp.maritaca.persistence.converter;

import me.prettyprint.hom.PropertyMappingDefinition;
import me.prettyprint.hom.converters.Converter;

public class BooleanConverter implements Converter<Boolean> {

	@Override
	public Boolean convertCassTypeToObjType(PropertyMappingDefinition arg0,
			byte[] value) {
		return Boolean.parseBoolean(new String(value));
	}

	@Override
	public byte[] convertObjTypeToCassType(Boolean value) {
		return value.toString().getBytes();
	}
}