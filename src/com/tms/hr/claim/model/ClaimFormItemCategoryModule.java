/**
 * @author Vincent Lee, Wavelet Solutions Sdn Bhd (632468-W)
 * Created On: 2004-06-17
 * Copyright of The Media Shoppe Berhad
 */
package com.tms.hr.claim.model;

import kacang.Application;

import kacang.model.DaoException;
import kacang.model.DefaultModule;

import kacang.util.Log;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;


public class ClaimFormItemCategoryModule extends DefaultModule {
    public static final String DEFAULTID = "default";
    public static final String TRAVEL_MILEAGE = "travel-mileage";
    public static final String TRAVEL_TOLL = "travel-toll";
    public static final String TRAVEL_PARKING = "travel-parking";
    public static final String TRAVEL_ALLOWANCE = "travel-allowance";
    public static final String STATUS_ACTIVE = "act";
    public static final String STATUS_INACTIVE = "ina";
    public static final String STATE_ACTIVE = "act";
    public static final String STATE_INACTIVE = "ina";
    Log log = Log.getLog(getClass());

    public boolean addObject(ClaimFormItemCategory obj) {
        ClaimFormItemCategoryDao dao = (ClaimFormItemCategoryDao) getDao();

        try {
            dao.insertRecord(obj);

            return true;
        } catch (DaoException ex) {
            log.error("Error adding ClaimFormItemCategory " + ex.toString(), ex);

            return false;
        }
    }

    public boolean updateObject(ClaimFormItemCategory obj) {
        ClaimFormItemCategoryDao dao = (ClaimFormItemCategoryDao) getDao();

        try {
            dao.updateRecord(obj);

            return true;
        } catch (DaoException ex) {
            log.error("Error updating ClaimFormItemCategory " + ex.toString(),
                ex);

            return false;
        }
    }

    public ClaimFormItemCategory selectObject(String id) {
        ClaimFormItemCategoryDao dao = (ClaimFormItemCategoryDao) getDao();

        try {
            return dao.selectRecord(id);
        } catch (Exception ex) {
            log.error("Error getting Product " + ex.toString(), ex);

            return null;
        }
    }

    public Collection selectObjects(String field, String value, int start,
        int number) {
        ClaimFormItemCategoryDao dao = (ClaimFormItemCategoryDao) getDao();

        try {
            return dao.selectRecords(field, value, start, number, null, false);
        } catch (DaoException ex) {
            log.error("Error selecting objects .. " + ex.toString(), ex);

            return null;
        }
    }


    public Collection selectObjectsIgnoreDefault(String field, String value, int start,
        int number) {
        ClaimFormItemCategoryDao dao = (ClaimFormItemCategoryDao) getDao();

        try {
            return dao.selectRecordsIgnoreDefault(field, value, start, number, null, false);
        } catch (DaoException ex) {
            log.error("Error selecting objects .. " + ex.toString(), ex);

            return null;
        }
    }

    public Collection selectObjects(String field, String value, int start,
        int number, String sort, boolean desc) {
        ClaimFormItemCategoryDao dao = (ClaimFormItemCategoryDao) getDao();

        try {
            return dao.selectRecords(field, value, start, number, sort, desc);
        } catch (DaoException ex) {
            log.error("Error selecting objects .. " + ex.toString(), ex);

            return null;
        }
    }

    public Collection selectObjects(String keyword,String field, String value, String type,
        int start, int number, String sort, boolean desc) {
        ClaimFormItemCategoryDao dao = (ClaimFormItemCategoryDao) getDao();

        try {
            return dao.selectRecords(keyword,field, value, type, start, number, sort,
                desc);
        } catch (DaoException ex) {
            log.error("Error selecting objects .. " + ex.toString(), ex);

            return null;
        }
    }

    public Collection findObjects(String name, String sort, boolean desc,
        int start, int rows) {
        ClaimFormItemCategoryDao dao = (ClaimFormItemCategoryDao) getDao();

        try {
            return dao.query(name, sort, desc, start, rows);
        } catch (DaoException e) {
            log.error("Error finding contacts " + e.toString(), e);
            throw new ClaimFormItemCategoryException(e.toString());
        }
    }

    /*
       public int countObjects(String status) {
          ClaimFormItemCategoryDao dao = (ClaimFormItemCategoryDao) getDao();
          try {
             return dao.count(String status);
          } catch (DaoException e) {
             log.error("Error counting ContactType " + e.toString(), e);
             return(0);
          }
       }
    */
    public int countObjects(String status) {
        ClaimFormItemCategoryDao dao = (ClaimFormItemCategoryDao) getDao();

        try {
            return dao.count(status);
        } catch (DaoException e) {
            log.error("Error finding contacts " + e.toString(), e);
            throw new ClaimFormItemCategoryException(e.toString());
        }
    }

    public int countObjects(String keyword,String status, String type) {
        ClaimFormItemCategoryDao dao = (ClaimFormItemCategoryDao) getDao();

        try {
            return dao.count(keyword,status, type);
        } catch (DaoException e) {
            log.error("Error finding contacts " + e.toString(), e);
            throw new ClaimFormItemCategoryException(e.toString());
        }
    }

    public boolean deleteObject(String id) {
        ClaimFormItemCategoryDao dao = (ClaimFormItemCategoryDao) getDao();

        try {
            if (id.equals(DEFAULTID) || id.equals(TRAVEL_MILEAGE) ||
                    id.equals(TRAVEL_TOLL) || id.equals(TRAVEL_PARKING)) {
                return false;
            }

            Application application = Application.getInstance();
            ClaimFormItemModule module = (ClaimFormItemModule) application.getModule(ClaimFormItemModule.class);

            int count = module.countObjects(null,new String[] {
                        "categoryId = '" + id + "' "
                    });

            if (count == 0) {
                dao.deleteRecord(id);
            }

            return true;
        } catch (DaoException ex) {
            log.error("Error deleting object " + ex.toString(), ex);

            return false;
        }
    }

    public Collection query(String[] conditions, String orderBy) {
        try {
            ClaimFormItemCategoryDao dao = (ClaimFormItemCategoryDao) getDao();

            return dao.query(conditions, orderBy);
        } catch (Exception e) {
            Log.getLog(getClass()).error(e);
        }

        return null;
    }

    public Collection countTotalType() {
        try {
            ClaimFormItemCategoryDao dao = (ClaimFormItemCategoryDao) getDao();

            return dao.countTotalType();
        } catch (Exception e) {
            Log.getLog(getClass()).error(e);
        }

        return null;
    }

    public void addDependency(String categoryId, String departmentCode) {
        try {
            ClaimFormItemCategoryDao dao = (ClaimFormItemCategoryDao) getDao();

            if (!dao.dependencyExisted(categoryId, departmentCode)) {
                dao.insertDependencies(categoryId, departmentCode);
            }
        } catch (Exception e) {
        }
    }

    public boolean dependencyExisted(String categoryId, String departmentCode) {
        try {
            ClaimFormItemCategoryDao dao = (ClaimFormItemCategoryDao) getDao();

            return dao.dependencyExisted(categoryId, departmentCode);
        } catch (Exception e) {
        }

        return false;
    }

    public void deleteDependency(String categoryId, String departmentCode) {
        try {
            ClaimFormItemCategoryDao dao = (ClaimFormItemCategoryDao) getDao();
            dao.deleteDependencies(categoryId, departmentCode);
        } catch (Exception e) {
        }
    }

    public String[] selectDependencies(String categoryId) {
        String[] sRet = {  };

        try {
            ClaimFormItemCategoryDao dao = (ClaimFormItemCategoryDao) getDao();
            Collection c = dao.selectDependencies(categoryId);

            if ((c != null) && (c.size() > 0)) {
                sRet = new String[c.size()];

                int iCounter = 0;

                for (Iterator i = c.iterator(); i.hasNext();) {
                    HashMap map = (HashMap) i.next();
                    sRet[iCounter] = map.get("departmentCode").toString();
                    iCounter++;
                }
            }
        } catch (Exception e) {
            Log.getLog(getClass()).error(e);
        }

        return sRet;
    }
}
