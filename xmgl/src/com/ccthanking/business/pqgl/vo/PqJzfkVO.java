package com.ccthanking.business.pqgl.vo;
import com.ccthanking.framework.base.BaseVO;
import java.util.Date;

public class PqJzfkVO extends BaseVO{

	public PqJzfkVO(){
		this.addField("GC_PQ_JZFK_ID",OP_STRING|this.TP_PK);
		this.addField("JHID",OP_STRING);
		this.addField("JHSJID",OP_STRING);
		this.addField("XMID",OP_STRING);
		this.addField("BDID",OP_STRING);
		this.addField("ZXMID",OP_STRING);
		this.addField("FKRQ",OP_DATE);
		this.addField("JZQK",OP_STRING);
		this.addField("SYGZL",OP_STRING);
		this.addField("CZWT",OP_STRING);
		this.addField("JJFA",OP_STRING);
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
		this.setFieldDateFormat("FKRQ","yyyy-MM-dd");
		this.setFieldDateFormat("LRSJ","yyyy-MM-dd");
		this.setFieldDateFormat("GXSJ","yyyy-MM-dd");
		this.setVOTableName("GC_PQ_JZFK");
	}

	public void setGc_pq_jzfk_id(String gc_pq_jzfk_id){
		this.setInternal("GC_PQ_JZFK_ID",gc_pq_jzfk_id);
	}
	public String getGc_pq_jzfk_id(){
		return (String)this.getInternal("GC_PQ_JZFK_ID");
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
	public void setZxmid(String zxmid){
		this.setInternal("ZXMID",zxmid);
	}
	public String getZxmid(){
		return (String)this.getInternal("ZXMID");
	}
	public void setFkrq(Date fkrq){
		this.setInternal("FKRQ",fkrq);
	}
	public Date getFkrq(){
		return (Date)this.getInternal("FKRQ");
	}
	public void setJzqk(String jzqk){
		this.setInternal("JZQK",jzqk);
	}
	public String getJzqk(){
		return (String)this.getInternal("JZQK");
	}
	public void setSygzl(String sygzl){
		this.setInternal("SYGZL",sygzl);
	}
	public String getSygzl(){
		return (String)this.getInternal("SYGZL");
	}
	public void setCzwt(String czwt){
		this.setInternal("CZWT",czwt);
	}
	public String getCzwt(){
		return (String)this.getInternal("CZWT");
	}
	public void setJjfa(String jjfa){
		this.setInternal("JJFA",jjfa);
	}
	public String getJjfa(){
		return (String)this.getInternal("JJFA");
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