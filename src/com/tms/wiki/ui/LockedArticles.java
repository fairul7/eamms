package com.tms.wiki.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import kacang.Application;
import kacang.stdui.Table;
import kacang.stdui.TableAction;
import kacang.stdui.TableColumn;
import kacang.stdui.TableFilter;
import kacang.stdui.TableFormat;
import kacang.stdui.TableModel;
import kacang.stdui.TableStringFormat;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.ui.WidgetManager;
import kacang.util.Log;

import com.tms.wiki.model.WikiException;
import com.tms.wiki.model.WikiModule;

public class LockedArticles extends Table {
	Log log = Log.getLog(getClass());

	public LockedArticles() {
		super();
	}

	public LockedArticles(String s) {
		super(s);
	}

	public void init() {
		setModel(new LockedArticlesModel());
	}

	public class LockedArticlesModel extends TableModel {

		public LockedArticlesModel() {

			TableColumn title = new TableColumn("title", Application.getInstance().getMessage("wiki.label.TitleName", "Title"));
			addColumn(title);

			TableColumn category = new TableColumn("category", Application.getInstance().getMessage("wiki.label.category", "Category"));
			addColumn(category);

			TableColumn moduleName = new TableColumn("moduleName", Application.getInstance().getMessage("wiki.label.module", "Module Name"));
			addColumn(moduleName);
			
			TableColumn articleStatus = new TableColumn("articleStatus", Application.getInstance().getMessage("wiki.label.articleLocked", "Article Locked"));			
			String contextPath = (String) getWidgetManager().getAttribute(WidgetManager.CONTEXT_PATH);  
			
			Map map = new HashMap();
            /*map.put("1", "<img src=\"" + "/images/booleantrue.gif\">");*/
			 map.put("true", "<img src=\"" + "images/booleantrue.gif\">");
             map.put("false", "");
             map.put("1", "<img src=\"" + "images/booleantrue.gif\">");
             map.put("0", "");
             map.put(Boolean.TRUE, "<img src=\"" + "images/booleantrue.gif\">");
             map.put(Boolean.FALSE, "");
             TableFormat stringFormat = new TableStringFormat(map);
             articleStatus.setFormat(stringFormat);
        
			
			addColumn(articleStatus);
			addAction(new TableAction("lock", Application.getInstance().getMessage("wiki.label.lock", "Lock")));
			addAction(new TableAction("unlock", Application.getInstance().getMessage("wiki.label.unlock", "Unlock"), Application.getInstance().getMessage("wiki.label.checkdelete")));
			addAction(new TableAction("delete", Application.getInstance().getMessage("wiki.label.delete", "Delete"), Application.getInstance().getMessage("wiki.label.delete")));

			TableFilter filter = new TableFilter("searchBy");
			addFilter(filter);

		}

		public Collection getTableRows() {
			try {
				String searchBy = (String) getFilterValue("searchBy");

				WikiModule module = (WikiModule) Application.getInstance().getModule(WikiModule.class);
				return module.getLockedArticles(searchBy, getSort(), isDesc(), getStart(), getRows());

			} catch (WikiException e) {
				log.error("Error retrieving data", e);
				
			}
			return new ArrayList();
		}

		public int getTotalRowCount() {
			int rows = 0;
			String searchBy = (String) getFilterValue("searchBy");
			try {
				WikiModule module = (WikiModule) Application.getInstance().getModule(WikiModule.class);
				return module.getLockedArticlesCount(searchBy);
			} catch (WikiException e) {
				log.error("Error retrieving questions", e);
				
			}
			return rows;
		}	
		
		public Forward processAction(Event evt, String action, String[] selectedKeys) {
			Application app = Application.getInstance();
			WikiModule module = (WikiModule) app.getModule(WikiModule.class);
			
			try {                
                Boolean locked = new Boolean(false);
                if(action.equals("lock"))
                	locked = new Boolean(true);
                if(action.equals("lock") || action.equals("unlock")) {
	                for (int i = 0; i < selectedKeys.length; i++) {
	                    module.lockArticle(selectedKeys[i], locked);
	                }
                }
         
	            if ("delete".equals(action)) {               
                    for (int i = 0; i < selectedKeys.length; i++) {
                        module.deleteArticle(selectedKeys[i]);
                    }
	            }
	            if(action.equals("lock"))
	            	return new Forward("lock");
	            else if (action.equals("unlock"))
	            	return new Forward("unlock");
	            else
	            	return new Forward("delete");
		   } catch (Exception e) {
	            log.error("Unable to unlock articles", e);
	        }
			
            return super.processAction(evt, action, selectedKeys);
        }
					
		public String getTableRowKey() {
		    return "articleId";
		}	

	}

}
