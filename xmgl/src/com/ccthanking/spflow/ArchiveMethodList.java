package com.ccthanking.spflow;

import java.sql.Connection;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.QuerySet;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.coreapp.aplink.TaskVO;
import com.ccthanking.framework.message.messagemgr.PushMessage;
import com.ccthanking.framework.util.DateTimeUtil;
import com.ccthanking.framework.util.Pub;

/**
 * 
 * @author
 * @des 审批归档调用方法列表,误删
 */

public class ArchiveMethodList {

    /*****************************************************/
    public static final String YWLX040401 = "taskJsxsajSp"; //

    public static final String YWLX000002 = "taskTs"; // 工作联系单
    public static final String YWLX000003 = "taskTs"; // 工作請示单

    public static final String YWLX200502 = "taskSw"; // 收文处理

    public static final String YWLX200503 = "taskFw"; // 发文处理

    public static final String YWLX300101 = "taskZtbxq"; // 招投标需求处理

    public static final String YWLX700101 = "taskHt"; // 合同审批

    public static final String YWLX700201 = "taskLybzj"; // 履约保证金

    public static final String YWLX700204 = "taskTqk"; // 提请款
    
    public static final String YWLX040417 = "taskGcsx"; // 工程甩项

    public static boolean defaultArchiveMethod(Connection conn, String eventID, User user) throws Exception {

        Object[] objs = new Object[1];
        objs[0] = eventID;

        try {

        } catch (Exception e) {
            System.out.println(e.toString());
            return false;
        }
        return true;
    }

    public static boolean taskTs(Connection conn, String eventID, User user) throws Exception {
    	try {
    		String date = DateTimeUtil.getDateTime();
    		
    		// 归档操作
            String sql = "update AP_TASK_SCHEDULE set " 
            		+ "RWZT='"+TaskVO.TASK_STATUS_EXECUTED+"'," 
            		+ "RWLX='"+TaskVO.TASK_TYPE_PRINT+"'," 
            		+ "WCSJ=to_date('"+date+"','YYYY-MM-DD HH24:MI:SS'), " 
            		+ "WCRID='"+user.getAccount()+"'," 
            		+ "WCRXM='"+user.getName()+"'," 
            		+ "WCDWDM='"+user.getDepartment()+"' "
            		+ "where wcsj is null and sjbh = '"+eventID+"' and dbryid = '"+user.getAccount()+"'";
            DBUtil.exec(conn, sql);
            
            // 未处理流程，在流程结束时也相应处理，但不是归档
            String querysql = "select dbryid from ap_task_schedule where sjbh='"+eventID+"' and rwzt='01'";
            String[][] rs = DBUtil.query(conn, querysql);
            if (rs != null && rs.length != 0) {
            	String dbryid = rs[0][0];
            	sql = "update AP_TASK_SCHEDULE set " 
                		+ "RWZT='"+TaskVO.TASK_STATUS_EXECUTED+"'," 
                		+ "RWLX='"+TaskVO.TASK_TYPE_APPROVE+"'," 
                		+ "WCSJ=to_date('"+date+"','YYYY-MM-DD HH24:MI:SS'), " 
                		+ "WCRID='"+dbryid+"', RESULT='1',"
                		+ "WCRXM='"+Pub.getUserNameByLoginId(dbryid)+"'," 
                		+ "WCDWDM='"+Pub.getUserDepartmentById(dbryid)+"' "
                		+ "where wcsj is null and sjbh = '"+eventID+"' and rwzt='01'";
                DBUtil.exec(conn, sql);
			}
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public static boolean taskSw(Connection conn, String eventID, User user) throws Exception {

        return true;
    }

    public static boolean taskFw(Connection conn, String eventID, User user) throws Exception {
        QuerySet wsxx = DBUtil.executeQuery("select * from ap_process_ws where sjbh = '" + eventID + "'", null, conn);
        String bh = "";
        String fcrq = "";
        if (wsxx.getRowCount() > 0) {
            for (int i = 0; i < wsxx.getRowCount(); i++) {
                String fieldname = wsxx.getString(i + 1, "FIELDNAME");
                if (!Pub.empty(fieldname) && "BH".equals(fieldname)) {
                    bh = wsxx.getString(i + 1, "DOMAIN_VALUE");
                }
                if (!Pub.empty(fieldname) && "FCRQ".equals(fieldname)) {
                    fcrq = wsxx.getString(i + 1, "DOMAIN_VALUE");
                }
            }
            String update_sql = "update xtbg_gwgl_fwgl set WJBH=?,FWSJ=? where sjbh=?";
            Object[] objs = { bh, DateTimeUtil.parse(fcrq, ""), eventID };
            DBUtil.executeUpdate(conn, update_sql, objs);

        }
        return true;
    }

    public static boolean taskZtbxq(Connection conn, String eventID, User user) throws Exception {
        // 更新需求状态为2
        try {
            String sql = "update GC_ZTB_XQ set xqzt = '3' where sjbh = '" + eventID + "'";
            DBUtil.exec(conn, sql);

            // 招标需求已归档，发送相关配置人员信息提醒。    add by xiahongbo by 2014-10-14
            String zbxqSql = "select GZMC,GC_ZTB_XQ_ID from GC_ZTB_XQ where sjbh='" + eventID + "'";
            String[][] zbxq = DBUtil.query(conn, zbxqSql);
            String zbxqName = "";
            String param = "";
            if(zbxq!=null && zbxq.length>0){
            	zbxqName = zbxq[0][0];
            	param = "?openType=message&xqid="+zbxq[0][1];
            }
            PushMessage.push(conn, null, PushMessage.ZBXQGD, "招标需求【"+zbxqName+"】已归档，请办理相应业务。", param, eventID, "300101", "", "招标需求待启动");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    public static boolean taskHt(Connection conn, String eventID, User user) throws Exception {
        // 更新合同状态为审批通过
        try {
/*
            String htbm_sqlString = "select t.domain_value from ap_process_ws t where t.fieldname='Fld464' and ywlx='700101' and sjbh=? ";
            String[][] rsArray = DBUtil.querySql(conn, htbm_sqlString, new Object[] { eventID });
*/
            String sql = "UPDATE gc_htgl_ht SET htzt = '-1' where sjbh = '"+eventID+"'";
            DBUtil.executeUpdate(conn, sql, null);
            // DBUtil.exec(conn, sql);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    public static boolean taskLybzj(Connection conn, String eventID, User user) throws Exception {
        // 更新履约保证金状态为审批通过
        try {
            String sql = "UPDATE gc_zjgl_lybzj SET fhqk = '2' where sjbh = '" + eventID + "'";
            DBUtil.exec(conn, sql);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    public static boolean taskTqk(Connection conn, String eventID, User user) throws Exception {
        // 更新状态为审批通过
        try {

            // 更新提请款主表状态
            String updatesql = "UPDATE gc_zjgl_tqk SET tqkzt = '6'  where sjbh = '" + eventID + "'";
            DBUtil.exec(conn, updatesql);
            // 更新提请款部门主表状态
            String updatetqkbmzt = "UPDATE gc_zjgl_tqkbm tt SET tt.tqkzt='6' WHERE tt.id IN(SELECT gzt.id  FROM gc_zjgl_tqkbm gzt, gc_zjgl_tqkbmmx gzt2, gc_zjgl_tqkmx gzt3 WHERE gzt3.bmmxid=gzt2.id AND gzt2.tqkid=gzt.id AND gzt3.tqkid IN(SELECT ID FROM gc_zjgl_tqk WHERE sjbh='"
                    + eventID + "'))";
            DBUtil.exec(conn, updatetqkbmzt);
            // 更新提请款部门明细状态
            String updatetqkbmmxzt = "UPDATE gc_zjgl_tqkbmmx g SET g.bmtqkmxzt='6' WHERE ID IN(SELECT gzt.id FROM gc_zjgl_tqkbmmx gzt, gc_zjgl_tqkmx gzt2 WHERE gzt.id=gzt2.bmmxid  and gzt2.tqkid IN  (SELECT ID FROM gc_zjgl_tqk WHERE sjbh = '"
                    + eventID + "'))";
            DBUtil.exec(conn, updatetqkbmmxzt);
            // 更新提请款部门明细状态
            String inserthtzf = "insert into gc_htgl_ht_htzf  (id,HTSJID,ZFJE,HTDID,ZFNF,ZFYF,SFDZF,LRR,LRSJ,LRBM,LRBMMC) SELECT SYS_GUID(),gzt.htid,gzt.cwshz,gzt.id,gzt3.QKNF,gzt3.YF,'1','"+user.getAccount()+"',SYSDATE,'"+user.getOrgDept().getDeptID()+"','"+user.getOrgDept().getDeptFullName()+"' FROM gc_zjgl_tqkbmmx gzt, gc_zjgl_tqkmx gzt2, gc_zjgl_tqk gzt3 " 
            	+" WHERE gzt.id=gzt2.bmmxid and gzt2.tqkid = gzt3.id and gzt2.tqkid IN  (SELECT ID FROM gc_zjgl_tqk WHERE sjbh = '"+ eventID + "') ";
            DBUtil.exec(conn, inserthtzf);
            
            

        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }
    
    //20140304-sjl
    public static boolean taskGcsx(Connection conn, String eventID, User user) throws Exception {
        // 更新履约保证金状态为审批通过
        try {
        	Date d = new Date();
            String sql = "UPDATE gc_jh_sj SET wgsj_sj = to_date('"+d+"','yyyy-MM-dd') where gc_jh_sj_id =(select JHSJID from gc_gcgl_gcsx where sjbh = '" + eventID + "')";
            System.out.println("===========:"+sql);
            DBUtil.exec(conn, sql);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

}