package com.tms.collab.rss.ui;

import java.util.Collection;

import kacang.Application;
import kacang.stdui.Table;

import kacang.stdui.TableColumn;
import kacang.stdui.TableFilter;
import kacang.stdui.TableFormat;
import kacang.stdui.TableModel;


import com.tms.collab.rss.model.RssHandler;
import com.tms.ekms.setup.model.SetupModule;

public class RssListTable extends Table{
	
	String strChannelId;
	
	public RssListTable(){
	}
	
	public RssListTable(String name){
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
			addColumn(idCol);
            TableColumn rssCol = new TableColumn("channelId",app.getMessage("rss.channel.rss", "RSS"));
            rssCol.setFormat(new TableFormat(){
            	public String format(Object arg0) {
            		Application application = Application.getInstance();
            		SetupModule setupModel = (SetupModule) application.getModule(SetupModule.class);
            		String siteURL = "";
        			try {
        				siteURL = setupModel.get("siteUrl");
        			} catch (Exception e) {
        				System.out.println(e.toString());
        			}
            		
            		String channelid = (String)arg0;
            		String show = "images/rss.gif";
            		StringBuffer returnItemRssCol = new StringBuffer("<a href=\"" +siteURL+ "/rssXml.jsp?channelId="+channelid+"\" target=\"popup\" onClick=\"open('"+siteURL+"/rssXml.jsp?channelId="+channelid+"','popup','toolbar=yes,scrollbars=yes,resizable=yes,location=yes');return false\"><IMG SRC=" + show +"></a>");
            		//System.out.println("returnItemCol="+returnItemCol.toString());
            		return returnItemRssCol.toString();
            	}
            });
            addColumn(rssCol);	    
            addFilter(new TableFilter("searchName"));
		}
		
		public Collection getTableRows() {
			String searchName = (String) getFilterValue("searchName");
			Application app = Application.getInstance();
			RssHandler handler = (RssHandler) app.getModule(RssHandler.class);
			return handler.getAllSearchChannels(searchName, getSort(), isDesc(), true, getStart(), getRows());
		}
	
		public int getTotalRowCount() {
			String searchName = (String) getFilterValue("searchName");
			Application app = Application.getInstance();
			RssHandler handler = (RssHandler) app.getModule(RssHandler.class);
			return handler.getAllSearchChannels(searchName, getSort(), isDesc(), true, 0, -1).size();
		}	
		
	}

	public String getStrChannelId() {
		return strChannelId;
	}

	public void setStrChannelId(String strChannelId) {
		this.strChannelId = strChannelId;
	}
	
	

}
;
