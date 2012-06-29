package com.tms.elearning.course.ui;

import kacang.Application;
import kacang.services.security.User;
import kacang.model.DaoException;
import kacang.stdui.*;
import kacang.ui.Forward;
import kacang.ui.Event;
import kacang.ui.WidgetManager;
import kacang.util.Log;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import com.tms.elearning.course.model.*;

/**
 * Created by IntelliJ IDEA.
 * User: mahe
 * Date: Oct 26, 2004
 * Time: 7:10:14 PM
 * To change this template use Options | File Templates.
 */
public class TopicTable extends Table {


     public void init() {
        setModel(new TopicTableModel());
        setPageSize(10);
    }

    public class TopicTableModel extends TableModel {
        public TopicTableModel() {
            Application application = Application.getInstance();
            WidgetManager manager = getWidgetManager();
            User user = manager.getUser();
            String userId=user.getUsername();
            Log log = Log.getLog(getClass());

            addFilter(new TableFilter("topic"));
            addAction(new TableAction("add", application.getMessage("eLearning.topic.label.add")));
            addAction(new TableAction("delete",application.getMessage("eLearning.topic.label.delete"),application.getMessage("eLearning.topic.label.deleteselectedtopics")));

            TableColumn topicCol = new TableColumn("topic",application.getMessage("eLearning.topic.label.topic"));
            topicCol.setUrlParam("id");
            addColumn(topicCol);

            addColumn(new TableColumn("course_name",application.getMessage("eLearning.course.label.course")));

            TableFilter courseFilter = new TableFilter("course");
            SelectBox courseSelect = new SelectBox("courseid");
            courseSelect.setOptions("-1="+application.getMessage("eLearning.topic.label.coursemenu"));

            TopicModuleDao tpcDao = (TopicModuleDao)Application.getInstance().getModule(TopicModule.class).getDao();

            try {
                Collection courses = tpcDao.getCourses(userId);
                for(Iterator i= courses.iterator();i.hasNext();){
                    Topic temp =(Topic)i.next();
                    courseSelect.setOptions(temp.getCourse_id()+"="+temp.getCourse_name());
                }
            } catch (DaoException e) {
                log.error(e.toString(),e);
            }
            courseFilter.setWidget(courseSelect);
            addFilter(courseFilter);
        }

        public Collection getTableRows() {
            String keyword = (String)getFilterValue("topic");
            List courseList = (List)getFilterValue("course");
            String course = "-1";

            if(courseList.size()>0) {
                course = (String)courseList.get(0);
            }

            Application application = Application.getInstance();
            TopicModule module = (TopicModule)application.getModule(TopicModule.class);
            return module.getTopics(keyword,course,getSort(),isDesc(),getStart(),getRows());
        }

        public int getTotalRowCount() {
            String keyword = (String)getFilterValue("topic");
            List courseList = (List)getFilterValue("course");
            String course = "-1";

            if(courseList.size()>0) {
                course = (String)courseList.get(0);
            }

            Application application = Application.getInstance();
            TopicModule module = (TopicModule)application.getModule(TopicModule.class);
            return module.countTopics(keyword,course);
        }

        public Forward processAction(Event evt, String action, String[] selectedKeys) {
           if ("add".equals(action)) {
                return new Forward("add");
            }
            else if ("delete".equals(action)) {
                Application application = Application.getInstance();
                TopicModule module = (TopicModule)application.getModule(TopicModule.class);
                for (int i=0; i<selectedKeys.length; i++) {
                    module.deleteTopic(selectedKeys[i]);
                }
            }
           return null;
        }

        public String getTableRowKey() {
            return "id";
        }
    }
}
