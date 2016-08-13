package com.ccthanking.framework.spflow;

import java.util.*;
import javax.servlet.http.*;

import com.ccthanking.framework.log.LogManager;
import org.apache.struts.action.*;
import com.ccthanking.framework.base.*;
import org.dom4j.*;
import com.ccthanking.framework.dic.*;
import com.ccthanking.framework.message.messagemgr.sendMessage;
import com.ccthanking.framework.util.*;

import java.io.OutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import com.ccthanking.framework.*;
import com.ccthanking.framework.common.*;
import com.ccthanking.framework.coreapp.aplink.TaskMgrBean;
import com.ccthanking.framework.coreapp.aplink.TaskVO;
import com.ccthanking.common.EventManager;
import com.ccthanking.common.SequenceUtil;
import com.ccthanking.common.vo.*;

public class ChangeSP
{
    public ChangeSP()
    {
    }

    /**
     * 改变审批级数操作
     * @param sjbh
     * @param ywlx
     * @return
     * @throws java.lang.Exception
     */
    public static String change(Connection conn, String sjbh, String ywlx)
        throws Exception
    {
        if (ywlx.equals("040321"))
        {
            // 处理意见
            return change040321(conn, sjbh, ywlx);
        }
        return null;
    }

    private static String change040321(Connection conn, String sjbh,
                                       String ywlx)
        throws Exception
    {
        // 清空当前处理意见关联的处罚信息的处理意见序号和处理意见审批级数字段
        String selSql = "select m.xzcfxh from ZA_ZFBA_XZAJ_WS_XZCFXX m, ZA_ZFBA_XZAJ_WS_CLYJDJ n"
            +" where m.zhux='0' and m.clyjxh = n.clyjxh and n.sjbh = '"+sjbh+"'";
        String[][] selRes = DBUtil.query(conn,selSql);
        if(selRes == null){
            return "处理意见审批下无处罚信息 \n此处理意见可能由其他处理意见审批代替！";
        }
        String updateSql = "update  ZA_ZFBA_XZAJ_WS_XZCFXX m "
            + " set m.clyjxh = '', m.clyjspjs = ''"
            + " where m.zhux='0' and m.clyjxh = ( select n.clyjxh from ZA_ZFBA_XZAJ_WS_CLYJDJ n where n.sjbh = '" +
            sjbh + "')";

        if (DBUtil.execSql(conn, updateSql))
        {
            return "请重新登记处理意见信息并发起审批； \n \n变更审批级数成功；";
        }
        else
        {
            return "变更审批级数失败";
        }
    }
}