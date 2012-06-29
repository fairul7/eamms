package com.tms.quotation.ui;

import com.tms.quotation.model.*;
import java.lang.reflect.Array;
import java.util.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import kacang.Application;
import kacang.model.DaoException;
import kacang.stdui.*;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;


public class ViewQtnColumn extends Table {

  public static String FORWARD_ADD="ViewQtnColumn.add";
  public static String FORWARD_DELETE="ViewQtnColumn.delete";
  public static String FORWARD_SORT="ViewQtnColumn.sort";
  
  private String canAdd = "0";
  private String canEdit = "0";
  private String canDelete = "0";
  private String tableId = "";
  
  public String getCanAdd() {return canAdd;}
  public void setCanAdd(String string) {canAdd = string;}
  public String getCanEdit() {return canEdit;}
  public void setCanEdit(String string) {canEdit = string;}
  public String getCanDelete() {return canDelete;}
  public void setCanDelete(String string) {canDelete = string;}

  public ViewQtnColumn() {}

  public ViewQtnColumn(String s) {super(s);}

  public String getTableId() { return tableId; }
  
  public void init() {
    super.init();
    setPageSize(10);
    setModel(new ViewQtnColumnModel());
    setWidth("100%");
  }

  public void onRequest(Event event) {
    //if ("0".equals(canEdit)) {setSortable(false);}
    tableId=event.getRequest().getParameter("tableId");
    setModel(new ViewQtnColumnModel());
  }

  class ViewQtnColumnModel extends TableModel {
    ViewQtnColumnModel() {
      removeChildren();
      Application app = Application.getInstance();
        
      if ("1".equals(canAdd)) {
        addAction(new TableAction("add", app.getMessage("com.tms.quotation.button.add")));
      }
      if ("1".equals(canDelete)) {
        addAction(new TableAction("delete", 
            app.getMessage("com.tms.quotation.button.delete"), 
            app.getMessage("com.tms.quotation.viewQtnColumn.alert.delete")));
        }
      addAction(new TableAction("sort",app.getMessage("com.tms.quotation.viewQtnColumn.button.sort")));
      
      //table columns
/*      
      TableColumn tc1 = new TableColumn("tableId", "tableId");
      tc1.setUrlParam("columnId");
      addColumn(tc1);
      
      TableColumn tc2 = new TableColumn("position", "position");
      addColumn(tc2);
*/      
      TableColumn tc3 = new TableColumn("header", app.getMessage("com.tms.quotation.viewQtnColumn.header"));
      tc3.setUrlParam("columnId");
      tc3.setSortable(false);
      addColumn(tc3);      
/*      
      TableColumn tc4 = new TableColumn("columnClassName", "columnClassName");
      addColumn(tc4);
      TableColumn tc5 = new TableColumn("columnStyle", "columnStyle");
      addColumn(tc5);
*/      
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
//        return dao.selectQtnColumn(tableId, searchBy, getSort(), isDesc(), getStart(), getRows());
        return dao.selectQtnColumn(tableId, searchBy, getStart(), getRows());        
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
        return dao.selectQtnColumnCount(tableId, searchBy);
      } catch (DaoException e) {
        Log.getLog(getClass()).error(e.toString());
        return 0;
      }
    }

    public String getTableRowKey() {
      if ("1".equals(canDelete)) {return "columnId";}
      else {return "";}
    }

    public Forward processAction(Event event, String action, String[] selectedKeys) {
      QuotationDao dao = (QuotationDao)Application.getInstance().getModule(QuotationModule.class).getDao();
      if ("add".equals(action))
        return new Forward(FORWARD_ADD);
      if ("sort".equals(action))
        return new Forward(FORWARD_SORT);
      else if ("delete".equals(action)) {
	if (selectedKeys.length > 0) {
	  for (int i = 0; i < selectedKeys.length; i++) {
	    try {
	      dao.deleteQtnColumn(selectedKeys[i]);
	      return new Forward(FORWARD_DELETE);
	    } catch (DaoException e) {
	      Log.getLog(getClass()).error(e.toString());
	    }
	  }
	}	
      }
      return super.processAction(event, action, selectedKeys);
    }
  }
}