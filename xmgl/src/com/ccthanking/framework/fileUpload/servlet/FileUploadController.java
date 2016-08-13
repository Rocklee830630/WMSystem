package com.ccthanking.framework.fileUpload.servlet;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.Connection;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ccthanking.common.FjlbManager;
import com.ccthanking.common.YwlxManager;
import com.ccthanking.framework.Globals;
import com.ccthanking.framework.TwoDimensionCode.TwoDimensionCode;
import com.ccthanking.framework.base.BaseDAO;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.fileUpload.service.FileUploadService;
import com.ccthanking.framework.fileUpload.vo.FileUploadVO;
import com.ccthanking.framework.model.requestJson;
import com.ccthanking.framework.params.ParaManager;
import com.ccthanking.framework.params.SysPara.SysParaConfigureVO;
import com.ccthanking.framework.plugin.AppInit;
import com.ccthanking.framework.util.Pub;
import com.ccthanking.framework.util.RandomGUID;
import com.sun.xml.internal.messaging.saaj.packaging.mime.internet.MimeUtility;


@Controller
@RequestMapping("/fileUploadController")
public class FileUploadController {

//	private final BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
//	private final BlobInfoFactory blobInfoFactory = new BlobInfoFactory();
	/**
	 * 查询文件列表
	 * @param request
	 * @param js
	 */
	@RequestMapping(params = "queryFileList")
	@ResponseBody
	public requestJson queryFileList(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		FileUploadService ser = new FileUploadService();
		String  resultVO = "";
		resultVO = ser.queryFile(request,js);
		j.setMsg(resultVO);
		return j;
	}
	/**
	 * 查询文件列表
	 * @param request
	 * @param js
	 */
	@RequestMapping(params = "queryInfoTable")
	@ResponseBody
	public requestJson queryInfoTable(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		FileUploadService ser = new FileUploadService();
		String  resultVO = "";
		resultVO = ser.queryInfoTable(request,js.getMsg());
		j.setMsg(resultVO);
		return j;
	}
	/**
	 * 删除文件
	 * @param request
	 * @param js
	 */
	@RequestMapping(params = "deleteFile")
	@ResponseBody
	public requestJson deleteFile(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		FileUploadVO vo = new FileUploadVO();
		JSONArray list = vo.doInitJson(js.getMsg());
		vo.setValueFromJson((JSONObject)list.get(0));
//		FileUploadService ser = new FileUploadService();
		String  resultVO = "";
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		vo.setLrr(user.getAccount());//只能删除当前登录人上传的
		FileUploadService.deleteByConditionVO(null, vo);
		j.setMsg(resultVO);
		return j;
	}
	/**
	 * 预览功能
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(params = "doPreview")
	@ResponseBody
	public requestJson doPreview(HttpServletRequest request, HttpServletResponse response) throws Exception{
		requestJson j = new requestJson();
		String fileID = request.getParameter("fileid");
		FileUploadVO condVO = new FileUploadVO();
		Connection conn =DBUtil.getConnection();
		try {
			condVO.setFileid(fileID);
			FileUploadVO vo = (FileUploadVO) BaseDAO.getVOByPrimaryKey(conn,
					condVO);
			SysParaConfigureVO syspara = (SysParaConfigureVO) ParaManager
					.getInstance().getSysParameter("FILEUPLOADROOT");
			String fileRoot = syspara.PARAVALUE1;
			File file = new File(fileRoot + "/" + vo.getUrl());
			if (file.exists()) {
				/**
				int bytes = 0;
				response.setCharacterEncoding("GBK");
				response.setContentType(vo.getFjlx());
				response.setContentLength((int) file.length());
				response.setHeader("Content-Disposition", "inline; filename=\""
						+ MimeUtility.encodeWord(file.getName()) + "\"");
				response.setHeader("Content-type", vo.getFjlx());
				response.setHeader("Accept-Ranges", "bytes");
				response.setHeader("content", "text/html");
				ServletOutputStream op = response.getOutputStream();
				byte[] bbuf = new byte[1024];
				FileInputStream in = new FileInputStream(file);
				while ((in != null) && ((bytes = in.read(bbuf)) != -1)) {
					op.write(bbuf, 0, bytes);
				}
				in.close();
				op.flush();
				op.close();
				*/
				String base = AppInit.appPath+"/file";
				String root = request.getContextPath()+"/file";
				String fileDir = "";
				String result = "";
				// 文件保存路径：根路径/项目ID/附件ID
				fileDir = base + "/" + vo.getYwid() + "/" + fileID;
				// 文件夹不存在的话，需要创建文件夹，否则直接使用
				File copyFile = new File(fileDir);
				if (!copyFile.exists() && !copyFile.isDirectory()) {
					copyFile.mkdirs();
					copyFile = new File(fileDir);
					copyFile.mkdir();
					copyFile = new File(fileDir, vo.getFilename());
					copyFile.createNewFile();
					//-----------------------------------
					int bytes = 0;
					FileOutputStream op = new FileOutputStream(copyFile);
					byte[] bbuf = new byte[1024];
					FileInputStream in = new FileInputStream(file);
					while ((in != null) && ((bytes = in.read(bbuf)) != -1)) {
						op.write(bbuf, 0, bytes);
					}
					in.close();
					op.flush();
					op.close();
					//-----------------------------------
				} else {
					//do nothing
				}
				result = root + "/"+ vo.getYwid() + "/" + fileID+"/"+vo.getFilename();
				j.setMsg(result);
			}
		} catch (Exception e){
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace();
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return j;
	}
	/**
	 * 获取用户图片
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(params = "getFile")
	@ResponseBody
	public void getFile(HttpServletRequest request, HttpServletResponse response) throws Exception{
		String fileID = request.getParameter("fileid");
		FileUploadVO condVO = new FileUploadVO();
		Connection conn =DBUtil.getConnection();
		String account = request.getParameter("account");
		String ywlx = request.getParameter("ywlx");
		try {
			SysParaConfigureVO syspara = (SysParaConfigureVO) ParaManager
					.getInstance().getSysParameter("FILEUPLOADROOT");
			if(YwlxManager.SYSTEM_USER_TX.equals(ywlx)){
				String getFileIDSql = "select fsid from FS_ORG_PERSON_FS where account='"+account+"' and fslb='"+FjlbManager.FS_USER_YHTX+"' and sfyx='1'";
				String arr[][] = DBUtil.query(conn, getFileIDSql);
				syspara = (SysParaConfigureVO) ParaManager.getInstance().getSysParameter("SYSTEM_USER_TX");
				if(arr==null || Pub.empty(arr[0][0])){
					//没查到用户的头像，那么使用表中保存的默认头像
					//fileRoot =  AppInit.appPath;
					condVO.setFileid("000000000000000000000000000000000001");
				}else{
					condVO.setFileid(arr[0][0]);
				}
			}else if(YwlxManager.SYSTEM_USER_QM.equals(ywlx)){
				String getFileIDSql = "select fsid from FS_ORG_PERSON_FS where account='"+account+"' and fslb='"+FjlbManager.FS_USER_YHQM+"' and sfyx='1'";
				String arr[][] = DBUtil.query(conn, getFileIDSql);
				syspara = (SysParaConfigureVO) ParaManager.getInstance().getSysParameter("SYSTEM_USER_QM");
				if(arr==null || Pub.empty(arr[0][0])){
					//没查到用户的头像，那么使用表中保存的默认头像
					//fileRoot =  AppInit.appPath;
					condVO.setFileid("000000000000000000000000000000000001");
				}else{
					condVO.setFileid(arr[0][0]);
				}
			}else{
				condVO.setFileid(fileID);
			}
			String fileRoot = syspara.PARAVALUE1;
			FileUploadVO vo = (FileUploadVO) BaseDAO.getVOByPrimaryKey(conn,
					condVO);
			File file = null;
			if(YwlxManager.SYSTEM_USER_TX.equals(ywlx)){
				//处理用户头像
				file = new File(fileRoot + "/" + condVO.getFileid());
				vo = new FileUploadVO();
				vo.setFjlx("image/jpg");
				if(file.exists()){
				}else{
					file = new File(AppInit.appPath + "/images/Snip20130907_2.png");
				}
				
			}else if(YwlxManager.SYSTEM_USER_QM.equals(ywlx)){
				//处理用户签名
				file = new File(fileRoot + "/" + condVO.getFileid());
				vo = new FileUploadVO();
				vo.setFjlx("image/jpg");
				if(file.exists()){
				}else{
					file = new File(AppInit.appPath + "/images/signature.png");
				}
				
			}else{
				file = new File(fileRoot + "/" + vo.getUrl());
			}
			
			
			if (file.exists()) {
				int bytes = 0;
				response.setCharacterEncoding("GBK");
				response.setContentType(vo.getFjlx());
				response.setContentLength((int) file.length());
				response.setHeader("Content-Disposition", "inline; filename=\""
						+ MimeUtility.encodeWord(file.getName()) + "\"");
				response.setHeader("Content-type", vo.getFjlx());
				response.setHeader("Accept-Ranges", "bytes");
				response.setHeader("content", "text/html");
				ServletOutputStream op = response.getOutputStream();
				byte[] bbuf = new byte[1024];
				FileInputStream in = new FileInputStream(file);
				while ((in != null) && ((bytes = in.read(bbuf)) != -1)) {
					op.write(bbuf, 0, bytes);
				}
				in.close();
				op.flush();
				op.close();
			}
		} catch (Exception e){
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace();
		} finally {
			DBUtil.closeConnetion(conn);
		}
	}
	/**
	 * 生成项目二维码
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(params = "getFileEwm")
	@ResponseBody
	public void getFileEwm(HttpServletRequest request, HttpServletResponse response) throws Exception{
		String GC_TCJH_XMXDK_ID = request.getParameter("id");

		Connection conn =DBUtil.getConnection();
		
		try {
			if(!Pub.empty(GC_TCJH_XMXDK_ID)){
				File file = null;
				file = new File(AppInit.appPath + "/TwoDimensionCode/"+GC_TCJH_XMXDK_ID+".png");
				
				if(file.exists()){//存在则直接输出二维码
					int bytes = 0;
					response.setCharacterEncoding("GBK");
					response.setContentType("image/png");
					response.setContentLength((int) file.length());
					response.setHeader("Content-Disposition", "inline; filename=\""
							+ MimeUtility.encodeWord(file.getName()) + "\"");
					response.setHeader("Content-type", "image/png");
					response.setHeader("Accept-Ranges", "bytes");
					response.setHeader("content", "text/html");
					ServletOutputStream op = response.getOutputStream();
					byte[] bbuf = new byte[1024];
					FileInputStream in = new FileInputStream(file);
					while ((in != null) && ((bytes = in.read(bbuf)) != -1)) {
						op.write(bbuf, 0, bytes);
					}
					in.close();
					op.flush();
					op.close();
				}else{
					String encoderContent = "项目信息";
					//生成新的file并输出
					String imgPath = AppInit.appPath + "/TwoDimensionCode/"+GC_TCJH_XMXDK_ID+".png";
					//获取内容
					//获取内容
					encoderContent = getXdkxx(conn,GC_TCJH_XMXDK_ID);
					 
					TwoDimensionCode handler = new TwoDimensionCode();
					handler.encoderQRCode(encoderContent, imgPath, "png");
					//handler.encoderQRCodeBk(imgPath, "png");
					
					file = new File(AppInit.appPath + "/TwoDimensionCode/"+GC_TCJH_XMXDK_ID+".png");
					if(file.exists()){//存在则直接输出二维码
						int bytes = 0;
						response.setCharacterEncoding("GBK");
						response.setContentType("image/png");
						response.setContentLength((int) file.length());
						response.setHeader("Content-Disposition", "inline; filename=\""
								+ MimeUtility.encodeWord(file.getName()) + "\"");
						response.setHeader("Content-type", "image/png");
						response.setHeader("Accept-Ranges", "bytes");
						response.setHeader("content", "text/html");
						ServletOutputStream op = response.getOutputStream();
						byte[] bbuf = new byte[1024];
						FileInputStream in = new FileInputStream(file);
						while ((in != null) && ((bytes = in.read(bbuf)) != -1)) {
							op.write(bbuf, 0, bytes);
						}
						in.close();
						op.flush();
						op.close();
					}
					
				}
			}

		} catch (Exception e){
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace();
		} finally {
			DBUtil.closeConnetion(conn);
		}
	}
	
	/**
	 * 重新生成二维码功能
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public static void doRegetEwm(Connection conn, String id) throws Exception{
		
		String GC_TCJH_XMXDK_ID = id;
		
		try {
			
			
				if(!Pub.empty(GC_TCJH_XMXDK_ID)){
						String encoderContent = "项目信息";
						//生成新的file并输出
						String imgPath = AppInit.appPath + "/TwoDimensionCode/"+GC_TCJH_XMXDK_ID+".png";
						//获取内容
						encoderContent = getXdkxx(conn,GC_TCJH_XMXDK_ID);
						
						 
						TwoDimensionCode handler = new TwoDimensionCode();
						handler.encoderQRCode(encoderContent, imgPath, "png");
						
		/*				file = new File(AppInit.appPath + "/images/TwoDimensionCode/"+GC_TCJH_XMXDK_ID+".png");
						if(file.exists()){//存在则直接输出二维码
							int bytes = 0;
							response.setCharacterEncoding("GBK");
							response.setContentType("image/png");
							response.setContentLength((int) file.length());
							response.setHeader("Content-Disposition", "inline; filename=\""
									+ MimeUtility.encodeWord(file.getName()) + "\"");
							response.setHeader("Content-type", "image/png");
							response.setHeader("Accept-Ranges", "bytes");
							response.setHeader("content", "text/html");
							ServletOutputStream op = response.getOutputStream();
							byte[] bbuf = new byte[1024];
							FileInputStream in = new FileInputStream(file);
							while ((in != null) && ((bytes = in.read(bbuf)) != -1)) {
								op.write(bbuf, 0, bytes);
							}
							in.close();
							op.flush();
							op.close();
						}*/
					

				}
		} catch (Exception e){
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace();
		} finally {
			DBUtil.closeConnetion(conn);
		}
	}
	/*
	 * 通过项目下达库主键信息返回项目内容
	 * @parame	conn
	 * @parame	id
	 * @return	rsarr[][]
	 */
	public static String  getXdkxx(Connection conn,String xdkid){
		String rscontent = "";
		String rsarr[][] = null;
		//获取内容//
//		String getFileIDSql = "select xmbh,xmmc from GC_TCJH_XMXDK t where gc_tcjh_xmxdk_id ='"+xdkid+"'";
		String getFileIDSql = "select xmbh,xmmc, nd," +
       "(select dic_value from FS_DIC_TREE a where a.parent_id = '1000000000010' and a.dic_code = t.xmlx) xmlx," +
       "(select dept_name from FS_ORG_DEPT a where a.row_id = t.xmglgs) xmglgs," +
       "(select name from FS_ORG_PERSON c where c.account = t.fzr_glgs) glgsfzr," +
       "(select sjhm from FS_ORG_PERSON c where c.account = t.fzr_glgs) lxfs " +
       " from GC_TCJH_XMXDK t where gc_tcjh_xmxdk_id ='"+xdkid+"'";
		rsarr = DBUtil.query(conn, getFileIDSql);
		if(rsarr != null){
			rscontent = "项目编号:" +rsarr[0][0]+ " 项目名称:"+rsarr[0][1];
		}
		
		return rscontent;
	}
	/**
	 * 附件获取业务ID
	 * @param request
	 * @param js
	 */
	@RequestMapping(params = "getYwid")
	@ResponseBody
	public requestJson getYwid(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = new RandomGUID().toString();
		j.setMsg(resultVO);
		return j;
	}
}
