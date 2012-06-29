package com.tms.collab.forum.ui;

import com.tms.collab.forum.model.ForumModule;
import com.tms.collab.forum.model.Message;
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
public class EditMessageForm extends Form
{
    public static final String DEFAULT_TEMPLATE = "forum/editMessageForm";

    private String              messageId;
    private String              userId;
    private Label               subject;
    private Label               author;
    private Label               email;
    private TextBox             content;
    private Button              updateMessage;
    private ResetButton         reset;
    private Button              cancel;
    private Message             postedMessage;

    public EditMessageForm()
    {
    }

    public EditMessageForm(String name)
    {
        this();
        setName(name);
    }

    public void init()
    {
        Log log = Log.getLog(this.getClass());
        removeChildren();
        userId = getWidgetManager().getUser().getId();
        postedMessage = new Message();
        setMethod("POST");
        try
        {
            if(getMessageId() !=null && !getMessageId().equals(""))
            {
                ForumModule forumModule = (ForumModule) Application.getInstance().getModule(ForumModule.class);
                postedMessage = forumModule.getPostedMessage(getMessageId(), userId);
                log.debug("~~~ postedMessage = " + postedMessage);
                subject = new Label(Application.getInstance().getMessage("forum.label.subject","subject"));
                subject.setText(postedMessage.getSubject());
                author = new Label(Application.getInstance().getMessage("forum.label.author","author"));
                author.setText(postedMessage.getOwnerId());
                email = new Label(Application.getInstance().getMessage("forum.label.email","email"));
                email.setText(postedMessage.getEmail());
                content = new RichTextBox("content");
                content.setValue(postedMessage.getContent());
                updateMessage = new Button("updateMessage");
                updateMessage.setText(Application.getInstance().getMessage("forum.label.updateMessage","Update Message"));
                reset = new ResetButton("reset");
                reset.setText(Application.getInstance().getMessage("forum.label.reset","Reset"));
                cancel = new Button("cancel");
                cancel.setText(Application.getInstance().getMessage("forum.label.cancel","Cancel"));
                addChild(author);
                addChild(email);
                addChild(subject);
                addChild(content);
                addChild(updateMessage);
                addChild(reset);
                addChild(cancel);
            }
        }
        catch (Exception e)
        {
            log.error(e.getMessage(), e);
        }
    }

    public void setMessageId(String messageId)
    {
        this.messageId = messageId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public void setSubject(Label subject)
    {
        this.subject = subject;
    }

    public void setContent(TextBox content)
    {
        this.content = content;
    }

    public void setUpdateMessage(Button updateMessage)
    {
        this.updateMessage = updateMessage;
    }

    public void setReset(ResetButton reset)
    {
        this.reset = reset;
    }

    public void setCancel(Button cancel)
    {
        this.cancel = cancel;
    }

    public void setPostedMessage(Message postedMessage)
    {
        this.postedMessage = postedMessage;
    }

    public String getMessageId()
    {
        return messageId;
    }

    public String getUserId()
    {
        return userId;
    }

    public Label getSubject()
    {
        return subject;
    }

    public TextBox getContent()
    {
        return content;
    }

    public Button getUpdateMessage()
    {
        return updateMessage;
    }

    public ResetButton getReset()
    {
        return reset;
    }

    public Button getCancel()
    {
        return cancel;
    }

    public Message getPostedMessage()
    {
        return postedMessage;
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
                postedMessage.setContent((String)content.getValue());
                postedMessage.setModificationDate(new Date());
                log.debug("~~~ Forum Description = " + postedMessage.getContent());

                ForumModule forumModule = (ForumModule) Application.getInstance().getModule(ForumModule.class);
                forumModule.editMessage(postedMessage, widgetManager.getUser().getId());
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
