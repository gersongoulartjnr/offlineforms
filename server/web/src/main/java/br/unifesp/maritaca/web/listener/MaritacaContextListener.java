package br.unifesp.maritaca.web.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.WebApplicationContextUtils;

import br.unifesp.maritaca.business.init.MaritacaInit;
import br.unifesp.maritaca.web.util.ConstantsWeb;

public class MaritacaContextListener implements ServletContextListener {
	
	private static final Log log = LogFactory.getLog(MaritacaContextListener.class);
	
	@Autowired
	private MaritacaInit maritacaInit;
	
	@Override
	public void contextInitialized(ServletContextEvent arg) {
		WebApplicationContextUtils.getRequiredWebApplicationContext(arg.getServletContext()).getAutowireCapableBeanFactory().autowireBean(this);
		
		maritacaInit.createAllEntities();
		maritacaInit.createDirectories();
	}

	@Override
	public void contextDestroyed(ServletContextEvent arg) {
		if (maritacaInit != null) {
			maritacaInit.close();
		} else {
			System.err.println("Error-contextDestroyed");
		}
	}
}