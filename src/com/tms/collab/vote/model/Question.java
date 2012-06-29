package com.tms.collab.vote.model;

import kacang.model.DefaultDataObject;

/**
 * Created by IntelliJ IDEA.
 * User: Disen
 * Date: Apr 22, 2003
 * Time: 2:01:36 PM
 * To change this template use Options | File Templates.
 */
public class Question extends DefaultDataObject
{
    public static final int STATUS_APPEND = 0;
    public static final int STATUS_ACTIVE = 1;
    public static final int STATUS_PUBLIC = 2;

    private String title;
    private String question;
    private String id;
    private String assigned;
    private long total;
    private Answer[] answers = null;
    private boolean active;
    private boolean ispublic;
    private boolean pending;


    public Question()
    {
        this.question = "";
        this.title = "";
        this.id = "0";
        assigned = "";
        active = false;
        ispublic = false;
        pending = false;
        total = 0;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public Question(String id, String question)
    {
        this.question = question;
        this.id = id;
    }


    public String getQuestion()
    {
        return this.question;
    }

    public void setQuestion(String question)
    {
        this.question = question;
    }

    public String getId()
    {
        return id;
    }

    public long getTotal()
    {
        return total;
    }

    public void setTotal(long total)
    {
        this.total = total;
    }

    public String getAssigned()
    {
        return assigned;
    }

    public void setAssigned(String assigned)
    {
        this.assigned = assigned;
    }

    public Answer[] getAnswers()
    {
        return answers;
    }

    public void setAnswers(Answer[] answers)
    {
        this.answers = answers;
    }

    public void vote()
    {
        total++;
    }

    public boolean isActive()
    {
        return active;
    }

    public void setActive(boolean active)
    {
        this.active = active;
    }

    public boolean getIspublic()
    {
        return ispublic;
    }

    public void setIspublic(boolean aPublic)
    {
        this.ispublic = aPublic;
    }

    public boolean isPending()
    {
        return pending;
    }

    public void setPending(boolean pending)
    {
        this.pending = pending;
    }


}
