package com.tms.ekms.statistics.ui;

import com.tms.ekms.statistics.StatisticsException;
import com.tms.ekms.statistics.StatisticsHandler;
import kacang.Application;
import kacang.services.security.*;
import kacang.services.security.SecurityException;
import kacang.stdui.*;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;
import org.apache.commons.collections.SequencedHashMap;

import java.util.*;

public class MostActiveUsers extends Form
{
	public static final String FORWARD_SUBMIT = "forward_submit";
	public static final String FORWARD_CANCEL = "forward_cancel";
	public static final String DEFAULT_TEMPLATE = "statistics/mostActive";

    protected DatePopupField startDate;
	protected DatePopupField endDate;
    protected SelectBox list;
	protected Button submit;
	protected Button cancel;

	protected Map mostActive;

	public void init()
	{
		super.init();
		setColumns(2);

		startDate = new DatePopupField("startDate");
		endDate = new DatePopupField("endDate");
		list = new SelectBox("list");
		list.addOption("-1", "Show All");
		list.addOption("5", "Top 5");
		list.addOption("10", "Top 10");
		list.addOption("25", "Top 25");
		list.addOption("50", "Top 50");
		submit = new Button("submit", "Submit");
		cancel = new Button("cancel", "Cancel");
		Panel buttonPanel = new Panel("buttonPanel");
		buttonPanel.addChild(submit);
		buttonPanel.addChild(cancel);
		mostActive = null;

		addChild(new Label("labelStart", "Starts"));
		addChild(startDate);
		addChild(new Label("labelEnd", "End"));
		addChild(endDate);
		addChild(new Label("labelList", "List"));
		addChild(list);
		addChild(new Label("labelButton", ""));
		addChild(buttonPanel);
	}

	public Forward onSubmit(Event event)
	{
		Forward forward = super.onSubmit(event);
		mostActive = new SequencedHashMap();
		if("cancel".equals(findButtonClicked(event)))
			forward = new Forward(FORWARD_CANCEL);
		else
		{
			StatisticsHandler handler = (StatisticsHandler) Application.getInstance().getModule(StatisticsHandler.class);
			try
			{
				Map scores = handler.getScoredUsage(startDate.getDate(), endDate.getDate());
				TreeMap tree = new TreeMap();
				SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
				for (Iterator i = scores.keySet().iterator(); i.hasNext();)
				{
					String userId = (String) i.next();
					Long score = (Long) scores.get(userId);
					Collection users = new ArrayList();
					if(tree.containsKey(score))
						users = (Collection) tree.get(score);
					try
					{
						users.add(service.getUser(userId));
					}
					catch (SecurityException e)
					{
						Log.getLog(getClass()).error("Error retrieving user " + userId, e);
					}
					tree.put(score, users);
					
				}
				//-- Limiting listing
				int count = 0;
				for (Iterator it = tree.keySet().iterator(); it.hasNext();)
				{
					Long key = (Long) it.next();
					Collection list = (Collection) tree.get(key);
					for (Iterator il = list.iterator(); il.hasNext();)
					{
						User user = (User) il.next();
						mostActive.put(user, key);
					}
					count ++;
					if(isLimit(count))
						break;
				}
			}
			catch (StatisticsException e)
			{
				Log.getLog(getClass()).error("Error retrieving usage", e);
			}

			forward = new Forward(FORWARD_SUBMIT);
		}
		return forward;
	}

	protected boolean isLimit(int current)
	{
		if("-1".equals(list.getSelectedOptions().keySet().iterator().next()))
			return false;
		if(current < (new Integer((String) list.getSelectedOptions().keySet().iterator().next()).intValue()))
			return false;
		return true;
	}

	public String getDefaultTemplate()
	{
		return DEFAULT_TEMPLATE;
	}

	public Map getMostActive()
	{
		return mostActive;
	}

	public void setMostActive(Map mostActive)
	{
		this.mostActive = mostActive;
	}

	public DatePopupField getStartDate()
	{
		return startDate;
	}

	public void setStartDate(DatePopupField startDate)
	{
		this.startDate = startDate;
	}

	public DatePopupField getEndDate()
	{
		return endDate;
	}

	public void setEndDate(DatePopupField endDate)
	{
		this.endDate = endDate;
	}

	public SelectBox getList()
	{
		return list;
	}

	public void setList(SelectBox list)
	{
		this.list = list;
	}

	public Button getSubmit()
	{
		return submit;
	}

	public void setSubmit(Button submit)
	{
		this.submit = submit;
	}

	public Button getCancel()
	{
		return cancel;
	}

	public void setCancel(Button cancel)
	{
		this.cancel = cancel;
	}
}
