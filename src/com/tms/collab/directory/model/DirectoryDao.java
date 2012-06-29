package com.tms.collab.directory.model;

public class DirectoryDao extends AddressBookDao {

    public String getTablePrefix() {
        return "dir_biz";
    }

}
