package com.ccthanking.common.vo;

import com.ccthanking.framework.base.*;

import java.util.*;

/**
 * @author wangzh
 * @version 1.0
 */

public class EventVO extends BaseVO
{
    public EventVO()
    {
        super(17);
        this.addField("SJBH",this.TP_PK);
        this.addField("SJZT");
        this.addField("YWLX");
        this.addField("SLSJ",this.OP_DATE);
        this.addField("SBRBH");
        this.addField("SBRSFHM");
        this.addField("SBRXM",this.OP_NCHAR);
        this.addField("PCSDM");
        this.addField("FJDM");
        this.addField("SJDM");
        this.addField("CZYBH");
        this.addField("CZYXM",this.OP_NCHAR);
        this.addField("GDRBH");
        this.addField("GDRXM",this.OP_NCHAR);
        this.addField("GDSJ");
        this.addField("GDDWBH");
        this.addField("SSXQ");


        this.setVOOwner("empty");
        this.setVOTableName("fs_event");
    }

    public void setSjbh(String aSjbh)
    {
        this.setInternal("SJBH",aSjbh);
    }
    public String getSjbh()
    {
        return (String) this.getInternal("SJBH");
    }
    public void setSjzt(String aSjzt)
    {
        this.setInternal("SJZT",aSjzt);
    }
    public String getSjzt()
    {
        return (String) this.getInternal("SJZT");
    }

    public void setYwlx(String aYwlx)
    {
        this.setInternal("YWLX",aYwlx);
    }
    public String getYwlx()
    {
        return (String) this.getInternal("Ywlx");
    }
    public void setSlsj(Date aSlsj)
    {
        this.setInternal("SLSJ",aSlsj);
    }
    public Date getSlsj()
    {
        return (Date) this.getInternal("SLSJ");
    }
    public void setSbrbh(String aSbrbh)
    {
        this.setInternal("SBRBH",aSbrbh);
    }
    public String getSbrbh()
    {
        return (String) this.getInternal("SBRBH");
    }
    public void setSbrsfhm(String aSbrsfhm)
    {
        this.setInternal("SBRSFHM",aSbrsfhm);
    }
    public String getSbrsfhm()
    {
        return (String) this.getInternal("SBRSFHM");
    }
    public void setSbrxm(String aSbrxm)
    {
        this.setInternal("SBRXM",aSbrxm);
    }
    public String getSbrxm()
    {
        return (String) this.getInternal("SBRXM");
    }
    public void setPcsdm(String aPcsdm)
    {
        this.setInternal("PCSDM",aPcsdm);
    }
    public String getPcsdm()
    {
        return (String) this.getInternal("PCSDM");
    }
    public void setFjdm(String aFjdm)
    {
        this.setInternal("FJDM",aFjdm);
    }
    public String getFjdm()
    {
        return (String) this.getInternal("FJDM");
    }
    public void setSjdm(String aSjdm)
    {
        this.setInternal("SJDM",aSjdm);
    }
    public String getSjdm()
    {
        return (String) this.getInternal("SJDM");
    }
    public void setCzybh(String aCzybh)
    {
        this.setInternal("CZYBH",aCzybh);
    }
    public String getCzybh()
    {
        return (String) this.getInternal("CZYBH");
    }
    public void setCzyxm(String aCzyxm)
    {
        this.setInternal("CZYXM",aCzyxm);
    }
    public String getCzyxm()
    {
        return (String) this.getInternal("CZYXM");
    }
    public void setGdrbh(String aGdrbh)
    {
        this.setInternal("GDRBH",aGdrbh);
    }
    public String getGdrbh()
    {
        return (String) this.getInternal("GDRBH");
    }
    public void setGdrxm(String aGdrxm)
    {
        this.setInternal("GDRXM",aGdrxm);
    }
    public String getGdrxm()
    {
        return (String) this.getInternal("GDRXM");
    }
    public void setGdsj(Date aGdsj)
    {
        this.setInternal("GDSJ",aGdsj);
    }
    public Date getGdsj()
    {
        return (Date) this.getInternal("GDSJ");
    }
    public void setGddwbh(String aGddwbh)
    {
        this.setInternal("GDDWBH",aGddwbh);
    }
    public String getGddwbh()
    {
        return (String) this.getInternal("GDDWBH");
    }
    public void setSsxq(String aSsxq)
    {
        this.setInternal("SSXQ",aSsxq);
    }
    public String getSsxq()
    {
        return (String) this.getInternal("SSXQ");
    }

}