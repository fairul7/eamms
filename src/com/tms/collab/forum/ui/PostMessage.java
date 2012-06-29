package com.tms.collab.forum.ui;

import com.tms.collab.forum.model.ForumModule;
import com.tms.collab.forum.model.Message;
import kacang.Application;
import kacang.services.security.SecurityException;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.ui.Event;
import kacang.ui.LightWeightWidget;
import kacang.util.Log;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: WaiHung
 * Date: May 23, 2003
 * Time: 11:42:56 AM
 * To change this template use Options | File Templates.
 */
public class PostMessage extends LightWeightWidget
{
    public static final String ATTRIBUTE_FLAG = "messageCreationFlag";
    
    private String forumId;
    private String threadId;
    private String subject;
    private String content;
    private String author;
    private String email;
    private String parentMessageId;
    private Map message;
    private Message postedMessage;
    private String url;

    public void onRequest(Event evt)
    {
        HttpServletRequest request = evt.getRequest();

    	String userid=evt.getWidgetManager().getUser().getId();
        if(request.getAttribute(ATTRIBUTE_FLAG)!=null && request.getAttribute(ATTRIBUTE_FLAG) instanceof PostMessage)
            request.removeAttribute(ATTRIBUTE_FLAG);

        if(request.getParameter("form")!=null && request.getParameter("form").equals("newMessageForm"))
        {
            postedMessage = new Message();
            message = new HashMap();
            boolean emailerror=false;
            boolean authorerror=false;
            setForumId(request.getParameter("forumId"));
            setThreadId(request.getParameter("threadId"));
            setSubject(request.getParameter("msgSubject"));
            setParentMessageId(request.getParameter("parentMessageId"));
            setContent(request.getParameter("msgContent"));
            if("anonymous".equals(evt.getWidgetManager().getUser().getUsername())){
            	setAuthor(request.getParameter("msgAuthor"));
                setEmail(request.getParameter("msgEmail"));
                
                if (email == null || "".equals(email)){
                	emailerror=true;}
                if ((email.indexOf("@") != -1) && (email.indexOf("@") == email.lastIndexOf("@"))){
                	if (email.indexOf(".", email.lastIndexOf("@")) != -1 && email.indexOf(".", email.lastIndexOf("@")) < email.length() - 2)
                		{emailerror=false;}
                	else {
                		emailerror=true;	
                	}               	
                 }else{
                	 emailerror=true; 
                 }
                if(author==null || author.trim().equals(""))
                {
                	authorerror=true;
                }
            }else{
					setEmail((String)evt.getWidgetManager().getUser().getProperty("email1"));				
            }
            
            
            if((forumId!=null && !forumId.trim().equals("")) && (threadId!=null && !threadId.trim().equals("")) && (subject!=null && !subject.trim().equals("")) && !authorerror && !emailerror)
            {
                postedMessage.setForumId(forumId);
                postedMessage.setThreadId(threadId);
                postedMessage.setSubject(subject);
                postedMessage.setContent(content);
                postedMessage.setEmail(email);
                if("anonymous".equals(evt.getWidgetManager().getUser().getUsername())){
                	postedMessage.setOwnerId(author);                	
                }
                if (parentMessageId != null && parentMessageId.trim().length() > 0)
                {
                    postedMessage.setParentMessageId(parentMessageId);
                }
                else
                {
                    postedMessage.setParentMessageId(threadId);
                }

                ForumModule forumModule = (ForumModule) Application.getInstance().getModule(ForumModule.class);
                try
                {
                    forumModule.postMessage(postedMessage, evt.getWidgetManager().getUser().getUsername());
					Log.getLog(getClass()).write(new Date(), postedMessage.getMessageId(), userid,
						"Forum: Message created", postedMessage.getSubject(), evt.getRequest().getRemoteAddr(),
						evt.getRequest().getSession().getId());
                    request.setAttribute(ATTRIBUTE_FLAG, this);
                    if(getUrl()!=null)
                        evt.getResponse().sendRedirect(evt.getResponse().encodeRedirectURL(getUrl()+"?threadId="+threadId));
                }
                catch (Exception e)
                {
                    setMessageString("error", "ERROR in creating new Topic, please try again");
                    Log.getLog(PostMessage.class).error(e.getMessage(), e);
                }
            }
            else
            {
            	String error="";
            	if("anonymous".equals(evt.getWidgetManager().getUser().getUsername())){
                if(author==null || author.trim().equals(""))
                {
                    setMessageString(Application.getInstance().getMessage("forum.label.invalidAuthor","invalidAuthor"), "!");
                	error="error";
                }
                if (email == null || "".equals(email)){
                	setMessageString(Application.getInstance().getMessage("forum.label.invalidEmail","invalidEmail"), "!");
                	error="error";}
                
                if ((email.indexOf("@") != -1) && (email.indexOf("@") == email.lastIndexOf("@"))){
                	if (email.indexOf(".", email.lastIndexOf("@")) != -1 && email.indexOf(".", email.lastIndexOf("@")) < email.length() - 2)
                		{}
                	else {
                		setMessageString(Application.getInstance().getMessage("forum.label.invalidEmail","invalidEmail"), "!");
                		error="error";	
                	}
                	
                 }else{
                	 setMessageString(Application.getInstance().getMessage("forum.label.invalidEmail","invalidEmail"), "!");
             		error="error"; 
                 }
               
                }
            	
                if(subject==null || subject.trim().equals(""))
                {
                    setMessageString(Application.getInstance().getMessage("forum.label.invalidSubject","invalidSubject"), "!");
                	error="error";
                }else{
                	setMessageString(Application.getInstance().getMessage("forum.label.invalidSubject","invalidSubject"), "");
                }
                
                if(error.length()>0){
                	setMessageString("error", Application.getInstance().getMessage("general.error.formInvalid.message","Please fill in all the fields correctly"));	
                }
                /*else
                {
                    setMessageString("error", "ERROR in creating new Topic, please try again");
                }*/
            }
            
        }
    }

    public String getForumId()
    {
        return forumId;
    }

    public void setForumId(String forumId)
    {
        this.forumId = forumId;
    }

    public String getSubject()
    {
        return subject;
    }

    public void setSubject(String subject)
    {
        this.subject = subject;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public String getThreadId()
    {
        return threadId;
    }

    public void setThreadId(String threadId)
    {
        this.threadId = threadId;
    }

    public Map getMessage()
    {
        return message;
    }

    public void setMessage(Map message)
    {
        this.message = message;
    }

    public Message getPostedMessage()
    {
        return postedMessage;
    }

    public void setPostedMessage(Message postedMessage)
    {
        this.postedMessage = postedMessage;
    }

    public void setMessageString(String key, String value)
    {
        message.put(key, value);
    }

    public String getMessageString(String key)
    {
        return (String) message.get(key);
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public String getParentMessageId()
    {
        return parentMessageId;
    }

    public void setParentMessageId(String parentMessageId)
    {
        this.parentMessageId = parentMessageId;
    }

    public String getDefaultTemplate()
    {
        return null;
    }

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
