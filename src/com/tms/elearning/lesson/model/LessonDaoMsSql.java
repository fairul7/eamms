package com.tms.elearning.lesson.model;

import java.util.Collection;
import java.util.HashMap;

import com.tms.elearning.folder.model.Folder;
import com.tms.elearning.testware.model.Assessment;

import kacang.model.DaoException;
import kacang.model.DataObjectNotFoundException;

public class LessonDaoMsSql extends LessonDao{
	
	public void init() throws DaoException {
		try {
            super.update("ALTER TABLE cel_content_lesson ALTER COLUMN createdByUser varchar(70)",
                null);
        } catch (DaoException e) {
        }
        try {
            super.update("ALTER TABLE cel_content_lesson ALTER COLUMN createdByUserId varchar(70)",
                null);
        } catch (DaoException e) {
        }
		try {
            super.update("ALTER TABLE cel_content_lesson ALTER COLUMN associatedoc text",
                null);
        } catch (DaoException e) {
        }

        super.update("CREATE TABLE cel_content_lesson (" +
            "  id varchar(50) NOT NULL default ''," +
            "  courseId varchar(50) NOT NULL default ''," +
            "  folderId varchar(50) default ''," +
            "  name varchar(50) default NULL," + "  brief text default NULL," +
            "  \"public\" varchar(50)  DEFAULT '0' NOT NULL,\n" +
            "  createdDate DATETIME default '0000-00-00 00:00:00'," +
            "  createdByUser varchar(50) default NULL," +
            "  createdByUserId varchar(50) default NULL," +
            "  fileName varchar(255)," + 
            "  filePath varchar(255), " +
            "  associatedoc text DEFAULT NULL, " +
            "  PRIMARY KEY  (id)" + "  )", null);

        super.update("CREATE TABLE cel_content_course (" +
            "  id varchar(50) NOT NULL default ''," +
            "  name varchar(50) default NULL," +
            "  instructor varchar(50) default NULL," +
            "  author varchar(50) default NULL," + 
            "  synopsis text," +
            "  categoryid varchar(50) NOT NULL default ''," +
            "  registered integer default '0'," +
            "  \"public\" varchar(50)  DEFAULT '0' NOT NULL," +
            "  createdDate datetime NOT NULL default '0000-00-00 00:00:00'," +
            "  createdByUser varchar(50) default NULL," +
            "  createdByUserId varchar(50) default NULL," +
            "  PRIMARY KEY  (id)" + ")", null);

        super.update("CREATE TABLE cel_content_module (" +
            "  id varchar(50) NOT NULL default ''," +
            "  courseid varchar(50) NOT NULL default ''," +
            "  name varchar(50) default NULL," +
            "  introduction varchar(255) default NULL," +
            "  \"public\" varchar(50)  DEFAULT '0' NOT NULL," +
            "  createdDate datetime NOT NULL default '0000-00-00 00:00:00'," +
            "  createdByUser varchar(25) default NULL," +
            "  createdByUserId varchar(25) default NULL," +
            "  PRIMARY KEY  (id)" + ")", null);
    }
	
	public void insert(Lesson lesson) throws DaoException {
        super.update("INSERT INTO cel_content_lesson (id,courseId,folderId,name,brief,\"public\",createdDate,createdByUser,createdByUserId,fileName,filePath,associatedoc) VALUES (#id#,#courseId#,#folderId#,#name#,#brief#,#is_public#,#createdDate#,#createdByUser#,#createdByUserId#,#fileName#,#filePath#,#associatedoc#)",
            lesson);        
    }
	
	public void update(Lesson lesson) throws DaoException {
        super.update("UPDATE cel_content_lesson SET courseId=#courseId#,folderId=#folderId#,name=#name#,brief=#brief#,\"public\"=#is_public#,createdDate=#createdDate#,createdByUser=#createdByUser#,createdByUserId=#createdByUserId#,fileName=#fileName#,filePath=#filePath#, associatedoc=#associatedoc# WHERE id=#id#",
            lesson);
    }
	
	public Lesson load(String id)
	    throws DaoException, DataObjectNotFoundException {
	    Object[] args = { id };
	    Collection results = super.select("SELECT id,name,courseId,folderId,brief,fileName,filePath,\"public\" as is_public, associatedoc FROM cel_content_lesson WHERE id=?",
	            Lesson.class, args, 0, -1);
	
	    if (results.size() == 0) {
	        throw new DataObjectNotFoundException();
	    } else {
	        return (Lesson) results.iterator().next();
	    }
	}
	
	public Collection loadModule() throws DaoException {
        return super.select("select id, name from cel_content_module", Folder.class,
            "", 0, -1);
    }
	
	public Collection queryLessonsByFolder(String moduleId, String sort,
	        boolean desc, int start, int rows) throws DaoException {
	        String orderBy = "";

	        if (desc) {
	            orderBy += " DESC";
	        }

	        Object[] args = { moduleId };

	        return super.select(
	            "SELECT id, courseId, folderId, name, brief, \"public\", createdDate, createdByUser, createdByUserId, fileName, filePath, associatedoc from cel_content_lesson where \"public\"='1' AND folderId=? ORDER BY name " +
	            orderBy, Lesson.class, args, start, rows);
	    }
	
	public int countByFolder(String moduleId) throws DaoException {
        Object[] args = { moduleId };

        Collection list = super.select("SELECT COUNT(*) AS total FROM cel_content_lesson WHERE \"public\"='1' AND folderId=?",
                HashMap.class, args, 0, 1);
        HashMap map = (HashMap) list.iterator().next();

        return Integer.parseInt(map.get("total").toString());
    }
	
	public Collection getLessonList(String module, String userId)
	    throws DaoException {
	    //instructor A can query lesson for instructor B
	    Object[] args = new String[] { module };
	
	    return super.select("SELECT id, name as lessonName FROM cel_content_lesson WHERE courseId = ? AND (\"public\"='1' ) ORDER BY name",
	        Lesson.class, args, 0, -1);
	
	    /*Object[] args = new String[] { module,userId };
	         return super.select("SELECT id, name as lessonName FROM cel_content_lesson WHERE courseId = ? AND (public=1 OR createdByUser=?) ORDER BY name",Lesson.class,args,0,-1);
	    */
	}
	
	public Collection query(String name, String course, String folder,
	        String userId, String sort, boolean desc, int start, int rows)
	        throws DaoException {
	        String condition = (name != null) ? ("%" + name + "%") : "%";

	        String orderBy = (sort != null) ? (sort.equals("name")?"L.name":sort) : "L.name";

	        if (desc) {
	            orderBy += " DESC";
	        }
	        
	        if ((course != null) && !("".equals(course)) && (folder != null) &&
	                !("".equals(folder))) {
	            Object[] args = { condition, course, folder };

	            return super.select(
	                "SELECT L.id, L.name,C.name as courseName,F.name as folderName, L.courseId,L.brief, L.\"public\" as is_public FROM cel_content_lesson L, cel_content_course C, cel_content_module F WHERE L.courseId=C.id and L.folderId=F.id and L.name LIKE ? AND L.courseid LIKE ? AND L.folderId LIKE ? ORDER BY " +
	                orderBy, Lesson.class, args, start, rows);
	        } else if ((course != null) && !("".equals(course))) {
	            Object[] args = { condition, course };

	            return super.select(
	                "SELECT L.id, L.name,C.name as courseName,F.name as folderName, L.courseId,L.brief, L.\"public\" as is_public FROM cel_content_lesson L, cel_content_course C, cel_content_module F WHERE L.courseId=C.id and L.folderId=F.id and L.name LIKE ? AND L.courseid LIKE ? ORDER BY " +
	                orderBy, Lesson.class, args, start, rows);
	        } else if ((folder != null) && !("".equals(folder))) {
	            Object[] args = { condition, folder };

	            return super.select(
	                "SELECT L.id, L.name,C.name as courseName,F.name as folderName, L.courseId,L.brief, L.\"public\" as is_public FROM cel_content_lesson L, cel_content_course C, cel_content_module F WHERE L.courseId=C.id and L.folderId=F.id and L.name LIKE ? AND L.folderId LIKE ? ORDER BY " +
	                orderBy, Lesson.class, args, start, rows);
	        } else {
	            Object[] args = { condition };

	            return super.select(
	                "SELECT L.id, L.name,C.name as courseName,F.name as folderName, L.courseId,L.brief, L.\"public\" as is_public FROM cel_content_lesson L, cel_content_course C, cel_content_module F WHERE L.courseId=C.id and L.folderId=F.id and L.name LIKE ? ORDER BY " +
	                orderBy, Lesson.class, args, start, rows);
	        }

	    }
	
	public void setActivationLesson(String id) throws DaoException {
        Object args = new Object[] { id };
        super.update("UPDATE cel_content_lesson SET cel_content_lesson.\"public\"='1' WHERE id=?",
            args);
    }

    public void setDeactivationLesson(String id) throws DaoException {
        Object args = new Object[] { id };
        super.update("UPDATE cel_content_lesson SET cel_content_lesson.\"public\"='0' WHERE id=?",
            args);
    }

    public Collection getAssessmentByLesson(String id)
        throws DaoException {
        Object args = new Object[] { id };

        return super.select("SELECT id,name FROM cel_content_assessment WHERE lesson_id=? AND cel_content_assessment.\"public\"='1' AND (start_date <= getDate() AND end_date >= getDate()   ) ",
            Assessment.class, args, 0, -1);
    }


    public Lesson checkActive(String id) throws DaoException{


        Collection result= super.select("SELECT id,\"public\" as is_public FROM cel_content_lesson WHERE id=?",Lesson.class,new Object[]{id}, 0,1);


        if(result.size()>0)
         return (Lesson) result.iterator().next();
        else
         return null;
    }

}
