package com.ccthanking.business.jjg.vo;
import com.ccthanking.framework.base.BaseVO;
import java.util.Date;

public class JjgwhVO extends BaseVO{

	public JjgwhVO(){
		this.addField("GC_SJGL_JJG_ID",OP_STRING|this.TP_PK);
		this.addField("JHID",OP_STRING);
		this.addField("JHSJID",OP_STRING);
		this.addField("ND",OP_STRING);
		this.addField("XMID",OP_STRING);
		this.addField("BDID",OP_STRING);
		this.addField("JIAOGJSDW",OP_STRING);
		this.addField("JIAOGJSR",OP_STRING);
		this.addField("JGYSSJ",OP_DATE);
		this.addField("JGYSRQ",OP_DATE);
		this.addField("WJGYSY",OP_STRING);
		this.addField("WJGYS",OP_STRING);
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
		this.addField("JUNGJSDW",OP_STRING);
		this.addField("JUNGJSR",OP_STRING);
		this.addField("JA_KSRQ",OP_DATE);
		this.addField("JU_KSRQ",OP_DATE);
		this.addField("JU_SFBZJZ",OP_STRING);
		this.addField("JU_JBYSTJ",OP_STRING);
		this.addField("JU_SFZZYS",OP_STRING);
		this.addField("JU_YSQK",OP_STRING);
		this.addField("JU_SFZLCD",OP_STRING);
		this.addField("JU_SFZJZBA",OP_STRING);
		this.setFieldDateFormat("JGYSSJ","yyyy-MM-dd");
		this.setFieldDateFormat("JGYSRQ","yyyy-MM-dd");
		this.setFieldDateFormat("LRSJ","yyyy-MM-dd");
		this.setFieldDateFormat("GXSJ","yyyy-MM-dd");
		this.setFieldDateFormat("JA_KSRQ","yyyy-MM-dd");
		this.setFieldDateFormat("JU_KSRQ","yyyy-MM-dd");
		this.setVOTableName("GC_SJGL_JJG");
	}
	public void setGc_sjgl_jjg_id(String gc_sjgl_jjg_id){
		this.setInternal("GC_SJGL_JJG_ID",gc_sjgl_jjg_id);
	}
	public String getGc_sjgl_jjg_id(){
		return (String)this.getInternal("GC_SJGL_JJG_ID");
	}
	public void setJhid(String jhid){
		this.setInternal("JHID",jhid);
	}
	public String getJhid(){
		return (String)this.getInternal("JHID");
	}
	public void setJhsjid(String jhsjid){
		this.setInternal("JHSJID",jhsjid);
	}
	public String getJhsjid(){
		return (String)this.getInternal("JHSJID");
	}
	public void setNd(String nd){
		this.setInternal("ND",nd);
	}
	public String getNd(){
		return (String)this.getInternal("ND");
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
	public void setJiaogjsdw(String jiaogjsdw){
		this.setInternal("JIAOGJSDW",jiaogjsdw);
	}
	public String getJiaogjsdw(){
		return (String)this.getInternal("JIAOGJSDW");
	}
	public void setJiaogjsr(String jiaogjsr){
		this.setInternal("JIAOGJSR",jiaogjsr);
	}
	public String getJiaogjsr(){
		return (String)this.getInternal("JIAOGJSR");
	}
	public void setJgyssj(Date jgyssj){
		this.setInternal("JGYSSJ",jgyssj);
	}
	public Date getJgyssj(){
		return (Date)this.getInternal("JGYSSJ");
	}
	public void setJgysrq(Date jgysrq){
		this.setInternal("JGYSRQ",jgysrq);
	}
	public Date getJgysrq(){
		return (Date)this.getInternal("JGYSRQ");
	}
	public void setWjgysy(String wjgysy){
		this.setInternal("WJGYSY",wjgysy);
	}
	public String getWjgysy(){
		return (String)this.getInternal("WJGYSY");
	}
	public void setWjgys(String wjgys){
		this.setInternal("WJGYS",wjgys);
	}
	public String getWjgys(){
		return (String)this.getInternal("WJGYS");
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
	public void setJungjsdw(String jungjsdw){
		this.setInternal("JUNGJSDW",jungjsdw);
	}
	public String getJungjsdw(){
		return (String)this.getInternal("JUNGJSDW");
	}
	public void setJungjsr(String jungjsr){
		this.setInternal("JUNGJSR",jungjsr);
	}
	public String getJungjsr(){
		return (String)this.getInternal("JUNGJSR");
	}
	public void setJa_ksrq(Date ja_ksrq){
		this.setInternal("JA_KSRQ",ja_ksrq);
	}
	public Date getJa_ksrq(){
		return (Date)this.getInternal("JA_KSRQ");
	}
	public void setJu_ksrq(Date ju_ksrq){
		this.setInternal("JU_KSRQ",ju_ksrq);
	}
	public Date getJu_ksrq(){
		return (Date)this.getInternal("JU_KSRQ");
	}
	public void setJu_sfbzjz(String ju_sfbzjz){
		this.setInternal("JU_SFBZJZ",ju_sfbzjz);
	}
	public String getJu_sfbzjz(){
		return (String)this.getInternal("JU_SFBZJZ");
	}
	public void setJu_jbystj(String ju_jbystj){
		this.setInternal("JU_JBYSTJ",ju_jbystj);
	}
	public String getJu_jbystj(){
		return (String)this.getInternal("JU_JBYSTJ");
	}
	public void setJu_sfzzys(String ju_sfzzys){
		this.setInternal("JU_SFZZYS",ju_sfzzys);
	}
	public String getJu_sfzzys(){
		return (String)this.getInternal("JU_SFZZYS");
	}
	public void setJu_ysqk(String ju_ysqk){
		this.setInternal("JU_YSQK",ju_ysqk);
	}
	public String getJu_ysqk(){
		return (String)this.getInternal("JU_YSQK");
	}
	public void setJu_sfzlcd(String ju_sfzlcd){
		this.setInternal("JU_SFZLCD",ju_sfzlcd);
	}
	public String getJu_sfzlcd(){
		return (String)this.getInternal("JU_SFZLCD");
	}
	public void setJu_sfzjzba(String ju_sfzjzba){
		this.setInternal("JU_SFZJZBA",ju_sfzjzba);
	}
	public String getJu_sfzjzba(){
		return (String)this.getInternal("JU_SFZJZBA");
	}
}