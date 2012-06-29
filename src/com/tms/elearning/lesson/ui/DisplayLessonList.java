package com.tms.elearning.lesson.ui;

import kacang.ui.LightWeightWidget;
import kacang.ui.Event;
import kacang.Application;
import java.util.Collection;
import java.io.Serializable;

import com.tms.elearning.lesson.model.LessonModule;

/**
 * Created by IntelliJ IDEA.
 * User: tirupati
 * Date: Oct 26, 2004
 * Time: 4:40:18 PM
 * To change this template use File | Settings | File Templates.
 */
public class DisplayLessonList extends LightWeightWidget implements Serializable {

    private Collection lessonList;

    public void onRequest(Event evt) {
        Application application = Application.getInstance();
        LessonModule module = (LessonModule)application.getModule(LessonModule.class);
        lessonList = module.findLessons(null,null, null,null,null, false, 0, -1);
    }

    public String getDefaultTemplate() {
        return "lesson/lessonForm";
    }

    public Collection getLessonsList() {
        return lessonList;
    }

}
