package com.ccthanking.business.gcgl.gcjl.vo;
import com.ccthanking.framework.base.BaseVO;
import java.util.Date;

public class HtsjVO extends BaseVO{

	public HtsjVO(){
		this.addField("ID",OP_STRING|this.TP_PK);
		this.addField("JHSJID",OP_STRING);
		this.addField("XMID",OP_STRING);
		this.addField("BDID",OP_STRING);
		this.addField("HTID",OP_STRING);
		this.addField("HTQDJ",OP_STRING);
		this.addField("GCJGBFB",OP_STRING);
		this.addField("HTJSJ",OP_STRING);
		this.addField("MBLX",OP_STRING);
		this.addField("MBID",OP_STRING);
		this.addField("JE",OP_STRING);
		this.addField("MBSJLX",OP_STRING);
		this.addField("RQ",OP_DATE);
		this.addField("MC",OP_STRING);
		this.addField("BGTS",OP_STRING);
		this.addField("HTDID",OP_STRING);
		this.addField("YWLX",OP_STRING);
		this.addField("SJBH",OP_STRING);
		this.addField("SJMJ",OP_STRING);
		this.addField("SFYX",OP_STRING);
		this.addField("BZ",OP_STRING);
		this.addField("LRR",OP_STRING);
		this.addField("LRSJ",OP_DATE);
		this.addField("LRBM",OP_STRING);
		this.addField("LRBMMC",OP_STRING);
		this.addField("GXR",OP_STRING);
		this.addField("GXSJ",OP_DATE);
		this.addField("GXBM",OP_STRING);
		this.addField("GXBMMC",OP_STRING);
		this.addField("SORTNO",OP_STRING);
		
		//表选 
		this.bindFieldToTranslater("XMID", "GC_TCJH_XMXDK", "GC_TCJH_XMXDK_ID", "XMMC");
		this.bindFieldToTranslater("BDID", "GC_XMBD", "GC_XMBD_ID", "BDMC");
		this.setFieldDateFormat("RQ","yyyy-MM-dd");
		this.setFieldDateFormat("LRSJ","yyyy-MM-dd");
		this.setFieldDateFormat("GXSJ","yyyy-MM-dd");
		this.setVOTableName("GC_HTGL_HTSJ");
	}

	public void setId(String id){
		this.setInternal("ID",id);
	}
	public String getId(){
		return (String)this.getInternal("ID");
	}
	public void setJhsjid(String jhsjid){
		this.setInternal("JHSJID",jhsjid);
	}
	public String getJhsjid(){
		return (String)this.getInternal("JHSJID");
	}
	public void setXmid(String xmid){
		this.setInternal("XMID",xmid);
	}
	public String getXmid(){
		return (String)this.getInternal("XMID");
	}
	public void setBdid(String bdid){
		this.setInternal("BDID",bdid);
	}
	public String getBdid(){
		return (String)this.getInternal("BDID");
	}
	public void setHtid(String htid){
		this.setInternal("HTID",htid);
	}
	public String getHtid(){
		return (String)this.getInternal("HTID");
	}
	public void setHtqdj(String htqdj){
		this.setInternal("HTQDJ",htqdj);
	}
	public String getHtqdj(){
		return (String)this.getInternal("HTQDJ");
	}
	public void setGcjgbfb(String gcjgbfb){
		this.setInternal("GCJGBFB",gcjgbfb);
	}
	public String getGcjgbfb(){
		return (String)this.getInternal("GCJGBFB");
	}
	public void setHtjsj(String htjsj){
		this.setInternal("HTJSJ",htjsj);
	}
	public String getHtjsj(){
		return (String)this.getInternal("HTJSJ");
	}
	public void setMblx(String mblx){
		this.setInternal("MBLX",mblx);
	}
	public String getMblx(){
		return (String)this.getInternal("MBLX");
	}
	public void setMbid(String mbid){
		this.setInternal("MBID",mbid);
	}
	public String getMbid(){
		return (String)this.getInternal("MBID");
	}
	public void setJe(String je){
		this.setInternal("JE",je);
	}
	public String getJe(){
		return (String)this.getInternal("JE");
	}
	public void setMbsjlx(String mbsjlx){
		this.setInternal("MBSJLX",mbsjlx);
	}
	public String getMbsjlx(){
		return (String)this.getInternal("MBSJLX");
	}
	public void setRq(Date rq){
		this.setInternal("RQ",rq);
	}
	public Date getRq(){
		return (Date)this.getInternal("RQ");
	}
	public void setMc(String mc){
		this.setInternal("MC",mc);
	}
	public String getMc(){
		return (String)this.getInternal("MC");
	}
	public void setBgts(String bgts){
		this.setInternal("BGTS",bgts);
	}
	public String getBgts(){
		return (String)this.getInternal("BGTS");
	}
	public void setHtdid(String htdid){
		this.setInternal("HTDID",htdid);
	}
	public String getHtdid(){
		return (String)this.getInternal("HTDID");
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
	public void setSortno(String sortno){
		this.setInternal("SORTNO",sortno);
	}
	public String getSortno(){
		return (String)this.getInternal("SORTNO");
	}
}