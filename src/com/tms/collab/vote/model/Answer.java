package com.tms.collab.vote.model;

import kacang.model.DefaultDataObject;

/**
 * Created by IntelliJ IDEA.
 * User: Disen
 * Date: Apr 22, 2003
 * Time: 4:57:42 PM
 * To change this template use Options | File Templates.
 */
public class Answer extends DefaultDataObject
{
    private String id;
    private String answer;
    private String q_id;
    private long total;
    private Question question;
    private float percentage;
    private int piority;

    public int getPiority()
    {
        return piority;
    }

    public void setPiority(int piority)
    {
        this.piority = piority;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getAnswer()
    {
        return answer;
    }

    public void setAnswer(String answer)
    {
        this.answer = answer;
    }

    public long getTotal()
    {
        return total;
    }

    public void setTotal(long total)
    {
        this.total = total;
    }

    public Question getQuestion()
    {
        return question;
    }

    public void setQuestion(Question question)
    {
        this.question = question;
    }

    public void vote()
    {
        total++;
    }

    public String getQ_id()
    {
        return q_id;
    }

    public void setQ_id(String q_id)
    {
        this.q_id = q_id;
    }

    public float getPercentage()
    {
        return this.percentage ;
    }

    public void setPercentage(float percentage)
    {
        this.percentage = percentage;
    }
}
