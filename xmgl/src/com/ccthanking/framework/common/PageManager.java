package com.ccthanking.framework.common;

public class PageManager implements java.io.Serializable
{

	public void setPageRows(int iPageRows)
	{
		this.iPageRows = iPageRows;
	}

	public int getPageRows()
	{
		return iPageRows;
	}

	public void setCountPage(int iCountPage)
	{
		this.iCountPage = iCountPage;
	}

	public int getCountPage()
	{
		return iCountPage;
	}

	public void setCurrentPage(int iCurrentPage)
	{
		this.iCurrentPage = iCurrentPage;
	}

	public int getCurrentPage()
	{
		return iCurrentPage;
	}

	public void setCountRows(int iCountRows)
	{
		this.iCountRows = iCountRows;
	}

	public int getCountRows()
	{
		return iCountRows;
	}

	public void setFilter(String strFilter)
	{
		this.strFilter = strFilter;
	}

	public String getFilter()
	{
		return strFilter;
	}

	public PageManager()
	{
		iPageRows = 10;
		iCountPage = 0;
		iCurrentPage = 1;
		iCountRows = 0;
		strFilter = "";
	}

	int iPageRows;
	int iCountPage;
	int iCurrentPage;
	int iCountRows;
	String strFilter;
}
