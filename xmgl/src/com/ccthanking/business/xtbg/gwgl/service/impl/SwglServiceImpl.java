package com.ccthanking.business.xtbg.gwgl.service.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import com.ccthanking.business.xtbg.gwgl.service.SwglService;
import com.ccthanking.business.xtbg.gwgl.vo.SwglVo;
import com.ccthanking.common.BusinessUtil;
import com.ccthanking.common.EventManager;
import com.ccthanking.common.YwlxManager;
import com.ccthanking.common.vo.EventVO;
import com.ccthanking.framework.Globals;
import com.ccthanking.framework.base.BaseDAO;
import com.ccthanking.framework.common.BaseResultSet;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.PageManager;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.fileUpload.service.FileUploadService;
import com.ccthanking.framework.fileUpload.vo.FileUploadVO;
import com.ccthanking.framework.log.LogManager;
import com.ccthanking.framework.util.Pub;
import com.ccthanking.framework.util.QueryConditionList;
import com.ccthanking.framework.util.RandomGUID;
import com.ccthanking.framework.util.RequestUtil;

/**
 * @auther xhb 
 */
@Service
public class SwglServiceImpl implements SwglService {
	
	/* (non-Javadoc)
	 * @see com.ccthanking.business.xtbg.gwgl.service.impl.SwglService#querySw(java.lang.String, com.ccthanking.framework.common.User)
	 */
	@Override
	public String querySw(String json, User user) throws SQLException {
		Connection conn = DBUtil.getConnection();
		String domResult = "";
		try {
			QueryConditionList list = RequestUtil.getConditionList(json);
			String orderFilter = RequestUtil.getOrderFilter(json);
			String condition = list == null ? "" : list.getConditionWhere();
			condition += BusinessUtil.getSJYXCondition(null) + BusinessUtil.getCommonCondition(user, null);
			condition += " AND S.SJBH=T.SJBH ";
			condition += orderFilter;
			
			PageManager page = RequestUtil.getPageManager(json);
			page.setFilter(condition);
			
			String querySql = "SELECT SWID, SWBH, T.SJZT, WJBT, LWDW, DQZT, WZ, HJ, WH,SERIAL_NUM, " 
					+ "SWRQ, SWLB, SSXM, CBKS, FWRQ, MJ,S.SJBH,S.YWLX FROM XTBG_GWGL_SWGL S,FS_EVENT T";
			BaseResultSet bs = DBUtil.query(conn, querySql, page);
			bs.setFieldTranslater("LWDW", "FS_COMMON_DICT", "DICT_ID", "DICT_NAME");
			bs.setFieldTranslater("WZ", "FS_COMMON_DICT", "DICT_ID", "DICT_NAME");
			
			bs.setFieldDic("SWLB", "SWLB");
			bs.setFieldDic("HJ", "HJ1000000000302");
			bs.setFieldDic("DQZT", "SFWZT");
			bs.setFieldSjbh("SJBH");
			domResult = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domResult;
	}
	
	/* (non-Javadoc)
	 * @see com.ccthanking.business.xtbg.gwgl.service.impl.SwglService#executeSw(java.lang.String, com.ccthanking.framework.common.User, java.lang.String, java.lang.String)
	 */
	@Override
	public String executeSw(String json, User user, String operatorSign, String ywid)
			throws Exception {
		Connection conn = DBUtil.getConnection();
		String msg = null;
		String resultVo = null;
		int sign = Integer.parseInt(operatorSign);
		String sjbh = "";
		try {
			SwglVo swglVo = new SwglVo();
			conn.setAutoCommit(false);
			JSONArray list = swglVo.doInitJson(json);
			JSONObject jsonObj = (JSONObject)list.get(0);
			
			switch (sign) {
			case 1:
				msg = "登记";
				if (!"".equals(ywid)) {
					FileUploadService.updateFjztByYwid(conn, ywid);
					jsonObj.put("SWID", ywid);
				} else {
					jsonObj.put("SWID", new RandomGUID().toString());
				}
				jsonObj.put("YWLX", YwlxManager.OA_GWGL_SWGL);
				jsonObj.put("DQZT", Globals.OA_GWGL_DJ);

				swglVo.setValueFromJson(jsonObj);
				//add by cbl
				EventVO eventVO = EventManager.createEvent(conn, swglVo.getYwlx(), user);//生成事件
				swglVo.setSjbh(eventVO.getSjbh());
				sjbh = swglVo.getSjbh();
				//add by cbl end
				BusinessUtil.setInsertCommonFields(swglVo, user);
				BaseDAO.insert(conn, swglVo);

				//add by liujs at 2013年11月13日 18:15:47 start	附件信息
				FileUploadVO inVo=new FileUploadVO();
				inVo.setFjzt("1");
				inVo.setYwlx(YwlxManager.OA_GWGL_FWGL);
				inVo.setSjbh(swglVo.getSjbh());
				FileUploadService.updateVOByYwid(conn,inVo,swglVo.getSwid(),user);
				//add end
				
				String sql = "update fs_fileupload set sjbh='" + eventVO.getSjbh() + "'," 
						+ "ywlx='" + YwlxManager.OA_GWGL_SWGL + "' where ywid='"+ywid+"'";
				DBUtil.execUpdateSql(conn, sql);
				//插入ap_processconfig表
				String insert_sql = "insert into AP_PROCESSCONFIG (AP_PROCESS_ID,WS_TEMPLATEID,YWLX,SJBH,LRR,LRSJ) values('" 
						+ swglVo.getSwid() + "','381','" + swglVo.getYwlx() + "','"+swglVo.getSjbh()+"','"+user.getAccount()+"',SYSDATE)";
				DBUtil.execSql(conn, insert_sql);
				break;

			case 2:

				msg = "修改";
				swglVo.setValueFromJson(jsonObj);
				BusinessUtil.setUpdateCommonFields(swglVo, user);
				
				//add by liujs at 2013年11月13日 18:16:28 附件问题
				FileUploadVO upVo=new FileUploadVO();
				upVo.setFjzt("1");
				FileUploadService.updateVOByYwid(conn,upVo,swglVo.getSwid(),user);
				
				String updateSwBh = "update AP_PROCESS_WS T set t.domain_value='"+swglVo.getSwbh()+"' where t.sjbh='"+swglVo.getSjbh()+"' and t.fieldname='SWH'";

				DBUtil.execSql(conn, updateSwBh);
				//add end
				
				BaseDAO.update(conn, swglVo);
				break;
			
			default:
				break;
			}
			
			resultVo = swglVo.getRowJson();
			conn.commit();
			LogManager.writeUserLog(sjbh, YwlxManager.OA_GWGL_SWGL,
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ msg + "收文信息成功", user, "", "");
		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
			LogManager.writeUserLog(sjbh, YwlxManager.OA_GWGL_SWGL,
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_FAILURE,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ msg + "收文信息失败", user, "", "");
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return resultVo;
	}
	
	
	@Override
	public SwglVo findById(int id) {
		return null;
	}

	@Override
	public List<SwglVo> find() {
		return null;
	}

	@Override
	public List<SwglVo> find(List<Integer> ids) {
		return null;
	}

	@Override
	public int remove(int id) {
		return 0;
	}

	@Override
	public int remove(List<Integer> ids) {
		return 0;
	}

	@Override
	public int update(SwglVo bean) {
		return 0;
	}

	@Override
	public int insert(SwglVo bean) {
		return 0;
	}
	
	
	@Override
	public String queryLs(String json, User user) throws SQLException {
		Connection conn = DBUtil.getConnection();
		String domResult = "";
		try {
			QueryConditionList list = RequestUtil.getConditionList(json);
			String orderFilter = RequestUtil.getOrderFilter(json);
			String condition = list == null ? "" : list.getConditionWhere();
			condition += orderFilter;
			
			PageManager page = RequestUtil.getPageManager(json);
			page.setFilter(condition);
			
			String querySql = "SELECT t.*,(select flaflwid from TAC_FLOWAPPLY where flaid = t.finflaid) as flowid FROM TOA_FILEIN t";
			BaseResultSet bs = DBUtil.query(conn, querySql, page);
			bs.setFieldDateFormat("FINRECDATE", "yyyy-MM-dd");
			domResult = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domResult;
	}

	@Override
	public String queryUnique(HttpServletRequest request, User user) {
		String serial_num = request.getParameter("serial_num");
		String swlb = request.getParameter("swlb");
		String swid = request.getParameter("swid");
		String sql = "SELECT COUNT(1) CNT FROM XTBG_GWGL_SWGL WHERE SERIAL_NUM = '" + serial_num + "' AND SWLB='"+swlb+"' and swid!='"+swid+"' and sfyx='1'";
		String[][] cnt = DBUtil.query(sql);
		JSONObject jsonObj = new JSONObject();
		if ("0".equals(cnt[0][0])) {
			jsonObj.put("isUnique", "可以使用");
			jsonObj.put("sign", "0");
		} else {
			String sw = "1".equals(swlb) ? "收文" : "临时";
			jsonObj.put("isUnique", sw+"号重复，请重新填写");
			jsonObj.put("sign", "1");
		}
		return jsonObj.toString();
	}

	@Override
	public String querySwbh(String swlb, User user) {
		Connection conn = DBUtil.getConnection();
		JSONObject jsonObj = new JSONObject();
		try {
			conn.setAutoCommit(false);
			String nd = Pub.getDate("yyyy");
			String querySql = "SELECT MAX(SERIAL_NUM) MAXNUM FROM XTBG_GWGL_SWGL T WHERE TO_CHAR(SWRQ,'YYYY')='"+nd+"' AND T.SWLB='"+swlb+"' and sfyx='1'";
			String[][] rs = DBUtil.query(conn, querySql);
			
			String maxId = (rs != null && rs[0][0].length() >= 1) ? rs[0][0] : "0";
			int maxIdInt = Integer.parseInt(maxId) + 1;
			maxId = Pub.formatSerialNumber(maxIdInt, 3);
			
			jsonObj.put("swbh", maxId);
			
		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace();
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return jsonObj.toString();
	}

	@Override
	public String deleteSw(String id) {
		Connection conn = null;
		try {
			conn = DBUtil.getConnection();
			conn.setAutoCommit(false);

			String sql = "UPDATE XTBG_GWGL_SWGL SET SFYX='0' WHERE SWID='"+id+"'";
			DBUtil.execUpdateSql(conn, sql);
			conn.commit();
			
		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace();
			return "0";
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return "1";
	}
}
