package com.ccthanking.common.util.excelutil;

import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ccthanking.framework.common.User;


/**
 * 导入导出控制中心
 * @author cbl
 *
 */
public class ControllCenter {

	/**
	 * 执行导出excel，文件名为默认值download
	 * @param response
	 * @param title
	 * @param content
	 */
//	public static void executeExportExcel(HttpServletResponse response, Title title, Content content){
//		executeExportExcel(response,title,content,"download");
//	}
	/**
	 * 执行导出excel，文件名由调用者指定
	 * @param response
	 * @param title
	 * @param content
	 * @param filename
	 */
//	public static void executeExportExcel(HttpServletResponse response, Title title, Content content, String filename){
//        try {    
//            OutputStream os = response.getOutputStream();// 取得输出流
//            response.reset();// 清空输出流
//            response.setContentType("application/x-msdownload");// 定义输出类型
//            response.setHeader("Content-disposition", "attachment; filename="+filename+".xls");// 设定输出文件头
//            ExportExcel exportExcel = new ExportExcel();
//            exportExcel.execute(os,title,content);// 调用生成excel文件bean
//			os.close();
//        } catch (Exception e) {
//        }   
//
//	}
	/**
	 * 执行excel导入，返回从excel中读取的信息
	 * @param ins
	 * @param response
	 * @return
	 */
	public static Content[] executeImortExcel(HttpServletRequest request,Configer cfg,HttpServletResponse response,User user){
		return ImportExcel.executeJxl(ImportExcel.getInputStream(request),cfg,response,user);
	}
	/**
	 * @param args
	 */
	public static String getImportFormParameter(HttpServletRequest request, String name) {
		return ImportExcel.getParameter(request,name);

	}

}
