package com.tms.collab.formwizard.ui;

import kacang.stdui.*;
import kacang.ui.Forward;
import kacang.ui.Event;
import kacang.Application;
import kacang.model.DefaultDataObject;
import kacang.util.Log;
import com.tms.collab.formwizard.grid.G2Field;
import com.tms.collab.formwizard.grid.G2Column;
import com.tms.collab.formwizard.model.FormDao;
import com.tms.collab.formwizard.model.FormModule;
import com.tms.collab.formwizard.model.FormDaoException;
import com.tms.collab.formwizard.model.Util;
import com.tms.collab.formwizard.xmlwidget.FormElement;

import java.util.*;

import org.apache.commons.collections.SequencedHashMap;
import org.jdom.Element;

public class AddG2FieldForm extends Form {
    private List columnList = new ArrayList();
    private Map idMap = new HashMap();
    private Map idSequenceMap = new HashMap();

    private TextField tfId;
    private TextField tfHeader;
    private SelectBox sbType;
    private TextField tfFormula;
    private CheckBox cbTotal;
    private SelectBox sbValidation;
    private TextBox tbItems;
    private TextField tfTitle;
    private Button btAddColumn;
    private Button btUpdateColumn;
    private String columnId;
    private Label lbColumnLabel;
    private boolean tableGridDataEmpty;
    private Button btReturnToMainForm;

    protected String initWidgetId;
    private String formId;
    private String formUid;
    private String formTemplateId;


    public AddG2FieldForm() {
        super();
    }

    public AddG2FieldForm(String name) {
        super(name);
    }

    public void onRequest(Event event) {
        setIdMap();
        initilizeFields();
        isTableGridDataEmpty();
    }

    public void init() {
        setMethod("post");
        Map optionMap = new SequencedHashMap();
        Map validationMap = new SequencedHashMap();

        tfTitle = new TextField("title");
        addChild(tfTitle);

        tfId = new TextField("Id");
        addChild(tfId);

        tfHeader = new TextField("header");
        addChild(tfHeader);

        optionMap.put(G2Column.TYPE_TEXT, Application.getInstance().getMessage("formWizard.label.addG2FieldForm.text","Text"));
        optionMap.put(G2Column.TYPE_FORMULA, Application.getInstance().getMessage("formWizard.label.addG2FieldForm.formula","Formula"));
        optionMap.put(G2Column.TYPE_DROP_DOWN, Application.getInstance().getMessage("formWizard.label.addG2FieldForm.selectBox","Select Box"));
        sbType = new SelectBox("type");
        sbType.setOptionMap(optionMap);
        sbType.setOnChange("selectType()");
        addChild(sbType);

        cbTotal = new CheckBox("total");
        addChild(cbTotal);

        validationMap.put("-1", Application.getInstance().getMessage("formWizard.label.addG2FieldForm.pleaseSelect","Please select"));
        validationMap.put(G2Column.VALIDATE_REQUIRED,Application.getInstance().getMessage("formWizard.label.addG2FieldForm.requiredColumn","Required Column"));
        validationMap.put(G2Column.VALIDATE_NUMBER, Application.getInstance().getMessage("formWizard.label.addG2FieldForm.number","Number"));
        sbValidation = new SelectBox("validation");
        sbValidation.setOptionMap(validationMap);
        addChild(sbValidation);


        tfFormula = new TextField("formula");
        addChild(tfFormula);

        tbItems = new TextBox("item");
        tbItems.setRows("10");
        tbItems.setCols("25");
        addChild(tbItems);

        btAddColumn = new Button("addColumn");
        btAddColumn.setText(Application.getInstance().getMessage("formWizard.label.addG2FieldForm.addColumn","Add Column"));
        addChild(btAddColumn);

        btUpdateColumn = new Button("updateColumn");
        btUpdateColumn.setText(Application.getInstance().getMessage("formWizard.label.addG2FieldForm.updateColumn","Update Column"));
        addChild(btUpdateColumn);

        lbColumnLabel = new Label("columnLabel");
        addChild(lbColumnLabel);

        btReturnToMainForm = new Button("returnToMainForm");
        btReturnToMainForm.setText(Application.getInstance().getMessage("formWizard.label.addG2FieldForm.ReturnMain","Return To Main Form"));
        btReturnToMainForm.setOnClick("doDone()");
        addChild(btReturnToMainForm);
    }

    public void initilizeFields() {
        tfId.setValue("");
        tfHeader.setValue("");
        tfFormula.setValue("");
        tbItems.setValue("");
        cbTotal.setChecked(false);
        sbType.setSelectedOption(G2Column.TYPE_TEXT);
        sbValidation.setSelectedOption("-1");

        tfTitle.setInvalid(false);
        tfTitle.setMessage("");

        tfId.setInvalid(false);
        tfId.setMessage("");

        tfHeader.setInvalid(false);
        tfHeader.setMessage("");

        tfFormula.setInvalid(false);
        tfFormula.setMessage("");

        tbItems.setInvalid(false);
        tbItems.setMessage("");

        lbColumnLabel.setText("");

        setInvalid(false);

    }

    public void initField(Element element) {
        initilizeFields();
        G2Field gridField = null;

        tfTitle.setValue(element.getAttributeValue("title"));       
        gridField = new G2Field(tfTitle.getValue().toString());
        gridField.setColumnListXml(element.getAttributeValue("columnListXml"));
        columnList = gridField.getColumnList();
        setIdMap();

    }

     public Forward onSubmit(Event evt) {
        Forward result = new Forward();
        result = super.onSubmit(evt);
        tfTitle.setInvalid(false);
        tfTitle.setMessage("");

        tfId.setInvalid(false);
        tfId.setMessage("");

        tfHeader.setInvalid(false);
        tfHeader.setMessage("");

        tfFormula.setInvalid(false);
        tfFormula.setMessage("");

        tbItems.setInvalid(false);
        tbItems.setMessage("");

        lbColumnLabel.setText("");

        setInvalid(false);
        return result;
     }

    public Forward actionPerformed(Event evt) {
        super.actionPerformed(evt);

        if (evt.getRequest().getParameter("columnId") != null && "editColumn".equals(evt.getType())) {
            populateColumn(evt.getRequest().getParameter("columnId"));
        }
        else if (evt.getRequest().getParameter("columnId") != null && "deleteColumn".equals(evt.getType())) {
            deleteColumn(evt.getRequest().getParameter("columnId"));
        }

        return null;
    }

    public Forward onValidate(Event evt){
        String buttonName = findButtonClicked(evt);




        if (buttonName != null) {
            if (btReturnToMainForm.getAbsoluteName().equals(buttonName) )
                return null;
            if (btUpdateColumn.getAbsoluteName().equals(buttonName))
                idMap.remove(evt.getRequest().getParameter("columnId"));
            verifyField();
            if (isInvalid())
                return null;
            if (btAddColumn.getAbsoluteName().equals(buttonName)) {
                addColumn();
            }
            else if (btUpdateColumn.getAbsoluteName().equals(buttonName)) {
                updateColumn(evt.getRequest().getParameter("columnId"));
            }
        }
        return null;
    }

    protected void addColumn() {
        G2Column column = null;
        column = getColumn();
        columnList.add(column);
        initilizeFields();
    }

    protected void updateColumn(String columnId) {
        G2Column column = null;
        column = getColumn();
        int index = getColumnIndex(columnId);
        columnList.set(index,column);
        initilizeFields();
    }

    protected void populateColumn(String columnId) {
        G2Column g2Column = null;
        Collection options = null;
        String option = "", value = "";

        initilizeFields();
        for (Iterator iterator = columnList.iterator(); iterator.hasNext();) {
            g2Column = (G2Column) iterator.next();
            if (columnId.equals(g2Column.getName())) {
                tfId.setValue(g2Column.getName());
                tfHeader.setValue(g2Column.getHeader());
                sbType.setSelectedOption(g2Column.getType());
                sbValidation.setSelectedOption(g2Column.getValidation());

                if (g2Column.getFormula() != null && !g2Column.getFormula().equals(""))
                    tfFormula.setValue(g2Column.getFormula());

                if (g2Column.getItemMap() != null && !g2Column.getItemMap().isEmpty()) {
                    options = g2Column.getItemMap().values();
                    for (Iterator iterator1 = options.iterator(); iterator1.hasNext();) {
                        value = String.valueOf(iterator1.next());
                        option += value + "\r\n";
                    }

                    tbItems.setValue(option);
                }

                if (g2Column.isColumnTotal())
                    cbTotal.setChecked(true);




            }
        }
    }

    protected int getColumnIndex(String columnId) {
        G2Column g2Column = null;
        int index = 0, i = 0;
        for (Iterator iterator = columnList.iterator(); iterator.hasNext();i++) {
            g2Column = (G2Column) iterator.next();
            if (columnId.equals(g2Column.getName())) {
                idMap.remove(g2Column.getName());
                index = i;
            }
        }
        return index;
    }

    protected void deleteColumn(String columnId) {
        int index = getColumnIndex(columnId);
        columnList.remove(index);
        initilizeFields();
    }

    protected G2Column getColumn() {
        String selectedType =  String.valueOf(sbType.getSelectedOptions().keySet().iterator().next());
        String validationType = String.valueOf(sbValidation.getSelectedOptions().keySet().iterator().next());

        G2Column column = null;
        String optionsStr = "";
        StringTokenizer tokenizer = null;
        String token = null;
        SequencedHashMap map = null;

        idMap.put(tfId.getValue().toString().trim(),null);



        column = new G2Column(tfId.getValue().toString().trim(),tfHeader.getValue().toString(), selectedType);
        if (!"-1".equals(validationType)) {
            column.setValidation(validationType);
        }




        if (G2Column.TYPE_FORMULA.equals(selectedType))
           column.setFormula(tfFormula.getValue().toString());

        if (G2Column.TYPE_DROP_DOWN.equals(selectedType)) {
            map = new SequencedHashMap();
            optionsStr = tbItems.getValue().toString();
            tokenizer = new StringTokenizer(optionsStr, "\r\n");
            while (tokenizer.hasMoreTokens()) {
                token = tokenizer.nextToken();
                map.put(token,token);
            }
            column.setItemMap(map);
        }

        if (cbTotal.isChecked()) {
            column.setColumnTotal(true);
        }

        return column;

    }

    protected void verifyField() {


        if (tfId.getValue() == null || tfId.getValue().toString().equals("")) {
            tfId.setInvalid(true);
            tfId.setMessage(Application.getInstance().getMessage("formWizard.label.addG2FieldForm.idNotEmpty","Id cannot be empty"));
            setInvalid(true);
        }
        else if (isNumeric(tfId.getValue().toString())) {
            tfId.setInvalid(true);
            tfId.setMessage(Application.getInstance().getMessage("formWizard.label.addG2FieldForm.idNotNumeric","Id cannot be numeric"));
            setInvalid(true);

        }
        else if (idMap.containsKey(tfId.getValue().toString().trim())){
            tfId.setInvalid(true);
            tfId.setMessage(Application.getInstance().getMessage("formWizard.label.addG2FieldForm.idUnique","Id must be unique"));
            setInvalid(true);
        }

        if (tfHeader.getValue() == null || tfHeader.getValue().toString().equals("")) {
            tfHeader.setInvalid(true);
            tfHeader.setMessage(Application.getInstance().getMessage("formWizard.label.addG2FieldForm.columnHeaderNotEmpty","Column header cannot be empty"));
            setInvalid(true);
        }

        String selectedType =  String.valueOf(sbType.getSelectedOptions().keySet().iterator().next());

        if (G2Column.TYPE_FORMULA.equals(selectedType)
            && tfFormula.getValue() != null
            && tfFormula.getValue().toString().equals("")) {
            tfFormula.setInvalid(true);
            tfFormula.setMessage(Application.getInstance().getMessage("formWizard.label.addG2FieldForm.formulaNotEmpty","Formula cannot be empty"));
            setInvalid(true);
        }
        else if (G2Column.TYPE_DROP_DOWN.equals(selectedType)
            && tbItems.getValue() != null
            && tbItems.getValue().toString().equals("")) {
            tbItems.setInvalid(true);
            tbItems.setMessage(Application.getInstance().getMessage("formWizard.label.addG2FieldForm.optionsNotEmpty","Options cannot be empty"));
            setInvalid(true);
        }
    }

    public void setTfTitleInvalid() {
        tfTitle.setInvalid(true);
        tfTitle.setMessage(Application.getInstance().getMessage("formWizard.label.addG2FieldForm.titleNotEmpty","Title cannot be empty"));
        setInvalid(true);

    }

    public void setLbColumnLabel(String labelText, boolean invalid) {
        lbColumnLabel.setText(labelText);
        setInvalid(invalid);
    }

    public void isTableGridDataEmpty() {
        Collection data = null;
        FormDao dao = null;
        String tableName = "";
        FormElement form = null;

        if(getFormId()!=null) {
            form = Util.getFormElement(getFormId());
        }

        if (form != null)
            tableName = form.getAttributeValue("name");

        dao = (FormDao) Application.getInstance().getModule(FormModule.class).getDao();

        try {
            if (getFormUid() == null) {
                tableGridDataEmpty = true;
            }
            else if (getFormTemplateId() != null) {
                data = dao.getFormTemplateField(getFormTemplateId());
                if (data == null || data.size() == 0 || data.isEmpty())
                    tableGridDataEmpty = true;
                else
                    tableGridDataEmpty = false;
            }
            else {
                try {
                    data = dao.getSubmittedFormData(tableName,getFormUid());
                } catch(FormDaoException e) {
                    // just ignore
                }
                if (data == null || data.size() == 0 || data.isEmpty())
                    tableGridDataEmpty = true;
                else
                    tableGridDataEmpty = false;

                DefaultDataObject ddo = null;
                String str = "";
                if (!tableGridDataEmpty) {
                    tableGridDataEmpty = true;
                    for (Iterator iterator = data.iterator(); iterator.hasNext();) {
                        ddo =  (DefaultDataObject)iterator.next();
                        if (ddo.getProperty(getFormUid()) != null) {
                            str = (String)ddo.getProperty(getFormUid());
                            if (!str.equals(""))  {
                                tableGridDataEmpty = false;
                                break;
                            }
                        }

                    }
                }
            }
        }
        catch (FormDaoException e) {
            Log.getLog(getClass()).error(e.getMessage(),e);
            tableGridDataEmpty = false;
        }

    }

    public boolean isNumeric(String str) {

        try {
            Integer.parseInt(str);
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

    public void setIdMap() {
        G2Column g2Column = null;
        int cnt = 0;
        for (Iterator iterator = columnList.iterator(); iterator.hasNext(); cnt++) {
            g2Column = (G2Column) iterator.next();
            idMap.put(g2Column.getName(),null);
            idSequenceMap.put(g2Column.getName(),String.valueOf(cnt));
        }
    }

    public String getDefaultTemplate() {
        return "formwizard/addG2FieldForm";
    }





    // === [ getters/setters ] =========================================================================================
    public List getColumnList() {
        return columnList;
    }

    public void setColumnList(List columnList) {
        this.columnList = columnList;
    }

    public TextField getTfId() {
        return tfId;
    }

    public void setTfId(TextField tfId) {
        this.tfId = tfId;
    }

    public TextField getTfHeader() {
        return tfHeader;
    }

    public void setTfHeader(TextField tfHeader) {
        this.tfHeader = tfHeader;
    }

    public SelectBox getSbType() {
        return sbType;
    }

    public void setSbType(SelectBox sbType) {
        this.sbType = sbType;
    }

    public TextField getTfFormula() {
        return tfFormula;
    }

    public void setTfFormula(TextField tfFormula) {
        this.tfFormula = tfFormula;
    }

    public CheckBox getCbTotal() {
        return cbTotal;
    }

    public void setCbTotal(CheckBox cbTotal) {
        this.cbTotal = cbTotal;
    }

    public SelectBox getSbValidation() {
        return sbValidation;
    }

    public void setSbValidation(SelectBox sbValidation) {
        this.sbValidation = sbValidation;
    }

    public TextBox getTbItems() {
        return tbItems;
    }

    public void setTbItems(TextBox tbItems) {
        this.tbItems = tbItems;
    }

    public TextField getTfTitle() {
        return tfTitle;
    }

    public void setTfTitle(TextField tfTitle) {
        this.tfTitle = tfTitle;
    }


    public Button getBtAddColumn() {
        return btAddColumn;
    }

    public void setBtAddColumn(Button btAddColumn) {
        this.btAddColumn = btAddColumn;
    }

    public Map getIdMap() {
        return idMap;
    }

    public void setIdMap(Map idMap) {
        this.idMap = idMap;
    }

    public Button getBtUpdateColumn() {
        return btUpdateColumn;
    }

    public void setBtUpdateColumn(Button btUpdateColumn) {
        this.btUpdateColumn = btUpdateColumn;
    }

    public String getColumnId() {
        return columnId;
    }

    public void setColumnId(String columnId) {
        this.columnId = columnId;
    }

    public Label getLbColumnLabel() {
        return lbColumnLabel;
    }

    public void setLbColumnLabel(Label lbColumnLabel) {
        this.lbColumnLabel = lbColumnLabel;
    }

    public int getColumnListSize() {
        return columnList.size();
    }

    public boolean getTableGridDataEmpty() {
        return tableGridDataEmpty;
    }

    public void setTableGridDataEmpty(boolean tableGridDataEmpty) {
        this.tableGridDataEmpty = tableGridDataEmpty;
    }

    public Button getBtReturnToMainForm() {
        return btReturnToMainForm;
    }

    public void setBtReturnToMainForm(Button btReturnToMainForm) {
        this.btReturnToMainForm = btReturnToMainForm;
    }

    public String getInitWidgetId() {
        return initWidgetId;
    }

    public void setInitWidgetId(String initWidgetId) {
            G2Field grid = (G2Field) getWidgetManager().getWidget(initWidgetId);
            if(grid != null)
                columnList = grid.getColumnList();
    }

    public Map getIdSequenceMap() {
        return idSequenceMap;
    }

    public void setIdSequenceMap(Map idSequenceMap) {
        this.idSequenceMap = idSequenceMap;
    }

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    public String getFormUid() {
        return formUid;
    }

    public void setFormUid(String formUid) {
        this.formUid = formUid;
    }

    public String getFormTemplateId() {
        return formTemplateId;
    }

    public void setFormTemplateId(String formTemplateId) {
        this.formTemplateId = formTemplateId;
    }


}
