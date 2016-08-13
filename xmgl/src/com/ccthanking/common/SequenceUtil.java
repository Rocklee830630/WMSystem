package com.ccthanking.common;
import java.sql.Connection;

import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.util.Pub;

public class SequenceUtil {

    public static String getCommonSerialNumber(Connection conn) throws Exception{
    	return DBUtil.getSequenceValue("seq_common_serival_number", conn);
    }
    
    public static String getCommonSerivalNumber(Connection conn) throws Exception
    {
        if(conn == null)
            throw new Exception("connection 为空！");
        com.ccthanking.framework.params.SysPara.SysParaConfigureVO sysParaVo;
        int iTemp;
        String res;
        try
        {
            sysParaVo = com.ccthanking.framework.params.ParaManager.getInstance().getSysParameter("DEPLOY_NODE");
            iTemp = 100000000 + Integer.parseInt(getCommonSerialNumber(conn), 10);
            //res = sysParaVo.getSysParaConfigureParavalue1().substring(0, 4) + Pub.getDate("yyyy") + ("" + iTemp).substring(1);
            res = sysParaVo.getSysParaConfigureParavalue1() + Pub.getDate("yyyy") + ("" + iTemp).substring(1);
            return res;
        }
        finally
        {
        }
    }
    
    public static String getCommonSerivalNumber() throws Exception
    {
    	com.ccthanking.framework.params.SysPara.SysParaConfigureVO sysParaVo;
    	int iTemp;
    	String res; 
        Connection conn = DBUtil.getConnection();
        try
        {
        	sysParaVo = com.ccthanking.framework.params.ParaManager.getInstance().getSysParameter("DEPLOY_NODE");
        	iTemp = 100000000 + Integer.parseInt(getCommonSerialNumber(conn), 10);
        	res = sysParaVo.getSysParaConfigureParavalue1().substring(0, 4) + Pub.getDate("yyyy") + ("" + iTemp).substring(1);
        	return res;
        }
        finally
        {
            if (conn != null)
                conn.close();
            conn = null;
        }
    }
    /*
     此类为测试使用的类，各个项目应该按照自己的需要来定义相应的取序列的类
     */

	/*
	 * 获取日期格式的序列值
	 */
	public static String getDateSequence(String sqName)throws Exception
	{
		java.sql.Connection conn = DBUtil.getConnection();
		String result="";
		try
		{
			result =com.ccthanking.framework.util.StringUtil.lPad(DBUtil.getSequenceValue(sqName, conn),6,"0");
			//result =DBUtil.getServerTime("yyyymmdd", conn)+(result+"000000").substring(0,6);
		}
		catch(Exception E)
		{
			throw new Exception(E.getMessage());
		}
        finally
        {
            try
            {
                if(conn != null)
                    conn.close();
                conn = null;
            }
            catch(Exception ex)
            {

            }
        }

		return result;
	}
	
	public static String getXXBH(){
		return "1";
	}
}
