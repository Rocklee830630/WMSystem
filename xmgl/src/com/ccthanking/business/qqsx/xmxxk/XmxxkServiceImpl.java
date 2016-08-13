package com.ccthanking.business.qqsx.xmxxk;

import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.ccthanking.common.BusinessUtil;
import com.ccthanking.common.YwlxManager;
import com.ccthanking.framework.Globals;
import com.ccthanking.framework.common.BaseResultSet;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.PageManager;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.log.LogManager;
import com.ccthanking.framework.util.RequestUtil;


@Service
public  class XmxxkServiceImpl implements XmxxkService {
	private String ywlx=YwlxManager.GC_QQSX_TTSP;

	@Override
	public String exist(HttpServletRequest request,String json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		Connection conn = DBUtil.getConnection();
		String sjwybh=request.getParameter("sjwybh");
		String domresult = "";
		try {
			conn.setAutoCommit(false);
			String sql="select " +
						"sum(decode(fjlb,0007,1,0)) as \"0007\"," +
						"sum(decode(fjlb,0008,1,0)) as \"0008\"," +
						"sum(decode(fjlb,0009,1,0)) as \"0009\", " +
						//土地
						"sum(decode(fjlb,0010,1,0)) as \"0010\"," +
						"sum(decode(fjlb,0011,1,0)) as \"0011\"," +
						"sum(decode(fjlb,0012,1,0)) as \"0012\"," +
						"sum(decode(fjlb,0013,1,0)) as \"0013\"," +
						"sum(decode(fjlb,0014,1,0)) as \"0014\", " +
						//
						"sum(decode(fjlb,0015,1,0)) as \"0015\"," +
						"sum(decode(fjlb,0016,1,0)) as \"0016\"," +
						"sum(decode(fjlb,0017,1,0)) as \"0017\"," +
						"sum(decode(fjlb,0018,1,0)) as \"0018\"," +
						"sum(decode(fjlb,0019,1,0)) as \"0019\"," +
						"sum(decode(fjlb,0020,1,0)) as \"0020\"," +
						"sum(decode(fjlb,0021,1,0)) as \"0021\"," +
						"sum(decode(fjlb,0022,1,0)) as \"0022\", " +
						"sum(decode(fjlb,0023,1,0)) as \"0023\", " +
						"sum(decode(fjlb,0024,1,0)) as \"0024\", " +
						"sum(decode(fjlb,0025,1,0)) as \"0025\", " +
						//
						"sum(decode(fjlb,2020,1,0)) as \"2020\"," +
						"sum(decode(fjlb,2021,1,0)) as \"2021\"," +
						"sum(decode(fjlb,2022,1,0)) as \"2022\"," +
						"sum(decode(fjlb,2023,1,0)) as \"2023\"," +
						"sum(decode(fjlb,2024,1,0)) as \"2024\" " +
						"from FS_FILEUPLOAD where WYBH='"+sjwybh+"' and zhux='1' and fjzt='1'" ;
			BaseResultSet bs = DBUtil.query(conn, sql, null);
			domresult = bs.getJson();
			conn.commit();
		} catch (Exception e) {
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	@Override
	public String querySxxx(HttpServletRequest request,String json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		Connection conn = null;
		String sjwybh=request.getParameter("sjwybh");
		String domresult = "";
		try {
			conn = DBUtil.getConnection();
			conn.setAutoCommit(false); 
			String sql="select " +
					//立项信息查询
					"max(decode(fjlx,2020,blsj,'')) as \"2020\"," +
					"max(decode(fjlx,2021,blsj,'')) as \"2021\"," +
					"max(decode(fjlx,2022,blsj,'')) as \"2022\"," +
					"max(decode(fjlx,2023,blsj,'')) as \"2023\"," +
					"max(decode(fjlx,2024,blsj,'')) as \"2024\"," +
					"max(decode(dfl,0,czwt,'')) as \"0\" ," +
					//规划信息查询
					"max(decode(fjlx,0007,blsj,'')) as \"0007\"," +
					"max(decode(fjlx,0008,blsj,'')) as \"0008\"," +
					"max(decode(fjlx,0009,blsj,'')) as \"0009\"," +
					"max(decode(dfl,1,czwt,'')) as \"1\" ," +
					//土地信息查询
					"max(decode(fjlx,0010,blsj,'')) as \"0010\"," +
					"max(decode(fjlx,0011,blsj,'')) as \"0011\"," +
					"max(decode(fjlx,0012,blsj,'')) as \"0012\"," +
					"max(decode(fjlx,0013,blsj,'')) as \"0013\"," +
					"max(decode(fjlx,0014,blsj,'')) as \"0014\"," +
					"max(decode(fjlx,0023,blsj,'')) as \"0023\"," +
					"max(decode(fjlx,0024,blsj,'')) as \"0024\"," +
					"max(decode(fjlx,0025,blsj,'')) as \"0025\"," +
					"max(decode(dfl,2,czwt,'')) as \"2\" ," +
					//施工信息查询
					"max(decode(fjlx,0015,blsj,'')) as \"0015\",max(decode(fjlx,0016,blsj,'')) as \"0016\",max(decode(fjlx,0017,blsj,'')) as \"0017\",max(decode(fjlx,0018,blsj,'')) as \"0018\",max(decode(fjlx,0019,blsj,'')) as \"0019\",max(decode(fjlx,0020,blsj,'')) as \"0020\",max(decode(fjlx,0021,blsj,'')) as \"0021\",max(decode(fjlx,0022,blsj,'')) as \"0022\",max(decode(dfl,3,czwt,'')) as \"3\" " +
					"from gc_qqsx_sxfj sxfj where sxfj.sfyx='1' and sxfj.sjwybh='"+sjwybh+"'" ;
			BaseResultSet bs = DBUtil.query(conn, sql, null);
			domresult = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
}
