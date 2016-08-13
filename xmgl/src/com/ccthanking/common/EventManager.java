package com.ccthanking.common;

import java.sql.Connection;

import com.ccthanking.common.vo.EventVO;
import com.ccthanking.framework.Globals;
import com.ccthanking.framework.base.BaseDAO;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.OrgDept;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.util.Pub;
import com.ccthanking.framework.util.RandomGUID;

public class EventManager
{
    private EventManager()
    {
    }

    public static EventVO createEvent(Connection conn,String ywlx, User user)
        throws Exception
    {
        EventVO event = new EventVO();
        event.setYwlx(ywlx);
        event.setSlsj(Pub.getCurrentDate());
        event.setCzybh(user.getAccount());
        event.setCzyxm(user.getName());
        OrgDept dept = user.getOrgDept();
        if(dept.getDeptLevel() > 1)
        {
            event.setSjdm(Pub.getDeptParentSjdm(dept));
        }
        if(dept.getDeptLevel() > 2)
        {
            event.setFjdm(Pub.getDeptParentFjdm(dept));
        }
        event.setPcsdm(dept.getDeptID());
        event.setSjzt(Globals.EVENT_STATE_REGISTER);
        event.setSsxq(user.getDepartment().substring(0,6));
        return createEvent(conn,event,user);
    }

    public static EventVO createEvent(Connection conn,String ywlx,String sqrbh,String sqrxm,String sqrsfhm,User user)
        throws Exception
    {
        EventVO event = new EventVO();
        event.setYwlx(ywlx);
        event.setSlsj(Pub.getCurrentDate());
        event.setCzybh(user.getAccount());
        event.setCzyxm(user.getName());
        OrgDept dept = user.getOrgDept();
        if(dept.getDeptLevel() > 1)
        {
            event.setSjdm(Pub.getDeptParentSjdm(dept));
        }
        if(dept.getDeptLevel() > 2)
        {
            event.setFjdm(Pub.getDeptParentFjdm(dept));
        }
        event.setPcsdm(dept.getDeptID());
        event.setSjzt(Globals.EVENT_STATE_REGISTER);
        event.setSsxq(user.getDepartment().substring(0,6));
        event.setSbrbh(sqrbh);
        event.setSbrxm(sqrxm);
        event.setSbrsfhm(sqrsfhm);
        return createEvent(conn,event,user);
    }

    public static EventVO createEvent(Connection conn,EventVO event,User user)
        throws Exception
    {
        if(event.getSlsj() == null)
            event.setSlsj(Pub.getCurrentDate());
        if(user != null)
        {
            event.setCzybh(user.getAccount());
            event.setCzyxm(user.getName());
            OrgDept dept = user.getOrgDept();
            if(dept.getDeptLevel() > 1)
            {
                event.setSjdm(Pub.getDeptParentSjdm(dept));
            }
            if(dept.getDeptLevel() > 2)
            {
                event.setFjdm(Pub.getDeptParentFjdm(dept));
            }
            event.setPcsdm(dept.getDeptID());
            event.setSjzt(Globals.EVENT_STATE_REGISTER);
            event.setSsxq(user.getDepartment().substring(0,6));
        }
        return saveEvent(conn,event);
    }
    
    public static String getPkValue(Connection conn) throws Exception{
    	if(conn == null)
    		throw new Exception("connection 为空！");
    	int iTemp;
    	String res;
    	try {
    		String sequence = DBUtil.getSequenceValue("EVENT_ID_S", conn);
    		iTemp = 100000000 + Integer.parseInt(sequence, 10);
    		res = Pub.getDate("yyyyMMdd") + (""+iTemp).substring(1);
    		return res;
		} finally{
		}
    }
    
    private static EventVO saveEvent(Connection conn,EventVO event)
        throws Exception
    {
        if(event.getSjbh() == null){
        	event.setSjbh(new RandomGUID().toString());
            BaseDAO.insert(conn,event);
        }
        else
            BaseDAO.update(conn,event);
        return event;
    }

    public static void updateEvent(Connection conn,EventVO event,User user)
        throws Exception
    {
        if(event.getSjbh() == null) throw new Exception("Cann't update event!");
        if(user != null)
        {
            event.setCzybh(user.getAccount());
            event.setCzyxm(user.getName());
            OrgDept dept = user.getOrgDept();
            if(dept.getDeptLevel() > 1)
            {
                event.setSjdm(Pub.getDeptParentSjdm(dept));
            }
            if(dept.getDeptLevel() > 2)
            {
                event.setFjdm(Pub.getDeptParentFjdm(dept));
            }
            event.setPcsdm(dept.getDeptID());
            event.setSjzt(Globals.EVENT_STATE_REGISTER);
            event.setSsxq(user.getDepartment().substring(0,6));
        }
        saveEvent(conn,event);
    }

    public static void setEventState(Connection conn,String sjbh,String sjzt)
        throws Exception
    {
        EventVO event = getEventByID(conn,sjbh);
        event.setSjzt(sjzt);
        saveEvent(conn,event);
    }

    public static EventVO getEventByID(String sjbh)
        throws Exception
    {
        Connection conn = null;
        try
        {
            conn = DBUtil.getConnection();
            return getEventByID(conn,sjbh);
        }
        finally
        {
            if(conn != null)
                conn.close();
            conn = null;
        }
    }

    public static EventVO getEventByID(Connection conn,String sjbh)
        throws Exception
    {
        EventVO event = new EventVO();
        event.setSjbh(sjbh);
        return (EventVO) BaseDAO.getVOByPrimaryKey(conn,event);
    }

    public static EventVO archiveEvent(Connection conn,String sjbh,User user)
        throws Exception
    {
        EventVO event = getEventByID(conn,sjbh);
        return archiveEvent(conn,event,user);
    }

    public static EventVO archiveEvent(Connection conn,EventVO event,User user)
        throws Exception
    {
        if(user != null)
        {
            event.setGddwbh(user.getDepartment());
            event.setGdrbh(user.getAccount());
            event.setGdrxm(user.getName());
        }
        event.setGdsj(Pub.getCurrentDate());
        event.setSjzt(Globals.EVENT_STATE_FINISHED);
        return saveEvent(conn,event);
    }

    public static EventVO deleteEvent(Connection conn,String sjbh,User user)
        throws Exception
    {
        EventVO event = getEventByID(conn,sjbh);
        return deleteEvent(conn,event,user);
    }

    public static EventVO deleteEvent(Connection conn,EventVO event,User user)
        throws Exception
    {
        if(user != null)
        {
            event.setGddwbh(user.getDepartment());
            event.setGdrbh(user.getAccount());
            event.setGdrxm(user.getName());
        }
        event.setGdsj(Pub.getCurrentDate());
        event.setSjzt(Globals.EVENT_STATE_CANCELED);
        return saveEvent(conn,event);
    }
}