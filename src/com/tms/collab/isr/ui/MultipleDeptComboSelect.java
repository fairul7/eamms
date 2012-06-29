package com.tms.collab.isr.ui;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import kacang.Application;
import kacang.stdui.ComboSelectBox;
import kacang.util.Log;

import org.apache.commons.collections.SequencedHashMap;

import com.tms.hr.orgChart.model.DepartmentCountryAssociativityObject;
import com.tms.hr.orgChart.model.OrgChartHandler;

public class MultipleDeptComboSelect extends ComboSelectBox {
	public MultipleDeptComboSelect() {
		super();
	}
	
	public MultipleDeptComboSelect(String name) {
		super(name);
	}
	
	public void init()
    {
        super.init();
        Map map = getDeptList();
        
        if(!(map.isEmpty()))
            setLeftValues(map);
    }
	
	private Map getDeptList()
    {
        Map map = new SequencedHashMap();
        try
        {
            Application application = Application.getInstance();
        	OrgChartHandler orgChartModel = (OrgChartHandler) application.getModule(OrgChartHandler.class);
        	
        	Collection deptCols = orgChartModel.selectDepartmentCountryAssociativity(null, null, null, "countryDesc, deptDesc", false, 0, -1);
        	DepartmentCountryAssociativityObject requesterDept = orgChartModel.getAssociatedCountryDept(application.getCurrentUser().getId());
        	
        	for(Iterator i=deptCols.iterator(); i.hasNext();) {
        		DepartmentCountryAssociativityObject obj = (DepartmentCountryAssociativityObject) i.next();
        		if(! obj.getAssociativityId().equals(requesterDept.getAssociativityId())) {
        			map.put(obj.getAssociativityId(), 
        					obj.getCountryDesc() + " - " + obj.getDeptDesc());
        		}
        	}
        }
        catch(Exception error)
        {
        	Log.getLog(getClass()).error(error, error);
        }
        return map;
    }
}
