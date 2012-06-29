package com.tms.quotation.ui;

import com.tms.quotation.model.*;
import java.lang.reflect.Array;
import java.util.*;
import kacang.Application;
import kacang.model.DaoException;
import kacang.stdui.*;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;


public class ViewQtnTable extends Table {

  public static String FORWARD_ADD="ViewQtnTable.add";
  public static String FORWARD_DELETE="ViewQtnTable.delete";
  private String canAdd = "0";
  private String canEdit = "0";
  private String canDelete = "0";
  private String canActivate ="0";  

  private String colEditUrl="";
  
  public String getCanAdd() {return canAdd;}
  public void setCanAdd(String string) {canAdd = string;}
  public String getCanEdit() {return canEdit;}
  public void setCanEdit(String string) {canEdit = string;}
  public String getCanDelete() {return canDelete;}
  public void setCanDelete(String string) {canDelete = string;}
  public String getColEditUrl() { return colEditUrl; }
  public void setColEditUrl(String url) { colEditUrl = url; }
  public String getCanActivate() {return canActivate;}
  public void setCanActivate(String string) {canActivate=string;}  
  
  public ViewQtnTable() {}

  public ViewQtnTable(String s) {super(s);}

  public void init() {
    super.init();
    setPageSize(10);
    setModel(new ViewQtnTableModel());
    setWidth("100%");
  }

  public void onRequest(Event event) {
    //if ("0".equals(canEdit)) {setSortable(false);}
    setModel(new ViewQtnTableModel());
  }

  class ViewQtnTableModel extends TableModel {
    ViewQtnTableModel() {
      removeChildren();
      Application app = Application.getInstance();
      
      if ("1".equals(canAdd)) {
        addAction(new TableAction("add", app.getMessage("com.tms.quotation.button.add")));
      }
      if ("1".equals(canDelete)) {
        addAction(new TableAction("delete", 
            app.getMessage("com.tms.quotation.button.delete"), 
            app.getMessage("com.tms.quotation.viewQtnTable.alert.delete")));
      }
      if ("1".equals(canActivate)) {
        addAction(new TableAction("activate",
            app.getMessage("com.tms.quotation.button.activate"),
            app.getMessage("com.tms.quotation.viewQtnTable.alert.activate")));
        addAction(new TableAction("deactivate", 
            app.getMessage("com.tms.quotation.button.deactivate"), 
            app.getMessage("com.tms.quotation.viewQtnTable.alert.deactivate")));
      }
      
      //table columns
      TableColumn tc1 = new TableColumn("tableDescription", app.getMessage("com.tms.quotation.viewQtnTable.description"));
      tc1.setUrlParam("tableId");
      addColumn(tc1);
      TableColumn tc2 = new TableColumn("tableCaption", app.getMessage("com.tms.quotation.viewQtnTable.caption"));
      addColumn(tc2);
      
      TableColumn tc3 = new TableColumn("active", app.getMessage("com.tms.quotation.active"));
      HashMap map = new HashMap();
      map.put("0", app.getMessage("com.tms.quotation.inactive"));
      map.put("1", app.getMessage("com.tms.quotation.active"));
      tc3.setFormat(new TableStringFormat(map));
      addColumn(tc3);
      
      TableColumn editColumns = new TableColumn(null, app.getMessage("com.tms.quotation.viewQtnTable.columns"));
      editColumns.setLabel(app.getMessage("com.tms.quotation.label.edit"));
      editColumns.setUrl(colEditUrl);
      editColumns.setUrlParam("tableId");
      addColumn(editColumns);
/*      
      TableColumn tc3 = new TableColumn("tableStyle", "tableStyle");
      addColumn(tc3);
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
        return dao.selectQtnTable(searchBy, getSort(), isDesc(), getStart(), getRows());
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
        return dao.selectQtnTableCount(searchBy);
      } catch (DaoException e) {
        Log.getLog(getClass()).error(e.toString());
        return 0;
      }
    }

    public String getTableRowKey() {
      if ("1".equals(canDelete)) {return "tableId";}
      else {return "";}
    }

    public Forward processAction(Event event, String action, String[] selectedKeys) {
      QuotationModule module = (QuotationModule)Application.getInstance().getModule(QuotationModule.class);
      QuotationDao dao = (QuotationDao)module.getDao();
      Log log = Log.getLog(getClass());
      
      if ("add".equals(action))
        return new Forward(FORWARD_ADD);
      else if ("deactivate".equals(action)) {
	if (selectedKeys.length > 0) {
	  for (int i = 0; i < selectedKeys.length; i++) {
	    QtnTableDataObject z = module.getQtnTable(selectedKeys[i]);
	    z.setActive("0");
	    try {
	      dao.updateQtnTable(z);
	    } catch (DaoException e) {
	      log.error(e.toString());
	    }
	  }
	}        
      }
      else if ("activate".equals(action)) {
	if (selectedKeys.length > 0) {
	  for (int i = 0; i < selectedKeys.length; i++) {
	    QtnTableDataObject z = module.getQtnTable(selectedKeys[i]);
	    z.setActive("1");
	    try {
	      dao.updateQtnTable(z);
	    } catch (DaoException e) {
	      log.error(e.toString());
	    }
	  }
	}        
      }
      else if ("delete".equals(action)) {
	if (selectedKeys.length > 0) {
	  for (int i = 0; i < selectedKeys.length; i++) {
	    try {
	      dao.deleteQtnTable(selectedKeys[i]);
	    } catch (DaoException e) {
	      log.error(e.toString());
	    }
	    try {
	      dao.deleteQtnTableColumn(selectedKeys[i]);
	    } catch (DaoException e) {
	      log.error(e.toString());
	    }
	    try {
	      dao.deleteTableQuotation(selectedKeys[i]);
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