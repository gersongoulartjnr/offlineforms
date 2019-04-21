package br.unifesp.maritaca.persistence.converter;

import me.prettyprint.hom.PropertyMappingDefinition;
import me.prettyprint.hom.converters.Converter;

public class IntegerConverter implements Converter<Integer> {

	@Override
	public Integer convertCassTypeToObjType(PropertyMappingDefinition arg0,
			byte[] value) {
		return Integer.parseInt(new String(value));
	}

	@Override
	public byte[] convertObjTypeToCassType(Integer value) {
		return value.toString().getBytes();
	}

}
