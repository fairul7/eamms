package com.tms.elearning.course.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import kacang.Application;
import kacang.model.DaoException;
import kacang.model.DataObjectNotFoundException;
import kacang.services.security.User;
import kacang.util.Log;

public class CourseModuleDaoMsSql extends CourseModuleDao{
	
	public void init() throws DaoException {
        
		try {
            super.update("ALTER TABLE cel_content_course ALTER COLUMN createdByUserId varchar(70)", null);
        } catch (DaoException e) {
            Log.getLog(getClass()).debug("Error editing table cel_content_course");
        }
        try {
            super.update("ALTER TABLE cel_content_course ALTER COLUMN createdByUser varchar(70)", null);
        } catch (DaoException e) {
            Log.getLog(getClass()).debug("Error editing table cel_content_course");
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
                "  \"public\" varchar(50)  DEFAULT \"0\" NOT NULL," +
                "  createdDate DATETIME default '0000-00-00 00:00:00'," +
                "  createdByUser varchar(50) default NULL," +
                "  createdByUserId varchar(50) default NULL," +
                "  PRIMARY KEY  (id)" + ")", null);
        } catch (DaoException e) {
            Log.getLog(getClass()).debug("Error creating table cms_content_course");
        }

        try {
            super.update("CREATE TABLE cel_content_lesson (" +
                "  id varchar(50) NOT NULL default '0'," +
                "  courseId varchar(50) default NULL," +
                "  folderId varchar(50) default NULL," +
                "  name varchar(50) default NULL," + "  brief text," +
                "  \"public\" varchar(50)  DEFAULT \"0\" NOT NULL," +
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
                "  \"public\" varchar(50)  DEFAULT \"0\" NOT NULL," +
                "  createdDate datetime NOT NULL default '0000-00-00 00:00:00'," +
                "  createdByUser varchar(25) default NULL," +
                "  createdByUserId varchar(25) default NULL," +
                "  PRIMARY KEY  (id)" + ")", null);
        } catch (DaoException e) {
            Log.getLog(getClass()).debug("Error creating table cms_content_module");
        }
    }
	
	public void delete(String id) throws DaoException {
        Object[] args = new String[] { id };
        
        Collection modId = super.select("SELECT id as modId FROM cel_content_questions, cel_content_assessment WHERE cel_content_questions.assessment_id = cel_content_assessment.id AND cel_content_assessment.course_id=?", HashMap.class, args, 0, -1);
        
        Object[] args2 = new String[modId.size()];
        int counter = 0;
        for(Iterator i = modId.iterator(); i.hasNext(); ){
        	HashMap map = (HashMap) i.next();
        	args2[counter] = map.get("assessId").toString();
        	counter++;
        }
        
        if(args2.length > 0)
        	super.update("DELETE FROM cel_content_questions WHERE assessment_id = ?", args2); //erase questions set from assessment
        
        super.update("DELETE FROM cel_content_course WHERE id=?", args);
        super.update("DELETE FROM cel_content_module WHERE courseId=?", args);
        super.update("DELETE FROM cel_content_lesson WHERE courseId=?", args);
        super.update("DELETE FROM cel_content_assessment WHERE course_id=?",
            args);

    }
	
	public void insert(Course course) throws DaoException {
        super.update("INSERT INTO cel_content_course (id,name,author,synopsis,instructor,categoryid,\"public\",createdDate,createdByUser,createdByUserId) VALUES (#id#,#name#,#author#,#synopsis#,#instructor#,#categoryid#,#is_public#,#createdDate#,#createdByUser#,#createdByUserId#)",
            course);

        //  super.update("INSERT INTO cel_content_status (id,new) VALUES (#id#,1)", course);
    }
	
	public Collection querySubjects(String category, String sort, boolean desc, int start, int rows) throws DaoException {
	        
		String condition = (category != null) ? ("%" + category + "%") : "%";
	    String orderBy = (sort != null) ? sort : "category";

	    if (desc) {
	    	orderBy += " DESC";
	    }

	    Object[] args = { condition };

	    return super.select(
	    	"SELECT distinct category.id as categoryid, category.category as category FROM " +
	        "cel_content_course_category category LEFT JOIN cel_content_course course on category.id = course.categoryid WHERE category.category like ?  ORDER BY " +
	        orderBy, Course.class, args, start, rows);
	}
	
	public Collection query(String name, String category, String userId,
	        String sort, boolean desc, int start, int rows)
	        throws DaoException {
	        String condition = (name != null) ? ("%" + name + "%") : "%";
	        String orderBy = (sort != null) ? sort : "name";

	        if (desc) {
	            orderBy += " DESC";
	        }

	        if ((category != null) && !("".equals(category))) {
	            Object[] args = { condition, category };

	            return super.select(
	                "SELECT course.id,course.name,course.author,course.synopsis,course.instructor as instructor,category.category,course.\"public\" as is_public FROM cel_content_course as course, cel_content_course_category as category WHERE course.categoryid = category.id AND course.name LIKE ? and course.categoryid=? ORDER BY " +
	                orderBy, Course.class, args, start, rows);
	        } else {
	            Object[] args = { condition };

	            return super.select(
	                "SELECT course.id,course.name,course.author,course.synopsis,course.instructor as instructor,category.category, course.\"public\" as is_public FROM cel_content_course as course, cel_content_course_category as category WHERE course.categoryid = category.id AND course.name LIKE ? ORDER BY " +
	                orderBy, Course.class, args, start, rows);
	        }
	    }
	
	public void update(Course course) throws DaoException {
        super.update("UPDATE cel_content_course SET name=#name#, author=#author#, synopsis=#synopsis#, instructor=#instructor#, categoryid=#categoryid#, \"public\"=#is_public# WHERE id=#id#",
            course);

        //    super.update("INSERT INTO cel_content_status (id,modified,modifiedDate,modifiedByUser,modifiedByUserId) VALUES (#id#,'Y',#createdDate#,#createdByUser#,#createdByUserId#)", course);
    }
	
	public Course load(String id)
	    throws DaoException, DataObjectNotFoundException {
	    Object[] args = { id };
	    Collection results = super.select("SELECT course.id,course.name,course.author,course.synopsis,course.instructor,category.category, course.categoryid, course.\"public\" as is_public FROM cel_content_course as course, cel_content_course_category as category WHERE course.categoryid = category.id AND course.id=?",
	            Course.class, args, 0, 1);
	
	    if (results.size() == 0) {
	        //not error, something dont have record in database
	        //   throw new DataObjectNotFoundException();
	        return null;
	    } else {
	        return (Course) results.iterator().next();
	    }
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
	            "SELECT cel_content_course.id,cel_content_course.name,cel_content_course.author,cel_content_course.synopsis,cel_content_course.instructor FROM cel_content_course LEFT JOIN cel_content_studentreg on (cel_content_course.id =cel_content_studentreg.id AND cel_content_studentreg.student=?) WHERE cel_content_course.\"public\"='1' AND cel_content_course.categoryid= ?  AND ( cel_content_studentreg.student IS NULL ) ORDER BY " +
	            orderBy, Course.class, args, start, rows);
	    }
	
	public int countList(String categoryid) throws DaoException {
        String condition = (categoryid != null) ? ("%" + categoryid + "%") : "%";
        Application app = Application.getInstance();
        User user = app.getCurrentUser();
        Object[] args = { user.getId(), condition };

        //Collection list = super.select("SELECT count(*) as total FROM cel_content_course WHERE categoryid=? AND public=\"1\"", HashMap.class, args, 0, 1);
        Collection list = super.select("SELECT count(cel_content_course.id) as total FROM cel_content_course LEFT JOIN cel_content_studentreg on (cel_content_course.id =cel_content_studentreg.id AND cel_content_studentreg.student=? )  WHERE cel_content_course.categoryid=? AND cel_content_course.\"public\"='1'  AND cel_content_studentreg.student IS NULL ",
                HashMap.class, args, 0, 1);

        HashMap map = (HashMap) list.iterator().next();

        return Integer.parseInt(map.get("total").toString());
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
	                "select s.id, s.student, c.name, c.createdDate, category.category as categoryName from cel_content_course_category as category, cel_content_studentreg as s , cel_content_course as c where s.id=c.id and s.student=? AND c.\"public\"='1' AND c.name like ? AND category.id = c.categoryid AND category.id LIKE ? ORDER BY s.student" +
	                orderBy, Student.class, args, start, rows);
	        } else {
	            Object[] args = { studentId, condition };

	            // return super.select("SELECT * from cel_content_studentreg WHERE student=?", Student.class, args, start, rows);
	            return super.select(
	                "select s.id, s.student, c.name, c.createdDate, category.category as categoryName from cel_content_course_category as category, cel_content_studentreg as s , cel_content_course as c where s.id=c.id and s.student=? AND c.\"public\"='1' AND c.name like ? AND c.categoryid = category.id ORDER BY s.student" +
	                orderBy, Student.class, args, start, rows);
	        }

	        //   return super.select("SELECT * FROM cel_content_studentreg", Student.class, "", start, rows);
	        //    return super.select("SELECT id,name,author,synopsis,instructor FROM cel_content_course WHERE registered= 1 ORDER BY name", Course.class, "", start, rows);
	    }
	
	public int countStudentReg(String course, String category, String studentId)
	    throws DaoException {
	    Collection list;
	    String condition = (course != null) ? ("%" + course + "%") : "%";
	
	    if ((category != null) && !("".equals(category))) {
	        Object[] args = { studentId, condition, category };
	        list = super.select("SELECT count(distinct(studentreg.id)) FROM cel_content_course_category as category, cel_content_studentreg as studentreg, cel_content_course as course WHERE studentreg.id= course.id AND course.\"public\"='1' AND studentreg.student=? AND course.name like ? AND category.id = course.categoryid AND category.id LIKE ?",
	                HashMap.class, args, 0, 1);
	    } else {
	        Object[] args = { studentId, condition };
	        list = super.select("SELECT count(distinct(studentreg.id)) FROM cel_content_course_category as category, cel_content_studentreg as studentreg, cel_content_course as course WHERE studentreg.id= course.id AND course.\"public\"='1' AND studentreg.student=? AND course.name like ? AND category.id = course.categoryid",
	                HashMap.class, args, 0, 1);
	    }
	
	    HashMap map = (HashMap) list.iterator().next();
	
	    return Integer.parseInt(map.get("total").toString());
	}
	
	public void setActivateCourse(String id) throws DaoException {
        Object args = new Object[] { id };

        super.update("UPDATE cel_content_course SET \"public\"='1' WHERE id=?", args);
    }

    public void setDeactivateCourse(String id) throws DaoException {
        Object args = new Object[] { id };

        super.update("UPDATE cel_content_course SET \"public\"='0' WHERE id=?", args);
    }

}
