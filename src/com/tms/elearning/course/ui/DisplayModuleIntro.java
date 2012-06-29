package com.tms.elearning.course.ui;

import com.tms.elearning.course.model.CourseModule;
import com.tms.elearning.folder.model.FolderModule;
import com.tms.elearning.folder.model.Folder;

import kacang.Application;

import kacang.stdui.Form;
import kacang.stdui.Label;

import kacang.ui.Event;
import kacang.ui.Forward;


public class DisplayModuleIntro extends Form {

    String id;



    public void init() {
    }

    public void onRequest(Event evt) {
        Application app = Application.getInstance();

        Label introLabel = new Label("introLabel",
                "<b>" + app.getMessage("eLearning.folder.label.introduction") +
                "</b><br><br>");
        addChild(introLabel);

        Label introText;

        if(getId() !=null && !("".equals(getId()))){

            FolderModule module = (FolderModule) app.getModule(FolderModule.class);
            Folder object = (Folder) module.getIntroduction(getId());
        introText = new Label("introText", object.getIntroduction());

        if (object.getIntroduction() == null) {
            introText = new Label("introTextNotfound",
                    app.getMessage("eLearning.folder.label.introductionnotfound"));
        }

        addChild(introText);


        }//if
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
