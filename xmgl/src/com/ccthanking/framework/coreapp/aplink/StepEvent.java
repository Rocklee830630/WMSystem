package com.ccthanking.framework.coreapp.aplink;

public interface StepEvent
{
	public void onOpen(Step step) throws Exception;
	public void onExecute(Step step)throws Exception;
	public void onClose(Step step)throws Exception;
	public void onDelete(Step step)throws Exception;
	public boolean onPreCondition1(Step step)throws Exception;
	public boolean onPreCondition2(Step step)throws Exception;
	public boolean onPreCondition3(Step step)throws Exception;
}