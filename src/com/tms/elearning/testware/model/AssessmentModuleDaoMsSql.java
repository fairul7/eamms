package com.tms.elearning.testware.model;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

import kacang.Application;
import kacang.model.DaoException;
import kacang.util.Log;

public class AssessmentModuleDaoMsSql extends AssessmentModuleDao{
	
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
                "   time_limit numeric(5,2) NOT NULL default '0'," +
                "   level integer default '1'," +
                "   createdDate datetime default '0000-00-00 00:00:00'," +
                "   createdByUser varchar(255) default NULL," +
                "   createdByUserId varchar(255) default NULL," +
                "   \"public\" varchar(50)  DEFAULT '0' NOT NULL,\n" +
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
                "  \"public\" varchar(50)  DEFAULT \"0\" NOT NULL,\n" +
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
                "  registered integer unsigned default '0',\n" +
                "  \"public\" varchar(50)  DEFAULT \"0\" NOT NULL,\n" +
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
                "  frequency integer NOT NULL default '0'" + ")", null);
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
                "  score_a integer unsigned default '0',\n" +
                "  score_b integer unsigned default '0',\n" +
                "  score_c integer unsigned default '0',\n" +
                "  score_d integer unsigned default '0',\n" +
                "  score_e integer unsigned default '0',\n" +
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
	
	public Collection getAssessmentsByStudent(String student_id, String sort,
	        boolean desc, int start, int rows) throws DaoException {
	        Object[] args = { student_id };

	        return super.select("SELECT cel_content_assessment_students_history.assessment_id, cel_content_assessment.name, cel_content_course.name as course, cel_content_assessment.id as examid  FROM cel_content_assessment_students_history, cel_content_assessment,cel_content_course,cel_content_studentreg WHERE  cel_content_assessment.course_id = cel_content_course.id AND cel_content_assessment.course_id = cel_content_studentreg.id AND cel_content_assessment_students_history.assessment_id =cel_content_assessment.id AND cel_content_assessment_students_history.user_id=? ",
	            Assessment.class, args, start, rows);
	    }
	
	public void updateAssessment(Assessment asmt) throws DaoException {
        super.update("UPDATE cel_content_assessment SET course_id = #course_id#, module_id = #module_id#, lesson_id = #lesson_id#, name = #name#, start_date = #start_date#, end_date = #end_date#, time_limit = #time_limit#, \"level\" = #level#, \"public\" = #is_public#, createdDate = #modifiedDate#, createdByUser = #modifiedDateByUser#, createdByUserId = #modifiedDateByUserId# WHERE id=#id#",
            asmt);
    }
	
	public Collection getAssessmentsByModule(String moduleId, String sort,
	        boolean desc, int start, int rows) throws DaoException {
	        String todayDate = "";
	        Calendar calendar = Calendar.getInstance();
	        Date todayD = calendar.getTime();
	        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	        todayDate = sdf.format(todayD);

	        Object[] args = { moduleId, todayDate, todayDate, Application.getInstance().getCurrentUser().getId() };

	        return super.select("SELECT a.id, a.name, c.name as course, a.id as examid, a.start_date as startDate, " +
	        		"a.end_date as endDate, " +
	        		"CASE WHEN h.frequency IS NULL THEN 0 ELSE h.frequency END AS numbTakenStr, " +
	        		"h.last_date as lastTakenDateStr, a.\"level\" " +
	                "FROM cel_content_assessment a " +
	                "INNER JOIN cel_content_course c ON a.course_id=c.id and a.module_id=? " +
	                "LEFT JOIN cel_content_assessment_students_history h ON a.id=h.assessment_id " +
	                "WHERE " +
	                "a.\"public\"='1' " +
	                "AND a.start_date <= ? " +
	                "AND a.end_date >= ?  " +
	                "AND (h.user_id IS NULL OR h.user_id=?) ", Assessment.class, args, start, rows);

	    }
	
	public int countAssessmentsByModule(String moduleId)
	    throws DaoException {
	    Collection list = null;
	
	    Calendar calendar = Calendar.getInstance();
	    Date todayD = calendar.getTime();
	    String todayDate = "";
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	    todayDate = sdf.format(todayD);
	
	    Object[] args = { moduleId, todayDate, todayDate, Application.getInstance().getCurrentUser().getId()};
	    list = super.select("" +
	            "SELECT COUNT(cel_content_assessment.id) as total FROM cel_content_assessment,cel_content_assessment_students_history WHERE cel_content_assessment.\"public\"='1' AND module_id=? " +
	            "AND (start_date <= ? AND end_date >= ?  ) AND (cel_content_assessment_students_history.user_id IS NULL OR cel_content_assessment_students_history.user_id=?) ",
	            HashMap.class, args, 0, 1);
	
	    HashMap map = (HashMap) list.iterator().next();
	
	    return Integer.parseInt(map.get("total").toString());
	}
	
	public Collection getAssessments(String keyword, String course, String folder,
	        String sort, boolean desc, int start, int rows)
	        throws DaoException {
	        
	        String orderBy = (sort != null) ? (sort.equals("name")?"cel_content_assessment.name":sort) : "cel_content_assessment.start_date";

	        if (desc) {
	            orderBy += " DESC";
	        }

	        if( course !=null && !("".equals(course)) && folder !=null && !("".equals(folder))) {
	                Object [] arg ={course, folder };
	            return super.select(
	                       "select cel_content_assessment.id, cel_content_assessment.name, cel_content_module.name as folderName, cel_content_course.name as courseName, cel_content_assessment.\"public\" as is_public FROM cel_content_assessment, cel_content_module, cel_content_course WHERE cel_content_module.id = cel_content_assessment.module_id AND cel_content_course.id = cel_content_module.courseId AND cel_content_assessment.course_id LIKE ? AND cel_content_assessment.module_id LIKE ? ORDER BY " +
	                       orderBy, Assessment.class, arg, start, rows);

	        }
	        else if( course != null && !("".equals(course))){
	             Object [] arg ={course };
	            return super.select(
	                       "select cel_content_assessment.id, cel_content_assessment.name, cel_content_module.name as folderName, cel_content_course.name as courseName, cel_content_assessment.\"public\" as is_public FROM cel_content_assessment, cel_content_module, cel_content_course WHERE cel_content_module.id = cel_content_assessment.module_id AND cel_content_course.id = cel_content_module.courseId AND cel_content_assessment.course_id LIKE ? ORDER BY " +
	                       orderBy, Assessment.class, arg, start, rows);


	        }
	        else if ( folder !=null && !("".equals(folder))){

	             Object [] arg ={ folder };
	            return super.select(
	                       "select cel_content_assessment.id, cel_content_assessment.name, cel_content_module.name as folderName, cel_content_course.name as courseName, cel_content_assessment.\"public\" as is_public FROM cel_content_assessment, cel_content_module, cel_content_course WHERE cel_content_module.id = cel_content_assessment.module_id AND cel_content_course.id = cel_content_module.courseId AND cel_content_assessment.module_id LIKE ? ORDER BY " +
	                       orderBy, Assessment.class, arg, start, rows);


	        }
	        else {
	        return super.select(
	            "select cel_content_assessment.id, cel_content_assessment.name, cel_content_module.name as folderName, cel_content_course.name as courseName, cel_content_assessment.\"public\" as is_public FROM cel_content_assessment, cel_content_module, cel_content_course WHERE cel_content_module.id = cel_content_assessment.module_id AND cel_content_course.id = cel_content_module.courseId ORDER BY " +
	            orderBy, Assessment.class, "", start, rows);

	        }
	    }
	
	
	
	public void insertAssessment(Assessment assmt) throws DaoException {
        super.update("INSERT INTO cel_content_assessment (id,course_id,module_id,lesson_id,name,start_date,end_date,time_limit,\"level\",\"public\",createdDate,createdByUser,createdByUserId) VALUES (#id#,#course_id#,#module_id#,#lesson_id#,#name#,#start_date#,#end_date#,#time_limit#,#level#,#is_public#,#modifiedDate#,#modifiedDateByUser#,#modifiedDateByUserId#)",
            assmt);
    }
	
	public Assessment getAssessment(String id) throws DaoException {
        Object[] args = { id };
        Collection results = super.select("SELECT id, course_id, module_id, lesson_id, name, start_date, end_date, time_limit, \"level\", \"public\" as is_public  FROM cel_content_assessment WHERE id = ?",
                Assessment.class, args, 0, 1);


        if (results.size() == 0) {
            return null;
        }

        return (Assessment) results.iterator().next();
    }
	
	public void setActivationAssessment(String id) throws DaoException{
        Object args = new Object[]{id};
        super.update("UPDATE cel_content_assessment SET cel_content_assessment.\"public\"='1' WHERE id=?", args);
    }

    public void setDeactivationAssessment(String id) throws DaoException{
        Object args = new Object[]{id};
        super.update("UPDATE cel_content_assessment SET cel_content_assessment.\"public\"='0' WHERE id=?", args);
    }
    
    public Assessment checkActive(String id, String startDate, String endDate) throws DaoException {
    	
    	int spaceIndex = startDate.indexOf(" ");
    	startDate = startDate.substring(0, spaceIndex);
    	
    	spaceIndex = endDate.indexOf(" ");
    	endDate = endDate.substring(0, spaceIndex);
    	
        Collection result = super.select("SELECT \"public\" as is_public,id from cel_content_assessment WHERE id=? AND  (start_date <= ? AND end_date >= ? ) ", Assessment.class,new Object[]{id,startDate,endDate},0,1);

          if(result.size() >0)
          return (Assessment) result.iterator().next();

          return null;
      }

}
