package br.unifesp.maritaca.ws.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringApplicationContextTest {
	
	private static ApplicationContext appContext;

	public static Object getBean(String beanName) {
		appContext = new ClassPathXmlApplicationContext("classpath:webservices-context-test.xml");
		return appContext.getBean(beanName);
	}
}