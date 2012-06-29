package com.tms.cms.core.ui;

import com.tms.cms.core.model.ContentException;
import com.tms.cms.core.model.ContentManager;
import com.tms.cms.core.model.ContentObject;
import com.tms.cms.core.model.ContentUtil;
import com.tms.cms.section.Section;
import com.tms.cms.vsection.VSection;
import com.tms.ekms.setup.model.SetupModule;
import com.tms.util.FormatUtil;
import kacang.Application;
import kacang.services.security.User;
import kacang.stdui.*;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.ui.WidgetManager;
import kacang.util.Log;
import org.apache.commons.collections.SequencedHashMap;

import java.util.*;

public class ContentTemplateForm extends Form {

    public static final String FORWARD_SITE_TEMPLATE = "siteTemplate";
    public static final String FORWARD_PAGE_TEMPLATE = "pageTemplate";
    public static final String FORWARD_FAILURE = "failure";

    private String templateName;
    private String[] ids = null;
    private SelectBox siteTemplateBox;
    private SelectBox pageTemplateBox;
    private SelectBox selectedContentBox;
    private Table contentListTable;
    private Button siteTemplateButton;
    private Button pageTemplateButton;
    private Button removeButton;

    public ContentTemplateForm() {
    }

    public ContentTemplateForm(String name) {
        super(name);
    }

    public String getDefaultTemplate() {
        return "cms/admin/contentTemplateForm";
    }

    public void init() {
        // remove existing children
        removeChildren();

        contentListTable = new ContentListTable("contentListTable");
        contentListTable.setWidth("100%");
        addChild(contentListTable);
        contentListTable.init();

        // set list of available site templates
        Application application = Application.getInstance();
        siteTemplateBox = new SelectBox("siteTemplateBox");
        Map optionMap = new SequencedHashMap();
        optionMap.put("", "--- "+application.getMessage("general.label.pleaseSelect", "Please Select")+" ---");
        optionMap.putAll(ContentUtil.getAvailableSiteTemplates());
        siteTemplateBox.setOptionMap(optionMap);
        addChild(siteTemplateBox);

        siteTemplateButton = new Button("siteTemplateButton");
        siteTemplateButton.setText(application.getMessage("general.label.select", "Select"));
        addChild(siteTemplateButton);

        // set list of available page templates
        pageTemplateBox = new SelectBox("pageTemplateBox");
        Map pageOptionMap = new SequencedHashMap();
        pageOptionMap.put("", "--- "+application.getMessage("general.label.pleaseSelect", "Please Select")+" ---");
        pageOptionMap.putAll(ContentUtil.getAvailablePageTemplates());
        pageTemplateBox.setOptionMap(pageOptionMap);
        addChild(pageTemplateBox);

        pageTemplateButton = new Button("pageTemplateButton");
        pageTemplateButton.setText(application.getMessage("general.label.select", "Select"));
        addChild(pageTemplateButton);

        removeButton = new Button("removeButton");
        removeButton.setText(application.getMessage("general.label.removeSelected", "Remove Selected"));
        addChild(removeButton);

        selectedContentBox = new SelectBox("selectedContentBox");
        selectedContentBox.setRows(5);
        selectedContentBox.setMultiple(true);
        addChild(selectedContentBox);
    }

    public void onRequest(Event evt) {
        populateSelectedContentBox();
    }

    public Forward onValidate(Event evt) {
        Forward fwd = null;
        String button = findButtonClicked(evt);
        if (siteTemplateButton.getAbsoluteName().equals(button)) {
            List list = (List) siteTemplateBox.getValue();
            String selected = (String) list.get(0);
            if (selected.trim().length() > 0) {
                // call module to update site template
                try {
                    SetupModule setup = (SetupModule)Application.getInstance().getModule(SetupModule.class);
                    setup.save(ContentManager.SETUP_PROPERTY_SITE_TEMPLATE, selected);
                } catch (Exception e) {
                    Log.getLog(getClass()).error(e.toString(), e);
                }
                // reset page templates
                Application application = Application.getInstance();
                Map pageOptionMap = new SequencedHashMap();
                pageOptionMap.put("", "--- "+application.getMessage("general.label.pleaseSelect", "Please Select")+" ---");
                pageOptionMap.putAll(ContentUtil.getAvailablePageTemplates());
                pageTemplateBox.setOptionMap(pageOptionMap);
                selectedContentBox.setOptionMap(new HashMap());
                selectedContentBox.setValue(new ArrayList());
                setTemplateName(null);
                fwd = new Forward(FORWARD_SITE_TEMPLATE);
            }
        } else if (pageTemplateButton.getAbsoluteName().equals(button)) {
            List list = (List) pageTemplateBox.getValue();
            String selected = (String) list.get(0);
            if (selected.trim().length() > 0) {
                setTemplateName(selected);
            }
            fwd = new Forward(FORWARD_PAGE_TEMPLATE);
        } else if (removeButton.getAbsoluteName().equals(button)) {
            // get selected
            List list = (List) selectedContentBox.getValue();
            if (list != null) {
                if (list.size() > 0 && !"".equals(list.get(0))) {
                    String[] ids = (String[]) list.toArray(new String[0]);
                    // call module to update page templates
                    try {
                        ContentManager cm = (ContentManager) Application.getInstance().getModule(ContentManager.class);
                        cm.updateContentPageTemplate(null, ids);
                    } catch (ContentException e) {
                        Log.getLog(getClass()).error(e.toString(), e);
                    }
                }
            }
            fwd = new Forward(FORWARD_PAGE_TEMPLATE);
        }
        populateSelectedContentBox();
        return fwd;
    }

    protected void populateSelectedContentBox() {
        // set current site template
        ContentManager cm = (ContentManager) Application.getInstance().getModule(ContentManager.class);
        String siteTemplate = cm.getSiteTemplate("text/html", null);
        siteTemplateBox.setSelectedOptions(new String[]{siteTemplate});

        // get ids from module
        if (getTemplateName() != null && getTemplateName().trim().length() > 0) {
            try {
                Collection idList = cm.getContentByTemplate(getTemplateName());
                setIds((String[]) idList.toArray(new String[0]));
            } catch (ContentException e) {
                Log.getLog(getClass()).error(e.toString(), e);
            }
        } else {
            setIds(null);
        }

        // populate values?
        Collection list = new ArrayList();
        if (getIds() != null && getIds().length > 0) {
            list = getContentObjectList();
        }
        Map optionMap = new SequencedHashMap();
        for (Iterator i = list.iterator(); i.hasNext();) {
            ContentObject co = (ContentObject) i.next();
            optionMap.put(co.getId(), co.getName());
        }
        if (optionMap.size() == 0) {
	        Application application = Application.getInstance();
            optionMap.put("", "--- "+application.getMessage("general.label.none", "None")+" ---");
        }
        selectedContentBox.setOptionMap(optionMap);
    }

    public class ContentListTable extends Table {
        public ContentListTable() {
        }

        public ContentListTable(String name) {
            super(name);
        }

        public void init() {
            setSort("date");
            setDesc(true);
            setModel(new ContentListTableModel());
        }

    }


    public class ContentListTableModel extends TableModel {

        public ContentListTableModel() {

            // add columns
            Application application = Application.getInstance();
            TableColumn nameColumn = new TableColumn("name", application.getMessage("general.label.name", "Name"));
            addColumn(nameColumn);

            TableColumn classColumn = new TableColumn("className", application.getMessage("general.label.type", "Type"));
            classColumn.setFormat(new TableResourceFormat("cms.label_", null));
            addColumn(classColumn);

            TableColumn dateColumn = new TableColumn("date", application.getMessage("general.label.date", "Date"));
            dateColumn.setFormat(new TableDateFormat(FormatUtil.getInstance().getLongDateFormat()));
            addColumn(dateColumn);

            String contextPath = (String) getWidgetManager().getAttribute(WidgetManager.CONTEXT_PATH);
            TableFormat booleanFormat = new TableBooleanFormat("<img src=\"" + contextPath + "/common/table/booleantrue.gif\">", "");

            TableColumn publishedColumn = new TableColumn("published", application.getMessage("general.label.published", "Published"));
            publishedColumn.setFormat(booleanFormat);
            addColumn(publishedColumn);

            // add actions
            addAction(new TableAction("add", application.getMessage("general.label.add", "Add")));

            // add filters
            TableFilter nameFilter = new TableFilter("name", application.getMessage("general.label.name", "Name"));
            TextField nameField = new TextField("name");
            nameFilter.setWidget(nameField);
            addFilter(nameFilter);

            TableFilter publishedFilter = new TableFilter("published", application.getMessage("general.label.published", "Published"));
            SelectBox publishedSelectBox = new SelectBox("publishedSelectBox");
            publishedSelectBox.setOptions("any="+application.getMessage("general.label.any", "Any")+";true="+application.getMessage("general.label.published", "Published")+";false="+application.getMessage("general.label.notPublished", "Not Published")+"");
            List selected = new ArrayList();
            selected.add("true");
            publishedSelectBox.setValue(selected);
            publishedFilter.setWidget(publishedSelectBox);
            addFilter(publishedFilter);

        }

        public String getTableRowKey() {
            return "id";
        }

        public Collection getTableRows() {
            try {
                // process filters
                String name = null;
                Boolean checkedOut = null;
                Boolean submitted = null;
                Boolean approved = null;
                Boolean published = null;
                Boolean archived = null;
                String[] classArray = null;

                classArray = new String[]{Section.class.getName(), VSection.class.getName(), com.tms.cms.page.Page.class.getName()};

                name = (String) getFilterValue("name");

                List list = (List) getFilterValue("published");
                if (list != null && list.size() > 0) {
                    if (list.get(0).equals("true"))
                        published = Boolean.TRUE;
                    else if (list.get(0).equals("false"))
                        published = Boolean.FALSE;
                }

                // query
                Application application = Application.getInstance();
                ContentManager cm = (ContentManager) application.getModule(ContentManager.class);
                Collection children = cm.viewList(null, classArray, name, null, checkedOut, submitted, approved, archived, published, Boolean.FALSE, null, getSort(), isDesc(), getStart(), getRows(), ContentManager.USE_CASE_VIEW, getWidgetManager().getUser());
                return children;
            } catch (Exception e) {
                throw new RuntimeException(e.toString());
            }
        }

        public int getTotalRowCount() {
            try {
                // process filters
                String name = null;
                Boolean checkedOut = null;
                Boolean submitted = null;
                Boolean approved = null;
                Boolean published = null;
                Boolean archived = null;
                String[] classArray = null;

                classArray = new String[]{Section.class.getName(), VSection.class.getName(), com.tms.cms.page.Page.class.getName()};

                name = (String) getFilterValue("name");

                List list = (List) getFilterValue("published");
                if (list != null && list.size() > 0) {
                    if (list.get(0).equals("true"))
                        published = Boolean.TRUE;
                    else if (list.get(0).equals("false"))
                        published = Boolean.FALSE;
                }

                // query
                Application application = Application.getInstance();
                ContentManager cm = (ContentManager) application.getModule(ContentManager.class);
                int total = cm.viewCount(null, classArray, name, null, checkedOut, submitted, approved, archived, published, Boolean.FALSE, null, ContentManager.USE_CASE_VIEW, getWidgetManager().getUser());
                return total;
            } catch (Exception e) {
                throw new RuntimeException(e.toString());
            }
        }

        public Forward processAction(Event evt, String action, String[] selectedKeys) {
            if ("add".equals(action)) {
                Map optionMap = selectedContentBox.getOptionMap();
                List keySet = new ArrayList(optionMap.keySet());
                for (int i = 0; i < selectedKeys.length; i++) {
                    if (!keySet.contains(selectedKeys[i]) && selectedKeys[i].trim().length() > 0) {
                        keySet.add(0, selectedKeys[i]);
                    }
                }
                String[] newKeys = (String[]) keySet.toArray(new String[0]);
                setIds(newKeys);

                // save
                try {
                    updateContentPageTemplate(newKeys);
                    return new Forward(FORWARD_PAGE_TEMPLATE);
                } catch (ContentException e) {
                    Log.getLog(getClass()).error(e.toString(), e);
                    return new Forward(FORWARD_FAILURE);
                }
            } else {
                return null;
            }
        }

    }

    protected Collection getContentObjectList() {
        User user = getWidgetManager().getUser();
        try {
            String[] keys = getIds();
            ContentManager cm = (ContentManager) Application.getInstance().getModule(ContentManager.class);
            return cm.viewList(keys, null, null, null, null, null, null, null, null, Boolean.FALSE, null, null, false, 0, -1, ContentManager.USE_CASE_VIEW, user);
        } catch (ContentException e) {
            Log.getLog(getClass()).error(e.toString(), e);
            return new ArrayList();
        }
    }

    protected void updateContentPageTemplate(String[] newKeys) throws ContentException {
        ContentManager cm = (ContentManager) Application.getInstance().getModule(ContentManager.class);
        cm.updateContentPageTemplate(getTemplateName(), newKeys);
        populateSelectedContentBox();
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String[] getIds() {
        return ids;
    }

    public void setIds(String[] ids) {
        this.ids = ids;
    }
}
