package com.ccthanking.framework.util;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import sun.misc.BASE64Decoder;

import com.ccthanking.framework.Globals;
import com.ccthanking.framework.common.BaseResultSet;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.PageManager;

public class TxtUtil
{
    /**
     * @param tlii	表头及对应字段信息的xml字符串
     * @param doc	查询结果document对应的xml字符串
     * @return		返回html页面代码串
     */
    public static String getOutPutString(Document doc)
    {
        return  getBodyStr(doc);
    }

    /**
     * @param tlii		表头及对应字段信息的xml字符串
     * @param doc		查询结果document对应的xml字符串
     * @param response
     * @param fileName	输出的Excel名称
     */
    public static void output(Document doc, HttpServletResponse response,
                              String fileName, String titles)
    {
        OutputStream os = null;
        try
        {
            response.setHeader("content-disposition",
                               "attachment;filename=" +
                               java.net.URLEncoder.encode(fileName, "GBK") +
                               ".txt");
            response.setContentType("text/plain;");
            response.setCharacterEncoding("GBK");
            os = response.getOutputStream();
            os.write(getOutPutString(doc).getBytes("GBK"));
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally
        {
            try
            {
                if (os != null)
                {
                    os.flush();
                    os.close();
                }
            }
            catch (IOException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public static void output(Document doc, HttpServletResponse response,
                              String fileName)
    {
        OutputStream os = null;
        try
        {
            response.setHeader("content-disposition",
                               "attachment;filename=" +
                               java.net.URLEncoder.encode(fileName, "GBK") +
                               ".txt");
            response.setContentType("text/plain;");
            response.setCharacterEncoding("GBK");
            os = response.getOutputStream();
            os.write(getOutPutString(doc).getBytes("GBK"));
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally
        {
            try
            {
                if (os != null)
                {
                    os.flush();
                    os.close();
                }
            }
            catch (IOException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

	public static void outputAll(Document doc, HttpServletResponse response, String fileName)
	{
        OutputStream os = null;
		try {
			response.setHeader("content-disposition","attachment;filename="+java.net.URLEncoder.encode(fileName, "GBK") + ".txt");
	        response.setContentType("text/plain;");
			response.setCharacterEncoding("GBK");
			os = response.getOutputStream();
			os.write((getBodyStrAll(doc)).getBytes("GBK"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
		    try {
		    	if(os!=null)
		    	{
					os.flush();
			    	os.close();
		    	}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	


    private static String getBodyStr(Document doc)
    {
        StringBuffer strBuffer = new StringBuffer();
        try
        {
           
            StringBuffer strTitle = new StringBuffer();
            StringBuffer strData = new StringBuffer();
            List list = doc.selectNodes("/DATA/HEADER/CELL");
            for (int i = 0; i < list.size(); i++)
            {
                Element ele = (Element) list.get(i);
                strTitle.append( ele.getText() + "|");
            }
            strTitle.append("\r\n");

            List dataList = doc.selectNodes("/DATA/ROWS/ROW");
            for (int i = 0; i < dataList.size(); i++)
            {
                Element ele = (Element) dataList.get(i);
               
                List cells = ele.elements("CELL");
                for (int j = 0; j < cells.size(); j++)
                {
                    strData.append(((Element) cells.get(j)).getText() +"|");
                }
                strData.append("\r\n");
            }
            strBuffer.append(strTitle).append(strData);
            return strBuffer.toString();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

	private static String getBodyStrAll(Document docXml)
	{
		StringBuffer strBuffer = new StringBuffer();
		try {
			
			StringBuffer strTitle = new StringBuffer();
			StringBuffer strData = new StringBuffer();
			//取Excel表头信息
			List list = docXml.selectNodes("//QUERYCONDITION/TITLEINFO/FIELDINFO");
			String[] fieldNames = new String[list.size()];
			for(int i=0; i<list.size(); i++)
			{
				Element ele = (Element)list.get(i);
				strTitle.append(ele.elementText("FIELDTITLE")+"|");
				fieldNames[i] = ele.elementText("FIELDNAME");
			}
			strTitle.append("\r\n");
			
			Document doc = getQueryData(docXml);
			
			List dataList = doc.selectNodes("//RESPONSE/RESULT/ROW");
			for(int i=0; i<dataList.size(); i++)
			{
				Element ele = (Element)dataList.get(i);
				for(int j=0; j<fieldNames.length; j++)
				{
					Element field = ele.element(fieldNames[j]);
					String value = null;
					if(field!=null)
						value = (field.attribute("sv")==null || field.attribute("sv").getText().equals(""))?field.getText():field.attribute("sv").getText();
					else
						value = "";
					strData.append(value+"|");
				}
				strData.append("\r\n");
			}
			strBuffer.append(strTitle).append(strData);
			return strBuffer.toString();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}
	
	public static Document getQueryData(Document docXml)
	{
		Document docData = null;
		String sql = null;
		List listDic = null;
		Connection conn = DBUtil.getConnection("p3");
		Element ele = (Element)docXml.selectSingleNode("//QUERYCONDITION/CONF");

		BASE64Decoder de = new BASE64Decoder();
		try {
			String conf = new String(de.decodeBuffer(ele.getText()),"GBK");
			Document docConf = DocumentHelper.parseText(conf);
			listDic = docConf.selectNodes("//CONF/DICS/DIC");
			sql = docConf.selectSingleNode("//CONF/SQL").getText();
			System.out.println(docConf.asXML());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}

		PageManager page = new PageManager();
		page.setPageRows(Globals.MAX_RECORD_LIMITED);
		BaseResultSet bs = DBUtil.query(conn, sql, page);
		for(int i=0; i<listDic.size(); i++)
		{
			Element e = (Element)listDic.get(i);
			bs.setFieldDic(e.attributeValue("fname"), e.attributeValue("dicname"));
		}
		
		docData = bs.getDocument();
		return docData;
	}
	
    private static String getTailStr()
    {
        return "</body></html>";
    }
}
