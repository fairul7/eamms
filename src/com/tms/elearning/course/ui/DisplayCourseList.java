/**
 * Created by IntelliJ IDEA.
 * User: tirupati
 * Date: Oct 6, 2004
 * Time: 7:18:43 PM
 * To change this template use File | Settings | File Templates.
 */
package com.tms.elearning.course.ui;

import kacang.ui.*;
import kacang.Application;
import com.tms.elearning.course.model.CourseModule;
import java.util.Collection;

public class DisplayCourseList extends LightWeightWidget {

    private Collection courseList;

    public void onRequest(Event evt) {
        Application application = Application.getInstance();
        CourseModule module = (CourseModule)application.getModule(CourseModule.class);

        courseList = module.findCourses(null,null,null, null, false, 0, -1);
    }

    public String getDefaultTemplate() {
          return "course/courseForm";
    }

    public Collection getCourseList() {
        return courseList;
    }

}
