package com.tms.collab.resourcemanager.model;

import kacang.model.DataSourceDao;
import kacang.model.DaoException;
import kacang.model.DefaultDataObject;
import kacang.util.Log;
import kacang.util.Transaction;

import java.util.*;

public class ResourceManagerDao extends DataSourceDao
{
    Log log = Log.getLog(getClass());

    public void init() throws DaoException
    {
        super.update(" CREATE TABLE rm_access (" +
                "resourceId varchar(50) NOT NULL default '0'," +
                "id varchar(100) NOT NULL default '0'," +
                "type tinyint(3) unsigned NOT NULL default '0', " +
                "name varchar(25) NOT NULL default '0' " +
                ") TYPE=MyISAM",null);

        super.update("CREATE TABLE rm_authority (" +
                "resourceId varchar(255) NOT NULL default '0', " +
                "userId varchar(255) NOT NULL default '0' " +
                ") TYPE=MyISAM",null);

        super.update(" CREATE TABLE rm_booking ( " +
                "eventId varchar(100) NOT NULL default ''," +
                "instanceId varchar(255) default NULL, " +
                "resourceId varchar(100) NOT NULL default ''," +
                "startDate datetime default '0000-00-00 00:00:00',  " +
                "endDate datetime default NULL," +
                "userId varchar(100) NOT NULL default '', " +
                "returnedDate datetime default NULL," +
                "status char(3) default NULL,  " +
                "comments varchar(255) default NULL  " +
                ") TYPE=MyISAM",null);

        super.update(" CREATE TABLE rm_category ( " +
                "id varchar(255) NOT NULL default '0', " +
                "name varchar(255) NOT NULL default '0' " +
                ") TYPE=MyISAM",null);

        super.update("CREATE TABLE rm_resource (" +
                "id varchar(255) NOT NULL default ''," +
                "name varchar(255) NOT NULL default ''," +
                "creationDate datetime NOT NULL default '0000-00-00 00:00:00',  " +
                "modificationDate datetime default NULL," +
                "creator varchar(255) NOT NULL default ''," +
                "description text," +
                "modifiedBy varchar(255) default NULL," +
                "requireApproval char(1) binary NOT NULL default '0'," +
                "image varchar(255) default NULL," +
                "approved char(1) binary NOT NULL default '0'," +
                "imageType tinyint(3) unsigned default NULL," +
                "active char(1) binary NOT NULL default '0'," +
                "classification tinyint(3) unsigned NOT NULL default '1'," +
                "deleted char(1) binary NOT NULL default '0'," +
                "categoryId varchar(255) default NULL," +
                "status char(3) default NULL," +
                "bookingId varchar(255) default NULL" +
                ") TYPE=MyISAM",null);

    }

    public int getResourcesCount(String[] userIds,String[] groupIds) throws DaoException
    {
        String idClause = "";
        if(userIds!= null && userIds.length>0){
            idClause = quote(userIds[0]);
            for(int i=1;i<userIds.length;i++)
                idClause += ","+quote(userIds[i]);
            // userClause += ")) ";
        }
        if(groupIds != null && groupIds.length>0){
            if(!idClause.equals(""))
                idClause += ",";
            idClause += quote(groupIds[0]);
            for(int i=1;i<groupIds.length;i++)
                idClause += ","+quote(groupIds[i]);
            //  groupClause += ")) ";
        }
        String accessJoin = "INNER JOIN rm_access ra ON r.id = ra.resourceId ";
        if(!idClause.equals("")){
            accessJoin += "AND ra.id IN(";
            accessJoin += idClause + ")";
        }
        String sql = "SELECT COUNT(DISTINCT r.id) as total " +
                "from rm_resource r "  + accessJoin ;//, //rm_access ra WHERE
        return ((Integer)((HashMap)super.select(sql,HashMap.class,null,0,-1).iterator().next()).get("total")).intValue();
    }

    public void deleteBooking(String eventId){
        try{
            String sql = "DELETE FROM rm_booking WHERE eventId=? ";
            super.update(sql,new String[]{eventId});
/*
            sql = "UPDATE rm_resource set status='"+ResourceManager.RESOURCE_STATUS_AVAILABLE+
                    "', bookingId='' WHERE bookingId = ?";
            super.update(sql,new String[]{eventId});
*/
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void deleteBooking(String resourceId,String eventId,String instanceId) throws DaoException
    {
        String sql = "DELETE FROM rm_booking WHERE eventId=? AND resourceId=? AND instanceId = ?";
        super.update(sql,new String[]{eventId,resourceId,instanceId});
        sql = "UPDATE rm_resource set status='"+ResourceManager.RESOURCE_STATUS_AVAILABLE+
                "', bookingId='' WHERE bookingId = ? AND id =?";
        super.update(sql,new String[]{eventId,resourceId});
    }

    public Collection selectAuthorizedMembers(String resourceId){
        try{
            String sql = "SELECT userId as userId FROM rm_authority  WHERE resourceId =?";
            Collection col = super.select(sql,HashMap.class,new String[]{resourceId},0,-1);
            Collection ids = new TreeSet();
            for (Iterator iterator = col.iterator(); iterator.hasNext();)
            {
                HashMap hashMap = (HashMap) iterator.next();
                ids.add(hashMap.get("userId"));
            }
            return ids;
        }catch(Exception e){
            log.error(e);
        }
        return null;
    }

    public Resource selectResource(String id,boolean onlyApproved) throws DaoException
    {
        String sql = "SELECT r.id,r.name,creationDate,modificationDate,creator,status,"+
                "description,modifiedBy,requireApproval,image,imageType,approved,active,classification,deleted,categoryId, rc.name as category " +
                "from rm_resource r LEFT JOIN rm_category rc ON rc.id = r.categoryId WHERE r.id=? "+ (onlyApproved? " AND approved = '1'" : "");
        Collection col = super.select(sql,Resource.class,new String[]{id},0,-1);
        if(col.size()>0)
            return (Resource)col.iterator().next();
        return null;
    }

    public Collection selectBookedResources(String eventId,String instanceId) throws DaoException
    {
        String sql = "SELECT r.id,r.name,creationDate,modificationDate,creator,"+
                "description,modifiedBy,requireApproval,image,imageType,approved,active,classification,deleted,categoryId, rc.name as category,rb.status as status " +
                "from rm_resource r INNER JOIN rm_booking rb ON (rb.resourceId = r.id) LEFT JOIN rm_category rc ON rc.id = r.categoryId WHERE rb.eventId=? AND rb.instanceId=?";
        Collection col = super.select(sql,Resource.class,new String[]{eventId,instanceId},0,-1);
        for (Iterator iterator = col.iterator(); iterator.hasNext();)
        {
            Resource resource = (Resource) iterator.next();
            if(resource.isRequireApproval()){
                resource.setAuthorities(selectAuthorizedMembers(resource.getId()));
            }
        }
        return col;
    }

    public Collection selectResources(String filter,String catFilter,String[] userIds,String[] groupIds,boolean onlyApproved,boolean includeDeleted,boolean includePrivate,String sort, boolean desc, int startIndex,int maxRows) throws DaoException
    {
        String strSort= "";
        if (sort != null) {
            strSort += " ORDER BY " + sort;
            if (desc)
                strSort += " DESC";
        }
        String filterClause = "";
        filterClause = " r.name LIKE '%"+ (filter==null?"":filter) +"%' ";
        String idClause = "";
        if(userIds!= null && userIds.length>0){
            idClause = quote(userIds[0]);
            for(int i=1;i<userIds.length;i++)
                idClause += ","+quote(userIds[i]);
        }
        String catFilterClause = "";
        if(catFilter!=null&&catFilter.trim().length()>0){
            catFilterClause = " AND categoryId='"+catFilter+"' ";
        }
        if(groupIds != null && groupIds.length>0){
            if(!idClause.equals(""))
                idClause += ",";
            idClause += quote(groupIds[0]);
            for(int i=1;i<groupIds.length;i++)
                idClause += ","+quote(groupIds[i]);
        }
        String accessJoin = " ";
//            if(idClause!=null&&idClause.trim().length()>0){
        accessJoin += " ra.id IN(";
        accessJoin += idClause + ") " ;
        //  }
        String sql = "SELECT DISTINCT r.id,r.name,creationDate,modificationDate,creator,active,status,bookingId, "+
                "description,modifiedBy,requireApproval,image,imageType,approved,deleted,classification,categoryId,rc.name as category " +
                "from rm_resource r LEFT JOIN rm_access ra ON r.id = ra.resourceId "+ " LEFT JOIN rm_category rc ON rc.id = r.categoryId "+
                "WHERE ((" + accessJoin + ") OR"+ " classification="+
                ResourceManager.RESOURCE_CLASSIFICATION_TYPE_PUBLIC +
                (includePrivate?" OR classification="+ResourceManager.RESOURCE_CLASSIFICATION_TYPE_PRIVATE:"")+
                ")AND "+
                filterClause + catFilterClause+(onlyApproved?"AND approved = '1' ":"")
                + (includeDeleted?"":" AND deleted = '0' ") + strSort;
        return super.select(sql,Resource.class,null,startIndex,maxRows);
    }

    public Collection selectResourceAccessIds(String resourceId,boolean onlyApproved) throws DaoException
    {
        String sql = "SELECT DISTINCT ra.id from rm_access ra,rm_resource " +
                "WHERE rm_resource.id = ra.resourceId AND ra.resourceId = '"+resourceId + "'"+
                (onlyApproved?" AND rm_resource.approved = '1' ":"");
        return super.select(sql,HashMap.class,null,0,-1);
    }

    public Collection selectCategories() throws DaoException
    {
        String sql = "SELECT id as id, name as name FROM rm_category";
        return super.select(sql,DefaultDataObject.class,null,0,-1);
    }

    public String addResource(Resource resource)
    {
        try{
            String sql = "INSERT INTO rm_resource(id,name,creationDate,modificationDate,creator,"+
                    "description,modifiedBy,requireApproval,image,imageType,approved,deleted," +
                    "classification,active,categoryId) VALUES(#id#,#name#,#creationDate#," +
                    "#modificationDate#,#creator#,#description#,#modifiedBy#,#requireApproval#," +
                    "#image#,#imageType#,#approved#,#deleted#,#classification#,#active#,#categoryId#)";
            super.update(sql,resource);
            return resource.getId();
        }catch(Exception e){
            log.error(e.getMessage(),e);
        }
        return null;
    }

    public void insertCategory(String id, String name){
        try{
            String sql = "INSERT INTO rm_category(id,name)VALUES(?,?)";
            super.update(sql, new String[] { id, name });
        }catch(Exception e){
            log.error(e.getMessage(),e);
        }
    }

    public void deleteUnusedCategories() {
        Transaction tx = null;
        try {
            String sql = "SELECT c.id AS categoryId\n" +
                    "FROM rm_category c\n" +
                    "LEFT JOIN rm_resource r ON r.categoryId=c.id\n" +
                    "WHERE r.categoryId IS NULL";
            String sql2 = "DELETE FROM rm_category WHERE id=?";

            tx = super.getTransaction();
            tx.begin();
            Collection results = tx.select(sql, Resource.class, null, 0, -1);
            for (Iterator i=results.iterator(); i.hasNext();) {
                Resource rc = (Resource)i.next();
                tx.update(sql2, new String[] { rc.getCategoryId() });
            }
            tx.commit();
        }
        catch (Exception e) {
            log.error("Error deleting unused categories", e);
            tx.rollback();
        }
    }

    public void insertAuthorizedUser(String resourceId, String userId){
        try{
            String sql = "INSERT INTO rm_authority(resourceId,userId) VALUES(?,?)";
            super.update(sql,new String[]{resourceId,userId});
        } catch(Exception e){
            log.error(e);
        }
    }

    public void removeAuthorizedUser(Resource resource){
        try{
            String sql = "DELETE FROM rm_authority WHERE resourceId=?";
            super.update(sql,new String[]{resource.getId()});
        } catch(Exception e){
            log.error(e);
        }
    }

    public void insertBooking(ResourceBooking booking){
        try{
            String sql = "INSERT INTO rm_booking(eventId,instanceId,resourceId,startDate,endDate,userId,status,returnedDate)" +
                    "VALUES(#eventId#,#instanceId#,#resourceId#,#startDate#,#endDate#,#userId#,#status#,#returnedDate#)";
            super.update(sql,booking);
        } catch(Exception e){
            log.error(e.getMessage(),e);
        }
    }

    public void updateBookingStatus(String resourceId,String eventId,String instanceId,String status,String comments){
        try{
            ResourceBooking booking = new ResourceBooking();
            booking.setResourceId(resourceId);
            booking.setEventId(eventId);
            booking.setInstanceId(instanceId);
            booking.setStatus(status);
            booking.setComments(comments);
            String sql = "UPDATE rm_booking SET status=#status#, comments=#comments# WHERE resourceId=#resourceId# AND eventId=#eventId# AND" +
                    " instanceId=#instanceId#";
            super.update(sql,booking);
        } catch(Exception e){
            log.error(e.getMessage(),e);
        }
    }

    public Collection selectResourceBookings(String resourceId,Date startDate,Date endDate){
        try{
            String sql="SELECT eventId,instanceId,resourceId,startDate,endDate,userId,status,returnedDate FROM rm_booking WHERE" +
                    " resourceId=? AND startDate<? AND endDate>? ORDER BY startDate DESC";
            return super.select(sql,ResourceBooking.class,new Object[]{resourceId,endDate,startDate},0,-1);
        }catch(Exception e){
            log.error(e.getMessage(),e);
        }
        return null;
    }

    public boolean hasPendingBooking(String resourceId){
        boolean hasPending = false;

        try{
            String sql = "SELECT COUNT(*) AS total FROM rm_booking WHERE resourceId=? AND status=?";
            Collection collection = super.select(sql,HashMap.class,new String[]{resourceId,ResourceManager.RESOURCE_BOOKING_PENDING},0,-1);
            HashMap hashMap = (HashMap) collection.iterator().next();
            int total = Integer.parseInt(hashMap.get("total").toString());

            if(total>0) {
                hasPending = true;
            }
        }catch(Exception e){
            log.error(e.getMessage(),e);
        }
        return hasPending;
    }

    public Collection selectResourceBooking(String resourceId,String eventId,String instanceId){
        try{
            String sql="SELECT eventId,instanceId,resourceId,startDate,endDate,userId,status,returnedDate FROM rm_booking WHERE" +
                    " resourceId=? AND eventId=? AND instanceId=?";
            return super.select(sql,ResourceBooking.class,new Object[]{resourceId,eventId,instanceId},0,-1);
        }catch(Exception e){
            log.error(e.getMessage(),e);
        }
        return null;
    }

    public Collection selectResourceBookings(String resourceId){
        try{
            String sql="SELECT eventId,instanceId,resourceId,startDate,endDate,userId,status,returnedDate FROM rm_booking WHERE" +
                    " resourceId=? ORDER BY startDate";
            return super.select(sql,ResourceBooking.class,new String[]{resourceId},0,-1);
        }catch(Exception e){
            log.error(e.getMessage(),e);
        }
        return null;
    }

    public Collection selectBookingConflicts(ResourceBooking booking){
        try{
            String sql="SELECT eventId,instanceId,resourceId,startDate,endDate,userId,status,returnedDate FROM rm_booking WHERE" +
                    " resourceId='"+booking.getResourceId()+"' AND (" +
                    "(startDate <= ? AND endDate > ?) OR " +
                    "(startDate > ? AND startDate < ?)OR" +
                    "(startDate = ? AND endDate = ?) " +
                    ") ";
            Date from = booking.getStartDate();
            Date to = booking.getEndDate();
            Object arg[] = new Object[]{from,from,from,to,from,to};
            return super.select(sql,ResourceBooking.class,arg,0,-1);
        }catch(Exception e){
            log.error(e.getMessage(),e);
        }
        return null;
    }

    public Collection selectBookingConflicts(String resourceId, Date from, Date to){
        try{
            String sql="SELECT eventId,instanceId,resourceId,startDate,endDate,userId,status,returnedDate FROM rm_booking WHERE" +
                    " resourceId=? AND (" +
                    "(startDate <= ? AND endDate > ?) OR " +
                    "(startDate > ? AND startDate < ?)OR" +
                    "(startDate = ? AND endDate = ?) " +
                    ") ";
            Object arg[] = new Object[]{resourceId,from,from,from,to,from,to};
            return super.select(sql,ResourceBooking.class,arg,0,-1);
        }catch(Exception e){
            log.error(e.getMessage(),e);
        }
        return null;
    }

    public void updateResource(Resource resource){
        try{
            String sql = "UPDATE rm_resource SET name=#name#,description=#description#,status=#status#,bookingId=#bookingId#," +
                    "modifiedBy=#modifiedBy#,requireApproval=#requireApproval#,deleted=#deleted#," +
                    "image=#image#,classification=#classification#,modificationDate=#modificationDate#," +
                    "imageType=#imageType#,approved=#approved#,active=#active#,categoryId=#categoryId# WHERE id=#id#";
            super.update(sql,resource);
        } catch(Exception e){
            log.error(e.getMessage(),e);
        }
    }

    public void removeResourceAccess(Resource resource){
        try{
            String sql = "DELETE from rm_access WHERE resourceId=#id#";
            super.update(sql,resource);
        }catch(Exception e){
            log.error(e.getMessage(),e);
        }
    }

    public void deleteResource(String resourceId){
        try{
            Resource resource = new Resource();
            resource.setId(resourceId);
            String sql = "DELETE FROM rm_resource WHERE id=#id#";
            super.update(sql,resource);
            sql = "DELETE FROM rm_access WHERE id=#id#";
            super.update(sql,resource);
            sql = "DELETE FROM rm_booking WHERE resourceId=#id#";
            super.update(sql,resource);
            sql = "DELETE FROM rm_authority WHERE resourceId=#id#";
            super.update(sql,resource);
        }catch(Exception e){
            log.error(e.getMessage(),e);
        }
    }

    public void insertResourceAccess(String resourceId,String id, int type,String name){
        try{
            String sql="INSERT INTO rm_access(resourceId,id,type,name)" +
                    " VALUES(?,?,?,?)";
            super.update(sql, new Object[] {resourceId, id, new Integer(type), name});
        }catch(Exception e){
            log.error(e.getMessage(),e);
        }
    }

    /**
     * Wraps a String with single (') quotes, and escaping characters when necessary.
     * @param str The String to wrap.
     * @return The wrapped String.
     */
    protected String quote(String str) {
        if (str == null)
            return null;

        StringBuffer buffer = new StringBuffer();
        buffer.append('\'');
        for(int j = 0; j < str.length(); j++) {
            char c = str.charAt(j);
            if(c == '\\' || c == '\'' || c == '"')
                buffer.append('\\');
            buffer.append(c);
        }
        buffer.append('\'');
        return buffer.toString();
    }
}
