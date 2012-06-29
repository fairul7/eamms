package com.tms.elearning.testware.ui;

import com.tms.elearning.core.ui.ValidatorNotEmpty;
import com.tms.elearning.course.model.Topic;
import com.tms.elearning.course.model.TopicModule;
import com.tms.elearning.course.model.TopicModuleDao;
import com.tms.elearning.course.model.TopicModuleException;
import com.tms.elearning.folder.model.Folder;
import com.tms.elearning.folder.model.FolderModule;
import com.tms.elearning.folder.model.FolderModuleDao;
import com.tms.elearning.lesson.model.Lesson;
import com.tms.elearning.lesson.model.LessonDao;
import com.tms.elearning.lesson.model.LessonModule;
import com.tms.elearning.testware.model.Question;
import com.tms.elearning.testware.model.QuestionModule;

import kacang.Application;

import kacang.model.DaoException;

import kacang.services.security.User;

import kacang.stdui.*;

import kacang.stdui.validator.ValidatorMessage;

import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.ui.WidgetManager;

import kacang.util.Log;

import org.apache.commons.collections.SequencedHashMap;

import java.io.Serializable;

import java.text.SimpleDateFormat;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;


/**
 * Created by IntelliJ IDEA.
 * User: mahe
 * Date: Nov 2, 2004
 * Time: 6:13:10 PM
 * To change this template use Options | File Templates.
 */
public class QuestionEditForm extends Form implements Serializable {
    protected SelectBox module_select;
    protected SelectBox course_select;
    protected SelectBox lesson_select;
    protected TextBox question;
    protected TextField answer_a;
    protected TextField answer_b;
    protected TextField answer_c;
    protected TextField answer_d;
    protected Radio a;
    protected Radio b;
    protected Radio c;
    protected Radio d;
    protected ValidatorNotEmpty aV;
    protected ValidatorNotEmpty bV;
    protected ValidatorNotEmpty cV;
    protected ValidatorNotEmpty dV;
    protected Button submit;
    protected Button cancel;
    private String id;
    protected ValidatorMessage vmsg_course;
    protected ValidatorMessage vmsg_module;
    protected ValidatorMessage vmsg_lesson;

    

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDefaultTemplate() {
        return "elearning/questionEditForm";
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
        QuestionModule qmodule = (QuestionModule) Application.getInstance()
                                                                 .getModule(QuestionModule.class);
        WidgetManager manager = getWidgetManager();
        User user = manager.getUser();
        String userId = user.getUsername();
        course_select = new SelectBox("course");
        course_select.addChild(vmsg_course = new ValidatorMessage("vmsg_course"));

        TopicModuleDao tpcDao = (TopicModuleDao) Application.getInstance()
                                                            .getModule(TopicModule.class)
                                                            .getDao();
        FolderModuleDao modDao = (FolderModuleDao) Application.getInstance()
                                                              .getModule(FolderModule.class)
                                                              .getDao();
        LessonDao lsnDao = (LessonDao) Application.getInstance()
                                                  .getModule(LessonModule.class)
                                                  .getDao();

        try {
            Collection courses = null;

            courses = tpcDao.getCourses();

            Map hash = new SequencedHashMap();
            hash.put("-1",
                application.getMessage("eLearning.topic.label.coursemenu"));

            for (Iterator i = courses.iterator(); i.hasNext();) {
                Topic temp = (Topic) i.next();

                hash.put(temp.getCourse_id(), temp.getCourse_name());
            }

            course_select.setOptionMap(hash);
        } catch (DaoException e) {
            Log.getLog(getClass()).error(e.toString(), e);
        }

        course_select.setOnChange("document.forms['" + getAbsoluteName() +
            "'].submit()");
        addChild(course_select);

        module_select = new SelectBox("module");
        module_select.addChild(vmsg_module = new ValidatorMessage("vmsg_module"));
        module_select.setOptions("-1=" +
            application.getMessage("eLearning.lesson.label.modulemenu"));

        try {
            Question q = qmodule.getQuestion(id);
            Collection mods = null;

            if (userId != null) {
                mods = modDao.getModuleList(q.getCourse_id(), userId);
            } else {
                mods = modDao.getModuleList(q.getCourse_id());
            }

            Map hash = new SequencedHashMap();
            hash.put("-1",
                application.getMessage("eLearning.lesson.label.modulemenu"));

            for (Iterator i = mods.iterator(); i.hasNext();) {
                Folder temp = (Folder) i.next();
                hash.put(temp.getId(), temp.getFolderName());
            }

            module_select.setOptionMap(hash);
        } catch (DaoException e) {
            Log.getLog(getClass()).error(e.toString(), e);
        }

        module_select.setOnChange("document.forms['" + getAbsoluteName() +
            "'].submit()");
        addChild(module_select);

        lesson_select = new SelectBox("lesson");
        lesson_select.addChild(vmsg_lesson = new ValidatorMessage("vmsg_lesson"));
        lesson_select.setOptions("-1=" +
            application.getMessage("eLearning.lesson.label.lessonmenu"));

        try {
            Question q = qmodule.getQuestion(id);
            Collection lsn = null;

            if (userId != null) {
                lsn = lsnDao.getLessonList(q.getCourse_id(), userId);
            } else {
                lsn = lsnDao.getLessonList(q.getCourse_id());
            }

            Map hash = new SequencedHashMap();
            hash.put("-1",
                application.getMessage("eLearning.lesson.label.lessonmenu"));

            for (Iterator i = lsn.iterator(); i.hasNext();) {
                Lesson temp = (Lesson) i.next();
                hash.put(temp.getId(), temp.getLessonName());
            }

            lesson_select.setOptionMap(hash);
        } catch (DaoException e) {
            Log.getLog(getClass()).error(e.toString(), e);
        }

        lesson_select.setOnChange("document.forms['" + getAbsoluteName() +
            "'].submit()");
        addChild(lesson_select);

        question = new RichTextBox("question");
        question.setCols("8");
        question.setRows("25");
        ((RichTextBox) question).setLinkUrl(evt.getRequest().getContextPath() +
            "/cmsadmin/content/imageSelectorFrame.jsp");
        addChild(question);

        aV = new ValidatorNotEmpty("aV");
        bV = new ValidatorNotEmpty("bV");
        cV = new ValidatorNotEmpty("cV");
        dV = new ValidatorNotEmpty("dV");

        answer_a = new TextField("answer_a");
        answer_a.setSize("100");
        answer_a.setMaxlength("255");
        answer_a.addChild(aV);
        addChild(answer_a);

        answer_b = new TextField("answer_b");
        answer_b.setSize("100");
        answer_b.setMaxlength("255");
        answer_b.addChild(bV);
        addChild(answer_b);

        answer_c = new TextField("answer_c");
        answer_c.setSize("100");
        answer_c.setMaxlength("255");
        answer_c.addChild(cV);
        addChild(answer_c);

        answer_d = new TextField("answer_d");
        answer_d.setSize("100");
        answer_d.setMaxlength("255");
        answer_d.addChild(dV);
        addChild(answer_d);

        a = new Radio("a");
        a.setGroupName("correct_answer");
        a.setChecked(true);
        addChild(a);

        b = new Radio("b");
        b.setGroupName("correct_answer");
        addChild(b);

        c = new Radio("c");
        c.setGroupName("correct_answer");
        addChild(c);

        d = new Radio("d");
        d.setGroupName("correct_answer");
        addChild(d);

        submit = new Button("submit", "Update");
        addChild(submit);
        cancel = new Button("cancel", "Cancel");
        addChild(cancel);

        if (id != null) {
            try {
                Question q = qmodule.getQuestion(id);
                module_select.setSelectedOption(q.getModule_id());
                lesson_select.setSelectedOption(q.getLesson_id());
                course_select.setSelectedOption(q.getCourse_id());
                question.setValue(q.getQuestion());

                answer_a.setValue(q.getAnswer_a());
                answer_b.setValue(q.getAnswer_b());
                answer_c.setValue(q.getAnswer_c());
                answer_d.setValue(q.getAnswer_d());

                if (q.getCorrect_answer().equalsIgnoreCase("A")) {
                    a.setChecked(true);
                } else if (q.getCorrect_answer().equalsIgnoreCase("B")) {
                    b.setChecked(true);
                } else if (q.getCorrect_answer().equalsIgnoreCase("C")) {
                    c.setChecked(true);
                } else if (q.getCorrect_answer().equalsIgnoreCase("D")) {
                    d.setChecked(true);
                }
            } catch (Exception e) {
                Log.getLog(getClass()).error(application.getMessage(
                        "eLearning.question.label") + " " + id + " " +
                    application.getMessage(
                        "eLearning.question.label.couldnotretrieved") +
                    e.toString(), e);
                throw new TopicModuleException(e.toString());
            }
        }
    }

    public Forward onSubmit(Event evt) {
        Application application = Application.getInstance();
        WidgetManager manager = getWidgetManager();
        User user = manager.getUser();
        String userId = user.getUsername();
        String button = findButtonClicked(evt);
        FolderModuleDao modDao = (FolderModuleDao) Application.getInstance()
                                                              .getModule(FolderModule.class)
                                                              .getDao();
        LessonDao lsnDao = (LessonDao) Application.getInstance()
                                                  .getModule(LessonModule.class)
                                                  .getDao();

        super.onSubmit(evt);

        if (!submit.getAbsoluteName().equals(button)) {
            try {
                Collection courseList = (Collection) course_select.getValue();
                String selected_course = (String) courseList.iterator().next();

                Collection mods = null;

                if (userId != null) {
                    mods = modDao.getModuleList(selected_course, userId);
                } else {
                    mods = modDao.getModuleList(selected_course);
                }

                module_select.removeAllOptions();
                module_select.setOptions("-1=" +
                    application.getMessage("eLearning.lesson.label.modulemenu"));

                for (Iterator i = mods.iterator(); i.hasNext();) {
                    Folder temp = (Folder) i.next();
                    module_select.setOptions(temp.getId() + "=" +
                        temp.getFolderName());
                }
            } catch (DaoException e) {
                Log.getLog(getClass()).error(e.toString(), e);
            }

            try {
                Collection courseList = (Collection) course_select.getValue();
                String selected_course = (String) courseList.iterator().next();

                Collection lessons = null;

                if (userId != null) {
                    lessons = lsnDao.getLessonList(selected_course, userId);
                } else {
                    lessons = lsnDao.getLessonList(selected_course);
                }

                lesson_select.removeAllOptions();
                lesson_select.setOptions("-1=" +
                    application.getMessage("eLearning.lesson.label.lessonmenu"));

                for (Iterator i = lessons.iterator(); i.hasNext();) {
                    Lesson temp = (Lesson) i.next();
                    lesson_select.setOptions(temp.getId() + "=" +
                        temp.getLessonName());
                }
            } catch (DaoException e) {
                Log.getLog(getClass()).error(e.toString(), e);
            }

            this.setInvalid(true);
        }
         /*else {
          Collection selectedList = (Collection) course_select.getValue();
          String selected = (String) selectedList.iterator().next();

          if (selected.equalsIgnoreCase("-1")) {
              vmsg_course.showError("");
              this.setInvalid(true);
          }

          Collection selectedList2 = (Collection) module_select.getValue();
          String selected2 = (String) selectedList2.iterator().next();

          if (selected2.equalsIgnoreCase("-1")) {
              vmsg_module.showError("");
              this.setInvalid(true);
          }

          Collection selectedList3 = (Collection) lesson_select.getValue();
          String selected3 = (String) selectedList3.iterator().next();

          if (selected3.equalsIgnoreCase("-1")) {
              vmsg_lesson.showError("");
              this.setInvalid(true);
          }
        }*/

        return null;
    }

    public Forward onValidationFailed(Event evt) {
        String buttonClicked = findButtonClicked(evt);

        if ((buttonClicked != null) &&
                buttonClicked.equals(cancel.getAbsoluteName())) {
            return new Forward("cancel");
        } else {
            return null;
        }
    }

    public Forward onValidate(Event evt) {
        QuestionModule qmodule = (QuestionModule) Application.getInstance()
                                                                 .getModule(QuestionModule.class);
        Question q = new Question();
        WidgetManager manager = getWidgetManager();
        User user = manager.getUser();

        /*        Collection courseList = (Collection) course_select.getValue();
                String selected_course = (String) courseList.iterator().next();
                q.setCourse_id(selected_course);

                Collection moduleList = (Collection) module_select.getValue();
                String selected_module = (String) moduleList.iterator().next();
                q.setModule_id(selected_module);

                Collection lessonList = (Collection) lesson_select.getValue();
                String selected_lesson = (String) lessonList.iterator().next();
                q.setLesson_id(selected_lesson);*/
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date today = new Date(System.currentTimeMillis());
        String createdDate = formatter.format(today);

        q.setId(id);
         if(question.getValue()== null || ("".equals(question.getValue())) || "<br />".equals(question.getValue())) {


            return new Forward("empty");
        }
        q.setQuestion((String) question.getValue());
        q.setAnswer_a((String) answer_a.getValue());
        q.setAnswer_b((String) answer_b.getValue());
        q.setAnswer_c((String) answer_c.getValue());
        q.setAnswer_d((String) answer_d.getValue());
        q.setCreatedByUser(user.getUsername());
        q.setCreatedByUserId(user.getId());
        q.setCreatedDate(createdDate);

        q.setCourse_id("");
        q.setModule_id("");
        q.setLesson_id("");

        /*  q.setParentId(selected_course);

        if (!q.getModule_id().equalsIgnoreCase("-1")) {
            q.setParentId(selected_module);
        }

        if (!q.getLesson_id().equalsIgnoreCase("-1")) {
            q.setParentId(selected_lesson);
        }*/
        if (a.isChecked()) {
            q.setCorrect_answer("A");
        } else if (b.isChecked()) {
            q.setCorrect_answer("B");
        } else if (c.isChecked()) {
            q.setCorrect_answer("C");
        } else if (d.isChecked()) {
            q.setCorrect_answer("D");
        }

        qmodule.updateQuestion(q);

        return new Forward("updated");
    }
}
