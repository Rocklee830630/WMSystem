package com.ccthanking.framework.params.UserPara;

import javax.servlet.http.HttpServletRequest;

import com.ccthanking.framework.base.BaseBO;
import com.ccthanking.framework.common.*;
import com.ccthanking.framework.params.SysPara.SysParaConfigureDAO;
import com.ccthanking.framework.params.SysPara.SysParaConfigureVO;

import java.sql.*;

public class UserParaConfigureBO  extends BaseBO {

  public UserParaConfigureBO(javax.servlet.http.HttpSession session)
  {
  }

  private static org.apache.log4j.Logger logger = org.apache.log4j.LogManager.getLogger("UserParaConfigureBO");

  public Object selectById(String pk)throws Exception {
		 Connection conn =DBUtil.getConnection();
		 UserParaConfigureVO tempVO = null;
		 try
		 {
			 UserParaConfigureDAO tempDAO = new UserParaConfigureDAO();
			 tempVO =(UserParaConfigureVO)tempDAO.selectById(pk, conn);
		 }
		 catch (Exception e) {
					conn.rollback();
				throw e;
			} finally {
				if (conn != null) {
					conn.close();
				}
			}
		 return tempVO;
	 }

   public String insert(Object obj ,HttpServletRequest request) throws Exception
   {
     UserParaConfigureVO tempVO=(UserParaConfigureVO)obj;
     Connection conn =DBUtil.getConnection();
     String intId = DBUtil.getSequenceValue("AP_PARA_CONFIGURE_SN",conn);
     tempVO.setUserParaConfigureSn(intId);
     String strRes=null;
     try {
      conn.setAutoCommit(false);
       UserParaConfigureDAO tempDAO = new UserParaConfigureDAO();
         strRes = tempDAO.insert(tempVO,conn);
          conn.commit();
     }
     catch(Exception  e) {
            conn.rollback();
       throw e;
     }finally{
      if(conn != null){
        conn.close();
      }
    }
    return strRes;
  }

   public String update(Object obj ,HttpServletRequest request) throws Exception {
     UserParaConfigureVO tempVO=(UserParaConfigureVO)obj;
     Connection conn =DBUtil.getConnection();
     String strRes=null;
     try {
      conn.setAutoCommit(false);
       UserParaConfigureDAO tempDAO = new UserParaConfigureDAO();
         strRes = tempDAO.update(tempVO,conn);
          conn.commit();
     }
     catch(Exception  e) {
            conn.rollback();
       throw e;
     }finally{
      if(conn != null){
        conn.close();
      }
    }
     return strRes;
  }

   public String delete(Object obj ,HttpServletRequest request) throws Exception {
     UserParaConfigureVO tempVO=(UserParaConfigureVO)obj;
     String strRes = "";
     Connection conn =DBUtil.getConnection();
     try {
      conn.setAutoCommit(false);
       UserParaConfigureDAO tempDAO = new UserParaConfigureDAO();
         strRes = tempDAO.delete(tempVO,conn);
          conn.commit();
     }
     catch(Exception  e) {
            conn.rollback();
       throw e;
     }finally{
      if(conn != null){
        conn.close();
      }

    }
     return strRes;
  }


}
