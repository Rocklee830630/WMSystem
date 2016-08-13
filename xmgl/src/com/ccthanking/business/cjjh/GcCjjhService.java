package com.ccthanking.business.cjjh;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.ccthanking.framework.common.User;
import com.ccthanking.framework.model.autocomplete;

/**
 * <p>
 * GcCjjhService.java
 * </p>
 * <p>
 * 功能：城建基础设施项目建设计划
 * </p>
 * 
 * <p>
 * <a href="GcCjjhService.java.html"><i>查看源代码</i></a>
 * </p>
 * 
 * @author xiahongbo
 * @version 0.1
 * @since 2014-05-27
 * 
 */
public interface GcCjjhService  {

    /**
     * 根据条件查询记录.
     * 
     * @param json
     * @param user
     * @return
     * @throws Exception
     * @since v1.00
     */
    String query(HttpServletRequest request,String json);

    String getProjectfl(HttpServletRequest request);
    
    String add(String json, User user);
    
    String update(String json, User user);
    
    public String queryNodeList(HttpServletRequest request,String json) throws Exception;
    public String getNodeDetail(HttpServletRequest request,String json) throws Exception;
    public String queryXmList(HttpServletRequest request,String json) throws Exception;
    public String saveInfo(HttpServletRequest request, String json) throws Exception;
    public String saveRoot(HttpServletRequest request, String json) throws Exception;
    public String doDeleteNode(HttpServletRequest request, String json) throws Exception;
    List<autocomplete> xmmcAutoComplete(autocomplete json,User user)  throws Exception;
	String queryXMbyId(String msg, User user,HttpServletRequest request);
    
}
