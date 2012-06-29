package com.tms.collab.qmessaging.ui;

import kacang.Application;
import kacang.ui.Forward;
import kacang.ui.Event;
import kacang.stdui.Form;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.services.security.SecurityException;
import kacang.util.Log;
import com.tms.collab.messaging.model.Message;
import com.tms.collab.messaging.model.MessagingModule;
import com.tms.collab.messaging.model.MessagingException;
import com.tms.util.FormatUtil;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.AddressException;
import java.util.Collection;
import java.util.Calendar;
import java.text.SimpleDateFormat;

/**
 * Created by IntelliJ IDEA.
 * User: Michael
 * Date: Jan 28, 2004
 * Time: 4:01:56 PM
 * To change this template use Options | File Templates.
 */
public class QuickMessengerReply extends Form
{
    public static final String DEFAULT_TEMPLATE = "qmessaging/qMessengerReply";

    private String messageId;
    private Message originalMessage;
    private QuickMessengerForm form;
    private SimpleDateFormat formatter;

    public QuickMessengerReply()
    {
        formatter = new SimpleDateFormat(FormatUtil.getInstance().getLongDateTimeFormat());
    }

    public QuickMessengerReply(String s)
    {
        super(s);
        formatter = new SimpleDateFormat(FormatUtil.getInstance().getLongDateTimeFormat());
    }

    public Forward actionPerformed(Event event)
    {
        Forward forward = null;
        if(originalMessage != null)
        {
            if(form.getCancel().getAbsoluteName().equals(findButtonClicked(event)))
                forward = new Forward((QuickMessengerForm.FORWARD_CANCEL));
            else
                forward = super.actionPerformed(event);
        }
        return forward;
    }

    public void init()
    {
        if(originalMessage != null)
        {
            try
            {
                InternetAddress[] array = new InternetAddress[0];
                array = InternetAddress.parse(originalMessage.getFrom());
                String address = array[0].getAddress();
                String username = "";
                if(address != null && address.indexOf('@') != -1)
                    username = address.substring(0, address.indexOf('@'));
                SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
                Collection list = service.getUsersByUsername(username);
                if(!list.isEmpty())
                {
                    Calendar now = Calendar.getInstance();
                    User user = (User) list.iterator().next();
                    form = new QuickMessengerForm("form");
                    form.setUserId(user.getId());
                    form.init();
                    form.getBody().setValue("\n\n\n- [" + formatter.format(now.getTime()) + "] " + username + " wrote: \n" + originalMessage.getBody());
                    addChild(form);
                }
            }
            catch (AddressException e)
            {
                Log.getLog(QuickMessengerReply.class).error(e.getMessage(), e);
            }
            catch (SecurityException e)
            {
                Log.getLog(QuickMessengerReply.class).error(e.getMessage(), e);
            }
        }
    }

    public String getDefaultTemplate()
    {
        return DEFAULT_TEMPLATE;
    }

    //Getters and Setters
    public QuickMessengerForm getForm()
    {
        return form;
    }

    public void setForm(QuickMessengerForm form)
    {
        this.form = form;
    }

    public Message getOriginalMessage()
    {
        return originalMessage;
    }

    public void setOriginalMessage(Message message)
    {
        originalMessage = message;
    }

    public String getMessageId()
    {
        return messageId;
    }

    public void setMessageId(String messageId)
    {
        this.messageId = messageId;
        MessagingModule module = (MessagingModule) Application.getInstance().getModule(MessagingModule.class);
        try
        {
            originalMessage = module.getMessageByMessageId(messageId);
            originalMessage.setRead(true);
            module.updateMessage(originalMessage);
        }
        catch (MessagingException e)
        {
            Log.getLog(QuickMessengerReply.class).error(e.getMessage(), e);
        }
        init();
    }
}
