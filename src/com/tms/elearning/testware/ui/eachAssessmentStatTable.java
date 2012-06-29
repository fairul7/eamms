package com.tms.elearning.testware.ui;

import kacang.stdui.Form;
import kacang.stdui.Label;
import kacang.stdui.Button;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.ui.WidgetManager;
import kacang.ui.Widget;
import kacang.Application;
import com.tms.elearning.testware.model.QuestionModule;
import com.tms.elearning.testware.model.StudentAssessmentHistory;

import java.util.Collection;
import java.util.Iterator;
import java.io.Serializable;

public class eachAssessmentStatTable extends Form implements Serializable{

    protected Button backButton;


    public String getAssessmentId() {
        return assessmentId;
    }

    public void setAssessmentId(String assessmentId) {
        this.assessmentId = assessmentId;
    }

    protected String assessmentId;


         public void init(){




         }



        public void onRequest(Event event) {


              Application application = Application.getInstance();
        QuestionModule module = (QuestionModule) application.getModule(QuestionModule.class);

          Collection wrongQuestions=  module.showWrongQuestionsByAssessmentId( getAssessmentId(), false, 0, -1);
            int countLabel =0;
             removeChildren();
            for (Iterator icount = wrongQuestions.iterator(); icount.hasNext();   countLabel++) {

                StudentAssessmentHistory studentAssessmentHistory  = (StudentAssessmentHistory) icount.next();
                setColumns(1);
                Label WrongQuestion = new Label("Question_label"+countLabel, "<b>"+application.getMessage("eLearning.assessment.statistic.question")+"</b>&nbsp;"+studentAssessmentHistory.getQuestion());

                addChild(WrongQuestion);

                Label WrongAnswer = new Label("WrongAnswer_label"+countLabel, "<b>"+application.getMessage("eLearning.assessment.statistic.wronganswer")+"</b>&nbsp;"+ studentAssessmentHistory.getWrong_answer());

                addChild(WrongAnswer);

                Label CorrectAnswer = new Label("CorrectAnswer_label"+countLabel, "<b>"+application.getMessage("eLearning.assessment.statistic.correctanswer")+"</b>&nbsp;"+ studentAssessmentHistory.getRight_answer());

                addChild(CorrectAnswer);


            }

            addChild(backButton = new Button("back",Application.getInstance().getMessage("eLearning.assessment.back")));




        }



    public Forward onValidate(Event evt) {


        WidgetManager wm = evt.getWidgetManager();
        String buttonClicked = findButtonClicked(evt);
        Widget button = wm.getWidget(buttonClicked);


          if (backButton.equals(button)) {
                return new Forward("back");
            }

      return null;
     }




}
