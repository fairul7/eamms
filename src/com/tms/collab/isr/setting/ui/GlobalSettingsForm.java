package com.tms.collab.isr.setting.ui;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

import kacang.Application;
import kacang.services.scheduling.JobSchedule;
import kacang.services.scheduling.JobTask;
import kacang.services.scheduling.SchedulingException;
import kacang.services.scheduling.SchedulingService;
import kacang.stdui.Button;
import kacang.stdui.Form;
import kacang.stdui.Label;
import kacang.stdui.Panel;
import kacang.stdui.SelectBox;
import kacang.stdui.TextBox;
import kacang.stdui.TextField;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

import org.apache.commons.collections.SequencedHashMap;

import com.tms.collab.isr.jobs.DailyDigestJob;
import com.tms.collab.isr.model.RequestModel;
import com.tms.collab.isr.setting.model.ConfigDetailObject;
import com.tms.collab.isr.setting.model.ConfigModel;
import com.tms.collab.isr.ui.ValidatorIsInteger;
import com.tms.collab.isr.ui.ValidatorNotEmptySelectBox;

public class GlobalSettingsForm extends Form {
	protected SelectBox fileSizeUpload;
	protected TextField autoClose;
	protected TextField reminder;
	protected TextBox priority;
	protected TextBox attachmentFileExt;
	protected TextBox requestType;
	protected Panel autoClosurePanel;
	protected Panel reminderPanel;
	protected SelectBox dailyDigestScheduleHour;
	protected SelectBox dailyDigestScheduleMinute;
	protected Panel dailyDigestSchedulePanel;
	protected Button btnSubmit;
	protected Button btnCancel;
	public static final String FORWARD_SUCCESS = "success";
    public static final String FORWARD_ERROR = "error";
    public static final String FORWARD_INVALID_EXT = "invalid file ext";
    
    public void init(){
		super.init();
	}
    
    public void initForm() {
    	setMethod("POST");
		removeChildren();
		setColumns(2);
		
		Application application = Application.getInstance();
		
    	fileSizeUpload = new SelectBox("fileSizeUpload");
    	fileSizeUpload.setMultiple(false);
    	fileSizeUpload.setOptionMap(getRoleOptionList());
    	fileSizeUpload.addChild(new ValidatorNotEmptySelectBox("fileSizeUploadVNE", application.getMessage("isr.validator.notEmpty")));
    	
    	autoClose = new TextField("autoClose");
    	autoClose.setMaxlength("3");
    	autoClose.setValue("30");
    	autoClose.addChild(new ValidatorIsInteger("autoCloseIsInteger", application.getMessage("isr.validator.notInteger"), true));
    	autoClosurePanel = new Panel("autoClosurePanel");
    	autoClosurePanel.setRows(1);
    	autoClosurePanel.addChild(autoClose);
    	autoClosurePanel.addChild(new Label("lblDays", application.getMessage("isr.label.days", "Day(s)")));
    	
    	dailyDigestScheduleHour = new SelectBox("dailyDigestScheduleHour");
    	dailyDigestScheduleHour.setOptionMap(getSchedulerHourList());
    	dailyDigestScheduleHour.setSelectedOption("23");
    	dailyDigestScheduleHour.setMultiple(false);
    	
    	dailyDigestScheduleMinute = new SelectBox("dailyDigestScheduleMinute");
    	dailyDigestScheduleMinute.setOptionMap(getSchedulerMinuteList());
    	dailyDigestScheduleMinute.setSelectedOption("00");
    	dailyDigestScheduleMinute.setMultiple(false);
    	
    	dailyDigestSchedulePanel = new Panel("dailyDigestSchedulePanel");
    	dailyDigestSchedulePanel.setRows(1);
    	dailyDigestSchedulePanel.addChild(dailyDigestScheduleHour);
    	dailyDigestSchedulePanel.addChild(new Label("lblColon", ":"));
    	dailyDigestSchedulePanel.addChild(dailyDigestScheduleMinute);
    	
    	priority = new TextBox("priority");
    	priority.setCols("50");
    	priority.setRows("5");
    	priority.addChild(new ValidatorNotEmpty("priorityVNE", application.getMessage("isr.validator.notEmpty")));
    	
    	attachmentFileExt = new TextBox("attachmentFileExt");
    	attachmentFileExt.setCols("50");
    	attachmentFileExt.setRows("5");
    	attachmentFileExt.setOnKeyPress("return fileTypeChecking(event)");
    	attachmentFileExt.addChild(new ValidatorNotEmpty("attachmentFileExtVNE", application.getMessage("isr.validator.notEmpty")));
    	
    	requestType = new TextBox("requestType");
    	requestType.setCols("50");
    	requestType.setRows("5");
    	requestType.addChild(new ValidatorNotEmpty("requestTypeVNE", application.getMessage("isr.validator.notEmpty")));
    	
    	
    	reminder = new TextField("reminder");
    	reminder.setMaxlength("2");
    	reminder.setValue(ConfigDetailObject.REMINDER_DEFAULT);
    	reminder.addChild(new ValidatorIsInteger("reminderIsInteger", application.getMessage("isr.validator.notInteger"), true));
    	reminderPanel = new Panel("reminderPanel");
    	reminderPanel.setRows(1);
    	reminderPanel.addChild(reminder);
    	reminderPanel.addChild(new Label("lbl2Days", application.getMessage("isr.label.days", "Day(s)")));
    	
    	
    	Panel btnPanel = new Panel("btnPanel");
        btnPanel.setColumns(2);
        
        btnSubmit = new Button("btnSubmit", application.getMessage("isr.label.submit"));
        btnSubmit.setOnClick("return confirmSubmit()");
        btnPanel.addChild(btnSubmit);
        
        /*btnCancel = new Button(Form.CANCEL_FORM_ACTION, application.getMessage("isr.label.cancel"));
        btnCancel.setOnClick("return confirmCancel()");
        btnPanel.addChild(btnCancel);*/
        
        Label lblFileSizeUpload = new Label("lblFileSizeUpload", "<span class=\"fieldTitle\">" + application.getMessage("isr.label.fileSizeUpload", "File Size Upload") + " *" + "</span>");
        lblFileSizeUpload.setAlign("right");
        addChild(lblFileSizeUpload);
        addChild(fileSizeUpload);
        
        Label lblRequestAutoClose = new Label("lblRequestAutoClose", "<span class=\"fieldTitle\">" + application.getMessage("isr.label.requestAutoClose", "Default for Request Auto Close" + "</span>"));
        lblRequestAutoClose.setAlign("right");
        addChild(lblRequestAutoClose);
        addChild(autoClosurePanel);
        
        Label lblDailyDigestExecutionTime = new Label("lblDailyDigestExecutionTime", "<span class=\"fieldTitle\">" + application.getMessage("isr.label.dailyDigestExecutionTime", "Daily Digest Execution Time (Hour:Minute)" + "</span>"));
        lblDailyDigestExecutionTime.setAlign("right");
        addChild(lblDailyDigestExecutionTime);
        addChild(dailyDigestSchedulePanel);
        
        Label lblPriority = new Label("lblPriority", "<span class=\"fieldTitle\">" + application.getMessage("isr.label.priority", "Priority") + " *" + "</span>");
        lblPriority.setAlign("right");
        addChild(lblPriority);
        addChild(new Label("lblPriorityInstruction", application.getMessage("isr.label.priorityInstruction")));
        addChild(new Label("dummyLb1", ""));
        addChild(priority);
        
        Label lblAttachmentFileExt = new Label("lblAttachmentFileExt", "<span class=\"fieldTitle\">" + application.getMessage("isr.label.allowedFileExtensions", "Allowed Attachment File Extension(s)") + " *" + "</span>");
        lblAttachmentFileExt.setAlign("right");
        addChild(lblAttachmentFileExt);
        addChild(new Label("lblAttachmentFileExtInstruction", application.getMessage("isr.label.allowedFileExtensionsInstruction")));
        addChild(new Label("dummyLb2", ""));
        addChild(attachmentFileExt);
        
        Label lblRequestType = new Label("lblRequestType", "<span class=\"fieldTitle\">" + application.getMessage("isr.label.requestType", "Request Type") + " *" + "</span>");
        lblRequestType.setAlign("right");
        addChild(lblRequestType);
        addChild(new Label("lblRequestTypeInstruction", application.getMessage("isr.label.requestTypeInstruction")));
        addChild(new Label("dummyLb3", ""));
        addChild(requestType);
        
        Label lblReminder = new Label("lblReminder", "<span class=\"fieldTitle\">" + application.getMessage("isr.message.reminder", "Reminder") + " *" + "</span>");
        lblReminder.setAlign("right");
        addChild(lblReminder);
        addChild(reminderPanel);
        
        addChild(new Label("dummyLb4", ""));
        addChild(btnPanel);
    }
    
    public void onRequest(Event ev) {
    	initForm();
    	
    	ConfigModel model = (ConfigModel)Application.getInstance().getModule(ConfigModel.class);
		ConfigDetailObject config = new ConfigDetailObject();
		Collection cols;
		
		cols = model.getConfigDetailsByType(ConfigDetailObject.FILE_SIZE_UPLOAD, null);
		if(cols != null) {
			if(cols.size() > 0) {
				config = (ConfigDetailObject) cols.iterator().next();
				if(config.getConfigDetailName() != null && !"".equals(config.getConfigDetailName())) {
					fileSizeUpload.setSelectedOption(config.getConfigDetailName());
				}
			}
		}
		
		cols = model.getConfigDetailsByType(ConfigDetailObject.REQUEST_AUTO_CLOSE, null);
		if(cols != null) {
			if(cols.size() > 0) {
				config = (ConfigDetailObject) cols.iterator().next();
				if(config.getConfigDetailName() != null && !"".equals(config.getConfigDetailName())) {
					autoClose.setValue(config.getConfigDetailName());
				}
			}
		}
		
		cols = model.getConfigDetailsByType(ConfigDetailObject.DAILY_DIGEST_EXECUTION_HOUR, null);
		if(cols != null) {
			if(cols.size() > 0) {
				config = (ConfigDetailObject) cols.iterator().next();
				if(config.getConfigDetailName() != null && !"".equals(config.getConfigDetailName())) {
					dailyDigestScheduleHour.setSelectedOption(config.getConfigDetailName());
				}
			}
		}
		
		cols = model.getConfigDetailsByType(ConfigDetailObject.DAILY_DIGEST_EXECUTION_MINUTE, null);
		if(cols != null) {
			if(cols.size() > 0) {
				config = (ConfigDetailObject) cols.iterator().next();
				if(config.getConfigDetailName() != null && !"".equals(config.getConfigDetailName())) {
					dailyDigestScheduleMinute.setSelectedOption(config.getConfigDetailName());
				}
			}
		}
		cols = model.getConfigDetailsByType(ConfigDetailObject.REMINDER_SETTING, null);
		if(cols != null) {
			if(cols.size() > 0) {
				config = (ConfigDetailObject) cols.iterator().next();
				if(config.getConfigDetailName() != null && !"".equals(config.getConfigDetailName())) {
					reminder.setValue(config.getConfigDetailName());
				}
			}
		}		
		
		cols = model.getConfigDetailsByType(ConfigDetailObject.PRIORITY, null);
		String priorityList = "";
		int j=0;
		for(Iterator i=cols.iterator(); i.hasNext(); j++) {
			config = (ConfigDetailObject) i.next();
			if(config.getConfigDetailName() != null && !"".equals(config.getConfigDetailName())) {
				if(j != 0)
					priorityList += "\n";
				priorityList += config.getConfigDetailName();
			}
		}
		if(!"".equals(priorityList)) {
			priority.setValue(priorityList);
		}
		
		cols = model.getConfigDetailsByType(ConfigDetailObject.ALLOWED_FILE_EXTENSION, null);
		String fileExtList = "";
		j=0;
		for(Iterator i=cols.iterator(); i.hasNext(); j++) {
			config = (ConfigDetailObject) i.next();
			if(config.getConfigDetailName() != null && !"".equals(config.getConfigDetailName())) {
				if(j != 0)
					fileExtList += "\n";
				fileExtList += config.getConfigDetailName();
			}
		}
		if(!"".equals(fileExtList)) {
			attachmentFileExt.setValue(fileExtList);
		}
		
		cols = model.getConfigDetailsByType(ConfigDetailObject.REQUEST_TYPE, null);
		String requestTypeList = "";
		j=0;
		for(Iterator i=cols.iterator(); i.hasNext(); j++) {
			config = (ConfigDetailObject) i.next();
			if(config.getConfigDetailName() != null && !"".equals(config.getConfigDetailName())) {
				if(j != 0)
					requestTypeList += "\n";
				requestTypeList += config.getConfigDetailName();
			}
		}
		if(!"".equals(requestTypeList)) {
			requestType.setValue(requestTypeList);
		}
	}
    
    private Map getRoleOptionList() {
    	Map optionList = new SequencedHashMap();
    	Application application = Application.getInstance();
		String strFileSize = application.getProperty("isr.globalSetting.fileSize");
		
		if (strFileSize != null && !strFileSize.equals("")){
			StringTokenizer tokenizer = new StringTokenizer(strFileSize, ",");
			
			while(tokenizer.hasMoreTokens())
         	{
         		String strFileSizeUpload = tokenizer.nextToken();    
         		optionList.put(strFileSizeUpload, strFileSizeUpload);
         	}      	
		}
		else{
    	optionList.put("", "---" + application.getMessage("isr.label.selectUploadSize") + "---");
    	optionList.put("256KB", "256KB");
    	optionList.put("512KB", "512KB");
    	optionList.put("1MB", "1MB");
    	optionList.put("2MB", "2MB");
    	optionList.put("3MB", "3MB");
    	optionList.put("4MB", "4MB");
    	optionList.put("5MB", "5MB");
    	
		}
    	return optionList;
    }
    
    private Map getSchedulerHourList() {
    	Map optionList = new SequencedHashMap();
    	NumberFormat zeroFillingFormat = new DecimalFormat("00");
    	
    	for(int i=1; i<=23; i++) {
    		optionList.put(String.valueOf(i), zeroFillingFormat.format(i));
    	}
    	
    	return optionList;
    }
    
    private Map getSchedulerMinuteList() {
    	Map optionList = new SequencedHashMap();
    	optionList.put("0", "00");
    	optionList.put("30", "30");
    	
    	return optionList;
    }
    
    public Forward onSubmit(Event event) {
    	Forward forward = super.onSubmit(event);
        
    	String fileExtList = attachmentFileExt.getValue().toString();
    	ArrayList fileExts = getTextBoxValuesByLine(fileExtList, ".");
    	ArrayList invalidFileExts = null;
    	
    	for(int i=0; i<fileExts.size(); i++) {
    		String extItem = (String) fileExts.get(i);
    		if(extItem.indexOf(".") != extItem.lastIndexOf(".") || 
    				!extItem.startsWith(".")) {
    			attachmentFileExt.setInvalid(true);
    			this.setInvalid(true);
    			
    			if(invalidFileExts == null)
    				invalidFileExts = new ArrayList();
    			invalidFileExts.add(extItem);
    		}
    	}
    	
    	if(invalidFileExts != null) {
    		event.getRequest().setAttribute("invalidFileExts", invalidFileExts);
    		return new Forward(FORWARD_INVALID_EXT);
    	}
    	else {
    		return forward;
    	}
    }
    
    public Forward onValidate(Event event) {
		ConfigModel model = (ConfigModel)Application.getInstance().getModule(ConfigModel.class);
		ConfigDetailObject config = new ConfigDetailObject();
		boolean isSuccess = false;
		
		if(model.deleteConfigDetailsByType(ConfigDetailObject.FILE_SIZE_UPLOAD)) {
			String selectedFileSize = (String) fileSizeUpload.getSelectedOptions().keySet().iterator().next();
			config.setConfigDetailName(selectedFileSize);
			config.setConfigDetailDescription(selectedFileSize);
			config.setConfigDetailType(ConfigDetailObject.FILE_SIZE_UPLOAD);
	    	config.setConfigDetailOrder(new Integer(1));
	    	isSuccess = model.insertConfigDetail(config);
		}
    	
		if(model.deleteConfigDetailsByType(ConfigDetailObject.REQUEST_AUTO_CLOSE)) {
	    	String autoCloseValue = autoClose.getValue().toString();
	    	if("".equals(autoCloseValue)) {
	    		autoCloseValue = "30";
	    	}
	    	config = new ConfigDetailObject();
			config.setConfigDetailName(autoCloseValue);
			config.setConfigDetailDescription(autoCloseValue);
			config.setConfigDetailType(ConfigDetailObject.REQUEST_AUTO_CLOSE);
	    	config.setConfigDetailOrder(new Integer(1));
	    	isSuccess = model.insertConfigDetail(config);
		}
		if(model.deleteConfigDetailsByType(ConfigDetailObject.REMINDER_SETTING)) {
	    	String reminderSetting = reminder.getValue().toString();
	    	if("".equals(reminder)) {
	    		reminderSetting = ConfigDetailObject.REMINDER_DEFAULT;
	    	}
	    	config = new ConfigDetailObject();
			config.setConfigDetailName(reminderSetting);
			config.setConfigDetailDescription(reminderSetting);
			config.setConfigDetailType(ConfigDetailObject.REMINDER_SETTING);
	    	config.setConfigDetailOrder(new Integer(1));
	    	isSuccess = model.insertConfigDetail(config);
		}
		
		String selectedDailyDigestScheduleHour = "";
		String selectedDailyDigestScheduleMinute = "";
		if(model.deleteConfigDetailsByType(ConfigDetailObject.DAILY_DIGEST_EXECUTION_HOUR)) {
			config = new ConfigDetailObject();
			selectedDailyDigestScheduleHour = (String) dailyDigestScheduleHour.getSelectedOptions().keySet().iterator().next();
			config.setConfigDetailName(selectedDailyDigestScheduleHour);
			config.setConfigDetailDescription(selectedDailyDigestScheduleHour);
			config.setConfigDetailType(ConfigDetailObject.DAILY_DIGEST_EXECUTION_HOUR);
	    	config.setConfigDetailOrder(new Integer(1));
	    	isSuccess = model.insertConfigDetail(config);
		}
		
		if(model.deleteConfigDetailsByType(ConfigDetailObject.DAILY_DIGEST_EXECUTION_MINUTE)) {
			config = new ConfigDetailObject();
			selectedDailyDigestScheduleMinute = (String) dailyDigestScheduleMinute.getSelectedOptions().keySet().iterator().next();
			config.setConfigDetailName(selectedDailyDigestScheduleMinute);
			config.setConfigDetailDescription(selectedDailyDigestScheduleMinute);
			config.setConfigDetailType(ConfigDetailObject.DAILY_DIGEST_EXECUTION_MINUTE);
	    	config.setConfigDetailOrder(new Integer(1));
	    	isSuccess = model.insertConfigDetail(config);
		}
		
		if(isSuccess) {
			if(selectedDailyDigestScheduleHour != null && !"".equals(selectedDailyDigestScheduleHour) &&
					selectedDailyDigestScheduleMinute != null && !"".equals(selectedDailyDigestScheduleMinute)) {
				rescheduleDailyDigestJob(Integer.parseInt(selectedDailyDigestScheduleHour), Integer.parseInt(selectedDailyDigestScheduleMinute));
			}
		}
		
		if(model.deleteConfigDetailsByType(ConfigDetailObject.PRIORITY)) {
	    	String priorityList = priority.getValue().toString();
	    	ArrayList priorities = getTextBoxValuesByLine(priorityList, null);
	    	for(int i=0; i<priorities.size(); i++) {
	    		String priorityValue = priorities.get(i).toString();
	    		
	    		config = new ConfigDetailObject();
	    		config.setConfigDetailName(priorityValue);
	    		config.setConfigDetailDescription(priorityValue);
	    		config.setConfigDetailType(ConfigDetailObject.PRIORITY);
	        	config.setConfigDetailOrder(new Integer(i + 1));
	        	isSuccess = model.insertConfigDetail(config);
	    	}
		}
		
		if(model.deleteConfigDetailsByType(ConfigDetailObject.ALLOWED_FILE_EXTENSION)) {
	    	String fileExtList = attachmentFileExt.getValue().toString();
	    	ArrayList fileExts = getTextBoxValuesByLine(fileExtList, ".");
	    	for(int i=0; i<fileExts.size(); i++) {
	    		String fileExtValue = fileExts.get(i).toString();
	    		
	    		config = new ConfigDetailObject();
	    		config.setConfigDetailName(fileExtValue);
	    		config.setConfigDetailDescription(fileExtValue);
	    		config.setConfigDetailType(ConfigDetailObject.ALLOWED_FILE_EXTENSION);
	        	config.setConfigDetailOrder(new Integer(i + 1));
	        	isSuccess = model.insertConfigDetail(config);
	    	}
		}
		
		if(model.deleteConfigDetailsByType(ConfigDetailObject.REQUEST_TYPE)) {
	    	String requestTypeList = requestType.getValue().toString();
	    	ArrayList requestTypes = getTextBoxValuesByLine(requestTypeList, null);
	    	
	    	// Check if user has specified the default option of General
	    	boolean defaultValueSpecified = false;
	    	for(int i=0; i<requestTypes.size() && !defaultValueSpecified; i++) {
	    		String requestTypeValue = requestTypes.get(i).toString();
	    		if(requestTypeValue.equalsIgnoreCase("general")) {
	    			defaultValueSpecified = true;
	    		}
	    	}
	    	if(! defaultValueSpecified) {
	    		requestTypes.add(0, Application.getInstance().getMessage("isr.label.requestTypeDefaultOption"));
	    	}
	    	
	    	for(int i=0; i<requestTypes.size(); i++) {
	    		String requestTypeValue = requestTypes.get(i).toString();
	    		
	    		config = new ConfigDetailObject();
	    		config.setConfigDetailName(requestTypeValue);
	    		config.setConfigDetailDescription(requestTypeValue);
	    		config.setConfigDetailType(ConfigDetailObject.REQUEST_TYPE);
	        	config.setConfigDetailOrder(new Integer(i + 1));
	        	isSuccess = model.insertConfigDetail(config);
	    	}
		}
    	
    	if(isSuccess) {
    		return new Forward(FORWARD_SUCCESS);
    	}
    	else {
    		return new Forward(FORWARD_ERROR);
    	}
	}
    
    private void rescheduleDailyDigestJob(int hourOfDay, int minute) {
    	JobSchedule schedule = new JobSchedule(RequestModel.DAILY_DIGEST_NAME, JobSchedule.DAILY);
        Calendar calendar = Calendar.getInstance();
        /*calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.MONTH, Calendar.MAY);
        calendar.set(Calendar.YEAR, 2006);*/
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        schedule.setStartTime(calendar.getTime());
        schedule.setRepeatCount(JobSchedule.REPEAT_INDEFINITELY);
        schedule.setGroup(RequestModel.ISR_GROUP);
        
        JobTask dailyDigestTask = new DailyDigestJob();
        dailyDigestTask.setName(RequestModel.DAILY_DIGEST_NAME);
        dailyDigestTask.setGroup(RequestModel.ISR_GROUP);
        dailyDigestTask.setDescription(RequestModel.DAILY_DIGEST_DESC);
        
        SchedulingService service = (SchedulingService) Application.getInstance().getService(SchedulingService.class);
        
        try {
        	Log.getLog(getClass()).debug("Delete existing " + RequestModel.DAILY_DIGEST_NAME + " job and re-schedule a new one.");
            service.deleteJobTask(dailyDigestTask);
            service.scheduleJob(dailyDigestTask, schedule);
        } 
        catch (SchedulingException e) {
            Log.getLog(getClass()).error("Error re-initializing job: " + dailyDigestTask.getName(), e);
        }
    }
    
    private ArrayList getTextBoxValuesByLine(String value, String mandatoryPrefix) {
    	ArrayList values = new ArrayList();
    	char textChar[] = value.toCharArray();
        String delimitedString = "";
        boolean isNewString = true;
        
        for(int i=0; i<textChar.length; i++) {
            int ascii = textChar[i];
            // If textChar[i] not line feed
            if(ascii != 10) {
	            if(isNewString) {
	            	if(ascii != 13) {
		            	delimitedString = String.valueOf(textChar[i]);
		            	isNewString = false;
	            	}
	            }
	            else {
	            	// If textChar[i] is carriage return, then finalize the current item
	            	if(ascii == 13) {
	            		if(!"".equals(delimitedString.trim())) {
	            			if(mandatoryPrefix != null && !"".equals(mandatoryPrefix)) {
		            			if(delimitedString.indexOf(mandatoryPrefix) == -1) {
		                    		delimitedString = mandatoryPrefix + delimitedString;
		                    	}
	            			}
	            			values.add(delimitedString);
	            		}
	            		isNewString = true;
	                }
	                else {
	                	delimitedString += textChar[i];
	                }
	            }
            }
        }
        if(!"".equals(delimitedString.trim())) {
        	if(mandatoryPrefix != null && !"".equals(mandatoryPrefix)) {
    			if(delimitedString.indexOf(mandatoryPrefix) == -1) {
            		delimitedString = mandatoryPrefix + delimitedString;
            	}
			}
        	values.add(delimitedString);
        }
        
        return values;
    }

	public TextField getAutoClose() {
		return autoClose;
	}

	public Panel getAutoClosurePanel() {
		return autoClosurePanel;
	}

	public TextBox getAttachmentFileExt() {
		return attachmentFileExt;
	}

	public Button getBtnCancel() {
		return btnCancel;
	}

	public Button getBtnSubmit() {
		return btnSubmit;
	}

	public Panel getDailyDigestSchedulePanel() {
		return dailyDigestSchedulePanel;
	}

	public SelectBox getFileSizeUpload() {
		return fileSizeUpload;
	}

	public TextBox getPriority() {
		return priority;
	}
}
