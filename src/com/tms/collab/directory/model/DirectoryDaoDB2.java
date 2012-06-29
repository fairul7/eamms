package com.tms.collab.directory.model;

public class DirectoryDaoDB2 extends AddressBookDaoDB2
{
	public String getTablePrefix() {
        return "dir_biz";
    }
}
