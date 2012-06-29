/**
 * @author Vincent Lee, Wavelet Solutions Sdn Bhd (632468-W)
 * Created On: 2004-07-08
 * Copyright of The Media Shoppe Berhad
 */


package com.tms.hr.claim.ui;

import com.tms.hr.claim.model.ClaimFormIndex;
import com.tms.hr.claim.model.ClaimFormIndexModule;
import com.tms.hr.claim.model.ClaimConfigModule;
import com.tms.hr.claim.model.ClaimConfig;
import kacang.Application;
import kacang.services.security.SecurityException;
import kacang.stdui.*;
import kacang.ui.Event;
import kacang.ui.Forward;
import org.apache.commons.collections.SequencedHashMap;

import java.util.*;


public class ClaimFormIndexActionForm extends Form {

    protected TextField tf_words;
    protected Button bn_Submit1;
    protected Button bn_Submit2;
    protected SelectBox sbxMonth, sbxYear;
    protected static String namespace = "com.tms.hr.claim.ui.ClaimConfigAssessor";
    protected String [] assessors;

    String formId = null;

    public String getId() {
        return this.formId;
    }

    public void setId(String formId) {
        this.formId = formId;
    }

    public void init() {
        setMethod("POST");
    }

    public void onRequest(Event evt) {
        removeChildren();
        Application application = Application.getInstance();
        ClaimFormIndexModule module = (ClaimFormIndexModule)
        application.getModule(ClaimFormIndexModule.class);

        ClaimFormIndex theObj = module.selectObject(getId());
        String thisUserId = getWidgetManager().getUser().getId();

        /// if null, return right away
        if (theObj == null || thisUserId == null) return;
        if ((theObj.getUserOriginator().equals(thisUserId) ||
        theObj.getUserOwner().equals(thisUserId))
        && theObj.getState().equals(ClaimFormIndexModule.STATE_CREATED)) {
            setColumns(1);
            bn_Submit1 = new Button("submit", Application.getInstance().getMessage("claims.label.submitforapprover","Submit For Approval"));
            addChild(bn_Submit1);

        }
        if ((theObj.getUserApprover1().equals(thisUserId)
        || theObj.getUserApprover2().equals(thisUserId)
        || theObj.getUserApprover3().equals(thisUserId)
        || theObj.getUserApprover4().equals(thisUserId)
        )
        && theObj.getState().equals(ClaimFormIndexModule.STATE_SUBMITTED)) {
            setColumns(1);
            bn_Submit1 = new Button("approve", Application.getInstance().getMessage("claims.label.approve","Approve"));
            bn_Submit2 = new Button("reject", Application.getInstance().getMessage("claims.label.reject","Reject"));
            tf_words = new TextField("tf_words");
            tf_words.setValue(Application.getInstance().getMessage("claims.label.reject.message","If Reject, Please Indicate The Reasons Here"));
            Panel p = new Panel("panel");
            p.setAlign("center");
            p.addChild(bn_Submit1);
            p.addChild(bn_Submit2);
            addChild(p);
        }

        if (theObj.getState().equals(ClaimFormIndexModule.STATE_APPROVED)) {

            boolean isAssessor =false;

            setAssessors();
            for(int i=0 ; i < assessors.length ; i++){

                if(assessors[i].equals(thisUserId)) isAssessor=true;
            }


            //if (( !theObj.getUserOriginator().equals(thisUserId) && !theObj.getUserOwner().equals(thisUserId) )  || isAssessor     )
              if(isAssessor)
                makeAssessButtons();
        }

        if ((theObj.getUserOriginator().equals(thisUserId) ||
        theObj.getUserOwner().equals(thisUserId))
        && theObj.getState().equals(ClaimFormIndexModule.STATE_REJECTED)) {
            setColumns(1);
            bn_Submit1 = new Button("resub", Application.getInstance().getMessage("claims.label.resubmit","Resubmit"));
            addChild(bn_Submit1);
        }

        if ((theObj.getUserOriginator().equals(thisUserId) ||
        theObj.getUserOwner().equals(thisUserId))
        && theObj.getState().equals(ClaimFormIndexModule.STATE_ASSESSED)) {
            setColumns(1);
            bn_Submit1 = new Button("close", "Acknowledge and Close This Claim");
            addChild(bn_Submit1);
        }


    }

    private void makeAssessButtons() {
        setColumns(1);

        Panel pnlAssess;
        Button btnAssess, btnReject;

        pnlAssess = new Panel("pnlAssessed");
        //pnlAssess.setColumns(3);

        btnAssess = new Button("btnAssessed", Application.getInstance().getMessage("claims.label.accessedAndProcessed","Assessed and Processed"));
        pnlAssess.addChild(new Label("claimDate", "<b>"+"Expenses Date: "+"</b>"));
        sbxMonth = new SelectBox("sbxMonth", getMonthMap(), getCurrentMonthMap());
        pnlAssess.addChild(sbxMonth);
        sbxYear = new SelectBox("sbxYear", getYearMap(), getCurrentYearMap());
        pnlAssess.addChild(sbxYear);
        pnlAssess.addChild(new Label("1",""));
        pnlAssess.addChild(btnAssess);

        btnReject = new Button("btnReject", Application.getInstance().getMessage("claims.label.reject","Reject"));
        pnlAssess.addChild(btnReject);
        addChild(pnlAssess);

    }

    private static Map monthMap;

    static {
        monthMap = new SequencedHashMap();
        monthMap.put("1", "1");
        monthMap.put("2", "2");
        monthMap.put("3", "3");
        monthMap.put("4", "4");
        monthMap.put("5", "5");
        monthMap.put("6", "6");
        monthMap.put("7", "7");
        monthMap.put("8", "8");
        monthMap.put("9", "9");
        monthMap.put("10", "10");
        monthMap.put("11", "11");
        monthMap.put("12", "12");
    }

    public static Map getMonthMap() {
        return monthMap;
    }

    public static Map getYearMap() {
        Map yearMap;
        int currentYear;
        String yearString;

        currentYear = Calendar.getInstance().get(Calendar.YEAR);
        yearMap = new SequencedHashMap();
        currentYear -= 5;

        for (int i = 0; i < 10; i++) {
            yearString = Integer.toString(currentYear++);
            yearMap.put(yearString, yearString);
        }

        return yearMap;
    }

    public static Map getCurrentMonthMap() {
        Map map;
        int currentMonth;
        String monthString;

        map = new HashMap();
        currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;
        monthString = Integer.toString(currentMonth);
        map.put(monthString, monthString);

        return map;
    }

    public static Map getCurrentYearMap() {
        Map map;
        int currentYear;
        String yearString;

        map = new HashMap();
        currentYear = Calendar.getInstance().get(Calendar.YEAR);
        yearString = Integer.toString(currentYear);
        map.put(yearString, yearString);

        return map;
    }

    public Forward onValidate(Event evt) {
        String button = findButtonClicked(evt);
        button = (button == null) ? "" : button;

        if (button.endsWith("submit")) {
            return ClaimFormIndexTableModel.procAction(evt, "submit", new String[]{this.formId});
        } else if (button.endsWith("approve")) {
            ClaimFormIndexTableModel.procAction(evt, "approve", new String[]{this.formId});
            return new Forward("approveSuccess");
        } else if (button.endsWith("reject")) {
            /*Application app = Application.getInstance();
            ClaimFormIndexModule cim = (ClaimFormIndexModule)
            app.getModule(ClaimFormIndexModule.class);
            ClaimFormIndex obj = cim.selectObject(this.formId);
            //obj.setRejectReason((String) tf_words.getValue());
            cim.updateObject(obj);

            ClaimFormIndexTableModel.procAction(evt, "reject", new String[]{this.formId});
            */return new Forward("rejectSuccess");
        } else if (button.endsWith("resub")) {
            ClaimFormIndexTableModel.procAction(evt, "resubmit", new String[]{this.formId});
            return new Forward("resubSuccess");
        } else if (button.endsWith("close")) {
            ClaimFormIndexTableModel.procAction(evt, "close", new String[]{this.formId});
            return new Forward("closeSuccess");

        } else if (button.endsWith("btnAssessed")) {
            Date claimDate;
            claimDate = getClaimDate();

            ClaimFormIndexModule.assessProcessClaim(evt, new String[]{this.formId}, claimDate);
            return new Forward("assessSuccess");

        } else if (button.endsWith("btnReject")) {
//            try {
//                ClaimFormIndexModule.assessRejectClaim(evt, new String[]{this.formId});
//            } catch (SecurityException e) {
//                return (Forward) null;
//            }
            return new Forward("assessRejectSuccess");
        }

        return (Forward) null;
    }

    private Date getClaimDate() {
        String monthStr, yearStr;
        int month, year;
        Calendar cal;

        try {
            monthStr = ((List) this.sbxMonth.getValue()).get(0).toString();
            month = Integer.parseInt(monthStr) - 1;
            yearStr = ((List) this.sbxYear.getValue()).get(0).toString();
            year = Integer.parseInt(yearStr);

            cal = Calendar.getInstance();
            cal.set(year, month,  1, 0, 0, 0);

            return cal.getTime();

        } catch (Exception e) {
            return null;
        }
    }



    public void setAssessors()
	{
      ClaimConfigModule module = (ClaimConfigModule)
                  Application.getInstance().getModule(ClaimConfigModule.class);

      Collection col = module.findObjects(
							new String[]{" namespace='"+namespace+"' "},
                     (String) "id", false,0,-1);
      Vector vecAssessors = new Vector();
      if(col!=null) { vecAssessors = new Vector(col); }
      else { vecAssessors = new Vector(); }
      String rightIds[] = new String[vecAssessors.size()];
      for(int cnt=0;cnt<vecAssessors.size();cnt++)
      {
         ClaimConfig theObj = (ClaimConfig) vecAssessors.get(cnt);
         rightIds[cnt]=theObj.getProperty1();
      }
      assessors  =rightIds;
	}


}
