package com.tms.collab.weblog.ui;

import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.Application;
import kacang.services.security.SecurityException;
import kacang.util.Log;
import kacang.model.DaoException;
import com.tms.collab.weblog.model.Blog;
import com.tms.collab.weblog.model.WeblogModule;
import com.tms.collab.weblog.model.BlogUtil;
import com.tms.collab.calendar.ui.UserUtil;

/**
 * Created by IntelliJ IDEA.
 * User: Disen
 * Date: Mar 5, 2004
 * Time: 5:40:21 PM
 * To change this template use Options | File Templates.
 */
public class editBlogForm extends addBlogForm
{
    private String blogId;
  //  private Button saveButton;
    private Blog blog;
    public editBlogForm()
    {
    }

    public editBlogForm(String s)
    {
        super(s);
    }

    public void init()
    {
        super.init();
        getAddButton().setText(Application.getInstance().getMessage("weblog.label.save","Save"));
    }

    public void onRequest(Event event)
    {
        super.onRequest(event);
        if(blogId!=null&&!blogId.equals("")&&BlogUtil.hasEditBlogPermission(getWidgetManager().getUser().getId(),blogId)){
            WeblogModule wm = (WeblogModule) Application.getInstance().getModule(WeblogModule.class);
            try
            {
                blog = wm.getBlog(blogId);
                populateForm();
            } catch (DaoException e)
            {
                Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
            }
        }
    }

    private void populateForm(){
        if(blog!=null){
            nameTF.setValue(blog.getName());
            title.setValue(blog.getTitle());
            description.setValue(blog.getDescription());
        }
    }

    public Forward onValidate(Event event)
    {
        try
        {
            String buttonName = findButtonClicked(event);
            if(getAddButton().getAbsoluteName().equals(buttonName)&&BlogUtil.hasEditBlogPermission(getWidgetManager().getUser().getId(),blogId)){
                WeblogModule wm = (WeblogModule) Application.getInstance().getModule(WeblogModule.class);
                if(!nameTF.getValue().toString().equals(blog.getName())){
                    if(wm.isNameExisted(nameTF.getValue().toString())){
                        nameTF.setInvalid(true);
                        return new Forward(FORWARD_NAME_EXISTED);
                    }
                    blog.setName(nameTF.getValue().toString());
                }
                blog.setTitle(title.getValue().toString());
                blog.setDescription(description.getValue().toString());
                blog.setUserName(UserUtil.getUser(blog.getUserId()).getUsername());
                wm.updateBlog(blog);
/*                init();*/
                return new Forward(BlogForm.FORWARD_BLOG_UPDATED);
            }
            //init();
        } catch (DaoException e)
        {
            Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
        } catch (SecurityException e)
        {
            Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
        }
        return super.onValidate(event);
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
}
