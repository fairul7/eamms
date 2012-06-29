package com.tms.crm.helpdesk.ui;

import kacang.stdui.*;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.stdui.validator.ValidatorIsNumeric;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.Application;
import kacang.model.DaoException;
import kacang.util.UuidGenerator;
import kacang.util.Log;
import kacang.services.security.User;

import java.util.Date;

import com.tms.crm.helpdesk.Notification;
import com.tms.crm.helpdesk.NotificationModule;
import com.tms.crm.helpdesk.NotificationDao;

public class AlertNotificationSetup extends Form{
	private TextField firstAlert;
	private TextField subsequentAlert;
	private TextField alertOccurance;
	private CheckBox memo;
	private CheckBox email;
	private Button save;
	private String userId;
	private Date createdOn;
	public static final String ALERT_MEMO = "memo";
    public static final String ALERT_EMAIL = "email";
	private int total;
	private String incidentId;

	//View Setting
	private Label firstAlertLbl;
	private Label subsequentAlertLbl;
	private Label alertOccuranceLbl;
	private Label alertMethod1Lbl;
	private Label alertMethod2Lbl;

	public AlertNotificationSetup()
	{}
	public AlertNotificationSetup(String s)	{
		super(s);
	}

	public void initForm()	{

		firstAlert = new TextField("firstAlert");
		firstAlert.setSize("5");
		firstAlert.setMaxlength("5");
		firstAlert.addChild(new ValidatorNotEmpty("vFirstAlert"));
		firstAlert.addChild(new ValidatorIsNumeric("vinFirstAlert","Must be a number."));
		subsequentAlert = new TextField("subsequentAlert");
		subsequentAlert.setSize("5");
		subsequentAlert.setMaxlength("5");
		subsequentAlert.addChild(new ValidatorNotEmpty("vSubsequentAlert"));
		subsequentAlert.addChild(new ValidatorIsNumeric("vinSubsequentAlert","Must be a number."));
		alertOccurance = new TextField("alertOccurance");
		alertOccurance.setSize("5");
		alertOccurance.setMaxlength("5");
		alertOccurance.addChild(new ValidatorNotEmpty("vAlertOccurance"));
		alertOccurance.addChild(new ValidatorIsNumeric("vinAlertOccurance","Must be a number."));
		memo = new CheckBox("memo");
		memo.setText("Memo");
		memo.setGroupName("Method");
		email = new CheckBox("email");
		email.setText("Email");
		save = new Button("save");
		save.setText("Save Setting");

		// View Setting
		firstAlertLbl = new Label("firstAlertLbl");
		subsequentAlertLbl = new  Label("subsequentAlertLbl");
		alertOccuranceLbl = new Label("alertOccuranceLbl");
		alertMethod1Lbl = new Label("alertMethod1Lbl");
		alertMethod2Lbl = new Label("alertMethod2Lbl");
		addChild(firstAlertLbl);
		addChild(subsequentAlertLbl);
		addChild(alertOccuranceLbl);
		addChild(alertMethod1Lbl);
		addChild(alertMethod2Lbl);

		addChild(firstAlert);
		addChild(subsequentAlert);
		addChild(alertOccurance);
		addChild(memo);
		addChild(email);
		addChild(save);
		setMethod("POST");
	}

	public void init()	{
		initForm();
	}

	public void onRequest(Event event) {
		initForm();
		User user = Application.getInstance().getCurrentUser();
		userId = user.getId();
		createdOn = new Date();
		total = checkSetting();
		incidentId = null;
		if (total > 0)	{
			populateForm();
		}
	}

	public void populateForm()	{
		NotificationModule handler = (NotificationModule) Application.getInstance().getModule(NotificationModule.class);
		Notification notification  = handler.getAlertSettings();
		incidentId = notification.getId();
		firstAlert.setValue(notification.getFirstAlert());
		subsequentAlert.setValue(notification.getSubsequentAlert());
		alertOccurance.setValue(notification.getAlertOccurance());
		if("memo".equals(notification.getMethod1()))	{
			memo.setChecked(true);
		} if ("email".equals(notification.getMethod2()))	{
			email.setChecked(true);
		}
		save.setText("Update");
	}

	public Forward onValidate(Event event) {
		if (!memo.isChecked() && !email.isChecked()) {
			return new Forward("false");
		}

		NotificationModule handler = (NotificationModule) Application.getInstance().getModule(NotificationModule.class);
		Notification alert = new Notification();
		if (incidentId != null && !incidentId.equals(""))	{
			alert.setId(incidentId);
		} else	{
			alert.setId(UuidGenerator.getInstance().getUuid());
		}
		alert.setFirstAlert((String) firstAlert.getValue());
		alert.setSubsequentAlert((String) subsequentAlert.getValue());
		alert.setAlertOccurance((String) alertOccurance.getValue());
		String faStr = (String) firstAlert.getValue();
		String saStr = (String) subsequentAlert.getValue();
		String occStr = (String) alertOccurance.getValue();

		if (Integer.parseInt(occStr) < 0 )	{
			return new Forward("invalidOcc");
		} else if (Integer.parseInt(faStr) < 0) {
			return new Forward("invalidFa");
		} else if (Integer.parseInt(saStr) < 0) {
			return new Forward("invalidSa");
		}




		if (memo.isChecked())	{
			alert.setMethod1(ALERT_MEMO);
		} if (email.isChecked()) {
			alert.setMethod2(ALERT_EMAIL);
		}
		alert.setOccurance((String) alertOccurance.getValue());
		alert.setUserId(userId);
		alert.setCreatedOn(createdOn);
		if (incidentId != null && !incidentId.equals(""))	{
			try {
				handler.updateAlertSetting(alert);
			} catch (Exception e) {
				Log.getLog(getClass()).error(e.toString(), e);
			}
			return new Forward("updated");
		} else	{
			handler.insertNotification(alert);
			return new Forward("added");
		}
	}

	public int checkSetting()	{
		Application application = Application.getInstance();
		NotificationModule alert = (NotificationModule) application.getModule(NotificationModule.class);
		NotificationDao notification =  (NotificationDao) alert.getDao();
		int count = 0;
		try {
			count = notification.dispalyAlertSettingCount();
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.toString(), e);
		}
		return count;
	}

	public Notification getSetting()	{
		Application application = Application.getInstance();
		NotificationModule notification = (NotificationModule) application.getModule(NotificationModule.class);
		Notification alert = new Notification();
		alert = notification.getAlertSettings();
		return alert;
	}

	public String getDefaultTemplate() {
		return "helpdesk/alertNotificationSetup";
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}
}
