/**
 * @author Vincent Lee, Wavelet Solutions Sdn Bhd (632468-W)
 * Created On: 2004-07-08
 * Copyright of The Media Shoppe Berhad
 */

package com.tms.hr.claim.ui;

import com.tms.hr.claim.model.ClaimFormIndex;
import com.tms.hr.claim.model.ClaimFormIndexModule;
import com.tms.hr.employee.ui.EmployeeSelectBox;
import kacang.Application;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.stdui.*;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.UuidGenerator;

import java.math.BigDecimal;

public class ClaimFormIndexProcessForm extends Form 
{
	public static String UC_CREATE= "create"; 
	public static String UC_EDIT = "edit"; 
	public static String UC_VIEW = "view"; 

    protected DateField df_TimeEdit;
    protected SelectBox sb_UserOriginator;
    protected SelectBox sb_UserOwner;
    protected SelectBox sb_UserApprover1;
    protected SelectBox sb_UserApprover2;
//    protected TextField tf_UserApprover3;
//    protected TextField tf_UserApprover4;
//    protected TextField tf_UserAssessor;
    protected SelectBox sb_Currency;
    protected TextField tf_Amount;
//    protected TextField tf_ApprovalLevelRequired;
//    protected TextField tf_ApprovalLevelGranted;
    protected TextField tf_Remarks;
//    protected TextField tf_RejectReason;
//    protected SelectBox sb_State;
//    protected SelectBox sb_Status;
    protected Button bn_Submit;

	protected String useCase = "";
	
	public void setUseCase(String useCase)
	{ this.useCase = useCase;}


	protected String justCreatedId;
	public void setJustCreatedId(String justCreatedId)
	{ this.justCreatedId = justCreatedId;}
	public String getJustCreatedId()
	{ return this.justCreatedId;}

	public void init() 
	{
      SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
      Application application = Application.getInstance();
      ClaimFormIndexModule module = (ClaimFormIndexModule)
                  application.getModule(ClaimFormIndexModule.class);

		setColumns(2);
        setMethod("POST");

		addChild(new Label("date", "Date"));
		df_TimeEdit = new DateField("df_TimeEdit");
		addChild(df_TimeEdit);

		addChild(new Label("userOriginator", "Originator"));
		sb_UserOriginator = new SelectBox("sb_UserOriginator");
		sb_UserOriginator.addOption(getWidgetManager().getUser().getId(),
									getWidgetManager().getUser().getName());
		addChild(sb_UserOriginator);

//String userId = getWidgetManager().getUser().getId();

		addChild(new Label("userOwner", "Owner"));
//		sb_UserOwner= new TextField("tf_UserOwner");
		sb_UserOwner=  new EmployeeSelectBox("sb_UserOwner", EmployeeSelectBox.SHOW_ALL);
		sb_UserOwner.setSelectedOptions(new String[]{getWidgetManager().getUser().getId()});
		addChild(sb_UserOwner);

		///////////////////
		String approver1Id = null;
		approver1Id = module.selectHierarchy(getWidgetManager().getUser().getId());
		User approver1 =  null;
		try
		{
			sb_UserApprover1 = null;
			if(approver1Id!=null && approver1Id.length()>2)
			{
				approver1 = service.getUser(approver1Id);
				sb_UserApprover1 = new SelectBox("Approver1");
				sb_UserApprover1.addOption(approver1Id,approver1.getName());
			}
		}
		catch(Exception ex)
		{ sb_UserApprover1=null;ex.printStackTrace(); }
		if(sb_UserApprover1==null)
		{
      	sb_UserApprover1 =  new EmployeeSelectBox("sb_UserApprover1", EmployeeSelectBox.SHOW_ALL);
		}
		addChild(new Label("UserApprover1", "Approver1"));
		addChild(sb_UserApprover1);
		/////////////////////
		String approver2Id = null;
		if(approver1Id!=null)
		{
      	approver2Id = module.selectHierarchy(approver1Id);
			User approver2 = null;
			try
			{
				sb_UserApprover2 = null;
				if(approver2Id!=null && approver2Id.length()>2)
				{
					approver2 = service.getUser(approver2Id);
					sb_UserApprover2 = new SelectBox("Approver2");
					sb_UserApprover2.addOption(approver2Id,approver2.getName());
				}

			}
			catch(Exception ex)
			{sb_UserApprover2=null;ex.printStackTrace();}
			if(sb_UserApprover2==null)
			{
				sb_UserApprover2 =  new EmployeeSelectBox("sb_UserApprover2", 
															EmployeeSelectBox.SHOW_ALL);
			}
		}
		else
		{
      	sb_UserApprover2 =  new EmployeeSelectBox("sb_UserApprover2", EmployeeSelectBox.SHOW_ALL);
		}

		addChild(new Label("UserApprover2", "Approver2"));
		addChild(sb_UserApprover2);

/*
		addChild(new Label("UserApprover3", "Approver3"));
		tf_UserApprover3 = new TextField("tf_UserApprover3");
		addChild(tf_UserApprover3);

		addChild(new Label("UserApprover4", "Approver4"));
		tf_UserApprover4 = new TextField("tf_UserApprover4");
		addChild(tf_UserApprover4);

		addChild(new Label("UserAssessor", "Assessor"));
		tf_UserAssessor = new TextField("tf_UserAssessor");
		addChild(tf_UserAssessor);

*/
		addChild(new Label("currency", "Currency"));
		sb_Currency = new SelectBox("sb_Currency");
		sb_Currency.addOption("MYR","MYR");
		sb_Currency.addOption("USD","USD");
		sb_Currency.addOption("SGD","SGD");
		addChild(sb_Currency);
/*
		addChild(new Label("Amount", "Amount"));
		tf_Amount = new TextField("tf_Amount");
		addChild(tf_Amount);
*/
/*
		addChild(new Label("ApprovalLevelRequired", "ApprovalLevelRequired"));
		tf_ApprovalLevelRequired = new TextField("tf_ApprovalLevelRequired");
		addChild(tf_ApprovalLevelRequired);

		addChild(new Label("ApprovalLevelGranted", "ApprovalLevelGranted"));
		tf_ApprovalLevelGranted = new TextField("tf_ApprovalLevelGranted");
		addChild(tf_ApprovalLevelGranted);
*/
		addChild(new Label("Remarks", "Remarks"));
		tf_Remarks = new TextField("tf_Remarks");
		addChild(tf_Remarks);
/*
		addChild(new Label("RejectReason", "RejectReason"));
		tf_RejectReason = new TextField("tf_RejectReason");
		addChild(tf_RejectReason);
		addChild(new Label("state", "State"));
		sb_State = new SelectBox("sb_State");
		addChild(sb_State);

		addChild(new Label("status", "Status"));
		sb_Status = new SelectBox("sb_Status");
		sb_Status.addOption("act","Active");
		sb_Status.addOption("ina","In-active");
		sb_Status.addOption("del","Deleted");
		addChild(sb_Status);
*/

		if(useCase==null || !useCase.equals(UC_VIEW))
		{
			bn_Submit = new Button("submit", "Submit");
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
		obj.setTimeEdit(df_TimeEdit.getDate());
		//obj.setUserOriginator((String)sb_UserOriginator.getValue());
		obj.setUserOriginator(getWidgetManager().getUser().getId());
		obj.setUserOwner((String)sb_UserOwner.getSelectedOptions().keySet().iterator().next());
		obj.setUserApprover1((String)sb_UserApprover1.getSelectedOptions().keySet().iterator().next());
		obj.setUserApprover2((String)sb_UserApprover2.getSelectedOptions().keySet().iterator().next());
//		obj.setUserApprover3((String)tf_UserApprover3.getValue());
//		obj.setUserApprover4((String)tf_UserApprover4.getValue());
//		obj.setUserAssessor((String)tf_UserAssessor.getValue());
		obj.setCurrency((String)sb_Currency.getSelectedOptions().keySet().iterator().next());
		obj.setAmount(new BigDecimal("0.00"));
		//obj.setApprovalLevelRequired(new Integer((String)tf_ApprovalLevelRequired.getValue()));
		obj.setApprovalLevelRequired(obj.getNumberOfApprover());
//		obj.setApprovalLevelGranted(new Integer((String)tf_ApprovalLevelGranted.getValue()));
		obj.setApprovalLevelGranted(new Integer(0));
		obj.setRemarks((String)tf_Remarks.getValue());
//		obj.setRejectReason((String)tf_RejectReason.getValue());
//		obj.setState((String) sb_State.getSelectedOptions().keySet().iterator().next());
//		obj.setStatus((String) sb_Status.getSelectedOptions().keySet().iterator().next());
		obj.setState(ClaimFormIndexModule.STATE_CREATED);
		obj.setStatus("act");
	
      module.addObject(obj);
		setJustCreatedId(obj.getId());

		removeChildren();
		init();

		return super.onValidate(evt);
    }

}
