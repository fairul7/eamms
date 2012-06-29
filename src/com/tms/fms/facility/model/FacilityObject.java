package com.tms.fms.facility.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.tms.fms.engineering.model.EngineeringModule;

import kacang.Application;
import kacang.model.DefaultDataObject;

public class FacilityObject extends DefaultDataObject{

	private String id;
	private String name;
	private String description;
	private String category_id;
	private String category_name;
	private String channel_id;
	private String channel_name;
	private String maketype;
	private String model_name;
	private int quantity;
	private String is_pm;
	private String pm_type;
	private String pm_month;
	private String pm_year;
	private String is_pool;
	private String have_child;
	private String status;
	private String createdby;
	private String createdby_name;
	private Date createdby_date;
	private String updatedby;
	private Date updatedby_date;
	
	private String facility_id;
	private String related_id;
	private String barcode;
	private Date purchased_date;
	private String purchased_cost;
	private String do_num;
	private String easset_num;
	private String availability;
	private String location_id;
	private String location_name;
	private String replacement;
	
	private Date checkout_date;
	private String checkout_by;
	private Date checkin_date;
	private String location;
	private String checkin_by;
	private String purpose;
	private String groupId;
	private int noOfCheckedOut;
	private int noOfCheckedIn;
	
	private String storeLocation;
	
	private int quantityAvailable;
	private int quantityCheckedOut;
	
	private String checkin_by_name;
	
	private String takenBy;
	private String preparedBy;
	
	public String item;
	
	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Date getCheckout_date() {
		return checkout_date;
	}

	public void setCheckout_date(Date checkout_date) {
		this.checkout_date = checkout_date;
	}

	public String getCheckout_by() {
		return checkout_by;
	}

	public void setCheckout_by(String checkout_by) {
		this.checkout_by = checkout_by;
	}

	public Date getCheckin_date() {
		return checkin_date;
	}

	public void setCheckin_date(Date checkin_date) {
		this.checkin_date = checkin_date;
	}

	public String getCheckin_by() {
		return checkin_by;
	}

	public void setCheckin_by(String checkin_by) {
		this.checkin_by = checkin_by;
	}

	public String getPurpose() {
		return purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}

	public String getAvailability() {
		return availability;
	}

	public void setAvailability(String availability) {
		this.availability = availability;
	}

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

	public String getCategory_id() {
		return category_id;
	}

	public void setCategory_id(String category_id) {
		this.category_id = category_id;
	}

	public String getCategory_name() {
		return category_name;
	}

	public void setCategory_name(String category_name) {
		this.category_name = category_name;
	}

	public String getChannel_id() {
		return channel_id;
	}

	public void setChannel_id(String channel_id) {
		this.channel_id = channel_id;
	}

	public String getChannel_name() {
		return channel_name;
	}

	public void setChannel_name(String channel_name) {
		this.channel_name = channel_name;
	}

	public String getMaketype() {
		return maketype;
	}

	public void setMaketype(String maketype) {
		this.maketype = maketype;
	}

	public String getModel_name() {
		return model_name;
	}

	public void setModel_name(String model_name) {
		this.model_name = model_name;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getIs_pm() {
		return is_pm;
	}

	public void setIs_pm(String is_pm) {
		this.is_pm = is_pm;
	}

	public String getPm_type() {
		return pm_type;
	}

	public void setPm_type(String pm_type) {
		this.pm_type = pm_type;
	}

	public String getPm_month() {
		return pm_month;
	}

	public void setPm_month(String pm_month) {
		this.pm_month = pm_month;
	}

	public String getPm_year() {
		return pm_year;
	}

	public void setPm_year(String pm_year) {
		this.pm_year = pm_year;
	}

	public String getIs_pool() {
		return is_pool;
	}

	public void setIs_pool(String is_pool) {
		this.is_pool = is_pool;
	}

	public String getHave_child() {
		return have_child;
	}

	public void setHave_child(String have_child) {
		this.have_child = have_child;
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

	public String getFacility_id() {
		return facility_id;
	}

	public void setFacility_id(String facility_id) {
		this.facility_id = facility_id;
	}

	public String getRelated_id() {
		return related_id;
	}

	public void setRelated_id(String related_id) {
		this.related_id = related_id;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public Date getPurchased_date() {
		return purchased_date;
	}

	public void setPurchased_date(Date purchased_date) {
		this.purchased_date = purchased_date;
	}

	public String getPurchased_cost() {
		return purchased_cost;
	}

	public void setPurchased_cost(String purchased_cost) {
		this.purchased_cost = purchased_cost;
	}

	public String getDo_num() {
		return do_num;
	}

	public void setDo_num(String do_num) {
		this.do_num = do_num;
	}

	public String getEasset_num() {
		return easset_num;
	}

	public void setEasset_num(String easset_num) {
		this.easset_num = easset_num;
	}

	public void setId(String id) {
		this.id = id;
	}

	/*public String getCreatedby_name() {
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		return createdby_name + " [" + formatter.format(getCreatedby_date()) + "]";
	}

	public void setCreatedby_name(String createdby_name) {
		this.createdby_name = createdby_name;
	}*/

	public String getLocation_id() {
		return location_id;
	}

	public void setLocation_id(String location_id) {
		this.location_id = location_id;
	}

	public String getLocation_name() {
		return location_name;
	}

	public void setLocation_name(String location_name) {
		this.location_name = location_name;
	}

	public String getReplacement() {
		return replacement;
	}

	public void setReplacement(String replacement) {
		this.replacement = replacement;
	}

	/**
	 * @return the groupId
	 */
	public String getGroupId() {
		return groupId;
	}

	/**
	 * @param groupId the groupId to set
	 */
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	/**
	 * @return the noOfCheckedIn
	 */
	public int getNoOfCheckedIn() {
		return noOfCheckedIn;
	}

	/**
	 * @param noOfCheckedIn the noOfCheckedIn to set
	 */
	public void setNoOfCheckedIn(int noOfCheckedIn) {
		this.noOfCheckedIn = noOfCheckedIn;
	}

	/**
	 * @return the noOfCheckedOut
	 */
	public int getNoOfCheckedOut() {
		return noOfCheckedOut;
	}

	/**
	 * @param noOfCheckedOut the noOfCheckedOut to set
	 */
	public void setNoOfCheckedOut(int noOfCheckedOut) {
		this.noOfCheckedOut = noOfCheckedOut;
	}

	public int getQuantityAvailable() {
		return quantityAvailable;
	}

	public void setQuantityAvailable(int quantityAvailable) {
		this.quantityAvailable = quantityAvailable;
	}

	public int getQuantityCheckedOut() {
		return quantityCheckedOut;
	}

	public void setQuantityCheckedOut(int quantityCheckedOut) {
		this.quantityCheckedOut = quantityCheckedOut;
	}

	public String getTakenBy() {
		return takenBy;
	}

	public void setTakenBy(String takenBy) {
		this.takenBy = takenBy;
	}
	
	public static String getFullName(String userId) {
		FacilityDao dao = (FacilityDao)Application.getInstance().getModule(FacilityModule.class).getDao();
		return dao.getFullName(userId);
	}

	public String getCheckin_by_name() {
		return getFullName(checkin_by);
	}

	public void setCheckin_by_name(String checkin_by_name) {
		this.checkin_by_name = checkin_by_name;
	}

	public String getPreparedBy() {
		return preparedBy;
	}

	public void setPreparedBy(String preparedBy) {
		this.preparedBy = preparedBy;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public String getStoreLocation() {
		return storeLocation;
	}

	public void setStoreLocation(String storeLocation) {
		this.storeLocation = storeLocation;
	}
	public String getStatusLabel() {
		return (String)EngineeringModule.ASSIGNMENT_FACILITY_STATUS_MAP.get(status);
	}
	
}
