package com.tms.elearning.testware.ui;

import kacang.stdui.*;
import kacang.stdui.validator.ValidatorMessage;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.stdui.validator.ValidatorRange;
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
import java.text.DateFormat;
import java.util.StringTokenizer;
import java.io.Serializable;

import org.apache.commons.collections.SequencedHashMap;

/**
 * Created by IntelliJ IDEA.
 * User: mahe
 * Date: Nov 23, 2004
 * Time: 4:31:01 PM
 * To change this template use Options | File Templates.
 */
public class AssessmentEditForm extends Form implements Serializable{
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
    protected ValidatorMessage vmsg_module;
    protected ValidatorMessage vmsg_lesson;
    protected ValidatorNotEmpty lessonEmpty;
    protected ValidatorRange timeRangeValidator;

    private String uid;
    private String uid2;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String id;

    public String getAssessmentId() {
        return assessmentId;
    }

    public void setAssessmentId(String assessmentId) {
        this.assessmentId = assessmentId;
    }

    private String assessmentId;


    private Assessment asm;





    public String getDefaultTemplate() {
        return "elearning/assessmentEditForm";
    }

     public void init() {
        super.init();
        setMethod("post");
        initForm();
    }

    public void onRequest(Event evt) {
        super.onRequest(evt);
        initForm();
    }

    public void initForm() {

        Application application = Application.getInstance();
        AssessmentModule asmtModule = (AssessmentModule)Application.getInstance().getModule(AssessmentModule.class);
        WidgetManager manager = getWidgetManager();
        User user = manager.getUser();
        String userId=user.getUsername();
        name = new TextField("name");
        name.setSize("30");
        name.setMaxlength("255");


        ValidatorNotEmpty v_name = new ValidatorNotEmpty("v_name", "");
        name.addChild(v_name);
        addChild(name);

        course_select = new SelectBox("course");
        course_select.addChild(vmsg_course = new ValidatorMessage("vmsg_course"));
        course_select.setOptions("-1="+application.getMessage("eLearning.topic.label.coursemenu"));

        TopicModuleDao tpcDao = (TopicModuleDao)Application.getInstance().getModule(TopicModule.class).getDao();
        FolderModuleDao modDao = (FolderModuleDao)Application.getInstance().getModule(FolderModule.class).getDao();
        LessonDao lsnDao = (LessonDao)Application.getInstance().getModule(LessonModule.class).getDao();

        try {

            Collection courses =  tpcDao.getCourses(userId);


            HashMap hash = new HashMap();
            for(Iterator i = courses.iterator();i.hasNext();){
                Topic temp =(Topic)i.next();
                hash.put(temp.getCourse_id(),temp.getCourse_name());
            }
            course_select.setOptionMap(hash);
        } catch (DaoException e) {
            Log.getLog(getClass()).error(e.toString(),e);
        }
        //course_select.setOnChange("document.forms['"+getAbsoluteName()+"'].submit()");
        course_select.addChild(vmsg_course = new ValidatorMessage("vmsg_course"));

        addChild(course_select);

        module_select = new SelectBox("module");
        module_select.setOptions("-1="+application.getMessage("eLearning.lesson.label.modulemenu"));
        if (this.id != null) {
        try {
            Assessment asm = asmtModule.getAssessment(this.id);

            Collection mods = modDao.getModuleList(asm.getCourse_id(),userId);




            HashMap hash = new HashMap();
            for(Iterator i = mods.iterator();i.hasNext();){
                Folder temp =(Folder)i.next();
                hash.put(temp.getId(),temp.getFolderName());
            }
            module_select.setOptionMap(hash);
        } catch (DaoException e) {
            Log.getLog(getClass()).error(e.toString(),e);
        }
       }

        module_select.addChild(vmsg_module = new ValidatorMessage("vmsg_module"));

        addChild(module_select);

        lesson_select = new SelectBox("lesson");
        lesson_select.setOptions("-1="+application.getMessage("eLearning.lesson.label.lessonmenu"));
        if (this.id != null) {
        try {
            Assessment asm = asmtModule.getAssessment(id);
            Collection lsn = lsnDao.getLessonList(asm.getCourse_id(),userId);
            HashMap hash = new HashMap();
            for(Iterator i = lsn.iterator();i.hasNext();){
                Lesson temp =(Lesson)i.next();
                hash.put(temp.getId(),temp.getLessonName());
            }
            lesson_select.setOptionMap(hash);
        } catch (DaoException e) {
            Log.getLog(getClass()).error(e.toString(),e);
        }
        }
        lesson_select.addChild(vmsg_lesson = new ValidatorMessage("vmsg_lesson"));
        lessonEmpty = new ValidatorNotEmpty("notEmptyLesson","");
        lesson_select.addChild(lessonEmpty);
        addChild(lesson_select);

        timelimit = new TextField("timelimit");
        timelimit.setSize("5");
        timelimit.setMaxlength("5");
        timeRangeValidator = new ValidatorRange("timeRangeValidator","out of range",Double.valueOf("0.0"), Double.valueOf("200"));
        timelimit.addChild(timeRangeValidator);
        addChild(timelimit);

        level = new SelectBox("level");
        level.setOptions("1="+application.getMessage("eLearning.assessment.easy")+";2="+application.getMessage("eLearning.assessment.intermediate")+";3="+application.getMessage("eLearning.assessment.difficult"));
              addChild(level);



        String months = "0="+application.getMessage("eLearning.assessment.january")+";1="+application.getMessage("eLearning.assessment.february")+";2="+application.getMessage("eLearning.assessment.march")+";3="+application.getMessage("eLearning.assessment.april")+";4="+application.getMessage("eLearning.assessment.may")+";5="+application.getMessage("eLearning.assessment.june")+";6="+application.getMessage("eLearning.assessment.july")+";" +
                      "7="+application.getMessage("eLearning.assessment.august")+";8="+application.getMessage("eLearning.assessment.september")+";9="+application.getMessage("eLearning.assessment.october")+";10="+application.getMessage("eLearning.assessment.november")+";11="+application.getMessage("eLearning.assessment.december");
        String dates = "1=1;2=2;3=3;4=4;5=5;6=6;7=7;8=8;9=9;10=10;11=11;12=12;13=13;14=14;15=15;16=16;17=17;18=18;19=19;20=20;21=21;22=22;23=23;24=24;25=25;26=26;27=27;28=28;29=29;30=30;31=31";

        stmonth = new SelectBox("stmonth");
        stmonth.setOptions(months);
        addChild(stmonth);

        stdate = new SelectBox("stdate");
        stdate.setOptions(dates);
        addChild(stdate);

        enmonth = new SelectBox("enmonth");
        enmonth.setOptions(months);
        addChild(enmonth);

        endate = new SelectBox("endate");
        endate.setOptions(dates);
        addChild(endate);

        Calendar  cal = Calendar.getInstance();

        String thisYear = Integer.toString(cal.get(Calendar.YEAR));
        String nextYear = Integer.toString(cal.get(Calendar.YEAR)+1);

        styear = new SelectBox("styear");
        styear.setOptions(thisYear+"="+thisYear+";"+nextYear+"="+nextYear);
        addChild(styear);

        enyear = new SelectBox("enyear");
        enyear.setOptions(thisYear+"="+thisYear+";"+nextYear+"="+nextYear);
        addChild(enyear);

        ispublic = new CheckBox("ispublic");
        addChild(ispublic);

        submit = new Button("submit", application.getMessage("eLearning.assessment.update"));
        addChild(submit);

        cancel = new Button("cancel",application.getMessage("eLearning.assessment.cancel"));

        addChild(cancel);


        addFromQuestionBank = new Button("addFromQuestionBank", application.getMessage("eLearning.assessment.addquestionfrombank"));
        addChild(addFromQuestionBank);
        viewQuestions = new Button("viewQuestions", application.getMessage("eLearning.assessment.viewquestion"));
        addChild(viewQuestions);

        if (this.id != null) {
        try {
                asm = asmtModule.getAssessment(this.id);
                module_select.setSelectedOption(asm.getModule_id());
                lesson_select.setSelectedOption(asm.getLesson_id());
                course_select.setSelectedOption(asm.getCourse_id());
                name.setValue(asm.getName());
                level.setSelectedOption(Integer.toString(asm.getLevel()));
                timelimit.setValue(Double.toString(asm.getTime_limit()));
                int startDateArray [] ={0,0,0};
                int endDateArray [] ={0,0,0};
                //filter start date and end date


                   StringTokenizer st=new StringTokenizer(asm.getStart_date(),"- ");
                   int i=0;
                   while(st.hasMoreTokens() && i<3  ){
                       startDateArray[i] = Integer.parseInt(st.nextToken());

                       i++;
                   }

                   StringTokenizer st2=new StringTokenizer(asm.getEnd_date(),"- ");
                   int j=0;
                   while(st2.hasMoreTokens() && j<3  ){
                       endDateArray [j] = Integer.parseInt(st2.nextToken());

                       j++;
                   }

                 styear.setSelectedOption(String.valueOf(startDateArray[0]));
                 stmonth.setSelectedOption(String.valueOf(startDateArray[1] -1 ));
                 stdate.setSelectedOption(String.valueOf(startDateArray[2]));

                 enyear.setSelectedOption(String.valueOf(endDateArray[0]));
                 enmonth.setSelectedOption(String.valueOf(endDateArray[1] -1 ));
                 endate.setSelectedOption(String.valueOf(endDateArray[2]));




                if ("1".equals(asm.getIs_public() )) {
                    ispublic.setChecked(true);
                } else {
                    ispublic.setChecked(false);
                }

               } catch (Exception e) {
                    Log.getLog(getClass()).error(application.getMessage("eLearning.assessment.label")+" " + this.id + " "+ application.getMessage("eLearning.course.label.cannotretrieve") + e.toString(),e);
                    throw new AssessmentModuleException(e.toString());
               }

        }
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

                return new Forward("viewQuestions");
         }else  if(addFromQuestionBank.getAbsoluteName().equals(button)){
                evt.getRequest().getSession().setAttribute("courseid",asm.getCourse_id());
                evt.getRequest().getSession().setAttribute("examid",this.id);
                evt.getRequest().getSession().setAttribute("id",this.id);

                return new Forward("questionbank");
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
                  // Collection moduleList = (Collection)module_select.getValue();
                  // String selected_module = (String)moduleList.iterator().next();

                   Collection courseList = (Collection)course_select.getValue();
                   String selected_course = (String)courseList.iterator().next();

                   Collection lessons = lsnDao.getLessonList(selected_course,userId);
                   lesson_select.removeAllOptions();
                   lesson_select.setOptions("-1="+ application.getMessage("eLearning.lesson.label.lessonmenu"));
                   for(Iterator i = lessons.iterator();i.hasNext();){
                       Lesson temp =(Lesson)i.next();
                       lesson_select.setOptions(temp.getId()+"="+temp.getLessonName());
                   }
              } catch (DaoException e) {
                       Log.getLog(getClass()).error(e.toString(),e);
              }
              setInvalid(true); //System.out.println("inside2 ***********************");
           }   else {
               Collection selectedList = (Collection)course_select.getValue();
               String selected = (String)selectedList.iterator().next();
               if (selected.equalsIgnoreCase("-1")) {
                   vmsg_course.showError("");
                   this.setInvalid(true);
               }

             Collection selectedList2 = (Collection)module_select.getValue();
                           String selected2 = (String)selectedList2.iterator().next();
                           if (selected2.equalsIgnoreCase("-1") ) {
                               vmsg_module.showError("");
                               this.setInvalid(true);
                           }

             Collection selectedList3 = (Collection)lesson_select.getValue();
                           String selected3 = (String)selectedList3.iterator().next();
                           if (selected3.equalsIgnoreCase("-1")) {
                               vmsg_lesson.showError("");
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
           AssessmentModule asmtModule = (AssessmentModule)Application.getInstance().getModule(AssessmentModule.class);
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



           Date startingDate = createDate(selected_styear,
                              selected_stmonth, selected_stdate, 0, 0, 0);

           Date endingDate = createDate(selected_enyear,
                              selected_enmonth, selected_endate, 0, 0, 0);

                   //check date
                  if(startingDate.after(endingDate))
                  {


                      return new Forward("dateproblem");

                  }

           String startDate = formatter.format(startingDate);
           String endDate = formatter.format(endingDate);


           assmt.setName((String)name.getValue());
           assmt.setCourse_id(selected_course);
           assmt.setModule_id(selected_module);
           assmt.setLesson_id(selected_lesson);
           assmt.setTime_limit(Double.parseDouble(timelimit.getValue().toString()));
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


    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;

    }

    public String getUid2() {
        return uid2;
    }

    public void setUid2(String uid2) {
        this.uid2 = uid2;
    }


        public Map getModule() {
        LessonDao dao = (LessonDao) Application.getInstance()
                                               .getModule(LessonModule.class)
                                               .getDao();

        try {
            Map tempMap = new SequencedHashMap();
            tempMap.put("-1", Application.getInstance().getMessage("eLearning.lesson.label.modulemenu"));
            for (Iterator iterator = dao.getFolders(uid, 1).iterator();
                 iterator.hasNext();) {
                Lesson lesson = (Lesson) iterator.next();
                tempMap.put(lesson.getFolderId(), lesson.getFolderName());
            }

            return tempMap;
        } catch (DaoException e) {
            Log.getLog(getClass()).warn("can't populate data in selectbox for assessment form");
        }

        return null;
    }

    public Map getLesson() {
        LessonDao ldao = (LessonDao) Application.getInstance()
                                                .getModule(LessonModule.class)
                                                .getDao();
        Collection col = null;

        try {
            col = ldao.getLessonsByModule(uid2);

            Map tempMap = new SequencedHashMap();

            tempMap.put("-1", Application.getInstance().getMessage("eLearning.lesson.label.lessonmenu"));
            for (Iterator iterator = col.iterator(); iterator.hasNext();) {
                Lesson lesson = (Lesson) iterator.next();
                tempMap.put(lesson.getId(), lesson.getLessonName());

            }

            return tempMap;
        } catch (DaoException e) {
            Log.getLog(getClass()).warn("can't populate data in selectbox for assessment form");
        }

        return null;
    }


}
