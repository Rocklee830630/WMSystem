package com.ccthanking.framework.common.cache;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.ccthanking.framework.base.BaseDispatchAction;
import com.ccthanking.framework.util.Pub;


public class CacheSynchronizeAction extends BaseDispatchAction{
	
    private Logger logger = com.ccthanking.framework.log.log.getLogger(
        "CacheSynchronizeAction");

	public ActionForward SynchronizeCache(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// 此处应该加上对权限的校验
		try {
			String cacheType = (String) request.getParameter("CACHE_TYPE");
			String data = (String) request.getParameter("CACHE_DATA");
			int operation = Integer.parseInt((String) request
					.getParameter("CACHE_OPERATION"));
			CacheManager.synchronizeCache(cacheType, data, operation);
			logger.info("同步完成！");
			Pub.writeMessage(response, "同步完成！", "GBK");
		} catch (Exception E) {
			E.printStackTrace();
			logger.error("同步失败！" + E);
			Pub.writeMessage(response, "同步失败！", "GBK");
		}
		return null;
	}
	
	public ActionForward reBuildMemory(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// 此处应该加上对权限的校验
		try {
			String cacheType = (String) request.getParameter("CACHE_TYPE");
			CacheManager.synchronizeCache(cacheType);
			logger.info("同步完成！");
			Pub.writeMessage(response, "同步完成！", "GBK");
		} catch (Exception E) {
			E.printStackTrace();
			logger.error("同步失败！" + E);
			Pub.writeMessage(response, "同步失败！", "GBK");
		}
		return null;
	}
	
}