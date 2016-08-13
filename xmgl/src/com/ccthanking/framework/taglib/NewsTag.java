package com.ccthanking.framework.taglib;

import java.io.IOException;
import java.sql.Blob;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;

import com.ccthanking.common.FjlbManager;
import com.ccthanking.framework.Globals;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.QuerySet;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.params.ParaManager;
import com.ccthanking.framework.params.SysPara.SysParaConfigureVO;
import com.ccthanking.framework.util.Pub;

public class NewsTag extends TagSupport {

	private String informHtml() {
		String appPath = ((HttpServletRequest) pageContext.getRequest())
				.getContextPath();
		User user =  (User)((HttpServletRequest) pageContext.getRequest()).getSession().getAttribute(Globals.USER_KEY);
		String userid = user.getAccount();
		String text = "<div class=\"Box50\">\r\n";
		text += "<div class=\"page-header\">\r\n";
		text += "<h3>通知公告<span class=\"pull-right\"><small><a href=\"javascript:void(0)\" onclick=\"showMoreNews('1')\">更多</a></small></span></h3>\r\n";
		text += "</div>\r\n";
		text += "<ul class=\"Announcement unstyled\">\r\n";
		String sql = "select * from(select ggid,ggbt,'' as nr,FBFWMC,fbr,TO_CHAR(fbsj,'yyyy\"年\"fmmm\"月\"dd\"日\"') ,FBBMMC,decode(GGLB,'GG','公告','TZ','通知','通知') gglb,sfyd from xtbg_xxzx_ggtz_fb where SHZT = '1' and (GGLB='GG' or GGLB='TZ') and JSR_ACCOUNT = '"+userid+"' order by fbsj desc,lrsj desc )";
		String[][] qs = DBUtil.query(sql);
		if (qs != null && qs.length > 0) {
			for (int i = 0; i < qs.length; i++) {
				if (i < 5) {
                        
                        String sfyd = qs[i][8];
                        String ydHtml = "";
                        if(sfyd!=null&&"0".equals(sfyd)){
                        	ydHtml = "<img src=\""+appPath+"/images/listIcon.png\">";
                        }else{
                        	ydHtml = "<img style='margin-bottom:4px;' src=\""+appPath+"/images/content_new_blank.png\" />";
                        }
                        String fj_sql = " select count(1) from FS_FILEUPLOAD where YWID='"+qs[i][0]+"' ";
                        String[][] t = DBUtil.query(fj_sql);
                        String fj_img = "";
                        if(t!=null&&t.length>0&&!t[0][0].equals("0")){
                        	fj_img = "&nbsp;<img src=\"/xmgl/images/icon-annex.png\"onclick=\"showNewDetail('1','"
								+ qs[i][0]
								+ "','',this)\" alt=\"附件\" width=\"16\" height=\"16\"> ";
                        }
                        String ggbt  ="" ; 
                        if(qs[i][1]!=null&&qs[i][1].length()>38){
                        	ggbt = qs[i][1].substring(0,38)+"...";
                        }
						text += "<li><span class=\"iconLeft\">"+ydHtml+"</span> <a href=\"javascript:void(0)\">["
								+ qs[i][7]
								+ "]</a>&nbsp;&nbsp;<a href=\"javascript:void(0)\" onclick=\"showNewDetail('1','"
								+ qs[i][0]
								+ "','',this)\">";
						        if(!"".equals(ggbt)){
						        	text +="<abbr title=\""+qs[i][1]+"\">"+ggbt+"</abbr>";
						        }else{
						        	text +=qs[i][1];
						        }
								//+ ggbt
								text +=	"</a>"+fj_img+"<small class=\"muted pull-right\">"
								+ qs[i][5] + "</small> </li>";
					
				}
			}
		} else {
			text += "暂无通知公告";
		}
		text += "</ul>\r\n";
		text += "</div>\r\n";
		return text;
	}

	private String newsHtml() {
		String appPath = ((HttpServletRequest) pageContext.getRequest())
				.getContextPath();
		SysParaConfigureVO syspara = (SysParaConfigureVO) ParaManager.getInstance().getSysParameter("FILEUPLOADROOT");
		String fileRoot = syspara.PARAVALUE1;
		String text = "<div class=\"Box50\">\r\n";
		text += "<div class=\"page-header\">\r\n";
		text += "<h3>中心新闻<span class=\"pull-right\"><small><a href=\"javascript:void(0)\" onclick=\"showMoreNews('2')\">更多</a></small></span></h3>\r\n";
		text += "</div>\r\n";
		text += "<ul class=\"unstyled centerNews\">\r\n";
		String sql = "select * from(select newsid,xwbt,nr ,fbr,TO_CHAR(fbsj,'yyyy\"年\"fmmm\"月\"dd\"日\"'),lrbmmc,trunc( sysdate - fbsj) as ts from xtbg_xxzx_zxxw where sfyx = '1' order by fbsj desc,lrsj desc  ) where rownum<3";
		String[][] qs = DBUtil.query(sql);
		try{
		if (qs != null && qs.length > 0) {
			for (int i = 0; i < qs.length; i++) {
				if (i < 2) {
          		    String hasNew = "";
					String ts = qs[i][6];
          		    if(ts!=null&&!"".equals(ts))
          		    {
          			  int t = Integer.parseInt(ts);
          			  if(t<=7){
          				  hasNew = "&nbsp;<img src=\""+appPath+"/images/new.gif\">";
          			  }
          		    } 
					
					text += "<li>\r\n";
					text += "<p><small class=\"muted\">"
							+ qs[i][4]
							+ "</small> <a href=\"javascript:void(0)\" onclick=\"showNewDetail('2','"
							+ qs[i][0] + "')\" >" + qs[i][1]
							+ "</a>"+hasNew+"</p>\r\n";
					text += "<p>";
					text += "<span class=\"centerNewsLiImg\">";
					//---------------------------对附件缩略图的处理--开始---------
					//查询附件SQL
					String fileHtml = "";
					String queryFileSql = "select fileid,filename,url,fjlx from FS_FILEUPLOAD where ywid='"+qs[i][0]+"' and fjlb='"+FjlbManager.OA_XXZX_ZXXW+"' order by lrsj desc";
					String files[][] = DBUtil.query(queryFileSql);
					if(files!=null){
						for(int x=0;x<files.length;x++){
							String fileDir = fileRoot+"/"+files[x][2].substring(0, files[x][2].indexOf(files[x][1]));
							if(files[x][3].indexOf("image")!=-1){
								fileHtml += "<img src=\""
										+ appPath
										+ "/UploadServlet?getthumb="+files[x][1]+"&amp;fileDir="+fileDir+"\" width=\"70px\" height=\"30px\">";
								break;
							}else{
								continue;
							}
						}
					}
					text += fileHtml;
					//---------------------------对附件缩略图的处理--结束---------
					String queryGgBlob = "select nr from XTBG_XXZX_ZXXW where NEWSID='"+qs[i][0]+"'";
					QuerySet queryset = DBUtil.executeQuery(queryGgBlob, null);
					String nr = queryset.getString(1, "nr");
					Blob dbBlob = (Blob)queryset.getObject(1, "nr");
						if (dbBlob != null) {
							int length = (int) dbBlob.length();
							byte[] buffer = dbBlob.getBytes(1, length);
							nr = new String(buffer, "GBK");
						}
					nr = Pub.splitAndFilterString(nr, 90);	

					
					text += "</span>";
					text += "" + nr + "\r\n";
					text += "</p>";
					text += "</li>\r\n";
				}
				
			}
		} else {
			text += "暂无中心新闻";
		}
		text += "</ul>\r\n";
		text += "</div>\r\n";
		}catch(Exception e){
			e.printStackTrace();
		}
		return text;
	}
	private String ndxxgxHtml() {
		String appPath = ((HttpServletRequest) pageContext.getRequest())
				.getContextPath();
		SysParaConfigureVO syspara = (SysParaConfigureVO) ParaManager.getInstance().getSysParameter("FILEUPLOADROOT");
		User user =  (User)((HttpServletRequest) pageContext.getRequest()).getSession().getAttribute(Globals.USER_KEY);
		String userid = user.getAccount();
		String fileRoot = syspara.PARAVALUE1;
		String title = "年度信息共享";
		String text = "";
		//样式1--实线羽毛效果
//		text += "<div style='height:32px;width:100%;display:inline-block;'>" +
//				"<div style='width:100%;height:100%;border-bottom:#3974B9 solid 3px;left:-32px;position:relative;display:inline-block;float:left;'></div>" +
//				"<image style='position:relative;right:1px;float:right;top:-32px;' src=\""+appPath+"/images/feather_black.png\" />" +
//				"</div>";
		//样式2--黑色虚线
//		text += "<div style='height:12px;width:100%;display:inline-block;border-bottom:3px dashed #000;'></div>";
		//样式3--蓝色虚线
//		text += "<div style='height:12px;width:100%;display:inline-block;border-bottom:3px dashed #3974B9;'></div>";
		//样式4--灰色虚线
//		text += "<div style='height:12px;width:100%;display:inline-block;border-bottom:3px dashed #EEEEEE;'></div>";
		//样式5--深灰色虚线
		text += "<div style='height:5px;width:100%;display:inline-block;border-bottom:3px dashed #B7B7B7;margin:5px 0px 4px 0px;'></div>";
		text += "<div class=\"Box50\">\r\n";
		//样式6--横向渐变，浅蓝变深蓝
//		text += "<div style='height:3px;width:100%;display:inline-block;background-image:linear-gradient(90deg,#9BCBFA,#0866C6);margin-top:5px;'></div>";
		//样式7--横向渐变，浅灰变深灰
//		text += "<div style='height:3px;width:100%;display:inline-block;background-image:linear-gradient(90deg,#EEEEEE,#797676);margin-top:5px;'></div>";
		//样式8--横向渐变，浅蓝变深蓝变浅蓝
//		text += "<div style='height:3px;width:100%;display:inline-block;background-image:linear-gradient(90deg,#9BCBFA,#0866C6,#9BCBFA);margin-top:5px;'></div>";
		//样式9--横向渐变，浅灰变深灰变浅灰
//		text += "<div style='height:3px;width:100%;display:inline-block;background-image:linear-gradient(90deg,#EEEEEE,#797676,#EEEEEE);margin-top:5px;'></div>";
		//样式10--横向渐变，彩虹
//		text += "<div style='height:5px;width:100%;display:inline-block;background-image:linear-gradient(90deg,red, orange,yellow,green,blue,indigo,violet);margin-top:5px;'></div>";
		
		
		text += "<div class=\"page-header\" style='padding-top:0px;'>\r\n";
		text += "<h3>"+title+"<span class='pull-right'><small><a href='javascript:void(0)' onclick='showMoreNdxxgx(\"1\")'>更多</a></small></span></h3>\r\n";
		text += "</div>\r\n";
		text += "<ul class=\"Announcement unstyled\">\r\n";
		try{
			String sql = "select * from(select A.XTBG_XXZX_NDXXGX_ID,A.NDXXBT,TO_CHAR(A.fbsj,'yyyy\"年\"fmmm\"月\"dd\"日\"') ,A.FBR,nvl(floor(sysdate-A.fbsj),0),B.SFYD " +
					"from XTBG_XXZX_NDXXGX A,XTBG_XXZX_GGTZ_FB B " +
					"where A.XTBG_XXZX_NDXXGX_ID=B.GGID and B.JSR_ACCOUNT='"+userid+"' and B.SFYX='1' and A.ISFB='1' and A.SFYX='1' and GGLB='ND' order by A.fbsj desc,A.lrsj desc ) where rownum<10";
			String[][] qs = DBUtil.query(sql);
			if (qs != null && qs.length > 0) {
				for (int i = 0; i < qs.length; i++) {
					if (i < 5) {
						String sfyd = qs[i][5];
                        String ydHtml = "";
                        if("0".equals(sfyd)){
    		        		ydHtml = "<img style='margin-bottom:4px;' src=\""+appPath+"/images/new.gif\" />";
                        }else{
    		        		ydHtml = "<img style='margin-bottom:4px;' src=\""+appPath+"/images/content_new_blank.png\" />";
                        }
                        String fj_sql = " select count(1) from FS_FILEUPLOAD where YWID='"+qs[i][0]+"' ";
                        String[][] t = DBUtil.query(fj_sql);
                        String fj_img = "";
                        if(t!=null&&t.length>0&&!t[0][0].equals("0")){
                        	fj_img = "&nbsp;<img src=\""+appPath+"/images/icon-annex.png\" " +
                        			"onclick=\"showGxxxDetail('"+qs[i][0]+"','年度信息共享',this)\" alt=\"附件\" width=\"16\" height=\"16\"> ";
                        }
                        String ggbt  ="" ; 
                        if(qs[i][1]!=null){
                        	if(qs[i][1].length()>38){
	                        	ggbt = "<abbr title=\""+qs[i][1]+"\">"+qs[i][1].substring(0,38)+"...</abbr>";
                        	}else{
                        		ggbt = qs[i][1];
                        	}
                        }
						text += "<li><span class=\"iconLeftNew\">"+ydHtml+"</span> <a href=\"javascript:void(0)\">"
								+ "</a><a href=\"javascript:void(0)\" onclick=\"showGxxxDetail('"
								+ qs[i][0]
								+ "','"+title+"',this)\">";
					        	text += ggbt;
								text +=	"</a>"+fj_img+"<small class=\"muted pull-right\">"
								+ qs[i][2] + "</small> </li>";
					}
				}
			} else {
				text += "暂无年度信息共享数据";
			}
			text += "</ul>\r\n";
			text += "</div>\r\n";
		}catch(Exception e){
			e.printStackTrace();
		}
		return text;
	}
	private String zrbghHtml() {
		String appPath = ((HttpServletRequest) pageContext.getRequest())
				.getContextPath();
		SysParaConfigureVO syspara = (SysParaConfigureVO) ParaManager.getInstance().getSysParameter("FILEUPLOADROOT");
		String fileRoot = syspara.PARAVALUE1;
		String title = "会议中心";
		String text = "<div class=\"Box50\">\r\n";
		text += "<div class=\"page-header\">\r\n";
		text += "<h3>"+title+"<span class=\"pull-right\"><small><a href=\"javascript:void(0)\" onclick=\"showMoreZrbgh('"+title+"')\">更多</a></small></span></h3>\r\n";
		text += "</div>\r\n";
		text += "<ul class=\"Announcement unstyled\">\r\n";
		try{
			String sql = "select * from(select GC_BGH_ID,HC,TO_CHAR(hysj,'yyyy\"年\"fmmm\"月\"dd\"日\"') ,HYZC,nvl(floor(sysdate-hysj),0) " +
					"from GC_BGH A,(select count(GC_BGH_WT_ID) WTSL,BGHID from GC_BGH_WT where SFGK='1' and SFYX='1' group by BGHID) B " +
					"where A.SFYX='1' and A.GC_BGH_ID=B.BGHID and B.WTSL>0 order by A.hysj desc,A.lrsj desc ) where rownum<10";
			String[][] qs = DBUtil.query(sql);
			if (qs != null && qs.length > 0) {
				for (int i = 0; i < qs.length; i++) {
					if (i < 5) {
	                        String ydHtml = "";
				        	int betweenDays = qs[i][4]==null?0:Integer.parseInt(qs[i][4]+"");
				        	if(betweenDays<10){
				        		ydHtml = "<img style='margin-bottom:4px;' src=\""+appPath+"/images/content_new.gif\" />";
				        	}else{
				        		ydHtml = "<img style='margin-bottom:4px;' src=\""+appPath+"/images/content_new_blank.png\" />";
				        	}
	                        String fj_sql = " select count(1) from FS_FILEUPLOAD where YWID='"+qs[i][0]+"' ";
	                        String[][] t = DBUtil.query(fj_sql);
//	                        暂时主任办公会不给附件
	                        String fj_img = "";
//							if(t!=null&&t.length>0&&!t[0][0].equals("0")){
//								fj_img = "&nbsp;<img src=\""+appPath+"/images/icon-annex.png\"onclick=\"showNewDetail('1','"
//									+ qs[i][0]
//									+ "','',this)\" alt=\"附件\" width=\"16\" height=\"16\"> ";
//	                        }
	                        String ggbt  ="";
	                        if(qs[i][1]!=null){
	                        	if(qs[i][1].length()>38){
		                        	ggbt = "<abbr title=\""+qs[i][1]+"\">"+qs[i][1].substring(0,38)+"...</abbr>";
	                        	}else{
	                        		ggbt = qs[i][1];
	                        	}
	                        }
							text += "<li><span class=\"iconLeftNew\">"+ydHtml+"</span> <a href=\"javascript:void(0)\">"
									+ "</a><a href=\"javascript:void(0)\" onclick=\"showZrbghDetail('2','"
									+ qs[i][0]
									+ "','"+title+"','"+qs[i][2]+"')\">";
						        	text += ggbt;
									text +=	"</a>"+fj_img+"<small class=\"muted pull-right\">"
									+ qs[i][2] + "</small> </li>";
						
					}
				}
			} else {
				text += "暂无主任办公会数据";
			}
			text += "</ul>\r\n";
			text += "</div>\r\n";
		}catch(Exception e){
			e.printStackTrace();
		}
		return text;
	}
	private String getResult() throws JspException {

		StringBuffer sb = new StringBuffer();
//		sb.append(newsHtml() + informHtml()+ndxxgxHtml());
		sb.append(informHtml()+zrbghHtml()+ndxxgxHtml());
		String str = sb.toString();
		sb = null;
		return str;
	}

	public int doEndTag() throws JspTagException {
		try {
			// 获取页面输出流，并输出字符串
			String appPath = ((HttpServletRequest) pageContext.getRequest())
					.getContextPath();
			String base = "";
			base += "";
			pageContext.getOut().write(getResult());
		}
		// 捕捉异常
		catch (IOException ex) {
			// 抛出新异常
			// throw new JspTagException("错误"};
		} catch (JspException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 值回返
		return EVAL_PAGE;
	}

}
