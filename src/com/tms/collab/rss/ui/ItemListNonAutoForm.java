package com.tms.collab.rss.ui;

import java.util.Collection;
import java.util.Iterator;

import kacang.Application;
import kacang.stdui.Button;
import kacang.stdui.CheckBox;
import kacang.stdui.Form;
import kacang.stdui.Label;
import kacang.stdui.Panel;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.UuidGenerator;

import com.tms.collab.rss.model.Item;
import com.tms.collab.rss.model.RssAble;
import com.tms.collab.rss.model.RssHandler;

public class ItemListNonAutoForm extends Form{
	public static final String FORWARD_SUCCESS = "success";
    public static final String FORWARD_ERROR = "error";
    public static final String FORWARD_CLOSE = "close";
	protected CheckBox[] checkItem;
	protected CheckBox checkAllItem;
	protected Button save;
	protected Button close;
	private String channelId;
	private int totalCount;
	private String moduleId;
	private String rCategoryId;
	boolean tickCheckItem;
	
	//public void init(){
		//initForm();
	//}
	
	public CheckBox getCheckAllItem() {
		return checkAllItem;
	}

	public void setCheckAllItem(CheckBox checkAllItem) {
		this.checkAllItem = checkAllItem;
	}

	public CheckBox[] getCheckItem() {
		return checkItem;
	}

	public void setCheckItem(CheckBox[] checkItem) {
		this.checkItem = checkItem;
	}

	public Button getSave() {
		return save;
	}

	public void setSave(Button save) {
		this.save = save;
	}

	public boolean isTickCheckItem() {
		return tickCheckItem;
	}

	public void setTickCheckItem(boolean tickCheckItem) {
		this.tickCheckItem = tickCheckItem;
	}

	public void initForm(){
		int itemSize = 0;
		setMethod("POST"); 
		setColumns(1); // set table column
		Application app = Application.getInstance();		

		CheckBox checkAllItem = new CheckBox("CheckAllItem");
		checkAllItem.setText(app.getMessage("rss.channel.SelectAll", "Select All"));
		
		addChild(checkAllItem);
//		Label label1 = new Label("newItem1", "<b>"+app.getMessage("rss.channel.DisplayMessage", "Display Items")+"</b>");
//		label1.setAlign("left");
//		addChild(label1);
				
		Panel itemPanel2 = new Panel("itemPanel");
		itemPanel2.setColumns(2);
		
		try {
			//System.out.println("getModuleId="+ getModuleId());
			//System.out.println("moduleId="+ moduleId);
			
			//System.out.println("getChannelId="+ getChannelId());
			//System.out.println("channelId="+ channelId);
			
			RssAble rss = (RssAble)Application.getInstance().getModule(Class.forName(getModuleId()));
			Collection collection = rss.getRssItems(getRCategoryId(), getTotalCount());
			itemSize = rss.getRssItems(getRCategoryId(), getTotalCount()).size();
			checkItem = new CheckBox[itemSize];
			
			int a = 0;
			for(Iterator i = collection.iterator();i.hasNext();){
				String outItem = i.next().toString();
				String outItemDesc = rss.getItemTitle(outItem);
				checkItem[a]= new CheckBox("checkItem" + a);
				itemPanel2.addChild(checkItem[a]);
				itemPanel2.addChild(new Label(outItemDesc, outItemDesc));
				//itemPanel2.addChild(new Label(outItem, outItem));
				a++;
			}
		} catch (Exception e){
			System.out.println("initForm="+e.toString());
		}
		
		itemPanel2.setColspan(2);
		addChild(itemPanel2);
		checkAllItem.setOnClick("checkAll("+itemSize+")");

		
		save = new Button("save", app.getMessage("rss.channel.save", "Save"));
		if (itemSize == 0){
			save.setHidden(true);
		}
		save.setOnClick("return isSave()");
		close = new Button("close", app.getMessage("rss.channel.close", "Close"));
		Panel buttonPanel = new Panel("buttonPanel");
		
		buttonPanel.setColumns(2);
		buttonPanel.addChild(save);
		buttonPanel.addChild(close);
		addChild(buttonPanel);
	}
	
	public void onRequest(Event evt) {
		String Id = evt.getRequest().getParameter("channelId");
		String count = evt.getRequest().getParameter("count");
		//String moduleId = evt.getRequest().getParameter("moduleId");
		String categoryId = evt.getRequest().getParameter("categoryId");
		
		if(Id != null)
			channelId = Id;
		
		if(count != null)
			totalCount = Integer.parseInt(count);
		
		if(moduleId != null)
			moduleId = moduleId.substring(1, moduleId.indexOf("]"));

		if(categoryId != null)
			rCategoryId = categoryId.substring(1, categoryId.indexOf("]"));
		
		initForm();
		try{
			//declare
			RssHandler handler = (RssHandler) Application.getInstance().getModule(RssHandler.class);
			RssAble rssable = (RssAble)Application.getInstance().getModule(Class.forName(moduleId));
			Collection collection = handler.getAllItemsByChannelId(Id);
			Collection fcollection = rssable.getRssItems(rCategoryId, totalCount);
			Item item = new Item();

			for(Iterator i = collection.iterator();i.hasNext();){
				item = (Item)i.next();
				int k =0;
				
				for(Iterator f = fcollection.iterator();f.hasNext();){
					String nItem = f.next().toString();
					if (item.getItemId().equals(nItem)){
						checkItem[k].setChecked(true);
					}
					k++;
				} 
			}
		} catch (Exception e) {
			System.out.println("onRequest"+e.toString());
		}
	}
	
	public Forward onValidate(Event evt) throws RuntimeException {
	   Forward forward = new Forward();
       String buttonName = findButtonClicked(evt);
       if(buttonName != null && save.getAbsoluteName().equals(buttonName)) {
    	   forward = saveItem();
       }
       if(buttonName != null && close.getAbsoluteName().equals(buttonName)) {
           forward = new Forward(FORWARD_CLOSE);
       }       
	   return forward;
    }

    public Forward saveItem() throws RuntimeException
    {
    	boolean isSuccess = false;
    	Item item = new Item();
    	try{
	    	RssAble rssable = (RssAble)Application.getInstance().getModule(Class.forName(getModuleId()));
			Collection fcollection = rssable.getRssItems(getRCategoryId(), getTotalCount());
			RssHandler handler = (RssHandler)Application.getInstance().getModule(RssHandler.class);
			int j = 0;
            handler.deleteItemByChannel(getChannelId());
            
			for(Iterator f = fcollection.iterator();f.hasNext();){
				String fitem = f.next().toString(); 
	    		item.setChannelId(getChannelId());
	            item.setItemId(fitem);
	            item.setRssItemId(UuidGenerator.getInstance().getUuid());

	            boolean isFound = handler.getCountFoundItemKey(fitem,getChannelId());
	    		if (checkItem[j].isChecked() == true) {
			        if (isFound == false){
			        	handler.addItem(item);
			        }
	    		} else {
	    			if (isFound == true){
	    				handler.deleteItem(fitem,getChannelId());
	    			}
	    		}
	    		j++;
	    		isSuccess = true;
	    	}
    	} catch (Exception e) {
    		System.out.println("saveItem"+e.toString());
    	}
	    	
    	Forward forward;        
    	if (isSuccess == true) {
    		forward = new Forward(FORWARD_SUCCESS);        
    	} else {
    		forward = new Forward(FORWARD_ERROR);
    	}
    	return forward;
    	
    }	

    
//    public String getDefaultTemplate() {
//    	return "rss/itemListing";
//    }
  
	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}
	

	public String getModuleId() {
		return moduleId;
	}

	public void setModuleId(String moduleId) {
		this.moduleId = moduleId;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public String getRCategoryId() {
		return rCategoryId;
	}

	public void setRCategoryId(String categoryId) {
		rCategoryId = categoryId;
	}
}
