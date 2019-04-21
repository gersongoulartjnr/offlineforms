package br.unifesp.maritaca.business.account;

import br.unifesp.maritaca.business.account.dto.AccountDTO;
import br.unifesp.maritaca.business.base.dto.Message;
import br.unifesp.maritaca.business.base.dto.UserDTO;

/**
 * 
 * @author Jimmy Valverde Sanchez
 * @author Tiago Barabasz
 *
 */
public interface ManagementAccount {
	
	/**
	 * Create a new account(Server side)
	 * @param accountDTO User data of the new user
	 * @return
	 */
	public Message createAccount(AccountDTO accountDTO);
	
	/**
	 * Create a new account(Mobile side)
	 * @param accountDTO
	 * @return
	 */
	public Message saveNewAccount(AccountDTO accountDTO);
	
	/**
	 * Allows to activate an account after to be created
	 * @param activationCode
	 * @return
	 */
	public Message activateAccount(String activationCode);
	
	/**
	 * Update an account
	 * @param userDTO User data in session
	 * @param accountDTO User data to be updated
	 * @return
	 */
	public Message updateAccount(UserDTO userDTO, AccountDTO accountDTO);

	/**
	 * Change a password
	 * @param userDTO User data in session
	 * @param accountDTO User data to be updated
	 * @return
	 */
	public Message changePassword(UserDTO userDTO, AccountDTO accountDTO);	

	/**
	 * Allows to change a password when the user does not remember the password
	 * First step: User typed his email
	 * @param user
	 * @return
	 */
	public Message recoverPasswordStepOne(AccountDTO user);
	
	/**
	 * Second step: User typed his new password
	 * @param user User data to be updated
	 * @return
	 */
	public Message recoverPasswordStepTwo(AccountDTO user);
	
	/**
	 * Checks if the email from the user is already registered.
	 * If the email belongs to the email of one registered user, 
	 * this function also returns false.
	 * @param email 
	 * @return true if the email is already taken, false otherwise.
	 */
	public boolean emailExists(String email);
	
	/**
	 * Gets an AccountDTO by username(email)
	 * @param userDTO
	 * @return
	 */
	public AccountDTO getUserByUserName(UserDTO userDTO);
}