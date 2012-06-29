package com.tms.collab.rss.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import kacang.Application;
import kacang.model.DefaultModule;

import org.apache.commons.collections.SequencedHashMap;

import com.tms.cms.core.model.ContentObject;
import com.tms.cms.core.model.ContentPublisher;

public class ContentHomeHandler extends DefaultModule implements RssAble
{
	String itemDesc;
	String itemLink;
	String itemTitle;
	String itemName;
	String itemAuthor;
	String itemPubDate;
	String channelId;
	Map mapContent = new SequencedHashMap();
	
	
	public String getModuleName() {
		return "Content";
	}
	
	public Collection getContent(String categoryId){
		String[] newCategory = {categoryId};
		ContentPublisher cp = (ContentPublisher)Application.getInstance().getModule(ContentPublisher.class);
		try {
			Collection collContent = cp.viewListWithContents(newCategory, null, null, null, false, "DATE", false, 0, -1, null, null);
			return collContent;
		} catch (Exception e){
			System.out.println("getContent()"+e.toString());
		}
		return null;
	}	
	
	//interface
	public Map getCategories(){
		ContentPublisher cp = (ContentPublisher)Application.getInstance().getModule(ContentPublisher.class);
		Map mapContent = new SequencedHashMap();
		try {
			Collection collContent = null;
			collContent = cp.viewListWithContents(null, null, null, "", true, "NAME", false, 0, -1, null, null);
			for (Iterator i = collContent.iterator(); i.hasNext();){
				ContentObject contentObj = (ContentObject)i.next();
				String parentId = contentObj.getParentId();
				String contentName = contentObj.getName();
				String contentId = contentObj.getId();
				String contentClassName = contentObj.getClassName();
				
				return getCategoriesChild(contentId,"");
			}
			
		} catch (Exception e){
			System.out.println("getCategories()"+e.toString());
		}
		return mapContent;
	}

	
	public Map getCategoriesChild(String parentId, String dash){
		ContentPublisher cp = (ContentPublisher)Application.getInstance().getModule(ContentPublisher.class);
		Collection collContentChild;
		Collection collContentChild2;
		Collection collContentChild3;
		try {
			collContentChild = cp.viewListWithContents(null, null, null, parentId, false, "NAME", false, 0, -1, null, null);
			for (Iterator c = collContentChild.iterator(); c.hasNext();){
				ContentObject contentObjChild = (ContentObject)c.next();
				String ContentNameChild = contentObjChild.getName();
				String ContentIdChild = contentObjChild.getId();
				collContentChild = cp.viewListWithContents(null, null, null, ContentIdChild, false, "NAME", false, 0, -1, null, null);
				if (dash.equals("")) {
					mapContent.put(ContentIdChild, ContentNameChild);
				}
				
				collContentChild2 = cp.viewListWithContents(null, null, null, ContentIdChild, false, "NAME", false, 0, -1, null, null);
			
				for (Iterator c2 = collContentChild2.iterator(); c2.hasNext();){
					ContentObject contentObjChild2 = (ContentObject)c2.next();
					String ContentNameChild2 = contentObjChild2.getName();
					String ContentIdChild2 = contentObjChild2.getId();
					collContentChild3 = cp.viewListWithContents(null, null, null, ContentIdChild2, false, "NAME", false, 0, -1, null, null);
					
					if (collContentChild3.size() > 0){
						mapContent.put(ContentIdChild2, dash+"...| "+ContentNameChild2);
						//System.out.println("ContentNameChild2="+dash+ContentNameChild2);
						getCategoriesChild(ContentIdChild2, dash+"...");
					}
				}
				
			}	
			
		} catch(Exception e) {
			System.out.println("getCategoriesChild()"+e.toString());
		}
		return mapContent;
	}
	
	//interface
	public Collection getRssItems(String categoryId, int count){
		String[] newCategory = {categoryId};
		ContentPublisher cp = (ContentPublisher)Application.getInstance().getModule(ContentPublisher.class);
		ContentObject contentObj;
		Collection list = new ArrayList();
		try {
			Collection collContent;
			if (count != 0){
				collContent = cp.viewListWithContents(null, null, null, categoryId, false, "DATE", false, 0, count, null, null);
			} else {
				collContent = cp.viewListWithContents(null, null, null, categoryId, false, "DATE", false, 0, -1, null, null);
			}
    		for (Iterator i = collContent.iterator();i.hasNext();){
				contentObj = (ContentObject)i.next();
				String ContentId = contentObj.getId();
				String ContentName = contentObj.getName();
				String ContentClassName = contentObj.getClassName();
				list.add(ContentId);
			}
		} catch (Exception e){
			System.out.println("getRssItems()"+e.toString());
		}
		return list;
	}
	
	// interface
	// get Name (Title) of the Content
	public String getItemLink(String itemId){
		String mode = "";
		for (Iterator i = getContent(itemId).iterator();i.hasNext();){
			ContentObject contentObj = (ContentObject)i.next();
			String ContentId = contentObj.getId();
			if (itemId.equals(ContentId)){
				Application app = Application.getInstance();
				RssHandler rssHandler = (RssHandler)app.getModule(RssHandler.class);
				Channel channel = rssHandler.getOneChannel(getChannelId());				
				
				try {
					mode = channel.getLink().substring(0, channel.getLink().indexOf("/"));
				} catch (Exception e){
					System.out.println("getItemLink="+e.toString());
				}
				
				if (mode.equals("ekms")){
					itemLink = "ekms/content/content.jsp?id="+ContentId;
				} else if (mode.equals("cms")){
					itemLink = "cms/content.jsp?id="+ContentId;
				} else {
					itemLink = mode+"/";
				}
			}
			return itemLink;
		}
		return null;		
	}	
	
	// interface
	// get Name (Title) of the Content
	public String getItemTitle(String itemId){
		for (Iterator i = getContent(itemId).iterator();i.hasNext();){
			ContentObject contentObj = (ContentObject)i.next();
			String ContentName = contentObj.getName();
			String ContentId = contentObj.getId();
			if (itemId.equals(ContentId)){
				if (!(ContentName == null)){
					if (ContentName.equals("")){
						itemName = "Name is not specifiy!";
					} else {
						itemName = ContentName;
					}
				} else {
					itemName = "Name is not specifiy!";
				}
			}
			return itemName;
		}
		return null;		
	}	
	
	// interface
	// get Name of the Content
	public String getItemDesc(String itemId){
		for (Iterator i = getContent(itemId).iterator();i.hasNext();){
			ContentObject contentObj = (ContentObject)i.next();
			//String ContentDesc = contentObj.getDescription();
			String ContentDesc = contentObj.getSummary();
			//String ContentName = contentObj.getName();
			String ContentId = contentObj.getId();
			
			if (itemId.equals(ContentId)){
				if (!(ContentDesc == null)){
					if (ContentDesc.equals("")){
						itemDesc = "Description is not specifiy!";
					} else {
						itemDesc = ContentDesc;
					}
				} else {
					itemDesc = "Description is not specifiy!";
				}
			}
			return itemDesc;
		}
		return null;		
	}
	
	// interface
	// get Author of the Content
	public String getItemAuthor(String itemId){
		for (Iterator i = getContent(itemId).iterator();i.hasNext();){
			ContentObject contentObj = (ContentObject)i.next();
			String ContentAuthor = contentObj.getAuthor();
			String ContentId = contentObj.getId();
			if (itemId.equals(ContentId)){
				if (!(ContentAuthor == null)){
					if (ContentAuthor.equals("")){
						itemAuthor = "Author is not specifiy!";
					} else {
						itemAuthor = ContentAuthor;
					}
				} else {
					itemAuthor = "Author is not specifiy!";
				}
			}
			return itemAuthor;
		}
		return null;		
	}
	
	// interface
	// get PubDate of the Content
	public String getItemPubDate(String itemId){
		for (Iterator i = getContent(itemId).iterator();i.hasNext();){
			ContentObject contentObj = (ContentObject)i.next();
			String ContentPubDate = contentObj.getDate().toString();
			String ContentId = contentObj.getId();
			if (itemId.equals(ContentId)){
				if (!(ContentPubDate == null)){
					if (ContentPubDate.equals("")){
						itemPubDate = "Publish Date is not specifiy!";
					} else {
						itemPubDate = ContentPubDate;
					}
				} else {
					itemPubDate = "Publish Date is not specifiy!";
				}
			}
			return itemPubDate;
		}
		return null;		
	}			
	
	// interface
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
