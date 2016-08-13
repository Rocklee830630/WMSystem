package com.ccthanking.business.gcgl.kfgl.vo;
import com.ccthanking.framework.base.BaseVO;
import java.util.Date;

public class KfgVO extends BaseVO{

	public KfgVO(){
		this.addField("GC_GCB_KFG_ID",OP_STRING|this.TP_PK);
		this.addField("XDKID",OP_STRING);
		this.addField("BDID",OP_STRING);
		this.addField("KGFG",OP_STRING);
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
		this.addField("TBR",OP_STRING);
		this.addField("TBDW",OP_STRING);
		this.addField("TBSJ",OP_DATE);
		this.addField("JSDW",OP_STRING);
		this.addField("GCSL",OP_STRING);
		this.addField("JGNR",OP_STRING);
		this.addField("FZR",OP_STRING);
		this.addField("ZT",OP_STRING);
		this.addField("TCJHSJID",OP_STRING);
		this.addField("HTID",OP_STRING);
		this.addField("TBRXM",OP_STRING);
		this.addField("TBDWMC",OP_STRING);
		this.addField("FZRXM",OP_STRING);
		this.addField("BLSJ",OP_DATE);
		this.addField("JHKGSJ",OP_DATE);
		this.addField("SJKGSJ",OP_DATE);
		this.addField("JHJGSJ",OP_DATE);
		this.addField("XMGLGS",OP_STRING);

		this.addField("SJTGSJ",OP_DATE);
		//字典
		this.bindFieldToDic("KGFG", "KFTGZT");
		//表选
		this.bindFieldToTranslater("SJDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");
		this.bindFieldToTranslater("JLDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");
		this.bindFieldToTranslater("SGDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");
		this.bindFieldToTranslater("XMGLGS", "FS_ORG_DEPT", "ROW_ID", "BMJC");
		
		this.setFieldDateFormat("LRSJ","yyyy-MM-dd");
		this.setFieldDateFormat("GXSJ","yyyy-MM-dd");
		this.setFieldDateFormat("TBSJ","yyyy-MM-dd");
		this.setFieldDateFormat("BLSJ","yyyy-MM-dd");
		this.setFieldDateFormat("JHKGSJ","yyyy-MM-dd");
		this.setFieldDateFormat("SJKGSJ","yyyy-MM-dd");
		this.setFieldDateFormat("JHJGSJ","yyyy-MM-dd");
		this.setVOTableName("GC_GCB_KFG");
		//add by cbl start绑定sjbh
		this.bindFieldToSjbh("SJBH");
		//add by cbl end
	}

	public void setGc_gcb_kfg_id(String gc_gcb_kfg_id){
		this.setInternal("GC_GCB_KFG_ID",gc_gcb_kfg_id);
	}
	public String getGc_gcb_kfg_id(){
		return (String)this.getInternal("GC_GCB_KFG_ID");
	}
	public void setXdkid(String xdkid){
		this.setInternal("XDKID",xdkid);
	}
	public String getXdkid(){
		return (String)this.getInternal("XDKID");
	}
	public void setBdid(String bdid){
		this.setInternal("BDID",bdid);
	}
	public String getBdid(){
		return (String)this.getInternal("BDID");
	}
	public void setKgfg(String kgfg){
		this.setInternal("KGFG",kgfg);
	}
	public String getKgfg(){
		return (String)this.getInternal("KGFG");
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
	public void setTbr(String tbr){
		this.setInternal("TBR",tbr);
	}
	public String getTbr(){
		return (String)this.getInternal("TBR");
	}
	public void setTbdw(String tbdw){
		this.setInternal("TBDW",tbdw);
	}
	public String getTbdw(){
		return (String)this.getInternal("TBDW");
	}
	public void setTbsj(Date tbsj){
		this.setInternal("TBSJ",tbsj);
	}
	public Date getTbsj(){
		return (Date)this.getInternal("TBSJ");
	}
	public void setJsdw(String jsdw){
		this.setInternal("JSDW",jsdw);
	}
	public String getJsdw(){
		return (String)this.getInternal("JSDW");
	}
	public void setGcsl(String gcsl){
		this.setInternal("GCSL",gcsl);
	}
	public String getGcsl(){
		return (String)this.getInternal("GCSL");
	}
	public void setJgnr(String jgnr){
		this.setInternal("JGNR",jgnr);
	}
	public String getJgnr(){
		return (String)this.getInternal("JGNR");
	}
	public void setFzr(String fzr){
		this.setInternal("FZR",fzr);
	}
	public String getFzr(){
		return (String)this.getInternal("FZR");
	}
	public void setZt(String zt){
		this.setInternal("ZT",zt);
	}
	public String getZt(){
		return (String)this.getInternal("ZT");
	}
	public void setTcjhsjid(String tcjhsjid){
		this.setInternal("TCJHSJID",tcjhsjid);
	}
	public String getTcjhsjid(){
		return (String)this.getInternal("TCJHSJID");
	}
	public void setHtid(String htid){
		this.setInternal("HTID",htid);
	}
	public String getHtid(){
		return (String)this.getInternal("HTID");
	}
	public void setTbrxm(String tbrxm){
		this.setInternal("TBRXM",tbrxm);
	}
	public String getTbrxm(){
		return (String)this.getInternal("TBRXM");
	}
	public void setTbdwmc(String tbdwmc){
		this.setInternal("TBDWMC",tbdwmc);
	}
	public String getTbdwmc(){
		return (String)this.getInternal("TBDWMC");
	}
	public void setFzrxm(String fzrxm){
		this.setInternal("FZRXM",fzrxm);
	}
	public String getFzrxm(){
		return (String)this.getInternal("FZRXM");
	}
	public void setBlsj(Date blsj){
		this.setInternal("BLSJ",blsj);
	}
	public Date getBlsj(){
		return (Date)this.getInternal("BLSJ");
	}
	public void setJhkgsj(Date jhkgsj){
		this.setInternal("JHKGSJ",jhkgsj);
	}
	public Date getJhkgsj(){
		return (Date)this.getInternal("JHKGSJ");
	}
	public void setSjkgsj(Date sjkgsj){
		this.setInternal("SJKGSJ",sjkgsj);
	}
	public Date getSjkgsj(){
		return (Date)this.getInternal("SJKGSJ");
	}
	public void setJhjgsj(Date jhjgsj){
		this.setInternal("JHJGSJ",jhjgsj);
	}
	public Date getJhjgsj(){
		return (Date)this.getInternal("JHJGSJ");
	}
	public void setXmglgs(String xmglgs){
		this.setInternal("XMGLGS",xmglgs);
	}
	public String getXmglgs(){
		return (String)this.getInternal("XMGLGS");
	}

	public void setSjtgsj(Date sjtgsj){
		this.setInternal("SJTGSJ",sjtgsj);
	}
	public Date getSjtgsj(){
		return (Date)this.getInternal("SJTGSJ");
	}
}