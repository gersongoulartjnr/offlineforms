package br.unifesp.maritaca.business.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import br.unifesp.maritaca.business.enums.OrderFormBy;
import br.unifesp.maritaca.business.enums.OrderType;
import br.unifesp.maritaca.business.form.dto.FormDTO;
import br.unifesp.maritaca.persistence.entity.MaritacaDate;
import br.unifesp.maritaca.persistence.permission.Policy;

public class UtilsBusinessTest extends TestCase {
	
	private List<FormDTO> forms;
	private List<String> owners;
	long longTime = (new Date()).getTime();
 
    @Before
    public void setUp() {
    	owners = new ArrayList<String>();
    	owners.add("Arlindo F.");
    	owners.add("Alvaro M. A.");
    	owners.add("Jimmy V. S.");
    	owners.add("Tiago B.");
    	
    	forms = new ArrayList<FormDTO>();
		FormDTO formDTO;
		for (int i = 0; i < 20; i++) {
			formDTO = new FormDTO();
			formDTO.setKey(UUID.randomUUID());
			formDTO.setTitle("form" + i);
			formDTO.setOwner(owners.get((int)(Math.random()*4)));
			formDTO.setCreationDate((new MaritacaDate(longTime + (i*60000))).toString());
			formDTO.setPolicy(Policy.getPolicyFromId((int)(Math.random()*4)));
			formDTO.setNumberOfCollects((int)(Math.random()*10));
			
			forms.add(formDTO);
		}
    }
 
    @After
    public void tearDown() {
    	forms.clear();
    }
 
    @Test
    public void testSortFormsGeneralCase() throws ParseException {
        UtilsBusiness.sortGrid(forms, OrderFormBy.DATE, OrderType.DESC);
        
    	DateFormat df = DateFormat.getInstance();
    	
        Date date1 = df.parse(forms.get(0).getCreationDate());
        Date date2 = df.parse(forms.get(1).getCreationDate());
                
        int compareTo = date1.compareTo(date2);
		assertTrue(compareTo >= 0);
    }
    
    
    @Test
    public void testSortFormsByDateAscendingOrder() throws ParseException {
        UtilsBusiness.sortGrid(forms, OrderFormBy.DATE, OrderType.ASC);
        
    	DateFormat df = DateFormat.getInstance();
    	
        Date date1 = df.parse(forms.get(0).getCreationDate());
        Date date2 = df.parse(forms.get(1).getCreationDate());
                
        int compareTo = date1.compareTo(date2);
		assertTrue(compareTo <= 0);
    }
    
    @Test
    public void testSortFormsByTitleAscendingOrder() {
    	UtilsBusiness.sortGrid(forms, OrderFormBy.TITLE, OrderType.ASC);
	    	
    	String title1 = forms.get(0).getTitle();
    	String title2 = forms.get(1).getTitle();
    	
    	assertTrue(title1.compareTo(title2) <= 0);
    }
    
    @Test
    public void testSortFormsByTitleDescendingOrder() {
    	UtilsBusiness.sortGrid(forms, OrderFormBy.TITLE, OrderType.DESC);
    	
    	String title1 = forms.get(0).getTitle();
    	String title2 = forms.get(1).getTitle();
    	
    	assertTrue(title1.compareTo(title2) >= 0);
    }
    
    @Test
    public void testSortFormsByOwnerAscendingOrder() {
    	UtilsBusiness.sortGrid(forms, OrderFormBy.OWNER, OrderType.ASC);
	
    	Collections.sort(owners, new Comparator<String>() {
    		public int compare(String value1, String value2){
    			return value1.compareTo(value2);
    		}
    	});
    	
    	String owner = forms.get(0).getOwner();
    	assertTrue(owner.equals(owners.get(0)));
    }
    
    @Test
    public void testSortFormsByOwnerDescendingOrder() {
    	UtilsBusiness.sortGrid(forms, OrderFormBy.OWNER, OrderType.DESC);
	
    	Collections.sort(owners, new Comparator<String>() {
    		public int compare(String value1, String value2){
    			return value2.compareTo(value1);
    		}
    	});
    	
    	String owner = forms.get(0).getOwner();
    	assertTrue(owner.equals(owners.get(0)));
    }
    
    @Test
    public void testSortFormsByPolicyDescendingOrder() {
    	UtilsBusiness.sortGrid(forms, OrderFormBy.POLICY, OrderType.DESC);
   	
    	Policy policy1 = forms.get(0).getPolicy();
    	Policy policy2 = forms.get(forms.size()-1).getPolicy();
    	int compareTo = policy1.compareTo(policy2);
		assertTrue(compareTo >= 0);
    }
    
    @Test
    public void testSortFormsByPolicyAscendingOrder() {
    	UtilsBusiness.sortGrid(forms, OrderFormBy.POLICY, OrderType.ASC);
   	
    	Policy policy1 = forms.get(0).getPolicy();
    	Policy policy2 = forms.get(forms.size()-1).getPolicy();
    	int compareTo = policy1.compareTo(policy2);
		assertTrue(compareTo <= 0);
    }
}