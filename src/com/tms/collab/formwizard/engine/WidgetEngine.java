package com.tms.collab.formwizard.engine;

import kacang.stdui.*;
import kacang.stdui.event.FormEventListener;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.stdui.validator.ValidatorEmail;
import kacang.stdui.validator.ValidatorIsNumeric;
import kacang.stdui.validator.Validator;
import kacang.ui.Widget;
import kacang.util.Log;

import java.util.*;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.io.Serializable;

import com.tms.collab.formwizard.ui.validator.ValidatorIsInteger;
import com.tms.collab.formwizard.grid.G2Field;
import com.tms.collab.formwizard.widget.FileLinkCheckbox;
import com.tms.collab.formwizard.widget.validator.ValidatorFileLinkCheckbox;
import com.tms.util.FormatUtil;

public class WidgetEngine implements Serializable {
    private FormLayout layout;
    private Form form;



    public WidgetEngine() {
    }

    public WidgetEngine(Form form) {
        this.form = form;
    }

    public void setWidget(Field field, Widget widget) {

        if (field instanceof LabelField) {
             setLabelField((LabelField) field, (Label) widget);
        }
        else if (field instanceof TextFieldField) {
            setTextField((TextFieldField) field, (TextField)  widget);
        }
        else if (field instanceof TextBoxField) {
            setTextBoxField((TextBoxField) field, (TextBox)  widget);
        }
        else if (field instanceof ButtonGroupField) {
             setButtonGroupField((ButtonGroupField) field, (ButtonGroup)  widget);
        }
        else if (field instanceof SelectBoxField) {
            setSelectBoxField((SelectBoxField) field, (SelectBox)  widget);
        }
        else if (field instanceof DateFieldField) {
            setDateField((DateFieldField) field, (DateField)  widget);
        }
        else if (field instanceof FileField) {
            setFileField((FileField) field, (FileUpload) widget);
        }
        else if (field instanceof PanelField) {
           setPanelField((PanelField) field, (Panel)  widget);
        }
        else if (field instanceof FileLinkCheckboxField) {
            setFileLinkCheckboxField((FileLinkCheckboxField) field, (FileLinkCheckbox) widget);
        }


    }

    public void setWidget(Form form) {
        Object object;

        for (Iterator iterator = layout.getFieldList().iterator(); iterator.hasNext();) {
            object =  iterator.next();

            if (object instanceof Field)
                setWidget((Field) object, (Widget) form.getChildMap().get( ((Field) object).getName()));

        }

    }

    public void setFieldList(Form form, List fieldList) {
      Object object;

      for (Iterator iterator = fieldList.iterator(); iterator.hasNext();) {
          object =  iterator.next();


          if (object instanceof TextFieldField) {
            setTextFieldList((TextFieldField) object, (TextField) form.getChildMap().get( ((TextFieldField) object).getName()));
          }
          else if (object instanceof TextBoxField) {
            setTextBoxList((TextBoxField) object, (TextBox) form.getChildMap().get( ((TextBoxField) object).getName()));
          }
          else if (object instanceof ButtonGroupField) {
            setButtonGroupList((ButtonGroupField) object, (ButtonGroup) form.getChildMap().get( ((ButtonGroupField) object).getName()));
          }
          else if (object instanceof SelectBoxField) {
            setSelectBoxList((SelectBoxField) object, (SelectBox) form.getChildMap().get( ((SelectBoxField) object).getName()));
          }
          else if (object instanceof DateFieldField) {
            setDateFieldList((DateFieldField) object, (DateField) form.getChildMap().get( ((DateFieldField) object).getName()));
          }
          else if (object instanceof FileField) {
            setFileFieldList((FileField) object, (FileUpload) form.getChildMap().get( ((FileField) object).getName()));
          }
          else if (object instanceof TableGridField) {
            setTableGridList( (TableGridField) object, (G2Field) form.getChildMap().get( ((TableGridField) object).getName()));
          }
          else if (object instanceof PanelField) {
            if (((PanelField) object).getFormLayout() != null)
                setFieldList((Form) form.getChild(  ((PanelField)object).getName()).getChild(  ((PanelField)object).getFormLayout().getFormName()),
                         ((PanelField) object).getFormLayout().getFieldList());
          }
          else if (object instanceof FileLinkCheckboxField) {
            setFileLinkCheckboxFieldList((FileLinkCheckboxField) object, (FileLinkCheckbox) form.getChildMap().get( ((FileLinkCheckboxField) object).getName()));
          }

      }
    }

    public void setFieldList(Form form) {
        setFieldList(form,layout.getFieldList());
    }

    public Form retriveWidget() {
        Object object;
        createForm();
        for (Iterator iterator = layout.getFieldList().iterator(); iterator.hasNext();) {
            object =  iterator.next();

            if (object instanceof LabelField) {
                createLabel((LabelField) object);
            }
            else if (object instanceof TextFieldField) {
                createTextField((TextFieldField) object);
            }
            else if (object instanceof TextBoxField) {
                createTextBoxField((TextBoxField) object);
            }
            else if (object instanceof ButtonGroupField) {
                createButtonGroupField((ButtonGroupField) object);
            }
            else if (object instanceof SelectBoxField) {
                createSelectBoxField((SelectBoxField) object);
            }
            else if (object instanceof DateFieldField) {
                createDateField((DateFieldField) object);
            }
            else if (object instanceof FileField) {
                createFileField((FileField) object);
            }
            else if (object instanceof TableGridField) {
                createTableGridField((TableGridField) object);
            }
            else if (object instanceof PanelField) {
                createPanelField((PanelField) object);
            }
            else if (object instanceof ListenerForm) {
                createListenerForm((ListenerForm) object);
            }
            else if (object instanceof HiddenField) {
                createHiddenField((HiddenField) object);
            }
            else if (object instanceof LinkField) {
                createLinkField((LinkField) object);
            }
            else if (object instanceof FileLinkCheckboxField) {
                createFileLinkCheckBoxField((FileLinkCheckboxField) object);
            }

        }

        return form;
    }

    public FormLayout retriveFieldList() {
        Object object;
        layout = new FormLayout();

        layout.setFormName(form.getName());
        layout.setFieldList(new ArrayList());

        for (Iterator iterator = form.getChildren().iterator(); iterator.hasNext();) {
            object =  iterator.next();

            if (object instanceof Hidden) {
                 createHiddenFieldList(layout.getFieldList(),(Hidden) object);
            }
            else if (object instanceof TextBox) {
                  createTextBoxList(layout.getFieldList(),(TextBox) object);
            }
            else if (object instanceof TextField) {
                createTextFieldList(layout.getFieldList(),(TextField) object);
            }
            else if (object instanceof ButtonGroup) {
               createButtonGroupList(layout.getFieldList(),(ButtonGroup) object);
            }
            else if (object instanceof SelectBox) {
               createSelectBoxList(layout.getFieldList(),(SelectBox) object);
            }
            else if (object instanceof DateField) {
               createDateFieldList(layout.getFieldList(),(DateField) object);
            }
            else if (object instanceof FileUpload) {
               createFileFieldList(layout.getFieldList(),(FileUpload) object);
            }
            else if (object instanceof G2Field) {
               createTableGridList(layout.getFieldList(),(G2Field) object);
            }
            else if (object instanceof Panel) {
              createPanelList(layout.getFieldList(),(Panel) object);
            }
            else if (object instanceof Label) {
              createLabelList(layout.getFieldList(),(Label) object);
            }
            else if (object instanceof FileLinkCheckbox) {
              createFileLinkCheckBoxList(layout.getFieldList(),(FileLinkCheckbox) object);
            }

        }
        return layout;
    }

    public void createForm() {
        form = new Form(layout.getFormName());
        form.setColumns(Integer.parseInt(layout.getColumns()));
    }

    public void createLabel(LabelField labelField) {
        Label label = new Label(labelField.getName(),labelField.getText());
        if ("1".equals(labelField.getHidden()))
            label.setHidden(true);
        else
            label.setHidden(false);

        if (labelField.getColspan() != null)
            label.setColspan(Integer.parseInt(labelField.getColspan()));

        if (labelField.getRowspan() != null)
            label.setRowspan(Integer.parseInt(labelField.getRowspan()));

        if (labelField.getAlign() != null)
            label.setAlign(labelField.getAlign());

        if (labelField.getValign() != null)
            label.setValign(labelField.getValign());

        if (labelField.getEscapeXml() != null)
            if ("false".equals(labelField.getEscapeXml()))
                label.setEscapeXml(false);
            else
                label.setEscapeXml(true);

        form.addChild(label);
    }

    public void setLabelField(LabelField labelField, Label label) {
        if ("1".equals(labelField.getHidden()))
            label.setHidden(true);
        else
            label.setHidden(false);

        if (labelField.getText() != null)
            label.setText(labelField.getText());


        if (labelField.getColspan() != null)
            label.setColspan(Integer.parseInt(labelField.getColspan()));

        if (labelField.getRowspan() != null)
            label.setRowspan(Integer.parseInt(labelField.getRowspan()));

        if (labelField.getAlign() != null)
            label.setAlign(labelField.getAlign());

        if (labelField.getValign() != null)
            label.setValign(labelField.getValign());

        if (labelField.getEscapeXml() != null)
            if ("false".equals(labelField.getEscapeXml()))
                label.setEscapeXml(false);
            else
                label.setEscapeXml(true);

    }

    protected void createLabelList(List fieldList, Label label) {
        LabelField labelField = new LabelField();
        labelField.setName(label.getName());
        labelField.setText(label.getText());


        fieldList.add(labelField);
    }

    public void createTextField(TextFieldField textFieldField) {
        TextField textField = new TextField(textFieldField.getName());

        textField.setValue(textFieldField.getValue());
        if ("1".equals(textFieldField.getHidden()))
           textField.setHidden(true);
       else
           textField.setHidden(false);

       if (textFieldField.getColspan() != null)
           textField.setColspan(Integer.parseInt(textFieldField.getColspan()));

       if (textFieldField.getRowspan() != null)
           textField.setRowspan(Integer.parseInt(textFieldField.getRowspan()));

       if (textFieldField.getAlign() != null)
           textField.setAlign(textFieldField.getAlign());

       if (textFieldField.getValign() != null)
           textField.setValign(textFieldField.getValign());

       if (textFieldField.getSize() != null)
           textField.setSize(textFieldField.getSize());

       if (textFieldField.getMaxLength() != null)
           textField.setMaxlength(textFieldField.getMaxLength());

       if (Field.FIELD_INVALID.equals(textFieldField.isInvalid()))
            textField.setInvalid(true);
       else if (Field.FIELD_VALID.equals(textFieldField.isInvalid()))
            textField.setInvalid(false);


       if ("1".equals(textFieldField.getRequire())) {
           createValidatorNotEmpty(textFieldField.getValidatorNotEmpty(), textField);
       }

       if (textFieldField.getValidatorIsInteger() != null)
          createValidatorIsInteger(textFieldField.getValidatorIsInteger(), textField);

       if (textFieldField.getValidatorIsNumeric() != null)
          createValidatorIsNumeric(textFieldField.getValidatorIsNumeric(), textField);

       if (textFieldField.getValidatorEmail() != null)
           createValidatorEmail(textFieldField.getValidatorEmail(), textField);

        form.addChild(textField);
    }

    public void setTextField(TextFieldField textFieldField, TextField textField) {

        if ("1".equals(textFieldField.getHidden()))
            textField.setHidden(true);
        else
            textField.setHidden(false);

        if (textFieldField.getColspan() != null)
            textField.setColspan(Integer.parseInt(textFieldField.getColspan()));

        if (textFieldField.getRowspan() != null)
            textField.setRowspan(Integer.parseInt(textFieldField.getRowspan()));

        if (textFieldField.getAlign() != null)
            textField.setAlign(textFieldField.getAlign());

        if (textFieldField.getValign() != null)
            textField.setValign(textFieldField.getValign());

        if (textFieldField.getSize() != null)
            textField.setSize(textFieldField.getSize());

        if (textFieldField.getMaxLength() != null)
            textField.setMaxlength(textFieldField.getMaxLength());

        if ("1".equals(textFieldField.getRequire()))
            setValidatorNotEmpty(textFieldField.getValidatorNotEmpty(), textField);


        if (textFieldField.getValidatorIsInteger() != null)
           setValidatorIsInteger(textFieldField.getValidatorIsInteger(), textField);

         if (textFieldField.getValidatorIsNumeric() != null)
           setValidatorIsNumeric(textFieldField.getValidatorIsNumeric(), textField);

        if (textFieldField.getValidatorEmail() != null)
            setValidatorEmail(textFieldField.getValidatorEmail(), textField);

        if (Field.FIELD_INVALID.equals(textFieldField.isInvalid()))
            textField.setInvalid(true);
        else if (Field.FIELD_VALID.equals(textFieldField.isInvalid()))
            textField.setInvalid(false);


    }

    protected void createTextFieldList(List fieldList, TextField textField) {
        TextFieldField textFieldField = new TextFieldField();
        textFieldField.setName(textField.getName());
        textFieldField.setValue(textField.getValue());
        //Parsing child nodes to detect validators
        for (Iterator i = textField.getChildren().iterator(); i.hasNext();)
        {
            Object object = i.next();
            if(object instanceof ValidatorIsInteger)
            {
                ValidatorIsIntegerField field = new ValidatorIsIntegerField();
                field.setName(((ValidatorIsInteger)object).getName());
                field.setText(((ValidatorIsInteger)object).getText());
                field.setOptional(((ValidatorIsInteger)object).isOptional() ? "1" : "0");
                textFieldField.setValidatorIsInteger(field);
            }
            else if(object instanceof ValidatorIsNumeric)
            {
                ValidatorIsNumericField field = new ValidatorIsNumericField();
                field.setName(((ValidatorIsNumeric)object).getName());
                field.setText(((ValidatorIsNumeric)object).getText());
                field.setOptional(((ValidatorIsNumeric)object).isOptional() ? "1" : "0");
                textFieldField.setValidatorIsNumeric(field);
            }
        }
        fieldList.add(textFieldField);
    }

    public void setTextFieldList(TextFieldField textFieldField, TextField textField) {

        if (textField.isInvalid())
            textFieldField.setInvalid(ValidatorField.VALIDATOR_INVALID);
        else
            textFieldField.setInvalid(ValidatorField.VALIDATOR_VALID);

        Collection children = textField.getChildren();
        Object object;

        for (Iterator iterator = children.iterator(); iterator.hasNext();) {
            object =  iterator.next();
            if (object instanceof ValidatorNotEmpty) {
                if (((ValidatorNotEmpty) object).isInvalid())
                    textFieldField.getValidatorNotEmpty().setInvalid(ValidatorField.VALIDATOR_INVALID);
                else
                    textFieldField.getValidatorNotEmpty().setInvalid(ValidatorField.VALIDATOR_VALID);
            }
            else if (object instanceof ValidatorEmail) {
                if (((ValidatorEmail) object).isInvalid())
                    textFieldField.getValidatorEmail().setInvalid(ValidatorField.VALIDATOR_INVALID);
                else
                    textFieldField.getValidatorEmail().setInvalid(ValidatorField.VALIDATOR_VALID);
            }
            else if (object instanceof ValidatorIsInteger) {
                if (((ValidatorIsInteger) object).isInvalid())
                    textFieldField.getValidatorIsInteger().setInvalid(ValidatorField.VALIDATOR_INVALID);
                else
                    textFieldField.getValidatorIsInteger().setInvalid(ValidatorField.VALIDATOR_VALID);
            }
            else if (object instanceof ValidatorIsNumeric) {
                if (((ValidatorIsNumeric) object).isInvalid())
                    textFieldField.getValidatorIsNumeric().setInvalid(ValidatorField.VALIDATOR_INVALID);
                else
                    textFieldField.getValidatorIsNumeric().setInvalid(ValidatorField.VALIDATOR_VALID);
            }

        }

    }

    public void createTextBoxField(TextBoxField textBoxField) {
        TextBox textBox = new TextBox(textBoxField.getName(),(String)textBoxField.getValue());

        if ("1".equals(textBoxField.getHidden()))
            textBox.setHidden(true);
        else
            textBox.setHidden(false);

        if (textBoxField.getColspan() != null)
            textBox.setColspan(Integer.parseInt(textBoxField.getColspan()));

        if (textBoxField.getRowspan() != null)
            textBox.setRowspan(Integer.parseInt(textBoxField.getRowspan()));

        if (textBoxField.getAlign() != null)
            textBox.setAlign(textBoxField.getAlign());

        if (textBoxField.getValign() != null)
            textBox.setValign(textBoxField.getValign());

        if (textBoxField.getRows() != null)
            textBox.setRows(textBoxField.getRows());

        if (textBoxField.getCols() != null)
            textBox.setCols(textBoxField.getCols());

        if ("1".equals(textBoxField.getRequire())) {
            createValidatorNotEmpty(textBoxField.getValidatorNotEmpty(), textBox);
        }



        form.addChild(textBox);
    }

    public void setTextBoxField(TextBoxField textBoxField, TextBox textBox) {


        if ("1".equals(textBoxField.getHidden()))
            textBox.setHidden(true);
        else
            textBox.setHidden(false);

        if (textBoxField.getColspan() != null)
            textBox.setColspan(Integer.parseInt(textBoxField.getColspan()));

        if (textBoxField.getRowspan() != null)
            textBox.setRowspan(Integer.parseInt(textBoxField.getRowspan()));

        if (textBoxField.getAlign() != null)
            textBox.setAlign(textBoxField.getAlign());

        if (textBoxField.getValign() != null)
            textBox.setValign(textBoxField.getValign());



        if ("1".equals(textBoxField.getRequire())) {
            setValidatorNotEmpty(textBoxField.getValidatorNotEmpty(), textBox);
        }


        if (Field.FIELD_INVALID.equals(textBoxField.isInvalid()))
            textBox.setInvalid(true);
        else if (Field.FIELD_VALID.equals(textBoxField.isInvalid()))
            textBox.setInvalid(false);

    }

    public void setTextBoxList(TextBoxField textBoxField, TextBox textBox) {

         if (textBox.isInvalid())
            textBoxField.setInvalid(ValidatorField.VALIDATOR_INVALID);
        else
            textBoxField.setInvalid(ValidatorField.VALIDATOR_VALID);

    }

    protected void createTextBoxList(List fieldList, TextBox textBox) {
        TextBoxField textBoxField = new TextBoxField();
        textBoxField.setName(textBox.getName());
        textBoxField.setValue(textBox.getValue());

        fieldList.add(textBoxField);
    }

    public void createButtonGroupField(ButtonGroupField buttonGroupField) {
        ButtonGroup buttonGroup = new ButtonGroup(buttonGroupField.getName());

        if ("1".equals(buttonGroupField.getHidden()))
            buttonGroup.setHidden(true);
        else
            buttonGroup.setHidden(false);

        if (buttonGroupField.getColspan() != null)
            buttonGroup.setColspan(Integer.parseInt(buttonGroupField.getColspan()));

        if (buttonGroupField.getRowspan() != null)
            buttonGroup.setRowspan(Integer.parseInt(buttonGroupField.getRowspan()));

        if (buttonGroupField.getAlign() != null)
            buttonGroup.setAlign(buttonGroupField.getAlign());

        if (buttonGroupField.getValign() != null)
            buttonGroup.setValign(buttonGroupField.getValign());

        if (buttonGroupField.getType() != null)
            buttonGroup.setType(buttonGroupField.getType());

        if (buttonGroupField.getCheckBoxList() != null && buttonGroupField.getCheckBoxList().size() > 0)
            createCheckBoxField(buttonGroupField.getCheckBoxList(),buttonGroup);

        if (buttonGroupField.getRadioList() != null && buttonGroupField.getRadioList().size() > 0)
            createRadioButtonField(buttonGroupField.getRadioList(),buttonGroup);

        if ("1".equals(buttonGroupField.getRequire())) {
            createValidatorNotEmpty(buttonGroupField.getValidatorNotEmpty(), buttonGroup);
        }

        form.addChild(buttonGroup);
    }

    public void setButtonGroupField(ButtonGroupField buttonGroupField, ButtonGroup buttonGroup) {


        if ("1".equals(buttonGroupField.getHidden()))
            buttonGroup.setHidden(true);
        else
            buttonGroup.setHidden(false);

        if (buttonGroupField.getColspan() != null)
            buttonGroup.setColspan(Integer.parseInt(buttonGroupField.getColspan()));

        if (buttonGroupField.getRowspan() != null)
            buttonGroup.setRowspan(Integer.parseInt(buttonGroupField.getRowspan()));

        if (buttonGroupField.getAlign() != null)
            buttonGroup.setAlign(buttonGroupField.getAlign());

        if (buttonGroupField.getValign() != null)
            buttonGroup.setValign(buttonGroupField.getValign());

        if (buttonGroupField.getType() != null)
            buttonGroup.setType(buttonGroupField.getType());


        if (buttonGroupField.getCheckBoxList() != null && buttonGroupField.getCheckBoxList().size() > 0)
            createCheckBoxField(buttonGroupField.getCheckBoxList(),buttonGroup);

        if (buttonGroupField.getRadioList() != null && buttonGroupField.getRadioList().size() > 0)
            createRadioButtonField(buttonGroupField.getRadioList(),buttonGroup);

        if ("1".equals(buttonGroupField.getRequire())) {
            setValidatorNotEmpty(buttonGroupField.getValidatorNotEmpty(), buttonGroup);
        }

        if (Field.FIELD_INVALID.equals(buttonGroupField.isInvalid()))
            buttonGroup.setInvalid(true);
        else if (Field.FIELD_VALID.equals(buttonGroupField.isInvalid()))
            buttonGroup.setInvalid(false);


    }

    public void setButtonGroupList(ButtonGroupField buttonGroupField, ButtonGroup buttonGroup) {
        Object object;
        int cnt = 0;
        CheckBoxField checkBoxField;
        RadioField radioField;

        if (buttonGroup.isInvalid())
           buttonGroupField.setInvalid(ValidatorField.VALIDATOR_INVALID);
       else
           buttonGroupField.setInvalid(ValidatorField.VALIDATOR_VALID);


        Collection collection = buttonGroup.getButtons();

        for (Iterator iterator = collection.iterator(); iterator.hasNext();) {
            object =  iterator.next();
            if (object instanceof Radio) {
                radioField = (RadioField) buttonGroupField.getRadioList().get(cnt);
                radioField.setChecked( ((Radio)object).isChecked());
                cnt++;
            }
            else if (object instanceof CheckBox) {
                checkBoxField = (CheckBoxField) buttonGroupField.getCheckBoxList().get(cnt);
                checkBoxField.setChecked( ((CheckBox)object).isChecked());
                cnt++;
            }
        }

    }

    protected void createButtonGroupList(List fieldList, ButtonGroup buttonGroup) {
        ButtonGroupField buttonGroupField = new ButtonGroupField();
        buttonGroupField.setName(buttonGroup.getName());


        List buttonList = getButtonGroupButtons(buttonGroup.getChildren());
        if (buttonList != null && buttonList.size() > 0) {
            Object object = buttonList.get(0);
            if (object instanceof RadioField)
                buttonGroupField.setRadioList(buttonList);
            else if (object instanceof CheckBoxField)
                buttonGroupField.setCheckBoxList(buttonList);
        }


        fieldList.add(buttonGroupField);
    }

    protected List getButtonGroupButtons(Collection collection) {
        Object object;
        CheckBox cb;
        Radio rd;
        List buttonList = new ArrayList();
        for (Iterator iterator = collection.iterator(); iterator.hasNext();) {
            object =  iterator.next();

            if (object instanceof Radio) {
                rd = (Radio) object;
                RadioField radio = new RadioField();
                radio.setName(rd.getName());
                radio.setValue((String) rd.getValue());
                radio.setChecked(rd.isChecked());
                buttonList.add(radio);
            }
            else if (object instanceof CheckBox) {
                cb = (CheckBox) object;
                CheckBoxField checkbox = new CheckBoxField();
                checkbox.setName(cb.getName());
                checkbox.setValue((String) cb.getValue());
                checkbox.setChecked(cb.isChecked());
                buttonList.add(checkbox);
            }


        }

        return buttonList;
    }

    public void createSelectBoxField(SelectBoxField selectBoxField) {
        SelectBox selectBox = new SelectBox(selectBoxField.getName());

        if ("1".equals(selectBoxField.getHidden()))
            selectBox.setHidden(true);
        else
            selectBox.setHidden(false);

        if (selectBoxField.getColspan() != null)
            selectBox.setColspan(Integer.parseInt(selectBoxField.getColspan()));

        if (selectBoxField.getRowspan() != null)
            selectBox.setRowspan(Integer.parseInt(selectBoxField.getRowspan()));

        if (selectBoxField.getAlign() != null)
            selectBox.setAlign(selectBoxField.getAlign());

        if (selectBoxField.getValign() != null)
            selectBox.setValign(selectBoxField.getValign());

        if (selectBoxField.getOptions() != null)
            selectBox.setOptions(selectBoxField.getOptions());

        if (selectBoxField.getValue() != null)
            selectBox.setSelectedOption((String) selectBoxField.getValue());



        if ("1".equals(selectBoxField.getRequire())) {
            createValidatorNotEmpty(selectBoxField.getValidatorNotEmpty(), selectBox);
        }

        form.addChild(selectBox);
    }

    public void setSelectBoxField(SelectBoxField selectBoxField, SelectBox selectBox) {


        if ("1".equals(selectBoxField.getHidden()))
            selectBox.setHidden(true);
        else
            selectBox.setHidden(false);

        if (selectBoxField.getColspan() != null)
            selectBox.setColspan(Integer.parseInt(selectBoxField.getColspan()));

        if (selectBoxField.getRowspan() != null)
            selectBox.setRowspan(Integer.parseInt(selectBoxField.getRowspan()));

        if (selectBoxField.getAlign() != null)
            selectBox.setAlign(selectBoxField.getAlign());

        if (selectBoxField.getValign() != null)
            selectBox.setValign(selectBoxField.getValign());

        if (selectBoxField.getOptions() != null)
            selectBox.setOptions(selectBoxField.getOptions());


        if ("1".equals(selectBoxField.getRequire())) {
            setValidatorNotEmpty(selectBoxField.getValidatorNotEmpty(), selectBox);
        }

        if (Field.FIELD_INVALID.equals(selectBoxField.isInvalid()))
            selectBox.setInvalid(true);
        else if (Field.FIELD_VALID.equals(selectBoxField.isInvalid()))
            selectBox.setInvalid(false);



    }

    public void setSelectBoxList(SelectBoxField selectBoxField, SelectBox selectBox) {
        if (selectBox.isInvalid())
            selectBoxField.setInvalid(ValidatorField.VALIDATOR_INVALID);
        else
            selectBoxField.setInvalid(ValidatorField.VALIDATOR_VALID);
    }

    protected void createSelectBoxList(List fieldList, SelectBox selectBox) {
        SelectBoxField selectBoxField  = new SelectBoxField();
        selectBoxField.setName(selectBox.getName());
        selectBoxField.setValues(selectBox.getSelectedOptions());
        fieldList.add(selectBoxField);

    }

    public void createDateField(DateFieldField dateField) {
        DateField date = new DateField(dateField.getName());

        if ("1".equals(dateField.getHidden()))
            date.setHidden(true);
        else
            date.setHidden(false);

        if (dateField.getColspan() != null)
            date.setColspan(Integer.parseInt(dateField.getColspan()));

        if (dateField.getRowspan() != null)
            date.setRowspan(Integer.parseInt(dateField.getRowspan()));

        if (dateField.getAlign() != null)
            date.setAlign(dateField.getAlign());

        if (dateField.getValign() != null)
            date.setValign(dateField.getValign());

        if (dateField.getValue() != null && !dateField.getValue().equals("")) {
            SimpleDateFormat inDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            SimpleDateFormat outDateFormat = new SimpleDateFormat(FormatUtil.getInstance().getLongDateFormat());

            try {
                Date utilDate = inDateFormat.parse(dateField.getValue().toString());
                date.setFormat(FormatUtil.getInstance().getLongDateFormat());
                date.setValue(outDateFormat.format(utilDate) );
            }
            catch (ParseException e) {
                Log.getLog(getClass()).error("Error parsing the date", e);
            }
        }


        if ("1".equals(dateField.getRequire())) {
            createValidatorNotEmpty(dateField.getValidatorNotEmpty(), date);
        }

        form.addChild(date);
    }

    public void setDateField(DateFieldField dateField, DateField date) {


        if ("1".equals(dateField.getHidden()))
            date.setHidden(true);
        else
            date.setHidden(false);

        if (dateField.getColspan() != null)
            date.setColspan(Integer.parseInt(dateField.getColspan()));

        if (dateField.getRowspan() != null)
            date.setRowspan(Integer.parseInt(dateField.getRowspan()));

        if (dateField.getAlign() != null)
            date.setAlign(dateField.getAlign());

        if (dateField.getValign() != null)
            date.setValign(dateField.getValign());


        if ("1".equals(dateField.getRequire())) {
            setValidatorNotEmpty(dateField.getValidatorNotEmpty(), date);
        }

        if (Field.FIELD_INVALID.equals(dateField.isInvalid()))
            date.setInvalid(true);
        else if (Field.FIELD_VALID.equals(dateField.isInvalid()))
            date.setInvalid(false);
    }

    public void setDateFieldList(DateFieldField dateField, DateField date) {
        dateField.setDate(date.getDate());
        if (date.isInvalid())
            dateField.setInvalid(ValidatorField.VALIDATOR_INVALID);
        else
            dateField.setInvalid(ValidatorField.VALIDATOR_VALID);
    }

    protected void createDateFieldList(List fieldList, DateField dateField) {
        DateFieldField dateFieldField  = new DateFieldField();
        dateFieldField.setName(dateField.getName());
        dateFieldField.setDate(dateField.getDate());
        fieldList.add(dateFieldField);

    }

    public void createFileField(FileField fileField) {
        FileUpload file = new FileUpload(fileField.getName());

        if ("1".equals(fileField.getHidden()))
            file.setHidden(true);
        else
            file.setHidden(false);

        if (fileField.getColspan() != null)
            file.setColspan(Integer.parseInt(fileField.getColspan()));

        if (fileField.getRowspan() != null)
            file.setRowspan(Integer.parseInt(fileField.getRowspan()));

        if (fileField.getAlign() != null)
            file.setAlign(fileField.getAlign());

        if (fileField.getValign() != null)
            file.setValign(fileField.getValign());


        if ("1".equals(fileField.getRequire())) {
            createValidatorNotEmpty(fileField.getValidatorNotEmpty(), file);
        }

        form.addChild(file);
    }

    public void setFileField(FileField fileField, FileUpload file) {


        if ("1".equals(fileField.getHidden()))
            file.setHidden(true);
        else
            file.setHidden(false);

        if (fileField.getColspan() != null)
            file.setColspan(Integer.parseInt(fileField.getColspan()));

        if (fileField.getRowspan() != null)
            file.setRowspan(Integer.parseInt(fileField.getRowspan()));

        if (fileField.getAlign() != null)
            file.setAlign(fileField.getAlign());

        if (fileField.getValign() != null)
            file.setValign(fileField.getValign());


        if ("1".equals(fileField.getRequire())) {
            setValidatorNotEmpty(fileField.getValidatorNotEmpty(), file);
        }

        if (Field.FIELD_INVALID.equals(fileField.isInvalid()))
            file.setInvalid(true);
        else if (Field.FIELD_VALID.equals(fileField.isInvalid()))
            file.setInvalid(false);


    }

    public void setFileFieldList(FileField fileField, FileUpload file) {

        if (file.isInvalid())
            fileField.setInvalid(ValidatorField.VALIDATOR_INVALID);
        else
            fileField.setInvalid(ValidatorField.VALIDATOR_VALID);
    }

     protected void createFileFieldList(List fieldList, FileUpload fileUpload) {
        FileField fileField  = new FileField();
        fileField.setFileUpload(fileUpload);
        fileField.setName(fileUpload.getName());
        fileField.setValue(fileUpload.getValue());
        fileField.setWidget(fileUpload);
        fieldList.add(fileField);

    }

    public void createTableGridField(TableGridField tableGridField) {
        G2Field g2Field = new G2Field(tableGridField.getName());

        if ("1".equals(tableGridField.getHidden()))
            g2Field.setHidden(true);
        else
            g2Field.setHidden(false);

        if (tableGridField.getColspan() != null)
            g2Field.setColspan(Integer.parseInt(tableGridField.getColspan()));

        if (tableGridField.getRowspan() != null)
            g2Field.setRowspan(Integer.parseInt(tableGridField.getRowspan()));

        if (tableGridField.getAlign() != null)
            g2Field.setAlign(tableGridField.getAlign());

        if (tableGridField.getValign() != null)
            g2Field.setValign(tableGridField.getValign());

        if (tableGridField.getColumnListXml() != null)
            g2Field.setColumnListXml(tableGridField.getColumnListXml());

        if (tableGridField.getTitle() != null)
            g2Field.setTitle(tableGridField.getTitle());

        if (tableGridField.getValue() != null)
            g2Field.setValue(tableGridField.getValue());



        form.addChild(g2Field);
    }

    public void setTableGridList(TableGridField tableGridField, G2Field g2Field) {
         if (g2Field.isInvalid())
            tableGridField.setInvalid(ValidatorField.VALIDATOR_INVALID);
        else
            tableGridField.setInvalid(ValidatorField.VALIDATOR_VALID);
    }

    protected void createTableGridList(List fieldList, G2Field g2Field) {
        TableGridField tableGridField  = new TableGridField();
        tableGridField.setName(g2Field.getName());
        tableGridField.setValue(g2Field.getValue());
        tableGridField.setWidget(g2Field);
        fieldList.add(tableGridField);

    }

    public void createCheckBoxField(List checkBoxList, ButtonGroup buttonGroup) {
        CheckBoxField checkbox;
        CheckBox[] checkBoxs = new CheckBox[checkBoxList.size()];
        int i = 0;



        for (Iterator iterator = checkBoxList.iterator(); iterator.hasNext(); i++) {
            checkbox =  (CheckBoxField) iterator.next();
            checkBoxs[i] = new CheckBox(checkbox.getName(),checkbox.getText(),checkbox.isChecked());
            checkBoxs[i].setValue(checkbox.getValue());

        }
        buttonGroup.addButtons(checkBoxs);
    }

    public void createRadioButtonField(List radioList, ButtonGroup buttonGroup) {
        RadioField radio;
        Radio[] radios = new Radio[radioList.size()];
        int i = 0;



        for (Iterator iterator = radioList.iterator(); iterator.hasNext(); i++) {
            radio =  (RadioField) iterator.next();
            radios[i] = new Radio(radio.getName(),radio.getText(), radio.isChecked());
            radios[i].setValue(radio.getValue());


        }
        buttonGroup.addButtons(radios);
    }

    public void createButtonField(ButtonField buttonField) {
        Button button = new Button(buttonField.getName(),buttonField.getText());
        form.addChild(button);
    }

    public void createButtonField(List buttonList, Widget widget) {
        ButtonField buttonField;
        Button button;

        for (Iterator iterator = buttonList.iterator(); iterator.hasNext();) {
           buttonField =  (ButtonField) iterator.next();
           button = new Button(buttonField.getName(),buttonField.getText());
           widget.addChild(button);
        }
    }

    public void createPanelField(PanelField panelField) {
        Panel panel = new Panel(panelField.getName());

        if (panelField.getColspan() != null)
            panel.setColspan(Integer.parseInt(panelField.getColspan()));

        if (panelField.getRowspan() != null)
            panel.setRowspan(Integer.parseInt(panelField.getRowspan()));

        if (panelField.getAlign() != null)
            panel.setAlign(panelField.getAlign());

        if (panelField.getValign() != null)
            panel.setValign(panelField.getValign());

        if (panelField.getButtonList() != null && panelField.getButtonList().size() > 0)
            createButtonField(panelField.getButtonList(),panel);

        if (panelField.getFormLayout() != null) {
            WidgetEngine engine = new WidgetEngine();
            engine.setLayout(panelField.getFormLayout());
            panel.addChild(engine.retriveWidget());
        }

        form.addChild(panel);
    }

    public void setPanelField(PanelField panelField, Panel panel) {


        if (panelField.getColspan() != null)
            panel.setColspan(Integer.parseInt(panelField.getColspan()));

        if (panelField.getRowspan() != null)
            panel.setRowspan(Integer.parseInt(panelField.getRowspan()));

        if (panelField.getAlign() != null)
            panel.setAlign(panelField.getAlign());

        if (panelField.getValign() != null)
            panel.setValign(panelField.getValign());



        if (panelField.getFormLayout() != null) {
            WidgetEngine widgetEngine = new WidgetEngine();
            widgetEngine.setLayout(panelField.getFormLayout());
            Form form = (Form) panel.getChild(panelField.getFormLayout().getFormName());
            widgetEngine.setWidget(form);
            panel.addChild(form);
        }

    }

    protected void createPanelList(List fieldList, Panel panel) {
        PanelField panelField = new PanelField();
        Collection chilren = panel.getChildren();
        Object object;

        panelField.setName(panel.getName());

        for (Iterator iterator = chilren.iterator(); iterator.hasNext();) {
            object =  iterator.next();
            if (object instanceof Form) {
                WidgetEngine engine = new WidgetEngine((Form)object);
                panelField.setFormLayout(engine.retriveFieldList());
            }

        }
        fieldList.add(panelField);
    }

    public void createHiddenField(HiddenField hiddenField) {
        Hidden hidden = new Hidden(hiddenField.getName());
        hidden.setValue(hiddenField.getValue());
        form.addChild(hidden);
    }

    protected void createHiddenFieldList(List fieldList, Hidden hidden) {
        HiddenField hiddenField = new HiddenField();
        hiddenField.setName(hidden.getName());
        hiddenField.setValue(hidden.getValue());

        fieldList.add(hiddenField);
    }

    protected void createFileLinkCheckBoxField(FileLinkCheckboxField fileLinkCheckboxField) {
        FileLinkCheckbox fileLinkCheckbox = new FileLinkCheckbox(fileLinkCheckboxField.getName());

        fileLinkCheckbox.setRequired(fileLinkCheckboxField.isRequired());


        fileLinkCheckbox.setLinkText(fileLinkCheckboxField.getLinkText());
        fileLinkCheckbox.setLinkUrl(fileLinkCheckboxField.getLinkUrl());


        fileLinkCheckbox.setCheckboxText(fileLinkCheckboxField.getCheckBoxText());
        fileLinkCheckbox.setCheckboxValue(fileLinkCheckboxField.getCheckBoxValue());


        fileLinkCheckbox.setColspan(Integer.parseInt(fileLinkCheckboxField.getColspan()));
        fileLinkCheckbox.setRowspan(Integer.parseInt(fileLinkCheckboxField.getRowspan()));
        fileLinkCheckbox.setAlign(fileLinkCheckboxField.getAlign());
        fileLinkCheckbox.setValign(fileLinkCheckboxField.getValign());

        if (fileLinkCheckboxField.getValidatorFileLinkCheckboxField() != null)
            createValidatorFileLinkCheckbox(fileLinkCheckboxField.getValidatorFileLinkCheckboxField(), fileLinkCheckbox);


        if (Field.FIELD_INVALID.equals(fileLinkCheckboxField.isInvalid()))
            fileLinkCheckbox.setInvalid(true);
        else if (Field.FIELD_VALID.equals(fileLinkCheckboxField.isInvalid()))
             fileLinkCheckbox.setInvalid(false);


        fileLinkCheckbox.init();
        form.addChild(fileLinkCheckbox);
    }

    public void createValidatorFileLinkCheckbox(ValidatorFileLinkCheckboxField validatorFileLinkCheckboxField, Widget widget) {
        ValidatorFileLinkCheckbox validatorFileLinkCheckbox = new ValidatorFileLinkCheckbox(validatorFileLinkCheckboxField.getName(),
                                                                                            validatorFileLinkCheckboxField.getText());

         if (ValidatorField.VALIDATOR_INVALID.equals(validatorFileLinkCheckboxField.isInvalid()))
            validatorFileLinkCheckbox.setInvalid(true);
        else if (ValidatorField.VALIDATOR_VALID.equals(validatorFileLinkCheckboxField.isInvalid()))
            validatorFileLinkCheckbox.setInvalid(false);

        widget.addChild(validatorFileLinkCheckbox);
    }

    protected void createFileLinkCheckBoxList(List fieldList, FileLinkCheckbox fileLinkCheckbox) {
        FileLinkCheckboxField fileLinkCheckboxField = new FileLinkCheckboxField();
        fileLinkCheckboxField.setName(fileLinkCheckbox.getName());
        fileLinkCheckboxField.setChecked(fileLinkCheckbox.getCheckBox().isChecked());
        fileLinkCheckboxField.setCheckBoxValue(fileLinkCheckbox.getCheckboxValue());

        fileLinkCheckboxField.setFileUploadValue((String)fileLinkCheckbox.getFile().getValue());
        fileLinkCheckboxField.setWidget(fileLinkCheckbox);
        fieldList.add(fileLinkCheckboxField);
    }

    protected void setFileLinkCheckboxField(FileLinkCheckboxField fileLinkCheckboxField, FileLinkCheckbox fileLinkCheckbox) {
         if (Field.FIELD_INVALID.equals(fileLinkCheckboxField.isInvalid()))
            fileLinkCheckbox.setInvalid(true);
        else if (Field.FIELD_VALID.equals(fileLinkCheckboxField.isInvalid()))
            fileLinkCheckbox.setInvalid(false);

        if (fileLinkCheckboxField.getValidatorFileLinkCheckboxField() != null)
            setValidatorFileLinkCheckbox(fileLinkCheckboxField.getValidatorFileLinkCheckboxField(), fileLinkCheckbox);
    }

    protected void setValidatorFileLinkCheckbox(ValidatorFileLinkCheckboxField validatorFileLinkCheckboxField, Widget widget) {
        ValidatorFileLinkCheckbox validatorFileLinkCheckbox = (ValidatorFileLinkCheckbox) widget.getChild(validatorFileLinkCheckboxField.getName());

         if (ValidatorField.VALIDATOR_INVALID.equals(validatorFileLinkCheckboxField.isInvalid()))
            validatorFileLinkCheckbox.setInvalid(true);
        else if (ValidatorField.VALIDATOR_VALID.equals(validatorFileLinkCheckboxField.isInvalid()))
            validatorFileLinkCheckbox.setInvalid(false);
    }

    protected void setFileLinkCheckboxFieldList(FileLinkCheckboxField fileLinkCheckboxField, FileLinkCheckbox fileLinkCheckbox) {

        if (fileLinkCheckbox.isInvalid())
            fileLinkCheckboxField.setInvalid(ValidatorField.VALIDATOR_INVALID);
        else
            fileLinkCheckboxField.setInvalid(ValidatorField.VALIDATOR_VALID);
    }

    protected void createLinkField(LinkField linkField) {
        Link link = new Link(linkField.getName());
        link.setText(linkField.getText());
        link.setUrl(linkField.getUrl());
        form.addChild(link);

    }

    public void createListenerForm(ListenerForm listener) {
        try {
            Object object = Class.forName(listener.getClassName()).newInstance();
            form.addFormEventListener((FormEventListener)object);
        }
        catch (InstantiationException e) {
            Log.getLog(getClass()).fatal("Error in instantiating the class :" + listener.getClassName());
        }
        catch (IllegalAccessException e) {
            Log.getLog(getClass()).fatal("Error in accessing the class :" + listener.getClassName());
        }
        catch (ClassNotFoundException e) {
            Log.getLog(getClass()).fatal("Class not found :" + listener.getClassName());
        }
    }

    public void createValidatorNotEmpty(ValidatorNotEmptyField validatorNotEmptyField, Widget widget) {
        ValidatorNotEmpty validatorNotEmpty = new ValidatorNotEmpty(validatorNotEmptyField.getName(),
                                                                    validatorNotEmptyField.getText());
        widget.addChild(validatorNotEmpty);

    }

    public void setValidatorNotEmpty(ValidatorNotEmptyField validatorNotEmptyField, Widget widget) {
        ValidatorNotEmpty validatorNotEmpty = (ValidatorNotEmpty) widget.getChild(validatorNotEmptyField.getName());
        validatorNotEmpty.setText(validatorNotEmptyField.getText());


        if (ValidatorField.VALIDATOR_INVALID.equals(validatorNotEmptyField.isInvalid()))
            validatorNotEmpty.setInvalid(true);
        else if (ValidatorField.VALIDATOR_VALID.equals(validatorNotEmptyField.isInvalid()))
            validatorNotEmpty.setInvalid(false);

    }

    public void createValidatorIsInteger(ValidatorIsIntegerField validatorIsIntegerField, Widget widget) {
        ValidatorIsInteger validatorIsInteger = new ValidatorIsInteger(validatorIsIntegerField.getName(),
                                                                       validatorIsIntegerField.getText());
        if ("true".equals(validatorIsIntegerField.getOptional()))
            validatorIsInteger.setOptional(true);
        else
            validatorIsInteger.setOptional(false);

        widget.addChild(validatorIsInteger);
    }

    public void setValidatorIsInteger(ValidatorIsIntegerField validatorIsIntegerField, Widget widget) {
        ValidatorIsInteger validatorIsInteger = (ValidatorIsInteger) widget.getChild(validatorIsIntegerField.getName());

        if ("true".equals(validatorIsIntegerField.getOptional()))
            validatorIsInteger.setOptional(true);
        else
            validatorIsInteger.setOptional(false);

        validatorIsInteger.setText(validatorIsIntegerField.getText());

        if (ValidatorField.VALIDATOR_INVALID.equals(validatorIsIntegerField.isInvalid()))
            validatorIsInteger.setInvalid(true);
        else if (ValidatorField.VALIDATOR_VALID.equals(validatorIsIntegerField.isInvalid()))
            validatorIsInteger.setInvalid(false);

    }

    public void createValidatorIsNumeric(ValidatorIsNumericField validatorIsNumericField, Widget widget) {
        ValidatorIsNumeric validatorIsNumeric = new ValidatorIsNumeric(validatorIsNumericField.getName(),
                                                                       validatorIsNumericField.getText());


        if ("true".equals(validatorIsNumericField.getOptional()))
            validatorIsNumeric.setOptional(true);
        else
            validatorIsNumeric.setOptional(false);
        widget.addChild(validatorIsNumeric);
    }

    public void setValidatorIsNumeric(ValidatorIsNumericField validatorIsNumericField, Widget widget) {
        ValidatorIsNumeric validatorIsNumeric = (ValidatorIsNumeric) widget.getChild(validatorIsNumericField.getName());

        if ("true".equals(validatorIsNumericField.getOptional()))
            validatorIsNumeric.setOptional(true);
        else
            validatorIsNumeric.setOptional(false);

        if (ValidatorField.VALIDATOR_INVALID.equals(validatorIsNumericField.isInvalid()))
            validatorIsNumeric.setInvalid(true);
        else if (ValidatorField.VALIDATOR_VALID.equals(validatorIsNumericField.isInvalid()))
            validatorIsNumeric.setInvalid(false);

        validatorIsNumeric.setText(validatorIsNumericField.getText());
    }

    public void createValidatorEmail(ValidatorEmailField validatorEmailField, Widget widget) {
        ValidatorEmail validatorEmail = new ValidatorEmail(validatorEmailField.getName(),
                                                           validatorEmailField.getText());

        widget.addChild(validatorEmail);
    }

    public void setValidatorEmail(ValidatorEmailField validatorEmailField, Widget widget) {
        ValidatorEmail validatorEmail = (ValidatorEmail) widget.getChild(validatorEmailField.getName());

        validatorEmail.setText(validatorEmailField.getText());

        if (ValidatorField.VALIDATOR_INVALID.equals(validatorEmailField.isInvalid()))
            validatorEmail.setInvalid(true);
        else if (ValidatorField.VALIDATOR_VALID.equals(validatorEmailField.isInvalid()))
            validatorEmail.setInvalid(false);

    }

    public void setLayout(FormLayout layout) {
        this.layout = layout;
    }

    public Form getForm() {
        return form;
    }




}




