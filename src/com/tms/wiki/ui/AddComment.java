package com.tms.wiki.ui;

import kacang.stdui.*;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.Application;
import com.tms.wiki.stdui.WikiEditor;

/**
 * Created by IntelliJ IDEA.
 * User: hima
 * Date: Dec 14, 2006
 * Time: 2:11:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class AddComment extends Form{

    private TextField name;
    private TextField email;
    private TextField comments;
    private Button save;

    public void init() {
        super.setColumns(2);
        super.init();
        setMethod("post");

        addChild(new Label("l1", "Name"));
        name = new TextField("name");
        name.addChild(new ValidatorNotEmpty("nm", Application.getInstance().getMessage("wiki.label.Name")));
        addChild(name);

        addChild(new Label("l2", "Email"));
        email = new TextField("email");
        email.addChild(new ValidatorNotEmpty("nm", Application.getInstance().getMessage("wiki.label.Email")));
        addChild(email);

        addChild(new Label("l3", "Comments"));
        comments = new WikiEditor("comments");
        addChild(comments);

        save = new Button("save", "Save");
        addChild(save);
    }

    public void onRequest(Event event) {
        super.onRequest(event);
    }

    public Forward onValidate(Event event) {
        return super.onValidate(event);        
    }

    public String getDefaultTemplate() {
        return "wiki/addComment";
    }
}
