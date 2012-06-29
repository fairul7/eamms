package com.tms.ekms.ekpmaildaemon.model.stub;

import com.tms.cms.article.Article;
import com.tms.cms.core.model.*;
import com.tms.cms.section.Section;

import kacang.Application;

import kacang.services.security.SecurityService;
import kacang.services.security.User;

import kacang.util.Log;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;


public class AddArticle implements MailStub {
    private ContentObject contentObject;
    private User user;
    private boolean checkPermission;

    public void doAdd() {
    }

    public ContentObject getContentObject() {
        return contentObject;
    }

    public void setContentObject(ContentObject contentObject) {
        this.contentObject = contentObject;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isCheckPermission() {
        return checkPermission;
    }

    public void setCheckPermission(boolean checkPermission) {
        this.checkPermission = checkPermission;
    }

    public boolean getPattern() //return subject pattern
     {
        return false; //To change body of implemented methods use File | Settings | File Templates.
    }

    public Map processMail(String emailAddress, Map contentMap) //pass to mailmodule
     {
        Application app = Application.getInstance();
        String errorMsg = "";
        contentMap.put("EMAIL", emailAddress);

        //check section start
        boolean testSection = false;
        Section section = new Section();
        SecurityService ss = (SecurityService) Application.getInstance()
                                                          .getService(SecurityService.class);
        Collection userCollectionAdmin = null;

        User user = new User();
        User admin = new User();

        try {
            userCollectionAdmin = ss.getUsersByUsername("admin");
            user = ss.getUser(contentMap.get("USERID").toString());
        } catch (kacang.services.security.SecurityException e) {
            e.printStackTrace();
        }

        for (Iterator itc = userCollectionAdmin.iterator(); itc.hasNext();) {
            admin = (User) itc.next();
        }

        try {
            ContentPublisher cp = (ContentPublisher) app.getModule(ContentPublisher.class);

            ContentObject root = cp.viewTreeWithOrphans(ContentManager.CONTENT_TREE_ROOT_ID,
                    new String[] { Section.class.getName() }, "Create",
                    admin.getId());

            Collection children = root.getChildren();

            for (Iterator i = children.iterator(); i.hasNext();) {
                ContentObject co = (ContentObject) i.next();

                if (co.getName().equalsIgnoreCase(app.getMessage(
                                "maildaemon.stub.addarticle.label.default.section"))) {
                    section.setId(co.getId());
                    testSection = true;
                } else {
                }
            }
        } catch (Exception e) {
            Log.getLog(getClass()).error("Error retrieving sections", e);
        }

        if (!testSection) {
            //mail module is not found inside the section therefore create one
            ContentManager module = (ContentManager) Application.getInstance()
                                                                .getModule(ContentManager.class);

            section.setName(app.getMessage(
                    "maildaemon.stub.addarticle.label.default.section"));
            section.setParentId(ContentManager.CONTENT_TREE_ROOT_ID);

            try {
                module.createNew(section, admin);
                module.submitForApproval(section.getId(), admin);

                if (module.hasPermission(section, admin.getId(),
                            module.USE_CASE_APPROVE)) {
                    // approve
                    section.setCheckedOut(false);
                    section.setSubmitted(true);
                    module.approve(section, admin);

                    // publish
                    module.publish(section.getId(), false, admin);

                    // return contentObject;
                }
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            } catch (ContentException e) {
                e.printStackTrace();
            }
        }

        //check section end
        ContentManager module = (ContentManager) Application.getInstance()
                                                            .getModule(ContentManager.class);
        Article article = new Article();

        article.setId("");
        article.setName(contentMap.get(app.getMessage(
                    "maildaemon.stub.addarticle.label.title")).toString());

        article.setStartDate(null);
        article.setEndDate(null);
        article.setParentId(section.getId());

        article.setAuthor(contentMap.get(app.getMessage(
                    "maildaemon.stub.addarticle.label.author")).toString());

        String tempStory= contentMap.get(app.getMessage(
                    "maildaemon.stub.addarticle.label.story")).toString();

        article.setContents(tempStory.replaceAll("\n","<br>"));
        article.setApproved(false);

        article.setPublishUser(user.getUsername());
        article.setPublishUserId(user.getId());

        try {
            module.createNew(article, user);

            module.submitForApproval(article.getId(), user);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
            errorMsg += app.getMessage(
                "maildaemon.stub.addarticle.label.invalidkey");
        } catch (ContentException e) {
            e.printStackTrace();
            errorMsg += app.getMessage(
                "maildaemon.stub.addarticle.label.invalidcontent");
        }

        if ("".equals(errorMsg)) {
            errorMsg = "\n" + app.getMessage("maildaemon.label.successful");
            Log.getLog(getClass()).info("SUCCESSFUL ADDED ARTICLE <EKPMAILDAEMON>");
        }

        contentMap.put("ERROR", (String) errorMsg);

        return contentMap;
    }

    public String getSubjectPattern() {
        Application app = Application.getInstance();

        return app.getMessage("maildaemon.stub.addarticle.label.addarticle");
    }

    public String[] getBodyPattern() {
        Application app = Application.getInstance();
        String[] tempPattern = {
            app.getMessage("maildaemon.stub.addarticle.label.title"),
            app.getMessage("maildaemon.stub.addarticle.label.author"),
            app.getMessage("maildaemon.stub.addarticle.label.story")
        };

        return tempPattern;
    }

    public String getInfo() {
        Application app = Application.getInstance();
        boolean testSection = false;

        SecurityService ss = (SecurityService) Application.getInstance()
                                                          .getService(SecurityService.class);
        Collection userCollection = null;
        User user = new User();

        try {
            userCollection = ss.getUsersByUsername("admin");
        } catch (kacang.services.security.SecurityException e) {
            e.printStackTrace();
        }

        for (Iterator itc = userCollection.iterator(); itc.hasNext();) {
            user = (User) itc.next();
        }

        try {
            ContentPublisher cp = (ContentPublisher) app.getModule(ContentPublisher.class);

            ContentObject root = cp.viewTreeWithOrphans(ContentManager.CONTENT_TREE_ROOT_ID,
                    new String[] { Section.class.getName() }, "Create",
                    user.getId());

            Collection children = root.getChildren();

            for (Iterator i = children.iterator(); i.hasNext();) {
                ContentObject co = (ContentObject) i.next();

                if (co.getName().equalsIgnoreCase(app.getMessage(
                                "maildaemon.stub.addarticle.label.default.section"))) {
                    testSection = true;
                } else {
                }
            }
        } catch (Exception e) {
            Log.getLog(getClass()).error("Error retrieving sections", e);
        }

        if (!testSection) {
            //mail module is not found inside the section therefore create one
            ContentManager module = (ContentManager) Application.getInstance()
                                                                .getModule(ContentManager.class);
            Section section = new Section();
            section.setName(app.getMessage(
                    "maildaemon.stub.addarticle.label.default.section"));
            section.setParentId(ContentManager.CONTENT_TREE_ROOT_ID);

            try {
                module.createNew(section, user);
                module.submitForApproval(section.getId(), user);

                if (module.hasPermission(section, user.getId(),
                            module.USE_CASE_APPROVE)) {
                    // approve
                    section.setCheckedOut(false);
                    section.setSubmitted(true);
                    module.approve(section, user);

                    // publish
                    module.publish(section.getId(), false, user);

                    // return contentObject;
                }
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            } catch (ContentException e) {
                e.printStackTrace();
            }
        }

        return "";
    }

     public String getHeaderInfo(){



          return "";
    }

}
