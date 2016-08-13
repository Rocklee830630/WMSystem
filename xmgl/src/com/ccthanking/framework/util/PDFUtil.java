package com.ccthanking.framework.util;

import java.io.*;
import java.sql.Connection;
import java.util.*;
import java.awt.Color;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;

import sun.misc.BASE64Decoder;

import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.Font;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.Phrase;
import com.lowagie.text.Element;
import com.lowagie.text.Table;
import com.lowagie.text.Rectangle;
import com.lowagie.text.Cell;
import com.ccthanking.framework.Globals;
import com.ccthanking.framework.common.BaseResultSet;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.PageManager;

/**
 * <p>Title: UniMis -- oss -- You And Me</p>
 *
 * <p>Description: Universal Framework For Mis Development</p>
 *
 * <p>Copyright: Copyright (c) 2009</p>
 *
 * <p>Company: oss Co.,Ltd ChangChun branch</p>
 *
 * @author leo / Leo.Chou / oss@tom.com
 * @version 1.0
 */
public class PDFUtil
{
    public PDFUtil()
    {
    }

    public static void output(org.dom4j.Document doc, HttpServletResponse response,
                              String fileName)
    {
        OutputStream os = null;
        if(doc == null) return;
        com.lowagie.text.Document document = null;
        try
        {
            response.setHeader("content-disposition",
                               "attachment;filename=" +
                               java.net.URLEncoder.encode(fileName, "UTF-8") +
                               ".pdf");
            response.setContentType("application/x-msdownload;");
            response.setCharacterEncoding("GBK");
            os = response.getOutputStream();
            document = new com.lowagie.text.Document();
            //DocListener doclis = new DocListener();
            //Rectangle pageRect = document.getPageSize();
            PdfWriter.getInstance(document, os);
            //int test = document.getPageNumber();
            BaseFont bfSong = BaseFont.createFont("STSong-Light","UniGB-UCS2-H", false);
            Font fontSong = new Font(bfSong, 10, Font.NORMAL);
//     try {
//         Watermark watermark = new Watermark(Image.getInstance("test.jpg"), pageRect.left()+50,pageRect.top()-85);
//         watermark.scalePercent(50);
//         document.add(watermark);
//     }catch(Exception e) {
//    System.err.println("请查看文件“test.jpg”是否在正确的位置?");
//     }
            HeaderFooter header = new HeaderFooter(new Phrase("查询结果列表",fontSong), false);
            header.setBorder(2);
            header.setAlignment(Element.ALIGN_LEFT);
            document.setHeader(header);
            HeaderFooter footer = new HeaderFooter(new Phrase("第 ", fontSong), new Phrase(" 页", fontSong));
            footer.setAlignment(Element.ALIGN_CENTER);
            footer.setBorder(1);
            document.setFooter(footer);
            document.open();


                List list = doc.selectNodes("/DATA/HEADER/CELL");
                if(list == null || list.size() == 0) return;

                Table table = new Table(list.size());
                table.setDefaultVerticalAlignment(Element.ALIGN_MIDDLE);
                table.setBorder(Rectangle.NO_BORDER);
//                int hws[] = { 10, 20, 10, 20};
//                table.setWidths(hws);
                table.setWidth(100);
                table.setPadding(2);
                table.setDefaultHorizontalAlignment(Element.ALIGN_CENTER);
                Font headerfont = new Font(bfSong, 10,Font.BOLD, new Color(0, 0, 0));
                Font bodyfont = new Font(bfSong, 9,Font.NORMAL, new Color(0, 0, 200));
                Cell cell = null;
//              cellleft.setHorizontalAlignment(Element.ALIGN_CENTER);
                for (int i = 0; i < list.size(); i++)
                {
                    org.dom4j.Element ele = (org.dom4j.Element) list.get(i);
                    cell = new Cell(new Phrase(ele.getText(),headerfont));
                    table.addCell(cell);
                }

                List dataList = doc.selectNodes("/DATA/ROWS/ROW");
                for (int i = 0; i < dataList.size(); i++)
                {
                    org.dom4j.Element ele = (org.dom4j.Element) dataList.get(i);
                    List cells = ele.elements("CELL");
                    for (int j = 0; j < cells.size(); j++)
                    {
                        cell = new Cell(new Phrase (((org.dom4j.Element) cells.get(j)).getText(),bodyfont));
                        table.addCell(cell);
                    }
                }


//表头信息
//            Cell cellmain = new Cell(new Phrase("用户信息",
//                                                new Font(bfSong, 10, Font.BOLD,
//                new Color(0, 0, 255))));
//            cellmain.setHorizontalAlignment(Element.ALIGN_CENTER);
//            cellmain.setColspan(4);
//            cellmain.setBorder(Rectangle.NO_BORDER);
//            cellmain.setBackgroundColor(new Color(0xC0, 0xC0, 0xC0));
//            table.addCell(cellmain);
//
//
//            //收货和订货人信息，表体内容
//            table.addCell(new Phrase("姓名", fontSong));
//            table.addCell(new Phrase("张三", fontSong));
//            table.addCell(new Phrase("姓名", fontSong));
//            table.addCell(new Phrase("李四", fontSong));
//
//            table.addCell(new Phrase("电话", fontSong));
//            table.addCell(new Phrase("23456789", fontSong));
//            table.addCell(new Phrase("电话", fontSong));
//            table.addCell(new Phrase("9876543", fontSong));
//
//            table.addCell(new Phrase("邮编", fontSong));
//            table.addCell(new Phrase("100002", fontSong));
//            table.addCell(new Phrase("邮编", fontSong));
//            table.addCell(new Phrase("200001", fontSong));
//
//            table.addCell(new Phrase("地址", fontSong));
//            table.addCell(new Phrase("北京西城区XX路XX号", fontSong));
//            table.addCell(new Phrase("地址", fontSong));
//            table.addCell(new Phrase("上海陆家嘴区XX路XX号", fontSong));
//
//            table.addCell(new Phrase("电子邮件", fontSong));
//            table.addCell(new Phrase("zh_san@hotmail.com", fontSong));
//            table.addCell(new Phrase("电子邮件", fontSong));
//            table.addCell(new Phrase("li_si@hotmail.com", fontSong));
            document.add(table);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                if( document != null)
                {
                    document.close();
                }
                if (os != null)
                {
                    os.flush();
                    os.close();
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    public static void outputAll(org.dom4j.Document doc, HttpServletResponse response,
                              String fileName)
    {
        OutputStream os = null;
        if(doc == null) return;
        com.lowagie.text.Document document = null;
        try
        {
            response.setHeader("content-disposition",
                               "attachment;filename=" +
                               java.net.URLEncoder.encode(fileName, "UTF-8") +
                               ".pdf");
            response.setContentType("application/x-msdownload;");
            response.setCharacterEncoding("GBK");
            os = response.getOutputStream();
            document = new com.lowagie.text.Document();
            PdfWriter.getInstance(document, os);
            BaseFont bfSong = BaseFont.createFont("STSong-Light","UniGB-UCS2-H", false);
            Font fontSong = new Font(bfSong, 10, Font.NORMAL);
            HeaderFooter header = new HeaderFooter(new Phrase("查询结果列表",fontSong), false);
            header.setBorder(2);
            header.setAlignment(Element.ALIGN_LEFT);
            document.setHeader(header);
            HeaderFooter footer = new HeaderFooter(new Phrase("第 ", fontSong), new Phrase(" 页", fontSong));
            footer.setAlignment(Element.ALIGN_CENTER);
            footer.setBorder(1);
            document.setFooter(footer);
            document.open();

            List list = doc.selectNodes("//QUERYCONDITION/TITLEINFO/FIELDINFO");
            if(list == null || list.size() == 0) return;

            Table table = new Table(list.size());
            table.setDefaultVerticalAlignment(Element.ALIGN_MIDDLE);
            table.setBorder(Rectangle.NO_BORDER);
            table.setWidth(100);
            table.setPadding(2);
            table.setDefaultHorizontalAlignment(Element.ALIGN_CENTER);
            Font headerfont = new Font(bfSong, 10,Font.BOLD, new Color(0, 0, 0));
            Font bodyfont = new Font(bfSong, 9,Font.NORMAL, new Color(0, 0, 200));
            Cell cell = null;
			String[] fieldNames = new String[list.size()];
            for (int i = 0; i < list.size(); i++)
            {
                org.dom4j.Element ele = (org.dom4j.Element) list.get(i);
                cell = new Cell(new Phrase(ele.elementText("FIELDTITLE"),headerfont));
                table.addCell(cell);
				fieldNames[i] = ele.elementText("FIELDNAME");
            }

        	Document docData = ExcelUtil.getQueryData(doc);

        	List dataList = docData.selectNodes("//RESPONSE/RESULT/ROW");
            for (int i = 0; i < dataList.size(); i++)
            {
                org.dom4j.Element ele = (org.dom4j.Element) dataList.get(i);
                for (int j = 0; j < fieldNames.length; j++)
                {
                	org.dom4j.Element field = ele.element(fieldNames[j]);
					String value = null;
					if(field!=null)
						value = (field.attribute("sv")==null || field.attribute("sv").getText().equals(""))?field.getText():field.attribute("sv").getText();
					else
						value = "";
					
                    cell = new Cell(new Phrase (value,bodyfont));
                    table.addCell(cell);
                }
            }
            document.add(table);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                if( document != null)
                {
                    document.close();
                }
                if (os != null)
                {
                    os.flush();
                    os.close();
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
    
}
