package com.ccthanking.framework.wsdy;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ccthanking.common.EventManager;
import com.ccthanking.common.vo.EventVO;
import com.ccthanking.framework.base.BaseVO;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import java.io.*;
import java.net.URLEncoder;
import java.sql.*;

import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.QuerySet;
import org.dom4j.*;
import org.dom4j.io.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ccthanking.framework.dic.Dics;
import com.ccthanking.framework.wsdy.pub_blob.*;
import com.ccthanking.framework.model.requestJson;
import com.ccthanking.framework.params.ParaManager;
import com.ccthanking.framework.params.SysPara.SysParaConfigureVO;
import com.ccthanking.framework.plugin.AppInit;
import com.ccthanking.framework.util.*;
import com.ccthanking.framework.wsdy.doc_sequence.WswhSequence;
import com.ccthanking.framework.common.User;

import java.util.Collection;
import java.util.List;
import com.ccthanking.framework.wsdy.electron_print.*;
import com.ccthanking.framework.Globals;
import com.ccthanking.framework.common.OrgDept;
import com.ccthanking.framework.util.Pub;

import java.util.HashMap;
@Controller
@RequestMapping("/PubWS")
public class PubWS{
  public PubWS() {
  }
  boolean mutilrow = false;
  QuerySet mutqs = null;
  public static String FIELDEXTEN_WSWH = "1";//文书域
  public static String FIELDEXTEN_GDH = "2";//归档域
  public static String FIELDEXTEN_CONTENT = "3";//普通域
  public static String FIELDEXTEN_IMAGE = "4";//图片域
  public static String FIELDEXTEN_MIND = "5";//审批意见域
  public static String FIELDEXTEN_SEAL = "6";//盖章域
  public static String FIELDEXTEN_SEALANDMIND = "7";//盖章保护域
  public static String FIELDEXTEN_USER = "8";//审批人域
  public static String FIELDEXTEN_DATE = "9";//审批时间域
  public static String FIELDEXTEN_WRITE = "10";//签名域
  public static String FIELDEXTEN_WRITEANDMIND = "11";//签名保护域
  public static String FIELDEXTEN_12 = "12";//工作联系单中主要内容与要求的文本域

  public static String FILE_EXCEL = "1";//excel文件
  public static String FILE_HTML = "2";//html文件
  public static String FILE_MHT = "3";//mht文件

  public static String VARIABLE_FROM_STATIC = "1";//固定值
  public static String VARIABLE_FROM_SESSION = "2";//session值
  public static String VARIABLE_FROM_REQUEST = "3";//request值
  public static String VARIABLE_FROM_SJBH = "4";//事件值

  public static String YZLB_QM = "1"; //印章类别 签名
  public static String YZLB_YZ = "2"; //印章类别 印章

  public static String RULE_MUTIL_STYLE = "10";//多列多记录显示单元格样式规则(其他行)
  public static String RULE_MUTIL_STYLE_FIRST = "11";//多列多记录显示单元格样式规则(第一行)
  public static String RULE_MUTIL_PAGE = "20";//多页显示规则
  public static String RULE_MUTIL_PAGE_ROW = "21";//多页多记录显示规则
  public static String RULE_NULL_PAGE = "30";  //域为空时，显示“规则内容”
  /**
   * 文书模板表sql语句
   * @param templateid
   * @return
   */
  private String getWs_template(String templateid){
      String sql_ws_template = "select * from WS_TEMPLATE where WS_TEMPLATE_ID='" +templateid + "' ";
      return sql_ws_template;
  }
  /**
   * 文书模板域规则
   * @param templateid
   * @return
   */
  private String getWs_template_rule(String templateid){
      String sql_ws_template_rule ="select * from WS_TEMPLATE_RULE where  WS_TEMPLATE_ID='" + templateid +"'";
      return sql_ws_template_rule;
  }
  /**
   * 文书模板SQL表sql语句
   * @param templateid
   * @return
   */
  private String getWs_template_sql(String templateid){
      String sql_ws_template_sql ="select * from WS_TEMPLATE_SQL where  WS_TEMPLATE_ID='" + templateid +"'";
      return sql_ws_template_sql;
  }
  /**
   * 文书变量表sql语句
   * @param templateid
   * @param variable
   * @return
   */
  private String getVariable(String templateid,String variable){
      String sql_variable = "select * from ws_template_variable where WS_TEMPLATE_ID='"+templateid+"' and VARIABLE_NAME='"+variable +"'";
      return sql_variable;
  }
  /**
   * 打印预览
   * @param request
   * @param response
   * @param templateid
   * @param sjbh
   * @param ywlx
   * @param user
   * @param conn
   * @throws java.lang.Exception
   */
  
  public void  getPreviewWS(HttpServletRequest request,
                          HttpServletResponse response, String templateid,
                          String sjbh, String ywlx, User user,Connection conn)
  throws Exception
  {
      String template_name, template_type, template_lb, key,sybmjb,getno_hz,gzType,dcdy_flag,sfqm; //模板名，模板类型，模板类别,业务主键，取号方式，给号汉字描述，是否需要盖章，多次生成标志,是否签名
      String domain_id,sheet_id, line_id, column_id, domain_sql, dic, separator, fieldname,canEdit,domain_style; //sheet页，行，列，sql，代码项,域名称，是否可编辑，样式表

      String variable_name = null; //变量名
      Util util = new Util();
      mutilrow = false;//多条打印标示
      String orgid = null;
      if (user != null)
          orgid = user.getDepartment(); //登陆人的组织机构编号
      try {
          QuerySet qs_ws_template = DBUtil.executeQuery(getWs_template(templateid),null,conn);//查询WS_TEMPLATE表
          if (qs_ws_template.getRowCount() > 0) {
              template_name = qs_ws_template.getString(1, "WS_TEMPLATE_NAME");
              template_type = qs_ws_template.getString(1, "WS_FILE_TYPE");
              template_lb = qs_ws_template.getString(1, "WS_KIND");
              key = qs_ws_template.getString(1, "YW_KEY_VARIABLE");
              getno_hz = qs_ws_template.getString(1, "WS_NO_CHINESE");
              gzType = qs_ws_template.getString(1, "SFGZ");
              dcdy_flag = qs_ws_template.getString(1, "DCDY_FLAG");
              sybmjb = qs_ws_template.getString(1, "SYBMJB");
              sfqm =  qs_ws_template.getString(1, "SFQM");
          }
          else {
              throw new Exception("未查询到文书配置错误！");
          }
          if(true){
              QuerySet qs_ws_template_sql = DBUtil.executeQuery(getWs_template_sql(templateid),null,conn);
              QuerySet qs_ws_template_rule = DBUtil.executeQuery(getWs_template_rule(templateid),null,conn);

              Document domresult = DocumentFactory.getInstance().createDocument();
              Element root = domresult.addElement("PRINTDATA");//构造打印xml
              if (qs_ws_template_sql.getRowCount() != 0) {
                  for (int i = 0; i < qs_ws_template_sql.getRowCount(); i++) {
                      domain_id = qs_ws_template_sql.getString(i + 1,"DOMAIN_ID");
                      sheet_id = qs_ws_template_sql.getString(i + 1,"SHEET_ID");
                      line_id = qs_ws_template_sql.getString(i + 1, "LINE_ID");
                      column_id = qs_ws_template_sql.getString(i + 1,"COLUMN_ID");
                      domain_sql = qs_ws_template_sql.getString(i + 1,"DOMAIN_SQL");
                      dic = qs_ws_template_sql.getString(i + 1,"CODEPAGE");
                      separator = qs_ws_template_sql.getString(i + 1,"SEPARATOR");
                      fieldname = qs_ws_template_sql.getString(i + 1,"FIELDNAME");
                      canEdit = qs_ws_template_sql.getString(i + 1,"CANEDIT");
                      domain_style = qs_ws_template_sql.getString(i + 1,"DOMAIN_STYLE");

                       if (qs_ws_template_sql.getString(i + 1, "WSWH_FLAG").
                          equalsIgnoreCase(FIELDEXTEN_CONTENT)) {//普通域 and 不可编辑
                          if(Pub.empty(domain_sql)){
                              domain_sql = "";
                          }else{
                          domain_sql = util.ParseSQL(domain_sql);
                          domain_sql = getValue_sql(request, domain_sql,util.value,separator,templateid,sjbh,ywlx);
                          }
                          if (this.mutilrow == false) {
                              if ( (dic == null) ||(dic.equalsIgnoreCase(""))) {
                              }
                              else {
                                  domain_sql = getCodeName(dic,domain_sql, separator);
                              }
                              if (domain_sql == null) {
                                  domain_sql = "";
                              }
                              else if ("null".equals(domain_sql) ||"null ".equals(domain_sql)) {
                                  domain_sql = "";
                              }
                              //设置域的xml
                              Element recordItem = root.addElement("ROW");
                              recordItem.addAttribute("sheet_id",sheet_id);
                              recordItem.addAttribute("line_id",line_id);
                              recordItem.addAttribute("column_id",column_id);
                              recordItem.addAttribute("fieldname",fieldname);
                              recordItem.addAttribute("domain",qs_ws_template_sql.getString(i + 1, "WSWH_FLAG"));
                              if(!Pub.empty(domain_style))
                              recordItem.addAttribute("domain_style",domain_style);
                              //判断是否有规则
                              boolean hasRule = false;
                              if (qs_ws_template_rule.getRowCount() > 0)
                              {
                                  for (int k = 1;k <= qs_ws_template_rule.getRowCount();k++)
                                  {
                                      if (domain_id.equalsIgnoreCase(qs_ws_template_rule.getString(k,"DOMAIN_ID")))
                                      {
                                          Element recordItemR = recordItem.addElement("RULE");//定义多值项的行数
                                          recordItemR.addAttribute("ruletype",qs_ws_template_rule.getString(k, "RULETYPE"));
                                          recordItemR.addText(qs_ws_template_rule.getString(k, "RULETEXT"));
                                          hasRule = true;
                                      }
                                  }
                              }
                              if(hasRule)
                              {
                                  recordItem.addAttribute("hasrule","true");
                              }
                              recordItem.addText(domain_sql);
                          }
                          else {
                              if(this.mutqs.getRowCount()>0){
                                    Element recordItem = root.addElement("ROW");
                                    recordItem.addAttribute("sheet_id",sheet_id);
                                    recordItem.addAttribute("line_id",line_id);
                                    recordItem.addAttribute("column_id",column_id);
                                    recordItem.addAttribute("fieldname",fieldname);
                                    recordItem.addAttribute("domain",qs_ws_template_sql.getString(i + 1, "WSWH_FLAG"));
                                    if(!Pub.empty(canEdit))
                                    recordItem.addAttribute("domain_style",domain_style);
                                    recordItem.addAttribute("mutilrow","true");//标志为多值项
                                    for(int m = 0;m<this.mutqs.getRowCount();m++){
                                      Element recordItemR = recordItem.addElement("ROL");//定义多值项的行数
                                       for (int n = 0; n < mutqs.getColumnCount(); n++) {
                                           Element recordItemC = recordItemR.addElement("COL");//定义多值项的列数
                                            if(mutqs.getString(m + 1, n + 1)!=null)
                                           recordItemC.addText(mutqs.getString(m + 1, n + 1));
                                            else
                                           recordItemC.addText("&nbsp;");
                                       }
                                    }
                                    //判断是否有规则
                                    boolean hasRule = false;
                                    if (qs_ws_template_rule.getRowCount() > 0)
                                    {
                                        for (int k = 1;k <= qs_ws_template_rule.getRowCount();k++)
                                        {
                                            if (domain_id.equalsIgnoreCase(qs_ws_template_rule.getString(k,"DOMAIN_ID")))
                                            {
                                                Element recordItemR = recordItem.addElement("RULE");//定义多值项的行数
                                                recordItemR.addAttribute("ruletype",qs_ws_template_rule.getString(k, "RULETYPE"));
                                                recordItemR.addText(qs_ws_template_rule.getString(k, "RULETEXT"));
                                                hasRule = true;
                                            }
                                        }
                                    }
                                    if(hasRule)
                                    {
                                        recordItem.addAttribute("hasrule","true");
                                    }
                                    this.mutilrow = false;
                                    this.mutqs = null;
                              }
                          }
                      }
                      else if (qs_ws_template_sql.getString(i + 1,
                          "WSWH_FLAG").equalsIgnoreCase(FIELDEXTEN_IMAGE))
                      { //图片域
                          if (Pub.empty(domain_sql))
                          {
                              domain_sql = "";
                          }
                          else
                          {
                              domain_sql = util.ParseSQL(domain_sql);
                              domain_sql = getValue_sql(request, domain_sql,util.value, separator, templateid, sjbh,ywlx, java.sql.Types.BLOB);
                          }
                          if (Pub.empty(domain_sql))
                              domain_sql = "";
                          Element recordItem = root.addElement("ROW");
                          recordItem.addAttribute("sheet_id", sheet_id);
                          recordItem.addAttribute("line_id", line_id);
                          recordItem.addAttribute("column_id", column_id);
                          recordItem.addAttribute("fieldname", fieldname);
                          recordItem.addAttribute("domain",qs_ws_template_sql.getString(i + 1, "WSWH_FLAG"));
                          recordItem.addText(domain_sql);
                      }
                  }
              }

           String xmlString =domresult.asXML();
           if(xmlString.indexOf("UTF-8")>0){
             // xmlString =  xmlString.replaceAll("UTF-8","GB2312");//xml字符集转化
           }
           byte [] xmlBlob =xmlString.getBytes("UTF-8");
           BlobUtil blobUtil = new BlobUtil();
           //获得文书模版blob
           String ws_template_sql = "select WS_TEMPLATE from WS_TEMPLATE where WS_TEMPLATE_ID ='"+templateid+"'";
           byte[] templateBlob = blobUtil.get("blob",ws_template_sql);//文书模版blob

           SAXReader reader = new SAXReader();
           ByteArrayInputStream in = new ByteArrayInputStream(xmlBlob);
           Document doc = reader.read(in);
           TemplateUtil templateUtil = new TemplateUtil();
           if(FILE_EXCEL.equalsIgnoreCase(template_type)){//excel文件
              //templateUtil.printExcel(request,response,doc,templateBlob);
           }else if(FILE_HTML.equalsIgnoreCase(template_type)||FILE_MHT.equalsIgnoreCase(template_type)){//html文件,或mht文件
              StringBuffer buff = templateUtil.printHTML(template_type,request,response,templateid,sjbh,ywlx,"","0","0");
              response.setContentType("text/html;charset=UTF-8");
              Pub.writeMessage(response,buff.toString(),"UTF-8");
//              response.getWriter().write(buff.toString());
//              response.flushBuffer();
           }
          }

      }
      catch (Exception ex) {
          ex.printStackTrace();
          throw new Exception("文书打印错误："+ex.getMessage());
      }
  }

  /**
   * 形成打印的SQL
   * @param request
   * @param response
   * @param templateid
   * @param sjbh
   * @param ywlx
   * @param user
   * @throws java.lang.Exception
   */

  public void getPrintXml(HttpServletRequest request,
                          HttpServletResponse response, String templateid,
                          String sjbh, String ywlx, User user,Connection conn,String sfieldname,String spzt)
      throws Exception
  {
      String template_name, template_type, template_lb, key,sybmjb,getno_hz,gzType,dcdy_flag,sfqm; //模板名，模板类型，模板类别,业务主键，取号方式，给号汉字描述，是否需要盖章，多次生成标志,是否签名
      String domain_id,sheet_id, line_id, column_id, domain_sql, dic, separator, fieldname,canEdit,domain_style; //sheet页，行，列，sql，代码项
      String approverole,approvelevel,domaintype;
      String wswh = "";//文书文号
      String gdbh = "";//归档编号
      boolean printtag = false;//是否提取数据打印
      String insertSql = "";
      String variable_name = null; //变量名
      Util util = new Util();
      mutilrow = false;//多条打印标示
      mutqs = null;//多次打印结果集
      //WswhSequence sequence = new WswhSequence();//初始化给号类
      String orgid = null;
      if (user != null)
          orgid = user.getDepartment(); //登陆人的组织机构编号
      try {
          QuerySet qs_ws_template = DBUtil.executeQuery(getWs_template(templateid),null,conn);//查询WS_TEMPLATE表
          if (qs_ws_template.getRowCount() > 0) {
              template_name = qs_ws_template.getString(1, "WS_TEMPLATE_NAME");
              template_type = qs_ws_template.getString(1, "WS_FILE_TYPE");
              template_lb = qs_ws_template.getString(1, "WS_KIND");
              key = qs_ws_template.getString(1, "YW_KEY_VARIABLE");
              getno_hz = qs_ws_template.getString(1, "WS_NO_CHINESE");
              gzType = qs_ws_template.getString(1, "SFGZ");
              dcdy_flag = qs_ws_template.getString(1, "DCDY_FLAG");
              sybmjb = qs_ws_template.getString(1, "SYBMJB");
              sfqm =  qs_ws_template.getString(1, "SFQM");
          }
          else {
              throw new Exception("未查询到文书配置错误！");
          }
          //判断模版类型
//          template_type = getTemplate_type(template_type);
          //得到业务主健
          if (key != null) {
               key = getValue(request, key, templateid,sjbh,ywlx);
          }
          else {
              key = "";
          }
          //判断是否需要重新打印
          if (!"1".equals(dcdy_flag)) {
              printtag = queryCreateDetail(sjbh,ywlx, templateid,conn);
          }else{
              printtag = false;
          }

          if(printtag==false){
              QuerySet qs_ws_template_sql = DBUtil.executeQuery(getWs_template_sql(templateid),null,conn);
              //QuerySet qs_ws_template_rule = DBUtil.executeQuery(getWs_template_rule(templateid),null,conn);
              //Document domresult = DocumentFactory.getInstance().createDocument();
              //Element root = domresult.addElement("PRINTDATA");//构造打印xml
              if (qs_ws_template_sql.getRowCount() != 0) {
                  for (int i = 0; i < qs_ws_template_sql.getRowCount(); i++) {
                      domain_id = qs_ws_template_sql.getString(i + 1,"DOMAIN_ID");
                      sheet_id = qs_ws_template_sql.getString(i + 1,"SHEET_ID");
                      line_id = qs_ws_template_sql.getString(i + 1, "LINE_ID");
                      column_id = qs_ws_template_sql.getString(i + 1,"COLUMN_ID");
                      domain_sql = qs_ws_template_sql.getString(i + 1,"DOMAIN_SQL");
                      dic = qs_ws_template_sql.getString(i + 1,"CODEPAGE");
                      separator = qs_ws_template_sql.getString(i + 1,"SEPARATOR");
                      fieldname = qs_ws_template_sql.getString(i + 1,"FIELDNAME");
                      //alter by cbl
                      canEdit = qs_ws_template_sql.getString(i + 1,"CANEDIT") == null ? "":qs_ws_template_sql.getString(i + 1,"CANEDIT");
                      //alter by cbl end
                      domain_style = qs_ws_template_sql.getString(i+1,"DOMAIN_STYLE");
                      approverole = qs_ws_template_sql.getString(i+1,"APPROVEROLE");
                      approvelevel = qs_ws_template_sql.getString(i+1,"APPROVELEVEL");
                      domaintype = qs_ws_template_sql.getString(i+1,"DOMAIN_TYPE");
                      
                      if (qs_ws_template_sql.getString(i + 1, "WSWH_FLAG").    //判断域类别提取数据
                          equalsIgnoreCase(FIELDEXTEN_WSWH)) {//文号域
                          if(wswh!=null){//文书文号已经生成，就不再给号
                              if(wswh.length()==0){
                                  String bmbh = getBmbh(sybmjb, orgid, templateid, ywlx, conn, user);
                                  getno_hz = getHzms(request,getno_hz);//处理文号中显示单位名称
                                  //sequence.doInit(bmbh, templateid, getno_hz);
                                  //wswh = WswhSequence.getInstance().getSequenceID(bmbh,templateid,getno_hz,conn); //生成文号
                                  // update by guanchb -- 2008-04-05 -- start --
                                  // 增加 业务类型 和 事件编号 参数
                                  wswh = WswhSequence.getInstance().getSequenceID(ywlx,sjbh,bmbh,templateid,getno_hz,conn); //生成文号
                                  // update by guanchb -- 2008-04-05 -- end --
                              }
                          }
                          String id = new RandomGUID().toString();
                          insertSql = "insert into AP_PROCESS_WS(SPWSID,WSWH_FLAG,WS_TEMPLATE_ID,DOMAIN_TYPE,DOMAIN_VALUE,CODEPAGE,FIELDNAME,APPROVEROLE,APPROVELEVEL,CANEDIT,DOMAIN_STYLE,SJBH,YWLX,LRSJ,LRRID,LRRXM)"+
                        		  	" values('"+id+"','"+FIELDEXTEN_WSWH+"','"+templateid+"','"+domaintype+"','"+wswh+"','','"+fieldname+"','"+(approverole==null?"":approverole)+"','"+(approvelevel==null?"":approvelevel)+"','"+canEdit+"','"+(domain_style==null?"":domain_style)+"','"+sjbh+"','"+ywlx+"',SYSDATE,'"+user.getAccount()+"','"+user.getName()+"') ";
                          DBUtil.exec(conn, insertSql);

                      }else
                      if (qs_ws_template_sql.getString(i + 1, "WSWH_FLAG").
                          equalsIgnoreCase(FIELDEXTEN_GDH)) {//归档域
                          domain_sql = util.ParseSQL(domain_sql);
                          domain_sql = getValue_sql(request, domain_sql,util.value,separator,templateid,sjbh,ywlx);
                          if (domain_sql != null) {
                            domain_sql = domain_sql.trim();
                          }
                          gdbh = domain_sql;
                      }else if (qs_ws_template_sql.getString(i + 1, "WSWH_FLAG").
                          equalsIgnoreCase(FIELDEXTEN_CONTENT)) {//普通域

                          if(Pub.empty(domain_sql)){
                              domain_sql = "";
                          }else{
                          domain_sql = util.ParseSQL(domain_sql);
                          domain_sql = getValue_sql(request, domain_sql,util.value,separator,templateid,sjbh,ywlx);
                          }
                          if (this.mutilrow == false) {
                              if ( (dic == null) ||(dic.equalsIgnoreCase(""))) {
                              }
                              else {
                                  domain_sql = getCodeName(dic,domain_sql, separator);
                              }
                              if (domain_sql == null) {
                                  domain_sql = "";
                              }
                              else if ("null".equals(domain_sql) ||"null ".equals(domain_sql)) {
                                  domain_sql = "";
                              }

                              String id = new RandomGUID().toString();
                              insertSql = "insert into AP_PROCESS_WS(SPWSID,WSWH_FLAG,WS_TEMPLATE_ID,DOMAIN_TYPE,DOMAIN_VALUE,CODEPAGE,FIELDNAME,APPROVEROLE,APPROVELEVEL,CANEDIT,DOMAIN_STYLE,SJBH,YWLX,LRSJ,LRRID,LRRXM)"+
                            		  	" values('"+id+"','"+FIELDEXTEN_CONTENT+"','"+templateid+"','"+domaintype+"','"+domain_sql+"','"+(dic==null?"":dic)+"','"+fieldname+"','"+(approverole==null?"":approverole)+"','"+(approvelevel==null?"":approvelevel)+"','"+canEdit+"','"+(domain_style==null?"":domain_style)+"','"+sjbh+"','"+ywlx+"',SYSDATE,'"+user.getAccount()+"','"+user.getName()+"') ";
                              DBUtil.exec(conn, insertSql);
                          }

                      }
                      else if (qs_ws_template_sql.getString(i + 1,"WSWH_FLAG").equalsIgnoreCase(FIELDEXTEN_IMAGE)) {//图片域
                      }
                      else if (qs_ws_template_sql.getString(i + 1,"WSWH_FLAG").equalsIgnoreCase(FIELDEXTEN_MIND)) {//审批意见域：定义审批人的级别和角色
                          if(Pub.empty(domain_sql)){
                              domain_sql = "";
                          }else{
                        	  domain_sql = util.ParseSQL(domain_sql);
                        	  domain_sql = getValue_sql(request, domain_sql,util.value,separator,templateid,sjbh,ywlx);
                          }
                          if(Pub.empty(domain_sql)) domain_sql = "";
                          String id = new RandomGUID().toString();
                          insertSql = "insert into AP_PROCESS_WS(SPWSID,WSWH_FLAG,WS_TEMPLATE_ID,DOMAIN_TYPE,DOMAIN_VALUE,CODEPAGE,FIELDNAME,APPROVEROLE,APPROVELEVEL,CANEDIT,DOMAIN_STYLE,SJBH,YWLX,LRSJ,LRRID,LRRXM)"+
                        		  	" values('"+id+"','"+FIELDEXTEN_MIND+"','"+templateid+"','"+domaintype+"','','','"+fieldname+"','"+(approverole==null?"":approverole)+"','"+(approvelevel==null?"":approvelevel)+"','"+canEdit+"','"+(domain_style==null?"":domain_style)+"','"+sjbh+"','"+ywlx+"','','"+user.getAccount()+"','"+user.getName()+"') ";
                          DBUtil.exec(conn, insertSql);

  		            	  // xhb add start
                          // 发起人->马主任->发起人->多人->发起人->...循环下去
                          // ywlx為200502表示是收文，此時YJRYJ這個域既是发起人填写的区域又是审批人填写的区域
                          if("200502".equals(ywlx) && "YJRYJ".equals(fieldname)){
                        	  insertSql = "insert into AP_PROCESS_WS(SPWSID,WSWH_FLAG,WS_TEMPLATE_ID,DOMAIN_TYPE,DOMAIN_VALUE," 
                            		  + "CODEPAGE,FIELDNAME,APPROVEROLE,APPROVELEVEL,CANEDIT,DOMAIN_STYLE,SJBH,YWLX,LRSJ,LRRID,LRRXM)"
                            		  + " values('"+new RandomGUID().toString()+"','"+FIELDEXTEN_MIND+"','"+templateid+"','"+domaintype+"','"
                            		  + domain_sql+"','','"+fieldname+"','','"
                            		  + (approvelevel==null?"":approvelevel)+"','"+canEdit+"','"+(domain_style==null?"":domain_style)+"','"
                            		  + sjbh+"','"+ywlx+"',sysdate,'"+user.getAccount()+"','"+user.getName()+"') ";
                              DBUtil.exec(conn, insertSql);
                          }
  		            	  // xhb add end
                          
                        
                       }else if (qs_ws_template_sql.getString(i + 1,"WSWH_FLAG").equalsIgnoreCase(FIELDEXTEN_SEAL)) {//盖章域
                        }else if (qs_ws_template_sql.getString(i + 1,"WSWH_FLAG").equalsIgnoreCase(FIELDEXTEN_SEALANDMIND)) {//盖章保护域
                        }else if (qs_ws_template_sql.getString(i + 1,"WSWH_FLAG").equalsIgnoreCase(FIELDEXTEN_USER)) {//审批人
                        }else if (qs_ws_template_sql.getString(i + 1,"WSWH_FLAG").equalsIgnoreCase(FIELDEXTEN_DATE)) {//审批时间       
                        	  String id = new RandomGUID().toString();
                        	  insertSql = "insert into AP_PROCESS_WS(SPWSID,WSWH_FLAG,WS_TEMPLATE_ID,DOMAIN_TYPE,DOMAIN_VALUE,CODEPAGE,FIELDNAME,APPROVEROLE,APPROVELEVEL,CANEDIT,DOMAIN_STYLE,SJBH,YWLX,LRSJ,LRRID,LRRXM)"+
                          		  	    " values('"+id+"','"+FIELDEXTEN_DATE+"','"+templateid+"','','','','"+fieldname+"','"+(approverole==null?"":approverole)+"','"+(approvelevel==null?"":approvelevel)+"','"+canEdit+"','"+(domain_style==null?"":domain_style)+"','"+sjbh+"','"+ywlx+"','','"+user.getAccount()+"','"+user.getName()+"') ";
                              DBUtil.exec(conn, insertSql);
                        	
                        }else if (qs_ws_template_sql.getString(i + 1,"WSWH_FLAG").equalsIgnoreCase(FIELDEXTEN_WRITE)) {//签名域
                        	  String id = new RandomGUID().toString();
                              insertSql = "insert into AP_PROCESS_WS(SPWSID,WSWH_FLAG,WS_TEMPLATE_ID,DOMAIN_TYPE,DOMAIN_VALUE,CODEPAGE,FIELDNAME,APPROVEROLE,APPROVELEVEL,CANEDIT,DOMAIN_STYLE,SJBH,YWLX,LRSJ,LRRID,LRRXM)"+
                            		  	" values('"+id+"','"+FIELDEXTEN_WRITE+"','"+templateid+"','','','','"+fieldname+"','"+(approverole==null?"":approverole)+"','"+(approvelevel==null?"":approvelevel)+"','"+canEdit+"','"+(domain_style==null?"":domain_style)+"','"+sjbh+"','"+ywlx+"','','"+user.getAccount()+"','"+user.getName()+"') ";
                              DBUtil.exec(conn, insertSql);
                        	
                        }else if (qs_ws_template_sql.getString(i + 1,"WSWH_FLAG").equalsIgnoreCase(this.FIELDEXTEN_WRITEANDMIND)) {//签名保护域
                        } else if(qs_ws_template_sql.getString(i + 1,"WSWH_FLAG").equalsIgnoreCase(FIELDEXTEN_12)) {
                           String id = new RandomGUID().toString();
                           insertSql = "insert into AP_PROCESS_WS(SPWSID,WSWH_FLAG,WS_TEMPLATE_ID,DOMAIN_TYPE,DOMAIN_VALUE,CODEPAGE,FIELDNAME,APPROVEROLE,APPROVELEVEL,CANEDIT,DOMAIN_STYLE,SJBH,YWLX,LRSJ,LRRID,LRRXM)"+
                         		  	" values('"+id+"','"+FIELDEXTEN_12+"','"+templateid+"','"+domaintype+"','','"+(dic==null?"":dic)+"','"+fieldname+"','"+user.getAccount()+"','"+(approvelevel==null?"":approvelevel)+"','"+canEdit+"','"+(domain_style==null?"":domain_style)+"','"+sjbh+"','"+ywlx+"','','"+user.getAccount()+"','"+user.getName()+"') ";
                           DBUtil.exec(conn, insertSql);
                       }
                     }
              }

           //String xmlString =domresult.asXML();
           //if(xmlString.indexOf("UTF-8")>0){
             // xmlString =  xmlString.replaceAll("UTF-8","GB2312");//xml字符集转化
          // }
           //byte [] b = xmlString.getBytes("UTF-8");

           //维护pub_blob表
           Pub_BlobVO vo = new Pub_BlobVO();
           if (template_name != null)
           {
               if (!Pub.empty(sfieldname))
               {
                   vo.setFilename(sfieldname); //设置自定义文件名
               }
               else
               {
                   vo.setFilename(template_name); //文件名(文件描述)
               }
           }
           else
           {
               if (!Pub.empty(sfieldname))
               {
                   vo.setFilename(sfieldname); //设置自定义文件名
               }
           }
           if(template_lb!=null){
        	     String ws_kind[][] = DBUtil.query("select ws_template_type from ws_template_type where ws_template_id = '"+templateid+"' and ws_template_ywlx='"+ywlx+"'");
                 if(null == ws_kind || ws_kind[0][0]==null){
                	 ws_kind[0][0] = "";
                 }
        	     vo.setWslbdm(ws_kind[0][0]);//文书类别代码-区分用于审批
           }
           //vo.setMultimedia(b);
           vo.setWswh(wswh);//文书文号
           if(gdbh!=null)
           vo.setDah(gdbh);//档案号
           vo.setTbsj(Pub.getCurrentDate());//入库时间
           if(gzType!=null)
           vo.setSfgz(gzType);//是否盖章
           vo.setSjbh(sjbh);//事件编号
           vo.setYwlx(ywlx);//业务类型
           vo.setKey_id(key);//要素编号
           vo.setWs_template_id(templateid);//文书模板编号
           //added by andy 20080925 作废标识默认为0,审批状态默认为0
           vo.setZfbs("0");
           vo.setSpzt(spzt);
           //added by andy 20090109对于打包审批处理的文书，文书作废后，重新生成的文书应该重新加入到打包表中
           // modified by guanchb@2009-04-29 start
           // 问题描述：提请逮捕审批被退回两次，第三次提请领导审批通过，但是在归档页面制作的《提请逮捕意见书》在PUB_BLOB表中的SPZT字段值为7.应为9.导致在‘在办案件查询’模块中查询此文书时无法进行打印。
           // 解决方法：增加where条件：ws_template_id=templateid
           String query = " select fjbh from pub_blob where sjbh='"+sjbh+"' and ywlx='"+ywlx+"' and spzt='7' and zfbs='1' and ws_template_id='"+templateid+"' and rownum=1 ";
           // modified by guanchb@2009-04-29 end.
           QuerySet qs = DBUtil.executeQuery(query, null, conn);
           String old_fjbh = "";
           if(qs != null && qs.getRowCount()>0)
           {
        	   old_fjbh = qs.getString(1, 1);
        	   vo.setSpzt("7");
           }
           
           Pub_BlobAction action = new Pub_BlobAction();
           action.Insert(vo,conn);//插入pub_blob表
           if(qs != null && qs.getRowCount()>0)
           {	   
        	   String updateSpckws = " update ZA_ZFBA_JCXX_WS_SPCKWS set fjbh='"+vo.getFjbh()+"' where fjbh='"+old_fjbh+"'";
  	           DBUtil.executeUpdate(conn, updateSpckws, null);
           }
           //end;
          }else
          {
        	  if(spzt.equals("6") || spzt.equals("1"))
        	  {
        		  // modified by guanchb@2009-04-29 start
        		  // 增加where条件：作废标识（zfbs）
        		  String sql = " update pub_blob set spzt='"+spzt+"' where sjbh='"+sjbh+"' and ywlx='"+ywlx+"' and ws_template_id = '"+templateid+"' and (zfbs is null or zfbs='0') ";
        		  // modified by guanchb@2009-04-29 end.
        		  DBUtil.executeUpdate(conn, sql, null);
        	  }
          }

      }
      catch (Exception ex) {
          //sequence.temporaryWh();
          ex.printStackTrace();
          throw new Exception("文书打印错误："+ex.getMessage());
      }

  }
  
  /**
   * 形成打印的XML
   * @param request
   * @param response
   * @param templateid
   * @param sjbh
   * @param ywlx
   * @param user
   * @throws java.lang.Exception
   */
 
/*  public void getPrintXml(HttpServletRequest request,
                          HttpServletResponse response, String templateid,
                          String sjbh, String ywlx, User user,Connection conn,String sfieldname,String spzt)
      throws Exception
  {
      String template_name, template_type, template_lb, key,sybmjb,getno_hz,gzType,dcdy_flag,sfqm; //模板名，模板类型，模板类别,业务主键，取号方式，给号汉字描述，是否需要盖章，多次生成标志,是否签名
      String domain_id,sheet_id, line_id, column_id, domain_sql, dic, separator, fieldname,canEdit,domain_style; //sheet页，行，列，sql，代码项
      String wswh = "";//文书文号
      String gdbh = "";//归档编号
      boolean printtag = false;//是否提取数据打印

      String variable_name = null; //变量名
      Util util = new Util();
      mutilrow = false;//多条打印标示
      mutqs = null;//多次打印结果集
      //WswhSequence sequence = new WswhSequence();//初始化给号类
      String orgid = null;
      if (user != null)
          orgid = user.getDepartment(); //登陆人的组织机构编号
      try {
          QuerySet qs_ws_template = DBUtil.executeQuery(getWs_template(templateid),null,conn);//查询WS_TEMPLATE表
          if (qs_ws_template.getRowCount() > 0) {
              template_name = qs_ws_template.getString(1, "WS_TEMPLATE_NAME");
              template_type = qs_ws_template.getString(1, "WS_FILE_TYPE");
              template_lb = qs_ws_template.getString(1, "WS_KIND");
              key = qs_ws_template.getString(1, "YW_KEY_VARIABLE");
              getno_hz = qs_ws_template.getString(1, "WS_NO_CHINESE");
              gzType = qs_ws_template.getString(1, "SFGZ");
              dcdy_flag = qs_ws_template.getString(1, "DCDY_FLAG");
              sybmjb = qs_ws_template.getString(1, "SYBMJB");
              sfqm =  qs_ws_template.getString(1, "SFQM");
          }
          else {
              throw new Exception("未查询到文书配置错误！");
          }
          //判断模版类型
//          template_type = getTemplate_type(template_type);
          //得到业务主健
          if (key != null) {
               key = getValue(request, key, templateid,sjbh,ywlx);
          }
          else {
              key = "";
          }
          //判断是否需要重新打印
          if (!"1".equals(dcdy_flag)) {
              printtag = queryCreateDetail(sjbh,ywlx, templateid,conn);
          }else{
              printtag = false;
          }

          if(printtag==false){
              QuerySet qs_ws_template_sql = DBUtil.executeQuery(getWs_template_sql(templateid),null,conn);
              QuerySet qs_ws_template_rule = DBUtil.executeQuery(getWs_template_rule(templateid),null,conn);
              Document domresult = DocumentFactory.getInstance().createDocument();
              Element root = domresult.addElement("PRINTDATA");//构造打印xml
              if (qs_ws_template_sql.getRowCount() != 0) {
                  for (int i = 0; i < qs_ws_template_sql.getRowCount(); i++) {
                      domain_id = qs_ws_template_sql.getString(i + 1,"DOMAIN_ID");
                      sheet_id = qs_ws_template_sql.getString(i + 1,"SHEET_ID");
                      line_id = qs_ws_template_sql.getString(i + 1, "LINE_ID");
                      column_id = qs_ws_template_sql.getString(i + 1,"COLUMN_ID");
                      domain_sql = qs_ws_template_sql.getString(i + 1,"DOMAIN_SQL");
                      dic = qs_ws_template_sql.getString(i + 1,"CODEPAGE");
                      separator = qs_ws_template_sql.getString(i + 1,"SEPARATOR");
                      fieldname = qs_ws_template_sql.getString(i + 1,"FIELDNAME");
                      canEdit = qs_ws_template_sql.getString(i + 1,"CANEDIT");
                      domain_style = qs_ws_template_sql.getString(i+1,"DOMAIN_STYLE");


                      if (qs_ws_template_sql.getString(i + 1, "WSWH_FLAG").    //判断域类别提取数据
                          equalsIgnoreCase(FIELDEXTEN_WSWH)) {//文号域
                          if(wswh!=null){//文书文号已经生成，就不再给号
                              if(wswh.length()==0){
                                  String bmbh = getBmbh(sybmjb, orgid, templateid, ywlx, conn, user);
                                  getno_hz = getHzms(request,getno_hz);//处理文号中显示单位名称
                                  //sequence.doInit(bmbh, templateid, getno_hz);
                                  //wswh = WswhSequence.getInstance().getSequenceID(bmbh,templateid,getno_hz,conn); //生成文号
                                  // update by guanchb -- 2008-04-05 -- start --
                                  // 增加 业务类型 和 事件编号 参数
                                  wswh = WswhSequence.getInstance().getSequenceID(ywlx,sjbh,bmbh,templateid,getno_hz,conn); //生成文号
                                  // update by guanchb -- 2008-04-05 -- end --
                              }
                          }
                          //设置域的xml
                          Element recordItem = root.addElement("ROW");
                          recordItem.addAttribute("sheet_id",sheet_id);
                          recordItem.addAttribute("line_id",line_id);
                          recordItem.addAttribute("column_id",column_id);
                          recordItem.addAttribute("fieldname",fieldname);
                          recordItem.addAttribute("domain",qs_ws_template_sql.getString(i + 1, "WSWH_FLAG"));
                          if(!Pub.empty(domain_style))
                          recordItem.addAttribute("domain_style",domain_style);
                          recordItem.addText(wswh);

                      }else
                      if (qs_ws_template_sql.getString(i + 1, "WSWH_FLAG").
                          equalsIgnoreCase(FIELDEXTEN_GDH)) {//归档域
                          domain_sql = util.ParseSQL(domain_sql);
                          domain_sql = getValue_sql(request, domain_sql,util.value,separator,templateid,sjbh,ywlx);
                          if (domain_sql != null) {
                            domain_sql = domain_sql.trim();
                          }
                          gdbh = domain_sql;
                      }else if (qs_ws_template_sql.getString(i + 1, "WSWH_FLAG").
                          equalsIgnoreCase(FIELDEXTEN_CONTENT)) {//普通域

                          if(Pub.empty(domain_sql)){
                              domain_sql = "";
                          }else{
                          domain_sql = util.ParseSQL(domain_sql);
                          domain_sql = getValue_sql(request, domain_sql,util.value,separator,templateid,sjbh,ywlx);
                          }
                          if (this.mutilrow == false) {
                              if ( (dic == null) ||(dic.equalsIgnoreCase(""))) {
                              }
                              else {
                                  domain_sql = getCodeName(dic,domain_sql, separator);
                              }
                              if (domain_sql == null) {
                                  domain_sql = "";
                              }
                              else if ("null".equals(domain_sql) ||"null ".equals(domain_sql)) {
                                  domain_sql = "";
                              }
                              //设置域的xml
                              Element recordItem = root.addElement("ROW");
                              recordItem.addAttribute("sheet_id",sheet_id);
                              recordItem.addAttribute("line_id",line_id);
                              recordItem.addAttribute("column_id",column_id);
                              recordItem.addAttribute("fieldname",fieldname);
                              recordItem.addAttribute("domain",qs_ws_template_sql.getString(i + 1, "WSWH_FLAG"));
                              if(!Pub.empty(canEdit))
                                  recordItem.addAttribute("canedit", canEdit);
                              if (!Pub.empty(domain_style))
                                  recordItem.addAttribute("domain_style",
                                      domain_style);
                              //判断是否有规则
                              boolean hasRule = false;
                              if (qs_ws_template_rule.getRowCount() > 0)
                              {
                                  for (int k = 1;k <= qs_ws_template_rule.getRowCount();k++)
                                  {
                                      if (domain_id.equalsIgnoreCase(qs_ws_template_rule.getString(k,"DOMAIN_ID")))
                                      {
                                          Element recordItemR = recordItem.addElement("RULE");//定义多值项的行数
                                          recordItemR.addAttribute("ruletype",qs_ws_template_rule.getString(k, "RULETYPE"));
                                          recordItemR.addText(qs_ws_template_rule.getString(k, "RULETEXT"));
                                          hasRule = true;
                                      }
                                  }
                              }
                              if(hasRule)
                              {
                                  recordItem.addAttribute("hasrule","true");
                              }
                              recordItem.addText(domain_sql);
                          }
                          else {
                              if(this.mutqs.getRowCount()>0){
                                    Element recordItem = root.addElement("ROW");
                                    recordItem.addAttribute("sheet_id",sheet_id);
                                    recordItem.addAttribute("line_id",line_id);
                                    recordItem.addAttribute("column_id",column_id);
                                    recordItem.addAttribute("fieldname",fieldname);
                                    recordItem.addAttribute("domain",qs_ws_template_sql.getString(i + 1, "WSWH_FLAG"));
                                    if(!Pub.empty(canEdit))
                                    recordItem.addAttribute("domain_style",domain_style);
                                    recordItem.addAttribute("mutilrow","true");//标志为多值项
                                    for(int m = 0;m<this.mutqs.getRowCount();m++){
                                      Element recordItemR = recordItem.addElement("ROL");//定义多值项的行数
                                       for (int n = 0; n < mutqs.getColumnCount(); n++) {
                                           Element recordItemC = recordItemR.addElement("COL");//定义多值项的列数
                                           if(mutqs.getString(m + 1, n + 1)!=null)
                                             recordItemC.addText(mutqs.getString(m + 1, n + 1));
                                           else
                                               recordItemC.addText("&nbsp;");
                                       }
                                    }
                                    //判断是否有规则
                                    boolean hasRule = false;
                                    if (qs_ws_template_rule.getRowCount() > 0)
                                    {
                                        for (int k = 1;k <= qs_ws_template_rule.getRowCount();k++)
                                        {
                                            if (domain_id.equalsIgnoreCase(qs_ws_template_rule.getString(k,"DOMAIN_ID")))
                                            {
                                                Element recordItemR = recordItem.addElement("RULE");//定义多值项的行数
                                                recordItemR.addAttribute("ruletype",qs_ws_template_rule.getString(k, "RULETYPE"));
                                                recordItemR.addText(qs_ws_template_rule.getString(k, "RULETEXT"));
                                                hasRule = true;
                                            }
                                        }
                                    }
                                    if(hasRule)
                                    {
                                        recordItem.addAttribute("hasrule","true");
                                    }
                                    this.mutilrow = false;
                                    this.mutqs = null;
                              }
                          }
                      }
                      else if (qs_ws_template_sql.getString(i + 1,"WSWH_FLAG").equalsIgnoreCase(FIELDEXTEN_IMAGE)) {//图片域
                          if(Pub.empty(domain_sql)){
                              domain_sql = "";
                          }else{
                          domain_sql = util.ParseSQL(domain_sql);
                          domain_sql = getValue_sql(request, domain_sql,util.value,separator,templateid,sjbh,ywlx,java.sql.Types.BLOB);
                          }
                          if(Pub.empty(domain_sql)) domain_sql = "";
                          Element recordItem = root.addElement("ROW");
                          recordItem.addAttribute("sheet_id",sheet_id);
                          recordItem.addAttribute("line_id",line_id);
                          recordItem.addAttribute("column_id",column_id);
                          recordItem.addAttribute("fieldname",fieldname);
                          recordItem.addAttribute("domain",qs_ws_template_sql.getString(i + 1, "WSWH_FLAG"));
                          recordItem.addText(domain_sql);
                      }else if (qs_ws_template_sql.getString(i + 1,"WSWH_FLAG").equalsIgnoreCase(FIELDEXTEN_MIND)) {//审批意见域：定义审批人的级别和角色
                          if(Pub.empty(domain_sql)){
                              domain_sql = "";
                          }else{
                          domain_sql = util.ParseSQL(domain_sql);
                          domain_sql = getValue_sql(request, domain_sql,util.value,separator,templateid,sjbh,ywlx);
                          }
                          if(Pub.empty(domain_sql)) domain_sql = "";
                          domain_sql = domain_sql.trim();
                          Element recordItem = root.addElement("ROW");
                          recordItem.addAttribute("sheet_id",sheet_id);
                          recordItem.addAttribute("line_id",line_id);
                          recordItem.addAttribute("column_id",column_id);
                          recordItem.addAttribute("fieldname",fieldname);
                          recordItem.addAttribute("domain",qs_ws_template_sql.getString(i + 1, "WSWH_FLAG"));
                          recordItem.addAttribute("approverole",qs_ws_template_sql.getString(i + 1, "APPROVEROLE"));
                          recordItem.addAttribute("approvelevel",qs_ws_template_sql.getString(i + 1, "APPROVELEVEL"));
                          recordItem.addText(domain_sql);
                        }else if (qs_ws_template_sql.getString(i + 1,"WSWH_FLAG").equalsIgnoreCase(FIELDEXTEN_SEAL)) {//盖章域
                          Element recordItem = root.addElement("ROW");
                          recordItem.addAttribute("sheet_id",sheet_id);
                          recordItem.addAttribute("line_id",line_id);
                          recordItem.addAttribute("column_id",column_id);
                          recordItem.addAttribute("fieldname",fieldname);
                          recordItem.addAttribute("domain",qs_ws_template_sql.getString(i + 1, "WSWH_FLAG"));
                          recordItem.addAttribute("approverole",qs_ws_template_sql.getString(i + 1, "APPROVEROLE"));
                          recordItem.addAttribute("approvelevel",qs_ws_template_sql.getString(i + 1, "APPROVELEVEL"));
                        }else if (qs_ws_template_sql.getString(i + 1,"WSWH_FLAG").equalsIgnoreCase(FIELDEXTEN_SEALANDMIND)) {//盖章保护域
                          Element recordItem = root.addElement("ROW");
                          recordItem.addAttribute("sheet_id",sheet_id);
                          recordItem.addAttribute("line_id",line_id);
                          recordItem.addAttribute("column_id",column_id);
                          recordItem.addAttribute("fieldname",fieldname);
                          recordItem.addAttribute("domain",qs_ws_template_sql.getString(i + 1, "WSWH_FLAG"));
                          recordItem.addAttribute("approverole",qs_ws_template_sql.getString(i + 1, "APPROVEROLE"));
                          recordItem.addAttribute("approvelevel",qs_ws_template_sql.getString(i + 1, "APPROVELEVEL"));
                        }else if (qs_ws_template_sql.getString(i + 1,"WSWH_FLAG").equalsIgnoreCase(FIELDEXTEN_USER)) {//审批人
                          Element recordItem = root.addElement("ROW");
                          recordItem.addAttribute("sheet_id",sheet_id);
                          recordItem.addAttribute("line_id",line_id);
                          recordItem.addAttribute("column_id",column_id);
                          recordItem.addAttribute("fieldname",fieldname);
                          recordItem.addAttribute("domain",qs_ws_template_sql.getString(i + 1, "WSWH_FLAG"));
                          recordItem.addAttribute("approverole",qs_ws_template_sql.getString(i + 1, "APPROVEROLE"));
                          recordItem.addAttribute("approvelevel",qs_ws_template_sql.getString(i + 1, "APPROVELEVEL"));
                        }else if (qs_ws_template_sql.getString(i + 1,"WSWH_FLAG").equalsIgnoreCase(FIELDEXTEN_DATE)) {//审批时间
                          Element recordItem = root.addElement("ROW");
                          recordItem.addAttribute("sheet_id",sheet_id);
                          recordItem.addAttribute("line_id",line_id);
                          recordItem.addAttribute("column_id",column_id);
                          recordItem.addAttribute("fieldname",fieldname);
                          recordItem.addAttribute("domain",qs_ws_template_sql.getString(i + 1, "WSWH_FLAG"));
                          recordItem.addAttribute("approverole",qs_ws_template_sql.getString(i + 1, "APPROVEROLE"));
                          recordItem.addAttribute("approvelevel",qs_ws_template_sql.getString(i + 1, "APPROVELEVEL"));
                        }else if (qs_ws_template_sql.getString(i + 1,"WSWH_FLAG").equalsIgnoreCase(FIELDEXTEN_WRITE)) {//签名域
                          Element recordItem = root.addElement("ROW");
                          recordItem.addAttribute("sheet_id",sheet_id);
                          recordItem.addAttribute("line_id",line_id);
                          recordItem.addAttribute("column_id",column_id);
                          recordItem.addAttribute("fieldname",fieldname);
                          recordItem.addAttribute("domain",qs_ws_template_sql.getString(i + 1, "WSWH_FLAG"));
                          recordItem.addAttribute("approverole",qs_ws_template_sql.getString(i + 1, "APPROVEROLE"));
                          recordItem.addAttribute("approvelevel",qs_ws_template_sql.getString(i + 1, "APPROVELEVEL"));
                        }else if (qs_ws_template_sql.getString(i + 1,"WSWH_FLAG").equalsIgnoreCase(this.FIELDEXTEN_WRITEANDMIND)) {//签名保护域
                          Element recordItem = root.addElement("ROW");
                          recordItem.addAttribute("sheet_id",sheet_id);
                          recordItem.addAttribute("line_id",line_id);
                          recordItem.addAttribute("column_id",column_id);
                          recordItem.addAttribute("fieldname",fieldname);
                          recordItem.addAttribute("domain",qs_ws_template_sql.getString(i + 1, "WSWH_FLAG"));
                          recordItem.addAttribute("approverole",qs_ws_template_sql.getString(i + 1, "APPROVEROLE"));
                          recordItem.addAttribute("approvelevel",qs_ws_template_sql.getString(i + 1, "APPROVELEVEL"));
                       }
                      }
              }

           String xmlString =domresult.asXML();
           if(xmlString.indexOf("UTF-8")>0){
             // xmlString =  xmlString.replaceAll("UTF-8","GB2312");//xml字符集转化
           }
           byte [] b = xmlString.getBytes("UTF-8");

           //维护pub_blob表
           Pub_BlobVO vo = new Pub_BlobVO();
           if (template_name != null)
           {
               if (!Pub.empty(sfieldname))
               {
                   vo.setFilename(sfieldname); //设置自定义文件名
               }
               else
               {
                   vo.setFilename(template_name); //文件名(文件描述)
               }
           }
           else
           {
               if (!Pub.empty(sfieldname))
               {
                   vo.setFilename(sfieldname); //设置自定义文件名
               }
           }
           if(template_lb!=null){
        	     String ws_kind[][] = DBUtil.query("select ws_template_type from ws_template_type where ws_template_id = '"+templateid+"' and ws_template_ywlx='"+ywlx+"'");
                 if(null == ws_kind || ws_kind[0][0]==null){
                	 ws_kind[0][0] = "";
                 }
        	     vo.setWslbdm(ws_kind[0][0]);//文书类别代码-区分用于审批
           }
           vo.setMultimedia(b);
           vo.setWswh(wswh);//文书文号
           if(gdbh!=null)
           vo.setDah(gdbh);//档案号
           vo.setTbsj(Pub.getCurrentDate());//入库时间
           if(gzType!=null)
           vo.setSfgz(gzType);//是否盖章
           vo.setSjbh(sjbh);//事件编号
           vo.setYwlx(ywlx);//业务类型
           vo.setKey_id(key);//要素编号
           vo.setWs_template_id(templateid);//文书模板编号
           //added by andy 20080925 作废标识默认为0,审批状态默认为0
           vo.setZfbs("0");
           vo.setSpzt(spzt);
           //added by andy 20090109对于打包审批处理的文书，文书作废后，重新生成的文书应该重新加入到打包表中
           // modified by guanchb@2009-04-29 start
           // 问题描述：提请逮捕审批被退回两次，第三次提请领导审批通过，但是在归档页面制作的《提请逮捕意见书》在PUB_BLOB表中的SPZT字段值为7.应为9.导致在‘在办案件查询’模块中查询此文书时无法进行打印。
           // 解决方法：增加where条件：ws_template_id=templateid
           String query = " select fjbh from pub_blob where sjbh='"+sjbh+"' and ywlx='"+ywlx+"' and spzt='7' and zfbs='1' and ws_template_id='"+templateid+"' and rownum=1 ";
           // modified by guanchb@2009-04-29 end.
           QuerySet qs = DBUtil.executeQuery(query, null, conn);
           String old_fjbh = "";
           if(qs != null && qs.getRowCount()>0)
           {
        	   old_fjbh = qs.getString(1, 1);
        	   vo.setSpzt("7");
           }
           
           Pub_BlobAction action = new Pub_BlobAction();
           action.Insert(vo,conn);//插入pub_blob表
           if(qs != null && qs.getRowCount()>0)
           {	   
        	   String updateSpckws = " update ZA_ZFBA_JCXX_WS_SPCKWS set fjbh='"+vo.getFjbh()+"' where fjbh='"+old_fjbh+"'";
  	           DBUtil.executeUpdate(conn, updateSpckws, null);
           }
           //end;
          }else
          {
        	  if(spzt.equals("6") || spzt.equals("1"))
        	  {
        		  // modified by guanchb@2009-04-29 start
        		  // 增加where条件：作废标识（zfbs）
        		  String sql = " update pub_blob set spzt='"+spzt+"' where sjbh='"+sjbh+"' and ywlx='"+ywlx+"' and ws_template_id = '"+templateid+"' and (zfbs is null or zfbs='0') ";
        		  // modified by guanchb@2009-04-29 end.
        		  DBUtil.executeUpdate(conn, sql, null);
        	  }
          }

      }
      catch (Exception ex) {
          //sequence.temporaryWh();
          ex.printStackTrace();
          throw new Exception("文书打印错误："+ex.getMessage());
      }

  }*/

  /**
   * 获得文书汉字描述部分
   * @ 替换本单位
   * # 替换上级单位
   * @param request
   * @param vHzms
   * @return
 * @throws Exception 
   */
  public String getHzms(HttpServletRequest request,String vHzms) throws Exception{
      String hzms = vHzms;
      if(!Pub.empty(hzms))
      {
          User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
          if(user!=null){
              OrgDept dept = user.getOrgDept();
              int level = dept.getDeptLevel();
              String pcs = "";
              String fj = "";
              String sj = "";
              if(level == 4){
                  pcs = dept.getBmjc();
                  fj = dept.getParent().getBmjc();
                  sj = dept.getParent().getParent().getBmjc();
              }else if(level ==3){
                  fj = dept.getBmjc();
                  sj = dept.getParent().getBmjc();
              }else if(level ==2){
                  sj = dept.getBmjc();
              }


              if (hzms.indexOf("@") >= 0)
              { //替换本单位
                  hzms = hzms.substring(0, hzms.indexOf("@")) + dept.getBmjc() +
                      hzms.substring(hzms.indexOf("@") + 1, hzms.length());
              }
              if (hzms.indexOf("#") >= 0)
              { //替换上级单位
                  hzms = hzms.substring(0, hzms.indexOf("#")) +
                      dept.getParent().getBmjc() +
                      hzms.substring(hzms.indexOf("#") + 1, hzms.length());
              }
              if (hzms.indexOf("$") > 0)//替换派出所
              {
                  hzms = hzms.substring(0, hzms.indexOf("$")) + pcs +
                      hzms.substring(hzms.indexOf("$") + 1, hzms.length());
              }
              if (hzms.indexOf("%") > 0)//替换分局
              {
                  hzms = hzms.substring(0, hzms.indexOf("%")) + fj +
                      hzms.substring(hzms.indexOf("%") + 1, hzms.length());
              }
              if (hzms.indexOf("&") > 0)//替换市局
              {
                  hzms = hzms.substring(0, hzms.indexOf("&")) + sj +
                      hzms.substring(hzms.indexOf("&") + 1, hzms.length());
              }

          }
      }
      return hzms;
  }

  /**
   * 通用文书打印读取配置表信息
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return
   * @throws java.lang.Exception
   */
  @RequestMapping(params = "createSP", method = RequestMethod.POST)
  public void createSP(HttpServletRequest request,HttpServletResponse response) throws Exception {
    String templateid = request.getParameter("templateid"); //模板id
    String ywlx = request.getParameter("ywlx");//业务类型
    String operationoid = request.getParameter("operationoid");//审批编号

    User user = (User)request.getSession().getAttribute(com.ccthanking.framework.Globals.USER_KEY);
    Connection conn = null;
    String sjbh = null;
    String ywid = null;
    try
    {
        conn = DBUtil.getConnection();
        conn.setAutoCommit(false);
        String sql = "select AP_PROCESS_ID,sjbh,ywlx from AP_PROCESSCONFIG t where ywlx = '"+ywlx+"' " ;
        if(templateid!=null&&templateid.length()>0)
        {
        	sql += " and ws_templateid='"+templateid+"'";
        }
            sql += " and operationoid='"+operationoid+"' and spzt='0' and lrr='"+user.getAccount()+"'";
        String result[][] = DBUtil.query(conn, sql);
        if(result == null || result.length == 0)
        {
        	//sql = "insert into AP_PROCESSCONFIG ";
        	ywid = new RandomGUID().toString();
        	EventVO eventVO = EventManager.createEvent(conn,ywlx, user);//生成事件
			sjbh = eventVO.getSjbh();
        	String insert_sql =
                    "insert into AP_PROCESSCONFIG (AP_PROCESS_ID,WS_TEMPLATEID,YWLX,SJBH,LRR,LRSJ,operationoid) values('" +
                    ywid + "','" + templateid+ "','" + ywlx + "','"+sjbh+"','"+user.getAccount()+"',SYSDATE,'"+operationoid+"')";
                DBUtil.execSql(conn, insert_sql);
            
        }else
        {
        	sjbh = result[0][1];
        	ywid = result[0][0];
        }
        
        conn.commit();
        JSONObject jo = new JSONObject();
        jo.put("MSG", "1");
        jo.put("SJBH", sjbh);
        jo.put("YWLX", ywlx);
        jo.put("YWID", ywid);
        Pub.writeMessage(response, jo.toString(),"UTF-8");
    }
    catch(Exception e)
    {
        conn.rollback();
        e.printStackTrace();
        JSONObject jo = new JSONObject();
        jo.put("MSG", "0");
        jo.put("ERROR", e.toString());
        Pub.writeMessage(response, jo.toString(),"UTF-8");
    }
    finally
    {
        if(conn != null) conn.close();
        //conn = null;
    }
   // return null;
  }

  /**
   * 通用文书打印读取配置表信息
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return
   * @throws java.lang.Exception
   */
  @RequestMapping(params = "createSPYW", method = RequestMethod.POST)
  public void createSPYW(HttpServletRequest request,HttpServletResponse response) throws Exception {
    String sjbh = request.getParameter("sjbh"); //模板id
    String ywlx = request.getParameter("ywlx");//业务类型
    String condition = request.getParameter("condition");//审批条件

    User user = (User)request.getSession().getAttribute(com.ccthanking.framework.Globals.USER_KEY);
	OrgDept dept = user.getOrgDept();
    Connection conn = null;
   // String sjbh = null;
    String ywid = null;
    String templateid = "";
    String operationoid = "";
    String processtype = "";
    try
    {
        conn = DBUtil.getConnection();
        conn.setAutoCommit(false);
        String sql = "select AP_PROCESS_ID,sjbh,ywlx from AP_PROCESSCONFIG t where ywlx = '"+ywlx+"' " ;
        
            sql += " and sjbh='"+sjbh+"' and spzt='0' and lrr='"+user.getAccount()+"'";
        String result[][] = DBUtil.query(conn, sql);
        if(result == null || result.length == 0)
        {
        	//sql = "insert into AP_PROCESSCONFIG ";
        	ywid = new RandomGUID().toString();
        	EventVO eventVO = EventManager.createEvent(conn,ywlx, user);//生成事件
			sjbh = eventVO.getSjbh();
        	String insert_sql =
                    "insert into AP_PROCESSCONFIG (AP_PROCESS_ID,WS_TEMPLATEID,YWLX,SJBH,LRR,LRSJ,operationoid) values('" +
                    ywid + "','','" + ywlx + "','"+sjbh+"','"+user.getAccount()+"',SYSDATE,'')";
                DBUtil.execSql(conn, insert_sql);
            
        }
        	
        	String  selectsql = "select t.ywlx,t.ws_template_id,d.name,d.operationoid,d.processtype from AP_WS_TYPZ t,AP_PROCESSTYPE d where t.operationoid = d.processtypeoid";
        	selectsql += " and t.ywlx = '"+ywlx+"'";
        	if(!Pub.empty(condition)){
        		selectsql += " and t.condition = '"+condition+"'";
        	}
        	
        	// 原来的位置错了。 add by xhb
        	selectsql += " and deptid in('"+dept.getDeptID()+"'";
			while (!("100000000000".equals(dept.getDeptID()))) {
				dept = dept.getParent();
				if(dept==null)
					break;
				selectsql+= ",'"+dept.getDeptID()+"'";

			}
			selectsql += ") order by deptid ";
			String selrs[][]  = DBUtil.query(conn, selectsql);
			//templateid,ywlx,operationoid,processtype,sjbh
			if(selrs != null){
				templateid = selrs[0][1];
				operationoid =selrs[0][3];
				processtype = selrs[0][4];
			}
        
        
        conn.commit();
        JSONObject jo = new JSONObject();
        jo.put("MSG", "1");
        jo.put("YWLX", ywlx);

        //
        jo.put("templateid", templateid);
        jo.put("operationoid", operationoid);
        jo.put("processtype", processtype);
        Pub.writeMessage(response, jo.toString(),"UTF-8");
    }
    catch(Exception e)
    {
        conn.rollback();
        e.printStackTrace();
        JSONObject jo = new JSONObject();
        jo.put("MSG", "0");
        jo.put("ERROR", e.toString());
        Pub.writeMessage(response, jo.toString(),"UTF-8");
    }
    finally
    {
        if(conn != null) conn.close();
        //conn = null;
    }
   // return null;
  }
   
   @RequestMapping(params = "getProIsover", method = RequestMethod.POST)
  public void getProIsover(HttpServletRequest request,HttpServletResponse response) throws Exception {
    String sjbh = request.getParameter("sjbh"); //模板id
    String ywlx = request.getParameter("ywlx");//业务类型
   
    Connection conn = null;
   // String sjbh = null;
   
    String isSpover = "0";
    String isview = "";
    try
    {
        conn = DBUtil.getConnection();
        conn.setAutoCommit(false);
        String sql = "select count(*) from AP_PROCESSINFO t, AP_TASK_SCHEDULE a where t.eventid = '"+sjbh+"' and t.result = '1'  and t.eventid = a.sjbh  and a.rwzt = '01' and t.cjrid = a.dbryid" ;
        
            
        String result[][] = DBUtil.query(conn, sql);
        if(result != null)
        {
        	isview = result[0][0];
        	if(!"".equals(isview)){
        		if("0".equals(isview)){
        			isSpover = "0";
        		}else{
        			isSpover = "1";
        		}
        	}
            
        }
        	
        
        JSONObject jo = new JSONObject();
        jo.put("MSG", "1");
        jo.put("YWLX", ywlx);

        //
        jo.put("isSpover", isSpover);
        Pub.writeMessage(response, jo.toString(),"UTF-8");
    }
    catch(Exception e)
    {
        conn.rollback();
        e.printStackTrace();
        JSONObject jo = new JSONObject();
        jo.put("MSG", "0");
        jo.put("ERROR", e.toString());
        jo.put("isSpover", "0");
        Pub.writeMessage(response, jo.toString(),"UTF-8");
    }
    finally
    {
        if(conn != null) conn.close();
        //conn = null;
    }
   // return null;
  }
  
  @RequestMapping(params = "getProwsid", method = RequestMethod.POST)
  public void getProwsid(HttpServletRequest request,HttpServletResponse response) throws Exception {
   
    String ywlx = request.getParameter("ywlx");//业务类型
    String operationoid = request.getParameter("operationoid");//审批条件

    Connection conn = null;
   // String sjbh = null;
    String wsid = "";
    try
    {
        conn = DBUtil.getConnection();
        conn.setAutoCommit(false);
        String sql = "select ws_template_id from ap_ws_typz t where t.ywlx = '"+ywlx+"' and t.operationoid = '"+operationoid+"' " ;
        
            
        String result[][] = DBUtil.query(conn, sql);
			if(result != null){
				wsid = result[0][0];

			}
        
        
        conn.commit();
        JSONObject jo = new JSONObject();
        jo.put("MSG", "1");
        jo.put("wsid", wsid);
        //
        Pub.writeMessage(response, jo.toString(),"UTF-8");
    }
    catch(Exception e)
    {
        conn.rollback();
        e.printStackTrace();
        JSONObject jo = new JSONObject();
        jo.put("MSG", "0");
        jo.put("ERROR", e.toString());
        Pub.writeMessage(response, jo.toString(),"UTF-8");
    }
    finally
    {
        if(conn != null) conn.close();
    }
  }
  
  @RequestMapping(params = "getSproles", method = RequestMethod.POST)
  @ResponseBody
  public void getSproles(HttpServletRequest request,HttpServletResponse response) throws Exception {
    String templateid = request.getParameter("templateid"); //模板id
    String ywlx = request.getParameter("ywlx");//业务类型
    String condition = request.getParameter("condition") == null?"":request.getParameter("condition");

    User user = (User)request.getSession().getAttribute(com.ccthanking.framework.Globals.USER_KEY);
    Connection conn = null;

    JSONObject jo = new JSONObject();
    String userroles  = user.getRoleListIdString();
    boolean hasroles = false;
    try
    {
        conn = DBUtil.getConnection();
        conn.setAutoCommit(false);
        
        String conditionSql = condition != "" ? (" and condition = '" + condition + "'") : "";
        
        String sql = "select rolename from AP_WS_TYPZ where ywlx = '"+ywlx+"'" +
        		" and ws_template_id='" + templateid + "'" + conditionSql;

        String [][] rs = DBUtil.query(conn, sql);
        
		if(rs != null)
        {
			String[] roles = new String[rs.length];
        	for(int i= 0;i<rs.length;i++){
        		roles[i] = rs[i][0];
        	}
        	for(int j=0;j<roles.length;j++){
        		if(userroles.indexOf(roles[j])>=0){
        			hasroles = true;
        		}
        	}
        }
        
      
        if(hasroles == true){
        	  jo.put("MSG", "1");
        	 jo.put("hasroles", hasroles);
        }else{
        	  jo.put("MSG", "0");
        	 jo.put("hasroles", hasroles);
        }
       
        Pub.writeMessage(response, jo.toString(),"UTF-8");
    }
    catch(Exception e)
    {
        conn.rollback();
        e.printStackTrace();
        Pub.writeMessage(response, jo.toString(),"UTF-8");
    }
    finally
    {
        if(conn != null) conn.close();
        //conn = null;
    }
   // return null;
  }
  
  
  @RequestMapping(params = "getTsMethod", method = RequestMethod.POST)
  @ResponseBody
  public void getTsMethod(HttpServletRequest request,HttpServletResponse response) throws Exception {
	  
	    String operationoid = request.getParameter("operationoid");//业务类型
	    JSONObject jo = new JSONObject();
	    try
	    {

	        String sql = "select  a.processtype from  ap_processtype  a where operationoid = '"+operationoid+"'";
	        String [][] rs = DBUtil.query(sql);
	        if(rs != null)
	        {
				
	        		if(rs[0][0].equals("4")){
	        			  jo.put("MSG", "1");
	        	        	 jo.put("isTs", true);
	        		}else{
	        			jo.put("MSG", "0");
	   	        	 	jo.put("isTs", false);
	        		}
	        	
	        }else{
	        	
	        	 jo.put("MSG", "0");
	        	 jo.put("isTs", false);
	      
	        }
	       
	        Pub.writeMessage(response, jo.toString(),"UTF-8");
	    }
	    catch(Exception e)
	    {

	        e.printStackTrace();
	        Pub.writeMessage(response, jo.toString(),"UTF-8");
	    }
	    finally
	    {

	    }
  }
  
  /**
   * 通用文书打印读取配置表信息
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return
   * @throws java.lang.Exception
   */
  @RequestMapping(params = "getXMLPrintAction", method = RequestMethod.GET)
  public void getXMLPrintAction(HttpServletRequest request,HttpServletResponse response) throws Exception {
    String templateid = request.getParameter("templateid"); //模板id
    String sjbh = request.getParameter("sjbh");//事件编号
    String ywlx = request.getParameter("ywlx");//业务类型
    String isEdit = request.getParameter("isEdit");//是否编辑
    String fieldname = request.getParameter("fieldname"); //自定义文件名
    String isSp = request.getParameter("isSp");//是否为审批中打印文书
    String spzt = request.getParameter("spzt");//是否生成不带印章和签名的文书
    if(spzt == null || "".equals(spzt)) spzt = "9";
    //added by andy 重置成员变量 fieldname
    this.setFieldName("");
    if (!Pub.empty(fieldname))
    {
        setFieldName(fieldname);
    }

    User user = (User)request.getSession().getAttribute(com.ccthanking.framework.Globals.USER_KEY);
    Connection conn = null;
    try
    {
    	if(templateid==null||templateid.equals("null")||templateid.length()<=0)
    		return;
        conn = DBUtil.getConnection();
        conn.setAutoCommit(false);
        getPrintXml(request, response, templateid, sjbh, ywlx, user,conn,fieldname,spzt); //生成文书xml串
        conn.commit();
        print(request, response, templateid, sjbh, ywlx,isEdit,isSp,spzt); //打印显示
    }
    catch(Exception e)
    {
        conn.rollback();
        e.printStackTrace();
    }
    finally
    {
        if(conn != null) conn.close();
        conn = null;
    }
   // return null;
  }
  private String zFieldName = null;
  public void setFieldName (String zFieldName){
      this.zFieldName = zFieldName;
  }
  public String getFieldName (){
      return this.zFieldName;
  }
  /**
   * 获得文书xml action 用于只生成blob不显示功能
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return
   * @throws java.lang.Exception
   */
  public ActionForward getXMLAction(ActionMapping mapping, ActionForm form,
                               HttpServletRequest request,
                               HttpServletResponse response) throws Exception {
    String templateid = request.getParameter("templateid"); //模板id
    String sjbh = request.getParameter("sjbh");//事件编号
    String ywlx = request.getParameter("ywlx");//业务类型
    String fieldname = request.getParameter("fieldname"); //自定义文件名
    String isSp = request.getParameter("isSp");//是否为审批中打印文书
    //added by andy 重置成员变量 fieldname
    this.setFieldName("");
    if (!Pub.empty(fieldname))
    {
        setFieldName(fieldname);
    }
    User user = (User)request.getSession().getAttribute(com.ccthanking.framework.Globals.USER_KEY);
    Connection conn = null;
    try
    {
        conn = DBUtil.getConnection();
        conn.setAutoCommit(false);
        getPrintXml(request, response, templateid, sjbh, ywlx, user,conn,fieldname,"0"); //生成文书xml串
        conn.commit();
    }
    catch(Exception e)
    {
        conn.rollback();
        e.printStackTrace();
    }
    finally
    {
        if(conn != null) conn.close();
        conn = null;
    }


    return null;

  }
  /**
   * 打印预览action
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return
   * @throws java.lang.Exception
   */
  @RequestMapping(params = "getPreviewXMLAction", method = RequestMethod.GET)
  public void getPreviewXMLAction(HttpServletRequest request,HttpServletResponse response) throws Exception {
    String templateid = request.getParameter("templateid"); //模板id
    //String sjbh = request.getParameter("sjbh");//事件编号
    //String ywlx = request.getParameter("ywlx");//业务类型
    //String isSp = request.getParameter("isSp");//是否为审批中打印文书

    User user = (User)request.getSession().getAttribute(com.ccthanking.framework.Globals.USER_KEY);
    try
    {
        TemplateUtil templateUtil = new TemplateUtil();
        StringBuffer buff = templateUtil.printViewWS(request, response, templateid);
        response.setContentType("text/html;charset=UTF-8");
        Pub.writeMessage(response,buff.toString(),"UTF-8");
    }
    catch(Exception e)
    {
        e.printStackTrace();
    }
    finally
    {

    }


    //return null;

  }
  
  @RequestMapping(params = "getPreviewOldXMLAction", method = RequestMethod.GET)
  public void getPreviewOldXMLAction(HttpServletRequest request,HttpServletResponse response) throws Exception {
    String flaid = request.getParameter("flaid"); //审批申请ID
    String flaflwid = request.getParameter("flaflwid");//审批流ID
    String flwno = request.getParameter("flwno");//审批文书编号
    String isEdit = request.getParameter("isEdit");//是否可编辑

    User user = (User)request.getSession().getAttribute(com.ccthanking.framework.Globals.USER_KEY);
    try
    {
        
        TemplateUtil templateUtil = new TemplateUtil();
        StringBuffer buff = templateUtil.printOldHTML(request, response, flaid, flaflwid, flwno, isEdit);
        response.setContentType("text/html;charset=UTF-8");
        Pub.writeMessage(response,buff.toString(),"UTF-8");
    }
    catch(Exception e)
    {
        e.printStackTrace();
    }
    finally
    {

    }


    //return null;

  }
  
  
  @RequestMapping(params = "getPersonSignAction", method = RequestMethod.GET)
  public void getPersonSignAction(HttpServletRequest request,HttpServletResponse response) throws Exception {
    String filename = request.getParameter("filename"); //文件名称
    String filenametype = request.getParameter("filenametype"); //文件名称
    String rootPath = null;
    SysParaConfigureVO syspara = (SysParaConfigureVO) ParaManager.getInstance().getSysParameter("CCJGAccessory");;
	if (syspara != null) {
		rootPath = syspara.getSysParaConfigureParavalue1();
	}
    
    String path = rootPath+"\\PersonSign";
    File file = new File(path+"\\"+filename);
    DataInputStream in = null;
    ServletOutputStream op = null;
    //User user = (User)request.getSession().getAttribute(com.ccthanking.framework.Globals.USER_KEY);
    try
    {
    	if (file.exists()) {
            int bytes = 0;
            op = response.getOutputStream();

            //response.setContentType(ser.getMimeType(file));
            response.setContentLength((int) file.length());
            response.setHeader( "Content-Disposition", "inline; filename=\"" + filenametype + "\"" );

            byte[] bbuf = new byte[1024];
            in = new DataInputStream(new FileInputStream(file));

            while ((in != null) && ((bytes = in.read(bbuf)) != -1)) {
                op.write(bbuf, 0, bytes);
            }
            in.close();
            op.flush();
            op.close();
        }
    }
    catch(Exception e)
    {
        e.printStackTrace();
    }
    finally
    {
    	if(in!=null)
    		in.close();
    	if(op!=null)
    		op.close();

    }


    //return null;

  }
  
  @RequestMapping(params = "getOldFjAction")
  @ResponseBody
  public requestJson getOldFjAction(HttpServletRequest request,HttpServletResponse response) throws Exception {
    String filename = request.getParameter("filename"); //文件名称
    String filenametype = request.getParameter("filenametype"); //文件名称
	requestJson j = new requestJson();
    String rootPath = null;
    SysParaConfigureVO syspara = (SysParaConfigureVO) ParaManager.getInstance().getSysParameter("CCJGAccessory");;
	if (syspara != null) {
		rootPath = syspara.getSysParaConfigureParavalue1();
	}
    String filepath=request.getParameter("filepath"); //文件路径
    String path = rootPath+"\\"+filepath;
    File file = new File(path+"\\"+filename);
//    DataInputStream in = null;
//    ServletOutputStream op = null;
    //User user = (User)request.getSession().getAttribute(com.ccthanking.framework.Globals.USER_KEY);
    try
    {
    	if (file.exists()) {
//            int bytes = 0;
//            op = response.getOutputStream();
//            response.setCharacterEncoding("UTF-8");
//            String fileName = URLEncoder.encode(filenametype, "UTF-8");
//            //response.setContentType(ser.getMimeType(file));
//            response.setContentLength((int) file.length());
//            response.setHeader("Content-Disposition", "filename=\"" + fileName + "\"" );
//
//            byte[] bbuf = new byte[1024];
//            in = new DataInputStream(new FileInputStream(file));
//
//            while ((in != null) && ((bytes = in.read(bbuf)) != -1)) {
//                op.write(bbuf, 0, bytes);
//            }
//            in.close();
//            op.flush();
//            op.close();
//            
            String base = AppInit.appPath + "/file";
			String root = request.getContextPath() + "/file";
			String fileDir = "";
			String result = "";
			String copyName = filename.substring(0,filename.lastIndexOf("."))+filenametype.substring(filenametype.lastIndexOf("."),filenametype.length());
			// 文件保存路径：根路径/项目ID/附件ID
			fileDir = base + "/" + filepath + "/" + filename;
			// 文件夹不存在的话，需要创建文件夹，否则直接使用
			File copyFile = new File(fileDir);
			if (!copyFile.exists() && !copyFile.isDirectory()) {
				copyFile.mkdirs();
				copyFile = new File(fileDir);
				copyFile.mkdir();
				copyFile = new File(fileDir, copyName);
				copyFile.createNewFile();
				// -----------------------------------
				int bytes = 0;
				FileOutputStream copyop = new FileOutputStream(copyFile);
				byte[] bbuf = new byte[1024];
				FileInputStream copyin = new FileInputStream(file);
				while ((copyin != null) && ((bytes = copyin.read(bbuf)) != -1)) {
					copyop.write(bbuf, 0, bytes);
				}
				copyin.close();
				copyop.flush();
				copyop.close();
				// -----------------------------------
			} else {
				// do nothing
			}
			result = root + "/" + filepath + "/" +filename+ "/" + copyName;
			j.setMsg(result);
        }
    }catch(Exception e){
        e.printStackTrace();
    }finally{
    }
	return j;
  }
  /**
   * 显示生成文书
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return
   * @throws java.lang.Exception
   */
  @RequestMapping(params = "getPrintAction", method = RequestMethod.GET)
  public void getPrintAction(HttpServletRequest request,HttpServletResponse response) throws Exception {
    String templateid = request.getParameter("templateid"); //模板id
    String sjbh = request.getParameter("sjbh");//事件编号
    String ywlx = request.getParameter("ywlx");//业务类型
    String isEdit = request.getParameter("isEdit");//是否编辑
    String isSp = request.getParameter("isSp");//是否为审批中打印文书
    String spzt = request.getParameter("spzt");//审批状态
    if(spzt == null || "".equals(spzt)) spzt = "9";
    print(request, response, templateid, sjbh, ywlx,isEdit,isSp,spzt); //打印显示

    //return null;

  }
  
	@RequestMapping(params = "logout", method = RequestMethod.POST)
	protected ModelAndView logout(final HttpServletRequest request,
			final ModelAndView mav)
			throws Exception {
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		if (user != null) {
			com.ccthanking.framework.log.LogManager.writeLogoutLog(user);
		}
		request.getSession().invalidate();
		mav.setViewName("index");
		return mav;

	}
  //added by andy 20081211 根据除fjbh、wswh等字段查询文书
  public ActionForward getPrintActionByFilter(ActionMapping mapping, ActionForm form,
          HttpServletRequest request,
          HttpServletResponse response) throws Exception {
	String wswh = request.getParameter("wswh");//文书文号
	if(wswh == null) wswh = "";
	wswh = wswh.replaceAll("\\$", " ");
	String fjbh = request.getParameter("fjbh");//附件编号	
	// modified by guanchb@2009-03-16 start
	// 问题现象：在专管员接管物证模块中，点击文书文号链接，查看文书，显示的文书内容与所选物品的文书内容不同
	// 问题原因：同一个文书文号在PUB_BLOB表中有多条记录，所以根据文号取得文书信息时，有可能获取到了其他文书的信息
	// 解决办法：增加档案号（dah）过滤条件，即同一个案件下，清单类文书中相同的文书文号不会对应两个文书
	if(fjbh == null) fjbh = "";
	String dah = Pub.val(request, "dah");
	if("".equals(dah)){
		throw new Exception("无档案号信息，查看文书失败！");
	}	
	printByFilter(request, response, wswh, fjbh, dah); //打印显示
	// modified by guanchb@2009-03-16 end
	return null;

}
  public void printByFilter(HttpServletRequest request,HttpServletResponse response,String wswh,String fjbh, String dah) throws Exception{
      BlobUtil blobUtil = new BlobUtil();
      String templateType = "";//模版类型
	  // modified by guanchb@2009-03-16 start
	  // 问题现象：在专管员接管物证模块中，点击文书文号链接，查看文书，显示的文书内容与所选物品的文书内容不同
	  // 问题原因：同一个文书文号在PUB_BLOB表中有多条记录，所以根据文号取得文书信息时，有可能获取到了其他文书的信息
	  // 解决办法：增加档案号（dah）过滤条件，即同一个案件下，清单类文书中相同的文书文号不会对应两个文书
      String pub_blob_template_id = "select ws_template_id,sjbh,ywlx from PUB_BLOB where zfbs='0' and dah='"+dah+"' and wswh='"+wswh+"' order by tbsj desc";
      // modified by guanchb@2009-03-16 end
      QuerySet qs = DBUtil.executeQuery(pub_blob_template_id, null);
      String ws_template_id = qs.getString(1, 1);
      String sjbh = qs.getString(1, 2);
      String ywlx = qs.getString(1, 3);
      //add by hongf 2009-7-6 start 此处需要打开清单文书，调取证据清单和通知书的文书文号是一致的，所以如果ws_template_id=160（调取证据通知书），则改为显示161（调取证据清单）
      if(ws_template_id.equals("160"))
      {
    	  ws_template_id = "161";
      }
      //add by hongf 2009-7-6 end
      String pub_blob_sql = "select MULTIMEDIA from PUB_BLOB where WS_TEMPLATE_ID ='"+ws_template_id+"' and SJBH='"+sjbh+"' and YWLX='"+ywlx+"' and (ZFBS = '0' or ZFBS IS NULL) order by tbsj desc";
      //获得文书模版blob
      String ws_template_sql = "select WS_TEMPLATE from WS_TEMPLATE where WS_TEMPLATE_ID ='"+ws_template_id+"'";
      byte[] templateBlob = blobUtil.get("blob",ws_template_sql);//文书模版blob
      byte[] xmlBlob = blobUtil.get("blob",pub_blob_sql);//xml blob
      SAXReader reader = new SAXReader();
      ByteArrayInputStream in = new ByteArrayInputStream(xmlBlob);
      InputStreamReader isr = new InputStreamReader(in,"UTF-8");
      Document doc = reader.read(isr);//DocumentHelper.parseText(str);
      TemplateUtil templateUtil = new TemplateUtil();
      QuerySet qs_ws_template = DBUtil.executeQuery(getWs_template(ws_template_id),null);//查询WS_TEMPLATE表
          if(qs_ws_template.getRowCount()>0){
              templateType = qs_ws_template.getString(1, "WS_FILE_TYPE");
          }
      if(FILE_EXCEL.equalsIgnoreCase(templateType)){//excel文件
         //templateUtil.printExcel(request,response,doc,templateBlob);
      }else if(FILE_HTML.equalsIgnoreCase(templateType)||FILE_MHT.equalsIgnoreCase(templateType)){//html文件,或mht文件
         StringBuffer buff = templateUtil.printHTML(templateType,request,response,ws_template_id,sjbh,ywlx,"0","0","0");
         String strtt = buff.toString();

           response.setContentType("text/html;charset=UTF-8");
           Pub.writeMessage(response,strtt,"UTF-8");
//         response.getWriter().write(buff.toString());
//         response.flushBuffer();
      }
  }

  /**
   * 获得电子印章
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return
   * @throws java.lang.Exception
   */
  public ActionForward getAztXML(ActionMapping mapping, ActionForm form,
                               HttpServletRequest request,
                               HttpServletResponse response) throws Exception {
         BlobUtil blobUtil = new BlobUtil();
         String xh =request.getParameter("xh");
         String sql = "select MULTIMEDIA from ws_electron_print where xh='"+xh+"'";
         byte[] blob = blobUtil.get("blob",sql);//电子印章xml
         SAXReader reader = new SAXReader();
         ByteArrayInputStream in = new ByteArrayInputStream(blob);
         Document doc = reader.read(in);
         if(in!=null)
         in.close();

         OutputStream os = response.getOutputStream();
         if(Pub.empty("UTF-8"))
             os.write(doc.asXML().getBytes());
         else
             os.write(doc.asXML().getBytes("UTF-8"));
         os.flush();
         os.close();

    return null;

  }

  /**
   * 打印显示
   * @param request
   * @param response
   * @param templateid 模版编号
   * @param sjbh       事件编号
   * @param ywlx       业务类型
   * @throws java.lang.Exception
   */

  public void print(HttpServletRequest request,HttpServletResponse response,String templateid,String sjbh,String ywlx,String isEdit,String isSp,String spzt) throws Exception{
      //BlobUtil blobUtil = new BlobUtil();
      String templateType = "";//模版类型
      //获得文书模版blob
      //String ws_template_sql = "select WS_TEMPLATE from WS_TEMPLATE where WS_TEMPLATE_ID ='"+templateid+"'";
      //byte[] templateBlob = blobUtil.get("blob",ws_template_sql);//文书模版blob
      //获得打印xml blob
      //xukx 过滤掉作废的文书
      //String pub_blob_sql = "select MULTIMEDIA from PUB_BLOB where WS_TEMPLATE_ID ='"+templateid+"' and SJBH='"+sjbh+"' and YWLX='"+ywlx+"' and (ZFBS = '0' or ZFBS IS NULL) order by tbsj desc";
      //end
      //byte[] xmlBlob = blobUtil.get("blob",pub_blob_sql);//xml blob
      //SAXReader reader = new SAXReader();
     // ByteArrayInputStream in = new ByteArrayInputStream(xmlBlob);
      //InputStreamReader isr = new InputStreamReader(in,"UTF-8");
      //Document doc = reader.read(isr);//DocumentHelper.parseText(str);
      TemplateUtil templateUtil = new TemplateUtil();
      QuerySet qs_ws_template = DBUtil.executeQuery(getWs_template(templateid),null);//查询WS_TEMPLATE表
          if(qs_ws_template.getRowCount()>0){
              templateType = qs_ws_template.getString(1, "WS_FILE_TYPE");
          }
      if(FILE_EXCEL.equalsIgnoreCase(templateType)){//excel文件
         //templateUtil.printExcel(request,response,doc,templateBlob);
      }else if(FILE_HTML.equalsIgnoreCase(templateType)||FILE_MHT.equalsIgnoreCase(templateType)){//html文件,或mht文件
         StringBuffer buff = templateUtil.printHTML(templateType,request,response,templateid,sjbh,ywlx,isEdit,isSp,spzt);
         //String strtt = ;

         response.setContentType("text/html;charset=UTF-8");
         Pub.writeMessage(response,buff.toString(),"UTF-8");
//         response.getWriter().write(buff.toString());
//         response.flushBuffer();
      }
  }
  /**
   * 处理审批意见填充至文书模板
   * @param conn
   * @param fjbh
   * @param result
   * @param approverole
   * @param approvelevel
   * @throws java.lang.Exception
   */
  public void getSpYjXML(Connection conn,String fjbh,String result,String approverole,String approvelevel,User user)
        throws Exception
    {
        if(fjbh==null||"".equals(fjbh)){
            throw new Exception("fjbh为空，不能处理审批文书！");
        }
      BlobUtil blobUtil = new BlobUtil();
      String pub_blob_sql = "select MULTIMEDIA from PUB_BLOB where fjbh='"+fjbh+"' order by tbsj desc";
      byte[] xmlBlob = blobUtil.get("blob", pub_blob_sql); //xml blob
      Document doc = null;
      SAXReader reader = new SAXReader();
      ByteArrayInputStream in = null;
      InputStreamReader   isr  = null;
      try
      {
          in = new ByteArrayInputStream(xmlBlob);
          isr   =   new   InputStreamReader(in,"UTF-8");
          doc = reader.read(isr);
          List printRoot = doc.selectNodes("//PRINTDATA/ROW");
          for (int i = 0; i < printRoot.size(); i++)
          {

            Element ele = (Element) printRoot.get(i);
            if (ele.getNodeType() != Node.ELEMENT_NODE)
            {
                continue;
            }
            String domain = ele.attributeValue("domain");
            if (FIELDEXTEN_MIND.equals(domain)||FIELDEXTEN_USER.equals(domain)||FIELDEXTEN_DATE.equals(domain))
            {
                String domainrole = ele.attributeValue("approverole");
                String domainlevel = ele.attributeValue("approvelevel");
                if (Pub.empty(domainrole) && Pub.empty(domainlevel))
                { //如果文书定义的role和level都为空，那么直接将意见填入此域
                    ele.addAttribute("avail","true");
                    ele.setText(getSpValue(result,domain,user));
                }
                else if (!Pub.empty(domainrole) && Pub.empty(domainlevel))
                { //文书定义的role非空
                    if (Pub.empty(approverole))
                    {
                    }
                    else if (domainrole.equalsIgnoreCase(approverole))
                    { //节点role不为空，判断两个role
                        ele.addAttribute("avail","true");
                        ele.setText(getSpValue(result,domain,user));
                    }

                }
                else if (!Pub.empty(domainlevel) && Pub.empty(domainrole))
                { //节点level不为空
                    if (domainlevel.equals(approvelevel))
                    {
                        ele.addAttribute("avail","true");
                        ele.setText(getSpValue(result,domain,user));
                    }
                }
                else if (!Pub.empty(domainrole) && !Pub.empty(domainlevel))
                { //文书定义的role,level非空
                    if (Pub.empty(approverole))
                    { //判断节点的role为空，那么比较两个level
                        if (domainlevel.equals(approvelevel))
                        {
                            ele.addAttribute("avail","true");
                            ele.setText(getSpValue(result,domain,user));
                        }
                    }
                    else if (domainrole.equalsIgnoreCase(approverole))
                    { //节点role不为空，判断两个role
                        if(domainlevel.equalsIgnoreCase(approvelevel)){
                          ele.addAttribute("avail","true");
                          ele.setText(getSpValue(result,domain,user));
                        }
                    }
                }

            }

        }
    }
    catch (DocumentException ex)
    {
        ex.printStackTrace();
    }finally{
        if(in!=null)
            in.close();
        if (isr != null)
            isr.close();

    }
//      String xmlString =doc.asXML();
//      if(xmlString.indexOf("UTF-8")>0){
//        // xmlString =  xmlString.replaceAll("UTF-8","GB2312");//xml字符集转化
//      }
//      byte [] b =xmlString.getBytes("UTF-8");
//      String s = "update pub_blob set MULTIMEDIA=EMPTY_BLOB() WHERE fjbh='"+fjbh+"'";
//      DBUtil.exec(conn,s);
//      String sql = "select MULTIMEDIA from pub_blob WHERE fjbh='"+fjbh+"' for update ";
//      blobUtil.updateBlob(conn,sql,b);
  }
  
  
  /**
   * 处理审批意见填充至文书模板
   * @param conn
   * @param fjbh
   * @param result
   * @param approverole
   * @param approvelevel
   * @throws java.lang.Exception
   */
  public void getSpYjXML(Connection conn,String fjbh,String strwsbh,String sjbh,String ywlx,String spFieldname,String result,String approverole,String approvelevel,String flag,String autodoApprove,User user)
        throws Exception
    {
        if(fjbh==null||"".equals(fjbh)){
            throw new Exception("fjbh为空，不能处理审批文书！");
        }
		try {
            String id = new RandomGUID().toString();
            if("1".equals(autodoApprove)){
            	result = " ";
            }
            String objs[] = {result};
            
            String insertSql = "insert into AP_PROCESS_WS(SPWSID,WSWH_FLAG,WS_TEMPLATE_ID,DOMAIN_TYPE,DOMAIN_VALUE,CODEPAGE,FIELDNAME,APPROVEROLE,APPROVELEVEL,CANEDIT,DOMAIN_STYLE,SJBH,YWLX,LRSJ,LRRID,LRRXM,SPYLB)"+
          		  	" values('"+id+"','"+flag+"','"+strwsbh+"','',?,'','"+spFieldname+"','"+(approverole==null?"":approverole)+"','"+(approvelevel==null?"":approvelevel)+"','1','','"+sjbh+"','"+ywlx+"',SYSDATE,'"+user.getAccount()+"','"+user.getName()+"','"+autodoApprove+"') ";
            DBUtil.executeUpdate(conn, insertSql, objs);

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {

		}

  }
  private String getSpValue(String result,String domain,User user){
      if(FIELDEXTEN_MIND.equals(domain)){
          return result;
      }else
      if(FIELDEXTEN_USER.equals(domain)){
          String userName = user.getName();
          return userName;
      }else
      if(FIELDEXTEN_DATE.equals(domain)){
          String [][] dd = DBUtil.query("select to_char(sysdate,'yyyy\"年\"mm\"月\"dd\"日\"') from dual");
          return dd[0][0];
      }
      return "";


  }
  /**
   * 处理编辑域,填充至文书模板
   * @param conn
   * @param fjbh
   * @param result
   * @param approverole
   * @param approvelevel
   * @throws java.lang.Exception
   */
  public ActionForward editFieldXML(Connection conn,String templateid,String sjbh,String ywlx,String filename,String result)
      throws Exception
  {

      SAXReader readerEdit = new SAXReader();
      BlobUtil blobUtil = new BlobUtil();
      String pub_blob_sql = "select MULTIMEDIA from PUB_BLOB where  WS_TEMPLATE_ID ='"+templateid+"' and SJBH='" +sjbh + "' and YWLX='" + ywlx + "' order by tbsj desc";
      byte[] xmlBlob = blobUtil.get("blob", pub_blob_sql); //xml blob
      Document doc = null;
      SAXReader reader = new SAXReader();
      ByteArrayInputStream in = null;
      InputStreamReader   isr   =  null;
      try
      {

          in = new ByteArrayInputStream(xmlBlob);
          isr   =   new   InputStreamReader(in,"UTF-8");
          doc = reader.read(isr);
          List printRoot = doc.selectNodes("//PRINTDATA/ROW");

          for (int j = 0; j < printRoot.size(); j++)
          {

              Element ele = (Element) printRoot.get(j);
              if (ele.getNodeType() != Node.ELEMENT_NODE)
              {
                  continue;
              }
              String field= ele.attributeValue("fieldname");
              if (field!=null&&field.equalsIgnoreCase(filename))
              {
                  ele.setText(result);
                  break;
              }
          }
      }
      catch (DocumentException ex)
      {
          ex.printStackTrace();
      }
      finally
      {
          if (in != null)
              in.close();
          if(isr!=null)
          isr.close();
      }
          String xmlString = doc.asXML();
          if (xmlString.indexOf("UTF-8") > 0)
          {
              //xmlString = xmlString.replaceAll("UTF-8", "GB2312"); //xml字符集转化
          }
          byte[] b = xmlString.getBytes("UTF-8");
          try
          {
              String s =
                  "update pub_blob set MULTIMEDIA=EMPTY_BLOB() WHERE WS_TEMPLATE_ID ='"+templateid+"' and SJBH='" +
                  sjbh + "' and YWLX='" + ywlx + "'";
              DBUtil.exec(conn, s);
              String sql = "select MULTIMEDIA from pub_blob WHERE WS_TEMPLATE_ID ='"+templateid+"' and SJBH='" +
                  sjbh + "' and YWLX='" + ywlx +
                  "' order by tbsj desc for update ";
              blobUtil.updateBlob(conn, sql, b);
              //conn.commit();
          }
          catch (Exception e)
          {
              e.printStackTrace();
          }
          finally
          {
          }
      return null;
  }

  /**
   * 处理编辑域,填充至文书模板
   * @param conn
   * @param fjbh
   * @param result
   * @param approverole
   * @param approvelevel
   * @throws java.lang.Exception
   */
  public ActionForward getEditXML(ActionMapping mapping, ActionForm form,
                                  HttpServletRequest request,
                                  HttpServletResponse response)
      throws Exception
  {
      String templateid = request.getParameter("templateid"); //模板id
      String sjbh = request.getParameter("sjbh"); //事件编号
      String ywlx = request.getParameter("ywlx"); //业务类型


      SAXReader readerEdit = new SAXReader();

      InputStream inEdit = null;// request.getInputStream();
      Document docEdit = null;//readerEdit.read(inEdit);



      BlobUtil blobUtil = new BlobUtil();
      String pub_blob_sql = "select MULTIMEDIA from PUB_BLOB where  WS_TEMPLATE_ID ='"+templateid+"' and SJBH='" +sjbh + "' and YWLX='" + ywlx + "' order by tbsj desc";
      byte[] xmlBlob = blobUtil.get("blob", pub_blob_sql); //xml blob
      Document doc = null;
      SAXReader reader = new SAXReader();
      ByteArrayInputStream in = null;
      List printRootEdit = null;
      InputStreamReader   isr   =  null;
      try
      {

          inEdit = request.getInputStream();
          docEdit = readerEdit.read(inEdit);
          printRootEdit = docEdit.selectNodes("//EDITS/ROW");

          in = new ByteArrayInputStream(xmlBlob);
          isr   =   new   InputStreamReader(in,"UTF-8");
          doc = reader.read(isr);
          List printRoot = doc.selectNodes("//PRINTDATA/ROW");
          for (int i = 0; i < printRootEdit.size(); i++)
          {
              Element eleEdit = (Element) printRootEdit.get(i);
              if (eleEdit.getNodeType() != Node.ELEMENT_NODE)
              {
                  continue;
              }
              String EdittagName = eleEdit.attributeValue("tagname");

              for (int j = 0; j < printRoot.size(); j++)
              {

                  Element ele = (Element) printRoot.get(j);
                  if (ele.getNodeType() != Node.ELEMENT_NODE)
                  {
                      continue;
                  }
                  String fieldname = ele.attributeValue("fieldname");
                  if(EdittagName.equalsIgnoreCase(fieldname)){
                      ele.setText(eleEdit.getText());
                      break;
                  }
              }
          }
      }
      catch (DocumentException ex)
      {
          ex.printStackTrace();
      }
      finally
      {
          if (in != null)
              in.close();
          if(inEdit !=null)
              inEdit.close();
          if(isr!=null)
          isr.close();
      }
      if(printRootEdit!=null&&printRootEdit.size()>0){
          String xmlString = doc.asXML();
          if (xmlString.indexOf("UTF-8") > 0)
          {
              //xmlString = xmlString.replaceAll("UTF-8", "GB2312"); //xml字符集转化
          }
          byte[] b = xmlString.getBytes("UTF-8");
          Connection conn = null;
          try
          {
              conn = DBUtil.getConnection();
              conn.setAutoCommit(false);
              String s =
                  "update pub_blob set MULTIMEDIA=EMPTY_BLOB() WHERE WS_TEMPLATE_ID ='"+templateid+"' and SJBH='" +
                  sjbh + "' and YWLX='" + ywlx + "'";
              DBUtil.exec(conn, s);
              String sql = "select MULTIMEDIA from pub_blob WHERE WS_TEMPLATE_ID ='"+templateid+"' and SJBH='" +
                  sjbh + "' and YWLX='" + ywlx +
                  "' order by tbsj desc for update ";
              blobUtil.updateBlob(conn, sql, b);
              /**customed code  begin
               *  added by andy 20080522
               *  base on templateid,customed self code
               */
              switch(Integer.parseInt(templateid))
              {
                 default:
                    break;
              }
              /**   end */
              conn.commit();
          }
          catch (Exception e)
          {
              conn.rollback();
              e.printStackTrace();
          }
          finally
          {
              if (conn != null)
                  conn.close();
              conn = null;
          }
      }
      return null;
  }
  /**
   * 复制一个文书的印章到另外一个文书中
   * @param conn
   * @param oldAzt
   * @param newAzt
   * @throws java.lang.Exception
   */
  public void copyAzt(Connection conn,HashMap oldAzt,HashMap newAzt) throws Exception{

      String ows_template_id = (String)oldAzt.get("WS_TEMPLATE_ID");
        if(ows_template_id==null)
            throw new Exception("原文书模板编号为空");
      String osjbh = (String)oldAzt.get("SJBH");
      if(osjbh==null)
          throw new Exception("原事件编号为空");
      String oywlx = (String)oldAzt.get("YWLX");
      if(oywlx==null)
          throw new Exception("原业务类型为空");
      String orole = (String)oldAzt.get("APPROVEROLE");
      if(orole==null)
          throw new Exception("原审批角色为空");
      String olevel = (String)oldAzt.get("APPROVELEVEL");
      if(olevel==null)
          throw new Exception("原审批级别为空");
      String oyzlx = (String)oldAzt.get("YZLX");
      if(oyzlx==null)
          throw new Exception("原印章类型为空");

      BlobUtil blobUtil = new BlobUtil();
      String ws_electron_print_sql =
          "select MULTIMEDIA from WS_ELECTRON_PRINT where WS_TEMPLATE_ID='" +
          ows_template_id + "' and sjbh = '" + osjbh + "' and ywlx = '" +
          oywlx + "' and APPROVEROLE='" + orole + "' and APPROVELEVEL ='" +
          olevel + "' and YZLX='" + oyzlx + "'";
      byte[] xmlBlob = blobUtil.get("blob",ws_electron_print_sql);//xml blob

      Ws_Electron_PrintVO vo = new Ws_Electron_PrintVO();
      if((String)newAzt.get("WS_TEMPLATE_ID")==null)
      {
          throw new Exception("新文书模板编号为空");
      }else
      {
          vo.setWs_template_id((String)newAzt.get("WS_TEMPLATE_ID"));
      }
      if((String)newAzt.get("SJBH")==null)
      {
          throw new Exception("新事件编号为空");
      }else
      {
          vo.setSjbh((String)newAzt.get("SJBH"));
      }
      if((String)newAzt.get("YWLX")==null)
      {
          throw new Exception("新业务类型为空");
      }else
      {
          vo.setYwlx((String)newAzt.get("YWLX"));
      }
      if((String)newAzt.get("APPROVEROLE")==null)
      {
          throw new Exception("新审批角色为空");
      }else
      {
          vo.setApproverole((String)newAzt.get("APPROVEROLE"));
      }
      if((String)newAzt.get("APPROVELEVEL")==null)
      {
          throw new Exception("新审批级别为空");
      }else
      {
          vo.setApprovelevel((String)newAzt.get("APPROVELEVEL"));
      }
      if((String)newAzt.get("YZLX")==null)
      {
          throw new Exception("新印章类型为空");
      }else
      {
          vo.setYzlx((String)newAzt.get("YZLX"));
      }
      vo.setMultimedia(xmlBlob);
      String[][] xh = DBUtil.query(conn, "select AP_TASK_S.Nextval from dual");
      vo.setXh(xh[0][0]);
      Ws_Electron_PrintAction action = new Ws_Electron_PrintAction();
      action.Insert(conn,vo);


  }

//---------------------------------add end -------------------------------------

  /**
   * 复制一个文书的印章到另外一个文书中
   * @param conn
   * @param oldAzt
   * @param newAzt
   * @throws java.lang.Exception
   */
  public void copyOrgPrintAzt(Connection conn,String zzjg,String yzlb,HashMap newAzt) throws Exception{
      if(Pub.empty(zzjg))
      {
          throw new Exception("组织结构编号为空");
      }
      if(Pub.empty(yzlb))
      {
          throw new Exception("印章类别为空");
      }

      BlobUtil blobUtil = new BlobUtil();
      String ws_electron_print_sql =
          "select PRINT from org_print where JG='" +zzjg + "' and PRINTKIND = '" + yzlb + "' ";
      byte[] xmlBlob = blobUtil.get("blob", ws_electron_print_sql); //xml blob
      SAXReader reader = new SAXReader();
      ByteArrayInputStream in = new ByteArrayInputStream(xmlBlob);
      Document doc = reader.read(in);
      if (in != null)
          in.close();
      String xmlString = doc.asXML();
      if (xmlString.indexOf("UTF-8") > 0)
      {
          //xmlString = xmlString.replaceAll("UTF-8", "GB2312"); //xml字符集转化
      }
      byte[] b = xmlString.getBytes("UTF-8");

      Ws_Electron_PrintVO vo = new Ws_Electron_PrintVO();
      if((String)newAzt.get("WS_TEMPLATE_ID")==null)
      {
          throw new Exception("新文书模板编号为空");
      }else
      {
          vo.setWs_template_id((String)newAzt.get("WS_TEMPLATE_ID"));
      }
      if((String)newAzt.get("SJBH")==null)
      {
          throw new Exception("新事件编号为空");
      }else
      {
          vo.setSjbh((String)newAzt.get("SJBH"));
      }
      if((String)newAzt.get("YWLX")==null)
      {
          throw new Exception("新业务类型为空");
      }else
      {
          vo.setYwlx((String)newAzt.get("YWLX"));
      }
      if((String)newAzt.get("APPROVEROLE")==null)
      {
          throw new Exception("新审批角色为空");
      }else
      {
          vo.setApproverole((String)newAzt.get("APPROVEROLE"));
      }
      if((String)newAzt.get("APPROVELEVEL")==null)
      {
          throw new Exception("新审批级别为空");
      }else
      {
          vo.setApprovelevel((String)newAzt.get("APPROVELEVEL"));
      }
      if((String)newAzt.get("YZLX")==null)
      {
          throw new Exception("新印章类型为空");
      }else
      {
          vo.setYzlx((String)newAzt.get("YZLX"));
      }
      vo.setMultimedia(b);
      String[][] xh = DBUtil.query(conn, "select AP_TASK_S.Nextval from dual");
      vo.setXh(xh[0][0]);
      Ws_Electron_PrintAction action = new Ws_Electron_PrintAction();
      action.Insert(conn,vo);


  }
  /**
   * 维护盖章表
   * @param conn
   * @param ws_template_id
   * @param sjbh
   * @param ywlx
   * @param level
   * @param role
   * @param aztXML
   * @throws java.lang.Exception
   */

  public void insertAztPrint(Connection conn,String ws_template_id,String sjbh,String ywlx,String level,String role,String aztXML,String yzlx)
        throws Exception
    {
      if(aztXML.indexOf("UTF-8")>0){
         //aztXML =  aztXML.replaceAll("UTF-8","GB2312");//xml字符集转化
      }
      Ws_Electron_PrintVO vo = new Ws_Electron_PrintVO();
      String[][] xh = DBUtil.query(conn,"select AP_TASK_S.Nextval from dual");
      vo.setXh(xh[0][0]);
      vo.setWs_template_id(ws_template_id);
      vo.setSjbh(sjbh);
      vo.setYwlx(ywlx);
      if(level!=null)
      vo.setApprovelevel(level);
      if(role!=null)
      vo.setApproverole(role);
      byte[] b = aztXML.getBytes("UTF-8");
      vo.setMultimedia(b);
      vo.setYzlx(yzlx);
      Ws_Electron_PrintAction action = new Ws_Electron_PrintAction();
      action.Insert(conn,vo);

  }
  public String printWSJson(HttpServletRequest request, String json,User user)
	        throws Exception

	        
   {

	      Connection conn = null;
	      BaseVO bv = new BaseVO();
	      JSONArray list = bv.doInitJson(json);
	      JSONObject jo = (JSONObject)list.get(0);
	      String sjbh = (String)jo.get("SJBH");
	      String ywlx = (String)jo.get("YWLX");
	      String ws_templateid = (String)jo.get("WSTEMPLATEID");
	      
	      // 重启
	      String isReStart =  request.getParameter("isReStart");
	      try
	      {
	    	  if(ws_templateid==null||ws_templateid.length()<=0)
	    		  return "1";
	    	  conn = DBUtil.getConnection();
	    	  conn.setAutoCommit(false);
	    //	  BlobUtil blobUtil = new BlobUtil();
	    	  if("isReStart".equals(isReStart)) {
	    		  String approverole = "".equals(jo.getString("YJRYJ")) ? "" : "deef5ed2-7d52-410c-a4b3-d8c17b4c70a3";
	    		  String insertSql = "insert into AP_PROCESS_WS(SPWSID,WSWH_FLAG,WS_TEMPLATE_ID,DOMAIN_TYPE," 
	  					+ "DOMAIN_VALUE,CODEPAGE,FIELDNAME,APPROVEROLE,APPROVELEVEL,CANEDIT,DOMAIN_STYLE,SJBH,YWLX,LRSJ,LRRID,LRRXM)"
	    				+ " values('"+new RandomGUID().toString()+"','"+PubWS.FIELDEXTEN_MIND+"','381','','"
	  					+ jo.getString("YJRYJ")+"','','YJRYJ','" + approverole + "','','1','','"
	            		+ sjbh+"','"+ywlx+"',SYSDATE,'"+user.getAccount()+"','"+user.getName()+"') ";
	              DBUtil.execSql(conn, insertSql);
	    	  } else {
				  String pub_blob_sql = "select fieldname,SPWSID,lrsj,domain_value,lrrid,lrrxm,WSWH_FLAG from AP_PROCESS_WS where sjbh= '"+sjbh+"' and ywlx='" +ywlx+"' and WS_TEMPLATE_ID = '"+ws_templateid+"' ";
				  QuerySet qs = DBUtil.executeQuery(pub_blob_sql, null, conn);
				  
		          for (int i = 0; i < qs.getRowCount(); i++)
		          {
		            String fieldname = qs.getString(i+1, 1);
		            String spwsid = qs.getString(i+1, 2);
		            String lrsj = qs.getString(i+1, 3);
		       //     String domain_value = qs.getString(i+1, 4);
		            String data_lrrid = qs.getString(i+1, 5);
		            String data_lrrxm = qs.getString(i+1, 6);
		            String wswh_flag = qs.getString(i+1, 7);
		            
					List<Object> arr = jo.names();
					Collection<Object> arra = jo.values();// 获取值

					for (Object name : arr) {
						String key = name.toString().toUpperCase();
			            
			            if(fieldname.equalsIgnoreCase(key))
			            {
			            	System.out.println("fieldname:"+fieldname+"|key:"+key+"|wswh_flag:"+wswh_flag);
			            	// 录入时间和录入人默认是当前登录人
			            	String lrrid = user.getAccount();
			            	String lrrxm = user.getName();
				            String swDomainValue = "";
			            	// xhb add start
				            // 当业务类型是200502表示为收文，提交文书时，必须保持时间为空（时间为空表示审批域）的数据的domain_value为空。
				            String sql = "";
				            if("200502".equals(ywlx)) {
				            	swDomainValue = lrsj == null ? "" : jo.getString(name.toString());
				            	sql = "update AP_PROCESS_WS set domain_value ='"+swDomainValue+"',lrrid='"+lrrid+"',lrrxm='"+lrrxm+"' where spwsid = '"+spwsid+"'";
				            } else if("000002".equals(ywlx) || "000003".equals(ywlx)) {// 当业务类型是工作联系单时
				            	// issp为1时，表示工作联系单的主要内容与要求要修改
				            	
				            	String wsid = "000002".equals(ywlx) ? "383" : "384";
				            	
				            	swDomainValue = jo.getString(name.toString());
				            	lrrid = data_lrrid;
				            	lrrxm = data_lrrxm;
				            	if ("3".equals(wswh_flag)) {
						            sql = "update AP_PROCESS_WS set domain_value ='"+swDomainValue+"',lrrid='"+lrrid+"',lrrxm='"+lrrxm+"' where spwsid = '"+spwsid+"'";
								} else if ("5".equals(wswh_flag) && lrsj== null) {
									sql = "insert into AP_PROCESS_WS(SPWSID,WSWH_FLAG,WS_TEMPLATE_ID,DOMAIN_TYPE,DOMAIN_VALUE,CODEPAGE,FIELDNAME,APPROVEROLE,APPROVELEVEL,CANEDIT,DOMAIN_STYLE,SJBH,YWLX,LRSJ,LRRID,LRRXM)"+
				                		  	" values('"+new RandomGUID().toString()+"','"+FIELDEXTEN_MIND+"','"+wsid+"','','"+swDomainValue+"','','"+fieldname+"','','','1','','"+sjbh+"','"+ywlx+"',SYSDATE,'"+user.getAccount()+"','"+user.getName()+"') ";
								} else if ("12".equals(wswh_flag) && lrsj == null) {
									if(!"".equals(swDomainValue)) {
										sql = "insert into AP_PROCESS_WS(SPWSID,WSWH_FLAG,WS_TEMPLATE_ID,DOMAIN_TYPE,DOMAIN_VALUE,CODEPAGE,FIELDNAME,APPROVEROLE,APPROVELEVEL,CANEDIT,DOMAIN_STYLE,SJBH,YWLX,LRSJ,LRRID,LRRXM)"+
					                		  	" values('"+new RandomGUID().toString()+"','"+FIELDEXTEN_12+"','"+wsid+"','','"+swDomainValue+"','','"+fieldname+"','"+user.getAccount()+"','','1','','"+sjbh+"','"+ywlx+"',SYSDATE,'"+user.getAccount()+"','"+user.getName()+"') ";
									} else {
										sql = "update AP_PROCESS_WS set WS_TEMPLATE_ID='"+wsid+"' where WS_TEMPLATE_ID='"+wsid+"' and rownum=1";
									}
								} else {
									sql = "update AP_PROCESS_WS set WS_TEMPLATE_ID='"+wsid+"' where WS_TEMPLATE_ID='"+wsid+"' and rownum=1";
								}
				            } else {
				            	swDomainValue = jo.getString(name.toString());
				            	sql = "update AP_PROCESS_WS set domain_value ='"+swDomainValue+"',lrrid='"+lrrid+"',lrrxm='"+lrrxm+"' where spwsid = '"+spwsid+"'";
				            }
			            	// xhb add end
				            sql = sql.replace("'null',", "null,");
			            	DBUtil.exec(conn, sql);
			                //ele.setText();
			                break;
			            }
				    }

		          }
	    	  }

		      conn.commit();
	    }
	    catch (DocumentException ex)
	    {
	    	conn.rollback();
	        ex.printStackTrace();
	        return "0";
	    }finally{

	        if (conn != null)
	        	conn.close();
	    }
	      return "1";

	  }  
	@RequestMapping(params = "saveWSJson")
	@ResponseBody
	protected requestJson saveWSJson(HttpServletRequest request,requestJson json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
//		String isReStart = request.getParameter("isReStart");
		try {
			String domResult = printWSJson(request, json.getMsg(),user);
			j.setMsg(domResult);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return j;
	}  
  
  public void updateWsXML(Connection conn,String fjbh ,String nr,String fieldname)
        throws Exception

    {
        if(fjbh==null||"".equals(fjbh)){
            throw new Exception("fjbh为空，不能处理文书！");
    }
      BlobUtil blobUtil = new BlobUtil();
      String pub_blob_sql = "select MULTIMEDIA from PUB_BLOB where fjbh='"+fjbh+"'  order by tbsj desc";
      byte[] xmlBlob = blobUtil.get("blob", pub_blob_sql); //xml blob
      Document doc = null;
      SAXReader reader = new SAXReader();
      ByteArrayInputStream in = null;
      InputStreamReader isr  = null;
      try
      {
          in = new ByteArrayInputStream(xmlBlob);
          isr   =   new   InputStreamReader(in,"UTF-8");
          doc = reader.read(isr);
          List printRoot = doc.selectNodes("//PRINTDATA/ROW");
          for (int i = 0; i < printRoot.size(); i++)
          {
            Element ele = (Element) printRoot.get(i);
            if (ele.getNodeType() != Node.ELEMENT_NODE)
            {
                continue;
            }
            String fieldname_ = ele.attributeValue("fieldname");
            if(fieldname_.equalsIgnoreCase(fieldname)){
                ele.setText(nr);
                break;
           }
        }
    }
    catch (DocumentException ex)
    {
        ex.printStackTrace();
    }finally{
        if(in!=null)
            in.close();
        if (isr != null)
            isr.close();

    }
      String xmlString =doc.asXML();
      byte [] b =xmlString.getBytes("UTF-8");
      String s = "update pub_blob set MULTIMEDIA=EMPTY_BLOB() WHERE fjbh='"+fjbh+"'";
      DBUtil.exec(conn,s);
      String sql = "select MULTIMEDIA from pub_blob WHERE fjbh='"+fjbh+"'  for update ";
      blobUtil.updateBlob(conn,sql,b);
  }

  /* add by songxb@2007-12-26 重载updateWsXML函数
   * 参数 Hashtable hm 可以批量一次更新多个字段
   */
  public static void updateWsXML(Connection conn,String fjbh ,HashMap hm)
        throws Exception

    {
        if(fjbh==null||"".equals(fjbh)){
            throw new Exception("fjbh为空，不能处理文书！");
        }
      BlobUtil blobUtil = new BlobUtil();
      String pub_blob_sql = "select MULTIMEDIA from PUB_BLOB where fjbh='"+fjbh+"'  order by tbsj desc";
      byte[] xmlBlob = blobUtil.get("blob", pub_blob_sql); //xml blob
      Document doc = null;
      SAXReader reader = new SAXReader();
      ByteArrayInputStream in = null;
      InputStreamReader isr  = null;
      try
      {
          in = new ByteArrayInputStream(xmlBlob);
          isr   =   new   InputStreamReader(in,"UTF-8");
          doc = reader.read(isr);
          List printRoot = doc.selectNodes("//PRINTDATA/ROW");
          for (int i = 0; i < printRoot.size(); i++)
          {
            Element ele = (Element) printRoot.get(i);
            if (ele.getNodeType() != Node.ELEMENT_NODE)
            {
                continue;
            }
            String fieldname_ = ele.attributeValue("fieldname");
            if(hm.containsKey(fieldname_.toLowerCase())){
                ele.setText((String)hm.get(fieldname_.toLowerCase()));
            }
        }
    }
    catch (DocumentException ex)
    {
        ex.printStackTrace();
    }finally{
        if(in!=null)
            in.close();
        if (isr != null)
            isr.close();

    }
      String xmlString =doc.asXML();
      byte [] b =xmlString.getBytes("UTF-8");
      String s = "update pub_blob set MULTIMEDIA=EMPTY_BLOB() WHERE fjbh='"+fjbh+"'";
      DBUtil.exec(conn,s);
      String sql = "select MULTIMEDIA from pub_blob WHERE fjbh='"+fjbh+"'  for update ";
      blobUtil.updateBlob(conn,sql,b);
  }

  /**
   * 自定义处理审批文书 填充单元格
   * @param fjbh
   * @param result
   * @param sheet_id
   * @param line_id
   * @param column_id
   * @param fieldname
   * @param domain
   * @throws java.lang.Exception
   */
  public void getSpYjXML(Connection conn,String fjbh,String result,String sheet_id,String line_id,String column_id,String fieldname,String domain)
        throws Exception
    {
        if(fjbh==null||"".equals(fjbh)){
            throw new Exception("fjbh为空，不能处理审批文书！");
        }
      BlobUtil blobUtil = new BlobUtil();
      String pub_blob_sql = "select MULTIMEDIA from PUB_BLOB where fjbh='"+fjbh+"' order by tbsj desc";
      byte[] xmlBlob = blobUtil.get("blob",pub_blob_sql);//xml blob
      SAXReader reader = new SAXReader();
      ByteArrayInputStream in = new ByteArrayInputStream(xmlBlob);
      InputStreamReader   isr   =   new   InputStreamReader(in,"UTF-8");
      Document doc = reader.read(isr);
      if(isr!=null)
          isr.close();
      List printRoot = doc.selectNodes("//PRINTDATA");
      Element ele = (Element) printRoot.get(0);
      Element recordItem = ele.addElement("ROW");
      recordItem.addAttribute("sheet_id",sheet_id);
      recordItem.addAttribute("line_id",String.valueOf(Integer.parseInt(line_id)));
      recordItem.addAttribute("column_id",String.valueOf(Integer.parseInt(column_id)));
      recordItem.addAttribute("fieldname",fieldname);
      recordItem.addAttribute("domain",domain);
      recordItem.addText(result);

      String xmlString =doc.asXML();
      if(xmlString.indexOf("UTF-8")>0){
         //xmlString =  xmlString.replaceAll("UTF-8","GB2312");//xml字符集转化
      }
      byte [] b =xmlString.getBytes("UTF-8");
      String s = "update pub_blob set MULTIMEDIA=EMPTY_BLOB() WHERE fjbh='"+fjbh+"'";
      DBUtil.exec(conn,s);
      String sql = "select MULTIMEDIA from pub_blob WHERE fjbh='"+fjbh+"' for update ";
      blobUtil.updateBlob(conn,sql,b);
  }


  /**
   * 从变量表中得到变量的真实值
   * @param request
   * @param key
   * @return
   * @throws java.lang.Exception
   */
  private String getValue(HttpServletRequest request, String key,String templateid,String sjbh,String ywlx) throws
      Exception {
    QuerySet qs = DBUtil.executeQuery(getVariable(templateid,key), null);
    if (qs.getRowCount() != 0) {
      if (qs.getString(1, "VARIABLE_FROM_KIND").equalsIgnoreCase("1"))

      { //固定值
        key = qs.getString(1, "VARIABLE_FROM_VALUE");
      }
      if (qs.getString(1, "VARIABLE_FROM_KIND").equalsIgnoreCase("2"))

      { //session
        key = (String) request.getSession().getAttribute(qs.getString(1,"VARIABLE_FROM_VALUE"));
      }
      if (qs.getString(1, "VARIABLE_FROM_KIND").equalsIgnoreCase("3")) { //request

        key = request.getParameter(qs.getString(1, "VARIABLE_FROM_VALUE"));


      }
//      if (qs.getString(1, "VARIABLE_FROM_KIND").equalsIgnoreCase("4")) { //事件方式
//
//        QuerySet sj = DBUtil.executeQuery("select * from sjb where sjbh='"+sjbh+"' and ywlx = '"+ywlx+"'",null);
//        if(sj.getRowCount()>0){
//            key = sj.getString(1,"ywzj");
//        }
//
//      }

    }
    return key;
  }
  /**
   * 文书生成情况查询是否已打印过此文书
   * @param sjbh
   * @param templateid
   * @return
   * @throws java.lang.Exception
   */
  private boolean queryCreateDetail(String sjbh,String templateid,Connection conn) throws Exception {
    QuerySet qs = null;
    //xukx 增加作废标识的过滤
    String sql = "select * from PUB_BLOB a where a.SJBH='" +sjbh + "' and a.WS_TEMPLATE_ID = '" + templateid + "' and a.ZFBS = '0' ";
    try {
      qs = DBUtil.executeQuery(sql, null,conn);
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new Exception ("");
    }
    if(qs.getRowCount()>0){
        return true;
    }else{
        return false;
    }

  }

  /**
   * 文书生成情况查询是否已打印过此文书
   * @param sjbh
   * @param templateid
   * @return
   * @throws java.lang.Exception
   */
  private boolean queryCreateDetail(String sjbh,String ywlx,String templateid,Connection conn) throws Exception {
    QuerySet qs = null;
    //xukx 增加作废标识的过滤
    String sql = "select * from PUB_BLOB a where a.SJBH='" +sjbh + "' and a.YWLX='" +ywlx + "' and a.WS_TEMPLATE_ID = '" + templateid + "' and a.ZFBS = '0' ";

    try {
      qs = DBUtil.executeQuery(sql, null,conn);
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new Exception ("");
    }
    if(qs.getRowCount()>0){
        return true;
    }else{
        return false;
    }

  }
  
  private String getValue_sql(HttpServletRequest request, String sql,
                             String[] value, String SEPARATOR,String templateid,String sjbh,String ywlx) throws Exception {
     return getValue_sql(request,sql,value,SEPARATOR,templateid,sjbh,ywlx,java.sql.Types.VARCHAR);
  }
  /**
   * 从变量表中得到变量的真实值通过查询已知的sql语句
   * @param request
   * @param sql
   * @param value
   * @return
   * @throws java.lang.Exception
   */
  private String getValue_sql(HttpServletRequest request, String sql,
                             String[] value, String SEPARATOR,String templateid,String sjbh,String ywlx,int colType) throws Exception {
    QuerySet qs = null;
    Connection conn = null;
    try
    {
    	conn = DBUtil.getConnection();
    }catch(Exception e)
    {
    	throw e;
    }
    String va = "";
    String variable_sql = "";
    //String[] type = null;

    this.mutilrow = false;
    this.mutqs = null;

    if (SEPARATOR == null) {
      SEPARATOR = "";
    }
    if (value != null) {
      //type = new String[value.length];
      for (int i = 0; i < value.length; i++) {
        variable_sql = "select * from WS_TEMPLATE_VARIABLE where VARIABLE_NAME='" + value[i] + "' and WS_TEMPLATE_ID = '"+templateid+"'";
        qs = DBUtil.executeQuery(variable_sql, null,conn);
        if (qs.getRowCount() != 0) {
          if (qs.getString(1, "VARIABLE_FROM_KIND").equalsIgnoreCase(VARIABLE_FROM_STATIC)) { //固定值
            value[i] = qs.getString(1, "VARIABLE_FROM_VALUE");
            //type[i] = qs.getString(1, "VARIABLE_TYPE");
          }
          if (qs.getString(1, "VARIABLE_FROM_KIND").equalsIgnoreCase(VARIABLE_FROM_SESSION)) { //session
            value[i] = (String) request.getSession().getAttribute(qs.getString(1, "VARIABLE_FROM_VALUE"));
            //type[i] = qs.getString(1, "VARIABLE_TYPE");
            if(value[i] == null)
            {
                value[i]="";
            }
          }
          if (qs.getString(1, "VARIABLE_FROM_KIND").equalsIgnoreCase(VARIABLE_FROM_REQUEST)) { //requestUtil
            if( request.getParameter(qs.getString(1,"VARIABLE_FROM_VALUE"))==null)
            {
                value[i] ="";
            }else
            {
                value[i] = request.getParameter(qs.getString(1,"VARIABLE_FROM_VALUE"));
            }
            //type[i] = qs.getString(1, "VARIABLE_TYPE");
          }
          if (qs.getString(1, "VARIABLE_FROM_KIND").equalsIgnoreCase(VARIABLE_FROM_SJBH)) { //事件方式
            value[i] = sjbh;
          }
        }
      }
      Object paras[] = new Object[value.length];
      for (int i = 0; i < paras.length; i++) {
          paras[i] = value[i];
      }

      qs = DBUtil.executeQuery(sql, paras,conn);

      if (qs.getColumnCount() > 1) {
        this.mutilrow = true;
        this.mutqs = qs;
      }

      if (qs.getRowCount() != 0)
      {
          int columnType = qs.getType(1);
          if(columnType == java.sql.Types.BLOB&&colType!=java.sql.Types.BLOB){
              colType = java.sql.Types.CLOB;
          }
          if (colType == java.sql.Types.VARCHAR)
          {
              va = "";
              if (qs.getRowCount() == 1)
              {
                  va += qs.getString(1, 1);
              }
              else
              {
                  for (int i = 0; i < qs.getRowCount(); i++)
                  {
                      String temp = qs.getString(i + 1, 1);
                      if(qs.getString(i + 1, 1) ==null)
                          temp = "";
                      if (SEPARATOR.equals("") || SEPARATOR == null ||
                          SEPARATOR.equals(" "))
                      {
                          va += temp + " ";
                      }
                      else
                      {
                          if (i == qs.getRowCount() - 1)
                          {
                              va += temp + " ";
                          }
                          else
                          {
                              va += temp + SEPARATOR;
                          }
                      }
                  }
              }
          }
          else if (colType == java.sql.Types.BLOB)
          {
              Blob dbBlob;
              dbBlob = (Blob)qs.getObject(1,1);
              if (dbBlob != null)
              {
                  int length = (int) dbBlob.length();
                  byte[] buffer = dbBlob.getBytes(1, length);
                  va = Pub.toBase64(buffer);
              }
          }
          else if (colType == java.sql.Types.CLOB)
          {
               //dbBlob;
               Blob dbBlob;
               dbBlob= (Blob)qs.getObject(1,1);
              if (dbBlob != null)
              {
                  int length = (int) dbBlob.length();
                  byte[] buffer = dbBlob.getBytes(1, length);
                  va = new String(buffer,"GBK");
              }
          }
      }

  }
    else {

      qs = DBUtil.executeQuery(sql, null,conn);

      if (qs.getColumnCount() > 1) {
        this.mutilrow = true;
        this.mutqs = qs;
      }

      if (qs.getRowCount() != 0)
      {
          int columnType = qs.getType(1);
          if(columnType == java.sql.Types.BLOB&&colType!=java.sql.Types.BLOB){
              colType = java.sql.Types.CLOB;
          }
          if (colType == java.sql.Types.VARCHAR)
          {

              va = "";
              if (qs.getRowCount() == 1)
              {
                  va += qs.getString(1, 1);
              }
              else
              {
                  for (int i = 0; i < qs.getRowCount(); i++)
                  {
                      String temp = qs.getString(i + 1, 1);
                      if(qs.getString(i + 1, 1) ==null)
                          temp = "";

                      if (SEPARATOR.equals("") || SEPARATOR == null ||
                          SEPARATOR.equals(" "))
                      {
                          va += temp + " ";
                      }
                      else
                      {
                          if (i == qs.getRowCount() - 1)
                          {
                              va += temp + " ";
                          }
                          else
                          {
                              va += temp + SEPARATOR;
                          }
                      }
                  }
              }
          }
          else if (colType == java.sql.Types.BLOB)
          {
              Blob dbBlob;
              dbBlob = (Blob)qs.getObject(1,1);
              if (dbBlob != null)
              {
                  int length = (int) dbBlob.length();
                  byte[] buffer = dbBlob.getBytes(1, length);
                  va = Pub.toBase64(buffer);
              }
          }
          else if (colType == java.sql.Types.CLOB)
          {
              Blob dbBlob;
              dbBlob = (Blob)qs.getObject(1,1);
              if (dbBlob != null)
              {
                  int length = (int) dbBlob.length();
                  byte[] buffer = dbBlob.getBytes(1, length);
                  va = new String(buffer,"GBK");
              }
          }
      }
  }
    try{
    	conn.close();
    }catch(Exception e)
    {
    	throw e;
    }
    
    return va;
  }
  
	private String getOldValue_sql(HttpServletRequest request, String sql,
			String[] value,int colType) throws Exception {
		QuerySet qs = null;
		Connection conn = null;
		try {
			conn = DBUtil.getConnection();
		} catch (Exception e) {
			throw e;
		}
		String va = "";
		String variable_sql = "";
		// String[] type = null;

		this.mutilrow = false;
		this.mutqs = null;
			qs = DBUtil.executeQuery(sql, null, conn);

			if (qs.getColumnCount() > 1) {
				this.mutilrow = true;
				this.mutqs = qs;
			}

			if (qs.getRowCount() != 0) {
				int columnType = qs.getType(1);
				if (columnType == java.sql.Types.BLOB
						&& colType != java.sql.Types.BLOB) {
					colType = java.sql.Types.CLOB;
				}
				if (colType == java.sql.Types.VARCHAR) {

					va = "";
					if (qs.getRowCount() == 1) {
						va += qs.getString(1, 1);
					} else {
						for (int i = 0; i < qs.getRowCount(); i++) {
							String temp = qs.getString(i + 1, 1);
							if (qs.getString(i + 1, 1) == null)
								temp = "";


						}
					}
				} else if (colType == java.sql.Types.BLOB) {
					Blob dbBlob;
					dbBlob = (Blob) qs.getObject(1, 1);
					if (dbBlob != null) {
						int length = (int) dbBlob.length();
						byte[] buffer = dbBlob.getBytes(1, length);
						va = Pub.toBase64(buffer);
					}
				} else if (colType == java.sql.Types.CLOB) {
					Blob dbBlob;
					dbBlob = (Blob) qs.getObject(1, 1);
					if (dbBlob != null) {
						int length = (int) dbBlob.length();
						byte[] buffer = dbBlob.getBytes(1, length);
						va = new String(buffer, "GBK");
					}
				}
			}

		try {
			conn.close();
		} catch (Exception e) {
			throw e;
		}

		return va;
	}

  /**
   * 字典翻译
   * @param codepage
   * @param domain
   * @param SEPARATOR
   * @return
   * @throws java.lang.Exception
   */

  private String getCodeName(String codepage, String domain, String SEPARATOR) throws
      Exception {
    String tem = "";
    if (SEPARATOR == null) {
      SEPARATOR = "";
    }
    if (SEPARATOR.equals("")) {
      if (domain != null) {
        domain = domain.trim();
      }
      return getDicValue(domain,codepage);//Dics.getTextByDicNameCode(codepage, domain);
    }
    else {
      String[] temp = domain.split(SEPARATOR);
      for (int i = 0; i < temp.length; i++) {
        if (temp[i] != null) {
          temp[i] = temp[i].trim();
        }
        String ting = getDicValue(domain,codepage);//Dics.getTextByDicNameCode(codepage, temp[i]);
        if (i == temp.length - 1) {
          tem += ting;
        }
        else {
          tem += ting + SEPARATOR;
        }
      }
      domain = tem ;
    }
    return domain;
  }
  private String getDicValue(String code,String dic){
      if("muserid".equalsIgnoreCase(dic)){
          return Pub.getMUserNameById(code);
      }else if("userid".equalsIgnoreCase(dic)){
         return Pub.getUserNameByLoginId(code);
      }else
      if("deptid".equalsIgnoreCase(dic)){
          return Pub.getDeptNameByID(code);

      }else{
          return Dics.getTextByDicNameCode(dic,code);
      }
     // return code;


  }
  /**
   * 获得给号类对应的部门
   * @param bmjb 部门级别
   * @param ORGID 部门编号
   * @return 对应的部门编号
 * @throws Exception 
   */
  private String getBmbh(String bmjb, String ORGID, String templateid, String ywlx, Connection conn, User user) throws
      Exception {
    String bmbh = "";
    if(ORGID != null){
    	bmbh = getCusBmbh(ORGID, templateid, ywlx, conn, user);
    	if(!bmbh.equals(""))
    		return bmbh ;
    }
    String deptType = "";
    QuerySet qs = null;
    StringBuffer sqlStatment = null;
    try{
      if (bmjb != null&&ORGID!=null) {
        if ("0".equals(bmjb)) {
            bmbh =  "000000000000";
        }else if ("2".equals(bmjb)) {
          bmbh = ORGID.substring(0, 4) + "00000000";
        }
        else if ("3".equals(bmjb)) {
          bmbh = ORGID.substring(0, 6) + "000000";
        }
        else if ("4".equals(bmjb)) {
          bmbh = ORGID;
          sqlStatment = new StringBuffer();
          sqlStatment.append(
              " SELECT DEPTTYPE FROM ORG_DEPT WHERE ROW_ID = ?");

          Object paras[] = {
              ORGID
          };
          qs = DBUtil.executeQuery(sqlStatment.toString(), paras,conn);
          deptType = qs.getString(1,1);
          if(deptType==null || "".equals(deptType)){
            return ORGID;
          }else if ("3".equals(deptType)){
             bmbh = ORGID.substring(0, 6) + "000000";
          }else if("2".equals(deptType) ||"4".equals(deptType)){
              bmbh = ORGID;
          }

        }else{
           bmbh = ORGID;
        }

      }
      else {
          bmbh =  "000000000000";
      }
  }
  catch (SQLException ex) {
      ex.printStackTrace();
  }
    return bmbh;
  }

  public String getWsFileName(String fjbh){
      String fileName = "";
      String sql = "select * from pub_blob where fjbh='"+fjbh+"'";

      QuerySet qs = null;
      try {
          qs = DBUtil.executeQuery(sql, null);
      }
      catch (SQLException ex) {
          ex.printStackTrace();
      }
      if (qs.getRowCount() > 0) {
          fileName = qs.getString(1,"FILENAME");
      }


      return fileName;
  }
  /**
   * 获得文书xml文档
   * @param fjbh
   * @return
   * @throws java.lang.Exception
   */

  public Document getDoc(String sjbh,String ywlx,String templateid)
      throws Exception
  {
      BlobUtil blobUtil = new BlobUtil();
      String pub_blob_sql = "select MULTIMEDIA from PUB_BLOB where sjbh='" +sjbh + "' and ywlx = '"+ywlx+"' and ws_template_id = '"+templateid+"'";
      byte[] xmlBlob = blobUtil.get("blob", pub_blob_sql); //xml blob

      String result = null;
      SAXReader reader = new SAXReader();
      ByteArrayInputStream in = null;
      Document doc  =null;
      InputStreamReader   isr  = null;
      try
      {
          in = new ByteArrayInputStream(xmlBlob);
          isr   =   new   InputStreamReader(in,"UTF-8");
          doc = reader.read(isr);
       }
      catch (DocumentException ex)
      {
          ex.printStackTrace();
      }
      finally
      {
          if (in != null)
              in.close();
          if(isr!=null)
          isr.close();
      }
      return doc;

  }
  /**
   * 获得文书xml文档
   * @param fjbh
   * @return
   * @throws java.lang.Exception
   */

  public Document getDoc(String fjbh)
      throws Exception
  {
      BlobUtil blobUtil = new BlobUtil();
      String pub_blob_sql = "select MULTIMEDIA from PUB_BLOB where fjbh='" +fjbh + "'";
      byte[] xmlBlob = blobUtil.get("blob", pub_blob_sql); //xml blob

      String result = null;
      SAXReader reader = new SAXReader();
      ByteArrayInputStream in = null;
      Document doc  =null;
      InputStreamReader   isr  = null;
      try
      {
          in = new ByteArrayInputStream(xmlBlob);
          isr   =   new   InputStreamReader(in,"UTF-8");
          doc = reader.read(isr);
       }
      catch (DocumentException ex)
      {
          ex.printStackTrace();
      }
      finally
      {
          if (in != null)
              in.close();
          if(isr!=null)
          isr.close();
      }
      return doc;

  }
  /**
   * 获得文书某个域的元素对象
   * @param doc
   * @param fieldname
   * @return
   * @throws java.lang.Exception
   */
  public Element getFieldElement(Document doc ,String fieldname) throws Exception{

      List printRoot = doc.selectNodes("//PRINTDATA/ROW");
      String domainrole = null;
      String domainlevel = null;
      String domain = null;


      for (int i = 0; i < printRoot.size(); i++)
      {
          Element ele = (Element) printRoot.get(i);
          if (ele.getNodeType() != Node.ELEMENT_NODE)
          {
              continue;
          }
          if(fieldname.equalsIgnoreCase(ele.attributeValue("fieldname"))){
              return ele;
          }

      }
      return null;
  }


  /**
   *
   * @param doc
   * @param approverole
   * @param approvelevel
   * @param type
   * @return
   * @throws java.lang.Exception
   */
  public Element canGetAzt(Document doc ,String approverole,String approvelevel,String type) throws Exception{

        List printRoot = doc.selectNodes("//PRINTDATA/ROW");
        String domainrole = null;
        String domainlevel = null;
        String domain = null;


        for (int i = 0; i < printRoot.size(); i++)
        {
            Element ele = (Element) printRoot.get(i);
            if (ele.getNodeType() != Node.ELEMENT_NODE)
            {
                continue;
            }
            domainrole = ele.attributeValue("approverole");
            domainlevel = ele.attributeValue("approvelevel");
            domain = ele.attributeValue("domain");

            if (type.equals(domain))
            {
                if (Pub.empty(domainrole) && Pub.empty(domainlevel))
                { //如果文书定义的role和level都为空，那么直接将意见填入此域
                   return ele;
                }
                else if (!Pub.empty(domainrole) && !Pub.empty(domainlevel))
                { //文书定义的role,level非空
                    if (Pub.empty(approverole))
                    { //判断节点的role为空，那么比较两个level
                        if (domainlevel.equals(approvelevel))
                        {
                            return ele ;
                        }
                    }
                    else if (domainrole.equalsIgnoreCase(approverole))
                    { //节点role不为空，判断两个role
                        return ele ;
                    }
                }
                else if (!Pub.empty(domainlevel) && Pub.empty(domainrole))
                { //节点level不为空
                    if (domainlevel.equals(approvelevel))
                    {
                        return ele ;
                    }
                }
                else if (!Pub.empty(domainrole) && Pub.empty(domainlevel))
                { //文书定义的role非空
                    if (Pub.empty(approverole))
                    {
                    }
                    else if (domainrole.equalsIgnoreCase(approverole))
                    { //节点role不为空，判断两个role
                        return ele ;
                    }

                }
            }

        }


      return null;

  }

  /**
   * 判断文书是否已经打印
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public ActionForward hasDocDone(ActionMapping mapping, ActionForm form,
                                  HttpServletRequest request,
                                  HttpServletResponse response)
      throws Exception
  {
      String sjbh = request.getParameter("sjbh");
      String templateid = request.getParameter("templateid");
      String ywlx = request.getParameter("ywlx");

      User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
      String userLevel = user.getLevelName();
      String sql = null;
      try
      {

          sql =
              "select t.fjbh from pub_blob t where t.sjbh = ? and t.ws_template_id = ?  and t.ywlx= ?";
          QuerySet qs = DBUtil.executeQuery(sql, new Object[]
                                            {sjbh, templateid,ywlx});

          if (qs.getRowCount() < 1)
              Pub.writeMessage(response, "0");
          else
              Pub.writeMessage(response, "1");
      }
      catch (Exception e)
      {
          e.printStackTrace(System.out);
          Pub.writeMessage(response, "意外错误，判断[文书是否已存在]失败！！"); // + e.toString());
      }
      return null;
  }

// add by songxb@2008-02-28 查询文书变量

  public ActionForward getMargin(ActionMapping mapping, ActionForm form,
                                  HttpServletRequest request,
                                  HttpServletResponse response)
      throws Exception
  {
      String templateid = request.getParameter("templateid");
      //String ywlx = request.getParameter("ywlx");

      User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
      String userLevel = user.getLevelName();
      String sql = "select VARIABLE_FROM_VALUE from ws_template_variable where WS_TEMPLATE_ID=? and VARIABLE_NAME='margin'";
      String marginStr = "";
      try
      {

          QuerySet qs = DBUtil.executeQuery(sql, new Object[]{templateid});
          if(qs.getRowCount() > 0){
              marginStr = qs.getString(1,1);
          }
              Pub.writeMessage(response, marginStr);
      }
      catch (Exception e)
      {
          e.printStackTrace(System.out);
          Pub.writeMessage(response, "意外错误，判断[查询文书变量]出错！！"); // + e.toString());
      }
      return null;
  }

//add by andy@2008-06-21 查询文书有多少sheet页

  public ActionForward getSheets(ActionMapping mapping, ActionForm form,
                                  HttpServletRequest request,
                                  HttpServletResponse response)
      throws Exception
  {
      String templateid = request.getParameter("templateid");
      //String ywlx = request.getParameter("ywlx");

      String sql = "select VARIABLE_FROM_VALUE from ws_template_variable where WS_TEMPLATE_ID=? and VARIABLE_NAME='sheets'";
      String marginStr = "";
      Connection conn = null;
      try
      {
          conn = DBUtil.getConnection();

          QuerySet qs = DBUtil.executeQuery(sql, new Object[]{templateid},conn);
          if(qs.getRowCount() > 0){
              marginStr = qs.getString(1,1);
          }
              Pub.writeMessage(response, marginStr);
      }
      catch (Exception e)
      {
          e.printStackTrace(System.out);
          Pub.writeMessage(response, "意外错误，判断[查询文书变量]出错！！"); // + e.toString());
      }finally
      {
          if(conn != null) conn.close();
          conn = null;
      }
      return null;
  }
  public String changeFieldText(String wstemplateid,String fieldname,String sjbh,String ywlx,HttpServletRequest request) throws Exception{
	  String fieldtext= "";
	  String sql = getWs_template_sql(wstemplateid)+" and FIELDNAME = '"+fieldname+"'";
	  QuerySet sq = DBUtil.executeQuery(sql,null);
      Util util = new Util();

	  String domain_sql = null ;
	  String separator = null;
	  String canedit = null;
	  String dic ="";
	  if(sq!=null&&sq.getRowCount()>0){
		  domain_sql = sq.getString(1,"DOMAIN_SQL");
		  separator = sq.getString(1, "SEPARATOR");
		  dic= sq.getString(1, "CODEPAGE");
		  canedit = sq.getString(1, "CANEDIT");
	  }
	  if(Pub.empty(domain_sql)){
          domain_sql = "";
          
      }else{
        domain_sql = util.ParseSQL(domain_sql);
        fieldtext = getValue_sql(request, domain_sql,util.value,separator,wstemplateid,sjbh,ywlx);
        if ( (dic == null) ||(dic.equalsIgnoreCase(""))) {
        }
        else {
        	fieldtext = getCodeName(dic,fieldtext, separator);
        }
        
        if(Pub.empty(fieldtext)||"null".equalsIgnoreCase(fieldtext)){
        	fieldtext = "";
        }
      }
	  
	  
	  
	  if(!Pub.empty(domain_sql)){
		  // 非可编辑域，不更改
		  if (!"1".equals(canedit)) {
			  DBUtil.exec("update ap_process_ws set DOMAIN_VALUE='"+fieldtext+"' where WS_TEMPLATE_ID='"+wstemplateid+"' and FIELDNAME = '"+fieldname+"' and SJBH = '"+sjbh+"' and YWLX ='"+ywlx+"'");
		  } else {
			  if ("200503".equals(ywlx)) { 
				  // 当业务类型是发文时，直接返回文本域内容fieldtext
				  return fieldtext;
			  } else {
				  return null; 
			  }
		  }
		  
	  }else{
		  return null;
	  }
	  
	  return fieldtext;
  }
  
  private String getCusBmbh(String orgid, String templateid, String ywlx, Connection conn, User user) throws Exception 
  {
	  String bmbh = "";
	  String cus_dept_leveal = user.getOrgDept().getCus_dept_level();
	  String jz = user.getOrgDept().getJz();
	  try{
		  if(cus_dept_leveal != null && !cus_dept_leveal.equals("") && jz != null && !jz.equals("")){	//文书流水规则【10：省厅流水，20：市局流水，30：分局流水，40：支队流水，50：所在部门流水】
			  String queryLsgz = "SELECT WSLSGZ FROM ZA_ZFBA_JCXX_WS_DOC_SEQUENCE WHERE WS_TEMPLATE_ID='"+templateid+"' "
			  	+ " AND YWLX='"+ywlx+"' AND JZ='"+jz+"' AND CUS_DEPT_LEVEL='"+cus_dept_leveal+"'";
			  String[][] lsgzRes = DBUtil.query(conn, queryLsgz);
			  if(lsgzRes != null){
				  String lsgz = lsgzRes[0][0];
				  switch(Integer.parseInt(lsgz))
				  {
			  	  	  case 10: // 省厅流水			  		  
			  	  		  break;				  
				  	  case 20: // 市局流水				  		  
				  		  break;
				  	  case 30: // 分局流水
				  		  bmbh = orgid.substring(0, 6) + "000000";
				  		  break;
				  	  case 40: // 支队流水
				  		  bmbh = user.getOrgDept().getDeptParentID();				  		  
				  		  break;
				  	  case 50: // 所在部门流水
				  		  bmbh = orgid;
				  		  break;				  	
				  }					  				  
			  }
		  }
	  }catch(Exception e){
		  e.printStackTrace();
	  }
	  return bmbh ;
  }
}
