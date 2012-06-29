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
public class QuestionTableList extends Table implements Serializable{


    private String examid;
    private String courseId;
    private boolean flag = false;
    protected String id;



    //String ex1 = null,ex2=null;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void onRequest(Event event) {
        //  if (event.getRequest().getSession().getAttribute("courseid") != null) {
        //      ex1 = event.getRequest().getSession().getAttribute("courseid").toString();
        //   ex2 = event.getRequest().getSession().getAttribute("examid").toString();
        //   setCourseId(ex1);
        //   setExamid(ex2);
        flag = false;

        String display = event.getRequest().getParameter("show");

        if ("yes".equalsIgnoreCase(display)) {
            flag = true;
        }

        init();

        //    }
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

            //  addFilter(new TableFilter("topic"));
            try {


                //    if (service.hasPermission(current.getId(), "com.tms.elearning.testware.Delete", null, null))
                if (service.hasPermission(current.getId(),
                            "com.tms.elearning.testware.Delete", null, null)) {
                    addAction(new TableAction("delete", application.getMessage("eLearning.question.delete"),
                            application.getMessage("eLearning.question.deletequestions")));
                }

               // addAction(new TableAction("cancel", "Cancel"));
            } catch (Exception e) {
                Log.getLog(Question.class).error(application.getMessage("eLearning.question.errorloadquestion")+" " +
                    e.toString(), e);
            }

            TableColumn questionCol = new TableColumn("question", application.getMessage("eLearning.question.label"));
            addColumn(questionCol);

            try {
                //  if (service.hasPermission(current.getId(), "com.tms.elearning.testware.Edit", null, null))
                //   questionCol.setUrlParam("id");
            } catch (Exception e) {
                Log.getLog(Question.class).error(e.getMessage(), e);
            }
        }

        public Collection getTableRows() {
            Application application = Application.getInstance();
            QuestionModule module = (QuestionModule) application.getModule(QuestionModule.class);

            /*
              if(flag){
                  System.out.println("exam is "+getExamid());
                  return module.showExamQuestions(getExamid(),getSort(),isDesc(),getStart(),getRows());

              }else{
                  String courseId = getCourseId();

                  return module.getQuestions(null,courseId,getSort(),isDesc(),getStart(),getRows());

              }

              */
            return module.showExamQuestionsByAssessmentSelected(getId(), isDesc(),
                getStart(), getRows());



        }

        public int getTotalRowCount() {
            /*  String keyword = (String)getFilterValue("topic");
              List courseList = (List)getFilterValue("course");
              String course = "-1";

              if(courseList.size()>0) {
                  course = (String)courseList.get(0);
              }*/
            String course = getCourseId();
            Application application = Application.getInstance();
            QuestionModule module = (QuestionModule) application.getModule(QuestionModule.class);

            //  if (getExamid() == null) {
            // return module.countQuestions(null,course);
            return module.countQuestionsByAssessmentSelected(getId());

            //} /*else {
            //  return module.countQuestionsForExam(keyword,course,getExamid());
            //}   */
        }

        public Forward processAction(Event evt, String action,
            String[] selectedKeys) {
            if ("cancel".equals(action)) {
                //return new Forward("cancel","assessment.jsp",false);
                return new Forward("cancel");
            } else if ("delete".equals(action)) {
                Application application = Application.getInstance();
                QuestionModule module = (QuestionModule) application.getModule(QuestionModule.class);

                for (int i = 0; i < selectedKeys.length; i++) {
                    module.deleteQuestions(selectedKeys[i]);
                }

                return new Forward("deleted");
            }
            else if ("addToExam".equals(action)) {
                UuidGenerator uuid = UuidGenerator.getInstance();
                Question q = new Question();
                Application application = Application.getInstance();
                QuestionModule module = (QuestionModule) application.getModule(QuestionModule.class);

                for (int i = 0; i < selectedKeys.length; i++) {
                    q = module.getQuestion(selectedKeys[i]);
                    q.setQuestion_id(q.getId());
                    q.setQuestion(q.getQuestion());

                    String idn = uuid.getUuid();
                    q.setQbank_id(idn);

                    q.setExam_id(getExamid());

                    //   q.setExam_id(getId());
                    //   module.insertSelectedExamQuestion(q);

                    //delete questions set from old assessment first
                    module.deleteExamQuestionsByAssessment(getExamid());
                    //then only add new set of questions into assessment
                    module.addToExam(q);



                }

                return new Forward("addToExam");
            }

            //  return new Forward("assessment","assessment.jsp",false);
            return null;
        }

        public String getTableRowKey() {
            return "id";
        }





    }
}
