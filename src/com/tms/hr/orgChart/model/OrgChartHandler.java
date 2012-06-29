package com.tms.hr.orgChart.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import kacang.Application;
import kacang.model.DaoException;
import kacang.model.DaoQuery;
import kacang.model.DefaultModule;
import kacang.services.security.SecurityService;
import kacang.util.Log;

/**
 * Created by IntelliJ IDEA.
 * User: blake
 * Date: Mar 15, 2006
 * Time: 6:43:34 PM
 * generics would work great here
 */
public class OrgChartHandler extends DefaultModule {
    public final static String TYPE_DEPT = "dept";
    public final static String TYPE_COUNTRY = "country";
    public final static String TYPE_TITLE = "title";
    public final static String TYPE_STATION = "station";

    public void init(){
    }

    public Collection findAllSetup(String type){
        OrgChartDao dao = (OrgChartDao) getDao();
        try {
            return dao.findAllSetup(type, null, 0, -1, null, false, false);
        } catch (DaoException e) {
            Log.getLog(getClass()).debug("Error getting all", e);
        }
        return null;
    }

    public Collection findAllSetup(String type, boolean onlyActive){
        OrgChartDao dao = (OrgChartDao) getDao();
        try {
            return dao.findAllSetup(type, null, 0, -1, null, false, onlyActive);
        } catch (DaoException e) {
            Log.getLog(getClass()).debug("Error getting all", e);
        }
        return null;
    }
    
    public Collection findAllSetup(String type, String shortDesc, int start, int rows, String sort, boolean desc, boolean onlyActive){
        OrgChartDao dao = (OrgChartDao) getDao();
        try {
            return dao.findAllSetup(type, shortDesc, start, rows, sort, desc, onlyActive);
        } catch (DaoException e) {
            Log.getLog(getClass()).debug("Error getting all", e);
        }
        return null;
    }

    public int countAllSetup(String type, String shortDesc) {
        OrgChartDao dao = (OrgChartDao) getDao();
        try {
            return dao.countAllSetup(type, shortDesc, false);
        } catch (DaoException e) {
            Log.getLog(getClass()).debug("Error counting numbers", e);

        }
        return 0;
    }

    public void saveSetup(String type, OrgSetup obj) {
        OrgChartDao dao = (OrgChartDao) getDao();
        try {
            dao.insertSetup(type, obj);
        } catch (DaoException e) {
            Log.getLog(getClass()).debug("Error saving", e);
        }
    }

    public boolean codeExist(String type, String code) throws DaoException {
        OrgChartDao dao = (OrgChartDao) getDao();
        return dao.codeExist(type, code);
    }

    public void deleteSetup(String type, String selectedKey) {
        OrgChartDao dao = (OrgChartDao) getDao();
        try {
            dao.deleteSetup(type, selectedKey);
        } catch (DaoException e) {
            Log.getLog(getClass()).debug("Error delete setup " + selectedKey, e);
        }
    }

    public Collection findAllSetup(String type, String shortDesc, int start, int rows, String sort, boolean desc) {
        OrgChartDao dao = (OrgChartDao) getDao();
        try {
            return dao.findAllSetup(type, shortDesc, start, rows, sort, desc, false);
        } catch (DaoException e) {
            Log.getLog(getClass()).debug("Error finding all", e);
        }
        return null;
    }

    public void activateSetup(String type, String selectedKey, boolean active) {
        OrgChartDao dao = (OrgChartDao) getDao();
        try {

            OrgSetup obj = dao.selectSetup(type, selectedKey);
            obj.setActive(active);
            dao.updateSetup(type, obj);
        } catch (DaoException e) {
            Log.getLog(getClass()).debug("Error activating " + selectedKey, e);
        }
    }

    public void saveHierachy(StaffHierachy sh) {
        OrgChartDao dao = (OrgChartDao) getDao();
        try {
            dao.insertHierachy(sh);
        } catch (DaoException e) {
            Log.getLog(getClass()).debug("error saving hierachy for user " + sh.getUserId(), e);
        }
    }

    public boolean hierachyExist(String userId) {
        OrgChartDao dao =  (OrgChartDao) getDao();
        try {
            return dao.hierachyExist(userId);
        } catch (DaoException e) {
            Log.getLog(getClass()).debug("error checking hierachy", e);
        }
        return true;
    }
    
    public boolean hierachyDeleted(String userId) {
        OrgChartDao dao =  (OrgChartDao) getDao();
        try {
            return dao.hierachyDeleted(userId);
        } catch (DaoException e) {
            Log.getLog(getClass()).debug("error checking if hierachy previously exist but deleted", e);
        }
        return true;
    }

    public Collection findAllHierachy(int start, int rows, String sort, boolean desc) {
        OrgChartDao dao = (OrgChartDao) getDao();
        try {
            return dao.findAllHierachy(null, start,rows,sort,desc, false, null, null, null);
        } catch (DaoException e) {
            Log.getLog(getClass()).debug("error getting list of hierachy", e);
        }

        return null;
    }

    public Collection findAllHierachy(String name, int start, int rows, String sort, boolean desc, boolean onlyActive, String deptCode, String countryCode, String titleCode) {
    	OrgChartDao dao = (OrgChartDao) getDao();
        try {
            return dao.findAllHierachy(name, start,rows,sort,desc, onlyActive, deptCode, countryCode, titleCode);
        } catch (DaoException e) {
            Log.getLog(getClass()).debug("error getting list of hierachy", e);
        }

        return null;
    }
    
    public int countAllHierachy() {
        OrgChartDao dao = (OrgChartDao) getDao();
        try {
            return dao.countAllHierachy();
        } catch (DaoException e) {
            Log.getLog(getClass()).debug("error counting hierachy", e);
        }
        return 0;
    }

    public Collection findAllHierachyForTable(int start, int rows, String sort, boolean desc, Map countries, Map depts, Map stations, Map titles) {
        OrgChartDao dao = (OrgChartDao) getDao();
        try {
            Collection col = dao.findAllHierachy(null, start, rows, sort, desc, false, null, null, null);
            return fillHierachySets(col, depts, countries, titles, stations);


        } catch (DaoException e) {
            Log.getLog(getClass()).debug("error getting list of hierachy", e);
        }
        return new ArrayList();
    }

    public Collection findAllHierachyForTableWithFilter(String name, int start, int rows, String sort, boolean desc, Map countries, Map depts, Map stations, Map titles, String strCountry, boolean onlyActive, String strDept, String strTitle) {
        OrgChartDao dao = (OrgChartDao) getDao();
        try {
            Collection col = dao.findAllHierachy(name, start, rows, sort, desc, onlyActive, strDept, strCountry, strTitle);
            return fillHierachySets(col, depts, countries, titles, stations);

        } catch (DaoException e) {
            Log.getLog(getClass()).debug("error getting list of hierachy", e);
        }
        return new ArrayList();
    }

    public int countAllHierachy(String strName, boolean onlyActive, String deptCode, String countryCode, String titleCode) {
        OrgChartDao dao = (OrgChartDao) getDao();
        try {
            return dao.countHierachy(strName, onlyActive, deptCode, countryCode, titleCode);
        } catch (DaoException e) {
            Log.getLog(getClass()).debug("error counting hierachy", e);
        }
        return 0;
    }

    public Collection findHierachyUser(DaoQuery userProperties, String sort, boolean desc, int start, int rows) {
    	OrgChartDao dao = (OrgChartDao) getDao();
        try {
            return dao.findHierachyUser(userProperties, sort, desc, start, rows);
        } catch (DaoException e) {
            Log.getLog(getClass()).debug(e, e);
        }
        return null;
    }
    
    public int countHierachyUser(DaoQuery userProperties) {
    	OrgChartDao dao = (OrgChartDao) getDao();
        try {
            return dao.countHierachyUser(userProperties);
        } catch (DaoException e) {
            Log.getLog(getClass()).debug(e, e);
        }
        return 0;
    }
    
    public Collection findHierachyGroupUser(String groupId, DaoQuery userProperties, String sort, boolean desc, int start, int rows) {
    	OrgChartDao dao = (OrgChartDao) getDao();
        try {
            return dao.findHierachyGroupUser(groupId, userProperties, sort, desc, start, rows);
        } catch (DaoException e) {
            Log.getLog(getClass()).debug(e, e);
        }
        return null;
    }
    
    public int countHierachyGroupUser(String groupId, DaoQuery userProperties) {
    	OrgChartDao dao = (OrgChartDao) getDao();
        try {
            return dao.countHierachyGroupUser(groupId, userProperties);
        } catch (DaoException e) {
            Log.getLog(getClass()).debug(e, e);
        }
        return 0;
    }
    
    private Collection fillHierachySets(Collection partial, Map depts, Map countries, Map titles, Map stations){
        SecurityService ss = (SecurityService) Application.getInstance().getService(SecurityService.class);
        Collection shMaps = new ArrayList();
        for(Iterator itr=partial.iterator(); itr.hasNext();){
            HashMap map = new HashMap();
            StaffHierachy sh = (StaffHierachy) itr.next();
            try {
                // put short Desc into Code
                map.put("userId", sh.getUserId());
                map.put("staffName", ss.getUser(sh.getUserId()).getName());
                map.put("countryCode", countries.get(sh.getCountryCode()));
                map.put("deptCode", depts.get(sh.getDeptCode()));
                map.put("stationCode", stations.get(sh.getStationCode()));
                map.put("titleCode", titles.get(sh.getTitleCode()));
                map.put("active", Boolean.valueOf(sh.isActive()));

                if(sh.getSubordinatesId()!=null){
                    map.put("noSubordinates", Integer.toString(sh.getSubordinatesId().size()));
                }else {
                    map.put("noSubordinates", new Integer(0));
                }
            } catch (kacang.services.security.SecurityException e) {

            }
            shMaps.add(map);
        }
        return shMaps;
    }

    public StaffHierachy findStaffHierachy(String userId){
        OrgChartDao dao = (OrgChartDao) getDao();
        try {
            return dao.selectHierachy(userId);
        } catch (DaoException e) {
            Log.getLog(getClass()).debug("Error finding staff hierachy for user: "+userId, e);
        }
        return null;
    }

    public void deleteHierachy(String userId) {
        OrgChartDao dao = (OrgChartDao) getDao();
        try {
            dao.deleteHierachy(userId, false);
        } catch (DaoException e) {
            Log.getLog(getClass()).debug("Error deleting staff hierachy user: "+userId, e);
        }
    }
    
    public void deleteHierachy(String userId, boolean isPhysicalDelete) {
        OrgChartDao dao = (OrgChartDao) getDao();
        try {
            dao.deleteHierachy(userId, isPhysicalDelete);
        } catch (DaoException e) {
            Log.getLog(getClass()).debug("Error deleting staff hierachy user: "+userId, e);
        }
    }
    
    public void undeleteHierachy(String userId) {
        OrgChartDao dao = (OrgChartDao) getDao();
        try {
            dao.undeleteHierachy(userId);
        } catch (DaoException e) {
            Log.getLog(getClass()).debug("Error undeleting staff hierachy user: "+userId, e);
        }
    }
    
    public boolean insertDepartmentCountryAssociativity(DepartmentCountryAssociativityObject obj) {
    	OrgChartDao dao = (OrgChartDao) getDao();
    	boolean isSuccess = true;
    	
    	try {
    		dao.insertDepartmentCountryAssociativity(obj);
    	}
    	catch(DaoException e) {
    		isSuccess = false;
    		Log.getLog(getClass()).error(e, e);
    	}
    	
    	return isSuccess;
    }
    
    public Collection selectDepartmentCountryAssociativity(String associativityId, String deptCode, String countryCode,
    		String sort, boolean desc, int start, int rows) {
    	OrgChartDao dao = (OrgChartDao) getDao();
    	
    	try {
    		return dao.selectDepartmentCountryAssociativity(associativityId, deptCode, countryCode, sort, desc, start, rows);
    	}
    	catch(DaoException e) {
    		Log.getLog(getClass()).error(e, e);
    		return null;
    	} 
    }
    
    public int countDepartmentCountryAssociativity(String associativityId, String deptCode, String countryCode) {
    	OrgChartDao dao = (OrgChartDao) getDao();
    	
    	try {
    		return dao.countDepartmentCountryAssociativity(associativityId, deptCode, countryCode);
    	}
    	catch(DaoException e) {
    		Log.getLog(getClass()).error(e, e);
    		return 0;
    	} 
    }
    
    public DepartmentCountryAssociativityObject getAssociatedCountryDept(String userId) {
    	OrgChartDao dao = (OrgChartDao) getDao();
    	
    	try {
    		return dao.getAssociatedCountryDept(userId);
    	}
    	catch(DaoException e) {
    		Log.getLog(getClass()).error(e, e);
    		return null;
    	} 
    }
    
    public boolean deleteDepartmentCountryAssociativity(String associativityId) {
    	OrgChartDao dao = (OrgChartDao) getDao();
    	boolean isSuccess = true;
    	
    	try {
    		dao.deleteDepartmentCountryAssociativity(associativityId);
    	}
    	catch(DaoException e) {
    		isSuccess = false;
    		Log.getLog(getClass()).error(e, e);
    	}
    	
    	return isSuccess;
    }
    
    public void saveDeptSetup(OrgSetup obj) {
        OrgChartDao dao = (OrgChartDao) getDao();
        try {
            dao.insertDeptSetup(obj);
        } catch (DaoException e) {
            Log.getLog(getClass()).debug("Error saving", e);
        }
    }
    
    public OrgSetup selectDeptSetup(String code) {
        OrgChartDao dao = (OrgChartDao) getDao();
        try {
            return dao.selectDeptSetup(code);
        } catch (DaoException e) {
            Log.getLog(getClass()).debug("Error select", e);
            return new OrgSetup();
        }
    }
    
    public void updateDeptSetup(OrgSetup obj) {
        OrgChartDao dao = (OrgChartDao) getDao();
        try {
            dao.updateDeptSetup(obj);
        } catch (DaoException e) {
            Log.getLog(getClass()).debug("Error update", e);
        }
    }
    
    public Collection findDeptSetup(String shortDesc, int start, int rows, String sort, boolean desc) {
        OrgChartDao dao = (OrgChartDao) getDao();
        try {
            return dao.findDeptSetup(shortDesc, start, rows, sort, desc, false);
        } catch (DaoException e) {
            Log.getLog(getClass()).debug("Error finding all", e);
        }
        return null;
    }
}
