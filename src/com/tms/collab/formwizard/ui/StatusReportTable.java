package com.tms.collab.formwizard.ui;

import kacang.stdui.*;
import kacang.Application;
import kacang.model.DaoException;
import kacang.model.DaoQuery;
import kacang.model.operator.OperatorParenthesis;
import kacang.model.operator.DaoOperator;
import kacang.model.operator.OperatorLike;
import kacang.util.Log;

import java.util.Collection;

import com.tms.collab.formwizard.model.FormModule;
import com.tms.collab.formwizard.model.FormDao;
import com.tms.collab.formwizard.model.FormDaoException;
import com.tms.util.FormatUtil;


public class StatusReportTable extends Table{
    public StatusReportTable() {
    }

    public StatusReportTable(String s) {
        super(s);
    }
    public void init(){
        super.init();
        setPageSize(10);
        setModel(new StatusReportTableModel());
        setWidth("100%");
    }

    class StatusReportTableModel extends TableModel{
        StatusReportTableModel(){
            TableColumn tcFormName = new TableColumn("formDisplayName", Application.getInstance().getMessage("formWizard.label.statusReportTable.formName","Form Name"));
            addColumn(tcFormName);
            tcFormName.setUrlParam("formUid");            
            TableColumn tcApproverName = new TableColumn("approverName", Application.getInstance().getMessage("formWizard.label.statusReportTable.formPlacedAt","Form Placed At"));
            addColumn(tcApproverName);
            TableColumn tcStatus  = new TableColumn("status", Application.getInstance().getMessage("formWizard.label.statusReportTable.status","Status"));
            addColumn(tcStatus);
            TableColumn tcSubmissionDate = new TableColumn("formSubmissionDate", Application.getInstance().getMessage("formWizard.label.statusReportTable.submissionDate","Submission Date"));
            tcSubmissionDate.setFormat(new TableDateFormat(FormatUtil.getInstance().getLongDateTimeFormat()));
            addColumn(tcSubmissionDate);

            addFilter(new TableFilter("search"));
        }

        public DaoQuery generateDaoProperties() {
            DaoQuery properties = new DaoQuery();
            OperatorParenthesis op = new OperatorParenthesis(DaoOperator.OPERATOR_AND);
            op.addOperator(new OperatorLike("formDisplayName",getFilterValue("search"),null));
            op.addOperator(new OperatorLike("formHeader",getFilterValue("search"),DaoOperator.OPERATOR_OR));
            properties.addProperty(op);
            return properties;
        }

        public Collection getTableRows(){
             try {
                 FormModule handler  = (FormModule) Application.getInstance().getModule(FormModule.class);
                 FormDao  dao = (FormDao) handler.getDao();
                 return dao.selectFormsWorkFlow(generateDaoProperties(),getWidgetManager().getUser().getId(),getSort(),
                                                isDesc(), getStart(), getRows());
             }
             catch (FormDaoException e) {
                 Log.getLog(getClass()).error(e.getMessage(),e);
                 return null;
             }
        }

        public int getTotalRowCount() {
             try {
                 FormModule handler  = (FormModule) Application.getInstance().getModule(FormModule.class);
                 FormDao  dao = (FormDao) handler.getDao();
                 return dao.selectFormsWorkFlow(generateDaoProperties(),getWidgetManager().getUser().getId());
             }
             catch (FormDaoException e) {
                 Log.getLog(getClass()).error(e.getMessage(),e);
                 return 0;
             }
        }
    }
}
