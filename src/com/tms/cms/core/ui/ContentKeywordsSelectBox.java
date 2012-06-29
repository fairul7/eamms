package com.tms.cms.core.ui;

import com.tms.cms.core.model.ContentManager;
import kacang.Application;
import kacang.stdui.*;
import kacang.util.Log;
import org.apache.commons.collections.SequencedHashMap;

import java.util.*;

public class ContentKeywordsSelectBox extends PopupSelectBox {

    public ContentKeywordsSelectBox() {
        super();
    }

    public ContentKeywordsSelectBox(String name) {
        super(name);
    }


    protected Table initPopupTable() {
        return new ContentKeywordsPopupTable();
    }

    protected Map generateOptionMap(String[] ids) {
        Map tmpMap = new SequencedHashMap();
        if (ids == null || ids.length == 0) {
            return tmpMap;
        }

        try {
            Collection keywordList = new ArrayList(Arrays.asList(ids));

            // build keywords map
            for (Iterator i=keywordList.iterator(); i.hasNext();) {
                String kw = (String)i.next();
                tmpMap.put(kw, kw);
            }
        }
        catch (Exception e) {
            Log.getLog(getClass()).error("Error retrieving keywords", e);
        }

        return tmpMap;
    }

    public class ContentKeywordsPopupTable extends PopupSelectBoxTable {

        public ContentKeywordsPopupTable() {
        }

        public ContentKeywordsPopupTable(String name) {
            super(name);
        }

        public void init() {
            super.init();
            setWidth("100%");
            setModel(new ContentKeywordsPopupTable.ContentKeywordsTableModel());
        }

        public class ContentKeywordsTableModel extends PopupSelectBoxTableModel {
            public ContentKeywordsTableModel() {
                super();

                Application application = Application.getInstance();
                //Adding Columns
                TableColumn nameColumn = new TableColumn("keyword", application.getMessage("general.label.keyword", "Keyword"));
                nameColumn.setUrlParam("keyword");
                addColumn(nameColumn);

                //Adding Actions
                addAction(new TableAction(FORWARD_SELECT,  application.getMessage("general.label.select", "Select")));

                //Adding Filters
                TableFilter keywordFilter = new TableFilter("query");
                addFilter(keywordFilter);
            }

            public Collection getTableRows() {

                Collection list = new ArrayList();
                try {
                    String query = (String)getFilterValue("query");

                    Application app = Application.getInstance();
                    ContentManager cm = (ContentManager)app.getModule(ContentManager.class);
                    Collection keywordList = cm.viewKeywords(query, getSort(), isDesc(), getStart(), getRows());
                    for (Iterator i=keywordList.iterator(); i.hasNext();) {
                        String kw = (String)i.next();
                        Map row = new HashMap();
                        row.put("keyword", kw);
                        list.add(row);
                    }

                }
                catch (Exception e) {
                    Log.getLog(ContentKeywordsTableModel.class).error(e.toString(), e);
                }
                return list;
            }

            public int getTotalRowCount() {
                try {
                    String query = (String)getFilterValue("query");

                    Application app = Application.getInstance();
                    ContentManager cm = (ContentManager)app.getModule(ContentManager.class);
                    int keywordListCount = cm.viewKeywordsCount(query);
                    return keywordListCount;
                }
                catch (Exception e) {
                    Log.getLog(ContentKeywordsTableModel.class).error(e.toString(), e);
                }
                return 0;
            }

            public String getTableRowKey() {
                return "keyword";
            }

        }
    }

}
