package com.ccthanking.business.gcgl.kfgl.service.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.ccthanking.business.gcgl.kfgl.service.KfglService;
import com.ccthanking.business.gcgl.kfgl.vo.KfgVO;
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
import com.ccthanking.framework.util.RandomGUID;
import com.ccthanking.framework.util.RequestUtil;


@Service
public class KfglServiceImpl implements KfglService {

	@Override
	public String query(String json,HttpServletRequest request) throws Exception {
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		try {
			
			PageManager page = RequestUtil.getPageManager(json);
			String orderFilter = RequestUtil.getOrderFilter(json);
			String condition = RequestUtil.getConditionList(json).getConditionWhere();
			String xmglgs = request.getParameter("xmglgs");
			if(!Pub.empty(xmglgs)){
				condition += "AND t.GC_TCJH_XMXDK_ID = t1.XMID ";
				condition += " AND (/*t1.XMGLGS = '"+xmglgs+"' or*/ t1.gc_jh_sj_id in (select tt.gc_jh_sj_id from gc_jh_sj tt where /*tt.xmbs = '0' and*/ tt.xmid in (select xmid from gc_jh_sj j /*where j.xmglgs = '"+xmglgs+"'*/))) ";
				
			}else{
				condition += "AND t.GC_TCJH_XMXDK_ID = t1.XMID ";
			}
			condition += BusinessUtil.getSJYXCondition("t1");
		    condition += BusinessUtil.getCommonCondition(user,"t1");
		    condition += orderFilter;
			if (page == null)
				page = new PageManager();
				page.setFilter(condition);
	
				conn.setAutoCommit(false);
				
				String sql = "SELECT " +
						"t.GC_TCJH_XMXDK_ID,t.XMLX,t.XMDZ,t.QGRQ, " +
						"(case xmbs when '0' then (select sgdw from GC_TCJH_XMXDK where gc_tcjh_xmxdk_id = t1.xmid) when '1' then (select sgdw from GC_XMBD where GC_XMBD_ID = t1.bdid) end) sgdw, "+
						"(case xmbs when '0' then (select SGDWXMJL from GC_TCJH_XMXDK where gc_tcjh_xmxdk_id = t1.xmid) when '1' then (select SGDWXMJL from GC_XMBD where GC_XMBD_ID = t1.bdid) end) FZR_SGDW, "+
						"(case xmbs when '0' then (select jldw from GC_TCJH_XMXDK where gc_tcjh_xmxdk_id = t1.xmid) when '1' then (select jldw from GC_XMBD where GC_XMBD_ID = t1.bdid) end) JLDW, "+
						"(case xmbs when '0' then (select JLDWZJ from GC_TCJH_XMXDK where gc_tcjh_xmxdk_id = t1.xmid) when '1' then (select JLDWZJ from GC_XMBD where GC_XMBD_ID = t1.bdid) end) FZR_JLDW, "+
						"(case xmbs when '0' then (select sjdw from GC_TCJH_XMXDK where gc_tcjh_xmxdk_id = t1.xmid) when '1' then (select sjdw from GC_XMBD where GC_XMBD_ID = t1.bdid) end) SJDW,"+
						"t1.GC_JH_SJ_ID,t1.BDID,t1.ND,t1.XMBH,t1.XMMC,t1.BDBH,t1.BDMC,t1.KGSJ,t1.KGSJ_SJ,t1.JHID,t1.XMSX,t1.XMBS, "+
						"(SELECT BDDD FROM GC_XMBD WHERE GC_XMBD_ID = T1.BDID)AS BDDD, "+
						"(SELECT HTID FROM GC_HTGL_HTSJ WHERE htid in(select ID from GC_HTGL_HT where htlx = 'SG' and sfyx = '1') and sfyx = '1' and jhsjid = t1.gc_jh_sj_id and rownum=1) as HTID, "+
						"(select blsj from (select TCJHSJID, blsj,KGFG from GC_GCB_KFG WHERE SFYX='1' order by blsj desc) where TCJHSJID = t1.GC_JH_SJ_ID AND KGFG = '0' and rownum = 1) as M_KGSJ,"+
						"(select blsj from (select TCJHSJID, blsj,KGFG from GC_GCB_KFG WHERE SFYX='1' order by blsj desc) where TCJHSJID = t1.GC_JH_SJ_ID AND KGFG = '1' and rownum = 1) as M_FGSJ, "+
						"(select blsj from (select TCJHSJID, blsj,KGFG from GC_GCB_KFG WHERE SFYX='1' order by blsj desc) where TCJHSJID = t1.GC_JH_SJ_ID AND KGFG = '2' and rownum = 1) as M_TGSJ,"+
						"(select GC_GCB_KFG_ID from (select GC_GCB_KFG_ID, TCJHSJID, blsj,KGFG from GC_GCB_KFG  where sfyx='1' order by blsj desc) where TCJHSJID = t1.GC_JH_SJ_ID AND KGFG = '0' and rownum = 1) as M_KGFJ,"+
						"(select GC_GCB_KFG_ID from (select GC_GCB_KFG_ID, TCJHSJID, blsj,KGFG from GC_GCB_KFG  where sfyx='1' order by blsj desc) where TCJHSJID = t1.GC_JH_SJ_ID AND KGFG = '1' and rownum = 1) as M_FGFJ, "+
						"(select GC_GCB_KFG_ID from (select GC_GCB_KFG_ID, TCJHSJID, blsj,KGFG from GC_GCB_KFG  where sfyx='1' order by blsj desc) where TCJHSJID = t1.GC_JH_SJ_ID AND KGFG = '2' and rownum = 1) as M_TGFJ, "+
						"(select sjbh from (select sjbh,GC_GCB_KFG_ID, TCJHSJID, blsj,KGFG from GC_GCB_KFG  where sfyx='1' order by blsj desc) where TCJHSJID = t1.GC_JH_SJ_ID and rownum = 1) as sjbh, "+
						"(select ywlx from (select ywlx,GC_GCB_KFG_ID, TCJHSJID, blsj,KGFG from GC_GCB_KFG  where sfyx='1' order by blsj desc) where TCJHSJID = t1.GC_JH_SJ_ID and rownum = 1) as ywlx, "+
						"(case t1.xmbs when '0' then (select XMDZ from GC_TCJH_XMXDK where nd = t1.nd and GC_TCJH_XMXDK_ID = t1.XMID and rownum = 1) when '1' then (select BDDD from GC_XMBD where GC_XMBD_ID = t1.bdid and rownum = 1) end ) as XMBDDZ " + 
						" FROM " +
						"GC_TCJH_XMXDK t,GC_JH_SJ t1";
				BaseResultSet bs = DBUtil.query(conn, sql, page);
				//表选
				bs.setFieldTranslater("FS_ORG_PERSON", "FZRXM", "ACCOUNT", "NAME");
				bs.setFieldTranslater("HTID", "GC_HTGL_HTSJ", "HTID", "HTQDJ");
				bs.setFieldTranslater("SGDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");
				bs.setFieldTranslater("JLDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");
				bs.setFieldTranslater("SJDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");
				//字典
				bs.setFieldDic("GCZL", "XMLX");
				bs.setFieldFileUpload("M_KGFJ","0040");
				bs.setFieldFileUpload("M_FGFJ","0040");
				bs.setFieldFileUpload("M_TGFJ","0040");
				bs.setFieldDic("SFFJ", "FJSCZT");
				bs.setFieldDic("KGFG", "KFTGZT");
				bs.setFieldDic("XMLX", "XMLX");
				//add by cbl 绑定sjbh
				bs.setFieldSjbh("sjbh");
				//add by cbl end
				//日期
				bs.setFieldDateFormat("BLSJ", "yyyy-MM-dd");
				domresult = bs.getJson();
		}catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	
	@Override
	public String queryXdxmk(String json,HttpServletRequest request) throws Exception {
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		try {
			
		PageManager page = RequestUtil.getPageManager(json);
		String condition = RequestUtil.getConditionList(json).getConditionWhere();
		String orderFilter = RequestUtil.getOrderFilter(json);
		String xmglgs = request.getParameter("xmglgs");
		/**
		if(!StringUtils.isBlank(yjxm)){
			condition += " AND t.XMSX = '"+yjxm+"' ";
		}
		if(!StringUtils.isBlank(condition)){
			condition += " AND t.xmid = t2.GC_TCJH_XMXDK_ID and t1.sfyx='1' AND t1.ISXF='1' ";
		}else{
			condition += " t.jhid = t1.GC_JH_ZT_ID AND t.xmbh = t2.xmbh and t1.sfyx='1' AND t1.ISXF='1' ";
		}
		 **/
		condition += " AND t.xmid = t2.GC_TCJH_XMXDK_ID ";
		if(!Pub.empty(xmglgs)){
			condition += " AND (/*t.XMGLGS = '"+xmglgs+"' or*/ t.gc_jh_sj_id in (select tt.gc_jh_sj_id from gc_jh_sj tt where /*tt.xmbs = '0' and*/ tt.xmid in (select xmid from gc_jh_sj j /*where j.xmglgs = '"+xmglgs+"'*/))) ";
		}
		condition += " AND T.GC_JH_SJ_ID NOT IN(SELECT JHSJID FROM GC_GCGL_GCSX WHERE SFYX ='1') ";
		condition += BusinessUtil.getSJYXCondition("t");
	    condition += BusinessUtil.getCommonCondition(user,"t");
	    condition += orderFilter;
		if (page == null)
			page = new PageManager();
			page.setFilter(condition);

			conn.setAutoCommit(false);
			String sql = "SELECT "
					+ "t.GC_JH_SJ_ID,t.JHID,t.xmbs,t.ND,t.XMID,t.BDID,t.XMBH,t.BDBH,t.XMMC,t.BDMC,t.XMXZ,t.PXH,t.KGSJ,t.KGSJ_SJ,t.KGSJ_BZ,t.WGSJ,t.WGSJ_SJ,t.WGSJ_BZ,t.KYPF,t.KYPF_SJ,t.KYPF_BZ,t.HPJDS,t.HPJDS_SJ,t.HPJDS_BZ,t.GCXKZ,t.GCXKZ_SJ,t.GCXKZ_BZ,t.SGXK,t.SGXK_SJ,t.SGXK_BZ,t.CBSJPF,t.CBSJPF_SJ,t.CBSJPF_BZ,t.CQT,t.CQT_SJ,t.CQT_BZ,t.PQT,t.PQT_SJ,t.PQT_BZ,t.SGT,t.SGT_SJ,t.SGT_BZ,t.TBJ,t.TBJ_SJ,t.TBJ_BZ,t.CS,t.CS_SJ,t.CS_BZ,t.JLDW jh_jldw,t.JLDW_SJ,t.JLDW_BZ,t.SGDW jh_sgdw,t.SGDW_SJ,t.SGDW_BZ,t.ZC,t.ZC_SJ,t.ZC_BZ,t.PQ,t.PQ_SJ,t.PQ_BZ,t.JG,t.JG_SJ,t.JG_BZ,t.XMSX,t.YWLX,t.SJBH,t.BZ,t.LRR,t.LRSJ,t.LRBM,t.LRBMMC,t.GXR,t.GXSJ,t.GXBM,t.GXBMMC,t.SJMJ,t.SFYX,t.XFLX,t.ISXF,t.xmglgs,t2.GC_TCJH_XMXDK_ID,t2.FZR_GLGS,t2.xmdz,t2.xmlx,t2.YZDB,t2.JSNR,t2.WGRQ,t2.LXFS_GLGS,t2.LXFS_YZDB, "
					+"(SELECT SJHM FROM FS_ORG_PERSON WHERE ACCOUNT = T2.YZDB)AS YZDBLXFS,"
					+ "(case xmbs when '0' then (select sgdw from GC_TCJH_XMXDK where gc_tcjh_xmxdk_id = t.xmid) when '1' then (select sgdw from GC_XMBD where GC_XMBD_ID = t.bdid) end) sgdw, "
					+ "(case xmbs when '0' then (select fzr_sgdw from GC_TCJH_XMXDK where gc_tcjh_xmxdk_id = t.xmid) when '1' then (select SGDWXMJL from GC_XMBD where GC_XMBD_ID = t.bdid) end) FZR_SGDW, "
					+ "(case xmbs when '0' then (select jldw from GC_TCJH_XMXDK where gc_tcjh_xmxdk_id = t.xmid) when '1' then (select jldw from GC_XMBD where GC_XMBD_ID = t.bdid) end) JLDW, "
					+ "(case xmbs when '0' then (select fzr_jldw from GC_TCJH_XMXDK where gc_tcjh_xmxdk_id = t.xmid) when '1' then (select jldwfzr from GC_XMBD where GC_XMBD_ID = t.bdid) end) FZR_JLDW, "
					+ "(case xmbs when '0' then (select lxfs_sgdw from GC_TCJH_XMXDK where gc_tcjh_xmxdk_id = t.xmid) when '1' then (select sgdwfzrlxfs from GC_XMBD where GC_XMBD_ID = t.bdid) end) LXFS_SGDW, "
					+ "(case xmbs when '0' then (select LXFS_JLDW from GC_TCJH_XMXDK where gc_tcjh_xmxdk_id = t.xmid) when '1' then (select jldwfzrlxfs from GC_XMBD where GC_XMBD_ID = t.bdid) end) LXFS_JLDW, "
					
					+ "(case xmbs when '0' then (select sjdw from GC_TCJH_XMXDK where gc_tcjh_xmxdk_id = t.xmid) when '1' then (select sjdw from GC_XMBD where GC_XMBD_ID = t.bdid) end) sjdw, "
					+ "(case xmbs when '0' then (select fzr_sJdw from GC_TCJH_XMXDK where gc_tcjh_xmxdk_id = t.xmid) when '1' then (select SJDWFZR from GC_XMBD where GC_XMBD_ID = t.bdid) end) FZR_SJDW, "
					+ "(case xmbs when '0' then (select LXFS_SJDW from GC_TCJH_XMXDK where gc_tcjh_xmxdk_id = t.xmid) when '1' then (select SJDWFZRLXFS from GC_XMBD where GC_XMBD_ID = t.bdid) end) LXFS_SJDW, "
					
					+ "(case xmbs when '0' then (select JLDWZJ from GC_TCJH_XMXDK where gc_tcjh_xmxdk_id = t.xmid) when '1' then (select JLDWZJ from GC_XMBD where GC_XMBD_ID = t.bdid) end) JLDWZJ, "
					+ "(case xmbs when '0' then (select JLDWZJLXDH from GC_TCJH_XMXDK where gc_tcjh_xmxdk_id = t.xmid) when '1' then (select JLDWZJLXDH from GC_XMBD where GC_XMBD_ID = t.bdid) end) JLDWZJLXDH, "
					+ "(case xmbs when '0' then (select SGDWXMJL from GC_TCJH_XMXDK where gc_tcjh_xmxdk_id = t.xmid) when '1' then (select SGDWXMJL from GC_XMBD where GC_XMBD_ID = t.bdid) end) SGDWXMJL, "
					+ "(case xmbs when '0' then (select SGDWXMJLLXDH from GC_TCJH_XMXDK where gc_tcjh_xmxdk_id = t.xmid) when '1' then (select SGDWXMJLLXDH from GC_XMBD where GC_XMBD_ID = t.bdid) end) SGDWXMJLLXDH, "
					+ "(case xmbs when '0' then (select XMDZ from GC_TCJH_XMXDK where nd = t.nd and GC_TCJH_XMXDK_ID = t.XMID and rownum = 1) when '1' then (select BDDD from GC_XMBD where GC_XMBD_ID = t.bdid and rownum = 1) end ) as XMBDDZ "
					+ "FROM "
					+ "GC_JH_SJ t,GC_TCJH_XMXDK t2";
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldDic("ISXF", "SF");
			bs.setFieldDic("XMSX", "XMSX");
			bs.setFieldTranslater("XMGLGS", "FS_ORG_DEPT", "ROW_ID", "BMJC");//项目管理公司
			bs.setFieldTranslater("SGDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");
			bs.setFieldTranslater("JLDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");
			bs.setFieldTranslater("SJDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");
			bs.setFieldTranslater("YZDB", "FS_ORG_PERSON", "ACCOUNT", "NAME");//业主代表
			bs.setFieldTranslater("FZR_GLGS", "FS_ORG_PERSON", "ACCOUNT", "NAME");//项目管理公司负责人
			//日期
			bs.setFieldDateFormat("LRSJ", "yyyy-MM-dd");
			domresult = bs.getJson();
		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	
	@Override
	public String insert(String json,HttpServletRequest request) throws Exception {
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		Connection conn = DBUtil.getConnection();
		String resultVO = null;
		EventVO eventVO = null;
		KfgVO vo = new KfgVO();//开复工令表
		try {
			conn.setAutoCommit(false);
			JSONArray list = vo.doInitJson(json);
			vo.setValueFromJson((JSONObject)list.get(0));
			String ywid = request.getParameter("ywid");
			
			String kgfg = vo.getKgfg();
			String ywxl = "";
			if("0".equals(kgfg)) {
				ywxl = YwlxManager.GC_GCGL_KFTGL;
			} else if("1".equals(kgfg)) {
				ywxl = YwlxManager.GC_GCGL_FGL;
			} else if("2".equals(kgfg)) {
				ywxl = YwlxManager.GC_GCGL_TGL;
			}
			
			eventVO = EventManager.createEvent(conn, ywxl, user);//生成事件
			if(Pub.empty(vo.getGc_gcb_kfg_id())){
				if(StringUtils.isBlank(ywid)){
					vo.setGc_gcb_kfg_id(new RandomGUID().toString());
				}else{
					vo.setGc_gcb_kfg_id(ywid); 
					FileUploadVO fvo = new FileUploadVO();
			        fvo.setFjzt("1");//更新附件状态
			        fvo.setGlid2(vo.getTcjhsjid());//存入计划数据ID
			        fvo.setGlid3(vo.getXdkid()); //存入项目ID
			        fvo.setGlid4(vo.getBdid()); //存入标段ID
			        fvo.setSjbh(eventVO.getSjbh());
			        FileUploadService.updateVOByYwid(conn, fvo, vo.getGc_gcb_kfg_id(),user);
					//维护附件表FileUploadVO end
				}
				vo.setZt("0");//是否下达状态
				vo.setTbsj(new Date());
				vo.setYwlx(ywxl);
			   
			    vo.setSjbh(eventVO.getSjbh());
				BusinessUtil.setInsertCommonFields(vo,user);
				//维护附件表FileUploadVO start
			    //add by cbl 审批用
				vo.setConnection(conn);
			    //add by cbl
				FileUploadVO fuv = new FileUploadVO();
				fuv.setYwlx(ywxl);
				fuv.setSjbh(eventVO.getSjbh());
				fuv.setFjzt("1");
				FileUploadService.updateVOByYwid(conn, fuv, ywid);
				//维护附件表FileUploadVO end
				BaseDAO.insert(conn, vo);
				LogManager.writeUserLog(vo.getSjbh(), vo.getYwlx(),
						Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
						user.getOrgDept().getDeptName() + " " + user.getName()
								+ "新增<开复工令>成功", user,"","");
				

			}else{
				FileUploadService.updateFjztByYwid(conn, vo.getGc_gcb_kfg_id());
				BusinessUtil.setUpdateCommonFields(vo,user);
			    //add by cbl 审批用
				vo.setConnection(conn);
				vo.setYwlx(ywxl);
				FileUploadVO fvo = new FileUploadVO();
		        fvo.setFjzt("1");//更新附件状态
		        fvo.setGlid2(vo.getTcjhsjid());//存入计划数据ID
		        fvo.setGlid3(vo.getXdkid()); //存入项目ID
		        fvo.setGlid4(vo.getBdid()); //存入标段ID
		        FileUploadService.updateVOByYwid(conn, fvo, vo.getGc_gcb_kfg_id());
				BaseDAO.update(conn, vo);
				
				LogManager.writeUserLog(vo.getSjbh(), vo.getYwlx(),
						Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
						user.getOrgDept().getDeptName() + " " + user.getName()
								+ "修改<开复工令>成功", user,"","");
			}
		
			resultVO = vo.getRowJson();
			conn.commit();
			String jsona="{querycondition: {conditions: [{\"value\":\""+vo.getGc_gcb_kfg_id()+"\",\"fieldname\":\"t3.GC_GCB_KFG_ID\",\"operation\":\"=\",\"logic\":\"and\"} ]}}";
            return queryKfgById(jsona, request);
		}catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return resultVO;
	}
	
	
	@Override
	public String queryKfgById(String json,HttpServletRequest request) throws Exception {
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		try {

			PageManager page = RequestUtil.getPageManager(json);
			String orderFilter = RequestUtil.getOrderFilter(json);
			String condition = RequestUtil.getConditionList(json).getConditionWhere();
			String kfg = request.getParameter("kfg");
			if(Pub.empty(kfg)){
				kfg = "";
			}else{
				kfg = kfg.substring(0,kfg.length()-1);
			}
			if(!Pub.empty(kfg)){
				condition += " AND t3.KGFG in("+kfg+")";
			}
			condition += " AND t.GC_TCJH_XMXDK_ID = t1.XMID AND t1.gc_jh_sj_id = t3.TCJHSJID ";
			condition += BusinessUtil.getSJYXCondition("t3");
		    condition += BusinessUtil.getCommonCondition(user,"t3");
		    condition += orderFilter;
			if (page == null)
				page = new PageManager();
				page.setFilter(condition);
	
				conn.setAutoCommit(false);
				String sql = "SELECT " +
						"t.GC_TCJH_XMXDK_ID,t.XMLX,t.XMDZ,t.QGRQ, "+
						"(case xmbs when '0' then (select sgdw from GC_TCJH_XMXDK where gc_tcjh_xmxdk_id = t1.xmid) when '1' then (select sgdw from GC_XMBD where GC_XMBD_ID = t1.bdid) end) sgdw, "+
						"(case xmbs when '0' then (select SGDWXMJL from GC_TCJH_XMXDK where gc_tcjh_xmxdk_id = t1.xmid) when '1' then (select SGDWXMJL from GC_XMBD where GC_XMBD_ID = t1.bdid) end) FZR_SGDW, "+
						"(case xmbs when '0' then (select jldw from GC_TCJH_XMXDK where gc_tcjh_xmxdk_id = t1.xmid) when '1' then (select jldw from GC_XMBD where GC_XMBD_ID = t1.bdid) end) JLDW, "+
						"(case xmbs when '0' then (select JLDWZJ from GC_TCJH_XMXDK where gc_tcjh_xmxdk_id = t1.xmid) when '1' then (select JLDWZJ from GC_XMBD where GC_XMBD_ID = t1.bdid) end) FZR_JLDW, "+
						"(case xmbs when '0' then (select sjdw from GC_TCJH_XMXDK where gc_tcjh_xmxdk_id = t1.xmid) when '1' then (select sjdw from GC_XMBD where GC_XMBD_ID = t1.bdid) end) SJDW,"+
						"t1.GC_JH_SJ_ID, t1.ND, t1.XMBH, t1.XMMC, t1.BDBH, t1.BDMC,t1.KGSJ,t1.KGSJ_SJ,t1.JHID,t1.XMSX, "+
						//暂时注释合同签订价待与科瑞确定
						"(SELECT HTID FROM GC_HTGL_HTSJ WHERE htid in(select ID from GC_HTGL_HT where htlx = 'SG' and sfyx = '1') and sfyx = '1' and jhsjid = t1.gc_jh_sj_id) as HTID, "+
						"t3.GC_GCB_KFG_ID,t3.TCJHSJID,t3.KGFG,t3.TBR,t3.TBRXM,t3.TBDW,t3.TBDWMC,t3.BDID,t3.JSDW,t3.GCSL,t3.JGNR,t3.FZR,t3.FZRXM,t3.BLSJ,t3.BZ,t3.JHKGSJ,t3.SJKGSJ,t3.JHJGSJ,t3.sjbh,t3.ywlx,t3.SJTGSJ "+
						",GC_GCB_KFG_ID FJ "+
						"FROM " +
						"GC_TCJH_XMXDK t, GC_JH_SJ t1,GC_GCB_KFG t3 ";
				BaseResultSet bs = DBUtil.query(conn, sql, page);
				bs.setFieldTranslater("FS_ORG_PERSON", "FZRXM", "ACCOUNT", "NAME");
				bs.setFieldTranslater("HTID", "GC_HTGL_HTSJ", "HTID", "HTQDJ");
				bs.setFieldTranslater("SGDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");
				bs.setFieldTranslater("JLDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");
				bs.setFieldTranslater("SJDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");
				//字典
				bs.setFieldDic("XMLX", "XMLX");
				bs.setFieldFileUpload("FJ","0040");
				bs.setFieldDic("KGFG", "KFTGZT");
				//日期
				bs.setFieldDateFormat("BLSJ", "yyyy-MM-dd");
				//add by cbl start
				bs.setFieldSjbh("sjbh");
				//add by cbl end
				domresult = bs.getJson();
		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	
	@Override
	public String update(String json,HttpServletRequest request) throws Exception {
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		String ztid = request.getParameter("ztid");
		Connection conn = DBUtil.getConnection();
		String resultVO = null;
		EventVO eventVO = null;
		KfgVO vo = new KfgVO();//开复工令表
		
		try {
			conn.setAutoCommit(false);
			JSONArray list = vo.doInitJson(json);
			vo.setValueFromJson((JSONObject)list.get(0));
			
			FileUploadService.updateFjztByYwid(conn, vo.getGc_gcb_kfg_id());
			BusinessUtil.setUpdateCommonFields(vo,user);
			BaseDAO.update(conn, vo);
			if(!Pub.empty(ztid)){
				
			}
			
			
			LogManager.writeUserLog(vo.getSjbh(), vo.getYwlx(),
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "修改<开复工令>成功", user,"","");
			
		
			resultVO = vo.getRowJson();
			conn.commit();
			
		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return resultVO;
	}
	
	@Override
	public String insertZt(String json,HttpServletRequest request) throws Exception {
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		String ywid = request.getParameter("ywid");
		String kfglid = request.getParameter("kfglid");
		Connection conn = DBUtil.getConnection();
		String resultVO = null;
		KfgVO vo = null;//开复工令表
		
		try {
			conn.setAutoCommit(false);
			
			resultVO = vo.getRowJson();
			conn.commit();
			
		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return resultVO;
	}
	
	@Override
	public String queryKfglByXm(String json,HttpServletRequest request) throws Exception {
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		String tcjhid = request.getParameter("tcjhid");
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		try {
			conn.setAutoCommit(false);
			String sql ="SELECT * FROM GC_GCB_KFG WHERE TCJHSJID = '"+json+"'"; 
			BaseResultSet bs = DBUtil.query(conn, sql, null);
			ResultSet rs = bs.getResultSet();
			if(rs.next()){
				//记录存在
				domresult = "true";
			}else{
				//记录不存在
				domresult = "false";
			}
			
		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	
	@Override
	public String delete(HttpServletRequest request, String json) throws Exception {
		Connection conn = null;
		String domresult = "";
		KfgVO vo = new KfgVO();
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		try {
			conn = DBUtil.getConnection();
			conn.setAutoCommit(false);
			JSONArray list = vo.doInitJson(json);
			vo.setValueFromJson((JSONObject)list.get(0));
			//删业务数据
			KfgVO delVO = new KfgVO();
			delVO.setGc_gcb_kfg_id(vo.getGc_gcb_kfg_id());
			delVO.setSfyx("0");
			BaseDAO.update(conn, delVO);
			//删附件
			FileUploadVO fvo = new FileUploadVO();
			fvo.setYwid(vo.getGc_gcb_kfg_id());
			FileUploadService.deleteByConditionVO(conn, fvo);
			LogManager.writeUserLog(vo.getSjbh(), vo.getYwlx(),
					Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "开复工令删除成功", user,"","");
			conn.commit();
			String jsona = Pub.makeQueryConditionByID(vo.getTcjhsjid(), "t1.GC_JH_SJ_ID");
            return query(jsona, request);
		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
}
