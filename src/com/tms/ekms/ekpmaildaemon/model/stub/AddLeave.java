package com.tms.ekms.ekpmaildaemon.model.stub;

import com.tms.hr.leave.model.BalanceException;
import com.tms.hr.leave.model.LeaveException;
import com.tms.hr.leave.model.LeaveModule;
import com.tms.hr.leave.model.LeaveType;

import kacang.Application;

import kacang.services.security.SecurityException;
import kacang.services.security.SecurityService;
import kacang.services.security.User;

import kacang.util.Log;

import java.util.*;
import java.text.SimpleDateFormat;
import java.text.ParseException;


public class AddLeave implements MailStub {
    private String leaveTypeId;
    private Date startDate;
    private Date endDate;
    private boolean halfDay;
    private String userId;
    private String reason;
    private User user;
    LeaveModule module = (LeaveModule) Application.getInstance().getModule(LeaveModule.class);

    public String getLeaveTypeId() {
        return leaveTypeId;
    }

    public void setLeaveTypeId(String leaveTypeId) {
        this.leaveTypeId = leaveTypeId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public boolean isHalfDay() {
        return halfDay;
    }

    public void setHalfDay(boolean halfDay) {
        this.halfDay = halfDay;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean getPattern() //return subject pattern
     {
        return false;
    }

    public Map processMail(String emailAddress, Map contentMap) //pass to mailmodule
     {
        Application app = Application.getInstance();
        String errorMsg = "";

        contentMap.put("EMAIL", emailAddress);

        try {
            SecurityService ss = (SecurityService) Application.getInstance()
                                                              .getService(SecurityService.class);

            user = new User();
            user = ss.getUser(contentMap.get("USERID").toString());

            String startDate = contentMap.get(app.getMessage(
                        "maildaemon.stub.addleave.label.startdate")).toString();
            String endDate = contentMap.get(app.getMessage(
                        "maildaemon.stub.addleave.label.enddate")).toString();

        /*    //do filtering of date from string into int
            int[] timeStart = { 0, 0, 0, 0, 0 };
            int[] timeEnd = { 0, 0, 0, 0, 0 };
            int count = 0;

            StringTokenizer st = new StringTokenizer(startDate, " .-,/\\:");

            while (st.hasMoreTokens()) {
                try {
                    timeStart[count] = Integer.parseInt(st.nextToken());
                } catch (NumberFormatException e) {
                    timeStart[count] = 0;
                }

                count++;
            }

            count = 0;

            StringTokenizer st2 = new StringTokenizer(endDate, " .-,/\\:");

            while (st2.hasMoreTokens()) {
                try {
                    timeEnd[count] = Integer.parseInt(st2.nextToken());
                } catch (NumberFormatException e) {
                    timeEnd[count] = 0;
                }

                count++;
            }*/

            //force month +1 ,coz start count from 0
         /*   timeStart[1] -= 1;
            timeEnd[1] -= 1;

            Calendar calStart = Calendar.getInstance();
            calStart.set(timeStart[0], timeStart[1], timeStart[2],
                timeStart[3], timeStart[4]);

            Calendar calEnd = Calendar.getInstance();
            calEnd.set(timeEnd[0], timeEnd[1], timeEnd[2], timeEnd[3],
                timeEnd[4]);*/

            Calendar calStart = Calendar.getInstance();
            Calendar calEnd = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            sdf.setLenient(false);
                    try {

                        calStart.setTime(sdf.parse(startDate));


                        calEnd.setTime(sdf.parse(endDate));

                    } catch (ParseException e) {

                           errorMsg = "\n" + app.getMessage("maildaemon.label.error.invalidDate");

                    }






            module.applyLeave(contentMap.get(app.getMessage(
                        "maildaemon.stub.addleave.label.type")).toString()
                                        .replaceAll(" ", "").trim(),
                calStart.getTime(), calEnd.getTime(), false,
                contentMap.get("USERID").toString(),
                contentMap.get(app.getMessage(
                        "maildaemon.stub.addleave.label.reason")).toString(),
                user);

            //if success
        } catch (NumberFormatException g) {
            errorMsg += ("\n" +
            app.getMessage("maildaemon.stub.addleave.label.calendar.error"));
            Log.getLog(getClass()).info("invalid calender date <EKPMAILDAEMON>");
        } catch (BalanceException f) {
            errorMsg += ("\n" +
            app.getMessage("maildaemon.stub.addleave.label.insufficientbalance"));
            Log.getLog(getClass()).info("invalid BALANCE <EKPMAILDAEMON>");
        } catch (LeaveException e) {
            errorMsg += ("\n" +
            app.getMessage("maildaemon.stub.addleave.label.error.apply"));
            Log.getLog(getClass()).info("Error applying Leave <EKPMAILDAEMON>");
        } catch (Exception e) {
            errorMsg += ("\n" +
            app.getMessage("maildaemon.stub.addleave.label.error.apply"));
            Log.getLog(getClass()).info("Error applying Leave <EKPMAILDAEMON>");

            e.printStackTrace();
        }

        if ("".equals(errorMsg)) {
            errorMsg = "\n" + app.getMessage("maildaemon.label.successful");
            Log.getLog(getClass()).info("SUCCESSFUL ADDED LEAVE <EKPMAILDAEMON>");
        }

        contentMap.put("ERROR", (String) errorMsg);

        return contentMap;
    }

    public String getSubjectPattern() {
        Application app = Application.getInstance();

        return app.getMessage("maildaemon.stub.addleave.label.addleave");
    }

    public String[] getBodyPattern() {
        Application app = Application.getInstance();

        //USERNAME AND PASSWORD ARE "MUST"
        String[] tempPattern = {
            app.getMessage("maildaemon.stub.addleave.label.type"),
            app.getMessage("maildaemon.stub.addleave.label.startdate"),
            app.getMessage("maildaemon.stub.addleave.label.enddate"),
            app.getMessage("maildaemon.stub.addleave.label.reason")
        };

        return tempPattern;
    }

    public String getInfo() {
        //initilize some of of setting some of the custom OPTION . for instance, type = annual, emergency, unpaid...etc
        Application app = Application.getInstance();
        String customOption = "";

        SecurityService ss = (SecurityService) Application.getInstance()
                                                          .getService(SecurityService.class);
        Collection userCollection = null;

        try {
            userCollection = ss.getUsersByUsername((String) "anonymous");
        } catch (SecurityException e) {
            e.printStackTrace();
        }

        for (Iterator itc = userCollection.iterator(); itc.hasNext();) {
            user = (User) itc.next();
        }

        try {
            Collection leaveTypes = module.viewLeaveTypeList(user.getId(),
                    false, false);

            customOption += ("\n" +
            app.getMessage("maildaemon.stub.addleave.label.type") + ":\n");

            for (Iterator itc = leaveTypes.iterator(); itc.hasNext();) {
                LeaveType leavetype = (LeaveType) itc.next();
                customOption += (leavetype.getName().replaceAll("Leave", "")
                                          .trim());
                if(itc.hasNext()) customOption += ", ";

            }
        } catch (Exception e) {
            Log.getLog(getClass()).error("Error retrieving leave types", e);
        }

        return customOption;
    }


 public String getHeaderInfo(){



          return "";
    }


}
