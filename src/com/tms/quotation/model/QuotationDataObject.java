package com.tms.quotation.model;

import java.util.Date;

import kacang.model.DefaultDataObject;

public class QuotationDataObject extends DefaultDataObject {

	private String quotationId;

	private String customerId;

	private String templateId;

    private String tableId;
    
	private String subject;

	private String status;

	private Date openDate;

	private Date closeDate;

	private Date recdate;
    
    private String whoModified;

    private String content;
    
    private String companyId;
    
    
    public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	/*     
    private String header;
    
    private String footer;
*/
    public QuotationDataObject() {
    }

/*
	public String getFooter() {
      return footer;
    }

    public void setFooter(String footer) {
      this.footer = footer;
    }

    public String getHeader() {
      return header;
    }

    public void setHeader(String header) {
      this.header = header;
    }
*/
	public Date getCloseDate() {
		return closeDate;
	}

	public void setCloseDate(Date closeDate) {
		this.closeDate = closeDate;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public Date getOpenDate() {
		return openDate;
	}

	public void setOpenDate(Date openDate) {
		this.openDate = openDate;
	}

	public String getQuotationId() {
		return quotationId;
	}

	public void setQuotationId(String quotationId) {
		this.quotationId = quotationId;
	}

	public Date getRecdate() {
		return recdate;
	}

	public void setRecdate(Date recdate) {
		this.recdate = recdate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

  public String getWhoModified() {
    return whoModified;
  }

  public void setWhoModified(String whoModified) {
    this.whoModified = whoModified;
  }

  public String getTableId() {
    return tableId;
  }
  
  public void setTableId(String tableId) {
    this.tableId = tableId;
  }
  
}