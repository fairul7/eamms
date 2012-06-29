package com.tms.portlet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import kacang.Application;
import kacang.model.DaoException;
import kacang.model.DaoQuery;
import kacang.model.operator.DaoOperator;
import kacang.model.operator.OperatorIn;
import kacang.services.security.Group;
import kacang.services.security.SecurityException;
import kacang.services.security.SecurityService;

/**
 * Oracle's version of the portlet dao. Please note that oracle has a size restriction on table names which causes the
 * need for this extension
 */
public class PortletDaoDB2 extends PortletDao
{
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
            themes = super.select("SELECT portalserver_theme_registry.themeId, themeManagerClass, themeName, themeDescription FROM portalserver_theme_registry, portalserver_theme_group WHERE portalserver_theme_registry.themeId = portalserver_theme_group.themeId " + query.getStatement() + " ORDER BY themeName", Theme.class, query.getArray(), 0, -1);
        }
        return themes;
    }
}
