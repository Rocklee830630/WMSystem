package com.ccthanking.business.xdxmk.vo;
import com.ccthanking.framework.base.BaseVO;
import java.util.Date;

public class DjzVO extends BaseVO{

	public DjzVO(){
		this.addField("GC_CBK_DJZ_ID",OP_STRING|this.TP_PK);
		this.addField("XMZJ",OP_STRING);
		this.addField("XMBH",OP_STRING);
		this.addField("XMMC",OP_STRING);
		this.addField("DCZLX",OP_STRING);
		this.addField("BZ",OP_STRING);
		this.setVOTableName("GC_CBK_DJZ");
	}

	public void setGc_cbk_djz_id(String gc_cbk_djz_id){
		this.setInternal("GC_CBK_DJZ_ID",gc_cbk_djz_id);
	}
	public String getGc_cbk_djz_id(){
		return (String)this.getInternal("GC_CBK_DJZ_ID");
	}
	public void setXmzj(String xmzj){
		this.setInternal("XMZJ",xmzj);
	}
	public String getXmzj(){
		return (String)this.getInternal("XMZJ");
	}
	public void setXmbh(String xmbh){
		this.setInternal("XMBH",xmbh);
	}
	public String getXmbh(){
		return (String)this.getInternal("XMBH");
	}
	public void setXmmc(String xmmc){
		this.setInternal("XMMC",xmmc);
	}
	public String getXmmc(){
		return (String)this.getInternal("XMMC");
	}
	public void setDczlx(String dczlx){
		this.setInternal("DCZLX",dczlx);
	}
	public String getDczlx(){
		return (String)this.getInternal("DCZLX");
	}
	public void setBz(String bz){
		this.setInternal("BZ",bz);
	}
	public String getBz(){
		return (String)this.getInternal("BZ");
	}
}