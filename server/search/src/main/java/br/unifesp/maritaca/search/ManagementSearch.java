package br.unifesp.maritaca.search;

import br.unifesp.maritaca.search.solr.pojo.WrapperForms;

public interface ManagementSearch<T> {
	
	/**
	 * 
	 * @param searchType
	 * @param orderBy
	 * @param orderType
	 * @param page
	 * @param numRows
	 * @param currentUser
	 * @return
	 */
	public WrapperForms listForms(Integer searchType,  
			String orderBy, String orderType, Integer page, Integer numRows, 
			String currentUser);
	
	/**
	 * 
	 * @param searchType
	 * @param search
	 * @param orderBy
	 * @param orderType
	 * @param page
	 * @param numRows
	 * @param currentUser
	 * @return
	 */
	public WrapperForms searchForms(Integer searchType, String[] search, 
			String orderBy, String orderType, Integer page, Integer numRows, 
			String currentUser);
	
	/**
	 * Save/Update a register of form using the unique id(form_url)
	 * @param form Object SolrForm
	 */
	public void saveForm(T form);
	
	/**
	 * Delete a form by the unique id(form_url)
	 * @param idForm form url
	 */
	public void deleteForm(String idForm);	
}