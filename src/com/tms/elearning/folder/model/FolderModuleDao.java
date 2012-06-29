package com.tms.elearning.folder.model;

import com.tms.elearning.core.model.DefaultLearningContentDao;
import com.tms.elearning.course.model.Course;

import kacang.model.DaoException;
import kacang.model.DataObjectNotFoundException;

import kacang.util.Log;

import java.util.Collection;
import java.util.HashMap;


/**
 * Created by IntelliJ IDEA.
 * User: tirupati
 * Date: Oct 25, 2004
 * Time: 12:04:38 PM
 * To change this template use File | Settings | File Templates.
 */
public class FolderModuleDao extends DefaultLearningContentDao {
    protected String getTableName() {
        return "cel_content_module";
    }

    protected Class getContentObjectClass() {
        return Folder.class;
    }

    public void init() throws DaoException {
        try {
            super.update("CREATE TABLE cel_content_module (" +
                "  id varchar(50) NOT NULL default ''," +
                "  courseId varchar(50) NOT NULL default ''," +
                "  name varchar(50) default NULL," +
                "  introduction TEXT default NULL," +
                "  public ENUM('0','1')  DEFAULT \"0\" NOT NULL," +
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
                "  registered int(1) unsigned default '0'," +
                "  public ENUM('0','1')  DEFAULT \"0\" NOT NULL," +
                "  createdDate datetime NOT NULL default '0000-00-00 00:00:00'," +
                "  createdByUser varchar(50) default NULL," +
                "  createdByUserId varchar(50) default NULL," +
                "  PRIMARY KEY  (id)" + ")", null);
        } catch (DaoException e) {
            Log.getLog(getClass()).debug("Error creating table cms_content_course");
        }
    }

    public void insert(Folder folder) throws DaoException {
        super.update("INSERT INTO cel_content_module (id,courseId,name,introduction,public,createdDate,createdByUser,createdByUserId) VALUES (#id#,#courseId#,#name#,#introduction#,#is_public#,#createdDate#,#createdByUser#,#createdByUserId#)",
            folder);

        //  super.update("INSERT INTO cel_content_status (id,new) VALUES (#id#,1)", folder);
    }

    public void update(Folder folder) throws DaoException {
        //String now=
        super.update("UPDATE cel_content_module SET courseId=#courseId#,name=#name#,introduction=#introduction#,public=#is_public# WHERE id=#id#",
            folder);

        //  super.update("UPDATE cel_content_status SET modified='1',modifiedDate=#createdDate#,modifiedByUser=#createdByUser#,modifiedByUserId=#createdByUserId# WHERE id=#id#", folder);
    }

    public void delete(String id) throws DaoException {
        Object[] args = new String[] { id };

        super.update("DELETE cel_content_questions.* FROM cel_content_questions, cel_content_assessment WHERE cel_content_questions.assessment_id = cel_content_assessment.id AND cel_content_assessment.module_id=?",
            args); //erase questions set from assessment
        super.update("DELETE FROM cel_content_module WHERE id=?", args); //erase module
        super.update("DELETE FROM cel_content_lesson WHERE folderid=?", args); //erase lessons from module
        super.update("DELETE FROM cel_content_assessment WHERE module_id=?",
            args); //erase assessments from module

        //super.update("UPDATE cel_content_status SET deleted='1',deletedDate=#createdDate#,deletedByUser=#createdByUser#,deletedByUserId=#createdByUserId# WHERE id=#id#", folder);
    }

    public Folder load(String id)
        throws DaoException, DataObjectNotFoundException {
        Object[] args = { id };
        Collection results = super.select("SELECT id,name,courseId,introduction, public as is_public FROM cel_content_module WHERE id=?",
                Folder.class, args, 0, -1);

        if (results.size() == 0) {
            throw new DataObjectNotFoundException();
        } else {
            return (Folder) results.iterator().next();
        }
    }

    public Collection loadCourses()
        throws DaoException, DataObjectNotFoundException {
        //Collection results = super.select("SELECT name AS courseName,id AS courseId FROM cel_content_course", Folder.class, "", 0, -1);
        Collection results = super.select("SELECT id AS courseId, name AS courseName FROM cel_content_course ORDER BY courseName",
                Folder.class, null, 0, -1);

        if (results.size() == 0) {
            throw new DataObjectNotFoundException();
        } else {
            return results;
        }
    }

    public Collection getCourses(String userId) throws DaoException {
        // Object[] args = {userId};
        // return super.select("SELECT id AS courseId, name AS courseName FROM cel_content_course WHERE public=1 OR createdByUser=? ORDER BY courseName",Folder.class,args,0,-1);
        return super.select("SELECT id AS courseId, name AS courseName FROM cel_content_course ORDER BY courseName",
            Folder.class, "", 0, -1);
    }

    /*
    public Collection getCourses() throws DaoException {
        return super.select("SELECT id AS courseId, name AS courseName FROM cel_content_course ORDER BY courseName",Folder.class,null,0,-1);
    }
    */
    public Collection query(String name, String course, String userId,
        String sort, boolean desc, int start, int rows)
        throws DaoException {
        String condition = (name != null) ? ("%" + name + "%") : "%";
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
                "SELECT F.id, F.name,C.name as courseName, F.courseId, F.introduction, F.public as is_public FROM cel_content_module F, cel_content_course C WHERE F.courseId=C.id and F.name LIKE ?  ORDER BY " +
                orderBy, Folder.class, args, start, rows);
        } else {
            // Object[] args = {condition,course,userId};
            // return super.select("SELECT F.id, F.name,C.name as courseName, F.courseId, F.introduction FROM cel_content_module F, cel_content_course C WHERE F.courseId=C.id AND F.name LIKE ? AND F.courseId = ? AND (F.public=1 or F.createdByUser=?)", Folder.class, args, start, rows);
            Object[] args = { condition, course };

            return super.select(
                "SELECT F.id, F.name,C.name as courseName, F.courseId, F.introduction, F.public as is_public FROM cel_content_module F, cel_content_course C WHERE F.courseId=C.id AND F.name LIKE ? AND F.courseId = ? ORDER BY " +
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
            "SELECT * from cel_content_module where public=\"1\" AND courseId=? ORDER BY name " +
            orderBy, Folder.class, args, start, rows);
    }

    public int count(String name, String course) throws DaoException {
        String condition = (name != null) ? ("%" + name + "%") : "%";
        Collection list;

        if ((course != null) && !("".equals(course))) {
            Object[] args = { condition, course };

            list = super.select("SELECT COUNT(*) AS total FROM cel_content_module, cel_content_course WHERE cel_content_module.name LIKE ? AND cel_content_module.courseId = cel_content_course.id AND cel_content_course.id=?",
                    HashMap.class, args, 0, 1);
        } else {
            Object[] args = { condition };

            list = super.select("SELECT COUNT(*) AS total FROM cel_content_module, cel_content_course WHERE cel_content_module.name LIKE ? AND cel_content_module.courseId = cel_content_course.id",
                    HashMap.class, args, 0, 1);
        }

        HashMap map = (HashMap) list.iterator().next();

        return Integer.parseInt(map.get("total").toString());
    }

    public int countByCourse(String courseId) throws DaoException {
        Object[] args = { courseId };
        Collection list = super.select("SELECT COUNT(*) AS total FROM cel_content_module WHERE public=\"1\" AND courseId=?",
                HashMap.class, args, 0, 1);
        HashMap map = (HashMap) list.iterator().next();

        return Integer.parseInt(map.get("total").toString());
    }

    public int count(String name, int id) throws DaoException {
        String condition = (name != null) ? ("%" + name + "%") : "%";
        Object[] args = { condition };

        Collection list = super.select("SELECT COUNT(*) AS total FROM cel_content_module WHERE courseId= ?",
                HashMap.class, args, 0, 1);
        HashMap map = (HashMap) list.iterator().next();

        return Integer.parseInt(map.get("total").toString());
    }

    public Folder folderExists(String name) throws DaoException {
        Object[] args = { name };
        Collection results = super.select("SELECT id FROM cel_content_module WHERE name = ?",
                Folder.class, args, 0, 1);

        if (results.size() == 0) {
            return null;
        }

        return (Folder) results.iterator().next();
    }

    public Folder checkFolderExits(String name,String courseId) throws DaoException{

        Object[] args = { name, courseId};
        Collection results = super.select("SELECT id FROM cel_content_module WHERE name = ? and courseId=?",
                Folder.class, args, 0, 1);

        if (results.size() == 0) {
            return null;
        }

        return (Folder) results.iterator().next();


    }


     public Folder checkFolderExitsModuleId(String name,String courseId, String moduleid) throws DaoException{

        Object[] args = { name, courseId,moduleid};
        Collection results = super.select("SELECT id FROM cel_content_module WHERE name = ? and courseId=? AND id !=?",
                Folder.class, args, 0, 1);

        if (results.size() == 0) {
            return null;
        }

        return (Folder) results.iterator().next();


    }


    public Folder folderExists(String name, String id)
        throws DaoException {
        Object[] args = { name, id };
        Collection results = super.select("SELECT id FROM cel_content_module WHERE name = ? AND id <> ?",
                Folder.class, args, 0, 1);

        if (results.size() == 0) {
            return null;
        }

        return (Folder) results.iterator().next();
    }

    //--- mahe ---
    public Collection getModuleList(String course, String userId)
        throws DaoException {
        //ignore userID, consider all instructors are the same, so instructor A created module, instructor B also can access it
        Object[] args = new String[] { course };

        return super.select("SELECT id, name as folderName FROM cel_content_module WHERE courseId = ?  ORDER BY name",
            Folder.class, args, 0, -1);

        /*
        old one
        Object[] args = new String[] { course,userId };
         return super.select("SELECT id, name as folderName FROM cel_content_module WHERE courseId = ?  and (public='1' or createdByUser=?) ORDER BY name",Folder.class,args,0,-1);
        */
    }

    public Collection getModuleList(String course) throws DaoException {
        Object[] args = new String[] { course };

        return super.select("SELECT id, name as folderName FROM cel_content_module WHERE course_id = ? ORDER BY name",
            Folder.class, args, 0, -1);
    }

    //--- mahe ---
    public Collection loadCourse() throws DaoException {
        return super.select("select id, name from cel_content_course", Course.class,
            "", 0, -1);
    }

    public void setActivationModule(String id) throws DaoException {
        Object args = new Object[] { id };
        super.update("UPDATE cel_content_module SET public='1' WHERE id=?", args);
    }

    public void setDeactivationModule(String id) throws DaoException {
        Object args = new Object[] { id };
        super.update("UPDATE cel_content_module SET public='0' WHERE id=?", args);
    }

    public Folder getIntroduction(String id) throws DaoException{

        Collection result= super.select("SELECT * from cel_content_module WHERE id=?", Folder.class, new Object[]{id},0,1);

        if(result.size() >0)
        return (Folder) result.iterator().next();


        return null;



    }


}
