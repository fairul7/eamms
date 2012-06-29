package com.tms.elearning.folder.ui;

/**
 * Created by IntelliJ IDEA.
 * User: tirupati
 * Date: Oct 20, 2004
 * Time: 7:01:35 PM
 * To change this template use File | Settings | File Templates.
 */
import kacang.Application;
import kacang.stdui.*;
import kacang.stdui.validator.ValidatorMessage;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.services.security.User;
import kacang.util.Log;
import kacang.model.DataObjectNotFoundException;
import kacang.model.DaoException;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.ui.WidgetManager;
import com.tms.elearning.folder.model.FolderModule;
import com.tms.elearning.folder.model.Folder;
import com.tms.elearning.folder.model.FolderModuleDao;
import com.tms.elearning.course.model.CourseModule;

import java.util.*;
import java.text.SimpleDateFormat;
import java.sql.Date;
import java.io.Serializable;

import org.apache.commons.collections.SequencedHashMap;

public class FolderFormEdit extends Form implements Serializable {

    public static final String FORWARD_SUCCESS = "success";
    public static final String FORWARD_ERROR = "error";
    protected TextField name;
    protected SelectBox courseId;
    protected RichTextBox introduction;
    protected CheckBox ispublic;
    protected Button submit;
    protected Button cancel;

    private String id;
    private String moduleid;
    protected ValidatorMessage vmsg_folderExists;

    

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDefaultTemplate() {
        return "elearning/folderEditForm";
    }

     public void init() {
        super.init();
        initForm();
    }

    public void onRequest(Event evt) {
        super.onRequest(evt);
        initForm();
    }
    public void initForm() {

        setMethod("post");
        WidgetManager manager = getWidgetManager();
        User user = manager.getUser();
        String userId=user.getUsername();
        addChild(new Label("l1", "Name"));
        name = new TextField("name");
        name.setMaxlength("50");
        name.addChild(vmsg_folderExists = new ValidatorMessage("vmsg_folderExists"));
        ValidatorNotEmpty vne = new ValidatorNotEmpty("name", "");
        name.addChild(vne);
        addChild(name);

        addChild(new Label("l2", "Course"));
        courseId=new SelectBox("courseId");
        courseId.setOptions("-1=--- Course ---");
        FolderModuleDao dao = (FolderModuleDao) Application.getInstance().getModule(FolderModule.class).getDao();
        try {
            Collection col = dao.getCourses(userId);
            Map hash = new SequencedHashMap();
            for (Iterator iterator = col.iterator(); iterator.hasNext();) {
                Folder temp =  (Folder) iterator.next();
                //courseId.setOptions(temp.getCourseId()+"="+temp.getCourseName());
                 hash.put(temp.getCourseId(),temp.getCourseName());
            }
           courseId.setOptionMap(hash);
        } catch (DaoException e) {
            Log.getLog(getClass()).error(e.toString());
        }
        addChild(courseId);

        addChild(new Label("l3", "Introduction"));
        introduction = new RichTextBox("introduction");
        introduction.setMaxlength("2000");
        addChild(introduction);


        ispublic = new CheckBox("ispublic");
        addChild(ispublic);


        submit = new Button("submit", "Update");
        addChild(submit);
        cancel = new Button("cancel","Cancel");
        addChild(cancel);

        if (null != getId()) {
            Application application = Application.getInstance();
            FolderModule module = (FolderModule)application.getModule(FolderModule.class);

            try {
                // load module
                Folder folder = module.loadFolder(getId());
                setModuleid(folder.getId());
                name.setValue(folder.getName());
                courseId.setSelectedOption(folder.getCourseId());
                introduction.setValue(folder.getIntroduction());

                boolean publicBool;
                if( "1".equals(folder.getIs_public()))
                publicBool = true;
                else
                publicBool = false;

                ispublic.setChecked(publicBool);

           } catch (DataObjectNotFoundException e) {
                Log.getLog(getClass()).error("Module " + getId() + " not found");
                init();
            }
        }
    }

    public Forward onSubmit(Event evt) {
        super.onSubmit(evt);
        Application application = Application.getInstance();
        FolderModule module = (FolderModule)application.getModule(FolderModule.class);
        if (module.checkFolderExitsModuleId((String) name.getValue(), (String) ((List)courseId.getValue()).get(0), getModuleid()) != null) {
            vmsg_folderExists.showError(Application.getInstance().getMessage("eLearning.module.label.moduleexist"));
		    this.setInvalid(true);
        }
        return null;
    }
    public Forward onValidate(Event evt) {
        Application application = Application.getInstance();
        FolderModule module = (FolderModule)application.getModule(FolderModule.class);

        SimpleDateFormat formatter= new SimpleDateFormat ("dd/MM/yyyy h:mm a");
        Date today=new Date(System.currentTimeMillis());
        String createdDate = formatter.format(today);

        WidgetManager manager = getWidgetManager();
        User user = manager.getUser();
        Folder folder = new Folder();

        folder.setId(getId());
        folder.setName((String)name.getValue());
        String  selectedCourse = (String) ((List)courseId.getValue()).get(0);
        folder.setCourseId(selectedCourse);
        folder.setIntroduction((String)introduction.getValue());
        folder.setCreatedByUser(user.getUsername());
        folder.setCreatedByUserId(user.getId());
        folder.setCreatedDate(createdDate);


        if (ispublic != null && ispublic.isChecked()) {
           folder.setIs_public("1");
        }else {
           folder.setIs_public("0");

        }

         module.updateFolder(folder);

         return new Forward("updated");
     }
    public Forward onValidationFailed(Event evt) {
        String buttonClicked = findButtonClicked(evt);
        if (buttonClicked.equals(cancel.getAbsoluteName())) {
            return new Forward("cancel");
        } else {
            return null;
        }
    }

    public String getModuleid() {
        return moduleid;
    }

    public void setModuleid(String moduleid) {
        this.moduleid = moduleid;
    }


}

