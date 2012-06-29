package com.tms.crm.sales.ui;

import com.tms.crm.sales.misc.AccessUtil;
import com.tms.crm.sales.model.Opportunity;
import com.tms.crm.sales.model.OpportunityModule;
import kacang.Application;
import kacang.services.security.ui.UsersSelectBox;
import kacang.stdui.*;
import kacang.ui.Event;
import kacang.util.Log;
import org.apache.commons.collections.SequencedHashMap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

public class ReportsUsersSelectBox extends UsersSelectBox {

	private SelectBox viewAll;
	public static final String ACTIVE_USERS = "1";
	public static final String INACTIVE_USERS = "2";
	public static final String DELETED_USERS = "3";
	public static final String ALL_USERS = "4";

    public ReportsUsersSelectBox() {
        super();
    }

    public ReportsUsersSelectBox(String name) {
        super(name);
    }

    protected Table initPopupTable() {
        return new SalesUserTable();
    }

    protected Map generateOptionMap(String[] ids) {
        Map usersMap = new SequencedHashMap();
        if (ids == null || ids.length == 0) {
            return usersMap;
        }

		OpportunityModule om = (OpportunityModule) Application.getInstance().getModule(OpportunityModule.class);
		Map tempMap = new SequencedHashMap();
		for (int i=0 ; i<ids.length ; i++)	{
		Opportunity am = om.getAccountManager(ids[i]);
			if (am !=null ) {
			tempMap.put(am.getId(),am.getFirstName()+" "+am.getLastName());
			} else if (am == null) {
				am = om.getDeletedUsername(ids[i]);
				tempMap.put(ids[i],am.getUsername());
			}
		}

		// sort
		for (int j=0; j<ids.length; j++) {
			String name = (String) tempMap.get(ids[j]);
			if (name == null) {
				name = "---";
			}
			usersMap.put(ids[j], name);
		}

		return usersMap;
    }

    public class SalesUserTable extends PopupSelectBoxTable {
        public SalesUserTable() {
            super();
        }

	public SalesUserTable(String name) {
		super(name);
	}

	public void init() {
		setWidth("100%");
		setModel(new SalesUserTableModel());
	}

	public void onRequest(Event event) {
		viewAll.setSelectedOption("1");
	}

	public class SalesUserTableModel extends PopupSelectBoxTableModel {
		int count = 0;

	public SalesUserTableModel() {
		Application application = Application.getInstance();
		//Adding Columns
		TableColumn nameColumn = new TableColumn("username", application.getMessage("security.label.username", "Username"));
		nameColumn.setUrlParam("id");
		addColumn(nameColumn);
		addColumn(new TableColumn("firstName", application.getMessage("security.label.firstName", "First Name")));
		addColumn(new TableColumn("lastName", application.getMessage("security.label.lastName", "Last Name")));

		//Adding Actions
		addAction(new TableAction(FORWARD_SELECT,  application.getMessage("general.label.select", "Select")));

		//Adding Filters
		TableFilter userNameFilter = new TableFilter("query");

		TableFilter viewAllUser = new TableFilter("allUser");
		viewAll = new SelectBox("viewAll");
		viewAll.addOption(ACTIVE_USERS,"Active Users");
		viewAll.addOption(INACTIVE_USERS,"Inactive Users");
		viewAll.addOption(DELETED_USERS,"Deleted Users");
		viewAll.addOption(ALL_USERS,"All Users");
		viewAll.setSelectedOption("1");
		viewAllUser.setWidget(viewAll);
		addFilter(userNameFilter);
		addFilter(viewAllUser);

	}

	public Collection getTableRows() {
		Collection list = new ArrayList();
		String userType = getSbValue(viewAll);
		String query =  (String) getFilterValue("query");	// getting filter values

		count = 0 ;
		OpportunityModule listUser = (OpportunityModule) Application.getInstance().getModule(OpportunityModule.class);
		try {
			list = listUser.getUsers(userType, query, getSort(), isDesc());
			String userId, firstName;
			for (Iterator i = list.iterator(); i.hasNext();) {
				Opportunity opp =  (Opportunity) i.next();
				userId = opp.getUserId();
				firstName = opp.getFirstName();
				if ("1".equals(userType) || "2".equals(userType))	{
				if(!(AccessUtil.isExternalSalesPerson(userId)||AccessUtil.isSalesManager(userId)||AccessUtil.isSalesPerson(userId))){
					i.remove();
					}
				} else if ("3".equals(userType))	{
				if(!(AccessUtil.isExternalSalesPerson(userId)||AccessUtil.isSalesManager(userId)||AccessUtil.isSalesPerson(userId))){

				}
				} else if ("4".equals(userType)) {
					if (firstName != null)	{
						if(!(AccessUtil.isExternalSalesPerson(userId)||AccessUtil.isSalesManager(userId)||AccessUtil.isSalesPerson(userId))){
							i.remove();
						}
					}
				}
			}

			count = list.size();
			int sStart = getStart();
            int sEnd = sStart + getRows();
            if (sStart < 0) {
				sStart = 0;
            } else if (sStart > list.size()) {
				sStart = list.size()-1;
            }
			if (sEnd > list.size()) {
				sEnd = list.size();
           } else if (sEnd <= sStart) {
			   sEnd = sStart + 1;
		   }
           list = new ArrayList(list).subList(sStart, sEnd);

		} catch (Exception e)	{
			Log.getLog(UsersPopupTable.class).error("Error retrieving calendar users", e);
		}
		return list;
	}

            public int getTotalRowCount() {
				return count;
            }
            public String getTableRowKey() {
                return "userId";
            }
        }
    }


	public static String getSbValue(SelectBox sb) {
		if (sb != null) {
			Map selected = sb.getSelectedOptions();
			if (selected.size() == 1) {
				return (String) selected.keySet().iterator().next();
			}
		}
	return null;
	}
	
}
