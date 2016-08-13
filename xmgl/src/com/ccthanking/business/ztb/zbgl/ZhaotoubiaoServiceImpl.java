

package com.ccthanking.business.ztb.zbgl;

import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import com.ccthanking.business.tcjh.jhgl.vo.FkqkVO;
import com.ccthanking.business.tcjh.jhgl.vo.TcjhVO;
import com.ccthanking.business.ztb.zbxq.GcZtbXqVO;
import com.ccthanking.business.ztb.zbxq.GcZtbXqsjYsVO;
import com.ccthanking.common.BusinessUtil;
import com.ccthanking.common.EventManager;
import com.ccthanking.common.YwlxManager;
import com.ccthanking.common.vo.EventVO;
import com.ccthanking.framework.Globals;
import com.ccthanking.framework.base.BaseDAO;
import com.ccthanking.framework.base.BaseVO;
import com.ccthanking.framework.common.BaseResultSet;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.PageManager;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.fileUpload.service.FileUploadService;
import com.ccthanking.framework.fileUpload.vo.FileUploadVO;
import com.ccthanking.framework.log.LogManager;
import com.ccthanking.framework.message.messagemgr.PushMessage;
import com.ccthanking.framework.util.Pub;
import com.ccthanking.framework.util.RandomGUID;
import com.ccthanking.framework.util.RequestUtil;


@Service
public class ZhaotoubiaoServiceImpl implements ZhaotoubiaoService {

	private String ywlx=YwlxManager.GC_ZTB_SJ;

	@Override
	public String queryConditionZhaotoubiao(String json,User user) throws Exception {
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		try {
      
			PageManager page = RequestUtil.getPageManager(json);
			String orderFilter = RequestUtil.getOrderFilter(json);
			String condition = RequestUtil.getConditionList(json).getConditionWhere();
			String cond = " and a.gc_ztb_sj_id = y.ztbsjid " +
					"and y.ztbxqid=x.GC_ZTB_XQ_ID " +
					"and x.gc_ztb_xq_id=b.xqid(+) " +
					"and c.gc_jh_sj_id(+)=b.jhsjid " +
					"and a.gc_ztb_sj_id = HT.ZTBID(+) ";
			//add by zhangbr@ccthanking.com	如果使用项目名称查询条件，那么查询招标数据的时候，要考虑一个需求多次招标的情况
			if(condition!=null && condition.contains("C.XMMC")){
				cond += " and b.ZBID=a.GC_ZTB_SJ_ID ";
			}
			condition += cond;
		    condition += BusinessUtil.getSJYXCondition("a") + BusinessUtil.getCommonCondition(user,null);
			
			condition += orderFilter;
		    page.setFilter(condition);
			conn.setAutoCommit(false);
			String sql = "select distinct a.gc_ztb_sj_id, a.zjgcmc, a.xqzt,a.ztbmc, a.zbbh, a.zbfs, a.zbdl, a.kbrq, a.dzfs, " +
					"a.zbtzsbh, a.zzbj, a.bjxs, a.dsfjgid, a.xmzj, a.xmqtjlry, a.xmjl, a.xmqtry, a.cslbj, a.zbjbzcs, " +
					"a.xcyqjf, a.ylj, a.yjnlybzj, a.ggfbmt, a.ggfbjsrq, a.ggqk, a.ggfbqsrq, a.zkxs, a.jhzztzj, a.bzzj, " +
					"a.zbdlf, a.ywlx, a.sjbh, a.bz, a.lrr, a.lrsj, a.lrbm, a.lrbmmc, a.gxr, a.gxsj, a.gxbm, a.gxbmmc, " +
					"a.sjmj, a.sfyx,a.GGNR,a.SJFZR,a.ZJDB,a.AQJL,a.JSFZR,decode(x.ZBLX,'13',XMJL,'12',XMZJ,'11',SJFZR,'') ZBXMFZR,x.ZBLX,X.TBJFS " +
					",HT.HTBM,HT.ID as HTID,HT.HTZT,HT.SJBH as HTSJBH " +
					"from GC_ZTB_SJ a ," +
					"gc_ztb_xqsj_ys y," +
					"GC_ZTB_XQ x ,gc_ztb_jhsj b,gc_jh_sj c,GC_HTGL_HT ht ";
			BaseResultSet bs = DBUtil.query(conn, sql, page);

			bs.setFieldDic("ZBFS", "ZBFS");//招标方式
			bs.setFieldDic("GGQK", "GGQK");//公告情况
			bs.setFieldDic("DZFS", "DZFS");//垫资方式
			bs.setFieldDic("XQZT", "ZBZT");//垫资方式
			bs.setFieldDic("ZBLX", "ZBLX");//招标类型
			bs.setFieldThousand("ZZBJ");//总中标价
			bs.setFieldThousand("YLJ");//预留金
			bs.setFieldThousand("YJNLYBZJ");//应缴纳履约保证金
			bs.setFieldThousand("JHZZTZJ");//计划中主体造价
			bs.setFieldTranslater("DSFJGID", "GC_CJDW", "GC_CJDW_ID", "DWMC");
			bs.setFieldTranslater("ZBDL", "GC_CJDW", "GC_CJDW_ID", "DWMC");
            bs.setFieldDic("HTZT", "HTRXZT");// 合同签订状态
			//
			bs.setFieldSjbh("SJBH");
			bs.setFieldSjbh("HTSJBH");
			domresult = bs.getJson();
		
		} catch (Exception e) {
			e.printStackTrace(System.out);
		
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}

	@Override
	public String insertdemo(String json, User user,String ZTBXQID) throws Exception {
		
		Connection conn = DBUtil.getConnection();
		String resultVO = null;
		GcZtbSjVO ztbsj = new GcZtbSjVO();
		
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
			String[] jhsjid= ZTBXQID.split(",");
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
				 String sql = "update GC_ZTB_XQ set XQZT='5' where GC_ZTB_XQ_ID='"+jhsjid[i]+"'";
				 DBUtil.execSql(conn, sql);
			}
			resultVO = ztbsj.getRowJson();
			conn.commit();
			LogManager.writeUserLog(ztbsj.getSjbh(), ztbsj.getYwlx(),
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "招标需添加成功", user,"","");
	
			} catch (Exception e) {
				DBUtil.rollbackConnetion(conn);
				e.printStackTrace(System.out);
			} finally {
				DBUtil.closeConnetion(conn);
			}
			String jsona="{querycondition: {conditions: [{\"value\":\""+ztbsj.getGc_ztb_sj_id()+"\",\"fieldname\":\"gc_ztb_sj_id\",\"operation\":\"=\",\"logic\":\"and\"} ]}}";
			String resultXinXi=this.queryConditionZhaotoubiao(jsona,user);
			 		
		return resultXinXi;
	}

	
	@Override
	public String updatedemo(String json, User user) throws Exception {
		Connection conn = DBUtil.getConnection();
		String resultVO = null;
		GcZtbSjVO ztbsj = new GcZtbSjVO();
		try {
			conn.setAutoCommit(false);
		    JSONArray list = ztbsj.doInitJson(json);
		    ztbsj.setValueFromJson((JSONObject)list.get(0));
			//设置公共字段
			BusinessUtil.setUpdateCommonFields(ztbsj, user);
			//修改 
			BaseDAO.update(conn, ztbsj);
			resultVO = ztbsj.getRowJson();
			FileUploadVO fvo = new FileUploadVO();
			fvo.setFjzt("1");
//			fvo.setGlid2(ztbsj.getJhsjid());//存入计划数据ID
//			fvo.setGlid3(ztbsj.getXmid());	//存入项目ID
//			fvo.setGlid4(ztbsj.getBdid());	//存入标段ID
			fvo.setSjbh(ztbsj.getSjbh());	//存入时间编号
			fvo.setYwlx(ztbsj.getYwlx());	//存入时间编号
			FileUploadService.updateVOByYwid(conn, fvo, ztbsj.getGc_ztb_sj_id(),user);
			
			String sql = "select a.GC_ZTB_JHSJ_ID,c.ZZBJ from gc_ztb_jhsj a ,gc_ztb_xqsj_ys b ,gc_ztb_sj c " +
					"where c.GC_ZTB_SJ_ID=b.ztbsjid and b.ZTBXQID = a.xqid " +
					"and c.GC_ZTB_SJ_ID='"+ztbsj.getGc_ztb_sj_id()+"' and a.sfyx = '1' and a.ZBID='"+ztbsj.getGc_ztb_sj_id()+"'";
			String[][] t = DBUtil.query(conn, sql);
			if(t!=null&&t.length==1){
				DBUtil.exec(conn, "update gc_ztb_jhsj set ZZBJ='"+t[0][1]+"' where GC_ZTB_JHSJ_ID = '"+t[0][0]+"'");
			}
			conn.commit();
			LogManager.writeUserLog(ztbsj.getSjbh(), ztbsj.getYwlx(),
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "招投标管理修改成功", user,"","");
	
			} catch (Exception e) {
				DBUtil.rollbackConnetion(conn);
				e.printStackTrace(System.out);
			} finally {
				DBUtil.closeConnetion(conn);
			}
		/*String jsona="{querycondition: {conditions: [{\"value\":\""+ztbsj.getGc_ztb_sj_id()+"\",\"fieldname\":\"gc_ztb_sj_id\",\"operation\":\"=\",\"logic\":\"and\"} ]}}";
		String resultXinXi=this.queryConditionZhaotoubiao(jsona,user);*/
		return resultVO;
	}

	@Override
	public String queryConditionZhaobiaoxuqiu(String json, User user)
			throws Exception {
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		try {
      
			PageManager page = RequestUtil.getPageManager(json);
			String orderFilter = RequestUtil.getOrderFilter(json);
			String condition = RequestUtil.getConditionList(json).getConditionWhere();
			condition += " and xqzt = '3' and gc_ztb_xq_id not in (select XQID from GC_ZTB_JHSJ where SFYX='1' group by XQID having sum(decode(ZBID, null, 1, 0)) = 0)";
		    condition += BusinessUtil.getSJYXCondition("") + BusinessUtil.getCommonCondition(user,null);
			
			condition += orderFilter;
		    page.setFilter(condition);
			conn.setAutoCommit(false);
			String sql = "select gc_ztb_xq_id, gzmc, gznr, zzyjyq, sxyq, jsyq, cgmbyq, pbryyq, pbsbyq, tbjfs, zbfs, qtyq, zblx, yse, xqdwjbr, xqdwjbrsj, xqdwfzr, xqdwfzrsj, zbbjbr, zbbfzr, xqzt, ywlx, sjbh, bz, lrr, lrsj, lrbm, lrbmmc, gxr, gxsj, gxbm, gxbmmc, sjmj, sfyx from gc_ztb_xq";
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			
			bs.setFieldDic("ZBLX", "ZBLX");//招标类型
			bs.setFieldDic("ZBFS", "ZBFS");//招标方式
			bs.setFieldDic("XQZT", "SF");//招标方式
			bs.setFieldDic("TBJFS", "TBBJFS");//投标报价方式
			bs.setFieldThousand("YSE");//预算额
			
			domresult = bs.getJson();
		
		} catch (Exception e) {
			e.printStackTrace(System.out);
		
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}

	@Override
	public String queryConditionZhaobiaoxiangmu(String json, User user,String ZTBSJID)
			throws Exception {
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		try {
      
			PageManager page = RequestUtil.getPageManager(json);
			String orderFilter = RequestUtil.getOrderFilter(json);
			String condition = RequestUtil.getConditionList(json).getConditionWhere();
			condition += "  and c.sjwybh=lbj.sjwybh(+) and c.xmid=p.gc_tcjh_xmxdk_id and c.bdid=a.gc_xmbd_id(+)  and c.gc_jh_sj_id=b.jhsjid and d.gc_ztb_xq_id=b.xqid and b.ZBID='"+ZTBSJID+"' ";
		    condition += BusinessUtil.getSJYXCondition("c") + BusinessUtil.getCommonCondition(user,null);
			
			condition += orderFilter;
		    page.setFilter(condition);
			conn.setAutoCommit(false);
			String sql = "select lbj.cssdz CSLBJ,lbj.SBCSZ,lbj.ISXYSCS,lbj.gc_zjb_lbjb_id,c.gc_jh_sj_id,c.xmbh,c.xmid,c.bdid,c.xmmc,c.bdmc,c.bdbh,p.xmlx,p.xmsx,p.isbt,decode(c.bdid, '', p.xmdz, a.bddd) xmdz,d.gzmc from gc_jh_sj c,GC_TCJH_XMXDK p,gc_xmbd a,GC_ZTB_JHSJ b,GC_ZTB_XQ d, (select * from  gc_zjb_lbjb lbj  where lbj.sfyx='1') lbj ";
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldDic("XMLX", "XMLX");
			bs.setFieldDic("XMSX", "XMSX");
			bs.setFieldDic("ISBT", "SF");
			
			
			domresult = bs.getJson();
		
		} catch (Exception e) {
			e.printStackTrace(System.out);
		
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}

	@Override
	public String queryZhongbiaojia(String json, User user, String ZTBSJID)
			throws Exception {
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		try {
      
			PageManager page = RequestUtil.getPageManager(json);
			String orderFilter = RequestUtil.getOrderFilter(json);
			String condition = RequestUtil.getConditionList(json).getConditionWhere();
			if(Pub.empty(ZTBSJID))
			{
				condition += " and  c.xmid = p.gc_tcjh_xmxdk_id and c.GC_JH_SJ_ID = b.jhsjid and d.gc_ztb_xq_id=b.xqid ";
			}else{
				condition += " and  c.xmid = p.gc_tcjh_xmxdk_id and c.GC_JH_SJ_ID = b.jhsjid and d.gc_ztb_xq_id=b.xqid and b.ZBID = '"+ZTBSJID+"' ";
			}
			
		    condition += BusinessUtil.getSJYXCondition("c") + BusinessUtil.getCommonCondition(user,null);
		    condition += BusinessUtil.getSJYXCondition("p") + BusinessUtil.getCommonCondition(user,null);
		    condition += BusinessUtil.getSJYXCondition("b") + BusinessUtil.getCommonCondition(user,null);
		    condition += BusinessUtil.getSJYXCondition("d") + BusinessUtil.getCommonCondition(user,null);
			
			condition += orderFilter;
		    page.setFilter(condition);
			conn.setAutoCommit(false);
			String sql = "select b.GC_ZTB_JHSJ_ID,c.xmbh, c.xmmc, c.bdbh,c.bdmc, p.xmlx, p.xmsx, p.isbt, p.xmdz,b.zzbj,b.bz,d.gzmc " +
					"from gc_jh_sj c, GC_TCJH_XMXDK p,GC_ZTB_JHSJ b,GC_ZTB_XQ d  ";
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			
			bs.setFieldDic("XMLX", "XMLX");
			bs.setFieldDic("XMSX", "XMSX");
			bs.setFieldDic("ISBT", "SF");
			
			
			domresult = bs.getJson();
		
		} catch (Exception e) {
			e.printStackTrace(System.out);
		
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}

	@Override
	public String updateZhongbiaojia(String json, User user)
			throws SQLException, Exception {
		Connection conn = DBUtil.getConnection();
		String resultVO = null;
		GcZtbJhsjVO jhsj = new GcZtbJhsjVO();
		try {
			conn.setAutoCommit(false);
		    JSONArray list = jhsj.doInitJson(json);
		    jhsj.setValueFromJson((JSONObject)list.get(0));
			//设置公共字段
			BusinessUtil.setUpdateCommonFields(jhsj, user);
	     	
			//修改 
			BaseDAO.update(conn, jhsj);

			resultVO = jhsj.getRowJson();
			conn.commit();
			LogManager.writeUserLog(jhsj.getSjbh(), jhsj.getYwlx(),
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "计划数据子中标价修改成功", user,"","");
	
			} catch (Exception e) {
				DBUtil.rollbackConnetion(conn);
				e.printStackTrace(System.out);
			} finally {
				DBUtil.closeConnetion(conn);
			}
		String jsona="{querycondition: {conditions: [{\"value\":\""+jhsj.getGc_ztb_jhsj_id()+"\",\"fieldname\":\"GC_ZTB_JHSJ_ID\",\"operation\":\"=\",\"logic\":\"and\"} ]}}";
		String resultXinXi=this.queryZhongbiaojia(jsona,user,"");
		return resultXinXi;
	}

	@Override
	public String updateZhaotoubiao(String json, User user,String ZTBSJID) throws SQLException,
			Exception {
		/*Connection conn = DBUtil.getConnection();
		
		try {
			conn.setAutoCommit(false);
		    String updatesql = "update GC_ZTB_SJ set XQZT = '2' where GC_ZTB_SJ_ID = '"+ZTBSJID+"'";
	     	DBUtil.exec(conn, updatesql);
			conn.commit();
			//给需求部门发送消息
//			String 
//			PushMessage.push(request, PushMessage.ZTB, "", "", "", YwlxManager.GC_ZTB_SJ, "", "招投标完成");
			//通过ZTBSJID获取需求部门
	     	String getxqbm = "";

	
			} catch (Exception e) {
				DBUtil.rollbackConnetion(conn);
				e.printStackTrace(System.out);
			} finally {
				DBUtil.closeConnetion(conn);
			}
		String jsona="{querycondition: {conditions: [{\"value\":\""+ztbsj.getGc_ztb_sj_id()+"\",\"fieldname\":\"gc_ztb_sj_id\",\"operation\":\"=\",\"logic\":\"and\"} ]}}";
		String resultXinXi=this.queryConditionZhaotoubiao(jsona,user);
		return resultXinXi;*/
		return "";
	}

	@Override
	public String queryZtbsptg(String json, User user) throws Exception {
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		try {
      
			PageManager page = RequestUtil.getPageManager(json);
			String orderFilter = RequestUtil.getOrderFilter(json);
			String condition = RequestUtil.getConditionList(json).getConditionWhere();
			//20150527 去掉招投标签订合同的过滤
			condition += " and a.xqzt = '2'  ";//增加过滤条件
		    condition += BusinessUtil.getSJYXCondition("a") + BusinessUtil.getCommonCondition(user,null);
			
			condition += orderFilter;
		    page.setFilter(condition);
			conn.setAutoCommit(false);
			String sql = "select distinct a.gc_ztb_sj_id, a.zjgcmc, a.ztbmc, a.zbbh, a.zbfs, a.zbdl, a.kbrq, a.dzfs, a.zbtzsbh, a.zzbj, a.bjxs, a.dsfjgid, a.xmzj, a.xmqtjlry, a.xmjl, a.xmqtry, a.cslbj, a.zbjbzcs, a.xcyqjf, a.ylj, a.yjnlybzj, a.ggfbmt, a.ggfbjsrq, a.ggqk, a.ggfbqsrq, a.zkxs, a.jhzztzj, a.bzzj, a.zbdlf, a.ywlx, a.sjbh, a.bz, a.lrr, a.lrsj, a.lrbm, a.lrbmmc, a.gxr, a.gxsj, a.gxbm, a.gxbmmc, a.sjmj, a.sfyx,a.xqzt,a.ggnr,gzq.Zblx "
					+" from GC_ZTB_SJ a left join GC_ZTB_XQSJ_YS gzxs on gzxs.ztbsjid = a.gc_ztb_sj_id "
					+" left join gc_ztb_xq gzq on gzq.gc_ztb_xq_id = gzxs.ztbxqid ";
			BaseResultSet bs = DBUtil.query(conn, sql, page);

			bs.setFieldDic("ZBFS", "ZBFS");//招标方式
			bs.setFieldDic("GGQK", "GGQK");//公告情况
			bs.setFieldDic("DZFS", "DZFS");//垫资方式
			bs.setFieldDic("ZBLX", "ZBLX");//招标类型
			bs.setFieldThousand("ZZBJ");//总中标价
			bs.setFieldThousand("YLJ");//预留金
			bs.setFieldThousand("YJNLYBZJ");//应缴纳履约保证金
			bs.setFieldThousand("JHZZTZJ");//计划中主体造价
			bs.setFieldTranslater("DSFJGID", "GC_CJDW", "GC_CJDW_ID", "DWMC");
			domresult = bs.getJson();
		
		} catch (Exception e) {
			e.printStackTrace(System.out);
		
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}

	@Override
	public String updateZhaotoubiaoZT(String json, User user)throws SQLException, Exception
	{
		Connection conn = DBUtil.getConnection();
		String resultVO = null;
		GcZtbSjVO ztbsj = new GcZtbSjVO();
		try {
			conn.setAutoCommit(false);
		    JSONArray list = ztbsj.doInitJson(json);
		    ztbsj.setValueFromJson((JSONObject)list.get(0));
			//设置公共字段
			BusinessUtil.setUpdateCommonFields(ztbsj, user);
			//修改 
			BaseDAO.update(conn, ztbsj);
			//招标需求修改
			GcZtbXqsjYsVO sjys=new GcZtbXqsjYsVO();
			sjys.setZtbsjid(ztbsj.getGc_ztb_sj_id());
			//----------------------------------------------------------------回写中标次数
			String sql = "select DSFJGID from GC_ZTB_SJ where GC_ZTB_SJ_ID='"+ztbsj.getGc_ztb_sj_id()+"'";
			String[][] arr = DBUtil.query(conn, sql);
			if(arr!=null){
				String cjdwSql = "update GC_CJDW set zbcs = (select ZBCS+1 from GC_CJDW where GC_CJDW_ID='"+arr[0][0]+"') where GC_CJDW_ID='"+arr[0][0]+"'";
				DBUtil.execSql(conn, cjdwSql);
			}
			BaseVO[] vs =(BaseVO[]) BaseDAO.getVOByCondition(conn, sjys);

			String msg = "";//消息提醒字符串
			String zblx = "";
			int serialNum = 0;	//消息提醒，多项目招标时，项目的序号
			for(int i = 0 ;i<vs.length;i++){
				GcZtbXqsjYsVO shys = (GcZtbXqsjYsVO)vs[i];
				GcZtbXqVO xq=new GcZtbXqVO();
				xq.setGc_ztb_xq_id(shys.getZtbxqid());
				GcZtbXqVO xq1=(GcZtbXqVO) BaseDAO.getVOByPrimaryKey(conn, xq);
				String wzbxmSql = "select count(GC_ZTB_JHSJ_ID) from GC_ZTB_JHSJ where XQID='"+shys.getZtbxqid()+"' and ZBID is null";
				String wzbxmArr[][] = DBUtil.query(conn, wzbxmSql);
				if(wzbxmArr!=null && wzbxmArr.length>0){
					if(wzbxmArr[0][0]=="0" || "0".equals(wzbxmArr[0][0])){
						//如果需求下面不存在未招标的项目，那么更新需求的状态
						xq1.setXqzt("6");
						BusinessUtil.setUpdateCommonFields(xq1, user);
						BaseDAO.update(conn, xq1);
					}
				}
				String tcjhSql="";
				// 工作名称【招标需求名称】
				String gzmc = xq1.getGzmc();
				//查询招投标计划数据
				zblx = xq1.getZblx();
				GcZtbJhsjVO jhsjCondVO = new GcZtbJhsjVO();
				jhsjCondVO.setXqid(xq1.getGc_ztb_xq_id());
				jhsjCondVO.setSfyx("1");
				BaseVO[] jhsjBV = BaseDAO.getVOByCondition(conn, jhsjCondVO);
				if(jhsjBV!=null){
					for(int j=0;j<jhsjBV.length;j++){
						GcZtbJhsjVO jhsjVO = (GcZtbJhsjVO)jhsjBV[j];
						//--操作反馈情况表
						FkqkVO fkVO = new FkqkVO();
						fkVO.setGc_jh_fkqk_id(new RandomGUID().toString()); // 主键
	//					fkVO.setJhid(jhsjVO.getJhid());
						fkVO.setJhsjid(jhsjVO.getJhsjid());
						fkVO.setSjbh(jhsjVO.getSjbh());
						fkVO.setFkid(jhsjVO.getGc_ztb_jhsj_id());
						fkVO.setFkrq(Pub.getCurrentDate());
						fkVO.setBz(jhsjVO.getBz());
						if("13".equals(zblx)||zblx=="13"){
							//施工
							fkVO.setYwlx(YwlxManager.GC_ZTB_SJ);
							fkVO.setFklx("1010001");
							fkVO.setZxbz("1");
							BusinessUtil.setInsertCommonFields(fkVO,user);
							String updateZxbzSql = "update GC_JH_FKQK set ZXBZ='0' where JHSJID='"+jhsjVO.getJhsjid()+"' and FKLX='1010001'";
							DBUtil.exec(conn, updateZxbzSql);
							BaseDAO.insert(conn, fkVO);
							//反馈到计划数据表
							TcjhVO jhVO = new TcjhVO();
							jhVO.setGc_jh_sj_id(jhsjVO.getJhsjid());
							jhVO.setSgdw_sj(fkVO.getFkrq());
							BaseDAO.update(conn, jhVO);
							
							TcjhVO tcjhV = (TcjhVO)BaseDAO.getVOByPrimaryKey(conn, jhVO);
							
							// 记录所有施工招标数据中的项目
						//	tcjhSql += "'"+jhsjVO.getJhsjid()+"',";
							if("".equals(msg)) {
								msg += "施工招标已提交到合同。";
							}
							if("0".equals(tcjhV.getXmbs())) {
								msg += (1+serialNum)+"、项目："+tcjhV.getXmmc()+"	";
								serialNum++;
							}
							if("1".equals(tcjhV.getXmbs())) {
								msg += (1+serialNum)+"、项目："+tcjhV.getXmmc()+"	标段："+tcjhV.getBdmc()+"	";
								serialNum++;
							}
						}else if("12".equals(zblx)||zblx=="12"){
							//监理
							fkVO.setYwlx(YwlxManager.GC_ZTB_SJ);
							fkVO.setFklx("1011001");
							fkVO.setZxbz("1");
							BusinessUtil.setInsertCommonFields(fkVO,user);
							String updateZxbzSql = "update GC_JH_FKQK set ZXBZ='0' where JHSJID='"+jhsjVO.getJhsjid()+"' and FKLX='1011001'";
							DBUtil.exec(conn, updateZxbzSql);
							BaseDAO.insert(conn, fkVO);
							//反馈到计划数据表
							TcjhVO jhVO = new TcjhVO();
							jhVO.setGc_jh_sj_id(jhsjVO.getJhsjid());
							jhVO.setJldw_sj(fkVO.getFkrq());
							BaseDAO.update(conn, jhVO);
							
							TcjhVO tcjhV = (TcjhVO)BaseDAO.getVOByPrimaryKey(conn, jhVO);

							// 记录所有监理招标数据中的项目
							tcjhSql += "'"+jhsjVO.getJhsjid()+"',";
							if("".equals(msg)) {
								msg = "监理招标已提交到合同。";
							}

							if("0".equals(tcjhV.getXmbs())) {
								msg += (1+j)+"、项目："+tcjhV.getXmmc()+"	";
							}
							if("1".equals(tcjhV.getXmbs())) {
								msg += (1+j)+"、项目："+tcjhV.getXmmc()+"	标段："+tcjhV.getBdmc()+"	";
							}
						}else if("14".equals(zblx)||zblx=="14"){
							//造价招标回写拦标价表的咨询公司
							String lbjSql = "update GC_ZJB_LBJB set ZXGS='"+arr[0][0]+"' where JHSJID='"+jhsjVO.getJhsjid()+"' and SFYX='1'";
							DBUtil.execSql(conn, lbjSql);
						}
					}
				}
				
/*				// 查询招标中有哪些项目，放在这里可以不用在循环中频繁查询
				tcjhSql = tcjhSql.substring(0, tcjhSql.lastIndexOf(","));
				String jhsql = "select xmmc,xmbs from GC_JH_SJ where GC_JH_SJ_ID in ("+tcjhSql+")";
				String[][] xmmcRs = DBUtil.query(conn, jhsql);
				String xmmcs = "";
				for (int j = 0; j < xmmcRs.length; j++) {
					xmmcs += xmmcRs[j][0] + ",";
				}
				xmmcs = xmmcs.substring(0, xmmcs.lastIndexOf(","));
				msg = msg+"已提交到合同。其中项目包括："+xmmcs;*/
	            // 招标需求提交到合同，发送需求录入人员信息提醒。    add by xiahongbo by 2014-10-14
	            String[] person = new String[1];
				person[0] = xq1.getLrr();
	            PushMessage.push(conn, null, PushMessage.ZBXQ_TJDHT, "招标需求【"+gzmc+"】已提交到合同。", "url", ztbsj.getSjbh(), ztbsj.getYwlx(), "", person, "招标需求已提交到合同");
			}
			// 监理（施工）招标完成后，提醒工程部
			
			if("12".equals(zblx)) {
				PushMessage.push(conn, null, PushMessage.JL_ZBXQ_TJDHT, msg, "url", ztbsj.getSjbh(), ztbsj.getYwlx(), "", "监理招标完成");
			} else if("13".equals(zblx)) {
				PushMessage.push(conn, null, PushMessage.SG_ZBXQ_TJDHT, msg, "url", ztbsj.getSjbh(), ztbsj.getYwlx(), "", "施工招标完成");
			}
			resultVO = ztbsj.getRowJson();
			conn.commit();
			LogManager.writeUserLog(ztbsj.getSjbh(), ztbsj.getYwlx(),
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "招投标管理修改成功", user,"","");
	
			} catch (Exception e) {
				DBUtil.rollbackConnetion(conn);
				e.printStackTrace(System.out);
			} finally {
				DBUtil.closeConnetion(conn);
			}
		String jsona="{querycondition: {conditions: [{\"value\":\""+ztbsj.getGc_ztb_sj_id()+"\",\"fieldname\":\"gc_ztb_sj_id\",\"operation\":\"=\",\"logic\":\"and\"} ]}}";
		String resultXinXi=this.queryConditionZhaotoubiao(jsona,user);
		return resultXinXi;
	}

	@Override
	public String deleteZtbsj(HttpServletRequest request, String json)
			throws SQLException, Exception {
		Connection conn = null;
		String domresult = "";
		GcZtbSjVO vo = new GcZtbSjVO();
		BaseVO[] bv = null;
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		try {
			conn = DBUtil.getConnection();
			conn.setAutoCommit(false);
			JSONArray list = vo.doInitJson(json);
			vo.setValueFromJson((JSONObject)list.get(0));
			//删业务数据
			GcZtbSjVO delVO = new GcZtbSjVO();
			delVO.setGc_ztb_sj_id(vo.getGc_ztb_sj_id());
			delVO.setSfyx("0");
			BaseDAO.update(conn, delVO);
			
			GcZtbXqsjYsVO condVO = new GcZtbXqsjYsVO();
			condVO.setZtbsjid(vo.getGc_ztb_sj_id());
			condVO.setSfyx("1");
			if(condVO!=null&&!condVO.isEmpty()){
				bv = (BaseVO [])BaseDAO.getVOByCondition(conn, condVO);
			}
			if(bv!=null){
				for(int i=0;i<bv.length;i++){
					BaseVO[] jhbv = null;
					//--------------操作需求数据映射表
					GcZtbXqsjYsVO ysvo = (GcZtbXqsjYsVO)bv[i];
					ysvo.setSfyx("0");
					BaseDAO.update(conn, ysvo);
					//--------------操作需求表（还原需求状态）
					GcZtbXqVO xqvo = new GcZtbXqVO();
					xqvo.setGc_ztb_xq_id(ysvo.getZtbxqid());
					xqvo.setXqzt("3");
					BaseDAO.update(conn, xqvo);
					//--------------操作计划数据表（还原子中标价和备注）
					GcZtbJhsjVO jhCondVO = new GcZtbJhsjVO();
//					jhCondVO.setXqid(xqvo.getGc_ztb_xq_id());
					jhCondVO.setZbid(vo.getGc_ztb_sj_id());
					jhCondVO.setSfyx("1");
					if(jhCondVO!=null&&!jhCondVO.isEmpty()){
						jhbv = (BaseVO [])BaseDAO.getVOByCondition(conn, jhCondVO);
					}
					if(jhbv!=null){
						for(int x=0;x<jhbv.length;x++){
							GcZtbJhsjVO jhsjvo =  (GcZtbJhsjVO)jhbv[x];
							jhsjvo.setZbid("");
							jhsjvo.setZzbj("");
							jhsjvo.setBz("");
							BaseDAO.update(conn, jhsjvo);
						}
					}
				}
			}
			//删附件
			FileUploadVO fvo = new FileUploadVO();
			fvo.setYwid(vo.getGc_ztb_sj_id());
			FileUploadService.deleteByConditionVO(conn, fvo);
			LogManager.writeUserLog(vo.getSjbh(), vo.getYwlx(),
					Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "招投标数据删除成功", user,"","");
			conn.commit();
		} catch (Exception e) {
			LogManager.writeUserLog(vo.getSjbh(), vo.getYwlx(),
					Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_FAILURE,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "招投标数据删除失败", user,"","");
			e.printStackTrace(System.out);
			DBUtil.rollbackConnetion(conn);
			throw e;
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}

	@Override
	public String getWhfxmCount(HttpServletRequest request, String msg)
			throws SQLException, Exception {
		Connection conn = null;
		String id = request.getParameter("id");
		String domresult = "";
		try {
			conn = DBUtil.getConnection();
			conn.setAutoCommit(false);
			//查询一下该招标数据的总中标价
			String sqlZzbj = "select ZZBJ from GC_ZTB_SJ where gc_ztb_sj_id='"+id+"'";
			String res[][] = DBUtil.query(conn, sqlZzbj);
			if(res!=null ){
				if(!"".equals(res[0][0])){
					//如果存在总中标价，那么判断项目是否已经划分子中标价
					String sql = "select count(j.gc_ztb_jhsj_id) from gc_ztb_sj s, gc_ztb_xqsj_ys y, gc_ztb_xq x, gc_ztb_jhsj j "+
					 		"where "+
							" s.gc_ztb_sj_id = y.ztbsjid(+)" +
							" and y.ztbxqid = x.gc_ztb_xq_id(+)" +
							" and x.gc_ztb_xq_id = j.xqid(+)" +
							" and y.sfyx='1'" +
							" and x.sfyx='1'" +
							" and j.sfyx='1'" +
							" and s.gc_ztb_sj_id='"+id+"'" +
							" and j.ZBID='"+id+"'" +
							" and j.zzbj is null";
					domresult = DBUtil.query(conn, sql)[0][0];
				}else{
					//如果总中标价的值为空，那么所有子中标价都不需要划分了
					domresult = "0";
				}
			}else{
				//应该不会出现没有数据的情况，暂时不处理
			}
		} catch (Exception e) {
			throw e;
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}

	@Override
	public String queryZxmListWithXqid(HttpServletRequest request, String msg)
			throws SQLException, Exception {
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		String condition = "";
		try {
			PageManager page = new PageManager();
			page.setPageRows(1000);
		    page.setFilter(condition);
			conn.setAutoCommit(false);
			JSONObject obj = JSONObject.fromObject(msg);
			String xqidStr = obj.getString("XQID");
			if(xqidStr!=null){
				if(xqidStr.indexOf(",")!=-1){
					xqidStr = xqidStr.replaceAll(",", "','");
				}
			}else{
				//如果前台传入一个空的需求ID，那么产生一个假的需求ID
				xqidStr = "9999999999999999999999999999999999999999";
			}
			condition += " c.xmid=p.gc_tcjh_xmxdk_id and Z.JHSJID=C.GC_JH_SJ_ID and Z.GC_ZTB_JHSJ_ID in (select b.GC_ZTB_JHSJ_ID from GC_ZTB_JHSJ b where b.xqid in ('"+xqidStr+"') and ZBID is null)";
			
		    page.setFilter(condition);
			conn.setAutoCommit(false);
			String sql = "select c.gc_jh_sj_id,c.xmbh,c.xmid,c.bdid,c.xmmc,c.bdmc,c.bdbh,p.xmlx,p.xmsx,p.isbt,p.xmdz,Z.GC_ZTB_JHSJ_ID " +
					"from gc_jh_sj c,GC_TCJH_XMXDK p,GC_ZTB_JHSJ Z ";
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldDic("XMLX", "XMLX");
			bs.setFieldDic("XMSX", "XMSX");
			bs.setFieldDic("ISBT", "SF");
			domresult = bs.getJson();
		
		} catch (Exception e) {
			e.printStackTrace(System.out);
		
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}

	@Override
	public String queryLanBiaoJia(HttpServletRequest request)
			throws SQLException, Exception {
		Connection conn = DBUtil.getConnection();
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		String ZTBSJID= request.getParameter("ZTBSJID");
		String domresult = "";
		try {
      
			PageManager page = null;
			conn.setAutoCommit(false);
			String sql = "select distinct a.cssdz CSLBJ,A.SBCSZ,A.ISXYSCS,a.gc_zjb_lbjb_id from gc_zjb_lbjb a, GC_ZTB_JHSJ b where a.sjwybh = b.sjwybh and a.sfyx='1' and b.sfyx='1' and b.zbid ='"+ZTBSJID+"'";
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
	public String doCheck(HttpServletRequest request) throws SQLException,
			Exception {
		Connection conn = null;
		String ztbxqid= request.getParameter("ztbxqid");
		String domresult = "";
		try {
			conn = DBUtil.getConnection();
			conn.setAutoCommit(false);
			String arr = ztbxqid.replaceAll(",","','");
			String sql = "select count(*) from GC_ZTB_XQ where GC_ZTB_XQ_ID in ('"+arr+"') and (XQZT='5' or XQZT='6') and SFYX='1'";
			domresult = DBUtil.query(conn, sql)[0][0];
		} catch (Exception e) {
			e.printStackTrace(System.out);
		
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}

	@Override
	public String getxmZJECount(HttpServletRequest request) throws Exception {
		Connection conn = null;
		String id = request.getParameter("id");
		String domresult = "";
		try {
			conn = DBUtil.getConnection();
			conn.setAutoCommit(false);
			//查询一下该招标数据的总中标价
			String sqlZzbj = "select ZZBJ from GC_ZTB_SJ where gc_ztb_sj_id='"+id+"'";
			String res[][] = DBUtil.query(conn, sqlZzbj);
			if(res!=null ){
				if(!"".equals(res[0][0])){
					//如果存在总中标价，那么判断子中标价是否等于总中标价
					String sql = "select sum(j.zzbj) zzbj from gc_ztb_sj s, gc_ztb_xqsj_ys y, gc_ztb_xq x, gc_ztb_jhsj j "+
					 		"where "+
							" s.gc_ztb_sj_id = y.ztbsjid(+)" +
							" and y.ztbxqid = x.gc_ztb_xq_id(+)" +
							" and x.gc_ztb_xq_id = j.xqid(+)" +
							" and y.sfyx='1'" +
							" and x.sfyx='1'" +
							" and j.sfyx='1'" +
							" and s.gc_ztb_sj_id='"+id+"'" +
							" and j.ZBID='"+id+"'";
					domresult = DBUtil.query(conn, sql)[0][0];
				}else{
					//如果总中标价的值为空，那么所有子中标价都不需要划分了
					domresult = "0";
				}
			}else{
				//应该不会出现没有数据的情况，暂时不处理
			}
		} catch (Exception e) {
			throw e;
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
}



