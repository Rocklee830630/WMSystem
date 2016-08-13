package com.ccthanking.common.vo;

import com.ccthanking.framework.base.BaseVO;

import java.util.Date;

public class PubAttachmentVO
    extends BaseVO
{
    public PubAttachmentVO()
    {
        super(11);
        //设置字段信息
        this.addField("SJBH", this.OP_STRING); //事件编号
        this.addField("YWLX", this.OP_STRING); //业务类型
        this.addField("FJBH", this.OP_STRING | this.TP_PK); //附件编号
        this.addField("DAH", this.OP_STRING); //归档号
        this.addField("FILENAME", this.OP_STRING); //文件名
        this.addField("DATA", this.OP_BLOB); //数据
        this.addField("MEMO", this.OP_STRING); //备注
        this.addField("CJSJ", this.OP_DATE); //入库时间
        this.addField("STATE", this.OP_STRING); //状态
        this.addField("SCSJ", this.OP_DATE); //删除时间
        this.addField("FJLX",this.OP_STRING);
        this.addField("FJLBJDBH", this.OP_STRING);
        this.addField("FJLBJDMC", this.OP_STRING);
        //设置主键信息
        //设置字典类型定义
        this.bindFieldToDic("STATE", "SF"); //状态
        this.bindFieldToDic("YWLX", "YWLX"); //业务类型
        //设置时间的显示格式
        //this.bindFieldToSequence("FJBH","seq_common_serival_number");
        this.setFieldDateFormat("CJSJ", "yyyy-MM-dd HH:mm:ss");
        this.setFieldDateFormat("SCSJ", "yyyy-MM-dd HH:mm:ss");
        //设置表名称
        //设置表名称
        this.setVOTableName("PUB_ATTACHMENT");

    }
    public void setFjlbjdbh(String fjlbjdbh)
    {
        this.setInternal("FJLBJDBH", fjlbjdbh); // 附件类别节点编号
    }
    public String getFjlbjdbh()
    {
    	return (String)this.getInternal("FJLBJDBH"); // 附件类别节点编号
    }
    public void setFjlbjdmc(String fjlbjdmc)
    {
        this.setInternal("FJLBJDMC", fjlbjdmc); // 附件类别节点名称
    }
    public String getFjlbjdmc()
    {
    	return (String)this.getInternal("FJLBJDMC"); // 附件类别节点名称
    }    
    /**
     * 设置事件编号
     * @param sjbh String
     */
    public void setSjbh(String sjbh)
    {
        this.setInternal("SJBH", sjbh); //事件编号
    }

    /**
     * 设置业务类型
     * @param ywlx String
     */
    public void setYwlx(String ywlx)
    {
        this.setInternal("YWLX", ywlx); //业务类型
    }

    /**
     * 设置附件编号
     * @param fjbh String
     */
    public void setFjbh(String fjbh)
    {
        this.setInternal("FJBH", fjbh); //附件编号
    }

    /**
     * 设置归档号
     * @param dah String
     */
    public void setDah(String dah)
    {
        this.setInternal("DAH", dah); //归档号
    }

    /**
     * 设置文件名
     * @param filename String
     */
    public void setFilename(String filename)
    {
        this.setInternal("FILENAME", filename); //文件名
    }

    /**
     * 设置数据
     * @param data String
     */
    public void setData(byte[] data)
    {
        this.setInternal("DATA", data); //数据
    }

    /**
     * 设置备注
     * @param memo String
     */
    public void setMemo(String memo)
    {
        this.setInternal("MEMO", memo); //备注
    }

    /**
     * 设置入库时间
     * @param cjsj String
     */
    public void setCjsj(Date cjsj)
    {
        this.setInternal("CJSJ", cjsj); //入库时间
    }

    /**
     * 设置状态
     * @param state String
     */
    public void setState(String state)
    {
        this.setInternal("STATE", state); //状态
    }

    /**
     * 设置删除时间
     * @param scsj String
     */
    public void setScsj(Date scsj)
    {
        this.setInternal("SCSJ", scsj); //删除时间
    }

    /**
     * 获得事件编号
     * @return String
     */
    public String getSjbh()
    {
        return (String)this.getInternal("SJBH");
    }

    /**
     * 获得业务类型
     * @return String
     */
    public String getYwlx()
    {
        return (String)this.getInternal("YWLX");
    }

    /**
     * 获得附件编号
     * @return String
     */
    public String getFjbh()
    {
        return (String)this.getInternal("FJBH");
    }

    /**
     * 获得归档号
     * @return String
     */
    public String getDah()
    {
        return (String)this.getInternal("DAH");
    }

    /**
     * 获得文件名
     * @return String
     */
    public String getFilename()
    {
        return (String)this.getInternal("FILENAME");
    }

    /**
     * 获得数据
     * @return String
     */
    public byte[] getData()
    {
        return (byte[])this.getInternal("DATA");
    }

    /**
     * 获得备注
     * @return String
     */
    public String getMemo()
    {
        return (String)this.getInternal("MEMO");
    }

    /**
     * 获得入库时间
     * @return String
     */
    public Date getCjsj()
    {
        return (Date)this.getInternal("CJSJ");
    }

    /**
     * 获得状态
     * @return String
     */
    public String getState()
    {
        return (String)this.getInternal("STATE");
    }

    /**
     * 获得删除时间
     * @return String
     */
    public Date getScsj()
    {
        return (Date)this.getInternal("SCSJ");
    }

    public void setFjlx(String fjlx)
    {
        this.setInternal("FJLX",fjlx);
    }

    public String getFjlx()
    {
        return (String) this.getInternal("FJLX");
    }

}