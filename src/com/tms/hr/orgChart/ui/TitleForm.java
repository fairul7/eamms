package com.tms.hr.orgChart.ui;

import kacang.stdui.*;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.stdui.validator.Validator;
import kacang.Application;
import kacang.model.DaoException;
import kacang.ui.Forward;
import kacang.ui.Event;
import com.tms.hr.orgChart.model.OrgChartHandler;
import com.tms.hr.orgChart.model.OrgSetup;

/**
 * Created by IntelliJ IDEA.
 * User: blake
 * Date: Mar 16, 2006
 * Time: 12:13:01 PM
 */
public class TitleForm extends Form {
    public static final String FORWARD_SAVED = "saved";
    public static final String FORWARD_ERROR = "error";
    private TextField txtTitleCode;
    private TextField txtShortDesc;
    private TextField txtLongDesc;
    private CheckBox chkActive;

    private Button btnSave;
    private Button btnCancel;


    public void init(){
        Application app = Application.getInstance();
        setMethod("POST");
        setColumns(2);

        txtTitleCode = new TextField("txtTitleCode");
        txtTitleCode.setSize("25");
        txtTitleCode.addChild(new ValidateTitleCode("vdc", app.getMessage("orgChart.general.warn.codeKey")));
        txtShortDesc = new TextField("txtShortDesc");
        txtShortDesc.setSize("25");
        txtShortDesc.addChild(new ValidatorNotEmpty("shortDescVNE", app.getMessage("orgChart.general.warn.empty")));
        txtLongDesc = new TextField("txtLongDesc");
        txtLongDesc.setSize("80");
        chkActive = new CheckBox("chkActive");

        btnSave = new Button("btnSave", app.getMessage("general.label.save","Save"));
        btnCancel = new Button(Form.CANCEL_FORM_ACTION, app.getMessage("general.label.cancel", "Cancel"));

        Label lblTitleCode = new Label("lblTitleCode", "<span class=\"fieldTitle\">" + app.getMessage("orgChart.title.label.titleCode", "Title Code")+" *" + "</span>");
        lblTitleCode.setAlign("right");
        addChild(lblTitleCode);
        addChild(txtTitleCode);
        
        Label lblShortDesc = new Label("lblShortDesc", "<span class=\"fieldTitle\">" + app.getMessage("orgChart.general.label.shortDesc", "Short Description")+" *" + "</span>");
        lblShortDesc.setAlign("right");
        addChild(lblShortDesc);
        addChild(txtShortDesc);
        
        Label lblLongDesc = new Label("lblLongDesc", "<span class=\"fieldTitle\">" + app.getMessage("orgChart.general.label.longDesc", "Long Description") + "</span>");
        lblLongDesc.setAlign("right");
        addChild(lblLongDesc);
        addChild(txtLongDesc);
        
        Label lblActivate = new Label("lblActivate", "<span class=\"fieldTitle\">" + app.getMessage("orgChart.general.label.activate", "Activate") + "</span>");
        lblActivate.setAlign("right");
        addChild(lblActivate);
        addChild(chkActive);
        
        Panel panel = new Panel("btnPanel");
        panel.setColspan(2);
        panel.setAlign(Panel.ALIGH_MIDDLE);
        panel.addChild(btnSave);
        panel.addChild(btnCancel);
        addChild(panel);
    }

    public Forward onValidate(Event evt) {
        String action = findButtonClicked(evt);
        if(action.equals(btnSave.getAbsoluteName())){
            OrgChartHandler oc = (OrgChartHandler) Application.getInstance().getModule(OrgChartHandler.class);
            OrgSetup title = new OrgSetup();
            title.setCode((String) txtTitleCode.getValue());
            title.setShortDesc((String) txtShortDesc.getValue());
            title.setLongDesc((String) txtLongDesc.getValue());
            title.setActive(chkActive.isChecked());
            oc.saveSetup(OrgChartHandler.TYPE_TITLE, title);

            //reset
            txtTitleCode.setValue("");
            txtShortDesc.setValue("");
            txtLongDesc.setValue("");
            chkActive.setChecked(false);
            return new Forward(FORWARD_SAVED);
        }else return new Forward(FORWARD_ERROR);

    }

    // make sure titleCode is unique
    private class ValidateTitleCode extends Validator {

        public ValidateTitleCode(String name, String text){
            super(name);
            setText(text);
        }

        public boolean validate(FormField ff) {
            String titleCode = (String) ff.getValue();
            if(titleCode == null || titleCode.equals("")){
                return false;
            }else{
                OrgChartHandler oc = (OrgChartHandler) Application.getInstance().getModule(OrgChartHandler.class);
                try {
                    return !oc.codeExist(OrgChartHandler.TYPE_TITLE,titleCode);
                } catch (DaoException e) {
                    return false;
                }
            }
        }
    }
}
