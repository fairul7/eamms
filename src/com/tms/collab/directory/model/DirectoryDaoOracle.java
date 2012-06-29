package com.tms.collab.directory.model;

public class DirectoryDaoOracle extends AddressBookDaoOracle
{
	public String getTablePrefix() {
        return "dir_biz";
    }
}
