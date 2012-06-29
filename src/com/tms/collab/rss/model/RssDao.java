package com.tms.collab.rss.model;

import java.util.*;

import kacang.model.*;

public class RssDao extends DataSourceDao{
	public void init() throws DaoException{
		// create channel
		super.update("CREATE TABLE rss_channel " +
					"(channelId VARCHAR(255) NOT NULL, " +
					"title VARCHAR(255) NOT NULL, " +
					"description VARCHAR(300), " +
					"link VARCHAR(255), " +
					"moduleId VARCHAR(255) NOT NULL, " +
					"categoryId VARCHAR(255) NOT NULL, " +
					"active CHAR(1) NOT NULL, " +
					"autoCount CHAR(1) NOT NULL, " +
					"autoCountFor integer(10), " +
					"createBy VARCHAR(255), " +
					"createDate datetime, " +
					"updateBy VARCHAR(255), " +
					"updateDate datetime, " +
					"PRIMARY KEY(channelId))", null);

		// create item
		super.update("CREATE TABLE rss_item " +
				"(itemId VARCHAR(255) NOT NULL, " +
				"channelId VARCHAR(255) NOT NULL, " +
				"rssItemId VARCHAR(255) NOT NULL, " +
				"PRIMARY KEY(itemId, channelId), " +
				"FOREIGN KEY(channelId) references rss_channel(channelId))", null);
	}
	
	// insert channel
	public void insertChannel(Channel channel) throws DaoException{
		super.update("INSERT INTO rss_channel(channelId, title, description, link, moduleId, categoryId, active, autoCount, autoCountFor, createBy, createDate, updateBy, updateDate) " +
				"VALUES (#channelId#, #title#, #description#, #link#, #moduleId#, #categoryId#, #active#, #autoCount#, #autoCountFor#, #createBy#, now(), #updateBy#, now()) ", channel);
	}
	
	// update channel
	public void updateChannel(Channel channel) throws DaoException {
        super.update("UPDATE rss_channel SET " +
        		"title=#title#, description=#description#, link=#link#, moduleId=#moduleId#, categoryId=#categoryId#, active=#active#, autoCount=#autoCount#, autoCountFor=#autoCountFor#, updateBy=#updateBy#, updateDate=now() " +
        		"WHERE channelId=#channelId#", channel);
    }	
	
	// delete channel
	public void deleteChannel(String channelId) throws DaoException{
		Object[] args = { channelId	};
		//delete children - item
		super.update("DELETE FROM rss_item WHERE channelId=?", args);
		//delete parent - channel
		super.update("DELETE FROM rss_channel WHERE channelId=?", args);
	}	
	

	//	insert item
	public void insertItem(Item item) throws DaoException{
		super.update("INSERT INTO rss_item(itemId, channelId, rssItemId) " +
				"VALUES (#itemId#, #channelId# ,#rssItemId#)", item);
	}
	
	
	//	insert item
	public void insertCollItem(Collection item) throws DaoException{
		for(Iterator i=item.iterator();i.hasNext();){
			Item itm = (Item)i.next();
			super.update("INSERT INTO rss_item(itemId, channelId, rssItemId) " +
					"VALUES (#itemId#, #channelId# ,#rssItemId#)", itm);
		}		
	}
	
	//update item
	public void updateItem(Item item) throws DaoException {
        super.update("UPDATE rss_channel SET " +
        		"rssItemId=#rssItemId#, registeredItem=#registeredItem# " +
        		"WHERE itemId=#itemId#, channelId=#channelid#" , item);
    }
	
	// delete item
	public void deleteItem(String itemId, String channelId) throws DaoException{
		Object[] args = { itemId, channelId	};
		super.update("DELETE FROM rss_item WHERE itemId=? AND channelId=?", args);
	}
	
	
	// delete item
	public void deleteItemByChannel(String channelId) throws DaoException{
		Object[] args = { channelId	};
		super.update("DELETE FROM rss_item WHERE channelId=?", args);
	}	
	
	
	//return list of Channel
	public Collection loadActiveChannels() throws DaoException {
			Collection allChannels = new ArrayList();
			allChannels = super.select("SELECT channelId, title, description, link, moduleId, categoryId, active, autoCount, autoCountFor, createBy, createDate, updateBy, updateDate " +
		    						"FROM rss_channel WHERE active = '1'", Channel.class, null, 0, -1);
		return allChannels;
	}	
	
	// return only one Channel
	public Channel loadOneChannels(String channelId) throws DaoException {
		
		Collection oneChannels = null;
		oneChannels = super.select("SELECT channelId, title, description, link, moduleId, " +
								"categoryId, active, autoCount, autoCountFor, createBy, createDate, updateBy, updateDate " +
	    						"FROM rss_channel where channelId=?", Channel.class, new Object[]{channelId}, 0, -1);
		 for(Iterator i= oneChannels.iterator();i.hasNext();){
			 Channel channel = (Channel)i.next();
			 return channel;
		}
		return null;
	}
	
	// return only one Item
	public Item loadOneItem(String itemId) throws DaoException {
		Collection oneItem = null;
		oneItem = super.select("SELECT channelId, itemId, rssItemId " +
	    						"FROM rss_item where itemId=?", Item.class, new Object[]{itemId}, 0, -1);
		 for(Iterator i= oneItem.iterator();i.hasNext();){
			 Item item = (Item)i.next();
			 return item;
		}
		return null;
	}		
	
	
	public Collection loadItems(String channelId) throws DaoException {
		Object[] args = { channelId	};
		Collection allItems = new ArrayList();
		allItems = super.select("SELECT itemId, channelId, rssItemId " +
						"FROM rss_item WHERE channelId=?", Item.class, args, 0, -1);
		return allItems;
	}	
	
    public int selectChannelListCount(String ChannelId) throws DaoException
	{
	    String sql = "SELECT count(*) FROM rss_channel";
	    Map row = (Map)super.select(sql, HashMap.class, null, 0, -1).iterator().next();
	    return Integer.parseInt(row.get("total").toString());
	}	
    
    
	public Collection selectChannelBySearch(String searchBy, String sort, boolean desc, boolean active, int start, int rows)
	throws DaoException
	{
	    Collection ChannelList = new ArrayList();
	    Collection args = new ArrayList();

	    String sql = "SELECT channelId, title, description, link, moduleId, categoryId, createBy, createDate, updateBy, updateDate FROM rss_channel";
	    if(searchBy != null && searchBy.trim().length() > 0)
	    {
	        searchBy = (new StringBuilder()).append("%").append(searchBy.trim()).append("%").toString();
	        sql = (new StringBuilder()).append(sql).append(" WHERE (title LIKE ? OR description LIKE ? OR link LIKE ?)").toString();
	        args.add(searchBy);
	        args.add(searchBy);
	        args.add(searchBy);
	        
	        if (active == true){
		    	sql = (new StringBuilder()).append(sql).append(" AND active=true ").toString();
		    }
	    } else {
	    	if (active == true){
		    	sql = (new StringBuilder()).append(sql).append(" WHERE active=true ").toString();
		    }	
	    }
	    
	    
	    if (sort == null || sort.equals("")){
	    	sort ="title";
	    }
	    sql = (new StringBuilder()).append(sql).append(" ORDER BY ").toString();
	    sql = (new StringBuilder()).append(sql).append(sort).toString();
//	    if (sort.equals(null))
//	    	sql = (new StringBuilder()).append(sql).append(" ORDER BY title").toString();
//	    else 
//	    if(sort.equals("title"))
//	        sql = (new StringBuilder()).append(sql).append(" ORDER BY title").toString();
//	    else
//	    if(sort.equals("description"))
//	        sql = (new StringBuilder()).append(sql).append(" ORDER BY description").toString();
//	    else
//	    if(sort.equals("link"))
//	        sql = (new StringBuilder()).append(sql).append(" ORDER BY link").toString();
	    if(desc)
	        sql = (new StringBuilder()).append(sql).append(" DESC").toString();
	    ChannelList = super.select(sql, Channel.class, ((Object) (args.toArray())), start, rows);
	    return ChannelList;
	}
	

	public boolean countFoundItemKey (String itemId, String channelId) throws DaoException{
        boolean isFound = false;
        Object[] args = { itemId, channelId	};
        
        try
        {
            String sql = "SELECT COUNT(*) AS total FROM rss_item WHERE itemId=? AND channelId=?";
            Collection collection = super.select(sql,HashMap.class, args, 0, -1);
            int total =0;
            Map hashMap = (Map)collection.iterator().next();
            if(hashMap.get("total")!= null)
            	total = Integer.parseInt(hashMap.get("total").toString());
            if(total > 0)
            	isFound = true;
        }
        catch(Exception e)
        {
            e.getMessage();
        }
        return isFound;
	}
		
	    
	
	



	
	
}
