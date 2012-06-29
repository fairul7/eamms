package com.tms.fms.widgets;

import kacang.stdui.*;

public class ExtendedTableColumn extends TableColumn {
	private String align;
	private String headerAlign;
	private String style;
	
	public ExtendedTableColumn(String property, String header) {
		super(property, header);
	}

	public ExtendedTableColumn(String property, String header, boolean sortable) {
		super(property, header, sortable);
	}
	
	public String getAlign() {
		return align;
	}

	public void setAlign(String align) {
		this.align = align;
	}
	
	public void setAlign(String align, String headerAlign) {
		this.align = align;
		this.headerAlign = headerAlign;
	}

	public String getHeaderAlign() {
		return headerAlign;
	}

	public void setHeaderAlign(String headerAlign) {
		this.headerAlign = headerAlign;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}
}
