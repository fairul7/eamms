package com.tms.collab.formwizard.ui;

import kacang.stdui.*;
import kacang.Application;
import kacang.model.DaoQuery;
import kacang.model.operator.OperatorParenthesis;
import kacang.model.operator.DaoOperator;
import kacang.model.operator.OperatorLike;
import kacang.util.Log;

import com.tms.collab.formwizard.model.FormModule;
import com.tms.collab.formwizard.model.FormDaoException;
import com.tms.util.FormatUtil;

import java.util.Collection;
public class FormsView extends Table{
    public FormsView() {
    }

    public FormsView(String s) {
        super(s);
    }

    public void init(){
           super.init();
           setPageSize(10);
           setModel(new FormsView.FormsViewModel());
           setWidth("100%");
       }

    class FormsViewModel extends TableModel{
        FormsViewModel(){
            TableColumn tcFormName= new TableColumn("formDisplayName", Application.getInstance().getMessage("formWizard.label.formsView.formName","Form Name"));
            tcFormName.setUrlParam("formId");
            addColumn(tcFormName);
            TableColumn tcDateCreated = new TableColumn("formDateCreated", Application.getInstance().getMessage("formWizard.label.formsView.createdOn","Created On"));
            tcDateCreated.setFormat(new TableDateFormat(FormatUtil.getInstance().getLongDateTimeFormat()));
            addColumn(tcDateCreated);
            TableColumn tcCreatedBy = new TableColumn("formUpdatedByName", Application.getInstance().getMessage("formWizard.label.formsView.updatedBy","Created By"));
            addColumn(tcCreatedBy);
            addFilter(new TableFilter("search"));
        }



        public Collection getTableRows() {
             try {
                 generateDaoProperties();
                 FormModule handler  = (FormModule) Application.getInstance().getModule(FormModule.class);
                 return handler.getViewForms(generateDaoProperties(),getWidgetManager().getUser().getId(),getSort(),isDesc(), getStart(), getRows());
             } catch (FormDaoException e) {
                 Log.getLog(getClass()).error(e.getMessage(), e);
                 return null;
             }
        }

        public int getTotalRowCount(){
             try {
                 FormModule handler  = (FormModule) Application.getInstance().getModule(FormModule.class);
                 return handler.getViewFormsCount(generateDaoProperties(),getWidgetManager().getUser().getId());
             }
             catch (FormDaoException e) {
                 Log.getLog(getClass()).error(e.getMessage(), e);
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
