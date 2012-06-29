package com.tms.hr.claim.model;

public class ClaimFormIndexException extends RuntimeException 
{
	public ClaimFormIndexException() { }

	public ClaimFormIndexException(String toEmail) 
	{ super(toEmail); }

}
