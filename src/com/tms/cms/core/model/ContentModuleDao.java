package com.tms.cms.core.model;

import kacang.model.DaoException;
import kacang.model.DataObjectNotFoundException;

import java.util.Collection;

public interface ContentModuleDao {

    public int insert(ContentObject contentObject) throws InvalidKeyException, DaoException;

    public int processNew(ContentObject contentObject) throws DaoException;

    public int update(ContentObject contentObject) throws DaoException;

    public void delete(String key) throws DaoException;

    public void delete(String key, String version) throws DaoException;

    public ContentObject selectByVersion(String key, String version) throws DataObjectNotFoundException, DaoException;

    public Collection selectAllVersions(String key) throws DaoException;

}
