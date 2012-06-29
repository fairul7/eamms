package com.tms.hr.leave.ui;

import kacang.stdui.TabbedPanel;
import kacang.stdui.Table;
import kacang.stdui.SelectBox;
import kacang.ui.Event;
import kacang.ui.EventListener;
import kacang.ui.Forward;
import kacang.ui.Widget;
import kacang.util.Log;
import kacang.Application;

public class HolidaySetupPanel extends TabbedPanel implements EventListener {
    private PublicHolidaysSetupTable publicHolidaysSetupTable;
    private PublicHolidaysSetupForm publicHolidaysSetupForm;
    private PublicHolidaysEditForm publicHolidaysEditForm;


    private String title;



    public HolidaySetupPanel() {
        super();
    }

    public HolidaySetupPanel(String name) {
        this();
        setName(name);
    }

    public void init() {
        removeChildren();
        try {
            publicHolidaysSetupTable = new PublicHolidaysSetupTable("publicHoilidays");
            publicHolidaysSetupForm = new PublicHolidaysSetupForm("publicHolidaysSetupForm");
            publicHolidaysEditForm = new PublicHolidaysEditForm("publicHolidaysEditForm");
            title = Application.getInstance().getMessage("leavel.label.Holidays","Holidays");
            addChild(publicHolidaysSetupTable);
            addChild(publicHolidaysSetupForm);
            addChild(publicHolidaysEditForm);

            publicHolidaysSetupTable.init();


            publicHolidaysSetupForm.init();
            publicHolidaysEditForm.init();

            publicHolidaysSetupTable.setHidden(false);
            publicHolidaysSetupForm.setHidden(true);
            publicHolidaysEditForm.setHidden(true);

            publicHolidaysSetupTable.addEventListener(this);
            publicHolidaysSetupForm.addEventListener(this);
            publicHolidaysEditForm.addEventListener(this);
        }
        catch (Exception e) {
            Log log = Log.getLog(this.getClass());
            log.error(e.getMessage(), e);
        }
    }

    public Forward actionPerformed(Event evt) {
        Widget widget = evt.getWidget();
        Log log = Log.getLog(this.getClass());
        try {
            title = Application.getInstance().getMessage("leavel.label.Holidays","Holidays");
            log.debug("~~~ widget = " + widget.getName());
            Application app = Application.getInstance();
            if (widget.getName().equals(publicHolidaysSetupTable.getName())) {

                Table table = (Table) widget;
                log.debug("~~~ evt.getType() = " + evt.getType());
                String date = evt.getRequest().getParameter("holidayDate");
                if (evt.getType().equals(Table.PARAMETER_KEY_ACTION)) {
                    if (table.getSelectedAction().equalsIgnoreCase("add")) {
                        publicHolidaysSetupTable.setHidden(true);
                        publicHolidaysSetupForm.init();
                        publicHolidaysSetupForm.setHidden(false);
                        String title = app.getMessage("leave.label.addNewHoliday","Add New Holiday");
                        setTitle(title);
                        publicHolidaysEditForm.setHidden(true);
                    }
                    else if (table.getSelectedAction().equalsIgnoreCase("delete")) {
                        publicHolidaysSetupTable.init();
                        publicHolidaysSetupTable.setHidden(false);
                        publicHolidaysSetupForm.setHidden(true);
                        publicHolidaysEditForm.setHidden(true);
                    }
                }
                else if (evt.getType().equals(Table.PARAMETER_KEY_SELECTION)) {
                    if (date != null && !date.equals("")) {
                        publicHolidaysEditForm.setDate(date);
                        publicHolidaysSetupTable.setHidden(true);
                        publicHolidaysSetupForm.setHidden(true);
                        publicHolidaysEditForm.init();
                        publicHolidaysEditForm.setHidden(false);
                        String title = app.getMessage("leave.label.editHoliday","Edit Holiday");
                        setTitle(title);
                    }
                }
            }
            else if (widget.getName().equals(publicHolidaysSetupForm.getName())) {
                String buttonClicked = publicHolidaysSetupForm.findButtonClicked(evt);
                if (buttonClicked.equalsIgnoreCase(publicHolidaysSetupForm.getCancel().getAbsoluteName()) ||
                        (!publicHolidaysSetupForm.isInvalid() && buttonClicked.equalsIgnoreCase(publicHolidaysSetupForm.getSetupButton().getAbsoluteName()))) {
                    publicHolidaysSetupTable.init();
                    publicHolidaysSetupTable.setHidden(false);
                    publicHolidaysSetupForm.setHidden(true);
                    publicHolidaysEditForm.setHidden(true);
                    String title = app.getMessage("leave.label.Holidays","Holidays");
                    setTitle(title);
                }
            }
            else if (widget.getName().equals(publicHolidaysEditForm.getName())) {
                String buttonClicked = publicHolidaysEditForm.findButtonClicked(evt);
                if (buttonClicked.equalsIgnoreCase(publicHolidaysEditForm.getCancel().getAbsoluteName()) ||
                        (!publicHolidaysEditForm.isInvalid() && buttonClicked.equalsIgnoreCase(publicHolidaysEditForm.getSetupButton().getAbsoluteName()))) {
                    publicHolidaysSetupTable.setHidden(false);
                    publicHolidaysSetupForm.setHidden(true);
                    publicHolidaysEditForm.setHidden(true);
                    String title = app.getMessage("leave.label.Holidays","Holidays");
                    setTitle(title);
                }
            }

        }
        catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return new Forward();
    }


    public PublicHolidaysSetupTable getPublicHolidaysSetupTable() {
        return publicHolidaysSetupTable;
    }

    public void setPublicHolidaysSetupTable(PublicHolidaysSetupTable publicHolidaysSetupTable) {
        this.publicHolidaysSetupTable = publicHolidaysSetupTable;
    }

    public PublicHolidaysSetupForm getPublicHolidaysSetupForm() {
        return publicHolidaysSetupForm;
    }

    public void setPublicHolidaysSetupForm(PublicHolidaysSetupForm publicHolidaysSetupForm) {
        this.publicHolidaysSetupForm = publicHolidaysSetupForm;
    }

    public PublicHolidaysEditForm getPublicHolidaysEditForm() {
        return publicHolidaysEditForm;
    }

    public void setPublicHolidaysEditForm(PublicHolidaysEditForm publicHolidaysEditForm) {
        this.publicHolidaysEditForm = publicHolidaysEditForm;
    }

    public String getDefaultTemplate() {
        return "leave/holidaySetupPanel";
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
