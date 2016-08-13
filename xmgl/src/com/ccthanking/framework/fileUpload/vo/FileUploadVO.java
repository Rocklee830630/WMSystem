package com.ccthanking.framework.fileUpload.vo;
import com.ccthanking.framework.base.BaseVO;
import java.util.Date;

public class FileUploadVO extends BaseVO{

	public FileUploadVO(){
		this.addField("FILEID",OP_STRING|this.TP_PK);
		this.addField("FILENAME",OP_STRING);
		this.addField("URL",OP_STRING);
		this.addField("ZHUX",OP_STRING);
		this.addField("BZ",OP_STRING);
		this.addField("LRR",OP_STRING);
		this.addField("LRBM",OP_STRING);
		this.addField("LRSJ",OP_DATE);
		this.addField("GXR",OP_STRING);
		this.addField("GXBM",OP_STRING);
		this.addField("GXSJ",OP_DATE);
		this.addField("SJBH",OP_STRING);
		this.addField("YWLX",OP_STRING);
		this.addField("FJLX",OP_STRING);
		this.addField("FILESIZE",OP_STRING);
		this.addField("YWID",OP_STRING);
		this.addField("FJZT",OP_STRING);
		this.addField("GLID1",OP_STRING);
		this.addField("GLID2",OP_STRING);
		this.addField("GLID3",OP_STRING);
		this.addField("GLID4",OP_STRING);
		this.addField("FJLB",OP_STRING);
		this.addField("WYBH",OP_STRING);
		this.setFieldDateFormat("LRSJ","yyyy-MM-dd");
		this.setFieldDateFormat("GXSJ","yyyy-MM-dd");
		this.setVOTableName("FS_FILEUPLOAD");
	}

	public void setFileid(String fileid){
		this.setInternal("FILEID",fileid);
	}
	public String getFileid(){
		return (String)this.getInternal("FILEID");
	}
	public void setFilename(String filename){
		this.setInternal("FILENAME",filename);
	}
	public String getFilename(){
		return (String)this.getInternal("FILENAME");
	}
	public void setUrl(String url){
		this.setInternal("URL",url);
	}
	public String getUrl(){
		return (String)this.getInternal("URL");
	}
	public void setZhux(String zhux){
		this.setInternal("ZHUX",zhux);
	}
	public String getZhux(){
		return (String)this.getInternal("ZHUX");
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
	public void setLrbm(String lrbm){
		this.setInternal("LRBM",lrbm);
	}
	public String getLrbm(){
		return (String)this.getInternal("LRBM");
	}
	public void setLrsj(Date lrsj){
		this.setInternal("LRSJ",lrsj);
	}
	public Date getLrsj(){
		return (Date)this.getInternal("LRSJ");
	}
	public void setGxr(String gxr){
		this.setInternal("GXR",gxr);
	}
	public String getGxr(){
		return (String)this.getInternal("GXR");
	}
	public void setGxbm(String gxbm){
		this.setInternal("GXBM",gxbm);
	}
	public String getGxbm(){
		return (String)this.getInternal("GXBM");
	}
	public void setGxsj(Date gxsj){
		this.setInternal("GXSJ",gxsj);
	}
	public Date getGxsj(){
		return (Date)this.getInternal("GXSJ");
	}
	public void setSjbh(String sjbh){
		this.setInternal("SJBH",sjbh);
	}
	public String getSjbh(){
		return (String)this.getInternal("SJBH");
	}
	public void setYwlx(String ywlx){
		this.setInternal("YWLX",ywlx);
	}
	public String getYwlx(){
		return (String)this.getInternal("YWLX");
	}
	public void setFjlx(String fjlx){
		this.setInternal("FJLX",fjlx);
	}
	public String getFjlx(){
		return (String)this.getInternal("FJLX");
	}
	public void setFilesize(String filesize){
		this.setInternal("FILESIZE",filesize);
	}
	public String getFilesize(){
		return (String)this.getInternal("FILESIZE");
	}
	public void setYwid(String ywid){
		this.setInternal("YWID",ywid);
	}
	public String getYwid(){
		return (String)this.getInternal("YWID");
	}
	public void setFjzt(String fjzt){
		this.setInternal("FJZT",fjzt);
	}
	public String getFjzt(){
		return (String)this.getInternal("FJZT");
	}
	public void setGlid1(String glid1){
		this.setInternal("GLID1",glid1);
	}
	public String getGlid1(){
		return (String)this.getInternal("GLID1");
	}
	public void setGlid2(String glid2){
		this.setInternal("GLID2",glid2);
	}
	public String getGlid2(){
		return (String)this.getInternal("GLID2");
	}
	public void setGlid3(String glid3){
		this.setInternal("GLID3",glid3);
	}
	public String getGlid3(){
		return (String)this.getInternal("GLID3");
	}
	public void setGlid4(String glid4){
		this.setInternal("GLID4",glid4);
	}
	public String getGlid4(){
		return (String)this.getInternal("GLID4");
	}
	public void setFjlb(String fjlb){
		this.setInternal("FJLB",fjlb);
	}
	public String getFjlb(){
		return (String)this.getInternal("FJLB");
	}
	public void setWybh(String wybh){
		this.setInternal("WYBH",wybh);
	}
	public String getWybh(){
		return (String)this.getInternal("WYBH");
	}
}