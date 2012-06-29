package com.tms.collab.formwizard.ui;


import com.tms.collab.formwizard.engine.*;

import java.util.*;

import kacang.Application;
import kacang.ui.Forward;
import kacang.ui.Event;


public class DynamicViewFormField extends DynamicField {
    private String formId;

    public Forward actionPerformed(Event event) {
        Forward forward = super.actionPerformed(event);
        String buttonClicked = findButtonClicked(event);

        if (buttonClicked.endsWith("draft")) {
            // if save draft, just call onValidate() to save it - ignore validation
            if (getWidgetForm().isInvalid()) {
                resetFormStatus(getLayout().getFieldList());
                if (!getWidgetForm().isInvalid())
                    forward = onValidate(event);
            }
        }


        setIndication();
        return forward;
    }

    public void setIndication() {
        if (getWidgetForm().isInvalid())
            setIndicationLabel(Application.getInstance().getMessage("formWizard.message.field.not.fill", "<font color=\"red\">Some fields are not filled or invalid</font>"));
        else
            setIndicationLabel("");
    }

    public void setPanelField(PanelField panel) {
        FormLayout formLayout = panel.getFormLayout();
        ButtonField buttonField = new ButtonField();
        buttonField.setName("draft");
        buttonField.setText(Application.getInstance().getMessage("formWizard.label.viewForm.draft", "Save As Draft"));
        if (panel.getButtonList() != null)
            panel.getButtonList().add(buttonField);

        if (formLayout != null) {
            formLayout.setWidth("100%");
            parseLayout(formLayout.getFieldList());
        }

    }

    public void resetValidator(List fieldList) {

        Object object;
        ValidatorNotEmptyField validatorNotEmpty;
        ValidatorIsIntegerField validatorIsInteger;
        ValidatorEmailField validatorEmail;
        ValidatorIsNumericField validatorIsNumeric;


        for (Iterator iterator = fieldList.iterator(); iterator.hasNext();) {
            object = iterator.next();

            if (object instanceof Field) {
                if ("1".equals(((Field) object).getRequire())) {
                    if (object instanceof TextFieldField) {
                        TextFieldField textFieldField = (TextFieldField) object;
                        if (textFieldField.getValidatorNotEmpty() != null) {
                            textFieldField.setInvalid(Field.FIELD_VALID);
                            validatorNotEmpty = textFieldField.getValidatorNotEmpty();
                            validatorNotEmpty.setInvalid(ValidatorField.VALIDATOR_VALID);

                            if (textFieldField.getValidatorIsInteger() != null &&
                                ValidatorField.VALIDATOR_INVALID.equals(textFieldField.getValidatorIsInteger().isInvalid())) {
                                textFieldField.setInvalid(Field.FIELD_INVALID);
                                validatorIsInteger = textFieldField.getValidatorIsInteger();
                                validatorIsInteger.setInvalid(ValidatorField.VALIDATOR_INVALID);
                            }


                            if (textFieldField.getValidatorEmail() != null &&
                                ValidatorField.VALIDATOR_INVALID.equals(textFieldField.getValidatorEmail().isInvalid())) {
                                textFieldField.setInvalid(Field.FIELD_INVALID);
                                validatorEmail = textFieldField.getValidatorEmail();
                                validatorEmail.setInvalid(ValidatorField.VALIDATOR_INVALID);
                            }


                            if (textFieldField.getValidatorIsNumeric() != null &&
                                ValidatorField.VALIDATOR_INVALID.equals(textFieldField.getValidatorIsNumeric().isInvalid())) {
                                textFieldField.setInvalid(Field.FIELD_INVALID);
                                validatorIsNumeric = textFieldField.getValidatorIsNumeric();
                                validatorIsNumeric.setInvalid(ValidatorField.VALIDATOR_INVALID);
                            }


                        }
                    }
                    else if (object instanceof TextBoxField) {
                        TextBoxField textBoxField = (TextBoxField) object;
                        if (textBoxField.getValidatorNotEmpty() != null) {
                            textBoxField.setInvalid(Field.FIELD_VALID);
                            validatorNotEmpty = textBoxField.getValidatorNotEmpty();
                            validatorNotEmpty.setInvalid(ValidatorField.VALIDATOR_VALID);
                        }
                    }
                    else if (object instanceof ButtonGroupField) {
                        ButtonGroupField buttonGroupField = (ButtonGroupField) object;
                        if (buttonGroupField.getValidatorNotEmpty() != null) {
                            buttonGroupField.setInvalid(Field.FIELD_VALID);
                            validatorNotEmpty = buttonGroupField.getValidatorNotEmpty();
                            validatorNotEmpty.setInvalid(ValidatorField.VALIDATOR_VALID);
                        }
                    }
                    else if (object instanceof SelectBoxField) {
                        SelectBoxField selectBoxField = (SelectBoxField) object;
                        if (selectBoxField.getValidatorNotEmpty() != null) {
                            selectBoxField.setInvalid(Field.FIELD_VALID);
                            validatorNotEmpty = selectBoxField.getValidatorNotEmpty();
                            validatorNotEmpty.setInvalid(ValidatorField.VALIDATOR_VALID);
                        }
                    }
                    else if (object instanceof DateFieldField) {
                        DateFieldField dateFieldField = (DateFieldField) object;
                        if (dateFieldField.getValidatorNotEmpty() != null) {
                            dateFieldField.setInvalid(Field.FIELD_VALID);
                            validatorNotEmpty = dateFieldField.getValidatorNotEmpty();
                            validatorNotEmpty.setInvalid(ValidatorField.VALIDATOR_VALID);
                        }
                    }
                    else if (object instanceof FileField) {
                        FileField fileField = (FileField) object;
                        if (fileField.getValidatorNotEmpty() != null) {
                            fileField.setInvalid(Field.FIELD_VALID);
                            validatorNotEmpty = fileField.getValidatorNotEmpty();
                            validatorNotEmpty.setInvalid(ValidatorField.VALIDATOR_VALID);
                        }
                    }
                }

                if (object instanceof PanelField) {
                    PanelField panelField = (PanelField) object;
                    if (panelField.getFormLayout() != null) {
                        resetValidator(panelField.getFormLayout().getFieldList());
                    }
                }

            }

        }

    }

    public void resetFormStatus(List fieldList) {
        engine.setFieldList(getWidgetForm());
        resetValidator(fieldList);
        engine.setLayout(getLayout());
        engine.setWidget(getWidgetForm());
        engine.setFieldList(getWidgetForm());
        setWidgetFormValidationStatus(fieldList);
    }

    public void setWidgetFormValidationStatus(List fieldList) {
        Object object;
        for (Iterator iterator = fieldList.iterator(); iterator.hasNext();) {
            object = iterator.next();

            if (object instanceof PanelField) {
                if (((PanelField) object).getFormLayout() != null)
                    setWidgetFormValidationStatus(((PanelField) object).getFormLayout().getFieldList());
            }
            else if (object instanceof Field) {
                if (Field.FIELD_INVALID.equals(((Field) object).isInvalid())) {
                    getWidgetForm().setInvalid(true);
                    break;
                }
                else {
                    getWidgetForm().setInvalid(false);
                }
            }
        }

    }

    public void parseLayout(List fieldList) {
        Object object;
        Map removedMap = new HashMap();
        Set removedMapSet;


        for (Iterator iterator = fieldList.iterator(); iterator.hasNext();) {
            object = iterator.next();

            if (object instanceof LabelField) {
                setLabelField((LabelField) object);
                if (removeSubmitLbl((LabelField) object)) {
                    removedMap.put(object, null);
                }
            }
            else if (object instanceof PanelField) {
                setPanelField((PanelField) object);
            }
            else if (object instanceof ListenerForm) {
                setListener((ListenerForm) object);
            }
        }

        removedMapSet = removedMap.keySet();
        for (Iterator iterator = removedMapSet.iterator(); iterator.hasNext();) {
            object = iterator.next();
            fieldList.remove(object);
        }

        fieldList.add(0, addIndicationLabel(fieldList));
        fieldList.add(0, addHiddenField());
    }

    public boolean removeSubmitLbl(LabelField label) {
        boolean removed = false;
        if ("submitLbl".equals(label.getName()))
            removed = true;
        return removed;
    }

    public void setLabelField(LabelField label) {
        if (label.getType() == null) {
            if ("1".equals(label.getRequire()))
                label.setText("<b>" + label.getText() + "</b> *");
            else if ("1".equals(label.getHidden())||"3".equals(label.getHidden()))
                label.setText("<b><i>" + label.getText() + "</i></b>");
            else
                label.setText("<b>" + label.getText() + "</b>");
        }
    }

    public void setListener(ListenerForm listener) {
        listener.setClassName(FormSubmissionEvent.class.getName());
    }

    public LabelField addIndicationLabel(List fieldList) {
        LabelField labelField = new LabelField();
        labelField.setName("indication");
        labelField.setColspan(getLayout().getColumns());
        return labelField;
    }

    public void setIndicationLabel(String text) {
        List fieldlist = getLayout().getFieldList();
        Object object;

        for (Iterator iterator = fieldlist.iterator(); iterator.hasNext();) {
            object = iterator.next();

            if (object instanceof LabelField && "indication".equals(((LabelField) object).getName())) {
                ((LabelField) object).setText(text);
                engine.setWidget((Field) object, getWidgetForm().getChild("indication"));
            }

        }
    }

    public HiddenField addHiddenField() {
        HiddenField hiddenField = new HiddenField();
        hiddenField.setName("formId");
        hiddenField.setValue(getFormId());
        return hiddenField;
    }

    public HiddenField addHiddenFormUidField() {
        return null;
    }


    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }
}


