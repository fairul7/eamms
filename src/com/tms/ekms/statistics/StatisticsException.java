package com.tms.ekms.statistics;

import kacang.util.DefaultException;

public class StatisticsException extends DefaultException
{
	public StatisticsException()
	{
		super();
	}

	public StatisticsException(String s)
	{
		super(s);
	}

	public StatisticsException(Throwable throwable)
	{
		super(throwable);
	}

	public StatisticsException(String s, Throwable throwable)
	{
		super(s, throwable);
	}
}
