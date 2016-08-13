
var formEditor = new Object();
var editor;

/**
 * 加載KindEditor編輯器
 */
formEditor.init = function(textareaId) {
	editor = KindEditor.create('#' + textareaId, {
	//	cssPath : '../plugins/code/prettify.css',
		uploadJson : '/xmgl/module/jsp/upload_json.jsp',
		fileManagerJson : '/xmgl/module/jsp/file_manager_json.jsp',
		allowFileManager : true,
		resizeType : 1,
		allowPreviewEmoticons : false,
		allowImageUpload : true,
		items : [
		 		'source', '|', 'undo', 'redo', '|', 'preview', 'print', 'template', 'code', 'cut', 'copy', 'paste',
		 		'plainpaste', 'wordpaste', '|', 'justifyleft', 'justifycenter', 'justifyright',
		 		'justifyfull', 'insertorderedlist', 'insertunorderedlist', 'indent', 'outdent', 'subscript',
		 		'superscript', 'clearhtml', 'quickformat', 'selectall', '|', 'fullscreen', '/',
		 		'formatblock', 'fontname', 'fontsize', '|', 'forecolor', 'hilitecolor', 'bold',
		 		'italic', 'underline', 'strikethrough', 'lineheight', 'removeformat', '|', 'image', 'multiimage',
		 		'table', 'hr', 'emoticons', 'baidumap', 'pagebreak',
		 		'anchor', 'link', 'unlink', '|', 'about'
		 	]

	});
};

/**
 * 返回編輯器中帶有html元素的值
 */
formEditor.htmlVal = function() {
	return editor.html();
};
