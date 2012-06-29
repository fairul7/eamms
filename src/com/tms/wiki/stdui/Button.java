package com.tms.wiki.stdui;

import kacang.stdui.FormField;


/**
 *  <p>This class is used to create a HTML form input element - Button.</p>
 */
public class Button extends FormField {
    public static final String PREFIX_BUTTON = "button*";
    private String onBlur;
    private String onClick;
    private String onFocus;
    private String text;
	private Boolean disableIt;

    /**
     * Default constructor of Button. Programmer is required to set the name for the button
     * before it is added as a child to its parent by calling <code>setName</code> method.
     */
    public Button() {
        super();      
        
    }

    /**
     * Creates a button with the specifed name.
     * @param name The specified name.
     */
    public Button(String name) {
        super(name);
    }

    /**
     * Creates a button with the specifed name and text.
     * @param name The name of the button.
     * @param text The text of the button.
     */
    public Button(String name, String text) {
        super(name);
        this.text = text;
    }

    /**
     * Return the button's text.
     * @return The button's text.
     */
    public java.lang.String getText() {
        return text;
    }

    /**
     * Sets the button's text.
     * @param text The button's text
     */
    public void setText(java.lang.String text) {
        this.text = text;
    }

    /**
     * Return the handle script for OnBlur event of HTML button
     * @return The handle script
     */
    public String getOnBlur() {
        return onBlur;
    }

    /**
     * Set the handle script for OnBlur event of HTML button
     * @param onBlur The handle script
     */
    public void setOnBlur(String onBlur) {
        this.onBlur = onBlur;
    }

    /**
     * Return the handle script for OnClick event of HTML button
     * @return The handle script
     */
    public String getOnClick() {
        return onClick;
    }

    /**
     * Set the handle script for OnClick event of HTML button
     * @param onClick The handle script
     */
    public void setOnClick(String onClick) {
        this.onClick = onClick;
    }

    /**
     * Return the handle script for OnFocus event of HTML button
     * @return The handle script
     */
    public String getOnFocus() {
        return onFocus;
    }

    /**
     * Set the handle script for OnFocus event of HTML button
     * @param onFocus The handle script
     */
    public void setOnFocus(String onFocus) {
        this.onFocus = onFocus;
    }

    public String getDefaultTemplate() {
        return "button";
    }

	public Boolean getDisableIt() {
		return disableIt;
	}

	public void setDisableIt(Boolean disableIt) {
		this.disableIt = disableIt;
	}  
    
}

