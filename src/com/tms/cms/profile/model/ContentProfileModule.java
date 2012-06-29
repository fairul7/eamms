package com.tms.cms.profile.model;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import kacang.Application;
import kacang.model.DaoException;
import kacang.model.DaoQuery;
import kacang.model.DataObjectNotFoundException;
import kacang.model.DefaultModule;
import kacang.model.Module;
import kacang.stdui.ButtonGroup;
import kacang.stdui.CheckBox;
import kacang.stdui.DateField;
import kacang.stdui.Form;
import kacang.stdui.FormField;
import kacang.stdui.Hidden;
import kacang.stdui.Label;
import kacang.stdui.Radio;
import kacang.stdui.RichTextBox;
import kacang.stdui.SelectBox;
import kacang.stdui.TextBox;
import kacang.stdui.TextField;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.util.Log;
import kacang.util.UuidGenerator;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.SequencedHashMap;
import org.apache.commons.lang.StringUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import com.tms.cms.core.model.ContentException;
import com.tms.cms.core.model.ContentModule;
import com.tms.cms.core.model.ContentObject;
import com.tms.cms.profile.ui.ContentProfileForm;
import com.tms.util.FormatUtil;

// TODO: Searching profile data by fields

public class ContentProfileModule extends DefaultModule {

    public static final String APPLICATION_PROPERTY_CONTENT_PROFILE = "cms.content.profile";
    public static final String APPLICATION_PROPERTY_CONTENT_PROFILE_CLASSES = "cms.content.profile.classes";
    public static final String APPLICATION_PROPERTY_CONTENT_PROFILE_PARENT_CLASSES = "cms.content.profile.parent.classes";
    public static final String APPLICATION_PROPERTY_CONTENT_PROFILE_COMPULSORY = "cms.content.profile.compulsory";

    public static SimpleDateFormat sdf = new SimpleDateFormat(FormatUtil.getInstance().getLongDateTimeFormat());


    private static Set profileClassSet;
    private static Set profileParentClassSet;

    /**
     * Checks to see whether the content profiling feature is enabled.
     * @return
     */
    public static boolean isProfileEnabled() {
        Application app = Application.getInstance();
        return Boolean.valueOf(app.getProperty(APPLICATION_PROPERTY_CONTENT_PROFILE)).booleanValue();
    }

    /**
     * Checks to see if a certain content object class can be profiled e.g. Document
     * @param className
     * @return
     */
    public static boolean isProfiledContent(String className) {
        // check that feature is enabld
        Application app = Application.getInstance();
        if (className == null || !isProfileEnabled()) {
            return false;
        }

        if (profileClassSet == null) {
            profileClassSet = new HashSet();

            // retrieve set of valid content modules that support profiling
            Collection classList = new HashSet();
            String classes = app.getProperty(APPLICATION_PROPERTY_CONTENT_PROFILE_CLASSES);

            if (classes == null) {
                // look in all content modules
                Map moduleMap = app.getModuleMap();
                for (Iterator i = moduleMap.values().iterator(); i.hasNext();) {
                    Module module = (Module) i.next();
                    if (module instanceof ContentModule) {
                        ContentModule contentModule = (ContentModule) module;
                        Class[] classArray = contentModule.getContentObjectClasses();
                        if (classArray != null) {
                            classList.addAll(Arrays.asList(classArray));
                        }
                    }
                }
            }
            else {
                // tokenize string and determine valid classes
                StringTokenizer st = new StringTokenizer(classes, ";,\n ");
                while (st.hasMoreTokens()) {
                    String token = st.nextToken().trim();
                    try {
                        Class clazz = Class.forName(token);
                        classList.add(clazz);
                    }
                    catch (Exception e) {
                        Log.getLog(ContentProfileModule.class).error("Error loading profile class " + token, e);
                    }
                }
            }

            // filter out classes that don't implement ProfiledContent
            for (Iterator i=classList.iterator(); i.hasNext();) {
                Class clazz = (Class)i.next();
                try {
                    Object o = clazz.newInstance();
                    if (o instanceof ProfiledContent) {
                        profileClassSet.add(clazz.getName());
                    }
                }
                catch (Exception e) {
                    Log.getLog(ContentProfileModule.class).error("Error instantiating profile class " + clazz, e);
                }
            }
        }

        return profileClassSet.contains(className);
    }

    /**
     * Checks to see if a certain content object class is a profile parent,
     * i.e. a profile can be assigned to it e.g. Section
     * @param className
     * @return
     */
    public static boolean isProfiledContentParent(String className) {
        // check that feature is enabld
        Application app = Application.getInstance();
        if (className == null || !isProfileEnabled()) {
            return false;
        }

        if (profileParentClassSet == null) {
            profileParentClassSet = new HashSet();

            // retrieve set of valid content modules that support profiling
            Collection classList = new HashSet();
            String classes = app.getProperty(APPLICATION_PROPERTY_CONTENT_PROFILE_PARENT_CLASSES);

            if (classes == null) {
                // look in all content modules
                Map moduleMap = app.getModuleMap();
                for (Iterator i = moduleMap.values().iterator(); i.hasNext();) {
                    Module module = (Module) i.next();
                    if (module instanceof ContentModule) {
                        ContentModule contentModule = (ContentModule) module;
                        Class[] classArray = contentModule.getContentObjectClasses();
                        if (classArray != null) {
                            classList.addAll(Arrays.asList(classArray));
                        }
                    }
                }
            }
            else {
                // tokenize string and determine valid classes
                StringTokenizer st = new StringTokenizer(classes, ";,\n ");
                while (st.hasMoreTokens()) {
                    String token = st.nextToken().trim();
                    try {
                        Class clazz = Class.forName(token);
                        classList.add(clazz);
                    }
                    catch (Exception e) {
                        Log.getLog(ContentProfileModule.class).error("Error loading profile parent class " + token, e);
                    }
                }
            }

            // filter out classes that don't implement ProfiledContent
            for (Iterator i=classList.iterator(); i.hasNext();) {
                Class clazz = (Class)i.next();
                try {
                    Object o = clazz.newInstance();
                    if (o instanceof ProfiledContentParent) {
                        profileParentClassSet.add(clazz.getName());
                    }
                }
                catch (Exception e) {
                    Log.getLog(ContentProfileModule.class).error("Error instantiating profile parent class " + clazz, e);
                }
            }
        }

        return profileParentClassSet.contains(className);
    }


    public String addProfile(ContentProfile profile) throws ContentException {
        try {
            // generate ID
            String id = UuidGenerator.getInstance().getUuid();
            profile.setId(id);

            ContentProfileDao dao = (ContentProfileDao)getDao();
            dao.insertProfile(profile);
            return id;
        }
        catch (DaoException e) {
            throw new ContentException("Error creating content profile: " + e.getMessage());
        }
    }

    public void updateProfile(ContentProfile profile) throws ContentException {
        try {
            ContentProfileDao dao = (ContentProfileDao)getDao();
            dao.updateProfile(profile);
        }
        catch (DaoException e) {
            throw new ContentException("Error updating content profile: " + e.getMessage());
        }
    }

    public void deleteProfile(String profileId) throws ContentException {
        try {
            ContentProfileDao dao = (ContentProfileDao)getDao();
            dao.deleteProfile(profileId);
        }
        catch (DaoException e) {
            throw new ContentException("Error deleting content profile: " + e.getMessage());
        }
    }

    public ContentProfile getProfile(String profileId) throws DataObjectNotFoundException, ContentException {
        try {
            ContentProfileDao dao = (ContentProfileDao)getDao();
            ContentProfile profile = dao.selectById(profileId);

            // generate fields
            String def = profile.getDefinition();
            if (def != null) {
                ByteArrayInputStream bis = new ByteArrayInputStream(def.getBytes());
                List fieldList = parseProfile(profile.getId(), bis);
                profile.setFields(fieldList);
            }

            return profile;
        }
        catch (DataObjectNotFoundException e) {
            throw e;
        }
        catch (DaoException e) {
            throw new ContentException("Error retrieving content profile " + profileId);
        }
    }

    public ContentProfile getProfileByParent(String parentId) throws DataObjectNotFoundException, ContentException {
        try {
            ContentProfileDao dao = (ContentProfileDao)getDao();
            ContentProfile profile = dao.selectByParent(parentId);
            return profile;
        }
        catch (DataObjectNotFoundException e) {
            throw e;
        }
        catch (DaoException e) {
            throw new ContentException("Error retrieving content profile for parent " + parentId);
        }
    }

    public Collection getProfileList(String name, String sort, boolean desc, int start, int rows) throws ContentException {
        try {
            ContentProfileDao dao = (ContentProfileDao)getDao();
            return dao.selectProfileList(name, sort, desc, start, rows);
        }
        catch (DaoException e) {
            throw new ContentException("Error retrieving content profile list: " + e.getMessage());
        }
    }

    public int getProfileCount(String name) throws ContentException {
        try {
            ContentProfileDao dao = (ContentProfileDao)getDao();
            return dao.selectProfileCount(name);
        }
        catch (DaoException e) {
            throw new ContentException("Error retrieving content profile count: " + e.getMessage());
        }
    }

    /**
     * Assigns a profile to an array of content. Previous assignments for the profile are deleted.
     * @param profileId cannot be null
     * @param contentIdArray Set to null just to remove previous assignments
     * @throws ContentException
     */
    public void assignByProfile(String profileId, String[] contentIdArray) throws ContentException {
        if (profileId == null) {
            throw new ContentException("profileId cannot be null");
        }
        try {
            ContentProfileDao dao = (ContentProfileDao)getDao();
            dao.deleteProfileMap(profileId, null);

            if (contentIdArray != null) {
                for (int i=0; i<contentIdArray.length; i++) {
                    dao.insertProfileMap(profileId, contentIdArray[i]);
                }
            }
        }
        catch (Exception e) {
            throw new ContentException("Error assigning by profile");
        }
    }

    /**
     * Assigns a profile to a content object. Previous assignments for the profile are NOT deleted.
     * @param profileId Set to null to remove assignment
     * @param contentId ID of the content to assign, cannot be null.
     * @throws ContentException
     */
    public void assignByContent(String profileId, String contentId) throws ContentException {
        if (contentId == null) {
            throw new ContentException("contentId cannot be null");
        }
        try {
            ContentProfileDao dao = (ContentProfileDao)getDao();
            dao.deleteProfileMap(null, contentId);
            dao.insertProfileMap(profileId, contentId);
        }
        catch (Exception e) {
            throw new ContentException("Error assigning by content");
        }
    }

    public Collection searchProfileContent(DaoQuery query, String sort, boolean desc, int start, int rows) {
        return null;
    }

    public int searchProfileContentCount(DaoQuery query) {
        return 0;
    }

    /**
     * Creates a new
     * @param contentObject
     * @param previousVersion
     * @throws ContentException
     */
    public void checkOutProfileData(ContentObject contentObject, String previousVersion) throws ContentException {
        try {
            // load profile from dao
            ContentProfile profile = getProfileByParent(contentObject.getParentId());
            profile = getProfile(profile.getId());
            Collection fieldList = profile.getFields();

            if (fieldList != null) {
                ContentProfileDao dao = (ContentProfileDao)getDao();

                Map fieldMap = new SequencedHashMap();
                for (Iterator i=fieldList.iterator(); i.hasNext();) {
                    ContentProfileField field = (ContentProfileField)i.next();
                    fieldMap.put(field.getName(), field);
                }

                // load data
                Collection dataList = dao.selectData(contentObject.getId(), previousVersion);
                for (Iterator i=dataList.iterator(); i.hasNext();) {
                    Map row = (Map)i.next();
                    String name = (String)row.get("name");
                    if (name != null) {
                        ContentProfileField field = (ContentProfileField)fieldMap.get(name);
                        if (field != null) {
                            field.setValue((String)row.get("value"));
                        }
                    }
                }

                dao.updateData(contentObject.getId(), contentObject.getVersion(), profile);
            }
        }
        catch(DataObjectNotFoundException e) {
            Log.getLog(getClass()).debug("Profile form not found for content " + contentObject);
        }
        catch (ContentException e) {
            throw e;
        }
        catch (Exception e) {
            Log.getLog(getClass()).error("Error creating profile data for content " + contentObject, e);
            throw new ContentException("Error creating profile data: " + e.getMessage());
        }
    }

    public void deleteProfileData(String contentId, String version) throws ContentException {
        try {
            ContentProfileDao dao = (ContentProfileDao)getDao();
            dao.deleteData(contentId, version);
        }
        catch (Exception e) {
            Log.getLog(getClass()).error("Error deleting profile data for content " + contentId + " version " + version, e);
            throw new ContentException("Error deleting profile data: " + e.getMessage());
        }
    }

    /**
     *
     * @param contentId
     * @param version
     * @return A Collection of ContentProfileField objects.
     */
    public Collection getProfileData(String contentId, String version) throws ContentException {

        Collection results = new ArrayList();

        // load data
        Collection dataList = loadProfileData(contentId, version);
        for (Iterator i=dataList.iterator(); i.hasNext();) {
            Map row = (Map)i.next();
            ContentProfileField field = new ContentProfileField();
            field.setName((String)row.get("name"));
            field.setType((String)row.get("type"));
            
            //strip off the time section from the date field value
            String fieldType = (String)row.get("type");
            String value = (String)row.get("value");
            if(fieldType.equals(ContentProfileField.FORM_DATEFIELD)){
            	value = StringUtils.substringBefore(value, "12:00 AM");
            }
            
            field.setValue(value);
            field.setLabel((String)row.get("label"));
            results.add(field);
        }

        return results;
    }

    /**
     * Retrieves a String containing text that is to be indexed for searching.
     * @param contentId
     * @param version
     * @return
     * @throws ContentException
     */
    public String getIndexableProfileData(String contentId, String version) throws ContentException {
        String content = "";
        Collection fieldList = getProfileData(contentId, version);
        for (Iterator i=fieldList.iterator(); i.hasNext();) {
            ContentProfileField field = (ContentProfileField)i.next();
/*
            if (field.getType() == null
                    || ContentProfileField.FORM_TEXTFIELD.equals(field.getType())
                    || ContentProfileField.FORM_TEXTBOX.equals(field.getType())
                    || ContentProfileField.FORM_RICHTEXTBOX.equals(field.getType())) {

                content += nullToEmpty(field.getValue()) + "\n";
            }
*/
            content += nullToEmpty(field.getValue()) + "\n";
        }
        return content;
    }

    protected String nullToEmpty(String str) {
        return (str != null) ? str : "";
    }

//-- Methods for generating UI forms


    /**
     * Creates a ContentProfileForm for a profile and loads the appropriate profile data if the content ID and version is specified.
     * @param profileId
     * @param contentId
     * @param version
     * @return
     * @throws DataObjectNotFoundException
     * @throws ContentException
     */
    public ContentProfileForm loadForm(String profileId, String contentId, String version) throws DataObjectNotFoundException, ContentException {

        ContentProfileForm form = new ContentProfileForm("contentProfileForm");
        form.setMethod("POST");
        form.setColumns(2);
        form.setProfileId(profileId);
        form.setContentId(contentId);
        form.setVersion(version);

        // load profile from dao
        ContentProfile profile = getProfile(profileId);

        // generate form
        form.setProfileName(profile.getName());
        Collection fieldList = profile.getFields();
        if (fieldList != null) {

            // load data
            Map dataMap = new HashMap();
            if (contentId != null && version != null) {
                Collection dataList = loadProfileData(contentId, version);
                for (Iterator i=dataList.iterator(); i.hasNext();) {
                    Map row = (Map)i.next();
                    dataMap.put(row.get("name"), row.get("value"));
                }
            }

            for (Iterator i=fieldList.iterator(); i.hasNext();) {
                ContentProfileField field = (ContentProfileField)i.next();
                // generate widget
                FormField formField = generateWidget(field);
                // populate value
                String value = (String)dataMap.get(field.getName());
                if (value != null) {
                    field.setValue(value);
                }
                formField = populateWidgetValue(formField, field);
                Label label = new Label("l_" + field.getName(), field.getLabel());
                form.addChild(label);
                form.addChild(formField);
            }
        }

        return form;
    }

    /**
     * Stores the profile data for content based on a ContentProfileForm.
     * @param profileId
     * @param contentId
     * @param version
     * @param form
     * @throws DataObjectNotFoundException
     * @throws ContentException
     */
    public void storeForm(String profileId, String contentId, String version, ContentProfileForm form) throws DataObjectNotFoundException, ContentException {

        // load profile from dao
        ContentProfile profile = getProfile(profileId);

        try {

            // grab data from form
            profile = generateProfile(form, profile);

            // store profile data
            ContentProfileDao dao = (ContentProfileDao)getDao();
            dao.updateData(contentId, version, profile);

        }
        catch (Exception e) {
            Log.getLog(getClass()).error("Error storing profile data " + profileId + " for content " + contentId + " version " + version, e);
            throw new ContentException("Error storing profile data: " + e.getMessage());
        }


    }

    /**
     * Loads the data for a specified profile and content version.
     * @param contentId
     * @param version
     * @return A Collection of Map objects, containing keys "name" and "value"
     * @throws ContentException
     */
    protected Collection loadProfileData(String contentId, String version) throws ContentException {
        try {
            ContentProfileDao dao = (ContentProfileDao)getDao();
            Collection dataList = dao.selectData(contentId, version);
            return dataList;
        }
        catch (DaoException e) {
            Log.getLog(getClass()).error("Error retrieving profile data for content " + contentId + " version " + version, e);
            throw new ContentException("Error retrieving profile data for content " + contentId + " version " + version);
        }
    }

    /**
     * Parses an XML definition into corresponding ContentProfileField objects.
     * @param profileId
     * @param in
     * @return a List of ContentProfileField objects.
     */
    public List parseProfile(String profileId, InputStream in) {
        List fieldList = new ArrayList();
        try {

            SAXBuilder builder = new SAXBuilder();
            Document document = builder.build(in);
            Element root = document.getRootElement();

            // set peanut properties
            List propertyList = root.getChildren("field");
            for (Iterator i=propertyList.iterator(); i.hasNext();) {
                // obtains attributes
                Element prop = (Element)i.next();
                String name = prop.getAttributeValue("name");
                String label = prop.getAttributeValue("label");
                String widgetType = prop.getAttributeValue("type");
                String options = prop.getAttributeValue("options");
                String validator = prop.getAttributeValue("validator");
                String value = prop.getTextTrim();
                if (value == "")
                    value = null;

                // add property
                ContentProfileField field = new ContentProfileField();
                field.setName(name);
                field.setLabel(label);
                field.setType(widgetType);
                field.setOptions(options);
                field.setValidator(validator);
                field.setValue(value);
                fieldList.add(field);
            }
        }
        catch (Exception e) {
            Log.getLog(getClass()).error("Error parsing profile " + profileId, e);
        }

        return fieldList;
    }

    /**
     * Retrieves an appropriate FormField widget that represents a profileField.
     * @param profileField
     * @return
     */
    public FormField generateWidget(ContentProfileField profileField) {
        // create widget
        String widgetType = profileField.getType();
        String name = profileField.getName();
        String label = profileField.getLabel();
        Object value = profileField.getValue();
        String options = profileField.getOptions();
        Map optionMap = parseOptions(options);
        FormField field = null;
        if (ContentProfileField.FORM_CHECKBOX.equals(widgetType)) {
            if (options == null) {
                field = new CheckBox(name);
                ((CheckBox) field).setText(label);
            }
            else {
                field = new ButtonGroup(name);
                for (Iterator i = optionMap.keySet().iterator(); i.hasNext();) {
                    String key = (String) i.next();
                    if (key.trim().length() > 0) {
                        CheckBox checkbox = new CheckBox(key);
                        checkbox.setGroupName(name);
                        checkbox.setText((String) optionMap.get(key));
                        if (key.equals(value))
                            checkbox.setChecked(true);
                        field.addChild(checkbox);
                    }
                    else {
                        Log.getLog(getClass()).debug("ContentProfileField option key is empty for profileField " + name);
                    }
                }
            }
        }
        else if (ContentProfileField.FORM_RADIO.equals(widgetType)) {
            if (options == null) {
                field = new Radio(name);
                ((Radio) field).setText(label);
            }
            else {
                field = new ButtonGroup(name);
                for (Iterator i = optionMap.keySet().iterator(); i.hasNext();) {
                    String key = (String) i.next();
                    if (key.trim().length() > 0) {
                        Radio radio = new Radio(key);
                        radio.setGroupName(name);
                        radio.setText((String) optionMap.get(key));
                        if (key.equals(value))
                            radio.setChecked(true);
                        field.addChild(radio);
                    }
                    else {
                        Log.getLog(getClass()).debug("ContentProfileField option key is empty for profileField " + name);
                    }
                }
            }
        }
        else if (ContentProfileField.FORM_HIDDEN.equals(widgetType)) {
            field = new Hidden(name);
            if (value != null)
                field.setValue(value);
        }
        else if (ContentProfileField.FORM_LISTBOX.equals(widgetType)) {
            field = new SelectBox(name);
            if (options != null)
                ((SelectBox) field).setOptionMap(optionMap);
            ((SelectBox) field).setMultiple(true);
            ((SelectBox) field).setRows(7);
        }
        else if (ContentProfileField.FORM_SELECTBOX.equals(widgetType)) {
            field = new SelectBox(name);
            if (options != null)
                ((SelectBox) field).setOptionMap(optionMap);
        }
        else if (ContentProfileField.FORM_TEXTBOX.equals(widgetType)) {
            field = new TextBox(name);
            if (value != null)
                field.setValue(value);
        }
        else if (ContentProfileField.FORM_RICHTEXTBOX.equals(widgetType)) {
            field = new RichTextBox(name);
            if (value != null)
                field.setValue(value);
        }
        else if (ContentProfileField.FORM_TEXTFIELD.equals(widgetType)) {
            field = new TextField(name);
            if (value != null)
                field.setValue(value.toString());
        }
        else if (ContentProfileField.FORM_DATEFIELD.equals(widgetType)) {
            field = new DateField(name);
        }
        else if (ContentProfileField.FORM_NONE.equals(widgetType)) {

        }

        // add validator if necessary
        if (field != null) {
            addValidator(field, profileField);
        }

        return field;
    }

    /**
     * Adds an appropriate Validator to a FormField for a property.
     * @param field
     * @param property
     */
    protected void addValidator(FormField field, ContentProfileField property) {
        String validatorType = property.getValidator();
        if (ContentProfileField.VALIDATOR_NOT_EMPTY.equals(validatorType)) {
            ValidatorNotEmpty vne = new ValidatorNotEmpty("vne");
            field.addChild(vne);
        }
    }

    /**
     * Parses a String in the format key1=value1;key2=value2;... into a Map
     * @param options
     * @return empty Map if the String cannot be parsed
     */
    public static Map parseOptions(String options) {
        Map optionMap = new SequencedHashMap();
        if (options != null) {
            try {
                StringTokenizer st = new StringTokenizer(options, SelectBox.SEPARATOR_OPTION);
                while (st.hasMoreTokens()) {
                    String option = st.nextToken().trim();
                    StringTokenizer st2 = new StringTokenizer(option, SelectBox.SEPARATOR_VALUE);
                    String key = st2.nextToken().trim();
                    String value = st2.nextToken().trim();
                    optionMap.put(key, value);
                }
            }
            catch (Exception e) {
                Log.getLog(ContentProfileModule.class).debug("Error parsing options: " + options);
            }
        }
        return optionMap;
    }

    /**
     * Builds a ContentProfile from a Form widget, populating it properties from values within the Form.
     * @param form
     * @param profile
     * @return
     * @throws IllegalAccessException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     */
    protected ContentProfile generateProfile(Form form, ContentProfile profile) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Collection fields = profile.getFields();
        if (fields != null) {
            for (Iterator i = fields.iterator(); i.hasNext();) {
                ContentProfileField field = (ContentProfileField) i.next();
                String key = field.getName();
                FormField widget = (FormField) form.getChild(key);
                if (widget != null) {
                    String value = evalPropertyValue(widget, field);
                    PropertyUtils.setSimpleProperty(field, "value", value);
                }
            }
        }
        return profile;
    }

    /**
     * Evaluates the value of a FormField for a profileField.
     * @param formField
     * @param profileField
     * @return
     */
    protected String evalPropertyValue(FormField formField, ContentProfileField profileField) {
        String value = null;
        if (formField instanceof SelectBox) {
            StringBuffer selected = new StringBuffer();
            Map selectedOptions = ((SelectBox) formField).getSelectedOptions();
            int count = 0;
            for (Iterator i = selectedOptions.keySet().iterator(); i.hasNext(); count++) {
                String key = (String) i.next();
                if (count > 0) {
                    selected.append(SelectBox.SEPARATOR_OPTION);
                }
                selected.append(key);
            }
            value = selected.toString();
        }
        else if (formField instanceof ButtonGroup) {
            StringBuffer selected = new StringBuffer();
            Collection buttons = ((ButtonGroup) formField).getButtons();
            int count = 0;
            for (Iterator i = buttons.iterator(); i.hasNext();) {
                Object widget = i.next();
                if (widget instanceof CheckBox) {
                    CheckBox cb = (CheckBox)widget;
                    if (cb.isChecked()) {
                        if (count > 0) {
                            selected.append(SelectBox.SEPARATOR_OPTION);
                        }
                        selected.append(cb.getName());
                        count++;
                    }
                }
            }
            value = selected.toString();
        }
        else if (formField instanceof DateField) {
            Date date = ((DateField) formField).getDate();
            value = sdf.format(date);
        }
        else {
            Object fieldValue = formField.getValue();
            if (fieldValue != null) {
                value = fieldValue.toString();
            }
        }
        return value;
    }

    /**
     * Sets the value of a FormField from its corresponding profileField value.
     * @param formField
     * @param profileField
     * @return
     */
    public FormField populateWidgetValue(FormField formField, ContentProfileField profileField) {
        if (profileField == null || profileField.getValue() == null)
            return formField;

        String value = profileField.getValue();
        String widgetClass = formField.getClass().getName();

        if (CheckBox.class.getName().equals(widgetClass)) {
            boolean checked = (value != null && Boolean.valueOf(value.toString()).booleanValue()) || "1".equals(value);
            ((CheckBox) formField).setChecked(checked);
        }
        else if (DateField.class.getName().equals(widgetClass)) {
            try {
                Date tmpDate = sdf.parse(value.toString());
                ((DateField) formField).setDate(tmpDate);
            }
            catch (ParseException e) {
                Log.getLog(getClass()).debug("Error parsing date for formField " + formField);
            }
        }
        else if (SelectBox.class.getName().equals(widgetClass)) {
            if (value != null) {
                ArrayList selectedList = new ArrayList();
                StringTokenizer st = new StringTokenizer(value.toString(), SelectBox.SEPARATOR_OPTION);
                while (st.hasMoreTokens()) {
                    String option = st.nextToken().trim();
                    StringTokenizer st2 = new StringTokenizer(option, SelectBox.SEPARATOR_VALUE);
                    String key = st2.nextToken().trim();
                    selectedList.add(key);
                }
                String[] selected = (String[]) selectedList.toArray(new String[0]);
                ((SelectBox) formField).setSelectedOptions(selected);
            }
        }
        else if (ButtonGroup.class.getName().equals(widgetClass)) {
            if (value != null) {
                ArrayList selectedList = new ArrayList();
                StringTokenizer st = new StringTokenizer(value.toString(), SelectBox.SEPARATOR_OPTION);
                while (st.hasMoreTokens()) {
                    String option = st.nextToken().trim();
                    StringTokenizer st2 = new StringTokenizer(option, SelectBox.SEPARATOR_VALUE);
                    String key = st2.nextToken().trim();
                    selectedList.add(key);
                }
                Collection buttons = ((ButtonGroup) formField).getButtons();
                for (Iterator i = buttons.iterator(); i.hasNext();) {
                    Object widget = i.next();
                    if (widget instanceof CheckBox) {
                        CheckBox cb = (CheckBox)widget;
                        cb.setChecked(selectedList.contains(cb.getName()));
                    }
                }
            }
        }
        else {
            if (value != null)
                formField.setValue(value.toString());
            else
                formField.setValue("");
        }
        return formField;
    }

}
