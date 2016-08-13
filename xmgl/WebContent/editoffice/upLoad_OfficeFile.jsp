<%@ page contentType="text/html;charset=utf-8" %>
<%@ page import="com.ccthanking.framework.plugin.AppInit"%>
<%@ page import="com.ccthanking.framework.common.DBUtil"%>
<%@ page import="com.ccthanking.framework.util.Pub"%>
<%@ page import="java.io.File"%>
<%@ page language="java" import="java.io.*,java.sql.*,java.util.*" %>
<%@ page import="com.ccthanking.framework.params.ParaManager"%>
<%@ page import="com.ccthanking.framework.params.SysPara.SysParaConfigureVO"%>
<%@ page language="java" import="org.apache.commons.fileupload.*,org.apache.commons.fileupload.disk.*,org.apache.commons.fileupload.servlet.*"%>
<%!/*------------------------------------------------------------
	 officeFileItem:文档FileItem类
	 attachFileItem:附件的FileItem类
	 officefileNameDisk:文档保存到磁盘上的路径
	 attachFileNameDisk:附件保存到磁盘上的路径
	 --------------------------------------------------------------*/
	public FileItem officeFileItem = null;
	public FileItem attachFileItem = null;

	public String officefileNameDisk = "";
	public String attachFileNameDisk = "";

	/*------------------------------------------------------------
	 保存文档到服务器磁盘，返回值true，保存成功，返回值为false时，保存失败。
	 --------------------------------------------------------------*/
	public boolean saveFileToDisk(String absoluteOfficeFileDir) {
		File officeFileUpload = null;
		String officeFileUploadPath = "";
		boolean result = true;

		try {


			if (officeFileItem != null) {
				officeFileUpload = new File(absoluteOfficeFileDir);
				officeFileItem.write(officeFileUpload);
			}
		} catch (Exception e) {
			System.out.println("error saveFileToDisk:" + e.getMessage());
			e.printStackTrace();
			result = false;
		}
		return result;
	}%>
<%
	//乱码加这个解决
	request.setCharacterEncoding("utf-8");
	String fileId = "";
	long fileSize = 0;
	String fileName = "";
	String fileName1 = "";

	String fileType = "";

	String result = "";
	String tempFileDir = "";

	boolean isNewRecode = true;
	officeFileItem = null;

	DiskFileItemFactory factory = new DiskFileItemFactory();
	// 设置最多只允许在内存中存储的数据,单位:字节
	factory.setSizeThreshold(409600);
	// 设置一旦文件大小超过setSizeThreshold()的值时数据存放在硬盘的目录
	tempFileDir = request.getContextPath()+"/tempFile";
	factory.setRepository(new File(tempFileDir));//文件缓存路径

	ServletFileUpload upload = new ServletFileUpload(factory);
	//设置允许用户上传文件大小,单位:字节
	upload.setSizeMax(1024 * 1024 * 400);
	List fileItems = null;
	try {
		fileItems = upload.parseRequest(request);

	}

	catch (FileUploadException e) {
		out.println("the max upload size is 4m,cheeck upload file size!");
		out.println(e.getMessage());
		e.printStackTrace();
		return;
	}
	Iterator iter = fileItems.iterator();
	attachFileItem = null;

	while (iter.hasNext()) {
		FileItem item = (FileItem) iter.next();

		//打印提交的文本域和文件域名称

		if (item.isFormField()) {
			if (item.getFieldName().equalsIgnoreCase("fileId")) {

					fileId = item.getString("utf-8").trim();
			}
			if (item.getFieldName().equalsIgnoreCase("fileName")) {
				fileName = item.getString("utf-8").trim();
			}

			if (item.getFieldName().equalsIgnoreCase("fileType"))//也算表单数据
			{
				fileType = item.getString("utf-8").trim();
			}

		} else {

			if (item.getFieldName().equalsIgnoreCase("upLoadFile")) {
				System.out
						.println("==upLoadFileupLoadFileupLoadFileupLoadFile=");
				officeFileItem = item;
			}

		}
	}
	if (!fileId.equalsIgnoreCase("") && !fileName.equalsIgnoreCase("")
			&& officeFileItem != null)
	//if(officeFileItem!=null)
	{
		//保存到磁盘中的文
		String  geturl = "";
		Connection conn = DBUtil.getConnection();
		String[][] rs = null;
		String absoluteOfficeFileDir = "";
		String fileRoot = "";
		try {
			rs = DBUtil
					.query("select fileid,filename,url,ywid from fs_fileupload where FILEID='"
							+ fileId + "'");
			if (rs != null) {
				geturl = rs[0][2];
			}
			SysParaConfigureVO syspara = (SysParaConfigureVO) ParaManager
					.getInstance()
					.getSysParameter("FILEUPLOADROOT");
			fileRoot = syspara.PARAVALUE1;
			
		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace();
		} finally {
			DBUtil.closeConnetion(conn);
		}
		absoluteOfficeFileDir = fileRoot+"\\"+geturl;
		out.println("absoluteOfficeFileDir:"+absoluteOfficeFileDir);
		if (saveFileToDisk(absoluteOfficeFileDir))

		{
			out.print("path" + absoluteOfficeFileDir);
			result = "文档保存成功ok。";
		} else {
			result = "save file failed,please check upload file size,the max size is 4M";
		}
	} else {
		result = "wrong information";
	}
	out.println(result);
	/*	conn = DriverManager.getConnection(ConnStr,userName,userPasswd);    
	 stmt=conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
	 rs = stmt.executeQuery("select * from "+officeFileInfoTableName+" fileid="+fileId); 
	 */
%>