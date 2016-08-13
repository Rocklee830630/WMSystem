<%@ page contentType="text/html;charset=utf-8" %>
<%@ page import="com.ccthanking.framework.plugin.AppInit"%>
<%@ page import="com.ccthanking.framework.common.DBUtil"%>
<%@ page import="java.sql.*"%>
<%@ page import="com.ccthanking.framework.util.Pub"%>
<%@ page import="java.io.File"%>
<%@ page import="java.io.FileInputStream"%>
<%@ page import="java.io.FileOutputStream"%>
<%@ page import="com.ccthanking.framework.params.ParaManager"%>
<%@ page import="com.ccthanking.framework.params.SysPara.SysParaConfigureVO"%>

<%

boolean isNewFile ;
String filetype="";
String fileId="";
String fileName="";
String fileUrl="";
String ywid = "";
String	geturl = "";
String templateFileUrl="templateFile/";//新建文档模板url
String fileDir = "";
fileId = request.getParameter("FileId")==null?"":request.getParameter("FileId").toString().trim();
if(fileId=="")
{isNewFile = true;}
else
{isNewFile = false;}
if(isNewFile)
{
	
}
else
{
	Connection conn =DBUtil.getConnection();
	String[][] rs = null;
	try{
		
	  rs = DBUtil.query("select fileid,filename,url,ywid from fs_fileupload where FILEID='"+fileId+"'");
	  
		if(rs != null)
		{
			ywid = rs[0][3];
			fileName = rs[0][1];	
			geturl = rs[0][2];
			String base = AppInit.appPath+"/file";
			// 文件保存路径：根路径/项目ID/附件ID
			fileDir = base + "/" + ywid + "/" + fileId;
			//通过文件名称确定文件类型
			if(!Pub.empty(fileName)){
				
				if(fileName.substring(fileName.lastIndexOf("."), fileName.length()).equals(".doc")||fileName.substring(fileName.lastIndexOf("."), fileName.length()).equals(".docx")){
					filetype = "word";
				}else if(fileName.substring(fileName.lastIndexOf("."), fileName.length()).equals(".xls")||fileName.substring(fileName.lastIndexOf("."), fileName.length()).equals(".xlsx"))
				{
					filetype="excel";
				}else if(fileName.substring(fileName.lastIndexOf("."), fileName.length()).equals(".ppt")||fileName.substring(fileName.lastIndexOf("."), fileName.length()).equals(".pptx"))
				{
					filetype="ppt";
				}
			}
			SysParaConfigureVO syspara = (SysParaConfigureVO) ParaManager
					.getInstance().getSysParameter("FILEUPLOADROOT");
			/* SysParaConfigureVO syspara1 = (SysParaConfigureVO) ParaManager
					.getInstance().getSysParameter("APPPATH"); */
			String fileRoot = syspara.PARAVALUE1;
			String webfileRoot = "http://"+request.getLocalAddr()+":"+request.getLocalPort()+request.getContextPath()+"/";
			//
			fileUrl = webfileRoot+"file/"+geturl;
			
			File file = new File(fileRoot + "/" + geturl);
			if (file.exists()) {
			// 文件夹不存在的话，需要创建文件夹，否则直接使用
			File copyFile = new File(fileDir);
			if (!copyFile.exists() && !copyFile.isDirectory()) {
				copyFile.mkdirs();
				copyFile = new File(fileDir);
				copyFile.mkdir();
				copyFile = new File(fileDir, fileName);
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
		}else if(copyFile.exists() && copyFile.isDirectory()){//存在路径则覆盖文件
			//-----------------------------------
			copyFile = new File(fileDir, fileName);
			copyFile.createNewFile();
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
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html  xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>ntko office文档控件示例-ms office文档编辑</title>
<meta content="IE=7" http-equiv="X-UA-Compatible" /> 
		<!--设置缓存-->
		<meta http-equiv="cache-control" content="no-cache,must-revalidate">
		<meta http-equiv="pragram" content="no-cache">
		<meta http-equiv="expires" content="0">
     <link href="<%=request.getContextPath()%>/editoffice/StyleSheet.css" rel="stylesheet" type="text/css" />
   <script type="text/javascript" src="<%=request.getContextPath()%>/editoffice/OfficeContorlFunctions.js"></script>
</head>
<body  onload='intializePage("<%=fileUrl%>")'>
    <form id="form1" action="upLoad_OfficeFile.jsp" enctype="multipart/form-data" style="padding:0px;margin:0px;">
    <div id="editmain" class="editmain">
	<%--        <div id="edittop" class="top">
        <img alt="在线编辑示例程序" src="<%=request.getContextPath()%>/editoffice/images/edit_banner.jpg" />
        </div> --%>
        <div id="editmain_top" class="editmain_top">
                <div id="edit_button_div" class="edit_button_div">
                <img alt="保存office文档" src="<%=request.getContextPath()%>/editoffice/images/edit_save_office.gif" onclick="saveFileToUrl();" />
                </div>
            <table border="0" cellpadding="0" cellspacing="0">
                <tr>
                    <td><img src="<%=request.getContextPath()%>/editoffice/images/edit_main_top.jpg"  alt="文件列表上框" /></td>
                </tr>
                <tr>
                    <td class="edittablebackground"><!--示例标题--><b>ntko office文档控件示例-ms office文档编辑</b></td>
                </tr>
            </table>
        </div>
        <div id="editmain_middle" class="editmain_middle">
            <div id="editmain_left" class="editmain_left">
                <div class="funbutton">
                    <ul class="ul">
                    <li class="listtile">界面设置:</li>
                    <li onclick="OFFICE_CONTROL_OBJ.Menubar=!OFFICE_CONTROL_OBJ.Menubar;">菜单栏栏切换</li>
                    <li onclick="OFFICE_CONTROL_OBJ.ToolBars=!OFFICE_CONTROL_OBJ.ToolBars;">工具栏栏切换</li>
                    <li onclick="OFFICE_CONTROL_OBJ.IsShowInsertMenu=!OFFICE_CONTROL_OBJ.IsShowInsertMenu;">"插入"菜单切换</li>
                    <li onclick="OFFICE_CONTROL_OBJ.IsShowEditMenu=!OFFICE_CONTROL_OBJ.IsShowEditMenu;">"编辑"菜单切换</li>
                    <li onclick="OFFICE_CONTROL_OBJ.IsShowToolMenu=!OFFICE_CONTROL_OBJ.IsShowToolMenu;">"工具"菜单切换</li>
                    </ul>
                </div>
                <div class="funbutton">
                    <ul class="ul">
                    <li class="listtile">打印控制:</li>
                    <li onclick="setFilePrint(true);">允许打印</li>
                    <li onclick="setFilePrint(false);">禁止打印</li>
                    <li onclick="OFFICE_CONTROL_OBJ.showDialog(5);">页面设置</li>
                    <li onclick="OFFICE_CONTROL_OBJ.PrintPreview();">打印预览</li>
                    </ul>
                </div>
                <div class="funbutton">
                    <ul class="ul">
                    <li class="listtile">痕迹保留功能:</li>
                    <li onclick="SetReviewMode(true);">保留痕迹</li>
                    <li onclick="SetReviewMode(false);">取消留痕</li>
                    <li onclick="setShowRevisions(true);">显示痕迹</li>
                    <li onclick="setShowRevisions(false);">隐藏痕迹</li>
                    <li onclick="OFFICE_CONTROL_OBJ.ActiveDocument.AcceptAllRevisions();">接受修订</li>
                    </ul>
                </div>
                <div class="funbutton">
                    <ul class="ul">
                    <li class="listtile">权限控制:</li>
                    <li onclick="OFFICE_CONTROL_OBJ.SetReadOnly(true);">禁止编辑</li>
                    <li onclick="OFFICE_CONTROL_OBJ.SetReadOnly(false);">允许编辑</li>
                    <li onclick="setFileNew(true);">允许新建</li>
                    <li onclick="setFileNew(false);">禁止新建</li>
                   <!--  <li onclick="setFileSaveAs(true);">允许另存</li>--> 
                   <li onclick="setFileSaveAs(true)">允许另存</li>
                    <li onclick="setFileSaveAs(false);">禁止另存</li>
                    <li onclick="setIsNoCopy(false);">允许拷贝</li>
                    <li onclick="setIsNoCopy(true);">禁止拷贝</li>
                    </ul>
                </div>
            </div>
            <div id="editmain_right" class="editmain_right">
                <div id="formtop" style="display:none">
                     <table>
                        <tr>
                            <td colspan="5"  class="edit_tabletitle">文件表单数据:</td>
                        </tr>
                        <tr>
                            <td colspan="5">&nbsp;</td>
                        </tr>
                        <tr>
                            <td width="7%"> 文&nbsp;件&nbsp;ID:</td>
                            <td width="20%"><input name="fileId" id="fileId" readOnly  type="text" value="<%=fileId%>" /></td>
                            <td width="8%">文件名称:</td>
                            <td width="40%"><input name="filename" id="filename" type="text" value="<%=fileName%>" /></td>
                            <td>&nbsp;</td>
                        </tr>
                    </table>
                </div>
                
                <div id="officecontrol">
                 <script type="text/javascript" src="<%=request.getContextPath()%>/editoffice/officecontrol/ntkoofficecontrol.js"></script>
              
                <div id=statusBar style="height:20px;width:100%;background-color:#c0c0c0;font-size:12px;"></div>
				<script language="JScript" for=TANGER_OCX event="OnDocumentClosed()">
									setFileOpenedOrClosed(false);
								</script>
								<script language="JScript" for="TANGER_OCX" event="OnDocumentOpened(TANGER_OCX_str,TANGER_OCX_obj)">
									
									OFFICE_CONTROL_OBJ.activeDocument.saved=true;//saved属性用来判断文档是否被修改过,文档打开的时候设置成ture,当文档被修改,自动被设置为false,该属性由office提供.
									//获取文档控件中打开的文档的文档类型
									switch (OFFICE_CONTROL_OBJ.doctype)
									{
										case 1:
											fileType = "wrod";
											fileTypeSimple = "wrod";
											break;
										case 2:
											fileType = "excel";
											fileTypeSimple="excel";
											break;
										case 3:
											fileType = "ppt";
											fileTypeSimple = "ppt";
											break;
										case 4:
											fileType = "Visio.Drawing";
											break;
										case 5:
											fileType = "MSProject.Project";
											break;
										case 6:
											fileType = "WPS Doc";
											fileTypeSimple="wps";
											break;
										case 7:
											fileType = "Kingsoft Sheet";
											fileTypeSimple="et";
											break;
										default :
											fileType = "unkownfiletype";
											fileTypeSimple="unkownfiletype";
									}
									setFileOpenedOrClosed(true);
								</script>
									<script language="JScript" for=TANGER_OCX event="BeforeOriginalMenuCommand(TANGER_OCX_str,TANGER_OCX_obj)">
									//alert("BeforeOriginalMenuCommand事件被触发");
								</script>
								<script language="JScript" for=TANGER_OCX event="OnFileCommand(TANGER_OCX_str,TANGER_OCX_obj)">
									
								</script>
								<script language="JScript" for=TANGER_OCX event="AfterPublishAsPDFToURL(result,code)">
								//alert(code);
									result=trim(result);
									//alert(result);
									document.all("statusBar").innerHTML="服务器返回信息:"+result;
									if(result=="文档保存成功。")
									{window.close();}
								</script>
								
								<script language="JScript" for=TANGER_OCX event="OnCustomMenuCmd2(menuPos,submenuPos,subsubmenuPos,menuCaption,menuID)">
								//alert("第" + menuPos +","+ submenuPos +","+ subsubmenuPos +"个菜单项,menuID="+menuID+",菜单标题为\""+menuCaption+"\"的命令被执行.");
								</script>
								<script language="JScript" for=TANGER_OCX event="OnDoWebGet(type,code,html)">
								//alert(html);
								//alert("OnDoWebGet成功回调");
								</script>
								<script language="JScript" for=TANGER_OCX event="OnDoWebExecute(type,code,html)">
								//alert(html);
								//alert("OnDoWebExecute成功回调");
								</script>
								<script language="JScript" for=TANGER_OCX event="OnDoWebExecute2(type,code,html)">
								//alert(html);
								//alert("OnDoWebExecute2成功回调");
								</script>
								<script language="JScript" for=TANGER_OCX event="OnSaveToURL(type,code,html)">
								
								//alert("SaveToURL成功回调");
								
								</script>
								<script language="JScript" for=TANGER_OCX event="OnpublishAshtmltourl(type,code,html)">
								//alert(html);
								//alert("publishAshtmltourl成功回调");
								</script>
								<script language="JScript" for="TANGER_OCX" event="OnScreenModeChanged(ntko)">
			
						if(ntko){
							AddCustomButtonOnMenu(0,"关闭",false);
					//自定义菜单
						}
			
		
</script>
                </div>
            </div>
        </div>
       <%-- <div id="edit_bottom" class="edit_bottom">
       <img alt="" src="<%=request.getContextPath()%>/editoffice/images/edit_main_nether.jpg" />
           <div id="conmpanyinfo" class="conmpanyinfo">
            <img alt="重庆软航科技有限公司" src="<%=request.getContextPath()%>/editoffice/images/Companyinfo.jpg" />
             <p>技术支持详见公司网站www.ntko.com “联系我们”</p>
            
            </div>
        </div> --%>
    </div>
    </form>
</body>
</html>
