package com.ccthanking.framework.fileUpload.service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.imgscalr.Scalr;
import org.json.JSONArray;
import org.json.JSONObject;


import com.ccthanking.framework.Globals;
import com.ccthanking.framework.base.BaseDAO;
import com.ccthanking.framework.base.BaseVO;
import com.ccthanking.framework.common.BaseResultSet;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.PageManager;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.fileUpload.vo.FileUploadVO;
import com.ccthanking.framework.model.requestJson;
import com.ccthanking.framework.params.ParaManager;
import com.ccthanking.framework.params.SysPara.SysParaConfigureVO;
import com.ccthanking.framework.util.Pub;
import com.ccthanking.framework.util.RandomGUID;
import com.ccthanking.framework.util.RequestUtil;

public class FileUploadService {
	public FileUploadService(){
		SysParaConfigureVO syspara = (SysParaConfigureVO) ParaManager.getInstance().getSysParameter("FILEUPLOADROOT");
		fileRoot = syspara.PARAVALUE1;
	}
	public static String fileRoot = "";
	/**
	 * 
	 * @param request
	 * @param fileRoot 这个参数不再使用了
	 * @return
	 * @throws Exception
	 */
	public JSONArray doInsert(HttpServletRequest request,String fileRoot1) throws Exception{
        ServletFileUpload uploadHandler = new ServletFileUpload(new DiskFileItemFactory());
        String root = request.getContextPath();
        JSONArray json = new JSONArray();
        String ywid = request.getParameter("ywid");
        String fjlb = request.getParameter("fjlb");
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		try{
			String fileID = "";//附件ID
	    	String fileDir = "";//文件路径
	    	String fileName = "";//文件名称
	        List<FileItem> items = uploadHandler.parseRequest(request);
	        if(Pub.empty(ywid)){
	        	ywid = new RandomGUID().toString();
	        }
	        for (FileItem item : items) {
	            if (!item.isFormField()) {
	            	//生成附件ID，40位
	            	fileID = new RandomGUID().toString();
	            	//文件保存路径：根路径/项目ID/附件ID
	            	fileDir = fileRoot + "/"+ywid+"/"+fileID;
	            	//保存到系统的文件名，默认使用上传文件的名字
	            	fileName = item.getName();
	            	if(fjlb=="0038" || "0038".equals(fjlb)){
	            		SysParaConfigureVO syspara = (SysParaConfigureVO) ParaManager.getInstance().getSysParameter("SYSTEM_USER_TX");
	            		fileRoot = syspara.PARAVALUE1;
		            	fileDir = fileRoot;
		            	fileName = fileID+"."+this.getSuffix(fileName);
	            	}else if(fjlb=="0039" || "0039".equals(fjlb)){
	            		SysParaConfigureVO syspara = (SysParaConfigureVO) ParaManager.getInstance().getSysParameter("SYSTEM_USER_QM");
	            		fileRoot = syspara.PARAVALUE1;
		            	fileDir = fileRoot;
		            	fileName = fileID+"."+this.getSuffix(fileName);
	            	}
	                //文件夹不存在的话，需要创建文件夹，否则直接写入文件
	                File file = new File(fileDir);
	                if(!file.exists()&& !file.isDirectory()){
	                	file.mkdirs();
	                	file = new File(fileDir);
	                	file.mkdir();
	                }else{
	                	//do nothing
	                }
	            	file = new File(fileDir, fileName);
	                item.write(file);
					String size = this.resizeImage(file);//调整图片类文件的大小
	                JSONObject jsono = new JSONObject();
	                jsono.put("name", fileName);
	                jsono.put("lrr",user.getName());//附件录入人
	                jsono.put("lrrCode", user.getAccount());
	                jsono.put("size", size);
	                jsono.put("ywid", ywid);		//业务编号
	                jsono.put("fileid", fileID);	//附件编号
	                jsono.put("fileDir", fileDir);	//附件路径
	                jsono.put("fjlb", fjlb);		//附件类别
	                jsono.put("fileType", this.getMimeType(file));//获取附件类型
	                jsono.put("url", root+"/UploadServlet?getfile=" + fileID+"&fileDir="+fileDir+"/"+"/&fileid="+fileID);
	                jsono.put("thumbnail_url", root+"/UploadServlet?getthumb=" + fileID+"&fileDir="+fileDir+"/");
	                jsono.put("delete_url", root+"/UploadServlet?delfile=" + fileID+"&fileDir="+fileDir+"/&fileid="+fileID);
	                jsono.put("modifyflag_url", root+"/UploadServlet?modifyflag=" + fileID+"&fileDir="+fileDir+"/&fileid="+fileID);
	                jsono.put("delete_type", "GET");
	                json.put(jsono);
	            }
	        }
	        insertTable(json,request);
		}catch (Exception e) {
			throw e;
        }
		return json;
	}
	
	public String resizeImage(File file) throws Exception{
		String size = "";
		try{
			String mimetype = this.getMimeType(file);
            File newFile = new File(file.getAbsolutePath());
            if (mimetype.endsWith("png") || mimetype.endsWith("jpeg")|| mimetype.endsWith("jpg")) {
            	//读入图片文件
                BufferedImage im = ImageIO.read(newFile);
                //图片高度超出尺寸时，重置文件大小
                if (im != null && im.getHeight()>1280) {
                    BufferedImage thumb = Scalr.resize(im,1280);//重新定义图片的高度
					FileOutputStream op = new FileOutputStream(newFile);
			        ByteArrayOutputStream out = new ByteArrayOutputStream();
					ImageIO.write(thumb, getSuffix(file.getName()), out);//将图片写入输出流
					byte[] b = out.toByteArray();
			        op.write(b);
			        op.close();
                }
            }
            Double d = new Double(2);
			d = Double.parseDouble(Long.toString(newFile.length()));
			java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");
			size = df.format(d/1000)+"KB";
			if(size.startsWith(".")){
				size = "0"+size;
			}
		}catch(Exception e){
			e.printStackTrace();
			throw e;
		}
		return size;
	}
	/**
	 * 获得VO集合
	 * @param json
	 * @param request
	 * @return
	 * @throws Exception
	 */
	private List<FileUploadVO> doInitVO(JSONArray json,HttpServletRequest request) throws Exception{
		List<FileUploadVO> list = new ArrayList<FileUploadVO>();
		String fjzt = request.getParameter("fjzt");
		String sjbh = request.getParameter("sjbh");//保存记录时，不再记录时间编号和业务类型
		String ywlx = request.getParameter("ywlx");
		String glid1 = request.getParameter("glid1");
		HttpSession session = request.getSession();
		User user = (User)session.getAttribute(Globals.USER_KEY);
		Date scsj = Pub.getCurrentDate();
		try{
			for(int i=0;i<json.length();i++){
				FileUploadVO vo = new FileUploadVO();
				JSONObject jsono = (JSONObject)json.get(i);
				vo.setFileid((String)jsono.get("fileid"));
				vo.setFilename((String)jsono.get("name"));
				vo.setYwid((String)jsono.get("ywid"));
				//处理url
				String fileDir = vo.getYwid()+"/"+vo.getFileid();
				vo.setUrl(fileDir+"/"+(String)jsono.get("name"));
				//区分新增页面的插入附件和查询页面的插入附件
				if(fjzt=="1" || "1".equals(fjzt)){
					vo.setFjzt("1");//表示已生效
				}else{
					vo.setFjzt("0");//表示刚存入，还未生效
				}
				vo.setSjbh(sjbh=Pub.empty(sjbh)||"undefined".equals(sjbh)?"":sjbh);
				vo.setYwlx(ywlx=Pub.empty(ywlx)||"undefined".equals(ywlx)?"":ywlx);
				vo.setGlid1(glid1=Pub.empty(glid1)||"undefined".equals(glid1)?"":glid1);
				vo.setFjlb((String)jsono.get("fjlb"));
				vo.setZhux("1");
				vo.setLrr(user.getAccount());
				vo.setLrbm(user.getDepartment());
				vo.setLrsj(scsj);
				vo.setFjlx((String)jsono.get("fileType"));
				vo.setFilesize(jsono.get("size").toString());
				list.add(vo);
			}
		}catch(Exception e){
			throw e;
		}
		return list;
	}
	/**
	 * 将VO信息存入数据库表
	 * @param json
	 * @param request
	 * @return
	 * @throws Exception
	 */
	private String insertTable(JSONArray json,HttpServletRequest request) throws Exception {
		Connection conn = DBUtil.getConnection();
		String resultVO = null;
		try {
			conn.setAutoCommit(false);
			List<FileUploadVO> list = this.doInitVO(json,request);
			for(FileUploadVO vo:list){
				BaseDAO.insert(conn, vo);
				resultVO = vo.getRowJson();
			}
			conn.commit();
		} catch (Exception e) {
			conn.rollback();
			e.printStackTrace(System.out);
			throw e;
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
		return resultVO;
	}
	/**
	 * 根据业务ID更新附件状态
	 * @param conn
	 * @param ywid
	 * @return
	 * @throws Exception
	 */
	public static boolean updateFjztByYwid(Connection conn,String ywid) throws Exception{
		boolean flag = true;
		try{
			//BaseDAO.delete(conn, vo);
			//conn.commit();
			if(!Pub.empty(ywid)){
				String sql = "update fs_fileupload set fjzt='1'"+
						" where ywid='"+ywid+"' and fjzt='0'";
				DBUtil.execUpdateSql(conn, sql);
			}else{
				flag = false;
			}
		}catch(Exception e){
			e.printStackTrace();
			throw e;
		}finally {
			//传入的conn不由我关闭
		}
		return flag;
	}
	/**
	 * 根据FileUploadVO更新附件状态
	 * @param conn
	 * @param ywid
	 * @return
	 * @throws Exception
	 */
	public static boolean updateFjztByVO(Connection conn,FileUploadVO vo) throws Exception{
		boolean flag = true;
		try{
			if(!Pub.empty(vo.getYwid())){
				String sql = "update fs_fileupload set fjzt='1'," +
						"glid1='"+vo.getGlid1()+"'," +
						"sjbh='"+vo.getSjbh()+"'," +
						"ywlx='"+vo.getYwlx()+"' " +
						" where ywid='"+vo.getYwid()+"' and fjzt='0'";
				DBUtil.execUpdateSql(conn, sql);
			}else{
				flag = false;
			}
		}catch(Exception e){
			e.printStackTrace();
			throw e;
		}finally {
			//传入的conn不由我关闭
		}
		return flag;
	}
	/**
	 * 根据YWID更新附件信息
	 * @param conn
	 * @param vo
	 * @param ywid
	 * @return
	 * @throws Exception
	 */
	public static boolean updateVOByYwid(Connection conn,FileUploadVO vo,String ywid) throws Exception{
		boolean flag = true;
		try{
			String sql = "select FILEID from FS_FILEUPLOAD where ywid='"+ywid+"'";
			String[][] arr = DBUtil.query(conn, sql);
			if(arr!=null){
				for(int i=0;i<arr.length;i++){
					vo.setFileid(arr[i][0]);
					BaseDAO.update(conn, vo);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			throw e;
		}finally {
			//传入的conn不由我关闭
		}
		return flag;
	}
	/**
	 * 根据YWID更新附件信息
	 * @param conn
	 * @param vo
	 * @param ywid
	 * @return
	 * @throws Exception
	 */
	public static boolean updateVOByYwid(Connection conn,FileUploadVO vo,String ywid,User user) throws Exception{
		boolean flag = true;
		try{
			String sql = "select FILEID from FS_FILEUPLOAD where ywid='"+ywid+"' and lrr='"+user.getAccount()+"' and fjzt='0'" +
					" union all " +
					" select FILEID from FS_FILEUPLOAD where ywid='"+ywid+"' and fjzt='1'";
			String[][] arr = DBUtil.query(conn, sql);
			if(arr!=null){
				for(int i=0;i<arr.length;i++){
					String getWybhSql = "select sjwybh from GC_JH_SJ where GC_JH_SJ_ID='"+vo.getGlid2()+"'";
					String wybhArr[][] = DBUtil.query(conn, getWybhSql);
					if(!Pub.empty(vo.getGlid2()) && wybhArr!=null){
						//如果传入了计划数据ID，那么要把附件表的唯一编号保存上
						vo.setWybh(wybhArr[0][0]);
					}
					vo.setFileid(arr[i][0]);
					vo.setGxr(user.getAccount());
					vo.setGxbm(user.getDepartment());
					vo.setGxsj(Pub.getCurrentDate());
					BaseDAO.update(conn, vo);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			throw e;
		}finally {
			//传入的conn不由我关闭
		}
		return flag;
	}
	/**
	 * 根据GLID1更新附件信息
	 * @param conn
	 * @param vo
	 * @param glid1
	 * @return
	 * @throws Exception
	 */
	public static boolean updateVOByGlid1(Connection conn,FileUploadVO vo,String glid1,User user) throws Exception{
		boolean flag = true;
		try{
			String sql = "select FILEID from FS_FILEUPLOAD where glid1='"+glid1+"' and lrr='"+user.getAccount()+"' and fjzt='0'" +
					" union all " +
					" select FILEID from FS_FILEUPLOAD where glid1='"+glid1+"' and fjzt='1'";
			String[][] arr = DBUtil.query(conn, sql);
			if(arr!=null){
				for(int i=0;i<arr.length;i++){
					String getWybhSql = "select sjwybh from GC_JH_SJ where GC_JH_SJ_ID='"+vo.getGlid2()+"'";
					String wybhArr[][] = DBUtil.query(conn, getWybhSql);
					if(!Pub.empty(vo.getGlid2()) && wybhArr!=null){
						//如果传入了计划数据ID，那么要把附件表的唯一编号保存上
						vo.setWybh(wybhArr[0][0]);
					}
					vo.setFileid(arr[i][0]);
					vo.setGxr(user.getAccount());
					vo.setGxbm(user.getDepartment());
					vo.setGxsj(Pub.getCurrentDate());
					BaseDAO.update(conn, vo);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			throw e;
		}finally {
			//传入的conn不由我关闭
		}
		return flag;
	}
	/**
	 * 根据FileUploadVO更新附件信息
	 * @param conn
	 * @param vo
	 * @param condVO
	 * @return
	 * @throws Exception
	 */
	public static boolean updateVOByCondVO(Connection conn,FileUploadVO vo,FileUploadVO condVO) throws Exception{
		boolean flag = true;
		int createFlag = 0;
		try{
			BaseVO[] bv = null;
			if(conn==null){
				createFlag = 1;
				conn = DBUtil.getConnection();
			}
			if(condVO!=null&&!condVO.isEmpty()){
				bv = (BaseVO [])BaseDAO.getVOByCondition(conn, condVO);
			}
			if(bv!=null){
				for(int i=0;i<bv.length;i++){
					FileUploadVO newvo = (FileUploadVO)bv[i];
					vo.setFileid(newvo.getFileid());
					BaseDAO.update(conn, vo);
				}
			}
			if(createFlag==1){
				//本方法创建的conn由本方法提交
				conn.commit();
			}
		}catch(Exception e){
			if(createFlag==1){
				//本方法创建的conn由本方法回撤
				DBUtil.rollbackConnetion(conn);
			}
			e.printStackTrace();
			throw e;
		}finally{
			if(createFlag==1){
				//本方法创建的conn由本方法关闭
				DBUtil.closeConnetion(conn);
			}
		}
		return flag;
	}
	/**
	 * 根据业务ID更新附件状态
	 * @param conn
	 * @param ywid
	 * @return
	 * @throws Exception
	 */
	public static boolean updateFjztByYwid(String ywid) throws Exception{
		boolean flag = true;
		Connection conn = null;
		try{
			conn = DBUtil.getConnection();
			if(!Pub.empty(ywid)){
				String sql = "update fs_fileupload set fjzt='1'"+
					" where ywid='"+ywid+"' and fjzt='0'";
				DBUtil.execUpdateSql(conn, sql);
			}else{
				flag = false;
			}
			conn.commit();
		}catch(Exception e){
			e.printStackTrace();
			throw e;
		}finally {
			if (conn != null) {
				conn.close();
			}
		}
		return flag;
	}
	/**
	 * 根据FileUploadVO更新附件状态
	 * @param conn
	 * @param ywid
	 * @return
	 * @throws Exception
	 */
	public static boolean updateFjztByVO(FileUploadVO vo) throws Exception{
		boolean flag = true;
		Connection conn = null;
		try{
			conn = DBUtil.getConnection();
			if(!Pub.empty(vo.getYwid())){
				String sql = "update fs_fileupload set fjzt='1'," +
						"glid1='"+vo.getGlid1()+"'," +
						"sjbh='"+vo.getSjbh()+"'," +
						"ywlx='"+vo.getYwlx()+"' " +
						" where ywid='"+vo.getYwid()+"' and fjzt='0'";
				DBUtil.execUpdateSql(conn, sql);
			}else{
				flag = false;
			}
			conn.commit();
		}catch(Exception e){
			e.printStackTrace();
			throw e;
		}finally {
			if (conn != null) {
				conn.close();
			}
		}
		return flag;
	}
	/**
	 * 根据新增的业务编号更新附件状态
	 * @param conn
	 * @param newywid,oldywid
	 * @return
	 * @throws Exception
	 */
	public static boolean updateFjztByYwid(Connection conn,String newywid,String oldywid) throws Exception{
		boolean flag = true;
		//Connection conn = null;
		try{
			//conn = DBUtil.getConnection();
			if(!Pub.empty(newywid)){
				String sql = "update fs_fileupload set fjzt='1'," +
						"ywid='"+newywid+"'" +
						//"sjbh='"+vo.getSjbh()+"'," +
						//"ywlx='"+vo.getYwlx()+"' " +
						" where ywid='"+oldywid+"'";
				DBUtil.execUpdateSql(conn, sql);
			}else{
				flag = false;
			}
			//conn.commit();
		}catch(Exception e){
			e.printStackTrace();
			throw e;
		}finally {
			//if (conn != null) {
			//	conn.close();
			//}
		}
		return flag;
	}
	/**
	public static boolean deleteByYwid(Connection conn,String ywid) throws Exception{
		boolean b = false;
		try{
			if(conn==null){
				conn = DBUtil.getConnection();
			}
			String sql = "select FILEID,FILENAME,URL from fs_fileupload where ywid='"+ywid+"'";
			String[][] arr = DBUtil.query(conn, sql);
			if(arr!=null){
				for(int i=0;i<arr.length;i++){
					FileUploadVO vo = new FileUploadVO();
					vo.setFileid(arr[i][0]);
					BaseDAO.delete(conn, vo);
		        	//1.先删除文件
		            File file = new File(fileRoot+"/"+arr[i][2]);
		            if (file.exists()) {
		                file.delete(); // TODO:check and report success
		            }
				}
			}
			b = true;
		}catch(Exception e){
			e.printStackTrace();
			throw e;
		}
		return b;
	}
	*/
	public static boolean deleteByConditionVO(Connection conn,FileUploadVO condVO) throws Exception{
		boolean b = false;
		int createFlag = 0;
		try{
			BaseVO[] bv = null;
			if(conn==null){
				createFlag = 1;
				conn = DBUtil.getConnection();
			}
			if(condVO!=null&&!condVO.isEmpty()){
				bv = (BaseVO [])BaseDAO.getVOByCondition(conn, condVO);
			}
			if(bv!=null){
				for(int i=0;i<bv.length;i++){
					FileUploadVO vo = (FileUploadVO)bv[i];
					if("0".equals(vo.getFjzt())){
						//如果附件状态为0，那么删除附件
						BaseDAO.delete(conn, vo);
			        	//1.先删除文件
			            File file = new File(fileRoot+"/"+vo.getUrl());
			            if (file.exists()) {
			                file.delete(); // TODO:check and report success
			            }
					}else{
						//如果附件状态不为0，那么把附件状态置为2（被删除）,文件和记录都保留
						vo.setFjzt("2");
						BaseDAO.update(conn, vo);
					}
				}
			}
			b = true;
			if(createFlag==1){
				//本方法创建的conn由本方法提交
				conn.commit();
			}
		}catch(Exception e){
			if(createFlag==1){
				//本方法创建的conn由本方法回撤
				DBUtil.rollbackConnetion(conn);
			}
			e.printStackTrace();
			throw e;
		}finally{
			if(createFlag==1){
				//本方法创建的conn由本方法关闭
				DBUtil.closeConnetion(conn);
			}
		}
		return b;
	}
	/**
	 * 附件删除方法
	 * @return
	 * @throws Exception
	 */
	public String delete(FileUploadVO vo) throws Exception{
		Connection conn = null;
		String resultVO = null;
		try{
			conn = DBUtil.getConnection();
			BaseDAO.delete(conn, vo);
			conn.commit();
		}catch(Exception e){
			e.printStackTrace();
			DBUtil.rollbackConnetion(conn);
		}finally {
			DBUtil.closeConnetion(conn);
		}
		return resultVO;
	}
	/**
	 * 附件修改方法
	 * @return
	 * @throws Exception
	 */
	public String update(FileUploadVO vo) throws Exception{
		Connection conn = null;
		String resultVO = null;
		try{
			conn = DBUtil.getConnection();
			BaseDAO.update(conn, vo);
			conn.commit();
		}catch(Exception e){
			e.printStackTrace();
			DBUtil.rollbackConnetion(conn);
		}finally {
			DBUtil.closeConnetion(conn);
		}
		return resultVO;
	}
	/**
	 * 附件修改方法
	 * @return
	 * @throws Exception
	 */
	public FileUploadVO getLastFileVO(FileUploadVO condVO) throws Exception{
		Connection conn = null;
		BaseVO[] bv = null;
		try{
			conn = DBUtil.getConnection();
			String sql = "select fileid from fs_fileupload where ywid='"+condVO.getYwid()+"' and fjlb='"+condVO.getFjlb()+"' order by lrsj desc";
			String arr[][] = DBUtil.query(conn, sql);
			if(arr!=null){
				condVO.setFileid(arr[0][0]);
				bv = (BaseVO [])BaseDAO.getVOByCondition(conn, condVO);
			}
			conn.commit();
		}catch(Exception e){
			e.printStackTrace();
			DBUtil.rollbackConnetion(conn);
		}finally {
			DBUtil.closeConnetion(conn);
		}
		if(bv==null){
			return new FileUploadVO();
		}else{
			return (FileUploadVO)bv[0];
		}
	}
	/**
	 * 根据项目ID查询附件方法
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public String queryFile(HttpServletRequest request,requestJson js) throws Exception{
        JSONArray json = new JSONArray();
        String root = request.getContextPath();
		Connection conn = null;
		String domresult = "";
		try{
			conn = DBUtil.getConnection();
			List<FileUploadVO> list = this.getFileUploadVOByJson(conn,js);
			for(FileUploadVO vo:list){
				String fileDir = fileRoot+"/"+vo.getUrl().substring(0, vo.getUrl().indexOf(vo.getFilename()));
				JSONObject jsono = new JSONObject();
				jsono.put("name", vo.getFilename());
				jsono.put("size", vo.getFilesize());//暂时不准备使用大小
//				jsono.put("xmid", xmid);		//项目编号
                jsono.put("lrrCode", vo.getLrr());
				jsono.put("ywid", vo.getYwid());
				jsono.put("lrr", Pub.getUserNameByLoginId(vo.getLrr()));
				jsono.put("lrsj", Pub.getDate("yyyy-MM-dd HH24:MM:SS",vo.getLrsj()));
				jsono.put("fileid", vo.getFileid());			//附件编号
				jsono.put("fileDir", fileRoot+"/"+vo.getUrl());	//附件路径
				jsono.put("fileType", vo.getFjlx());			//获取附件类型
				jsono.put("fjlb", vo.getFjlb());				//附件类别
				jsono.put("url", root+"/UploadServlet?getfile=" + vo.getFileid()+"&fileDir="+fileDir+"&fileid="+vo.getFileid());
				jsono.put("thumbnail_url", root+"/UploadServlet?getthumb=" + vo.getFileid()+"&fileDir="+fileDir);
				jsono.put("delete_url", root+"/UploadServlet?delfile=" + vo.getFileid()+"&fileDir="+fileDir+"&fileid="+vo.getFileid());
				jsono.put("delete_type", "GET");
				json.put(jsono);
			}
			domresult = json.toString();
		}catch(Exception e){
			e.printStackTrace();
			throw e;
		}finally{
			conn.close();
		}
        return domresult;
	}
	/**
	 * 这个是临时使用的方法，由于BaseDAO.getVOByCondition方法不能使用，只能先自己查询了
	 * @param conn	数据库连接
	 * @param js	前台传入条件
	 * @return
	 */
	private List<FileUploadVO> getFileUploadVOByJson(Connection conn,requestJson js) throws Exception{
//		FileUploadVO condVO = new FileUploadVO();
//		List list = new ArrayList();
//		try{
//			net.sf.json.JSONArray tlist = condVO.doInitJson(js.getMsg());
//			condVO.setValueFromJson((net.sf.json.JSONObject)tlist.get(0));
//			//保证至少存在一个条件，否则不允许查询
//			if(!(Pub.empty(condVO.getYwid()) && Pub.empty(condVO.getGlid1()) && Pub.empty(condVO.getYwlx()) && Pub.empty(condVO.getSjbh()))){
//				condVO.setFjzt("1");
//				BaseVO[] bv = (BaseVO [])BaseDAO.getVOByCondition(conn, condVO);
//				if(bv!=null){
//					for(int i=0;i<bv.length;i++){
//						FileUploadVO vo = (FileUploadVO)bv[i];
//						list.add(vo);
//					}
//				}
//			}
//		}catch(Exception e){
//			e.printStackTrace();
//			
//		}
//		return list;
		ResultSet rs = null;
		ResultSetMetaData md = null;
		PreparedStatement ps = null;
		List<FileUploadVO> list = new ArrayList<FileUploadVO>();
		try{
			String sql = "select fileid, filename, url, zhux, bz, lrr, lrbm, lrsj, gxr, gxbm, gxsj, sjbh, ywlx, fjlx, filesize, ywid, fjzt, glid1, glid2, glid3, glid4, fjlb " +
					"from fs_fileupload " +
					"where fjzt='1' ";
			FileUploadVO condVO = new FileUploadVO();
			net.sf.json.JSONArray tlist = condVO.doInitJson(js.getMsg());
			condVO.setValueFromJson((net.sf.json.JSONObject)tlist.get(0));
			String cond = "";
			//保证至少存在一个条件，否则不允许查询
			if(!(Pub.empty(condVO.getYwid()) && Pub.empty(condVO.getGlid1()) && Pub.empty(condVO.getYwlx()) && Pub.empty(condVO.getSjbh())&& Pub.empty(condVO.getWybh()))){
				//处理业务ID条件
				if(!Pub.empty(condVO.getYwid())){
					if(condVO.getYwid().indexOf(",")!=-1){
						String[] tempArr = condVO.getYwid().split(",");
						for(int i=0;i<tempArr.length;i++){
							cond += "  ywid='"+tempArr[i]+"' or ";
						}
						cond = " and ("+cond.substring(0,cond.length()-3)+")";
					}else{
						cond += " and ywid='"+condVO.getYwid()+"'";
					}
				}
		//		这些条件暂时不使用了
				//处理文件编号条件
				if(!Pub.empty(condVO.getFileid())){
					if(condVO.getFileid().indexOf(",")!=-1){
						String[] tempArr = condVO.getFileid().split(",");
						for(int i=0;i<tempArr.length;i++){
							cond += "  fileid='"+tempArr[i]+"' or ";
						}
						cond = " and ("+cond.substring(0,cond.length()-3)+")";
					}else{
						cond += " and fileid='"+condVO.getFileid()+"'";
					}
				}
				//处理文件名称条件
				if(!Pub.empty(condVO.getFilename())){
					if(condVO.getFilename().indexOf(",")!=-1){
						String[] tempArr = condVO.getFilename().split(",");
						for(int i=0;i<tempArr.length;i++){
							cond += "  filename='"+tempArr[i]+"' or ";
						}
						cond = " and ("+cond.substring(0,cond.length()-3)+")";
					}else{
						cond += " and filename='"+condVO.getFilename()+"'";
					}
				}
				//处理附件类型条件
				if(!Pub.empty(condVO.getFjlx())){
					if(condVO.getFjlx().indexOf(",")!=-1){
						String[] tempArr = condVO.getFjlx().split(",");
						for(int i=0;i<tempArr.length;i++){
							cond += "  fjlx='"+tempArr[i]+"' or ";
						}
						cond = " and ("+cond.substring(0,cond.length()-3)+")";
					}else{
						cond += " and fjlx='"+condVO.getFjlx()+"'";
					}
				}
				//处理事件编号条件
				if(!Pub.empty(condVO.getSjbh())){
					if(condVO.getSjbh().indexOf(",")!=-1){
						String[] tempArr = condVO.getSjbh().split(",");
						for(int i=0;i<tempArr.length;i++){
							cond += "  sjbh='"+tempArr[i]+"' or ";
						}
						cond = " and ("+cond.substring(0,cond.length()-3)+")";
					}else{
						cond += " and sjbh='"+condVO.getSjbh()+"'";
					}
				}
				//处理业务类型条件
				if(!Pub.empty(condVO.getYwlx())){
					if(condVO.getYwlx().indexOf(",")!=-1){
						String[] tempArr = condVO.getYwlx().split(",");
						for(int i=0;i<tempArr.length;i++){
							cond += "  ywlx='"+tempArr[i]+"' or ";
						}
						cond = " and ("+cond.substring(0,cond.length()-3)+")";
					}else{
						cond += " and ywlx='"+condVO.getYwlx()+"'";
					}
				}
				//处理关联ID1条件
				if(!Pub.empty(condVO.getGlid1())){
					if(condVO.getGlid1().indexOf(",")!=-1){
						String[] tempArr = condVO.getGlid1().split(",");
						for(int i=0;i<tempArr.length;i++){
							cond += "  glid1='"+tempArr[i]+"' or ";
						}
						cond = " and ("+cond.substring(0,cond.length()-3)+")";
					}else{
						cond += " and glid1='"+condVO.getGlid1()+"'";
					}
				}
				//处理唯一编号条件
				if(!Pub.empty(condVO.getWybh())){
					if(condVO.getWybh().indexOf(",")!=-1){
						String[] tempArr = condVO.getWybh().split(",");
						for(int i=0;i<tempArr.length;i++){
							cond += "  wybh='"+tempArr[i]+"' or ";
						}
						cond = " and ("+cond.substring(0,cond.length()-3)+")";
					}else{
						cond += " and wybh='"+condVO.getWybh()+"'";
					}
				}
				//处理条件为空的情况
				if(cond==""){
					cond = " and 1=0 ";
				}
				sql = sql+cond;
				ps = conn.prepareStatement(sql);
				rs = ps.executeQuery();
				md = rs.getMetaData();
				while(rs.next()){
					FileUploadVO res = new FileUploadVO();
					for (int i = 1; i <= md.getColumnCount(); i++) {
						String colname = md.getColumnName(i).toUpperCase();
						if (!res.isValidField(colname))
							continue;
						int coltype = md.getColumnType(i);
						if (coltype == java.sql.Types.DATE
								|| coltype == java.sql.Types.TIMESTAMP) {
							if (rs.getDate(i) != null)
								res.put(colname, rs.getDate(i));
						} else if (coltype == java.sql.Types.BLOB) {
							Blob dbBlob;
							dbBlob = rs.getBlob(i);
							if (dbBlob == null)
								continue;
							int length = (int) dbBlob.length();
							byte[] buffer = dbBlob.getBytes(1, length);
							res.put(colname, buffer);
						} else if (coltype == java.sql.Types.NULL) {
							// res.put(colname,null);
						} else {
							if (rs.getString(i) != null){
								res.put(colname, rs.getString(i));
							}
						}
					}
					list.add(res);
				}
			}
		}catch(Exception e ){
			throw e;
		}finally{
			if(rs!=null){
				rs.close();
			}
			if(ps!=null){
				ps.close();
			}
		}
		return list;
	} 
	/**
	 * 获取文件扩展类型
	 * @param file
	 * @return
	 */
	public String getMimeType(File file) {
        String mimetype = "";
        if (file.exists()) {
            if (getSuffix(file.getName()).equalsIgnoreCase("png")) {
                mimetype = "image/png";
            }else if(getSuffix(file.getName()).equalsIgnoreCase("jpg")){
                mimetype = "image/jpg";
            }else if(getSuffix(file.getName()).equalsIgnoreCase("jpeg")){
                mimetype = "image/jpeg";
            }else if(getSuffix(file.getName()).equalsIgnoreCase("gif")){
                mimetype = "image/gif";
            }else {
                javax.activation.MimetypesFileTypeMap mtMap = new javax.activation.MimetypesFileTypeMap();
                mimetype  = mtMap.getContentType(file);
            }
        }
        return mimetype;
    }
	/**
	 * 
	 * @param filename
	 * @return
	 */
    private String getSuffix(String filename) {
        String suffix = "";
        int pos = filename.lastIndexOf('.');
        if (pos > 0 && pos < filename.length() - 1) {
            suffix = filename.substring(pos + 1);
        }
        return suffix;
    }
    /**
     * 
     */
	public static ArrayList getUploadFiles(String ywid,String fjlb){
    	ArrayList arr = new ArrayList();
    	String file_sql = "select  FILEID,FILENAME,BZ from fs_fileupload where ywid = '"+ywid+"' and fjzt='1' ";
    	if(!Pub.empty(fjlb)){
    		if(fjlb.indexOf(",")!=-1){
    			file_sql +=" and fjlb in ('"+fjlb.replaceAll("\\,", "\\','")+"')";
    		}else{
    			file_sql +=" and fjlb='"+fjlb+"'";
    		}
    	}
    	file_sql += " order by LRSJ desc";
    	String[][] a =DBUtil.query(file_sql);
    //	String fileid = "";
    	String[] fileid = null;
    	String[] filenames = null;
    	if(a!=null&&a.length>0)
    	{
    		filenames = new String[a.length];
    		fileid = new String[a.length];
    		
    		for (int i =0;i<a.length;i++)
    		{
    			if(!Pub.empty(a[i][2]))
    			{
    				filenames[i] = a[i][2];
    			}else{
    				fileid[i] = a[i][0];
    				filenames[i] = a[i][1];
    			}
    		}
    		
    	}
    	arr.add(0,fileid);
    	arr.add(1,filenames);
    	
    	
    	return arr;
    }
	public static ArrayList getUploadFilesWithWybh(String wybh,String fjlb){
    	ArrayList arr = new ArrayList();
    	String file_sql = "select  FILEID,FILENAME,BZ from fs_fileupload where wybh = '"+wybh+"' and fjzt='1' ";
    	if(!Pub.empty(fjlb)){
    		if(fjlb.indexOf(",")!=-1){
    			file_sql +=" and fjlb in ('"+fjlb.replaceAll("\\,", "\\','")+"')";
    		}else{
    			file_sql +=" and fjlb='"+fjlb+"'";
    		}
    	}
    	file_sql += " order by LRSJ desc";
    	String[][] a =DBUtil.query(file_sql);
    //	String fileid = "";
    	String[] fileid = null;
    	String[] filenames = null;
    	if(a!=null&&a.length>0){
    		filenames = new String[a.length];
    		fileid = new String[a.length];
    		
    		for (int i =0;i<a.length;i++)
    		{
    			if(!Pub.empty(a[i][2]))
    			{
    				filenames[i] = a[i][2];
    			}else{
    				fileid[i] = a[i][0];
    				filenames[i] = a[i][1];
    			}
    		}
    		
    	}
    	arr.add(0,fileid);
    	arr.add(1,filenames);
    	return arr;
    }
    public boolean doTransfer(Connection conn,FileUploadVO condVO){
    	boolean b = false;
    	
//		String fileID = request.getParameter("fileid");
//		FileUploadVO condVO = new FileUploadVO();
    	if(conn==null){
    		conn=DBUtil.getConnection();
    	}
		try {
			String sql = "select fileid from FS_FILEUPLOAD where ywid='"+condVO.getYwid()+"' and fjlb='"+condVO.getFjlb()+"'";
			String arr[][] = DBUtil.query(conn, sql);
			condVO.setFileid(arr[0][0]);
			FileUploadVO vo = (FileUploadVO) BaseDAO.getVOByPrimaryKey(conn,
					condVO);
			File oldFile = new File(fileRoot + "/" + vo.getUrl());
			if (oldFile.exists()) {
//				String base = AppInit.appPath+"/file";
//				String root = request.getContextPath()+"/file";
//				String oldFileDir = "";
				String newFileDir = "";
//				String result = "";
				// 文件保存路径：根路径/项目ID/附件ID
//				oldFileDir = oldFileRoot + "/" + vo.getYwid() + "/" + vo.getFileid();
				newFileDir = condVO.getUrl();
				// 文件夹不存在的话，需要创建文件夹，否则直接使用
				File copyFile = new File(newFileDir);
				if (!copyFile.exists() && !copyFile.isDirectory()) {
					copyFile.mkdirs();
					copyFile = new File(newFileDir);
					copyFile.mkdir();
					
				} else {
					copyFile.delete();
				}
				copyFile = new File(newFileDir, condVO.getFilename());
				copyFile.createNewFile();
				//-----------------------------------
				int bytes = 0;
				FileOutputStream op = new FileOutputStream(copyFile);
				byte[] bbuf = new byte[1024];
				FileInputStream in = new FileInputStream(oldFile);
				while ((in != null) && ((bytes = in.read(bbuf)) != -1)) {
					op.write(bbuf, 0, bytes);
				}
				in.close();
				op.flush();
				op.close();
				//-----------------------------------
				b=true;
//				result = root + "/"+ vo.getYwid() + "/" + fileID+"/"+vo.getFilename();
//				j.setMsg(result);
			}
		} catch (Exception e){
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace();
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return b;
    }
    public String queryInfoTable(HttpServletRequest request,String json) throws Exception {
		// TODO Auto-generated method stub
		Connection conn = null;
		String domresult = "";
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		try {
			conn = DBUtil.getConnection();
			PageManager page = RequestUtil.getPageManager(json);
			String orderFilter = RequestUtil.getOrderFilter(json);
			String condition = RequestUtil.getConditionList(json).getConditionWhere();
			String roleCond = " and FJZT='1' ";
			String sql = "select fileid, filename, url, zhux, bz, lrr, lrbm, lrsj, gxr, gxbm, gxsj, sjbh, ywlx, fjlx, filesize, ywid, fjzt, glid1, glid2, glid3, glid4, fjlb from fs_fileupload";
			condition +=roleCond;
			condition +=orderFilter;
			page.setFilter(condition);
			conn.setAutoCommit(false);
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldDateFormat("LRSJ", "yyyy-MM-dd HH:mm");
			bs.setFieldOrgDept("LRBM");
			bs.setFieldUserID("LRR");
			domresult = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace(System.out);
			throw e;
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
    
}
