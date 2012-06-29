package com.tms.collab.rss.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import kacang.Application;
import kacang.model.DefaultModule;
import kacang.services.security.User;

import org.apache.commons.collections.SequencedHashMap;

import com.tms.collab.forum.model.Forum;
import com.tms.collab.forum.model.ForumModule;
import com.tms.collab.forum.model.Thread;

public class ForumHandler extends DefaultModule implements RssAble
{
	String itemDesc;
	String itemLink;
	String itemTitle;
	String itemName;
	String itemAuthor;
	String itemPubDate;
	String channelId;
	
	public String getModuleName() {
		return "Forum";
	}
	
	public Thread getContent(String categoryId){
		//Application app = Application.getInstance();
		//User currentUser;
		//currentUser = app.getCurrentUser();
		
		ForumModule fm = (ForumModule)Application.getInstance().getModule(ForumModule.class);
		try {
			Thread collForum = fm.getThread(categoryId, null);
			return collForum;
		} catch (Exception e){
			System.out.println("getContent()"+e.toString());
		}
		return null;
	}	
	
	public Map getCategories(){
		ForumModule fm = (ForumModule)Application.getInstance().getModule(ForumModule.class);
		Map mapContent = new SequencedHashMap();
		Forum forumObj;
		try {
			Collection collForum = null;
			collForum = fm.getAllForum(null);
			
			for (Iterator i = collForum.iterator(); i.hasNext();){
				forumObj = (Forum)i.next();
				String ForumId = forumObj.getForumId();
				String ForumName = forumObj.getName();
				//System.out.println("ForumId="+ForumId);
				//System.out.println("ForumName="+ForumName);
				mapContent.put(ForumId, ForumName);
				
			}
		} catch (Exception e){
			System.out.println("getCategories()"+e.toString());
		}
		return mapContent;
	}
	
	public Collection getRssItems(String categoryId, int count){
		ForumModule fm = (ForumModule)Application.getInstance().getModule(ForumModule.class);
		Thread forumObj;
		Collection list = new ArrayList();
		try {
			Collection collForum;
			if (count != 0){
				collForum = fm.getThreads(categoryId, null, 0, count, "creationDate", true);
			} else {
				collForum = fm.getThreads(categoryId, null, 0, -1, "creationDate", true);
			}
			//collForum.size();
    		for (Iterator i = collForum.iterator();i.hasNext();){
    			forumObj = (Thread)i.next();
    			String newThreadId = forumObj.getThreadId();
				String ForumName = forumObj.getSubject();
				list.add(newThreadId);
			}
		} catch (Exception e){
			System.out.println("getRssItems()"+e.toString());
		}
		return list;
	}

	// get Link of the Content
	public String getItemLink(String itemId){
		String threadName = getContent(itemId).getSubject();
		String threadId = getContent(itemId).getThreadId();
		
		if (itemId.equals(threadId)){
			String mode = "";
			if (!threadId.equals(null)){
				
				Application app = Application.getInstance();
				RssHandler rssHandler = (RssHandler)app.getModule(RssHandler.class);
				Channel channel = rssHandler.getOneChannel(getChannelId());				
				try {
					mode = channel.getLink().substring(0, channel.getLink().indexOf("/"));
				} catch (Exception e){
					System.out.println("getItemLink="+e.toString());
				}
				//System.out.println("mode="+mode);
				
				if (mode.equals("ekms")){
					itemLink = "ekms/forums/forumTopicList.jsp?forumId="+threadId;
				} else if (mode.equals("cms")){
					itemLink = "cms/forumTopicList.jsp?forumId="+threadId;
				} else {
					itemLink = mode+"/";
				}
			}
		}
		return itemLink;		
	}	
	
	// get Name (Title) of the Content
	public String getItemTitle(String itemId){
		String threadName = getContent(itemId).getSubject();
		String threadId = getContent(itemId).getThreadId();
		if (itemId.equals(threadId)){
			if (!(threadName == null)){
				if (threadName.equals("")){
					itemName = "Name is not specifiy!";
				} else {
					itemName = threadName;
				}
			} else {
				itemName = "Name is not specifiy!";
			}
		}
		return itemName;
	}	
	
	// get Description of the Content
	public String getItemDesc(String itemId){
		String threadDesc = getContent(itemId).getContent();
		String threadId = getContent(itemId).getThreadId();
		if (itemId.equals(threadId)){
			if (!(threadDesc == null)){
				if (threadDesc.equals("")){
					itemDesc = "Description is not specifiy!";
				} else {
					itemDesc = threadDesc;
				}
			} else {
				itemDesc = "Description is not specifiy!";
			}
		}
		return itemDesc;
	}		
	
	
	// get author of the Content
	public String getItemAuthor(String itemId){
		String threadAuthor = getContent(itemId).getOwnerId();
		String threadId = getContent(itemId).getThreadId();
		if (itemId.equals(threadId)){
			if (!(threadAuthor == null)){
				if (threadAuthor.equals("")){
					itemAuthor = "Author is not specifiy!";
				} else {
					itemAuthor = threadAuthor;
				}
			} else {
				itemAuthor = "Author is not specifiy!";
			}
		}
		return itemAuthor;
	}		
	
	// get pubDate of the Content
	public String getItemPubDate(String itemId){
		String threadPubDate = getContent(itemId).getModificationDate().toString();
		String threadId = getContent(itemId).getThreadId();
		if (itemId.equals(threadId)){
			if (!(threadPubDate == null)){
				if (threadPubDate.equals("")){
					itemPubDate = "Publish Date is not specifiy!";
				} else {
					itemPubDate = threadPubDate;
				}
			} else {
				itemPubDate = "Publish Date is not specifiy!";
			}
		}
		return itemPubDate;
	}		
	

	public Collection getItems(String[] itemId){
        Collection list = new ArrayList();
		return list;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

}
