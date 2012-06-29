package com.tms.elearning.folder.ui;

import com.tms.elearning.folder.model.Folder;
import com.tms.elearning.folder.model.FolderModule;
import com.tms.elearning.folder.model.FolderModuleDao;

import kacang.Application;

import kacang.model.DaoException;

import kacang.services.security.User;

import kacang.stdui.*;

import kacang.stdui.validator.ValidatorMessage;
import kacang.stdui.validator.ValidatorNotEmpty;

import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.ui.WidgetManager;

import kacang.util.Log;
import kacang.util.UuidGenerator;

import java.sql.Date;

import java.text.SimpleDateFormat;

import java.util.*;
import java.io.Serializable;

import org.apache.commons.collections.SequencedHashMap;


/**
 * Created by IntelliJ IDEA.
 * User: tirupati
 * Date: Oct 25, 2004
 * Time: 1:19:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class FolderAddForm extends Form implements Serializable {
    public static final String FORWARD_SUCCESS = "success";
    public static final String FORWARD_ERROR = "error";
    protected TextField name;
    protected SelectBox courseId;
    protected RichTextBox introduction;
    protected CheckBox ispublic;
    protected Button submit;
    protected Button cancel;
    private String id;
    protected ValidatorMessage vmsg_folderExists;

    String courseid;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDefaultTemplate() {
        return "elearning/folderForm";
    }

    public void init() {
        super.init();
        initForm();
    }

    public void onRequest(Event evt) {
        super.onRequest(evt);
        courseid = evt.getRequest().getParameter("cid");
        setMethod("post");
                
        initForm();
    }

    public void initForm() {

        WidgetManager manager = getWidgetManager();
        User user = manager.getUser();
        String userId = user.getUsername();
        addChild(new Label("l1", "Name"));
        name = new TextField("name");
        name.setMaxlength("50");
        name.addChild(vmsg_folderExists = new ValidatorMessage(
                    "vmsg_folderExists"));

        ValidatorNotEmpty vne = new ValidatorNotEmpty("name",
                "");
        name.addChild(vne);
        addChild(name);

        addChild(new Label("l2", "Course"));
        courseId = new SelectBox("courseId");
        courseId.setOptions("-1=--- Course ---");

        FolderModuleDao dao = (FolderModuleDao) Application.getInstance()
                                                           .getModule(FolderModule.class)
                                                           .getDao();

        try {
            Collection col = dao.getCourses(userId);
            Map hash = new SequencedHashMap();

            for (Iterator iterator = col.iterator(); iterator.hasNext();) {
                Folder temp = (Folder) iterator.next();
                hash.put(temp.getCourseId(), temp.getCourseName());
            }

            courseId.setOptionMap(hash);
        } catch (DaoException e) {
            Log.getLog(getClass()).error(e.toString());
        }

        addChild(courseId);
        courseId.addSelectedOption(courseid);
        addChild(new Label("l3", "Introduction"));
        introduction = new RichTextBox("introduction");
        introduction.setMaxlength("2000");
        addChild(introduction);

        ispublic = new CheckBox("ispublic");
        addChild(ispublic);

        submit = new Button("submit", "Submit");
        addChild(submit);
        cancel = new Button("cancel", "Cancel");
        addChild(cancel);
    }

    public Forward onSubmit(Event evt) {
        super.onSubmit(evt);
        FolderModule foldermodule = (FolderModule) Application.getInstance()
                                                                  .getModule(FolderModule.class);

        if (foldermodule.checkFolderExits((String) name.getValue(), (String) ((List)courseId.getValue()).get(0)) != null) {
            vmsg_folderExists.showError(Application.getInstance().getMessage("eLearning.module.label.moduleexist"));
            this.setInvalid(true);
        }

        return null;
    }

    public Forward onValidate(Event evt) {

        FolderModule foldermodule = (FolderModule) Application.getInstance()
                                                                  .getModule(FolderModule.class);
        WidgetManager manager = getWidgetManager();
        User user = manager.getUser();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date today = new Date(System.currentTimeMillis());
        String createdDate = formatter.format(today);

        Folder folder = new Folder();
        UuidGenerator uuid = UuidGenerator.getInstance();
        folder.setId(uuid.getUuid());

        folder.setName((String) name.getValue());

        String selected = (String) ((List) courseId.getValue()).get(0);
        folder.setCourseId(selected);

        folder.setIntroduction((String) introduction.getValue());
        folder.setCreatedByUser(user.getUsername());
        folder.setCreatedByUserId(user.getId());
        folder.setCreatedDate(createdDate);

        if ((ispublic != null) && ispublic.isChecked()) {
            folder.setIs_public("1");
        } else {
            folder.setIs_public("0");
        }

        foldermodule.addFolder(folder);

        return new Forward(FORWARD_SUCCESS);
    }

    public Forward onValidationFailed(Event evt) {
        String buttonClicked = findButtonClicked(evt);

        if (buttonClicked.equals(cancel.getAbsoluteName())) {
            return new Forward(FORWARD_ERROR);
        } else {
            return null;
        }
    }
}
