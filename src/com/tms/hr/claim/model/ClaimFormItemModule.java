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
import java.util.Iterator;
import java.util.Date;
import java.math.BigDecimal;
import java.text.DecimalFormat;


public class ClaimFormItemModule extends DefaultModule {
    Log log = Log.getLog(getClass());

    public boolean addObject(ClaimFormItem obj) {
        ClaimFormItemDao dao = (ClaimFormItemDao) getDao();

        try {
            Application application = Application.getInstance();
            ClaimFormIndexModule moduleForm = (ClaimFormIndexModule) application.getModule(ClaimFormIndexModule.class);
            ClaimFormIndex formObj = moduleForm.selectObject(obj.getFormId());

            /// check the status of the formObj
            if (!formObj.getState().equals(ClaimFormIndexModule.STATE_CREATED)) {
                return false;
            }


            if( formObj.getAmount().add(obj.getAmount()) !=null &&(formObj.getAmount().add(obj.getAmount()).longValue() < 999999999) && (formObj.getAmount().add(obj.getAmount()).longValue() >-999999999)==true)
            {
            dao.insertRecord(obj);


            formObj.setAmount(formObj.getAmount().add(obj.getAmount()));



            if (formObj.getApprovedBy() == null) {
                formObj.setApprovedBy("");
            }

            moduleForm.updateObject(formObj);

            return true;
            }//if less than 999999999


            return false;
        } catch (DaoException ex) {
            log.error("Error adding ClaimFormItem " + ex.toString(), ex);

            return false;
        }
    }

    public boolean updateObject(ClaimFormItem obj) {
        ClaimFormItemDao dao = (ClaimFormItemDao) getDao();

        try {
            dao.updateRecord(obj);

            return true;
        } catch (DaoException ex) {
            log.error("Error updating ClaimFormItem " + ex.toString(), ex);

            return false;
        }
    }

    public ClaimFormItem selectObject(String id) {
        ClaimFormItemDao dao = (ClaimFormItemDao) getDao();

        try {
            ClaimFormItem obj = dao.selectRecord(id);
            Application application = Application.getInstance();

            ClaimFormItemCategoryModule moduleCFIC = (ClaimFormItemCategoryModule) application.getModule(ClaimFormItemCategoryModule.class);
            obj.setClaimFormItemCategory(moduleCFIC.selectObject(
                    obj.getCategoryId()));

            ClaimProjectModule moduleProject = (ClaimProjectModule) application.getModule(ClaimProjectModule.class);
            obj.setClaimProject(moduleProject.selectObject(obj.getProjectId()));

            ClaimStandardTypeModule moduleCST = (ClaimStandardTypeModule) application.getModule(ClaimStandardTypeModule.class);
            obj.setClaimStandardType(moduleCST.selectObject(
                    obj.getStandardTypeId()));

            return obj;
        } catch (Exception ex) {
            log.error("Error getting Product " + ex.toString(), ex);

            return null;
        }
    }

    public Collection selectObjects(String field, String value, int start,
                                    int number) {
        ClaimFormItemDao dao = (ClaimFormItemDao) getDao();

        try {
            return fnAddChildObjects(dao.selectRecords(field, value, start,
                    number));
        } catch (DaoException ex) {
            log.error("Error selecting objects .. " + ex.toString(), ex);

            return null;
        }
    }

    public Collection findObjects(String keyword,String[] conditions, String sort,
                                  boolean desc, int start, int rows) {
        ClaimFormItemDao dao = (ClaimFormItemDao) getDao();

        try{
        if(sort.equals("") || sort ==null)
        	sort="timeFrom";
        }
        catch(java.lang.NullPointerException e){
        	sort="timeFrom";
        }
        if(sort.equals("timeFromStr"))
        	sort ="timeFrom";
        if(sort.equals("categoryName"))
        	sort ="categoryId";
        if(sort.equals("standardTypeName"))
        	sort ="standardTypeId";
        if(sort.equals("projectName"))
        	sort ="projectId";
        if(sort.equals("amountInStr"))
        	sort="amount";
        
        
        
        try {
            Collection colObj = dao.query(keyword,conditions, sort, desc, start, rows);

            return fnAddChildObjects(colObj);
        } catch (DaoException e) {
            log.error("Error finding contacts " + e.toString(), e);
            throw new ClaimFormIndexException(e.toString());
        }
    }

    public int countObjects(String keyword,String[] conditions) {
        ClaimFormItemDao dao = (ClaimFormItemDao) getDao();

        try {
            return dao.count(keyword,conditions);
        } catch (DaoException e) {
            log.error("Error finding contacts " + e.toString(), e);
            throw new ClaimFormItemException(e.toString());
        }
    }

    public Collection findObjects(String name, String sort, boolean desc,
                                  int start, int rows) {
        ClaimFormItemDao dao = (ClaimFormItemDao) getDao();

        try {
            return fnAddChildObjects(dao.query(name, sort, desc, start, rows));
        } catch (DaoException e) {
            log.error("Error finding contacts " + e.toString(), e);
            throw new ClaimFormItemException(e.toString());
        }
    }

    protected Collection fnAddChildObjects(Collection colObj) {
        Application application = Application.getInstance();
        Iterator itr = colObj.iterator();

        while (itr.hasNext()) {
            ClaimFormItem obj = (ClaimFormItem) itr.next();
            String temp=    new DecimalFormat("0.00").format(obj.getAmount());
                 if(temp.charAt(0) == '-')
                  obj.setAmountInStr("("+temp.substring(1,temp.length()) +")");
                 else
                  obj.setAmountInStr(obj.getAmount().toString());


            ClaimFormItemCategoryModule moduleCFIC = (ClaimFormItemCategoryModule) application.getModule(ClaimFormItemCategoryModule.class);
            ClaimFormItemCategory categoryItem = moduleCFIC.selectObject(obj.getCategoryId());

            if (categoryItem == null) {
                categoryItem = new ClaimFormItemCategory();
                categoryItem.setId(obj.getCategoryId());
                categoryItem.setName(obj.getCategoryId());
            }

            obj.setClaimFormItemCategory(categoryItem);

            ClaimProjectModule moduleProject = (ClaimProjectModule) application.getModule(ClaimProjectModule.class);
            obj.setClaimProject(moduleProject.selectObject(obj.getProjectId()));

            ClaimStandardTypeModule moduleCST = (ClaimStandardTypeModule) application.getModule(ClaimStandardTypeModule.class);

            ClaimStandardType object = new ClaimStandardType();

            if (moduleCST.selectObject(obj.getStandardTypeId()) == null) {
                object.setName(obj.getStandardTypeId());
                object.setCode(obj.getStandardTypeId());
                object.setClaimStandardTypeID(obj.getStandardTypeId());

                obj.setClaimStandardType(object);
            } else {
                obj.setClaimStandardType(moduleCST.selectObject(
                        obj.getStandardTypeId()));
            }
        }

        return colObj;
    }

    public boolean setState(String id, String status) {
        ClaimFormItemDao dao = (ClaimFormItemDao) getDao();

        try {
            ClaimFormItem obj = selectObject(id);
            obj.setState(status);
            updateObject(obj);

            return true;
        } catch (Exception ex) {
            log.error("Error deleting object " + ex.toString(), ex);

            return false;
        }
    }

    /*
       public int countObjects(String status) {
          ClaimFormItemDao dao = (ClaimFormItemDao) getDao();
          try {
             return dao.count(String status);
          } catch (DaoException e) {
             log.error("Error counting ContactType " + e.toString(), e);
             return(0);
          }
       }
    */
    public int countObjects(String status) {
        ClaimFormItemDao dao = (ClaimFormItemDao) getDao();

        try {
            return dao.count(status);
        } catch (DaoException e) {
            log.error("Error finding contacts " + e.toString(), e);
            throw new ClaimFormItemException(e.toString());
        }
    }

    public int countObjects(String field, String value) {
        ClaimFormItemDao dao = (ClaimFormItemDao) getDao();

        try {
            return dao.count(field, value);
        } catch (DaoException e) {
            log.error("Error finding contacts " + e.toString(), e);
            throw new ClaimFormItemException(e.toString());
        }
    }

    public boolean deleteObject(String id) {
        ClaimFormItemDao dao = (ClaimFormItemDao) getDao();

        try {
            Application application = Application.getInstance();
            ClaimFormIndexModule moduleForm = (ClaimFormIndexModule) application.getModule(ClaimFormIndexModule.class);
            ClaimFormItem cfi = selectObject(id);
            ClaimFormIndex formObj = moduleForm.selectObject(cfi.getFormId());
            dao.deleteRecord(id);
            try{
            formObj.setAmount(formObj.getAmount().subtract(cfi.getAmount()));
            }
            catch(Exception e){
                formObj.setAmount(new BigDecimal(0));
            }
            moduleForm.updateObject(formObj);

            return true;
        } catch (DaoException ex) {
            log.error("Error deleting object " + ex.toString(), ex);

            return false;
        }
    }

    // add for reporting purposes
    public double getTotalByMonthAndCategory(String userId, String categoryId,
                                             String type, String month) {
        double dbl = 0.0;

        try {
            ClaimFormItemDao dao = (ClaimFormItemDao) getDao();
            dbl = dao.selectTotalByMonthAndCategory(userId, categoryId, type,
                    month);
        } catch (Exception e) {
            Log.getLog(getClass()).error("getTotalByMonthAndCategory:", e);
        }

        return dbl;
    }

    public double getTotalByMonthAndCategory(String userId, String categoryId,
                                             String month) {
        double dbl = 0.0;

        try {
            ClaimFormItemDao dao = (ClaimFormItemDao) getDao();
            dbl = dao.selectTotalByMonthAndCategory(userId, categoryId, month);
        } catch (Exception e) {
            Log.getLog(getClass()).error("getTotalByMonthAndCategory:", e);
        }

        return dbl;
    }

    public ClaimFormItem retriveDraftItem(String id){

        try{
            ClaimFormItemDao dao = (ClaimFormItemDao) getDao();
            return dao.retriveDraftItem(id);

        } catch(Exception e){
            Log.getLog(getClass()).error("cannot retrieve draft item from claim_form_item");
        }
         return null;
    }


     public boolean checkSameDayDiffName(Date thatDay, String remarks, String categoryId){

         ClaimFormItemDao dao = (ClaimFormItemDao) getDao();
         try{
         return dao.checkSameDayDiffName(thatDay,remarks, categoryId);
         }
         catch(DaoException e){
             Log.getLog(getClass()).warn("cannot check same item on same day exist or not");
         }
        return true;
     }

     
     public boolean checkSameDayDiffNameOther(Date thatDay, String remarks, String categoryId, String id){

         ClaimFormItemDao dao = (ClaimFormItemDao) getDao();
         try{
         return dao.checkSameDayDiffNameOther(thatDay,remarks, categoryId, id);
         }
         catch(DaoException e){
             Log.getLog(getClass()).warn("cannot check same item on same day exist or not");
         }
        return true;
     }

     public boolean checkSameDayDiffName2(Date thatDay, String remarks){

         ClaimFormItemDao dao = (ClaimFormItemDao) getDao();
         try{
         return dao.checkSameDayDiffName2(thatDay,remarks);
         }
         catch(DaoException e){
             Log.getLog(getClass()).warn("cannot check same item on same day exist or not");
         }
        return true;
     }
     
}
