package br.unifesp.maritaca.business.authentication;

import java.util.List;

import br.unifesp.maritaca.business.account.dto.AccountDTO;
import br.unifesp.maritaca.business.base.dto.Message;
import br.unifesp.maritaca.business.base.dto.UserDTO;
import br.unifesp.maritaca.business.form.dto.FormDTO;

public interface Login {
	
	/**
	 * Login with username(email) and a encrypted password 
	 * @param accountDTO
	 * @return
	 */
	public Message doLogin(AccountDTO accountDTO);

	/**
	 * Login using Google, Yahoo, Facebook, Twitter
	 * @param accountDTO
	 * @return
	 */
	public AccountDTO doLoginSocial(AccountDTO accountDTO);

	/**
	 * Gets the top public forms for the index page
	 * @return
	 */
	public List<FormDTO> getTopPublicForms();
	
	/**
	 * Search the public forms to show on the index page
	 * @param search
	 * @return
	 */
	public List<FormDTO> searchPublicForms(String search);

	/**
	 * Gets an APK by both formUrl and user logged 
	 * @param url
	 * @param userDTO
	 * @return
	 */
	public Message getApkByUrl(String url, UserDTO userDTO);
	
	/**
	 * Gets an APK by formUrl
	 * @param url
	 * @return
	 */
	public Message getAPK(String url);

	/**
	 * This method returns if apk is being built or not. 
	 * @param formUrl
	 * @return
	 */
	public boolean isAPKBuilding(String formUrl);
}