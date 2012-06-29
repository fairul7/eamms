package com.tms.cms.medialib.model;

import kacang.model.DaoException;

public class AlbumDaoMsSql extends AlbumDao{
	
	public void init() throws DaoException {
        super.update("CREATE TABLE cml_album (" +
        		"id VARCHAR(35) NOT NULL, " +
        		"name VARCHAR(255), " +
        		"description TEXT, " +
        		"eventDate DATETIME DEFAULT '0000-00-00 00:00:00', " +
        		"featured CHAR(1) DEFAULT '0', " +
        		"libraryId VARCHAR(35), " +
        		"dateCreated DATETIME DEFAULT '0000-00-00 00:00:00', " +
        		"lastUpdatedDate DATETIME DEFAULT '0000-00-00 00:00:00', " +
        		"createdBy VARCHAR(255), " +
        		"PRIMARY KEY(id))", null);
    }
	
	public void update(AlbumObject album) throws DaoException {
        super.update("UPDATE cml_album SET " +
        		"name=#name#, description=#description#, eventDate=#eventDate#, featured=#featured#, libraryId=#libraryId#, lastUpdatedDate=getDate() " +
        		"WHERE id=#id#", album);
    }
	
	public void insert(AlbumObject album) throws DaoException {
        /*
        String id = UuidGenerator.getInstance().getUuid();
        album.setId(id);
        */
        super.update("INSERT INTO cml_album (" +
            		"id, name, description, eventDate, featured, libraryId, dateCreated, lastUpdatedDate, createdBy) VALUES " +
            		"(#id#, #name#, #description#, #eventDate#, #featured#, #libraryId#, getDate(), getDate(), #createdBy#)", album);
    }
	
}
