package com.tms.collab.forum.ui;

import kacang.Application;
import kacang.model.DaoQuery;
import kacang.services.security.Group;
import kacang.services.security.SecurityService;
import kacang.stdui.ComboSelectBox;
import kacang.util.Log;
import org.apache.commons.collections.SequencedHashMap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

public class UserGroupComboSelect extends ComboSelectBox
{

    public UserGroupComboSelect()
    {
        super();
    }

    public UserGroupComboSelect(String name)
    {
        super(name);
    }

    public void init()
    {
        super.init();
        Map map = getGroupList();
        Log log = Log.getLog(this.getClass());
        log.debug("~~~ Number of User Group = " + map.size() );
        if(!(map.isEmpty()))
            setLeftValues(map);
    }

    private Map getGroupList()
    {
        Log log = Log.getLog(this.getClass());
        Map map = new SequencedHashMap();
        Collection list = new ArrayList();
        try
        {
            list = ((SecurityService) Application.getInstance().getService(SecurityService.class)).getGroups(new DaoQuery(), 0, -1, "groupName", false);
            Group group;
            for(Iterator i = list.iterator(); i.hasNext();)
            {
                group = (Group) i.next();
                log.debug("~~~ groupName = " + group.getGroupName());
                map.put(group.getId(), group.getGroupName());
            }
        }
        catch(Exception e)
        {
            log.error(e.toString(), e);
        }
        return map;
    }
}
