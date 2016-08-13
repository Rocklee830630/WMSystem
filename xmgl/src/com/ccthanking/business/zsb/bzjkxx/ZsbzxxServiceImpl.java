package com.ccthanking.business.zsb.bzjkxx;

import java.sql.Connection;

import org.springframework.stereotype.Service;

import com.ccthanking.framework.common.BaseResultSet;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.PageManager;
import com.ccthanking.framework.util.Pub;
import com.ccthanking.framework.util.RequestUtil;

/**
 * @auther xhb 
 */
@Service
public class ZsbzxxServiceImpl {

	public String queryZsxx(String json, String lujing, String mingchen,String tiaojian, String nd) {
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		try {

		PageManager page = RequestUtil.getPageManager(json);
		if (page == null)
			page = new PageManager();
			String propertiesName = "com.ccthanking.properties.business.bmjk.bmjk_zsb";
			
			String sql = Pub.getPropertiesSqlValue(propertiesName, mingchen);
			if ("MDWCXMS".equals(tiaojian)) { //摸底完成项目数
				
				sql=sql+"   AND jh.BDID IS NULL AND jh.ISZC = '1' AND GC_JH_SJ_ID IN (select distinct JHSJID GC_JH_SJ_ID   from ( SELECT JHSJID, SUM(DECODE(MDWCRQ, NULL, 1, 0)) AS MARK FROM GC_ZSB_XXB  " +
						"   WHERE SFYX = '1'  GROUP BY JHSJID )xxb where  MARK = '0' ) and jh.nd="+nd;
			
				
			} else if ("TDFWYJXMS".equals(tiaojian)) { //土地房屋移交项目数
				
				sql=sql+ "  AND jh.BDID IS NULL AND jh.ISZC = '1'  AND GC_JH_SJ_ID IN (select distinct JHSJID GC_JH_SJ_ID   from ( SELECT JHSJID, SUM(DECODE(TDFWYJRQ, NULL, 1, 0)) AS MARK FROM GC_ZSB_XXB  " +
						 "   WHERE SFYX = '1'  GROUP BY JHSJID )xxb where  MARK = '0' ) and jh.nd="+nd;
			
			}else if ("WCCQXMS".equals(tiaojian)) { //土地
				
				sql=sql+"    AND jhsj.BDID IS NULL AND jhsj.ISZC = '1'  AND jhsj.ZC_SJ IS NOT NULL and jhsj.nd="+nd;
			
			}else if ("CDYJXMS".equals(tiaojian)) { //施工许可
				
				sql=sql+"   AND JHSJ.BDID IS NULL AND JHSJ.ISZC='1' AND 	JHSJ.GC_JH_SJ_ID IN (SELECT JHSJID FROM GC_ZSB_XMB WHERE SFYX='1' " +
						"   AND CDSJSJ IS NOT NULL) and  jhsj.nd="+nd;
			
			}
			
			if (("WCCQXMS".equals(tiaojian))||("CDYJXMS".equals(tiaojian))){
				sql=sql+" order by jhsj.xmbh, jhsj.PXH asc";
			 }else{
				 sql=sql+"   order by  xxb.xmid,xxb.lrsj desc ";
			 }
			
			
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldDic("qy","QY");
			bs.setFieldThousand("XYJE");
			bs.setFieldThousand("MDGS");//摸底估算
			bs.setFieldThousand("ZJDWJE");//资金到位金额
			bs.setFieldFileUpload("mdfj", "1111");
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
