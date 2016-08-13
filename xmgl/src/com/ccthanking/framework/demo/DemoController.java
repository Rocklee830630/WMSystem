/**
 * @author wangzh
 */
package com.ccthanking.framework.demo;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ccthanking.framework.Globals;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.QuerySet;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.model.autocomplete;
import com.ccthanking.framework.model.requestJson;
import com.ccthanking.framework.plugin.AppInit;
import com.ccthanking.framework.util.Pub;

/**
 * @author wangzh
 */
@Controller
@RequestMapping("/demo/DemoController")
public class DemoController {

	@Autowired
	private DemoService demoService;
	
	@RequestMapping(params = "insertdemo")
	@ResponseBody
	protected requestJson insertdemo(HttpServletRequest request,requestJson json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.demoService.insertdemo(json.getMsg(),user);
		j.setMsg(resultVO);
		return j;
	}
	@RequestMapping(params = "updatedemo")
	@ResponseBody
	protected requestJson updatedemo(HttpServletRequest request,requestJson json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.demoService.updatedemo(json.getMsg(),user);
		j.setMsg(resultVO);
		return j;
	}
	@RequestMapping(params = "deletedemo")
	@ResponseBody
	protected requestJson deletedemo(HttpServletRequest request,requestJson json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.demoService.deletedemo(json.getMsg(),user);
		j.setMsg(resultVO);
		return j;
	}
	
	@RequestMapping(params = "updatebatchdemo")
	@ResponseBody
	protected requestJson updatebatchdemo(HttpServletRequest request,requestJson json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.demoService.updatebatchdemo(json.getMsg(),user);
		j.setMsg(resultVO);
		return j;
	}
	@ResponseBody  
	@RequestMapping(value = "getTags", method = {RequestMethod.GET})  
	public String getProjectListByProjectNameContent(@RequestParam("term") String term //注意看参数名称，这个是固定的不可配置  
	        )  
	{  
	  
	    return "[ { \"id\": \"5\", \"label\": \"巨人\", \"value\": \"巨人\" } ]";  
	}  
	
	/**
	 * 查询样例json
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "querydemo")
	@ResponseBody
	public requestJson querydemo(HttpServletRequest request,requestJson json) throws Exception
	{
		ServletContext application = request.getSession().getServletContext();
		String[] m = new String[5];
		m[0] = "这是一个消息";
		m[1] = "superman";
		m[2] = "jsp/business/jhb/tcjh/tcjh_list.jsp";
		m[3] = "sjbh";
		m[4] = "ywlx";
		//DwrService.remindToPerson(application,m);
		try{
			Thread.sleep(200);
		}catch(InterruptedException e)
		{
			e.printStackTrace();
		}
		
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  domresult = "";
		domresult = this.demoService.queryConditiondemo(json.getMsg(),user);
		j.setMsg(domresult);
		return j;

	}
	@RequestMapping(params = "returnauto")
	@ResponseBody
	public List returnauto(HttpServletRequest request,autocomplete match) throws Exception
	{
        String [][] q = DBUtil.query("select dic_code,dic_value,dic_value_spell,dic_value_aspell from fs_dic_tree where parent_id = '1000000000010' and dic_value_spell like'"+match.getMatchInputValue()+"%'");
        List list = new ArrayList();  
        if(q!=null&&q.length>0)
        {
        	for(int i =0;i<q.length;i++)
        	{
        		 autocomplete a = new autocomplete();
        	     a.setRegionCode(q[i][0]);
        	     a.setRegionName(q[i][1]);
        	     a.setRegionNameEn(q[i][3]);
        	     a.setRegionShortnameEn(q[i][2]);
        	     list.add(a);

        	}
        	
        }
		return list;

	}
		   /** 
		    * 导出Excel 读取excel模版实现导出
		    * @param model 
		    * @param projectId 
		    * @param request 
		    * @return 
		    */  
		    @RequestMapping(params = "toDsExcel")
		    @ResponseBody
		    public void toDsExcel( HttpServletRequest request, HttpServletResponse response,@RequestParam(value = "queryResult") final String queryResult){
		    	   HSSFWorkbook workBook=null;  
		    	  String fileName = "test";//此处自定义导出文件名称
		    	try {
					String templatepath = AppInit.appPath + "WEB-INF\\template\\demo.xls";//此处为读取模版，需要自定义
					InputStream inp = new FileInputStream(templatepath);
					//从源文件中进行读取  
					if(inp!=null){  
			            workBook = new HSSFWorkbook(inp);  
			        }
					HSSFSheet sheet = workBook.getSheetAt(0);//获得sheet页
					//循环写结果集数据 begin
					
					JSONArray list = Pub.doInitJson(queryResult);
					int r =2;//起始行
					int c = 1;//其实列
					for(int i=0;i<list.size();i++){
						JSONObject jsonObject = (JSONObject)list.get(i);
						HSSFRow row = sheet.createRow(r+i); //创建一行
						
				        HSSFCell cell = row.createCell((short)c);  //序号
				//		cell.setEncoding((short)HSSFCell.ENCODING_UTF_16);
				        cell.setCellValue(i+1);
				        cell = row.createCell((short)(c+1));  //项目名称
				//		cell.setEncoding((short)HSSFCell.ENCODING_UTF_16);
				        cell.setCellValue(jsonObject.getString("XMMC"));
				        cell = row.createCell((short)(c+4));  //项目起始点
				//		cell.setEncoding((short)HSSFCell.ENCODING_UTF_16);
				        boolean t =  jsonObject.containsKey("XMNF_SV");
				        if(t==true){
				        	cell.setCellValue(jsonObject.getString("XMNF_SV"));
				        }else{
				        	cell.setCellValue(jsonObject.getString("XMNF"));
				        }
				        ////////////其余的再次补充
					  }
					//循环写结果集数据 end
					
					  //修改表格内容 begin   （有需要写类似代码）
					 HSSFRow row_ = sheet.getRow((short) 1);
					 HSSFCell cell_ = row_.getCell((short) 1);
			//		 cell_.setEncoding((short)HSSFCell.ENCODING_UTF_16);
					 cell_.setCellValue("序号");
					  //修改表格内容 end  

			        response.setHeader("Content-type", "application/vnd.ms-excel");  
			        response.setHeader("Content-Disposition", "attachment; filename="+fileName+".xls");  
			          
			        ServletOutputStream fileOut =response.getOutputStream();  
			        workBook.write(fileOut);  
			        fileOut.flush();  
			        fileOut.close();  
		    	} catch (Exception e) {
					System.out.println("[Info: ] User canceled - " + e.getMessage());
				}
		    }
		    @SuppressWarnings("static-access")  
		    private String getValue(HSSFCell hssfCell){  
		      if(hssfCell.getCellType() == hssfCell.CELL_TYPE_BOOLEAN){  
		        return String.valueOf( hssfCell.getBooleanCellValue());  
		      }else if(hssfCell.getCellType() == hssfCell.CELL_TYPE_NUMERIC){  
		        return String.valueOf( hssfCell.getNumericCellValue());  
		      }else{  
		        return String.valueOf( hssfCell.getStringCellValue());  
		      }  
		    }  
		    
			/**
			 * 查询样例json
			 * @param json
			 * @return
			 * @throws Exception
			 */
			@RequestMapping(params = "queryAttch")
			@ResponseBody
			public requestJson queryAttch(HttpServletRequest request,requestJson json) throws Exception
			{

				//DwrService.remindToPerson(application,m);
				try{
					Thread.sleep(200);
				}catch(InterruptedException e)
				{
					e.printStackTrace();
				}
				
				User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
				requestJson j = new requestJson();
				String  domresult = "";
				domresult = this.demoService.queryAttchdemo(json.getMsg(),user);
				j.setMsg(domresult);
				return j;

			}
			/**
			 * 查询样例
			 * @param json
			 * @return
			 * @throws Exception
			 */
			@RequestMapping(params = "savePoint")
			@ResponseBody
			public requestJson savePoint(HttpServletRequest request,requestJson json) throws Exception
			{
                String point = request.getParameter("point");
                String xmbh = request.getParameter("xmbh");
                String bdbh = request.getParameter("bdbh");

				requestJson j = new requestJson();
				String  domresult = "操作成功";
				
				Connection conn = DBUtil.getConnection();
				
				try {
					conn.setAutoCommit(false);
					QuerySet qs = DBUtil.executeQuery("select xmid  from  BAIDU_MAP where XMID='"+xmbh+"' and bdid='"+bdbh+"'", null);
					if(qs.getRowCount()>0){
						String sql = "update BAIDU_MAP set PONIT ='"+point+"' where  XMID='"+xmbh+"' and bdid='"+bdbh+"'";
						DBUtil.exec(conn, sql);
					}else{
						String sql = "insert into BAIDU_MAP (XMID,BDID,PONIT) values('"+xmbh+"','"+bdbh+"','"+point+"')";
						DBUtil.exec(conn, sql);
					}
					
					conn.commit();
		
				} catch (Exception e) {
					conn.rollback();
					e.printStackTrace(System.out);
					domresult = "操作失败";
				} finally {
					if (conn != null) {
						conn.close();
					}
				}

				j.setMsg(domresult);
				return j;

			}

		
}
