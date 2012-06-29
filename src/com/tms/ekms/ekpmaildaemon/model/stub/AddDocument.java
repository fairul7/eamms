package com.tms.ekms.ekpmaildaemon.model.stub;

import com.tms.cms.core.model.*;
import com.tms.cms.document.Document;
import com.tms.cms.section.Section;

import kacang.Application;

import kacang.services.security.SecurityService;
import kacang.services.security.User;

import kacang.services.storage.StorageFile;

import kacang.util.Log;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;


public class AddDocument implements MailStub {
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

        User admin = new User();
        User user = new User();

        try {
            userCollectionAdmin = ss.getUsersByUsername("admin");
            user = ss.getUser(contentMap.get("USERID").toString());
        } catch (kacang.services.security.SecurityException e) {
            e.printStackTrace();
        }

        //iterate through collection to get information
        for (Iterator itc = userCollectionAdmin.iterator(); itc.hasNext();) {
            //Object element = itc.next();
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
                                "maildaemon.stub.adddocument.label.default.section"))) {
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
                    "maildaemon.stub.adddocument.label.default.section"));
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
        Document document = new Document();

        document.setId("");
        document.setName(contentMap.get(app.getMessage(
                    "maildaemon.stub.adddocument.label.title")).toString());

        document.setStartDate(null);
        document.setEndDate(null);
        document.setParentId(section.getId());

        document.setAuthor(contentMap.get(app.getMessage(
                    "maildaemon.stub.adddocument.label.author")).toString());
        document.setSummary(contentMap.get(app.getMessage(
                    "maildaemon.stub.adddocument.label.abstract")).toString());

        document.setApproved(false);

        document.setPublishUser(user.getUsername());
        document.setPublishUserId(user.getId());

        //do get attachment from email end
        StorageFile sf;

        sf = new StorageFile(contentMap.get("FILENAME").toString());

        if (contentMap.get("BINARY_ATTACHMENT") == null) {
            String tempByte = contentMap.get("ATTACHMENT").toString();

            sf.setInputStream(new ByteArrayInputStream(tempByte.getBytes()));
        }
        else {
            sf.setInputStream((InputStream) contentMap.get("BINARY_ATTACHMENT"));
        }

        document.setStorageFile(sf);
        document.setFileName(contentMap.get("FILENAME").toString());

        if ((contentMap.get("FILE_SIZE") != null) &&
                (contentMap.get("FILE_TYPE") != null)) {
            document.setFileSize(Long.parseLong(contentMap.get("FILE_SIZE")
                                                          .toString()));
            document.setContentType(contentMap.get("FILE_TYPE").toString());
        }

        //do get attachment from email end
        try {
            module.createNew(document, user);

            module.submitForApproval(document.getId(), user);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
            errorMsg += app.getMessage(
                "maildaemon.stub.adddocument.label.invalidkey");
        } catch (ContentException e) {
            e.printStackTrace();
            errorMsg += app.getMessage(
                "maildaemon.stub.adddocument.label.invalidcontent");
        }

        if ("".equals(errorMsg)) {
            errorMsg = "\n" + app.getMessage("maildaemon.label.successful");
            Log.getLog(getClass()).info("SUCCESSFUL ADDED DOCUMENT <EKPMAILDAEMON>");
        }

        contentMap.put("ERROR", (String) errorMsg);

        return contentMap; //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getSubjectPattern() {
        Application app = Application.getInstance();

        return app.getMessage("maildaemon.stub.adddocument.label.adddocument");
    }

    public String[] getBodyPattern() {
        Application app = Application.getInstance();
        String[] tempPattern = {
            app.getMessage("maildaemon.stub.adddocument.label.title"),
            app.getMessage("maildaemon.stub.adddocument.label.author"),
            app.getMessage("maildaemon.stub.adddocument.label.abstract")
        };

        return tempPattern;
    }

    public String getInfo() {
        return "";
    }

 public String getHeaderInfo(){



          return Application.getInstance().getMessage("maildaemon.stub.header.adddocument.message");
    }

}
