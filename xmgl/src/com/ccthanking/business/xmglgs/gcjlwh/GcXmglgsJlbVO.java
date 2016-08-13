package com.ccthanking.business.xmglgs.gcjlwh;
import com.ccthanking.framework.base.BaseVO;
import java.util.Date;

public class GcXmglgsJlbVO extends BaseVO{

	public GcXmglgsJlbVO(){
		this.addField("ID",OP_STRING|this.TP_PK);
		this.addField("XDKID",OP_STRING);
		this.addField("HTID",OP_STRING);
		this.addField("BDID",OP_STRING);
		this.addField("JLRQ",OP_DATE);
		this.addField("DYJLSDZ",OP_STRING);
		this.addField("DYZBJ",OP_STRING);
		this.addField("LJDYYFK",OP_STRING);
		this.addField("LJJLSDZ",OP_STRING);
		this.addField("LJZBJ",OP_STRING);
		this.addField("JZJLZ",OP_STRING);
		this.addField("LJJLZ",OP_STRING);
		this.addField("WGBFB",OP_STRING);
		this.addField("SFYS",OP_STRING);
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
		this.addField("DYYFK",OP_STRING);
		this.addField("YJLBZ",OP_STRING);
		this.addField("SJID",OP_STRING);
		this.addField("JGZT",OP_STRING);
		this.addField("JGZTT",OP_STRING);
		this.addField("XJXJ",OP_STRING);
		this.setFieldDateFormat("JLRQ","yyyy-MM-dd");
		this.setFieldDateFormat("LRSJ","yyyy-MM-dd");
		this.setFieldDateFormat("GXSJ","yyyy-MM-dd");
		
		this.bindFieldToDic("SFYS", "SFYS");
		this.bindFieldToDic("XJXJ", "XMXZ");
		this.bindFieldToDic("JGZT", "SF");
		this.bindFieldToDic("JGZTT", "SF");
		
	
		this.setVOTableName("GC_XMGLGS_JLB");
	}

	public void setId(String id){
		this.setInternal("ID",id);
	}
	public String getId(){
		return (String)this.getInternal("ID");
	}
	public void setXdkid(String xdkid){
		this.setInternal("XDKID",xdkid);
	}
	public String getXdkid(){
		return (String)this.getInternal("XDKID");
	}
	public void setHtid(String htid){
		this.setInternal("HTID",htid);
	}
	public String getHtid(){
		return (String)this.getInternal("HTID");
	}
	public void setBdid(String bdid){
		this.setInternal("BDID",bdid);
	}
	public String getBdid(){
		return (String)this.getInternal("BDID");
	}
	public void setJlrq(Date jlrq){
		this.setInternal("JLRQ",jlrq);
	}
	public Date getJlrq(){
		return (Date)this.getInternal("JLRQ");
	}
	public void setDyjlsdz(String dyjlsdz){
		this.setInternal("DYJLSDZ",dyjlsdz);
	}
	public String getDyjlsdz(){
		return (String)this.getInternal("DYJLSDZ");
	}
	public void setDyzbj(String dyzbj){
		this.setInternal("DYZBJ",dyzbj);
	}
	public String getDyzbj(){
		return (String)this.getInternal("DYZBJ");
	}
	public void setLjdyyfk(String ljdyyfk){
		this.setInternal("LJDYYFK",ljdyyfk);
	}
	public String getLjdyyfk(){
		return (String)this.getInternal("LJDYYFK");
	}
	public void setLjjlsdz(String ljjlsdz){
		this.setInternal("LJJLSDZ",ljjlsdz);
	}
	public String getLjjlsdz(){
		return (String)this.getInternal("LJJLSDZ");
	}
	public void setLjzbj(String ljzbj){
		this.setInternal("LJZBJ",ljzbj);
	}
	public String getLjzbj(){
		return (String)this.getInternal("LJZBJ");
	}
	public void setJzjlz(String jzjlz){
		this.setInternal("JZJLZ",jzjlz);
	}
	public String getJzjlz(){
		return (String)this.getInternal("JZJLZ");
	}
	public void setLjjlz(String ljjlz){
		this.setInternal("LJJLZ",ljjlz);
	}
	public String getLjjlz(){
		return (String)this.getInternal("LJJLZ");
	}
	public void setWgbfb(String wgbfb){
		this.setInternal("WGBFB",wgbfb);
	}
	public String getWgbfb(){
		return (String)this.getInternal("WGBFB");
	}
	public void setSfys(String sfys){
		this.setInternal("SFYS",sfys);
	}
	public String getSfys(){
		return (String)this.getInternal("SFYS");
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
	public void setDyyfk(String dyyfk){
		this.setInternal("DYYFK",dyyfk);
	}
	public String getDyyfk(){
		return (String)this.getInternal("DYYFK");
	}
	public void setYjlbz(String yjlbz){
		this.setInternal("YJLBZ",yjlbz);
	}
	public String getYjlbz(){
		return (String)this.getInternal("YJLBZ");
	}
	public void setSjid(String sjid){
		this.setInternal("SJID",sjid);
	}
	public String getSjid(){
		return (String)this.getInternal("SJID");
	}
	public void setJgzt(String jgzt){
		this.setInternal("JGZT",jgzt);
	}
	public String getJgzt(){
		return (String)this.getInternal("JGZT");
	}
	public void setJgztt(String jgztt){
		this.setInternal("JGZTT",jgztt);
	}
	public String getJgztt(){
		return (String)this.getInternal("JGZTT");
	}
	public void setXjxj(String xjxj){
		this.setInternal("XJXJ",xjxj);
	}
	public String getXjxj(){
		return (String)this.getInternal("XJXJ");
	}
}