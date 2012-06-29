package com.tms.elearning.lesson.ui;

import kacang.stdui.*;
import kacang.util.Log;
import kacang.Application;
import kacang.services.security.User;
import kacang.ui.Forward;
import kacang.ui.Event;
import kacang.ui.WidgetManager;
import kacang.model.DaoException;
import com.tms.elearning.lesson.model.LessonDao;
import com.tms.elearning.lesson.model.LessonModule;
import com.tms.elearning.lesson.model.Lesson;
import kacang.stdui.*;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.HashMap;
import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: tirupati
 * Date: Dec 6, 2004
 * Time: 5:49:09 PM
 * To change this template use File | Settings | File Templates.
 */

public class LessonViewTable extends Table implements Serializable  {

     private String courseid;



     public String getCourseid() {
        return this.courseid;
    }

    public void setCourseid(String courseid) {
        this.courseid = courseid;
    }

    public void init() {
        setModel(new com.tms.elearning.lesson.ui.LessonViewTable.LessonViewTableModel());
    }

     public void onRequest(Event evt) {
        super.onRequest(evt);
        init();
    }

    public class LessonViewTableModel extends TableModel {

        public LessonViewTableModel() {
            Application application = Application.getInstance();
            WidgetManager manager = getWidgetManager();
            User user = manager.getUser();
            String userId=user.getUsername();
            Log log = Log.getLog(getClass());

            TableColumn naCol = new TableColumn("name", application.getMessage("eLearning.lesson.label.lessonname"));
            naCol.setUrl("showLesson.jsp");
            naCol.setUrlParam("id");
            addColumn(naCol);
            addColumn(new TableColumn("courseName", "Course"));
            addColumn(new TableColumn("folderName", "Folder"));

            addFilter(new TableFilter("name"));

            TableFilter courseFilter = new TableFilter("course");
            SelectBox courseSelect = new SelectBox("courseid");
            courseSelect.setOptions("-1="+ application.getMessage("eLearning.topic.label.coursemenu"));

            LessonDao lessonDao = (LessonDao)Application.getInstance().getModule(LessonModule.class).getDao();

            try {
                Collection courses = lessonDao.getCourses(userId);
                HashMap hash = new HashMap();
                for(Iterator i= courses.iterator();i.hasNext();){
                    Lesson temp =(Lesson)i.next();
                   // courseSelect.setOptions(temp.getCourseId()+"="+temp.getCourseName());
                    hash.put(temp.getCourseId(),temp.getCourseName());
                }
                courseSelect.setOptionMap(hash);
            } catch (DaoException e) {
                log.error(e.toString(),e);
            }
            courseFilter.setWidget(courseSelect);
            addFilter(courseFilter);

            TableFilter folderFilter = new TableFilter("folder");
            SelectBox folderSelect = new SelectBox("folderid");
            folderSelect.setOptions("-1="+application.getMessage("eLearning.lesson.label.modulemenu"));

            try {
                Collection folders = lessonDao.getFolders(userId);
                HashMap hash = new HashMap();
                for(Iterator i= folders.iterator();i.hasNext();){
                    Lesson temp1 =(Lesson)i.next();
                    hash.put(temp1.getFolderId(),temp1.getFolderName());
                }
                folderSelect.setOptionMap(hash);
            } catch (DaoException e) {
                log.error(e.toString(),e);
            }
            folderFilter.setWidget(folderSelect);
            addFilter(folderFilter);
            //addAction(new TableAction("add", "Add"));
            //addAction(new TableAction("delete", "Delete", "Confirm?"));
            //
        }

        public Collection getTableRows() {
            String name = (String)getFilterValue("name");
            Application application = Application.getInstance();
            LessonModule module = (LessonModule)application.getModule(LessonModule.class);
            return module.findCourseLessons(name,getCourseid(), getSort(), isDesc(), getStart(), getRows());
        }

        public int getTotalRowCount() {
            String name = (String)getFilterValue("name");
            Application application = Application.getInstance();
            LessonModule module = (LessonModule)application.getModule(LessonModule.class);
            return module.countCourseLessons(name,getCourseid());
        }

        public String getTableRowKey() {
            return "id";
        }

        public Forward processAction(Event evt, String action, String[] selectedKeys) {
            /*
            if ("add".equals(action)) {
                return new Forward("add");
            }
            else if ("delete".equals(action)) {
                Application application = Application.getInstance();
                LessonModule module = (LessonModule)application.getModule(LessonModule.class);
                for (int i=0; i<selectedKeys.length; i++) {
                    module.deleteLesson(selectedKeys[i]);
                }
            }
            */
            return null;
        }

    }

}
