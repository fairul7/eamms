package com.tms.fms.department.model;

import java.util.Date;

import kacang.model.DefaultDataObject;


/**
 * 
 * User: Fairul
 * Date: Jun 24, 2003
 * Time: 2:35:16 PM
 * To change this template use Options | File Templates.
 */
public class FMSDepartment extends DefaultDataObject
{
	
	
	//id,company,contactName,contactOfficeTel,contactHandphone,contactEmail 
	
    private String id;    
    private String name;
    private String description;
    private String HOD;
    private String[] deptApprover;
    private String status;
   
    public FMSDepartment()
    {

    }    

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
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

	public String getHOD() {
		return HOD;
	}

	public void setHOD(String hod) {
		HOD = hod;
	}

	public String[] getDeptApprover() {
		return deptApprover;
	}

	public void setDeptApprover(String[] deptApprover) {
		this.deptApprover = deptApprover;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	
	
}
