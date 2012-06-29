package com.tms.cms.profile.ui;

import kacang.stdui.*;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.ui.Forward;
import kacang.ui.Event;
import kacang.util.Log;
import kacang.Application;
import com.tms.cms.profile.model.ContentProfileField;
import com.tms.cms.profile.model.ContentProfile;
import com.tms.cms.profile.model.ContentProfileModule;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.output.XMLOutputter;
import org.jdom.input.SAXBuilder;
import org.apache.commons.collections.SequencedHashMap;

import java.util.*;
import java.io.IOException;
import java.io.ByteArrayInputStream;

public class ContentProfileDesignerPanel extends Panel {

    public static final String EVENT_EDIT = "edit";
    public static final String EVENT_UPDATE = "update";
    public static final String EVENT_DELETE = "delete";
    public static final String EVENT_UP = "up";
    public static final String EVENT_DOWN = "down";

    public static final String FORWARD_EDIT = "edit";
    public static final String FORWARD_UPDATE = "update";
    public static final String FORWARD_DELETE = "delete";
    public static final String FORWARD_MOVE = "move";
    public static final String FORWARD_SAVE = "save";
    public static final String FORWARD_ERROR = "error";

    protected String profileId;
    protected ContentProfile profile;
    protected ContentProfileField profileField;

    protected TextField tfName;
    protected TextField tfLabel;
    protected SelectBox sbType;
    protected TextBox tbOptions;
    protected TextBox tbValue;
    protected CheckBox cbRequired;
    protected Button bUpdate;
    protected Button bSave;
    protected Button bCancel;
    protected Button bDelete;

    protected Form fieldForm;
    protected Form profilePreviewForm;
    protected Form optionsForm;


    public ContentProfileDesignerPanel() {
    }

    public ContentProfileDesignerPanel(String name) {
        super(name);
    }

    public String getDefaultTemplate() {
        return "cms/admin/contentProfileDesignerForm";
    }

    public void init() {
        initProfile(null);
        initForm();
    }

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
        loadProfile();
    }

    public ContentProfile getProfile() {
        return profile;
    }

    public void setProfile(ContentProfile profile) {
        this.profile = profile;
    }

    protected void initForm() {

        removeChildren();

        fieldForm = new ContentProfileFieldForm("fieldForm");
        addChild(fieldForm);
        fieldForm.init();

        optionsForm = new ContentProfileOptionsForm("optionsForm");
        addChild(optionsForm);
        optionsForm.init();
    }

    protected ContentProfile initProfile(String definition) {
        profile = new ContentProfile();

        if (definition == null) {
            Element root = new Element("profile");
            XMLOutputter outputter = new XMLOutputter();
            definition = outputter.outputString(root);
        }

        profile.setDefinition(definition);

        ContentProfileModule cpm = (ContentProfileModule)Application.getInstance().getModule(ContentProfileModule.class);
        ByteArrayInputStream bis = new ByteArrayInputStream(definition.getBytes());
        List fieldList = cpm.parseProfile(getProfileId(), bis);
        profile.setFields(fieldList);

        if (profilePreviewForm != null) {
            removeChild(profilePreviewForm);
        }
        profilePreviewForm = getProfileForm();
        profilePreviewForm.setName("profileForm");
        addChild(profilePreviewForm);

        return profile;
    }

    public Forward actionPerformed(Event event) {
        try {
            if (EVENT_EDIT.equals(event.getType())) {
                String name = event.getRequest().getParameter("name");
                return editField(name);
            }
            else if (EVENT_DELETE.equals(event.getType())) {
                String name = event.getRequest().getParameter("name");
                return deleteField(name);
            }
            else if (EVENT_UP.equals(event.getType())) {
                String name = event.getRequest().getParameter("name");
                return moveFieldUp(name);
            }
            else if (EVENT_DOWN.equals(event.getType())) {
                String name = event.getRequest().getParameter("name");
                return moveFieldDown(name);
            }
            else {
                return super.actionPerformed(event);
            }
        }
        catch (Exception e) {
            Log.getLog(getClass()).error("Error processing event: " + e.getMessage(), e);
            throw new RuntimeException("Error processing event: " + e.getMessage());
        }
    }

    protected Forward editField(String name) throws IOException, JDOMException {

        // get xml profile
        String xmlDefinition = profile.getDefinition();
        SAXBuilder builder = new SAXBuilder();
        ByteArrayInputStream bis = new ByteArrayInputStream(xmlDefinition.getBytes());
        Document xmlDoc = builder.build(bis);
        Element sourceRoot = xmlDoc.getRootElement();

        // find element
        Element toEdit = null;
        for (Iterator i=new ArrayList(sourceRoot.getChildren("field")).iterator(); i.hasNext();) {
            Element child = (Element)i.next();
            if (name.equals(child.getAttributeValue("name"))) {
                toEdit = child;
                break;
            }
        }

        if (toEdit != null) {
            // populate form
            fieldForm.init();

            tfName.setValue(toEdit.getAttributeValue("name"));
            tfLabel.setValue(toEdit.getAttributeValue("label"));
            sbType.setSelectedOptions(new String[] { toEdit.getAttributeValue("type") });

            // handle \n delimited options
            String options = "";
            String optionsValue = toEdit.getAttributeValue("options");
            Map optionMap = ContentProfileModule.parseOptions(optionsValue);
            for (Iterator i=optionMap.keySet().iterator(); i.hasNext();) {
                String key = (String)i.next();
                options += key + "\n";
            }
            tbOptions.setValue(options);
            tbValue.setValue(toEdit.getText());
            cbRequired.setChecked(ContentProfileField.VALIDATOR_NOT_EMPTY.equals(toEdit.getAttributeValue("validator")));
        }

        return new Forward(FORWARD_EDIT);
    }

    protected Forward updateField(Event evt) throws JDOMException, IOException {

        // get form values
        String name = (String)tfName.getValue();
        String label = (String)tfLabel.getValue();
        String type = ContentProfileField.FORM_TEXTFIELD;
        List selected = (List)sbType.getValue();
        if (selected != null && selected.size() > 0) {
            type = selected.iterator().next().toString();
        }
        String options = "";
        String optionValue = (String)tbOptions.getValue();
        if (optionValue != null) {
            StringTokenizer st = new StringTokenizer(optionValue, "\n");
            while(st.hasMoreTokens()) {
                String token = st.nextToken().trim();
                options += token + SelectBox.SEPARATOR_VALUE + token + SelectBox.SEPARATOR_OPTION;
            }
        }

        String defaultValue = (String)tbValue.getValue();
        String validator = (cbRequired.isChecked()) ? ContentProfileField.VALIDATOR_NOT_EMPTY : null;

        // create xml element
        Element el = new Element("field");
        el.setAttribute("name", name);
        el.setAttribute("label", label);
        el.setAttribute("type", type);
        el.setAttribute("options", options);
        if (defaultValue != null) {
            el.setText(defaultValue);
        }
        if (validator != null) {
            el.setAttribute("validator", validator);
        }

        // get xml profile
        Element sourceRoot;
        Element targetRoot = new Element("profile");
        String xmlDefinition = profile.getDefinition();
        if (xmlDefinition != null) {
            SAXBuilder builder = new SAXBuilder();
            ByteArrayInputStream bis = new ByteArrayInputStream(xmlDefinition.getBytes());
            Document xmlDoc = builder.build(bis);
            sourceRoot = xmlDoc.getRootElement();
        }
        else {
            sourceRoot = new Element("profile");
        }

        // insert or replace element
        boolean inserted = false;
        for (Iterator i=new ArrayList(sourceRoot.getChildren("field")).iterator(); i.hasNext();) {
            Element child = (Element)i.next();
            child.detach();
            if (!name.equals(child.getAttributeValue("name"))) {
                targetRoot.addContent(child);
            }
            else {
                targetRoot.addContent(el);
                inserted = true;
            }
        }
        if (!inserted) {
            targetRoot.addContent(el);
        }

        // reset root element and set to profile
        XMLOutputter outputter = new XMLOutputter();
        String definition = outputter.outputString(targetRoot);
        profile = initProfile(definition);

        return new Forward(FORWARD_UPDATE);
    }

    protected Forward deleteField(String name) throws JDOMException, IOException {

        // get xml profile
        String xmlDefinition = profile.getDefinition();
        SAXBuilder builder = new SAXBuilder();
        ByteArrayInputStream bis = new ByteArrayInputStream(xmlDefinition.getBytes());
        Document xmlDoc = builder.build(bis);
        Element sourceRoot = xmlDoc.getRootElement();
        Element targetRoot = new Element("profile");

        // find and delete element
        for (Iterator i=new ArrayList(sourceRoot.getChildren("field")).iterator(); i.hasNext();) {
            Element child = (Element)i.next();
            child.detach();
            if (!name.equals(child.getAttributeValue("name"))) {
                targetRoot.addContent(child);
            }
        }

        // reset root element and set to profile
        xmlDoc.setRootElement(targetRoot);
        XMLOutputter outputter = new XMLOutputter();
        String definition = outputter.outputString(targetRoot);
        profile = initProfile(definition);

        return new Forward(FORWARD_DELETE);
    }

    protected Forward moveFieldUp(String name) throws IOException, JDOMException {
        // get xml profile
        String xmlDefinition = profile.getDefinition();
        SAXBuilder builder = new SAXBuilder();
        ByteArrayInputStream bis = new ByteArrayInputStream(xmlDefinition.getBytes());
        Document xmlDoc = builder.build(bis);
        Element sourceRoot = xmlDoc.getRootElement();
        Element targetRoot = new Element("profile");
        List newChildren = new ArrayList();

        // find and move element
        for (Iterator i=new ArrayList(sourceRoot.getChildren("field")).iterator(); i.hasNext();) {
            Element child = (Element)i.next();
            child.detach();
            if (name.equals(child.getAttributeValue("name"))) {
                int size = newChildren.size();
                if (size > 0)
                    --size;
                newChildren.add(size, child);
            }
            else {
                newChildren.add(child);
            }
        }
        targetRoot.setContent(newChildren);

        // reset root element and set to profile
        xmlDoc.setRootElement(targetRoot);
        XMLOutputter outputter = new XMLOutputter();
        String definition = outputter.outputString(targetRoot);
        profile = initProfile(definition);
        return new Forward(FORWARD_MOVE);
    }

    protected Forward moveFieldDown(String name) throws IOException, JDOMException {
        // get xml profile
        String xmlDefinition = profile.getDefinition();
        SAXBuilder builder = new SAXBuilder();
        ByteArrayInputStream bis = new ByteArrayInputStream(xmlDefinition.getBytes());
        Document xmlDoc = builder.build(bis);
        Element sourceRoot = xmlDoc.getRootElement();
        Element targetRoot = new Element("profile");

        // find and move element
        List origChildren = sourceRoot.getChildren("field");
        List newChildren = new ArrayList(origChildren.size());
        for (Iterator i=new ArrayList(origChildren).iterator(); i.hasNext();) {
            Element child = (Element)i.next();
            child.detach();
            if (name.equals(child.getAttributeValue("name"))) {
                if (i.hasNext()) {
                    Element nextEl = (Element)i.next();
                    nextEl.detach();
                    newChildren.add(nextEl);
                }
            }
            newChildren.add(child);
        }
        targetRoot.setContent(newChildren);

        // reset root element and set to profile
        xmlDoc.setRootElement(targetRoot);
        XMLOutputter outputter = new XMLOutputter();
        String definition = outputter.outputString(targetRoot);
        profile = initProfile(definition);
        return new Forward(FORWARD_MOVE);
    }

    protected ContentProfileForm getProfileForm() {
        ContentProfileForm form = new ContentProfileForm("contentProfileForm");
        form.setMethod("POST");
        form.setColumns(2);
        form.setProfileId(profileId);

        // generate form
        ContentProfileModule cpm = (ContentProfileModule)Application.getInstance().getModule(ContentProfileModule.class);
        Collection fieldList = profile.getFields();
        if (fieldList != null) {
            for (Iterator i=fieldList.iterator(); i.hasNext();) {
                ContentProfileField field = (ContentProfileField)i.next();
                // generate widget
                FormField formField = cpm.generateWidget(field);
                // populate value
                String value = field.getValue();
                if (value != null) {
                    field.setValue(value);
                }
                formField = cpm.populateWidgetValue(formField, field);
                Label label = new Label("l_" + field.getName(), field.getLabel());
                form.addChild(label);
                form.addChild(formField);
            }
        }
        return form;
    }

    protected void loadProfile() {
        if (getProfileId() != null) {
            try {
                Application app = Application.getInstance();
                ContentProfileModule cpm = (ContentProfileModule)app.getModule(ContentProfileModule.class);
                profile = cpm.getProfile(getProfileId());

                if (profilePreviewForm != null) {
                    removeChild(profilePreviewForm);
                }
                profilePreviewForm = getProfileForm();
                profilePreviewForm.setName("profileForm");
                addChild(profilePreviewForm);
            }
            catch (Exception e) {
                if (profilePreviewForm != null) {
                    removeChild(profilePreviewForm);
                }
                profile = null;
                Log.getLog(getClass()).error("Error retrieving profile " + getProfileId(), e);
                throw new RuntimeException("Error retrieving profile " + getProfileId());
            }
        }
        else {
            if (profilePreviewForm != null) {
                removeChild(profilePreviewForm);
                profilePreviewForm = null;
            }
        }
    }

    protected Forward saveProfile(Event evt) {
        try {
            Application app = Application.getInstance();
            ContentProfileModule cpm = (ContentProfileModule)app.getModule(ContentProfileModule.class);

            String profileId = getProfileId();
            if (profileId == null) {
                // should not happen
                return new Forward(FORWARD_ERROR);
            }
            else {
                // save existing profile
                ContentProfile existingProfile = cpm.getProfile(getProfileId());
                String newDefinition = profile.getDefinition();
                existingProfile.setDefinition(newDefinition);
                cpm.updateProfile(existingProfile);
                return new Forward(FORWARD_SAVE);
            }
        }
        catch (Exception e) {
            Log.getLog(getClass()).error("Error saving profile " + getProfileId(), e);
            throw new RuntimeException("Error saving profile " + getProfileId());
        }
    }

    protected class ContentProfileOptionsForm extends Form {

        public ContentProfileOptionsForm() {
        }

        public ContentProfileOptionsForm(String name) {
            super(name);
        }

        public void init() {
            setMethod("POST");
            initFields();
        }

        protected void initFields() {

            removeChildren();

            Application app = Application.getInstance();
            bSave = new Button("bSave", "Save Profile");
            bCancel = new Button(Form.CANCEL_FORM_ACTION, app.getMessage("general.label.cancel", "Cancel"));

            addChild(bSave);
            addChild(bCancel);
        }

        public Forward onValidate(Event event) {

            try {
                String button = findButtonClicked(event);
                if (bSave.getAbsoluteName().equals(button)) {
                    Forward f = saveProfile(event);
                    return f;
                }
                return super.onValidate(event);
            }
            catch (Exception e) {
                Log.getLog(getClass()).error("Error processing event: " + e.getMessage(), e);
                throw new RuntimeException("Error processing event: " + e.getMessage());
            }

        }

    }

    protected class ContentProfileFieldForm extends Form {

        public ContentProfileFieldForm() {
        }

        public ContentProfileFieldForm(String name) {
            super(name);
        }

        public String getDefaultTemplate() {
            return "cms/admin/contentProfileDesignerForm";
        }

        public void init() {
            setMethod("POST");
            initFields();
        }

        protected void initFields() {

            removeChildren();

            tfName = new TextField("tfName");
            tfName.addChild(new ValidatorNotEmpty("vName"));
            tfLabel = new TextField("tfLabel");
            tfLabel.addChild(new ValidatorNotEmpty("vLabel"));
            tbOptions = new TextBox("tbOptions");
            tbValue = new TextBox("tbValue");
            sbType = new SelectBox("sbType");
            Map typeMap = new SequencedHashMap();
            typeMap.put(ContentProfileField.FORM_TEXTFIELD, "Text Field");
            typeMap.put(ContentProfileField.FORM_TEXTBOX, "Text Box");
            typeMap.put(ContentProfileField.FORM_RICHTEXTBOX, "Rich Text Box");
            typeMap.put(ContentProfileField.FORM_DATEFIELD, "Date Field");
            typeMap.put(ContentProfileField.FORM_SELECTBOX, "Select Box");
            typeMap.put(ContentProfileField.FORM_LISTBOX, "List Box");
            typeMap.put(ContentProfileField.FORM_CHECKBOX, "Check Box");
            typeMap.put(ContentProfileField.FORM_RADIO, "Radio");
            sbType.setOptionMap(typeMap);
            sbType.addChild(new ValidatorNotEmpty("vType"));
            cbRequired = new CheckBox("cbRequired");
            bUpdate = new Button("bUpdate", "Update Field");
            bDelete = new Button("bDelete", "Delete");

            addChild(tfName);
            addChild(tfLabel);
            addChild(sbType);
            addChild(tbOptions);
            addChild(tbValue);
            addChild(cbRequired);
            addChild(bUpdate);
            addChild(bDelete);
        }

        public Forward onValidate(Event event) {

            try {
                String button = findButtonClicked(event);
                if (bUpdate.getAbsoluteName().equals(button)) {
                    Forward f = updateField(event);
                    initFields();
                    return f;
                }
                else if (bDelete.getAbsoluteName().equals(button)) {
                    String name = (String)tfName.getValue();
                    Forward f = deleteField(name);
                    initFields();
                    return f;
                }

                return super.onValidate(event);
            }
            catch (Exception e) {
                Log.getLog(getClass()).error("Error processing event: " + e.getMessage(), e);
                throw new RuntimeException("Error processing event: " + e.getMessage());
            }

        }


    }
}
