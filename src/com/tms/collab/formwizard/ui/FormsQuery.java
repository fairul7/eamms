package com.tms.collab.formwizard.ui;

import kacang.stdui.*;
import kacang.Application;
import kacang.model.DaoQuery;
import kacang.model.operator.OperatorParenthesis;
import kacang.model.operator.DaoOperator;
import kacang.model.operator.OperatorLike;
import kacang.services.security.User;
import kacang.services.security.SecurityService;
import kacang.services.security.SecurityException;
import kacang.ui.Forward;
import kacang.ui.Event;
import kacang.util.Log;

import java.util.Collection;

import com.tms.collab.formwizard.model.FormModule;
import com.tms.collab.formwizard.model.FormException;
import com.tms.collab.formwizard.model.FormDaoException;
import com.tms.util.FormatUtil;



public class FormsQuery extends Table{
    public FormsQuery() {
    }

    public FormsQuery(String s) {
        super(s);
    }

    public void init(){
       super.init();
       setPageSize(10);
       setModel(new FormsQueryModel());
       setWidth("100%");
    }

    class FormsQueryModel extends TableModel{
        FormsQueryModel(){
            TableColumn tcFormName= new TableColumn("formDisplayName",Application.getInstance().getMessage("formWizard.label.formsView.formName","Form Name"));
            tcFormName.setUrlParam("formId");
            addColumn(tcFormName);
            TableColumn tcDateCreated = new TableColumn("formDateCreated",Application.getInstance().getMessage("formWizard.label.formsView.createdOn","Created On"));
            tcDateCreated.setFormat(new TableDateFormat(FormatUtil.getInstance().getLongDateTimeFormat()));
            addColumn(tcDateCreated);
            TableColumn tcCreatedBy = new TableColumn("formsUpdatedByName",Application.getInstance().getMessage("formWizard.label.formsView.createdBy","Created By"));
            addColumn(tcCreatedBy);

            addFilter(new TableFilter("search"));
        }

        public Collection getTableRows() {
             try {
                 FormModule handler  = (FormModule) Application.getInstance().getModule(FormModule.class);
                 return handler.getQueryForms(generateDaoProperties(), getWidgetManager().getUser().getId(),
                                              getSort(),isDesc(), getStart(), getRows());
             } catch (FormDaoException e) {
                 Log.getLog(getClass()).error(e.getMessage(),e);
                 return null;
             }
        }

        public int getTotalRowCount(){
             try {
                 FormModule handler  = (FormModule) Application.getInstance().getModule(FormModule.class);
                 return handler.getQueryFormsCount(generateDaoProperties(),getWidgetManager().getUser().getId());
             } catch (FormDaoException e) {
                 Log.getLog(getClass()).error(e.getMessage(),e);
                 return 0;
             }
        }

         public DaoQuery generateDaoProperties() {
            DaoQuery properties = new DaoQuery();
            OperatorParenthesis op = new OperatorParenthesis(DaoOperator.OPERATOR_AND);
            op.addOperator(new OperatorLike("formDisplayName",getFilterValue("search"),null));
             op.addOperator(new OperatorLike("formHeader",getFilterValue("search"),DaoOperator.OPERATOR_OR));
            properties.addProperty(op);
            return properties;
         }

        public Forward processAction(Event evt, String action, String[] selectedKeys){
            Log log = Log.getLog(this.getClass());
            log.debug("~~~ action = " + action);
            String userID ="";
            User user= evt.getWidgetManager().getUser();
            userID = user.getId();
            SecurityService security = (SecurityService) Application.getInstance().getService(SecurityService.class);
            try {
                FormModule handler  = (FormModule) Application.getInstance().getModule(FormModule.class);
                for (int i = 0; i < selectedKeys.length; i++)  {
                    if(security.hasPermission(userID, "com.tms.collab.formwizard.Query", "com.tms.collab.formwizard.model.FormModule", null))
                        handler.deleteForms(selectedKeys[i]);
                }
            } catch (SecurityException e) {
                Log.getLog(getClass()).error(e.toString(), e);
            } catch (FormException e) {
                Log.getLog(getClass()).error(e.toString(), e);
            }
            return super.processAction(evt, action, selectedKeys);
        }
    }
}
