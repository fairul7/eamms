/**
 * @author Vincent Lee, Wavelet Solutions Sdn Bhd (632468-W)
 * Created On: 2004-06-18
 * Copyright of The Media Shoppe Berhad
 */
package com.tms.hr.claim.ui;

import com.tms.hr.claim.model.ClaimFormIndexModule;

import kacang.Application;

import kacang.services.security.SecurityService;
import kacang.services.security.User;

import kacang.stdui.TableAction;
import kacang.stdui.TableColumn;

import kacang.ui.Event;

import kacang.util.Log;


public class ClaimFormIndexTableApprover extends ClaimFormIndexGenericTable {
    protected ThisTableModel theModel;
    String state = null;

    public void setState(String state) {
        this.state = state;
    }

    public void init() {
    }

    public void onRequest(Event evt) {
        super.init();
        init();
        this.theModel = new ThisTableModel(getCondition(userId, this.state),
                state);
        setModel(this.theModel);
    }

    protected String[] getCondition(String userId, String state2) {
        super.init();

        // based on the state/usecase
        // determine the search criteria
        String[] lCond = new String[] {
                " ( userApprover1 ='" + userId + "' " + " OR userApprover2 ='" +
                userId + "' " + " OR userApprover3 ='" + userId + "' " +
                " OR userApprover4 ='" + userId + "' ) "
            };

        //String []lCond =null;// new String[]{"userApprover ='"+userId+"' "};
        if (state2.equals(ClaimFormIndexModule.STATE_CREATED)) {
            Log.getLog(this.getClass()).debug(" in created ");
            lCond = new String[] {
                    " state = 'cre' ", " status = 'act' ",

                    " ( userApprover1 ='" + userId + "' " +
                    " OR userApprover2 ='" + userId + "' " +
                    " OR userApprover3 ='" + userId + "' " +
                    " OR userApprover4 ='" + userId + "' ) "
                };
        } else if (state2.equals(ClaimFormIndexModule.STATE_SUBMITTED)) {
            Log.getLog(this.getClass()).debug(" in submitted");

            String userName = "";

            try {
                SecurityService ss = (SecurityService) Application.getInstance()
                                                                  .getService(SecurityService.class);
                User user = ss.getUser(userId);
                userName = user.getName();
            } catch (Exception e) {
            }

            lCond = new String[] {
                    " state = 'sub' ", " status = 'act' ",

                    " ( userApprover1 ='" + userId + "' " +
                    " OR userApprover2 ='" + userId + "' " +
                    " OR userApprover3 ='" + userId + "' " +
                    " OR userApprover4 ='" + userId + "' ) ",
                    " (approvedBy IS NOT NULL AND approvedBy not like '%" +
                    userName + "%')"
                };
        } else if (state2.equals(ClaimFormIndexModule.STATE_APPROVED)) {
            Log.getLog(this.getClass()).debug(" in approved");
            lCond = new String[] {
                    " state = 'app' ", " status = 'act' ",

                    " ( userApprover1 ='" + userId + "' " +
                    " OR userApprover2 ='" + userId + "' " +
                    " OR userApprover3 ='" + userId + "' " +
                    " OR userApprover4 ='" + userId + "' ) "
                };
        } else if (state2.equals(ClaimFormIndexModule.STATE_ASSESSED)) {
            Log.getLog(this.getClass()).debug(" in assessed");
            lCond = new String[] {
                    " state = 'ass' ", " status = 'act' ",

                    " ( userApprover1 ='" + userId + "' " +
                    " OR userApprover2 ='" + userId + "' " +
                    " OR userApprover3 ='" + userId + "' " +
                    " OR userApprover4 ='" + userId + "' ) "
                };
        } else if (state2.equals(ClaimFormIndexModule.STATE_REJECTED)) {
            Log.getLog(this.getClass()).debug(" in rejected");
            lCond = new String[] {
                    " state = 'rej' ", " status = 'act' ",

                    " ( userApprover1 ='" + userId + "' " +
                    " OR userApprover2 ='" + userId + "' " +
                    " OR userApprover3 ='" + userId + "' " +
                    " OR userApprover4 ='" + userId + "' ) "
                };
        }

        return lCond;
    }

    public class ThisTableModel extends ClaimFormIndexTableModel {
        String state = null;

        public ThisTableModel(String[] theCondition, String state) {
            super(theCondition);
            super.setState(state);
            this.state = state;
            setColumns();
            setActionLinks();
        }

        public void printConditions() {
            for (int cnt = 0; cnt < theCondition.length; cnt++) {
                Log.getLog(this.getClass()).debug(" Condition[" + cnt + "]=" +
                    theCondition[cnt]);
            }
        }

        protected void setActionLinks() {
            printConditions();

            if ((state != null) &&
                    state.equals(ClaimFormIndexModule.STATE_SUBMITTED)) {
                //	addColumn(new TableColumn("rejectReason", "Reject Reason"));
                addColumn(new TableColumn("info", "Info"));

                TableColumn editCol = new TableColumn(null, "", false);
                editCol.setLabel("View");
                editCol.setUrl("view_claim.jsp");
                editCol.setUrlParam("formId");
                addColumn(editCol);

                addAction(new TableAction("reject", "Reject",
                        "Are you sure you want to reject these expenses?"));
                addAction(new TableAction("approve", "Approve",
                        "Once approved, you may not be able to reject the selected expenses. Are you sure?"));
            }

            //			TableColumn submitCol = new TableColumn
        }
    }
}
