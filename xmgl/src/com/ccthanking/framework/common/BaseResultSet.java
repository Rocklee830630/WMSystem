package com.ccthanking.framework.common;

import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;

import sun.misc.BASE64Encoder;

import com.ccthanking.common.EventManager;
import com.ccthanking.common.vo.EventVO;
import com.ccthanking.framework.base.BaseDAO;
import com.ccthanking.framework.coreapp.orgmanage.OrgDeptManager;
import com.ccthanking.framework.coreapp.orgmanage.OrgRoleManager;
import com.ccthanking.framework.coreapp.orgmanage.UserManager;
import com.ccthanking.framework.dic.Dics;
import com.ccthanking.framework.dic.TreeNode;
import com.ccthanking.framework.fileUpload.service.FileUploadService;
import com.ccthanking.framework.util.DateTimeUtil;
import com.ccthanking.framework.util.Pub;
import com.ccthanking.framework.util.Translater;
public class BaseResultSet
{
    private ResultSet resultset;
    private Statement ps;
    private Hashtable hashtab;
    private Hashtable infotab;
    private PageManager page;
    private ResultSetMetaData rsmd;
    private Document doc;
	private String originalSQL;
    private static final String EXT_ORG_INFO = "_ORG_INFO_!@#";
    private static final String EXT_ORG_DEPT = "_ORG_DEPT_$%^";
    private static final String EXT_ORG_ROLE = "_ORG_ROLE_$%^";
    private static final String EXT_DATE_FMT = "_DATE_FMT_&*(";
    private static final String EXT_DEPT_SMP = "_DEPT_SMP_)!@";
    private static final String EXT_USER_ID = "_USER_ID_#$%^";
    private static final String EXT_AREA_ID = "_AREA_ID_&*()";
    private static final String EXT_XZJG = "_XZJG_ID_*)%@";
    private static final String EXT_XZJGJC = "_XZJG_JC_*&(%";
    private static final String EXT_JLX = "_DZ_JLX_&)@%";
    private static final String EXT_QDZ = "_DZ_QDZ_*^(@";
    private static final String EXT_SJBH = "_SJBH_(&%$";
    private static final String EXT_CLOB = "_CLOB_*&^%";
    private static final String EXT_WRIT = "_WRIT_&^$#";
    private static final String EXT_RKBM = "_RK_RKBM_%#@&(";
    private static final String EXT_FILEUPLOAD = "_FILEUPLOAD_*&^%";
    private static final String EXT_FILEUPLOAD_WYBH = "_FILEUPLOAD_WYBH_*&^%";
    private static final String EXT_THOUSANDS = "_THOUSANDS%&#!(";
    private static final String EXT_DECIMALS = "_Decimals^%$%#";
    private static final String EXT_MOVE_TO_LEFT = "_MoveToLeft^%$%#";

    private static final String EXT_CUSTOM="_CUSTOM_&*$#";
    private static final String EXT_TRANSLATER="TRANSLATER%$@#$";
    private static final String EXT_TABLENAME="TABLENAME%$@#$";
    private static final String EXT_CODEFIELD="CODEFIELD%$@#$";
    private static final String EXT_VALUEFIELD="VALUEFIELD%$@#$";
    public void Close()
    {
        try
        {
            if (this.resultset != null)
                this.resultset.close();
            if (this.ps != null)
                this.ps.close();
            this.resultset = null;
            this.ps = null;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public BaseResultSet(ResultSet resultSet, Statement statement)
    {
        this.resultset = resultSet;
        try
        {
            if (resultset != null)
                rsmd = resultset.getMetaData();
        }
        catch (SQLException ex)
        {
        }
        this.hashtab = new Hashtable();
        this.ps = statement;
    }

    public BaseResultSet(ResultSet set, Statement statement, PageManager page)
    {
        this.resultset = set;
        this.page = page;
        try
        {
            if (resultset != null)
                rsmd = resultset.getMetaData();
        }
        catch (SQLException ex)
        {
        }
        this.hashtab = new Hashtable();
        this.ps = statement;
    }

    public void setPage(PageManager aPage)
    {
        this.page = aPage;
    }

    public PageManager getPage()
    {
        return this.page;
    }
//  fieldName 需要翻译的字段名称
    //tableName 表名称
    //titleColumn 表中汉字字段名称
    public void setCustomField(String fieldName,String tableName,String titleColumn)
    {
        hashtab.put(fieldName.toUpperCase()+EXT_CUSTOM, tableName+"@"+titleColumn);
    }
    public void setFieldDic(int index, String dicname)
    {
        try
        {
            String colname = rsmd.getColumnName(index);
            setFieldDic(colname, dicname);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public void setFieldDic(String fieldname, String dicname)
    {
        hashtab.put(fieldname.toUpperCase(), dicname);
    }

    public void setFieldXZJG(int index)
    {
        try
        {
            this.setFieldXZJG(rsmd.getColumnName(index));
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
    }

    public void setFieldXZJG(String fieldname)
    {
        this.hashtab.put(fieldname.toUpperCase() + EXT_XZJG, "6");
    }
    public void setFieldThousand(String fieldname)
    {
    	this.hashtab.put(fieldname.toUpperCase() + EXT_THOUSANDS,"0");
    }
    public void setFieldDecimals(String fieldname)
    {
    	this.hashtab.put(fieldname.toUpperCase() + EXT_DECIMALS,"0");
    }
    
    public void setFieldMoveToLeft(String fieldname) {
    	this.hashtab.put(fieldname.toUpperCase() + EXT_MOVE_TO_LEFT, "0");
    }

    public void setFieldSimpleXZJG(int index)
    {
        try
        {
            this.setFieldSimpleXZJG(rsmd.getColumnName(index));
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
    }

    public void setFieldSimpleXZJG(String fieldname)
    {
        this.hashtab.put(fieldname.toUpperCase() + EXT_XZJGJC, "6");
    }

    public void setFieldOrgAdm(int index)
    {
        try
        {
            this.setFieldOrgAdm(rsmd.getColumnName(index));
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
    }

    public void setFieldOrgAdm(String fieldname)
    {
        this.hashtab.put(fieldname.toUpperCase() + EXT_ORG_INFO, "6");
    }

    public void setFieldOrgRole(int index)
    {
        try
        {
            this.setFieldOrgRole(rsmd.getColumnName(index));
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
    }

    public void setFieldOrgRole(String fieldname)
    {
        hashtab.put(fieldname.toUpperCase() + EXT_ORG_ROLE, "0");
    }
    public void setFieldClob(int index)
    {
        try
        {
            this.setFieldClob(rsmd.getColumnName(index));
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
    }

    public void setFieldClob(String fieldname)
    {
        hashtab.put(fieldname.toUpperCase() + EXT_CLOB, "0");
    }

    public void setFieldOrgDept(int index)
    {
        try
        {
            this.setFieldOrgDept(rsmd.getColumnName(index));
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
    }
    
    public void setFieldTranslater(String fieldName,String tableName,String codeField,String valueField){    
    	hashtab.put(fieldName.toUpperCase() + EXT_TRANSLATER, fieldName);
    	hashtab.put(fieldName.toUpperCase() + EXT_TABLENAME, tableName);
    	hashtab.put(fieldName.toUpperCase() + EXT_CODEFIELD, codeField);
    	hashtab.put(fieldName.toUpperCase() + EXT_VALUEFIELD, valueField);
    }

    public void setFieldOrgDept(String fieldname)
    {
        hashtab.put(fieldname.toUpperCase() + EXT_ORG_DEPT, "0");
    }

    public void setFieldUserID(int index)
    {
        try
        {
            this.setFieldUserID(rsmd.getColumnName(index));
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
    }
    /**
     * 查询是否打印过文书 返回文书个数
     * @param index
     */
    public void setFieldWrit(int index)
    {
        try
        {
            this.setFieldWrit(rsmd.getColumnName(index));
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }

    }

    public void setFieldWrit(String fieldname)
    {
        hashtab.put(fieldname.toUpperCase() + EXT_WRIT, "0");
    }
    
    public void setFieldSjbh(int index){
        try
        {
            this.setFieldSjbh(rsmd.getColumnName(index));
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }

    }

    public void setFieldSjbh(String fieldname)
    {
        hashtab.put(fieldname.toUpperCase() + EXT_SJBH, "0");
    }

    public void setFieldDzJlx(String fieldname)
    {
        hashtab.put(fieldname.toUpperCase() + EXT_JLX, "0");
    }
    
    public void setFieldFileUpload(String fieldname,String fjlb)
    {
    	hashtab.put(fieldname.toUpperCase() + EXT_FILEUPLOAD, fjlb);
    }
    
    public void setFieldFileUploadWithWybh(String fieldname,String fjlb){
    	hashtab.put(fieldname.toUpperCase() + EXT_FILEUPLOAD_WYBH, fjlb);
	}
    
    public void setFieldDzJlx(int index)
    {
        try
        {
            this.setFieldDzJlx(rsmd.getColumnName(index));
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
    }

    public void setFieldDzQdz(String fieldname)
    {
        hashtab.put(fieldname.toUpperCase() + EXT_QDZ, "0");
    }

    public void setFieldDzQdz(int index)
    {
        try
        {
            this.setFieldDzQdz(rsmd.getColumnName(index));
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
    }

    public void setFieldUserID(String fieldname)
    {
        hashtab.put(fieldname.toUpperCase() + EXT_USER_ID, "0");
    }

    public void setFieldOrgDeptSimpleName(int index)
    {
        try
        {
            this.setFieldOrgDeptSimpleName(rsmd.getColumnName(index));
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
    }

    public void setFieldOrgDeptSimpleName(String fieldname)
    {
        hashtab.put(fieldname.toUpperCase() + EXT_DEPT_SMP, "0");
    }
    public void setFieldRkRkbm(String fieldname)
    {
        hashtab.put(fieldname.toUpperCase() + EXT_RKBM, "0");
    }

    public void setFieldRkRkbm(int index)
    {
        try
        {
            this.setFieldDzJlx(rsmd.getColumnName(index));
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
    }
    //end
    
    public void setFieldDateFormat(int index, String format)
    {
        try
        {
            this.setFieldDateFormat(rsmd.getColumnName(index), format);
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
    }

    public void setFieldDateFormat(String fieldname, String format)
    {
        hashtab.put(fieldname.toUpperCase() + EXT_DATE_FMT, format);
    }

    public String getXml()
    {
        try
        {
            return this.getDocument().asXML();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return "";
        }
    }

    public ResultSet getResultSet()
    {
        return this.resultset;
    }

    public void addInfo(String tagName, String text)
        throws Exception
    {
        if (Pub.empty(tagName) || Pub.empty(text))
            throw new Exception("不能添加空的信息！");
        if (infotab == null)
            infotab = new Hashtable(1);
        infotab.put(tagName, text);
    }
    public String getJSON() {
		JSONObject json = new JSONObject();
		String result = "";
		try {
			JSONArray dataArr = new JSONArray();
			JSONObject pageObj = new JSONObject();
			while (resultset.next()) {
				JSONObject rowObj = new JSONObject();
				for (int i = 1; i <= rsmd.getColumnCount(); i++) {
					String colname = rsmd.getColumnName(i).toUpperCase();
					int coltype = rsmd.getColumnType(i);
					String colvalue = resultset.getString(i);
					String svValue = null;
					if (coltype == java.sql.Types.DATE
							|| coltype == java.sql.Types.TIMESTAMP) {
						//------------------------处理日期类型--------------------------
						String format = hashtab.get(colname + EXT_DATE_FMT) == null ? "yyyy-MM-dd"
								: (String) hashtab.get(colname + EXT_DATE_FMT);
						Timestamp d = resultset.getTimestamp(i);
						if (d != null){
							svValue = DateTimeUtil.getDateTime(d, format);
                            colvalue = svValue;
						}else{
                            colvalue = resultset.getString(i);
                            svValue = "";
						}
						if(Pub.empty(colvalue)){
							colvalue="";
						}
					}else if (coltype == java.sql.Types.BLOB) {
						//------------------------处理BLOB类型--------------------------
						Blob dbBlob;
						dbBlob = resultset.getBlob(i);
						if (dbBlob != null) {
							int length = (int) dbBlob.length();
							byte[] buffer = dbBlob.getBytes(1, length);
							if (hashtab.get(colname + EXT_CLOB) != null) {
								colvalue = new String(buffer, "GBK");
							} else {
								colvalue = Pub.toBase64(buffer);
							}
						}
					}else if (coltype == java.sql.Types.NUMERIC) {
						//------------------------处理数字类型--------------------------
						if(Pub.empty(colvalue)){
							colvalue="";
						}else{
							BigDecimal bigdecimal = resultset.getBigDecimal(i);
							colvalue = bigdecimal.toString();
						}
					} else {
						//------------------------处理其他类型--------------------------
						colvalue = resultset.getString(i)==null?"":resultset.getString(i);
					}
					//------------------------处理字典--------------------------
					String dic = getDicName(colname);
					if(!Pub.empty(dic)){
						TreeNode tn = Dics.getDicByName(dic);
						if (tn != null) {
							String[] dics = colvalue.split(",");
							if (dics.length == 1) {
								tn = tn.getNodeByCode(colvalue);
								if (tn != null) {
									svValue = tn.getDicValue();
								}
							} else {
								String sv = "";
								TreeNode temp;
								for (int m = 0; m < dics.length; m++) {
									temp = tn.getNodeByCode(dics[m]);
									if (temp != null) {
										sv = sv + temp.getDicValue();
									}
									if (m < dics.length - 1)
										sv += ",";
								}
								svValue = sv;
							}
						}
					}
					//----------------------最后将处理后的数据放入行对象中--------------------------
					if(!Pub.empty(svValue)){
						rowObj.put(colname+"_SV", svValue);
					}
					rowObj.put(colname, colvalue);
				}
				dataArr.add(rowObj);
			}
			pageObj.put("currentpagenum", String.valueOf(page.getCurrentPage()));
			pageObj.put("recordsperpage", String.valueOf(page.getPageRows()));
			pageObj.put("totalpage", String.valueOf(page.getCountPage()));
			pageObj.put("countrows", String.valueOf(page.getCountRows()));
			JSONObject dataObj = new JSONObject();
			dataObj.put("data", dataArr);
			json.put("response", dataObj);
			json.put("pages", pageObj);
			result = json.toString();
		} catch (Exception e) {
			result = "-1";
		}
		return result;
	}
	public String getJson()
    {
    	String json_ = "";
    	boolean dicTreatFlag = true;
    	try{
    	if (page != null)
        {
    		json_  = "{\"response\":";
    		
    		if(page.getCountRows()>0)
    		{
    			json_ +="{\"data\":[";
    			 while (resultset.next())
    	         {
    				 json_ +="{";
    				 String json = "";
    	                for (int i = 1; i <= rsmd.getColumnCount(); i++)
    	                {
    	                    String colname = rsmd.getColumnName(i).toUpperCase();
    	                    int coltype = rsmd.getColumnType(i);
    	                    String colvalue = "";
    	                    String svValue = null;
    	                    if (coltype == java.sql.Types.DATE ||
    	                        coltype == java.sql.Types.TIMESTAMP)
    	                    {
    	                        String format = hashtab.get(colname + EXT_DATE_FMT) == null ?
    	                            "yyyy-MM-dd" :
    	                            (String) hashtab.get(colname + EXT_DATE_FMT);
    	                        Timestamp d = resultset.getTimestamp(i);
    	                        if (d != null)
    	                        {
    	                            svValue = DateTimeUtil.getDateTime(d, format);
    	                            colvalue = Pub.getDate("yyyyMMddHHmmss", d);
    	                            //edit by cbl 返回一致
    	                            colvalue = svValue;
    	                            //edit by cbl end
    	    						
    	                        }
    	                        else
    	                        {
    	                            colvalue = resultset.getString(i);
    	                            svValue = "";
    	                        }
    							if(Pub.empty(colvalue))
    							{
    								colvalue="";
    							}
    							colvalue = BaseDAO.EncodeJsString(colvalue);
    							svValue = BaseDAO.EncodeJsString(svValue);
    	                    }else if (coltype == java.sql.Types.BLOB) {
    							Blob dbBlob;
    							dbBlob = resultset.getBlob(i);
    							if (dbBlob != null) {
    								int length = (int) dbBlob.length();
    								byte[] buffer = dbBlob.getBytes(1, length);
    								if (hashtab.get(colname + EXT_CLOB) != null) {
    									colvalue = new String(buffer, "GBK");
    									colvalue = BaseDAO.EncodeJsString(colvalue);
    								} else {
    									colvalue = Pub.toBase64(buffer);
    								}
    							}
    						}else if (coltype == java.sql.Types.NUMERIC) {//增加对number类型的处理
    							BigDecimal bigdecimal = resultset.getBigDecimal(i);
    							if(bigdecimal!=null)
    							colvalue = bigdecimal.toString();
    							if(Pub.empty(colvalue))
    							{
    								colvalue="";
    							}
    						} else {
    							colvalue = resultset.getString(i);
    							//add by cbl
    							if(!Pub.empty(colvalue))
    							{
    								colvalue = BaseDAO.EncodeJsString(colvalue);
    							//
    							}
    							if(Pub.empty(colvalue))
    							{
    								colvalue="";
    							}
    						}
    	                    if (hashtab.get(colname + EXT_SJBH) != null) {
    							if (!Pub.empty(colvalue)) {
    								EventVO eventvo = EventManager.getEventByID(
    										this.ps.getConnection(), colvalue);
    								json +="\"EVENTSJBH\":\"";
    								if (eventvo == null) {
    									//如果为空 后面拼出逗号
    									json +="\",";
    									continue;
    								}
//    								json += value+","+key.toLowerCase()+"_sv:"+strval;
    								json +=eventvo.getSjzt()+"\",";
    								//"{code:"+eventvo.getSjzt()+",";
    								TreeNode tn = Dics.getDicByName("SJZT");
    								if (tn != null) {
    									tn = tn.getNodeByCode(eventvo.getSjzt());
    									if (tn != null) {
    										String strDic = tn.getDicValue();
    										json +="\"EVENTSJBH_SV\":\""+strDic+"\",";
    									}
    								}
    								json +="\""+colname+"\":\""+colvalue+"\",";
    							}else{
    								json +="\""+colname+"\":\"\",\"EVENTSJBH\":\"\",\"EVENTSJBH_SV\":\"\",";
    							}
    							continue;
    						}
    	                    json += "\""+colname+"\":";
    	                   
    	                    
    	                    String dic = getDicName(colname);
    						if (!Pub.empty(dic)) {
    							TreeNode tn = Dics.getDicByName(dic);
    							if (tn != null) {
    								if (colvalue == null)
    									colvalue = "\"\"";
    								String[] dics = colvalue.split(",");
    								if (dics.length == 1) {
    									tn = tn.getNodeByCode(colvalue);
    									if (tn != null) {
    										String strDic = tn.getDicValue();
    										
    										json += "\""+colvalue+"\",\""+colname+"_SV\":\""+strDic+"\",";
    										//json +="{code:'"+colvalue+"',value:'"+strDic+"'},";
    									}else{
    										json +="\""+colvalue+"\",";
    									}
    								} else {
    									String sv = "";
    									TreeNode temp;
    									for (int m = 0; m < dics.length; m++) {
    										temp = tn.getNodeByCode(dics[m]);
    										if (temp != null) {
    											sv = sv + temp.getDicValue();
    										}
    										if (m < dics.length - 1)
    											sv += ",";
    									}
    									json += "\""+colvalue+"\",\""+colname+"_SV\":\""+sv+"\",";
    									//json +="{code:'"+colvalue+"',value:'"+sv+"'},";
    								}
    							}else
    							{
    								json +="\""+colvalue+"\",";
    							}
    						}else  if (hashtab.get(colname + EXT_ORG_DEPT) != null) {
    							OrgDept dept = OrgDeptManager.getInstance()
    									.getDeptByID(colvalue) == null ? null
    									: OrgDeptManager.getInstance().getDeptByID(
    											colvalue);
    							String strDic = dept == null ? colvalue : dept
    									.getDeptName();

    							//json +="{code:'"+colvalue+"',value:'"+strDic+"'},";
    							if(dept!=null){
    							  json +="\""+ colvalue+"\",\""+colname+"_SV\":\""+strDic+"\",";
    							}else{
    							  json +="\""+colvalue+"\",";
    							}
    						}else if (hashtab.get(colname + EXT_ORG_ROLE) != null)
    	                    {
    	                        String strDic = "";
    	                        if (colvalue != null)
    	                            strDic = OrgRoleManager.getInstance().getRole(
    	                                colvalue) == null ? colvalue :
    	                                OrgRoleManager.getInstance().getRole(colvalue).
    	                                getMemo();
    	                        if(!Pub.empty(strDic)){
      							  json +="\""+ colvalue+"\",\""+colname+"_SV\":\""+strDic+"\",";
      							}else{
      							  json +="\""+colvalue+"\",";
      							}
    	                    } else
    	                    if (hashtab.get(colname + EXT_TRANSLATER) != null)
    	                    {
    	                        String tableName=(String)hashtab.get(colname + EXT_TABLENAME);
    	                        String codeField=(String)hashtab.get(colname + EXT_CODEFIELD);
    	                        String valueField=(String)hashtab.get(colname + EXT_VALUEFIELD);
    	                        Translater translater=new Translater(tableName,codeField,valueField);    
    	                        String sv = "";
    	                        if(translater!=null)
    	                        {
    	                        	String s = translater.translate(colvalue);
    	                        	if(!Pub.empty(s))
    	                        	{
    	                        		sv = s;
    	                        	}
    	                        	
    	                        }
    	                        if(!Pub.empty(sv))
    	                        {
    	                        	json +="\""+ colvalue+"\",\""+colname+"_SV\":\""+sv+"\",";
    	                        }else{
    	                        	//json +="\""+colvalue+"\","; 
    	                        	json +="\"\",";  //表选字典，如果没有value值则返回空串
    	                        }                         
    	                    }else if(hashtab.get(colname+EXT_FILEUPLOAD) != null){
      							if (!Pub.empty(colvalue)) {
      								String fjlb = (String)hashtab.get(colname + EXT_FILEUPLOAD);
      								ArrayList arr =FileUploadService.getUploadFiles(colvalue,fjlb);
      								
      								if(arr==null||arr.size()==0)
      								{
      									json +="\""+colvalue+"\",";
      								}else{
      									
//      									String filesid = (String)arr.get(0);
      									String[] filesid = (String[])arr.get(0);
      									String[] filenames = (String[])arr.get(1);
      									if(filenames!=null)
      									{
											String filehrefHTML = "";
//											if (filenames.length <= 3) {
//
//												for (int s = 0; s < filenames.length; s++) {
//													 String temp="<a href=\"javascript:void(0);\" onclick=\"checkupFiles('"+filesid[s]+"')\"><img src=\"/xmgl/images/icon-download.png\" title=\"点击查看"+filenames[s]+"\"></a>&nbsp;";
//													//String temp = "<i class=\"icon-download-alt\" title=\""+ filenames[s]+ "\" onclick=\"checkupFiles('"+ filesid[s]+ "')\"></i>&nbsp;";
//													temp = BaseDAO.EncodeJsString(temp);
//													filehrefHTML += temp;
//												}
//											}else{
											    String function = "";
											    if(filenames.length==1){
											    	function = "checkupFiles('"+filesid[0]+"')";
											    }else{
											    	function = "showAllFiles('"+colvalue+"','"+fjlb+"')";
											    }
												String temp = "<a href=\"javascript:void(0);\" onclick=\""+function+"\"><img src=\"/xmgl/images/icon-annex.png\"  title=\"点击查看附件\"></a>";
												temp = BaseDAO.EncodeJsString(temp);
												filehrefHTML +=temp;
										//	}
      										json +="\""+ colvalue+"\",\""+colname+"_SV\":\""+filehrefHTML+"\",";
      									}else{
      										json +="\""+ colvalue+"\",\""+colname+"_SV\":\"\",";
      									}	
      								}
      								
    						
    							}else{
    								json +="\""+colvalue+"\",";
    							}
    	                    }else if(hashtab.get(colname+EXT_FILEUPLOAD_WYBH) != null){
      							if (!Pub.empty(colvalue)) {
      								String fjlb = (String)hashtab.get(colname + EXT_FILEUPLOAD_WYBH);
      								ArrayList arr =FileUploadService.getUploadFilesWithWybh(colvalue,fjlb);
      								
      								if(arr==null||arr.size()==0)
      								{
      									json +="\""+colvalue+"\",";
      								}else{
      									String[] filesid = (String[])arr.get(0);
      									String[] filenames = (String[])arr.get(1);
      									if(filenames!=null)
      									{
											String filehrefHTML = "";
											String function = "";
										    if(filenames.length==1){
										    	function = "checkupFiles('"+filesid[0]+"')";
										    }else{
										    	function = "showAllFilesWithWybh('"+colvalue+"','"+fjlb+"')";
										    }
											String temp = "<a href=\"javascript:void(0);\" onclick=\""+function+"\"><img src=\"/xmgl/images/icon-annex.png\"  title=\"点击查看附件\"></a>";
											temp = BaseDAO.EncodeJsString(temp);
											filehrefHTML +=temp;
      										json +="\""+ colvalue+"\",\""+colname+"_SV\":\""+filehrefHTML+"\",";
      									}else{
      										json +="\""+ colvalue+"\",\""+colname+"_SV\":\"\",";
      									}	
      								}
    							}else{
    								json +="\""+colvalue+"\",";
    							}
    	                    }
    						
    						
    						else if (hashtab.get(colname + EXT_USER_ID) != null) {
    							if (colvalue == null){
    								colvalue = "";
    								json +="";
    								continue;
    							}
    								
    							String[] dics = colvalue.split(",");
    							if (dics.length == 1) {
    								String strDic = "";
    								if (colvalue != null) {
    								    if(UserManager.getInstance().getUserByLoginNameFromNc(colvalue)==null) {
        									// 所有用户
        									User userFromDB = UserManager.getInstance().getUserFromDB(colvalue, false);
    								    	strDic = userFromDB == null ? colvalue : userFromDB.getName();
    								    } else {
    								    	strDic = UserManager.getInstance().getUserByLoginNameFromNc(colvalue).getName();
    								    }
    								}
    								//json +="{code:'"+colvalue+"',value:'"+strDic+"'},";
    								json += "\""+colvalue+"\",\""+colname+"_SV\":\""+strDic+"\",";
    							} else {
    								String sv = "";
    								String temp;
    								for (int m = 0; m < dics.length; m++) {
    									temp = UserManager.getInstance()
    											.getUserByLoginNameFromNc(dics[m]) == null ? dics[m]
    											: UserManager.getInstance()
    													.getUserByLoginNameFromNc(dics[m])
    													.getName();
    									if (temp != null) {
    										sv = sv + temp;
    									}
    									if (m < dics.length - 1)
    										sv += ",";

    								}
    								
    							//	json +="{code:'"+colvalue+"',value:'"+sv+"'},";
    								json += "\""+colvalue+"\",\""+colname+"_SV\":\""+sv+"\",";
    							}
    							
							} else if (hashtab.get(colname + EXT_THOUSANDS) != null) {
								
								if (colvalue == null){
    								colvalue = "";
    								json +="";
    								continue;
    							}else{
    								String sv = "";
    								sv = Pub.NumberToThousand(colvalue);
    								if(!Pub.empty(sv)){
    									sv = Pub.NumberAddDec(sv);
    								}
    								json += "\""+colvalue+"\",\""+colname+"_SV\":\""+sv+"\",";
    							}

							}else if (hashtab.get(colname + EXT_DECIMALS) != null) {
								
								if (colvalue == null){
    								colvalue = "";
    								json +="";
    								continue;
    							}else{
    								String sv = "";
    								sv = Pub.DecimalsFormat(colvalue);
    								json += "\""+colvalue+"\",\""+colname+"_SV\":\""+sv+"\",";
    							}

							} else if(hashtab.get(colname + EXT_MOVE_TO_LEFT) != null) {
								if (colvalue == null){
    								colvalue = "";
    								json +="";
    								continue;
    							}else if("0".equals(colvalue) || "".equals(colvalue)){
    								String sv = "";
    								sv = "0";
    								json += "0,\""+colname+"_SV\":\""+sv+"\",";
    							}else{
    								String sv = "";
    								sv = Pub.moveToleft3(colvalue);
    								json += "\""+colvalue+"\",\""+colname+"_SV\":\""+sv+"\",";
    							}
							}

							else {
    							 if (svValue != null){
    	    	                   // 	json +="{code:'"+colvalue+"',value:'"+svValue+"'},";
    								 json += "\""+colvalue+"\",\""+colname+"_SV\":\""+svValue+"\",";
    	    	                 }else
    	    	                 {
    	    	                	 json +="\""+colvalue+"\",";
    	    	                 }
    						}
    			
    		}//for end
    	               json = json.substring(0, json.length()-1)+"},";
    	               json_ +=json;
        }//while end
    			 json_ = json_.substring(0, json_.length()-1)+"]},";
    			 json_ +="pages:{";
    			 json_ +="currentpagenum:"+String.valueOf(page.getCurrentPage())+",";
    			 json_ +="recordsperpage:"+String.valueOf(page.getPageRows())+",";
    			 json_ +="totalpage:"+String.valueOf(page.getCountPage())+",";
    			 json_ +="countrows:"+String.valueOf(page.getCountRows())+"}}";
       }else{
    	   json_ ="0";
       }
    		
      }
    	} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (this.resultset != null)
					this.resultset.close();
				if (this.ps != null)
					this.ps.close();
				this.resultset = null;
				this.ps = null;
			} catch (Exception ee) {
				ee.printStackTrace();
				json_ ="-1";
			}
		}
    	
    	
    	//json = BaseDAO.EncodeJsString(json);//处理返回前台的特殊符号
    	
    	return json_;
    }

    public Document getDocument()
    {
        if (this.doc != null)
            return this.doc;
        try
        {
            Document domresult = DocumentFactory.getInstance().createDocument();
            Element root = domresult.addElement("RESPONSE");

			//添加配置节点
			boolean dicTreatFlag = true;
			Element confInfoRoot = root.addElement("CONF");
			//创建配置信息dom结构
			Document domconf = DocumentFactory.getInstance().createDocument();
			Element confRoot = domconf.addElement("CONF");
			Element dicsRoot = confRoot.addElement("DICS");
			Element sqlRoot = confRoot.addElement("SQL");
			sqlRoot.addText(this.originalSQL);

            Element resultRoot = root.addElement("RESULT");
            if (page != null)
            {
                resultRoot.addAttribute("currentpagenum",
                                        String.valueOf(page.getCurrentPage()));
                resultRoot.addAttribute("recordsperpage",
                                        String.valueOf(page.getPageRows()));
                resultRoot.addAttribute("totalpage",
                                        String.valueOf(page.getCountPage()));
                resultRoot.addAttribute("countrows",
                                        String.valueOf(page.getCountRows()));
            }
            if (this.infotab != null)
            {
                Element info = root; //root.addElement("INFO");
                Enumeration enumer = infotab.keys();
                while (enumer.hasMoreElements())
                {
                    String key = (String) enumer.nextElement();
                    info.addElement(key).setText( (String) infotab.get(key));
                }
            }

            while (resultset.next())
            {
                Element recordItem = resultRoot.addElement("ROW");
                for (int i = 1; i <= rsmd.getColumnCount(); i++)
                {
                    String colname = rsmd.getColumnName(i).toUpperCase();
                    int coltype = rsmd.getColumnType(i);
                    String colvalue = "";
                    String svValue = null;
                    if (coltype == java.sql.Types.DATE ||
                        coltype == java.sql.Types.TIMESTAMP)
                    {
                        String format = hashtab.get(colname + EXT_DATE_FMT) == null ?
                            "yyyyMMdd" :
                            (String) hashtab.get(colname + EXT_DATE_FMT);
                        Timestamp d = resultset.getTimestamp(i);
                        if (d != null)
                        {
                            svValue = DateTimeUtil.getDateTime(d, format);
                            colvalue = Pub.getDate("yyyyMMddHHmmss", d);
    						if(dicTreatFlag)
    						{
    							Element dicelem = dicsRoot.addElement("DIC");
    							dicelem.addAttribute("fname", colname);
    							dicelem.addAttribute("dicname", "DATE");
    							dicelem.addAttribute("dateformate", format);
    						}

                        }
                        else
                        {
                            colvalue = resultset.getString(i);
                            svValue = colvalue;
                        }
                    }
                    else if (coltype == java.sql.Types.BLOB)
                    {
                        Blob dbBlob;
                        dbBlob = resultset.getBlob(i);
                        if (dbBlob != null)
                        {
                            int length = (int) dbBlob.length();
                            byte[] buffer = dbBlob.getBytes(1, length);
                            if(hashtab.get(colname+EXT_CLOB)!=null){
                                colvalue = new String(buffer,"GBK");
                            }else{
                                colvalue = Pub.toBase64(buffer);
                            }
                        }
                    }
                    else
                    {
                        colvalue = resultset.getString(i);
                    }



                    if(hashtab.get(colname+EXT_SJBH)!=null){
                        if(!Pub.empty(colvalue)){
                            EventVO eventvo = EventManager.getEventByID(this.ps.getConnection(),colvalue);
                            Element fieldNode = recordItem.addElement("EVENTSJZT");
                            if(eventvo==null)
                            {
                                 fieldNode.addAttribute("sv", "-1");
                                 continue;
                            }
                            fieldNode.addText(eventvo.getSjzt());
                            TreeNode tn = Dics.getDicByName("SJZT");
                            if (tn != null)
                            {
                                tn = tn.getNodeByCode(eventvo.getSjzt());
                                if (tn != null)
                                {
                                    String strDic = tn.getDicValue();
                                    fieldNode.addAttribute("sv", strDic);
                                }
                            }
                        }
                    }
                    /**
                    if(hashtab.get(colname+EXT_WRIT)!=null){
                        if(!Pub.empty(colvalue)){
                            String count = Pub.getWritCount(colvalue,this.ps.getConnection());
                            Element fieldNode = recordItem.addElement("WRITCOUNT");
                            fieldNode.addText(count);
                        }
                    }
                    **/
                    Element fieldNode = recordItem.addElement(colname);
                    if (colvalue != null)
                    {	
                        fieldNode.addText(colvalue);
                        int n=0;
                        if(colvalue.startsWith(" "))
                        {
                        	n++;
                        	for(int j=1;j<colvalue.length();j++)
                        	{
                        		if(" ".equals(String.valueOf(colvalue.charAt(j))))
                        			n++;
                        		else
                        			break;
                        	}
                        }
                        if(n>0)
                        {	
                           fieldNode.addAttribute("spaces", String.valueOf(n));
                        }
                    }    
                    if (svValue != null)
                        fieldNode.addAttribute("sv", svValue);
                    String dic = getDicName(colname);
                    if (!Pub.empty(dic))
                    {
                        TreeNode tn = Dics.getDicByName(dic);

                        if (tn != null)
                        {
                            if(colvalue==null)colvalue="";
                            String[] dics  = colvalue.split(",");
                            if(dics.length==1)
                            {
                                tn = tn.getNodeByCode(colvalue);
                                if (tn != null)
                                {
                                    String strDic = tn.getDicValue();
            						if(dicTreatFlag)
            						{
            							Element dicelem = dicsRoot.addElement("DIC");
            							dicelem.addAttribute("fname", colname);
            							dicelem.addAttribute("dicname", dic);
            						}
                                    fieldNode.addAttribute("sv", strDic);
                                }
                            }
                            else
                            {
                                String sv="";
                                TreeNode temp;
                                for(int m=0;m<dics.length;m++)
                                {
                                    temp = tn.getNodeByCode(dics[m]);
                                    if (temp != null)
                                    {
                                        sv=sv+temp.getDicValue();
                                    }
                                    if(m<dics.length-1)sv+=",";

                               }
        						if(dicTreatFlag)
        						{
        							Element dicelem = dicsRoot.addElement("DIC");
        							dicelem.addAttribute("fname", colname);
        							dicelem.addAttribute("dicname", dic);
        						}
                                fieldNode.addAttribute("sv", sv);
                            }
                        }
                    }

                    if (hashtab.get(colname + EXT_ORG_DEPT) != null)
                    {
                        OrgDept dept = OrgDeptManager.getInstance().getDeptByID(
                            colvalue) == null ? null :
                            OrgDeptManager.getInstance().getDeptByID(colvalue);
                        String strDic = dept == null ? colvalue :
                            dept.getDeptName();
						if(dicTreatFlag)
						{
							Element dicelem = dicsRoot.addElement("DIC");
							dicelem.addAttribute("fname", colname);
							dicelem.addAttribute("dicname", "ZZJG");
						}

                        fieldNode.addAttribute("sv", strDic);
                    }
                    if (hashtab.get(colname + EXT_TRANSLATER) != null)
                    {
                        String tableName=(String)hashtab.get(colname + EXT_TABLENAME);
                        String codeField=(String)hashtab.get(colname + EXT_CODEFIELD);
                        String valueField=(String)hashtab.get(colname + EXT_VALUEFIELD);
                        Translater translater=new Translater(tableName,codeField,valueField);                        
                        fieldNode.addAttribute("sv", translater.translate(colvalue));                                               
                    }
                    if (hashtab.get(colname + EXT_DEPT_SMP) != null)
                    {
                        OrgDept dept = OrgDeptManager.getInstance().getDeptByID(
                            colvalue) == null ? null :
                            (OrgDept) OrgDeptManager.
                            getInstance().getDeptByID(colvalue);
                        String strDic = dept == null ? colvalue : dept.getBmjc();
                        fieldNode.addAttribute("sv", strDic);
                    }
                    if (hashtab.get(colname + EXT_USER_ID) != null)
                    {
                        if(colvalue==null)colvalue="";
                        String[] dics  = colvalue.split(",");
                        if(dics.length==1)
                        {
                            String strDic = "";
                            if (colvalue != null)
                                strDic = UserManager.getInstance().getUserByLoginNameFromNc(colvalue) == null ? colvalue :
                                    UserManager.getInstance().getUserByLoginNameFromNc(colvalue).getName();
    						if(dicTreatFlag)
    						{
    							Element dicelem = dicsRoot.addElement("DIC");
    							dicelem.addAttribute("fname", colname);
    							dicelem.addAttribute("dicname", "USERID");
    						}
                            fieldNode.addAttribute("sv", strDic);
                        }
                        else
                        {
                            String sv="";
                            String  temp;
                            for(int m=0;m<dics.length;m++)
                            {
                                temp = UserManager.getInstance().getUserByLoginNameFromNc(dics[m]) == null ? dics[m] :
                                    UserManager.getInstance().getUserByLoginNameFromNc(dics[m]).getName();
                                if (temp != null)
                                {
                                    sv=sv+temp;
                                }
                                if(m<dics.length-1)sv+=",";

                           }
    						if(dicTreatFlag)
    						{
    							Element dicelem = dicsRoot.addElement("DIC");
    							dicelem.addAttribute("fname", colname);
    							dicelem.addAttribute("dicname", "USERID");
    						}
                            fieldNode.addAttribute("sv", sv);
                        }

                    }
                    if (hashtab.get(colname + EXT_ORG_ROLE) != null)
                    {
                        String strDic = "";
                        if (colvalue != null)
                            strDic = OrgRoleManager.getInstance().getRole(
                                colvalue) == null ? colvalue :
                                OrgRoleManager.getInstance().getRole(colvalue).
                                getMemo();
                        fieldNode.addAttribute("sv", strDic);
                    }
                    if (hashtab.get(colname + EXT_CUSTOM) != null)
                    {
                        String tableinfo = (String)hashtab.get(colname + EXT_CUSTOM);
                        String tableName=null;
                        String titleColumn=null;
                        String codeColumn = null;
                        if(tableinfo != null && (tableinfo.split("@").length==2 || tableinfo.split("@").length==3))
                        {
                            tableName = tableinfo.split("@")[0];
                            titleColumn = tableinfo.split("@")[1];
                            if (tableinfo.split("@").length==3 )
                                codeColumn = tableinfo.split("@")[2];
                            else
                                codeColumn = colname;
                        }
                        if (colvalue != null&&tableName !=null&&titleColumn !=null)
                        {
                            String sql =
                                "select "+titleColumn+" from "+tableName+"  where "+codeColumn+"=?";
                            Object[] paras =
                                {
                                colvalue};
                            String[][] list = DBUtil.querySql(sql, paras);
                            String sv = list == null ? colvalue : list[0][0];
                            fieldNode.addAttribute("sv", sv);
                        }
                    }
                }
				if(dicTreatFlag)
				{
					dicTreatFlag = false;
					BASE64Encoder be = new BASE64Encoder();
					confInfoRoot.addText(be.encode(domconf.asXML().getBytes()));
				}            
            }
            this.doc = domresult;
            return this.doc;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                if (this.resultset != null)
                    this.resultset.close();
                if (this.ps != null)
                    this.ps.close();
                this.resultset = null;
                this.ps = null;
            }
            catch (Exception ee)
            {
                ee.printStackTrace();
            }
        }
        return null;
    }

    private String getDicName(String fieldname)
    {
        try
        {
            return hashtab.get(fieldname.toUpperCase()) == null ? null :
                (String) hashtab.get(fieldname.toUpperCase());
        }
        catch (Exception e)
        {
            return null;
        }
    }
    public Document getDocumentWithoutNullValue()
    {
        if (this.doc != null)
            return this.doc;
        try
        {
            Document domresult = DocumentFactory.getInstance().createDocument();
            Element root = domresult.addElement("RESPONSE");
            Element resultRoot = root.addElement("RESULT");
            if (page != null)
            {
                resultRoot.addAttribute("currentpagenum",
                                        String.valueOf(page.getCurrentPage()));
                resultRoot.addAttribute("recordsperpage",
                                        String.valueOf(page.getPageRows()));
                resultRoot.addAttribute("totalpage",
                                        String.valueOf(page.getCountPage()));
                resultRoot.addAttribute("countrows",
                                        String.valueOf(page.getCountRows()));
            }
            if (this.infotab != null)
            {
                Element info = root; //root.addElement("INFO");
                Enumeration enumer = infotab.keys();
                while (enumer.hasMoreElements())
                {
                    String key = (String) enumer.nextElement();
                    info.addElement(key).setText( (String) infotab.get(key));
                }
            }

            while (resultset.next())
            {
                Element recordItem = resultRoot.addElement("ROW");
                for (int i = 1; i <= rsmd.getColumnCount(); i++)
                {
                    String colname = rsmd.getColumnName(i).toUpperCase();
                    int coltype = rsmd.getColumnType(i);
                    String colvalue = "";
                    String svValue = null;
                    if (coltype == java.sql.Types.DATE ||
                        coltype == java.sql.Types.TIMESTAMP)
                    {
                        String format = hashtab.get(colname + EXT_DATE_FMT) == null ?
                            "yyyyMMdd" :
                            (String) hashtab.get(colname + EXT_DATE_FMT);
                        Timestamp d = resultset.getTimestamp(i);
                        if (d != null)
                        {
                            svValue = DateTimeUtil.getDateTime(d, format);
                            colvalue = Pub.getDate("yyyyMMddHHmmss", d);
                        }
                        else
                        {
                            colvalue = resultset.getString(i);
                            svValue = colvalue;
                        }
                    }
                    else if (coltype == java.sql.Types.BLOB)
                    {
                        Blob dbBlob;
                        dbBlob = resultset.getBlob(i);
                        if (dbBlob != null)
                        {
                            int length = (int) dbBlob.length();
                            byte[] buffer = dbBlob.getBytes(1, length);
                            if(hashtab.get(colname+EXT_CLOB)!=null){
                                colvalue = new String(buffer,"GBK");
                            }else{
                                colvalue = Pub.toBase64(buffer);
                            }
                        }
                    }
                    else
                    {
                        colvalue = resultset.getString(i);
                    }



                    if(hashtab.get(colname+EXT_SJBH)!=null){
                        if(!Pub.empty(colvalue)){
                            EventVO eventvo = EventManager.getEventByID(this.ps.getConnection(),colvalue);
                            Element fieldNode = recordItem.addElement("EVENTSJZT");
                            if(eventvo==null)
                            {
                                 fieldNode.addAttribute("sv", "-1");
                                 continue;
                            }
                            fieldNode.addText(eventvo.getSjzt());
                            TreeNode tn = Dics.getDicByName("SJZT");
                            if (tn != null)
                            {
                                tn = tn.getNodeByCode(eventvo.getSjzt());
                                if (tn != null)
                                {
                                    String strDic = tn.getDicValue();
                                    fieldNode.addAttribute("sv", strDic);
                                }
                            }
                        }
                    }
                    /**
                    if(hashtab.get(colname+EXT_WRIT)!=null){
                        if(!Pub.empty(colvalue)){
                            String count = Pub.getWritCount(colvalue,this.ps.getConnection());
                            Element fieldNode = recordItem.addElement("WRITCOUNT");
                            fieldNode.addText(count);
                        }
                    }
                    **/
                    
                    if(Pub.empty(colvalue))
                    	continue;
                    Element fieldNode = recordItem.addElement(colname);
                    if (colvalue != null)
                    {	
                        fieldNode.addText(colvalue);
                        int n=0;
                        if(colvalue.startsWith(" "))
                        {
                        	n++;
                        	for(int j=1;j<colvalue.length();j++)
                        	{
                        		if(" ".equals(String.valueOf(colvalue.charAt(j))))
                        			n++;
                        		else
                        			break;
                        	}
                        }
                        if(n>0)
                        {	
                           fieldNode.addAttribute("spaces", String.valueOf(n));
                        }
                    }    
                    if (svValue != null)
                        fieldNode.addAttribute("sv", svValue);
                    String dic = getDicName(colname);
                    if (!Pub.empty(dic))
                    {
                        TreeNode tn = Dics.getDicByName(dic);

                        if (tn != null)
                        {
                            if(colvalue==null)colvalue="";
                            String[] dics  = colvalue.split(",");
                            if(dics.length==1)
                            {
                                tn = tn.getNodeByCode(colvalue);
                                if (tn != null)
                                {
                                    String strDic = tn.getDicValue();
                                    fieldNode.addAttribute("sv", strDic);
                                }
                            }
                            else
                            {
                                String sv="";
                                TreeNode temp;
                                for(int m=0;m<dics.length;m++)
                                {
                                    temp = tn.getNodeByCode(dics[m]);
                                    if (temp != null)
                                    {
                                        sv=sv+temp.getDicValue();
                                    }
                                    if(m<dics.length-1)sv+=",";

                               }
                                fieldNode.addAttribute("sv", sv);
                            }
                        }
                    }

                    if (hashtab.get(colname + EXT_ORG_DEPT) != null)
                    {
                        OrgDept dept = OrgDeptManager.getInstance().getDeptByID(
                            colvalue) == null ? null :
                            OrgDeptManager.getInstance().getDeptByID(colvalue);
                        String strDic = dept == null ? colvalue :
                            dept.getDeptName();
                        fieldNode.addAttribute("sv", strDic);
                    }
                    if (hashtab.get(colname + EXT_DEPT_SMP) != null)
                    {
                        OrgDept dept = OrgDeptManager.getInstance().getDeptByID(
                            colvalue) == null ? null :
                            (OrgDept) OrgDeptManager.
                            getInstance().getDeptByID(colvalue);
                        String strDic = dept == null ? colvalue : dept.getBmjc();
                        fieldNode.addAttribute("sv", strDic);
                    }
                    if (hashtab.get(colname + EXT_USER_ID) != null)
                    {
                        if(colvalue==null)colvalue="";
                        String[] dics  = colvalue.split(",");
                        if(dics.length==1)
                        {
                            String strDic = "";
                            if (colvalue != null)
                                strDic = UserManager.getInstance().getUserByLoginNameFromNc(colvalue) == null ? colvalue :
                                    UserManager.getInstance().getUserByLoginNameFromNc(colvalue).getName();
                            fieldNode.addAttribute("sv", strDic);
                        }
                        else
                        {
                            String sv="";
                            String  temp;
                            for(int m=0;m<dics.length;m++)
                            {
                                temp = UserManager.getInstance().getUserByLoginNameFromNc(dics[m]) == null ? dics[m] :
                                    UserManager.getInstance().getUserByLoginNameFromNc(dics[m]).getName();
                                if (temp != null)
                                {
                                    sv=sv+temp;
                                }
                                if(m<dics.length-1)sv+=",";

                           }
                            fieldNode.addAttribute("sv", sv);
                        }

                    }
                    if (hashtab.get(colname + EXT_ORG_ROLE) != null)
                    {
                        String strDic = "";
                        if (colvalue != null)
                            strDic = OrgRoleManager.getInstance().getRole(
                                colvalue) == null ? colvalue :
                                OrgRoleManager.getInstance().getRole(colvalue).
                                getMemo();
                        fieldNode.addAttribute("sv", strDic);
                    }
                    if (hashtab.get(colname + EXT_CUSTOM) != null)
                    {
                        String tableinfo = (String)hashtab.get(colname + EXT_CUSTOM);
                        String tableName=null;
                        String titleColumn=null;
                        String codeColumn = null;
                        if(tableinfo != null && (tableinfo.split("@").length==2 || tableinfo.split("@").length==3))
                        {
                            tableName = tableinfo.split("@")[0];
                            titleColumn = tableinfo.split("@")[1];
                            if (tableinfo.split("@").length==3 )
                                codeColumn = tableinfo.split("@")[2];
                            else
                                codeColumn = colname;
                        }
                        if (colvalue != null&&tableName !=null&&titleColumn !=null)
                        {
                            String sql =
                                "select "+titleColumn+" from "+tableName+"  where "+codeColumn+"=?";
                            Object[] paras =
                                {
                                colvalue};
                            String[][] list = DBUtil.querySql(sql, paras);
                            String sv = list == null ? colvalue : list[0][0];
                            fieldNode.addAttribute("sv", sv);
                        }
                    }
                }
            }
            this.doc = domresult;
            return this.doc;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                if (this.resultset != null)
                    this.resultset.close();
                if (this.ps != null)
                    this.ps.close();
                this.resultset = null;
                this.ps = null;
            }
            catch (Exception ee)
            {
                ee.printStackTrace();
            }
        }
        return null;
    }
    public void setOriginalSQL(String originalSQL)
    {
		originalSQL = "select * from ("+originalSQL+") where rownum<="+"60000";
        this.originalSQL = originalSQL;
    }

}