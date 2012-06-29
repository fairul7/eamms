package com.tms.elearning.core.model;

import kacang.model.DataSourceDao;
import kacang.services.security.User;

import java.util.Collection;
import java.util.ArrayList;

public class LearningContentManagerDao extends DataSourceDao {
    public Collection selectByCriteria(String name,User user, boolean archived, boolean active, boolean publiz, String sort, boolean desc, int start, int rows) {
        return new ArrayList();
    }
}
