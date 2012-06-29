package com.tms.elearning.core.model;

import kacang.model.DaoException;
import kacang.model.DataObjectNotFoundException;
import kacang.model.DefaultModule;
import kacang.util.Log;
import kacang.util.UuidGenerator;
import kacang.services.indexing.SearchableModule;
import kacang.services.indexing.SearchResult;
import kacang.services.indexing.QueryException;
import kacang.services.indexing.SearchResultItem;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.util.*;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.SequencedHashMap;

/**
 * Generic address book module to handle contacts.
 * Represents a personal address book so no security restrictions are enforced.
 */
public class LearningModule extends DefaultModule {


     public Collection getCourses(String cid) {
        LearningDao dao = (LearningDao)getDao();
        try {
            Collection results = dao.getCoursesQuery(cid);
            return results;
        } catch (DaoException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Collection getModules(String courseId) {
        LearningDao dao = (LearningDao)getDao();
        try {
            Collection results = dao.getModules(courseId);
            return results;
        } catch (DaoException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Collection getLessons(String moduleId) {
        LearningDao dao = (LearningDao)getDao();
        try {
            Collection results = dao.getLessons(moduleId);
            return results;
        } catch (DaoException e) {
            e.printStackTrace();
            return null;
        }
    }
    public Collection getExams(String lessonId) {
            LearningDao dao = (LearningDao)getDao();
            try {
                Collection results = dao.getAssessments(lessonId);
                return results;
            } catch (DaoException e) {
                e.printStackTrace();
                return null;
            }
    }

}
