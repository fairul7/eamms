package com.tms.cms.core.ui;

import com.tms.cms.core.model.ContentException;
import com.tms.cms.core.model.ContentManager;
import com.tms.cms.core.model.ContentObject;
import kacang.Application;
import kacang.services.security.User;
import kacang.stdui.Button;
import kacang.stdui.Form;
import kacang.stdui.Panel;
import kacang.stdui.SortableSelectBox;
import kacang.stdui.event.FormEventAdapter;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.ui.Widget;
import kacang.ui.WidgetManager;
import kacang.util.Log;
import org.apache.commons.collections.SequencedHashMap;

import java.util.*;

public class ReorderContentObjectPanel extends Panel {

    private String id;

    private Form containerForm;
    private SortableSelectBox sortableSelectBox;
    private Button submitButton;
    private Button reorderButton;
    private Button cancelButton;

    public ReorderContentObjectPanel() {
    }

    public ReorderContentObjectPanel(String name) {
        super(name);
    }

    public void init() {
        addContainerForm();
    }

    public void onRequest(Event evt) {
        ContentObject co = ContentHelper.getContentObject(evt, getId());
        if (co != null) {
            setId(co.getId());
        }
        addContainerForm();
    }

    protected void addContainerForm() {
        // remove existing widgets
        removeChildren();

        // add container form
        containerForm = new Form("containerForm");
        containerForm.setMethod("POST");
        addChild(containerForm);

        // get content form
        try {
            if (getId() != null) {
                // retrieve from module
                User user = getWidgetManager().getUser();
                ContentManager cm = (ContentManager)Application.getInstance().getModule(ContentManager.class);
                Collection children = cm.viewList(null, null, null, getId(), null, null, null, null, null, Boolean.FALSE, null, null, false, 0, -1, null, user);

                // add select box
                sortableSelectBox = new SortableSelectBox("children");
                sortableSelectBox.setRows(10);
                sortableSelectBox.setMultiple(true);
                
                Map optionMap = new SequencedHashMap();
                Iterator i=children.iterator();
                
                while(i.hasNext()){
                	ContentObject tmp = (ContentObject)i.next();
                	if(!(tmp.getOrdering() == null || Integer.parseInt(tmp.getOrdering()) == -1)){
                		optionMap.put(tmp.getId(), tmp.getName());
                	}
                }
                optionMap.put("", "--- Not Ordered ---");
                i=children.iterator();
                while(i.hasNext()){
                	ContentObject tmp = (ContentObject)i.next();
                	if(tmp.getOrdering() == null || Integer.parseInt(tmp.getOrdering()) == -1){
                		optionMap.put(tmp.getId(), tmp.getName());
                	}
                }
                
                sortableSelectBox.setOptionMap(optionMap);
                containerForm.addChild(sortableSelectBox);

                // add buttons
                submitButton = new Button("submitButton");
                submitButton.setText("Submit");
                containerForm.addChild(submitButton);
                reorderButton = new Button("reorderButton");
                reorderButton.setText("Sort Alphabetically");
                containerForm.addChild(reorderButton);
                cancelButton = new Button(Form.CANCEL_FORM_ACTION);
                cancelButton.setText("Cancel");
                containerForm.addChild(cancelButton);

                // add form listener
                containerForm.addFormEventListener(new FormEventAdapter() {
                    public Forward onValidate(Event evt) {
                        // get selected button
                        WidgetManager wm = evt.getWidgetManager();
                        String buttonClicked = containerForm.findButtonClicked(evt);
                        Widget button = wm.getWidget(buttonClicked);
                        if (submitButton.equals(button)) {
                            reorderContentObject();
                            init();
                            return new Forward("submit");
                        }
                        else if (reorderButton.equals(button)) {
                            reorderContentObjectAlphabetically();
                            init();
                            return new Forward("submit");
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

    protected void reorderContentObject() {
        try {
            // get ordered keys
            List values = (List)sortableSelectBox.getValue();
            List childKeyList = new ArrayList();
            for (Iterator i=values.iterator(); i.hasNext();) {
                String key = (String)i.next();
                if (key.trim().length() > 0) {
                    childKeyList.add(key);
                }
                else {
                    break;
                }
            }
            String[] childKeys = (String[])childKeyList.toArray(new String[0]);

            // save
            User user = getWidgetManager().getUser();
            ContentManager cm = (ContentManager)Application.getInstance().getModule(ContentManager.class);
            cm.reorder(getId(), childKeys, user);

        }
        catch (Exception e) {
            throw new RuntimeException(e.toString());
        }
    }

    protected void reorderContentObjectAlphabetically() {
        try {
            ContentManager cm = (ContentManager)Application.getInstance().getModule(ContentManager.class);

            // get children
            User user = getWidgetManager().getUser();
            String parentId = getId();
            Collection children = cm.viewList(null, null, null, parentId, null, null, null, null, null, null, null, null, false, 0, -1, null, user);

            // get ordered keys
            List values = (List)sortableSelectBox.getValue();
            Map childKeyMap = new TreeMap();
            for (Iterator i=children.iterator(); i.hasNext();) {
                ContentObject co = (ContentObject)i.next();
                if (values.contains(co.getId())) {
                    childKeyMap.put(co.getName().toLowerCase(), co.getId());
                }
            }
            String[] childKeys = (String[])childKeyMap.values().toArray(new String[0]);

            // save
            cm.reorder(parentId, childKeys, user);

        }
        catch (Exception e) {
            throw new RuntimeException(e.toString());
        }
    }


}
