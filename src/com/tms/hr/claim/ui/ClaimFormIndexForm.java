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
import kacang.stdui.*;
import kacang.stdui.validator.Validator;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.UuidGenerator;

import java.util.Date;


public class ClaimFormIndexForm extends Form 
{
	public static String UC_CREATE= "create"; 
	public static String UC_EDIT = "edit"; 
	public static String UC_VIEW = "view"; 

    protected DateField df_TimeEdit;
    protected SelectBox sb_UserOriginator;
    protected SelectBox sb_UserOwner = null;
    protected SelectBox sb_UserApprover1;
    protected SelectBox sb_UserApprover2;
    protected SelectBox sb_Currency;
    protected TextField tf_Amount;
    protected Label lb_Amount;
    protected TextField tf_Remarks;
    protected Button bn_Submit;

    // add for display purposes
    protected Label lbUserOriginator;
    protected Label lbUserOriginatorName;
    protected Label lbUserOwner;
    protected Label lbUserApprover1;
    protected Label lbUserApprover1Name;
    protected Label lbUserApprover2;
    protected Label lbUserApprover2Name;
    protected Label lbRemark;
    protected Label lbAmount;



    protected SelectBox sb_Title;

    protected boolean owner=false,userApprover1=false,userApprover2=false,submit=false;

	protected String useCase = "";

	public String getDefaultTemplate() {
        return "claims/claimFormIndexForm";
    }

    public void setUseCase(String useCase)
	{ this.useCase = useCase;}


	protected String justCreatedId;
	public void setJustCreatedId(String justCreatedId)
	{ this.justCreatedId = justCreatedId;}
	public String getJustCreatedId()
	{ return this.justCreatedId;}

	public void init() 
	{
        SecurityService service = (SecurityService)
				Application.getInstance().getService(SecurityService.class);
        Application application = Application.getInstance();
        ClaimFormIndexModule module = (ClaimFormIndexModule)
                  application.getModule(ClaimFormIndexModule.class);

		setColumns(4);
        setMethod("POST");
		try
		{
			lbUserOriginator = new Label("userOriginator", "<b>"+application.getMessage("claims.label.submitBy","Submit By")+"</b>");
            addChild(lbUserOriginator);
			User usrSubmitBy = service.getUser(getWidgetManager().getUser().getId());
            lbUserOriginatorName = new Label("userOriginator-name",usrSubmitBy.getName());
			addChild(lbUserOriginatorName);
		}
		catch(Exception ex)
		{ ex.printStackTrace();}

        int nOwners = ClaimConfigAssistant.countPossibleOptions(
                                 getWidgetManager().getUser().getId(),
                                 ClaimConfigAssistant.namespace);
		if(nOwners>0)
		{
			owner=true;
            lbUserOwner=new Label("userOwner", "<b>"+application.getMessage("claims.label.claimant","Claimant")+"</b>");
            addChild(lbUserOwner);
			sb_UserOwner = new SelectBox("sb_UserOwner");
			sb_UserOwner.addOption(getWidgetManager().getUser().getId(),
                           getWidgetManager().getUser().getName());
			ClaimConfigAssistant.addPossibleOptions(sb_UserOwner, 
											getWidgetManager().getUser().getId(),
											ClaimConfigAssistant.namespace);
			sb_UserOwner.setSelectedOptions(new String[]{getWidgetManager().getUser().getId()});
			addChild(sb_UserOwner);
		}
		else
		{
			addChild(new Label("dummy1",""));
			addChild(new Label("dummy2",""));
		}

		///////////////////
		String approver1Id = null;
		approver1Id = module.selectHierarchy(getWidgetManager().getUser().getId());
		User approver1 =  null;
		try
		{ approver1 = service.getUser(approver1Id); }
		catch(Exception ex) { }
		if(approver1!=null)
		{
			userApprover1=true;
            lbUserApprover1 =new Label("UserApprover1", "<b>"+application.getMessage("claims.label.approval","Approver")+"1 </b>");
            addChild(lbUserApprover1);
            lbUserApprover1Name = new Label("UserApprover1-name",approver1.getName());
			addChild(lbUserApprover1Name);
			String approver2Id = null;
			approver2Id = module.selectHierarchy(approver1Id);
			User approver2 = null;
			try
			{ approver2 = service.getUser(approver2Id); }
			catch(Exception ex) { }
			if(approver2!=null)
			{
				userApprover2=true;
                lbUserApprover2 = new Label("UserApprover2","<b>"+application.getMessage("claims.label.approval","Approver")+"2 </b>");
                addChild(lbUserApprover2);
                lbUserApprover2Name = new Label("UserApprover2-name",approver2.getName());
				addChild(lbUserApprover2Name);
			}

		}
		/////////////////////
        lbRemark = new Label("Remarks", "<b>"+application.getMessage("claims.label.title","Title")+" * "+"</b>");
		addChild(lbRemark);

		tf_Remarks = new TextField("tf_Remarks");
		tf_Remarks.setSize("20");
        Validator vne = new ValidatorNotEmpty("vne", "Must not be empty");
        tf_Remarks.addChild(vne);
		addChild(tf_Remarks);

		lbAmount = new Label("Amount", "<b>"+application.getMessage("claims.label.amount","Amount"));
        addChild(lbAmount);
//		tf_Amount = new TextField("tf_Amount");
//		tf_Amount.setSize("8");
//		addChild(tf_Amount);

		lb_Amount = new Label("lbAmount","0.00");
		addChild(lb_Amount);	

		if(useCase==null || !useCase.equals(UC_VIEW))
		{
			submit = true;
            addChild(new Label("dummy5",""));
			addChild(new Label("dummy6",""));
			bn_Submit = new Button("submit", Application.getInstance().getMessage("claims.label.update", "Update"));
			addChild(bn_Submit);
		}
    }


	public Forward onValidate(Event evt) 
	{
		Application application = Application.getInstance();
		ClaimFormIndexModule module = (ClaimFormIndexModule)
						application.getModule(ClaimFormIndexModule.class);

		ClaimFormIndex obj = new ClaimFormIndex();
		UuidGenerator uuid = UuidGenerator.getInstance();
		obj.setId(uuid.getUuid());
		obj.setTimeEdit(new Date());
		obj.setUserOriginator(getWidgetManager().getUser().getId());
		if(sb_UserOwner!=null)
		{
			obj.setUserOwner((String)sb_UserOwner.getSelectedOptions().keySet().iterator().next());
           
        }
		else
		{
			obj.setUserOwner(getWidgetManager().getUser().getId());
		}

		String approver1Id = null;
      approver1Id = module.selectHierarchy(obj.getUserOwner());
		if(approver1Id!=null && approver1Id.length()>2)
		{
			obj.setUserApprover1(approver1Id);
			String approver2Id = null;
			approver2Id = module.selectHierarchy(obj.getUserApprover1());
			if(approver2Id!=null && approver2Id.length()>2)
			{
				obj.setUserApprover2(approver2Id);
			}
		}

		obj.setCurrency("MYR");
		obj.setApprovalLevelRequired(obj.getNumberOfApprover());
		obj.setApprovalLevelGranted(new Integer(0));
        obj.setRemarks((String)tf_Remarks.getValue());
        obj.setState(ClaimFormIndexModule.STATE_CREATED);
		obj.setStatus("act");
	
      module.addObject(obj);
		setJustCreatedId(obj.getId());

		removeChildren();
		init();
		return new Forward("edit");
//		return super.onValidate(evt);
    }

    public SelectBox getSb_UserOriginator() {
        return sb_UserOriginator;
    }

    public SelectBox getSb_UserOwner() {
        return sb_UserOwner;
    }

    public SelectBox getSb_UserApprover1() {
        return sb_UserApprover1;
    }

    public SelectBox getSb_UserApprover2() {
        return sb_UserApprover2;
    }

    public Label getLb_Amount() {
        return lb_Amount;
    }

    public TextField getTf_Remarks() {
        return tf_Remarks;
    }

    public Button getBn_Submit() {
        return bn_Submit;
    }

    public Label getLbUserOriginator() {
        return lbUserOriginator;
    }

    public Label getLbUserOriginatorName() {
        return lbUserOriginatorName;
    }

    public Label getLbUserOwner() {
        return lbUserOwner;
    }

    public Label getLbUserApprover1() {
        return lbUserApprover1;
    }

    public Label getLbUserApprover1Name() {
        return lbUserApprover1Name;
    }

    public Label getLbUserApprover2() {
        return lbUserApprover2;
    }

    public Label getLbUserApprover2Name() {
        return lbUserApprover2Name;
    }

    public Label getLbRemark() {
        return lbRemark;
    }

    public Label getLbAmount() {
        return lbAmount;
    }

    public boolean isOwner() {
        return owner;
    }

    public boolean isUserApprover1() {
        return userApprover1;
    }

    public boolean isUserApprover2() {
        return userApprover2;
    }

    public boolean isSubmit() {
        return submit;
    }



}
