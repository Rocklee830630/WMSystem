package com.ccthanking.business.htgl.vo;
import com.ccthanking.framework.base.BaseVO;
import java.util.Date;

public class GcHtglHtWjVO extends BaseVO{

	public GcHtglHtWjVO(){
		this.addField("ID",OP_STRING|this.TP_PK);//编号
		this.addField("HTSJID",OP_STRING);//合同数据编号
		this.addField("WJLX",OP_STRING);//文件类型
		this.addField("JDLX",OP_STRING);//节点类型，文档或目录
		this.addField("WJMC",OP_STRING);//文件名称
		this.addField("WJKZM",OP_STRING);//文件扩展名
		this.addField("WJDX",OP_STRING);//文件大小(Byte)
		this.addField("MIMELX",OP_STRING);//MIME_TYPE
		this.addField("YWLX",OP_STRING);//业务类型
		this.addField("SJBH",OP_STRING);//事件编号
		this.addField("SJMJ",OP_STRING);//数据密级
		this.addField("SFYX",OP_STRING);//是否有效
		this.addField("LRR",OP_STRING);//录入人
		this.addField("LRSJ",OP_DATE);//录入时间
		this.addField("LRBM",OP_STRING);//录入部门
		this.addField("LRBMMC",OP_STRING);//录入部门名称
		this.addField("GXR",OP_STRING);//更新人
		this.addField("GXSJ",OP_DATE);//更新时间
		this.addField("GXBM",OP_STRING);//更新部门
		this.addField("GXBMMC",OP_STRING);//更新部门名称
		this.addField("SORTNO",OP_STRING);//排序号
		this.setFieldDateFormat("LRSJ","yyyy-MM-dd");
		this.setFieldDateFormat("GXSJ","yyyy-MM-dd");
		this.setVOTableName("GC_HTGL_HT_WJ");
	}

	public void setId(String id){
		this.setInternal("ID",id);
	}
	public String getId(){
		return (String)this.getInternal("ID");
	}
	public void setHtsjid(String htsjid){
		this.setInternal("HTSJID",htsjid);
	}
	public String getHtsjid(){
		return (String)this.getInternal("HTSJID");
	}
	public void setWjlx(String wjlx){
		this.setInternal("WJLX",wjlx);
	}
	public String getWjlx(){
		return (String)this.getInternal("WJLX");
	}
	public void setJdlx(String jdlx){
		this.setInternal("JDLX",jdlx);
	}
	public String getJdlx(){
		return (String)this.getInternal("JDLX");
	}
	public void setWjmc(String wjmc){
		this.setInternal("WJMC",wjmc);
	}
	public String getWjmc(){
		return (String)this.getInternal("WJMC");
	}
	public void setWjkzm(String wjkzm){
		this.setInternal("WJKZM",wjkzm);
	}
	public String getWjkzm(){
		return (String)this.getInternal("WJKZM");
	}
	public void setWjdx(String wjdx){
		this.setInternal("WJDX",wjdx);
	}
	public String getWjdx(){
		return (String)this.getInternal("WJDX");
	}
	public void setMimelx(String mimelx){
		this.setInternal("MIMELX",mimelx);
	}
	public String getMimelx(){
		return (String)this.getInternal("MIMELX");
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
	public void setSortno(String sortno){
		this.setInternal("SORTNO",sortno);
	}
	public String getSortno(){
		return (String)this.getInternal("SORTNO");
	}
}