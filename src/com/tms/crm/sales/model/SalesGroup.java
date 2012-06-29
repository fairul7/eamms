/*
 * Created on Apr 23, 2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.tms.crm.sales.model;

import kacang.model.*;

import java.util.Collection;
import java.util.Set;

/**
 * @author Paul Pak
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class SalesGroup extends DefaultDataObject {
	private String id;
    private String name;
    private String description;
    private Set memberIdSet;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGroupName(){
        return name;
    }

    public Set getMemberIdSet() {
        return memberIdSet;
    }

    public void setMemberIdSet(Set memberIdSet) {
        this.memberIdSet = memberIdSet;
    }


}