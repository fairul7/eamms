/**
 * @author Vincent Lee, Wavelet Solutions Sdn Bhd (632468-W)
 * Created On: 2004-07-08
 * Copyright of The Media Shoppe Berhad
 */


package com.tms.hr.claim.ui;

import com.tms.hr.claim.model.ClaimFormIndex;
import com.tms.hr.claim.model.ClaimFormIndexModule;
import kacang.Application;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.stdui.Button;
import kacang.stdui.Form;
import kacang.stdui.Label;
import kacang.ui.Event;
import kacang.ui.Forward;

import java.text.DateFormat;
import java.text.SimpleDateFormat;


public class ClaimFormIndexViewForm extends Form
{
    public static String UC_VIEW = "view";
    public static String UC_APPROVE_REJECT = "app_rej";
    public static String UC_PROCESS = "process";
    public static String UC_CLONE = "clone";

   protected Button bn_Approve;
   protected Button bn_Reject;
   protected Button bn_Process;
   protected Button bn_Clone;

    protected Label lbUserOriginator;
    protected Label lbTitle;
    protected Label lbDate;
    protected Label lbUserOwner;
    protected Label lbUserApprover1;
    protected Label lbUserApprover2;
    protected Label lbAmount;

    protected Label approver1Date;
    protected Label approver2Date;

    protected boolean approver1=false,approver2=false;

    protected boolean approver1DateBool=false, approver2DateBool=false;

    protected String useCase = "";

    public void setUseCase(String useCase)
    { this.useCase = useCase;}


    protected String justCreatedId;
    public void setJustCreatedId(String justCreatedId)
    { this.justCreatedId = justCreatedId;}
    public String getJustCreatedId()
    { return this.justCreatedId;}

    public String getId()
    { return this.justCreatedId;}
    public void setId(String formId)
    { this.justCreatedId = formId;}


    public void onRequest(Event evt)
    {
      SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
      Application application = Application.getInstance();
      ClaimFormIndexModule module = (ClaimFormIndexModule)
                  application.getModule(ClaimFormIndexModule.class);

        ClaimFormIndex theObj = module.selectObject(getId());



        setColumns(5);
        setMethod("POST");

        lbTitle = new Label("TitleValue", theObj.getRemarks());
        addChild(new Label("TitleLabel", "<b>"+Application.getInstance().getMessage("claims.label.title")+"</b>"));
        addChild(lbTitle);
        addChild(new Label("1",""));
        //lbDate = new Label("dateValue", theObj.getTimeEditStr());
        lbDate = new Label("dateValue", new SimpleDateFormat("yyy-MM-dd").format(theObj.getTimeEdit()));
		addChild(new Label("dateLabel", "<b>"+Application.getInstance().getMessage("claims.label.date","Date")+"</b>"));
		addChild(lbDate);

		addChild(new Label("userOriginatorLabel", "<b>"+Application.getInstance().getMessage("claims.label.submittedBy","Submitted By")+"</b>"));
		User userOriginator = null;
		try 
		{	
			userOriginator = 	service.getUser(theObj.getUserOriginator());
            lbUserOriginator = new Label("userOriginatorValue", userOriginator.getName());
        }
        catch(Exception ex)
        {
            lbUserOriginator = new Label("userOriginatorValue", "none");
        }
        addChild(lbUserOriginator);

        addChild(new Label("2",""));
		addChild(new Label("userOwnerLabel", "<b>"+Application.getInstance().getMessage("claims.label.claimant","Claimant")+"</b>"));
      User userOwner = null;
      try
      {
         userOwner =  service.getUser(theObj.getUserOwner());
          lbUserOwner = new Label("userOwnerValue", userOwner.getName());
      }
      catch(Exception ex)
      {
          lbUserOwner = new Label("userOwnerValue", Application.getInstance().getMessage("claims.label.none","none"));
      }
        addChild(lbUserOwner);

        ///////////////////
        User approver1 = null;
        try
        { approver1 = service.getUser(theObj.getUserApprover1());}
        catch(Exception ex)
        { }
        if(approver1!=null)
        {
            this.approver1=true;
			addChild(new Label("UserApprover1Label", "<b>"+Application.getInstance().getMessage("claims.label.approver1","Approver1")+"</b>"));
            lbUserApprover1  = new Label("UserApprover1Value", approver1.getName());
            addChild(lbUserApprover1);
            addChild(new Label("3",""));


           if(module.retrieveApprover1Date(getId()) !=null){
           approver1Date = new Label("approver1Date",  new SimpleDateFormat("yyy-MM-dd").format(module.retrieveApprover1Date(getId())));
           addChild(approver1Date);

               approver1DateBool=true;
           }

            else
              approver1DateBool=false;





        }

      User approver2 = null;
      try
      { approver2 = service.getUser(theObj.getUserApprover2());}
      catch(Exception ex)
      { }
      if(approver2!=null)
      {
         this.approver2=true;
         addChild(new Label("UserApprover2Label", "<b>"+Application.getInstance().getMessage("claims.label.approver2","Approver2")+"</b>"));
          lbUserApprover2 = new Label("UserApprover2Value", approver2.getName());
         addChild(lbUserApprover2);

         if(module.retrieveApprover2Date(getId()) !=null){
         approver2Date = new Label("approver2Date", new SimpleDateFormat("yyyy-MM-dd").format(module.retrieveApprover2Date(getId())));
         addChild(approver2Date);

         approver2DateBool=true;
         }
         else
         approver2DateBool=false;




      }
        else {
          addChild(new Label("4",""));
          addChild(new Label("5",""));
      }

//		addChild(new Label("statusLabel","State"));
//		addChild(new Label("statusValue",theObj.getState()));

        String tempAmount = theObj.getAmountStr();
        if(tempAmount.charAt(0)=='-')
        tempAmount = "("+tempAmount.substring(1,tempAmount.length())+")";
        lbAmount = new Label("AmountValue", tempAmount);
		addChild(new Label("AmountLabel", "<b>"+Application.getInstance().getMessage("claims.label.amount","Amount")+"</b>"));
		addChild(lbAmount);

    }

    public void init()
    {
    }

    public Forward onValidate(Event evt)
    {
        return (Forward) null;
    }

    public String getDefaultTemplate() {
        return "claims/claimFormIndexViewForm";
    }

    public Label getLbUserOriginator() {
        return lbUserOriginator;
    }

    public Label getLbTitle() {
        return lbTitle;
    }

    public Label getLbDate() {
        return lbDate;
    }

    public Label getLbUserOwner() {
        return lbUserOwner;
    }

    public Label getLbUserApprover1() {
        return lbUserApprover1;
    }

    public Label getLbUserApprover2() {
        return lbUserApprover2;
    }

    public boolean isApprover1() {
        return approver1;
    }

    public Label getLbAmount() {
        return lbAmount;
    }

    public boolean isApprover2() {
        return approver2;
    }


    public Label getApprover1Date() {
        return approver1Date;
    }

    public void setApprover1Date(Label approver1Date) {
        this.approver1Date = approver1Date;
    }


    public Label getApprover2Date() {
        return approver2Date;
    }

    public void setApprover2Date(Label approver2Date) {
        this.approver2Date = approver2Date;
    }


    public boolean isApprover1DateBool() {
        return approver1DateBool;
    }

    public void setApprover1DateBool(boolean approver1DateBool) {
        this.approver1DateBool = approver1DateBool;
    }

    public boolean isApprover2DateBool() {
        return approver2DateBool;
    }

    public void setApprover2DateBool(boolean approver2DateBool) {
        this.approver2DateBool = approver2DateBool;
    }
}
