package com.ccthanking.framework.coreapp.aplink;

import java.util.*;

public class ProcessEventImpl implements ProcessEvent
{

	public void onCreate(Process process)
	{
		System.out.println("Process event onCreate loaded...");
	}

	public void onOpen(Process process)
	{
		System.out.println("Process event onOpen loaded...");
	}

	public void onClose(Process process) throws Exception
	{
		System.out.println("Process event onClose loaded...");
/*        String sjbh = process.getEventID();
        TaskMgrBean taskmgr = new TaskMgrBean();
        Collection tasks = taskmgr.getTasks(sjbh,"",TaskVO.TASK_TYPE_APPROVE);
        ((TaskVO)tasks.toArray()[tasks.toArray().length-1]).setLINKURL("/aaa.jsp");*/
	}

    public void onClose(Process process,TaskVO vo) throws Exception
    {
        System.out.println("Process event onClose loaded...with taskVo");
/*        String sjbh = process.getEventID();
        TaskMgrBean taskmgr = new TaskMgrBean();
        Collection tasks = taskmgr.getTasks(sjbh,"",TaskVO.TASK_TYPE_APPROVE);
        ((TaskVO)tasks.toArray()[tasks.toArray().length-1]).setLINKURL("/aaa.jsp");*/
    }

	public void onDelete(Process process)
	{
		System.out.println("Process event onDelete loaded...");
	}
	public boolean onPreCondition1(Process process)
	{
		return true;
	}

	public boolean onPreCondition2(Process process)
	{
		return true;
	}

	public boolean onPreCondition3(Process process)
	{
		return true;
	}
}