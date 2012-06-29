package com.tms.crm.sales.model;

import kacang.model.DefaultModule;
import kacang.model.DaoException;

import java.util.Map;
import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: Disen
 * Date: Jul 1, 2004
 * Time: 2:57:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class CompanyTypeModule extends DefaultModule {

    private CompanyTypeDao dao = null;


    /*public Map getCompanyTypeMap(){
        dao = (CompanyTypeDao) getDao();

        return null;
    }*/

    public Collection listCompanyTypes(String sort, boolean desc, int sIndex, int rows) throws CompanyTypeException {
        dao = (CompanyTypeDao) getDao();
        try {
            return dao.listCompanyTypes(sort,desc,sIndex,rows);
        } catch (DaoException e) {
            throw new CompanyTypeException("Error listing company types "+e.getMessage(),e);
        }
    }

    public int countCompanyTypes() throws DaoException {
        dao = (CompanyTypeDao) getDao();
        return dao.countCompanyTypes();
    }

    public CompanyType getCompanyType(String id) throws CompanyTypeException {
        dao = (CompanyTypeDao) getDao();
        try {
            return dao.selectCompanyType(id);
        } catch (DaoException e) {
            throw  new CompanyTypeException("Error getting company type"+e.getMessage(),e);
        }
    }

    public Collection getCompanyTypes() throws CompanyTypeException {
        dao = (CompanyTypeDao) getDao();
        try {
            return dao.selectCompanyTypes();
        } catch (DaoException e) {
            throw new CompanyTypeException("Error getting company types"+e.getMessage(),e);
        }
    }


    public void addCompanyType(CompanyType companyType) throws CompanyTypeException {
        dao = (CompanyTypeDao) getDao();
        try {
            dao.insertCompanyType(companyType);
        } catch (DaoException e) {
            throw new CompanyTypeException("Error adding company type "+e.getMessage(),e);
        }
    }

    public void updateCompanyType(CompanyType companyType) throws CompanyTypeException {
        dao = (CompanyTypeDao) getDao();
        try {
            dao.updateCompanyType(companyType);
        } catch (DaoException e) {
            throw  new CompanyTypeException("Error updating company type"+e.getMessage(),e);
        }
    }

    public void deleteCompanyType(CompanyType companyType) throws CompanyTypeException {
        dao = (CompanyTypeDao) getDao();
        try {
            dao.deleteCompanyType(companyType);
        } catch (DaoException e) {
            throw new CompanyTypeException("Error deleting company type"+e.getMessage(),e);
        }
    }

    public void deleteCompanyType(String id) throws CompanyTypeException {
        dao = (CompanyTypeDao) getDao();
        try {
            dao.deleteCompanyType(id);
        } catch (DaoException e) {
            throw new CompanyTypeException("Error deleting company type"+e.getMessage(),e);
        }
    }

    public boolean isUnique(CompanyType companyType) throws CompanyTypeException {
        dao = (CompanyTypeDao) getDao();
        try {
            return dao.isUnique(companyType);
        } catch (DaoException e) {
            throw new CompanyTypeException("Error verifying uniqueness of company type"+e.getMessage(),e);
        }

    }


}
