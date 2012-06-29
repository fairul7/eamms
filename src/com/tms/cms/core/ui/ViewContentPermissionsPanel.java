package com.tms.cms.core.ui;

import com.tms.cms.core.model.ContentAcl;
import com.tms.cms.core.model.ContentManager;
import com.tms.cms.core.model.ContentObject;
import kacang.Application;
import kacang.model.DaoQuery;
import kacang.model.operator.DaoOperator;
import kacang.model.operator.OperatorIn;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.stdui.Panel;
import kacang.ui.Event;
import kacang.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * Panel to display the permissions set for a Content Object.
 */
public class ViewContentPermissionsPanel extends Panel {

    private String id;

    public ViewContentPermissionsPanel() {
    }

    public ViewContentPermissionsPanel(String name) {
        super(name);
    }

    public void init() {
    }

    public void onRequest(Event evt) {
        ContentObject co = ContentHelper.getContentObject(evt, getId());
        if (co != null) {
            setId(co.getId());
        }
    }

    public String getDefaultTemplate() {
        return "cms/admin/contentPermissionsPanel";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Collection getManagerList() {
        if (getId() != null) {
            try {
                // retrieve from module
                return retrievePrincipalList(getId(), "manager");
            }
            catch(Exception e) {
                Log.getLog(getClass()).error(e.toString(), e);
                throw new RuntimeException(e.toString());
            }
        }
        else {
            return new ArrayList();
        }
    }

    public Collection getEditorList() {
        if (getId() != null) {
            try {
                // retrieve from module
                return retrievePrincipalList(getId(), "editor");
            }
            catch(Exception e) {
                Log.getLog(getClass()).error(e.toString(), e);
                throw new RuntimeException(e.toString());
            }
        }
        else {
            return new ArrayList();
        }
    }

    public Collection getAuthorList() {
        if (getId() != null) {
            try {
                // retrieve from module
                return retrievePrincipalList(getId(), "author");
            }
            catch(Exception e) {
                Log.getLog(getClass()).error(e.toString(), e);
                throw new RuntimeException(e.toString());
            }
        }
        else {
            return new ArrayList();
        }
    }

    public Collection getReaderList() {
        if (getId() != null) {
            try {
                // retrieve from module
                return retrievePrincipalList(getId(), "reader");
            }
            catch(Exception e) {
                Log.getLog(getClass()).error(e.toString(), e);
                throw new RuntimeException(e.toString());
            }
        }
        else {
            return new ArrayList();
        }
    }

    protected Collection retrievePrincipalList(String id, String role) {
        try {
            Collection principalList = new ArrayList();
            Collection idList = new ArrayList();

            // retrieve from module
            User user = getWidgetManager().getUser();
            ContentManager cm = (ContentManager)Application.getInstance().getModule(ContentManager.class);
            ContentObject co = cm.view(getId(), user);

            // retrieve principal IDs
            Collection aclList = cm.viewAcl(co.getAclId(), new String[] { role }, user);
            for (Iterator i=aclList.iterator(); i.hasNext();) {
                ContentAcl acl = (ContentAcl)i.next();
                String principalId = acl.getPrincipalId();
                idList.add(principalId);
            }

            // retrieve principals
            if (idList.size() > 0) {
                SecurityService security = (SecurityService)Application.getInstance().getService(SecurityService.class);
                DaoQuery props = new DaoQuery();
                props.addProperty(new OperatorIn("id", idList.toArray(), DaoOperator.OPERATOR_AND));
                Collection groupList = security.getGroups(props, 0, -1, "groupName", false);
                principalList.addAll(groupList);
                Collection userList = security.getUsers(props, 0, -1, "firstName", false);
                principalList.addAll(userList);
            }

            return principalList;
        }
        catch (Exception e) {
            throw new RuntimeException(e.toString());
        }

    }

}
