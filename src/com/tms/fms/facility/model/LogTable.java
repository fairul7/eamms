package com.tms.fms.facility.model;

import java.util.Date;

import kacang.model.DefaultDataObject;


public class LogTable extends DefaultDataObject
{
	
	
	//id,company,contactName,contactOfficeTel,contactHandphone,contactEmail 
	
    private String fileName;    
    private String errors;
    private String errorsLine;
    
   
    public LogTable()
    {

    }


	public String getFileName() {
		return fileName;
	}


	public void setFileName(String fileName) {
		this.fileName = fileName;
	}


	public String getErrors() {
		return errors;
	}


	public void setErrors(String errors) {
		this.errors = errors;
	}


	public String getErrorsLine() {
		return errorsLine;
	}


	public void setErrorsLine(String errorsLine) {
		this.errorsLine = errorsLine;
	}    

   
	
}
