// JavaScript Document
	modifyLockTable = function(sObj){
	
	var $d = null;	
    if(sObj && typeof(sObj) == "object")
    {   
	   $d = $(sObj);
	}
    else if(sObj)
	$d = $("#"+sObj);
    if($d.attr("class")=="table-hover table-activeTd B-table LockingTableRoot"){
    	
    	var LockingBoxRow  = null;
    	var LockingBoxThead = null;
     	var LockingBox = null;
    	
    	 var LockingBoxRow_div = $d.prev();
		 if(LockingBoxRow_div[0].tagName =="DIV")
		 {
			 LockingBoxRow = LockingBoxRow_div;
		 }else{
			 alert("固定表头列头错误，table上一个应该为LockingBoxRow DIV标签")
		 }
		 var LockingBoxThead_div =  $d.prev().prev();
		 if(LockingBoxThead_div[0].tagName =="DIV")
		 {
			 LockingBoxThead = $(LockingBoxThead_div);
		 }else{
			 alert("固定表头列头错误，table上一个的上一个应该为LockingBoxThead DIV标签")
		 }
		 var LockingBox_div = $d[0].parentElement;
		 if(LockingBox_div.tagName =="DIV")
		 {
			 LockingBox = $(LockingBox_div);
		 }else{
			 alert("固定表头列头错误，table上一层应该为LockingBox DIV标签")
		 }

    	var LockingBoxTheadHeight = $d.find('thead').innerHeight();
    	var LockingBoxTheadWidth = $d.innerWidth();
    	//alert (LockingBoxTheadWidth);
    	//alert (LockingBoxTheadHeight);
    	var LockingBoxRowWidth = $d.find('th').first().innerWidth();
    	LockingBoxThead.css('height',LockingBoxTheadHeight+1).css('width',LockingBoxTheadWidth);
    	var tdWidth = $d.find('tbody>tr>td:first').innerWidth();
    	LockingBoxRow.css('width',LockingBoxRowWidth+2).css('top',LockingBoxTheadHeight+1);
    	$d.clone().removeAttr('id').appendTo(LockingBoxThead).clone().appendTo(LockingBoxRow);
    	LockingBoxRow.find('thead').remove();
    	LockingBoxRow.find('table').css('width',(LockingBoxRowWidth+2));
    	LockingBoxRow.find('tr > th').nextAll().css('display','none');
    	LockingBoxRow.find('tr > td').nextAll().css('display','none');
    	LockingBox.scroll(function(e) {
            var scrollY = $(this).scrollTop();
            var scrollX = $(this).scrollLeft();
            LockingBoxThead.css('top',scrollY);
            LockingBoxRow.css('left',scrollX);
        });
    	var trLen = ($d.not('.LockingBoxThead > table.LockingTableRoot,.LockingBoxRow > table.LockingTableRoot').find('tbody tr').length);
    	for (var i=0;i<trLen;i++) {
    		var eqi = $d.not('.LockingBoxThead > table.LockingTableRoot,.LockingBoxRow > table.LockingTableRoot').find('tbody tr').eq(i).innerHeight();
    		$('.LockingBoxRow table.LockingTableRoot').find('tbody tr').eq(i).height(eqi);
    	}
    }
    $('.table-activeTd tr').hover(
			function () {
				$(this).find('td[rowspan]').css('background','none')
			},function () {

			}
	)
	$('.table-activeTd').find('tr').click(function () {
		$('.tableActive').removeClass('tableActive');
		$(this).find('td').addClass('tableActive');
		$(this).find('td[rowspan]').css('background','none')

	});

  };
	clearLockTable = function(sObj){
		
		var $d = null;	
	    if(sObj && typeof(sObj) == "object")
	    {   
		   $d = $(sObj);
		}
	    else if(sObj)
		$d = $("#"+sObj);
	    if($d.attr("class")=="table-hover table-activeTd B-table LockingTableRoot"){
	    	var LockingBoxRow  = null;
	    	var LockingBoxThead = null;
	    	
	    	 var LockingBoxRow_div = $d.prev();
			 if(LockingBoxRow_div[0].tagName =="DIV")
			 {
				 LockingBoxRow = LockingBoxRow_div;
			 }else{
				 alert("固定表头列头错误，table上一个应该为LockingBoxRow DIV标签")
			 }
			 var LockingBoxThead_div =  $d.prev().prev();
			 if(LockingBoxThead_div[0].tagName =="DIV")
			 {
				 LockingBoxThead = $(LockingBoxThead_div);
			 }else{
				 alert("固定表头列头错误，table上一个的上一个应该为LockingBoxThead DIV标签")
			 }
			 if(LockingBoxThead.find('table')){
				 LockingBoxThead.find('table').remove();
				}
				if(LockingBoxRow.find('table')){
					LockingBoxRow.find('table').remove();
				}	 
	    }
	 };
  
  
  
  