package com.ccthanking.business.xmzhxx;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.poi.hssf.usermodel.HSSFAnchor;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFComment;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ccthanking.framework.Globals;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.model.autocomplete;
import com.ccthanking.framework.model.requestJson;
import com.ccthanking.framework.plugin.AppInit;
import com.ccthanking.framework.util.Pub;

/**
 * @auther xhb 
 */
@Controller
@RequestMapping("/xmzhxxController")
public class XmzhxxController {

	@Autowired
	private XmzhxxServiceImpl xmzhxxServiceImpl;
	
	private static final String MINUS = "—";
	private static final String YES = "√";
	private static final String NO = "×";
	
	/**
	 * 项目综合信息表查询
	 * @param request
	 * @param json 页面传进来的对象json
	 * @return
	 */
	@RequestMapping(params = "queryXmzhxx")
	@ResponseBody
	public requestJson queryXmzhxx(HttpServletRequest request, requestJson json) {
		User user = (User)request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		try {
			String domResult = xmzhxxServiceImpl.queryXmzhxx(json.getMsg(), user);
			j.setMsg(domResult);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return j;
	}
	/**
	 * 项目综合信息表查询带大小类
	 * @param request
	 * @param json 页面传进来的对象json
	 * @return
	 */
	@RequestMapping(params = "queryXmzhxxFenLei")
	@ResponseBody
	public requestJson queryXmzhxxFenLei(HttpServletRequest request, requestJson json) {
		User user = (User)request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		try {
			String domResult = xmzhxxServiceImpl.queryXmzhxxFenLei(json.getMsg(), user);
			j.setMsg(domResult);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return j;
	}
	//项目名称联想查询
	@RequestMapping(params = "xmmcAutoQuery")
	@ResponseBody
	public List<autocomplete> xmmcAutoComplete(final HttpServletRequest request,autocomplete match) throws Exception{
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		List<autocomplete> list  = this.xmzhxxServiceImpl.xmmcAutoComplete(match,user);
		return list;
	}

	//项目标段查询
	@RequestMapping(params = "bdmcAutoQuery")
	@ResponseBody
	public List<autocomplete> bdmcAutoQuery(final HttpServletRequest request,autocomplete match) throws Exception{
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		List<autocomplete> list  = this.xmzhxxServiceImpl.bdmcAutoComplete(match,user);
		return list;
	}
	
	@RequestMapping(params="exportXxzhxx")
	@ResponseBody
	public void ExportXxzhxx(final HttpServletRequest request, final HttpServletResponse response) {
//		User user = (User)request.getSession().getAttribute(Globals.USER_KEY);
		HSSFWorkbook workBook = null;
		
		try {
			String exportFileName = "项目综合信息";
			exportFileName = new String(exportFileName.getBytes("gb2312"), "iso8859-1");
			
			String templatepath = AppInit.appPath + "WEB-INF\\template\\xmzhxxb.xls";//此处为读取模版，需要自定义
			InputStream in = new FileInputStream(templatepath);
			if (in != null) {
				workBook = new HSSFWorkbook(in);
			}
			
			HSSFSheet  sheet = workBook.getSheetAt(0);
			String queryData = request.getParameter("exportQueryResultCondition");
			JSONArray jsonList = Pub.doInitJson(queryData);

			// 默认样式【边框】
			HSSFCellStyle defaultStyle = workBook.createCellStyle();
			defaultStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
			defaultStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			defaultStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			defaultStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
			
			// 右对齐
			HSSFCellStyle rigthStyle = workBook.createCellStyle();
			rigthStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
			rigthStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
			rigthStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			rigthStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			rigthStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);

			// 居中对齐
			HSSFCellStyle centerStyle = workBook.createCellStyle();
			centerStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			centerStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
			centerStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			centerStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			centerStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
			
			// 背景红色
			HSSFCellStyle redBackgroundStyle = workBook.createCellStyle();
			redBackgroundStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
			redBackgroundStyle.setFillForegroundColor(HSSFColor.RED.index);
			redBackgroundStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			redBackgroundStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
			redBackgroundStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
			redBackgroundStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			redBackgroundStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			redBackgroundStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
			
			// 概算背景橙色
			HSSFCellStyle  orangeBackgroundStyle = workBook.createCellStyle();
			orangeBackgroundStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
			orangeBackgroundStyle.setFillForegroundColor(HSSFColor.ORANGE.index);
			orangeBackgroundStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			orangeBackgroundStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
			orangeBackgroundStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
			orangeBackgroundStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			orangeBackgroundStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			orangeBackgroundStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
			
			// 结算背景粉色（自定义）#FF7F7F[<div id='error' title='审减率超过20%'>"+obj.JS_GCJE_SV+"</div>]
			HSSFCellStyle pinkBackgroundStyle = workBook.createCellStyle();
			HSSFPalette customPalette = workBook.getCustomPalette();
	//		HSSFColor customPinkColor = customPalette.addColor((byte)255, (byte)127, (byte)127);
			customPalette.setColorAtIndex(HSSFColor.PINK.index,(byte)255, (byte)127, (byte)127);
			pinkBackgroundStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
			pinkBackgroundStyle.setFillForegroundColor(HSSFColor.PINK.index);
			pinkBackgroundStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			pinkBackgroundStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
			pinkBackgroundStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			pinkBackgroundStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			pinkBackgroundStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
			
			// 结算背景橙色（自定义）#FBC77D[<div id='exception' title='审减率超过15%，少于20%'>"+obj.JS_GCJE_SV+"</div>]
			HSSFCellStyle customOrangeBackgroundStyle = workBook.createCellStyle();
	//		HSSFColor customOrangeColor = customPalette.addColor((byte)251, (byte)199, (byte)125);   这种方式在2007中可以用
			customPalette.setColorAtIndex(HSSFColor.LIGHT_ORANGE.index,(byte)251, (byte)199, (byte)125);
			customOrangeBackgroundStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
			customOrangeBackgroundStyle.setFillForegroundColor(HSSFColor.LIGHT_ORANGE.index);
			customOrangeBackgroundStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			customOrangeBackgroundStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
			customOrangeBackgroundStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			customOrangeBackgroundStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			customOrangeBackgroundStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
			
			// 红色居中
			HSSFCellStyle redStyle = workBook.createCellStyle();
			HSSFFont redFont = workBook.createFont();
			redFont.setColor(HSSFColor.RED.index);
			redFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			redFont.setFontHeightInPoints((short)12);
			redFont.setFontName("黑体");
			redStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			redStyle.setFont(redFont);
			redStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
			redStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			redStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			redStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
			
			// 绿色居中
			HSSFCellStyle greenStyle = workBook.createCellStyle();
			HSSFFont greenFont = workBook.createFont();
			greenFont.setColor(HSSFColor.GREEN.index);
			greenFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			greenFont.setFontHeightInPoints((short)12);
			greenFont.setFontName("黑体");
			greenStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			greenStyle.setFont(greenFont);
			greenStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
			greenStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			greenStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			greenStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);

			// 灰色居中
			HSSFCellStyle greyStyle = workBook.createCellStyle();
			HSSFFont greyFont = workBook.createFont();
			greyFont.setColor(HSSFColor.GREY_40_PERCENT.index);
			greyFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			greyFont.setFontHeightInPoints((short)12);
			greyFont.setFontName("黑体");
			greyStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			greyStyle.setFont(greyFont);
			greyStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
			greyStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			greyStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			greyStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);

			// 黄色居中
			HSSFCellStyle yellowStyle = workBook.createCellStyle();
			HSSFFont yellowFont = workBook.createFont();
			yellowFont.setColor(HSSFColor.YELLOW.index);
			yellowFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			yellowFont.setFontHeightInPoints((short)12);
			yellowFont.setFontName("黑体");
			yellowStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			yellowStyle.setFont(yellowFont);
			yellowStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
			yellowStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			yellowStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			yellowStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);

			Map<String, HSSFCellStyle> styleMap = new HashMap<String, HSSFCellStyle>();
			styleMap.put("1", greyStyle);
			styleMap.put("2", greenStyle);
			styleMap.put("3", greyStyle);
			styleMap.put("4", yellowStyle);
			styleMap.put("5", redStyle);
			
			HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
			HSSFAnchor anchor = new HSSFClientAnchor(0,0,0,0,(short)3,3,(short)9,9);
			
			int r =3;//起始行
			int c = -1;//其实列
			HSSFCell cell = null;
			for (int i = 0; i < jsonList.size(); i++) {
				HSSFRow row = sheet.createRow(r+i);
				JSONObject obj = (JSONObject)jsonList.get(i);
				/*
				// 序号
				cell = row.createCell(0);
				cell.setCellValue(i+1);
				cell.setCellStyle(defaultStyle);*/
				for (int j = 1; j < 65; j++) {
					HSSFComment comment = null;
					
					switch (j) {
					case 1:
						// 项目编号
						cell = row.createCell((c+1));
						cell.setCellValue(obj.getString("XMBH"));
						cell.setCellStyle(centerStyle);
						break;

					case 2:
						// 项目名称
						cell = row.createCell((c+2));
						cell.setCellValue(obj.getString("XMMC"));
						cell.setCellStyle(defaultStyle);
						break;
					case 3:
						// 建设位置
						cell = row.createCell((c+3));
						cell.setCellValue(obj.getString("JSDZ"));
						cell.setCellStyle(defaultStyle);
						break;
					case 4:
						// 项管公司
						cell = row.createCell((c+4));
						if ("".equals(obj.getString("XMGLGS"))) {
							cell.setCellValue(obj.getString("XMGLGS"));
						} else {
							cell.setCellValue(obj.getString("XMGLGS_SV"));
						}
						cell.setCellStyle(defaultStyle);
						break;
					case 5:
						// 标段名称
						cell = row.createCell((c+5));
						if ("".equals(obj.getString("BDMC"))) {
							cell.setCellValue(MINUS);
							cell.setCellStyle(centerStyle);
						} else {
							cell.setCellValue(obj.getString("BDMC"));
							cell.setCellStyle(defaultStyle);
						}
						break;
					case 6:
						// 项目性质
						cell = row.createCell((c+6));
						if ("".equals(obj.getString("XMXZ"))) {
							cell.setCellValue(obj.getString("XMXZ"));
						} else {
							cell.setCellValue(obj.getString("XMXZ_SV"));
						}
						cell.setCellStyle(centerStyle);
						break;
					case 7:
						// 年度目标
						cell = row.createCell((c+7));
						cell.setCellValue(obj.getString("NDMB"));
						cell.setCellStyle(defaultStyle);
						break;
					case 8:
						// 已拆迁
						cell = row.createCell((c+8));
						String ycq = obj.getString("GC_YCQ");
						comment = patriarch.createComment(anchor);
						setCellValue(styleMap, cell, ycq, comment);
						break;
					case 9:
						// 已排迁
						cell = row.createCell((c+9));
						String ypq = obj.getString("GC_YPQ");
						comment = patriarch.createComment(anchor);
						setCellValue(styleMap, cell, ypq, comment);
						break;
					case 10:
						// 已开工
						cell = row.createCell((c+10));
						String ykg = obj.getString("GC_YKG");
						comment = patriarch.createComment(anchor);
						setCellValue(styleMap, cell, ykg, comment);
						break;
					case 11:
						// 已完工
						cell = row.createCell((c+11));
						String ywg = obj.getString("GC_YWG");
						comment = patriarch.createComment(anchor);
						setCellValue(styleMap, cell, ywg, comment);
						break;
					case 12:
						// 已交竣工
						cell = row.createCell((c+12));
						String yjjg = obj.getString("GC_YJJG");
						comment = patriarch.createComment(anchor);
						setCellValue(styleMap, cell, yjjg, comment);
						break;
					case 13:
						// 可研批复
						cell = row.createCell((c+13));
						String kypf = obj.getString("SXBL_KYPF");
						comment = patriarch.createComment(anchor);
						setCellValue(styleMap, cell, kypf, comment);
						break;
					case 14:
						// 土地划拨
						cell = row.createCell((c+14));
						String tdhb = obj.getString("SXBL_TDHB");
						comment = patriarch.createComment(anchor);
						setCellValue(styleMap, cell, tdhb, comment);
						break;
					case 15:
						// 工程规划许可
						cell = row.createCell((c+15));
						String gcghxk = obj.getString("SXBL_GCGHXK");
						comment = patriarch.createComment(anchor);
						setCellValue(styleMap, cell, gcghxk, comment);
						break;
					case 16:
						// 施工许可
						cell = row.createCell((c+16));
						String sgxk = obj.getString("SXBL_SGXK");
						comment = patriarch.createComment(anchor);
						setCellValue(styleMap, cell, sgxk, comment);
						break;
					case 17:
						// 设计任务书样式
						comment = patriarch.createComment(anchor);
						cell = row.createCell((c+17));
						String sjrws = obj.getString("SJ_SJRWS");
						if ("".equals(sjrws)) {
							comment.setString(new HSSFRichTextString("无"));
							cell.setCellValue(NO);
							cell.setCellStyle(redStyle);
							cell.setCellComment(comment);
						} else {
							comment.setString(new HSSFRichTextString("有"));
							cell.setCellValue(YES);
							cell.setCellStyle(greenStyle);
							cell.setCellComment(comment);
						}
						break;
					case 18:
						// 初设批复
						cell = row.createCell((c+18));
						String cspf = obj.getString("SJ_CSPF");
						comment = patriarch.createComment(anchor);
						setCellValue(styleMap, cell, cspf, comment);
						break;
					case 19:
						// 拆迁图
						cell = row.createCell((c+19));
						String cqt = obj.getString("SJ_CQT");
						comment = patriarch.createComment(anchor);
						setCellValue(styleMap, cell, cqt, comment);
						break;
					case 20:
						// 排迁图
						cell = row.createCell((c+20));
						String pqt = obj.getString("SJ_PQT");
						comment = patriarch.createComment(anchor);
						setCellValue(styleMap, cell, pqt, comment);
						break;
					case 21:
						// 施工图
						cell = row.createCell((c+21));
						String sgt = obj.getString("SJ_SGT");
						comment = patriarch.createComment(anchor);
						setCellValue(styleMap, cell, sgt, comment);
						break;
					case 22:
						// 变更数
						cell = row.createCell((c+22));
						cell.setCellValue(obj.getString("SJ_BGS"));
						cell.setCellStyle(rigthStyle);
						break;
					case 23:
						// 拦标价
						cell = row.createCell((c+23));
						cell.setCellValue(obj.getString("LBJ"));
						if ("".equals(obj.getString("LBJ"))) {
							String cs = obj.getString("CS");
							
							if ("".equals(cs)) {
								cell.setCellValue(MINUS);
								cell.setCellStyle(greyStyle);
							} else {
								Date sysDate = new Date();
								Date csDate = new java.text.SimpleDateFormat("yyyy-MM-dd").parse(cs);
								
								if (sysDate.after(csDate)) {
									cell.setCellValue(NO);
									cell.setCellStyle(redStyle);
								} else {
									cell.setCellValue(MINUS);
									cell.setCellStyle(greyStyle);
								}
							}
						} else {
							cell.setCellValue(obj.getString("LBJ_SV"));
							cell.setCellStyle(rigthStyle);
						}
						break;
					case 24:
						// 设计单位
						cell = row.createCell((c+24));
						if ("".equals(obj.getString("ZB_SJDW"))) {
							cell.setCellValue(MINUS);
							cell.setCellStyle(centerStyle);
						} else {
							cell.setCellValue(obj.getString("ZB_SJDW_SV"));
						}
						break;
					case 25:
						// 监理单位
						cell = row.createCell((c+25));
						if ("".equals(obj.getString("ZB_JLDW"))) {
							String jldw = obj.getString("JLDW");
							
							if ("".equals(jldw)) {
								cell.setCellValue(MINUS);
								cell.setCellStyle(greyStyle);
							} else {
								Date sysDate = new Date();
								Date jldwDate = new java.text.SimpleDateFormat("yyyy-MM-dd").parse(jldw);
								
								if (sysDate.after(jldwDate)) {
									cell.setCellValue(NO);
									cell.setCellStyle(redStyle);
								} else {
									cell.setCellValue(MINUS);
									cell.setCellStyle(greyStyle);
								}
							}
						} else {
							cell.setCellValue(obj.getString("ZB_JLDW_SV"));
						}
						break;
					case 26:
						// 施工单位
						cell = row.createCell((c+26));
						if ("".equals(obj.getString("ZB_SGDW"))) {
							String sgdw = obj.getString("SGDW");
							
							if ("".equals(sgdw)) {
								cell.setCellValue(MINUS);
								cell.setCellStyle(greyStyle);
							} else {
								Date sysDate = new Date();
								Date sgdwDate = new java.text.SimpleDateFormat("yyyy-MM-dd").parse(sgdw);
								
								if (sysDate.after(sgdwDate)) {
									cell.setCellValue(NO);
									cell.setCellStyle(redStyle);
								} else {
									cell.setCellValue(MINUS);
									cell.setCellStyle(greyStyle);
								}
							}
						} else {
							cell.setCellValue(obj.getString("ZB_SGDW_SV"));
						}
						break;
					case 27:
						// 排迁任务数/完成数
						cell = row.createCell((c+27));
						cell.setCellValue(obj.getString("PQ_RWS_WCS"));
						cell.setCellStyle(rigthStyle);
						break;
						
					case 28:
						// 总合同金额（元）
						cell = row.createCell((c+28));
						cell.setCellValue(obj.getString("PQ_ZHTJE_SV"));
						cell.setCellStyle(rigthStyle);
						break;
					case 29:
						// 已支付（元）
						cell = row.createCell((c+29));
						cell.setCellValue(obj.getString("PQ_YZF_SV"));
						cell.setCellStyle(rigthStyle);
						break;
					case 30:
						// %
						cell = row.createCell((c+30));
						cell.setCellValue(obj.getString("PQ_BFB_SV"));
						cell.setCellStyle(rigthStyle);
						break;
					case 31:
						// 总居民数/已完拆迁
						cell = row.createCell((c+31));
						if ("0".equals(obj.getString("XMBS"))) {
							cell.setCellValue(obj.getString("ZS_ZJMS_JWCQ"));
							cell.setCellStyle(rigthStyle);
						} else {
							cell.setCellValue(MINUS);
							cell.setCellStyle(greyStyle);
						}
						break;
						
					case 32:
						// 总企业数/已完拆迁
						cell = row.createCell((c+32));
						if ("0".equals(obj.getString("XMBS"))) {
							cell.setCellValue(obj.getString("ZS_ZQYS_YWCQ"));
							cell.setCellStyle(rigthStyle);
						} else {
							cell.setCellValue(MINUS);
							cell.setCellStyle(greyStyle);
						}
						break;
					case 33:
						// 征地面积/已完征地
						cell = row.createCell((c+33));
						if ("0".equals(obj.getString("XMBS"))) {
							cell.setCellValue(obj.getString("ZS_ZDMJ_YWZD"));
							cell.setCellStyle(rigthStyle);
						} else {
							cell.setCellValue(MINUS);
							cell.setCellStyle(greyStyle);
						}
						break;
					case 34:
						// 拨入资金（元）
						cell = row.createCell((c+34));
						if ("0".equals(obj.getString("XMBS"))) {
							cell.setCellValue(obj.getString("ZS_BRZJ_SV"));
							cell.setCellStyle(rigthStyle);
						} else {
							cell.setCellValue(MINUS);
							cell.setCellStyle(greyStyle);
						}
						break;
					case 35:
						// 已使用资金（元）
						cell = row.createCell((c+35));
						if ("0".equals(obj.getString("XMBS"))) {
							cell.setCellValue(obj.getString("ZS_YSYZJ_SV"));
							cell.setCellStyle(rigthStyle);
						} else {
							cell.setCellValue(MINUS);
							cell.setCellStyle(greyStyle);
						}
						break;
					case 36:
						// 余额（元）
						cell = row.createCell((c+36));
						if ("0".equals(obj.getString("XMBS"))) {
							double ye = Double.parseDouble(obj.getString("ZS_YE"));
							if (ye >= 0) {
								cell.setCellValue(obj.getString("ZS_YE_SV"));
								cell.setCellStyle(rigthStyle);
							} else {
								cell.setCellValue(obj.getString("ZS_YE_SV"));
								cell.setCellStyle(redBackgroundStyle);
							}
						} else {
							cell.setCellValue(MINUS);
							cell.setCellStyle(greyStyle);
						}
						break;
					case 37:
						// 已签合同数
						cell = row.createCell((c+37));
						cell.setCellValue(obj.getString("HT_JQHTS"));
						cell.setCellStyle(rigthStyle);
						break;
					case 38:
						// 征收合同（虚拟）金额（元）
						cell = row.createCell((c+38));
						cell.setCellValue(obj.getString("HT_ZSHTJE_SV"));
						cell.setCellStyle(rigthStyle);
						break;

					case 39:
						// 排迁合同金额（元）
						cell = row.createCell((c+39));
						cell.setCellValue(obj.getString("HT_PQHTJE_SV"));
						cell.setCellStyle(rigthStyle);
						break;
					case 40:
						// 设计合同金额（元）
						cell = row.createCell((c+40));
						cell.setCellValue(obj.getString("HT_SJHTJE_SV"));
						cell.setCellStyle(rigthStyle);
						break;
					case 41:
						// 监理合同金额（元）
						cell = row.createCell((c+41));
						cell.setCellValue(obj.getString("HT_JLHTJE_SV"));
						cell.setCellStyle(rigthStyle);
						break;
					case 42:
						// 施工合同金额（元）
						cell = row.createCell((c+42));
						cell.setCellValue(obj.getString("HT_SGHTJE_SV"));
						cell.setCellStyle(rigthStyle);
						break;
					case 43:
						// 其他合同金额（元）
						cell = row.createCell((c+43));
						cell.setCellValue(obj.getString("HT_QTHTJE_SV"));
						cell.setCellStyle(rigthStyle);
						break;
					case 44:
						// 合同总金额（元）
						cell = row.createCell((c+44));
						cell.setCellValue(obj.getString("HT_ZHTJE_SV"));
						cell.setCellStyle(rigthStyle);
						break;
					case 45:
						// 完成投资（元）
						cell = row.createCell((c+45));
						cell.setCellValue(obj.getString("HT_WCTZ_SV"));
						cell.setCellStyle(rigthStyle);
						break;
					case 46:
						// %
						cell = row.createCell((c+46));
						cell.setCellValue(obj.getString("HT_BFB1_SV"));
						cell.setCellStyle(rigthStyle);
						break;
					case 47:
						// 已支付（元）
						cell = row.createCell((c+47));
						cell.setCellValue(obj.getString("HT_YZF_SV"));
						cell.setCellStyle(rigthStyle);
						break;
					case 48:
						// %
						cell = row.createCell((c+48));
						cell.setCellValue(obj.getString("HT_BFB2_SV"));
						cell.setCellStyle(rigthStyle);
						break;
					case 49:
						// 投资估算（万元）工程
						cell = row.createCell((c+49));
						if ("0".equals(obj.getString("XMBS"))) {
							cell.setCellValue(obj.getString("TZ_GC_SV"));
							cell.setCellStyle(rigthStyle);
						} else {
							cell.setCellValue(MINUS);
							cell.setCellStyle(greyStyle);
						}
						break;
					case 50:
						// 投资估算（万元）征拆
						cell = row.createCell((c+50));
						if ("0".equals(obj.getString("XMBS"))) {
							cell.setCellValue(obj.getString("TZ_ZC_SV"));
							cell.setCellStyle(rigthStyle);
						} else {
							cell.setCellValue(MINUS);
							cell.setCellStyle(greyStyle);
						}
						break;
					case 51:
						// 投资估算（万元）其他
						cell = row.createCell((c+51));
						if ("0".equals(obj.getString("XMBS"))) {
							cell.setCellValue(obj.getString("TZ_QT_SV"));
							cell.setCellStyle(rigthStyle);
						} else {
							cell.setCellValue(MINUS);
							cell.setCellStyle(greyStyle);
						}
						break;
					case 52:
						// 投资估算（万元）总投资
						cell = row.createCell((c+52));
						if ("0".equals(obj.getString("XMBS"))) {
							cell.setCellValue(obj.getString("TZ_ZTZ_SV"));
							cell.setCellStyle(rigthStyle);
						} else {
							cell.setCellValue(MINUS);
							cell.setCellStyle(greyStyle);
						}
						break;
					case 53:
						// 概算信息（元）工程
						cell = row.createCell((c+53));
						if ("0".equals(obj.getString("XMBS"))) {
							cell.setCellValue(obj.getString("GS_GC_SV"));
							cell.setCellStyle(rigthStyle);
						} else {
							cell.setCellValue(MINUS);
							cell.setCellStyle(greyStyle);
						}
						break;
					case 54:
						// 概算信息（元）征拆
						cell = row.createCell((c+54));
						if ("0".equals(obj.getString("XMBS"))) {
							cell.setCellValue(obj.getString("GS_ZC_SV"));
							cell.setCellStyle(rigthStyle);
						} else {
							cell.setCellValue(MINUS);
							cell.setCellStyle(greyStyle);
						}
						break;
					case 55:
						// 概算信息（元）其他
						cell = row.createCell((c+55));
						if ("0".equals(obj.getString("XMBS"))) {
							cell.setCellValue(obj.getString("GS_QT_SV"));
							cell.setCellStyle(rigthStyle);
						} else {
							cell.setCellValue(MINUS);
							cell.setCellStyle(greyStyle);
						}
						break;
					case 56:
						// 概算信息（元）总投资
						cell = row.createCell((c+56));
						if ("0".equals(obj.getString("XMBS"))) {
							double gs_ztz = Double.parseDouble(obj.getString("GS_ZTZ"));
							double tz_ztz = Double.parseDouble(obj.getString("TZ_ZTZ"));
							
							if (gs_ztz > tz_ztz) {
								cell.setCellValue(obj.getString("GS_ZTZ_SV"));
								cell.setCellStyle(orangeBackgroundStyle);
							} else {
								cell.setCellValue(obj.getString("GS_ZTZ_SV"));
								cell.setCellStyle(rigthStyle);
							}
						} else {
							cell.setCellValue(MINUS);
							cell.setCellStyle(greyStyle);
						}
						break;
					case 57:
						// 资金支付信息（元）工程费用
						cell = row.createCell((c+57));
						cell.setCellValue(obj.getString("ZJZF_GCFY_SV"));
						cell.setCellStyle(rigthStyle);
						break;
					case 58:
						// 资金支付信息（元）征拆费用
						cell = row.createCell((c+58));
						cell.setCellValue(obj.getString("ZJZF_ZCFY_SV"));
						cell.setCellStyle(rigthStyle);
						break;
					case 59:
						// 资金支付信息（元）其他费用
						cell = row.createCell((c+59));
						cell.setCellValue(obj.getString("ZJZF_QTFY_SV"));
						cell.setCellStyle(rigthStyle);
						break;
					case 60:
						// 资金支付信息（元）总支付费用
						cell = row.createCell((c+60));
						cell.setCellValue(obj.getString("ZJZF_ZZFFY_SV"));
						cell.setCellStyle(rigthStyle);
						break;
					case 61:
						// 结算信息（元）工程金额
						cell = row.createCell((c+61));
						double sjb = Double.parseDouble(obj.getString("SJB"));
						if (sjb > 0.15 && sjb < 0.2) {
							cell.setCellValue(obj.getString("ZJZF_GCFY_SV"));
							cell.setCellStyle(customOrangeBackgroundStyle);
						} else if (sjb > 0.2) {
							cell.setCellValue(obj.getString("ZJZF_GCFY_SV"));
							cell.setCellStyle(pinkBackgroundStyle);
						} else {
							cell.setCellValue(obj.getString("ZJZF_GCFY_SV"));
							cell.setCellStyle(rigthStyle);
						}
						break;
					case 62:
						// 结算信息（元）征拆金额
						cell = row.createCell((c+62));
						cell.setCellValue(obj.getString("JS_ZCJE_SV"));
						cell.setCellStyle(rigthStyle);
						break;
					case 63:
						// 结算信息（元）其他金额
						cell = row.createCell((c+63));
						cell.setCellValue(obj.getString("JS_QTJE_SV"));
						cell.setCellStyle(rigthStyle);
						break;
					case 64:
						// 结算信息（元）总结算金额
						cell = row.createCell((c+64));
						cell.setCellValue(obj.getString("JS_ZJSJE_SV"));
						cell.setCellStyle(rigthStyle);
						break;
					default:
						break;
					}
				}

			}
	        response.setHeader("Content-type", "application/vnd.ms-excel");
	        response.setHeader("Content-Disposition", "attachment; filename=" + exportFileName + ".xls");
	        
	        ServletOutputStream fileOut =response.getOutputStream();  
	        workBook.write(fileOut);  
	        fileOut.flush();  
	        fileOut.close();  
		} catch (Exception e) {
			System.out.println("[Info: ] User canceled - " + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * 
		1：不需要办理
		2：按期完成、
		3：未完成未到期
		4：延期完成
		5：到期未完成
		//各节点样式。不需要办理：灰√；按期完成:绿√；延期完成：黄√；未完成未到期：灰—；到期未完成：红× */
	private void setCellValue(Map<String, HSSFCellStyle> styleMap, HSSFCell cell, String xmzt, HSSFComment comment) {
		if ("1".equals(xmzt)) {
			comment.setString(new HSSFRichTextString("不需要办理!"));
			cell.setCellValue(YES);
		} else if ("2".equals(xmzt)) {
			comment.setString(new HSSFRichTextString("按期完成!"));
			cell.setCellValue(YES);
		} else if ("3".equals(xmzt)) {
			comment.setString(new HSSFRichTextString("未完成未到期!"));
			cell.setCellValue(MINUS);
		} else if ("4".equals(xmzt)) {
			comment.setString(new HSSFRichTextString("延期完成!"));
			cell.setCellValue(YES);
		} else if ("5".equals(xmzt)) {
			comment.setString(new HSSFRichTextString("到期未完成!"));
			cell.setCellValue(NO);
		}
		cell.setCellStyle(styleMap.get(xmzt));
		cell.setCellComment(comment);
	}
}
