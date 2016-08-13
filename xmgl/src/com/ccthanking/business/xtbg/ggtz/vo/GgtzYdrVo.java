package com.ccthanking.business.xtbg.ggtz.vo;

import java.util.Date;

import com.ccthanking.framework.base.BaseVO;

/**
 * @auther xhb 
 */
public class GgtzYdrVo extends BaseVO {
	
	public GgtzYdrVo() {
		this.addField("GGID",OP_STRING|this.TP_PK);
		this.addField("USERID",OP_STRING|this.TP_PK);
		this.addField("LASTREADTIME",OP_DATE);
		
		this.setFieldDateFormat("LASTREADTIME","yyyy-MM-dd");
		
		this.setVOTableName("XTBG_XXZX_GGTZ_YDR");
	}
	
	public void setGgid(String ggid){
		this.setInternal("GGID",ggid);
	}
	public String getGgid(){
		return (String)this.getInternal("GGID");
	}
	public void setUserid(String userid){
		this.setInternal("USERID",userid);
	}
	public String getUserid(){
		return (String)this.getInternal("USERID");
	}
	public void setLastreadtime(Date lastreadtime){
		this.setInternal("LASTREADTIME",lastreadtime);
	}
	public Date getLastreadtime(){
		return (Date)this.getInternal("LASTREADTIME");
	}
}
