package com.tms.collab.rss.model;

import kacang.model.*;

public class Item extends DefaultDataObject {
	private String itemId;

	private String channelId;

	private String rssItemId;

	private String registeredItem;

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getRssItemId() {
		return rssItemId;
	}

	public void setRssItemId(String rssItemId) {
		this.rssItemId = rssItemId;
	}

	public String getRegisteredItem() {
		return registeredItem;
	}

	public void setRegisteredItem(String registeredItem) {
		this.registeredItem = registeredItem;
	}

}
