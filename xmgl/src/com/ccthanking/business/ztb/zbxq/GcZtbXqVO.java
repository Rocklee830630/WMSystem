package com.ccthanking.business.ztb.zbxq;
import com.ccthanking.framework.base.BaseVO;
import java.util.Date;

public class GcZtbXqVO extends BaseVO{

	public GcZtbXqVO(){
		this.addField("GC_ZTB_XQ_ID",OP_STRING|this.TP_PK);
		this.addField("GZMC",OP_STRING);
		this.addField("GZNR",OP_STRING);
		this.addField("ZZYJYQ",OP_STRING);
		this.addField("SXYQ",OP_STRING);
		this.addField("JSYQ",OP_STRING);
		this.addField("CGMBYQ",OP_STRING);
		this.addField("PBRYYQ",OP_STRING);
		this.addField("PBSBYQ",OP_STRING);
		this.addField("TBJFS",OP_STRING);
		this.addField("ZBFS",OP_STRING);
		this.addField("QTYQ",OP_STRING);
		this.addField("ZBLX",OP_STRING);
		this.addField("YSE",OP_STRING);
		this.addField("XQDWJBR",OP_STRING);
		this.addField("XQDWJBRSJ",OP_DATE);
		this.addField("XQDWFZR",OP_STRING);
		this.addField("XQDWFZRSJ",OP_DATE);
		this.addField("ZBBJBR",OP_STRING);
		this.addField("ZBBFZR",OP_STRING);
		this.addField("XQZT",OP_STRING);
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
		this.addField("QFBZ",OP_STRING);
		this.addField("TGBZ",OP_STRING);
		this.addField("ND",OP_STRING);
		this.setFieldDateFormat("XQDWJBRSJ","yyyy-MM-dd");
		this.setFieldDateFormat("XQDWFZRSJ","yyyy-MM-dd");
		this.setFieldDateFormat("LRSJ","yyyy-MM-dd");
		this.setFieldDateFormat("GXSJ","yyyy-MM-dd");
		
		  this.bindFieldToThousand("YSE");//预算额
	      this.bindFieldToDic("ZBLX", "ZBLX");//招标类型
	      this.bindFieldToDic("ZBFS", "ZBFS");//招标方式
	      
		this.setVOTableName("GC_ZTB_XQ");
	}

	public void setGc_ztb_xq_id(String gc_ztb_xq_id){
		this.setInternal("GC_ZTB_XQ_ID",gc_ztb_xq_id);
	}
	public String getGc_ztb_xq_id(){
		return (String)this.getInternal("GC_ZTB_XQ_ID");
	}
	public void setGzmc(String gzmc){
		this.setInternal("GZMC",gzmc);
	}
	public String getGzmc(){
		return (String)this.getInternal("GZMC");
	}
	public void setGznr(String gznr){
		this.setInternal("GZNR",gznr);
	}
	public String getGznr(){
		return (String)this.getInternal("GZNR");
	}
	public void setZzyjyq(String zzyjyq){
		this.setInternal("ZZYJYQ",zzyjyq);
	}
	public String getZzyjyq(){
		return (String)this.getInternal("ZZYJYQ");
	}
	public void setSxyq(String sxyq){
		this.setInternal("SXYQ",sxyq);
	}
	public String getSxyq(){
		return (String)this.getInternal("SXYQ");
	}
	public void setJsyq(String jsyq){
		this.setInternal("JSYQ",jsyq);
	}
	public String getJsyq(){
		return (String)this.getInternal("JSYQ");
	}
	public void setCgmbyq(String cgmbyq){
		this.setInternal("CGMBYQ",cgmbyq);
	}
	public String getCgmbyq(){
		return (String)this.getInternal("CGMBYQ");
	}
	public void setPbryyq(String pbryyq){
		this.setInternal("PBRYYQ",pbryyq);
	}
	public String getPbryyq(){
		return (String)this.getInternal("PBRYYQ");
	}
	public void setPbsbyq(String pbsbyq){
		this.setInternal("PBSBYQ",pbsbyq);
	}
	public String getPbsbyq(){
		return (String)this.getInternal("PBSBYQ");
	}
	public void setTbjfs(String tbjfs){
		this.setInternal("TBJFS",tbjfs);
	}
	public String getTbjfs(){
		return (String)this.getInternal("TBJFS");
	}
	public void setZbfs(String zbfs){
		this.setInternal("ZBFS",zbfs);
	}
	public String getZbfs(){
		return (String)this.getInternal("ZBFS");
	}
	public void setQtyq(String qtyq){
		this.setInternal("QTYQ",qtyq);
	}
	public String getQtyq(){
		return (String)this.getInternal("QTYQ");
	}
	public void setZblx(String zblx){
		this.setInternal("ZBLX",zblx);
	}
	public String getZblx(){
		return (String)this.getInternal("ZBLX");
	}
	public void setYse(String yse){
		this.setInternal("YSE",yse);
	}
	public String getYse(){
		return (String)this.getInternal("YSE");
	}
	public void setXqdwjbr(String xqdwjbr){
		this.setInternal("XQDWJBR",xqdwjbr);
	}
	public String getXqdwjbr(){
		return (String)this.getInternal("XQDWJBR");
	}
	public void setXqdwjbrsj(Date xqdwjbrsj){
		this.setInternal("XQDWJBRSJ",xqdwjbrsj);
	}
	public Date getXqdwjbrsj(){
		return (Date)this.getInternal("XQDWJBRSJ");
	}
	public void setXqdwfzr(String xqdwfzr){
		this.setInternal("XQDWFZR",xqdwfzr);
	}
	public String getXqdwfzr(){
		return (String)this.getInternal("XQDWFZR");
	}
	public void setXqdwfzrsj(Date xqdwfzrsj){
		this.setInternal("XQDWFZRSJ",xqdwfzrsj);
	}
	public Date getXqdwfzrsj(){
		return (Date)this.getInternal("XQDWFZRSJ");
	}
	public void setZbbjbr(String zbbjbr){
		this.setInternal("ZBBJBR",zbbjbr);
	}
	public String getZbbjbr(){
		return (String)this.getInternal("ZBBJBR");
	}
	public void setZbbfzr(String zbbfzr){
		this.setInternal("ZBBFZR",zbbfzr);
	}
	public String getZbbfzr(){
		return (String)this.getInternal("ZBBFZR");
	}
	public void setXqzt(String xqzt){
		this.setInternal("XQZT",xqzt);
	}
	public String getXqzt(){
		return (String)this.getInternal("XQZT");
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
	public void setQfbz(String qfbz){
		this.setInternal("QFBZ",qfbz);
	}
	public String getQfbz(){
		return (String)this.getInternal("QFBZ");
	}
	public void setTgbz(String tgbz){
		this.setInternal("TGBZ",tgbz);
	}
	public String getTgbz(){
		return (String)this.getInternal("TGBZ");
	}
	public void setNd(String nd){
		this.setInternal("ND",nd);
	}
	public String getNd(){
		return (String)this.getInternal("ND");
	}
}