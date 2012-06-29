package com.tms.collab.directory.model;

public class CompanyDao extends AddressBookDao {

    public String getTablePrefix() {
        return "dir_com";
    }
}
