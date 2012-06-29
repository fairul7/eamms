package com.tms.collab.rss.model;

import java.util.*;

public interface RssAble {
	Map getCategories();
	Collection getRssItems(String categoryId, int count);
	Collection getItems(String[] itemId);
	String getItemDesc(String itemId);
	String getItemTitle(String itemId);
	String getItemLink(String itemId);
	String getItemAuthor(String itemId);
	String getItemPubDate(String itemId);
	String getModuleName();
	void setChannelId(String channelId);
	String getChannelId();
}
