package com.ccthanking.framework.wsdy.doc_sequence;


import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.QuerySet;
import java.sql.SQLException;
import java.sql.Connection;
import com.ccthanking.framework.base.BaseDAO;
import java.util.Calendar;
/**
 * <p>Title: ppx</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class WswhSequence
{
    static private WswhSequence instance;
    static synchronized public WswhSequence getInstance()
    {
        if (instance == null)
        {
            instance = new WswhSequence();
        }
        return instance;
    }
    public WswhSequence()
    {
    }
    /**
     * 获得文书文号
     * @return
     * @throws SQLException
     */
    public  synchronized String getSequenceID(String ywlx, String sjbh, String bmbh,String wsID,String hzms,Connection conn) throws SQLException {
        String sequenceID = hzms;

        //added by xukx 特殊情况:254 和 253 使用一个流水号
        switch(Integer.parseInt(wsID))
        {
        // 清理了历史中涉及的文书编号。这里就留下本系统中用到的。
        // 24、25为工程洽商审核意见单:有拆迁和无拆迁。但是他们的编号是连续的流水号。
            case 24:
            	wsID = "24";
            	break;
            case 25:
            	wsID = "24";
            	break;
        // 20、22、26、27为甩项申请会签单:有拆迁和无拆迁。4个会签单统一流水
            case 20:
            	wsID = "20";
            	break;
            case 22:
            	wsID = "20";
            	break;
            case 26:
            	wsID = "20";
            	break;
            case 27:
            	wsID = "20";
            	break;
        }
        //end
        String writerID = getWriterID(bmbh,wsID,hzms,conn);
        String curYear = Integer.toString(Calendar.getInstance().get(Calendar.YEAR)); //取得当前年
        sequenceID =curYear+writerID;
        //added by xukx 20080312如果有特殊文书文号，单独配置.
//        String seq = "";
//        seq = WswhSeq_Spec.getWswhSeq_Spec(ywlx,sjbh,bmbh,wsID,hzms,conn,writerID,curYear);
//        if(!"".equals(seq))
//        	return seq;
        //end
        return sequenceID;
    }
    /**
     * 获得流水号
     * @param bmbh
     * @throws SQLException
     */

    private  String getWriterID(String bmbh,String wsID,String hzms,Connection conn) throws SQLException{
        String  writerID = null;
        //modified by andy 20081209 增加年度的条件
        String sql = "select DOCID,ND from ws_doc_sequence where DEPTID='"+bmbh+"' and DOCTYPEID = '"+wsID+"' and STATE=? and ND=to_char(sysdate,'YYYY') order by DOCID ";
        Object[] paras = {"2"};//查询最小未使用的流水号
        Doc_SequenceVO vo = new Doc_SequenceVO();
        vo.setDeptid(bmbh);
        vo.setDoctypeid(wsID);
        vo.setPrefix(hzms);
        vo.setState("1");
        QuerySet qs = DBUtil.executeQuery(sql+"asc",paras);
        if(qs.getRowCount()>0){
            writerID = qs.getString(1,1);//取最小的废号
            vo.setDocid(writerID);
            //added by andy 20081209 设置年度
            vo.setNd(qs.getString(1,2));
            updateDoc_sequence(vo,conn);
        }else{
            paras[0] = "1";//查找最大的已使用流水号
            qs = DBUtil.executeQuery(sql+"desc",paras);
            if(qs.getRowCount()>0){
              int docid= Integer.parseInt(qs.getString(1,1))+1;
              writerID = String.valueOf(docid);//最大值加1
            }else{
              writerID = "1";
            }
            vo.setDocid(writerID);
            //added by andy 20081209 设置年度
            vo.setNd(com.ccthanking.framework.util.Pub.getDate("yyyy"));
            insertDoc_sequence(vo,conn);
        }
        if(writerID.length()<4){
        	int t = Integer.parseInt(writerID);
        	t = 1000+t;
        	writerID = String.valueOf(t).substring(1,4);
        }
        return writerID;


    }
    /**
     * 更新Doc_sequence表
     * @param vo
     * @throws SQLException
     */
    private  void updateDoc_sequence(Doc_SequenceVO vo,Connection conn) throws SQLException{
        try
           {
               //conn.setAutoCommit(false);
               BaseDAO dao = new BaseDAO();
               dao.update(conn,vo);

//               conn.commit();

           }
           catch (Exception e)
           {

//               conn.rollback();

               e.printStackTrace(System.out);
               throw new SQLException("更新DOC_SEQUENCE表失败:"+e.getMessage());
           }
           finally
           {
           }
    }

    /**
     * 插入Doc_sequence表
     * @param vo
     * @throws SQLException
     */

    private  void insertDoc_sequence(Doc_SequenceVO vo,Connection conn) throws SQLException{

        try
           {
               //conn.setAutoCommit(false);
               BaseDAO dao = new BaseDAO();
               dao.insert(conn,vo);

//               conn.commit();

           }
           catch (Exception e)
           {

//               conn.rollback();

               e.printStackTrace(System.out);
               throw new SQLException("插入DOC_SEQUENCE表失败:"+e.getMessage());
           }
           finally
           {
           }
    }
    /**
     * 文书打印失败流水号处理成为使用
     * @throws SQLException
     */
    public  void temporaryWh(String writerID,String bmbh,String wsID,String hzms,Connection conn)throws SQLException{
        if(writerID!=null){
            Doc_SequenceVO vo = new Doc_SequenceVO();
            vo.setDeptid(bmbh);
            vo.setDoctypeid(wsID);
            vo.setPrefix(hzms);
            vo.setDocid(writerID);
            vo.setState("2");
            updateDoc_sequence(vo,conn);
        }

    }

}