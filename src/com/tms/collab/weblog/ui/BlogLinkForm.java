package com.tms.collab.weblog.ui;

import kacang.stdui.Form;
import kacang.stdui.Button;
import kacang.stdui.TextField;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.ui.Forward;
import kacang.ui.Event;
import kacang.Application;
import kacang.model.DaoException;
import com.tms.collab.weblog.model.Link;
import com.tms.collab.weblog.model.WeblogModule;
import com.tms.collab.weblog.model.Blog;

/**
 * Created by IntelliJ IDEA.
 * User: Disen
 * Date: Mar 10, 2004
 * Time: 1:20:43 PM
 * To change this template use Options | File Templates.
 */
public class BlogLinkForm  extends Form
{
    public static final String FORWARD_CANCEL ="cancel";
    public static final String FORWARD_LINK_ADDED = "added";
    public static final String FORWARD_LINK_UPDATED = "updated";
    public static final String FORWARD_LINK_FAILED = "failed";
    public static final String FORWARD_BLOG_NOT_FOUND= "blog not found";
    public static final String FORWARD_INVALID_USER= "invalid user";


    protected String blogId;
    protected Link link;
    protected String linkId;
    protected Button saveButton,cancelButton;
    protected TextField nameTF;
    protected TextField url;
    protected LinkTable linkTable;

    public BlogLinkForm()
    {
    }

    public BlogLinkForm(String s)
    {
        super(s);
    }

    public Button getCancelButton()
    {
        return cancelButton;
    }

    public void setCancelButton(Button cancelButton)
    {
        this.cancelButton = cancelButton;
    }

    public void init()
    {
        super.init();
        setMethod("POST");
        saveButton = new Button("saveButton",Application.getInstance().getMessage("weblog.label.save","Save"));
        cancelButton = new Button("cancelButton",Application.getInstance().getMessage("weblog.label.cancel","Cancel"));
        nameTF = new TextField("nameTF");
        nameTF.addChild(new ValidatorNotEmpty("namevalidator"));
        url = new TextField("url");
        url.addChild(new ValidatorNotEmpty("urlvalidator"));
        linkTable = new LinkTable("linkTable");
        addChild(linkTable);
        linkTable.init();
        addChild(cancelButton);
        addChild(saveButton);
        addChild(nameTF);
        addChild(url);
    }

    public void onRequest(Event event)
    {
        super.onRequest(event);
        if(blogId!=null&&!blogId.equals("")){
            linkTable.setBlogId(blogId);
            linkTable.onRequest(event);
        }
    }

        public Forward onSubmit(Event event)
        {
            if(blogId==null||blogId.equals(""))
                return new Forward(FORWARD_LINK_FAILED);
            WeblogModule wm = (WeblogModule) Application.getInstance().getModule(WeblogModule.class);
            try
            {
                Blog blog = wm.getBlog(blogId);
                if(!getWidgetManager().getUser().getId().equals(blog.getUserId()))
                    return new Forward(FORWARD_INVALID_USER);
            } catch (DaoException e)
            {
                return new Forward(FORWARD_BLOG_NOT_FOUND);
            }

        if(cancelButton.getAbsoluteName().equals(findButtonClicked(event))){
            return new Forward(FORWARD_CANCEL);
        }
        return super.onSubmit(event);
    }

    public String getDefaultTemplate()
    {
        return "weblog/linkform";
    }

    public LinkTable getLinkTable()
    {
        return linkTable;
    }

    public void setLinkTable(LinkTable linkTable)
    {
        this.linkTable = linkTable;
    }

       public String getBlogId()
    {
        return blogId;
    }

    public void setBlogId(String blogId)
    {
        this.blogId = blogId;
    }

    public Link getLink()
    {
        return link;
    }

    public void setLink(Link link)
    {
        this.link = link;
    }

    public String getLinkId()
    {
        return linkId;
    }

    public void setLinkId(String linkId)
    {
        this.linkId = linkId;
    }

    public Button getSaveButton()
    {
        return saveButton;
    }

    public void setSaveButton(Button saveButton)
    {
        this.saveButton = saveButton;
    }

    public TextField getNameTF()
    {
        return nameTF;
    }

    public void setNameTF(TextField nameTF)
    {
        this.nameTF = nameTF;
    }

    public TextField getUrl()
    {
        return url;
    }

    public void setUrl(TextField url)
    {
        this.url = url;
    }


}
