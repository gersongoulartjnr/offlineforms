package br.unifesp.maritaca.persistence.converter;

import br.unifesp.maritaca.persistence.permission.Policy;

import me.prettyprint.hom.PropertyMappingDefinition;
import me.prettyprint.hom.converters.Converter;

public class PolicyConverter implements Converter<Policy>{

	@Override
	public Policy convertCassTypeToObjType(PropertyMappingDefinition md,
			byte[] value) {
		return Policy.getInstance(new String(value));
	}

	@Override
	public byte[] convertObjTypeToCassType(Policy value) {
		return String.valueOf(value.getId()).getBytes();
	}

}
