package com.tms.elearning.testware.ui;

import com.tms.elearning.testware.model.QuestionModule;
import com.tms.elearning.testware.model.StudentStatistic;

import kacang.Application;

import kacang.stdui.Button;
import kacang.stdui.Form;
import kacang.stdui.Label;

import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.ui.Widget;
import kacang.ui.WidgetManager;

import java.io.Serializable;

import java.text.SimpleDateFormat;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;


public class resultAssessmentsForm extends Form implements Serializable {
    private Button backButton;
    private String module_id;


    public void init() {
        Application application = Application.getInstance();
        setColumns(5);

        Label statisticLabel = new Label("Statistic_Label",
                "<b>" +
                application.getMessage(
                    "eLearning.assessment.assessmentstatistic") + "</b>");
        statisticLabel.setColspan(5);
        addChild(statisticLabel);

        Label assessmentDate = new Label("assessmentDate",
                "<b>" + application.getMessage("eLearning.assessment.date") +
                "</b>");
        assessmentDate.setColspan(1);
        addChild(assessmentDate);

        Label assessmentName = new Label("assessmentName",
                "<b>" +
                application.getMessage("eLearning.assessment.assessmentname") +
                "</b>");
        assessmentName.setColspan(1);
        addChild(assessmentName);

        Label totalWrong = new Label("totalWrong",
                "<b>" +
                application.getMessage("eLearning.assessment.totalwrong") +
                "</b>");
        totalWrong.setColspan(1);
        addChild(totalWrong);

        Label totalQuestions = new Label("totalQuestions",
                "<b>" +
                application.getMessage("eLearning.assessment.totalquestion") +
                "</b>");
        totalQuestions.setColspan(1);
        addChild(totalQuestions);

        Label assessmentScore = new Label("assessmentScore",
                "<b>" + application.getMessage("eLearning.assessment.score") +
                "</b>");
        assessmentScore.setColspan(1);
        addChild(assessmentScore);

        QuestionModule module = (QuestionModule) application.getModule(QuestionModule.class);
        Collection studentHistory = module.showAssessmentHistoryByStudent(application.getCurrentUser()
                                                                                     .getId(),
                false, 0, -1);
        StudentStatistic studentStatistic = new StudentStatistic();
        int countElement = 0;

        for (Iterator icount = studentHistory.iterator(); icount.hasNext();
                countElement++) {
            studentStatistic = (StudentStatistic) icount.next();

            Date assessmentDateFromObject = new Date();
            assessmentDateFromObject = studentStatistic.getDateTook();

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");

            Label assessmentDateLabel = new Label("AssessmentDateTook" +
                    countElement, sdf.format(studentStatistic.getDateTook()));
            Label assessmentNameLabel = new Label("AssessmentNameLabel" +
                    countElement, studentStatistic.getName());
            Label assessmentNameLabel2 = new Label("AssessmentWrongLabel" +
                    countElement, studentStatistic.getWrong_questions().toString());
            Label assessmentNameLabel3 = new Label("AssessmentTotalLabel" +
                    countElement, studentStatistic.getTotal_questions().toString());

            // count score  out of 100
            float score = ((studentStatistic.getTotal_questions().floatValue() -
                	studentStatistic.getWrong_questions().floatValue()) / 
					studentStatistic.getTotal_questions().floatValue()) * 100;

            Label assessmentNameLabel4 = null;

            if (!(Float.isInfinite(score)) && !(Float.isNaN(score))) {
                assessmentNameLabel4 = new Label("AssessmentScoreLabel" +
                        countElement, Float.toString(score));
            } else {
                score = 0;
                assessmentNameLabel4 = new Label("AssessmentScoreLabel" +
                        countElement, Float.toString(score));
            }

            assessmentDateLabel.setColspan(1);
            assessmentNameLabel.setColspan(1);
            assessmentNameLabel2.setColspan(1);
            assessmentNameLabel3.setColspan(1);
            assessmentNameLabel4.setColspan(1);
            addChild(assessmentDateLabel);
            addChild(assessmentNameLabel);
            addChild(assessmentNameLabel2);
            addChild(assessmentNameLabel3);
            addChild(assessmentNameLabel4);
        }
        setModule_id(studentStatistic.getModule_id());  //set module id , use when need to press back button

        resultAssessmentsTable rat = new resultAssessmentsTable(
                "assessmentTookTable");
        rat.init();

        rat.setWidth("100%");
        rat.setColspan(5);
        addChild(rat);

        //add back button
        backButton = new Button("backButton",
                Application.getInstance().getMessage("eLearning.assessment.back"));
        addChild(backButton);
    }

    public Forward onSubmit(Event evt) {
        super.onSubmit(evt);

        WidgetManager wm = evt.getWidgetManager();
        String buttonClicked = findButtonClicked(evt);
        Widget button = wm.getWidget(buttonClicked);

        if (backButton.equals(button)) {
            return new Forward("back");
        }

        return null;
    }


    public String getModule_id() {
        return module_id;
    }

    public void setModule_id(String module_id) {
        this.module_id = module_id;
    }
}
