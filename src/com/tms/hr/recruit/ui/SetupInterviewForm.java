package com.tms.hr.recruit.ui;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.mail.internet.AddressException;
import javax.servlet.http.HttpSession;

import kacang.Application;
import kacang.model.DataObjectNotFoundException;
import kacang.services.security.User;
import kacang.services.storage.StorageDirectory;
import kacang.services.storage.StorageException;
import kacang.services.storage.StorageFile;
import kacang.services.storage.StorageService;
import kacang.stdui.Button;
import kacang.stdui.Form;
import kacang.stdui.Label;
import kacang.stdui.Panel;
import kacang.stdui.RichTextBox;
import kacang.stdui.TextBox;
import kacang.stdui.TextField;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;
import kacang.util.UuidGenerator;

import com.tms.collab.messaging.model.IntranetAccount;
import com.tms.collab.messaging.model.Message;
import com.tms.collab.messaging.model.MessagingException;
import com.tms.collab.messaging.model.MessagingModule;
import com.tms.collab.messaging.model.SmtpAccount;
import com.tms.collab.messaging.model.StorageFileDataSource;
import com.tms.collab.messaging.model.Util;
import com.tms.collab.messaging.ui.AttachmentInfo;
import com.tms.collab.myfolder.model.FileFolder;
import com.tms.collab.myfolder.model.MyFolderModule;
import com.tms.ekms.setup.model.SetupModule;
import com.tms.hr.recruit.model.ApplicantObj;
import com.tms.hr.recruit.model.InterviewObj;
import com.tms.hr.recruit.model.RecruitAppModule;
import com.tms.hr.recruit.model.RecruitModule;
import com.tms.hr.recruit.model.VacancyObj;

public class SetupInterviewForm extends Form{
	public static final String FORWARD_SENDANDSAVE = "sendAndSave";
	public static final String FORWARD_ERROR = "error";
	public static final String ATTACHMENT_MAP_SESSION_ATTRIBUTE = "attachmentMap";
	
	private TextField txtSubject;
	private TextBox tbMessageBody;
	private AttachmentInfo attachmentInfo;
	private Collection sfCol = new ArrayList(); //store temp files
	
	private Panel btnPanel;
	private Button btnSend;
	private Button btnCancel;
	
	//mail var
	private boolean htmlEmail = true;
	private Collection interviewObjCol;
	
	public void init(){
		initForm();
	}
	
	public void initForm(){
		setColumns(2);
    	setMethod("POST");
    	//removeChildren();
    	Application app = Application.getInstance();
    	
    	Label lblSubject= new Label("lblSubject", "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.subject") + "*</span>");
    	lblSubject.setAlign("right");
    	addChild(lblSubject);
    	txtSubject = new TextField("txtSubject");
    	txtSubject.addChild(new ValidatorNotEmpty("txtSubjectVNE",app.getMessage("recruit.general.warn.empty")));
    	txtSubject.setMaxlength("60");
    	txtSubject.setSize("30");
    	addChild(txtSubject);
    	
    	Label lblMessageBody= new Label("lblMessageBody", "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.tbMessageBody") + "</span>");
    	lblMessageBody.setAlign("right");
    	addChild(lblMessageBody);
    	tbMessageBody = new RichTextBox("tbMessageBody");
    	tbMessageBody.setRows("40");
    	tbMessageBody.setCols("20");
    	addChild(tbMessageBody);
    	
    	Label lblAttachment= new Label("lblAttachment", "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.attachment") + "</span>");
    	lblAttachment.setAlign("right");
    	addChild(lblAttachment);
    	attachmentInfo = new AttachmentInfo("attachmentInfo");
        addChild(attachmentInfo);
        
        btnSend= new Button("btnSend", app.getMessage("recruit.general.label.send","Send"));
    	btnCancel = new Button(Form.CANCEL_FORM_ACTION, app.getMessage("recruit.general.label.cancel","Cancel"));
    	
    	btnPanel = new Panel("btnPanel");
    	btnPanel.setColspan(2);
    	btnPanel.addChild(btnSend);
    	btnPanel.addChild(btnCancel);
    	Label lblspace = new Label("lblspace","");
    	addChild(lblspace);
    	addChild(btnPanel);
	}
	
	//interviewee detail for the interview session included email (email purpose)
	public Collection getapplicantEmailCol(){
		Application app = Application.getInstance();
		RecruitAppModule ram = (RecruitAppModule) app.getModule(RecruitAppModule.class);
		Collection intervieweeDetailCol=new ArrayList();
		
		for(Iterator ite=getInterviewObjCol().iterator(); ite.hasNext();){
			InterviewObj iObj = (InterviewObj)ite.next();
			Collection col=ram.loadApplicantPersonal(iObj.getApplicantId()[0]);//load based on the current applicant Id
			HashMap map = (HashMap)col.iterator().next();
			
			iObj.setVacancyCode(map.get("vacancyCode").toString());
			iObj.setName(map.get("name").toString());
			iObj.setEmail(map.get("email").toString());
			iObj.setPositionDesc(map.get("positionDesc").toString());
			
			intervieweeDetailCol.add(iObj);
		}
		
		return intervieweeDetailCol;
	}
	
	//send Message-mail #1
	public void sendMessage(Event event) throws MessagingException, AddressException, FileNotFoundException, StorageException {
		MessagingModule mm;
        User user;
        SmtpAccount smtpAccount;
        Message message;
        HttpSession session = event.getRequest().getSession();
        
        mm = Util.getMessagingModule();
        user = Util.getUser(event);
        smtpAccount = mm.getSmtpAccountByUserId(user.getId());
        String id="";
        
        // construct the message to send-send 1 by 1-for applicant
        int noOfUser=0;
        for(Iterator ite=getapplicantEmailCol().iterator(); ite.hasNext();){
        	InterviewObj interviewObj = (InterviewObj)ite.next();
	        message = new Message();
	        id = UuidGenerator.getInstance().getUuid();
	        message.setMessageId(id);
	        
	        setMessageProperties(message, event, interviewObj, noOfUser, true);
	        
	        mm.sendMessage(smtpAccount, message, user.getId(), false);
	        
	        //copy attachment coz the original file already deleted
	        /*try{
	        	  copyDuplicateAttachment(getSfCol(), user.getId());
			}catch(IOException e){
				//nothing
			}catch(StorageException e){
				//nothing
			}*/
	     
	        Log.getLog(getClass()).write(new Date(), id, user.getId(), "kacang.services.log.messaging.SendMessage", "Message sent from user " + user.getName(), event.getRequest().getRemoteAddr(), event.getRequest().getSession().getId());     
	        noOfUser++;
        }
        
        	// construct the message to send-for HOD
        	message = new Message();
        	id = UuidGenerator.getInstance().getUuid();
        	message.setMessageId(id);
	        
        	setMessageProperties(message, event, new InterviewObj(), noOfUser, false);//not applicant is false
	        
	        mm.sendMessage(smtpAccount, message, user.getId(), false);
	        Log.getLog(getClass()).write(new Date(), id, user.getId(), "kacang.services.log.messaging.SendMessage", "Message sent from user " + user.getName(), event.getRequest().getRemoteAddr(), event.getRequest().getSession().getId());     
	        
	        //delete the temp1 folder
	        deleteUserTempFolder1(user.getId(),session);
	}
	
	//	send Message-mail #2
	public void setMessageProperties(Message message, Event event, InterviewObj interviewObj, int noOfUser, boolean hasApplicant) throws AddressException, MessagingException, FileNotFoundException, StorageException {
			IntranetAccount intranetAccount;
			User user = Util.getUser(event);
	        intranetAccount = Util.getMessagingModule().getIntranetAccountByUserId(user.getId());
	        
	        String tbTo="";	
			if(hasApplicant){
				tbTo=interviewObj.getEmail();
				message.setToList(Util.convertStringToInternetRecipientsList(tbTo));
			}else{
				tbTo=intranetAccount.getIntranetUsername() + "@" + MessagingModule.INTRANET_EMAIL_DOMAIN;
				message.setToIntranetList(Util.convertStringToIntranetRecipientsList(tbTo));
			}
			
		    String sendFrom = intranetAccount.getIntranetUsername() + "@" + MessagingModule.INTRANET_EMAIL_DOMAIN;
		    message.setFrom(sendFrom);
		
	        //message.setCcList(Util.convertStringToInternetRecipientsList(tbCc.getValue().toString()));
	        //message.setBccList(Util.convertStringToInternetRecipientsList(tbBcc.getValue().toString()));
	       
	        //message.setCcIntranetList(Util.convertStringToIntranetRecipientsList(tbCc.getValue().toString()));
	        //message.setBccIntranetList(Util.convertStringToIntranetRecipientsList(BccItself));
	        message.setSubject((String)txtSubject.getValue());

	        String signature;
	        signature = ""; //intranetAccount.getSignature() == null ? "" : intranetAccount.getSignature().trim();

	        // set message body with html or plain text detection
	        detectAndSetMessageBody(event, message, signature, interviewObj, hasApplicant);

	        message.setDate(new Date());

	        Map attachmentMap = attachmentInfo.getAttachmentMap();
	        StorageFile sf;
	        StorageFile sfn;
	        StorageService ss = (StorageService) Application.getInstance().getService(StorageService.class);
	         
	        for (Iterator iterator = attachmentMap.keySet().iterator(); iterator.hasNext();) {
	            String name = (String) iterator.next();
	            
	            //getting attachment for the 1st time-only will be used for 1st time
	            if(noOfUser==0){
	            	sf = new StorageFile(MessagingModule.ROOT_PATH + "/" + user.getId() + "/temp/" + name);
	            	sf = ss.get(sf);
	            }else{
	            	sf = new StorageFile(MessagingModule.ROOT_PATH + "/" + user.getId() + "/temp1/" + name);
		            sf = ss.get(sf);
	            }
	            
	            if (sf != null) {
	                // if has attachment
	                message.addStorageFile(sf);
	            }
	            
	            //duplicate only for 1st time-coz 1st attachment will destroy itself.thus we will use the duplicated one
	            if(noOfUser==0){
	            	try{	
	            		copyDuplicateAttachment(sf,ss, user.getId());
	            	}catch(IOException e){
		   				//nothing
		   			}catch(StorageException e){
		   				//nothing
		   			}
	            	//storageFileCol.add(sfn);
	            }
	            
	       
	        }
	}
	
	//delete duplicated temp1 folder
	public void deleteUserTempFolder1(String userId, HttpSession session) throws StorageException {
        // clear user attachments temp folder
        if (userId != null) {
            StorageDirectory sd;
            StorageService ss = (StorageService) Application.getInstance().getService(StorageService.class);

            sd = new StorageDirectory(MessagingModule.ROOT_PATH + "/" + userId + "/temp1/");
            ss.delete(sd);
        }

        // clear session attribute
        if (session != null) {
            session.removeAttribute(ATTACHMENT_MAP_SESSION_ATTRIBUTE);
        }
    }
	
	
	//	duplicate extra file
	public void copyDuplicateAttachment(StorageFile sfn, StorageService ss, String userId) throws IOException, StorageException{ //upload the file to file storage	
		StorageDirectory dest;
		StorageFile source;
		
		Application application = Application.getInstance();
		
		source = new StorageFile(sfn.getAbsolutePath());
		dest = new StorageDirectory(MessagingModule.ROOT_PATH + "/" + userId + "/temp1/");
		
		ss.copy(source, dest);
	}
	
	//	send Message-mail #3
	 public void detectAndSetMessageBody(Event event, Message message, String signature, InterviewObj interviewObj, boolean hasApplicant) {
		 	Application app = Application.getInstance();
		 	//format the date time
		 	DateFormat dmyDateFmt = new SimpleDateFormat(app.getProperty("globalDatetimeLong"));
		 	//audit  part 1-for applicant & HOD
			VacancyObj auditObj = new VacancyObj();
			String actionTaken="";
		
			//message body
			String title="";
			StringBuffer sbTitle = new StringBuffer();
			
		 	if(hasApplicant){
				title = "<br /><pre><font size=\"2\" face=\"verdana,arial,helvetica,sans-serif\" color=\"#000000\">Dear " + interviewObj.getName() + ", </font></pre>";
				sbTitle.append("<br /><pre><font size=\"2\" face=\"verdana,arial,helvetica,sans-serif\" color=\"#000000\">Interview Date & Time : "+  
						dmyDateFmt.format(interviewObj.getInterviewDateTime()) +"</font></pre><br />");
			 		
				actionTaken="Send eMail to applicant and Interview Date Time, " + dmyDateFmt.format(interviewObj.getInterviewDateTime());
				auditObj.setAndInsertAudit(interviewObj.getVacancyCode(), interviewObj.getApplicantId()[0], actionTaken);
		 	}else{
		 		try{
		 		//title="The following are the Interview Date & Time and Interviewee(s) details";
		 		String titleHeader = "The following are the Interview Date & Time and Interviewee(s) details";	
				sbTitle.append("<table cellspacing=0 cellpadding=5 width=\"100%\" border=0>\n");
				sbTitle.append("  <tr valign=center> \n");
				sbTitle.append("    <td bgcolor=\"#FFFFFF\" colspan=\"4\"><b><font size=\"2\" face=\"verdana,arial,helvetica,sans-serif\" color=\"#000000\">" + titleHeader + "</font></b></td>\n");
				sbTitle.append("  </tr>\n");
			        
				sbTitle.append("  <tr> \n");
				sbTitle.append("    <td valign=\"top\" bgcolor=\"#FFFFFF\"><font size=\"2\" face=\"verdana,arial,helvetica,sans-serif\">Interview Date & Times</font></td>\n");
				sbTitle.append("    <td valign=\"top\" bgcolor=\"#FFFFFF\"><font size=\"2\" face=\"verdana,arial,helvetica,sans-serif\">Interviewee(s)</font></td>\n");
				sbTitle.append("    <td valign=\"top\" bgcolor=\"#FFFFFF\"><font size=\"2\" face=\"verdana,arial,helvetica,sans-serif\">Vacancy Code</font></td>\n");
				sbTitle.append("    <td valign=\"top\" bgcolor=\"#FFFFFF\"><font size=\"2\" face=\"verdana,arial,helvetica,sans-serif\">Position</font></td>\n");
				sbTitle.append("  </tr>\n");
					
				for(Iterator ite=getInterviewObjCol().iterator();ite.hasNext();){
			 		InterviewObj iObj = (InterviewObj)ite.next();
			 		//URL for mail
			 		
					SetupModule setup = (SetupModule) Application.getInstance().getModule(SetupModule.class);
					String siteUrl = setup.get("siteUrl");
					 
					sbTitle.append("  <tr> \n");
					sbTitle.append("    <td valign=\"top\" bgcolor=\"#FFFFFF\"><font size=\"2\" face=\"verdana,arial,helvetica,sans-serif\">" + dmyDateFmt.format(iObj.getInterviewDateTime()) + "</font></td>\n");
					sbTitle.append("    <td valign=\"top\" bgcolor=\"#FFFFFF\"><font size=\"2\" face=\"verdana,arial,helvetica,sans-serif\">" + iObj.getName() + "</font></td>\n");
					sbTitle.append("    <td valign=\"top\" bgcolor=\"#FFFFFF\"><font size=\"2\" face=\"verdana,arial,helvetica,sans-serif\">" + 
							"<a href='"+siteUrl+"/ekms/recruit/applicantListing.jsp?vacancyCodeApply="+iObj.getVacancyCode()+"'>"+iObj.getVacancyCode()+"</a></font></td>\n");
					sbTitle.append("    <td valign=\"top\" bgcolor=\"#FFFFFF\"><font size=\"2\" face=\"verdana,arial,helvetica,sans-serif\">" + iObj.getPositionDesc()+ "</font></td>\n");
					sbTitle.append("  </tr>\n");
					
					actionTaken="Send eMail to HOD about applicant and Interview Date Time, "+dmyDateFmt.format(iObj.getInterviewDateTime());
					auditObj.setAndInsertAudit(iObj.getVacancyCode(), iObj.getApplicantId()[0], actionTaken);
				}
				
				sbTitle.append("</table>\n");
		 		}catch (Exception e) {
					Log.getLog(getClass()).error("Error getting site URL", e);
				}
		 	}
			
			String body;
	        body = tbMessageBody.getValue().toString();
	        
	        if (!Util.isRichTextCapable(event.getRequest())) {
	            // editor don't support RTE, send as text.

	            // text format
	            message.setMessageFormat(Message.MESSAGE_FORMAT_TEXT);
	            if (signature.length() > 0) {
	                signature = "\n" + signature;
	            }

	        } else {
	            // try to determine if contents of email is HTML
	            boolean hasHtmlSign = false;
	            for (int i = 0; i < HTML_MESSAGE_SIGNATURES.length; i++) {

	                if (body.indexOf(HTML_MESSAGE_SIGNATURES[i]) != -1) {
	                    hasHtmlSign = true;
	                    break;
	                }
	            }

	            if (hasHtmlSign) {
	                // HTML format
	                message.setMessageFormat(Message.MESSAGE_FORMAT_HTML);
	                if (signature.length() > 0) {
	                    signature = "<br><pre>" + signature + "</pre>";
	                }
	            } else {
	                // text format
	                message.setMessageFormat(Message.MESSAGE_FORMAT_TEXT);
	                if (signature.length() > 0) {
	                    signature = "\n" + signature;
	                }
	            }

	        }
	        
	        message.setBody(title + sbTitle.toString() + body + signature);
	 }
	
	 //	send Message-mail #4
	 public static final String[] HTML_MESSAGE_SIGNATURES = {
	        "</p>",
	        "<br />",
	        "<BLOCKQUOTE style=\"PADDING-LEFT: 5px; MARGIN-LEFT: 5px; BORDER-LEFT: #1010ff 2px solid\">"
	};
	 
	public void onRequest(Event evt){
		init();
		populateForm(evt);
        HttpSession session;
        session = evt.getRequest().getSession();
    	session.removeAttribute("attachmentMap");
    	interviewObjCol = (Collection)evt.getRequest().getSession().getAttribute("sessionInterviewObjCol");
	}
	
	public void populateForm(Event evt){
		Application app = Application.getInstance();
		RecruitModule rm = (RecruitModule) app.getModule(RecruitModule.class);
		
		//for mail
		if(Util.isRichTextCapable(evt.getRequest())) {
	        htmlEmail = true;
	    } else {
	    	htmlEmail = false;
	    }
		
		try{
			VacancyObj smbObj = rm.loadMessagebody();
			if(smbObj.getMessageBody()!=null && !smbObj.getMessageBody().equals("")){
				// show signature
				String signature = "";
				try {
		            IntranetAccount intranetAccount;
		            User user;

		            user = Util.getUser(evt);
		            intranetAccount = Util.getMessagingModule().getIntranetAccountByUserId(user.getId());
		            signature = intranetAccount.getSignature() == null ? "" : intranetAccount.getSignature().trim();

		            if (signature.length() > 0) {
		                if (htmlEmail) {
		                	tbMessageBody.setValue(smbObj.getMessageBody() + "<br><pre><font size=\"2\" face=\"verdana,arial,helvetica,sans-serif\">" + signature + "</font></pre><br>");
		                }
		                else {
		                	tbMessageBody.setValue(smbObj.getMessageBody()  + "\n\r\n\r\n\r\n\r" + signature);
		                }
		            }
		            else {
		            	tbMessageBody.setValue(smbObj.getMessageBody());
		            }
				}
				catch (MessagingException e) {
		            Log.getLog(getClass()).error("Error retrieving signature", e);
				}
			}	
		}catch(DataObjectNotFoundException e){
			Log.getLog(getClass()).error("Module Message Body not found" + e);
			init();
		}
	}
	
	public Forward onSubmit(Event evt) {
		Forward forward = super.onSubmit(evt); 
		Application app = Application.getInstance();
		boolean flagInterviewDateTime=false;
		String action = findButtonClicked(evt);
		
		if(action.equals(btnSend.getAbsoluteName())){
			//getting info from SetupInterviewDateList-session data
			
			if(!this.isInvalid()){
				if(getInterviewObjCol()!=null && getInterviewObjCol().size() > 0){
					for(Iterator ite=getInterviewObjCol().iterator();ite.hasNext();){
						InterviewObj iObj = (InterviewObj)ite.next();
						if(iObj.getInterviewDateTime()!=null && !iObj.getInterviewDateTime().equals("")){
							//nothing
						}else{
							flagInterviewDateTime=true;
						}
					}
				}else{
					flagInterviewDateTime=true;
				}
				
				if(flagInterviewDateTime){
					 setInvalid(true);	
					 return new Forward("interviewDateTimeNotEntered");
				}
			}else{
				setInvalid(true);
			}
		}
		
		return forward;
	}
	
	//	store applicant Id into String of Array
	public String[] getMultipleApplicantId(Collection interviewObjCol){
		String[] selectedApplicantId=new String[getInterviewObjCol().size()];
		int i=0;
		for(Iterator ite = interviewObjCol.iterator(); ite.hasNext();){
			 InterviewObj interviewObj = (InterviewObj)ite.next();
			 selectedApplicantId[i]=interviewObj.getApplicantId()[0];
			 i++;
		 }
		
		return selectedApplicantId;
	}
	
	public Forward onValidate(Event evt) {
		Log log;
        log = Log.getLog(getClass());
		String action = findButtonClicked(evt);
		try {
		if(action.equals(btnSend.getAbsoluteName())){
			 //auditObj
			 VacancyObj auditObj = new VacancyObj();
			 String actionTaken="";
			
			 //process send-send mail with attachement if any #1
			 sendMessage(evt);
			 
			 //insert interview date & time (rec_interview_date) #2
			 Application app = Application.getInstance();
			 RecruitAppModule ram = (RecruitAppModule) app.getModule(RecruitAppModule.class);
			 
			 for(Iterator ite = getInterviewObjCol().iterator(); ite.hasNext();){
				 InterviewObj interviewObj = (InterviewObj)ite.next();
				 ram.insertInterviewDate(interviewObj);
			 }
			 
			 //update the applicantStatus AND insert audit trail #3 
			 ApplicantList appList=new ApplicantList();
			 ApplicantObj applicantObj = new ApplicantObj();
			 
			 Collection lookUpVacancyCol = ram.loadShortlisted(getMultipleApplicantId(getInterviewObjCol()), "Short-Listed,Another Interview");//lookup vacancy applied by applicant
			 for(Iterator ite=lookUpVacancyCol.iterator(); ite.hasNext();){
				ApplicantObj appObj= (ApplicantObj)ite.next();
				
				if(appObj.getApplicantStatus().equals("Short-Listed")){
					 applicantObj = appList.setApplicantStatus(appObj.getVacancyCode(), appObj.getApplicantId(), "Short-Listed & Email Sent");
					 actionTaken="set applicant status to Short-Listed & Email Sent";
					 auditObj.setAndInsertAudit(appObj.getVacancyCode(),  appObj.getApplicantId(), actionTaken);
				}else{
					 applicantObj = appList.setApplicantStatus(appObj.getVacancyCode(), appObj.getApplicantId(), "Another Interview & Email Sent");
					 actionTaken="set applicant status to Another Interview & Email Sent";
					 auditObj.setAndInsertAudit(appObj.getVacancyCode(), appObj.getApplicantId(), actionTaken);
				}
				ram.updateApplicantStatus(applicantObj);//update the applicant status
			 }
			
			//call init
			init();
			return new Forward(FORWARD_SENDANDSAVE);
   	 	}else
   	 		return new Forward(FORWARD_ERROR);
		} catch (MessagingException e) {
            log.error(e.getMessage(), e);
            evt.getRequest().getSession().setAttribute("error", e);
            return new Forward(FORWARD_ERROR);
        } catch (AddressException e) {
            log.error(e.getMessage(), e);
            evt.getRequest().getSession().setAttribute("error", e);
            return new Forward(FORWARD_ERROR);
        } catch (FileNotFoundException e) {
            log.error(e.getMessage(), e);
            evt.getRequest().getSession().setAttribute("error", e);
            return new Forward(FORWARD_ERROR);
        } catch (StorageException e) {
            log.error(e.getMessage(), e);
            evt.getRequest().getSession().setAttribute("error", e);
            return new Forward(FORWARD_ERROR);
        }
	}
		
	//getter setter
	public AttachmentInfo getAttachmentInfo() {
		return attachmentInfo;
	}

	public void setAttachmentInfo(AttachmentInfo attachmentInfo) {
		this.attachmentInfo = attachmentInfo;
	}

	public TextBox getTbMessageBody() {
		return tbMessageBody;
	}

	public void setTbMessageBody(TextBox tbMessageBody) {
		this.tbMessageBody = tbMessageBody;
	}

	public TextField getTxtSubject() {
		return txtSubject;
	}

	public void setTxtSubject(TextField txtSubject) {
		this.txtSubject = txtSubject;
	}

	public Collection getInterviewObjCol() {
		return interviewObjCol;
	}

	public void setInterviewObjCol(Collection interviewObjCol) {
		this.interviewObjCol = interviewObjCol;
	}

	public Collection getSfCol() {
		return sfCol;
	}

	public void setSfCol(Collection sfCol) {
		this.sfCol = sfCol;
	}
	
	
}
