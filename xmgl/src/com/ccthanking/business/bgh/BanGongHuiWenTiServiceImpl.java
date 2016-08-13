package com.ccthanking.business.bgh;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ccthanking.business.htgl.service.impl.GcHtglHtServiceImpl;
import com.ccthanking.business.htgl.vo.GcHtglHtsjVO;
import com.ccthanking.business.tcjh.jhgl.vo.BgbbVO;
import com.ccthanking.business.tcjh.jhgl.vo.BgxmVO;
import com.ccthanking.business.tcjh.jhgl.vo.FkqkVO;
import com.ccthanking.business.tcjh.jhgl.vo.TcjhVO;
import com.ccthanking.business.wttb.service.impl.WttbServiceImpl;
import com.ccthanking.business.wttb.vo.WttbHflsVO;
import com.ccthanking.business.wttb.vo.WttbInfoVO;
import com.ccthanking.business.wttb.vo.WttbLzlsVO;
import com.ccthanking.business.wttb.vo.WttbPfqkVO;
import com.ccthanking.business.xdxmk.vo.ZtVO;
import com.ccthanking.business.zjb.jsgl.GcZjbJsbVO;
import com.ccthanking.business.zjb.lbj.dyqk.GcZjbDyqkVO;
import com.ccthanking.common.BusinessUtil;
import com.ccthanking.common.EventManager;
import com.ccthanking.common.YwlxManager;
import com.ccthanking.common.vo.EventVO;
import com.ccthanking.common.vo.XmxxVO;
import com.ccthanking.framework.Globals;
import com.ccthanking.framework.base.BaseDAO;
import com.ccthanking.framework.base.BaseVO;
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
public class BanGongHuiWenTiServiceImpl implements BanGongHuiWenTiService {

	private String ywlx=YwlxManager.GC_BGH_WT;
	@Override
	public String querybanGongHuiWen(String json,User user) throws Exception {
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		try {
      
			PageManager page = RequestUtil.getPageManager(json);
			/*String orderFilter = " order by a.xuhao , a.lrsj desc ";*/
			String orderFilter = RequestUtil.getOrderFilter(json);
			String condition = RequestUtil.getConditionList(json).getConditionWhere();
		    condition += BusinessUtil.getSJYXCondition("A") + BusinessUtil.getCommonCondition(user,null);
		    condition +=" and a.jhsjid=b.gc_jh_sj_id(+)  and a.bghid=c.gc_bgh_id(+)  ";
			condition += orderFilter;
		    page.setFilter(condition);
			conn.setAutoCommit(false);
			String sql = " select  a.gc_bgh_wt_id, a.xuhao,a.gc_bgh_wt_id fj, a.bghid, a.wtbt, a.wtms, a.fqr, a.xwjjsj," +
					" a.zt, a.fqsj, a.shr, a.shsj, a.shyj, a.bghjl, a.bghhf, a.zzbm, a.zzr, a.zzzxld, a.yqjjsj," +
					" a.dbcs, a.isjj, a.jjsj, a.ywlx, a.sjbh, a.bz, a.lrr, a.lrsj, a.lrbm, a.lrbmmc,a.sfgk, " +
					" a.gxr, a.gxsj, a.gxbm, a.gxbmmc, a.sjmj, a.sfyx,a.FQBM, a.wtlx,b.xmmc,b.bdmc,b.xmbh,B.BDBH,b.gc_jh_sj_id jhsjid," +
					" c.gc_bgh_id, to_char(c.HYSJ, 'yyyy-mm-dd HH24:MI:SS') HYSJ , c.hc, c.hydd, c.hyzc, c.chbm, c.hyzt, c.chry   " +
						"  from GC_BGH_WT a,(select * from GC_BGH where sfyx='1') C,( select * from gc_jh_sj where sfyx='1') b  ";
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldOrgDept("LRBM");
			bs.setFieldDic("ISJJ", "SF");
			bs.setFieldDic("SFGK", "SF");
			bs.setFieldDic("ZT", "BGHZT");
			bs.setFieldDic("WTLX", "LX");
			bs.setFieldFileUpload("FJ", "0071");
			bs.setFieldTranslater("FQR", "FS_ORG_PERSON", "ACCOUNT", "NAME");
			bs.setFieldTranslater("SHR", "FS_ORG_PERSON", "ACCOUNT", "NAME");
			bs.setFieldTranslater("FQBM", "FS_ORG_DEPT", "ROW_ID", "BMJC");
			bs.setFieldTranslater("LRBM", "FS_ORG_DEPT", "ROW_ID", "BMJC");
			bs.setFieldTranslater("ZZBM", "FS_ORG_DEPT", "ROW_ID", "BMJC");
			bs.setFieldTranslater("ZZR", "FS_ORG_PERSON", "ACCOUNT", "NAME");
			bs.setFieldTranslater("ZZZXLD", "FS_ORG_PERSON", "ACCOUNT", "NAME");
			domresult = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace(System.out);
		
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}

	@Override
	public String insertBanGongHuiWenTi(String json, HttpServletRequest request) throws Exception {
		Connection conn = DBUtil.getConnection();
		String resultVO = null;
		GcBghWtVO xmvo = new GcBghWtVO();
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		String ywid =request.getParameter("ywid");
		try {
			conn.setAutoCommit(false);
			JSONArray list = xmvo.doInitJson(json);
			xmvo.setValueFromJson((JSONObject)list.get(0));
			BusinessUtil.setInsertCommonFields(xmvo, user);
			xmvo.setYwlx(ywlx);//业务类型
			EventVO eventVO = EventManager.createEvent(conn, xmvo.getYwlx(), user);//生成事件
			xmvo.setSjbh(eventVO.getSjbh());
			//设置主键
			if(!Pub.empty(ywid)){
				xmvo.setGc_bgh_wt_id(ywid);
			    FileUploadVO fvo = new FileUploadVO();
		        fvo.setFjzt("1");//更新附件状态
		        fvo.setGlid2(xmvo.getJhsjid());//存入计划数据ID	 
		        fvo.setGlid3(xmvo.getXmid()); //存入项目ID
		        fvo.setGlid4(xmvo.getBdid()); //存入标段ID
		        fvo.setYwlx(ywlx);
		        fvo.setSjbh(eventVO.getSjbh());
		        FileUploadService.updateVOByYwid(conn, fvo, xmvo.getGc_bgh_wt_id(),user);
			}else{
				xmvo.setGc_bgh_wt_id(new RandomGUID().toString()); // 主键
			}
			//插入
			BaseDAO.insert(conn, xmvo);
			resultVO = xmvo.getRowJson();
			conn.commit();
			LogManager.writeUserLog(xmvo.getSjbh(), xmvo.getYwlx(),
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "办公会问题添加成功", user,"","");
			} catch (Exception e) {
				DBUtil.rollbackConnetion(conn);
				e.printStackTrace(System.out);
			} finally {
				DBUtil.closeConnetion(conn);
			}
			String jsona="{querycondition: {conditions: [{\"value\":\""+xmvo.getGc_bgh_wt_id()+"\",\"fieldname\":\"GC_BGH_WT_ID\",\"operation\":\"=\",\"logic\":\"and\"} ]}}";
			String resultXinXi=this.querybanGongHuiWen(jsona,user);
			return resultXinXi;
	}

	@Override
	public String updateBanGongHuiWenTi(String json, HttpServletRequest request) throws Exception {
		Connection conn = DBUtil.getConnection();
		String resultVO = null;
		GcBghWtVO xmvo = new GcBghWtVO();
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		try {
			conn.setAutoCommit(false);
		    JSONArray list = xmvo.doInitJson(json);
		    xmvo.setValueFromJson((JSONObject)list.get(0));
			//设置公共字段
			BusinessUtil.setUpdateCommonFields(xmvo, user);
	     	xmvo.setYwlx(ywlx);//业务类型
	     	//根据ID查YWID和SJBH
	     	GcBghWtVO vo1=(GcBghWtVO) BaseDAO.getVOByPrimaryKey(conn, xmvo);
			
	     	FileUploadVO fvo = new FileUploadVO();
	     	fvo.setFjzt("1");
	     	fvo.setGlid2(xmvo.getJhsjid());//存入计划数据ID
	     	fvo.setGlid3(xmvo.getXmid()); //存入项目ID
	     	fvo.setGlid4(xmvo.getBdid()); //存入标段ID
	     	fvo.setYwlx(ywlx);
	     	fvo.setSjbh(vo1.getSjbh());
	     	FileUploadService.updateVOByYwid(conn, fvo, xmvo.getGc_bgh_wt_id(),user);
	     	//回写问题中心
	     	//如果没处理过，并且会议类型是“问题中心”，并且是否解决值为“是”，那么回写问题中心
			if("0".equals(vo1.getWthxcs()) && "2".equals(vo1.getWtlx()) && ("1".equals(xmvo.getIsjj())||"3".equals(xmvo.getZt()))){
				if(!Pub.empty(vo1.getWtid())){
					String wtid = vo1.getWtid();
					WttbInfoVO wtvo = new WttbInfoVO();
					wtvo.setWttb_info_id(vo1.getWtid());
					wtvo = (WttbInfoVO) BaseDAO.getVOByPrimaryKey(conn, wtvo);

					String dqblrSql = "select WTTB_LZLS_ID,JSR from WTTB_LZLS where WTID='"+wtid+"' and SFDQBLR='1'";
					//如果主办人正好是当前办理人，那么回复并且将当前办理人传给上一个人，并记录回传历史
					//如果主办人不是当前办理人，那么只回复
					String[][] dqblrArr = DBUtil.query(conn, dqblrSql);
					String updateDqblrSql = "update WTTB_LZLS set SFDQBLR='0' where WTID='"+wtid+"'";
					DBUtil.execSql(conn, updateDqblrSql);
					//将上一个转发人的记录设置为当前办理人
					WttbServiceImpl wtser = new WttbServiceImpl();
					String currentLzid = wtser.getNextZfid(dqblrArr[0][0], wtid, conn);
//					String queryFqrLzidSql = "select WTTB_LZLS_ID from WTTB_LZLS where BLRJS='0' and WTID='"+wtid+"' and SFYX='1'";
//					String fqrLzid = DBUtil.query(conn, queryFqrLzidSql)[0][0];
					if("0".equals(currentLzid)){//如果到达了发起人，那么不用记录了，发起人选择回复和解决就可以了
						if("7".equals(wtvo.getSjzt())){
							//如果问题状态是“已上会”，那么变更为“已处理”
							wtvo.setSjzt("4");
							BusinessUtil.setUpdateCommonFields(wtvo,user);
							BaseDAO.update(conn, wtvo);
						}
					}else{//如果还在回复过程中，那么逐级向前找节点
						if("7".equals(wtvo.getSjzt())){
							//如果问题状态是“已上会”，那么变更为“处理中”
							wtvo.setSjzt("2");
							BusinessUtil.setUpdateCommonFields(wtvo,user);
							BaseDAO.update(conn, wtvo);
						}
						updateDqblrSql = "update WTTB_LZLS set SFDQBLR='1' where WTID='"+wtid+"' and WTTB_LZLS_ID='"+currentLzid+"'";
						DBUtil.execSql(conn, updateDqblrSql);
						String getDqblrSql = "select JSR from WTTB_LZLS where WTTB_LZLS_ID='"+currentLzid+"'";
						String nextJsrAccount = DBUtil.query(conn, getDqblrSql)[0][0];
						//将转发记录保存到回复历史表
						WttbHflsVO hfvo = new WttbHflsVO();
						hfvo.setWttb_hfls_id(new RandomGUID().toString());
						hfvo.setWtid(wtid);
						hfvo.setJsr(nextJsrAccount);
						hfvo.setJsid(currentLzid);
						hfvo.setHfid(dqblrArr[0][0]);
						BusinessUtil.setInsertCommonFields(hfvo,user);
						BaseDAO.insert(conn, hfvo);
					}
					String wtlzSql = "select WTTB_LZLS_ID,JSR,JSBM from WTTB_lZLS where WTID='"+wtvo.getWttb_info_id()+"' and BLRJS='1' and SFYX='1'";
					String wtlzArr[][] = DBUtil.query(conn, wtlzSql);
					//如果查询到了主办人，那么以主办人的名义进行回复
					if(wtlzArr!=null && wtlzArr.length>0){
						WttbPfqkVO pfvo = new WttbPfqkVO();
						pfvo.setWttb_pfqk_id(new RandomGUID().toString());//批复情况直接使用“会议中心”的问题ID，这样附件就可以通用了
						pfvo.setWtid(wtvo.getWttb_info_id());
						pfvo.setPfr(wtlzArr[0][1]);
						pfvo.setPfsj(wtvo.getGxsj());
						pfvo.setPfdw(wtlzArr[0][2]);
						pfvo.setPfnr(xmvo.getBghhf());
						BusinessUtil.setInsertCommonFields(pfvo, user);
						pfvo.setYwlx(YwlxManager.GC_WTTB_GT);
						EventVO event = EventManager.createEvent(conn, pfvo.getYwlx(), user);//生成事件
						pfvo.setSjbh(event.getSjbh());
						pfvo.setNrlx("1");
						pfvo.setLzid(wtlzArr[0][0]);
						pfvo.setXxly("1");
						BaseDAO.insert(conn, pfvo);
						//处理流转表回复情况
						WttbLzlsVO lzvo = new WttbLzlsVO();
						lzvo.setWttb_lzls_id(wtlzArr[0][0]);
						lzvo.setHfqk("1");
						BusinessUtil.setUpdateCommonFields(lzvo, user);
						BaseDAO.update(conn, lzvo);
					}
					
				}
				xmvo.setWthxcs("1");//如果回写问题表了，那么问题回写次数+1
			}
			//修改 
			BaseDAO.update(conn, xmvo);
			resultVO = xmvo.getRowJson();
			conn.commit();
			LogManager.writeUserLog(vo1.getSjbh(), vo1.getYwlx(),
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "办公会问题修改成功", user,"","");
	
			} catch (Exception e) {
				DBUtil.rollbackConnetion(conn);
				e.printStackTrace(System.out);
			} finally {
				DBUtil.closeConnetion(conn);
			}
			String jsona="{querycondition: {conditions: [{\"value\":\""+xmvo.getGc_bgh_wt_id()+"\",\"fieldname\":\"gc_bgh_wt_id\",\"operation\":\"=\",\"logic\":\"and\"} ]}}";
			String resultXinXi=this.querybanGongHuiWen(jsona,user);
			return resultXinXi;
	}

	@Override
	public String deleteGongHuiWenTi(String json, User user) {
		Connection conn = DBUtil.getConnection();
		String resultVO = null;
		GcBghWtVO vo = new GcBghWtVO();

		try {
			conn.setAutoCommit(false);
			JSONArray list = vo.doInitJson(json);
			vo.setValueFromJson((JSONObject)list.get(0));
			BusinessUtil.setUpdateCommonFields(vo, user);
			vo.setYwlx(ywlx);
			//置成失效
	        vo.setSfyx("0");
			//插入
			BaseDAO.update(conn, vo);
			//附件删除
			FileUploadVO fvo = new FileUploadVO();
			fvo.setYwid(vo.getGc_bgh_wt_id());
			FileUploadService.deleteByConditionVO(conn, fvo);
			
			resultVO = vo.getRowJson();
			conn.commit();
			LogManager.writeUserLog(vo.getSjbh(), vo.getYwlx(),
					Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "删除办公会问题成功", user,"","");
	
			} catch (Exception e) {
				DBUtil.rollbackConnetion(conn);
			} finally {
				DBUtil.closeConnetion(conn);
			}
			return resultVO;
	}

	@Override
	public String updatebatchdata(String json, User user,HttpServletRequest request) {
		Connection conn = DBUtil.getConnection();

		String resultVO = "";
		try {
			conn.setAutoCommit(false);
			GcBghWtVO bghwt = new GcBghWtVO();
			JSONArray list = bghwt.doInitJson(json);
			List<String> listrow = new ArrayList<String>();
			GcBghWtVO bghwtVO = null;
			for (int i = 0; i < list.size(); i++) {
				bghwtVO = new GcBghWtVO();
				bghwtVO.setValueFromJson((JSONObject) list.get(i));
				BusinessUtil.setUpdateCommonFields(bghwtVO,user);
				BaseDAO.update(conn, bghwtVO);
				listrow.add(bghwtVO.getRowJsonSingle());
			}
			conn.commit();
			resultVO = BaseDAO.comprisesResponseData(conn, listrow);
			LogManager.writeUserLog(bghwtVO.getSjbh(), bghwtVO.getYwlx(),
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "修改办公会议题成功", user, "", "");
		}catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return resultVO;
	}
	
}


