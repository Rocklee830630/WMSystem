package com.ccthanking.common.util.excelutil;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.Sheet;
import jxl.Workbook;
import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.WritableCell;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.apache.poi.hssf.usermodel.*;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.User;

public class ImportExcel {
	
	private static DiskFileItemFactory factory;

	private static ServletFileUpload upload;
	

	static {
		factory = new DiskFileItemFactory();
		// 附件大于该尺寸时就要使用硬盘缓存:10M
		factory.setSizeThreshold(10485760);
		// 临时保存的位置,默认使用tomcat的临时目录,如:d:\tomcat5\temp
		upload = new ServletFileUpload(factory);
	}

	/**
	 * 用poi方式执行导入操作
	 * @param ins
	 * @return
	 */
	public static Content execute(InputStream ins,Configer cfg, HttpServletResponse response) {
		Content content = new Content();
		try {
			HSSFWorkbook workbook = new HSSFWorkbook(ins);
			// 获得Excel的Sheet页
			// 在Excel文档中，第一工作表的缺省索引是0

			HSSFSheet sheet = workbook.getSheetAt(0);
			int rowCount = 0;
			int colCount = 0;
			HSSFRow hssfRow;// poi中定义的excel行

			if (sheet.getPhysicalNumberOfRows() > 0) {
				rowCount = sheet.getPhysicalNumberOfRows();
				if (sheet.getRow(0).getPhysicalNumberOfCells() > 0) {
					colCount = sheet.getRow(0).getPhysicalNumberOfCells();
				}
			}
			//add by cbl 获取列对应的字典项存放到对应数组中
			//取出config.xml配置信息存入arrayList对象
//			获取信息存入arraylist
//			ArrayList arrayListkey = cfg.getRow();
			
			ArrayList coldic = cfg.getColdic();
			//重新定义列长度
			colCount = coldic.size();
			//add by cbl
			if (rowCount > 0 && colCount > 0) {
				for (int i = 1; i < rowCount; i++) {//第一行为标题默认重第二行读取
					Row row = new Row();// 用于导入内容对象的行
					hssfRow = sheet.getRow(i);
					if (hssfRow != null) {
						for (int j = 0; j < colCount; j++) {
							//读取配置文件,通过配置文件获取单元格对应的字段(单元格对应的字段与excle导入的数据项对应)
							//如果对应的column存在dic则通过字典名和字典值找到字典代码存入数据库,如果没有字典文件则直接存入
							//获取字典名
							String dic_name = (String)coldic.get(j);
							if(!"".equals(dic_name)){
								if(hssfRow.getCell((short) j)!=null){
//									String dic_code = getDiccodeByValue(dic_name,hssfRow.getCell((short) j).getStringCellValue());
//									row.addCell(dic_code);
								}else{
									row.addCell("");
								}
								
							}else{//无字典处理
								if(hssfRow.getCell((short) j)!=null){
									row.addCell(hssfRow.getCell((short) j).getStringCellValue());
								}else{
									row.addCell("");
								}
							}
						}
					}
					content.addRow(row);
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
		}
		return content;
	}
	public static String getDiccodeByValue(String dic_name,String dic_value,String dic_filters,User user){

		String diccode = "";
		String getsql= "select dic_code from dic_tree where dic_value = trim('"+dic_value+"') and parent_id = (select id from dic_tree where dic_code = '"+dic_name+"')";
		if(!"".equals(dic_filters)){
			try {
			//	getsql += QueryConditionUtil.getSJQXCondition(user, null);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		String rs[][] = DBUtil.query(getsql);
		if(!"".equals(rs)&& rs!= null){
			diccode = rs[0][0];
		}
		return diccode;
	}
	/**
	 * 用jxl方式执行导入操作
	 * @param ins
	 * @return
	 */
	public static Content[] executeJxl(InputStream ins,Configer cfg,HttpServletResponse response,User user){
		List<Content> clist = new ArrayList<Content>();
		Content content = new Content();
		Content content1 = new Content();//翻译好的content对象
		ArrayList coldic = cfg.getColdic();
		ArrayList dicfilters = cfg.getDicfilters();
		int realrow = 1;//实际行数
		//重新定义列长度
		int colCount = coldic.size();
		 try {
			if (ins != null) {
				Workbook workbook = null;
				workbook = Workbook.getWorkbook(ins);
				//
				Sheet sheet = workbook.getSheet(0);
				//获取实际行数
				//content
				if (sheet != null && sheet.getRows() > 0) {
					realrow = getRightRows(sheet);
					for (int i = 1; i < realrow; i++) {//从第二行读
						jxl.Cell[] cellArray = (jxl.Cell[]) sheet.getRow(i);
						Row row = new Row();
						if (cellArray != null && cellArray.length > 0) {
							for (int j = 0; j < cellArray.length; j++) {
								
									jxl.Cell cell = cellArray[j];
									row.addCell(cell.getContents(),j,i);
							}
						}
						content.addRow(row);
					}
				}
				//content1
				if (sheet != null && sheet.getRows() > 0) {
					for (int i = 1; i < realrow; i++) {//从第二行读
						jxl.Cell[] cellArray = (jxl.Cell[]) sheet.getRow(i);
						Row row = new Row();
						if (cellArray != null && cellArray.length > 0) {
							for (int j = 0; j < colCount; j++) {
								String dic_name = (String)coldic.get(j);
								String dic_filters = (String)dicfilters.get(j);
								if(!"".equals(dic_name)){
									String dic_code = getDiccodeByValue(dic_name,cellArray[j].getContents(),dic_filters,user);
									jxl.Cell cell = cellArray[j];
									row.addCell(dic_code,j,i);
								}else{
									jxl.Cell cell = cellArray[j];
									row.addCell(cell.getContents(),j,i);
								}

							}
						}
						content1.addRow(row);
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		clist.add(0, content);
		clist.add(1, content1);
		if(clist.size() == 0)
			return null;
		Content[] acontent = new Content[clist.size()];
		clist.toArray(acontent);
		return acontent;
//		return clist;
	}
	/**
	 * 获得输入流
	 * @param request
	 */
	public static InputStream getInputStream(HttpServletRequest request){
		if (request == null) {
			return null;
		}
		try {
			List items = upload.parseRequest(request);
			if (items != null && !items.isEmpty()) {
				for (int i = 0; i < items.size(); i++) {
					FileItem item = (FileItem) items.get(i);
					if (!item.isFormField()) {
						return item.getInputStream();
					}
				}
			}
		} catch (Exception e) {
			return null;
		}
		return null;
	}
	/**
	 * 返回指定名字的参数
	 * @param request
	 * @param name
	 * @return
	 */
	public static String getParameter(HttpServletRequest request,String name){
		if (request == null) {
			return null;
		}
		HashMap params = getParameters(request);
		if(params != null){
			return (String)params.get(name);
		}
		else{
			return null;
		}
	}
	/**
	 * 返回form中的全部参数
	 * @param request
	 * @return
	 */
	public static HashMap getParameters(HttpServletRequest request){
		HashMap parameters= new HashMap();
		if (request == null) {
			return null;
		}
		try {
			List /* FileItem */items = upload.parseRequest(request);
			if (items.size() > 0) {
				Iterator it = items.iterator();
				while (it.hasNext()) {
					FileItem item = (FileItem) it.next();
					if (item.isFormField()) {
						String fieldName = item.getFieldName();
						String value = item.getString();
						if (value != null) {
							try {
								value = new String(value.getBytes("8859_1"),
										"UTF8");
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						parameters.put(fieldName, value);
					}

				}
			}

		} catch (Exception e) {
			return null;
		}
		return parameters;
	}

//	返回去掉空行的记录数  

	private static int getRightRows(Sheet sheet) {  

	int rsCols = sheet.getColumns(); //列数  

	int rsRows = sheet.getRows(); //行数  

	int nullCellNum;  

	int afterRows = rsRows;  

	for (int i = 1; i < rsRows; i++) { //统计行中为空的单元格数  

	   nullCellNum = 0;  

	    for (int j = 0; j < rsCols; j++) {  

	        String val = sheet.getCell(j, i).getContents();  

//	        val = StringUtils.trimToEmpty(val);  

	       if("".equals(val) ||val == null)

	           nullCellNum++;  

	    }  

	    if (nullCellNum >= rsCols) { //如果nullCellNum大于或等于总的列数  

	     afterRows--;          //行数减一  

	   }  

	}  

	return afterRows;  

	} 


}
