package com.ccthanking.common.vo;
import com.ccthanking.framework.base.BaseVO;

import java.util.Date;

public class SmsOutVO extends BaseVO{
  public SmsOutVO()
  {

                //设置字段信息
                  this.addField("mbno",this.OP_STRING);
                  this.addField("spno",this.OP_STRING);
                  this.addField("content",this.OP_STRING);


      this.setVOTableName("SmsOut");

  }

  public void setmbno(String mbno){
    this.setInternal("mbno" ,mbno );
  }

  public void setspno(String spno){
    this.setInternal("spno" ,spno );
  }

  public void setcontent(String content){
    this.setInternal("content" ,content );
  }

  public String getmbno(){
    return (String)this.getInternal("mbno");
  }


  public String getspno(){
    return (String)this.getInternal("spno");
  }


  public String getcontent(){
    return (String)this.getInternal("content");
  }


}
