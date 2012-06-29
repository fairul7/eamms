package com.tms.fms.setup.model;

import java.util.Date;
import kacang.model.DefaultDataObject;
public class ProgramObject extends DefaultDataObject{
	private String programId;
	private String programName;
	private String description;
	private String producer;
	private String pfeCode;
	private Date startProductionDate;
	private Date endProductionDate;
	private String department;
	private String status;
	private String createdBy;
	private Date createdDate;
	private String updatedBy;
	private Date updatedDate;
	
	private String engManpowerBudget;
	private String facilitiesBudget;
	private String vtrBudget;
	private String transportBudget;
	
	//constructor
	public ProgramObject(){}
	
	public String getProgramId(){
		return programId;
	}
	public void setProgramId(String programId){
		this.programId=programId;
	}	
	public String getProgramName(){
		return programName;
	}	
	public void setProgramName(String programName){
		this.programName=programName;
	}	
	public String getDescription(){
		return description;
	}
	public void setDescription(String description){
		this.description=description;
	}	
	public String getProducer() {
		return producer;
	}
	public void setProducer(String producer) {
		this.producer = producer;
	}
	public Date getStartProductionDate(){
		return startProductionDate;
	}
	public void setStartProductionDate(Date startProductionDate){
		this.startProductionDate=startProductionDate;
	}
	public Date getEndProductionDate(){
		return endProductionDate;
	}
	public void setEndProductionDate(Date endProductionDate){
		this.endProductionDate=endProductionDate;
	}
	public String getDepartment(){
		return department;
	}
	public void setDepartment(String department){
		this.department=department;
	}
	public String getStatus(){
		return status;
	}
	public void setStatus(String status){
		this.status=status;
	}
	public String getCreatedBy(){
		return createdBy;
	}
	public void setCreatedBy(String createdBy){
		this.createdBy=createdBy;
	}
	public Date getCreatedDate(){
		return createdDate;
	}
	public void setCreatedDate(Date createdDate){
		this.createdDate=createdDate;
	}
	public String getUpdatedBy(){
		return updatedBy;
	}
	public void setUpdatedBy(String updatedBy){
		this.updatedBy=updatedBy;
	}
	public Date getUpdatedDate(){
		return updatedDate;
	}
	public void setUpdatedDate(Date updatedDate){
		this.updatedDate=updatedDate;
	}
	public String getPfeCode() {
		return pfeCode;
	}
	public void setPfeCode(String pfeCode) {
		this.pfeCode = pfeCode;
	}

	public String getEngManpowerBudget() {
		return engManpowerBudget;
	}

	public void setEngManpowerBudget(String engManpowerBudget) {
		this.engManpowerBudget = engManpowerBudget;
	}

	public String getFacilitiesBudget() {
		return facilitiesBudget;
	}

	public void setFacilitiesBudget(String facilitiesBudget) {
		this.facilitiesBudget = facilitiesBudget;
	}

	public String getVtrBudget() {
		return vtrBudget;
	}

	public void setVtrBudget(String vtrBudget) {
		this.vtrBudget = vtrBudget;
	}

	public String getTransportBudget() {
		return transportBudget;
	}

	public void setTransportBudget(String transportBudget) {
		this.transportBudget = transportBudget;
	}
	
}
