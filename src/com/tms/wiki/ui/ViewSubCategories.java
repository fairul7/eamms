package com.tms.wiki.ui;


import com.tms.wiki.model.WikiCategory;
import com.tms.wiki.model.WikiException;
import com.tms.wiki.model.WikiModule;
import kacang.Application;
import kacang.stdui.*;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

/**
 * Date: Dec 22, 2006
 * Time: 4:15:16 PM
 */
public class ViewSubCategories extends Table {
    Log log = Log.getLog(getClass());
    private String categoryId;
    private String categoryName;
    private Date categoryCreatedOn;

    public ViewSubCategories() {
    }

    public ViewSubCategories(String s) {
        super(s);
    }

    public void init() {
        setModel(new ViewSubCategoryModel());
    }

    public void onRequest(Event event) {
        super.onRequest(event);
        init();
        WikiModule module = (WikiModule) Application.getInstance().getModule(WikiModule.class);
        WikiCategory wikiCategory;
        try {
            wikiCategory = module.selectCategory(categoryId);
            if (wikiCategory != null) {
                categoryName = wikiCategory.getCategory();
                categoryCreatedOn = wikiCategory.getCreatedOn();
            }
        } catch (Exception e) {
            Log.getLog(getClass()).error("Unable to retrieve category", e);
        }
    }

    public class ViewSubCategoryModel extends TableModel {

        public ViewSubCategoryModel() {

            TableColumn subCategory = new TableColumn("subCategory", Application.getInstance().getMessage("wiki.label.subCategory", "SubCategories"));
            addColumn(subCategory);

            TableColumn createdOn = new TableColumn("createdOn", Application.getInstance().getMessage("wiki.label.createdOn", "Created On"));
            addColumn(createdOn);

            addAction(new TableAction("add", Application.getInstance().getMessage("wiki.label.add", "Add")));
            addAction(new TableAction("delete", Application.getInstance().getMessage("wiki.label.delete", "Delete"), Application.getInstance().getMessage("wiki.label.checkdelete")));

            TableFilter filter = new TableFilter("searchBy");
            addFilter(filter);

        }

        public Collection getTableRows() {
            try {
                String searchBy = (String) getFilterValue("searchBy");

                WikiModule module = (WikiModule) Application.getInstance().getModule(WikiModule.class);
                return module.getSubCategories(categoryId, searchBy, getSort(), isDesc(), getStart(), getRows());

            } catch (WikiException e) {
                log.error("Error retrieving data", e);
                return new ArrayList();
            }
        }

        public int getTotalRowCount() {
            int rows = 0;
            String searchBy = (String) getFilterValue("searchBy");

            try {
                WikiModule module = (WikiModule) Application.getInstance().getModule(WikiModule.class);
                return module.getSubCategoryCount(categoryId, searchBy);
            } catch (WikiException e) {
                // log error and return an empty collection
                log.error("Error retrieving category members", e);
                return rows;
            }
        }

        public String getTableRowKey() {
            return "subCategoryId";
        }

        public Forward processAction(Event evt, String action, String[] selectedKeys) {
            Log log = Log.getLog(this.getClass());
            if ("add".equals(action)) {
                return new Forward("Add");
            }
            if ("delete".equals(action)) {
                Application app = Application.getInstance();
                try {
                    WikiModule module = (WikiModule) app.getModule(WikiModule.class);
                    for (int i = 0; i < selectedKeys.length; i++) {
                        module.deleteSubCategory(selectedKeys[i]);
                    }
                } catch (Exception e) {
                    log.error("Unable to delete...", e);
                }
            }
            return super.processAction(evt, action, selectedKeys);
        }
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Date getCategoryCreatedOn() {
        return categoryCreatedOn;
    }

    public void setCategoryCreatedOn(Date categoryCreatedOn) {
        this.categoryCreatedOn = categoryCreatedOn;
    }
}



