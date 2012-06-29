package com.tms.wiki.ui;

import com.tms.wiki.model.WikiCategory;
import com.tms.wiki.model.WikiModule;
import kacang.Application;
import kacang.stdui.*;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.UuidGenerator;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * Date: Dec 22, 2006
 * Time: 10:41:35 AM
 */
public class AddSubCategory extends Form {
    private TextField subCategory;
    private Button add;
    private Button cancel;

    public void init() {
        super.setColumns(2);
        super.init();
        setMethod("post");

        Application app = Application.getInstance();
        addChild(new Label("l1", "SubCategory"));
        subCategory = new TextField("SubCategory");
        subCategory.addChild(new ValidatorNotEmpty("nm", app.getMessage("wiki.label.NewSubCategory")));
        addChild(subCategory);


        add = new Button("add", "Add");
        cancel = new Button("cancel", "Cancel");
        Panel panel = new Panel("panel");
        panel.addChild(add);
        panel.addChild(cancel);
        addChild(new Label("l2"));
        addChild(panel);
    }

    public void onRequest(Event event) {
        super.onRequest(event);
        removeChildren();
        init();
    }

    public Forward onSubmit(Event event) {
        Forward fwd = super.onSubmit(event);
        String buttonName = findButtonClicked(event);
        if (buttonName != null && cancel.getAbsoluteName().equals(buttonName)) {
            fwd = new Forward(Form.CANCEL_FORM_ACTION);
            return fwd;
        }
        return fwd;
    }

    public Forward onValidate(Event evt) {
        Forward fwd = null;
        fwd = super.onValidate(evt);
        String buttonName = findButtonClicked(evt);
        if (buttonName != null && add.getAbsoluteName().equals(buttonName)) {
            fwd = addCategory(evt.getRequest());
        }
        return fwd;
    }

    protected Forward addCategory(HttpServletRequest req) {
        Forward forward = null;
        WikiCategory subCategoryDO = new WikiCategory();
        subCategoryDO.setSubCategoryId(UuidGenerator.getInstance().getUuid());
        subCategoryDO.setCategory((String) subCategory.getValue());
        subCategoryDO.setCreatedOn(new Date());

        try {
            WikiModule module = (WikiModule) Application.getInstance().getModule(WikiModule.class);
            module.addSubCategory(subCategoryDO);
            forward = new Forward("Add");
        } catch (Exception le) {
            forward = new Forward("Cancel");
        }
        return forward;
    }


}
