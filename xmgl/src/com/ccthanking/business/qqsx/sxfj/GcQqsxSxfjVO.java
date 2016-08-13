package com.ccthanking.business.qqsx.sxfj;
import com.ccthanking.framework.base.BaseVO;
import java.util.Date;

public class GcQqsxSxfjVO extends BaseVO{

	public GcQqsxSxfjVO(){
		this.addField("ID",OP_STRING|this.TP_PK);
		this.addField("FJID",OP_STRING);
		this.addField("FJLX",OP_STRING);
		this.addField("YWID",OP_STRING);
		this.addField("WHMC",OP_STRING);
		this.addField("BLR",OP_STRING);
		this.addField("BLSJ",OP_DATE);
		this.addField("YWLX",OP_STRING);
		this.addField("SJBH",OP_STRING);
		this.addField("BZ",OP_STRING);
		this.addField("LRR",OP_STRING);
		this.addField("LRSJ",OP_DATE);
		this.addField("LRBM",OP_STRING);
		this.addField("GXR",OP_STRING);
		this.addField("GXSJ",OP_DATE);
		this.addField("GXBM",OP_STRING);
		this.addField("SJMJ",OP_STRING);
		this.addField("SFYX",OP_STRING);
		this.addField("TZBH",OP_STRING);
		this.bindFieldToDic("FJLX", "LXSX");
		this.setFieldDateFormat("BLSJ","yyyy-MM-dd");
		this.setFieldDateFormat("LRSJ","yyyy-MM-dd");
		this.setFieldDateFormat("GXSJ","yyyy-MM-dd");
		this.setVOTableName("GC_QQSX_SXFJ");
	}

	public void setId(String id){
		this.setInternal("ID",id);
	}
	public String getId(){
		return (String)this.getInternal("ID");
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
	public void setYwid(String ywid){
		this.setInternal("YWID",ywid);
	}
	public String getYwid(){
		return (String)this.getInternal("YWID");
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
	public void setDfl(String dfl){
		this.setInternal("DFL",dfl);
	}
	public String getDfl(){
		return (String)this.getInternal("DFL");
	}
	public void setTzbh(String tzbh){
		this.setInternal("TZBH",tzbh);
	}
	public String getTzbh(){
		return (String)this.getInternal("TZBH");
	}
}