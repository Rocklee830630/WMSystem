package com.ccthanking.common;

import com.ccthanking.framework.common.*;
import java.sql.Connection;
import java.sql.*;
import com.ccthanking.common.vo.*;
import com.ccthanking.framework.base.*;
import java.util.ArrayList;
import com.ccthanking.framework.wsdy.*;
import java.io.*;
import org.dom4j.io.*;
import org.dom4j.*;
import java.util.List;

/**
 * <p>Title: jwzh</p>
 * <p>Description: </p>
 */

public class EditWsColumn
{
    public EditWsColumn()
    {
    }

    /*conn 连接串
      sjbh 事件编号
      ywlx 业务类型
      templateid 文书id
      columnname 要替换的域名数组
      value 替换值数组
     */
    public static boolean EditColumn(Connection conn, String sjbh, String ywlx,
                                     String templateid, ArrayList ColumnName,
                                     ArrayList value)
        throws Exception
    {
        //拷贝志海文书方法

        Document doc = null;
        SAXReader reader = new SAXReader();
        ByteArrayInputStream in = null;
        InputStreamReader isr = null;
        BlobUtil blobUtil = new BlobUtil();
        try
        {
            String pub_blob_sql =
                "select MULTIMEDIA from PUB_BLOB where  WS_TEMPLATE_ID ='" +
                templateid + "' and SJBH='" + sjbh + "' and YWLX='" + ywlx +
                "' and (ZFBS = '0' or ZFBS IS NULL) order by tbsj desc";
            byte[] xmlBlob = null;
            xmlBlob = blobUtil.get("blob", pub_blob_sql);
            if (xmlBlob == null)
            {
                throw new Exception("无法获得BLOB数据");
            }

            in = new ByteArrayInputStream(xmlBlob);
            isr = new InputStreamReader(in, "UTF-8");
            doc = reader.read(isr);
            List printRoot = doc.selectNodes("//PRINTDATA/ROW");
            if (printRoot == null)
            {
                return false;
            }

            for (int i = 0; i < ColumnName.size(); i++)
            {

                for (int j = 0; j < printRoot.size(); j++)
                {

                    Element ele = (Element) printRoot.get(j);
                    if (ele.getNodeType() != Node.ELEMENT_NODE)
                    {
                        continue;
                    }
                    String fieldname = ele.attributeValue("fieldname");
                    if ( ( (String) ColumnName.get(i)).equalsIgnoreCase(
                        fieldname))
                    {
                        ele.setText(ele.getText() + (String) value.get(i));
                        //modified by andy 20080403 去掉break,更新时将文书中相同节点全部更新
                        //break;
                    }
                }

            }
        }
        catch (DocumentException ex)
        {
            ex.printStackTrace();
            return false;
        }

        finally
        {
            if (in != null)
            {
                in.close();
            }
            if (isr != null)
            {
                isr.close();

            }
        }
        String xmlString = doc.asXML();
        byte[] b = xmlString.getBytes("UTF-8");
        try
        {
            conn.setAutoCommit(false);
            String s =
                "update pub_blob set MULTIMEDIA=EMPTY_BLOB() WHERE WS_TEMPLATE_ID ='" +
                templateid + "' and SJBH='" +
                sjbh + "' and YWLX='" + ywlx +
                "' and (ZFBS = '0' or ZFBS IS NULL) ";
            DBUtil.exec(conn, s);
            String sql =
                "select MULTIMEDIA from pub_blob WHERE WS_TEMPLATE_ID ='" +
                templateid + "' and SJBH='" +
                sjbh + "' and YWLX='" + ywlx +
                "' and (ZFBS = '0' or ZFBS IS NULL) order by tbsj desc for update ";
            blobUtil.updateBlob(conn, sql, b);
            //conn.commit();
        }
        catch (Exception e)
        {
            // conn.rollback();
            e.printStackTrace();
            return false;
        }
        finally
        {
            // if (conn != null)
            //     conn.close();
            // conn = null;
        }

        return true;
    }

    /*conn 连接串
      sjbh 事件编号
      ywlx 业务类型
      templateid 文书id
      columnname 要替换的域名数组
      value 替换值数组
     */
    public static boolean OverColumn(Connection conn, String sjbh, String ywlx,
                                     String templateid, ArrayList ColumnName,
                                     ArrayList value)
        throws Exception
    {
        //拷贝志海文书方法

        Document doc = null;
        SAXReader reader = new SAXReader();
        ByteArrayInputStream in = null;
        InputStreamReader isr = null;
        BlobUtil blobUtil = new BlobUtil();
        try
        {
            String pub_blob_sql =
                "select MULTIMEDIA from PUB_BLOB where  WS_TEMPLATE_ID ='" +
                templateid + "' and SJBH='" + sjbh + "' and YWLX='" + ywlx +
                "' and (ZFBS = '0' or ZFBS IS NULL) order by tbsj desc";
            byte[] xmlBlob = null;
            xmlBlob = blobUtil.get("blob", pub_blob_sql);
            if (xmlBlob == null)
            {
                throw new Exception("无法获得BLOB数据");
            }

            in = new ByteArrayInputStream(xmlBlob);
            isr = new InputStreamReader(in, "UTF-8");
            doc = reader.read(isr);
            List printRoot = doc.selectNodes("//PRINTDATA/ROW");
            if (printRoot == null)
            {
                return false;
            }

            for (int i = 0; i < ColumnName.size(); i++)
            {

                for (int j = 0; j < printRoot.size(); j++)
                {

                    Element ele = (Element) printRoot.get(j);
                    if (ele.getNodeType() != Node.ELEMENT_NODE)
                    {
                        continue;
                    }
                    String fieldname = ele.attributeValue("fieldname");
                    if ( ( (String) ColumnName.get(i)).equalsIgnoreCase(
                        fieldname))
                    {
                        //ele.setText(ele.getText() + (String) value.get(i));
                        ele.setText((String) value.get(i));
                        //break;
                    }
                }

            }
        }
        catch (DocumentException ex)
        {
            ex.printStackTrace();
            return false;
        }

        finally
        {
            if (in != null)
            {
                in.close();
            }
            if (isr != null)
            {
                isr.close();

            }
        }
        String xmlString = doc.asXML();
        byte[] b = xmlString.getBytes("UTF-8");
        try
        {
            conn.setAutoCommit(false);
            String s =
                "update pub_blob set MULTIMEDIA=EMPTY_BLOB() WHERE WS_TEMPLATE_ID ='" +
                templateid + "' and SJBH='" +
                sjbh + "' and YWLX='" + ywlx +
                "' and (ZFBS = '0' or ZFBS IS NULL) ";
            DBUtil.exec(conn, s);
            String sql =
                "select MULTIMEDIA from pub_blob WHERE WS_TEMPLATE_ID ='" +
                templateid + "' and SJBH='" +
                sjbh + "' and YWLX='" + ywlx +
                "' and (ZFBS = '0' or ZFBS IS NULL) order by tbsj desc for update ";
            blobUtil.updateBlob(conn, sql, b);
            //conn.commit();
        }
        catch (Exception e)
        {
            // conn.rollback();
            e.printStackTrace();
            return false;
        }
        finally
        {
            // if (conn != null)
            //     conn.close();
            // conn = null;
        }

        return true;
    }

}