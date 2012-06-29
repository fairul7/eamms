package com.tms.collab.directory.ui;

import kacang.stdui.TableFormat;
import kacang.Application;
import kacang.ui.WidgetManager;
import com.tms.collab.directory.model.Contact;

public class ContactTableNameFormatter implements TableFormat {

    String contextPath;

    public ContactTableNameFormatter() {
    }

    public ContactTableNameFormatter(WidgetManager wm) {
        contextPath = (String)wm.getAttribute(WidgetManager.CONTEXT_PATH);
    }

    public String format(Object o) {
        StringBuffer buffer = new StringBuffer();
        if (o == null || o.toString().trim().length() == 0) {
            o = Application.getInstance().getMessage("addressbook.label.Notspecified","- Not specified -");
        }
        else if (o.toString().trim().length() > 50) {
            o = o.toString().substring(0, 50) + "...";
        }

        if (o.toString().startsWith(Contact.PREFIX_CONTACT_GROUP)) {
            if (contextPath != null) {
                buffer.append("<img src=\"" + contextPath + "/ekms/images/dp_doublehead.gif\" border=\"0\">");
            }
            buffer.append("<b>" + o.toString().substring(Contact.PREFIX_CONTACT_GROUP.length()) + "</b>");
        }
        else {
            buffer.append(o.toString());
        }
        return buffer.toString();
    }

}

