package com.tms.wiki.ui;


import com.tms.wiki.model.WikiArticle;
import com.tms.wiki.model.WikiDao;
import com.tms.wiki.model.WikiException;
import com.tms.wiki.model.WikiModule;
import com.tms.wiki.model.WikiCategory;
import kacang.Application;
import kacang.stdui.*;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

/**
 * Date: Dec 20, 2006 Time: 5:40:34 PM
 */
public class ViewCategories extends Table {
	Log log = Log.getLog(getClass());

	private String moduleId;
	private String parentId;
	private String categoryName;
	private String path;

	private Date categoryCreatedOn;

	public ViewCategories() {
	}

	public ViewCategories(String s) {
		super(s);
	}

	public void init() {
		setModel(new ViewCategoryModel());
	}

	public void onRequest(Event event) {
		removeChildren();
		init();
		super.onRequest(event);
		
		parentId = event.getRequest().getParameter("categoryId");
		WikiModule module = (WikiModule) Application.getInstance().getModule(WikiModule.class);
		WikiCategory wikiCategory;
		try {
			wikiCategory = module.selectCategory(parentId);
			path = module.viewCategoryPath(parentId);
			if (wikiCategory != null) {
				categoryName = wikiCategory.getCategory();
				categoryCreatedOn = wikiCategory.getCreatedOn();
			} else {
				categoryCreatedOn = null;
				categoryName = null;
			}
		} catch (Exception e) {
			Log.getLog(getClass()).error("Unable to retrieve category", e);
		}
	}

	public class ViewCategoryModel extends TableModel {

		public ViewCategoryModel() {

			TableColumn title = new TableColumn("category", Application.getInstance().getMessage("wiki.label.category", "Category"));
			title.setUrlParam("categoryId");
			addColumn(title);

						
			TableColumn createdOn = new TableColumn("createdOn", Application.getInstance().getMessage("wiki.label.createdOn", "Created On"));
			createdOn.setFormat(new TableDateFormat("dd/MM/yyyy"));
			addColumn(createdOn);

			addAction(new TableAction("add", Application.getInstance().getMessage("wiki.label.add", "Add")));
			addAction(new TableAction("delete", Application.getInstance().getMessage("wiki.label.delete", "Delete"), Application.getInstance().getMessage("wiki.label.checkdelete")));

			TableFilter filter = new TableFilter("searchBy");
			addFilter(filter);

		}

		public Collection getTableRows() {
			try {
				String searchBy = (String) getFilterValue("searchBy");

				WikiModule module = (WikiModule) Application.getInstance()
						.getModule(WikiModule.class);
				return module.getCategories(moduleId, parentId, searchBy, getSort(), isDesc(), getStart(), getRows());

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
				return module.getCategoryCount(moduleId, parentId, searchBy);
			} catch (WikiException e) {
				// log error and return an empty collection
				log.error("Error retrieving category members", e);
				return rows;
			}
		}

		public String getTableRowKey() {
			return "categoryId";
		}

		public Forward processAction(Event evt, String action,
				String[] selectedKeys) {
			Log log = Log.getLog(this.getClass());
			if ("add".equals(action)) {
				return new Forward("Add");
			}
			if ("delete".equals(action)) {
				Application app = Application.getInstance();
				try {
					WikiModule module = (WikiModule) app.getModule(WikiModule.class);
					for (int i = 0; i < selectedKeys.length; i++) {
						module.deleteCategory(selectedKeys[i]);
					}
				} catch (WikiException e) {
					log.error("Unable to delete...", e);
					return new Forward("deleteError");
				}
			}
			return super.processAction(evt, action, selectedKeys);

		}
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
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

	public String getModuleId() {
		return moduleId;
	}

	public void setModuleId(String moduleId) {
		this.moduleId = moduleId;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	
	public String getDefaultTemplate() {
		return "wiki/viewCategories";
	}	
	
	
}
