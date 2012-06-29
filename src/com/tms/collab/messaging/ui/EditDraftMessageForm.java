package com.tms.collab.messaging.ui;

import com.tms.collab.messaging.model.Message;
import com.tms.collab.messaging.model.MessagingException;
import com.tms.collab.messaging.model.MessagingModule;
import com.tms.collab.messaging.model.Util;
import kacang.services.storage.StorageException;
import kacang.ui.Event;
import kacang.util.Log;

import java.io.FileNotFoundException;

public class EditDraftMessageForm extends ReplyMessageForm {
    public void onRequest(Event event) {
        String messageId;
        MessagingModule mm;
        Message message;

        messageId = event.getRequest().getParameter("messageId");

        if(messageId==null) {
            Log.getLog(getClass()).error("MessageId not specified");
            return;
        }

        try {
            mm = Util.getMessagingModule();
            message = mm.getMessageByMessageId(messageId);

            setFormFieldValues(message);
            setAttachmentValues(event,  message);

        } catch (MessagingException e) {
            Log.getLog(getClass()).error(e.getMessage(), e);
        } catch (StorageException e) {
            Log.getLog(getClass()).error(e.getMessage(), e);
        } catch (FileNotFoundException e) {
            Log.getLog(getClass()).error(e.getMessage(), e);
        }

    }

}