package com.ccthanking.business.xmcbk.vo;

import java.util.Date;

import com.ccthanking.framework.base.BaseVO;

public class NdVO extends BaseVO {
	
	public NdVO()
	{
		this.addField("GC_CBK_ND",OP_STRING|this.TP_PK);
		this.addField("ND",OP_STRING);
		this.setVOTableName("GC_CBK_ND");
	}
	
	
	public void setId(String id){
		this.setInternal("GC_CBK_ND",id);
	}
	public String getId(){
		return (String)this.getInternal("GC_CBK_ND");
	}
	public void setNd(String nd){
		this.setInternal("ND",nd);
	}
	public String getNd(){
		return (String)this.getInternal("ND");
	}


}
