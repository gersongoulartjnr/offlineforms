package br.unifesp.maritaca.search.solr.pojo;

import java.io.Serializable;
import java.util.List;

public class WrapperForms implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer totalFormsByQuery;
	private List<SolrForm> solrForms;
	
	public WrapperForms(Integer totalFormsByQuery, List<SolrForm> solrForms) {
		this.totalFormsByQuery = totalFormsByQuery;
		this.solrForms = solrForms;
	}

	public Integer getTotalFormsByQuery() {
		return totalFormsByQuery;
	}
	public void setTotalFormsByQuery(Integer totalFormsByQuery) {
		this.totalFormsByQuery = totalFormsByQuery;
	}

	public List<SolrForm> getSolrForms() {
		return solrForms;
	}
	public void setSolrForms(List<SolrForm> solrForms) {
		this.solrForms = solrForms;
	}
}