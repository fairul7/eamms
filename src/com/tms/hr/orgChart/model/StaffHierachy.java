package com.tms.hr.orgChart.model;

import kacang.model.DefaultDataObject;
import kacang.services.security.*;
import kacang.Application;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: blake
 * Date: Mar 17, 2006
 * Time: 11:26:29 AM
 */
public class StaffHierachy extends DefaultDataObject {
    private String userId;
    private String titleCode;
    private String deptCode;
    private String countryCode;
    private String stationCode;
    private Set comIds;
    private Set subordinateIds;
    private boolean active;
    
    private boolean hod;
    private String gender;
    private String staffNumber;
    private String contactHouseTelNumber;
    private String contactOfficeDirectLineNumber;
    private String contactOfficeGeneralLineNumber;
    private String contactHpNumber;
    private String passportNumber;
    private Date passportExpiryDate;
    private String icNumber;
    private Date dateOfBirth;
    private String remarks;
    private String email;
    private boolean deleted = false;

    public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTitleCode() {
        return titleCode;
    }

    public void setTitleCode(String titleCode) {
        this.titleCode = titleCode;
    }

    public String getDeptCode() {
        return deptCode;
    }

    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getStationCode() {
        return stationCode;
    }

    public void setStationCode(String stationCode) {
        this.stationCode = stationCode;
    }
    
	public String getContactHpNumber() {
		return contactHpNumber;
	}

	public void setContactHpNumber(String contactHpNumber) {
		this.contactHpNumber = contactHpNumber;
	}

	public String getContactOfficeDirectLineNumber() {
		return contactOfficeDirectLineNumber;
	}

	public void setContactOfficeDirectLineNumber(
			String contactOfficeDirectLineNumber) {
		this.contactOfficeDirectLineNumber = contactOfficeDirectLineNumber;
	}

	public String getContactOfficeGeneralLineNumber() {
		return contactOfficeGeneralLineNumber;
	}

	public void setContactOfficeGeneralLineNumber(
			String contactOfficeGeneralLineNumber) {
		this.contactOfficeGeneralLineNumber = contactOfficeGeneralLineNumber;
	}

	public String getContactHouseTelNumber() {
		return contactHouseTelNumber;
	}

	public void setContactHouseTelNumber(String contactHouseTelNumber) {
		this.contactHouseTelNumber = contactHouseTelNumber;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public boolean isHod() {
		return hod;
	}

	public void setHod(boolean hod) {
		this.hod = hod;
	}

	public String getIcNumber() {
		return icNumber;
	}

	public void setIcNumber(String icNumber) {
		this.icNumber = icNumber;
	}

	public Date getPassportExpiryDate() {
		return passportExpiryDate;
	}

	public void setPassportExpiryDate(Date passportExpiryDate) {
		this.passportExpiryDate = passportExpiryDate;
	}

	public String getPassportNumber() {
		return passportNumber;
	}

	public void setPassportNumber(String passportNumber) {
		this.passportNumber = passportNumber;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getStaffNumber() {
		return staffNumber;
	}

	public void setStaffNumber(String staffNumber) {
		this.staffNumber = staffNumber;
	}

	public Set getSubordinateIds() {
		return subordinateIds;
	}

	public void setSubordinateIds(Set subordinateIds) {
		this.subordinateIds = subordinateIds;
	}

	public Set getComIds() {
        return comIds;
    }

    public void setComIds(Set comIds) {
        this.comIds = comIds;
    }

    public Set getSubordinatesId() {
        return subordinateIds;
    }

    public void setSubordinatesId(Set subordinatesId) {
        this.subordinateIds = subordinatesId;
    }


    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public void addCommunicatesId(String userId){
        try{
            if(comIds == null)
                comIds = new HashSet();
            comIds.add(userId);
        }catch(Exception e){
            //ignore duplicates entry
        }
    }

    public void addSubordinatesId(String userId){
        try{
            if(subordinateIds == null)
                subordinateIds = new HashSet();
            subordinateIds.add(userId);
        }catch(Exception e){
            //ignore duplicates entry
        }
    }

    public Collection getSubordinateUsers(){
        Collection users = new ArrayList();
        if(subordinateIds != null){
            SecurityService ss = (SecurityService) Application.getInstance().getService(SecurityService.class);
            for(Iterator itr = subordinateIds.iterator(); itr.hasNext();){
                String userId = (String) itr.next();
                try {
                    users.add(ss.getUser(userId));
                    
                } catch (kacang.services.security.SecurityException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
            return users;
        }
        return null;
    }

    public Collection getCommunicateUsers(){
        Collection users = new ArrayList();
        if(comIds != null){
            SecurityService ss = (SecurityService) Application.getInstance().getService(SecurityService.class);
            for(Iterator itr = comIds.iterator(); itr.hasNext();){
                String userId = (String) itr.next();
                try {
                    users.add(ss.getUser(userId));
                    
                } catch (kacang.services.security.SecurityException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
            return users;
        }
        return null;
    }
}
