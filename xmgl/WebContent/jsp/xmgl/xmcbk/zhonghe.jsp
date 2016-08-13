<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ page import="com.ccthanking.framework.common.User"%>
<%@ page import="com.ccthanking.framework.common.OrgDept"%>
<%@ page import="com.ccthanking.framework.Globals"%>
<%@ page import="com.ccthanking.framework.util.Pub"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>项目储备库综合页面</title>
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
            <h3>项目储备库 <small>综合信息页</small></h3>
        </div>
        <div class="well"> 德国足球甲级联赛是德国足球最高等级的赛事类别，由德国足球协会于1962年7月28日在多特蒙德确立，自1963-64赛季面世。作为德国足球联赛系统的组成部分，德甲的每支球队均需与同级别的全部其它球队进行主客场制对赛，最终的德国足球冠军可获得欧洲冠军联赛的参赛资格；而排名最末的两支球队将降级至德国足球乙级联赛。排名倒数第三的球队则需要与德乙第三名进行保级附加赛，胜者可获准留在德甲。此外，所有德甲球队都可直接入围德国足协杯比赛，两者冠军将参加德国超级杯的争夺。作为欧洲五大联赛之一，德甲在欧洲足联的联赛系数排名中目前位居全欧第三。德甲也是全球平均上座率最高的足球联赛，其在2011-12赛季以场均45,134人的现场观战人数在全球所有体育联盟中排名第二，同时还在全球208个国家和地区进行电视转播。拜仁慕尼黑是德甲最为成功的球队，共获得22次德国冠军。2012-13赛季德国足球甲级联赛为第50届赛事，于2012年8月24日至2013年5月18日举行，由拜仁慕尼黑以25分的优势夺得冠军。 </div>
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
                    <tr>
                        <td>6</td>
                        <td>JC000006</td>
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
                        <td>7</td>
                        <td>JC000007</td>
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
                        <td>8</td>
                        <td>JC000008</td>
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
                        <td>9</td>
                        <td>JC000009</td>
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
                        <td>10</td>
                        <td>JC000010</td>
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
		  <div class="btn-group pull-right">
				  <button class="btn btn-primary">项目维护</button>
				  <button class="btn btn-primary">项目下达</button>
				  <button class="btn btn-primary">项目截转</button>
				</div>
		</div>
    </div>
</div>

</body>
</html>