package com.tms.collab.rss.model;

import kacang.Application;
import kacang.model.DaoException;
import kacang.model.DefaultModule;
import kacang.model.Module;
import kacang.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import kacang.util.UuidGenerator;

public class RssHandler extends DefaultModule{
	
	Log log =Log.getLog(getClass());
	
	
	public Collection getModules()
    {
		Collection modules = new ArrayList();  
		Map map = Application.getInstance().getModuleMap();
            for (Iterator i = map.keySet().iterator(); i.hasNext();)
            {
                Object module = (Object) i.next();
                modules.add(module);
                //System.out.println("modules"+module);
            }
        return modules;
    }
    
	public void addChannel(Channel channel) {
		RssDao dao = (RssDao) getDao();
		try {
			dao.insertChannel(channel);
		} catch (DaoException e) {
			log.error("Error adding channel " + e.toString(), e);
		}
	}
	
	public void updateChannel(Channel channel) {
		RssDao dao = (RssDao) getDao();
		try {
			dao.updateChannel(channel);
		} catch (DaoException e) {
			log.error("Error adding channel " + e.toString(), e);
		}
	}	
	
	public void deleteChannel(String channelId) {
		RssDao dao = (RssDao) getDao();
		try {
			dao.deleteChannel(channelId);
		} catch (DaoException e) {
			log.error("Error delete channel " + e.toString(), e);
		}
	}

	public Collection getAllActiveChannels() {
		Collection allChannels = new ArrayList();
		RssDao dao = (RssDao) getDao();
		try {
			allChannels = dao.loadActiveChannels();
		} catch (DaoException e) {
			log.error("Error Displaying channel " + e.toString(), e);
		}
		return allChannels;
	}
	
	public Collection getAllSearchChannels(String searchBy, String sort, boolean desc, boolean active ,int start, int rows) {
		Collection allSearchChannels = new ArrayList();
		RssDao dao = (RssDao) getDao();
		try {
			allSearchChannels = dao.selectChannelBySearch(searchBy, sort, desc, active ,start, rows);
		} catch (DaoException e) {
			log.error("Error Displaying Search channel " + e.toString(), e);
		}
		return allSearchChannels;
	}	
	
	public Channel getOneChannel(String channelId) {
		Channel allChannels = null;
		RssDao dao = (RssDao) getDao();
		try {
			allChannels = dao.loadOneChannels(channelId);
		} catch (DaoException e) {
			log.error("Error Displaying channel " + e.toString(), e);
		}
		return allChannels;
	}	
	
	public Item getOneItem(String itemId) {
		Item oneItem = null;
		RssDao dao = (RssDao) getDao();
		try {
			oneItem = dao.loadOneItem(itemId);
		} catch (DaoException e) {
			log.error("Error Displaying item " + e.toString(), e);
		}
		return oneItem;
	}
	
	public void addItem(Item item){
		RssDao dao = (RssDao) getDao(); 
		try {
			dao.insertItem(item);
		} catch (DaoException e) {
			log.error("Error adding item " + e.toString(), e);
		}	
	}
	
	public void addCollItem(Collection item){
		RssDao dao = (RssDao) getDao(); 
		try {
			dao.insertCollItem(item);
		} catch (DaoException e) {
			log.error("Error insertCollItem: " + e.toString(), e);
		}	
	}
	
	
	
	
	public void deleteItem(String itemId, String channelId){
		RssDao dao = (RssDao) getDao(); 
		try {
			dao.deleteItem(itemId, channelId);
		} catch (DaoException e) {
			log.error("Error Deleting item " + e.toString(), e);
		}	
	}
	
	
	public void deleteItemByChannel(String channelId){
		RssDao dao = (RssDao) getDao(); 
		try {
			dao.deleteItemByChannel(channelId);
		} catch (DaoException e) {
			log.error("Error Deleting item by Channel" + e.toString(), e);
		}	
	}		

	
    public int getChannelCount(String ChannelId) {
	    int count = 0;
	    RssDao dao = (RssDao)getDao();
	    try
	    {
	        count = dao.selectChannelListCount(ChannelId);
	    }
	    catch(Exception e)
	    {
	    	e.toString();
	    }
	    return count;
    }	
    
    
    public boolean getCountFoundItemKey(String channelId, String itemId) {
	    boolean isFound = false;
	    RssDao dao = (RssDao)getDao();
	    try
	    {
	    	isFound = dao.countFoundItemKey(channelId, itemId);
	    }
	    catch(Exception e)
	    {
	    	e.toString();
	    }
	    return isFound;
    }	  
    
    
	public Collection getAllItemsByChannelId(String channelId) {
		Collection allItems = new ArrayList();
		RssDao dao = (RssDao) getDao();
		try {
			allItems = dao.loadItems(channelId);
		} catch (DaoException e) {
			log.error("Error Displaying items " + e.toString(), e);
		}
		return allItems;
	}    
    
    
    
    
    
    /*	
    public void updateItem(Item item, String auto, String rssItemId[], boolean isCheck){
	    try
	    {
	    	int i = 0;
	    	RssDao dao = (RssDao)getDao();
	    	if (auto.equals("0")){
	    		if (isCheck == true){
		    		if (item.getRssItemId().equals(rssItemId)){
		    			item.setItemId(item.getItemId());
				    	item.setChannelId(item.getChannelId());
		    			dao.updateItem(item);	
		    		} else {
		    			for(i=0 ; i<rssItemId.length; i++) {
			    			item.setItemId(item.getItemId());
					    	item.setChannelId(item.getChannelId());
					    	item.setRssItemId(rssItemId[i]);
					    	dao.insertItem(item);
		    			}
		    		}
	    		} 
	    	} else {
	    		
	    		
	    		dao.insertItem(item);
	    		
	    		
	    		
	    	}
	    }
	    catch(DaoException e)
	    {
	    	log.error("Error adding channel " + e.toString(), e);
	    }
	}
*/
	

	
}
