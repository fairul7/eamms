package com.tms.fms.util;



import kacang.ui.Widget;

/**
 * Displays a String value.
 */
public class Label extends Widget {

    private String text;
    private boolean escapeXml;

    /**
     * Create a new instance of Label
     */
    public Label() {
        super();
    }

    /**
     * Create a new instance of Label with specific name
     */
    public Label(String name) {
        super(name);
    }

    /**
     * Create a new instance of Label with specific name and text
     */
    public Label(String name, String text) {
        super(name);
        setText(text);
    }

    /**
     * Retrieves the Label text as in the display value
     * @return Label text
     */
    public java.lang.String getText() {
        return text;
    }

    /**
     * Sets the Label text as the value to be display
     * @param text Label text
     */
    public void setText(java.lang.String text) {
        this.text = text;
    }

    /**
     * Stating if the object should ignore xml code, true to ignore and false to interprete xml
     * @return true is ignore xml, false is to interprete
     */
    public boolean isEscapeXml() {
        return escapeXml;
    }

    /**
     * Sets if the object should ignore xml code.
     * True if ignore, false if to interprete xml
     * @param escapeXml
     */
    public void setEscapeXml(boolean escapeXml) {
        this.escapeXml = escapeXml;
    }

    public String getDefaultTemplate() {
        return "fms/labelfms";
    }

}

