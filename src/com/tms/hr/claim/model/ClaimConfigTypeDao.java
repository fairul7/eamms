package com.tms.hr.claim.model;

import java.util.Collection;
import java.util.HashMap;

import kacang.model.DaoException;
import kacang.model.DaoQuery;
import kacang.model.DataSourceDao;
import kacang.util.JdbcUtil;


/**
 * Created by IntelliJ IDEA.
 * User: cheewei
 * Date: Dec 8, 2005
 * Time: 12:23:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class ClaimConfigTypeDao extends DataSourceDao {
    public void init() throws DaoException {
        try {
            super.update("ALTER TABLE claim_form_item_category MODIFY claim_form_item_category.type VARCHAR(55)",
                null);
        } catch (DaoException e) {
        }

        try {
            super.update("CREATE TABLE claim_form_type (" +
                "  id varchar(255) NOT NULL default '0'," +
                "  typeName varchar(255) default NULL, PRIMARY KEY  (id)" +
                ")", null);
        } catch (DaoException e) {
        }

        try{
            super.update("ALTER TABLE claim_form_type ADD accountcode VARCHAR(100)",null);
        }
        catch(DaoException e){

        }


        try {
            super.update("CREATE TABLE claim_form_typedept (" +
                "  typeid varchar(255) NOT NULL default '0'," +
                "  departmentid varchar(255) default NULL" +
                ")", null);
        } catch (DaoException e) {
        }
    }

    public void insertType(ClaimTypeObject claimtypeobject)
        throws DaoException {
        super.update("INSERT INTO claim_form_type(id,typeName,accountcode) VALUES(#id#,#typeName#, #accountcode#)",
            claimtypeobject);
    }

    public Collection retrieveAllType(String keyword,String sort, boolean desc, int start,
        int rows) throws DaoException {
        String orderBy = (sort != null) ? sort : "typeName";



        if (desc) {
            orderBy += " DESC";
        }

        if(keyword !=null && !("".equals(keyword))){


        keyword ="'%"+keyword+"%'";

        return super.select("SELECT * FROM claim_form_type WHERE typeName LIKE "+keyword+" Order By " +
            orderBy, ClaimTypeObject.class, null, start, rows);

        }

        else
        return super.select("SELECT * FROM claim_form_type Order By " +
            orderBy, ClaimTypeObject.class, null, start, rows);
    }

    public ClaimTypeObject retrieveType(String id) throws DaoException {
        Collection result = super.select("SELECT * FROM claim_form_type WHERE id=?",
                ClaimTypeObject.class, new Object[] { id }, 0, 1);

        if (result != null) {
            return (ClaimTypeObject) result.iterator().next();
        }

        return null;
    }

    public int countCourseLessons(String keyword) throws DaoException {
        Collection list = null;
        if(keyword !=null && !("".equals(keyword))) {
            keyword = "'%" + keyword +"%'";
            list = super.select("SELECT COUNT(*) AS total FROM claim_form_type WHERE typeName LIKE "+keyword, HashMap.class, null, 0, 1);
        }
        else
            list = super.select("SELECT COUNT(*) AS total FROM claim_form_type", HashMap.class, null, 0, 1);
        HashMap map = (HashMap) list.iterator().next();
        return Integer.parseInt(map.get("total").toString());
    }

    public void deleteType(String id) throws DaoException {
        super.update("DELETE FROM claim_form_type WHERE id=?",
            new Object[] { id });
    }

    public void editType(String name, String id, String accountcode) throws DaoException {
        super.update("UPDATE claim_form_type set typeName=? WHERE id=?",
            new Object[] { name, id });
        super.update("UPDATE claim_form_type set accountcode=? WHERE id=?", new Object[]{accountcode,id});
    }

    public Collection retrivesAllType() throws DaoException {
        return super.select("SELECT * FROM claim_form_type  ",
            ClaimTypeObject.class, null, 0, -1);
    }

    public Collection retrieveAllTypeDepartment(String keyword, String sort, boolean desc,
        int start, int rows) throws DaoException {
        String orderBy = (sort != null) ? sort : "t.typeName";




        if (desc) {
            orderBy += " DESC";
        }


       if(keyword !=null && !("".equals(keyword))){

       keyword = "%"+keyword+"%";

       return super.select(
                "SELECT t.id,t.typeName, CASE WHEN  d.dm_dept_desc IS NULL THEN '-' ELSE  d.dm_dept_desc END as dm_dept_desc FROM claim_form_type t LEFT JOIN claim_form_typedept td on t.id = td.typeid LEFT JOIN department_main d on td.departmentid =d.dm_dept_code WHERE t.typeName LIKE '"+keyword+"' ORDER BY " +
                       orderBy, ClaimTypeDepartObject.class, null, start, rows);


       }

        else
        return super.select(
            "SELECT t.id,t.typeName, CASE WHEN  d.dm_dept_desc IS NULL THEN '-' ELSE  d.dm_dept_desc END as dm_dept_desc FROM claim_form_type t LEFT JOIN claim_form_typedept td on t.id = td.typeid LEFT JOIN department_main d on td.departmentid =d.dm_dept_code ORDER BY " +
            orderBy, ClaimTypeDepartObject.class, null, start, rows);


    }

    public Collection retrieveAllTypeDepartment() throws DaoException {
        return super.select("SELECT * from claim_form_typedept",
            ClaimTypeDepartObject.class, null, 0, -1);
    }

    public int countAllTypeDepart(String keyword) throws DaoException {

        Collection list= null;
        if(keyword !=null && !("".equals(keyword))){

        keyword = "%"+keyword+"%";

        list = super.select("SELECT COUNT(*) as total FROM claim_form_type t LEFT JOIN claim_form_typedept td on t.id = td.typeid LEFT JOIN department_main d on td.departmentid =d.dm_dept_code WHERE t.typeName LIKE '"+keyword+"'",
                            HashMap.class, null, 0, 1);

        }

        else
        list = super.select("SELECT COUNT(*) as total FROM claim_form_type t LEFT JOIN claim_form_typedept td on t.id = td.typeid LEFT JOIN department_main d on td.departmentid =d.dm_dept_code ",
                HashMap.class, null, 0, 1);

        HashMap map = (HashMap) list.iterator().next();

        return Integer.parseInt(map.get("total").toString());
    }

    public void addTypeDepart(ClaimTypeDepartObject object)
        throws DaoException {
        super.update("INSERT INTO claim_form_typedept(typeid,departmentid) VALUES(#typeid#,#departmentid#)",
            object);
    }

    public void delTypeDepart(String typeid) throws DaoException {
        super.update("DELETE FROM claim_form_typedept WHERE typeid=?",
            new Object[] { typeid });
    }

    public ClaimTypeObject selectTypeName(String typeid)
        throws DaoException {

        
        Collection result = super.select("SELECT * from claim_form_type WHERE id=?",
                ClaimTypeObject.class, new Object[] { typeid }, 0, 1);

        if (result != null) {
            return (ClaimTypeObject) result.iterator().next();
        }

        return null;
    }

    public void updateTypeDepart(ClaimTypeDepartObject object)
        throws DaoException {
        super.update("UPDATE claim_form_typedept set departmentid=#departmentid# WHERE typeid=#typeid#",
            object);
    }

    public void deleteTypeDepart(String typeid) throws DaoException {
        super.update("DELETE FROM claim_form_typedept WHERE typeid=?",
            new Object[] { typeid });
    }

    public ClaimTypeDepartObject selectDepartmentType(String typeid)
        throws DaoException {
        Collection result = super.select("SELECT td.departmentid, d.dm_dept_desc FROM claim_form_typedept td LEFT JOIN department_main d ON td.typeid=d.dm_dept_code WHERE typeid=?",
                ClaimTypeDepartObject.class, new Object[] { typeid }, 0, 1);

        if (result.size() > 0) {
            return (ClaimTypeDepartObject) result.iterator().next();
        }

        return null;
    }
    
    public boolean isUniqueType(String type, String id) throws DaoException {
		
    	Collection count = super.select("SELECT COUNT(id) as total FROM claim_form_type WHERE TypeName=? AND NOT id=?", HashMap.class, new Object[]{type,id}, 0, -1);
	 
    	int total = Integer.parseInt(((HashMap)count.iterator().next()).get("total").toString());
	  	if (total > 0) {
	  		return false;
	  	} else {
	  		return true;
	  	}
    }
    
    public boolean isUniqueTypeCode(String code, String id) throws DaoException {
		
    	Collection count = super.select("SELECT COUNT(id) as total FROM claim_form_type WHERE AccountCode=? AND NOT id=?", HashMap.class, new Object[]{code,id}, 0, -1);
	 
    	int total = Integer.parseInt(((HashMap)count.iterator().next()).get("total").toString());
	  	if (total > 0) {
	  		return false;
	  	} else {
	  		return true;
	  	}
    }
}
