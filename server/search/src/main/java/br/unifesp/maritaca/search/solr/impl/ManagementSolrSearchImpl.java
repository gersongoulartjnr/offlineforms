package br.unifesp.maritaca.search.solr.impl;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.unifesp.maritaca.search.ManagementSearch;
import br.unifesp.maritaca.search.enums.OrderDocBy;
import br.unifesp.maritaca.search.enums.Policy;
import br.unifesp.maritaca.search.enums.SearchType;
import br.unifesp.maritaca.search.solr.pojo.SolrForm;
import br.unifesp.maritaca.search.solr.pojo.WrapperForms;
import br.unifesp.maritaca.search.util.SearchConstants;

@Service("managementSolrSearch")
public class ManagementSolrSearchImpl<T> implements ManagementSearch<T> {
	
	private static Logger logger = Logger.getLogger(ManagementSolrSearchImpl.class);	
	@Autowired private SolrServer solr;

	@Override
	public WrapperForms listForms(Integer searchType, String orderBy,
			String orderType, Integer page, Integer numRows, String currentUser) {

		return getForms(searchType, null, orderBy, orderType, page, numRows, currentUser);
	}
	
	@Override			
	public WrapperForms searchForms(Integer searchType, String[] search, 
			String orderBy, String orderType, Integer page, Integer numRows, 
			String currentUser){

		if(search == null || search.length == 0)
			return null;
		
		return getForms(searchType, search, orderBy, orderType, page, numRows, currentUser);
	}
	
	private WrapperForms getForms(Integer searchType, String[] search, 
			String orderBy, String orderType, Integer page, Integer numRows, 
			String currentUser){		
		
		SearchType sType = SearchType.getSearchType(searchType);
		if(sType == null)
			return null;
				
		OrderDocBy orderDocBy = OrderDocBy.getOrderBy(orderBy);
		if(orderType == null || "".equals(orderType))
			orderType = "desc";
		
		WrapperForms wrapperForms = null;
		List<SolrForm> forms = null;
		try {
			StringBuffer strQuery = new StringBuffer();
			 
			if(sType.equals(SearchType.PUBLIC_FORMS)){
				strQuery.append(SearchConstants.FORM_POLICY);
				strQuery.append(":");
				strQuery.append(Policy.PUBLIC.getId());
			} else if(sType.equals(SearchType.MY_FORMS)) {
				strQuery.append(SearchConstants.FORM_OWNER);
				strQuery.append(":");
				strQuery.append(currentUser);
			} else {
				strQuery.append(SearchConstants.FORM_USERS);
				strQuery.append(":");
				strQuery.append(currentUser);
				strQuery.append(" OR ");
				strQuery.append(SearchConstants.FORM_POLICY);
				strQuery.append(":");
				strQuery.append(Policy.PUBLIC.getId());
				strQuery.append(" OR ");
				strQuery.append("!");
				strQuery.append(SearchConstants.FORM_OWNER);
				strQuery.append(":");
				strQuery.append(currentUser);
			}			
			
			if(search != null){
				strQuery.append(" AND ");
				int i;
				int max = search.length;
				for(i = 0; i < max; i++){
					String word = search[i];
					strQuery.append(SearchConstants.FORM_TEXT);
					strQuery.append(":");
					strQuery.append(word);
					if(word.length() > 4){
						strQuery.append("~2 ");
					}
					
					if((i+1) != max){
						strQuery.append("AND ");
					}
				}
			}
			
			ModifiableSolrParams params = new ModifiableSolrParams();			
			params.set("q", strQuery.toString());
			//Start-End
			if(page != null){
				int _start = (page-1)*numRows;
				params.set("start", String.valueOf(_start));
				if(numRows != null)
					params.set("rows", String.valueOf(numRows));
			} else{
				params.set("start", "0");
			}
			//Sort
			params.set("sort", orderDocBy.getDocDescription()+" "+orderType);
			
			QueryResponse response = solr.query(params);
			forms = response.getBeans(SolrForm.class);
			Long totalForms = response.getResults().getNumFound();
			
			wrapperForms = new WrapperForms(totalForms.intValue(), forms);
		} catch (SolrServerException e) {
			logger.error(e.getMessage());
			throw new RuntimeException();
		}
		return wrapperForms;
	}

	@Override
	public void saveForm(T form) {
		if(form == null)
			return;
		try {
			solr.addBean(form);
			solr.commit();
		} catch (IOException e) {			
			logger.error(e.getMessage());
			throw new RuntimeException();
		} catch (SolrServerException e) {
			logger.error(e.getMessage());
			throw new RuntimeException();
		}
	}

	@Override
	public void deleteForm(String idForm) {
		if(idForm == null || "".equals(idForm))
			return;
		try {
			solr.deleteById(idForm);
			solr.commit();
		} catch (SolrServerException e) {
			logger.error(e.getMessage());
			throw new RuntimeException();
		} catch (IOException e) {
			logger.error(e.getMessage());
			throw new RuntimeException();
		}			
	}
}