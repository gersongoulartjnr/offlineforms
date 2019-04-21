package br.unifesp.maritaca.persistence.converter;

import me.prettyprint.hom.PropertyMappingDefinition;
import me.prettyprint.hom.converters.Converter;

public class LongConverter  implements Converter<Long> {

	@Override
	public Long convertCassTypeToObjType(PropertyMappingDefinition md,
			byte[] value) {
		return Long.parseLong(new String(value));
	}

	@Override
	public byte[] convertObjTypeToCassType(Long value) {
		return value.toString().getBytes();
	}

}
