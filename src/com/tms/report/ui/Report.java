package com.tms.report.ui;

import kacang.stdui.*;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.Application;
import com.tms.crm.helpdesk.validator.ValidatorNotEquals;
import com.tms.report.model.ReportModule;
import com.tms.report.model.ReportObject;

import java.util.Map;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: oilai
 * Date: Nov 28, 2005
 * Time: 4:50:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class Report extends Form {
    protected DateField startDate;
    protected DateField endDate;

    protected SelectBox reportType;
    protected Button btnSubmit;
    protected Button btnCancel;

    protected String eventType;
    protected Map reportMap;
    protected ReportObject[] reportList;

    protected Date selectedStartDate;
    protected Date selectedEndDate;

    public void init() {
        startDate = new DatePopupField("startDate");
        startDate.addChild(new ValidatorNotEmpty("vne1"));
        addChild(startDate);
        endDate = new DatePopupField("endDate");
        endDate.addChild(new ValidatorNotEmpty("vne2"));
        addChild(endDate);
        reportType = new SelectBox("reportType");
        reportType.addOption("-1","Please select");
        reportType.addOption("0","Users Login Report");
        reportType.addOption("1","Modules Usage Report");
        reportType.addChild(new ValidatorNotEmpty("vne3"));
        ValidatorNotEquals validatorNotEquals = new ValidatorNotEquals("validDivisionNotEquals");
        validatorNotEquals.setCheck("-1");
        reportType.addChild(validatorNotEquals);
        addChild(reportType);

        btnSubmit = new Button("btnSubmit","Submit");
        btnCancel = new Button("btnCancel","Cancel");
        addChild(btnSubmit);
        addChild(btnCancel);

    }

    public void onRequest(Event event) {

    }

    public Forward onValidate(Event event) {
        if (btnSubmit.getAbsoluteName().equals(findButtonClicked(event))) {
            ReportModule mod = (ReportModule) Application.getInstance().getModule(ReportModule.class);

            Date sDate = startDate.getDate();
            Date eDate = endDate.getDate();
            selectedStartDate = sDate;
            selectedEndDate = eDate;
            String rType = (String)reportType.getSelectedOptions().keySet().iterator().next();
            if (rType.equals("0")) {
                eventType="user";
                //reportList = new ReportObject[]{};
                reportList = mod.getReportUserMap(sDate,eDate);
                //reportMap = mod.getReportUserMap(sDate,eDate);

            }
            else if (rType.equals("1")) {
                eventType="module";
                reportList = mod.getReportModuleMap(sDate,eDate);
                //reportMap = mod.getReportModuleMap(sDate,eDate);
            }
            return new Forward("report");
        }
        else if (btnCancel.getAbsoluteName().equals(findButtonClicked(event))) {
            return new Forward("cancel");
        }
        return null;
    }


    public String getDefaultTemplate() {
        return "kb/report";
    }

    public DateField getStartDate() {
        return startDate;
    }

    public void setStartDate(DateField startDate) {
        this.startDate = startDate;
    }

    public DateField getEndDate() {
        return endDate;
    }

    public void setEndDate(DateField endDate) {
        this.endDate = endDate;
    }

    public SelectBox getReportType() {
        return reportType;
    }

    public void setReportType(SelectBox reportType) {
        this.reportType = reportType;
    }

    public Button getBtnSubmit() {
        return btnSubmit;
    }

    public void setBtnSubmit(Button btnSubmit) {
        this.btnSubmit = btnSubmit;
    }

    public Button getBtnCancel() {
        return btnCancel;
    }

    public void setBtnCancel(Button btnCancel) {
        this.btnCancel = btnCancel;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public Map getReportMap() {
        return reportMap;
    }

    public void setReportMap(Map reportMap) {
        this.reportMap = reportMap;
    }

    public ReportObject[] getReportList() {
        return reportList;
    }

    public void setReportList(ReportObject[] reportList) {
        this.reportList = reportList;
    }

    public Date getSelectedStartDate() {
        return selectedStartDate;
    }

    public Date getSelectedEndDate() {
        return selectedEndDate;
    }
}
