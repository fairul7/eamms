package com.tms.hr.leave.ui;

import com.tms.hr.leave.model.*;
import kacang.Application;
import kacang.services.security.User;
import kacang.stdui.*;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;

public class PublicHolidaysSetupForm extends Form {
    private Label errorMsg;
    private DateField holidayDate;
    private TextField description;
    private Button setupButton;
    private Button resetButton;
    private Button cancel;

    Log log = Log.getLog(getClass());

    public PublicHolidaysSetupForm() {
        super();
    }

    public PublicHolidaysSetupForm(String name) {
        this();
        setName(name);
    }

    public void init() {
        setMethod("POST");
        errorMsg = new Label("errorMsg");
        errorMsg.setText("");
        holidayDate = new DatePopupField("holidayDate");
        ((DatePopupField)holidayDate).setOptional(false);
        description = new TextField("description");
        ValidatorNotEmpty empty = new ValidatorNotEmpty("empty", Application.getInstance().getMessage("leave.error.enterDescription","Enter Description"));
        description.addChild(empty);
        setupButton = new Button("setupButton");
        setupButton.setText(Application.getInstance().getMessage("leave.label.add","Add"));
        resetButton = new Button("resetButton");
        resetButton.setText(Application.getInstance().getMessage("leave.label.reset","Reset"));
        cancel = new Button("cancel");
        cancel.setText(Application.getInstance().getMessage("leave.label.cancel","Cancel"));

        addChild(errorMsg);
        addChild(holidayDate);
        addChild(description);
        addChild(setupButton);
        addChild(resetButton);
        addChild(cancel);
    }

    public Label getErrorMsg() {
        return errorMsg;
    }

    public DateField getHolidayDate() {
        return holidayDate;
    }


    public TextField getDescription() {
        return description;
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
            fwd = setupHolidays(evt.getRequest());
        }
        return fwd;
    }

    public Forward setupHolidays(HttpServletRequest req) {
        Forward forward;
        User user = getWidgetManager().getUser();
        SetupDataObject setup = new SetupDataObject();
        Calendar holidayCal = holidayDate.getCalendar();
        setup.setHolidayDate(holidayCal.getTime());
        setup.setType(LeaveModule.LEAVE_CALENDAR_PUBLICHOLIDAY_CODE);
        setup.setDescription(description.getValue().toString()); 
        try {
            Application application = Application.getInstance();
            LeaveModule handler = (LeaveModule) application.getModule(LeaveModule.class);
            handler.setupHolidays(setup, user);
            forward = new Forward("holidaySuccess");
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
        }
        return forward;
    }

    public String getDefaultTemplate() {
        return "leave/publicHolidaysSetupForm";
    }

}
