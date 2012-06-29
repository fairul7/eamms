package com.tms.hr.claim.ui;

import com.tms.hr.claim.model.ClaimFormIndexModule;
import com.tms.hr.claim.model.ClaimConfigModule;
import com.tms.hr.claim.model.ClaimConfig;

import kacang.util.Log;
import kacang.Application;
import kacang.ui.Event;
import kacang.stdui.TableColumn;
import kacang.stdui.TableAction;

import java.util.Collection;
import java.util.Vector;
import java.util.Enumeration;
import java.util.Iterator;


/**
 * Created by IntelliJ IDEA.
 * User: cheewei
 * Date: Jan 23, 2006
 * Time: 3:22:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class ClaimHistory extends ClaimFormIndexTableOwner {

    protected static String namespace = "com.tms.hr.claim.ui.ClaimConfigAssessor";

    protected ThisTableModel theModel;

    protected String[] getCondition(String userId, String state2) {
        super.init();



        Application application = Application.getInstance();
      ClaimConfigModule module = (ClaimConfigModule)
                  application.getModule(ClaimConfigModule.class);

        //ge assessors
        Collection col = module.findObjects(new String[] {
                    " namespace='" + namespace + "' "
                }, (String) "id", false, 0, -1);


           // based on the state/usecase
           // determine the search criteria
           //force retrieve everything

        userId="%%";
        String[] lCond = new String[] { "claim_form_index.userOwner LIKE '" + userId + "' " };



        if(col.size() >0){
             state2 =ClaimFormIndexModule.STATE_CLOSED;
            boolean activate = false;

            for (Iterator icount = col.iterator(); icount.hasNext();) {

                ClaimConfig object = (ClaimConfig)icount.next();


                if(object.getProperty1().equalsIgnoreCase(application.getCurrentUser().getId()))
                  activate= true;


            }

            if(activate == true){






        //String []lCond =null;// new String[]{"userOwner ='"+userId+"' "};
        if (state2.equals(ClaimFormIndexModule.STATE_CREATED)) {
            Log.getLog(this.getClass()).debug(" in created ");
            lCond = new String[] {
                    " claim_form_index.state = 'cre' ", " claim_form_index.status = 'act' ",
                    " (claim_form_index.userOwner = '" + userId + "' OR claim_form_index.userOriginator='" +
                    userId + "') "
                };
        } else if (state2.equals(ClaimFormIndexModule.STATE_SUBMITTED)) {
            Log.getLog(this.getClass()).debug(" in submitted");
            lCond = new String[] {
                    " claim_form_index.state = 'sub' ", " claim_form_index.status = 'act' ",
                    " (claim_form_index.userOwner = '" + userId + "' OR claim_form_index.userOriginator='" +
                    userId + "') "
                };
        } else if (state2.equals(ClaimFormIndexModule.STATE_APPROVED)) {
            Log.getLog(this.getClass()).debug(" in approved");
            lCond = new String[] {
                    " claim_form_index.state = 'app' ", " claim_form_index.status = 'act' ",
                    " (claim_form_index.userOwner = '" + userId + "' OR claim_form_index.userOriginator='" +
                    userId + "') "
                };
        } else if (state2.equals(ClaimFormIndexModule.STATE_ASSESSED)) {
            Log.getLog(this.getClass()).debug(" in assessed");
            lCond = new String[] {
                    " claim_form_index.state = 'ass' ", " claim_form_index.status = 'act' ",
                    " (claim_form_index.userOwner = '" + userId + "' OR claim_form_index.userOriginator='" +
                    userId + "') "
                };
        } else if (state2.equals(ClaimFormIndexModule.STATE_REJECTED)) {
            Log.getLog(this.getClass()).debug(" in rejected");
            lCond = new String[] {
                    " claim_form_index.state = 'rej' ", " claim_form_index.status = 'act' ",
                    " (claim_form_index.userOwner = '" + userId + "' OR claim_form_index.userOriginator='" +
                    userId + "') "
                };
        } else if (state2.equals(ClaimFormIndexModule.STATE_CLOSED)) {
            Log.getLog(this.getClass()).debug(" in rejected");
            lCond = new String[] {
                    " claim_form_index.state = 'clo' ", " claim_form_index.status = 'act' ",
                    " claim_form_index.userOwner LIKE '" + userId + "' "


                   /* " state LIKE '%%' ", " status LIKE '%%' ",
                                        " userOwner LIKE '" + userId + "' "
*/

                };
        }





            }
            else{

                 userId=null;
                state2=null;

                 lCond = new String[] {
                    " claim_form_index.state = 'NULL' ", " claim_form_index.status = 'NULL' ",
                    " claim_form_index.userOwner = 'NULL' "
                };
            }






        }







        return lCond;
    }



    
    public void init() 
	{
    	
    	setMethod("POST");
	}
    


    public void onRequest(Event evt)
	{
		removeChildren();
    	super.init();
		
		this.theModel = new ThisTableModel(getCondition(userId,this.state),state);
		setModel(this.theModel);
	}


    public class ThisTableModel extends ClaimHistoryFormIndexTableModel
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
                    TableColumn infoCol = new TableColumn("info",Application.getInstance().getMessage("claims.label.info","Info"));
                    infoCol.setSortable(false);
                    addColumn(infoCol);
                    TableColumn editCol = new TableColumn(null, "", false);
                    editCol.setLabel(Application.getInstance().getMessage("claims.label.edit","Edit"));
                    editCol.setUrl("../claim/user_editClaim.jsp");
                    editCol.setUrlParam("formId");
                    editCol.setSortable(false);
                    addColumn(editCol);

                    addAction(new TableAction("submit",Application.getInstance().getMessage("claims.category.submit","Submit"),
                    		Application.getInstance().getMessage("claims.message.editClaimConfim","Once submitted, you may not be able to edit this claim anymore. Are you sure?")));
                    addAction(new TableAction("delete",Application.getInstance().getMessage("claims.label.delete","Delete"),
                    		Application.getInstance().getMessage("claims.message.deleteClaimConfirm","Once deleted, it will be purged from the database permanently, are you sure?")));
                }

                if(state!=null && state.equals(ClaimFormIndexModule.STATE_SUBMITTED))
                {
                    addColumn(new TableColumn("info", Application.getInstance().getMessage("claims.label.info","Info")));
                TableColumn viewCol = new TableColumn(null, "", false);
                viewCol.setLabel(Application.getInstance().getMessage("claims.label.view","View"));
                viewCol.setUrl("view_claim.jsp");
                viewCol.setUrlParam("formId");
                addColumn(viewCol);

//            addAction(new TableAction("delete","Delete","Once deleted, it will be purged from the database permanently, are you sure?"));

                }

            if(state!=null && state.equals(ClaimFormIndexModule.STATE_APPROVED))
             {
                addColumn(new TableColumn("info", Application.getInstance().getMessage("claims.label.info","Info")));

                TableColumn viewCol = new TableColumn(null, "", false);
                viewCol.setLabel(Application.getInstance().getMessage("claims.label.view","View"));
                viewCol.setUrl("view_claim.jsp");
                viewCol.setUrlParam("formId");
                addColumn(viewCol);

             }

            if(state!=null && state.equals(ClaimFormIndexModule.STATE_ASSESSED))
             {
                    addColumn(new TableColumn("info", Application.getInstance().getMessage("claims.label.info","Info")));
                    addColumn(new TableColumn("assessorName", Application.getInstance().getMessage("claims.label.assessor","Assessor")));

                TableColumn viewCol = new TableColumn(null, "", false);
                viewCol.setLabel(Application.getInstance().getMessage("claims.label.view","View"));
                viewCol.setUrl("view_claim.jsp");
                viewCol.setUrlParam("formId");
                addColumn(viewCol);

                addAction(new TableAction("close","Acknowledge and Close","Are you sure?"));

             }

            if(state!=null && state.equals(ClaimFormIndexModule.STATE_REJECTED))
             {
                    addColumn(new TableColumn("info", Application.getInstance().getMessage("claims.label.info","Info")));
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
                addColumn(new TableColumn("info", Application.getInstance().getMessage("claims.label.info","Info")));

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
