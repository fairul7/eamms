package com.tms.collab.directory.model;

public class CompanyDaoOracle extends AddressBookDaoOracle
{
	public String getTablePrefix() {
        return "dir_com";
    }
}
