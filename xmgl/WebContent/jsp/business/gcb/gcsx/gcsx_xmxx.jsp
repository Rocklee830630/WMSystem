<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<%@ page import="com.ccthanking.framework.common.User"%>
<%@ page import="com.ccthanking.framework.common.OrgDept"%>
<%@ page import="com.ccthanking.framework.Globals"%>
<%@ page import="com.ccthanking.framework.util.Pub"%>
<app:base/>
<title>工程甩项-维护</title>
<script type="text/javascript" charset="utf-8">
//请求路径，对应后台RequestMapping
var controllername= "${pageContext.request.contextPath }/gcsx/gcsxController.do";
//页面初始化
$(function() {
	init();
	
});
//页面默认参数
function init(){
		var pwindow =$(window).manhuaDialog.getParentObj();
		var rowValue = pwindow.document.getElementById("resultXML").value;
		var obj = convertJson.string2json1(rowValue);
		deleteFileData(obj.GC_GCGL_GCSX_ID,"",obj.SJBH,obj.YWLX);
		setFileData(obj.GC_GCGL_GCSX_ID,"",obj.SJBH,obj.YWLX,"0");
		queryFileData(obj.GC_GCGL_GCSX_ID,"",obj.SJBH,obj.YWLX);
		$("#gcsxForm").setFormValues(obj);
}
</script>      
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
<div class="row-fluid">
    <div class="B-small-from-table-autoConcise">
      <h4 class="title">工程甩项信息
      	<span class="pull-right">
  		</span>
      </h4>
     <form method="post" id="gcsxForm"  >
      <table class="B-table" width="100%" id="gcsxList">
      <input type="hidden" id="GC_GCGL_GCSX_ID" fieldname="GC_GCGL_GCSX_ID" name = "GC_GCGL_GCSX_ID"/>
      <input type="hidden" id="XMID" fieldname="XMID" name = XMID/>
      <input type="hidden" id="BDID" fieldname="BDID" name = BDID/>
      <input type="hidden" id="JHSJID" fieldname="JHSJID" name = JHSJID/>
    	<!--  add by cbl start -->
    	<input type="hidden" id="SJBH" fieldname="SJBH" name = SJBH/>
      	<input type="hidden" id="YWLX" fieldname="YWLX" name = YWLX/>
      	<input type="hidden" id="FQLX" fieldname="FQLX" name = FQLX/>
      <!--  add by cbl end -->
        <tr>
          <th width="8%" class="right-border bottom-border text-right disabledTh">项目名称</th>
          <td class="right-border bottom-border" width="30%">
          	<input class="span12" id="XMMC" type="text" placeholder="必填" check-type="required" fieldname="XMMC" name = "XMMC"  readonly />
          </td>
          <th width="8%" class="right-border bottom-border text-right disabledTh">标段名称</th>
          <td class="right-border bottom-border" width="30">
          	<input class="span12" style="width:70%" id="BDMC" type="text" fieldname="BDMC" name = "BDMC"  readonly />
         </td>
         <th width="8%" class="right-border bottom-border text-right disabledTh">项目年度</th>
         <td class="right-border bottom-border" width="16%">
          	<input class="span12" id="ND" type="text" fieldname="ND" name = "ND"  readonly />
         </td>
        </tr>
        <tr>
          <th width="8%" class="right-border bottom-border text-right disabledTh">甩项名称</th>
          <td class="right-border bottom-border" width="26%">
           	<input class="span12" readonly check-type="required" maxlength="200" id="SXMC" type="text" fieldname="SXMC" name = "SXMC" />
          </td>
          <th width="8%" class="right-border bottom-border text-right disabledTh">工程部业主代表</th>
          <td class="right-border bottom-border" width="26%">
          	<select class="span12 person"  readonly id="GCBYZDB" kind="dic" src="T#VIEW_YW_ORG_PERSON:ACCOUNT:NAME:PERSON_KIND = '3' and DEPARTMENT in (select row_id from VIEW_YW_ORG_DEPT where EXTEND1='2')" fieldname="GCBYZDB" name="GCBYZDB">
	        </select>
          </td>
          <th width="8%" class="right-border bottom-border text-right disabledTh">联系方式</th>
          <td class="right-border bottom-border" width="26%">
           	<input class="span12" id="GCBYZDBLXFS"  readonly type="text" fieldname="GCBYZDBLXFS" name = "GCBYZDBLXFS" />
          </td>
          
        </tr>
        <tr>
        	<th width="8%" class="right-border bottom-border text-right disabledTh" >项目管理公司</th>
          	<td class="right-border bottom-border">
	           	<select class="span12 4characters"  readonly id="XMGLGS" kind="dic" src="T#VIEW_YW_XMGLGS:ROW_ID:BMJC" fieldname="XMGLGS" name="XMGLGS">
	     		</select>
          	</td>
          	<th width="8%" class="right-border bottom-border text-right disabledTh" >业主代表</th>
          	<td class="bottom-border right-border">
	            <input class="span12" type="text" id="XMGLGSYZDB" readonly name = "XMGLGSYZDB"  fieldname = "XMGLGSYZDB"  />
          	</td>
          	<th width="8%" class="right-border bottom-border text-right disabledTh"  >联系方式</th>
          	<td class="bottom-border right-border">
	            <input class="span12" id="XMGLGSYZDBLXFS"  readonly type="text" fieldname="XMGLGSYZDBLXFS" name = "XMGLGSYZDBLXFS" />
          	</td>
        </tr>
        <tr>
          <th width="8%" class="right-border bottom-border text-right disabledTh">施工单位</th>
          <td class="right-border bottom-border" width="26%">
          	<input class="span12" id="SGDW" type="text" readonly fieldname="SGDW" name = "SGDW"  readonly />
          </td>
          <th width="8%" class="right-border bottom-border text-right disabledTh">项目经理</th>
          <td class="right-border bottom-border" width="26%">
           	<input class="span12" id="SGDWXMJL" type="text" readonly fieldname="SGDWXMJL" name = "SGDWXMJL"  readonly />
          </td>
          <th width="8%" class="right-border bottom-border text-right disabledTh">联系方式</th>
          <td class="right-border bottom-border" width="26%">
           	<input class="span12" id="SGDWXMJLLXDH" type="text" readonly fieldname="SGDWXMJLLXDH" name = "SGDWXMJLLXDH"  readonly />
          </td>
        </tr>
        <tr>
          <th width="8%" class="right-border bottom-border text-right disabledTh">监理单位</th>
          <td class="right-border bottom-border" width="26%">
          	<input class="span12" id="JLDW" type="text" fieldname="JLDW" name = "JLDW"  readonly />
          </td>
          <th width="8%" class="right-border bottom-border text-right disabledTh">项目总监</th>
          <td class="right-border bottom-border" width="26%">
           	<input class="span12" id="JLDWZJ" type="text" fieldname="JLDWZJ" name = "JLDWZJ"  readonly />
          </td>
          <th width="8%" class="right-border bottom-border text-right disabledTh">联系方式</th>
          <td class="right-border bottom-border" width="26%">
           	<input class="span12" id="JLDWZJLXDH" type="text" fieldname="JLDWZJLXDH" name = "JLDWZJLXDH"  readonly />
          </td>
        </tr>
        <tr>
          <th width="8%" class="right-border bottom-border text-right disabledTh">工程开工日期</th>
          <td class="right-border bottom-border">
          	<input class="span12 date" id="GCKGRQ" readonly type="date" name="GCKGRQ" fieldname="GCKGRQ"/>
          </td>
          <th width="8%" class="right-border bottom-border text-right disabledTh">甩项申请日期</th>
          <td class="right-border bottom-border">
          	<input class="span12 date" id="SXSQRQ" readonly type="date" name="SXSQRQ"  fieldname="SXSQRQ"/>
          </td>
          <th width="8%" class="right-border bottom-border text-right disabledTh">是否涉及拆迁</th>
	       <td class="right-border bottom-border">
	       	<select class="span12 3characters" id="SFSJPQ" kind="dic" src="SF" readonly noNullSelect ="true" fieldname="SFSJPQ" name="SFSJPQ">
		    </select>
	      </td>
        </tr>
        <tr>
          <th width="8%" class="right-border bottom-border text-right disabledTh">甩项申请内容</th>
          <td class="right-border bottom-border" colspan="6">
          	<textarea class="span12" id="SXSQNR" rows="4" name ="SXSQNR" readonly fieldname="SXSQNR" maxlength="1000"></textarea>
          </td>
        </tr>
        <tr>
	     	<th width="8%" class="right-border bottom-border text-right disabledTh">附件信息</th>
	     	<td colspan="6" class="bottom-border right-border">
				<div>
					<table role="presentation" class="table table-striped">
						<tbody fjlb="0070" class="files showFileTab" id="fileTab" onlyView="true"
							data-toggle="modal-gallery" data-target="#modal-gallery">
						</tbody>
					</table>
				</div>
			</td>
	     </tr>
      </table>
      </form>
      	    
    </div>
  </div>
</div>
<jsp:include page="/jsp/file_upload/fileupload_config.jsp" flush="true"/>
<div align="center">
	<FORM id="frmPost" name="frmPost" method="post" style="display:none" target="_blank">
 	<!--系统保留定义区域-->
       <input type="hidden" name="queryXML" id = "queryXML">
       <input type="hidden" name="txtXML" id = "txtXML">
       <input type="hidden" name="txtFilter"  order="desc" fieldname = "LRSJ"	id = "txtFilter">
       <input type="hidden" name="resultXML" id = "resultXML">
       <input type="hidden" name="ywid" id = "ywid" value="">
     		 <!--传递行数据用的隐藏域-->
        <input type="hidden" name="rowData">
	</FORM>
</div>
</body>
</html>