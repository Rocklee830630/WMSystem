package com.ccthanking.business.qqsx.qqsxgl;
import com.ccthanking.framework.base.BaseVO;
import java.util.Date;

public class GcZgbQqsxVO extends BaseVO{

	public GcZgbQqsxVO(){
		this.addField("GC_ZGB_QQSX_ID",OP_STRING|this.TP_PK);
		this.addField("XDKID",OP_STRING);
		this.addField("JBDW",OP_STRING);
		this.addField("JER",OP_STRING);
		this.addField("JJSJ",OP_DATE);
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
		this.addField("JHID",OP_STRING);
		this.addField("ND",OP_STRING);
		this.addField("JJCLMX",OP_STRING);
		this.addField("LXFKZT",OP_STRING);
		this.addField("SGFKZT",OP_STRING);
		this.addField("TDFKZT",OP_STRING);
		this.addField("GHFKZT",OP_STRING);
		this.addField("JHSJID",OP_STRING);
		this.addField("LXSFBL",OP_STRING);
		this.addField("SGSFBL",OP_STRING);
		this.addField("TDSFBL",OP_STRING);
		this.addField("GHSFBL",OP_STRING);
		this.addField("LXKYBJSJ",OP_DATE);
		this.addField("LXKYBBLYY",OP_STRING);
		this.addField("LXKYBBLSX",OP_STRING);
		this.addField("SGXKBJSJ",OP_DATE);
		this.addField("SGXKBBLYY",OP_STRING);
		this.addField("SGXKBBLSX",OP_STRING);
		this.addField("TDSPBJSJ",OP_DATE);
		this.addField("TDSPBBLYY",OP_STRING);
		this.addField("TDSPBBLSX",OP_STRING);
		this.addField("GHSPBJSJ",OP_DATE);
		this.addField("GHSPBBLYY",OP_STRING);
		this.addField("GHSPBBLSX",OP_STRING);
		this.addField("SJWYBH",OP_STRING);
		this.setFieldDateFormat("JJSJ","yyyy-MM-dd");
		this.setFieldDateFormat("LRSJ","yyyy-MM-dd");
		this.setFieldDateFormat("GXSJ","yyyy-MM-dd");
		this.setFieldDateFormat("LXKYBJSJ","yyyy-MM-dd");
		this.setFieldDateFormat("SGXKBJSJ","yyyy-MM-dd");
		this.setFieldDateFormat("TDSPBJSJ","yyyy-MM-dd");
		this.setFieldDateFormat("GHSPBJSJ","yyyy-MM-dd");
		this.bindFieldToDic("GHSPBBLSX", "GHSX");
		this.bindFieldToDic("LXKYBBLSX", "LXKYFJLX");
		this.bindFieldToDic("SGXKBBLSX", "SGXKSX");
		this.bindFieldToDic("TDSPBBLSX", "TDSPSX");
		this.setVOTableName("GC_ZGB_QQSX");
	}

	public void setGc_zgb_qqsx_id(String gc_zgb_qqsx_id){
		this.setInternal("GC_ZGB_QQSX_ID",gc_zgb_qqsx_id);
	}
	public String getGc_zgb_qqsx_id(){
		return (String)this.getInternal("GC_ZGB_QQSX_ID");
	}
	public void setXdkid(String xdkid){
		this.setInternal("XDKID",xdkid);
	}
	public String getXdkid(){
		return (String)this.getInternal("XDKID");
	}
	public void setJbdw(String jbdw){
		this.setInternal("JBDW",jbdw);
	}
	public String getJbdw(){
		return (String)this.getInternal("JBDW");
	}
	public void setJer(String jer){
		this.setInternal("JER",jer);
	}
	public String getJer(){
		return (String)this.getInternal("JER");
	}
	public void setJjsj(Date jjsj){
		this.setInternal("JJSJ",jjsj);
	}
	public Date getJjsj(){
		return (Date)this.getInternal("JJSJ");
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
	public void setJhid(String jhid){
		this.setInternal("JHID",jhid);
	}
	public String getJhid(){
		return (String)this.getInternal("JHID");
	}
	public void setNd(String nd){
		this.setInternal("ND",nd);
	}
	public String getNd(){
		return (String)this.getInternal("ND");
	}
	public void setJjclmx(String jjclmx){
		this.setInternal("JJCLMX",jjclmx);
	}
	public String getJjclmx(){
		return (String)this.getInternal("JJCLMX");
	}
	public void setLxfkzt(String lxfkzt){
		this.setInternal("LXFKZT",lxfkzt);
	}
	public String getLxfkzt(){
		return (String)this.getInternal("LXFKZT");
	}
	public void setSgfkzt(String sgfkzt){
		this.setInternal("SGFKZT",sgfkzt);
	}
	public String getSgfkzt(){
		return (String)this.getInternal("SGFKZT");
	}
	public void setTdfkzt(String tdfkzt){
		this.setInternal("TDFKZT",tdfkzt);
	}
	public String getTdfkzt(){
		return (String)this.getInternal("TDFKZT");
	}
	public void setGhfkzt(String ghfkzt){
		this.setInternal("GHFKZT",ghfkzt);
	}
	public String getGhfkzt(){
		return (String)this.getInternal("GHFKZT");
	}
	public void setJhsjid(String jhsjid){
		this.setInternal("JHSJID",jhsjid);
	}
	public String getJhsjid(){
		return (String)this.getInternal("JHSJID");
	}
	public void setLxsfbl(String lxsfbl){
		this.setInternal("LXSFBL",lxsfbl);
	}
	public String getLxsfbl(){
		return (String)this.getInternal("LXSFBL");
	}
	public void setSgsfbl(String sgsfbl){
		this.setInternal("SGSFBL",sgsfbl);
	}
	public String getSgsfbl(){
		return (String)this.getInternal("SGSFBL");
	}
	public void setTdsfbl(String tdsfbl){
		this.setInternal("TDSFBL",tdsfbl);
	}
	public String getTdsfbl(){
		return (String)this.getInternal("TDSFBL");
	}
	public void setGhsfbl(String ghsfbl){
		this.setInternal("GHSFBL",ghsfbl);
	}
	public String getGhsfbl(){
		return (String)this.getInternal("GHSFBL");
	}
	public void setLxkybjsj(Date lxkybjsj){
		this.setInternal("LXKYBJSJ",lxkybjsj);
	}
	public Date getLxkybjsj(){
		return (Date)this.getInternal("LXKYBJSJ");
	}
	public void setLxkybblyy(String lxkybblyy){
		this.setInternal("LXKYBBLYY",lxkybblyy);
	}
	public String getLxkybblyy(){
		return (String)this.getInternal("LXKYBBLYY");
	}
	public void setLxkybblsx(String lxkybblsx){
		this.setInternal("LXKYBBLSX",lxkybblsx);
	}
	public String getLxkybblsx(){
		return (String)this.getInternal("LXKYBBLSX");
	}
	public void setSgxkbjsj(Date sgxkbjsj){
		this.setInternal("SGXKBJSJ",sgxkbjsj);
	}
	public Date getSgxkbjsj(){
		return (Date)this.getInternal("SGXKBJSJ");
	}
	public void setSgxkbblyy(String sgxkbblyy){
		this.setInternal("SGXKBBLYY",sgxkbblyy);
	}
	public String getSgxkbblyy(){
		return (String)this.getInternal("SGXKBBLYY");
	}
	public void setSgxkbblsx(String sgxkbblsx){
		this.setInternal("SGXKBBLSX",sgxkbblsx);
	}
	public String getSgxkbblsx(){
		return (String)this.getInternal("SGXKBBLSX");
	}
	public void setTdspbjsj(Date tdspbjsj){
		this.setInternal("TDSPBJSJ",tdspbjsj);
	}
	public Date getTdspbjsj(){
		return (Date)this.getInternal("TDSPBJSJ");
	}
	public void setTdspbblyy(String tdspbblyy){
		this.setInternal("TDSPBBLYY",tdspbblyy);
	}
	public String getTdspbblyy(){
		return (String)this.getInternal("TDSPBBLYY");
	}
	public void setTdspbblsx(String tdspbblsx){
		this.setInternal("TDSPBBLSX",tdspbblsx);
	}
	public String getTdspbblsx(){
		return (String)this.getInternal("TDSPBBLSX");
	}
	public void setGhspbjsj(Date ghspbjsj){
		this.setInternal("GHSPBJSJ",ghspbjsj);
	}
	public Date getGhspbjsj(){
		return (Date)this.getInternal("GHSPBJSJ");
	}
	public void setGhspbblyy(String ghspbblyy){
		this.setInternal("GHSPBBLYY",ghspbblyy);
	}
	public String getGhspbblyy(){
		return (String)this.getInternal("GHSPBBLYY");
	}
	public void setGhspbblsx(String ghspbblsx){
		this.setInternal("GHSPBBLSX",ghspbblsx);
	}
	public String getGhspbblsx(){
		return (String)this.getInternal("GHSPBBLSX");
	}
	public void setSjwybh(String sjwybh){
		this.setInternal("SJWYBH",sjwybh);
	}
	public String getSjwybh(){
		return (String)this.getInternal("SJWYBH");
	}
}