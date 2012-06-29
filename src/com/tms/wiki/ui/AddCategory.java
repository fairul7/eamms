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
public class AddCategory extends Form {

    private TextField newCategory;
    private Button add;
    private Button cancel;
    private String parentId;
    private String moduleId;

    public void init() {
        super.setColumns(2);
        super.init();
        setMethod("post");

        Application app = Application.getInstance();
        addChild(new Label("l1", "Category"));
        newCategory = new TextField("newCategory");
        newCategory.addChild(new ValidatorNotEmpty("nm", app.getMessage("wiki.label.category")));
        newCategory.setSize("30");
        addChild(newCategory);


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
        WikiCategory category = new WikiCategory();
        category.setCategoryId(UuidGenerator.getInstance().getUuid());
        category.setCategory((String) newCategory.getValue());
        category.setCreatedOn(new Date());
        category.setParentId(parentId);
        category.setModuleId(moduleId);

        try {
            WikiModule module = (WikiModule) Application.getInstance().getModule(WikiModule.class);
            module.addCategory(category);
            forward = new Forward("Add");
        } catch (Exception le) {
            forward = new Forward("Cancel");
        }
        return forward;

    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getModuleId() {
        return moduleId;
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }
}
