package com.tms.tmsPIMSync.model;

import kacang.model.DataObjectNotFoundException;
import kacang.model.DefaultModule;

import java.util.Date;
import java.util.Map;
import java.util.Collection;

public class SyncTransactionModule extends DefaultModule {    
    public void saveTransaction(String id, String sessionId, String userId, String type, String content, Date createdTime){
        SyncTransactionDao dao = (SyncTransactionDao) getDao();
        dao.saveTransaction(id, sessionId, userId, type, content, createdTime);
    }

    public void deleteTransactionById(String id){
        SyncTransactionDao dao = (SyncTransactionDao) getDao();
        dao.deleteTransaction(id);
    }

    public void deleteTransactionBySessionId(String sessionId){
        SyncTransactionDao dao = (SyncTransactionDao) getDao();
        dao.deleteTransactionBySessionId(sessionId);
    }

    public Map getTransactionById(String id){
        SyncTransactionDao dao = (SyncTransactionDao) getDao();
        try {
            return dao.getTransactionById(id);
        } catch (DataObjectNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return null;
    }

    public Collection getTransactionBySessionId(String sessionId){
        SyncTransactionDao dao = (SyncTransactionDao) getDao();
        try {
            return dao.getTransactionBySessionId(sessionId);
        } catch (DataObjectNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return null;
    }
}
