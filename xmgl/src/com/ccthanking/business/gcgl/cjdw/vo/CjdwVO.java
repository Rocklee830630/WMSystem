package com.ccthanking.business.gcgl.cjdw.vo;
import com.ccthanking.framework.base.BaseVO;
import java.util.Date;

public class CjdwVO extends BaseVO{

	public CjdwVO(){
		this.addField("GC_CJDW_ID",OP_STRING|this.TP_PK);
		this.addField("DWBH",OP_STRING);
		this.addField("DWMC",OP_STRING);
		this.addField("DWLX",OP_STRING);
		this.addField("CLSJ",OP_STRING);
		this.addField("DZ",OP_STRING);
		this.addField("DH",OP_STRING);
		this.addField("QYZZ",OP_STRING);
		this.addField("QYXZ",OP_STRING);
		this.addField("FZR",OP_STRING);
		this.addField("FZRDH",OP_STRING);
		this.addField("YWFW",OP_STRING);
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
		this.addField("ZBCS",OP_STRING);
		
		//字典
		this.bindFieldToDic("DWLX", "JGLB");
		this.bindFieldToDic("QYXZ", "QYXZ");
		this.setFieldDateFormat("LRSJ","yyyy-MM-dd");
		this.setFieldDateFormat("GXSJ","yyyy-MM-dd");
		this.setVOTableName("GC_CJDW");
	}

	public void setGc_cjdw_id(String gc_cjdw_id){
		this.setInternal("GC_CJDW_ID",gc_cjdw_id);
	}
	public String getGc_cjdw_id(){
		return (String)this.getInternal("GC_CJDW_ID");
	}
	public void setDwbh(String dwbh){
		this.setInternal("DWBH",dwbh);
	}
	public String getDwbh(){
		return (String)this.getInternal("DWBH");
	}
	public void setDwmc(String dwmc){
		this.setInternal("DWMC",dwmc);
	}
	public String getDwmc(){
		return (String)this.getInternal("DWMC");
	}
	public void setDwlx(String dwlx){
		this.setInternal("DWLX",dwlx);
	}
	public String getDwlx(){
		return (String)this.getInternal("DWLX");
	}
	public void setClsj(String clsj){
		this.setInternal("CLSJ",clsj);
	}
	public String getClsj(){
		return (String)this.getInternal("CLSJ");
	}
	public void setDz(String dz){
		this.setInternal("DZ",dz);
	}
	public String getDz(){
		return (String)this.getInternal("DZ");
	}
	public void setDh(String dh){
		this.setInternal("DH",dh);
	}
	public String getDh(){
		return (String)this.getInternal("DH");
	}
	public void setQyzz(String qyzz){
		this.setInternal("QYZZ",qyzz);
	}
	public String getQyzz(){
		return (String)this.getInternal("QYZZ");
	}
	public void setQyxz(String qyxz){
		this.setInternal("QYXZ",qyxz);
	}
	public String getQyxz(){
		return (String)this.getInternal("QYXZ");
	}
	public void setFzr(String fzr){
		this.setInternal("FZR",fzr);
	}
	public String getFzr(){
		return (String)this.getInternal("FZR");
	}
	public void setFzrdh(String fzrdh){
		this.setInternal("FZRDH",fzrdh);
	}
	public String getFzrdh(){
		return (String)this.getInternal("FZRDH");
	}
	public void setYwfw(String ywfw){
		this.setInternal("YWFW",ywfw);
	}
	public String getYwfw(){
		return (String)this.getInternal("YWFW");
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
	public void setZbcs(String zbcs){
		this.setInternal("ZBCS",zbcs);
	}
	public String getZbcs(){
		return (String)this.getInternal("ZBCS");
	}
}