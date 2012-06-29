package com.tms.cms.taxonomy.ui;

import java.util.Collection;
import java.util.Iterator;

import kacang.Application;
import kacang.ui.Widget;

import org.apache.commons.collections.SequencedHashMap;

import com.tms.cms.taxonomy.model.TaxonomyModule;
import com.tms.cms.taxonomy.model.TaxonomyNode;

public class ViewNode extends Widget  {

    private Collection nodeList;
    private Collection[] childNodeList;
    private SequencedHashMap title;
    private String selectedNodeId;
	private String selectedNodeTitle;
    private boolean ready;
    private boolean endNode;

    public String getDefaultTemplate() {
        return "taxonomy/viewNode";
    }

    public void init() {
        ready=false;
        endNode=false;
        initForm();
    }

    public void initForm() {
        ready=true;
        endNode=false;
        title = new SequencedHashMap();
        if (selectedNodeId==null||selectedNodeId.equals("")) {
            selectedNodeId="0";
        }

        TaxonomyModule mod = (TaxonomyModule)Application.getInstance().getModule(TaxonomyModule.class);
        try {
            nodeList = mod.getNodesByParent(selectedNodeId,1);
        }
        catch(Exception e) {}

        if (selectedNodeId.equals("0") || (nodeList==null || nodeList.size()==0)) {
            ready=false;
        }
        if (!selectedNodeId.equals("0")&& (nodeList==null || nodeList.size()==0)) {
            endNode=true;
        }
        if (nodeList!=null && nodeList.size()>0) {
            childNodeList = new Collection[nodeList.size()];
            int iCounter=0;
            for (Iterator i=nodeList.iterator();i.hasNext();) {
                TaxonomyNode node = (TaxonomyNode)i.next();
                childNodeList[iCounter] = mod.getNodesByParent(node.getTaxonomyId(),1);
                iCounter++;
            }
        }

        selectedNodeTitle = "";
        generateTitle(selectedNodeId);
		title.put("", "HOME");
    }

    public void generateTitle(String id) {
		//selectedNodeTitle = "";
        
        TaxonomyModule mod = (TaxonomyModule)Application.getInstance().getModule(TaxonomyModule.class);
        TaxonomyNode node = mod.getNode(id);
        if (node!=null) {
            if (node.getTaxonomyId().toString().equals(selectedNodeId))
			{
				title.put("-1", node.getTaxonomyName());
				selectedNodeTitle = node.getTaxonomyName();
			}
            else
				title.put(node.getTaxonomyId(), node.getTaxonomyName());
            if (!node.getParentId().equals("0")) {
                generateTitle(node.getParentId());
            }
        }
		if("".equals(selectedNodeTitle))
			selectedNodeTitle = Application.getInstance().getMessage("taxonomy.label.superparent","Home");
    }

    public Collection getNodeList() {
        return nodeList;
    }

    public void setNodeList(Collection nodeList) {
        this.nodeList = nodeList;
    }

    public Collection[] getChildNodeList() {
        return childNodeList;
    }

    public void setChildNodeList(Collection[] childNodeList) {
        this.childNodeList = childNodeList;
    }

    public String getSelectedNodeId() {
        return selectedNodeId;
    }

    public void setSelectedNodeId(String selectedNodeId) {
        this.selectedNodeId = selectedNodeId;
        initForm();
    }

    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    public boolean isEndNode() {
        return endNode;
    }

    public void setEndNode(boolean endNode) {
        this.endNode = endNode;
    }

    public SequencedHashMap getTitle() {
        return title;
    }

	public String getSelectedNodeTitle()
	{
		return selectedNodeTitle;
	}

	public void setSelectedNodeTitle(String selectedNodeTitle)
	{
		this.selectedNodeTitle = selectedNodeTitle;
	}
}
