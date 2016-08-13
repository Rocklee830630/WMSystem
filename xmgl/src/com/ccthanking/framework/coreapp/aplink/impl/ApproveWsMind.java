package com.ccthanking.framework.coreapp.aplink.impl;

import com.ccthanking.framework.wsdy.PubWS;
import com.ccthanking.framework.util.Pub;
import java.sql.Connection;

import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.User;

public class ApproveWsMind 
{
    public ApproveWsMind()
    {
    }

    public void ModifyApproveMind(Connection conn,String strwsbh,String sjbh,String ywlx,String approverole,String stepSequence,String fjbh,String result,String spFieldname,String sfth,String autodoApprove,User user)
        throws Exception
    {
        PubWS pubws = new PubWS();
        //默认处理审批意见写入文书
        if(!"1".equals(sfth)){
           if(spFieldname==null||spFieldname.length()<=0)
           {
        	   spFieldname = getFieldname(conn,strwsbh,approverole,stepSequence,PubWS.FIELDEXTEN_MIND);
           }
           //默认处理签字和签字日期
           String qzFieldName = getFieldname(conn,strwsbh,approverole,stepSequence,PubWS.FIELDEXTEN_WRITE);
           if(qzFieldName!=null && qzFieldName.length()>=0&&!"1".equals(autodoApprove)){
        	   pubws.getSpYjXML(conn,fjbh, strwsbh, sjbh, ywlx, qzFieldName, result, approverole, stepSequence,PubWS.FIELDEXTEN_WRITE,"",user);
           }
         //默认处理签字和签字日期
           String qzrqFieldName = getFieldname(conn,strwsbh,approverole,stepSequence,PubWS.FIELDEXTEN_DATE);
           if(qzrqFieldName!=null && qzrqFieldName.length()>=0&&!"1".equals(autodoApprove)){
        	   pubws.getSpYjXML(conn,fjbh, strwsbh, sjbh, ywlx, qzrqFieldName, result, approverole, stepSequence,PubWS.FIELDEXTEN_DATE,"",user);
           }
           //如果是请假审批，内勤专员不写意见 add by wangzh
           if("040409".equals(ywlx)&&"5915833b-e17c-4d48-a910-ab6d1ab7c3d2".equals(approverole)){
        	   
           } else if("040417".equals(ywlx)) {   //工程甩项申请会签单 只显示意见
        	   pubws.getSpYjXML(conn,fjbh, strwsbh, sjbh, ywlx, spFieldname, result, approverole, stepSequence,PubWS.FIELDEXTEN_CONTENT,autodoApprove,user);
           } else {
        	   if(Pub.empty(autodoApprove)&&!Pub.empty(qzFieldName)){
        		   autodoApprove = "1";
        	   }
        	   if(spFieldname!=null && spFieldname.length()>=0)
            	   pubws.getSpYjXML(conn,fjbh, strwsbh, sjbh, ywlx, spFieldname, result, approverole, stepSequence,PubWS.FIELDEXTEN_MIND,autodoApprove,user);
           }
           
          
        }

        switch (Pub.toInt(ywlx)) {
          default:
            break;
        }

    }
    
    public String getFieldname(Connection conn,String wsid,String approverole,String stepSequence,String flag) throws Exception
    {
    	String sql = "select fieldname from WS_TEMPLATE_SQL where wswh_flag = '"+flag+"' and ws_template_id = '"+wsid+"' and APPROVELEVEL = '"+stepSequence+"' ";
    	String str[][] = DBUtil.query(conn, sql);
    	if(str!=null)    		
    		return str[0][0];
    	else
    		return null;
    }

}