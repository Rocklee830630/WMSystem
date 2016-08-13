package com.ccthanking.business.xtbg.txl.vo;

import java.util.Date;

import com.ccthanking.framework.base.BaseVO;
import com.ccthanking.framework.util.DateTimeUtil;

/**
 * @auther xhb 
 */
public class TxlVo extends BaseVO {
	public TxlVo() {
		this.addField("TXID",OP_STRING|this.TP_PK);

		this.addField("XM",OP_STRING);
		this.addField("NC",OP_STRING);
		this.addField("XB",OP_STRING);
		this.addField("CSRQ",OP_DATE);
		this.addField("DW",OP_STRING);
		this.addField("ZW",OP_STRING);
		this.addField("GZDH",OP_STRING);
		this.addField("GZCZ",OP_STRING);
		this.addField("GZDZ",OP_STRING);
		this.addField("YX",OP_STRING);
		this.addField("SJHM",OP_STRING);
		this.addField("ZZDH",OP_STRING);
		this.addField("ZZDZ",OP_STRING);
		this.addField("QQ",OP_STRING);
		this.addField("MSN",OP_STRING);
		this.addField("XMJP",OP_STRING);
		this.addField("BS",OP_STRING);
		this.addField("USERID",OP_STRING);
		this.addField("ORDERNUM",OP_STRING);
		this.addField("BZ",OP_STRING);
		this.addField("YWLX",OP_STRING);
		this.addField("SJBH",OP_STRING);
		this.addField("BZ2",OP_STRING);
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
		this.addField("GROUPID",OP_STRING);
		
		this.setFieldDateFormat("CSRQ",DateTimeUtil.DEFAULT_DATE_FORMAT);
		this.setFieldDateFormat("LRSJ",DateTimeUtil.DEFAULT_DATETIME_FORMAT);
		this.setFieldDateFormat("GXSJ",DateTimeUtil.DEFAULT_DATETIME_FORMAT);
		
		this.bindFieldToDic("XB", "XB");
		this.bindFieldToDic("BS", "BS");
		this.bindFieldToDic("ZW", "ZW");
		this.bindFieldToOrgDept("DW");

		this.setVOTableName("XTBG_TXL");
	}
	
	public void setTxid(String txid){
		this.setInternal("TXID",txid);
	}
	public String getTxid(){
		return (String)this.getInternal("TXID");
	}
	public void setXm(String xm){
		this.setInternal("XM",xm);
	}
	public String getXm(){
		return (String)this.getInternal("XM");
	}
	public void setNc(String nc){
		this.setInternal("NC",nc);
	}
	public String getNc(){
		return (String)this.getInternal("NC");
	}
	public void setXb(String xb){
		this.setInternal("XB",xb);
	}
	public String getXb(){
		return (String)this.getInternal("XB");
	}
	public void setCsrq(Date csrq){
		this.setInternal("CSRQ",csrq);
	}
	public Date getCsrq(){
		return (Date)this.getInternal("CSRQ");
	}
	public void setDw(String dw){
		this.setInternal("DW",dw);
	}
	public String getDw(){
		return (String)this.getInternal("DW");
	}
	public void setZw(String zw){
		this.setInternal("ZW",zw);
	}
	public String getZw(){
		return (String)this.getInternal("ZW");
	}
	public void setGzdh(String gzdh){
		this.setInternal("GZDH",gzdh);
	}
	public String getGzdh(){
		return (String)this.getInternal("GZDH");
	}
	public void setGzcz(String gzcz){
		this.setInternal("GZCZ",gzcz);
	}
	public String getGzcz(){
		return (String)this.getInternal("GZCZ");
	}
	public void setGzdz(String gzdz){
		this.setInternal("GZDZ",gzdz);
	}
	public String getGzdz(){
		return (String)this.getInternal("GZDZ");
	}
	public void setYx(String yx){
		this.setInternal("YX",yx);
	}
	public String getYx(){
		return (String)this.getInternal("YX");
	}
	public void setSjhm(String sjhm){
		this.setInternal("SJHM",sjhm);
	}
	public String getSjhm(){
		return (String)this.getInternal("SJHM");
	}
	public void setZzdh(String zzdh){
		this.setInternal("ZZDH",zzdh);
	}
	public String getZzdh(){
		return (String)this.getInternal("ZZDH");
	}
	public void setZzdz(String zzdz){
		this.setInternal("ZZDZ",zzdz);
	}
	public String getZzdz(){
		return (String)this.getInternal("ZZDZ");
	}
	public void setQq(String qq){
		this.setInternal("QQ",qq);
	}
	public String getQq(){
		return (String)this.getInternal("QQ");
	}
	public void setMsn(String msn){
		this.setInternal("MSN",msn);
	}
	public String getMsn(){
		return (String)this.getInternal("MSN");
	}
	public void setXmjp(String xmjp){
		this.setInternal("XMJP",xmjp);
	}
	public String getXmjp(){
		return (String)this.getInternal("XMJP");
	}
	public void setBs(String bs){
		this.setInternal("BS",bs);
	}
	public String getBs(){
		return (String)this.getInternal("BS");
	}
	public void setUserid(String userid){
		this.setInternal("USERID",userid);
	}
	public String getUserid(){
		return (String)this.getInternal("USERID");
	}
	public void setOrdernum(String ordernum){
		this.setInternal("ORDERNUM",ordernum);
	}
	public String getOrdernum(){
		return (String)this.getInternal("ORDERNUM");
	}
	public void setBz(String bz){
		this.setInternal("BZ",bz);
	}
	public String getBz(){
		return (String)this.getInternal("BZ");
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
	public void setBz2(String bz2){
		this.setInternal("BZ2",bz2);
	}
	public String getBz2(){
		return (String)this.getInternal("BZ2");
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
	public void setGroupId(String groupId){
		this.setInternal("GROUPID",groupId);
	}
	public String getGroupId(){
		return (String)this.getInternal("GROUPID");
	}
}
