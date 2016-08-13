package com.ccthanking.business.lcgl;
import com.ccthanking.framework.base.BaseVO;
import java.util.Date;

public class TacFlowapplyVO extends BaseVO{

	public TacFlowapplyVO(){
		this.addField("FLAID",OP_STRING);
		this.addField("FLAFLWID",OP_STRING);
		this.addField("FLANO",OP_STRING);
		this.addField("FLATITLE",OP_STRING);
		this.addField("FLAAEMPNAME",OP_STRING);
		this.addField("FLAADATE",OP_DATE);
		this.addField("FLAFDATE",OP_DATE);
		this.addField("FLASTATUS",OP_STRING);
		this.addField("FLAAEMPID",OP_STRING);
		this.addField("FLARESULT",OP_STRING);
		this.addField("FLAFSYSTEM",OP_STRING);
		this.addField("FLAFKIND",OP_STRING);
		this.addField("FLAFID",OP_STRING);
		this.addField("FLAADPTID",OP_STRING);
		this.addField("FLAADPTNAME",OP_STRING);
		this.addField("FLAPRESS",OP_STRING);
		this.addField("FLAAPRJID",OP_STRING);
		this.addField("FLAAPRJNAME",OP_STRING);
		this.addField("FLAAARGID",OP_STRING);
		this.addField("FLAAARGNAME",OP_STRING);
		this.addField("FLAFILESTATUS",OP_STRING);
		this.addField("FLATARGETDATE",OP_DATE);
		this.addField("FLACEMPNAME",OP_STRING);
		this.setFieldDateFormat("FLAADATE","yyyy-MM-dd");
		this.setFieldDateFormat("FLAFDATE","yyyy-MM-dd");
		this.setFieldDateFormat("FLATARGETDATE","yyyy-MM-dd");
		this.setVOTableName("TAC_FLOWAPPLY");
	}

	public void setFlaid(String flaid){
		this.setInternal("FLAID",flaid);
	}
	public String getFlaid(){
		return (String)this.getInternal("FLAID");
	}
	public void setFlaflwid(String flaflwid){
		this.setInternal("FLAFLWID",flaflwid);
	}
	public String getFlaflwid(){
		return (String)this.getInternal("FLAFLWID");
	}
	public void setFlano(String flano){
		this.setInternal("FLANO",flano);
	}
	public String getFlano(){
		return (String)this.getInternal("FLANO");
	}
	public void setFlatitle(String flatitle){
		this.setInternal("FLATITLE",flatitle);
	}
	public String getFlatitle(){
		return (String)this.getInternal("FLATITLE");
	}
	public void setFlaaempname(String flaaempname){
		this.setInternal("FLAAEMPNAME",flaaempname);
	}
	public String getFlaaempname(){
		return (String)this.getInternal("FLAAEMPNAME");
	}
	public void setFlaadate(Date flaadate){
		this.setInternal("FLAADATE",flaadate);
	}
	public Date getFlaadate(){
		return (Date)this.getInternal("FLAADATE");
	}
	public void setFlafdate(Date flafdate){
		this.setInternal("FLAFDATE",flafdate);
	}
	public Date getFlafdate(){
		return (Date)this.getInternal("FLAFDATE");
	}
	public void setFlastatus(String flastatus){
		this.setInternal("FLASTATUS",flastatus);
	}
	public String getFlastatus(){
		return (String)this.getInternal("FLASTATUS");
	}
	public void setFlaaempid(String flaaempid){
		this.setInternal("FLAAEMPID",flaaempid);
	}
	public String getFlaaempid(){
		return (String)this.getInternal("FLAAEMPID");
	}
	public void setFlaresult(String flaresult){
		this.setInternal("FLARESULT",flaresult);
	}
	public String getFlaresult(){
		return (String)this.getInternal("FLARESULT");
	}
	public void setFlafsystem(String flafsystem){
		this.setInternal("FLAFSYSTEM",flafsystem);
	}
	public String getFlafsystem(){
		return (String)this.getInternal("FLAFSYSTEM");
	}
	public void setFlafkind(String flafkind){
		this.setInternal("FLAFKIND",flafkind);
	}
	public String getFlafkind(){
		return (String)this.getInternal("FLAFKIND");
	}
	public void setFlafid(String flafid){
		this.setInternal("FLAFID",flafid);
	}
	public String getFlafid(){
		return (String)this.getInternal("FLAFID");
	}
	public void setFlaadptid(String flaadptid){
		this.setInternal("FLAADPTID",flaadptid);
	}
	public String getFlaadptid(){
		return (String)this.getInternal("FLAADPTID");
	}
	public void setFlaadptname(String flaadptname){
		this.setInternal("FLAADPTNAME",flaadptname);
	}
	public String getFlaadptname(){
		return (String)this.getInternal("FLAADPTNAME");
	}
	public void setFlapress(String flapress){
		this.setInternal("FLAPRESS",flapress);
	}
	public String getFlapress(){
		return (String)this.getInternal("FLAPRESS");
	}
	public void setFlaaprjid(String flaaprjid){
		this.setInternal("FLAAPRJID",flaaprjid);
	}
	public String getFlaaprjid(){
		return (String)this.getInternal("FLAAPRJID");
	}
	public void setFlaaprjname(String flaaprjname){
		this.setInternal("FLAAPRJNAME",flaaprjname);
	}
	public String getFlaaprjname(){
		return (String)this.getInternal("FLAAPRJNAME");
	}
	public void setFlaaargid(String flaaargid){
		this.setInternal("FLAAARGID",flaaargid);
	}
	public String getFlaaargid(){
		return (String)this.getInternal("FLAAARGID");
	}
	public void setFlaaargname(String flaaargname){
		this.setInternal("FLAAARGNAME",flaaargname);
	}
	public String getFlaaargname(){
		return (String)this.getInternal("FLAAARGNAME");
	}
	public void setFlafilestatus(String flafilestatus){
		this.setInternal("FLAFILESTATUS",flafilestatus);
	}
	public String getFlafilestatus(){
		return (String)this.getInternal("FLAFILESTATUS");
	}
	public void setFlatargetdate(Date flatargetdate){
		this.setInternal("FLATARGETDATE",flatargetdate);
	}
	public Date getFlatargetdate(){
		return (Date)this.getInternal("FLATARGETDATE");
	}
	public void setFlacempname(String flacempname){
		this.setInternal("FLACEMPNAME",flacempname);
	}
	public String getFlacempname(){
		return (String)this.getInternal("FLACEMPNAME");
	}
}