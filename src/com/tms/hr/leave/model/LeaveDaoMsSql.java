package com.tms.hr.leave.model;

import java.util.Collection;

import kacang.model.DaoException;
import kacang.model.DataObjectNotFoundException;

public class LeaveDaoMsSql extends LeaveDao{
	
	public Collection getLeaveDistinct(String [] users, String startDate, String endDate) throws DaoException, DataObjectNotFoundException {

        //add 00:00:00 and 23:59:59
        startDate += " 00:00:00";
        endDate +=" 23:59:59";

        String getLeave ="select distinct l.id, l.leaveType, l.leaveTypeId, l.startDate, l.endDate, l.userId, l.applicantId, l.status, l.applicationDate " +
        		"from hr_leave_entry l, "+
                "security_user su where (l.status LIKE 'Approved' OR "+
                "l.status LIKE 'CancelSubmitted' OR "+
                "l.status LIKE 'CancelRejected') AND "+
                "( (l.startDate <= '"+startDate+"' AND "+
                "l.endDate >= '"+startDate+"' AND "+
                "l.endDate <= '"+endDate+"' ) OR "+
                "(l.startDate >= '"+startDate+"' AND "+
                "l.endDate <= '"+endDate+"' ) OR "+
                "(l.startDate <= '"+endDate+"' AND "+
                "l.endDate >= '"+endDate+"' AND "+
                "l.startDate >= '"+startDate+"' ) OR "+
                "(l.startDate <= '"+startDate+"' AND "+
                "l.startDate <= '"+endDate+"'  AND "+
                "l.endDate >= '"+startDate+"' AND "+
                "l.endDate >= '"+endDate+"' )) AND ( ";

        String addUser ="";
        if(users.length >0)
        {
            for(int i=0 ; i< users.length ; i++)
            {addUser +="l.userId='"+users[i]+"'";
                //   addUser +="AND l.applicantId = su.id";

                if(i <= (users.length-2))
                    addUser +=" OR ";
            }
        }
        //String getLeave2 =") GROUP BY l.startDate ";
        String getLeave2 =") AND l.days < 0 ORDER BY l.startDate, l.endDate ";
        return super.select(getLeave+addUser+getLeave2,LeaveEntry.class,null,0,-1);
    }

}
