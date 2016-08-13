package com.ccthanking.framework.spflow.ApproveTask;

import javax.servlet.http.*;
import org.apache.struts.action.*;
import org.dom4j.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ccthanking.framework.base.*;
import com.ccthanking.framework.common.*;
import com.ccthanking.framework.util.*;

/**
 * 
 * @author 
 * @des    审批归档通用类,误删
 */

@Controller
@RequestMapping("/TaskQuery")
public class ApproveTaskAction{
    
  
 	public ActionForward getApproveTask(ActionMapping mapping, ActionForm form,
                                        HttpServletRequest request,
                                        HttpServletResponse response)
        throws Exception
    {
        Document doc = RequestUtil.getDocument(request);
        QueryConditionList cond = RequestUtil.getConditionList(doc);
        PageManager pm = RequestUtil.getPageManager(doc);
        ApproveTaskBO bo = new ApproveTaskBO(null);
        try
        {
            Document domresult = bo.getApproveTaskList(null, cond, pm);
            Pub.writeXmlDocument(response, domresult);
        }
        catch (Exception e)
        {
            e.printStackTrace(System.out);
            Pub.writeXmlMessage(response, "意外错误！！" + e.toString(),
                                "ERRMESSAGE");
        }
        return null;
    }
}