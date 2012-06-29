package com.tms.hr.leave.ui;

import com.tms.hr.leave.model.*;
import com.tms.util.FormatUtil;
import kacang.Application;
import kacang.stdui.*;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;

public class PublicHolidaysEditForm extends Form {
    private String date;
    private Label errorMsg;
    private DateField holidayDate;
    private SelectBox type;
    private TextField description;
    private Button setupButton;
    private Button resetButton;
    private Button cancel;
    private String userId;
    private SetupDataObject oldSetupObject;

    Log log = Log.getLog(getClass());

    public PublicHolidaysEditForm() {
    }

    public PublicHolidaysEditForm(String s) {
        super(s);
    }

    public void init() {
        setMethod("POST");
        String holidayType[] = new String[1];
        SetupDataObject setup = null;
        oldSetupObject = new SetupDataObject();
        errorMsg = new Label("errorMsg");
        errorMsg.setText("");
        holidayDate = new DatePopupField("holidayDate");
        ((DatePopupField)holidayDate).setOptional(false);
        type = new SelectBox("type");
        setupButton = new Button("setupButton");
        setupButton.setText(Application.getInstance().getMessage("leave.label.update","Update"));
        resetButton = new Button("resetButton");
        resetButton.setText(Application.getInstance().getMessage("leave.label.reset","Reset"));
        cancel = new Button("cancel");
        cancel.setText(Application.getInstance().getMessage("leave.label.cancel","Cancel"));

        type.addOption(LeaveModule.LEAVE_CALENDAR_NORMALHOLIDAY_CODE, LeaveModule.LEAVE_CALENDAR_NORMALHOLIDAY_DESC);
        type.addOption(LeaveModule.LEAVE_CALENDAR_PUBLICHOLIDAY_CODE, LeaveModule.LEAVE_CALENDAR_PUBLICHOLIDAY_DESC);

        LeaveModule handler;
        Application application = Application.getInstance();
        handler = (LeaveModule) application.getModule(LeaveModule.class);

    
        if (date != null && !date.equals("")) {
            try {
                Collection holiday = handler.getPublicHoliday(date);
                setup = (SetupDataObject) holiday.iterator().next();
                holidayType[0] = setup.getType();
            }
            catch (Exception e) {
                log.error(e.toString(), e);
            }
            holidayDate.setDate(setup.getHolidayDate());
            oldSetupObject.setHolidayDate(setup.getHolidayDate());
            type.setSelectedOptions(holidayType);
            description = new TextField("description", setup.getDescription());
            oldSetupObject.setDescription(setup.getDescription());
        }
        else {
            description = new TextField("description");
        }
        
        userId = application.getCurrentUser().getId();
        
        ValidatorNotEmpty empty = new ValidatorNotEmpty("empty", Application.getInstance().getMessage("leave.error.enterDescription","Enter Description"));
        description.addChild(empty);

        addChild(errorMsg);
        addChild(holidayDate);
        addChild(type);
        addChild(description);
        addChild(setupButton);
        addChild(resetButton);
        addChild(cancel);
        
        
        
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Label getErrorMsg() {
        return errorMsg;
    }

    public Button getSetupButton() {
        return setupButton;
    }

    public Button getResetButton() {
        return resetButton;
    }

    public Button getCancel() {
        return cancel;
    }

    public DateField getHolidayDate() {
        return holidayDate;
    }

    public SelectBox getType() {
        return type;
    }

    public TextField getDescription() {
        return description;
    }

    public Forward onSubmit(Event evt) {
        Forward result = null;
        result = super.onSubmit(evt);
        return result;
    }

    public Forward onValidationFailed(Event evt) {
        Forward fwd = null;
        String buttonName = findButtonClicked(evt);
        if (buttonName != null && (resetButton.getAbsoluteName().equals(buttonName) || cancel.getAbsoluteName().equals(buttonName))) {
            init();
            fwd = new Forward();
            fwd.setName(Form.CANCEL_FORM_ACTION);
            return fwd;
        }
        return fwd;
    }

    public Forward onValidate(Event evt) {
        Forward fwd = null;
        String buttonName = findButtonClicked(evt);
        if (buttonName != null && (resetButton.getAbsoluteName().equals(buttonName) || cancel.getAbsoluteName().equals(buttonName))) {
            init();
            fwd = new Forward();
            fwd.setName(Form.CANCEL_FORM_ACTION);
            return fwd;
        }
        else if (buttonName != null && setupButton.getAbsoluteName().equals(buttonName)) {
            fwd = updateHoliday(evt.getRequest());
        }
        return fwd;
    }

    protected Forward updateHoliday(HttpServletRequest req) {
        Forward forward = null;
        SetupDataObject setup = new SetupDataObject();
        Calendar holidayCal = holidayDate.getCalendar();
        setup.setHolidayDate(holidayCal.getTime());
        setup.setType(LeaveModule.LEAVE_CALENDAR_PUBLICHOLIDAY_CODE);
        Collection selectedList = (Collection) type.getValue();
        if (selectedList.size() > 0) {
            setup.setType((String) selectedList.iterator().next());
        }
        setup.setDescription(description.getValue().toString());
        try {
            Application application = Application.getInstance();
            LeaveModule handler = (LeaveModule) application.getModule(LeaveModule.class);
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            setup.setDate(df.parse(date));
            setup.setUserID(getUserId());
            
            if(handler.updateHolidays(setup,oldSetupObject))
            	forward = new Forward("holidaySuccess");
            else
            	forward = new Forward("requiredManualErase");
        }
        catch (EntitlementException le) {
            errorMsg.setText(Application.getInstance().getMessage("leave.error.entitlementNotSetForThisYear","Entitlement Not Set For This Year"));
            holidayDate.setInvalid(true);
            forward = new Forward("fail");
            Log.getLog(getClass()).error(le.toString(), le);
        }
        catch (DuplicateKeyException le) {
            errorMsg.setText(Application.getInstance().getMessage("leave.error.holidaySetBeforeOnThisDate","Holiday Set Before On This Date"));
            holidayDate.setInvalid(true);
            forward = new Forward("fail");
            Log.getLog(getClass()).error(le.toString(), le);
        }
        catch (LeaveException le) {
            errorMsg.setText(Application.getInstance().getMessage("leave.error.holidayNotSet","Holiday Not Set! Contact Your System Administrator"));
            holidayDate.setInvalid(true);
            forward = new Forward("fail");
            Log.getLog(getClass()).error(le.toString(), le);
        } catch (ParseException e) {
            Log.getLog(getClass()).error(e.toString(), e);
        }
        return forward;
    }

    public String getDefaultTemplate() {
        return "leave/publicHolidaysSetupForm";
    }

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public SetupDataObject getOldSetupObject() {
		return oldSetupObject;
	}

	public void setOldSetupObject(SetupDataObject oldSetupObject) {
		this.oldSetupObject = oldSetupObject;
	}

    
	
    
    
}
