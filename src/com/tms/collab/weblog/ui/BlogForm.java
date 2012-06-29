package com.tms.collab.weblog.ui;

import kacang.stdui.*;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.ui.Forward;
import kacang.ui.Event;
import kacang.Application;

/**
 * Created by IntelliJ IDEA.
 * User: Disen
 * Date: Feb 25, 2004
 * Time: 11:49:25 AM
 * To change this template use Options | File Templates.
 */
public class BlogForm extends Form
{
    public static final String FORWARD_NAME_EXISTED = "name existed";
    public static final String FORWARD_BLOG_ADDED = "blog added";
    public static final String FORWARD_BLOG_UPDATED = "blog updated";
    public static final String FORWARD_CANCEL = "cancel";
    protected TextField title,nameTF;
    protected TextBox description;
    protected Radio allowComment;
    protected Button cancelButton;

    public BlogForm()
    {
    }

    public BlogForm(String s)
    {
        super(s);
    }

    public void init()
    {
        super.init();
        setMethod("POST");
        title = new TextField("title");
        title.addChild(new ValidatorNotEmpty("titlenotempty"));
        nameTF = new TextField("name");
        nameTF.setMaxlength("12");
        nameTF.addChild(new ValidatorNotEmpty("namenotempty"));
        description = new RichTextBox("description");
        description.addChild(new ValidatorNotEmpty("descnotempty"));
        allowComment = new Radio("allowComment");
        cancelButton = new Button("cancelButton",Application.getInstance().getMessage("weblog.label.cancel","Cancel"));
        addChild(cancelButton);
        addChild(title);
        addChild(description);
        addChild(allowComment);
        addChild(nameTF);
    }

    public Forward onSubmit(Event event)
    {
        if(cancelButton.getAbsoluteName().equals(findButtonClicked(event)))
            return new Forward(FORWARD_CANCEL);
        return super.onSubmit(event);
    }

    public TextField getTitle()
    {
        return title;
    }

    public void setTitle(TextField title)
    {
        this.title = title;
    }

    public TextBox getDescription()
    {
        return description;
    }

    public void setDescription(TextBox description)
    {
        this.description = description;
    }

    public TextField getNameTF()
    {
        return nameTF;
    }

    public void setNameTF(TextField name)
    {
        this.nameTF = name;
    }

    public Radio getAllowComment()
    {
        return allowComment;
    }

    public void setAllowComment(Radio allowComment)
    {
        this.allowComment = allowComment;
    }

    public Button getCancelButton()
    {
        return cancelButton;
    }

    public void setCancelButton(Button cancelButton)
    {
        this.cancelButton = cancelButton;
    }

}
