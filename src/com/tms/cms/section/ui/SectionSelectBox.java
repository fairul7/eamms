package com.tms.cms.section.ui;

import com.tms.cms.core.model.ContentManager;
import com.tms.cms.core.model.ContentObject;
import com.tms.cms.core.model.ContentPublisher;
import com.tms.cms.section.Section;
import kacang.Application;
import kacang.stdui.SelectBox;
import kacang.ui.Event;
import kacang.util.Log;
import org.apache.commons.collections.SequencedHashMap;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 * Select box that displays the available Sections
 */
public class SectionSelectBox extends SelectBox {

	public boolean type=false;
	
    public boolean isType() {
		return type;
	}

	public void setType(boolean type) {
		this.type = type;
	}

	public SectionSelectBox() {
    }

    public SectionSelectBox(String name) {
        super(name);
    }

    private String permissionId;

    public String getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(String permissionId) {
        this.permissionId = permissionId;
    }

    public void onRequest(Event evt) {
        populateSelectBox(evt);
    }

    public void populateSelectBox(Event event) {
        try {
            String userId = getWidgetManager().getUser().getId();
            Application app = Application.getInstance();
            ContentPublisher cp = (ContentPublisher)app.getModule(ContentPublisher.class);
            Map sectionMap = new SequencedHashMap();
            if(type)
            	sectionMap.put("Any","Any Section");	
            ContentObject root = cp.viewTreeWithOrphans(ContentManager.CONTENT_TREE_ROOT_ID, new String[] { Section.class.getName() }, getPermissionId(), userId);
            populateMap(sectionMap, root, 0);            
            setOptionMap(sectionMap);
        }
        catch (Exception e) {
            Log.getLog(getClass()).error("Error retrieving sections", e);
        }
    }

    protected void populateMap(Map sectionMap, ContentObject root, int level) {
        Collection children = root.getChildren();
        if (children != null) {
            for (Iterator i = children.iterator(); i.hasNext();) {
                ContentObject co = (ContentObject)i.next();
                StringBuffer buffer = new StringBuffer();
                for (int j=0; j<level; j++) {
                    buffer.append("...");
                }
                if (level > 0) {
                    buffer.append("| ");
                }
                buffer.append(co.getName());
                String title = buffer.toString();
                if (title.length() > 50) {
                    title = title.substring(0, 50) + "..";
                }
                sectionMap.put(co.getId(), title);
                populateMap(sectionMap, co, level+1);
            }
        }
    }

}
