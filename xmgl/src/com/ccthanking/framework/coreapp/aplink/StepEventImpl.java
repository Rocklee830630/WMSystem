package com.ccthanking.framework.coreapp.aplink;

public class StepEventImpl implements StepEvent
{
	public void onOpen(Step step)
	{
		System.out.println("onOpen loaded...");
	}

	public void onExecute(Step step)
	{
		System.out.println("onExecute loaded...");
	}

	public void onClose(Step step)
	{
		System.out.println("onClose loaded...");
	}

	public void onDelete(Step step)
	{
		System.out.println("onDelete loaded...");
	}
	public boolean onPreCondition1(Step step)
	{
		return true;
	}

	public boolean onPreCondition2(Step step)
	{
		return true;
	}

	public boolean onPreCondition3(Step step)
	{
		return true;
	}
}