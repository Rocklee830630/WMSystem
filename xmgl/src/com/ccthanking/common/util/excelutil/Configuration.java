package com.ccthanking.common.util.excelutil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

public class Configuration {
	private static String driverClass;
	private static String url ;
	private static String user;
	private static String pass;
	private static String xmlFile = "/WEB-INF/conf/eximp/config.xml";
	private static String logFile ;			//日志文件
	private static String[] orgChkList;
	private static Element  root ;


//	row.add(cell);
	//解析xml返回对应业务类型的数组
	  public Configer Parsecfgxml(HttpServletRequest request){
			 Configer cfg = new Configer();
		 //存储返回值
		 	String ywlx = request.getParameter("ywlx");
		  SAXBuilder sb = new SAXBuilder();
	      try {
	    	  String path = request.getRealPath("");
	    	  path+=xmlFile;
			Document doc = sb.build(new File(path));
			root = doc.getRootElement();			//根节点
			
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
		
		
		//获取Excel文件导入配置信息
		List list = root.getChildren("excel_unit");	
		
		int len = list.size();
		show("len="+len);
		
		//读出所每个单元的配置信息
		for(int i=0;i<len;i++){
			String tmp ;
			Element element = (Element)list.get(i);
			String xywlx = element.getAttributeValue("ywlx");
			if(ywlx.equals(xywlx))
			{
				String voname = element.getAttributeValue("voname");
				cfg.setVoName(voname);
				 ArrayList row = new ArrayList();
				 ArrayList coldic = new ArrayList();
				 ArrayList titles = new ArrayList();
				 ArrayList dicfilters = new ArrayList();
				//
//				获取将要被读取的Excel文件夹
				
				//读取该文件的配置信息
				//	获取需要读取的sheet页
				tmp = element.getChildText("sheet_index");
				//	开始行
				tmp = element.getChildText("start_row");
				tmp = (tmp==null?"0":tmp);	//空值则认为无限制
			
				show("start_row="+tmp);
				//	结束行
				tmp = element.getChildText("end_row");
				tmp = (tmp==null?"0":tmp);	//空值则认为无限制
				show("end_row="+tmp);
				
				//	开始列
				
				show("start_column="+element.getChildText("start_column"));
				//	结束列
				show("end_column="+element.getChildText("end_column"));
				
				//表及映射关系
				Element table = (Element) element.getChild("target_table");
				
//				show("table_name="+table.getAttributeValue("table_name"));
				//	遍历全部映射关系
				List colMap = table.getChildren("column_map");
				//	分配Map的空间
				int mapLen = colMap.size();
//				String[][] map = new String[mapLen][5];
				String colname = "";
				String dicname = "";
				String title = "";
				String dicfilter = "";
				show("colMap Len="+mapLen);
				for(int j=0;j<mapLen;j++){
					Element eMap = (Element) colMap.get(j);
					colname = eMap.getAttributeValue("db_column");	//数据库字段
					dicname = eMap.getAttributeValue("column_dic");	//数据库字典
					title = eMap.getAttributeValue("column_title");	//列标题
					dicfilter = eMap.getAttributeValue("dic_filter")== null ? "" :eMap.getAttributeValue("dic_filter");
					row.add(colname);
					coldic.add(dicname);
					titles.add(title);
					dicfilters.add(dicfilter);
				}
				//为cfg对象赋值
				cfg.setRow(row);//column_name
				cfg.setColdic(coldic);//column_dic
				cfg.setTitles(titles);
				cfg.setDicfilters(dicfilters);
			}
		}
		return cfg;
	  }
	  public static void show(String str){
		  System.out.println(str);
	  }

	
}



