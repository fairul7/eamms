package com.tms.elearning.core.model;

import kacang.ui.Widget;
import kacang.stdui.TextBox;
import kacang.stdui.Form;

/**
 * A Rich-Text (WYSIWYG) editor that can be used in place of a TextBox.
 */
public class RichTextBox extends TextBox {

    private String functionName;
    private String linkUrl;
    private String imageUrl;
    private String width = "100%";

    /**
     * Creates a new instance of RichTextBox
     */
    public RichTextBox() {
        super();
    }

    /**
     * Creates a new instance of RichTextBox with specific name
     */
    public RichTextBox(String name) {
        super(name);
    }

    /**
     * Creates a new instance of RichTextBox with specific name and value
     */
    public RichTextBox(String name, String value) {
        super(name);
        setValue(value);
    }

    /**
     * Retrieves the URL of the hyperlink contain in the object
     * @return URL of the hyperlink contain in the object
     */
    public String getLinkUrl() {
        return linkUrl;
    }

    /**
     * Sets the URL of the hyperlink to the object
     * @param linkUrl URL of the hyperlink to the object
     */
    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    /**
     * Retrieves the URL of an image contain in the object
     * @return URL of an image contain in the object
     */
    public String getImageUrl() {
        return imageUrl;
    }

    /**
     * Sets the URL of an image to the object
     * @param imageUrl URL of an image to the object
     */
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

   

    public String getDefaultTemplate() {
        return "richtextbox";
    }

    /**
     * Returns name refering to the object which to be use in javascript
     * It's the absoluteName but replacing '.' with '_'
     * @return name refering to the object which to be use in javascript
     */
    public String getAbsoluteNameForJavaScript() {
        return getAbsoluteName().replace(SEPARATOR_WIDGET.charAt(0), '_');
    }

    /**
     * Retrieves the width of the object
     * @return width of the RichTextBox
     */
    public String getWidth() {
        return width;
    }

    /**
     * Sets the width of the object
     * @param width Width of the RichTextBox
     */
    public void setWidth(String width) {
        this.width = width;
    }
}

