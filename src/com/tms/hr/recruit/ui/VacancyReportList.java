package com.tms.hr.recruit.ui;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import kacang.Application;
import kacang.stdui.DatePopupField;
import kacang.stdui.Label;
import kacang.stdui.SelectBox;
import kacang.stdui.Table;
import kacang.stdui.TableColumn;
import kacang.stdui.TableFilter;
import kacang.stdui.TableModel;

import com.tms.hr.orgChart.model.OrgChartHandler;
import com.tms.hr.orgChart.model.OrgSetup;
import com.tms.hr.recruit.model.RecruitAppModule;
import com.tms.hr.recruit.model.RecruitModule;

public class VacancyReportList extends Table{
	
	public void init(){
		setWidth("100%");
		setModel(new VacancyReportListModel());
	}
	
	public class VacancyReportListModel extends TableModel{
		private Map countries = new LinkedHashMap();
	    private Map positions = new LinkedHashMap();
	    private Map depts = new LinkedHashMap();
	    
		public VacancyReportListModel(){
			Application app = Application.getInstance();
			TableColumn vacancyCode = new TableColumn("vacancyCode", app.getMessage("recruit.general.label.vacancyCode"));
			vacancyCode.setUrlParam("vacancyCode");
			addColumn(vacancyCode);
			
			TableColumn positionApplied = new TableColumn("positionDesc", app.getMessage("recruit.general.label.position"));
			addColumn(positionApplied);
			
			TableColumn country = new TableColumn("countryDesc", app.getMessage("recruit.general.label.country"));
			addColumn(country);
			
			TableColumn department = new TableColumn("departmentDesc", app.getMessage("recruit.general.label.department"));
			addColumn(department);
			
			TableColumn vacancyStartDate = new TableColumn("startDate", app.getMessage("recruit.general.label.vacancyStartDate"));
			addColumn(vacancyStartDate);
			
			TableColumn vacancyEndDate = new TableColumn("endDate", app.getMessage("recruit.general.label.vacancyEndDate"));
			addColumn(vacancyEndDate);
			
			TableColumn totalApplied = new TableColumn("totalApplied", app.getMessage("recruit.general.label.totalApplied"));
			addColumn(totalApplied);
			
			TableColumn totalBlackListed = new TableColumn("totalBlackListed", app.getMessage("recruit.general.label.totalBlackListed"));
			addColumn(totalBlackListed);
			
			TableColumn totalJobAccepted = new TableColumn("totalJobAccepted", app.getMessage("recruit.general.label.totalJobAccepted"));
			addColumn(totalJobAccepted);
			
			TableColumn totalJobRejected = new TableColumn("totalJobRejected", app.getMessage("recruit.general.label.totalJobRejected"));
			addColumn(totalJobRejected);
			
			//filter textbox 
			addFilter(new TableFilter("vacancyFilter"));
			
			//filter selectbox
			OrgChartHandler oc = (OrgChartHandler) app.getModule(OrgChartHandler.class);
	        Collection countriesCol = oc.findAllSetup(OrgChartHandler.TYPE_COUNTRY, null, 0, -1, "shortDesc", false, true);
	        Collection positionsCol = oc.findAllSetup(OrgChartHandler.TYPE_TITLE, null, 0, -1, "shortDesc", false, true);
	        Collection deptsCol = oc.findAllSetup(OrgChartHandler.TYPE_DEPT, null, 0, -1, "shortDesc", false, true);

	         positions.put("---", app.getMessage("recruit.general.hierachy.selectPosition"));
	         for(Iterator itr = positionsCol.iterator(); itr.hasNext();){
	             OrgSetup obj = (OrgSetup) itr.next();
	             positions.put(obj.getCode(), obj.getShortDesc());
	         }
	         
	         countries.put("---", app.getMessage("recruit.general.hierachy.selectCountry"));
	         for(Iterator itr = countriesCol.iterator(); itr.hasNext();){
	             OrgSetup obj = (OrgSetup) itr.next();
	             countries.put(obj.getCode(), obj.getShortDesc());
	         }
	         
	         depts.put("---", app.getMessage("recruit.general.hierachy.selectDept"));
	         for(Iterator itr = deptsCol.iterator(); itr.hasNext();){
	             OrgSetup obj = (OrgSetup) itr.next();
	             depts.put(obj.getCode(), obj.getShortDesc());
	         }
			
	        TableFilter positionFilter = new TableFilter("positionFilter");
	        TableFilter countryFilter = new TableFilter("countryFilter");
	        TableFilter departmentFilter = new TableFilter("departmentFilter");
			
	        SelectBox positionSb = new SelectBox("positionSb", positions, null, false, 1);
	        SelectBox countrySb = new SelectBox("countrySb", countries, null, false, 1);
	        SelectBox departmentSb = new SelectBox("departmentSb", depts, null, false, 1);
	        
	        positionFilter.setWidget(positionSb);
	        countryFilter.setWidget(countrySb);
	        departmentFilter.setWidget(departmentSb);
	        
	        addFilter(positionFilter);
	        addFilter(countryFilter);
	        addFilter(departmentFilter);
	        
	        //add br-newLinse
			TableFilter lblbr = new TableFilter("lblbr");
			Label lblSpace = new Label("lblSpace","<br />");
			lblbr.setWidget(lblSpace);
			addFilter(lblbr);
	        
			//filter start date
			TableFilter startDateFilter = new TableFilter("startDateFilter");
			DatePopupField startDate = new DatePopupField("startDate");
			startDate.setOptional(true);
			startDateFilter.setWidget(startDate);
			addFilter(startDateFilter);
			
			//filter endDate
			TableFilter endDateFilter = new TableFilter("endDateFilter");
			DatePopupField endDate = new DatePopupField("endDate");
			endDate.setOptional(true);
			endDateFilter.setWidget(endDate);
			addFilter(endDateFilter);
		}
		
		public Collection getTableRows() {
			String strPosition="";
			String strCountry="";
			String strDepartment="";
			
			List selectedPosition = (List)getFilterValue("positionFilter");
			if(selectedPosition!=null && selectedPosition.size() > 0){
				strPosition = selectedPosition.get(0).toString().startsWith("---")? null : selectedPosition.get(0).toString();
			}
			
			List selectedCountry = (List)getFilterValue("countryFilter");
			if(selectedCountry!=null && selectedCountry.size() > 0){
				strCountry = selectedCountry.get(0).toString().startsWith("---")? null : selectedCountry.get(0).toString();
			}
			
			List selectedDepartment = (List)getFilterValue("departmentFilter");
			if(selectedDepartment!=null && selectedDepartment.size() > 0){
				strDepartment = selectedDepartment.get(0).toString().startsWith("---")? null : selectedDepartment.get(0).toString();
			}
			
			String vacancyMisc = (String)getFilterValue("vacancyFilter");
			String startDate = (String) getFilterValue("startDateFilter");
			String endDate = (String) getFilterValue("endDateFilter");
			
			Application app = Application.getInstance();  
			RecruitModule rm = (RecruitModule) app.getModule(RecruitModule.class);
			
			return rm.findAllVacancyTotal(getSort(), isDesc(), getStart(), getRows(), strPosition, strCountry, strDepartment, vacancyMisc, startDate, endDate, "");
		}
		
		public int getTotalRowCount() {
			String strPosition="";
			String strCountry="";
			String strDepartment="";
			
			List selectedPosition = (List)getFilterValue("positionFilter");
			if(selectedPosition!=null && selectedPosition.size() > 0){
				strPosition = selectedPosition.get(0).toString().startsWith("---")? null : selectedPosition.get(0).toString();
			}
			
			List selectedCountry = (List)getFilterValue("countryFilter");
			if(selectedCountry!=null && selectedCountry.size() > 0){
				strCountry = selectedCountry.get(0).toString().startsWith("---")? null : selectedCountry.get(0).toString();
			}
			
			List selectedDepartment = (List)getFilterValue("departmentFilter");
			if(selectedDepartment!=null && selectedDepartment.size() > 0){
				strDepartment = selectedDepartment.get(0).toString().startsWith("---")? null : selectedDepartment.get(0).toString();
			}
			
			String vacancyMisc = (String)getFilterValue("vacancyFilter");
			String startDate = (String) getFilterValue("startDateFilter");
			String endDate = (String) getFilterValue("endDateFilter");
			
			Application app = Application.getInstance();  
			RecruitModule rm = (RecruitModule) app.getModule(RecruitModule.class);
			
			return rm.countAllVacancyTotal(strPosition, strCountry, strDepartment, vacancyMisc, startDate, endDate, "");
		}
		
		public String getTableRowKey() {
			return "";
		}
		
		/*public Forward processAction(Event evt, String action, String[] selectedKeys) {
			
		}*/
	}
	
}
