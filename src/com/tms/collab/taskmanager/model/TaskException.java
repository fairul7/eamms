package com.tms.collab.taskmanager.model;

import kacang.util.DefaultException;

public class TaskException extends DefaultException
{
	public TaskException()
	{
		super();
	}

	public TaskException(String s)
	{
		super(s);
	}

	public TaskException(Throwable throwable)
	{
		super(throwable);
	}

	public TaskException(String s, Throwable throwable)
	{
		super(s, throwable);
	}
}
