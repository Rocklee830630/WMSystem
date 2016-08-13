package com.ccthanking.business.tcjh.jhgl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.sf.json.JSONObject;

import com.ccthanking.business.tcjh.jhgl.vo.TcjhVO;
import com.ccthanking.framework.util.DateTimeUtil;
import com.ccthanking.framework.util.Pub;

public class GenerateGtt {

	public static String createGtt(TcjhVO vo) throws ParseException {
		String strHTML = "";// 返回字符串

		strHTML += "[";
		strHTML += newString("工期安排", "开工时间", vo.getKgsj(), vo.getKgsj_sj());
		strHTML += newString("", "完工时间", vo.getWgsj(), vo.getWgsj_sj());
		strHTML += newString("可研批复", "", vo.getKypf(), vo.getKypf_sj());
		strHTML += newString("划拨决定书", "", vo.getHpjds(), vo.getHpjds_sj());
		strHTML += newString("工程规划许可证", "", vo.getGcxkz(), vo.getGcxkz_sj());
		strHTML += newString("施工许可", "", vo.getSgxk(), vo.getSgxk_sj());
		strHTML += newString("初步设计批复", "", vo.getCbsjpf(), vo.getCbsjpf_sj());
		strHTML += newString("拆迁图", "", vo.getCqt(), vo.getCqt_sj());
		strHTML += newString("排迁图", "", vo.getPqt(), vo.getPqt_sj());
		strHTML += newString("施工图", "", vo.getSgt(), vo.getSgt_sj());
		strHTML += newString("提报价", "", vo.getTbj(), vo.getTbj_sj());
		strHTML += newString("造价财审", "", vo.getCs(), vo.getCs_sj());
		strHTML += newString("招标监理单位", "", vo.getJldw(), vo.getJldw_sj());
		strHTML += newString("招标施工单位", "", vo.getSgdw(), vo.getSgdw_sj());
		strHTML += newString("征拆", "", vo.getZc(), vo.getZc_sj());
		strHTML += newString("排迁", "", vo.getPq(), vo.getPq_sj());
		strHTML += newString("交工", "", vo.getJg(), vo.getJg_sj());
		strHTML = strHTML.substring(0, strHTML.length() - 1) + "]";
		return strHTML;
	}
	
	public static String createXxjdGtt(JSONObject json, String xmlx) throws ParseException {
		String gttJson = "[";
		
		// 道路工程
		if("1".equals(xmlx)) {
			gttJson += newString("开工" ,"", formatDate(json.get("KGSJ").toString()), formatDate(json.get("KGSJ_SJ").toString()));
			gttJson += newString("土基" ,"", formatDate(json.get("TJJSWCSJ").toString()), formatDate(json.get("TJSJWCSJ").toString()));
			gttJson += newString("基层" ,"", formatDate(json.get("JCJSWCSJ").toString()), formatDate(json.get("JCSJWCSJ").toString()));
			gttJson += newString("面层" ,"", formatDate(json.get("MCJSWCSJ").toString()), formatDate(json.get("MCSJWCSJ").toString()));
			gttJson += newString("方砖" ,"", formatDate(json.get("FZJSWCSJ").toString()), formatDate(json.get("FZSJWCSJ").toString()));
			gttJson += newString("完工" ,"", formatDate(json.get("WGSJ").toString()), formatDate(json.get("WGSJ_SJ").toString()));
		// 排水工程
		} else if("2".equals(xmlx)) {
			gttJson += newString("开工" ,"", formatDate(json.get("KGSJ").toString()), formatDate(json.get("KGSJ_SJ").toString()));
			gttJson += newString("完工" ,"", formatDate(json.get("WGSJ").toString()), formatDate(json.get("WGSJ_SJ").toString()));
		// 桥梁
		} else if("3".equals(xmlx)) {
			gttJson += newString("开工" ,"", formatDate(json.get("KGSJ").toString()), formatDate(json.get("KGSJ_SJ").toString()));
			gttJson += newString("桩基础" ,"", formatDate(json.get("ZJCJSWCSJ").toString()), formatDate(json.get("ZJCSJWCSJ").toString()));
			gttJson += newString("承台及墩柱" ,"", formatDate(json.get("DZJSWCSJ").toString()), formatDate(json.get("DZSJWCSJ").toString()));
			gttJson += newString("桥梁上部结构" ,"", formatDate(json.get("QLSBJSWCSJ").toString()), formatDate(json.get("QLSBSJWCSJ").toString()));
			gttJson += newString("桥梁附属" ,"", formatDate(json.get("QLFSJSWCSJ").toString()), formatDate(json.get("QLFSSJWCSJ").toString()));
			gttJson += newString("完工" ,"", formatDate(json.get("WGSJ").toString()), formatDate(json.get("WGSJ_SJ").toString()));
		// 框构
		} else if("4".equals(xmlx)) {
			gttJson += newString("开工" ,"", formatDate(json.get("KGSJ").toString()), formatDate(json.get("KGSJ_SJ").toString()));
			gttJson += newString("土方" ,"", formatDate(json.get("TFJSWCSJ").toString()), formatDate(json.get("TFSJWCSJ").toString()));
			gttJson += newString("结构" ,"", formatDate(json.get("JGJSWCSJ").toString()), formatDate(json.get("JGSJWCSJ").toString()));
			gttJson += newString("附属" ,"", formatDate(json.get("FSJSWCSJ").toString()), formatDate(json.get("FSSJWCSJ").toString()));
			gttJson += newString("完工" ,"", formatDate(json.get("WGSJ").toString()), formatDate(json.get("WGSJ_SJ").toString()));
		}
		gttJson = gttJson.substring(0, gttJson.length() - 1) + "]";
		return gttJson;
	}

	public static String newString(String title, String label, Date o_time_jh,
			Date o_time_sj) throws ParseException {
		String str = "";
		long time_jh = 0;// 计划时间
		long time_sj = 0;// 实际时间
		Date date = new Date();// 当前日期
		String color = "";// 设置颜色
		String dataObj = "";// 设置连接内容
		String nullType = "";// 设置为空类型
		str += "{name:'" + title + "',desc:'" + label + "',";
		str += "values:[{from:'/Date(";
		if (o_time_jh == null && o_time_sj != null) {
			time_jh = Pub.Date2MS(o_time_sj);
			time_sj = Pub.Date2MS(o_time_sj);
			color = "";
			nullType = "from";
		} else if (o_time_jh == null && o_time_sj == null) {
			time_jh = Pub.Date2MS(formatDate(date));
			time_sj = Pub.Date2MS(formatDate(date));
			color = "";
			nullType = "all";
		} else if (o_time_jh != null && o_time_sj == null) {
			if (Pub.Date2MS(date) > Pub.Date2MS(o_time_jh)) {
				time_jh = Pub.Date2MS(o_time_jh);
				time_sj = Pub.Date2MS(formatDate(date));
				color = "ganttRed";
				nullType = "to";
			} else if (Pub.Date2MS(date) <= Pub.Date2MS(o_time_jh)) {
				time_jh = Pub.Date2MS(formatDate(date));
				time_sj = Pub.Date2MS(o_time_jh);
				color = "ganttGreen";
				nullType = "from";
			}
		} else if (o_time_jh != null && o_time_sj != null) {
			time_jh = Pub.Date2MS(o_time_jh);
			time_sj = Pub.Date2MS(o_time_sj);
			if (time_jh > time_sj) {
				time_jh = Pub.Date2MS(o_time_jh);
				time_sj = Pub.Date2MS(o_time_sj);
				color = "ganttGreen";
				nullType = "";
			} else if (time_jh < time_sj) {
				time_jh = Pub.Date2MS(o_time_jh);
				time_sj = Pub.Date2MS(o_time_sj);
				color = "ganttRed";
				nullType = "";
			} else {
				time_jh = Pub.Date2MS(o_time_jh);
				time_sj = Pub.Date2MS(o_time_sj);
				color = "";
				nullType = "";
			}
		}/*
		 * else { time_jh = Pub.Date2MS(date)+Long.parseLong("100000000");
		 * time_sj = Pub.Date2MS(date); color = ""; }
		 */
		str += time_jh + ")/',to:'/Date(" + time_sj
				+ ")/',label:'',customClass:'" + color + "',dataObj:'"
				+ dataObj + "',nullType:'" + nullType + "'}]},";
		return str;
	}

	// 格式化日期
	public static Date formatDate(Date date) throws ParseException  {
		SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
		String str = formatDate.format(date);
		return formatDate.parse(str);
	}
	
	// 格式化日期
	public static Date formatDate(String date) throws ParseException  {
		return DateTimeUtil.parse(date, "yyyy-MM-dd");
	}
}
