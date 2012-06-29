package com.tms.hr.claim.model;

public class ClaimFormItemException extends RuntimeException 
{
	public ClaimFormItemException() { }

	public ClaimFormItemException(String toEmail) 
	{ 
		super(toEmail); 
	}

}
