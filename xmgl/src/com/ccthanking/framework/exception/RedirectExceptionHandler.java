package com.ccthanking.framework.exception;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ExceptionHandler;
import org.apache.struts.config.ExceptionConfig;
import org.apache.struts.util.ModuleException;

public class RedirectExceptionHandler extends ExceptionHandler {

	private static org.apache.log4j.Logger logger = org.apache.log4j.LogManager
			.getLogger("RedirectExceptionHandler");

	public ActionForward execute(Exception ex, ExceptionConfig ae,
			ActionMapping mapping, ActionForm formInstance,
			HttpServletRequest request, HttpServletResponse response)
			throws ServletException {
		ActionForward forward = null;
		ActionError error = null;
		String property = null;
		if (ae.getPath() != null) {
			forward = new ActionForward(ae.getPath());


		} else {
			forward = mapping.getInputForward();
		}

		// Figure out the error
		if (ex instanceof ModuleException) {
			error = ((ModuleException) ex).getError();
			property = ((ModuleException) ex).getProperty();
		} else {
			logger.error(ex.getMessage());
			error = new ActionError(ae.getKey(), ex.getMessage());
			property = error.getKey();
		}

		// Store the exception
		request.setAttribute(
				com.ccthanking.framework.Globals.KEY_ERROR_MESSAGE,
				ex.getMessage());
		storeException(request, property, error, forward, ae.getScope());
		return forward;
	}

}
