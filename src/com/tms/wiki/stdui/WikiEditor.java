package com.tms.wiki.stdui;

import kacang.stdui.TextBox;

/**
 * Date: Dec 19, 2006
 * Time: 11:03:19 AM
 */
public class WikiEditor extends TextBox{

    private String height;

    public WikiEditor() {
        super();
    }

    public WikiEditor(String s) {
        super(s);
    }

    public WikiEditor(String s, String height) {
        super(s);
        this.height = height;
    }

    public String getDefaultTemplate() {
        return "wiki/wikiEditor";
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }
}
