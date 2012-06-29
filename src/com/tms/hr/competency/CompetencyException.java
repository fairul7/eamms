package com.tms.hr.competency;

import kacang.util.DefaultException;

public class CompetencyException extends DefaultException
{
    public CompetencyException()
    {
    }

    public CompetencyException(String s)
    {
        super(s);
    }

    public CompetencyException(Throwable throwable)
    {
        super(throwable);
    }

    public CompetencyException(String s, Throwable throwable)
    {
        super(s, throwable);
    }
}
