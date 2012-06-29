/*
 * Created on Apr 23, 2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.tms.crm.sales.model;

import java.util.*;
import kacang.model.*;

/**
 * @author Paul Pak
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class SalesGroupDao extends DataSourceDao {

    public void insertGroup(SalesGroup group ) throws DaoException {
        super.update(" INSERT INTO salesgroup(id,name,description) VALUES( #id# , #name# , #description# ) ",group);
        Set ids = group.getMemberIdSet();
        if(ids!=null&&ids.size()>0){
            DefaultDataObject obj = new DefaultDataObject();
            obj.setProperty("groupId",group.getId());
            for (Iterator iterator = ids.iterator(); iterator.hasNext();) {
                String id = (String) iterator.next();
                obj.setProperty("userId",id);
                super.update("INSERT INTO sfa_groupuser(groupId,userId) VALUES( #groupId# , #userId# )",obj);
            }
        }
    }

    public Collection selectSalesGroups(String userId) throws DaoException {
        return super.select("SELECT id, name , description FROM salesgroup,sfa_groupuser " +
                "WHERE sfa_groupuser.userId=? AND sfa_groupuser.groupId=salesgroup.id ",
                SalesGroup.class,new Object[]{userId},0,-1);

    }

    public void deleteGroupUsers(SalesGroup group) throws DaoException {
        DefaultDataObject obj = new DefaultDataObject();
        obj.setProperty("groupId",group.getId());
        super.update("DELETE FROM sfa_groupuser WHERE groupId= #groupId#",obj);

    }

    public void updateGroup(SalesGroup group ) throws DaoException {
        super.update(" UPDATE salesgroup SET name=#name#, description=#description# WHERE id=#id#",group);
        Set ids = group.getMemberIdSet();
        if(ids!=null&&ids.size()>0){
            DefaultDataObject obj = new DefaultDataObject();
            obj.setProperty("groupId",group.getId());
            for (Iterator iterator = ids.iterator(); iterator.hasNext();) {
                String id = (String) iterator.next();
                obj.setProperty("userId",id);
                super.update("INSERT INTO sfa_groupuser(groupId,userId) VALUES( #groupId# , #userId# )",obj);
            }
        }

    }

    public void deleteGroup(String groupId) throws DaoException {
        DefaultDataObject obj = new DefaultDataObject();
        obj.setId(groupId);
        super.update("DELETE FROM salesgroup WHERE id= #id#",obj);
        super.update("DELETE FROM sfa_groupuser WHERE groupId= #id#",obj);

    }


    public Collection listGroups(String filter, String sort, boolean desc,int startRow,int rows) throws DaoException {
        String sql = "SELECT id, name,description FROM salesgroup ";
        String whereClause = "";
        Object [] args = null;
        if(filter!=null && filter.trim().length()>0){
            filter = "%"+filter+"%";
            whereClause = " WHERE name LIKE ? or description LIKE ? ";
            args = new Object[]{filter,filter};
        }
        String order = "";
        if(sort!=null&&sort.trim().length()>0){
            order = "ORDER BY "+ sort + (desc?" DESC":"");
        }
        return super.select(sql+whereClause+order,SalesGroup.class,args,startRow,rows);
    }

    public int countGroups(String filter) throws DaoException {
        String sql = "SELECT COUNT(id) as total  FROM salesgroup ";
        String whereClause = "";
        Object [] args = null;
        if(filter!=null && filter.trim().length()>0){
            filter = "%"+filter+"%";
            whereClause = " WHERE name LIKE ? or description LIKE ? ";
            args = new Object[]{filter,filter};
        }
        return Integer.parseInt(((HashMap)super.select(sql+whereClause,HashMap.class,args,0,-1).iterator().next()).get("total").toString());
    }



	public void insertRecord(SalesGroup grp) throws DaoException {
		super.update("INSERT INTO salesgroup (id) VALUES (#id#)", grp);
	}


	public void deleteRecords() throws DaoException {
		super.update("DELETE FROM salesgroup", null);
	}
	
	public Hashtable getSalesGroups() throws DaoException {
		Collection col = super.select("SELECT id FROM salesgroup", SalesGroup.class, null, 0, -1);
		
		Hashtable ht = new Hashtable();
		Iterator iterator = col.iterator();
		while (iterator.hasNext()) {
			SalesGroup sg = (SalesGroup) iterator.next();
			ht.put(sg.getId(), "");
		}
		return (ht);
	}

    public SalesGroup selectSalesGroup(String groupId) throws DaoException {
        SalesGroup group = null;
        Collection col = super.select("SELECT id,name,description FROM salesgroup WHERE id=?",SalesGroup.class,new Object[]{groupId},0,-1);
        if(col!=null && col.size()>0){
            group = (SalesGroup) col.iterator().next();
            col = super.select("SELECT userId as userId FROM sfa_groupuser WHERE groupId = ? ",HashMap.class, new Object[]{groupId},0,-1);
            if(col!=null){
                TreeSet set = new TreeSet();
                for (Iterator iterator = col.iterator(); iterator.hasNext();) {
                    HashMap hashMap = (HashMap) iterator.next();
                    set.add(hashMap.get("userId"));
                }
                group.setMemberIdSet(set);
            }
        }

        return group;
    }

}