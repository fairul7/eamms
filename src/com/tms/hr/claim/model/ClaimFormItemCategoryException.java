package com.tms.hr.claim.model;

public class ClaimFormItemCategoryException extends RuntimeException 
{
	public ClaimFormItemCategoryException() { }

	public ClaimFormItemCategoryException(String toEmail) 
	{ super(toEmail); }

}
