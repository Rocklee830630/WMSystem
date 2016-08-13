package com.ccthanking.framework.params;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.cache.Cache;
import com.ccthanking.framework.common.cache.CacheManager;
import com.ccthanking.framework.params.AppPara.AppParaVO;
import com.ccthanking.framework.params.SysPara.SysParaConfigureVO;
import com.ccthanking.framework.util.Pub;


public class ParaManager implements Cache
{
  private ArrayList usertable;
  private Hashtable systable;
  private ArrayList apptable;
  static private ParaManager instance;
  private static org.apache.log4j.Logger logger = org.apache.log4j.LogManager.getLogger("ParaManager");

  private ParaManager() {
    init();
  }
  private void init()
  {
    Connection conn = null;
    try
    {
      conn = DBUtil.getConnection();
      //logger.info("初始化系统参数信息...");
      String[][] list = DBUtil.query(conn, "select t.sn,t.apptype,t.application,t.parakey,t.paraname,t.paravalue1,t.paravalue2,t.paravalue3,t.paravalue4,t.memo from fs_para_sys_configure t");
      if(list != null)
      {
        systable = new Hashtable(list.length);
        for(int i=0;i<list.length;i++)
        {
          SysParaConfigureVO para = new SysParaConfigureVO();
          para.setSysParaConfigureSn(list[i][0]);
          para.setSysParaConfigureApptype(list[i][1]);
          para.setSysParaConfigureApplicateion(list[i][2]);
          para.setSysParaConfigureParakey(list[i][3]);
          para.setSysParaConfigureParaname(list[i][4]);
          para.setSysParaConfigureParavalue1(list[i][5]);
          para.setSysParaConfigureParavalue2(list[i][6]);
          para.setSysParaConfigureParavalue3(list[i][7]);
          para.setSysParaConfigureParavalue4(list[i][8]);
          para.setSysParaConfigureMemo(list[i][9]);
          systable.put(list[i][3],para);
        }
      }

      list = DBUtil.query(conn, "select t.sn,t.apptype,t.operationtype,t.unitid,"+
                          "t.orglevel,t.applicateion,t.parakey,t.paraname,t.paravalue1,"+
                          "t.paravalue2,t.paravalue3,t.paravalue4,t.memo from fs_para_app_configure t");
      if(list != null)
      {
        apptable = new ArrayList(list.length);
        for(int i=0;i<list.length;i++)
        {
          AppParaVO para = new AppParaVO();
          para.setAppParaSn(list[i][0]);
          para.setAppParaApptype(list[i][1]);
          para.setAppParaOperationtype(list[i][2]);
          para.setAppParaUnitid(list[i][3]);
          para.setAppParaOrglevel(list[i][4]);
          para.setAppParaApplicateion(list[i][5]);
          para.setAppParaParakey(list[i][6]);
          para.setAppParaParaname(list[i][7]);
          para.setAppParaParavalue1(list[i][8]);
          para.setAppParaParavalue2(list[i][9]);
          para.setAppParaParavalue3(list[i][10]);
          para.setAppParaParavalue4(list[i][11]);
          para.setAppParaMemo(list[i][12]);
          apptable.add(para);
        }
      }
/*
      logger.info("初始化用户参数信息...");
      list = DBUtil.query(conn, "select t.sn,t.apptype,t.useraccount,t.username,"+
                          "t.userid,t.userlevel,t.application,t.parakey,t.paraname,"+
                          "t.paravalue1,t.paravalue2,t.paravalue3,"+
                          "t.paravalue4,t.memo from user_para_configure t");
      if(list != null)
      {
        usertable = new ArrayList(list.length);
        for(int i=0;i<list.length;i++)
        {
        	UserParaConfigureVO para = new UserParaConfigureVO();
          para.setUserParaConfigureSn(list[i][0]);
          para.setUserParaConfigureApptype(list[i][1]);
          para.setUserParaConfigureUseraccount(list[i][2]);
          para.setUserParaConfigureUsername(list[i][3]);
          para.setUserParaConfigureUserid(list[i][4]);
          para.setUserParaConfigureUserlevel(list[i][5]);
          para.setUserParaConfigureApplication(list[i][6]);
          para.setUserParaConfigureParakey(list[i][7]);
          para.setUserParaConfigureParaname(list[i][8]);
          para.setUserParaConfigureParavalue1(list[i][9]);
          para.setUserParaConfigureParavalue2(list[i][10]);
          para.setUserParaConfigureParavalue3(list[i][11]);
          para.setUserParaConfigureParavalue4(list[i][12]);
          para.setUserParaConfigureMemo(list[i][13]);
          usertable.add(para);
        }
      }
*/
    }
    catch(Exception e)
    {
    	logger.info(e.getMessage());
      e.printStackTrace(System.out);
    }
    finally
    {
      if (conn != null) {
        try {
          conn.close();
        }
        catch (SQLException ex) {
        }
      }
    }
  }
  public SysParaConfigureVO getSysParameter(String key)
  {
    return systable==null?null: (SysParaConfigureVO) systable.get(key);
  }
/*
  public void setUserParameter(UserParaConfigureVO vo)
  {
    if(vo == null) return;
    UserParaConfigureVO oldvo = getUserParameter(vo.getUserParaConfigureUseraccount(),vo.getUserParaConfigureParakey());
    if(oldvo != null) usertable.remove(oldvo);
    usertable.add(vo);
  }
 */
  public void setSysParameter(SysParaConfigureVO vo)
  {
    if(vo == null) return;
    systable.put(vo.getSysParaConfigureParakey(),vo);
  }
/*
  public void deleteUserParameter(UserParaConfigureVO vo)
  {
    if(vo != null) usertable.remove(vo);
  }
        */
  public void deleteSysParameter(SysParaConfigureVO vo)
  {
    if(vo != null) systable.remove(vo);
  }
/*
  public UserParaConfigureVO getUserParameter(String userAccount,String key)
  {
    if(Pub.empty(userAccount) || Pub.empty(key))
      return null;
    UserParaConfigureVO vo = null;
    Iterator itor = usertable.iterator();
    while(itor.hasNext())
    {
      vo = (UserParaConfigureVO) itor.next();
      if(userAccount.equals(vo.getUserParaConfigureUseraccount()) && key.equals(vo.getUserParaConfigureParakey()))
        return vo;
    }
    return null;
  }
        */
  static synchronized public ParaManager getInstance()
  {
      if (instance == null)
      {
          instance = new ParaManager();
          CacheManager.register(CacheManager.CACHE_PARAMS,instance);
      }
      return instance;
  }

  public void setAppParameter(AppParaVO vo)
  {
    if(vo == null) return;
    AppParaVO oldvo = getAppParameter(vo.getAppParaSn());
    if(oldvo != null) apptable.remove(oldvo);
    apptable.add(vo);
  }
  public void deleteAppParameter(AppParaVO vo)
  {
    if(vo != null) apptable.remove(vo);
  }
  public AppParaVO[] getAppParameter(String deptID,String ywlx,String key)
  {
    if(Pub.empty(key)  || Pub.empty(ywlx)) return null;//|| Pub.empty(deptID)
    ArrayList list = new ArrayList();
    if(apptable!=null){
        Iterator itor = apptable.iterator();
        while (itor.hasNext()) {
            AppParaVO vo = (AppParaVO) itor.next();
            if (ywlx.equals(vo.getAppParaOperationtype()) &&
//                deptID.equals(vo.getAppParaUnitid()) &&
                key.equals(vo.getAppParaParakey()))
                list.add(vo);
        }
    }
    if(list.size()==0) return null;
    AppParaVO[] arr = new AppParaVO[list.size()];
    for(int i=0;i<arr.length;i++)
    {
      arr[i]=(AppParaVO)list.get(i);
    }
    return arr;
  }
  public AppParaVO getAppParameter(String oid)
  {
    if(Pub.empty(oid)) return null;
    Iterator itor = apptable.iterator();
    while(itor.hasNext())
    {
      AppParaVO vo = (AppParaVO) itor.next();
      if(oid.equals(vo.getAppParaSn())) return vo;
    }
    return null;
  }

    public void synchronize(String data, int action)
    {
        init();
    }
	public void reBuildMemory() throws Exception {
		// TODO Auto-generated method stub
		if(systable != null){
			systable.clear();
			systable = null;
		}
		if(apptable != null){
			apptable.clear();
			apptable = null;
		}
		 init();
	}
}
