package com.tms.fms.facility.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import kacang.model.DefaultDataObject;

public class CategoryObject extends DefaultDataObject{
	private String id;
	private String name;
	private String description;
	private String department_id;
	private String department_name;
	private String unit_id;
	private String unit_name;
	private String parent_cat;
	private String parent_cat_id;
	private String parent_cat_name;
	private String status;
	private String createdby;
	private Date createdby_date;
	private String updatedby;
	private Date updatedby_date;
	
	public String getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDepartment_id() {
		return department_id;
	}

	public void setDepartment_id(String department_id) {
		this.department_id = department_id;
	}

	public String getDepartment_name() {
		return department_name;
	}

	public void setDepartment_name(String department_name) {
		this.department_name = department_name;
	}

	public String getUnit_id() {
		return unit_id;
	}

	public void setUnit_id(String unit_id) {
		this.unit_id = unit_id;
	}

	public String getUnit_name() {
		return unit_name;
	}

	public void setUnit_name(String unit_name) {
		this.unit_name = unit_name;
	}

	public String getParent_cat() {
		return parent_cat;
	}

	public void setParent_cat(String parent_cat) {
		this.parent_cat = parent_cat;
	}

	public String getParent_cat_id() {
		return parent_cat_id;
	}

	public void setParent_cat_id(String parent_cat_id) {
		this.parent_cat_id = parent_cat_id;
	}

	public String getParent_cat_name() {
		return parent_cat_name;
	}

	public void setParent_cat_name(String parent_cat_name) {
		this.parent_cat_name = parent_cat_name;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCreatedby() {
		return createdby;
	}

	public void setCreatedby(String createdby) {
		this.createdby = createdby;
	}

	public Date getCreatedby_date() {
		return createdby_date;
	}

	public void setCreatedby_date(Date createdby_date) {
		this.createdby_date = createdby_date;
	}

	public String getUpdatedby() {
		return updatedby;
	}

	public void setUpdatedby(String updatedby) {
		this.updatedby = updatedby;
	}

	public Date getUpdatedby_date() {
		return updatedby_date;
	}

	public void setUpdatedby_date(Date updatedby_date) {
		this.updatedby_date = updatedby_date;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	
}
