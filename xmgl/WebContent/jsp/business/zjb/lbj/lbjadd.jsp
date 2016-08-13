<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<app:base/>
<script type="text/javascript" charset="utf-8">
  var controllername= "${pageContext.request.contextPath }/zjb/lbj/lbjController.do";
  var tbl = null;
  $(function() {
    //获取父页面的值
      var rowValue =$($(window).manhuaDialog.getParentObj().document).find("#resultXML").val();
    var odd=convertJson.string2json1(rowValue);
    //将数据放入表单
      $("#demoForm").setFormValues(odd);
      init();
    //保存按钮
    var btn = $("#example1");
    btn.click(function()
      {
      id=$("#ID").val();
      if($("#demoForm").validationButton())
      {
        if(""!=$("#CSSDZ").val()&&""==$("#SBCSZ").val())
          {
          xAlert("警告","请输入提标价","3");
          return;
          }
        if(id==null||id==""){
          //生成json串
           var data = Form2Json.formToJSON(demoForm);
           //组成保存json串格式
           var data1 = defaultJson.packSaveJson(data);
           //调用ajax插入
           defaultJson.doInsertJson(controllername + "?insertLbj",data1, null,'addHuiDiao');
        }else{
          //生成json串
           var data = Form2Json.formToJSON(demoForm);
           //组成保存json串格式
           var data1 = defaultJson.packSaveJson(data);
           //调用ajax插入
           defaultJson.doUpdateJson(controllername + "?updateLbj",data1, null,'editHuiDiao');
        }
        }
        });
      //清空表单
    var btn1 = $("#example_clear");
    btn1.click(function() 
    {
      $("#demoForm").clearFormResult(); 
    });
  }
  );
  //自动计算审减值 审减百分比
  function jssjz()
  {
    var sbcsz=$("#SBCSZ").val();
    var cssdz=$("#CSSDZ").val();
    $("#SJZ").val(sbcsz-cssdz);
    var sjz=$("#SJZ").val();
    var chazhi=Math.round((Math.abs(sjz/sbcsz* 10000))) / 100.00;
    $("#SJBFB").val(chazhi);
  }
  //回调
  function addHuiDiao()
  {
    var data2 = $("#frmPost").find("#resultXML").val();
     var fuyemian=$(window).manhuaDialog.getParentObj();
      fuyemian.xiugaihang(data2);
      $(window).manhuaDialog.close();
  }
  function editHuiDiao()
  {
    var data2 = $("#frmPost").find("#resultXML").val();
     var fuyemian=$(window).manhuaDialog.getParentObj();
      fuyemian.xiugaihang(data2);
      $(window).manhuaDialog.close();
  }
  //判断是否需要送财审
  function sfxyscs() {
    $("input[name=ISXYSCS]").each(function(){
      if(this.checked==true){
        var aa=this.value;
        if (aa == '0') {
          $("#csTable").clearFormResult();
          $("#JGBCSRQ").attr({disabled : 'true'});
          $("#CZSWRQ").attr({disabled : 'true'});
          $("#CSBGBH").attr({disabled : 'true'});
         /*  $("#SBCSZ").attr({disabled : 'true'}); */
          $("#SBCSZRQ").attr({disabled : 'true'});
          $("#CSSDZ").attr({disabled : 'true'});
          $("#CSSDZRQ").attr({disabled : 'true'});
          $("#ZDJ").attr({disabled : 'true'});
          $("#CSSX option[value='3']").remove();
        } else {
          $("#JGBCSRQ").removeAttr("disabled");
          $("#CZSWRQ").removeAttr("disabled");
          $("#CSBGBH").removeAttr("disabled");
          /* $("#SBCSZ").removeAttr("disabled"); */
          $("#SBCSZRQ").removeAttr("disabled");
          $("#CSSDZ").removeAttr("disabled");
          $("#CSSDZRQ").removeAttr("disabled");
          $("#ZDJ").removeAttr("disabled");
          reloadSelectTableDic($("#CSSX"));
        }
      }
      
    });
  }
  function zjbzzt(){
    if("1"==$("#ZJBZZT").val()){
      $("#csTable").clearFormResult();
      $("#SBCSZ").val('');
      $("#JGBCSRQ").attr({disabled : 'true'});
      $("#CZSWRQ").attr({disabled : 'true'});
      $("#CSBGBH").attr({disabled : 'true'});
      $("#SBCSZ").attr({disabled : 'true'});
      $("#SBCSZRQ").attr({disabled : 'true'});
      $("#CSSDZ").attr({disabled : 'true'});
      $("#CSSDZRQ").attr({disabled : 'true'});
      $("#ZDJ").attr({disabled : 'true'});
      $("#CSSX").attr({disabled : 'true'});
      reloadSelectTableDic($("#CSSX"));
      $("input[name=ISXYSCS]").attr("checked",false);
      $("input[name=ISXYSCS]").attr("disabled",true);
    }else{
      $("#JGBCSRQ").removeAttr("disabled");
      $("#CZSWRQ").removeAttr("disabled");
      $("#CSBGBH").removeAttr("disabled");
      $("#SBCSZ").removeAttr("disabled");
      $("#SBCSZRQ").removeAttr("disabled");
      $("#CSSDZ").removeAttr("disabled");
      $("#CSSDZRQ").removeAttr("disabled");
      $("#ZDJ").removeAttr("disabled");
      $("#CSSX").removeAttr("disabled");
      $("input[name=ISXYSCS]").removeAttr("disabled",true);
    }
  }
  //默认否
  function init(){
    $("input[name=ISQTBMFZ]").each(function()
       {
        if(this.value=='0')
        {
        this.checked=true;
        }
    });  
  }
</script>   
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
 <div class="row-fluid">
 <div class="B-small-from-table-autoConcise">
  <h4 class="title">项目信息
    <span class="pull-right">
         <button id="example1" class="btn"  type="button">保存</button>
        </span> 
  </h4>
 <form method="post" action="${pageContext.request.contextPath }/insertdemo.xhtml" id="demoForm"  >
          <table class="B-table"  width="100%">
      <TR style="display:none;">
      <!-- <TR> -->
      <TD><INPUT class="span12" type="text" name="ID" fieldname="GC_ZJB_LBJB_ID" id="ID" />
        </TD><TD>  <INPUT class="span12"  keep="true" type="text" id="JHSJID" name="JHSJID" fieldname="JHSJID" />
        </TD><TD>  <INPUT class="span12"  keep="true" type="text" id="XMSX" name="XMSX" fieldname="XMSX" />
        </TD><TD>  <INPUT class="span12"  keep="true" type="text" id="ND" name="ND" fieldname="ND" />
        </TD><TD>  <INPUT class="span12"  keep="true" type="text" id="XMID" name="XMID" fieldname="XMID" />
        </TD><TD>  <INPUT class="span12"  keep="true" type="text" id="BDID" name="BDID" fieldname="BDID" />
        </TD><TD>  <INPUT class="span12"  keep="true" type="text" id="PXH" name="PXH" fieldname="PXH" />
        </TD><TD>  <INPUT class="span12"  keep="true" type="text" id="SJWYBH" name="SJWYBH" fieldname="SJWYBH" />
        </TD>
      </TR>  
      <tr>
        <th width="10%" class="right-border bottom-border text-right disabledTh">项目名称</th>
        <td width="40%"   class="right-border bottom-border"><input keep="true" class="span12 xmmc" type="text" placeholder="必填" disabled data-toggle="modal" check-type="required" id="XMMC" name="XMMC" fieldname="XMMC"/></td>
        <th width="10%" class="right-border bottom-border text-right disabledTh">标段名称</th>
        <td width="40%"   class="bottom-border"><input class="span12" keep="true" type="text" placeholder="" id="BDMC" name="BDMC" fieldname="BDMC" check-type="" disabled/></td>
      </tr>
        <tr>
        <th width="8%" class="right-border bottom-border text-right disabledTh"> 概预算 </th>
        <td width="15%"   class="bottom-border"><input class="span12" style="width:65%;text-align:right" keep="true" type="text" placeholder="" id="GYS" name="GYS" fieldname="GYS"   disabled/>&nbsp;&nbsp;<b>(元)</b></td>
      
         <th class="right-border bottom-border text-right disabledTh">咨询公司</th>
           <td  class="bottom-border"   >
           <select class="span10 department " style="width: 80%" id="ZXGS" name = "ZXGS"  fieldname="ZXGS" kind="dic" src="T#GC_CJDW:GC_CJDW_ID:DWMC:DWLX='4' " >
                  </select>
                  <a href="javascript:void(0)" title="点击选择单位"><i id="lydwSelect" selObj="ZXGS" dwlx="4" class="icon-edit" onclick="selectCjdw('lydwSelect');" isLxSelect="1"></i></a>
                  </td>
      </tr>
      </table>
       <h4 class="title">造价前期条件</h4>
      <table class="B-table"  width="100%">
      <tr>
        <th width="16%" class="right-border bottom-border text-right">其他部门负责</th>
        <td width="17%" class="right-border bottom-border"><input class="span12" id="ISQTBMFZ" type="radio"    placeholder="" kind="dic" src="E#1=是:0=否" name = "ISQTBMFZ" fieldname="ISQTBMFZ" ></td>
      	<td  colspan="5"></td>
      </tr>
      <tr>
        <th width="16%" class="right-border bottom-border text-right">与总工办交接图纸</th>
        <td width="17%" class="right-border bottom-border"><input class="span12 date" type="date" placeholder="" id="TZJJSJ" name="TZJJSJ" fieldname="TZJJSJ" check-type="" maxlength=""></td>
          <th width="16%" class="right-border bottom-border text-right">发给咨询公司图纸</th>
        <td width="17%" class="bottom-border"><input class="span12 date" type="date" placeholder="" id="ZXGSJ" name="ZXGSJ" fieldname="ZXGSJ"  check-type="" maxlength=""></td>            
         <th width="16%" class="right-border bottom-border text-right" colspan="2">咨询公司交造价组</th>
        <td width="17%" class="right-border bottom-border"><input class="span12 date" type="date" placeholder="" id="ZXGSRQ" name="ZXGSRQ" fieldname="ZXGSRQ" check-type="" maxlength=""></td>
      </tr>
      <tr>
        <th class="right-border bottom-border text-right">编制造价</th>
             <td class=" bottom-border" >
          			<select class="span8"  id="ZJBZZT" name = "ZJBZZT"  fieldname="ZJBZZT" kind="dic" src="ZJTJ"  onchange="zjbzzt()">
                    </select>
              </td>
              <th class="right-border bottom-border text-right">是否需要送财审</th>
             <td class=" bottom-border text-center" ><input class="span12" id="ISXYSCS" type="radio"    placeholder="" kind="dic" src="E#1=是:0=否" name = "ISXYSCS" fieldname="ISXYSCS" onclick="sfxyscs()"></td>
        <td class=" bottom-border text-left" >
        	<select class="span12"  id="CSSX" name = "CSSX"  fieldname="CSSX" kind="dic" src="CSSX" >
            </select></td>
        <th  class="right-border bottom-border text-right">提报价</th>
        <td  class="right-border bottom-border"><input class="span12" style="width:65%;text-align:right"  type="number" onkeyup="jssjz()" placeholder="" id="SBCSZ" name="SBCSZ" fieldname="SBCSZ" check-type="maxlength" maxlength="17" min="0">&nbsp;&nbsp;<b>(元)</b></td>
        
      </tr>
      </table> 
      <h4 class="title">财审</h4>
      <table class="B-table" id="csTable"  width="100%">
      <tr>
        <th width="16%" class="right-border bottom-border text-right">建管报财审日期</th>
        <td width="17%" class="bottom-border"><input class="span12 date" type="date" placeholder="" id="JGBCSRQ" name="JGBCSRQ" fieldname="JGBCSRQ" check-type="" maxlength=""></td>
        <th width="16%" class="right-border bottom-border text-right">财审审完日期</th>
        <td width="17%" class="bottom-border"><input class="span12 date" type="date" placeholder="" id="CZSWRQ" name="CZSWRQ" fieldname="CZSWRQ" check-type="" maxlength=""></td>
          <th width="16%" class="right-border bottom-border text-right">财审报告编号</th>
        <td width="17%" class="right-border bottom-border"><input class="span12" type="text" placeholder="" id="CSBGBH" name="CSBGBH" fieldname="CSBGBH" check-type="maxlength" maxlength="50"></td>
      </tr>
      <tr>
       <th  class="right-border bottom-border text-right">财审审定值</th>
        <td  class="right-border bottom-border"><input class="span12" style="width:65%;text-align:right" type="number" onkeyup="jssjz()"  placeholder="" id="CSSDZ" name="CSSDZ" fieldname="CSSDZ" check-type="maxlength" maxlength="17" min="0">&nbsp;&nbsp;<b>(元)</b></td>
        <th  class="right-border bottom-border text-right">审减值</th>
        <td class="bottom-border"><input class="span12" style="width:65%;text-align:right" type="number" disabled placeholder="" id="SJZ" name="SJZ" fieldname="SJZ" check-type="maxlength" maxlength="17" >&nbsp;&nbsp;<b>(元)</b></td>
       <th  class="right-border bottom-border text-right">审减百分比</th>
        <td  class="right-border bottom-border"><input class="span12" style="width:65%;text-align:right" type="number" disabled placeholder="" id="SJBFB" name="SJBFB" fieldname="SJBFB" check-type="maxlength" maxlength="17"  >&nbsp;&nbsp;<b>(%)</b></td>
      </tr>
      <tr>
        <th  class="right-border bottom-border text-right">暂定金</th>
        <td  class="bottom-border"><input class="span12" style="width:65%;text-align:right" type="number"  placeholder="" id="ZDJ" name="ZDJ" fieldname="ZDJ" check-type="maxlength" maxlength="17" min="0">&nbsp;&nbsp;<b>(元)</b></td>
          <td  class="right-border bottom-border" colspan="4">
      </tr>
      </table>
      <h4 ></h4>
      <table class="B-table"  width="100%">
      <tr>
        <th width="10%" class="right-border bottom-border text-right">备注</th>
        <td width="90%" colspan="7" class="bottom-border"><textarea class="span12" rows="3" id="BZ" name="BZ" fieldname="BZ" check-type="maxlength" maxlength="500"></textarea></td>
      </tr>
    </table>
          </form>
       </div>
   </div>
</div>
   <jsp:include page="/jsp/file_upload/fileupload_config.jsp" flush="true"/>
 <div align="center">
   <FORM name="frmPost" method="post" style="display:none" target="_blank" id ="frmPost">
     <!--系统保留定义区域-->
       <input type="hidden" name="ywid" id = "ywid" value="">
         <input type="hidden" name="queryXML" id = "queryXML">
         <input type="hidden" name="txtXML" id = "txtXML">
         <input type="hidden" name="txtFilter"  order="desc" fieldname = "XMNF"  id = "txtFilter">
         <input type="hidden" name="resultXML" id = "resultXML">
            <!--传递行数据用的隐藏域-->
         <input type="hidden" name="rowData">
    
   </FORM>
 </div>

</body>
</html>