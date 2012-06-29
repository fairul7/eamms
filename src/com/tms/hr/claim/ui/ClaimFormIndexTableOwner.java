/**
 * @author Vincent Lee, Wavelet Solutions Sdn Bhd (632468-W)
 * Created On: 2004-06-18
 * Copyright of The Media Shoppe Berhad
 */


package com.tms.hr.claim.ui;

import kacang.Application;
import kacang.stdui.TableAction;
import kacang.stdui.TableColumn;
import kacang.ui.Event;
import kacang.util.Log;

import com.tms.hr.claim.model.ClaimFormIndexModule;

public class ClaimFormIndexTableOwner extends ClaimFormIndexGenericTable  
{
	protected ThisTableModel theModel;
	String state  = null;

	public void setState(String state)
	{ 
		this.state = state; 
	}

	public void init() 
	{
	}

	public void onRequest(Event evt)
	{
		super.init();
		init();
		this.theModel = new ThisTableModel(getCondition(userId,this.state),state);
		setModel(this.theModel);
	}


	protected String[] getCondition(String userId,String state2)
	{
		super.init();
		// based on the state/usecase
		// determine the search criteria
		String []lCond = new String[]{"userOwner ='"+userId+"' "};
		//String []lCond =null;// new String[]{"userOwner ='"+userId+"' "};

		if( state2.equals(ClaimFormIndexModule.STATE_CREATED))
		{
			Log.getLog(this.getClass()).debug(" in created ");
			lCond = new String[] { " state = 'cre' ",
                       " status = 'act' ",    
								" (userOwner = '"+userId+"' OR userOriginator='"+userId+"') "};
		}
		else if(state2.equals(ClaimFormIndexModule.STATE_SUBMITTED))
		{	
			Log.getLog(this.getClass()).debug(" in submitted");
			lCond = new String[] { " state = 'sub' ",
                       " status = 'act' ",    
								" (userOwner = '"+userId+"' OR userOriginator='"+userId+"') "};
		}
		else if(state2.equals(ClaimFormIndexModule.STATE_APPROVED))
		{	
			Log.getLog(this.getClass()).debug(" in approved");
			lCond = new String[] { " state = 'app' ",
                       " status = 'act' ",    
								" (userOwner = '"+userId+"' OR userOriginator='"+userId+"') "};
		}
		else if(state2.equals(ClaimFormIndexModule.STATE_ASSESSED))
		{	
			Log.getLog(this.getClass()).debug(" in assessed");
			lCond = new String[] { " state = 'ass' ",
                       " status = 'act' ",    
								" (userOwner = '"+userId+"' OR userOriginator='"+userId+"') "};
		}
		else if(state2.equals(ClaimFormIndexModule.STATE_REJECTED))
		{	
			Log.getLog(this.getClass()).debug(" in rejected");
			lCond = new String[] { " state = 'rej' ",
                       " status = 'act' ",    
								" (userOwner = '"+userId+"' OR userOriginator='"+userId+"') "};
		}
      else if(state2.equals(ClaimFormIndexModule.STATE_CLOSED))
      {
         Log.getLog(this.getClass()).debug(" in rejected");
         lCond = new String[] { " state = 'clo' ",
                       " status = 'act' ",
                           " userOwner = '"+userId+"' "};
      }

		return lCond;
	}


	public class ThisTableModel extends ClaimFormIndexTableModel
	{
		String state = null;
		public ThisTableModel(String[] theCondition, String state) 
		{
			super(theCondition);
			super.setState(state);
			this.state = state;
			setColumns();
			setActionLinks();
		}

		public void printConditions()
		{
			for(int cnt=0;cnt<theCondition.length;cnt++)
			{
				
				Log.getLog(this.getClass()).debug(" Condition["+cnt+"]="+theCondition[cnt]);

			}

		}
	
		protected void setActionLinks()
		{
			printConditions();

			if(state!=null && state.equals(ClaimFormIndexModule.STATE_CREATED))
			{
				TableColumn infoCol = new TableColumn("info", Application.getInstance().getMessage("claims.label.info", "Info"));
                infoCol.setSortable(false);
                addColumn(infoCol);
				TableColumn editCol = new TableColumn(null, "", false);
				editCol.setLabel(Application.getInstance().getMessage("claims.label.edit", "Edit"));
				editCol.setUrl("../claim/user_editClaim.jsp");
				editCol.setUrlParam("formId");
                editCol.setSortable(false);
				addColumn(editCol);
				
				addAction(new TableAction("submit",Application.getInstance().getMessage("claims.category.submit", "Submit"),
						Application.getInstance().getMessage("claims.message.editClaimConfim","Once submitted, you may not be able to edit this claim anymore. Are you sure?")));
				addAction(new TableAction("delete",Application.getInstance().getMessage("claims.label.delete", "Delete"),
						Application.getInstance().getMessage("claims.message.deleteClaimConfirm","Once deleted, it will be purged from the database permanently, are you sure?")));
			}

			if(state!=null && state.equals(ClaimFormIndexModule.STATE_SUBMITTED))
			{
				addColumn(new TableColumn("info", Application.getInstance().getMessage("claims.label.info", "Info")));
            TableColumn viewCol = new TableColumn(null, "", false);
            viewCol.setLabel(Application.getInstance().getMessage("claims.label.view","View"));
            viewCol.setUrl("view_claim.jsp");
            viewCol.setUrlParam("formId");
            addColumn(viewCol);

//            addAction(new TableAction("delete","Delete","Once deleted, it will be purged from the database permanently, are you sure?"));

			}

        if(state!=null && state.equals(ClaimFormIndexModule.STATE_APPROVED))
         {
            addColumn(new TableColumn("info", Application.getInstance().getMessage("claims.label.info", "Info")));

            TableColumn viewCol = new TableColumn(null, "", false);
            viewCol.setLabel(Application.getInstance().getMessage("claims.label.view","View"));
            viewCol.setUrl("view_claim.jsp");
            viewCol.setUrlParam("formId");
            addColumn(viewCol);

         }

        if(state!=null && state.equals(ClaimFormIndexModule.STATE_ASSESSED))
         {
				addColumn(new TableColumn("info", Application.getInstance().getMessage("claims.label.info", "Info")));
				addColumn(new TableColumn("assessorName", Application.getInstance().getMessage("claims.label.assessor","Assessor")));

            TableColumn viewCol = new TableColumn(null, "", false);
            viewCol.setLabel(Application.getInstance().getMessage("claims.label.view","View"));
            viewCol.setUrl("view_claim.jsp");
            viewCol.setUrlParam("formId");
            addColumn(viewCol);

            addAction(new TableAction("close",Application.getInstance().getMessage("claims.label.ackAndClose",
            		Application.getInstance().getMessage("claims.label.ackAndClose","Acknowledge and Close")),
            		Application.getInstance().getMessage("claims.message.confirmation","Are you sure?")));

         }

        if(state!=null && state.equals(ClaimFormIndexModule.STATE_REJECTED))
         {
				addColumn(new TableColumn("info", Application.getInstance().getMessage("claims.label.info", "Info")));
				addColumn(new TableColumn("rejectReason", Application.getInstance().getMessage("claims.label.rejectReason","Reject Reason")));

            TableColumn viewCol = new TableColumn(null, "", false);
            viewCol.setLabel(Application.getInstance().getMessage("claims.label.view","View"));
            viewCol.setUrl("view_claim.jsp");
            viewCol.setUrlParam("formId");
            addColumn(viewCol);

            addAction(new TableAction("resubmit",Application.getInstance().getMessage("claims.label.resubmit","Resubmit"),
            		Application.getInstance().getMessage("claims.message.confirmation","Are you sure?")));
            addAction(new TableAction("delete",Application.getInstance().getMessage("claims.label.delete","Delete"),
            		Application.getInstance().getMessage("claims.message.deleteClaimConfirm","Once deleted, it will be purged from the database permanently, are you sure?")));

         }

        if(state!=null && state.equals(ClaimFormIndexModule.STATE_CLOSED))
         {
            addColumn(new TableColumn("info", Application.getInstance().getMessage("claims.label.info", "Info")));

            TableColumn viewCol = new TableColumn(null, "", false);
            viewCol.setLabel(Application.getInstance().getMessage("claims.label.view","View"));
            viewCol.setUrl("view_claim.jsp");
            viewCol.setUrlParam("formId");
            addColumn(viewCol);

         }



//			TableColumn submitCol = new TableColumn

		}




	}

}
