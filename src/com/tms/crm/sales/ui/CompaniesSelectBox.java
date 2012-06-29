package com.tms.crm.sales.ui;

import com.tms.crm.sales.model.Opportunity;
import com.tms.crm.sales.model.OpportunityModule;
import com.tms.crm.sales.model.OpportunityDao;
import kacang.Application;
import kacang.model.DaoQuery;
import kacang.model.DaoException;
import kacang.model.operator.DaoOperator;
import kacang.model.operator.OperatorIn;
import kacang.services.security.SecurityException;
import kacang.stdui.*;
import kacang.ui.Event;
import kacang.util.Log;
import org.apache.commons.collections.SequencedHashMap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

public class CompaniesSelectBox extends PopupSelectBox {
	private int total = 0;

    public CompaniesSelectBox() {
        super();
    }

    public CompaniesSelectBox(String name) {
        super(name);
    }

    protected Table initPopupTable() {
        return new CompanyPopupTable();
    }

    protected Map generateOptionMap(String[] ids) {
        Map usersMap = new SequencedHashMap();
        if (ids == null || ids.length == 0) {
            return usersMap;
        }

        try {
            DaoQuery query = new DaoQuery();
            query.addProperty(new OperatorIn("id", ids, DaoOperator.OPERATOR_AND));

			Collection list = new ArrayList();
			OpportunityModule companyHandler = (OpportunityModule) Application.getInstance().getModule(OpportunityModule.class);
			list =  companyHandler.listCompanies(null, null, false, 0, -1);

			// build users map
            Map tmpMap = new SequencedHashMap();
            for (Iterator i=list.iterator(); i.hasNext();) {
                Opportunity opp = (Opportunity) i.next();
                tmpMap.put(opp.getCompanyID(), opp.getCompanyNameStr());
            }

            // sort
            for (int j=0; j<ids.length; j++) {
                String name = (String)tmpMap.get(ids[j]);
                if (name == null) {
                    name = "---";
                }
                usersMap.put(ids[j], name);
            }
        }
        catch (SecurityException e) {
            Log.getLog(getClass()).error("Error retrieving companies", e);
        } catch (Exception e) {
			Log.getLog(getClass()).error(e.toString(), e);
		}
        return usersMap;
    }

    public class CompanyPopupTable extends PopupSelectBoxTable {

        public CompanyPopupTable() {
        }

        public CompanyPopupTable(String name) {
            super(name);
        }

        public void init() {
            super.init();
            setWidth("100%");
            setModel(new CompanyPopupTable.UserTableModel());
        }

        public void onRequest(Event evt) {
            init();
        }

        public class UserTableModel extends PopupSelectBoxTableModel {
            public UserTableModel() {
                super();

                // Adding Table Colums
                Application application = Application.getInstance();
                addColumn(new TableColumn("companyNameStr", application.getMessage("sfa.message.companyName", "Company Name")));
				TableFilter compnayNameFilter = new TableFilter("compnayNameFilter");

                //Adding Actions
                addAction(new TableAction(FORWARD_SELECT,  application.getMessage("general.label.select", "Select")));
				addFilter(compnayNameFilter);
            }

            public Collection getTableRows() {
				String search = (String) getFilterValue("compnayNameFilter");
				Collection list = new ArrayList();
				OpportunityModule companyHandler = (OpportunityModule) Application.getInstance().getModule(OpportunityModule.class);
				try {
					list =  companyHandler.listCompanies(search, getSort(), isDesc(), getStart(), getRows());
				} catch (Exception e) {
					Log.getLog(getClass()).error(e.toString(), e);
				}
				return list;
            }

            public int getTotalRowCount() {
				String search = (String) getFilterValue("compnayNameFilter");
				OpportunityModule listHandler = (OpportunityModule) Application.getInstance().getModule(OpportunityModule.class);
				OpportunityDao companiesListHandler =  (OpportunityDao) listHandler.getDao();
				try {
					total = companiesListHandler.getCompaniesList(search,null);
					return total;
				} catch (DaoException e) {
					Log.getLog(getClass()).error(e.toString(), e);
				}
				return 0;
            }
            public String getTableRowKey() {
                return "companyID";
            }

        }
    }

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

}
