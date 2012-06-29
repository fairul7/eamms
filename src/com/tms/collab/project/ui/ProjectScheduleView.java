package com.tms.collab.project.ui;

import java.util.Collection;
import java.util.Iterator;

import kacang.Application;
import kacang.services.security.User;
import kacang.stdui.Button;
import kacang.stdui.Form;
import kacang.stdui.SelectBox;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

import com.tms.collab.project.Project;
import com.tms.collab.project.WormsException;
import com.tms.collab.project.WormsHandler;

public class ProjectScheduleView extends Form
{
    public static final String DEFAULT_TEMPLATE = "project/projectScheduleView";
    private Button daily;
    private Button monthly;
    private Button weekly;
    private String type;

    public void init()
    {

        super.init();       
        daily = new Button("daily","Daily");
        daily.setOnClick("submit()");
        addChild(daily);
        monthly = new Button("monthly","Monthly");
        monthly.setOnClick("submit()");
        addChild(monthly);
        weekly = new Button("weekly","Weekly");
        weekly.setOnClick("submit()");
        addChild(weekly);
    }

    public void onRequest(Event event)
    {
        super.onRequest(event);
        init();
        type="daily";       
    }

    public Forward onSubmit(Event event)
    {
        Forward forward = super.onSubmit(event);
       
        if((daily.getAbsoluteName().equals(findButtonClicked(event)))){
            type="daily";
        }else if((monthly.getAbsoluteName().equals(findButtonClicked(event)))){
            type="monthly";
        }else if((weekly.getAbsoluteName().equals(findButtonClicked(event)))){
            type="weekly";
        }
           
        return forward;
    }

    public String getDefaultTemplate()
    {
        return DEFAULT_TEMPLATE;
    }

    /* Getters and Setters */
    
	public Button getDaily() {
		return daily;
	}

	public void setDaily(Button daily) {
		this.daily = daily;
	}

	public Button getMonthly() {
		return monthly;
	}

	public void setMonthly(Button monthly) {
		this.monthly = monthly;
	}

	public Button getWeekly() {
		return weekly;
	}

	public void setWeekly(Button weekly) {
		this.weekly = weekly;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
