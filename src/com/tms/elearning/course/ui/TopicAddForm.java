package com.tms.elearning.course.ui;

import kacang.stdui.*;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.stdui.validator.ValidatorMessage;
import kacang.util.UuidGenerator;
import kacang.util.Log;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.ui.WidgetManager;
import kacang.Application;
import kacang.model.DaoException;
import kacang.Application;
import kacang.services.security.User;


import com.tms.elearning.course.model.*;

import java.util.Collection;
import java.util.Iterator;

/**
 * Created by IntelliJ IDEA.
 * User: mahe
 * Date: Oct 27, 2004
 * Time: 4:41:36 PM
 * To change this template use Options | File Templates.
 */
public class TopicAddForm extends Form {
    protected TextField topic;
    protected SelectBox course_select;
    protected Button submit;
    protected Button cancel;
    private String id;
    protected ValidatorMessage vmsg_topicExists;






    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDefaultTemplate() {
        return "elearning/topicAddForm";
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
        Application application = Application.getInstance();
        WidgetManager manager = getWidgetManager();
        User user = manager.getUser();
        String userId=user.getUsername();

        topic = new TextField("topic");
        ValidatorNotEmpty v_topic = new ValidatorNotEmpty("v_topic", application.getMessage("eLearning.topic.label.topicrequired"));
        topic.addChild(v_topic);
        topic.addChild(vmsg_topicExists = new ValidatorMessage("vmsg_topicExists"));
        topic.setSize("40");
        topic.setMaxlength("255");
        addChild(topic);

        course_select = new SelectBox("course");
        //course_select.addChild(new ValidatorSelectBox("sbOrgTypeSB", application.getMessage("com.epermit.validator.selectOne"), "-1"));
        course_select.setOptions("-1="+application.getMessage("eLearning.topic.label.coursemenu"));
        TopicModuleDao tpcDao = (TopicModuleDao)Application.getInstance().getModule(TopicModule.class).getDao();
        try {
            Collection courses = tpcDao.getCourses(userId);
            for(Iterator i = courses.iterator();i.hasNext();){
                Topic temp =(Topic)i.next();
                course_select.setOptions(temp.getCourse_id()+"="+temp.getCourse_name());
            }
        } catch (DaoException e) {
            Log.getLog(getClass()).error(e.toString(),e);
        }
        addChild(course_select);

        submit = new Button("submit", application.getMessage("eLearning.course.label.submit"));
        addChild(submit);

        cancel = new Button("cancel",application.getMessage("eLearning.course.label.cancel"));
        addChild(cancel);

    }

    public Forward onSubmit(Event evt) {
        super.onSubmit(evt);
        TopicModule topicmodule = (TopicModule)Application.getInstance().getModule(TopicModule.class);
        Application application = Application.getInstance();
        if (topicmodule.topicExists((String)topic.getValue()) != null) {
            vmsg_topicExists.showError(application.getMessage("eLearning.topic.label.sametopicexist"));
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
        String msg = "";
        TopicModule topicmodule = (TopicModule)Application.getInstance().getModule(TopicModule.class);
        Topic tpc = new Topic();
        UuidGenerator uuid = UuidGenerator.getInstance();
        String sid = uuid.getUuid();

        tpc.setTopic((String)topic.getValue());
        Collection selectedList = (Collection)course_select.getValue();
        String selected = (String)selectedList.iterator().next();
        tpc.setCourse_id(selected);
        tpc.setId(sid);
        topicmodule.insertTopic(tpc);
        msg = "added";

        return new Forward(msg);
    }
}
