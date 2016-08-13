package com.ccthanking.business.jhfk.service.impl;

import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import com.ccthanking.business.clzx.service.ClzxManagerService;
import com.ccthanking.business.clzx.service.impl.ClzxManagerServiceImpl;
import com.ccthanking.business.jhfk.service.JhfkService;
import com.ccthanking.business.jhfk.vo.FkgxVO;
import com.ccthanking.business.tcjh.jhgl.vo.FkqkVO;
import com.ccthanking.business.tcjh.jhgl.vo.TcjhVO;
import com.ccthanking.common.BusinessUtil;
import com.ccthanking.common.EventManager;
import com.ccthanking.common.vo.EventVO;
import com.ccthanking.framework.Globals;
import com.ccthanking.framework.base.BaseDAO;
import com.ccthanking.framework.base.BaseVO;
import com.ccthanking.framework.common.BaseResultSet;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.PageManager;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.util.Pub;
import com.ccthanking.framework.util.RandomGUID;
import com.ccthanking.framework.util.RequestUtil;
@Service
public class JhfkServiceImpl implements JhfkService{
	@Override
	public String doFkjhBatch(HttpServletRequest request,String param,Connection conn)throws Exception{
		//{"response":{"data":[{"JHSJID":"f0d37a90-f92f-4bbf-abfa-cd7aa088c7fb","FKRQ":"2012-11-17"}]}}
		String[] paramArray = param.split("\\|");
		String jhsjid = paramArray[0];
		String fkrq = paramArray[1];
		String fklx = paramArray[2];
		JSONObject obj = new JSONObject();
		obj.put("JHSJID", jhsjid);
		obj.put("FKRQ", fkrq);
		obj.put("FKLX", fklx);
		JSONArray array = new JSONArray();
		array.add(obj);
		JSONObject jsonDataObj = new JSONObject();
		jsonDataObj.put("data", array);
		JSONObject jsonResponseObj = new JSONObject();
		jsonResponseObj.put("response", jsonDataObj);
		this.doFkjh(request, jsonResponseObj.toString());
		return "";
	}
	@Override
	public String doFkjh(HttpServletRequest request, String json) throws Exception{
		FkqkVO resultVO = new FkqkVO();
		JhfkCommon common = new JhfkCommon();
		Connection conn = null;
		String fklx = request.getParameter("fklx");
		String domresult = "";
//		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		try {
			BaseVO[] bv = null;
			conn=DBUtil.getConnection();
			FkgxVO condVO = new FkgxVO();
			condVO.setFklx(fklx);
			if(condVO!=null&&!condVO.isEmpty()){
				bv = (BaseVO [])BaseDAO.getVOByCondition(conn, condVO);
			}
			JSONObject jsono = JSONObject.fromObject(json);
			JSONObject response = (JSONObject) jsono.get("response");
			JSONArray data = (JSONArray) response.get("data");
			JSONObject rowJson = JSONObject.fromObject(data.get(0));//取第一行的数据
			if(bv!=null){
				for(int i=0;i<bv.length;i++){
					FkgxVO newvo = (FkgxVO)bv[i];
					if("1".equals(newvo.getFklb())){
						resultVO = doFksj(conn,rowJson,newvo,request);
					}else if("2".equals(newvo.getFklb())){
						resultVO = doFkwt(conn,rowJson,newvo,request);
					}
					common.callbackMethod(conn,resultVO,newvo,request);
				}
			}
			//-------------加入处理中心任务生成代码-------------BEGIN---------
			ClzxManagerService cms = new ClzxManagerServiceImpl();
			cms.achieveTaskByJhfk(fklx, rowJson, request,conn);
			//-------------加入处理中心任务生成代码-------------END---------
			conn.commit();
			domresult = Pub.makeQueryConditionByID(resultVO.getGc_jh_fkqk_id(), "F.GC_JH_FKQK_ID");
			domresult = this.queryTabList(request, domresult);
		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace();
			throw e;
		}finally{
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	/**
	 * 反馈时间
	 * @param conn
	 * @param rowJson
	 * @param fkgxVO
	 * @param user
	 * @return
	 * @throws Exception
	 */
	private FkqkVO doFksj(Connection conn,JSONObject rowJson,FkgxVO fkgxVO,HttpServletRequest request) throws Exception{
		JhfkCommon common = new JhfkCommon();
		FkqkVO fkqkvo = common.doFksj(conn, rowJson, fkgxVO, request);
		return fkqkvo;
	}
	/**
	 * 
	 * @return
	 */
	@Override
	public String queryXMFkxx(HttpServletRequest request, String json) throws Exception{
		// TODO Auto-generated method stub
		Connection conn = null;
		String domresult = "";
		String fklx = request.getParameter("fklx");
		String jhsjid = request.getParameter("jhsjid");
		try {
			PageManager page = new PageManager();
			page.setPageRows(100);
			BaseVO[] bv = null;
			conn=DBUtil.getConnection();
			String queryXMIDSql = "select XMID,XMBS from GC_JH_SJ where GC_JH_SJ_ID='"+jhsjid+"'";
			String array[][] = DBUtil.query(conn, queryXMIDSql);
			String xmid = array[0][0];
			String xmbs = array[0][1];
			if("0".equals(xmbs)){
				//如果本次反馈的是项目的时间点，那么不需要自动反馈了 
				domresult = "0";
			}else{
				FkgxVO condVO = new FkgxVO();
				//如果本次反馈的时标段的，那么可能需要自动反馈项目的数据
				condVO.setFklx(fklx);
				if(condVO!=null&&!condVO.isEmpty()){
					bv = (BaseVO [])BaseDAO.getVOByCondition(conn, condVO);
				}
				if(bv!=null){
					for(int i=0;i<bv.length;i++){
						FkgxVO fkgxVO = (FkgxVO)bv[i];
						if("1".equals(fkgxVO.getFklb())){
							String sql = "select GC_JH_SJ_ID JHSJID,'"+fklx+"' FKLX, " +
									"(select max("+fkgxVO.getJhzdmc()+") from GC_JH_SJ where "+fkgxVO.getKzzdmc()+"='1' and XMBS='1' and XMID='"+xmid+"') as FKRQ, " +
									"(select count(GC_JH_SJ_ID) from GC_JH_SJ where "+fkgxVO.getKzzdmc()+"='1' " +
									"and "+fkgxVO.getJhzdmc()+" is null and XMBS='1' " +
									"and XMID='"+xmid+"') FKBZ " +
									"from GC_JH_SJ where XMBS='0' and XMID='"+xmid+"'" ;
							BaseResultSet bs = DBUtil.query(conn, sql, page);
							bs.setFieldDateFormat("FKRQ", "yyyy-MM-dd");
							domresult = bs.getJson();
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	private FkqkVO doFkwt(Connection conn,JSONObject rowJson,FkgxVO fkgxVO,HttpServletRequest request) throws Exception{
		FkqkVO fkqkvo = new FkqkVO();
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
//		String pk = getPrimaryKeys(conn,fkgxVO.getYwbmc());
//		String sqlCount = "select count(JHSJID),"+pk+",SJBH from "+fkgxVO.getYwbmc()+" where SFYX='1' and JHSJID='"+rowJson.getString("JHSJID")+"' group by "+pk+",SJBH";
//		String[][] arr = DBUtil.query(conn, sqlCount);
//		String count = arr[0][0];
//		String pkValue = arr[0][1];
//		String sjbh = arr[0][2];
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        //-------------------------------------------------获取计划数据VO
        TcjhVO condVO = new TcjhVO();
        condVO.setGc_jh_sj_id((String)rowJson.get("JHSJID"));
        TcjhVO tcjhVO = (TcjhVO) BaseDAO.getVOByPrimaryKey(conn,condVO);
        //-------------------------------------------------获取eventVO
        EventVO eventVO = EventManager.createEvent(conn, fkgxVO.getYwlx(), user);//生成事件
		String sjbh = eventVO.getSjbh();
        /**
         * 暂时不反馈业务表了
        //-------------------------------------------------存入业务表
		BaseVO bvo = new BaseVO();
		bvo.setVOTableName(fkgxVO.getYwbmc());
		bvo = initVO(bvo);
		if(Integer.parseInt(count)==0){
			pkValue = new RandomGUID().toString();
			bvo.addField(pk, BaseVO.OP_STRING|BaseVO.TP_PK);
			bvo.addField(fkgxVO.getYwzdmc(), BaseVO.OP_DATE);
			bvo.setInternal(pk,pkValue);
//			bvo.setInternal(fkgxVO.getYwzdmc(),fkrq);	//反馈问题没有反馈日期
			bvo.setInternal("JHSJID", rowJson.get("JHSJID"));
			bvo.setInternal("SJBH", sjbh);
			bvo.setInternal("YWLX", fkgxVO.getYwlx());
			BusinessUtil.setInsertCommonFields(bvo, user);
			BaseDAO.insert(conn, bvo);
		}else{
			bvo.addField(pk, BaseVO.OP_STRING|BaseVO.TP_PK);
			bvo.addField(fkgxVO.getYwzdmc(), BaseVO.OP_DATE);
			bvo.setInternal(pk,pkValue);
//			bvo.setInternal(fkgxVO.getYwzdmc(), fkrq);	//反馈问题没有反馈日期
			bvo.setInternal("JHSJID", rowJson.get("JHSJID"));
			BusinessUtil.setUpdateCommonFields(bvo,user);
			BaseDAO.update(conn, bvo);
		}
		*/
		//-------------------------------------------------反馈情况表（GC_JH_FKQK）插入数据
		String updateFkqkSql = "update GC_JH_FKQK set ZXBZ='0' where JHSJID='"+tcjhVO.getGc_jh_sj_id()+"' and FKLX='"+fkgxVO.getFklx()+"'";
		DBUtil.execSql(conn,updateFkqkSql);
		fkqkvo.setGc_jh_fkqk_id(new RandomGUID().toString()); // 主键
		fkqkvo.setJhid(tcjhVO.getJhid());
		fkqkvo.setJhsjid(tcjhVO.getGc_jh_sj_id());
		fkqkvo.setXmid(tcjhVO.getXmid());
		fkqkvo.setBdid(tcjhVO.getBdid());
		fkqkvo.setSjbh(sjbh);
//		fkqkvo.setFkid(pkValue);
		fkqkvo.setYwlx(fkgxVO.getYwlx());
		fkqkvo.setFklx(fkgxVO.getFklx());
		fkqkvo.setBz((String)rowJson.get("BZ"));
		fkqkvo.setZxbz("1");
		BusinessUtil.setInsertCommonFields(fkqkvo,user);
		BaseDAO.insert(conn, fkqkvo);
		return fkqkvo;
	}
	@Override
	public String queryFormInfo(HttpServletRequest request, String json)
			throws Exception {
		// TODO Auto-generated method stub
		Connection conn = null;
		String domresult = "";
		String fklx = request.getParameter("fklx");
		try {
			conn = DBUtil.getConnection();
			PageManager page = new PageManager();
			page.setPageRows(100);
			String condition = "";
			String roleCond = "  fklx like '"+fklx+"%' and FLAG='1' order by SORT";
			condition +=roleCond;
			String sql = "select fklx, fkms, jhzdmc, ywzdmc, fklb, bz, ywbmc, sort,title,ywlx,kzzdmc,hisurl,hisdesc,jhsjms,jhsjmc from gc_jh_fkgx";
			page.setFilter(condition);
			conn.setAutoCommit(false);
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldDateFormat("FKRQ", "yyyy-MM-dd");
			domresult = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	@Override
	public String getJhfkCounts(HttpServletRequest request, String json)
			throws Exception {
		Connection conn = null;
		String domresult = "";
		String jhsjid = request.getParameter("jhsjid");
		String fklx = request.getParameter("fklx");
		try {
			conn = DBUtil.getConnection();
			conn.setAutoCommit(false);
//			String sqlYwlx = "select YWLX from GC_JH_FKGX where FKLX='"+fklx+"'";
//			String ywlx = DBUtil.query(conn, sqlYwlx)[0][0];
			String sql = "select count(GC_JH_FKQK_ID) from GC_JH_FKQK where jhsjid='"+jhsjid+"' and FKLX='"+fklx+"'";
			String[][] resArr = DBUtil.query(conn, sql);
			if(resArr!=null){
				domresult = resArr[0][0];
			}
			conn.commit();
		} catch (Exception e) {
			e.printStackTrace(System.out);
			DBUtil.rollbackConnetion(conn);
			throw e;
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	@Override
	public String queryTabList(HttpServletRequest request, String json)
			throws Exception {
		// TODO Auto-generated method stub
		Connection conn = null;
		String domresult = "";
		String fklx = request.getParameter("fklx");
		try {
			conn = DBUtil.getConnection();
			PageManager page = RequestUtil.getPageManager(json);
			String orderFilter = RequestUtil.getOrderFilter(json);
			String condition = RequestUtil.getConditionList(json).getConditionWhere();
			condition += " and F.FKLX=G.FKLX and F.FKLX like '"+fklx+"%'" ;
			condition +=orderFilter;
			page.setFilter(condition);
			String sql = "select G.FKLB,LRSJ,FKRQ,F.BZ FKNR,F.FKID,G.TITLE " +
					"from GC_JH_FKQK F,GC_JH_FKGX G";
			conn.setAutoCommit(false);
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldDateFormat("LRSJ", "yyyy-MM-dd");
			bs.setFieldDateFormat("FKRQ", "yyyy-MM-dd");
			bs.setFieldDic("FKLB", "FKLB");
			domresult = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	@Override
	public String getFkFlag(HttpServletRequest request, String json) throws Exception {
		Connection conn = null;
		String domresult = "";
		String jhsjid = request.getParameter("jhsjid");
		String fklx = request.getParameter("fklx");
		try {
			BaseVO[] bv = null;
			conn=DBUtil.getConnection();
			FkgxVO condVO = new FkgxVO();
			condVO.setFklx(fklx);
			if(condVO!=null&&!condVO.isEmpty()){
				bv = (BaseVO [])BaseDAO.getVOByCondition(conn, condVO);
			}
			if(bv!=null){
				for(int i=0;i<bv.length;i++){
					FkgxVO newvo = (FkgxVO)bv[i];
			        TcjhVO tcjhCondVO = new TcjhVO();
			        tcjhCondVO.setGc_jh_sj_id(jhsjid);
			        TcjhVO tcjhVO = (TcjhVO) BaseDAO.getVOByPrimaryKey(conn,tcjhCondVO);
			        domresult = (String)tcjhVO.getInternal(newvo.getKzzdmc());
				}
			}
			conn.commit();
		} catch (Exception e) {
			e.printStackTrace(System.out);
			DBUtil.rollbackConnetion(conn);
			throw e;
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	@Override
	public String getFkFkrq(HttpServletRequest request, String json)
			throws Exception {
		Connection conn = null;
		String domresult = "";
		String jhsjid = request.getParameter("jhsjid");
		String fklx = request.getParameter("fklx");
		try {
			BaseVO[] bv = null;
			conn=DBUtil.getConnection();
			FkgxVO condVO = new FkgxVO();
			condVO.setFklx(fklx);
			if(condVO!=null&&!condVO.isEmpty()){
				bv = (BaseVO [])BaseDAO.getVOByCondition(conn, condVO);
			}
			if(bv!=null){
				for(int i=0;i<1;i++){
					FkgxVO newvo = (FkgxVO)bv[i];
					if(!Pub.empty(newvo.getSjbmc())&&!Pub.empty(newvo.getSjzdmc())){
						String sql = "select to_char("+newvo.getSjzdmc()+",'yyyy-MM-dd') " +
								"from "+newvo.getSjbmc()+" " +
								"where SJWYBH=(select SJWYBH from GC_JH_SJ where GC_JH_SJ_ID='"+jhsjid+"') and SFYX='1'";
						String arr[][] = DBUtil.query(conn, sql);
						if(arr!=null){
					        domresult = (arr[0][0]);
						}
					}
				}
			}
			conn.commit();
		} catch (Exception e) {
			e.printStackTrace(System.out);
			DBUtil.rollbackConnetion(conn);
			throw e;
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	@Override
	public String getFkJhrq(HttpServletRequest request, String json)
			throws Exception {
		Connection conn = null;
		String domresult = "";
		String jhsjid = request.getParameter("jhsjid");
		String fklx = request.getParameter("fklx");
		try {
			BaseVO[] bv = null;
			conn=DBUtil.getConnection();
			FkgxVO condVO = new FkgxVO();
			condVO.setFklx(fklx);
			if(condVO!=null&&!condVO.isEmpty()){
				bv = (BaseVO [])BaseDAO.getVOByCondition(conn, condVO);
			}
			if(bv!=null){
				for(int i=0;i<1;i++){
					FkgxVO newvo = (FkgxVO)bv[i];
					if(!Pub.empty(newvo.getSjbmc())&&!Pub.empty(newvo.getSjzdmc())){
						String sql = "select to_char("+newvo.getJhsjmc()+",'yyyy-MM-dd') from GC_JH_SJ where GC_JH_SJ_ID='"+jhsjid+"' and SFYX='1'";
						String arr[][] = DBUtil.query(conn, sql);
						if(arr!=null){
					        domresult = (arr[0][0]);
						}
					}
				}
			}
			conn.commit();
		} catch (Exception e) {
			e.printStackTrace(System.out);
			DBUtil.rollbackConnetion(conn);
			throw e;
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	@Override
	public String queryXmxx(HttpServletRequest request, String json)
			throws Exception {
		Connection conn = null;
		String domresult = "";
		String jhsjid = request.getParameter("jhsjid");
		try {
			conn=DBUtil.getConnection();
			TcjhVO vo =  new TcjhVO();
			vo.setGc_jh_sj_id(jhsjid);
			vo = (TcjhVO)BaseDAO.getVOByPrimaryKey(conn, vo);
			domresult = vo.getRowJson();
		} catch (Exception e) {
			e.printStackTrace(System.out);
			DBUtil.rollbackConnetion(conn);
			throw e;
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
}
