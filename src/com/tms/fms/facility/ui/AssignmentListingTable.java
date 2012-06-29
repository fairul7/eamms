package com.tms.fms.facility.ui;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import kacang.Application;
import kacang.model.DaoException;
import kacang.stdui.DatePopupField;
import kacang.stdui.Label;
import kacang.stdui.SelectBox;
import kacang.stdui.Table;
import kacang.stdui.TableColumn;
import kacang.stdui.TableDateFormat;
import kacang.stdui.TableFilter;
import kacang.stdui.TableFormat;
import kacang.stdui.TableModel;
import kacang.stdui.TextField;
import kacang.ui.Event;
import kacang.util.Log;

import org.apache.axis.collections.SequencedHashMap;

import com.tms.crm.sales.misc.DateUtil;
import com.tms.fms.department.model.FMSDepartmentManager;
import com.tms.fms.facility.model.FacilityModule;
import com.tms.fms.facility.model.RateCard;
import com.tms.fms.facility.model.SetupModule;
import com.tms.fms.util.WidgetUtil;

public class AssignmentListingTable extends Table {
	DatePopupField fromDateFilter=new DatePopupField(); 
	DatePopupField toDateFilter=new DatePopupField();
	protected SelectBox sbDepartment;
	
	public AssignmentListingTable() {
	}

	public AssignmentListingTable(String s) {
		super(s);
	}

	public void init() {
		super.init();
		setPageSize(20);
		setModel(new AssignmentListingTableModel());
		setWidth("100%");
	}

	public void onRequest(Event event) {
		setModel(new AssignmentListingTableModel());
	}

	class AssignmentListingTableModel extends TableModel {
		AssignmentListingTableModel() {
			removeChildren();
			Application app = Application.getInstance();
			
			// table columns
			TableColumn requestIdTab = new TableColumn("requestId", app.getMessage("fms.request.label.requestId"));
			requestIdTab.setUrl("requestDetails.jsp?page=all");
			requestIdTab.setUrlParam("requestId");
			addColumn(requestIdTab);
			
			TableColumn title = new TableColumn("requestTitle", app.getMessage("fms.request.label.requestTitle"));
			addColumn(title);
			
			TableColumn firstName = new TableColumn("requestor", app.getMessage("fms.facility.form.requestor"));
			addColumn(firstName);
			
			TableColumn department = new TableColumn("department", app.getMessage("fms.facility.table.department"));
			addColumn(department);
			
			TableColumn requiredFrom = new TableColumn("requiredFrom", app.getMessage("fms.facility.label.requiredFrom"));
			requiredFrom.setFormat(new TableDateFormat(SetupModule.DATE_FORMAT));
			addColumn(requiredFrom);
			
			TableColumn requiredTo = new TableColumn("requiredTo", app.getMessage("fms.facility.label.requiredTo"));
			requiredTo.setFormat(new TableDateFormat(SetupModule.DATE_FORMAT));
			addColumn(requiredTo);
			
			TableColumn items = new TableColumn("requestId", app.getMessage("fms.request.label.requestedItem"), false);
			items.setFormat(new TableFormat(){

				public String format(Object value) {
					FacilityModule module = (FacilityModule) Application.getInstance().getModule(FacilityModule.class);
					SetupModule setupmodule = (SetupModule)Application.getInstance().getModule(SetupModule.class);
										
					String requestId=value.toString();
					int timesTotal  = 0; 
					int linkTotal =0;
					String[] fromTimes = new String[timesTotal];
					Collection facilities = new ArrayList();
					Collection requestItems = new ArrayList();
					
					Collection rc = module.getRateCardIdByRequestId(requestId);
					if (rc != null && rc.size()>0) {
						linkTotal = rc.size();
						fromTimes = new String[linkTotal];
						for (Iterator it = rc.iterator(); it.hasNext();){
							HashMap mrcs = (HashMap) it.next();
							String fromTime = (String) mrcs.get("fromTime");
							if(fromTime != null){
								fromTimes[timesTotal] = fromTime;
								timesTotal++;
							}
							
							if (mrcs.get("rateCardId") != null && mrcs.get("rateCardCategoryId") != null){
								requestItems = setupmodule.getRateCardEquipment((String)mrcs.get("rateCardId"), (String)mrcs.get("rateCardCategoryId"));
								facilities.addAll(requestItems);
							}
							
							//String type = (String)mrcs.get("serviceType");
						}
					}		
					int colSize = 0;
					String resultValue = "";
					try {		
						for (Iterator<RateCard> rateItr=facilities.iterator();rateItr.hasNext();) {
							RateCard rate = (RateCard) rateItr.next();
							int equipmentQty = 0;
							if ("".equals(resultValue)){								
								equipmentQty = setupmodule.getTotalFacility(requestId, fromTimes[colSize], rate.getCategoryId(), false);
								resultValue=rate.getEquipment()+"("+equipmentQty+")";
								colSize++;			
							} else {								
								equipmentQty = setupmodule.getTotalFacility(requestId, fromTimes[colSize], rate.getCategoryId(), false);
								resultValue+=",<br>" + rate.getEquipment()+"("+equipmentQty+")";
								colSize++;			
							}
						}
					}catch (Exception e) {
						Log.getLog(getClass()).error(e.getMessage(), e);
					}
					
					return resultValue;
				}
			}
			);
			addColumn(items);
			
			TableColumn indicator = new TableColumn("requestId", "");
			indicator.setFormat(new TableFormat(){

				public String format(Object value) {
					FacilityModule module = (FacilityModule) Application.getInstance().getModule(FacilityModule.class);
					String requestId=value.toString();					
					String indicator="";					
					
					Collection rc = module.getRateCardIdByRequestId(requestId);
					if (rc != null && rc.size()>0) {
						for (Iterator it = rc.iterator(); it.hasNext();){
							HashMap mrcs = (HashMap) it.next();
							if (mrcs.get("utilized") == null || "".equals(mrcs.get("utilized"))){
								indicator = "<span align=\"center\"><img align=\"center\" src=\"/ekms/images/icn_check.png\"></span><br>";
							}else{
								indicator = "<span align=\"center\"><img align=\"center\" src=\"/ekms/images/icn_delete.png\"></span><br>";
							}
							break;
						}						
						
					}					
					return indicator;
				}
			}
			);
			addColumn(indicator);
			
			TableFilter tfSearchBy = new TableFilter("searchBy");
			TextField tfSearchText = new TextField("tfSearchText");
			tfSearchText.setSize("20");
			tfSearchBy.setWidget(tfSearchText);
			addFilter(tfSearchBy);
			
			TableFilter label1=new TableFilter("fromDateLabel");
			label1.setWidget(new Label("fromDate1",Application.getInstance().getMessage("fms.facility.label.fromDate")));
			addFilter(label1);
			
			TableFilter tfFromDate=new TableFilter("fromDateFilter");
			
			fromDateFilter.setOptional(true);
			fromDateFilter.setTemplate("extDatePopupField");
			fromDateFilter.setFormat("dd-MM-yyyy");
			fromDateFilter.setSize("10");
			
			Date from = DateUtil.getToday();
			int dayOfWeek = DateUtil.getDatePart(from, Calendar.DAY_OF_WEEK);
			int offset;
			if (dayOfWeek == Calendar.SUNDAY) {
				offset = -6;
			} else {
				offset = 2 - dayOfWeek;
			}
			from = DateUtil.dateAdd(from, Calendar.DATE, offset);
			Date to = DateUtil.dateAdd(from, Calendar.DATE, 6);
			
			fromDateFilter.setDate(from);
			tfFromDate.setWidget(fromDateFilter);
			addFilter(tfFromDate);
			
			TableFilter label2=new TableFilter("toDateLabel");
			label2.setWidget(new Label("toDate1", Application.getInstance().getMessage("fms.facility.label.toDate")));
			addFilter(label2);
			
			TableFilter tfEndDate=new TableFilter("toDateFilter");
			
			toDateFilter.setOptional(true);
			toDateFilter.setTemplate("extDatePopupField");
			toDateFilter.setFormat("dd-MM-yyyy");
			toDateFilter.setSize("10");
			toDateFilter.setDate(to);
			tfEndDate.setWidget(toDateFilter);
			addFilter(tfEndDate);
			
			
			FMSDepartmentManager deptManager=(FMSDepartmentManager)Application.getInstance().getModule(FMSDepartmentManager.class);
			TableFilter tfDepartment = new TableFilter("department");
			sbDepartment=new SelectBox("sbDepartment");
			SequencedHashMap typeMap=new SequencedHashMap();
			typeMap.put("-1",app.getMessage("fms.facility.table.allDepartment"));
			try {
				typeMap.putAll(deptManager.getFMSDepartments());
			} catch (DaoException e) {
				Log.getLog(getClass()).error(e.getMessage(), e);
			}
			sbDepartment.setOptionMap(typeMap);
			tfDepartment.setWidget(sbDepartment);
			addFilter(tfDepartment);
			
			
		}

		public Collection getTableRows() {
			String searchBy = "";
			searchBy = (String) getFilterValue("searchBy");
			String dept = WidgetUtil.getSbValue(sbDepartment);
			
			Date fromDate=null;
			
			if (fromDateFilter.getDate()!=null) {
				fromDate = (Date) fromDateFilter.getDate();		
			}
			
			Date toDate=null;
			if(toDateFilter.getDate()!=null){
				toDate = (Date) toDateFilter.getDate();
			}
			
			FacilityModule module = (FacilityModule) Application.getInstance().getModule(FacilityModule.class);
			try {
				return module.getRequestListing(searchBy, dept, fromDate, toDate, getSort(), isDesc(), false, getStart(), getRows());
				// only for Today's Assignment: return module.getRequestListingByTime(searchBy,dept, fromDate, toDate, getSort(), isDesc(), false, getStart(), getRows());
			} catch (Exception e) {
				Log.getLog(getClass()).error(e.toString());
				return new ArrayList();
			}
		}

		public int getTotalRowCount() {
			String searchBy = "";
			searchBy = (String) getFilterValue("searchBy");
			String dept = WidgetUtil.getSbValue(sbDepartment);
			FacilityModule module = (FacilityModule) Application.getInstance().getModule(FacilityModule.class);
			
			Date fromDate=null;			
			if (fromDateFilter.getDate()!=null) {
				fromDate = (Date)fromDateFilter.getDate();			
			}
			
			Date toDate=null;
			if(toDateFilter.getDate()!=null){
				toDate = (Date) toDateFilter.getDate();
			}			
			
			return module.countRequestListing(searchBy, dept, fromDate, toDate, false);
		}

		public String getTableRowKey() {
			return "requestId";
		}
	}
}
