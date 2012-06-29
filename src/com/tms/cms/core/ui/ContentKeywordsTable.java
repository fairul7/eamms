package com.tms.cms.core.ui;

import com.tms.cms.core.model.ContentManager;
import kacang.Application;
import kacang.stdui.*;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by IntelliJ IDEA.
 * User: julian
 * Date: Mar 24, 2003
 * Time: 6:40:53 PM
 * To change this template use Options | File Templates.
 */
public class ContentKeywordsTable extends Table {

    public ContentKeywordsTable() {
    }

    public ContentKeywordsTable(String name) {
        super(name);
    }

    public void init() {
        setModel(new ContentKeywordsTableModel());
    }

    public class ContentKeywordsTableModel extends TableModel {

        public ContentKeywordsTableModel() {

            // add columns
            Application application = Application.getInstance();
            addColumn(new TableColumn("keyword", application.getMessage("general.label.keyword", "Keyword")));

            // add filters
            addFilter(new TableFilter("keyword", application.getMessage("general.label.keyword", "Keyword")));

            // add actions
            addAction(new TableAction("delete", application.getMessage("general.label.delete", "Delete"), Application.getInstance().getMessage("cms.message.deleteKeywords", "Delete Selected Keywords?")));

        }

        public String getTableRowKey() {
            return "keyword";
        }

        public Collection getTableRows() {
            try {
                // process filter
                String kwd = (String)getFilterValue("keyword");

                // query
                Application application = Application.getInstance();
                ContentManager cm = (ContentManager)application.getModule(ContentManager.class);
                Collection kwList = cm.viewKeywords(kwd, getSort(), isDesc(), getStart(), getRows());
                Collection results = new ArrayList();
                for (Iterator i=kwList.iterator(); i.hasNext();) {
                    String kw = (String)i.next();
                    HashMap map = new HashMap();
                    map.put("keyword", kw);
                    results.add(map);
                }
                return results;
            }
            catch(Exception e) {
                throw new RuntimeException(e.toString());
            }
        }

        public int getTotalRowCount() {
            try {
                // process filter
                String kwd = (String)getFilterValue("keyword");

                // query
                Application application = Application.getInstance();
                ContentManager cm = (ContentManager)application.getModule(ContentManager.class);
                return cm.viewKeywordsCount(kwd);
            }
            catch(Exception e) {
                throw new RuntimeException(e.toString());
            }
        }

        public Forward processAction(Event evt, String action, String[] selectedKeys) {
            if ("delete".equals(action)) {
                try {
                    // query
                    Application application = Application.getInstance();
                    ContentManager cm = (ContentManager)application.getModule(ContentManager.class);
                    cm.deleteKeywords(selectedKeys);
                    return new Forward("deleted");
                }
                catch(Exception e) {
                    Log.getLog(getClass()).error(e.toString(), e);
                    return null;
                }
            }
            else {
                return null;
            }
        }

    }

}
