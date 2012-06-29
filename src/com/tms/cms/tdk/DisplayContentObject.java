package com.tms.cms.tdk;

import com.tms.cms.article.Article;
import com.tms.cms.bookmark.Bookmark;
import com.tms.cms.comment.Comment;
import com.tms.cms.comment.Commentary;
import com.tms.cms.core.model.*;
import com.tms.cms.document.Document;
import com.tms.cms.image.Image;
import com.tms.cms.page.Page;
import com.tms.cms.section.Section;
import com.tms.cms.spot.Spot;
import com.tms.cms.vsection.VSection;
import kacang.Application;
import kacang.model.DataObjectNotFoundException;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.ui.Event;
import kacang.ui.LightWeightWidget;

public class DisplayContentObject extends LightWeightWidget {

    private ContentObject contentObject;
    private String id;
    private String ignorePermission;
    private String hideSummary;
    private boolean restricted;
    private boolean preview;
    private boolean noHeader;
    private String types;

    public ContentObject getContentObject() {
        return contentObject;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIgnorePermission() {
        return ignorePermission;
    }

    public void setIgnorePermission(String ignorePermission) {
        this.ignorePermission = ignorePermission;
    }

    public String getHideSummary() {
        return hideSummary;
    }

    public void setHideSummary(String hideSummary) {
        this.hideSummary = hideSummary;
    }

    public boolean isRestricted() {
        return restricted;
    }

    public boolean isPreview() {
        return preview;
    }

    public boolean isNoHeader()
    {
        return noHeader;
    }

    public void setNoHeader(boolean noHeader)
    {
        this.noHeader = noHeader;
    }

    public String getTypes()
    {
        return types;
    }

    public void setTypes(String types)
    {
        this.types = types;
    }

    public void onRequest(Event evt) {
        // get id
        String key = getId();
        if (key == null)
            key = evt.getRequest().getParameter("id");

        // retrieve content object
        if (key != null && key.trim().length() > 0) {
            try {
                User user = ((SecurityService)Application.getInstance().getService(SecurityService.class)).getCurrentUser(evt.getRequest());
                Application application = Application.getInstance();
                ContentPublisher cp = (ContentPublisher)application.getModule(ContentPublisher.class);
                ContentManager cm = (ContentManager)application.getModule(ContentManager.class);

                // retrieve object
                ContentObject co;
                preview = ContentUtil.isPreviewRequest(evt.getRequest());
                if (!preview) {
                    // live content
                    co = cp.view(key, user);
                }
                else {
                    // preview content
                    co = cm.view(key, user);
                }

                // check permission
                if (!Boolean.valueOf(getHideSummary()).booleanValue() && (Section.class.getName().equals(co.getClassName()) || VSection.class.getName().equals(co.getClassName()))) {
                    this.contentObject = co;
                }
                else if (!Boolean.valueOf(getIgnorePermission()).booleanValue() && !ContentUtil.hasPermission(co, user.getId(), ContentManager.USE_CASE_VIEW)) {
                    restricted = true;
                }
                else {
                    this.contentObject = co;

                    // retrieve parent
                    if (co.getParentId() != null) {
                        try {
                            ContentObject parent;
                            if (!preview) {
                                // live content
                                parent = cp.view(co.getParentId(), user);
                            }
                            else {
                                // preview content
                                parent = cm.view(co.getParentId(), user);
                            }
                            co.setParent(parent);
                        }
                        catch (DataObjectNotFoundException e) {
                            ;
                        }
                    }
                }

                // add hit statistic
                if (!preview && !restricted && contentObject != null) {
                    ContentReporter reporter = (ContentReporter)application.getModule(ContentReporter.class);
                    reporter.audit(contentObject.getId(), ContentManager.USE_CASE_VIEW, contentObject.getVersion(), user);
                }
            }
            catch(DataObjectNotFoundException e) {
                ;
            }
            catch(Exception e) {
                throw new RuntimeException(e.toString());
            }
        }
    }

    public String getDefaultTemplate() {
        if (restricted) {
            return "cms/tdk/displayContentNoPermission";
        }
        else if (contentObject == null) {
            return "cms/tdk/displayContentNotFound";
        }
        else if (Article.class.getName().equals(contentObject.getClassName())) {
            return "cms/tdk/displayArticle";
        }
        else if (Section.class.getName().equals(contentObject.getClassName())) {
            return "cms/tdk/displaySection";
        }
        else if (VSection.class.getName().equals(contentObject.getClassName())) {
            return "cms/tdk/displayVSection";
        }
        else if (Document.class.getName().equals(contentObject.getClassName())) {
            return "cms/tdk/displayDocument";
        }
        else if (Image.class.getName().equals(contentObject.getClassName())) {
            return "cms/tdk/displayImage";
        }
        else if (Commentary.class.getName().equals(contentObject.getClassName())) {
            return "cms/tdk/displayCommentary";
        }
        else if (Comment.class.getName().equals(contentObject.getClassName())) {
            return "cms/tdk/displayComment";
        }
        else if (Bookmark.class.getName().equals(contentObject.getClassName())) {
            return "cms/tdk/displayBookmark";
        }
        else if (Spot.class.getName().equals(contentObject.getClassName())) {
            return "cms/tdk/displaySpot";
        }
        else if (Page.class.getName().equals(contentObject.getClassName())) {
            return "cms/tdk/displayPage";
        }
        else {
            return "cms/tdk/displayContentObject";
        }
    }

}
