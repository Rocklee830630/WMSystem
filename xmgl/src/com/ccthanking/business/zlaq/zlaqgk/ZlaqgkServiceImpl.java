package com.ccthanking.business.zlaq.zlaqgk;

import java.sql.Connection;

import org.springframework.stereotype.Service;

import com.ccthanking.framework.common.BaseResultSet;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.PageManager;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.util.Pub;
import com.ccthanking.framework.util.RequestUtil;

@Service
public class ZlaqgkServiceImpl  implements ZlaqgkService{

	
	//数据钻取
	@Override
	public String zlaq_gk(String json,User user,String nd,String sqlname,String xmglgs) throws Exception {
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		try {
				PageManager page = RequestUtil.getPageManager(json);
				if (page == null)
					page = new PageManager();
					conn.setAutoCommit(false);
					if(!Pub.empty(xmglgs))
					{
						xmglgs= " and xmglgs='"+xmglgs+"'";
					}
					else
					{
						xmglgs="";
					}	
					String propertiesName="com.ccthanking.properties.business.bmjk.bmjk_zlaq";
					String sql = Pub.getPropertiesSqlValue(propertiesName, sqlname);
					sql+= " and nd='"+nd+"'"+xmglgs;
					BaseResultSet bs = DBUtil.query(conn, sql, page);					
					bs.setFieldDic("ZT", "ZGZT");
					bs.setFieldDic("JCGM", "JCGM");
					bs.setFieldDic("JCLX", "JCLX");
					bs.setFieldTranslater("SGDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");
					bs.setFieldTranslater("JLDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");
					bs.setFieldTranslater("SJDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");
					bs.setFieldTranslater("XMGLGS", "FS_ORG_DEPT", "ROW_ID", "BMJC");
					bs.setFieldTranslater("BDID", "GC_XMBD", "GC_XMBD_ID", "BDBH");				
					domresult = bs.getJson();
		}  catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}

}
