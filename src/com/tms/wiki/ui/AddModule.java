package com.tms.wiki.ui;

import kacang.stdui.*;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.Application;
import kacang.util.UuidGenerator;
import kacang.ui.Event;
import kacang.ui.Forward;

import javax.servlet.http.HttpServletRequest;

import com.tms.wiki.model.WikiCategory;
import com.tms.wiki.model.WikiModule;

import java.util.Date;

/**
 * Date: Dec 26, 2006
 * Time: 5:16:42 PM
 */
public class AddModule extends Form{

    private TextField newModule;
    private Button add;
    private Button cancel;
    private String moduleId;

    public void init() {
        super.setColumns(2);
        super.init();
        setMethod("post");

        Application app = Application.getInstance();
        addChild(new Label("l1", "New Module"));
        newModule = new TextField("newModule");
        newModule.addChild(new ValidatorNotEmpty("nm", app.getMessage("wiki.label.NewModule")));
        newModule.setSize("30");
        addChild(newModule);


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
            fwd = addModule(evt.getRequest());
        }
        return fwd;
    }

    protected Forward addModule(HttpServletRequest req) {
        Forward forward = null;
        WikiCategory category = new WikiCategory();
        category.setModuleId(UuidGenerator.getInstance().getUuid());
        category.setModuleName((String) newModule.getValue());
        category.setCreatedOn(new Date());        
        try {
            WikiModule module = (WikiModule) Application.getInstance().getModule(WikiModule.class);
            module.addModule(category);
            forward = new Forward("Add");
        } catch (Exception le) {
            forward = new Forward("Cancel");
        }
        return forward;
    }

}
