package com.ccthanking.business.wttb.vo;
import com.ccthanking.framework.base.BaseVO;
import java.util.Date;

public class WttbPfqkVO extends BaseVO{

	public WttbPfqkVO(){
		this.addField("WTTB_PFQK_ID",OP_STRING|this.TP_PK);
		this.addField("WTID",OP_STRING);
		this.addField("PFR",OP_STRING);
		this.addField("PFSJ",OP_DATE);
		this.addField("PFDW",OP_STRING);
		this.addField("PFNR",OP_STRING);
		this.addField("LRR",OP_STRING);
		this.addField("LRSJ",OP_DATE);
		this.addField("LRBM",OP_STRING);
		this.addField("LRBMMC",OP_STRING);
		this.addField("GXR",OP_STRING);
		this.addField("GXSJ",OP_DATE);
		this.addField("GXBM",OP_STRING);
		this.addField("GXBMMC",OP_STRING);
		this.addField("YWLX",OP_STRING);
		this.addField("SJBH",OP_STRING);
		this.addField("SJMJ",OP_STRING);
		this.addField("SFYX",OP_STRING);
		this.addField("NRLX",OP_STRING);
		this.addField("JYJJFA",OP_STRING);
		this.addField("LZID",OP_STRING);
		this.addField("XXLY",OP_STRING);
		this.setFieldDateFormat("PFSJ","yyyy-MM-dd");
		this.setFieldDateFormat("LRSJ","yyyy-MM-dd");
		this.setFieldDateFormat("GXSJ","yyyy-MM-dd");
		this.setVOTableName("WTTB_PFQK");
	}

	public void setWttb_pfqk_id(String wttb_pfqk_id){
		this.setInternal("WTTB_PFQK_ID",wttb_pfqk_id);
	}
	public String getWttb_pfqk_id(){
		return (String)this.getInternal("WTTB_PFQK_ID");
	}
	public void setWtid(String wtid){
		this.setInternal("WTID",wtid);
	}
	public String getWtid(){
		return (String)this.getInternal("WTID");
	}
	public void setPfr(String pfr){
		this.setInternal("PFR",pfr);
	}
	public String getPfr(){
		return (String)this.getInternal("PFR");
	}
	public void setPfsj(Date pfsj){
		this.setInternal("PFSJ",pfsj);
	}
	public Date getPfsj(){
		return (Date)this.getInternal("PFSJ");
	}
	public void setPfdw(String pfdw){
		this.setInternal("PFDW",pfdw);
	}
	public String getPfdw(){
		return (String)this.getInternal("PFDW");
	}
	public void setPfnr(String pfnr){
		this.setInternal("PFNR",pfnr);
	}
	public String getPfnr(){
		return (String)this.getInternal("PFNR");
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
	public void setNrlx(String nrlx){
		this.setInternal("NRLX",nrlx);
	}
	public String getNrlx(){
		return (String)this.getInternal("NRLX");
	}
	public void setJyjjfa(String jyjjfa){
		this.setInternal("JYJJFA",jyjjfa);
	}
	public String getJyjjfa(){
		return (String)this.getInternal("JYJJFA");
	}
	public void setLzid(String lzid){
		this.setInternal("LZID",lzid);
	}
	public String getLzid(){
		return (String)this.getInternal("LZID");
	}
	public void setXxly(String xxly){
		this.setInternal("XXLY",xxly);
	}
	public String getXxly(){
		return (String)this.getInternal("XXLY");
	}
}