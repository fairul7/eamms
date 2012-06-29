package com.tms.elearning.course.ui;

import kacang.stdui.*;
import kacang.Application;
import kacang.ui.Forward;
import kacang.ui.Event;

import java.util.Collection;
import com.tms.elearning.course.model.CourseModule;

/**
 * Created by IntelliJ IDEA.
 * User: tirupati
 * Date: Nov 8, 2004
 * Time: 5:55:09 PM
 * To change this template use File | Settings | File Templates.
 */
import kacang.stdui.*;

public class SubjectTable extends Table  {

    public void init() {
        setModel(new SubjectTableModel());
    }
    public class SubjectTableModel extends TableModel {

        public SubjectTableModel() {
            TableColumn naCol = new TableColumn("category", "Category");
            naCol.setUrlParam("categoryid");
            naCol.setUrl("subjectCourses.jsp");
            addColumn(naCol);



            addFilter(new TableFilter("category"));




        }

        public Collection getTableRows() {
            String category = (String)getFilterValue("category");
            Application application = Application.getInstance();
            CourseModule module = (CourseModule)application.getModule(CourseModule.class);
            return module.findSubjects(category, getSort(), isDesc(), getStart(), getRows());
        }

        public int getTotalRowCount() {
            String category = (String)getFilterValue("category");
            Application application = Application.getInstance();
            CourseModule module = (CourseModule)application.getModule(CourseModule.class);
            return module.countSubjects(category);
        }

        public String getTableRowKey() {
            return "id";
        }

        public Forward processAction(Event evt, String action, String[] selectedKeys) {
            if ("add".equals(action)) {
                return new Forward("add");
            }
            return null;
        }
    }

}
