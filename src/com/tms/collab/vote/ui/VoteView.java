package com.tms.collab.vote.ui;


import com.tms.collab.vote.model.Answer;
import com.tms.collab.vote.model.PollModule;
import com.tms.collab.vote.model.Question;
import kacang.Application;
import kacang.services.security.SecurityService;
import kacang.ui.Event;
import kacang.ui.LightWeightWidget;
import kacang.ui.WidgetManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Vector;


public class VoteView extends LightWeightWidget
{
    private boolean voted = false;
    private String url = "";
    private String id,sectionId;
    private String title = "Default Title";
    private Question question;
    private Answer[] answers;
    private boolean showImage = false;
    private boolean images = false;
    protected static final String name = "Vote";


    public VoteView()
    {
        this.setName(name);
    }

    public VoteView(String name)
    {
        this.setName(name);
    }

    public String getSectionId()
    {
        return sectionId;
    }

    public void setSectionId(String sectionId)
    {
        this.sectionId = sectionId;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public boolean isShowImage()
    {
        return showImage;
    }

    public void setShowImage(boolean showImage)
    {
        this.showImage = showImage;
    }

    private boolean hasImage()
    {
        return question.getQuestion().indexOf("<IMG") != -1;
    }

    public boolean isImages()
    {
        return images;
    }

    public void setImages(boolean images)
    {
        this.images = images;
    }

    public void onRequest(Event evt)
    {
        try
        {
            question = null;
            HttpServletRequest req = evt.getRequest();
            HttpSession session = req.getSession(false);
            String choice = req.getParameter("votechoice");
            String voteID = req.getParameter("VoteID");
            String result = req.getParameter("Result");
            PollModule pm = (PollModule) Application.getInstance().getModule(PollModule.class);
            if (id != null)
                question = pm.getQuestion(id);
            else if (sectionId != null && sectionId.trim().length() > 0)
                question = pm.getQuestionByAssignment(sectionId);
            if (question != null)
            {
                id = question.getId();
                String userID = WidgetManager.getWidgetManager(evt.getRequest()).getUser().getId();
                if (userID.equals(SecurityService.ANONYMOUS_USER_ID) && !question.getIspublic())
                {
                    question = null;
                } else
                {
                    setQuestion(question);
                    setAnswers(question.getAnswers());
                    setImages(hasImage());
                    if (voteID != null && voteID.equals(question.getId()))
                    {
                        //String voted = (String)session.getAttribute("voted");
                        if (result != null)
                        {
                            this.voted = true;
                        } else if (choice != null)
                        {
                            this.voted = true;
                            List votedList = (List) session.getAttribute("votedList");

                            if (votedList == null)
                                votedList = new Vector(5, 5);
                            if (!votedList.contains(id))
                            {
                                votedList.add(id);
                                pm.vote(question, choice);
                                if (getAnswers() != null)
                                    pm.calculatePercentage(getAnswers());
                                session.setAttribute("votedList", votedList);
                            }
                        }
                    }
                }
            }
        } catch (Exception e)
        {
        }
    }


    public String getDefaultTemplate()
    {
        if (!voted)
            return "vote/vote";
        return "vote/pollresult";
    }


    public void setQuestion(Question question)
    {
        this.question = question;
    }

    public Question getQuestion()
    {
        return this.question;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getTitle()
    {
        return this.title;
    }

    public Answer[] getAnswers()
    {
        return answers;
    }

    public void setAnswers(Answer[] answers)
    {
        this.answers = answers;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }


}