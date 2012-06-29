/*
 * Created on Feb 26, 2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.tms.crm.sales.model;

import java.util.*;

import kacang.model.*;
import kacang.util.Log;

/**
 * @author Paul Pak
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class ProductModule extends DefaultModule {
	Log log = Log.getLog(getClass());
	
	public boolean addProduct(Product pro) {
		ProductDao dao = (ProductDao) getDao();
		try {
			dao.insertRecord(pro);
			return (true);
		} catch (DaoException e) {
			log.error("Error adding Product " + e.toString(), e);
			return (false);
		}
	}
	
	public boolean updateProduct(Product pro) {
		ProductDao dao = (ProductDao) getDao();
		try {
			dao.updateRecord(pro);
			return (true);
		} catch (DaoException e) {
			log.error("Error updating Product " + e.toString(), e);
			return (false);
		}
	}
	
	public Product getProduct(String productID) {
		ProductDao dao = (ProductDao) getDao();
		try {
			return (dao.selectRecord(productID));
		} catch (DaoException e) {
			log.error("Error getting Product " + e.toString(), e);
			return (null);
		}
	}
	
	public Collection getProducts(String category) {
		ProductDao dao = (ProductDao) getDao();
		try {
			return (dao.getProducts(category));
		} catch (DaoException e) {
			log.error("Error getting Product " + e.toString(), e);
			return (null);
		}
	}
	
	public Collection getProductCollection(boolean incArchive) {
		ProductDao dao = (ProductDao) getDao();
		try {
			return (dao.getProductCollection(incArchive));
		} catch (DaoException e) {
			log.error("Error getting Product Collection " + e.toString(), e);
			return (null);
		}
	}
	
	public Map getProductMap(boolean incArchive) {
		ProductDao dao = (ProductDao) getDao();
		try {
			return (dao.getProductMap(incArchive));
		} catch (DaoException e) {
			log.error("Error getting Product Map " + e.toString(), e);
			return (null);
		}
	}
	
	public Map getProductMapping(String category) {
		ProductDao dao = (ProductDao) getDao();
		try {
			return (dao.getProductMapping(category));
		} catch (DaoException e) {
			log.error("Error getting Product Map " + e.toString(), e);
			return (null);
		}
	}  
	
	public Map getProductMap() {
		ProductDao dao = (ProductDao) getDao();
		try {
			return (dao.getProductMap());
		} catch (DaoException e) {
			log.error("Error getting Product Map " + e.toString(), e);
			return (null);
		}
	}
	
	public Collection listProducts(String name, String searchCol, String sort, boolean desc, int start, int rows) {
		ProductDao dao = (ProductDao) getDao();
		try {
			Collection col = dao.listRecords(name, searchCol, sort, desc, start, rows);
			return (col);
		} catch (DaoException e) {
			log.error("Error listing Product " + e.toString(), e);
			return (null);
		}
	}
	
	public int countProducts(String name, String searchCol) {
		ProductDao dao = (ProductDao) getDao();
		try {
			return dao.count(name, searchCol);
		} catch (DaoException e) {
			log.error("Error counting Product " + e.toString(), e);
			return(0);
		}
	}
	
	public boolean isUnique(Product pro) {
		ProductDao dao = (ProductDao) getDao();
		try {
			return dao.isUnique(pro);
		} catch (DaoException e) {
			log.error("Error isUnique Product " + e.toString(), e);
			return(false);
		}
	}

    public void deleleProduct(String id){
        ProductDao dao = (ProductDao) getDao();
        try {
            dao.deleteProduct(id);
        } catch (DaoException e) {
            Log.getLog(getClass()).error(e.getMessage(), e);  //To change body of catch statement use Options | File Templates.
        }
    }

}