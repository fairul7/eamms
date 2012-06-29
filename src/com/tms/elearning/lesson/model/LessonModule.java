package com.tms.elearning.lesson.model;

import com.tms.elearning.course.model.Course;
import com.tms.elearning.course.model.CourseModuleDao;
import com.tms.elearning.course.model.CourseModuleException;
import com.tms.elearning.core.model.LearningContentModule;
import com.tms.cms.core.model.InvalidKeyException;
import kacang.util.Log;
import kacang.util.UuidGenerator;
import kacang.model.DaoException;
import kacang.model.DataObjectNotFoundException;

import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: tirupati
 * Date: Oct 26, 2004
 * Time: 4:23:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class LessonModule extends LearningContentModule {

    Log log = Log.getLog(getClass());

    public boolean addLesson(Lesson lesson) {
        // generate ID
       // String uuid = UuidGenerator.getInstance().getUuid();
       // lesson.setId(uuid);

        // call dao to save
        LessonDao dao = (LessonDao) getDao();
        try {
            log.debug("Inserting Lesson " + lesson.getName());
            dao.insert(lesson);
            dao.storeFile(lesson);
            return true;
        } catch (DaoException e) {
            log.error("Error adding Lesson " + e.toString(), e);
            return false;
        } catch (InvalidKeyException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            return false;
        }
    }

    public boolean updateLesson(Lesson lesson) {
        // call dao to save
        LessonDao dao = (LessonDao)getDao();
        try {
            log.debug("Updating Lesson " + lesson.getLessonName());
            dao.update(lesson);
            dao.storeFile(lesson);
            return true;
        } catch (DaoException e) {
            log.error("Error updating Lesson " + e.toString(), e);
            return false;
        }

        catch (InvalidKeyException e) {
            log.error("Error updating Lesson in Storage" + e.toString(), e);
            return false;
            }
    }

    public boolean deleteLesson(String id) {
        // call dao to delete
        LessonDao dao = (LessonDao)getDao();
        try {
            dao.delete(id);
            return true;
        } catch (DaoException e) {
            log.error("Error delete Lesson " + e.toString(), e);
            return false;
        }
    }

    public Lesson loadLesson(String id) throws DataObjectNotFoundException {
        LessonDao dao = (LessonDao)getDao();
        try {
            return dao.load(id);
        } catch (DaoException e) {
            log.error("Error loading Lesson " + id, e);
            throw new LessonException(e.toString());
        }
    }

    public Collection findLessons(String name,String course,String folder,String userId, String sort, boolean desc, int start, int rows){
        LessonDao dao = (LessonDao)getDao();



        try {
            return dao.query(name,course,folder,userId, sort, desc, start, rows);
        } catch (DaoException e) {
            log.error("Error finding Lessons " + e.toString(), e);
            throw new LessonException(e.toString());
        }
    }


    public Collection findLessons(String moduleId, String sort, boolean desc, int start, int rows){
        LessonDao dao = (LessonDao)getDao();

        try {
            return dao.queryLessonsByFolder(moduleId, sort, desc, start, rows);
        } catch (DaoException e) {
            log.error("Error finding Lessons " + e.toString(), e);
            throw new LessonException(e.toString());
        }
    }











    public Collection findCourseLessons(String name,String course, String sort, boolean desc, int start, int rows){
        LessonDao dao = (LessonDao)getDao();

        try {
            return dao.queryCourseLessons(name,course, sort, desc, start, rows);
        } catch (DaoException e) {
            log.error("Error finding Course Lessons " + e.toString(), e);
            throw new LessonException(e.toString());
        }
    }
    public int countLessons(String name,String course, String folder) {
        LessonDao dao = (LessonDao)getDao();
        try {
            return dao.count(name, course,folder);
        } catch (DaoException e) {
            log.error("Error counting Lessons " + e.toString(), e);
            throw new LessonException(e.toString());
        }
    }


        public int countLessons(String moduleId) {
        LessonDao dao = (LessonDao)getDao();
        try {
            return dao.countByFolder(moduleId);
        } catch (DaoException e) {
            log.error("Error counting Lessons " + e.toString(), e);
            throw new LessonException(e.toString());
        }
    }










    public int countCourseLessons(String name,String course) {
            LessonDao dao = (LessonDao)getDao();
            try {
                return dao.countCourseLessons(name,course);
            } catch (DaoException e) {
                log.error("Error counting Course Lessons " + e.toString(), e);
                throw new LessonException(e.toString());
            }
        }

    public Lesson lessonExists(String name) {
        LessonDao dao = (LessonDao)getDao();

        try {
            return dao.lessonExists(name);
        } catch (DaoException e)  {
            log.error("Error finding lesson " + e.toString(), e);
            throw new CourseModuleException(e.toString());
        }
    }

    public Lesson checkLessonExists(String name,String courseid,String folderid){
        LessonDao dao = (LessonDao)getDao();

        try {
            return dao.checkLessonExists(name,courseid,folderid);
        } catch (DaoException e)  {
            log.error("Same lesson for the same module exist " + e.toString(), e);
            throw new CourseModuleException(e.toString());
        }



    }


     public Lesson checkLessonExistsWithLesson(String name,String courseid,String folderid,String lessonid){
        LessonDao dao = (LessonDao)getDao();

        try {
            return dao.checkLessonExistsWithLesson(name,courseid,folderid,lessonid);
        } catch (DaoException e)  {
            log.error("Same lesson for the same module exist " + e.toString(), e);
            throw new CourseModuleException(e.toString());
        }



    }

    public Lesson lessonExists(String name, String id) {
        LessonDao dao = (LessonDao)getDao();
        try {
            return dao.lessonExists(name, id);
        } catch (DaoException e)  {
            log.error("Error finding lesson " + e.toString(), e);
            throw new CourseModuleException(e.toString());
        }
    }


    //for filter in table

    public Collection loadModule(){


            LessonDao dao = (LessonDao)getDao();

        try {
            return dao.loadModule();
        } catch (DaoException e) {
            log.error("Error finding modules" + e.toString(), e);
            throw new LessonException(e.toString());
        }

    }


    public void setActivationLesson(String id){

        LessonDao dao = (LessonDao) getDao();

        try {
            dao.setActivationLesson(id);
        } catch (DaoException e) {
            Log.getLog(getClass()).warn("Error set flag for activation in cel_content_lesson");
        }
    }

    public void setDeactivationLesson(String id){

        LessonDao dao = (LessonDao) getDao();
        try {
            dao.setDeactivationLesson(id);
        } catch (DaoException e) {
            Log.getLog(getClass()).warn("Error reverse flag for activation in cel_content_lesson");
        }
    }

    public Collection getAssessmentByLesson(String id){

        LessonDao dao= (LessonDao) getDao();

        try {
          return dao.getAssessmentByLesson(id);
        } catch (DaoException e) {
            Log.getLog(getClass()).warn("Error retrieving assessment with keyword of 'lesson id'");
        }

        return null;
    }

    public Lesson checkActive(String id){
        LessonDao dao =(LessonDao) getDao();
        try{
           return dao.checkActive(id);
        } catch(DaoException e){
            Log.getLog(getClass()).warn("cannot check whether lesson is actived or not");
        }

       return null;

    }


}
