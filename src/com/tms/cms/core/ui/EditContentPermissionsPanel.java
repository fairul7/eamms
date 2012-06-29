package com.tms.cms.core.ui;

import com.tms.cms.core.model.ContentAcl;
import com.tms.cms.core.model.ContentException;
import com.tms.cms.core.model.ContentManager;
import com.tms.cms.core.model.ContentObject;
import com.tms.ekms.security.ui.UsersSelectBox;
import kacang.Application;
import kacang.model.DaoQuery;
import kacang.services.security.Principal;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.stdui.*;
import kacang.stdui.event.FormEventAdapter;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.stdui.validator.Validator;
import kacang.ui.*;
import kacang.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Panel containing a form to edit the permissions for a Content Object.
 */
public class EditContentPermissionsPanel extends Panel {

    private String id;

    private Form containerForm;
    private CheckBox inheritCheckBox;
    private CheckBox propagateCheckBox;
    private FlexiComboSelectBox aclSelectBox;
    private SelectBox principalSelectBox;
    private SelectBox managerSelectBox;
    private SelectBox editorSelectBox;
    private SelectBox authorSelectBox;
    private SelectBox readerSelectBox;
    private Button submitButton;
    private Button cancelButton;

    private UsersSelectBox userManagerSelectBox;
    private UsersSelectBox userEditorSelectBox;
    private UsersSelectBox userAuthorSelectBox;
    private UsersSelectBox userReaderSelectBox;

    public EditContentPermissionsPanel() {
    }

    public EditContentPermissionsPanel(String name) {
        super(name);
    }

    public void init() {
        addContainerForm();
    }

    public void onRequest(Event evt) {
        ContentObject co = ContentHelper.getContentObject(evt, getId());
        boolean sameId = false;
        if (co != null) {
            sameId = co.getId().equals(getId());
            setId(co.getId());
        }
        if (!sameId || containerForm == null || !containerForm.isInvalid()) {
            addContainerForm();
        }
    }

    protected void addContainerForm() {
        // remove existing widgets
        removeChildren();

        // get content form
        try {
            if (getId() != null) {
                // retrieve from module
                User user = getWidgetManager().getUser();
                ContentManager cm = (ContentManager)Application.getInstance().getModule(ContentManager.class);
                ContentObject co = cm.view(getId(), user);

                // check permission
                if (cm.hasPermission(co, user.getId(), ContentManager.USE_CASE_ACL_UPDATE)) {

                    // add form
                    containerForm = new Form("containerForm");
                    containerForm.setMethod("POST");
                    addChild(containerForm);


                    // add inherit check box
                    Application application = Application.getInstance();
                    inheritCheckBox = new CheckBox("inheritCheckBox");
                    inheritCheckBox.setText(application.getMessage("general.label.inheritFromParent", "Inherit from parent"));
                    inheritCheckBox.setChecked(!co.getId().equals(co.getAclId()));
                    if (co.getParentId() != null) {
                        containerForm.addChild(inheritCheckBox);
                    }

                    // add propagate check box
                    propagateCheckBox = new CheckBox("propagateCheckBox");
                    propagateCheckBox.setText(application.getMessage("general.label.propagate", "Propagate"));
                    propagateCheckBox.setChecked(false);
                    if (co.getParentId() != null) {
                        containerForm.addChild(propagateCheckBox);
                    }

                    // create manager select box
                    Validator managerNotEmpty = new ValidatorNotEmptyManager("managerNotEmpty");
                    managerNotEmpty.setText(application.getMessage("general.error.managers", "Managers must not be empty"));
                    principalSelectBox = new SelectBox("principalSelectBox");
                    principalSelectBox.setRows(20);
                    managerSelectBox = new SelectBox("managerSelectBox");
                    managerSelectBox.addChild(managerNotEmpty);
                    editorSelectBox = new SelectBox("editorSelectBox");
                    authorSelectBox = new SelectBox("authorSelectBox");
                    readerSelectBox = new SelectBox("readerSelectBox");
                    userManagerSelectBox = new UsersSelectBox("userManagerSelectBox");
                    userEditorSelectBox = new UsersSelectBox("userEditorSelectBox");
                    userAuthorSelectBox = new UsersSelectBox("userAuthorSelectBox");
                    userReaderSelectBox = new UsersSelectBox("userReaderSelectBox");

                    // get all available principals
                    SecurityService security = (SecurityService)Application.getInstance().getService(SecurityService.class);
                    Collection principalList = security.getGroups(new DaoQuery(), 0, -1, "groupName", false);

                    // get acl for content
                    Collection managerList = new ArrayList();
                    Collection editorList = new ArrayList();
                    Collection authorList = new ArrayList();
                    Collection readerList = new ArrayList();
                    Collection aclList = cm.viewAcl(getId(), cm.getRoleArray(), user);

                    // iterate thru acl to populate lists
                    for (Iterator i=aclList.iterator(); i.hasNext();) {
                        ContentAcl acl = (ContentAcl)i.next();
                        if ("manager".equals(acl.getRole())) {
                            managerList.add(acl.getPrincipalId());
                        }
                        else if ("editor".equals(acl.getRole())) {
                            editorList.add(acl.getPrincipalId());
                        }
                        else if ("author".equals(acl.getRole())) {
                            authorList.add(acl.getPrincipalId());
                        }
                        else if ("reader".equals(acl.getRole())) {
                            readerList.add(acl.getPrincipalId());
                        }
                    }

                    // iterate thru groups
                    for (Iterator i=principalList.iterator(); i.hasNext();) {
                        Principal principal = (Principal)i.next();
                        if (managerList.contains(principal.getId())) {
                            managerSelectBox.addOption(principal.getId(), principal.getName());
                            managerList.remove(principal.getId());
                        }
                        else if (editorList.contains(principal.getId())) {
                            editorSelectBox.addOption(principal.getId(), principal.getName());
                            editorList.remove(principal.getId());
                        }
                        else if (authorList.contains(principal.getId())) {
                            authorSelectBox.addOption(principal.getId(), principal.getName());
                            authorList.remove(principal.getId());
                        }
                        else if (readerList.contains(principal.getId())) {
                            readerSelectBox.addOption(principal.getId(), principal.getName());
                            readerList.remove(principal.getId());
                        }
                        else {
                            principalSelectBox.addOption(principal.getId(), principal.getName());
                        }
                    }

                    aclSelectBox = new FlexiComboSelectBox("aclSelectBox");
                    aclSelectBox.setLeftSelect(principalSelectBox);
                    aclSelectBox.setLeftSelectHeader(application.getMessage("general.label.principals", "Principals"));
                    aclSelectBox.addRightSelect(managerSelectBox, application.getMessage("general.label.managers", "Managers"));
                    aclSelectBox.addRightSelect(editorSelectBox, application.getMessage("general.label.editors", "Editors"));
                    aclSelectBox.addRightSelect(authorSelectBox, application.getMessage("general.label.authors", "Authors"));
                    aclSelectBox.addRightSelect(readerSelectBox, application.getMessage("general.label.readers", "Readers"));

                    containerForm.addChild(aclSelectBox);

                    // add user select boxes
                    userManagerSelectBox.setIds((String[])managerList.toArray(new String[0]));
                    userEditorSelectBox.setIds((String[])editorList.toArray(new String[0]));
                    userAuthorSelectBox.setIds((String[])authorList.toArray(new String[0]));
                    userReaderSelectBox.setIds((String[])readerList.toArray(new String[0]));
                    containerForm.addChild(new Label("l1", application.getMessage("general.label.managers", "Managers")));
                    containerForm.addChild(userManagerSelectBox);
                    containerForm.addChild(new Label("l2", application.getMessage("general.label.editors", "Editors")));
                    containerForm.addChild(userEditorSelectBox);
                    containerForm.addChild(new Label("l3", application.getMessage("general.label.authors", "Authors")));
                    containerForm.addChild(userAuthorSelectBox);
                    containerForm.addChild(new Label("l4", application.getMessage("general.label.readers", "Readers")));
                    containerForm.addChild(userReaderSelectBox);
                    userManagerSelectBox.init();
                    userEditorSelectBox.init();
                    userAuthorSelectBox.init();
                    userReaderSelectBox.init();

                    // add buttons
                    submitButton = new Button("submitButton");
                    submitButton.setText(application.getMessage("general.label.submit", "Submit"));
                    containerForm.addChild(submitButton);
                    cancelButton = new Button(Form.CANCEL_FORM_ACTION);
                    cancelButton.setText(application.getMessage("general.label.cancel", "Cancel"));
                    containerForm.addChild(cancelButton);

                    // add form listener
                    containerForm.addFormEventListener(new FormEventAdapter() {
                        public Forward onValidate(Event evt) {
                            // get selected button
                            WidgetManager wm = evt.getWidgetManager();
                            String buttonClicked = containerForm.findButtonClicked(evt);
                            Widget button = wm.getWidget(buttonClicked);

                            if (submitButton.equals(button)) {
                                boolean hasManager = saveAcl();
                                if (hasManager) {
                                    init();
                                    return new Forward("updated");
                                }
                                else {
                                    return new Forward("invalid");
                                }
                            }
                            else {
                                return new Forward(Form.CANCEL_FORM_ACTION);
                            }
                        }
                    });

                    // add forwards
                    for (Iterator it=getForwardMap().values().iterator(); it.hasNext();) {
                        containerForm.addForward((Forward)it.next());
                    }
                }
            }
        }
        catch(ContentException e) {
            Log.getLog(getClass()).error(e.toString(), e);
        }
        catch (Exception e) {
            throw new RuntimeException(e.toString());
        }

    }

    public String getDefaultTemplate() {
        return "cms/admin/contentPermissionsForm";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    protected boolean saveAcl() {
        try {
            // retrieve contentObject
            Application application = Application.getInstance();
            ContentManager contentManager = (ContentManager)application.getModule(ContentManager.class);
            User user = getWidgetManager().getUser();
            ContentObject co = contentManager.view(getId(), user);
            boolean propagate = propagateCheckBox.isChecked();

            if (inheritCheckBox.isChecked()) {
                // inherit permissions
                contentManager.inheritAcl(co.getId(), propagate, user);
                return true;
            }
            else {
                // update permissions
                boolean hasManager = false;
                Collection aclList = new ArrayList();
                List managerList = (List)managerSelectBox.getValue();
                for (Iterator i=managerList.iterator(); i.hasNext();) {
                    String principalId = (String)i.next();
                    ContentAcl acl = new ContentAcl();
                    acl.setRole("manager");
                    acl.setPrincipalId(principalId);
                    acl.setObjectId(co.getId());
                    aclList.add(acl);
                    hasManager = true;
                }
                List editorList = (List)editorSelectBox.getValue();
                for (Iterator i=editorList.iterator(); i.hasNext();) {
                    String principalId = (String)i.next();
                    ContentAcl acl = new ContentAcl();
                    acl.setRole("editor");
                    acl.setPrincipalId(principalId);
                    acl.setObjectId(co.getId());
                    aclList.add(acl);
                }
                List authorList = (List)authorSelectBox.getValue();
                for (Iterator i=authorList.iterator(); i.hasNext();) {
                    String principalId = (String)i.next();
                    ContentAcl acl = new ContentAcl();
                    acl.setRole("author");
                    acl.setPrincipalId(principalId);
                    acl.setObjectId(co.getId());
                    aclList.add(acl);
                }
                List readerList = (List)readerSelectBox.getValue();
                for (Iterator i=readerList.iterator(); i.hasNext();) {
                    String principalId = (String)i.next();
                    ContentAcl acl = new ContentAcl();
                    acl.setRole("reader");
                    acl.setPrincipalId(principalId);
                    acl.setObjectId(co.getId());
                    aclList.add(acl);
                }
                String[] userManagerArray = userManagerSelectBox.getIds();
                for (int i=0; i<userManagerArray.length; i++) {
                    String principalId = userManagerArray[i];
                    ContentAcl acl = new ContentAcl();
                    acl.setRole("manager");
                    acl.setPrincipalId(principalId);
                    acl.setObjectId(co.getId());
                    aclList.add(acl);
                }
                String[] userEditorArray = userEditorSelectBox.getIds();
                for (int i=0; i<userEditorArray.length; i++) {
                    String principalId = userEditorArray[i];
                    ContentAcl acl = new ContentAcl();
                    acl.setRole("editor");
                    acl.setPrincipalId(principalId);
                    acl.setObjectId(co.getId());
                    aclList.add(acl);
                }
                String[] userAuthorArray = userAuthorSelectBox.getIds();
                for (int i=0; i<userAuthorArray.length; i++) {
                    String principalId = userAuthorArray[i];
                    ContentAcl acl = new ContentAcl();
                    acl.setRole("author");
                    acl.setPrincipalId(principalId);
                    acl.setObjectId(co.getId());
                    aclList.add(acl);
                }
                String[] userReaderArray = userReaderSelectBox.getIds();
                for (int i=0; i<userReaderArray.length; i++) {
                    String principalId = userReaderArray[i];
                    ContentAcl acl = new ContentAcl();
                    acl.setRole("reader");
                    acl.setPrincipalId(principalId);
                    acl.setObjectId(co.getId());
                    aclList.add(acl);
                }

                if (hasManager) {
                    contentManager.updateAcl(co.getId(), (ContentAcl[])aclList.toArray(new ContentAcl[0]), propagate, user);
                    return true;
                }
                else {
                    return false;
                }
            }
        }
        catch (Exception e) {
            throw new RuntimeException("Error saving ACL: " + e.toString());
        }
    }

    class ValidatorNotEmptyManager extends ValidatorNotEmpty {

        public ValidatorNotEmptyManager() {
        }

        public ValidatorNotEmptyManager(String name) {
            super(name);
        }

        public boolean validate(FormField formField) {
            boolean result = super.validate(formField);
            if (inheritCheckBox != null && inheritCheckBox.isChecked()) {
                result = true;
            }
            return result;
        }

    }

}
