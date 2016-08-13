package com.ccthanking.business.qqsx.bzjkxx;

import java.sql.Connection;

import org.springframework.stereotype.Service;

import com.ccthanking.framework.common.BaseResultSet;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.PageManager;
import com.ccthanking.framework.util.Pub;
import com.ccthanking.framework.util.RequestUtil;

/**
 * @auther zhaiyl
 */
@Service
public class QqxsbzxxServiceImpl {

	public String queryQqsx(String json, String lujing, String mingchen,String tiaojian, String nd) {
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		try {

		PageManager page = RequestUtil.getPageManager(json);
		if (page == null)
			page = new PageManager();
			String propertiesName = "com.ccthanking.properties.business.bmjk.bmjk_qqb";
			
			String sql = Pub.getPropertiesSqlValue(propertiesName, mingchen);
			if ("LX".equals(tiaojian)) { //立项
				
				sql=sql+" and jhsj.bdid is null  AND jhsj.SFYX = '1' AND JHSJ.ISKYPF = '1' AND jhsj.KYPF_SJ IS NOT NULL  and jhsj.nd="+nd;
				
			} else if ("GH".equals(tiaojian)) { //规划
				
				sql=sql+" and jhsj.bdid is null  AND jhsj.SFYX = '1' AND JHSJ.ISGCXKZ = '1' AND jhsj.GCXKZ_SJ IS NOT NULL  and jhsj.nd="+nd;
			
			}else if ("TD".equals(tiaojian)) { //土地
				
				sql=sql+" and jhsj.bdid is null  AND jhsj.SFYX = '1' AND JHSJ.ISHPJDS = '1' AND jhsj.HPJDS_SJ IS NOT NULL  and jhsj.nd="+nd;
			
			}else if ("SG".equals(tiaojian)) { //施工许可
				
				sql=sql+"   AND JHSJ.GC_JH_SJ_ID NOT IN (SELECT GC_JH_SJ_ID FROM GC_JH_SJ WHERE XMBS = '0'  AND ISNOBDXM = '0') " +
						"   AND JHSJ.ISSGXK = '1' AND JHSJ.SGXK_SJ IS NOT NULL and jhsj.nd="+nd;
			
			}
			if("CL".equals(tiaojian))
			{
				sql=sql+" order by  B.xmbh,B.xmbs,B.pxh asc";
			}else{
				sql=sql+"   order by  JHSJ.xmbh,JHSJ.xmbs,JHSJ.pxh asc ";
			}
			
			
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			//字典翻译
			if ("LX".equals(tiaojian)) { //立项
				
				bs.setFieldDic("BBLSX", "LXKYFJLX");
				bs.setFieldFileUpload("XMJYSPF", "2020");
				bs.setFieldFileUpload("HPPF", "2021");
				bs.setFieldFileUpload("TDYJH", "2022");
				bs.setFieldFileUpload("GDZCTZXM", "2023");
				bs.setFieldFileUpload("KYPF", "2024");
				
			} else if ("GH".equals(tiaojian)) { //规划
				
				bs.setFieldDic("BBLSX", "GHSX");
				bs.setFieldFileUpload("JSXZXMYJS", "0100");
				bs.setFieldFileUpload("JSYDGHXKZ", "0101");
				bs.setFieldFileUpload("JSGCGHXKZ", "0102");
				bs.setFieldFileUpload("BBLFJ", "0103");
			
			}else if ("TD".equals(tiaojian)) { //土地
				
				bs.setFieldDic("BBLSX", "TDSPSX");
				bs.setFieldFileUpload("YDYS", "0106");
				bs.setFieldFileUpload("JTTDZD", "0101");
				bs.setFieldFileUpload("GDSX", "0102");
				bs.setFieldFileUpload("TDDJ", "0104");
				bs.setFieldFileUpload("TDSYZ", "0105");
				bs.setFieldFileUpload("BBLFJ", "0103");
			}else if ("SG".equals(tiaojian)) { //施工许可
				
				bs.setFieldDic("BBLSX", "SGXKSX");
				bs.setFieldFileUpload("BJ", "0104");
				bs.setFieldFileUpload("QTSX", "0102");
				bs.setFieldFileUpload("ZLJD", "0102");
				bs.setFieldFileUpload("AQJD", "0102");
				bs.setFieldFileUpload("ZJGL", "0102");
				bs.setFieldFileUpload("STQ", "0102");
				bs.setFieldFileUpload("ZFJC", "0102");
				bs.setFieldFileUpload("SGXK", "0102");
				bs.setFieldFileUpload("BBLFJ", "0103");
			}
		
			domresult = bs.getJson();
		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
}
