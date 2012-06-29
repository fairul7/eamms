package com.tms.tmsPIMSync.model;

import kacang.model.DefaultModule;
import kacang.model.DaoException;

import java.util.Collection;
import java.util.HashMap;

public class TaskMasker extends DefaultModule
{
    public void insertMask(String userId, String taskId, String content)
    {
        TaskMaskerDao dao = (TaskMaskerDao) getDao();
        dao.insertMask(userId, taskId, content);
    }

    public void deleteMask(String userId, String taskId)
    {
        try
        {
            TaskMaskerDao dao = (TaskMaskerDao) getDao();
            dao.deleteMask(userId, taskId);
        }
        catch (DaoException e)
        {
        }
    }

    public void deleteUserMasks(String userId)
    {
        try
        {
            TaskMaskerDao dao = (TaskMaskerDao) getDao();
            dao.deleteUserMasks(userId);
        }
        catch (DaoException e)
        {
        }
    }

    public Collection getUserMasks(String userId)
    {
        try
        {
            TaskMaskerDao dao = (TaskMaskerDao) getDao();
            return dao.selectUserMasks(userId);
        }
        catch (DaoException e)
        {
        }
        return null;
    }

    public boolean hasMask(String userId, String taskId)
    {
        try
        {
            TaskMaskerDao dao = (TaskMaskerDao) getDao();
            return dao.hasMask(userId, taskId);
        }
        catch (DaoException e)
        {
        }
        return false;
    }

    public HashMap getMask(String userId, String taskId)
    {
        try
        {
            TaskMaskerDao dao = (TaskMaskerDao) getDao();
            return dao.selectMask(userId, taskId);
        }
        catch (DaoException e)
        {
        }
        return null;
    }
}
