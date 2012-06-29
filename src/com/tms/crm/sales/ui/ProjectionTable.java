package com.tms.crm.sales.ui;

import kacang.stdui.*;
import kacang.Application;
import kacang.model.DaoQuery;
import kacang.model.DefaultDataObject;
import kacang.model.operator.OperatorParenthesis;
import kacang.model.operator.DaoOperator;
import kacang.model.operator.OperatorLike;
import kacang.model.operator.OperatorEquals;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;
import kacang.services.security.ui.UsersSelectBox;

import java.util.Collection;
import java.util.ArrayList;
import java.util.Iterator;

import com.tms.crm.sales.misc.DateUtil;
import com.tms.crm.sales.misc.AccessUtil;
import com.tms.crm.sales.model.OPPModule;
import com.tms.crm.sales.model.OpportunityProductArchiveModule;
import com.tms.crm.sales.model.OpportunityProductModule;
import com.tms.crm.sales.model.ProductModule;

public class ProjectionTable extends Table{
    public ProjectionTable() {
    }

    public ProjectionTable(String name) {
        super(name);
    }

    public void init() {
        super.init();    //To change body of overridden methods use File | Settings | File Templates.
        setModel(new ProjectionTableModel());

    }


    public class ProjectionTableModel extends TableModel{
        ProjectionTableFormat format = new ProjectionTableFormat();
        private SelectBox sel_Year;
        int count = 0;

        public ProjectionTableModel() {
            TableColumn firstName = new TableColumn("firstName",Application.getInstance().getMessage("sfa.label.firstName","FirstName"));

            /*firstName.setUrlParam("id");*/

            addColumn(firstName);
            addColumn(new TableColumn("lastName",Application.getInstance().getMessage("sfa.label.lastName","Last Name")));
            TableColumn projectionColumn = new TableColumn("projection",Application.getInstance().getMessage("sfa.label.projection","Projection"));
            projectionColumn.setFormat(format);
            addColumn(projectionColumn);
            sel_Year = new SelectBox("sel_Year");
            int currYear = DateUtil.getYear(DateUtil.getDate());
            for (int i=-1; i<2; i++) {
                int year = currYear + i;
                sel_Year.addOption(String.valueOf(year), String.valueOf(year));
            }
            sel_Year.setSelectedOption(String.valueOf(currYear));
            TableFilter yearFilter = new TableFilter("year");
            yearFilter.setWidget(sel_Year);
            Label yearLabel = new Label("year",Application.getInstance().getMessage("sfa.label.year","Year"));
            TableFilter yearLabelFilter = new TableFilter("yearlabel");
            yearLabelFilter.setWidget(yearLabel);
            addFilter(yearLabelFilter);
            addFilter(yearFilter);
            addAction(new TableAction("delete",Application.getInstance().getMessage("sfa.message.delete","Delete"),Application.getInstance().getMessage("sfa.message.deleteMessage","Are you sure you want to delete?")));
        }

        public Collection getTableRows() {
            String key = (String) sel_Year.getSelectedOptions().keySet().iterator().next();
            //String year =sel_Year.getSelectedOptions().get(key).toString();
            format.setYear(Integer.parseInt(key));
            Collection list = new ArrayList();
            //SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
            OPPModule om = (OPPModule) Application.getInstance().getModule(OPPModule.class);
            try {
                list = om.listProjections(generateDaoProperties(), getSort(), isDesc(), 0, -1);
                String userId;
                for (Iterator i = list.iterator(); i.hasNext();) {
                    DefaultDataObject user = (DefaultDataObject) i.next();
                    userId = user.getId();
                    if(!(AccessUtil.isDashboardUser(userId)||AccessUtil.isExternalSalesPerson(userId)||AccessUtil.isSalesManager(userId)||AccessUtil.isSalesPerson(userId))){
                        i.remove();
                    }
                }
                count = list.size();

                // get selected page
                int sStart = getStart();
                int sEnd = sStart + getRows();
                if (sStart < 0) {
                    sStart = 0;
                }
                else if (sStart > list.size()) {
                    sStart = list.size()-1;
                }
                if (sEnd > list.size()) {
                    sEnd = list.size();
                }
                else if (sEnd <= sStart) {
                    sEnd = sStart + 1;
                }
                list = new ArrayList(list).subList(sStart, sEnd);
            }
            catch (Exception e) {
                Log.getLog(UsersSelectBox.UsersPopupTable.class).error("Error retrieving calendar users", e);
            }
            return list;
        }

        public String getTableRowKey()
        {
            return "projectionID";
        }
        public int getTotalRowCount() {
            return count;  //To change body of implemented methods use File | Settings | File Templates.
        }

        protected DaoQuery generateDaoProperties() {
            DaoQuery properties = new DaoQuery();
            OperatorParenthesis op1 = new OperatorParenthesis(DaoOperator.OPERATOR_AND);
			Integer year = new Integer(sel_Year.getSelectedOptions().keySet().iterator().next().toString());
            op1.addOperator(new OperatorEquals("year", year, null));
            OperatorParenthesis op = new OperatorParenthesis(DaoOperator.OPERATOR_AND);
            op.addOperator(new OperatorLike("username", getFilterValue("query"), null));
            op.addOperator(new OperatorLike("firstName", getFilterValue("query"), DaoOperator.OPERATOR_OR));
            op.addOperator(new OperatorLike("lastName", getFilterValue("query"), DaoOperator.OPERATOR_OR));
            properties.addProperty(op);
            properties.addProperty(op1);
            properties.addProperty(new OperatorEquals("active", "1", DaoOperator.OPERATOR_AND));
            return properties;
        }

        public Forward processAction(Event evt, String action, String[] selectedKeys) {
            if("delete".equals(action)){
            	OPPModule om = (OPPModule) Application.getInstance().getModule(OPPModule.class);

                for (int i = 0; i < selectedKeys.length; i++) {
                    String selectedKey = selectedKeys[i];
                    om.deleteProjection(selectedKey);
                }
                if(selectedKeys.length>0)
                return new Forward("delete");
               

            }
            return super.processAction(evt, action, selectedKeys);    //To change body of overridden methods use File | Settings | File Templates.
        }
    }

}
