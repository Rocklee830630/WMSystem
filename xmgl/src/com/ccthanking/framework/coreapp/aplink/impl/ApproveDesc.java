package com.ccthanking.framework.coreapp.aplink.impl;

import java.sql.*;
import com.ccthanking.framework.util.*;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.common.vo.EventVO;
import com.ccthanking.common.EventManager;
import com.ccthanking.framework.coreapp.aplink.TaskVO; 

public class ApproveDesc
{
    public ApproveDesc()
    {
    }
    public String getDesc(Connection conn,String sjbh,String ywlx,String cjdwdm)throws Exception{
        String url = null;
        EventVO event = EventManager.getEventByID(conn,sjbh);
        String[][] urls = DBUtil.query(conn,
            "select url from ap_task_link where ywlx='" + ywlx +
            "' and (DEPTID='" +
            event.getPcsdm() +
            "' or DEPTID='0') and rwlx='0' order by DEPTID desc");
        if (urls == null)
        {
            url = "jsp/framework/aplink/defaultDetailPage.jsp";
        }
        else
        {
            url = urls[0][0];
        }
        String str = null;
        switch (Pub.toInt(ywlx)) {
          default:
              str="["+Pub.getDeptNameByID(cjdwdm) + " - "+Pub.getDeptNameByID(event.getPcsdm())+"] 办理 [" +Pub.getDictValueByCode("YWLX",ywlx)+"] 业务，<font color=red><a herf=\"#\" onclick=\"OpenDetail('"+sjbh+"','"+ywlx+"','"+url+"')\">详细信息</a></font>";
            break;
        }
        return str;
    }
    public static void main(String[] args)
    {
        ApproveDesc approveDesc1 = new ApproveDesc();
    }

}