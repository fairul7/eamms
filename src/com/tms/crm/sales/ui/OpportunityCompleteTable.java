package com.tms.crm.sales.ui;



import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import kacang.Application;
import kacang.stdui.ButtonGroup;
import kacang.stdui.CheckBox;
import kacang.stdui.Form;
import kacang.stdui.Label;
import kacang.stdui.Radio;
import kacang.stdui.SelectBox;
import kacang.stdui.Table;
import kacang.stdui.TableColumn;
import kacang.stdui.TableDateFormat;
import kacang.stdui.TableDecimalFormat;
import kacang.stdui.TableFilter;
import kacang.stdui.TableFormat;
import kacang.stdui.TableModel;
import kacang.stdui.TableStringFormat;
import kacang.stdui.TextField;
import kacang.stdui.event.FormEventAdapter;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.ui.WidgetManager;

import com.tms.crm.sales.misc.AccessUtil;
import com.tms.crm.sales.misc.MyUtil;
import com.tms.crm.sales.model.AccountManager;
import com.tms.crm.sales.model.AccountManagerModule;
import com.tms.crm.sales.model.Opportunity;
import com.tms.crm.sales.model.OpportunityModule;

public class OpportunityCompleteTable extends Table {

    private String userId = null;
    private boolean checked = false;
    private boolean myopportunity = false;
    private String closed;
    private String search;
    private String stage;
    private String manager;
    private String size;
    private boolean closedSale=false;
    private boolean lost;
    private boolean monthly;
    private Date toDate;
    private Date fromDate;
    private CheckBox cb_showClosed;
    
    public void init() {
    	setModel(new OpportunityCompleteTableModel());
        Form tableForm = getFilterForm();
        
        tableForm.addFormEventListener(new FormEventAdapter() {
                public Forward onSubmit(Event evt) {
                    Forward f = super.onSubmit(evt);
                    try {
                        Map childMap = getChildMap();
                        Form form = (Form)childMap.get("filterForm");
                        Map formChildMap = form.getChildMap();
                        
                        CheckBox chkClose = (CheckBox)formChildMap.get("cbshowclosed");
                        SelectBox selAcMng = (SelectBox)formChildMap.get("sel_AccountManagerID");
                        TextField keyword = (TextField)formChildMap.get("keywordField");
                        Radio radioMonthly = (Radio)formChildMap.get("viewmonthly");
                        CheckBox cbLost = (CheckBox)formChildMap.get("cbshowlost");
                        SelectBox selStage = (SelectBox)formChildMap.get("sel_StageID");
                        MonthFilter monthFil = (MonthFilter)formChildMap.get("monthfilterwidget");
                        SelectBox selPageSize = (SelectBox)formChildMap.get("pageSizeSelectBox");
                        
                        manager = MyUtil.getSingleValue_SelectBox(selAcMng);
                        userId = MyUtil.getSingleValue_SelectBox(selAcMng);;
                        search = keyword.getValue().toString();
                        stage = MyUtil.getSingleValue_SelectBox(selStage);
                        monthly = radioMonthly.isChecked();
                        lost = cbLost.isChecked();
                        toDate = monthFil.getFromDate();
                        fromDate = monthFil.getToDate();
                        size = MyUtil.getSingleValue_SelectBox(selPageSize);
                        if(chkClose.isChecked()){
                        	checked = true;
                        	
                        }else{
                        	checked = false;
                        }
                        init();
                    } catch (Exception e) {
                        ;
                    }
                    return f;
                }
            });
        
    }
    
    
    public CheckBox getCb_showClosed() {
		return cb_showClosed;
	}
    
    

	public void setCb_showClosed(CheckBox cb_showClosed) {
		this.cb_showClosed = cb_showClosed;
	}

	public void onRequest(Event event) {
//		initTable();
	}

    public void initTable() {
    	//init();
    	//super.init();    //To change body of overridden methods use File | Settings | File Templates.
        
        setSort("opportunityEnd");
        setModel(new OpportunityCompleteTableModel());
    }

    public void initClosedSalesTable() {
        super.init();    //To change body of overridden methods use File | Settings | File Templates.
        setSort("opportunityEnd");
        setModel(new OpportunityCompleteTableModel());
    }
    
    public class OpportunityCompleteTableModel extends TableModel{
        private Radio viewAll;
        private Radio viewMonthly;
        private MonthFilter monthFilter;
       
        
        public OpportunityCompleteTableModel() {
            
			// Getting Financial Setting
			if(checked){
				getClosedSalesColumns();
				closedSale= true;
			}else{
				getOppColumns();
				closedSale=false;
			}
			
            getFilters();
        }

        public void getOppColumns(){
        	OpportunityModule om = (OpportunityModule) Application.getInstance().getModule(OpportunityModule.class);
			Opportunity csign = om.getFinancilSetting();
			String sign = csign.getCurrencySymbol();
			
            TableColumn status = new TableColumn("modifiedDate","&#149;",true);
            status.setFormat(new TableFormat(){
                public String format(Object o) {
                    Date modifiedDate = (Date)o;
                    Date today = new Date();
                    long diff = today.getTime() - modifiedDate.getTime();
                    int day = (int)(diff/(1000*60*60*24));


					//if ("1".equals(opp.getOpportunityStatus())) {

						if(day>=OpportunitySummaryTable.red){
							return "<img src=\""+ getWidgetManager().getAttribute(WidgetManager.CONTEXT_PATH) +  "/ekms/sfa/images/03.gif\" />";
						}else if(day>=OpportunitySummaryTable.orange){
							return "<img src=\""+ getWidgetManager().getAttribute(WidgetManager.CONTEXT_PATH) +  "/ekms/sfa/images/02.gif\" />";
						} else if ( day>=OpportunitySummaryTable.green || day<=OpportunitySummaryTable.orange ) {
							return "<img src=\""+ getWidgetManager().getAttribute(WidgetManager.CONTEXT_PATH) +  "/ekms/sfa/images/01.gif\" />";
						}
					//}
                    return null;
                }
            } );



            addColumn(status);

            TableColumn tc_OpportunityName = new TableColumn("opportunityName", Application.getInstance().getMessage("sfa.label.opportunity","Opportunity"));
            tc_OpportunityName.setFormat(new TableLimitStringFormat(25));
			tc_OpportunityName.setUrlParam("opportunityID");

            addColumn(tc_OpportunityName);

            TableColumn tc_CompanyID = new TableColumn("companyName", Application.getInstance().getMessage("sfa.label.company","Company"));
            tc_CompanyID.setUrlParam("companyID");
            tc_CompanyID.setFormat(new TableLimitStringFormat(25));
            addColumn(tc_CompanyID);

            TableColumn tc_OpportunityStatus = new TableColumn("opportunityStatus", Application.getInstance().getMessage("sfa.label.status","Status"));
            tc_OpportunityStatus.setFormat(new TableStringFormat(Opportunity.getOpportunityStatus_More_Map()));
            addColumn(tc_OpportunityStatus);

            TableColumn tc_OpportunityStage = new TableColumn("opportunityStage", Application.getInstance().getMessage("sfa.label.stage","Stage"));
            tc_OpportunityStage.setFormat(new TableStringFormat(Opportunity.getOpportunityStage_Less_Map()));
            addColumn(tc_OpportunityStage);

            TableColumn tc_OpportunityValue = new TableColumn("opportunityValue", Application.getInstance().getMessage("sfa.label.value","Value")+" "+"("+sign+")");
            tc_OpportunityValue.setFormat(new TableDecimalFormat("#,##0.00"));
            addColumn(tc_OpportunityValue);

            TableColumn tc_AdjustedValue = new TableColumn("adjustedValue", Application.getInstance().getMessage("sfa.label.adjValue","Adj. Value")+" "+"("+sign+")");
            tc_AdjustedValue.setFormat(new TableDecimalFormat("#,##0.00"));
            tc_AdjustedValue.setSortable(false);
            addColumn(tc_AdjustedValue);

            HashMap hm = new HashMap();
            hm.put("0", Application.getInstance().getMessage("sfa.label.direct","Direct"));
            hm.put("1", Application.getInstance().getMessage("sfa.label.channel","Channel"));
            TableColumn tc_HasPartner = new TableColumn("hasPartner", Application.getInstance().getMessage("sfa.label.type","Type"));
            tc_HasPartner.setFormat(new TableStringFormat(hm));
            addColumn(tc_HasPartner);

            TableColumn tc_OpportunityEnd = new TableColumn("opportunityEnd", Application.getInstance().getMessage("sfa.label.closingDate","Closing Date"));
            tc_OpportunityEnd.setFormat(new TableDateFormat(DisplayConstants.DATE_FORMAT));
            addColumn(tc_OpportunityEnd);

//            if(checked){
//            	TableColumn a = new TableColumn("a", "AA");
//                
//                addColumn(a);
//            }
            
            if (userId == null || "".equals(userId)) {
                TableColumn tc_Users = new TableColumn("accountManagers", Application.getInstance().getMessage("sfa.label.user(s)","User(s)"));
                tc_Users.setSortable(false);
                addColumn(tc_Users);
            }
        }
        
        
        
        
        public void getClosedSalesColumns(){
        	 Application application = Application.getInstance();

 			 OpportunityModule om = (OpportunityModule) Application.getInstance().getModule(OpportunityModule.class);
 			 Opportunity csign = om.getFinancilSetting();
 			 String sign = csign.getCurrencySymbol();

             TableColumn tc_OpportunityName = new TableColumn("opportunityName", Application.getInstance().getMessage("sfa.label.opportunity","Opportunity"));
             tc_OpportunityName.setFormat(new TableLimitStringFormat(25));
             tc_OpportunityName.setUrlParam("opportunityID");

             addColumn(tc_OpportunityName);

             TableColumn tc_CompanyID = new TableColumn("companyName", Application.getInstance().getMessage("sfa.label.company","Company"));
             tc_CompanyID.setUrlParam("companyID");
             tc_CompanyID.setFormat(new TableLimitStringFormat(25));
             addColumn(tc_CompanyID);

             TableColumn tc_CloseReferenceNo = new TableColumn("closeReferenceNo", Application.getInstance().getMessage("sfa.label.referenceNo.","Reference No."));
             addColumn(tc_CloseReferenceNo);

             TableColumn tc_OpportunityValue = new TableColumn("opportunityValue", Application.getInstance().getMessage("sfa.label.value","Value")+" "+"("+sign+")");
             tc_OpportunityValue.setFormat(new TableDecimalFormat("#,##0.00"));
             addColumn(tc_OpportunityValue);

             HashMap hm = new HashMap();
             hm.put("0", Application.getInstance().getMessage("sfa.label.direct","Direct"));
             hm.put("1", Application.getInstance().getMessage("sfa.label.channel","Channel"));
             TableColumn tc_HasPartner = new TableColumn("hasPartner", Application.getInstance().getMessage("sfa.label.type","Type"));
             tc_HasPartner.setFormat(new TableStringFormat(hm));
             addColumn(tc_HasPartner);

             TableColumn tc_OpportunityEnd = new TableColumn("opportunityEnd", Application.getInstance().getMessage("sfa.label.closingDate","Closing Date"));
             tc_OpportunityEnd.setFormat(new TableDateFormat(DisplayConstants.DATE_FORMAT));
             addColumn(tc_OpportunityEnd);

             if (userId == null || "".equals(userId)) {
                 TableColumn tc_Users = new TableColumn("accountManagers", Application.getInstance().getMessage("sfa.label.a/CManager","A/C Manager"));
                 tc_Users.setSortable(false);
                 addColumn(tc_Users);
             }
        }
        
        public void getFilters(){
        	Application application = Application.getInstance();

        	TableFilter viewAllFilter = new TableFilter("viewfilter");
            TableFilter viewMonthlyFilter = new TableFilter("monthlyfilter");

            viewAll = new Radio("vewall",Application.getInstance().getMessage("sfa.label.viewAll","View All"));
            viewAll.setChecked(true);
            viewAllFilter.setWidget(viewAll);
           
            viewMonthly = new Radio("viewmonthly",Application.getInstance().getMessage("sfa.label.viewMonthly","View Monthly"));
            viewMonthlyFilter.setWidget(viewMonthly);
            if(monthly){
            	viewMonthly.setChecked(true);
            }
            ButtonGroup viewGroup = new ButtonGroup("viewGroup");
            viewGroup.addButton(viewAll);
            viewGroup.addButton(viewMonthly);
            addFilter(viewAllFilter);
            addFilter(viewMonthlyFilter);

            monthFilter = new MonthFilter("monthfilterwidget");

            TableFilter monthlyFilter= new TableFilter("monthfilter");
            monthlyFilter.setWidget(monthFilter);
            if(toDate!=null){
            	monthFilter.setDate(toDate);
            	
            }
            addFilter(monthlyFilter);

            TableFilter tf_seperator = new TableFilter("newline");
            tf_seperator.setWidget(new Label("lbnewline","<br>"));
            addFilter(tf_seperator);


            TableFilter showClosed = new TableFilter("showclosed");
            cb_showClosed = new CheckBox("cbshowclosed",Application.getInstance().getMessage("sfa.label.showClosedSales","Show Closed Sales"));
            if(closedSale){
            	cb_showClosed.setChecked(true);
            }
            showClosed.setWidget(cb_showClosed);
            addFilter(showClosed);

            TableFilter showLost = new TableFilter("showlost");
            CheckBox cb_showLost = new CheckBox("cbshowlost",Application.getInstance().getMessage("sfa.label.showLostOpportunities","Show Lost Opportunities"));
            if(lost){
            	cb_showLost.setChecked(true);
            }
            showLost.setWidget(cb_showLost);
            addFilter(showLost);

            TableFilter tf_newLine = new TableFilter("newline");
            tf_newLine.setWidget(new Label("lbnewline","<br>"));
            addFilter(tf_newLine);


            String tmpId;
            tmpId = getWidgetManager().getUser().getId();
            if(AccessUtil.isSalesManager(tmpId)&& !myopportunity){
                AccountManagerModule amModule = (AccountManagerModule) application.getModule(AccountManagerModule.class);
                Collection amCol = amModule.getAccountManagers();

                TableFilter tf_AccountManagerID = new TableFilter("tf_AccountManagerID");
                SelectBox sel_AccountManagerID = new SelectBox("sel_AccountManagerID");
                sel_AccountManagerID.addOption("", "-"+Application.getInstance().getMessage("sfa.label.all","All")+" -");
               
                Iterator iterator = amCol.iterator();
                while (iterator.hasNext()) {
                    AccountManager accountManager = (AccountManager) iterator.next();
                    sel_AccountManagerID.addOption(accountManager.getId(), accountManager.getFullName());
                }

                if(manager!=""){
                	sel_AccountManagerID.setSelectedOption(manager);
                }
                
                TableFilter tf_LabelAC = new TableFilter("labelAccountManager");
                tf_LabelAC.setWidget(new Label("lbFilterAC", "<span class='filterLabelStyle'>&nbsp;"+Application.getInstance().getMessage("sfa.label.a/CManager","A/C Manager")+":</span>"));
                addFilter(tf_LabelAC);
                tf_AccountManagerID.setWidget(sel_AccountManagerID);
                addFilter(tf_AccountManagerID);
            }

            TableFilter tf_StageLabel = new TableFilter("labelStage");
            tf_StageLabel.setWidget(new Label("lbFilterStage", "<span class='filterLabelStyle'>&nbsp;"+Application.getInstance().getMessage("sfa.label.stage","Stage")+":</span>"));
            addFilter(tf_StageLabel);

            SelectBox sel_StageID = new SelectBox("sel_StageID");
            sel_StageID.addOption("", "-"+ Application.getInstance().getMessage("sfa.label.all","All")+" -");
            Integer[] stageCode = Opportunity.getOpportunityStage_Code();
            short[]  stagePerc = Opportunity.getOpportunityStage_Percent();
            for (int i=0; i<stageCode.length; i++) {
                sel_StageID.addOption(stageCode[i].toString(), stagePerc[i] + "%");
            }
            if(stage!=""){
            	sel_StageID.setSelectedOption(stage);
            }
            TableFilter tf_StageFilter = new TableFilter("tf_StageFilter");
            tf_StageFilter.setWidget(sel_StageID);
            addFilter(tf_StageFilter);

            TableFilter tf_LabelKW = new TableFilter("labelKeyword");
            tf_LabelKW.setWidget(new Label("lbFilterSearch", "<span class='filterLabelStyle'>&nbsp;"+Application.getInstance().getMessage("sfa.label.search","Search")+":</span>"));
            addFilter(tf_LabelKW);

            TableFilter tf_Keyword = new TableFilter("keyword");
            TextField keywordField = new TextField("keywordField");
            keywordField.setSize("12");
            if(search!=""){
            	keywordField.setValue(search);
            }
            tf_Keyword.setWidget(keywordField);
            addFilter(tf_Keyword);
            if(size!=null){
            	 setPageSize(Integer.parseInt(size));
            }
           
        }
        
        public Collection getTableRows() {
             Application application  = Application.getInstance();
            OpportunityModule module = (OpportunityModule) application.getModule(OpportunityModule.class);
            String  sort = getSort();
            
            //initTable();
            
            if(((CheckBox)getFilter("showclosed").getWidget()).isChecked()){
            	checked=true;
            	//initTable();
                Object[] qParams = getQueryParams();
            	return module.listOpportunities((String)qParams[0], (String)qParams[1], (String)qParams[2], (String)qParams[3], ((Integer)qParams[4]).intValue(),false,false,  (Date)qParams[5], (Date)qParams[6],sort, isDesc(), getStart(), getRows());
            }else{
            	checked=false;
            	//initTable();
            	Object[] qParams = getQueryParams();
                return module.listOpportunities((String)qParams[0], (String)qParams[1], (String)qParams[2], (String)qParams[3], ((Integer)qParams[4]).intValue(),((CheckBox)getFilter("showclosed").getWidget()).isChecked(),((CheckBox)getFilter("showlost").getWidget()).isChecked(), (Date)qParams[5], (Date)qParams[6], sort, isDesc(), getStart(), getRows());
            }
            // firstTime sort

        }

        public int getTotalRowCount() {
            Application application  = Application.getInstance();
            OpportunityModule module = (OpportunityModule) application.getModule(OpportunityModule.class);
            
            if(((CheckBox)getFilter("showclosed").getWidget()).isChecked()){
            	//checked=true;
            	//initTable();
                Object[] qParams = getQueryParams();
            	return module.countOpportunities((String)qParams[0], (String)qParams[1], (String)qParams[2], (String)qParams[3], ((Integer)qParams[4]).intValue(),false,false,  (Date)qParams[5], (Date)qParams[6]);
            }else{
            	//checked=false;
            	//initTable();
            	Object[] qParams = getQueryParams();
                return module.countOpportunities((String)qParams[0], (String)qParams[1], (String)qParams[2], (String)qParams[3], ((Integer)qParams[4]).intValue(),((CheckBox)getFilter("showclosed").getWidget()).isChecked(),((CheckBox)getFilter("showlost").getWidget()).isChecked(), (Date)qParams[5], (Date)qParams[6]);
            }
           
        }

        Object[] getQueryParams() {
        	if(checked==true){
                    Object[] qParams = new Object[7];

                    String stageID = "";
        
                    SelectBox sel_stageID = (SelectBox) MyUtil.getTableFilterWidget(this, "tf_StageFilter");
                    stageID = MyUtil.getSingleValue_SelectBox(sel_stageID);
                    stage = stageID;

                    if((userId== null || "".equals(userId))&&AccessUtil.isSalesManager(getWidgetManager().getUser().getId())) {
                        SelectBox sel_AccountManagerID = (SelectBox) MyUtil.getTableFilterWidget(this, "tf_AccountManagerID");
                        userId = MyUtil.getSingleValue_SelectBox(sel_AccountManagerID);
                    } else if (userId !=null & !"".equals(userId)) {
        				qParams[2] = userId;
        				manager = userId;
        			}

                    String keyword = "";
                    keyword = (String) getFilterValue("keyword");
                    search = keyword;
                    
                    Integer statusFilter = Opportunity.STATUS_CLOSE;
                    //statusFilter = new Integer(-1000);
                    qParams[0] = keyword;
                    qParams[1] = null;
                    if(getFilter("tf_AccountManagerID")==null)
                    {
                        qParams[2] = userId;
                    	manager = userId;
                    }
                    else
                    {
                        SelectBox sel_AccountManagerID = (SelectBox) MyUtil.getTableFilterWidget(this, "tf_AccountManagerID");
                        qParams[2] = MyUtil.getSingleValue_SelectBox(sel_AccountManagerID);
                        manager = MyUtil.getSingleValue_SelectBox(sel_AccountManagerID);
                        
                    }

                    if(((CheckBox)getFilter("showclosed").getWidget()).isChecked())
                    {
                    	closedSale = true;
                    }
                    
                    if(((CheckBox)getFilter("showlost").getWidget()).isChecked()){
                    	lost = true;
                    }
                    
                    qParams[3] = stageID;
                    qParams[4] = statusFilter;
                    qParams[5] = null;
                    qParams[6] = null;

                    if (viewMonthly.isChecked()) {
        				qParams[5] = monthFilter.getFromDate();
                        qParams[6] = monthFilter.getToDate();
                        monthly = true;
                        toDate = monthFilter.getFromDate() ;
                        fromDate =  monthFilter.getToDate();
                    }

                    return qParams;
           
        	}
        	
            Object[] qParams = new Object[7];

            String stageID = "";
            SelectBox sel_stageID = (SelectBox) MyUtil.getTableFilterWidget(this, "tf_StageFilter");
            stageID = MyUtil.getSingleValue_SelectBox(sel_stageID);
            stage = stageID;
            
            if((userId== null || "".equals(userId)) &&AccessUtil.isSalesManager(getWidgetManager().getUser().getId())) {
                SelectBox sel_AccountManagerID = (SelectBox) MyUtil.getTableFilterWidget(this, "tf_AccountManagerID");
                userId = MyUtil.getSingleValue_SelectBox(sel_AccountManagerID);
                manager = userId;
            }

            String keyword = "";
            keyword = (String) getFilterValue("keyword");
            search = keyword;
            
            Integer statusFilter = new Integer(-100);
            //statusFilter = new Integer(-1000);

            qParams[0] = keyword;
            qParams[1] = null;
            if(getFilter("tf_AccountManagerID")==null)
            {
                qParams[2] = userId;
                manager = userId;
            }
            else
            {
                SelectBox sel_AccountManagerID = (SelectBox) MyUtil.getTableFilterWidget(this, "tf_AccountManagerID");
                qParams[2] = MyUtil.getSingleValue_SelectBox(sel_AccountManagerID);
                manager = MyUtil.getSingleValue_SelectBox(sel_AccountManagerID);
            }

            qParams[3] = stageID;
            qParams[4] = statusFilter;
            qParams[5] = null;
            qParams[6] = null;
            if(((CheckBox)getFilter("showclosed").getWidget()).isChecked())
            {
            	closedSale = true;
            }
            if(((CheckBox)getFilter("showlost").getWidget()).isChecked()){
            	lost = true;
            }
            
            if (viewMonthly.isChecked()) {
                qParams[5] = monthFilter.getFromDate();
                qParams[6] = monthFilter.getToDate();
                monthly = true;
                toDate = monthFilter.getFromDate() ;
                fromDate =  monthFilter.getToDate();
            }
            
            
            return qParams;
        }

		
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
		initTable();
	}

	public String getClosed() {
		return closed;
	}

	public void setClosed(String closed) {
		this.closed = closed;
	}

	public boolean isClosedSale() {
		return closedSale;
	}

	public void setClosedSale(boolean closedSale) {
		this.closedSale = closedSale;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public boolean isLost() {
		return lost;
	}

	public void setLost(boolean lost) {
		this.lost = lost;
	}

	public String getManager() {
		return manager;
	}

	public void setManager(String manager) {
		this.manager = manager;
	}

	public boolean isMonthly() {
		return monthly;
	}

	public void setMonthly(boolean monthly) {
		this.monthly = monthly;
	}

	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}

	public String getStage() {
		return stage;
	}

	public void setStage(String stage) {
		this.stage = stage;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}


	public String getSize() {
		return size;
	}


	public void setSize(String size) {
		this.size = size;
	}


	public boolean isMyopportunity() {
		return myopportunity;
	}


	public void setMyopportunity(boolean myopportunity) {
		this.myopportunity = myopportunity;
	}


	/*public String getDefaultTemplate() {
		return "sfa/table";
	}*/
}