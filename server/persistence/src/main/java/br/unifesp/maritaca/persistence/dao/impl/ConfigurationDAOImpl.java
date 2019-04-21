package br.unifesp.maritaca.persistence.dao.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import br.unifesp.maritaca.persistence.dao.AbstractDAO;
import br.unifesp.maritaca.persistence.dao.ConfigurationDAO;
import br.unifesp.maritaca.persistence.entity.Configuration;

@Service("configurationDAO")
public class ConfigurationDAOImpl extends AbstractDAO implements ConfigurationDAO {
	
	public Configuration getSuperUserByName() {
		List<Configuration> confs = emHector.cQuery(Configuration.class, "name", "root");
		if(!confs.isEmpty() && confs.size() > 0)
			return confs.get(0);
		return null;
	}
	
	public Boolean isRootUser(UUID userKey) {
		Configuration configuration = this.getSuperUserByName();
		if(configuration != null && configuration.getValue().toString().equals(userKey.toString())) {
			return  true;
		}
		return false;
	}
	
	public String getValueByName(String name) {
		List<Configuration> confs = emHector.cQuery(Configuration.class, "name", name);
		if(confs != null && !confs.isEmpty())
			return confs.get(0).getValue();
		return null;
	}
}