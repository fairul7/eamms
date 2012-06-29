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


public class ViewTemplate extends Table {

  public static String FORWARD_ADD="ViewTemplate.add";
  public static String FORWARD_DELETE="ViewTemplate.delete";
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
  public String getCanActivate() {return canActivate;}
  public void setCanActivate(String string) {canActivate=string;}
  
  public ViewTemplate() {}

  public ViewTemplate(String s) {super(s);}

  public void init() {
    super.init();
    setPageSize(10);
    setModel(new ViewTemplateModel());
    setWidth("100%");
  }

  public void onRequest(Event event) {
    //if ("0".equals(canEdit)) {setSortable(false);}
    setModel(new ViewTemplateModel());
  }

  class ViewTemplateModel extends TableModel {
    ViewTemplateModel() {
      removeChildren();
      Application app = Application.getInstance();
//      if ("1".equals(canAdd)) {addAction(new TableAction("add", "Add"));}
//      if ("1".equals(canDelete)) {addAction(new TableAction("delete", "Delete", "Delete?"));}
//      addAction(new TableAction("deactivate", "Deactivate","Deactivate Template?"));
      if ("1".equals(canAdd)) {
        addAction(new TableAction("add", app.getMessage("com.tms.quotation.button.add")));
      }
      if ("1".equals(canDelete)) {
        addAction(new TableAction("delete", 
            app.getMessage("com.tms.quotation.button.delete"), 
            app.getMessage("com.tms.quotation.viewTemplate.alert.delete")));
      }
      if ("1".equals(canActivate)) {
        addAction(new TableAction("activate",
            app.getMessage("com.tms.quotation.button.activate"),
            app.getMessage("com.tms.quotation.viewTemplate.alert.activate")));
        addAction(new TableAction("deactivate", 
            app.getMessage("com.tms.quotation.button.deactivate"), 
            app.getMessage("com.tms.quotation.viewTemplate.alert.deactivate")));
      }
      //table columns
      TableColumn tc1 = new TableColumn("templateName", app.getMessage("com.tms.quotation.viewTemplate.name"));
      tc1.setUrlParam("templateId");
      addColumn(tc1);
/*      TableColumn tc2 = new TableColumn("templateHeader", "Template Header");
      addColumn(tc2);
      TableColumn tc3 = new TableColumn("templateFooter", "Template Footer");
      addColumn(tc3);*/
      TableColumn tc4 = new TableColumn("recdate", app.getMessage("com.tms.quotation.viewTemplate.created"));
      tc4.setFormat(new TableDateFormat("dd-MM-yyyy"));
      addColumn(tc4);
      TableColumn tc5 = new TableColumn("whoModified", app.getMessage("com.tms.quotation.viewTemplate.createdBy"));
      addColumn(tc5);
      
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
//      String[] arractive = MODULE.selectBoxTemplateactive();
      TableFilter tfactive = new TableFilter("searchactive");
      SelectBox sbactive = new SelectBox("sbactive");
      sbactive.setOptions("-1=All");
      sbactive.setOptions("1=Active");
      sbactive.setOptions("0=Inactive");
/*      for (int i=0; i<Array.getLength(arractive); i++) {
        sbactive.setOptions(arractive[i]);
      }
*/      tfactive.setWidget(sbactive);
      addFilter(tfactive);

    }

    public Collection getTableRows() {
      String searchBy = "";
      searchBy = (String)getFilterValue("searchBy");

      List lstactive = (List)getFilterValue("searchactive");
      String searchactive = "-1";
      if (lstactive.size() > 0) {searchactive = (String)lstactive.get(0);}

      QuotationDao dao = (QuotationDao)Application.getInstance().getModule(QuotationModule.class).getDao();
      try {
        return dao.selectTemplate(searchBy, searchactive, getSort(), isDesc(), getStart(), getRows());
      } catch (DaoException e) {
        Log.getLog(getClass()).error(e.toString());
        return new ArrayList();
      }
    }

    public int getTotalRowCount() {
      String searchBy = "";
      searchBy = (String)getFilterValue("searchBy");

      List lstactive = (List)getFilterValue("searchactive");
      String searchactive = "-1";
      if (lstactive.size() > 0) {searchactive = (String)lstactive.get(0);}

      QuotationDao dao = (QuotationDao)Application.getInstance().getModule(QuotationModule.class).getDao();
      try {
        return dao.selectTemplateCount(searchBy, searchactive);
      } catch (DaoException e) {
        Log.getLog(getClass()).error(e.toString());
        return 0;
      }
    }

    public String getTableRowKey() {
/*      
      if ("1".equals(canDelete)) {return "templateId";}
      else {return "";}
*/  return "templateId";      
    }

    public Forward processAction(Event event, String action, String[] selectedKeys) {
      QuotationModule MODULE = (QuotationModule)Application.getInstance().getModule(QuotationModule.class);
//      QuotationDao dao = (QuotationDao)Application.getInstance().getModule(QuotationModule.class).getDao();
      if ("add".equals(action))
        return new Forward(FORWARD_ADD);
      else if ("delete".equals(action)) {
	if (selectedKeys.length > 0) {
	  for (int i = 0; i < selectedKeys.length; i++) {
	    MODULE.deleteTemplate(selectedKeys[i]);
	  }
	  /*        
		   try {
		   for(int i=0; i<selectedKeys.length; i++)
		   dao.deleteTemplate(selectedKeys[i]);
		   return new Forward(FORWARD_DELETE);
		   } catch (DaoException e) {
		   Log.getLog(getClass()).error(e.toString());
		   }
	   */
	}                
      }
      else if ("deactivate".equals(action)) {
	if (selectedKeys.length > 0) {
	  for(int i=0; i<selectedKeys.length; i++) {
	    TemplateDataObject tdo = MODULE.getTemplate(selectedKeys[i]);
	    tdo.setActive("0");
	    MODULE.updateTemplate(tdo);
	  }
        }
      }
      else if ("activate".equals(action)) {
	if (selectedKeys.length > 0) {
	  for(int i=0; i<selectedKeys.length; i++) {
	    TemplateDataObject tdo = MODULE.getTemplate(selectedKeys[i]);
	    tdo.setActive("1");
	    MODULE.updateTemplate(tdo);
	  }
	}
      }
      return super.processAction(event, action, selectedKeys);
    }
  }
}