/*
* jQuery Table Panel Freezing Plug-in
*
* Copyright 2012, Ken Yang
*
* Dual licensed under the MIT and GPL licenses:
* http://www.opensource.org/licenses/mit-license.php
* http://www.gnu.org/licenses/gpl.html
*
*/

(function ($) {
    $.fn.freeze = function (options) {

        options = $.extend({
            width: 400,
            height: 200,
            cols: 1,
            rows: 1
        }, options || {});

        $(this).filter("table").each(function () {

            var _topLeftWrapper, _colHeaderWrapper, _rowHeaderWrapper;

            _topLeftWrapper = $("<div></div>")
                            .css({
                                position: "relative",
                                float: "left",
                                zIndex: "0",
                                overflow: "hidden"
                            });

            var _tabCorner = $(this).clone()
                                    .removeAttr("id")
                                    .appendTo(_topLeftWrapper);

            $(_tabCorner).find("tr:gt(" + (options.rows - 1) + ")").remove();

            _colHeaderWrapper = $("<div></div>")
                                    .css({ position: "relative",
                                        zIndex: "0",
                                        overflow: "hidden"
                                    });

            _rowHeaderWrapper = $("<div></div>")
                                    .css({ position: "relative",
                                        zIndex: "0",
                                        overflow: "hidden",
                                        float: "left"
                                    });

            var _tabRowHeader = $(this).clone()
                                   .removeAttr("id")
                                   .appendTo(_rowHeaderWrapper);

            var _tableWrapper = $("<div></div")
                .css({ position: "relative",
                    overflow: "auto"
                });

            var _newTable = $(this).clone();

            $(_tableWrapper).append(_newTable);

            var _outerWrapper = $("<div></div>")
                                    .append(_topLeftWrapper)
                                    .append(_colHeaderWrapper)
                                    .append(_rowHeaderWrapper)
                                    .append(_tableWrapper);

            $(this).replaceWith(_outerWrapper);

            var _headerHeight = 0;
            var border = 0;
            border = parseInt(this.border);

            if (!border) {
                border = 0;
            }

            $(_newTable).find("tr:lt(" + options.rows + ")").each(function () {
                _headerHeight = _headerHeight + $(this).height(); 
            });

            _headerHeight = _headerHeight + border * 2;

            var _headerWidth = 0;
            $(_newTable).find("tr:eq(0)").find("th,td").slice(0, options.cols).each(function () {
                _headerWidth = _headerWidth + $(this).width();
            });
            _headerWidth = _headerWidth + border * 2;

            $(_topLeftWrapper).css({
                width: _headerWidth + "px",
                height: _headerHeight + "px"
            }).addClass("topLeftWrapper");

            $("tr", _tabCorner).each(function (rowIndex) {
                $(this).css("height", $(_newTable).find("tr:eq(" + rowIndex + ")").height() + "px");
                $("th,td", this).each(function (cellIndex) {
                    $(this).css("width", $("tr", _newTable).eq(rowIndex).find("th,td").eq(cellIndex).width() + "px");
                });
            });

            $(_colHeaderWrapper).css({
                width: $(_newTable).height() <= options.height ? (options.width - _headerWidth) + "px" : (options.width - _headerWidth - 16) + "px",
                height: _headerHeight + "px"
            }).addClass("colHeaderWrapper");

            var _tabColHeader = $(_tabCorner).clone()
                .css({
                    marginLeft: (-_headerWidth - border - 1) + "px",
                    width: $(_newTable).width() + "px"
                })
                .appendTo(_colHeaderWrapper);


            $("tr", _tabCorner).each(function (rowIndex) {
                $(this).find("th,td").slice(options.cols).remove();
            });
            $(_tabCorner).css("width", _headerWidth + "px");


            $(_tabRowHeader).find("tr").each(function (rowIndex) {
                $(this).find("td,th").slice(options.cols).remove();
                $("td,th", this).each(function (cellIndex) {
                    $(this).css("width", $("tr", _newTable).eq(rowIndex).find("td,th").eq(cellIndex).width() + "px");
                });
            });

            $(_rowHeaderWrapper).css({
                height: $(_newTable).width() <= options.width ? (options.height - _headerHeight) + "px" : (options.height - _headerHeight - 16) + "px",
                width: _headerWidth + "px"
            }).addClass("rowHeaderWrapper");

            $(_tabRowHeader).css({
                width: _headerWidth + "px",
                marginTop: (-_headerHeight) + "px"
            });

            $(_tableWrapper).css({
                width: (options.width - _headerWidth) + "px",
                height: (options.height - _headerHeight) + "px"
            }).scroll(function () {
                $(_tabColHeader).css("marginLeft", (-$(this).scrollLeft() - _headerWidth - border - 1) + "px");
                $(_tabRowHeader).css("marginTop", (-$(this).scrollTop() - _headerHeight) + "px");
            });

            $(_newTable).css({
                marginLeft: (-_headerWidth - border - 1) + "px",
                marginTop: -_headerHeight + "px"
            });

            $(_newTable).find("tr:eq(0)").find("td,th").each(function (index) {
                $(this).css("width", $(_tabColHeader).find("tr:eq(0)").find("td,th").eq(index).width() + "px");
            });

            $("tr", _newTable).each(function (index) {
                var _headerRowHeight = $("tr", _tabRowHeader).eq(index).height();
                var _tableRowHeight = $(this).height();
                if (_headerRowHeight > _tableRowHeight)
                    $(this).css("height", _headerRowHeight + "px");
                else
                    $("tr", _tabRowHeader).eq(index).css("height", _tableRowHeight + "px");
            });

        });

    };
})(jQuery);