package com.tms.elearning.course.ui;

import com.tms.elearning.course.model.Course;
import com.tms.elearning.course.model.CourseModule;
import com.tms.elearning.testware.model.QuestionModule;
import com.tms.elearning.testware.model.StudentStatistic;

import kacang.Application;

import kacang.model.DaoQuery;
import kacang.model.DataObjectNotFoundException;

import kacang.model.operator.DaoOperator;
import kacang.model.operator.OperatorEquals;

import kacang.services.security.SecurityService;
import kacang.services.security.User;

import kacang.stdui.Form;
import kacang.stdui.Label;

import kacang.ui.Event;

import java.text.SimpleDateFormat;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Random;


public class StudentDetail extends Form {
    protected String courseId;
    protected String studentId;
    protected String studentName;
    protected String courseName;



    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public void init() {
    }

    public void onRequest(Event evt) {
        removeChildren();

        setColumns(5);
        Application application = Application.getInstance();
        Label studentNameLabel = new Label("studentName",
                "<b>" +
                application.getMessage("eLearning.studentDetail.studentname") +
                "</b>");

        //create name list from id
        //convert id into user name
        if ((getStudentId() != null) && !("".equals(getStudentId()))) {
            SecurityService ss = (SecurityService) Application.getInstance()
                                                              .getService(SecurityService.class);
            DaoQuery query = new DaoQuery();

            query.addProperty(new OperatorEquals("id", getStudentId(),
                    DaoOperator.OPERATOR_AND));

            Collection userCollection = null;

            try {
                userCollection = ss.getUsers(query, 0, -1, "username", false);
            } catch (kacang.services.security.SecurityException e) {
                e.printStackTrace();
            }

            User user = null;

            for (Iterator icount = userCollection.iterator(); icount.hasNext();) {
                user = (User) icount.next();
                setStudentName(user.getName());
            }
        } else {
           // setStudentName(application.getCurrentUser().getUsername());
             setStudentName(application.getCurrentUser().getProperty("firstName")+" "+application.getCurrentUser().getProperty("lastName"));  
            setStudentId(application.getCurrentUser().getId());
        }

        Label studentNameField = new Label("studentNameField", getStudentName());
        Label courseName = new Label("courseName",
                "<b>" +
                application.getMessage("eLearning.course.label.courseName") +
                "</b>");

        studentNameLabel.setColspan(1);
        addChild(studentNameLabel);
        studentNameField.setColspan(4);
        addChild(studentNameField);

        Course cour = null;

        try {
            CourseModule module = (CourseModule) Application.getInstance()
                                                            .getModule(CourseModule.class);

            if (module.loadCourse(getCourseId()) != null) {
                cour = module.loadCourse(getCourseId());
                setCourseName(cour.getName());
            }
        } catch (DataObjectNotFoundException e) {
            e.printStackTrace(); //To change body of catch statement use File | Settings | File Templates.
        }

        if (cour != null) {
            courseName.setColspan(1);
            addChild(courseName);

            Label courseNameField = new Label("courseNameField", getCourseName());
            courseNameField.setColspan(4);
            addChild(courseNameField);
        }

        Label horizontalLine = new Label("horizontalLine1", "<hr>");
        horizontalLine.setColspan(5);
        addChild(horizontalLine);

        CourseModule module = (CourseModule) Application.getInstance()
                                                        .getModule(CourseModule.class);

        try {
            Collection moduleColl = module.queryCourseStatistic(getStudentId(),
                    getCourseId());


            int counting = new Random(1000).nextInt();

            for (Iterator iterator = moduleColl.iterator(); iterator.hasNext();
                 counting++) {
                Course tempCourse = (Course) iterator.next();

                String tempModuleId = tempCourse.getModuleId();

                //moduleName
                Label horizontalLine2 = new Label("horizontalLine2", "<hr>");
                horizontalLine2.setColspan(5);
                addChild(horizontalLine2);

                Label moduleName = new Label("moduleName" + counting,
                        "<b>" +
                        application.getMessage(
                            "eLearning.folder.label.folderName") + "</b>");

                Label moduleNameField = new Label("moduleNameField" + counting,
                        tempCourse.getModuleName());

                moduleName.setColspan(1);
                addChild(moduleName);
                moduleNameField.setColspan(4);
                addChild(moduleNameField);

                // name, dateTook, total_questions, wrong_questions
                Label assessmentName = new Label("assessmentName" + counting,
                        "<b>" +
                        application.getMessage(
                            "eLearning.assessment.assessmentname") + "</b>");

                Label dateTook = new Label("dateTook" + counting,
                        "<b>" +
                        application.getMessage("eLearning.assessment.date") +
                        "</b>");

                Label score = new Label("score" + counting,
                        "<b>" +
                        application.getMessage("eLearning.studentDetail.score") +
                        "</b>");

                dateTook.setColspan(1);
                addChild(dateTook);

                assessmentName.setColspan(1);
                addChild(assessmentName);

                //menu for statistic
                Label wrongQuestion = null;

                /*  Label wrongQuestionField=null;*/
                Label totalQuestion = null;

                /*Label totalQuestionField=null ;*/
                wrongQuestion = new Label("wrongQuestion" + counting,
                        "<b>" +
                        application.getMessage(
                            "eLearning.assessment.statistic.wronganswer") +
                        "</b>");
                totalQuestion = new Label("totalQuestion" + counting,
                        "<b>" +
                        application.getMessage(
                            "eLearning.assessment.totalquestion") + "</b>");

                wrongQuestion.setColspan(1);
                addChild(wrongQuestion);
                totalQuestion.setColspan(1);
                addChild(totalQuestion);

                score.setColspan(1);
                addChild(score);

                //statistic for each assessment
                QuestionModule qModule = (QuestionModule) application.getModule(QuestionModule.class);
                Collection getAssessmentSet = qModule.getAssessmentHistoryByUserAndModule(studentId,
                        tempModuleId);
                StudentStatistic studentStatistic = new StudentStatistic();
                int countElement = 0;

                for (Iterator icount = getAssessmentSet.iterator();
                     icount.hasNext(); countElement++) {
                    studentStatistic = (StudentStatistic) icount.next();

                    //get date
                    Date assessmentDateFromObject = new Date();
                    assessmentDateFromObject = studentStatistic.getDateTook();

                    SimpleDateFormat sdf = new SimpleDateFormat(
                            "dd/MM/yyyy hh:mm:ss");

                    Label dateTookField = new Label("dateTookField" + counting +countElement
                          , sdf.format(assessmentDateFromObject));

                    Label assessmentNameField = new Label("assessmentNameField" +
                            counting+countElement,
                            studentStatistic.getName());
                    Label wrongQuestionField = new Label("wrongQuestionField" +
                             counting+countElement,
                            studentStatistic.getWrong_questions().toString());
                    Label totalQuestionField = new Label("totalQuestionField" +
                             counting+countElement,
                            studentStatistic.getTotal_questions().toString());

                    String scoreStr = Integer.toString(
							((studentStatistic.getTotal_questions().intValue() -
                            studentStatistic.getWrong_questions().intValue()) * 100) / 
							studentStatistic.getTotal_questions().intValue());
                    scoreStr += "%";

                    Label scoreField = new Label("scoreField" +counting+countElement, scoreStr);

                    dateTookField.setColspan(1);
                    addChild(dateTookField);

                    assessmentNameField.setColspan(1);
                    addChild(assessmentNameField);
                    wrongQuestionField.setColspan(1);
                    addChild(wrongQuestionField);
                    totalQuestionField.setColspan(1);
                    addChild(totalQuestionField);
                    scoreField.setColspan(1);
                    addChild(scoreField);
                }

                if (countElement <= 0) {
                    Label dateTookFieldEmpty = new Label("dateTookFieldEmpty", "-");

                    Label assessmentNameFieldEmpty = new Label("assessmentNameFieldEmpty",
                            "-");
                    Label wrongQuestionFieldEmpty = new Label("wrongQuestionFieldEmpty",
                            "-");
                    Label totalQuestionFieldEmpty = new Label("totalQuestionFieldEmpty",
                            "-");
                      Label scoreFieldEmpty = new Label("scoreFieldEmpty",
                            "-");

                    addChild(dateTookFieldEmpty);
                    addChild(assessmentNameFieldEmpty);
                    addChild(wrongQuestionFieldEmpty);
                    addChild(totalQuestionFieldEmpty);
                    addChild(scoreFieldEmpty);
                }
            }
        } catch (DataObjectNotFoundException e) {
            e.printStackTrace();
        }
    }
}
