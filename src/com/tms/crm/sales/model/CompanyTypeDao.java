package com.tms.crm.sales.model;

import kacang.model.DataSourceDao;
import kacang.model.DaoException;
import kacang.model.DefaultDataObject;

import java.util.Collection;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Disen
 * Date: Jul 1, 2004
 * Time: 2:57:38 PM
 * To change this template use File | Settings | File Templates.
 */
public class CompanyTypeDao extends DataSourceDao {


    public int countCompanyTypes() throws DaoException {
        Collection col = super.select("SELECT COUNT(id) as total FROm sfa_companytype",HashMap.class,null,0,-1);
        return Integer.parseInt((String)((HashMap)col.iterator().next()).get("total"));

    }

    public Collection listCompanyTypes(String sort , boolean desc, int sIndex,int rows) throws DaoException {
        String orderBy = "ORDER BY " + ((sort != null) ? sort : "type") + ((desc) ? " DESC" : "");
        return super.select("SELECT id,type,archived FROM sfa_companytype "+orderBy,CompanyType.class,null,sIndex,rows);

    }

    public CompanyType selectCompanyType(String id) throws DaoException {
        Collection col = super.select("SELECT id, type , archived FROM sfa_companytype WHERE id = ?",CompanyType.class, new Object[]{id},0,-1);
        if(col!=null &&col.size()>0){
            return (CompanyType)col.iterator().next();
        }
        return null;
    }

    public Collection selectCompanyTypes() throws DaoException {
        return super.select("SELECT id, type,archived FROM sfa_companytype WHERE archived='0'",CompanyType.class,null,0,-1);
    }

    public void insertCompanyType(CompanyType companyType) throws DaoException {
        super.update("INSERT INTO sfa_companytype(id, type,archived) VALUES( #id#, #type#, #archived# )",companyType);
    }

    public void updateCompanyType(CompanyType companyType) throws DaoException {
        super.update("UPDATE sfa_companytype SET type=#type#, archived=#archived# WHERE id=#id#",companyType);
    }

    public void deleteCompanyType(CompanyType companyType) throws DaoException {
        super.update("DELETE FROM sfa_companytype WHERE id= #id#",companyType);

    }

    public void deleteCompanyType(String id) throws DaoException {
        DefaultDataObject obj = new DefaultDataObject();
        obj.setId(id);
        super.update("DELETE FROM sfa_companytype WHERE id= #id#",obj);

    }

    public boolean isUnique(CompanyType companyType) throws DaoException {
        Collection col = super.select("SELECT COUNT(id) as total FROM sfa_companytype WHERE type=? AND id<>?",HashMap.class,new Object[]{companyType.getType(),companyType.getId()},0,-1);
        int total  = Integer.parseInt( ((HashMap)col.iterator().next()).get("total").toString());
        if(total>0)
            return false;
        return true;       
    }

}
