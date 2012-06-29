package com.tms.cms.core.ui;

import com.tms.cms.core.model.ContentException;
import com.tms.cms.core.model.ContentManager;
import com.tms.cms.core.model.ContentObject;
import com.tms.cms.section.Section;
import kacang.Application;
import kacang.model.DataObjectNotFoundException;
import kacang.services.security.User;
import kacang.stdui.Button;
import kacang.stdui.Form;
import kacang.stdui.Panel;
import kacang.ui.Event;
import kacang.ui.EventListener;
import kacang.ui.Forward;
import kacang.util.Log;

import java.util.Iterator;

/**
 * Created by IntelliJ IDEA.
 * User: julian
 * Date: Mar 21, 2003
 * Time: 4:44:04 PM
 * To change this template use Options | File Templates.
 */
public class MoveContentObjectPanel extends Panel {

    private String id;
    private String[] selectedKeys;

    private Form containerForm;
//    private ContentObjectView contentObjectView;

    private ContentTree contentTree;
    private Button cancelButton;

    public MoveContentObjectPanel() {
    }

    public MoveContentObjectPanel(String name) {
        super(name);
    }

    public void init() {
        // remove existing widgets
        removeChildren();

        // add content specific form
        addContainerForm();
    }

    public void onRequest(Event evt) {
        ContentObject co = ContentHelper.getContentObject(evt, getId());
        if (co != null) {
            setId(co.getId());
        }
        init();

        // HACK: check request for multiple object keys
        selectedKeys = (String[])evt.getRequest().getAttribute("MoveContentObjectKeys");
    }

    protected void addContainerForm() {
        try {
            if (getId() != null) {
                // retrieve from module
                User user = getWidgetManager().getUser();
                ContentManager cm = (ContentManager)Application.getInstance().getModule(ContentManager.class);
                ContentObject contentObject = cm.view(getId(), user);

/*
                // retrieve specific class
                ContentModule module = (ContentModule)Application.getInstance().getModule(contentObject.getContentModuleClass());
                contentObjectView = module.getContentObjectView(contentObject.getClass());

                // initialize content view
                contentObjectView.setName("contentObjectView");
                contentObjectView.init();
                if (contentObject != null) {
                    contentObjectView.setContentObject(contentObject);
                }

                // add content view
                addChild(contentObjectView);
*/

                // add tree
                contentTree = new MoveContentTree("moveContentTree");
                contentTree.setContentObjectClasses(new String[] {Section.class.getName()});
                contentTree.setSelectedId(contentObject.getParentId());
                addChild(contentTree);

                // add container form
                containerForm = new Form("containerForm");
                addChild(containerForm);

                // add buttons
                Application application = Application.getInstance();
                cancelButton = new Button(Form.CANCEL_FORM_ACTION);
                cancelButton.setText(application.getMessage("general.label.cancel", "Cancel"));
                containerForm.addChild(cancelButton);

                // add listener
                contentTree.addEventListener(new EventListener() {
                    public Forward actionPerformed(Event evt) {
                        String selectedId = contentTree.getSelectedId();
                        moveContentObject(evt, selectedId);
                        return new Forward("selection");
                    }
                });

                // add forwards
                for (Iterator i=getForwardMap().values().iterator(); i.hasNext();) {
                    Forward f = (Forward)i.next();
                    containerForm.addForward(f);
                    contentTree.addForward(f);
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    protected void moveContentObject(Event evt, String parentId) {
        Application application = Application.getInstance();
        ContentManager contentManager = (ContentManager)application.getModule(ContentManager.class);
        try {
            // HACK: check request for multiple object keys
            if (selectedKeys == null) {
                // move
                User user = getWidgetManager().getUser();
                contentManager.move(getId(), parentId, user);
            }
            else {
                // move selected keys
                User user = getWidgetManager().getUser();
                for (int i=0; i<selectedKeys.length; i++) {
                    try {
                        contentManager.move(selectedKeys[i], parentId, user);
                    }
                    catch (ContentException e) {
                        ;
                    }
                }
            }
        }
        catch (Exception e) {
            throw new RuntimeException(e.toString());
        }
    }

    public class MoveContentTree extends ContentTree {

        public MoveContentTree() {
        }

        public MoveContentTree(String name) {
            super(name);
        }

        public void onRequest(Event evt) {
        }

        public Object getModel() {
            Application app = Application.getInstance();
            ContentManager contentManager = (ContentManager)app.getModule(ContentManager.class);
            try {
                // get current user
                User user = getWidgetManager().getUser();

                // get content tree root
                ContentObject root = contentManager.viewTreeWithOrphans(getRootId(), getContentObjectClasses(), isIncludeContents(), ContentManager.USE_CASE_MOVE, user);

                return root;
            }
            catch (DataObjectNotFoundException e) {
                Log.getLog(getClass()).error(e.toString(), e);
                return null;
            }
            catch (ContentException e) {
                Log.getLog(getClass()).error(e.toString(), e);
                return null;
            }
        }

    }

}
