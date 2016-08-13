package com.ccthanking.business.xmglgs.xxjd.vo;
import com.ccthanking.framework.base.BaseVO;
import java.util.Date;

public class ZdyjdVO extends BaseVO{

	public ZdyjdVO(){
		this.addField("GC_XMGLGS_XXJD_ZDYJD_ID",OP_STRING|this.TP_PK);
		this.addField("JDMC",OP_STRING);
		this.addField("JHSJ",OP_DATE);
		this.addField("SJSJ",OP_DATE);
		this.addField("CZWT",OP_STRING);
		this.addField("YWLX",OP_STRING);
		this.addField("SJBH",OP_STRING);
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
		this.addField("JHBZID",OP_STRING);
		this.setFieldDateFormat("JHSJ","yyyy-MM-dd");
		this.setFieldDateFormat("SJSJ","yyyy-MM-dd");
		this.setFieldDateFormat("LRSJ","yyyy-MM-dd");
		this.setFieldDateFormat("GXSJ","yyyy-MM-dd");
		this.setVOTableName("GC_XMGLGS_XXJD_ZDYJD");
	}

	public void setGc_xmglgs_xxjd_zdyjd_id(String gc_xmglgs_xxjd_zdyjd_id){
		this.setInternal("GC_XMGLGS_XXJD_ZDYJD_ID",gc_xmglgs_xxjd_zdyjd_id);
	}
	public String getGc_xmglgs_xxjd_zdyjd_id(){
		return (String)this.getInternal("GC_XMGLGS_XXJD_ZDYJD_ID");
	}
	public void setJdmc(String jdmc){
		this.setInternal("JDMC",jdmc);
	}
	public String getJdmc(){
		return (String)this.getInternal("JDMC");
	}
	public void setJhsj(Date jhsj){
		this.setInternal("JHSJ",jhsj);
	}
	public Date getJhsj(){
		return (Date)this.getInternal("JHSJ");
	}
	public void setSjsj(Date sjsj){
		this.setInternal("SJSJ",sjsj);
	}
	public Date getSjsj(){
		return (Date)this.getInternal("SJSJ");
	}
	public void setCzwt(String czwt){
		this.setInternal("CZWT",czwt);
	}
	public String getCzwt(){
		return (String)this.getInternal("CZWT");
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
	public void setJhbzid(String jhbzid){
		this.setInternal("JHBZID",jhbzid);
	}
	public String getJhbzid(){
		return (String)this.getInternal("JHBZID");
	}
}