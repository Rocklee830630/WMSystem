package com.ccthanking.framework.spflow.DealSP;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import com.ccthanking.framework.spflow.IDealSP;
import java.sql.Connection;
import com.ccthanking.common.EventManager;
import com.ccthanking.common.vo.ApWsTypzVO;
import com.ccthanking.common.vo.EventVO;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.coreapp.aplink.TaskMgrBean;
import com.ccthanking.framework.coreapp.aplink.TaskVO;
import com.ccthanking.framework.util.Pub;
import com.ccthanking.framework.spflow.WsConfigManager;

/**
 * 履约保证金回调.
 * 
 * @author <a href="mailto:jianggl88@gmail.com">蒋根亮</a>
 * @version v1.00
 * @since 1.00 2013-10-9
 * 
 */
public class ImpDealSP700201 implements IDealSP {

    public void dealSP(HttpServletRequest request, HttpServletResponse response, Connection conn, 
    		EventVO evo, String ywlx, String desc, User user,String type,JSONObject joList) throws java.lang.Exception {
        TaskMgrBean taskMgr = new TaskMgrBean();
        TaskVO task = null;
        String dbr = request.getParameter("dbr");
        String dbdw = request.getParameter("dbdw");
        String ywbh = (String) request.getParameter("ywbh");// 案件编号
        if (ywbh == null)
            ywbh = "";
        String title = (String) request.getParameter("title");// 标题
        String condition = (String) request.getParameter("condition");// 条件
        String operationoid = (String) request.getParameter("operationoid");
        String eventid = (String) request.getParameter("eventid");
        String mind = joList.getString("mind");//(String)request.getParameter("mind");

        ApWsTypzVO typzvo = WsConfigManager.getDefaultConfig(ywlx, user, condition);
        if (typzvo != null) {
            // 判发起是否为特送审批dbdw有值即为特送审批，否则为普通审批
            if (operationoid == null || operationoid.length() <= 0)
                operationoid = typzvo.getOperationoid();
            boolean isTs = false;
            if (!Pub.empty(operationoid)) {
                String sql = " select a.processtype from ap_processtype a  where a.operationoid = '" + operationoid + "'";
                String rs[][] = DBUtil.query(conn, sql);
                if (rs != null && "4".equals(rs[0][0])) {
                    isTs = true;
                }
            }
            task = taskMgr.createApproveTask(conn, evo.getSjbh(), ywlx, desc + "，请办理。", dbdw, "", dbr, user, operationoid, ywbh, title,
                    ywlx, mind); //
            if (task == null) { // 该业务不需要审批
                EventManager.archiveEvent(conn, evo, user);
            }
            // 更新具体业务信息
            if (!Pub.empty(eventid)) {
                String updatesql = "UPDATE gc_zjgl_lybzj SET fhqk = '1'  where sjbh = '" + eventid + "'";
                DBUtil.exec(conn, updatesql);
            }

        } else {
            task = taskMgr.createApproveTask(conn, evo.getSjbh(), ywlx, desc + "，请办理。", "", "", dbr, false, user, ywbh, title, ywlx, mind);

            if (task == null) { // 该业务不需要审批
                EventManager.archiveEvent(conn, evo, user);
            }
        }

    }
}