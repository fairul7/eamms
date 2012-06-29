/**
 * @author Vincent Lee, Wavelet Solutions Sdn Bhd (632468-W)
 * Created On: 2004-06-18
 * Copyright of The Media Shoppe Berhad
 */


package com.tms.hr.claim.ui;

import com.tms.hr.claim.model.ClaimFormIndexModule;
import kacang.Application;
import kacang.services.security.User;
import kacang.services.security.SecurityService;
import kacang.stdui.Table;
import kacang.stdui.TableAction;
import kacang.stdui.TableColumn;
import kacang.stdui.TableModel;
import kacang.ui.Event;
import kacang.ui.Forward;

import java.util.Collection;

public class ClaimFormIndexTable extends Table  
{
	public static String UC_ORIGINATOR_VIEW_LIST = "ori_view";
	public static String UC_OWNERLIST_VIEW_LIST = "own_view";
	public static String UC_APPROVERS="app_view";
	public static String UC_APPROVERS_PENDING="app_pending_view";
	public static String UC_ASSESSOR = "ass_view";


	protected String useCase = null;
	protected String[] theCondition = null ;


	public void setUseCase(String useCase)
	{ 
		if(useCase==null) { return; }
		String userId = getWidgetManager().getUser().getId();
		this.useCase = useCase;

		// viewed by originator
		if(useCase.equals("ori_view"))
		{
			theCondition =new String[] { " status = 'act' ",
									" userOriginator = '"+userId+"' "};
		}
		else if (useCase.equals("own_view"))
		{
			theCondition =new String[] { " status = 'act' ",
									" userOwner= '"+userId+"' "};
		}
		else if (useCase.equals("app_view"))
		{
            theCondition =new String[] { " status = 'act' "
									, " userApprover1= '"+userId+"' "
									+ " OR userApprover2= '"+userId+"' "
									+ " OR userApprover3= '"+userId+"' "
									+ " OR userApprover4= '"+userId+"' "

								};
		}
		else if (useCase.equals("app_view"))
		{
			theCondition =new String[] { " status = 'act' "
									, " userApprover1= '"+userId+"' "
									+ " OR userApprover2= '"+userId+"' "
									+ " OR userApprover3= '"+userId+"' "
									+ " OR userApprover4= '"+userId+"' ",
									" approvalLevelRequired < approvalLevelGranted "
								};	
		}
		else if (useCase.equals("ass_view"))
		{
			theCondition = new String[] { " status = 'act' ",
									" userOwner= '"+userId+"' "};
		}

	}



	public void init() 
	{
        setMethod("POST");
		setModel(new ClaimFormIndexTableModel());
	}

	public class ClaimFormIndexTableModel extends TableModel 
	{
		public ClaimFormIndexTableModel() 
		{
			addColumn(new TableColumn("timeEdit", "Create/Edit Time"));
			addColumn(new TableColumn("originatorName", "Originator"));
			addColumn(new TableColumn("ownerName", "Owner"));
			addColumn(new TableColumn("approver1Name", "Approver1"));
			addColumn(new TableColumn("approver2Name", "Approver2"));
			addColumn(new TableColumn("amount", "Amount"));
			addColumn(new TableColumn("approvalLevelRequired", "Approval Level Required"));
			addColumn(new TableColumn("approvalLevelGranted", "Approval Level Granted"));
			addColumn(new TableColumn("remarks", "Remarks"));
			addColumn(new TableColumn("rejectReason", "Reject Reason"));
			addColumn(new TableColumn("state", "State"));
            TableColumn editCol = new TableColumn(null, "", false);
            editCol.setLabel("View");
            editCol.setUrl("view_claim.jsp?useCase="+useCase);
            editCol.setUrlParam("formId");
            addColumn(editCol);

			addAction(new TableAction("delete", "Delete", "Delete Selected Items?"));
		}

		public Collection getTableRows() 
		{
			Application application = Application.getInstance();
			ClaimFormIndexModule module = (ClaimFormIndexModule)
					application.getModule(ClaimFormIndexModule.class);
			return module.findObjects(theCondition,"timeEdit",
							true, getStart(), getRows());
		}

		public int getTotalRowCount() 
		{
            Application application = Application.getInstance();
            ClaimFormIndexModule module = (ClaimFormIndexModule)	
						application.getModule(ClaimFormIndexModule.class);
            return module.countObjects(new String[]{ " status = 'act' " });
		}

		protected String[] getConditions()
		{
			String[] str = {"" };
			return str;
		}

		public String getTableRowKey() 
		{ return "id"; }

		public Forward processAction(Event evt, String action, String[] selectedKeys) 
		{
			if ("delete".equals(action)) 
			{
				Application application = Application.getInstance();
				ClaimFormIndexModule module = (ClaimFormIndexModule)application.getModule(ClaimFormIndexModule.class);
        		for (int i=0; i<selectedKeys.length; i++) 
				{
        			module.deleteObject(selectedKeys[i]);
         	}
			}
			return null;
		}

    }

}
