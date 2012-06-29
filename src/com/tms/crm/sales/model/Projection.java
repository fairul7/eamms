package com.tms.crm.sales.model;

import kacang.model.DefaultDataObject;

public class Projection extends DefaultDataObject {
	private String projectionID;
	private String userID;
	private Integer year;
	private Integer[] monthData = new Integer[12];
	
	public String getProjectionID() {
		return projectionID;
	}
	
	public String getUserID() {
		return userID;
	}
	
	public Integer getYear() {
		return year;
	}
	
	public Integer getMonth1() {
		return monthData[0];
	}
	
	public Integer getMonth2() {
		return monthData[1];
	}
	
	public Integer getMonth3() {
		return monthData[2];
	}
	
	public Integer getMonth4() {
		return monthData[3];
	}

	public Integer getMonth5() {
		return monthData[4];
	}
	
	public Integer getMonth6() {
		return monthData[5];
	}
	
	public Integer getMonth7() {
		return monthData[6];
	}
	
	public Integer getMonth8() {
		return monthData[7];
	}
	
	public Integer getMonth9() {
		return monthData[8];
	}
	
	public Integer getMonth10() {
		return monthData[9];
	}
	
	public Integer getMonth11() {
		return monthData[10];
	}
	
	public Integer getMonth12() {
		return monthData[11];
	}
	
	public Integer getMonth(int index) {
		return monthData[index];
	}
	
	public void setProjectionID(String string) {
		projectionID = string;
	}
	
	public void setUserID(String string) {
		userID = string;
	}
	
	public void setYear(Integer integer) {
		year = integer;
	}
	
	public void setMonth1(Integer integer) {
		monthData[0] = integer;
	}
	
	public void setMonth2(Integer integer) {
		monthData[1] = integer;
	}
	
	public void setMonth3(Integer integer) {
		monthData[2] = integer;
	}
	
	public void setMonth4(Integer integer) {
		monthData[3] = integer;
	}
	
	public void setMonth5(Integer integer) {
		monthData[4] = integer;
	}
	
	public void setMonth6(Integer integer) {
		monthData[5] = integer;
	}
		
	public void setMonth7(Integer integer) {
		monthData[6] = integer;
	}
	
	public void setMonth8(Integer integer) {
		monthData[7] = integer;
	}
	
	public void setMonth9(Integer integer) {
		monthData[8] = integer;
	}
	
	public void setMonth10(Integer integer) {
		monthData[9] = integer;
	}
	
	public void setMonth11(Integer integer) {
		monthData[10] = integer;
	}
	
	public void setMonth12(Integer integer) {
		monthData[11] = integer;
	}
	
	public void setMonth(int index, Integer integer) {
		monthData[index] = integer;
	}
}