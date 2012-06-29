package com.tms.hr.competency;

import kacang.services.security.User;
import kacang.services.security.SecurityService;
import kacang.services.security.SecurityException;
import kacang.Application;
import kacang.util.Log;

import java.util.Map;
import java.util.HashMap;

public class UserCompetencies
{
    private User user;
    private Map competencies;

    public UserCompetencies()
    {
        competencies = new HashMap();
    }

    public User getUser()
    {
        return user;
    }

    public void setUser(User user)
    {
        this.user = user;
    }

    public Map getCompetencies()
    {
        return competencies;
    }

    public void setCompetencies(Map competencies)
    {
        this.competencies = competencies;
    }

    public String getUserId()
    {
        String userId = "";
        if(user != null)
            userId = user.getId();
        return userId;
    }

    public void setUserId(String userId)
    {
        if(!(userId == null || "".equals(userId)))
        {
            SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
            try
            {
                user = service.getUser(userId);
            }
            catch (SecurityException e)
            {
                user = null;
                Log.getLog(UserCompetencies.class).error(e.getMessage(), e);
            }
        }
    }

    public void addCompetency(Competency competency, String competencyLevel)
    {
        competencies.put(competency, competencyLevel);
    }

    public void removeCompetency(Competency competency)
    {
        competencies.remove(competency);
    }
}
