package com.ccthanking.business.xdxmk.service.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.ccthanking.business.bdhf.vo.BdZCVO;
import com.ccthanking.business.bdhf.vo.BdwhVO;
import com.ccthanking.business.cjjh.GcCjjhVO;
import com.ccthanking.business.tcjh.jhgl.vo.BgbbVO;
import com.ccthanking.business.tcjh.jhgl.vo.BgxmVO;
import com.ccthanking.business.tcjh.jhgl.vo.TcjhVO;
import com.ccthanking.business.xdxmk.service.XdxmkService;
import com.ccthanking.business.xdxmk.vo.DjzVO;
import com.ccthanking.business.xdxmk.vo.XmxdkSpVO;
import com.ccthanking.business.xdxmk.vo.XmxdkVO;
import com.ccthanking.business.xdxmk.vo.ZtVO;
import com.ccthanking.business.xmcbk.vo.TcjhSpVO;
import com.ccthanking.business.xmcbk.vo.XmcbkVO;
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
import com.ccthanking.framework.fileUpload.servlet.FileUploadController;
import com.ccthanking.framework.fileUpload.vo.FileUploadVO;
import com.ccthanking.framework.log.LogManager;
import com.ccthanking.framework.message.messagemgr.PushMessage;
import com.ccthanking.framework.model.autocomplete;
import com.ccthanking.framework.util.Pub;
import com.ccthanking.framework.util.QueryConditionList;
import com.ccthanking.framework.util.RandomGUID;
import com.ccthanking.framework.util.RequestUtil;
import com.sun.org.apache.xerces.internal.impl.xpath.regex.ParseException;


@Service
public class XdxmkServiceImpl implements XdxmkService {
	
	private String ywlx = YwlxManager.GC_XM;//业务类型-项目下达库
	
	//查询项目中的合同信息
	private static String QUERY_HTSJ_XMSJ_ZTBSJ = "SELECT distinct( ght.id) as htid,ght.*,ghh.zxhtj,gzs.zzbj" 
			+",gzs.ztbmc,gzs.kbrq,gzs.DSFJGID,gzs.zkxs	,gzs.ZBFS" 
			+",gjs.xmmc,gjs.bdmc,gzx.gc_ztb_xq_id,gzx.zblx,gzx.gzmc" 
			+",(select g1.dwmc from GC_CJDW g1 where g1.gc_cjdw_id=gzs.zbdl) dldw" 
			+",(select g2.dwmc from GC_CJDW g2 where g2.gc_cjdw_id=gzs.dsfjgid) zbdw FROM gc_htgl_ht ght　"
			+"　left join gc_htgl_htsj ghh on ght.id= ghh.htid "
			+"　left join gc_jh_sj gjs on ghh.jhsjid = gjs.gc_jh_sj_id "
			+"　left join  GC_TCJH_XMXDK t on t.gc_tcjh_xmxdk_id = gjs.xmid "
			+"　left join gc_ztb_sj gzs on ght.ztbid = gzs.gc_ztb_sj_id   "
			+"　left join gc_ztb_xqsj_ys gzxs on gzs.GC_ZTB_SJ_ID = gzxs.ztbsjid "
			+"  left join gc_ztb_xq gzx on gzxs.ztbxqid = gzx.gc_ztb_xq_id  "
			+"  left join GC_CJDW gc on gzs.zbdl = gc.gc_cjdw_id ";
	
	@Override
	public String insert(String json, User user,HttpServletRequest request) throws Exception {
		Connection conn = DBUtil.getConnection();
		String resultVO = null;
		XmxdkVO vo = new XmxdkVO();
		XmxdkVO bvo = new XmxdkVO();
		XmcbkVO cbkvo= new XmcbkVO();
		String ids_xj=request.getParameter("ids");
		//String id_xj[]= ids_xj.split(",");
		try {
			conn.setAutoCommit(false);
			JSONArray list = vo.doInitJson(json);
			vo.setValueFromJson((JSONObject)list.get(0));
			JSONObject formObj = (JSONObject)list.get(0);
//			cbkvo.setValueFromJson((JSONObject)list.get(0));//不能从页面生成下达库数据，因为存在同名字段，直接获取太危险了
			if(Pub.empty(vo.getGc_tcjh_xmxdk_id())){
				vo.setGc_tcjh_xmxdk_id(new RandomGUID().toString()); 
				BusinessUtil.setInsertCommonFields(vo,user);
				BaseDAO.insert(conn, vo);
			}else{

				//FileUploadService.updateFjztByYwid(conn, vo.getGc_tcjh_xmxdk_id());//调用修改附件方法
				//========同步计划-项目管理公司=========
				BaseVO[] bv = null;
				TcjhVO condVO = new TcjhVO();
				condVO.setXmid(vo.getGc_tcjh_xmxdk_id());
				condVO.setXmbs("0");
				bv = (BaseVO [])BaseDAO.getVOByCondition(conn, condVO);
				if(bv != null){
					for(int i =0;i<bv.length;i++){
						TcjhVO newvo = (TcjhVO)bv[i];
						newvo.setXmglgs(vo.getXmglgs());//同步项目管理公司
						newvo.setXmxz(vo.getXjxj());//同步项目性质
						BusinessUtil.setUpdateCommonFields(newvo,user);
						BaseDAO.update(conn, newvo);
					}
				}
				//获取表中数据
				bvo=(XmxdkVO)BaseDAO.getVOByPrimaryKey(conn,vo);
				//jiangc开始
				//判断是新建项目还是续建项目
				if(vo.getXjxj().equals("0"))
				{
					if(null!=bvo.getXjxj()&&bvo.getXjxj().equals("1"))
					{//原来是续建项目，现在改成新建项目，清空之前的信息
						/*vo.setXmwybh(null);*/
						vo.setXmglgs(null);
						vo.setFzr_glgs(null);
						vo.setLxfs_glgs(null);
						vo.setSgdw(null);
						vo.setFzr_sgdw(null);
						vo.setLxfs_sgdw(null);
						vo.setJldw(null);
						vo.setFzr_jldw(null);
						vo.setLxfs_jldw(null);
						vo.setYzdb(null);
						vo.setSjdw(null);
						vo.setFzr_sjdw(null);
						vo.setLxfs_sjdw(null);
						vo.setLxfs_yzdb(null);
						vo.setWgrq(null);
						vo.setQgrq(null);
						vo.setJgrq(null);
						vo.setSghtid(null);
						vo.setJlhtid(null);
						vo.setSjhtid(null);
						vo.setSgdwxmjl(null);
						vo.setSgdwxmjllxdh(null);
						vo.setSgdwjsfzr(null);
						vo.setSgdwjsfzrlxdh(null);
						vo.setJldwzj(null);
						vo.setJldwzjlxdh(null);
						vo.setJldwzjdb(null);
						vo.setJldwzjdblxdh(null);
						vo.setJldwaqjl(null);
						vo.setJldwaqjllxdh(null);
						vo.setIsjxm(null);
						//删除标段临时表
						//删除标段
						String sql = "DELETE FROM GC_XMBD_ZC WHERE xmid = '"+vo.getGc_tcjh_xmxdk_id()+"'";
						DBUtil.execSql(conn, sql);
					}
					//如果已经下达了数据唯一标号不变
					if(vo.getIsnatc().equals("0")){
						vo.setXmwybh(new RandomGUID().toString());	
					}
					
				}
				else
				{
					//现在改成续建项目，并且有旧项目
					if(vo.getIsjxm().equals("1"))
					{
						if(null!=bvo.getXjxj()&&bvo.getXjxj().equals("1"))
						{
							//删除标段
							String sql = "DELETE FROM GC_XMBD_ZC WHERE xmid = '"+vo.getGc_tcjh_xmxdk_id()+"'";
							DBUtil.execSql(conn, sql);
						}
						//复制原项目的部分信息到新项目
						XmxdkVO vo2 = new XmxdkVO();
						vo2.setGc_tcjh_xmxdk_id(vo.getXj_xmid());
						vo2 = (XmxdkVO)BaseDAO.getVOByPrimaryKey(conn,vo2);
						vo.setXmwybh(vo2.getXmwybh());
						vo.setXmglgs(vo2.getXmglgs());
						vo.setFzr_glgs(vo2.getFzr_glgs());
						vo.setLxfs_glgs(vo2.getLxfs_glgs());
						vo.setSgdw(vo2.getSgdw());
						vo.setFzr_sgdw(vo2.getFzr_sgdw());
						vo.setLxfs_sgdw(vo2.getLxfs_sgdw());
						vo.setJldw(vo2.getJldw());
						vo.setFzr_jldw(vo2.getFzr_jldw());
						vo.setLxfs_jldw(vo2.getLxfs_jldw());
						vo.setYzdb(vo2.getYzdb());
						vo.setSjdw(vo2.getSjdw());
						vo.setFzr_sjdw(vo2.getFzr_sjdw());
						vo.setLxfs_sjdw(vo2.getLxfs_sjdw());
						vo.setLxfs_yzdb(vo2.getLxfs_yzdb());
						vo.setWgrq(vo2.getWgrq());
						vo.setQgrq(vo2.getQgrq());
						vo.setJgrq(vo2.getJgrq());
						vo.setSghtid(vo2.getSghtid());
						vo.setJlhtid(vo2.getJlhtid());
						vo.setSjhtid(vo2.getSjhtid());
						vo.setSgdwxmjl(vo2.getSgdwxmjl());
						vo.setSgdwxmjllxdh(vo2.getSgdwxmjllxdh());
						vo.setSgdwjsfzr(vo2.getSgdwjsfzr());
						vo.setSgdwjsfzrlxdh(vo2.getSgdwjsfzrlxdh());
						vo.setJldwzj(vo2.getJldwzj());
						vo.setJldwzjlxdh(vo2.getJldwzjlxdh());
						vo.setJldwzjdb(vo2.getJldwzjdb());
						vo.setJldwzjdblxdh(vo2.getJldwzjdblxdh());
						vo.setJldwaqjl(vo2.getJldwaqjl());
						vo.setJldwaqjllxdh(vo2.getJldwaqjllxdh());	
						
						
						//将续建的项目的标段复制到暂存表
						
						
						String ids_all=query_bdid(vo.getXj_xmid());
						//判断续建项目是否有标段
						if(!ids_all.equals(""))
						{	
							
							//删除标段
							String sql = "DELETE FROM GC_XMBD_ZC WHERE xmid = '"+vo.getGc_tcjh_xmxdk_id()+"'";
							DBUtil.execSql(conn, sql);
	
							String id_all[]= ids_all.split(",");
							for(int all=0;all<id_all.length;all++)
							{
								BdZCVO bdzcvo =new BdZCVO();
								BdwhVO bdvo = new BdwhVO();
								bdvo.setGc_xmbd_id(id_all[all]);
								bdvo = (BdwhVO)BaseDAO.getVOByPrimaryKey(conn,bdvo);
								bdzcvo.setGc_xmbd_zc_id(new RandomGUID().toString());
								bdzcvo.setGc_xmbd_id(id_all[all]);
								bdzcvo.setJhid(bdvo.getJhid());
								bdzcvo.setJhsjid(new RandomGUID().toString());
								bdzcvo.setNd(vo.getNd());//项目年度
								bdzcvo.setXmid(vo.getGc_tcjh_xmxdk_id());//项目id
								bdzcvo.setBdbh( bdvo.getBdbh());
								bdzcvo.setBdmc( bdvo.getBdmc());
								bdzcvo.setQdzh( bdvo.getQdzh());
								bdzcvo.setZdzh( bdvo.getZdzh());
								bdzcvo.setCd( bdvo.getCd());
								bdzcvo.setKd( bdvo.getKd());
								bdzcvo.setGj( bdvo.getGj());
								bdzcvo.setWgrq(bdvo.getWgrq());
								bdzcvo.setQgrq(bdvo.getQgrq());
								bdzcvo.setJgrq(bdvo.getJgrq());
								bdzcvo.setGcztfy( bdvo.getGcztfy());
								bdzcvo.setSgdw( bdvo.getSgdw());
								bdzcvo.setSgdwfzr( bdvo.getSgdwfzr());
								bdzcvo.setSgdwfzrlxfs( bdvo.getSgdwfzrlxfs());
								bdzcvo.setJldw( bdvo.getJldw());
								bdzcvo.setJldwfzr( bdvo.getJldwfzr());
								bdzcvo.setJldwfzrlxfs( bdvo.getJldwfzrlxfs());
								bdzcvo.setYwlx( bdvo.getYwlx());
								bdzcvo.setSjbh( bdvo.getSjbh());
								bdzcvo.setBz( bdvo.getBz());
								bdzcvo.setLrr( bdvo.getLrr());
								bdzcvo.setLrsj(bdvo.getLrsj());
								bdzcvo.setLrbm( bdvo.getLrbm());
								bdzcvo.setLrbmmc( bdvo.getLrbmmc());
								bdzcvo.setGxr( bdvo.getGxr());
								bdzcvo.setGxsj(bdvo.getGxsj());
								bdzcvo.setGxbm( bdvo.getGxbm());
								bdzcvo.setGxbmmc( bdvo.getGxbmmc());
								bdzcvo.setSjmj( bdvo.getSjmj());
								bdzcvo.setSfyx( bdvo.getSfyx());
								bdzcvo.setBddd( bdvo.getBddd());
								bdzcvo.setMj( bdvo.getMj());
								bdzcvo.setSghtid( bdvo.getSghtid());
								bdzcvo.setJlhtid( bdvo.getJlhtid());
								bdzcvo.setJsgm( bdvo.getJsgm());	
								bdzcvo.setSjdw( bdvo.getSjdw());
								bdzcvo.setSjdwfzr( bdvo.getSjdwfzr());
								bdzcvo.setSjdwfzrlxfs( bdvo.getSjdwfzrlxfs());	
								bdzcvo.setSgdwxmjl( bdvo.getSgdwxmjl());
								bdzcvo.setSgdwxmjllxdh( bdvo.getSgdwxmjllxdh());
								bdzcvo.setSgdwjsfzr( bdvo.getSgdwjsfzr());
								bdzcvo.setSgdwjsfzrlxdh( bdvo.getSgdwjsfzrlxdh());
								bdzcvo.setJldwzj( bdvo.getJldwzj());
								bdzcvo.setJldwzjlxdh( bdvo.getJldwzjlxdh());
								bdzcvo.setJldwzjdb( bdvo.getJldwzjdb());
								bdzcvo.setJldwzjdblxdh( bdvo.getJldwzjdblxdh());
								bdzcvo.setJldwaqjl( bdvo.getJldwaqjl());
								bdzcvo.setJldwaqjllxdh( bdvo.getJldwaqjllxdh());
								bdzcvo.setJsgm_sj( bdvo.getJsgm_sj());
								bdzcvo.setXmglgs( bdvo.getXmglgs());
								bdzcvo.setBdwybh( bdvo.getBdwybh());
								bdzcvo.setXjglbdid(bdvo.getGc_xmbd_id());
								bdzcvo.setXjxj("1");
								if(ids_xj.indexOf(id_all[all])!=-1)
								{
									bdzcvo.setIsgx("1");
								}
								BaseDAO.insert(conn, bdzcvo);
							}				
						}
					}
					else
					{//现在改成续建项目，并且有没有项目
						/*vo.setXmwybh(null);*/
						vo.setXmglgs(null);
						vo.setFzr_glgs(null);
						vo.setLxfs_glgs(null);
						vo.setSgdw(null);
						vo.setFzr_sgdw(null);
						vo.setLxfs_sgdw(null);
						vo.setJldw(null);
						vo.setFzr_jldw(null);
						vo.setLxfs_jldw(null);
						vo.setYzdb(null);
						vo.setSjdw(null);
						vo.setFzr_sjdw(null);
						vo.setLxfs_sjdw(null);
						vo.setLxfs_yzdb(null);
						vo.setWgrq(null);
						vo.setQgrq(null);
						vo.setJgrq(null);
						vo.setSghtid(null);
						vo.setJlhtid(null);
						vo.setSjhtid(null);
						vo.setSgdwxmjl(null);
						vo.setSgdwxmjllxdh(null);
						vo.setSgdwjsfzr(null);
						vo.setSgdwjsfzrlxdh(null);
						vo.setJldwzj(null);
						vo.setJldwzjlxdh(null);
						vo.setJldwzjdb(null);
						vo.setJldwzjdblxdh(null);
						vo.setJldwaqjl(null);
						vo.setJldwaqjllxdh(null);
						
						//删除标段
						String sql = "DELETE FROM GC_XMBD_ZC WHERE xmid = '"+vo.getGc_tcjh_xmxdk_id()+"'";
						DBUtil.execSql(conn, sql);
						//如果已经下达了数据唯一标号不变
						if(vo.getIsnatc().equals("0")){
							vo.setXmwybh(new RandomGUID().toString());	
						}
					}						
				}
				FileUploadVO fvo = new FileUploadVO();
		        fvo.setFjzt("1");//更新附件状态
		        fvo.setGlid2("");//存入计划数据ID
		        fvo.setGlid3(vo.getGc_tcjh_xmxdk_id()); //存入项目ID
		        fvo.setGlid4(""); //存入标段ID
		        FileUploadService.updateVOByYwid(conn, fvo, vo.getGc_tcjh_xmxdk_id());
				BusinessUtil.setUpdateCommonFields(vo,user);
				vo.setXmbh(vo.getPre_xmbm()+vo.getXdk_xmbm());
				//回写是否按目标完成
				cbkvo.setIsambwc(formObj.getString("ISAMBWC"));
				cbkvo.setGc_tcjh_xmcbk_id(vo.getXmcbk_id());
				BaseDAO.update(conn, cbkvo);
				//回写新建续建
				String cjjhIDSql = "select GC_CJJH_ID from GC_CJJH where XMID='"+vo.getXmcbk_id()+"'";
				String cjjhIDArr[][] = DBUtil.query(conn, cjjhIDSql);
				if(cjjhIDArr!=null && cjjhIDArr.length>0){
					GcCjjhVO cjjhVO = new GcCjjhVO();
					cjjhVO.setGc_cjjh_id(cjjhIDArr[0][0]);
					cjjhVO.setXmxz(formObj.getString("XJXJ"));
					BaseDAO.update(conn, cjjhVO);
				}
				BaseDAO.update(conn, vo);
				Pub.doSynchronousXmbm(conn, vo.getGc_tcjh_xmxdk_id(), "2", vo.getXdk_xmbm());//同步各数据表
				LogManager.writeUserLog(vo.getSjbh(), ywlx,
						Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_SUCCESS,
						user.getOrgDept().getDeptName() + " " + user.getName()
								+ "更新<下达项目库>成功", user,"","");
								//add by cbl start
				/*
				 * 更新业务操作后重新生成项目二维码
				 * @parame conn 
				 * @parame xdkid 下达库ID
				 */
				FileUploadController.doRegetEwm(conn, vo.getGc_tcjh_xmxdk_id());

			}
		
		resultVO = vo.getRowJson();
		conn.commit();
		
		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		String jsona="{querycondition: {conditions: [{\"value\":\""+vo.getGc_tcjh_xmxdk_id()+"\",\"fieldname\":\"gc_tcjh_xmxdk_id\",\"operation\":\"=\",\"logic\":\"and\"} ]}}";
		String resultXinXi=this.query(jsona,user,null,request);
		return resultXinXi;
	}

	@Override
	public String query(String json,User user,String ids, HttpServletRequest request) throws Exception {
		String flag_xj=request.getParameter("flag_xj");
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		try {
			PageManager page = RequestUtil.getPageManager(json);
			String orderFilter = RequestUtil.getOrderFilter(json);
			String condition = RequestUtil.getConditionList(json).getConditionWhere();
	        condition += BusinessUtil.getSJYXCondition("xdk");
	        condition += BusinessUtil.getCommonCondition(user,null);
	        if(!Pub.empty(ids)){
	        	//condition += " AND gc_tcjh_xmxdk_id NOT IN("+ids+") ";
	        	condition += "  AND  gc_tcjh_xmxdk_id NOT IN(SELECT gc_tcjh_xmxdk_sp_id FROM GC_TCJH_XMXDK_SP) ";
	        }
		if (page == null)
			page = new PageManager();
		

			conn.setAutoCommit(false);
			String sql = "SELECT " +
					"xdk.gc_tcjh_xmxdk_id,xdk.sfkybz, xdk.xmbh, xdk.xmmc, xdk.ISJXM,xdk.qy, xdk.nd," +
					" xdk.pch, xdk.xmlx, xdk.xmdz, xdk.jsnr, xdk.jsyy, xdk.jsrw, xdk.jsbyx, xdk.jhztze," +
					" xdk.gc, xdk.zc, xdk.qt,xdk.ztztze, xdk.ztgc, xdk.ztzc, xdk.ztqt, xdk.xmsx, xdk.isbt," +
					" xdk.isnatc, xdk.jszt,   xdk.xmglgs, xdk.fzr_glgs, xdk.lxfs_glgs, xdk.sgdw, xdk.fzr_sgdw," +
					"xdk.lxfs_sgdw, xdk.jldw, xdk.fzr_jldw, xdk.lxfs_jldw,xdk.yzdb, xdk.sjdw," +
					"xdk.fzr_sjdw, xdk.lxfs_sjdw, xdk.lxfs_yzdb, xdk.wgrq, xdk.qgrq, xdk.jgrq," +
					" xdk.ywlx, xdk.sjbh, xdk.bz, xdk.lrr, xdk.lrsj, xdk.lrbm, xdk.lrbmmc, xdk.gxr," +
					" xdk.gxsj, xdk.gxbm, xdk.gxbmmc, xdk.sjmj, xdk.sfyx, xdk.xmcbk_id, xdk.xjxj," +
					" xdk.pxh, xdk.xj_xmid,xdk.wdd,xdk.jsmb,xdk.PRE_XMBM,xdk.XDK_XMBM," +
					" cbk.isambwc  isambwc, cbk.xmfr " +
					" FROM " +
					" GC_TCJH_XMXDK xdk left join  GC_TCJH_XMCBK cbk on xdk.xmcbk_id = cbk.gc_tcjh_xmcbk_id  ";
			//jiangc  start
			//处理选择续建项目不需要查询出当前年度下的项目
			if(null!=flag_xj&&flag_xj.equals("1"))
			{	
				Date date=new Date();//获取时间
				SimpleDateFormat y=new SimpleDateFormat("yyyy");//转换格式
				String year=y.format(date);	
				condition+=" and  xdk.nd <>'"+year+"' and xmbs=0 and zxbz=1 ";
				sql = "SELECT " +
						"xdk.gc_tcjh_xmxdk_id, xdk.xmbh, xdk.ISJXM,xdk.xmmc,xdk2.xmmc as xjxm,xdk.qy, xdk.nd, xdk.pch, xdk.xmlx, xdk.xmdz, xdk.jsnr, xdk.jsyy, xdk.jsrw, xdk.jsbyx, xdk.jhztze, xdk.gc, xdk.zc, xdk.qt,xdk.ztztze, xdk.ztgc, xdk.ztzc, xdk.ztqt, xdk.xmsx, xdk.isbt, xdk.isnatc, xdk.jszt, xdk.xmfr, xdk.xmglgs, xdk.fzr_glgs, xdk.lxfs_glgs, xdk.sgdw, xdk.fzr_sgdw, xdk.lxfs_sgdw, xdk.jldw, xdk.fzr_jldw, xdk.lxfs_jldw, xdk.yzdb, xdk.sjdw, xdk.fzr_sjdw, xdk.lxfs_sjdw, xdk.lxfs_yzdb, xdk.wgrq, xdk.qgrq, xdk.jgrq, xdk.ywlx, xdk.sjbh, xdk.bz, xdk.lrr, xdk.lrsj, xdk.lrbm, xdk.lrbmmc, xdk.gxr, xdk.gxsj, xdk.gxbm, xdk.gxbmmc, xdk.sjmj, xdk.sfyx, xdk.xmcbk_id, xdk.xjxj, xdk.pxh, xdk.xj_xmid,xdk.wdd, xdk.jsmb " +
						"FROM " +
						"GC_TCJH_XMXDK xdk left join gc_jh_sj jhsj on gc_tcjh_xmxdk_id=xmid left join GC_TCJH_XMXDK xdk2 on xdk.gc_tcjh_xmxdk_id=xdk2.xj_xmid ";
			}	
			condition += orderFilter;
			page.setFilter(condition);
			// jiangc end
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			//表选翻译
			//bs.setFieldTranslater("ND", "GC_CBK_ND", "ND", "ND");//年度
			bs.setFieldTranslater("XJ_XMID", "GC_TCJH_XMXDK", "GC_TCJH_XMXDK_ID", "XMMC");//续建关联下达库
			bs.setFieldTranslater("XMGLGS", "FS_ORG_DEPT", "ROW_ID", "BMJC");//项目管理公司
			bs.setFieldTranslater("YZDB", "FS_ORG_PERSON", "ACCOUNT", "NAME");//业主代表
			//字典翻译
			bs.setFieldDic("XMLX", "XMLX");//项目类型
			bs.setFieldDic("PCH","PCH");//批次号
			bs.setFieldDic("QY","QY");//区域
			bs.setFieldDic("XMSX", "XMSX");//项目属性
			bs.setFieldDic("ISNATC","XDZT");//是否下发
			bs.setFieldDic("ISBT", "SF");//是否BT项目
			bs.setFieldDic("XJXJ", "SF");//是否项目
			bs.setFieldDic("SFYX", "SF");//是否有效
			bs.setFieldDic("XJXJ", "XMXZ");//新建/续建
			bs.setFieldDic("WDD", "WDD");//稳定度
			bs.setFieldDic("XMFR", "XMFR");//稳定度
			//金额翻译
			bs.setFieldThousand("JHZTZE");//总投资
			bs.setFieldThousand("GC");//工程
			bs.setFieldThousand("ZC");//征拆
			bs.setFieldThousand("QT");//其他
			domresult = bs.getJson();
			LogManager.writeUserLog(null, ywlx,
					Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "查询<下达项目库>", user,"","");
		}  catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	
	@Override
	public String queryXx(String json,User user, HttpServletRequest request) throws Exception {
		Connection conn = DBUtil.getConnection();
		String spid = request.getParameter("spid");
		String domresult = "";
		try {
			PageManager page = RequestUtil.getPageManager(json);
			String orderFilter = RequestUtil.getOrderFilter(json);
			String condition = RequestUtil.getConditionList(json).getConditionWhere();
	        condition += BusinessUtil.getSJYXCondition("xdk");
	        condition += BusinessUtil.getCommonCondition(user,null);
	        	condition += " AND  gc_tcjh_xmxdk_id IN(SELECT gc_tcjh_xmxdk_sp_id FROM GC_TCJH_XMXDK_SP where tcjhztid = '"+spid+"') ";
	      
		if (page == null)
			page = new PageManager();
		

			conn.setAutoCommit(false);
			String sql = "SELECT " +
					"gc_tcjh_xmxdk_id,sfkybz, xmbh, xmmc, ISJXM,qy, nd, pch, xmlx, xmdz, jsnr, jsyy, jsrw, jsbyx, jhztze, gc, zc, qt,ztztze, ztgc, ztzc, ztqt, xmsx, isbt, isnatc, jszt, xmfr, xmglgs, fzr_glgs, lxfs_glgs, sgdw, fzr_sgdw, lxfs_sgdw, jldw, fzr_jldw, lxfs_jldw, yzdb, sjdw, fzr_sjdw, lxfs_sjdw, lxfs_yzdb, wgrq, qgrq, jgrq, ywlx, sjbh, bz, lrr, lrsj, lrbm, lrbmmc, gxr, gxsj, gxbm, gxbmmc, sjmj, sfyx, xmcbk_id, xjxj, pxh, xj_xmid,wdd,jsmb,PRE_XMBM,XDK_XMBM," +
					"(select cbk.isambwc from GC_TCJH_XMCBK cbk where xdk.xmcbk_id=cbk.gc_tcjh_xmcbk_id) isambwc " +
					"FROM " +
					"GC_TCJH_XMXDK xdk";
			//jiangc  start
			condition += orderFilter;
			page.setFilter(condition);
			// jiangc end
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			//表选翻译
			//bs.setFieldTranslater("ND", "GC_CBK_ND", "ND", "ND");//年度
			bs.setFieldTranslater("XJ_XMID", "GC_TCJH_XMXDK", "GC_TCJH_XMXDK_ID", "XMMC");//续建关联下达库
			bs.setFieldTranslater("XMGLGS", "FS_ORG_DEPT", "ROW_ID", "BMJC");//项目管理公司
			bs.setFieldTranslater("YZDB", "FS_ORG_PERSON", "ACCOUNT", "NAME");//业主代表
			//字典翻译
			bs.setFieldDic("XMLX", "XMLX");//项目类型
			bs.setFieldDic("PCH","PCH");//批次号
			bs.setFieldDic("QY","QY");//区域
			bs.setFieldDic("XMSX", "XMSX");//项目属性
			bs.setFieldDic("ISNATC","XDZT");//是否下发
			bs.setFieldDic("ISBT", "SF");//是否BT项目
			bs.setFieldDic("XJXJ", "SF");//是否项目
			bs.setFieldDic("SFYX", "SF");//是否有效
			bs.setFieldDic("XJXJ", "XMXZ");//新建/续建
			bs.setFieldDic("WDD", "WDD");//稳定度
			//金额翻译
			bs.setFieldThousand("JHZTZE");//总投资
			bs.setFieldThousand("GC");//工程
			bs.setFieldThousand("ZC");//征拆
			bs.setFieldThousand("QT");//其他
			domresult = bs.getJson();
			LogManager.writeUserLog(null, ywlx,
					Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "查询<下达项目库>", user,"","");
		}  catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}

	//jiangc 查询标段
	@Override
	public String query_bd(String json,User user, HttpServletRequest request) throws Exception {
		String xmid=request.getParameter("xmid");
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		try {
			PageManager page = RequestUtil.getPageManager(json);
			String orderFilter = RequestUtil.getOrderFilter(json);
			String condition = RequestUtil.getConditionList(json).getConditionWhere();
			condition +=" and xmid='"+xmid+"'";
			condition += BusinessUtil.getSJYXCondition(null);
			condition += orderFilter;
			page.setFilter(condition);
			conn.setAutoCommit(false);

			String sql = "select gc_xmbd_id, jhid, jhsjid, nd, xmid, bdbh, bdmc, qdzh, zdzh, cd, kd, gj, wgrq, qgrq, jgrq, gcztfy, sgdw, sgdwfzr, sgdwfzrlxfs, jldw, jldwfzr, jldwfzrlxfs, ywlx, sjbh, bz, lrr, lrsj, lrbm, lrbmmc, gxr, gxsj, gxbm, gxbmmc, sjmj, sfyx, bddd, mj, sghtid, jlhtid, jsgm, sjdw, sjdwfzr, sjdwfzrlxfs, sgdwxmjl, sgdwxmjllxdh, sgdwjsfzr, sgdwjsfzrlxdh, jldwzj, jldwzjlxdh, jldwzjdb, jldwzjdblxdh, jldwaqjl, jldwaqjllxdh, jsgm_sj, xmglgs, bdwybh, xjglbdid, xjxj from gc_xmbd ";
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			//字典翻译
			bs.setFieldTranslater("XMGLGS", "FS_ORG_DEPT", "ROW_ID", "BMJC");//项目管理公司
			bs.setFieldDic("ISNOBDXM", "SF");
			bs.setFieldThousand("GCZTFY");
			domresult = bs.getJson();
			LogManager.writeUserLog(null, ywlx,
					Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "查询<标段>", user,"","");
		}  catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}

	//jiangc 查询续建标段
	@Override
	public String query_bdxj(String json,User user, HttpServletRequest request) throws Exception {
		String xmid=request.getParameter("xmid");
		String isxf=request.getParameter("isxf");
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		try {
			PageManager page = RequestUtil.getPageManager(json);
			String orderFilter = RequestUtil.getOrderFilter(json);
			String condition = RequestUtil.getConditionList(json).getConditionWhere();
			condition +=" and xmid='"+xmid+"'";
			condition += BusinessUtil.getSJYXCondition(null);
			condition += orderFilter;
			page.setFilter(condition);
			conn.setAutoCommit(false);
			String sql="";
			if(isxf.equals("1"))
			{
				sql = "select gc_xmbd_id, jhid, jhsjid, nd, xmid, bdbh, bdmc, qdzh, zdzh, cd, kd, gj, wgrq, qgrq, jgrq, gcztfy, sgdw, sgdwfzr, sgdwfzrlxfs, jldw, jldwfzr, jldwfzrlxfs, ywlx, sjbh, bz, lrr, lrsj, lrbm, lrbmmc, gxr, gxsj, gxbm, gxbmmc, sjmj, sfyx, bddd, mj, sghtid, jlhtid, jsgm, sjdw, sjdwfzr, sjdwfzrlxfs, sgdwxmjl, sgdwxmjllxdh, sgdwjsfzr, sgdwjsfzrlxdh, jldwzj, jldwzjlxdh, jldwzjdb, jldwzjdblxdh, jldwaqjl, jldwaqjllxdh, jsgm_sj, xmglgs, bdwybh, xjglbdid, xjxj from gc_xmbd ";
			}
			else
			{
				sql = "select gc_xmbd_zc_id, gc_xmbd_id,jhid, jhsjid, nd, xmid, bdbh, bdmc, qdzh, zdzh, cd, kd, gj, wgrq, qgrq, jgrq, gcztfy, sgdw, sgdwfzr, sgdwfzrlxfs, jldw, jldwfzr, jldwfzrlxfs, ywlx, sjbh, bz, lrr, lrsj, lrbm, lrbmmc, gxr, gxsj, gxbm, gxbmmc, sjmj, sfyx, bddd, mj, sghtid, jlhtid, jsgm, sjdw, sjdwfzr, sjdwfzrlxfs, sgdwxmjl, sgdwxmjllxdh, sgdwjsfzr, sgdwjsfzrlxdh, jldwzj, jldwzjlxdh, jldwzjdb, jldwzjdblxdh, jldwaqjl, jldwaqjllxdh, jsgm_sj, xmglgs, bdwybh, xjglbdid, xjxj,isgx from gc_xmbd_zc ";				
			}	
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			//字典翻译
			bs.setFieldTranslater("XMGLGS", "FS_ORG_DEPT", "ROW_ID", "BMJC");//项目管理公司
			bs.setFieldDic("ISNOBDXM", "SF");
			bs.setFieldThousand("GCZTFY");
			domresult = bs.getJson();
			LogManager.writeUserLog(null, ywlx,
					Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "查询<标段暂存表>", user,"","");
		}  catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	
	
	/*	
	 * 判断是否续建标段
	 * 
	 */
	@Override
	public String query_countbd(String json,User user,HttpServletRequest request) throws Exception {
		Connection conn = DBUtil.getConnection();
		String domresult = "1";
		String xmid=request.getParameter("xmid");
		try {
			 String sql="select count(*) zs ,nvl(sum(decode(ISGX,0,0,1)),'0') as isgx from GC_XMBD_ZC t  where xmid='"+xmid+"'";
			 String result[][] = DBUtil.query(conn, sql);
			 if(null!=result&&result.length>0&& Integer.parseInt(result[0][0])>0)
			{		
				 if(Integer.parseInt(result[0][1])<=0)
				 {
					 domresult ="0";					 
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

	//按项目
	@Override
	public String insertJhAxm(String json,User user,String ids,String ywid) throws Exception {
		Connection conn = DBUtil.getConnection();
		String resultVO = null;
		XmxdkVO xvo = null;//项目下达库VO
		TcjhVO tvo = null;//统筹计划（数据）VO
		EventVO eventVO = null;//生成事件编号VO
		ZtVO vo = new ZtVO();//计划主体VO
		BgbbVO bvo = null;//变更版本VO
		BgxmVO bxmvo = null;//版本项目VO
		Boolean flag = false;//是否为追加计划 true:追加 false:新建
		try {
			conn.setAutoCommit(false);
			//===================保存计划表=======================
			JSONArray list = vo.doInitJson(json);
			vo.setValueFromJson((JSONObject)list.get(0));
			
		    String nd = formatTime(vo.getXdrq());//获取下达年度
		    //根据批次号和年度查询数据，如果存在，不添加计划，将项目追加到该批次下
			String sql = "SELECT GC_JH_ZT_ID FROM GC_JH_ZT WHERE JHPCH = '"+vo.getJhpch()+"' AND ND ='"+nd+"'";
			BaseResultSet bs = DBUtil.query(conn, sql, null);
			ResultSet rs = bs.getResultSet();
			//判断是否有记录
			if(rs.next()){
				String o_jhid = rs.getString("GC_JH_ZT_ID");//记录存在，获取原来的ID
				vo = new ZtVO();
				vo.setGc_jh_zt_id(o_jhid);
				vo = (ZtVO)BaseDAO.getVOByPrimaryKey(conn,vo);
				flag = true;
			}else{
				vo.setGc_jh_zt_id(new RandomGUID().toString());//ID
				vo.setNd(nd);//设置年度为所选日期所在年
			    vo.setYwlx(YwlxManager.GC_JH_ZT);//业务类型
			    vo.setMqbb("0");//目前版本-首次下发
			    if(vo.getXflx().equals("2")){
			    	vo.setXflx("2");//下发类型 -零星
			    	vo.setIsxf("0");//是否下发 -直接下发
			    }else {
			    	vo.setXflx("1");//下发类型-正常
			    }
			    eventVO = EventManager.createEvent(conn, vo.getYwlx(), user);//生成事件
			    vo.setSjbh(eventVO.getSjbh());//事件编号
			    BusinessUtil.setInsertCommonFields(vo,user);//公共字段
				BaseDAO.insert(conn, vo);
				flag = false;
			}
			String jhid = vo.getGc_jh_zt_id();//获取计划ID
			//=====================保存初次下达版本变更记录=====================
			bvo = new BgbbVO();
			if(StringUtils.isBlank(ywid)){
				bvo.setGc_jh_bgbb_id(new RandomGUID().toString());
			}else{
				bvo.setGc_jh_bgbb_id(ywid);
				FileUploadVO fvo = new FileUploadVO();
		        fvo.setFjzt("1");//更新附件状态
		        fvo.setGlid2("");//存入计划数据ID
		        fvo.setGlid3(bvo.getGc_jh_bgbb_id()); //存入项目ID
		        fvo.setGlid4(""); //存入标段ID
		        FileUploadService.updateVOByYwid(conn, fvo, bvo.getGc_jh_bgbb_id());
			}
			int res = 0;
			if(flag){
				String Msql = "select max(BGBBH) from GC_JH_BGBB t where t.jhid ='"+ jhid + "'";
				String[][] str = DBUtil.query(conn, Msql);
				if (str[0][0] != "" && str[0][0].length()>0) {
					res = Integer.parseInt(str[0][0]) + 1;
				}
				bvo.setBgbbh(String.valueOf(res));
				bvo.setBgsm("追加统筹计划");//变更说明
			}else{
				bvo.setBgbbh("0");//初次插入变更号为0
				bvo.setBgsm("首次下达统筹计划");//变更说明
			}
			bvo.setBgrq(new Date());//变更日期
			bvo.setJhid(jhid);//计划ID
			bvo.setYwlx(YwlxManager.GC_JH_BGBB);//业务类型
			eventVO = EventManager.createEvent(conn, bvo.getYwlx(), user);// 生成事件
			bvo.setSjbh(eventVO.getSjbh());//事件编号
			BusinessUtil.setInsertCommonFields(bvo,user);//公共字段
			BaseDAO.insert(conn, bvo);
			String bgbbId = bvo.getGc_jh_bgbb_id();//获取变更版本ID
			//循环取ID
			if(ids.length() > 0 && ids.indexOf("\"")>-1)
			{
				ids = ids.replaceAll("\"", "");
			}
			String[] id = ids.split(",");
			for(int i = 0; i<id.length;i++){
				xvo = new XmxdkVO();
				xvo.setGc_tcjh_xmxdk_id(id[i]);
				xvo = (XmxdkVO)BaseDAO.getVOByPrimaryKey(conn,xvo);
				String xmid = xvo.getGc_tcjh_xmxdk_id();//获取查询返回记录的项目ID
				String xjxj = xvo.getXjxj();//获取查询返回记录的新建/续建
				String pxh = xvo.getPxh();//获取查询返回记录的排序号
				String xmsx = xvo.getXmsx();//获取查询返回记录的项目属性
				xvo.setIsnatc("1");//更改下发状态
				BusinessUtil.setUpdateCommonFields(xvo,user);//公共字段
				BaseDAO.update(conn, xvo);
				//==============根据下达库ID删除待结转数据==================
				String Dsql = "DELETE FROM GC_CBK_DJZ WHERE DCZLX = '3' AND XMZJ = '"+xmid+"'";
				DBUtil.execSql(conn, Dsql);
				
				//===================新增统筹计划数据===================
				tvo = new TcjhVO();
				tvo.setGc_jh_sj_id(new RandomGUID().toString());//统筹计划数据ID
				tvo.setXmid(xmid);//项目ID
				tvo.setXmbh(xvo.getXmbh());//项目编号
				tvo.setXmmc(xvo.getXmmc());//项目名称
				tvo.setNd(xvo.getNd());//年度 
				tvo.setXmsx(xmsx);//项目属性
				tvo.setJhid(jhid);//计划ID
				tvo.setXflx(vo.getXflx());//下发类型
				tvo.setXmxz(xjxj);//项目性质-新建/续建
				
				
				//tvo.setPxh(pxh);//排序号
				tvo.setXmbs("0");//项目标识 -0项目，1标段
				tvo.setYwlx(YwlxManager.GC_JH_SJ);//业务类型
			    eventVO = EventManager.createEvent(conn, tvo.getYwlx(), user);//生成事件
			    tvo.setSjbh(eventVO.getSjbh());//事件编号
			    BusinessUtil.setInsertCommonFields(tvo,user);//公共方法
				BaseDAO.insert(conn, tvo);
				//===================增加版本项目数据===================
				bxmvo = new BgxmVO();
				bxmvo.setGc_jh_bgxm_id(new RandomGUID().toString());//版本项目ID
				bxmvo.setBgid(bgbbId);//变更版本ID
				bxmvo.setJhid(jhid);//计划ID
				bxmvo.setXmid(xmid);//项目ID
				bxmvo.setBdid(tvo.getBdid());//标段ID
				bxmvo.setXmbh(tvo.getXmbh());//项目编号
				bxmvo.setBdbh(tvo.getBdbh());//标段编号
				bxmvo.setXmmc(tvo.getXmmc());//项目名称
				bxmvo.setBdmc(tvo.getBdmc());//标段名称
				bxmvo.setXmxz(xjxj);//项目性质 -新建/续建
				bxmvo.setXmsx(xmsx);//项目属性
				bxmvo.setYwlx(YwlxManager.GC_JH_BGXM);//业务类型
				eventVO = EventManager.createEvent(conn, bxmvo.getYwlx(),user);// 生成事件
				bxmvo.setSjbh(eventVO.getSjbh());//事件编号
				BusinessUtil.setInsertCommonFields(bxmvo,user);//公共方法
				BaseDAO.insert(conn, bxmvo);
				
				}
				
			conn.commit();
			resultVO = xvo.getRowJson();
			LogManager.writeUserLog(xvo.getSjbh(), ywlx,
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "<按项目下达>成功", user,"","");
		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return resultVO;
	}

	@Override
	public String queryById(String json, User user) throws Exception {
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		PageManager page = null;
		try {
			if (page == null)
				page = new PageManager();
			conn.setAutoCommit(false);
			String sql = "SELECT " +
					"t.gc_tcjh_xmxdk_id, t.xmbh, t.xmmc, t.qy, t.nd, t.pch, t.xmlx, t.xmdz, t.jsnr, t.jsyy, t.jsrw, t.jsbyx, t.jhztze, t.gc, t.zc, t.qt,t.ztztze, t.ztgc, t.ztzc, t.ztqt, t.xmsx, t.isbt, t.isnatc, t.jszt, t.xmfr, t.xmglgs, t.fzr_glgs, t.lxfs_glgs, t.sgdw, t.fzr_sgdw, t.lxfs_sgdw, t.jldw, t.fzr_jldw, t.lxfs_jldw, t.yzdb, t.sjdw, t.fzr_sjdw, t.lxfs_sjdw, t.wgrq, t.qgrq, t.jgrq, t.ywlx, t.sjbh, t.bz, t.lrr, t.lrsj, t.lrbm, t.lrbmmc, t.gxr, t.gxsj, t.gxbm, t.gxbmmc, t.sjmj, t.sfyx, t.xmcbk_id, t.xjxj, t.pxh, t.xj_xmid, t.wdd, t.jsmb, t.sghtid, t.jlhtid, t.sjhtid, t.sgdwxmjl, t.sgdwxmjllxdh, t.sgdwjsfzr, t.sgdwjsfzrlxdh, t.jldwzj, t.jldwzjlxdh, t.jldwzjdb, t.jldwzjdblxdh, t.jldwaqjl, t.jldwaqjllxdh, " +
					"(SELECT SJHM FROM FS_ORG_PERSON WHERE ACCOUNT =t.yzdb)AS LXFS_YZDB, "+
					"(SELECT GC_XMBD_ID FROM GC_XMBD WHERE XMID = '"+json+"' AND ROWNUM = 1)AS BDID"+
					" FROM GC_TCJH_XMXDK T" +
					" WHERE T.GC_TCJH_XMXDK_ID = '"+json+"'";
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldDic("XMLX", "XMLX");
			bs.setFieldDic("XMSX", "XMSX");
			bs.setFieldDic("XJXJ", "XMXZ");
			bs.setFieldDic("ISNATC","XMZT");
			bs.setFieldDic("ISBT", "SF");
			bs.setFieldDic("QY","QY");//区域字典
			bs.setFieldDic("SFYX", "SF");
			bs.setFieldTranslater("SGDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");
			bs.setFieldTranslater("JLDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");
			bs.setFieldTranslater("SJDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");
			bs.setFieldTranslater("XJ_XMID", "GC_TCJH_XMXDK", "GC_TCJH_XMXDK_ID", "XMMC");//续建关联下达库
			bs.setFieldTranslater("XMGLGS", "FS_ORG_DEPT", "ROW_ID", "BMJC");//项目管理公司
			bs.setFieldTranslater("YZDB", "FS_ORG_PERSON", "ACCOUNT", "NAME");//业主代表
			bs.setFieldTranslater("FZR_GLGS", "FS_ORG_PERSON", "ACCOUNT", "NAME");//项目管理公司负责人
			domresult = bs.getJson();
			conn.commit();
			LogManager.writeUserLog(null, ywlx,
					Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "查询<项目信息卡>", user,"","");
		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}

	/*
	 * 按计划下达------------------------------------------------
	 * 
	 */
	@Override
	public String insertJhApc(String json, HttpServletRequest request) throws Exception {
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		Connection conn = DBUtil.getConnection();
		String resultVO = null;
		PageManager page = null;
		XmxdkVO xvo = null;//项目下达库VO
		TcjhVO tvo = null;//统筹计划（数据）VO
		EventVO eventVO = null;//生成事件编号VO
		TcjhSpVO spvo = new TcjhSpVO();//审批VO
		ZtVO vo  = new ZtVO();//计划主体VO
		BgbbVO bvo = null;//变更版本VO
		BgxmVO bxmvo = null;//版本项目VO
		Boolean flag = false;//是否为追加计划 true:追加 false:新建
		BdwhVO bdvo =null;//标段表
		BdZCVO bdzcvo =null;//标段暂存表
		try {
			conn.setAutoCommit(false);
			String ywid = request.getParameter("ywid");
			//===================保存计划表=======================
			JSONArray list = vo.doInitJson(json);
			vo.setValueFromJson((JSONObject)list.get(0));
		    //String nd = formatTime(vo.getXdrq());//获取下达年度
			String nd = request.getParameter("nd");
		    //根据批次号和年度查询数据，如果存在，不添加计划，将项目追加到该批次下
			String sqlzt = "SELECT GC_JH_ZT_ID FROM GC_JH_ZT WHERE JHPCH = '"+vo.getJhpch()+"' AND ND ='"+nd+"'";
			BaseResultSet bszt = DBUtil.query(conn, sqlzt, null);
			ResultSet rs1 = bszt.getResultSet();
			//判断是否有记录
			if(rs1.next()){
				String o_jhid = rs1.getString("GC_JH_ZT_ID");//记录存在，获取原来的ID
				vo = new ZtVO();
				vo.setGc_jh_zt_id(o_jhid);
				vo = (ZtVO)BaseDAO.getVOByPrimaryKey(conn,vo);
				flag = true;
			}else{
				if(Pub.empty(ywid)){
					vo.setGc_jh_zt_id(new RandomGUID().toString());//ID
				}else{
					vo.setGc_jh_zt_id(ywid);//ID
				}
				vo.setNd(nd);//设置年度为所选日期所在年
			    vo.setYwlx(YwlxManager.GC_JH_ZT);//业务类型
			    vo.setMqbb("0");//目前版本-首次下发
			    if(vo.getXflx().equals("2")){
			    	vo.setXflx("2");//下发类型 -零星
			    	vo.setIsxf("0");//是否下发 -直接下发
			    }else {
			    	vo.setXflx("1");//下发类型-正常
			    }
			    vo.setSpzt("3");
			    eventVO = EventManager.createEvent(conn, vo.getYwlx(), user);//生成事件
			    vo.setSjbh(eventVO.getSjbh());//事件编号
			    BusinessUtil.setInsertCommonFields(vo,user);//公共字段
				BaseDAO.insert(conn, vo);
				flag = false;
			}
			String spid = request.getParameter("spid");
			if(!Pub.empty(spid)){
				spvo = new TcjhSpVO();
				spvo.setGc_tcjh_sp_id(spid);//下达库ID赋值，用于查询
				spvo = (TcjhSpVO)BaseDAO.getVOByPrimaryKey(conn,spvo);//根据VO对象查询
				spvo.setPch(vo.getJhpch());
				spvo.setIsxd("1");
				BusinessUtil.setUpdateCommonFields(spvo,user);//公共字段
				BaseDAO.update(conn, spvo);
			}
			String jhid = vo.getGc_jh_zt_id();//获取计划ID
			//=====================保存初次下达版本变更记录=====================
			bvo = new BgbbVO();
			
			FileUploadVO fvo = new FileUploadVO();
	        fvo.setFjzt("1");//更新附件状态
	        fvo.setGlid2("");//存入计划数据ID
	        fvo.setGlid3(bvo.getGc_jh_bgbb_id()); //存入项目ID
	        fvo.setGlid4(""); //存入标段ID
	        FileUploadService.updateVOByYwid(conn, fvo, bvo.getGc_jh_bgbb_id(),user);
			int res = 0;
			if(flag){
				bvo.setGc_jh_bgbb_id(jhid);
				bvo = (BgbbVO)BaseDAO.getVOByPrimaryKey(conn,bvo);//根据VO对象查询
				String Msql = "select max(BGBBH) from GC_JH_BGBB t where t.jhid ='"
						+ jhid + "'";
				String[][] str = DBUtil.query(conn, Msql);
				if (str[0][0] != "" && str[0][0].length()>0) {
					res = Integer.parseInt(str[0][0]) + 1;
				}
				
				bvo.setBgbbh(String.valueOf(res));
				bvo.setBgsm("追加统筹计划");//变更说明
				bvo.setBgrq(new Date());//变更日期
				bvo.setJhid(jhid);//计划ID
				bvo.setYwlx(YwlxManager.GC_JH_BGBB);//业务类型
				eventVO = EventManager.createEvent(conn, bvo.getYwlx(), user);// 生成事件
				bvo.setSjbh(eventVO.getSjbh());//事件编号
				BusinessUtil.setInsertCommonFields(bvo,user);//公共字段
				BaseDAO.update(conn, bvo);
			}else{
				bvo.setGc_jh_bgbb_id(jhid);
				bvo.setBgbbh("0");//初次插入变更号为0
				bvo.setBgsm("首次下达统筹计划");//变更说明
				bvo.setBgrq(new Date());//变更日期
				bvo.setJhid(jhid);//计划ID
				bvo.setYwlx(YwlxManager.GC_JH_BGBB);//业务类型
				eventVO = EventManager.createEvent(conn, bvo.getYwlx(), user);// 生成事件
				bvo.setSjbh(eventVO.getSjbh());//事件编号
				BusinessUtil.setInsertCommonFields(bvo,user);//公共字段
				BaseDAO.insert(conn, bvo);
			}
			String bgbbId = bvo.getGc_jh_bgbb_id();//获取变更版本ID
			//==============根据下达库数据进行操作==================
				String sql = "SELECT GC_TCJH_XMXDK_SP_ID,XMBH,XMMC,ND FROM GC_TCJH_XMXDK_SP WHERE TCJHZTID = '"+spid+"'" ;
				BaseResultSet bs = DBUtil.query(conn, sql, page);
				ResultSet rs = bs.getResultSet();
				while(rs.next()){
					//=========根据批次号修改数据========
					xvo = new XmxdkVO();
					xvo.setGc_tcjh_xmxdk_id(rs.getString("GC_TCJH_XMXDK_SP_ID"));//下达库ID赋值，用于查询
					xvo = (XmxdkVO)BaseDAO.getVOByPrimaryKey(conn,xvo);//根据VO对象查询
					String xmid = xvo.getGc_tcjh_xmxdk_id();//获取查询返回记录的项目ID
					String xjxj = xvo.getXjxj();//获取查询返回记录的新建/续建
					String pxh = xvo.getPxh();//获取查询返回记录的排序号
					String xmsx = xvo.getXmsx();//获取查询返回记录的项目属性
					xvo.setIsnatc("1");//更改下发状态
					if(!Pub.empty(xvo.getXjxj())&&xvo.getXjxj().equals("0")&&Pub.empty(xvo.getXmwybh()))
					{
						xvo.setXmwybh(new RandomGUID().toString());
					}	
					
					BusinessUtil.setUpdateCommonFields(xvo,user);//公共字段
					BaseDAO.update(conn, xvo);
					//==============根据下达库ID删除待结转数据==================
					String Dsql = "DELETE FROM GC_CBK_DJZ WHERE DCZLX = '3' AND XMZJ = '"+xmid+"'";
					DBUtil.execSql(conn, Dsql);
					//===================新增统筹计划数据===================
					tvo = new TcjhVO();
					tvo.setGc_jh_sj_id(new RandomGUID().toString());//统筹计划数据ID
					tvo.setXmid(xmid);//项目ID
					tvo.setXmbh(rs.getString("XMBH"));//项目编号
					tvo.setXmmc(rs.getString("XMMC"));//项目名称
					tvo.setNd(rs.getString("ND"));//年度 
					tvo.setXmbs("0");//项目标识 -0项目，1标段
					tvo.setXmsx(xmsx);
					tvo.setXmglgs(xvo.getXmglgs());
					tvo.setIsnrtj(xvo.getIsnrtj());
					// jiangc satrt
					tvo.setSjwybh(xvo.getXmwybh());
					if(xjxj.equals("1"))
					{
						//修改最新数据标识
						DBUtil.execUpdateSql(conn, "update gc_jh_sj set zxbz='0' where  sjwybh='"+xvo.getXmwybh()+"'");
						String id=getid(xvo.getXmwybh(),conn);
						if(!id.equals(""))
						{	
							TcjhVO tvo2 = new TcjhVO();
							tvo2.setGc_jh_sj_id(id);
							tvo2 = (TcjhVO)BaseDAO.getVOByPrimaryKey(conn,tvo2);
							settvo(tvo,tvo2);//复制原项目的时间点
							String ids=query_bdid_xj(xmid);
							if(!ids.equals(""))
							{
								String bdid[]= ids.split(",");
								for(int all=0;all<bdid.length;all++)
								{
									bdvo = new BdwhVO();
									bdzcvo =new BdZCVO();
									bdzcvo.setGc_xmbd_zc_id(bdid[all]);
									bdzcvo = (BdZCVO)BaseDAO.getVOByPrimaryKey(conn,bdzcvo);
									bdvo.setGc_xmbd_id(new RandomGUID().toString());
									bdvo.setJhid(bdzcvo.getJhid());
									bdvo.setJhsjid(bdzcvo.getJhsjid());
									bdvo.setNd(vo.getNd());//项目年度
									bdvo.setXmid(xmid);//项目id
									bdvo.setBdbh( bdzcvo.getBdbh());
									bdvo.setBdmc( bdzcvo.getBdmc());
									bdvo.setQdzh( bdzcvo.getQdzh());
									bdvo.setZdzh( bdzcvo.getZdzh());
									bdvo.setCd( bdzcvo.getCd());
									bdvo.setKd( bdzcvo.getKd());
									bdvo.setGj( bdzcvo.getGj());
									bdvo.setWgrq(bdzcvo.getWgrq());
									bdvo.setQgrq(bdzcvo.getQgrq());
									bdvo.setJgrq(bdzcvo.getJgrq());
									bdvo.setGcztfy( bdzcvo.getGcztfy());
									bdvo.setSgdw( bdzcvo.getSgdw());
									bdvo.setSgdwfzr( bdzcvo.getSgdwfzr());
									bdvo.setSgdwfzrlxfs( bdzcvo.getSgdwfzrlxfs());
									bdvo.setJldw( bdzcvo.getJldw());
									bdvo.setJldwfzr( bdzcvo.getJldwfzr());
									bdvo.setJldwfzrlxfs( bdzcvo.getJldwfzrlxfs());
									bdvo.setYwlx( bdzcvo.getYwlx());
									bdvo.setSjbh( bdzcvo.getSjbh());
									bdvo.setBz( bdzcvo.getBz());
									bdvo.setLrr( bdzcvo.getLrr());
									bdvo.setLrsj(bdzcvo.getLrsj());
									bdvo.setLrbm( bdzcvo.getLrbm());
									bdvo.setLrbmmc( bdzcvo.getLrbmmc());
									bdvo.setGxr( bdzcvo.getGxr());
									bdvo.setGxsj(bdzcvo.getGxsj());
									bdvo.setGxbm( bdzcvo.getGxbm());
									bdvo.setGxbmmc( bdzcvo.getGxbmmc());
									bdvo.setSjmj( bdzcvo.getSjmj());
									bdvo.setSfyx( bdzcvo.getSfyx());
									bdvo.setBddd( bdzcvo.getBddd());
									bdvo.setMj( bdzcvo.getMj());
									bdvo.setSghtid( bdzcvo.getSghtid());
									bdvo.setJlhtid( bdzcvo.getJlhtid());
									bdvo.setJsgm( bdzcvo.getJsgm());	
									bdvo.setSjdw( bdzcvo.getSjdw());
									bdvo.setSjdwfzr( bdzcvo.getSjdwfzr());
									bdvo.setSjdwfzrlxfs( bdzcvo.getSjdwfzrlxfs());	
									bdvo.setSgdwxmjl( bdzcvo.getSgdwxmjl());
									bdvo.setSgdwxmjllxdh( bdzcvo.getSgdwxmjllxdh());
									bdvo.setSgdwjsfzr( bdzcvo.getSgdwjsfzr());
									bdvo.setSgdwjsfzrlxdh( bdzcvo.getSgdwjsfzrlxdh());
									bdvo.setJldwzj( bdzcvo.getJldwzj());
									bdvo.setJldwzjlxdh( bdzcvo.getJldwzjlxdh());
									bdvo.setJldwzjdb( bdzcvo.getJldwzjdb());
									bdvo.setJldwzjdblxdh( bdzcvo.getJldwzjdblxdh());
									bdvo.setJldwaqjl( bdzcvo.getJldwaqjl());
									bdvo.setJldwaqjllxdh( bdzcvo.getJldwaqjllxdh());
									bdvo.setJsgm_sj( bdzcvo.getJsgm_sj());
									bdvo.setXmglgs( bdzcvo.getXmglgs());
									bdvo.setBdwybh( bdzcvo.getBdwybh());	
									bdvo.setXjglbdid(bdzcvo.getXjglbdid());
									bdvo.setXjxj(bdzcvo.getXjxj());
									bdvo.setIsnrtj(xvo.getIsnrtj());
									BaseDAO.insert(conn, bdvo);
									
																		
									TcjhVO newTcjhvo = new TcjhVO();
									newTcjhvo.setGc_jh_sj_id(bdvo.getJhsjid());
									newTcjhvo.setBdid(bdvo.getGc_xmbd_id());
									newTcjhvo.setBdbh(bdvo.getBdbh());
									newTcjhvo.setBdmc(bdvo.getBdmc());
									newTcjhvo.setJhid(vo.getGc_jh_zt_id());
									newTcjhvo.setNd(tvo.getNd());
									newTcjhvo.setXmid(tvo.getXmid());
									newTcjhvo.setXmbh(tvo.getXmbh());
									newTcjhvo.setXmmc(tvo.getXmmc());
									newTcjhvo.setXmxz(tvo.getXmxz());
									newTcjhvo.setXmsx(tvo.getXmsx());
									newTcjhvo.setYwlx(YwlxManager.GC_JH_SJ);
									BusinessUtil.setInsertCommonFields(newTcjhvo, user);//公共字段插入
									eventVO = EventManager.createEvent(conn, newTcjhvo.getYwlx(), user);//生成事件
									newTcjhvo.setSjbh(eventVO.getSjbh());
									newTcjhvo.setBz(bdvo.getBz());
									newTcjhvo.setXflx(tvo.getXflx());
									//newTcjhvo.setIsxf(tvo.getIsxf());
									newTcjhvo.setXmglgs(bdvo.getXmglgs());
									newTcjhvo.setXmbs("1");
									newTcjhvo.setIskgsj(tvo.getIskgsj());
									newTcjhvo.setIswgsj(tvo.getIswgsj());
									newTcjhvo.setIskypf(tvo.getIskypf());
									newTcjhvo.setIshpjds(tvo.getIshpjds());
									newTcjhvo.setIsgcxkz(tvo.getIsgcxkz());
									newTcjhvo.setIssgxk(tvo.getIssgxk());
									newTcjhvo.setIscbsjpf(tvo.getIscbsjpf());
									newTcjhvo.setIscqt(tvo.getIscqt());
									newTcjhvo.setIspqt(tvo.getIspqt());
									newTcjhvo.setIssgt(tvo.getIssgt());
									newTcjhvo.setIstbj(tvo.getIstbj());
									newTcjhvo.setIscs(tvo.getIscs());
									newTcjhvo.setIsjldw(tvo.getIsjldw());
									newTcjhvo.setIssgdw(tvo.getIssgdw());
									newTcjhvo.setIszc(tvo.getIszc());
									newTcjhvo.setIspq(tvo.getIspq());
									newTcjhvo.setIsjg(tvo.getIsjg());
									newTcjhvo.setSjwybh(bdvo.getBdwybh());//查了标段唯一编号
									newTcjhvo.setIsnrtj(xvo.getIsnrtj());
									BaseDAO.insert(conn, newTcjhvo);

								}
									
								//删除暂存标段
								String sql_del = "DELETE FROM GC_XMBD_ZC WHERE xmid = '"+xmid+"'";
								DBUtil.execSql(conn, sql_del);
							}	
						}
					}
					tvo.setZxbz("1");	
					
					// jiangc end
					
					
					/*if(vo.getXflx().equals("2")){//计划为应急时，项目属性为0应急
						tvo.setXmsx("2");//项目属性
					}else{
						tvo.setXmsx(xmsx);//项目属性
					}*/
					tvo.setJhid(jhid);//计划ID
					tvo.setXflx(vo.getXflx());//下发类型
					tvo.setXmxz(xjxj);//项目性质-新建/续建
					tvo.setYwlx(YwlxManager.GC_JH_SJ);//业务类型
				    eventVO = EventManager.createEvent(conn, tvo.getYwlx(), user);//生成事件
				    tvo.setSjbh(eventVO.getSjbh());//事件编号
				    BusinessUtil.setInsertCommonFields(tvo,user);//公共方法
					BaseDAO.insert(conn, tvo);
					//===================增加版本项目数据===================
					bxmvo = new BgxmVO();

					bxmvo.setGc_jh_bgxm_id(new RandomGUID().toString());//版本项目ID
					bxmvo.setBgid(bgbbId);//变更版本ID
					bxmvo.setJhid(jhid);//计划ID
					bxmvo.setXmid(xmid);//项目ID
					bxmvo.setBdid(tvo.getBdid());//标段ID
					bxmvo.setXmbh(tvo.getXmbh());//项目编号
					bxmvo.setBdbh(tvo.getBdbh());//标段编号
					bxmvo.setXmmc(tvo.getXmmc());//项目名称
					bxmvo.setBdmc(tvo.getBdmc());//标段名称
					bxmvo.setXmxz(xjxj);//项目性质 -新建/续建
					bxmvo.setXmsx(xmsx);//项目属性
					bxmvo.setYwlx(YwlxManager.GC_JH_BGXM);//业务类型
					eventVO = EventManager.createEvent(conn, bxmvo.getYwlx(),user);// 生成事件
					bxmvo.setSjbh(eventVO.getSjbh());//事件编号
					BusinessUtil.setInsertCommonFields(bxmvo,user);//公共方法
					BaseDAO.insert(conn, bxmvo);
			}	
			resultVO = vo.getRowJson();
			// 统筹计划下达：项目下达到统筹计划，发送相关配置人员信息提醒。    add by xiahongbo by 2014-10-14
            PushMessage.push(conn, request, YwlxManager.GC_JH_ZT, "统筹名称为【"+vo.getJhmc()+"】，统筹批次为【"+vo.getJhpch()+"】的项目已经下达，请查看。", "&spid="+spid, xvo.getSjbh(), ywlx, "", "统筹计划下达，请查看");
			conn.commit();
			resultVO = xvo.getRowJson();

            LogManager.writeUserLog(xvo.getSjbh(), ywlx,
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "<按计划下达>成功", user,"","");
			String jsona = Pub.makeQueryConditionByID(spid, "t.GC_TCJH_SP_ID");
	        return queryJhpc(jsona, request);
		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return resultVO;
	}
	
	@Override
	public String insertDjz(String json,User user,String ids) throws Exception {
		Connection conn = DBUtil.getConnection();
		String resultVO = null;
		DjzVO vo = null;
		XmxdkVO xvo = null;
		
		try {
			conn.setAutoCommit(false);
			//删除原有数据
			String sql = "DELETE FROM GC_CBK_DJZ WHERE DCZLX = '3'";
			DBUtil.execSql(conn, sql);
			if(!Pub.empty(ids)){
				//循环取ID
				if(ids.length() > 0 && ids.indexOf("\"")>-1)
				{
					ids = ids.replaceAll("\"", "");
				}
				String[] id = ids.split(",");
				for(int i =0;i<id.length;i++){
					//根据ID获取下达库对象
					xvo = new XmxdkVO();
					xvo.setGc_tcjh_xmxdk_id(id[i]);
					xvo = (XmxdkVO)BaseDAO.getVOByPrimaryKey(conn,xvo);
					//新增待结转数据
					vo = new DjzVO();
					vo.setGc_cbk_djz_id(new RandomGUID().toString());
					vo.setXmzj(xvo.getGc_tcjh_xmxdk_id());
					vo.setXmbh(xvo.getXmbh());
					vo.setXmmc(xvo.getXmmc());
					vo.setDczlx("3");//下达库类型
					BaseDAO.insert(conn, vo);
					LogManager.writeUserLog(null, YwlxManager.GC_JH_XM_ZC,
							Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_SUCCESS,
							user.getOrgDept().getDeptName() + " " + user.getName()
									+ "<结转>成功", user,"","");
				}
				resultVO = vo.getRowJson();
			}
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
	public String queryDjz(String json,User user) throws Exception {
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		try {
			PageManager page = RequestUtil.getPageManager(json);
		if (page == null)
			page = new PageManager();
			conn.setAutoCommit(false);
			String sql = "SELECT " +
					"gc_tcjh_xmxdk_id, xmbh, xmmc, qy, nd, pch, xmlx, xmdz, jsnr, jsyy, jsrw, jsbyx, jhztze, gc, zc, qt, xmsx, isbt, isnatc, jszt, xmfr, xmglgs, fzr_glgs, lxfs_glgs, sgdw, fzr_sgdw, lxfs_sgdw, jldw, fzr_jldw, lxfs_jldw, yzdb, sjdw, fzr_sjdw, lxfs_sjdw, lxfs_yzdb, wgrq, qgrq, jgrq, ywlx, sjbh, bz, lrr, lrsj, lrbm, lrbmmc, gxr, gxsj, gxbm, gxbmmc, sjmj, sfyx, xmcbk_id, xjxj, pxh,wdd,jsmb "+
					"FROM " +
					"GC_TCJH_XMXDK " +
					"WHERE " +
					"GC_TCJH_XMXDK_ID " +
					"IN(SELECT XMZJ FROM GC_CBK_DJZ WHERE DCZLX = '3') ";
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			//表选翻译
			bs.setFieldTranslater("ND", "GC_CBK_ND", "ND", "ND");//年度
			bs.setFieldTranslater("XMGLGS", "FS_ORG_DEPT", "ROW_ID", "BMJC");//项目管理公司
			//字典翻译
			bs.setFieldDic("XMLX", "XMLX");//项目类型
			//bs.setFieldDic("XMGLGS", "XMGLGS");//项目管理公司
			bs.setFieldDic("PCH","PCH");//批次号
			bs.setFieldDic("QY","QY");//区域
			bs.setFieldDic("XMSX", "XMSX");//项目属性
			bs.setFieldDic("ISNATC","XDZT");//是否下发
			bs.setFieldDic("ISBT", "SF");//是否BT项目
			bs.setFieldDic("SFYX", "SF");//是否有效
			bs.setFieldDic("XJXJ", "XMXZ");//新建/续建
			//金额翻译
			bs.setFieldThousand("JHZTZE");//总投资
			bs.setFieldThousand("GC");//工程
			bs.setFieldThousand("ZC");//征拆
			bs.setFieldThousand("QT");//其他
			domresult = bs.getJson();
			LogManager.writeUserLog(null, ywlx,
					Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "查询<下达项目库>", user,"","");
		}  catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	
	//项目名称自动查询
	/* 
	 * 根据输入值从计划数据表自动模糊匹配项目名称
	 * @see com.ccthanking.business.tcjh.jhgl.service.TcjhService#xmmcAutoComplete(com.ccthanking.framework.model.autocomplete, com.ccthanking.framework.common.User)
	 */
	@Override
	public List<autocomplete> xmmcAutoCompleteToXmxdk(autocomplete json,User user) throws Exception {
		List<autocomplete> autoResult = new ArrayList<autocomplete>(); 
		autocomplete ac = new autocomplete();
		String condition = RequestUtil.getConditionList(json.getMatchInfo()).getConditionWhere();
		condition += BusinessUtil.getSJYXCondition(null);
		condition += BusinessUtil.getCommonCondition(user,null);
		String [][] result = DBUtil.query("select distinct xmmc from GC_TCJH_XMXDK " + json.getTablePrefix() + " where " + condition);
        if(null != result){
        	for(int i =0;i<result.length;i++){
        	  ac = new autocomplete();
              ac.setRegionName(result[i][0]);
              autoResult.add(ac);
        	}
        }
		return autoResult;
	}
	
	@Override
	public String queryMaxPch(String xflx,String nd,User user) throws Exception {
		
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		try {
			conn.setAutoCommit(false);
			Calendar ca = Calendar.getInstance();
			int year = ca.get(Calendar.YEAR);
			//String nd = String.valueOf(year);
			String maxPch = "";
			String minPch = "";
			String sql1 = "";
			if(xflx.equals("1")){
				sql1 = "SELECT MAX(T.DIC_CODE),MIN(T.DIC_CODE) FROM fs_dic_tree T WHERE T.DIC_NAME_CODE = 'PCH' AND t.dic_code != 'PCH' ";
			}else{
				sql1 = "SELECT MAX(T.DIC_CODE),MIN(T.DIC_CODE) FROM fs_dic_tree T WHERE T.DIC_NAME_CODE = 'LXPC' AND t.dic_code != 'LXPC'";
			}
			String[][] max_pch = DBUtil.query(conn, sql1);
			if(max_pch.length>0 && null !=max_pch && !Pub.empty(max_pch[0][0]) && !Pub.empty(max_pch[0][1])){
				maxPch = max_pch[0][0].toString();
				minPch = max_pch[0][1].toString();
			}
			String sql = "SELECT MAX(JHPCH) from GC_JH_ZT WHERE SFYX='1' and nd='"+nd+"' and XFLX='"+xflx+"'";
			String[][] pch = DBUtil.query(conn, sql);
			String jhpch = "";
			if(pch.length>0 && null !=pch && !Pub.empty(pch[0][0])){
				jhpch = pch[0][0].toString();
				if(xflx.equals("1")){
					if(jhpch.equals(maxPch)){
						domresult = minPch;
					}else if(Integer.parseInt(jhpch) == 9){
						domresult = "10";
					}else if(Integer.parseInt(jhpch) >= 10){
						domresult = String.valueOf(Integer.parseInt(jhpch)+1);
					}else{
						domresult = "0"+String.valueOf(Integer.parseInt(jhpch)+1);
					}
				}else{
					if(jhpch.equals(maxPch)){
						domresult = minPch;
					}else if(jhpch.equals("零星09")){
						domresult = "零星10";
					}else{
						String subStr = jhpch.substring(2,4);
						if(Integer.parseInt(subStr) >= 10){
							domresult = "零星"+String.valueOf(Integer.parseInt(jhpch)+1);
						}else{
							domresult = "零星0"+String.valueOf(Integer.parseInt(subStr)+1);
						}
					}
				}
				
			}else{
				domresult = minPch;
			}
		}  catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	
	//日期类型转型
	public String formatTime(Date date){
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy"); 
		String strDate = formatter.format(date);  
		return strDate;
	}
	//字符串转日期
	public static Date StrToDate(String str) throws java.text.ParseException {
		  
		   SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		   Date date = null;
		   try {
		    date = format.parse(str);
		   } catch (ParseException e) {
		    e.printStackTrace();
		   }
		   return date;
		}

	
	@Override
    public String queryHTSJ(String json, User user, Map map) throws Exception {

		Connection conn = DBUtil.getConnection();
        String domresult = "";
        try {

            // 组织查询条件
            PageManager page = RequestUtil.getPageManager(json);
            QueryConditionList list = RequestUtil.getConditionList(json);

            // ghh.htid需要合同号
            String condition = list == null ? "" : list.getConditionWhere();

            // 组织查询条件
            String orderFilter = RequestUtil.getOrderFilter(json);
            if(!Pub.empty((String)map.get("id"))) {
            //	condition += " and t.gc_tcjh_xmxdk_id = '"+ map.get("id") +"' ";
            	condition += " and t.gc_tcjh_xmxdk_id in ("+ getAllXm(conn, (String)map.get("id")) +") ";
            }
            if(!Pub.empty((String)map.get("htlx")))
            	condition += " and ght.htlx = '"+ map.get("htlx") +"' ";
            else
            	condition += " and ght.htlx not in ('SG','JL','SJ') ";
            condition += BusinessUtil.getSJYXCondition("ghh");// 是否有效
            condition += BusinessUtil.getCommonCondition(user, "ghh");// 预留
            // update by xhb 2014-04-22  查询已经签订的合同 start
            // condition += " and ght.htzt!='-3' and ght.htzt!='4' ";
            condition += " and ght.htzt>0 ";
            // update by xhb 2014-04-22  查询已经签订的合同 end
            condition += orderFilter;
            if (page == null)
                page = new PageManager();
            page.setFilter(condition);

            conn.setAutoCommit(false);

            BaseResultSet bs = DBUtil.query(conn, QUERY_HTSJ_XMSJ_ZTBSJ, page);

            bs.setFieldDic("BGLX", "BGLX");
            bs.setFieldDic("HTLX", "HTLX");
            bs.setFieldDic("HTZT", "HTRXZT");// 合同签订状态
            bs.setFieldDic("ZBFS", "ZBFS");// 招标方式
            
            
            bs.setFieldThousand("ZZXHTJ");
            bs.setFieldThousand("ZXHTJ");
            bs.setFieldThousand("ZZBJ");
            bs.setFieldThousand("HTQDJ");//合同签订价(元)
            bs.setFieldThousand("HTJSJ");//合同结算价或中止价
            //bs.setFieldThousand("JE");//金额
            //bs.setFieldThousand("BGTS");//变更天数
            //bs.setFieldThousand("HTZF");//合同支付
            //bs.setFieldThousand("WCZF");//完成投资
            //bs.setFieldThousand("BGJE");//变更金额
            //bs.setFieldThousand("HTJS");//合同结算
            //bs.setFieldThousand("ZXHTJ");//最新合同价

            bs.setFieldSjbh("SJBH");
            
            domresult = bs.getJson();
            LogManager.writeUserLog(null, ywlx, Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS, user.getOrgDept().getDeptName()
                    + " " + user.getName() + "查询<单合同数据>", user, "", "");
        } catch (Exception e) {
            DBUtil.rollbackConnetion(conn);
            e.printStackTrace(System.out);
        } finally {
            DBUtil.closeConnetion(conn);
        }
        return domresult;

    }
	
	@Override
	public String queryHtSum(String json,User user,Map params) throws Exception {
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		try {
			String sql = " select count(0) NUM,sum(zhtqdj) ZHTQDJ,SUM(ZZXHTJ) ZZXHTJ " 
				+" from gc_htgl_ht where id in ( "
				+" select distinct(ghh2.id) from  GC_TCJH_XMXDK t "
				+" left join gc_jh_sj gjs on t.gc_tcjh_xmxdk_id = gjs.xmid "
				+" left join gc_htgl_htsj ghh on ghh.jhsjid = gjs.gc_jh_sj_id "
				+" left join gc_htgl_ht ghh2 on ghh.htid = ghh2.id "
		//		+" where t.gc_tcjh_xmxdk_id= '"+params.get("id")+"' and " 
				+" where t.gc_tcjh_xmxdk_id in ("+getAllXm(conn, (String)params.get("id"))+") and " 
				// ADD BY XHB 合同状态大于0，表示已签订的合同
				+" /*ghh2.htzt!='-3' and ghh2.htzt!='4'*/ "
				+" ghh2.htzt>0";
		
			if(!Pub.empty((String)params.get("htlx"))){
				if(!((String)params.get("htlx")).equals("all")){
					sql += " and ghh2.htlx = '"+params.get("htlx")+"' ";
				}
			}else{
				sql += " and ghh2.htlx not in ('SJ','SG','JL') ";
			}
			sql +=" ) ";

			conn.setAutoCommit(false);
			BaseResultSet bs = DBUtil.query(conn, sql, null);
			
			
			//金额翻译
			bs.setFieldThousand("ZHTQDJ");//总合同签订价
			bs.setFieldThousand("ZZXHTJ");//总最新合同价
			domresult = bs.getJson();
			LogManager.writeUserLog(null, ywlx,
					Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "查询部门合同总数及金额", user,"","");
		}  catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	
	
	//=================================================================================================
	//查询计划批次数据
	@Override
	public String queryJhpc(String json,HttpServletRequest request) throws Exception {
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		try {
			PageManager page = RequestUtil.getPageManager(json);
			String orderFilter = RequestUtil.getOrderFilter(json);
			String condition = RequestUtil.getConditionList(json).getConditionWhere();
			//condition +=" and t.sjbh = e.sjbh ";
	        condition += BusinessUtil.getSJYXCondition("t");
	        condition += BusinessUtil.getCommonCondition(user,"t");
	        condition += orderFilter;
		if (page == null)
			page = new PageManager();
			page.setFilter(condition);

			conn.setAutoCommit(false);
			String sql = "select GC_TCJH_SP_ID,spmc ,nd,t.ywlx,spfs, sp.resultdscr,t.sjbh, event.sjzt,isxd,closetime,pch,lrsj,splx,bz from gc_tcjh_sp t left join  " +
					"( select info.processoid,eventid, info.row_flg ,info.memo, tail.resultdscr,  tail.closetime from( select processoid, memo,eventid,  row_number() over(partition by eventid order by createtime desc) as row_flg from AP_PROCESSINFO )" +
					" info left join ap_processdetail tail on info.processoid = tail.processoid where row_flg=1 ) sp " +
					" on t.sjbh= sp.eventid left join FS_EVENT event on t.sjbh = event.sjbh";
			/*String sql = "select " +
					"t.gc_tcjh_sp_id, t.spmc, t.isxd, t.sjbh, t.ywlx, t.pch, t.nd, t.sfyx, t.splx, t.bz, t.lrr, t.lrsj, t.lrbm, t.lrbmmc, t.gxr, t.gxsj, t.gxbm, t.gxbmmc, t.sjmj,e.sjzt "+
					"from " +
					"gc_tcjh_sp t,fs_event e";*/
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			//表选翻译
			//bs.setFieldTranslater("ND", "GC_CBK_ND", "ND", "ND");//年度
			//字典翻译
			bs.setFieldDic("PCH","PCH");//批次号
			bs.setFieldDic("SJZT","SJZT");//审批状态
			bs.setFieldDic("ISXD","SF");//审批状态
			domresult = bs.getJson();
			LogManager.writeUserLog(null, YwlxManager.GC_JH_ZT,
					Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "查询<统筹计划主体>", user,"","");
		}  catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	
	@Override
	public String insertJhsp(String json,HttpServletRequest request) throws Exception {
		Connection conn = DBUtil.getConnection();
		String resultVO = null;
		TcjhSpVO vo = new TcjhSpVO();
		EventVO eventVO = null;
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		try {
			conn.setAutoCommit(false);
			JSONArray list = vo.doInitJson(json);
			vo.setValueFromJson((JSONObject)list.get(0));
			if(Pub.empty(vo.getGc_tcjh_sp_id())){
				vo.setGc_tcjh_sp_id(new RandomGUID().toString()); 
				//String nd = formatTime(new Date());//获取下达年度
				//vo.setNd(nd);
				vo.setYwlx(YwlxManager.GC_CBK_SP);
			    eventVO = EventManager.createEvent(conn, vo.getYwlx(), user);//生成事件
			    vo.setSjbh(eventVO.getSjbh());
				BusinessUtil.setInsertCommonFields(vo,user);
				vo.setConnection(conn);
				BaseDAO.insert(conn, vo);
				LogManager.writeUserLog(vo.getSjbh(), YwlxManager.GC_CBK_SP,
						Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_SUCCESS,
						user.getOrgDept().getDeptName() + " " + user.getName()
								+ "新增<计划审批>成功", user,"","");
				
			}else{

			    vo.setConnection(conn);
				BusinessUtil.setUpdateCommonFields(vo,user);
				BaseDAO.update(conn, vo);
				LogManager.writeUserLog(vo.getSjbh(), YwlxManager.GC_CBK_SP,
						Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_SUCCESS,
						user.getOrgDept().getDeptName() + " " + user.getName()
								+ "更新<计划审批>成功", user,"","");
			}
		
		resultVO = vo.getRowJson();
		conn.commit();
		String jsona = Pub.makeQueryConditionByID(vo.getGc_tcjh_sp_id(), "t.gc_tcjh_sp_id");
        return queryJhpc(jsona, request);
		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return resultVO;
	}
	
		//查询计划批次数据
		@Override
		public String queryXmxxByPch(String json,HttpServletRequest request) throws Exception {
			Connection conn = DBUtil.getConnection();
			String domresult = "";
			User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
			try {
				PageManager page = RequestUtil.getPageManager(json);
				String orderFilter = RequestUtil.getOrderFilter(json);
				String condition = RequestUtil.getConditionList(json).getConditionWhere();
				//condition += " and t.gc_tcjh_xmxdk_id not in(select GC_TCJH_XMXDK_SP_ID from GC_TCJH_XMXDK_SP) ";
		        condition += BusinessUtil.getSJYXCondition("t");
		        condition += BusinessUtil.getCommonCondition(user,"t");
		        condition += orderFilter;
			if (page == null)
				page = new PageManager();
				page.setFilter(condition);

				conn.setAutoCommit(false);
				String sql = "select " +
						"t.gc_tcjh_xmxdk_sp_id, t.xmbh, t.xmmc, t.qy, t.nd, t.pch, t.xmlx, t.xmdz, t.jsnr, t.jsyy, t.jsrw, t.jsbyx, t.jhztze, t.gc, t.zc, t.qt, t.xmsx, t.isbt, t.isnatc, t.jszt, t.xmfr, t.xmglgs, t.fzr_glgs, t.lxfs_glgs, t.sgdw, t.fzr_sgdw, t.lxfs_sgdw, t.jldw, t.fzr_jldw, t.lxfs_jldw, t.yzdb, t.sjdw, t.fzr_sjdw, t.lxfs_sjdw, t.lxfs_yzdb, t.wgrq, t.qgrq, t.jgrq, t.ywlx, t.sjbh, t.bz, t.lrr, t.lrsj, t.lrbm, t.lrbmmc, t.gxr, t.gxsj, t.gxbm, t.gxbmmc, t.sjmj, t.sfyx, t.xmcbk_id, t.xjxj, t.pxh, t.xj_xmid, t.wdd, t.jsmb, t.sghtid, t.jlhtid, t.sjhtid, t.sgdwxmjl, t.sgdwxmjllxdh, t.sgdwjsfzr, t.sgdwjsfzrlxdh, t.jldwzj, t.jldwzjlxdh, t.jldwzjdb, t.jldwzjdblxdh, t.jldwaqjl, t.jldwaqjllxdh, t.jsnr_sj,t.tcjhztid " +
						"from " +
						"gc_tcjh_xmxdk_sp t";
				BaseResultSet bs = DBUtil.query(conn, sql, page);
				//表选翻译
				//bs.setFieldTranslater("ND", "GC_CBK_ND", "ND", "ND");//年度
				bs.setFieldTranslater("XJ_XMID", "GC_TCJH_XMXDK", "GC_TCJH_XMXDK_ID", "XMMC");//续建关联下达库
				bs.setFieldTranslater("XMGLGS", "FS_ORG_DEPT", "ROW_ID", "BMJC");//项目管理公司
				bs.setFieldTranslater("YZDB", "FS_ORG_PERSON", "ACCOUNT", "NAME");//业主代表
				//字典翻译
				bs.setFieldDic("XMLX", "XMLX");//项目类型
				bs.setFieldDic("PCH","PCH");//批次号
				bs.setFieldDic("QY","QY");//区域
				bs.setFieldDic("XMSX", "XMSX");//项目属性
				bs.setFieldDic("ISNATC","XDZT");//是否下发
				bs.setFieldDic("ISBT", "SF");//是否BT项目
				bs.setFieldDic("SFYX", "SF");//是否有效
				bs.setFieldDic("XJXJ", "XMXZ");//新建/续建
				bs.setFieldDic("WDD", "WDD");//稳定度
				//金额翻译
				bs.setFieldThousand("JHZTZE");//总投资
				bs.setFieldThousand("GC");//工程
				bs.setFieldThousand("ZC");//征拆
				bs.setFieldThousand("QT");//其他
				domresult = bs.getJson();
				LogManager.writeUserLog(null, YwlxManager.GC_JH_ZT,
						Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS,
						user.getOrgDept().getDeptName() + " " + user.getName()
								+ "查询<下达库审批数据>", user,"","");
			}  catch (Exception e) {
				DBUtil.rollbackConnetion(conn);
				e.printStackTrace(System.out);
			} finally {
				DBUtil.closeConnetion(conn);
			}
			return domresult;
		}
		
		@Override
		public String insertXmxdkSp(String json,HttpServletRequest request) throws Exception {
			Connection conn = DBUtil.getConnection();
			User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
			String resultVO = null;
			ZtVO vo = new ZtVO();
			EventVO eventVO = null;
			try {
				conn.setAutoCommit(false);
				String nd = request.getParameter("nd");
				if(Pub.empty(nd)){
					nd = "";
				}
				String pch = request.getParameter("pch");
				if(Pub.empty(pch)){
					pch = "";
				}
				String tcjhztid = request.getParameter("tcjhztid");
				if(Pub.empty(tcjhztid)){
					tcjhztid = "";
				}
				String querySql = "SELECT gc_tcjh_xmxdk_id FROM GC_TCJH_XMXDK WHERE SFYX='1' AND ND='"+nd+"' AND PCH='"+pch+"' AND gc_tcjh_xmxdk_id NOT IN(SELECT GC_TCJH_XMXDK_SP_ID FROM GC_TCJH_XMXDK_SP WHERE SFYX = '1')";
				String[][] queryResult = DBUtil.query(conn, querySql);
				if(Pub.emptyArray(queryResult)){
					for(int i=0;i<queryResult.length;i++){
						String id = queryResult[i][0].toString();
						XmxdkVO xvo= new XmxdkVO();
						xvo.setGc_tcjh_xmxdk_id(id);
						xvo = (XmxdkVO)BaseDAO.getVOByPrimaryKey(conn,xvo);
						XmxdkSpVO spvo= new XmxdkSpVO();
						JSONArray splist = spvo.doInitJson(xvo.getRowJson());
						spvo.setValueFromJson((JSONObject)splist.get(0));
						spvo.setGc_tcjh_xmxdk_sp_id(xvo.getGc_tcjh_xmxdk_id());
						spvo.setTcjhztid(tcjhztid);//插入对应审批批次ID
						spvo.setYwlx(YwlxManager.GC_XM_SP);//也类型
						eventVO = EventManager.createEvent(conn, spvo.getYwlx(),user);// 生成事件
						spvo.setSjbh(eventVO.getSjbh());//事件编号
						BusinessUtil.setInsertCommonFields(spvo,user);//公共方法
						BaseDAO.insert(conn, spvo);
					}
				}
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
		public String deleteXdkxmSp(String json,HttpServletRequest request ) throws Exception {
			Connection conn = null;
			String domresult = "";
			XmxdkSpVO vo = new XmxdkSpVO();
			User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
			try {
				conn = DBUtil.getConnection();
				conn.setAutoCommit(false);
				JSONArray list = vo.doInitJson(json);
				vo.setValueFromJson((JSONObject)list.get(0));
				//String sql ="DELETE FROM GC_TCJH_XMXDK_SP WHERE GC_TCJH_XMXDK_SP_ID = '"+vo.getGc_tcjh_xmxdk_sp_id()+"'";
				
				//删业务数据
				XmxdkSpVO delVO = new XmxdkSpVO();
				delVO.setGc_tcjh_xmxdk_sp_id(vo.getGc_tcjh_xmxdk_sp_id());
				///delVO.setSfyx("0");
				BaseDAO.delete(conn, delVO);
				conn.commit();
			} catch (Exception e) {
				DBUtil.rollbackConnetion(conn);
				e.printStackTrace(System.out);
			} finally {
				DBUtil.closeConnetion(conn);
			}
			return domresult;
		}
		
		//查询计划批次数据
		@Override
		public String queryXmxxSp(String json,HttpServletRequest request) throws Exception {
			Connection conn = DBUtil.getConnection();
			String domresult = "";
			User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
			try {
				PageManager page = RequestUtil.getPageManager(json);
				String orderFilter = RequestUtil.getOrderFilter(json);
				String condition = RequestUtil.getConditionList(json).getConditionWhere();
				String sjbh = request.getParameter("sjbh");
				if(!Pub.empty(sjbh)){
					condition += " AND  t.tcjhztid = (select GC_TCJH_SP_ID from GC_TCJH_SP where sjbh ='"+sjbh+"' and sfyx='1')";
				}
				//condition += " and t.gc_tcjh_xmxdk_id not in(select GC_TCJH_XMXDK_SP_ID from GC_TCJH_XMXDK_SP) ";
		        condition += BusinessUtil.getSJYXCondition("t");
		        condition += BusinessUtil.getCommonCondition(user,"t");
		        condition += orderFilter;
			if (page == null)
				page = new PageManager();
				page.setFilter(condition);

				conn.setAutoCommit(false);
				String sql = "select " +
						"t.gc_tcjh_xmxdk_sp_id, t.xmbh, t.xmmc, t.qy, t.nd, t.pch, t.xmlx, t.xmdz, t.jsnr, t.jsyy, t.jsrw, t.jsbyx, t.jhztze, t.gc, t.zc, t.qt, t.xmsx, t.isbt, t.isnatc, t.jszt, t.xmfr, t.xmglgs, t.fzr_glgs, t.lxfs_glgs, t.sgdw, t.fzr_sgdw, t.lxfs_sgdw, t.jldw, t.fzr_jldw, t.lxfs_jldw, t.yzdb, t.sjdw, t.fzr_sjdw, t.lxfs_sjdw, t.lxfs_yzdb, t.wgrq, t.qgrq, t.jgrq, t.ywlx, t.sjbh, t.bz, t.lrr, t.lrsj, t.lrbm, t.lrbmmc, t.gxr, t.gxsj, t.gxbm, t.gxbmmc, t.sjmj, t.sfyx, t.xmcbk_id, t.xjxj, t.pxh, t.xj_xmid, t.wdd, t.jsmb, t.sghtid, t.jlhtid, t.sjhtid, t.sgdwxmjl, t.sgdwxmjllxdh, t.sgdwjsfzr, t.sgdwjsfzrlxdh, t.jldwzj, t.jldwzjlxdh, t.jldwzjdb, t.jldwzjdblxdh, t.jldwaqjl, t.jldwaqjllxdh, t.jsnr_sj,t.tcjhztid " +
						"from " +
						"gc_tcjh_xmxdk_sp t";
				BaseResultSet bs = DBUtil.query(conn, sql, page);
				//表选翻译
				//bs.setFieldTranslater("ND", "GC_CBK_ND", "ND", "ND");//年度
				bs.setFieldTranslater("XJ_XMID", "GC_TCJH_XMXDK", "GC_TCJH_XMXDK_ID", "XMMC");//续建关联下达库
				bs.setFieldTranslater("XMGLGS", "FS_ORG_DEPT", "ROW_ID", "BMJC");//项目管理公司
				bs.setFieldTranslater("YZDB", "FS_ORG_PERSON", "ACCOUNT", "NAME");//业主代表
				//字典翻译
				bs.setFieldDic("XMLX", "XMLX");//项目类型
				bs.setFieldDic("PCH","PCH");//批次号
				bs.setFieldDic("QY","QY");//区域
				bs.setFieldDic("XMSX", "XMSX");//项目属性
				bs.setFieldDic("ISNATC","XDZT");//是否下发
				bs.setFieldDic("ISBT", "SF");//是否BT项目
				bs.setFieldDic("SFYX", "SF");//是否有效
				bs.setFieldDic("XJXJ", "XMXZ");//新建/续建
				bs.setFieldDic("WDD", "WDD");//稳定度
				//金额翻译
				bs.setFieldThousand("JHZTZE");//总投资
				bs.setFieldThousand("GC");//工程
				bs.setFieldThousand("ZC");//征拆
				bs.setFieldThousand("QT");//其他
				domresult = bs.getJson();
				LogManager.writeUserLog(null, YwlxManager.GC_JH_ZT,
						Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS,
						user.getOrgDept().getDeptName() + " " + user.getName()
								+ "查询<下达库审批数据>", user,"","");
			}  catch (Exception e) {
				DBUtil.rollbackConnetion(conn);
				e.printStackTrace(System.out);
			} finally {
				DBUtil.closeConnetion(conn);
			}
			return domresult;
		}
				
		@Override
		public String insertXmxdSpAxm(String json,HttpServletRequest request) throws Exception {
			Connection conn = DBUtil.getConnection();
			User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
			String resultVO = null;
			ZtVO vo = new ZtVO();
			EventVO eventVO = null;
			try {
				conn.setAutoCommit(false);
				String tcjhztid = request.getParameter("tcjhztid");
				if(Pub.empty(tcjhztid)){
					tcjhztid = "";
				}
				String ids = request.getParameter("ids");
				if(!Pub.empty(ids)){
					//循环取ID
					if(ids.length() > 0 && ids.indexOf("\"")>-1)
					{
						ids = ids.replaceAll("\"", "");
					}
					String[] id = ids.split(",");
					for(int i=0;i<id.length;i++){
						XmxdkVO xvo= new XmxdkVO();
						xvo.setGc_tcjh_xmxdk_id(id[i]);
						xvo = (XmxdkVO)BaseDAO.getVOByPrimaryKey(conn,xvo);
						XmxdkSpVO spvo= new XmxdkSpVO();
						JSONArray splist = spvo.doInitJson(xvo.getRowJson());
						spvo.setValueFromJson((JSONObject)splist.get(0));
						spvo.setGc_tcjh_xmxdk_sp_id(xvo.getGc_tcjh_xmxdk_id());
						spvo.setTcjhztid(tcjhztid);//插入对应审批批次ID
						spvo.setYwlx(YwlxManager.GC_XM_SP);//也类型
						eventVO = EventManager.createEvent(conn, spvo.getYwlx(),user);// 生成事件
						spvo.setSjbh(eventVO.getSjbh());//事件编号
						BusinessUtil.setInsertCommonFields(spvo,user);//公共方法
						BaseDAO.insert(conn, spvo);
					}
				}
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
		public String queryApc(String json,HttpServletRequest request) throws Exception {
			Connection conn = DBUtil.getConnection();
			User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
			String domresult = "";
			try {
				PageManager page = RequestUtil.getPageManager(json);
				String orderFilter = RequestUtil.getOrderFilter(json);
				String condition = RequestUtil.getConditionList(json).getConditionWhere();
		        condition += BusinessUtil.getSJYXCondition(null);
		        condition += BusinessUtil.getCommonCondition(user,null);
		        String ids = request.getParameter("ids");
				if(Pub.empty(ids)){
					ids = "";
				}else{
					ids = ids.substring(0,ids.length()-1);
				}
		        if(!Pub.empty(ids)){
		        	condition += " AND gc_tcjh_xmxdk_id NOT IN("+ids+") ";
		        }
		        condition += " AND  gc_tcjh_xmxdk_id NOT IN(SELECT gc_tcjh_xmxdk_sp_id FROM GC_TCJH_XMXDK_SP where sfyx='1' ) ";
		        
		        condition += orderFilter;
			if (page == null)
				page = new PageManager();
				page.setFilter(condition);

				conn.setAutoCommit(false);
				String sql = "SELECT " +
						"gc_tcjh_xmxdk_id, xmbh, xmmc, qy, nd, pch, xmlx, xmdz, jsnr, jsyy, jsrw, jsbyx, jhztze, gc, zc, qt, xmsx, isbt, isnatc, jszt, xmfr, xmglgs, fzr_glgs, lxfs_glgs, sgdw, fzr_sgdw, lxfs_sgdw, jldw, fzr_jldw, lxfs_jldw, yzdb, sjdw, fzr_sjdw, lxfs_sjdw, lxfs_yzdb, wgrq, qgrq, jgrq, ywlx, sjbh, bz, lrr, lrsj, lrbm, lrbmmc, gxr, gxsj, gxbm, gxbmmc, sjmj, sfyx, xmcbk_id, xjxj, pxh, xj_xmid,wdd,jsmb,xmwybh " +
						"FROM " +
						"GC_TCJH_XMXDK";
				BaseResultSet bs = DBUtil.query(conn, sql, page);
				//表选翻译
				//bs.setFieldTranslater("ND", "GC_CBK_ND", "ND", "ND");//年度
				bs.setFieldTranslater("XJ_XMID", "GC_TCJH_XMXDK", "GC_TCJH_XMXDK_ID", "XMMC");//续建关联下达库
				bs.setFieldTranslater("XMGLGS", "FS_ORG_DEPT", "ROW_ID", "BMJC");//项目管理公司
				bs.setFieldTranslater("YZDB", "FS_ORG_PERSON", "ACCOUNT", "NAME");//业主代表
				//字典翻译
				bs.setFieldDic("XMLX", "XMLX");//项目类型
				bs.setFieldDic("PCH","PCH");//批次号
				bs.setFieldDic("QY","QY");//区域
				bs.setFieldDic("XMSX", "XMSX");//项目属性
				bs.setFieldDic("ISNATC","XDZT");//是否下发
				bs.setFieldDic("ISBT", "SF");//是否BT项目
				bs.setFieldDic("SFYX", "SF");//是否有效
				bs.setFieldDic("XJXJ", "XMXZ");//新建/续建
				bs.setFieldDic("WDD", "WDD");//稳定度
				//金额翻译
				bs.setFieldThousand("JHZTZE");//总投资
				bs.setFieldThousand("GC");//工程
				bs.setFieldThousand("ZC");//征拆
				bs.setFieldThousand("QT");//其他
				domresult = bs.getJson();
				LogManager.writeUserLog(null, ywlx,
						Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS,
						user.getOrgDept().getDeptName() + " " + user.getName()
								+ "查询<下达项目库>", user,"","");
			}  catch (Exception e) {
				DBUtil.rollbackConnetion(conn);
				e.printStackTrace(System.out);
			} finally {
				DBUtil.closeConnetion(conn);
			}
			return domresult;
		}
//验证项目年度是否统一
	@Override
	public String verificationXmnd(String json, HttpServletRequest request)
			throws Exception {
		Connection conn = DBUtil.getConnection();
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		String resultVO = "false";
		XmxdkVO xvo = null;
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
					xvo= new XmxdkVO();
					xvo.setGc_tcjh_xmxdk_id(id[i]);
					xvo = (XmxdkVO)BaseDAO.getVOByPrimaryKey(conn,xvo);
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
//通过
	@Override
	public String xmSptg(String json, HttpServletRequest request)
			throws Exception {
		// TODO Auto-generated method stub
		Connection conn = DBUtil.getConnection();
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		String resultVO = null;
		TcjhSpVO tvo = null;
		try {
			conn.setAutoCommit(false);
			String id = request.getParameter("id");
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
			resultVO = tvo.getRowJson();
			conn.commit();
		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return resultVO;
	}
	
	private String getAllXm(Connection conn , String xmid) {
		String xmidList = "";
		try {
			String sql = "select gc_tcjh_xmxdk_id from GC_TCJH_XMXDK xdk where xdk.xmwybh in " 
					+ "(select xmwybh from GC_TCJH_XMXDK x0 where x0.gc_tcjh_xmxdk_id='"+xmid+"')";
			
			String[][] rs = DBUtil.query(conn, sql);
			
			if (rs != null) {
				for (int i = 0; i < rs.length; i++) {
					xmidList += "'" + rs[i][0] + "',";
				}
			}
			
			xmidList = xmidList.endsWith(",") ? xmidList.substring(0, xmidList.lastIndexOf(",")) : xmidList;
			if(Pub.empty(xmidList)){
				xmidList = "''";
			}
		} catch (Exception e) {
			e.printStackTrace(System.out);
		} finally {}
		
		return xmidList;
	}

	//jiangc start
	//修改最新数据标识
	public static String getid(String sjwybh,Connection conn) throws Exception{
		String result[][] = DBUtil.query("select gc_jh_sj_id from gc_jh_sj where  sjwybh='"+sjwybh+"' order by nd desc");
		String id="";
		if(null!=result&&result.length>0&&!Pub.empty(result[0][0]))
		{	
			id=result[0][0];
		}
		return id;
	}
	
	//为续建项目添加已完成的信息
	public static void settvo(TcjhVO tvo,TcjhVO tvo2){
		if(Pub.empty(String.valueOf(tvo2.getWgsj_sj())))  //实际开工时间
		{
			tvo.setKgsj(null);
			tvo.setIskgsj("1");
		}
		else
		{
			tvo.setKgsj(tvo2.getKgsj_sj());
			tvo.setIskgsj("2");
		}	
		
		if((!Pub.empty(String.valueOf(tvo2.getWgsj_sj()))&&tvo2.getIswgsj().equals("1"))||tvo2.getIswgsj().equals("2"))  //实际完工时间
		{
			tvo.setWgsj(tvo2.getWgsj_sj());
			tvo.setIswgsj("2");
		}
		
		if(((!Pub.empty(String.valueOf(tvo2.getKypf()))&&tvo2.getIskypf().equals("1")))||tvo2.getIskypf().equals("2"))  //可研批复
		{
			tvo.setKypf(tvo2.getKypf());
			tvo.setIskypf("2");
		}	

		if((!Pub.empty(String.valueOf(tvo2.getHpjds()))&&tvo2.getIshpjds().equals("1"))||tvo2.getIshpjds().equals("2"))  //划拔决定书
		{
			tvo.setHpjds(tvo2.getHpjds());
			tvo.setIshpjds("2");
		}	

		if((!Pub.empty(String.valueOf(tvo2.getGcxkz()))&&tvo2.getIsgcxkz().equals("1"))||tvo2.getIsgcxkz().equals("2"))  //工程规划许可证
		{
			tvo.setGcxkz(tvo2.getGcxkz());
			tvo.setIsgcxkz("2");
		}	

		if((!Pub.empty(String.valueOf(tvo2.getSgxk()))&&tvo2.getIssgxk().equals("1"))||tvo2.getIssgxk().equals("2"))  //施工许可
		{
			tvo.setSgxk(tvo2.getSgxk());
			tvo.setIssgxk("2");
		}	

		if((!Pub.empty(String.valueOf(tvo2.getCbsjpf()))&&tvo2.getIscbsjpf().equals("1"))||tvo2.getIscbsjpf().equals("2"))  //初步设计批复
		{
			tvo.setCbsjpf(tvo2.getCbsjpf());
			tvo.setIscbsjpf("2");
		}	

		if((!Pub.empty(String.valueOf(tvo2.getCqt()))&&tvo2.getIscqt().equals("1"))||tvo2.getIscqt().equals("2"))  //拆迁图
		{
			tvo.setCqt(tvo2.getCqt());
			tvo.setIscqt("2");
		}	

		if((!Pub.empty(String.valueOf(tvo2.getPqt()))&&tvo2.getIspqt().equals("1"))||tvo2.getIspqt().equals("2"))  //排迁图
		{
			tvo.setPqt(tvo2.getPqt());
			tvo.setIspqt("2");
		}	

		if((!Pub.empty(String.valueOf(tvo2.getSgt()))&&tvo2.getIssgt().equals("1"))||tvo2.getIssgt().equals("2"))  //施工图
		{
			tvo.setSgt(tvo2.getSgt());
			tvo.setIssgt("2");
		}	

		if((!Pub.empty(String.valueOf(tvo2.getTbj()))&&tvo2.getIstbj().equals("1"))||tvo2.getIstbj().equals("2"))  //提报价
		{
			tvo.setTbj(tvo2.getTbj());
			tvo.setIstbj("2");
		}	

		if((!Pub.empty(String.valueOf(tvo2.getCs()))&&tvo2.getIscs().equals("1"))||tvo2.getIscs().equals("2"))  //财审
		{
			tvo.setCs(tvo2.getCs());
			tvo.setIscs("2");
		}	

		if((!Pub.empty(String.valueOf(tvo2.getJldw()))&&tvo2.getIsjldw().equals("1"))||tvo2.getIsjldw().equals("2"))  //监理单位
		{
			tvo.setJldw(tvo2.getJldw());
			tvo.setIsjldw("2");
		}	

		if((!Pub.empty(String.valueOf(tvo2.getSgdw()))&&tvo2.getIssgdw().equals("1"))||tvo2.getIssgdw().equals("2"))  //施工单位
		{
			tvo.setSgdw(tvo2.getSgdw());
			tvo.setIssgdw("2");
		}	

		if((!Pub.empty(String.valueOf(tvo2.getZc()))&&tvo2.getIszc().equals("1"))||tvo2.getIszc().equals("2"))  //征拆
		{
			tvo.setZc(tvo2.getZc());
			tvo.setIszc("2");
		}	

		if((!Pub.empty(String.valueOf(tvo2.getPq()))&&tvo2.getIspq().equals("1"))||tvo2.getIspq().equals("2"))  //排迁
		{
			tvo.setPq(tvo2.getPq());
			tvo.setIspq("2");
		}	

		if((!Pub.empty(String.valueOf(tvo2.getJg()))&&tvo2.getIsjg().equals("1"))||tvo2.getIsjg().equals("2"))  //交工
		{
			tvo.setJg(tvo2.getJg());
			tvo.setIsjg("2");
		}		
	}
	
	
	//jiangc 查询标段
	public String query_bdid(String xmid) throws Exception {
		Connection conn = DBUtil.getConnection();
		String domresult="";
		try {
			String sql = "select gc_xmbd_id from gc_xmbd where xmid='"+xmid+"' and sfyx=1 ";
			String bdid[][] = DBUtil.query(conn, sql);
			if(null!=bdid&&bdid.length>2&&bdid[0][0]!=null)
			{		
				for(int i=0;i<bdid.length;i++)
				{
					domresult+=bdid[i][0]+",";
				}	
			}	
		}  catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}

	//查询被续建标段
	public String query_bdid_xj(String xmid) throws Exception {
		Connection conn = DBUtil.getConnection();
		String domresult="";
		try {
			String sql = "select gc_xmbd_zc_id from gc_xmbd_zc where xmid='"+xmid+"' and isgx=1 and sfyx=1 ";
			String bdid[][] = DBUtil.query(conn, sql);
			if(null!=bdid&&bdid.length>0&&bdid[0][0]!=null)
			{		
				for(int i=0;i<bdid.length;i++)
				{
					domresult+=bdid[i][0]+",";
				}	
			}	
		}  catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}

	@Override
	public String deleteSp(String json, HttpServletRequest request) {
		Connection conn = null;
		String domresult = "";
		XmxdkSpVO vo = null;
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
			String sql=" select gc_tcjh_xmxdk_sp_id from GC_TCJH_XMXDK_SP a where a.sfyx='1' and a.tcjhztid='"+spvo.getGc_tcjh_sp_id()+"' ";
			String xdkspid[][] = DBUtil.query(conn, sql);
			if(null!=xdkspid&&xdkspid.length>0&&xdkspid[0][0]!=null){
				for(int i=0;i<xdkspid.length;i++){
					vo = new XmxdkSpVO();
					vo.setGc_tcjh_xmxdk_sp_id(xdkspid[i][0]);
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

	//jiangc end
}
