package com.tms.collab.messaging.ui;

import com.tms.collab.messaging.model.MessagingModule;
import com.tms.collab.messaging.model.Util;
import kacang.Application;
import kacang.services.security.User;
import kacang.services.storage.StorageException;
import kacang.services.storage.StorageFile;
import kacang.services.storage.StorageService;
import kacang.stdui.Button;
import kacang.stdui.FileUpload;
import kacang.stdui.Form;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;
import org.apache.commons.collections.SequencedHashMap;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;

public class AttachmentForm extends Form {

    public static final String FORWARD_ERROR = "error";
    public static final String FORWARD_SUCCESS = "success";
    public static final String FORWARD_DONE = "done";

    private FileUpload fuAttach1;
    private FileUpload fuAttach2;
    private FileUpload fuAttach3;

    private Button btUpload;
    private Button btDone;

    private Map attachmentMap;

    public void init() {
        setMethod("POST");

        fuAttach1 = new FileUpload("attach1");
        addChild(fuAttach1);

        fuAttach2 = new FileUpload("attach2");
        addChild(fuAttach2);

        fuAttach3 = new FileUpload("attach3");
        addChild(fuAttach3);

        btUpload = new Button("btUpload", Application.getInstance().getMessage("messaging.label.upload","upload"));
        btDone = new Button("btDone", Application.getInstance().getMessage("messaging.label.done","done"));
        addChild(btUpload);
        addChild(btDone);
    }

    public void onRequest(Event event) {
        attachmentMap = getAttachmentMapFromSession(event);
    }

    public Forward onValidate(Event event) {
        Log log;
        log = Log.getLog(getClass());

        try {
            if("remove".equals(event.getType())) {
                // remove attachment
                doRemove(event);
                return new Forward(FORWARD_SUCCESS);

            } else {
                String button = findButtonClicked(event);
                button = button == null ? "" : button;

                if (button.endsWith("btUpload")) {
                    // process upload
                    doUpload(event);
                    return new Forward(FORWARD_SUCCESS);

                } else if (button.endsWith("btDone")) {
                    // process done
                    return new Forward(FORWARD_DONE);

                } else {
                    String msg = "Unknown button '" + button + "' pressed!";
                    log.error(msg);
                    event.getRequest().getSession().setAttribute("error", msg);
                    return new Forward(FORWARD_ERROR);
                }
            }

        } catch (IOException e) {
            Log.getLog(getClass()).error(e.getMessage(), e);
            event.getRequest().getSession().setAttribute("error", e);
            return new Forward(FORWARD_ERROR);

        } catch (StorageException e) {
            Log.getLog(getClass()).error(e.getMessage(), e);
            event.getRequest().getSession().setAttribute("error", e);
            return new Forward(FORWARD_ERROR);
        }

    }

    private void doRemove(Event event) throws StorageException {
        String encodedKey, key;
        StorageService ss;
        StorageFile sf;
        User user;

        encodedKey = event.getParameter("key");
        key = Util.decodeHex(encodedKey);

        user = Util.getUser(event);
        ss = (StorageService) Application.getInstance().getService(StorageService.class);
        attachmentMap = getAttachmentMapFromSession(event);
        attachmentMap.remove(key);
        setAttachmentMapToSession(event, attachmentMap);
        
        sf = new StorageFile(MessagingModule.ROOT_PATH + "/" + user.getId() + "/temp/" + key);
        ss.delete(sf);
    }

    private void doUpload(Event event) throws IOException, StorageException {
        StorageService ss;
        StorageFile sf;
        User user;

        user = Util.getUser(event);
        ss = (StorageService) Application.getInstance().getService(StorageService.class);
        attachmentMap = getAttachmentMapFromSession(event);

        // attachment 1
        sf = fuAttach1.getStorageFile(event.getRequest());
        if(sf!=null) {
            sf.setParentDirectoryPath(MessagingModule.ROOT_PATH + "/" + user.getId() + "/temp/");
            ss.store(sf);
            attachmentMap.put(sf.getName(), "-data in storage service-");
        }

        // attachment 2
        sf = fuAttach2.getStorageFile(event.getRequest());
        if(sf!=null) {
            sf.setParentDirectoryPath(MessagingModule.ROOT_PATH + "/" + user.getId() + "/temp/");
            ss.store(sf);
            attachmentMap.put(sf.getName(), "-data in storage service-");
        }

        // attachment 3
        sf = fuAttach3.getStorageFile(event.getRequest());
        if(sf!=null) {
            sf.setParentDirectoryPath(MessagingModule.ROOT_PATH + "/" + user.getId() + "/temp/");
            ss.store(sf);
            attachmentMap.put(sf.getName(), "-data in storage service-");
        }

        // reset to session to propagate when clustering 
        setAttachmentMapToSession(event, attachmentMap);

    }

    public String getTemplate() {
        return "messaging/attachmentForm";
    }

    public static Map getAttachmentMapFromSession(Event event) {
        Map attachmentMap;
        HttpSession session;

        session = event.getRequest().getSession();
        attachmentMap = (Map) session.getAttribute(MessagingModule.ATTACHMENT_MAP_SESSION_ATTRIBUTE);
        if(attachmentMap==null) {
            attachmentMap = new SequencedHashMap();
            session.setAttribute(MessagingModule.ATTACHMENT_MAP_SESSION_ATTRIBUTE, attachmentMap);
        }
        return attachmentMap;
    }

    public static void setAttachmentMapToSession(Event event, Map attachmentMap) {
        HttpSession session;

        session = event.getRequest().getSession();
        if(attachmentMap != null) {
            session.setAttribute(MessagingModule.ATTACHMENT_MAP_SESSION_ATTRIBUTE, attachmentMap);
        }
    }

    // === [ getters/setters] ==================================================
    public FileUpload getFuAttach1() {
        return fuAttach1;
    }

    public void setFuAttach1(FileUpload fuAttach1) {
        this.fuAttach1 = fuAttach1;
    }

    public FileUpload getFuAttach2() {
        return fuAttach2;
    }

    public void setFuAttach2(FileUpload fuAttach2) {
        this.fuAttach2 = fuAttach2;
    }

    public FileUpload getFuAttach3() {
        return fuAttach3;
    }

    public void setFuAttach3(FileUpload fuAttach3) {
        this.fuAttach3 = fuAttach3;
    }

    public Button getBtUpload() {
        return btUpload;
    }

    public void setBtUpload(Button btUpload) {
        this.btUpload = btUpload;
    }

    public Button getBtDone() {
        return btDone;
    }

    public void setBtDone(Button btDone) {
        this.btDone = btDone;
    }

    public Map getAttachmentMap() {
        return attachmentMap;
    }

    public void setAttachmentMap(Map attachmentMap) {
        this.attachmentMap = attachmentMap;
    }

}
