package com.tms.cms.digest.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.collections.SequencedHashMap;

import com.tms.cms.core.model.ContentException;
import com.tms.cms.core.model.ContentManager;
import com.tms.cms.core.model.ContentObject;
import com.tms.cms.digest.model.DigestContentDataObject;
import com.tms.cms.digest.model.DigestException;
import com.tms.cms.digest.model.DigestModule;

import kacang.Application;
import kacang.services.security.User;
import kacang.stdui.Button;
import kacang.stdui.Form;
import kacang.stdui.SortableSelectBox;
import kacang.ui.Event;
import kacang.ui.Forward;


public class ReorderDigestContent extends Form{
	public static final String DEFAULT_TEMPLATE = "digest/reorderDigestContentForm";
	protected String digestId;
	private SortableSelectBox sortableSelectBox;
    private Button submitButton;
    private Button reorderButton;
    private Button cancelButton;
	
	public ReorderDigestContent()
    {
    }

    public ReorderDigestContent(String name)
    {
        super(name);
    }

    public void init()
    {
    	super.init();
        removeChildren();   
        
        // add select box
        sortableSelectBox = new SortableSelectBox("children");
        sortableSelectBox.setRows(10);
        sortableSelectBox.setMultiple(true);
        
        addChild(sortableSelectBox);

        // add buttons
        submitButton = new Button("submitButton");
        submitButton.setText("Submit");
        addChild(submitButton);
        reorderButton = new Button("reorderButton");
        reorderButton.setText("Sort Alphabetically");
        addChild(reorderButton);
        cancelButton = new Button("cancelButton");
        cancelButton.setText("Cancel");
        addChild(cancelButton);
        setMethod("post");
		
    }

    public Forward onSubmit(Event evt)
    {
    	Forward fwd=super.onSubmit(evt);
    	if(cancelButton.getAbsoluteName().equals(findButtonClicked(evt)))
            return new Forward("cancel","content.jsp?digestId="+digestId,true);
    	return fwd;
        
    }

    public String getDefaultTemplate()
    {
        return DEFAULT_TEMPLATE;
    }

    public void onRequest(Event evt)
    {
    	super.onRequest(evt);
    	init();
    	DigestModule cm = (DigestModule)Application.getInstance().getModule(DigestModule.class);
        Collection children=new ArrayList();
		try {
			children = cm.selectDigestContents(digestId,null,null,true,0,-1,false);
		} catch (DigestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        
        Map optionMap = new SequencedHashMap();
        Iterator i=children.iterator();
        
        while(i.hasNext()){
        	DigestContentDataObject tmp = (DigestContentDataObject)i.next();
        	if(!(tmp.getOrdering() == null || Integer.parseInt(tmp.getOrdering()) == -1)){
        		optionMap.put(tmp.getDigestContentId(), tmp.getName());
        	}
        }
        optionMap.put("-Order-", "--- Not Ordered ---");
        i=children.iterator();
        while(i.hasNext()){
        	DigestContentDataObject tmp = (DigestContentDataObject)i.next();
        	if(tmp.getOrdering() == null || Integer.parseInt(tmp.getOrdering()) == -1){
        		optionMap.put(tmp.getDigestContentId(), tmp.getName());
        	}
        }
        
        sortableSelectBox.setOptionMap(optionMap);
		
    }
    
    public Forward onValidate(Event evt)
    {
        Forward forward = null;
        if(submitButton.getAbsoluteName().equals(findButtonClicked(evt))){
        	reorderContentObject();
        	 return new Forward("order","content.jsp?digestId="+digestId,true);
        }else if(reorderButton.getAbsoluteName().equals(findButtonClicked(evt))){
        	reorderContentObjectAlphabetically();
        	return new Forward("order","content.jsp?digestId="+digestId,true);
        }

        return forward;
    }

    protected void reorderContentObject() {
        try {
            // get ordered keys
            List values = (List)sortableSelectBox.getValue();
            List childKeyList = new ArrayList();
            for (Iterator i=values.iterator(); i.hasNext();) {
                String key = (String)i.next();
                if (key.trim().length() > 0) {
                    childKeyList.add(key);
                }
                else {
                    break;
                }
            }
            String[] childKeys = (String[])childKeyList.toArray(new String[0]);

            // save
            User user = getWidgetManager().getUser();
            DigestModule digest = (DigestModule)Application.getInstance().getModule(DigestModule.class);
            digest.reorder(digestId, childKeys, user);

        }
        catch (Exception e) {
            throw new RuntimeException(e.toString());
        }
    }

    protected void reorderContentObjectAlphabetically() {
        try {
        	DigestModule digest = (DigestModule)Application.getInstance().getModule(DigestModule.class);
        	ContentManager cm = (ContentManager)Application.getInstance().getModule(ContentManager.class);
            // get children
            User user = getWidgetManager().getUser();
            Collection children = digest.selectDigestContents(digestId,null,"name",true,0,-1,false);
            digest.reorderAlphabetically(digestId, children, user);
        }
        catch (Exception e) {
            throw new RuntimeException(e.toString());
        }
    }
    
	public Button getCancelButton() {
		return cancelButton;
	}

	public void setCancelButton(Button cancelButton) {
		this.cancelButton = cancelButton;
	}

	public Button getReorderButton() {
		return reorderButton;
	}

	public void setReorderButton(Button reorderButton) {
		this.reorderButton = reorderButton;
	}

	public SortableSelectBox getSortableSelectBox() {
		return sortableSelectBox;
	}

	public void setSortableSelectBox(SortableSelectBox sortableSelectBox) {
		this.sortableSelectBox = sortableSelectBox;
	}

	public Button getSubmitButton() {
		return submitButton;
	}

	public void setSubmitButton(Button submitButton) {
		this.submitButton = submitButton;
	}

	public String getDigestId() {
		return digestId;
	}

	public void setDigestId(String digestId) {
		this.digestId = digestId;
	}

}
