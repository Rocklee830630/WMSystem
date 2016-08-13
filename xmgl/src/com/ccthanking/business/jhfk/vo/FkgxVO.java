package com.ccthanking.business.jhfk.vo;
import com.ccthanking.framework.base.BaseVO;
import java.util.Date;

public class FkgxVO extends BaseVO{

	public FkgxVO(){
		this.addField("FKLX",OP_STRING);
		this.addField("FKMS",OP_STRING);
		this.addField("JHZDMC",OP_STRING);
		this.addField("YWZDMC",OP_STRING);
		this.addField("FKLB",OP_STRING);
		this.addField("BZ",OP_STRING);
		this.addField("YWBMC",OP_STRING);
		this.addField("SORT",OP_STRING);
		this.addField("TITLE",OP_STRING);
		this.addField("YWLX",OP_STRING);
		this.addField("KZZDMC",OP_STRING);
		this.addField("HISURL",OP_STRING);
		this.addField("HISDESC",OP_STRING);
		this.addField("SJBMC",OP_STRING);
		this.addField("SJZDMC",OP_STRING);
		this.addField("JHSJMC",OP_STRING);
		this.addField("JHSJMS",OP_STRING);
		this.addField("FLAG",OP_STRING);
		this.setVOTableName("GC_JH_FKGX");
	}

	public void setFklx(String fklx){
		this.setInternal("FKLX",fklx);
	}
	public String getFklx(){
		return (String)this.getInternal("FKLX");
	}
	public void setFkms(String fkms){
		this.setInternal("FKMS",fkms);
	}
	public String getFkms(){
		return (String)this.getInternal("FKMS");
	}
	public void setJhzdmc(String jhzdmc){
		this.setInternal("JHZDMC",jhzdmc);
	}
	public String getJhzdmc(){
		return (String)this.getInternal("JHZDMC");
	}
	public void setYwzdmc(String ywzdmc){
		this.setInternal("YWZDMC",ywzdmc);
	}
	public String getYwzdmc(){
		return (String)this.getInternal("YWZDMC");
	}
	public void setFklb(String fklb){
		this.setInternal("FKLB",fklb);
	}
	public String getFklb(){
		return (String)this.getInternal("FKLB");
	}
	public void setBz(String bz){
		this.setInternal("BZ",bz);
	}
	public String getBz(){
		return (String)this.getInternal("BZ");
	}
	public void setYwbmc(String ywbmc){
		this.setInternal("YWBMC",ywbmc);
	}
	public String getYwbmc(){
		return (String)this.getInternal("YWBMC");
	}
	public void setSort(String sort){
		this.setInternal("SORT",sort);
	}
	public String getSort(){
		return (String)this.getInternal("SORT");
	}
	public void setTitle(String title){
		this.setInternal("TITLE",title);
	}
	public String getTitle(){
		return (String)this.getInternal("TITLE");
	}
	public void setYwlx(String ywlx){
		this.setInternal("YWLX",ywlx);
	}
	public String getYwlx(){
		return (String)this.getInternal("YWLX");
	}
	public void setKzzdmc(String kzzdmc){
		this.setInternal("KZZDMC",kzzdmc);
	}
	public String getKzzdmc(){
		return (String)this.getInternal("KZZDMC");
	}
	public void setHisurl(String hisurl){
		this.setInternal("HISURL",hisurl);
	}
	public String getHisurl(){
		return (String)this.getInternal("HISURL");
	}
	public void setHisdesc(String hisdesc){
		this.setInternal("HISDESC",hisdesc);
	}
	public String getHisdesc(){
		return (String)this.getInternal("HISDESC");
	}
	public void setSjbmc(String sjbmc){
		this.setInternal("SJBMC",sjbmc);
	}
	public String getSjbmc(){
		return (String)this.getInternal("SJBMC");
	}
	public void setSjzdmc(String sjzdmc){
		this.setInternal("SJZDMC",sjzdmc);
	}
	public String getSjzdmc(){
		return (String)this.getInternal("SJZDMC");
	}
	public void setJhsjmc(String jhsjmc){
		this.setInternal("JHSJMC",jhsjmc);
	}
	public String getJhsjmc(){
		return (String)this.getInternal("JHSJMC");
	}
	public void setJhsjms(String jhsjms){
		this.setInternal("JHSJMS",jhsjms);
	}
	public String getJhsjms(){
		return (String)this.getInternal("JHSJMS");
	}
	public void setFlag(String flag){
		this.setInternal("FLAG",flag);
	}
	public String getFlag(){
		return (String)this.getInternal("FLAG");
	}
}