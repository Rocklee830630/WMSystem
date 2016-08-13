package com.ccthanking.business.xtbg.gwgl.vo;

import java.util.Date;

import com.ccthanking.framework.base.BaseVO;

/**
 * @auther xhb 
 */
public class FwglVo extends BaseVO {
	public FwglVo(){
		this.addField("FWID",OP_STRING|this.TP_PK);
		this.addField("WJBH",OP_STRING);
		this.addField("WJBT",OP_STRING);
		this.addField("LSH",OP_STRING);
		this.addField("DQZT",OP_STRING);
		this.addField("NGDW",OP_STRING);
		this.addField("NGRQ",OP_DATE);
		this.addField("NGR",OP_STRING);
		this.addField("FWSJ",OP_DATE);
		this.addField("WZ",OP_STRING);
		this.addField("HJ",OP_STRING);
		this.addField("SSXM",OP_STRING);
		this.addField("ZS",OP_STRING);
		this.addField("CS",OP_STRING);
		this.addField("MJ",OP_STRING);
		this.addField("DYFS",OP_STRING);
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

		this.addField("LHFW",OP_STRING);
		this.setFieldDateFormat("NGRQ","yyyy-MM-dd");
		this.setFieldDateFormat("FWSJ","yyyy-MM-dd");
		this.setFieldDateFormat("LRSJ","yyyy-MM-dd");
		this.setFieldDateFormat("GXSJ","yyyy-MM-dd");
		this.bindFieldToTranslater("ZS", "FS_COMMON_DICT", "DICT_ID", "DICT_NAME");
		this.bindFieldToTranslater("CS", "FS_COMMON_DICT", "DICT_ID", "DICT_NAME");
		
		this.setVOTableName("XTBG_GWGL_FWGL");
	}

	public void setFwid(String fwid){
		this.setInternal("FWID",fwid);
	}
	public String getFwid(){
		return (String)this.getInternal("FWID");
	}
	public void setWjbh(String wjbh){
		this.setInternal("WJBH",wjbh);
	}
	public String getWjbh(){
		return (String)this.getInternal("WJBH");
	}
	public void setWjbt(String wjbt){
		this.setInternal("WJBT",wjbt);
	}
	public String getWjbt(){
		return (String)this.getInternal("WJBT");
	}
	public void setLsh(String lsh){
		this.setInternal("LSH",lsh);
	}
	public String getLsh(){
		return (String)this.getInternal("LSH");
	}
	public void setDqzt(String dqzt){
		this.setInternal("DQZT",dqzt);
	}
	public String getDqzt(){
		return (String)this.getInternal("DQZT");
	}
	public void setNgdw(String ngdw){
		this.setInternal("NGDW",ngdw);
	}
	public String getNgdw(){
		return (String)this.getInternal("NGDW");
	}
	public void setNgrq(Date ngrq){
		this.setInternal("NGRQ",ngrq);
	}
	public Date getNgrq(){
		return (Date)this.getInternal("NGRQ");
	}
	public void setNgr(String ngr){
		this.setInternal("NGR",ngr);
	}
	public String getNgr(){
		return (String)this.getInternal("NGR");
	}
	public void setFwsj(Date fwsj){
		this.setInternal("FWSJ",fwsj);
	}
	public Date getFwsj(){
		return (Date)this.getInternal("FWSJ");
	}
	public void setWz(String wz){
		this.setInternal("WZ",wz);
	}
	public String getWz(){
		return (String)this.getInternal("WZ");
	}
	public void setHj(String hj){
		this.setInternal("HJ",hj);
	}
	public String getHj(){
		return (String)this.getInternal("HJ");
	}
	public void setSsxm(String ssxm){
		this.setInternal("SSXM",ssxm);
	}
	public String getSsxm(){
		return (String)this.getInternal("SSXM");
	}
	public void setZs(String zs){
		this.setInternal("ZS",zs);
	}
	public String getZs(){
		return (String)this.getInternal("ZS");
	}
	public void setCs(String cs){
		this.setInternal("CS",cs);
	}
	public String getCs(){
		return (String)this.getInternal("CS");
	}
	public void setMj(String mj){
		this.setInternal("MJ",mj);
	}
	public String getMj(){
		return (String)this.getInternal("MJ");
	}
	public void setDyfs(String dyfs){
		this.setInternal("DYFS",dyfs);
	}
	public String getDyfs(){
		return (String)this.getInternal("DYFS");
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
	

	public void setLhfw(String lhfw) {
		this.setInternal("LHFW",lhfw);
	}
	public String getLhfw() {
		return (String)this.getInternal("LHFW");
	}
}
