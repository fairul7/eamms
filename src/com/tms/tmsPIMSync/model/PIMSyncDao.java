package com.tms.tmsPIMSync.model;

import kacang.model.DataSourceDao;
import kacang.model.DaoException;
import kacang.model.DataObjectNotFoundException;
import kacang.util.Log;
import kacang.util.JdbcUtil;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: blake
 * Date: Jun 25, 2006
 * Time: 7:19:27 PM
 * tmsPIMSync DAO object. Need to follow funambol DS Server data schema
 */
public class PIMSyncDao extends DataSourceDao {
    protected final String FUNAMBOL_USER_TABLE = "fnbl_user";
    protected final String FUNAMBOL_ROLE_TABLE = "fnbl_user_role";
    protected final String FUNAMBOL_PRINCIPAL_TABLE = "fnbl_principal";
    protected final String FUNAMBOL_DEVICE_TABLE = "fnbl_device";
    protected final String FUNAMBOL_IDSPACE_TABLE = "fnbl_id";
    protected final String FUNAMBOL_ROLE_NORMAL = "sync_user";



    public void init() throws DaoException {
        super.update("INSERT INTO " + FUNAMBOL_DEVICE_TABLE + " (id, type, server_password) SET VALUES(?,?)", new Object[]{"sc-pim-outlook", "desktop app", "fnbl"}) ;
    }

    /**
     * Return a sync user
     * @param username username to locate
     * @return SyncUser a SyncUser object if found or NULL if not found
     */
    public SyncUser findUser(String username) throws Exception {
        Collection col = null;
        try {
            col = super.select("SELECT username, password, email, first_name, last_name FROM " +
                    FUNAMBOL_USER_TABLE + " WHERE username=?", SyncUser.class, new Object[]{username}, 0, -1);
        } catch (DaoException e) {
            throw new Exception("Error finding sync user with username: " + username);
        }

        if(col.size() > 0){
            return (SyncUser) col.iterator().next();
        } else return null;
    }

    /**
     * Return all sync users
     * @return Collection a collaction of SyncUser objects
     */
    public Collection findAllUser() throws Exception {
        Collection col = null;
        try {
            col = super.select("SELECT username, password, email, first_name, last_name FROM " +
                    FUNAMBOL_USER_TABLE, SyncUser.class, null, 0, -1);
        } catch (DaoException e) {
            throw new Exception("Error finding all sync user", e);
        }
        return col;
    }

    /**
     *
     * @param su
     * @throws Exception
     */
    public void addUser(SyncUser su) throws Exception {
        // insert user
        try{
            super.update("INSERT into " + FUNAMBOL_USER_TABLE + "(username, password, email, first_name, last_name) " +
                "VALUES(#username#, #password#, #email#, #first_name#, #last_name#)", su);
        // insert default role
        super.update("INSERT into " + FUNAMBOL_ROLE_TABLE + "(username, role) " +
                "VALUES(?, ?)", new Object[]{su.getUsername(), FUNAMBOL_ROLE_NORMAL});
        }catch (DaoException e){
            throw new Exception("error inserting new sync user: " + su.getUsername(), e);
        }
    }

    public void updateUser(SyncUser su) throws Exception {
        try {
            super.update("UPDATE " + FUNAMBOL_USER_TABLE + " SET first_name=#first_name#, last_name=#last_name#, email=#email#, password=#password# WHERE username=#username#", su);
        } catch (DaoException e) {
            throw new Exception("Error updating sync user: " + su.getUsername(), e);
        }
    }

    /**
     * Remove all existence of this user from Funambol DS server
     * @param username
     */
    public void deleteUser(String username){
        // delete principals
        Collection principals = findPrincipalsByUser(username);
        for(Iterator itr=principals.iterator(); itr.hasNext(); ){
            PIMSyncPrincipal principal = (PIMSyncPrincipal) itr.next();
            deletePrincipal(principal.getId());
        }

        try {
            super.update("DELETE FROM " + FUNAMBOL_ROLE_TABLE + " WHERE username=?", new Object[]{username});
        } catch (DaoException e) {
            Log.getLog(getClass()).debug("couldnt delete sync user role, probably doesnt exist");
        }
        try {
            super.update("DELETE FROM " + FUNAMBOL_USER_TABLE + " WHERE username=?", new Object[]{username});
        } catch (DaoException e) {
            Log.getLog(getClass()).debug("couldnt delete sync user, probably doesnt exist");
        }
    }


    public Collection findAllUser(String filter, String value, int rows, String sort, boolean desc) throws Exception {
        Collection col;
        String filterStr = "";

        // filter must have a valid value: username, email, first_name, last_name and value param cannot be null
        // or empty if filter has a valid value
        if(filter != null && !filter.equals("")){
            if(value !=null && !value.equals("")){
                filterStr = " WHERE " + filter + " LIKE '%"+value+"%' ";
            }
        }

        try {
            col = super.select("SELECT username, password, email, first_name, last_name FROM " +
                    FUNAMBOL_USER_TABLE + filterStr + JdbcUtil.getSort(sort, desc), SyncUser.class, null, 0, rows);
        } catch (DaoException e) {
            throw new Exception("Error finding all sync user", e);
        }
        return col;
    }

    public Integer countFindAllUser(String filter, String value) throws Exception {
        Collection col;
        String filterStr = "";

        // filter must have a valid value: username, email, first_name, last_name and value param cannot be null
        // or empty if filter has a valid value
        if(filter != null && !filter.equals("")){
            if(value !=null && !value.equals("")){
                filterStr = " WHERE " + filter + " LIKE '%"+value+"%' ";
            }
        }

        try {
            col = super.select("SELECT COUNT(username) AS total FROM " +
                    FUNAMBOL_USER_TABLE + filterStr, HashMap.class, null, 0, -1);
        } catch (DaoException e) {
            throw new Exception("Error finding all sync user", e);
        }

        HashMap map = (HashMap) col.iterator().next();
        Integer total = (Integer) map.get("total");

        return total;

    }

     public boolean userExist(String username){
        Collection col;
        try {
            col = super.select("SELECT username FROM " + FUNAMBOL_USER_TABLE +
                    " WHERE username=?", SyncUser.class, new Object[]{username}, 0, -1);

            if(col.size() > 0){
                return true;
            }else return false;
        } catch (DaoException e) {
            Log.getLog(getClass()).debug("error in looking for SyncUser", e);
        }
        return false;
    }

    public void addPrincipal(String username, String deviceId){
        try {
            // ignore if principal exist
            if(principalExist(username, deviceId)) return;

            Collection col = super.select("SELECT counter+1 AS nextId FROM " + FUNAMBOL_IDSPACE_TABLE + " WHERE idspace=?", HashMap.class, new Object[]{"principal"}, 0, 1);
            HashMap map = (HashMap) col.iterator().next();
            Number nextId = (Number) map.get("nextId");

            super.update("INSERT INTO " + FUNAMBOL_PRINCIPAL_TABLE + " (username, device, id) " +
                    "VALUES(?, ?, ?)", new Object[]{username, deviceId, nextId});
            super.update("UPDATE " + FUNAMBOL_IDSPACE_TABLE + " set counter=? WHERE idspace=?", new Object[]{nextId, "principal"});
        } catch (DaoException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public boolean principalExist(String username, String deviceId){
        Collection col;
        try {
            col = super.select("SELECT username, device FROM " + FUNAMBOL_PRINCIPAL_TABLE +
                    " WHERE username=? and device=?", HashMap.class, new Object[]{username, deviceId}, 0, -1);

            if(col.size() > 0){
                return true;
            }else return false;
        } catch (DaoException e) {
            Log.getLog(getClass()).debug("error in checkPrincipal", e);
        }
        return false;
    }

    public Collection findPrincipalsByUser(String username){
        Collection col = new ArrayList();
        try {
            col = super.select("SELECT username, device, id FROM " + FUNAMBOL_PRINCIPAL_TABLE +
            " WHERE username=?", PIMSyncPrincipal.class, new Object[]{username}, 0, -1);

        } catch (DaoException e) {
            Log.getLog(getClass()).debug("Cant get collection of principal for username: " + username, e);

        }
        return col;
    }

    /**
     * This is more complicated. We have to remove everything which depend on this principal
     * @param id
     */
    public void deletePrincipal(String id){
        try {
            super.update("delete from fnbl_client_mapping where principal=?", new Object[]{id});
        } catch (DaoException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        try {
            super.update("delete from fnbl_last_sync where principal=?", new Object[]{id});
        } catch (DaoException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        try {
            super.update("DELETE FROM " + FUNAMBOL_PRINCIPAL_TABLE + " WHERE id=?", new Object[]{id});
        } catch (DaoException e) {
            Log.getLog(getClass()).debug("error deleting principal");
        }
    }


    public void deletePrincipal(String username, String deviceId) {
        try {
            Collection col = super.select("SELECT username, device, id FROM " + FUNAMBOL_PRINCIPAL_TABLE +
                    " WHERE username=? AND device=?", PIMSyncPrincipal.class, new Object[]{username, deviceId}, 0, 1);

            if(col.size() > 0){
                PIMSyncPrincipal principal = (PIMSyncPrincipal) col.iterator().next();
                deletePrincipal(principal.getId());
            }
        } catch (DaoException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }


    public Collection getAllPrincipals(String username) {
        Collection col = null;
        try {
            col = super.select("SELECT username, device, id FROM " + FUNAMBOL_PRINCIPAL_TABLE +
                " WHERE username=?", PIMSyncPrincipal.class, new Object[]{username}, 0, -1);
        } catch (DaoException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return col;
    }

    public void saveDevice(PIMSyncDevice device) {
        try {
            super.update("DELETE FROM " + FUNAMBOL_DEVICE_TABLE + " WHERE id=?", new Object[]{device.getId()});
        } catch (DaoException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        try {
            super.update("INSERT INTO " + FUNAMBOL_DEVICE_TABLE + " (id, description, type, server_password, id_caps) " +
                "VALUES(?,?,?, 'fnbl', '-1')", new Object[]{device.getId(), device.getDescription(), device.getType()});
        } catch (DaoException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public PIMSyncDevice getDevice(String id) {
        try {
            Collection col = super.select("SELECT id, description, type FROM " + FUNAMBOL_DEVICE_TABLE + " WHERE id=?", PIMSyncDevice.class, new Object[]{id}, 0, -1);
            if(col.size() > 0){
                return (PIMSyncDevice) col.iterator().next();
            }
        } catch (DaoException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return null;
    }

    public void cleanup() throws DaoException
    {
        super.update("DELETE FROM fnbl_device_datastore", null);
        super.update("DELETE FROM fnbl_ds_cttype_tx", null);
        super.update("DELETE FROM fnbl_ds_cttype_rx", null);
        super.update("DELETE FROM fnbl_ds_mem", null);
    }
}
