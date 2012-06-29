package com.tms.collab.messaging.ui;

import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.ui.Widget;

/**
 * A Line widget, representing the &lt;hr&gt; in html tag.
 */
public class Line extends Widget {

    private String align;
    private String clazz;
    private Boolean noShade = Boolean.FALSE;
    private String dir;
    private String lang;
    private String onclick;
    private String ondbclick;
    private String onkeydown;
    private String onkeypress;
    private String onkeyup;
    private String onmousedown;
    private String onmouseover;
    private String onmouseup;
    private String size;
    private String style;
    private String width;
    private String title;
    
    
    public Line(String name) {
    	super(name);
    }
    

    /**
	 * @see kacang.ui.Widget#getDefaultTemplate()
	 */
	public String getDefaultTemplate() {
        return "line";
	}
    
    /**
	 * @see kacang.ui.Widget#actionPerformed(kacang.ui.Event)
	 */
	public Forward actionPerformed(Event evt) {
		return super.actionPerformed(evt);
	}
    
    
    
    // getters / setters ===================================================
	public String getAlign() {
		return align;
	}
	public void setAlign(String align) {
		this.align = align;
	}
	public String getClazz() {
		return clazz;
	}
	public void setClazz(String clazz) {
		this.clazz = clazz;
	}
	public String getDir() {
		return dir;
	}
	public void setDir(String dir) {
		this.dir = dir;
	}
	public String getLang() {
		return lang;
	}
	public void setLang(String lang) {
		this.lang = lang;
	}
	public Boolean getNoshade() {
		return noShade;
	}
	public void setNoshade(Boolean noShade) {
		this.noShade = noShade;
	}
	public String getOnclick() {
		return onclick;
	}
	public void setOnclick(String onclick) {
		this.onclick = onclick;
	}
	public String getOndbclick() {
		return ondbclick;
	}
	public void setOndbclick(String ondbclick) {
		this.ondbclick = ondbclick;
	}
	public String getOnkeydown() {
		return onkeydown;
	}
	public void setOnkeydown(String onkeydown) {
		this.onkeydown = onkeydown;
	}
	public String getOnkeypress() {
		return onkeypress;
	}
	public void setOnkeypress(String onkeypress) {
		this.onkeypress = onkeypress;
	}
	public String getOnkeyup() {
		return onkeyup;
	}
	public void setOnkeyup(String onkeyup) {
		this.onkeyup = onkeyup;
	}
	public String getOnmousedown() {
		return onmousedown;
	}
	public void setOnmousedown(String onmousedown) {
		this.onmousedown = onmousedown;
	}
	public String getOnmouseover() {
		return onmouseover;
	}
	public void setOnmouseover(String onmouseover) {
		this.onmouseover = onmouseover;
	}
	public String getOnmouseup() {
		return onmouseup;
	}
	public void setOnmouseup(String onmouseup) {
		this.onmouseup = onmouseup;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public String getStyle() {
		return style;
	}
	public void setStyle(String style) {
		this.style = style;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getWidth() {
		return width;
	}
	public void setWidth(String width) {
		this.width = width;
	}
}
