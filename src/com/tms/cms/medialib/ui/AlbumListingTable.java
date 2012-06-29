/*
 * AlbumListingTable
 * Date Created: Jun 28, 2005
 * Author: Tien Soon, Law
 * Description: TODO Change the class descriptions
 * Company: TMS Berhad
 */
package com.tms.cms.medialib.ui;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import kacang.Application;
import kacang.stdui.SelectBox;
import kacang.stdui.Table;
import kacang.stdui.TableAction;
import kacang.stdui.TableBooleanFormat;
import kacang.stdui.TableColumn;
import kacang.stdui.TableDateFormat;
import kacang.stdui.TableFilter;
import kacang.stdui.TableFormat;
import kacang.stdui.TableModel;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.ui.WidgetManager;

import org.apache.commons.collections.SequencedHashMap;

import com.tms.cms.medialib.model.AlbumModule;


public class AlbumListingTable extends Table {
    public void init() {
        setWidth("100%");
        setModel(new AlbumListingTableModel());
    }
    
    public void onRequest(Event evt) {
        init();
    }
    
    public class AlbumListingTableModel extends TableModel {
        public AlbumListingTableModel() {
            Application app = Application.getInstance();
            AlbumModule module = (AlbumModule) Application.getInstance().getModule(AlbumModule.class);
            
            // Name
            TableColumn nameCol = new TableColumn("name", app.getMessage("medialib.label.name", "Name"));
            nameCol.setUrlParam("id");
            addColumn(nameCol);
            
            // Descriptions
            addColumn(new TableColumn("description", app.getMessage("medialib.label.description", "Description")));

            String contextPath = (String) getWidgetManager().getAttribute(WidgetManager.CONTEXT_PATH);
            TableFormat booleanFormat = new TableBooleanFormat("<img src=\"" + contextPath + "/common/table/booleantrue.gif\">", "");
            /*// Manageable
            if(module.isManager()) {
                TableColumn manageable = new TableColumn("manageable", app.getMessage("medialib.label.manageable", "Manageable"));
	            manageable.setSortable(false);
	            manageable.setFormat(booleanFormat);
	            addColumn(manageable);
            }*/
            
            // Library Name
            TableColumn libCol = new TableColumn("libraryName", app.getMessage("medialib.label.library", "Library"));
            libCol.setUrlParam("libraryId");
            addColumn(libCol);
            
            // Featured
            TableColumn featuredCol = new TableColumn("featured", app.getMessage("medialib.label.featured", "Featured"));
            featuredCol.setFormat(booleanFormat);
            addColumn(featuredCol);
            
            // Event Date
            TableColumn eventDateCol = new TableColumn("eventDate", app.getMessage("medialib.label.eventDate", "Event Date"));
            TableFormat dateFormat = new TableDateFormat(Application.getInstance().getProperty("globalDatetimeLong"));
            eventDateCol.setFormat(dateFormat);
            addColumn(eventDateCol);

            // Text field for value of filter
            TableFilter name = new TableFilter("name");
            addFilter(name);
            
            // Either search by album name or library name
            SelectBox searchCol = new SelectBox("searchCol");
            searchCol.setMultiple(false);
            Map searchColMap = new SequencedHashMap();
            searchColMap.put("name", app.getMessage("medialib.label.name", "Name"));
            searchColMap.put("library", app.getMessage("medialib.label.library", "Library"));
            searchCol.setOptionMap(searchColMap);
            TableFilter searchColFilter = new TableFilter("searchColFilter");
            searchColFilter.setWidget(searchCol);
            addFilter(searchColFilter);

            if(module.isManager()) {
	            addAction(new TableAction("featured", app.getMessage("medialib.label.featured", "Featured"), null));
	            addAction(new TableAction("nonfeatured", app.getMessage("medialib.label.nonfeatured", "Non-featured"), null));
	            addAction(new TableAction("delete", app.getMessage("medialib.label.delete", "Delete"), app.getMessage("medialib.message.deleteConfirm", "Confirm to delete?")));
            }
        }

        public Collection getTableRows() {
            String name = (String) getFilterValue("name");
            SelectBox searchCol = (SelectBox) getFilter("searchColFilter").getWidget();
            List cols = (List) searchCol.getValue();
            String searchColValue = "";
            if (cols.size() > 0) {
                searchColValue = (String) cols.get(0);
            }

            Application application = Application.getInstance();
            AlbumModule module = (AlbumModule) application.getModule(AlbumModule.class);
            return module.findAlbum(name, true, searchColValue, getSort(), "", isDesc(), getStart(), getRows());
        }

        public int getTotalRowCount() {
            String name = (String) getFilterValue("name");
            SelectBox searchCol = (SelectBox) getFilter("searchColFilter").getWidget();
            List cols = (List) searchCol.getValue();
            String searchColValue = "";
            if (cols.size() > 0) {
                searchColValue = (String) cols.get(0);
            }
            
            Application application = Application.getInstance();
            AlbumModule module = (AlbumModule) application.getModule(AlbumModule.class);
            return module.countAlbum(name, true, searchColValue, "");
        }

        public String getTableRowKey() {
            return "id";
        }

        public Forward processAction(Event evt, String action, String[] selectedKeys) {
            Application application = Application.getInstance();
            AlbumModule module = (AlbumModule) application.getModule(AlbumModule.class);
            
            if ("delete".equals(action)) {
                for (int i=0; i<selectedKeys.length; i++) {
                    module.deleteAlbum(selectedKeys[i]);
                }
            }
            
            if ("featured".equals(action)) {
                for (int i=0; i<selectedKeys.length; i++) {
                    module.setFeaturedAlbum(selectedKeys[i]);
                }
            }
            
            if ("nonfeatured".equals(action)) {
                for (int i=0; i<selectedKeys.length; i++) {
                    module.setNonfeaturedAlbum(selectedKeys[i]);
                }
            }
            return null;
        }
    }
}
