package com.tms.elearning.testware.model;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

import kacang.Application;
import kacang.model.DaoException;

public class AssessmentModuleDaoDB2 extends AssessmentModuleDao{
	
	public Collection getAssessmentsByStudent(String student_id, String sort,
	        boolean desc, int start, int rows) throws DaoException {
	        Object[] args = { student_id };

	        return super.select("SELECT cel_content_assessment_students_history.assessment_id, cel_content_assessment.name, cel_content_course.name as course, cel_content_assessment.id as examid  FROM cel_content_assessment_students_history, cel_content_assessment,cel_content_course,cel_content_studentreg WHERE  cel_content_assessment.course_id = cel_content_course.id AND cel_content_assessment.course_id = cel_content_studentreg.id AND cel_content_assessment_students_history.assessment_id =cel_content_assessment.id AND cel_content_assessment_students_history.user_id=? ",
	            Assessment.class, args, start, rows);
	    }
	
	public Assessment checkActive(String id, String startDate, String endDate) throws DaoException {
		
		int startIn = startDate.indexOf(" ");
		int endInd  = endDate.indexOf(" ");
		
		startDate = startDate.substring(0, startIn);
		endDate = endDate.substring(0, endInd);
		
	    Collection result = super.select("SELECT public as is_public,id from cel_content_assessment WHERE id=? AND  ( DATE(start_date) <= ? AND DATE(end_date) >= ? ) ", Assessment.class,new Object[]{id,startDate,endDate},0,1);

	    if(result.size() >0)
	        return (Assessment) result.iterator().next();

	    return null;
	}
	
	public void updateAssessment(Assessment asmt) throws DaoException {
        super.update("UPDATE cel_content_assessment SET course_id = #course_id#, module_id = #module_id#, lesson_id = #lesson_id#, name = #name#, start_date = #start_date#, end_date = #end_date#, time_limit = #time_limit#, level = #level#, public = #is_public#, createdDate = #modifiedDate#, createdByUser = #modifiedDateByUser#, createdByUserId = #modifiedDateByUserId# WHERE id=#id#",
            asmt);     
    }
	
	public Collection getAssessmentsByModule(String moduleId, String sort,
	        boolean desc, int start, int rows) throws DaoException {
	        
	        Calendar calendar = Calendar.getInstance();
	        Date todayD = calendar.getTime();
	        String todayDate = "";
	        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	        todayDate = sdf.format(todayD);
	        
	        Object[] args = { moduleId, todayDate, todayDate, Application.getInstance().getCurrentUser().getId() };

	        return super.select("SELECT a.id, a.name, c.name as course, a.id as examid, a.start_date as startDate, " +
	        		"a.end_date as endDate, " +
	        		"CASE WHEN h.frequency IS NULL THEN 0 ELSE h.frequency END AS numbTakenStr, " +
	        		"h.last_date as lastTakenDateStr, a.level " +
	                "FROM cel_content_assessment a " +
	                "INNER JOIN cel_content_course c ON a.course_id=c.id and a.module_id=? " +
	                "LEFT JOIN cel_content_assessment_students_history h ON a.id=h.assessment_id " +
	                "WHERE " +
	                "a.public='1' " +
	                "AND (DATE(a.start_date) <= ? " +
	                "AND DATE(a.end_date) >= ? ) " +
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
	            "SELECT COUNT(cel_content_assessment.id) as total FROM cel_content_assessment,cel_content_assessment_students_history WHERE cel_content_assessment.public='1' AND module_id=? " +
	            "AND (DATE(start_date) <= ? AND DATE(end_date) >= ?  ) AND (cel_content_assessment_students_history.user_id IS NULL OR cel_content_assessment_students_history.user_id=?) ",
	            HashMap.class, args, 0, 1);
	
	    HashMap map = (HashMap) list.iterator().next();
	
	    return Integer.parseInt(map.get("total").toString());
	}

}
