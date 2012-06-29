package com.tms.collab.weblog.ui;

import kacang.ui.Forward;
import kacang.ui.Event;
import kacang.util.Log;
import kacang.util.UuidGenerator;
import kacang.model.DaoException;
import kacang.Application;

import java.util.Date;
import java.util.Calendar;

import com.tms.collab.weblog.model.Post;
import com.tms.collab.weblog.model.WeblogModule;

/**
 * Created by IntelliJ IDEA.
 * User: Disen
 * Date: Feb 27, 2004
 * Time: 9:50:54 AM
 * To change this template use Options | File Templates.
 */
public class addPostForm extends PostForm
{
    public void init()
    {
        super.init();
        publishDate.setDate(new Date());
        publishTime.setDate(new Date());
     }

    public Forward onValidate(Event event)
    {
        String buttonName = findButtonClicked(event);
        if(postButton.getAbsoluteName().equals(buttonName)||saveAsDraftButton.getAbsoluteName().equals(buttonName)){
            WeblogModule wm = (WeblogModule) Application.getInstance().getModule(WeblogModule.class);
            try
            {
                blog = wm.getBlog(blogId);
                if(blog==null||!blog.getUserId().equals(getWidgetManager().getUser().getId()))
                    return new Forward(FORWARD_ACCESS_DENIED);
                Post post = new Post();
                post.setId(UuidGenerator.getInstance().getUuid());
                post.setBlogId(blog.getId());
                post.setTitle(titleTF.getValue().toString());
                post.setContents(contentTB.getValue().toString());
                Calendar pCal = publishDate.getCalendar();
                pCal.set(Calendar.HOUR,publishTime.getHour());
                pCal.set(Calendar.MINUTE,publishTime.getMinute());
                pCal.set(Calendar.SECOND,publishTime.getSecond());
                post.setPublishTime(pCal.getTime());
                post.setPublished(saveAsDraftButton.getAbsoluteName().equals(buttonName)?false:true);
                post.setUserId(getWidgetManager().getUser().getId());
                wm.addPost(post);
                init();
                return new Forward(FORWARD_POST_ADDED);
            } catch (DaoException e)
            {
                Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
            }
        }
        return super.onValidate(event);
    }




}
