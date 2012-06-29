/*
 * SelectListObject
 * Date Created: Jun 20, 2005
 * Author: Tien Soon, Law
 * Description: An object class to represent an item in select list
 * Company: TMS Berhad
 */
package com.tms.cms.medialib.model;

import kacang.model.DataSourceDao;


public class SelectListObject extends DataSourceDao {
    private String id = "";
    private String name = "";
    
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
}
