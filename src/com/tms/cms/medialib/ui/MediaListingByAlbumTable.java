/*
 * MediaListingByAlbumTable
 * Date Created: Jul 6, 2005
 * Author: Tien Soon, Law
 * Description: TODO Change the class descriptions
 * Company: TMS Berhad
 */
package com.tms.cms.medialib.ui;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import kacang.Application;
import kacang.stdui.Table;
import kacang.stdui.TableAction;
import kacang.stdui.TableColumn;
import kacang.stdui.TableDateFormat;
import kacang.stdui.TableFilter;
import kacang.stdui.TableFormat;
import kacang.stdui.TableModel;
import kacang.stdui.TableStringFormat;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.ui.WidgetManager;

import com.tms.cms.medialib.model.MediaModule;


public class MediaListingByAlbumTable extends Table {
    private String albumId = "";
    private String enforceViewOnly = "false";
    
    public void init() {
        setWidth("100%");
        setModel(new MediaListingByAlbumTableModel());
    }
    
    public void onRequest(Event evt) {
        init();
    }
    
    public class MediaListingByAlbumTableModel extends TableModel {
        public MediaListingByAlbumTableModel() {
            Application app = Application.getInstance();
            MediaModule module = (MediaModule) Application.getInstance().getModule(MediaModule.class);
            String contextPath = (String) getWidgetManager().getAttribute(WidgetManager.CONTEXT_PATH);
            
            TableColumn nameCol = new TableColumn("name", app.getMessage("medialib.label.name", "Name"));
            nameCol.setUrlParam("id");
            addColumn(nameCol);
            
            addColumn(new TableColumn("mediaType", app.getMessage("medialib.label.mediaType", "Media Type")));

            if(! "true".equals(enforceViewOnly)) {
	            if(module.isAlbumManager(albumId) || module.isAlbumContributor(albumId)) {
		            TableColumn approved = new TableColumn("isApproved", app.getMessage("medialib.label.approved", "Approved"));
		            Map map = new HashMap();
		            map.put("Y", "<img src=\"" + contextPath + "/common/table/booleantrue.gif\">");
		            map.put("N", "");
		            TableFormat stringFormat = new TableStringFormat(map);
		            approved.setFormat(stringFormat);
		            addColumn(approved);
	            }
            }
            
            addColumn(new TableColumn("fileSizeStr", app.getMessage("medialib.label.size", "Size")));
            
            TableColumn dateCreated = new TableColumn("dateCreated", app.getMessage("medialib.label.date", "Date"));
            TableFormat dateFormat = new TableDateFormat(Application.getInstance().getProperty("globalDateLong"));
            dateCreated.setFormat(dateFormat);
            addColumn(dateCreated);

            // Text field for value of filter
            addFilter(new TableFilter("name"));

            if(! "true".equals(enforceViewOnly)) {
	            if(module.isAlbumManager(albumId)) {
		            addAction(new TableAction("approve", app.getMessage("medialib.label.approve", "Approve"), null));
		            addAction(new TableAction("disapprove", app.getMessage("medialib.label.disapprove", "Disapprove"), null));
	            }
	            if(module.isAlbumManager(albumId) || module.isAlbumContributor(albumId)) {
		            addAction(new TableAction("delete", app.getMessage("medialib.label.delete", "Delete"), app.getMessage("medialib.message.deleteConfirm", "Confirm to delete?")));
	            }
            }
        }

        public Collection getTableRows() {
            String name = (String) getFilterValue("name");

            Application application = Application.getInstance();
            MediaModule module = (MediaModule) application.getModule(MediaModule.class);
            String sortingOrder = getSort();
            if("fileSizeStr".equalsIgnoreCase(getSort())) {
                sortingOrder = "fileSize";
            }
            boolean viewOnly = "true".equals(enforceViewOnly);
            return module.findMedia(name, !viewOnly, "media", sortingOrder, albumId, isDesc(), getStart(), getRows());
        }

        public int getTotalRowCount() {
            String name = (String) getFilterValue("name");
            
            Application application = Application.getInstance();
            MediaModule module = (MediaModule) application.getModule(MediaModule.class);
            boolean viewOnly = "true".equals(enforceViewOnly);
            return module.countMedia(name, !viewOnly, "media", albumId);
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
    
    public String getAlbumId() {
        return albumId;
    }
    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }

	public String getEnforceViewOnly() {
		return enforceViewOnly;
	}

	public void setEnforceViewOnly(String enforceViewOnly) {
		this.enforceViewOnly = enforceViewOnly;
	}
}
