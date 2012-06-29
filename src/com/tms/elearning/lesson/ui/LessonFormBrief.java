package com.tms.elearning.lesson.ui;

import kacang.stdui.*;
import kacang.Application;
import kacang.util.Log;
import kacang.model.DataObjectNotFoundException;
import kacang.ui.Event;
import com.tms.elearning.lesson.model.LessonModule;
import com.tms.elearning.lesson.model.Lesson;
import kacang.stdui.*;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: tirupati
 * Date: Dec 8, 2004
 * Time: 10:18:20 AM
 * To change this template use File | Settings | File Templates.
 */

public class LessonFormBrief extends Form implements Serializable {

    protected TextField name;
    protected RichTextBox brief;
    protected Button cancel;
    private String id;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDefaultTemplate() {
        return "elearning/lessonBriefForm";
    }

     public void init(Event evt) {
        super.init();
        initForm(evt);
    }

    public void onRequest(Event evt) {
        super.onRequest(evt);
        initForm(evt);
    }
    public void initForm(Event evt) {
        LessonModule lessonmodule = (LessonModule)Application.getInstance().getModule(LessonModule.class);
        addChild(new Label("l1", "Lesson Name"));
        name = new TextField("name");
        name.isInvalid();
        addChild(name);

        addChild(new Label("l2", "Brief"));
        brief = new RichTextBox("brief");
        brief.setCols("8");
        brief.setRows("25");
       
        brief.isInvalid();
        addChild(brief);

        cancel = new Button("cancel","Cancel");
        addChild(cancel);

        if (id != null) {
           try {
                Lesson lesson = lessonmodule.loadLesson(getId());
                name.setValue(lesson.getName());
                brief.setValue(lesson.getBrief());

           } catch (DataObjectNotFoundException e) {
                Log.getLog(getClass()).error("lesson " + getId() + " not found");
                init();
            }
        }
    }

}
