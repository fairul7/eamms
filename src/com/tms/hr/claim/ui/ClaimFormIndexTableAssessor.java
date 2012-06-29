/**
 * @author Vincent Lee, Wavelet Solutions Sdn Bhd (632468-W)
 * Created On: 2004-06-18
 * Copyright of The Media Shoppe Berhad
 */


package com.tms.hr.claim.ui;

import com.tms.hr.claim.model.ClaimFormIndexModule;
import kacang.stdui.TableColumn;
import kacang.ui.Event;
import kacang.util.Log;

public class ClaimFormIndexTableAssessor extends ClaimFormIndexGenericTable  
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
		String []lCond = new String[]{"userAssessor ='"+userId+"' "};
		//String []lCond =null;// new String[]{"userAssessor ='"+userId+"' "};

		if(state2.equals(ClaimFormIndexModule.STATE_APPROVED))
		{	
			Log.getLog(this.getClass()).debug(" in approved");
			lCond = new String[] { " state = 'app' ",
                       " status = 'act' " };   
		}
		else if(state2.equals(ClaimFormIndexModule.STATE_ASSESSED))
		{	
			Log.getLog(this.getClass()).debug(" in assessed");
			lCond = new String[] { " state = 'ass' ",
                       " status = 'act' " };   
		}
		else if(state2.equals(ClaimFormIndexModule.STATE_REJECTED))
		{	
			Log.getLog(this.getClass()).debug(" in rejected");
			lCond = new String[] { " state = 'rej' ",
                       " status = 'act' " }; 
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

			if(state!=null && state.equals(ClaimFormIndexModule.STATE_APPROVED))
			{
			//	addColumn(new TableColumn("rejectReason", "Reject Reason"));
				addColumn(new TableColumn("info", "Info"));
            TableColumn viewCol = new TableColumn(null, "", false);
            viewCol.setLabel("View");
            viewCol.setUrl("view_claim.jsp");
            viewCol.setUrlParam("formId");
            addColumn(viewCol);

/*
                // Deprecated: Must access/reject from claim form to indicate claim month
				addAction(new TableAction("reject","Reject","Are you sure?"));
				addAction(new TableAction("assess","Assessed and Processed","Are you sure?"));
*/
			}


		}




	}

}
