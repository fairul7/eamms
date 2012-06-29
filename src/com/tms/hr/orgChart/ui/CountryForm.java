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
 * Time: 10:43:33 AM
 */
public class CountryForm extends Form {
    public static final String FORWARD_SAVED = "saved";
    public static final String FORWARD_ERROR = "error";
    private TextField txtCountryCode;
    private TextField txtShortDesc;
    private TextField txtLongDesc;
    private CheckBox chkActive;

    private Button btnSave;
    private Button btnCancel;


    public void init(){
        Application app = Application.getInstance();
        setMethod("POST");
        setColumns(2);

        txtCountryCode = new TextField("txtCountryCode");
        txtCountryCode.setSize("25");
        txtCountryCode.addChild(new ValidateCountryCode("vdc", app.getMessage("orgChart.general.warn.codeKey")));
        txtShortDesc = new TextField("txtShortDesc");
        txtShortDesc.setSize("25");
        txtShortDesc.addChild(new ValidatorNotEmpty("shortDescVNE", app.getMessage("orgChart.general.warn.empty")));
        txtLongDesc = new TextField("txtLongDesc");
        txtLongDesc.setSize("80");
        chkActive = new CheckBox("chkActive");

        btnSave = new Button("btnSave", app.getMessage("general.label.save","Save"));
        btnCancel = new Button(Form.CANCEL_FORM_ACTION, app.getMessage("general.label.cancel", "Cancel"));

        Label lblCountryCode = new Label("lblCountryCode", "<span class=\"fieldTitle\">" + app.getMessage("orgChart.country.label.countryCode", "Country Code")+" *</span>");
        lblCountryCode.setAlign("right");
        addChild(lblCountryCode);
        addChild(txtCountryCode);
        
        Label lblShortDesc = new Label("lblShortDesc", "<span class=\"fieldTitle\">" + app.getMessage("orgChart.general.label.shortDesc", "Short Description")+" *</span>");
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
            OrgSetup country = new OrgSetup();
            country.setCode((String) txtCountryCode.getValue());
            country.setShortDesc((String) txtShortDesc.getValue());
            country.setLongDesc((String) txtLongDesc.getValue());
            country.setActive(chkActive.isChecked());
            oc.saveSetup(OrgChartHandler.TYPE_COUNTRY, country);

            //reset
            txtCountryCode.setValue("");
            txtShortDesc.setValue("");
            txtLongDesc.setValue("");
            chkActive.setChecked(false);
            return new Forward(FORWARD_SAVED);
        }else return new Forward(FORWARD_ERROR);

    }

    // make sure countryCode is unique
    private class ValidateCountryCode extends Validator {

        public ValidateCountryCode(String name, String text){
            super(name);
            setText(text);
        }

        public boolean validate(FormField ff) {
            String countryCode = (String) ff.getValue();
            if(countryCode == null || countryCode.equals("")){
                return false;
            }else{
                OrgChartHandler oc = (OrgChartHandler) Application.getInstance().getModule(OrgChartHandler.class);
                try {
                    return !oc.codeExist(OrgChartHandler.TYPE_COUNTRY,countryCode);
                } catch (DaoException e) {
                    return false;
                }
            }
        }
    }
}
