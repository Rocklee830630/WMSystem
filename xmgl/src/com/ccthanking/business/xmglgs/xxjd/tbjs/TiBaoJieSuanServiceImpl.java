package com.ccthanking.business.xmglgs.xxjd.tbjs;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ccthanking.business.htgl.service.impl.GcHtglHtServiceImpl;
import com.ccthanking.business.htgl.vo.GcHtglHtsjVO;
import com.ccthanking.business.tcjh.jhgl.vo.FkqkVO;
import com.ccthanking.business.tcjh.jhgl.vo.TcjhVO;
import com.ccthanking.business.zjb.jsgl.GcZjbJsbVO;
import com.ccthanking.business.zjb.lbj.dyqk.GcZjbDyqkVO;
import com.ccthanking.common.BusinessUtil;
import com.ccthanking.common.EventManager;
import com.ccthanking.common.YwlxManager;
import com.ccthanking.common.vo.EventVO;
import com.ccthanking.common.vo.XmxxVO;
import com.ccthanking.framework.Globals;
import com.ccthanking.framework.base.BaseDAO;
import com.ccthanking.framework.coreapp.orgmanage.UserManager;
import com.ccthanking.framework.common.BaseResultSet;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.PageManager;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.fileUpload.service.FileUploadService;
import com.ccthanking.framework.fileUpload.vo.FileUploadVO;
import com.ccthanking.framework.log.LogManager;
import com.ccthanking.framework.model.requestJson;
import com.ccthanking.framework.service.UserService;
import com.ccthanking.framework.util.Encipher;
import com.ccthanking.framework.util.Pub;
import com.ccthanking.framework.util.QueryConditionList;
import com.ccthanking.framework.util.RandomGUID;
import com.ccthanking.framework.util.RequestUtil;


@Service
public class TiBaoJieSuanServiceImpl implements TiBaoJieSuanService {

	private String ywlx=YwlxManager.GC_XMGLGS_XXJD_TBZJ;
	@Override
	public String queryTiBaoJieSuan(String json, User user) {
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		try {
      
			PageManager page = RequestUtil.getPageManager(json);
			String orderFilter = RequestUtil.getOrderFilter(json);
			String condition = RequestUtil.getConditionList(json).getConditionWhere();
		    condition += BusinessUtil.getSJYXCondition("JHSJ") + BusinessUtil.getCommonCondition(user,null);
			if(!Pub.empty(condition)){
				condition +=" AND JHSJ.GC_JH_SJ_ID = TBZJ.JHSJID(+) and jhsj.wgsj_sj is not null ";
				
			}else{
				condition="   JHSJ.GC_JH_SJ_ID = TBZJ.JHSJID(+)  and jhsj.wgsj_sj is not null ";
			}
			condition += orderFilter;
		    page.setFilter(condition);
			conn.setAutoCommit(false);
			String sql = " SELECT JHSJ.XMBH," +
						" JHSJ.GC_JH_SJ_ID JHSJID, " +
						" JHSJ.XMMC, " +
						" JHSJ.BDMC, " +
						" JHSJ.BDBH, " +
						" (CASE JHSJ.XMBS " +
						" WHEN '0' THEN " +
						" (SELECT XMDZ " +
						" FROM GC_TCJH_XMXDK " +
						" WHERE ND = JHSJ.ND " +
						" AND GC_TCJH_XMXDK_ID = JHSJ.XMID " +
						" AND ROWNUM = 1) " +
						" WHEN '1' THEN " +
						" (SELECT BDDD " +
						" FROM GC_XMBD " +
						" WHERE GC_XMBD_ID = JHSJ.BDID " + 
						" AND ROWNUM = 1) " +
						" END) AS XMBDDZ, " +
						" JHSJ.KGSJ, " +
						" JHSJ.KGSJ_SJ KGSJSJ, " +
						" JHSJ.WGSJ, " +
						" JHSJ.WGSJ_SJ WGSJSJ, " +
						" TBZJ.JHSJID tbjszt " +
						" FROM GC_JH_SJ JHSJ, GC_XMGLGS_XXJD_TBZJ TBZJ " ;
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			domresult = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace(System.out);
		
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	@Override
	public String insertTBJSZT(String json, User user, String ywid) {
		Connection conn = DBUtil.getConnection();
		String resultVO = null;
		GcXmglgsXxjdTbzjVO xmvo = new GcXmglgsXxjdTbzjVO();
		try {
			conn.setAutoCommit(false);
			JSONArray list = xmvo.doInitJson(json);
			xmvo.setValueFromJson((JSONObject)list.get(0));
			BusinessUtil.setInsertCommonFields(xmvo, user);
			xmvo.setYwlx(ywlx);//业务类型
			EventVO eventVO = EventManager.createEvent(conn, xmvo.getYwlx(), user);//生成事件
			xmvo.setSjbh(eventVO.getSjbh());
			//设置主键
			xmvo.setGc_xmglgs_xxjd_tbzj_id(new RandomGUID().toString()); // 主键
			//插入
			BaseDAO.insert(conn, xmvo);
			resultVO = xmvo.getRowJson();
			conn.commit();
			LogManager.writeUserLog(xmvo.getSjbh(), xmvo.getYwlx(),
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "提报结算状态添加成功", user,"","");
			} catch (Exception e) {
				DBUtil.rollbackConnetion(conn);
				e.printStackTrace(System.out);
			} finally {
				DBUtil.closeConnetion(conn);
			}
			String jsona="{querycondition: {conditions: [{\"value\":\""+xmvo.getGc_xmglgs_xxjd_tbzj_id()+"\",\"fieldname\":\"gc_xmglgs_xxjd_tbzj_id\",\"operation\":\"=\",\"logic\":\"and\"} ]}}";
			String resultXinXi=this.queryTiBaoJieSuan(jsona,user);
			return resultXinXi;
	}

	}


