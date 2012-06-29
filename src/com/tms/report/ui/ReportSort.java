package com.tms.report.ui;

import kacang.ui.Event;
import kacang.Application;
import kacang.util.Log;

import java.util.Date;

import com.tms.report.model.ReportModule;
import com.tms.report.model.ReportObject;


public class ReportSort extends Report {

    protected String sortBy;
    protected String preSelectStartDate;
    protected String preSelectEndDate;

    public void init() {
       if (eventType!=null) {

       }
        else {
           super.init();
       }
    }

    public void onRequest(Event ev) {
        ReportModule mod = (ReportModule) Application.getInstance().getModule(ReportModule.class);

        Date sDate = new Date(preSelectStartDate);
        Date eDate = new Date(preSelectEndDate);
        selectedStartDate = sDate;
        selectedEndDate = eDate;
        if (eventType.equals("user")) {
            reportList = mod.getReportUserMap(sDate,eDate);
            if (sortBy.equals("total")) {
                ReportObject[] temp=new ReportObject[1];
                ReportObject[] actual=new ReportObject[1];
                actual[0] = reportList[0];
                for (int i=0; i<reportList.length; i++) {
                    //Log.getLog(getClass()).info("reportList["+i+"]="+reportList[i].getUser().getUsername()+" getTotalLogin()="+reportList[i].getTotalLogin());
                    temp = new ReportObject[i+1];
                    int k=0;
                    int j=0;
                    boolean b=false;
                    for (j=0;j<(i+1);j++) {
                            if (k>=actual.length || reportList[i].getTotalLogin()<=actual[k].getTotalLogin()) {
                                temp[j]=reportList[i];
                                b=true;
                                j++;
                                if (j<(i+1))
                                for (int p=k;p<actual.length;p++) {
                                    temp[j]=actual[p];
                                    j++;
                                }
                            }
                            else {
                                temp[j]=actual[k];
                                k++;
                            }
                    }
                    if (!b) {
                        temp[j] = reportList[i];
                    }
                    actual = new ReportObject[temp.length];
                    actual = temp;
                }
                reportList = new ReportObject[actual.length];
                reportList = actual;

            }
        }
        else {
            reportList = mod.getReportModuleMap(sDate,eDate);
            if (sortBy.equals("total")) {
                ReportObject[] temp=new ReportObject[1];
                ReportObject[] actual=new ReportObject[1];
                actual[0] = reportList[0];
                for (int i=0; i<reportList.length; i++) {
                    temp = new ReportObject[i+1];
                    boolean b = false;
                    int k=0;
                    int j=0;
                    for (j=0;j<actual.length;j++) {
                        if (k>=actual.length || reportList[i].getTotalCount()<=actual[k].getTotalCount()) {
                            temp[j]=reportList[i];
                            b=true;
                            j++;
                            if (j<(i+1))
                            for (int p=k;p<actual.length;p++) {
                                temp[j]=actual[p];
                                j++;
                            }
                        }
                        else {
                            temp[j]=actual[k];
                            k++;
                        }
                    }
                    if (!b) {
                        temp[j] = reportList[i];
                    }
                    actual = new ReportObject[temp.length];
                    actual = temp;
                }
                reportList = new ReportObject[actual.length];
                reportList = actual;
            }
            if (sortBy.equals("unique")) {
                ReportObject[] temp=new ReportObject[1];
                ReportObject[] actual=new ReportObject[1];
                actual[0] = reportList[0];
                for (int i=0; i<reportList.length; i++) {
                    temp = new ReportObject[i+1];
                    boolean b = false;
                    int k=0;
                    int j=0;
                    for (j=0;j<actual.length;j++) {
                        if (k>=actual.length || reportList[i].getUniqueCount()<=actual[k].getUniqueCount()) {
                            temp[j]=reportList[i];
                            b=true;
                            j++;
                            if (j<(i+1))
                            for (int p=k;p<actual.length;p++) {
                                temp[j]=actual[p];
                                j++;
                            }
                        }
                        else {
                            temp[j]=actual[k];
                            k++;
                        }
                    }
                    if (!b) {
                        temp[j] = reportList[i];
                    }
                    actual = new ReportObject[temp.length];
                    actual = temp;
                }
                reportList = new ReportObject[actual.length];
                reportList = actual;
            }

        }
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public String getPreSelectStartDate() {
        return preSelectStartDate;
    }

    public void setPreSelectStartDate(String preSelectStartDate) {
        this.preSelectStartDate = preSelectStartDate;
    }

    public String getPreSelectEndDate() {
        return preSelectEndDate;
    }

    public void setPreSelectEndDate(String preSelectEndDate) {
        this.preSelectEndDate = preSelectEndDate;
    }
}
