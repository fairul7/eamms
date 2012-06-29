package com.tms.collab.formwizard.ui;

import com.tms.collab.calendar.model.Appointment;
import com.tms.collab.calendar.model.CalendarEvent;
import com.tms.collab.formwizard.engine.*;
import com.tms.collab.formwizard.model.FormModule;
import com.tms.collab.formwizard.model.FormDaoException;
import com.tms.collab.formwizard.model.FormConstants;
import com.tms.collab.formwizard.model.Util;
import kacang.Application;
import kacang.stdui.Form;
import kacang.ui.Forward;
import kacang.ui.Event;
import kacang.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.*;

import org.w3c.dom.NamedNodeMap;

public class DynamicEditFormDataField extends DynamicViewFormField {
    private String id;
    private String url;


    public void onRequest(Event event) {
        url = event.getRequest().getScheme() + "://" + event.getRequest().getServerName() + ":"
              + event.getRequest().getServerPort() + event.getRequest().getContextPath() + "/storage";
        //super.initEngine("edit");
        super.initEngine();
    }
    
   
    public Forward actionPerformed(Event event) {

        Forward forward = super.actionPerformed(event);
        String buttonClicked = findButtonClicked(event);

        if (buttonClicked.endsWith("print")) {
            resetAllFormStatus(getLayout().getFieldList());
            setEmptyIndication();
        }

        if (buttonClicked.endsWith(CANCEL_FORM_ACTION)) {
            return new Forward("cancel");
        }
        else if (buttonClicked.endsWith("print")) {
            return new Forward("print");
        }


        return forward;
    }

    public Map getData() throws FormDaoException {
        FormModule module = (FormModule) Application.getInstance().getModule(FormModule.class);
        return module.getFormDetailMap(getFormId(), getId());
    }
   
    public void parseLayout(List fieldList) {


        Object object;
        FileField fileField;
        Map map = new HashMap();
        int index;

        for (Iterator iterator = fieldList.iterator(); iterator.hasNext();) {
            object = iterator.next();

            if (object instanceof FileField) {
                fileField = (FileField) object;
                if (fileField.getValue() != null) {
                    map.put(object, createFileLinkCheckbox(fileField.getName(), (String) fileField.getValue(),
                                                           fileField.getColspan(), fileField.getRowspan(),
                                                           fileField.getAlign(), fileField.getValign(),
                                                           fileField.getRequire()));
                }

            }


        }

        Set set = map.keySet();

        for (Iterator iterator = set.iterator(); iterator.hasNext();) {
            object = iterator.next();

            index = fieldList.indexOf(object);
            fieldList.set(index, map.get(object));
        }

        fieldList.add(0, addHiddenFormUidField());

        super.parseLayout(fieldList);


    }

    public void resetAllValidator(List fieldList) {

        Object object;
        ValidatorIsIntegerField validatorIsInteger;
        ValidatorEmailField validatorEmail;
        ValidatorIsNumericField validatorIsNumeric;

        for (Iterator iterator = fieldList.iterator(); iterator.hasNext();) {
            object = iterator.next();

            if (object instanceof TextFieldField) {
                TextFieldField textFieldField = (TextFieldField) object;
                if (textFieldField.getValidatorEmail() != null &&
                    ValidatorField.VALIDATOR_INVALID.equals(textFieldField.getValidatorEmail().isInvalid())) {
                    textFieldField.setInvalid(Field.FIELD_VALID);
                    validatorEmail = textFieldField.getValidatorEmail();
                    validatorEmail.setInvalid(ValidatorField.VALIDATOR_VALID);
                }

                if (textFieldField.getValidatorIsInteger() != null &&
                    ValidatorField.VALIDATOR_INVALID.equals(textFieldField.getValidatorIsInteger().isInvalid())) {
                    textFieldField.setInvalid(Field.FIELD_VALID);
                    validatorIsInteger = textFieldField.getValidatorIsInteger();
                    validatorIsInteger.setInvalid(ValidatorField.VALIDATOR_VALID);
                }


                if (textFieldField.getValidatorIsNumeric() != null &&
                    ValidatorField.VALIDATOR_INVALID.equals(textFieldField.getValidatorIsNumeric().isInvalid())) {
                    textFieldField.setInvalid(Field.FIELD_VALID);
                    validatorIsNumeric = textFieldField.getValidatorIsNumeric();
                    validatorIsNumeric.setInvalid(ValidatorField.VALIDATOR_VALID);
                }


            }
            else if (object instanceof PanelField) {
                PanelField panelField = (PanelField) object;
                if (panelField.getFormLayout() != null) {
                    resetAllValidator(panelField.getFormLayout().getFieldList());
                }
            }
        }
    }

    public void resetAllFormStatus(List fieldList) {
        engine.setFieldList(getWidgetForm());
        resetValidator(fieldList);
        resetAllValidator(fieldList);
        engine.setLayout(getLayout());
        engine.setWidget(getWidgetForm());
        engine.setFieldList(getWidgetForm());
        getWidgetForm().setInvalid(false);
    }

    public void setEmptyIndication() {
        setIndicationLabel("");
    }

    public FileLinkCheckboxField createFileLinkCheckbox(String name, String value,
                                                        String colspan, String rowspan, String align, String valign,
                                                        String require) {
        FileLinkCheckboxField fileLinkCheckboxField = new FileLinkCheckboxField();
        ValidatorFileLinkCheckboxField validatorFileLinkCheckboxField = new ValidatorFileLinkCheckboxField();

        fileLinkCheckboxField.setName(name);
        fileLinkCheckboxField.setRequired(false);
        if ("1".equals(require))
            fileLinkCheckboxField.setRequired(true);

        //fileLinkCheckboxField.setLinkName(name);
        fileLinkCheckboxField.setLinkText(Util.processFile(value));
        fileLinkCheckboxField.setLinkUrl(url + value);


        //fileLinkCheckboxField.setCheckBoxName(name);
        fileLinkCheckboxField.setCheckBoxText(Application.getInstance().getMessage("formWizard.label.dynamicEditFormDataField.fileUpload.remove", "Remove"));
        fileLinkCheckboxField.setCheckBoxValue(value);

        //fileLinkCheckboxField.setFileUploadName(name);
        fileLinkCheckboxField.setColspan(colspan);
        fileLinkCheckboxField.setRowspan(rowspan);
        fileLinkCheckboxField.setAlign(align);
        fileLinkCheckboxField.setValign(valign);

        validatorFileLinkCheckboxField.setInvalid(ValidatorField.VALIDATOR_VALID);
        validatorFileLinkCheckboxField.setName("fileLinkCheckbox");
        validatorFileLinkCheckboxField.setText(Application.getInstance().getMessage("formWizard.label.requiredField","Require Field"));
        fileLinkCheckboxField.setValidatorFileLinkCheckboxField(validatorFileLinkCheckboxField);


        return fileLinkCheckboxField;
    }

    public LinkField createLinkField(String name, String value) {
        LinkField linkField = new LinkField();
        linkField.setName(name + FormConstants.FIELD_LINK_SUFFIX + getId());
        linkField.setText(Util.processFile(value));
        linkField.setUrl(url + value);
        return linkField;
    }

    public void setPanelField(PanelField panel) {
        FormLayout formLayout = panel.getFormLayout();
        ButtonField buttonField = new ButtonField();
        buttonField.setName(CANCEL_FORM_ACTION);
        buttonField.setText(Application.getInstance().getMessage("formWizard.label.viewFormFields.cancel", "Cancel"));

        ButtonField printButtonField = new ButtonField();
        printButtonField.setName("print");
        printButtonField.setText(Application.getInstance().getMessage("formWizard.label.dynamicEditFormDataField.print", "Print"));

        if (panel.getButtonList() != null) {
            panel.getButtonList().add(printButtonField);
            panel.getButtonList().add(buttonField);
        }


        if (formLayout != null) {
            formLayout.setWidth("100%");
            parseLayout(formLayout.getFieldList());
        }

    }

    public void setListener(ListenerForm listener) {
        listener.setClassName(FormEditSubmissionEvent.class.getName());
    }

    public HiddenField addHiddenFormUidField() {
        HiddenField hiddenField = new HiddenField();
        hiddenField.setName("formUid");
        hiddenField.setValue(getId());
        return hiddenField;
    }


    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}



    
}
