package com.tms.collab.rss.ui;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import kacang.Application;
import kacang.services.security.User;
import kacang.stdui.Button;
import kacang.stdui.CheckBox;
import kacang.stdui.Form;
import kacang.stdui.Label;
import kacang.stdui.Panel;
import kacang.stdui.ResetButton;
import kacang.stdui.SelectBox;
import kacang.stdui.TextBox;
import kacang.stdui.TextField;
import kacang.stdui.validator.Validator;
import kacang.stdui.validator.ValidatorIsNumeric;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.UuidGenerator;

import org.apache.commons.collections.SequencedHashMap;

import com.tms.collab.rss.model.Channel;
import com.tms.collab.rss.model.Item;
import com.tms.collab.rss.model.RssAble;
import com.tms.collab.rss.model.RssHandler;
import com.tms.ekms.setup.model.SetupModule;


public class ChannelEditForm extends Form{

	public static final String FORWARD_SUCCESS = "success";
    public static final String FORWARD_ERROR = "error";
    public static final String FORWARD_POPUP_SUCCESS = "popup_success";
    public static final String FORWARD_CANCEL = "cancel";
	protected TextField title;
	protected TextField link;
	protected TextBox description;
	protected SelectBox module;
	protected SelectBox category;
	protected TextField autocountfor;
	protected CheckBox active;
	protected CheckBox autocount;
	protected Button submit;
	protected ResetButton reset;
	protected Button addItem;
	protected Button cancel;
    protected Validator validTitle; 
    protected Validator validModule;
    protected Validator validLink;
    protected Validator validCategory;
    protected Validator validDesc;
    protected ValidatorIsNumeric validAutoCountFor;
    
    private String channelId;
	private String strTitle;
	private String strLink;
	private String strDescription;
	private String strAutoCountFor;
	private String moduleSelected;
	private int intCountFor;
	private boolean tickActive;
	private boolean tickAutoCount;
	
	public void init(){
		//Application app = Application.getInstance();
		//User currentUser;  
		//currentUser =app.getCurrentUser();
		//currentUser.getUsername();
		initForm();
	}
	
	public void initForm(){
		setMethod("POST");
		setColumns(2);

		Application app = Application.getInstance();

		SetupModule setupModel = (SetupModule) app.getModule(SetupModule.class);
		String siteURL = "";
		try {
			siteURL = setupModel.get("siteUrl");
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		
		Label label1 = new Label("Title", "<b>"+app.getMessage("rss.channel.manTitle", "Title*")+"<b>");
		label1.setAlign("right");
		addChild(label1);
		title = new TextField("title");
		validTitle = new ValidatorNotEmpty("ValidTitle", app.getMessage("channel.message.vNotEmpty", "Title Not allow to empty"));
		title.addChild(validTitle);
		addChild(title);

		Label label2 = new Label("Link", "<b>"+app.getMessage("rss.channel.link", "Link")+"<b>");
		label2.setAlign("right");
		addChild(label2);
		
		Label labelLink = new Label(siteURL+"/",siteURL+"/");	
		link = new TextField("link");
		validLink = new ValidatorNotEmpty("ValidLink", app.getMessage("channel.message.vNotEmpty", "Title Not allow to empty"));
		link.addChild(validLink);

		Panel panelLink = new Panel("PanelLink");
		panelLink.setColumns(2);
		panelLink.addChild(labelLink);
		panelLink.addChild(link);
		addChild(panelLink);		
		
		Label label3 = new Label("Description", "<b>"+app.getMessage("rss.channel.description", "Description")+"<b>");
		label3.setAlign("right");
		addChild(label3);
		description = new TextBox("description");
		validDesc = new ValidatorNotEmpty("ValidDesc", app.getMessage("channel.message.vNotEmpty", "Title Not allow to empty"));
		description.addChild(validDesc);
		addChild(description);
		
		Label label4 = new Label("Module", "<b>"+app.getMessage("rss.channel.manModule", "Module*")+"<b>");
		label4.setAlign("right");
		addChild(label4);	
		module = new SelectBox("module");
		module.setMultiple(false);
		module.addOption("-1", "Select A Module");

        try
        {
        	RssHandler handler = (RssHandler)Application.getInstance().getModule(RssHandler.class);
            Collection moduleList = handler.getModules();
            //Object t = new Object();
            Object t = null;
            for(Iterator i = moduleList.iterator(); i.hasNext(); ){
            	t = (Object)i.next();
            	t = (Object)Application.getInstance().getModule((Class)t);
            	
            	if(t instanceof RssAble){
            		RssAble rssable = (RssAble)Application.getInstance().getModule(t.getClass());
        			//System.out.println("Modulename = "+rssable.getmoduleName());
            		module.addOption(String.valueOf(t).substring(0, String.valueOf(t).indexOf("@")), rssable.getModuleName());
            	}
            }
        }
        catch(Exception e) {
        	System.out.println("init="+e.toString());
        }
        module.setOnChange("submit()");
		addChild(module);

		Label label6 = new Label("Category", "<b>"+app.getMessage("rss.channel.manCategory", "Category*")+"</b>");
		label6.setAlign("right");
		addChild(label6);	
		//addChild(new Label("Category", app.getMessage("rss.channel.category", "Category")));
		category = new SelectBox("category");
		category.setMultiple(false);
		addChild(category);		
		
		Label label7 = new Label("AutoCountFor", "<b>"+app.getMessage("rss.channel.autoCountFor", "Auto Count For")+"</b>");
		label7.setAlign("right");
		addChild(label7);	
		
		autocountfor = new TextField("countfor");
		validAutoCountFor = new ValidatorIsNumeric("validAutoCountFor", app.getMessage("channel.message.vNotNumberEmpty", "must be Numeric"));
		autocountfor.addChild(validAutoCountFor);
		autocountfor.setOnBlur("disabledAutoCount()");
		autocountfor.setMaxlength("10");
		autocountfor.setSize("5");
		addChild(autocountfor);

		addChild(new Label("CheckActive", ""));
		active= new CheckBox("active");
		active.setText(app.getMessage("rss.channel.active", "Active"));
		addChild(active);		
		
		addChild(new Label("CheckAutoCount", ""));
		autocount= new CheckBox("autocount");
		autocount.setText(app.getMessage("rss.channel.autoCount", "Auto Count"));
		
		addChild(autocount);
		
		submit = new Button("Submit", app.getMessage("rss.channel.submit", "Submit"));
		addItem = new Button("AddItem", app.getMessage("rss.channel.addItem", "Add Item"));
		
		reset = new ResetButton("reset");
		reset.setText("Reset");
		
		cancel = new Button("cancel", app.getMessage("rss.channel.cancel", "Cancel"));
		   
		Panel buttonPanel = new Panel("buttonPanel");
		buttonPanel.setColumns(4);
		addItem.setOnFocus("disabledButton()");
		addItem.setOnClick("setZero()");
		submit.setOnClick("setZero()");
		buttonPanel.addChild(addItem);
		buttonPanel.addChild(submit);
		buttonPanel.addChild(reset);
		buttonPanel.addChild(cancel);
		addChild(new Label("empty1", ""));
		addChild(buttonPanel);
	}
	
	public void onRequest(Event evt) {
		
		
		init();
        RssHandler handler = (RssHandler) Application.getInstance().getModule(RssHandler.class);
        //evt.getRequest().getSession().setAttribute("channelId", channelId);
        String channelId = evt.getRequest().getParameter("channelId");
        Channel channel = handler.getOneChannel(channelId);
        	title.setValue(channel.getTitle());
            description.setValue(channel.getDescription());	
            link.setValue(channel.getLink());
            module.setSelectedOption(channel.getModuleId());
            try
        	{
            	RssAble f = (RssAble) Application.getInstance().getModule(Class.forName(channel.getModuleId()));
            	category.setOptionMap(f.getCategories());
            	category.setSelectedOption(channel.getCategoryId());	
        	} catch (Exception e) {
        		System.out.println("onRequest="+e.toString());
        	}
            active.setChecked(channel.isActive());
            autocount.setChecked(channel.isAutoCount());
    		autocountfor.setValue(channel.getAutoCountFor());
            intCountFor = channel.getAutoCountFor();
            autocount.setOnClick("hideButton("+intCountFor+")");
    }	
	
    public Forward editChannel() throws RuntimeException
    {
    	Application app = Application.getInstance();
    	User currentUser;
    	currentUser = app.getCurrentUser();
    	
    	Forward forward;
    	Channel channel = new Channel();
        channel.setChannelId(channelId);
        channel.setTitle((String)title.getValue());
        channel.setDescription((String)description.getValue());
        channel.setLink((String)link.getValue());
        channel.setModuleId((String)module.getSelectedOptions().keySet().iterator().next().toString());
        channel.setCategoryId((String)category.getSelectedOptions().keySet().iterator().next().toString());
        channel.setActive(active.isChecked());
        channel.setAutoCount(autocount.isChecked());
        if (autocountfor.getValue().equals("")){
        	autocountfor.setValue("0");
        	channel.setAutoCountFor(0);
        } else {
        	channel.setAutoCountFor(Integer.parseInt((String)autocountfor.getValue()));
        }
        channel.setUpdateBy(currentUser.getUsername());
        //channel.setCreateBy(channel.getCreateBy());
        //channel.setCreateDate(channel.getCreateDate());
        
        try {
        	RssAble rssable = (RssAble)Application.getInstance().getModule(Class.forName(channel.getModuleId()));
        	Collection collection = rssable.getRssItems(channel.getCategoryId(), channel.getAutoCountFor());
        	RssHandler handler = (RssHandler)Application.getInstance().getModule(RssHandler.class);
        	handler.updateChannel(channel);

	        if (autocount.isChecked() == true){
		        //delete old item before adding item
	        	handler.deleteItemByChannel(getChannelId());
	        	
	        	Item item = new Item();
	        	for(Iterator i = collection.iterator();i.hasNext();){
	        		String fitem = i.next().toString();
	        		boolean isFound = handler.getCountFoundItemKey(fitem,getChannelId());
	        		if (isFound == false){
		        		item.setChannelId(getChannelId());
			            item.setItemId(fitem);
			            item.setRssItemId(UuidGenerator.getInstance().getUuid());
				        handler.addItem(item);
	        		}
	        	}
	        }
        	forward = new Forward(FORWARD_SUCCESS);        
        } catch (Exception e){
            forward = new Forward(FORWARD_ERROR);
        }
        return forward;
    }
    
    public Forward onValidate(Event evt) throws RuntimeException
    {
    	Forward forward = null;
        String buttonName = findButtonClicked(evt);
        if(buttonName != null && submit.getAbsoluteName().equals(buttonName)) {
        	//System.out.println("auto1="+autocountfor.getValue());
        	forward = editChannel();
        }
        if(buttonName != null && addItem.getAbsoluteName().equals(buttonName)) {
        	//System.out.println("auto2="+autocountfor.getValue());
            forward = new Forward(FORWARD_POPUP_SUCCESS);
        }
        return forward;
    }    
    
    public Forward onSubmit(Event event) {
    	Forward forward = super.onSubmit(event);
    	String buttonName = findButtonClicked(event);
    	
    	if (!submit.getAbsoluteName().equals(buttonName) && !addItem.getAbsoluteName().equals(buttonName)) {
			moduleSelected = (String) module.getSelectedOptions().keySet().iterator().next();
			strTitle = title.getValue().toString();
			strLink = link.getValue().toString();
			strDescription = description.getValue().toString();
			strAutoCountFor = autocountfor.getValue().toString();
			tickActive = active.isChecked();
			tickAutoCount = autocount.isChecked();
			
			if (!moduleSelected.equals("-1")){
            	try
            	{
            		//init();
            		RssAble f = (RssAble) Application.getInstance().getModule(Class.forName(moduleSelected));
	        		category.setOptionMap(f.getCategories());
	        		module.setSelectedOption(moduleSelected.toString());
	        		//System.out.println("11="+moduleSelected.toString());
            	}
            	catch(Exception e)
            	{
            		System.out.println("onSubmit="+e.toString());
            	}
            } else {
            	Map m = new SequencedHashMap();
            	m.put("", "");
            	category.setOptionMap(m);
            	//System.out.println("22="+moduleSelected.toString());
            }   

			title.setValue(strTitle);
    		link.setValue(strLink);
    		description.setValue(strDescription);
    		autocountfor.setValue(strAutoCountFor);
    		active.setValue(tickActive);
    		active.setChecked(tickActive);
    		autocount.setChecked(tickAutoCount);
	    	setInvalid(false);
	    	
	   		if (title.getValue().toString() ==  ""){
    			title.setInvalid(false);
    			this.setInvalid(false);
    			validTitle.setText("");
    		}
	   		if (link.getValue().toString() ==  ""){
	   			link.setInvalid(false);
	   			this.setInvalid(false);
	   			validLink.setText("");
    		}
	   		if (description.getValue().toString() ==  ""){
	   			description.setInvalid(false);
	   			this.setInvalid(false);
	   			validDesc.setText("");
    		}
	    	
	    	
	    	
    	} else {
    		
    		Application app = Application.getInstance();
    		
			if(module.getSelectedOptions().keySet().iterator().next().toString().equals("-1")){
				module.setInvalid(true);
				this.setInvalid(true);
			}
			
    		if (title.getValue().toString() ==  ""){
    			title.setInvalid(true);
    			validTitle.setInvalid(true);
    			validTitle.setText(app.getMessage("channel.message.vNotEmpty", "Must not be Empty"));
    		}
	   		if (link.getValue().toString() ==  ""){
	   			link.setInvalid(true);
	   			validLink.setInvalid(true);
	   			validLink.setText(app.getMessage("channel.message.vNotEmpty", "Must not be Empty"));
    		}
	   		if (description.getValue().toString() ==  ""){
	   			description.setInvalid(true);
	   			validDesc.setInvalid(true);
	   			validDesc.setText(app.getMessage("channel.message.vNotEmpty", "Must not be Empty"));
    		}
			
			
    	}
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	if(buttonName != null && cancel.getAbsoluteName().equals(buttonName)) {
            forward = new Forward(FORWARD_CANCEL);
        }
    	return forward;
    }
    
	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public TextField getAutocountfor() {
		return autocountfor;
	}

	public void setAutocountfor(TextField autocountfor) {
		this.autocountfor = autocountfor;
	}

	public CheckBox getAutocount() {
		return autocount;
	}

	public void setAutocount(CheckBox autocount) {
		this.autocount = autocount;
	}

	public int getIntCountFor() {
		return intCountFor;
	}

	public void setIntCountFor(int intCountFor) {
		this.intCountFor = intCountFor;
	}

	public String getModuleSelected() {
		return moduleSelected;
	}

	public void setModuleSelected(String moduleSelected) {
		this.moduleSelected = moduleSelected;
	}
	
}
