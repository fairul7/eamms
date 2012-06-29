package com.tms.hr.claim.model;

import java.util.Collection;

import kacang.model.DaoException;
import kacang.model.DaoQuery;
import kacang.model.DefaultModule;
import kacang.util.Log;


/**
 * Created by IntelliJ IDEA.
 * User: cheewei
 * Date: Dec 8, 2005
 * Time: 12:24:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class ClaimConfigTypeModule extends DefaultModule {
    public void insertType(ClaimTypeObject claimtypeobject) {
        ClaimConfigTypeDao dao = (ClaimConfigTypeDao) getDao();

        try {
            dao.insertType(claimtypeobject);
        } catch (DaoException e) {
            Log.getLog(getClass()).warn("cannot insert new type into claim_form_type ");
        }
    }

    public Collection retrieveAllType(String keyword,String sort, boolean desc, int start,
        int rows) {
        ClaimConfigTypeDao dao = (ClaimConfigTypeDao) getDao();

        try {
            return dao.retrieveAllType(keyword, sort, desc, start, rows);
        } catch (DaoException e) {
            Log.getLog(getClass()).warn("cannot retrieve type from claim_form_type to populate data in table ");
        }

        return null;
    }

    public int countRetrieveAllType(String keyword) {
        ClaimConfigTypeDao dao = (ClaimConfigTypeDao) getDao();

        try {
            return dao.countCourseLessons(keyword);
        } catch (DaoException e) {
            Log.getLog(getClass()).warn("cannot COUNT type from claim_form_type to populate date in table");
            throw new ClaimConfigException(e.toString());
        }
    }

    public Collection retrieveAllTypeDepartment() {
        ClaimConfigTypeDao dao = (ClaimConfigTypeDao) getDao();

        try {
            return dao.retrieveAllTypeDepartment();
        } catch (DaoException e) {
            Log.getLog(getClass()).warn("cannot retrieve type dept from claim_form_typedept");
            throw new ClaimConfigException(e.toString());
        }
    }

    public ClaimTypeObject selectTypeName(String typeid) {
        ClaimConfigTypeDao dao = (ClaimConfigTypeDao) getDao();

        try {
            return dao.selectTypeName(typeid);
        } catch (DaoException e) {
            Log.getLog(getClass()).warn("cannot retrieve type's name from claim_form_type");
        }

        return null;
    }

    public void deleteType(String id) {
        ClaimConfigTypeDao dao = (ClaimConfigTypeDao) getDao();

        try {
            dao.deleteType(id);
        } catch (DaoException e) {
            Log.getLog(getClass()).warn("cannot delete type in claim_form_type");
        }
    }

    public void editType(String name, String id, String accountcode) {
        ClaimConfigTypeDao dao = (ClaimConfigTypeDao) getDao();

        try {
            dao.editType(name, id, accountcode);
        } catch (DaoException e) {
            Log.getLog(getClass()).warn("cannnot edit claim_form_type");
        }
    }

    public Collection retrieveAllType() {
        ClaimConfigTypeDao dao = (ClaimConfigTypeDao) getDao();

        try {
            return dao.retrivesAllType();
        } catch (DaoException e) {
            Log.getLog(getClass()).warn("cannot retrieve type from claim_form_type");
        }

        return null;
    }

    public Collection retrieveTypeDepartment(String sort, boolean desc,
        int start, int rows) {
        ClaimConfigTypeDao dao = (ClaimConfigTypeDao) getDao();

        try {
            return dao.retrieveAllTypeDepartment(null,sort, desc, start, rows);
        } catch (DaoException e) {
            Log.getLog(getClass()).warn("cannot retrieve type from claim_form_type to populate data in table ");
        }

        return null;
    }

    public Collection retrieveAllTypeDepartment(String keyword, String sort, boolean desc,
        int start, int rows) {
        ClaimConfigTypeDao dao = (ClaimConfigTypeDao) getDao();

        try {
            return dao.retrieveAllTypeDepartment(keyword, sort, desc, start, rows);
        } catch (DaoException e) {
            Log.getLog(getClass()).warn("cannot retrieve type from claim_form_typedept to populate data in table ");
        }

        return null;
    }

    public int countAllTypeDepart(String keyword) {
        ClaimConfigTypeDao dao = (ClaimConfigTypeDao) getDao();

        try {
            return dao.countAllTypeDepart(keyword);
        } catch (DaoException e) {
            Log.getLog(getClass()).warn("cannot count typedepart in claim_form_typedept ");
            throw new ClaimConfigException(e.toString());
        }
    }

    public void addTypeDepart(ClaimTypeDepartObject object) {
        ClaimConfigTypeDao dao = (ClaimConfigTypeDao) getDao();

        try {
            dao.addTypeDepart(object);
        } catch (DaoException e) {
            Log.getLog(getClass()).warn("cannot add object into claim_form_typedept table");
        }
    }

    public void deleteTypeDepart(String typeid) {
        ClaimConfigTypeDao dao = (ClaimConfigTypeDao) getDao();

        try {
            dao.deleteTypeDepart(typeid);
        } catch (DaoException e) {
            Log.getLog(getClass()).warn("cannot delete object from claim_form_typedept table");
        }
    }

    public ClaimTypeObject retrieveType(String id) {
        ClaimConfigTypeDao dao = (ClaimConfigTypeDao) getDao();

        try {
            return dao.retrieveType(id);
        } catch (DaoException e) {
            Log.getLog(getClass()).warn("cannot access claim_form_type");
        }

        return null;
    }

    public ClaimTypeDepartObject selectDepartmentType(String typeid) {
        ClaimConfigTypeDao dao = (ClaimConfigTypeDao) getDao();

        try {
            return dao.selectDepartmentType(typeid);
        } catch (DaoException e) {
            Log.getLog(getClass()).warn("cannot retrieve department type");
        }

        return null;
    }

    public void updateTypeDepart(ClaimTypeDepartObject object) {
        ClaimConfigTypeDao dao = (ClaimConfigTypeDao) getDao();

        try {
            dao.updateTypeDepart(object);
        } catch (DaoException e) {
            Log.getLog(getClass()).warn("cannot update table for claim_form_typedept");
        }
    }

    public void delTypeDepart(String typeid) {
        ClaimConfigTypeDao dao = (ClaimConfigTypeDao) getDao();

        try {
            dao.deleteTypeDepart(typeid);
        } catch (DaoException e) {
            Log.getLog(getClass()).warn("cannot delete object from claim_form_typedept");
        }
    }
    
    public boolean isUniqueType(String type, String id) {	      
    	ClaimConfigTypeDao dao = (ClaimConfigTypeDao) getDao();
    	
        try {
            return dao.isUniqueType(type, id);
        } catch (DaoException e) {
        	Log.getLog(getClass()).error("Error verifying uniqueness of type " + e);
           return false;
        }
    }

    public boolean isUniqueTypeCode(String code, String id) {	      
    	ClaimConfigTypeDao dao = (ClaimConfigTypeDao) getDao();
    	
        try {
            return dao.isUniqueTypeCode(code, id);
        } catch (DaoException e) {
        	Log.getLog(getClass()).error("Error verifying uniqueness of type code " + e);
           return false;
        }
    }
}
