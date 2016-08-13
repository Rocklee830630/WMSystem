package com.ccthanking.framework.print;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import jxl.Workbook;
import jxl.write.DateFormat;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.plugin.AppInit;
import com.ccthanking.framework.util.Pub;
import com.ccthanking.framework.util.RandomGUID;

public class ExportAsExcel extends HttpServlet {
	public void doGet_temp(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		HttpSession session = request.getSession(true);
		String fileName = "表格打印";//(String) session.getAttribute("fileName");
		String querySql ="select * from XMXX"; //(String) session.getAttribute("querySql");
        String info = request.getParameter("ExpTabListResultValue");
        String queryData = request.getParameter("ExpTabListQueryCondition");
        String tabThead = request.getParameter("ExpTabListThead");
        String fieldnames = request.getParameter("ExpTabListFieldNames");
		// session.removeAttribute("fileName");
		// session.removeAttribute("querySql");
		try {
			WritableFont arial15font = new WritableFont(WritableFont.ARIAL, 15,
					WritableFont.BOLD);
			arial15font.setColour(jxl.format.Colour.LIGHT_BLUE);
			WritableCellFormat arial15format = new WritableCellFormat(
					arial15font);
			arial15format.setAlignment(jxl.format.Alignment.CENTRE);
			arial15format.setBorder(jxl.format.Border.ALL,
					jxl.format.BorderLineStyle.THIN);
			arial15format.setBackground(jxl.format.Colour.VERY_LIGHT_YELLOW);
			File file = new File("output.xls");
			WritableWorkbook workbook = Workbook.createWorkbook(file);
			WritableSheet sheet = workbook.createSheet("Sheet 1", 0);
			sheet.addCell(new Label(0, 0, fileName, arial15format));
			sheet.setName(fileName);
			Connection conn = DBUtil.getConnection();
			PreparedStatement ps = conn.prepareStatement(querySql);
			ResultSet rs = ps.executeQuery(querySql);
			if (rs != null) {
				WritableFont arial11font = new WritableFont(WritableFont.ARIAL,
						11, WritableFont.BOLD);
				WritableCellFormat arial11format = new WritableCellFormat(
						arial11font);
				arial11format.setAlignment(jxl.format.Alignment.CENTRE);
				arial11format.setBorder(jxl.format.Border.ALL,
						jxl.format.BorderLineStyle.THIN);
				arial11format.setBackground(jxl.format.Colour.RED);
				int row = 0;
				int col = 1;
				ResultSetMetaData rsmd = rs.getMetaData();
				int[] validColumn = new int[rsmd.getColumnCount()];
				for (int i = 0; i < rsmd.getColumnCount(); i++) {
					String colName = rsmd.getColumnName(i + 1);
					if (colName.indexOf("NextIsURL") != -1) {
						validColumn[i] = -1;
					} else if (colName.indexOf("ThisIsCheckBox") != -1) {
						validColumn[i] = -2;
					} else {
						sheet.addCell(new Label(row, col, colName,
								arial11format));
						validColumn[i] = getStrLen(colName) + 4;
						// sheet.setColumnView(row, validColumn[i]);
						row++;
					}
				}
				col++;
				if (row > 1) {
					sheet.mergeCells(0, 0, row - 1, 0);
				}
				WritableFont arial9font = new WritableFont(WritableFont.ARIAL, 9);
				WritableCellFormat arial9format = new WritableCellFormat(arial9font);
				// arial9format.setAlignment(Alignment.CENTRE);
				arial9format.setBorder(jxl.format.Border.ALL,
						jxl.format.BorderLineStyle.THIN);
				DateFormat dateFormat = new DateFormat("yyyy-MM-dd");
				WritableCellFormat dateCellFormat = new WritableCellFormat(
						arial9font, dateFormat);
				// dateCellFormat.setAlignment(Alignment.CENTRE);
				dateCellFormat.setBorder(jxl.format.Border.ALL,
						jxl.format.BorderLineStyle.THIN);
				DateFormat timeFormat = new DateFormat("hh:mm:ss");
				WritableCellFormat timeCellFormat = new WritableCellFormat(
						arial9font, timeFormat);
				// timeCellFormat.setAlignment(Alignment.CENTRE);
				timeCellFormat.setBorder(jxl.format.Border.ALL,
						jxl.format.BorderLineStyle.THIN);
				while (rs.next()) {
					row = 0;
					for (int i = 0; i < rsmd.getColumnCount(); i++) {
						if (validColumn[i] == -1 || validColumn[i] == -2) {
							continue;
						}
						int columnType = rsmd.getColumnType(i + 1);
						switch (columnType) {
						case Types.BIT:
						case Types.BIGINT:
						case Types.BOOLEAN:
						case Types.NUMERIC:
						case Types.REAL:
						case Types.SMALLINT:
						case Types.TINYINT:
						case Types.DECIMAL:
						case Types.FLOAT:
						case Types.INTEGER:
							float number = rs.getFloat(i + 1);
							sheet.addCell(new jxl.write.Number(row, col,
									number, arial9format));
							break;
						case Types.DATE:
						case Types.TIMESTAMP:
							Date date = rs.getDate(i + 1);
							if (date == null) {
								sheet.addCell(new jxl.write.Blank(row, col));
							} else {
								sheet.addCell(new jxl.write.DateTime(row, col,
										date, dateCellFormat));
							}
							break;
						case Types.TIME:
							Date time = rs.getDate(i + 1);
							if (time == null) {
								sheet.addCell(new jxl.write.Blank(row, col));
							} else {
								sheet.addCell(new jxl.write.DateTime(row, col,
										time, timeCellFormat));
							}
							break;
						default:
							String str = rs.getString(i + 1);
							if (str == null) {
								sheet.addCell(new jxl.write.Blank(row, col,
										arial9format));
							} else {
								str = str.trim();
								sheet.addCell(new Label(row, col, str,
										arial9format));
								int len = getStrLen(str);
								if (len > validColumn[i]) {
									validColumn[i] = len;
								}
							}
							break;
						}
						row++;
					}
					col++;
				}
				row = 0;
				for (int i = 0; i < rsmd.getColumnCount(); i++) {
					if (validColumn[i] > 0) {
						sheet.setColumnView(row, validColumn[i]);
						row++;
					}
				}
			}
			conn.close();
			workbook.write();
			workbook.close();
			response.setContentType("application/x-msdownload");
			fileName = new String(fileName.getBytes("gb2312"), "ISO8859_1");
			response.setHeader("Content-Disposition", "attachment;filename=\""
					+ fileName + ".xls" + "\"");
			
			int len = (int) file.length();
			byte[] buf = new byte[len];
			FileInputStream fis = new FileInputStream(file);
			OutputStream out = response.getOutputStream();
	        StringBuffer strBuffer = new StringBuffer();
	        strBuffer.append("<html>");
	        strBuffer.append("<head>");
	        strBuffer.append(
	            "<meta http-equiv=\"Content-Type\" content=\"text/html;charset=GBK\">");
	        strBuffer.append("<meta name=ProgId content=Excel.Sheet>");
	        strBuffer.append("<style>");
	        strBuffer.append("table{ border:1px solid #000; border-spacing:0; border-collapse:collapse; padding:0; margin:0;}");
	        strBuffer.append(" td,th{ border-right:1px solid #000; border-bottom:1px solid #000; padding:0; margin:0;}");
	        strBuffer.append("</style>");
	        strBuffer.append("</head>");
	        strBuffer.append("<body >");
	        //strBuffer.append(getTableList(querySql,tabThead,fieldnames));
	        strBuffer.append("</body></html>");
	     
			
			
			out.write(strBuffer.toString().getBytes("GB2312"));
			out.flush();
			out.close();
//			len = fis.read(buf);
//			out.write(buf, 0, len);
//			out.flush();
//			fis.close();
//			file.delete();
		} catch (Exception e) {
			System.out.println("[Info: ] User canceled - " + e.getMessage());
		}
	}
	 private String readJSONString(HttpServletRequest request) {
		StringBuffer json = new StringBuffer();
		String line = null;
		try {
			BufferedReader reader = request.getReader();
			while ((line = reader.readLine()) != null) {
				json.append(line);
			}
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return json.toString();
	}
	 private String createDownloadFile(String byteStream,String fileName,HttpServletRequest request,HSSFWorkbook workBook) throws Exception{
		String guid = (new RandomGUID()).toString();
		String base = AppInit.appPath+"/file";
		String fileDir = base + "/ExpDownload/"+guid;
		// 文件保存路径：根路径/项目ID/附件ID
		File folder = new File(fileDir);
		if(!folder.exists() && !folder.isDirectory()){
			folder.mkdirs();
			folder = new File(fileDir);
			folder.mkdir();
			folder = new File(fileDir, fileName+".xls");
			folder.createNewFile();
		}
		FileOutputStream fos = new FileOutputStream(folder);
		OutputStreamWriter writer = null;
		try {
			if(workBook !=null){
				//指定模版导出
				workBook.write(fos);
			}else{
				//页面导出
				writer = new OutputStreamWriter(fos);
				writer.append(byteStream);
			}
		} finally {
			if (null != writer) {
				writer.flush();
				writer.close();
			}
			if(fos!=null){
				fos.flush();
				fos.close();
			}
		}
		
		return guid;

	}
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		String jsonStr = readJSONString(request);
		JSONObject obj = JSONObject.fromObject(jsonStr);
		String fileName = "表格打印";
        String resultJson = obj.get("ExpTabListResultValue").toString();
        String queryData = obj.get("ExpTabListQueryCondition").toString();
        String tabThead = obj.get("ExpTabListThead").toString();
        String fieldnames = obj.get("ExpTabListFieldNames").toString();
        String templateName = obj.get("templateName").toString();
        String t_fieldnames = obj.get("fieldnames").toString();
        String startXY = obj.get("startXY").toString();
        String printFileName = obj.get("printFileName").toString();
        if(!Pub.empty(printFileName)){
        	fileName = printFileName;
        }

		try {
			if(Pub.empty(templateName)){//通用模式实现
				response.setContentType("application/x-msdownload");
				//fileName = new String(fileName.getBytes("gb2312"), "ISO8859_1");
				response.setHeader("Content-Disposition", "attachment;filename=\""+ fileName + ".xls" + "\"");
//				OutputStream out = response.getOutputStream();
		        StringBuffer strBuffer = new StringBuffer();
		        strBuffer.append("<html>");
		        strBuffer.append("<head>");
		        strBuffer.append(
		            "<meta http-equiv=\"Content-Type\" content=\"text/html;charset=GBK\">");
		        strBuffer.append("<meta name=ProgId content=Excel.Sheet>");
		        strBuffer.append("<style>");
		        strBuffer.append("table{ border:1px solid #000; border-spacing:0; border-collapse:collapse; padding:0; margin:0;}");
		        strBuffer.append(" td,th{ border-right:1px solid #000; border-bottom:1px solid #000; padding:0; margin:0;}");
		        strBuffer.append("</style>");
		        strBuffer.append("</head>");
		        strBuffer.append("<body >");
		        strBuffer.append(getTableListResultJson(resultJson,tabThead,fieldnames));
		        strBuffer.append("</body></html>");
	
//				out.write(strBuffer.toString().getBytes());
//				
//				out.flush();
//				out.close();
		        System.out.println("FF1>"+strBuffer.toString());
				String guid = this.createDownloadFile(strBuffer.toString(),fileName,request,null);
				response.setCharacterEncoding("UTF-8");
		        response.setContentType("text/xml");
		        response.getWriter().print(guid);
			}else{//指定模版模式
				// 导出文件的名称。当指定模板时，导出文件名称得设置一下编码，且必须放在这里。
//	        	fileName = new String(fileName.getBytes("gb2312"), "iso8859-1");
		    	HSSFWorkbook workBook=null;  
				try {
					String templatepath = AppInit.appPath + "WEB-INF\\template\\"+templateName;//此处为读取模版，需要自定义
					InputStream inp = new FileInputStream(templatepath);
					//从源文件中进行读取  
					if(inp!=null){  
			            workBook = new HSSFWorkbook(inp);  
			        }
					HSSFSheet sheet = workBook.getSheetAt(0);//获得sheet页
					//循环写结果集数据 begin
					
					JSONArray list = Pub.doInitJson(resultJson);
					int r = 2;//起始行
					int c = 1;//其实列
					if(!Pub.empty(startXY)){
						if(startXY.indexOf(",")>-1){
							String[] t = startXY.split(",");
							if(t!=null&&t.length>0){
								r = Integer.parseInt(t[0]);
								c = Integer.parseInt(t[1]);
							}
						}
					}
					String[] fieldname_ = t_fieldnames.split(",");
					for(int i=0;i<list.size();i++){
						JSONObject jsonObject = (JSONObject)list.get(i);
						HSSFRow row = sheet.createRow(r+i); //创建一行
						if(fieldname_!=null&&fieldname_.length>0){
							HSSFCell cell_xh = row.createCell((short)(c));  
							HSSFCellStyle style_xh = workBook.createCellStyle();
							style_xh.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
							style_xh.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
							style_xh.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
							style_xh.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
							style_xh.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 居中						
							cell_xh.setCellStyle(style_xh);
							cell_xh.setCellValue(i+1);
							for(int j=0;j<fieldname_.length;j++){
								
								 HSSFCell cell = row.createCell((short)(c+j+1));  
								HSSFCellStyle style = workBook.createCellStyle();
								style.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框
								style.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框
								style.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框
								style.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框
								cell.setCellStyle(style);
					//			cell.setEncoding((short)HSSFCell.ENCODING_UTF_16);
								 boolean t =  jsonObject.containsKey(fieldname_[j].toUpperCase()+"_SV");
								 String key_sv_value="";
							        if(t==true){
							        	key_sv_value = jsonObject.getString(fieldname_[j].toUpperCase()+"_SV");
										if(key_sv_value!=null&&key_sv_value.indexOf("\b")>-1){
											key_sv_value = key_sv_value.replaceAll("\b", " ");
										}
							        	cell.setCellValue(key_sv_value);
							        }else{
							        	key_sv_value = jsonObject.getString(fieldname_[j].toUpperCase());
										if(key_sv_value!=null&&key_sv_value.indexOf("\b")>-1){
											key_sv_value = key_sv_value.replaceAll("\b", " ");
										}
							        	cell.setCellValue(key_sv_value);
							        }
							}
							
						}
						
						
					  }
			        response.setHeader("Content-type", "application/vnd.ms-excel");  
			        response.setHeader("Content-Disposition", "attachment; filename=" + fileName + ".xls");  
			          
//			        ServletOutputStream fileOut =response.getOutputStream();  
//			        workBook.write(fileOut);
//			        workBook.toString();
//			        FileOutputStream os = new FileOutputStream("e:\\workbook.xls");
//			        workBook.write(os);
//			        os.close();
					String guid = this.createDownloadFile(workBook.toString(),fileName,request,workBook);
//			        fileOut.flush();
//			        fileOut.close();
					response.setCharacterEncoding("UTF-8");
			        response.setContentType("text/xml");
			        response.getWriter().print(guid);
		    	} catch (Exception e) {
					System.out.println("[Info: ] User canceled - " + e.getMessage());
				}
				
				
			}

		} catch (Exception e) {
			System.out.println("[Info: ] User canceled - " + e.getMessage());
			e.printStackTrace();
		}
	}
	public String getTableListBySql(String querySql, String tabThead,
			String fieldnames) throws SQLException {
		String tabHTML = "<table>";
		tabHTML += tabThead;
		String[] fn = fieldnames.split(",");
		Connection conn = DBUtil.getConnection();
		PreparedStatement ps = conn.prepareStatement(querySql);
		ResultSet rs = ps.executeQuery(querySql);
		if (rs != null) {
			ResultSetMetaData rsmd = rs.getMetaData();
			int[] validColumn = new int[rsmd.getColumnCount()];
			while (rs.next()) {
				tabHTML +="<tr>";
				for (int j = 0; j < fn.length; j++) {

					for (int i = 0; i < rsmd.getColumnCount(); i++) {
						String colName = rsmd.getColumnName(i + 1);
						if(fn[j].equalsIgnoreCase(colName)){
							tabHTML +="<td>"+rs.getString(i+1)+"</td>";
						}

					}
				}
				tabHTML +="</tr>";
			}

		}
		tabHTML +="</table>";
		return tabHTML;
		
	}

	public String getTableListResultJson(String resultJson, String tabThead,
			String fieldnames) throws SQLException {
		String tabHTML = "<table>";
		tabHTML += tabThead;
		String[] fn = fieldnames.split(",");
		JSONArray list = doInitJson(resultJson);
		for(int i=0;i<list.size();i++){
			JSONObject jsonObject = (JSONObject)list.get(i);
			List<Object> arr = jsonObject.names();
			tabHTML +="<tr><td>"+String.valueOf(i+1)+"</td>";
		  for(int j=0;j<fn.length;j++){	
			for (Object name : arr) {
				String key = name.toString().toUpperCase();
				String value = jsonObject.getString(name.toString());
				String fieldname = fn[j];
				String colspan = "";
				String colspanTd = "";
				if(fieldname.indexOf("|")>-1){
					String[] s = Pub.getStringSplit(fieldname,"|");
					String kzJsonString =s[1];
					JSONObject kzJson = JSONObject.fromObject(kzJsonString);
					colspan = kzJson.getString("colspan");
					int t = 0;
					if(!Pub.empty(colspan)){
						t = Integer.parseInt(colspan);
					}
					colspanTd +="colspan =\""+t+"\"";
					fieldname = s[0];

				}
				
                if(fieldname.equalsIgnoreCase(key))
                {
                	String key_sv_value = "";;
					try {
						key_sv_value = jsonObject.getString(key+"_SV");
						if(key_sv_value!=null&&key_sv_value.indexOf("\b")>-1){
							key_sv_value = key_sv_value.replaceAll("\b", " ");
						}
						tabHTML +="<td "+colspanTd+">"+key_sv_value+"</td>";
					} catch (Exception e) {
						if(value!=null&&value.indexOf("\b")>-1){
							value = value.replaceAll("\b", " ");
						}
						tabHTML +="<td "+colspanTd+">"+value+"</td>";
					}
                	break;
                }
			}
		  }
		   tabHTML +="</tr>";
			
		}
		tabHTML +="</table>";
		return tabHTML;
		
	}
	
	public JSONArray doInitJson(String initJson)
	{
		JSONObject response = JSONObject.fromObject(initJson);
		String response_txt = response.getString("response");
		JSONObject data = JSONObject.fromObject(response_txt);
		String data_txt = data.getString("data");
		JSONArray jsonArray = (JSONArray) JSONSerializer.toJSON(data_txt);
		return jsonArray;
	}
	public int getStrLen(String str) {
		if (str == null) {
			return 0;
		}
		byte[] buf = str.getBytes();
		return buf.length;
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		String method = request.getParameter("method");
		//add by zhangbr@ccthanking.com 	添加导出文件的下载和删除处理
		if("expDownload".equals(method)){
			String expID = request.getParameter("f2_expID");
			String fileName = request.getParameter("f2_fileName");
			if(Pub.empty(fileName)){
				fileName = "表格打印";
			}
			String base = AppInit.appPath+"/file";
			String fileDir = base + "/ExpDownload/"+expID;
            File file = new File(fileDir+"/"+fileName+".xls");
            if (file.exists()) {
                int bytes = 0;
				ServletOutputStream op = response.getOutputStream();
				response.setContentType("application/x-msdownload");
				fileName = new String(fileName.getBytes("gb2312"), "ISO8859_1");
				response.setHeader("Content-Disposition",
						"attachment;filename=\"" + fileName + ".xls" + "\"");
				byte[] bbuf = new byte[1024];
				DataInputStream in = new DataInputStream(new FileInputStream(
						file));
				while ((in != null) && ((bytes = in.read(bbuf)) != -1)) {
					op.write(bbuf, 0, bytes);
				}
				in.close();
				op.flush();
				op.close();
				//删除生成的附件
				file.delete();
				//删除附件的GUID文件夹
				file.getParentFile().delete();	
	        }
		}else{
			doGet(request, response);
		}
	}
}