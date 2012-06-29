package com.tms.elearning.folder.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import kacang.model.DaoException;

public class FolderModuleDaoDB2 extends FolderModuleDao{
	
	public Folder getIntroduction(String id) throws DaoException{

        Collection result= super.select("SELECT introduction from cel_content_module WHERE id=?", Folder.class, new Object[]{id},0,1);

        if(result.size() >0)
        return (Folder) result.iterator().next();
        return null;

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

        //super.update("UPDATE cel_content_status SET deleted='1',deletedDate=#createdDate#,deletedByUser=#createdByUser#,deletedByUserId=#createdByUserId# WHERE id=#id#", folder);
    }
	
	public Collection queryModuleByCourse(String courseId, boolean desc,
	        int start, int rows) throws DaoException {
	        String orderBy = "";

	        if (desc) {
	            orderBy += " DESC";
	        }

	        Object[] args = { courseId };

	        return super.select(
	            "SELECT id, courseId, name, introduction, public, createdDate, createdByUser, createdByUserId from cel_content_module where public='1' AND courseId=? ORDER BY name " +
	            orderBy, Folder.class, args, start, rows);
	    }
	
	public int countByCourse(String courseId) throws DaoException {
        Object[] args = { courseId };
        Collection list = super.select("SELECT COUNT(*) AS total FROM cel_content_module WHERE public='1' AND courseId=?",
                HashMap.class, args, 0, 1);
        HashMap map = (HashMap) list.iterator().next();

        return Integer.parseInt(map.get("total").toString());
    }
	
	public Collection query(String name, String course, String userId,
	        String sort, boolean desc, int start, int rows)
	        throws DaoException {
	        String condition = (name != null) ? ("%" + name.toUpperCase() + "%") : "%";
	        String orderBy = (sort != null) ? sort : "name";

	        if (desc) {
	            orderBy += " DESC";
	        }

	        /*
	        if (course.equalsIgnoreCase("-1")) {
	            Object[] args = {condition,userId};
	            return super.select("SELECT F.id, F.name,C.name as courseName, F.courseId, F.introduction FROM cel_content_module F, cel_content_course C WHERE F.courseId=C.id and F.name LIKE ? AND F.public=1 OR F.createdByUser=? ", Folder.class, args, start, rows);
	        } else {
	            Object[] args = {condition,course,userId};
	            return super.select("SELECT F.id, F.name,C.name as courseName, F.courseId, F.introduction FROM cel_content_module F, cel_content_course C WHERE F.courseId=C.id and F.name LIKE ? AND F.courseId = ? AND F.public=1 OR F.createdByUser=?", Folder.class, args, start, rows);
	        }
	        */

	        //    if (course.equalsIgnoreCase("-1")) {
	        if ((course == null) || "".equals(course)) {
	            //    Object[] args = {condition,userId};
	            //  return super.select("SELECT F.id, F.name,C.name as courseName, F.courseId, F.introduction FROM cel_content_module F, cel_content_course C WHERE F.courseId=C.id and F.name LIKE ? AND (F.public=1 or F.createdByUser=?) ", Folder.class, args, start, rows);
	            Object[] args = { condition };

	            return super.select(
	                "SELECT F.id, F.name,C.name as courseName, F.courseId, F.introduction, F.public as is_public " +
	                "FROM cel_content_module F, cel_content_course C WHERE F.courseId=C.id and UPPER(F.name) LIKE ?  ORDER BY " +
	                orderBy, Folder.class, args, start, rows);
	        } else {
	            // Object[] args = {condition,course,userId};
	            // return super.select("SELECT F.id, F.name,C.name as courseName, F.courseId, F.introduction FROM cel_content_module F, cel_content_course C WHERE F.courseId=C.id AND F.name LIKE ? AND F.courseId = ? AND (F.public=1 or F.createdByUser=?)", Folder.class, args, start, rows);
	            Object[] args = { condition, course };

	            return super.select(
	                "SELECT F.id, F.name,C.name as courseName, F.courseId, F.introduction, F.public as is_public " +
	                "FROM cel_content_module F, cel_content_course C " +
	                "WHERE F.courseId=C.id AND UPPER(F.name) LIKE ? AND F.courseId = ? ORDER BY " +
	                orderBy, Folder.class, args, start, rows);
	        }
	    }
	
	public int count(String name, String course) throws DaoException {
        String condition = (name != null) ? ("%" + name.toUpperCase() + "%") : "%";
        Collection list;

        if ((course != null) && !("".equals(course))) {
            Object[] args = { condition, course };

            list = super.select("SELECT COUNT(*) AS total FROM cel_content_module, cel_content_course " +
            		"WHERE UPPER(cel_content_module.name) LIKE ? AND cel_content_module.courseId = cel_content_course.id " +
            		"AND cel_content_course.id=?",
                    HashMap.class, args, 0, 1);
        } else {
            Object[] args = { condition };

            list = super.select("SELECT COUNT(*) AS total FROM cel_content_module, cel_content_course " +
            		"WHERE UPPER(cel_content_module.name) LIKE ? " +
            		"AND cel_content_module.courseId = cel_content_course.id",
                    HashMap.class, args, 0, 1);
        }

        HashMap map = (HashMap) list.iterator().next();

        return Integer.parseInt(map.get("total").toString());
    }

}
