package com.ccthanking.business.xtbg.zxxw.vo;

import java.util.Date;

import com.ccthanking.framework.base.BaseVO;

/**
 * @auther xhb 
 */
public class ZxxwHfrVo extends BaseVO {
	
	public ZxxwHfrVo() {
		this.addField("NEWSID",OP_STRING);
		this.addField("USERID",OP_STRING);
		this.addField("HFRXM",OP_STRING);
		this.addField("HFNR",OP_STRING);
		this.addField("HFSJ",OP_DATE);
		
		this.setFieldDateFormat("HFSJ","yyyy-MM-dd");
		
		this.setVOTableName("XTBG_XXZX_ZXXW_HFR");
	}
	
	public void setNewsid(String newsid){
		this.setInternal("NEWSID",newsid);
	}
	public String getNewsid(){
		return (String)this.getInternal("NEWSID");
	}
	public void setUserid(String userid){
		this.setInternal("USERID",userid);
	}
	public String getUserid(){
		return (String)this.getInternal("USERID");
	}
	public void setHfrxm(String hfrxm){
		this.setInternal("HFRXM",hfrxm);
	}
	public String getHfrxm(){
		return (String)this.getInternal("HFRXM");
	}
	public void setHfnr(String hfnr){
		this.setInternal("HFNR",hfnr);
	}
	public String getHfnr(){
		return (String)this.getInternal("HFNR");
	}
	public void setHfsj(Date hfsj){
		this.setInternal("HFSJ",hfsj);
	}
	public Date getHfsj(){
		return (Date)this.getInternal("HFSJ");
	}
}
