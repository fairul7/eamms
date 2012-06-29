package com.tms.collab.messaging.ui;

import kacang.ui.Event;
import kacang.util.Log;
import kacang.services.security.User;
import kacang.services.storage.StorageService;
import kacang.services.storage.StorageFile;
import kacang.services.storage.StorageException;
import kacang.Application;
import com.tms.collab.messaging.model.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;
import java.io.FileNotFoundException;

public class ReplyMessageForm extends ComposeMessageForm {
    public void onRequest(Event event) {
        String messageId;
        String replyAll;
        MessagingModule mm;
        Message message;
        User user;

        if(Util.isRichTextCapable(event.getRequest())) {
            setHtmlEmail(true);
        } else {
            setHtmlEmail(false);
        }


        messageId = event.getRequest().getParameter("messageId");
        replyAll = event.getRequest().getParameter("replyAll");

        if(messageId==null) {
            Log.getLog(getClass()).error("MessageId not specified");
            return;
        }

        try {
            mm = Util.getMessagingModule();
            if("1".equals(replyAll)) {
                // get reply all message
                user = Util.getUser(event);
                message = mm.getReplyAllMessage(event, messageId, user.getId());

            } else {
                // get reply message
                message = mm.getReplyMessage(event, messageId);
            }

            setFormFieldValues(message);

            // reset attachments
            HttpSession session = event.getRequest().getSession();
            Util.getMessagingModule().deleteUserTempFolder(Util.getUser(event).getId(), session);

        } catch (Exception e) {
            Log.getLog(getClass()).error("Error generating reply message", e);
        }

    }

    protected void setFormFieldValues(Message message) {
        List list;
        String selfFrom = "";
        String selfIntranet = getWidgetManager().getUser().getUsername() + "@" + MessagingModule.INTRANET_EMAIL_DOMAIN;

		try
		{
			MessagingModule module = (MessagingModule) Application.getInstance().getModule(MessagingModule.class);
			IntranetAccount account = module.getIntranetAccountByUserId(getWidgetManager().getUser().getId());
			selfFrom = account.getFromAddress();
		}
		catch (MessagingException e)
		{
			Log.getLog(getClass()).error("Error while retrieving intranet account", e);
		}

        list = message.getToIntranetList();
        list.addAll(message.getToList());
		list.remove(selfFrom);
		list.remove(selfIntranet);
        getTbTo().setValue(Util.convertRecipientsListToString(list));

        list = message.getCcIntranetList();
        list.addAll(message.getCcList());
		list.remove(selfFrom);
		list.remove(selfIntranet);
        getTbCc().setValue(Util.convertRecipientsListToString(list));

        list = message.getBccIntranetList();
        list.addAll(message.getBccList());
		list.remove(selfFrom);
		list.remove(selfIntranet);
        getTbBcc().setValue(Util.convertRecipientsListToString(list));

		getTfSubject().setValue(message.getSubject());
        getTbBody().setValue(message.getBody());
    }

    protected void setAttachmentValues(Event event, Message message) throws StorageException, FileNotFoundException {
        User user = Util.getUser(event);

        // clear attachment map
        HttpSession session = event.getRequest().getSession();
        session.removeAttribute(MessagingModule.ATTACHMENT_MAP_SESSION_ATTRIBUTE);
        Map attachmentMap = AttachmentForm.getAttachmentMapFromSession(event);

        // populate attachment map
        StorageService ss = (StorageService) Application.getInstance().getService(StorageService.class);
        List storageFileList = message.getStorageFileList();
        for (int i = 0; i < storageFileList.size(); i++) {
            StorageFile src = (StorageFile) storageFileList.get(i);
            StorageFile dest = new StorageFile(MessagingModule.ROOT_PATH + "/" + user.getId() + "/temp/" + src.getName());
            dest.setInputStream(src.getInputStream());
            ss.store(dest);
            attachmentMap.put(dest.getName(), "-data in storage service-");
        }
    }
}
