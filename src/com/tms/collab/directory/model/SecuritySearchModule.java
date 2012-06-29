package com.tms.collab.directory.model;

import kacang.Application;
import kacang.model.DaoQuery;
import kacang.model.DefaultModule;
import kacang.model.operator.DaoOperator;
import kacang.model.operator.OperatorEquals;
import kacang.model.operator.OperatorLike;
import kacang.model.operator.OperatorParenthesis;
import kacang.services.indexing.QueryException;
import kacang.services.indexing.SearchResult;
import kacang.services.indexing.SearchResultItem;
import kacang.services.indexing.SearchableModule;
import kacang.services.security.SecurityException;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import org.apache.commons.collections.SequencedHashMap;

import java.util.*;

public class SecuritySearchModule extends DefaultModule implements SearchableModule {
    public static final String CUSTOM_SEARCH_PROPERTY_MESSAGE = "user";

	public SearchResult search(String q, int start, int rows, String userId) throws QueryException {
        SearchResult sr;
        SearchResultItem item;
        Collection list = new ArrayList();
        Map valueMap;

        SecurityService ss;
        ss = (SecurityService) Application.getInstance().getService(SecurityService.class);
        sr = new SearchResult();
        try {
            list = ss.getUsers(generateDaoProperties(q), start, rows, "username", false);
            for (Iterator iterator = list.iterator(); iterator.hasNext();) {
                User user = (User) iterator.next();

                valueMap = new SequencedHashMap();
                valueMap.put(SearchableModule.SEARCH_PROPERTY_MODULE_CLASS, getClass().getName());
                valueMap.put(SearchableModule.SEARCH_PROPERTY_OBJECT_CLASS, User.class.getName());
                valueMap.put(SearchableModule.SEARCH_PROPERTY_KEY, user.getId());
                valueMap.put(SearchableModule.SEARCH_PROPERTY_TITLE, user.getUsername() + " - " +
                        user.getProperty("firstName") + " " + user.getProperty("lastName"));
                valueMap.put(SearchableModule.SEARCH_PROPERTY_DESCRIPTION, user.getProperty("email1"));
                valueMap.put(CUSTOM_SEARCH_PROPERTY_MESSAGE, user);

                item = new SearchResultItem();
                item.setValueMap(valueMap);
                if (user.getUsername().equalsIgnoreCase(q)) {
                    item.setScore(new Float(100));
                } else if(user.getUsername().startsWith(q)) {
                    item.setScore(new Float(80));
                } else {
                    item.setScore(new Float(50));
                }
                sr.add(item);
            }
            sr.setTotalSize(ss.getUserCount(generateDaoProperties(q)));
            return sr;

        } catch (SecurityException e) {
            throw new QueryException(e.getMessage(), e);
        }
    }

    public SearchResult searchFullText(String s, int i, int i1, String s1) throws QueryException {
        return new SearchResult();
    }

	public SearchResult search(String query, int start, int rows, String userId, Date startDate, Date endDate) throws QueryException
	{
		return search(query, start, rows, userId);
	}

	public SearchResult searchFullText(String query, int start, int rows, String userId, Date startDate, Date endDate) throws QueryException
	{
		return new SearchResult();
	}

	private DaoQuery generateDaoProperties(String q) {
            DaoQuery properties = new DaoQuery();
            OperatorParenthesis op = new OperatorParenthesis(DaoOperator.OPERATOR_AND);
            op.addOperator(new OperatorLike("username", q, null));
            op.addOperator(new OperatorLike("firstName", q, DaoOperator.OPERATOR_OR));
            op.addOperator(new OperatorLike("lastName", q, DaoOperator.OPERATOR_OR));
            op.addOperator(new OperatorLike("email1", q, DaoOperator.OPERATOR_OR));
            properties.addProperty(op);
            properties.addProperty(new OperatorEquals("u.active", "1", DaoOperator.OPERATOR_AND));
            return properties;
        }

	public boolean isSearchSupported() {
        return true;
    }

    public boolean isFullTextSearchSupported() {
        return false;
    }
}
