package com.ccthanking.business.clzx.vo;
import com.ccthanking.framework.base.BaseVO;
import java.util.Date;

public class ClzxRwbVO extends BaseVO{

	public ClzxRwbVO(){
		this.addField("CLZX_RWB_ID",OP_STRING|this.TP_PK);
		this.addField("YWBID",OP_STRING);
		this.addField("RYID",OP_STRING);
		this.addField("GLID",OP_STRING);
		this.addField("FILTER",OP_STRING);
		this.addField("RWZT",OP_STRING);
		this.addField("SJWYBH",OP_STRING);
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
		this.setFieldDateFormat("LRSJ","yyyy-MM-dd");
		this.setFieldDateFormat("GXSJ","yyyy-MM-dd");
		this.setVOTableName("CLZX_RWB");
	}

	public void setClzx_rwb_id(String clzx_rwb_id){
		this.setInternal("CLZX_RWB_ID",clzx_rwb_id);
	}
	public String getClzx_rwb_id(){
		return (String)this.getInternal("CLZX_RWB_ID");
	}
	public void setYwbid(String ywbid){
		this.setInternal("YWBID",ywbid);
	}
	public String getYwbid(){
		return (String)this.getInternal("YWBID");
	}
	public void setRyid(String ryid){
		this.setInternal("RYID",ryid);
	}
	public String getRyid(){
		return (String)this.getInternal("RYID");
	}
	public void setGlid(String glid){
		this.setInternal("GLID",glid);
	}
	public String getGlid(){
		return (String)this.getInternal("GLID");
	}
	public void setFilter(String filter){
		this.setInternal("FILTER",filter);
	}
	public String getFilter(){
		return (String)this.getInternal("FILTER");
	}
	public void setRwzt(String rwzt){
		this.setInternal("RWZT",rwzt);
	}
	public String getRwzt(){
		return (String)this.getInternal("RWZT");
	}
	public void setSjwybh(String sjwybh){
		this.setInternal("SJWYBH",sjwybh);
	}
	public String getSjwybh(){
		return (String)this.getInternal("SJWYBH");
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
}