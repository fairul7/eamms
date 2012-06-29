package com.tms.hr.claim.model;

public class ClaimProjectException extends RuntimeException 
{
	public ClaimProjectException() { }

	public ClaimProjectException(String toEmail) 
	{ super(toEmail); }

}
