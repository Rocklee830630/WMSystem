package com.ccthanking.framework.log;

import java.sql.Connection;

import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.util.Pub;
import com.ccthanking.framework.util.RandomGUID;


public class LogManager{
	
	public static final String LOGIN_STATUS_SUCCESS = "1";
	public static final String LOGIN_STATUS_FAILURE = "2";
	public static final String LOGIN_STATUS_LOGOUT = "3";
	public static final String RESULT_SUCCESS = "1";
	public static final String RESULT_FAILURE = "2";
  
	private LogManager()
	{
	}
  
	/**
	 * 系统日志
	 * @param moduleID
	 * @param dscr
	 * @param result
	 * @return
	 */
	public static SysLogVO writeSysLog(String moduleID,String dscr,String result){
	      try{
	          SysLogVO vo = new SysLogVO();
	          vo.setMODULEID(moduleID);
	          vo.setMEMO(dscr);
	          vo.setRESULT(result);
	          vo.setOPERATETIME(Pub.getCurrentDate());
	
	          if(vo == null) return null;
	          Connection conn = DBUtil.getConnection();
	          conn.setAutoCommit(false);
	          try {
	        	  String id = DBUtil.getSequenceValue("log_syslog_s", conn);
	        	  vo.setOPID(id);
	        	  String sql = "insert into fs_log_syslog cols(opid,operatetime,result,"
	        			  +"month,memo,operatetype,moduleid) values(?,?,?,?,?,?,?)";
	        	  Object[] paras = {vo.getOPID(),vo.getOPERATETIME(),vo.getRESULT()
	                ,vo.getMONTH(),vo.getMEMO(),vo.getOPERATETYPE(),vo.getMODULEID()};
	        	  DBUtil.executeUpdate(conn,sql,paras);
	        	  conn.commit();
	          }catch (Exception e) {
	        	  conn.rollback();
	        	  e.printStackTrace(System.out);
	        	  return null;
	          }finally {
	        	  if (conn != null){
	        		  conn.close();
	        	  }
	          }
	
	          return vo;
	      }catch(Exception e){
	          e.printStackTrace(System.out);
	          return null;
	      }
	}
  
	/**
	 * 用户登录日志
	 * @param user
	 * @param status
	 * @return
	 */
	public static UserLoginVO writeLoginLog(User user,String status){
		try{
	    	UserLoginVO vo = new UserLoginVO();
			vo.setLOGINIP(user.getIP());
			vo.setLOGINSTATUS(status);
			vo.setLOGINTIME(Pub.getCurrentDate());
			vo.setUSERDEPTID(user.getDepartment());
			vo.setUERNAME(user.getName());
			vo.setUSERID(user.getAccount());
	
			if (vo == null){
				return null;
			}
			
	        Connection conn = DBUtil.getConnection();
	        try {
	        	String id = new RandomGUID().toString();
	        	conn.setAutoCommit(false);
	        	vo.setLOGINID(id);
	        	String sql = "insert into fs_log_userlogin (loginid,userid,uername,userdeptid,loginip,logintime,loginstatus,logouttime,month) values(?,?,?,?,?,?,?,?,?)";
	        	Object[] paras = {
	        			vo.getLOGINID(), vo.getUSERID(), vo.getUERNAME(), vo.getUSERDEPTID(),
	        			vo.getLOGINIP(), vo.getLOGINTIME(), vo.getLOGINSTATUS(),
	        			vo.getLOGOUTTIME(),vo.getMONTH()};
	        	DBUtil.executeUpdate(conn,sql,paras);
	        	conn.commit();
	        }catch (Exception e) {
	        	conn.rollback();
	        	e.printStackTrace(System.out);
	        	return null;
	        }finally {
	        	if (conn != null){
	        		conn.close();
	        	}
	        }
	        user.setLoginLogID(vo.getLOGINID());
	        return vo;
		}catch(Exception e){
			e.printStackTrace(System.out);
			return null;
		}
	}

	/**
	 * 用户登出日志
	 * @param user
	 */
	public static void writeLogoutLog(User user){
	    try{
	    	
	    	if(null == user){
	    		return;
	    	}
	      
	    	UserLoginVO vo = new UserLoginVO();
	    	vo.setLOGINID(user.getLoginLogID());
	    	vo.setLOGINSTATUS(LogManager.LOGIN_STATUS_LOGOUT);
	    	vo.setLOGOUTTIME(Pub.getCurrentDate());
	
	    	if (vo == null){return;}
	    	if(Pub.empty(vo.getLOGINID())){return;}
	    	
	    	Connection conn = DBUtil.getConnection();
	    	conn.setAutoCommit(false);
	    	
	    	try {
	    		String sql = "update fs_log_userlogin set LOGINSTATUS=?,LOGOUTTIME=?,MONTH=? where LOGINID=? and MONTH=?";
	    		Object[] paras = {vo.getLOGINSTATUS(),vo.getLOGOUTTIME(),
	    				vo.getMONTH(),vo.getLOGINID(),vo.getMONTH()};
	    		DBUtil.executeUpdate(conn,sql,paras);
	    		conn.commit();
	      }
	      catch (Exception e) {
	    	conn.rollback();
	        e.printStackTrace(System.out);
	        return;
	      }
	      finally {
	        if (conn != null)
	          conn.close();
	      }
	    }
	    catch(Exception e)
	    {
	      e.printStackTrace(System.out);
	    }
	}
  
  
	/**
	 * 用户操作日志
	 * @param sjbh
	 * @param ywlx
	 * @param optype
	 * @param result
	 * @param memo
	 * @param user
	 */
	public static void writeUserLog(String sjbh,String ywlx,String optype,String result,String memo,User user,String ywzjzd,String ywzjz){
		try{
			OperationLogVO vo = new OperationLogVO();
			vo.setLOGINID(user.getLoginLogID());
			vo.setMEMO(memo);
			vo.setSJBH(sjbh);
			vo.setYWLX(ywlx);
			vo.setRESULT(result);
			vo.setOPERATETYPE(optype);
			vo.setOPERATEIP(user.getIP());
			vo.setOPERATETIME(Pub.getCurrentDate());
			vo.setUSERDEPTID(user.getDepartment());
			vo.setUSERDEPTNAME(user.getOrgDept().getDeptName());
			vo.setUSERID(user.getAccount());
			vo.setUSERNAME(user.getName());
			vo.setYWZJZD(ywzjzd);
			vo.setYWZJZ(ywzjz);

			if(vo == null) return;
			Connection conn = DBUtil.getConnection();
			conn.setAutoCommit(false);
			
			try {
				String id = new RandomGUID().toString();
				vo.setOPID(id);
				String sql = "insert into fs_log_useroperation cols(opid,userid,username,"
						+"userdeptid,operateip,operatetime,ywlx,sjbh,result,loginid,month,"
						+"memo,OPERATETYPE,USERDEPTNAME,YWZJZD,YWZJZ) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
				Object[] paras = {
						vo.getOPID(), vo.getUSERID(), vo.getUSERNAME(), vo.getUSERDEPTID(),
						vo.getOPERATEIP(), vo.getOPERATETIME(), vo.getYWLX(),vo.getSJBH(),
						vo.getRESULT(),vo.getLOGINID(),vo.getMONTH(),vo.getMEMO(),vo.getOPERATETYPE(),
						vo.getUSERDEPTNAME(),vo.getYWZJZD(),vo.getYWZJZ()};
				DBUtil.executeUpdate(conn,sql,paras);
				conn.commit();
			}catch (Exception e) {
				conn.rollback();
				e.printStackTrace(System.out);
				return ;
			}finally {
				if (conn != null){
					conn.close();
				}
			}
		}catch(Exception e){
			e.printStackTrace(System.out);
		}
	}
	
	public static void writeUpdateLog(String dscr, String beforeValue,
			String afterValue, String result, User user) {
		try {
			DataLogVO vo = new DataLogVO();
			vo.setLOGINID(user.getLoginLogID());
			vo.setDATATYPE(dscr);
			vo.setRESULT(result);
			vo.setAFTERVALUE(afterValue);
			vo.setBEFOREVALUE(beforeValue);
			vo.setOPERATEIP(user.getIP());
			vo.setOPERATETIME(Pub.getCurrentDate());
			vo.setDEPTID(user.getDepartment());
			vo.setUSERID(user.getAccount());
			vo.setUSERNAME(user.getName());

			if (vo == null)
				return;
			Connection conn = DBUtil.getConnection();
			conn.setAutoCommit(false);
			try {
				String id = DBUtil.getSequenceValue("log_syslog_s", conn);
				vo.setUPDATEID(id);
				String sql = "insert into fs_log_sysdataupdate cols(updateid,userid,username,"
						+ "deptid,operateip,operatetime,datatype,beforevalue,aftervalue,result,"
						+ "loginid) values(?,?,?,?,?,?,?,?,?,?,?)";
				Object[] paras = { vo.getUPDATEID(), vo.getUSERID(),
						vo.getUSERNAME(), vo.getDEPTID(), vo.getOPERATEIP(),
						vo.getOPERATETIME(), vo.getDATATYPE(),
						vo.getBEFOREVALUE(), vo.getAFTERVALUE(),
						vo.getRESULT(), vo.getLOGINID() };
				DBUtil.executeUpdate(conn, sql, paras);
				conn.commit();
			} catch (Exception e) {
				conn.rollback();
				e.printStackTrace(System.out);
				return;
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
	}

}