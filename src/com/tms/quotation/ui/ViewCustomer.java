package com.tms.quotation.ui;

import com.tms.quotation.model.*;

import java.lang.reflect.Array;
import java.util.*;

import javax.swing.plaf.basic.BasicBorders.RadioButtonBorder;

import kacang.Application;
import kacang.model.DaoException;
import kacang.stdui.*;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;


public class ViewCustomer extends Table {

  public static String FORWARD_ADD="ViewCustomer.add";
  public static String FORWARD_DELETE="ViewCustomer.delete";
  public static String FORWARD_ACTIVE="ViewCustomer.active";
  public static String FORWARD_INACTIVE="ViewCustomer.inactive";
  
  private String canAdd = "0";
  private String canEdit = "0";
  private String canDelete = "0";
  private String canActivate = "0";

  public String getCanAdd() {return canAdd;}
  public void setCanAdd(String string) {canAdd = string;}
  public String getCanEdit() {return canEdit;}
  public void setCanEdit(String string) {canEdit = string;}
  public String getCanDelete() {return canDelete;}
  public void setCanDelete(String string) {canDelete = string;}
  public String getCanActivate() { return canActivate; }
  public void setCanActivate(String string) { canActivate=string; }

  public ViewCustomer() {}

  public ViewCustomer(String s) {super(s);}

  public void init() {
    super.init();
    setPageSize(10);
    setModel(new ViewCustomerModel());
    setWidth("100%");
  }

  public void onRequest(Event event) {
    //if ("0".equals(canEdit)) {setSortable(false);}
    setModel(new ViewCustomerModel());
  }

  class ViewCustomerModel extends TableModel {
    ViewCustomerModel() {
      removeChildren();
      Application app = Application.getInstance();
      
      if ("1".equals(canAdd)) {
        addAction(new TableAction("add",
            app.getMessage("com.tms.quotation.button.add")));
      }
      if( "1".equals(canDelete)) {
        addAction(new TableAction("delete",
            app.getMessage("com.tms.quotation.button.delete"),
            app.getMessage("com.tms.quotation.viewCustomer.alert.delete")));
      }
      if ("1".equals(canActivate)) {
        addAction(new TableAction("activate",
            app.getMessage("com.tms.quotation.button.activate")));
        addAction(new TableAction("deactivate", 
            app.getMessage("com.tms.quotation.button.deactivate"), 
            app.getMessage("com.tms.quotation.viewCustomer.alert.deactivate")));
      }
      
      //table columns
      TableColumn tc3 = new TableColumn("companyName", app.getMessage("com.tms.quotation.viewCustomer.company"));
      if("1".equals(canEdit)){tc3.setUrlParam("customerId");}
      addColumn(tc3);
      TableColumn tc1 = new TableColumn("contactFirstName", app.getMessage("com.tms.quotation.viewCustomer.firstName"));
//    if("1".equals(canEdit)){tc1.setUrlParam("customerId");}
      addColumn(tc1);
      TableColumn tc2 = new TableColumn("contactLastName", app.getMessage("com.tms.quotation.viewCustomer.lastName"));
      addColumn(tc2);
      
      TableColumn tc4 = new TableColumn("active", app.getMessage("com.tms.quotation.active"));
      HashMap map = new HashMap();
      map.put("0",app.getMessage("com.tms.quotation.inactive"));      
      map.put("1",app.getMessage("com.tms.quotation.active"));
      tc4.setFormat(new TableStringFormat(map));
      addColumn(tc4);
            
/*      TableColumn tc4 = new TableColumn("address1", "Address");
      addColumn(tc4);
      TableColumn tc5 = new TableColumn("address2", "Address 2");
      addColumn(tc5);
      TableColumn tc6 = new TableColumn("address3", "City");
      addColumn(tc6);
      TableColumn tc7 = new TableColumn("postcode", "Postcode");
      addColumn(tc7);
      TableColumn tc8 = new TableColumn("state", "State");
      addColumn(tc8);
      TableColumn tc9 = new TableColumn("country", "Country");
      addColumn(tc9);
      TableColumn tc10 = new TableColumn("gender", "Gender");
      addColumn(tc10);
      TableColumn tc11 = new TableColumn("salutation", "Salutation");
      addColumn(tc11);*/
      //setSort(" ");

      //table filters
      //search filter
//      TableFilter searchBy = new TableFilter("lbSearchBy");
//      Label search = new Label("search","  Search  ");
//      searchBy.setWidget(search);
//      addFilter(searchBy);

      TableFilter tfSearchBy = new TableFilter("searchBy");
      TextField tfSearchText = new TextField("tfSearchText");
      tfSearchText.setSize("20");
      tfSearchBy.setWidget(tfSearchText);
      addFilter(tfSearchBy);

//      QuotationModule MODULE = (QuotationModule)Application.getInstance().getModule(QuotationModule.class);
    }

    public Collection getTableRows() {
      String searchBy = "";
      searchBy = (String)getFilterValue("searchBy");

      QuotationDao dao = (QuotationDao)Application.getInstance().getModule(QuotationModule.class).getDao();
      try {
        return dao.selectCustomer(searchBy, getSort(), isDesc(), getStart(), getRows());
      } catch (DaoException e) {
        Log.getLog(getClass()).error(e.toString());
        return new ArrayList();
      }
    }

    public int getTotalRowCount() {
      String searchBy = "";
      searchBy = (String)getFilterValue("searchBy");

      QuotationDao dao = (QuotationDao)Application.getInstance().getModule(QuotationModule.class).getDao();
      try {
        return dao.selectCustomerCount(searchBy);
      } catch (DaoException e) {
        Log.getLog(getClass()).error(e.toString());
        return 0;
      }
    }

    public String getTableRowKey() {
      if ("1".equals(canDelete)) {return "customerId";}
      else {return "";}
    }

    public Forward processAction(Event event, String action, String[] selectedKeys) {
      QuotationModule module = (QuotationModule)Application.getInstance().getModule(QuotationModule.class);
      QuotationDao dao = (QuotationDao)module.getDao();
      Log log = Log.getLog(getClass());
      
      if ("add".equals(action))
        return new Forward(FORWARD_ADD);
      else if ("activate".equals(action)) {
	if (selectedKeys.length > 0) {
	  for (int i = 0; i < selectedKeys.length; i++) {
	    try {
	      CustomerDataObject z = module.getCustomer(selectedKeys[i]);
	      z.setActive("1");
	      dao.updateCustomer(z);
	    } catch (DaoException e) {
	      log.error(e.toString());
	    }
	  }
	  return new Forward(FORWARD_ACTIVE);
	}        
      }
      else if ("deactivate".equals(action)) {
	if (selectedKeys.length > 0) {
	  for (int i = 0; i < selectedKeys.length; i++) {
	    try {
	      CustomerDataObject z = module.getCustomer(selectedKeys[i]);
	      z.setActive("0");
	      dao.updateCustomer(z);
	    } catch (DaoException e) {
	      log.error(e.toString());
	    }
	  }
	  return new Forward(FORWARD_INACTIVE);
	}        
      }
      else if("delete".equals(action)) {
	if (selectedKeys.length > 0) {
	  for (int i = 0; i < selectedKeys.length; i++) {
	    try {
	      /*delete customer's quotations? */
	      dao.deleteCustomerQuotation(selectedKeys[i]);
	      dao.deleteCustomer(selectedKeys[i]);
	    } catch (DaoException e) {
	      log.error(e.toString());
	    }
	  }
	  return new Forward(FORWARD_DELETE);
	}        
      }
      return super.processAction(event, action, selectedKeys);
    }
  }
}