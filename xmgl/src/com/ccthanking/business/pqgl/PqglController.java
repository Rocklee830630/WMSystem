package com.ccthanking.business.pqgl;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ccthanking.business.pqgl.service.PqglService;
import com.ccthanking.framework.model.requestJson;
import com.ccthanking.framework.plugin.AppInit;
import com.ccthanking.framework.util.Pub;
import com.ccthanking.framework.util.RandomGUID;

@Controller
@RequestMapping("/pqgl/pqglController")
public class PqglController {
	@Autowired
	private PqglService service;
	@RequestMapping(params = "queryProjectInfo")
	@ResponseBody
	public requestJson queryProjectInfo(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.service.queryProjectInfo(request,js.getMsg());
		j.setMsg(resultVO);
		return j;
	}
	/**
	 * 计划反馈方法
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "doJhfk")
	@ResponseBody
	public requestJson doJhfk(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.service.doJhfk(request,js.getMsg());
		j.setMsg(resultVO);
		return j;
	}
	/**
	 *方法
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "doYnqk")
	@ResponseBody
	public requestJson doYnqk(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.service.doYnqk(request,js.getMsg());
		j.setMsg(resultVO);
		return j;
	}
	/**
	 * 子项目管理页面保存方法
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "doZxmgl")
	@ResponseBody
	public requestJson doZxmgl(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.service.insertPqzxm(request,js.getMsg());
		j.setMsg(resultVO);
		return j;
	}
	@RequestMapping(params = "queryPqInfo")
	@ResponseBody
	public requestJson queryPqInfo(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.service.queryPqInfo(request,js.getMsg());
		j.setMsg(resultVO);
		return j;
	}
	/**
	 * 进展剩余查询
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryJzsy")
	@ResponseBody
	public requestJson queryJzsy(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.service.queryJzsy(request,js.getMsg());
		j.setMsg(resultVO);
		return j;
	}
	/**
	 * 新增进展剩余
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "insertJzsy")
	@ResponseBody
	public requestJson insertJzsy(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.service.insertJzsy(request,js.getMsg());
		j.setMsg(resultVO);
		return j;
	}
	/**
	 * 更新进展剩余
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "updateJzsy")
	@ResponseBody
	public requestJson updateJzsy(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.service.updateJzsy(request,js.getMsg());
		j.setMsg(resultVO);
		return j;
	}
	/**
	 * 问题解决查询
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryWtjj")
	@ResponseBody
	public requestJson queryWtjj(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.service.queryWtjj(request,js.getMsg());
		j.setMsg(resultVO);
		return j;
	}
	/**
	 * 新增问题解决
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "insertWtjj")
	@ResponseBody
	public requestJson insertWtjj(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.service.insertWtjj(request,js.getMsg());
		j.setMsg(resultVO);
		return j;
	}
	/**
	 * 更新问题解决
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "updateWtjj")
	@ResponseBody
	public requestJson updateWtjj(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.service.updateWtjj(request,js.getMsg());
		j.setMsg(resultVO);
		return j;
	}
	/**
	 * 更新问题解决
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "updatePqzxm")
	@ResponseBody
	public requestJson updatePqzxm(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.service.updatePqzxm(request,js.getMsg());
		j.setMsg(resultVO);
		return j;
	}
	
	/**
	 * 根据主键查询子项目信息
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryPqzxmByPk")
	@ResponseBody
	public requestJson queryPqzxmByPk(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.service.queryPqzxmByPk(request,js.getMsg());
		j.setMsg(resultVO);
		return j;
	}
	/**
	 * 查询进展反馈信息
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryJzfk")
	@ResponseBody
	public requestJson queryJzfk(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.service.queryJzfk(request,js.getMsg());
		j.setMsg(resultVO);
		return j;
	}
	/**
	 * 新增进展反馈信息
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "insertJzfk")
	@ResponseBody
	public requestJson insertJzfk(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.service.insertJzfk(request,js.getMsg());
		j.setMsg(resultVO);
		return j;
	}
	/**
	 * 修改进展反馈信息
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "updateJzfk")
	@ResponseBody
	public requestJson updateJzfk(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.service.updateJzfk(request,js.getMsg());
		j.setMsg(resultVO);
		return j;
	}
	/**
	 * 修改进展反馈信息
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "deleteJzfk")
	@ResponseBody
	public requestJson delJzfk(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.service.deleteJzfk(request,js.getMsg());
		j.setMsg(resultVO);
		return j;
	}
	/**
	 * 新增进展反馈信息
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "doJhsjfk")
	@ResponseBody
	public requestJson doJhsjfk(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.service.doJhsjfk(request,js.getMsg());
		j.setMsg(resultVO);
		return j;
	}
	/**
	 * 获取排迁计划反馈次数
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "getJhfkCounts")
	@ResponseBody
	public requestJson getJhfkCounts(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.service.getJhfkCounts(request,js.getMsg());
		j.setMsg(resultVO);
		return j;
	}
	/**
	 * 获取子项目个数
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "getPqzxmCounts")
	@ResponseBody
	public requestJson getPqzxmCounts(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.service.getPqzxmCounts(request,js.getMsg());
		j.setMsg(resultVO);
		return j;
	}
	/**
	 * 获取子项目个数
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "getGUID")
	@ResponseBody
	public requestJson getGUID(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = new RandomGUID().toString();
		j.setMsg(resultVO);
		return j;
	}
	/**
	 * 排迁子项目删除方法
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "deletePqzxm")
	@ResponseBody
	public requestJson deletePqzxm(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.service.deletePqzxm(request,js.getMsg());
		j.setMsg(resultVO);
		return j;
	}
	private String getJSONValue(JSONObject obj,String s){
		String val = "";
		try{
			if(obj.containsKey(s)){
				val = obj.getString(s);
			}else{
				// nothing to do
			}
		}catch(Exception e){
			// nothing to do
		}
		return val;
	}
	/**
	 * 排迁内业导出方法
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "doCustomExportExcel")
	@ResponseBody
	public void doCustomExportExcel(HttpServletRequest request,
			HttpServletResponse response,requestJson js) {
		String fileName = "表格打印";
		//String querySql ="select * from XMXX"; 
		//        String info = request.getParameter("ExpTabListResultValue");
		String objStr = (String)js.getObj();
//		JSONObject obj = JSONObject.fromObject(objStr);
//		String queryData = this.getJSONValue(obj,"ExpTabListQueryCondition");
//		String tabThead = this.getJSONValue(obj,"ExpTabListThead");
//		String fieldnames = this.getJSONValue(obj,"ExpTabListFieldNames");
//		String templateName = this.getJSONValue(obj,"templateName");
//		String t_fieldnames = this.getJSONValue(obj,"fieldnames");
//		String startXY = this.getJSONValue(obj,"startXY");
//		String printFileName = this.getJSONValue(obj,"printFileName");
		String queryFlag = request.getParameter("queryFlag");
		String queryData = request.getParameter("ExpTabListQueryCondition");
		String queryDataAll = request.getParameter("ExpTabListQueryConditionAll");
        String tabThead = request.getParameter("ExpTabListThead");
        String fieldnames = request.getParameter("ExpTabListFieldNames");
        String templateName = Pub.val(request, "templateName");
        String t_fieldnames = Pub.val(request, "fieldnames");
        String startXY = Pub.val(request, "startXY");
        String printFileName = Pub.val(request, "printFileName");
		if (!Pub.empty(printFileName)) {
			fileName = printFileName;
		}
		String resultJson = "";
		try {
			if("1".equals(queryFlag)){
				resultJson = this.service.doCustomExportExcel(request,queryData);
			}else{
				resultJson = this.service.doCustomExportExcel(request,queryDataAll);
			}
			// 导出文件的名称。当指定模板时，导出文件名称得设置一下编码，且必须放在这里。
			fileName = new String(fileName.getBytes("gb2312"), "iso8859-1");
			HSSFWorkbook workBook = null;
			try {
				String templatepath = AppInit.appPath
						+ "WEB-INF\\template\\" + templateName;//此处为读取模版，需要自定义
				InputStream inp = new FileInputStream(templatepath);
				//从源文件中进行读取
				if (inp != null) {
					workBook = new HSSFWorkbook(inp);
				}
				HSSFSheet sheet = workBook.getSheetAt(0);//获得sheet页
				//循环写结果集数据 begin

				JSONArray list = Pub.doInitJson(resultJson);
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
				for (int i = 0; i < list.size(); i++) {
					JSONObject jsonObject = (JSONObject) list.get(i);
					HSSFRow row = sheet.createRow(r + i); //创建一行
					if (fieldname_ != null && fieldname_.length > 0) {
						HSSFCell cell_xh = row.createCell((short) (c));						
						cell_xh.setCellStyle(style_xh);
						cell_xh.setCellValue(i + 1);
						for (int j = 0; j < fieldname_.length; j++) {
							HSSFCell cell = row.createCell((short) (c + j + 1));
							cell.setCellStyle(style);
							boolean t = jsonObject.containsKey(fieldname_[j].toUpperCase()+ "_SV");
							if (t == true) {
								cell.setCellValue(jsonObject.getString(fieldname_[j].toUpperCase()+ "_SV"));
							} else {
								cell.setCellValue(jsonObject.getString(fieldname_[j].toUpperCase()));
							}
						}
					}
				}
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

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("[Info: ] User canceled - " + e.getMessage());
		}
	}
}
