package com.tms.cms.core.ui;

import com.tms.cms.core.model.ContentAcl;
import com.tms.cms.core.model.ContentException;
import com.tms.cms.core.model.ContentManager;
import com.tms.cms.core.model.ContentObject;
import kacang.Application;
import kacang.model.DaoQuery;
import kacang.model.operator.OperatorIn;
import kacang.model.operator.DaoOperator;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.services.security.Group;
import kacang.stdui.Button;
import kacang.stdui.Form;
import kacang.stdui.SelectBox;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;
import org.apache.commons.collections.SequencedHashMap;

import java.util.*;

public class AclContentForm extends Form {

    public static final String FORWARD_ACL_UPDATED = "aclUpdated";
    public static final String FORWARD_ERROR = "error";

    String key; // content id
    String principalId;
    boolean group;

    SelectBox sbRole;
    Button bSubmit;
    Button bCancel;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getPrincipalId() {
        return principalId;
    }

    public void setPrincipalId(String principalId) {
        this.principalId = principalId;
    }

    public boolean isGroup() {
        return group;
    }

    public void setGroup(boolean group) {
        this.group = group;
    }

    public AclContentForm() {
    }

    public AclContentForm(String name) {
        super(name);
    }

    public void init() {
        initForm();
    }

    public void onRequest(Event event) {
        initForm();
    }

    protected void initForm() {
        removeChildren();

        Application app = Application.getInstance();
        sbRole = new SelectBox("sbRole");
        Map optionMap = new SequencedHashMap();
        optionMap.put("", app.getMessage("general.label.none", "--- None ---"));
        optionMap.put("manager", app.getMessage("general.label.managers", "Managers"));
        optionMap.put("editor", app.getMessage("general.label.editors", "Editors"));
        optionMap.put("author", app.getMessage("general.label.authors", "Authors"));
        optionMap.put("reader", app.getMessage("general.label.readers", "Readers"));
        sbRole.setOptionMap(optionMap);

        bSubmit = new Button("bSubmit", app.getMessage("general.label.submit", "Submit"));
        bCancel = new Button(Form.CANCEL_FORM_ACTION, app.getMessage("general.label.cancel", "Cancel"));

        addChild(sbRole);
        addChild(bSubmit);
        addChild(bCancel);

        // load values
        try {
            String key = getKey();
            String principalId = getPrincipalId();
            User user = getWidgetManager().getUser();

            if (key != null && principalId != null) {
                ContentManager contentManager = (ContentManager)app.getModule(ContentManager.class);
                Collection aclList = contentManager.viewAcl(key, contentManager.getRoleArray(), user);
                for (Iterator i=aclList.iterator(); i.hasNext();) {
                    ContentAcl acl = (ContentAcl)i.next();
                    if (key.equals(acl.getObjectId()) && principalId.equals(acl.getPrincipalId())) {
                        String role = acl.getRole();
                        sbRole.setSelectedOptions(new String[] { role });
                    }
                }
            }
        }
        catch (ContentException e) {
            Log.getLog(getClass()).error("Error retrieving acl for content: " + key, e);
        }

    }

    public Forward onValidate(Event event) {

        String key = getKey();
        String principalId = getPrincipalId();
        String role = null;
        User user = getWidgetManager().getUser();

        // get selected role
        Map selectedMap = sbRole.getSelectedOptions();
        if (selectedMap != null && selectedMap.size() > 0) {
            role = (String)selectedMap.keySet().iterator().next();
        }

        if (key != null && principalId != null) {
            Application app = Application.getInstance();
            ContentManager contentManager = (ContentManager)app.getModule(ContentManager.class);

            try {
                contentManager.updateAcl(key, principalId, role, user);
                return new Forward(FORWARD_ACL_UPDATED);
            }
            catch (ContentException e) {
                Log.getLog(getClass()).error("Error updating acl for content: " + key, e);
                return new Forward(FORWARD_ERROR);
            }
        }
        else {
            return super.onValidate(event);
        }
    }

    /**
     * Retrieves a Map of role names to Collection of Group objects for the current content and principal
     * @return a Map of role=Collection of Group objects
     */
    public Map getRoleGroups() {
        Map roleGroupIdMap = new SequencedHashMap();
        Map roleGroupMap = new SequencedHashMap();

        Application app = Application.getInstance();
        ContentManager contentManager = (ContentManager)app.getModule(ContentManager.class);
        SecurityService security = (SecurityService) app.getService(SecurityService.class);

        try {
            // get acl for principal
            String key = getKey();
            String principalId = getPrincipalId();

            if (key != null && principalId != null) {
                User user = getWidgetManager().getUser();

                // get acl ID for content
                ContentObject co = contentManager.view(key, user);
                Collection aclList = contentManager.viewAcl(co.getAclId(), contentManager.getRoleArray(), user);

                // get user groups and put into map
                Collection userGroups = security.getUserGroups(principalId);
                Map groupMap = new HashMap();
                for (Iterator i=userGroups.iterator(); i.hasNext();) {
                    Group g = (Group)i.next();
                    groupMap.put(g.getId(), g);
                }

                // iterate thru acl list to get groups
                for (Iterator i=aclList.iterator(); i.hasNext();) {
                    ContentAcl acl = (ContentAcl)i.next();
                    if (!acl.getPrincipalId().equals(principalId) && groupMap.containsKey(acl.getPrincipalId())) {
                        Collection groupList = (Collection)roleGroupMap.get(acl.getRole());
                        Group group = (Group)groupMap.get(acl.getPrincipalId());
                        if (groupList == null) {
                            groupList = new ArrayList();
                        }
                        groupList.add(group);
                        roleGroupMap.put(acl.getRole(), groupList);
                    }
                }

/*
                // get groups from security service
                String[] roleArray = contentManager.getRoleArray();
                for (int j=0; j<roleArray.length; j++) {
                    Collection groupIdList = (Collection)roleGroupIdMap.get(roleArray[j]);
                    if (groupIdList != null) {
                        String[] groupIdArray = (String[])groupIdList.toArray(new String[0]);
                        DaoQuery query = new DaoQuery();
                        query.addProperty(new OperatorIn("id", groupIdArray, DaoOperator.OPERATOR_AND));
                        Collection groups = security.getGroups(query, 0, -1, "groupName", false);
                        if (groups != null && groups.size() > 0) {
                            roleGroupMap.put(roleArray[j], groups);
                        }
                    }
                }
*/

            }

        }
        catch (Exception e) {
            Log.getLog(getClass()).error("Error retrieving role groups for content: " + key, e);
        }

        return roleGroupMap;
    }

}
