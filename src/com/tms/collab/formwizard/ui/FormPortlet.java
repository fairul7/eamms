package com.tms.collab.formwizard.ui;

import kacang.ui.LightWeightWidget;
import kacang.ui.Event;
import kacang.Application;
import kacang.model.DaoQuery;
import kacang.util.Log;
import com.tms.collab.formwizard.model.FormModule;
import com.tms.collab.formwizard.model.FormDao;
import com.tms.collab.formwizard.model.FormDaoException;
import com.tms.portlet.Entity;
import com.tms.portlet.theme.ThemeManager;

import java.util.Collection;

public class FormPortlet extends LightWeightWidget {
    private Collection formList = null;
    public static final String SUBMISSION_LINK = "FormWizardListLink";
    public static final String PENDING_WORKFLOW_LINK = "FormWizardPendingWorkFlow";
    public static final String DEFAULT_SUBMISSION_LINK = "/ekms/formwizard/frwViewForm.jsp";
    public static final String DEFAULT_PENDING_WORKFLOW_LINK = "/ekms/formwizard/frwApproveFormData.jsp";
    private String link = DEFAULT_SUBMISSION_LINK ;
    private String workflowLink = DEFAULT_PENDING_WORKFLOW_LINK;
    private int pendingForm = 0;


    public void onRequest(Event evt) {

        Object obj = evt.getRequest().getAttribute(ThemeManager.LABEL_ENTITY);
        if (obj instanceof Entity) {
            Entity entity = (Entity) obj;
            FormModule module = (FormModule) Application.getInstance().getModule(FormModule.class);
            FormDao dao =(FormDao) module.getDao();
            try {
                setFormList(module.getViewForms(new DaoQuery(),evt.getWidgetManager().getUser().getId(),"formDisplayName",false, 0, -1));

                if (entity.getPreference(SUBMISSION_LINK) != null)
                    link = String.valueOf(entity.getPreference(SUBMISSION_LINK).getPreferenceValue());

                if (link == null)
                    link = DEFAULT_SUBMISSION_LINK;

                if (entity.getPreference(PENDING_WORKFLOW_LINK) != null)
                    workflowLink = String.valueOf(entity.getPreference(PENDING_WORKFLOW_LINK).getPreferenceValue());

                if (workflowLink == null)
                    workflowLink = DEFAULT_PENDING_WORKFLOW_LINK;

                pendingForm = dao.getPendingForms(new DaoQuery(),evt.getWidgetManager().getUser().getId());


            }
            catch (FormDaoException e) {
                Log.getLog(getClass()).error(e.getMessage(), e);
            }
        }




	}



    public String getDefaultTemplate() {
        return "formwizard/formPortlet";
    }

    public Collection getFormList() {
        return formList;
    }

    public void setFormList(Collection formList) {
        this.formList = formList;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getPendingForm() {
        return pendingForm;
    }

    public void setPendingForm(int pendingForm) {
        this.pendingForm = pendingForm;
    }

    public String getWorkflowLink() {
        return workflowLink;
    }

    public void setWorkflowLink(String workflowLink) {
        this.workflowLink = workflowLink;
    }

}
