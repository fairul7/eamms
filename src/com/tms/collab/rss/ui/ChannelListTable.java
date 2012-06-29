package com.tms.collab.rss.ui;

import kacang.Application;
import kacang.stdui.Table;
import kacang.stdui.TableAction;
import kacang.stdui.TableColumn;
import kacang.stdui.TableFilter;
import kacang.stdui.TableModel;
import kacang.stdui.TableFormat;
import java.util.Collection;
import java.util.Iterator;

import kacang.ui.Forward;
import kacang.ui.Event;
import java.util.*;
import com.tms.collab.rss.model.Channel;
//import kacang.stdui.SelectBox;
//import kacang.stdui.TextField;

//import java.util.Collection;
//import java.util.List;
//import java.util.Map;
//import org.apache.commons.collections.SequencedHashMap;

import com.tms.collab.rss.model.RssHandler;

public class ChannelListTable extends Table{
	
	String strChannelId;
	
	public ChannelListTable(){
	}
	
	public ChannelListTable(String name){
		super(name);
	}

	public void init(){
		setWidth("100%");
		setModel(new ChannelListingModel());
	}
	
	public class ChannelListingModel extends TableModel{
		public ChannelListingModel(){
			Application app = Application.getInstance();
	        TableColumn idCol = new TableColumn("title", app.getMessage("rss.channel.title", "Title"));
	        idCol.setUrlParam("channelId");
	        addColumn(idCol);
	        addColumn(new TableColumn("link", app.getMessage("rss.channel.link","Link")));
	        addColumn(new TableColumn("description", app.getMessage("rss.channel.description","Description")));
            TableColumn itemCol = new TableColumn("channelId",app.getMessage("rss.channel.item", "Item"));

            itemCol.setFormat(new TableFormat(){
            	public String format(Object arg0) {
            		String channelid = (String)arg0;
            		String show = "Show";
            		StringBuffer returnItemCol = new StringBuffer("<a href=\"itemListing.jsp?channelId=" + channelid + "\" target=\"popup\" onClick=\"open('itemListing.jsp?channelId="+channelid+"','popup','height=400,width=400,toolbar=yes,scrollbars=yes,resizable=yes,directories=no,location=yes,status=yes,menubar=yes');return false\">"+show+"</a>");
            		return returnItemCol.toString();
            	}
            });
            addColumn(itemCol);	        
	        
	        addFilter(new TableFilter("searchName"));

	        // Add Channel Button
	        addAction(new TableAction("add", app.getMessage("rss.channel.addChannel", "Add Channel")));

	        // Delete Button
	        addAction(new TableAction("delete", app.getMessage("rss.channel.Delete", "Delete"), Application.getInstance().getMessage("rss.channel.deleteChannel","Delete Selected Channels ?")));
		}
		
		public Collection getTableRows() {
			String searchName = (String) getFilterValue("searchName");
			Application app = Application.getInstance();
			RssHandler handler = (RssHandler) app.getModule(RssHandler.class);
			return handler.getAllSearchChannels(searchName, getSort(), isDesc(), false,getStart(), getRows());
		}
			
		public int getTotalRowCount() {
			String searchName = (String) getFilterValue("searchName");
			Application app = Application.getInstance();
			RssHandler handler = (RssHandler) app.getModule(RssHandler.class);
			return handler.getAllSearchChannels(searchName, getSort(), isDesc(), false,0, -1).size();

		}	
		
		public Forward processAction(Event evt, String action, String[] selectedKeys) {
           Application app = Application.getInstance();
           RssHandler handler = (RssHandler) app.getModule(RssHandler.class);
           
           if ("delete".equals(action)) {
               for (int i=0; i<selectedKeys.length; i++) {
            	   handler.deleteChannel(selectedKeys[i]);
               }
           }
           
           if ("add".equals(action)) {
        	   return new Forward("add");
           }
           return null;
		}		
		
		public String getTableRowKey() {
			return "channelId";
		}
	}

	public String getStrChannelId() {
		return strChannelId;
	}

	public void setStrChannelId(String strChannelId) {
		this.strChannelId = strChannelId;
	}
	

}
