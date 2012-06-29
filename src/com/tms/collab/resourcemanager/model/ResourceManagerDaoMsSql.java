package com.tms.collab.resourcemanager.model;

import java.util.Collection;

import kacang.model.DaoException;
import kacang.util.Log;

public class ResourceManagerDaoMsSql extends ResourceManagerDao
{
    Log log = Log.getLog(getClass());
    
    public Collection selectResources(String filter,String catFilter,String[] userIds,String[] groupIds,boolean onlyApproved,boolean includeDeleted,boolean includePrivate,String sort, boolean desc, int startIndex,int maxRows) throws DaoException
    {
            String strSort= "";
            if (sort != null) {
            	
            	if(sort.equals("name"))
            		sort = "r.name";
            	
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
                    + (includeDeleted?"":" AND deleted='0'") + strSort;
            return super.select(sql,Resource.class,null,startIndex,maxRows);
    }

}
