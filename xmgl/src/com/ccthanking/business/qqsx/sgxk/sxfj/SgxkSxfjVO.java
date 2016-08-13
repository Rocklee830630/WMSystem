package com.ccthanking.business.qqsx.sgxk.sxfj;
import com.ccthanking.framework.base.BaseVO;
import java.util.Date;

public class SgxkSxfjVO extends BaseVO{

	public SgxkSxfjVO(){
		this.addField("GC_QQSX_SXFJ_ID",OP_STRING|this.TP_PK);
		this.addField("FJID",OP_STRING);
		this.addField("FJLX",OP_STRING);
		this.addField("DFL",OP_STRING);
		this.addField("YWBID",OP_STRING);
		this.addField("WHMC",OP_STRING);
		this.addField("BLR",OP_STRING);
		this.addField("TZBH",OP_STRING);
		this.addField("BLSJ",OP_DATE);
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
		this.addField("XMID",OP_STRING);
		this.addField("CZWT",OP_STRING);
		this.addField("JHSJID",OP_STRING);
		this.addField("SJWYBH",OP_STRING);
		this.bindFieldToDic("FJLX", "LXKYFJLX");
		this.bindFieldToTranslater("BLR", "FS_ORG_PERSON", "ACCOUNT", "NAME");
		this.setFieldDateFormat("BLSJ","yyyy-MM-dd");
		this.setFieldDateFormat("LRSJ","yyyy-MM-dd");
		this.setFieldDateFormat("GXSJ","yyyy-MM-dd");
		this.setVOTableName("GC_QQSX_SXFJ");
	}

	public void setGc_qqsx_sxfj_id(String gc_qqsx_sxfj_id){
		this.setInternal("GC_QQSX_SXFJ_ID",gc_qqsx_sxfj_id);
	}
	public String getGc_qqsx_sxfj_id(){
		return (String)this.getInternal("GC_QQSX_SXFJ_ID");
	}
	public void setFjid(String fjid){
		this.setInternal("FJID",fjid);
	}
	public String getFjid(){
		return (String)this.getInternal("FJID");
	}
	public void setFjlx(String fjlx){
		this.setInternal("FJLX",fjlx);
	}
	public String getFjlx(){
		return (String)this.getInternal("FJLX");
	}
	public void setDfl(String dfl){
		this.setInternal("DFL",dfl);
	}
	public String getDfl(){
		return (String)this.getInternal("DFL");
	}
	public void setYwbid(String ywbid){
		this.setInternal("YWBID",ywbid);
	}
	public String getYwbid(){
		return (String)this.getInternal("YWBID");
	}
	public void setWhmc(String whmc){
		this.setInternal("WHMC",whmc);
	}
	public String getWhmc(){
		return (String)this.getInternal("WHMC");
	}
	public void setBlr(String blr){
		this.setInternal("BLR",blr);
	}
	public String getBlr(){
		return (String)this.getInternal("BLR");
	}
	public void setTzbh(String tzbh){
		this.setInternal("TZBH",tzbh);
	}
	public String getTzbh(){
		return (String)this.getInternal("TZBH");
	}
	public void setBlsj(Date blsj){
		this.setInternal("BLSJ",blsj);
	}
	public Date getBlsj(){
		return (Date)this.getInternal("BLSJ");
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
	public void setXmid(String xmid){
		this.setInternal("XMID",xmid);
	}
	public String getXmid(){
		return (String)this.getInternal("XMID");
	}
	public void setCzwt(String czwt){
		this.setInternal("CZWT",czwt);
	}
	public String getCzwt(){
		return (String)this.getInternal("CZWT");
	}
	public void setJhsjid(String jhsjid){
		this.setInternal("JHSJID",jhsjid);
	}
	public String getJhsjid(){
		return (String)this.getInternal("JHSJID");
	}
	public void setSjwybh(String sjwybh){
		this.setInternal("SJWYBH",sjwybh);
	}
	public String getSjwybh(){
		return (String)this.getInternal("SJWYBH");
	}
}