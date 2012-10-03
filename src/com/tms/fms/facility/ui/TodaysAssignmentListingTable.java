package com.tms.fms.facility.ui;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import kacang.Application;
import kacang.model.DaoException;
import kacang.stdui.DatePopupField;
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

import com.tms.fms.department.model.FMSDepartmentManager;
import com.tms.fms.facility.model.FacilityModule;
import com.tms.fms.facility.model.RateCard;
import com.tms.fms.facility.model.SetupModule;
import com.tms.fms.util.WidgetUtil;

public class TodaysAssignmentListingTable extends Table{
	DatePopupField fromDateFilter=new DatePopupField(); 
	DatePopupField toDateFilter=new DatePopupField();
	protected SelectBox sbDepartment;
	public TodaysAssignmentListingTable() {
	}

	public TodaysAssignmentListingTable(String s) {
		super(s);
	}

	public void init() {
		super.init();
		setPageSize(20);
		setModel(new TodaysAssignmentListingTableModel());
		setWidth("100%");
	}

	public void onRequest(Event event) {
		setModel(new TodaysAssignmentListingTableModel());
	}

	class TodaysAssignmentListingTableModel extends TableModel {
		TodaysAssignmentListingTableModel() {
			removeChildren();
			Application app = Application.getInstance();
			
			// table columns
			TableColumn requestIdTab = new TableColumn("requestId", app.getMessage("fms.request.label.requestId"));
			requestIdTab.setUrl("requestDetails.jsp?page=today");
			requestIdTab.setUrlParam("requestId");
			addColumn(requestIdTab);
			
			TableColumn title = new TableColumn("requestTitle", app.getMessage("fms.request.label.requestTitle"));
			//title.setUrl("requestDetails.jsp?page=today");
			//title.setUrlParam("requestId");
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
					String resultValue="";
					String requestId=value.toString();
					FacilityModule module = (FacilityModule) Application.getInstance().getModule(FacilityModule.class);
					Collection rc = new ArrayList();
					Collection facilities = new ArrayList();
					Collection requestItems = new ArrayList();
					int timesTotal  = 0; 
					int linkTotal =0;
					int typeTotal=0;
					String[] fromTimes = new String[timesTotal];
					String[] times = new String[timesTotal];
					String[] type = new String[typeTotal];
					
					SetupModule setupmodule = (SetupModule)Application.getInstance().getModule(SetupModule.class);
						rc=module.getRateCardIdByRequestId(requestId);
						
							if(rc != null && rc.size()>0){
								linkTotal = rc.size();
								fromTimes = new String[linkTotal];
								times = new String[linkTotal];
								type = new String[linkTotal];
								for (Iterator it = rc.iterator(); it.hasNext();){
									HashMap mrcs = (HashMap) it.next();
									String fromTime = (String) mrcs.get("fromTime");
									
									if(fromTime != null){
										fromTimes[timesTotal] = fromTime;
										times[timesTotal] = fromTime;
										timesTotal++;
									}
									if(mrcs.get("serviceType") !=null){
										type[typeTotal] = (String)mrcs.get("serviceType");
										typeTotal++;
									}
									if (mrcs.get("rateCardId") != null && mrcs.get("rateCardCategoryId") != null){
										requestItems = setupmodule.getRateCardEquipment((String)mrcs.get("rateCardId"), (String)mrcs.get("rateCardCategoryId"));
										facilities.addAll(requestItems);
										
									}
								}
							}		
							
							for(int i=0; i<times.length; i++){
								String firstHour = times[i].substring(0,2);
								String zone = "";
								int firstHours = Integer.parseInt(firstHour);
								if(firstHours < 12 ){
									zone = "AM";
								}else{
									zone = "PM";
								}
								//---------------------------COMPARE TIME
								SimpleDateFormat sdf=new SimpleDateFormat("hh:mm a");
								
								String timeNow;					        
						        Date date = new Date();					       
						        timeNow = sdf.format(date);
						        
						        //System.out.println("Time : "+timeNow);
								String timesFromRequest = times[i] + " " + zone;
								
								Date d1 = new Date();
								Date d2 = new Date();
								try {
									d1 = sdf.parse(timeNow);
									d2 =sdf.parse(timesFromRequest);
								} catch (ParseException e1) {
									Log.getLog(getClass()).error(e1.toString(), e1);
								}
							   
								
								//System.out.println(firstHour);
								
								
							    long timeNowMs=d1.getTime();
							    long timesMs=d2.getTime();
							    long timesMinus = timeNowMs-timesMs;
								long timeDiff = Math.abs((timeNowMs-timesMs)/60000);
								
								if(timesMinus <0){
									times[i] = times[i] + " " +zone;
								}else{
									if(timeDiff > 360){
										times[i] = "<b><font color=red>" + times[i] + " " +zone + "</font></b>";
									}else{
										times[i] = times[i] + " " +zone;
									}
								}
								
							}
							
							int colSize = 0;
							
							try{		
								for(Iterator<RateCard> rateItr=facilities.iterator();rateItr.hasNext();){
									RateCard rate=(RateCard)rateItr.next();
									int equipmentQty = 0;
									if("".equals(resultValue)){										
										if(type[colSize].equals("1") || type[colSize].equals("6")){											
											//equipmentQty = setupmodule.getTotalFacility(requestId, type[colSize], rate.getId(), true);
											equipmentQty = setupmodule.getTotalFacility(requestId, fromTimes[colSize], rate.getCategoryId(), true);
											resultValue=rate.getEquipment()+"("+equipmentQty+")" + ", " + times[colSize] ;
											colSize++;																					
										}else{
											resultValue=rate.getEquipment()+"("+rate.getEquipmentQty()+")";
											colSize++;
										}
									}else{
										if(type[colSize].equals("1") || type[colSize].equals("6")){							
											equipmentQty = setupmodule.getTotalFacility(requestId, fromTimes[colSize], rate.getCategoryId(), true);
											resultValue+=",<br>" + rate.getEquipment()+"("+equipmentQty+"), " + times[colSize] ;
											colSize++;					
										}else{
											resultValue+=",<br>" + rate.getEquipment()+"("+rate.getEquipmentQty()+")" ;
											colSize++;
										}
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
					SetupModule setupmodule = (SetupModule)Application.getInstance().getModule(SetupModule.class);					
					String requestId=value.toString();					
					Collection facilities = new ArrayList();
					Collection requestItems = new ArrayList();
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
			Collection todayList = new ArrayList();
			FacilityModule module = (FacilityModule) Application.getInstance().getModule(FacilityModule.class);
			try {
				//return module.getRequestListing(searchBy,dept, null, null, getSort(), isDesc(), true, getStart(), getRows());
				todayList = module.getRequestListingByTime(searchBy,dept, null, null, getSort(), isDesc(), true, getStart(), getRows());
			} catch (Exception e) {
				Log.getLog(getClass()).error(e.toString());
			}
			return todayList;
		}

		public int getTotalRowCount() {
			String searchBy = "";
			int count = 0;
			searchBy = (String) getFilterValue("searchBy");
			String dept = WidgetUtil.getSbValue(sbDepartment);
			FacilityModule module = (FacilityModule) Application.getInstance().getModule(FacilityModule.class);
			
			count = module.countRequestListing(searchBy,dept, null, null, true);
			return count;
			
		}

		public String getTableRowKey() {
				return "requestId";
		}

		
	}
}
