// JavaScript Document
$(document).ready(function(e) {
	//$('.LockingTableRoot > tbody').html('<tr><td align="center" width="10%"><strong>1</strong></td><td width="10%" align="center">a</td><td width="10%" align="right">2013</td><td width="10%">2013</td><td width="10%">2013-08-08</td><td width="10%"><i class="icon-ok"></i></td><td width="10%">cccdbbb</td><td width="10%">dddaa</td><td width="10%">sss</td><td width="10%">122,221</td></tr><tr><td align="center" width="10%"><strong>1</strong></td><td width="10%" align="center">a</td><td width="10%" align="right">2013</td><td width="10%">2013</td><td width="10%">2013-08-08</td><td width="10%"><i class="icon-ok"></i></td><td width="10%">cccdbbb</td><td width="10%">dddaa</td><td width="10%">sss</td><td width="10%">122,221</td></tr><tr><td align="center" width="10%"><strong>1</strong></td><td width="10%" align="center">a</td><td width="10%" align="right">2013</td><td width="10%">2013</td><td width="10%">2013-08-08</td><td width="10%"><i class="icon-ok"></i></td><td width="10%">cccdbbb</td><td width="10%">dddaa</td><td width="10%">sss</td><td width="10%">122,221</td></tr><tr><td align="center" width="10%"><strong>1</strong></td><td width="10%" align="center">a</td><td width="10%" align="right">2013</td><td width="10%">2013</td><td width="10%">2013-08-08</td><td width="10%"><i class="icon-ok"></i></td><td width="10%">cccdbbb</td><td width="10%">dddaa</td><td width="10%">sss</td><td width="10%">122,221</td></tr><tr><td align="center" width="10%"><strong>1</strong></td><td width="10%" align="center">a</td><td width="10%" align="right">2013</td><td width="10%">2013</td><td width="10%">2013-08-08</td><td width="10%"><i class="icon-ok"></i></td><td width="10%">cccdbbb</td><td width="10%">dddaa</td><td width="10%">sss</td><td width="10%">122,221</td></tr><tr><td align="center" width="10%"><strong>1</strong></td><td width="10%" align="center">a</td><td width="10%" align="right">2013</td><td width="10%">2013</td><td width="10%">2013-08-08</td><td width="10%"><i class="icon-ok"></i></td><td width="10%">cccdbbb</td><td width="10%">dddaa</td><td width="10%">sss</td><td width="10%">122,221</td></tr><tr><td align="center" width="10%"><strong>1</strong></td><td width="10%" align="center">a</td><td width="10%" align="right">2013</td><td width="10%">2013</td><td width="10%">2013-08-08</td><td width="10%"><i class="icon-ok"></i></td><td width="10%">cccdbbb</td><td width="10%">dddaa</td><td width="10%">sss</td><td width="10%">122,221</td></tr><tr><td align="center" width="10%"><strong>1</strong></td><td width="10%" align="center">a</td><td width="10%" align="right">2013</td><td width="10%">2013</td><td width="10%">2013-08-08</td><td width="10%"><i class="icon-ok"></i></td><td width="10%">cccdbbb</td><td width="10%">dddaa</td><td width="10%">sss</td><td width="10%">122,221</td></tr><tr><td align="center" width="10%"><strong>1</strong></td><td width="10%" align="center">a</td><td width="10%" align="right">2013</td><td width="10%">2013</td><td width="10%">2013-08-08</td><td width="10%"><i class="icon-ok"></i></td><td width="10%">cccdbbb</td><td width="10%">dddaa</td><td width="10%">sss</td><td width="10%">122,221</td></tr><tr><td align="center" width="10%"><strong>1</strong></td><td width="10%" align="center">a</td><td width="10%" align="right">2013</td><td width="10%">2013</td><td width="10%">2013-08-08</td><td width="10%"><i class="icon-ok"></i></td><td width="10%">cccdbbb</td><td width="10%">dddaa</td><td width="10%">sss</td><td width="10%">122,221</td></tr><tr><td align="center" width="10%"><strong>1</strong></td><td width="10%" align="center">a</td><td width="10%" align="right">2013</td><td width="10%">2013</td><td width="10%">2013-08-08</td><td width="10%"><i class="icon-ok"></i></td><td width="10%">cccdbbb</td><td width="10%">dddaa</td><td width="10%">sss</td><td width="10%">122,221</td></tr>');
	
	
	
	var LockingBoxTheadHeight = $('.LockingTableRoot').find('thead').innerHeight();
	var LockingBoxTheadWidth = $('.LockingTableRoot').innerWidth();
	//alert (LockingBoxTheadWidth);
	//alert (LockingBoxTheadHeight);
	var LockingBoxRowWidth = $('.LockingTableRoot').find('th').first().innerWidth();
	$('.LockingBoxThead').css('height',LockingBoxTheadHeight+1).css('width',LockingBoxTheadWidth);
	$('.LockingBoxRow').css('width',LockingBoxRowWidth+2).css('top',LockingBoxTheadHeight);
    $('.LockingTableRoot').clone().removeAttr('id').appendTo('.LockingBoxThead').clone().appendTo('.LockingBoxRow');
	$('.LockingBoxRow').find('thead').remove();
	$('.LockingBoxRow').find('table').removeAttr('width');
	$('.LockingBoxRow').find('table.LockingTableRoot tr > th,table.LockingTableRoot tr > td').first().css('width',(LockingBoxRowWidth+2));
	$('.LockingBoxRow').find('tr > th').nextAll().css('display','none');
	$('.LockingBoxRow').find('tr > td').nextAll().css('display','none');
	$('.LockingBox').scroll(function(e) {
        var scrollY = $(this).scrollTop();
        var scrollX = $(this).scrollLeft();
		$('.LockingBoxThead').css('top',scrollY);
		$('.LockingBoxRow').css('left',scrollX);
    });
	
	
	/*start 自定义锁定列*/
	var LockingUBoxTheadHeight = $('.LockingUTableRoot').find('thead').innerHeight();
	var LockingUBoxTheadWidth = $('.LockingUTableRoot').innerWidth();
	//alert (LockingUBoxTheadWidth);
	//alert (LockingUBoxTheadHeight);
	var FromDefinition = $('.LockingUTableRoot').find('th.definition,td.definition');
	var LockingUBoxRowWidth = $('.LockingUTableRoot').find('th.definition,td.definition').innerWidth();
	
	
	
	//var LockingUBoxRowWidth = $('.LockingUTableRoot').find('th').first().innerWidth();
	$('.LockingUBoxThead').css('height',LockingUBoxTheadHeight+1).css('width',LockingUBoxTheadWidth);
	$('.LockingUBoxRow').css('width',LockingUBoxRowWidth+2).css('top',LockingUBoxTheadHeight);
    $('.LockingUTableRoot').clone().removeAttr('id').appendTo('.LockingUBoxThead').clone().appendTo('.LockingUBoxRow');
	$('.LockingUBoxRow').find('thead').remove();
	//$('.LockingUBoxRow').find('table').removeAttr('width');
	$('.LockingUBoxRow').find('table.LockingUTableRoot tr > th,table.LockingUTableRoot tr > td').find('.definition').css('width',(LockingUBoxRowWidth+2));
	//$('.LockingUBoxRow').find('tr > th').nextAll().css('display','none');
	//$('.LockingUBoxRow').find('tr > td').nextAll().css('display','none');
	$('.LockingUBox').scroll(function(e) {
        var scrollY = $(this).scrollTop();
        var scrollX = $(this).scrollLeft();
		$('.LockingUBoxThead').css('top',scrollY);
		$('.LockingUBoxRow').css('left',scrollX);
    });
	var trLen = ($('.LockingUTableRoot').not('.LockingUBoxThead > table.LockingUTableRoot,.LockingUBoxRow > table.LockingUTableRoot').find('tbody tr').length);
	for (var i=0;i<trLen;i++) {
		var eqi = $('.LockingUTableRoot').not('.LockingUBoxThead > table.LockingUTableRoot,.LockingUBoxRow > table.LockingUTableRoot').find('tbody tr').eq(i).innerHeight();
		$('.LockingUBoxRow table.LockingUTableRoot').find('tbody tr').eq(i).height(eqi);
	}
	var definition = $('table.LockingUTableRoot .definition:first').index();
	//alert (definition);
	for (var i=0; i<definition; i++) {
		var Total=$('.LockingUBox > .LockingUTableRoot .definition:first').prevAll();
		Total  += Total;
		alert (Total)
	}
	

});
$(document).ready(function(e) {
	var trLen = ($('.LockingTableRoot').not('.LockingBoxThead > table.LockingTableRoot,.LockingBoxRow > table.LockingTableRoot').find('tbody tr').length);
	for (var i=0;i<trLen;i++) {
		var eqi = $('.LockingTableRoot').not('.LockingBoxThead > table.LockingTableRoot,.LockingBoxRow > table.LockingTableRoot').find('tbody tr').eq(i).innerHeight();
		$('.LockingBoxRow table.LockingTableRoot').find('tbody tr').eq(i).height(eqi);
	}
	
	
	
	
	
	
});