package com.ccthanking.framework.params.UserPara;

import java.sql.SQLException;
import java.sql.ResultSet;
import java.io.Serializable;  



public class UserParaConfigureVO implements Serializable{   /* 用户参数配置表 */ 

  
  public  String SN = null;        /* 参数序号 */
  public  String APPTYPE = null;        /* 参数类型 */
  public  String USERACCOUNT = null;        /* 用户账号 */
  public  String USERNAME = null;        /* 用户名称 */
  public  String USERID = null;        /* 用户编号 */
  public  String USERLEVEL = null;        /* 用户级别 */
  public  String APPLICATION = null;        /* 应用名 */
  public  String PARAKEY = null;        /* 参数key */
  public  String PARANAME = null;        /* 参数名称 */
  public  String PARAVALUE1 = null;        /* 参数值1 */
  public  String PARAVALUE2 = null;        /* 参数值2 */
  public  String PARAVALUE3 = null;        /* 参数值3 */
  public  String PARAVALUE4 = null;        /* 参数值4 */
  public  String MEMO = null;        /* 备注 */
  
  public UserParaConfigureVO (  ){ 
  }
  
   /**
   * 设置参数序号
   * @param userParaConfigureSn String
   */
  public void setUserParaConfigureSn(String userParaConfigureSn){ 
   
     this.SN = userParaConfigureSn ;        /* 参数序号 */    
   
  }
  
   /**
   * 设置参数类型
   * @param userParaConfigureApptype String
   */
  public void setUserParaConfigureApptype(String userParaConfigureApptype){ 
   
     this.APPTYPE = userParaConfigureApptype ;        /* 参数类型 */    
    
  }
  
   /**
   * 设置用户账号
   * @param userParaConfigureUseraccount String
   */
  public void setUserParaConfigureUseraccount(String userParaConfigureUseraccount){ 
  
     this.USERACCOUNT = userParaConfigureUseraccount ;        /* 用户账号 */    
    
  }
  
   /**
   * 设置用户名称
   * @param userParaConfigureUsername String
   */
  public void setUserParaConfigureUsername(String userParaConfigureUsername){ 
    
     this.USERNAME = userParaConfigureUsername ;        /* 用户名称 */    
  
  }
  
   /**
   * 设置用户编号
   * @param userParaConfigureUserid String
   */
  public void setUserParaConfigureUserid(String userParaConfigureUserid){ 
   
     this.USERID = userParaConfigureUserid ;        /* 用户编号 */    
    
  }
  
   /**
   * 设置用户级别
   * @param userParaConfigureUserlevel String
   */
  public void setUserParaConfigureUserlevel(String userParaConfigureUserlevel){ 
  
     this.USERLEVEL = userParaConfigureUserlevel ;        /* 用户级别 */    
   
  }
  
   /**
   * 设置应用名
   * @param userParaConfigureApplicateion String
   */
  public void setUserParaConfigureApplication(String userParaConfigureApplicateion){ 
  
     this.APPLICATION = userParaConfigureApplicateion ;        /* 应用名 */    
   
  }
  
   /**
   * 设置参数key
   * @param userParaConfigureParakey String
   */
  public void setUserParaConfigureParakey(String userParaConfigureParakey){ 
    
     this.PARAKEY = userParaConfigureParakey ;        /* 参数key */    
   
  }
  
   /**
   * 设置参数名称
   * @param userParaConfigureParaname String
   */
  public void setUserParaConfigureParaname(String userParaConfigureParaname){ 
   
     this.PARANAME = userParaConfigureParaname ;        /* 参数名称 */    
    
  }
  
   /**
   * 设置参数值1
   * @param userParaConfigureParavalue1 String
   */
  public void setUserParaConfigureParavalue1(String userParaConfigureParavalue1){ 
  
     this.PARAVALUE1 = userParaConfigureParavalue1 ;        /* 参数值1 */    
   
  }
  
   /**
   * 设置参数值2
   * @param userParaConfigureParavalue2 String
   */
  public void setUserParaConfigureParavalue2(String userParaConfigureParavalue2){ 
   
     this.PARAVALUE2 = userParaConfigureParavalue2 ;        /* 参数值2 */    
  
  }
  
   /**
   * 设置参数值3
   * @param userParaConfigureParavalue3 String
   */
  public void setUserParaConfigureParavalue3(String userParaConfigureParavalue3){ 
   
     this.PARAVALUE3 = userParaConfigureParavalue3 ;        /* 参数值3 */    
   
  }
  
   /**
   * 设置参数值4
   * @param userParaConfigureParavalue4 String
   */
  public void setUserParaConfigureParavalue4(String userParaConfigureParavalue4){ 
   
     this.PARAVALUE4 = userParaConfigureParavalue4 ;        /* 参数值4 */    
    
  }
  
   /**
   * 设置备注
   * @param userParaConfigureMemo String
   */
  public void setUserParaConfigureMemo(String userParaConfigureMemo){ 
   
     this.MEMO = userParaConfigureMemo ;        /* 备注 */    
   
  }
  
  
  

  /**
   * 获得参数序号
   * @return String
   */
  public String getUserParaConfigureSn(){
    return this.SN ;        /* 参数序号 */
  }
  
  /**
   * 获得参数类型
   * @return String
   */
  public String getUserParaConfigureApptype(){
    return this.APPTYPE ;        /* 参数类型 */
  }
  
  /**
   * 获得用户账号
   * @return String
   */
  public String getUserParaConfigureUseraccount(){
    return this.USERACCOUNT ;        /* 用户账号 */
  }
  
  /**
   * 获得用户名称
   * @return String
   */
  public String getUserParaConfigureUsername(){
    return this.USERNAME ;        /* 用户名称 */
  }
  
  /**
   * 获得用户编号
   * @return String
   */
  public String getUserParaConfigureUserid(){
    return this.USERID ;        /* 用户编号 */
  }
  
  /**
   * 获得用户级别
   * @return String
   */
  public String getUserParaConfigureUserlevel(){
    return this.USERLEVEL ;        /* 用户级别 */
  }
  
  /**
   * 获得应用名
   * @return String
   */
  public String getUserParaConfigureApplication(){
    return this.APPLICATION ;        /* 应用名 */
  }
  
  /**
   * 获得参数key
   * @return String
   */
  public String getUserParaConfigureParakey(){
    return this.PARAKEY ;        /* 参数key */
  }
  
  /**
   * 获得参数名称
   * @return String
   */
  public String getUserParaConfigureParaname(){
    return this.PARANAME ;        /* 参数名称 */
  }
  
  /**
   * 获得参数值1
   * @return String
   */
  public String getUserParaConfigureParavalue1(){
    return this.PARAVALUE1 ;        /* 参数值1 */
  }
  
  /**
   * 获得参数值2
   * @return String
   */
  public String getUserParaConfigureParavalue2(){
    return this.PARAVALUE2 ;        /* 参数值2 */
  }
  
  /**
   * 获得参数值3
   * @return String
   */
  public String getUserParaConfigureParavalue3(){
    return this.PARAVALUE3 ;        /* 参数值3 */
  }
  
  /**
   * 获得参数值4
   * @return String
   */
  public String getUserParaConfigureParavalue4(){
    return this.PARAVALUE4 ;        /* 参数值4 */
  }
  
  /**
   * 获得备注
   * @return String
   */
  public String getUserParaConfigureMemo(){
    return this.MEMO ;        /* 备注 */
  }
  
}
