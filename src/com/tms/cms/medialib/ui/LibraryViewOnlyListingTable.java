package com.tms.cms.medialib.ui;

import java.util.Collection;

import kacang.Application;
import kacang.services.security.SecurityException;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.stdui.Table;
import kacang.stdui.TableColumn;
import kacang.stdui.TableFilter;
import kacang.stdui.TableModel;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

import com.tms.cms.medialib.model.LibraryModule;

public class LibraryViewOnlyListingTable extends Table {
private Log log = Log.getLog(getClass());
    
    public void init() {
        setWidth("100%");
        setModel(new LibraryViewOnlyListingTableModel());
    }
    
    public void onRequest(Event evt) {
        init();
    }
    
    public class LibraryViewOnlyListingTableModel extends TableModel {
        public LibraryViewOnlyListingTableModel() {
            Application app = Application.getInstance();
            LibraryModule module = (LibraryModule) Application.getInstance().getModule(LibraryModule.class);
            SecurityService securityService = (SecurityService) app.getService(SecurityService.class);
            User user = Application.getInstance().getCurrentUser();
            boolean isAccessibleToAll = false;
        	try {
        	    isAccessibleToAll = securityService.hasPermission(user.getId(), LibraryModule.PERMISSION_CREATE_LIBRARY, null, null);
        	}
        	catch(SecurityException error) {
        	    log.error("Error retriving user id: " + error, error);
        	}
            
        	// Name
            TableColumn nameCol = new TableColumn("name", app.getMessage("medialib.label.name", "Name"));
            nameCol.setUrlParam("id");
            addColumn(nameCol);
            
            // Total Album
            TableColumn totalAlbumCol = new TableColumn("totalAlbum", app.getMessage("medialib.label.totalAlbum", "Total Album"));
            totalAlbumCol.setSortable(false);
            addColumn(totalAlbumCol);
            
            // Descriptions
            addColumn(new TableColumn("description", app.getMessage("medialib.label.description", "Description")));

            /*String contextPath = (String) getWidgetManager().getAttribute(WidgetManager.CONTEXT_PATH);
            if(module.isManager() || isAccessibleToAll) {
	            // Manageable
	            TableColumn manageable = new TableColumn("manageable", app.getMessage("medialib.label.manageable", "Manageable"));
	            manageable.setSortable(false);
	            TableFormat booleanFormat = new TableBooleanFormat("<img src=\"" + contextPath + "/common/table/booleantrue.gif\">", "");
	            manageable.setFormat(booleanFormat);
	            addColumn(manageable);
            }
            
            // Approval Needed
            TableColumn approvalNeeded = new TableColumn("approvalNeeded", app.getMessage("medialib.label.approvalNeeded","Approval Needed?"));
            //TableFormat booleanFormat = new TableBooleanFormat("<img src=\"" + contextPath + "/common/table/booleantrue.gif\">", "");
            Map map = new HashMap();
            map.put("Y", "<img src=\"" + contextPath + "/common/table/booleantrue.gif\">");
            map.put("N", "");
            TableFormat stringFormat = new TableStringFormat(map);
            approvalNeeded.setFormat(stringFormat);
            addColumn(approvalNeeded);*/

            addFilter(new TableFilter("name"));

            
            /*if(module.isManager() || isAccessibleToAll) {
	            addAction(new TableAction("btnApprovalNeeded", app.getMessage("medialib.label.approvalNeeded"), null));
	            addAction(new TableAction("btnNoApprovalNeeded", app.getMessage("medialib.label.noApprovalNeeded"), null));
	            addAction(new TableAction("delete", app.getMessage("medialib.label.delete", "Delete"), app.getMessage("medialib.message.deleteConfirm", "Confirm to delete?")));
            }*/
        }

        public Collection getTableRows() {
            String name = (String) getFilterValue("name");

            Application application = Application.getInstance();
            LibraryModule module = (LibraryModule) application.getModule(LibraryModule.class);
            return module.findLibrary(name, false, getSort(), isDesc(), getStart(), getRows());
        }

        public int getTotalRowCount() {
            String name = (String) getFilterValue("name");

            Application application = Application.getInstance();
            LibraryModule module = (LibraryModule) application.getModule(LibraryModule.class);
            return module.countLibrary(name, false);
        }

        public String getTableRowKey() {
            return "id";
        }

        public Forward processAction(Event evt, String action, String[] selectedKeys) {
            Application application = Application.getInstance();
            LibraryModule module = (LibraryModule) application.getModule(LibraryModule.class);
            
            if ("delete".equals(action)) {
                for (int i=0; i<selectedKeys.length; i++) {
                    module.deleteLibrary(selectedKeys[i]);
                }
            }
            if ("btnApprovalNeeded".equals(action)) {
                for (int i=0; i<selectedKeys.length; i++) {
                    module.toggleApprovalNeeded(selectedKeys[i], "Y");
                }
            }
            if ("btnNoApprovalNeeded".equals(action)) {
                for (int i=0; i<selectedKeys.length; i++) {
                    module.toggleApprovalNeeded(selectedKeys[i], "N");
                }
            }
            return null;
        }
    }
}
