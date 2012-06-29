package com.tms.elearning.folder.model;

import com.tms.elearning.course.model.Course;
import com.tms.elearning.course.model.CourseModuleDao;
import com.tms.elearning.course.model.CourseModuleException;
import com.tms.elearning.core.model.LearningContentModule;
import kacang.util.Log;
import kacang.util.UuidGenerator;
import kacang.model.DaoException;
import kacang.model.DataObjectNotFoundException;

import java.util.Collection;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: tirupati
 * Date: Oct 25, 2004
 * Time: 12:00:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class FolderModule extends LearningContentModule {

    Log log = Log.getLog(getClass());

    public boolean addFolder(Folder folder) {
        // generate ID
        String uuid = UuidGenerator.getInstance().getUuid();
        folder.setId(uuid);

        // call dao to save
        FolderModuleDao dao = (FolderModuleDao)getDao();
        try {
            log.debug("Inserting Modules " + folder.getFolderName());
            dao.insert(folder);
            return true;
        } catch (DaoException e) {
            log.error("Error adding Modules " + e.toString(), e);
            return false;
        }
    }

    public boolean updateFolder(Folder folder) {
        // call dao to save
        FolderModuleDao dao = (FolderModuleDao)getDao();
        try {
            log.debug("Updating Modules " + folder.getFolderName());
            dao.update(folder);
            return true;
        } catch (DaoException e) {
            log.error("Error updating Modules " + e.toString(), e);
            return false;
        }
    }

    public boolean deleteFolder(String id) {
        // call dao to delete
        FolderModuleDao dao = (FolderModuleDao)getDao();
        try {
            dao.delete(id);
            return true;
        } catch (DaoException e) {
            log.error("Error delete Modules " + e.toString(), e);
            return false;
        }
    }

    public Folder loadFolder(String id) throws DataObjectNotFoundException {
        FolderModuleDao dao = (FolderModuleDao)getDao();
        try {
            return dao.load(id);
        } catch (DaoException e) {
            log.error("Error loading Modules " + id, e);
            throw new FolderModuleException(e.toString());
        }
    }

    public Collection findFolders(String name, String course,String userId,String sort, boolean desc, int start, int rows){
        FolderModuleDao dao = (FolderModuleDao)getDao();

        try {
            
            return dao.query(name,course,userId, sort, desc, start, rows);
        } catch (DaoException e) {
            log.error("Error finding Modules " + e.toString(), e);
            throw new FolderModuleException(e.toString());
        }
    }


      public Collection findFolders(String courseId , boolean desc, int start, int rows){
        FolderModuleDao dao = (FolderModuleDao)getDao();

        try {

            return dao.queryModuleByCourse(courseId, desc, start, rows);
        } catch (DaoException e) {
            log.error("Error finding Modules " + e.toString(), e);
            throw new FolderModuleException(e.toString());
        }
    }













    public int countFolders(String name,String course) {

        FolderModuleDao dao = (FolderModuleDao)getDao();
        try {
            return dao.count(name,course);
        } catch (DaoException e) {
            log.error("Error counting Modules " + e.toString(), e);
            throw new FolderModuleException(e.toString());
        }
    }


    public int countFoldersByCourse(String courseId) {

        FolderModuleDao dao = (FolderModuleDao)getDao();
        try {
            return dao.countByCourse(courseId);
        } catch (DaoException e) {
            log.error("Error counting Modules " + e.toString(), e);
            throw new FolderModuleException(e.toString());
        }
    }

    public int countFolders(String course) {

        FolderModuleDao dao = (FolderModuleDao)getDao();
        try {
            return dao.count(course,1);
        } catch (DaoException e) {
            log.error("Error counting Modules " + e.toString(), e);
            throw new FolderModuleException(e.toString());
        }
    }
    public Folder folderExists(String name) {
        FolderModuleDao dao = (FolderModuleDao)getDao();
        try {
            return dao.folderExists(name);
        } catch (DaoException e)  {
            log.error("Error finding module " + e.toString(), e);
            throw new FolderModuleException(e.toString());
        }
    }

    public Folder checkFolderExits(String name,String courseid){
        FolderModuleDao dao = (FolderModuleDao)getDao();
        try {
            return dao.checkFolderExits(name,courseid);
        } catch (DaoException e)  {
            log.error("The same module is assigned to same course " + e.toString(), e);
            throw new FolderModuleException(e.toString());
        }

    }


     public Folder checkFolderExitsModuleId(String name,String courseid,String moduleid){
        FolderModuleDao dao = (FolderModuleDao)getDao();
        try {
            return dao.checkFolderExitsModuleId(name,courseid,moduleid);
        } catch (DaoException e)  {
            log.error("The same module is assigned to same course " + e.toString(), e);
            throw new FolderModuleException(e.toString());
        }

    }


    public Folder folderExists(String name, String id) {
        FolderModuleDao dao = (FolderModuleDao)getDao();
        try {
            return dao.folderExists(name, id);
        } catch (DaoException e)  {
            log.error("Error finding module " + e.toString(), e);
            throw new CourseModuleException(e.toString());
        }
    }


    //for table filter

    public Collection loadCourse(){


        FolderModuleDao dao = (FolderModuleDao)getDao();

        try {

            return dao.loadCourse();
        } catch (DaoException e) {
            log.error("Error loading courses " + e.toString(), e);
            throw new FolderModuleException(e.toString());
        }
    }



    public void setActivationModule(String id){

        FolderModuleDao dao = (FolderModuleDao) getDao();

        try {
            dao.setActivationModule(id);
        } catch (DaoException e) {
            Log.getLog(getClass()).warn("error set flag for activation in cel_content_module");
        }


    }

   public void setDeactivationModule(String id){

       FolderModuleDao dao = (FolderModuleDao) getDao();
       try {
           dao.setDeactivationModule(id);
       } catch (DaoException e) {
           Log.getLog(getClass()).warn("error reverse flag for activation in cel_content_module");
       }
   }


   public Folder getIntroduction(String id){
       FolderModuleDao dao = (FolderModuleDao) getDao();
       try{
           return dao.getIntroduction(id);

       } catch (DaoException e){
           Log.getLog(getClass()).warn("cannot get introduction for this module");
       }



      return null;
   }


}
