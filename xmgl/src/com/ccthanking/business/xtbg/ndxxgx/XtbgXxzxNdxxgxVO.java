package com.ccthanking.business.xtbg.ndxxgx;
import com.ccthanking.framework.base.BaseVO;
import java.util.Date;

public class XtbgXxzxNdxxgxVO extends BaseVO{

	public XtbgXxzxNdxxgxVO(){
		this.addField("XTBG_XXZX_NDXXGX_ID",OP_STRING|this.TP_PK);
		this.addField("NDXXBT",OP_STRING);
		this.addField("NR",OP_CLOB);
		this.addField("FBR",OP_STRING);
		this.addField("FBSJ",OP_DATE);
		this.addField("ISFB",OP_STRING);
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
		this.setFieldDateFormat("FBSJ","yyyy-MM-dd");
		this.setFieldDateFormat("LRSJ","yyyy-MM-dd");
		this.setFieldDateFormat("GXSJ","yyyy-MM-dd");
		
		this.bindFieldToTranslater("FBR", "FS_ORG_PERSON", "ACCOUNT", "NAME");
		
		this.setVOTableName("XTBG_XXZX_NDXXGX");
	}

	public void setXtbg_xxzx_ndxxgx_id(String xtbg_xxzx_ndxxgx_id){
		this.setInternal("XTBG_XXZX_NDXXGX_ID",xtbg_xxzx_ndxxgx_id);
	}
	public String getXtbg_xxzx_ndxxgx_id(){
		return (String)this.getInternal("XTBG_XXZX_NDXXGX_ID");
	}
	public void setNdxxbt(String ndxxbt){
		this.setInternal("NDXXBT",ndxxbt);
	}
	public String getNdxxbt(){
		return (String)this.getInternal("NDXXBT");
	}
	public void setNr(byte[] nr){
		this.setInternal("NR",nr);
	}
	public byte[] getNr(){
		return (byte[])this.getInternal("NR");
	}
	public void setFbr(String fbr){
		this.setInternal("FBR",fbr);
	}
	public String getFbr(){
		return (String)this.getInternal("FBR");
	}
	public void setFbsj(Date fbsj){
		this.setInternal("FBSJ",fbsj);
	}
	public Date getFbsj(){
		return (Date)this.getInternal("FBSJ");
	}
	public void setIsfb(String isfb){
		this.setInternal("ISFB",isfb);
	}
	public String getIsfb(){
		return (String)this.getInternal("ISFB");
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