package com.tms.collab.formwizard.ui;

import kacang.stdui.*;
import kacang.Application;
import kacang.model.DaoQuery;
import kacang.model.operator.OperatorParenthesis;
import kacang.model.operator.DaoOperator;
import kacang.model.operator.OperatorLike;
import kacang.util.Log;

import java.util.Collection;

import com.tms.collab.formwizard.model.FormModule;
import com.tms.collab.formwizard.model.FormException;
import com.tms.collab.formwizard.model.FormDaoException;

public class SubmittedFormHistory extends Table {
    public void init() {
        super.init();
        setModel(new SubmittedFormHistory.SubmittedFormHistoryModel());
        setWidth("100%");
    }

    class SubmittedFormHistoryModel extends TableModel {
        SubmittedFormHistoryModel() {
            TableColumn tcFormName = new TableColumn("formDisplayName", Application.getInstance().getMessage("formWizard.label.submittedFormHistory.formName","Form Name"));
            tcFormName.setUrlParam("formId");
            addColumn(tcFormName);

            addFilter(new TableFilter("search"));

        }

         public Collection getTableRows() {
             try {
                 FormModule handler  = (FormModule) Application.getInstance().getModule(FormModule.class);
                 return handler.getViewForms(generateDaoProperties(),getWidgetManager().getUser().getId(),getSort(),isDesc(), getStart(), getRows());
             } catch (FormDaoException e) {
                 Log.getLog(getClass()).error(e.getMessage(),e);
                 return null;
             }
         }

         public int getTotalRowCount(){
             try {
                 FormModule handler  = (FormModule) Application.getInstance().getModule(FormModule.class);
                 return handler.getViewFormsCount(generateDaoProperties(),getWidgetManager().getUser().getId());
             }
             catch (FormDaoException e) {
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
    }
}
