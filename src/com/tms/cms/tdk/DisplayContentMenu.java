package com.tms.cms.tdk;

import com.tms.cms.bookmark.Bookmark;
import com.tms.cms.core.model.ContentManager;
import com.tms.cms.core.model.ContentObject;
import com.tms.cms.core.model.ContentPublisher;
import com.tms.cms.core.model.DefaultContentObject;
import com.tms.cms.section.Section;
import com.tms.cms.vsection.VSection;
import kacang.Application;
import kacang.model.DataObjectNotFoundException;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.ui.Event;
import kacang.ui.LightWeightWidget;

public class DisplayContentMenu extends LightWeightWidget {

    private String id;
    private ContentObject root;
    private String hideSummary;
    private String orphans;
    private int levels;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ContentObject getRoot() {
        return root;
    }

    public void setRoot(ContentObject root) {
        this.root = root;
    }

    public String getHideSummary() {
        return hideSummary;
    }

    public void setHideSummary(String hideSummary) {
        this.hideSummary = hideSummary;
    }

    public String getOrphans() {
        return orphans;
    }

    public void setOrphans(String orphans) {
        this.orphans = orphans;
    }

    public int getLevels() {
        return levels;
    }

    public void setLevels(int levels) {
        this.levels = levels;
    }

    public void onRequest(Event evt) {

        String rootId = (getId() != null) ? getId() : ContentManager.CONTENT_TREE_ROOT_ID;

        // retrieve content tree
        try {
            Application application = Application.getInstance();
            ContentPublisher cm = (ContentPublisher)application.getModule(ContentPublisher.class);
            User user = ((SecurityService)Application.getInstance().getService(SecurityService.class)).getCurrentUser(evt.getRequest());
            String permission = (Boolean.valueOf(getHideSummary()).booleanValue()) ? ContentManager.USE_CASE_VIEW : null;
            if (Boolean.valueOf(getOrphans()).booleanValue()) {
                this.root = cm.viewTreeWithOrphans(rootId, new String[] { Section.class.getName(), VSection.class.getName(), Bookmark.class.getName() }, permission, user.getId());
            }
            else {
                this.root = cm.viewTree(rootId, new String[] { Section.class.getName(), VSection.class.getName(), Bookmark.class.getName() }, permission, user.getId());
            }
        }
        catch(DataObjectNotFoundException e) {
            this.root = new DefaultContentObject();
        }
        catch(Exception e) {
            throw new RuntimeException(e.toString());
        }

    }

    public String getDefaultTemplate() {
        return "cms/tdk/displayContentMenu";
    }

}
