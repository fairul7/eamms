package com.tms.cms.taxonomy.ui;

import java.util.ArrayList;
import java.util.Collection;

import kacang.Application;
import kacang.services.security.User;
import kacang.stdui.Button;
import kacang.stdui.CheckBox;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.ui.Widget;
import kacang.util.Log;

import org.apache.commons.collections.SequencedHashMap;

import com.tms.cms.core.ui.ContentHelper;
import com.tms.cms.taxonomy.model.TaxonomyMap;
import com.tms.cms.taxonomy.model.TaxonomyModule;
import com.tms.cms.taxonomy.model.TaxonomyNode;

/**
 * 
 * Widget for taxonomy Advance search
 *
 */
public class TaxonomySearch extends Widget{
	
	public static final String ADDNODE = "addNode";
	public static final String CLEARNODE = "clearNode";
	private int RESULT_PER_PAGE=10;
	
	private TaxonomyCheckboxTree mapping;
	
	private Collection selectedNodes;
	private Collection results;
	private Collection pageMap;
	
	private boolean selectNoteAction = false;
	
	private Button addNode;
	private Button clearAll;
	
	private SequencedHashMap titles;
	
	private int iPage;
	private String page;
	private int totalContents;
	
	private ArrayList idStringArr;
	
	public void init(){
		mapping = new TaxonomyCheckboxTree("mapping");
		mapping.init();
		mapping.setHidden(true);
		mapping.addEventListener(this);
		
		addNode = new Button("addNode", Application.getInstance().getMessage("taxonomy.label.addNode","Add Node"));
		addNode.addEventListener(this);
		
		clearAll = new Button("clearAll", Application.getInstance().getMessage("taxonomy.label.clearNode","Clear Node"));
		
		addChild(addNode);
		addChild(clearAll);
		
		addChild(mapping);
		
		String resultPerPage = Application.getInstance().getProperty("TaxonomyMappedResultPerPage");
        if (resultPerPage!=null && !resultPerPage.equals("")) {
            try {
                RESULT_PER_PAGE = Integer.parseInt(resultPerPage);
            }
            catch(Exception e) { }
        }
	}
	
	public void onRequest(Event evt){
		init();
		mapping.setHidden(false);
			selectNoteAction = true;
		iPage=0;
        if (page!=null && !page.equals("")) {
            try {
                iPage = Integer.parseInt(page);
            }
            catch(Exception e) {

            }
        }
        else {
            page="1";
        }
        
        if(idStringArr != null && idStringArr.size() > 0){
        	selectNoteAction=false;
        	mapping.setHidden(true);
        	getContents();
        }
        else {
        	//selectNoteAction=true;
        	//mapping.init();
   		 	mapping.setHidden(false);
   		 	selectNoteAction = true;
        }
	}
	
	public Forward actionPerformed(Event evt){
		
		Widget widget = evt.getWidget();
		try {
			//when user click on the file to link to content.jsp only occur in cmsadmin or default template
			if(evt.getType() != null && evt.getType().equals("link")){
				String id = evt.getRequest().getParameter("id");
		        ContentHelper.setId(evt, id);
		        Forward forward = new Forward("selection");
		        return forward;
			}
			
            if (widget.getName().equals(mapping.getName())) {
            	String buttonClicked = mapping.findButtonClicked(evt);
            	if (buttonClicked.equalsIgnoreCase(mapping.getBtnSubmit().getAbsoluteName())) {
            		
            		CheckBox[] mappingCheckBox = mapping.getCbNodes();
            		selectedNodes = new ArrayList();
            		
            		
            		totalContents = 0;
            		
            		idStringArr = new ArrayList();
            		
            		int counter = 0;
            		for (int i=0; i<mappingCheckBox.length;i++) {
                         if (mappingCheckBox[i].isChecked()) {
                             String sId = (String)mappingCheckBox[i].getValue();
                             titles = new SequencedHashMap();
                             
                             //get the path of each selected node
                             generateTitle(sId, sId);
                             selectedNodes.add(titles);
                             
                             idStringArr.add(sId);
                             
                             counter++;
                             
                         }
                    }
                    
            		getContents();
            		
            		mapping.setHidden(true);
            		selectNoteAction = false;
            	}else{
            		mapping.setHidden(true);
            		selectNoteAction = false;
            	}
            }
            else{            	
            	 if (ADDNODE.equals(evt.getType())){
            		 mapping.init();
            		 mapping.setHidden(false);
            		 selectNoteAction = true;
            	 }else if(CLEARNODE.equals(evt.getType())){
            		 
            	 }
            }
        }
        catch (Exception e) {
        	Log.getLog(getClass()).error(e.getMessage(), e);
        }
        return new Forward();
		
	}
	
	public void getContents(){
		
		TaxonomyMap[] mapResult = null;
		results = new ArrayList();
        
        User user = getWidgetManager().getUser();
        TaxonomyModule mod = (TaxonomyModule)Application.getInstance().getModule(TaxonomyModule.class);
        
        mapResult = mod.getMappingForPublic(idStringArr,user,1,(iPage-1)*RESULT_PER_PAGE,RESULT_PER_PAGE);
        
        results.add(mapResult);
        
        totalContents = mod.getMappingTotalForPublic(idStringArr, 1);
        generatePage();
	}
	
	public void generatePage(){
		int iTotalRecord = totalContents;
        pageMap = new ArrayList();
        if (iTotalRecord>RESULT_PER_PAGE) {
            int totalPageCount = iTotalRecord/RESULT_PER_PAGE;
            int pageLeft = iTotalRecord%RESULT_PER_PAGE;
            if (pageLeft>0) {
                totalPageCount++;
            }
            for(int i=0;i<totalPageCount;i++) {
                pageMap.add(""+(i+1));
            }
        }
	}
	
	public void generateTitle(String id, String mainId) {
		//selectedNodeTitle = "";
        TaxonomyModule mod = (TaxonomyModule)Application.getInstance().getModule(TaxonomyModule.class);
        TaxonomyNode node = mod.getNode(id);
        
        if (node!=null) {
            
            if (node.getTaxonomyId().toString().equals(mainId))
			{
            	titles.put("-1", node.getTaxonomyName());
			}
            else
            	titles.put(node.getTaxonomyId().toString(), node.getTaxonomyName());
            
            if (!node.getParentId().equals("0")) {
                generateTitle("" + node.getParentId(), mainId);
            }
        }
    }
	
	
	
	public String getDefaultTemplate(){
		return "taxonomy/search";
	}
	
	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
		//getContents();
	}

	public TaxonomyCheckboxTree getMapping() {
		return mapping;
	}

	public void setMapping(TaxonomyCheckboxTree mapping) {
		this.mapping = mapping;
	}
	
	
	public Collection getPageMap() {
		return pageMap;
	}

	public void setPageMap(Collection pageMap) {
		this.pageMap = pageMap;
	}

	public Collection getResults() {
		return results;
	}

	public void setResults(Collection results) {
		this.results = results;
	}

	public Collection getSelectedNodes() {
		return selectedNodes;
	}

	public void setSelectedNodes(Collection selectedNodes) {
		this.selectedNodes = selectedNodes;
	}

	public boolean isSelectNoteAction() {
		return selectNoteAction;
	}

	public void setSelectNoteAction(boolean selectNoteAction) {
		this.selectNoteAction = selectNoteAction;
	}

	public Button getAddNode() {
		return addNode;
	}

	public void setAddNode(Button addNode) {
		this.addNode = addNode;
	}

	public Button getClearAll() {
		return clearAll;
	}

	public void setClearAll(Button clearAll) {
		this.clearAll = clearAll;
	}
	
	
	
	
}

