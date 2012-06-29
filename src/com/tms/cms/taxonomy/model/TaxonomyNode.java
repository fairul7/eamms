/*
 * TaxonomyNode.java
 *
 * Created on March 7, 2006, 3:09 PM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package com.tms.cms.taxonomy.model;

import kacang.model.DefaultDataObject;
import java.util.Collection;

/**
 *
 * @author oilai
 */
public class TaxonomyNode extends DefaultDataObject {
    
    private String taxonomyId;
    private String taxonomyName;
    private String parentId;
    private String description;
    private String nodeSynonym;
    private String[] synonymList;
    private int nodeOrder;
    private int shown;
    private boolean parent;
    
    private Collection childNodes;
    
    
    /** Creates a new instance of TaxonomyNode */
    public TaxonomyNode() {
    }
    
    public void setTaxonomyId(String taxonomyId) {
        this.taxonomyId = taxonomyId;
    }
    
    public String getTaxonomyId() {
        return this.taxonomyId;
    }
    
    public void setTaxonomyName(String taxonomyName) {
        this.taxonomyName=taxonomyName;
    }
    
    public String getTaxonomyName() {
        return this.taxonomyName;
    }
    
    public void setParentId(String parentId) {
        this.parentId=parentId;
    }
    
    public String getParentId() {
        return this.parentId;
    }
    
    public void setDescription(String description) {
        this.description=description;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public void setNodeSynonym(String synonym) {
        this.nodeSynonym=synonym;
    }
    
    public String getNodeSynonym() {
        return this.nodeSynonym;
    }
    
    public void setSynonymList(String[] synonymList) {
        this.synonymList=synonymList;
    }
    
    public String[] getSynonymList() {
        return this.synonymList;
    }
    
    public void setNodeOrder(int order) {
        this.nodeOrder = order;
    }
    
    public int getNodeOrder() {
        return this.nodeOrder;
    }

    public int getShown() {
        return shown;
    }

    public void setShown(int shown) {
        this.shown = shown;
    }

    public boolean isParent() {
        return parent;
    }

    public void setParent(boolean parent) {
        this.parent = parent;
    }

    public Collection getChildNodes() {
        return childNodes;
    }

    public void setChildNodes(Collection childNodes) {
        this.childNodes = childNodes;
    }
    
    
}
