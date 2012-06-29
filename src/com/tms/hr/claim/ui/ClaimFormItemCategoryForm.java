/**
 * @author Vincent Lee, Wavelet Solutions Sdn Bhd (632468-W)
 * Created On: 2004-07-08
 * Copyright of The Media Shoppe Berhad
 */
package com.tms.hr.claim.ui;

import com.tms.hr.claim.model.ClaimConfigTypeModule;
import com.tms.hr.claim.model.ClaimFormItemCategory;
import com.tms.hr.claim.model.ClaimFormItemCategoryModule;
import com.tms.hr.claim.model.ClaimTypeObject;

import kacang.Application;

import kacang.stdui.*;

import kacang.stdui.validator.ValidatorNotEmpty;

import kacang.ui.Event;
import kacang.ui.Forward;

import kacang.util.UuidGenerator;

import org.apache.commons.collections.SequencedHashMap;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;


public class ClaimFormItemCategoryForm extends Form {
    protected TextField tf_Code;
    protected TextField tf_Name;
    protected TextField tf_Description;

    //    protected DateField df_TimeEdit ;
    protected SelectBox sb_Category;
    protected SelectBox sb_Type;

    //    protected SelectBox sb_State;
    //   protected SelectBox sb_Status;
	protected Panel buttonPanel;
    protected Button bn_Submit;
	protected Button bn_Cancel;

	public static final String FORWARD_CANCEL = "cancel";

	
	public String getDefaultTemplate() {
		return "claims/config_category";
	}
	
	
    public void init() {
        setColumns(2);
        setMethod("POST");

        ValidatorNotEmpty vne1 = new ValidatorNotEmpty("vne1", "Must not be empty");
        addChild(new Label("l2",
                "<b>" +
                Application.getInstance().getMessage("claims.category.name",
                    "Name") +" *"+ "</b>"));
        tf_Name = new TextField("tf_Name");
        tf_Name.addChild(vne1);
        addChild(tf_Name);

        addChild(new Label("l3",
                "<b>" +
                Application.getInstance().getMessage("claims.category.description",
                    "Description") +" *"+ "</b>"));
        tf_Description = new TextField("tf_Description");
        ValidatorNotEmpty vne2 = new ValidatorNotEmpty("vne2", "Must not be empty");
        tf_Description.addChild(vne2);
        addChild(tf_Description);

        addChild(new Label("l1",
                "<b>" +
                Application.getInstance().getMessage("claims.category.code",
                    "Account Code") + "</b>"));
        tf_Code = new TextField("tf_Code");
        addChild(tf_Code);

        //		addChild(new Label("l4","Last Edit Time"));
        //		df_TimeEdit = new DateField("df_TimeEdit");
        //		addChild(df_TimeEdit);

        /*
                        addChild(new Label("l5", "State"));
                        sb_State = new SelectBox("sb_State");
                        sb_State.addOption("act","Active");
                        sb_State.addOption("ina","In-active");
                        sb_State.addOption("del","Deleted");
                        addChild(sb_State);

                        addChild(new Label("l6", "Status"));
                        sb_Status = new SelectBox("sb_Status");
                        sb_Status.addOption("act","Active");
                        sb_Status.addOption("ina","In-active");
                        sb_Status.addOption("del","Deleted");
                        addChild(sb_Status);
        */

        //retrieve type from table
        Application app = Application.getInstance();
        Collection allTypeCol = null;
        Map allTypeMap = new SequencedHashMap();
        ClaimConfigTypeModule module = (ClaimConfigTypeModule) app.getModule(ClaimConfigTypeModule.class);
        allTypeCol = module.retrieveAllType();
        allTypeMap.put("default", "Default");
        allTypeMap.put("travel", "Travel");

        for (Iterator iterator = allTypeCol.iterator(); iterator.hasNext();) {
            ClaimTypeObject object = (ClaimTypeObject) iterator.next();
            allTypeMap.put(object.getId(), object.getTypeName());
        }

        //addChild(new Label("12", "<b>" + "Type" + "</b>"));
        sb_Type = new SelectBox("sb_type");

        if (allTypeMap != null) {
            sb_Type.setOptionMap(allTypeMap);
        }

        /*sb_Type.addOption("travel","Travel");
        sb_Type.addOption("default","Default");
        sb_Type.setSelectedOption("default");*/
        // addChild(sb_Type);
        addChild(new Label("a", ""));
		if (isEditMode()) {
			buttonPanel = new Panel("buttonPanel");
			bn_Submit = new Button("submit", Application.getInstance().getMessage("claims.categoty.update","Update"));
			bn_Cancel = new Button("cancel", Application.getInstance().getMessage("claims.category.cancel","Cancel"));
			buttonPanel.addChild(bn_Submit);
			buttonPanel.addChild(bn_Cancel);
			addChild(buttonPanel);
		} else {
        	bn_Submit = new Button("submit", Application.getInstance().getMessage("claims.category.submit", "Submit"));
			addChild(bn_Submit);
		}

    }

    public Forward onValidate(Event evt) {
        Application application = Application.getInstance();
        ClaimFormItemCategoryModule module = (ClaimFormItemCategoryModule) application.getModule(ClaimFormItemCategoryModule.class);

        String userId = getWidgetManager().getUser().getId();

        ClaimFormItemCategory obj = new ClaimFormItemCategory();
        UuidGenerator uuid = UuidGenerator.getInstance();
        obj.setId(uuid.getUuid());
        obj.setCode((String) tf_Code.getValue());
        obj.setName((String) tf_Name.getValue());

        obj.setDescription((String) tf_Description.getValue());
        obj.setUserEdit(userId);
        obj.setTimeEdit(new Date());

        //		obj.setTimeEdit(df_TimeEdit.getDate());
        /*        obj.setType((String) sb_Type.getSelectedOptions().keySet().iterator()
                                            .next());*/
        obj.setType("default");

        obj.setState(ClaimFormItemCategoryModule.STATE_ACTIVE);
        obj.setStatus(ClaimFormItemCategoryModule.STATUS_ACTIVE);
        module.addObject(obj);
        removeChildren();
        init();

        return new Forward("submit");
        //return super.onValidate(evt);
    }

	public boolean isEditMode() {
		return false;
	}
}
