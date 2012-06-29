package com.tms.portlet;

import kacang.model.DaoException;
import kacang.model.DaoQuery;
import kacang.model.operator.OperatorIn;
import kacang.model.operator.DaoOperator;
import kacang.services.security.*;
import kacang.Application;

import java.util.Collection;
import java.util.ArrayList;
import java.util.Iterator;

public class PortletDaoMsSql extends PortletDao {
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
            themes = super.select("SELECT portalserver_theme_registry.themeId, themeManagerClass, themeName, themeDescription " +
                    "FROM portalserver_theme_registry WHERE themeId IN (" +
                    "SELECT DISTINCT portalserver_theme_registry.themeId " +
                    "FROM portalserver_theme_registry, portalserver_theme_group " +
                    "WHERE portalserver_theme_registry.themeId = portalserver_theme_group.themeId " + query.getStatement() + ") " +
                    "ORDER BY themeName", Theme.class, query.getArray(), 0, -1);
        }
        return themes;
    }
}
