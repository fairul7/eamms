package com.tms.cms.core.ui;

import com.tms.cms.core.model.ContentException;
import com.tms.cms.core.model.ContentManager;
import com.tms.cms.core.model.ContentObject;
import com.tms.cms.section.ui.SectionSelectBox;
import com.tms.util.FormatUtil;
import kacang.Application;
import kacang.services.security.User;
import kacang.stdui.*;
import kacang.ui.WidgetManager;
import kacang.util.Log;
import org.apache.commons.collections.SequencedHashMap;

import java.util.*;

public class ContentPopupSelectBox extends PopupSelectBox {

    ContentListTable table;
    String[] classes;
    boolean showParentFilter;

    public ContentPopupSelectBox() {
        super();
        table = new ContentListTable();
    }

    public ContentPopupSelectBox(String name) {
        super(name);
        table = new ContentListTable();
    }

    public String[] getClasses() {
        return classes;
    }

    public void setClasses(String[] classes) {
        this.classes = classes;
    }

    public boolean isShowParentFilter() {
        return showParentFilter;
    }

    public void setShowParentFilter(boolean showParentFilter) {
        this.showParentFilter = showParentFilter;
    }

    protected Table initPopupTable() {
        return table;
    }

    protected Map generateOptionMap(String[] ids) {
        Map optionMap = new SequencedHashMap();
        try {
            ContentManager cm = (ContentManager) Application.getInstance().getModule(ContentManager.class);
            User user = getWidgetManager().getUser();
            Collection list = cm.viewListWithContents(ids, classes, null, null, null, null, null, null, null, Boolean.FALSE, null, null, false, 0, -1, null, user);
            Map tmpMap = new HashMap();
            for (Iterator i = list.iterator(); i.hasNext();) {
                ContentObject co = (ContentObject) i.next();
                String name = co.getName();
                if (name != null && name.length() > 20)
                    name = name.substring(0, 20) + "..";
                tmpMap.put(co.getId(), name);
            }
            for (int j = 0; j < ids.length; j++)
                optionMap.put(ids[j], tmpMap.get(ids[j]));
        }
        catch (ContentException e) {
            Log.getLog(getClass()).error("Error retrieving content", e);
        }
        return optionMap;
    }

    public class ContentListTable extends PopupSelectBoxTable {
        ContentListTableModel model;

        public ContentListTable() {
        }

        public ContentListTable(String name) {
            super(name);
        }

        public void init() {
            model = new ContentListTableModel();

            setSort("date");
            setDesc(true);
            setModel(model);
        }
    }

    public class ContentListTableModel extends PopupSelectBoxTableModel {

        public ContentListTableModel() {
            /* Columns */
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

            /* Actions */
            addAction(new TableAction(FORWARD_SELECT, application.getMessage("general.label.add", "Add")));

            /* Filters */
            TableFilter nameFilter = new TableFilter("name");
            TextField nameField = new TextField("name");
            nameFilter.setWidget(nameField);
            addFilter(nameFilter);

            if (classes == null) {
                TableFilter contentType = new TableFilter("contentType");
                SelectBox filterType = new SelectBox("filterType");
                filterType.addOption("-1", "All Content Types");
                filterType.addOption("com.tms.cms.article.Article", "Articles");
                filterType.addOption("com.tms.cms.section.Section", "Sections");
                filterType.addOption("com.tms.cms.document.Document", "Documents");
                contentType.setWidget(filterType);
                addFilter(contentType);
            }

            if (showParentFilter) {
                TableFilter parent = new TableFilter("parent");
                SectionSelectBox filterParent = new SectionSelectBox("filterParent");
                filterParent.setWidgetManager(getWidgetManager());
                parent.setWidget(filterParent);
                addFilter(parent);
            }

            TableFilter publishedFilter = new TableFilter("published");
            SelectBox publishedSelectBox = new SelectBox("publishedSelectBox");
            publishedSelectBox.setOptions("any=" + application.getMessage("general.label.any", "Any") + ";true=" + application.getMessage("general.label.published", "Published") + ";false=" + application.getMessage("general.label.notPublished", "Not Published") + "");
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
                String name = null;
                Boolean checkedOut = null;
                Boolean submitted = null;
                Boolean approved = null;
                Boolean published = null;
                Boolean archived = null;
                String[] classArray = null;
                String parentId = null;

                if (classes != null) {
                    classArray = classes;
                }
                if (!("-1".equals(getTypeFilter()))) {
                    classArray = new String[]{getTypeFilter()};
                }
                if (showParentFilter && !("-1".equals(getParentFilter()))) {
                    parentId = getParentFilter();
                }

                name = (String) getFilterValue("name");

                List list = (List) getFilterValue("published");
                if (list != null && list.size() > 0) {
                    if (list.get(0).equals("true"))
                        published = Boolean.TRUE;
                    else if (list.get(0).equals("false"))
                        published = Boolean.FALSE;
                }

                Application application = Application.getInstance();
                ContentManager cm = (ContentManager) application.getModule(ContentManager.class);
                Collection children = cm.viewList(null, classArray, name, parentId, checkedOut, submitted, approved, archived, published, Boolean.FALSE, null, getSort(), isDesc(), getStart(), getRows(), null, getWidgetManager().getUser());
                return children;
            }
            catch (Exception e) {
                throw new RuntimeException(e.toString());
            }
        }

        public int getTotalRowCount() {
            try {
                String name = null;
                Boolean checkedOut = null;
                Boolean submitted = null;
                Boolean approved = null;
                Boolean published = null;
                Boolean archived = null;
                String[] classArray = null;
                String parentId = null;


                if (classes != null) {
                    classArray = classes;
                }
                if (!("-1".equals(getTypeFilter()))) {
                    classArray = new String[]{getTypeFilter()};
                }
                if (showParentFilter && !("-1".equals(getParentFilter()))) {
                    parentId = getParentFilter();
                }

                name = (String) getFilterValue("name");

                List list = (List) getFilterValue("published");
                if (list != null && list.size() > 0) {
                    if (list.get(0).equals("true"))
                        published = Boolean.TRUE;
                    else if (list.get(0).equals("false"))
                        published = Boolean.FALSE;
                }

                Application application = Application.getInstance();
                ContentManager cm = (ContentManager) application.getModule(ContentManager.class);
                int total = cm.viewCount(null, classArray, name, parentId, checkedOut, submitted, approved, archived, published, Boolean.FALSE, null, null, getWidgetManager().getUser());
                return total;
            }
            catch (Exception e) {
                throw new RuntimeException(e.toString());
            }
        }

        public String getTypeFilter() {
            SelectBox select = (SelectBox) getFilter("contentType").getWidget();
            if (select.getSelectedOptions() != null && select.getSelectedOptions().size() > 0)
                return (String) select.getSelectedOptions().keySet().iterator().next();
            return "-1";
        }

        public String getParentFilter() {
            SectionSelectBox select = (SectionSelectBox) getFilter("parent").getWidget();
            if (select.getSelectedOptions() != null && select.getSelectedOptions().size() > 0)
                return (String) select.getSelectedOptions().keySet().iterator().next();
            return "-1";
        }
    }
}
