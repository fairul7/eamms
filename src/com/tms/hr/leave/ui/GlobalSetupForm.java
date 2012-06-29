package com.tms.hr.leave.ui;

import kacang.stdui.*;
import kacang.stdui.validator.ValidatorIsNumeric;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.Application;
import kacang.util.Log;

import java.util.Calendar;
import java.util.Collection;

import com.tms.hr.leave.model.LeaveModule;
import com.tms.hr.leave.model.LeaveSettings;
import com.tms.hr.leave.model.LeaveException;

public class GlobalSetupForm extends Form {

    protected CheckBox halfDay;
    protected TextField carryForwardMaxDays;
    protected Radio workingDaysFive;
    protected Radio workingDaysFiveHalf;
    protected Radio workingDaysSix;
    protected CheckBox alternateWeekend;
    protected CheckBox fridayWeekend;
    protected CheckBox proRata;
    protected CheckBox realTime;
    protected CheckBox allowShift;
    protected CheckBox notifyMemo;
    protected CheckBox notifyEmail;
    protected CheckBox ccAdmin;
    protected SelectBox yearSelect;
    protected Button submitButton;
    protected Button carryForwardButton;

    public GlobalSetupForm() {
        super();
    }

    public GlobalSetupForm(String name) {
        super(name);
    }

    public String getDefaultTemplate() {
    	return "leave/globalSetupForm";
    }

    public void init() {
        initForm();
    }

    public void onRequest(Event evt) {
        initForm();
        loadSettings();
    }

    public CheckBox getHalfDay() { return this.halfDay; }
    public Radio getWorkingDaysFive() { return this.workingDaysFive; }
    public Radio getWorkingDaysFiveHalf() { return this.workingDaysFiveHalf; }
    public Radio getWorkingDaysSix() { return this.workingDaysSix; }
    public CheckBox getAlternateWeekend() { return this.alternateWeekend; }
    public CheckBox getFridayWeekend() { return this.fridayWeekend; }
    public CheckBox getProRata() { return this.proRata; }
    public CheckBox getRealTime() { return this.realTime; }
    public CheckBox getAllowShift() { return this.allowShift; }
    public CheckBox getNotifyMemo() { return this.notifyMemo; }
    public CheckBox getNotifyEmail() { return this.notifyEmail; }
    public CheckBox getCcAdmin() { return this.ccAdmin; }
    public TextField getCarryForwardMaxDays() { return this.carryForwardMaxDays; }
    public Button getSubmitButton() { return this.submitButton; }
    
    public Button getCarryForwardButton() { return this.carryForwardButton; }
    public SelectBox getYearSelect() { return this.yearSelect; }
    
    
    public void initForm() {
        removeChildren();
        setMethod("POST");                
        setColumns(2);

        Application app = Application.getInstance();

        halfDay = new CheckBox("halfDay", app.getMessage("leave.label.halfDay", "Allow Half Day"));
        carryForwardMaxDays = new TextField("carryForwardMaxDays");
        carryForwardMaxDays.setSize("5");
        carryForwardMaxDays.addChild(new ValidatorIsNumeric("v1", "", true));
        workingDaysFive = new Radio("workingDaysFive", app.getMessage("leave.label.fiveDays", "5 day week"));
        workingDaysFive.setGroupName("workingDays");
        workingDaysFiveHalf = new Radio("workingDaysFiveHalf", app.getMessage("leave.label.fiveHalfDays", "5 half day week"));
        workingDaysFiveHalf.setGroupName("workingDays");
        workingDaysSix = new Radio("workingDaysSix", app.getMessage("leave.label.sixDays", "Six day week"));
        workingDaysSix.setGroupName("workingDays");
        alternateWeekend = new CheckBox("alternateWeekend", app.getMessage("leave.label.alternateWeekend", "Alternate Weekend Off"));
        fridayWeekend = new CheckBox("fridayWeekend", app.getMessage("leave.label.fridayWeekend", "Friday Weekend"));
        proRata = new CheckBox("proRata", app.getMessage("leave.label.proRata", "Pro Rata Calculation"));
        proRata.setHidden(true);
        realTime = new CheckBox("realTime", app.getMessage("leave.label.realTime", "Real Time Balance Calculation"));
        realTime.setHidden(true);
        allowShift = new CheckBox("allowShift", app.getMessage("leave.label.allowShift", "Allow non fixed working time"));
        notifyMemo = new CheckBox("notifyMemo", app.getMessage("leave.label.notifyMemo", "Notify via Memo"));
        notifyEmail = new CheckBox("notifyEmail", app.getMessage("leave.label.notifyEmail", "Notify via Email"));
        ccAdmin = new CheckBox("ccAdmin", app.getMessage("leave.label.ccAdmin", "CC Administrator"));
        yearSelect = new SelectBox("yearSelect");
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        for (int i=year-10; i<year+10; i++) {
            Integer iy = new Integer(i);
            yearSelect.addOption(iy.toString(), iy.toString());
        }
        yearSelect.setSelectedOption(new Integer(year).toString());
        submitButton = new Button("submitButton", app.getMessage("leave.label.update","Update"));
        carryForwardButton = new Button("carryForwardButton", app.getMessage("leave.label.processCarryForwardLeave", "Process Carry Forward Leave"));

        Panel panel1 = new Panel("panel1");
        panel1.setColumns(1);
        Panel panel2 = new Panel("panel2");
        panel2.setColumns(1);
        Panel panel3 = new Panel("panel3");
        Panel panel4 = new Panel("panel4");
        panel1.addChild(halfDay);
        panel3.addChild(workingDaysFive);
        panel3.addChild(workingDaysFiveHalf);
        panel3.addChild(workingDaysSix);
        panel1.addChild(panel3);
        panel1.addChild(alternateWeekend);
        panel1.addChild(fridayWeekend);
        panel1.addChild(proRata);
        panel1.addChild(realTime);
        panel1.addChild(allowShift);
        panel1.addChild(notifyMemo);
        panel1.addChild(notifyEmail);
        panel1.addChild(ccAdmin);
        panel4.addChild(carryForwardMaxDays);
        panel4.addChild(new Label("l2", app.getMessage("leave.label.carryForwardMaxDays", "Carry Forward Max.Days")));
        panel1.addChild(panel4);
        panel1.addChild(submitButton);
        
        panel2.addChild(yearSelect);
        panel2.addChild(carryForwardButton);

        addChild(new Label("l1", app.getMessage("leave.label.Global", "Global")));
        addChild(panel1);
        addChild(new Label("l2", app.getMessage("leave.label.LeaveCarryForward", "Leave Carry Forward")));
        addChild(panel2);

    }

    public void loadSettings() {
        try {
            Application application = Application.getInstance();
            LeaveModule lm = (LeaveModule)application.getModule(LeaveModule.class);
            Calendar cal = Calendar.getInstance();
            LeaveSettings settings = lm.viewLeaveSettings(cal.get(Calendar.YEAR));
            halfDay.setChecked(settings.isHalfDay());
            String cfDays = (settings.getCarryForwardMaxDays() != null) ? settings.getCarryForwardMaxDays().toString() : "0";
            carryForwardMaxDays.setValue(cfDays);
            if ("5.5".equals(settings.getWorkingDays())) {
                workingDaysFiveHalf.setChecked(true);
            }
            else if ("6".equals(settings.getWorkingDays())) {
                workingDaysSix.setChecked(true);
            }
            else {
                workingDaysFive.setChecked(true);
            }
            alternateWeekend.setChecked(settings.isAlternateWeekend());
            fridayWeekend.setChecked(settings.isFridayWeekend());
            proRata.setChecked(settings.isProRata());
            realTime.setChecked(settings.isRealTime());
            allowShift.setChecked(settings.isAllowShift());
            notifyMemo.setChecked(settings.isNotifyMemo());
            notifyEmail.setChecked(settings.isNotifyEmail());
            ccAdmin.setChecked(settings.isCcAdmin());
        }
        catch (LeaveException e) {
            Log.getLog(getClass()).error("Error loading leave settings", e);
        }
    }

    public Forward onValidate(Event evt) {
        String button = findButtonClicked(evt);
        if (submitButton.getAbsoluteName().equals(button)) {
            try {
                Application application = Application.getInstance();
                LeaveModule lm = (LeaveModule)application.getModule(LeaveModule.class);
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);

                LeaveSettings settings = lm.viewLeaveSettings(year);
                settings.setHalfDay(halfDay.isChecked());
                settings.setCarryForwardMaxDays(carryForwardMaxDays.getValue().toString());
                if (workingDaysFiveHalf.isChecked()) {
                    settings.setWorkingDays("5.5");
                }
                else if (workingDaysSix.isChecked()) {
                    settings.setWorkingDays("6");
                }
                else {
                    settings.setWorkingDays("5");
                }
                settings.setAlternateWeekend(alternateWeekend.isChecked());
                settings.setFridayWeekend(fridayWeekend.isChecked());
                settings.setProRata(true);
                settings.setRealTime(true);
                settings.setAllowShift(allowShift.isChecked());
                settings.setNotifyMemo(notifyMemo.isChecked());
                settings.setNotifyEmail(notifyEmail.isChecked());
                settings.setCcAdmin(ccAdmin.isChecked());

                lm.updateLeaveSettings(settings, year);
                return new Forward("success");
            }
            catch (LeaveException e) {
                Log.getLog(getClass()).error("Error updating leave settings", e);
                return new Forward("failure");
            }
        }
        else if (carryForwardButton.getAbsoluteName().equals(button)) {
            try {
                // determine year
                Collection tmp = (Collection)yearSelect.getValue();
                int year = Integer.parseInt(tmp.iterator().next().toString());

                // process leave
                Application application = Application.getInstance();
                LeaveModule lm = (LeaveModule)application.getModule(LeaveModule.class);
                lm.processCarryForward(year);
                return new Forward("carryForwardSuccess");
            }
            catch (Exception e1) {
                return new Forward("carryForwardFailure");
            }
        }
        return super.onValidate(evt);
    }

}
