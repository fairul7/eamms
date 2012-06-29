package com.tms.portlet;

import kacang.model.DataSourceDao;
import kacang.model.DaoException;
import kacang.model.DaoQuery;
import kacang.model.operator.OperatorIn;
import kacang.model.operator.DaoOperator;
import kacang.model.operator.OperatorEquals;
import kacang.util.JdbcUtil;
import kacang.services.security.Group;
import kacang.services.security.SecurityService;
import kacang.services.security.SecurityException;
import kacang.Application;

import java.util.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

import org.apache.commons.collections.SequencedHashMap;

import com.tms.cms.syndication.model.SyndicationDaoException;

/**
 * Created by IntelliJ IDEA.
 * User: Michael
 * Date: Oct 14, 2003
 * Time: 5:16:52 PM
 * To change this template use Options | File Templates.
 */
public class PortletDao extends DataSourceDao
{
    public void init() throws DaoException
    {
        super.update("CREATE TABLE portalserver_portlet_registry(portletId VARCHAR(35) NOT NULL, portletClass VARCHAR(250) NOT NULL, portletName VARCHAR(250) NOT NULL, portletTitle VARCHAR(200) NOT NULL, portletDescription TEXT, PRIMARY KEY(portletId))", null);
        super.update("CREATE TABLE portalserver_portlet_preference(portletId VARCHAR(35) NOT NULL, preferenceName VARCHAR(200) NOT NULL, preferenceValue TEXT, preferenceEditable CHAR(1) NOT NULL)", null);
        super.update("CREATE TABLE portalserver_theme_registry(themeId VARCHAR(200) NOT NULL, themeManagerClass VARCHAR(200) NOT NULL, themeName VARCHAR(200) NOT NULL, themeDescription TEXT, PRIMARY KEY(themeId))", null);
        super.update("CREATE TABLE portalserver_theme_default_portlet(themeId VARCHAR(200) NOT NULL, portletId VARCHAR(35) NOT NULL, PRIMARY KEY(themeId, portletId))", null);
        super.update("CREATE TABLE portalserver_theme_group(themeId VARCHAR(200) NOT NULL, groupId VARCHAR(250) NOT NULL, PRIMARY KEY(themeId, groupId))", null);
        super.update("CREATE TABLE portalserver_user_theme(userId VARCHAR(200) NOT NULL, themeId VARCHAR(200) NOT NULL, PRIMARY KEY(userId))", null);
        super.update("CREATE TABLE portalserver_portlet_entity(entityId VARCHAR(35) NOT NULL, userId VARCHAR(200) NOT NULL, portletId VARCHAR(200), entityLocation VARCHAR(50), entityOrder INTEGER, entityState VARCHAR(50), PRIMARY KEY(entityId))", null);
        super.update("CREATE TABLE portalserver_portlet_entity_preference(entityId VARCHAR(35) NOT NULL, preferenceName VARCHAR(200) NOT NULL, preferenceValue TEXT, preferenceEditable CHAR(1) NOT NULL)", null);
    }

    public Portlet selectPortlet(String portletId) throws DaoException
    {
        Collection portlets = super.select("SELECT portletId, portletClass, portletName, portletDescription, portletTitle FROM portalserver_portlet_registry WHERE portletId = ?", Portlet.class, new Object[] {portletId}, 0, 1);
        Portlet portlet = (Portlet) portlets.iterator().next();
        portlet.setPortletPreferences(selectPortletPreference(portlet.getPortletId()));
        return portlet;
    }

    public Collection selectPortlets(boolean deepRetrieval) throws DaoException
    {
        return retrievePreferences(super.select("SELECT portletId, portletClass, portletName, portletDescription, portletTitle FROM portalserver_portlet_registry ORDER BY portletName", Portlet.class, null, 0, -1), deepRetrieval);
    }

    public Collection selectPortlets(DaoQuery query, int start, int maxResults, String sort, boolean descending, boolean deepRetrieval) throws DaoException
    {
        return retrievePreferences(super.select("SELECT portletId, portletClass, portletName, portletDescription, portletTitle FROM portalserver_portlet_registry WHERE portletId = portletId" + query.getStatement() + getSort(sort, descending), Portlet.class, query.getArray(), start, maxResults), deepRetrieval);
    }

    public int selectPortletsCount(DaoQuery query) throws DaoException
    {
        Collection list = super.select("SELECT COUNT(portletId) AS value FROM portalserver_portlet_registry WHERE portletId = portletId" + query.getStatement(), HashMap.class, query.getArray(), 0, -1);
        Map map = (HashMap) list.iterator().next();
        return Integer.parseInt(map.get("value").toString());
    }

    public Map selectPortletPreference(String portletId) throws DaoException
    {
        Map map = new SequencedHashMap();
        Collection preferences = super.select("SELECT preferenceName, preferenceValue, preferenceEditable FROM portalserver_portlet_preference WHERE portletId = ?", PortletPreference.class, new Object[] {portletId}, 0, -1);
        for(Iterator i = preferences.iterator(); i.hasNext();)
        {
            PortletPreference preference = (PortletPreference) i.next();
            map.put(preference.getPreferenceName(), preference);
        }
        return map;
    }

    public Collection selectPortletThemes(String portletId) throws DaoException
    {
        Connection con = null;
        PreparedStatement statement = null;
        ResultSet rsThemes = null;
        Collection themes = new ArrayList();
        Collection themeId = new ArrayList();
        try
        {
            con = getDataSource().getConnection();
            statement = JdbcUtil.getInstance().createPreparedStatement(con, "SELECT themeId FROM portalserver_theme_default_portlet WHERE portletId = ?", new Object[] {portletId});
            rsThemes = statement.executeQuery();
            while(rsThemes.next())
                themeId.add(rsThemes.getString("themeId"));
            if(themeId.size() > 0)
            {
                DaoQuery query = new DaoQuery();
                query.addProperty(new OperatorIn("themeId", themeId.toArray(), DaoOperator.OPERATOR_AND));
                themes = selectThemes(query, 0, -1, "themeName", false, false);
            }
        }
        catch(Exception e)
        {
            throw new DaoException(e.toString(), e);
        }
        finally
        {
            try
            {
                if(statement != null)
                    statement.close();
                if(con != null)
                    con.close();
            }
            catch (Exception e)
            {
                throw new DaoException(e.toString(), e);
            }
        }
        return themes;
    }

    public Theme selectTheme(String themeId) throws DaoException
    {
        Collection themes = super.select("SELECT themeId, themeManagerClass, themeName, themeDescription FROM portalserver_theme_registry WHERE themeId = ?", Theme.class, new Object[] {themeId}, 0, 1);
        Theme theme = (Theme) themes.iterator().next();
        theme.setDefaultPortlets(retrieveThemeDefaultPortlets(theme.getThemeId()));
        theme.setGroups(retrieveThemeGroups(theme.getThemeId()));
        return theme;
    }

    public Collection selectThemes(boolean deepRetrieval) throws DaoException
    {
        Collection themes = super.select("SELECT themeId, themeManagerClass, themeName, themeDescription FROM portalserver_theme_registry", Theme.class, null, 0, -1);
        if(deepRetrieval)
        {
            for(Iterator i = themes.iterator(); i.hasNext();)
            {
                Theme theme = (Theme) i.next();
                theme.setDefaultPortlets(retrieveThemeDefaultPortlets(theme.getThemeId()));
                theme.setGroups(retrieveThemeGroups(theme.getThemeId()));
            }
        }
        return themes;
    }

    public Collection selectThemes(DaoQuery query, int start, int maxResults, String sort, boolean descending, boolean deepRetrieval) throws DaoException
    {
        Collection themes = super.select("SELECT themeId, themeManagerClass, themeName, themeDescription FROM portalserver_theme_registry WHERE themeId = themeId" + query.getStatement() + getSort(sort, descending), Theme.class, query.getArray(), start, maxResults);
        if(deepRetrieval)
        {
            for(Iterator i = themes.iterator(); i.hasNext();)
            {
                Theme theme = (Theme) i.next();
                theme.setDefaultPortlets(retrieveThemeDefaultPortlets(theme.getThemeId()));
                theme.setGroups(retrieveThemeGroups(theme.getThemeId()));
            }
        }
        return themes;
    }

    public int selectThemesCount(DaoQuery query) throws DaoException
    {
        Collection list = super.select("SELECT COUNT(themeId) AS value FROM portalserver_theme_registry WHERE themeId = themeId" + query.getStatement(), HashMap.class, query.getArray(), 0, -1);
        Map map = (HashMap) list.iterator().next();
        return Integer.parseInt(map.get("value").toString());
    }

    public Theme selectUserTheme(String userId) throws DaoException
    {
        Theme theme = null;
        Collection themes = super.select("SELECT portalserver_theme_registry.themeId, themeManagerClass, themeName, themeDescription FROM portalserver_theme_registry INNER JOIN portalserver_user_theme ON portalserver_theme_registry.themeId = portalserver_user_theme.themeId AND userId = ?", Theme.class, new Object[] {userId}, 0, 1);
        if(themes.iterator().hasNext())
            theme = (Theme) themes.iterator().next();
        return theme;
    }

    public Entity selectEntity(String entityId) throws DaoException
    {
        Collection entities = super.select("SELECT entityId, userId, portletId, entityLocation, entityOrder, entityState FROM portalserver_portlet_entity WHERE entityId = ?", Entity.class, new Object[] {entityId}, 0, 1);
        entities = retrieveEntityPreferences(entities);
        Entity entity = (Entity) entities.iterator().next();
        entity.setPortlet(selectPortlet(entity.getPortletId()));
        return entity;
    }

    public Collection selectEntities(String userId, boolean deepRetrieval) throws DaoException
    {
        Collection entities =  super.select("SELECT entityId, userId, portletId, entityLocation, entityOrder, entityState FROM portalserver_portlet_entity WHERE userId = ?", Entity.class, new Object[] {userId}, 0, -1);
        if(deepRetrieval)
        {
            entities = retrieveEntityPreferences(entities);
            for(Iterator i = entities.iterator(); i.hasNext();)
            {
                Entity entity = (Entity) i.next();
                entity.setPortlet(selectPortlet(entity.getPortletId()));
            }
        }
        return entities;
    }

    public Collection selectEntities(DaoQuery query, int start, int maxResults, String sort, boolean descending, boolean deepRetrieval) throws DaoException
    {
        Collection entities = super.select("SELECT entityId, userId, portletId, entityLocation, entityOrder, entityState FROM portalserver_portlet_entity WHERE entityId = entityId" + query.getStatement() + getSort(sort, descending), Entity.class, query.getArray(), start, maxResults);
        if(deepRetrieval)
        {
            entities = retrieveEntityPreferences(entities);
            for(Iterator i = entities.iterator(); i.hasNext();)
            {
                Entity entity = (Entity) i.next();
                entity.setPortlet(selectPortlet(entity.getPortletId()));
            }
        }
        return entities;
    }

    public Collection selectAvailableThemes(String userId) throws DaoException, SecurityException
    {
        SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
        Collection themes = new ArrayList();
        Collection groups = service.getUserGroups(userId);
        Collection groupId = new ArrayList();
        for(Iterator i = groups.iterator(); i.hasNext();)
        {
            groupId.add(((Group) i.next()).getId());
        }
        if(groupId.size() > 0)
        {
            DaoQuery query = new DaoQuery();
            query.addProperty(new OperatorIn("groupId", groupId.toArray(), DaoOperator.OPERATOR_AND));
            themes = super.select("SELECT DISTINCT portalserver_theme_registry.themeId, themeManagerClass, themeName, themeDescription FROM portalserver_theme_registry, portalserver_theme_group WHERE portalserver_theme_registry.themeId = portalserver_theme_group.themeId " + query.getStatement() + " ORDER BY themeName", Theme.class, query.getArray(), 0, -1);
        }
        return themes;
    }

    public Collection selectAvailablePortlets(String userId) throws DaoException
    {
        Collection portlets = new ArrayList();
        Collection themes = super.select("SELECT portalserver_theme_registry.themeId, themeManagerClass, themeName, themeDescription FROM portalserver_theme_registry INNER JOIN portalserver_user_theme ON portalserver_theme_registry.themeId = portalserver_user_theme.themeId AND userId = ?", Theme.class, new Object[] {userId}, 0, 1);
        if(themes.iterator().hasNext())
        {
            Theme theme = (Theme) themes.iterator().next();
            portlets = retrieveThemeDefaultPortlets(theme.getThemeId());
        }
        return portlets;
    }

    public void insertPortlet(Portlet portlet) throws DaoException
    {
        super.update("INSERT INTO portalserver_portlet_registry(portletId, portletClass, portletName, portletDescription, portletTitle) VALUES(#portletId#, #portletClass#, #portletName#, #portletDescription#, #portletTitle#)", portlet);
    }

    public void insertTheme(Theme theme) throws DaoException
    {
        super.update("INSERT INTO portalserver_theme_registry(themeId, themeManagerClass, themeName, themeDescription) VALUES(#themeId#, #themeManagerClass#, #themeName#, #themeDescription#)", theme);
        insertDefaultPortlet(theme.getThemeId(), theme.getDefaultPortlets());
        insertThemeGroups(theme.getThemeId(), theme.getGroups());
    }

    public void insertDefaultPortlet(String themeId, String portletId) throws DaoException
    {
        super.update("INSERT INTO portalserver_theme_default_portlet(themeId, portletId) VALUES(?, ?)", new Object[] {themeId, portletId});
    }

    protected void insertDefaultPortlet(String themeId, Collection portlets) throws DaoException
    {
        if(portlets != null)
        {
            try
            {
                for(Iterator i = portlets.iterator(); i.hasNext();)
                {
                    String portletId = ((Portlet) i.next()).getPortletId();
                    insertDefaultPortlet(themeId, portletId);
                }
            }
            catch(Exception e)
            {
                throw new DaoException(e.toString(), e);
            }
        }
    }

    protected void insertThemeGroups(String themeId, Collection groups) throws DaoException
    {
        if(groups != null)
        {
            for(Iterator i = groups.iterator(); i.hasNext();)
            {
                String groupId = ((Group) i.next()).getId();
                super.update("INSERT INTO portalserver_theme_group(themeId, groupId) VALUES(?, ?)", new Object[] {themeId, groupId});
            }
        }
    }

    public void insertEntity(Entity entity) throws DaoException
    {
        super.update("INSERT INTO portalserver_portlet_entity(entityId, userId, portletId, entityLocation, entityOrder, entityState) VALUES(#entityId#, #userId#, #portletId#, #entityLocation#, #entityOrder#, #entityState#)", entity);
        if(entity.getPreferences() != null)
        {
            for(Iterator i = entity.getPreferences().keySet().iterator(); i.hasNext();)
            {
                PortletPreference preference = (PortletPreference) entity.getPreferences().get(i.next());
                super.update("INSERT INTO portalserver_portlet_entity_preference(entityId, preferenceName, preferenceValue, preferenceEditable) VALUES(?, ?, ?, ?)", new Object[] {entity.getEntityId(), preference.getPreferenceName(), preference.getPreferenceValue(), new Boolean(preference.isPreferenceEditable())});
            }
        }
    }

    public void updatePortlet(Portlet portlet) throws DaoException
    {
        super.update("UPDATE portalserver_portlet_registry SET portletClass = #portletClass#, portletName = #portletName#, portletDescription = #portletDescription#, portletTitle=#portletTitle# WHERE portletId = #portletId#", portlet);
        super.update("DELETE FROM portalserver_portlet_preference WHERE portletId = ?", new Object[] {portlet.getPortletId()});
        if(portlet.getPortletPreferences() != null)
        {
            for(Iterator i = portlet.getPortletPreferences().keySet().iterator(); i.hasNext();)
            {
                PortletPreference preference = (PortletPreference) portlet.getPortletPreferences().get(i.next());
                super.update("INSERT INTO portalserver_portlet_preference(portletId, preferenceName, preferenceValue, preferenceEditable) VALUES(?, ?, ?, ?)", new Object[] {portlet.getPortletId(), preference.getPreferenceName(), preference.getPreferenceValue(), new Boolean(preference.isPreferenceEditable())});
            }
        }
    }

    public void updateTheme(Theme theme) throws DaoException
    {
        super.update("UPDATE portalserver_theme_registry SET themeManagerClass = #themeManagerClass#, themeName = #themeName#, themeDescription = #themeDescription# WHERE themeId = #themeId#", theme);
        deleteDefaultPortlets(theme.getThemeId());
        insertDefaultPortlet(theme.getThemeId(), theme.getDefaultPortlets());
        deleteThemeGroups(theme.getThemeId());
        insertThemeGroups(theme.getThemeId(), theme.getGroups());
    }

    public void updateUserTheme(String userId, String themeId) throws DaoException
    {
        Connection con = null;
        PreparedStatement statement = null;
        ResultSet rsUserTheme = null;
        try
        {
            con = getDataSource().getConnection();
            statement = JdbcUtil.getInstance().createPreparedStatement(con, "SELECT userId FROM portalserver_user_theme WHERE userId = ?", new String[] {userId});
            rsUserTheme = statement.executeQuery();
            if(rsUserTheme.next())
                statement = JdbcUtil.getInstance().createPreparedStatement(con, "UPDATE portalserver_user_theme SET themeId = ? WHERE userId = ?", new Object[] {themeId, userId});
            else
                statement = JdbcUtil.getInstance().createPreparedStatement(con, "INSERT INTO portalserver_user_theme(userId, themeId) VALUES(?, ?)", new Object[] {userId, themeId});
            statement.executeUpdate();
        }
        catch(SQLException e)
        {
            throw new DaoException(e.toString(), e);
        }
        finally
        {
            try
            {
                if(rsUserTheme != null)
                    rsUserTheme.close();
                if(statement != null)
                    statement.close();
                if(con != null)
                    con.close();
            }
            catch (Exception e)
            {
            }
        }
    }

    public void updateEntity(Entity entity) throws DaoException
    {
        super.update("UPDATE portalserver_portlet_entity SET entityLocation = #entityLocation#, entityOrder = #entityOrder#, entityState = #entityState# WHERE entityId = #entityId#", entity);
        super.update("DELETE FROM portalserver_portlet_entity_preference WHERE entityId = ?", new Object[] {entity.getEntityId()});
        if(entity.getPreferences() != null)
        {
            for(Iterator i = entity.getPreferences().keySet().iterator(); i.hasNext();)
            {
                PortletPreference preference = (PortletPreference) entity.getPreferences().get(i.next());
                super.update("INSERT INTO portalserver_portlet_entity_preference(entityId, preferenceName, preferenceValue, preferenceEditable) VALUES(?, ?, ?, ?)", new Object[] {entity.getEntityId(), preference.getPreferenceName(), preference.getPreferenceValue(), new Boolean(preference.isPreferenceEditable())});
            }
        }
    }

    //Delete syn_object when rss portlet is deleted
   /* public void deleteSynObject() throws DaoException {
        String sql;
        sql = "DELETE FROM syn_object";
        super.update(sql,null);
    }*/
    
    public void deletePortlet(String portletId) throws DaoException
    {
    	/*Portlet p=selectPortlet(portletId);
    	if("syndicationPortlet.jsp".equals(p.getPortletName())){
    		deleteSynObject();
    	}*/
        DaoQuery entityQuery = new DaoQuery();
        entityQuery.addProperty(new OperatorEquals("portletId", portletId, DaoOperator.OPERATOR_AND));
        Collection entities = selectEntities(entityQuery, 0, -1, null, false, true);
        for(Iterator i = entities.iterator(); i.hasNext();)
        {
            Entity entity = (Entity) i.next();
            deleteEntity(entity.getEntityId());
        }
        super.update("DELETE FROM portalserver_portlet_registry WHERE portletId = ?", new Object[] {portletId});
        deletePortletThemes(portletId);
    }

    public void deleteTheme(String themeId) throws DaoException
    {
        super.update("DELETE FROM portalserver_theme_registry WHERE themeId = ?", new Object[] {themeId});
        deleteDefaultPortlets(themeId);
        deleteThemeGroups(themeId);
    }

    public void deleteEntity(String entityId) throws DaoException
    {
        super.update("DELETE FROM portalserver_portlet_entity WHERE entityId = ?", new Object[] {entityId});
        super.update("DELETE FROM portalserver_portlet_entity_preference WHERE entityId = ?", new Object[] {entityId});
    }

    public void deleteUserEntities(String userId) throws DaoException
    {
        Collection entities = selectEntities(userId, false);
        super.update("DELETE FROM portalserver_portlet_entity WHERE userId = ?", new Object[] {userId});
        if(entities.size() > 0)
        {
            Collection entityId = new ArrayList();
            for(Iterator i = entities.iterator(); i.hasNext();)
            {
                Entity entity = (Entity) i.next();
                entityId.add(entity.getEntityId());
            }
            DaoQuery query = new DaoQuery();
            query.addProperty(new OperatorIn("entityId", entityId.toArray(), null));
            super.update("DELETE FROM portalserver_portlet_entity_preference WHERE " + query.getStatement(), query.getArray());
        }
    }

    public void deletePortletThemes(String portletId) throws DaoException
    {
        super.update("DELETE FROM portalserver_theme_default_portlet WHERE portletId = ?", new Object[] {portletId});
    }

    protected void deleteDefaultPortlets(String themeId) throws DaoException
    {
        super.update("DELETE FROM portalserver_theme_default_portlet WHERE themeId = ?", new Object[] {themeId});
    }

    protected void deleteThemeGroups(String themeId) throws DaoException
    {
        super.update("DELETE FROM portalserver_theme_group WHERE themeId = ?", new Object[] {themeId});
    }

    protected Collection retrievePreferences(Collection portlets, boolean deepRetrieval) throws DaoException
    {
        if(deepRetrieval)
        {
            for(Iterator i = portlets.iterator(); i.hasNext();)
            {
                Portlet portlet = (Portlet) i.next();
                portlet.setPortletPreferences(selectPortletPreference(portlet.getPortletId()));
            }
        }
        return portlets;
    }

    protected Collection retrieveEntityPreferences(Collection entities) throws DaoException
    {
        for(Iterator i = entities.iterator(); i.hasNext();)
        {
            Entity entity = (Entity) i.next();
            Map map = new SequencedHashMap();
            Collection preferences = super.select("SELECT preferenceName, preferenceValue, preferenceEditable FROM portalserver_portlet_entity_preference WHERE entityId= ? ORDER BY preferenceName", PortletPreference.class, new Object[] {entity.getEntityId()}, 0, -1);
            for(Iterator it = preferences.iterator(); it.hasNext();)
            {
                PortletPreference preference = (PortletPreference) it.next();
                map.put(preference.getPreferenceName(), preference);
            }
            entity.setPreferences(map);
        }
        return entities;
    }

    protected Collection retrieveThemeDefaultPortlets(String themeId) throws DaoException
    {
        Collection portlets = new ArrayList();
        Collection portletId = new ArrayList();

        Connection con = null;
        PreparedStatement statement = null;
        ResultSet rsPortlets = null;
        try
        {
            con = getDataSource().getConnection();
            statement = JdbcUtil.getInstance().createPreparedStatement(con, "SELECT portletId FROM portalserver_theme_default_portlet WHERE themeId = ?", new Object[] {themeId});
            rsPortlets = statement.executeQuery();
            while(rsPortlets.next())
            {
                portletId.add(rsPortlets.getString("portletId"));
            }
            if(portletId.size() > 0)
            {
                DaoQuery query = new DaoQuery();
                query.addProperty(new OperatorIn("portletId", portletId.toArray(), DaoOperator.OPERATOR_AND));
                portlets = selectPortlets(query, 0, -1, null, false, true);
            }
        }
        catch(Exception e)
        {
            throw new DaoException(e.toString(), e);
        }
        finally
        {
            try
            {
                if(rsPortlets != null)
                    rsPortlets.close();
                if(statement != null)
                    statement.close();
                if(con != null)
                    con.close();
            }
            catch (Exception e)
            {
                throw new DaoException(e.toString(), e);
            }
        }
        return portlets;
    }

    protected Collection retrieveThemeGroups(String themeId) throws DaoException
    {
        Collection groups = new ArrayList();
        Collection groupId = new ArrayList();

        Connection con = null;
        PreparedStatement statement = null;
        ResultSet rsGroups = null;
        try
        {
            con = getDataSource().getConnection();
            statement = JdbcUtil.getInstance().createPreparedStatement(con, "SELECT groupId FROM portalserver_theme_group WHERE themeId = ?", new Object[] {themeId});
            rsGroups = statement.executeQuery();
            while(rsGroups.next())
            {
                groupId.add(rsGroups.getString("groupId"));
            }
            if(groupId.size() > 0)
            {
                SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
                DaoQuery query = new DaoQuery();
                query.addProperty(new OperatorIn("id", groupId.toArray(), DaoOperator.OPERATOR_AND));
                groups = service.getGroups(query, 0, -1, "groupName", false);
            }
        }
        catch(Exception e)
        {
            throw new DaoException(e.toString(), e);
        }
        finally
        {
            try
            {
                if(rsGroups != null)
                    rsGroups.close();
                if(statement != null)
                    statement.close();
                if(con != null)
                    con.close();
            }
            catch (Exception e)
            {
                throw new DaoException(e.toString(), e);
            }
        }
        return groups;
    }

    protected String getSort(String sort, boolean descending)
    {
        String strSort = "";
        if(sort != null)
        {
            strSort += " ORDER BY " + sort;
            if(descending)
                strSort += " DESC";
        }
        return strSort;
    }
}
