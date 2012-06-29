package com.tms.collab.formwizard.ui;

import kacang.stdui.*;
import kacang.Application;
import kacang.model.DaoQuery;
import kacang.model.operator.OperatorParenthesis;
import kacang.model.operator.DaoOperator;
import kacang.model.operator.OperatorLike;
import kacang.ui.Forward;
import kacang.ui.Event;
import kacang.util.Log;

import java.util.Collection;

import com.tms.collab.formwizard.model.*;
import com.tms.util.FormatUtil;

public class FormsDraft extends Table {
     public void init() {
        super.init();
        setModel(new FormsDraft.FormsDraftModel());
        setWidth("100%");
    }

    class FormsDraftModel extends TableModel {
        FormsDraftModel() {
            addAction(new TableAction("delete", Application.getInstance().getMessage("formWizard.label.formsDraft.delete","Delete"),Application.getInstance().getMessage("formWizard.label.formsDraft.confirmDelete","Are You Sure Delete the draft?")));

            TableColumn tcFormName = new TableColumn("formDisplayName", Application.getInstance().getMessage("formWizard.label.formsDraft.formName","Form Name"));
            tcFormName.setUrlParam("formUid");
            addColumn(tcFormName);

            TableColumn tcSubmissionDate = new TableColumn("formSubmissionDate", Application.getInstance().getMessage("formWizard.label.formsDraft.submissionDate","Submission Date"));
            tcSubmissionDate.setFormat(new TableDateFormat(FormatUtil.getInstance().getLongDateTimeFormat()));
            addColumn(tcSubmissionDate);

            addFilter(new TableFilter("search"));
        }

        public String getTableRowKey() {
            return "formUid";
        }

        public Collection getTableRows() {
            FormModule handler  = (FormModule) Application.getInstance().getModule(FormModule.class);
            try {
                return handler.getFormDraft(generateDaoProperties(),getWidgetManager().getUser().getId(),getSort(),isDesc(),
                                            getStart(), getRows());
            }
            catch (FormDaoException e) {
                Log.getLog(getClass()).error(e.getMessage(),e);
                return null;
            }

        }

        public int getTotalRowCount() {
            FormModule handler  = (FormModule) Application.getInstance().getModule(FormModule.class);
            try {
                return handler.getFormDraftCount(generateDaoProperties(), getWidgetManager().getUser().getId());
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

        public Forward processAction(Event evt, String action, String[] selectedKeys){
            FormModule handler  = (FormModule) Application.getInstance().getModule(FormModule.class);
            for (int i = 0; i < selectedKeys.length; i++)  {
                try {
                    handler.deleteDraft(selectedKeys[i]);
                }
                catch (FormDaoException e) {
                    Log.getLog(getClass()).error(e.getMessage(),e);
                }                
                catch (FormDocumentException e) {
                   Log.getLog(getClass()).error(e.getMessage(),e);
                }
            }
             return super.processAction(evt, action, selectedKeys);
        }
    }
}
