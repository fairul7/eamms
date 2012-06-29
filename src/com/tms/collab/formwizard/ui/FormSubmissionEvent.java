package com.tms.collab.formwizard.ui;

import kacang.stdui.Form;
import kacang.stdui.FileUpload;
import kacang.ui.Forward;
import kacang.ui.Event;
import kacang.util.UuidGenerator;
import kacang.util.Log;
import kacang.Application;
import kacang.services.storage.StorageFile;


import java.util.*;
import java.io.IOException;
import java.io.InputStream;

import com.tms.collab.formwizard.engine.*;
import com.tms.collab.formwizard.model.*;
import com.tms.collab.formwizard.grid.G2Field;
import org.apache.commons.collections.SequencedHashMap;


public class FormSubmissionEvent extends Form {
    protected FormLayout layout;
    private String formId;

    public Forward onSubmit(Event event) {
        return super.onSubmit(event);
    }

    public Forward onValidate(Event event) {
        String buttonName = findButtonClicked(event);
        Forward forward = null;

        try {
            if (buttonName.endsWith("draft")) {
                draftForm(event);
                forward = new Forward("dataDrafted");
            }
            else if (buttonName.endsWith("submit")) {
                submitForm(event);
                forward = getSubmissionForward(event);
            }
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

    public void draftForm(Event event) throws FormDaoException,FormException {

        FormModule module = (FormModule) Application.getInstance().getModule(FormModule.class);
        module.draftForm(getFormWorkFlowDataObject(event),event, layout.getFieldList());

    }

    public FormWorkFlowDataObject getFormWorkFlowDataObject(Event event) {
        WidgetEngine engine = new WidgetEngine((Form)event.getWidget());
        layout = engine.retriveFieldList();

        Map values = new SequencedHashMap();



        FormWorkFlowDataObject fwfdo = new FormWorkFlowDataObject();

        fwfdo.setFormUid(UuidGenerator.getInstance().getUuid());
        fwfdo.setUserId(event.getWidgetManager().getUser().getId());
        fwfdo.setFileList(new ArrayList());
        fwfdo.setValues(values);
        fwfdo.setFormName(layout.getFormName());
        parseFieldList(layout.getFieldList(),values,"",fwfdo, event);


        fwfdo.setValues(values);

        return fwfdo;
    }

    public void getLabelMap(FormWorkFlowDataObject fwfdo) {


        FormModule module = (FormModule) Application.getInstance().getModule(FormModule.class);

        InputStream stream = null;


        try {
            stream = module.getFormXML(fwfdo.getFormId());
            StructureEngine engine = new StructureEngine();
            engine.setXml(stream);
            FormLayout layout =  engine.retriveStructure();

            parseLabelList(layout.getFieldList(),fwfdo.getValues(),"");




        }
        catch (FormDaoException e) {
            Log.getLog(getClass()).fatal(e.getMessage(),e);
        }
        finally {
            try {
                if (stream != null)
                    stream.close();
            }
            catch (IOException e) {
            }
        }
    }

    public Forward getSubmissionForward(Event event) throws FormDaoException {
        Forward forward;
        FormModule module = (FormModule) Application.getInstance().getModule(FormModule.class);


        Object nextFormId =   module.getFormProperty(formId,"formLink");
        forward = new Forward("submissionMessage");
        try {
            if (nextFormId != null && !nextFormId.toString().trim().equals("") &&
                FormModule.FORM_ACTIVE.equals(module.getForm(nextFormId.toString()).getIsActive()) &&
                module.isValidForm(formId,event.getWidgetManager().getUser().getId())
                ) {
                    DynamicViewFormField dynamicField = (DynamicViewFormField) event.getWidget().getParent();
                    dynamicField.setFormId(nextFormId.toString());
                    forward = new Forward("formLink");
            }
        }
        catch (FormException e) {
            Log.getLog(getClass()).error(e.getMessage(),e);
        }

        return forward;
    }

    public void parseFieldList(List fieldList,Map values,
                               String templateId, FormWorkFlowDataObject fwfdo, Event event) {
        Field field;
        StringBuffer buffer = new StringBuffer();
        String str;
        FormData formData;


        for (Iterator iterator = fieldList.iterator(); iterator.hasNext();) {
            field =  (Field) iterator.next();
            formData = new FormData();
            formData.setType(field);
            formData.setWidget(field.getWidget());

            if (field instanceof HiddenField) {
                if ( "formId".equals(field.getName()))
                    fwfdo.setFormId ((String)field.getValue());
                    formId = fwfdo.getFormId();
            }
            else if (field instanceof TextFieldField ||
                     field instanceof TextBoxField ||
                     field instanceof TableGridField) {

                formData.setValue(field.getValue());
                formData.setDisplayValue(formData.getValue());

                //Initializing blank numeric fields to 0
                if(field instanceof TextFieldField)
                {
                    if(((TextFieldField)field).getValidatorIsInteger() != null || ((TextFieldField)field).getValidatorIsNumeric() != null)
                    {
                        String value = initializeNumerics((String) field.getValue());
                        formData.setValue(value);
                        formData.setDisplayValue(value);
                    }
                }

                values.put( templateId + field.getName(),formData);
            }
            else if (field instanceof ButtonGroupField) {
                ButtonGroupField buttonGroupField = (ButtonGroupField) field;
                CheckBoxField checkBoxField;
                RadioField radioField;
                if (buttonGroupField.getCheckBoxList() != null) {
                    buffer = new StringBuffer();
                    for (Iterator iterator1 = buttonGroupField.getCheckBoxList().iterator(); iterator1.hasNext();) {
                        checkBoxField =  (CheckBoxField) iterator1.next();
                        if (checkBoxField.isChecked()) {
                            buffer.append(checkBoxField.getValue()).append(",");
                        }

                    }
                }
                else if (buttonGroupField.getRadioList() != null) {
                   buffer = new StringBuffer();
                   for (Iterator iterator1 = buttonGroupField.getRadioList().iterator(); iterator1.hasNext();) {
                        radioField =  (RadioField) iterator1.next();
                        if (radioField.isChecked()) {
                            buffer.append(radioField.getValue()).append(",");
                        }

                    }
                }
                str = buffer.toString();
                if (str.endsWith(","))
                    str = buffer.substring(0,buffer.length()-1);

                formData.setValue(str);
                formData.setDisplayValue(formData.getValue());
                values.put(templateId + field.getName(),formData);
            }
            else if (field instanceof SelectBoxField) {
                Iterator valuesIterator = ((SelectBoxField)field).getValues().keySet().iterator();
                if (valuesIterator.hasNext()) {
                    formData.setValue(valuesIterator.next());
                    formData.setDisplayValue(formData.getValue());
                    values.put(templateId + field.getName(),formData);

                }
            }
            else if (field instanceof DateFieldField) {
                formData.setValue(((DateFieldField) field).getDate());
                formData.setDisplayValue(formData.getValue());
                values.put(templateId + field.getName(),formData);
            }
            else if (field instanceof FileField) {
                try {
                    putFileUpload((FileField) field, event, fwfdo, templateId, formData,values);
                }
                catch (IOException e) {
                    Log.getLog(getClass()).fatal(e.getMessage(),e);
                }               
            }
            else if (field instanceof PanelField) {
                if (((PanelField) field).getFormLayout() != null) {
                    parseFieldList(((PanelField) field).getFormLayout().getFieldList(),
                                   values,
                                   field.getName(),
                                   fwfdo,
                                   event
                                   );
                }
            }
        }
    }

    private String initializeNumerics(String value)
    {
        return (value == null || "".equals(value)) ? "0" : value;
    }

    public void putFileUpload(FileField field, Event event, FormWorkFlowDataObject fwfdo, String templateId,
                              FormData formData, Map values) throws IOException {

        putFileUpload(field.getFileUpload(), field.getName(), event, fwfdo, templateId, formData, values);

    }

    public void putFileUpload(FileUpload fileUpload, String name, Event event, FormWorkFlowDataObject fwfdo, String templateId,
                              FormData formData, Map values) throws IOException {
        String value;

        StorageFile sf = fileUpload.getStorageFile(event.getRequest());
        if (sf != null) {
            value = FormConstants.ROOT_PATH + "/" + fwfdo.getFormId() + "/"
                    + fwfdo.getFormUid() + "/" + templateId + name + sf.getId();
            formData.setValue(value);
            formData.setDisplayValue(formData.getValue());
            values.put(templateId + name, formData);
        }
    }

    public void parseLabelList(List fieldList, Map values, String templateId) {
        Object object;
        LabelField labelField;
        PanelField panelField;
        TableGridField tableGridField;
        FormData formData;


        for (Iterator iterator = fieldList.iterator(); iterator.hasNext();) {
            object =  iterator.next();

            if (object instanceof LabelField) {
                labelField = (LabelField) object;
                if (labelField.getName().endsWith("lb")) {
                    formData = (FormData)values.get(templateId + labelField.getName().substring(0,labelField.getName().length()-2));
                    if (formData != null) {
                        formData.setLabel(labelField.getText());
                        if (formData.getType() instanceof DateFieldField) {
                            formData.setDisplayValue(Util.getDate((Date)formData.getDisplayValue()));
                        }
                        else if (formData.getType() instanceof FileField) {
                            formData.setDisplayValue(Util.processFile((String) formData.getDisplayValue()));
                        }
                    }
                }
            }
            else if (object instanceof PanelField) {
                panelField = (PanelField) object;
                if (panelField.getFormLayout() != null) {
                    parseLabelList(panelField.getFormLayout().getFieldList(),values,panelField.getName());
                }
            }
            else if (object instanceof TableGridField) {
               tableGridField =  (TableGridField) object;
               formData = (FormData)values.get(templateId + tableGridField.getName());
               formData.setLabel(tableGridField.getTitle());
               formData.setDisplayValue(Util.formatG2FieldData( (G2Field) formData.getWidget()));
            }


        }


    }

    public void submitForm(Event event) throws FormDaoException, FormException, FormDocumentException {


        FormModule module = (FormModule) Application.getInstance().getModule(FormModule.class);
        FormWorkFlowDataObject fwfdo = getFormWorkFlowDataObject(event);
        getLabelMap(fwfdo);
        module.saveForm(fwfdo,event, layout.getFieldList());
    }








}
