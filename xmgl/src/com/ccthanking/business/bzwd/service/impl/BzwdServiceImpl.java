package com.ccthanking.business.bzwd.service.impl;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import com.ccthanking.business.bzwd.service.BzwdService;
import com.ccthanking.business.bzwd.vo.BzwdVO;
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
import com.ccthanking.framework.log.LogManager;
import com.ccthanking.framework.util.Pub;
import com.ccthanking.framework.util.RandomGUID;

@Service
public class BzwdServiceImpl implements BzwdService{
	private String ywlx=YwlxManager.GC_WD_BZWD;
	@Override
	public String queryWdTree(HttpServletRequest request,
			String jsonString) throws Exception {
		Connection conn = null;
		String s = request.getParameter("str");
		String parentid = request.getParameter("parentid");
		JSONArray jsonArr = new JSONArray();
		try {
			conn = DBUtil.getConnection();
			String queryDictSql = "select t.wdid WDID,WDMC WDMC,parent_id parent_id,'0' TREENODE," +
					" sort,WDDJ,GC_WD_BZWD_ID ,sjbh,ywlx from GC_WD_BZWD t  " +
					" order by SORT";
			List list = new ArrayList();
			List parentList = new ArrayList();
			if(!Pub.empty(s)){
				String arrs[] = s.split(",");
				for(String arr:arrs){
					String checkedParentSql = "select department from FS_ORG_PERSON where account='"+arr+"'";
					String parentArr[][] = DBUtil.query(conn, checkedParentSql);
					if(parentArr!=null){
						list.add(parentArr[0][0]);
					}
					list.add(arr);
				}
			}
			List<Map<String, String>> rsList = DBUtil.queryReturnList(conn, queryDictSql);
			for (int i = 0; i < rsList.size(); i++) {
				Map<String, String> rsMap = rsList.get(i);
				JSONObject json = new JSONObject();
				json.put("id",rsMap.get("WDID"));
				json.put("parentId", rsMap.get("PARENT_ID"));
				json.put("name", rsMap.get("WDMC"));
				json.put("treenode", rsMap.get("TREENODE"));
				json.put("wddj", rsMap.get("WDDJ"));
				json.put("sort", rsMap.get("SORT"));
				json.put("sjbh", rsMap.get("SJBH"));
				json.put("ywlx", rsMap.get("YWLX"));
				json.put("bzwdid",rsMap.get("GC_WD_BZWD_ID"));
				//默认展开父节点 
				if(rsMap.get("WDID").equals(parentid)){
					json.put("selectNode", "true");
					json.put("open", "true");
					
				}
				jsonArr.add(json);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return jsonArr.toString();
	}

	@Override
	public String insertBzwd(String json, HttpServletRequest request) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		Connection conn = DBUtil.getConnection();
		String resultVO = null;
		BzwdVO xmvo = new BzwdVO();
		try {
			conn.setAutoCommit(false);
			JSONArray list = xmvo.doInitJson(json);
			
			xmvo.setValueFromJson((JSONObject)list.get(0));
			BusinessUtil.setInsertCommonFields(xmvo, user);
			xmvo.setYwlx(ywlx);//业务类型
			EventVO eventVO = EventManager.createEvent(conn, xmvo.getYwlx(), user);//生成事件
			xmvo.setSjbh(eventVO.getSjbh());
			String sql=" ";
			//设置主键
			xmvo.setGc_wd_bzwd_id(new RandomGUID().toString()); // 主键
			if(xmvo.getParent_id().equals("0"))
			{
				 sql="select max(substr(wdid,0,3))+1||'0000' zdwdid from GC_WD_BZWD t where t.parent_id='"+xmvo.getParent_id()+"'";
			}
			else{
				 sql="select max(t.wdid)+1 zdwdid from GC_WD_BZWD t where t.parent_id='"+xmvo.getParent_id()+"'";
			}	
			
			String[][] aa=DBUtil.query(sql);
			
			if(!Pub.empty(aa[0][0]))
			{
				if(aa[0][0].equals("0000"))
				{
					xmvo.setWdid("1010000");
				}else{
					xmvo.setWdid(aa[0][0]);
				}
			}
			else{
					String wdid=Integer.parseInt(xmvo.getParent_id())+1+"";
					xmvo.setWdid(wdid);
			}
			//插入
			BaseDAO.insert(conn, xmvo);
			resultVO = xmvo.getRowJson();
		
			conn.commit();
			LogManager.writeUserLog(xmvo.getSjbh(), xmvo.getYwlx(),
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "标准文档添加成功", user,"","");
	
			} catch (Exception e) {
				DBUtil.rollbackConnetion(conn);
			} finally {
				DBUtil.closeConnetion(conn);
			}
			return resultVO;
	}

	@Override
	public String updateBzwd(String json, HttpServletRequest request) {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		Connection conn = DBUtil.getConnection();
		String resultVO = null;
		BzwdVO xmvo = new BzwdVO();

		try {
			conn.setAutoCommit(false);
		    JSONArray list = xmvo.doInitJson(json);
		    xmvo.setValueFromJson((JSONObject)list.get(0));
			//设置公共字段
			BusinessUtil.setUpdateCommonFields(xmvo, user);
	     	xmvo.setYwlx(ywlx);//业务类型
	     	
			//修改 
			BaseDAO.update(conn, xmvo);
			//根据ID查YWID和SJBH
	     	BzwdVO vo1=(BzwdVO) BaseDAO.getVOByPrimaryKey(conn, xmvo);
			resultVO = vo1.getRowJson();
			conn.commit();
			LogManager.writeUserLog(vo1.getSjbh(), vo1.getYwlx(),
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "标准文档修改成功", user,"","");
	
			} catch (Exception e) {
				DBUtil.rollbackConnetion(conn);
				e.printStackTrace(System.out);
			} finally {
				DBUtil.closeConnetion(conn);
			}
			return resultVO;
	}

	@Override
	public String queryOneById(HttpServletRequest request) {
		Connection conn = DBUtil.getConnection();
		String domresult ="";
		//计划筛选后的个数
		String wdid= request.getParameter("wdid");
		try {
			PageManager page=new PageManager();
			conn.setAutoCommit(false);
			String sql="select gc_wd_bzwd_id, wdid, wdmc, ywlx, sjbh, sjmj, sfyx, bz, lrr, lrsj," +
					" lrbm, lrbmmc, gxr, gxsj, gxbm, gxbmmc, sort, wddj, parent_id from gc_wd_bzwd where wdid="+wdid+" ";
			BaseResultSet bs = DBUtil.query(conn, sql,page);
			domresult = bs.getJson();
			
		} catch (Exception e) {
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
}
