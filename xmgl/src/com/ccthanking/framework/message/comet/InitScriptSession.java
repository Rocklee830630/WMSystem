package com.ccthanking.framework.message.comet;

import javax.servlet.GenericServlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpSession;

import org.directwebremoting.Container;
import org.directwebremoting.ScriptSession;
import org.directwebremoting.ServerContextFactory;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.event.ScriptSessionEvent;
import org.directwebremoting.event.ScriptSessionListener;
import org.directwebremoting.extend.ScriptSessionManager;

public class InitScriptSession extends GenericServlet {

	ServletContext application;

	public void init() {
		Container container = ServerContextFactory.get().getContainer();
		ScriptSessionManager manager = container
				.getBean(ScriptSessionManager.class);
		ScriptSessionListener listener = new ScriptSessionListener() {
			public void sessionCreated(ScriptSessionEvent ev) {
				HttpSession session = WebContextFactory.get().getSession();
				ScriptSession scriptSession = ev.getSession();
				
				String userId =(String) session.getAttribute("userId");
				scriptSession.setAttribute("userId", userId);
//				System.err.println("创建---"+scriptSession.getId()+"------- put userId into scriptSession as "
//						+ userId);
			}

			public void sessionDestroyed(ScriptSessionEvent ev) {
			}
		};
		manager.addScriptSessionListener(listener);
	}

	public void service(ServletRequest req, ServletResponse res) {
		init();
	}
}
