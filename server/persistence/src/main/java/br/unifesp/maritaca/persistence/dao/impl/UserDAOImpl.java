package br.unifesp.maritaca.persistence.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import br.unifesp.maritaca.persistence.dao.AbstractDAO;
import br.unifesp.maritaca.persistence.dao.UserDAO;
import br.unifesp.maritaca.persistence.entity.GroupsByUser;
import br.unifesp.maritaca.persistence.entity.MaritacaList;
import br.unifesp.maritaca.persistence.entity.User;
import br.unifesp.maritaca.persistence.util.ConstantsPersistence;

@Service("userDAO")
public class UserDAOImpl extends AbstractDAO implements UserDAO {
	
	private static Logger logger = Logger.getLogger(UserDAOImpl.class);
	
	@Override
	public User findUserByKey(UUID userKey) {
		return emHector.find(User.class, userKey);
	}
	
	@Override
	public User findUserByEmail(String email) {
		List<User> users = emHector.cQuery(User.class, "email", email);
		if (users == null || users.size() == 0) {
			return null;
		} else if (users.size() == 1) {
			return users.get(0);
		} else {
			//TODO:
			logger.error(email + " " + users.size());
			return null;
		}
	}
	
	@Override
	public void saveUser(User user) {
		emHector.persist(user);
		this.saveGroupAndUser(user);
	}
	
	@Override
	public void updateUser(User user) {
		emHector.persist(user);
	}
	
	private void saveGroupAndUser(User owner) {
		// saving Group (owner group)
		MaritacaList group = new MaritacaList();
		group.setKey(owner.getKey());
		group.setOwner(owner.getKey());
		group.setName(owner.getEmail());
		emHector.persist(group);
		// saving the GroupByUser
		GroupsByUser gbu = new GroupsByUser();
		gbu.setKey(owner.getKey());
		List<UUID> groups = new ArrayList<UUID>();
		groups.add(owner.getKey());
		gbu.setGroups((groups));
		emHector.persist(gbu);
		// Updating user MaritacaList. This will be deleted
		owner.setMaritacaList(group.getKey());
		emHector.persist(owner);		
	}
	
	@Override
	public boolean verifyIfCodeExists(String activationCode) {
		List<User> usersList = emHector.cQuery(User.class, "activationCode", activationCode, true);
		return usersList.size() > 0;
	}

	@Override
	public User findUserByActivationCode(String code) {
		List<User> usersList = emHector.cQuery(User.class, "activationCode", code, false);
		if (usersList == null || usersList.size() == 0) {
			return null;
		} else if (usersList.size() == 1) {
			return usersList.get(0);
		} else {
			//TODO:
			logger.error(" " + code);
			return null;
		}
	}

	@Override
	public void updateUserLoggedBefore(User user) {
		if(user.getLoggedBefore()==null || user.getLoggedBefore().equals((ConstantsPersistence.NEVER_LOGGED))){
			user.setLoggedBefore(ConstantsPersistence.ALREADY_LOGGED);
			updateUser(user);
		}		
	}

	@Override
	public List<User> getAllUsers() {
		return emHector.listAll(User.class);
	}
}