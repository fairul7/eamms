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


public class ViewQtnTemplateText extends Table {

  public static String FORWARD_ADD="ViewQtnTemplateText.add";
  public static String FORWARD_DELETE="ViewQtnTemplateText.delete";
  private String canAdd = "0";
  private String canEdit = "0";
  private String canDelete = "0";
  private String canActivate ="0";
  
  public String getCanAdd() {return canAdd;}
  public void setCanAdd(String string) {canAdd = string;}
  public String getCanEdit() {return canEdit;}
  public void setCanEdit(String string) {canEdit = string;}
  public String getCanDelete() {return canDelete;}
  public void setCanDelete(String string) {canDelete = string;}
  public String getCanActivate() { return canActivate; }
  public void setCanActivate(String string) {canActivate=string;}
  
  public ViewQtnTemplateText() {}

  public ViewQtnTemplateText(String s) {super(s);}

  public void init() {
    super.init();
    setPageSize(10);
    setModel(new ViewQtnTemplateTextModel());
    setWidth("100%");
  }

  public void onRequest(Event event) {
    //if ("0".equals(canEdit)) {setSortable(false);}
    setModel(new ViewQtnTemplateTextModel());
  }

  class ViewQtnTemplateTextModel extends TableModel {
    ViewQtnTemplateTextModel() {
      removeChildren();
      Application app = Application.getInstance();
      
      if ("1".equals(canAdd)) {
        addAction(new TableAction("add", app.getMessage("com.tms.quotation.button.add")));
      }
      if ("1".equals(canDelete)) {
        addAction(new TableAction("delete",
            app.getMessage("com.tms.quotation.button.delete"),
            app.getMessage("com.tms.quotation.viewQtnTemplateText.alert.delete")));
      }
      if ("1".equals(canActivate)) {
        addAction( new TableAction("activate",
            app.getMessage("com.tms.quotation.button.activate"), 
            app.getMessage("com.tms.quotation.viewQtnTemplateText.alert.activate")));

        addAction( new TableAction("deactivate",
            app.getMessage("com.tms.quotation.button.deactivate"),
            app.getMessage("com.tms.quotation.viewQtnTemplateText.alert.deactivate")));
      }

      //table columns
      TableColumn tc1 = new TableColumn("templateDescription", app.getMessage("com.tms.quotation.viewQtnTemplateText.description"));
      tc1.setUrlParam("templateId");
      addColumn(tc1);
      TableColumn tc2 = new TableColumn("templateBody", app.getMessage("com.tms.quotation.viewQtnTemplateText.body"));
      addColumn(tc2);

      TableColumn tc3 = new TableColumn("active", app.getMessage("com.tms.quotation.active"));
      HashMap map = new HashMap();
      map.put("0", app.getMessage("com.tms.quotation.inactive"));
      map.put("1", app.getMessage("com.tms.quotation.active"));
      tc3.setFormat(new TableStringFormat(map));
      addColumn(tc3);      
      
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
        return dao.selectQtnTemplateText(searchBy, getSort(), isDesc(), getStart(), getRows());
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
        return dao.selectQtnTemplateTextCount(searchBy);
      } catch (DaoException e) {
        Log.getLog(getClass()).error(e.toString());
        return 0;
      }
    }

    public String getTableRowKey() {
      if ("1".equals(canDelete)) {return "templateId";}
      else {return "";}
    }

    public Forward processAction(Event event, String action, String[] selectedKeys) {
      QuotationModule module = (QuotationModule)Application.getInstance().getModule(QuotationModule.class);
      QuotationDao dao = (QuotationDao)module.getDao();
      Log log = Log.getLog(getClass());
      
      if ("add".equals(action))
        return new Forward(FORWARD_ADD);
      else if("activate".equals(action)) {
	if (selectedKeys.length > 0) {
	  for (int i = 0; i < selectedKeys.length; i++) {
	    QtnTemplateTextDataObject z = module
		.getQtnTemplateText(selectedKeys[i]);
	    try {
	      z.setActive("1");
	      dao.updateQtnTemplateText(z);
	      return new Forward(FORWARD_DELETE);
	    } catch (DaoException e) {
	      log.error(e.toString());
	    }
	  }
	}        
        
      }
      else if ("deactivate".equals(action)) {
	if (selectedKeys.length > 0) {
	  for (int i = 0; i < selectedKeys.length; i++) {
	    QtnTemplateTextDataObject z = module
		.getQtnTemplateText(selectedKeys[i]);
	    try {
	      z.setActive("0");
	      dao.updateQtnTemplateText(z);
	      //            dao.deleteQtnTemplateText(selectedKeys[i]);
	      return new Forward(FORWARD_DELETE);
	    } catch (DaoException e) {
	      log.error(e.toString());
	    }
	  }
	}        
      }
      else if("delete".equals(action)) {
	if (selectedKeys.length > 0) {
	  for (int i = 0; i < selectedKeys.length; i++) {
	    try {
	      dao.deleteQtnTemplateText(selectedKeys[i]);
	    } catch (DaoException e) {
	      log.error(e.toString());
	    }
	  }
	}        
      }
      return super.processAction(event, action, selectedKeys);
    }
  }
}