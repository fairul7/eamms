package com.tms.elearning.folder.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import kacang.model.DaoException;
import kacang.model.DataObjectNotFoundException;
import kacang.util.Log;

public class FolderModuleDaoMsSql extends FolderModuleDao{
	
	public void init() throws DaoException {
		try {
            super.update("ALTER TABLE cel_content_module ALTER COLUMN createdByUser varchar(70)", null);
        } catch (DaoException e) {
            Log.getLog(getClass()).debug("Error editing table cms_content_module");
        }
        try {
            super.update("ALTER TABLE cel_content_module ALTER COLUMN createdByUserId varchar(70)", null);
        } catch (DaoException e) {
            Log.getLog(getClass()).debug("Error editing table cms_content_module");
        }
		
		try {
            super.update("CREATE TABLE cel_content_module (" +
                "  id varchar(50) NOT NULL default ''," +
                "  courseId varchar(50) NOT NULL default ''," +
                "  name varchar(50) default NULL," +
                "  introduction TEXT default NULL," +
                "  \"public\" varchar(50)  DEFAULT '0' NOT NULL," +
                "  createdDate DATETIME default '0000-00-00 00:00:00'," +
                "  createdByUser varchar(50) default NULL," +
                "  createdByUserId varchar(50) default NULL," +
                "  PRIMARY KEY  (id)" + ")", null);
        } catch (DaoException e) {
            Log.getLog(getClass()).debug("Error creating table cms_content_module");
        }

        try {
            super.update("CREATE TABLE cel_content_course (" +
                "  id varchar(50) NOT NULL default ''," +
                "  name varchar(50) default NULL," +
                "  instructor varchar(50) default NULL," +
                "  author varchar(50) default NULL," + "  synopsis text," +
                "  categoryid varchar(50) NOT NULL default ''," +
                "  registered integer default '0'," +
                "  \"public\" varchar(50)  DEFAULT '0' NOT NULL," +
                "  createdDate datetime NOT NULL default '0000-00-00 00:00:00'," +
                "  createdByUser varchar(50) default NULL," +
                "  createdByUserId varchar(50) default NULL," +
                "  PRIMARY KEY  (id)" + ")", null);
        } catch (DaoException e) {
            Log.getLog(getClass()).debug("Error creating table cms_content_course");
        }
    }
	
	public void insert(Folder folder) throws DaoException {
        super.update("INSERT INTO cel_content_module (id,courseId,name,introduction,\"public\",createdDate,createdByUser,createdByUserId) VALUES (#id#,#courseId#,#name#,#introduction#,#is_public#,#createdDate#,#createdByUser#,#createdByUserId#)",
            folder);
    }
	
	public void update(Folder folder) throws DaoException {
        super.update("UPDATE cel_content_module SET courseId=#courseId#,name=#name#,introduction=#introduction#,\"public\"=#is_public# WHERE id=#id#",
            folder);
    }
	
	public Folder load(String id)
	    throws DaoException, DataObjectNotFoundException {
	    Object[] args = { id };
	    Collection results = super.select("SELECT id,name,courseId,introduction, \"public\" as is_public FROM cel_content_module WHERE id=?",
	            Folder.class, args, 0, -1);
	
	    if (results.size() == 0) {
	        throw new DataObjectNotFoundException();
	    } else {
	        return (Folder) results.iterator().next();
	    }
	}
	
	public Collection query(String name, String course, String userId,
	        String sort, boolean desc, int start, int rows)
	        throws DaoException {
	        String condition = (name != null) ? ("%" + name.toUpperCase() + "%") : "%";
	        String orderBy = (sort != null) ? (sort.equals("name")?"F.name":sort) : "F.name";

	        if (desc) {
	            orderBy += " DESC";
	        }

	        //    if (course.equalsIgnoreCase("-1")) {
	        if ((course == null) || "".equals(course)) {
	            //    Object[] args = {condition,userId};
	            //  return super.select("SELECT F.id, F.name,C.name as courseName, F.courseId, F.introduction FROM cel_content_module F, cel_content_course C WHERE F.courseId=C.id and F.name LIKE ? AND (F.public=1 or F.createdByUser=?) ", Folder.class, args, start, rows);
	            Object[] args = { condition };

	            return super.select(
		                "SELECT F.id, F.name,C.name as courseName, F.courseId, F.introduction, F.\"public\" as is_public " +
		                "FROM cel_content_module F, cel_content_course C WHERE F.courseId=C.id and UPPER(F.name) LIKE ?  ORDER BY " +
		                orderBy, Folder.class, args, start, rows);
	        } else {
	            // Object[] args = {condition,course,userId};
	            // return super.select("SELECT F.id, F.name,C.name as courseName, F.courseId, F.introduction FROM cel_content_module F, cel_content_course C WHERE F.courseId=C.id AND F.name LIKE ? AND F.courseId = ? AND (F.public=1 or F.createdByUser=?)", Folder.class, args, start, rows);
	            Object[] args = { condition, course };

	            return super.select(
		                "SELECT F.id, F.name,C.name as courseName, F.courseId, F.introduction, F.\"public\" as is_public " +
		                "FROM cel_content_module F, cel_content_course C " +
		                "WHERE F.courseId=C.id AND UPPER(F.name) LIKE ? AND F.courseId = ? ORDER BY " +
		                orderBy, Folder.class, args, start, rows);
	        }
	    }
	
	public Collection queryModuleByCourse(String courseId, boolean desc,
	        int start, int rows) throws DaoException {
	        String orderBy = "";

	        if (desc) {
	            orderBy += " DESC";
	        }

	        Object[] args = { courseId };

	        return super.select(
	            "SELECT id, courseId, name, introduction, \"public\", createdDate, createdByUser, createdByUserId from cel_content_module where \"public\"='1' AND courseId=? ORDER BY name " +
	            orderBy, Folder.class, args, start, rows);
	    }
	
	public void delete(String id) throws DaoException {
        Object[] args = new String[] { id };
        
        
        Collection assessId = super.select("SELECT id as assessId FROM cel_content_assessment WHERE module_id = ?", HashMap.class, args, 0, -1);
        Object[] args2 = new String[assessId.size()];
        int counter = 0;
        for(Iterator i = assessId.iterator(); i.hasNext(); ){
        	HashMap map = (HashMap) i.next();
        	args2[counter] = map.get("assessId").toString();
        	counter++;
        }
        
        if(args2.length > 0)
        	super.update("DELETE FROM cel_content_questions WHERE assessment_id = ?", args2); //erase questions set from assessment
        
        super.update("DELETE FROM cel_content_module WHERE id=?", args); //erase module
        super.update("DELETE FROM cel_content_lesson WHERE folderid=?", args); //erase lessons from module
        super.update("DELETE FROM cel_content_assessment WHERE module_id=?",
            args); //erase assessments from module
        
    }
	
	public int countByCourse(String courseId) throws DaoException {
        Object[] args = { courseId };
        Collection list = super.select("SELECT COUNT(*) AS total FROM cel_content_module WHERE \"public\"='1' AND courseId=?",
                HashMap.class, args, 0, 1);
        HashMap map = (HashMap) list.iterator().next();

        return Integer.parseInt(map.get("total").toString());
    }
	
	public void setActivationModule(String id) throws DaoException {
        Object args = new Object[] { id };
        super.update("UPDATE cel_content_module SET \"public\"='1' WHERE id=?", args);
    }
	
	public void setDeactivationModule(String id) throws DaoException {
        Object args = new Object[] { id };
        super.update("UPDATE cel_content_module SET \"public\"='0' WHERE id=?", args);
    }
	
	

}
