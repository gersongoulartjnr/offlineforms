package br.unifesp.maritaca.web.controller.websocket;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.atmosphere.cpr.AtmosphereResource;
import org.atmosphere.cpr.Broadcaster;
import org.atmosphere.cpr.BroadcasterFactory;
import org.atmosphere.cpr.FrameworkConfig;
import org.atmosphere.websocket.WebSocketEventListenerAdapter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * This was based on the Atmosphere samples about Spring.
 * 
 */
@Controller
public class AutomaticRefrestController  {
	private static Logger logger = Logger.getLogger(AutomaticRefrestController.class);
	private static final String MYFORMS = "myforms";
	
	public AutomaticRefrestController() {
		logger.info("AutomaticRefrestController: contructor - Created!");
	}
	
	@RequestMapping(value = "{name}/websocket", method = RequestMethod.GET)
	public ModelAndView subscribe(@PathVariable("name") String name, HttpServletRequest request) 
			throws Exception {
		
		AtmosphereResource resource = (AtmosphereResource) request.getAttribute(FrameworkConfig.ATMOSPHERE_RESOURCE);
		resource.addEventListener(new WebSocketEventListenerAdapter());
		resource.getResponse().setContentType("text/html;charset=ISO-8859-1");
		Broadcaster broadcaster = lookupBroadcaster(name);
		resource.setBroadcaster(broadcaster);
		resource.suspend(-1);
		
		return new ModelAndView(new NoOpView());
	}
	
	@RequestMapping(value = "{topic}", method = RequestMethod.POST)
	public ModelAndView broadcastMessage(HttpServletRequest request)
			throws Exception {
		
		this.doPost(request);

		// A NoOpView is returned to tell Spring Dispatcher framework not to render anything
		// since it is all Atmosphere-related code
		ModelAndView mv = new ModelAndView(new NoOpView());
		return mv;
	}
	
	private void doPost(HttpServletRequest req) throws IOException {
		Broadcaster b = lookupBroadcaster(req.getPathInfo());
		String message = req.getReader().readLine();

		if (message != null && message.indexOf("message") != -1) {
			b.broadcast(message.substring("message=".length()));
		}
	}
	
    Broadcaster lookupBroadcaster(String name) {
        if (name == null) {
            return BroadcasterFactory.getDefault().lookup(MYFORMS, true);
        } else {
            return BroadcasterFactory.getDefault().lookup(name, true);
        }
    }
	
}
