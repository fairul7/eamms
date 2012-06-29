package com.tms.quotation.ui;

import com.tms.quotation.model.*;
import java.lang.reflect.Array;
import java.util.*;
import kacang.Application;
import kacang.model.DaoException;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.stdui.*;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;


public class ViewApprovedQuotation extends Table {

  public static String FORWARD_DELETE="ViewQuotation.delete";
  public static String FORWARD_INVALID="ViewQuotation.invalid";
  
  private String canEdit = "0";
  private String canApprove = "0";
  private String canDelete = "0";
  private String editUrl="";

  private String userId="";
  private String quotationId="";
  
  public String getCanEdit() {return canEdit;}
  public void setCanEdit(String string) {canEdit = string;}
  public String getCanDelete() {return canDelete;}
  public void setCanDelete(String string) {canDelete = string;}
  public String getEditUrl(){return editUrl;}
  public void setEditUrl(String url){this.editUrl=url;}
  public String getQuotationId() {return quotationId;}
  public void setCanApprove(String string) {canApprove = string;}
  public String getCanApprove() {return canApprove;}
  
  public ViewApprovedQuotation() {}

  public ViewApprovedQuotation(String s) {super(s);}

  public void init() {
    super.init();
    setPageSize(10);
    setModel(new ViewApprovedModel());
    setWidth("100%");
  }

  public void onRequest(Event event) {
    //if ("0".equals(canEdit)) {setSortable(false);}
    setModel(new ViewApprovedModel());
   
    if( "0".equals(canApprove)){
      SecurityService srv = (SecurityService) Application.getInstance().getService(SecurityService.class);
      User user = srv.getCurrentUser(event.getRequest());
      userId = user.getId();      
    }
    else {
      userId="";
    }
  }

  class ViewApprovedModel extends TableModel {
    ViewApprovedModel() {
      removeChildren();      
      setMultipleSelect(true);
      Application app=Application.getInstance();
      QuotationModule MODULE = (QuotationModule)app.getModule(QuotationModule.class);      

      if ("1".equals(canDelete)) {
        addAction(new TableAction("delete",
            app.getMessage("com.tms.quotation.button.delete"),
            app.getMessage("com.tms.quotation.viewQuotation.alert.delete")));
      }
      //table columns
      HashMap hm = MODULE.customerMap();
      TableStringFormat ts = new TableStringFormat(hm);
      TableColumn tc1 = new TableColumn("companyId", app.getMessage("com.tms.quotation.viewQuotation.customer"));
      tc1.setFormat(ts);
      tc1.setUrlParam("quotationId");
      addColumn(tc1);
      
      TableColumn tc3 = new TableColumn("subject", app.getMessage("com.tms.quotation.viewQuotation.subject"));
      addColumn(tc3);
      
//      TableColumn tc5 = new TableColumn("status", "Status");
//      addColumn(tc5);
      
      TableColumn tc6 = new TableColumn("openDate", app.getMessage("com.tms.quotation.viewQuotation.created"));
      tc6.setFormat( new TableDateFormat("dd-MM-yyyy"));
      addColumn(tc6);
      
      TableColumn tc7 = new TableColumn("closeDate", app.getMessage("com.tms.quotation.viewQuotation.approved"));
      tc7.setFormat( new TableDateFormat("dd-MM-yyyy"));
      addColumn(tc7);
      
//      TableColumn tc8 = new TableColumn("recdate", "Last Modified");
//      tc8.setFormat( new TableDateFormat("dd-MM-yyyy"));
//      addColumn(tc8);
      
//      TableColumn editCol = new TableColumn(null,"", false);
//      editCol.setLabel("Edit");
//      editCol.setUrl(editUrl);
//      editCol.setUrlParam("quotationId");
//      addColumn(editCol);
      
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

/*
      String[] arrstatus = MODULE.selectBoxQuotationstatus();
      TableFilter tfstatus = new TableFilter("searchstatus");
      SelectBox sbstatus = new SelectBox("sbstatus");
      sbstatus.setOptions("-1=All");
      for (int i=0; i<Array.getLength(arrstatus); i++) {
        sbstatus.setOptions(arrstatus[i]);
      }
      tfstatus.setWidget(sbstatus);
      addFilter(tfstatus);
*/
    }

    public Collection getTableRows() {
      String searchBy = "";
      searchBy = (String)getFilterValue("searchBy");
      String searchstatus = "Approved";
//      List lststatus = (List)getFilterValue("searchstatus");
//      if (lststatus.size() > 0) {searchstatus = (String)lststatus.get(0);}

      QuotationDao dao = (QuotationDao)Application.getInstance().getModule(QuotationModule.class).getDao();
      try {
        return dao.selectQuotation(searchBy, userId, searchstatus, getSort(), isDesc(), getStart(), getRows());
      } catch (DaoException e) {
        Log.getLog(getClass()).error(e.toString());
        return new ArrayList();
      }
    }

    public int getTotalRowCount() {
      String searchBy = "";
      searchBy = (String)getFilterValue("searchBy");
      String searchstatus = "Approved";
//      List lststatus = (List)getFilterValue("searchstatus");
//      if (lststatus.size() > 0) {searchstatus = (String)lststatus.get(0);}

      QuotationDao dao = (QuotationDao)Application.getInstance().getModule(QuotationModule.class).getDao();
      try {
        return dao.selectQuotationCount(searchBy, userId, searchstatus);
      } catch (DaoException e) {
        Log.getLog(getClass()).error(e.toString());
        return 0;
      }
    }

    public String getTableRowKey() {
      if ("1".equals(canDelete) ) { //|| "1".equals(canApprove)) {
//        setMultipleSelect(false);
        return "quotationId";
      }
      else {return "";}
    }

    public Forward processAction(Event event, String action, String[] selectedKeys) {
      QuotationModule MODULE = (QuotationModule)Application.getInstance().getModule(QuotationModule.class);
      QuotationDao dao = (QuotationDao)MODULE.getDao();
      /*
      if ("add".equals(action))
        return new Forward(FORWARD_ADD);
      else if ("approve".equals(action)) {

//        MODULE.updateQuotationStatus("Approved", selectedKeys);

        quotationId = selectedKeys[0];
        QuotationDataObject z = MODULE.getQuotation(quotationId);
        if( "Approved".equals(z.getStatus()))
          return new Forward(FORWARD_INVALID);
        else
          return new Forward(FORWARD_APPROVE);
      }
      else
       */        
      if ("delete".equals(action)) {
	if (selectedKeys.length > 0) {
	  try {
	    for (int i = 0; i < selectedKeys.length; i++) {
	      dao.deleteQuotationIdItem(selectedKeys[i]);
	      dao.deleteQtnContent(selectedKeys[i]);
	      dao.deleteQuotation(selectedKeys[i]);
	      //            dao.deleteQuotationItemMap(selectedKeys[i]);
	    }

	    return new Forward(FORWARD_DELETE);
	  } catch (DaoException e) {
	    Log.getLog(getClass()).error(e.toString());
	  }
	}        
      }
      return super.processAction(event, action, selectedKeys);
    }
  }
}