package com.ccthanking.framework.coreapp.aplink;
import java.util.*;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import com.ccthanking.framework.base.*;
import org.dom4j.*;
import com.ccthanking.framework.dic.*;
import com.ccthanking.framework.util.*;
import java.sql.Connection;
import com.ccthanking.framework.*;
import com.ccthanking.framework.common.*;
import com.ccthanking.common.vo.*;
import com.ccthanking.common.EventManager;
import com.ccthanking.framework.log.LogManager;
import com.ccthanking.framework.coreapp.aplink.*;

public class AfterSP
{
	public AfterSP(){

	}
    /**
     * 审批通过时，需要进行的业务操作类
     * @param conn：数据库链接
     * @param sjbh：事件编号
     * @param ywlx：业务类型
     * @param id：流程id
     * @param seq：流程seq
     * @param spbh：审批编号
     * @return 操作是否成功
     * @throws java.lang.Exception
     */
	public static boolean ywOp(Connection conn,String id, String seq, String sjbh, String ywlx, String spbh) throws Exception{
		if(ywlx.equals("040338")){
            // 强制隔离戒毒审批通过时需要进行的操作
            return op040338(conn,sjbh,ywlx,id,seq,spbh);
        }
        if(ywlx.equals("040336")){
            // 社区戒毒审批通过时需要进行的操作
            return op040336(conn,sjbh,ywlx,id,seq,spbh);
        }
        if(ywlx.equals("040340")){
            // 延长强制隔离戒毒审批通过时需要进行的操作
            return op040340(conn,sjbh,ywlx,id,seq,spbh);
        }

       if(ywlx.equals("040345")){
           // 解除社区戒毒审批通过时需要进行的操作
           return op040345(conn,sjbh,ywlx,id,seq,spbh);
       }
       if(ywlx.equals("040341")){
           // 提前解除强制隔离戒毒审批通过时需要进行的操作
           return op040342(conn,sjbh,ywlx,id,seq,spbh);
       }

       if(ywlx.equals("040342")){
           // 提前解除强制隔离戒毒审批通过时需要进行的操作
           return op040342(conn,sjbh,ywlx,id,seq,spbh);
       }
       if(ywlx.equals("040343")){
           // 责令社区康复审批通过时需要进行的操作
           return op040336(conn,sjbh,ywlx,id,seq,spbh);
       }
       if(ywlx.equals("040344")){
           // 解除社区康复审批通过时需要进行的操作
           return op040345(conn,sjbh,ywlx,id,seq,spbh);
       }

        return true;

	}
   private static boolean op040336(Connection conn,String sjbh, String ywlx, String id, String seq, String spbh) throws Exception{
       String selClyj =
           "SELECT CLYJXH FROM ZA_ZFBA_XZAJ_WS_CLYJDJ WHERE SJBH = '" + sjbh +
           "'";
       String[][] clyjRes = DBUtil.query(conn, selClyj);
       if (clyjRes == null)
           throw new Exception("获得处理意见序号失败！");
       String clyjxh = clyjRes[0][0];
       String sql = "select (a.cjsj), a.spr "
           + " from ap_task_schedule a"
           + " where a.cjsj = (select max(t.cjsj)"
           + " from ap_task_schedule t"
           + " where t.sjbh = '" + sjbh + "')";
       QuerySet spsjQS = DBUtil.executeQuery(sql, null, conn);
       if (spsjQS.getRowCount() <= 0)
           throw new Exception("无法获得审批信息");
       String spsjStr = spsjQS.getString(1, "cjsj");
       String spr = spsjQS.getString(1, "spr");

       spsjStr = spsjStr.replaceAll("-", "");
       spsjStr = spsjStr.replaceAll(" ", "");
       spsjStr = spsjStr.replaceAll(":", "");
       String year = spsjStr.substring(0, 4);
       String month = spsjStr.substring(4, 6);
       String day = spsjStr.substring(6, 8);
       String hour = spsjStr.substring(8, 10);
       String minute = spsjStr.substring(10, 12);
       String second = spsjStr.substring(12, 14);
       Date cjsj = new Date();
       cjsj.setYear( (new Integer(year).intValue()) - 1900);
       cjsj.setMonth( (new Integer(month).intValue()) - 1);
       cjsj.setDate(new Integer(day).intValue());
       cjsj.setHours(new Integer(hour).intValue());
       cjsj.setMinutes(new Integer(minute).intValue());
       cjsj.setSeconds(new Integer(second).intValue());
       Calendar kssj = Calendar.getInstance();
       kssj.setTime(cjsj);
       //kssj.add(Calendar.MINUTE, 60);
       Date spsj = kssj.getTime(); // 审批时间
       Object[] paras = new Object[4];
       paras[0] = spsj;
       paras[1] = spsj;
       paras[2] = spr;
       paras[3] = clyjxh;
       String upSql = "UPDATE ZA_ZFBA_XZAJ_QT_SQJDKFXX SET SPRQ=?, TFRQ=?, SPR=? WHERE CLYJXH=? AND ZHUX='0'";
       DBUtil.executeUpdate(conn, upSql, paras);
       return true;
    }

    private static boolean op040338(Connection conn,String sjbh, String ywlx, String id, String seq, String spbh) throws Exception{
        String selClyj = "SELECT CLYJXH FROM ZA_ZFBA_XZAJ_WS_CLYJDJ WHERE SJBH = '" + sjbh + "'";
        String[][] clyjRes = DBUtil.query(conn,selClyj);
        if( clyjRes == null)
            throw new Exception("获得处理意见序号失败！");
        String clyjxh = clyjRes[0][0];
        /*
        String selSql = "SELECT DISTINCT(QZGLJDQX) FROM ZA_ZFBA_XZAJ_QT_QZGLJDXX WHERE CLYJXH='"+clyjxh+"' AND ZHUX='0'";
        String [][] selRes = DBUtil.querySql(conn,selSql);
        if(selRes == null)
            throw new Exception("获得强制戒毒期限失败！");
        // 获得强制戒毒期限
        int jdqx = Integer.parseInt(selRes[0][0]);
        */
        String sql = "select (a.cjsj), a.spr "
            +" from ap_task_schedule a"
            +" where a.cjsj = (select max(t.cjsj)"
                  +" from ap_task_schedule t"
                  +" where t.sjbh = '"+sjbh+"')";
        QuerySet spsjQS = DBUtil.executeQuery(sql,null,conn);
        if(spsjQS.getRowCount()<=0)
            throw new Exception("无法获得审批信息");
        String spsjStr = spsjQS.getString(1, "cjsj");
        String spr = spsjQS.getString(1, "spr");

        spsjStr = spsjStr.replaceAll("-", "");
        spsjStr = spsjStr.replaceAll(" ", "");
        spsjStr = spsjStr.replaceAll(":", "");
        String year = spsjStr.substring(0, 4);
        String month = spsjStr.substring(4, 6);
        String day = spsjStr.substring(6, 8);
        String hour = spsjStr.substring(8, 10);
        String minute = spsjStr.substring(10, 12);
        String second = spsjStr.substring(12, 14);
        Date cjsj = new Date();
        cjsj.setYear( (new Integer(year).intValue()) - 1900);
        cjsj.setMonth( (new Integer(month).intValue()) - 1);
        cjsj.setDate(new Integer(day).intValue());
        cjsj.setHours(new Integer(hour).intValue());
        cjsj.setMinutes(new Integer(minute).intValue());
        cjsj.setSeconds(new Integer(second).intValue());

        Calendar kssj = Calendar.getInstance();
        kssj.setTime(cjsj);
        //计算强制戒毒开始时间,在审批时间基础上加一小时，再取年月日作为强戒开始时间
        kssj.add(Calendar.MINUTE, 60);
        Date jdkssj = kssj.getTime(); // 强制戒毒开始时间
        //计算强制戒毒结束时间
        /*
        kssj.add(Calendar.YEAR, jdqx);
        kssj.add(Calendar.DATE, -1);
        Date jdjssj = kssj.getTime(); // 强制戒毒结束时间
        */
        Object[] paras = new Object[4];
        //paras[0] = jdkssj;
        //paras[1] = jdjssj;
        paras[0] = jdkssj;
        paras[1] = jdkssj;
        paras[2] = spr;
        paras[3] = clyjxh;
        //String upSql = "UPDATE ZA_ZFBA_XZAJ_QT_QZGLJDXX SET QZGLJDQSRQ=?, QZGLJDZZRQ=?, SPRQ=?, TFRQ=?, SPR=? WHERE CLYJXH=? AND ZHUX='0'";
        String upSql = "UPDATE ZA_ZFBA_XZAJ_QT_QZGLJDXX SET SPRQ=?, TFRQ=?, SPR=? WHERE CLYJXH=? AND ZHUX='0'";
        DBUtil.executeUpdate(conn, upSql, paras);

        return true;
    }
    private static boolean op040342(Connection conn,String sjbh, String ywlx, String id, String seq, String spbh) throws Exception{
         String selClyj = "SELECT CLYJXH FROM ZA_ZFBA_XZAJ_WS_CLYJDJ WHERE SJBH = '" + sjbh + "'";
         String[][] clyjRes = DBUtil.query(conn,selClyj);
         if( clyjRes == null)
              throw new Exception("获得处理意见序号失败！");
         String clyjxh = clyjRes[0][0];
         String selSql = "SELECT DISTINCT(TBSJ) FROM ZA_ZFBA_XZAJ_QT_JCQZGLJDXX WHERE CLYJXH='"+clyjxh+"' AND ZHUX='0'";
         String [][] selRes = DBUtil.querySql(conn,selSql);
         if(selRes == null)
              throw new Exception("获得解除强制戒毒期限失败！");

        String sql = "select (a.cjsj), a.spr "
        +" from ap_task_schedule a"
        +" where a.cjsj = (select max(t.cjsj)"
              +" from ap_task_schedule t"
              +" where t.sjbh = '"+sjbh+"')";
        QuerySet spsjQS = DBUtil.executeQuery(sql,null,conn);
        if(spsjQS.getRowCount()<=0)
            throw new Exception("无法获得审批信息");
        String spsjStr = spsjQS.getString(1, "cjsj");
        String spr = spsjQS.getString(1, "spr");

        spsjStr = spsjStr.replaceAll("-", "");
        spsjStr = spsjStr.replaceAll(" ", "");
        spsjStr = spsjStr.replaceAll(":", "");
        String year = spsjStr.substring(0, 4);
        String month = spsjStr.substring(4, 6);
        String day = spsjStr.substring(6, 8);
        String hour = spsjStr.substring(8, 10);
        String minute = spsjStr.substring(10, 12);
        String second = spsjStr.substring(12, 14);
        Date cjsj = new Date();
        cjsj.setYear( (new Integer(year).intValue()) - 1900);
        cjsj.setMonth( (new Integer(month).intValue()) - 1);
        cjsj.setDate(new Integer(day).intValue());
        cjsj.setHours(new Integer(hour).intValue());
        cjsj.setMinutes(new Integer(minute).intValue());
        cjsj.setSeconds(new Integer(second).intValue());
        Calendar kssj = Calendar.getInstance();
        kssj.setTime(cjsj);
        Date spsj = kssj.getTime(); // 审批时间
        Object[] paras = new Object[4];
        paras[0] = spsj;
        paras[1] = spsj;
        paras[2] = spr;
        paras[3] = clyjxh;
        String upSql = "UPDATE ZA_ZFBA_XZAJ_QT_JCQZGLJDXX SET  SPRQ=?, TFRQ=?, SPR=? WHERE CLYJXH=? AND ZHUX='0'";
        DBUtil.executeUpdate(conn, upSql, paras);

        return true;
}

    private static boolean op040340(Connection conn,String sjbh, String ywlx, String id, String seq, String spbh) throws Exception{
         String selClyj = "SELECT CLYJXH FROM ZA_ZFBA_XZAJ_WS_CLYJDJ WHERE SJBH = '" + sjbh + "'";
         String[][] clyjRes = DBUtil.query(conn,selClyj);
         if( clyjRes == null)
              throw new Exception("获得处理意见序号失败！");
         String clyjxh = clyjRes[0][0];
         String selSql = "SELECT DISTINCT(TBSJ) FROM ZA_ZFBA_XZAJ_QT_YCQZGLJDXX WHERE CLYJXH='"+clyjxh+"' AND ZHUX='0'";
         String [][] selRes = DBUtil.querySql(conn,selSql);
         if(selRes == null)
              throw new Exception("获得延长强制戒毒期限失败！");

        String sql = "select (a.cjsj), a.spr "
        +" from ap_task_schedule a"
        +" where a.cjsj = (select max(t.cjsj)"
              +" from ap_task_schedule t"
              +" where t.sjbh = '"+sjbh+"')";
        QuerySet spsjQS = DBUtil.executeQuery(sql,null,conn);
        if(spsjQS.getRowCount()<=0)
            throw new Exception("无法获得审批信息");
        String spsjStr = spsjQS.getString(1, "cjsj");
        String spr = spsjQS.getString(1, "spr");

        spsjStr = spsjStr.replaceAll("-", "");
        spsjStr = spsjStr.replaceAll(" ", "");
        spsjStr = spsjStr.replaceAll(":", "");
        String year = spsjStr.substring(0, 4);
        String month = spsjStr.substring(4, 6);
        String day = spsjStr.substring(6, 8);
        String hour = spsjStr.substring(8, 10);
        String minute = spsjStr.substring(10, 12);
        String second = spsjStr.substring(12, 14);
        Date cjsj = new Date();
        cjsj.setYear( (new Integer(year).intValue()) - 1900);
        cjsj.setMonth( (new Integer(month).intValue()) - 1);
        cjsj.setDate(new Integer(day).intValue());
        cjsj.setHours(new Integer(hour).intValue());
        cjsj.setMinutes(new Integer(minute).intValue());
        cjsj.setSeconds(new Integer(second).intValue());
        Calendar kssj = Calendar.getInstance();
        kssj.setTime(cjsj);
        Date spsj = kssj.getTime(); // 审批时间
        Object[] paras = new Object[4];
        paras[0] = spsj;
        paras[1] = spsj;
        paras[2] = spr;
        paras[3] = clyjxh;
        String upSql = "UPDATE ZA_ZFBA_XZAJ_QT_YCQZGLJDXX SET  SPRQ=?, TFRQ=?, SPR=? WHERE CLYJXH=? AND ZHUX='0'";
        DBUtil.executeUpdate(conn, upSql, paras);

        return true;
}
    private static boolean op040345(Connection conn,String sjbh, String ywlx, String id, String seq, String spbh) throws Exception{
         String selClyj = "SELECT CLYJXH FROM ZA_ZFBA_XZAJ_WS_CLYJDJ WHERE SJBH = '" + sjbh + "'";
         String[][] clyjRes = DBUtil.query(conn,selClyj);
         if( clyjRes == null)
              throw new Exception("获得处理意见序号失败！");
         String clyjxh = clyjRes[0][0];
         String selSql = "SELECT DISTINCT(TBSJ) FROM ZA_ZFBA_XZAJ_QT_JCSQJDKFXX WHERE CLYJXH='"+clyjxh+"' AND ZHUX='0'";
         String [][] selRes = DBUtil.querySql(conn,selSql);
         if(selRes == null)
              throw new Exception("获得解除社区戒毒期限失败！");

        String sql = "select (a.cjsj), a.spr "
        +" from ap_task_schedule a"
        +" where a.cjsj = (select max(t.cjsj)"
              +" from ap_task_schedule t"
              +" where t.sjbh = '"+sjbh+"')";
        QuerySet spsjQS = DBUtil.executeQuery(sql,null,conn);
        if(spsjQS.getRowCount()<=0)
            throw new Exception("无法获得审批信息");
        String spsjStr = spsjQS.getString(1, "cjsj");
        String spr = spsjQS.getString(1, "spr");

        spsjStr = spsjStr.replaceAll("-", "");
        spsjStr = spsjStr.replaceAll(" ", "");
        spsjStr = spsjStr.replaceAll(":", "");
        String year = spsjStr.substring(0, 4);
        String month = spsjStr.substring(4, 6);
        String day = spsjStr.substring(6, 8);
        String hour = spsjStr.substring(8, 10);
        String minute = spsjStr.substring(10, 12);
        String second = spsjStr.substring(12, 14);
        Date cjsj = new Date();
        cjsj.setYear( (new Integer(year).intValue()) - 1900);
        cjsj.setMonth( (new Integer(month).intValue()) - 1);
        cjsj.setDate(new Integer(day).intValue());
        cjsj.setHours(new Integer(hour).intValue());
        cjsj.setMinutes(new Integer(minute).intValue());
        cjsj.setSeconds(new Integer(second).intValue());
        Calendar kssj = Calendar.getInstance();
        kssj.setTime(cjsj);
        Date spsj = kssj.getTime(); // 审批时间
        Object[] paras = new Object[4];
        paras[0] = spsj;
        paras[1] = spsj;
        paras[2] = spr;
        paras[3] = clyjxh;
        String upSql = "UPDATE ZA_ZFBA_XZAJ_QT_JCSQJDKFXX SET  SPRQ=?, TFRQ=?, SPR=? WHERE CLYJXH=? AND ZHUX='0'";
        DBUtil.executeUpdate(conn, upSql, paras);

        return true;
}

}