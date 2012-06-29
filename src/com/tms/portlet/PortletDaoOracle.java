package com.tms.portlet;

import kacang.Application;
import kacang.model.DaoException;
import kacang.model.DaoQuery;
import kacang.model.operator.DaoOperator;
import kacang.model.operator.OperatorIn;
import kacang.services.security.*;
import kacang.util.JdbcUtil;
import org.apache.commons.collections.SequencedHashMap;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 * Oracle's version of the portlet dao. Please note that oracle has a size restriction on table names which causes the
 * need for this extension
 */
public class PortletDaoOracle extends PortletDao
{
	public void init() throws DaoException
    {
        super.update("CREATE TABLE portalserver_portlet_registry(portletId VARCHAR(35) NOT NULL, portletClass VARCHAR(250) NOT NULL, portletName VARCHAR(250) NOT NULL, portletTitle VARCHAR(200) NOT NULL, portletDescription TEXT, PRIMARY KEY(portletId))", null);
        super.update("CREATE TABLE portalserver_portlet_preferenc(portletId VARCHAR(35) NOT NULL, preferenceName VARCHAR(200) NOT NULL, preferenceValue TEXT, preferenceEditable CHAR(1) NOT NULL)", null);
        super.update("CREATE TABLE portalserver_theme_registry(themeId VARCHAR(200) NOT NULL, themeManagerClass VARCHAR(200) NOT NULL, themeName VARCHAR(200) NOT NULL, themeDescription TEXT, PRIMARY KEY(themeId))", null);
        super.update("CREATE TABLE portalserver_theme_default_por(themeId VARCHAR(200) NOT NULL, portletId VARCHAR(35) NOT NULL, PRIMARY KEY(themeId, portletId))", null);
        super.update("CREATE TABLE portalserver_theme_group(themeId VARCHAR(200) NOT NULL, groupId VARCHAR(250) NOT NULL, PRIMARY KEY(themeId, groupId))", null);
        super.update("CREATE TABLE portalserver_user_theme(userId VARCHAR(200) NOT NULL, themeId VARCHAR(200) NOT NULL, PRIMARY KEY(userId))", null);
        super.update("CREATE TABLE portalserver_portlet_entity(entityId VARCHAR(35) NOT NULL, userId VARCHAR(200) NOT NULL, portletId VARCHAR(200), entityLocation VARCHAR(50), entityOrder INTEGER, entityState VARCHAR(50), PRIMARY KEY(entityId))", null);
        super.update("CREATE TABLE portalserver_portlet_entity_pr(entityId VARCHAR(35) NOT NULL, preferenceName VARCHAR(200) NOT NULL, preferenceValue TEXT, preferenceEditable CHAR(1) NOT NULL)", null);
    }

    public Map selectPortletPreference(String portletId) throws DaoException
    {
        Map map = new SequencedHashMap();
        Collection preferences = super.select("SELECT preferenceName, preferenceValue, preferenceEditable FROM portalserver_portlet_preferenc WHERE portletId = ?", PortletPreference.class, new Object[] {portletId}, 0, -1);
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
            statement = JdbcUtil.getInstance().createPreparedStatement(con, "SELECT themeId FROM portalserver_theme_default_por WHERE portletId = ?", new Object[] {portletId});
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

    public void insertDefaultPortlet(String themeId, String portletId) throws DaoException
    {
        super.update("INSERT INTO portalserver_theme_default_por(themeId, portletId) VALUES(?, ?)", new Object[] {themeId, portletId});
    }

    public void insertEntity(Entity entity) throws DaoException
    {
        super.update("INSERT INTO portalserver_portlet_entity(entityId, userId, portletId, entityLocation, entityOrder, entityState) VALUES(#entityId#, #userId#, #portletId#, #entityLocation#, #entityOrder#, #entityState#)", entity);
        if(entity.getPreferences() != null)
        {
            for(Iterator i = entity.getPreferences().keySet().iterator(); i.hasNext();)
            {
                PortletPreference preference = (PortletPreference) entity.getPreferences().get(i.next());
                super.update("INSERT INTO portalserver_portlet_entity_pr(entityId, preferenceName, preferenceValue, preferenceEditable) VALUES(?, ?, ?, ?)", new Object[] {entity.getEntityId(), preference.getPreferenceName(), preference.getPreferenceValue(), new Boolean(preference.isPreferenceEditable())});
            }
        }
    }

    public void updatePortlet(Portlet portlet) throws DaoException
    {
        super.update("UPDATE portalserver_portlet_registry SET portletClass = #portletClass#, portletName = #portletName#, portletDescription = #portletDescription#, portletTitle=#portletTitle# WHERE portletId = #portletId#", portlet);
        super.update("DELETE FROM portalserver_portlet_preferenc WHERE portletId = ?", new Object[] {portlet.getPortletId()});
        if(portlet.getPortletPreferences() != null)
        {
            for(Iterator i = portlet.getPortletPreferences().keySet().iterator(); i.hasNext();)
            {
                PortletPreference preference = (PortletPreference) portlet.getPortletPreferences().get(i.next());
                super.update("INSERT INTO portalserver_portlet_preferenc(portletId, preferenceName, preferenceValue, preferenceEditable) VALUES(?, ?, ?, ?)", new Object[] {portlet.getPortletId(), preference.getPreferenceName(), preference.getPreferenceValue(), new Boolean(preference.isPreferenceEditable())});
            }
        }
    }

    public void updateEntity(Entity entity) throws DaoException
    {
        super.update("UPDATE portalserver_portlet_entity SET entityLocation = #entityLocation#, entityOrder = #entityOrder#, entityState = #entityState# WHERE entityId = #entityId#", entity);
        super.update("DELETE FROM portalserver_portlet_entity_pr WHERE entityId = ?", new Object[] {entity.getEntityId()});
        if(entity.getPreferences() != null)
        {
            for(Iterator i = entity.getPreferences().keySet().iterator(); i.hasNext();)
            {
                PortletPreference preference = (PortletPreference) entity.getPreferences().get(i.next());
                super.update("INSERT INTO portalserver_portlet_entity_pr(entityId, preferenceName, preferenceValue, preferenceEditable) VALUES(?, ?, ?, ?)", new Object[] {entity.getEntityId(), preference.getPreferenceName(), preference.getPreferenceValue(), new Boolean(preference.isPreferenceEditable())});
            }
        }
    }

    public void deleteEntity(String entityId) throws DaoException
    {
        super.update("DELETE FROM portalserver_portlet_entity WHERE entityId = ?", new Object[] {entityId});
        super.update("DELETE FROM portalserver_portlet_entity_pr WHERE entityId = ?", new Object[] {entityId});
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
            super.update("DELETE FROM portalserver_portlet_entity_pr WHERE " + query.getStatement(), query.getArray());
        }
    }

    public void deletePortletThemes(String portletId) throws DaoException
    {
        super.update("DELETE FROM portalserver_theme_default_por WHERE portletId = ?", new Object[] {portletId});
    }

    protected void deleteDefaultPortlets(String themeId) throws DaoException
    {
        super.update("DELETE FROM portalserver_theme_default_por WHERE themeId = ?", new Object[] {themeId});
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
            Collection preferences = super.select("SELECT preferenceName, preferenceValue, preferenceEditable FROM portalserver_portlet_entity_pr WHERE entityId= ? ORDER BY preferenceName", PortletPreference.class, new Object[] {entity.getEntityId()}, 0, -1);
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
            statement = JdbcUtil.getInstance().createPreparedStatement(con, "SELECT portletId FROM portalserver_theme_default_por WHERE themeId = ?", new Object[] {themeId});
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

	public Collection selectAvailableThemes(String userId) throws DaoException, kacang.services.security.SecurityException
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
            themes = super.select("SELECT themeId, themeManagerClass, themeName, themeDescription FROM portalserver_theme_registry " +
						"WHERE themeId IN (SELECT DISTINCT portalserver_theme_registry.themeID FROM portalserver_theme_registry, " +
						"portalserver_theme_group WHERE portalserver_theme_registry.themeId = portalserver_theme_group.themeId " +
						query.getStatement() + ") " + "ORDER BY themeName", Theme.class, query.getArray(), 0, -1);
        }
        return themes;
    }
}
