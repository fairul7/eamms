package com.tms.collab.directory.ui;

import kacang.Application;
import kacang.model.DaoException;
import kacang.model.DaoQuery;
import kacang.model.operator.DaoOperator;
import kacang.model.operator.OperatorIn;
import kacang.services.security.SecurityException;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.stdui.SelectBox;
import kacang.stdui.SortableSelectBox;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;
import org.apache.commons.collections.SequencedHashMap;

import com.tms.collab.directory.model.AddressBookDao;
import com.tms.collab.directory.model.AddressBookModule;
import com.tms.collab.directory.model.Contact;
import com.tms.collab.directory.model.DirectoryModule;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Generic select box that allows a user to select options from a popup window.
 * Subclasses need to override the methods initPopupTable() to return a AddressBookSelectBoxTable
 * for selection, and generateOptionMap(String[] ids) to generate a Map of value=label
 * pairs based on the current selection.
 * NOTE: Remember to call the init() method after adding this widget to a form.
 */
public class AddressBookSelectBox extends SortableSelectBox {

     SelectBox sbOptions;    
    boolean sortable;

    public AddressBookSelectBox() {
        setSortable(false);
    }

    public AddressBookSelectBox(String name) {
        super(name);
        setSortable(false);
    }

    public void init() {
        super.init();
    }

    public boolean isSortable() {
        return sortable;
    }

    public void setSortable(boolean sortable) {
        this.sortable = sortable;
    }

    /**
     * Gets the current options for the select box.
     * @return never null, empty array if no options
     */
    public String[] getIds() {
        Map optionMap = getOptionMap();
        if (optionMap != null) {
            Collection idSet = optionMap.keySet();
            idSet.remove("");
            String[] idArray = (String[])idSet.toArray(new String[0]);
            return idArray;
        }
        else {
            return new String[0];
        }
    }

    /**
     * Sets the current options for the select box.
     * @param ids
     */
    public void setIds(String[] ids) {
        if (ids != null) {
            try {
                Map optionMap = generateOptionMap(ids);
                setOptionMap(optionMap);

                if (sbOptions != null) {
                    sbOptions.setOptionMap(optionMap);
                }
            }
            catch (Exception e) {
                Log.getLog(getClass()).error("Error setting options: " + e.toString(), e);
            }
        }
        else {
            setOptionMap(new SequencedHashMap());
        }
    }
    
    /*public Forward onSubmit(Event event) {
        Forward forward = super.onSubmit(event);

        // retrieve users
        Map optionMap = getOptionMap();
        if (optionMap != null) {
            String[] idArray = (String[])optionMap.keySet().toArray(new String[0]);
            setIds(idArray);
        }

        return forward;
    }
*/
    public String getDefaultTemplate() {
        return "addressBookSelectBox";
    }    
       
     
    /**
     * Generates a Map of value=label pairs for the selected options.
     * @param ids The identifiers for the selected options.
     * @return
     */
    protected Map generateOptionMap(String[] ids) throws SecurityException, DaoException {
		Map usersMap = new SequencedHashMap();
		if (ids == null || ids.length == 0) {
			return usersMap;
		}
		String sort = "username";
		// Retriving users from Personal Contacts
		try {
			Application app = Application.getInstance();
			AddressBookModule dm = (AddressBookModule)app.getModule(AddressBookModule.class);
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
					usersMap.put(ids[j]+":BC", name);
				}
			}
		} catch (Exception e) {
			Log.getLog(getClass()).error("Error retrieving personal contacts", e);
		}

//		 Retriving users from Bussiness Contacts
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
					usersMap.put(ids[j]+":BC", name);
				}
			}
		} catch (Exception e) {
			Log.getLog(getClass()).error("Error retrieving bussiness contacts", e);
		}
		//Retriving users from Intranet contacts list
		try {
			Application app = Application.getInstance();
			SecurityService security = (SecurityService) app
					.getService(SecurityService.class);
			DaoQuery query = new DaoQuery();
		
			query.addProperty(new OperatorIn("id", ids,
					DaoOperator.OPERATOR_AND));
			Collection userList = security.getUsers(query, 0, -1, sort, false);

			// build users map
			Map tmpMap = new SequencedHashMap();
				if(userList.size()>0){
					for (Iterator i = userList.iterator(); i.hasNext();) {
						User user = (User) i.next();
						String label = user.getName();
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
				}
			} catch (SecurityException e) {
			Log.getLog(getClass()).error("Error retrieving users", e);
		}
		
		return usersMap;
	}

}
