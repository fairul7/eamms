package com.tms.wiki.ui;

import com.tms.wiki.model.WikiException;
import com.tms.wiki.model.WikiArticle;
import com.tms.wiki.model.WikiDao;
import com.tms.wiki.model.WikiModule;
import kacang.Application;
import kacang.model.DaoException;
import kacang.stdui.*;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

/**
 * Date: Dec 20, 2006
 * Time: 5:40:34 PM
 */
public class ViewModules extends Table {
    Log log = Log.getLog(getClass());

    private String moduleId;
    private String moduleName;
    private Date categoryCreatedOn;
    
    public ViewModules() {
    }

    public ViewModules(String s) {
        super(s);
    }

    public void init() {
        setModel(new ViewModuleModel());
    }
    
    public void onRequest(Event event){
    	removeChildren();
    	init();
    	super.onRequest(event);
    }


    public class ViewModuleModel extends TableModel {

        public ViewModuleModel() {

            TableColumn moduleName = new TableColumn("moduleName", Application.getInstance().getMessage("wiki.label.module", "Module Name"));
            moduleName.setUrlParam("moduleId");
            addColumn(moduleName);
            
           
            TableColumn createdOn = new TableColumn("createdOn", Application.getInstance().getMessage("wiki.label.createdOn", "Created On"));
            createdOn.setFormat(new TableDateFormat("dd/MM/yyyy"));
            addColumn(createdOn);

            addAction(new TableAction("add", Application.getInstance().getMessage("wiki.label.add", "Add")));
            String confirm=Application.getInstance().getMessage("wiki.label.checkdelete");
            TableAction ta = new TableAction("delete", Application.getInstance().getMessage("wiki.label.delete", "Delete"), confirm);
            addAction(ta);

            TableFilter filter = new TableFilter("searchBy");
            addFilter(filter);

        }

        public Collection getTableRows() {
            
                String searchBy = (String) getFilterValue("searchBy");

                WikiModule module = (WikiModule) Application.getInstance().getModule(WikiModule.class);
                try {
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
                }catch (WikiException e) {
                    log.error("Categories for module exist...", e);  
                    return new Forward("deleteError");       
           
                } 
            }
            return super.processAction(evt, action, selectedKeys);
        }
        
        public String getTableRowKey() {
            return "moduleId";
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

