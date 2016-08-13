package com.ccthanking.business.xmcbk.vo;

import com.ccthanking.framework.base.BaseVO;
import java.util.Date;

public class PcbVO  extends BaseVO{
	public PcbVO(){
		this.addField("GC_CBK_PCB_ID",OP_STRING|this.TP_PK);
		this.addField("PCH",OP_STRING);
		this.addField("XDLX",OP_STRING);
		this.addField("XDRQ",OP_DATE);
		this.addField("XDWH",OP_STRING);
		this.addField("YWLX",OP_STRING);
		this.addField("SJBH",OP_STRING);
		this.addField("BZ",OP_STRING);
		this.addField("LRR",OP_STRING);
		this.addField("LRSJ",OP_DATE);
		this.addField("LRBM",OP_STRING);
		this.addField("LRBMMC",OP_STRING);
		this.addField("GXR",OP_STRING);
		this.addField("GXSJ",OP_DATE);
		this.addField("GXBM",OP_STRING);
		this.addField("GXBMMC",OP_STRING);
		this.addField("SJMJ",OP_STRING);
		this.addField("SFYX",OP_STRING);
		this.addField("ISXD",OP_STRING);
		this.addField("PCLX",OP_STRING);
		this.addField("PC_JHZTZE",OP_STRING);
		this.addField("PC_GC",OP_STRING);
		this.addField("PC_ZC",OP_STRING);
		this.addField("PC_QT",OP_STRING);
		this.setFieldDateFormat("XDRQ","yyyy-MM-dd");
		this.setFieldDateFormat("LRSJ","yyyy-MM-dd");
		this.setFieldDateFormat("GXSJ","yyyy-MM-dd");

		this.bindFieldToDic("PCH", "PCH");	
		this.setVOTableName("GC_CBK_PCB");
	}
	public void setGc_cbk_pcb_id(String gc_cbk_pcb_id){
		this.setInternal("GC_CBK_PCB_ID",gc_cbk_pcb_id);
	}
	public String getGc_cbk_pcb_id(){
		return (String)this.getInternal("GC_CBK_PCB_ID");
	}
	public void setPch(String pch){
		this.setInternal("PCH",pch);
	}
	public String getPch(){
		return (String)this.getInternal("PCH");
	}
	public void setXdlx(String xdlx){
		this.setInternal("XDLX",xdlx);
	}
	public String getXdlx(){
		return (String)this.getInternal("XDLX");
	}
	public void setXdrq(Date xdrq){
		this.setInternal("XDRQ",xdrq);
	}
	public Date getXdrq(){
		return (Date)this.getInternal("XDRQ");
	}
	public void setXdwh(String xdwh){
		this.setInternal("XDWH",xdwh);
	}
	public String getXdwh(){
		return (String)this.getInternal("XDWH");
	}
	public void setYwlx(String ywlx){
		this.setInternal("YWLX",ywlx);
	}
	public String getYwlx(){
		return (String)this.getInternal("YWLX");
	}
	public void setSjbh(String sjbh){
		this.setInternal("SJBH",sjbh);
	}
	public String getSjbh(){
		return (String)this.getInternal("SJBH");
	}
	public void setBz(String bz){
		this.setInternal("BZ",bz);
	}
	public String getBz(){
		return (String)this.getInternal("BZ");
	}
	public void setLrr(String lrr){
		this.setInternal("LRR",lrr);
	}
	public String getLrr(){
		return (String)this.getInternal("LRR");
	}
	public void setLrsj(Date lrsj){
		this.setInternal("LRSJ",lrsj);
	}
	public Date getLrsj(){
		return (Date)this.getInternal("LRSJ");
	}
	public void setLrbm(String lrbm){
		this.setInternal("LRBM",lrbm);
	}
	public String getLrbm(){
		return (String)this.getInternal("LRBM");
	}
	public void setLrbmmc(String lrbmmc){
		this.setInternal("LRBMMC",lrbmmc);
	}
	public String getLrbmmc(){
		return (String)this.getInternal("LRBMMC");
	}
	public void setGxr(String gxr){
		this.setInternal("GXR",gxr);
	}
	public String getGxr(){
		return (String)this.getInternal("GXR");
	}
	public void setGxsj(Date gxsj){
		this.setInternal("GXSJ",gxsj);
	}
	public Date getGxsj(){
		return (Date)this.getInternal("GXSJ");
	}
	public void setGxbm(String gxbm){
		this.setInternal("GXBM",gxbm);
	}
	public String getGxbm(){
		return (String)this.getInternal("GXBM");
	}
	public void setGxbmmc(String gxbmmc){
		this.setInternal("GXBMMC",gxbmmc);
	}
	public String getGxbmmc(){
		return (String)this.getInternal("GXBMMC");
	}
	public void setSjmj(String sjmj){
		this.setInternal("SJMJ",sjmj);
	}
	public String getSjmj(){
		return (String)this.getInternal("SJMJ");
	}
	public void setSfyx(String sfyx){
		this.setInternal("SFYX",sfyx);
	}
	public String getSfyx(){
		return (String)this.getInternal("SFYX");
	}
	public void setIsxd(String isxd){
		this.setInternal("ISXD",isxd);
	}
	public String getIsxd(){
		return (String)this.getInternal("ISXD");
	}
	public void setPclx(String pclx){
		this.setInternal("PCLX",pclx);
	}
	public String getPclx(){
		return (String)this.getInternal("PCLX");
	}
	public void setPc_jhztze(String pc_jhztze){
		this.setInternal("PC_JHZTZE",pc_jhztze);
	}
	public String getPc_jhztze(){
		return (String)this.getInternal("PC_JHZTZE");
	}
	public void setPc_gc(String pc_gc){
		this.setInternal("PC_GC",pc_gc);
	}
	public String getPc_gc(){
		return (String)this.getInternal("PC_GC");
	}
	public void setPc_zc(String pc_zc){
		this.setInternal("PC_ZC",pc_zc);
	}
	public String getPc_zc(){
		return (String)this.getInternal("PC_ZC");
	}
	public void setPc_qt(String pc_qt){
		this.setInternal("PC_QT",pc_qt);
	}
	public String getPc_qt(){
		return (String)this.getInternal("PC_QT");
	}
}
