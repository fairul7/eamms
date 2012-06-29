package com.tms.elearning.testware.ui;

import com.tms.elearning.core.ui.ValidatorNotEmpty;
import com.tms.elearning.course.model.TopicModuleException;
import com.tms.elearning.folder.model.FolderModule;
import com.tms.elearning.folder.model.FolderModuleDao;
import com.tms.elearning.lesson.model.LessonDao;
import com.tms.elearning.lesson.model.LessonModule;
import com.tms.elearning.testware.model.Question;
import com.tms.elearning.testware.model.QuestionModule;

import kacang.Application;

import kacang.services.security.User;

import kacang.stdui.*;

import kacang.stdui.validator.ValidatorMessage;

import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.ui.WidgetManager;

import kacang.util.Log;
import kacang.util.UuidGenerator;

import java.io.Serializable;

import java.text.SimpleDateFormat;

import java.util.Date;


/**
 * Created by IntelliJ IDEA.
 * User: mahe
 * Date: Nov 2, 2004
 * Time: 6:13:10 PM
 * To change this template use Options | File Templates.
 */
public class QuestionAddForm extends Form implements Serializable {
    protected SelectBox module_select;
    protected SelectBox course_select;
    protected SelectBox lesson_select;
    protected Button submit;
    protected Button cancel;
    private TextBox question;
    protected TextField answer_a;
    protected TextField answer_b;
    protected TextField answer_c;
    protected TextField answer_d;
    protected Radio a;
    protected Radio b;
    protected Radio c;
    protected Radio d;
    private ValidatorNotEmpty validateCourse;
    private ValidatorNotEmpty validateModule;
    private ValidatorNotEmpty validateLesson;
    protected ValidatorMessage vmsg_course;
    private ValidatorNotEmpty aV;
    private ValidatorNotEmpty bV;
    private ValidatorNotEmpty cV;
    private ValidatorNotEmpty dV;
    protected String id;



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDefaultTemplate() {
        return "elearning/questionAddForm";
    }

    public void onRequest(Event evt) {
        super.onRequest(evt);
        setMethod("post");
                
        initForm(evt);
    }

    public void init(Event evt) {
        super.init();
        initForm(evt);
    }

    public void initForm(Event evt) {


        QuestionModule qmodule = (QuestionModule) Application.getInstance()
                                                                 .getModule(QuestionModule.class);
        WidgetManager manager = getWidgetManager();
        User user = manager.getUser();
        String userId = user.getUsername();

        /* course_select = new SelectBox("course");

         validateCourse = new ValidatorNotEmpty("validCourse", "");


         course_select.addChild(vmsg_course = new ValidatorMessage("vmsg_course"));

         TopicModuleDao tpcDao = (TopicModuleDao) Application.getInstance()
                                                             .getModule(TopicModule.class)
                                                             .getDao();
         LessonDao dao = (LessonDao) Application.getInstance()
                                                .getModule(LessonModule.class)
                                                .getDao();

         try {
             Collection courses = null;

             courses = tpcDao.getCourses();

             Map hash = new SequencedHashMap();
             hash.put("",
                 application.getMessage("eLearning.topic.label.coursemenu"));

             for (Iterator i = courses.iterator(); i.hasNext();) {
                 Topic temp = (Topic) i.next();
                 hash.put(temp.getCourse_id(), temp.getCourse_name());
             }

             course_select.setOptionMap(hash);
         } catch (DaoException e) {
             Log.getLog(getClass()).error(e.toString(), e);
         }



         course_select.setOnChange(" document.forms['" + getAbsoluteName() +
             "'].submit()");




         addChild(course_select);

         module_select = new SelectBox("module");

         validateModule = new ValidatorNotEmpty("validModule", "");
         module_select.addChild(validateModule);
         addChild(module_select);

         lesson_select = new SelectBox("lesson");

         LessonDao ldao = (LessonDao) Application.getInstance()
                                                 .getModule(LessonModule.class)
                                                 .getDao();

         try {
             Collection coll = ldao.getLessons(userId);
             Map hashh = new SequencedHashMap();
             hashh.put("",
                 application.getMessage("eLearning.lesson.label.lessonmenu"));

             for (Iterator iterator = coll.iterator(); iterator.hasNext();) {
                 Topic temp = (Topic) iterator.next();
                 hashh.put(temp.getId(), temp.getName());
             }

             lesson_select.setOptionMap(hashh);
         } catch (DaoException e) {
             Log.getLog(getClass()).error(e.toString());
         }

         lesson_select = new SelectBox("lesson");

         validateLesson = new ValidatorNotEmpty("validLesson", "");
         lesson_select.addChild(validateLesson);

         addChild(lesson_select);*/
        aV = new ValidatorNotEmpty("aV");
        bV = new ValidatorNotEmpty("bV");
        cV = new ValidatorNotEmpty("cV");
        dV = new ValidatorNotEmpty("dV");

        question = new com.tms.elearning.core.model.RichTextBox("question");

        question.setCols("8");
        question.setRows("25");
        ((com.tms.elearning.core.model.RichTextBox) question).setLinkUrl(evt.getRequest()
                                                                            .getContextPath() +
            "/cmsadmin/content/imageSelectorFrame.jsp");

        addChild(question);

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

        submit = new Button("submit", "Create New Question");
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
                Log.getLog(getClass()).error("Question " + id +
                    " could not be retrieved " + e.toString(), e);
                throw new TopicModuleException(e.toString());
            }
        }
    }

    public Forward onSubmit(Event evt) {
        super.onSubmit(evt);

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

        if (!submit.getAbsoluteName().equals(button)) {
            /*  try {
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
            */
            this.setInvalid(true);
        }
         /*else {
          Collection selectedList = (Collection) course_select.getValue();
          String selected = (String) selectedList.iterator().next();

          if (selected.equalsIgnoreCase("-1")) {
              vmsg_course.showError("");
             this.setInvalid(true);
          }
        }*/

        return null;
    }

    public Forward onValidationFailed(Event evt) {
        String buttonClicked = findButtonClicked(evt);
        String testing123 = (String) question.getValue();

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

        /* Collection courseList = (Collection) course_select.getValue();
         String selected_course = (String) courseList.iterator().next();
         q.setCourse_id(selected_course);

         Collection moduleList = (Collection) module_select.getValue();
         String selected_module = (String) moduleList.iterator().next();
         q.setModule_id(selected_module);

         Collection lessonList = (Collection) lesson_select.getValue();
         String selected_lesson = (String) lessonList.iterator().next();
         q.setLesson_id(selected_lesson);
        */
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


        //no need course id, module id, and lesson id
        q.setCourse_id("");
        q.setModule_id("");
        q.setLesson_id("");

        /*q.setParentId(selected_course);

                if (!q.getModule_id().equalsIgnoreCase("-1")) {
                    q.setParentId(selected_module);

                }

                if (!q.getLesson_id().equalsIgnoreCase("-1")) {
                    q.setParentId(selected_lesson);

                }
        */
        if (a.isChecked()) {
            q.setCorrect_answer("A");
        } else if (b.isChecked()) {
            q.setCorrect_answer("B");
        } else if (c.isChecked()) {
            q.setCorrect_answer("C");
        } else if (d.isChecked()) {
            q.setCorrect_answer("D");
        }

        q.setId(UuidGenerator.getInstance().getUuid());

        qmodule.insertQuestion(q);

        return new Forward("add");
    }
}
