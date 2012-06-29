package com.tms.tmsPIMSync.model;

import kacang.model.DataSourceDao;
import kacang.model.DaoException;

import java.util.Collection;
import java.util.HashMap;

public class TaskMaskerDao extends DataSourceDao
{
    public void init() throws DaoException
    {
        super.update("CREATE TABLE syn_task_mask(userId VARCHAR(255) NOT NULL, taskId VARCHAR(255) NOT NULL, content TEXT, PRIMARY KEY(userId, taskId));", null);
    }

    public void insertMask(String userId, String taskId, String content){
        try{
            deleteMask(userId, taskId);
        }catch (DaoException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        try {
            super.update("INSERT INTO syn_task_mask(userId, taskId, content) VALUES(?,?,?)", new Object[] {userId, taskId, content});
        } catch (DaoException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public void deleteMask(String userId, String taskId) throws DaoException
    {
        super.update("DELETE FROM syn_task_mask WHERE userId=? AND taskId=?", new Object[] {userId, taskId});
    }

    public void deleteUserMasks(String userId) throws DaoException
    {
        super.update("DELETE FROM syn_task_mask WHERE userId=?", new Object[] {userId});
    }

    public Collection selectUserMasks(String userId) throws DaoException
    {
        return super.select("SELECT taskId, content FROM syn_task_mask WHERE userId=?", HashMap.class, new Object[] {userId}, 0, -1);
    }

    public HashMap selectMask(String userId, String taskId) throws DaoException
    {
        HashMap map = new HashMap();
        Collection list = super.select("SELECT userId, taskId, content FROM syn_task_mask WHERE userId=? AND taskId=?", HashMap.class, new Object[] {userId, taskId}, 0, 1);
        if(list.size() > 0)
            map = (HashMap) list.iterator().next();    
        return map;
    }

    public boolean hasMask(String userId, String taskId) throws DaoException
    {
        Collection list = super.select("SELECT taskId FROM syn_task_mask WHERE userId=? AND taskId=?", HashMap.class, new Object[] {userId, taskId}, 0, 1);
        return (list.size() > 0) ? true : false;
    }
}
