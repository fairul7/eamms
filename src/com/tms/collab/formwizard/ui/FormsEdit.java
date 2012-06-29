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

import com.tms.collab.formwizard.model.FormConstants;
import com.tms.collab.formwizard.model.FormModule;
import com.tms.collab.formwizard.model.FormException;
import com.tms.collab.formwizard.model.FormDaoException;
import com.tms.util.FormatUtil;


public class FormsEdit extends Table{
    public FormsEdit() {
    }

    public FormsEdit(String s) {
        super(s);
    }

    public void init(){
       super.init();
       setPageSize(10);
       setModel(new FormsEditModel());
       setWidth("100%");
    }

    class FormsEditModel extends TableModel{
        FormsEditModel(){
            SecurityService security = (SecurityService)Application.getInstance().getService(SecurityService.class);
            try {
                if (security.hasPermission(getWidgetManager().getUser().getId(), FormConstants.FORM_DELETE_PERMISSION_ID, FormModule.class.getName(), null)) {
                    addAction(new TableAction("delete",Application.getInstance().getMessage("formWizard.label.formsEdit.delete","Delete"),Application.getInstance().getMessage("formWizard.label.formsEdit.confirmDelete","Are You Sure Delete the form?")));
                }
            } catch (SecurityException e) {
                Log.getLog(getClass()).error(e.toString());
            }
           TableColumn tcFormName= new TableColumn("formDisplayName",Application.getInstance().getMessage("formWizard.label.formsView.formName","Form Name"));
            tcFormName.setUrlParam("formId");
            addColumn(tcFormName);
            TableColumn tcDateCreated = new TableColumn("formDateCreated",Application.getInstance().getMessage("formWizard.label.formsView.createdOn","Created On"));
            tcDateCreated.setFormat(new TableDateFormat(FormatUtil.getInstance().getLongDateTimeFormat()));
            addColumn(tcDateCreated);
            TableColumn tcCreatedBy = new TableColumn("formUpdatedByName",Application.getInstance().getMessage("formWizard.label.formsView.createdBy","Created By"));
            addColumn(tcCreatedBy);

            addFilter(new TableFilter("search"));
        }

         public String getTableRowKey() {
            return "formId";
        }

        public Collection getTableRows() {
             try {
                 FormModule handler  = (FormModule) Application.getInstance().getModule(FormModule.class);
                 return handler.getEditForms(generateDaoProperties(),getSort(),isDesc(), getStart(), getRows());
             } catch (FormDaoException e) {
                 Log.getLog(getClass()).error(e.getMessage(),e);
                 return null;
             }
        }

        public int getTotalRowCount(){
             try {
                 FormModule handler  = (FormModule) Application.getInstance().getModule(FormModule.class);
                 return handler.getEditFormsCount(generateDaoProperties());
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
            String userID ="";
            User user= evt.getWidgetManager().getUser();
            userID = user.getId();
            SecurityService security = (SecurityService) Application.getInstance().getService(SecurityService.class);
            try {
                FormModule handler  = (FormModule) Application.getInstance().getModule(FormModule.class);
                for (int i = 0; i < selectedKeys.length; i++)  {
                	
                    if(security.hasPermission(userID, FormConstants.FORM_DELETE_PERMISSION_ID, FormModule.class.getName(), null))
						handler.deleteForms(selectedKeys[i]);                    
                     
                }
            } 
            catch (SecurityException e) {
                Log.getLog(getClass()).error(e.toString(), e);
            } 
			catch (FormException e) {
                Log.getLog(getClass()).error(e.toString(), e);
            }
            return super.processAction(evt, action, selectedKeys);
        }
    }
}
