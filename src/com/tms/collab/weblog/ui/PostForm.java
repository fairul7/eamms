package com.tms.collab.weblog.ui;

import com.tms.collab.weblog.model.Blog;
import com.tms.collab.weblog.model.WeblogModule;
import kacang.stdui.*;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.Application;
import kacang.util.Log;
import kacang.model.DaoException;

/**
 * Created by IntelliJ IDEA.
 * User: Disen
 * Date: Feb 26, 2004
 * Time: 7:00:10 PM
 * To change this template use Options | File Templates.
 */
public class PostForm extends Form
{
    public static final String FORWARD_ACCESS_DENIED = "access denied";
    public static final String FORWARD_POST_ADDED = "post added";
    public static final String FORWARD_POST_UPDATED = "post updated";
    public static final String FORWARD_POST_UPDATE_FAILED = "post update failed";
    public static final String FORWARD_POST_CANCEL = "cancel";

    protected String blogId;
    protected Blog blog;
    protected TextField titleTF;
    protected RichTextBox contentTB;
    protected Button saveAsDraftButton,cancelButton;
    protected DateField publishDate;
    protected BlogTimeField publishTime;
    protected Button postButton;

    public PostForm()
    {
    }

    public PostForm(String s)
    {
        super(s);
    }

    public void init()
    {
        super.init();
        setMethod("POST");        
        titleTF = new TextField("title");
        titleTF.addChild(new ValidatorNotEmpty("titlenotempty"));
        contentTB = new RichTextBox("content");
        contentTB.addChild(new ValidatorNotEmpty("contentnotempty"));
        publishDate = new DateField("publishdate");
        publishTime = new BlogTimeField("publishTime");
        saveAsDraftButton = new Button("saveasdraftbutton",Application.getInstance().getMessage("weblog.label.saveasDraft","Save as Draft"));
        postButton = new Button("postButton",Application.getInstance().getMessage("weblog.label.posttoWeblog","Post to Weblog"));
        cancelButton = new Button("cancelButton",Application.getInstance().getMessage("weblog.label.cancel","Cancel"));
        addChild(cancelButton);
        addChild(postButton);
        addChild(saveAsDraftButton);
        addChild(publishDate);
        addChild(publishTime);
        addChild(titleTF);
        addChild(contentTB);
    }

    public void onRequest(Event event)
    {
        if(blogId!=null&&!blogId.equals("")){
            WeblogModule wm = (WeblogModule) Application.getInstance().getModule(WeblogModule.class);
            try
            {
                blog = wm.getBlog(blogId);
            } catch (DaoException e)
            {
                Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
            }
        }
        super.onRequest(event);
    }

    public String getDefaultTemplate()
    {
        return "weblog/postform";
    }


    public String getBlogId()
    {
        return blogId;
    }

    public void setBlogId(String blogId)
    {
        this.blogId = blogId;
    }

    public Blog getBlog()
    {
        return blog;
    }

    public void setBlog(Blog blog)
    {
        this.blog = blog;
    }

    public TextField getTitleTF()
    {
        return titleTF;
    }

    public void setTitleTF(TextField titleTF)
    {
        this.titleTF = titleTF;
    }

    public RichTextBox getContentTB()
    {
        return contentTB;
    }

    public void setContentTB(RichTextBox contentTB)
    {
        this.contentTB = contentTB;
    }

    public Button getSaveAsDraftButton()
    {
        return saveAsDraftButton;
    }

    public void setSaveAsDraftButton(Button saveAsDraftButton)
    {
        this.saveAsDraftButton = saveAsDraftButton;
    }

    public DateField getPublishDate()
    {
        return publishDate;
    }

    public void setPublishDate(DateField publishDate)
    {
        this.publishDate = publishDate;
    }

    public BlogTimeField getPublishTime()
    {
        return publishTime;
    }

    public void setPublishTime(BlogTimeField publishTime)
    {
        this.publishTime = publishTime;
    }

    public Button getPostButton()
    {
        return postButton;

    }


    public Forward onSubmit(Event event)
    {
        if(cancelButton.getAbsoluteName().equals(findButtonClicked(event)))
            return new Forward(FORWARD_POST_CANCEL);
        return super.onSubmit(event);
    }



    public void setPostButton(Button postButton)
    {
        this.postButton = postButton;
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
