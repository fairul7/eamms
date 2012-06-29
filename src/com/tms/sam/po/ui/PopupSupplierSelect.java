package com.tms.sam.po.ui;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import kacang.Application;
import kacang.stdui.PopupSelectBox;
import kacang.stdui.PopupSelectBoxTable;
import kacang.stdui.PopupSelectBoxTableModel;
import kacang.stdui.Table;
import kacang.stdui.TableAction;
import kacang.stdui.TableColumn;
import kacang.stdui.TableFilter;
import kacang.util.Log;

import org.apache.commons.collections.SequencedHashMap;

import com.tms.collab.directory.model.AddressBookDao;
import com.tms.collab.directory.model.AddressBookException;
import com.tms.collab.directory.model.Contact;
import com.tms.collab.directory.model.DirectoryModule;
import com.tms.collab.directory.ui.CompanySelectBox;
import com.tms.collab.directory.ui.FolderSelectBox;

public class PopupSupplierSelect extends PopupSelectBox {
	private String ppID="";
    public PopupSupplierSelect() {
        super();
    }

    public PopupSupplierSelect(String name) {
        super(name);
    }


    protected Table initPopupTable() {
        return new SupplierPopupTable();
    }

    protected Map generateOptionMap(String[] ids) {
        Map usersMap = new SequencedHashMap();
        if (ids == null || ids.length == 0) {
            return usersMap;
        }
        String sort = "username";
        try {
			Application app = Application.getInstance();
			DirectoryModule dm = (DirectoryModule)app.getModule(DirectoryModule.class);
            AddressBookDao abdao = (AddressBookDao)dm.getDao();
            Collection userList =abdao.selectContactListById(ids,sort,false);
        	// build users map
            
			Map tmpMap = new SequencedHashMap();
			for (Iterator i = userList.iterator(); i.hasNext();) {
				Contact user = (Contact) i.next();
				String label = user.getDisplayName();
				tmpMap.put(user.getId(), label);
			}
			
			// sort
			for (int j = 0; j < ids.length; j++) {
				String name = (String) tmpMap.get(ids[j]);
				if (name != null){
					if  (name.trim().length() > 50)
						name = name.substring(0, 50) + "..";
					usersMap.put(ids[j], name);
				}
			}
		} catch (Exception e) {
			Log.getLog(getClass()).error("Error retrieving bussiness contacts", e);
		}

        return usersMap;
    }

    public class SupplierPopupTable extends PopupSelectBoxTable {

        public SupplierPopupTable() {
        }

        public SupplierPopupTable(String name) {
            super(name);
        }

        public void init() {
            super.init();
            setWidth("100%");
            setModel(new SupplierPopupTableModel());
        }
     
        public String getType() {
            return DirectoryModule.MODULE_NAME;
        }
        
        public class SupplierPopupTableModel extends PopupSelectBoxTableModel {
            public SupplierPopupTableModel() {
            	   super();

                   Application application = Application.getInstance();
                   //Adding Columns
                   TableColumn firstName = new TableColumn("displayFirstName", Application.getInstance().getMessage("addressbook.label.firstName","First Name"));
                   //firstName.setFormat(new ContactTableNameFormatter(getWidgetManager()));
                   addColumn(firstName);

                   TableColumn lastName= new TableColumn("lastName", Application.getInstance().getMessage("addressbook.label.lastName","Last Name"));
                   addColumn(lastName);

                   TableColumn email= new TableColumn("email", Application.getInstance().getMessage("addressbook.label.email","Email"));
                   addColumn(email);
                   
                   //Adding Actions
                   addAction(new TableAction(FORWARD_SELECT,  application.getMessage("general.label.select", "Select")));

                   //Adding Filters
                   TableFilter query = new TableFilter("query");
                   addFilter(query);

                   TableFilter folderFilter = new TableFilter("folderSelectBox");
                   FolderSelectBox folderSelectBox = new FolderSelectBox("folderSelectBox");
                   folderSelectBox.setType(getType());
                   folderFilter.setWidget(folderSelectBox);
                   addFilter(folderFilter);

                   TableFilter companyFilter = new TableFilter("companySelectBox");
                   CompanySelectBox companySelectBox = new CompanySelectBox("companySelectBox");
                   companySelectBox.setType(getType());
                   companyFilter.setWidget(companySelectBox);
                   addFilter(companyFilter);
            }

            public Collection getTableRows() {
               
            	 try {
                     // get filter values
                     String query = (String)getFilterValue("query");
                     String folderId = null;
                     List selected = (List)getFilterValue("folderSelectBox");
                     if (selected != null && selected.size() > 0) {
                         folderId = (String)selected.iterator().next();
                         if (folderId.trim().length() == 0) {
                             folderId = null;
                         }
                     }
                     String companyId = null;
                     selected = (List)getFilterValue("companySelectBox");
                     if (selected != null && selected.size() > 0) {
                         companyId = (String)selected.iterator().next();
                         if (companyId.trim().length() == 0) {
                             companyId = null;
                         }
                     }
                     String ownerId = null;
                     Boolean approved = Boolean.TRUE;

                     // invoke module
                     DirectoryModule dm = (DirectoryModule)Application.getInstance().getModule(DirectoryModule.class);
                     Collection rows = dm.getContactList(query, folderId, companyId, ownerId, approved, getSort(), isDesc(), getStart(), getRows());
                     return rows;
                 }
                 catch (AddressBookException e) {
                     Log.getLog(getClass()).error("Unable to retrieve contacts: " + e.toString(), e);
                     throw new RuntimeException("Unable to retrieve contacts: " + e.toString());
                 }
            }

            public int getTotalRowCount() {
            	 try {
                     // get filter values
                     String query = (String)getFilterValue("query");
                     String folderId = null;
                     List selected = (List)getFilterValue("folderSelectBox");
                     if (selected != null && selected.size() > 0) {
                         folderId = (String)selected.iterator().next();
                         if (folderId.trim().length() == 0) {
                             folderId = null;
                         }
                     }
                     String companyId = null;
                     selected = (List)getFilterValue("companySelectBox");
                     if (selected != null && selected.size() > 0) {
                         companyId = (String)selected.iterator().next();
                         if (companyId.trim().length() == 0) {
                             companyId = null;
                         }
                     }
                     String ownerId = null;
                     Boolean approved = Boolean.TRUE;

                     // invoke module
                     DirectoryModule dm = (DirectoryModule)Application.getInstance().getModule(DirectoryModule.class);
                     int count = dm.getContactCount(query, folderId, companyId, ownerId, approved);
                     return count;
                 }
                 catch (AddressBookException e) {
                     Log.getLog(getClass()).error("Unable to retrieve contact count: " + e.toString(), e);
                     throw new RuntimeException("Unable to retrieve contact count: " + e.toString());
                 }
            }

            public String getTableRowKey() {
                return "id";
            }

        }
    }

}
