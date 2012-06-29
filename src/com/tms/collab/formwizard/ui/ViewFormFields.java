package com.tms.collab.formwizard.ui;

import kacang.stdui.Form;
import kacang.stdui.ComboSelectBox;
import kacang.stdui.Button;
import kacang.stdui.SortableComboSelectBox;
import kacang.util.Log;
import kacang.ui.Event;
import kacang.ui.Forward;
import com.tms.collab.formwizard.model.*;
import com.tms.collab.formwizard.xmlwidget.TemplateElement;
import com.tms.collab.formwizard.xmlwidget.FormElement;
import kacang.Application;

import java.util.Iterator;
import java.util.Map;
import java.util.List;


import org.apache.commons.collections.SequencedHashMap;

import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.xpath.XPath;



public class ViewFormFields extends Form{
    private SortableComboSelectBox fields;
    private Button submit;
    private Button cancel;
    private String formId;
    private Map selectedFields;
    private String formName;
    
    final public static String FORM_NAME ="formName";

    public void onRequest(Event event) {
        initForm();
    }

    public ViewFormFields() {
    }

    public ViewFormFields(String s) {
        super(s);
    }

    public void initForm(){
		fields = new SortableComboSelectBox("fields");
        submit = new Button ("submit");
        submit.setText(Application.getInstance().getMessage("formWizard.label.viewFormFields.submit","Submit"));
        cancel = new Button("cancel");
        cancel.setText(Application.getInstance().getMessage("formWizard.label.viewFormFields.cancel","Cancel"));
        addChild(fields);
        fields.init();
        addChild(submit);
        addChild(cancel);
        setMethod("post");
        Map optionMap = null;
        FormElement form = null;
        List templatList = null;
        Element element = null;
        FormTemplate formTemplate = null;

        try {
            form = Util.getFormElement(getFormId());
            formName = form.getAttributeValue("name").toString();
            optionMap = setOptionMap(form , "");

            templatList = XPath.selectNodes(form,"/form/" + TemplateElement.ELEMENT_NAME);
            for (Iterator iterator = templatList.iterator(); iterator.hasNext();) {
                element = (Element) iterator.next();
                formTemplate = new FormTemplate();
                formTemplate.setFormTemplateId(element.getAttributeValue("templateId"));
                form = Util.getTemplateElement(formTemplate);
                optionMap.putAll(setOptionMap(form,element.getAttributeValue("name")));
            }

        }
        catch (FormDocumentException e) {
            Log.getLog(getClass()).error(e.getMessage(),e);
        }
        catch (JDOMException e) {
            Log.getLog(getClass()).error("Error selecting formwizardtemplate node",e);
        }





            fields.setLeftValues(optionMap);
            fields.setTemplate("formwizard/formSortableComboSelectBox");



    }

    public Map setOptionMap(FormElement element, String templateNodeName) throws FormDocumentException {
        Map optionMap = new SequencedHashMap();
        List nodeList = null;
        try {
            nodeList = XPath.selectNodes(element,"/form/label[@name != 'submitLbl']");
            for (Iterator iterator = nodeList.iterator(); iterator.hasNext();) {
                Element lblElem = (Element) iterator.next();
                String nameAttrStr = lblElem.getAttributeValue("name");
                if (nameAttrStr == null || nameAttrStr.endsWith(FormConstants.FIELD_TEXT_BLOCK_SUFFIX)) {
                    continue;
                }
                if (nameAttrStr.endsWith("lb")) {
                    String dbColumn = nameAttrStr.substring(0, nameAttrStr.lastIndexOf("lb"));
                    dbColumn = templateNodeName + dbColumn;
                    String lbText ="";
                    if (lblElem.getAttributeValue("text") != null) {
                        lbText = lblElem.getAttributeValue("text");
                    }
                    optionMap.put(dbColumn,lbText);

                }

            }

        }
        catch (JDOMException e) {
            throw new FormDocumentException("Error selecting the label nodes");
        }

        return optionMap;

    }

    public Forward onValidate(Event event) {
        String buttonName = findButtonClicked(event);
        Forward fwd = null;
        if (buttonName != null && buttonName.equals(cancel.getAbsoluteName())) {
            init();            
            fwd = new Forward();
            fwd.setName(Form.CANCEL_FORM_ACTION);
            return fwd;
        }
        Map selected = fields.getRightValues();
        if(selected.keySet().isEmpty()){
            selected = fields.getLeftValues();         
        }

        setSelectedFields(selected);

		return new Forward("done");		
    }


    public Map getSelectedFields() {
        return selectedFields;
    }

    public void setSelectedFields(Map selectedFields) {
        this.selectedFields = selectedFields;
    }

    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }

    public ComboSelectBox getFields() {
        return fields;
    }

    public Button getSubmit() {
        return submit;
    }

    public Button getCancel() {
        return cancel;
    }

    public String getTemplate() {
        return "formwizard/viewFormFields";
    }

	public String getFormId() {
		return formId;
	}


	public void setFormId(String formId) {
		this.formId = formId;
	}

}
