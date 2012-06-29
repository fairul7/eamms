package com.tms.hr.leave.ui;

import com.tms.hr.leave.model.LeaveEntry;
import com.tms.hr.leave.model.LeaveModule;
import kacang.Application;
import kacang.services.security.User;
import kacang.stdui.Button;
import kacang.stdui.Form;
import kacang.stdui.Label;
import kacang.stdui.TextField;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

import java.util.Arrays;

public class ViewLeaveEntryForm extends Form {

    private String id;
    private LeaveEntry entry;
    private TextField reasonRejectText;
    private String statusRejectTxtfd;

    protected Button approveLeaveButton;
    protected Button rejectLeaveButton;
    protected Button cancelLeaveButton;




    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LeaveEntry getEntry() {
        return entry;
    }

    public void setEntry(LeaveEntry entry) {
        this.entry = entry;
    }

    public String getDefaultTemplate() {
        return "leave/viewLeaveEntry";
    }

    public void init() {
        initForm();
    }

    public void onRequest(Event evt) {
        initForm();


        reasonRejectText = new TextField("reasonRejectText");   
        addChild(reasonRejectText);

    }

    public void initForm() {
        setMethod("POST");
        removeChildren();
        
        
        Application application = Application.getInstance();
        approveLeaveButton = new Button("approveLeaveButton", application.getMessage("leave.label.approve", "Approve"));
        approveLeaveButton.setOnClick("return confirm(\"" + application.getMessage("leave.label.approveselecteditem", "Approve selected item(s)") + "\")");
        approveLeaveButton.setHidden(true);
        rejectLeaveButton = new Button("rejectLeaveButton", application.getMessage("leave.label.reject", "Reject"));
        rejectLeaveButton.setOnClick("return confirm(\"" + application.getMessage("leave.label.rejectselecteditem", "Reject selected item(s)") + "\")");
        rejectLeaveButton.setHidden(true);
        cancelLeaveButton = new Button("cancelLeaveButton", application.getMessage("leave.label.cancelLeave", "Cancel Leave"));
        cancelLeaveButton.setOnClick("return confirm(\"" + application.getMessage("leave.label.cancelLeave", "Cancel Leave") + "\")");
        cancelLeaveButton.setHidden(true);
        addChild(approveLeaveButton);
        addChild(rejectLeaveButton);
        addChild(cancelLeaveButton);

        //Default: Hide reasonRejectText
        statusRejectTxtfd="HIDE";
        
        String id = getId();
        if (id != null) {
            try {
                LeaveModule lm = (LeaveModule)application.getModule(LeaveModule.class);
                User user = getWidgetManager().getUser();
                boolean isAdmin = lm.checkForAdmin(user);
                entry = lm.viewLeaveEntry(id, user);

                if (!entry.isAdjustment()) {
                    if (LeaveModule.STATUS_SUBMITTED.equals(entry.getStatus()) || LeaveModule.STATUS_CANCEL_SUBMITTED.equals(entry.getStatus())) {

                        // check for approver status
                        String[] ids = lm.viewEmployeeListForApprover(user.getId());
                        boolean isApprover = Arrays.asList(ids).contains(entry.getUserId());

                        if (isAdmin || isApprover) {
                        	statusRejectTxtfd ="SHOW";
                            approveLeaveButton.setHidden(false);
                            rejectLeaveButton.setHidden(false);
                            if (!entry.isCredit()) {                            	
                                cancelLeaveButton.setHidden(false);
                            }
                        }
                    }

                    if (LeaveModule.STATUS_SUBMITTED.equals(entry.getStatus()) || LeaveModule.STATUS_APPROVED.equals(entry.getStatus())) {
                        if (!entry.isCredit() && isAdmin || user.getId().equals(entry.getUserId())) {
                        	cancelLeaveButton.setHidden(false);                        	
                        }
                    }

	                // after approved cannot cancel Credit Leave
	                if (LeaveModule.STATUS_APPROVED.equals(entry.getStatus()) && entry.isCredit()) {
	                    cancelLeaveButton.setHidden(true);	 
	                }
	                
	                if (LeaveModule.STATUS_REJECTED.equals(entry.getStatus())) {
	                	statusRejectTxtfd ="SHOW";
	                }
                }
            }
            catch (Exception e) {
                Log.getLog(getClass()).error("Error retrieving leave entry " + id);
            }
        }

    }

    public Forward onValidate(Event evt) {

        Application application = Application.getInstance();
        LeaveModule lm = (LeaveModule)application.getModule(LeaveModule.class);
        User user = getWidgetManager().getUser();

        try {
            String buttonClicked = findButtonClicked(evt);
            if (approveLeaveButton.getAbsoluteName().equals(buttonClicked)) {
                lm.approveLeave(entry.getId(), "", user);
                return new Forward("approve");
            }
            else if (rejectLeaveButton.getAbsoluteName().equals(buttonClicked)) {
                //lm.rejectLeave(entry.getId(), "", user);



                if(reasonRejectText.getValue() ==null || "".equals(reasonRejectText.getValue()) )
                    lm.rejectLeave(entry.getId(), "", user);
                if(reasonRejectText.getValue() !=null && !("".equals(reasonRejectText.getValue())))
                    lm.rejectLeaveWithReason(entry.getId(), (String)reasonRejectText.getValue(),"", user);




                return new Forward("reject");
            }
            else if (cancelLeaveButton.getAbsoluteName().equals(buttonClicked)) {
                lm.cancelLeave(entry.getId(), user);
                return new Forward("cancel");
            }
            else {
                return super.onValidate(evt);
            }
        }
        catch (Exception e) {
            Log.getLog(getClass()).error("Error updating leave entry " + id);
            return new Forward("failure");
        }
    }

	public String getStatusRejectTxtfd() {
		return statusRejectTxtfd;
	}

	public void setStatusRejectTxtfd(String statusRejectTxtfd) {
		this.statusRejectTxtfd = statusRejectTxtfd;
	}




}
