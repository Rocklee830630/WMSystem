package com.ccthanking.business.bdhf.vo;
import com.ccthanking.framework.base.BaseVO;
import java.util.Date;

public class BdwhVO extends BaseVO{

	public BdwhVO(){
		this.addField("GC_XMBD_ID",OP_STRING|this.TP_PK);
		this.addField("JHID",OP_STRING);
		this.addField("JHSJID",OP_STRING);
		this.addField("ND",OP_STRING);
		this.addField("XMID",OP_STRING);
		this.addField("BDBH",OP_STRING);
		this.addField("BDMC",OP_STRING);
		this.addField("QDZH",OP_STRING);
		this.addField("ZDZH",OP_STRING);
		this.addField("CD",OP_STRING);
		this.addField("KD",OP_STRING);
		this.addField("GJ",OP_STRING);
		this.addField("WGRQ",OP_DATE);
		this.addField("QGRQ",OP_DATE);
		this.addField("JGRQ",OP_DATE);
		this.addField("GCZTFY",OP_STRING);
		this.addField("SGDW",OP_STRING);
		this.addField("SGDWFZR",OP_STRING);
		this.addField("SGDWFZRLXFS",OP_STRING);
		this.addField("JLDW",OP_STRING);
		this.addField("JLDWFZR",OP_STRING);
		this.addField("JLDWFZRLXFS",OP_STRING);
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
		this.addField("BDDD",OP_STRING);
		this.addField("MJ",OP_STRING);
		this.addField("SGHTID",OP_STRING);
		this.addField("JLHTID",OP_STRING);
		this.addField("JSGM",OP_STRING);
		this.addField("SJDW",OP_STRING);
		this.addField("SJDWFZR",OP_STRING);
		this.addField("SJDWFZRLXFS",OP_STRING);
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
		this.addField("JSGM_SJ",OP_STRING);
		this.addField("XMGLGS",OP_STRING);
		this.addField("BDWYBH",OP_STRING);
		this.addField("XJGLBDID",OP_STRING);
		this.addField("XJXJ",OP_STRING);
		this.addField("BDBM",OP_STRING);
		this.addField("BD_XMBM",OP_STRING);
		this.addField("PRE_BDBM",OP_STRING);
		this.addField("ISNRTJ",OP_STRING);
		this.setFieldDateFormat("WGRQ","yyyy-MM-dd");
		this.setFieldDateFormat("QGRQ","yyyy-MM-dd");
		this.setFieldDateFormat("JGRQ","yyyy-MM-dd");
		this.setFieldDateFormat("LRSJ","yyyy-MM-dd");
		this.setFieldDateFormat("GXSJ","yyyy-MM-dd");
		
		
		   //字典
	      this.bindFieldToDic("XMLX", "XMLX");
	      this.bindFieldToDic("ND", "XMNF");
	      this.bindFieldToDic("XMSX", "XMSX");
	      this.bindFieldToDic("SFYX", "SF");
	      
	      this.bindFieldToTranslater("SGDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");
	      this.bindFieldToTranslater("JLDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");
	      this.bindFieldToTranslater("SJDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");
	      this.bindFieldToTranslater("XMGLGS", "FS_ORG_DEPT", "ROW_ID", "BMJC");
	      
	      this.setVOTableName("GC_XMBD");
	      this.bindFieldToThousand("GCZTFY"); //数字千分位
	      this.bindFieldToThousand("CD");
	      this.bindFieldToThousand("KD");
	      this.bindFieldToThousand("GJ");
	      this.bindFieldToThousand("MJ");
	      
		this.setVOTableName("GC_XMBD");
	}

	public void setGc_xmbd_id(String gc_xmbd_id){
		this.setInternal("GC_XMBD_ID",gc_xmbd_id);
	}
	public String getGc_xmbd_id(){
		return (String)this.getInternal("GC_XMBD_ID");
	}
	public void setJhid(String jhid){
		this.setInternal("JHID",jhid);
	}
	public String getJhid(){
		return (String)this.getInternal("JHID");
	}
	public void setJhsjid(String jhsjid){
		this.setInternal("JHSJID",jhsjid);
	}
	public String getJhsjid(){
		return (String)this.getInternal("JHSJID");
	}
	public void setNd(String nd){
		this.setInternal("ND",nd);
	}
	public String getNd(){
		return (String)this.getInternal("ND");
	}
	public void setXmid(String xmid){
		this.setInternal("XMID",xmid);
	}
	public String getXmid(){
		return (String)this.getInternal("XMID");
	}
	public void setBdbh(String bdbh){
		this.setInternal("BDBH",bdbh);
	}
	public String getBdbh(){
		return (String)this.getInternal("BDBH");
	}
	public void setBdmc(String bdmc){
		this.setInternal("BDMC",bdmc);
	}
	public String getBdmc(){
		return (String)this.getInternal("BDMC");
	}
	public void setQdzh(String qdzh){
		this.setInternal("QDZH",qdzh);
	}
	public String getQdzh(){
		return (String)this.getInternal("QDZH");
	}
	public void setZdzh(String zdzh){
		this.setInternal("ZDZH",zdzh);
	}
	public String getZdzh(){
		return (String)this.getInternal("ZDZH");
	}
	public void setCd(String cd){
		this.setInternal("CD",cd);
	}
	public String getCd(){
		return (String)this.getInternal("CD");
	}
	public void setKd(String kd){
		this.setInternal("KD",kd);
	}
	public String getKd(){
		return (String)this.getInternal("KD");
	}
	public void setGj(String gj){
		this.setInternal("GJ",gj);
	}
	public String getGj(){
		return (String)this.getInternal("GJ");
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
	public void setGcztfy(String gcztfy){
		this.setInternal("GCZTFY",gcztfy);
	}
	public String getGcztfy(){
		return (String)this.getInternal("GCZTFY");
	}
	public void setSgdw(String sgdw){
		this.setInternal("SGDW",sgdw);
	}
	public String getSgdw(){
		return (String)this.getInternal("SGDW");
	}
	public void setSgdwfzr(String sgdwfzr){
		this.setInternal("SGDWFZR",sgdwfzr);
	}
	public String getSgdwfzr(){
		return (String)this.getInternal("SGDWFZR");
	}
	public void setSgdwfzrlxfs(String sgdwfzrlxfs){
		this.setInternal("SGDWFZRLXFS",sgdwfzrlxfs);
	}
	public String getSgdwfzrlxfs(){
		return (String)this.getInternal("SGDWFZRLXFS");
	}
	public void setJldw(String jldw){
		this.setInternal("JLDW",jldw);
	}
	public String getJldw(){
		return (String)this.getInternal("JLDW");
	}
	public void setJldwfzr(String jldwfzr){
		this.setInternal("JLDWFZR",jldwfzr);
	}
	public String getJldwfzr(){
		return (String)this.getInternal("JLDWFZR");
	}
	public void setJldwfzrlxfs(String jldwfzrlxfs){
		this.setInternal("JLDWFZRLXFS",jldwfzrlxfs);
	}
	public String getJldwfzrlxfs(){
		return (String)this.getInternal("JLDWFZRLXFS");
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
	public void setBddd(String bddd){
		this.setInternal("BDDD",bddd);
	}
	public String getBddd(){
		return (String)this.getInternal("BDDD");
	}
	public void setMj(String mj){
		this.setInternal("MJ",mj);
	}
	public String getMj(){
		return (String)this.getInternal("MJ");
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
	public void setJsgm(String jsgm){
		this.setInternal("JSGM",jsgm);
	}
	public String getJsgm(){
		return (String)this.getInternal("JSGM");
	}
	public void setSjdw(String sjdw){
		this.setInternal("SJDW",sjdw);
	}
	public String getSjdw(){
		return (String)this.getInternal("SJDW");
	}
	public void setSjdwfzr(String sjdwfzr){
		this.setInternal("SJDWFZR",sjdwfzr);
	}
	public String getSjdwfzr(){
		return (String)this.getInternal("SJDWFZR");
	}
	public void setSjdwfzrlxfs(String sjdwfzrlxfs){
		this.setInternal("SJDWFZRLXFS",sjdwfzrlxfs);
	}
	public String getSjdwfzrlxfs(){
		return (String)this.getInternal("SJDWFZRLXFS");
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
	public void setJsgm_sj(String jsgm_sj){
		this.setInternal("JSGM_SJ",jsgm_sj);
	}
	public String getJsgm_sj(){
		return (String)this.getInternal("JSGM_SJ");
	}
	public void setXmglgs(String xmglgs){
		this.setInternal("XMGLGS",xmglgs);
	}
	public String getXmglgs(){
		return (String)this.getInternal("XMGLGS");
	}
	public void setBdwybh(String bdwybh){
		this.setInternal("BDWYBH",bdwybh);
	}
	public String getBdwybh(){
		return (String)this.getInternal("BDWYBH");
	}
	public void setXjglbdid(String xjglbdid){
		this.setInternal("XJGLBDID",xjglbdid);
	}
	public String getXjglbdid(){
		return (String)this.getInternal("XJGLBDID");
	}
	public void setXjxj(String xjxj){
		this.setInternal("XJXJ",xjxj);
	}
	public String getXjxj(){
		return (String)this.getInternal("XJXJ");
	}
	public void setBdbm(String bdbm){
		this.setInternal("BDBM",bdbm);
	}
	public String getBdbm(){
		return (String)this.getInternal("BDBM");
	}
	public void setBd_xmbm(String bd_xmbm){
		this.setInternal("BD_XMBM",bd_xmbm);
	}
	public String getBd_xmbm(){
		return (String)this.getInternal("BD_XMBM");
	}
	public void setPre_bdbm(String pre_bdbm){
		this.setInternal("PRE_BDBM",pre_bdbm);
	}
	public String getPre_bdbm(){
		return (String)this.getInternal("PRE_BDBM");
	}
	public void setIsnrtj(String isnrtj){
		this.setInternal("ISNRTJ",isnrtj);
	}
	public String getIsnrtj(){
		return (String)this.getInternal("ISNRTJ");
	}
}