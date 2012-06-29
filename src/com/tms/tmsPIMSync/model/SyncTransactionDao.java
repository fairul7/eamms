package com.tms.tmsPIMSync.model;

import kacang.model.DataSourceDao;
import kacang.model.DaoException;
import kacang.model.DataObjectNotFoundException;

import java.util.Date;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


public class SyncTransactionDao extends DataSourceDao {
    private final String SYNC_TRANSACTION_PROPERTY = "id, sessionId, userId, type, content, createdTime";

    public void init(){
        try {
            super.update("CREATE TABLE `sync_transaction` (" +
                    "  `id` varchar(200) NOT NULL," +
                    "  `sessionId` varchar(200) NOT NULL," +
                    "  `userId` varchar(200) NOT NULL," +
                    "  `type` varchar(50) NOT NULL," +
                    "  `content` text NOT NULL," +
                    "  `createdTime` datetime NOT NULL," +
                    "  PRIMARY KEY  (`id`)," +
                    "  KEY `createdTime` (`createdTime`)," +
                    "  KEY `sessionId` (`sessionId`)," +
                    "  KEY `userId` (`userId`)" +
                    ")", new Object[]{});
        } catch (DaoException e) {
            
        }
    }

    public void saveTransaction(String id, String sessionId, String userId, String type, String content, Date createdTime) {
        try {
            super.update("INSERT INTO sync_transaction ("+ SYNC_TRANSACTION_PROPERTY + ") " +
                    "VALUES(?,?,?,?,?,?)", new Object[]{id, sessionId, userId, type, content, createdTime});
        } catch (DaoException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public void deleteTransaction(String id){
        try {
            super.update("DELETE FROM sync_transaction WHERE id=?", new Object[]{id});
        } catch (DaoException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public Collection getTransactionBySessionId(String sessionId) throws DataObjectNotFoundException {
        Collection col = null;
        try {
            col = super.select("SELECT " + SYNC_TRANSACTION_PROPERTY + " FROM sync_transaction WHERE sessionId=?", HashMap.class, new Object[]{sessionId}, 0, -1);            
        } catch (DaoException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return col;
    }

    public Map getTransactionById(String id) throws DataObjectNotFoundException {
        try {
            Collection col = super.select("SELECT " + SYNC_TRANSACTION_PROPERTY + " FROM sync_transaction WHERE id=?", HashMap.class, new Object[]{id}, 0, -1);
            if(col.size() == 0){
                throw new DataObjectNotFoundException("Sync Transaction with Id: " + id + " not found.");
            }else return (HashMap) col.iterator().next();

        } catch (DaoException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return null;
    }

    public void deleteTransactionBySessionId(String sessionId){
        try {
            super.update("DELETE FROM sync_transaction WHERE sessionId=?", new Object[]{sessionId});
        } catch (DaoException e) {
            
        }
    }
}
