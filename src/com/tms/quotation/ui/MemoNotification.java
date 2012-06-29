package com.tms.quotation.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import kacang.Application;
import kacang.services.security.SecurityException;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.util.Log;
import kacang.util.UuidGenerator;

import com.tms.collab.messaging.model.IntranetAccount;
import com.tms.collab.messaging.model.Message;
import com.tms.collab.messaging.model.MessagingException;
import com.tms.collab.messaging.model.MessagingModule;
import com.tms.collab.messaging.model.SmtpAccount;
import com.tms.collab.messaging.model.Util;
import com.tms.crm.helpdesk.Incident;
import com.tms.quotation.model.CustomerDataObject;
import com.tms.quotation.model.QuotationDataObject;
import com.tms.quotation.model.QuotationModule;

public class MemoNotification {
  User user;

  
  public MemoNotification() {
    Application app = Application.getInstance();
    user = app.getCurrentUser();
  }
  
  public void submissionMsg(String [] selected) {
    MessagingModule mm = Util.getMessagingModule();
    Message m = new Message();
    Application app = Application.getInstance();
    
    SecurityService ss = (SecurityService) app.getService(SecurityService.class);
    
    Collection approvers = null; 
    try {
      approvers = ss.getUsersByPermission("com.tms.quotation.approve", Boolean.TRUE, null, false, 0, -1);
    } catch (SecurityException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
    List approverAddr = new ArrayList();
    for( Iterator i=approvers.iterator(); i.hasNext();) {
      User user = (User)i.next();
      try {
        IntranetAccount account = mm.getIntranetAccountByUserId(user.getId());
        approverAddr.add( account.getIntranetUsername() +'@'+MessagingModule.INTRANET_EMAIL_DOMAIN );
      } catch (MessagingException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
    
    QuotationModule qm = (QuotationModule)app.getModule(QuotationModule.class);
    Collection colQuotations = new ArrayList();
    for(int i=0; i<selected.length; i++) {
      colQuotations.add(qm.getQuotation(selected[i]));
    }
     
    StringBuffer msgBody = new StringBuffer();
    msgBody.append("<p>"+user.getName() + " has submitted the following quotation for approval:</p>");
    msgBody.append("<table width=\"100%\" cellpadding=\"4\" cellspacing=\"1\" border=\"0\">" +
        "<tr style=\"font-weight:bold; background-color:#0000FF; color:#FFFFFF\">" +
        "<td width=\"10px\">No.</td><td>Customer Name</td><td>Subject</td></tr>");
    int count=1;
    
    Incident incident = new Incident();
    for( Iterator it = colQuotations.iterator(); it.hasNext(); count++) {
      QuotationDataObject q = (QuotationDataObject) it.next();
      //CustomerDataObject qc = qm.getCustomer(q.getCustomerId());
      
      incident = new Incident();
	  incident.populateContactId(q.getCustomerId());
	  incident.populateCompanyId(q.getCompanyId());
      
      msgBody.append(
          "<tr><td>"+count+
          "</td><td>"+incident.getContactFirstName() + " " + incident.getContactLastName() +
          "</td><td><a href=\"/ekms/quotation/previewQuotation.jsp?quotationId="+
           q.getQuotationId()+"\">" +
           q.getSubject() +
          "</a></td></tr>");      
    }
    msgBody.append("</table>");
    msgBody.append("<p><a href=\"/ekms/quotation/viewQuotation.jsp\">" +
            "Click on this link to view the Quotations List</a></p>");
    
    m.setMessageId(UuidGenerator.getInstance().getUuid());    
    m.setToIntranetList(approverAddr);
    m.setSubject("Quotation submitted for approval");
    m.setDate(new java.util.Date());
    m.setMessageFormat(Message.MESSAGE_FORMAT_HTML);
    m.setBody(msgBody.toString());
    
    send(m);
   
  }
  
  public void approvalMsg(String quotationId) {
    QuotationModule qm = (QuotationModule)Application.getInstance().getModule(QuotationModule.class);
    MessagingModule mm = Util.getMessagingModule();
    Message m = new Message();
    List author=new ArrayList();
    QuotationDataObject q = qm.getQuotation(quotationId);
    CustomerDataObject qc = qm.getCustomer(q.getCustomerId());
    
    String authorId = q.getWhoModified();
    try {
      IntranetAccount acct = mm.getIntranetAccountByUserId(authorId);
      author.add(acct.getIntranetUsername()+'@'+MessagingModule.INTRANET_EMAIL_DOMAIN);
    } catch (MessagingException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    StringBuffer msgBody = new StringBuffer();
    msgBody.append("<p>"+user.getName() + " has approved the following Quotation:</p>");
    msgBody.append("<table width=\"100%\" cellpadding=\"4\" cellspacing=\"1\" border=\"0\">" +
        "<tr style=\"font-weight:bold; background-color:#0000FF; color:#FFFFFF\">" +
        "<td width=\"10px\">No.</td><td>Customer Name</td><td>Subject</td></tr>");
    msgBody.append(
        "<tr><td>1"+
        "</td><td>"+qc.getCompanyName()+
        "</td><td><a href=\"/ekms/quotation/previewQuotation.jsp?quotationId="+
          q.getQuotationId()+"\">" +
          q.getSubject() +
        "</a></td></tr>");      
    
    msgBody.append("</table>");
    msgBody.append("<p><a href=\"/ekms/quotation/viewQuotation.jsp\">" +
    "Click on this link to view the Quotations List</a></p>");
    
    m.setToIntranetList(author);
    m.setSubject("Quotation approved");
    m.setDate(new java.util.Date());
    m.setMessageId(UuidGenerator.getInstance().getUuid());
    m.setBody(msgBody.toString());
    send(m);
  }
  
  public void approveMany( String [] quotationList ) {
    for( int i=0; i<quotationList.length; i++) {
      approvalMsg(quotationList[i]);
    }
  }
  
  void send(Message message) {

    MessagingModule mm;
    mm = Util.getMessagingModule();
    SmtpAccount smtpAccount = null;
    String userId = user.getId();
    
 //send Memo
    try {
      smtpAccount = mm.getSmtpAccountByUserId(userId);
    } catch (MessagingException e) {
    e.printStackTrace();}       
    
    try {
      mm.sendMessage(smtpAccount, message, userId, false);
    } catch (MessagingException e) {
      Log.getLog(getClass()).error("Error Sending Memo ", e);
    }
  }    
//    Message message;    
//    Application app = Application.getInstance();
    
//    User user = app.getCurrentUser();
//    String userId = user.getId();

/*
    // construct the message to send
    message = new Message();
    message.setMessageId(UuidGenerator.getInstance().getUuid());

    IntranetAccount intranetAccount = null;


    List listRecepient = new ArrayList(colRecipientList.size());
    for (Iterator i = colRecipientList.iterator(); i.hasNext();) {              
      Map tempAdd = (Map) i.next();
      try {intranetAccount = Util.getMessagingModule().getIntranetAccountByUserId(tempAdd.get("userId").toString());} catch (Exception e){e.printStackTrace();}              
      if(intranetAccount != null) {
        String add = intranetAccount.getIntranetUsername() + "@" + MessagingModule.INTRANET_EMAIL_DOMAIN;
        listRecepient.add(add);
      }       
    }      

    if (listRecepient.size() > 0)
      message.setToIntranetList(listRecepient);

    message.setSubject(emailSubject + " - " + requestObject.getRequestId());                
    message.setBody(emailContents.toString());

    message.setMessageFormat(Message.MESSAGE_FORMAT_HTML);
    message.setDate(new java.util.Date());
    
*/

}