package com.tms.crm.sales.model;

import java.util.Collection;

import kacang.model.DaoException;

public class CompanyTypeDaoDB2 extends CompanyTypeDao{
	
	public Collection selectCompanyTypes() throws DaoException {
        return super.select("SELECT id, type,archived FROM sfa_companytype WHERE archived='0'",CompanyType.class,null,0,-1);
    }

}
