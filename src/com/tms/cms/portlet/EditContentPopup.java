package com.tms.cms.portlet;

import com.tms.cms.core.ui.EditContentObjectPanel;
import com.tms.cms.core.ui.ContentHelper;
import kacang.ui.Event;

public class EditContentPopup extends EditContentObjectPanel {

    public EditContentPopup() {
    }

    public EditContentPopup(String name) {
        super(name);
    }

    public void onRequest(Event evt) {
        String key = getId();
        if (key != null) {
            ContentHelper.setId(evt, key);
        }
        super.onRequest(evt);
    }
}
