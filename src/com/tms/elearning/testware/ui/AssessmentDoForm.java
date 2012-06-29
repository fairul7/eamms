package com.tms.elearning.testware.ui;

import kacang.stdui.*;
import kacang.stdui.validator.ValidatorMessage;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.Application;
import kacang.services.security.User;
import kacang.util.Log;
import kacang.model.DaoException;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.ui.WidgetManager;
import com.tms.elearning.testware.model.*;
import com.tms.elearning.course.model.*;
import com.tms.elearning.folder.model.*;
import com.tms.elearning.lesson.model.*;

import java.util.*;
import java.text.SimpleDateFormat;
import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: mahe
 * Date: Nov 23, 2004
 * Time: 4:31:01 PM
 * To change this template use Options | File Templates.
 */
public class AssessmentDoForm extends Form implements Serializable {
    protected SelectBox module_select;
    protected SelectBox course_select;
    protected SelectBox lesson_select;

    protected TextField name;
    protected TextField timelimit;
    protected SelectBox styear;
    protected SelectBox stdate;
    protected SelectBox stmonth;
    protected SelectBox enyear;
    protected SelectBox endate;
    protected SelectBox enmonth;
    protected SelectBox level;
    protected CheckBox active;
    protected CheckBox ispublic;


    protected Button submit;
    protected Button cancel;
    protected Button addFromQuestionBank;
    protected Button viewQuestions;
    protected ValidatorMessage vmsg_course;
    private String id;

    private Assessment asm;


    public String getAssessmentId() {
        return id;
    }

    public void setAssessmentId(String id) {
        this.id = id;
    }

    public String getDefaultTemplate() {
        return "elearning/assessmentEditForm";
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

         






    }

    public Forward onSubmit(Event evt) {
           Application application = Application.getInstance();
           WidgetManager manager = getWidgetManager();
           User user = manager.getUser();
           String userId=user.getUsername();
           String button = findButtonClicked(evt);
           FolderModuleDao modDao = (FolderModuleDao)Application.getInstance().getModule(FolderModule.class).getDao();
           LessonDao lsnDao = (LessonDao)Application.getInstance().getModule(LessonModule.class).getDao();

           super.onSubmit(evt);
        evt.getRequest().getSession().setAttribute("courseid",asm.getCourse_id());
        evt.getRequest().getSession().setAttribute("examid",this.id);

         if(viewQuestions.getAbsoluteName().equals(button)){
                return new Forward("viewQuestions","questionlist.jsp?show=yes",false);
           }else  if(addFromQuestionBank.getAbsoluteName().equals(button)){
                evt.getRequest().getSession().setAttribute("courseid",asm.getCourse_id());
                evt.getRequest().getSession().setAttribute("examid",this.id);
                return new Forward("questionbank","questionlist.jsp?show=no",false);
           }else   if (!submit.getAbsoluteName().equals(button)) {
               try {
                   Collection courseList = (Collection)course_select.getValue();
                   String selected_course = (String)courseList.iterator().next();

                   Collection mods = modDao.getModuleList(selected_course,userId);
                   module_select.removeAllOptions();
                   module_select.setOptions("-1="+application.getMessage("eLearning.lesson.label.modulemenu"));
                   for(Iterator i = mods.iterator();i.hasNext();){
                       Folder temp =(Folder)i.next();
                       module_select.setOptions(temp.getId()+"="+temp.getFolderName());
                   }
              } catch (DaoException e) {
                       Log.getLog(getClass()).error(e.toString(),e);
              }

              try {

                   Collection courseList = (Collection)course_select.getValue();
                   String selected_course = (String)courseList.iterator().next();

                   Collection lessons = lsnDao.getLessonList(selected_course,userId);
                   lesson_select.removeAllOptions();
                   lesson_select.setOptions("-1=--- Lesson ---");
                   for(Iterator i = lessons.iterator();i.hasNext();){
                       Lesson temp =(Lesson)i.next();
                       lesson_select.setOptions(temp.getId()+"="+temp.getLessonName());
                   }
              } catch (DaoException e) {
                       Log.getLog(getClass()).error(e.toString(),e);
              }
              setInvalid(true); 
           }   else {
               Collection selectedList = (Collection)course_select.getValue();
               String selected = (String)selectedList.iterator().next();
               if (selected.equalsIgnoreCase("-1")) {
                   vmsg_course.showError(application.getMessage("eLearning.lesson.label.courserequired"));
                   this.setInvalid(true);
               }
           }
           return null;
       }

        public Forward onValidationFailed(Event evt) {
           String buttonClicked = findButtonClicked(evt);
           if (buttonClicked != null && buttonClicked.equals(cancel.getAbsoluteName())) {
               return new Forward("cancel");
           } else {
               return null;
           }
       }

       public Forward onValidate(Event evt) {
           Assessment assmt = new Assessment();

           WidgetManager manager = getWidgetManager();
           User user = manager.getUser();

           Collection courseList = (Collection)course_select.getValue();
           String selected_course = (String)courseList.iterator().next();
           Collection moduleList = (Collection)module_select.getValue();
           String selected_module = (String)moduleList.iterator().next();
           Collection lessonList = (Collection)lesson_select.getValue();
           String selected_lesson = (String)lessonList.iterator().next();
           Collection levelList = (Collection)level.getValue();
           int selected_level = Integer.parseInt(levelList.iterator().next().toString());

           Collection stdateList = (Collection)stdate.getValue();
           int selected_stdate = Integer.parseInt(stdateList.iterator().next().toString());
           Collection stmonthList = (Collection)stmonth.getValue();
           int selected_stmonth = Integer.parseInt(stmonthList.iterator().next().toString());
           Collection styearList = (Collection)styear.getValue();
           int selected_styear = Integer.parseInt(styearList.iterator().next().toString());

           Collection endateList = (Collection)endate.getValue();
           int selected_endate = Integer.parseInt(endateList.iterator().next().toString());
           Collection enmonthList = (Collection)enmonth.getValue();
           int selected_enmonth = Integer.parseInt(enmonthList.iterator().next().toString());
           Collection enyearList = (Collection)enyear.getValue();
           int selected_enyear = Integer.parseInt(enyearList.iterator().next().toString());


           SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
           Date today = new Date(System.currentTimeMillis());
           String createdDate = formatter.format(today);
           String startDate = formatter.format(createDate(selected_styear, selected_stmonth, selected_stdate,0,0,0));
           String endDate = formatter.format(createDate(selected_enyear, selected_enmonth, selected_endate,0,0,0));

           assmt.setName((String)name.getValue());
           assmt.setCourse_id(selected_course);
           assmt.setModule_id(selected_module);
           assmt.setLesson_id(selected_lesson);
           assmt.setTime_limit(Integer.parseInt(timelimit.getValue().toString()));
           assmt.setStart_date(startDate);
           assmt.setEnd_date(endDate);
           assmt.setModifiedDateByUser(user.getUsername());
           assmt.setModifiedDateByUserId(user.getId());
           assmt.setModifiedDate(createdDate);
           assmt.setLevel(selected_level);
           assmt.setId(this.id);

           assmt.setParentId(selected_course);
           if (!assmt.getModule_id().equalsIgnoreCase("-1")) {
                assmt.setParentId(selected_module);
           }
            if (!assmt.getLesson_id().equalsIgnoreCase("-1")) {
                assmt.setParentId(selected_lesson);
           }



           if (ispublic != null && ispublic.isChecked()) {
               assmt.setIs_public("1");
           } else {
               assmt.setIs_public("0");
           }
           AssessmentModule asmtModule = (AssessmentModule)Application.getInstance().getModule(AssessmentModule.class);
           asmtModule.updateAssessment(assmt);
           return new Forward("updated");
       }

       public static Date createDate(int year, int month, int date, int hour, int minute, int second) {
		   GregorianCalendar cal = new GregorianCalendar();
		   cal.setLenient(false);
		   cal.clear();

		   Date retDate = null;
		   try {
			cal.set(year, month, date, hour, minute, second);
			retDate = cal.getTime();
		   } catch (Exception e) { }
		   return(retDate);
	   }


}
