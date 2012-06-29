package com.tms.elearning.lesson.model;

import com.tms.cms.core.model.InvalidKeyException;

import com.tms.elearning.core.model.DefaultLearningContentDao;
import com.tms.elearning.folder.model.Folder;
import com.tms.elearning.testware.model.Assessment;

import kacang.Application;

import kacang.model.DaoException;
import kacang.model.DataObjectNotFoundException;

import kacang.services.storage.StorageFile;
import kacang.services.storage.StorageService;

import java.util.Collection;
import java.util.HashMap;
import java.util.Properties;


/**
 * Created by IntelliJ IDEA.
 * User: tirupati
 * Date: Oct 26, 2004
 * Time: 3:54:50 PM
 * To change this template use File | Settings | File Templates.
 */
public class LessonDao extends DefaultLearningContentDao {
    protected String getTableName() {
        return "cel_content_lesson";
    }

    protected Class getContentObjectClass() {
        return Lesson.class;
    }

    protected String getStorageRoot() {
        return "lesson";
    }

    protected Properties properties() {
        Properties props = new Properties();
        props.putAll(DEFAULT_PROPERTIES);
        props.put("fileName", "fileName");
        props.put("filePath", "filePath");
        props.put("fileSize", "fileSize");
        props.put("contentType", "contentType");

        return props;
    }

    public void init() throws DaoException {
        try {
            super.update("ALTER TABLE cel_content_lesson ADD associatedoc text",
                null);
        } catch (DaoException e) {
        }

        super.update("CREATE TABLE cel_content_lesson (" +
            "  id varchar(50) NOT NULL default ''," +
            "  courseId varchar(50) NOT NULL default ''," +
            "  folderId varchar(50) default ''," +
            "  name varchar(50) default NULL," + "  brief text default NULL," +
            "  public ENUM('0','1')  DEFAULT \"0\" NOT NULL,\n" +
            "  createdDate DATETIME default '0000-00-00 00:00:00'," +
            "  createdByUser varchar(50) default NULL," +
            "  createdByUserId varchar(50) default NULL," +
            "  fileName varchar(255)," + "  filePath varchar(255)," +
            "  PRIMARY KEY  (id)" + "  associatedoc text DEFAULT NULL)", null);

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

        super.update("CREATE TABLE cel_content_module (" +
            "  id varchar(50) NOT NULL default ''," +
            "  courseId varchar(50) NOT NULL default ''," +
            "  name varchar(50) default NULL," +
            "  introduction varchar(255) default NULL," +
            "  public ENUM('0','1')  DEFAULT \"0\" NOT NULL," +
            "  createdDate datetime NOT NULL default '0000-00-00 00:00:00'," +
            "  createdByUser varchar(25) default NULL," +
            "  createdByUserId varchar(25) default NULL," +
            "  PRIMARY KEY  (id)" + ")", null);
    }

    public void insert(Lesson lesson) throws DaoException {
        super.update("INSERT INTO cel_content_lesson (id,courseId,folderId,name,brief,public,createdDate,createdByUser,createdByUserId,fileName,filePath,associatedoc) VALUES (#id#,#courseId#,#folderId#,#name#,#brief#,#is_public#,#createdDate#,#createdByUser#,#createdByUserId#,#fileName#,#filePath#,#associatedoc#)",
            lesson);

        // super.update("INSERT INTO cel_content_status (id,new) VALUES (#id#,1)", lesson);
    }

    public void update(Lesson lesson) throws DaoException {
        //  super.update("UPDATE cel_content_lesson SET courseId=#courseId#,folderId=#folderId#,name=#name#,brief=#brief#,attachment=#attachment# WHERE id=#id#", lesson);
        super.update("UPDATE cel_content_lesson SET courseId=#courseId#,folderId=#folderId#,name=#name#,brief=#brief#,public=#is_public#,createdDate=#createdDate#,createdByUser=#createdByUser#,createdByUserId=#createdByUserId#,fileName=#fileName#,filePath=#filePath#, associatedoc=#associatedoc# WHERE id=#id#",
            lesson);

        //close//   super.update("UPDATE cel_content_status SET modified='1',modifiedDate=#createdDate#,modifiedByUser=#createdByUser#,modifiedByUserId=#createdByUserId# WHERE id=#id#",lesson);
    }

    public void delete(String id) throws DaoException {
        Object[] args = new String[] { id };
        super.update("DELETE FROM cel_content_lesson WHERE id=?", args);

        //super.update("INSERT INTO cel_content_status (id,deleted,deletedDate,deletedByUser,deletedByUserId) VALUES (#id#,1,#createdDate#,#createdByUser#,#createdByUserId#)",lesson);
    }

    public Lesson load(String id)
        throws DaoException, DataObjectNotFoundException {
        Object[] args = { id };
        Collection results = super.select("SELECT id,name,courseId,folderId,brief,fileName,filePath,public as is_public, associatedoc FROM cel_content_lesson WHERE id=?",
                Lesson.class, args, 0, -1);

        if (results.size() == 0) {
            throw new DataObjectNotFoundException();
        } else {
            return (Lesson) results.iterator().next();
        }
    }

    public Collection getCourses(String userId) throws DaoException {
        //  Object[] args = {userId};
        // return super.select("SELECT id AS courseId, name AS courseName FROM cel_content_course WHERE public=1 OR createdByUser=? ORDER BY courseName",Lesson.class,args,0,-1);
        return super.select("SELECT id AS courseId, name AS courseName FROM cel_content_course ORDER BY courseName",
            Lesson.class, "", 0, -1);
    }

    public Collection getCourses(String courseId, int id)
        throws DaoException {
        Object[] args = { courseId };

        return super.select("SELECT id AS courseId, name AS courseName FROM cel_content_course WHERE id = ? ORDER BY courseName",
            Lesson.class, args, 0, -1);
    }

    public Collection getFolders(String userId) throws DaoException {
        //   Object[] args = {userId};
        // return super.select("SELECT id AS folderId, name AS folderName FROM cel_content_module WHERE public=1 OR createdByUser=? ORDER BY folderName",Lesson.class,args,0,-1);
        return super.select("SELECT id AS folderId, name AS folderName FROM cel_content_module  ORDER BY folderName",
            Lesson.class, "", 0, -1);
    }

    public Collection getFoldersByCourse(String courseId)
        throws DaoException {
        Object[] args = { courseId };

        // return super.select("SELECT id AS folderId, name AS folderName FROM cel_content_module WHERE public=1 OR createdByUser=? ORDER BY folderName",Lesson.class,args,0,-1);
        return super.select("SELECT id AS folderId, name AS folderName FROM cel_content_module  WHERE courseId=? ORDER BY folderName",
            Lesson.class, args, 0, -1);
    }

    public Collection getCourses() throws DaoException {
        return super.select("SELECT id AS courseId, name AS courseName FROM cel_content_course ORDER BY courseName",
            Lesson.class, null, 0, -1);
    }

    public Collection getFolders() throws DaoException {
        return super.select("SELECT id AS folderId, name AS folderName FROM cel_content_module ORDER BY folderName",
            Lesson.class, null, 0, -1);
    }

    //filter for table
    public Collection loadModule() throws DaoException {
        return super.select("select * from cel_content_module", Folder.class,
            "", 0, -1);
    }

    public Collection getFolders(String courseId, int i)
        throws DaoException {
        Object[] args = { courseId };

        return super.select("SELECT id AS folderId, name AS folderName FROM cel_content_module WHERE courseId = ? ORDER BY folderName",
            Lesson.class, args, 0, -1);
    }

    public Collection query(String name, String course, String folder,
        String userId, String sort, boolean desc, int start, int rows)
        throws DaoException {
        String condition = (name != null) ? ("%" + name + "%") : "%";

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
                "SELECT L.id, L.name,C.name as courseName,F.name as folderName, L.courseId,L.brief, L.public as is_public FROM cel_content_lesson L, cel_content_course C, cel_content_module F WHERE L.courseId=C.id and L.folderId=F.id and L.name LIKE ? AND L.courseId LIKE ? AND L.folderId LIKE ? ORDER BY " +
                orderBy, Lesson.class, args, start, rows);
        } else if ((course != null) && !("".equals(course))) {
            Object[] args = { condition, course };

            return super.select(
                "SELECT L.id, L.name,C.name as courseName,F.name as folderName, L.courseId,L.brief, L.public as is_public FROM cel_content_lesson L, cel_content_course C, cel_content_module F WHERE L.courseId=C.id and L.folderId=F.id and L.name LIKE ? AND L.courseId LIKE ? ORDER BY " +
                orderBy, Lesson.class, args, start, rows);
        } else if ((folder != null) && !("".equals(folder))) {
            Object[] args = { condition, folder };

            return super.select(
                "SELECT L.id, L.name,C.name as courseName,F.name as folderName, L.courseId,L.brief, L.public as is_public FROM cel_content_lesson L, cel_content_course C, cel_content_module F WHERE L.courseId=C.id and L.folderId=F.id and L.name LIKE ? AND L.folderId LIKE ? ORDER BY " +
                orderBy, Lesson.class, args, start, rows);
        } else {
            Object[] args = { condition };

            return super.select(
                "SELECT L.id, L.name,C.name as courseName,F.name as folderName, L.courseId,L.brief, L.public as is_public FROM cel_content_lesson L, cel_content_course C, cel_content_module F WHERE L.courseId=C.id and L.folderId=F.id and L.name LIKE ? ORDER BY " +
                orderBy, Lesson.class, args, start, rows);
        }

        //  return super.select("SELECT * FROM cel_content_lesson WHERE name  LIKE ? ORDER BY "+orderBy, Lesson.class, args, start, rows);
    }

    public Collection queryLessonsByFolder(String moduleId, String sort,
        boolean desc, int start, int rows) throws DaoException {
        String orderBy = "";

        if (desc) {
            orderBy += " DESC";
        }

        Object[] args = { moduleId };

        return super.select(
            "SELECT * from cel_content_lesson where public=\"1\" AND folderId=? ORDER BY name " +
            orderBy, Lesson.class, args, start, rows);
    }

    public Collection queryCourseLessons(String name, String course,
        String sort, boolean desc, int start, int rows)
        throws DaoException {
        String condition = (name != null) ? ("%" + name + "%") : "%";
        String orderBy = (sort != null) ? sort : "name";

        if (desc) {
            orderBy += " NAME";
        }

        Object[] args = { condition, course };

        return super.select("SELECT L.id, L.name,C.name as courseName, F.name as folderName, L.courseId,L.brief FROM cel_content_lesson L, cel_content_course C , cel_content_module F WHERE L.courseId=C.id and L.folderId=F.id and L.name LIKE ? AND L.courseId = ? ",
            Lesson.class, args, start, rows);
    }

    public int countCourseLessons(String name, String course)
        throws DaoException {
        String condition = (name != null) ? ("%" + name + "%") : "%";
        Object[] args = { condition, course };

        Collection list = super.select("SELECT COUNT(*) AS total FROM cel_content_lesson WHERE name LIKE ? and courseId=?",
                HashMap.class, args, 0, 1);
        HashMap map = (HashMap) list.iterator().next();

        return Integer.parseInt(map.get("total").toString());
    }

    public int count(String name, String course, String module)
        throws DaoException {
        String condition = (name != null) ? ("%" + name + "%") : "%";

        Collection list;

        if ((course != null) && !("".equals(course)) && (module != null) &&
                !("".equals(module))) {
            Object[] args = { condition, course, module };
            list = super.select("SELECT count(L.id) as total FROM cel_content_lesson L, cel_content_course C, cel_content_module F WHERE L.courseId=C.id and L.folderId=F.id and L.name LIKE ? AND L.courseId LIKE ? AND L.folderId LIKE ?",
                    HashMap.class, args, 0, 1);
        } else if ((course != null) && !("".equals(course))) {
            Object[] args = { condition, course };
            list = super.select("SELECT count(L.id) as total FROM cel_content_lesson L, cel_content_course C, cel_content_module F WHERE L.courseId=C.id and L.folderId=F.id and L.name LIKE ? AND L.courseId LIKE ?",
                    HashMap.class, args, 0, 1);
        } else if ((module != null) && !("".equals(module))) {
            Object[] args = { condition, module };
            list = super.select("SELECT count(L.id) as total FROM cel_content_lesson L, cel_content_course C, cel_content_module F WHERE L.courseId=C.id and L.folderId=F.id and L.name LIKE ? AND L.folderId LIKE ?",
                    HashMap.class, args, 0, 1);
        } else {
            Object[] args = { condition };
            list = super.select("SELECT count(L.id) as total FROM cel_content_lesson L, cel_content_course C, cel_content_module F WHERE L.courseId=C.id and L.folderId=F.id and L.name LIKE ?",
                    HashMap.class, args, 0, 1);
        }

        //  Collection list = super.select("SELECT COUNT(*) AS total FROM cel_content_lesson WHERE name LIKE ?", HashMap.class, args, 0, 1);
        HashMap map = (HashMap) list.iterator().next();

        return Integer.parseInt(map.get("total").toString());
    }

    public int countByFolder(String moduleId) throws DaoException {
        Object[] args = { moduleId };

        Collection list = super.select("SELECT COUNT(*) AS total FROM cel_content_lesson WHERE public=\"1\" AND folderId=?",
                HashMap.class, args, 0, 1);
        HashMap map = (HashMap) list.iterator().next();

        return Integer.parseInt(map.get("total").toString());
    }

    public Lesson lessonExists(String name) throws DaoException {
        Object[] args = { name };
        Collection results = super.select("SELECT id FROM cel_content_lesson WHERE name = ?",
                Lesson.class, args, 0, 1);

        if (results.size() == 0) {
            return null;
        }

        return (Lesson) results.iterator().next();
    }

    public Lesson checkLessonExists(String name,String courseId,String folderId) throws DaoException{

        Object[] args = { name, courseId, folderId };
        Collection results = super.select("SELECT id FROM cel_content_lesson WHERE name = ? and courseId=? and folderId=?",
                Lesson.class, args, 0, 1);

        if (results.size() == 0) {
            return null;
        }

        return (Lesson) results.iterator().next();


    }

     public Lesson checkLessonExistsWithLesson(String name,String courseId,String folderId,String lessonid) throws DaoException{

        Object[] args = { name, courseId, folderId,lessonid };
        Collection results = super.select("SELECT id FROM cel_content_lesson WHERE name = ? and courseId=? and folderId=? AND id !=?",
                Lesson.class, args, 0, 1);

        if (results.size() == 0) {
            return null;
        }

        return (Lesson) results.iterator().next();


    }

    public Lesson lessonExists(String name, String id)
        throws DaoException {
        Object[] args = { name, id };
        Collection results = super.select("SELECT id FROM cel_content_lesson WHERE name = ? AND id <> ?",
                Lesson.class, args, 0, 1);

        if (results.size() == 0) {
            return null;
        }

        return (Lesson) results.iterator().next();
    }

    public Collection getLessons(String courseId) throws DaoException {
        Object[] args = new String[] { courseId };

        return super.select("SELECT id, name as lessonName FROM cel_content_lesson WHERE courseId = ? ORDER BY name",
            Lesson.class, args, 0, -1);
    }

    public Collection getLessonsByModule(String moduleid)
        throws DaoException {
        Object[] args = new String[] { moduleid };

        return super.select("SELECT id, name as lessonName FROM cel_content_lesson WHERE folderId = ? ORDER BY name",
            Lesson.class, args, 0, -1);
    }

    //--- mahe ---
    public Collection getLessonList(String module, String userId)
        throws DaoException {
        //instructor A can query lesson for instructor B
        Object[] args = new String[] { module };

        return super.select("SELECT id, name as lessonName FROM cel_content_lesson WHERE courseId = ? AND (public='1' ) ORDER BY name",
            Lesson.class, args, 0, -1);

        /*Object[] args = new String[] { module,userId };
             return super.select("SELECT id, name as lessonName FROM cel_content_lesson WHERE courseId = ? AND (public=1 OR createdByUser=?) ORDER BY name",Lesson.class,args,0,-1);
        */
    }

    public Collection getLessonList(String module) throws DaoException {
        Object[] args = new String[] { module };

        return super.select("SELECT id, name as lessonName FROM cel_content_lesson WHERE courseId = ? ORDER BY name",
            Lesson.class, args, 0, -1);
    }

    //--- mahe ---
    public void storeFile(Lesson doc) throws InvalidKeyException, DaoException {
        // store uploaded file
        //Lesson doc = (Lesson)contentObject;
        if (doc.getStorageFile() != null) {
            try {
                Application application = Application.getInstance();
                StorageService storage = (StorageService) application.getService(StorageService.class);
                StorageFile file = new StorageFile("/" + getStorageRoot() +
                        "/" + doc.getId() + "/", doc.getStorageFile());
                doc.setStorageFile(file);
                doc.setFilePath(file.getAbsolutePath());
                storage.store(file);
            } catch (Exception e) {
                throw new DaoException("Unable to save uploaded file for " +
                    doc.getId() + ": " + e.toString());
            }
        }

        //int result = super.insert(doc);
        //return result;
    }

    public void setActivationLesson(String id) throws DaoException {
        Object args = new Object[] { id };
        super.update("UPDATE cel_content_lesson SET cel_content_lesson.public='1' WHERE id=?",
            args);
    }

    public void setDeactivationLesson(String id) throws DaoException {
        Object args = new Object[] { id };
        super.update("UPDATE cel_content_lesson SET cel_content_lesson.public='0' WHERE id=?",
            args);
    }

    public Collection getAssessmentByLesson(String id)
        throws DaoException {
        Object args = new Object[] { id };

        return super.select("SELECT id,name FROM cel_content_assessment WHERE lesson_id=? AND cel_content_assessment.public='1' AND (start_date <= date_format( now(),'%Y-%m-%d') AND end_date >= date_format( now(),'%Y-%m-%d')   ) ",
            Assessment.class, args, 0, -1);
    }


    public Lesson checkActive(String id) throws DaoException{


        Collection result= super.select("SELECT id,public as is_public FROM cel_content_lesson WHERE id=?",Lesson.class,new Object[]{id}, 0,1);


        if(result.size()>0)
         return (Lesson) result.iterator().next();
        else
         return null;
    }
}
