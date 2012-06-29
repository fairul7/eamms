package com.tms.crm.helpdesk;

import kacang.util.DefaultException;

public class HelpdeskException extends DefaultException
{
	public HelpdeskException()
	{
		super();
	}

	public HelpdeskException(String s)
	{
		super(s);
	}

	public HelpdeskException(Throwable throwable)
	{
		super(throwable);
	}

	public HelpdeskException(String s, Throwable throwable)
	{
		super(s, throwable);
	}
}
