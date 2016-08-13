package com.ccthanking.framework.fileUpload.servlet;

import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.imgscalr.Scalr;
import org.json.JSONArray;

import com.ccthanking.framework.base.BaseDAO;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.fileUpload.service.FileUploadService;
import com.ccthanking.framework.fileUpload.vo.FileUploadVO;
import com.ccthanking.framework.params.ParaManager;
import com.ccthanking.framework.params.SysPara.SysParaConfigureVO;
import com.sun.xml.internal.messaging.saaj.packaging.mime.internet.MimeUtility;

public class FileUploadServlet extends HttpServlet {
	
	//文件上传到服务器端的根目录
	private String fileRoot = "";
	
    @Override
	public void init(ServletConfig config) throws ServletException {
    	try{
    		//获取文件保存的根路径
    		SysParaConfigureVO syspara = (SysParaConfigureVO) ParaManager.getInstance().getSysParameter("FILEUPLOADROOT");
    		fileRoot = syspara.PARAVALUE1;
    	}catch(Exception e){
    		
    	}finally{
    		
    	}
	}
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	String fileDir = request.getParameter("fileDir");
    	FileUploadService ser = new FileUploadService();
        FileUploadVO vo = new FileUploadVO();
        String encoding = request.getCharacterEncoding();
        response.setCharacterEncoding(encoding);
    	String fileID = request.getParameter("fileid");
    	try{
			String sql = "select FILENAME from FS_FILEUPLOAD where FILEID='"+fileID+"'";
			String arr[][] = DBUtil.query(sql);
	        if (request.getParameter("getfile") != null && !request.getParameter("getfile").isEmpty()) {
	        	//获取文件，下载，点击缩略图或者文件名时调用
	            File file = new File(fileDir + (arr==null?request.getParameter("getfile"):arr[0][0]));
	            if (file.exists()) {
	                int bytes = 0;
	                ServletOutputStream op = response.getOutputStream();
	                
	                response.setContentType(ser.getMimeType(file));
	                response.setContentLength((int) file.length());
	                response.setHeader( "Content-Disposition", "inline; filename=\"" + MimeUtility.encodeWord(file.getName()) + "\"" );
	                byte[] bbuf = new byte[1024];
	                DataInputStream in = new DataInputStream(new FileInputStream(file));
	                while ((in != null) && ((bytes = in.read(bbuf)) != -1)) {
	                    op.write(bbuf, 0, bytes);
	                }
	                in.close();
	                op.flush();
	                op.close();
	            }
	        } else if (request.getParameter("delfile") != null && !request.getParameter("delfile").isEmpty()) {
	        	//删除文件，点击“删除”按钮时调用
	        	//1.先删除文件
	            File file = new File(fileDir + (arr==null?request.getParameter("delfile"):arr[0][0]));
	            if (file.exists()) {
	                file.delete(); // TODO:check and report success
	            }
	            //2.删除表中记录
	            vo.setFileid(fileID);
	            ser.delete(vo);
	        } else if (request.getParameter("modifyflag") != null && !request.getParameter("modifyflag").isEmpty()) {
	        	//修改文件标志位，点击“删除”按钮时调用
	            //只修改附件状态，不删除任何信息
	            vo.setFileid(fileID);
	            vo.setGlid1("");
	            vo.setFjzt("0");
	            ser.update(vo);
	        } else if (request.getParameter("getthumb") != null && !request.getParameter("getthumb").isEmpty()) {
	        	//获取图片类文件的缩略图
	            File file = new File(fileDir + (arr==null?request.getParameter("getthumb"):arr[0][0]));
	                if (file.exists()) {
	                    String mimetype = ser.getMimeType(file);
	                    if (mimetype.endsWith("png") || mimetype.endsWith("jpeg")|| mimetype.endsWith("jpg") || mimetype.endsWith("gif")) {
	                        BufferedImage im = ImageIO.read(file);
	                        if (im != null) {
	                            BufferedImage thumb = Scalr.resize(im, 75); 
	                            ByteArrayOutputStream os = new ByteArrayOutputStream();
	                            if (mimetype.endsWith("png")) {
	                                ImageIO.write(thumb, "PNG" , os);
	                                response.setContentType("image/png");
	                            } else if (mimetype.endsWith("jpeg")) {
	                                ImageIO.write(thumb, "jpg" , os);
	                                response.setContentType("image/jpeg");
	                            } else if (mimetype.endsWith("jpg")) {
	                                ImageIO.write(thumb, "jpg" , os);
	                                response.setContentType("image/jpeg");
	                            } else {
	                                ImageIO.write(thumb, "GIF" , os);
	                                response.setContentType("image/gif");
	                            }
	                            ServletOutputStream srvos = response.getOutputStream();
	                            response.setContentLength(os.size());
	                            response.setHeader( "Content-Disposition", "inline; filename=\"" + MimeUtility.encodeWord(file.getName()) + "\"" );
	                            os.writeTo(srvos);
	                            srvos.flush();
	                            srvos.close();
	                        }
	                    }
	            } // TODO: check and report success
	        } else {
	            PrintWriter writer = response.getWriter();
	            writer.write("call POST with multipart form data");
	        }
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
    
    /**
        * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
        * 
        */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        if (!ServletFileUpload.isMultipartContent(request)) {
            throw new IllegalArgumentException("Request is not multipart, please 'multipart/form-data' enctype for your form.");
        }
        String encoding = request.getCharacterEncoding();
        response.setCharacterEncoding(encoding);
        PrintWriter writer = response.getWriter();
        response.setContentType("application/json");
        JSONArray json = new JSONArray();
        try {
        	FileUploadService ser = new FileUploadService();
        	json = ser.doInsert(request, fileRoot);
        }  catch (Exception e) {
                throw new RuntimeException(e);
        } finally {
            writer.write(json.toString());
            writer.close();
        }

    }

    
}