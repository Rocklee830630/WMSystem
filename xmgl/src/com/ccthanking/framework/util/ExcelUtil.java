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

/**
 * description	:	针对TabList控件查询的结果输出Excel，与前台的tablistInstanceInfo.js配合使用
 * version	:		1.0
 * author	:		liu.ld@neusoft.com
 * date		:		2009-12-24
 */
public class ExcelUtil
{
    /**
     * @param tlii	表头及对应字段信息的xml字符串
     * @param doc	查询结果document对应的xml字符串
     * @return		返回html页面代码串
     */
    public static String getOutPutString(Document doc)
    {
        return getHeaderStr() + getBodyStr(doc) + getTailStr();
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
                               java.net.URLEncoder.encode(fileName, "UTF-8") +
                               ".xls");
            response.setContentType("application/x-msdownload;");
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
                               java.net.URLEncoder.encode(fileName, "UTF-8") +
                               ".xls");
            response.setContentType("application/x-msdownload;");
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
			response.setHeader("content-disposition","attachment;filename="+java.net.URLEncoder.encode(fileName, "UTF-8") + ".xls");
	        response.setContentType("application/x-msdownload;");
			response.setCharacterEncoding("GBK");
			os = response.getOutputStream();
			os.write((getHeaderStr()+getBodyStrAll(doc)+getTailStr()).getBytes("GBK"));
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
	
    private static String getHeaderStr()
    {
        StringBuffer strBuffer = new StringBuffer();
        strBuffer.append("<html>");
        strBuffer.append("<head>");
        strBuffer.append(
            "<meta http-equiv=\"Content-Type\" content=\"text/html;charset=GBK\">");
        strBuffer.append("<meta name=ProgId content=Excel.Sheet>");
        strBuffer.append("<style>");
        strBuffer.append(".x2{");
        strBuffer.append(" font-family:宋体;");
        strBuffer.append(" font-size:10.5pt;");
        strBuffer.append(" text-align:center;");
        strBuffer.append("}");
        strBuffer.append(".x3{");
        strBuffer.append(" font-family:宋体;");
        strBuffer.append(" font-size:9.0pt;");
        strBuffer.append("}");
        strBuffer.append("</style>");
        strBuffer.append("</head>");
        strBuffer.append("<body leftmargin='150' topmargin='150'>");

        return strBuffer.toString();
    }

    private static String getBodyStr(Document doc)
    {
        StringBuffer strBuffer = new StringBuffer();
        try
        {
            strBuffer.append("<table align='center' border='1' cellpadding='0' cellspacing='0' width='100%' >");
            StringBuffer strTitle = new StringBuffer("<tr>");
            StringBuffer strData = new StringBuffer();
            List list = doc.selectNodes("/DATA/HEADER/CELL");
            for (int i = 0; i < list.size(); i++)
            {
                Element ele = (Element) list.get(i);
                strTitle.append("<th class='x2'>" + ele.getText() + "</th>");
            }
            strTitle.append("</tr>");

            List dataList = doc.selectNodes("/DATA/ROWS/ROW");
            for (int i = 0; i < dataList.size(); i++)
            {
                Element ele = (Element) dataList.get(i);
                strData.append("<tr>");
                List cells = ele.elements("CELL");
                for (int j = 0; j < cells.size(); j++)
                {
                    strData.append("<td class='x3'>" +
                                   ((Element) cells.get(j)).getText() +
                                   "&nbsp;</td>");
                }
                strData.append("</tr>");
            }
            strBuffer.append(strTitle).append(strData).append("</table>");
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
			strBuffer.append("<table align='center' border='1' cellpadding='0' cellspacing='0' width='100%' >");
			StringBuffer strTitle = new StringBuffer("<tr>");
			StringBuffer strData = new StringBuffer();
			//取Excel表头信息
			List list = docXml.selectNodes("//QUERYCONDITION/TITLEINFO/FIELDINFO");
			String[] fieldNames = new String[list.size()];
			for(int i=0; i<list.size(); i++)
			{
				Element ele = (Element)list.get(i);
				strTitle.append("<th class='x2'>"+ele.elementText("FIELDTITLE")+"</th>");
				fieldNames[i] = ele.elementText("FIELDNAME");
			}
			strTitle.append("</tr>");
			
			Document doc = getQueryData(docXml);
			
			List dataList = doc.selectNodes("//RESPONSE/RESULT/ROW");
			for(int i=0; i<dataList.size(); i++)
			{
				Element ele = (Element)dataList.get(i);
				strData.append("<tr>");
				for(int j=0; j<fieldNames.length; j++)
				{
					Element field = ele.element(fieldNames[j]);
					String value = null;
					if(field!=null)
						value = (field.attribute("sv")==null || field.attribute("sv").getText().equals(""))?field.getText():field.attribute("sv").getText();
					else
						value = "";
					strData.append("<td class='x3'>"+value+"&nbsp;</td>");
				}
				strData.append("</tr>");
			}
			strBuffer.append(strTitle).append(strData).append("</table>");
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
