package br.unifesp.maritaca.business.base.dto;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;

import br.unifesp.maritaca.business.util.ConstantsBusiness;
import br.unifesp.maritaca.persistence.util.ConstantsTest;

public abstract class AbstractBusiness {
	
	private Locale locale = new Locale(ConstantsBusiness.LOCALE_DEFAULT);
	private MessageSourceAccessor messages;
	
	@Autowired
    public void setMessages(MessageSource messageSource) {
        messages = new MessageSourceAccessor(messageSource);        
    }
	
	public String getText(String messageKey) {
		try {
			return messages.getMessage(messageKey, locale);
		}
		catch(Exception ex) {
			return "";
		}
    }
    
    public String getText(String messageKey, String arg) {
        return getText(messageKey, new Object[] { arg });
    }
    
    public String getText(String messageKey, Object[] args) {
        return messages.getMessage(messageKey, args, locale);
    }
	
	public int getTotalNumPages(int totalRows, int numRows)  {
        return (int)Math.ceil(((double)totalRows/(double)numRows));
    }
	
	protected Integer getPage(Integer page) {
		return page == null ? 1 : page;
	}
	
	protected boolean isRunningTestEnvironment(){
		if(System.getProperty(ConstantsTest.SYS_PROP_KEY_TEST) == null){
			return false;
		}
		return true;
	}
}