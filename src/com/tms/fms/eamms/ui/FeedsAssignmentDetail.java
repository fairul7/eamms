package com.tms.fms.eamms.ui;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import kacang.Application;
import kacang.services.storage.StorageFile;
import kacang.services.storage.StorageService;
import kacang.stdui.Button;
import kacang.stdui.CheckBox;
import kacang.stdui.Form;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

import com.tms.fms.eamms.model.EammsAssignment;
import com.tms.fms.eamms.model.EammsFeedsModule;
import com.tms.fms.util.WidgetUtil;
import com.tms.fms.widgets.CollapsiblePanel;

public class FeedsAssignmentDetail extends Form 
{
	protected String requestId;
	protected String assignmentId;
	protected Button add;
	protected Button delete;
	protected boolean viewMode;		
	protected boolean removeLink;	
	protected CollapsiblePanel panel;
	protected Collection assignments;
	protected CheckBox[] checkBoxes; 
	
	private String popupAdd;
	private String popupEdit;
	private String popupUpdate;
	
	private boolean networkView;
	private boolean showsNetworkColumns;
	
	public FeedsAssignmentDetail() {}

	public FeedsAssignmentDetail(String s) 
	{
		super(s);
	}
	
	public void init() 
	{
		assignments = new ArrayList();
		viewMode = false;		
		removeLink = false;	
		networkView = false;
		showsNetworkColumns = false;
	}

	public void onRequest(Event evt) 
	{
		popupAdd = "window.open('feedsAssignmentAdd.jsp', 'feedsAssignmentADDOpener', 'resizable=1, scrollbars=yes, statusbar=1, menubar=1, width=600, height=250');";
		popupEdit = "window.open('feedsAssignmentEdit.jsp', 'feedsAssignmentEditOpener', 'resizable=1, scrollbars=yes, statusbar=1, menubar=1, width=600, height=250');";
		popupUpdate = "window.open('feedsAssignmentUpdate_network.jsp', 'feedsAssignmentUpdateOpener', 'resizable=1, scrollbars=yes, statusbar=1, menubar=1, width=700, height=400');";
		
		populateForm();
	}
	
	private void populateForm()
	{
		setMethod("post");
		
		panel = new CollapsiblePanel("collapsiblePanel", "");
		panel.setCollapsed(false);
		panel.setTemplate("");
		addChild(panel);
		
		add = new Button("add",Application.getInstance().getMessage("eamms.feed.list.button.add"));
		add.setOnClick(getAddPopup());
		delete = new Button("delete",Application.getInstance().getMessage("eamms.feed.list.button.delete"));
		delete.setOnClick("javascript:return confirm('"+Application.getInstance().getMessage("eamms.feed.list.msg.confirmDelete")+"');");
		if(viewMode)
		{
			add.setHidden(true);
			delete.setHidden(true);
		}
		addChild(add);
		addChild(delete);
		
		EammsFeedsModule em = (EammsFeedsModule) Application.getInstance().getModule(EammsFeedsModule.class);
		assignments = em.getAssignments(requestId, null);
		if(assignments != null && !assignments.isEmpty())
		{
			checkBoxes = new CheckBox[assignments.size()];
			int i = 0;
			for(Iterator itr = assignments.iterator();itr.hasNext();i++)
			{
				EammsAssignment assignObj = (EammsAssignment)itr.next();
				if(!removeLink) 
				{
					assignObj.setTitle(getEditPageLink(assignObj.getAssignmentId(), assignObj.getFeedsDetailsId(), 
							assignObj.isBlockbooking() ? "1" : "0"));
				}
				else
				{
					assignObj.setTitle(assignObj.getAssignmentId());
				}
				
				String attchLink = addAttchLink(assignObj.getAttachment());
				assignObj.setProperty("attchLink", attchLink);
				
				checkBoxes[i]=new CheckBox("deleteKey"+i);
				checkBoxes[i].setValue(assignObj.getAssignmentId());
				checkBoxes[i].setGroupName("deleteKeyGrp");
				addChild(checkBoxes[i]);
			}
		}
	}
	
	public Forward onValidate(Event event) 
	{
		String buttonName = findButtonClicked(event);
		
		if (buttonName != null && delete.getAbsoluteName().equals(buttonName)) 
		{
			EammsFeedsModule em = (EammsFeedsModule) Application.getInstance().getModule(EammsFeedsModule.class);
			
			Collection col = WidgetUtil.getCheckBoxValue(checkBoxes);
			for(Iterator itr = col.iterator();itr.hasNext();)
			{
				String key =(String)itr.next();
				em.deleteAssignment(key);
			}
			return new Forward("DELETED");
		}
		return super.onValidate(event);
	}
	
	private String getAddPopup()
	{
		EammsFeedsModule em = (EammsFeedsModule) Application.getInstance().getModule(EammsFeedsModule.class);
		String feedsDetailsId = em.getFeedsDetailsIdByRequestId(requestId);
		
		String str = popupAdd;
		str = str.replaceFirst("Add.jsp", "Add.jsp?feedsDetailsId=" + feedsDetailsId);
		return str;
	}
	
	private String getEditPageLink(String id, String feedsDetailsId, String blockbooking)
	{
		String link = "";
		
		if(!networkView)
		{
			link = popupEdit;
			link = link.replaceFirst("Edit.jsp", "Edit.jsp?id=" + id + 
					"&feedsDetailsId=" + feedsDetailsId + "&blockbooking=" + blockbooking);
			link = "<a class='collapsiblePanelBar' onClick=\""+link+"\">" +id+ "</a>";
		}
		else
		{
			link = popupUpdate;
			link = link.replaceFirst("network.jsp", "network.jsp?id=" + id + 
					"&feedsDetailsId=" + feedsDetailsId + "&blockbooking=" + blockbooking);
			link = "<a class='collapsiblePanelBar' onClick=\""+link+"\">" +id+ "</a>";
		}
		
		return link;
	}
	
	private String addAttchLink(String attachment)
	{
		String link = "";
		if(attachment != null && !attachment.equals(""))
		{
			try
			{
				StorageService storageService = (StorageService) Application.getInstance().getService(StorageService.class);
				if(storageService.get(new StorageFile(attachment)) != null)
				{
					StorageFile sf = storageService.get(new StorageFile(attachment));
					link = "<a href='/storage/"+ attachment + "' target='_blank'>" + sf.getName() + "</a>";
				}
			}
			catch (FileNotFoundException e)
			{
				link = "attachment not found ..";
			}
			catch (Exception e) 
			{
				Log.getLog(getClass()).error(e.toString(), e);
				link = "";
			}
		}
		return link;
	}
	
	public String getDefaultTemplate() 
	{
		return "fms/eamms/feedsAssignmentDetailTemplate";
	}
	
	public boolean isViewMode() {
		return viewMode;
	}

	public void setViewMode(boolean viewMode) {
		this.viewMode = viewMode;
	}

	public CheckBox[] getCheckBoxes() {
		return checkBoxes;
	}

	public void setCheckBoxes(CheckBox[] checkBoxes) {
		this.checkBoxes = checkBoxes;
	}

	public boolean isRemoveLink() {
		return removeLink;
	}

	public void setRemoveLink(boolean removeLink) {
		this.removeLink = removeLink;
	}

	public String getAssignmentId()
	{
		return assignmentId;
	}

	public void setAssignmentId(String assignmentId)
	{
		this.assignmentId = assignmentId;
	}

	public String getRequestId()
	{
		return requestId;
	}

	public void setRequestId(String requestId)
	{
		this.requestId = requestId;
	}

	public Collection getAssignments()
	{
		return assignments;
	}

	public void setAssignments(Collection assignments)
	{
		this.assignments = assignments;
	}

	public CollapsiblePanel getPanel()
	{
		return panel;
	}

	public void setPanel(CollapsiblePanel panel)
	{
		this.panel = panel;
	}

	public boolean isNetworkView()
	{
		return networkView;
	}

	public void setNetworkView(boolean networkView)
	{
		this.networkView = networkView;
	}

	public boolean isShowsNetworkColumns()
	{
		return showsNetworkColumns;
	}

	public void setShowsNetworkColumns(boolean showsNetworkColumns)
	{
		this.showsNetworkColumns = showsNetworkColumns;
	}
}
