/*!
 * By: Bingzi Xu
 *
 * Date: 2013-07-01 18:21
 */
function windowHeight() {
	var winH = window.innerHeight;
	var winW = window.innerWidth;
	var headerH = $('.header').innerHeight();
	var leftNavH = winH - headerH;
	$('.leftNav').css('min-height',leftNavH-4);
	$('.windowOpenBox').css('height',winH);
	var  modalBodyHeight = winH-56;
	$('.windowOpenBox-body').css('max-height',modalBodyHeight);
	$('.windowOpenBox-body > iframe').css('height',modalBodyHeight);
	var MainBoxIframeHeight = winH - headerH-4;
	var leftBoxWidth = $('.page-sidebar').innerWidth();
	var MainBoxIframeWidth = winW - leftBoxWidth;
	var mainOpenBoxIframeHeight = MainBoxIframeHeight-56;
    $('.MainBox > iframe').css('min-height',MainBoxIframeHeight);

    $('.mainOpenBox').css({"width":MainBoxIframeWidth,
			"height":MainBoxIframeHeight
		});
    $('.windowOpenBoxMain-body > iframe').css('height',mainOpenBoxIframeHeight);
    $('.ztqyzzOpenBox').css({"width":MainBoxIframeWidth,
			"height":MainBoxIframeHeight,
			"marginLeft":-(MainBoxIframeWidth/2),
			"marginTop":-(MainBoxIframeHeight/2)
		});
	$('.MainBox').css('margin-left',leftBoxWidth);
	$('.Box50').css('height',MainBoxIframeHeight/2);
}
$(document).ready(function(e) {
    $('.selectAddBox tr').click(function(e) {
        $('.selectAddBox tr.selectAddBox-active').removeClass('selectAddBox-active');
		$(this).addClass('selectAddBox-active');
    });
    $('.unstyled li').click(function(e) {
        $('.unstyled li.selectAddBox-active').removeClass('selectAddBox-active');
		$(this).addClass('selectAddBox-active');
    });
});
$(document).ready(function(e) {
	loadingCss3();
    windowHeight();
	//alert (leftNavH);
	$('.AdvancedSearch').click(function(e) {
        $('.AdvancedSearchTables').show('fast');
    });
	$('.table-activeTd').find('tr').click(function () {
		$('.tableActive').removeClass('tableActive');
		$(this).find('td').addClass('tableActive');
		$(this).find('td[rowspan]').css('background','none')

	});
	$('.sub-menu>li>a').click(function(e) {
		$('.leftNav .active').removeClass('active');
		$(this).parent('li').addClass('active');
		$(this).parent('li').siblings().find('ul').slideUp('fast')
    });
	$('.B-small-from-table-auto .B-table tr th:last-child,.B-small-from-table-auto .B-table tr td:last-child').css('border-right-style','none');
	$('.Nav:even').addClass('even');
	$('.Nav:first').css('opacity','1');
	$('.Nav>a').click(function(e) {
		$('.Nav').css('opacity','0.8');
        $(this).parent('.Nav').css('opacity','1');
    });
    // $('.Nav').hover(function() {
    // 	$(this).css('opacity','1');
    // },function () {
    // 	$(this).css('opacity','0.8');
    // })
	$('.Nav > .dropdown > a').click(function(e) {
		$('.Nav').css('opacity','0.8');
        $(this).parent('.dropdown').parent('.Nav').css('opacity','1');
    });
	/*$('.success-b').click(function () {
		$('.success-b-open').slideDown('fast').delay(2000).slideUp('fast');
	})*/
    //$( ".leftNav" ).accordion({header : "h3",heightStyle: "content" });
    //$('.leftNav > div').accordion({header: "h4" ,heightStyle: "content"});
	// $('#myModal').modal('show');
	closeLoading3 ();
	$('tr').hover(
			function () {
				$(this).find('td[rowspan]').css('background','none')
			},function () {

			}
		)
});
/*-----start Process------*/
$(document).ready(function(e) {
	/*$('.ProcessBox').css('width',function () {
		var ProcesWidth = $('.Process').innerWidth();
		var ProcesNum = ($(this).find('.Process:last').index())+1;
		var ProcesBoxsWidth = ($(this).find('.Process').innerWidth()+20)*(ProcesNum) + 2*(ProcesNum);
		var ProcesNextsWidth = ($(this).find('.next').innerWidth())*(ProcesNum-1);
		var ProcessBoxSum = ProcesBoxsWidth + ProcesNextsWidth;
		return (ProcessBoxSum)
	})*/
	$('.ProcessBox').css('width',function () {
		var ProcesNum = $(this).find('.Process').length;
		var ProcesBoxsWidth = 0;
		for (i=0; i<ProcesNum; i++) {
			ProcesBoxsWidth += $(this).find('.Process').eq(i).innerWidth();
		}
		var padding = ProcesNum*22;
		return (ProcesBoxsWidth + padding);
	});
	
});
/*-----end Process------*/

$(window).resize(function(){
   windowHeight();
});
function successInfo () {
	$('.success-b-open').slideDown('fast').delay(2000).slideUp('fast');
}
function blockInfo () {
	$('.block-b-open').slideDown('fast').delay(2000).slideUp('fast');
}
function errorInfo () {
	$('.error-b-open').slideDown('fast').delay(2000).slideUp('fast');
}

/*start loading*/
function loadingCss3 () {
	var windowHeight = $(window).innerHeight();
	//alert (windowHeight);
	var windowWidth = $(window).innerWidth();
	//alert (windowWidth);
	$('<div class="loadingBox"><img src="/xmgl/images/loading.gif"></div>').css('height',windowHeight).insertAfter('body');
	$('<div class="loadingBg"></div>').css('height',windowHeight).insertAfter('body');

	
}
function closeLoading3 () {
	$('.loadingBg,.loadingBox').remove();
}
/*end loading*/

