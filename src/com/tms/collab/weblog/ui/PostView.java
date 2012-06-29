package com.tms.collab.weblog.ui;

import kacang.ui.LightWeightWidget;
import kacang.ui.Event;
import kacang.Application;
import kacang.services.security.SecurityService;
import kacang.util.Log;
import kacang.util.UuidGenerator;
import kacang.model.DaoException;
import com.tms.collab.weblog.model.Post;
import com.tms.collab.weblog.model.WeblogModule;
import com.tms.collab.weblog.model.Blog;
import com.tms.collab.weblog.model.Comment;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Disen
 * Date: Feb 27, 2004
 * Time: 3:31:45 PM
 * To change this template use Options | File Templates.
 */
public class PostView extends LightWeightWidget
{
    private boolean preview = false,commentEmpty = false;
    private String currentUserId;
    private String postId;
    private Post post;
    private Comment temp_comment;
    private String name,email,url,comment;
    private Blog blog;

    public void onRequest(Event event)
    {
        super.onRequest(event);
        SecurityService  ss = (SecurityService )Application.getInstance().getService(SecurityService .class);
        currentUserId = ss.getCurrentUser(event.getRequest()).getId();
        HttpServletRequest req = event.getRequest();
        String buttonName = req.getParameter("submit");
        name = req.getParameter("name");
        email = req.getParameter("email");
        url = req.getParameter("url");
        comment = req.getParameter("comment");
        if(postId!=null&&!postId.equals("")){
            WeblogModule wm = (WeblogModule) Application.getInstance().getModule(WeblogModule.class);
            try
            {
                if((Application.getInstance().getMessage("weblog.label.preview","Preview").equals(buttonName)||"Post".equals(buttonName))){
                    if(comment==null||comment.equals(""))
                        commentEmpty= true;
                    else{
                        temp_comment = new Comment();
                        temp_comment.setUserName(((name==null||name.equals(""))?"Anonymous":name));
                        temp_comment.setUrl(url);
                        temp_comment.setEmail(email);
                        temp_comment.setComment(comment);
                        temp_comment.setDate(new Date());
                        if("Preview".equals(buttonName))
                            preview = true;
                        else{
                            temp_comment.setPostId(postId);
                            temp_comment.setId(UuidGenerator.getInstance().getUuid());
                            wm.addComment(temp_comment);
                            temp_comment = null;
                            preview = false;
                            name="";
                            email="";
                            url = "";
                            comment = "";
                        }
                    }
                }else{
                    preview = false;


                }
                post = wm.getPost(postId);
                blog = wm.getBlog(post.getBlogId());
                if(post.isCommented()&&!preview){
                    post.setComments(wm.getComments(postId));
                }
            } catch (DaoException e)
            {
                Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
            }
        }

    }

    public boolean isCommentEmpty()
    {
        return commentEmpty;
    }

    public String getPostId()
    {
        return postId;
    }

    public void setPostId(String postId)
    {
        this.postId = postId;
    }

    public String getDefaultTemplate()
    {
        return "weblog/postview";
    }

    public String getTemplate()
    {
        return getDefaultTemplate();
    }

    public Post getPost()
    {
        return post;
    }

    public void setPost(Post post)
    {
        this.post = post;
    }

    public Blog getBlog()
    {
        return blog;
    }

    public void setBlog(Blog blog)
    {
        this.blog = blog;
    }
    public String getCurrentUserId()
    {
        return currentUserId;
    }

    public Comment getTemp_comment()
    {
        return temp_comment;
    }

    public void setTemp_comment(Comment temp_comment)
    {
        this.temp_comment = temp_comment;
    }

    public boolean isPreview()
    {
        return preview;
    }

    public String getName()
    {
        return name;
    }

    public String getEmail()
    {
        return email;
    }

    public String getUrl()
    {
        return url;
    }

    public String getComment()
    {
        return comment;
    }

}
