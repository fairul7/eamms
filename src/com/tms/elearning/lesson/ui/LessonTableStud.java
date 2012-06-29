package com.tms.elearning.lesson.ui;

import com.tms.elearning.lesson.model.LessonModule;
import com.tms.elearning.lesson.model.LessonException;
import com.tms.elearning.lesson.model.Lesson;
import com.tms.collab.messaging.model.Message;
import com.tms.collab.messaging.model.MessagingModule;
import com.tms.collab.messaging.model.MessagingException;

import kacang.Application;
import kacang.util.Log;

import kacang.stdui.Table;
import kacang.stdui.TableColumn;
import kacang.stdui.TableModel;

import java.util.Collection;
import java.util.Iterator;
import java.io.Serializable;


/**
 * Created by IntelliJ IDEA.
 * User: tirupati
 * Date: Oct 26, 2004
 * Time: 4:55:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class LessonTableStud extends Table implements Serializable {
    protected String moduleId;

    public String getModuleId() {
        return moduleId;
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }

    public void init() {
        setModel(new com.tms.elearning.lesson.ui.LessonTableStud.LessonTableModel());
    }

    public class LessonTableModel extends TableModel {
        public LessonTableModel() {
            TableColumn naCol = new TableColumn("name", "Lesson Name");
            naCol.setLabel("Lessons");
            naCol.setUrl("readLesson.jsp");
            naCol.setUrlParam("id");


            addColumn(naCol);
        }

        public Collection getTableRows() {
            Application application = Application.getInstance();
            LessonModule module = (LessonModule) application.getModule(LessonModule.class);

            return module.findLessons(getModuleId(), getSort(), isDesc(),
                getStart(), getRows());
        }

        public int getTotalRowCount() {
            Application application = Application.getInstance();
            LessonModule module = (LessonModule) application.getModule(LessonModule.class);

            return module.countLessons(getModuleId());
        }




    }
}
