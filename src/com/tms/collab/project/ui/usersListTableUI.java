package com.tms.collab.project.ui;

import com.tms.ekms.security.ui.LeaveUserSelectBox;
import com.tms.hr.leave.model.LeaveEntry;
import com.tms.hr.leave.model.LeaveException;
import com.tms.hr.leave.model.LeaveModule;

import kacang.Application;

import kacang.model.DaoException;
import kacang.model.DataObjectNotFoundException;

import kacang.services.security.SecurityException;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.services.security.ui.UsersSelectBox;

import kacang.stdui.Button;
import kacang.stdui.DateField;
import kacang.stdui.Form;

import kacang.ui.Event;
import kacang.ui.Forward;

import org.apache.commons.collections.SequencedHashMap;

import java.text.SimpleDateFormat;

import java.util.*;


public class usersListTableUI extends Form {

    public static final String FORWARD_REPORT_GENERATED = "com.tms.worms.mole.ReportGenerated";
    public static final String FORWARD_CANCEL = "com.tms.worms.mole.Cancel";
    public static final String DEFAULT_TEMPLATE = "usersListTableUI/usersListTableUI";
    protected LeaveUserSelectBox users;
    protected DateField startDate;
    protected DateField endDate;
    protected Button submit;
    protected Button reset;
    protected Button cancel;
    protected Map report;
    protected Map timeRange;
    
 

    public void init() {
        super.init();
        setMethod("POST");
        users = new LeaveUserSelectBox("users");
        startDate = new DateField("startDate");
        endDate = new DateField("endDate");
        submit = new Button("submit",
                Application.getInstance().getMessage("project.label.generateReport",
                        "Generate Report"));
        reset = new Button("reset",
                Application.getInstance().getMessage("general.label.reset",
                        "Reset"));
        cancel = new Button("cancel",
                Application.getInstance().getMessage("general.label.cancel",
                        "Cancel"));

        addChild(users);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH,-1);
        
        startDate.setDate(calendar.getTime());
        addChild(startDate);
        addChild(endDate);
        addChild(submit);
        addChild(reset);
        addChild(cancel);

        users.init();
        report = new SequencedHashMap();
    }

    public Forward onSubmit(Event event) {
        Forward forward = null;
        String button = findButtonClicked(event);

        if (cancel.getAbsoluteName().equals(button)) {
            forward = new Forward(FORWARD_CANCEL);
        } else if (reset.getAbsoluteName().equals(button)) {
        	forward = super.onSubmit(event);
            
        	users.setIds(new String[]{});
        	
        } else {
            forward = super.onSubmit(event);

            if (users.getIds().length <= 0) {
                setInvalid(true);
                users.setInvalid(true);
            }

            if (startDate.getDate().after(endDate.getDate())) {
                setInvalid(true);
                startDate.setInvalid(true);
                endDate.setInvalid(true);
            }
        }
        return forward;
    }

    public Forward onValidate(Event event) {
        return generateReport();
    }

    public void onRequest(Event event) {
        if (!isInvalid()) {
            generateReport();
        }
        Application app = Application.getInstance();
        LeaveModule handler = (LeaveModule) app.getInstance().getModule(LeaveModule.class);
        
    	SecurityService service = (SecurityService) app.getService(SecurityService.class);
        User user = service.getCurrentUser(event.getRequest());
    	String userId = user.getId();

				users.setUserId(userId);	
			
    }

    protected Forward generateReport() {
        report = new SequencedHashMap();
        timeRange = new SequencedHashMap();

        if (users.getIds().length > 0) {
            Map matrix = new SequencedHashMap();
            String[] ids = users.getIds();
            SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);

            LeaveModule lm = (LeaveModule) Application.getInstance().getModule(LeaveModule.class);

            //Collection leaves = lm.viewLeaveSummaryList(leaveType, userIdArray, year, user);
            //  TaskManager handler = (TaskManager) Application.getInstance().getModule(TaskManager.class);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
            Collection involved = null;

            
        
            
            
            try {
                involved = lm.getLeaveDistinct(ids, sdf.format(startDate.getDate()),sdf.format(endDate.getDate()));
                
                timeRange.clear();
                matrix.clear();
                report.clear();

                String bufferUsername = "";

                for (Iterator iterator = involved.iterator();
                     iterator.hasNext();) {
                    LeaveEntry temp = (LeaveEntry) iterator.next();

                    Map tempMatrix = (Map) timeRange.get(temp.getUser().getUsername());

                    if (tempMatrix == null) {
                        tempMatrix = new SequencedHashMap();
                    }
                    //temp is element from database

/*                   if(startDate.getDate().after(temp.getStartDate()))
                   {
                       temp.setStartDate(startDate.getDate());
                   }
                   if(endDate.getDate().before(temp.getEndDate())){

                       temp.setEndDate(endDate.getDate());
                   }*/
                    
          
                    
                    tempMatrix.put( temp.getStartDate(), temp.getEndDate()); System.out.println("start="+ temp.getStartDate()+" endDate="+temp.getEndDate());

                    //   matrix.put(temp.getStartDate(), temp.getEndDate());
                    timeRange.put(temp.getUser().getUsername(), tempMatrix);
                    report.put(temp.getUser().getUsername(), temp);
                }
            } catch (DaoException e) {
                e.printStackTrace();
            } catch (DataObjectNotFoundException e) {
                e.printStackTrace();
            }
        }
        return new Forward(FORWARD_REPORT_GENERATED);
    }

    public String getDefaultTemplate() {
        return DEFAULT_TEMPLATE;
    }

    protected boolean withinRange(Date date, Date rangeStart, Date rangeEnd) {
        if ((date.after(rangeStart) || date.equals(rangeStart)) &&
                (date.before(rangeEnd) || date.equals(rangeEnd))) {
            return true;
        }
        return false;
    }

    protected boolean remove(Date startDate, Date endDate, Date compareStart,
                             Date compareEnd) {
        if ((startDate.before(compareStart) || startDate.equals(compareStart))) {
            return true;
        }
        return false;
    }

    /* Getters and Setters */
    public Button getCancel() {
        return cancel;
    }

    public void setCancel(Button cancel) {
        this.cancel = cancel;
    }

    public DateField getEndDate() {
        return endDate;
    }

    public void setEndDate(DateField endDate) {
        this.endDate = endDate;
    }

    public Button getReset() {
        return reset;
    }

    public void setReset(Button reset) {
        this.reset = reset;
    }

    public DateField getStartDate() {
        return startDate;
    }

    public void setStartDate(DateField startDate) {
        this.startDate = startDate;
    }

    public Button getSubmit() {
        return submit;
    }

    public void setSubmit(Button submit) {
        this.submit = submit;
    }

    public UsersSelectBox getUsers() {
        return users;
    }

    public void setUsers(LeaveUserSelectBox users) {
        this.users = users;
    }

    public Map getReport() {
        return report;
    }

    public void setReport(Map report) {
        this.report = report;
    }

    public Map getTimeRange() {
        return timeRange;
    }

    public void setTimeRange(Map timeRange) {
        this.timeRange = timeRange;
    }



    
}
