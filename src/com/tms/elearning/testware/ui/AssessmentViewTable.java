package com.tms.elearning.testware.ui;

import com.tms.elearning.course.model.Topic;
import com.tms.elearning.course.model.TopicModule;
import com.tms.elearning.course.model.TopicModuleDao;
import com.tms.elearning.testware.model.Assessment;
import com.tms.elearning.testware.model.AssessmentModule;

import kacang.Application;

import kacang.model.DaoException;

import kacang.services.security.SecurityService;
import kacang.services.security.User;

import kacang.stdui.*;

import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.ui.WidgetManager;

import kacang.util.Log;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.io.Serializable;


/**
 * Created by IntelliJ IDEA.
 * User: tirupati
 * Date: Dec 8, 2004
 * Time: 2:20:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class AssessmentViewTable extends Table implements Serializable{

    private String courseid;

    public void init() {
        setModel(new AssessmentViewTableModel());
        setPageSize(10);
    }

    public void onRequest(Event evt) {
        super.onRequest(evt);
        init();
    }

    public String getCourseid() {
        return this.courseid;
    }

    public void setCourseid(String courseid) {
        this.courseid = courseid;
    }

    public class AssessmentViewTableModel extends TableModel {
        public AssessmentViewTableModel() {
            Application application = Application.getInstance();
            SecurityService service = (SecurityService) Application.getInstance()
                                                                   .getService(SecurityService.class);
            User current = getWidgetManager().getUser();
            WidgetManager manager = getWidgetManager();
            User user = manager.getUser();
            String userId = user.getUsername();
            Log log = Log.getLog(getClass());

            addFilter(new TableFilter("key"));

            TableColumn assCol = new TableColumn("name",
                    application.getMessage("eLearning.assessment.label"));
            addColumn(assCol);

            TableColumn qCol = new TableColumn("",
                    application.getMessage("eLearning.questions.label"), false);
            qCol.setLabel("questions");
            qCol.setUrl("asmtQuestion.jsp");
            addColumn(qCol);

            TableColumn qbankCol = new TableColumn("",
                    application.getMessage("eLearning.assessment.questionbank"),
                    false);
            qbankCol.setLabel("question bank");
            qbankCol.setUrl("question.jsp");
            addColumn(qbankCol);

            try {
                if (service.hasPermission(current.getId(),
                            "com.tms.elearning.testware.Edit", null, null)) {
                    assCol.setUrlParam("id");
                    qCol.setUrlParam("examid");
                    qbankCol.setUrlParam("examid");
                }
            } catch (Exception e) {
                Log.getLog(Assessment.class).error(e.getMessage(), e);
            }

            TableFilter courseFilter = new TableFilter("course");
            SelectBox courseSelect = new SelectBox("courseid");
            courseSelect.setOptions("-1=" +
                application.getMessage("eLearning.topic.label.coursemenu"));

            TopicModuleDao tpcDao = (TopicModuleDao) Application.getInstance()
                                                                .getModule(TopicModule.class)
                                                                .getDao();

            try {
                Collection courses = tpcDao.getCourses(userId);
                HashMap hash = new HashMap();

                for (Iterator i = courses.iterator(); i.hasNext();) {
                    Topic temp = (Topic) i.next();
                    hash.put(temp.getCourse_id(), temp.getCourse_name());
                }

                courseSelect.setOptionMap(hash);
            } catch (DaoException e) {
                log.error(e.toString(), e);
            }

            courseFilter.setWidget(courseSelect);
            addFilter(courseFilter);
        }

        public Collection getTableRows() {
            String keyword = (String) getFilterValue("key");
            List courseList = (List) getFilterValue("course");
            String course = "-1";

            if (courseList.size() > 0) {
                course = (String) courseList.get(0);
            }

            Application application = Application.getInstance();
            AssessmentModule module = (AssessmentModule) application.getModule(AssessmentModule.class);

            return module.getCourseAssessments(keyword, getCourseid(),
                getSort(), isDesc(), getStart(), getRows());
        }

        public int getTotalRowCount() {
            String keyword = (String) getFilterValue("key");
            List courseList = (List) getFilterValue("course");
            String course = "-1";

            if (courseList.size() > 0) {
                course = (String) courseList.get(0);
            }

            Application application = Application.getInstance();
            AssessmentModule module = (AssessmentModule) application.getModule(AssessmentModule.class);

            return module.countCourseAssessments(keyword, getCourseid());
        }

        public Forward processAction(Event evt, String action,
            String[] selectedKeys) {
            return null;
        }

        public String getTableRowKey() {
            return "id";
        }
    }
}
