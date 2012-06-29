package com.tms.elearning.coursecategory.model;

/**
 * Created by IntelliJ IDEA.
 * User: vivek
 * Date: Feb 25, 2005
 * Time: 3:05:32 PM
 * To change this template use File | Settings | File Templates.
 */

import kacang.model.DefaultModule;
import kacang.model.DaoException;
import kacang.model.DataObjectNotFoundException;
import kacang.util.Log;
import kacang.util.UuidGenerator;
import com.tms.elearning.coursecategory.model.Category;
import com.tms.elearning.coursecategory.model.CategoryModuleDao;
import com.tms.elearning.coursecategory.model.CategoryModuleException;

import java.util.Collection;


public class CategoryModule extends DefaultModule{

       Log log = Log.getLog(getClass());

    public boolean addCategory(Category category) {
        // generate ID
        String uuid = UuidGenerator.getInstance().getUuid();
        category.setId(uuid);

        // call dao to save
        CategoryModuleDao dao = (CategoryModuleDao)getDao();
        try {
            log.debug("Inserting Category " + category.getCategory());
            dao.insert(category);
            return true;
        } catch (DaoException e) {
            log.error("Error adding category " + e.toString(), e);
            return false;
        }
    }


    public boolean editCategory(Category category) {


            // call dao to save
            CategoryModuleDao dao = (CategoryModuleDao)getDao();
            try {

                dao.update(category);
                return true;
            } catch (DaoException e) {
                log.error("Error updating category " + e.toString(), e);
                return false;
            }
        }








    public boolean deleteCourse(String id) {
        // call dao to delete
        CategoryModuleDao dao = (CategoryModuleDao)getDao();
        try {
            dao.delete(id);
            return true;
        } catch (DaoException e) {
            log.error("Error delete category " + e.toString(), e);
            return false;
        }
    }

    public Category loadCategory(String id) throws DataObjectNotFoundException {
        CategoryModuleDao dao = (CategoryModuleDao)getDao();
        try {
            return dao.load(id);
        } catch (DaoException e) {
            log.error("Error loading category " + id, e);
            throw new CategoryModuleException(e.toString());
        }
    }

    public Collection findCategory(String category,String userId, String sort, boolean desc, int start, int rows){
        CategoryModuleDao dao = (CategoryModuleDao)getDao();

        try {
            return dao.query(category,userId, sort, desc, start, rows);
        } catch (DaoException e) {
            log.error("Error finding category " + e.toString(), e);
            throw new CategoryModuleException(e.toString());
        }
    }







    public int countCategory(String category) {
       CategoryModuleDao dao = (CategoryModuleDao)getDao();
       try {
           return dao.countList(category);
       } catch (DaoException e) {
           log.error("Error counting in category " + e.toString(), e);
           throw new CategoryModuleException(e.toString());
       }
   }

   public Collection loadCategories()  {
        CategoryModuleDao dao = (CategoryModuleDao)getDao();
       try {
           return dao.getCategories();
       } catch (DaoException e) {
           log.error("Error counting in category " + e.toString(), e);
           throw new CategoryModuleException(e.toString());
       }
   }
}
