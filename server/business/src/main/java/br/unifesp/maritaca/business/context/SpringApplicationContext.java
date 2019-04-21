package br.unifesp.maritaca.business.context;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class SpringApplicationContext implements ApplicationContextAware {
	
	private static Logger logger = Logger.getLogger(SpringApplicationContext.class);
	
	private static ApplicationContext appContext;

	private SpringApplicationContext() {
		logger.info("SpringApplicationContext loaded");
	}
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		appContext = applicationContext;	
	}
	
	public static Object getBean(String beanName) {
		return appContext.getBean(beanName);
	}	
}