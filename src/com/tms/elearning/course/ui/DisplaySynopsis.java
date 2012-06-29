package com.tms.elearning.course.ui;

import com.tms.elearning.course.model.CourseModule;

import kacang.Application;

import kacang.stdui.Form;
import kacang.stdui.Label;

import kacang.ui.Event;
import kacang.ui.Forward;


public class DisplaySynopsis extends Form {
    private String id;

    public void init() {
    }

    public void onRequest(Event evt) {
        Application app = Application.getInstance();
        CourseModule module = (CourseModule) app.getModule(CourseModule.class);

        Label synopsisLabel = new Label("synopsisLabel",
                "<b>" + app.getMessage("eLearning.course.label.synopsis") +
                "</b><br><br>");
        addChild(synopsisLabel);

        Label synopsisText;

        synopsisText = new Label("synopsisText", module.showSynopsis(id));

        if (synopsisText == null) {
            synopsisText = new Label("synopsisTextNotfound",
                    app.getMessage("eLearning.course.label.synopsisnotfound"));
        }

        addChild(synopsisText);
    }

    public Forward onSubmit(Event evt) {
        super.onSubmit(evt);

        return null;
    }

    //get and set
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
