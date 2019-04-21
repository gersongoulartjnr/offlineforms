package br.unifesp.maritaca.persistence.dao;

import java.util.UUID;

import br.unifesp.maritaca.persistence.entity.Configuration;

public interface ConfigurationDAO {
	
	public Configuration getSuperUserByName();
	
	public Boolean isRootUser(UUID userKey);
	
	public String getValueByName(String name);
}