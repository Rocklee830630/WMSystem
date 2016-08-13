package com.ccthanking.business.xdxmk.vo;
import com.ccthanking.framework.base.BaseVO;
import java.util.Date;

public class XmxdkSpVO extends BaseVO{

	public XmxdkSpVO(){
		this.addField("GC_TCJH_XMXDK_SP_ID",OP_STRING|this.TP_PK);
		this.addField("XMBH",OP_STRING);
		this.addField("XMMC",OP_STRING);
		this.addField("QY",OP_STRING);
		this.addField("ND",OP_STRING);
		this.addField("PCH",OP_STRING);
		this.addField("XMLX",OP_STRING);
		this.addField("XMDZ",OP_STRING);
		this.addField("JSNR",OP_STRING);
		this.addField("JSYY",OP_STRING);
		this.addField("JSRW",OP_STRING);
		this.addField("JSBYX",OP_STRING);
		this.addField("JHZTZE",OP_STRING);
		this.addField("GC",OP_STRING);
		this.addField("ZC",OP_STRING);
		this.addField("QT",OP_STRING);
		this.addField("XMSX",OP_STRING);
		this.addField("ISBT",OP_STRING);
		this.addField("ISNATC",OP_STRING);
		this.addField("JSZT",OP_STRING);
		this.addField("XMFR",OP_STRING);
		this.addField("XMGLGS",OP_STRING);
		this.addField("FZR_GLGS",OP_STRING);
		this.addField("LXFS_GLGS",OP_STRING);
		this.addField("SGDW",OP_STRING);
		this.addField("FZR_SGDW",OP_STRING);
		this.addField("LXFS_SGDW",OP_STRING);
		this.addField("JLDW",OP_STRING);
		this.addField("FZR_JLDW",OP_STRING);
		this.addField("LXFS_JLDW",OP_STRING);
		this.addField("YZDB",OP_STRING);
		this.addField("SJDW",OP_STRING);
		this.addField("FZR_SJDW",OP_STRING);
		this.addField("LXFS_SJDW",OP_STRING);
		this.addField("LXFS_YZDB",OP_STRING);
		this.addField("WGRQ",OP_DATE);
		this.addField("QGRQ",OP_DATE);
		this.addField("JGRQ",OP_DATE);
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
		this.addField("XMCBK_ID",OP_STRING);
		this.addField("XJXJ",OP_STRING);
		this.addField("PXH",OP_STRING);
		this.addField("XJ_XMID",OP_STRING);
		this.addField("WDD",OP_STRING);
		this.addField("JSMB",OP_STRING);
		this.addField("SGHTID",OP_STRING);
		this.addField("JLHTID",OP_STRING);
		this.addField("SJHTID",OP_STRING);
		this.addField("SGDWXMJL",OP_STRING);
		this.addField("SGDWXMJLLXDH",OP_STRING);
		this.addField("SGDWJSFZR",OP_STRING);
		this.addField("SGDWJSFZRLXDH",OP_STRING);
		this.addField("JLDWZJ",OP_STRING);
		this.addField("JLDWZJLXDH",OP_STRING);
		this.addField("JLDWZJDB",OP_STRING);
		this.addField("JLDWZJDBLXDH",OP_STRING);
		this.addField("JLDWAQJL",OP_STRING);
		this.addField("JLDWAQJLLXDH",OP_STRING);
		this.addField("JSNR_SJ",OP_STRING);
		this.addField("TCJHZTID",OP_STRING);
		this.setFieldDateFormat("WGRQ","yyyy-MM-dd");
		this.setFieldDateFormat("QGRQ","yyyy-MM-dd");
		this.setFieldDateFormat("JGRQ","yyyy-MM-dd");
		this.setFieldDateFormat("LRSJ","yyyy-MM-dd");
		this.setFieldDateFormat("GXSJ","yyyy-MM-dd");
		this.setVOTableName("GC_TCJH_XMXDK_SP");
	}

	public void setGc_tcjh_xmxdk_sp_id(String gc_tcjh_xmxdk_sp_id){
		this.setInternal("GC_TCJH_XMXDK_SP_ID",gc_tcjh_xmxdk_sp_id);
	}
	public String getGc_tcjh_xmxdk_sp_id(){
		return (String)this.getInternal("GC_TCJH_XMXDK_SP_ID");
	}
	public void setXmbh(String xmbh){
		this.setInternal("XMBH",xmbh);
	}
	public String getXmbh(){
		return (String)this.getInternal("XMBH");
	}
	public void setXmmc(String xmmc){
		this.setInternal("XMMC",xmmc);
	}
	public String getXmmc(){
		return (String)this.getInternal("XMMC");
	}
	public void setQy(String qy){
		this.setInternal("QY",qy);
	}
	public String getQy(){
		return (String)this.getInternal("QY");
	}
	public void setNd(String nd){
		this.setInternal("ND",nd);
	}
	public String getNd(){
		return (String)this.getInternal("ND");
	}
	public void setPch(String pch){
		this.setInternal("PCH",pch);
	}
	public String getPch(){
		return (String)this.getInternal("PCH");
	}
	public void setXmlx(String xmlx){
		this.setInternal("XMLX",xmlx);
	}
	public String getXmlx(){
		return (String)this.getInternal("XMLX");
	}
	public void setXmdz(String xmdz){
		this.setInternal("XMDZ",xmdz);
	}
	public String getXmdz(){
		return (String)this.getInternal("XMDZ");
	}
	public void setJsnr(String jsnr){
		this.setInternal("JSNR",jsnr);
	}
	public String getJsnr(){
		return (String)this.getInternal("JSNR");
	}
	public void setJsyy(String jsyy){
		this.setInternal("JSYY",jsyy);
	}
	public String getJsyy(){
		return (String)this.getInternal("JSYY");
	}
	public void setJsrw(String jsrw){
		this.setInternal("JSRW",jsrw);
	}
	public String getJsrw(){
		return (String)this.getInternal("JSRW");
	}
	public void setJsbyx(String jsbyx){
		this.setInternal("JSBYX",jsbyx);
	}
	public String getJsbyx(){
		return (String)this.getInternal("JSBYX");
	}
	public void setJhztze(String jhztze){
		this.setInternal("JHZTZE",jhztze);
	}
	public String getJhztze(){
		return (String)this.getInternal("JHZTZE");
	}
	public void setGc(String gc){
		this.setInternal("GC",gc);
	}
	public String getGc(){
		return (String)this.getInternal("GC");
	}
	public void setZc(String zc){
		this.setInternal("ZC",zc);
	}
	public String getZc(){
		return (String)this.getInternal("ZC");
	}
	public void setQt(String qt){
		this.setInternal("QT",qt);
	}
	public String getQt(){
		return (String)this.getInternal("QT");
	}
	public void setXmsx(String xmsx){
		this.setInternal("XMSX",xmsx);
	}
	public String getXmsx(){
		return (String)this.getInternal("XMSX");
	}
	public void setIsbt(String isbt){
		this.setInternal("ISBT",isbt);
	}
	public String getIsbt(){
		return (String)this.getInternal("ISBT");
	}
	public void setIsnatc(String isnatc){
		this.setInternal("ISNATC",isnatc);
	}
	public String getIsnatc(){
		return (String)this.getInternal("ISNATC");
	}
	public void setJszt(String jszt){
		this.setInternal("JSZT",jszt);
	}
	public String getJszt(){
		return (String)this.getInternal("JSZT");
	}
	public void setXmfr(String xmfr){
		this.setInternal("XMFR",xmfr);
	}
	public String getXmfr(){
		return (String)this.getInternal("XMFR");
	}
	public void setXmglgs(String xmglgs){
		this.setInternal("XMGLGS",xmglgs);
	}
	public String getXmglgs(){
		return (String)this.getInternal("XMGLGS");
	}
	public void setFzr_glgs(String fzr_glgs){
		this.setInternal("FZR_GLGS",fzr_glgs);
	}
	public String getFzr_glgs(){
		return (String)this.getInternal("FZR_GLGS");
	}
	public void setLxfs_glgs(String lxfs_glgs){
		this.setInternal("LXFS_GLGS",lxfs_glgs);
	}
	public String getLxfs_glgs(){
		return (String)this.getInternal("LXFS_GLGS");
	}
	public void setSgdw(String sgdw){
		this.setInternal("SGDW",sgdw);
	}
	public String getSgdw(){
		return (String)this.getInternal("SGDW");
	}
	public void setFzr_sgdw(String fzr_sgdw){
		this.setInternal("FZR_SGDW",fzr_sgdw);
	}
	public String getFzr_sgdw(){
		return (String)this.getInternal("FZR_SGDW");
	}
	public void setLxfs_sgdw(String lxfs_sgdw){
		this.setInternal("LXFS_SGDW",lxfs_sgdw);
	}
	public String getLxfs_sgdw(){
		return (String)this.getInternal("LXFS_SGDW");
	}
	public void setJldw(String jldw){
		this.setInternal("JLDW",jldw);
	}
	public String getJldw(){
		return (String)this.getInternal("JLDW");
	}
	public void setFzr_jldw(String fzr_jldw){
		this.setInternal("FZR_JLDW",fzr_jldw);
	}
	public String getFzr_jldw(){
		return (String)this.getInternal("FZR_JLDW");
	}
	public void setLxfs_jldw(String lxfs_jldw){
		this.setInternal("LXFS_JLDW",lxfs_jldw);
	}
	public String getLxfs_jldw(){
		return (String)this.getInternal("LXFS_JLDW");
	}
	public void setYzdb(String yzdb){
		this.setInternal("YZDB",yzdb);
	}
	public String getYzdb(){
		return (String)this.getInternal("YZDB");
	}
	public void setSjdw(String sjdw){
		this.setInternal("SJDW",sjdw);
	}
	public String getSjdw(){
		return (String)this.getInternal("SJDW");
	}
	public void setFzr_sjdw(String fzr_sjdw){
		this.setInternal("FZR_SJDW",fzr_sjdw);
	}
	public String getFzr_sjdw(){
		return (String)this.getInternal("FZR_SJDW");
	}
	public void setLxfs_sjdw(String lxfs_sjdw){
		this.setInternal("LXFS_SJDW",lxfs_sjdw);
	}
	public String getLxfs_sjdw(){
		return (String)this.getInternal("LXFS_SJDW");
	}
	public void setLxfs_yzdb(String lxfs_yzdb){
		this.setInternal("LXFS_YZDB",lxfs_yzdb);
	}
	public String getLxfs_yzdb(){
		return (String)this.getInternal("LXFS_YZDB");
	}
	public void setWgrq(Date wgrq){
		this.setInternal("WGRQ",wgrq);
	}
	public Date getWgrq(){
		return (Date)this.getInternal("WGRQ");
	}
	public void setQgrq(Date qgrq){
		this.setInternal("QGRQ",qgrq);
	}
	public Date getQgrq(){
		return (Date)this.getInternal("QGRQ");
	}
	public void setJgrq(Date jgrq){
		this.setInternal("JGRQ",jgrq);
	}
	public Date getJgrq(){
		return (Date)this.getInternal("JGRQ");
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
	public void setXmcbk_id(String xmcbk_id){
		this.setInternal("XMCBK_ID",xmcbk_id);
	}
	public String getXmcbk_id(){
		return (String)this.getInternal("XMCBK_ID");
	}
	public void setXjxj(String xjxj){
		this.setInternal("XJXJ",xjxj);
	}
	public String getXjxj(){
		return (String)this.getInternal("XJXJ");
	}
	public void setPxh(String pxh){
		this.setInternal("PXH",pxh);
	}
	public String getPxh(){
		return (String)this.getInternal("PXH");
	}
	public void setXj_xmid(String xj_xmid){
		this.setInternal("XJ_XMID",xj_xmid);
	}
	public String getXj_xmid(){
		return (String)this.getInternal("XJ_XMID");
	}
	public void setWdd(String wdd){
		this.setInternal("WDD",wdd);
	}
	public String getWdd(){
		return (String)this.getInternal("WDD");
	}
	public void setJsmb(String jsmb){
		this.setInternal("JSMB",jsmb);
	}
	public String getJsmb(){
		return (String)this.getInternal("JSMB");
	}
	public void setSghtid(String sghtid){
		this.setInternal("SGHTID",sghtid);
	}
	public String getSghtid(){
		return (String)this.getInternal("SGHTID");
	}
	public void setJlhtid(String jlhtid){
		this.setInternal("JLHTID",jlhtid);
	}
	public String getJlhtid(){
		return (String)this.getInternal("JLHTID");
	}
	public void setSjhtid(String sjhtid){
		this.setInternal("SJHTID",sjhtid);
	}
	public String getSjhtid(){
		return (String)this.getInternal("SJHTID");
	}
	public void setSgdwxmjl(String sgdwxmjl){
		this.setInternal("SGDWXMJL",sgdwxmjl);
	}
	public String getSgdwxmjl(){
		return (String)this.getInternal("SGDWXMJL");
	}
	public void setSgdwxmjllxdh(String sgdwxmjllxdh){
		this.setInternal("SGDWXMJLLXDH",sgdwxmjllxdh);
	}
	public String getSgdwxmjllxdh(){
		return (String)this.getInternal("SGDWXMJLLXDH");
	}
	public void setSgdwjsfzr(String sgdwjsfzr){
		this.setInternal("SGDWJSFZR",sgdwjsfzr);
	}
	public String getSgdwjsfzr(){
		return (String)this.getInternal("SGDWJSFZR");
	}
	public void setSgdwjsfzrlxdh(String sgdwjsfzrlxdh){
		this.setInternal("SGDWJSFZRLXDH",sgdwjsfzrlxdh);
	}
	public String getSgdwjsfzrlxdh(){
		return (String)this.getInternal("SGDWJSFZRLXDH");
	}
	public void setJldwzj(String jldwzj){
		this.setInternal("JLDWZJ",jldwzj);
	}
	public String getJldwzj(){
		return (String)this.getInternal("JLDWZJ");
	}
	public void setJldwzjlxdh(String jldwzjlxdh){
		this.setInternal("JLDWZJLXDH",jldwzjlxdh);
	}
	public String getJldwzjlxdh(){
		return (String)this.getInternal("JLDWZJLXDH");
	}
	public void setJldwzjdb(String jldwzjdb){
		this.setInternal("JLDWZJDB",jldwzjdb);
	}
	public String getJldwzjdb(){
		return (String)this.getInternal("JLDWZJDB");
	}
	public void setJldwzjdblxdh(String jldwzjdblxdh){
		this.setInternal("JLDWZJDBLXDH",jldwzjdblxdh);
	}
	public String getJldwzjdblxdh(){
		return (String)this.getInternal("JLDWZJDBLXDH");
	}
	public void setJldwaqjl(String jldwaqjl){
		this.setInternal("JLDWAQJL",jldwaqjl);
	}
	public String getJldwaqjl(){
		return (String)this.getInternal("JLDWAQJL");
	}
	public void setJldwaqjllxdh(String jldwaqjllxdh){
		this.setInternal("JLDWAQJLLXDH",jldwaqjllxdh);
	}
	public String getJldwaqjllxdh(){
		return (String)this.getInternal("JLDWAQJLLXDH");
	}
	public void setJsnr_sj(String jsnr_sj){
		this.setInternal("JSNR_SJ",jsnr_sj);
	}
	public String getJsnr_sj(){
		return (String)this.getInternal("JSNR_SJ");
	}
	public void setTcjhztid(String tcjhztid){
		this.setInternal("TCJHZTID",tcjhztid);
	}
	public String getTcjhztid(){
		return (String)this.getInternal("TCJHZTID");
	}
}