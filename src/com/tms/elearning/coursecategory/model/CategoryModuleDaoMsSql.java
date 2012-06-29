package com.tms.elearning.coursecategory.model;

import kacang.model.DaoException;

public class CategoryModuleDaoMsSql extends CategoryModuleDao{
	
	public void init() throws DaoException {
		
		try{
			super.update("ALTER TABLE cel_content_course_category ALTER COLUMN createdByUser varchar(70)", null);
		}catch(DaoException e){
			
		}
		
		try{
			super.update("ALTER TABLE cel_content_course_category ALTER COLUMN createdByUserId varchar(70)", null);
		}catch(DaoException e){
			
		}
		
		try{
			super.update("CREATE TABLE cel_content_course_category (" +
	                "  id varchar(50) NOT NULL default ''," +
	                "  category varchar(50) unique default NULL,"+
	                "  createdByUser varchar(50),"+
	                "  createdByUserId varchar(50),"+
	                "  createdDate datetime,"+
	                "  PRIMARY KEY  (id)" +
	                ")", null);
		}catch(DaoException e){
			
		}
		
        
    }
	
	public void delete(String id) throws DaoException {
        Object[] args = new String[] {
            id
        };
        
        super.update("DELETE FROM cel_content_questions WHERE assessment_id IN ( " +
        		"SELECT assessment.id FROM " +
        		"cel_content_assessment as assessment, " +
        		"cel_content_module as module, " +
        		"cel_content_course as course " +
        		"WHERE  assessment.module_id = module.id " +
        		"AND module.courseId = course.id " +
        		"AND course.categoryid=? ) ", args);
        
        super.update("DELETE FROM cel_content_assessment WHERE module_id IN ( " +
        		"SELECT module.id FROM cel_content_module as module, cel_content_course as course " +
        		"WHERE module.courseId = course.id AND course.categoryid=?) ", args);
        
        super.update("DELETE FROM cel_content_lesson WHERE folderId IN ( " +
        		"SELECT module.id FROM cel_content_module as module, cel_content_course as course " +
        		"WHERE module.courseId = course.id AND course.categoryid=?) ",args);
        
        super.update("DELETE FROM cel_content_module WHERE courseId IN( " +
        		"SELECT course.id FROM cel_content_course as course WHERE course.categoryid=?) ", args);
        
        super.update("DELETE FROM cel_content_course WHERE categoryid=?", args);
        super.update("DELETE FROM cel_content_course_category WHERE id=?", args);

    }

}
