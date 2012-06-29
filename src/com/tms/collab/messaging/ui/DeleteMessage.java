package com.tms.collab.messaging.ui;

import java.util.Enumeration;

import com.tms.collab.messaging.model.*;
import kacang.ui.Event;
import kacang.ui.LightWeightWidget;
import kacang.util.Log;
import kacang.services.security.User;

public class DeleteMessage extends LightWeightWidget {
    private boolean deleteMessageSuccessful;
    private boolean movedToTrash;
    private String nextMessage;

    public void onRequest(Event event) {
        MessagingModule mm;
        User user;
        Message message;
        Folder trashFolder;
        String messageId;

        
        try {
            
            setDeleteMessageSuccessful(false);
        
            messageId = event.getRequest().getParameter("messageId");
            if(messageId==null) {
                Log.getLog(getClass()).error("MessageId not specified");
                return;
            }
            nextMessage = event.getRequest().getParameter("nextMessageId");
            
            // BUG 3095:
            setIndex(event.getRequest().getParameter("index"));
            

            user = Util.getUser(event);
            mm = Util.getMessagingModule();
            trashFolder = mm.getSpecialFolder(user.getId(), Folder.FOLDER_TRASH);
            message = mm.getMessageByMessageId(messageId, false);

            if(trashFolder.getId().equals(message.getFolderId())) {
                // if message is already in user's Trash folder, delete it
                mm.deleteMessage(messageId);
                setMovedToTrash(false);

            } else {
                // move it to the user's trash folde
                mm.moveMessage(message, trashFolder.getId());
                setMovedToTrash(true);
            }

            setDeleteMessageSuccessful(true);
            mm.updateUserDiskQuota(user.getId());

        } catch (MessagingException e) {
            Log.getLog(getClass()).error(e.getMessage(), e);
        }
    }

    public String getTemplate() {
        return "messaging/deleteMessage";
    }


    // === [ getters/setters ] =================================================
    
    // BUG 3095:
    private String _index;
    public String getIndex() {
        return _index;
    }
    public void setIndex(String index) {
        _index = index;
    }
    
    
    
    public boolean isDeleteMessageSuccessful() {
        return deleteMessageSuccessful;
    }

    public void setDeleteMessageSuccessful(boolean deleteMessageSuccessful) {
        this.deleteMessageSuccessful = deleteMessageSuccessful;
    }

    public boolean isMovedToTrash() {
        return movedToTrash;
    }

    public void setMovedToTrash(boolean movedToTrash) {
        this.movedToTrash = movedToTrash;
    }

	public String getNextMessage()
	{
		return nextMessage;
	}

	public void setNextMessage(String nextMessage)
	{
		this.nextMessage = nextMessage;
	}
}
