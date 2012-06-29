package com.tms.elearning.core.model;

import com.tms.cms.core.model.InvalidKeyException;
import kacang.model.DaoException;
import kacang.model.DataObjectNotFoundException;

import java.util.Collection;

public interface LearningContentDao {
    public int insert(LearningContentObject contentObject) throws InvalidKeyException, DaoException;

    public int processNew(LearningContentObject contentObject) throws DaoException;

    public int update(LearningContentObject contentObject) throws DaoException;

    public void delete(String key) throws DaoException;

    public void delete(String key, String version) throws DaoException;

    public LearningContentObject selectByVersion(String key, String version) throws DataObjectNotFoundException, DaoException;

    public Collection selectAllVersions(String key) throws DaoException;

}
