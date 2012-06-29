package com.tms.portlet;

import kacang.model.DefaultModule;
import kacang.model.DaoQuery;
import kacang.model.operator.OperatorEquals;
import kacang.model.operator.DaoOperator;
import kacang.Application;
import kacang.services.security.User;
import kacang.services.security.SecurityService;
import kacang.util.UuidGenerator;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;

import com.tms.portlet.theme.ThemeManager;

/**
 * Created by IntelliJ IDEA.
 * User: Michael
 * Date: Oct 14, 2003
 * Time: 5:15:29 PM
 * To change this template use Options | File Templates.
 */
public class PortletHandler extends DefaultModule
{
    public static final String PERMISSION_PORTLET_VIEW = "com.tms.portlet.Portlet.view";
    public static final String PERMISSION_PORTLET_ADD = "com.tms.portlet.Portlet.add";
    public static final String PERMISSION_PORTLET_EDIT = "com.tms.portlet.Portlet.edit";
    public static final String PERMISSION_PORTLET_DELETE = "com.tms.portlet.Portlet.delete";
    public static final String PERMISSION_THEME_VIEW = "com.tms.portlet.Theme.view";
    public static final String PERMISSION_THEME_ADD = "com.tms.portlet.Theme.add";
    public static final String PERMISSION_THEME_EDIT = "com.tms.portlet.Theme.edit";
    public static final String PERMISSION_THEME_DELETE = "com.tms.portlet.Theme.delete";

    public static final String PROPERTY_IMAGE_PATH = "com.tms.portlet.portalServer.imagePath";

    public void init()
    {
    }

    public Portlet getPortlet(String portletId) throws PortletException
    {
        try
        {
            PortletDao dao = (PortletDao) getDao();
            return dao.selectPortlet(portletId);
        }
        catch(Exception e)
        {
            throw new PortletException(e.getMessage(), e);
        }
    }

    public Collection getPortlets(boolean deepRetrieval) throws PortletException
    {
        try
        {
            PortletDao dao = (PortletDao) getDao();
            return dao.selectPortlets(deepRetrieval);
        }
        catch(Exception e)
        {
            throw new PortletException(e.getMessage(), e);
        }
    }

    public Collection getPortlets(DaoQuery query, int start, int maxResults, String sort, boolean descending, boolean deepRetrieval) throws PortletException
    {
        try
        {
            PortletDao dao = (PortletDao) getDao();
            return dao.selectPortlets(query, start, maxResults, sort, descending, deepRetrieval);
        }
        catch(Exception e)
        {
            throw new PortletException(e.getMessage(), e);
        }
    }

    public int getPortletsCount(DaoQuery query) throws PortletException
    {
        try
        {
            PortletDao dao = (PortletDao) getDao();
            return dao.selectPortletsCount(query);
        }
        catch(Exception e)
        {
            throw new PortletException(e.getMessage(), e);
        }
    }

    public Theme getTheme(String themeId) throws PortletException
    {
        try
        {
            PortletDao dao = (PortletDao) getDao();
            return dao.selectTheme(themeId);
        }
        catch(Exception e)
        {
            throw new PortletException(e.getMessage(), e);
        }
    }

    public Theme getUserTheme(String userId) throws PortletException
    {
        try
        {
            Theme theme;
            PortletDao dao = (PortletDao) getDao();
            String defaultTheme = Application.getInstance().getProperty(Theme.DEFAULT_THEME_PROPERTY);
            theme = dao.selectUserTheme(userId);
            if(theme == null)
            {

                // get themes allowed for user
                Collection themeList = getAvailableThemes(userId);
                Map themeMap = new HashMap();
                for(Iterator i = themeList.iterator(); i.hasNext();)
                {
                    Theme th = (Theme) i.next();
                    themeMap.put(th.getThemeId(), th.getThemeName());
                }

                // get default themes
                DaoQuery query = new DaoQuery();
                query.addProperty(new OperatorEquals("themeManagerClass", defaultTheme, DaoOperator.OPERATOR_AND));
                Collection themes = dao.selectThemes(query, 0, -1, null, false, true);
                for(Iterator i = themes.iterator(); i.hasNext();)
                {
                    Theme th = (Theme) i.next();
                    if (themeMap.containsKey(th.getThemeId()))
                    {
                        theme = th;
                        dao.updateUserTheme(userId, theme.getThemeId());
                        break;
                    }
                }

                if (theme == null)
                {
                    theme = new Theme();
                    theme.setThemeId(UuidGenerator.getInstance().getUuid());
                    theme.setThemeName(defaultTheme);
                    theme.setThemeManagerClass(defaultTheme);
                    addTheme(theme);
                }
                ThemeManager manager = (ThemeManager) Class.forName(theme.getThemeManagerClass()).newInstance();
                manager.initUserThemes(theme.getThemeId(), userId);
            }
            return theme;
        }
        catch(Exception e)
        {
            throw new PortletException(e.getMessage(), e);
        }
    }

    public Collection getThemes(boolean deepRetrieval) throws PortletException
    {
        try
        {
            PortletDao dao = (PortletDao) getDao();
            return dao.selectThemes(deepRetrieval);
        }
        catch(Exception e)
        {
            throw new PortletException(e.getMessage(), e);
        }
    }

    public Collection getThemes(DaoQuery query, int start, int maxResults, String sort, boolean descending, boolean deepRetrieval) throws PortletException
    {
        try
        {
            PortletDao dao = (PortletDao) getDao();
            return dao.selectThemes(query, start, maxResults, sort, descending, deepRetrieval);
        }
        catch(Exception e)
        {
            throw new PortletException(e.getMessage(), e);
        }
    }

    public Collection getPortletThemes(String portletId) throws PortletException
    {
        try
        {
            PortletDao dao = (PortletDao) getDao();
            return dao.selectPortletThemes(portletId);
        }
        catch(Exception e)
        {
            throw new PortletException(e.getMessage(), e);
        }
    }

    public Collection getAvailableThemes(String userId) throws PortletException
    {
        try
        {
            PortletDao dao = (PortletDao) getDao();
            return dao.selectAvailableThemes(userId);
        }
        catch(Exception e)
        {
            throw new PortletException(e.getMessage(), e);
        }
    }

    public Collection getAvailablePortlets(String userId) throws PortletException
    {
        try
        {
            PortletDao dao = (PortletDao) getDao();
            return dao.selectAvailablePortlets(userId);
        }
        catch(Exception e)
        {
            throw new PortletException(e.getMessage(), e);
        }
    }

    public int getThemesCount(DaoQuery query) throws PortletException
    {
        try
        {
            PortletDao dao = (PortletDao) getDao();
            return dao.selectThemesCount(query);
        }
        catch(Exception e)
        {
            throw new PortletException(e.getMessage(), e);
        }
    }

    public Entity getEntity(String entityId) throws PortletException
    {
        try
        {
            PortletDao dao = (PortletDao) getDao();
            Entity entity = dao.selectEntity(entityId);

            return entity;
        }
        catch(Exception e)
        {
            throw new PortletException(e.getMessage(), e);
        }
    }

    public Collection getEntities(String userId, boolean deepRetrieval) throws PortletException
    {
        try
        {
            PortletDao dao = (PortletDao) getDao();
            return dao.selectEntities(userId, deepRetrieval);
        }
        catch(Exception e)
        {
            throw new PortletException(e.getMessage(), e);
        }
    }

    public Collection getEntities(DaoQuery query, int start, int maxResults, String sort, boolean descending, boolean deepRetrieval) throws PortletException
    {
        try
        {
            PortletDao dao = (PortletDao) getDao();
            return dao.selectEntities(query, start, maxResults, sort, descending, deepRetrieval);
        }
        catch(Exception e)
        {
            throw new PortletException(e.getMessage(), e);
        }
    }

    public void addPortlet(Portlet portlet) throws PortletException
    {
        try
        {
            PortletDao dao = (PortletDao) getDao();
            dao.insertPortlet(portlet);
        }
        catch(Exception e)
        {
            throw new PortletException(e.getMessage(), e);
        }
    }

    public void addTheme(Theme theme) throws PortletException
    {
        try
        {
            PortletDao dao = (PortletDao) getDao();
            dao.insertTheme(theme);
        }
        catch(Exception e)
        {
            throw new PortletException(e.getMessage(), e);
        }
    }

    public void addEntity(Entity entity) throws PortletException
    {
        try
        {
            PortletDao dao = (PortletDao) getDao();
            DaoQuery query = new DaoQuery();
            query.addProperty(new OperatorEquals("userId", entity.getUserId(), DaoOperator.OPERATOR_AND));
            query.addProperty(new OperatorEquals("portletId", entity.getPortletId(), DaoOperator.OPERATOR_AND));
            Collection entities = dao.selectEntities(query, 0, -1, null, false, false);
            for(Iterator i = entities.iterator(); i.hasNext();)
            {
                Entity object = (Entity) i.next();
                dao.deleteEntity(object.getEntityId());
            }
            dao.insertEntity(entity);
        }
        catch(Exception e)
        {
            throw new PortletException(e.getMessage(), e);
        }
    }

    public void addThemePortlet(String themeId, String portletId) throws PortletException
    {
        try
        {
            PortletDao dao = (PortletDao) getDao();
            dao.insertDefaultPortlet(themeId, portletId);
        }
        catch(Exception e)
        {
            throw new PortletException(e.getMessage(), e);
        }
    }

    public void editPortlet(Portlet portlet) throws PortletException
    {
        try
        {
            PortletDao dao = (PortletDao) getDao();
            dao.updatePortlet(portlet);
        }
        catch(Exception e)
        {
            throw new PortletException(e.getMessage(), e);
        }
    }

    public void editUserTheme(String userId, String themeId) throws PortletException
    {
        try
        {
            PortletDao dao = (PortletDao) getDao();
            dao.updateUserTheme(userId, themeId);
        }
        catch(Exception e)
        {
            throw new PortletException(e.getMessage(), e);
        }
    }

    public void editTheme(Theme theme) throws PortletException
    {
        try
        {
            PortletDao dao = (PortletDao) getDao();
            dao.updateTheme(theme);
        }
        catch(Exception e)
        {
            throw new PortletException(e.getMessage(), e);
        }
    }

    public void editEntity(Entity entity) throws PortletException
    {
        try
        {
            PortletDao dao = (PortletDao) getDao();
            dao.updateEntity(entity);
        }
        catch(Exception e)
        {
            throw new PortletException(e.getMessage(), e);
        }
    }

    public void deletePortlet(String portletId) throws PortletException
    {
        try
        {
            PortletDao dao = (PortletDao) getDao();
            dao.deletePortlet(portletId);
        }
        catch(Exception e)
        {
            throw new PortletException(e.getMessage(), e);
        }
    }

    public void deleteTheme(String themeId) throws PortletException
    {
        try
        {
            PortletDao dao = (PortletDao) getDao();
            dao.deleteTheme(themeId);
        }
        catch(Exception e)
        {
            throw new PortletException(e.getMessage(), e);
        }
    }

    public void deleteEntity(String entityId) throws PortletException
    {
        try
        {
            PortletDao dao = (PortletDao) getDao();
            dao.deleteEntity(entityId);
        }
        catch(Exception e)
        {
            throw new PortletException(e.getMessage(), e);
        }
    }

    public void deleteUserEntity(String userId) throws PortletException
    {
        try
        {
            PortletDao dao = (PortletDao) getDao();
            dao.deleteUserEntities(userId);
        }
        catch(Exception e)
        {
            throw new PortletException(e.getMessage(), e);
        }
    }

    public void deletePortletThemes(String portletId) throws PortletException
    {
        try
        {
            PortletDao dao = (PortletDao) getDao();
            dao.deletePortletThemes(portletId);
        }
        catch(Exception e)
        {
            throw new PortletException(e.getMessage(), e);
        }
    }
}
