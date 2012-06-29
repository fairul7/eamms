package com.tms.quotation.ui;

import com.tms.quotation.model.*;
//import java.lang.reflect.Array;
import java.util.*;

//import javax.swing.plaf.basic.BasicBorders.RadioButtonBorder;

import kacang.Application;
import kacang.model.DaoException;
import kacang.stdui.*;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;


public class CustomerTable extends Table {
  private String nextUrl = "linkURL";
/*
  public static String FORWARD_ADD="ViewCustomer.add";
  public static String FORWARD_DELETE="ViewCustomer.delete";
  
  private String canAdd = "0";
  private String canEdit = "0";
  private String canDelete = "0";

  public String getCanAdd() {return canAdd;}
  public void setCanAdd(String string) {canAdd = string;}
  public String getCanEdit() {return canEdit;}
  public void setCanEdit(String string) {canEdit = string;}
  public String getCanDelete() {return canDelete;}
  public void setCanDelete(String string) {canDelete = string;}
*/
  public CustomerTable() {}

  public CustomerTable(String s) {super(s);}

  public void setNextUrl(String url){
    nextUrl=url;
  }
  
  public String getNextUrl(){
    return nextUrl;
  }
  
  public void init() {
    super.init();
    setPageSize(10);
    setModel(new CustomerTableModel());
    setWidth("100%");
  }

  public void onRequest(Event event) {
    //if ("0".equals(canEdit)) {setSortable(false);}
    setModel(new CustomerTableModel());
  }

  class CustomerTableModel extends TableModel {
    CustomerTableModel() {
      removeChildren();
      Application app=Application.getInstance();
      //table actions
//      addAction(new TableAction("select", "Select"));
      
      //table columns
      TableColumn tc1 = new TableColumn("companyName", app.getMessage("com.tms.quotation.viewCustomer.company"));
      tc1.setUrl(nextUrl);
      tc1.setUrlParam("customerId");
      addColumn(tc1);
      TableColumn tc2 = new TableColumn("contactFirstName", app.getMessage("com.tms.quotation.viewCustomer.firstName"));
      addColumn(tc2);
      TableColumn tc3 = new TableColumn("contactLastName", app.getMessage("com.tms.quotation.viewCustomer.lastName"));
      addColumn(tc3);
      
      //setSort(" ");

      //table filters
      //search filter
      TableFilter searchBy = new TableFilter("lbSearchBy");
      Label search = new Label("search","  Search  ");
      searchBy.setWidget(search);
      addFilter(searchBy);

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
        return dao.selectActiveCustomer(searchBy, getSort(), isDesc(), getStart(), getRows());
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
        return dao.selectActiveCustomerCount(searchBy);
      } catch (DaoException e) {
        Log.getLog(getClass()).error(e.toString());
        return 0;
      }
    }
/*
    public String getTableRowKey() {
    	return "";
    }
*/
/*    
    public Forward processAction(Event event, String action, String[] selectedKeys) {
      Application app = Application.getInstance();
      QuotationModule mod = (QuotationModule) app.getModule(QuotationModule.class);
      QuotationDao dao = (QuotationDao) mod.getDao();
      if(("select".equals(action)) && (selectedKeys.length == 1)){
        return new Forward(FORWARD_SELECT);        
      }
      
      if ("add".equals(action))
        return new Forward(FORWARD_ADD);
      else if ("delete".equals(action)) {
        try {
          for(int i=0; i<selectedKeys.length; i++)
            dao.deleteCustomer(selectedKeys[i]);
          return new Forward(FORWARD_DELETE);
        } catch (DaoException e) {
          Log.getLog(getClass()).error(e.toString());
        }
      }
      
      return super.processAction(event, action, selectedKeys);
    }
*/    
  }
}