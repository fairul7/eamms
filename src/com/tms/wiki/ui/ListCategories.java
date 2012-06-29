package com.tms.wiki.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import kacang.Application;
import kacang.stdui.Table;
import kacang.stdui.TableAction;
import kacang.stdui.TableColumn;
import kacang.stdui.TableDateFormat;
import kacang.stdui.TableFilter;
import kacang.stdui.TableFormat;
import kacang.stdui.TableModel;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

import com.tms.wiki.model.WikiException;
import com.tms.wiki.model.WikiArticle;
import com.tms.wiki.model.WikiCategory;
import com.tms.wiki.model.WikiException;
import com.tms.wiki.model.WikiModule;
import com.tms.wiki.ui.ViewCategories.ViewCategoryModel;

public class ListCategories extends Table{
	Log log = Log.getLog(getClass());

	private String moduleId;
	private String parentId;
	private String categoryName;
	private Date categoryCreatedOn;
	public ListCategories() {
	}

	public ListCategories(String s) {
		super(s);
	}

	public void initTable() {
		setModel(new ListCategoriesModel());
	}

	public void onRequest(Event event) {
		parentId = event.getRequest().getParameter("categoryId");
		init();
		WikiModule module = (WikiModule) Application.getInstance().getModule(
				WikiModule.class);
		WikiCategory wikiCategory;
		try {
			wikiCategory = module.selectCategory(parentId);
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
		initTable();
	}

	public class ListCategoriesModel extends TableModel {

		public ListCategoriesModel() {

			TableColumn title = new TableColumn("category", Application.getInstance().getMessage("wiki.label.category", "Category"));
			title.setUrlParam("categoryId");
			addColumn(title);

			TableColumn showArticleCol = new TableColumn("categoryId", "articles");
			showArticleCol.setFormat(new TableFormat() {
				public String format(Object categoryId) {
					if (categoryId != null) {

					String displayResult = "";

					WikiModule module = (WikiModule) Application.getInstance().getModule(WikiModule.class);
					
                   	Collection articlesResult = module.selectArticleByCategory("" + categoryId);
					
					for (Iterator iterator = articlesResult.iterator(); iterator.hasNext();) {
						WikiArticle wa = (WikiArticle) iterator.next();
						
						if (wa.getTitle() != null && wa.getTitle().length() > 15) {
							displayResult += wa.getTitle().substring(0, 15)+ ".. ";
						} else {
							displayResult += wa.getTitle();
						}

						if (iterator.hasNext())
							displayResult += "<br>";

					}
					
					return displayResult;
					}
					

					return categoryId.toString();
				}
			});

			addColumn(showArticleCol);

			TableColumn createdOn = new TableColumn("createdOn", Application.getInstance().getMessage("wiki.label.createdOn", "Created On"));
			createdOn.setFormat(new TableDateFormat("dd/MM/yyyy"));
			addColumn(createdOn);
			
			if(parentId!=null && !parentId.equals(""))
				addAction(new TableAction("newArticle", Application.getInstance().getMessage("wiki.label.newArticle", "New Article")));
			
			TableFilter filter = new TableFilter("searchBy");
			addFilter(filter);

		}

		public Collection getTableRows() {
			try {
				String searchBy = (String) getFilterValue("searchBy");

				WikiModule module = (WikiModule) Application.getInstance().getModule(WikiModule.class);
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

		public Forward processAction(Event evt, String action, String[] selectedKeys) {
			Log log = Log.getLog(this.getClass());
			
			if(action !=null && action.equals("newArticle")){
				
				return new Forward("newArticle");			
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
}

