/*
 * MediaListingTable
 * Date Created: Jun 28, 2005
 * Author: Tien Soon, Law
 * Description: TODO Change the class descriptions
 * Company: TMS Berhad
 */
package com.tms.cms.medialib.ui;

import java.util.Collection;
import java.util.HashMap;
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
import kacang.stdui.TableStringFormat;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.ui.WidgetManager;

import org.apache.commons.collections.SequencedHashMap;

import com.tms.cms.medialib.model.MediaModule;


public class MediaListingTable extends Table {
    public void init() {
        setWidth("100%");
        setModel(new MediaListingTableModel());
    }
    
    public void onRequest(Event evt) {
        init();
    }
    
    public class MediaListingTableModel extends TableModel {
        public MediaListingTableModel() {
            Application app = Application.getInstance();
            MediaModule module = (MediaModule) Application.getInstance().getModule(MediaModule.class);
            String contextPath = (String) getWidgetManager().getAttribute(WidgetManager.CONTEXT_PATH);
            
            TableColumn nameCol = new TableColumn("name", app.getMessage("medialib.label.name", "Name"));
            nameCol.setUrlParam("id");
            addColumn(nameCol);
            
            addColumn(new TableColumn("mediaType", app.getMessage("medialib.label.mediaType", "Media Type")));

            if(module.isManager() || module.isContributor()) {
	            TableColumn approved = new TableColumn("isApproved", app.getMessage("medialib.label.approved", "Approved"));
	            Map map = new HashMap();
	            map.put("Y", "<img src=\"" + contextPath + "/common/table/booleantrue.gif\">");
	            map.put("N", "");
	            TableFormat stringFormat = new TableStringFormat(map);
	            approved.setFormat(stringFormat);
	            addColumn(approved);
            }
            
            addColumn(new TableColumn("fileSizeStr", app.getMessage("medialib.label.size", "Size")));
            
            if(module.isManager()) {
                TableColumn manageable = new TableColumn("manageable", app.getMessage("medialib.label.manageable", "Manageable"));
	            manageable.setSortable(false);
	            TableFormat booleanFormat = new TableBooleanFormat("<img src=\"" + contextPath + "/common/table/booleantrue.gif\">", "");
	            manageable.setFormat(booleanFormat);
	            addColumn(manageable);
            }
            
            /*if(module.isContributor() || module.isManager()) {
                TableColumn deletable = new TableColumn("deletable", app.getMessage("medialib.label.deletable", "Deletable"));
                deletable.setSortable(false);
	            TableFormat booleanFormat = new TableBooleanFormat("<img src=\"" + contextPath + "/common/table/booleantrue.gif\">", "");
	            deletable.setFormat(booleanFormat);
	            addColumn(deletable);
            }*/
            
            TableColumn albumCol = new TableColumn("albumName", app.getMessage("medialib.label.album", "Album"));
            albumCol.setUrlParam("albumId");
            addColumn(albumCol);
            
            TableColumn libCol = new TableColumn("libraryName", app.getMessage("medialib.label.library", "Library"));
            libCol.setUrlParam("libraryId");
            addColumn(libCol);
            
            TableColumn dateCreated = new TableColumn("dateCreated", app.getMessage("medialib.label.date", "Date"));
            TableFormat dateFormat = new TableDateFormat(Application.getInstance().getProperty("globalDateLong"));
            dateCreated.setFormat(dateFormat);
            addColumn(dateCreated);

            // Text field for value of filter
            addFilter(new TableFilter("name"));
            
            // Either search by media name, album name or library name
            SelectBox searchCol = new SelectBox("searchCol");
            searchCol.setMultiple(false);
            Map searchColMap = new SequencedHashMap();
            searchColMap.put("name", app.getMessage("medialib.label.name", "Name"));
            searchColMap.put("album", app.getMessage("medialib.label.album", "Album"));
            searchColMap.put("library", app.getMessage("medialib.label.library", "Library"));
            searchCol.setOptionMap(searchColMap);
            TableFilter searchColFilter = new TableFilter("searchColFilter");
            searchColFilter.setWidget(searchCol);
            addFilter(searchColFilter);

            if(module.isManager()) {
	            addAction(new TableAction("approve", app.getMessage("medialib.label.approve", "Approve"), null));
	            addAction(new TableAction("disapprove", app.getMessage("medialib.label.disapprove", "Disapprove"), null));
            }
            if(module.isContributor() || module.isManager()) {
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
            MediaModule module = (MediaModule) application.getModule(MediaModule.class);
            String sortingOrder = getSort();
            if("fileSizeStr".equalsIgnoreCase(getSort())) {
                sortingOrder = "fileSize";
            }
            return module.queryOwnMedia(name, true, searchColValue, sortingOrder, isDesc(), getStart(), getRows());
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
            MediaModule module = (MediaModule) application.getModule(MediaModule.class);
            if(module.isManager()) {
            	return module.countMedia(name, true, searchColValue, "");
            }
            else {
            	return module.countOwnMedia(name, true, searchColValue);
            }
        }

        public String getTableRowKey() {
            return "id";
        }

        public Forward processAction(Event evt, String action, String[] selectedKeys) {
            Application application = Application.getInstance();
            MediaModule module = (MediaModule) application.getModule(MediaModule.class);
            
            if ("delete".equals(action)) {
                for (int i=0; i<selectedKeys.length; i++) {
                    module.deleteMedia(selectedKeys[i]);
                }
            }
            
            if ("approve".equals(action)) {
                for (int i=0; i<selectedKeys.length; i++) {
                    module.approveMedia(selectedKeys[i]);
                }
            }
            
            if ("disapprove".equals(action)) {
                for (int i=0; i<selectedKeys.length; i++) {
                    module.disapproveMedia(selectedKeys[i]);
                }
            }
            return null;
        }
    }
}
