package com.ccthanking.business.bgs;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ccthanking.business.pqgl.bmgk.service.impl.PqBmgkServiceImpl;
import com.ccthanking.common.BusinessUtil;
import com.ccthanking.common.EventManager;
import com.ccthanking.common.YwlxManager;
import com.ccthanking.common.vo.EventVO;
import com.ccthanking.common.vo.XmxxVO;
import com.ccthanking.framework.Globals;
import com.ccthanking.framework.CommonChart.showchart.chart.ChartUtil;
import com.ccthanking.framework.base.BaseDAO;
import com.ccthanking.framework.coreapp.orgmanage.UserManager;
import com.ccthanking.framework.common.BaseResultSet;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.PageManager;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.fileUpload.service.FileUploadService;
import com.ccthanking.framework.log.LogManager;
import com.ccthanking.framework.model.requestJson;
import com.ccthanking.framework.service.UserService;
import com.ccthanking.framework.util.Encipher;
import com.ccthanking.framework.util.Pub;
import com.ccthanking.framework.util.QueryConditionList;
import com.ccthanking.framework.util.RandomGUID;
import com.ccthanking.framework.util.RequestUtil;


@Service
public class BgsBmgkServiceImpl implements BgsBmgkService {
	private String propertyFileName;
    private ResourceBundle resourceBundle;
    public BgsBmgkServiceImpl() {
    	Locale locale = Locale.getDefault();
        propertyFileName = "com.ccthanking.properties.business.bmgk.bgs";
        resourceBundle = ResourceBundle.getBundle(propertyFileName,locale);
    }
    public String getString(String key) {
        if (key == null || key.equals("") || key.equals("null")) {
            return "";
        }
        String result = "";
        try {
            result = new String(resourceBundle.getString(key).getBytes("iso-8859-1"),"UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
	@Override
	public String TongChouJiHuaQuery(HttpServletRequest request,User user) throws Exception {
		Connection conn = null;
		String domresult = "";
		String nd = request.getParameter("nd");
		try {
			conn = DBUtil.getConnection();
			PageManager page = null;
			String sql = this.getString("TCJHGK");
			//拼接年度查询条件
			nd = Pub.empty(nd)?"":"T.ND='"+nd+"'";
			String nd1 = Pub.empty(nd)?"":"T1.ND='"+nd+"'";
			String sql1=sql.replaceAll("NDTJ", nd);
			sql1=sql.replaceAll("NDTJ1", nd1);
			page.setFilter(nd);
			conn.setAutoCommit(false);
			BaseResultSet bs = DBUtil.query(conn, sql1, page);
			/*bs.setFieldDecimals("JBPQTXMBFB");	//具备排迁图项目百分比
			bs.setFieldDecimals("YWCPQXMBFB");	//已完成排迁项目百分比
			bs.setFieldDecimals("CDYJXMBFB");	//场地移交项目百分比
			bs.setFieldDecimals("AQWCXMBFB");	//按期完成项目百分比
			bs.setFieldDecimals("YQWCXMBFB");	//延期完成项目百分比
			bs.setFieldDecimals("CQWCXMBFB");	//超期完成项目百分比
*/			domresult = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace(System.out);
			throw e;
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	@Override
	public String faWenQingKuang(HttpServletRequest request, User user) throws Exception {
		Connection conn = null;
		String domresult = "";
		String nd = request.getParameter("nd");
		try {
			conn = DBUtil.getConnection();
			PageManager page = null;
			String sql = this.getString("FEGL");
			//拼接年度查询条件
			nd = Pub.empty(nd)?"":" and to_char(a.ngrq,'yyyy')='"+nd+"'";
			String sql1=sql.replaceAll("NDTJ", nd);
			conn.setAutoCommit(false);
			BaseResultSet bs = DBUtil.query(conn, sql1, page);
			bs.setFieldTranslater("NGDW", "FS_ORG_DEPT", "ROW_ID", "BMJC");
 		 domresult = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace(System.out);
			throw e;
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	@Override
	public String shouWenQingKuang(HttpServletRequest request, User user) throws Exception {
		Connection conn = null;
		String domresult = "";
		String nd = request.getParameter("nd");
		try {
			conn = DBUtil.getConnection();
			PageManager page = null;
			String sql = this.getString("SWGL");
			//拼接年度查询条件
			nd = Pub.empty(nd)?"":" and to_char(a.SWRQ,'yyyy')='"+nd+"'";
			String sql1=sql.replaceAll("NDTJ", nd);
			conn.setAutoCommit(false);
			BaseResultSet bs = DBUtil.query(conn, sql1, page);
			bs.setFieldTranslater("WZ", "FS_COMMON_DICT", "DICT_ID", "DICT_NAME");
 		 domresult = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace(System.out);
			throw e;
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	@Override
	public String liuChengQingKuang(HttpServletRequest request, User user)	throws Exception {
		Connection conn = null;
		String domresult = "";
		String nd = request.getParameter("nd");
		try {
			conn = DBUtil.getConnection();
			PageManager page = null;
			String sql = this.getString("LCZXQK");
			//拼接年度查询条件
			nd = Pub.empty(nd)?"":"  and to_char(ap.createtime,'yyyy')='"+nd+"' ";
			String sql1=sql.replaceAll("NDTJ", nd);
			conn.setAutoCommit(false);
			BaseResultSet bs = DBUtil.query(conn, sql1, page);
			bs.setFieldTranslater("CJDWDM", "FS_ORG_DEPT", "ROW_ID", "BMJC");
 		 domresult = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace(System.out);
			throw e;
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	@Override
	public String cunZaiWenTiQingKuang(HttpServletRequest request, User user) throws Exception {
		Connection conn = null;
		String domresult = "";
		String nd = request.getParameter("nd");
		try {
			conn = DBUtil.getConnection();
			PageManager page =  null;
			String sql = this.getString("CZWT");
			//拼接年度查询条件
			nd = Pub.empty(nd)?"":"  and to_char(lrsj,'yyyy')='"+nd+"' ";
			String sql1=sql.replaceAll("NDTJ", nd);
			conn.setAutoCommit(false);
			BaseResultSet bs = DBUtil.query(conn, sql1, page);
			bs.setFieldTranslater("LRBM", "FS_ORG_DEPT", "ROW_ID", "BMJC");
 		 domresult = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace(System.out);
			throw e;
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	@Override
	public String tongChouJiHuaQingKuang(HttpServletRequest request, User user) throws Exception {
		Connection conn = null;
		String domresult = "";
		String nd = request.getParameter("nd");
		try {
			conn = DBUtil.getConnection();
			PageManager page = null;
			String sql = this.getString("TCJHJD");
			//拼接年度查询条件
			nd = Pub.empty(nd)?"":"  and nd='"+nd+"' ";
			String sql1=sql.replaceAll("NDTJ", nd);
			conn.setAutoCommit(false);
			BaseResultSet bs = DBUtil.query(conn, sql1, page);
			bs.setFieldTranslater("LRBM", "FS_ORG_DEPT", "ROW_ID", "BMJC");
 		 domresult = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace(System.out);
			throw e;
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
}


