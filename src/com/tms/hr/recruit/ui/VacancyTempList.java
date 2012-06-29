package com.tms.hr.recruit.ui;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import kacang.Application;
import kacang.stdui.SelectBox;
import kacang.stdui.Table;
import kacang.stdui.TableAction;
import kacang.stdui.TableColumn;
import kacang.stdui.TableDateFormat;
import kacang.stdui.TableFilter;
import kacang.stdui.TableFormat;
import kacang.stdui.TableModel;
import kacang.ui.Event;
import kacang.ui.Forward;

import com.tms.hr.orgChart.model.OrgChartHandler;
import com.tms.hr.orgChart.model.OrgSetup;
import com.tms.hr.recruit.model.RecruitModule;
import com.tms.hr.recruit.model.VacancyObj;

public class VacancyTempList extends Table{
		
	public void init(){
		setWidth("100%");
		setModel(new VacancyTempListModel());
	}
	
	public class VacancyTempListModel extends TableModel{
		//from org chart
		private Map countries = new LinkedHashMap();
        private Map titles = new LinkedHashMap();
        private Map stations = new LinkedHashMap();
        private Map depts = new LinkedHashMap();
        
		public VacancyTempListModel(){
			Application app = Application.getInstance();
			TableColumn vacancyTempCode = new TableColumn("vacancyTempCode", app.getMessage("recruit.general.label.vacancyTempCode"));
			vacancyTempCode.setUrlParam("vacancyTempCode");
            addColumn(vacancyTempCode);
            TableColumn position = new TableColumn("positionDesc", app.getMessage("recruit.general.label.position"));
            addColumn(position);
            TableColumn country = new TableColumn("countryDesc", app.getMessage("recruit.general.label.country"));
            addColumn(country);
            TableColumn dept = new TableColumn("departmentDesc", app.getMessage("recruit.general.label.department"));
            addColumn(dept);
            TableColumn dateCreated = new TableColumn("createdDate", app.getMessage("recruit.general.label.dateCreated"));
            TableFormat dateCreatedFormat = new TableDateFormat(app.getProperty("globalDatetimeLong"));
            dateCreated.setFormat(dateCreatedFormat);
            addColumn(dateCreated);
            
            addFilter(new TableFilter("codeFilter"));
            
            addAction(new TableAction("add", app.getMessage("recruit.general.label.add")));
            addAction(new TableAction("delete", app.getMessage("recruit.general.label.delete"), app.getMessage("recruit.vacancyTemp.alert.delete")));
            
            // init OrgSetup Objects - from org chart
            OrgChartHandler oc = (OrgChartHandler) app.getModule(OrgChartHandler.class);
            Collection countriesCol = oc.findAllSetup(OrgChartHandler.TYPE_COUNTRY);
            Collection titlesCol = oc.findAllSetup(OrgChartHandler.TYPE_TITLE);
            Collection deptsCol = oc.findAllSetup(OrgChartHandler.TYPE_DEPT);

            countries.put("---", app.getMessage("general.hierachy.selectCountry"));
            for(Iterator itr = countriesCol.iterator(); itr.hasNext();){
                OrgSetup obj = (OrgSetup) itr.next();
                countries.put(obj.getCode(), obj.getShortDesc());
            }

            titles.put("---", app.getMessage("recruit.general.hierachy.selectPosition"));
            for(Iterator itr = titlesCol.iterator(); itr.hasNext();){
                OrgSetup obj = (OrgSetup) itr.next();
                titles.put(obj.getCode(), obj.getShortDesc());
            }
            
            depts.put("---", app.getMessage("general.hierachy.selectDept"));
            for(Iterator itr = deptsCol.iterator(); itr.hasNext();){
                OrgSetup obj = (OrgSetup) itr.next();
                depts.put(obj.getCode(), obj.getShortDesc());
            }

            TableFilter countryFilter = new TableFilter("countryFilter");
            SelectBox countrySel = new SelectBox("countrySel", countries, null, false, 1);
            countryFilter.setWidget(countrySel);
            addFilter(countryFilter);

            TableFilter deptFilter = new TableFilter("deptFilter");
            SelectBox deptSel = new SelectBox("deptSel", depts, null, false, 1);
            deptFilter.setWidget(deptSel);
            addFilter(deptFilter);

            TableFilter titleFilter = new TableFilter("titleFilter");
            SelectBox titleSel = new SelectBox("titleSel", titles, null, false, 1);
            titleFilter.setWidget(titleSel);
            addFilter(titleFilter);
            //end of getting org chart info
		}
		
		  public Collection getTableRows() {
			  //get org chart
			  List selectedCountry = (List) getFilterValue("countryFilter");
	          List selectedDept = (List) getFilterValue("deptFilter");
	          List selectedTitle = (List) getFilterValue("titleFilter");
	          String strCountry = null;
	          String strDept = null;
	          String strTitle = null;
			  
	          if(selectedCountry != null && selectedCountry.size() > 0){
	                strCountry = selectedCountry.get(0).toString().startsWith("---")? null : selectedCountry.get(0).toString();
	          }

	          if(selectedDept != null && selectedDept.size() > 0){
	                strDept = selectedDept.get(0).toString().startsWith("---")? null : selectedDept.get(0).toString();
	          }

	          if(selectedTitle !=null && selectedTitle.size() > 0){
	                strTitle = selectedTitle.get(0).toString().startsWith("---")? null : selectedTitle.get(0).toString();
	          }
	          //end org chart
	         
	          String vacancyTempCode = (String) getFilterValue("codeFilter");
			  Application app = Application.getInstance();
			  
			  RecruitModule rm = (RecruitModule) app.getModule(RecruitModule.class);
			  return rm.findAllVacancyTempCodeFilter(vacancyTempCode,getSort(), isDesc(),getStart(),getRows(), strCountry, strDept, strTitle);
		  }
		  
		  public int getTotalRowCount() {
			  //get org chart
			  List selectedCountry = (List) getFilterValue("countryFilter");
	          List selectedDept = (List) getFilterValue("deptFilter");
	          List selectedTitle = (List) getFilterValue("titleFilter");
	          String strCountry = null;
	          String strDept = null;
	          String strTitle = null;
			  
	          if(selectedCountry != null && selectedCountry.size() > 0){
	                strCountry = selectedCountry.get(0).toString().startsWith("---")? null : selectedCountry.get(0).toString();
	          }

	          if(selectedDept != null && selectedDept.size() > 0){
	                strDept = selectedDept.get(0).toString().startsWith("---")? null : selectedDept.get(0).toString();
	          }

	          if(selectedTitle !=null && selectedTitle.size() > 0){
	                strTitle = selectedTitle.get(0).toString().startsWith("---")? null : selectedTitle.get(0).toString();
	          }
	          //end org chart
	          
			  String vacancyTempCode = null;
			  
			  List selectedCode = (List) getFilterValue("codeFilter");
			  if(selectedCode != null && selectedCode.size() > 0){
				  vacancyTempCode = selectedCode.get(0).toString().equals("") ? null : selectedCode.get(0).toString(); 
			  }
			  
			  Application app = Application.getInstance();
			  RecruitModule rm = (RecruitModule) app.getModule(RecruitModule.class);
			  
			  return rm.countAllVacancyTemp(vacancyTempCode, strCountry, strDept, strTitle);
		  }
		  
	      public String getTableRowKey() {
	            return "vacancyTempCode";
	      }
	      
	      public Forward processAction(Event evt, String action, String[] selectedKeys) {
	    	  Application app = Application.getInstance();
	    	  if("add".equals(action)){
	    		  return new Forward("add");
	    	  }else if("delete".equals(action)){
	    		  RecruitModule rm = (RecruitModule) app.getModule(RecruitModule.class); 
	    		  //auditObj
	 			  VacancyObj auditObj = new VacancyObj();
	 			  String actionTaken="";
	    		  for(int i=0; i<selectedKeys.length; i++){
	    			  //audit
				  	  actionTaken="Delete Vacancy Template";
					  auditObj.setAndInsertAudit(selectedKeys[i], "", actionTaken);
	    			  rm.deleteVacancyTemp(selectedKeys[i]);
	    		  }
	    		  return new Forward("delete");
	    	  }
	    	  return new Forward("error");
	      }
	}
	
	
}






























