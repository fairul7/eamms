package com.tms.elearning.testware.ui;

import com.tms.elearning.testware.model.*;

import kacang.Application;

import kacang.stdui.*;

import kacang.stdui.validator.ValidatorNotEmpty;

import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.ui.Widget;
import kacang.ui.WidgetManager;

import java.text.SimpleDateFormat;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Calendar;
import java.io.Serializable;


public class QuestionTableStud extends Form implements Serializable{
    public static final String FORWARD_SUCCESS = "success";
    public static final String FORWARD_ERROR = "error";
    private ValidatorNotEmpty validEntry;
    protected String assessmentId;
    private ButtonGroup bg1;
    protected Button submitButton;
    protected int c; //count number of buttongroup

    public String getAssessmentId() {
        return assessmentId;
    }

    public void setAssessmentId(String assessmentId) {
        this.assessmentId = assessmentId;
    }

    public ButtonGroup getBg1() {
        return bg1;
    }

    public void setBg1(ButtonGroup bg1) {
        this.bg1 = bg1;
    }

    public int getC() {
        return c;
    }

    public void setC(int c) {
        this.c = c;
    }

    public void init() {
    }

    public void onRequest(Event event) {
        removeChildren();
        setC(1);

        Question question = new Question();
        Application application = Application.getInstance();
        QuestionModule module = (QuestionModule) application.getModule(QuestionModule.class);

        Collection questionSet = module.showExamQuestionsByAssessmentSelected(getAssessmentId(),
                false, 0, -1);

        //check active or not
        AssessmentModule amodule = (AssessmentModule)application.getModule(AssessmentModule.class);
        Date todayDate = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");

        if( !("".equals(amodule.checkActive(getAssessmentId(),formatter.format(todayDate),formatter.format(todayDate)).getIs_public())))
        {

             String activate = amodule.checkActive(getAssessmentId(),formatter.format(todayDate),formatter.format(todayDate)).getIs_public();
            if("1".equals(activate)){

        int tempNumb;
        setColumns(1);

        for (Iterator icount = questionSet.iterator(); icount.hasNext();) {
            question = (Question) icount.next();

            Label displayQuestion = new Label("label" + getC(),
                    getC() + ") " + question.getQuestion());
            addChild(displayQuestion);

            bg1 = new ButtonGroup("ButtonGroup" + getC());

            tempNumb = getC();
            tempNumb += 1;
            setC(tempNumb);
            bg1.setType(ButtonGroup.RADIO_TYPE);

            String[] questionOption = {
                question.getAnswer_a(), question.getAnswer_b(),
                question.getAnswer_c(), question.getAnswer_d()
            };

            for (int i = 0; i < 4; i++) {
                Radio radio = new Radio("radio" + i);
                radio.setText(questionOption[i]);

                bg1.addButton(radio);
            }

            validEntry = new ValidatorNotEmpty("validEntry" + getC(),
                    application.getMessage(
                        "eLearning.assessment.answerallquestions"));
            bg1.addChild(validEntry);
            addChild(bg1);

            submitButton = new Button("submit",
                    application.getMessage("eLearning.assessment.checkanswer"));
            addChild(submitButton);
        }

            }//if
        }//if
    }

    public Forward onValidate(Event evt) {
        int CountWrongQuestions = 0;

        Question question = new Question();
        Application application = Application.getInstance();
        QuestionModule module = (QuestionModule) application.getModule(QuestionModule.class);

        Collection questionSet = module.showExamQuestionsByAssessmentSelected(getAssessmentId(),
                false, 0, -1);

        Widget childWidget;

        // get selected button
        WidgetManager wm = evt.getWidgetManager();
        String buttonClicked = findButtonClicked(evt);
        Widget button = wm.getWidget(buttonClicked);

        if (submitButton.equals(button)) {
            //retrieve last freqency of assessment
            Collection freqAssessment = module.getAssessmentFreqStudent(application.getCurrentUser()
                                                                                   .getId(),
                    getAssessmentId());

            int frequency = 0;
            Iterator iterator = freqAssessment.iterator();

            while (iterator.hasNext()) {
                Assessment assessmentfreq = (Assessment) iterator.next();
                frequency = assessmentfreq.getNumbTaken(); //get frequency
            }

            frequency += 1; //increment frequency

            int i = 1;


            module.deleteAssessmentStudent(application.getCurrentUser().getId(),           getAssessmentId());  //reset previous test

            for (Iterator icount = questionSet.iterator(); icount.hasNext();
                    i++) {
                ButtonGroup bg = (ButtonGroup) getChild("ButtonGroup" + i);

                question = (Question) icount.next();

                String answer= (String) bg.getValue();



                int point = answer.indexOf(".");
                String realAnswerTemp = answer.substring(point - 1, point);
                char[] realAnswer = realAnswerTemp.toCharArray();
                String answerSelected;
                String answerSelectedDetail = "";

                switch (realAnswer[0]) {
                case '0':
                    answerSelected = "A";

                    if (question.getAnswer_a() == null) {
                        answerSelectedDetail = "";
                    } else {
                        answerSelectedDetail = question.getAnswer_a();
                    }

                    break;

                case '1':
                    answerSelected = "B";

                    if (question.getAnswer_b() == null) {
                        answerSelectedDetail = "";
                    } else {
                        answerSelectedDetail = question.getAnswer_b();
                    }

                    break;

                case '2':
                    answerSelected = "C";

                    if (question.getAnswer_c() == null) {
                        answerSelectedDetail = "";
                    } else {
                        answerSelectedDetail = question.getAnswer_c();
                    }

                    break;

                case '3':
                    answerSelected = "D";

                    if (question.getAnswer_d() == null) {
                        answerSelectedDetail = "";
                    } else {
                        answerSelectedDetail = question.getAnswer_d();
                    }

                    break;

                default:
                    answerSelected = "E";
                    answerSelectedDetail = "";

                    break;
                }

                //compare answer with database      , getC() is total question , i is current question
                if (answerSelected.equalsIgnoreCase(
                            question.getCorrect_answer())) {



                } else {
                    CountWrongQuestions += 1;

                    StudentAssessmentHistory studentAssessmentHistory = new StudentAssessmentHistory();

                    studentAssessmentHistory.setUser_id(application.getCurrentUser()
                                                                   .getId());
                    studentAssessmentHistory.setQuestion(question.getQuestion());
                    studentAssessmentHistory.setWrong_answer(answerSelectedDetail);

                    char[] tempCorrectAnswer = question.getCorrect_answer()
                                                       .toCharArray();
                    String correctAnswer = "";

                    switch (tempCorrectAnswer[0]) {
                    case 'A':
                        correctAnswer = question.getAnswer_a();

                        break;

                    case 'B':
                        correctAnswer = question.getAnswer_b();

                        break;

                    case 'C':
                        correctAnswer = question.getAnswer_c();

                        break;

                    case 'D':
                        correctAnswer = question.getAnswer_d();

                        break;

                    default:
                        correctAnswer = "";

                        break;
                    }

                    studentAssessmentHistory.setRight_answer(correctAnswer);
                    studentAssessmentHistory.setAssessment_id(getAssessmentId());
                    studentAssessmentHistory.setNumbTaken(frequency);

                    //set lastest assessment date taken by student
                    SimpleDateFormat formatter = new SimpleDateFormat(
                            "yyyy-MM-dd HH:mm:ss");
                    Date today = new Date(System.currentTimeMillis());

                    studentAssessmentHistory.setLastTakenDate(today);

                //    module.deleteAssessmentStudent(application.getCurrentUser().getId(),           getAssessmentId());

                    module.insertStudentAssessmentHistory(studentAssessmentHistory);
                }




            }


            //if all questions answered correctly
            if(CountWrongQuestions <=0){

                  StudentAssessmentHistory studentAssessmentHistory = new StudentAssessmentHistory();

                    studentAssessmentHistory.setUser_id(application.getCurrentUser()
                                                                   .getId());
                    studentAssessmentHistory.setQuestion("-");
                    studentAssessmentHistory.setWrong_answer("-");

                    studentAssessmentHistory.setRight_answer("-");
                    studentAssessmentHistory.setAssessment_id(getAssessmentId());
                    studentAssessmentHistory.setNumbTaken(frequency);

                    //set lastest assessment date taken by student
                    SimpleDateFormat formatter = new SimpleDateFormat(
                            "yyyy-MM-dd HH:mm:ss");
                    Date today = new Date(System.currentTimeMillis());

                    studentAssessmentHistory.setLastTakenDate(today);


                 module.insertStudentAssessmentHistory(studentAssessmentHistory);




            }


            // calculate statistic and put into database
            StudentStatistic studentStatistic = new StudentStatistic();

            studentStatistic.setAssessment_id(getAssessmentId());

            Date today = new Date(System.currentTimeMillis());

            studentStatistic.setDateTook(today);

            studentStatistic.setUser_id(application.getCurrentUser().getId());

            studentStatistic.setWrong_questions(new Integer(CountWrongQuestions));
            studentStatistic.setTotal_questions(new Integer(getC() - 1));

            module.insertrecordStudentStatistic(studentStatistic);
        }

        return new Forward(FORWARD_SUCCESS);
    }
}
