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
import com.ccthanking.common.EditWsColumn;
import com.ccthanking.framework.log.LogManager;
import com.ccthanking.framework.coreapp.aplink.*;

public class SPAgainOP
{
	public SPAgainOP(){

	}
    /**
     * 再次提醒审批时，需要进行的业务操作类
     * @param conn：数据库链接
     * @param sjbh：事件编号
     * @param ywlx：业务类型
     * @param id：流程id
     * @param seq：流程seq
     * @param spbh：审批编号
     * @return 操作是否成功
     * @throws java.lang.Exception
     */

	public static boolean ywOp(Connection conn,String sjbh, String ywlx, String id, String seq, String spbh) throws Exception{
		if(ywlx.equals("040321")){
            // 处理意见再次发起审批时需要进行的操作
            return op040321(conn,sjbh,ywlx,id,seq,spbh);
        }
        return true;
	}
    private static boolean op040321(Connection conn,String sjbh, String ywlx, String id, String seq, String spbh) throws Exception{
        // 获得当前处理意见的序号主键值和审批级数；
        String clyjxh = null ; // 处理意见序号
        String saxyrxh = null; // 涉案嫌疑人序号
        String clyjspjs = null ; // 处理意见审批级数
        String ajbh = null ;
        String clyjlb = null ; // 处理意见类别
        // modified by guanchb 2008-06-19 start
        // 为selClyjXh查询语句增加 xzcf.zhux='0' 过滤条件
        String selClyjXh = "select distinct(xzcf.clyjxh) as clyjxh,xzcf.clyjspjs as clyjspjs, clyj.ajbh as ajbh , xzcf.CLYJLB as clyjlb , clyj.SAXYRXH "
            +" from ZA_ZFBA_XZAJ_WS_XZCFXX xzcf, Za_Zfba_Xzaj_Ws_Clyjdj clyj"
            +" where xzcf.zhux='0' and xzcf.clyjxh = clyj.clyjxh"
            +" and clyj.sjbh ='"+sjbh+"'";
        // modified by guanchb 2008-06-19 end
        // 为selClyjXh查询语句增加 xzcf.zhux='0' 过滤条件
        String[][] clyjxh_Qs = DBUtil.querySql(conn,selClyjXh);
        if(clyjxh_Qs == null){
            throw new Exception("本条处理意见信息下无处罚信息，无法再次提请处理意见审批");
        }
        clyjxh = clyjxh_Qs[0][0];
        clyjspjs = clyjxh_Qs[0][1];
        ajbh = clyjxh_Qs[0][2];
        clyjlb = clyjxh_Qs[0][3];
        saxyrxh = clyjxh_Qs[0][4];
        // 获得本案下需要更新的处罚信息记录（包括处理意见退回的处罚信息和新增的处罚信息）
        String selSql_cfxh = "select a.XZCFXH as xzcfxh"
            + " from ZA_ZFBA_XZAJ_WS_XZCFXX a, ZA_ZFBA_XZAJ_WS_CLYJDJ  b, ZA_ZFBA_JCXX_RK_SAXYRXX c"
            + " where a.zhux='0' and a.wfxyrbh = c.xh and a.ajbh = b.ajbh and a.clyjxh is null and c.xyrclzt='2' and b.ajbh='" +
            ajbh+"' " ;
        /*
            + " and a.wfxyrbh in ("
            + " select distinct(g.wfxyrbh) as wfxyrbh from ZA_ZFBA_XZAJ_WS_XZCFXX g, ZA_ZFBA_XZAJ_WS_CLYJDJ h, Event i"
            + " where g.zhux='0' and g.clyjxh = h.clyjxh and h.sjbh = i.sjbh and i.sjzt = '7'"
            + " and h.ajbh ='"+ajbh+"' ";
        */
        if(clyjlb.equals("2")){
            //selSql_cfxh = selSql_cfxh + " and g.wfxyrbh = h.saxyrxh and g.wfxyrbh ='"+saxyrxh+"' ";
        	selSql_cfxh = selSql_cfxh + " and a.wfxyrbh = b.saxyrxh and a.wfxyrbh ='"+saxyrxh+"' ";
        }
        //selSql_cfxh = selSql_cfxh +")";
        if(clyjlb.equals("1")){
            // 一般行政案件对单位进行处罚，出入境类案件不对 单位做出处罚处理
            selSql_cfxh = selSql_cfxh + " union"
                + " select d.XZCFXH as xzcfxh"
                + " from ZA_ZFBA_XZAJ_WS_XZCFXX d, ZA_ZFBA_XZAJ_WS_CLYJDJ e, ZA_ZFBA_JCXX_ZZ_WFFZDWXX f"
                + " where d.zhux='0' and d.wfdwbh = f.wfdwxh and d.ajbh = e.ajbh and d.clyjxh is null and f.dwclzt='2' and e.ajbh='" +
                ajbh + "' " ;
            /*
                + " and d.wfdwbh in ("
                + " select distinct(j.wfdwbh) as wfdwbh from ZA_ZFBA_XZAJ_WS_XZCFXX j, ZA_ZFBA_XZAJ_WS_CLYJDJ k, Event l"
                + " where j.zhux='0' and j.clyjxh = k.clyjxh and k.sjbh = l.sjbh and l.sjzt = '7'"
                + " and k.ajbh ='" + ajbh + "')";
            */
        }
        selSql_cfxh = selSql_cfxh + " union"
            + " select m.XZCFXH as xzcfxh from ZA_ZFBA_XZAJ_WS_XZCFXX m, ZA_ZFBA_XZAJ_WS_CLYJDJ n, Event p"
            + " where m.zhux='0' and m.clyjxh = n.clyjxh and n.sjbh = p.sjbh and p.sjzt = '7'"
            + " and n.ajbh ='"+ajbh+"'";
        if(clyjlb.equals("2")){
            selSql_cfxh = selSql_cfxh + " and m.wfxyrbh = n.saxyrxh and m.wfxyrbh ='"+saxyrxh+"' ";
        }

        String[][] cfxh_Qs = DBUtil.query(conn,selSql_cfxh);
        if(cfxh_Qs == null){
            throw new Exception("无法再次提请处理意见审批");
        }
        // 更新处罚信息表中的处理意见序号字段和处理意见审批级数字段
        String cfxh = "";
        String updateCFXX = "";
        for(int i = 0 ; i < cfxh_Qs.length ; i++ ){
            cfxh = cfxh_Qs[i][0] ;
            if(cfxh.equals("")){
                throw new Exception("提请失败");
            }
            updateCFXX = "update ZA_ZFBA_XZAJ_WS_XZCFXX set CLYJXH='"+clyjxh+"', CLYJSPJS='"+clyjspjs+"', CLYJLB='"+clyjlb+"' where XZCFXH='"+cfxh+"'";
            DBUtil.execSql(conn,updateCFXX);
        }
        // 设置处理意见信息的事件状态为 审批中
        String updateEventZT = "update event set sjzt='1' where sjbh='"+sjbh+"'";
        DBUtil.execSql(conn,updateEventZT);
        if(clyjlb.equals("2")){
            // 出入境类行政案件，预留位置

        }else{
        	/* 
        	 * 注释 by guanchb@2009-04-04 start
        	 * 不需要重新设置使用一级审批文书还是三级审批文书了，制作文书时，由页面JS函数控制。
            // 一般行政案件
            // 一级处理意见审批时，一级审批公章需要加盖到审批意见栏中时，使用304号处理意见审批文书
            // 删除263号处理意见审批文书，并且把263号文书的文书号和档案号更新到304号文书上
            if (clyjspjs.equals("1"))
            {
                setclyj_ws_use304(conn, ywlx, sjbh, "263", "304");
            }
            else
            {
                // 三级处理意见审批，
                setclyj_ws_use263(conn, ywlx, sjbh, "304");
            }
            */
        }
        return true;
    }
    private static void setclyj_ws_use304(Connection conn, String ywlx, String sjbh, String fromwsid, String towsid) throws Exception{
        String selWSWH = "select wswh,dah from pub_blob where sjbh='"+sjbh+"' and ywlx='"+ywlx+"' and WS_TEMPLATE_ID='"+fromwsid+"' and (ZFBS = '0' or ZFBS IS NULL)";
        String[][] selWSWH_Res = DBUtil.querySql(conn,selWSWH);
        if(selWSWH_Res == null){
            return;
            //throw new Exception("获得文书文号失败");
        }
        String fromwswh = selWSWH_Res[0][0];
        String fromdah  = selWSWH_Res[0][1];
        ArrayList fields =new ArrayList();
        fields.add("WSWH");
        ArrayList values=new ArrayList();
        values.add(fromwswh);
        // 更新304文书文号域的内容
        if(EditWsColumn.OverColumn(conn,sjbh,ywlx,towsid,fields,values)){
            // 更新blob表中记录的档案号和文书文号字段
            String updateWSWHandDAH = "update pub_blob set wswh='"+fromwswh+"', dah='"+fromdah+"' where sjbh='"+sjbh+"' and ywlx='"+ywlx+"' and WS_TEMPLATE_ID='"+towsid+"' and (ZFBS = '0' or ZFBS IS NULL)";
            // 删除无用的文书
            String delFromWS = "delete from pub_blob where sjbh='"+sjbh+"' and ywlx='"+ywlx+"' and WS_TEMPLATE_ID='"+fromwsid+"' and (ZFBS = '0' or ZFBS IS NULL)";
            DBUtil.execSql(conn,updateWSWHandDAH);
            DBUtil.execSql(conn,delFromWS);
        }else{
            throw new Exception("回填文书文号失败");
        }
    }
	/**
	 *
	 * @param conn
	 * @param ywlx
	 * @param sjbh
	 * @param delwsid：删除不使用的文书
	 * @throws Exception
	 */
	private static void setclyj_ws_use263(Connection conn, String ywlx, String sjbh,
			String delwsid) throws Exception {
		String delFromWS = "delete from pub_blob where sjbh='" + sjbh
				+ "' and ywlx='" + ywlx + "' and WS_TEMPLATE_ID='" + delwsid
				+ "' and (ZFBS = '0' or ZFBS IS NULL)";
		DBUtil.execSql(conn, delFromWS);
	}
}