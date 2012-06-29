package com.tms.collab.formwizard.ui;


import kacang.stdui.TextField;
import kacang.stdui.Form;
import kacang.stdui.Button;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.stdui.validator.ValidatorIsNumeric;
import kacang.ui.Forward;
import kacang.ui.Event;
import kacang.util.Log;
import kacang.Application;
import com.tms.collab.formwizard.model.*;
import org.apache.commons.lang.StringEscapeUtils;

public class AddFormTemplate extends Form{
    protected TextField templateNameTextField;
    protected Button addButton;
    private String templateId;
    protected TextField tfTableColumn;


    public void onRequest(Event event) {
        initializeFields();
    }

    public void init() {
        templateNameTextField = new TextField("templateNameTextField");
        templateNameTextField.addChild(new ValidatorNotEmpty("notEmpty", Application.getInstance().getMessage("formWizard.label.addFormTemplate.templateNotEmpty","Template Name cannot be empty")));
        addButton = new Button("addButton");
        addButton.setText(Application.getInstance().getMessage("formWizard.label.addFormTemplate.add","Add"));

        addChild(templateNameTextField);
        addChild(addButton);

        tfTableColumn = new TextField("tableColumn");
        tfTableColumn.addChild(new ValidatorIsNumeric("isNumric", Application.getInstance().getMessage("formWizard.label.addFormTemplate.tableColumnnBeNumeric","Table column must be numeric")));
        tfTableColumn.setSize("4");
        tfTableColumn.setValue("2");
        addChild(tfTableColumn);

    }

    public String getDefaultTemplate() {
        return "formwizard/addFormTemplate";
    }

    public void initializeFields() {
        templateNameTextField.setValue("");
        tfTableColumn.setValue("2");

        initializeValidation();
    }

    public void initializeValidation() {
        templateNameTextField.setMessage("");
        templateNameTextField.setInvalid(false);



        tfTableColumn.setMessage("");
        tfTableColumn.setInvalid(false);

    }

    public void verifyField() {
        if (templateNameTextField.getValue() != null){
            FormModule module = (FormModule) Application.getInstance().getModule(FormModule.class);

            try {
                boolean exist = module.isFormTemplateNameExist(StringEscapeUtils.escapeSql(templateNameTextField.getValue().toString()));
                if (exist) {
                    templateNameTextField.setInvalid(true);
                    templateNameTextField.setMessage(Application.getInstance().getMessage("formWizard.label.addFormTemplate.templateNameExist","Template name already exist !"));
                    setInvalid(true);
                }
            }
            catch (FormDaoException e) {
                templateNameTextField.setInvalid(true);
                templateNameTextField.setMessage(Application.getInstance().getMessage("formWizard.label.addFormTemplate.errorChecking","Error checking the duplication of this template name"));
                setInvalid(true);
                Log.getLog(getClass()).error(e.getMessage(), e);
           }
        }
        if (!tfTableColumn.isInvalid() && !tfTableColumn.getValue().toString().equals("")
            &&(Integer.parseInt(tfTableColumn.getValue().toString())) < 2) {
            tfTableColumn.setInvalid(true);
            tfTableColumn.setMessage(Application.getInstance().getMessage("formWizard.label.addFormTemplate.tableColumnOne","Table column value must be greater than 1"));
            setInvalid(true);
        }
    }

    public Forward onValidate(Event evt){
            Forward fwd = null;

            String buttonName = findButtonClicked(evt);

            if (buttonName!=null && addButton.getAbsoluteName().equals(buttonName)){
                fwd = manageTemplate();
            }


            return fwd;
    }

    public Forward onSubmit(Event evt) {
        initializeValidation();
        super.onSubmit(evt);
        verifyField();
        return null;
    }

    public Forward manageTemplate() {
        Forward forward = null;
        FormTemplate template = null;
        FormModule module = (FormModule) Application.getInstance().getModule(FormModule.class);
        try {
            template = setFormTemplate();
            module.addFormTemplate(template);
            setTemplateId(template.getFormTemplateId());
            forward = new Forward("formTemplateAdded");
        }
        catch (FormDaoException e) {
            Log.getLog(getClass()).error(e.getMessage(),e);
        }
        catch (FormException e) {
            Log.getLog(getClass()).error(e.getMessage(),e);
        }
        catch (FormDocumentException e) {
            Log.getLog(getClass()).error(e.getMessage(),e);
        }


        return forward;
    }


    public FormTemplate setFormTemplate() throws FormDaoException, FormDocumentException {
        FormTemplate template = null;


        template = new FormTemplate();
        template.setTemplateName(templateNameTextField.getValue().toString());
        template.setTableColumn(tfTableColumn.getValue().toString());
        return template;
    }





    public TextField getTemplateNameTextField() {
        return templateNameTextField;
    }

    public void setTemplateNameTextField(TextField templateNameTextField) {
        this.templateNameTextField = templateNameTextField;
    }

    public Button getAddButton() {
        return addButton;
    }

    public void setAddButton(Button addButton) {
        this.addButton = addButton;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public TextField getTfTableColumn() {
        return tfTableColumn;
    }

    public void setTfTableColumn(TextField tfTableColumn) {
        this.tfTableColumn = tfTableColumn;
    }


}

