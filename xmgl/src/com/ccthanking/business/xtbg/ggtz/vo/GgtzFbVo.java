package com.ccthanking.business.xtbg.ggtz.vo;

import java.util.Date;

import com.ccthanking.framework.base.BaseVO;

/**
 * @auther xhb 
 */
public class GgtzFbVo extends BaseVO {
	
	public GgtzFbVo(){
		this.addField("FBID",OP_STRING|this.TP_PK);
		this.addField("GGID",OP_STRING);
		this.addField("GGBT",OP_STRING);
		this.addField("GGLB",OP_STRING);
		this.addField("FBR",OP_STRING);
		this.addField("FBBM",OP_STRING);
		this.addField("FBBMMC",OP_STRING);
		this.addField("FBSJ",OP_DATE);
		this.addField("NF",OP_STRING);
		this.addField("JSR_ACCOUNT",OP_STRING);
		this.addField("JSR",OP_STRING);
		this.addField("JSBM",OP_STRING);
		this.addField("JSBMMC",OP_STRING);
		this.addField("SFYD",OP_STRING);
		this.addField("SFYX",OP_STRING);
		this.addField("FBFWMC",OP_STRING);
		this.addField("LRSJ",OP_DATE);

		this.bindFieldToDic("GGLB", "GGLB");
		this.setFieldDateFormat("FBSJ","yyyy-MM-dd");
		this.setFieldDateFormat("LRSJ","yyyy-MM-dd");
		
		this.setVOTableName("XTBG_XXZX_GGTZ_FB");
	}
	
	public void setFbfwmc(String Fbfwmc){
		this.setInternal("FBFWMC",Fbfwmc);
	}
	public String getFbfwmc(){
		return (String)this.getInternal("FBFWMC");
	}
	

	public void setFbid(String fbid){
		this.setInternal("FBID",fbid);
	}
	public String getFbid(){
		return (String)this.getInternal("FBID");
	}
	public void setGgid(String ggid){
		this.setInternal("GGID",ggid);
	}
	public String getGgid(){
		return (String)this.getInternal("GGID");
	}
	public void setGgbt(String ggbt){
		this.setInternal("GGBT",ggbt);
	}
	public String getGgbt(){
		return (String)this.getInternal("GGBT");
	}
	public void setGglb(String gglb){
		this.setInternal("GGLB",gglb);
	}
	public String getGglb(){
		return (String)this.getInternal("GGLB");
	}
	public void setFbr(String fbr){
		this.setInternal("FBR",fbr);
	}
	public String getFbr(){
		return (String)this.getInternal("FBR");
	}
	public void setFbbm(String fbbm){
		this.setInternal("FBBM",fbbm);
	}
	public String getFbbm(){
		return (String)this.getInternal("FBBM");
	}
	public void setFbbmmc(String fbbmmc){
		this.setInternal("FBBMMC",fbbmmc);
	}
	public String getFbbmmc(){
		return (String)this.getInternal("FBBMMC");
	}
	public void setFbsj(Date fbsj){
		this.setInternal("FBSJ",fbsj);
	}
	public Date getFbsj(){
		return (Date)this.getInternal("FBSJ");
	}
	public void setNf(String nf){
		this.setInternal("NF",nf);
	}
	public String getNf(){
		return (String)this.getInternal("NF");
	}
	public void setJsr_account(String jsr_account){
		this.setInternal("JSR_ACCOUNT",jsr_account);
	}
	public String getJsr_account(){
		return (String)this.getInternal("JSR_ACCOUNT");
	}
	public void setJsr(String jsr){
		this.setInternal("JSR",jsr);
	}
	public String getJsr(){
		return (String)this.getInternal("JSR");
	}
	public void setJsbm(String jsbm){
		this.setInternal("JSBM",jsbm);
	}
	public String getJsbm(){
		return (String)this.getInternal("JSBM");
	}
	public void setJsbmmc(String jsbmmc){
		this.setInternal("JSBMMC",jsbmmc);
	}
	public String getJsbmmc(){
		return (String)this.getInternal("JSBMMC");
	}
	public void setSfyd(String sfyd){
		this.setInternal("SFYD",sfyd);
	}
	public String getSfyd(){
		return (String)this.getInternal("SFYD");
	}
	public void setSfyx(String sfyx){
		this.setInternal("SFYX",sfyx);
	}
	public String getSfyx(){
		return (String)this.getInternal("SFYX");
	}

	public void setLrsj(Date lrsj){
		this.setInternal("LRSJ",lrsj);
	}
	public Date getLrsj(){
		return (Date)this.getInternal("LRSJ");
	}
}
