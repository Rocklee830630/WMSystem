package com.ccthanking.business.tcjh.jhtz;
import com.ccthanking.framework.base.BaseVO;

public class JhtzVO extends BaseVO{

	public JhtzVO(){
		this.addField("JHTZ_ND",OP_STRING|TP_PK);
		this.addField("JHTZ_NR1",OP_STRING);
		this.addField("JHTZ_NR2",OP_STRING);
		this.setVOTableName("GC_JHTZ");
	}

	void setJhtz_nd(String jhtz_nd){
		this.setInternal("JHTZ_ND",jhtz_nd);
	}
	public String getJhtz_nd(){
		return (String)this.getInternal("JHTZ_ND");
	}
	public void setJhtz_nr1(String jhtz_nr1){
		this.setInternal("JHTZ_NR1",jhtz_nr1);
	}
	public String getJhtz_nr1(){
		return (String)this.getInternal("JHTZ_NR1");
	}
	public void setJhtz_nr2(String jhtz_nr2){
		this.setInternal("JHTZ_NR2",jhtz_nr2);
	}
	public String getJhtz_nr2(){
		return (String)this.getInternal("JHTZ_NR2");
	}
}