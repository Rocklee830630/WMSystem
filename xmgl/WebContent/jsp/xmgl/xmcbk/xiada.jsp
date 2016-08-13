<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ page import="com.ccthanking.framework.common.User"%>
<%@ page import="com.ccthanking.framework.common.OrgDept"%>
<%@ page import="com.ccthanking.framework.Globals"%>
<%@ page import="com.ccthanking.framework.util.Pub"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>项目储备库下达页面</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link href="${pageContext.request.contextPath }/css/bootstrap.css" rel="stylesheet" media="screen">
<link href="${pageContext.request.contextPath }/css/base.css" rel="stylesheet" media="screen">
<script src="${pageContext.request.contextPath }/js/base/jquery.js"></script>
<script src="${pageContext.request.contextPath }/js/base/bootstrap.js"></script>
<script src="${pageContext.request.contextPath }/js/base/jquery-ui.js"></script>
<script src="${pageContext.request.contextPath }/js/base/jsBruce.js"></script>
</head>
<body>
<div class="container-fluid">
    <div class="row-fluid">
        <div class="page-header">
            <h3>项目储备库 <small>下达</small></h3>
        </div>
       
        <div class="page-header">
            <form class="form-inline">
            	<div class="input-prepend">
					<span class="add-on">项目年份：</span>
					<select class="span6">
						<option>2011</option>
						<option>2012</option>
						<option>2013</option>
						<option>2014</option>
						<option>2015</option>
					</select>　
				</div>　
				<div class="input-prepend">
					<span class="add-on">项目状态：</span>
					<select class="span6">
						<option>2011</option>
						<option>2012</option>
						<option>2013</option>
						<option>2014</option>
						<option>2015</option>
					</select>　
				</div>　
			  	<div class="input-prepend">
					<span class="add-on">项目分类：</span>
					<select class="span6">
						<option>2011</option>
						<option>2012</option>
						<option>2013</option>
						<option>2014</option>
						<option>2015</option>
					</select>　
				</div>　
				<div class="input-prepend">
					<span class="add-on">项目名称：</span>
					<input class="span8" id="appendedPrependedInput" type="text">
				</div> 　
				<label class="checkbox inline">
				  <input type="checkbox" id="inlineCheckbox1" value="option1"> 应急、特殊项目
				</label>
				<div class="btn-group pull-right">
				  <button class="btn btn-primary">查询</button>
				  <button class="btn btn-primary">导出EXCEL</button>
				</div>
			</form>
        </div>
        <div class="overFlowX">
            <table width="100%" class="table table-bordered table-hover">
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
        <div class="pagination">
		  <ul>
		    <li><a href="#">上一页</a></li>
		    <li class="active"><a href="#">1</a></li>
		    <li><a href="#">2</a></li>
		    <li><a href="#">3</a></li>
		    <li><a href="#">4</a></li>
		    <li><a href="#">5</a></li>
		    <li><a href="#">下一页</a></li>
		  </ul>
		  
		</div>
    </div>
    <div class="page-header">
    	<h4>要下达储备库项目列表</h4>

    </div>
    <div class="overFlowX">
            <table width="100%" class="table table-bordered table-hover">
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
        <p class="text-left"><button class="btn btn-large btn-primary" type="button">  下 达  </button></p>
        <div class="row-fluid">
    		<div class="input-prepend span6">
				<span class="add-on">批&nbsp;&nbsp;次&nbsp;&nbsp;号：</span>
				<input class="span10" placeholder="请输入批次号" id="appendedPrependedInput" type="text">
			</div>
			<div class="input-prepend span6">
				<span class="add-on">批次日期：</span>
				<input class="span10" placeholder="请输入批次日期"  id="appendedPrependedInput1" type="text">
			</div>
				&nbsp;&nbsp;附件上传： <textarea placeholder="指令、决议等" rows="5" class="span11"></textarea>
			
	    </div>
    </div>          
</div>
</body>
</html>