package com.ccthanking.business.sjgl.zlsfgl;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import com.ccthanking.business.clzx.service.ClzxManagerService;
import com.ccthanking.business.clzx.service.impl.ClzxManagerServiceImpl;
import com.ccthanking.business.sjgl.sj.GcSjVO;
import com.ccthanking.business.sjgl.xgzllq.GcSjXgzlLqVO;
import com.ccthanking.business.tcjh.jhgl.vo.TcjhVO;
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
import com.ccthanking.framework.log.LogManager;
import com.ccthanking.framework.message.messagemgr.PushMessage;
import com.ccthanking.framework.util.DateTimeUtil;
import com.ccthanking.framework.util.Pub;
import com.ccthanking.framework.util.RandomGUID;
import com.ccthanking.framework.util.RequestUtil;


@Service
public class ZiLiaoShouFaServiceImpl implements ZiLiaoShouFaService {

	private String ywlx=YwlxManager.GC_SJ_ZLSF_JS;

	@Override
	public String queryConditionZiLiaoShouFa(String json,String sjwybh,String nd,User user) throws SQLException {
		
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		try {
				PageManager page = RequestUtil.getPageManager(json);
				String orderFilter = RequestUtil.getOrderFilter(json);
				String condition = RequestUtil.getConditionList(json).getConditionWhere();
				if(!Pub.empty(condition)){
					condition +=" and a.sjwybh=b.sjwybh  and  b.sjwybh='"+sjwybh+"' and  b.nd='"+nd+"'";
				}else{
					condition=" a.sjwybh=b.sjwybh b.sjwybh='"+sjwybh+"' and  b.nd='"+nd+"'";
				}
				condition += BusinessUtil.getSJYXCondition("B") + BusinessUtil.getCommonCondition(user,null);
				condition += BusinessUtil.getSJYXCondition("A") + BusinessUtil.getCommonCondition(user,null);
				condition += orderFilter;
				page.setFilter(condition);
				conn.setAutoCommit(false);
				String sql = "select  a.gc_sj_zlsf_js_id, b.jhid, b.nd, b.xmbh, b.xmmc, b.bdbh, b.bdmc, a.sjy, a.tzlb, a.fs, a.jsrq, a.jsr, a.ywlx, a.sjbh, a.bz, a.lrr, a.lrsj, a.lrbm, a.lrbmmc, a.gxr, a.gxsj, a.gxbm, a.gxbmmc, a.sjmj, a.sfyx from GC_SJ_ZLSF_JS a,gc_jh_sj b ";
				BaseResultSet bs = DBUtil.query(conn, sql, page);
				bs.setFieldDic("TZLB", "TZLB");
				bs.setFieldTranslater("JSR", "FS_ORG_PERSON", "ACCOUNT", "NAME");
				bs.setFieldTranslater("SJY", "GC_CJDW", "GC_CJDW_ID", "DWMC");
				
				domresult = bs.getJson();
				LogManager.writeUserLog(null, ywlx,
						Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS,
						user.getOrgDept().getDeptName() + " " + user.getName()
								+ "资料收发查询", user,"","");
			} catch (Exception e) {
				e.printStackTrace(System.out);
			
			} finally {
				DBUtil.closeConnetion(conn);
			}
			return domresult;
	}

	@Override
	public String insert(HttpServletRequest request,String json, User user) throws SQLException, Exception {
		Connection conn = DBUtil.getConnection();
		String resultVO = null;
		String xiaoxi;
		GcSjZlsfJsVO xmvo = new GcSjZlsfJsVO();
		TcjhVO jh= new TcjhVO();
		try {
				conn.setAutoCommit(false);
				JSONArray list = xmvo.doInitJson(json);
				xmvo.setValueFromJson((JSONObject)list.get(0));
				jh.setValueFromJson((JSONObject)list.get(0));
				//推送消息判断
				if(Pub.empty(jh.getBdid()))
	        	{
	    		    xiaoxi="项目："+jh.getXmmc()+"";
	        	}
	        	else
	        	{
	        		xiaoxi="项目："+jh.getXmmc()+"  标段："+jh.getBdmc()+"";
	        	}
				xmvo.setGc_sj_zlsf_js_id(new RandomGUID().toString()); // 主键
				BusinessUtil.setInsertCommonFields(xmvo, user);//公共字段插入
				xmvo.setYwlx(ywlx);//业务类型
				EventVO eventVO = EventManager.createEvent(conn, xmvo.getYwlx(), user);//生成事件
				xmvo.setSjbh(eventVO.getSjbh());
				//插入
			
				BaseDAO.insert(conn, xmvo);
				resultVO = xmvo.getRowJson();
				
				//更新设计表的完成时间
				GcSjVO sjvo=new GcSjVO();
				sjvo.setSjwybh(xmvo.getSjwybh());

        		ClzxManagerService cms = new ClzxManagerServiceImpl();
				BaseVO[] vs=BaseDAO.getVOByCondition(conn, sjvo);
				if( vs!=null)
				{
					for(int i = 0 ;i<vs.length;i++)
					{
						
						GcSjVO sjvo1 = (GcSjVO)vs[i];
						sjvo1.setJhid(xmvo.getJhid());
						sjvo1.setSjwybh(xmvo.getSjwybh());
						sjvo1.setJhsjid(xmvo.getJhsjid());
						sjvo1.setNd(xmvo.getNd());
						sjvo1.setXmid(xmvo.getXmid());
						sjvo1.setBdid(xmvo.getBdid());
						int a =Integer.parseInt(xmvo.getTzlb());
						switch (a) {
						case 0:
							// sjvo1.setWcsj_cqt(xmvo.getJsrq());
							 //信息推送
							 PushMessage.push(conn, request, PushMessage.ZLSF_GHTJ,""+xiaoxi+" 。规划条件已到，请相关负责人领取。",null,xmvo.getSjbh(),xmvo.getYwlx(),xmvo.getGc_sj_zlsf_js_id(),"消息提示");
							 break;
                        case 1:
                        	// sjvo1.setWcsj_pqt(xmvo.getJsrq());
                        	 //信息推送
							 PushMessage.push(conn, request, PushMessage.ZLSF_GASJ,""+xiaoxi+" 。方案设计已到，请相关负责人领取。",null,xmvo.getSjbh(),xmvo.getYwlx(),xmvo.getGc_sj_zlsf_js_id(),"消息提示");
							 break;
                        case 2:
                        	 sjvo1.setWcsj_kcbg(xmvo.getJsrq());
                        	 //信息推送
							 PushMessage.push(conn, request, PushMessage.ZLSF_KCZL,""+xiaoxi+" 。勘察报告已到，请相关负责人领取。",null,xmvo.getSjbh(),xmvo.getYwlx(),xmvo.getGc_sj_zlsf_js_id(),"消息提示");
	                         break;
                        case 3:
                        	 sjvo1.setWcsj_cqt(xmvo.getJsrq());
                     		//-------------加入处理中心任务生成代码-------------BEGIN---------
                     		cms.createTask("1007003", xmvo.getJhsjid(), user,conn);
                     		//-------------加入处理中心任务生成代码-------------END---------
                        	 //信息推送
							 PushMessage.push(conn, request, PushMessage.ZLSF_CQT,""+xiaoxi+" 。拆迁图已到，请相关负责人领取。",null,xmvo.getSjbh(),xmvo.getYwlx(),xmvo.getGc_sj_zlsf_js_id(),"消息提示");
	                         break;
                        case 4:
                       	     sjvo1.setWcsj_pqt(xmvo.getJsrq());
                     		//-------------加入处理中心任务生成代码-------------BEGIN---------
                     		cms.createTask("1007005", xmvo.getJhsjid(), user,conn);
                     		//-------------加入处理中心任务生成代码-------------END---------
                        	 //信息推送
							 PushMessage.push(conn, request, PushMessage.ZLSF_PQT,""+xiaoxi+" 。排迁图已到，请相关负责人领取。",null,xmvo.getSjbh(),xmvo.getYwlx(),xmvo.getGc_sj_zlsf_js_id(),"消息提示");
	                         break;
                        case 5:
                       	     sjvo1.setWcsj_sgt_ssb(xmvo.getJsrq());
                        	 //信息推送
							 PushMessage.push(conn, request, PushMessage.ZLSF_SGT_SS,""+xiaoxi+" 。施工图（送审版）已到，请相关负责人领取。",null,xmvo.getSjbh(),xmvo.getYwlx(),xmvo.getGc_sj_zlsf_js_id(),"消息提示");
	                         break;
                        case 6:
                       	    // sjvo1.setWcsj_kcbg(xmvo.getJsrq());
                       	     //信息推送
							 PushMessage.push(conn, request, PushMessage.ZLSF_SGTSCBG,""+xiaoxi+" 。施工图审查报告已到，请相关负责人领取。",null,xmvo.getSjbh(),xmvo.getYwlx(),xmvo.getGc_sj_zlsf_js_id(),"消息提示");
	                         break;
                        case 7:
                      	     sjvo1.setWcsj_sgt_zsb(xmvo.getJsrq());
                     		//-------------加入处理中心任务生成代码-------------BEGIN---------
                     		cms.createTask("1007007", xmvo.getJhsjid(), user,conn);
                     		//-------------加入处理中心任务生成代码-------------END---------
                      	    // 当施工图（正式版）已到，发送给流程发起人提示信息。
							PushMessage.push(conn, request, PushMessage.ZLSF_SGT_ZS,""+xiaoxi+" 。施工图（正式版）已到，请相关负责人领取。",null,xmvo.getSjbh(),xmvo.getYwlx(),xmvo.getGc_sj_zlsf_js_id());
	                        break;    
						default:
							break;
						}
						
						BusinessUtil.setUpdateCommonFields(sjvo1, user);
					    //EventVO eventVO1 = EventManager.createEvent(conn, sjvo1.getYwlx(), user);//生成事件
						//sjvo1.setSjbh(eventVO1.getSjbh());
						BaseDAO.update(conn, sjvo1);
						 
					}
				}
				else
				{
					GcSjVO sjvo2=new GcSjVO();
					sjvo2.setGc_sj_id(new RandomGUID().toString()); // 主键
					sjvo2.setJhid(xmvo.getJhid());
					sjvo2.setSjwybh(xmvo.getSjwybh());
					sjvo2.setJhsjid(xmvo.getJhsjid());
					sjvo2.setNd(xmvo.getNd());
					sjvo2.setXmid(xmvo.getXmid());
					sjvo2.setBdid(xmvo.getBdid());
					int a =Integer.parseInt(xmvo.getTzlb());
					switch (a) {
					case 0:
						// sjvo1.setWcsj_cqt(xmvo.getJsrq());
						 //信息推送
						 PushMessage.push(conn, request, PushMessage.ZLSF_GHTJ,""+xiaoxi+" 。规划条件已到，请相关负责人领取。",null,xmvo.getSjbh(),xmvo.getYwlx(),xmvo.getGc_sj_zlsf_js_id(),"消息提示");
						 break;
                    case 1:
                    	// sjvo1.setWcsj_pqt(xmvo.getJsrq());
                    	 //信息推送
						 PushMessage.push(conn, request, PushMessage.ZLSF_GASJ,""+xiaoxi+" 。方案设计已到，请相关负责人领取。",null,xmvo.getSjbh(),xmvo.getYwlx(),xmvo.getGc_sj_zlsf_js_id(),"消息提示");
						 break;
                    case 2:
                    	sjvo2.setWcsj_kcbg(xmvo.getJsrq());
                    	 //信息推送
						 PushMessage.push(conn, request, PushMessage.ZLSF_KCZL,""+xiaoxi+" 。勘察报告已到，请相关负责人领取。",null,xmvo.getSjbh(),xmvo.getYwlx(),xmvo.getGc_sj_zlsf_js_id(),"消息提示");
                         break;
                    case 3:
                    	sjvo2.setWcsj_cqt(xmvo.getJsrq());
                		//-------------加入处理中心任务生成代码-------------BEGIN---------
                		cms.createTask("1007003", xmvo.getJhsjid(), user,conn);
                		//-------------加入处理中心任务生成代码-------------END---------
                    	 //信息推送
						 PushMessage.push(conn, request, PushMessage.ZLSF_CQT,""+xiaoxi+" 。拆迁图已到，请相关负责人领取。",null,xmvo.getSjbh(),xmvo.getYwlx(),xmvo.getGc_sj_zlsf_js_id(),"消息提示");
                         break;
                    case 4:
                    	sjvo2.setWcsj_pqt(xmvo.getJsrq());
                		//-------------加入处理中心任务生成代码-------------BEGIN---------
                		cms.createTask("1007005", xmvo.getJhsjid(), user,conn);
                		//-------------加入处理中心任务生成代码-------------END---------
                    	 //信息推送
						 PushMessage.push(conn, request, PushMessage.ZLSF_PQT,""+xiaoxi+" 。排迁图已到，请相关负责人领取。",null,xmvo.getSjbh(),xmvo.getYwlx(),xmvo.getGc_sj_zlsf_js_id(),"消息提示");
                         break;
                    case 5:
                    	sjvo2.setWcsj_sgt_ssb(xmvo.getJsrq());
                    	 //信息推送
						 PushMessage.push(conn, request, PushMessage.ZLSF_SGT_SS,""+xiaoxi+" 。施工图（送审版）已到，请相关负责人领取。",null,xmvo.getSjbh(),xmvo.getYwlx(),xmvo.getGc_sj_zlsf_js_id(),"消息提示");
                         break;
                    case 6:
                   	    // sjvo1.setWcsj_kcbg(xmvo.getJsrq());
                   	     //信息推送
						 PushMessage.push(conn, request, PushMessage.ZLSF_SGTSCBG,""+xiaoxi+" 。施工图审查报告已到，请相关负责人领取。",null,xmvo.getSjbh(),xmvo.getYwlx(),xmvo.getGc_sj_zlsf_js_id(),"消息提示");
                         break;
                    case 7:
                    	sjvo2.setWcsj_sgt_zsb(xmvo.getJsrq());
                		//-------------加入处理中心任务生成代码-------------BEGIN---------
                		cms.createTask("1007007", xmvo.getJhsjid(), user,conn);
                		//-------------加入处理中心任务生成代码-------------END---------
                  	    //当施工图（正式版）已到，发送给流程发起人提示信息。
						PushMessage.push(conn, request, PushMessage.ZLSF_SGT_ZS,""+xiaoxi+" 。施工图（正式版）已到，请相关负责人领取。",null,xmvo.getSjbh(),xmvo.getYwlx(),xmvo.getGc_sj_zlsf_js_id(),"消息提示");
                        break;    
					default:
						break;
					}
					BusinessUtil.setInsertCommonFields(sjvo2, user);
					sjvo2.setYwlx(YwlxManager.GC_SJ);
					EventVO eventVO4 = EventManager.createEvent(conn, sjvo2.getYwlx(), user);//生成事件
					sjvo2.setSjbh(eventVO4.getSjbh());
					BaseDAO.insert(conn, sjvo2);
				}
				
				
				conn.commit();
				LogManager.writeUserLog(xmvo.getSjbh(), xmvo.getYwlx(),
						Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
						user.getOrgDept().getDeptName() + " " + user.getName()
								+ "资料收取新增成功", user,"","");
		
				} catch (Exception e) {
					DBUtil.rollbackConnetion(conn);
					e.printStackTrace(System.out);
				} finally {
					DBUtil.closeConnetion(conn);
				}
		String jsona="{querycondition: {conditions: [{\"value\":\""+xmvo.getGc_sj_zlsf_js_id()+"\",\"fieldname\":\"gc_sj_zlsf_js_id\",\"operation\":\"=\",\"logic\":\"and\"} ]}}";
		String resultXinXi=this.queryConditionZiLiaoShouFa(jsona,xmvo.getSjwybh(),xmvo.getNd(),user);
		return resultXinXi;
	}
	@Override
	public String updatedemo(HttpServletRequest request,String json, User user) throws SQLException,Exception {
		Connection conn = DBUtil.getConnection();
		String resultVO = null;
		GcSjZlsfJsVO xmvo = new GcSjZlsfJsVO();
		try {
			    conn.setAutoCommit(false);
				JSONArray list = xmvo.doInitJson(json);
				xmvo.setValueFromJson((JSONObject)list.get(0));
				BusinessUtil.setUpdateCommonFields(xmvo, user);//公共字段更新
				//插入
				BaseDAO.update(conn, xmvo);
				resultVO = xmvo.getRowJson();
				
				
				GcSjVO sjvo=new GcSjVO();
        		ClzxManagerService cms = new ClzxManagerServiceImpl();
				sjvo.setSjwybh(xmvo.getSjwybh());
				
				BaseVO[] vs=BaseDAO.getVOByCondition(conn, sjvo);
				if( vs!=null)
				{
					for(int i = 0 ;i<vs.length;i++)
					{
						GcSjVO sjvo1 = (GcSjVO)vs[i];
						sjvo1.setJhid(xmvo.getJhid());
						sjvo1.setJhsjid(xmvo.getJhsjid());
						sjvo1.setNd(xmvo.getNd());
						sjvo1.setXmid(xmvo.getXmid());
						sjvo1.setSjwybh(xmvo.getSjwybh());
						sjvo1.setBdid(xmvo.getBdid());
						int a =Integer.parseInt(xmvo.getTzlb());
						String sql = "select to_char(CQT_SJ,'yyyy-MM-dd'),to_char(PQT_SJ,'yyyy-MM-dd'),to_char(SGT_SJ,'yyyy-MM-dd') from GC_JH_SJ where GC_JH_SJ_ID='"+xmvo.getJhsjid()+"'";
						String arr[][] = DBUtil.query(conn, sql);
						switch (a) {
						case 3:
	                		//-------------加入处理中心任务生成代码-------------BEGIN---------
							if(arr!=null && arr.length!=0 && xmvo.getJsrq()!=null){
								String jsrq = DateTimeUtil.getDateTime(xmvo.getJsrq(),"yyyy-MM-dd");
								if(arr[0][0]!=null && !jsrq.equals(arr[0][0])){
			                		cms.createTask("1007003", xmvo.getJhsjid(), user,conn);
								}
							}
	                		//-------------加入处理中心任务生成代码-------------END---------
							sjvo1.setWcsj_cqt(xmvo.getJsrq());
							break;
	                    case 4:
	                		//-------------加入处理中心任务生成代码-------------BEGIN---------
	                    	if(arr!=null && arr.length!=0 && xmvo.getJsrq()!=null){
								String jsrq = DateTimeUtil.getDateTime(xmvo.getJsrq(),"yyyy-MM-dd");
								if(arr[0][0]!=null && !jsrq.equals(arr[0][1])){
									cms.createTask("1007005", xmvo.getJhsjid(), user,conn);
								}
	                    	}
	                		//-------------加入处理中心任务生成代码-------------END---------
	                    	sjvo1.setWcsj_pqt(xmvo.getJsrq());
							break;
	                    case 5:
	                    	sjvo1.setWcsj_sgt_ssb(xmvo.getJsrq());
	                         break;
	                    case 7:
	                		//-------------加入处理中心任务生成代码-------------BEGIN---------
	                    	if(arr!=null && arr.length!=0 && xmvo.getJsrq()!=null){
								String jsrq = DateTimeUtil.getDateTime(xmvo.getJsrq(),"yyyy-MM-dd");
								if(arr[0][0]!=null && !jsrq.equals(arr[0][2])){
									cms.createTask("1007007", xmvo.getJhsjid(), user,conn);
								}
	                    	}
	                		//-------------加入处理中心任务生成代码-------------END---------
	                    	sjvo1.setWcsj_sgt_zsb(xmvo.getJsrq());
	                         break;
	                    case 2:
	                    	sjvo1.setWcsj_kcbg(xmvo.getJsrq());
	                         break;   
						default:
							break;
						}
						BusinessUtil.setUpdateCommonFields(sjvo1, user);
						BaseDAO.update(conn, sjvo1);
					}
				}
				else
				{
					GcSjVO sjvo2=new GcSjVO();
					sjvo2.setGc_sj_id(new RandomGUID().toString()); // 主键
					sjvo2.setJhid(xmvo.getJhid());
					sjvo2.setJhsjid(xmvo.getJhsjid());
					sjvo2.setNd(xmvo.getNd());
					sjvo2.setXmid(xmvo.getXmid());
					sjvo2.setBdid(xmvo.getBdid());
					sjvo2.setSjwybh(xmvo.getSjwybh());
					int a =Integer.parseInt(xmvo.getTzlb());
					switch (a) {
					case 3:
                		//-------------加入处理中心任务生成代码-------------BEGIN---------
                		cms.createTask("1007003", xmvo.getJhsjid(), user,conn);
                		//-------------加入处理中心任务生成代码-------------END---------
						sjvo2.setWcsj_cqt(xmvo.getJsrq());
						break;
                    case 4:
                		//-------------加入处理中心任务生成代码-------------BEGIN---------
                		cms.createTask("1007005", xmvo.getJhsjid(), user,conn);
                		//-------------加入处理中心任务生成代码-------------END---------
                    	sjvo2.setWcsj_pqt(xmvo.getJsrq());
						break;
                    case 5:
                    	sjvo2.setWcsj_sgt_ssb(xmvo.getJsrq());
                         break;
                    case 7:
                		//-------------加入处理中心任务生成代码-------------BEGIN---------
                		cms.createTask("1007007", xmvo.getJhsjid(), user,conn);
                		//-------------加入处理中心任务生成代码-------------END---------
                    	sjvo2.setWcsj_sgt_zsb(xmvo.getJsrq());
                         break;
                    case 2:
                    	sjvo2.setWcsj_kcbg(xmvo.getJsrq());
                         break;   
					default:
						break;
					}
					BusinessUtil.setInsertCommonFields(sjvo2, user);
					sjvo2.setYwlx(YwlxManager.GC_SJ);
					BaseDAO.insert(conn, sjvo2);
				}
				//根据ID查YWID和SJBH
				GcSjZlsfJsVO vo1 =(GcSjZlsfJsVO) BaseDAO.getVOByPrimaryKey(conn, xmvo);
				conn.commit();
				LogManager.writeUserLog(vo1.getSjbh(), vo1.getYwlx(),
						Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_SUCCESS,
						user.getOrgDept().getDeptName() + " " + user.getName()
								+ "资料收取修改成功", user,"","");
		
				} catch (Exception e) {
					DBUtil.rollbackConnetion(conn);
					e.printStackTrace(System.out);
				} finally {
					DBUtil.closeConnetion(conn);
				}
		String jsona="{querycondition: {conditions: [{\"value\":\""+xmvo.getGc_sj_zlsf_js_id()+"\",\"fieldname\":\"gc_sj_zlsf_js_id\",\"operation\":\"=\",\"logic\":\"and\"} ]}}";
		String resultXinXi=this.queryConditionZiLiaoShouFa(jsona,xmvo.getSjwybh(),xmvo.getNd(),user);
		return resultXinXi;
	}

	@Override
	public String insertLingQu(String json, User user) throws SQLException,Exception 
	{
		Connection conn = DBUtil.getConnection();
		String resultVO = null;
		GcSjXgzlLqVO xmvo = new GcSjXgzlLqVO();

		try {
				conn.setAutoCommit(false);
				JSONArray list = xmvo.doInitJson(json);
				xmvo.setValueFromJson((JSONObject)list.get(0));
				xmvo.setGc_sj_xgzl_lq_id(new RandomGUID().toString()); // 主键
				BusinessUtil.setInsertCommonFields(xmvo, user);//公共字段插入
				xmvo.setYwlx(ywlx);//业务类型
				xmvo.setZllb("01");
				EventVO eventVO = EventManager.createEvent(conn, xmvo.getYwlx(), user);//生成事件
				xmvo.setSjbh(eventVO.getSjbh());
				//插入
				BaseDAO.insert(conn, xmvo);
				resultVO = xmvo.getRowJson();
				conn.commit();
				GcSjXgzlLqVO xmvo1=(GcSjXgzlLqVO) BaseDAO.getVOByPrimaryKey(conn,xmvo);
				resultVO=xmvo1.getRowJson();
				LogManager.writeUserLog(xmvo.getSjbh(), xmvo.getYwlx(),
						Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
						user.getOrgDept().getDeptName() + " " + user.getName()
								+ "资料收发领取新增成功", user,"","");
		
				} catch (Exception e) {
					DBUtil.rollbackConnetion(conn);
					e.printStackTrace(System.out);
				} finally {
					DBUtil.closeConnetion(conn);
				}
				return resultVO;
	}

	@Override
	public String updateLingQu(String json, User user) throws SQLException,Exception 
	{
		Connection conn = DBUtil.getConnection();
		String resultVO = null;
		GcSjXgzlLqVO xmvo = new GcSjXgzlLqVO();

		try {
				conn.setAutoCommit(false);
				JSONArray list = xmvo.doInitJson(json);
				xmvo.setValueFromJson((JSONObject)list.get(0));
				BusinessUtil.setUpdateCommonFields(xmvo, user);//公共字段更新
				//插入
				BaseDAO.update(conn, xmvo);
				resultVO = xmvo.getRowJson();
				
				GcSjXgzlLqVO xmvo1=(GcSjXgzlLqVO) BaseDAO.getVOByPrimaryKey(conn,xmvo);
				resultVO=xmvo1.getRowJson();
			
				conn.commit();
				
				LogManager.writeUserLog(xmvo1.getSjbh(), xmvo1.getYwlx(),
						Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_SUCCESS,
						user.getOrgDept().getDeptName() + " " + user.getName()
								+ "资料收发领取修改成功", user,"","");
		
				} catch (Exception e) {
					DBUtil.rollbackConnetion(conn);
					e.printStackTrace(System.out);
				} finally {
					DBUtil.closeConnetion(conn);
				}
				return resultVO;
	}

	@Override
	public String queryConditionShouQu(String json, String ZLLJSDID,User user) throws SQLException {
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		try {
				PageManager page = RequestUtil.getPageManager(json);
				String orderFilter = RequestUtil.getOrderFilter(json);
				String condition = RequestUtil.getConditionList(json).getConditionWhere();
				if(!Pub.empty(condition)){
					condition +=" and ZLLB='01' and ZLLJSD='"+ZLLJSDID+"'";
					
				}else{
					condition=" ZLLB='01' and ZLLJSD='"+ZLLJSDID+"'";
				}
				condition += BusinessUtil.getSJYXCondition(null) + BusinessUtil.getCommonCondition(user,null);
				condition += orderFilter;
				page.setFilter(condition);
				conn.setAutoCommit(false);
				String sql = "select gc_sj_xgzl_lq_id, zlljsd, zllb, fs, lqbm, lqrq, lqr, blr, ywlx, sjbh, bz, lrr, lrsj, lrbm, lrbmmc, gxr, gxsj, gxbm, gxbmmc, sjmj, sfyx from GC_SJ_XGZL_LQ";
				BaseResultSet bs = DBUtil.query(conn, sql, page);
				 bs.setFieldTranslater("LQR", "FS_ORG_PERSON", "ACCOUNT", "NAME");
				 bs.setFieldTranslater("LQBM", "FS_ORG_DEPT", "ROW_ID", "DEPT_NAME");
				bs.setFieldTranslater("BLR", "FS_ORG_PERSON", "ACCOUNT", "NAME");
				domresult = bs.getJson();
				LogManager.writeUserLog(null, ywlx,
						Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS,
						user.getOrgDept().getDeptName() + " " + user.getName()
								+ "资料收发领取查询成功", user,"","");
			} catch (Exception e) {
				e.printStackTrace(System.out);
			
			} finally {
				DBUtil.closeConnetion(conn);
			}
			return domresult;
	}

	@Override
	public String deleteJS(HttpServletRequest request, String json) {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		Connection conn = DBUtil.getConnection();
		String resultVO = null;
		GcSjZlsfJsVO vo = new GcSjZlsfJsVO();

		try {
			conn.setAutoCommit(false);
			JSONArray list = vo.doInitJson(json);
			vo.setValueFromJson((JSONObject)list.get(0));
			BusinessUtil.setUpdateCommonFields(vo, user);
			//置成失效
	        vo.setSfyx("0");
			//插入
			BaseDAO.update(conn, vo);
			resultVO = vo.getRowJson();
			GcSjXgzlLqVO volq = new GcSjXgzlLqVO();
			volq.setZlljsd(vo.getGc_sj_zlsf_js_id());
			BaseVO[] lqs=(BaseVO[]) BaseDAO.getVOByCondition(conn, volq);
			if(null!=lqs)
			{
				for(int i=0;i<lqs.length;i++)
				{
					GcSjXgzlLqVO lqvo=(GcSjXgzlLqVO) lqs[i];
					lqvo.setSfyx("0");
					BaseDAO.update(conn, lqvo);
				}
			}
			
			updateSJ(request,vo.getSjwybh(),vo.getTzlb(),conn);
			conn.commit();
			LogManager.writeUserLog(vo.getSjbh(), ywlx,
					Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "删除设计文件管理（接收）信息成功", user,"","");
	
			} catch (Exception e) {
				DBUtil.rollbackConnetion(conn);
			} finally {
				DBUtil.closeConnetion(conn);
			}
		return resultVO;
	}
	public String updateSJ(HttpServletRequest request,String sjwybh,String lb,Connection conn) throws Exception{
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		
		String sjbh="";
		String jsrq="";
		Date jsrq1=null;
		//设计文件
		GcSjZlsfJsVO xmvo = new GcSjZlsfJsVO();
		try {
		xmvo.setSjwybh(sjwybh);
		BaseVO[] vszlsf=BaseDAO.getVOByCondition(conn, xmvo);
		xmvo=(GcSjZlsfJsVO) vszlsf[0];
		//根据jhsjid和类别查出最大接收时间
		String sql=" select max(a.jsrq) zxjsrq from GC_SJ_ZLSF_JS a where a.sjwybh='"+sjwybh+"' and  tzlb='"+lb+"' and sfyx='1' ";
		String[][] zxjsrq=DBUtil.query(conn,sql);
		//将时间格式为时间
		if(!Pub.empty(zxjsrq[0][0])){
			 jsrq=zxjsrq[0][0];
			 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			  jsrq1 = sdf.parse(jsrq); 
		}
		//操作设计表
		GcSjVO sjvo=new GcSjVO();
		sjvo.setSjwybh(sjwybh);
		BaseVO[] vs=BaseDAO.getVOByCondition(conn, sjvo);
		//设计表不为空 做修改
		if( vs!=null)
		{
			for(int i = 0 ;i<vs.length;i++)
			{
				GcSjVO sjvo1 = (GcSjVO)vs[i];
				int a =Integer.parseInt(lb);
				switch (a) {
				case 3:
					sjvo1.setWcsj_cqt(jsrq1);
					break;
                case 4:
                	sjvo1.setWcsj_pqt(jsrq1);
					break;
                case 5:
                	sjvo1.setWcsj_sgt_ssb(jsrq1);
                     break;
                case 7:
                	sjvo1.setWcsj_sgt_zsb(jsrq1);
                     break;
                case 2:
                	sjvo1.setWcsj_kcbg(jsrq1);
                     break;   
				default:
					break;
				}
				BusinessUtil.setUpdateCommonFields(sjvo1, user);
				BaseDAO.update(conn, sjvo1);
			}
		}
		//设计表为空 做添加
		else
		{
			GcSjVO sjvo2=new GcSjVO();
			sjvo2.setGc_sj_id(new RandomGUID().toString()); // 主键
			sjvo2.setJhid(xmvo.getJhid());
			sjvo2.setJhsjid(xmvo.getJhsjid());
			sjvo2.setNd(xmvo.getNd());
			sjvo2.setXmid(xmvo.getXmid());
			sjvo2.setBdid(xmvo.getBdid());
			int a =Integer.parseInt(xmvo.getTzlb());
			switch (a) {
			case 3:
				sjvo2.setWcsj_cqt(jsrq1);
				break;
            case 4:
            	sjvo2.setWcsj_pqt(jsrq1);
				break;
            case 5:
            	sjvo2.setWcsj_sgt_ssb(jsrq1);
                 break;
            case 7:
            	sjvo2.setWcsj_sgt_zsb(jsrq1);
                 break;
            case 2:
            	sjvo2.setWcsj_kcbg(jsrq1);
                 break;   
			default:
				break;
			}
			BusinessUtil.setInsertCommonFields(sjvo2, user);
			sjvo2.setYwlx(YwlxManager.GC_SJ);
			EventVO eventVO4 = EventManager.createEvent(conn, sjvo2.getYwlx(), user);//生成事件
			sjvo2.setSjbh(eventVO4.getSjbh());
			BaseDAO.insert(conn, sjvo2);
		}
		LogManager.writeUserLog(sjbh, ywlx,
				Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_SUCCESS,
				user.getOrgDept().getDeptName() + " " + user.getName()
						+ "设计管理修改信息成功", user,"","");

		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return "";
	}
	@Override
	public String deleteLQ(HttpServletRequest request, String json) {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		Connection conn = DBUtil.getConnection();
		String resultVO = null;
		GcSjXgzlLqVO vo = new GcSjXgzlLqVO();

		try {
			conn.setAutoCommit(false);
			JSONArray list = vo.doInitJson(json);
			vo.setValueFromJson((JSONObject)list.get(0));
			BusinessUtil.setUpdateCommonFields(vo, user);
			//置成失效
	        vo.setSfyx("0");
			//插入
			BaseDAO.update(conn, vo);
			resultVO = vo.getRowJson();
			conn.commit();
			LogManager.writeUserLog(vo.getSjbh(), ywlx,
					Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "删除文件管理（领取）信息成功", user,"","");
	
			} catch (Exception e) {
				DBUtil.rollbackConnetion(conn);
			} finally {
				DBUtil.closeConnetion(conn);
			}
		return resultVO;
	}

	@Override
	public String jSLQquery(String json, String JHSJID, User user) {
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		try {
				PageManager page = RequestUtil.getPageManager(json);
				String orderFilter = RequestUtil.getOrderFilter(json);
				String condition = RequestUtil.getConditionList(json).getConditionWhere();
				if(!Pub.empty(condition)){
					condition +=" and a.jhsjid=b.gc_jh_sj_id   and a.gc_sj_zlsf_js_id=c.zlljsd(+)  and  b.gc_jh_sj_id='"+JHSJID+"'";
				}else{
					condition=" a.jhsjid=b.gc_jh_sj_id  and a.gc_sj_zlsf_js_id=c.zlljsd(+) and b.gc_jh_sj_id='"+JHSJID+"'";
				}
				condition += BusinessUtil.getSJYXCondition("B") + BusinessUtil.getCommonCondition(user,null);
				condition += BusinessUtil.getSJYXCondition("A") + BusinessUtil.getCommonCondition(user,null);
				condition += orderFilter;
				page.setFilter(condition);
				conn.setAutoCommit(false);
				String sql = "select  a.gc_sj_zlsf_js_id, b.jhid, b.nd, b.xmbh, b.xmmc, b.bdbh, b.bdmc, a.sjy, a.tzlb, a.fs, a.jsrq, a.jsr, a.ywlx, a.sjbh, a.bz, a.lrr, a.lrsj, a.lrbm, a.lrbmmc, a.gxr, a.gxsj, a.gxbm, a.gxbmmc, a.sjmj, a.sfyx, C.LQRQ, C.LQBM,  C.LQR " +
							"  from GC_SJ_ZLSF_JS a,gc_jh_sj b,(select * from GC_SJ_XGZL_LQ a where a.sfyx='1' ) c ";
				BaseResultSet bs = DBUtil.query(conn, sql, page);
				bs.setFieldDic("TZLB", "TZLB");
				bs.setFieldTranslater("JSR", "FS_ORG_PERSON", "ACCOUNT", "NAME");
				bs.setFieldTranslater("SJY", "GC_CJDW", "GC_CJDW_ID", "DWMC");
				bs.setFieldTranslater("LQR", "FS_ORG_PERSON", "ACCOUNT", "NAME");
				bs.setFieldTranslater("LQBM", "FS_ORG_DEPT", "ROW_ID", "DEPT_NAME");
				
				
				domresult = bs.getJson();
				LogManager.writeUserLog(null, ywlx,
						Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS,
						user.getOrgDept().getDeptName() + " " + user.getName()
								+ "设计文件导出查询", user,"","");
			} catch (Exception e) {
				e.printStackTrace(System.out);
			
			} finally {
				DBUtil.closeConnetion(conn);
			}
			return domresult;
	}
}
