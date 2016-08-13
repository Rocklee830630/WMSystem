package com.ccthanking.common.timer;

import java.sql.Connection;
import java.util.Date;

import com.ccthanking.business.wttb.vo.WttbInfoVO;
import com.ccthanking.business.wttb.vo.WttbLzlsVO;
import com.ccthanking.framework.base.BaseDAO;
import com.ccthanking.framework.base.BaseVO;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.coreapp.orgmanage.UserManager;
import com.ccthanking.framework.coreapp.orgmanage.org_dept.OrgDeptVO;
import com.ccthanking.framework.util.Pub;
import com.ccthanking.framework.util.RandomGUID;

public class TimerService {
    private String para ; 
    
    /**
     * 问题提报-问题性质定时器
     * @throws Exception
     */
	public void wtxzTimer() throws Exception{
		Connection conn = null;
		try {
			conn = DBUtil.getConnection();
			wtxzUpgrade(conn,3,"11","12");
			wtxzUpgrade(conn,5,"12","13");
			updateCqbz(conn);//修改超期标识
			conn.commit();
		} catch (Exception e) {
			// TODO: handle exception
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace();
		}finally{
			DBUtil.closeConnetion(conn);
		}
	}
	/**
	 * 问题性质升级方法
	 * @param conn	数据库连接
	 * @param minDays	最小天数
	 * @param wtxzOld	现在的问题性质
	 * @param wtxzNew	升级后的问题性质
	 * @throws Exception
	 */
	private void wtxzUpgrade(Connection conn,int minDays,String wtxzOld,String wtxzNew) throws Exception{
		BaseVO[] bv = null;
		Date sysdate = Pub.getCurrentDate();
		WttbInfoVO condVO = new WttbInfoVO();
		if(conn==null){
			conn = DBUtil.getConnection();
		}
		condVO.setWtxz(wtxzOld);
		condVO.setSfyx("1");
		if(condVO!=null&&!condVO.isEmpty()){
			bv = (BaseVO [])BaseDAO.getVOByCondition(conn, condVO);
		}
		if(bv!=null){
			for(int i=0;i<bv.length;i++){
				WttbInfoVO vo = (WttbInfoVO)bv[i];
				if(sysdate.after(vo.getYjsj())){
					int realDays = Pub.daysBetween(vo.getYjsj(), sysdate);
					if(realDays>minDays){
						WttbInfoVO newvo = new WttbInfoVO();
						newvo.setWttb_info_id(vo.getWttb_info_id());
						newvo.setWtxz(wtxzNew);
						newvo.setYwtxz(wtxzOld);
						BaseDAO.update(conn, newvo);
						//自动抄送功能
						//1、一般上升到严重，那么抄送给部门分管主任
						//2、严重上升到极其严重，那么抄送给建管中心主任
						//----------------------------------获取自动抄送的用户名
						String jsrAccount = "";
						OrgDeptVO dvo = new OrgDeptVO();
						dvo.setRowId(vo.getLrbm());
						dvo = (OrgDeptVO)BaseDAO.getVOByPrimaryKey(conn, dvo);
						if("12".equals(wtxzNew)){
							jsrAccount = dvo.getFgzr();
						}else if("13".equals(wtxzNew)){
							jsrAccount = dvo.getYbzr();
						}
						//----------------------------------获取接收人
						User jsrUser = UserManager.getInstance().getUserByLoginNameFromNc(jsrAccount);
						//----------------------------------记录流转历史
						WttbLzlsVO lzvo = new WttbLzlsVO();
						lzvo.setWttb_lzls_id(new RandomGUID().toString());
						lzvo.setWtid(vo.getWttb_info_id());
						lzvo.setFsr(vo.getLrr());
						lzvo.setFsbm(vo.getLrbm());
						lzvo.setFssj(sysdate);
						lzvo.setJsr(jsrUser.getAccount());
						lzvo.setJsbm(jsrUser.getDepartment());
						lzvo.setJssj(sysdate);
						lzvo.setBlrjs("2");
						lzvo.setLrr(vo.getLrr());
						lzvo.setLrbm(vo.getLrbm());
						lzvo.setLrbmmc(vo.getLrbmmc());
						lzvo.setLrsj(vo.getLrsj());
						lzvo.setGxr(vo.getLrr());
						lzvo.setGxbm(vo.getLrbm());
						lzvo.setGxbmmc(vo.getLrbmmc());
						lzvo.setGxsj(sysdate);
						lzvo.setYwlx(vo.getYwlx());
						lzvo.setSjbh(vo.getSjbh());
						lzvo.setSfyx("1");
						lzvo.setXwwcsj(vo.getYjsj());
						BaseDAO.insert(conn, lzvo);
					}
				}
			}
		}
	}
	private void updateCqbz(Connection conn) throws Exception{
		try{
			String sql0 = "update WTTB_INFO set CQBZ='0' where CQBZ is null ";
			DBUtil.execSql(conn, sql0);
			String sql = "update WTTB_INFO set CQBZ='1' where WTTB_INFO_ID in ( " +
					"select WTTB_INFO_ID from WTTB_INFO I where to_char(trunc(decode(SJSJ, null, sysdate, SJSJ) - I.YJSJ))>7 and WTXZ='11' and SJZT!='3' " +
					"union all " +
					"select WTTB_INFO_ID from WTTB_INFO I where to_char(trunc(decode(SJSJ, null, sysdate, SJSJ) - I.YJSJ))>5 and WTXZ='12' and SJZT!='3' " +
					"union all " +
					"select WTTB_INFO_ID from WTTB_INFO I where to_char(trunc(decode(SJSJ, null, sysdate, SJSJ) - I.YJSJ))>3 and WTXZ='13' and SJZT!='3') ";
			DBUtil.execSql(conn, sql);
		}catch(Exception e){
			e.printStackTrace();
			throw e;
		}
	}
	public String getPara() {
		return para;
	}
	public void setPara(String para) {
		this.para = para;
	}
}
