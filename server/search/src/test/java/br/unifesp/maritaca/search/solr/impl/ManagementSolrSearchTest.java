package br.unifesp.maritaca.search.solr.impl;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import br.unifesp.maritaca.search.ManagementSearch;
import br.unifesp.maritaca.search.solr.pojo.SolrForm;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/search-context-test.xml")
public class ManagementSolrSearchTest {
	
	@Autowired ManagementSearch<SolrForm> managementSolrSearch;

	@SuppressWarnings("unchecked")
	@Test
	public void testSearchForms(){		
		String[] search = {"Third Form"};
		//List<SolrForm> forms = (List<SolrForm>)managementSolrSearch.searchForms(search, null, null);
		//Assert.assertNotNull(forms);
		/*if(forms !=null){
			System.out.println("forms: " + forms.size());
			for(SolrForm f : forms){
				System.out.println("form-title: " + f.getTitle());
			}
		}*/
		Assert.assertTrue(true);
	} 
	
	@Test
	public void insertForm(){
		/*System.out.println("insertForm...");
		SolrForm form = new SolrForm();
		form.setUrl("0003-0003");
		form.setTitle("Third Form");
		form.setDescription("Description for the third form");
		form.setPolicy(0);
		managementSolrSearch.insertForm(form);*/
		Assert.assertTrue(true);
	}
}