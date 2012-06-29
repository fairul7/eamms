package com.tms.cms.medialib.model;

import kacang.Application;
import kacang.model.*;
import kacang.model.operator.*;
import kacang.util.*;
import kacang.services.security.*;
import org.apache.commons.collections.SequencedHashMap;
import java.util.*;
import java.sql.SQLException;

public class LibraryDaoSybase extends LibraryDaoMsSql {
	// note: getHighestRole is the same as the one in LibraryDao except for "[role]"
	public String getHighestRole(String libraryId) throws DaoException {
		ArrayList principalIds = getUserPrincipalIds();
		boolean isManager = false;
		boolean isContributor = false;
		boolean isViewer = false;
		String role = null;
        
		for(int i=0; i<principalIds.size() && ! isManager; i++) {
			String principalId = (String) principalIds.get(i);
            
			Collection col = super.select("SELECT id FROM cml_permission " +
					"WHERE principalId = ? AND [role] = 'manager' AND id = ?", HashMap.class, new Object[] {principalId, libraryId}, 0, -1);
			if(col.size() > 0) {
				isManager = true;
			}
		}
        
		if(! isManager) {
			for(int i=0; i<principalIds.size() && ! isContributor; i++) {
				String id = (String) principalIds.get(i);
	            
				Collection col = super.select("SELECT id FROM cml_permission " +
						"WHERE principalId = ? AND [role] = 'contributor' AND id = ?", HashMap.class, new Object[] {id, libraryId}, 0, -1);
				if(col.size() > 0) {
					isContributor = true;
				}
			}
		}
        
		if(! isManager && ! isContributor) {
			for(int i=0; i<principalIds.size() && ! isViewer; i++) {
				String id = (String) principalIds.get(i);
	            
				Collection col = super.select("SELECT id FROM cml_permission " +
						"WHERE principalId = ? AND [role] = 'viewer' AND id = ?", HashMap.class, new Object[] {id, libraryId}, 0, -1);
				if(col.size() > 0) {
					isViewer = true;
				}
			}
		}
        
		if(isManager) {
			role = "manager";
		}
		else {
			if(isContributor) {
				role = "contributor";
			}
			else if(isViewer){
				role = "viewer";
			}
		}
        
		return role;
	}
	
	// note: isManager is the same as the one in LibraryDao except for "[role]"
    public boolean isManager() throws DaoException {
        ArrayList principalIds = getUserPrincipalIds();
        boolean isManager = false;
        
        for(int i=0; i<principalIds.size() && ! isManager; i++) {
            String id = (String) principalIds.get(i);
            
            Collection col = super.select("SELECT id FROM cml_permission " +
            		"WHERE principalId = ? AND [role] = 'manager'", HashMap.class, new Object[] {id}, 0, -1);
            if(col.size() > 0) {
                isManager = true;
            }
        }
        return isManager;
    }
	
	// note: isManager is the same as the one in LibraryDao except for "[role]"
    public boolean isManager(String libraryId) throws DaoException {
        ArrayList principalIds = getUserPrincipalIds();
        boolean isManager = false;
        
        for(int i=0; i<principalIds.size() && ! isManager; i++) {
            String id = (String) principalIds.get(i);
            
            Collection col = super.select("SELECT id FROM cml_permission " +
            		"WHERE principalId = ? AND [role] = 'manager' AND id = ?", HashMap.class, new Object[] {id, libraryId}, 0, -1);
            if(col.size() > 0) {
                isManager = true;
            }
        }
        return isManager;
    }
	
	// note: isAccessible is the same as the one in LibraryDao except for "[role]"
    public boolean isAccessible(String libraryId) throws DaoException {
        boolean isAccessible = isAccessibleToAll();
        ArrayList principalIds = getUserPrincipalIds();
        
        for(int i=0; i<principalIds.size() && ! isAccessible; i++) {
            String id = (String) principalIds.get(i);
            
            Collection col = super.select("SELECT id FROM cml_permission " +
            		"WHERE principalId = ? AND [role] = 'viewer' AND id = ?", HashMap.class, new Object[] {id, libraryId}, 0, -1);
            if(col.size() > 0) {
                isAccessible = true;
            }
        }
        
        return isAccessible;
    }
	
	// note: updateLibraryAccess is the same as the one in LibraryDao except for "[role]"
    protected void updateLibraryAccess(Transaction tx, String libraryId, Map managerGroup, Map contributorGroup, Map viewerGroup) throws SQLException {
        // delete existing entries
        tx.update("DELETE FROM cml_permission WHERE id=?", new Object[] { libraryId });
        
        String sqlInsertPermission = "INSERT INTO cml_permission (id, [role], principalId) VALUES (?, ?, ?)";

        // insert manager group
        if (managerGroup != null)
        {
            for (Iterator i=managerGroup.keySet().iterator(); i.hasNext();)
            {
                String principalId = (String)i.next();
                tx.update(sqlInsertPermission, new Object[] { libraryId, "manager", principalId });
                tx.update(sqlInsertPermission, new Object[] { libraryId, "contributor", principalId });
                tx.update(sqlInsertPermission, new Object[] { libraryId, "viewer", principalId });
            }
        }
        
        // insert contributor group
        if (contributorGroup != null)
        {
            for (Iterator i=contributorGroup.keySet().iterator(); i.hasNext();)
            {
                String principalId = (String)i.next();
                tx.update(sqlInsertPermission, new Object[] { libraryId, "contributor", principalId });
                tx.update(sqlInsertPermission, new Object[] { libraryId, "viewer", principalId });
            }
        }
        
        // insert viewer group
        if (viewerGroup != null)
        {
            for (Iterator i=viewerGroup.keySet().iterator(); i.hasNext();)
            {
                String principalId = (String)i.next();
                tx.update(sqlInsertPermission, new Object[] { libraryId, "viewer", principalId });
            }
        }
    }
	
	// note: setLibraryAccess is the same as the one in LibraryDao except for "[role]"
    protected LibraryObject setLibraryAccess(LibraryObject library) throws DaoException {        
        String sqlManager = "SELECT principalId from cml_permission WHERE id = ? AND [role] = 'manager'";
        String sqlContributor = "SELECT principalId from cml_permission WHERE id = ? AND [role] = 'contributor'";
        String sqlViewer = "SELECT principalId from cml_permission WHERE id = ? AND [role] = 'viewer'";
        Map managerGroupMap = new SequencedHashMap();
        Map contributorGroupMap = new SequencedHashMap();
        Map viewerGroupMap = new SequencedHashMap();
        Collection groupIdList;
        Collection groupList;
        
        try
        {
            SecurityService security = (SecurityService)Application.getInstance().getService(SecurityService.class);

            // get manager groups
            groupIdList = new ArrayList();
            Collection managerGroupList = super.select(sqlManager, HashMap.class, new Object[] {library.getId()}, 0, -1);
            for(Iterator i=managerGroupList.iterator(); i.hasNext();) {
                Map row = (Map) i.next();
                groupIdList.add(row.get("principalId"));
            }
            if(groupIdList.size() > 0) {
                groupList = security.getGroups(new DaoQuery().addProperty(new OperatorIn("id", groupIdList.toArray(), DaoOperator.OPERATOR_AND)), 0, -1, "groupName", false);
                for (Iterator j=groupList.iterator(); j.hasNext();)
                {
                    Group group = (Group) j.next();
                    managerGroupMap.put(group.getId(), group.getName());
                }
            }
            library.setManagerGroup(managerGroupMap);
            
            // get contributor groups
            groupIdList = new ArrayList();
            Collection contributorGroupList = super.select(sqlContributor, HashMap.class, new Object[] {library.getId()}, 0, -1);
            for(Iterator i=contributorGroupList.iterator(); i.hasNext();) {
                Map row = (Map) i.next();
                groupIdList.add(row.get("principalId"));
            }
            if(groupIdList.size() > 0) {
                groupList = security.getGroups(new DaoQuery().addProperty(new OperatorIn("id", groupIdList.toArray(), DaoOperator.OPERATOR_AND)), 0, -1, "groupName", false);
                for (Iterator j=groupList.iterator(); j.hasNext();)
                {
                    Group group = (Group) j.next();
                    contributorGroupMap.put(group.getId(), group.getName());
                }
            }
            library.setContributorGroup(contributorGroupMap);
            
            // get viewer groups
            groupIdList = new ArrayList();
            Collection viewerGroupList = super.select(sqlViewer, HashMap.class, new Object[] {library.getId()}, 0, -1);
            for(Iterator i=viewerGroupList.iterator(); i.hasNext();) {
                Map row = (Map) i.next();
                groupIdList.add(row.get("principalId"));
            }
            if(groupIdList.size() > 0) {
                groupList = security.getGroups(new DaoQuery().addProperty(new OperatorIn("id", groupIdList.toArray(), DaoOperator.OPERATOR_AND)), 0, -1, "groupName", false);
                for (Iterator j=groupList.iterator(); j.hasNext();)
                {
                    Group group = (Group) j.next();
                    viewerGroupMap.put(group.getId(), group.getName());
                }
            }
            library.setViewerGroup(viewerGroupMap);
        }
        catch (DaoException e)
        {
            throw e;
        }
        catch (kacang.services.security.SecurityException e)
        {
            throw new DaoException("Error getting library groups " + e.toString());
        }
        
        return library;
    }
	
	// note: getAccessibleLibrary is the same as the one in LibraryDao except for "[role]"
    protected ArrayList getAccessibleLibrary(ArrayList principalIds) throws DaoException {
        ArrayList accessibleLibrary = new ArrayList();
        String condition = "(";
        for(int i=0; i<principalIds.size(); i++) {
            if(i==0) {
                condition += "principalId = '" + (String) principalIds.get(i) + "'"; 
            }
            else {
                condition += " OR principalId = '" + (String) principalIds.get(i) + "'";
            }
        }
        condition += ")";
        Collection col = super.select("SELECT distinct id FROM cml_permission " +
        		"WHERE [role] = 'viewer' AND " + condition, HashMap.class, null, 0, -1);
        Map map;
        for(Iterator i=col.iterator(); i.hasNext();) {
            map = (Map) i.next();
            accessibleLibrary.add((String) map.get("id"));
        }
        
        return accessibleLibrary;
    }
	
	// note: getEditableLibrary is the same as the one in LibraryDao except for "[role]"
    protected ArrayList getEditableLibrary(ArrayList principalIds) throws DaoException {
        ArrayList accessibleLibrary = new ArrayList();
        String condition = "(";
        for(int i=0; i<principalIds.size(); i++) {
            if(i==0) {
                condition += "principalId = '" + (String) principalIds.get(i) + "'"; 
            }
            else {
                condition += " OR principalId = '" + (String) principalIds.get(i) + "'";
            }
        }
        condition += ")";
        Collection col = super.select("SELECT distinct id FROM cml_permission " +
        		"WHERE [role] = 'manager' AND " + condition, HashMap.class, null, 0, -1);
        Map map;
        for(Iterator i=col.iterator(); i.hasNext();) {
            map = (Map) i.next();
            accessibleLibrary.add((String) map.get("id"));
        }
        
        return accessibleLibrary;
    }
}
