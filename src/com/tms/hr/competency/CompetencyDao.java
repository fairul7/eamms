package com.tms.hr.competency;

import kacang.model.DataSourceDao;

import kacang.model.DaoException;
import kacang.model.DaoQuery;
import kacang.util.JdbcUtil;
import kacang.Application;
import kacang.services.security.SecurityService;
import kacang.services.security.User;

import java.util.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import kacang.util.Log;

import org.apache.commons.collections.SequencedHashMap;

public class CompetencyDao extends DataSourceDao
{
    public void init() throws DaoException
    {
        super.update("CREATE TABLE competency(competencyId VARCHAR(35) NOT NULL, competencyName VARCHAR(250) NOT NULL, competencyType VARCHAR(250) NOT NULL, competencyDescription TEXT, PRIMARY KEY(competencyId))", null);
        super.update("CREATE TABLE competency_user(userId VARCHAR(255) NOT NULL, competencyId VARCHAR(35) NOT NULL, competencyLevel VARCHAR(250) NOT NULL, PRIMARY KEY(userId, competencyId))", null);
        
       
    }

    /* Insertion */
    public void insertCompetency(Competency competency) throws DaoException
    {
        super.update("INSERT INTO competency(competencyId, competencyName, competencyType, competencyDescription, unitId) VALUES(#competencyId#, #competencyName#, #competencyType#, #competencyDescription#, #unitId#)", competency);
    }

    public void insertUserCompetencies(UserCompetencies userCompetencies) throws DaoException
    {
        Map competencies = userCompetencies.getCompetencies();
        if(competencies.size() > 0 && (! "".equals(userCompetencies.getUserId())))
        {
            for(Iterator i = competencies.keySet().iterator(); i.hasNext();)
            {
                Competency competency = (Competency) i.next();
                String level = (String) competencies.get(competency);
                insertUserCompetency(userCompetencies.getUserId(), competency.getCompetencyId(), level);
            }
        }
    }

    public void insertUserCompetency(String userId, String competencyId, String level) throws DaoException
    {
        super.update("INSERT INTO competency_user(userId, competencyId, competencyLevel) VALUES(?, ?, ?)", new Object[] {userId, competencyId, level});
    }

    /* Update */
    public void updateCompetency(Competency competency) throws DaoException
    {
        super.update("UPDATE competency SET competencyName = #competencyName#, competencyType = #competencyType#, competencyDescription = #competencyDescription#, unitId = #unitId# WHERE competencyId = #competencyId#", competency);
    }

    /* Deletion */
    public void deleteCompetency(String competencyId) throws DaoException
    {
        super.update("DELETE FROM competency WHERE competencyId = ?", new String[] {competencyId});
        super.update("DELETE FROM competency_user WHERE competencyId = ?", new String[] {competencyId});
    }

    public void deleteUserCompetency(String userId, String competencyId) throws DaoException
    {
        super.update("DELETE FROM competency_user WHERE userId = ? AND competencyId = ?", new Object[] {userId, competencyId});
    }

    public void deleteUserCompetencies(String userId) throws DaoException
    {
        super.update("DELETE FROM competency_user WHERE userId = ?", new Object[] {userId});
    }

    /* Retrieval */
    public Competency selectCompetency(String competencyId) throws DaoException
    {
        Competency competency = null;
        Collection list = super.select("SELECT competencyId, competencyName, competencyType, competencyDescription, unitId FROM competency WHERE competencyId = ?", Competency.class, new Object[] {competencyId}, 0, 1);
        if(list.size() > 0)
            competency = (Competency) list.iterator().next();
        return competency;
    }

    public Collection selectCompetencies() throws DaoException
    {
        return super.select("SELECT competencyId, competencyName, competencyType, competencyDescription FROM competency", Competency.class, null, 0, -1);
    }

    public Collection selectCompetencies(DaoQuery query, int start, int maxResults, String sort, boolean descending) throws DaoException
    {
    	String sql = 
    		"SELECT competencyId, competencyName, competencyType, competencyDescription " +
        	"FROM competency " +
        	"WHERE 1=1 ";
    	sql = sql + query.getStatement();
    	if (sort!= null && !sort.equals("")){
    		sql += "ORDER BY "+sort+(descending ? " DESC " : "");
    	}else {
    		sql += "ORDER BY competencyName ASC ";
    	}
        return super.select(sql, Competency.class, query.getArray(), start, maxResults);
    }

    public int selectCompetenciesCount(DaoQuery query, int start, int maxResults, String sort, boolean descending) throws DaoException
    {
        Collection list = super.select("SELECT COUNT(competencyId) AS intCount FROM competency WHERE 1=1 " + query.getStatement(), HashMap.class, query.getArray(), 0,-1);
        Map map = (HashMap) list.iterator().next();
        return Integer.parseInt(map.get("intCount").toString());
    }

    public Collection selectCompetencyTypes() throws DaoException
    {
        Connection con = null;
        PreparedStatement statement = null;
        ResultSet rsTypes = null;
        Collection types = new ArrayList();
        try
        {
            con = getDataSource().getConnection();
            statement = JdbcUtil.getInstance().createPreparedStatement(con, "SELECT DISTINCT competencyType FROM competency ORDER BY competencyType", null);
            rsTypes = statement.executeQuery();
            while(rsTypes.next())
                types.add(rsTypes.getString("competencyType"));
        }
        catch(Exception e)
        {
            throw new DaoException(e.toString(), e);
        }
        finally
        {
            try
            {
                if(rsTypes != null)
                    rsTypes.close();
            }
            catch (Exception e)
            {
                throw new DaoException(e.toString(), e);
            }
            try
            {
                if(statement != null)
                    statement.close();
            }
            catch (Exception e)
            {
                throw new DaoException(e.toString(), e);
            }
            try
            {
                if(con != null)
                    con.close();
            }
            catch (Exception e)
            {
                throw new DaoException(e.toString(), e);
            }
        }
        return types;
    }

    public UserCompetencies selectUserCompetencies(String userId) throws DaoException
    {
        Map results = new SequencedHashMap();
        Connection con = null;
        PreparedStatement statement = null;
        ResultSet rsCompetencies = null;
        UserCompetencies userCompetencies = new UserCompetencies();
        try
        {
            con = getDataSource().getConnection();
            statement = JdbcUtil.getInstance().createPreparedStatement(con, "SELECT c.competencyId, competencyName, competencyType, competencyDescription, competencyLevel FROM competency c LEFT JOIN competency_user cu ON c.competencyId = cu.competencyId WHERE userId = ? ORDER BY competencyName", new Object[] {userId});
            rsCompetencies = statement.executeQuery();
            while(rsCompetencies.next())
            {
                Competency competency = new Competency();
                competency.setCompetencyId(rsCompetencies.getString("competencyId"));
                competency.setCompetencyName(rsCompetencies.getString("competencyName"));
                competency.setCompetencyType(rsCompetencies.getString("competencyType"));
                competency.setCompetencyDescription(rsCompetencies.getString("competencyDescription"));
                results.put(competency, rsCompetencies.getString("competencyLevel"));
            }
            SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
            User user = service.getUser(userId);
            userCompetencies.setUser(user);
            userCompetencies.setCompetencies(results);
        }
        catch(Exception e)
        {
            throw new DaoException(e.toString(), e);
        }
        finally
        {
            try
            {
                if(rsCompetencies != null)
                    rsCompetencies.close();
            }
            catch (Exception e)
            {
                throw new DaoException(e.toString(), e);
            }
            try
            {
                if(statement != null)
                    statement.close();
            }
            catch (Exception e)
            {
                throw new DaoException(e.toString(), e);
            }
            try
            {
                if(con != null)
                    con.close();
            }
            catch (Exception e)
            {
                throw new DaoException(e.toString(), e);
            }
        }
        return userCompetencies;
    }


    public Collection retrieveCompetencies(String username,String competencyId,String level, String sort, boolean desc, int start, int rows ) throws DaoException{


             String orderBy = (sort != null) ? sort : " comp.competencyName";

                if (desc) {
                   orderBy += " DESC";
               }

        if(username !=null)
        username ="%"+username+"%";



        if(username ==null && competencyId ==null && level==null)
        return super.select("SELECT user.username, comp.competencyName, comp.competencyType, comp.competencyDescription, cuser.competencyLevel FROM competency_user cuser left join competency comp ON cuser.competencyId = comp.competencyId left join security_user user on cuser.userId = user.id ORDER BY "+orderBy,CompetencyListObject.class,null,start,rows);

        else if(username !=null  && competencyId ==null && level==null)
         return super.select("SELECT user.username, comp.competencyName, comp.competencyType, comp.competencyDescription, cuser.competencyLevel FROM competency_user cuser left join competency comp ON cuser.competencyId = comp.competencyId left join security_user user on cuser.userId = user.id WHERE user.username LIKE ? ORDER BY "+orderBy,CompetencyListObject.class,new Object[]{username},start,rows);

        else if(username==null && competencyId !=null && level==null)
         return super.select("SELECT user.username, comp.competencyName, comp.competencyType, comp.competencyDescription, cuser.competencyLevel FROM competency_user cuser left join competency comp ON cuser.competencyId = comp.competencyId left join security_user user on cuser.userId = user.id WHERE comp.competencyId=? ORDER BY "+orderBy,CompetencyListObject.class,new Object[]{competencyId},start,rows);
        else if(username==null && competencyId ==null && level!=null)
                return super.select("SELECT user.username, comp.competencyName, comp.competencyType, comp.competencyDescription, cuser.competencyLevel FROM competency_user cuser left join competency comp ON cuser.competencyId = comp.competencyId left join security_user user on cuser.userId = user.id WHERE cuser.competencyLevel LIKE ? ORDER BY "+orderBy,CompetencyListObject.class,new Object[]{level},start,rows);
        else if(username==null && competencyId !=null && level!=null)
            return super.select("SELECT user.username, comp.competencyName, comp.competencyType, comp.competencyDescription, cuser.competencyLevel FROM competency_user cuser left join competency comp ON cuser.competencyId = comp.competencyId left join security_user user on cuser.userId = user.id WHERE comp.competencyId=? AND cuser.competencyLevel LIKE ? ORDER BY "+orderBy,CompetencyListObject.class,new Object[]{competencyId,level},start,rows);
        else if(username!=null && competencyId ==null && level!=null)
                return super.select("SELECT user.username, comp.competencyName, comp.competencyType, comp.competencyDescription, cuser.competencyLevel FROM competency_user cuser left join competency comp ON cuser.competencyId = comp.competencyId left join security_user user on cuser.userId = user.id WHERE user.username LIKE ? AND cuser.competencyLevel LIKE ? ORDER BY "+orderBy,CompetencyListObject.class,new Object[]{username,level},start,rows);


        else
         return super.select("SELECT user.username, comp.competencyName, comp.competencyType, comp.competencyDescription, cuser.competencyLevel FROM competency_user cuser left join competency comp ON cuser.competencyId = comp.competencyId left join security_user user on cuser.userId = user.id WHERE user.username LIKE ? AND comp.competencyId=? AND cuser.competencyLevel LIKE ? ORDER BY "+orderBy,CompetencyListObject.class,new Object[]{username,competencyId,level},start,rows);




    }


    public int countRetrieveCompetencies(String username,String competencyId, String level) throws DaoException{

        Collection result =null;


        if(username !=null)
        username ="%"+username+"%";

        else if(username ==null && competencyId ==null && level==null)
        result = super.select("SELECT count(*) as total FROM competency_user cuser left join competency comp ON cuser.competencyId = comp.competencyId left join security_user user on cuser.userId = user.id",HashMap.class,null,0,1);

        else if(username !=null  && competencyId ==null && level==null)
         result = super.select("SELECT count(*) as total FROM competency_user cuser left join competency comp ON cuser.competencyId = comp.competencyId left join security_user user on cuser.userId = user.id WHERE user.username LIKE ?",HashMap.class,new Object[]{username},0,1);

        else if(username==null && competencyId !=null && level==null )
         result = super.select("SELECT count(*) as total FROM competency_user cuser left join competency comp ON cuser.competencyId = comp.competencyId left join security_user user on cuser.userId = user.id WHERE comp.competencyId=?",HashMap.class,new Object[]{competencyId},0,1);
        else if(username==null && competencyId ==null && level!=null)
         result = super.select("SELECT count(*) as total FROM competency_user cuser left join competency comp ON cuser.competencyId = comp.competencyId left join security_user user on cuser.userId = user.id WHERE cuser.competencyLevel LIKE ? ",HashMap.class,new Object[]{level},0,1);
        else if(username==null && competencyId !=null && level!=null)
            result = super.select("SELECT count(*) as total FROM competency_user cuser left join competency comp ON cuser.competencyId = comp.competencyId left join security_user user on cuser.userId = user.id WHERE comp.competencyId=? AND cuser.competencyLevel LIKE ? ",HashMap.class,new Object[]{competencyId,level},0,1);
        else if(username!=null && competencyId ==null && level!=null)
                 result = super.select("SELECT count(*) as total FROM competency_user cuser left join competency comp ON cuser.competencyId = comp.competencyId left join security_user user on cuser.userId = user.id WHERE user.username LIKE ? AND cuser.competencyLevel LIKE ? ",HashMap.class,new Object[]{username,level},0,1);


        else
         result = super.select("SELECT count(*) as total FROM competency_user cuser left join competency comp ON cuser.competencyId = comp.competencyId left join security_user user on cuser.userId = user.id WHERE user.username LIKE ? AND comp.competencyId=? AND cuser.competencyLevel LIKE ?",HashMap.class,new Object[]{username,competencyId,level},0,1);

        Map map= new HashMap();

       if(result.size()>0)
         map = (HashMap) result.iterator().next();


        return Integer.parseInt(map.get("total").toString());

    }

    public Collection retrieveAllCompetencies() throws DaoException{

        return super.select("SELECT * from competency", Competency.class,null, 0,-1);

    }




}
