/**
 * @author Vincent Lee, Wavelet Solutions Sdn Bhd (632468-W)
 * Created On: 2004-06-17
 * Copyright of The Media Shoppe Berhad
 */
package com.tms.hr.claim.model;

import java.lang.*;
import java.util.*;

import kacang.*;
import kacang.ui.Event;
import kacang.model.*;
import kacang.util.Log;
import kacang.services.security.*;
import kacang.services.security.SecurityException;
import kacang.services.*;


public class ClaimFormIndexModule extends DefaultModule {
    Log log = Log.getLog(getClass());

    public static String STATE_CREATED = "cre";
    public static String STATE_SUBMITTED = "sub";
    public static String STATE_REJECTED = "rej";
    public static String STATE_APPROVED = "app";
    public static String STATE_ASSESSED = "ass";
    public static String STATE_CLOSED = "clo";


    public boolean addObject(ClaimFormIndex obj) {
        ClaimFormIndexDao dao = (ClaimFormIndexDao) getDao();
        try {
            dao.insertRecord(obj);
            return true;
        } catch (DaoException ex) {
            log.error("Error adding ClaimFormIndex " + ex.toString(), ex);
            return false;
        }
    }

    public boolean updateObject(ClaimFormIndex obj) {
        ClaimFormIndexDao dao = (ClaimFormIndexDao) getDao();
        try {
            dao.updateRecord(obj);
            return true;
        } catch (DaoException ex) {
            log.error("Error updating ClaimFormIndex " + ex.toString(), ex);
            return false;
        }

    }


    public boolean updateObjectApprover1(ClaimFormIndex obj){

       ClaimFormIndexDao dao = (ClaimFormIndexDao) getDao();
        try {
            dao.updateObjectApprover1(obj);
            return true;
        } catch (DaoException ex) {
            log.error("Error updating ClaimFormIndex " + ex.toString(), ex);
            return false;
        }

    }


     public boolean updateObjectApprover2(ClaimFormIndex obj){

       ClaimFormIndexDao dao = (ClaimFormIndexDao) getDao();
        try {
            dao.updateObjectApprover2(obj);
            return true;
        } catch (DaoException ex) {
            log.error("Error updating ClaimFormIndex " + ex.toString(), ex);
            return false;
        }

    }


    public ClaimFormIndex selectObject(String id) {
        ClaimFormIndexDao dao = (ClaimFormIndexDao) getDao();
        try {
            return fnAddChildObjects(dao.selectRecord(id));
        } catch (Exception ex) {
            log.error("Error getting Product " + ex.toString(), ex);
            return null;
        }
    }


    public Date retrieveApprover1Date(String id){

        ClaimFormIndexDao dao = (ClaimFormIndexDao) getDao();
        try{
            return dao.retrieveApprover1Date(id);
        }
        catch(DaoException e){
          log.warn("cannot retrieve approver 1 date from table");
         }

     return null;

    }

    public Date retrieveApprover2Date(String id){

           ClaimFormIndexDao dao = (ClaimFormIndexDao) getDao();
           try{
               return dao.retrieveApprover2Date(id);
           }
           catch(DaoException e){
             log.warn("cannot retrieve approver 1 date from table");
            }

        return null;

       }






    public Collection selectObjects(String field, String value, int start, int number) {
        ClaimFormIndexDao dao = (ClaimFormIndexDao) getDao();
        try {
            Collection colObj = dao.selectRecords(field, value, start, number);
            Iterator itr = colObj.iterator();

            while (itr.hasNext()) {
                ClaimFormIndex obj = (ClaimFormIndex) itr.next();
                obj = fnAddChildObjects(obj);
            }

            return colObj;

        } catch (DaoException ex) {
            log.error("Error selecting objects .. " + ex.toString(), ex);
            return null;
        }
    }


    public Collection findObjects(String name, String sort,
                                  boolean desc, int start, int rows) {
        ClaimFormIndexDao dao = (ClaimFormIndexDao) getDao();
        try {
            Collection colObj = dao.query(name, sort, desc, start, rows);
            Iterator itr = colObj.iterator();

            while (itr.hasNext()) {
                ClaimFormIndex obj = (ClaimFormIndex) itr.next();
                obj = fnAddChildObjects(obj);
            }

            return colObj;
        } catch (DaoException e) {
            log.error("Error finding contacts " + e.toString(), e);
            throw new ClaimFormIndexException(e.toString());
        }
    }

    public Collection findObjects(String[] conditions, String sort, boolean desc, int start, int rows) {
        ClaimFormIndexDao dao = (ClaimFormIndexDao) getDao();
        try {
            Collection colObj = dao.query(conditions, sort, desc, start, rows);
            Iterator itr = colObj.iterator();

            while (itr.hasNext()) {
                ClaimFormIndex obj = (ClaimFormIndex) itr.next();
                obj = fnAddChildObjects(obj);

                String tempValue =obj.getAmountStr();
                if(tempValue.charAt(0)=='-')
                tempValue = "("+ tempValue.substring(1,tempValue.length())+")";

                obj.setAmountInStr(tempValue);


            }

            return colObj;
        } catch (DaoException e) {
            log.error("Error finding contacts " + e.toString(), e);
            throw new ClaimFormIndexException(e.toString());
        }
    }


     public Collection findObjectsWithDate(String[] conditions, Date startDate, Date endDate, String sort, boolean desc, int start, int rows) {
        ClaimFormIndexDao dao = (ClaimFormIndexDao) getDao();
        try {
            Collection colObj = dao.queryWithDate(conditions, startDate,endDate, sort, desc, start, rows);
            Iterator itr = colObj.iterator();

            while (itr.hasNext()) {
                ClaimFormIndex obj = (ClaimFormIndex) itr.next();
                obj = fnAddChildObjects(obj);

                String tempValue =obj.getAmountStr();
                if(tempValue.charAt(0)=='-')
                tempValue = "("+ tempValue.substring(1,tempValue.length())+")";

                obj.setAmountInStr(tempValue);


            }

            return colObj;
        } catch (DaoException e) {
            log.error("Error finding contacts " + e.toString(), e);
            throw new ClaimFormIndexException(e.toString());
        }
    }




    public List generateReport(Date date,
    String employeeId, String departmentId, List categoryList) {
        ClaimFormIndexDao dao = (ClaimFormIndexDao) getDao();
        try {
            List list;
            list = dao.generateReport(date, employeeId, departmentId, categoryList);
            return list;

        } catch (DaoException e) {
            log.error("Error generating claim report", e);
            return new ArrayList();
        }
    }

    public List generateProjectReport(Date date, String employeeId, String departmentId) {
        ClaimFormIndexDao dao = (ClaimFormIndexDao) getDao();
        try {
            List list;
            list = dao.generateProjectReport(date, employeeId, departmentId);
            return list;

        } catch (DaoException e) {
            log.error("Error generating claim report (project)", e);
            return new ArrayList();
        }
    }





/*
   public int countObjects(String status) {
      ClaimFormIndexDao dao = (ClaimFormIndexDao) getDao();
      try {
         return dao.count(String status);
      } catch (DaoException e) {
         log.error("Error counting ContactType " + e.toString(), e);
         return(0);
      }
   }
*/
    public int countObjects(String status) {
        ClaimFormIndexDao dao = (ClaimFormIndexDao) getDao();
        try {
            return dao.count(status);
        } catch (DaoException e) {
            log.error("Error finding contacts " + e.toString(), e);
            throw new ClaimFormIndexException(e.toString());
        }
    }

    public int countObjects(String[] conditions) {
        ClaimFormIndexDao dao = (ClaimFormIndexDao) getDao();
        try {
            return dao.count(conditions);
        } catch (DaoException e) {
            log.error("Error finding contacts " + e.toString(), e);
            throw new ClaimFormIndexException(e.toString());
        }
    }

      public int countObjectsWithDate(String[] conditions, Date startDate, Date endDate) {
        ClaimFormIndexDao dao = (ClaimFormIndexDao) getDao();
        try {
            return dao.countWithDate(conditions, startDate, endDate);
        } catch (DaoException e) {
            log.error("Error finding contacts " + e.toString(), e);
            throw new ClaimFormIndexException(e.toString());
        }
    }


    public boolean deleteObject(String id) {
        ClaimFormIndexDao dao = (ClaimFormIndexDao) getDao();
        ClaimFormIndex theObj = selectObject(id);
        if (!(theObj.getState().equals(STATE_CREATED) ||
        theObj.getState().equals(STATE_REJECTED))) {
            return false;
        }

        try {
            Application application = Application.getInstance();
            ClaimFormItemModule cfiModule = (ClaimFormItemModule) application.getModule(ClaimFormItemModule.class);
            Collection colCFI = cfiModule.selectObjects("formId", id, 0, -1);
            /// todo, delete the ClaimFormItem as well
            Iterator itr = colCFI.iterator();
            while (itr.hasNext()) {
                ClaimFormItem cfiObj = (ClaimFormItem) itr.next();
                cfiModule.deleteObject(cfiObj.getId());
            }

            dao.deleteRecord(id);
            return true;
        } catch (DaoException ex) {
            log.error("Error deleting object " + ex.toString(), ex);
            return false;
        }

    }

    public boolean setState(String id, String status) {
        return setState(id, status, null);
    }

    public boolean setState(String id, String status, Date claimMonth) {
        ClaimFormIndexDao dao = (ClaimFormIndexDao) getDao();
        try {
            Application application = Application.getInstance();
            ClaimFormItemModule cfiModule = (ClaimFormItemModule) application.getModule(ClaimFormItemModule.class);
            Collection colCFI = cfiModule.selectObjects("formId", id, 0, -1);
            /// todo, set the status of the claim form items as well
            Iterator itr = colCFI.iterator();
            while (itr.hasNext()) {
                ClaimFormItem cfiObj = (ClaimFormItem) itr.next();
                cfiModule.setState(cfiObj.getId(), status);
            }

            ClaimFormIndex indexObj = selectObject(id);
            indexObj.setState(status);
            if (status.equals(STATE_ASSESSED)) {
                // update claimDate
                indexObj.setClaimMonth(claimMonth);
            }
            updateObject(indexObj);

            // TODO: send alerts to approvers
            if (status.equals(STATE_SUBMITTED)) {


            }
            // TODO: send alerts to owners, originators
            if (status.equals(STATE_ASSESSED)) {

            }

            return true;
        } catch (Exception ex) {
            log.error("Error deleting object " + ex.toString(), ex);
            return false;
        }

    }


    public String selectHierarchy(String employeeID) {
        ClaimFormIndexDao dao = (ClaimFormIndexDao) getDao();
        try {
            return dao.selectHierarchy(employeeID);
        } catch (DaoException ex) {
            log.error("Error selecting object " + ex.toString(), ex);
            return (String) null;
        }
    }


    protected ClaimFormIndex fnAddChildObjects(ClaimFormIndex obj) {
        SecurityService service = (SecurityService)
        Application.getInstance().getService(SecurityService.class);
        Application application = Application.getInstance();
        try {
            obj.setUserOriginatorObj(service.getUser(obj.getUserOriginator()));
        } catch (Exception ex) {
        }
        try {
            obj.setUserOwnerObj(service.getUser(obj.getUserOwner()));
        } catch (Exception ex) {
        }
        try {
            obj.setUserApprover1Obj(service.getUser(obj.getUserApprover1()));
        } catch (Exception ex) {
        }
        try {
            obj.setUserApprover2Obj(service.getUser(obj.getUserApprover2()));
        } catch (Exception ex) {
        }
        try {
            obj.setUserApprover3Obj(service.getUser(obj.getUserApprover3()));
        } catch (Exception ex) {
        }
        try {
            obj.setUserApprover4Obj(service.getUser(obj.getUserApprover4()));
        } catch (Exception ex) {
        }
        try {
            obj.setUserAssessorObj(service.getUser(obj.getUserAssessor()));
        } catch (Exception ex) {
        }

        ClaimFormItemModule cfitmMod = (ClaimFormItemModule) application.getModule(ClaimFormItemModule.class);
        obj.vecItems = new Vector(cfitmMod.selectObjects("formId", obj.getId(), 0, -1));
        return obj;
    }


    // use cases
    /**
     * Assessor rejects claim.
     *
     * @param evt
     * @param selectedKeys
     * @throws SecurityException
     */
    public static void assessRejectClaim(Event evt, String[] selectedKeys) throws SecurityException {
        // set the status of the claim form and its item to "sub"
        Application application = Application.getInstance();
        ClaimFormIndexModule module = (ClaimFormIndexModule)
        application.getModule(ClaimFormIndexModule.class);
        String rejId = evt.getWidgetManager().getUser().getId();
        SecurityService service = (SecurityService)
        Application.getInstance().getService(SecurityService.class);
        User usrRej = null;

        usrRej = service.getUser(rejId);

        String description = " Rejected By: " + usrRej.getName() + ".";
        for (int i = 0; i < selectedKeys.length; i++) {
            ClaimFormIndex theObj = module.selectObject(selectedKeys[i]);
            if (!theObj.getInfo().endsWith(description))
            {
	            String tmpDesc = theObj.getInfo();
	            tmpDesc += description;
	            theObj.setInfo(tmpDesc);
	            //if (theObj.getRejectedBy()==null || theObj.getRejectedBy().equals(""))
	                theObj.setRejectedBy(usrRej.getName());
	            //else
	                //theObj.setRejectedBy(","+usrRej.getName());
	            
	            module.updateObject(theObj);
	            module.setState(selectedKeys[i], STATE_REJECTED);
            }
        }
    }

    /**
     * Assessor process (approve) claim.
     *
     * @param selectedKeys
     * @param evt
     */
    public static void assessProcessClaim(Event evt, String[] selectedKeys, Date claimDate) {
        /// set the status of the claim form and its item to "sub"
        Application application = Application.getInstance();
        ClaimFormIndexModule module = (ClaimFormIndexModule)
        application.getModule(ClaimFormIndexModule.class);


        for (int i = 0; i < selectedKeys.length; i++) {
            //module.setState(selectedKeys[i], STATE_ASSESSED, claimDate);
            // once assessor assess the claim, it'll set state to closed.
            module.setState(selectedKeys[i],STATE_CLOSED,claimDate);
            ClaimFormIndex theObj = module.selectObject(selectedKeys[i]);
            String assessorId = evt.getWidgetManager().getUser().getId();
            theObj.setUserAssessor(assessorId);
            theObj.setClaimMonth(claimDate);
            module.updateObject(theObj);
            try {
                Mailer.notifyOwnerOnProcess(evt, theObj);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    // select records for reporting purposes
    public Collection getRecordsForMonthlyReport(String userId, String monthStr) {
        Collection col = null;
        try {
            ClaimFormIndexDao dao = (ClaimFormIndexDao)getDao();
            col = dao.selectForMonthlyReport(userId,monthStr);
        }
        catch(DaoException e) {
            Log.getLog(getClass()).error("Claims - getRecordForMonthlyReport error : ",e);
        }
        return col;
    }



    public String remarkName(String id){

        ClaimFormIndexDao dao = (ClaimFormIndexDao)getDao();
        try {
            return dao.remarkName(id);
        } catch (DaoException e) {
            Log.getLog(getClass()).warn("cannot retrieve remark from id given");
        }

      return null;
    }
    
    
    
    
    public Collection getUsersByUsername(String username)  {
        // select users
      
        	ClaimFormIndexDao dao = (ClaimFormIndexDao)getDao();
            try {
				return dao.selectUsersByUsername(username);
			} catch (DaoException e) {
				Log.getLog(getClass()).error(e.toString());
				
			} catch (DataObjectNotFoundException e) {
				// TODO Auto-generated catch block
				Log.getLog(getClass()).error(e.toString());
			}
			return null;
      
    }
    
    
    
}



