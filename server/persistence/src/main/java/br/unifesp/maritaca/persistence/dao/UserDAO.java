package br.unifesp.maritaca.persistence.dao;

import java.util.List;
import java.util.UUID;

import br.unifesp.maritaca.persistence.entity.User;

public interface UserDAO {
	
	void saveUser(User user);
	
	void updateUser(User user);
	
	User findUserByKey(UUID userKey);
	
	User findUserByEmail(String email);
	
	boolean verifyIfCodeExists(String activationCode);
	
	User findUserByActivationCode(String code);

	void updateUserLoggedBefore(User user);

	List<User> getAllUsers();
}