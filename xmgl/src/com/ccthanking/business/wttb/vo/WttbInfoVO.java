package com.ccthanking.business.wttb.vo;
import com.ccthanking.framework.base.BaseVO;
import java.util.Date;

public class WttbInfoVO extends BaseVO{

	public WttbInfoVO(){
		this.addField("WTTB_INFO_ID",OP_STRING|this.TP_PK);
		this.addField("XMMC",OP_STRING);
		this.addField("BDMC",OP_STRING);
		this.addField("WTLX",OP_STRING);
		this.addField("WTBT",OP_STRING);
		this.addField("YJSJ",OP_DATE);
		this.addField("SJSJ",OP_DATE);
		this.addField("SJZT",OP_STRING);
		this.addField("WTXZ",OP_STRING);
		this.addField("CQBZ",OP_STRING);
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
		this.addField("JHSJID",OP_STRING);
		this.addField("YWTXZ",OP_STRING);
		this.addField("XMID",OP_STRING);
		this.addField("BDID",OP_STRING);
		this.addField("TBLX",OP_STRING);
		this.addField("CBCS",OP_STRING);
		this.addField("HYID",OP_STRING);
		this.setFieldDateFormat("YJSJ","yyyy-MM-dd");
		this.setFieldDateFormat("SJSJ","yyyy-MM-dd");
		this.setFieldDateFormat("LRSJ","yyyy-MM-dd");
		this.setFieldDateFormat("GXSJ","yyyy-MM-dd");
		// 字典翻译
		this.bindFieldToDic("WTLX", "WTLX");
		this.bindFieldToDic("SJZT", "WTZT");
		this.bindFieldToDic("TBLX", "TBLX");
		// 部门翻译
		this.bindFieldToOrgDept("LRBM");
		this.bindFieldToOrgDept("GXBM");
		// 账号翻译
		this.bindFieldToUserid("LRR");
		this.bindFieldToUserid("GXR");
		this.setVOTableName("wttb_info");
	}

	public void setWttb_info_id(String wttb_info_id){
		this.setInternal("WTTB_INFO_ID",wttb_info_id);
	}
	public String getWttb_info_id(){
		return (String)this.getInternal("WTTB_INFO_ID");
	}
	public void setXmmc(String xmmc){
		this.setInternal("XMMC",xmmc);
	}
	public String getXmmc(){
		return (String)this.getInternal("XMMC");
	}
	public void setBdmc(String bdmc){
		this.setInternal("BDMC",bdmc);
	}
	public String getBdmc(){
		return (String)this.getInternal("BDMC");
	}
	public void setWtlx(String wtlx){
		this.setInternal("WTLX",wtlx);
	}
	public String getWtlx(){
		return (String)this.getInternal("WTLX");
	}
	public void setWtbt(String wtbt){
		this.setInternal("WTBT",wtbt);
	}
	public String getWtbt(){
		return (String)this.getInternal("WTBT");
	}
	public void setYjsj(Date yjsj){
		this.setInternal("YJSJ",yjsj);
	}
	public Date getYjsj(){
		return (Date)this.getInternal("YJSJ");
	}
	public void setSjsj(Date sjsj){
		this.setInternal("SJSJ",sjsj);
	}
	public Date getSjsj(){
		return (Date)this.getInternal("SJSJ");
	}
	public void setSjzt(String sjzt){
		this.setInternal("SJZT",sjzt);
	}
	public String getSjzt(){
		return (String)this.getInternal("SJZT");
	}
	public void setWtxz(String wtxz){
		this.setInternal("WTXZ",wtxz);
	}
	public String getWtxz(){
		return (String)this.getInternal("WTXZ");
	}
	public void setCqbz(String cqbz){
		this.setInternal("CQBZ",cqbz);
	}
	public String getCqbz(){
		return (String)this.getInternal("CQBZ");
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
	public void setJhsjid(String jhsjid){
		this.setInternal("JHSJID",jhsjid);
	}
	public String getJhsjid(){
		return (String)this.getInternal("JHSJID");
	}
	public void setYwtxz(String ywtxz){
		this.setInternal("YWTXZ",ywtxz);
	}
	public String getYwtxz(){
		return (String)this.getInternal("YWTXZ");
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
	public void setTblx(String tblx){
		this.setInternal("TBLX",tblx);
	}
	public String getTblx(){
		return (String)this.getInternal("TBLX");
	}
	public void setCbcs(String cbcs){
		this.setInternal("CBCS",cbcs);
	}
	public String getCbcs(){
		return (String)this.getInternal("CBCS");
	}
	public void setHyid(String hyid){
		this.setInternal("HYID",hyid);
	}
	public String getHyid(){
		return (String)this.getInternal("HYID");
	}
}