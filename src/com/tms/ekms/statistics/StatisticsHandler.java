package com.tms.ekms.statistics;

import kacang.Application;
import kacang.model.*;
import kacang.model.operator.*;
import kacang.services.log.LogException;
import kacang.services.log.LogService;
import org.apache.commons.collections.SequencedHashMap;

import java.util.*;

public class StatisticsHandler extends DefaultModule
{
	public static final String PROPERTY_PREFIX = "com.tms.ekms.statistics.Operation_";

    private Map scores = new HashMap();

	/**
	 * Initializes the scoring matrix as specified in the configuration xml
	 */
	public void init()
	{
        Properties properties = Application.getInstance().getProperties();
		for (Iterator i = properties.keySet().iterator(); i.hasNext();)
		{
			String key = (String) i.next();
			if(key.startsWith(PROPERTY_PREFIX))
				scores.put(key.substring(PROPERTY_PREFIX.length(), key.length()), new Integer((String) properties.get(key)));
		}
	}

	/**
	 * Method called to retrieve a specific user's usage
	 * @param userId
	 * @return a collection of usage object for the specified user
	 * @throws StatisticsException
	 */
    public Collection getUsageByUser(String userId) throws StatisticsException
	{
		Collection usages = new ArrayList();
		LogService service = (LogService) Application.getInstance().getService(LogService.class);
		DaoQuery query = new DaoQuery();
		query.addProperty(new OperatorEquals("userId", userId, DaoOperator.OPERATOR_AND));
		try
		{
			Collection list = service.getUsage(query);
			for (Iterator i = list.iterator(); i.hasNext();)
			{
				Map map = (Map) i.next();
				Usage usage = new Usage();
				usage.setUserId((String) map.get("userId"));
				usage.setOperation((String) map.get("entryLabel"));
				usage.setCount(((Integer) map.get("count")).intValue());
				usages.add(usage);
			}
			return usages;
		}
		catch (LogException e)
		{
			throw new StatisticsException("Error retreving usage for user " + userId, e);
		}
	}

	/**
	 * Method used to retrive the frequency of operations
	 * @param startDate
	 * @param endDate
	 * @return A map containing the operation(key) and the frequency(value)
	 * @throws StatisticsException
	 */
	public Map getUsageByOperation(Date startDate, Date endDate) throws StatisticsException
	{
		Map usage = new HashMap();
		try
		{
			Collection list = getRangedUsage(startDate, endDate);
			for (Iterator i = list.iterator(); i.hasNext();)
			{
				Map map = (Map) i.next();
                String operation = (String) map.get("entryLabel");
				Number count = (Number) map.get("count");
                if(usage.containsKey(operation))
				{
                    Number assigned = (Number) usage.get(operation);
					usage.put(operation, new Long(count.intValue() + assigned.intValue()));
				}
				else
					usage.put(operation, count);
			}
		}
		catch (LogException e)
		{
			throw new StatisticsException("Error retrieving operational usage from " + startDate.toString() + " to " + endDate.toString(), e);
		}
		return usage;
	}

	/**
	 * This method returns a map of total activity scored by the frequency of an operation in conjuction
	 * with the configured weightage for a particular user
	 * @param startDate
	 * @param endDate
	 * @return A hashmap which contains the score value(value) and the user involved(key)
	 * @throws StatisticsException
	 */
	public Map getScoredUsage(Date startDate, Date endDate) throws StatisticsException
	{
        Map usage = new SequencedHashMap();
        try
		{
			Collection list = getRangedUsage(startDate, endDate);
			for (Iterator i = list.iterator(); i.hasNext();)
			{
				Map map = (Map) i.next();
                String userId = (String) map.get("userId");
				String operation = (String) map.get("entryLabel");
				Number count = (Number) map.get("count");
				//-- Calculating weightage
				int currentScore = 0;
                if(scores.containsKey(operation))
                    currentScore = count.intValue() * ((Integer) scores.get(operation)).intValue();
				else
                    currentScore = count.intValue();
                if(usage.containsKey(userId))
				{
                    Long assigned = (Long) usage.get(userId);
					usage.put(userId, new Long(currentScore + assigned.intValue()));
				}
				else
					usage.put(userId, new Long(currentScore));
			}
		}
		catch (LogException e)
		{
			throw new StatisticsException("Error retrieving scored usage from " + startDate.toString() + " to " + endDate.toString(), e);
		}
		return usage;
	}

	//-- Returns a collection of usage within the given dates
	protected Collection getRangedUsage(Date startDate, Date endDate) throws LogException
	{
        DaoQuery query = new DaoQuery();
		if(startDate != null)
		{
			OperatorParenthesis parenthesisStart = new OperatorParenthesis(DaoOperator.OPERATOR_AND);
			parenthesisStart.addOperator(new OperatorEquals("entryDate", startDate, null));
			parenthesisStart.addOperator(new OperatorGreaterThan("entryDate", startDate, DaoOperator.OPERATOR_OR));
			query.addProperty(parenthesisStart);
		}
		if(endDate != null)
		{
			OperatorParenthesis parenthesisEnd = new OperatorParenthesis(DaoOperator.OPERATOR_AND);
			parenthesisEnd.addOperator(new OperatorEquals("entryDate", endDate, null));
			parenthesisEnd.addOperator(new OperatorLessThan("entryDate", endDate, DaoOperator.OPERATOR_OR));
			query.addProperty(parenthesisEnd);
		}
		//LogService service = (LogService) Application.getInstance().getService(LogService.class);
		//return service.getUsage(query);
		
		//TODO: this is temporary only. Remove when LogService.getUsage has been changed.
		StatisticsDao statisticsDao = (StatisticsDao) getDao();
		try {
			return statisticsDao.selectUsage_Temp(query);
		}
		catch (DaoException e)
		{
			throw new LogException("Error while retriving usage", e);
		}
	}
}
