package com.tms.collab.rss.ui;

import java.util.ArrayList;
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

public class ChannelAddForm extends Form{
    public static final String FORWARD_SUCCESS = "success";
    public static final String FORWARD_ERROR = "error";
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
	protected Button cancel;
    protected Validator validTitle; 
    //protected Validator validModule;
    protected Validator validLink;
    protected Validator validCategory;
    protected Validator validDesc;
    protected ValidatorIsNumeric validAutoCountFor;
	
	private String strTitle;
	private String strLink;
	private String strDescription;
	private String strAutoCountFor;
	private String moduleid;
	
	//private int intCountFor;
	//private Channel channel;
    
	public void init(){
		setColumns(2);
		Application app = Application.getInstance();

		SetupModule setupModel = (SetupModule) app.getModule(SetupModule.class);
		String siteURL = "";
		try {
			siteURL = setupModel.get("siteUrl");
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		//System.out.println("siteURL="+siteURL);
		
		Label label1 = new Label("TextTitle", "<b>"+app.getMessage("rss.channel.manTitle", "Title*")+"</b>");
		label1.setAlign("right");
		addChild(label1);
		title = new TextField("title");
		validTitle = new ValidatorNotEmpty("ValidTitle", app.getMessage("channel.message.vNotEmpty", "Must not be Empty"));
		title.addChild(validTitle);
		addChild(title);

		Label label2 = new Label("TextLink", "<b>"+app.getMessage("rss.channel.link", "Link")+"</b>");
		label2.setAlign("right");
		addChild(label2);
		
		Label labelLink = new Label(siteURL+"/",siteURL+"/");	
		link = new TextField("link");
		validLink = new ValidatorNotEmpty("ValidLink", app.getMessage("channel.message.vNotEmpty", "Must not be Empty"));
		link.addChild(validLink);
		
		Panel panelLink = new Panel("PanelLink");
		panelLink.setColumns(2);
		panelLink.addChild(labelLink);
		panelLink.addChild(link);
		addChild(panelLink);
		
		Label label3 = new Label("TextDesc", "<b>"+app.getMessage("rss.channel.description", "Description")+"</b>");
		label3.setAlign("right");
		addChild(label3);
		description = new TextBox("description");
		validDesc = new ValidatorNotEmpty("ValidDesc", app.getMessage("channel.message.vNotEmpty", "Must not be Empty"));
		description.addChild(validDesc);
		addChild(description);
		
		Label label4 = new Label("selectModule", "<b>"+app.getMessage("rss.channel.manModule", "Module*")+"</b>");
		label4.setAlign("right");
		addChild(label4);
		module = new SelectBox("module");
		module.setMultiple(false);
		module.addOption("-1", "Select A Module");
		
		RssHandler handler = (RssHandler)Application.getInstance().getModule(RssHandler.class);
        Collection moduleList = handler.getModules();
		Object t = new Object();
        
        try
        {
        	for(Iterator i = moduleList.iterator(); i.hasNext(); ){
            	t = i.next();
            	t = Application.getInstance().getModule((Class)t);
        		
            	if(t instanceof RssAble) {
            		RssAble rssable = (RssAble)Application.getInstance().getModule(t.getClass());
            		module.addOption(String.valueOf(t).substring(0, String.valueOf(t).indexOf("@")), rssable.getModuleName());
            	}
            }
        }
        catch(Exception e) {
        	System.out.println(e);
        }
        module.setOnChange("submit()");
		addChild(module);
		
		Label label5 = new Label("selectCategory", "<b>"+app.getMessage("rss.channel.manCategory", "Category*")+"</b>");
		label5.setAlign("right");
		addChild(label5);
		category = new SelectBox("category");
		category.setMultiple(false);
		addChild(category);	

		Label label6 = new Label("AutoCountFor", "<b>"+app.getMessage("rss.channel.autoCountFor", "Auto Count For")+"</b>");
		label6.setAlign("right");
		addChild(label6);
		autocountfor = new TextField("countfor");
		validAutoCountFor = new ValidatorIsNumeric("validAutoCountFor", app.getMessage("channel.message.vNotNumberEmpty", "Must be numeric."));
		autocountfor.addChild(validAutoCountFor);
		autocountfor.setMaxlength("10");
		autocountfor.setSize("5");
		autocountfor.setValue("0");
		autocountfor.setOnBlur("setAutoChecked()");
		addChild(autocountfor);		
		
		addChild(new Label("CheckActive", ""));
		active= new CheckBox("active");
		active.setText(app.getMessage("rss.channel.active", "Active"));
		addChild(active);

		addChild(new Label("CheckAutoCount", ""));
		autocount= new CheckBox("autocount");
		autocount.setChecked(true);
		autocount.setOnClick("disableCountFor()");
		autocount.setText(app.getMessage("rss.channel.autoCount", "Auto Count"));
		addChild(autocount);
		
		//validTitle = new ValidatorNotEmpty("validTitle", "Title Not allow to empty");
		addChild(new Label("Empty",""));

		submit = new Button("submit", app.getMessage("rss.channel.submit", "Submit"));
		reset = new ResetButton("reset");
		reset.setText("Reset");
		cancel = new Button("cancel", app.getMessage("rss.channel.cancel", "Cancel"));
		   
		Panel buttonPanel = new Panel("buttonPanel");
		buttonPanel.setColumns(3);
		buttonPanel.addChild(submit);
		buttonPanel.addChild(reset);
		buttonPanel.addChild(cancel);
		addChild(buttonPanel);
	}
	
	public void onRequest(Event evt) {
		init();
	}
	
    public Forward addChannel() throws RuntimeException
    {
        Application app = Application.getInstance();
    	User currentUser;
    	currentUser = app.getCurrentUser();
       
    	Channel channel = new Channel();
        channel.setChannelId(UuidGenerator.getInstance().getUuid());
        channel.setTitle((String)title.getValue());
        channel.setDescription((String)description.getValue());
        channel.setLink((String)link.getValue());
        channel.setModuleId((String)module.getSelectedOptions().keySet().iterator().next().toString());
        channel.setCategoryId((String)category.getSelectedOptions().keySet().iterator().next().toString());
        if(active.isChecked())
        	channel.setActive(true);        
        if(autocount.isChecked())
        	channel.setAutoCount(true);
        channel.setAutoCountFor(Integer.parseInt((String)autocountfor.getValue()));
        channel.setCreateBy(currentUser.getUsername());
        channel.setUpdateBy(currentUser.getUsername());
        Forward forward;

        try {
	        RssHandler handler = (RssHandler)Application.getInstance().getModule(RssHandler.class);
	        RssAble rssable = (RssAble)Application.getInstance().getModule(Class.forName(channel.getModuleId()));
	        Collection collection = rssable.getRssItems(channel.getCategoryId(), channel.getAutoCountFor());
	        String strItem = new String();
	        Item itemObj ;
	        Collection items = new ArrayList();
	        	// insert new channel
	        	handler.addChannel(channel);
		        
	        	// insert new item
	        	for(Iterator i = collection.iterator();i.hasNext();){
	        		strItem = (String)i.next();
		        	itemObj = new Item();
		        	itemObj.setItemId(strItem);
		        	itemObj.setChannelId(channel.getChannelId());
		        	itemObj.setRssItemId(UuidGenerator.getInstance().getUuid());
		        	items.add(itemObj);
		        }
	        	handler.addCollItem(items);
	        	
        		forward = new Forward(FORWARD_SUCCESS);        
        } catch (Exception e){
        		System.out.println(e.toString());
            	forward = new Forward(FORWARD_ERROR);
        }
        return forward;
    }

    public Forward onValidate(Event evt) throws RuntimeException
    {
    	Forward forward = null;
        String buttonName = findButtonClicked(evt);
        if(buttonName != null && submit.getAbsoluteName().equals(buttonName)) {
            forward = addChannel();
        }
        return forward;
    }
    
    public Forward onSubmit(Event event) {
    	Forward forward = super.onSubmit(event);
    	String moduleSelected;
    	String buttonName = findButtonClicked(event);
    	
    	if (!submit.getAbsoluteName().equals(buttonName)) {
			moduleSelected = (String) module.getSelectedOptions().keySet().iterator().next();
			strTitle = title.getValue().toString();
			strLink = link.getValue().toString();
			strDescription = description.getValue().toString();
			strAutoCountFor = autocountfor.getValue().toString();

			if (!moduleSelected.equals("-1")){
            	//init();
            	try
            	{
            		RssAble f = (RssAble) Application.getInstance().getModule(Class.forName(moduleSelected));
	        		category.setOptionMap(f.getCategories());
	        		module.setSelectedOption(moduleSelected.toString());
            	}
            	catch(Exception e)
            	{
            		e.toString();
            	}
            } else {
            	//init();
            	Map m = new SequencedHashMap();
            	m.put("", "");
            	category.setOptionMap(m);
            	//System.out.println("22="+moduleSelected.toString());
            	
            }   

			title.setValue(strTitle);
    		link.setValue(strLink);
    		description.setValue(strDescription);
    		autocountfor.setValue(strAutoCountFor);
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


	public Validator getValidTitle() {
		return validTitle;
	}

	public void setValidTitle(Validator validTitle) {
		this.validTitle = validTitle;
	}
}
