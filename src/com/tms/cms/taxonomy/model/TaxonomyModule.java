/*
 * TaxonomyModule.java
 *
 * Created on March 8, 2006, 9:43 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.tms.cms.taxonomy.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import kacang.Application;
import kacang.model.DaoException;
import kacang.model.DefaultModule;
import kacang.services.security.User;
import kacang.util.Log;

import org.apache.commons.collections.SequencedHashMap;

import com.tms.cms.core.model.ContentObject;
import com.tms.cms.core.model.ContentPublisher;

/**
 *
 * @author oilai
 */
public class TaxonomyModule extends DefaultModule {
	
	// Dao action for permission setting
	public Collection getAllRole() {
		TaxonomyDao dao = (TaxonomyDao)getDao();
		try {
			Collection col = dao.getDistinctRole();
			return col;
		}catch(Exception e) {
			Log.getLog(getClass()).error(e.toString());
		}
		return null;
	}
	
	public Collection getAllRoleWithPermission(String permissionId) {
		TaxonomyDao dao = (TaxonomyDao)getDao();
		try {
			Collection col = dao.getDistinctRoleWithPermission(permissionId);
			return col;
		}catch(Exception e) {
			Log.getLog(getClass()).error(e.toString());
		}
		return null;
	}
	
	public void addPermission(String roleId, String permissionId) {
		TaxonomyDao dao = (TaxonomyDao)getDao();
		try {
			dao.insertRolePermission(roleId, permissionId);
		}catch(Exception e) {
			Log.getLog(getClass()).error(e.toString());
		}
	}
	
	public void deletePermission() {
		TaxonomyDao dao = (TaxonomyDao)getDao();
		try {
			dao.deleteRolePermission();
		}
		catch(Exception e) {
			Log.getLog(getClass()).error("error in deletePermission(), "+e.toString());
		}
	}

    //Dao action for node
	
	public Map getNodesWithMapping(String contendId){
		TaxonomyDao dao = (TaxonomyDao)getDao();
		Map nodeMap = new SequencedHashMap();
		try{
			Collection nodeWithMap = dao.getNodesWithMapping(contendId);
			
			if(nodeWithMap != null){
				
				TaxonomyNode tempNode;
				
				for (Iterator iterator = nodeWithMap.iterator(); iterator.hasNext();) {
					tempNode = (TaxonomyNode) iterator.next();
		        	nodeMap.put(tempNode.getTaxonomyId(), tempNode);
		        }
			}
			
			return nodeMap;
		}
		catch(DaoException e){
			Log.getLog(getClass()).error("error in getNodesWithMapping: " + e.getMessage());
		}
		return null;
	}
	
	public Collection formTaxonomyTree(Number shown){
		TaxonomyDao dao = (TaxonomyDao)getDao();
		try{  
			//if (shown==null) {
			//	shown = (Number)new Integer(-1);
			//}
			Collection allNodes = dao.selectAllNodes(shown);
			Collection rootNodes = new ArrayList();
			Collection returnRootNodes = new ArrayList();
			
			Map nodeMap = new SequencedHashMap();
			
			TaxonomyNode tempNode;    
			
			//put all the nodes into map
			for (Iterator iterator = allNodes.iterator(); iterator.hasNext();) {
				tempNode = (TaxonomyNode) iterator.next();
	        	// checking for the parent has the nodes which is shown for public
				Collection col = dao.selectNodesByParent(tempNode.getTaxonomyId(),(shown==null?-1:shown.intValue()));
	        	boolean showParent=false;
	        	if (col!=null && col.size()>0) {
	        		for (Iterator ite=col.iterator();ite.hasNext();) {
	        			TaxonomyNode node = (TaxonomyNode)ite.next();
	        			if (node.getShown()>0) {
	        				showParent=true;
	        			}
	        		}
	        	}
	        	tempNode.setParent(showParent);
	        	
	        	if(tempNode.getParentId().equals("0")){
	        		rootNodes.add(tempNode);
	        	}
	        	nodeMap.put(tempNode.getTaxonomyId(), tempNode);
	        }
			
			TaxonomyNode parentNode = new TaxonomyNode();
			TaxonomyNode tempNode2 = new TaxonomyNode();
			TaxonomyNode tempParentNode = new TaxonomyNode();
			int counter = 0;
			for (Iterator i = rootNodes.iterator(); i.hasNext(); ){
				if(counter == 0){
					
					parentNode = (TaxonomyNode)i.next();
					
					for (Iterator iterator = allNodes.iterator(); iterator.hasNext();) {
						tempNode2 = (TaxonomyNode) iterator.next();
						
			            if(!tempNode2.getParentId().equals("0")){
			            	
			            	tempParentNode = (TaxonomyNode) nodeMap.get(tempNode2.getParentId());
			            	
			            	if(tempParentNode != null){
			            		if (tempParentNode.getTaxonomyId().equals(parentNode.getTaxonomyId())) {
					            	tempParentNode = parentNode;
					            }
					            Collection children = tempParentNode.getChildNodes();
					            if (children == null) {
					                children = new ArrayList();
					                tempParentNode.setChildNodes(children);
					            }
					            
					            children.add(tempNode2);
					            tempParentNode.setChildNodes(children);
			            	}
			            }
			            
			        }
					
					returnRootNodes.add(tempParentNode);
				
				}else{
					break;
				}
				
				counter ++;
			}
						
			return rootNodes;
		}
		catch(DaoException e){
			Log.getLog(getClass()).error("error in formTaxonomyTree: " + e.getMessage());
		}
		return null;
	}
	
	
	public Collection formAdminTaxonomyTree(Number shown){
		TaxonomyDao dao = (TaxonomyDao)getDao();
		try{  
			
			Collection allNodes = dao.selectAllNodes(shown);
			Collection rootNodes = new ArrayList();
			Collection returnRootNodes = new ArrayList();
			
			Map nodeMap = new SequencedHashMap();
			
			TaxonomyNode tempNode;    
			
			//put all the nodes into map
			for (Iterator iterator = allNodes.iterator(); iterator.hasNext();) {
				tempNode = (TaxonomyNode) iterator.next();
	        	// checking for the parent has the nodes which is shown for public
				Collection col = dao.selectNodesByParent(tempNode.getTaxonomyId(),(shown==null?-1:shown.intValue()));
	        	boolean showParent=false;
	        	if (col!=null && col.size()>0) {
	        		for (Iterator ite=col.iterator();ite.hasNext();) {
	        			TaxonomyNode node = (TaxonomyNode)ite.next();
	        			//if (node.getShown()>0) {
	        				showParent=true;
	        			//}
	        		}
	        	}
	        	tempNode.setParent(showParent);
	        	
	        	if(tempNode.getParentId().equals("0")){
	        		rootNodes.add(tempNode);
	        	}
	        	nodeMap.put(tempNode.getTaxonomyId(), tempNode);
	        }
			
			TaxonomyNode parentNode = new TaxonomyNode();
			TaxonomyNode tempNode2 = new TaxonomyNode();
			TaxonomyNode tempParentNode = new TaxonomyNode();
			int counter = 0;
			for (Iterator i = rootNodes.iterator(); i.hasNext(); ){
				if(counter == 0){
					
					parentNode = (TaxonomyNode)i.next();
					
					for (Iterator iterator = allNodes.iterator(); iterator.hasNext();) {
						tempNode2 = (TaxonomyNode) iterator.next();
						
			            if(!tempNode2.getParentId().equals("0")){
			            	
			            	tempParentNode = (TaxonomyNode) nodeMap.get(tempNode2.getParentId());
			            	
			            	if(tempParentNode != null){
			            		if (tempParentNode.getTaxonomyId().equals(parentNode.getTaxonomyId())) {
					            	tempParentNode = parentNode;
					            }
					            Collection children = tempParentNode.getChildNodes();
					            if (children == null) {
					                children = new ArrayList();
					                tempParentNode.setChildNodes(children);
					            }
					            
					            children.add(tempNode2);
					            tempParentNode.setChildNodes(children);
			            	}
			            }
			            
			        }
					
					returnRootNodes.add(tempParentNode);
				
				}else{
					break;
				}
				
				counter ++;
			}
						
			return rootNodes;
		}
		catch(DaoException e){
			Log.getLog(getClass()).error("error in formAdminTaxonomyTree: " + e.getMessage());
		}
		return null;
	}
	

    public void addNode(TaxonomyNode node) {
        TaxonomyDao dao = (TaxonomyDao)getDao();
        try {
            dao.insertNode(node);
        }
        catch(Exception e) {
            Log.getLog(getClass()).error(e.toString());
        }
    }

    public void updateNode(TaxonomyNode node) {
        TaxonomyDao dao = (TaxonomyDao)getDao();
        try {
            dao.updateNode(node);
        }
        catch(Exception e) {
            Log.getLog(getClass()).error(e.toString());
        }
    }

    public void deleteNode(TaxonomyNode node) {
        TaxonomyDao dao = (TaxonomyDao)getDao();
        try {
            dao.deleteNode(node);
        }
        catch(Exception e) {
            Log.getLog(getClass()).error(e.toString());
        }
    }
    
    public void deleteNodeRecursive(TaxonomyNode node) {
        TaxonomyDao dao = (TaxonomyDao)getDao();
        try {
        	if (node.getChildNodes().size()>0) {
        		for (Iterator i=node.getChildNodes().iterator();i.hasNext();) {
        			TaxonomyNode child = (TaxonomyNode)i.next();
        			child.setChildNodes(getNodesByParent(child.getTaxonomyId(),-1));
        			deleteNodeRecursive(child);
        		}
        	}
            dao.deleteNode(node);
        }
        catch(Exception e) {
            Log.getLog(getClass()).error(e.toString());
        }
    }

    public void deleteNodeByParent(TaxonomyNode parentNode) {
        TaxonomyDao dao = (TaxonomyDao)getDao();
        try {
            dao.deleteNodeByParent(parentNode);
        }
        catch(Exception e) {
            Log.getLog(getClass()).error(e.toString());
        }
    }
    
    public TaxonomyNode getNode(String taxonomyId) {
    	TaxonomyDao dao = (TaxonomyDao)getDao();
    	try {
    		return dao.selectNode(taxonomyId);
    	}
    	catch(Exception e) {
    		Log.getLog(getClass()).error(e.toString());
    	}
    	return null;
    }

    public Collection getNodesByParent(String parentId, int shown) {
        TaxonomyDao dao = (TaxonomyDao)getDao();
        try {
            return dao.selectNodesByParent(parentId,shown);
        }
        catch(Exception e) {
            Log.getLog(getClass()).error(e.toString());
        }

        return null;
    }

    public TaxonomyNode[] getNodesByName(String name, String id, int shown) {
        TaxonomyDao dao = (TaxonomyDao)getDao();
        TaxonomyNode[] list={};
        try {
            Collection col = dao.selectNodesByName(name,id,shown);
            if (col!=null && col.size()>0) {
                list = new TaxonomyNode[col.size()];
                int iCount=0;
                for (Iterator i=col.iterator();i.hasNext();) {
                    list[iCount] = (TaxonomyNode)i.next();
                    iCount++;
                }
            }
        }
        catch(Exception e) {
            Log.getLog(getClass()).error(e.toString());
        }
        return list;
    }

    public Collection getRelatedNodes(String name,int shown) {
        TaxonomyDao dao = (TaxonomyDao)getDao();
        Collection col=null;
        try {
            col = dao.selectRelatedNodes(name,shown);
        }
        catch(DaoException e) {
            Log.getLog(getClass()).error(e.toString(),e);
        }
        return col;
    }

    public int getNodesTotal(int shown) {
        int iTotal=0;
        TaxonomyDao dao = (TaxonomyDao)getDao();
        try {
            Collection col = dao.selectNodeTotal(shown);
            if (col!=null && col.size()>0) {
                HashMap map = (HashMap)col.iterator().next();
                iTotal = ((Number)map.get("total")).intValue();
            }

        }   catch(Exception e) {
            Log.getLog(getClass()).error(e.toString());
        }
        return iTotal;
    }

    public TaxonomyNode getTaxonomyTree(String id,int shown) {
        TaxonomyNode parentNode=getNode(id);

        Collection col = getNodesByParent(id,shown);
        Collection childCol = new ArrayList();
        TaxonomyNode[] childNodes = null;
        if (col!=null && col.size()>0) {
            childNodes = new TaxonomyNode[col.size()];
            int iCounter=0;
            for (Iterator i=col.iterator();i.hasNext();) {
                TaxonomyNode node = (TaxonomyNode)i.next();
                childNodes[iCounter]= getTaxonomyTree(node.getTaxonomyId(),shown);
                childCol.add(childNodes[iCounter]);
                iCounter++;
            }
        }
        parentNode.setChildNodes(childCol);
        return parentNode;
    }

    // dao action for mapping
    public void addMapping(TaxonomyMap map)  {
        TaxonomyDao dao = (TaxonomyDao)getDao();
        try {
            if (!isMapped(map.getTaxonomyId(),map.getContentId()))
                dao.insertMapping(map);
        }
        catch(Exception e) {
            Log.getLog(getClass()).error(e.toString());
        }
    }

    public void deleteMapping(TaxonomyMap map) {
        TaxonomyDao dao = (TaxonomyDao)getDao();
        try {
            dao.deleteMapping(map);
        }
        catch(Exception e) {
            Log.getLog(getClass()).error(e.toString());
        }
    }

    public TaxonomyMap[] getMappingByNode(String nodeId, User user) {
        TaxonomyDao dao = (TaxonomyDao)getDao();
        TaxonomyMap[] map=null;
        try {
            Collection col = dao.selectMappingByNode(nodeId);
            if (col!=null && col.size()>0) {
                map = new TaxonomyMap[col.size()];
                int iCounter=0;
                //ContentModule contentMod = (ContentModule)Application.getInstance().getModule(ContentModule.class);
                ContentPublisher publisher = (ContentPublisher)Application.getInstance().getModule(ContentPublisher.class);

                for (Iterator i=col.iterator(); i.hasNext();) {
                    map[iCounter] = (TaxonomyMap)i.next();
                    ContentObject obj = publisher.view(map[iCounter].getContentId(),user);
                    map[iCounter].setContentObject(obj);
                    iCounter++;
                }
            }

        }
        catch(Exception e) {
            Log.getLog(getClass()).error(e.toString());
        }
        return map;
    }

    public TaxonomyMap[] getMappingByContentId(String contentId, User user) {
        TaxonomyDao dao = (TaxonomyDao)getDao();
        TaxonomyMap[] map=null;
        try {
            Collection col = dao.selectMappingByContentId(contentId,false);
            if (col!=null && col.size()>0) {
                map = new TaxonomyMap[col.size()];
                int iCounter=0;
                //ContentModule contentMod = (ContentModule)Application.getInstance().getModule(ContentModule.class);
                //ContentPublisher publisher = (ContentPublisher)Application.getInstance().getModule(ContentPublisher.class);

                for (Iterator i=col.iterator(); i.hasNext();) {
                    map[iCounter] = (TaxonomyMap)i.next();
                    //ContentObject obj = publisher.view(map[iCounter].getContentId(),user);
                    //map[iCounter].setContentObject(obj);
                    iCounter++;
                }
            }
        }
        catch(Exception e) {
            Log.getLog(getClass()).error(e.toString());
        }
        return map;
    }

    public boolean isMapped(String taxonomyId, String contentId) {
        boolean map = false;
        TaxonomyDao dao = (TaxonomyDao)getDao();
        try {
            map = dao.isMapped(taxonomyId,contentId);
        }
        catch(Exception e) {
            Log.getLog(getClass()).error(e.toString());
        }
        return map;
    }

    public TaxonomyMap[] getMappingByContentIdForPublic(String contentId, User user) {
        TaxonomyDao dao = (TaxonomyDao)getDao();
        TaxonomyMap[] map=null;
        try {
            Collection col = dao.selectMappingByContentId(contentId,true);
            if (col!=null && col.size()>0) {
                map = new TaxonomyMap[col.size()];
                int iCounter=0;
                //ContentModule contentMod = (ContentModule)Application.getInstance().getModule(ContentModule.class);
                //ContentPublisher publisher = (ContentPublisher)Application.getInstance().getModule(ContentPublisher.class);

                for (Iterator i=col.iterator(); i.hasNext();) {
                    map[iCounter] = (TaxonomyMap)i.next();
                    //ContentObject obj = publisher.view(map[iCounter].getContentId(),user);
                    //map[iCounter].setContentObject(obj);
                    iCounter++;
                }
            }
        }
        catch(Exception e) {
            Log.getLog(getClass()).error(e.toString());
        }
        return map;
    }
    
    public int getMappingTotalForPublic(ArrayList iTaxonomyId, int shown) {
        TaxonomyDao dao = (TaxonomyDao)getDao();
        try {
            return dao.selectMappingByNodeCountForPublic(iTaxonomyId,shown);
        }
        catch(Exception e) {
            Log.getLog(getClass()).error("error in get mapping total: ",e);
        }
        return 0;
    }
    
    public int getMappingTotalForPublic(String taxonomyId, Number shown) {
        TaxonomyDao dao = (TaxonomyDao)getDao();
        try {
            return dao.selectMappingByNodeCountForPublic(taxonomyId,shown);
        }
        catch(Exception e) {
            Log.getLog(getClass()).error("error in get mapping total: ",e);
        }
        return 0;
    }
    
    public TaxonomyMap[] getMappingForPublic(ArrayList iTaxonomyId, User user, int shown, int start, int maxRow) {
        TaxonomyDao dao = (TaxonomyDao)getDao();
        TaxonomyMap[] map = null;
        try {
            Collection col = dao.selectMappingByNodeForPublic(iTaxonomyId,shown, start,maxRow);
            
            if (col!=null && col.size()>0) {
                map = new TaxonomyMap[col.size()];
                int iCounter=0;

                ContentPublisher publisher = (ContentPublisher)Application.getInstance().getModule(ContentPublisher.class);

                for (Iterator i=col.iterator();i.hasNext();) {
                    map[iCounter] = (TaxonomyMap)i.next();
                    ContentObject obj = publisher.view(map[iCounter].getContentId(),user);
                    map[iCounter].setContentObject(obj);
                    iCounter++;
                }
            }
        }
        catch(Exception e) {
            Log.getLog(getClass()).error(e.toString());
        }

        return map;
    }

    public TaxonomyMap[] getMappingForPublic(String iTaxonomyId, User user, int shown, int start, int maxRow) {
        TaxonomyDao dao = (TaxonomyDao)getDao();
        TaxonomyMap[] map = null;
        try {
            Collection col = dao.selectMappingByNodeForPublic(iTaxonomyId,shown, start,maxRow);
            if (col!=null && col.size()>0) {
                map = new TaxonomyMap[col.size()];
                int iCounter=0;

                ContentPublisher publisher = (ContentPublisher)Application.getInstance().getModule(ContentPublisher.class);

                for (Iterator i=col.iterator();i.hasNext();) {
                    map[iCounter] = (TaxonomyMap)i.next();
                    ContentObject obj = publisher.view(map[iCounter].getContentId(),user);
                    map[iCounter].setContentObject(obj);
                    iCounter++;
                }
            }
        }
        catch(Exception e) {
            Log.getLog(getClass()).error(e.toString());
        }

        return map;
    }

    public Collection getMappingByNodeListForPublic(String[] ids, User user, int shown) {
        TaxonomyDao dao = (TaxonomyDao)getDao();
        Collection col=null;
        try {
            col = dao.selectMappingByNodeListForPublic(ids,shown);
        }
        catch(Exception e) {
            Log.getLog(getClass()).error(e.toString());
        }
        return col;
    }

    public Collection selectDistinctMappedId() {
        TaxonomyDao dao = (TaxonomyDao)getDao();
        try {
            return dao.selectDistinctIdFromMapping();
        }
        catch(Exception e) {
            Log.getLog(getClass()).error("error in select distinct mapped id:",e);
        }
        return null;
    }

    public void deleteMappingByNode(String taxonomyId) {
        TaxonomyDao dao = (TaxonomyDao)getDao();
        try {
            dao.deleteMappingByNode(taxonomyId);
        }
        catch(Exception e) {
            Log.getLog(getClass()).error("Error in delete mapping by taxonomyId: ",e);
        }
    }
    
    

}
