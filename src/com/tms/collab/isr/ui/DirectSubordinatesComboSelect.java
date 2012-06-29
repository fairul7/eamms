package com.tms.collab.isr.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import kacang.Application;
import kacang.services.security.User;
import kacang.stdui.ComboSelectBox;
import kacang.util.Log;

import org.apache.commons.collections.SequencedHashMap;

import com.tms.collab.isr.permission.model.ISRGroup;
import com.tms.collab.isr.permission.model.PermissionModel;
import com.tms.hr.orgChart.model.DepartmentCountryAssociativityObject;
import com.tms.hr.orgChart.model.OrgChartHandler;

public class DirectSubordinatesComboSelect extends ComboSelectBox {
	public DirectSubordinatesComboSelect()
    {
        super();
    }

    public DirectSubordinatesComboSelect(String name)
    {
        super(name);
    }

    public void init()
    {
        super.init();
        Map map = getUserList();
        
        if(!(map.isEmpty()))
            setLeftValues(map);
    }

    private Map getUserList()
    {
        Map map = new SequencedHashMap();
        Collection list = new ArrayList();
        try
        {
        	Application application = Application.getInstance();
        	String userId = application.getCurrentUser().getId();
        	OrgChartHandler orgChartHandler = (OrgChartHandler) application.getModule(OrgChartHandler.class);
        	DepartmentCountryAssociativityObject associatedCountryDept = orgChartHandler.getAssociatedCountryDept(userId);
        	
        	PermissionModel permissionModel = (PermissionModel) application.getModule(PermissionModel.class);
        	if(permissionModel.hasISRRole(userId, ISRGroup.ROLE_DEPT_ADMIN)) {
        		list = permissionModel.getDeptUsers(associatedCountryDept.getDeptCode(), associatedCountryDept.getCountryCode());
        	}
        	else if(permissionModel.hasISRRole(userId, ISRGroup.ROLE_SECTION_ADMIN)) {
        		list = permissionModel.getDeptUsersWithRole(associatedCountryDept.getDeptCode(), associatedCountryDept.getCountryCode(), ISRGroup.ROLE_STAFF);
        	}
            
            User user;
            for(Iterator i = list.iterator(); i.hasNext();)
            {
            	user = (User) i.next();
                map.put(user.getId(), user.getName());
            }
        }
        catch(Exception error)
        {
        	Log.getLog(getClass()).error(error, error);
        }
        return map;
    }
}
