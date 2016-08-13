package com.ccthanking.business.gcgl.zbgl;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.ccthanking.business.gcgl.zbgl.service.GczbService;
import com.ccthanking.business.gcgl.zbgl.vo.GcZbbVO;
import com.ccthanking.common.BusinessUtil;
import com.ccthanking.common.EventManager;
import com.ccthanking.common.YwlxManager;
import com.ccthanking.common.vo.EventVO;
import com.ccthanking.framework.Globals;
import com.ccthanking.framework.base.BaseDAO;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.QuerySet;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.model.autocomplete;
import com.ccthanking.framework.model.requestJson;
import com.ccthanking.framework.plugin.AppInit;
import com.ccthanking.framework.util.Pub;
import com.ccthanking.framework.util.RandomGUID;

/**
 * @author sunjl
 * @time 2013-8-7
 */
@Controller
@RequestMapping("/gczb/gczbController")
public class GczbController {
	
	@Autowired
	private GczbService gczbService;

	/**
	 * 查询json
	 * @param json
	 * @return
	 * @throws Exception
	 */
	//首页查询，查询出最新周报
	@RequestMapping(params = "query")
	@ResponseBody
	public requestJson queryCondition(final HttpServletRequest request,requestJson json) throws Exception
	{
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  domresult = "";
		domresult = this.gczbService.query(json.getMsg(),request);
		j.setMsg(domresult);
		return j;

	}
	
	
	/**
	 * 查询修改列表页，
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryZbsj")
	@ResponseBody
	public requestJson queryZbsj(final HttpServletRequest request,requestJson json) throws Exception
	{
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  domresult = "";
		domresult = this.gczbService.queryZbsj(json.getMsg(),user);
		j.setMsg(domresult);
		return j;

	}

	
	/**
	 * 查询是否存在周报信息json
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "query_zb")
	@ResponseBody
	public requestJson query_zb(final HttpServletRequest request,requestJson json) throws Exception
	{
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  domresult = "";
		String xmid=request.getParameter("xmid");
		domresult = this.gczbService.query_zb(json.getMsg(),user,xmid);
		j.setMsg(domresult);
		return j;

	}

	
	/**
	 * 查询json
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "query_tj")
	@ResponseBody
	public requestJson query_tj(final HttpServletRequest request,requestJson json) throws Exception
	{
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  domresult = "";
		String xmid =request.getParameter("xmid");
		String bdid =request.getParameter("bdid");
		domresult = this.gczbService.query_tj(json.getMsg(),user,xmid,bdid);
		j.setMsg(domresult);
		return j;

	}

	
	/**
	 * 项目管理公司概况列表查询json
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "query_gk")
	@ResponseBody
	public requestJson query_gk(final HttpServletRequest request,requestJson json) throws Exception
	{
		requestJson j = new requestJson();
		String  domresult = "";
		domresult = this.gczbService.query_gk(json.getMsg(),request);
		j.setMsg(domresult);
		return j;

	}
	
	/**
	 * 项目管理公司概况列表查询json月度详情
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "query_yd")
	@ResponseBody
	public requestJson query_yd(final HttpServletRequest request,requestJson json) throws Exception
	{
		requestJson j = new requestJson();
		String  domresult = "";
		domresult = this.gczbService.query_yd(json.getMsg(),request);
		j.setMsg(domresult);
		return j;

	}
	
	/**
	 * 项目管理公司概况周报本周完成统计
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "query_bz")
	@ResponseBody
	public requestJson query_bz(final HttpServletRequest request,requestJson json) throws Exception
	{
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  domresult = "";
		String year =request.getParameter("year");
		String xmglgs =request.getParameter("xmglgs");
		domresult = this.gczbService.query_bz(json.getMsg(),user,year,xmglgs);
		j.setMsg(domresult);
		return j;

	}

	
	/**
	 * 项目管理公司概况周报本月累计完成合计
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "query_bylj")
	@ResponseBody
	public requestJson query_bylj(final HttpServletRequest request,requestJson json) throws Exception
	{
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  domresult = "";
		String time=request.getParameter("time");
		String deptId = request.getParameter("xmglgs");
		domresult = this.gczbService.query_bylj(json.getMsg(),user,time,deptId);
		j.setMsg(domresult);
		return j;

	}
	//项目负责人以及总体概况
	@RequestMapping(params = "fzr_ztxx")
	@ResponseBody
	public requestJson fzr_ztxx(HttpServletRequest request,HttpServletResponse response) throws Exception
	{
		requestJson j = new requestJson();
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		String  domresult = "";
		String nd=request.getParameter("nd");
		String deptId = request.getParameter("xmglgs");
		domresult = this.gczbService.fzr_ztxx(user,nd,deptId);
		j.setMsg(domresult);
		return j;

	}
	
	
	//整改最多的项目(TOP5)
	@RequestMapping(params = "zgxm_xm_top")
	@ResponseBody
	public requestJson zgxm_xm_top(HttpServletRequest request,HttpServletResponse response) throws Exception
	{
		requestJson j = new requestJson();
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		String  domresult = "";
		String nd=request.getParameter("nd");
		String deptId = request.getParameter("xmglgs");
		domresult = this.gczbService.zgxm_xm_top(user,nd,deptId);
		j.setMsg(domresult);
		return j;

	}
	
	
	//整改最多的标段(TOP5)
	@RequestMapping(params = "zgxm_bd_top")
	@ResponseBody
	public requestJson zgxm_bd_top(HttpServletRequest request,HttpServletResponse response) throws Exception
	{
		requestJson j = new requestJson();
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		String  domresult = "";
		String nd=request.getParameter("nd");
		String deptId = request.getParameter("xmglgs");
		domresult = this.gczbService.zgxm_bd_top(user,nd,deptId);
		j.setMsg(domresult);
		return j;

	}
	
	
	//招标需求表格
	@RequestMapping(params = "zbxq_bg")
	@ResponseBody
	public requestJson zbxq_bg(HttpServletRequest request,HttpServletResponse response) throws Exception
	{
		requestJson j = new requestJson();
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		String  domresult = "";
		String nd=request.getParameter("nd");
		String deptId = request.getParameter("xmglgs");
		domresult = this.gczbService.zbxq_bg(user,nd,deptId);
		j.setMsg(domresult);
		return j;

	}
	
	
	//项目合同表格
	@RequestMapping(params = "xmht_bg")
	@ResponseBody
	public requestJson xmht_bg(HttpServletRequest request,HttpServletResponse response) throws Exception
	{
		requestJson j = new requestJson();
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		String  domresult = "";
		String nd=request.getParameter("nd");
		String deptId = request.getParameter("xmglgs");
		domresult = this.gczbService.xmht_bg(user,nd,deptId);
		j.setMsg(domresult);
		return j;

	}
	
	
	//工程部——工程进展——工程保函——工程洽商
	@RequestMapping(params = "gcb_ztgk")
	@ResponseBody
	public requestJson gcb_ztgk(HttpServletRequest request,HttpServletResponse response) throws Exception
	{
		requestJson j = new requestJson();
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		String  domresult = "";
		String nd=request.getParameter("nd");
		//String deptId = request.getParameter("xmglgs");
		domresult = this.gczbService.gcb_ztgk(user,nd);
		j.setMsg(domresult);
		return j;

	}
	
	
	//洽商最多的项目数(TOP5)
	@RequestMapping(params = "qsxm_xm_top")
	@ResponseBody
	public requestJson qsxm_xm_top(HttpServletRequest request,HttpServletResponse response) throws Exception
	{
		requestJson j = new requestJson();
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		String  domresult = "";
		String nd=request.getParameter("nd");
		domresult = this.gczbService.qsxm_xm_top(user,nd);
		j.setMsg(domresult);
		return j;

	}
	
	
	//洽商最多的标段数(TOP5)
	@RequestMapping(params = "qsxm_bd_top")
	@ResponseBody
	public requestJson qsxm_bd_top(HttpServletRequest request,HttpServletResponse response) throws Exception
	{
		requestJson j = new requestJson();
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		String  domresult = "";
		String nd=request.getParameter("nd");
		domresult = this.gczbService.qsxm_bd_top(user,nd);
		j.setMsg(domresult);
		return j;

	}

	
	/**
	 * 新增时校验开始时间和结束时间是否重复
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "query_ksjs_sj")
	@ResponseBody
	public requestJson query_ksjs_sj(final HttpServletRequest request,requestJson json) throws Exception
	{
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  domresult = "";
		String kssj=request.getParameter("kssj");
		String jssj=request.getParameter("jssj");
		String jhsjid=request.getParameter("jhsjid");
		domresult = this.gczbService.query_ksjs_sj(json.getMsg(),user,kssj,jssj,jhsjid);
		j.setMsg(domresult);
		return j;

	}

	
	/**
	 * 修改时校验开始时间和结束时间是否重复
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "query_sj")
	@ResponseBody
	public requestJson query_sj(final HttpServletRequest request,requestJson json) throws Exception
	{
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  domresult = "";
		String ksORjs=request.getParameter("ksORjs");
		String kssj=request.getParameter("kssj");
		String jssj=request.getParameter("jssj");
		String jhsjid=request.getParameter("jhsjid");
		String zbid=request.getParameter("zbid");
		domresult = this.gczbService.query_sj(json.getMsg(),user,ksORjs,kssj,jssj,jhsjid,zbid);
		j.setMsg(domresult);
		return j;

	}
	
	
	
	/**
	 * 保存数据json
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "insert")
	@ResponseBody
	protected requestJson insert(final HttpServletRequest request,requestJson json) throws Exception {
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  resultVO = "";
		String ywid=request.getParameter("ywid");
		resultVO = this.gczbService.insert(json.getMsg(),user,ywid);
		j.setMsg(resultVO);
		return j;
	}

	
	
	/**
	 * 修改数据json
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "update_zb")
	@ResponseBody
	protected requestJson update_zb(final HttpServletRequest request,requestJson json) throws Exception {
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  resultVO = "";
		String ywid=request.getParameter("ywid");
		resultVO = this.gczbService.update_zb(json.getMsg(),user,ywid);
		j.setMsg(resultVO);
		return j;
	}
	
	
	//删除周报信息
	@RequestMapping(params = "delete")
	@ResponseBody
	protected requestJson delete(HttpServletRequest request,requestJson json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.gczbService.delete(json.getMsg(),user);
		j.setMsg(resultVO);
		return j;
	}

	/**
	 * 保存数据周报时间json
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "insertZbsj")
	@ResponseBody
	protected requestJson insertZbsj(final HttpServletRequest request,requestJson json) throws Exception {
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.gczbService.insertZbsj(json.getMsg(),user);
		j.setMsg(resultVO);
		return j;
	}
	
	
	//周报工程管理项目名称联想查询
	@RequestMapping(params = "xmmcAutoQuery")
	@ResponseBody
	public List<autocomplete> xmmcAutoComplete(final HttpServletRequest request,autocomplete match) throws Exception{
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		List<autocomplete> list  = this.gczbService.xmmcAutoComplete(match,user);
		return list;
	}
	
	
	//周报统计项目名称联想查询
	@RequestMapping(params = "xmmcAutoQuery_tj")
	@ResponseBody
	public List<autocomplete> xmmcAutoComplete_tj(final HttpServletRequest request,autocomplete match) throws Exception{
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		List<autocomplete> list  = this.gczbService.xmmcAutoComplete_tj(match,user);
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
	    public void toDsExcel( HttpServletRequest request, HttpServletResponse response){
    		String nd=request.getParameter("nd");
			HSSFWorkbook workBook=null;  
			String fileName = "周报模版";//此处自定义导出文件名称
	    	try {
				String templatepath = AppInit.appPath + "WEB-INF\\template\\gczbgl.xls";//此处为读取模版，需要自定义
				InputStream inp = new FileInputStream(templatepath);
				//从源文件中进行读取  
				if(inp!=null){  
		            workBook = new HSSFWorkbook(inp);  
		        }
				HSSFCellStyle style = workBook.createCellStyle();
				style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
				style.setBorderTop(HSSFCellStyle.BORDER_THIN);
				style.setBorderRight(HSSFCellStyle.BORDER_THIN);
				style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
				HSSFSheet sheet = workBook.getSheetAt(0);//获得sheet页
				String xm_sql = "select xmbh, xmmc, bdbh, bdmc,nd from GC_JH_SJ where ND = '"+nd+"' and sfyx = '1' order by xmbh, xmbs, pxh, bdbh asc";
              QuerySet qs = DBUtil.executeQuery(xm_sql, null);
              int r = 2;//起始行
				 int c = 1;//起始列
              if(qs.getRowCount()>0)
              {
             	 
             	 for(int i =0;i<qs.getRowCount();i++)
             	 {
             		HSSFRow row = sheet.createRow(r+i); //创建一行
						
				        HSSFCell cell = row.createCell((short)c);  //序号
//						cell.setEncoding((short)HSSFCell.ENCODING_UTF_16);
						cell.setCellValue(i+1);
						cell.setCellStyle(style);
						
						
				        cell = row.createCell((short)(c+1));  //项目编号
//						cell.setEncoding((short)HSSFCell.ENCODING_UTF_16);
				        cell.setCellValue(qs.getString(i+1, "XMBH"));
				        cell.setCellStyle(style);
				        
				        cell = row.createCell((short)(c+2));  //项目名称
//						cell.setEncoding((short)HSSFCell.ENCODING_UTF_16);
						cell.setCellValue(qs.getString(i+1, "XMMC"));
						cell.setCellStyle(style);
						
						cell = row.createCell((short)(c+3));  //标段编号
//						cell.setEncoding((short)HSSFCell.ENCODING_UTF_16);
						cell.setCellValue(qs.getString(i+1, "BDBH"));
						cell.setCellStyle(style);
						
						cell = row.createCell((short)(c+4));  //标段名称
//						cell.setEncoding((short)HSSFCell.ENCODING_UTF_16);
						cell.setCellValue(qs.getString(i+1, "BDMC"));
						cell.setCellStyle(style);
						
				        cell = row.createCell((short)(c+5));  //计划数据id
//						cell.setEncoding((short)HSSFCell.ENCODING_UTF_16);
				        cell.setCellValue(qs.getString(i+1, "ND"));
				        cell.setCellStyle(style);
				     
             	 }
             	 
              }
              

				 HSSFRow row_ = sheet.getRow((short) 1);
				 HSSFCell cell_ = row_.getCell((short) 1);
//				 cell_.setEncoding((short)HSSFCell.ENCODING_UTF_16);
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
	
		
		@RequestMapping(params = "ExcelUpload", method = RequestMethod.POST)
	    public ModelAndView ExcelUpload( @RequestParam("myFile") MultipartFile file,HttpServletRequest request,final ModelAndView mav)  {

	    	 Connection conn = null;
	    	 HSSFWorkbook workBook=null;  
	    	 HSSFRow row = null;
	    	 User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
	    	 int count = 0;
	    	 String msg = "成功导入";
	    	 //String [] error =null;
	    	 try {
	 			conn = DBUtil.getConnection();
	 			conn.setAutoCommit(false);
	        if (!file.isEmpty()) {
	        
	        	InputStream inp = file.getInputStream();
				//从源文件中进行读取  
				if(inp!=null){  
		            workBook = new HSSFWorkbook(inp);  
		        }
				HSSFSheet sheet = workBook.getSheetAt(0);//获得sheet页
				  int rowNum = sheet.getLastRowNum();
				  String info[]= new String[rowNum+1];
			        row = sheet.getRow(1);
			        int colNum = row.getPhysicalNumberOfCells();
			        // 正文内容应该从第二行开始,第一行为表头的标题
			        for (int i = 2; i <= rowNum; i++) {	
			        	GcZbbVO vo = new GcZbbVO();
			           row = sheet.getRow(i);
			           if(row==null)
			        	   break;
			           String xmbh_tj="",bdbh_tj="",nd_tj="";
			            
			           String  xmbh= getCellFormatValue(2,row.getCell((short) 2));
			           if(Pub.empty(xmbh))
			        	   break;
			           xmbh=xmbh.trim();
			           if(!Pub.empty(xmbh))
			           {
			        	   vo.setXmbh(xmbh);
			        	   xmbh_tj=" and xmbh='"+xmbh+"'  ";
			        	   info[i]="序号:"+(i-1)+",项目编号："+xmbh+",";
			           }
			           
			           String  nd= getCellFormatValue(6,row.getCell((short) 6));
			           if(Pub.empty(nd))
			           {
			        	   nd=Pub.getDate("yyyy", new Date());
			           }
			           nd_tj="  and nd='"+nd+"'   ";
        
			           String  bdbh= getCellFormatValue(4,row.getCell((short) 4));
			           bdbh=bdbh.trim();
			           if(!Pub.empty(bdbh))
			           {
			        	   vo.setBdbh(bdbh);
			        	   bdbh_tj=" and bdbh='"+bdbh+"'  ";
			        	   info[i]+="标段编号："+bdbh+",";
			           }
			           else
			           {
			        	   info[i]+="标段编号：—,";
			        	   bdbh_tj=" and bdid is null  ";
			           }   
			           // 项目编码查询的结果为空，需返回错误提示   update by xiahongbo start
			           String[][]  gc_jh_sj_idArray = (DBUtil.query(conn,"select gc_jh_sj_id from gc_jh_sj where  sfyx=1 "+xmbh_tj+bdbh_tj+nd_tj));
			           String gc_jh_sj_id = null;
			           if (gc_jh_sj_idArray != null && gc_jh_sj_idArray.length != 0) {
			        	   gc_jh_sj_id = gc_jh_sj_idArray[0][0];
			        	   vo.setJhsjid(gc_jh_sj_id);
			           } else {
			        	   info[i]+="导入失败,计划下达中不存在此项目，项目编号【"+xmbh+"】！,1,";
			        	   continue;
			           }
			           // 项目编码查询的结果为空，需返回错误提示   update by xiahongbo end
			            
			            String xdkid[][] = DBUtil.query(conn, "SELECT xmid FROM gc_jh_sj  WHERE gc_jh_sj_id='"+gc_jh_sj_id+"'");
			            if(null!=xdkid&&xdkid.length>0&&!Pub.empty(xdkid[0][0])){
			            	vo.setXdkid(xdkid[0][0]);
			            }
			         
			             String bdid[][] = DBUtil.query(conn, "SELECT bdid FROM gc_jh_sj  WHERE gc_jh_sj_id='"+gc_jh_sj_id+"'");
			            if(null!=bdid&&bdid.length>0&&!Pub.empty(bdid[0][0])){
			            	vo.setBdid(bdid[0][0]);
			            }

			           String  xmmc= getCellFormatValue(3,row.getCell((short) 3));
			           if(!Pub.empty(xmmc))
			           {
			        	   vo.setXmmc(xmmc);
			           }
			           
			           String  bdmc= getCellFormatValue(5,row.getCell((short) 5));
			           if(!Pub.empty(bdmc))
			           {
			        	   vo.setBdmc(bdmc);
			           }   
			           
			            String  kssj= getCellFormatValue(7,row.getCell((short) 7));
			            kssj=kssj.trim();
			            if(!Pub.empty(kssj)){
			            	if(kssj.equals("false"))
			            	{
			            		info[i]+="导入失败,开始时间格式错误！,1,";
			            		continue;
			            	}
			            	else
			            	{
			            		Date date = new SimpleDateFormat("yyyy-MM-dd").parse(kssj); 
			            		vo.setKssj(date);			            		
			            	}	
			            }
			            else
			            {
			            	info[i]+="导入失败 ,开始时间为空！,1,";
			            	continue;
			            }	
			            String  jssj= getCellFormatValue(8,row.getCell((short) 8));
			            jssj=jssj.trim();
			            if(!Pub.empty(jssj)){
			            	if(jssj.equals("false"))
			            	{
			            		info[i]+="导入失败,结束时间格式错误！,1,";
			            		continue;
			            	}
			            	else
			            	{
				            	Date date = new SimpleDateFormat("yyyy-MM-dd").parse(jssj); 
				            	vo.setJssj(date);
			            	}	
			            }
			            else
			            {
			            	info[i]+="导入失败,结束时间为空！,1,";
			            	continue;
			            }	
			            String domresult = this.gczbService.query_ksjs_sj("",user,kssj,jssj,gc_jh_sj_id);
			           if(!Pub.empty(domresult))
			           {
			        	   int f=Integer.parseInt(domresult.substring(4,5));
			        	   switch(f)
			        	   {
			        	   case 1:
			        		   info[i]+="导入失败,该项目已录开始时间为【"+kssj+"】的周报信息！,1,";
			        		   break;
			        	   case 2:
			        		   info[i]+="导入失败,该项目已录结束时间为【"+jssj+"】的周报信息！,1,";
			        		   break;
			        	   case 3:
			        		   info[i]+="导入失败,该项目已录时间区间为【"+kssj+"—"+jssj+"】的周报信息！,1,";
			        		   break;
			        	   }
			        	   continue;
			           }   

			           String  bzjh= getCellFormatValue(9,row.getCell((short) 9));
			           if(!Pub.empty(bzjh))
			           {
			        	   vo.setBzjh(bzjh);
			           } 

			           String  bzwc= getCellFormatValue(10,row.getCell((short) 10));
			           if(!Pub.empty(bzwc))
			           {
			        	   vo.setBzwc(bzwc);
			           } 

			           String  bnwc= getCellFormatValue(11,row.getCell((short) 11));
			           if(!Pub.empty(bnwc))
			           {
			        	   vo.setBnwc(bnwc);
			           } 

			           String  ljwc= getCellFormatValue(12,row.getCell((short) 12));
			           if(!Pub.empty(ljwc))
			           {
			        	   vo.setLjwc(ljwc);
			           } 

			           String  xzjh= getCellFormatValue(13,row.getCell((short) 13));
			           if(!Pub.empty(xzjh))
			           {
			        	   vo.setXzjh(xzjh);
			           } 
			           
			           String  gxmc= getCellFormatValue(14,row.getCell((short) 14));
			           if(!Pub.empty(gxmc))
			           {
			        	   vo.setGxmc(gxmc);
			           }   
			           
			            String  pqwcsj= getCellFormatValue(7,row.getCell((short) 15));
			            pqwcsj=pqwcsj.trim();
			            if(!Pub.empty(pqwcsj)){
			            	if(pqwcsj.equals("false"))
			            	{
			            		info[i]+="导入失败,排迁完成时限格式错误！,1,";
			            		continue;
			            	}
			            	else
			            	{
			            		System.out.println(pqwcsj.length());
			            		Date date = new SimpleDateFormat("yyyy-MM-dd").parse(pqwcsj); 
			            		vo.setPqwcsj(date);            		
			            	}				            	
			            }		           

			           String  pabzjz= getCellFormatValue(16,row.getCell((short) 16));
			           if(!Pub.empty(pabzjz))
			           {
			        	   vo.setPabzjz(pabzjz);
			           } 

			           String  cqwmc= getCellFormatValue(17,row.getCell((short) 17));
			           if(!Pub.empty(cqwmc))
			           {
			        	   vo.setCqwmc(cqwmc);
			           }  

			            String  cqwcsj= getCellFormatValue(7,row.getCell((short) 18));
			            cqwcsj=cqwcsj.trim();
			            if(!Pub.empty(cqwcsj)){
			            	if(cqwcsj.equals("false"))
			            	{
			            		info[i]+="导入失败,拆迁完成时限格式错误！,1,";
			            		continue;
			            	}
			            	else
			            	{
				            	Date date = new SimpleDateFormat("yyyy-MM-dd").parse(cqwcsj); 
				            	vo.setCqwcsj(date);
			            	}	
			            }			           
			           
			           String  cqbzjz= getCellFormatValue(19,row.getCell((short) 19));
			           if(!Pub.empty(cqbzjz))
			           {
			        	   vo.setCqbzjz(cqbzjz);
			           }   
			           			           
			           String  zjlbz= getCellFormatValue(20,row.getCell((short) 20));
			           zjlbz=zjlbz.trim();
			           if(!Pub.empty(zjlbz))
			           {
			        	   if(isNum(zjlbz))
			        	   {
			        		   vo.setZjlbz(zjlbz);			        		   
			        	   }
			        	   else
			        	   {
			            		info[i]+="导入失败,周计量本周完成格式错误！,1,";
			            		continue;			        		   
			        	   }   
			           }   
   			           
					   String  zjlnd= getCellFormatValue(21,row.getCell((short) 21));
					   zjlnd=zjlnd.trim();
					   if(!Pub.empty(zjlnd))
					   {
			        	   if(isNum(zjlnd))
			        	   {			        		   
			        		   vo.setZjlnd(zjlnd);
			        	   }
			        	   else
			        	   {
			            		info[i]+="导入失败 ,周计量本年完成格式错误！,1,";
			            		continue;			        		   
			        	   }   
					   }   
   			           
					   String  zjlljwc= getCellFormatValue(22,row.getCell((short) 22));
					   zjlljwc=zjlljwc.trim();
					   if(!Pub.empty(zjlljwc))
					   {
			        	   if(isNum(zjlljwc))
			        	   {		        		   
			        		   vo.setZjlljwc(zjlljwc);
			        	   }
			        	   else
			        	   {
			            		info[i]+="导入失败,周计量累计完成格式错误！,1,";
			            		continue;			        		   
			        	   }   
					   }   
   			           
					   String  qqwt= getCellFormatValue(23,row.getCell((short) 23));
					   if(!Pub.empty(qqwt))
					   {
						   vo.setQqwt(qqwt);
					   }   
   			           
					   String  htzjwt= getCellFormatValue(24,row.getCell((short) 24));
					   if(!Pub.empty(htzjwt))
					   {
						   vo.setHtzjwt(htzjwt);
					   }   
   			           
					   String  sjwt= getCellFormatValue(25,row.getCell((short) 25));
					   if(!Pub.empty(sjwt))
					   {
						   vo.setSjwt(sjwt);
					   }   
   			           
					   String  zcwt= getCellFormatValue(26,row.getCell((short) 26));
					   if(!Pub.empty(zcwt))
					   {
						   vo.setZcwt(zcwt);
					   }   
   			           
					   String  pqwt= getCellFormatValue(27,row.getCell((short) 27));
					   if(!Pub.empty(pqwt))
					   {
						   vo.setPqwt(pqwt);
					   }   
   			           
					   String  bz= getCellFormatValue(28,row.getCell((short) 28));
					   if(!Pub.empty(bz))
					   {
						   vo.setBz(bz);
					   }

			            String other[][] = DBUtil.query(conn, "SELECT xmglgs,xmdz,jsrw,yzdb,sgdw,jldw FROM GC_tcjh_XMXDK  WHERE GC_tcjh_XMXDK_ID=(select xmid from gc_jh_sj where gc_jh_sj_id='"+gc_jh_sj_id+"')");
			            if(null!=other&&other.length>0){
			            	if(!Pub.empty(other[0][0]))
			            	{
			            		vo.setXmglgs(other[0][0]);			            		
			            	}	
			            	if(!Pub.empty(other[0][1]))
			            	{
			            		vo.setXmdz(other[0][1]);			            		
			            	}
			            	if(!Pub.empty(other[0][2]))
			            	{
			            		vo.setJsrw(other[0][2]);			            		
			            	}
			            	if(!Pub.empty(other[0][3]))
			            	{
			            		vo.setYzdb(other[0][3]);		            		
			            	}
			            	if(!Pub.empty(other[0][4]))
			            	{
			            		vo.setSgdw(other[0][4]);			            		
			            	}
			            	if(!Pub.empty(other[0][5]))
			            	{
			            		vo.setJldw(other[0][5]);			            		
			            	}
			            }
			            vo.setSjmj("1");
			            
			            vo.setYwlx(YwlxManager.GC_XMGLGS_ZB);
						EventVO event = EventManager.createEvent(conn, vo.getYwlx(), user);//生成事件
						vo.setSjbh(event.getSjbh());
						vo.setGc_xmglgs_zbb_id(new RandomGUID().toString()); // 主键
						BusinessUtil.setInsertCommonFields(vo,user);
						info[i]+="导入成功, ,0,";
						BaseDAO.insert(conn, vo);
						count ++;
						
						conn.commit();
			            
			         
			        }
			        String info_text="";
			        for(int text = 2; text <= rowNum; text++)
			        {
			        	info_text+=info[text]+"";
			        }	
			        info_text+=msg+String.valueOf(count)+"条记录。 , , , , , ,0,";
			        //request.setAttribute("info_text", info_text);
			        request.setAttribute("info_text",info_text );
			        mav.setViewName("jsp/business/gcb/zbgl/zbgl_List");
			        
	           return mav;
	       } else {
	           return mav;
	       }
	    	 } catch (Exception e) {
	    		 msg = e.getMessage();
	 			e.printStackTrace(System.out);
	 			if(conn!=null)
	 			DBUtil.rollbackConnetion(conn);
	 		} finally {
	 			if(conn!=null)
	 			DBUtil.closeConnetion(conn);
	 		}
	    	 return mav;
	    }
	    
	    //数字校验
	    public boolean isNum(String msg){
	    	
	    	if(isIntNumber(msg)==false&&isNumber(msg)==false)
	    	{
	    		return false;
	    	}
	    	return true;
	    }	    
	    public static boolean isIntNumber(String str) {//判断整型
	    	   return str.matches("[\\d]+");
	    	}

    	public static boolean isNumber(String str) {//判断小数，与判断整型的区别在与d后面的小数点（红色）
    	   return str.matches("[\\d.]+");
    	}
	    
	    	    
	    private String getCellFormatValue(int cellnum,HSSFCell cell) {
	        String cellvalue = "";
	        if (cell != null) {
	            // 判断当前Cell的Type
	            switch (cell.getCellType()) {
	            // 如果当前Cell的Type为NUMERIC
	            case HSSFCell.CELL_TYPE_NUMERIC:	            	
	            case HSSFCell.CELL_TYPE_FORMULA: {
	            	 if (HSSFDateUtil.isCellDateFormatted(cell)) {    //判断是日期类型
                         SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
                         Date dt = HSSFDateUtil.getJavaDate(cell.getNumericCellValue());//获取成DATE类型   
                           cellvalue = dateformat.format(dt); 
                 }else{
	                cellvalue = String.valueOf(cell.getNumericCellValue());
                 }
	                break;
	            }
	            // 如果当前Cell的Type为STRIN
	            case HSSFCell.CELL_TYPE_STRING:
	                // 取得当前的Cell字符串
	            	if(cellnum==7||cellnum==8||cellnum==15||cellnum==18)
	            	{
	            		Date d = null;
	            		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	            	     try
	            	       {  
	            	         d  =  df.parse(cell.getStringCellValue());  
	            	         cellvalue=cell.getStringCellValue();
	            	       }
	            	       catch(Exception  e)
	            	       {  
	            	         d=new  Date();
	            	         cellvalue="false";
	            	       }  
	            	}
	            	else
	            	{
	            		cellvalue = cell.getStringCellValue();	            		
	            	}	
	                break;
	            // 默认的Cell值
	            default:
	                cellvalue = " ";
	            }
	        } else {
	            cellvalue = "";
	        }
	        return cellvalue;

	    }
	    /**
	     * 获取单元格数据内容为字符串类型的数据
	     * 
	     * @param cell Excel单元格
	     * @return String 单元格数据内容
	     */
	    private String getStringCellValue(HSSFCell cell) {
	        String strCell = "";
	        switch (cell.getCellType()) {
	        case HSSFCell.CELL_TYPE_STRING:
	            strCell = cell.getStringCellValue();
	            break;
	        case HSSFCell.CELL_TYPE_NUMERIC:
	            strCell = String.valueOf(cell.getNumericCellValue());
	            break;
	        case HSSFCell.CELL_TYPE_BOOLEAN:
	            strCell = String.valueOf(cell.getBooleanCellValue());
	            break;
	        case HSSFCell.CELL_TYPE_BLANK:
	            strCell = "";
	            break;
	        default:
	            strCell = "";
	            break;
	        }
	        if (strCell.equals("") || strCell == null) {
	            return "";
	        }
	        if (cell == null) {
	            return "";
	        }
	        return strCell;
	    }
				
}
