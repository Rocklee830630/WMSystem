package com.ccthanking.spflow;

import java.sql.Connection;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.QuerySet;
import com.ccthanking.framework.common.User;
import java.util.*;
import java.text.*;

/**
 *
 * @author
 * @des 审批不同意调用方法列表,误删
 */

public class DisApproveMethodList 
{
	/** example
	public static final String YWLX040301 = "task040301"; // YWLX+业务号
	
	public static boolean task040301(Connection conn, String eventid, User user)
	                                throws Exception 
	{
       String sql = " update GA_JCXX_AJ_AJJBXX set AJZT = '104' where SJBH=?";
       Object[] objs = new Object[1];
       objs[0] = eventid;
       try
       {
	    DBUtil.executeUpdate(conn, sql, objs);
       } catch (Exception e) 
       {
	      return false;
       }
     return true;
    }
    */

}