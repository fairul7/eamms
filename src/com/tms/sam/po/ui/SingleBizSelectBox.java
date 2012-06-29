package com.tms.sam.po.ui;

import java.util.Collection;
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
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

import org.apache.commons.collections.SequencedHashMap;

import com.tms.collab.directory.model.AddressBookException;
import com.tms.collab.directory.model.CompanyModule;
import com.tms.collab.directory.model.Contact;
import com.tms.collab.directory.model.DirectoryModule;
import com.tms.collab.directory.ui.CompanySelectBox;
import com.tms.collab.directory.ui.FolderSelectBox;

public class SingleBizSelectBox extends PopupSelectBox {
	public SingleBizSelectBox() {
        super();
        super.setMultiple(false);
    }

    public SingleBizSelectBox(String name) {
        super(name);
        super.setMultiple(false);
    }

    protected Table initPopupTable() {
        return new UsersPopupTable();
    }

    protected Map generateOptionMap(String[] ids) {
        Map usersMap = new SequencedHashMap();
        if (ids == null || ids.length == 0) {
            return usersMap;
        }

        try {
            
            DirectoryModule dm = (DirectoryModule)Application.getInstance().getModule(DirectoryModule.class);
            Contact contact = dm.getContact(ids[0]);
            
            usersMap.put(contact.getId(), contact.getFirstName());
            
        }
        catch (Exception e) {
            Log.getLog(getClass()).error("Error retrieving users", e);
        }

        return usersMap;
    }

    public String getDefaultTemplate() {
        return "po/popupBizSingleSelect";
    }

    public String getId() {
        Map optionMap = getOptionMap();
        if (optionMap != null) {
            Collection idSet = optionMap.keySet();
            idSet.remove("");
            String[] idArray = (String[])idSet.toArray(new String[0]);
            return idArray[0];
        }
        else {
            return new String();
        }
    }

    public class UsersPopupTable extends PopupSelectBoxTable {

        public UsersPopupTable() {
        }

        public UsersPopupTable(String name) {
            super(name);
        }

        public void init() {
            super.init();
            setWidth("100%");
            setMultipleSelect(false);
            setModel(new UsersPopupTable.UserTableModel());
            
        }
        
        public void onRequest(Event evt) {
            
        }

        public class UserTableModel extends PopupSelectBoxTableModel {
            public UserTableModel() {
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

            public String getType() {
                return DirectoryModule.MODULE_NAME;
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
            
            public Forward processAction(Event event, String action, String[] selectedKeys) {
              try {
                    if (PopupSelectBox.FORWARD_SELECT.equals(action)) {
                        if (getPopupSelectBox() != null && selectedKeys != null && selectedKeys.length > 0) {
                            getPopupSelectBox().setIds(selectedKeys);
                            DirectoryModule dm = (DirectoryModule)Application.getInstance().getModule(DirectoryModule.class);
                            Contact contact = dm.getContact(getId());
                            
                            if (contact.getCompanyId() != null && !contact.getCompanyId().equals("")) {
                                CompanyModule cm = (CompanyModule)Application.getInstance().getModule(CompanyModule.class);
                                
                                Contact company = cm.getContact(contact.getCompanyId());
                                contact.setCompany(company.getCompany());
                                contact.setPhone(company.getPhone());
                                contact.setAddress(company.getAddress());
                                
                                
                            }else{
                            	 contact.setCompany("---");
                                 contact.setPhone("---");
                                 contact.setAddress("---");
                            }
                            
                            //add the user into session and pick up by hierarchyform to populate related info to the fields
                            event.getRequest().getSession().setAttribute("biz_dir", contact);
                            
                            return new Forward(PopupSelectBox.FORWARD_SELECT);
                        }
                        else {
                            return null;
                        }
                    }
                    else {
                        return null;
                    }
                }
                catch (Exception e) {
                    Log.getLog(getClass()).error("Error processing action " + action + ": " + e.toString(), e);
                    return new Forward(PopupSelectBox.FORWARD_ERROR);
                }
            }

            public String getGroupFilter() {
                Collection group = (Collection) getFilterValue("group");
                if (group != null)
                    if (!(group.isEmpty()))
                        return (String) group.iterator().next();
                return "";
            }

            public String getTableRowKey() {
                return "id";
            }

        }
    }
}
