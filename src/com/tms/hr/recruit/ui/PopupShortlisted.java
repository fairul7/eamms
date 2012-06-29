package com.tms.hr.recruit.ui;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import kacang.Application;
import kacang.stdui.DatePopupField;
import kacang.stdui.PopupSelectBox;
import kacang.stdui.PopupSelectBoxTable;
import kacang.stdui.PopupSelectBoxTableModel;
import kacang.stdui.SelectBox;
import kacang.stdui.Table;
import kacang.stdui.TableAction;
import kacang.stdui.TableColumn;
import kacang.stdui.TableFilter;

import org.apache.axis.collections.SequencedHashMap;

import com.tms.hr.recruit.model.ApplicantObj;
import com.tms.hr.recruit.model.RecruitAppModule;
import com.tms.hr.recruit.model.RecruitModule;
import com.tms.hr.recruit.model.VacancyObj;

public class PopupShortlisted extends PopupSelectBox{
	
	public PopupShortlisted(){
		  super();
	}
	
	public PopupShortlisted(String name){
		 super(name);
	}
	
	protected Table initPopupTable() {
        return new PopupShortlistedTable();
    }
	
	protected Map generateOptionMap(String[] ids) {
        Map itemsMap = new SequencedHashMap();
        if (ids == null || ids.length == 0) {
            return itemsMap;
        }

        Application app = Application.getInstance();
        RecruitAppModule ram = (RecruitAppModule) app.getModule(RecruitAppModule.class);
        Collection userList = ram.loadShortlisted(ids, "Short-Listed,Another Interview");
        
        // build users map
        Map tmpMap = new SequencedHashMap();
        for (Iterator i=userList.iterator(); i.hasNext();) {
             ApplicantObj applicantObj = (ApplicantObj)i.next();
             tmpMap.put(applicantObj.getApplicantId(), applicantObj.getName());
        }

        // sort
        for (int j=0; j<ids.length; j++) {
             String name = (String)tmpMap.get(ids[j]);
             if (name == null) {
                 name = "---";
             }
             itemsMap.put(ids[j], name);
        }
       
        return itemsMap;
    }
	
	public class PopupShortlistedTable extends PopupSelectBoxTable{
		public PopupShortlistedTable() {
        }

        public PopupShortlistedTable(String name) {
            super(name);
        }

        public void init() {
            super.init();
            setWidth("100%");
            setModel(new PopupShortlistedTableModel());
        }
        
        public class PopupShortlistedTableModel extends PopupSelectBoxTableModel {
        	public PopupShortlistedTableModel() {
                super();
                Application app = Application.getInstance();
                RecruitModule rm = (RecruitModule) app.getModule(RecruitModule.class);
                TableColumn vacancyCode = new TableColumn("vacancyCode", app.getMessage("recruit.general.label.vacancyCode"));
    			addColumn(vacancyCode);
                
    			TableColumn applicantName = new TableColumn("name", app.getMessage("recruit.general.label.applicant"));
    			addColumn(applicantName);
    			
    			TableColumn highestEduLevel = new TableColumn("highEduLevelDesc", app.getMessage("recruit.general.label.highestEduLevel"));
    			addColumn(highestEduLevel);
    			
    			TableColumn courseTitle = new TableColumn("courseTitle", app.getMessage("recruit.general.label.courseTitle"));
    			addColumn(courseTitle);
    			
    			TableColumn grade = new TableColumn("grade", app.getMessage("recruit.general.label.grade"));
    			addColumn(grade);
    			
    			TableColumn workingExp = new TableColumn("yearOfWorkingExp", app.getMessage("recruit.general.label.workingExp"));
    			addColumn(workingExp);
    			
    			TableColumn dateApplied = new TableColumn("dateApplied", app.getMessage("recruit.general.label.dateApplied"));
    			addColumn(dateApplied);

    			// filter textbox 
    			addFilter(new TableFilter("applicantFilter"));
    			
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
    			
    			//button
    			addAction(new TableAction(FORWARD_SELECT,  app.getMessage("general.label.select", "Select")));
            }
        	
        	public Collection getTableRows() {
        		String strVacancyCode="";
    		
    			String applicantMisc = (String)getFilterValue("applicantFilter");
    			String startDate = (String) getFilterValue("startDateFilter");
    			String endDate = (String) getFilterValue("endDateFilter");
    			
    			Application app = Application.getInstance();  
    			RecruitAppModule ram = (RecruitAppModule) app.getModule(RecruitAppModule.class);
    			
    			return ram.findAllApplicantSpecial("", getSort(), isDesc(), getStart(), getRows(), applicantMisc,  strVacancyCode, startDate, endDate);
    		}
    		
    		public int getTotalRowCount() {
    			String strVacancyCode="";
    			
    			String applicantMisc = (String)getFilterValue("applicantFilter");
    			String startDate = (String) getFilterValue("startDateFilter");
    			String endDate = (String) getFilterValue("endDateFilter");
    			
    			Application app = Application.getInstance();  
    			RecruitAppModule ram = (RecruitAppModule) app.getModule(RecruitAppModule.class);
    			return ram.countAllApplicantSpecial("",applicantMisc, strVacancyCode, startDate, endDate); 
    		}
        	
            public String getTableRowKey() {
                return "applicantId";
            }
    		
        }
        
	}
	
}
