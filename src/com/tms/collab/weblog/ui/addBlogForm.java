package com.tms.collab.weblog.ui;

import kacang.ui.Forward;
import kacang.ui.Event;
import kacang.stdui.Button;
import kacang.Application;
import kacang.services.security.SecurityException;
import kacang.services.security.User;
import kacang.util.Log;
import kacang.util.UuidGenerator;
import kacang.model.DaoException;
import com.tms.collab.weblog.model.WeblogModule;
import com.tms.collab.weblog.model.Blog;
import com.tms.collab.weblog.model.Category;
import com.tms.collab.calendar.ui.UserUtil;

import java.util.Date;
import java.util.Collection;
import java.util.TreeSet;


/**
 * Created by IntelliJ IDEA.
 * User: Disen
 * Date: Feb 25, 2004
 * Time: 11:58:42 AM
 * To change this template use Options | File Templates.
 */
public class addBlogForm extends BlogForm
{
    private Button addButton;
    private String blogId;

    public addBlogForm()
    {
    }

    public addBlogForm(String s)
    {
        super(s);
    }

    public void init()
    {
        super.init();
        addButton = new Button("addbutton",Application.getInstance().getMessage("weblog.label.create","Create"));
        addChild(addButton);
    }

    public Forward onValidate(Event event)
    {
        try
        {
            String buttonName = findButtonClicked(event);
            if(addButton.getAbsoluteName().equals(buttonName)){
            WeblogModule wm = (WeblogModule) Application.getInstance().getModule(WeblogModule.class);
                if(!wm.isNameExisted(nameTF.getValue().toString())){
                    Blog blog  = new Blog();
                    blogId = UuidGenerator.getInstance().getUuid();
                    blog.setId(blogId);
                    blog.setAllowComments(true);
                    blog.setName(nameTF.getValue().toString());
                    blog.setTitle(title.getValue().toString());
                    blog.setDescription(description.getValue().toString());
                    blog.setUserId(getWidgetManager().getUser().getId());
                    try
                    {
                        User user = UserUtil.getUser(blog.getUserId());
                        if(user!=null)
                            blog.setUserName(user.getUsername());
                    } catch (SecurityException e)
                    {
                        Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
                    }
                    blog.setHits(0);
                    blog.setCreationDate(new Date());
                    blog.setCategories(getDefaultCategories(blog));
                    wm.addBlog(blog);
                    init();
                    return new Forward(FORWARD_BLOG_ADDED);
                }else{
                    nameTF.setInvalid(true);
                    return new Forward(FORWARD_NAME_EXISTED);
                }
            }
        }
        catch (DaoException e)
        {
            Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
        }
        return super.onValidate(event);
    }

    private Collection getDefaultCategories(Blog blog){
        Collection categories = new TreeSet();
        Category category = new Category("General","General",blog.getId());
        categories.add(category);
        return categories;
    }

    public String getDefaultTemplate()
    {
        return "weblog/addBlogForm";
    }

    public Button getAddButton()
    {
        return addButton;
    }

    public void setAddButton(Button addButton)
    {
        this.addButton = addButton;
    }

    public String getBlogId()
    {
        return blogId;
    }

    public void setBlogId(String blogId)
    {
        this.blogId = blogId;
    }
}
