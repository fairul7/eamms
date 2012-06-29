package com.tms.elearning.course.ui;

import com.tms.elearning.course.model.Topic;
import com.tms.elearning.course.model.TopicModule;
import com.tms.elearning.course.model.TopicModuleDao;
import com.tms.elearning.course.model.TopicModuleException;

import kacang.Application;

import kacang.model.DaoException;

import kacang.services.security.User;

import kacang.stdui.Button;
import kacang.stdui.Form;
import kacang.stdui.SelectBox;
import kacang.stdui.TextField;

import kacang.stdui.validator.ValidatorMessage;
import kacang.stdui.validator.ValidatorNotEmpty;

import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.ui.WidgetManager;

import kacang.util.Log;

import java.util.Collection;
import java.util.Iterator;


/**
 * Created by IntelliJ IDEA.
 * User: mahe
 * Date: Oct 28, 2004
 * Time: 4:16:05 PM
 * To change this template use Options | File Templates.
 */
public class TopicEditForm extends Form {
    protected TextField topic;
    protected SelectBox course_select;
    protected Button submit;
    protected Button cancel;
    private String id;
    protected ValidatorMessage vmsg_topicExists;
    protected ValidatorMessage vmsg_courseNotSelected;

    private TopicModule topicmodule = (TopicModule) Application.getInstance()
                                                               .getModule(TopicModule.class);

    public String getTopicId() {
        return id;
    }

    public void setTopicId(String id) {
        this.id = id;
    }

    public String getDefaultTemplate() {
        return "elearning/topicEditForm";
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
        WidgetManager manager = getWidgetManager();
        User user = manager.getUser();
        String userId = user.getUsername();
        Application application = Application.getInstance();
        topic = new TextField("topic");

        ValidatorNotEmpty v_topic = new ValidatorNotEmpty("v_topic",
                application.getMessage("eLearning.topic.label.topicrequired"));
        topic.addChild(v_topic);
        topic.addChild(vmsg_topicExists = new ValidatorMessage(
                    "vmsg_topicExists"));
        topic.setSize("40");
        topic.setMaxlength("255");
        addChild(topic);

        course_select = new SelectBox("course");
        course_select.addChild(vmsg_courseNotSelected = new ValidatorMessage(
                    "vmsg_courseNotSelected"));

        course_select.setOptions("-1=" +
            application.getMessage("eLearning.topic.label.coursemenu"));

        TopicModuleDao tpcDao = (TopicModuleDao) Application.getInstance()
                                                            .getModule(TopicModule.class)
                                                            .getDao();

        try {
            Collection courses = tpcDao.getCourses(userId);

            for (Iterator i = courses.iterator(); i.hasNext();) {
                Topic temp = (Topic) i.next();
                course_select.setOptions(temp.getCourse_id() + "=" +
                    temp.getCourse_name());
            }
        } catch (DaoException e) {
            Log.getLog(getClass()).error(e.toString(), e);
        }

        addChild(course_select);

        submit = new Button("submit",
                application.getMessage("eLearning.topic.label.edit"));
        addChild(submit);
        cancel = new Button("cancel",
                application.getMessage("eLearning.course.label.cancel"));
        addChild(cancel);

        if (id != null) {
            try {
                Topic tpc = topicmodule.getTopic(id);
                topic.setValue(tpc.getTopic());
                course_select.setSelectedOption(tpc.getCourse_id());
            } catch (Exception e) {
                Log.getLog(getClass()).error(application.getMessage(
                        "eLearning.topic.label.topic") + " " + id + " " +
                    application.getMessage(
                        "eLearning.topic.label.cannotberetrieve") +
                    e.toString(), e);
                throw new TopicModuleException(e.toString());
            }
        }
    }

    public Forward onSubmit(Event evt) {
        super.onSubmit(evt);
        Application application = Application.getInstance();
        if (topicmodule.topicExists((String) topic.getValue(), id) != null) {
            vmsg_topicExists.showError(application.getMessage(
                    "eLearning.topic.label.sametopicexist"));
            this.setInvalid(true);
        }

        Collection selectedList = (Collection) course_select.getValue();
        String selected = (String) selectedList.iterator().next();

        if (selected.equalsIgnoreCase("-1")) {
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
        Topic tpc = new Topic();

        tpc.setTopic((String) topic.getValue());

        Collection selectedList = (Collection) course_select.getValue();
        String selected = (String) selectedList.iterator().next();
        tpc.setCourse_id(selected);
        tpc.setId(id);

        topicmodule.updateTopic(tpc);

        return new Forward("updated");
    }
}
