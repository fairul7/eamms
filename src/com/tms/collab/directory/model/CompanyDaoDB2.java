package com.tms.collab.directory.model;

public class CompanyDaoDB2 extends AddressBookDaoDB2
{
	public String getTablePrefix() {
        return "dir_com";
    }
}
