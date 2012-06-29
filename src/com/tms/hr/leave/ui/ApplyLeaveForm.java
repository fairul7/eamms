package com.tms.hr.leave.ui;

import com.tms.hr.leave.model.*;

import kacang.Application;

import kacang.model.DataObjectNotFoundException;

import kacang.services.security.User;

import kacang.stdui.*;

import kacang.stdui.validator.Validator;
import kacang.stdui.validator.ValidatorIsNumeric;
import kacang.stdui.validator.ValidatorNotEmpty;

import kacang.ui.Event;
import kacang.ui.Forward;

import kacang.util.Log;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;


/**
 * Form to apply for leave. For credit leave, set "credit" to true.
 * Set "admin" to true to allow application on another user's behalf.
 * Set "admin" to true and "adjustment" to true to make adjustments instead of a normal leave application
 */
public class ApplyLeaveForm extends Form {
    private boolean credit;
    private boolean admin;
    private boolean adjustment;
    protected EmployeeSelectBox employeeBox;
    protected DatePopupField startDate;
    protected DatePopupField endDate;
    protected TextField adjustmentDays;
    protected SelectBox adjustmentYear;
    protected CheckBox halfDay;
    protected SelectBox leaveType;
    protected TextBox reason;
    protected Button submitButton;
    protected Button cancelButton;
    protected Button checkDaysButton;
    protected Label errorMsg;
    protected Label effectiveDays;
    protected ValidatorDateRange validDateRange;
    Log log = Log.getLog(getClass());

    protected String showButton = "";

    public String getDefaultTemplate() {
    	return "form2";
    }
    
    
    public ApplyLeaveForm() {
    }

    public ApplyLeaveForm(String name) {
        super();
        setName(name);
    }

    public boolean isCredit() {
        return credit;
    }

    public void setCredit(boolean credit) {
        this.credit = credit;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public boolean isAdjustment() {
        return adjustment;
    }

    public void setAdjustment(boolean adjustment) {
        this.adjustment = adjustment;
    }

    public void init() {
        initForm();
    }

    public void onRequest(Event evt) {
        initForm();
    }

    public void initForm() {
        setMethod("POST");
        setColumns(2);
        removeChildren();

        // setup fields
        employeeBox = new EmployeeSelectBox("employeeBox");
        errorMsg = new Label("errorMsg");
        effectiveDays = new Label("effectiveDays");
        startDate = new DatePopupField("startDate");
        startDate.setOptional(false);
        endDate = new DatePopupField("endDate");
        endDate.setOptional(false);
        adjustmentDays = new TextField("adjustmentDays");
        adjustmentDays.setSize("4");
        adjustmentDays.addChild(new ValidatorIsNumeric("v1"));
        adjustmentYear = new SelectBox("adjustmentYear");

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);

        for (int i = year - 10; i < (year + 10); i++) {
            Integer iy = new Integer(i);
            adjustmentYear.addOption(iy.toString(), iy.toString());
        }

        adjustmentYear.setSelectedOption(new Integer(year).toString());
        halfDay = new CheckBox("halfDay",
                Application.getInstance().getMessage("leave.label.halfDayMessage",
                    "Half Day"));
        leaveType = new SelectBox("leaveType");
        leaveType.addChild(new ValidatorNotEmpty("notEmpty"));
        reason = new TextBox("reason");
        reason.addChild(new ValidatorNotEmpty("notEmpty",
                Application.getInstance().getMessage("leave.error.pleaseEnterReason",
                    "Please Enter Reason")));
        submitButton = new Button("submitButton");
        submitButton.setText(Application.getInstance().getMessage("leave.label.submit",
                "Submit"));
        cancelButton = new Button(Form.CANCEL_FORM_ACTION);
        cancelButton.setText(Application.getInstance().getMessage("general.label.cancel",
                "Cancel"));

        checkDaysButton = new Button("checkDaysButton", "Check Effective Days");

        validDateRange = new ValidatorDateRange("validDateRange");
        submitButton.addChild(validDateRange);

        Application application = Application.getInstance();
        LeaveModule handler = (LeaveModule) application.getModule(LeaveModule.class);
        User user = getWidgetManager().getUser();
        String userId = user.getId();
        boolean creditLeave = isCredit();
        boolean allowHalfDay = false;
        boolean admin = isAdmin();
        boolean adjustment = isAdjustment();

        try {
            LeaveSettings settings = handler.viewLeaveSettings(cal.get(
                        Calendar.YEAR));
            allowHalfDay = settings.isHalfDay();
        } catch (Exception e) {
        }

        // apply on behalf if admin mode
        if (admin) {
            addChild(new Label("l20",
                    application.getMessage("leave.label.employeeName",
                        "Employee Name")));
            addChild(employeeBox);
            employeeBox.init();
        }

        // set leave types
        try {
            Collection leaveTypes = handler.viewLeaveTypeList(userId,
                    creditLeave, admin);
            leaveType.setOptions(leaveTypes, "id", "name");
        } catch (Exception e) {
            Log.getLog(getClass()).error("Error retrieving leave types", e);
        }

        addChild(new Label("l0"));
        addChild(errorMsg);
        effectiveDays.setColspan(2);
        addChild(effectiveDays);
        addChild(new Label("l4",
                application.getMessage("leave.label.leaveTypes", "Leave Types")));
        addChild(leaveType);

        // dates or adjustment days
        if (admin && adjustment) {
            addChild(new Label("l21",
                    application.getMessage("leave.label.days", "Days")+" *"));
            addChild(adjustmentDays);
            addChild(new Label("l22",
                    application.getMessage("leave.label.year", "Year")+" *"));
            addChild(adjustmentYear);
        } else {
            addChild(new Label("l1",
                    application.getMessage("leave.label.startDate", "Start Date")+" *"));
            addChild(startDate);
            addChild(new Label("l2",
                    application.getMessage("leave.label.endDate", "End Date")+" *"));
            addChild(endDate);
        }

        // half day
        if (allowHalfDay && !adjustment) {
            addChild(new Label("l3",
                    application.getMessage("leave.label.halfDay", "Half Day")));
            addChild(halfDay);
        }

        // reason
        addChild(new Label("l5",
                application.getMessage("leave.label.reason", "Reason")+" *"));
        addChild(reason);

        // buttons
        Panel buttonPanel = new Panel("buttonPanel");
        buttonPanel.addChild(checkDaysButton);
        buttonPanel.addChild(submitButton);
        buttonPanel.addChild(cancelButton);
        addChild(new Label("l6"));
        addChild(buttonPanel);

        checkDaysButton.setHidden(("false".equals(showButton)));
    }

    public Forward onValidate(Event evt) {
        Forward forward = null;

        String buttonClicked = findButtonClicked(evt);

        if (buttonClicked.equals(checkDaysButton.getAbsoluteName())) {


               Collection selectedList = (Collection) this.leaveType.getValue();
                String leaveTypeId = (String) selectedList.iterator().next();

                Date start = startDate.getDate();
                Date end = endDate.getDate();
               // String reasonStr = reason.getValue().toString();
                boolean isHalfDay = halfDay.isChecked();
                boolean creditLeave = isCredit();
                boolean admin = isAdmin();
                boolean adjustment = isAdjustment();

                // get current user
                User user = getWidgetManager().getUser();


                Application application = Application.getInstance();
                LeaveModule handler = (LeaveModule) application.getModule(LeaveModule.class);

            try {


                 LeaveEntry leaveEntry= handler.checkEffectiveDays(leaveTypeId,start,end,isHalfDay,user.getId(),"nothing" ,user);

                 String tempGetDay ="";
                 tempGetDay +=leaveEntry.getDays();

                if(Float.parseFloat(tempGetDay) <=0) {
                //effectiveDays.setText( Application.getInstance().getMessage("leave.label.effectivedays","Effective Days: ")+"<B> "+ leaveEntry.getDays()+"</B>");
                	effectiveDays.setText(""+leaveEntry.getDays());
                } else {
                //effectiveDays.setText( Application.getInstance().getMessage("leave.label.effectivedays","Effective Days: ")+"<B> "+ Application.getInstance().getMessage("leave.label.notenoughbalance","Not enough Balance")+"</B>");
                	effectiveDays.setText(""+Application.getInstance().getMessage("leave.label.notenoughbalance","Not enough Balance"));
                }




            }



            catch (HolidayException e) {
                     //effectiveDays.setText( Application.getInstance().getMessage("leave.label.effectivedays","Effective Days: ")+"<B> "+ Application.getInstance().getMessage("leave.label.notepublicholiday","Cannot apply leave on public holiday")+"</B>");
            		 effectiveDays.setText(Application.getInstance().getMessage("leave.label.notepublicholiday","Cannot apply leave on public holiday"));
                     Log.getLog(getClass()).info("Cannot apply leave on public holiday", e);
            }
            
            catch(LeaveException e){
                 //effectiveDays.setText( Application.getInstance().getMessage("leave.label.effectivedays","Effective Days: ")+"<B> "+ Application.getInstance().getMessage("leave.label.notenoughbalance","Not enough Balance")+"</B>");
            	effectiveDays.setText(Application.getInstance().getMessage("leave.label.notenoughbalance","Not enough Balance"));
                     Log.getLog(getClass()).info("Don't have enough balance to take leave", e);

            }



        }

        if (buttonClicked.equals(submitButton.getAbsoluteName())) {
            try {
                // get leave type
                Collection selectedList = (Collection) this.leaveType.getValue();
                String leaveTypeId = (String) selectedList.iterator().next();

                Date start = startDate.getDate();
                Date end = endDate.getDate();
                String reasonStr = reason.getValue().toString();
                boolean isHalfDay = halfDay.isChecked();
                boolean creditLeave = isCredit();
                boolean admin = isAdmin();
                boolean adjustment = isAdjustment();

                // get current user
                User user = getWidgetManager().getUser();

                if (admin && adjustment) {
                    // get days
                    String dayStr = (String) adjustmentDays.getValue();
                    Collection tmp = (Collection) adjustmentYear.getValue();
                    int year = Integer.parseInt(tmp.iterator().next().toString());
                    float days = Float.parseFloat(dayStr);

                    // get selected users
                    String[] ids = employeeBox.getIds();

                    if (ids != null) {
                        for (int i = 0; i < ids.length; i++) {
                            String userId = ids[i];

                            try {
                                submitAdjustment(leaveTypeId, year, days,
                                    userId, reasonStr, user);
                            } catch (LeaveException e) {
                                Log.getLog(getClass()).debug("Unable to submit adjustment for user " +
                                    userId + ": " + toString());
                            }
                        }
                    }
                } else if (admin) {
                    // get selected users
                    String[] ids = employeeBox.getIds();

                    if (ids != null) {
                        for (int i = 0; i < ids.length; i++) {
                            String userId = ids[i];

                            try {
                                applyLeave(creditLeave, leaveTypeId, start,
                                    end, isHalfDay, userId, reasonStr, user);
                            } catch (LeaveException e) {
                                Log.getLog(getClass()).debug("Unable to apply leave for user " +
                                    userId + ": " + e.toString());
                            }
                        }
                    }
                } else {
                    // apply personal
                    String userId = user.getId();

                    // call module
                    applyLeave(creditLeave, leaveTypeId, start, end, isHalfDay,
                        userId, reasonStr, user);
                }

                forward = new Forward("success");
            } catch (BalanceException e) {
                errorMsg.setText("<span class=\"highlight\">" +
                    Application.getInstance().getMessage("leave.error.notEnoughBalanceToApplyLeave",
                        "Not Enough Balance To Apply Leave" + "</span>"));
                forward = new Forward("failure");
            } catch (DuplicateDateException e) {
                errorMsg.setText("<span class=\"highlight\">" +
                    Application.getInstance().getMessage("leave.error.leaveAlreadyAppliedOnTheseDates",
                        "Leave Already Applied On These Dates" + "</span>"));
                forward = new Forward("failure");
            } catch (HolidayException e) {
                errorMsg.setText("<span class=\"highlight\">" +
                    Application.getInstance().getMessage("leave.error.appliedOnHoliday",
                        "Applied On Holiday(s)" + "</span>"));
                forward = new Forward("failure");
            } catch (Exception e) {
                errorMsg.setText("<span class=\"highlight\">" +
                    Application.getInstance().getMessage("leave.error.leaveNotApplied",
                        "Leave Not Applied. Please Contact Your System Administrator" +
                        "</span>"));
                forward = new Forward("failure");
            }
        }

        return forward;
    }

    protected void applyLeave(boolean creditLeave, String leaveTypeId,
        Date start, Date end, boolean halfDay, String userId, String reasonStr,
        User user) throws LeaveException {
        Application application = Application.getInstance();
        LeaveModule handler = (LeaveModule) application.getModule(LeaveModule.class);

        if (!creditLeave) {
            handler.applyLeave(leaveTypeId, start, end, halfDay, userId,
                reasonStr, user);
        } else {
            handler.applyCreditLeave(leaveTypeId, start, end, halfDay, userId,
                reasonStr, user);
        }
    }






    protected void submitAdjustment(String leaveTypeId, int year, float days,
        String userId, String reasonStr, User user)
        throws LeaveException, DataObjectNotFoundException {
        Application application = Application.getInstance();
        LeaveModule handler = (LeaveModule) application.getModule(LeaveModule.class);
        handler.submitAdjustment(leaveTypeId, year, days, userId, reasonStr,
            user);
    }

    public class ValidatorDateRange extends Validator {
        public ValidatorDateRange() {
            super();
        }

        public ValidatorDateRange(String name) {
            super(name);
        }

        public boolean validate(FormField formField) {
        	errorMsg.setText("");
            if ((startDate.getDate() != null) && (endDate.getDate() != null)) {
                if (halfDay.isChecked() &&
                        (LeaveModule.utilDateDifference(endDate.getDate(),
                            startDate.getDate()) > 1)) {
                    startDate.setInvalid(true);
                    endDate.setInvalid(true);
                    errorMsg.setText("<span class=\"highlight\">" +
                        Application.getInstance().getMessage("leave.error.halfDay",
                            "Half Day Applied To One Day Only!" + "</span>"));

                    return false;
                } else if (startDate.getDate().after(endDate.getDate())) {
                    startDate.setInvalid(true);
                    endDate.setInvalid(true);
                    errorMsg.setText("<span class=\"highlight\">" +
                        Application.getInstance().getMessage("leave.error.endDateIsLessThanStartDate",
                            "End Date Is Less Than Start Date" + "</span>"));

                    return false;
                } else {
                    // check for same year
                    Calendar start = Calendar.getInstance();
                    start.setTime(startDate.getDate());

                    Calendar end = Calendar.getInstance();
                    end.setTime(endDate.getDate());

                    if (start.get(Calendar.YEAR) != end.get(Calendar.YEAR)) {
                        startDate.setInvalid(true);
                        endDate.setInvalid(true);
                        errorMsg.setText("<span class=\"highlight\">" +
                            Application.getInstance().getMessage("leave.error.differentYear",
                                "Start Date and End Date must be in the same year") +
                            "</span>");

                        return false;
                    }
                }
            }

            return true;
        }
    }

    public void setShowButton(String value)
    {
        this.showButton = value;
    }
}
