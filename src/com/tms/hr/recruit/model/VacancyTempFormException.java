package com.tms.hr.recruit.model;

public class VacancyTempFormException extends RuntimeException{
	public VacancyTempFormException(){
	}
	
	public VacancyTempFormException(String vacancyTempCode){
		super(vacancyTempCode);
	}
}
