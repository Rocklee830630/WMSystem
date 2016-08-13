package com.ccthanking.business.xmcbk.vo;

import com.ccthanking.framework.base.BaseVO;

public class XmcbkSpVO extends BaseVO {
	public XmcbkSpVO(){
		this.addField("GC_TCJH_XMCBK_SP_ID",OP_STRING|this.TP_PK);
		this.addField("PCBSJBH",OP_STRING);
		this.addField("PCID",OP_STRING);
		this.addField("XMCBKID",OP_STRING);
		this.addField("SPXXID",OP_STRING);
		this.addField("SPSJBH",OP_STRING);
		this.setVOTableName("GC_TCJH_XMCBK_SP");
	}
	public void setGc_tcjh_xmcbk_sp_id(String gc_tcjh_xmcbk_sp_id){
		this.setInternal("GC_TCJH_XMCBK_SP_ID",gc_tcjh_xmcbk_sp_id);
	}
	public String getGc_tcjh_xmcbk_sp_id(){
		return (String)this.getInternal("GC_TCJH_XMCBK_SP_ID");
	}
	public void setPcbsjbh(String pcbsjbh){
		this.setInternal("PCBSJBH",pcbsjbh);
	}
	public String getPcbsjbh(){
		return (String)this.getInternal("PCBSJBH");
	}
	public void setPcid(String pcid){
		this.setInternal("PCID",pcid);
	}
	public String getPcid(){
		return (String)this.getInternal("PCID");
	}
	public void setXmcbkid(String xmcbkid){
		this.setInternal("XMCBKID",xmcbkid);
	}
	public String getXmcbkid(){
		return (String)this.getInternal("XMCBKID");
	}
	public void setSpxxid(String spxxid){
		this.setInternal("SPXXID",spxxid);
	}
	public String getSpxxid(){
		return (String)this.getInternal("SPXXID");
	}
	public void setSpsjbh(String spsjbh){
		this.setInternal("SPSJBH",spsjbh);
	}
	public String getSpsjbh(){
		return (String)this.getInternal("SPSJBH");
	}
}
