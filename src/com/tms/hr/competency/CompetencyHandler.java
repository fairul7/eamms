package com.tms.hr.competency;

import kacang.model.DaoException;
import kacang.model.DaoQuery;
import kacang.model.DefaultModule;
import kacang.util.Log;

import java.util.Collection;

public class CompetencyHandler extends DefaultModule
{
    public static final String PERMISSION_COMPETENCY_ADD = "com.tms.hr.competency.Competency.add";
    public static final String PERMISSION_COMPETENCY_UPDATE = "com.tms.hr.competency.Competency.update";
    public static final String PERMISSION_COMPETENCY_DELETE = "com.tms.hr.competency.Competency.delete";
    public static final String PERMISSION_COMPETENCY_VIEW = "com.tms.hr.competency.Competency.view";

    public void addCompetency(Competency competency) throws CompetencyException
    {
        try
        {
            CompetencyDao dao = (CompetencyDao) getDao();
            dao.insertCompetency(competency);
        }
        catch (DaoException e)
        {
            Log.getLog(CompetencyHandler.class).error(e.getMessage(), e);
        }
    }

    public void addUserCompetencies(UserCompetencies userCompetencies) throws CompetencyException
    {
        try
        {
            CompetencyDao dao = (CompetencyDao) getDao();
            dao.deleteUserCompetencies(userCompetencies.getUser().getId());
            dao.insertUserCompetencies(userCompetencies);
        }
        catch (DaoException e)
        {
            Log.getLog(CompetencyHandler.class).error(e.getMessage(), e);
        }
    }

    public void updateCompetency(Competency competency) throws CompetencyException
    {
        try
        {
            CompetencyDao dao = (CompetencyDao) getDao();
            dao.updateCompetency(competency);
        }
        catch (DaoException e)
        {
            Log.getLog(CompetencyHandler.class).error(e.getMessage(), e);
        }
    }

    public void deleteCompetency(String competencyId) throws CompetencyException
    {
        try
        {
            CompetencyDao dao = (CompetencyDao) getDao();
            dao.deleteCompetency(competencyId);
        }
        catch (DaoException e)
        {
            Log.getLog(CompetencyHandler.class).error(e.getMessage(), e);
        }
    }

    public void deleteUserCompetency(String userId, String competencyId) throws CompetencyException
    {
        try
        {
            CompetencyDao dao = (CompetencyDao) getDao();
            dao.deleteUserCompetency(userId, competencyId);
        }
        catch (DaoException e)
        {
            Log.getLog(CompetencyHandler.class).error(e.getMessage(), e);
        }
    }

    public void deleteUserCompetencies(String userId) throws CompetencyException
    {
        try
        {
            CompetencyDao dao = (CompetencyDao) getDao();
            dao.deleteUserCompetencies(userId);
        }
        catch (DaoException e)
        {
            Log.getLog(CompetencyHandler.class).error(e.getMessage(), e);
        }
    }

    public Competency getCompetency(String competencyId) throws CompetencyException
    {
        try
        {
            CompetencyDao dao = (CompetencyDao) getDao();
            return dao.selectCompetency(competencyId);
        }
        catch (DaoException e)
        {
            throw new CompetencyException(e.getMessage(), e);
        }
    }

    public Collection getCompetencies() throws CompetencyException
    {
        try
        {
            CompetencyDao dao = (CompetencyDao) getDao();
            return dao.selectCompetencies();
        }
        catch (DaoException e)
        {
            throw new CompetencyException(e.getMessage(), e);
        }
    }

    public Collection getCompetencies(DaoQuery query, int start, int maxResults, String sort, boolean descending) throws CompetencyException
    {
        try
        {
            CompetencyDao dao = (CompetencyDao) getDao();
            return dao.selectCompetencies(query, start, maxResults, sort, descending);
        }
        catch (DaoException e)
        {
            throw new CompetencyException(e.getMessage(), e);
        }
    }

    public int getCompetenciesCount(DaoQuery query, int start, int maxResults, String sort, boolean descending) throws CompetencyException
    {
        try
        {
            CompetencyDao dao = (CompetencyDao) getDao();
            return dao.selectCompetenciesCount(query, start, maxResults, sort, descending);
        }
        catch (DaoException e)
        {
            throw new CompetencyException(e.getMessage(), e);
        }
    }

    public Collection getCompetencyTypes() throws CompetencyException
    {
        try
        {
            CompetencyDao dao = (CompetencyDao) getDao();
            return dao.selectCompetencyTypes();
        }
        catch(DaoException e)
        {
            throw new CompetencyException(e.getMessage(), e);
        }
    }

    public UserCompetencies getUserCompetencies(String userId) throws CompetencyException
    {
        try
        {
            CompetencyDao dao = (CompetencyDao) getDao();
            return dao.selectUserCompetencies(userId);
        }
        catch(DaoException e)
        {
            throw new CompetencyException(e.getMessage(), e);
        }
    }

    public Collection retrieveCompetencies(String username,String competencyId,String level,String sort, boolean desc, int start, int rows){

        try{
            CompetencyDao dao = (CompetencyDao) getDao();
            return dao.retrieveCompetencies(username,competencyId,  level,sort,  desc,  start, rows);
        }
        catch(DaoException e){
           Log.getLog(getClass()).warn("cannot retrieve competencies for users");
        }
     return null;
    }

    public int countRetrieveCompetencies(String username,String competencyId,String level){

        try{
            CompetencyDao dao = (CompetencyDao) getDao();
            return dao.countRetrieveCompetencies(username,competencyId,level);

        }
        catch(DaoException e){
            Log.getLog(getClass()).warn("cannot count competencies for users");
        }
    return 0;
    }


    public Collection retrieveAllCompetencies(){

        try{
            CompetencyDao dao = (CompetencyDao) getDao();
            return dao.retrieveAllCompetencies();
        }
        catch(DaoException e){
            Log.getLog(getClass()).warn("cannot retrieve competencies");
        }

    return null;

    }



}
