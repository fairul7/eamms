package com.tms.hr.claim.ui;

import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.stdui.Form;
import kacang.stdui.SelectBox;
import kacang.stdui.Button;
import kacang.stdui.Label;
import kacang.Application;
import kacang.ui.Event;
import kacang.ui.Forward;

import com.tms.hr.claim.model.ClaimConfig;
import com.tms.hr.claim.model.ClaimConfigModule;
import com.tms.hr.employee.model.EmployeeModule;
import com.tms.hr.employee.model.EmployeeDataObject;
import com.tms.collab.timesheet.TimeSheetUtil;

import java.util.Collection;
import java.util.Iterator;
import java.util.Calendar;

/**
 * Created by IntelliJ IDEA.
 * User: oilai
 * Date: Jul 11, 2005
 * Time: 12:02:50 PM
 * To change this template use File | Settings | File Templates.
 */
public class ClaimReportForm extends Form {
    // main report select form
    protected SelectBox sbReport;
    protected Button bnSubmit;

    // for summary report
    protected SelectBox sbMonth;
    protected SelectBox sbEmployee;

    // for others generic reports
    protected SelectBox sbDepartment;
    protected SelectBox sbCategory;

    protected String selectedMonth="";
    protected String reportType="";
    
    protected static String namespace = "com.tms.hr.claim.ui.ClaimConfigAssessor";

    public void init() {
        setColumns(2);
        populateReportForm();
    }

    public void onRequest(Event ev) {
        if (reportType.equals(""))
            populateReportForm();
        else if (reportType.equals("summary"))
            populateSummaryForm();
        else
            populateGenericForm();
    }

    public void populateReportForm() {
        removeChildren();
        Label lbSelect = new Label("lb1","<div align=\"right\"><b>"+Application.getInstance().getMessage("claims.label.reportType","Report Type")+"</b></div>");
        addChild(lbSelect);
        sbReport = new SelectBox("sbReport");
        sbReport.addOption("",Application.getInstance().getMessage("claims.label.pleaseSelect","Please select"));
        sbReport.addOption("summary",Application.getInstance().getMessage("claims.label.summaryReport","Summary Report"));
        
      
        Application application = Application.getInstance();
        ClaimConfigModule module = (ClaimConfigModule)
                    application.getModule(ClaimConfigModule.class);

        //ge assessors
        Collection colAccessors = module.findObjects(new String[] {
                    " namespace='" + namespace + "' "
                }, (String) "id", false, 0, -1);
        
        boolean activate = false;

        for (Iterator icount = colAccessors.iterator(); icount.hasNext();) {
        	  ClaimConfig object = (ClaimConfig)icount.next();
        	
        	  if(object.getProperty1().equalsIgnoreCase(application.getCurrentUser().getId()))
                  activate= true;
        	
        }
        
        
        if(activate==true)
        sbReport.addOption("history",Application.getInstance().getMessage("claims.label.claimHistory","Claim History"));
        //sbReport.addOption("generic","Others Report");
        sbReport.setSelectedOption("");
        addChild(sbReport);
        
        bnSubmit = new Button("bnSubmitForm",Application.getInstance().getMessage("claims.category.submit","Submit"));
        addChild(new Label("lb2",""));
        addChild(bnSubmit);
    }

    public void populateSummaryForm(){
        removeChildren();

        Label lbMonth = new Label("lb1","<div align=\"right\"><b>"+Application.getInstance().getMessage("claims.label.selectMonth","Select month")+"</b></div>");
        addChild(lbMonth);
        sbMonth=new SelectBox("sbMonth");
        setupMonth();
        addChild(sbMonth);

        Label lbEmployee = new Label("lb2","<div align=\"right\"><b>"+Application.getInstance().getMessage("claims.label.selectEmployee","Select Employee")+"</b></div>");
        addChild(lbEmployee);
        sbEmployee = new SelectBox("sbEmployee");
        sbEmployee.addOption("",Application.getInstance().getMessage("claims.label.all","- ALL -"));
        setupEmployee("");
        sbEmployee.setSelectedOption("");
        addChild(sbEmployee);

        bnSubmit = new Button("bnSubmitSummary",Application.getInstance().getMessage("claims.category.submit","Submit"));
        addChild(new Label("lb3",""));
        addChild(bnSubmit);
    }

    public void setupEmployee(String department) {
        EmployeeModule empMod = (EmployeeModule)Application.getInstance().getModule(EmployeeModule.class);
        try {
            Collection col;
            if (department==null||department.equals(""))
                col = empMod.getAllEmployees();
            else
                col = empMod.getAllDepartmentEmployees(department);
            if (col!=null && col.size()>0) {
                for (Iterator i=col.iterator();i.hasNext();) {
                    EmployeeDataObject obj = (EmployeeDataObject)i.next();
                    sbEmployee.addOption(obj.getEmployeeID(),obj.getFirstName()+" "+obj.getLastName());
                }
            }
        }
        catch(Exception e) {

        }
    }

    public void setupMonth() {
        Calendar calendar = Calendar.getInstance();

        for (int i=0;i<12;i++)  {

            int iMonth = calendar.get(Calendar.MONTH);
            int iYear = calendar.get(Calendar.YEAR);
            String sMonth="";
            if (iMonth<(i)) {
                String year = ""+(iYear-1);
                if (i<9)
                    sMonth="0"+(i+1);
                else
                    sMonth=""+(i+1);
                sbMonth.addOption(year+"-"+sMonth,TimeSheetUtil.getMonthDescription(i+1)+" "+year);
            }
            else {
                String year = ""+iYear;
                if (i<9)
                    sMonth = "0"+(i+1);
                else
                    sMonth = ""+(i+1);
                sbMonth.addOption(year+"-"+sMonth,TimeSheetUtil.getMonthDescription(i+1)+" "+year);
            }
        }

    }

    public void populateGenericForm() {
        removeChildren();


    }

    public Forward onValidate(Event ev) {
        String bt = findButtonClicked(ev);

        if (bt.endsWith("bnSubmitForm")) {
            String sReport = (String)sbReport.getSelectedOptions().keySet().iterator().next();
            if (sReport != null && !sReport.equals("")) {
                if (sReport.equals("summary")) {
                    return new Forward("summary");
                }
                else if(sReport.equals("history")){
                    return new Forward("history");
                }
                else {
                    return new Forward("generic");
                }
            }
            else {
                return new Forward("selectReport");
            }
        }
        else if (bt.endsWith("bnSubmitSummary")) {
            String selectedMonth = (String)sbMonth.getSelectedOptions().keySet().iterator().next();
            String selectedEmployee = (String)sbEmployee.getSelectedOptions().keySet().iterator().next();
            if (selectedMonth==null || selectedMonth.equals("")) {
                return new Forward("selectMonth");
            }
            else {
                String forwardUrl="summaryReport.jsp?month="+selectedMonth;
                if (selectedEmployee==null||selectedEmployee.equals("")) {
                    return new Forward("",forwardUrl,true);
                }
                else {
                    forwardUrl+="&employee="+selectedEmployee;
                    return new Forward("",forwardUrl,true);
                }
            }
        }
        else if (bt.endsWith("bnSubmitGeneric")) {

        }
        return null;
    }

    public void setReportType(String reportType)
    {
        this.reportType=reportType;

    }

    public String getReportType() {
        return reportType;
    }

    public String getSelectedMonth() {
        return selectedMonth;
    }

    public void setSelectedMonth(String selectedMonth) {
        this.selectedMonth = selectedMonth;
    }


}
