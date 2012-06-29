package com.tms.collab.rss.ui;

import java.util.Collection;
import java.util.Iterator;

import kacang.Application;
import kacang.stdui.Button;
import kacang.stdui.Form;
import kacang.stdui.Label;
import kacang.stdui.Panel;
import kacang.ui.Event;
import kacang.ui.Forward;

import com.tms.collab.rss.model.Channel;
import com.tms.collab.rss.model.Item;
import com.tms.collab.rss.model.RssAble;
import com.tms.collab.rss.model.RssHandler;

public class ItemListForm extends Form{
	
	public static final String FORWARD_CLOSE = "close";
	private String channelId;
	protected Button close;
	
	
	public void init(){
		initForm();
	}
	
	public void initForm(){
		setMethod("POST"); 
		setColumns(1);

		//Application app = Application.getInstance();		
		//addChild(new Label("newItem1", app.getMessage("rss.channel.DisplayMessage1", "Display Item")));
	}
	
	public void onRequest(Event evt) {
		Application app = Application.getInstance();	
		channelId = evt.getRequest().getParameter("channelId");
		
		//Log.getLog(getClass()).error("channelId " + channelId);
		//Log.getLog(getClass()).error("getChannelId " + getChannelId());
		Panel itemPanel = new Panel("itemPanel");
		itemPanel.setColumns(1);
		
    	try {
    		RssHandler handler = (RssHandler)Application.getInstance().getModule(RssHandler.class);
    		Channel channel = handler.getOneChannel(channelId);
        	Collection collection = handler.getAllItemsByChannelId(getChannelId());
        	RssAble rssable = (RssAble)Application.getInstance().getModule(Class.forName(channel.getModuleId()));
        	Collection rssColl = rssable.getRssItems(channel.getCategoryId(),channel.getAutoCountFor());
        	Item item = new Item();
    		
	    	for(Iterator i = collection.iterator(); i.hasNext();){
	    		item = (Item)i.next();
	    		item.getItemId();
	    		item.getChannelId();
	    		
	    		for (Iterator k = rssColl.iterator();k.hasNext();){
	    			String rssItemId = k.next().toString();
	    			if (item.getItemId().equals(rssItemId)){
	    	    		Label label1 = new Label(rssable.getItemTitle(item.getItemId()), rssable.getItemTitle(item.getItemId()));
	    	    		itemPanel.addChild(label1);
						itemPanel.setColspan(2);
					}
	    		}
	    	}
    	} catch (Exception e) {
    		e.toString();
    	}
    	
    	addChild(itemPanel);
		close = new Button("close", app.getMessage("rss.channel.close", "Close"));
		Panel buttonPanel = new Panel("buttonPanel");
		buttonPanel.setColumns(1);
		buttonPanel.addChild(close);
		addChild(buttonPanel);
	}
	
	public Forward onValidate(Event evt) throws RuntimeException {
	   Forward forward = new Forward();
       String buttonName = findButtonClicked(evt);
       if(buttonName != null && close.getAbsoluteName().equals(buttonName)) {
           forward = new Forward(FORWARD_CLOSE);
       }
	   return forward;
    }

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}
	
	
	
	

}
