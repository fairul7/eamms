package com.tms.elearning.course.ui;

import com.tms.elearning.course.model.Course;
import com.tms.elearning.course.model.CourseModule;
import com.tms.elearning.coursecategory.model.Category;
import com.tms.elearning.coursecategory.model.CategoryModule;

import kacang.Application;

import kacang.services.security.User;

import kacang.stdui.*;

import kacang.stdui.validator.ValidatorMessage;
import kacang.stdui.validator.ValidatorNotEmpty;

import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.ui.WidgetManager;

import kacang.util.UuidGenerator;

import java.sql.Date;

import java.text.SimpleDateFormat;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;


public class CourseAddForm extends Form {
    protected TextField course;
    protected RichTextBox synopsis;
    protected SelectBox instructor;
    protected SelectBox category;
    protected TextField author;
    protected CheckBox ispublic;
    protected Button submit;
    protected Button cancel;
    private String id;
    protected ValidatorMessage vmsg_courseExists;



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDefaultTemplate() {
        return "elearning/courseForm";
    }

    public void init(Event evt) {
        super.init();
        initForm(evt);
    }

    public void onRequest(Event evt) {
        super.onRequest(evt);
        initForm(evt);
    }

    public void initForm(Event evt) {

        setMethod("post");
        CategoryModule catmodule = (CategoryModule) Application.getInstance()
                                                                   .getModule(CategoryModule.class);
        course = new TextField("course");

        ValidatorNotEmpty v_course = new ValidatorNotEmpty("v_course",
                "");
        course.addChild(v_course);
        course.addChild(vmsg_courseExists = new ValidatorMessage(
                    "vmsg_courseExists"));
        course.setSize("40");
        course.setMaxlength("50");
        addChild(course);

        addChild(new Label("l2", "synopsis"));
        synopsis = new RichTextBox("synopsis");
        synopsis.setCols("8");
        synopsis.setRows("25");

        ((RichTextBox) synopsis).setLinkUrl(evt.getRequest().getContextPath() +
            "/cmsadmin/content/imageSelectorFrame.jsp");
        addChild(synopsis);

        addChild(new Label("l4", "category"));
        category = new SelectBox("category");

        Collection categories = catmodule.loadCategories();

        Iterator ite = categories.iterator();

        while (ite.hasNext()) {
            Category cat = (Category) ite.next();
            String id = cat.getId();
            String name = cat.getCategory();
            category.addOption(id, name);
        }

        addChild(category);

        addChild(new Label("l5", "author"));
        author = new TextField("author");
        author.setMaxlength("50");
        addChild(author);

        ispublic = new CheckBox("ispublic");
        addChild(ispublic);

        submit = new Button("submit",
                Application.getInstance().getMessage("eLearning.course.label.submit"));
        addChild(submit);

        cancel = new Button("cancel",
                Application.getInstance().getMessage("eLearning.course.label.cancel"));
        addChild(cancel);
    }

    public Forward onSubmit(Event evt) {
        super.onSubmit(evt);
        CategoryModule catmodule = (CategoryModule) Application.getInstance()
                                                                   .getModule(CategoryModule.class);

         CourseModule coursemodule = (CourseModule) Application.getInstance()
                                                                  .getModule(CourseModule.class);

        if (coursemodule.checkCanAdd((String) course.getValue(), (String) ((List)category.getValue()).get(0)) != null) {
            vmsg_courseExists.showError(Application.getInstance().getMessage("eLearning.course.label.coursexist"));
            this.setInvalid(true);
        }

        return null;
    }

    public Forward onValidationFailed(Event evt) {
        String buttonClicked = findButtonClicked(evt);

        if (buttonClicked.equals(cancel.getAbsoluteName())) {
            return new Forward("cancel");
        } else {
            return null;
        }
    }

    public Forward onValidate(Event evt) {


        CourseModule coursemodule = (CourseModule) Application.getInstance()
                                                                  .getModule(CourseModule.class);
        Course cour = new Course();
        UuidGenerator uuid = UuidGenerator.getInstance();
        String sid = uuid.getUuid();
        WidgetManager manager = getWidgetManager();
        User user = manager.getUser();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date today = new Date(System.currentTimeMillis());
        String createdDate = formatter.format(today);

        cour.setId(sid);
        cour.setName((String) course.getValue());
        cour.setSynopsis((String) synopsis.getValue());
       

        Application application = Application.getInstance();

        cour.setInstructor(application.getCurrentUser().getUsername());

        List catlist = (List) category.getValue();
        Iterator iterator1 = catlist.iterator();
        String str = "";

        while (iterator1.hasNext()) {
            str = (String) iterator1.next();
        }

        cour.setCategoryid(str);
        cour.setAuthor((String) author.getValue());
        cour.setCreatedByUser(user.getUsername());
        cour.setCreatedByUserId(user.getId());
        cour.setCreatedDate(createdDate);

        if ((ispublic != null) && ispublic.isChecked()) {
            cour.setIs_public("1");
        } else {
            cour.setIs_public("0");
        }

        coursemodule.addCourse(cour);

        return new Forward("added");
    }
}
