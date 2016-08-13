<%@ page contentType="text/html;charset=utf-8"%>
<%@ page import="com.ccthanking.framework.Globals"%>
<%@ page import="com.ccthanking.framework.common.*"%>
<%@ page import="com.ccthanking.common.vo.ApWsTypzVO"%>
<%@ page import="com.ccthanking.framework.util.*"%>
<%@ page import="org.dom4j.Document"%>
<%@ page import="org.dom4j.*"%>
<%@ page import="java.sql.Connection"%>
<%@ page import="java.util.*"%>
<%@ page import="com.ccthanking.framework.coreapp.aplink.Process"%>
<%@ page import="com.ccthanking.framework.coreapp.aplink.ProcessTypeMgr"%>
<%@ page import="com.ccthanking.framework.coreapp.aplink.TaskMgrBean"%>
<%@ page import="com.ccthanking.framework.coreapp.aplink.Step "%>
<%@ page import="com.ccthanking.framework.coreapp.orgmanage.*"%>
<%
    User user = (User)session.getAttribute(Globals.USER_KEY);
	OrgDept dept = user.getOrgDept();
	String userid = user.getAccount();
	String deptid = user.getDepartment();
    String deptName = dept.getDeptName();
    String userName = user.getName();
	String parentDeptId="";
	String parentName="";
	String wsname="";
	String username="";

	String sysdate=Pub.getDate("yyyy-MM-dd HH:mm:ss",Pub.getCurrentDate());
	if (dept != null && dept.getParent() != null)
	{
		parentDeptId = dept.getDeptID();
		parentName =dept.getDeptName();
	}
	else
	{
		parentDeptId=deptid;
		parentName = dept.getDeptName();
	}
    String ajbh = (String)request.getParameter("ajbh");
    String ajmc = (String)request.getParameter("ajmc");
    String slbh = (String)request.getParameter("slbh");
    String sjbh = (String)request.getParameter("sjbh");
    String wsid=(String)request.getParameter("wsid");
    String ywlx= (String)request.getParameter("ywlx");
    String titlemc = (String)request.getParameter("title");
    if(ajmc==null)ajmc=titlemc;

    String condition=request.getParameter("condition");

    String rwlx=Pub.getDictValueByCode("YWLX",ywlx);
    username=UserManager.getInstance().getUserByLoginName(userid).getName();
    TaskMgrBean taskBean = new TaskMgrBean();
     boolean isTs=false;
    try
    {

       ApWsTypzVO vo =com.ccthanking.framework.spflow.WsConfigManager.getDefaultConfig(ywlx,user);
  
       if (vo != null)
       {

       String proceType = null;
       String[][] results = DBUtil.querySql("SELECT PROCESSTYPE FROM ap_processtype WHERE OPERATIONOID=?",new Object[]{vo.getOperationoid()});
       if(results !=null && results.length>0)
       {
       		proceType = results[0][0];
       }
       if(proceType != null &&proceType.equals("4"))
       {
           isTs=true;
       }
       }
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
<%
response.setHeader("Pragma","No-cache");
response.setHeader("Cache-Control","no-cache");
response.setDateHeader("Expires", 0);
%>

<head>
<title></title>
<%@ taglib uri="/tld/base.tld" prefix="app"%>
<app:base />

</head>

<SCRIPT language="JavaScript">
<!--
//action变量定义
var strAction=g_sAppName+"/SPAction.do";//审批
var p_ajbh="<%=ajbh%>";
var p_slbh="<%=slbh%>";
var p_sjbh="<%=sjbh%>";
var p_deptid="<%=parentDeptId%>";
var p_deptname="<%=parentName%>";
var p_ywlx="<%=ywlx%>";

var controllername= "${pageContext.request.contextPath }/TaskQuery.do";
$(function() {
	var btn = $("#queryBtn");
	btn.click(function() {
        //生成json串

	});
});
function cmdOk(){
  var spUser = document.all('spr');
  var hsSelect = "";
  if(spUser){
     for(var  i=0;i<spUser.length;i++){
           if(spUser[i].selected==true)
           {
             hsSelect = spUser[i].value;
             break;
           }
     }
   }
 // alert('子页面：'+hsSelect);
   if(hsSelect.length>0){
	/*
		*	传值方法一
		*/
	  // $(window).manhuaDialog.getParentObj().getCqspRes(hsSelect);
				/*
		*	传值方法二(默认使用)
		*/
	 //  	$(window).manhuaDialog.setData(hsSelect);
	//	$(window).manhuaDialog.sendData();
	//	$(window).manhuaDialog.close(); 
					/*
		*	传值方法三
		*/

		$(window).manhuaDialog.getParentObj().submitSP(hsSelect);
        $(window).manhuaDialog.close();
   }else{
       xAlert("请选择一个审批人！");
	   $(window).manhuaDialog.close();
   }

}
function submitSP(selectRY)
{
	alert(selectRY);
}
//关闭
function docancel()
{
	$(window).manhuaDialog.close();

}
function onCancel(){
   window.returnValue="";
   window.close();

}

	$(function() {
		var btn = $("#tssp");
		btn.click(function() {
			var spUser = $("#sespr").val();
			var spDw = $("#DEPARTMENT").val();
			var hsSelect  = spUser;
			//window.returnValue=hsSelect;
			//window.close();
			//var action = strAction+"?method=StartFreeSP&dbr="+spUser+"&dbdw="+spDw+"&sjbh="+p_sjbh+"&ajbh="+p_ajbh+"&ywlx="+p_ywlx;
			//if(doGlobalPost(action))
		
			if (hsSelect.length > 0) {
				$(window).manhuaDialog.getParentObj().submitSPTS(hsSelect,spDw);
		        $(window).manhuaDialog.close();
			} else {
				 xAlert("请选择一个审批人！");
				 $(window).manhuaDialog.close();
			}
		});
		 //监听变化事件
	    $("#DEPARTMENT").change(function() {
	    	generatePc($("#sespr"));
	    }); 
	});
	//重置编辑录入区域
	function doReset() {
		resetValueByPages([ 1 ]);
		clearTxtXML();
	}
	//查询
	function doSearch(n) {
		var i = g_xTabPane.selectedIndex;
		if ((i == 0 && doQuery(i))) {
			processbar.style.display = "";
			document.getElementById('DEPARTMENT').value = p_deptid;

			document.frmPost.action = strAction + "?method=queryList&deptid="
					+ p_deptid;
			if (doGlobalQuery(document.frmPost.action, 0, 0, 0, 10)) {
				g_xTabPane.setSelectedIndex(0);
			}
			processbar.style.display = "none";
		}
	}

   
    
 	 //人员查询
    function generatePc(ndObj){
 		
    	ndObj.attr("src", "T#FS_ORG_PERSON:ACCOUNT:NAME:DEPARTMENT = "+$("#DEPARTMENT").val()+ " AND FLAG=1 ");
		ndObj.attr("kind", "dic");
		ndObj.html('');
    	reloadSelectTableDic(ndObj);
    }
	//-->
//-->
</SCRIPT>
<body leftmargin="10" topmargin="0">

<% if(isTs){ %>

	<!-- alter by cbl start -->
	<div class="container-fluid">
		</p>
		<div class="row-fluid">
			<div class="B-small-from-table-autoConcise">

				<h4 class="title">特送审批
				<span class="pull-right">
          			<button id="tssp" name="tssp" class="btn"  type="button" data-toggle="modal">提交</button>
          		</span>
          	</h4>
				<form method="post" id="tsForm">
					<table class="B-table" width="100%">
						<tr>
							<th width="20%" class="right-border bottom-border text-right">主办人姓名</th>
							<td width="30%" colspan="3"  class="right-border bottom-border"><input class="span12" type="text" placeholder="" id="username" name="username" fieldname="username" check-type="" value=<%=username%> readonly></td>
							<th width="20%" class="right-border bottom-border text-right">提请日期</th>
							<td width="30%" class="left-border bottom-border"><input class="span12" type="date" id="tqrq" name="tqrq" fieldname="tqrq" check-type="" value = <%=sysdate %> readonly></td>
						</tr>
						<tr>
							<th width="20%" class="right-border bottom-border text-right">办理部门:</th>
							<td width="30%" colspan="3"  class="right-border bottom-border">
								<select class="span12" type="text" placeholder="必填" check-type="required" id="DEPARTMENT" fieldname="DEPARTMENT" name="DEPARTMENT" kind="dic"
										src="T#VIEW_YW_ORG_DEPT:ROW_ID:DEPT_NAME" ></select>
							</td>
							<th width="20%" class="right-border bottom-border text-right">办理人:</th>
							<td width="30%" class="left-border bottom-border">
								<select class="span12"  type="text" placeholder="必填" check-type="required" id="sespr" fieldname="sespr" name="sespr" kind = "dic" src ="T#FS_ORG_PERSON:ACCOUNT:NAME:FLAG = 1"></select>
							</td>
						</tr>
					</table>
				</form>
			</div>
		</div>
	</div>

	<!-- //alter by cbl end -->

    

<%
}
else
{
 Collection set =  null;
      set = com.ccthanking.framework.spflow.WsConfigManager.getOperationSteps(ywlx,user,condition);
      if(set == null)
      {
    	set =  taskBean.getProcess(ywlx,user);
      }
      int length = set.size();
      Iterator itor = set.iterator();
 %>
	<div class="container-fluid">
	<div class="windowOpen-page-header">
    	<h4>审批流程图例
			<span class="pull-right">
				<button class="Btn" onclick="cmdOk();">提交</button>
			</span>
    	</h4>
    </div>
    <div class="row-fluid">
		<div class="span12">
        	<div class="windowOpenAuto">
	<div class="ProcessBox">
	<div class="ProcessSuccess Process"><h5><%=rwlx%></h5>
        提请单位：<%=deptName%><BR/>
        提请 人：<%=userName%><BR/>
	</div>
        <%
        int t =0;
        String[][] firstSpRolw = null;
        while(itor.hasNext()){
          //处理用户单位编码范围的方法
        Step step = (Step)itor.next();
        String title=step.getName();
        //String spsj =Pub.getDate("YYYY-MM-DD HH24:MI:SS", step.getProcessTime());
        String spRole = step.getRole();
        String spLevel = String.valueOf(step.getDeptLevel());
        int spJb = step.getDeptLevel();
        String sj = dept.getDeptID();
        String bz = "";
        String vsql = "";
        String dept_id = request.getParameter("deptid");
          if(dept_id!=null)
            sj = dept_id;

        if(t==0)
        {
        	   //String sql = "select a.PERSON_ACCOUNT,b.NAME from org_role_psn_map a,ORG_PERSON b where ROLE_NAME='"+spRole+"' and b.LEVEL_NAME = '"+spLevel+"' and a.PERSON_ACCOUNT = b.ACCOUNT and b.DEPARTMENT :deptid2 ";
        	   String sql = "select a.PERSON_ACCOUNT,b.NAME from fs_org_role_psn_map a,FS_ORG_PERSON b ,fs_org_role c where c.NAME='"+spRole+"' and a.role_id = c.role_id and a.PERSON_ACCOUNT = b.ACCOUNT and b.DEPARTMENT :deptid2 ";
        	   if("0".equals(bz)){
        		   sql =sql.replaceAll(":deptid2","like '"+sj+"%'");
        	   }else{
        		   sql =sql.replaceAll(":deptid2","in ('"+sj+"')");
        	   }
        	   firstSpRolw= DBUtil.querySql(sql);
        	   
        }
         
        if(spRole==null){
        	spRole = "";
        }
        String level  = Pub.getDictValueByCode("BMJB",String.valueOf(step.getDeptLevel()));
        if(level==null){
        	level = "";
        }
        String fillcolor = "yellow";
        if("0".equals(String.valueOf(step.getResult()))){
        }else{
        	fillcolor = "green";
        }
        %>
    <div class="next">下一步 <i class="icon-hand-right"></i></div>
	<div class="ProcessInfo Process"><h5><%=title%></h5>
	        审批角色：<%=spRole%><BR/>
	        审批级别：<%=level%><BR/>
        </div>
        <% 
        t++;
        }
        %>
    </div>
     <%if (firstSpRolw!=null){
        %>
        <div align = "center">
        <span>请选择审批人</span>
        <br/>
        <select style="width:100px;" id="spr">
         <%for (int i =0;i<firstSpRolw.length;i++){
          %>
           <option value="<%=firstSpRolw[i][0]%>"><%=firstSpRolw[i][1]%></option>
         <%}%>
        </select>
        </div>
        <%
         }
     	%>
           </div>
           </div>
       </div>
       </div>
    <%}%>
		<%
     }
    catch(Exception E)
    {
        System.out.println(E);
    }
    finally
     {
     }
	%>
    <div align="center">
 	<FORM name="frmPost" method="post" style="display:none" target="_blank">
		 <!--系统保留定义区域-->
         <input type="hidden" name="queryXML">
         <input type="hidden" name="txtXML">
         <input type="hidden" name="txtFilter">
         <input type="hidden" name="resultXML">
       		 <!--传递行数据用的隐藏域-->
         <input type="hidden" name="rowData">
	</FORM>
</div>
</body>
</html>
