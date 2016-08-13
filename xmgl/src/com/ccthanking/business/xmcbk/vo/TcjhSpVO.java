package com.ccthanking.business.xmcbk.vo;
import com.ccthanking.framework.base.BaseVO;
import java.util.Date;

public class TcjhSpVO extends BaseVO{

	public TcjhSpVO(){
		this.addField("GC_TCJH_SP_ID",OP_STRING|this.TP_PK);
		this.addField("SPMC",OP_STRING);
		this.addField("ISXD",OP_STRING);
		this.addField("SJBH",OP_STRING);
		this.addField("YWLX",OP_STRING);
		this.addField("PCH",OP_STRING);
		this.addField("ND",OP_STRING);
		this.addField("SFYX",OP_STRING);
		this.addField("SPLX",OP_STRING);
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
		this.addField("SPFS",OP_STRING);
		this.setFieldDateFormat("LRSJ","yyyy-MM-dd");
		this.setFieldDateFormat("GXSJ","yyyy-MM-dd");
		this.setVOTableName("GC_TCJH_SP");
	}

	public void setGc_tcjh_sp_id(String gc_tcjh_sp_id){
		this.setInternal("GC_TCJH_SP_ID",gc_tcjh_sp_id);
	}
	public String getGc_tcjh_sp_id(){
		return (String)this.getInternal("GC_TCJH_SP_ID");
	}
	public void setSpmc(String spmc){
		this.setInternal("SPMC",spmc);
	}
	public String getSpmc(){
		return (String)this.getInternal("SPMC");
	}
	public void setIsxd(String isxd){
		this.setInternal("ISXD",isxd);
	}
	public String getIsxd(){
		return (String)this.getInternal("ISXD");
	}
	public void setSjbh(String sjbh){
		this.setInternal("SJBH",sjbh);
	}
	public String getSjbh(){
		return (String)this.getInternal("SJBH");
	}
	public void setYwlx(String ywlx){
		this.setInternal("YWLX",ywlx);
	}
	public String getYwlx(){
		return (String)this.getInternal("YWLX");
	}
	public void setPch(String pch){
		this.setInternal("PCH",pch);
	}
	public String getPch(){
		return (String)this.getInternal("PCH");
	}
	public void setNd(String nd){
		this.setInternal("ND",nd);
	}
	public String getNd(){
		return (String)this.getInternal("ND");
	}
	public void setSfyx(String sfyx){
		this.setInternal("SFYX",sfyx);
	}
	public String getSfyx(){
		return (String)this.getInternal("SFYX");
	}
	public void setSplx(String splx){
		this.setInternal("SPLX",splx);
	}
	public String getSplx(){
		return (String)this.getInternal("SPLX");
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
	public void setSpfs(String spfs){
		this.setInternal("SPFS",spfs);
	}
	public String getSpfs(){
		return (String)this.getInternal("SPFS");
	}
}