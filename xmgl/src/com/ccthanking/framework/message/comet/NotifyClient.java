package com.ccthanking.framework.message.comet;

import java.util.Collection;

import javax.servlet.ServletContext;

import org.directwebremoting.Browser;
import org.directwebremoting.ScriptBuffer;
import org.directwebremoting.ScriptSession;
import org.directwebremoting.ScriptSessionFilter;
import org.directwebremoting.ServerContext;
import org.directwebremoting.ServerContextFactory;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.web.context.ServletContextAware;

public class NotifyClient implements ApplicationListener, ServletContextAware {
	private ServletContext servletContext = null;

	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	public void onApplicationEvent(ApplicationEvent event) {
		if (event instanceof InfoEvent) {
			final PerformInfo info = (PerformInfo) event.getSource();
			send(info);
		}
	}

	/**
	 * 推给指定用户
	 * @param userid
	 * @param request
	 * @return
	 */
	private void send(final PerformInfo info) {
		Browser.withAllSessionsFiltered(new ScriptSessionFilter() {
			public boolean match(ScriptSession session) {
				if (session.getAttribute("userId") == null)
					return false;
				else
					return (session.getAttribute("userId")).equals(info.getReceiver());
			}
		}, new Runnable() {
			public void run() {
				Collection<ScriptSession> colls = Browser.getTargetSessions();
				for (ScriptSession scriptSession : colls) {
					scriptSession.addScript(initFunctionCall(
							"dwr.util.setValue",info.getMsg(),info.getUrl(),info.getTitle()));
				}
			}
		});
	}
	
	private ScriptBuffer initFunctionCall(String funcName, Object... params) {
		ScriptBuffer script = new ScriptBuffer();
		script.appendScript("show(");  
		for (int i = 0; i < params.length; ++i) {
			if (i != 0) {
				script.appendScript(",");
			}
			script.appendData(params[i]);
		}
		script.appendScript(")");  
		
		
//		script.appendScript(funcName).appendScript("(");
//		for (int i = 0; i < params.length; ++i) {
//			if (i != 0) {
//				script.appendScript(",");
//			}
//			script.appendData(params[i]);
//		}
//		script.appendScript(");");
		return script;
	}
}
