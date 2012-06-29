package com.tms.elearning.course.ui;


/**
 * Created by IntelliJ IDEA.
 * User: tirupati
 * Date: Oct 20, 2004
 * Time: 7:01:35 PM
 * To change this template use File | Settings | File Templates.
 */
import com.tms.elearning.course.model.Course;
import com.tms.elearning.course.model.CourseModule;
import com.tms.elearning.course.model.CourseModuleException;
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

import kacang.util.Log;

import java.sql.Date;

import java.text.SimpleDateFormat;

import java.util.*;


public class CourseFormEdit extends Form {
    protected TextField name;
    protected RichTextBox synopsis;
    protected SelectBox category;
    protected TextField author;
    protected CheckBox ispublic;
    protected Button submit;
    protected Button cancel;
    private String id;
    private String courseId;
    protected ValidatorMessage vmsg_courseExists;



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDefaultTemplate() {
        return "elearning/courseEditForm";
    }

    public void init(Event evt) {
        super.init();
        setMethod("post");
        initForm(evt);
    }

    public void onRequest(Event evt) {
        super.onRequest(evt);
        initForm(evt);
    }

    public void initForm(Event evt) {

        CourseModule module = (CourseModule) Application.getInstance()
                                                            .getModule(CourseModule.class);
        CategoryModule catmodule = (CategoryModule) Application.getInstance()
                                                                   .getModule(CategoryModule.class);
        name = new TextField("course");

        ValidatorNotEmpty v_course = new ValidatorNotEmpty("v_course",
                "");
        name.addChild(v_course);
        name.addChild(vmsg_courseExists = new ValidatorMessage(
                    "vmsg_courseExists"));
        name.setSize("40");
        name.setMaxlength("50");
        addChild(name);

        addChild(new Label("l2", "synopsis"));
        synopsis = new RichTextBox("synopsis");
        synopsis.setCols("8");
        synopsis.setRows("25");
        ((RichTextBox) synopsis).setLinkUrl(evt.getRequest().getContextPath() +
            "/cmsadmin/content/imageSelectorFrame.jsp");
        addChild(synopsis);

        addChild(new Label("l3", "instructor"));

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
                Application.getInstance().getMessage("eLearning.course.label.update"));
        addChild(submit);

        cancel = new Button("cancel",
                Application.getInstance().getMessage("eLearning.course.label.cancel"));
        addChild(cancel);

        if (id != null) {
            try {

                if(module.loadCourse(getId()) !=null)
                {Course cour = module.loadCourse(getId());

                  setCourseId(cour.getId());
                name.setValue(cour.getName());
                synopsis.setValue(cour.getSynopsis());

                author.setValue(cour.getAuthor());

                category.setSelectedOption(cour.getCategoryid());

                boolean publicBool;

                if ("1".equals(cour.getIs_public())) {
                    publicBool = true;
                } else {
                    publicBool = false;
                }


                ispublic.setChecked(publicBool);


                }//if cour is not null

            } catch (Exception e) {
                Log.getLog(getClass()).error(Application.getInstance()
                                                        .getMessage("eLearning.course.label.course") +
                    " " + id + " " +
                    Application.getInstance().getMessage("eLearning.course.label.cannotretrieve") +
                    e.toString(), e);
                throw new CourseModuleException(e.toString());
            }
        }
    }

    public Forward onSubmit(Event evt) {
        super.onSubmit(evt);
        CourseModule module = (CourseModule) Application.getInstance()
                                                            .getModule(CourseModule.class);

         if (module.checkCanAddSameId((String) name.getValue(), (String) ((List)category.getValue()).get(0), getCourseId()) != null) {
            vmsg_courseExists.showError(Application.getInstance().getMessage("eLearning.course.label.same.course"));
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

        CourseModule module = (CourseModule) Application.getInstance()
                                                            .getModule(CourseModule.class);
        WidgetManager manager = getWidgetManager();
        User user = manager.getUser();

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy h:mm a");
        Date today = new Date(System.currentTimeMillis());
        String createdDate = formatter.format(today);

        Course course = new Course();
        course.setId(getId());
        course.setName((String) name.getValue());
        course.setSynopsis((String) synopsis.getValue());

        Map categorySelected = category.getSelectedOptions();
        Set set = categorySelected.keySet();
        Iterator iter = set.iterator();

        while (iter.hasNext()) {
            String key = iter.next().toString();

            course.setCategoryid(key);
        }

        course.setAuthor((String) author.getValue());
        course.setCreatedByUser(user.getUsername());
        course.setCreatedByUserId(user.getId());
        course.setCreatedDate(createdDate);
        course.setInstructor(Application.getInstance().getCurrentUser()
                                        .getUsername());

        if ((ispublic != null) && ispublic.isChecked()) {
            course.setIs_public("1");
        } else {
            course.setIs_public("0");
        }

        module.updateCourse(course);

        return new Forward("updated");
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

}
