package com.tms.collab.formwizard.ui;

import kacang.stdui.*;
import kacang.Application;
import kacang.services.security.User;
import kacang.ui.Forward;
import kacang.ui.Event;
import kacang.model.DaoQuery;
import kacang.model.operator.OperatorParenthesis;
import kacang.model.operator.DaoOperator;
import kacang.model.operator.OperatorLike;
import kacang.util.Log;

import java.util.Collection;

import com.tms.collab.formwizard.model.*;
import com.tms.util.FormatUtil;


public class ApproveFormData extends Table{
    private String formUID;
    public ApproveFormData() {
    }

    public ApproveFormData(String s) {
        super(s);
    }
    public void init(){
       super.init();
       setPageSize(10);
       setModel(new ApproveFormDataModel());
       setWidth("100%");
    }

    public String getFormUID() {
        return formUID;
    }

    public void setFormUID(String formUID) {
        this.formUID = formUID;
    }

    class  ApproveFormDataModel extends TableModel{
        ApproveFormDataModel(){
            addAction(new TableAction("approve", Application.getInstance().getMessage("formWizard.label.approveFormData.approve","Approve"),Application.getInstance().getMessage("formWizard.label.approveFormData.confirmApprove","Approve Selected Item(s)?")));
            TableColumn tcName = new TableColumn("name", Application.getInstance().getMessage("formWizard.label.approveFormData.postedBy","Posted By"));
            tcName.setUrlParam("formUid");
            addColumn(tcName);
            TableColumn tcFormName = new TableColumn("formDisplayName", Application.getInstance().getMessage("formWizard.label.approveFormData.formName","Form Name"));
            addColumn(tcFormName);
            // fixed Bug # 3652
            TableColumn tcDatePosted = new TableColumn("formSubmissionDate","Posted Date");
            tcDatePosted.setFormat(new TableDateFormat(FormatUtil.getInstance().getLongDateTimeFormat()));

            addColumn(tcDatePosted);

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

        public String getTableRowKey() {
            return "formUid";
        }

         public Collection getTableRows() {
             Collection cForms=null;
             try {
                 FormModule handler  = (FormModule) Application.getInstance().getModule(FormModule.class);
                 FormDao dao =(FormDao) handler.getDao();
                 cForms= dao.getPendingForms(generateDaoProperties(),getWidgetManager().getUser().getId(),getSort(),isDesc(), getStart(), getRows());
                 if(cForms!=null && cForms.iterator().hasNext()){
                     FormWorkFlowDataObject  wfDO = (FormWorkFlowDataObject)cForms.iterator().next();
                     setFormUID(wfDO.getFormUid());
                 }
             }
             catch (FormDaoException e) {
                 Log.getLog(getClass()).error(e.getMessage(),e);
             }
             return cForms;
        }
        public int getTotalRowCount(){
             try {
                 FormModule handler  = (FormModule) Application.getInstance().getModule(FormModule.class);
                 FormDao dao =(FormDao) handler.getDao();
                 return dao.getPendingForms(generateDaoProperties(),getWidgetManager().getUser().getId());
            }catch (FormDaoException e) {
                 Log.getLog(getClass()).error(e.toString());
                 return 0;
             }
        }

        public Forward processAction(Event evt, String action, String[] selectedKeys){
            FormModule handler  = (FormModule) Application.getInstance().getModule(FormModule.class);
             User user = getWidgetManager().getUser();
             String userId = user.getId();
            try{
                for (int i = 0; i < selectedKeys.length; i++)  {
                    handler.approveFormData(selectedKeys[i],action,userId,evt);
                }
            }
            catch (FormException e){
                Log.getLog(getClass()).error(e.toString());
            }
            catch (FormDaoException e) {
                Log.getLog(getClass()).error(e.toString());
            }
            catch (FormDocumentException e) {
                Log.getLog(getClass()).error(e.getMessage(),e);
            }
            return super.processAction(evt, action, selectedKeys);
        }
    }
}
