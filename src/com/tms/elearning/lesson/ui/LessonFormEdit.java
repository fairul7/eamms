package com.tms.elearning.lesson.ui;

import com.tms.cms.core.model.ContentException;
import com.tms.cms.core.model.ContentManager;
import com.tms.cms.core.model.ContentObject;
import com.tms.cms.document.Document;
import com.tms.cms.article.Article;

import com.tms.collab.project.ui.DocumentPopupSelectBox;

import com.tms.elearning.folder.model.Folder;
import com.tms.elearning.folder.model.FolderModule;
import com.tms.elearning.folder.model.FolderModuleDao;
import com.tms.elearning.lesson.model.Lesson;
import com.tms.elearning.lesson.model.LessonDao;
import com.tms.elearning.lesson.model.LessonModule;
import com.tms.elearning.core.ui.DocArtisPopupSelectBox;

import kacang.Application;

import kacang.model.DaoException;
import kacang.model.DataObjectNotFoundException;

import kacang.services.security.User;

import kacang.services.storage.StorageException;
import kacang.services.storage.StorageFile;
import kacang.services.storage.StorageService;

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

import java.util.*;


/**
 * Created by IntelliJ IDEA.
 * User: tirupati
 * Date: Oct 26, 2004
 * Time: 5:05:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class LessonFormEdit extends Form implements Serializable {
    protected TextField name;
    protected SelectBox courseId;
    protected SelectBox folderId;
    protected RichTextBox brief;
    protected CheckBox ispublic;
    protected Button submit;
    protected Button cancel;
    private String id;
    protected ValidatorMessage vmsg_lessonExists;
    protected ValidatorMessage vmsg_moduleExists;
    protected String filePath;
    protected String fileName;
    protected String lessonid;
    protected FileUpload attachment;
    protected ValidatorMessage courseIdValid;
    protected ValidatorMessage folderIdValid;
    protected ValidatorMessage vmsg_courseNotSelected;


    protected DocArtisPopupSelectBox eLearningFiles;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDefaultTemplate() {
        return "elearning/lessonEditForm";
    }

    public void init(Event evt) {
        super.init();
        initForm(evt);
    }

    public void onRequest(Event evt) {
        super.onRequest(evt);
        setMethod("post");
        initForm(evt);
    }

    public void initForm(Event evt) {
        Application application = Application.getInstance();
        WidgetManager manager = getWidgetManager();
        User user = manager.getUser();
        String userId = user.getUsername();
        LessonModule lessonmodule = (LessonModule) Application.getInstance()
                                                                  .getModule(LessonModule.class);

        addChild(new Label("l1",
                application.getMessage("eLearning.lesson.label.lessonname")));
        name = new TextField("name");

        ValidatorNotEmpty v_lesson = new ValidatorNotEmpty("name", "");
        name.addChild(v_lesson);
        name.addChild(vmsg_lessonExists = new ValidatorMessage(
                    "vmsg_lessonExists"));
        name.setSize("40");
        name.setMaxlength("255");
        addChild(name);

        addChild(new Label("l2", "Course"));
        courseId = new SelectBox("courseId");
        courseId.addChild(courseIdValid = new ValidatorMessage(
                    application.getMessage(
                        "eLearning.lesson.label.courseidexist")));

        LessonDao dao = (LessonDao) Application.getInstance()
                                               .getModule(LessonModule.class)
                                               .getDao();

        try {
            Collection col = dao.getCourses(userId);
            Map hash = new SequencedHashMap();
            hash.put("-1",
                application.getMessage("eLearning.topic.label.coursemenu"));

            for (Iterator iterator = col.iterator(); iterator.hasNext();) {
                Lesson temp = (Lesson) iterator.next();

                //courseId.setOptions(temp.getCourseId()+"="+temp.getCourseName());
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

        addChild(new Label("l3", "Folder"));
        folderId = new SelectBox("folderId");
        folderId.addChild(folderIdValid = new ValidatorMessage(
                    application.getMessage(
                        "eLearning.lesson.label.moduleidexist")));

        try {
            Collection collection = null;

            collection = dao.getFolders();

            Map hash = new SequencedHashMap();
            hash.put("-1",
                application.getMessage("eLearning.lesson.label.modulemenu"));

            for (Iterator iterator = collection.iterator(); iterator.hasNext();) {
                Lesson tempFolder = (Lesson) iterator.next();
                hash.put(tempFolder.getFolderId(), tempFolder.getFolderName());
            }

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

        submit = new Button("submit",
                application.getMessage("eLearning.lesson.label.update"));

        eLearningFiles = new DocArtisPopupSelectBox("eLearningFiles");
        addChild(eLearningFiles);
        eLearningFiles.init();

        addChild(submit);
        cancel = new Button("cancel",
                application.getMessage("elearning.lesson.label.cancel"));
        addChild(cancel);

        if (id != null) {
            try {
                // load lesson
                Lesson lesson = lessonmodule.loadLesson(getId());
                setLessonid(lesson.getId());
                name.setValue(lesson.getName());
                courseId.setSelectedOption(lesson.getCourseId());
                folderId.setSelectedOption(lesson.getFolderId());
                brief.setValue(lesson.getBrief());

                String tempAssociateDoc = "";

                if (lesson.getAssociatedoc() != null) {
                    tempAssociateDoc = lesson.getAssociatedoc();

                    StringTokenizer st = new StringTokenizer(tempAssociateDoc,
                            ",");

                    String[] eLearningFilesArray = new String[st.countTokens()];

                    int counting = 0;

                    while (st.hasMoreTokens()) {
                        String tempToken = "";
                        tempToken = st.nextToken();

                        if ((tempToken != null) && !(tempToken.equals("")) &&
                                !(tempToken.equals(","))) {
                            eLearningFilesArray[counting] = tempToken;
                            counting++;
                        }
                        
                    }
                    
                    eLearningFiles.setOptionMap(generateOptionMap(eLearningFilesArray));
                }

                filePath = lesson.getFilePath();
                fileName = lesson.getFileName();

                boolean publicBool;

                if ("1".equals(lesson.getIs_public())) {
                    publicBool = true;
                } else {
                    publicBool = false;
                }

                ispublic.setChecked(publicBool);
            } catch (DataObjectNotFoundException e) {
                Log.getLog(getClass()).error(application.getMessage(
                        "eLearning.lesson.label.lesson") + " " + getId() + " " +
                    application.getMessage("eLearning.lesson.label.notfound"));
                init();
            }
        }
    }

    public Forward onSubmit(Event evt) {
        super.onSubmit(evt);
        Application application = Application.getInstance();
        LessonModule lessonmodule = (LessonModule) Application.getInstance()
                                                                  .getModule(LessonModule.class);
        WidgetManager manager = getWidgetManager();
        User user = manager.getUser();
        FolderModuleDao modDao = (FolderModuleDao) Application.getInstance()
                                                              .getModule(FolderModule.class)
                                                              .getDao();
        String userId = user.getUsername();
        String button = findButtonClicked(evt);

          if (lessonmodule.checkLessonExistsWithLesson((String) name.getValue(),(String) ((List)courseId.getValue()).get(0),(String) ((List)folderId.getValue()).get(0), getLessonid()) != null) {
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
                courseIdValid.showError(application.getMessage(
                        "eLearning.lesson.label.courserequired"));
                this.setInvalid(true);
            }

            Collection selectedList2 = (Collection) folderId.getValue();
            String selected2 = (String) selectedList2.iterator().next();

            if (selected2.equalsIgnoreCase("-1")) {
                folderIdValid.showError(application.getMessage(
                        "eLearning.lesson.label.modulerequired"));
                this.setInvalid(true);
            }
        }

        return null;
    }

    public Forward onValidate(Event evt) {
        UuidGenerator uuid = UuidGenerator.getInstance();
        String identifier = uuid.getUuid();
        WidgetManager manager = getWidgetManager();
        User user = manager.getUser();
        LessonModule lessonmodule = (LessonModule) Application.getInstance()
                                                                  .getModule(LessonModule.class);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date today = new Date(System.currentTimeMillis());
        String createdDate = formatter.format(today);

        Lesson lesson = new Lesson();

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

        lesson.setId(getId());

        //erase all attachment
        //only erase attachment if old attachment exist and got new upload file
        if ((filePath != null) && !("".equals(filePath)) &&
                (attachment.getValue() != null)) {
            Application app = Application.getInstance();
            StorageService storage = (StorageService) app.getService(StorageService.class);
            String filterFilePath = filePath;

            int filterPoint = filePath.indexOf('/', 8);

            filterFilePath = filePath.substring(0, filterPoint);

            StorageFile sf = new StorageFile(filterFilePath);

            try {
                storage.delete(sf);
            } catch (StorageException e) {
                e.printStackTrace();
            }
        }

        //add new attachment
        StorageFile file = null;

        try {
            if ((getId() != null) && !("".equals(getId()))) {
                //old file exist, use old id , not using identifier
                if (attachment.getValue() != null) {
                    //new attachment found
                    lesson.setStorageFile(attachment.getStorageFile(
                            evt.getRequest()));

                    file = new StorageFile("/" + "lesson" + "/" + getId(),
                            lesson.getStorageFile());
                } else {
                    // else , new attachment is emtpy, therefore use old filepath and filename
                    lesson.setFilePath(filePath);
                    lesson.setFileName(fileName);
                }
            } else {
                //if old file doesnt exist , got upload new file, then use identifier
                file = new StorageFile("/" + "lesson" + "/" + identifier,
                        lesson.getStorageFile());
            }

            if (attachment.getValue() != null) {
                // only if new attachment exist
                lesson.setFilePath(file.getAbsolutePath());

                StorageFile f1 = attachment.getStorageFile(evt.getRequest());
                String str = f1.getAbsolutePath();
                String fn = str.substring(1, str.length());
                lesson.setFileName(fn);
            }

            // else, use old filepath and filename
        } catch (IOException e) {
            e.printStackTrace(); //To change body of catch statement use File | Settings | File Templates.
        } catch (StorageException e) {
            e.printStackTrace(); //To change body of catch statement use File | Settings | File Templates.
        }

        String selectedDocArticles = "";

        for (int i = 0; i < eLearningFiles.getIds().length; i++) {
        	if(i > 0)
        		selectedDocArticles += ("," + eLearningFiles.getIds()[i]);
        	else
        		selectedDocArticles += (eLearningFiles.getIds()[i]);
        }

        lesson.setAssociatedoc(selectedDocArticles);



        lessonmodule.updateLesson(lesson);

        return new Forward("updated");
    }

    public Forward onValidationFailed(Event evt) {
        String buttonClicked = findButtonClicked(evt);

        if (cancel.getAbsoluteName().equals(buttonClicked)) {
            return new Forward("cancel");
        } else {
            return null;
        }
    }

    public String getFilePath() {
        return filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public Map generateOptionMap(String[] ids) {
        Map optionMap = new SequencedHashMap();

        try {
            ContentManager cm = (ContentManager) Application.getInstance()
                                                            .getModule(ContentManager.class);
            User user = getWidgetManager().getUser();
            Collection list = cm.viewListWithContents(ids,
                    new String[] { Document.class.getName(), Article.class.getName() }, null, null,
                    null, null, null, null, null, Boolean.FALSE, null, null,
                    false, 0, -1, ContentManager.USE_CASE_VIEW, user);
            Map tmpMap = new HashMap();

            for (Iterator i = list.iterator(); i.hasNext();) {
                ContentObject co = (ContentObject) i.next();
                String name = co.getName();

                if ((name != null) && (name.length() > 20)) {
                    name = name.substring(0, 20) + "..";
                }

                tmpMap.put(co.getId(), name);
            }

            for (int j = 0; j < ids.length; j++)
                optionMap.put(ids[j], tmpMap.get(ids[j]));
        } catch (ContentException e) {
            Log.getLog(getClass()).error("Error retrieving content", e);
        }

        return optionMap;
    }


    public String getLessonid() {
        return lessonid;
    }

    public void setLessonid(String lessonid) {
        this.lessonid = lessonid;
    }
}
