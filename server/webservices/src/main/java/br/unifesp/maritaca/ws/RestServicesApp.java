package br.unifesp.maritaca.ws;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

import br.unifesp.maritaca.ws.api.impl.AnalyticsServiceImpl;
import br.unifesp.maritaca.ws.api.impl.AnswersServiceImpl;
import br.unifesp.maritaca.ws.api.impl.InviteServiceImpl;
import br.unifesp.maritaca.ws.api.impl.MessagesServiceImpl;
import br.unifesp.maritaca.ws.api.impl.ReportServiceImpl;

public class RestServicesApp extends Application {
	
	private Set<Object> singletons = new HashSet<Object>();
	
	private Set<Class<?>> classes = new HashSet<Class<?>>();
	
	public RestServicesApp() {
		classes.add(AnswersServiceImpl.class);
		classes.add(MessagesServiceImpl.class);
		classes.add(InviteServiceImpl.class);
		classes.add(ReportServiceImpl.class);
		classes.add(AnalyticsServiceImpl.class);
	}
	
	@Override
	public Set<Class<?>> getClasses() {
		return classes;
	}
	
	@Override
	public Set<Object> getSingletons() {
		return singletons;
	}
}