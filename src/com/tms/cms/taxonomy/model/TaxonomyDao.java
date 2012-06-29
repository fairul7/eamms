/*
 * TaxonomyDao.java
 *
 * Created on March 8, 2006, 9:42 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.tms.cms.taxonomy.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import kacang.model.DaoException;
import kacang.model.DataSourceDao;
import kacang.util.Log;

/**
 * 
 */
public class TaxonomyDao extends DataSourceDao {

    public void init() throws DaoException {
                         
        try {
            // create taxonomy node table
            super.update("CREATE TABLE txy_node (" +
                    "taxonomyId VARCHAR(35)," +
                    "taxonomyName VARCHAR(255)," + 
                    "parentId VARCHAR(35)," +
                    "parent VARCHAR(1)," +
                    "description VARCHAR(255)," +
                    "shown int(1)," +
                    "nodeSynonym VARCHAR(255)," +
                    "nodeOrder int(2))",null);

            // create taxonomy mapping table
            super.update("CREATE TABLE txy_mapping (" +
                    "taxonomyId VARCHAR(35)," +
                    "contentId VARCHAR(255)," +
                    "contentType VARCHAR(35)," +
                    "mapBy VARCHAR(255)," +
                    "mapDate DATETIME )",null);
        }
        catch(Exception e) {
        	Log.getLog(getClass()).error("----------------------"+e);
        }
        
        /*
        try {
        	Collection col = super.select("SELECT COUNT(*) AS total FROM cms_content_role_permission WHERE role=? AND permission=?",HashMap.class,new Object[]{"manager","Taxonomy"},0,-1);
        	boolean hasUpdate=false;
        	if (col!=null && col.size()>0) {
        		HashMap map = (HashMap)col.iterator().next();
        		Number i = (Number)map.get("total");
        		if (i.intValue()>0) {
        			hasUpdate=true;
        		}
        	}
        	if (!hasUpdate)
        		super.update("INSERT INTO cms_content_role_permission (role,permission) VALUES (?,?) ",
        			new Object[]{"manager","Taxonomy"});
        	
        }
        catch(Exception e) {
        	
        }
        */

    }
    
    
    // start - set role permission for taxonomy
    
    public Collection getDistinctRole() throws DaoException {
    	String sql = "SELECT DISTINCT(role) AS roleId from cms_content_role_permission where role not like 'drm%'";
    	return super.select(sql,HashMap.class,null,0,-1);
    }
    
    public Collection getDistinctRoleWithPermission(String permissionId) throws DaoException {
    	String sql = "SELECT DISTINCT(role) AS roleId FROM cms_content_role_permission " +
    			"WHERE role not like 'drm%' AND permissionId='"+permissionId+"'";
    	return super.select(sql, HashMap.class, null, 0, -1);
    }
    
    public void insertRolePermission(String roleId, String permissionId) throws DaoException {
    	String sqlCount = "SELECT COUNT(*) AS total FROM cms_content_role_permission " +
    			"WHERE role='"+roleId+"' AND permissionId='"+permissionId+"'";
    	Collection col = super.select(sqlCount, HashMap.class, null, 0, -1);
    	if (col!=null && col.size()>0) {
    		HashMap map = (HashMap)col.iterator().next();
    		Number total= (Number)map.get("total");
    		if (total.intValue()<=0) {
    	    	String sql = "INSERT INTO cms_content_role_permission " +
    						"(role,permissionid) VALUES ('"+roleId+"','"+permissionId+"')";
    	    	super.update(sql,null);
    		}
    	}
    }
    
    public void deleteRolePermission() throws DaoException {
    	String sql = "DELETE FROM cms_content_role_permission WHERE permissionId='Taxonomy' " +
    			"";
    	super.update(sql, null);
    }
    
    // end - set role permission for taxonomy

    //Dao action for node
    
    public Collection getNodesWithMapping(String contentId) throws DaoException{
    	String sql = "SELECT DISTINCT taxonomyId FROM txy_mapping WHERE contentId = ?";
    	
    	return super.select(sql, TaxonomyNode.class, new Object[]{contentId}, 0, -1);
    }
    
    public Collection selectAllNodes(Number shown) throws DaoException {
    	String sShown = (shown==null)?"":"AND shown="+shown.intValue();
    	
    	String sql = "SELECT taxonomyId,taxonomyName,parentId,parent,shown,nodeSynonym,nodeOrder,description FROM txy_node " +
        	"WHERE 1=1 " + sShown + " ORDER BY taxonomyName ";
    	
    	return super.select(sql,TaxonomyNode.class,null,0,-1);
    }

    public void insertNode(TaxonomyNode node) throws DaoException {
        super.update("INSERT INTO txy_node " +
                "(taxonomyId,taxonomyName,parentId,parent,shown,nodeSynonym,nodeOrder,description) VALUES " +
                "(#taxonomyId#,#taxonomyName#,#parentId#,#parent#,#shown#,#nodeSynonym#,#nodeOrder#,#description#)",node);
    }

    public void updateNode(TaxonomyNode node) throws DaoException {
        super.update("UPDATE txy_node SET " +
                "taxonomyName=#taxonomyName#,parentId=#parentId#,parent=#parent#," +
                "shown=#shown#,nodeSynonym=#nodeSynonym#,nodeOrder=#nodeOrder#,description=#description# " +
                "WHERE taxonomyId=#taxonomyId#",node);
    }

    public void deleteNode(TaxonomyNode node) throws DaoException {
        super.update("DELETE FROM txy_node WHERE taxonomyId=#taxonomyId#",node);
    }

    public void deleteNodeByParent(TaxonomyNode parentNode) throws DaoException {
        super.update("DELETE FROM txy_node WHERE parentId=#taxonomyId#",parentNode);
    }

    public TaxonomyNode selectNode(String taxonomyId) throws DaoException {
        String sql = "SELECT taxonomyId,taxonomyName,parentId,parent,shown,nodeSynonym,nodeOrder,description " +
                "FROM txy_node " +
                "WHERE taxonomyId=?";
        Object[] obj = new Object[] {taxonomyId};
        Collection col = super.select(sql,TaxonomyNode.class,obj,0,-1);
        TaxonomyNode node=null;
        if (col!=null && col.size()>0) {
            node = (TaxonomyNode)col.iterator().next();
        }
        return node;
    }

    public Collection selectNodesByParent(String parentId, int shown) throws DaoException {
        String sShown = (shown<0)?"":"AND shown="+shown;

        String sql = "SELECT taxonomyId,taxonomyName,parentId,parent,shown,nodeSynonym,nodeOrder,description FROM txy_node " +
                "WHERE parentId=? "+sShown+" ORDER BY taxonomyName ";
        Object[] obj = new Object[]{parentId};
        Collection col = super.select(sql,TaxonomyNode.class,obj,0,-1);
        return col;
    }

    public Collection selectNodesByName(String name, String id, int shown) throws DaoException {
        String sShown = (shown<0)?"":"AND shown="+shown;

        String sql = "SELECT taxonomyId,taxonomyName,parentId,parent,shown,nodeSynonym,nodeOrder,description FROM txy_node " +
                "WHERE upper(taxonomyName)=? AND taxonomyId!=? "+sShown+" ORDER BY taxonomyName";
        return super.select(sql,TaxonomyNode.class,new Object[]{name.toUpperCase(),id},0,-1);
    }

    public Collection selectRelatedNodes(String name, int shown) throws DaoException {
        String sShown = (shown<0)?"":"AND shown="+shown;

        String sql = "SELECT DISTINCT taxonomyId,taxonomyName,parentId,parent,shown,nodeSynonym,nodeOrder,description " +
                "FROM txy_node " +
                "WHERE (upper(taxonomyName) like ? OR upper(nodeSynonym) LIKE '%."+name.toUpperCase()+".%') "+sShown;
        return super.select(sql,TaxonomyNode.class,new Object[]{ name.toUpperCase() },0,-1);
    }

    public Collection selectNodeTotal(int shown) throws DaoException {
        String sShown = (shown<0)?"":"WHERE shown="+shown;

        String sql = "SELECT COUNT(taxonomyId) AS total FROM txy_node "+sShown;
        return super.select(sql,HashMap.class,null,0,-1);
    }

    // dao action for mapping
    public void insertMapping(TaxonomyMap map) throws DaoException {
        super.update("INSERT INTO txy_mapping (taxonomyId, contentId, contentType, mapBy, mapDate) " +
                "VALUES (#taxonomyId#,#contentId#,#contentType#,#mapBy#,#mapDate#)",map);
    }

    public void deleteMapping(TaxonomyMap map) throws DaoException {
        super.update("DELETE FROM txy_mapping WHERE taxonomyId=#taxonomyId# AND " +
                "contentId=#contentId#",map);
    }

    public void deleteMappingByNode(String id) throws DaoException  {
        super.update("DELETE FROM txy_mapping WHERE taxonomyId=?",new Object[]{id});
    }

    public Collection selectMappingByNode(String nodeId) throws DaoException {
        String sql = "SELECT taxonomyId,contentId,contentType,mapBy,mapDate FROM " +
                "txy_mapping WHERE taxonomyId=? ORDER BY mapDate DESC";
        Object[] obj = new Object[] {nodeId};
        Collection col = super.select(sql,TaxonomyMap.class,obj,0,-1);
        return col;
    }

    public Collection selectMappingByContentId(String contentId, boolean shown) throws DaoException {
        String s = (shown)?" AND t.shown=1":"";

        String sql = "SELECT m.taxonomyId,m.contentId,m.contentType,m.mapBy,m.mapDate FROM " +
                "txy_mapping m INNER JOIN txy_node t ON " +
                "m.taxonomyId=t.taxonomyId WHERE m.contentId=? "+s+" ORDER BY m.mapDate DESC";
        Collection col = super.select(sql,TaxonomyMap.class,new String[]{contentId},0,-1);
        return col;
    }

    public boolean isMapped(String taxonomyId, String contentId) throws DaoException {
        String sql = "SELECT count(taxonomyId) AS total FROM txy_mapping WHERE " +
                "taxonomyId=? AND contentId=?";
        Object[] obj = new Object[]{taxonomyId,contentId};
        Collection col = super.select(sql,HashMap.class,obj,0,-1);
        boolean isMap=false;
        if (col!=null && col.size()>0) {
            HashMap map = (HashMap)col.iterator().next();
            String total = map.get("total").toString();
            try {
                int iTotal = Integer.parseInt(total);
                if (iTotal>0) {
                    isMap=true;
                }
            }
            catch(Exception e) {
                isMap=false;
            }
        }
        return isMap;
    }
    
    public Collection selectMappingByNodeForPublic(ArrayList iId, int shown, int start, int maxRow) throws DaoException {
        String sShown = (shown<0)?"":"AND n.shown="+shown;
        
        StringBuffer inClause = new StringBuffer("");
        Collection args = new ArrayList();
        
        if(iId != null && iId.size() > 0){
	        for (int i=0; i<iId.size(); i++) {
	            String id = (String)iId.get(i);
	        	
	        	if (i > 0)
	            	inClause.append(",");
	            inClause.append("?");
	            args.add(id);
	        }
	          
	        String sql = "SELECT DISTINCT t.contentId,c.className FROM " +
	                "txy_mapping t INNER JOIN cms_content_published c ON t.contentId=c.id " +
	                "INNER JOIN txy_node n ON n.taxonomyId=t.taxonomyId " +
                    "INNER JOIN cms_content_status s ON c.id=s.id " +
                    "WHERE " +
	                "t.taxonomyId IN ( " + inClause.toString() + " ) AND c.className IS NOT NULL "+sShown+
                    "";//" ORDER BY s.date DESC";
	        
	        return super.select(sql,TaxonomyMap.class,args.toArray(),start,maxRow);
        }else{
        	return null;
        }
    } 
    
    public Collection selectMappingByNodeForPublic(String iId, int shown, int start, int maxRow) throws DaoException {
        String sShown = (shown<0)?"":"AND n.shown="+shown;

        String sql = "SELECT t.taxonomyId,t.contentId,t.contentType,t.mapBy,t.mapDate FROM " +
                "txy_mapping t INNER JOIN cms_content_published c ON t.contentId=c.id " +
                "INNER JOIN txy_node n ON n.taxonomyId=t.taxonomyId " +
                "INNER JOIN cms_content_status s ON c.id=s.id " +
                "WHERE t.taxonomyId=? AND c.className IS NOT NULL "+sShown + " ";
          
        return super.select(sql,TaxonomyMap.class,new Object[]{iId},start,maxRow);
    }
    
    public int selectMappingByNodeCountForPublic(ArrayList iId, int shown) throws DaoException {
        String sShown = (shown<0)?"":"AND n.shown="+shown;
        
        StringBuffer inClause = new StringBuffer("");
        Collection args = new ArrayList();
        
        if(iId != null && iId.size() > 0){
	        for (int i=0; i<iId.size(); i++) {
	            String id = (String)iId.get(i);
	        	
	        	if (i > 0)
	            	inClause.append(",");
	            inClause.append("?");
	            args.add(id);
	        }
	        
	        String sql = "SELECT COUNT(DISTINCT t.contentId) AS total FROM " +
	        	"txy_mapping t INNER JOIN cms_content_published c ON t.contentId=c.id " +
	        	"INNER JOIN txy_node n ON n.taxonomyId=t.taxonomyId WHERE " +
	        	"t.taxonomyId IN ( " + inClause.toString() + " ) AND c.className IS NOT NULL "+sShown;
	        
	        Collection col = super.select(sql,HashMap.class,args.toArray(),0,-1);
	        int iTotal = 0;
	        if (col!=null && col.size()>0) {
	            HashMap map = (HashMap)col.iterator().next();
	            Number s = (Number)map.get("total");
	            iTotal = s.intValue();
	        }
	        return iTotal;
        }else{
        	return 0;
        }
        
    }
    
    public int selectMappingByNodeCountForPublic(String id, Number shown) throws DaoException {
        String sShown = (shown==null)?"":"AND n.shown="+shown.intValue();

        String sql = "SELECT COUNT(t.taxonomyId) AS total FROM " +
                "txy_mapping t INNER JOIN cms_content_published c ON t.contentId=c.id " +
                "INNER JOIN txy_node n ON n.taxonomyId=t.taxonomyId WHERE " +
                "t.taxonomyId=? AND c.className IS NOT NULL "+sShown;
        //Number n = new Integer(iId);
        Collection col = super.select(sql,HashMap.class,new Object[]{id},0,-1);
        int iTotal = 0;
        if (col!=null && col.size()>0) {
            HashMap map = (HashMap)col.iterator().next();
            Number s = (Number)map.get("total");
            iTotal = s.intValue();
        }
        return iTotal;
    }

    // works for oracle.
    public Collection selectMappingByNodeListForPublic(String[] ids,int shown) throws DaoException {
        String sShown = (shown<0)?"":" AND n.shown="+shown;
        
        // get list of contentId
        String sql = "";
        String sWhere = "";
        
        if (ids!=null && ids.length>0) {
            sWhere+= "AND n.taxonomy IN ( ";
            for (int i=0;i<ids.length;i++) {
                if (i==0)
                    sWhere += " '"+ids[i]+"' ";
                else
                    sWhere += " , '"+ids[i]+ "' ";
            }
            sWhere += ")";
        }
        
        sql = "SELECT distinct contentId FROM " +
                "txy_mapping t INNER JOIN txy_node n ON t.taxonomyId=n.taxonomyId " +
                " WHERE 1=1 "+(sWhere==null || sWhere.equals("")?"":sWhere) + sShown;
        Collection col = super.select(sql,HashMap.class,null,0,-1);

        //sort content by latest
        if (col!=null && col.size()>0) {
            sql = "SELECT id FROM cms_content_published WHERE ";
            int iCounter=0;
            for (Iterator i=col.iterator();i.hasNext();) {
                HashMap map = (HashMap)i.next();
                if (iCounter==0)
                    sql += " id='"+(String)map.get("contentId")+"' ";
                else
                    sql += "OR id='"+(String)map.get("contentId")+"' ";
                iCounter++;
            }
            sql += "ORDER BY date DESC";
            col = super.select(sql,HashMap.class,null,0,10);
        }

        return col;
    }
    

    public Collection selectDistinctIdFromMapping() throws DaoException {
        String sql = "SELECT DISTINCT(taxonomyId) FROM txy_mapping";
        Collection col = super.select(sql,HashMap.class,null,0,-1);
        return col;
    }
    
}
