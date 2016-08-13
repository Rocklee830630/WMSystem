package com.ccthanking.business.xmcbk.xmxd;



import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import com.ccthanking.business.xdxmk.vo.DjzVO;
import com.ccthanking.business.xdxmk.vo.XmxdkSpVO;
import com.ccthanking.business.xdxmk.vo.XmxdkVO;
import com.ccthanking.business.xdxmk.vo.ZtVO;
import com.ccthanking.business.xmcbk.vo.NdVO;
import com.ccthanking.business.xmcbk.vo.PcbVO;
import com.ccthanking.business.xmcbk.vo.TcjhSpVO;
import com.ccthanking.business.xmcbk.vo.XmcbkSpVO;
import com.ccthanking.business.xmcbk.vo.XmcbkVO;
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
import com.ccthanking.framework.message.messagemgr.PushMessage;
import com.ccthanking.framework.util.Pub;
import com.ccthanking.framework.util.RandomGUID;
import com.ccthanking.framework.util.RequestUtil;

@Service
public  class XmcbkxdServiceImpl implements XmcbkxdService{
	
	private String ywlx=YwlxManager.GC_XM_CBK_PC;
	//@Autowired
	//private UserManager userMapper;
	//查询
	@Override
	public String query_jzxd(String json,User user,String ids) throws Exception {
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		try {
				PageManager page = RequestUtil.getPageManager(json);
				String orderFilter = RequestUtil.getOrderFilter(json);
				String condition = RequestUtil.getConditionList(json).getConditionWhere();
				condition += " and CBK.GC_TCJH_XMCBK_ID=CJJH.XMID ";
				if(!Pub.empty(ids)&&!"\'".equals(ids))
				{
					int index=ids.lastIndexOf(",\'");
					ids=ids.substring(0, index);
					condition+="    and gc_tcjh_xmcbk_id not in("+ids+")  ";
				} 	
				condition += BusinessUtil.getSJYXCondition(null) + BusinessUtil.getCommonCondition(user,null);
				condition += orderFilter;
				page.setFilter(condition);
				conn.setAutoCommit(false);
				String sql = "select  gc_tcjh_xmcbk_id, xmbh, xmmc,pcid, pxh, qy, nd, xmlx, xmdz, jsnr, jsyy, jsrw, jsbyx, jhztze, gc, zc, qt, xmsx, isbt, pch, xdlx, isxd, ywlx, sjbh, bz, lrr, lrsj, lrbm, lrbmmc, gxr, gxsj, gxbm, gxbmmc, sjmj, sfyx from GC_TCJH_XMCBK CBK,(select XMID from GC_CJJH where SFYX='1') CJJH ";
				BaseResultSet bs = DBUtil.query(conn, sql, page);
				bs.setFieldDic("XMLX", "XMLX");
				bs.setFieldDic("ND","XMNF");
				bs.setFieldDic("XMSX", "XMSX");
				bs.setFieldDic("ISXD","XMZT");
				bs.setFieldDic("ISBT", "SF");
				bs.setFieldDic("SFYX", "SF");
				bs.setFieldThousand("JHZTZE");
				bs.setFieldDic("QY","QY");
				domresult = bs.getJson();
				LogManager.writeUserLog(null, ywlx,
					Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "查询可下达或者结转的项目", user,"","");

		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
		
		} finally {
			if (conn != null)
				
				DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	
	
	/*	
	 * 最大批次号查询
	 * 
	 */
	@Override
	public String query_maxpch(String json,User user,String xdlx,String xdnf) throws Exception {
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		try {		
				conn.setAutoCommit(false);
				//查询批次表中最大批次号
				String sql = "select max(pch) from GC_CBK_PCB where sfyx='1' and xdlx='"+xdlx+"' and to_char(XDRQ,'YYYY')='"+xdnf+"'";
				String result[][] = DBUtil.query(conn, sql);
				if(null!=result&&result.length>0 && !Pub.empty(result[0][0]))
				{	
					//正常下达
					if(xdlx.equals("1"))
					{
						int a = Integer.parseInt(result[0][0]);
						//查询出字典表中正常批次号最大字典值
						String sql_max = "select max(dic_code) from FS_DIC_TREE where parent_id='1000000000039'";
						String result_max[][] = DBUtil.query(conn, sql_max);
						if(null!=result_max&&result_max.length>0 && !Pub.empty(result_max[0][0]))
						{
							//当批次号等于最大字典值
							if(result_max[0][0].equals(result[0][0]))
							{
								domresult = "01";
							}
							else
							{
								if(a>=9)
								{
									domresult=String.valueOf(a+1);
								}
								else
								{
									a=Integer.parseInt(result[0][0].substring(1,2))+1;
									domresult="0"+String.valueOf(a);
								}							
							}	
						}
					}				
					//应急下达
					else
					{
						int a = Integer.parseInt(result[0][0].substring(2,4));
						//查询出字典表中应急批次号最大字典值
						String sql_max = "select max(dic_code) from FS_DIC_TREE where parent_id='1000000000270'";
						String result_max[][] = DBUtil.query(conn, sql_max);
						if(null!=result_max&&result_max.length>0 && !Pub.empty(result_max[0][0]))
						{
							//当批次号等于最大字典值
							if(result_max[0][0].equals(result[0][0]))
							{
								domresult = "追加01";
							}
							else
							{
								if(a>=9)
								{						
									domresult="追加"+String.valueOf(a+1);
								}
								else
								{
									a=Integer.parseInt(result[0][0].substring(2,4))+1;
									domresult="追加0"+String.valueOf(a);
								}															
							}
						}
					}	
				}else{
					if(xdlx.equals("2")){
						domresult = "追加01";
					}else{
						domresult = "01";
					}
				}
		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);	
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	//@Autowired
	//private UserManager userMapper;
	//暂存查询
	@Override
	public String query_zc(String json,User user) throws Exception {
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		try {

			PageManager page = RequestUtil.getPageManager(json);
			String condition = RequestUtil.getConditionList(json).getConditionWhere();
			condition+="  and  xmzj=gc_tcjh_xmcbk_id";
			page.setFilter(condition);
			conn.setAutoCommit(false);
			String sql = "select  gc_cbk_djz_id, xmzj, DCZLX, gc_tcjh_xmcbk_id, cbk.xmbh, cbk.xmmc, pxh, qy, nd, xmlx, xmdz, jsnr, jsyy, jsrw, jsbyx, jhztze, gc, zc, qt, xmsx, isbt, pch, xdlx, isxd, ywlx, sjbh, cbk.bz, lrr, lrsj, lrbm, lrbmmc, gxr, gxsj, gxbm, gxbmmc, sjmj, sfyx from GC_CBK_DJZ djz,gc_tcjh_xmcbk cbk ";
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldDic("XMLX", "XMLX");
			bs.setFieldDic("ND","XMNF");
			bs.setFieldDic("XMSX", "XMSX");
			bs.setFieldDic("ISXD","XMZT");
			bs.setFieldDic("ISBT", "SF");
			bs.setFieldDic("SFYX", "SF");
			bs.setFieldDic("QY","QY");
			bs.setFieldThousand("JHZTZE");
			domresult = bs.getJson();
			LogManager.writeUserLog(null, ywlx,
					Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "查询可下达或者结转的项目", user,"","");

		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
		
		} finally {
			if (conn != null)
				DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	//@Autowired
	//private UserManager userMapper;
	//审批信息查询
	@Override
	public String query_sp_info(String json,User user) throws Exception {
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		try {
			PageManager page = RequestUtil.getPageManager(json);
			String orderFilter = RequestUtil.getOrderFilter(json);
			String condition = RequestUtil.getConditionList(json).getConditionWhere();
			condition += orderFilter;
			page.setFilter(condition);
			conn.setAutoCommit(false);
			String sql = "select GC_TCJH_SP_ID,spmc,spfs,nd, sp.resultdscr,tcjh_sp.sjbh, event.sjzt,isxd,closetime,pch,lrsj,splx,bz from gc_tcjh_sp tcjh_sp left join  " +
					"( select * from (select info.processoid,eventid, info.row_flg ,info.memo, tail.resultdscr,  tail.closetime from( select processoid, memo,eventid,  row_number() over(partition by eventid order by createtime desc) as row_flg from AP_PROCESSINFO )" +
					" info left join ap_processdetail tail on info.processoid = tail.processoid where row_flg=1 ) where rownum=1) sp " +
					" on tcjh_sp.sjbh= sp.eventid left join FS_EVENT event on tcjh_sp.sjbh = event.sjbh";
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldDic("SJZT", "SJZT");
			bs.setFieldDic("ISXD","XMZT");
			//bs.setFieldDic("SPFS","XMZT");
			domresult = bs.getJson();
			LogManager.writeUserLog(null, ywlx,
					Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "查询项目审批信息", user,"","");

		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
		
		} finally {
			if (conn != null)
				DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	
	//@Autowired
	//private UserManager userMapper;
	//审批项目查询
	@Override
	public String query_sp(String json,User user,String spxxid) throws Exception {
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		try {
			PageManager page = RequestUtil.getPageManager(json);
			String orderFilter = RequestUtil.getOrderFilter(json);
			String condition = RequestUtil.getConditionList(json).getConditionWhere();
			condition+="  and  cbk.spxxid='"+spxxid+"'";
			condition += BusinessUtil.getSJYXCondition("cbk") + BusinessUtil.getCommonCondition(user,null);
			//暂时不用前台传入的排序字段，先固定使用录入时间
//			condition += orderFilter;
			condition += " order by cbk.LRSJ desc ";
			if (page == null)
				page = new PageManager();
			page.setFilter(condition);
			conn.setAutoCommit(false);
			String sql = "select gc_tcjh_xmcbk_id, cbk.xmbh, cbk.xmmc, cbk.pxh, qy, cbk.nd, cbk.xmlx, cbk.xmdz, jsnr, jsyy, jsrw, jsbyx, jhztze, gc, zc, qt, cbk.xmsx, isbt, pch, xdlx, isxd, cbk.ywlx,(select spfs from GC_TCJH_SP where gc_tcjh_sp_id=cbk.spxxid)as spfs, " +
					"cbk.sjbh,  cbk.pcid ,sjzt,CBK.XMBM,cj.xmsx sjxmsx from GC_TCJH_XMCBK_SP sp " +
					"left join gc_tcjh_xmcbk cbk on sp.xmcbkid = cbk.gc_tcjh_xmcbk_id left join fs_event  event on sp.spsjbh=event.sjbh  left join gc_cjjh cj on cj.xmid=cbk.gc_tcjh_xmcbk_id ";
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldDic("XMLX", "XMLX");
			bs.setFieldDic("ND","XMNF");
			bs.setFieldDic("XMSX", "XMSX");
			bs.setFieldDic("ISXD","XMZT");
			bs.setFieldDic("ISBT", "SF");
			bs.setFieldDic("SFYX", "SF");
			bs.setFieldDic("QY","QY");
			bs.setFieldThousand("JHZTZE");
			domresult = bs.getJson();
			LogManager.writeUserLog(null, ywlx,
					Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "查询可审批或者已审批的项目", user,"","");

		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
		
		} finally {
			if (conn != null)
				DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	
	/*	
	 * 审批信息插入
	 * 
	 */
	@Override
	public String insert_sp_info(String json,User user) throws Exception {
		Connection conn = DBUtil.getConnection();
		String resultVO = null;
		TcjhSpVO spInfovo = new TcjhSpVO();
		try {	
				conn.setAutoCommit(false);					
				JSONArray list = spInfovo.doInitJson(json);
				spInfovo.setValueFromJson((JSONObject)list.get(0));
				spInfovo.setGc_tcjh_sp_id(new RandomGUID().toString());
				spInfovo.setYwlx(YwlxManager.GC_CBK_SP);
				EventVO eventpcVO = EventManager.createEvent(conn, spInfovo.getYwlx(), user);//生成事件
				spInfovo.setSjbh(eventpcVO.getSjbh());
				BusinessUtil.setInsertCommonFields(spInfovo,user);
				BaseDAO.insert(conn, spInfovo);
				resultVO = spInfovo.getRowJson();
				conn.commit();
				
				LogManager.writeUserLog(null, null,
				Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
				user.getOrgDept().getDeptName() + " " + user.getName()
						+ "新增审批成功", user,"","");
		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
			LogManager.writeUserLog(null, null,
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_FAILURE,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "新增审批失败", user,"","");
		} finally {
			if (conn != null) {
				DBUtil.closeConnetion(conn);
			}
		}
		//return resultVO;
		String jsona="{querycondition: {conditions: [{\"value\":\""+spInfovo.getGc_tcjh_sp_id()+"\",\"fieldname\":\"GC_TCJH_SP_ID\",\"operation\":\"=\",\"logic\":\"and\"} ]}}";
		String resultXinXi=this.query_sp_info(jsona,user);
		return resultXinXi;
	}	
	/*	
	 * 更新审批状态
	 * 
	 */
	@Override
	public String update_spzt(String json,User user,String sjbh) throws Exception {

		String jsona="{querycondition: {conditions: [{\"value\":\""+sjbh+"\",\"fieldname\":\"EVENT.SJBH\",\"operation\":\"=\",\"logic\":\"and\"} ]}}";
		String resultXinXi=this.query_sp_info(jsona,user);
		return resultXinXi;
	}	
	/*	
	 * 审批项目插入
	 * 
	 */
	@Override
	public String insert_sp(String json,User user,String ids,String spxxid) throws Exception {
		Connection conn = DBUtil.getConnection();
		boolean iscf=xmpd(ids);
		if(iscf==true)
		{
			DBUtil.rollbackConnetion(conn);
			//e.printStackTrace(System.out);
			LogManager.writeUserLog(null, null,
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_FAILURE,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "多个用户同时操作一条数据", user,"","");
			DBUtil.closeConnetion(conn);
			return "1";
		}
		String resultVO = null;
		XmcbkVO cbkvo = new XmcbkVO();
		TcjhSpVO infospvo = new TcjhSpVO();
		XmcbkSpVO spvo= new XmcbkSpVO();
		try {	
				conn.setAutoCommit(false);
				infospvo.setGc_tcjh_sp_id(spxxid);
				infospvo = (TcjhSpVO)BaseDAO.getVOByPrimaryKey(conn,infospvo);
				if(ids.length() > 0 && ids.indexOf("\"")>-1)
				{
					ids = ids.replaceAll("\"", "");
				}
				String[] id = ids.split(",");
				
				for(int i = 0; i<id.length;i++)
				{
					String Dsql = "DELETE FROM GC_CBK_DJZ WHERE DCZLX = '1' AND XMZJ = '"+id[i]+"'";
					DBUtil.execSql(conn, Dsql);
					
					cbkvo.setGc_tcjh_xmcbk_id(id[i]);
					cbkvo = (XmcbkVO)BaseDAO.getVOByPrimaryKey(conn,cbkvo);	
					cbkvo.setSpxxid(spxxid);
					BaseDAO.update(conn, cbkvo);
					
					spvo.setGc_tcjh_xmcbk_sp_id(new RandomGUID().toString());
					spvo.setSpxxid(spxxid);
					spvo.setSpsjbh(infospvo.getSjbh());
					spvo.setXmcbkid(id[i]);
				    BaseDAO.insert(conn, spvo);
				}
				resultVO = spvo.getRowJson();
				conn.commit();
				LogManager.writeUserLog(infospvo.getSjbh(), infospvo.getYwlx(),
				Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
				user.getOrgDept().getDeptName() + " " + user.getName()
						+ "插入审批项目成功", user,"","");

		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
			LogManager.writeUserLog(infospvo.getSjbh(), infospvo.getYwlx(),
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_FAILURE,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "插入审批项目失败", user,"","");
		} finally {
			if (conn != null) {
				DBUtil.closeConnetion(conn);
			}
		}
		return resultVO;
	}	

	/*	
	 * 下达插入
	 * 
	 */
	@Override
	public String insert_xd(String json,User user,String ids,HttpServletRequest request) throws Exception {
		Connection conn = DBUtil.getConnection();
		boolean iscf=xmpd(ids);
		if(iscf==true)
		{
			DBUtil.rollbackConnetion(conn);
			//e.printStackTrace(System.out);
			LogManager.writeUserLog(null, null,
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_FAILURE,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "多个用户同时操作一条数据", user,"","");
			DBUtil.closeConnetion(conn);
			return "1";
		}	
		String resultVO = null;
		String ywid=request.getParameter("ywid");
		String spxxid=request.getParameter("spxxid");
		String nd = request.getParameter("nd");
		XmxdkVO xmvo = new XmxdkVO();
		XmcbkVO cbkvo = new XmcbkVO();
		PcbVO pcbvo = new PcbVO();
		TcjhSpVO infospvo=new TcjhSpVO();
		try {	
				conn.setAutoCommit(false);
				JSONArray list = pcbvo.doInitJson(json);
				pcbvo.setValueFromJson((JSONObject)list.get(0));
				pcbvo.setIsxd("1");
				//BaseDAO.update(conn, pcbvo);
				DateFormat format = new SimpleDateFormat("yyyy");
				String xdrq=format.format(pcbvo.getXdrq());
				String flag=ispch(pcbvo.getPch(),xdrq);//判断批次表是否已经有该批次
				if(flag.equals("1"))
				{
					//===================向批次表插入信息=================//		
					if(ywid==null||ywid.equals(""))
					{
						ywid=new RandomGUID().toString();
					}					
					pcbvo.setGc_cbk_pcb_id(ywid);
					pcbvo.setYwlx(ywlx);
					EventVO eventpcVO = EventManager.createEvent(conn, pcbvo.getYwlx(), user);//生成事件
					pcbvo.setSjbh(eventpcVO.getSjbh());
					BusinessUtil.setInsertCommonFields(pcbvo, user);//公共字段插入
					FileUploadService.updateFjztByYwid(conn,ywid);
					//插入
					BaseDAO.insert(conn, pcbvo);
					flag = pcbvo.getGc_cbk_pcb_id();
				}
				else
				{	
					FileUploadVO vo = new FileUploadVO();
					vo.setYwid(flag);
					vo.setFjzt("1");
					//FileUploadService.updateFjztByYwid(conn,flag);
					FileUploadService.updateVOByYwid(conn, vo, ywid,user);
				}	
				
				//===================更新审批信息表信息=================//
				infospvo.setGc_tcjh_sp_id(spxxid);
				infospvo=(TcjhSpVO)BaseDAO.getVOByPrimaryKey(conn,infospvo);
				infospvo.setIsxd("1");
				infospvo.setPch(pcbvo.getPch());
				BaseDAO.update(conn, infospvo);				
				if(ids.length() > 0 && ids.indexOf("\"")>-1)
				{
					ids = ids.replaceAll("\"", "");
				}
				String[] id = ids.split(",");
				for(int i = 0; i<id.length;i++)
				{
				//===================更新储备库信息=================//		
					cbkvo = new XmcbkVO();
					xmvo = new XmxdkVO();
					cbkvo.setGc_tcjh_xmcbk_id(id[i]);
					cbkvo = (XmcbkVO)BaseDAO.getVOByPrimaryKey(conn,cbkvo);
					cbkvo.setIsxd("1");
					cbkvo.setPch(pcbvo.getPch());
					//cbkvo.setXdlx(pcbvo.getXdlx());//储备库的下达类型由储备库定义
					//cbkvo.setXmsx(pcbvo.getXdlx());
					cbkvo.setPcid(flag);
					BusinessUtil.setUpdateCommonFields(cbkvo, user);//公共字段更新
					BaseDAO.update(conn, cbkvo);
				//===================向下达库插入信息=================//		
					xmvo.setGc_tcjh_xmxdk_id(new RandomGUID().toString());
					xmvo.setXmcbk_id(cbkvo.getGc_tcjh_xmcbk_id());
					xmvo.setXmbh(cbkvo.getXmbh());
					xmvo.setXmdz(cbkvo.getXmdz());
					xmvo.setIsnrtj(cbkvo.getIsnrtj());
					xmvo.setXmlx(cbkvo.getXmlx());
					xmvo.setXmmc(cbkvo.getXmmc());
					xmvo.setXmsx(cbkvo.getXmsx());//項目屬性从储备库取   update by xiahongbo
					xmvo.setQy(cbkvo.getQy());
					xmvo.setNd(cbkvo.getNd());
					xmvo.setJsnr(cbkvo.getJsnr());
					xmvo.setJsyy(cbkvo.getJsyy());
					xmvo.setJsrw(cbkvo.getJsrw());
					xmvo.setJhztze(jeswitch(cbkvo.getJhztze()));
					xmvo.setGc(jeswitch(cbkvo.getGc()));
					xmvo.setZc(jeswitch(cbkvo.getZc()));
					xmvo.setQt(jeswitch(cbkvo.getQt()));
					xmvo.setZtztze(jeswitch(cbkvo.getZtztze()));
					xmvo.setZtgc(jeswitch(cbkvo.getZtgc()));
					xmvo.setZtzc(jeswitch(cbkvo.getZtzc()));
					xmvo.setZtqt(jeswitch(cbkvo.getZtqt()));
					xmvo.setIsbt(cbkvo.getIsbt());
					xmvo.setBz(cbkvo.getBz());
					xmvo.setPch(pcbvo.getPch());
					xmvo.setYwlx(cbkvo.getPxh());
					xmvo.setYwlx(YwlxManager.GC_XM);
					xmvo.setPre_xmbm(cbkvo.getXmbm());
					xmvo.setXjxj(getxmxz(cbkvo.getGc_tcjh_xmcbk_id()));
					if(cbkvo.getNdmb()!=null){
						//把储备库年度目标字典的值保存到下达库的“年度建设目标”中
						xmvo.setJsmb(Pub.getDictValueByCode("NDMB", cbkvo.getNdmb()));
					}
					if(xmvo.getXjxj().equals("1"))
					{
						xmvo.setIsjxm("1");
					}
					else
					{
						xmvo.setIsjxm("0");
					}					
					EventVO eventVO = EventManager.createEvent(conn, xmvo.getYwlx(), user);//生成事件
				    xmvo.setSjbh(eventVO.getSjbh());
				    BusinessUtil.setInsertCommonFields(xmvo, user);//公共字段插入
				    BaseDAO.insert(conn, xmvo);
				    //===============================更新城建计划表
				    String sql = "update GC_CJJH set XMSX='"+pcbvo.getPclx()+"' where XMID='"+cbkvo.getGc_tcjh_xmcbk_id()+"' and XMID is not null and SFYX='1'";
				    DBUtil.execSql(conn, sql);
				}
				resultVO = xmvo.getRowJson();
				
				//消息推送
				PushMessage.push(conn, request, PushMessage.XMCBK_XMXD,"计财第"+pcbvo.getPch()+"批项目已下达。",null,pcbvo.getSjbh(),pcbvo.getYwlx(),pcbvo.getGc_cbk_pcb_id());
				conn.commit();
				LogManager.writeUserLog(pcbvo.getSjbh(), pcbvo.getYwlx(),
				Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
				user.getOrgDept().getDeptName() + " " + user.getName()
						+ "下达项目成功", user,"","");

		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
			LogManager.writeUserLog(pcbvo.getSjbh(), pcbvo.getYwlx(),
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_FAILURE,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "下达项目失败", user,"","");
		} finally {
			if (conn != null) {
				DBUtil.closeConnetion(conn);
			}
		}
		return resultVO;
	}	
	/*	
	 * 暂存插入
	 * 
	 */
	@Override
	public String insert_zc(String json,User user,String ids,String dczlx) throws Exception {
		Connection conn = DBUtil.getConnection();
		String resultVO = null;
		DjzVO xmvo = new DjzVO();
		XmcbkVO cbkvo = new XmcbkVO();
		try {	
				conn.setAutoCommit(false);
				String sql = "DELETE FROM GC_CBK_DJZ WHERE DCZLX = '"+dczlx+"'";
				DBUtil.execSql(conn, sql);
				if(ids.length() > 0 && ids.indexOf("\"")>-1)
				{
					ids = ids.replaceAll("\"", "");	
				}
				String[] id = ids.split(",");
				for(int i = 0; i<id.length;i++)
				{
					if("".equals(id[i]))
					{
						conn.commit();
						return null;
					}
					cbkvo = new XmcbkVO();
					cbkvo.setGc_tcjh_xmcbk_id(id[i]);
					cbkvo = (XmcbkVO)BaseDAO.getVOByPrimaryKey(conn,cbkvo);
					BusinessUtil.setUpdateCommonFields(cbkvo, user);//公共字段更新
					BaseDAO.update(conn, cbkvo);					
					xmvo = new DjzVO();
					xmvo.setGc_cbk_djz_id(new RandomGUID().toString());
					xmvo.setXmzj(cbkvo.getGc_tcjh_xmcbk_id());
					xmvo.setXmbh(cbkvo.getXmbh());
					xmvo.setXmmc(cbkvo.getXmmc());
					xmvo.setDczlx(dczlx);
					xmvo.setBz(cbkvo.getBz());
				    BusinessUtil.setInsertCommonFields(xmvo, user);//公共字段插入
				    BaseDAO.insert(conn, xmvo);
				}				
				resultVO = xmvo.getRowJson();
				conn.commit();
				LogManager.writeUserLog(null, null,
				Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
				user.getOrgDept().getDeptName() + " " + user.getName()
						+ "暂存下达项目成功", user,"","");
		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
			LogManager.writeUserLog(null, null,
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_FAILURE,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "暂存下达项目失败", user,"","");
		} finally {
			if (conn != null) {
				DBUtil.closeConnetion(conn);
			}
		}
		return resultVO;
	}	
	/*	
	 * 结转(新)
	 * 
	 */
	@Override
	public String insert_jz(String json,User user,String ids,String year) throws Exception {
		Connection conn = DBUtil.getConnection();
		String resultVO = null;
		XmxdkVO xmvo = new XmxdkVO();
		XmcbkVO cbkvo = new XmcbkVO();
		try {	
				conn.setAutoCommit(false);
				if(ids.length() > 0 && ids.indexOf("\"")>-1)
				{
					ids = ids.replaceAll("\"", "");
				}
				NdVO ndvo= new NdVO();
				String[] id = ids.split(",");
				for(int i = 0; i<id.length;i++)
				{
					String Dsql = "DELETE FROM GC_CBK_DJZ WHERE DCZLX = '2' AND XMZJ = '"+id[i]+"'";
					DBUtil.execSql(conn, Dsql);
					cbkvo = new XmcbkVO();
					cbkvo.setGc_tcjh_xmcbk_id(id[i]);
					cbkvo = (XmcbkVO)BaseDAO.getVOByPrimaryKey(conn,cbkvo);
					cbkvo.setGc_tcjh_xmcbk_id(new RandomGUID().toString());
					cbkvo.setIsxd("0");
					cbkvo.setNd(year);
					String xmbh=year+getnumber("GC_TCJH_XMCBK",3,year).substring(4, 7);
					//String xmbh=year+getLshOnYearMonth("GC_TCJH_XMCBK",3," and nd="+year);
					int bh = Integer.parseInt(xmbh);
					cbkvo.setXmbh(String.valueOf(bh+i));
					BusinessUtil.setUpdateCommonFields(cbkvo, user);//公共字段更新					
					BaseDAO.insert(conn, cbkvo);
				}
				//结转
				
				ndvo.setId(new RandomGUID().toString());
				ndvo.setNd(year);
				BaseDAO.insert(conn, ndvo);
				resultVO = xmvo.getRowJson();
				conn.commit();
				LogManager.writeUserLog(xmvo.getSjbh(), xmvo.getYwlx(),
				Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
				user.getOrgDept().getDeptName() + " " + user.getName()
						+ "结转项目成功", user,"","");

		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
			LogManager.writeUserLog(xmvo.getSjbh(), xmvo.getYwlx(),
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_FAILURE,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "结转项目失败", user,"","");
		} finally {
			if (conn != null) {
				DBUtil.closeConnetion(conn);
			}
		}
		return resultVO;
	}
	
	
	/*	
	 * 审批信息修改
	 * 
	 */
	@Override
	public String update_sp_info(String json,User user) throws Exception {
		Connection conn = DBUtil.getConnection();
		String resultVO = null;
		TcjhSpVO spInfovo = new TcjhSpVO();
		try {	
				conn.setAutoCommit(false);					
				JSONArray list = spInfovo.doInitJson(json);
				spInfovo.setValueFromJson((JSONObject)list.get(0));
				BusinessUtil.setUpdateCommonFields(spInfovo, user);//公共字段更新
				BaseDAO.update(conn, spInfovo);

				resultVO = spInfovo.getRowJson();
				conn.commit();
				LogManager.writeUserLog(null, null,
				Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
				user.getOrgDept().getDeptName() + " " + user.getName()
						+ "修改审批信息成功", user,"","");
		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
			LogManager.writeUserLog(null, null,
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_FAILURE,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "修改审批信息失败", user,"","");
		} finally {
			if (conn != null) {
				DBUtil.closeConnetion(conn);
			}
		}
		String jsona="{querycondition: {conditions: [{\"value\":\""+spInfovo.getGc_tcjh_sp_id()+"\",\"fieldname\":\"Gc_tcjh_sp_id\",\"operation\":\"=\",\"logic\":\"and\"} ]}}";
		String resultXinXi=this.query_sp_info(jsona,user);
		return resultXinXi;
	}	

	
	//删除项目信息
	@Override
	public String delete_sp(String json, User user,String cbkid) throws Exception {
		Connection conn = DBUtil.getConnection();
		String resultVO = null;
		XmcbkVO xmvo = new XmcbkVO(); 
		try {
			String Dsql = "DELETE FROM GC_TCJH_XMCBK_SP WHERE  xmcbkid = '"+cbkid+"'";
			DBUtil.execSql(conn, Dsql);

		xmvo.setGc_tcjh_xmcbk_id(cbkid);
		xmvo.setPcid(null);
		BaseDAO.update(conn,xmvo);
		conn.commit();
		LogManager.writeUserLog(xmvo.getSjbh(), xmvo.getYwlx(),
				Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_SUCCESS,
				user.getOrgDept().getDeptName() + " " + user.getName()
						+ "删除审批项目信息成功", user,"","");

		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
			LogManager.writeUserLog(xmvo.getSjbh(), xmvo.getYwlx(),
					Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_FAILURE,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "删除审批项目信息失败", user,"","");
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return resultVO;
	}
	
	
	//结转生成项目编号
	public static String getnumber(String tablename,int num,String tj){
		String result[][] = DBUtil.query("select max(xmbh) + 1 from "+tablename+"  where  nd="+tj);
		String id=null;
		if(null!=result&&result.length>0&&!Pub.empty(result[0][0]))
		{					
			id = result[0][0];
			int len = id.length();
			if(0 != num && num > len){
				for (int i = 0; i < num - len; i++) {
					id = "0" + id;
				}
			}
		}
		else
		{
			id=tj+"001";
		}
		return id;
	}
	
	
	//确定批次号是否存在
	public static String ispch(String pch,String xdrq){
		String result[][] = DBUtil.query("select GC_CBK_PCB_ID from  GC_CBK_PCB  where sfyx='1' and pch='"+pch+"' and to_char(xdrq,'YYYY')='"+xdrq+"'");
		if(null!=result&&result.length>0)
		{	
			return result[0][0];
		}
		return "1";
	}	
	
	//获取项目性质
	public static String getxmxz(String cbkid){
		String result[][] = DBUtil.query("select xmxz from  GC_CJJH  where sfyx='1' and xmid='"+cbkid+"'");
		if(null!=result&&result.length>0)
		{	
			return result[0][0];
		}
		return null;
	}
	
	//金额转换方法
	public static String jeswitch(String je)
	{
		if(!Pub.empty(je))
		{
			Double d = new Double(2);
			d = Double.parseDouble(je);
			java.text.DecimalFormat   df   =new   java.text.DecimalFormat("#.00");
			String je_new = df.format(d*10000);
			return je_new;
		}				
	
		else
		{
			return null;
		}	
	}

	
	//判断项目是否重复
	public static boolean xmpd(String ids)
	{
		int index=ids.lastIndexOf(",");
		ids=ids.substring(0, index);
		String p=ids.replaceAll(",", "','");
		p="\'"+p+"\'";
		String result[][] = DBUtil.query("select XMCBK_ID from GC_TCJH_XMXDK where XMCBK_ID in ("+p+")");
		if(null!=result&&result.length>0)
		{
			return true;
		}	
		else
		{
			return false;			
		}	
	}
	
	//通过
	@Override
	public String xmSptg(String json, HttpServletRequest request)
			throws Exception {
		// TODO Auto-generated method stub
		Connection conn = DBUtil.getConnection();
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		String id = request.getParameter("id");
		TcjhSpVO tvo = null;
		try {
			conn.setAutoCommit(false);
			if(Pub.empty(id)){
				id = "";
			}
			if(!Pub.empty(id)){
				tvo= new TcjhSpVO();
				tvo.setGc_tcjh_sp_id(id);
				tvo = (TcjhSpVO)BaseDAO.getVOByPrimaryKey(conn,tvo);
				tvo.setBz("未经审批，直接通过");
				tvo.setSpfs("1");
				BusinessUtil.setUpdateCommonFields(tvo,user);//公共方法
				BaseDAO.update(conn, tvo);
			}
			conn.commit();
		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		//return resultVO;
		String jsona="{querycondition: {conditions: [{\"value\":\""+id+"\",\"fieldname\":\"Gc_tcjh_sp_id\",\"operation\":\"=\",\"logic\":\"and\"} ]}}";
		String resultXinXi=this.query_sp_info(jsona,user);
		return resultXinXi;

	}
	//验证项目年度是否统一
		@Override
		public String verificationXmnd(String json, HttpServletRequest request)
				throws Exception {
			Connection conn = DBUtil.getConnection();
			User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
			String resultVO = "false";
			XmcbkVO xvo = null;
			try {
				conn.setAutoCommit(false);
				String ids = request.getParameter("ids");
				if(Pub.empty(ids)){
					ids = "";
				}
				if(!Pub.empty(ids)){
					String oNd = "";
					if(ids.length() > 0 && ids.indexOf("\"")>-1)
					{
						ids = ids.replaceAll("\"", "");
					}
					String[] id = ids.split(",");
					for(int i=0;i<id.length;i++){
						xvo= new XmcbkVO();
						xvo.setGc_tcjh_xmcbk_id(id[i]);
						xvo = (XmcbkVO)BaseDAO.getVOByPrimaryKey(conn,xvo);
						if(i == 0){
							oNd = xvo.getNd();
						}else{
							if(!oNd.equals(xvo.getNd())){
								resultVO = "true";
								return resultVO;
							}
						}
						
					}
					
				}
			} catch (Exception e) {
				DBUtil.rollbackConnetion(conn);
				e.printStackTrace(System.out);
			} finally {
				DBUtil.closeConnetion(conn);
			}
			return resultVO;
		}


		@Override
		public String deleteSp(String json, HttpServletRequest request) {
			Connection conn = null;
			String domresult = "";
			XmcbkSpVO vo = null;
			TcjhSpVO spvo= new TcjhSpVO();
			User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
			try {
				conn = DBUtil.getConnection();
				conn.setAutoCommit(false);
				JSONArray list = spvo.doInitJson(json);
				spvo.setValueFromJson((JSONObject)list.get(0));
				spvo.setGc_tcjh_sp_id(spvo.getGc_tcjh_sp_id());
				/*spvo.setSfyx("0");*/
				BusinessUtil.setUpdateCommonFields(spvo,user);
				BaseDAO.delete(conn, spvo);
				domresult = spvo.getRowJson();
				String sql=" select a.gc_tcjh_xmcbk_sp_id from GC_TCJH_XMCBK_SP a where   a.SPXXID='"+spvo.getGc_tcjh_sp_id()+"' ";
				String cbkspid[][] = DBUtil.query(conn, sql);
				if(null!=cbkspid&&cbkspid.length>0&&cbkspid[0][0]!=null){
					for(int i=0;i<cbkspid.length;i++){
						vo = new XmcbkSpVO();
						vo.setGc_tcjh_xmcbk_sp_id(cbkspid[i][0]);
						/*vo.setSfyx("0");*/
						BusinessUtil.setUpdateCommonFields(vo,user);
						BaseDAO.delete(conn, vo);
					}
				}
				conn.commit();
			} catch (Exception e) {
				DBUtil.rollbackConnetion(conn);
				e.printStackTrace(System.out);
			} finally {
				DBUtil.closeConnetion(conn);
			}
			return domresult;
		}
}


