package com.tms.collab.messaging.ui;

import kacang.Application;
import kacang.ui.Event;
import kacang.ui.Widget;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.tms.collab.messaging.model.MessagingModule;

public class AttachmentInfo extends Widget {
    private Map attachmentMap;

    public AttachmentInfo(String s) {
        super(s);
    }

    public void onRequest(Event event) {
        //attachmentMap = AttachmentForm.getAttachmentMapFromSession(event);
    }

    public String getTemplate() {
        return "messaging/attachmentInfo";
    }

    // === [ getters/setters ] =================================================
    public Map getAttachmentMap() {
    	HttpSession session = Application.getThreadRequest().getSession();
    	Map attachmentMap = (Map)session.getAttribute(MessagingModule.ATTACHMENT_MAP_SESSION_ATTRIBUTE);
    	if (attachmentMap == null) {
    		attachmentMap = new HashMap();
    	}
        return attachmentMap;
    }

    public void setAttachmentMap(Map attachmentMap) {
        this.attachmentMap = attachmentMap;
    }
}
