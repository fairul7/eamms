package com.tms.ekms.statistics.ui;

import kacang.ui.LightWeightWidget;
import kacang.ui.Event;
import kacang.Application;
import kacang.util.Log;
import kacang.services.security.*;

import java.util.*;

import com.tms.ekms.statistics.StatisticsHandler;
import com.tms.ekms.statistics.StatisticsException;
import org.apache.commons.collections.SequencedHashMap;

public class ScoringSummary extends LightWeightWidget
{
	public static final String DEFAULT_TEMPLATE = "statistics/summary";
    public static final String DEFAULT_LABEL = "statistics_summary";
    public static final String FIELD_MONTH = "scoringSummaryMonth";
	public static final String FIELD_YEAR = "scoringSummaryYear";

	protected Map users;
	protected Map features;

	public void onRequest(Event event)
	{
		super.onRequest(event);
		users = new SequencedHashMap();
		features = new SequencedHashMap();
		StatisticsHandler handler = (StatisticsHandler) Application.getInstance().getModule(StatisticsHandler.class);
		SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);

        //-- Formulating start and end dates
		Calendar start = Calendar.getInstance();
		Calendar end = Calendar.getInstance();
		start.set(Calendar.DAY_OF_MONTH, 1);
		start.set(Calendar.HOUR, 0);
		start.set(Calendar.MINUTE, 0);
		start.set(Calendar.SECOND, 0);
		end.set(Calendar.DAY_OF_MONTH, end.getActualMaximum(Calendar.DAY_OF_MONTH));
		end.set(Calendar.HOUR, 23);
		end.set(Calendar.MINUTE, 59);
		end.set(Calendar.SECOND, 59);

        //-- Filtering By Date
		String selectedMonth = event.getRequest().getParameter("scoringSummaryMonth");
		if(!(selectedMonth == null || "".equals(selectedMonth)))
		{
			start.set(Calendar.MONTH, Integer.parseInt(selectedMonth));
			end.set(Calendar.MONTH, Integer.parseInt(selectedMonth));
		}
		String selectedYear = event.getRequest().getParameter("scoringSummaryYear");
		if(!(selectedYear == null || "".equals(selectedYear)))
		{
            start.set(Calendar.YEAR, Integer.parseInt(selectedYear));
			end.set(Calendar.YEAR, Integer.parseInt(selectedYear));
		}

		//-- Retrieving most active users
		try
		{
			Map scores = handler.getScoredUsage(start.getTime(), end.getTime());
			TreeMap tree = new TreeMap();
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
				catch (kacang.services.security.SecurityException e)
				{
                    // ignore
				}
				tree.put(score, users);
			}
            List mostActive = new ArrayList();
			for (Iterator it = tree.keySet().iterator(); it.hasNext();)
			{
				Long key = (Long) it.next();
				Collection list = (Collection) tree.get(key);
				for (Iterator il = list.iterator(); il.hasNext();)
				{
					User user = (User) il.next();
					mostActive.add(user);
				}
			}
			//-- Getting top 10
			if(mostActive.size() > 0)
			{
				int position = mostActive.size() - 1;
				for(int i = 1; i<=10; i++)
				{
					if(position < 0)
						break;
                    User user = (User) mostActive.get(position);
                    users.put(user, scores.get(user.getId()));
					position--;
				}
			}
			//-- Getting most popular features
            Map map = handler.getUsageByOperation(start.getTime(), end.getTime());
			Map categorized = new HashMap();
			Map treeMap = new TreeMap();
			for (Iterator i = map.keySet().iterator(); i.hasNext();)
			{
				String key = (String) i.next();
				Number score = (Number) map.get(key);
				String operation = "";
                if("kacang.services.log.messaging.ActivateAccount".equals(key) || "kacang.services.log.messaging.SendMessage".equals(key))
					operation = "Messaging";
				else if("kacang.services.log.security.Register".equals(key) || "kacang.services.log.security.UpdateProfile".equals(key))
					operation = "User Profile";
				else if("kacang.services.log.calendar.CreateEvent".equals(key) || "kacang.services.log.calendar.UpdatedEvent".equals(key))
					operation = "Calendaring";
				else if("kacang.services.log.task.CreateTask".equals(key) || "kacang.services.log.task.UpdateTask".equals(key) || "kacang.services.log.task.UpdateProgress".equals(key))
					operation = "Calendaring";
				else if("kacang.services.log.directory.CreateCompany".equals(key) || "kacang.services.log.directory.UpdateCompany".equals(key))
					operation = "Business Directory";
                else if("kacang.services.log.directory.CreateContact".equals(key) || "kacang.services.log.directory.UpdateContact".equals(key))
					operation = "Address Book";
				else if("kacang.services.log.security.Login".equals(key) || "kacang.services.log.security.Logout".equals(key))
					operation = "System Authentication";
                if(categorized.containsKey(operation))
					score = new Long(((Number) categorized.get(operation)).intValue() + score.intValue());
				categorized.put(operation, score);
			}
            for (Iterator i = categorized.keySet().iterator(); i.hasNext();)
			{
				String operation = (String) i.next();
				Number score = new Long((((Number)categorized.get(operation)).longValue()));
				Collection operations = new ArrayList();
				if(treeMap.containsKey(score))
					operations = (Collection) treeMap.get(score);
                if (operation != null && !operation.trim().equals(""))
				    operations.add(operation);
				treeMap.put(score, operations);
			}
			List mostPopular = new ArrayList();
			for (Iterator it = treeMap.keySet().iterator(); it.hasNext();)
			{
				Long key = (Long) it.next();
				Collection list = (Collection) treeMap.get(key);
                if (list != null) {
                    for (Iterator il = list.iterator(); il.hasNext();)
                    {
                        String operation = (String) il.next();
                        mostPopular.add(operation);
                    }                    
                }
			}
			//-- Getting top 10
			features = new SequencedHashMap();
			if(mostPopular.size() > 0)
			{
				int position = mostPopular.size() - 1;
				for(int i = 1; i<=10; i++)
				{
					if(position < 0)
						break;
                    String operation = (String) mostPopular.get(position);
                    features.put(operation, categorized.get(operation));
					position--;
				}
			}

		}
		catch (StatisticsException e)
		{
			Log.getLog(getClass()).error("Error retrieving usage", e);
		}
		event.getRequest().setAttribute(DEFAULT_LABEL, this);
	}

	public Map getUsers()
	{
		return users;
	}

	public void setUsers(Map users)
	{
		this.users = users;
	}

	public Map getFeatures()
	{
		return features;
	}

	public void setFeatures(Map features)
	{
		this.features = features;
	}

	public String getDefaultTemplate()
	{
		return DEFAULT_TEMPLATE;
	}
}
