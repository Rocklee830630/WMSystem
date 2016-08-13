

package com.ccthanking.business.ztb.zbxq;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import com.ccthanking.business.ztb.zbgl.GcZtbJhsjVO;
import com.ccthanking.business.ztb.zbgl.GcZtbSjVO;
import com.ccthanking.common.BusinessUtil;
import com.ccthanking.common.EventManager;
import com.ccthanking.common.YwlxManager;
import com.ccthanking.common.vo.EventVO;
import com.ccthanking.framework.Globals;
import com.ccthanking.framework.base.BaseDAO;
import com.ccthanking.framework.base.BaseVO;
import com.ccthanking.framework.coreapp.orgmanage.UserManager;
import com.ccthanking.framework.common.BaseResultSet;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.PageManager;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.fileUpload.service.FileUploadService;
import com.ccthanking.framework.log.LogManager;
import com.ccthanking.framework.util.Pub;
import com.ccthanking.framework.util.RandomGUID;
import com.ccthanking.framework.util.RequestUtil;
import com.ccthanking.framework.fileUpload.vo.FileUploadVO;

@Service
public class ZhaoBiaoXuQiuServiceImpl implements ZhaoBiaoXuQiuService {

	private String ywlx=YwlxManager.GC_ZTB_XQ;

	@Override
	public String queryConditionZhaobiaoxuqiu(HttpServletRequest request,String json) throws Exception {
		Connection conn = null;
		String domresult = "";
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		String xqzt = request.getParameter("xqzt");
		String readonly = request.getParameter("readonly");
		try {
			conn = DBUtil.getConnection();
			PageManager page = RequestUtil.getPageManager(json);
			String orderFilter = RequestUtil.getOrderFilter(json);
			String condition = RequestUtil.getConditionList(json).getConditionWhere();
			String roleCond = "";
			if(Pub.empty(xqzt)){
				if(!"1".equals(readonly)){//非只读页面需要查询此条件
					roleCond = " and x.lrbm='"+user.getDepartment()+"'";
				}
			}else{
				roleCond = " and x.xqzt in ("+xqzt+")";
			}
			condition +=roleCond;
			condition += BusinessUtil.getSJYXCondition("X") + BusinessUtil.getCommonCondition(user,null);
			if(!Pub.empty(condition)){
				condition +=" and x.gc_ztb_xq_id=a.xqid(+) and a.jhsjid=b.gc_jh_sj_id(+) ";
				
			}else{
				condition="   x.gc_ztb_xq_id=a.xqid(+) and a.jhsjid=b.gc_jh_sj_id(+)  ";
			}
			condition +=orderFilter;
			page.setFilter(condition);
			String sql = "select distinct X.gc_ztb_xq_id, X.gzmc, X.gznr, X.zzyjyq, X.sxyq, X.jsyq, X.cgmbyq, X.pbryyq, X.pbsbyq, X.tbjfs, X.zbfs, X.qtyq, X.zblx, X.yse, X.xqdwjbr, X.xqdwjbrsj, X.xqdwfzr, X.xqdwfzrsj, X.zbbjbr, X.zbbfzr, X.xqzt, X.ywlx, X.sjbh, X.bz, X.lrr, X.lrsj, X.lrbm, X.lrbmmc, X.gxr, X.gxsj, X.gxbm, X.gxbmmc, X.sjmj, X.sfyx,X.QFBZ,X.TGBZ,X.nd " +
			"from gc_ztb_xq X,(select * from  GC_ZTB_JHSJ where SFYX='1') a,(select * from  gc_jh_sj where SFYX='1') b";
			conn.setAutoCommit(false);
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldDic("ZBLX", "ZBLX");//招标类型
			bs.setFieldDic("ZBFS", "ZBFS");//招标方式
			bs.setFieldDic("XQZT", "SF");//招标方式
			bs.setFieldDic("XQZT", "XQZT");//垫资方式
			bs.setFieldDic("TBJFS", "TBBJFS");//投标报价方式
			bs.setFieldThousand("YSE");//预算额
			bs.setFieldSjbh("sjbh");//事件编号
			domresult = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace(System.out);
			e.printStackTrace();
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	
	@Override
	public String queryConditionZhaobiaoxuqiuNd(HttpServletRequest request,String json) throws Exception {
		Connection conn = null;
		String domresult = "";
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		try {
			conn = DBUtil.getConnection();
			PageManager page = RequestUtil.getPageManager(json);
			String orderFilter = RequestUtil.getOrderFilter(json);
			String condition = RequestUtil.getConditionList(json).getConditionWhere();
			condition += " and X.sfyx = '1' ";
			condition += BusinessUtil.getSJYXCondition("X") + BusinessUtil.getCommonCondition(user,null);
			condition +=orderFilter;
			page.setFilter(condition);
			String sql = "select gc_ztb_xq_id, gzmc, gznr, zzyjyq, sxyq, jsyq, cgmbyq, pbryyq, pbsbyq, tbjfs, zbfs, qtyq, zblx, yse, xqdwjbr, xqdwjbrsj, xqdwfzr, xqdwfzrsj, zbbjbr, zbbfzr, xqzt, ywlx, sjbh, bz, lrr, lrsj, lrbm, lrbmmc, gxr, gxsj, gxbm, gxbmmc, sjmj, sfyx,QFBZ,TGBZ,nd " +
			"from gc_ztb_xq X";
			conn.setAutoCommit(false);
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldDic("ZBLX", "ZBLX");//招标类型
			bs.setFieldDic("ZBFS", "ZBFS");//招标方式
			bs.setFieldDic("XQZT", "SF");//招标方式
			bs.setFieldDic("XQZT", "XQZT");//垫资方式
			bs.setFieldDic("TBJFS", "TBBJFS");//投标报价方式
			bs.setFieldThousand("YSE");//预算额
			bs.setFieldSjbh("sjbh");//事件编号
			domresult = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace(System.out);
			e.printStackTrace();
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	

	@Override
	public String updatebatchdataNobg(String json, User user) throws Exception {
		Connection conn = DBUtil.getConnection();
		String resultVO = "";
		try {
			conn.setAutoCommit(false);
			// ===========更新项目计划管理信息===========
			GcZtbXqVO xmvo = new GcZtbXqVO();
			JSONArray list = xmvo.doInitJson(json);

			List<String> listrow = new ArrayList<String>();

			GcZtbXqVO xmVO = null;
			for (int i = 0; i < list.size(); i++) {
				xmVO = new GcZtbXqVO();
				xmVO.setValueFromJson((JSONObject) list.get(i));
				String nd = ("undefined".equals(xmVO.getNd()) || "请选择".equals(xmVO.getNd())) ? "" : xmVO.getNd();
				xmVO.setNd(nd);
				
				System.out.println(xmVO.getGzmc()+":"+xmVO.getNd());
				
				BusinessUtil.setUpdateCommonFields(xmVO,user);
				BaseDAO.update(conn, xmVO);
				listrow.add(xmVO.getRowJsonSingle());
			}
			conn.commit();
			LogManager.writeUserLog(xmVO.getSjbh(), ywlx,
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "更新<统筹计划数据>成功", user, "", "");

			//
			resultVO = BaseDAO.comprisesResponseData(conn, listrow);
		}catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return resultVO;
	}

	@Override
	public String insertdemo(HttpServletRequest request,String json) throws Exception {
		
		Connection conn = DBUtil.getConnection();
		GcZtbXqVO xmvo = new GcZtbXqVO();
		String jhsjids=request.getParameter("jhsjids");
		String ywid = request.getParameter("ywid");
		String blFalg = request.getParameter("blFlag");
		String logStr = "招标需求添加成功";
		String condition = request.getParameter("condition") == null ? "" :request.getParameter("condition");
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		try {
			conn.setAutoCommit(false);
			JSONArray list = xmvo.doInitJson(json);
			xmvo.setValueFromJson((JSONObject)list.get(0));
			xmvo.setYwlx(ywlx);//业务类型
			EventVO eventVO = EventManager.createEvent(conn, xmvo.getYwlx(), user);//生成事件
			//设置主键
			if(!Pub.empty(ywid)){
				xmvo.setGc_ztb_xq_id(ywid); // 主键
				//维护附件表FileUploadVO start
				FileUploadVO fuv = new FileUploadVO();
				fuv.setYwlx(ywlx);
				fuv.setSjbh(eventVO.getSjbh());
				fuv.setFjzt("1");
				//FileUploadService.updateFjztByYwid(conn, ywid);
				FileUploadService.updateVOByYwid(conn, fuv, ywid);
				//维护附件表FileUploadVO end
			}else{
				xmvo.setGc_ztb_xq_id(new RandomGUID().toString()); // 主键
			}
			if(Pub.empty(xmvo.getXqzt()))
			{
				xmvo.setXqzt("1");
			}
			if(!Pub.empty(blFalg)){
				user = UserManager.getInstance().getUserByLoginName(xmvo.getLrr());
				xmvo.setXqzt("3");
				xmvo.setTgbz("1");//补录的数据，肯定没有走审批，所以都按点击“通过按钮”处理
				logStr = "补录招标需求成功";
			}
			BusinessUtil.setInsertCommonFields(xmvo, user);
			xmvo.setSjbh(eventVO.getSjbh());
		    //add by cbl 审批用
			xmvo.setConnection(conn);
		    //add by cbl
			//插入
			BaseDAO.insert(conn, xmvo);
			//如果没有计划数据ID，那就不存GC_ZTB_JHSJ表了
			if(!Pub.empty(jhsjids)){
				String[] jhsjid= jhsjids.split(",");
				for (int i = 0; i < jhsjid.length; i++) {
					GcZtbJhsjVO jhsjvo = new GcZtbJhsjVO();
					String tcjhSql = "select SJWYBH from GC_JH_SJ where GC_JH_SJ_ID='"+jhsjid[i]+"'";
					String arr[][] = DBUtil.query(conn, tcjhSql);
					if(arr!=null && arr.length!=0){
						jhsjvo.setSjwybh(arr[0][0]);//保存数据唯一编号
					}
					jhsjvo.setJhsjid(jhsjid[i]);
					jhsjvo.setSjbh(eventVO.getSjbh());
					jhsjvo.setGc_ztb_jhsj_id(new RandomGUID().toString()); // 主键
					jhsjvo.setXqid(xmvo.getGc_ztb_xq_id());
					BusinessUtil.setInsertCommonFields(jhsjvo, user);
					jhsjvo.setYwlx(ywlx);// 业务类型
					// EventVO eventVO1 = EventManager.createEvent(conn,
					// jhsjvo.getYwlx(), user);//生成事件
					// jhsjvo.setSjbh(eventVO1.getSjbh());
					// 插入
					BaseDAO.insert(conn, jhsjvo);
				}
			}
			// 插入AP_PROCESSCONFIG表start
			// 通过ywlx,user的角色获取WS_TEMPLATEID,OPERATIONOID
			Pub.getFlowinf(conn, ywlx, xmvo.getSjbh(), user, condition);
			// 插入AP_PROCESSCONFIG表 end
			conn.commit();
			LogManager.writeUserLog(xmvo.getSjbh(), xmvo.getYwlx(),
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ logStr, user,"","");
	
			} catch (Exception e) {
				DBUtil.rollbackConnetion(conn);
				e.printStackTrace(System.out);
			} finally {
				DBUtil.closeConnetion(conn);
			}
			String jsona="{querycondition: {conditions: [{\"value\":\""+xmvo.getGc_ztb_xq_id()+"\",\"fieldname\":\"gc_ztb_xq_id\",\"operation\":\"=\",\"logic\":\"and\"} ]}}";
			String resultXinXi=this.queryConditionZhaobiaoxuqiu(request,jsona);
			return resultXinXi;
		//return "";
	}
	@Override
	public String doQdzb(HttpServletRequest request,String json) throws Exception {
		
		Connection conn = DBUtil.getConnection();
		String resultVO = null;
		GcZtbSjVO ztbsj = new GcZtbSjVO();
		String ztbxqid = request.getParameter("ztbxqid");
		String zbfs = request.getParameter("zbfs");
		String zxmid = request.getParameter("zxmid");
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		try {
			conn.setAutoCommit(false);
			JSONArray list = ztbsj.doInitJson(json);
			ztbsj.setValueFromJson((JSONObject)list.get(0));
			//设置主键
			ztbsj.setGc_ztb_sj_id(new RandomGUID().toString()); // 主键

			BusinessUtil.setInsertCommonFields(ztbsj, user);
			ztbsj.setYwlx(ywlx);//业务类型
			EventVO eventVO = EventManager.createEvent(conn, YwlxManager.GC_ZTB_SJ, user);//生成事件
			ztbsj.setSjbh(eventVO.getSjbh());
			//插入
			BaseDAO.insert(conn, ztbsj);
			if(!Pub.empty(zxmid)){
				//如果存在选中的子项目，那么补全JHSJ对应的招投标数据ID
				zxmid = zxmid.replaceAll(",","','");
				String zxmSql = "update GC_ZTB_JHSJ set ZBID='"+ztbsj.getGc_ztb_sj_id()+"' where GC_ZTB_JHSJ_ID in ('"+zxmid+"')";
				DBUtil.execSql(conn, zxmSql);
			}
			String[] jhsjid= ztbxqid.split(",");//这个存的是需求ID
			for(int i=0;i<jhsjid.length;i++)
			{
				GcZtbXqsjYsVO xqsjvo = new GcZtbXqsjYsVO();
				xqsjvo.setGc_ztb_xqsj_ys_id(new RandomGUID().toString());
				xqsjvo.setZtbxqid(jhsjid[i]);
				xqsjvo.setZtbsjid(ztbsj.getGc_ztb_sj_id());
				BusinessUtil.setInsertCommonFields(xqsjvo, user);//公共字段更新
				xqsjvo.setYwlx(ywlx);//业务类型
				xqsjvo.setSjbh(eventVO.getSjbh());
				//插入
				BaseDAO.insert(conn, xqsjvo);
				String countSql = "select count(GC_ZTB_JHSJ_ID) from GC_ZTB_JHSJ where ZBID is null and XQID='"+jhsjid[i]+"'";
				String wzbjhsj = DBUtil.query(conn, countSql)[0][0];
				if("0".equals(wzbjhsj)){
					//如果不存在未招标的计划数据，那么更新需求状态
					String sql = "update GC_ZTB_XQ set XQZT='5',ZBFS='"+zbfs+"' where GC_ZTB_XQ_ID='"+jhsjid[i]+"'";
					DBUtil.execSql(conn, sql);
				}
			}
			//resultVO = ztbsj.getRowJson();
			conn.commit();
			String jsona="{querycondition: {conditions: [{\"value\":\""+jhsjid[0]+"\",\"fieldname\":\"GC_ZTB_XQ_ID\",\"operation\":\"=\",\"logic\":\"and\"} ]}}";
			resultVO=this.queryConditionZhaobiaoxuqiu(request,jsona);
			LogManager.writeUserLog(ztbsj.getSjbh(), ztbsj.getYwlx(),
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "启动招标需求成功", user,"","");
	
			} catch (Exception e) {
				DBUtil.rollbackConnetion(conn);
				e.printStackTrace(System.out);
			} finally {
				DBUtil.closeConnetion(conn);
			}
			 		
		return resultVO;
	}
	@Override
	public String queryZhaoBiaoXiangMu(String json, User user) {
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		try {
      
			PageManager page = RequestUtil.getPageManager(json);
			String orderFilter = RequestUtil.getOrderFilter(json);
			String condition = RequestUtil.getConditionList(json).getConditionWhere();
		    condition += BusinessUtil.getSJYXCondition("a") + BusinessUtil.getCommonCondition(user,null);
		    condition+=" and a.jhsjid=c.gc_jh_sj_id and c.xmid=b.gc_tcjh_xmxdk_id and c.bdid=d.gc_xmbd_id(+) ";
			condition += orderFilter;
		    page.setFilter(condition);
			conn.setAutoCommit(false);
			String sql = " select a.gc_ztb_jhsj_id, a.xqid, a.jhsjid, a.xmid xmxqid, a.bdid bdxqid, a.zzbj," +
					" c.GC_JH_SJ_ID,c.JHID,c.xmbs,c.ND,c.XMID,c.BDID,c.XMBH,c.BDBH,c.XMMC,c.BDMC,c.XMXZ,c.PXH,c.KGSJ,c.KGSJ_SJ,c.KGSJ_BZ," +
					"c.WGSJ,c.WGSJ_SJ,c.WGSJ_BZ,c.KYPF,c.KYPF_SJ,c.KYPF_BZ,c.HPJDS,c.HPJDS_SJ,c.HPJDS_BZ,c.GCXKZ,c.GCXKZ_SJ,c.GCXKZ_BZ,c.SGXK," +
					"c.SGXK_SJ,c.SGXK_BZ,c.CBSJPF,c.CBSJPF_SJ,c.CBSJPF_BZ,c.CQT,c.CQT_SJ,c.CQT_BZ,c.PQT,c.PQT_SJ," +
					"c.PQT_BZ,c.SGT,c.SGT_SJ,c.SGT_BZ,c.TBJ,c.TBJ_SJ,c.TBJ_BZ,c.CS,c.CS_SJ,c.CS_BZ,c.JLDW jh_jldw,c.JLDW_SJ,c.JLDW_BZ,c.SGDW jh_sgdw," +
					"c.SGDW_SJ,c.SGDW_BZ,c.ZC,c.ZC_SJ,c.ZC_BZ,c.PQ,c.PQ_SJ,c.PQ_BZ,c.JG,c.JG_SJ,c.JG_BZ,c.XMSX,c.YWLX,c.SJBH,c.BZ,c.LRR,c.LRSJ,c.LRBM," +
					"c.LRBMMC,c.GXR,c.GXSJ,c.GXBM,c.GXBMMC,c.SJMJ,c.SFYX,c.XFLX,c.ISXF,B.GC_TCJH_XMXDK_ID,B.XMGLGS,B.FZR_GLGS,decode(c.bdid,'',b.xmdz,d.bddd) xmdz,B.xmlx,B.YZDB,B.SGDW," +
					"B.FZR_SGDW,B.JLDW,B.FZR_JLDW,B.SJDW,B.FZR_SJDW,B.JSNR,B.WGRQ,B.LXFS_GLGS,B.LXFS_SGDW,B.LXFS_JLDW,B.LXFS_SJDW,B.LXFS_YZDB ,B.Isbt F" +
					" from gc_ztb_jhsj a ,gc_jh_sj c,gc_tcjh_xmxdk b ,gc_xmbd d ";
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			
			/*bs.setFieldDic("ZBLX", "ZBLX");//招标类型
			bs.setFieldDic("ZBFS", "ZBFS");//招标方式
			bs.setFieldThousand("YSE");//预算额
            */	
			bs.setFieldDic("ISXF", "SF");
			bs.setFieldDic("ISBT", "SF");
			bs.setFieldDic("XMLX", "XMLX");
			bs.setFieldDic("XMSX", "XMSX");
			bs.setFieldDic("XMGLGS", "XMGLGS");
			domresult = bs.getJson();
		
		} catch (Exception e) {
			e.printStackTrace(System.out);
		
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}

	@Override
	public String updateZhaobiaoxuqiu(HttpServletRequest request, String json,User user,String jhsjids) throws Exception {
		Connection conn = DBUtil.getConnection();
		GcZtbXqVO xmvo = new GcZtbXqVO();
		try {
			conn.setAutoCommit(false);
		    JSONArray list = xmvo.doInitJson(json);
		    xmvo.setValueFromJson((JSONObject)list.get(0));
			//设置公共字段
			BusinessUtil.setUpdateCommonFields(xmvo, user);
	     	xmvo.setYwlx(ywlx);//业务类型
			//EventVO eventVO = EventManager.createEvent(conn, xmvo.getYwlx(), user);//生成事件
			//xmvo.setSjbh(eventVO.getSjbh());
			//修改 
		    //add by cbl 审批用
			xmvo.setConnection(conn);
		    //add by cbl
			BaseDAO.update(conn, xmvo);
			//维护附件表FileUploadVO start
			FileUploadVO fuv = new FileUploadVO();
			fuv.setYwlx(xmvo.getYwlx());
			fuv.setSjbh(xmvo.getSjbh());
			fuv.setFjzt("1");
			FileUploadService.updateVOByYwid(conn, fuv, xmvo.getGc_ztb_xq_id());
			//维护附件表FileUploadVO end
			if(!Pub.empty(jhsjids))
			{
				//临时处理补录数据 hongpeng.dong at 2014.10.24
				String zbid = "";
				String tmpSql = "select ztbsjid from GC_ZTB_XQSJ_YS where ztbxqid = '" + xmvo.getGc_ztb_xq_id() + "'";
				String [][] result = DBUtil.query(conn, tmpSql);
				if(null != result && result.length > 0){
					zbid = result[0][0];
				}
				
				//删除中间表
				String sql1="delete from gc_ztb_jhsj where  xqid='"+xmvo.getGc_ztb_xq_id()+"' ";
				DBUtil.execSql(conn, sql1);
				//增加中间表
				String[] jhsjid= jhsjids.split(",");
				for(int i=0;i<jhsjid.length;i++){
					GcZtbJhsjVO jhsjvo = new GcZtbJhsjVO();
					String tcjhSql = "select SJWYBH from GC_JH_SJ where GC_JH_SJ_ID='"
							+ jhsjid[i] + "'";
					String arr[][] = DBUtil.query(conn, tcjhSql);
					if (arr != null && arr.length != 0) {
						jhsjvo.setSjwybh(arr[0][0]);// 保存数据唯一编号
					}
					jhsjvo.setJhsjid(jhsjid[i]);
					jhsjvo.setGc_ztb_jhsj_id(new RandomGUID().toString()); // 主键
					jhsjvo.setXqid(xmvo.getGc_ztb_xq_id());
					BusinessUtil.setUpdateCommonFields(jhsjvo, user);// 公共字段更新
					jhsjvo.setSjbh(xmvo.getSjbh());
					jhsjvo.setYwlx(ywlx);// 业务类型
					// EventVO eventVO1 = EventManager.createEvent(conn,
					// jhsjvo.getYwlx(), user);//生成事件
					// jhsjvo.setSjbh(eventVO1.getSjbh());
					// 插入
					
					//临时处理补录数据 hongpeng.dong at 2014.10.24
					jhsjvo.setZbid(zbid);
					
					BaseDAO.insert(conn, jhsjvo);
				}
			}
			conn.commit();
			LogManager.writeUserLog(xmvo.getSjbh(), xmvo.getYwlx(),
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "招标需求修改成功", user,"","");
	
			} catch (Exception e) {
				DBUtil.rollbackConnetion(conn);
				e.printStackTrace(System.out);
			} finally {
				DBUtil.closeConnetion(conn);
			}
		String jsona="{querycondition: {conditions: [{\"value\":\""+xmvo.getGc_ztb_xq_id()+"\",\"fieldname\":\"gc_ztb_xq_id\",\"operation\":\"=\",\"logic\":\"and\"} ]}}";
		String resultXinXi=this.queryConditionZhaobiaoxuqiu(request,jsona);
		return resultXinXi;
	}
	@Override
	public String queryMoreXiangMu(String json,User user) throws Exception {
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		try {
      
			PageManager page = RequestUtil.getPageManager(json);
			String orderFilter = RequestUtil.getOrderFilter(json);
			String condition = RequestUtil.getConditionList(json).getConditionWhere();
		    condition += BusinessUtil.getSJYXCondition("T") + BusinessUtil.getCommonCondition(user,null);
			if(!Pub.empty(condition)){
				condition +="  AND t.xmid = t2.GC_TCJH_XMXDK_ID and t.bdid=t3.gc_xmbd_id(+) ";
				
			}else{
				condition="   AND t.xmid = t2.GC_TCJH_XMXDK_ID and t.bdid=t3.gc_xmbd_id(+) ";
			}
			condition += orderFilter;
		    page.setFilter(condition);
			conn.setAutoCommit(false);
			String sql = "SELECT t.GC_JH_SJ_ID,t.JHID,t.xmbs,t.ND,t.XMID,t.BDID,t.XMBH,t.BDBH,t.XMMC,t.BDMC,t.XMXZ,t.PXH,t.KGSJ,t.KGSJ_SJ,t.KGSJ_BZ,t.WGSJ,t.WGSJ_SJ,t.WGSJ_BZ,t.KYPF,t.KYPF_SJ,t.KYPF_BZ,t.HPJDS,t.HPJDS_SJ,t.HPJDS_BZ,t.GCXKZ,t.GCXKZ_SJ,t.GCXKZ_BZ,t.SGXK,t.SGXK_SJ,t.SGXK_BZ,t.CBSJPF,t.CBSJPF_SJ,t.CBSJPF_BZ,t.CQT,t.CQT_SJ,t.CQT_BZ,t.PQT,t.PQT_SJ,t.PQT_BZ,t.SGT,t.SGT_SJ,t.SGT_BZ,t.TBJ,t.TBJ_SJ,t.TBJ_BZ,t.CS,t.CS_SJ,t.CS_BZ,t.JLDW jh_jldw,t.JLDW_SJ,t.JLDW_BZ,t.SGDW jh_sgdw,t.SGDW_SJ,t.SGDW_BZ,t.ZC,t.ZC_SJ,t.ZC_BZ,t.PQ,t.PQ_SJ,t.PQ_BZ,t.JG,t.JG_SJ,t.JG_BZ,t.XMSX,t.YWLX,t.SJBH,t.BZ,t.LRR,t.LRSJ,t.LRBM,t.LRBMMC,t.GXR,t.GXSJ,t.GXBM,t.GXBMMC,t.SJMJ,t.SFYX,t.XFLX,t.ISXF,t2.GC_TCJH_XMXDK_ID,t2.XMGLGS,t2.FZR_GLGS, decode(t.bdid,'',t2.xmdz,t3.bddd) xmdz,t2.xmlx,t2.YZDB,t2.SGDW,t2.FZR_SGDW,t2.JLDW,t2.FZR_JLDW,t2.SJDW,t2.FZR_SJDW,t2.JSNR,t2.WGRQ,t2.LXFS_GLGS,t2.LXFS_SGDW,t2.LXFS_JLDW,t2.LXFS_SJDW,t2.LXFS_YZDB ,t2.Isbt,decode(decode(t.bdid,null,t2.jhztze,t3.gcztfy),null,0,decode(t.bdid,null,t2.jhztze,t3.gcztfy)) je FROM GC_JH_SJ t,GC_TCJH_XMXDK t2,gc_xmbd t3  ";
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldDic("ISXF", "SF");
			bs.setFieldDic("ISBT", "SF");
			bs.setFieldDic("XMLX", "XMLX");
			bs.setFieldDic("XMSX", "XMSX");
			bs.setFieldDic("XMGLGS", "XMGLGS");
			//日期
			bs.setFieldDateFormat("LRSJ", "yyyy-MM-dd");
			domresult = bs.getJson();
		/*	LogManager.writeUserLog(null, ywlx,
					Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "结算管理查询", user,"","");*/
		} catch (Exception e) {
			e.printStackTrace(System.out);
		
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	@Override
	public String queryConditionZhaotoubiao(HttpServletRequest request,String json) throws Exception {
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		String id = request.getParameter("id");
		String cxlx = request.getParameter("cxlx");
		try {
			PageManager page = new PageManager();
			String condition = "";
			String idCond = "";
			if(cxlx=="1" || "1".equals(cxlx)){//传入的是需求表主键
				idCond = " and b.GC_ZTB_XQ_ID='"+id+"'";
			}else{//传入的是数据表主键
				idCond = " and a.GC_ZTB_SJ_ID='"+id+"'";
			}
			String cond = " b.GC_ZTB_XQ_ID = c.ZTBXQID(+) and a.GC_ZTB_SJ_ID(+)=c.ZTBSJID "+idCond;
			condition += cond;
		    condition += BusinessUtil.getSJYXCondition("b") + BusinessUtil.getCommonCondition(user,null);
			
		    page.setFilter(condition);
			conn.setAutoCommit(false);
			String sql = "select a.gc_ztb_sj_id, a.zjgcmc, a.ztbmc, a.zbbh, a.zbfs, a.zbdl, a.kbrq, a.dzfs," +
					" a.zbtzsbh, a.zzbj, a.bjxs, a.dsfjgid, a.xmzj, a.xmqtjlry, a.xmjl, a.xmqtry, a.cslbj, " +
					"a.zbjbzcs, a.xcyqjf, a.ylj, a.yjnlybzj, a.ggfbmt, a.ggfbjsrq, a.ggqk, a.ggfbqsrq, a.zkxs," +
					" a.jhzztzj, a.bzzj, a.zbdlf, a.ywlx, a.sjbh, a.bz, a.lrr, a.lrsj, a.lrbm, a.lrbmmc, a.gxr, " +
					"a.gxsj, a.gxbm, a.gxbmmc, a.sjmj, a.sfyx ,a.ggnr, a.SJFZR,a.ZJDB,a.AQJL,a.JSFZR," +
					"b.TBJFS " +
					"from GC_ZTB_SJ a,GC_ZTB_XQ b,GC_ZTB_XQSJ_YS c";
			BaseResultSet bs = DBUtil.query(conn, sql, page);

			bs.setFieldDic("ZBFS", "ZBFS");//招标方式
			bs.setFieldDic("GGQK", "GGQK");//公告情况
			bs.setFieldDic("DZFS", "DZFS");//垫资方式
			bs.setFieldDic("XQZT", "XQZT");//垫资方式
			bs.setFieldThousand("ZZBJ");//总中标价
			bs.setFieldThousand("YLJ");//预留金
			bs.setFieldThousand("YJNLYBZJ");//应缴纳履约保证金
			bs.setFieldThousand("JHZZTZJ");//计划中主体造价
			bs.setFieldTranslater("DSFJGID", "GC_CJDW", "GC_CJDW_ID", "DWMC");
			bs.setFieldTranslater("ZBDL", "GC_CJDW", "GC_CJDW_ID", "DWMC");
			domresult = bs.getJson();
		
		} catch (Exception e) {
			e.printStackTrace(System.out);
		
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}

	@Override
	public String doSubmit(HttpServletRequest request, String s)
			throws Exception {
		Connection conn = DBUtil.getConnection();
		GcZtbXqVO vo = new GcZtbXqVO();
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		String id = request.getParameter("id");
		String resultXinXi = "";
		try {
			conn.setAutoCommit(false);
			vo.setGc_ztb_xq_id(id);
			vo.setXqzt("3");
			vo.setTgbz("1");
			BaseDAO.update(conn, vo);
			String sqlSjbh = "select E.SJBH,E.SJZT from GC_ZTB_XQ X,FS_EVENT E where X.SJBH=E.SJBH and GC_ZTB_XQ_ID='"+id+"'";
			String array[][] = DBUtil.query(conn, sqlSjbh);
			if(array!=null){
				String sjbh = array[0][0];
				String sjzt = array[0][1];
				if(sjzt=="0"||"0".equals(sjzt)){
					//如果未发起审批，那么不需要处理待办
				}else{
					//如果已发起审批，那么要处理待办
					String updateTaskSql = "update AP_TASK_SCHEDULE " +
							"set rwzt='06',result='1',resultdscr='自动通过审批' " +
							"where sjbh='"+sjbh+"' and rwzt='01'";
					String updateProcessSql = "update ap_processinfo set result='1' where eventid='"+sjbh+"'";
					DBUtil.execSql(conn,updateTaskSql);
					DBUtil.execSql(conn,updateProcessSql);
				}
				EventVO evo = new EventVO();
				evo.setSjbh(sjbh);
				evo.setSjzt("2");
				BaseDAO.update(conn, evo);
			}
			conn.commit();
			String jsona = Pub.makeQueryConditionByID(id, "gc_ztb_xq_id");
			resultXinXi = this.queryConditionZhaobiaoxuqiu(request, jsona);
			LogManager.writeUserLog(vo.getSjbh(), vo.getYwlx(),
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "招标需求修改成功", user, "", "");
		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return resultXinXi;
	}

	@Override
	public String deleteZtbxq(HttpServletRequest request, String json)
			throws Exception {
		Connection conn = null;
		String domresult = "";
		GcZtbXqVO vo = new GcZtbXqVO();
		BaseVO[] bv = null;
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		try {
			conn = DBUtil.getConnection();
			conn.setAutoCommit(false);
			JSONArray list = vo.doInitJson(json);
			vo.setValueFromJson((JSONObject)list.get(0));
			//删业务数据
			GcZtbXqVO delVO = new GcZtbXqVO();
			delVO.setGc_ztb_xq_id(vo.getGc_ztb_xq_id());
			delVO.setSfyx("0");
			BaseDAO.update(conn, delVO);
			GcZtbJhsjVO condVO = new GcZtbJhsjVO();
			condVO.setXqid(vo.getGc_ztb_xq_id());
			condVO.setSfyx("1");
			if(condVO!=null&&!condVO.isEmpty()){
				bv = (BaseVO [])BaseDAO.getVOByCondition(conn, condVO);
			}
			if(bv!=null){
				for(int i=0;i<bv.length;i++){
					GcZtbJhsjVO jhvo = (GcZtbJhsjVO)bv[i];
					jhvo.setSfyx("0");
					BaseDAO.update(conn, jhvo);
				}
			}
			//删附件
			FileUploadVO fvo = new FileUploadVO();
			fvo.setYwid(vo.getGc_ztb_xq_id());
			FileUploadService.deleteByConditionVO(conn, fvo);
			LogManager.writeUserLog(vo.getSjbh(), vo.getYwlx(),
					Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "招标需求删除成功", user,"","");
			conn.commit();
		} catch (Exception e) {
			LogManager.writeUserLog(vo.getSjbh(), vo.getYwlx(),
					Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_FAILURE,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "招标需求删除失败", user,"","");
			e.printStackTrace(System.out);
			DBUtil.rollbackConnetion(conn);
			throw e;
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}

	@Override
	public String getFormValueByID(HttpServletRequest request,String json) throws Exception {
		// TODO Auto-generated method stub
		String result = "";
		try{
			String id = request.getParameter("id");
			String jsona=Pub.makeQueryConditionByID(id, "GC_ZTB_XQ_ID");
			result=this.queryConditionZhaobiaoxuqiu(request,jsona);
		}catch(Exception e ){
			e.printStackTrace();
			throw e;
		}
		return result;
	}

	@Override
	public String getCountZbcs(HttpServletRequest request, String json)
			throws Exception {
		String result = "0";
		Connection conn = null;
		try{
			conn = DBUtil.getConnection();
			String id = request.getParameter("xqid");
			String sql="select count(GC_ZTB_XQSJ_YS_ID) from GC_ZTB_XQSJ_YS where ZTBXQID='"+id+"' and SFYX='1'";
			String arr[][] = DBUtil.query(conn, sql);
			if(arr!=null && arr.length>0){
				result = arr[0][0];
			}
		}catch(Exception e ){
			e.printStackTrace();
			throw e;
		}finally{
			DBUtil.closeConnetion(conn);
		}
		return result;
	}
}


