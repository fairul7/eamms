package com.tms.collab.formwizard.ui;


import kacang.stdui.*;
import kacang.ui.Forward;
import kacang.ui.Event;
import kacang.util.UuidGenerator;
import kacang.util.Log;
import kacang.Application;


import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.io.IOException;
import java.io.InputStream;


import com.tms.collab.formwizard.model.*;
import com.tms.collab.formwizard.xmlwidget.*;
import com.tms.collab.formwizard.grid.G2Field;


import org.jdom.Element;
import org.apache.commons.collections.SequencedHashMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.NamedNodeMap;


public class AddFormField extends Form{
    protected static final String FORMID="formid";
    protected SelectBox fieldType;
    protected Radio hiddenYes;
    protected Radio hiddenNo;
    protected Radio requiredYes;
    protected Radio requiredNo;
    protected TextField fieldName;
    protected TextField maxRows;
    protected TextField maxCols;
    protected TextField fieldSize;
    protected TextField tfDefaultValue;
    protected TextField maxLength;
    protected TextBox options;
    protected SelectBox tfDataType;
    protected Button add;
    protected Button finish;
    protected CheckBox independentField;
    protected RichTextBox   rtbLabel;
    protected TextField tfColspan;
    protected TextField tfRowspan;
    protected SelectBox sbAlign;
    protected SelectBox sbValign;
    protected CheckBox cbResetLayout;
    protected TextField tfTitle;
    protected Label lbColumnLabel ;
    protected AddG2FieldForm fieldForm;
    protected String fieldFormAbsoluteName;







    protected String formId;


    public void onRequest(Event event) {
        fieldForm = (AddG2FieldForm)event.getWidgetManager().getWidget(fieldFormAbsoluteName);

        String et =  event.getRequest().getParameter("et");
        if ("editColumn".equals(et) || "deleteColumn".equals(et)) {
        }
        else {
            initilizeFields();

        }
    }

    public AddFormField() {
    }

    public AddFormField(String s) {
        super(s);
    }

    public void init(){
        fieldType = new SelectBox("fieldType");

        hiddenYes = new Radio("hiddenYes");
        hiddenNo = new Radio("hiddenNo");
        hiddenYes.setGroupName("hidden");
        hiddenNo.setGroupName("hidden");
        hiddenYes.setOnClick("toggleLayers('hiddenFieldYes');");
		hiddenNo.setOnClick("toggleLayers('hiddenFieldNo');");

        requiredYes = new Radio("requiredYes");
        requiredNo = new Radio("requiredNo");
        requiredYes.setGroupName("required");
        requiredNo.setGroupName("required");
        requiredYes.setOnClick("toggleLayers('requiredFieldYes');");
        requiredNo.setOnClick("toggleLayers('requiredFieldNo');");

        fieldName = new TextField("fieldName");
        fieldName.setSize("50");
        maxRows =new TextField("maxRows");
        maxRows.setValue("4");
        maxRows.setSize("3");
        maxCols = new TextField("maxCols");
        maxCols.setSize("3");
        maxCols.setValue("25");

        maxLength = new TextField("maxLength");
        maxLength.setSize("3");




        fieldSize = new TextField("fieldSize");
        fieldSize.setSize("3");
        fieldSize.setValue("15");

        tfDefaultValue= new TextField("tfDefaultValue");
        tfDefaultValue.setSize("15");




        options = new TextBox("options");
        options.setRows("10");
        options.setCols("25");
        tfDataType =new SelectBox("tfDataType");

        independentField = new CheckBox("independentField");
        independentField.setOnClick("toggleLayers('independentField');");





        add = new Button("add");
        finish = new Button("finish");


        finish.setText(Application.getInstance().getMessage("formWizard.label.addFormField.done","Done"));
        add.setText(Application.getInstance().getMessage("formWizard.label.addFormField.add","Add"));







        fieldType.setOptions(  FormConstants.FIELD_SELECT + "=" + Application.getInstance().getMessage("formWizard.label.addFormField.select","Select") + ";"
                             + FormConstants.FIELD_TEXT_BLOCK + "=" + Application.getInstance().getMessage("formWizard.label.addFormField.textBlock","Text Block") + ";"
                             + FormConstants.FIELD_TEXT_INPUT + "=" + Application.getInstance().getMessage("formWizard.label.addFormField.textInput","Text Input") + ";"
                             + FormConstants.FIELD_TEXT_BOX + "=" + Application.getInstance().getMessage("formWizard.label.addFormField.textBox","Text Box") + ";"
                             + FormConstants.FIELD_CHECK_BOX + "=" + Application.getInstance().getMessage("formWizard.label.addFormField.checkBox","Check Box") + ";"
                             + FormConstants.FIELD_RADIO_BUTTON + "=" + Application.getInstance().getMessage("formWizard.label.addFormField.radioButton","Radio Button") + ";"
                             + FormConstants.FIELD_PULL_DOWN_MENU + "=" + Application.getInstance().getMessage("formWizard.label.addFormField.pullDownMenu","Pull Down Menu") + ";"
                             + FormConstants.FIELD_DATE_SELECT + "=" + Application.getInstance().getMessage("formWizard.label.addFormField.dateSelect","Date Select") + ";"
                             + FormConstants.FIELD_FILE_UPLOAD + "=" + Application.getInstance().getMessage("formWizard.label.addFormField.fileUpload","File Upload") + ";"
                             + FormConstants.FIELD_TABLE_GRID + "=" + Application.getInstance().getMessage("formWizard.label.addFormField.tableGrid","tableGrid") + ";");
        fieldType.setOnChange("selectField()");
        hiddenNo.setChecked(true);
        requiredNo.setChecked(true);

        tfDataType.setOptions(  FormElement.FORM_VARCHAR_TYPE+"="+Application.getInstance().getMessage("formWizard.label.addFormField.text","text")+";"
                              + FormElement.FORM_NUMERIC_TYPE+"="+Application.getInstance().getMessage("formWizard.label.addFormField.number","number")+";"
                              + FormElement.FORM_DECIMAL_NUMBER + "=" + Application.getInstance().getMessage("formWizard.label.addFormField.datatype.decimal.number","decimal number") + ";"
                              + FormElement.FORM_EMAIL_TYPE+"="+Application.getInstance().getMessage("formWizard.label.addFormField.email","e-mail")+";"
                              );

        //fieldForm = new AddG2FieldForm("fieldForm");

        rtbLabel = new RichTextBox("labelValue");
 
        tfColspan = new TextField("colspan");
        tfColspan.setValue("1");
        tfColspan.setSize("4");

        tfRowspan = new TextField("rowspan");
        tfRowspan.setValue("1");
        tfRowspan.setSize("4");

        sbAlign = new SelectBox("align");
        sbAlign.setOptionMap(getAlignMap());

        sbValign = new SelectBox("valign");
        sbValign.setOptionMap(getValignMap());

        cbResetLayout = new CheckBox("resetLayout");
        cbResetLayout.setChecked(true);

        tfTitle = new TextField("title");

        lbColumnLabel = new Label("columnLabel");
        lbColumnLabel.setText("");
        addChild(lbColumnLabel);




        addChild(fieldType);
        addChild(fieldName);
        addChild(hiddenYes);
        addChild(hiddenNo);
        addChild(requiredYes);
        addChild(requiredNo);
        addChild(maxRows);
        addChild(maxCols);
        addChild(fieldSize);
        addChild(tfDefaultValue);
        addChild(options);
        addChild(tfDataType);
        addChild(add);
        addChild(finish);
        addChild(maxLength);
        addChild(independentField);
        addChild(rtbLabel);
        addChild(tfColspan);
        addChild(tfRowspan);
        addChild(sbAlign);
        addChild(sbValign);
        addChild(cbResetLayout);
        addChild(tfTitle);


        setMethod("post");
    }

    public Map getAlignMap() {
        Map alignMap = new SequencedHashMap();
        alignMap.put("left", Application.getInstance().getMessage("formWizard.label.addFormField.left","Left"));
        alignMap.put("center", Application.getInstance().getMessage("formWizard.label.addFormField.center","Center"));
        alignMap.put("right", Application.getInstance().getMessage("formWizard.label.addFormField.right","Right"));
        return alignMap;
    }

    public Map getValignMap() {
        Map alignMap = new SequencedHashMap();
        alignMap.put("top", Application.getInstance().getMessage("formWizard.label.addFormField.top","Top"));
        alignMap.put("middle", Application.getInstance().getMessage("formWizard.label.addFormField.middle","Middle"));
        alignMap.put("bottom", Application.getInstance().getMessage("formWizard.label.addFormField.bottom","Bottom"));
        return alignMap;
    }

    public void initilizeFields() {
        //reset the fieldtype to display the newly added template
        FormModule module = (FormModule) Application.getInstance().getModule(FormModule.class);
        Collection data = null;
        FormTemplate formTemplate = null;
        SequencedHashMap map = null;



        map = new SequencedHashMap();
        map.put(FormConstants.FIELD_SELECT , Application.getInstance().getMessage("formWizard.label.addFormField.select","Select"));
        map.put(FormConstants.FIELD_TEXT_BLOCK , Application.getInstance().getMessage("formWizard.label.addFormField.textBlock","Text Block"));
        map.put(FormConstants.FIELD_TEXT_INPUT , Application.getInstance().getMessage("formWizard.label.addFormField.textInput","Text Input"));
        map.put(FormConstants.FIELD_TEXT_BOX , Application.getInstance().getMessage("formWizard.label.addFormField.textBox","Text Box"));
        map.put(FormConstants.FIELD_CHECK_BOX , Application.getInstance().getMessage("formWizard.label.addFormField.checkBox","Check Box"));
        map.put(FormConstants.FIELD_RADIO_BUTTON , Application.getInstance().getMessage("formWizard.label.addFormField.radioButton","Radio Button"));
        map.put(FormConstants.FIELD_PULL_DOWN_MENU , Application.getInstance().getMessage("formWizard.label.addFormField.pullDownMenu","Pull Down Menu"));
        map.put(FormConstants.FIELD_DATE_SELECT , Application.getInstance().getMessage("formWizard.label.addFormField.dateSelect","Date Select"));
        map.put(FormConstants.FIELD_FILE_UPLOAD , Application.getInstance().getMessage("formWizard.label.addFormField.fileUpload","File Upload"));
        map.put(FormConstants.FIELD_TABLE_GRID , Application.getInstance().getMessage("formWizard.label.addFormField.tableGrid","Table Grid"));



        try {
            data = module.getFormTemplate(null,false,0,-1);
            if (data.size() > 0)
                map.put(FormConstants.FIELD_TEMPLATE_DIVIDER, "------------------------");
            for (Iterator iterator = data.iterator(); iterator.hasNext();) {
                 formTemplate =  (FormTemplate) iterator.next();
                 map.put(formTemplate.getFormTemplateId(), formTemplate.getTemplateName());
            }
        }
        catch (FormDaoException e) {
            Log.getLog(getClass()).error(e.getMessage(),e);
        }



        fieldType.setOptionMap(map);

        fieldType.setSelectedOption(FormConstants.FIELD_SELECT);
        hiddenNo.setChecked(true);
        hiddenYes.setChecked(false);

        requiredNo.setChecked(true);
        requiredYes.setChecked(false);

        independentField.setChecked(false);
        cbResetLayout.setChecked(true);

        fieldName.setValue("");

        fieldSize.setValue("");
        rtbLabel.setValue("");
        tfDefaultValue.setValue("");
        maxCols.setValue("");
        maxRows.setValue("");
        tfDataType.setSelectedOption(String.valueOf(FormElement.FORM_VARCHAR_TYPE));
        options.setValue("");
        maxLength.setValue("");


        //reset the field error message
        fieldName.setMessage("");
        maxCols.setMessage("");
        fieldSize.setMessage("");
        maxRows.setMessage("");
        options.setMessage("");
        fieldType.setMessage("");
        maxLength.setMessage("");
        rtbLabel.setMessage("");
        tfColspan.setMessage("");
        tfRowspan.setMessage("");
        tfTitle.setMessage("");

        fieldName.setInvalid(false);
        maxCols.setInvalid(false);
        fieldSize.setInvalid(false);
        maxRows.setInvalid(false);
        options.setInvalid(false);
        fieldType.setInvalid(false);
        maxLength.setInvalid(false);
        rtbLabel.setInvalid(false);
        tfColspan.setInvalid(false);
        tfRowspan.setInvalid(false);
        tfTitle.setInvalid(false);
        setInvalid(false);

        tfColspan.setValue("1");
        tfRowspan.setValue("1");

        tfTitle.setValue("");
        lbColumnLabel.setText("");







    }

    public Forward onSubmit(Event evt) {


        Forward result = new Forward();
        result = super.onSubmit(evt);
        fieldName.setMessage("");
        maxCols.setMessage("");
        fieldSize.setMessage("");
        maxRows.setMessage("");
        options.setMessage("");
        fieldType.setMessage("");
        maxLength.setMessage("");
        rtbLabel.setMessage("");
        tfColspan.setMessage("");
        tfRowspan.setMessage("");
        tfTitle.setMessage("");
        lbColumnLabel.setText("");


        fieldName.setInvalid(false);
        maxCols.setInvalid(false);
        fieldSize.setInvalid(false);
        maxRows.setInvalid(false);
        options.setInvalid(false);
        fieldType.setInvalid(false);
        maxLength.setInvalid(false);
        rtbLabel.setInvalid(false);
        tfColspan.setInvalid(false);
        tfRowspan.setInvalid(false);
        tfTitle.setInvalid(false);
        setInvalid(false);



        return result;
    }

    public Forward onValidate(Event evt){
        Forward fwd = null;
        String buttonName = findButtonClicked(evt);

        if (fieldForm == null)
            fieldForm = (AddG2FieldForm)evt.getWidgetManager().getWidget(fieldFormAbsoluteName);                



        if (buttonName != null) {
            if (finish.getAbsoluteName().equals(buttonName)){
        	    fwd = activateForm();
            }
            else {
                verifyField();
                if (isInvalid()) {
                    return null;
                }

                if (add.getAbsoluteName().equals(buttonName)) {
                    fwd = addField(evt.getRequest());
                }
            }
        }
        else {
            if (fieldForm != null) {
                fieldForm.setColumnList(new ArrayList());
                fieldForm.setIdMap(new HashMap());
            }
        }

        return fwd;
    }

    protected void verifyField() {
		String selected = null;
		Collection selectedList = (Collection)fieldType.getValue();

		if(selectedList.iterator().hasNext())
			selected = (String) selectedList.iterator().next();

        if (tfColspan.getValue().toString().trim().length() <= 0) {
            tfColspan.setInvalid(true);
			tfColspan.setMessage(Application.getInstance().getMessage("formWizard.label.addFormField.colspan","Colspan cannot be empty."));
			setInvalid(true);
        }
        else if (isNumeric(tfColspan.getValue().toString().trim())) {
            int colspan = Integer.parseInt(tfColspan.getValue().toString().trim());
            if (colspan < 1) {
                tfColspan.setInvalid(true);
				tfColspan.setMessage(Application.getInstance().getMessage("formWizard.label.addFormField.colspanGreater","Colspan must be greater than 0."));
				setInvalid(true);
            }
        }
        else {
            tfColspan.setInvalid(true);
			tfColspan.setMessage(Application.getInstance().getMessage("formWizard.label.addFormField.colspanNumeric","Colspan must be greater than 0 and without decimal point."));
			setInvalid(true);
        }


        if (tfRowspan.getValue().toString().trim().length() <= 0) {
            tfRowspan.setInvalid(true);
			tfRowspan.setMessage(Application.getInstance().getMessage("formWizard.label.addFormField.rowspanEmpty","Rowspan cannot be empty."));
			setInvalid(true);
        }
        else if (isNumeric(tfRowspan.getValue().toString().trim())) {
            int rowspan = Integer.parseInt(tfRowspan.getValue().toString().trim());
            if (rowspan < 1) {
                tfRowspan.setInvalid(true);
				tfRowspan.setMessage(Application.getInstance().getMessage("formWizard.label.addFormField.rowspanGreater","Rowspan must be greater than 0."));
				setInvalid(true);
            }
        }
        else {
            tfRowspan.setInvalid(true);
            tfRowspan.setMessage(Application.getInstance().getMessage("formWizard.label.addFormField.rowspanNumeric","Rowspan must be greater than 0 and without decimal point."));
            setInvalid(true);


        }

        if  (selected.equals(FormConstants.FIELD_TEXT_BLOCK)) {
            if (rtbLabel.getValue().toString().trim().length() <= 0 || "<p>".equalsIgnoreCase(rtbLabel.getValue().toString())||"<body />".equalsIgnoreCase(rtbLabel.getValue().toString())) {
                rtbLabel.setInvalid(true);
                rtbLabel.setMessage(Application.getInstance().getMessage("formWizard.label.addFormField.labelEmpty","Label cannot be empty"));
                setInvalid(true);
            }
        }
        else if (selected.equals(FormConstants.FIELD_TABLE_GRID)) {
            if ("".equals(tfTitle.getValue().toString())) {
                tfTitle.setInvalid(true);
                tfTitle.setMessage(Application.getInstance().getMessage("formWizard.label.addFormField.titleEmpty","Title cannot be empty"));
                setInvalid(true);
            }

            if ( fieldForm != null && fieldForm.getColumnListSize() < 1) {
                lbColumnLabel.setText(Application.getInstance().getMessage("formWizard.label.addFormField.addAColumn","Please add at least a column"));
                setInvalid(true);
           }
        }
		else if (selected != null && !selected.equals("") && !selected.equals(FormConstants.FIELD_SELECT) && !selected.equals(FormConstants.FIELD_TEMPLATE_DIVIDER)) {
            if (    selected.equals(FormConstants.FIELD_TEXT_INPUT)
                 || selected.equals(FormConstants.FIELD_TEXT_BOX)
                 || selected.equals(FormConstants.FIELD_CHECK_BOX)
				 || selected.equals(FormConstants.FIELD_RADIO_BUTTON)
				 || selected.equals(FormConstants.FIELD_PULL_DOWN_MENU)
				 || selected.equals(FormConstants.FIELD_FILE_UPLOAD)
				 || selected.equals(FormConstants.FIELD_DATE_SELECT)) {


			    if ( fieldName.getValue().toString().trim().length() <= 0) {
				    fieldName.setInvalid(true);
				    fieldName.setMessage(Application.getInstance().getMessage("formWizard.label.addFormField.fieldNotEmpty","Field name cannot be empty."));
				    setInvalid(true);
			    }





                if (selected.equals(FormConstants.FIELD_TEXT_INPUT))  {
                    if (maxLength.getValue().toString().trim().length() <= 0
                      || !isNumeric(maxLength.getValue().toString().trim())) {

                        maxLength.setInvalid(true);
                        maxLength.setMessage(Application.getInstance().getMessage("formWizard.label.addFormField.maxLengthNumeric","Max length is numeric and cannot be empty."));
                        setInvalid(true);
                    }
                    else {
                        int maxLengthValue = Integer.parseInt(maxLength.getValue().toString().trim());
                        if (maxLengthValue > 255) {
                            maxLength.setInvalid(true);
                            maxLength.setMessage(Application.getInstance().getMessage("formWizard.label.addFormField.maxLengthGreater","Max length value cannot greater than 255."));
                            setInvalid(true);
                        }
                    }

                    if (fieldSize.getValue().toString().trim().length() <=0
                      || !isNumeric(fieldSize.getValue().toString().trim())) {

                        fieldSize.setInvalid(true);
                        fieldSize.setMessage(Application.getInstance().getMessage("formWizard.label.addFormField.fieldSizeNumeric","Field size is numeric and cannot be empty."));
                        setInvalid(true);
                    }

                }
                else if (selected.equals(FormConstants.FIELD_TEXT_BOX)) {

                    if (maxCols.getValue().toString().trim().length() <= 0
                      || !isNumeric(maxCols.getValue().toString().trim())) {
                        maxCols.setInvalid(true);
                        maxCols.setMessage(Application.getInstance().getMessage("formWizard.label.addFormField.maxColsNumeric","Max cols is numeric and cannot be empty."));
                        setInvalid(true);
                    }

                    if (maxRows.getValue().toString().trim().length() <= 0
                      || !isNumeric(maxRows.getValue().toString().trim())) {
                        maxRows.setInvalid(true);
                        maxRows.setMessage(Application.getInstance().getMessage("formWizard.label.addFormField.maxRowsNumeric","Max rows is numeric and cannot be empty."));
                        setInvalid(true);
                    }


                }
                else if (selected.equals(FormConstants.FIELD_CHECK_BOX)
                      || selected.equals(FormConstants.FIELD_RADIO_BUTTON)
                      || selected.equals(FormConstants.FIELD_PULL_DOWN_MENU))  {

                    if (options.getValue().toString().trim().length() <= 0 ) {
                        options.setInvalid(true);
                        options.setMessage(Application.getInstance().getMessage("formWizard.label.addFormField.optionsNotEmpty","Options cannot be empty."));
                        setInvalid(true);
                    }else {
                    	String []array=options.getValue().toString().split("\r\n");

                    	for(int i=0;i<array.length-1;i++){
                    		for(int j=i+1;j<array.length;j++){
                        		if(array[i].equals(array[j])){
                        			options.setInvalid(true);
                                    options.setMessage(Application.getInstance().getMessage("formWizard.label.addFormField.optionsDuplicate","Duplicate options found."));
                                    setInvalid(true);
                        		}
                        	}
                    	}
                    }
                }
            }
		}
		else {
			fieldType.setMessage(Application.getInstance().getMessage("formWizard.label.addFormField.selectFieldType","Please select a field type or template."));
			fieldType.setInvalid(true);
			setInvalid(true);
		}

    }

    private boolean isNumeric(String str) {
           boolean isNumeric = true;

           try {
               Integer.parseInt(str);
           }
           catch (Exception e) {
               isNumeric = false;
           }
           return isNumeric;
    }

    public FormElement getFormElement() throws FormException, FormDaoException, FormDocumentException {
        FormElement form = null;
        form = Util.getFormElement(getFormId());
        getFieldElement(form);
        return form;
    }

    public void getFieldElement(FormElement form) throws FormException, FormDaoException, FormDocumentException {


        String selected = "";
        FormFieldDataObject ffDO = new FormFieldDataObject();


        ffDO.setFormId(getFormId());
        ffDO.setFormFieldId(Calendar.getInstance().get(Calendar.YEAR) + "_" + UuidGenerator.getInstance().getUuid());
        Collection selectedList = (Collection) fieldType.getValue();
        if (selectedList.iterator().hasNext())
            selected = (String) selectedList.iterator().next();
        ffDO.setFieldType(selected);
        ffDO.setName(fieldName.getValue().toString());
        ffDO.setColspan(tfColspan.getValue().toString());
        ffDO.setRowspan(tfRowspan.getValue().toString());
        ffDO.setAlign((String)sbAlign.getSelectedOptions().keySet().iterator().next());
        ffDO.setValign((String)sbValign.getSelectedOptions().keySet().iterator().next());

        if (selected.equals(FormConstants.FIELD_TEXT_INPUT)) {
            ffDO.setFieldSize(fieldSize.getValue().toString());
            Collection cSelectedList = (Collection) tfDataType.getValue();
            String dataTypeSelected = "";
            if (cSelectedList.iterator().hasNext())
                dataTypeSelected = (String) cSelectedList.iterator().next();
            ffDO.setDataType(dataTypeSelected);
            ffDO.setDefaultValue(tfDefaultValue.getValue().toString());
            ffDO.setMaxLength(maxLength.getValue().toString());
        }
        else if (selected.equals(FormConstants.FIELD_TEXT_BOX)) {
            ffDO.setDataType("1");
            ffDO.setDefaultValue(tfDefaultValue.getValue().toString());
        }
        else {
            ffDO.setDataType("1");
        }

        if (hiddenYes.isChecked())
            ffDO.setHiddenFlag("1");
        else if (hiddenNo.isChecked())
            ffDO.setHiddenFlag("0");


        if (requiredYes.isChecked())
            ffDO.setRequiredFlag("1");
        else if (requiredNo.isChecked())
            ffDO.setRequiredFlag("0");

        //for db2 field name cannot start with _ 
        String name = "f_" + System.currentTimeMillis();
        
        ffDO.setFormFieldId(name + "lb");

        if (selected.equals(FormConstants.FIELD_TEXT_INPUT)
            || selected.equals(FormConstants.FIELD_TEXT_BOX)
            || selected.equals(FormConstants.FIELD_CHECK_BOX)
            || selected.equals(FormConstants.FIELD_RADIO_BUTTON)
            || selected.equals(FormConstants.FIELD_PULL_DOWN_MENU)
            || selected.equals(FormConstants.FIELD_FILE_UPLOAD)
            || selected.equals(FormConstants.FIELD_DATE_SELECT)) {
            ffDO.setAlign("left");
            form.addContent(new LabelElement(ffDO));
            ffDO.setAlign((String)sbAlign.getSelectedOptions().keySet().iterator().next());
        }




        ffDO.setFormFieldId(name);

        if (selected.equals(FormConstants.FIELD_TEXT_INPUT)) {
            form.addContent(new TextFieldElement(ffDO));
        }
        else if (selected.equals(FormConstants.FIELD_TEXT_BOX)) {
            ffDO.setMaxRows(maxRows.getValue().toString());
            ffDO.setMaxCols(maxCols.getValue().toString());
            form.addContent(new TextBoxElement(ffDO));
        }
        else if (selected.equals(FormConstants.FIELD_CHECK_BOX)) {
            Element btn = new CheckBoxElement(ffDO);
            form.addContent(btn);
            addOptions(options.getValue().toString(), btn);
        }
        else if (selected.equals(FormConstants.FIELD_RADIO_BUTTON)) {
            Element btn = new RadioElement(ffDO);
            form.addContent(btn);
            addRadioOptions(options.getValue().toString(), btn, ffDO.getName());
        }
        else if (selected.equals(FormConstants.FIELD_PULL_DOWN_MENU)) {
            ffDO.setOptions(options.getValue().toString());
            form.addContent(new SelectElement(ffDO));
        }
        else if (selected.equals(FormConstants.FIELD_DATE_SELECT)) {
            form.addContent(new DateFieldElement(ffDO));
        }
        else if (selected.equals(FormConstants.FIELD_FILE_UPLOAD)) {
            form.addContent(new FileUploadElement(ffDO));

        }
        else if (selected.equals(FormConstants.FIELD_TEXT_BLOCK)) {
            ffDO.setFormFieldId(name + "lb" + FormConstants.FIELD_TEXT_BLOCK_SUFFIX);
            ffDO.setName(rtbLabel.getValue().toString());            
            ffDO.setLabelColspan(ffDO.getColspan());
            form.addContent(new TextBlockElement(ffDO));
        }
        else if (selected.equals(FormConstants.FIELD_TABLE_GRID)) {



            G2Field gridField = new G2Field(ffDO.getName());
            String columnListXml = "";
            if(fieldForm!=null) {
                List columnList = fieldForm.getColumnList();
                gridField.setColumnList(columnList);
                columnListXml = gridField.getColumnListXml();
            }
            ffDO.setName(tfTitle.getValue().toString());
            form.addContent(new TableGridElement(ffDO,columnListXml));
        }
        //form template
        else {
            if (independentField.isChecked() ) {
                addFormTemplateField(form, selected, ffDO);
            }
            else {
                form.addContent(new TemplateElement(name, selected,ffDO));
            }
        }


    }

    public void addFormTemplateField(FormElement form, String templateId, FormFieldDataObject ffDO) throws FormDaoException, FormDocumentException, FormException {
        InputStream stream = null;
        org.w3c.dom.Document domDocument = null;
        Node node = null;
        NodeList nodeList = null;
        FormModule module = null;
        String name =  null;
        

        module = (FormModule)Application.getInstance().getModule(FormModule.class);
        stream = module.getTemplatePreviewXml(templateId);
        try {
            domDocument = Util.buildDOMDocument(stream);
            node = domDocument.getDocumentElement();
            nodeList = node.getChildNodes();

            for (int i =0; i < nodeList.getLength(); i++) {

                if (nodeList.item(i).getNodeName().equals(LabelElement.ELEMENT_NAME) ||
                    nodeList.item(i).getNodeName().equals(TableGridElement.ELEMENT_NAME)) {
                	//for db2 field name cannot start with _
                    name = "f_" + i + System.currentTimeMillis();
                }

                form = parseElement(form,nodeList.item(i),ffDO,name);
            }
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

    public FormElement parseElement(FormElement form,Node node,FormFieldDataObject ffDO, String name) throws FormException {
        NamedNodeMap attributes = node.getAttributes(), childAttributes = null;
        Node childchildNode = null;
        Element btn = null;
        NodeList nodeList = null;
        SequencedHashMap map = null;



        if (node.getNodeName().equals(LabelElement.ELEMENT_NAME)) {
            ffDO.setFormFieldId(name + "lb");
            if (attributes.getNamedItem("colspan") != null)
                ffDO.setLabelColspan(attributes.getNamedItem("colspan").getNodeValue());

            if (attributes.getNamedItem("rowspan") != null)
                ffDO.setRowspan(attributes.getNamedItem("rowspan").getNodeValue());

            if (attributes.getNamedItem("align") != null)
                ffDO.setAlign(attributes.getNamedItem("align").getNodeValue());

            if (attributes.getNamedItem("valign") != null)
                ffDO.setValign(attributes.getNamedItem("valign").getNodeValue());

            if (attributes.getNamedItem("type") != null && attributes.getNamedItem("type").getNodeValue().equals(FormConstants.FIELD_TEXT_BLOCK_SUFFIX)) {
                ffDO.setFormFieldId(ffDO.getFormFieldId() + FormConstants.FIELD_TEXT_BLOCK_SUFFIX);
            }

            if (attributes.getNamedItem("text") != null)
                ffDO.setName(attributes.getNamedItem("text").getNodeValue());

            if (attributes.getNamedItem("require") != null)
                ffDO.setRequiredFlag(attributes.getNamedItem("require").getNodeValue());

            if (attributes.getNamedItem("hidden") != null)
                ffDO.setHiddenFlag(attributes.getNamedItem("hidden").getNodeValue());


            if (attributes.getNamedItem("type") != null
                && attributes.getNamedItem("type").getNodeValue().equals(FormConstants.FIELD_TEXT_BLOCK_SUFFIX))
                form.addContent(new TextBlockElement(ffDO));
            else
                form.addContent(new LabelElement(ffDO));


        }
        else if (node.getNodeName().equals(TextFieldElement.ELEMENT_NAME)) {
            ffDO.setFormFieldId(name);
            ffDO.setDefaultValue("");

            if (attributes.getNamedItem("colspan") != null)
                ffDO.setColspan(attributes.getNamedItem("colspan").getNodeValue());

            if (attributes.getNamedItem("rowspan") != null)
                ffDO.setRowspan(attributes.getNamedItem("rowspan").getNodeValue());

            if (attributes.getNamedItem("align") != null)
                ffDO.setAlign(attributes.getNamedItem("align").getNodeValue());

            if (attributes.getNamedItem("valign") != null)
                ffDO.setValign(attributes.getNamedItem("valign").getNodeValue());

            if (attributes.getNamedItem("value") != null)
                ffDO.setDefaultValue(attributes.getNamedItem("value").getNodeValue());

            if (attributes.getNamedItem("size") != null)
                ffDO.setFieldSize(attributes.getNamedItem("size").getNodeValue());

            if (attributes.getNamedItem("maxLength") != null)
                ffDO.setMaxLength(attributes.getNamedItem("maxLength").getNodeValue());

            if (attributes.getNamedItem("type") != null)
                ffDO.setDataType(attributes.getNamedItem("type").getNodeValue());

            if (attributes.getNamedItem("require") != null)
                ffDO.setRequiredFlag(attributes.getNamedItem("require").getNodeValue());

            if (attributes.getNamedItem("hidden") != null)
                ffDO.setHiddenFlag(attributes.getNamedItem("hidden").getNodeValue());

            form.addContent(new TextFieldElement(ffDO));


        }
        else if (node.getNodeName().equals(TextBoxElement.ELEMENT_NAME)) {
            ffDO.setFormFieldId(name);
            ffDO.setDefaultValue("");
            if (attributes.getNamedItem("colspan") != null)
                ffDO.setColspan(attributes.getNamedItem("colspan").getNodeValue());

            if (attributes.getNamedItem("rowspan") != null)
                ffDO.setRowspan(attributes.getNamedItem("rowspan").getNodeValue());

            if (attributes.getNamedItem("align") != null)
                ffDO.setAlign(attributes.getNamedItem("align").getNodeValue());

            if (attributes.getNamedItem("valign") != null)
                ffDO.setValign(attributes.getNamedItem("valign").getNodeValue());

            if (attributes.getNamedItem("value") != null)
                ffDO.setDefaultValue(attributes.getNamedItem("value").getNodeValue());

            if (attributes.getNamedItem("rows") != null)
                ffDO.setMaxRows(attributes.getNamedItem("rows").getNodeValue());

            if (attributes.getNamedItem("cols") != null)
                ffDO.setMaxCols(attributes.getNamedItem("cols").getNodeValue());

            if (attributes.getNamedItem("type") != null)
                ffDO.setDataType(attributes.getNamedItem("type").getNodeValue());

            if (attributes.getNamedItem("require") != null)
                ffDO.setRequiredFlag(attributes.getNamedItem("require").getNodeValue());

            if (attributes.getNamedItem("hidden") != null)
                ffDO.setHiddenFlag(attributes.getNamedItem("hidden").getNodeValue());

            form.addContent(new TextBoxElement(ffDO));

        }
        else if (node.getNodeName().equals(ButtonGroupElement.ELEMENT_NAME)) {
            if (attributes.getNamedItem("colspan") != null)
                ffDO.setColspan(attributes.getNamedItem("colspan").getNodeValue());

            if (attributes.getNamedItem("rowspan") != null)
                ffDO.setRowspan(attributes.getNamedItem("rowspan").getNodeValue());

            if (attributes.getNamedItem("align") != null)
                ffDO.setAlign(attributes.getNamedItem("align").getNodeValue());

            if (attributes.getNamedItem("valign") != null)
                ffDO.setValign(attributes.getNamedItem("valign").getNodeValue());

            if (attributes.getNamedItem("type") == null) {
                ffDO.setFormFieldId(name);

                if (attributes.getNamedItem("require") != null)
                    ffDO.setRequiredFlag(attributes.getNamedItem("require").getNodeValue());

                if (attributes.getNamedItem("hidden") != null)
                    ffDO.setHiddenFlag(attributes.getNamedItem("hidden").getNodeValue());


                btn = new RadioElement(ffDO);
                form.addContent(btn);




                nodeList = node.getChildNodes();
                map = new SequencedHashMap();
                for (int i = 0; i < nodeList.getLength(); i++) {
                    childchildNode = nodeList.item(i);
                    childAttributes = childchildNode.getAttributes();
                    if (ButtonGroupElement.RADIO_ELEMENT_NAME.equals(childchildNode.getNodeName())) {
                        if (childAttributes.getNamedItem("value") != null && childAttributes.getNamedItem("text") != null)
                            map.put(childAttributes.getNamedItem("value").getNodeValue(),childAttributes.getNamedItem("text").getNodeValue());
                    }
                }

                addRadioOptions(map, btn,ffDO.getName());




            }
            else if (attributes.getNamedItem("type").getNodeValue().equals(ButtonGroupElement.CHECKBOX_ELEMENT_NAME)) {
                ffDO.setFormFieldId(name);

                if (attributes.getNamedItem("colspan") != null)
                    ffDO.setColspan(attributes.getNamedItem("colspan").getNodeValue());

                if (attributes.getNamedItem("rowspan") != null)
                    ffDO.setRowspan(attributes.getNamedItem("rowspan").getNodeValue());

                if (attributes.getNamedItem("align") != null)
                    ffDO.setAlign(attributes.getNamedItem("align").getNodeValue());

                if (attributes.getNamedItem("valign") != null)
                    ffDO.setValign(attributes.getNamedItem("valign").getNodeValue());

                if (attributes.getNamedItem("require") != null)
                    ffDO.setRequiredFlag(attributes.getNamedItem("require").getNodeValue());

                if (attributes.getNamedItem("hidden") != null)
                    ffDO.setHiddenFlag(attributes.getNamedItem("hidden").getNodeValue());

                btn = new CheckBoxElement(ffDO);
                form.addContent(btn);



                nodeList = node.getChildNodes();
                map = new SequencedHashMap();
                for (int i = 0; i < nodeList.getLength(); i++) {
                    childchildNode = nodeList.item(i);
                    childAttributes = childchildNode.getAttributes();
                    if (ButtonGroupElement.CHECKBOX_ELEMENT_NAME.equals(childchildNode.getNodeName())) {
                        if (childAttributes.getNamedItem("value") != null && childAttributes.getNamedItem("text") != null)
                            map.put(childAttributes.getNamedItem("value").getNodeValue(),childAttributes.getNamedItem("text").getNodeValue());
                    }
                }

                addOptions(map, btn);
            }
        }
        else if (node.getNodeName().equals(SelectElement.ELEMENT_NAME)) {
            ffDO.setFormFieldId(name);



            if (attributes.getNamedItem("colspan") != null)
                ffDO.setColspan(attributes.getNamedItem("colspan").getNodeValue());

            if (attributes.getNamedItem("rowspan") != null)
                ffDO.setRowspan(attributes.getNamedItem("rowspan").getNodeValue());

            if (attributes.getNamedItem("align") != null)
                ffDO.setAlign(attributes.getNamedItem("align").getNodeValue());

            if (attributes.getNamedItem("valign") != null)
                ffDO.setValign(attributes.getNamedItem("valign").getNodeValue());

            if (attributes.getNamedItem("require") != null)
                ffDO.setRequiredFlag(attributes.getNamedItem("require").getNodeValue());

            if (attributes.getNamedItem("hidden") != null)
                ffDO.setHiddenFlag(attributes.getNamedItem("hidden").getNodeValue());

            if (attributes.getNamedItem("options") != null)
                ffDO.setOptions(attributes.getNamedItem("options").getNodeValue());
            form.addContent(new SelectElement(ffDO));



        }
        else if (node.getNodeName().equals(DateFieldElement.ELEMENT_NAME)) {
            ffDO.setFormFieldId(name);

            if (attributes.getNamedItem("colspan") != null)
                ffDO.setColspan(attributes.getNamedItem("colspan").getNodeValue());

            if (attributes.getNamedItem("rowspan") != null)
                ffDO.setRowspan(attributes.getNamedItem("rowspan").getNodeValue());

            if (attributes.getNamedItem("align") != null)
                ffDO.setAlign(attributes.getNamedItem("align").getNodeValue());

            if (attributes.getNamedItem("valign") != null)
                ffDO.setValign(attributes.getNamedItem("valign").getNodeValue());

            if (attributes.getNamedItem("require") != null)
                ffDO.setRequiredFlag(attributes.getNamedItem("require").getNodeValue());

            if (attributes.getNamedItem("hidden") != null)
                ffDO.setHiddenFlag(attributes.getNamedItem("hidden").getNodeValue());

            form.addContent(new DateFieldElement(ffDO));


        }
        else if (node.getNodeName().equals(FileUploadElement.ELEMENT_NAME)) {
            ffDO.setFormFieldId(name);

            if (attributes.getNamedItem("colspan") != null)
                ffDO.setColspan(attributes.getNamedItem("colspan").getNodeValue());

            if (attributes.getNamedItem("rowspan") != null)
                ffDO.setRowspan(attributes.getNamedItem("rowspan").getNodeValue());

            if (attributes.getNamedItem("align") != null)
                ffDO.setAlign(attributes.getNamedItem("align").getNodeValue());

            if (attributes.getNamedItem("valign") != null)
                ffDO.setValign(attributes.getNamedItem("valign").getNodeValue());

            if (attributes.getNamedItem("require") != null)
                ffDO.setRequiredFlag(attributes.getNamedItem("require").getNodeValue());

            if (attributes.getNamedItem("hidden") != null)
                ffDO.setHiddenFlag(attributes.getNamedItem("hidden").getNodeValue());

            form.addContent(new FileUploadElement(ffDO));
        }
        else if (node.getNodeName().equals(TableGridElement.ELEMENT_NAME)) {
            ffDO.setFormFieldId(name);
            if (attributes.getNamedItem("colspan") != null)
                ffDO.setColspan(attributes.getNamedItem("colspan").getNodeValue());

            if (attributes.getNamedItem("rowspan") != null)
                ffDO.setRowspan(attributes.getNamedItem("rowspan").getNodeValue());

            if (attributes.getNamedItem("align") != null)
                ffDO.setAlign(attributes.getNamedItem("align").getNodeValue());

            if (attributes.getNamedItem("valign") != null)
                ffDO.setValign(attributes.getNamedItem("valign").getNodeValue());
            ffDO.setName(attributes.getNamedItem("title").getNodeValue());
            form.addContent(new TableGridElement(ffDO, attributes.getNamedItem("columnListXml").getNodeValue()));
        }


       return form;



    }

    public Forward addField(HttpServletRequest request){
        Forward forward = null;
        FormElement form =  null;
        try {
            form = getFormElement();
            forward = addFormConfig(form);
        }
        catch (FormException e) {
            Log.getLog(getClass()).error("Error adding form field",e);
        }
        catch (FormDaoException e) {
            Log.getLog(getClass()).error(e.getMessage(),e);
        }
        catch (FormDocumentException e) {
            Log.getLog(getClass()).error(e.getMessage(),e);
        }

        return forward;
    }


    protected Forward activateForm() {
        FormModule module = (FormModule)Application.getInstance().getModule(FormModule.class);

        try {
            module.activateForm(getFormId());
            return new Forward("formConfigFinish");
        }
        catch (FormException e) {
            Log.getLog(getClass()).error("Error activate Form",e);
            return new Forward("failToActivate");
        }
    }

    protected void addOptions(String optionsStr,Element btn) {
        StringTokenizer tokenizer = new StringTokenizer(optionsStr, "\r\n");
        int counter = 0;
        while (tokenizer.hasMoreTokens()) {
            String line = tokenizer.nextToken();
            int ndx = line.indexOf('=');
            String code = null;
            String displayTag = null;
            if (ndx < 0) {
                code = line;
                displayTag = line;
            } else {
                code = line.substring(0, ndx);
                displayTag = line.substring(ndx + 1);
            }
            try {
                btn.addContent(new OptionElement(
                    counter++ + "",
                    code,
                    displayTag,
                    false, btn.getAttributeValue("type")));
            } catch (FormException e) {
                Log.getLog(getClass()).error(e.toString());
            }
            }
    }

    protected void addOptions(SequencedHashMap map,Element btn) {
        Set keySet = null;
        String key, value ;
        int counter = 0;

        keySet = map.keySet();
        for (Iterator iterator = keySet.iterator(); iterator.hasNext();) {
            key = String.valueOf(iterator.next());
            value = String.valueOf(map.get(key));

            try {
                btn.addContent(new OptionElement(
                    counter++ + "",
                    key,
                    value,
                    false, btn.getAttributeValue("type")));
            }
            catch (FormException e) {
                Log.getLog(getClass()).error(e.getMessage(),e);
            }
        }
    }

    protected void addRadioOptions(SequencedHashMap map, Element btn, String groupName) {
         Set keySet = null;
        String key, value ;
        int counter = 0;

        keySet = map.keySet();
        for (Iterator iterator = keySet.iterator(); iterator.hasNext();) {
            key = String.valueOf(iterator.next());
            value = String.valueOf(map.get(key));

            try {
                btn.addContent(new RadioOptionElement(
                    counter++ + "",
                    key,
                    value,
                    false, groupName));
            }
            catch (FormException e) {
                Log.getLog(getClass()).error(e.getMessage(),e);
            }
        }
    }



    protected void addRadioOptions(String optionsStr,Element btn, String groupName) {
        StringTokenizer tokenizer = new StringTokenizer(optionsStr, "\r\n");
        int counter = 0;
        while (tokenizer.hasMoreTokens()) {
            String line = tokenizer.nextToken();
            int ndx = line.indexOf('=');
            String code = null;
            String displayTag = null;
            if (ndx < 0) {
                code = line;
                displayTag = line;
            }
            else {
                code = line.substring(0, ndx);
                displayTag = line.substring(ndx + 1);
            }
            try {
                    btn.addContent(new RadioOptionElement(
                        counter++ + "",
                        code,
                        displayTag,
                        false,
                        groupName));
            }
            catch (FormException e) {
                Log.getLog(getClass()).error(e.toString());
            }
        }
    }


	public Forward addFormConfig(FormElement form)  {
        FormConfigDataObject  fcDO = new FormConfigDataObject();
        fcDO.setFormConfigId(UuidGenerator.getInstance().getUuid());
		fcDO.setFormId(getFormId());

        try{
            fcDO.setPreviewXml(form.display());
            FormModule handler = (FormModule)Application.getInstance().getModule(FormModule.class);

            handler.addFormConfig(fcDO,form);


            return new Forward("formConfigAdded");
        } catch (IOException e) {
            Log.getLog(getClass()).error(e.toString());
            return new Forward("formConfigFailed");
        }
        catch (FormException e) {
           Log.getLog(getClass()).error(e.getMessage(),e);
           return new Forward("formConfigFailed");
        }
        catch (FormDaoException e) {
           Log.getLog(getClass()).error(e.getMessage(),e);
           return new Forward("formConfigFailed");
        }
    }





    public SelectBox getFieldType() {
        return fieldType;
    }

    public Radio getHiddenYes() {
        return hiddenYes;
    }

    public Radio getHiddenNo() {
        return hiddenNo;
    }

    public Radio getRequiredYes() {
        return requiredYes;
    }

    public Radio getRequiredNo() {
        return requiredNo;
    }

    public TextField getFieldName() {
        return fieldName;
    }

    public TextField getMaxRows() {
        return maxRows;
    }

    public TextField getMaxCols() {
        return maxCols;
    }

    public String getFormId() {
        return formId;
    }

	public void setFormId(String formId) {
		   this.formId = formId;
	}

    public TextField getFieldSize() {
        return fieldSize;
    }

    public TextField getTfDefaultValue() {
        return tfDefaultValue;
    }

    public TextBox getOptions() {
        return options;
    }

    public SelectBox getTfDataType() {
        return tfDataType;
    }

    public Button getAdd() {
        return add;
    }

    public Button getFinish() {
        return finish;
    }


    public TextField getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(TextField maxLength) {
        this.maxLength = maxLength;
    }


    public CheckBox getIndependentField() {
        return independentField;
    }

    public void setIndependentField(CheckBox independentField) {
        this.independentField = independentField;
    }

    public String getDefaultTemplate() {
        return "formwizard/addFormField";
    }


    public RichTextBox getRtbLabel() {
        return rtbLabel;
    }

    public void setRtbLabel(RichTextBox rtbLabel) {
        this.rtbLabel = rtbLabel;
    }

    public TextField getTfColspan() {
        return tfColspan;
    }

    public void setTfColspan(TextField tfColspan) {
        this.tfColspan = tfColspan;
    }

    public TextField getTfRowspan() {
        return tfRowspan;
    }

    public void setTfRowspan(TextField tfRowspan) {
        this.tfRowspan = tfRowspan;
    }

    public SelectBox getSbAlign() {
        return sbAlign;
    }

    public void setSbAlign(SelectBox sbAlign) {
        this.sbAlign = sbAlign;
    }

    public SelectBox getSbValign() {
        return sbValign;
    }

    public void setSbValign(SelectBox sbValign) {
        this.sbValign = sbValign;
    }

    public CheckBox getCbResetLayout() {
        return cbResetLayout;
    }

    public void setCbResetLayout(CheckBox cbResetLayout) {
        this.cbResetLayout = cbResetLayout;
    }

    public TextField getTfTitle() {
        return tfTitle;
    }

    public void setTfTitle(TextField tfTitle) {
        this.tfTitle = tfTitle;
    }



    public Label getLbColumnLabel() {
        return lbColumnLabel;
    }

    public void setLbColumnLabel(Label lbColumnLabel) {
        this.lbColumnLabel = lbColumnLabel;
    }

    public String getFieldFormAbsoluteName() {
        return fieldFormAbsoluteName;
    }

    public void setFieldFormAbsoluteName(String fieldFormAbsoluteName) {
        this.fieldFormAbsoluteName = fieldFormAbsoluteName;
    }

}



