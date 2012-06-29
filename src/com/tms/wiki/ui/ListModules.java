package com.tms.wiki.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import kacang.Application;
import kacang.stdui.Table;
import kacang.stdui.TableAction;
import kacang.stdui.TableColumn;
import kacang.stdui.TableDateFormat;
import kacang.stdui.TableFilter;
import kacang.stdui.TableModel;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;


import com.tms.wiki.model.WikiException;
import com.tms.wiki.model.WikiModule;


public class ListModules extends Table{
	
	Log log = Log.getLog(getClass());

    private String moduleId;
    private String moduleName;
    private Date categoryCreatedOn;
    private String categoryId;

    public ListModules() {
    }

    public ListModules(String s) {
        super(s);
    }

    public void init() {
        setModel(new ListModuleModel());
    }


    public class ListModuleModel extends TableModel {

        public ListModuleModel() {

            TableColumn moduleName = new TableColumn("moduleName", Application.getInstance().getMessage("wiki.label.module", "Module Name"));
            moduleName.setUrlParam("moduleId");
            addColumn(moduleName);
            
           
            TableColumn createdOn = new TableColumn("createdOn", Application.getInstance().getMessage("wiki.label.createdOn", "Created On"));
            createdOn.setFormat(new TableDateFormat("dd/MM/yyyy"));
            addColumn(createdOn);

            TableFilter filter = new TableFilter("searchBy");
            addFilter(filter);

        }

        public Collection getTableRows() {
            try {
                String searchBy = (String) getFilterValue("searchBy");

                WikiModule module = (WikiModule) Application.getInstance().getModule(WikiModule.class);
                return module.getModules(searchBy, getSort(), isDesc(), getStart(), getRows());

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
                return module.getModuleCount(searchBy);
            } catch (WikiException e) {
                // log error and return an empty collection
                log.error("Error retrieving module list..", e);
                return rows;
            }
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
                        module.deleteModule(selectedKeys[i]);
                    }
                } catch (Exception e) {
                    log.error("Unable to delete module...", e);
                }
            }
            return super.processAction(evt, action, selectedKeys);

        }
    }

    public String getModuleId() {
        return moduleId;
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public Date getCategoryCreatedOn() {
        return categoryCreatedOn;
    }

    public void setCategoryCreatedOn(Date categoryCreatedOn) {
        this.categoryCreatedOn = categoryCreatedOn;
    }
}


