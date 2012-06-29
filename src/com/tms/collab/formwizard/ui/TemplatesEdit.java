package com.tms.collab.formwizard.ui;

import kacang.stdui.*;
import kacang.Application;
import kacang.model.DaoQuery;
import kacang.model.operator.OperatorParenthesis;
import kacang.model.operator.DaoOperator;
import kacang.model.operator.OperatorLike;
import kacang.ui.Forward;
import kacang.ui.Event;
import kacang.services.security.*;
import kacang.services.security.SecurityException;
import kacang.util.Log;

import java.util.Collection;

import com.tms.collab.formwizard.model.*;

public class TemplatesEdit extends Table {

    public void init() {
        super.init();
        setModel(new TemplatesEditModel());
        setWidth("100%");
    }

    class TemplatesEditModel extends TableModel {

        TemplatesEditModel() {
            SecurityService security = (SecurityService)Application.getInstance().getService(SecurityService.class);
            try {
                if (security.hasPermission(getWidgetManager().getUser().getId(), FormConstants.FORM_DELETE_PERMISSION_ID, FormModule.class.getName(), null)) {
                    addAction(new TableAction("delete",Application.getInstance().getMessage("formWizard.label.TemplatesEdit.delete","Delete"),Application.getInstance().getMessage("formWizard.label.TemplatesEdit.confirmDelete","Are You Sure Delete the Template?")));
                }
            } catch (kacang.services.security.SecurityException e) {
                Log.getLog(getClass()).error(e.toString());
            }

            TableColumn templateNameCol = new TableColumn("templateName", Application.getInstance().getMessage("formWizard.label.templatesEdit.templateName","Template Name"));
            templateNameCol.setUrlParam("formTemplateId");
            addColumn(templateNameCol);

            addFilter(new TableFilter("search"));

        }

         public DaoQuery generateDaoProperties() {
            DaoQuery properties = new DaoQuery();
            properties.addProperty(new OperatorLike("templateName",getFilterValue("search"),null));
            return properties;             
         }

        public Collection getTableRows() {
            FormModule module = (FormModule) Application.getInstance().getModule(FormModule.class);
            Collection data = null;
            try {
                data = module.getFormTemplate(generateDaoProperties(),getSort(),isDesc(), getStart(), getRows());
            }
            catch (FormDaoException e) {
                Log.getLog(getClass()).error(e.getMessage(),e);
            }
            return data;
        }

         public String getTableRowKey() {
            return "formTemplateId";
        }

         public int getTotalRowCount(){
             try {
                 FormModule module  = (FormModule) Application.getInstance().getModule(FormModule.class);
                 return module.getFormTemplateCount(generateDaoProperties());
             }
             catch (FormDaoException e) {
                 Log.getLog(getClass()).error(e.getMessage(),e);
                 return 0;
             }
        }

        public Forward processAction(Event evt, String action, String[] selectedKeys){
            String userID ="";
            User user= evt.getWidgetManager().getUser();
            userID = user.getId();
            SecurityService security = (SecurityService) Application.getInstance().getService(SecurityService.class);
            FormTemplate formTemplate = null;
            try {
                FormModule handler  = (FormModule) Application.getInstance().getModule(FormModule.class);
                formTemplate = new FormTemplate();
                for (int i = 0; i < selectedKeys.length; i++)  {

                    if(security.hasPermission(userID, FormConstants.FORM_DELETE_PERMISSION_ID, FormModule.class.getName(), null)) {
                        formTemplate.setFormTemplateId(selectedKeys[i]);
						handler.deleteFormTemplate(formTemplate);
                    }

                }
            }
            catch (SecurityException e) {
                Log.getLog(getClass()).error("Error getting the form wizard delete permission - userId:" + userID, e);
            }
			catch (FormException e) {
                Log.getLog(getClass()).error(e.getMessage(), e);
            }
            catch (FormDaoException e) {
                Log.getLog(getClass()).error(e.getMessage(), e);
            }
            catch (FormDocumentException e) {
                Log.getLog(getClass()).error(e.getMessage(), e);
            }
            return super.processAction(evt, action, selectedKeys);
        }
    }
}
