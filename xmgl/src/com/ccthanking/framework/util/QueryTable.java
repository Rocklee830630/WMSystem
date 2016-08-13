/*****************************************************************************
 * Free table class. for html and java(jsp/servlet) application with Database
 * operation. (oracle / based on sql)
 * You can copy and/or use and/or modify this program free,but please reserve
 * the segment above. Please mail me if you have any question, Good day!
 *****************************************************************************
 */

package com.ccthanking.framework.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;

public class QueryTable implements java.io.Serializable {
	public static final int STYLE_NONE = 0;
	public static final int STYLE_SHOW_DEFAULT = 1;
	public static final int STYLE_SHOW_EXCEL = 2;
	public static final int STYLE_PRINT_DEFAULT = 3;
	public static final int STYLE_PRINT_EXCEL = 4;
	public static final int STYLE_CUSTOMER = 5;

	public static final int WIDTH_ABSOLUTELY = 1; // 绝对宽度
	public static final int WIDTH_ADJUSTABLE = 0; // 宽度可调，相对宽度

	private boolean cellLink; // 单元格链接功能
	private Connection conn;
	protected String styleSheet; // 样式表名
	protected int rows = 0, cols = 0, // 显示的行列数
			allcols = 0; // 总列数
	protected String title, // 表格标题
			sql, // 绑定的sql语句
			align = null, valign = null; // 表格本身在页面中的对齐方式
	public FieldProperty[] fields = null; // 字段属性表
	protected int width, borderWidth, widthType = WIDTH_ADJUSTABLE;
	// 表格宽度，边框宽度，宽度类型
	protected String titleColor, titleBGColor, titleAlign = null,
			titleValign = null;
	// 标题颜色，标题背景颜色，标题对齐方式
	protected String tailColor, tailBGColor, tailAlign = null,
			tailValign = null;
	// 表格尾部颜色，背景，对齐方式
	protected String headerColor, headerBGColor, headerAlign = null,
			headerValign = null;
	// 表头颜色，背景，对齐方式
	protected String[] bodyColors, bodyBGColors;
	// 表格体颜色及背景数组
	protected String bodyAlign = null, bodyValign = null, borderColor = null,
			bgColor = null, bodyColor = null, bodyBGColor = null;
	// 表格体的对齐方式，边框颜色，背景颜色，前景色
	protected ArrayList titleMemoList, tailMemoList, attachList;
	// 标题备注信息，表格尾部备注信息，尾部附记信息列表(值)
	protected String attachmemo; // 附记信息描述
	protected String[][] data; // 表格数据
	protected int style; // 显示样式
	protected int rowsPerPage; // 每页行数
	protected String bodyFontSize, titleFontSize, tailFontSize,
			titleMemoFontSize, tailMemoFontSize, headerFontSize;
	protected String bodyStyleSheet, titleStyleSheet, headerStyleSheet,
			titleMemoStyleSheet, tailMemoStyleSheet, tailStyleSheet;
	protected String titleMemoAlign, titleMemoValign, titleMemoColor,
			titleMemoBgColor, tailMemoAlign, tailMemoValign, tailMemoColor,
			tailMemoBgColor;
	private boolean showTail = true; // 是否显示表格尾
	private boolean showTailMemoBorder = true; // 是否显示尾部信息的边框
	private boolean showTitleMemoBorder = false; // 是否显示标题备注边框
	private int lockIndex = 0; // 锁定区域到左起第 n 列
	private int[][] locks = null;
	private int[][] headerLocks = null;

	public void setFieldDicCode(int index, String code) {
		if (this.fields == null || this.fields.length <= index || index < 0)
			return;
		this.fields[index].dicCode = code;
	}

	public String getFieldDicCode(int index) {
		if (this.fields == null || this.fields.length <= index || index < 0)
			return null;
		return this.fields[index].dicCode;
	}

	public void setCellLink(boolean b) {
		this.cellLink = b;
	}

	public boolean getCellLink() {
		return this.cellLink;
	}

	public void setAttachMemo(String memo) {
		this.attachmemo = memo;
	}

	public void addAttachMemoValue(int index, String value) {
		if (attachList == null)
			attachList = new ArrayList();
		attachList.add(index + "->" + value);
	}

	public void setHeaderLockArea(int x1, int y1, int x2, int y2) {
		int[][] tmp = headerLocks;
		if (headerLocks == null) {
			headerLocks = new int[1][4];
		} else {
			headerLocks = new int[headerLocks.length][4];
			for (int i = 0; i < tmp.length; i++)
				for (int j = 0; j < 4; j++)
					headerLocks[i][j] = tmp[i][j];
		}
		headerLocks[locks.length - 1][0] = x1;
		headerLocks[locks.length - 1][1] = y1;
		headerLocks[locks.length - 1][2] = x2;
		headerLocks[locks.length - 1][3] = y2;
	}

	public void setLockArea(int x1, int y1, int x2, int y2) {
		int[][] tmp = locks;
		if (locks == null) {
			locks = new int[1][4];
		} else {
			locks = new int[locks.length][4];
			for (int i = 0; i < tmp.length; i++)
				for (int j = 0; j < 4; j++)
					locks[i][j] = tmp[i][j];
		}
		locks[locks.length - 1][0] = x1;
		locks[locks.length - 1][1] = y1;
		locks[locks.length - 1][2] = x2;
		locks[locks.length - 1][3] = y2;
	}

	public void setLockIndex(int i) {
		// if(i > this.cols) this.lockIndex = 0;
		// else
		{
			this.lockIndex = i;
			this.setLockArea(0, 0, i, 0);
		}
	}

	public int getLockIndex() {
		return this.lockIndex;
	}

	public void setShowTitleMemoBorder(boolean aBool) {
		this.showTitleMemoBorder = aBool;
	}

	public void setShowTailMemoBorder(boolean aBool) {
		this.showTailMemoBorder = aBool;
	}

	public void setShowTail(boolean aBool) {
		this.showTail = aBool;
	}

	public void setTailStyleSheet(String sheet) {
		this.tailStyleSheet = sheet;
	}

	public String getTailStyleSheet() {
		return this.tailStyleSheet;
	}

	public void setTitleMemoStyleSheet(String sheet) {
		this.titleMemoStyleSheet = sheet;
	}

	public String getTitleMemoStyleSheet() {
		return this.titleMemoStyleSheet;
	}

	public void setHeaderStyleSheet(String sheet) {
		this.headerStyleSheet = sheet;
	}

	public String getHeaderStyleSheet() {
		return this.headerStyleSheet;
	}

	public void setBodyStyleSheet(String sheet) {
		this.bodyStyleSheet = sheet;
	}

	public String getBodyStyleSheet() {
		return this.bodyStyleSheet;
	}

	public void setTailMemoStyleSheet(String sheet) {
		this.tailMemoStyleSheet = sheet;
	}

	public String getTailMemoStyleSheet() {
		return this.tailMemoStyleSheet;
	}

	public void setTitleStyleSheet(String sheet) {
		this.titleStyleSheet = sheet;
	}

	public String getTitleStyleSheet() {
		return this.titleStyleSheet;
	}

	public void setTailMemoFontSize(String size) {
		this.tailMemoFontSize = size;
	}

	public String getTailMemoFontSize() {
		return this.tailMemoFontSize;
	}

	public void setTailMemoColor(String color) {
		this.tailMemoColor = color;
	}

	public String getTailMemoColor() {
		return this.tailMemoColor;
	}

	public String getTailMemoBGColor() {
		return this.tailMemoBgColor;
	}

	public void setTailMemoBGColor(String color) {
		this.tailMemoBgColor = color;
	}

	public void setTailMemoAlign(String align) {
		this.tailMemoAlign = align;
	}

	public String getTailMemoAlign() {
		return this.tailMemoAlign;
	}

	public void setTailMemoValign(String align) {
		this.tailMemoValign = align;
	}

	public String getTailMemoValign() {
		return this.tailMemoValign;
	}

	public void setTitleMemoColor(String color) {
		this.titleMemoColor = color;
	}

	public String getTitleMemoColor() {
		return this.titleMemoColor;
	}

	public String getTitleMemoBGColor() {
		return this.titleMemoBgColor;
	}

	public void setTitleMemoBGColor(String color) {
		this.titleMemoBgColor = color;
	}

	public void setTitleMemoAlign(String align) {
		this.titleMemoAlign = align;
	}

	public String getTitleMemoAlign() {
		return this.titleMemoAlign;
	}

	public void setTitleMemoValign(String align) {
		this.titleMemoValign = align;
	}

	public String getTitleMemoValign() {
		return this.titleMemoValign;
	}

	public void setTitleMemoFontSize(String size) {
		this.titleMemoFontSize = size;
	}

	public String getTitleMemoFontSize() {
		return this.titleMemoFontSize;
	}

	public void setTailFontSize(String size) {
		this.tailFontSize = size;
	}

	public String getTailFontSize() {
		return this.tailFontSize;
	}

	public void setTitleFontSize(String size) {
		this.titleFontSize = size;
	}

	public String getTitleFontSize() {
		return this.titleFontSize;
	}

	public void setBodyFontSize(String size) {
		this.bodyFontSize = size;
	}

	public String getBodyFontSize() {
		return this.bodyFontSize;
	}

	public void addTitleMemo(String memo) {
		String[] list = new String[1];
		list[0] = memo;
		addTitleMemo(list);
	}

	public void addTitleMemo(String m1, String m2) {
		String[] list = new String[2];
		list[0] = m1;
		list[1] = m2;
		addTitleMemo(list);
	}

	public void addTitleMemo(String m1, String m2, String m3) {
		String[] list = new String[3];
		list[0] = m1;
		list[1] = m2;
		list[2] = m3;
		addTitleMemo(list);
	}

	public void addTitleMemo(String m1, String m2, String m3, String m4) {
		String[] list = new String[4];
		list[0] = m1;
		list[1] = m2;
		list[2] = m3;
		list[3] = m4;
		addTitleMemo(list);
	}

	public void addTitleMemo(String m1, String m2, String m3, String m4,
			String m5) {
		String[] list = new String[5];
		list[0] = m1;
		list[1] = m2;
		list[2] = m3;
		list[3] = m4;
		list[4] = m5;
		addTitleMemo(list);
	}

	public void addTitleMemo(String[] list) {
		if (list == null)
			return;
		String[] stlist = new String[list.length];
		for (int i = 0; i < list.length; i++)
			stlist[i] = "x_hm_d";
		addTitleMemo(list, stlist);
	}

	public void addTitleMemo(String[] list, String[] stlist) {
		if (list == null)
			return;
		if (stlist == null) {
			stlist = new String[list.length];
			for (int i = 0; i < list.length; i++)
				stlist[i] = "x_hm_d";
		}
		if (titleMemoList == null)
			titleMemoList = new ArrayList();
		ArrayList memo = new ArrayList(2);
		memo.add(list);
		memo.add(stlist);
		titleMemoList.add(titleMemoList.size(), memo);
	}

	private void addTitleMemoLocal(String memo, String styleSheetName) { // addTitleMemoLocal(memo,
																			// "x_hm");
		if (this.empty(memo))
			return;
		if (titleMemoList == null)
			titleMemoList = new ArrayList();
		ArrayList list = new ArrayList(2);
		list.add(list.size(), memo);
		list.add(list.size(), styleSheetName);
		titleMemoList.add(titleMemoList.size(), list);
	}

	// ////////////////////////////////////////////////////////////

	public void addTailMemo(String memo) {
		String[] list = new String[1];
		list[0] = memo;
		addTailMemo(list);
	}

	public void addTailMemo(String m1, String m2) {
		String[] list = new String[2];
		list[0] = m1;
		list[1] = m2;
		addTailMemo(list);
	}

	public void addTailMemo(String m1, String m2, String m3) {
		String[] list = new String[3];
		list[0] = m1;
		list[1] = m2;
		list[2] = m3;
		addTailMemo(list);
	}

	public void addTailMemo(String m1, String m2, String m3, String m4) {
		String[] list = new String[4];
		list[0] = m1;
		list[1] = m2;
		list[2] = m3;
		list[3] = m4;
		addTailMemo(list);
	}

	public void addTailMemo(String m1, String m2, String m3, String m4,
			String m5) {
		String[] list = new String[5];
		list[0] = m1;
		list[1] = m2;
		list[2] = m3;
		list[3] = m4;
		list[4] = m5;
		addTailMemo(list);
	}

	public void addTailMemo(String[] list) {
		if (list == null)
			return;
		String[] stlist = new String[list.length];
		for (int i = 0; i < list.length; i++)
			stlist[i] = "x_hm_d";
		addTailMemo(list, stlist);
	}

	public void addTailMemo(String[] list, String[] stlist) {
		if (list == null)
			return;
		if (stlist == null) {
			stlist = new String[list.length];
			for (int i = 0; i < list.length; i++)
				stlist[i] = "x_hm_d";
		}
		if (tailMemoList == null)
			tailMemoList = new ArrayList();
		ArrayList memo = new ArrayList(2);
		memo.add(list);
		memo.add(stlist);
		tailMemoList.add(tailMemoList.size(), memo);
	}

	private void addTailMemoLocal(String memo, String styleSheetName) { // addTailMemo(memo,
																		// "x_tm");
		if (this.empty(memo))
			return;
		if (tailMemoList == null)
			tailMemoList = new ArrayList();
		ArrayList list = new ArrayList(2);
		list.add(list.size(), memo);
		list.add(list.size(), styleSheetName);
		tailMemoList.add(tailMemoList.size(), list);
	}

	// ////////////////////////////////////////////////////////////
	public void setBorderColor(String color) {
		this.borderColor = color;
	}

	public String getBorderColor() {
		return this.borderColor;
	}

	public void setBgColor(String color) {
		this.bgColor = color;
	}

	public String getBgColor() {
		return this.bgColor;
	}

	public void setStyleSheet(String sheet) {
		this.styleSheet = sheet;
	}

	public String getStyleSheet() {
		return this.styleSheet;
	}

	public boolean setTitleAlign(String align) {
		if (align == null)
			this.titleAlign = "center";
		else
			this.titleAlign = align;
		return (align != null);
	}

	public boolean setTitleValign(String align) {
		if (align == null)
			this.titleValign = "middle";
		else
			this.titleValign = align;
		return (align != null);
	}

	public void setHeaderFontSize(String size) {
		this.headerFontSize = size;
	}

	public String getHeaderFontSize() {
		return this.headerFontSize;
	}

	public boolean setHeaderAlign(String align) {
		if (align == null)
			this.headerAlign = "center";
		else
			this.headerAlign = align;
		return (align != null);
	}

	public boolean setHeaderValign(String align) {
		if (align == null)
			this.headerValign = "middle";
		else
			this.headerValign = align;
		return (align != null);
	}

	public boolean setTailAlign(String align) {
		if (align == null)
			this.tailAlign = "right";
		else
			this.tailAlign = align;
		return (align != null);
	}

	public boolean setTailValign(String align) {
		if (align == null)
			this.tailValign = "middle";
		else
			this.tailValign = align;
		return (align != null);
	}

	public boolean setBodyAlign(String align) {
		if (align == null)
			this.bodyAlign = "center";
		else
			this.bodyAlign = align;
		return (align != null);
	}

	public boolean setBodyValign(String align) {
		if (align == null)
			this.bodyValign = "middle";
		else
			this.bodyValign = align;
		return (align != null);
	}

	public boolean setTailColor(String color) {
		if (color == null) {
			this.tailColor = "#000000";
			return false;
		} else {
			this.tailColor = color;
			return true;
		}
	}

	public String getTailColor() {
		return this.tailColor;
	}

	public boolean setTailBGColor(String color) {
		if (color == null) {
			this.tailBGColor = "#A2BEE1";
			return false;
		} else {
			this.tailBGColor = color;
			return true;
		}
	}

	public String getTailBGColor() {
		return this.tailBGColor;
	}

	public void setWidthType(int type) {
		this.widthType = type;
	}

	public int getWidthType() {
		return this.widthType;
	}

	public void setValign(String valign) {
		this.valign = valign;
	}

	public String getValign() {
		return this.valign;
	}

	public void setStyle(int style) {
		this.style = style;
		switch (style) {
		case QueryTable.STYLE_SHOW_DEFAULT:
			setDefault();
			break;
		case QueryTable.STYLE_SHOW_EXCEL:
			setShowExcel();
			break;
		case QueryTable.STYLE_CUSTOMER:
			setCustomer();
			break;
		case QueryTable.STYLE_NONE:
			setStyleNone();
			break;
		case QueryTable.STYLE_PRINT_DEFAULT:
			setStylePrintDefault();
			break;
		case QueryTable.STYLE_PRINT_EXCEL:
			setStylePrintExcel();
			break;
		default:
			setDefault();
			break;
		}
	}

	public int getStyle() {
		return this.style;
	}

	public void setRowsPerPage(int row) {
		this.rowsPerPage = row;
	}

	public int getRowsPerPage() {
		return this.rowsPerPage;
	}

	public boolean setHeaderColor(String color) {
		if (color == null) {
			this.headerColor = "#000000";
			return false;
		} else {
			this.headerColor = color;
			return true;
		}
	}

	public String getHeaderColor() {
		return this.headerColor;
	}

	public boolean setHeaderBGColor(String color) {
		if (color == null) {
			this.headerBGColor = "#99CCCC";
			return false;
		} else {
			this.headerBGColor = color;
			return true;
		}
	}

	public String getHeaderBGColor() {
		return this.headerBGColor;
	}

	public boolean setTitleBGColor(String color) {
		if (color == null) {
			this.titleBGColor = "#A2BEE1";
			return false;
		} else {
			this.titleBGColor = color;
			return true;
		}
	}

	public String getTitleBGColor() {
		return this.titleBGColor;
	}

	public boolean setTitleColor(String color) {
		if (color == null) {
			this.titleColor = "#AA0000";
			return false;
		} else {
			this.titleColor = color;
			return true;
		}
	}

	public String getTitleColor() {
		return this.titleColor;
	}

	public void setBorderWidth(int width) {
		this.borderWidth = width;
	}

	public int getBorderWidth() {
		return this.borderWidth;
	}

	public boolean setWidth(int width) {
		if (width < 0) {
			this.width = 100;
			return false;
		} else {
			this.width = width;
			return true;
		}
	}

	public int getWidth() {
		return this.width;
	}

	public boolean setBodyBGColors(String[] bodyColors) {
		if (bodyColors == null) {
			if (this.bodyBGColors == null) {
				this.bodyBGColors = new String[2];
				this.bodyBGColors[0] = "#D6D6D6";
				this.bodyBGColors[1] = "#E6E6E6";
			}
			return false;
		} else {
			this.bodyBGColors = bodyColors;
			return true;
		}
	}

	public String[] getBodyBGColors() {
		return this.bodyBGColors;
	}

	public void setBodyColor(String color) {
		this.bodyColor = color;
	}

	public String getBodyColor() {
		return this.bodyColor;
	}

	public void setBodyBGColor(String color) {
		this.bodyBGColor = color;
	}

	public String getBodyBGColor() {
		return this.bodyBGColor;
	}

	public boolean setBodyColors(String[] bodyColors) {
		if (bodyColors == null) {
			if (this.bodyColors == null) {
				this.bodyColors = new String[2];
				this.bodyColors[0] = "#0033CC";
				this.bodyColors[1] = "#0033CC";
			}
			return false;
		}
		this.bodyColors = bodyColors;
		return true;
	}

	public String[] getBodyColors() {
		return this.bodyColors;
	}

	public boolean setAlign(String align) {
		if (align == null) {
			this.align = "center";
			return false;
		}
		this.align = align;
		return false;
	}

	public String getAlign() {
		return this.align;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return this.title;
	}

	public boolean setCols(int cols) {
		if (cols > this.cols)
			return false;
		this.cols = cols;
		return true;
	}

	public int getCols() {
		return this.cols;
	}

	public boolean setRows(int rows) {
		if (rows > this.rows)
			return false;
		this.rows = rows;
		return true;
	}

	public int getRows() {
		return this.rows;
	}

	public boolean setAllcols(int cols) {
		if (cols > this.allcols && this.allcols != 0)
			return false;
		this.allcols = cols;
		return true;
	}

	public int getAllcols() {
		return this.allcols;
	}

	public boolean setData(String[][] data) {
		if (data == null)
			return false;
		this.data = data;
		this.allcols = data[0].length;
		this.rows = data.length;
		if (this.cols <= 0)
			this.cols = this.allcols;
		return true;
	}

	public String[][] getData() {
		return this.data;
	}

	public QueryTable() {
	}

	public QueryTable(Connection conn, String sql) {
		this.conn = conn;
		this.sql = sql;
		if (sql != null) {
			if (this.query(sql)) {
				// this.cols = this.allcols;
			}
		}
	}

	public QueryTable(Connection conn, String sql, String title, int style) {
		this.conn = conn;
		setStyle(style);
		this.sql = sql;
		this.title = title;
		this.style = style;
		if (sql != null) {
			if (this.query(sql)) {
				// this.cols = this.allcols;
			}
		}
	}

	public QueryTable(String title, int style, int width) {
		setStyle(style);
		this.title = title;
		this.style = style;
		this.width = width;
	}

	private void setShowExcel() {
		this.align = "center";
		this.valign = "middel";
		this.width = 100;
		this.borderWidth = 2;
		this.rowsPerPage = 0;
		this.styleSheet = null;

		this.title = null;
		this.titleColor = null;
		this.titleBGColor = null;
		this.titleAlign = null; // center
		this.titleFontSize = "12.0";
		this.titleStyleSheet = null;

		this.titleMemoFontSize = "9.0";
		this.titleMemoAlign = "right";
		this.titleMemoValign = null;
		this.titleMemoColor = null;
		this.titleMemoBgColor = null;
		this.titleMemoStyleSheet = null;

		this.headerBGColor = null;
		this.headerColor = null;
		this.headerAlign = null; // center
		this.headerFontSize = "10.5";
		this.headerValign = null;
		this.headerStyleSheet = null;

		this.bodyColors = null;
		this.bodyBGColors = null;
		this.bodyAlign = "center"; // center
		this.bodyFontSize = "9.0"; // 9.0
		this.bodyValign = null;
		this.bodyStyleSheet = null;

		this.tailBGColor = null;
		this.tailColor = null;
		this.tailAlign = "right";
		this.tailFontSize = "9.0";
		this.tailStyleSheet = null;
		this.tailValign = null;

		this.tailMemoFontSize = "9.0";
		this.tailMemoAlign = "left";
		this.tailMemoValign = null;
		this.tailMemoBgColor = null;
		this.tailMemoColor = null;
		this.tailMemoStyleSheet = null;

	}

	private void setCustomer() {
		this.align = "center";
		this.valign = "middel";
		this.width = 100;
		this.borderWidth = 2;
		this.rowsPerPage = 0;

		StringBuffer buf = new StringBuffer();
		buf.append("<style>\n");
		buf.append("<!--\n");
		buf.append(".x_table\n");
		buf.append("	{border-collapse: collapse;}\n");
		buf.append("-->\n");
		buf.append("</style>\n");
		this.styleSheet = buf.toString();

		this.title = null;
		this.titleColor = null;
		this.titleBGColor = null;
		this.titleAlign = "center"; // center
		this.titleFontSize = "12.0";
		this.titleStyleSheet = null;

		this.titleMemoFontSize = "9.0";
		this.titleMemoAlign = "right";
		this.titleMemoValign = null;
		this.titleMemoColor = null;
		this.titleMemoBgColor = null;
		this.titleMemoStyleSheet = null;

		this.headerBGColor = null;
		this.headerColor = null;
		this.headerAlign = "center"; // center
		this.headerFontSize = "10.5";
		this.headerValign = null;
		this.headerStyleSheet = null;

		this.bodyColors = null;
		this.bodyBGColors = null;
		this.bodyAlign = "center"; // center
		this.bodyFontSize = "9.0"; // 9.0
		this.bodyValign = null;
		this.bodyStyleSheet = null;

		this.tailBGColor = null;
		this.tailColor = null;
		this.tailAlign = "right";
		this.tailFontSize = "9.0";
		this.tailStyleSheet = null;
		this.tailValign = null;

		this.tailMemoFontSize = "9.0";
		this.tailMemoAlign = "left";
		this.tailMemoValign = null;
		this.tailMemoBgColor = null;
		this.tailMemoColor = null;
		this.tailMemoStyleSheet = null;
	}

	private void setStyleNone() {
		this.align = "center";
		this.valign = "middel";
		this.width = 100;
		this.borderWidth = 2;
		this.rowsPerPage = 0;
	}

	private void setStylePrintDefault() {
		// return;
	}

	private void setStylePrintExcel() {
		// return;
	}

	private boolean setDefault() {
		this.align = "center";
		this.valign = "middel";
		this.width = 100;
		this.borderWidth = 2;
		this.rowsPerPage = 0;
		this.styleSheet = "<style>\n" + "<!--\n" + ".trType\n"
				+ "{height:20px}\n" + "-->\n" + "</style>\n";
		this.style = QueryTable.STYLE_SHOW_DEFAULT;

		this.title = null;
		this.titleColor = "#DD0000";
		this.titleBGColor = "#A2BEE1";
		this.titleAlign = "center";
		this.titleValign = null;
		this.titleFontSize = "12.0";
		this.titleStyleSheet = null;

		this.titleMemoFontSize = "9.0";
		this.titleMemoAlign = "right";
		this.titleMemoValign = null;
		this.titleMemoColor = null;
		this.titleMemoBgColor = null;
		this.titleMemoStyleSheet = null;

		this.headerBGColor = "#99CCCC";
		this.headerColor = null;
		this.headerAlign = "center";
		this.headerFontSize = "10.5";
		this.headerValign = null;
		this.headerStyleSheet = null;

		this.bodyColors = new String[2];
		this.bodyBGColors = new String[2];
		this.bodyBGColors[0] = "#D6D6D6";
		this.bodyBGColors[1] = "#E6E6E6";
		this.bodyColors[0] = "#0033CC";
		this.bodyColors[1] = "#0033CC";
		this.bodyAlign = "center"; // center
		this.bodyFontSize = "9.0"; // 9.0
		this.bodyValign = null;
		this.bodyStyleSheet = null;

		this.tailBGColor = "#A2BEE1";
		this.tailColor = "#808080";
		this.tailAlign = "right";
		this.tailFontSize = "9.0";
		this.tailStyleSheet = null;
		this.tailValign = null;

		this.tailMemoFontSize = "9.0";
		this.tailMemoAlign = "left";
		this.tailMemoValign = null;
		this.tailMemoBgColor = null;
		this.tailMemoColor = null;
		this.tailMemoStyleSheet = null;

		return true;
	}

	public boolean query() {
		if (this.empty(sql))
			return false;
		try {
			this.data = execQuery(conn, sql);
			if (data != null) {
				this.rows = data.length;
				this.allcols = data[0].length;
				return true;
			} else {
				this.allcols = 0;
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return false;
		}
	}

	public boolean query(Connection conn, String sql) {
		this.conn = conn;
		this.sql = sql;
		return this.query();
	}

	public boolean query(String sql) {
		if (this.empty(sql))
			return false;
		this.sql = sql;
		return this.query();
	}

	public boolean addField(int index, FieldProperty fd) {
		if (fd == null)
			return false;
		if (!this.empty(sql)) {
			if (this.allcols <= 0)
				return false;
		} else {
			this.allcols = 128;
		}
		if (this.fields == null) {
			this.fields = new FieldProperty[allcols];
		}
		if (index >= allcols)
			return false;
		this.fields[index] = fd;
		return true;
	}

	public boolean addField(int index, String colName, int colType,
			int colIndex, int colWidth, String colSum, int colGroup,
			String colLink) {
		if (!this.empty(this.sql)) {
			if (this.allcols <= 0)
				return false;
		} else {
			this.allcols = 128;
		}
		if (this.fields == null) {
			this.fields = new FieldProperty[allcols];
		}
		if (index >= allcols)
			return false;
		this.fields[index] = new FieldProperty();
		this.fields[index].setColName(colName);
		this.fields[index].setColType(colType);
		this.fields[index].setColWidth(colWidth);
		this.fields[index].setColGroup(colGroup);
		this.fields[index].setColSum(colSum);
		this.fields[index].setColIndex(colIndex);
		this.fields[index].setColLink(colLink);
		return true;
	}

	public void setFieldAlias(int index, String name) {
		if (this.empty(name))
			return;
		if (index >= allcols)
			return;
		if (this.fields == null || index >= this.fields.length
				|| this.fields[index] == null)
			return;
		this.fields[index].setAlias(name);
	}

	public String getFieldAlias(int index) {
		try {
			return this.fields[index].getAlias();
		} catch (Exception e) {
			return null;
		}
	}

	public boolean addParameter(int index, String name, String value) {
		if (this.allcols <= 0)
			return false;
		if (index >= this.fields.length)
			return false;
		return this.fields[index].bindParameter(name, value);
	}

	public boolean addParameter(int index, String name, FieldProperty fd) {
		if (this.allcols <= 0)
			return false;
		if (index >= this.fields.length)
			return false;
		return this.fields[index].bindParameter(name, fd);
	}

	// //////////////////////////////////////////////////////////////////////////////////////
	public int getColIndex(int i) {

		for (int j = 0; j < allcols; j++) {
			if (this.fields[j].colIndex == (i + 1)) {
				return j;
			} else if (this.fields[j].colIndex == 0) {
				return -i;
			}
		}
		return -9999;
	}

	// public String listdetail(java.io.OutputStream out ,int st,String
	// fileName)
	// {
	// String res = null;
	//
	// return res;
	// }
	public String listdetail() {
		StringBuffer buf = new StringBuffer();
		int groupCols = 0;
		int maxGroup = 0;
		if (this.allcols == 0)
			this.allcols = this.fields.length;
		if (this.empty(sql))
			if (this.fields != null) {
				for (int i = 0; i < fields.length; i++) {
					if (fields[i] == null) {
						this.allcols = i;
						break;
					}
				}
			}
		int[] riseGroup = new int[allcols];
		int[] colIndex = new int[allcols];
		int tmpcols = 0;
		if (cols > 0)
			tmpcols = cols;
		cols = 0;
		int tag = 0;

		for (int i = 0; i < allcols; i++) {

			int tmp2 = getColIndex(i);
			if (tmp2 >= 0) {
				colIndex[i] = tmp2;
				cols++;
			} else {
				if (tmp2 > -9999)
					colIndex[i] = -tmp2;
			}

			if (this.fields[i].colGroup > 0) {
				if (this.fields[i].colGroup >= maxGroup) {
					riseGroup[groupCols] = i;
					maxGroup = this.fields[i].colGroup;
				} else {
					tag = this.fields[i].colGroup;
					for (int j = 0; j < groupCols; j++) {
						if (tag < this.fields[j].colGroup) {
							for (int k = groupCols; k > j; k--) {
								riseGroup[k] = riseGroup[k - 1];
							}
							riseGroup[j] = i;
							break;
						}
					}
				}
				groupCols++;
			}
		}
		int x = 0, y = cols, z = 2;
		for (int i = 0; i < y; i++) {
			if (this.fields[i].list.length > x)
				x = this.fields[i].list.length;
		}
		int[][][] tg = new int[x][y][z];
		for (int i = 0; i < x; i++)
			for (int j = 0; j < y; j++)
				for (int k = 0; k < z; k++)
					tg[i][j][k] = 1;

		for (int i = 0; i < x; i++) // rows
		{
			for (int q = 0; q < y; q++) // cols
			{
				int j = (colIndex[q] >= 0) ? colIndex[q] : q;
				// j--;
				// System.out.print(j+" ");
				if (tg[i][j][0] == 0 || tg[i][j][1] == 0)
					continue;
				int m = 0, n = 0;
				for (int a = i + 1; a < x; a++) {
					if (a >= this.fields[j].list.length) {
						tg[a][j][0] = 0;
						tg[i][j][0]++;
					} else {
						if (this.fields[j].list[i]
								.equals(this.fields[j].list[a])) {
							tg[i][j][0]++;
							tg[a][j][0] = 0;
							m = a;
						} else {
							break;
						}
					}
				}
				for (int r = q + 1; r < y; r++) {
					int b = (colIndex[r] >= 0) ? colIndex[r] : r;
					try {
						if (i >= this.fields[j].list.length) {
							// tg[i][b][0] = 0;
							tg[i][j][0] = 0;
							int xx = 0;
							for (xx = 0; xx < i; xx++) {
								if (tg[xx][j][0] == 0)
									break;
							}
							if (xx > 0)
								tg[xx - 1][j][0]++;
							else
								tg[0][j][0]++;
						}
						// else if (
						// this.fields[j].list[i].equals(this.fields[b].list[i]))
						else if (this.fields[j].list.length > i
								&& this.fields[b].list.length > i
								&& this.fields[j].list[i]
										.equals(this.fields[b].list[i])) {
							tg[i][j][1]++;
							tg[i][b][1] = 0;
							n = b;
						} else if (this.fields[j].list.length <= i) {
							// tg[i][j][1]

						} else {
							break;
						}
					} catch (Exception e) {
						System.out.println("b:" + b + " j:" + j + " i:" + i);
						System.out.println(e.getMessage());
						e.printStackTrace(System.out);
						return "";
					}
				}
				for (int a = i + 1; a <= m; a++) {
					for (int v = j + 1; v <= n; v++) {
						int b = (colIndex[v] >= 0) ? colIndex[v] : v;
						tg[a][b][0] = 0;
						tg[a][b][1] = 0;
					}
				}
			}
		}
		for (int i = 0; i < tg.length; i++) {
			for (int j = 0; j < tg[i].length; j++)
				System.out.print(tg[i][j][0] + ":" + tg[i][j][1] + " ");
			System.out.println("");
		}
		buf.append("<div id=\"Page_leo\" "
				+ ((this.style == 2 || this.style == 4) ? "x:publishsource=\"Excel\""
						: "") + ">\n");
		buf.append(getStyleSheet(this.style));
		buf.append(writeTableTag(this.style));
		if (!this.empty(this.title)) {
			buf.append(writeTitle(this.style));
		}
		if (this.titleMemoList != null) {
			buf.append(writeTitleMemoList(this.style));
		}

		for (int i = 0; i < x; i++) {
			boolean ok = false;
			for (int m = 0; m < y; m++) {
				int j = (colIndex[m] >= 0) ? colIndex[m] : m;
				// j--;
				if (tg[i][j][0] != 0 && tg[i][j][1] != 0) {
					ok = true;
					break;
				}
			}
			if (ok) {
				if (this.style == this.STYLE_SHOW_DEFAULT)
					buf.append("      <tr class=\"trType\" align=\"center\"> \n");
				// else if (this.style == this.STYLE_SHOW_EXCEL)
				// buf.append("<tr> \n");
				// else if(this.style == this.STYLE_NONE)
				// buf.append("<tr> \n");
				else
					buf.append("<tr> \n");
				for (int k = 0; k < y; k++) {
					int j = (colIndex[k] >= 0) ? colIndex[k] : k;
					// j--;
					// System.out.print("."+j+" ");
					if (tg[i][j][0] != 0 && tg[i][j][1] != 0) {
						if (this.style == this.STYLE_NONE)
							buf.append("<td class='x2' rowspan=\""
									+ tg[i][j][0] + "\" colspan=\""
									+ tg[i][j][1] + "\">"
									+ this.fields[j].list[i] + "</td>\n");
						else if (this.style == this.STYLE_SHOW_DEFAULT)
							buf.append("<td rowspan=\""
									+ tg[i][j][0]
									+ "\" colspan=\""
									+ tg[i][j][1]
									+ "\""
									+ (this.empty(this.headerAlign) ? ""
											: " align=\"" + this.headerAlign
													+ "\"")
									+ (this.empty(this.headerValign) ? ""
											: " valign=\"" + this.headerValign
													+ "\"")
									+ (this.empty(this.headerFontSize) ? ""
											: " style='font-size:"
													+ this.headerFontSize
													+ "pt;'")
									+ (this.empty(this.headerStyleSheet) ? ""
											: " style='"
													+ this.headerStyleSheet
													+ "'")
									+ (this.empty(this.headerBGColor) ? ""
											: " bgcolor=\""
													+ this.headerBGColor + "\"")
									+ ">"
									+ (this.empty(this.headerColor) ? ""
											: "<font color=\""
													+ this.headerColor + "\">")
									+ this.fields[j].list[i]
									+ (this.empty(this.headerColor) ? "</td>\n"
											: "</font></td>\n"));
						else // if (this.style == this.STYLE_SHOW_EXCEL)
						{
							// String hvalue = this.fields[j].list[i];
							// boolean line = false;
							// if(hvalue != null)
							// {
							// if(hvalue.indexOf("id=") >= 0)
							// {
							// line = true;
							// hvalue = hvalue.substring(hvalue.indexOf(":"));
							// String hvalue1 =
							// hvalue.substring(0,hvalue.indexOf("////"));
							// String hvalue2 =
							// hvalue.substring(hvalue.indexOf("////")+4);
							// hvalue = "";
							// }
							// }
							buf.append("<td class='x2' rowspan=\""
									+ tg[i][j][0]
									+ "\" colspan=\""
									+ tg[i][j][1]
									+ "\""
									+ (this.empty(this.headerAlign) ? ""
											: " align=\"" + this.headerAlign
													+ "\"")
									+ (this.empty(this.headerValign) ? ""
											: " valign=\"" + this.headerValign
													+ "\"")
									+ (this.empty(this.headerBGColor) ? ""
											: " bgcolor=\""
													+ this.headerBGColor + "\"")
									+ (this.empty(this.headerFontSize) ? ""
											: " style='font-size:"
													+ this.headerFontSize
													+ "pt;'")
									+ (this.empty(this.headerStyleSheet) ? ""
											: " style='"
													+ this.headerStyleSheet
													+ "'")
									+ ">"
									+ (this.empty(this.headerColor) ? ""
											: "<font color=\""
													+ this.headerColor + "\">")
									+ this.fields[j].list[i]
									+ (this.empty(this.headerColor) ? "</td>\n"
											: "</font></td>\n"));
						}
					}
				}
				buf.append("      </tr>\n");
				// System.out.println("");
			}
		}
		int[][] marge = new int[rows][cols];
		for (int t = 0; t < cols; t++) {
			for (int s = 0; s < rows; s++) {
				marge[s][t] = 1;
			}
		}
		for (int m = rows - 1; m >= 0; m--) {
			for (int p = 0; p < cols; p++) {
				int n = (colIndex[p] >= 0) ? colIndex[p] : p;
				// n--;
				if (this.fields[n].colGroup > 0) {
					if (m > 0) {
						if (data[m][n].equals(data[m - 1][n])) {
							int prev = -1;
							for (int j = 0; j < this.fields[n].colGroup - 1; j++) {
								int k = riseGroup[j];
								if (!data[m][k].equals(data[m - 1][k])) {
									prev = j;
									break;
								}
							}
							if (prev < 0) {
								marge[m - 1][n] += marge[m][n];
								marge[m][n] = 0;
							} else {
								//
							}
						} else {
							//
						}
					} else {
						//
					}
				} else {
					//
				}
			}
		}
		// ////////////////////////////////////
		for (int i = 0; i < rows; i++) {
			if (this.style == this.STYLE_SHOW_DEFAULT)
				buf.append("<tr class=\"trType\""
						+ (this.empty(this.bodyAlign) ? "" : " align=\""
								+ this.bodyAlign + "\"")
						+ (this.empty(this.bodyValign) ? "" : " valign=\""
								+ this.bodyValign + "\"") + "> \n");
			else
				// if (this.style == this.STYLE_SHOW_EXCEL)
				buf.append("<tr>\n");

			for (int k = 0; k < cols; k++) {
				int j = (colIndex[k] >= 0) ? colIndex[k] : k;
				// j--;
				if (marge[i][j] > 0) {
					String sv = null;
					boolean code = false;
					if (!empty(this.getFieldDicCode(j))) {
						code = true;
						sv = Pub.getDictValueByCode(getFieldDicCode(j),
								data[i][j]);
					}

					if (this.style == this.STYLE_NONE) {
						buf.append("<td class='x3' "
								+ ((this.fields[j].colWidth > 0) ? "width=\""
										+ this.fields[j].colWidth
										+ ((this.fields[0].getColWidthType() == WIDTH_ADJUSTABLE) ? "%"
												: "") + "\" "
										: "")
								+ ((marge[i][j] > 1) ? " rowspan=\""
										+ marge[i][j] + "\"" : "") + ">\n");
					} else if (this.style == this.STYLE_SHOW_DEFAULT) {
						buf.append("<td "
								+ ((this.fields[j].colWidth > 0) ? "width=\""
										+ this.fields[j].colWidth
										+ ((this.fields[0].getColWidthType() == WIDTH_ADJUSTABLE) ? "%"
												: "") + "\" "
										: "")
								+ (this.empty(this.fields[j].getAlign()) ? ((this.fields[j].colType > 0) ? " align=\"right\""
										: "")
										: " align=\""
												+ this.fields[j].getAlign()
												+ "\"")
								+ (this.empty(this.fields[j].getValign()) ? ""
										: " valign=\""
												+ this.fields[j].getValign()
												+ "\"")
								+ ((marge[i][j] > 1) ? " rowspan=\""
										+ marge[i][j] + "\"" : "")
								+ (this.empty(this.borderColor) ? ""
										: " bordercolor=\"" + this.borderColor
												+ "\" ")
								+ ((this.empty(this.fields[j].bgColor)) ? ((this.bodyBGColors == null) ? (this
										.empty(this.bodyBGColor) ? ""
										: " bgcolor=\"" + this.bodyBGColor
												+ "\"") : " bgcolor=\""
										+ this.bodyBGColors[i
												% this.bodyBGColors.length]
										+ "\"")
										: " bgcolor=\""
												+ this.fields[j].bgColor + "\"")
								+ ">\n");
						if (!this.empty(this.fields[j].fgColor)
						// || !this.empty(this.bodyColor)
								|| (this.bodyColors != null))
							buf.append("<font "
									+ (this.empty(this.fields[j].fgColor) ? ((this.bodyColors == null) ? (this
											.empty(this.bodyColor) ? ""
											: " color='" + this.bodyColor + "'")
											: " color=\""
													+ this.bodyColors[i
															% this.bodyColors.length]
													+ "\"")
											: " color=\""
													+ this.fields[j].fgColor
													+ "\"") + ">\n");
					} else // if (this.style == this.STYLE_SHOW_EXCEL)
					{
						int colspan = 1;
						if (j < this.lockIndex) {
							for (int s = j + 1; s < lockIndex; s++) {
								if (data[i][j].equals(data[i][s])) {
									colspan++;
									k++;
								}
							}
						}

						buf.append("<td class='x3' "
								+ (code ? ("sv='"
										+ (data[i][j] == null ? "" : data[i][j]) + "'")
										: "")
								+ ((this.fields[j].colWidth > 0) ? "width=\""
										+ this.fields[j].colWidth
										+ ((this.fields[0].getColWidthType() == WIDTH_ADJUSTABLE) ? "%"
												: "") + "\" "
										: "")
								+ (this.empty(this.fields[j].getAlign()) ? ((this.fields[j].colType > 0) ? " align=\"right\""
										: "")
										: " align=\""
												+ this.fields[j].getAlign()
												+ "\"")
								+ (this.empty(this.fields[j].getValign()) ? ""
										: " valign=\""
												+ this.fields[j].getValign()
												+ "\"")
								+ ((marge[i][j] > 1) ? " rowspan=\""
										+ marge[i][j] + "\"" : "")
								+ " colspan=\""
								+ colspan
								+ "\""
								+ (this.empty(this.borderColor) ? ""
										: " bordercolor=\"" + this.borderColor
												+ "\" ")
								+ ((this.empty(this.fields[j].bgColor)) ? ((this.bodyBGColors == null) ? ""
										: " bgcolor=\""
												+ this.bodyBGColors[i
														% this.bodyBGColors.length]
												+ "\"")
										: " bgcolor=\""
												+ this.fields[j].bgColor + "\"")
								+ ">\n");
						if (!this.empty(this.fields[j].fgColor)
								|| (this.bodyColors != null))
							buf.append("<font "
									+ (this.empty(this.fields[j].fgColor) ? ((this.bodyColors == null) ? ""
											: " color=\""
													+ this.bodyColors[i
															% this.bodyColors.length]
													+ "\"")
											: " color=\""
													+ this.fields[j].fgColor
													+ "\"") + ">\n");
					}
					if (this.fields[j].colLink != null
							&& this.style != this.STYLE_PRINT_DEFAULT
							&& this.style != this.STYLE_PRINT_EXCEL) {
						String href = new String(this.fields[j].colLink);
						if (this.fields[j].linkParameterNum > 0) {
							if (href.indexOf("?") == -1)
								href += "?";
							else
								href += "&";
						}
						if (href.charAt(href.length() - 1) != '&')
							href += "?";
						href += "y=" + i;
						for (int p = 0; p < this.fields[j].linkParameterNum; p++) {
							href += this.fields[j].linkParameter[p][0] + "=";
							for (x = 0; x < allcols; x++) {
								y = (colIndex[x] >= 0) ? colIndex[x] : x;
								// y--;
								if (this.fields[y].colName
										.equals(this.fields[j].linkParameter[p][1])) {
									href += data[i][y];
								}
							}
							if (p < (this.fields[j].linkParameterNum - 1))
								href += "&";
						}
						buf.append("<a href=\""
								+ href
								+ "\">"
								+ ((this.fields[j].colType > 0) ? ((!this
										.empty(data[i][j])) ? this
										.currency_format(data[i][j],
												this.fields[j].colType) : "")
										: code ? empty(sv) ? (data[i][j] == null ? ""
												: data[i][j])
												: sv
												: (data[i][j] == null ? ""
														: data[i][j]))
								+ "</a>\n");
					} else {
						buf.append((this.fields[j].colType > 0) ? ((!this
								.empty(data[i][j])) ? this.currency_format(
								data[i][j], this.fields[j].colType) : "")
								: code ? empty(sv) ? (data[i][j] == null ? ""
										: data[i][j]) : sv
										: (data[i][j] == null ? "" : data[i][j]));
					}
					if (!this.empty(this.fields[j].fgColor)
							|| (this.bodyColors != null))
						buf.append("</font>\n");
					buf.append("</td>\n");
				}
			}
			buf.append("</tr>\n");
		}
		// /////////////////////sum///////////////////////////
		boolean dosum = false;
		for (int i = 0; i < cols; i++) {
			if (!this.empty(this.fields[i].colSum)) {
				dosum = true;
				break;
			}
		}

		String[] sum = new String[cols];
		if (dosum)
			for (int init = 0; init < cols; init++)
				sum[init] = new String("");
		if (dosum) {
			if (this.style == this.STYLE_SHOW_DEFAULT)
				buf.append("      <tr class=\"trType\" align=\"center\"> \n");
			else
				// if (this.style == this.STYLE_SHOW_EXCEL)
				buf.append("      <tr> \n");
		}
		if (dosum)
			for (int k = 0; k < cols; k++) {
				int j = (colIndex[k] >= 0) ? colIndex[k] : k;
				boolean ifCurr = false;
				if (!this.empty(this.fields[j].colSum)) {
					String meth = this.fields[j].colSum.substring(0,
							this.fields[j].colSum.indexOf(":"));
					int scale = this.toInt(this.fields[j].colSum
							.substring(this.fields[j].colSum.indexOf(":") + 1));
					if (meth.equalsIgnoreCase("sum")) {
						for (int i = 0; i < rows; i++) {
							if (!this.empty(data[i][j])) {
								sum[j] = String.valueOf(this.toNum(sum[j])
										+ this.toNum(data[i][j]));
							} else {
								// continue;
							}
						}
						if (scale == 0) {
							sum[j] = String.valueOf(Math.round(this
									.toNum(sum[j])));
						} else {
							ifCurr = true;
							sum[j] = String.valueOf(Math.round(this
									.toNum(sum[j]) * Math.pow(10, scale))
									/ Math.pow(10, scale));
						}
					} else if (meth.equalsIgnoreCase("num")) {
						for (int i = 0; i < rows; i++) {
							if (!this.empty(data[i][j])) {
								sum[j] = String.valueOf(this.toInt(sum[j]) + 1);
							} else {
								// continue;
							}
						}
						if (scale == 0) {
							sum[j] = String.valueOf(Math.round(this
									.toNum(sum[j])));
						} else {
							sum[j] = String.valueOf(Math.round(this
									.toNum(sum[j]) * Math.pow(10, scale))
									/ Math.pow(10, scale));
						}
					} else if (meth.equalsIgnoreCase("avg")) {
						for (int i = 0; i < rows; i++) {
							if (!this.empty(data[i][j])) {
								sum[j] = String.valueOf(this.toNum(sum[j])
										+ this.toNum(data[i][j]));
							} else {
								// continue;
							}
							if (i == (rows - 1)) {
								sum[j] = String.valueOf(this.toNum(sum[j])
										/ rows);
							}
						}
						if (scale == 0) {
							sum[j] = String.valueOf(Math.round(this
									.toNum(sum[j])));
						} else {
							ifCurr = true;
							sum[j] = String.valueOf(Math.round(this
									.toNum(sum[j]) * Math.pow(10, scale))
									/ Math.pow(10, scale));
						}
					} else {
					}
				}
				if (k == 0) {
					if (this.style == this.STYLE_NONE)
						buf.append("<td class='x2'>合计</td>\n");
					else if (this.style == this.STYLE_SHOW_DEFAULT)
						buf.append("<td "
								+ (this.empty(this.headerAlign) ? ""
										: " align=\"" + this.headerAlign + "\"")
								+ (this.empty(this.headerValign) ? ""
										: " valign=\"" + this.headerValign
												+ "\"")
								+ (this.empty(this.headerFontSize) ? ""
										: " style='font-size:"
												+ this.headerFontSize + "pt;'")
								+ (this.empty(this.headerStyleSheet) ? ""
										: " style='" + this.headerStyleSheet
												+ "'")
								+ (this.empty(this.headerBGColor) ? ""
										: " bgcolor=\"" + this.headerBGColor
												+ "\"")
								+ ">"
								+ (this.empty(this.headerColor) ? "合计"
										: "<font color=\"" + this.headerColor
												+ "\">" + "合计</font>")
								+ "</td>\n");
					else
						// if (this.style == this.STYLE_SHOW_EXCEL)
						buf.append("<td class='x2' "
								+ (this.empty(this.headerAlign) ? ""
										: " align=\"" + this.headerAlign + "\"")
								+ (this.empty(this.headerValign) ? ""
										: " valign=\"" + this.headerValign
												+ "\"")
								+ (this.empty(this.headerFontSize) ? ""
										: " style='font-size:"
												+ this.headerFontSize + "pt;'")
								+ (this.empty(this.headerStyleSheet) ? ""
										: " style='" + this.headerStyleSheet
												+ "'")
								+ (this.empty(this.headerBGColor) ? ""
										: " bgcolor=\"" + this.headerBGColor
												+ "\"")
								+ " colspan='"
								+ (this.lockIndex > 0 ? this.lockIndex : 1)
								+ "'"
								+ ">"
								+ (this.empty(this.headerColor) ? "合计"
										: "<font color=\"" + this.headerColor
												+ "\">" + "合计</font>")
								+ "</td>\n");
				} else {
					if (k < this.lockIndex)
						continue;
					if (this.style == this.STYLE_NONE)
						buf.append("<td class='x2'>"
								+ ((this.fields[j].colType > 0 && ifCurr) ? this
										.currency_format(this.toNum(sum[j]))
										: sum[j]) + "</td>\n");
					else if (this.style == this.STYLE_SHOW_DEFAULT)
						buf.append("<td "
								+ (this.empty(this.headerAlign) ? ""
										: " align=\"" + this.headerAlign + "\"")
								+ (this.empty(this.headerValign) ? ""
										: " valign=\"" + this.headerValign
												+ "\"")
								+ (this.empty(this.headerFontSize) ? ""
										: " style='font-size:"
												+ this.headerFontSize + "pt;'")
								+ (this.empty(this.headerStyleSheet) ? ""
										: " style='" + this.headerStyleSheet
												+ "'")
								+ (this.empty(this.headerBGColor) ? ""
										: " bgcolor=\"" + this.headerBGColor
												+ "\"")
								+ ">"
								+ (this.empty(this.headerColor) ? ""
										: "<font color=\"" + this.headerColor
												+ "\">")
								+ ((this.fields[j].colType > 0 && ifCurr) ? this
										.currency_format(this.toNum(sum[j]))
										: sum[j])
								+ (this.empty(this.headerColor) ? ""
										: "</font>") + "</td>\n");
					else // if (this.style == this.STYLE_SHOW_EXCEL)
					{
						int scale = this.empty(this.fields[j].colSum) ? 0
								: this.toInt(this.fields[j].colSum
										.substring(this.fields[j].colSum
												.indexOf(":") + 1));
						buf.append("<td class='x2' "
								+ (this.empty(this.headerAlign) ? ""
										: " align=\"" + this.headerAlign + "\"")
								+ (this.empty(this.headerValign) ? ""
										: " valign=\"" + this.headerValign
												+ "\"")
								+ (this.empty(this.headerFontSize) ? ""
										: " style='font-size:"
												+ this.headerFontSize + "pt;'")
								+ (this.empty(this.headerStyleSheet) ? ""
										: " style='" + this.headerStyleSheet
												+ "'")
								+ (this.empty(this.headerBGColor) ? ""
										: " bgcolor=\"" + this.headerBGColor
												+ "\"")
								+ ">"
								+ (this.empty(this.headerColor) ? ""
										: "<font color=\"" + this.headerColor
												+ "\">")
								+ ((this.fields[j].colType > 0 && ifCurr) ? this
										.currency_format(sum[j], scale)
										: sum[j])
								+ (this.empty(this.headerColor) ? ""
										: "</font>") + "</td>\n");
					}
				}
			}
		if (dosum)
			buf.append("</tr>\n");
		// ////////////////////////////////////////////////
		if (this.attachmemo != null)
			buf.append(writeAttachMemo());
		if (!this.showTail) {
			// don't show table tail;
		} else if (this.style == this.STYLE_NONE) {
			buf.append("<tr>\n");
			buf.append("<td class=\"x_tail\" colspan=\"" + cols + "\">");
			buf.append("共查到[" + rows + "]条记录");
			buf.append("</td>\n</tr>\n");
		} else if (this.style == this.STYLE_SHOW_DEFAULT) {
			buf.append("       <tr class=\"trType\">\n");
			buf.append("        <td colspan=\""
					+ cols
					+ "\""
					+ (this.empty(this.tailBGColor) ? "" : " bgcolor=\""
							+ this.tailBGColor + "\"")
					+ (this.empty(this.tailAlign) ? "" : " align=\""
							+ this.tailAlign + "\"")
					+ (this.empty(this.tailFontSize) ? ""
							: " style='font-size:" + this.tailFontSize
									+ "pt;' ")
					+ (this.empty(this.tailStyleSheet) ? "" : " style='"
							+ this.tailStyleSheet + "' ")
					+ (this.empty(this.tailValign) ? "" : " valign=\""
							+ this.tailValign + "\"") + ">");
			buf.append(this.empty(this.tailColor) ? "" : "<font color=\""
					+ this.tailColor + "\">\n");
			buf.append("共查到[" + rows + "]条记录");
			buf.append(this.empty(this.tailColor) ? "" : "</font>\n");
			buf.append("</td>\n</tr>\n");
		} else // if (this.style == this.STYLE_SHOW_EXCEL)
		{
			buf.append("<tr>\n");
			buf.append("<td class='x_tail' colspan=\""
					+ cols
					+ "\""
					+ (this.empty(this.tailBGColor) ? "" : " bgcolor=\""
							+ this.tailBGColor + "\"")
					+ (this.empty(this.tailAlign) ? "" : " align=\""
							+ this.tailAlign + "\"")
					+ (this.empty(this.tailValign) ? "" : " valign=\""
							+ this.tailValign + "\"")
					+ (this.empty(this.tailFontSize) ? ""
							: " style='font-size:" + this.tailFontSize + "pt;'")
					+ (this.empty(this.tailStyleSheet) ? "" : " style='"
							+ this.tailStyleSheet + "' ") + ">");
			buf.append(this.empty(this.tailColor) ? "" : "<font color=\""
					+ this.tailColor + "\">\n");
			buf.append("共查到[" + rows + "]条记录");
			buf.append(this.empty(this.tailColor) ? "" : "</font>\n");
			buf.append("</td>\n</tr>\n");
		}
		if (this.tailMemoList != null) {
			buf.append(writeTailMemoList(this.style));
		}
		buf.append(writeEndTableTag(this.style));
		buf.append("</div>\n");
		return buf.toString();
	}

	// //////////////////////////////////////////////////////////////////////////////////////
	public static void main(String argv[]) {
		try {
			// QueryTable table = new
			// QueryTable("select id,uname,tname,tdept,to_char(sysdate,'yyyy-mm-dd') from cuiec_mesgstat_t order by id");
			QueryTable table = new QueryTable();
			table.addField(0, "A->D->序号", 0, 1, 25, null, 1, "test.jsp");
			table.addField(1, "A->E->发送人", 0, 4, 25, null, 0, null);
			table.addField(2, "C->接收人", 0, 2, 25, null, 0, null);
			table.addField(3, "C->部门", 0, 3, 25, null, 0, null);
			table.addField(4, "日期", 0, 0, 0, null, 0, null);

			String str = table.listdetail();

			System.out.println("rows:" + table.rows);
			System.out.println("cols:" + table.cols);
			System.out.println("allcols:" + table.allcols);
			System.out.println("field 0:" + table.fields[0].colName);
			System.out.println("para:" + table.fields[0].linkParameterNum);

			// System.out.print(table.listdetail());
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
		System.out.println("ok..");
	}

	private String getStyleSheet(int st) {
		StringBuffer buf = new StringBuffer();
		switch (st) {
		case STYLE_NONE:
			if (this.empty(this.styleSheet)) {
				buf.append("<style>\n");
				buf.append("<!--\n");
				buf.append(".x_table\n");
				buf.append("	{border-collapse: collapse;}\n");
				buf.append("-->\n");
				buf.append("</style>\n");
			} else
				buf.append(this.styleSheet);
			break;
		case STYLE_SHOW_EXCEL:
			buf.append("<style>\n");
			buf.append("<!--\n");
			buf.append(".x_table\n");
			buf.append("	{border-collapse: collapse;}\n");
			buf.append(".x_header\n");
			buf.append("{\n");
			buf.append("	color:windowtext;\n");
			// buf.append("	font-size:12.0pt;\n");
			buf.append("	font-weight:700;\n");
			buf.append("	font-style:normal;\n");
			buf.append("	text-decoration:none;\n");
			buf.append("	font-family:宋体;\n");
			buf.append("	text-align:center;\n");
			// buf.append("	vertical-align:middle;\n");
			buf.append("	border:none;\n");
			buf.append("	white-space:nowrap;");
			buf.append("}\n");
			buf.append(".x_tail\n");
			buf.append("{\n");
			buf.append("	color:windowtext;\n");
			buf.append("	font-size:9.0pt;\n");
			buf.append("	font-weight:400;\n");
			buf.append("	font-style:normal;\n");
			buf.append("	text-decoration:none;\n");
			buf.append("	font-family:宋体;\n");
			// buf.append("	text-align:right;\n");
			// buf.append("	vertical-align:middle;\n");
			buf.append("	border:"
					+ ((this.borderWidth > 0) ? String
							.valueOf(this.borderWidth * 1.0 / 2.0) : "1")
					+ "px solid "
					+ (this.empty(this.borderColor) ? "windowtext"
							: this.borderColor) + ";\n");
			buf.append("	white-space:nowrap;");
			buf.append("}\n");
			buf.append(".x1\n");
			buf.append("	{padding-top:1px;\n");
			buf.append("	padding-right:1px;\n");
			buf.append("	padding-left:1px;\n");
			buf.append("	mso-ignore:padding;\n");
			buf.append("	color:windowtext;\n");
			buf.append("	font-family:宋体;\n");
			buf.append("	mso-generic-font-family:auto;\n");
			buf.append("	mso-font-charset:134;\n");
			buf.append("	mso-number-format:General;\n");
			buf.append("	border:"
					+ ((this.borderWidth > 0) ? String
							.valueOf(this.borderWidth * 1.0 / 2.0) : "1")
					+ "px solid "
					+ (this.empty(this.borderColor) ? "windowtext"
							: this.borderColor) + ";\n");
			buf.append("	mso-background-source:auto;\n");
			buf.append("	mso-pattern:auto;\n");
			buf.append("	white-space:nowrap;}\n");
			buf.append(".x2\n");
			buf.append("	{padding-top:2px;\n");
			buf.append("	padding-right:1px;\n");
			buf.append("	padding-left:1px;\n");
			buf.append("	mso-ignore:padding;\n");
			// buf.append("	color:windowtext;\n");
			// buf.append("	font-size:10.5pt;\n");
			buf.append("	font-weight:400;\n");
			buf.append("	font-style:normal;\n");
			buf.append("	text-decoration:none;\n");
			buf.append("	font-family:宋体;\n");
			buf.append("	text-align:center;\n");
			// buf.append("	vertical-align:middle;\n");
			buf.append("	border:"
					+ ((this.borderWidth > 0) ? String
							.valueOf(this.borderWidth * 1.0 / 2.0) : "1")
					+ "px solid "
					+ (this.empty(this.borderColor) ? "windowtext"
							: this.borderColor) + ";\n");
			buf.append("	white-space:nowrap;}\n");
			buf.append(".x3\n");
			buf.append("	{padding-top:1px;\n");
			buf.append("	padding-right:1px;\n");
			buf.append("	padding-left:1px;\n");
			buf.append("	mso-ignore:padding;\n");
			// buf.append("	color:windowtext;\n");
			// buf.append("	font-size:9.0pt;\n");
			buf.append("	font-weight:400;\n");
			buf.append("	font-style:normal;\n");
			buf.append("	text-decoration:none;\n");
			buf.append("	font-family:宋体;\n");
			buf.append("	text-align:center;\n");
			// buf.append("	vertical-align:middle;\n");
			buf.append("	border:"
					+ ((this.borderWidth > 0) ? String
							.valueOf(this.borderWidth * 1.0 / 2.0) : "1")
					+ "px solid "
					+ (this.empty(this.borderColor) ? "windowtext"
							: this.borderColor) + ";\n");
			buf.append("	white-space:normal;}\n");
			buf.append(".x_tm\n");
			buf.append("	{padding-top:1px;\n");
			buf.append("	padding-right:1px;\n");
			buf.append("	padding-left:1px;\n");
			buf.append("	mso-ignore:padding;\n");
			buf.append("	color:windowtext;\n");
			buf.append("	font-size:9.0pt;\n");
			buf.append("	font-weight:400;\n");
			buf.append("	font-style:normal;\n");
			buf.append("	text-decoration:none;\n");
			buf.append("	font-family:宋体;\n");
			// buf.append("	text-align:left;\n");
			// buf.append("	vertical-align:middle;\n");
			buf.append("	border:"
					+ ((!this.showTailMemoBorder) ? "0"
							: ((this.borderWidth > 0) ? String
									.valueOf(this.borderWidth * 1.0 / 2.0)
									: "1"))
					+ "px solid "
					+ (this.empty(this.borderColor) ? "windowtext"
							: this.borderColor) + ";\n");
			buf.append("	white-space:nowrap;}\n");
			buf.append(".x_hm\n");
			buf.append("	{padding-top:1px;\n");
			buf.append("	padding-right:1px;\n");
			buf.append("	padding-left:1px;\n");
			buf.append("	mso-ignore:padding;\n");
			buf.append("	color:windowtext;\n");
			buf.append("	font-size:9.0pt;\n");
			buf.append("	font-weight:400;\n");
			buf.append("	font-style:normal;\n");
			buf.append("	text-decoration:none;\n");
			buf.append("	font-family:宋体;\n");
			// buf.append("	text-align:right;\n");
			// buf.append("	vertical-align:middle;\n");
			// buf.append("	border:none;\n");
			buf.append("	border:"
					+ ((!this.showTitleMemoBorder) ? "0"
							: ((this.borderWidth > 0) ? String
									.valueOf(this.borderWidth * 1.0 / 2.0)
									: "1"))
					+ "px solid "
					+ (this.empty(this.borderColor) ? "windowtext"
							: this.borderColor) + ";\n");
			buf.append("	white-space:nowrap;}\n");
			buf.append(".x_hm_d\n");
			buf.append("	{padding-top:1px;\n");
			buf.append("	padding-right:1px;\n");
			buf.append("	padding-left:1px;\n");
			buf.append("	mso-ignore:padding;\n");
			buf.append("	color:windowtext;\n");
			buf.append("	font-size:9.0pt;\n");
			buf.append("	font-weight:400;\n");
			buf.append("	font-style:normal;\n");
			buf.append("	text-decoration:none;\n");
			buf.append("	font-family:宋体;\n");
			buf.append("	border:none;\n");
			buf.append("	white-space:nowrap;}\n");
			buf.append("	\n");
			buf.append("-->\n");
			buf.append("</style>\n");
			if (!this.empty(this.styleSheet))
				buf.append(this.styleSheet);
			break;
		case STYLE_CUSTOMER:
		default:
			if (!this.empty(this.styleSheet))
				buf.append(this.styleSheet);
			break;
		}
		return buf.toString();
	}

	private String getTableScript() {
		return "<script language=\"JavaScript\">\n<!--\nfunction doMouseOver(tab)\n"
				+ "{\n"
				+ "    window.event.cancelBubble = true;\n"
				+ "    var obj = event.srcElement;\n"
				+ "    if(obj.tagName == \"TABLE\") return;\n"
				+ "    if(obj.tagName == \"TD\")\n"
				+ "    {\n"
				+ "        obj.style.backgroundColor = \"wheat\";\n"
				+ "    }\n"
				+ "}\n"
				+ "function doMouseClick(tab)\n"
				+ "{\n"
				+ " window.event.cancelBubble = true;\n"
				+ " var obj = event.srcElement;\n"
				+ " if(obj.tagName == \"TABLE\") return;\n"
				+ " if(obj.tagName == \"TD\")\n"
				+ " {\n//alert(obj.innerHTML);\n"
				+ "  if(obj.innerHTML.indexOf(\"</A>\")>0)\n"
				+ "  {\n//alert('ok');\n"
				+ "   var child = obj;\n"
				+ "   while(true)\n"
				+ "   {\n"
				+ "    child = child.firstChild;\n"
				+ "    if(child.tagName == \"A\")\n"
				+ "      break;\n"
				+ "   }\n"
				+ "   location.href = child.href;\n"
				+ "  }\n"
				+ " }\n"
				+ "}\n"
				+ "function doMouseOut(tab)\n"
				+ "{\n"
				+ "    window.event.cancelBubble = true;\n"
				+ "    var obj = event.srcElement;\n"
				+ "    if(obj.tagName == \"TABLE\") return;\n"
				+ "    if(obj.tagName == \"TD\")\n"
				+ "    {\n"
				+ "        if(obj.selected==\"true\") return;\n"
				+ "        obj.style.backgroundColor = \"transparent\";\n"
				+ "    }\n" + "}\n-->\n</script>";
	}

	private String writeTableTag(int st) {
		String res = null;
		if (st == this.STYLE_NONE)
			res = "<table id=\"FreeTable\" class='x_table' align=\""
					+ this.align
					+ "\""
					+ (this.empty(this.valign) ? "" : "valign=\"" + this.valign
							+ "\"")
					+ " border=\""
					+ this.borderWidth
					+ "\" cellpadding=\"0\" cellspacing=\"0\" width=\""
					+ this.width
					+ ((this.widthType == WIDTH_ADJUSTABLE) ? "%" : "")
					+ "\""
					+ (this.empty(this.borderColor) ? "" : " bordercolor=\""
							+ this.borderColor + "\"")
					+ (this.empty(this.bgColor) ? "" : " bgcolor=\""
							+ this.bgColor + "\"")
					+ (this.empty(this.bodyFontSize) ? ""
							: " style='font-size:" + this.bodyFontSize + "pt;'")
					+ (this.empty(this.bodyStyleSheet) ? "" : " style='"
							+ this.bodyStyleSheet + "'")
					+ (this.empty(this.bodyAlign) ? "" : " style='text-align:"
							+ this.bodyAlign + "'")
					+ (this.empty(this.bodyColor) ? "" : " style='color:"
							+ this.bodyColor + "'")
					+ (this.cellLink ? " onMouseOver=\"doMouseOver(this);\" onMouseOut=\"doMouseOut(this);\" onClick=\"doMouseClick(this);\""
							: "") + ">\n";
		else if (st == this.STYLE_SHOW_DEFAULT) {
			res = "<table align=\""
					+ this.align
					+ "\""
					+ (this.empty(this.valign) ? "" : "valign=\"" + this.valign
							+ "\"")
					+ " border=\""
					+ this.borderWidth
					+ "\" cellpadding=\"0\" cellspacing=\"0\" width=\""
					+ this.width
					+ ((this.widthType == WIDTH_ADJUSTABLE) ? "%" : "")
					+ "\""
					+ (this.empty(this.borderColor) ? "" : " bordercolor=\""
							+ this.borderColor + "\"")
					+ (this.empty(this.bgColor) ? "" : " bgcolor=\""
							+ this.bgColor + "\"")
					+ (this.empty(this.bodyFontSize) ? ""
							: " style='font-size:" + this.bodyFontSize + "pt;'")
					+ (this.empty(this.bodyStyleSheet) ? "" : " style='"
							+ this.bodyStyleSheet + "'")
					+ (this.empty(this.bodyAlign) ? "" : " style='text-align:"
							+ this.bodyAlign + "'")
					+ (this.empty(this.bodyColor) ? "" : " style='color:"
							+ this.bodyColor + "'")
					+ (this.cellLink ? " onMouseOver=\"doMouseOver(this);\" onMouseOut=\"doMouseOut(this);\" onClick=\"doMouseClick(this);\""
							: "") + ">\n";
			res += "<tr>\n";
			res += "  <td width=\"100%\"> \n";
			res += "    <table id=\"FreeTable\" border=\"0\" cellpadding=\"1\" cellspacing=\"1\" width=\"100%\" "
					+ (this.empty(this.bgColor) ? "" : " bgcolor=\""
							+ this.bgColor + "\"")
					+ (this.empty(this.bodyFontSize) ? ""
							: " style='font-size:" + this.bodyFontSize + "pt;'")
					+ (this.empty(this.bodyStyleSheet) ? "" : " style='"
							+ this.bodyStyleSheet + "'")
					+ (this.empty(this.bodyAlign) ? "" : " style='text-align:"
							+ this.bodyAlign + "'")
					+ (this.empty(this.bodyColor) ? "" : " style='color:"
							+ this.bodyColor + "'")
					+ (this.cellLink ? " onMouseOver=\"doMouseOver(this);\" onMouseOut=\"doMouseOut(this);\" onClick=\"doMouseClick(this);\""
							: "") + ">\n";
		}

		else if (st == this.STYLE_SHOW_EXCEL)
			res = "<table id=\"FreeTable\" class='x_table' "
					+ (this.empty(this.align) ? "" : " align=\"" + this.align
							+ "\" ")
					+ (this.empty(this.valign) ? "" : " valign=\""
							+ this.valign + "\" ")
					+
					// "border=\"" + this.borderWidth +
					// "\" cellpadding=\"0\" cellspacing=\"0\" " +
					"border=\"0\" cellpadding=\"0\" cellspacing=\"0\" "
					+ "width=\""
					+ this.width
					+ ((this.widthType == WIDTH_ADJUSTABLE) ? "%" : "")
					+ "\" "
					+ (this.empty(this.borderColor) ? "" : "bordercolor=\""
							+ this.borderColor + "\" ")
					+ (this.empty(this.bodyFontSize) ? ""
							: " style='font-size:" + this.bodyFontSize + "pt;'")
					+ (this.empty(this.bodyStyleSheet) ? "" : " style='"
							+ this.bodyStyleSheet + "'")
					+ // style="CURSOR: hand; table-layout:fixed;"
					(this.empty(this.bodyAlign) ? "" : " style='text-align:"
							+ this.bodyAlign + "'")
					+ (this.empty(this.bgColor) ? "" : " bgcolor=\""
							+ this.bgColor + "\" ")
					+ (this.empty(this.bodyColor) ? "" : " style='color:"
							+ this.bodyColor + "'")
					+ (this.cellLink ? " onMouseOver=\"doMouseOver(this);\" onMouseOut=\"doMouseOut(this);\" onClick=\"doMouseClick(this);\""
							: "") +
					// " style='border-collapse:collapse;border-style:solid;'" +
					">\n";
		else if (st == this.STYLE_CUSTOMER)
			res = "<table id=\"FreeTable\" class='x_table' border='"
					+ this.borderWidth
					+ "' cellpadding='0' cellspacing='0' "
					+ " align='"
					+ this.align
					+ "' valign='"
					+ this.valign
					+ "' "
					+ "width=\""
					+ this.width
					+ ((this.widthType == WIDTH_ADJUSTABLE) ? "%" : "")
					+ "\" "
					+ (this.empty(this.borderColor) ? "" : "bordercolor=\""
							+ this.borderColor + "\" ")
					+ (this.empty(this.bodyFontSize) ? ""
							: " style='font-size:" + this.bodyFontSize + "pt;'")
					+ (this.empty(this.bodyStyleSheet) ? "" : " style='"
							+ this.bodyStyleSheet + "'")
					+ (this.empty(this.bodyAlign) ? "" : " style='text-align:"
							+ this.bodyAlign + "'")
					+ (this.empty(this.bgColor) ? "" : " bgcolor=\""
							+ this.bgColor + "\" ")
					+ (this.empty(this.bodyColor) ? "" : " style='color:"
							+ this.bodyColor + "'")
					+ (this.cellLink ? " onMouseOver=\"doMouseOver(this);\" onMouseOut=\"doMouseOut(this);\" onClick=\"doMouseClick(this);\""
							: "") + ">\n";

		return this.cellLink ? this.getTableScript() + res : res;
	}

	private String writeTitle(int st) {
		StringBuffer buf = new StringBuffer();
		if (st == this.STYLE_SHOW_DEFAULT) {
			buf.append("      <tr class=\"trType\""
					+ (this.empty(this.titleAlign) ? "" : " align=\""
							+ this.titleAlign + "\"")
					+ (this.empty(this.titleValign) ? "" : " valign=\""
							+ this.titleValign + "\" ")
					+ (this.empty(this.titleBGColor) ? ">\n" : " bgcolor=\""
							+ this.titleBGColor + "\"> \n"));
			buf.append("        <td "
					+ (this.empty(this.titleFontSize) ? ""
							: " style=\"font-size:" + this.titleFontSize
									+ "pt;\"") + " colspan=\"" + cols
					+ "\"> \n");
			buf.append("<b>"
					+ (this.empty(this.titleColor) ? "" : "<font color=\""
							+ this.titleColor + "\">")
					+ ((this.title == null) ? "" : this.title)
					+ (this.empty(this.titleColor) ? "" : "</font>") + "</b>");
			buf.append("</td></tr>\n");
		} else if (st == this.STYLE_NONE) {
			if (this.title != null && !this.title.trim().equals("")) {
				buf.append("<tr>\n");
				buf.append("<td class='x_header' colspan='" + cols + "'>");
				buf.append(this.title);
				buf.append("</td>\n");
				buf.append("</tr>\n");
			}
		} else if (st == this.STYLE_CUSTOMER) {
			if (!this.empty(this.title)) {
				buf.append("<tr>\n");
				buf.append("<td class='x_header' "
						+ (this.empty(this.titleAlign) ? "" : " align=\""
								+ this.titleAlign + "\"")
						+ (this.empty(this.titleValign) ? "" : " valign=\""
								+ this.titleValign + "\" ")
						+ (this.empty(this.titleBGColor) ? "" : " bgcolor=\""
								+ this.titleBGColor + "\" ")
						+ (this.empty(this.titleFontSize) ? ""
								: " style='font-size:" + this.titleFontSize
										+ "pt;'")
						+ (this.empty(this.titleStyleSheet) ? "" : " style='"
								+ this.titleStyleSheet + "'") + " colspan=\""
						+ cols + "\">\n");
				buf.append((this.empty(this.titleColor) ? "" : "<font color=\""
						+ this.titleColor + "\">")
						+ ((this.title == null) ? "" : this.title)
						+ (this.empty(this.titleColor) ? "" : "</font>"));
				buf.append("</td>\n");
				buf.append("</tr>\n");
			}
		} else if (st == this.STYLE_SHOW_EXCEL) {
			buf.append("<tr>\n");
			buf.append("<td class='x_header' "
					+ (this.empty(this.titleAlign) ? "" : " align=\""
							+ this.titleAlign + "\"")
					+ (this.empty(this.titleValign) ? "" : " valign=\""
							+ this.titleValign + "\" ")
					+ (this.empty(this.titleBGColor) ? "" : " bgcolor=\""
							+ this.titleBGColor + "\" ")
					+ (this.empty(this.titleFontSize) ? ""
							: " style='font-size:" + this.titleFontSize
									+ "pt;'")
					+ (this.empty(this.titleStyleSheet) ? "" : " style='"
							+ this.titleStyleSheet + "'") + " colspan=\""
					+ cols + "\">\n");
			buf.append((this.empty(this.titleColor) ? "" : "<font color=\""
					+ this.titleColor + "\">")
					+ ((this.title == null) ? "" : this.title)
					+ (this.empty(this.titleColor) ? "" : "</font>"));
			buf.append("</td>\n");
			buf.append("</tr>\n");
		}
		return buf.toString();
	}

	private String writeTitleMemoList(int st) {
		if (this.titleMemoList == null)
			return "";
		StringBuffer buf = new StringBuffer();
		if (st == this.STYLE_NONE) {
			Iterator itor = this.titleMemoList.iterator();
			while (itor.hasNext()) {
				ArrayList list = (ArrayList) itor.next();
				buf.append("<tr><td class='" + list.get(1).toString()
						+ "' colspan=\"" + cols + "\"" + ">"
						+ list.get(0).toString() + "</td></tr>\n");
			}
		} else if (st == this.STYLE_SHOW_EXCEL) {
			Iterator itor = this.titleMemoList.iterator();
			while (itor.hasNext()) {
				ArrayList list = (ArrayList) itor.next();
				String[] mlist = (String[]) list.get(0);
				String[] slist = (String[]) list.get(1);
				if (mlist == null)
					continue;
				buf.append("<tr><td class='x_hm' colspan=\""
						+ cols
						+ "\""
						+ (this.empty(this.titleMemoAlign) ? "" : " align='"
								+ this.titleMemoAlign + "'")
						+ (this.empty(this.titleMemoValign) ? "" : " valign='"
								+ this.titleMemoValign + "'")
						+ (this.empty(this.titleMemoFontSize) ? ""
								: " style='font-size:" + this.titleMemoFontSize
										+ "pt;'")
						+ (this.empty(this.titleMemoStyleSheet) ? ""
								: " style='" + this.titleMemoStyleSheet + "'")
						+ (this.empty(this.titleMemoColor) ? ""
								: " style='color:" + this.titleMemoColor + "'")
						+ (this.empty(this.titleMemoBgColor) ? ""
								: " bgcolor='" + this.titleMemoBgColor + "'")
						+ ">");
				buf.append("<table border='0' width='100%' cellpadding=\"0\" cellspacing=\"0\"><tr>\n");
				int rate = Math.round(100 / mlist.length);
				for (int i = 0; i < mlist.length; i++) {
					buf.append("<td width='"
							+ rate
							+ "%' align='"
							+ ((i == 0) ? "left"
									: ((i == (mlist.length - 1) ? "right"
											: "center")))
							+ "' valign='bottom' class='"
							+ ((i >= slist.length) ? "x_hm_d" : slist[i])
							+ "'>");
					buf.append(mlist[i]);
					buf.append("</td>\n");
				}
				buf.append("</tr></table></td></tr>\n");

				// buf.append("<tr><td class='" + list.get(1).toString() +
				// "' colspan=\"" +
				// cols + "\"" +
				// (this.empty(this.titleMemoAlign) ? "" :
				// " align='" + this.titleMemoAlign + "'") +
				// (this.empty(this.titleMemoValign) ? "" :
				// " valign='" + this.titleMemoValign + "'") +
				// (this.empty(this.titleMemoFontSize) ? "" :
				// " style='font-size:" + this.titleMemoFontSize + "pt;'") +
				// (this.empty(this.titleMemoStyleSheet) ? "" :
				// " style='" + this.titleMemoStyleSheet + "'") +
				// (this.empty(this.titleMemoColor) ? "" :
				// " style='color:" + this.titleMemoColor + "'") +
				// (this.empty(this.titleMemoBgColor) ? "" :
				// " bgcolor='" + this.titleMemoBgColor + "'") +
				// ">" + list.get(0).toString() + "</td></tr>\n");
			}
		} else if (st == this.STYLE_CUSTOMER) {
			Iterator itor = this.titleMemoList.iterator();
			while (itor.hasNext()) {
				ArrayList list = (ArrayList) itor.next();
				buf.append("<tr><td class='"
						+ list.get(1).toString()
						+ "' colspan=\""
						+ cols
						+ "\""
						+ (this.empty(this.titleMemoAlign) ? "" : " align='"
								+ this.titleMemoAlign + "'")
						+ (this.empty(this.titleMemoValign) ? "" : " valign='"
								+ this.titleMemoValign + "'")
						+ (this.empty(this.titleMemoFontSize) ? ""
								: " style='font-size:" + this.titleMemoFontSize
										+ "pt;'")
						+ (this.empty(this.titleMemoStyleSheet) ? ""
								: " style='" + this.titleMemoStyleSheet + "'")
						+ (this.empty(this.titleMemoColor) ? ""
								: " style='color:" + this.titleMemoColor + "'")
						+ (this.empty(this.titleMemoBgColor) ? ""
								: " bgcolor='" + this.titleMemoBgColor + "'")
						+ ">" + list.get(0).toString() + "</td></tr>\n");
			}
		}
		return buf.toString();
	}

	private String writeAttachMemo() {
		if (this.attachmemo.matches(".*<\\d+>.*")) {
			System.out.println("macth here!");
			if (this.attachList != null) {
				Iterator itor = attachList.iterator();
				while (itor.hasNext()) {
					String val = (String) itor.next();
					String[] list = val.split("->");
					if (list[1].matches("\\[\\d+,\\d+\\].*")) {
						System.out.println("matches ...");
						char[] str = list[1].toCharArray();
						double v1 = 0;
						double result = 0;
						String res = "";
						String v2 = null;
						boolean isNum = true;
						char cond = '0';
						for (int i = 0; i < str.length; i++) {
							if (str[i] == '[') {
								for (int j = i + 1; j < str.length; j++) {
									if (str[j] == ']') {
										String[] p = list[1]
												.substring(i + 1, j).split(",");
										if (p == null)
											continue;
										if (p.length == 1) {
											try {
												if (this.empty(p[0]))
													v1 = 0;
												else
													v1 = this.toNum(p[0]);
												isNum = true;
											} catch (Exception e) {
												v2 = p[0];
												isNum = false;
											}
										} else {
											try {
												if (this.empty(data[this
														.toInt(p[0])][this
														.toInt(p[1])]))
													v1 = 0;
												else
													v1 = this.toNum(data[this
															.toInt(p[0])][this
															.toInt(p[1])]);
												isNum = true;
											} catch (Exception e) {
												try {
													v2 = data[this.toInt(p[0])][this
															.toInt(p[1])];
												} catch (Exception ex) {
													v2 = "";
												}
												isNum = false;
											}
										}
										if (!isNum)
											res += v2;
										else
											switch (cond) {
											case '0':
												result = v1;
												break;
											case '+':
												result += v1;
												break;
											case '-':
												result -= v1;
												break;
											case '*':
												result *= v1 == 0 ? 1 : v1;
												break;
											case '/':
												result /= v1 == 0 ? 1 : v1;
												break;
											default:
												result = v1;
											}
										System.out.println("x:" + p[0] + " y:"
												+ p[1]);
										if (j + 1 < str.length)
											cond = str[j + 1];
										else
											cond = '0';

										break;
									}
								}
							}
						}
						attachmemo = attachmemo.replaceFirst("<" + list[0]
								+ ">",
								"<u> "
										+ (isNum ? "" + Math.round(result)
												: res) + " </u>");
					} else
						attachmemo = attachmemo.replaceFirst("<" + list[0]
								+ ">", "<u> " + list[1] + " </u>");
				}
			}

		}
		StringBuffer buf = new StringBuffer();
		buf.append("<tr><td class='x_tail' colspan=\""
				+ (this.lockIndex > 0 ? this.lockIndex : 1)
				+ "\""
				+ " align='center' valign='middle'"
				+ (this.empty(this.tailFontSize) ? "" : " style='font-size:"
						+ this.tailFontSize + "pt;'")
				+ (this.empty(this.tailColor) ? "" : " style='color:"
						+ this.tailColor + "'")
				+ (this.empty(this.tailBGColor) ? "" : " bgcolor='"
						+ this.tailBGColor + "'") + ">");
		buf.append("附记");
		buf.append("</td><td class='x_tail' colspan=\""
				+ (this.lockIndex > 0 ? cols - this.lockIndex : cols - 1)
				+ "\""
				+ " align='left' valign='middle'"
				+ (this.empty(this.tailFontSize) ? "" : " style='font-size:"
						+ this.tailFontSize + "pt;'")
				+ (this.empty(this.tailColor) ? "" : " style='color:"
						+ this.tailColor + "'")
				+ (this.empty(this.tailBGColor) ? "" : " bgcolor='"
						+ this.tailBGColor + "'") + ">");
		buf.append(this.attachmemo);
		buf.append("</td></tr>\n");
		return buf.toString();
	}

	private String writeTailMemoList(int st) {
		if (this.tailMemoList == null)
			return "";
		StringBuffer buf = new StringBuffer();
		if (st == this.STYLE_SHOW_DEFAULT) {

		} else if (st == this.STYLE_NONE) {
			Iterator itor = this.tailMemoList.iterator();
			while (itor.hasNext()) {
				ArrayList list = (ArrayList) itor.next();
				buf.append("<tr><td class='" + list.get(1).toString()
						+ "' colspan=\"" + cols + "\">"
						+ list.get(0).toString() + "</td></tr>\n");
			}
		} else if (st == this.STYLE_SHOW_EXCEL || st == this.STYLE_CUSTOMER) {
			Iterator itor = this.tailMemoList.iterator();
			while (itor.hasNext()) {
				ArrayList list = (ArrayList) itor.next();
				String[] mlist = (String[]) list.get(0);
				String[] slist = (String[]) list.get(1);
				if (mlist == null)
					continue;
				buf.append("<tr><td class='x_tm' colspan=\""
						+ cols
						+ "\""
						+ (this.empty(this.tailMemoAlign) ? "" : " align='"
								+ this.tailMemoAlign + "'")
						+ (this.empty(this.tailMemoValign) ? "" : " valign='"
								+ this.tailMemoValign + "'")
						+ (this.empty(this.tailMemoFontSize) ? ""
								: " style='font-size:" + this.tailMemoFontSize
										+ "pt;'")
						+ (this.empty(this.tailMemoStyleSheet) ? ""
								: " style='" + this.tailMemoStyleSheet + "'")
						+ (this.empty(this.tailMemoColor) ? ""
								: " style='color:" + this.tailMemoColor + "'")
						+ (this.empty(this.tailMemoBgColor) ? "" : " bgcolor='"
								+ this.tailMemoBgColor + "'") + ">");
				buf.append("<table border='0' width='100%' cellpadding=\"0\" cellspacing=\"0\"><tr>\n");
				int rate = Math.round(100 / mlist.length);
				for (int i = 0; i < mlist.length; i++) {
					buf.append("<td width='"
							+ rate
							+ "%' align='"
							+ ((i == 0) ? "left"
									: ((i == (mlist.length - 1) ? "right"
											: "center")))
							+ "' valign='bottom' class='"
							+ ((i >= slist.length) ? "x_hm_d" : slist[i])
							+ "'>");
					buf.append(mlist[i]);
					buf.append("</td>\n");
				}
				buf.append("</tr></table></td></tr>\n");

				// buf.append("<tr><td class='" + list.get(1).toString() +
				// "' colspan=\"" +
				// cols + "\"" +
				// (this.empty(this.tailMemoAlign) ? "" :
				// " align='" + this.tailMemoAlign + "'") +
				// (this.empty(this.tailMemoValign) ? "" :
				// " valign='" + this.tailMemoValign + "'") +
				// (this.empty(this.tailMemoFontSize) ? "" :
				// " style='font-size:" + this.tailMemoFontSize + "pt;'") +
				// (this.empty(this.tailMemoStyleSheet) ? "" :
				// " style='" + this.tailMemoStyleSheet + "'") +
				// (this.empty(this.tailMemoColor) ? "" :
				// " style='color:" + this.tailMemoColor + "'") +
				// (this.empty(this.tailMemoBgColor) ? "" :
				// " bgcolor='" + this.tailMemoBgColor + "'") +
				// ">" + list.get(0).toString() + "</td></tr>\n");
			}
		}
		return buf.toString();
	}

	private String writeEndTableTag(int st) {
		StringBuffer buf = new StringBuffer();
		if (st == this.STYLE_SHOW_DEFAULT) {
			buf.append("    </table>\n");
			buf.append("  </td>\n");
			buf.append("</tr>\n");
			buf.append("</table>");
		} else // if (st == this.STYLE_SHOW_EXCEL)
		{
			buf.append("</table>\n");
		}
		return buf.toString();
	}

	public void writeExcelFile(String filePath, String str) throws Exception {
		java.io.FileOutputStream out = new java.io.FileOutputStream(filePath);
		String header = "<html><head><title></title>\n"
				+ "<meta http-equiv=\"Content-Type\" content=\"text/html;charset=GBK\">\n"
				+ "<meta name=ProgId content=Excel.Sheet>\n" + "<style>\n"
				+ "<!--\n" + "a {\n" + "font-size:          9pt;\n"
				+ "color:              navy;\n" + "text-decoration:    none;\n"
				+ "}\n" + "a:hover {\n" + "font-size:          9pt;\n"
				+ "color:              darkorange;\n"
				+ "text-decoration:    underline;\n" + "}\n" + "-->\n"
				+ "</style>\n" + "</head>\n" + "<body>\n";
		out.write(header.getBytes("GB2312"));
		out.write(str.getBytes("GB2312"));
		out.write("</body></html>".getBytes("GB2312"));
		out.flush();
		out.close();
	}

	private String[][] execQuery(Connection conn, String sql) {
		if (sql == null)
			return null;
		if (conn == null)
			return null;
		ResultSet rs = null;
		Statement stmt = null;
		ResultSetMetaData md = null;
		ArrayList aList = new ArrayList();
		System.out.println(sql);
		int rows = 0, cols;
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			md = rs.getMetaData();
			cols = md.getColumnCount();
			while (rs.next()) {
				String[] row = new String[md.getColumnCount() + 1];
				for (int i = 0; i < md.getColumnCount(); i++) {
					row[i] = rs.getString(i + 1);
					row[i] = row[i] == null ? "" : row[i];
				}
				aList.add(row);
			}
			rs.close();
			rs = null;
			stmt.close();
			stmt = null;
		} catch (Exception e) {
			try {
				if (rs != null) {
					rs.close();
					rs = null;
				}
				if (stmt != null) {
					stmt.close();
					stmt = null;
				}
				e.printStackTrace(System.out);
			} catch (Exception ee) {
				return null;
			}
			return null;
		}

		rows = aList.size();
		if (rows == 0 || cols == 0) {
			aList.clear();
			aList = null;
			return null;
		}
		String[][] res = new String[rows][cols];
		for (int i = 0; i < rows; i++) {
			Object[] row = (Object[]) aList.toArray()[i];
			for (int j = 0; j < cols; j++) {
				res[i][j] = (String) row[j];
			}
		}
		aList.clear();
		aList = null;
		return res;
	}

	public boolean empty(String s) {
		if (s == null || s.trim().equals(""))
			return true;
		else
			return false;
	}

	public String currency_format(String curr, int precision) {
		String prec = ".";
		for (int i = 0; i < precision; i++) {
			prec += "0";
		}
		if (prec.length() == 1)
			prec = "";
		Double dblNum = null;
		if (curr == null || curr == "")
			return "";
		try {
			dblNum = new Double(curr);
		} catch (Exception e) {
			return curr;
		}
		DecimalFormat nf = new DecimalFormat("#,##0" + prec);
		return nf.format(dblNum).toString();
	}

	public String currency_format(String curr, String precision) {
		return currency_format(curr, this.toInt(precision));
	}

	public String currency_format(double curr) {
		Double dblNum = new Double(curr);
		DecimalFormat nf = new DecimalFormat("#,##0.00");
		return nf.format(dblNum).toString();
	}

	public int toInt(String s) {
		return (int) this.toNum(s);
	}

	public double toNum(String s) {
		try {
			return Double.parseDouble(s.replaceAll(",", ""));
		} catch (Exception e) {
			// e.printStackTrace(System.out);
		}
		return 0;
	}

}
