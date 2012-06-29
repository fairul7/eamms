package com.tms.collab.vote.ui;

import com.tms.collab.vote.model.PollModule;
import com.tms.collab.vote.model.Question;
import kacang.Application;
import kacang.ui.Event;
import kacang.ui.LightWeightWidget;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by IntelliJ IDEA.
 * User: Disen
 * Date: May 26, 2003
 * Time: 11:24:45 AM
 * To change this template use Options | File Templates.
 */
public class ComplexVote extends LightWeightWidget
{

    private String voteUrl ="vote.jsp";

    public String getVoteUrl()
    {
        return voteUrl;
    }

    public void setVoteUrl(String voteUrl)
    {
        this.voteUrl = voteUrl;
    }

    public void onRequest(Event evt)
    {
        try
        {
            HttpServletRequest req = evt.getRequest();
            String vote = req.getParameter("VOTE");
            if(vote!=null&&vote.equals("Vote"))
            {
                String voteChoice = req.getParameter("votechoice");
                if(voteChoice==null)
                {
                    String voteID = req.getParameter("VoteID");
                    if(voteID!=null)
                    {
                        PollModule pm = (PollModule) Application.getInstance().getModule(PollModule.class);
                        Question question = pm.getQuestion(voteID);
                        if(question!=null)
                        {
                            if(question.getQuestion().indexOf("<IMG")!=-1)
                            {
                                evt.getResponse().sendRedirect(voteUrl+"?VoteID="+voteID);
                            }
                        }
                    }
                }
            }

        }
        catch(Exception e)
        {        }

    }

    public String getDefaultTemplate() {
        return "";
    }

}
