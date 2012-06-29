package com.tms.hr.claim.model;

public class ClaimConfigException extends RuntimeException 
{
	public ClaimConfigException() { }

	public ClaimConfigException(String toEmail) 
	{ super(toEmail); }

}
