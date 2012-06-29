package com.tms.cms.vsection.ui;

import com.tms.cms.article.Article;
import com.tms.cms.core.model.ContentManager;
import com.tms.cms.core.model.ContentObject;
import com.tms.cms.core.ui.ContentObjectForm;
import com.tms.cms.vsection.VSection;
import com.tms.cms.vsection.VSectionModule;
import com.tms.cms.document.Document;
import com.tms.cms.image.Image;
import com.tms.cms.section.Section;
import com.tms.util.FormatUtil;
import kacang.Application;
import kacang.services.security.User;
import kacang.stdui.*;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.ui.WidgetManager;
import org.apache.commons.collections.SequencedHashMap;

import java.util.*;

/**
 * Popup form for selecting content to be included in a VSection.
 */
public class VSectionContentObjectPopupForm extends ContentObjectForm {

    private String id;
    private String[] ids = null;
    private SortableSelectBox selectBox;
    private Table contentListTable;

    public VSectionContentObjectPopupForm() {
    }

    public VSectionContentObjectPopupForm(String name) {
        super(name);
    }

    public String getDefaultTemplate() {
        return "cms/admin/vSectionContentObjectPopupForm";
    }

    public void init(Event evt) {
        // remove existing children
        removeChildren();

        setMethod("POST");

        contentListTable = new ContentListTable("contentListTable");
        addChild(contentListTable);
        contentListTable.init();

        selectBox = new SortableSelectBox("selectBox");
        contentListTable.addChild(selectBox);
    }

    protected void populateFields(Event evt) {
        super.populateFields(evt);
        // populate values?
        VSection section;
        if(getContentObject() != null) {
            section = (VSection) getContentObject();
        } else {
            section = new VSection();
            setContentObject(section);
        }
        if (getIds() != null) {
            section.setIds(getIds());
        }
        Collection list = getContentObjectList();
        Map optionMap = new SequencedHashMap();
        for (Iterator i=list.iterator(); i.hasNext();) {
            ContentObject co = (ContentObject)i.next();
            optionMap.put(co.getId(), co.getName());
        }
//            optionMap.put("", "--- N/A ---");
        selectBox.setOptionMap(optionMap);
    }

    protected void populateContentObject(Event evt, ContentObject contentObject) {
        super.populateContentObject(evt, contentObject);
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

/*
        public Forward onSelection(Event evt) {
            String id = evt.getRequest().getParameter("id");
            ContentHelper.setId(evt, id);
            Forward forward = super.onSelection(evt);
            forward = new Forward("selection");
            return forward;
        }
*/
    }



    public class ContentListTableModel extends TableModel {

        public ContentListTableModel() {

            // add columns
            Application application = Application.getInstance();
            TableColumn nameColumn = new TableColumn("name", application.getMessage("general.label.name", "Name"));
            nameColumn.setUrlParam("id");
            addColumn(nameColumn);

            TableColumn classColumn = new TableColumn("className", application.getMessage("general.label.type", "Type"));
            classColumn.setFormat(new TableResourceFormat("cms.label_", null));
            addColumn(classColumn);

            TableColumn dateColumn = new TableColumn("date", application.getMessage("general.label.date", "Date"));
            dateColumn.setFormat(new TableDateFormat(FormatUtil.getInstance().getLongDateFormat()));
            addColumn(dateColumn);

            String contextPath = (String)getWidgetManager().getAttribute(WidgetManager.CONTEXT_PATH);
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

                classArray = new String[] {Section.class.getName(), Article.class.getName(), Document.class.getName(), Image.class.getName(), Page.class.getName()};

                name = (String)getFilterValue("name");

                List list = (List)getFilterValue("published");
                if (list != null && list.size() > 0) {
                    if (list.get(0).equals("true"))
                        published = Boolean.TRUE;
                    else if (list.get(0).equals("false"))
                        published = Boolean.FALSE;
                }

                // query
                Application application = Application.getInstance();
                ContentManager cm = (ContentManager)application.getModule(ContentManager.class);
                Collection children = cm.viewList(null, classArray, name, null, checkedOut, submitted, approved, archived, published, Boolean.FALSE, null, getSort(), isDesc(), getStart(), getRows(),ContentManager.USE_CASE_VIEW, getWidgetManager().getUser());
                return children;
            }
            catch(Exception e) {
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

                classArray = new String[] {Section.class.getName(), Article.class.getName(), Document.class.getName(), Image.class.getName(), Page.class.getName()};

                name = (String)getFilterValue("name");

                List list = (List)getFilterValue("published");
                if (list != null && list.size() > 0) {
                    if (list.get(0).equals("true"))
                        published = Boolean.TRUE;
                    else if (list.get(0).equals("false"))
                        published = Boolean.FALSE;
                }

                // query
                Application application = Application.getInstance();
                ContentManager cm = (ContentManager)application.getModule(ContentManager.class);
                int total = cm.viewCount(null, classArray, name, null, checkedOut, submitted, approved, archived, published, Boolean.FALSE, null, ContentManager.USE_CASE_VIEW, getWidgetManager().getUser());
                return total;
            }
            catch(Exception e) {
                throw new RuntimeException(e.toString());
            }
        }

        public Forward processAction(Event evt, String action, String[] selectedKeys) {
            if ("add".equals(action)) {
                Map optionMap = selectBox.getOptionMap();
                List keySet = new ArrayList(optionMap.keySet());
                for (int i=0; i<selectedKeys.length; i++) {
                    if (!keySet.contains(selectedKeys[i]) && selectedKeys[i].trim().length() > 0) {
                        keySet.add(0, selectedKeys[i]);
                    }
                }
                String[] newKeys = (String[])keySet.toArray(new String[0]);
                setIds(newKeys);
                populateFields(evt);
            }
            return new Forward("success");
        }
    }

    public Collection getContentObjectList() {
        User user = getWidgetManager().getUser();
        VSection contentObject= (VSection)getContentObject();
        return VSectionModule.getContentObjectList(contentObject, user);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String[] getIds() {
        return ids;
    }

    public void setIds(String[] ids) {
        this.ids = ids;
    }
}
