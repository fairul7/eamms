package com.tms.elearning.core.model;

import kacang.model.DataSourceDao;
import kacang.model.DaoException;

import java.util.Collection;

import com.tms.elearning.course.model.Course;
import com.tms.elearning.folder.model.Folder;
import com.tms.elearning.lesson.model.Lesson;
import com.tms.elearning.testware.model.Assessment;


/**
 * Created by IntelliJ IDEA.
 * User: vivek
 * Date: Mar 4, 2005
 * Time: 5:12:42 PM
 * To change this template use File | Settings | File Templates.
 */
public class LearningDao extends DataSourceDao{


    public Collection query() throws DaoException {


        String query = "select c.id as courseid, c.name as coursename, m.id as moduleid, m.name as moduledulename, "+
                "l.id as lessonid,l.name as lessonname,a.id as examid,a.name as examname from cel_content_course c "+
                " inner join cel_content_module m on c.id = m.courseid "+
                " inner join cel_content_lesson l on c.id = l.courseid and m.id = l.folderid "+
                " inner join cel_content_assessment a on c.id = a.course_id and m.id = a.module_id and l.id = a.lesson_id ";

        return super.select(query, Learning.class, null, 0, -1);
    }

    public  Collection getCoursesQuery(String cid) throws DaoException{
        String courses = "select id,name from cel_content_course where id = '"+cid+"'";
        return super.select(courses,Course.class,null,0,-1);
    }

    public  Collection getModulesQuery() throws DaoException{
        String modules = "select distinct (id), name from cel_content_module";
        return super.select(modules,Folder.class,null,0,-1);
    }

    public  Collection getLessonsQuery() throws DaoException{
        String lessons = "select distinct (id), name from cel_content_lesson";
        return super.select(lessons,Lesson.class,null,0,-1);
    }

    public  Collection getAssessmentQuery() throws DaoException{
        String exams = "select distinct (id), name from cel_content_assessment";
        return super.select(exams,Assessment.class,null,0,-1);
    }

    public Collection getModules(String courseId) throws DaoException {
        String modules = "select id, name from cel_content_module where courseid = '"+courseId+"'";
        return super.select(modules,Folder.class,null,0,-1);
    }

    public  Collection getLessons(String moduleId) throws DaoException{
        String lessons = "select id, name from cel_content_lesson where folderid = '"+moduleId+"'";
        return super.select(lessons,Lesson.class,null,0,-1);
    }

    public  Collection getAssessments(String lessonId) throws DaoException{
        String exams = "select id, name from cel_content_assessment where lesson_id = '"+lessonId+"'";
        return super.select(exams,Assessment.class,null,0,-1);
    }

}
