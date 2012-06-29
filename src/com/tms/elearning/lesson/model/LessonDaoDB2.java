package com.tms.elearning.lesson.model;

import java.util.Collection;
import java.util.HashMap;

import com.tms.elearning.folder.model.Folder;
import com.tms.elearning.testware.model.Assessment;

import kacang.model.DaoException;

public class LessonDaoDB2 extends LessonDao{
	
	public Collection getAssessmentByLesson(String id)
	    throws DaoException {
	    Object args = new Object[] { id };
	
	    return super.select("SELECT id,name FROM cel_content_assessment WHERE lesson_id=? AND cel_content_assessment.public='1' AND start_date <= CURRENT_TIMESTAMP AND end_date >= CURRENT_TIMESTAMP ",
	        Assessment.class, args, 0, -1);
	}
	
	public Collection queryLessonsByFolder(String moduleId, String sort,
	        boolean desc, int start, int rows) throws DaoException {
	        String orderBy = "";

	        if (desc) {
	            orderBy += " DESC";
	        }

	        Object[] args = { moduleId };

	        return super.select(
	            "SELECT id, courseId, folderId, name, brief, public, createdDate, createdByUser, createdByUserId, fileName, filePath, associatedoc from cel_content_lesson where public='1' AND folderId=? ORDER BY name " +
	            orderBy, Lesson.class, args, start, rows);
	    }
	
	public int countByFolder(String moduleId) throws DaoException {
        Object[] args = { moduleId };

        Collection list = super.select("SELECT COUNT(*) AS total FROM cel_content_lesson WHERE public='1' AND folderId=?",
                HashMap.class, args, 0, 1);
        HashMap map = (HashMap) list.iterator().next();

        return Integer.parseInt(map.get("total").toString());
    }
	
	
	public Collection loadModule() throws DaoException {
        return super.select("select id, name from cel_content_module", Folder.class,
            "", 0, -1);
    }
	
	public Collection query(String name, String course, String folder,
	        String userId, String sort, boolean desc, int start, int rows)
	        throws DaoException {
	        String condition = (name != null) ? ("%" + name.toUpperCase() + "%") : "%";

	        String orderBy = (sort != null) ? sort : "name";

	        if (desc) {
	            orderBy += " DESC";
	        }

	        /*
	        if ((course.equalsIgnoreCase("-1")) && (folder.equalsIgnoreCase("-1"))) {
	            Object[] args = { condition,userId};
	            return super.select("SELECT L.id, L.name,C.name as courseName,F.name as folderName, L.courseId,L.brief FROM cel_content_lesson L, cel_content_course C, cel_content_module F WHERE L.courseId=C.id and L.folderId=F.id and L.name LIKE ? AND (L.public=1 OR L.createdByUser=?)", Lesson.class, args, start, rows);
	        }else if (!course.equalsIgnoreCase("-1")){
	            Object[] args = {condition,course,userId};
	            return super.select("SELECT L.id, L.name,C.name as courseName, F.name as folderName, L.courseId,L.brief FROM cel_content_lesson L, cel_content_course C , cel_content_module F WHERE L.courseId=C.id and L.folderId=F.id and L.name LIKE ? AND L.courseId = ? AND (L.public=1 OR L.createdByUser=?)", Lesson.class, args, start, rows);
	        }else if (!folder.equalsIgnoreCase("-1")){
	            Object[] args = {condition,folder,userId};
	            return super.select("SELECT L.id, L.name,C.name as courseName, F.name as folderName, L.courseId,L.brief FROM cel_content_lesson L, cel_content_course C , cel_content_module F WHERE L.courseId=C.id and L.folderId=F.id and L.name LIKE ? AND L.folderId = ? AND (L.public=1 OR L.createdByUser=?)", Lesson.class, args, start, rows);
	        }else if(!(course.equalsIgnoreCase("-1")) && !(folder.equalsIgnoreCase("-1")) && condition==null) {
	            Object[] args = {course,folder,userId};
	            return super.select("SELECT L.id, L.name,C.name as courseName, F.name as folderName, L.courseId,L.brief FROM cel_content_lesson L, cel_content_course C , cel_content_module F WHERE L.courseId=C.id and L.folderId=F.id and L.courseId = ? AND L.folderId = ? AND (L.public=1 OR L.createdByUser=?)", Lesson.class, args, start, rows);
	        } else {
	            Object[] args = {condition,course,folder,userId};
	            return super.select("SELECT L.id, L.name,C.name as courseName, F.name as folderName, L.courseId,L.brief FROM cel_content_lesson L, cel_content_course C , cel_content_module F WHERE L.courseId=C.id and L.folderId=F.id and L.name LIKE ? AND L.courseId = ? AND L.folderId = ? AND (L.public=1 OR L.createdByUser=?)", Lesson.class, args, start, rows);
	        }
	          */
	        /*
	            if ((course.equalsIgnoreCase("-1")) && (folder.equalsIgnoreCase("-1"))) {
	                Object[] args = { condition};
	                return super.select("SELECT L.id, L.name,C.name as courseName,F.name as folderName, L.courseId,L.brief FROM cel_content_lesson L, cel_content_course C, cel_content_module F WHERE L.courseId=C.id and L.folderId=F.id and L.name LIKE ? ORDER BY "+orderBy, Lesson.class, args, start, rows);
	            }else if (!course.equalsIgnoreCase("-1")){
	                Object[] args = {condition,course};
	                return super.select("SELECT L.id, L.name,C.name as courseName, F.name as folderName, L.courseId,L.brief FROM cel_content_lesson L, cel_content_course C , cel_content_module F WHERE L.courseId=C.id and L.folderId=F.id and L.name LIKE ? AND L.courseId = ? ", Lesson.class, args, start, rows);
	            }else if (!folder.equalsIgnoreCase("-1")){
	                Object[] args = {condition,folder};
	                return super.select("SELECT L.id, L.name,C.name as courseName, F.name as folderName, L.courseId,L.brief FROM cel_content_lesson L, cel_content_course C , cel_content_module F WHERE L.courseId=C.id and L.folderId=F.id and L.name LIKE ? AND L.folderId = ? ", Lesson.class, args, start, rows);
	            }else if(!(course.equalsIgnoreCase("-1")) && !(folder.equalsIgnoreCase("-1")) && condition==null) {
	                Object[] args = {course,folder};
	                return super.select("SELECT L.id, L.name,C.name as courseName, F.name as folderName, L.courseId,L.brief FROM cel_content_lesson L, cel_content_course C , cel_content_module F WHERE L.courseId=C.id and L.folderId=F.id and L.courseId = ? AND L.folderId = ? ", Lesson.class, args, start, rows);
	            } else {
	                Object[] args = {condition,course,folder};
	                return super.select("SELECT L.id, L.name,C.name as courseName, F.name as folderName, L.courseId,L.brief FROM cel_content_lesson L, cel_content_course C , cel_content_module F WHERE L.courseId=C.id and L.folderId=F.id and L.name LIKE ? AND L.courseId = ? AND L.folderId = ? ", Lesson.class, args, start, rows);
	            }
	          */
	        if ((course != null) && !("".equals(course)) && (folder != null) &&
	                !("".equals(folder))) {
	            Object[] args = { condition, course, folder };

	            return super.select(
	                "SELECT L.id, L.name,C.name as courseName,F.name as folderName, L.courseId,L.brief, L.public as is_public FROM cel_content_lesson L, cel_content_course C, cel_content_module F WHERE L.courseId=C.id and L.folderId=F.id and UPPER(L.name) LIKE ? AND L.courseid LIKE ? AND L.folderId LIKE ? ORDER BY " +
	                orderBy, Lesson.class, args, start, rows);
	        } else if ((course != null) && !("".equals(course))) {
	            Object[] args = { condition, course };

	            return super.select(
	                "SELECT L.id, L.name,C.name as courseName,F.name as folderName, L.courseId,L.brief, L.public as is_public FROM cel_content_lesson L, cel_content_course C, cel_content_module F WHERE L.courseId=C.id and L.folderId=F.id and UPPER(L.name) LIKE ? AND L.courseid LIKE ? ORDER BY " +
	                orderBy, Lesson.class, args, start, rows);
	        } else if ((folder != null) && !("".equals(folder))) {
	            Object[] args = { condition, folder };

	            return super.select(
	                "SELECT L.id, L.name,C.name as courseName,F.name as folderName, L.courseId,L.brief, L.public as is_public FROM cel_content_lesson L, cel_content_course C, cel_content_module F WHERE L.courseId=C.id and L.folderId=F.id and UPPER(L.name) LIKE ? AND L.folderId LIKE ? ORDER BY " +
	                orderBy, Lesson.class, args, start, rows);
	        } else {
	            Object[] args = { condition };

	            return super.select(
	                "SELECT L.id, L.name,C.name as courseName,F.name as folderName, L.courseId,L.brief, L.public as is_public FROM cel_content_lesson L, cel_content_course C, cel_content_module F WHERE L.courseId=C.id and L.folderId=F.id and UPPER(L.name) LIKE ? ORDER BY " +
	                orderBy, Lesson.class, args, start, rows);
	        }

	        //  return super.select("SELECT * FROM cel_content_lesson WHERE name  LIKE ? ORDER BY "+orderBy, Lesson.class, args, start, rows);
	    }

}
