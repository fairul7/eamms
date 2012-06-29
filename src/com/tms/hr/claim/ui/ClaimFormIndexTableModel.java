/**
 * @author Vincent Lee, Wavelet Solutions Sdn Bhd (632468-W)
 * Created On: 2004-06-18
 * Copyright of The Media Shoppe Berhad
 */

package com.tms.hr.claim.ui;

import com.tms.hr.claim.model.ClaimFormIndex;
import com.tms.hr.claim.model.ClaimFormIndexModule;
import com.tms.hr.claim.model.ClaimFormItemModule;
import kacang.Application;
import kacang.util.Log;
import kacang.services.security.SecurityException;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.stdui.TableColumn;
import kacang.stdui.TableModel;
import kacang.ui.Event;
import kacang.ui.Forward;

import java.util.Collection;
import java.util.Vector;

class ClaimFormIndexTableModel extends TableModel {
    protected String[] theCondition = null;
    String state;

    protected void setState(String state) {
        this.state = state;
    }

    public ClaimFormIndexTableModel(String[] theCondition) {
        this.theCondition = theCondition;

    }


    protected void setColumns() {//Application.getInstance().getMessage("", )
        addColumn(new TableColumn("timeEditStr", Application.getInstance().getMessage("claims.label.lastModified", "Last Modified")));
        addColumn(new TableColumn("remarks", Application.getInstance().getMessage("claims.label.title", "Title")));
        addColumn(new TableColumn("ownerName", Application.getInstance().getMessage("claims.label.claimant", "Claimant")));
        addColumn(new TableColumn("originatorName", Application.getInstance().getMessage("claims.label.submittedBy", "Submitted By")));
        addColumn(new TableColumn("approver1Name", Application.getInstance().getMessage("claims.label.approver1", "Approver1")));
        addColumn(new TableColumn("approver2Name", Application.getInstance().getMessage("claims.label.approver2", "Approver2")));
        addColumn(new TableColumn("amountInStr", Application.getInstance().getMessage("claims.label.amount", "Amount")));
    }


    public Collection getTableRows() {
        if (getSort()!=null && getSort().equals("timeEditStr"))
            setSort("timeEdit");
        if (getSort()!=null && getSort().equals("originatorName"))
            setSort("userOriginator");
        if (getSort()!=null && getSort().equals("approver1Name"))
            setSort("userApprover1");
        if (getSort()!=null && getSort().equals("approver2Name"))
            setSort("userApprover2");
        if (getSort()!=null && getSort().equals("amountStr"))
            setSort("amount");
        if (getSort()!=null && getSort().equals("ownerName"))
                    setSort("userOwner");
        if (getSort()!=null && getSort().equals("amountInStr"))
                            setSort("amount");


        Application application = Application.getInstance();
        ClaimFormIndexModule module = (ClaimFormIndexModule)
        application.getModule(ClaimFormIndexModule.class);
        return module.findObjects(theCondition, getSort(),
        isDesc(), getStart(), getRows());
    }

    public int getTotalRowCount() {
        Application application = Application.getInstance();
        ClaimFormIndexModule module = (ClaimFormIndexModule)
        application.getModule(ClaimFormIndexModule.class);
        return module.countObjects(theCondition);
    }

    public String getTableRowKey() {
        return "id";
    }

    public Forward processAction(Event evt, String action, String[] selectedKeys) {
        return procAction(evt, action, selectedKeys);
    }


    public static Forward procAction(Event evt, String action, String[] selectedKeys) {
        if ("delete".equals(action)) {
            if (selectedKeys.length>0) {
                Application application = Application.getInstance();
                ClaimFormIndexModule module = (ClaimFormIndexModule)
                application.getModule(ClaimFormIndexModule.class);
                for (int i = 0; i < selectedKeys.length; i++) {
                    module.deleteObject(selectedKeys[i]);
                }
            }
            else {
                return new Forward("selectItem");
            }
        }

        boolean valid = true;
        if ("submit".equals(action)) {
            /// set the status of the claim form and its item to "sub"
            Application application = Application.getInstance();
            ClaimFormIndexModule module = (ClaimFormIndexModule)
            application.getModule(ClaimFormIndexModule.class);

            ClaimFormItemModule itemModule = (ClaimFormItemModule)
            application.getModule(ClaimFormItemModule.class);

            for (int i = 0; i < selectedKeys.length; i++) {
                Vector vecCLI = new Vector(itemModule.selectObjects("formId", selectedKeys[i], 0, -1));
                if (vecCLI.size() <= 0) {
                    valid = false;
                } else {
                    ClaimFormIndex theObj = module.selectObject(selectedKeys[i]);
                    int approvalLevelRequired = theObj.getApprovalLevelRequired().intValue();
                    if (approvalLevelRequired<=0) {
                        module.setState(selectedKeys[i],ClaimFormIndexModule.STATE_APPROVED);
                    }
                    else  {
                        module.setState(selectedKeys[i],ClaimFormIndexModule.STATE_SUBMITTED);
                        /// notify approvers
                        try {
                            com.tms.hr.claim.model.Mailer.notifyApproversOnSubmit(evt, theObj);
                        } catch (Exception ex) {
                            Log.getLog(ClaimFormIndexTableModel.class).error("Error notifying approvers", ex);
                        }
                    }
                }
            }

            if (selectedKeys.length<=0) {
                return new Forward("selectItem");
            }
            if (valid) {
                return new Forward("submitted");
            } else {
                return new Forward("failSubmit");
            }
        }

        if ("approve".equals(action)) {
            if (selectedKeys.length>0) {
                Application application = Application.getInstance();
                ClaimFormIndexModule module = (ClaimFormIndexModule)
                application.getModule(ClaimFormIndexModule.class);

                String approverId = evt.getWidgetManager().getUser().getId();

                SecurityService service = (SecurityService)
                Application.getInstance().getService(SecurityService.class);
                User usrAppBy = null;
                try {
                    usrAppBy = service.getUser(approverId);
                } catch (Exception ex) {
                    return null;
                }

                String description =  Application.getInstance().getMessage("claims.label.approveBy", "Approved By") + " : " + usrAppBy.getName() + ".";

                int nApprover = ClaimConfigApprovingLogic.getNumberOfApproverRequired(Application.getInstance());

                for (int i = 0; i < selectedKeys.length; i++) {
                    ClaimFormIndex theObj = module.selectObject(selectedKeys[i]);
                    if (!theObj.getInfo().endsWith(description))
                    {
	                    String tmpDesc = theObj.getInfo();
	                    tmpDesc += description;
	                    theObj.setInfo(tmpDesc);
	                    theObj.setApprovalLevelGranted(approverId);
	                    //if (theObj.getApprovedBy()==null || theObj.getApprovedBy().equals(""))
	                        theObj.setApprovedBy(usrAppBy.getName());
	                    //else
	                        //theObj.setApprovedBy(","+usrAppBy.getName());
	
	                    //if current user id == approver1 id, then write to approver 1 date
	                    if(Application.getInstance().getCurrentUser().getId().trim().equalsIgnoreCase(theObj.getUserApprover1().trim()) ){
	
	                       module.updateObjectApprover1(theObj);
	
	                    }
	
	                    if(Application.getInstance().getCurrentUser().getId().trim().equalsIgnoreCase(theObj.getUserApprover2().trim()) ){
	
	                       module.updateObjectApprover2(theObj);
	
	                    }
	
	
	
	                    //if current user id == approver2 id, then write to approver 2 date
	
	                  //  module.updateObject(theObj);
	
	
	
	                    if (nApprover < 2) {
	                        module.setState(selectedKeys[i],
	                        ClaimFormIndexModule.STATE_APPROVED);
	                    } else if (theObj.getFullyApproved()) {
	                        module.setState(selectedKeys[i],
	                        ClaimFormIndexModule.STATE_APPROVED);
	                    }
                    }
                }
            }
            else {
                return new Forward("selectItem");
            }
        }

        if ("assess".equals(action)) {
/*
            // deprecated: do not process this in table
            ClaimFormIndexModule.assessProcessClaim(selectedKeys, evt);
*/
        }

        if ("reject".equals(action)) {
            // deprecated: do not process this in table
            try {
                if (selectedKeys.length>0)
                    ClaimFormIndexModule.assessRejectClaim(evt, selectedKeys);
                else
                    return new Forward("selectItem");
            } catch (SecurityException e) {
                return null;
            }

        }

        if ("close".equals(action)) {
/// set the status of the claim form and its item to "sub"
            Application application = Application.getInstance();
            ClaimFormIndexModule module = (ClaimFormIndexModule)
            application.getModule(ClaimFormIndexModule.class);
            for (int i = 0; i < selectedKeys.length; i++) {
                module.setState(selectedKeys[i], ClaimFormIndexModule.STATE_CLOSED);
            }
        }

        if ("resubmit".equals(action)) {
/// set the status of the claim form and its item to "sub"
            Application application = Application.getInstance();
            ClaimFormIndexModule module = (ClaimFormIndexModule)
            application.getModule(ClaimFormIndexModule.class);
            for (int i = 0; i < selectedKeys.length; i++) {
                ClaimFormIndex theObj = module.selectObject(selectedKeys[i]);
                theObj.setState(ClaimFormIndexModule.STATE_CREATED);
                theObj.setApprovedBy("");
                module.updateObject(theObj);
                //module.setState(selectedKeys[i], ClaimFormIndexModule.STATE_CREATED);
            }
            if (selectedKeys.length<=0)
                return new Forward("selectItem");
        }


        return null;
    }

}







