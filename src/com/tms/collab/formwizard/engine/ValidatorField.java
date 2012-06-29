package com.tms.collab.formwizard.engine;

import java.io.Serializable;

public class ValidatorField implements Serializable {
    String name;
    String text;
    String invalid;

    public static String VALIDATOR_INVALID = "invalid";
    public static String VALIDATOR_VALID = "valid";

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

    public String isInvalid() {
        return invalid;
    }

    public void setInvalid(String invalid) {
        this.invalid = invalid;
    }
}
