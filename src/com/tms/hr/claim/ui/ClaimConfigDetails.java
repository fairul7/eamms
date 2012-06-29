package com.tms.hr.claim.ui;

import com.tms.hr.claim.model.ClaimConfig;
import com.tms.hr.claim.model.ClaimConfigModule;
import kacang.Application;
import kacang.services.security.SecurityService;
import kacang.ui.Event;
import kacang.ui.Widget;
import kacang.util.Log;

import java.util.Collection;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: oilai
 * Date: Jul 1, 2005
 * Time: 9:48:33 AM
 * To change this template use File | Settings | File Templates.
 */
public class ClaimConfigDetails extends Widget{

    protected static String namespace_assessor = "com.tms.hr.claim.ui.ClaimConfigAssessor";
    protected static String namespace_logic = "com.tms.hr.claim.ui.ClaimConfigApprovingLogic";
    protected static String namespace_mileage = "com.tms.hr.claim.ui.ClaimConfigMileage";

    private String[] assessor;
    private String[] approvalLogic;
    private String mileage;

    public ClaimConfigDetails(){

    }

    public ClaimConfigDetails(String name) {
        super(name);
    }

    public void init(){
        ClaimConfigModule module = (ClaimConfigModule)
                  Application.getInstance().getModule(ClaimConfigModule.class);

        //get assessor
        Collection col = module.findObjects(
							new String[]{" namespace='"+namespace_assessor+"' "},
                     (String) "id", false,0,-1);
        Vector vecAssessors = new Vector();
        if(col!=null) { vecAssessors = new Vector(col); }
        else { vecAssessors = new Vector(); }
        assessor = new String[vecAssessors.size()];
        SecurityService service = (SecurityService)Application.getInstance().getService(SecurityService.class);
        for(int cnt=0;cnt<vecAssessors.size();cnt++) {
            ClaimConfig theObj = (ClaimConfig) vecAssessors.get(cnt);
            try {
               // assessor[cnt]=service.getUser(theObj.getProperty1()).getUsername();
               assessor[cnt]=(String) service.getUser(theObj.getProperty1()).getProperty("firstName") +" "+ (String) service.getUser(theObj.getProperty1()).getProperty("lastName"); 

            }
            catch(Exception e) {
                Log.getLog(getClass()).error("unable to get assessor :",e);
            }
        }

        //get mileage
        col = module.findObjects(new String[]{" namespace='"+namespace_mileage+"' "},
                     (String) "id", false,0,-1);
        Vector vecMileages = new Vector();
		if(col!=null) { vecMileages = new Vector(col); }
		else { vecMileages = new Vector(); }

        for(int cnt=0;cnt<vecMileages.size();cnt++)
        {
            ClaimConfig theObj = (ClaimConfig) vecMileages.get(cnt);
            mileage = theObj.getProperty1();
        }

        col = module.findObjects(new String[]{" namespace='"+namespace_logic+"' "},
                     (String) "id", false,0,-1);
		Vector vecApprovingLogics = new Vector();
		if(col!=null) { vecApprovingLogics = new Vector(col); }
		else { vecApprovingLogics = new Vector(); }

		approvalLogic = new String[vecApprovingLogics.size()];
        for(int cnt=0;cnt<vecApprovingLogics.size();cnt++)
        {
            ClaimConfig theObj = (ClaimConfig) vecApprovingLogics.get(cnt);
            if (theObj.getProperty1().equals("1"))
                approvalLogic[cnt]="One Approver Only";
            else if (theObj.getProperty1().equals("2"))
                approvalLogic[cnt]="All Approvers Required";
        }

    }

    public void onRequest(Event event) {
        init();
    }

    public String getDefaultTemplate() {
        return "claims/config_details";
    }

    public String[] getAssessor() {
        return assessor;
    }

    public void setAssessor(String[] assessor) {
        this.assessor = assessor;
    }

    public String[] getApprovalLogic() {
        return approvalLogic;
    }

    public void setApprovalLogic(String[] approvalLogic) {
        this.approvalLogic = approvalLogic;
    }

    public String getMileage() {
        return mileage;
    }

    public void setMileage(String mileage) {
        this.mileage = mileage;
    }

}
