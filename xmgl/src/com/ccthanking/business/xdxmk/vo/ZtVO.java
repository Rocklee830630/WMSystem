package com.ccthanking.business.xdxmk.vo;
import com.ccthanking.framework.base.BaseVO;
import java.util.Date;

public class ZtVO extends BaseVO{

	public ZtVO(){
		this.addField("GC_JH_ZT_ID",OP_STRING|this.TP_PK);
		this.addField("JHPCH",OP_STRING);
		this.addField("ND",OP_STRING);
		this.addField("JHMC",OP_STRING);
		this.addField("ISXF",OP_STRING);
		this.addField("XFBB",OP_STRING);
		this.addField("XFRQ",OP_DATE);
		this.addField("SCXFBB",OP_STRING);
		this.addField("SCXFRQ",OP_DATE);
		this.addField("MQBB",OP_STRING);
		this.addField("MBID",OP_STRING);
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
		this.addField("XFLX",OP_STRING);
		this.addField("SPZT",OP_STRING);
		this.addField("XDRQ",OP_DATE);
		this.setFieldDateFormat("XFRQ","yyyy-MM-dd");
		this.setFieldDateFormat("SCXFRQ","yyyy-MM-dd");
		this.setFieldDateFormat("LRSJ","yyyy-MM-dd");
		this.setFieldDateFormat("GXSJ","yyyy-MM-dd");
		this.setFieldDateFormat("XDRQ","yyyy-MM-dd");
		this.setVOTableName("GC_JH_ZT");
	}

	public void setGc_jh_zt_id(String gc_jh_zt_id){
		this.setInternal("GC_JH_ZT_ID",gc_jh_zt_id);
	}
	public String getGc_jh_zt_id(){
		return (String)this.getInternal("GC_JH_ZT_ID");
	}
	public void setJhpch(String jhpch){
		this.setInternal("JHPCH",jhpch);
	}
	public String getJhpch(){
		return (String)this.getInternal("JHPCH");
	}
	public void setNd(String nd){
		this.setInternal("ND",nd);
	}
	public String getNd(){
		return (String)this.getInternal("ND");
	}
	public void setJhmc(String jhmc){
		this.setInternal("JHMC",jhmc);
	}
	public String getJhmc(){
		return (String)this.getInternal("JHMC");
	}
	public void setIsxf(String isxf){
		this.setInternal("ISXF",isxf);
	}
	public String getIsxf(){
		return (String)this.getInternal("ISXF");
	}
	public void setXfbb(String xfbb){
		this.setInternal("XFBB",xfbb);
	}
	public String getXfbb(){
		return (String)this.getInternal("XFBB");
	}
	public void setXfrq(Date xfrq){
		this.setInternal("XFRQ",xfrq);
	}
	public Date getXfrq(){
		return (Date)this.getInternal("XFRQ");
	}
	public void setScxfbb(String scxfbb){
		this.setInternal("SCXFBB",scxfbb);
	}
	public String getScxfbb(){
		return (String)this.getInternal("SCXFBB");
	}
	public void setScxfrq(Date scxfrq){
		this.setInternal("SCXFRQ",scxfrq);
	}
	public Date getScxfrq(){
		return (Date)this.getInternal("SCXFRQ");
	}
	public void setMqbb(String mqbb){
		this.setInternal("MQBB",mqbb);
	}
	public String getMqbb(){
		return (String)this.getInternal("MQBB");
	}
	public void setMbid(String mbid){
		this.setInternal("MBID",mbid);
	}
	public String getMbid(){
		return (String)this.getInternal("MBID");
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
	public void setXflx(String xflx){
		this.setInternal("XFLX",xflx);
	}
	public String getXflx(){
		return (String)this.getInternal("XFLX");
	}
	public void setXdrq(Date xdrq){
		this.setInternal("XDRQ",xdrq);
	}
	public Date getXdrq(){
		return (Date)this.getInternal("XDRQ");
	}
	public void setSpzt(String spzt){
		this.setInternal("SPZT",spzt);
	}
	public String getSpzt(){
		return (String)this.getInternal("SPZT");
	}
}