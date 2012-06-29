package com.tms.elearning.course.model;


/**
 * Created by IntelliJ IDEA.
 * User: tirupati
 * Date: Oct 6, 2004
 * Time: 4:52:15 PM
 * To change this template use File | Settings | File Templates.
 */
import com.tms.elearning.core.model.LearningContentModule;

import kacang.model.DaoException;
import kacang.model.DataObjectNotFoundException;

import kacang.util.Log;

import java.util.Collection;


public class CourseModule extends LearningContentModule {
    Log log = Log.getLog(getClass());

    public boolean addCourse(Course course) {
        // call dao to save
        CourseModuleDao dao = (CourseModuleDao) getDao();

        try {
            log.debug("Inserting Course " + course.getCourseName());
            dao.insert(course);

            return true;
        } catch (DaoException e) {
            log.error("Error adding course " + e.toString(), e);

            return false;
        }
    }

    public boolean updateCourse(Course course) {
        // call dao to save
        CourseModuleDao dao = (CourseModuleDao) getDao();

        try {
            log.debug("Updating Course " + course.getCourseName());
            dao.update(course);

            return true;
        } catch (DaoException e) {
            log.error("Error updating course " + e.toString(), e);

            return false;
        }
    }

    public boolean deleteCourse(String id) {
        // call dao to delete
        CourseModuleDao dao = (CourseModuleDao) getDao();

        try {
            dao.delete(id);

            return true;
        } catch (DaoException e) {
            log.error("Error delete course " + e.toString(), e);

            return false;
        }
    }

    public boolean deleteRegisteredCourse(String id) {
        // call dao to delete
        CourseModuleDao dao = (CourseModuleDao) getDao();

        try {
            dao.deleteStudentreg(id);

            return true;
        } catch (DaoException e) {
            log.error("Error delete course " + e.toString(), e);

            return false;
        }
    }

    public boolean registerCourse(String id) {
        // call dao to delete
        CourseModuleDao dao = (CourseModuleDao) getDao();

        try {
            dao.insertStudentReg(id);

            return true;
        } catch (DaoException e) {
            log.error("Error delete course " + e.toString(), e);

            //if already course already registered by same user exist, then do something
            return false;
        }
    }

    public Course loadCourse(String id) throws DataObjectNotFoundException {
        CourseModuleDao dao = (CourseModuleDao) getDao();

        try {
            return dao.load(id);
        } catch (DaoException e) {
            log.error("Error loading course " + id, e);
            throw new CourseModuleException(e.toString());
        }
    }

    public Collection queryCourseStatistic(String studentId, String courseId)
        throws DataObjectNotFoundException {
        CourseModuleDao dao = (CourseModuleDao) getDao();

        try {
            return dao.queryCourseStatistic(studentId, courseId);
        } catch (DaoException e) {
            log.error("Error loading each course statistic" + courseId, e);
            throw new CourseModuleException(e.toString());
        }
    }

    public Collection queryModulesStatistic(String moduleId)
        throws DataObjectNotFoundException {
        CourseModuleDao dao = (CourseModuleDao) getDao();

        try {
            return dao.queryModulesStatistic(moduleId);
        } catch (DaoException e) {
            log.error("Error loading each module statistic" + moduleId, e);
            throw new CourseModuleException(e.toString());
        }
    }

    public Collection displayStudentsEachCourse(String id)
        throws DataObjectNotFoundException {
        CourseModuleDao dao = (CourseModuleDao) getDao();

        try {
            return dao.displayStudentsEachCourse(id);
        } catch (DaoException e) {
            log.error("Error displaying students for each course " + id, e);
            throw new CourseModuleException(e.toString());
        }
    }

    public int countStudentsEachCourse(String id)
        throws DataObjectNotFoundException {
        CourseModuleDao dao = (CourseModuleDao) getDao();

        try {
            return dao.countStudentsEachCourse(id);
        } catch (DaoException e) {
            log.error("Error counting student for each course" + id, e);
            throw new CourseModuleException(e.toString());
        }
    }

    public Course loadSubjects(String categoryid)
        throws DataObjectNotFoundException {
        CourseModuleDao dao = (CourseModuleDao) getDao();

        try {
            return dao.loadSubjects(categoryid);
        } catch (DaoException e) {
            log.error("Error loading subjects " + categoryid, e);
            throw new CourseModuleException(e.toString());
        }
    }

    public Course loadList(String categoryid)
        throws DataObjectNotFoundException {
        CourseModuleDao dao = (CourseModuleDao) getDao();

        try {
            return dao.loadList(categoryid);
        } catch (DaoException e) {
            log.error("Error loading subjects " + categoryid, e);
            throw new CourseModuleException(e.toString());
        }
    }

    public Collection findCourses(String name, String category, String userId,
        String sort, boolean desc, int start, int rows) {
        CourseModuleDao dao = (CourseModuleDao) getDao();

        try {
            return dao.query(name, category, userId, sort, desc, start, rows);
        } catch (DaoException e) {
            log.error("Error finding courses " + e.toString(), e);
            throw new CourseModuleException(e.toString());
        }
    }

    public Collection findRegistered(boolean desc, int start, int rows) {
        CourseModuleDao dao = (CourseModuleDao) getDao();

        try {
            return dao.queryRegisteredCourses(desc, start, rows);
        } catch (DaoException e) {
            log.error("Error finding Registered Courses " + e.toString(), e);
            throw new CourseModuleException(e.toString());
        }
    }

    public Collection findRegistered(String course, String category,
        boolean desc, int start, int rows, String studentId) {
        CourseModuleDao dao = (CourseModuleDao) getDao();

        try {
            return dao.queryStudentReg(course, category, desc, start, rows,
                studentId);
        } catch (DaoException e) {
            log.error("Error finding Registered Courses " + e.toString(), e);
            throw new CourseModuleException(e.toString());
        }
    }

    public Collection findSubjects(String category, String sort, boolean desc,
        int start, int rows) {
        CourseModuleDao dao = (CourseModuleDao) getDao();

        try {
            return dao.querySubjects(category, sort, desc, start, rows);
        } catch (DaoException e) {
            log.error("Error finding subjects " + e.toString(), e);
            throw new CourseModuleException(e.toString());
        }
    }

    public Collection findList(String categoryid, String sort, boolean desc,
        int start, int rows) {
        CourseModuleDao dao = (CourseModuleDao) getDao();

        try {
            return dao.queryList(categoryid, sort, desc, start, rows);
        } catch (DaoException e) {
            log.error("Error finding subjects " + e.toString(), e);
            throw new CourseModuleException(e.toString());
        }
    }

    public int countList(String categoryid) {
        CourseModuleDao dao = (CourseModuleDao) getDao();

        try {
            return dao.countList(categoryid);
        } catch (DaoException e) {
            log.error("Error counting in List " + e.toString(), e);
            throw new CourseModuleException(e.toString());
        }
    }

    public int countRegistered() {
        CourseModuleDao dao = (CourseModuleDao) getDao();

        try {
            return dao.countRegisteredCourses();
        } catch (DaoException e) {
            log.error("Error counting Registered Courses " + e.toString(), e);
            throw new CourseModuleException(e.toString());
        }
    }

    public int countRegisteredStudent(String course, String category,
        String studentId) {
        CourseModuleDao dao = (CourseModuleDao) getDao();

        try {
            return dao.countStudentReg(course, category, studentId);
        } catch (DaoException e) {
            log.error("Error counting Registered Courses " + e.toString(), e);
            throw new CourseModuleException(e.toString());
        }
    }

    public int countSubjects(String category) {
        CourseModuleDao dao = (CourseModuleDao) getDao();

        try {
            return dao.countSubjects(category);
        } catch (DaoException e) {
            log.error("Error counting categories " + e.toString(), e);
            throw new CourseModuleException(e.toString());
        }
    }

    public int countCourses(String name, String category) {
        CourseModuleDao dao = (CourseModuleDao) getDao();

        try {
            return dao.count(name, category);
        } catch (DaoException e) {
            log.error("Error counting courses " + e.toString(), e);
            throw new CourseModuleException(e.toString());
        }
    }

    public Course courseExists(String name) {
        CourseModuleDao dao = (CourseModuleDao) getDao();

        try {
            return dao.courseExists(name);
        } catch (DaoException e) {
            log.error("Error finding course " + e.toString(), e);
            throw new CourseModuleException(e.toString());
        }
    }

    public Course courseExists(String name, String id) {
        CourseModuleDao dao = (CourseModuleDao) getDao();

        try {
            return dao.courseExists(name, id);
        } catch (DaoException e) {
            log.error("Error finding course " + e.toString(), e);
            throw new CourseModuleException(e.toString());
        }
    }


    public Course checkCanAdd(String name, String categoryid){
        CourseModuleDao dao = (CourseModuleDao) getDao();

        try {
            return dao.checkCanAdd(name, categoryid);
        } catch (DaoException e) {
            log.error("Cannot add this course because same category exist " + e.toString(), e);
            throw new CourseModuleException(e.toString());
        }



    }



    public Course checkCanAddSameId(String name, String categoryid, String id){
        CourseModuleDao dao = (CourseModuleDao) getDao();

        try {
            return dao.checkCanAddSameId(name, categoryid, id);
        } catch (DaoException e) {
            log.error("Cannot add this course because same category exist " + e.toString(), e);
            throw new CourseModuleException(e.toString());
        }



    }

    //load caegory for filter
    public Collection loadCategory() {
        CourseModuleDao dao = (CourseModuleDao) getDao();

        try {
            return dao.loadCategory();
        } catch (DaoException e) {
            log.error("Error loading category " + e.toString(), e);
            throw new CourseModuleException(e.toString());
        }
    }

    public Collection loadStudents(String courseId) {
        CourseModuleDao dao = (CourseModuleDao) getDao();

        try {
            return dao.studentsFromCourse(courseId);
        } catch (DaoException e) {
            log.error("Error loading category " + e.toString(), e);
            throw new CourseModuleException(e.toString());
        }
    }

    public String showSynopsis(String id) {
        CourseModuleDao dao = (CourseModuleDao) getDao();

        try {
            return dao.showSynopsisOfCourse(id);
        } catch (DaoException e) {
            Log.getLog(getClass()).warn("Error retrieving synopsis from course table");
        }

        return null;
    }

    public void setActivateCourse(String id){


        CourseModuleDao dao = (CourseModuleDao) getDao();
        try {
            dao.setActivateCourse(id);
        } catch (DaoException e) {
              Log.getLog(getClass()).warn("Error set flag for activation in cel_content_course");
        }

    }


        public void setDeactivateCourse(String id){


        CourseModuleDao dao = (CourseModuleDao) getDao();
        try {
            dao.setDeactivateCourse(id);
        } catch (DaoException e) {
              Log.getLog(getClass()).warn("Error reverse flag for activation in cel_content_course");
        }

    }


    public Collection studentRegistered(String course_id){

        CourseModuleDao dao = (CourseModuleDao) getDao();
        try {
            return dao.studentRegistered(course_id);
        } catch (DaoException e) {
            Log.getLog(getClass()).warn("Cannot access registered student database to retrieve data on StudentSelectBox");
        }
    return null;
    }

}
