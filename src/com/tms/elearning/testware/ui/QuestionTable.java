package com.tms.elearning.testware.ui;

import com.tms.elearning.testware.model.Question;
import com.tms.elearning.testware.model.QuestionModule;

import kacang.Application;

import kacang.services.security.SecurityService;
import kacang.services.security.User;

import kacang.stdui.*;

import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.ui.WidgetManager;

import kacang.util.Log;

import java.util.Collection;
import java.util.List;
import java.io.Serializable;


/**
 * Created by IntelliJ IDEA.
 * User: mahe
 * Date: Nov 2, 2004
 * Time: 4:40:03 PM
 * To change this template use Options | File Templates.
 */
public class QuestionTable extends Table implements Serializable{

    private String examid;

    public void onRequest(Event event) {
        String ex = null;

        if (event.getRequest().getSession().getAttribute("exam") != null) {
            ex = event.getRequest().getSession().getAttribute("exam").toString();
            setExamid(ex);
            init();
        } else {
            setExamid(null);
            init();
        }
    }

    public void init() {
        setModel(new QuestionTableModel());
        setPageSize(10);
    }

    public String getExamid() {
        return this.examid;
    }

    public void setExamid(String examid) {
        this.examid = examid;
    }

    public class QuestionTableModel extends TableModel {
        public QuestionTableModel() {
            Application application = Application.getInstance();
            SecurityService service = (SecurityService) Application.getInstance()
                                                                   .getService(SecurityService.class);
            User current = getWidgetManager().getUser();
            WidgetManager manager = getWidgetManager();
            User user = manager.getUser();
            String userId = user.getUsername();
            Log log = Log.getLog(getClass());

            addFilter(new TableFilter("topic"));

            try {
                if (service.hasPermission(current.getId(),
                            "com.tms.elearning.testware.Add", null, null)) {
                    addAction(new TableAction("add",
                            application.getMessage("eLearning.question.new")));

                    if (getExamid() != null) {
                        addAction(new TableAction("addToExam",
                                application.getMessage(
                                    "eLearning.assessment.addtoexam")));
                    }
                }

                if (service.hasPermission(current.getId(),
                            "com.tms.elearning.testware.Delete", null, null)) {
                    addAction(new TableAction("delete",
                            application.getMessage("eLearning.question.delete"),
                            application.getMessage(
                                "eLearning.question.deletequestions")));
                }
            } catch (Exception e) {
                Log.getLog(Question.class).error(application.getMessage(
                        "eLearning.question.errorloadquestion") + " " +
                    e.toString(), e);
            }

            TableColumn questionCol = new TableColumn("question", "Question");
            addColumn(questionCol);

            try {
                if (service.hasPermission(current.getId(),
                            "com.tms.elearning.testware.Edit", null, null)) {
                    questionCol.setUrl("questionEdit.jsp");
                }

                questionCol.setUrlParam("id");
            } catch (Exception e) {
                Log.getLog(Question.class).error(e.getMessage(), e);
            }
        }

        public Collection getTableRows() {
            String keyword = (String) getFilterValue("topic");
            List courseList = (List) getFilterValue("course");
            String course = "-1";

            Application application = Application.getInstance();
            QuestionModule module = (QuestionModule) application.getModule(QuestionModule.class);

            return module.getQuestions(keyword, course, getSort(), isDesc(),
                getStart(), getRows());
        }

        public int getTotalRowCount() {
            String keyword = (String) getFilterValue("topic");
            List courseList = (List) getFilterValue("course");
            String course = "-1";

            Application application = Application.getInstance();
            QuestionModule module = (QuestionModule) application.getModule(QuestionModule.class);

            return module.countQuestions(keyword, course);
        }

        public Forward processAction(Event evt, String action,
            String[] selectedKeys) {
            if ("add".equals(action)) {
                return new Forward("add");
            } else if ("delete".equals(action)) {
                Application application = Application.getInstance();
                QuestionModule module = (QuestionModule) application.getModule(QuestionModule.class);

                if(selectedKeys.length <=0) return new Forward("notSelected");

                for (int i = 0; i < selectedKeys.length; i++) {
                    module.deleteQuestion(selectedKeys[i]);
                }

                return new Forward("deleted");
            }

            return null;
        }

        public String getTableRowKey() {
            return "id";
        }
    }
}
