package com.ccthanking.framework.util;

public class QueryCondition {
	public QueryCondition() {
	}

	public QueryCondition(String field, String operation, String value,
			String kind, String format, String logic) {
		this.field = field;
		this.operation = operation;
		this.value = value;
		this.kind = kind;
		this.fieldFormat = format;
		this.logic = logic;
	}

	public static final String KIND_TEXT = "text";
	public static final String KIND_QUERY = "query";
	public static final String KIND_DATE = "date";
	public static final String KIND_DATETIME = "datetime";

	private String field;
	private String operation;
	private String value;
	private String kind;
	private String fieldFormat;
	private String logic;
	private String group;

	public void setFieldFormat(String format) {
		this.fieldFormat = format;
	}

	public String getFieldFormat() {
		return this.fieldFormat;
	}

	public void setfield(String field) {
		this.field = field;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public void setKind(String kind) {
		this.kind = kind;
	}

	public String getField() {
		return this.field;
	}

	public String getOperation() {
		return this.operation;
	}

	public String getValue() {
		if (this.value != null) {
			this.value = Pub.cat(this.value);
		}
		return this.value;
	}

	public String getKind() {
		return this.kind;
	}

	public String getLogic() {
		return logic;
	}

	public void setLogic(String logic) {
		this.logic = logic;
	}

	public String getGroup() {
		return  this.field;
	}
}