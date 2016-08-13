package com.ccthanking.framework.wsdy;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.FileOutputStream;
import com.ccthanking.framework.base.BaseDispatchAction;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import java.io.*;
import java.sql.*;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.QuerySet;
import org.dom4j.Document;
import org.dom4j.*;
import org.dom4j.io.*;
import com.ccthanking.framework.dic.Dics;
import com.ccthanking.framework.wsdy.pub_blob.*;
import com.ccthanking.framework.util.*;
import com.ccthanking.framework.wsdy.doc_sequence.WswhSequence;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.common.OPException;
import java.util.List;

import com.ccthanking.framework.wsdy.electron_print.*;
import com.ccthanking.framework.Globals;
import com.ccthanking.framework.common.OrgDept;
import java.util.HashMap;
import java.util.Hashtable;

public class UpdateWstemplate
{

    public static String TABLENAME = "ws_template";
    public UpdateWstemplate(){

    }
    public String update(String ws_template_id) throws Exception
    {
        Connection conn = null;
        java.sql.ResultSet rs = null;
        Statement st = null;
        StringBuffer buff=new StringBuffer(); //缓冲字符串，保存一个文书信息。
        String str = "";
        String id = "";
        boolean flag = false;
        int Total = 0;  //记录文书总数
        java.util.Date begin = null;  //开始时间
        java.util.Date end = null;    //结束时间
        ByteArrayInputStream byteInputStream=null;
        BufferedReader reader =   null;
        BlobUtil blobUtil = new BlobUtil();
        String sql_cx = "select WS_TEMPLATE_ID from " + TABLENAME +" "+(ws_template_id.length() > 0?"where ws_template_id in ("+ws_template_id+")":"");
        try
        {
            conn = DBUtil.getConnection();
            st = conn.createStatement();
            rs = st.executeQuery(sql_cx); //查询所有文书id
            begin = Pub.getCurrentDate();  //记录开始时间
            while (rs.next())  //循环读取每个文书的ws_template_id
            {

                id = rs.getString(1);
                String ws_template_sql = "select WS_TEMPLATE from " + TABLENAME +" where WS_TEMPLATE_ID='" + id + "'";
                byte[] templateBlob = blobUtil.get("blob",ws_template_sql);//文书模版blob
                byteInputStream = new ByteArrayInputStream(templateBlob);
                reader = new BufferedReader(new InputStreamReader(byteInputStream));
                str = reader.readLine();
                while(reader.ready()){
                  if(str.indexOf("<html xmlns:v=3D\"urn:schemas-microsoft-com:vml\"")>=0){
                      continue;
                  }
                  if(str.indexOf("<html xmlns:o=3D\"urn:schemas-microsoft-com:office:office\"")>=0){
                          str = str.replaceAll("<html xmlns:o=3D\"urn:schemas-microsoft-com:office:office\"",
                                               "<html xmlns:v=3D\"urn:schemas-microsoft-com:vml\" \r\nxmlns:o=3D\"urn:schemas-microsoft-com:office:office\" ");
                          flag = true;
                  }
                  if(str.indexOf("<link rel=3DOLE-Object-Data href=3D\"oletmp.files/oledata.mso\">")>=0){
                      if(flag){
                          str += "\r\n" + "<!--[if !mso]>\r\n" +
                              "<style>\r\n" +
                              "v" + "\\" + ":* {behavior:url(#default#VML);}\r\n" +
                              "o" + "\\" + ":* {behavior:url(#default#VML);}\r\n" +
                              "x" + "\\" + ":* {behavior:url(#default#VML);}\r\n" +
                              ".shape {behavior:url(#default#VML);}\r\n" +
                              "</style>\r\n" +
                              "<![endif]-->";
                      }

                  }
                  if(str.indexOf("<link rel=3DEdit-Time-Data href=3Deditdata.mso>")>=0){
                      if(flag){
                          str += "\r\n" + "<!--[if !mso]>\r\n" +
                              "<style>\r\n" +
                              "v" + "\\" + ":* {behavior:url(#default#VML);}\r\n" +
                              "o" + "\\" + ":* {behavior:url(#default#VML);}\r\n" +
                              "x" + "\\" + ":* {behavior:url(#default#VML);}\r\n" +
                              ".shape {behavior:url(#default#VML);}\r\n" +
                              "</style>\r\n" +
                              "<![endif]-->";
                      }
                  }
                  buff.append(str+"\r\n");
                  str = reader.readLine();

                }
                String s = "update " + TABLENAME +
                    " set WS_TEMPLATE=EMPTY_BLOB() WHERE WS_TEMPLATE_ID='" + id +
                    "'";
                DBUtil.exec(conn, s);
                String sql = "select WS_TEMPLATE from " + TABLENAME +
                    " WHERE WS_TEMPLATE_ID='" + id + "'  for update ";
                blobUtil.updateBlob(conn, sql, buff.toString().getBytes());
                conn.commit();
                buff=new StringBuffer();
                Total++;
            }
            end = Pub.getCurrentDate();
          byteInputStream.close();
          reader.close();
        }catch(Exception e){

            e.printStackTrace();
            return "处理文书失败 ws_template_id = '"+id+"'";
        }finally{
            if(conn!=null){
                conn.close();
            }
            if(st != null){
                st.close();
            }
            if(rs!=null){
                rs.close();
            }
        }
        return "执行成功！\n共处理文书："+Total+"个";
    }

}