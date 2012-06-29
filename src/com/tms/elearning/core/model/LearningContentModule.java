package com.tms.elearning.core.model;

import kacang.model.DefaultModule;


public abstract class LearningContentModule extends DefaultModule {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
