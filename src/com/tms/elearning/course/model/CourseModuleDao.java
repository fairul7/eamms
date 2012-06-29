package com.tms.elearning.course.model;

import com.tms.elearning.core.model.DefaultLearningContentDao;
import com.tms.elearning.coursecategory.model.Category;
import com.tms.elearning.lesson.model.Lesson;
import com.tms.elearning.testware.model.StudentStatistic;

import kacang.Application;

import kacang.model.DaoException;
import kacang.model.DataObjectNotFoundException;

import kacang.services.security.User;

import kacang.util.Log;

import java.util.Collection;
import java.util.HashMap;


public class CourseModuleDao extends DefaultLearningContentDao {
    protected String getTableName() {
        return "cel_content_course";
    }

    protected Class getContentObjectClass() {
        return Course.class;
    }

    public void init() throws DaoException {
    	try {
            super.update("ALTER TABLE cel_content_studentreg DROP PRIMARY KEY", null);
        } catch (DaoException e) {
            Log.getLog(getClass()).debug("Error alter table cms_content_studentreg");
        }
        
        try {
            super.update("CREATE TABLE cel_content_studentreg (" +
                "  id varchar(255) NOT NULL default '0'," +
                "  student varchar(255) default NULL," + ")", null);
        } catch (DaoException e) {
            Log.getLog(getClass()).debug("Error creating table cms_content_studentreg");
        }
        
        

        try {
            super.update("CREATE TABLE cel_content_course (" +
                "  id varchar(50) NOT NULL default ''," +
                "  name varchar(50) default NULL," +
                "  author varchar(50) default NULL," +
                "  instructor varchar(50) default NULL," +
                "  synopsis text default NULL," +
                "  categoryid varchar(50) NOT NULL default ''," +
                "  public ENUM('0','1')  DEFAULT \"0\" NOT NULL," +
                "  createdDate DATETIME default '0000-00-00 00:00:00'," +
                "  createdByUser varchar(50) default NULL," +
                "  createdByUserId varchar(50) default NULL," +
                "  PRIMARY KEY  (id)" + ")", null);
        } catch (DaoException e) {
            Log.getLog(getClass()).debug("Error creating table cms_content_course");
        }

        /*
          super.update("CREATE TABLE cel_content_status (" +
                  "  id varchar(255) NOT NULL default '0'," +
                  "  parentId varchar(255) default NULL," +
                  "  new char(1) default '0'," +
                  "  modified char(1) default '0'," +
                  "  deleted char(1) default '0'," +
                  "  archived char(1) default '0'," +
                  "  active char(1) default '0'," +
                  "  public char(1) default '0'," +
                  "  ordering int(10) unsigned default '0'," +
                  "  publishVersion int(10) unsigned default NULL," +
                  "  modifiedDate datetime default NULL," +
                  "  modifiedDateByUser varchar(255) default NULL," +
                  "  modifiedDateByUserId varchar(255) default NULL," +
                  "  deletedDate datetime default NULL," +
                  "  deletedByUser varchar(255) default NULL," +
                  "  deletedByUserId varchar(255) default NULL," +
                  "  archivedDate datetime default NULL," +
                  "  archivedByUser varchar(255) default NULL," +
                  "  archivedByUserId varchar(255) default NULL," +
                  "  PRIMARY KEY  (id)" +
                  ")", null);
          */
        try {
            super.update("CREATE TABLE cel_content_lesson (" +
                "  id varchar(50) NOT NULL default '0'," +
                "  courseId varchar(50) default NULL," +
                "  folderid varchar(50) default NULL," +
                "  name varchar(50) default NULL," + "  brief text," +
                "  public ENUM('0','1')  DEFAULT \"0\" NOT NULL," +
                "  createdDate datetime NOT NULL default '0000-00-00 00:00:00'," +
                "  createdByUser varchar(50) default NULL," +
                "  createdByUserId varchar(50) default NULL," +
                "  fileName varchar(255) default NULL," +
                "  filePath varchar(255) default NULL" + ")", null);
        } catch (DaoException e) {
            Log.getLog(getClass()).debug("Error creating table cel_content_lesson");
        }

        try {
            super.update("CREATE TABLE cel_content_module (" +
                "  id varchar(50) NOT NULL default ''," +
                "  courseId varchar(50) NOT NULL default ''," +
                "  name varchar(50) default NULL," +
                "  introduction varchar(255) default NULL," +
                "  public ENUM('0','1')  DEFAULT \"0\" NOT NULL," +
                "  createdDate datetime NOT NULL default '0000-00-00 00:00:00'," +
                "  createdByUser varchar(25) default NULL," +
                "  createdByUserId varchar(25) default NULL," +
                "  PRIMARY KEY  (id)" + ")", null);
        } catch (DaoException e) {
            Log.getLog(getClass()).debug("Error creating table cms_content_module");
        }
    }

    public void insert(Course course) throws DaoException {
        super.update("INSERT INTO cel_content_course (id,name,author,synopsis,instructor,categoryid,public,createdDate,createdByUser,createdByUserId) VALUES (#id#,#name#,#author#,#synopsis#,#instructor#,#categoryid#,#is_public#,#createdDate#,#createdByUser#,#createdByUserId#)",
            course);

        //  super.update("INSERT INTO cel_content_status (id,new) VALUES (#id#,1)", course);
    }

    public void update(Course course) throws DaoException {
        super.update("UPDATE cel_content_course SET name=#name#, author=#author#, synopsis=#synopsis#, instructor=#instructor#, categoryid=#categoryid#, public=#is_public# WHERE id=#id#",
            course);

        //    super.update("INSERT INTO cel_content_status (id,modified,modifiedDate,modifiedByUser,modifiedByUserId) VALUES (#id#,'Y',#createdDate#,#createdByUser#,#createdByUserId#)", course);
    }

    public void updateCourse(String id) throws DaoException {
        Object[] args = new String[] { id };
        super.update("UPDATE cel_content_course SET registered=1 WHERE id=?",
            args);

        //super.update("INSERT INTO cel_content_status (id,deleted,deletedDate,deletedByUser,deletedByUserId) VALUES (#id#,'Y',#createdDate#,#createdByUser#,#createdByUserId#)",course);
    }

    public void insertStudentReg(String id) throws DaoException {
        Application application = Application.getInstance();
        kacang.services.security.User user = application.getCurrentUser();
        Student student = new Student();
        student.setStudent(user.getId());
        student.setId(id); // put course id

        super.update("INSERT INTO cel_content_studentreg (id,student) VALUES (#id#,#student#)",
            student);
    }

    public void delete(String id) throws DaoException {
        Object[] args = new String[] { id };
        super.update("DELETE cel_content_questions.* FROM cel_content_questions, cel_content_assessment WHERE cel_content_questions.assessment_id = cel_content_assessment.id AND cel_content_assessment.course_id=?",
            args);
        super.update("DELETE FROM cel_content_course WHERE id=?", args);
        super.update("DELETE FROM cel_content_module WHERE courseId=?", args);
        super.update("DELETE FROM cel_content_lesson WHERE courseId=?", args);
        super.update("DELETE FROM cel_content_assessment WHERE course_id=?",
            args);

        //super.update("INSERT INTO cel_content_status (id,deleted,deletedDate,deletedByUser,deletedByUserId) VALUES (#id#,'Y',#createdDate#,#createdByUser#,#createdByUserId#)",course);
    }

    public void deleteStudentreg(String id) throws DaoException {
        Object[] args = new String[] {
                id, Application.getInstance().getCurrentUser().getId()
            };
        super.update("DELETE FROM cel_content_studentreg WHERE id=? AND student=?",
            args);

        //super.update("INSERT INTO cel_content_status (id,deleted,deletedDate,deletedByUser,deletedByUserId) VALUES (#id#,'Y',#createdDate#,#createdByUser#,#createdByUserId#)",course);
    }

    public Course load(String id)
        throws DaoException, DataObjectNotFoundException {
        Object[] args = { id };
        Collection results = super.select("SELECT course.id,course.name,course.author,course.synopsis,course.instructor,category.category, course.categoryid, course.public as is_public FROM cel_content_course as course, cel_content_course_category as category WHERE course.categoryid = category.id AND course.id=?",
                Course.class, args, 0, 1);

        if (results.size() == 0) {
            //not error, something dont have record in database
            //   throw new DataObjectNotFoundException();
            return null;
        } else {
            return (Course) results.iterator().next();
        }
    }

    public Collection displayStudentsEachCourse(String id)
        throws DaoException, DataObjectNotFoundException {
        Object[] args = { id };
        Collection results = super.select("SELECT distinct(cel_content_studentreg.student) as  student FROM cel_content_studentreg where cel_content_studentreg.id=?",
                Student.class, args, 0, -1);

        return results;
    }

    public int countStudentsEachCourse(String id) throws DaoException {
        Object[] args = { id };

        Collection list = super.select("select count(cel_content_studentreg.id) as totalStudent from cel_content_studentreg RIGHT JOIN cel_content_course on cel_content_studentreg.id = cel_content_course.id AND cel_content_course.id=?",
                HashMap.class, args, 0, 1);
        HashMap map = (HashMap) list.iterator().next();

        return Integer.parseInt(map.get("totalStudent").toString());
    }

    public Course loadSubjects(String categoryid)
        throws DaoException, DataObjectNotFoundException {
        Collection results = super.select("SELECT distinct(categoryid) FROM cel_content_course",
                Course.class, "", 0, 1);

        if (results.size() == 0) {
            throw new DataObjectNotFoundException();
        } else {
            return (Course) results.iterator().next();
        }
    }

    public Course loadList(String categoryid)
        throws DaoException, DataObjectNotFoundException {
        Object[] args = { categoryid };
        Collection results = super.select("SELECT id,name,author,synopsis,instructor FROM cel_content_course WHERE categoryid=?",
                Course.class, args, 0, 1);

        if (results.size() == 0) {
            throw new DataObjectNotFoundException();
        } else {
            return (Course) results.iterator().next();
        }
    }

    public Collection query(String name, String category, String userId,
        String sort, boolean desc, int start, int rows)
        throws DaoException {
        String condition = (name != null) ? ("%" + name + "%") : "%";
        String orderBy = (sort != null) ? sort : "name";

        //  if (desc)
        //    orderBy += " NAME";
        if (desc) {
            orderBy += " DESC";
        }

        //  Object[] args = { condition,userId };
        if ((category != null) && !("".equals(category))) {
            Object[] args = { condition, category };

            //  return super.select("SELECT id,name,author,synopsis,instructor,category FROM cel_content_course WHERE name LIKE ? AND public=1 OR createdByUser=? ORDER BY " + orderBy, Course.class, args, start, rows);
            return super.select(
                "SELECT course.id,course.name,course.author,course.synopsis,course.instructor as instructor,category.category,course.public as is_public FROM cel_content_course as course, cel_content_course_category as category WHERE course.categoryid = category.id AND course.name LIKE ? and course.categoryid=? ORDER BY " +
                orderBy, Course.class, args, start, rows);
        } else {
            Object[] args = { condition };

            //  return super.select("SELECT id,name,author,synopsis,instructor,category FROM cel_content_course WHERE name LIKE ? AND public=1 OR createdByUser=? ORDER BY " + orderBy, Course.class, args, start, rows);
            return super.select(
                "SELECT course.id,course.name,course.author,course.synopsis,course.instructor as instructor,category.category, course.public as is_public FROM cel_content_course as course, cel_content_course_category as category WHERE course.categoryid = category.id AND course.name LIKE ? ORDER BY " +
                orderBy, Course.class, args, start, rows);
        }
    }

    public Collection querySubjects(String category, String sort, boolean desc,
        int start, int rows) throws DaoException {
        //    String orderBy = (sort != null) ? sort : "category";

        /*    return super.select("SELECT distinct(course.categoryid) as categoryid, category.category as category FROM cel_content_course_category as category, cel_content_course as course ORDER BY " + orderBy, Course.class, "", start, rows);
        */
        String condition = (category != null) ? ("%" + category + "%") : "%";
        String orderBy = (sort != null) ? sort : "category";

        if (desc) {
            orderBy += " DESC";
        }

        Object[] args = { condition };

        return super.select(
            "SELECT category.id as categoryid, category.category as category FROM\n" +
            "cel_content_course_category category LEFT JOIN cel_content_course course on category.id = course.categoryid WHERE category.category like ?  GROUP BY category.id ORDER BY " +
            orderBy, Course.class, args, start, rows);
    }

    public Collection queryRegisteredSubjects(boolean desc, int start, int rows)
        throws DaoException {
        return super.select("SELECT categoryid FROM cel_content_course WHERE registered=1 ORDER BY categoryid",
            Course.class, "", start, rows);
    }

    public Collection queryRegisteredCourses(boolean desc, int start, int rows)
        throws DaoException {
        return super.select("SELECT id,name,author,synopsis,instructor FROM cel_content_course WHERE registered= 1 ORDER BY name",
            Course.class, "", start, rows);
    }

    public Collection queryStudentReg(String course, String category,
        boolean desc, int start, int rows, String studentId)
        throws DaoException {
        String orderBy = "";

        if (desc) {
            orderBy += " DESC";
        }

        String condition = (course != null) ? ("%" + course + "%") : "%";

        //String orderBy = (sort != null) ? sort : "name";
        if ((category != null) && !("".equals(category))) {
            Object[] args = { studentId, condition, category };

            // return super.select("SELECT * from cel_content_studentreg WHERE student=?", Student.class, args, start, rows);
            return super.select(
                "select s.id, s.student, c.name, c.createdDate, category.category as categoryName from cel_content_course_category as category, cel_content_studentreg as s , cel_content_course as c where s.id=c.id and s.student=? AND c.public=\"1\" AND c.name like ? AND category.id = c.categoryid AND category.id LIKE ? ORDER BY s.student" +
                orderBy, Student.class, args, start, rows);
        } else {
            Object[] args = { studentId, condition };

            // return super.select("SELECT * from cel_content_studentreg WHERE student=?", Student.class, args, start, rows);
            return super.select(
                "select s.id, s.student, c.name, c.createdDate, category.category as categoryName from cel_content_course_category as category, cel_content_studentreg as s , cel_content_course as c where s.id=c.id and s.student=? AND c.public=\"1\" AND c.name like ? AND c.categoryid = category.id ORDER BY s.student" +
                orderBy, Student.class, args, start, rows);
        }

        //   return super.select("SELECT * FROM cel_content_studentreg", Student.class, "", start, rows);
        //    return super.select("SELECT id,name,author,synopsis,instructor FROM cel_content_course WHERE registered= 1 ORDER BY name", Course.class, "", start, rows);
    }

    //load categories for filter selectbox
    public Collection loadCategory() throws DaoException {
        return super.select("select id, category, createdByUser, createdByUserId, createdDate from cel_content_course_category order by category",
            Category.class, "", 0, -1);
    }

    public Collection queryList(String categoryid, String sort, boolean desc,
        int start, int rows) throws DaoException {
        String condition = (categoryid != null) ? ("" + categoryid + "") : "";
        String orderBy = (sort != null) ? sort : "cel_content_course.categoryid";

        //   if (desc)  orderBy += " categoryid";
        Application app = Application.getInstance();
        User user = app.getCurrentUser();

        if (desc) {
            orderBy += " DESC";
        }

        Object[] args = { user.getId(), condition };

        // return super.select("SELECT id,name,author,synopsis,instructor FROM cel_content_course WHERE public=\"1\" AND categoryid= ? ORDER BY " + orderBy, Course.class, args, start, rows);
        return super.select(
            "SELECT cel_content_course.id,cel_content_course.name,cel_content_course.author,cel_content_course.synopsis,cel_content_course.instructor FROM cel_content_course LEFT JOIN cel_content_studentreg on (cel_content_course.id =cel_content_studentreg.id AND cel_content_studentreg.student=?) WHERE cel_content_course.public=\"1\" AND cel_content_course.categoryid= ?  AND ( cel_content_studentreg.student IS NULL ) ORDER BY " +
            orderBy, Course.class, args, start, rows);
    }

    public Collection queryCourseLessons(String name, String course,
        String sort, boolean desc, int start, int rows)
        throws DaoException {
        String condition = (name != null) ? ("%" + name + "%") : "%";
        String orderBy = (sort != null) ? sort : "name";

        if (desc) {
            orderBy += " NAME";
        }

        Object[] args = { condition, course };

        return super.select("SELECT L.id, L.name,C.name as courseName, F.name as folderName, L.courseId,L.brief FROM cel_content_lesson L, cel_content_course C , cel_content_module F WHERE L.courseId=C.id and L.folderId=F.id and L.name LIKE ? AND L.courseId = ? ",
            Lesson.class, args, start, rows);
    }

    public Collection queryCourseStatistic(String studentId, String courseId)
        throws DaoException {
        //
        //            String condition = (name != null) ? "%" + name + "%" : "%";
        //            String orderBy = (sort != null) ? sort : "name";
        //            if (desc) orderBy += " NAME";
        Object[] args = { studentId, courseId };

        return super.select("SELECT cel_content_module.id as moduleId, cel_content_module.name as moduleName FROM cel_content_studentreg, cel_content_module WHERE cel_content_studentreg.student=? AND cel_content_studentreg.id=? AND cel_content_studentreg.id = cel_content_module.courseId",
            Course.class, args, 0, -1);
    }

    public Collection queryModulesStatistic(String moduleId)
        throws DaoException {
        //
        //            String condition = (name != null) ? "%" + name + "%" : "%";
        //            String orderBy = (sort != null) ? sort : "name";
        //            if (desc) orderBy += " NAME";
        Object[] args = { moduleId };

        return super.select("SELECT cel_content_assessment.name as name, cel_content_assessment_statistic.dateTook as dateTook, cel_content_assessment_statistic.total_questions as total_questions, cel_content_assessment_statistic.wrong_questions as wrong_questions FROM cel_content_assessment, cel_content_assessment_statistic WHERE cel_content_assessment.id = cel_content_assessment_statistic.assessment_id AND cel_content_assessment.module_id=?",
            StudentStatistic.class, args, 0, -1);
    }

    public int countCourseLessons(String name, String course)
        throws DaoException {
        String condition = (name != null) ? ("%" + name + "%") : "%";
        Object[] args = { condition, course };

        Collection list = super.select("SELECT COUNT(*) AS total FROM cel_content_lesson WHERE name LIKE ? and courseId=?",
                HashMap.class, args, 0, 1);
        HashMap map = (HashMap) list.iterator().next();

        return Integer.parseInt(map.get("total").toString());
    }

    public int countList(String categoryid) throws DaoException {
        String condition = (categoryid != null) ? ("%" + categoryid + "%") : "%";
        Application app = Application.getInstance();
        User user = app.getCurrentUser();
        Object[] args = { user.getId(), condition };

        //Collection list = super.select("SELECT count(*) as total FROM cel_content_course WHERE categoryid=? AND public=\"1\"", HashMap.class, args, 0, 1);
        Collection list = super.select("SELECT count(cel_content_course.id) as total FROM cel_content_course LEFT JOIN cel_content_studentreg on (cel_content_course.id =cel_content_studentreg.id AND cel_content_studentreg.student=? )  WHERE cel_content_course.categoryid=? AND cel_content_course.public=\"1\"  AND cel_content_studentreg.student IS NULL ",
                HashMap.class, args, 0, 1);

        HashMap map = (HashMap) list.iterator().next();

        return Integer.parseInt(map.get("total").toString());
    }

    public int countRegisteredCourses() throws DaoException {
        Collection list = super.select("SELECT count(distinct(name)) FROM cel_content_course WHERE registered=1 ",
                HashMap.class, "", 0, 1);
        HashMap map = (HashMap) list.iterator().next();

        return Integer.parseInt(map.get("total").toString());
    }

    public int countStudentReg(String course, String category, String studentId)
        throws DaoException {
        Collection list;
        String condition = (course != null) ? ("%" + course + "%") : "%";

        if ((category != null) && !("".equals(category))) {
            Object[] args = { studentId, condition, category };
            list = super.select("SELECT count(distinct(studentreg.id)) FROM cel_content_course_category as category, cel_content_studentreg as studentreg, cel_content_course as course WHERE studentreg.id= course.id AND course.public=\"1\" AND studentreg.student=? AND course.name like ? AND category.id = course.categoryid AND category.id LIKE ?",
                    HashMap.class, args, 0, 1);
        } else {
            Object[] args = { studentId, condition };
            list = super.select("SELECT count(distinct(studentreg.id)) FROM cel_content_course_category as category, cel_content_studentreg as studentreg, cel_content_course as course WHERE studentreg.id= course.id AND course.public=\"1\" AND studentreg.student=? AND course.name like ? AND category.id = course.categoryid",
                    HashMap.class, args, 0, 1);
        }

        HashMap map = (HashMap) list.iterator().next();

        return Integer.parseInt(map.get("total").toString());
    }

    public int countSubjects(String category) throws DaoException {
        String condition = (category != null) ? ("%" + category + "%") : "%";

        Object[] args = { condition };
        Collection list = super.select("SELECT COUNT(*) as total FROM cel_content_course_category category WHERE category.category like ? ",
                HashMap.class, args, 0, 1);
        HashMap map = (HashMap) list.iterator().next();

        return Integer.parseInt(map.get("total").toString());
    }

    public int count(String name, String category) throws DaoException {
        String condition = (name != null) ? ("%" + name + "%") : "%";

        Collection list;
 
       if ((category != null) && !("".equals(category))) {
            Object[] args = { condition, category };
            list = super.select("SELECT count(*) as total FROM cel_content_course as course, cel_content_course_category as category WHERE course.categoryid = category.id AND course.name LIKE ? and course.categoryid=?",
                    HashMap.class, args, 0, 1);
            /*list = super.select("SELECT COUNT(*) AS total FROM cel_content_course WHERE name LIKE ? and categoryid LIKE ?",
                    HashMap.class, args, 0, 1);*/


        } else {
            Object[] args = { condition };
            list = super.select("SELECT count(*) as total FROM cel_content_course as course, cel_content_course_category as category WHERE course.categoryid = category.id AND course.name LIKE ?",
                    HashMap.class, args, 0, 1);
           /* list = super.select("SELECT COUNT(*) AS total FROM cel_content_course WHERE name LIKE ?",
                    HashMap.class, args, 0, 1);*/
        }

        HashMap map = (HashMap) list.iterator().next();

        return Integer.parseInt(map.get("total").toString());
    }

    public Course courseExists(String name) throws DaoException {
        Object[] args = { name };
        Collection results = super.select("SELECT id FROM cel_content_course WHERE name = ?",
                Course.class, args, 0, 1);

        if (results.size() == 0) {
            return null;
        }

        return (Course) results.iterator().next();
    }

    public Course courseExists(String name, String id)
        throws DaoException {
        Object[] args = { name, id };
        Collection results = super.select("SELECT id FROM cel_content_course WHERE name = ? AND id <> ?",
                Course.class, args, 0, 1);

        if (results.size() == 0) {
            return null;
        }

        return (Course) results.iterator().next();
    }

    public Course checkCanAdd(String name,String categoryid) throws DaoException{
        Object[] args = { name, categoryid };
        Collection results = super.select("SELECT id FROM cel_content_course WHERE name = ? AND categoryid = ?",
                Course.class, args, 0, 1);

        if (results.size() == 0) {
            return null;
        }

        return (Course) results.iterator().next();



    }


     public Course checkCanAddSameId(String name,String categoryid, String id) throws DaoException{
        Object[] args = { name, categoryid,id };
        Collection results = super.select("SELECT id FROM cel_content_course WHERE name = ? AND categoryid = ? AND id !=?",
                Course.class, args, 0, 1);

        if (results.size() == 0) {
            return null;
        }

        return (Course) results.iterator().next();



    }


    public Collection studentsFromCourse(String courseId)
        throws DaoException {
        Object[] args = { courseId };
        Collection results = super.select("SELECT security_user.* FROM security_user,cel_content_studentreg where cel_content_studentreg.id=? AND cel_content_studentreg.student=security_user.id",
                User.class, args, 0, -1);

        if (results.size() == 0) {
            return null;
        }

        return results;
    }

    public String showSynopsisOfCourse(String id) throws DaoException {
        Object[] args = { id };

        Collection result = super.select("SELECT synopsis from cel_content_course where id=?",
                Course.class, args, 0, 1);

        if ((result.size() == 0) || (result == null)) {
            return null;
        }

        Course course = (Course) result.iterator().next();

        return course.getSynopsis();
    }

    public void setActivateCourse(String id) throws DaoException {
        Object args = new Object[] { id };

        super.update("UPDATE cel_content_course SET public='1' WHERE id=?", args);
    }

    public void setDeactivateCourse(String id) throws DaoException {
        Object args = new Object[] { id };

        super.update("UPDATE cel_content_course SET public='0' WHERE id=?", args);
    }

    public Collection studentRegistered(String course_id)
        throws DaoException {
        Object args = new Object[] { course_id };

        Collection result = super.select("SELECT su.firstName, su.lastName,su.username, su.active,su.id FROM cel_content_studentreg studentreg, security_user su WHERE su.id=studentreg.student AND studentreg.id=?",
                User.class, args, 0, -1);

        return result;
    }
}

