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

import kacang.services.security.SecurityException;
import kacang.services.security.SecurityService;
import kacang.services.security.User;

import kacang.stdui.*;

import kacang.ui.Event;
import kacang.ui.Forward;

import kacang.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;


class ClaimHistoryFormIndexTableModel extends TableModel {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    protected String[] theCondition = null;
    String state;

    public ClaimHistoryFormIndexTableModel(String[] theCondition) {
        this.theCondition = theCondition;
        sdf.setCalendar(Calendar.getInstance());
    }

    protected void setState(String state) {
        this.state = state;
    }

    protected void setColumns() {

        Application app = Application.getInstance();

        addColumn(new TableColumn("timeEditStr", Application.getInstance().getMessage("claims.label.submissiondate", "Submission Date")));
        
        TableColumn remarks = new TableColumn("remarks", Application.getInstance().getMessage("claims.label.title", "Title"));
        remarks.setUrlParam("id");
        addColumn(remarks);

        addColumn(new TableColumn("ownerName", Application.getInstance().getMessage("claims.label.claimant", "Claimant")));
        addColumn(new TableColumn("originatorName", Application.getInstance().getMessage("claims.label.submittedBy", "Submitted By")));
        
        TableColumn approver1NameCol = new TableColumn("approver1Name", Application.getInstance().getMessage("claims.label.approver1", "Approver1"));
        approver1NameCol.setSortable(false);
        addColumn(approver1NameCol);
        
        TableColumn approver2NameCol = new TableColumn("approver2Name", Application.getInstance().getMessage("claims.label.approver2", "Approver2"));
        approver2NameCol.setSortable(false);
        addColumn(approver2NameCol);
        
        TableColumn amountInStrCol = new TableColumn("amountInStr", Application.getInstance().getMessage("claims.label.amount", "Amount"));
        amountInStrCol.setSortable(false);
        addColumn(amountInStrCol);
        
        TableFilter tfRead = new TableFilter("keyword");
        addFilter(tfRead);



        TableFilter tf = new TableFilter("tf");
        Label label = new Label("startDateLabel","<b>"+app.getMessage("claims.label.startdate")+"</b>");
        tf.setWidget(label);
        addFilter(tf);


        TableFilter tfRead2 = new TableFilter("dateSelectedStart");
        DatePopupField startDate = new DatePopupField("startDate");

       
         Calendar calendar = Calendar.getInstance();

         calendar.add(Calendar.MONTH,-1);



        startDate.setDate(calendar.getTime());

        startDate.setOptional(true);

        tfRead2.setWidget(startDate);
        addFilter(tfRead2);


        TableFilter tf2 = new TableFilter("tf2");
        Label label2 = new Label("endDateLabel","<b>"+app.getMessage("claims.label.enddate")+"</b>");
        tf2.setWidget(label2);
        addFilter(tf2);



        TableFilter tfRead3 = new TableFilter("dateSelectedEnd");
        DatePopupField endDate = new DatePopupField("endDate");
        endDate.setOptional(true);
        endDate.setDate(Calendar.getInstance().getTime());

        tfRead3.setWidget(endDate);
        addFilter(tfRead3);
    }

    public Collection getTableRows() {
        if ((getSort() != null) && getSort().equals("timeEditStr")) {
            setSort("timeEdit");
        }

        if ((getSort() != null) && getSort().equals("originatorName")) {
            setSort("userOriginator");
        }

        if ((getSort() != null) && getSort().equals("approver1Name")) {
            setSort("userApprover1");
        }

        if ((getSort() != null) && getSort().equals("approver2Name")) {
            setSort("userApprover2");
        }

        if ((getSort() != null) && getSort().equals("amountStr")) {
            setSort("amount");
        }

        if ((getSort() != null) && getSort().equals("ownerName")) {
            setSort("userOwner");
        }

        if ((getSort() != null) && getSort().equals("amountInStr")) {
            setSort("amount");
        }

        String keyword = "";
        String[] newCondition = null;

        if (((String) getFilterValue("keyword") != null) &&
                !"".equals((String) getFilterValue("keyword"))) {
            keyword = (String) getFilterValue("keyword");

            Application app = Application.getInstance();
            ClaimFormIndexModule module= (ClaimFormIndexModule) app.getModule(ClaimFormIndexModule.class);
            User user = null;
            Collection users= new ArrayList();
            Collection userNames;
         
            	users =  module.getUsersByUsername("%"+keyword+"%");
            	
           

            newCondition = new String[theCondition.length + 1];

            for (int i = 0; i < theCondition.length; i++) {
                newCondition[i] = theCondition[i];
            }
            
            newCondition[theCondition.length]="";
            if(users !=null && users.size()>0)
            for (Iterator iterator = users.iterator(); iterator.hasNext();) {
            	User tempUser = (User)iterator.next();
            	
            	newCondition[theCondition.length] += " ( userOriginator LIKE '" + tempUser.getId()+ "' OR userOwner LIKE '"+tempUser.getId()+"'  ) ";
            	
            	if(iterator.hasNext())
            		newCondition[theCondition.length] +=" OR ";
            }
            
            
            
            
            
        } else {
            newCondition = new String[theCondition.length];
            newCondition = theCondition;
        }

        //check date
        Date startDate = new Date();

        if (getFilterValue("dateSelectedStart") != null) {
            try {
                startDate = sdf.parse((String) getFilterValue(
                            "dateSelectedStart") + " 00:00:00");
            } catch (ParseException e) {
                //cannot parse Date
                startDate = null;
            }
        } else {
            startDate = null;
        }

        Date endDate = new Date();

        if (getFilterValue("dateSelectedEnd") != null) {
            try {
                endDate = sdf.parse((String) getFilterValue("dateSelectedEnd") +
                        " 23:59:59");
            } catch (ParseException e) {
                //cannot parse Date
                endDate = null;
            }
        } else {
            endDate = null;
        }

        Application application = Application.getInstance();
        ClaimFormIndexModule module = (ClaimFormIndexModule) application.getModule(ClaimFormIndexModule.class);

        return module.findObjectsWithDate(newCondition, startDate, endDate,
            getSort(), isDesc(), getStart(), getRows());
    }

    public int getTotalRowCount() {
        String keyword = "";
        String[] newCondition = null;

        if (((String) getFilterValue("keyword") != null) &&
                !"".equals((String) getFilterValue("keyword"))) {
            keyword = (String) getFilterValue("keyword");

            Application app = Application.getInstance();
            ClaimFormIndexModule module= (ClaimFormIndexModule) app.getModule(ClaimFormIndexModule.class);
            User user = null;
            Collection users= new ArrayList();
            Collection userNames;
         
            	users =  module.getUsersByUsername("%"+keyword+"%");
            	
           

            newCondition = new String[theCondition.length + 1];

            for (int i = 0; i < theCondition.length; i++) {
                newCondition[i] = theCondition[i];
            }
            
            newCondition[theCondition.length]="";
            if(users !=null && users.size()>0)
            for (Iterator iterator = users.iterator(); iterator.hasNext();) {
            	User tempUser = (User)iterator.next();
            	
            	newCondition[theCondition.length] += " ( userOriginator LIKE '" + tempUser.getId()+ "' OR userOwner LIKE '"+tempUser.getId()+"'  ) ";
            	
            	if(iterator.hasNext())
            		newCondition[theCondition.length] +=" OR ";
            }
            
            
            
            
            
        } else {
            newCondition = new String[theCondition.length];
            newCondition = theCondition;
        }

        //check date
        Date startDate = new Date();

        if (getFilterValue("dateSelectedStart") != null) {
            try {
                startDate = sdf.parse((String) getFilterValue(
                            "dateSelectedStart") + " 00:00:00");
            } catch (ParseException e) {
                //cannot parse Date
                startDate = null;
            }
        } else {
            startDate = null;
        }

        Date endDate = new Date();

        if (getFilterValue("dateSelectedEnd") != null) {
            try {
                endDate = sdf.parse((String) getFilterValue("dateSelectedEnd") +
                        " 23:59:59");
            } catch (ParseException e) {
                //cannot parse Date
                endDate = null;
            }
        } else {
            endDate = null;
        }

        Application application = Application.getInstance();
        ClaimFormIndexModule module = (ClaimFormIndexModule) application.getModule(ClaimFormIndexModule.class);

        return module.countObjectsWithDate(newCondition, startDate, endDate);
    }

    public String getTableRowKey() {
        return "id";
    }

    public Forward processAction(Event evt, String action, String[] selectedKeys) {
        return procAction(evt, action, selectedKeys);
    }

    public static Forward procAction(Event evt, String action,
        String[] selectedKeys) {
        if ("delete".equals(action)) {
            if (selectedKeys.length > 0) {
                Application application = Application.getInstance();
                ClaimFormIndexModule module = (ClaimFormIndexModule) application.getModule(ClaimFormIndexModule.class);

                for (int i = 0; i < selectedKeys.length; i++) {
                    module.deleteObject(selectedKeys[i]);
                }
            } else {
                return new Forward("selectItem");
            }
        }

        boolean valid = true;

        if ("submit".equals(action)) {
            /// set the status of the claim form and its item to "sub"
            Application application = Application.getInstance();
            ClaimFormIndexModule module = (ClaimFormIndexModule) application.getModule(ClaimFormIndexModule.class);

            ClaimFormItemModule itemModule = (ClaimFormItemModule) application.getModule(ClaimFormItemModule.class);

            for (int i = 0; i < selectedKeys.length; i++) {
                Vector vecCLI = new Vector(itemModule.selectObjects("formId",
                            selectedKeys[i], 0, -1));

                if (vecCLI.size() <= 0) {
                    valid = false;
                } else {
                    ClaimFormIndex theObj = module.selectObject(selectedKeys[i]);
                    int approvalLevelRequired = theObj.getApprovalLevelRequired()
                                                      .intValue();

                    if (approvalLevelRequired <= 0) {
                        module.setState(selectedKeys[i],
                            ClaimFormIndexModule.STATE_APPROVED);
                    } else {
                        module.setState(selectedKeys[i],
                            ClaimFormIndexModule.STATE_SUBMITTED);

                        /// notify approvers
                        try {
                            com.tms.hr.claim.model.Mailer.notifyApproversOnSubmit(evt,
                                theObj);
                        } catch (Exception ex) {
                            Log.getLog(ClaimFormIndexTableModel.class).error("Error notifying approvers",
                                ex);
                        }
                    }
                }
            }

            if (selectedKeys.length <= 0) {
                return new Forward("selectItem");
            }

            if (valid) {
                return new Forward("submitted");
            } else {
                return new Forward("failSubmit");
            }
        }

        if ("approve".equals(action)) {
            if (selectedKeys.length > 0) {
                Application application = Application.getInstance();
                ClaimFormIndexModule module = (ClaimFormIndexModule) application.getModule(ClaimFormIndexModule.class);

                String approverId = evt.getWidgetManager().getUser().getId();

                SecurityService service = (SecurityService) Application.getInstance()
                                                                       .getService(SecurityService.class);
                User usrAppBy = null;

                try {
                    usrAppBy = service.getUser(approverId);
                } catch (Exception ex) {
                    return null;
                }

                String description = " Approved By: " + usrAppBy.getName() +
                    ".";

                int nApprover = ClaimConfigApprovingLogic.getNumberOfApproverRequired(Application.getInstance());

                for (int i = 0; i < selectedKeys.length; i++) {
                    ClaimFormIndex theObj = module.selectObject(selectedKeys[i]);
                    String tmpDesc = theObj.getInfo();
                    tmpDesc += description;
                    theObj.setInfo(tmpDesc);
                    theObj.setApprovalLevelGranted(approverId);

                    if ((theObj.getApprovedBy() == null) ||
                            theObj.getApprovedBy().equals("")) {
                        theObj.setApprovedBy(usrAppBy.getName());
                    } else {
                        theObj.setApprovedBy("," + usrAppBy.getName());
                    }

                    module.updateObject(theObj);

                    if (nApprover < 2) {
                        module.setState(selectedKeys[i],
                            ClaimFormIndexModule.STATE_APPROVED);
                    } else if (theObj.getFullyApproved()) {
                        module.setState(selectedKeys[i],
                            ClaimFormIndexModule.STATE_APPROVED);
                    }
                }
            } else {
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
                if (selectedKeys.length > 0) {
                    ClaimFormIndexModule.assessRejectClaim(evt, selectedKeys);
                } else {
                    return new Forward("selectItem");
                }
            } catch (SecurityException e) {
                return null;
            }
        }

        if ("close".equals(action)) {
            /// set the status of the claim form and its item to "sub"
            Application application = Application.getInstance();
            ClaimFormIndexModule module = (ClaimFormIndexModule) application.getModule(ClaimFormIndexModule.class);

            for (int i = 0; i < selectedKeys.length; i++) {
                module.setState(selectedKeys[i],
                    ClaimFormIndexModule.STATE_CLOSED);
            }
        }

        if ("resubmit".equals(action)) {
            /// set the status of the claim form and its item to "sub"
            Application application = Application.getInstance();
            ClaimFormIndexModule module = (ClaimFormIndexModule) application.getModule(ClaimFormIndexModule.class);

            for (int i = 0; i < selectedKeys.length; i++) {
                ClaimFormIndex theObj = module.selectObject(selectedKeys[i]);
                theObj.setState(ClaimFormIndexModule.STATE_CREATED);
                theObj.setApprovedBy("");
                module.updateObject(theObj);

                //module.setState(selectedKeys[i], ClaimFormIndexModule.STATE_CREATED);
            }

            if (selectedKeys.length <= 0) {
                return new Forward("selectItem");
            }
        }

        return null;
    }
}
