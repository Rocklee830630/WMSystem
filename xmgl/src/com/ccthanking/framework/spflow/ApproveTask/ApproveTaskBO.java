package com.ccthanking.framework.spflow.ApproveTask;

import java.util.*;
import org.dom4j.*;

import java.sql.Connection;

import com.ccthanking.common.EventManager;
import com.ccthanking.common.vo.EventVO;
import com.ccthanking.framework.base.BaseBO;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.util.QueryConditionList;
import com.ccthanking.framework.common.PageManager;
import com.ccthanking.framework.util.Pub;
import com.ccthanking.framework.Constants;
import com.ccthanking.framework.common.BaseResultSet;
import com.ccthanking.framework.common.QuerySet;
import com.ccthanking.framework.wsdy.PubWS;
import com.ccthanking.framework.coreapp.aplink.*;
import com.ccthanking.framework.coreapp.aplink.Process;
import com.ccthanking.framework.coreapp.aplink.impl.ApproveDesc;

/**
 *
 * @author xukx
 * @des 重写TaskBO的getApproveTaskList方法,审批归档通用类,误删
 */
public class ApproveTaskBO extends BaseBO {
	public ApproveTaskBO(User userObj) {

	}

	// 获取dah
	public static String getDAH(String sjbh, String ywlx) throws Exception {
		String dah = "";
        // modified by guanchb 2008-05-08 start
        // 增加按照作废标识进行过滤
		String strSql = " select dah from pub_blob where SJBH=? and YWLX=? and (ZFBS = '0' or ZFBS IS NULL) and rownum<2 order by dah asc";
		// modified by guanchb 2008-05-08 end;
        PageManager page = new PageManager();
		Connection conn = DBUtil.getConnection();
        QuerySet qs = null;
        Object[] objs = null;
		try {
			conn.setAutoCommit(false);
			objs = new Object[2];
			objs[0] = sjbh;
			objs[1] = ywlx;
			qs = DBUtil.executeQuery(strSql, objs,conn);
			if (qs.getRowCount() > 0)
				dah = qs.getString(1, "DAH");
			conn.commit();
			page = null;
			qs = null;
            objs = null;
		} catch (Exception e)
        {
			conn.rollback();
            System.out.println("获取档案号ApproveTaskBO.getDAH方法抛出异常："+e.getMessage());
			throw e;
		} finally
        {
			if (conn != null) {
				conn.close();
			}
		}
		return dah;
	}

	public Document getApproveTaskList(Object obj, QueryConditionList list,
			PageManager page) throws Exception {
		String conditionAll = list == null ? "" : list.getConditionWhere();
		String condition = "";
		String strXM = "";
		if (!Pub.empty(conditionAll)) {
			condition = conditionAll.split(":")[0];
			if (conditionAll.split(":").length > 1) {
				strXM = conditionAll.split(":")[1];
			}
		} else {
			condition = " rownum < " + Constants.MAX_RECORD_LIMITED;
		}
		if (page == null) {
			page = new PageManager();
		}
		page.setFilter(condition);
		Document docRes = null;
		Connection conn = DBUtil.getConnection();
		try {
			conn.setAutoCommit(false);
			String sql = "select '['||id||'-'||seq||'] '||memo memo,"
					+ "RESULTDSCR,'wstemplateid' as wstemplateid,'' as gzxx,'0' as processtype,id,seq,sjbh,ywlx,rwzt,rwlx,spbh,"// 增加伪列wsprint用于显示文书
					+ " cjrxm,cjdwdm,cjsj,DBDWDM,dbrole,dbryid,spjg,spyj,spr,hh,'' as fjbh,linkurl as linkurl,'' as dah  from ap_task_schedule ";// 增加伪列fjbh用于print文书
			BaseResultSet bset = DBUtil.query(conn, sql, page);
			bset.setFieldDic("result", "SPJG");
			bset.setFieldDic("spjg", "SPJG");
			bset.setFieldOrgDept("cjdwdm");
			docRes = bset.getDocument();
			List nodes = docRes.selectNodes("//RESPONSE/RESULT/ROW");
			if (nodes != null && nodes.size() > 0) {
				Element node = null;
				for (int i = 0; i < nodes.size(); i++) {
					node = (Element) nodes.get(i);
					String sjbh = node.selectSingleNode("SJBH").getText();
					String ywlx = node.selectSingleNode("YWLX").getText();
					String spbh = node.selectSingleNode("SPBH").getText();
					String str = null;
					// 判断是否有文书
                    // modified by guanchb  2008-05-08 start
                    // 增加 dah != 'null' 条件
					String sql_pub_blob = "select * from pub_blob a,ws_template b where (a.ZFBS = '0' or a.ZFBS IS NULL) and a.dah != 'null' and a.ws_template_id = b.ws_template_id and a.SJBH='"
							+ sjbh + "' and a.YWLX = '" + ywlx + "'";
                    // modified by guanchb 2008-05-08 end;
					QuerySet qs = DBUtil.executeQuery(sql_pub_blob, null,conn);
					String sp_ws_template_id = "";//审批文书编号
					if(qs.getRowCount()>0)
					{
			              for (int j = 0; j < qs.getRowCount(); j++)
			              {
			                  if("1".equals(qs.getString(1+j,"IS_SP_FLAG")))
			                  {
			                      sp_ws_template_id =qs.getString(1+j,"WS_TEMPLATE_ID");
			                      break;
			                  }
			              }
			          }
			          //node.selectSingleNode("SP_WS_TEMPLATE_ID").setText(sp_ws_template_id);
					if (qs.getRowCount() > 0) {
						String ws_template_id = "";
						String fjbh = "";
						//String sp_ws_template_id = "";// 审批文书编号
						String sp_fjbh = "";
						String operationKey = "";// 业务主键
						for (int j = 0; j < qs.getRowCount(); j++) {
							if ("1".equals(qs.getString(1 + j, "IS_SP_FLAG"))) {
								sp_ws_template_id = qs.getString(1 + j,
										"WS_TEMPLATE_ID");
								sp_fjbh = qs.getString(1 + j, "FJBH");
							}
							ws_template_id += qs.getString(1 + j,
									"WS_TEMPLATE_ID")
									+ ",";
							fjbh += qs.getString(1 + j, "fjbh") + ",";
							// 增加档案号节点
							if (operationKey != null
									&& operationKey.length() > 0)
								operationKey += ","
										+ qs.getString(1 + j, "dah");
							else
								operationKey += qs.getString(1 + j, "dah");

						}

						if (ws_template_id.length() > 0) {
							ws_template_id = ws_template_id.substring(0,
									ws_template_id.length() - 1);
						}
						if (fjbh.length() > 0) {
							fjbh = fjbh.substring(0, fjbh.length() - 1);
						}
						node.selectSingleNode("WSTEMPLATEID").setText(
								ws_template_id);
						node.selectSingleNode("FJBH").setText(fjbh);
						node.selectSingleNode("DAH").setText(operationKey);
						// 盖章信息判断
						String[][] sfgz = DBUtil
								.query(conn,"select sfgz from ws_template where  WS_TEMPLATE_ID='"
										+ sp_ws_template_id + "'");
						if (sfgz != null && "1".equalsIgnoreCase(sfgz[0][0])) {
							Process proc = ProcessMgr
									.getProcessByID(conn, spbh);
							Step step = proc.open();
							String approverole = step.getRole();
							String approvelevel = String.valueOf(step
									.getDeptLevel());
							PubWS pubws = new PubWS();
							Document doc = pubws.getDoc(sp_fjbh);
							Element result = pubws.canGetAzt(doc, approverole,
									approvelevel, "6");
							if (result != null)
								node.selectSingleNode("GZXX").setText("1");
							else
								node.selectSingleNode("GZXX").setText("0");
						} else {
							node.selectSingleNode("GZXX").setText("0");
						}
						((Element) node.selectSingleNode("GZXX"))
								.setAttributeValue("sv", "未盖章");

					} else {
						((Element) node.selectSingleNode("GZXX"))
								.setAttributeValue("sv", "未盖章");
						node.selectSingleNode("GZXX").setText("0");
						node.selectSingleNode("WSTEMPLATEID").setText("无审批文书");
					}
					// 定义判断审批处理方式
					String sql_processtype = "Select a.processtype from ap_processinfo b,ap_processtype a Where a.PROCESSTYPEOID = b.PROCESSTYPEOID and b.PROCESSOID='"
							+ spbh + "'";
					QuerySet qs_processtype = DBUtil.executeQuery(
							sql_processtype, null,conn);
					if (qs_processtype.getRowCount() > 0) {
						node.selectSingleNode("PROCESSTYPE").setText(
								qs_processtype.getString(1, 1));
					}
					// 通过业务类型 组成审批显示信息字符串 begin
					String url = null;
					EventVO event = EventManager.getEventByID(conn, sjbh);
					String[][] urls = DBUtil
							.query(
									conn,
									"select url from ap_task_link where ywlx='"
											+ ywlx
											+ "' and (DEPTID='"
											+ event.getPcsdm()
											+ "' or DEPTID='0') and rwlx='0' order by DEPTID desc");
					if (urls == null) {
						url = "jsp/framework/aplink/defaultDetailPage.jsp";
					} else {
						url = urls[0][0];
					}
					if (!Pub.empty(url)) {
						node.selectSingleNode("LINKURL").setText(url);
					}
				}
			}
			conn.commit();
		} catch (Exception e) {
			conn.rollback();
			throw e;
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
		return docRes;
	}
}
