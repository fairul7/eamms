package com.tms.collab.formwizard.engine;

import java.io.Serializable;

public class ButtonField implements Serializable {
    String name;
    String text;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
