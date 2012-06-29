package com.tms.sam.po.ui;

import java.util.Map;

import kacang.ui.Event;
import kacang.ui.Widget;

public class AttachmentInfo extends Widget {
	private Map attachmentMap;

    public AttachmentInfo(String s) {
        super(s);
    }

    public void onRequest(Event event) {
     attachmentMap = AttachmentForm.getAttachmentMapFromSession(event);
    }

    public String getTemplate() {
        return "po/attachmentInfo";
    }

    // === [ getters/setters ] =================================================
    public Map getAttachmentMap() {
        return attachmentMap;
    }

    public void setAttachmentMap(Map attachmentMap) {
        this.attachmentMap = attachmentMap;
    }

}
