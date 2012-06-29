package com.tms.cms.personalization;

import kacang.services.security.User;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: Michael
 * Date: Jun 17, 2003
 * Time: 2:44:02 PM
 * To change this template use Options | File Templates.
 */
public class PersonalizationSetting
{
    private User user;
    private String userId;
    private String isSection;
    private int numberArticles;
    private int numberTopics;
    private Collection sections;
    private Collection forums;

    public PersonalizationSetting()
    {
        sections = new ArrayList();
        forums = new ArrayList();
    }

    public User getUser()
    {
        return user;
    }

    public void setUser(User user)
    {
        this.user = user;
    }

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public String getIsSection()
    {
        return isSection;
    }

    public void setIsSection(String isSection)
    {
        this.isSection = isSection;
    }

    public int getNumberArticles()
    {
        return numberArticles;
    }

    public void setNumberArticles(int numberArticles)
    {
        this.numberArticles = numberArticles;
    }

    public int getNumberTopics()
    {
        return numberTopics;
    }

    public void setNumberTopics(int numberTopics)
    {
        this.numberTopics = numberTopics;
    }

    public Collection getSections()
    {
        return sections;
    }

    public void setSections(Collection sections)
    {
        this.sections = sections;
    }

    public Collection getForums()
    {
        return forums;
    }

    public void setForums(Collection forums)
    {
        this.forums = forums;
    }
}
