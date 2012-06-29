package com.tms.elearning.testware.model;

import com.tms.elearning.core.model.DefaultLearningContentDao;

import kacang.model.DaoException;

import kacang.util.Log;
import kacang.Application;

import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;


/**
 * Created by IntelliJ IDEA.
 * User: mahe
 * Date: Nov 23, 2004
 * Time: 3:04:31 PM
 * To change this template use Options | File Templates.
 */
public class AssessmentModuleDao extends DefaultLearningContentDao {
    protected String getTableName() {
        return "cel_content_assessment";
    }

    protected Class getContentObjectClass() {
        return Assessment.class;
    }

    public void init() throws DaoException {
        try {
            super.update("CREATE TABLE cel_content_assessment (" +
                "   id varchar(255) NOT NULL default ''," +
                "   course_id varchar(255) NOT NULL default ''," +
                "   module_id varchar(255) default NULL," +
                "   lesson_id varchar(255) default NULL," +
                "   name varchar(255) default NULL," +
                "   start_date datetime default '0000-00-00 00:00:00'," +
                "   end_date datetime default '0000-00-00 00:00:00'," +
                "   time_limit double NOT NULL default '0'," +
                "   level int(11) default '1'," +
                

            "   createdDate datetime default '0000-00-00 00:00:00'," +
                "   createdByUser varchar(255) default NULL," +
                "   createdByUserId varchar(255) default NULL," +
                "  public ENUM('0','1')  DEFAULT \"0\" NOT NULL,\n" +
                "   PRIMARY KEY  (id)" + ")", null);
        } catch (DaoException e) {
            Log.getLog(getClass()).debug("Error creating table cel_content_assessment");
        }


        try {
            super.update("CREATE TABLE cel_content_module (\n" +
                "  id varchar(50) NOT NULL default '',\n" +
                "  courseId varchar(50) NOT NULL default '',\n" +
                "  name varchar(50) default NULL,\n" +
                "  introduction varchar(255) default NULL,\n" +
                "  public ENUM('0','1')  DEFAULT \"0\" NOT NULL,\n" +
                "  createdDate datetime NOT NULL default '0000-00-00 00:00:00',\n" +
                "  createdByUser varchar(25) default NULL,\n" +
                "  createdByUserId varchar(25) default NULL,\n" +
                "  PRIMARY KEY  (id)\n" + ")", null);
        } catch (DaoException e) {
            Log.getLog(getClass()).debug("Error creating table cel_content_module");
        }

        try {
            super.update("CREATE TABLE cel_content_course (\n" +
                "  id varchar(50) NOT NULL default '',\n" +
                "  name varchar(50) default NULL,\n" +
                "  instructor varchar(50) default NULL,\n" +
                "  author varchar(50) default NULL,\n" + "  synopsis text,\n" +
                "  categoryid varchar(50) NOT NULL default ''," +
                "  registered int(1) unsigned default '0',\n" +
                "  public ENUM('0','1')  DEFAULT \"0\" NOT NULL,\n" +
                "  createdDate datetime NOT NULL default '0000-00-00 00:00:00',\n" +
                "  createdByUser varchar(50) default NULL,\n" +
                "  createdByUserId varchar(50) default NULL,\n" +
                "  PRIMARY KEY  (id)\n" + ")", null);
        } catch (DaoException e) {
            Log.getLog(getClass()).debug("Error creating table cel_content_course");
        }

        try {
            super.update(
                "CREATE TABLE cel_content_assessment_students_history (\n" +
                "  user_id varchar(255) NOT NULL default '0',\n" +
                "  question varchar(255) NOT NULL default '0',\n" +
                "  wrong_answer varchar(255) NOT NULL default '0',\n" +
                "  right_answer varchar(255) NOT NULL default '0',\n" +
                "  assessment_id varchar(255) NOT NULL default '0',\n" +
                "  last_date datetime NOT NULL default '0000-00-00 00:00:00'," +
                "  frequency int(11) NOT NULL default '0'" + ")", null);
        } catch (DaoException e) {
            Log.getLog(getClass()).debug("Error creating table cel_content_assessment_students_history");
        }

        try {
            super.update("CREATE TABLE cel_content_studentreg (\n" +
                "  id varchar(255) NOT NULL default '0',\n" +
                "  student varchar(255) default NULL,\n" +
                "  PRIMARY KEY  (id)\n" + ")", null);
        } catch (DaoException e) {
            Log.getLog(getClass()).debug("Error creating table cel_content_studentreg");
        }

        try {
            super.update("CREATE TABLE cel_content_question (\n" +
                "  id varchar(255) NOT NULL default '',\n" +
                "  course_id varchar(255) NOT NULL default '',\n" +
                "  topic_id varchar(255) default NULL,\n" +
                "  module_id varchar(255) default NULL,\n" +
                "  lesson_id varchar(255) default NULL,\n" +
                "  question text NOT NULL,\n" + "  answer_a text,\n" +
                "  answer_b text,\n" + "  answer_c text,\n" +
                "  answer_d text,\n" + "  answer_e text,\n" +
                "  score_a int(11) unsigned default '0',\n" +
                "  score_b int(11) unsigned default '0',\n" +
                "  score_c int(11) unsigned default '0',\n" +
                "  score_d int(11) unsigned default '0',\n" +
                "  score_e int(11) unsigned default '0',\n" +
                "  correct_answer char(3) default '0',\n" +
                "  subjective_answer text,\n" +
                "  is_subjective char(3) default '0',\n" +
                "  createdDate datetime default NULL,\n" +
                "  createdByUser varchar(20) default NULL,\n" +
                "  createdByUserId varchar(20) default NULL,\n" +
                "  PRIMARY KEY  (id)\n" + ")", null);
        } catch (DaoException e) {
            Log.getLog(getClass()).debug("Error creating table cel_content_question");
        }
    }

    public Collection getAssessments(String keyword, String course, String folder,
        String sort, boolean desc, int start, int rows)
        throws DaoException {
        String condition = (keyword != null) ? ("%" + keyword + "%") : "%";
        String orderBy = (sort != null) ? sort
                                        : "cel_content_assessment.start_date";

        if (desc) {
            orderBy += " DESC";
        }


        if( course !=null && !("".equals(course)) && folder !=null && !("".equals(folder))) {
                Object [] arg ={course, folder };
            return super.select(
                       "select cel_content_assessment.id, cel_content_assessment.name, cel_content_module.name as folderName, cel_content_course.name as courseName, cel_content_assessment.public as is_public FROM cel_content_assessment, cel_content_module, cel_content_course WHERE cel_content_module.id = cel_content_assessment.module_id AND cel_content_course.id = cel_content_module.courseId AND cel_content_assessment.course_id LIKE ? AND cel_content_assessment.module_id LIKE ? ORDER BY " +
                       orderBy, Assessment.class, arg, start, rows);

        }

        else if( course != null && !("".equals(course))){
             Object [] arg ={course };
            return super.select(
                       "select cel_content_assessment.id, cel_content_assessment.name, cel_content_module.name as folderName, cel_content_course.name as courseName, cel_content_assessment.public as is_public FROM cel_content_assessment, cel_content_module, cel_content_course WHERE cel_content_module.id = cel_content_assessment.module_id AND cel_content_course.id = cel_content_module.courseId AND cel_content_assessment.course_id LIKE ? ORDER BY " +
                       orderBy, Assessment.class, arg, start, rows);


        }

        else if ( folder !=null && !("".equals(folder))){

             Object [] arg ={ folder };
            return super.select(
                       "select cel_content_assessment.id, cel_content_assessment.name, cel_content_module.name as folderName, cel_content_course.name as courseName, cel_content_assessment.public as is_public FROM cel_content_assessment, cel_content_module, cel_content_course WHERE cel_content_module.id = cel_content_assessment.module_id AND cel_content_course.id = cel_content_module.courseId AND cel_content_assessment.module_id LIKE ? ORDER BY " +
                       orderBy, Assessment.class, arg, start, rows);


        }


        else {
        return super.select(
            "select cel_content_assessment.id, cel_content_assessment.name, cel_content_module.name as folderName, cel_content_course.name as courseName, cel_content_assessment.public as is_public FROM cel_content_assessment, cel_content_module, cel_content_course WHERE cel_content_module.id = cel_content_assessment.module_id AND cel_content_course.id = cel_content_module.courseId ORDER BY " +
            orderBy, Assessment.class, "", start, rows);

        }




    }

    public Collection getAssessmentsByModule(String moduleId, String sort,
        boolean desc, int start, int rows) throws DaoException {
        String todayDate = "";
        Calendar calendar = Calendar.getInstance();
        Date todayD = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        todayDate = sdf.format(todayD);

        Object[] args = { moduleId, todayDate, todayDate, Application.getInstance().getCurrentUser().getId() };



        return super.select("SELECT a.id, a.name, c.name as course, a.id as examid, a.start_date as startDate, a.end_date as endDate,CASE WHEN h.frequency IS NULL THEN 0 ELSE h.frequency END AS numbTakenStr,  CASE WHEN h.last_date IS NULL THEN '-' ELSE h.last_date END as lastTakenDateStr, a.level \n" +
                "FROM cel_content_assessment a INNER JOIN cel_content_course c ON a.course_id=c.id and a.module_id=?\n" +
                "LEFT JOIN cel_content_assessment_students_history h ON a.id=h.assessment_id\n" +
                "WHERE \n" +
                "a.public='1' \n" +
                "" +
                "AND (a.start_date <= date_format( ?,'%Y-%m-%d') \n" +
                "AND a.end_date >= date_format( ?,'%Y-%m-%d') )" +
                "AND (h.user_id IS NULL OR h.user_id=?) GROUP BY id", Assessment.class, args, start, rows);

    }

    public Collection getAssessmentsByStudent(String student_id, String sort,
        boolean desc, int start, int rows) throws DaoException {
        Object[] args = { student_id };


        return super.select("SELECT cel_content_assessment_students_history.assessment_id, cel_content_assessment.name, cel_content_course.name as course, cel_content_assessment.id as examid  FROM cel_content_assessment_students_history, cel_content_assessment,cel_content_course,cel_content_studentreg WHERE  cel_content_assessment.course_id = cel_content_course.id AND cel_content_assessment.course_id = cel_content_studentreg.id AND cel_content_assessment_students_history.assessment_id =cel_content_assessment.id AND cel_content_assessment_students_history.user_id=? group by cel_content_assessment_students_history.assessment_id",
            Assessment.class, args, start, rows);
    }

    //----Tiru
    public Collection getCourseAssessments(String keyword, String course,
        String sort, boolean desc, int start, int rows)
        throws DaoException {
        String condition = (keyword != null) ? ("%" + keyword + "%") : "%";
        String orderBy = (sort != null) ? sort : "start_date";

        if (desc) {
            orderBy += " DESC";
        }

        if (course.equalsIgnoreCase("-1")) {
            Object[] args = { condition };

            return super.select(
                "SELECT cel_content_assessment.id, cel_content_assessment.name, cel_content_course.name as course, cel_content_assessment.id as examid  FROM cel_content_assessment,cel_content_course WHERE cel_content_assessment.name LIKE ? AND cel_content_assessment.course_id = cel_content_course.id  ORDER BY " +
                orderBy, Assessment.class, args, start, rows);
        } else {
            Object[] args = { condition, course };

            return super.select(
                "SELECT cel_content_assessment.id, cel_content_assessment.name, cel_content_course.name as course, cel_content_assessment.id as examid  FROM cel_content_assessment,cel_content_course WHERE cel_content_assessment.name LIKE ? AND cel_content_assessment.course_id = ? AND cel_content_assessment.course_id = cel_content_course.id ORDER BY " +
                orderBy, Assessment.class, args, start, rows);
        }
    }

    public int countCourseAssessments(String keyword, String course)
        throws DaoException {
        Collection list = null;
        String condition = (keyword != null) ? ("%" + keyword + "%") : "%";

        if (course.equalsIgnoreCase("-1")) {
            Object[] args = { condition };
            list = super.select("SELECT COUNT(cel_content_assessment.id) AS total FROM cel_content_assessment,cel_content_course WHERE cel_content_assessment.name LIKE ? AND cel_content_assessment.course_id = cel_content_course.id ",
                    HashMap.class, args, 0, 1);
        } else {
            Object[] args = { condition, course };
            list = super.select("SELECT COUNT(cel_content_assessment.id) AS total FROM cel_content_assessment,cel_content_course WHERE cel_content_assessment.name LIKE ? AND cel_content_assessment.course_id = ? AND cel_content_assessment.course_id = cel_content_course.id ",
                    HashMap.class, args, 0, 1);
        }

        HashMap map = (HashMap) list.iterator().next();

        return Integer.parseInt(map.get("total").toString());
    }

    //Tiru-----
    public int countAssessments(String keyword, String course, String folder)
        throws DaoException {
        Collection list = null;
        String condition = (keyword != null) ? ("%" + keyword + "%") : "%";


        if(course !=null && !("".equals(course)) && folder !=null && !("".equals(folder))){

            list = super.select("select COUNT(*) AS total FROM cel_content_assessment WHERE course_id LIKE ?",
                            HashMap.class, "", 0, 1);


        }

        else if(course !=null && !("".equals(course))) {

            list = super.select("select COUNT(*) AS total FROM cel_content_assessment WHERE module_id LIKE ?",
                            HashMap.class, "", 0, 1);


        }



        else if (folder !=null && !("".equals(folder))){
			Object [] arg ={ folder };
            list = super.select("select COUNT(*) AS total FROM cel_content_assessment, cel_content_module, cel_content_course WHERE cel_content_module.id = cel_content_assessment.module_id AND cel_content_course.id = cel_content_module.courseId AND cel_content_assessment.module_id LIKE ?",
                            HashMap.class, arg, 0, 1);
        }


        else{
        list = super.select("select COUNT(*) AS total FROM cel_content_assessment",
                HashMap.class, "", 0, 1);

        }


        HashMap map = (HashMap) list.iterator().next();

        return Integer.parseInt(map.get("total").toString());
    }

    public int countAssessmentsByModule(String moduleId)
        throws DaoException {
        Collection list = null;

        String todayDate = "";
        Calendar calendar = Calendar.getInstance();
        Date todayD = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        todayDate = sdf.format(todayD);

        Object[] args = { moduleId, todayDate, todayDate, Application.getInstance().getCurrentUser().getId()};
        list = super.select("\n" +
                "SELECT COUNT(cel_content_assessment.id) as total FROM cel_content_assessment,cel_content_assessment_students_history WHERE cel_content_assessment.public=\"1\" AND module_id=? \n" +
                "AND (start_date <= date_format( ?,'%Y-%m-%d') AND end_date >= date_format( ?,'%Y-%m-%d')  ) AND (cel_content_assessment_students_history.user_id IS NULL OR cel_content_assessment_students_history.user_id=?) GROUP BY id",
                HashMap.class, args, 0, 1);

        HashMap map = (HashMap) list.iterator().next();

        return Integer.parseInt(map.get("total").toString());
    }

    public int countAssessmentsByStudent(String student_id)
        throws DaoException {
        Collection list = null;
        Object[] args = { student_id };
        list = super.select("\n" +
                "SELECT COUNT(cel_content_assessment_students_history.assessment_id)   FROM cel_content_assessment,cel_content_course,cel_content_studentreg, cel_content_assessment_students_history WHERE  cel_content_assessment.course_id = cel_content_course.id AND cel_content_assessment.course_id = cel_content_studentreg.id AND cel_content_assessment_students_history.assessment_id =cel_content_assessment.id AND cel_content_assessment_students_history.user_id=?",
                HashMap.class, args, 0, 1);

        HashMap map = (HashMap) list.iterator().next();

        return Integer.parseInt(map.get("total").toString());
    }

    public void insertAssessment(Assessment assmt) throws DaoException {
        super.update("INSERT INTO cel_content_assessment (id,course_id,module_id,lesson_id,name,start_date,end_date,time_limit,level,public,createdDate,createdByUser,createdByUserId) VALUES (#id#,#course_id#,#module_id#,#lesson_id#,#name#,#start_date#,#end_date#,#time_limit#,#level#,#is_public#,#modifiedDate#,#modifiedDateByUser#,#modifiedDateByUserId#)",
            assmt);


    }

    public void deleteAssessment(String id) throws DaoException {
        Object[] args = new String[] { id };
        super.update("DELETE FROM cel_content_assessment WHERE id=?", args);
    }

    public Assessment getAssessment(String id) throws DaoException {
        Object[] args = { id };
        Collection results = super.select("SELECT id, course_id, module_id, lesson_id, name, start_date, end_date, time_limit, level, public as is_public  FROM cel_content_assessment WHERE id = ?",
                Assessment.class, args, 0, 1);


        if (results.size() == 0) {
            return null;
        }

        return (Assessment) results.iterator().next();
    }

    public void updateQuestion(Question q) throws DaoException {
        super.update("UPDATE cel_content_question SET course_id=#course_id#, module_id=#module_id#, lesson_id=#lesson_id#, question=#question#, answer_a=#answer_a#, answer_b=#answer_b#, answer_c=#answer_c#, answer_d=#answer_d#, correct_answer=#correct_answer# WHERE id=#id#",
            q);
    }

    public void updateAssessment(Assessment asmt) throws DaoException {
        super.update("UPDATE cel_content_assessment SET course_id = #course_id#, module_id = #module_id#, lesson_id = #lesson_id#, name = #name#, start_date = #start_date#, end_date = #end_date#, time_limit = #time_limit#, level = #level#, public = #is_public#, createdDate = #modifiedDate#, createdByUser = #modifiedDateByUser#, createdByUserId = #modifiedDateByUserId# WHERE id=#id#",
            asmt);

     
    }

    public void setActivationAssessment(String id) throws DaoException{

        Object args = new Object[]{id};
        super.update("UPDATE cel_content_assessment SET cel_content_assessment.public='1' WHERE id=?", args);
    }

    public void setDeactivationAssessment(String id) throws DaoException{

        Object args = new Object[]{id};
        super.update("UPDATE cel_content_assessment SET cel_content_assessment.public='0' WHERE id=?", args);

    }

    public Assessment checkActive(String id, String startDate, String endDate) throws DaoException {

      Collection result = super.select("SELECT public as is_public,id from cel_content_assessment WHERE id=? AND  (start_date <= date_format( ?,'%Y-%m-%d') AND end_date >= date_format( ?,'%Y-%m-%d') ) ", Assessment.class,new Object[]{id,startDate,endDate},0,1);

        if(result.size() >0)
        return (Assessment) result.iterator().next();

        return null;
    }

}
