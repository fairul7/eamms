package com.tms.collab.vote.ui;

import com.tms.collab.vote.model.PollModule;
import com.tms.collab.vote.model.Question;
import kacang.Application;
import kacang.ui.Event;
import kacang.ui.LightWeightWidget;

/**
 * Created by IntelliJ IDEA.
 * User: Disen
 * Date: Apr 28, 2003
 * Time: 11:51:15 AM
 * To change this template use Options | File Templates.
 */
public class VoteResult extends LightWeightWidget
{
    private Question question=null;
    private String questionID;

/*
    public VoteResult(Question question)
    {
        this.question = question;
    }
*/
    public String getDefaultTemplate()
    {
        return "vote/voteresult";
    }


    public String getTemplate()
    {
        return "vote/voteresult";
    }

    public void onRequest(Event evt)
    {
        try
        {
            if(questionID!=null&&!questionID.equals("0"))
            {
                PollModule pm = (PollModule) Application.getInstance().getModule(PollModule.class);
                if(question==null)
                {
                    question = pm.getQuestion(questionID);

                }
                pm.calculatePercentage(question.getAnswers());
            }
        }
        catch(Exception e)
        {
        }
    }

    public Question getQuestion()
    {
        return question;
    }

    public void setQuestion(Question question)
    {
        this.question = question;
    }

    public String getQuestionID()
    {
        return questionID;
    }

    public void setQuestionID(String questionID)
    {
        this.questionID = questionID;
    }
}
