package com.ccthanking.business.xtbg.txl.vo;

import com.ccthanking.framework.base.BaseVO;

/**
 * @auther xhb 
 */
public class TxlGroupVo extends BaseVO {
	
	public TxlGroupVo() {
		this.addField("GROUPID",OP_STRING|this.TP_PK);
		this.addField("ZMC",OP_STRING);
		this.addField("USERID",OP_STRING);
		this.addField("TXID",OP_STRING);
		this.addField("ISPUBLIC",OP_STRING);
		
		this.setVOTableName("XTBG_TXL_GROUP");
	}

	public void setGroupid(String groupid){
		this.setInternal("GROUPID",groupid);
	}
	public String getGroupid(){
		return (String)this.getInternal("GROUPID");
	}
	public void setZmc(String zmc){
		this.setInternal("ZMC",zmc);
	}
	public String getZmc(){
		return (String)this.getInternal("ZMC");
	}
	public void setUserid(String userid){
		this.setInternal("USERID",userid);
	}
	public String getUserid(){
		return (String)this.getInternal("USERID");
	}
	public void setTxid(String txid){
		this.setInternal("TXID",txid);
	}
	public String getTxid(){
		return (String)this.getInternal("TXID");
	}
	public void setIspublic(String ispublic){
		this.setInternal("ISPUBLIC",ispublic);
	}
	public String getIspublic(){
		return (String)this.getInternal("ISPUBLIC");
	}
}
