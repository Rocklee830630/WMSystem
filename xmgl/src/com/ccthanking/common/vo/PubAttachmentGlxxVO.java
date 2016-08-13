package com.ccthanking.common.vo;

import com.ccthanking.framework.base.BaseVO;

import java.util.Date;

public class PubAttachmentGlxxVO
    extends BaseVO
{
    public PubAttachmentGlxxVO()
    {
        super(3);
        //设置字段信息
        this.addField("GLXH", this.OP_STRING | this.TP_PK); //关联序号
        this.addField("FJLBS", this.OP_BLOB); //文件名
        this.addField("YWLX", this.OP_STRING);
        this.addField("SJBH", this.OP_STRING);
        //设置时间的显示格式
        this.setVOTableName("PUB_ATTACHMENT_GLXX");

    }
    public void setFjlbs(byte[] fjlbs)
    {
        this.setInternal("FJLBS", fjlbs); 
    }
    public byte[] getFjlbs(){
    	return (byte[])this.getInternal("FJLBS");
    }
    public void setGlxh(String glxh){
    	this.setInternal("GLXH", glxh);
    }
    public String getGlxh(){
    	return (String)this.getInternal("GLXH");
    }
    public void setSjbh(String sjbh){
    	this.setInternal("SJBH", sjbh);
    }
    public String getSjbh(){
    	return (String)this.getInternal("SJBH");
    }
    public void setYwlx(String ywlx){
    	this.setInternal("YWLX", ywlx);
    }
    public String getYwlx(){
    	return (String)this.getInternal("YWLX");
    }
}