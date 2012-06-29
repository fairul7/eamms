package com.tms.collab.forum.ui;

import com.tms.collab.forum.model.ForumModule;
import com.tms.collab.forum.model.Thread;
import kacang.Application;
import kacang.stdui.*;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.ui.WidgetManager;
import kacang.util.Log;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: WaiHung
 * Date: May 8, 2003
 * Time: 12:26:01 PM
 * To change this template use Options | File Templates.
 */
public class EditThreadForm extends Form
{
    public static final String DEFAULT_TEMPLATE = "forum/editThreadForm";

    private String              threadId;
    private String              userId;
    private Label               subject;
    private Label               author;
    private Label               email;
    private RichTextBox         content;
    private CheckBox            isPublic;
    private CheckBox            isActive;
    private Button              updateThread;
    private ResetButton         reset;
    private Button              cancel;
    private Thread              thread;

    public EditThreadForm()
    {
    }

    public EditThreadForm(String name)
    {
        this();
        setName(name);
    }

    public void init()
    {
        removeChildren();
        userId = getWidgetManager().getUser().getId();
        thread = new Thread();
        setMethod("POST");
        try
        {
            if(getThreadId() !=null && !getThreadId().equals(""))
            {
                ForumModule forumModule = (ForumModule) Application.getInstance().getModule(ForumModule.class);
                thread = forumModule.getThread(getThreadId(), userId);

                subject = new Label("subject");
                subject.setText(thread.getSubject());
                content = new RichTextBox("content");
                content.setValue(thread.getContent());
//                content.setImageUrl(getParent().getWidgetManager().getAttribute(WidgetManager.CONTEXT_PATH)+"/forum/frame.jsp?id="+getThreadId());
                author = new Label(Application.getInstance().getMessage("forum.label.author","author"));
                author.setText(thread.getOwnerId());
                email = new Label(Application.getInstance().getMessage("forum.label.email","email"));
                email.setText(thread.getEmail());
                isPublic = new CheckBox("isPublic");
                isPublic.setText(Application.getInstance().getMessage("forum.label.public","Public"));
                isPublic.setChecked(thread.getIsPublic());
                isActive = new CheckBox("isActive");
                isActive.setText(Application.getInstance().getMessage("forum.label.active","Active"));
                isActive.setChecked(thread.isActive());
                updateThread = new Button("updateThread");
                updateThread.setText(Application.getInstance().getMessage("forum.label.updateTopic","Update Topic"));
                reset = new ResetButton("reset");
                reset.setText(Application.getInstance().getMessage("forum.label.reset","Reset"));
                cancel = new Button("cancel");
                cancel.setText(Application.getInstance().getMessage("forum.label.cancel","Cancel"));
                addChild(author);
                addChild(email);
                addChild(subject);
                addChild(content);
                addChild(isPublic);
                addChild(isActive);
                addChild(updateThread);
                addChild(reset);
                addChild(cancel);
            }
        }
        catch (Exception e)
        {
            Log log = Log.getLog(this.getClass());
            log.error(e.getMessage(), e);
        }
    }
    
    public Label getAuthor() {
		return author;
	}

	public void setAuthor(Label author) {
		this.author = author;
	}

	public Label getEmail() {
		return email;
	}

	public void setEmail(Label email) {
		this.email = email;
	}

	public void setThreadId(String threadId)
    {
        this.threadId = threadId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public void setSubject(Label subject)
    {
        this.subject = subject;
    }

    public void setContent(RichTextBox content)
    {
        this.content = content;
    }

   public void setIsPublic(CheckBox isPublic)
    {
        this.isPublic = isPublic;
    }

    public void setActive(CheckBox active)
    {
        isActive = active;
    }

    public void setUpdateThread(Button updateThread)
    {
        this.updateThread = updateThread;
    }

    public void setReset(ResetButton reset)
    {
        this.reset = reset;
    }

    public void setCancel(Button cancel)
    {
        this.cancel = cancel;
    }

    public void setThread(Thread thread)
    {
        this.thread = thread;
    }

    public String getThreadId()
    {
        return threadId;
    }

    public String getUserId()
    {
        return userId;
    }

    public Label getSubject()
    {
        return subject;
    }

    public RichTextBox getContent()
    {
        return content;
    }

   public CheckBox getIsPublic()
    {
        return isPublic;
    }

    public CheckBox getActive()
    {
        return isActive;
    }

    public Button getUpdateThread()
    {
        return updateThread;
    }

    public ResetButton getReset()
    {
        return reset;
    }

    public Button getCancel()
    {
        return cancel;
    }

    public Thread getThread()
    {
        return thread;
    }

    public String getDefaultTemplate()
    {
        return DEFAULT_TEMPLATE;
    }

    public Forward onValidate(Event evt)
    {
        Log log = Log.getLog(this.getClass());
        Forward fwd = null;
        String buttonName = findButtonClicked(evt);
        try
        {
            if (buttonName != null && (buttonName.equals(reset.getAbsoluteName()) || buttonName.equals(cancel.getAbsoluteName())))
            {
                init();
                fwd = new Forward();
                fwd.setName(Form.CANCEL_FORM_ACTION);
            }
            else
            {
                WidgetManager widgetManager = WidgetManager.getWidgetManager(evt.getRequest());
                //forum.setName((String)forumName.getValue());
                thread.setContent((String)content.getValue());
                thread.setModificationDate(new Date());
                thread.setActive(isActive.isChecked());
                thread.setIsPublic(isPublic.isChecked());
                log.debug("~~~ Forum Description = " + thread.getContent());
                log.debug("~~~ Forum Active = " + thread.isActive());
                log.debug("~~~ Forum Public = " + thread.getIsPublic());

                ForumModule forumModule = (ForumModule) Application.getInstance().getModule(ForumModule.class);
                forumModule.editThread(thread, widgetManager.getUser().getId());
                fwd = super.onValidate(evt);
            }
         }
        catch(Exception e)
        {
            log.error(e.getMessage(), e);
        }
        return fwd;
    }

}
