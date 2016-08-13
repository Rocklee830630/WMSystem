<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>

<%@ page import="com.ccthanking.framework.common.User"%>
<%@ page import="com.ccthanking.framework.common.OrgDept"%>
<%@ page import="com.ccthanking.framework.Globals"%>
<%@ page import="com.ccthanking.framework.util.Pub"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>项目储备库下达维护</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<app:base/>
<script type="text/javascript" src="/xmgl/js/common/bootstrap-validation.js"> </script>
<script type="text/javascript" src="/xmgl/js/common/loadFields.js"> </script>
</head>
<body >
<%
  User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
  OrgDept dept = user.getOrgDept();
  String deptId = dept.getDeptID();
  String deptName = dept.getDept_Name();
  String userid = user.getAccount();
  String username = user.getName();
  String sysdate = Pub.getDate("yyyy-MM-dd HH:mm:ss");
%>

<script  type="text/javascript">




//$(document).ready(function(){
	// $('#inputEmail').validationField();
	
 // $("#dd").click(function()
//	{
	 
//	});
//  });

</script>   

<div class="container-fluid">
    <div class="page-header">
        <h3>信息采集</h3>
    </div>
    <div class="row-fluid">
      <div class="B-from-table-box">
        <h4>查询结果</h4>
        <div class="overFlowX">
              <table width="100%" class="table table-hover">
                  <thead>
                      <tr>
                          <th>序号</th>
                          <th>计财流水号</th>
                          <th>项目名称</th>
                          <th>项目状态</th>
                          <th>项目类别</th>
                          <th>应急、特殊项目标示</th>
                          <th>起止点</th>
                          <th>概算（万元）</th>
                          <th>建设必要性</th>
                          <th>建设意义</th>
                          <th>项目来源</th>
                          <th>备注</th>
                      </tr>
                  </thead>
                  <tbody>
                      <tr>
                          <td>1</td>
                          <td>JC000001</td>
                          <td>开运街桥</td>
                          <td>未下达</td>
                          <td>道路工程</td>
                          <td>应急、特殊项目</td>
                          <td>A到B</td>
                          <td>1,000</td>
                          <td>必需要建</td>
                          <td>建设意义</td>
                          <td>项目来源</td>
                          <td>备注</td>
                      </tr>
                      <tr>
                          <td>2</td>
                          <td>JC000002</td>
                          <td>开运街桥</td>
                          <td>未下达</td>
                          <td>道路工程</td>
                          <td>应急、特殊项目</td>
                          <td>A到B</td>
                          <td>1,000</td>
                          <td>必需要建</td>
                          <td>建设意义</td>
                          <td>项目来源</td>
                          <td>备注</td>
                      </tr>
                      <tr>
                          <td>3</td>
                          <td>JC000003</td>
                          <td>开运街桥</td>
                          <td>未下达</td>
                          <td>道路工程</td>
                          <td>应急、特殊项目</td>
                          <td>A到B</td>
                          <td>1,000</td>
                          <td>必需要建</td>
                          <td>建设意义</td>
                          <td>项目来源</td>
                          <td>备注</td>
                      </tr>
                      <tr>
                          <td>4</td>
                          <td>JC000004</td>
                          <td>开运街桥</td>
                          <td>未下达</td>
                          <td>道路工程</td>
                          <td>应急、特殊项目</td>
                          <td>A到B</td>
                          <td>1,000</td>
                          <td>必需要建</td>
                          <td>建设意义</td>
                          <td>项目来源</td>
                          <td>备注</td>
                      </tr>
                      <tr>
                          <td>5</td>
                          <td>JC000005</td>
                          <td>开运街桥</td>
                          <td>未下达</td>
                          <td>道路工程</td>
                          <td>应急、特殊项目</td>
                          <td>A到B</td>
                          <td>1,000</td>
                          <td>必需要建</td>
                          <td>建设意义</td>
                          <td>项目来源</td>
                          <td>备注</td>
                      </tr>
                  </tbody>
              </table>
          </div>
          <div class="pagination pagination-centered">
            <ul>
              <li><a href="#">上一页</a></li>
              <li><a href="#">1</a></li>
              <li><a href="#">2</a></li>
              <li><a href="#">3</a></li>
              <li><a href="#">4</a></li>
              <li><a href="#">5</a></li>
              <li><a href="#">上一页</a></li>
            </ul>
          </div>
      </div>
    </div>
    <div class="row-fluid">
    <div class="B-from-table-box">
    <form class="form-horizontal" id="myform">
      <h4>信息采集</h4>
      <table class="B-table">
        <tr>
          <th width="8%" class="right-border bottom-border">项目名称</th>
          <td width="42%" class="right-border bottom-border"> <input type="text" id="inputEmail" check-type="required" required > </td>
          <th width="8%" class="right-border bottom-border">项目年份</th>
          <td width="25%" class="bottom-border">
            <select class="span12">
              <option>全部</option>
              <option>2012</option>
              <option>2013</option>
              <option>2014</option>
              <option>2015</option>
            </select>
          </td>
          <td width="17%" class="text-left bottom-border">
            <label class="checkbox">
              应急、特殊项目 <input type="checkbox">
            </label>  
          </td>
        </tr>
        <tr>
          <th width="8%" class="right-border bottom-border">项目分类</th>
          <td width="42%" class="right-border bottom-border">
            <select class="span12">
              <option>全部</option>
              <option>2012</option>
              <option>2013</option>
              <option>2014</option>
              <option>2015</option>
            </select>
          </td>
          <th width="8%" class="right-border bottom-border">项目起止点</th>
          <td width="42%" colspan="2" class="bottom-border"><input class="span12"  type="text" placeholder="" ></td>
        </tr>
        <tr>
          <th width="8%" class="right-border bottom-border">概算</th>
          <td width="42%" class="right-border bottom-border"><input class="span12" type="text" placeholder=""></td>
          <th width="8%" class="right-border bottom-border">项目来源</th>
          <td width="42%" colspan="2" class="bottom-border"><input class="span12" type="text" placeholder=""></td>
        </tr>
        <tr>
          <th width="8%" class="right-border bottom-border">建设必要性</th>
          <td width="92%" colspan="4" class="bottom-border"><textarea class="span12" rows="3"></textarea></td>
        </tr>
        <tr>
          <th width="8%" class="right-border bottom-border">建设意义</th>
          <td width="92%" colspan="4" class="bottom-border"><textarea class="span12" rows="3"></textarea></td>
        </tr>
        <tr>
          <th width="8%" class="right-border bottom-border">备注</th>
          <td width="92%" colspan="4" class="bottom-border"><textarea class="span12" rows="3"></textarea></td>
        </tr>
        <tr>
          <td colspan="5" class="bottom-border"><button class="btn btn-primary" type="submit" id="insertButton" >新 增</button> <button class="btn btn-primary" type="button">复制新增</button> <button class="btn btn-primary" type="button">保 存</button></td>
        </tr>
      </table>
          </form>
    </div>
  </div>
</div>



</body>
</html>