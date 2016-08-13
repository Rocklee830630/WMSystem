package com.ccthanking.business.zjb.jsgl;
import com.ccthanking.framework.base.BaseVO;
import java.util.Date;

public class GcZjbJsbVO extends BaseVO{

	public GcZjbJsbVO(){
		this.addField("GC_ZJB_JSB_ID",OP_STRING|this.TP_PK);
		this.addField("XDKID",OP_STRING);
		this.addField("BDID",OP_STRING);
		this.addField("JSZT",OP_STRING);
		this.addField("TBR",OP_STRING);
		this.addField("TBRDH",OP_STRING);
		this.addField("TBRQ",OP_DATE);
		this.addField("TBJE",OP_STRING);
		this.addField("SFWT",OP_STRING);
		this.addField("WTZXGS",OP_STRING);
		this.addField("YZSDRQ",OP_DATE);
		this.addField("YZSDJE",OP_STRING);
		this.addField("CSSDRQ",OP_DATE);
		this.addField("CSSDJE",OP_STRING);
		this.addField("CSBGBH",OP_STRING);
		this.addField("SJSDRQ",OP_DATE);
		this.addField("SJSDJE",OP_STRING);
		this.addField("SJBGBH",OP_STRING);
		this.addField("JSQK",OP_STRING);
		this.addField("JSBZ",OP_STRING);
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
		this.addField("GCQK",OP_STRING);
		this.addField("HTID",OP_STRING);
		this.addField("JHSJID",OP_STRING);
		this.addField("ZT",OP_STRING);
		this.addField("SJWYBH",OP_STRING);
		this.addField("TBCSRQ",OP_DATE);
		this.addField("SJND",OP_STRING);
		this.addField("SJZ",OP_STRING);
		this.addField("SJZXGS",OP_STRING);
		this.setFieldDateFormat("TBRQ","yyyy-MM-dd");
		this.setFieldDateFormat("YZSDRQ","yyyy-MM-dd");
		this.setFieldDateFormat("CSSDRQ","yyyy-MM-dd");
		this.setFieldDateFormat("SJSDRQ","yyyy-MM-dd");
		this.setFieldDateFormat("LRSJ","yyyy-MM-dd");
		this.setFieldDateFormat("GXSJ","yyyy-MM-dd");
		this.setFieldDateFormat("TBCSRQ","yyyy-MM-dd");
		
	    this.bindFieldToDic("JSZT", "JSZT");
        this.bindFieldToDic("SFWT", "SF");
        this.bindFieldToThousand("TBJE"); 
        this.bindFieldToThousand("YZSDJE"); 
        this.bindFieldToThousand("CSSDJE"); 
        this.bindFieldToThousand("SJSDJE"); 
	    
		this.setVOTableName("GC_ZJB_JSB");
	}

	public void setGc_zjb_jsb_id(String gc_zjb_jsb_id){
		this.setInternal("GC_ZJB_JSB_ID",gc_zjb_jsb_id);
	}
	public String getGc_zjb_jsb_id(){
		return (String)this.getInternal("GC_ZJB_JSB_ID");
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
	public void setJszt(String jszt){
		this.setInternal("JSZT",jszt);
	}
	public String getJszt(){
		return (String)this.getInternal("JSZT");
	}
	public void setTbr(String tbr){
		this.setInternal("TBR",tbr);
	}
	public String getTbr(){
		return (String)this.getInternal("TBR");
	}
	public void setTbrdh(String tbrdh){
		this.setInternal("TBRDH",tbrdh);
	}
	public String getTbrdh(){
		return (String)this.getInternal("TBRDH");
	}
	public void setTbrq(Date tbrq){
		this.setInternal("TBRQ",tbrq);
	}
	public Date getTbrq(){
		return (Date)this.getInternal("TBRQ");
	}
	public void setTbje(String tbje){
		this.setInternal("TBJE",tbje);
	}
	public String getTbje(){
		return (String)this.getInternal("TBJE");
	}
	public void setSfwt(String sfwt){
		this.setInternal("SFWT",sfwt);
	}
	public String getSfwt(){
		return (String)this.getInternal("SFWT");
	}
	public void setWtzxgs(String wtzxgs){
		this.setInternal("WTZXGS",wtzxgs);
	}
	public String getWtzxgs(){
		return (String)this.getInternal("WTZXGS");
	}
	public void setYzsdrq(Date yzsdrq){
		this.setInternal("YZSDRQ",yzsdrq);
	}
	public Date getYzsdrq(){
		return (Date)this.getInternal("YZSDRQ");
	}
	public void setYzsdje(String yzsdje){
		this.setInternal("YZSDJE",yzsdje);
	}
	public String getYzsdje(){
		return (String)this.getInternal("YZSDJE");
	}
	public void setCssdrq(Date cssdrq){
		this.setInternal("CSSDRQ",cssdrq);
	}
	public Date getCssdrq(){
		return (Date)this.getInternal("CSSDRQ");
	}
	public void setCssdje(String cssdje){
		this.setInternal("CSSDJE",cssdje);
	}
	public String getCssdje(){
		return (String)this.getInternal("CSSDJE");
	}
	public void setCsbgbh(String csbgbh){
		this.setInternal("CSBGBH",csbgbh);
	}
	public String getCsbgbh(){
		return (String)this.getInternal("CSBGBH");
	}
	public void setSjsdrq(Date sjsdrq){
		this.setInternal("SJSDRQ",sjsdrq);
	}
	public Date getSjsdrq(){
		return (Date)this.getInternal("SJSDRQ");
	}
	public void setSjsdje(String sjsdje){
		this.setInternal("SJSDJE",sjsdje);
	}
	public String getSjsdje(){
		return (String)this.getInternal("SJSDJE");
	}
	public void setSjbgbh(String sjbgbh){
		this.setInternal("SJBGBH",sjbgbh);
	}
	public String getSjbgbh(){
		return (String)this.getInternal("SJBGBH");
	}
	public void setJsqk(String jsqk){
		this.setInternal("JSQK",jsqk);
	}
	public String getJsqk(){
		return (String)this.getInternal("JSQK");
	}
	public void setJsbz(String jsbz){
		this.setInternal("JSBZ",jsbz);
	}
	public String getJsbz(){
		return (String)this.getInternal("JSBZ");
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
	public void setGcqk(String gcqk){
		this.setInternal("GCQK",gcqk);
	}
	public String getGcqk(){
		return (String)this.getInternal("GCQK");
	}
	public void setHtid(String htid){
		this.setInternal("HTID",htid);
	}
	public String getHtid(){
		return (String)this.getInternal("HTID");
	}
	public void setJhsjid(String jhsjid){
		this.setInternal("JHSJID",jhsjid);
	}
	public String getJhsjid(){
		return (String)this.getInternal("JHSJID");
	}
	public void setZt(String zt){
		this.setInternal("ZT",zt);
	}
	public String getZt(){
		return (String)this.getInternal("ZT");
	}
	public void setSjwybh(String sjwybh){
		this.setInternal("SJWYBH",sjwybh);
	}
	public String getSjwybh(){
		return (String)this.getInternal("SJWYBH");
	}
	public void setTbcsrq(Date tbcsrq){
		this.setInternal("TBCSRQ",tbcsrq);
	}
	public Date getTbcsrq(){
		return (Date)this.getInternal("TBCSRQ");
	}
	public void setSjnd(String sjnd){
		this.setInternal("SJND",sjnd);
	}
	public String getSjnd(){
		return (String)this.getInternal("SJND");
	}
	public void setSjz(String sjz){
		this.setInternal("SJZ",sjz);
	}
	public String getSjz(){
		return (String)this.getInternal("SJZ");
	}
	public void setSjzxgs(String sjzxgs){
		this.setInternal("SJZXGS",sjzxgs);
	}
	public String getSjzxgs(){
		return (String)this.getInternal("SJZXGS");
	}
}