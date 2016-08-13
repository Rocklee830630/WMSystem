package com.ccthanking.framework.common;

import java.io.Serializable;

import com.ccthanking.framework.base.BaseVO;
/**
 * @auther xhb 
 */
public class BusinessMenu extends BaseVO implements Serializable {
	/* 菜单别名（英文、数字） */
	private String name;
	/* 菜单标题（汉字） */
	private String title;
	/* 父级菜单 */
	private String parent;
	/* 顺序号 */
	private String orderno;
	/* 页面显示区域 */
	private String target;
	/* 页面web路径 */
	private String location;
	/* 叶子节点 */
	private String chief;
	/* 图片 */
	private String image;
	/* 焦点图片 */
	private String altimage;
	/* 层级标志 */
	private String layersno;
	/* 备注 */
	private String memo;

	/*  */
	private String description;
	/*  */
	private String tooltip;
	/*  */
	private String page;
	/*  */
	private MenuVo[] child;
	
	public BusinessMenu() {
		this.addField("NAME",OP_STRING|this.TP_PK);
		this.addField("TITLE",OP_STRING);
		this.addField("PARENT",OP_STRING);
		this.addField("ORDERNO",OP_STRING);
		this.addField("TARGET",OP_STRING);
		this.addField("LOCATION",OP_STRING);
		this.addField("CHIEF",OP_STRING);
		this.addField("IMAGE",OP_STRING);
		this.addField("ALTIMAGE",OP_STRING);
		this.addField("APP_NAME",OP_STRING);
		this.addField("LAYERSNO",OP_STRING);
		this.addField("MEMO",OP_STRING);
		this.addField("LRR",OP_STRING);
		this.addField("LRSJ",OP_DATE);
		this.addField("LRBM",OP_STRING);
		this.addField("LRBMMC",OP_STRING);
		this.addField("GXR",OP_STRING);
		this.addField("GXSJ",OP_DATE);
		this.addField("GXBM",OP_STRING);
		this.addField("GXBMMC",OP_STRING);
		this.addField("SJMJ",OP_STRING);
		this.addField("SFYX",OP_STRING);
		this.addField("LEVELNO",OP_STRING);
		
		this.setFieldDateFormat("LRSJ","yyyy-MM-dd");
		this.setFieldDateFormat("GXSJ","yyyy-MM-dd");
		
		this.setVOTableName("FS_EAP_MENU");
	}

	public MenuVo[] getChild() {
		return child;
	}

	public void setChild(MenuVo[] child) {
		this.child = child;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLocation() {
		return this.location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getAbsoluteLocation() {
		return this.getLocation();
	}

	public String getFakeLocation() {
		return this.getLocation();
	}

	public String getTarget() {
		return this.target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLayersno() {
		return this.layersno;
	}

	public void setLayersno(String layersno) {
		this.layersno = layersno;
	}

	public String getImage() {
		return this.image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getAltImage() {
		return this.altimage;
	}

	public void setAltImage(String altimage) {
		this.altimage = altimage;
	}

	public void setOrderNo(String orderNo) {
		this.orderno = orderNo;
	}

	public String getOrderNo() {
		return this.orderno;
	}

	public String getToolTip() {
		return this.tooltip;
	}

	public void setToolTip(String toolTip) {
		this.tooltip = toolTip;
	}

	public String getPage() {
		return this.page;
	}

	public void setPage(String page) {
		this.page = page;
	}

	public String getChief() {
		return this.chief;
	}

	public void setChief(String chief) {
		this.chief = chief;
	}

	public String getParent() {
		return this.parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public String getOrderno() {
		return orderno;
	}

	public void setOrderno(String orderno) {
		this.orderno = orderno;
	}

	public String getAltimage() {
		return altimage;
	}

	public void setAltimage(String altimage) {
		this.altimage = altimage;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getTooltip() {
		return tooltip;
	}

	public void setTooltip(String tooltip) {
		this.tooltip = tooltip;
	}
}
