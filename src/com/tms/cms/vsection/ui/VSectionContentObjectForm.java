package com.tms.cms.vsection.ui;

import com.tms.cms.core.model.ContentObject;
import com.tms.cms.core.ui.ContentObjectForm;
import com.tms.cms.vsection.VSection;
import com.tms.cms.vsection.VSectionModule;
import kacang.services.security.User;
import kacang.stdui.SortableSelectBox;
import kacang.ui.Event;
import org.apache.commons.collections.SequencedHashMap;

import java.util.*;

/**
 * Form for adding/editing a VSection.
 */
public class VSectionContentObjectForm extends ContentObjectForm {

    private String id;
    private String[] ids = null;
    private SortableSelectBox selectBox;
    private VSectionContentObjectPopupForm popupForm;

    public VSectionContentObjectForm() {
    }

    public VSectionContentObjectForm(String name) {
        super(name);
    }

    public String getDefaultTemplate() {
        return "cms/admin/vSectionContentObjectForm";
    }

    public void init(Event evt) {
        super.init(evt);

        selectBox = new SortableSelectBox("selectBox");
        addChild(selectBox);

        getWidgetManager().removeWidget(popupForm);
        popupForm = new VSectionContentObjectPopupForm("vSectionContentObjectPopupForm");
        popupForm.setWidgetManager(getWidgetManager());
        popupForm.init(evt);
        popupForm.setContentObject(getContentObject());
        getWidgetManager().addWidget(popupForm);
    }

    public void setContentObject(ContentObject contentObject) {
        super.setContentObject(contentObject);
    }

    protected void populateFields(Event evt) {
        super.populateFields(evt);
        // populate values?
        popupForm.setContentObject(getContentObject());
        if (getContentObject() != null) {
            popupForm.populateFields(evt);
            if (getIds() != null) {
                VSection section = (VSection)getContentObject();
                section.setIds(getIds());
            }
            Collection list = getContentObjectList();
            Map optionMap = new SequencedHashMap();
            for (Iterator i=list.iterator(); i.hasNext();) {
                ContentObject co = (ContentObject)i.next();
                optionMap.put(co.getId(), co.getName());
            }
//            optionMap.put("", "--- N/A ---");
            selectBox.setOptionMap(optionMap);
        }
    }

    protected void populateContentObject(Event evt, ContentObject contentObject) {
        super.populateContentObject(evt, contentObject);

        List list = (List)selectBox.getValue();
        if (list != null) {
            String[] ids = (String[])list.toArray(new String[0]);
            setIds(ids);
            ((VSection)contentObject).setIds(ids);
        }
    }






    public Collection getContentObjectList() {
        User user = getWidgetManager().getUser();
        VSection contentObject= (VSection)getContentObject();
        return VSectionModule.getContentObjectList(contentObject, user);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String[] getIds() {
        return ids;
    }

    public void setIds(String[] ids) {
        this.ids = ids;
    }

//-- Convenience Methods

    public String getSelectBoxIdsAsString() {
        String str = "";
        Map map = selectBox.getOptionMap();
        if (map != null) {
            List list = new ArrayList(map.keySet());
            for (int i=0; i<list.size(); i++) {
                if (i > 0)
                    str += "|";
                str += list.get(i);
            }
        }
        return str;
    }

}
