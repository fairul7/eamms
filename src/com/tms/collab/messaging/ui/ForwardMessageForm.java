package com.tms.collab.messaging.ui;

import com.tms.collab.messaging.model.Message;
import com.tms.collab.messaging.model.MessagingException;
import com.tms.collab.messaging.model.MessagingModule;
import com.tms.collab.messaging.model.Util;
import kacang.ui.Event;
import kacang.util.Log;
import kacang.services.storage.StorageException;

import java.io.FileNotFoundException;

public class ForwardMessageForm extends ReplyMessageForm {
    public void onRequest(Event event) {
        String messageId;
        MessagingModule mm;
        Message message;

        if(Util.isRichTextCapable(event.getRequest())) {
            setHtmlEmail(true);
        } else {
            setHtmlEmail(false);
        }


        messageId = event.getRequest().getParameter("messageId");

        if(messageId==null) {
            Log.getLog(getClass()).error("MessageId not specified");
            return;
        }

        try {
            mm = Util.getMessagingModule();
            message = mm.getForwardMessage(event, messageId);

            setFormFieldValues(message);
            setAttachmentValues(event, message);

        } catch (MessagingException e) {
            Log.getLog(getClass()).error(e.getMessage(), e);
        } catch (StorageException e) {
            Log.getLog(getClass()).error(e.getMessage(), e);
        } catch (FileNotFoundException e) {
            Log.getLog(getClass()).error(e.getMessage(), e);
        }

    }

}