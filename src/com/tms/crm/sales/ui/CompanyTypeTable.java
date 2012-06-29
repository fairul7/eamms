package com.tms.crm.sales.ui;

import kacang.stdui.*;
import kacang.Application;
import kacang.ui.Forward;
import kacang.ui.Event;
import kacang.model.DaoException;
import kacang.util.Log;

import java.util.Collection;

import com.tms.crm.sales.model.CompanyModule;
import com.tms.crm.sales.model.CompanyTypeModule;
import com.tms.crm.sales.model.CompanyTypeException;

/**
 * Created by IntelliJ IDEA.
 * User: Disen
 * Date: Jul 1, 2004
 * Time: 5:28:24 PM
 * To change this template use File | Settings | File Templates.
 */
public class CompanyTypeTable extends Table{

    public CompanyTypeTable() {
    }

    public CompanyTypeTable(String name) {
        super(name);
    }

    public void init() {
        setNumbering(true);
        setModel(new CompanyTypeTableModel());
    }


    public class CompanyTypeTableModel extends TableModel{

        public CompanyTypeTableModel() {
            TableColumn type = new TableColumn("type",Application.getInstance().getMessage("sfa.label.companyType","Company Type"));
            type.setUrlParam("id");
            addColumn(type);
            TableColumn tc_isArchived = new TableColumn("archived", Application.getInstance().getMessage("sfa.label.archived","Archived"));
            tc_isArchived.setFormat(new TableBooleanFormat(Application.getInstance().getMessage("sfa.label.yes","Yes"),Application.getInstance().getMessage("sfa.label.no","No")));       
            addColumn(tc_isArchived);

            addAction(new TableAction("delete",Application.getInstance().getMessage("sfa.message.delete","Delete"),Application.getInstance().getMessage("sfa.message.deleteMessage","Are you sure you want to delete?")));


        }

        public Collection getTableRows() {
            CompanyTypeModule ctm = (CompanyTypeModule) Application.getInstance().getModule(CompanyTypeModule.class);
            try {
                return ctm.listCompanyTypes(getSort(),isDesc(),getStartIndex(),getRows());
            } catch (CompanyTypeException e) {
                Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
            }
            return null;
        }

        public int getTotalRowCount() {
            CompanyTypeModule ctm = (CompanyTypeModule) Application.getInstance().getModule(CompanyTypeModule.class);
            try {
                return ctm.countCompanyTypes();
            } catch (DaoException e) {
                Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
            }
            return 0;
        }

        public String getTableRowKey() {
            return "id";
        }

        public Forward processAction(Event evt, String action, String[] selectedKeys) {
            if("delete".equals(action)){
                CompanyTypeModule ctm = (CompanyTypeModule) Application.getInstance().getModule(CompanyTypeModule.class);
                CompanyModule cm = (CompanyModule) Application.getInstance().getModule(CompanyModule.class);
                boolean type=false;
                for (int i = 0; i < selectedKeys.length; i++) {
                	int count=cm.countCompanyByCompanyType(selectedKeys[i]);
                	if(count>0){
                		type=true;
                	}
                }
               if(!type){
                for (int i = 0; i < selectedKeys.length; i++) {
                    String selectedKey = selectedKeys[i];
                    try {
                        ctm.deleteCompanyType(selectedKey);
                    } catch (CompanyTypeException e) {
                        Log.getLog(getClass()).error(e.getMessage(), e);  //To change body of catch statement use Options | File Templates.
                    }
                }if(selectedKeys.length>0)
                return new Forward("delete");
               }
               else{
            	   return new Forward("notdelete");    
               }
            }
            return super.processAction(evt, action, selectedKeys);    //To change body of overridden methods use File | Settings | File Templates.
        }

    }

}
