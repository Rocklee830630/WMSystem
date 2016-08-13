package com.ccthanking.framework.controller;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ccthanking.framework.Globals;
import com.ccthanking.framework.common.BaseResultSet;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.model.requestJson;
import com.ccthanking.framework.plugin.AppInit;
import com.ccthanking.framework.service.LogService;
import com.ccthanking.framework.util.Pub;

/**
 * @auther xhb 
 */
@Controller
@RequestMapping("/logController")
public class LogController {

	@Autowired
	private LogService logService;
	
	/**
	 * 查询用户的登录日志
	 */
	@RequestMapping(params = "queryLogin")
	@ResponseBody
	public requestJson queryLogin(HttpServletRequest request, requestJson json) {
		User user = (User)request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		try {
			String domResult = logService.queryLogin(json.getMsg(), user);
			j.setMsg(domResult);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return j;
	}
	
	/**
	 * 查询用户的登录日志
	 */
	@RequestMapping(params = "queryLogOperation")
	@ResponseBody
	public requestJson queryLogOperation(HttpServletRequest request, requestJson json) {
		User user = (User)request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		try {
			String domResult = logService.queryLogOperation(json.getMsg(), user);
			j.setMsg(domResult);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return j;
	}
	/**
	 * 查询登陆详细
	 * @param request
	 * @param json
	 * @return
	 */
	@RequestMapping(params = "queryLogDetail")
	@ResponseBody
	public requestJson queryLogDetail(HttpServletRequest request, requestJson json) {
		requestJson j = new requestJson();
		try {
			String domResult = logService.queryLogDetail(json.getMsg(), request);
			j.setMsg(domResult);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return j;
	}
	
	@RequestMapping(params = "queryExportLogin")
	@ResponseBody
	public void queryExportLogin(HttpServletRequest request,
			HttpServletResponse response,requestJson js) {
		User user = (User)request.getSession().getAttribute(Globals.USER_KEY);
		String fileName = "登录日志";
		String queryFlag = request.getParameter("queryFlag");
		String queryData = request.getParameter("ExpTabListQueryCondition");
		String queryDataAll = request.getParameter("ExpTabListQueryConditionAll");
        String templateName = Pub.val(request, "templateName");
        String t_fieldnames = Pub.val(request, "fieldnames");
        String startXY = Pub.val(request, "startXY");
        String printFileName = Pub.val(request, "printFileName");
		if (!Pub.empty(printFileName)) {
			fileName = printFileName;
		}
		ResultSet resultJson =null;
		try {
			long startTime1=System.currentTimeMillis();  
			if("1".equals(queryFlag)){
				resultJson = this.logService.queryExportLogin(queryData,user);
			}else{
				resultJson = this.logService.queryExportLogin(queryDataAll,user);
			}
			// 导出文件的名称。当指定模板时，导出文件名称得设置一下编码，且必须放在这里。
			fileName = new String(fileName.getBytes("gb2312"), "iso8859-1");
			HSSFWorkbook workBook = null;
			String templatepath = AppInit.appPath
					+ "WEB-INF\\template\\" + templateName;//此处为读取模版，需要自定义
			InputStream inp = new FileInputStream(templatepath);
			//从源文件中进行读取
			if (inp != null) {
				workBook = new HSSFWorkbook(inp);
			}
			HSSFSheet sheet = workBook.getSheetAt(0);//获得sheet页
			//循环写结果集数据 begin

			//JSONArray list = Pub.doInitJson(resultJson);
			int r = 2;//起始行
			int c = 1;//其实列
			if (!Pub.empty(startXY)) {
				if (startXY.indexOf(",") > -1) {
					String[] t = startXY.split(",");
					if (t != null && t.length > 0) {
						r = Integer.parseInt(t[0]);
						c = Integer.parseInt(t[1]);
					}
				}
			}
			String[] fieldname_ = t_fieldnames.split(",");
			//创建序号样式
			HSSFCellStyle style_xh = workBook.createCellStyle();
			style_xh.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
			style_xh.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
			style_xh.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
			style_xh.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
			style_xh.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 居中
			//创建单元格样式
			HSSFCellStyle style = workBook.createCellStyle();
			style.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框
			style.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框
			style.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框
			style.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框
	//		ResultSet rs = resultJson.getResultSet();
			//for (int i = 0; i < list.size(); i++) {
			int i=0;
			while (resultJson.next()) {
				//JSONObject jsonObject = (JSONObject) list.get(i);
				HSSFRow row = sheet.createRow(r + i); //创建一行
				if (fieldname_ != null && fieldname_.length > 0) {
					HSSFCell cell_xh = row.createCell((short) (c));						
					cell_xh.setCellStyle(style_xh);
					cell_xh.setCellValue(i + 1);
					for (int j = 0; j < fieldname_.length; j++) {
						HSSFCell cell = row.createCell((short) (c + j + 1));
						cell.setCellStyle(style);
						/**
						boolean t = jsonObject.containsKey(fieldname_[j].toUpperCase()+ "_SV");
						if (t == true) {
							cell.setCellValue(jsonObject.getString(fieldname_[j].toUpperCase()+ "_SV"));
						} else {
							cell.setCellValue(jsonObject.getString(fieldname_[j].toUpperCase()));
						}
						**/
						cell.setCellValue(resultJson.getString(fieldname_[j]));
					}
				}
				i++;
			}
			long startTime2=System.currentTimeMillis();
			System.out.println(startTime2-startTime1);
			response.setHeader("Content-type",
					"application/vnd.ms-excel");
			response.setHeader("Content-Disposition",
					"attachment; filename=" + fileName + ".xls");
			ServletOutputStream fileOut = response.getOutputStream();
			workBook.write(fileOut);
			fileOut.flush();
			fileOut.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("[Info: ] User canceled - "
					+ e.getMessage());
		}
	}
}
