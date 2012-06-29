/**
 * @author Vincent Lee, Wavelet Solutions Sdn Bhd (632468-W)
 * Created On: 2004-06-17
 * Copyright of The Media Shoppe Berhad
 */

package com.tms.hr.claim.model;

import kacang.model.DefaultDataObject;

import java.math.BigDecimal;
import java.util.Date;
import java.text.DecimalFormat;

public class ClaimFormItem extends DefaultDataObject
{
    // 18 members
    private String id;
    private String formId;
    private String categoryId;
    private String standardTypeId;
    private String projectId;
    private Date timeFrom;
    private Date timeTo;
    private Date timeFinancial;
    private String currency;
    private BigDecimal amount;
    private BigDecimal qty;
    private BigDecimal unitPrice;
    private String uom;
    private String description;
    private String remarks;
    private String rejectReason;
    private String state;
    private String status;

    //additional for travel attributes
    private String travelFrom;
    private String travelTo;

    /// Additional members
    private ClaimFormItemCategory categoryObj;
    private ClaimProject projectObj;
    private ClaimStandardType stdTypeObj;


    private String amountInStr;


    public ClaimFormItem()
    {
        id="x";
        formId="x";
        categoryId="x";
        standardTypeId="x";
        projectId="x";
        timeFrom=new Date();
        timeTo=new Date();
        timeFinancial=new Date();
        currency="x";
        amount = new BigDecimal("0.00");
        qty = new BigDecimal("0.00");
        unitPrice = new BigDecimal("0.00");
        uom = "x";
        description = "x";
        remarks = "x";
        rejectReason = "x";
        state = "act";
        status = "act";
    }

    public void setClaimFormItemCategory(ClaimFormItemCategory categoryObj)
    { this.categoryObj = categoryObj;}
    public ClaimFormItemCategory getClaimFormItemCategory()
    { return this.categoryObj;}

    public void setClaimProject(ClaimProject projectObj)
    { this.projectObj = projectObj;}
    public ClaimProject getClaimProject()
    { return this.projectObj;}

    public void setClaimStandardType(ClaimStandardType stdTypeObj)
    { this.stdTypeObj = stdTypeObj;}
    public ClaimStandardType getClaimStandardType()
    { return this.stdTypeObj;}



    public String getCategoryName()
    {
        if(this.categoryObj.getName().equalsIgnoreCase("travel-allowance"))
        return "Allowance";
        else
        return this.categoryObj.getName();

    }

    public String getProjectName()
    { return this.projectObj.getName();}

    public String getStandardTypeName()
    { return this.stdTypeObj.getName();}

    public String getId()
    { return id;}
    public void setId(String id)
    { this.id = id ;}

    public String getFormId()
    { return formId;}
    public void setFormId( String formId)
    { this.formId = formId;}

    public String getCategoryId()
    { return categoryId;}
    public void setCategoryId(String categoryId)
    { this.categoryId = categoryId;}

    public String getStandardTypeId()
    { return standardTypeId;}
    public void setStandardTypeId(String standardId)
    { this.standardTypeId = standardId;}

    public String getProjectId()
    { return projectId;}
    public void setProjectId(String projectId)
    { this.projectId = projectId;}

    public String getTimeFromStr()
    { return DateFormat.format(this.timeFrom, "yyyy-MM-dd");}
    public Date getTimeFrom()
    { return this.timeFrom;}
    public void setTimeFrom(Date timeFrom)
    { this.timeFrom = timeFrom;}

    public String getTimeToStr()
    { return DateFormat.format(this.timeTo, "yyyy-MM-dd");}
    public Date getTimeTo()
    { return this.timeTo;}
    public void setTimeTo(Date timeTo)
    { this.timeTo = timeTo;}

    public String getTimeFinancialStr()
    { return DateFormat.format(this.timeFinancial, "yyyy-MM-dd");}
    public Date getTimeFinancial()
    { return this.timeFinancial;}
    public void setTimeFinancial(Date timeFinancial)
    { this.timeFinancial = timeFinancial;}

    public String getCurrency()
    { return this.currency;}
    public void setCurrency(String currency)
    { this.currency = currency;}

    public String getAmountStr()
    { return "<div align='right'>"+ this.amount.setScale(2).toString()+"</div>"; }
    public BigDecimal getAmount()
    { return this.amount;}
    public void setAmount(BigDecimal amount)
    { this.amount = amount;
      setAmountInStr(new DecimalFormat("0.00").format(amount));
    }

    public BigDecimal getQty()
    { return this.qty;}
    public void setQty(BigDecimal qty)
    { this.qty = qty;}

    public BigDecimal getUnitPrice()
    { return this.unitPrice;}
    public void setUnitPrice(BigDecimal unitPrice)
    { this.unitPrice = unitPrice;}

    public String getUom()
    { return this.uom;}
    public void setUom(String uom)
    { this.uom = uom;}

    public String getRemarks()
    { return remarks;}
    public void setRemarks(String remarks)
    { this.remarks = remarks ;}

    public String getDescription()
    { return description;}
    public void setDescription(String description)
    { this.description = description;}

    public String getRejectReason()
    { return this.rejectReason;}
    public void setRejectReason(String rejectReason)
    { this.rejectReason = rejectReason;}

    public String getState()
    { return this.state;}
    public void setState(String state)
    { this.state = state;}

    public String getStatus()
    { return status;}
    public void setStatus(String status)
    { this.status = status;}

    public String toString()
    {
        return (
            "<Claim ClaimFormItem" +
            " id=\""+getId()+"\""+
            " status=\""+getStatus()+"\""+
            " />"
                    );
    }

    public String getTravelFrom() {
        return travelFrom;
    }

    public void setTravelFrom(String travelFrom) {
        this.travelFrom = travelFrom;
    }

    public String getTravelTo() {
        return travelTo;
    }

    public void setTravelTo(String travelTo) {
        this.travelTo = travelTo;
    }


    public String getAmountInStr() {
        if(amountInStr.charAt(0)=='(')
         return amountInStr;
        else
        return new DecimalFormat("0.00").format(Double.parseDouble(amountInStr));
    }

    public void setAmountInStr(String amountInStr) {
        this.amountInStr = amountInStr;
    }

}


