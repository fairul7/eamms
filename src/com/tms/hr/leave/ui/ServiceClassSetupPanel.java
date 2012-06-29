package com.tms.hr.leave.ui;

import kacang.stdui.TabbedPanel;
import kacang.stdui.Table;
import kacang.ui.EventListener;
import kacang.ui.Forward;
import kacang.ui.Event;
import kacang.ui.Widget;
import kacang.util.Log;
import kacang.Application;

public class ServiceClassSetupPanel extends TabbedPanel implements EventListener {
    private ServiceClassSetupTable setupTable;
    private ServiceClassAddForm addForm;
    private ServiceClassEditForm editForm;
    private String title;

    public ServiceClassSetupPanel() {
        super();
    }

    public ServiceClassSetupPanel(String name) {
        super(name);
    }

    public void init() {
        removeChildren();

        setupTable = new ServiceClassSetupTable("setupTable");
        addForm = new ServiceClassAddForm("addForm");
        editForm = new ServiceClassEditForm("editForm");
        addChild(setupTable);
        addChild(addForm);
        addChild(editForm);

        setupTable.init();
        setupTable.setHidden(false);
        addForm.setHidden(true);
        editForm.setHidden(true);

        setupTable.addEventListener(this);
        addForm.addEventListener(this);
        editForm.addEventListener(this);
        Application app = Application.getInstance();
        title = app.getMessage("leave.label.serviceClassification","Service Classification");
    }

    public Forward actionPerformed(Event evt) {
        Widget widget = evt.getWidget();
        Log log = Log.getLog(this.getClass());
        Application app = Application.getInstance();
        title = app.getMessage("leave.label.serviceClassification","Service Classification");
        try {
            if (widget.getName().equals(setupTable.getName())) {
                Table table = (Table) widget;
                String code = evt.getRequest().getParameter("serviceCode");
                if (evt.getType().equals(Table.PARAMETER_KEY_ACTION)) {
                    if (table.getSelectedAction().equalsIgnoreCase("delete")) {
                        setupTable.init();
                        setupTable.setHidden(false);
                        addForm.setHidden(true);
                        editForm.setHidden(true);
                    }
                    else if (table.getSelectedAction().equalsIgnoreCase("add")) {
                        setupTable.setHidden(true);
                        addForm.init();
                        addForm.setHidden(false);
                        editForm.setHidden(true);
                        String title = app.getMessage("leave.label.addNewServiceClassification","Add New Service Classification");
                        setTitle(title);
                    }
                }
                else if (evt.getType().equals(Table.PARAMETER_KEY_SELECTION)) {
                    if (code != null && !code.equals("")) {
                        editForm.setCode(code);
                        setupTable.setHidden(true);
                        addForm.setHidden(true);
                        editForm.init();
                        editForm.setHidden(false);
                        String title = app.getMessage("leave.label.editServiceClassification","Edit Service Classification");
                        setTitle(title);
                    }
                }
            }
            else if (widget.getName().equals(addForm.getName())) {
                String buttonClicked = addForm.findButtonClicked(evt);
                if (buttonClicked.equalsIgnoreCase(addForm.getCancel().getAbsoluteName()) ||
                        (!addForm.isInvalid() && buttonClicked.equalsIgnoreCase(addForm.getSave().getAbsoluteName()))) {
                    setupTable.init();
                    setupTable.setHidden(false);
                    addForm.setHidden(true);
                    editForm.setHidden(true);
                }
            }
            else if(widget.getName().equals(editForm.getName())) {
                String buttonClicked = editForm.findButtonClicked(evt);
                if (buttonClicked.equalsIgnoreCase(editForm.getCancel().getAbsoluteName()) ||
                        (!editForm.isInvalid() && buttonClicked.equalsIgnoreCase(editForm.getSave().getAbsoluteName()))) {
                    setupTable.init();
                    setupTable.setHidden(false);
                    addForm.setHidden(true);
                    editForm.setHidden(true);
                }
            }
        }
        catch (Exception e) {
            log.error("Error action performed " + e.toString(), e);
        }

        return super.actionPerformed(evt);
    }

    public String getDefaultTemplate() {
        return "leave/serviceClassSetupPanel";
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
