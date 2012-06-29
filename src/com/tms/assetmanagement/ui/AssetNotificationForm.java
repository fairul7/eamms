package com.tms.assetmanagement.ui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import kacang.Application;
import kacang.services.scheduling.JobSchedule;
import kacang.services.scheduling.JobTask;
import kacang.services.scheduling.SchedulingException;
import kacang.services.scheduling.SchedulingService;
import kacang.services.security.User;
import kacang.stdui.Button;
import kacang.stdui.CheckBox;
import kacang.stdui.DateField;
import kacang.stdui.Form;
import kacang.stdui.TextBox;
import kacang.stdui.TextField;
import kacang.stdui.TimeField;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;
import kacang.util.UuidGenerator;

import com.tms.assetmanagement.model.AssetJobSchedulerSendMail;
import com.tms.assetmanagement.model.AssetModule;
import com.tms.assetmanagement.model.DataAddressee;
import com.tms.assetmanagement.model.DataNotification;
import com.tms.collab.calendar.ui.UserUtil;
import com.tms.collab.messaging.model.IntranetAccount;
import com.tms.collab.messaging.model.Message;
import com.tms.collab.messaging.model.MessagingModule;
import com.tms.collab.messaging.model.SmtpAccount;
import com.tms.collab.messaging.model.Util;
import com.tms.assetmanagement.ui.ValidatorIsInteger;
import com.tms.assetmanagement.ui.ValidatorIsNumeric;

public class AssetNotificationForm extends Form{
	
	protected TextField txtfdNotification;
	protected TimeField[] timeNotification;
	protected DateField[] dateNotification;
	protected TextBox[] txtbxMsg;
	protected TextField txtfdTitle;
	protected PopUpUserSelectBx userSelect;
	protected CheckBox cbxMemo , cbxEmail;
	protected Button btnSubmit;
	String strNumbOfNotification="";
	int intNumberNotification = 0 ;
	String strNotification = "false";	

		public void init() {		
		super.init();
		removeChildren();
		setMethod("post");	
	}
	
	public void onRequest(Event event) {
		super.onRequest(event);
		removeChildren();
		
		Application.getInstance().getCurrentUser();
		txtfdNotification = new TextField("txtfdNotification");
		txtfdNotification.setValue("1");
		txtfdNotification.setSize("10");
		txtfdNotification.setMaxlength("2");
		addChild(txtfdNotification);			
				
		if(strNumbOfNotification != null && !strNumbOfNotification.equals("")){
		intNumberNotification = Integer.parseInt(strNumbOfNotification);
		
			if (intNumberNotification > 0 ){
				
				 timeNotification = new TimeField[intNumberNotification];
				 dateNotification = new DateField[intNumberNotification];
				 txtbxMsg = new TextBox[intNumberNotification];
				 
				 txtfdTitle = new TextField("txtfdTitle");
				 txtfdTitle.setSize("50");
				 txtfdTitle.setMaxlength("99");
				 txtfdTitle.addChild(new ValidatorNotEmpty("vEmpty",Application.getInstance().getMessage("asset.message.vNotEmpty")));
				 addChild(txtfdTitle);
						 
				 userSelect = new PopUpUserSelectBx("userSelect");
				 userSelect.addChild(new ValidatorNotEmpty("vEmpty",Application.getInstance().getMessage("asset.message.vNotEmpty")));
				 addChild(userSelect);
				 userSelect.init();				 
				 
				 cbxMemo = new CheckBox("radioMemo");
				 cbxMemo.setChecked(true);
				 cbxMemo.setGroupName("notifyMethod");
				 addChild(cbxMemo);
				 
				 cbxEmail = new CheckBox("radioEmail");
				 cbxEmail.setGroupName("notifyMethod");
				 addChild(cbxEmail);
				 
				 for (int i = 0;  i < intNumberNotification; i++){
					 
					 timeNotification[i]= new TimeField("timeNotification" + i);
					 Calendar TodayDate = Calendar.getInstance();
					 java.util.Date now=TodayDate.getTime();		
					 timeNotification[i].setDate(now);
					 addChild(timeNotification[i]);
					 
					 dateNotification[i]= new DateField("dateNotification" + i);
					 dateNotification[i].setDate(new java.util.Date());
					 addChild(dateNotification[i]);
					 
					 txtbxMsg[i] = new TextBox("txtbxMsg" + i);
					 txtbxMsg[i].setRows("5");
					 txtbxMsg[i].setCols("30");
					 txtbxMsg[i].addChild(new ValidatorNotEmpty("vEmpty",Application.getInstance().getMessage("asset.message.vNotEmpty")));
					 addChild(txtbxMsg[i]);					 
				 }
				 
				 btnSubmit = new Button("btnSubmit","Submit");
				 addChild(btnSubmit);						 
			}
		}
	}
	public Forward onSubmit(Event event){
		super.onSubmit(event);
		if (strNotification.equals("true"))
		return new Forward("addNotification");		
	return null;
	}
 
	public Forward onValidate(Event event) {
		super.onValidate(event);

		Application app = Application.getInstance();
		User currentUser;  
		currentUser =app.getCurrentUser();
		AssetModule  mod = (AssetModule)app.getModule(AssetModule.class);
		int i ;
		String btnClicked = findButtonClicked(event);
				
		if (btnClicked.equals(btnSubmit.getAbsoluteName())) {

			for (i = 0; i < intNumberNotification; i++) {

				UuidGenerator uuid = UuidGenerator.getInstance();
				DataNotification objNotification = new DataNotification();
				objNotification.setNotificationId(uuid.getUuid());
				objNotification.setNotificationTitle((String) txtfdTitle
						.getValue());
				objNotification.setNotificationDate(dateNotification[i]
						.getDate());
				objNotification.setNotificationTime(timeNotification[i]
						.getDate());
				objNotification.setNotificationMsg((String) txtbxMsg[i]
						.getValue());

				if (cbxMemo.isChecked() && !cbxEmail.isChecked())
					objNotification.setNotifyMethod("m");
				else if (!cbxMemo.isChecked() && cbxEmail.isChecked())
					objNotification.setNotifyMethod("e");
				else if (cbxMemo.isChecked() && cbxEmail.isChecked())
					objNotification.setNotifyMethod("b");
				else if (!cbxMemo.isChecked() && !cbxEmail.isChecked())
					objNotification.setNotifyMethod("m");

				objNotification.setSenderID((String) currentUser.getId());

				// save data
				mod.insertNotification(objNotification);

				String[] userids = userSelect.getIds();
				for (int a = 0; a < userids.length; a++) {
					DataAddressee objAdd = new DataAddressee();
					UuidGenerator uuid1 = UuidGenerator.getInstance();
					objAdd.setAddresseeId(uuid1.getUuid());
					objAdd.setRecipientId(userids[a]);
					objAdd.setNotificationId(objNotification
							.getNotificationId());

					mod.insertAddressee(objAdd);
				}
				int iTotalNotification = mod.iGetTotalNotification();
				// set job scheduler
				setScheduler(dateNotification[i].getDate(), timeNotification[i]
						.getDate(), objNotification.getNotificationId(),
						iTotalNotification + i);
			}
				return new Forward("inserted");				
		}
		return null;		
	}
	
		
	public void setScheduler(Date date, Date time, String sid, int i){		
		
		JobSchedule jobSchedule;
		//AssetJobScheduler job;
		SchedulingService ss;
		String jobName, jobGroup;
		Log log = Log.getLog(getClass());

		ss = (SchedulingService) Application.getInstance().getService(
				SchedulingService.class);
		if (ss == null) {
			log.error("Error getting scheduling service");
			return;
		}

		// remove from scheduler first, if exist
		jobName = "assetManagement_jobscheduler" + i;
		jobGroup = getClass().getName();
		jobSchedule = null;
		try {
			jobSchedule = ss.getJobSchedule(jobName, jobGroup);

		try {
				ss.unscheduleJob(jobSchedule);
			} catch (SchedulingException e) {
				log.error("Error unscheduling mailing list job", e);
			}
		} catch (Exception e) {
			// job is not in schedule, no need to remove
		}

		// create the schedule/trigger
		jobSchedule = new JobSchedule(jobName, JobSchedule.SECONDLY);
		jobSchedule.setGroup(jobGroup);

		Calendar testCalendar = Calendar.getInstance();
		int iYear = Integer.parseInt(new SimpleDateFormat("yyyy").format(date));
		int iMonth = Integer.parseInt(new SimpleDateFormat("MM").format(date));
		int iDay = Integer.parseInt(new SimpleDateFormat("dd").format(date));
		int iHour = Integer.parseInt(new SimpleDateFormat("HH").format(time));
		int iMin = Integer.parseInt(new SimpleDateFormat("mm").format(time));
		testCalendar.set(Calendar.DAY_OF_MONTH ,iDay);
		testCalendar.set(Calendar.MONTH, iMonth-1);
		testCalendar.set(Calendar.YEAR, iYear);
		testCalendar.set(Calendar.HOUR_OF_DAY, iHour);	
		testCalendar.set(Calendar.MINUTE, iMin);
		testCalendar.set(Calendar.SECOND,0);	
		jobSchedule.setStartTime(testCalendar.getTime());
		jobSchedule.setRepeatCount(0);
				
		// create the job
		JobTask job = new AssetJobSchedulerSendMail();
		job.setName(jobName);		
		job.setGroup(jobGroup);
		
		job.getJobTaskData().put("id", sid);		

 		// schedule the job
		try {
			ss.scheduleJob(job, jobSchedule);
		} catch (SchedulingException e) {
			log.error("Error scheduling mailing list job", e);
		} catch(Exception e){
			log.error("Error scheduling mailing list job", e);
		}
			
	}

	public String getDefaultTemplate(){		
		return "assetmanagement/assetNotification";		
	}
	
	public String getStrNumbOfNotification() {
		return strNumbOfNotification;
	}

	public void setStrNumbOfNotification(String strNumbOfNotification) {
		this.strNumbOfNotification = strNumbOfNotification;
	}

	public Button getBtnSubmit() {
		return btnSubmit;
	}

	public void setBtnSubmit(Button btnSubmit) {
		this.btnSubmit = btnSubmit;
	}

	public CheckBox getCbxEmail() {
		return cbxEmail;
	}

	public void setCbxEmail(CheckBox cbxEmail) {
		this.cbxEmail = cbxEmail;
	}

	public CheckBox getCbxMemo() {
		return cbxMemo;
	}

	public void setCbxMemo(CheckBox cbxMemo) {
		this.cbxMemo = cbxMemo;
	}

	public DateField[] getDateNotification() {
		return dateNotification;
	}

	public void setDateNotification(DateField[] dateNotification) {
		this.dateNotification = dateNotification;
	}

	public TimeField[] getTimeNotification() {
		return timeNotification;
	}

	public void setTimeNotification(TimeField[] timeNotification) {
		this.timeNotification = timeNotification;
	}

	public TextBox[] getTxtbxMsg() {
		return txtbxMsg;
	}

	public void setTxtbxMsg(TextBox[] txtbxMsg) {
		this.txtbxMsg = txtbxMsg;
	}

	public TextField getTxtfdTitle() {
		return txtfdTitle;
	}

	public void setTxtfdTitle(TextField txtfdTitle) {
		this.txtfdTitle = txtfdTitle;
	}

	public PopUpUserSelectBx getUserSelect() {
		return userSelect;
	}

	public void setUserSelect(PopUpUserSelectBx userSelect) {
		this.userSelect = userSelect;
	}

	public TextField getTxtfdNotification() {
		return txtfdNotification;
	}

	public void setTxtfdNotification(TextField txtfdNotification) {
		this.txtfdNotification = txtfdNotification;
	}

	public String getStrNotification() {
		return strNotification;
	}
	
	public void setStrNotification(String strNotification) {
		this.strNotification = strNotification;
	}
	



}
