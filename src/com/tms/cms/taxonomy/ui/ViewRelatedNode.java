package com.tms.cms.taxonomy.ui;

import java.util.ArrayList;
import java.util.Collection;

import kacang.Application;
import kacang.stdui.Form;

import com.tms.cms.taxonomy.model.TaxonomyModule;
import com.tms.cms.taxonomy.model.TaxonomyNode;

public class ViewRelatedNode extends Form {
    private String taxonomyId;
    private TaxonomyNode[] taxonomyNodes;
    private Collection[] nodeList;

    public void init(){
        //taxonomyId="";
    }

    public String getDefaultTemplate() {
        return "taxonomy/viewRelatedNode";
    }

    public void initRelatedNode() {
        taxonomyNodes = null;
        nodeList = null;
        int iTaxonomyId=0;
        //if (taxonomyId!=null && !taxonomyId.equals("")) {
        //    iTaxonomyId = Integer.parseInt(taxonomyId);
        //}

        TaxonomyModule mod = (TaxonomyModule)Application.getInstance().getModule(TaxonomyModule.class);
        TaxonomyNode node = mod.getNode(taxonomyId);

        if (node!=null) {
            taxonomyNodes = mod.getNodesByName(node.getName(),taxonomyId,1);

            if (taxonomyNodes!=null && taxonomyNodes.length>0) {
                nodeList = new Collection[taxonomyNodes.length];

                for (int i=0;i<taxonomyNodes.length;i++) {
                    //Collection col = new ArrayList();
                    nodeList[i] = new ArrayList();

                    if (!taxonomyNodes[i].getParentId().equals("0")) {
                        TaxonomyNode parentNode1 = mod.getNode(taxonomyNodes[i].getParentId());
                        if (!parentNode1.getParentId().equals("0")) {
                            TaxonomyNode parentNode2 = mod.getNode(parentNode1.getParentId());
                            nodeList[i].add(parentNode2);
                        }
                        nodeList[i].add(parentNode1);
                    }
                    nodeList[i].add(taxonomyNodes[i]);

                }
            }
        }

    }

    public void setTaxonomyId(String taxonomyId) {
        this.taxonomyId = taxonomyId;
        initRelatedNode();
    }

    public String getTaxonomyId() {
        return taxonomyId;
    }

    public void setTaxonomyNodes(TaxonomyNode[] taxonomyNodes) {
        this.taxonomyNodes=taxonomyNodes;
    }

    public TaxonomyNode[] getTaxonomyNodes() {
        return taxonomyNodes;
    }

    public Collection[] getNodeList() {
        return nodeList;
    }
}

