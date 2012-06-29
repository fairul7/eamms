package com.tms.cms.medialib.model;

import kacang.model.DaoException;
import kacang.util.Log;
import kacang.util.Transaction;

public class LibraryDaoMsSql extends LibraryDao{
	
	public void init() throws DaoException {
        try {
            super.update("CREATE TABLE cml_library (" +
            		"id VARCHAR(35) NOT NULL, " +
            		"name VARCHAR(255), " +
            		"description TEXT, " +
            		"approvalNeeded CHAR(1) DEFAULT 'Y', " +
            		"maxWidth INTEGER DEFAULT 800, " +
            		"dateCreated DATETIME DEFAULT '0000-00-00 00:00:00', " +
            		"lastUpdatedDate DATETIME DEFAULT '0000-00-00 00:00:00', " +
            		"createdBy VARCHAR(255), " +
            		"PRIMARY KEY(id))", null);
            super.update("CREATE TABLE cml_permission (" +
            		"id VARCHAR(255), " +
            		"role VARCHAR(255), " +
            		"principalId VARCHAR(255))", null);
        }
        catch(DaoException error) {
            throw new DaoException(error);
        }
    }
	
	public void update(LibraryObject library) throws DaoException {
        Transaction tx = null;
        
        try {
            tx = getTransaction();
            tx.begin();
            tx.update("UPDATE cml_library SET " +
            		"name=#name#, description=#description#, approvalNeeded=#approvalNeeded#, maxWidth=#maxWidth#, lastUpdatedDate=getDate() " +
            		"WHERE id=#id#", library);
            updateLibraryAccess(tx, library.getId(), library.getManagerGroup(), library.getContributorGroup(), library.getViewerGroup());
            tx.commit();
        }
        catch(Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            Log.getLog(getClass()).error(e);
            throw new DaoException(e.toString());
        }
    }
	
	public void insert(LibraryObject library) throws DaoException {
        Transaction tx = null;
        
        try {
            tx = getTransaction();
            tx.begin();
            tx.update("INSERT INTO cml_library (" +
            		"id, name, description, approvalNeeded, maxWidth, dateCreated, lastUpdatedDate, createdBy) VALUES " +
            		"(#id#, #name#, #description#, #approvalNeeded#, #maxWidth#, getDate(), getDate(), #createdBy#)", library);
            updateLibraryAccess(tx, library.getId(), library.getManagerGroup(), library.getContributorGroup(), library.getViewerGroup());
            tx.commit();
        }
        catch(Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            Log.getLog(getClass()).error(e);
            throw new DaoException(e.toString());
        }
    }

}
