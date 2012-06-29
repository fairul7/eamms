package com.tms.ekms.statistics;

import kacang.model.DefaultDataObject;

public class Usage extends DefaultDataObject
{
	private String userId;
	private String operation;
	private int count;

	public String getUserId()
	{
		return userId;
	}

	public void setUserId(String userId)
	{
		this.userId = userId;
	}

	public String getOperation()
	{
		return operation;
	}

	public void setOperation(String operation)
	{
		this.operation = operation;
	}

	public int getCount()
	{
		return count;
	}

	public void setCount(int count)
	{
		this.count = count;
	}
}
