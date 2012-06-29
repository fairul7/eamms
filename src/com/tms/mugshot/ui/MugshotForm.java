package com.tms.mugshot.ui;

import com.tms.mugshot.model.Mugshot;
import com.tms.mugshot.model.MugshotModule;
import kacang.Application;
import kacang.services.security.Profileable;
import kacang.services.security.User;
import kacang.services.storage.StorageFile;
import kacang.stdui.Button;
import kacang.stdui.FileUpload;
import kacang.stdui.Form;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.ui.Widget;
import kacang.util.Log;

import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: blake
 * Date: Jul 6, 2005
 * Time: 2:49:05 PM
 */
public class MugshotForm extends Form implements Profileable{
    public static final String DEFAULT_NAME = "myMugShot";
    public static final String DEFAULT_TEMPLATE = "mugshot/mugshot";
    public static final String FORWARD_FILESIZE = "filesize invalid";
    public static final String FORWARD_FILETYPE = "file is not an image";

    protected User user;
    protected FileUpload fileUpload;
    protected StorageFile sf;
    protected String mugshotpath;
    protected Button remove;

    public void init(){
        setMethod("POST");

        fileUpload = new FileUpload("fileUpload");
        addChild(fileUpload);

        remove = new Button("remove", Application.getInstance().getMessage("mugshot.button.remove"));
        addChild(remove);
    };

    public String getMugshotpath() {
        return mugshotpath;
    }

    public void setMugshotpath(String mugshotpath) {
        this.mugshotpath = mugshotpath;
    }

    public String getProfileableLabel() {
        return  Application.getInstance().getMessage("mugshot.defaultlabel");
    }

    public String getProfileableName() {
        return DEFAULT_NAME;
    }

    public Widget getWidget() {
        return this;
    }

    public void init(User user) {
        this.user = user;

    }

    public void onRequest(Event evt) {
        MugshotModule mm = (MugshotModule) Application.getInstance().getModule(MugshotModule.class);
        Mugshot mug = null;
        mug = mm.get(getWidgetManager().getUser().getId());
        fileUpload.setInvalid(false);
        if(mug!=null){
            mugshotpath = "/storage"+mug.getFilePath();
        }else mugshotpath = "";
    }

    public void process(User user) {
        if(sf!=null){
            MugshotModule mm = (MugshotModule) Application.getInstance().getModule(MugshotModule.class);
            Mugshot mug = new Mugshot();
            sf.setParentDirectoryPath(MugshotModule.ROOT_DIR +"/"+ user.getId());
            mug.setUserId(user.getId());
            mug.setFilePath(sf.getAbsolutePath());
            mm.save(mug, sf);
            mugshotpath = "/storage"+mug.getFilePath();
        }
    }

    public String getDefaultTemplate() {
        return DEFAULT_TEMPLATE;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public FileUpload getFileUpload() {
        return fileUpload;
    }

    public void setFileUpload(FileUpload fileUpload) {
        this.fileUpload = fileUpload;
    }

    public Button getRemove() {
        return remove;
    }

    public void setRemove(Button remove) {
        this.remove = remove;
    }


    public Forward onSubmit(Event evt) {
        Forward forward = super.onSubmit(evt);
        try {
            sf = fileUpload.getStorageFile(evt.getRequest());
            if(sf!=null){
                // check if is image
                if(!sf.getContentType().startsWith("image")){
                    sf = null;
                    fileUpload.setInvalid(true);
                    return new Forward(FORWARD_FILETYPE);
                }

                if(sf.getSize() > 100000){
                    sf = null;
                    fileUpload.setInvalid(true);
                    return new Forward(FORWARD_FILESIZE);
                }
            }
        } catch (IOException e) {
            Log.getLog(getClass()).error("Error in upload mugshot", e);
        }

        if(remove.getAbsoluteName().equals(findButtonClicked(evt))){
            MugshotModule mm = (MugshotModule) Application.getInstance().getModule(MugshotModule.class);
            Mugshot mug = mm.get(getWidgetManager().getUser().getId());
            mm.remove(mug);
            mugshotpath = "";
        }
        return forward;
    }
}
