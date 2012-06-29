/**
 * 
 */
package com.tms.fms.facility.model;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import com.tms.fms.engineering.model.Service;

import kacang.model.DefaultDataObject;
import kacang.util.Log;

/**
 * @author fahmi
 *
 */
public class RateCard extends DefaultDataObject {
	private String id;
	private String idDetail;
	private String idEquipment;
	private String idManpower;
	private String idCategory;
	private String name;
	private String categoryName;
	private Collection categoryItems;
	private String[] equipments;
	private String itemsCSV;
	private String serviceTypeId;
	private String serviceType;	
	private String servicesCSV;
	private String requestTypeId;
	private Date effectiveDate;
	private String internalRate;
	private String externalRate;
	private String description;
	private String remarksRequest;
	private String status;
	private String createdBy;
	private String modifiedBy;
	private Date createdOn;
	private Date modifiedOn;
	private String equipment;
	private int equipmentQty;
	private String manpower;
	private String competencyType;
	private int manpowerQty;
	private String categoryId;
	private String competencyId;
	
	private Collection services;
	
	private String vehicleCategoryId;
	private String transportRequest;
	
	private String abwCode;
	private String abwCodeDesc;
	
	
	public Collection getServices() {
		return services;
	}
	public void setServices(Collection services) {
		this.services = services;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getIdDetail() {
		return idDetail;
	}
	public void setIdDetail(String idDetail) {
		this.idDetail = idDetail;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getServiceTypeId() {
		return serviceTypeId;
	}
	public void setServiceTypeId(String serviceTypeId) {
		this.serviceTypeId = serviceTypeId;
	}
	public String getServiceType() {
		return serviceType;
	}
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
	public String getRequestTypeId() {
		return requestTypeId;
	}
	public void setRequestTypeId(String requestTypeId) {
		this.requestTypeId = requestTypeId;
	}
	public Date getEffectiveDate() {
		return effectiveDate;
	}
	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}
	public String getInternalRate() {
		return internalRate;
	}
	public void setInternalRate(String internalRate) {
		this.internalRate = internalRate;
	}
	public String getExternalRate() {
		return externalRate;
	}
	public void setExternalRate(String externalRate) {
		this.externalRate = externalRate;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getRemarksRequest() {
		return remarksRequest;
	}
	public void setRemarksRequest(String remarksRequest) {
		this.remarksRequest = remarksRequest;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	public Date getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}
	public Date getModifiedOn() {
		return modifiedOn;
	}
	public void setModifiedOn(Date modifiedOn) {
		this.modifiedOn = modifiedOn;
	}
	public String getServicesCSV() {
		String servicesCSV="";
		try{
			for(Iterator itr2=services.iterator();itr2.hasNext();){
				Service service=(Service)itr2.next();
				String serviceName=service.getDisplayTitle();
				if(serviceName!=null && serviceName.length()>0){
					if("".equals(servicesCSV)){
						servicesCSV+=serviceName;
					}else{
						servicesCSV+=", "+serviceName;
					}
				}
			}
		}catch (Exception e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return servicesCSV;
	}
	public void setServicesCSV(String servicesCSV) {
		this.servicesCSV = servicesCSV;
	}
	
	public String getEquipment() {
		return equipment;
	}
	public void setEquipment(String equipment) {
		this.equipment = equipment;
	}
	public int getEquipmentQty() {
		return equipmentQty;
	}
	public void setEquipmentQty(int equipmentQty) {
		this.equipmentQty = equipmentQty;
	}
	public String getManpower() {
		return manpower;
	}
	public void setManpower(String manpower) {
		this.manpower = manpower;
	}
	public int getManpowerQty() {
		return manpowerQty;
	}
	public void setManpowerQty(int manpowerQty) {
		this.manpowerQty = manpowerQty;
	}
	public String getIdEquipment() {
		return idEquipment;
	}
	public void setIdEquipment(String idEquipment) {
		this.idEquipment = idEquipment;
	}
	public String getIdManpower() {
		return idManpower;
	}
	public void setIdManpower(String idManpower) {
		this.idManpower = idManpower;
	}
	public String getIdCategory() {
		return idCategory;
	}
	public void setIdCategory(String idCategory) {
		this.idCategory = idCategory;
	}
	
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public Collection getCategoryItems() {
		return categoryItems;
	}
	public void setCategoryItems(Collection categoryItems) {
		this.categoryItems = categoryItems;
	}
	public String getItemsCSV() {
		String itemsCSV="";
		try{
			for(Iterator itr=categoryItems.iterator();itr.hasNext();){
				RateCard rd = (RateCard)itr.next();
				String items = rd.getEquipment();
				
				if(items!=null && items.length()>0){
					if("".equals(itemsCSV)){
						itemsCSV+=items;
					}else{
						itemsCSV+=", "+items;
					}
				}
			}
			
		}catch (Exception e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return itemsCSV;
	}
	
	public void setItemsCSV(String itemsCSV) {
		this.itemsCSV = itemsCSV;
	}
	public String[] getEquipments() {
		return equipments;
	}
	public void setEquipments(String[] equipments) {
		this.equipments = equipments;
	}
	public String getCompetencyType() {
		return competencyType;
	}
	public void setCompetencyType(String competencyType) {
		this.competencyType = competencyType;
	}
	public String getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	public String getCompetencyId() {
		return competencyId;
	}
	public void setCompetencyId(String competencyId) {
		this.competencyId = competencyId;
	}
	public String getTransportRequest() {
		return transportRequest;
	}
	public void setTransportRequest(String transportRequest) {
		this.transportRequest = transportRequest;
	}
	public String getVehicleCategoryId() {
		return vehicleCategoryId;
	}
	public void setVehicleCategoryId(String vehicleCategoryId) {
		this.vehicleCategoryId = vehicleCategoryId;
	}
	public String getAbwCode() {
		return abwCode;
	}
	public void setAbwCode(String abwCode) {
		this.abwCode = abwCode;
	}
	public String getAbwCodeDesc() {
		return abwCodeDesc;
	}
	public void setAbwCodeDesc(String abwCodeDesc) {
		this.abwCodeDesc = abwCodeDesc;
	}
	
}
