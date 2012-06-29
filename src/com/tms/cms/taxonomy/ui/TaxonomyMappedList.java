package com.tms.cms.taxonomy.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import kacang.Application;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.ui.Event;
import kacang.ui.LightWeightWidget;
import kacang.util.Log;

import com.tms.cms.core.model.ContentManager;
import com.tms.cms.core.model.ContentObject;
import com.tms.cms.core.ui.ContentHelper;
import com.tms.cms.taxonomy.model.TaxonomyMap;
import com.tms.cms.taxonomy.model.TaxonomyModule;
import com.tms.cms.taxonomy.model.TaxonomyNode;

public class TaxonomyMappedList extends LightWeightWidget {
    private String contentId;
    private Collection[] mappedList;
    private TaxonomyMap[] list;

    private Map permissionMap;

    public void init() {

    }

    public String getDefaultTemplate() {
        return "taxonomy/contentMappedList";
    }

    public void onRequest(Event event) {
        ContentObject co = ContentHelper.getContentObject(event, getContentId());
        if (co != null) {
            setContentId(co.getId());
        }
        try {
            permissionMap = new HashMap();
            SecurityService service = (SecurityService)Application.getInstance().getService(SecurityService.class);
            User user = service.getCurrentUser(event.getRequest());
            ContentManager cm = (ContentManager)Application.getInstance().getModule(ContentManager.class);
            Collection permissions = cm.viewPermissions(co.getId(), user.getId());
            for (Iterator i=permissions.iterator(); i.hasNext();) {
                String permissionId = (String)i.next();
                if (permissionId != null) {
                    permissionMap.put(permissionId, Boolean.TRUE);
                }
            }
        }
        catch(Exception e) {
            Log.getLog(getClass()).error("Error in get permissionMap:",e);
        }
        addContainerForm(event);

    }

    public void addContainerForm(Event event) {
        mappedList = null;
        list=null;

        SecurityService ss = (SecurityService)Application.getInstance().getService(SecurityService.class);
        User user = ss.getCurrentUser(event.getRequest());
        TaxonomyModule mod = (TaxonomyModule) Application.getInstance().getModule(TaxonomyModule.class);
        list = mod.getMappingByContentId(contentId,user);

        if (list!=null && list.length>0) {
            mappedList = new Collection[list.length];

            for (int i=0;i<list.length;i++) {
                mappedList[i]=new ArrayList();
                getParentNode(list[i].getTaxonomyId(),i,mod);
            }
        }

    }


    public void getParentNode(String taxonomyId, int iCurrent, TaxonomyModule mod) {
        TaxonomyNode node = mod.getNode(taxonomyId);
        if (!node.getParentId().equals("0")) {
            getParentNode(node.getParentId(),iCurrent,mod);
            mappedList[iCurrent].add(node);
        }
        else {
            mappedList[iCurrent].add(node);
        }
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public String getContentId() {
        return contentId;
    }

    public Collection[] getMappedList() {
        return mappedList;
    }

    public void setMappedList(Collection[] mappedList) {
        this.mappedList = mappedList;
    }

    public TaxonomyMap[] getList() {
        return list;
    }

    public void setList(TaxonomyMap[] list) {
        this.list = list;
    }

    public Map getPermissionMap() {
        return permissionMap;
    }

    public void setPermissionMap(Map permissionMap) {
        this.permissionMap = permissionMap;
    }

}
