package com.tms.elearning.testware.ui;

import com.tms.elearning.testware.model.Question;
import com.tms.elearning.testware.model.QuestionModule;

import kacang.Application;

import kacang.services.security.SecurityService;
import kacang.services.security.User;

import kacang.stdui.Table;
import kacang.stdui.TableAction;
import kacang.stdui.TableColumn;
import kacang.stdui.TableModel;

import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.ui.WidgetManager;

import kacang.util.Log;
import kacang.util.UuidGenerator;

import java.util.Collection;
import java.io.Serializable;


/**
 * Created by IntelliJ IDEA.
 * User: mahe
 * Date: Nov 2, 2004
 * Time: 4:40:03 PM
 * To change this template use Options | File Templates.
 */
public class QuestionTableNew extends Table implements Serializable{
    private String examid;
    private String courseId;
    private boolean flag = false;
    protected String id;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void onRequest(Event event) {
        flag = false;

        String display = event.getRequest().getParameter("show");

        if ("yes".equalsIgnoreCase(display)) {
            flag = true;
        }

        init();
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

    public String getCourseId() {
        return this.courseId;
    }

    public void setCourseId(String courseid) {
        this.courseId = courseid;
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

            try {
                if (service.hasPermission(current.getId(),
                            "com.tms.elearning.testware.Add", null, null)) {
                    addAction(new TableAction("addToExam",
                            application.getMessage(
                                "eLearning.qusetions.label.addtoassessment")));
                }

                //addAction(new TableAction("cancel", "Cancel"));
            } catch (Exception e) {
                Log.getLog(Question.class).error(application.getMessage(
                        "eLearning.question.errorloadquestion") + " " +
                    e.toString(), e);
            }

            TableColumn questionCol = new TableColumn("question",
                    application.getMessage("eLearning.question.label"));
            addColumn(questionCol);

            try {
            } catch (Exception e) {
                Log.getLog(Question.class).error(e.getMessage(), e);
            }
        }

        public Collection getTableRows() {
            Application application = Application.getInstance();
            QuestionModule module = (QuestionModule) application.getModule(QuestionModule.class);

            return module.showExamQuestionsByAssessment(getId(), isDesc(),
                getStart(), getRows());
        }

        public int getTotalRowCount() {
            String course = getCourseId();
            Application application = Application.getInstance();
            QuestionModule module = (QuestionModule) application.getModule(QuestionModule.class);

            return module.countQuestionsByAssessment(getId());
        }

        public Forward processAction(Event evt, String action,
            String[] selectedKeys) {
            if ("cancel".equals(action)) {
                return new Forward("cancel");
            } else if ("addToExam".equals(action)) {
                UuidGenerator uuid = UuidGenerator.getInstance();

                Application application = Application.getInstance();
                QuestionModule module = (QuestionModule) application.getModule(QuestionModule.class);

                for (int i = 0; i < selectedKeys.length; i++) {

                    Question q = new Question();
                    q = module.getQuestion(selectedKeys[i]);
                    q.setQuestion_id(q.getId());
                    q.setQuestion(q.getQuestion());

                    String idn = uuid.getUuid();
                    q.setQbank_id(idn);

                    q.setExam_id(getExamid());

                  /*  //delete questions set from old assessment first
                    module.deleteExamQuestionsByAssessment(getExamid());
*/
                    //then only add new set of questions into assessment
                    module.addToExam(q);
                }

                return new Forward("addToExam");
            }

            return null;
        }

        public String getTableRowKey() {
            return "id";
        }
    }
}
