package com.tms.fms.widgets;



public class CollapsiblePanel extends SolidPanel {
	private boolean collapsed = true;
	private boolean displayCollapseButton = true; 
	
	public CollapsiblePanel() {
	}
	
	public CollapsiblePanel(String name, String title) {
		super(name, title);
	}

	public boolean isCollapsed() {
		return collapsed;
	}

	public void setCollapsed(boolean collapsed) {
		this.collapsed = collapsed;
	}

	public boolean isDisplayCollapseButton() {
		return displayCollapseButton;
	}

	public void setDisplayCollapseButton(boolean displayCollapseButton) {
		this.displayCollapseButton = displayCollapseButton;
	}

	public String getAbsoluteNameForJavaScript() {
		return getAbsoluteName().replace(SEPARATOR_WIDGET.charAt(0), '_');
	}

	public String getDefaultTemplate() {
		return "fms/collapsiblePanel";
	}
}
