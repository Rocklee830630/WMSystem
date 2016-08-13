package com.ccthanking.framework.coreapp.aplink;

public interface ProcessEvent
{
	public void onCreate(Process process)throws Exception;
	public void onOpen(Process process)throws Exception;
	public void onClose(Process process)throws Exception;
	public void onDelete(Process process)throws Exception;
	public boolean onPreCondition1(Process process)throws Exception;
	public boolean onPreCondition2(Process process)throws Exception;
	public boolean onPreCondition3(Process process)throws Exception;

}