package com.tms.elearning.lesson.ui;

import com.tms.elearning.core.ui.DocArtisPopupSelectBox;
import com.tms.elearning.folder.model.Folder;
import com.tms.elearning.folder.model.FolderModule;
import com.tms.elearning.folder.model.FolderModuleDao;
import com.tms.elearning.lesson.model.Lesson;
import com.tms.elearning.lesson.model.LessonDao;
import com.tms.elearning.lesson.model.LessonModule;

import kacang.Application;

import kacang.model.DaoException;

import kacang.services.security.User;

import kacang.services.storage.StorageException;
import kacang.services.storage.StorageFile;

import kacang.stdui.*;

import kacang.stdui.validator.ValidatorMessage;
import kacang.stdui.validator.ValidatorNotEmpty;

import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.ui.WidgetManager;

import kacang.util.Log;
import kacang.util.UuidGenerator;

import org.apache.commons.collections.SequencedHashMap;

import java.io.IOException;
import java.io.Serializable;

import java.sql.Date;

import java.text.SimpleDateFormat;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * Created by IntelliJ IDEA.
 * User: tirupati
 * Date: Oct 26, 2004
 * Time: 4:48:18 PM
 * To change this template use File | Settings | File Templates.
 */
public class LessonForm extends Form implements Serializable {
    protected TextField name;
    protected SelectBox courseId;
    protected SelectBox folderId;
    protected RichTextBox brief;
    protected CheckBox ispublic;
    protected Button submit;
    protected Button cancel;
    private String id;
    protected FileUpload attachment;
    protected ValidatorMessage vmsg_lessonExists;
    protected ValidatorMessage vmsg_moduleExists;
    protected ValidatorMessage vmsg_courseNotSelected;
    String courseid;
    protected DocArtisPopupSelectBox eLearningFiles;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDefaultTemplate() {
        return "elearning/lessonForm";
    }

    public void init(Event evt) {
        super.init();
        initForm(evt);
    }

    public void onRequest(Event evt) {
        super.onRequest(evt);
        setMethod("post");
        courseid = evt.getRequest().getParameter("cid");

        initForm(evt);
    }

    public void initForm(Event evt) {
        WidgetManager manager = getWidgetManager();
        User user = manager.getUser();
        String userId = user.getUsername();
        Application application = Application.getInstance();
        addChild(new Label("l1",
                application.getMessage("eLearning.lesson.label.lessonname")));
        name = new TextField("name");
        name.addChild(vmsg_lessonExists = new ValidatorMessage(
                    "vmsg_lessonExists"));
        name.setSize("40");
        name.setMaxlength("255");
        addChild(name);

        addChild(new Label("l2", "Course"));
        courseId = new SelectBox("courseId");
        courseId.setOptions("-1=" +
            application.getMessage("eLearning.topic.label.coursemenu"));

        LessonDao dao = (LessonDao) Application.getInstance()
                                               .getModule(LessonModule.class)
                                               .getDao();

        try {
            Collection col = null;

            if (courseid != null) {
                col = dao.getCourses(courseid, 1);
            } else {
                col = dao.getCourses();
            }

            Map hash = new SequencedHashMap();
            hash.put("-1",
                application.getMessage("eLearning.topic.label.coursemenu"));

            for (Iterator iterator = col.iterator(); iterator.hasNext();) {
                Lesson temp = (Lesson) iterator.next();
                hash.put(temp.getCourseId(), temp.getCourseName());
            }

            courseId.addChild(vmsg_courseNotSelected = new ValidatorMessage("courssenotselectd"));
            courseId.setOptionMap(hash);
        } catch (DaoException e) {
            Log.getLog(getClass()).error(e.toString());
        }

        courseId.setOnChange("document.forms['" + getAbsoluteName() +
            "'].submit()");
        addChild(courseId);
        courseId.addSelectedOption(courseid);
        addChild(new Label("l3", "Folder"));
        folderId = new SelectBox("folderId");
        folderId.setOptions("-1=" +
            application.getMessage("eLearning.lesson.label.modulemenu"));

        try {
            Collection col = null;

            if (courseid != null) {
                col = dao.getFolders(courseid, 1);
            } else {
                col = dao.getFolders();
            }

            Map hash = new SequencedHashMap();
            hash.put("-1",
                            application.getMessage("eLearning.lesson.label.modulemenu"));


            /*for (Iterator iterator = col.iterator(); iterator.hasNext();) {
                Lesson temp = (Lesson) iterator.next();
                hash.put(temp.getFolderId(), temp.getFolderName());
            }*/

            folderId.setOptionMap(hash);
            folderId.addChild(vmsg_moduleExists =  new ValidatorMessage(
                    "vmsg_moduleExists"));
        } catch (DaoException e) {
            Log.getLog(getClass()).error(e.toString());
        }


        addChild(folderId);

        addChild(new Label("l4",
                application.getMessage("eLearning.lesson.label.brief")));
        brief = new RichTextBox("brief");
        brief.setCols("8");
        brief.setRows("25");

        ((RichTextBox) brief).setLinkUrl(
            evt.getRequest().getContextPath() + "/cmsadmin/content/linkSelectorNew.jsp");

        ((RichTextBox) brief).setImageUrl(
            evt.getRequest().getContextPath() + "/cmsadmin/content/imageSelectorNew.jsp");

        addChild(brief);

        ispublic = new CheckBox("ispublic");
        addChild(ispublic);

        attachment = new FileUpload("attachment");
        addChild(attachment);

        eLearningFiles = new DocArtisPopupSelectBox("eLearningFiles");
        addChild(eLearningFiles);
        eLearningFiles.init();

        submit = new Button("submit",
                application.getMessage("eLearning.lesson.label.submit"));
        addChild(submit);
        cancel = new Button("cancel",
                application.getMessage("eLearning.lesson.label.cancel"));
        addChild(cancel);
    }

    public Forward onSubmit(Event evt) {
        WidgetManager manager = getWidgetManager();
        User user = manager.getUser();
        String userId = user.getUsername();
        String button = findButtonClicked(evt);
        super.onSubmit(evt);

        LessonModule lessonmodule = (LessonModule) Application.getInstance()
                                                              .getModule(LessonModule.class);

        Application application = Application.getInstance();
        FolderModuleDao modDao = (FolderModuleDao) Application.getInstance()
                                                              .getModule(FolderModule.class)
                                                              .getDao();

        if (lessonmodule.checkLessonExists((String) name.getValue(),(String) ((List)courseId.getValue()).get(0),(String) ((List)folderId.getValue()).get(0)) != null) {
            vmsg_lessonExists.showError(application.getMessage(
                    "eLearning.lesson.label.samelessonexist"));
            this.setInvalid(true);
        }


        if (!submit.getAbsoluteName().equals(button)) {
            try {
                Collection courseList = (Collection) courseId.getValue();
                String selected_course = (String) courseList.iterator().next();

                Collection mods = modDao.getModuleList(selected_course, userId);
                folderId.removeAllOptions();
                folderId.setOptions("-1=" +
                    application.getMessage("eLearning.lesson.label.modulemenu"));

                for (Iterator i = mods.iterator(); i.hasNext();) {
                    Folder temp = (Folder) i.next();
                    folderId.setOptions(temp.getId() + "=" +
                        temp.getFolderName());
                }
            } catch (DaoException e) {
                Log.getLog(getClass()).error(e.toString(), e);
            }

            setInvalid(true);
        } else {

        	 if(name.getValue().toString().trim().length()<=0){
        		 name.setInvalid(true);
        		 this.setInvalid(true);
        	 }
        	 
            if("-1".equals((String) ((List)folderId.getValue()).get(0))){

                    vmsg_moduleExists.showError(application.getMessage(
                            "eLearning.module.label.select"));
                    this.setInvalid(true);

                }

            if("-1".equals((String)((List)courseId.getValue()).get(0))){

                vmsg_courseNotSelected.showError("");
                this.setInvalid(true);
            }




            Collection selectedList = (Collection) courseId.getValue();
            String selected = (String) selectedList.iterator().next();

            if (selected.equalsIgnoreCase("-1")) {
                this.setInvalid(true);
            }
        }

        return null;
    }

    public Forward onValidate(Event evt) {
        LessonModule lessonmodule = (LessonModule) Application.getInstance()
                                                              .getModule(LessonModule.class);
        WidgetManager manager = getWidgetManager();
        User user = manager.getUser();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date today = new Date(System.currentTimeMillis());
        String createdDate = formatter.format(today);

        Lesson lesson = new Lesson();
        UuidGenerator uuid = UuidGenerator.getInstance();
        String identifier = uuid.getUuid();

        lesson.setId(identifier);
        lesson.setName((String) name.getValue());

        String selectedCourse = (String) ((List) courseId.getValue()).get(0);
        lesson.setCourseId(selectedCourse);

        String selectedFolder = (String) ((List) folderId.getValue()).get(0);
        lesson.setFolderId(selectedFolder);




        lesson.setBrief((String) brief.getValue());
        lesson.setCreatedByUser(user.getUsername());
        lesson.setCreatedByUserId(user.getId());
        lesson.setCreatedDate(createdDate);

        if ((ispublic != null) && ispublic.isChecked()) {
            lesson.setIs_public("1");
        } else {
            lesson.setIs_public("0");
        }

        try {
            if (attachment.getValue() != null) {
                lesson.setStorageFile(attachment.getStorageFile(
                        evt.getRequest()));

                StorageFile file = new StorageFile("/" + "lesson" + "/" +
                        identifier, lesson.getStorageFile());
                lesson.setFilePath(file.getAbsolutePath());

                StorageFile f1 = attachment.getStorageFile(evt.getRequest());
                String str = f1.getAbsolutePath();
                String fn = str.substring(1, str.length());
                lesson.setFileName(fn);
            }
        } catch (IOException e) {
            e.printStackTrace(); //To change body of catch statement use File | Settings | File Templates.
        } catch (StorageException e) {
            e.printStackTrace(); //To change body of catch statement use File | Settings | File Templates.
        }

        String selectedDocArticles = "";

        for (int i = 0; i < eLearningFiles.getIds().length; i++) {
            selectedDocArticles += (eLearningFiles.getIds()[i] + ",");
        }

        lesson.setAssociatedoc(selectedDocArticles);

        lessonmodule.addLesson(lesson);

        return new Forward("added");
    }

    public Forward onValidationFailed(Event evt) {
        String buttonClicked = findButtonClicked(evt);

        if (cancel.getAbsoluteName().equals(buttonClicked)) {
            return new Forward("cancel");
        } else {
            return null;
        }
    }
}
