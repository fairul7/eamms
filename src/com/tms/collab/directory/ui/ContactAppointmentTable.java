package com.tms.collab.directory.ui;

import kacang.model.DaoException;
import kacang.services.security.SecurityException;
import kacang.stdui.*;
import kacang.util.Log;
import kacang.ui.Forward;
import kacang.ui.Event;
import kacang.Application;

import com.tms.collab.directory.model.AddressBookDao;
import com.tms.collab.directory.model.AddressBookModule;
import com.tms.collab.directory.model.AddressBookException;
import com.tms.collab.directory.model.Contact;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.SequencedHashMap;

public class ContactAppointmentTable extends Table implements AddressBookWidget {
	
	private String keys;

	private String values;

    protected ContactTableModel tableModel;

    public ContactAppointmentTable() {
    }

    public ContactAppointmentTable(String name) {
        super(name);
    }

    public void init() {
        setWidth("100%");
        tableModel = new ContactTableModel();
        setModel(tableModel);
    }

    public void setQuery(String query) {
        TextField q = (TextField)getModel().getFilter("query").getWidget();
        q.setValue(query);
    }

    public void setSelectedFolder(String folderId) {
        SelectBox sb = (SelectBox) getModel().getFilter("folderSelectBox").getWidget();
        sb.setSelectedOptions(new String[]{folderId});
    }

    private String type;

    public String getType() {
        return AddressBookModule.MODULE_NAME;
    }

    public void setType(String type) {
        this.type = type;
    }

    protected AddressBookModule getModule() {
        return Util.getModule(this);
    }

    protected String getUserId() {
        return Util.getUserId(this);
    }

    public class ContactTableModel extends TableModel {

        public ContactTableModel() {

            // add columns
            TableColumn firstName = new TableColumn("displayFirstName", Application.getInstance().getMessage("addressbook.label.firstName","First Name"));
            firstName.setFormat(new ContactTableNameFormatter(getWidgetManager()));
            addColumn(firstName);

            TableColumn lastName= new TableColumn("lastName", Application.getInstance().getMessage("addressbook.label.lastName","Last Name"));
            addColumn(lastName);

            TableColumn email= new TableColumn("email", Application.getInstance().getMessage("addressbook.label.email","Email"));
            addColumn(email);

            // add actions
            addAction(new TableAction("select", Application.getInstance().getMessage("addressbook.label.select","Select")));

            // add filters
            TableFilter query = new TableFilter("query");
            addFilter(query);

            TableFilter folderFilter = new TableFilter("folderSelectBox");
            FolderSelectBox folderSelectBox = new FolderSelectBox("folderSelectBox");
            folderSelectBox.setType(getType());
            folderFilter.setWidget(folderSelectBox);
            addFilter(folderFilter);

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
                String ownerId = getUserId();
                Boolean approved = null;

                // invoke module
                AddressBookModule dm = getModule();
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
                String ownerId = getUserId();
                Boolean approved = null;

                // invoke module
                AddressBookModule dm = getModule();
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

        public Forward processAction(Event event, String action, String[] selectedKeys) {
            try {
            	keys=null;
				values=null;
				if ("select".equals(action)) {
					Map selectedUsers = generateOptionMap(selectedKeys);
					for (int i = 0; i < selectedKeys.length; i++) {
						if (keys != null) {
							keys = keys + "|||" + selectedKeys[i]+":BC";
							values = values
									+ "|||"
									+ (String) selectedUsers
											.get(selectedKeys[i]);
						} else {
							keys = selectedKeys[i]+":BC";
							values = (String) selectedUsers
									.get(selectedKeys[i]);
						}
					}
					return new Forward("select_personal");
				} else {
					return null;
				}
            }
            catch (Exception e) {
                Log.getLog(getClass()).error("Error processing action " + action + ": " + e.toString(), e);
                return new Forward("error");
            }
        }

    }
    protected Map generateOptionMap(String[] ids) throws SecurityException, DaoException {
		Map usersMap = new SequencedHashMap();
		if (ids == null || ids.length == 0) {
			return usersMap;
		}

		try {
			
			String sort = "username";
			AddressBookModule dm = getModule();
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
			
				if (name == null) {
					name = "---";
				} else if (name.trim().length() > 50) {
					name = name.substring(0, 50) + "..";
				}
				usersMap.put(ids[j], name);
			}
			
		} catch (Exception e) {
			Log.getLog(getClass()).error("Error retrieving users", e);
		}

		return usersMap;
	}
	public String getKeys() {
		return keys;
	}

	public void setKeys(String keys) {
		this.keys = keys;
	}

	public String getValues() {
		return values;
	}

	public void setValues(String values) {
		this.values = values;
	}

}
