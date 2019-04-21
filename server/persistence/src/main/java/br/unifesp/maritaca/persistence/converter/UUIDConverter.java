package br.unifesp.maritaca.persistence.converter;

import java.util.UUID;

import me.prettyprint.hom.PropertyMappingDefinition;
import me.prettyprint.hom.converters.Converter;

public class UUIDConverter implements Converter<UUID>{

	@Override
	public UUID convertCassTypeToObjType(PropertyMappingDefinition arg0, byte[] value) {
		return UUID.fromString(new String(value));
	}

	@Override
	public byte[] convertObjTypeToCassType(UUID value) {
		return value.toString().getBytes();
	}

}
