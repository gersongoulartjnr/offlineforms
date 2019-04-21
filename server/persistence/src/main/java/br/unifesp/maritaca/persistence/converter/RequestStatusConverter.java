package br.unifesp.maritaca.persistence.converter;

import me.prettyprint.hom.PropertyMappingDefinition;
import me.prettyprint.hom.converters.Converter;
import br.unifesp.maritaca.persistence.permission.RequestStatusType;;

public class RequestStatusConverter implements Converter<RequestStatusType>{

	public RequestStatusType convertCassTypeToObjType(PropertyMappingDefinition md,
			byte[] value) {
		return RequestStatusType.getInstance(new String(value));
	}

	@Override
	public byte[] convertObjTypeToCassType(RequestStatusType value) {
		return String.valueOf(value.getId()).getBytes();
	}

}