package com.tms.elearning.course.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import kacang.Application;
import kacang.model.DaoException;
import kacang.services.security.User;

public class CourseModuleDaoDB2 extends CourseModuleDao{
	
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
	            "SELECT cel_content_course.id,cel_content_course.name,cel_content_course.author,cel_content_course.synopsis,cel_content_course.instructor FROM cel_content_course LEFT JOIN cel_content_studentreg on (cel_content_course.id =cel_content_studentreg.id AND cel_content_studentreg.student=?) WHERE cel_content_course.public='1' AND cel_content_course.categoryid= ?  AND ( cel_content_studentreg.student IS NULL ) ORDER BY " +
	            orderBy, Course.class, args, start, rows);
	    }
	
	public int countList(String categoryid) throws DaoException {
        String condition = (categoryid != null) ? ("%" + categoryid + "%") : "%";
        Application app = Application.getInstance();
        User user = app.getCurrentUser();
        Object[] args = { user.getId(), condition };

        //Collection list = super.select("SELECT count(*) as total FROM cel_content_course WHERE categoryid=? AND public=\"1\"", HashMap.class, args, 0, 1);
        Collection list = super.select("SELECT count(cel_content_course.id) as total FROM cel_content_course LEFT JOIN cel_content_studentreg on (cel_content_course.id =cel_content_studentreg.id AND cel_content_studentreg.student=? )  WHERE cel_content_course.categoryid=? AND cel_content_course.public='1'  AND cel_content_studentreg.student IS NULL ",
                HashMap.class, args, 0, 1);

        HashMap map = (HashMap) list.iterator().next();

        return Integer.parseInt(map.get("total").toString());
    }
	
	public Collection query(String name, String category, String userId,
	        String sort, boolean desc, int start, int rows)
	        throws DaoException {
	        String condition = (name != null) ? ("%" + name.toUpperCase() + "%") : "%";
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
	                "SELECT course.id,course.name,course.author,course.synopsis,course.instructor as instructor," +
	                "category.category,course.public as is_public FROM cel_content_course as course, " +
	                "cel_content_course_category as category WHERE course.categoryid = category.id " +
	                "AND UPPER(course.name) LIKE ? and course.categoryid=? ORDER BY " +
	                orderBy, Course.class, args, start, rows);
	        } else {
	            Object[] args = { condition };

	            //  return super.select("SELECT id,name,author,synopsis,instructor,category FROM cel_content_course WHERE name LIKE ? AND public=1 OR createdByUser=? ORDER BY " + orderBy, Course.class, args, start, rows);
	            return super.select(
	                "SELECT course.id,course.name,course.author,course.synopsis,course.instructor as instructor," +
	                "category.category, course.public as is_public FROM cel_content_course as course, " +
	                "cel_content_course_category as category WHERE course.categoryid = category.id " +
	                "AND UPPER(course.name) LIKE ? ORDER BY " +
	                orderBy, Course.class, args, start, rows);
	        }
	    }
	
	public int count(String name, String category) throws DaoException {
        String condition = (name != null) ? ("%" + name.toUpperCase() + "%") : "%";

        Collection list;
 
       if ((category != null) && !("".equals(category))) {
            Object[] args = { condition, category };
            list = super.select("SELECT count(*) as total FROM cel_content_course as course, " +
            		"cel_content_course_category as category WHERE course.categoryid = category.id " +
            		"AND UPPER(course.name) LIKE ? and course.categoryid=?",
                    HashMap.class, args, 0, 1);
            /*list = super.select("SELECT COUNT(*) AS total FROM cel_content_course WHERE name LIKE ? and categoryid LIKE ?",
                    HashMap.class, args, 0, 1);*/


        } else {
            Object[] args = { condition };
            list = super.select("SELECT count(*) as total FROM cel_content_course as course, " +
            		"cel_content_course_category as category WHERE course.categoryid = category.id " +
            		"AND UPPER(course.name) LIKE ?",
                    HashMap.class, args, 0, 1);
           /* list = super.select("SELECT COUNT(*) AS total FROM cel_content_course WHERE name LIKE ?",
                    HashMap.class, args, 0, 1);*/
        }

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

	        String condition = (course != null) ? ("%" + course.toUpperCase() + "%") : "%";

	        //String orderBy = (sort != null) ? sort : "name";
	        if ((category != null) && !("".equals(category))) {
	            Object[] args = { studentId, condition, category };

	            // return super.select("SELECT * from cel_content_studentreg WHERE student=?", Student.class, args, start, rows);
	            return super.select(
	                "select s.id, s.student, c.name, c.createdDate, category.category as categoryName from cel_content_course_category as category, cel_content_studentreg as s , cel_content_course as c where s.id=c.id and s.student=? AND c.public='1' AND UPPER(c.name) like ? AND category.id = c.categoryid AND category.id LIKE ? ORDER BY s.student" +
	                orderBy, Student.class, args, start, rows);
	        } else {
	            Object[] args = { studentId, condition };

	            // return super.select("SELECT * from cel_content_studentreg WHERE student=?", Student.class, args, start, rows);
	            return super.select(
	                "select s.id, s.student, c.name, c.createdDate, category.category as categoryName from cel_content_course_category as category, cel_content_studentreg as s , cel_content_course as c where s.id=c.id and s.student=? AND c.public='1' AND UPPER(c.name) like ? AND c.categoryid = category.id ORDER BY s.student" +
	                orderBy, Student.class, args, start, rows);
	        }

	        //   return super.select("SELECT * FROM cel_content_studentreg", Student.class, "", start, rows);
	        //    return super.select("SELECT id,name,author,synopsis,instructor FROM cel_content_course WHERE registered= 1 ORDER BY name", Course.class, "", start, rows);
	    }
	
	public int countStudentReg(String course, String category, String studentId)
    	throws DaoException {
	    Collection list;
	    String condition = (course != null) ? ("%" + course.toUpperCase() + "%") : "%";
	
	    if ((category != null) && !("".equals(category))) {
	        Object[] args = { studentId, condition, category };
	        list = super.select("SELECT count(distinct(studentreg.id)) FROM cel_content_course_category as category, cel_content_studentreg as studentreg, cel_content_course as course WHERE studentreg.id= course.id AND course.public='1' AND studentreg.student=? AND UPPER(course.name) like ? AND category.id = course.categoryid AND category.id LIKE ?",
	                HashMap.class, args, 0, 1);
	    } else {
	        Object[] args = { studentId, condition };
	        list = super.select("SELECT count(distinct(studentreg.id)) FROM cel_content_course_category as category, cel_content_studentreg as studentreg, cel_content_course as course WHERE studentreg.id= course.id AND course.public='1' AND studentreg.student=? AND UPPER(course.name) like ? AND category.id = course.categoryid",
	                HashMap.class, args, 0, 1);
	    }
	
	    HashMap map = (HashMap) list.iterator().next();
	
	    return Integer.parseInt(map.get("total").toString());
	}
	
	public Collection querySubjects(String category, String sort, boolean desc,
	        int start, int rows) throws DaoException {
	        //    String orderBy = (sort != null) ? sort : "category";

	        /*    return super.select("SELECT distinct(course.categoryid) as categoryid, category.category as category FROM cel_content_course_category as category, cel_content_course as course ORDER BY " + orderBy, Course.class, "", start, rows);
	        */
	        String condition = (category != null) ? ("%" + category.toUpperCase() + "%") : "%";
	        String orderBy = (sort != null) ? sort : "category";

	        if (desc) {
	            orderBy += " DESC";
	        }

	        Object[] args = { condition };

	        return super.select(
	            "SELECT DISTINCT category.id as categoryid, category.category as category FROM " +
	            "cel_content_course_category category LEFT JOIN cel_content_course course on category.id = course.categoryid WHERE UPPER(category.category) like ?  ORDER BY " +
	            orderBy, Course.class, args, start, rows);
	    }
	
	public int countSubjects(String category) throws DaoException {
        String condition = (category != null) ? ("%" + category.toUpperCase() + "%") : "%";

        Object[] args = { condition };
        Collection list = super.select("SELECT COUNT(*) as total FROM cel_content_course_category category WHERE UPPER(category.category) like ? ",
                HashMap.class, args, 0, 1);
        HashMap map = (HashMap) list.iterator().next();

        return Integer.parseInt(map.get("total").toString());
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

}
