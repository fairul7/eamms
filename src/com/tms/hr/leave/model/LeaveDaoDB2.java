package com.tms.hr.leave.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import kacang.model.DaoException;
import kacang.model.DataObjectNotFoundException;

public class LeaveDaoDB2 extends LeaveDao{
	
	public Collection getLeave(String [] users, String startDate, String endDate) throws DaoException, DataObjectNotFoundException {

		startDate += " 00:00:00";
        endDate +=" 23:59:59";

        String getLeave ="select distinct l.id, l.leaveType, l.leaveTypeId, l.startDate, l.endDate, l.credit, l.adjustment, l.days, l.halfday, l.status, l.userId, l.reason, l.applicantId, l.applicationDate, l.approvalDate, l.approvalUserId, l.approvalcomments, l.eventId, l.lastModifiedDate, l.lastModifiedUserId from hr_leave_entry l,security_user su where (l.status LIKE 'Approved' OR l.status LIKE 'CancelSubmitted' OR l.status LIKE 'CancelRejected') AND ( (l.startDate <= '"+startDate+"' AND  l.endDAte >= '"+startDate+"' AND l.endDate <= '"+endDate+"') OR (l.startDate >= '"+startDate+"' AND l.endDate <= '"+endDate+"' ) OR (l.startDate <= '"+endDate+"' AND l.endDate >= '"+endDate+"' AND l.startDate >= '"+startDate+"' ) OR (l.startDate <= '"+startDate+"' AND l.startDate <= '"+endDate+"'  AND l.endDate >= '"+startDate+"' AND l.endDate >= '"+endDate+"'  )) AND ( ";
        String addUser ="";
        if(users.length >0)
        {
            for(int i=0 ; i< users.length ; i++)
            {
            	addUser +="l.userId='"+users[i]+"'";
            	if(i <= (users.length-2))
            		addUser +=" OR ";
            }
        }
        
        String getLeave2 =") AND l.days < 0 ORDER BY l.startDate, l.endDate ";
        return super.select(getLeave+addUser+getLeave2,LeaveEntry.class,null,0,-1);
    }
}
