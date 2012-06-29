package com.tms.collab.weblog.ui;

import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.Application;
import kacang.util.Log;
import kacang.model.DaoException;
import com.tms.collab.weblog.model.Post;
import com.tms.collab.weblog.model.WeblogModule;

import java.util.Calendar;

/**
 * Created by IntelliJ IDEA.
 * User: Disen
 * Date: Feb 27, 2004
 * Time: 2:11:59 PM
 * To change this template use Options | File Templates.
 */
public class editPostForm extends PostForm
{
    private String postId;
    private Post post;
    private CommentTable commentTable;

    public void init()
    {
        super.init();
        commentTable = new CommentTable("commenttable");
        addChild(commentTable);
        commentTable.init();
    }

    public void onRequest(Event event)
    {
        if(postId!=null&&!postId.equals("")){
            WeblogModule wm = (WeblogModule) Application.getInstance().getModule(WeblogModule.class);
            try
            {
                post = wm.getPost(postId);
                if(post!=null){
                    populateForm();
                    commentTable.setPostId(post.getId());
                    commentTable.onRequest(event);
                }
            } catch (DaoException e)
            {
                Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
            }
        }

        super.onRequest(event);
    }

    public Forward onValidate(Event event)
    {
        String buttonName = findButtonClicked(event);
        if(postButton.getAbsoluteName().equals(buttonName)||saveAsDraftButton.getAbsoluteName().equals(buttonName)){
            WeblogModule wm = (WeblogModule) Application.getInstance().getModule(WeblogModule.class);
            post.setTitle(titleTF.getValue().toString());
            post.setContents(contentTB.getValue().toString());
            Calendar pCal = publishDate.getCalendar();
            pCal.set(Calendar.HOUR,publishTime.getHour());
            pCal.set(Calendar.MINUTE,publishTime.getMinute());
            post.setPublishTime(pCal.getTime());
            post.setPublished(saveAsDraftButton.getAbsoluteName().equals(buttonName)?false:true);
            try
            {
                wm.updatePost(post);
                return new Forward(FORWARD_POST_UPDATED) ;
            } catch (DaoException e)
            {
                Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
                return new Forward(FORWARD_POST_UPDATE_FAILED);
            }

        }
        return super.onValidate(event);
    }

    private void populateForm(){
        titleTF.setValue(post.getTitle());
        contentTB.setValue(post.getContents());
        publishDate.setDate(post.getPublishTime());
        publishTime.setDate(post.getPublishTime());
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
        return "weblog/editpost";
    }

    public Post getPost()
    {
        return post;
    }

    public void setPost(Post post)
    {
        this.post = post;
    }

    public CommentTable getCommentTable()
    {
        return commentTable;
    }

    public void setCommentTable(CommentTable commentTable)
    {
        this.commentTable = commentTable;
    }

}
