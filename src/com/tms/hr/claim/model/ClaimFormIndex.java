/**
 * @author Vincent Lee, Wavelet Solutions Sdn Bhd (632468-W)
 * Created On: 2004-06-17
 * Copyright of The Media Shoppe Berhad
 */

package com.tms.hr.claim.model;

import kacang.model.DefaultDataObject;
import kacang.services.security.User;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;


public class ClaimFormIndex extends DefaultDataObject
{
	private int APPROVER1 = 0x0001;
	private int APPROVER2 = 0x0002;
	private int APPROVER3 = 0x0004;
	private int APPROVER4 = 0x0008;
	private String id;
	private Date timeEdit;
	private String userOriginator;
	private String userOwner;
	private String userApprover1;
	private String userApprover2;
	private String userApprover3;
	private String userApprover4;
	private String userAssessor;
	private String currency;
	private BigDecimal amount;
	private Integer approvalLevelRequired;
	private Integer approvalLevelGranted;
	private String remarks;
	private String rejectReason;
	private String info;
	private String state;
	private String status;

    public Vector vecItems;


   private User userOriginatorObj;
   private User userOwnerObj;
   private User userApprover1Obj;
   private User userApprover2Obj;
   private User userApprover3Obj;
   private User userApprover4Obj;
   private User userAssessorObj;

    private Date claimMonth;
    private String approvedBy;
    private String rejectedBy;

    private String amountInStr;

    private Date userApprover1Date;
    private Date userApprover2Date;


    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    public String getRejectedBy() {
        return this.rejectedBy;
    }

    public void setRejectedBy(String rejectedBy) {
        this.rejectedBy = rejectedBy;
    }
    
    public Date getClaimMonth() {
        return claimMonth;
    }

    public void setClaimMonth(Date claimMonth) {
        this.claimMonth = claimMonth;
    }

	public void setUserOriginatorObj(User userOriginatorObj)
	{ this.userOriginatorObj = userOriginatorObj;}

	public void setUserOwnerObj(User userOwnerObj)
	{ this.userOwnerObj = userOwnerObj;}

	public void setUserApprover1Obj(User userApprover1Obj)
	{ this.userApprover1Obj = userApprover1Obj;}

    public User getUserApprover1Obj() {
        return userApprover1Obj;
    }

    public void setUserApprover2Obj(User userApprover2Obj)
	{ this.userApprover2Obj = userApprover2Obj;}

    public User getUserApprover2Obj() {
        return userApprover2Obj;
    }

    public void setUserApprover3Obj(User userApprover3Obj)
	{ this.userApprover3Obj = userApprover3Obj;}

	public void setUserApprover4Obj(User userApprover4Obj)
	{ this.userApprover4Obj = userApprover4Obj;}

	public void setUserAssessorObj(User userAssessorObj)
	{ this.userAssessorObj = userAssessorObj;}


	public String getOriginatorName()
	{ return (this.userOriginatorObj!=null)?
			this.userOriginatorObj.getName():"";}

	public String getOwnerName()
	{ return (this.userOwnerObj!=null)?
			this.userOwnerObj.getName():"";}

	public String getApprover1Name()
	{ return (this.userApprover1Obj!=null)?
			this.userApprover1Obj.getName():"";}

	public String getApprover2Name()
	{ return (this.userApprover2Obj!=null)?
			this.userApprover2Obj.getName():"";}

	public String getApprover3Name()
	{ return (this.userApprover3Obj!=null)?
			this.userApprover3Obj.getName():"";}

	public String getApprover4Name()
	{ return (this.userApprover4Obj!=null)?
			this.userApprover4Obj.getName():"";}

	public String getAssessorName()
	{ return (this.userAssessorObj!=null)?
			this.userAssessorObj.getName():"";}


	public ClaimFormIndex()
	{
		id="x";
		timeEdit= new Date();
		userOriginator = "x";
		userOwner = "x";
		userApprover1 = "x";
		userApprover2 = "x";
		userApprover3 = "x";
		userApprover4 = "x";
		userAssessor = "x";

		currency = "x";
		amount = new BigDecimal("0.00");

		approvalLevelRequired = new Integer("2"); // default level
		approvalLevelGranted = new Integer("0");

		remarks = " ";
		rejectReason = "";
		info = "";
	
		state="x";
		status = "act";

		vecItems = new Vector();

	}

	public String getFormId()
	{ return id;}

	public String getId()
	{ return id;}
	public void setId(String id)
	{ this.id = id ;}


	public String getTimeEditStr()
	{ return DateFormat.format(this.timeEdit,"yyyy-MM-dd");}
	public Date getTimeEdit()
	{ return timeEdit;}
	public void setTimeEdit(Date timeEdit)
	{ this.timeEdit = timeEdit;}

	public String getUserOriginator()
	{ return userOriginator;}
	public void setUserOriginator(String userOriginator)
	{ this.userOriginator = userOriginator;}

	public String getUserOwner()
	{ return userOwner;}
	public void setUserOwner(String userOwner)
	{ this.userOwner = userOwner ;}

	public String getUserApprover1()
	{ return userApprover1;}
	public void setUserApprover1(String userApprover1)
	{ this.userApprover1 = userApprover1 ;}

	public String getUserApprover2()
	{ return userApprover2;}
	public void setUserApprover2(String userApprover2)
	{ this.userApprover2 = userApprover2 ;}

	public String getUserApprover3()
	{ return userApprover3;}
	public void setUserApprover3(String userApprover3)
	{ this.userApprover3 = userApprover3 ;}

	public String getUserApprover4()
	{ return userApprover4;}
	public void setUserApprover4(String userApprover4)
	{ this.userApprover4 = userApprover4 ;}

	public String getUserAssessor()
	{ return userAssessor;}
	public void setUserAssessor(String userAssessor)
	{ this.userAssessor = userAssessor ;}

	public String getCurrency()
	{ return currency;}
	public void setCurrency(String currency)
	{ this.currency= currency;}

	public String getAmountStr()
	{
        //return "<div align='right'>"+amount.setScale(2).toString()+"</div>";
        try{
        return amount.setScale(2).toString();
        }
        catch(Exception e){
            return "0";
        }
    }

	public BigDecimal getAmount()
	{ return amount.setScale(2);}
	public void setAmount(BigDecimal amount)
	{
        try{
        //this.amount = amount.setScale(2);
        this.amount = amount;
        }
        catch(Exception e){
            this.amount = new BigDecimal(0);
        }
    }

	public Integer getApprovalLevelRequired()
	{ return approvalLevelRequired;}
	public void setApprovalLevelRequired(Integer approvalLevelRequired)
	{ this.approvalLevelRequired = approvalLevelRequired;}

	public Integer getApprovalLevelGranted()
	{ return approvalLevelGranted;}
	public void setApprovalLevelGranted(Integer approvalLevelGranted)
	{ this.approvalLevelGranted = approvalLevelGranted;}

	public String getRemarks()
	{ return remarks;}
	public void setRemarks(String remarks)
	{ this.remarks = remarks;}

	public String getRejectReason()
	{ return rejectReason;}
	public void setRejectReason(String rejectReason)
	{ this.rejectReason = rejectReason;}

	public String getInfo()
	{ return info;}
	public void setInfo(String info)
	{ this.info = info;}

	public String getState()
	{ return state;}
	public void setState(String state)
	{ this.state = state;}
	
	public String getStatus()
	{ return status;}
	public void setStatus(String status)
	{ this.status = status;}

	public Integer getNumberOfApprover()
	{
		int count = 0;
		if(this.userApprover1.length()>2) {count = count | APPROVER1;}
		if(this.userApprover2.length()>2) {count = count | APPROVER2;}
		if(this.userApprover3.length()>2) {count = count | APPROVER3;}
		if(this.userApprover4.length()>2) {count = count | APPROVER4;}
		return new Integer(count);
	}

	public void setApprovalLevelGranted(String userid)
	{
		int granted = getApprovalLevelGranted().intValue();
		if(this.userApprover1.equals(userid)) { granted = granted | APPROVER1;}
		if(this.userApprover2.equals(userid)) { granted = granted | APPROVER2;}
		if(this.userApprover3.equals(userid)) { granted = granted | APPROVER3;}
		if(this.userApprover4.equals(userid)) { granted = granted | APPROVER4;}
		setApprovalLevelGranted(new Integer(granted));
	}

	public boolean getFullyApproved()
	{
		int required = getApprovalLevelRequired().intValue();
      int granted = getApprovalLevelGranted().intValue();
		int result = required & granted;
		if(result < required)
		{ return false;}
		else {return true;}
	}

	public String toString()
	{
		return (
			"<Claim ClaimFormIndex" +
			" id=\""+getId()+"\""+
			" timeEdit=\""+getTimeEdit().toString()+"\""+
			" userOriginator=\""+getUserOriginator()+"\""+
			" userOwner=\""+getUserOwner()+"\""+
			" userApprover1=\""+getUserApprover1()+"\""+
			" userApprover2=\""+getUserApprover2()+"\""+
			" userApprover3=\""+getUserApprover3()+"\""+
			" userApprover4=\""+getUserApprover4()+"\""+
			" userAssessor=\""+getUserAssessor()+"\""+
			" currency=\""+getCurrency()+"\""+
			" amount=\""+getAmount().toString()+"\""+
			" approvalLevelRequired=\""+getApprovalLevelRequired().toString()+"\""+
			" approvalLevelGranted=\""+getApprovalLevelGranted().toString()+"\""+
			" remarks =\""+getRemarks()+"\""+
			" rejectReason=\""+getRejectReason()+"\""+
			" state =\""+getState()+"\""+
			" status=\""+getStatus()+"\""+
			" />"
					);
	}


    public String getAmountInStr() {
        return amountInStr;
    }

    public void setAmountInStr(String amountInStr) {
        this.amountInStr = amountInStr;
    }


    public Date getUserApprover1Date() {
        return userApprover1Date;
    }

    public void setUserApprover1Date(Date userApprover1Date) {
        this.userApprover1Date = userApprover1Date;
    }

    public Date getUserApprover2Date() {
        return userApprover2Date;
    }

    public void setUserApprover2Date(Date userApprover2Date) {
        this.userApprover2Date = userApprover2Date;
    }


}


