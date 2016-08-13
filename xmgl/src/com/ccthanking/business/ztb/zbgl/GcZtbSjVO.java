package com.ccthanking.business.ztb.zbgl;
import com.ccthanking.framework.base.BaseVO;
import java.util.Date;

public class GcZtbSjVO extends BaseVO{

	public GcZtbSjVO(){
		this.addField("GC_ZTB_SJ_ID",OP_STRING|this.TP_PK);
		this.addField("ZJGCMC",OP_STRING);
		this.addField("ZTBMC",OP_STRING);
		this.addField("ZBBH",OP_STRING);
		this.addField("ZBFS",OP_STRING);
		this.addField("ZBDL",OP_STRING);
		this.addField("KBRQ",OP_DATE);
		this.addField("DZFS",OP_STRING);
		this.addField("ZBTZSBH",OP_STRING);
		this.addField("ZZBJ",OP_STRING);
		this.addField("BJXS",OP_STRING);
		this.addField("DSFJGID",OP_STRING);
		this.addField("XMZJ",OP_STRING);
		this.addField("XMQTJLRY",OP_STRING);
		this.addField("XMJL",OP_STRING);
		this.addField("XMQTRY",OP_STRING);
		this.addField("CSLBJ",OP_STRING);
		this.addField("ZBJBZCS",OP_STRING);
		this.addField("XCYQJF",OP_STRING);
		this.addField("YLJ",OP_STRING);
		this.addField("YJNLYBZJ",OP_STRING);
		this.addField("GGFBMT",OP_STRING);
		this.addField("GGFBJSRQ",OP_DATE);
		this.addField("GGQK",OP_STRING);
		this.addField("GGFBQSRQ",OP_DATE);
		this.addField("ZKXS",OP_STRING);
		this.addField("JHZZTZJ",OP_STRING);
		this.addField("BZZJ",OP_STRING);
		this.addField("ZBDLF",OP_STRING);
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
		this.addField("XQZT",OP_STRING);
		this.addField("GGNR",OP_STRING);
		this.addField("SJFZR",OP_STRING);
		this.addField("ZJDB",OP_STRING);
		this.addField("AQJL",OP_STRING);
		this.addField("JSFZR",OP_STRING);
		this.setFieldDateFormat("KBRQ","yyyy-MM-dd");
		this.setFieldDateFormat("GGFBJSRQ","yyyy-MM-dd");
		this.setFieldDateFormat("GGFBQSRQ","yyyy-MM-dd");
		this.setFieldDateFormat("LRSJ","yyyy-MM-dd");
		this.setFieldDateFormat("GXSJ","yyyy-MM-dd");
		//绑定字典
	    this.bindFieldToDic("ZBFS", "ZBFS");//招标方式
	    this.bindFieldToDic("DZFS", "DZFS");//垫资方式
	    //千分位
	    this.bindFieldToThousand("ZZBJ");//总中标价
	    this.bindFieldToThousand("YLJ");//预留金
	    this.bindFieldToThousand("YJNLYBZJ");//应缴纳履约保证金
	    this.bindFieldToThousand("JHZZTZJ");//计划中主体造价
	    this.bindFieldToDic("GGQK", "GGQK");//公告情况
	    this.bindFieldToTranslater("DSFJGID", "GC_CJDW", "GC_CJDW_ID", "DWMC");
		
		this.setVOTableName("GC_ZTB_SJ");
	}

	public void setGc_ztb_sj_id(String gc_ztb_sj_id){
		this.setInternal("GC_ZTB_SJ_ID",gc_ztb_sj_id);
	}
	public String getGc_ztb_sj_id(){
		return (String)this.getInternal("GC_ZTB_SJ_ID");
	}
	public void setZjgcmc(String zjgcmc){
		this.setInternal("ZJGCMC",zjgcmc);
	}
	public String getZjgcmc(){
		return (String)this.getInternal("ZJGCMC");
	}
	public void setZtbmc(String ztbmc){
		this.setInternal("ZTBMC",ztbmc);
	}
	public String getZtbmc(){
		return (String)this.getInternal("ZTBMC");
	}
	public void setZbbh(String zbbh){
		this.setInternal("ZBBH",zbbh);
	}
	public String getZbbh(){
		return (String)this.getInternal("ZBBH");
	}
	public void setZbfs(String zbfs){
		this.setInternal("ZBFS",zbfs);
	}
	public String getZbfs(){
		return (String)this.getInternal("ZBFS");
	}
	public void setZbdl(String zbdl){
		this.setInternal("ZBDL",zbdl);
	}
	public String getZbdl(){
		return (String)this.getInternal("ZBDL");
	}
	public void setKbrq(Date kbrq){
		this.setInternal("KBRQ",kbrq);
	}
	public Date getKbrq(){
		return (Date)this.getInternal("KBRQ");
	}
	public void setDzfs(String dzfs){
		this.setInternal("DZFS",dzfs);
	}
	public String getDzfs(){
		return (String)this.getInternal("DZFS");
	}
	public void setZbtzsbh(String zbtzsbh){
		this.setInternal("ZBTZSBH",zbtzsbh);
	}
	public String getZbtzsbh(){
		return (String)this.getInternal("ZBTZSBH");
	}
	public void setZzbj(String zzbj){
		this.setInternal("ZZBJ",zzbj);
	}
	public String getZzbj(){
		return (String)this.getInternal("ZZBJ");
	}
	public void setBjxs(String bjxs){
		this.setInternal("BJXS",bjxs);
	}
	public String getBjxs(){
		return (String)this.getInternal("BJXS");
	}
	public void setDsfjgid(String dsfjgid){
		this.setInternal("DSFJGID",dsfjgid);
	}
	public String getDsfjgid(){
		return (String)this.getInternal("DSFJGID");
	}
	public void setXmzj(String xmzj){
		this.setInternal("XMZJ",xmzj);
	}
	public String getXmzj(){
		return (String)this.getInternal("XMZJ");
	}
	public void setXmqtjlry(String xmqtjlry){
		this.setInternal("XMQTJLRY",xmqtjlry);
	}
	public String getXmqtjlry(){
		return (String)this.getInternal("XMQTJLRY");
	}
	public void setXmjl(String xmjl){
		this.setInternal("XMJL",xmjl);
	}
	public String getXmjl(){
		return (String)this.getInternal("XMJL");
	}
	public void setXmqtry(String xmqtry){
		this.setInternal("XMQTRY",xmqtry);
	}
	public String getXmqtry(){
		return (String)this.getInternal("XMQTRY");
	}
	public void setCslbj(String cslbj){
		this.setInternal("CSLBJ",cslbj);
	}
	public String getCslbj(){
		return (String)this.getInternal("CSLBJ");
	}
	public void setZbjbzcs(String zbjbzcs){
		this.setInternal("ZBJBZCS",zbjbzcs);
	}
	public String getZbjbzcs(){
		return (String)this.getInternal("ZBJBZCS");
	}
	public void setXcyqjf(String xcyqjf){
		this.setInternal("XCYQJF",xcyqjf);
	}
	public String getXcyqjf(){
		return (String)this.getInternal("XCYQJF");
	}
	public void setYlj(String ylj){
		this.setInternal("YLJ",ylj);
	}
	public String getYlj(){
		return (String)this.getInternal("YLJ");
	}
	public void setYjnlybzj(String yjnlybzj){
		this.setInternal("YJNLYBZJ",yjnlybzj);
	}
	public String getYjnlybzj(){
		return (String)this.getInternal("YJNLYBZJ");
	}
	public void setGgfbmt(String ggfbmt){
		this.setInternal("GGFBMT",ggfbmt);
	}
	public String getGgfbmt(){
		return (String)this.getInternal("GGFBMT");
	}
	public void setGgfbjsrq(Date ggfbjsrq){
		this.setInternal("GGFBJSRQ",ggfbjsrq);
	}
	public Date getGgfbjsrq(){
		return (Date)this.getInternal("GGFBJSRQ");
	}
	public void setGgqk(String ggqk){
		this.setInternal("GGQK",ggqk);
	}
	public String getGgqk(){
		return (String)this.getInternal("GGQK");
	}
	public void setGgfbqsrq(Date ggfbqsrq){
		this.setInternal("GGFBQSRQ",ggfbqsrq);
	}
	public Date getGgfbqsrq(){
		return (Date)this.getInternal("GGFBQSRQ");
	}
	public void setZkxs(String zkxs){
		this.setInternal("ZKXS",zkxs);
	}
	public String getZkxs(){
		return (String)this.getInternal("ZKXS");
	}
	public void setJhzztzj(String jhzztzj){
		this.setInternal("JHZZTZJ",jhzztzj);
	}
	public String getJhzztzj(){
		return (String)this.getInternal("JHZZTZJ");
	}
	public void setBzzj(String bzzj){
		this.setInternal("BZZJ",bzzj);
	}
	public String getBzzj(){
		return (String)this.getInternal("BZZJ");
	}
	public void setZbdlf(String zbdlf){
		this.setInternal("ZBDLF",zbdlf);
	}
	public String getZbdlf(){
		return (String)this.getInternal("ZBDLF");
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
	public void setXqzt(String xqzt){
		this.setInternal("XQZT",xqzt);
	}
	public String getXqzt(){
		return (String)this.getInternal("XQZT");
	}
	public void setGgnr(String ggnr){
		this.setInternal("GGNR",ggnr);
	}
	public String getGgnr(){
		return (String)this.getInternal("GGNR");
	}
	public void setSjfzr(String sjfzr){
		this.setInternal("SJFZR",sjfzr);
	}
	public String getSjfzr(){
		return (String)this.getInternal("SJFZR");
	}
	public void setZjdb(String zjdb){
		this.setInternal("ZJDB",zjdb);
	}
	public String getZjdb(){
		return (String)this.getInternal("ZJDB");
	}
	public void setAqjl(String aqjl){
		this.setInternal("AQJL",aqjl);
	}
	public String getAqjl(){
		return (String)this.getInternal("AQJL");
	}
	public void setJsfzr(String jsfzr){
		this.setInternal("JSFZR",jsfzr);
	}
	public String getJsfzr(){
		return (String)this.getInternal("JSFZR");
	}
}