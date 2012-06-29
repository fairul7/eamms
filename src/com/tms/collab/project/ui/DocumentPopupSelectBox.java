package com.tms.collab.project.ui;

import com.tms.cms.article.Article;
import com.tms.cms.core.model.ContentException;
import com.tms.cms.core.model.ContentManager;
import com.tms.cms.core.model.ContentObject;
import com.tms.cms.document.Document;
import com.tms.cms.image.Image;
import com.tms.cms.section.Section;
import com.tms.util.FormatUtil;
import kacang.Application;
import kacang.services.security.User;
import kacang.stdui.*;
import kacang.ui.WidgetManager;
import kacang.util.Log;
import org.apache.commons.collections.SequencedHashMap;

import java.util.*;

public class DocumentPopupSelectBox extends PopupSelectBox
{
    public DocumentPopupSelectBox()
	{
        super();
		setSortable(false);
    }

    public DocumentPopupSelectBox(String name)
	{
        super(name);
		setSortable(false);
    }

    protected Table initPopupTable()
	{
        return new ContentListTable();
    }

    protected Map generateOptionMap(String[] ids)
	{
        Map optionMap = new SequencedHashMap();
        try
		{
            ContentManager cm = (ContentManager)Application.getInstance().getModule(ContentManager.class);
            User user = getWidgetManager().getUser();
            Collection list = cm.viewListWithContents(ids, new String[] {Document.class.getName()}, null, null, null,
				null, null, null, null, Boolean.FALSE, null, null, false, 0, -1, ContentManager.USE_CASE_PREVIEW, user);
            Map tmpMap = new HashMap();
            for (Iterator i=list.iterator(); i.hasNext();)
			{
                ContentObject co = (ContentObject)i.next();
                String name = co.getName();
                if (name != null && name.length() > 20)
                    name = name.substring(0, 20) + "..";
                tmpMap.put(co.getId(), name);
            }
            for (int j=0; j<ids.length; j++)
                optionMap.put(ids[j], tmpMap.get(ids[j]));
        }
        catch (ContentException e)
		{
            Log.getLog(getClass()).error("Error retrieving content", e);
        }
        return optionMap;
    }

    public class ContentListTable extends PopupSelectBoxTable
	{
        public ContentListTable()
		{
        }

        public ContentListTable(String name)
		{
            super(name);
        }

        public void init()
		{
            setSort("date");
            setDesc(true);
            setModel(new ContentListTableModel());
        }

    }

    public class ContentListTableModel extends PopupSelectBoxTableModel
	{

        public ContentListTableModel()
		{
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

            String contextPath = (String)getWidgetManager().getAttribute(WidgetManager.CONTEXT_PATH);
            TableFormat booleanFormat = new TableBooleanFormat("<img src=\"" + contextPath + "/common/table/booleantrue.gif\">", "");

            TableColumn publishedColumn = new TableColumn("published", application.getMessage("general.label.published", "Published"));
            publishedColumn.setFormat(booleanFormat);
            addColumn(publishedColumn);

            /* Actions */
            addAction(new TableAction(FORWARD_SELECT, application.getMessage("general.label.add", "Add")));

            /* Filters */
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

        public String getTableRowKey()
		{
            return "id";
        }

        public Collection getTableRows()
		{
            try
			{
                String name = null;
                Boolean checkedOut = null;
                Boolean submitted = null;
                Boolean approved = null;
                Boolean published = null;
                Boolean archived = null;
                String[] classArray = null;

				classArray = new String[] {Document.class.getName()};

                name = (String)getFilterValue("name");

                List list = (List)getFilterValue("published");
                if (list != null && list.size() > 0)
				{
                    if (list.get(0).equals("true"))
                        published = Boolean.TRUE;
                    else if (list.get(0).equals("false"))
                        published = Boolean.FALSE;
                }

                Application application = Application.getInstance();
                ContentManager cm = (ContentManager)application.getModule(ContentManager.class);
                Collection children = cm.viewList(null, classArray, name, null, checkedOut, submitted, approved, archived, published, Boolean.FALSE, null, getSort(), isDesc(), getStart(), getRows(), null, getWidgetManager().getUser());
                return children;
            }
            catch(Exception e)
			{
                throw new RuntimeException(e.toString());
            }
        }

        public int getTotalRowCount()
		{
            try
			{
                String name = null;
                Boolean checkedOut = null;
                Boolean submitted = null;
                Boolean approved = null;
                Boolean published = null;
                Boolean archived = null;
                String[] classArray = null;

                classArray = new String[] {Document.class.getName()};

                name = (String)getFilterValue("name");

                List list = (List)getFilterValue("published");
                if (list != null && list.size() > 0)
				{
                    if (list.get(0).equals("true"))
                        published = Boolean.TRUE;
                    else if (list.get(0).equals("false"))
                        published = Boolean.FALSE;
                }

                Application application = Application.getInstance();
                ContentManager cm = (ContentManager)application.getModule(ContentManager.class);
                int total = cm.viewCount(null, classArray, name, null, checkedOut, submitted, approved, archived, published, Boolean.FALSE, null, null, getWidgetManager().getUser());
                return total;
            }
            catch(Exception e)
			{
                throw new RuntimeException(e.toString());
            }
        }
    }
}
