package com.tms.cms.core.model;

import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import kacang.Application;
import kacang.model.DataObjectNotFoundException;
import kacang.model.DefaultModule;
import kacang.services.indexing.Index;
import kacang.services.indexing.IndexingService;
import kacang.services.indexing.IndexingThread;
import kacang.services.indexing.QueryException;
import kacang.services.indexing.SearchResult;
import kacang.services.indexing.SearchResultItem;
import kacang.services.indexing.SearchableModule;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.util.Log;
import kacang.util.UuidGenerator;

import org.apache.commons.collections.SequencedHashMap;
import org.htmlparser.NodeReader;
import org.htmlparser.Parser;
import org.htmlparser.util.ParserUtils;
import org.htmlparser.visitors.TextExtractingVisitor;

import com.tms.util.FormatUtil;

/**
 * Module handler for accessing published content, i.e. CMS front-end functionality.
 */
public class ContentPublisher extends DefaultModule {

    public static String INDEX_PUBLISHED = "cmsPublishedIndex";
    private Log log = Log.getLog(getClass());
    private Index indexPublished;
    public static final String USE_CASE_ARCHIVE = "Archive";
    public static final String USE_CASE_ARCHIVE_RECUR = "ArchiveRecursively";
    public static final String USE_CASE_UNARCHIVE = "Unarchive";
    public static final String USE_CASE_UNARCHIVE_RECUR = "UnarchiveRecursively";
    
//-- Initialization

    public void init() {

        // init search indices
        try {
            Application application = Application.getInstance();
            IndexingService indexer = (IndexingService)application.getService(IndexingService.class);

            indexPublished = indexer.getIndexWithName(INDEX_PUBLISHED);
            if (indexPublished == null) {
                // make an index
                indexPublished = new Index();
                indexPublished.setIndexId(UuidGenerator.getInstance().getUuid());
                indexPublished.setName(INDEX_PUBLISHED);
                indexPublished.setPath(INDEX_PUBLISHED);

                // store index
                indexer.storeIndex(indexPublished);
            }
        }
        catch(Exception e) {
            log.error("Error initializing search indices: " + e.toString(), e);
        }
    }
    
    public void publish(ContentObject contentObject, User user) throws ContentException {
        try {
            ContentPublisherDao dao = (ContentPublisherDao)getDao();
            dao.insert(contentObject);

            // reindex object
            if (contentObject instanceof DefaultContentObject) {
                contentObject = ((DefaultContentObject)contentObject).getContentObject();
            }
            unindex(contentObject);
            index(contentObject);
        }
        catch (Exception e) {
            log.error(e.toString(), e);
            throw new ContentException(e.toString());
        }
    }

    public void withdraw(ContentObject contentObject, User user) throws ContentException {
        try {
            ContentPublisherDao dao = (ContentPublisherDao)getDao();
            dao.delete(contentObject.getId());

            // unindex object
            unindex(contentObject);
        }
        catch (Exception e) {
            log.error(e.toString(), e);
            throw new ContentException(e.toString());
        }
    }

    public ContentObject view(String key, User user) throws DataObjectNotFoundException, ContentException {
        try {
            // retrieve object
            ContentPublisherDao dao = (ContentPublisherDao)getDao();
            ContentObject object = dao.selectById(key);

            // retrieve module-specific object
            Application application = Application.getInstance();
            Class moduleClass = ContentUtil.getModuleClassFromKey(key);
            ContentModule module = (ContentModule)application.getModule(moduleClass);
            ContentModuleDao contentModuleDao = (ContentModuleDao)module.getDao();
            ContentObject fullObject = contentModuleDao.selectByVersion(object.getId(), object.getVersion());

            // copy values from object to fullObject
            ContentUtil.copyStatusAttributes(fullObject, object);

            return fullObject;
        }
        catch(DataObjectNotFoundException e) {
            throw e;
        }
        catch (Exception e) {
            log.error(e.toString(), e);
            throw new ContentException(e.toString());
        }
    }

    public ContentObject viewPath(String key, String userId) throws DataObjectNotFoundException, ContentException {
        try {
            // retrieve object
            ContentPublisherDao dao = (ContentPublisherDao)getDao();
            ContentObject object = dao.selectById(key);

/*
            // check permission
            if (!ContentUtil.hasPermission(object, userId, ContentManager.USE_CASE_VIEW))
                throw new ContentException("No permission");
*/

            // get path to parent
            ContentObject tmpObject = object;
            while (tmpObject.getParentId() != null && tmpObject.getParentId().trim().length() > 0) {
                ContentObject parent = dao.selectById(tmpObject.getParentId());
                tmpObject.setParent(parent);
                tmpObject = parent;
            }

            return object;
        }
        catch(DataObjectNotFoundException e) {
            throw e;
        }
        catch (Exception e) {
            log.error(e.toString(), e);
            throw new ContentException(e.toString());
        }
    }

    /**
     * Retrieves a tree structure from a particular Content Object.
     * @param key Content ID for the root of the tree.
     * @param classes Array of class names required, null for all classes.
     * @param permission If not null, only content objects whereby users have the appropriate permission are returned.
     * @param userId The ID of the user viewing the tree.
     * @return A DefaultContentObject that represents the root of the content tree.
     * @throws DataObjectNotFoundException
     * @throws ContentException
     */
    public ContentObject viewTree(String key, String[] classes, String permission, String userId) throws DataObjectNotFoundException, ContentException {
        try {
            // get root content object
            ContentPublisherDao dao = (ContentPublisherDao)getDao();
            ContentObject root = dao.selectById(key);

/*
            // check permission
            if (!ContentUtil.hasPermission(root, userId, ContentManager.USE_CASE_VIEW))
                throw new ContentException("No permission");
*/

            // retrieve list of all objects
            Collection objects = viewList(null, classes, null, null, Boolean.FALSE, null, false, 0, -1, permission, userId);
            Map objectMap = new SequencedHashMap();
            for (Iterator j=objects.iterator(); j.hasNext();) {
                ContentObject co = (ContentObject)j.next();
                if (co.getId() != null && co.getId().trim().length() > 0) {
                	objectMap.put(co.getId(), co);
                }
            }
            objectMap.put(root.getId(), root);

            // traverse list and populate tree
            for (Iterator i=objectMap.values().iterator(); i.hasNext();) {
                ContentObject co = (ContentObject)i.next();
                if (co.getParentId() != null) {
                    ContentObject parent = (ContentObject)objectMap.get(co.getParentId());
                    if (parent != null) {
                        if (!parent.containsChild(co))
                            parent.addChild(co);
                        co.setParent(parent);
                    }
                }
            }

            return root;
        }
        catch(DataObjectNotFoundException e) {
            throw e;
        }
        catch (Exception e) {
            log.error(e.toString(), e);
            throw new ContentException(e.toString());
        }
    }

    /**
     * Retrieves a tree structure, with orphan objects being pushed up the tree. Orphan objects will have a property
     * "orphan" set to Boolean.TRUE.
     * @param key Content ID for the root of the tree.
     * @param classes Array of class names required, null for all classes.
     * @param permission If not null, only content objects whereby users have the appropriate permission are returned.
     * @param userId The ID of the user viewing the tree.
     * @return A DefaultContentObject that represents the root of the content tree.
     * @throws DataObjectNotFoundException
     * @throws ContentException
     */
    public ContentObject viewTreeWithOrphans(String key, String[] classes, String permission, String userId) throws DataObjectNotFoundException, ContentException {
        try {
            // get root content object
            ContentPublisherDao dao = (ContentPublisherDao)getDao();
            ContentObject root = dao.selectById(key);

            // retrieve list of all objects
            Collection objects = viewList(null, classes, null, null, Boolean.FALSE, null, false, 0, -1, null, userId);
            Map objectMap = new SequencedHashMap();
            for (Iterator j=objects.iterator(); j.hasNext();) {
                ContentObject co = (ContentObject)j.next();
                if (co.getId() != null && co.getId().trim().length() > 0) {
                	objectMap.put(co.getId(), co);
                }
            }
            objectMap.put(root.getId(), root);
            objects = null;

            // retrieve list of all objects with the valid permission
            Collection validObjects = viewList(null, classes, null, null, Boolean.FALSE, null, false, 0, -1, permission, userId);
            Map validObjectMap = new SequencedHashMap();
            for (Iterator j=validObjects.iterator(); j.hasNext();) {
                ContentObject co = (ContentObject)j.next();
                if (co.getId() != null && co.getId().trim().length() > 0) {
                	validObjectMap.put(co.getId(), co);
                }
            }
            validObjectMap.put(root.getId(), root);
            validObjects = null;

            // traverse list and populate tree
            for (Iterator i=validObjectMap.values().iterator(); i.hasNext();) {
                ContentObject co = (ContentObject)i.next();
                if (co.getParentId() != null) {
                    ContentObject parent = (ContentObject)validObjectMap.get(co.getParentId());
                    if (parent != null) {
                        if (!parent.containsChild(co))
                            parent.addChild(co);
                        co.setParent(parent);
                    }
                    else {
                        boolean found = false;
                        ContentObject tmpParent = co;
                        while (!found) {
                            tmpParent = (ContentObject)objectMap.get(tmpParent.getParentId());
                            if (tmpParent == null) {
                                found = true;
                                break;
                            }
                            else {
                                parent = (ContentObject)validObjectMap.get(tmpParent.getId());
                                if (parent != null) {
                                    if (!parent.containsChild(co))
                                        parent.addChild(co);
                                    co.setParent(parent);
                                    co.setProperty("orphan", Boolean.TRUE);
                                    found = true;
                                }
                            }
                        }
                    }
                }
            }

            return root;
        }
        catch(DataObjectNotFoundException e) {
            throw e;
        }
        catch (Exception e) {
            log.error(e.toString(), e);
            throw new ContentException(e.toString());
        }
    }

    /**
     * Returns a Collection of Content Objects based on criteria that are specified through the
     * method parameters. This method does not return the objects' contents property.
     * For any of the parameters, setting the value to null will ignore that criteria.
     * @param keys An array of content IDs to retrieve specifying the subset to retrieve.
     * @param classes An array of class names denoting the specific content object classes that are required.
     * @param name A search string to filter by in the content name.
     * @param parentId The ID for the parent content object.
     * @param archived Boolean.TRUE for archived content, Boolean.FALSE for those not archived, null for any.
     * @param sort Property to sort by, defaults to sorting by ordering followed by name.
     * @param desc true if sorted in descending order.
     * @param start Starting index of the results to return.
     * @param rows Number of results to return
     * @param permission If not null, only content objects whereby users have the appropriate permission are returned.
     * @param userId The current user requesting for the listing.
     * @return A Collection of DefaultContentObject objects.
     * @throws ContentException
     */
    public Collection viewList(String[] keys, String[] classes, String name, String parentId, Boolean archived, String sort, boolean desc, int start, int rows, String permission, String userId) throws ContentException {
        return viewList(keys, classes, null, null, name, parentId, archived, sort, desc,  start, rows, permission, userId);
    }

    /**
     * Returns a Collection of Content Objects based on criteria that are specified through the
     * method parameters. This method does not return the objects' contents property.
     * For any of the parameters, setting the value to null will ignore that criteria.
     * @param keys An array of content IDs to retrieve specifying the subset to retrieve.
     * @param classes An array of class names denoting the specific content object classes that are required.
     * @param startDate specifies a start date filter (based on published date)
     * @param endDate specifies an end date filter (based on published date)
     * @param name A search string to filter by in the content name.
     * @param parentId The ID for the parent content object.
     * @param archived Boolean.TRUE for archived content, Boolean.FALSE for those not archived, null for any.
     * @param sort Property to sort by, defaults to sorting by ordering followed by name.
     * @param desc true if sorted in descending order.
     * @param start Starting index of the results to return.
     * @param rows Number of results to return
     * @param permission If not null, only content objects whereby users have the appropriate permission are returned.
     * @param userId The current user requesting for the listing.
     * @return A Collection of DefaultContentObject objects.
     * @throws ContentException
     */
    public Collection viewList(String[] keys, String[] classes, Date startDate, Date endDate, String name, String parentId, Boolean archived, String sort, boolean desc, int start, int rows, String permission, String userId) throws ContentException {
        try {
            ContentPublisherDao dao = (ContentPublisherDao)getDao();
            if (permission != null) {
                // retrieve user principals
                SecurityService security = (SecurityService)Application.getInstance().getService(SecurityService.class);
                Collection principalList = security.getUserPrincipalIds(userId);
                String[] principalArray = (String[])principalList.toArray(new String[0]);

                // retrieve list of objects
                Collection results = dao.selectByPermission(principalArray, permission, keys, classes, startDate, endDate, name, parentId, archived, false, sort, desc, start, rows);
                return results;
            }
            else {
                // retrieve list of objects
                Collection results = dao.selectByCriteria(keys, classes, startDate, endDate, name, parentId, archived, false, sort, desc, start, rows);
                return results;
            }
        }
        catch (Exception e) {
            log.error(e.toString(), e);
            throw new ContentException(e.toString());
        }
    }

    /**
     * Returns a Collection of Content Objects based on criteria that are specified through the
     * method parameters. This method does not return the objects' contents property.
     * For any of the parameters, setting the value to null will ignore that criteria.
     * @param keys An array of content IDs to retrieve specifying the subset to retrieve.
     * @param classes An array of class names denoting the specific content object classes that are required.
     * @param name A search string to filter by in the content name.
     * @param parentId The ID for the parent content object.
     * @param archived Boolean.TRUE for archived content, Boolean.FALSE for those not archived, null for any.
     * @param sort Property to sort by, defaults to sorting by ordering followed by name.
     * @param desc true if sorted in descending order.
     * @param start Starting index of the results to return.
     * @param rows Number of results to return
     * @param permission If not null, only content objects whereby users have the appropriate permission are returned.
     * @param userId The current user requesting for the listing.
     * @return A Collection of DefaultContentObject objects.
     * @throws ContentException
     */
    public Collection viewListWithContents(String[] keys, String[] classes, String name, String parentId, Boolean archived, String sort, boolean desc, int start, int rows, String permission, String userId) throws ContentException {
        try {
            ContentPublisherDao dao = (ContentPublisherDao)getDao();
            if (permission != null) {
                // retrieve user principals
                SecurityService security = (SecurityService)Application.getInstance().getService(SecurityService.class);
                Collection principalList = security.getUserPrincipalIds(userId);
                String[] principalArray = (String[])principalList.toArray(new String[0]);

                // retrieve list of objects
                Collection results = dao.selectByPermission(principalArray, permission, keys, classes, name, parentId, archived, true, sort, desc, start, rows);
                return results;
            }
            else {
                // retrieve list of objects
                Collection results = dao.selectByCriteria(keys, classes, name, parentId, archived, true, sort, desc, start, rows);
                return results;
            }
        }
        catch (Exception e) {
            log.error(e.toString(), e);
            throw new ContentException(e.toString());
        }
    }

    /**
     * Returns a Collection of content with full summary and contents based on the Collection of partial objects
     * @param contentList
     * @return
     * @throws ContentException 
     */
    public Collection viewListWithContents(Collection contentList) throws ContentException {
    	Collection results;
        if (contentList != null && contentList.size() > 0) {
            Collection keyList = new ArrayList();
            for (Iterator i=contentList.iterator(); i.hasNext();) {
            	ContentObject co = (ContentObject)i.next();
            	keyList.add(co.getId());
            }
            String[] keys = (String[])keyList.toArray(new String[keyList.size()]);
            results = viewListWithContents(keys, null, null, null, null, null, false, 0, -1, null, null);
        }
        else {
        	results = new ArrayList();
        }    	
        return results;
    }
    
    public int viewCount(String[] keys, String[] classes, String name, String parentId, Boolean archived, String permission, String userId) throws ContentException {
        return viewCount(keys, classes, null, null, name, parentId, archived, permission, userId);
    }
    public int viewCount(String[] keys, String[] classes, Date startDate, Date endDate, String name, String parentId, Boolean archived, String permission, String userId) throws ContentException {
        try {
            ContentPublisherDao dao = (ContentPublisherDao)getDao();
            if (permission != null) {
                // retrieve user principals
                SecurityService security = (SecurityService)Application.getInstance().getService(SecurityService.class);
                Collection principalList = security.getUserPrincipalIds(userId);
                String[] principalArray = (String[])principalList.toArray(new String[0]);

                // retrieve count of objects by permission
                return dao.selectCountByPermission(principalArray, permission, keys, classes, startDate, endDate, name, parentId, archived);
            }
            else {
                // retrieve count of objects
                return dao.selectCountByCriteria(keys, classes, startDate, endDate, name, parentId, archived);
            }
        }
        catch (Exception e) {
            log.error(e.toString(), e);
            throw new ContentException(e.toString());
        }
    }

    public Collection viewRelated(String key, String[] classes, String sort, boolean desc, int start, int rows, User user) throws ContentException {
        try {
            if (key == null || key.trim().length() == 0) {
                return new ArrayList();
            }

            ContentPublisherDao contentPublisherDao = (ContentPublisherDao)getDao();

            // retrieve
            return contentPublisherDao.selectRelated(key, classes, sort, desc, start, rows);
        }
        catch(Exception e) {
            log.error("viewRelated: " + e.toString());
            throw new ContentException(e.getMessage());
        }
    }

    public int viewRelatedCount(String key, String[] classes, User user) throws ContentException {
        try {
            if (key == null || key.trim().length() == 0) {
                return 0;
            }

            ContentPublisherDao contentPublisherDao = (ContentPublisherDao)getDao();

            // retrieve
            return contentPublisherDao.selectRelatedCount(key, classes);
        }
        catch(Exception e) {
            log.error("viewRelatedCount: " + e.toString());
            throw new ContentException(e.getMessage());
        }
    }

    public void index(ContentObject obj) throws ContentException {
        try {
            // wrap content object
            ContentObjectIndexingWrapper wrapper = new ContentObjectIndexingWrapper(obj, false);

            // do index
            Application application = Application.getInstance();
            IndexingService indexer = (IndexingService)application.getService(IndexingService.class);
            indexer.indexDataObject(indexPublished, wrapper, new String[] { "className", "date", "archived","author","parentId","name","fileName" });
        }
        catch(Exception e) {
            throw new ContentException("Unable to index object: " + e.toString());
        }
    }

    public void unindex(ContentObject obj) throws ContentException {
        try {
            Application application = Application.getInstance();
            IndexingService indexer = (IndexingService)application.getService(IndexingService.class);
            indexer.unindexDataObject(indexPublished, obj.getId());
        }
        catch(Exception e) {
            throw new ContentException("Unable to unindex object: " + e.toString());
        }
    }

    public void reindexTree(String key, User user) throws ContentException {
        try {
            ContentObject contentObject = viewTree(key, null, ContentManager.USE_CASE_PUBLISH, user.getId());
            propagateReindex(contentObject);
        }
        catch(Exception e) {
            throw new ContentException("Unable to reindex object: " + e.toString());
        }
    }

    private void propagateReindex(ContentObject contentObject) throws ContentException {
        try {
        	unindex(contentObject);
            index(contentObject);
            for (Iterator i=contentObject.getChildren().iterator(); i.hasNext();) {
                ContentObject child = (ContentObject)i.next();
                propagateReindex(child);
            }
        }
        catch(Exception e) {
            throw new ContentException("Unable to reindex object: " + e.toString());
        }
    }

    /**
     * Recursive method to archive a ContentObject.
     * @param contentObject
     * @param recursive
     * @param user
     * @throws ContentException
     */
    protected void propagateArchive(ContentObject contentObject, boolean recursive, User user) throws ContentException {
        try {
            // check permission
            if (!contentObject.isArchived()) {
                contentObject.setArchived(true);

                // reindex object
                unindex(contentObject);
                index(contentObject);
            }

            // propagate?
            if (recursive) {
                for (Iterator i = contentObject.getChildren().iterator(); i.hasNext();) {
                    ContentObject tmpObj = (ContentObject) i.next();
                    propagateArchive(tmpObj, true, user);
                }
            }
        }
        catch (Exception e) {
            log.error(e.toString(), e);
            throw new ContentException(e.toString());
        }
    }


    /**
     * Recursive method to unarchive a ContentObject.
     * @param contentObject
     * @param recursive
     * @param user
     * @throws ContentException
     */
    protected void propagateUnarchive(ContentObject contentObject, boolean recursive, User user) throws ContentException {
        try {
            // check permission
            if (contentObject.isArchived()) {
                contentObject.setArchived(false);

                // reindex object
                unindex(contentObject);
                index(contentObject);
            }

            // propagate?
            if (recursive) {
                for (Iterator i = contentObject.getChildren().iterator(); i.hasNext();) {
                    ContentObject tmpObj = (ContentObject) i.next();
                    propagateUnarchive(tmpObj, true, user);
                }
            }
        }
        catch (Exception e) {
            log.error(e.toString(), e);
            throw new ContentException(e.toString());
        }
    }
    
    public Collection search(String query, String sort, int start, int rows, User user) throws ContentException {
        try {
            Collection results = new ArrayList();

            // get search results
            Application application = Application.getInstance();
            IndexingService indexer = (IndexingService)application.getService(IndexingService.class);
            String sortField = ("date".equals(sort)) ? "date" : null;
            SearchResult rs = indexer.searchIndex(indexPublished, query, null, sortField, start, rows);

            for (int i=0; i<rs.size() && (rows < 0 || results.size()<rows); i++) {
                // set id
                String key = rs.get(i, IndexingService.FIELD_KEY);
                ContentObject co = new DefaultContentObject();
                co.setId(key);
                
                // set score
                String score = new Float(rs.getScore(i)).toString();
                co.setContents(score);

                // set archived flag
                boolean archived = Boolean.valueOf(rs.get(i, "archived")).booleanValue();
                co.setArchived(archived);

                // set name
                String name = rs.get(i, IndexingService.FIELD_NAME);
                co.setName(name);

                // set class name
                String className = rs.get(i, "className");
                co.setClassName(className);

                // set summary
                String summary = ContentUtil.formatText(rs.get(i, IndexingService.FIELD_SUMMARY), query);
                co.setSummary(summary);

                // set hilite
                co.setProperty("hilite", rs.get(i, "hilite"));
                
                // set date
                try {
                    Date date = new SimpleDateFormat(IndexingThread.DATE_FORMAT).parse(rs.get(i, "date"));
                    co.setDate(date);
                }
                catch(Exception e) {
                    log.warn("Invalid search result date: " + e.toString());
                }

                // add result
                results.add(co);
            }
            
            return results;
        }
        catch(Exception e) {
            log.error(e.toString(), e);
            throw new ContentException(e.toString());
        }

    }

    public int searchCount(String query, User user) throws ContentException {
        try {
            // get search results
            Application application = Application.getInstance();
            IndexingService indexer = (IndexingService)application.getService(IndexingService.class);
            int count = indexer.searchIndexCount(indexPublished, query, null);
            return count;
        }
        catch(Exception e) {
            log.error(e.toString(), e);
            throw new ContentException(e.toString());
        }

    }

//-- Implementation for SearchableModule

    protected String[] getSearchClasses() {
        return new String[] {
            "com.tms.cms.section.Section",
            "com.tms.cms.article.Article",
            "com.tms.cms.page.Page",
            "com.tms.cms.document.Document",
            "com.tms.cms.image.Image",
            };
    }

    public SearchResult search(String query, int start, int rows, String userId) throws QueryException {
        SearchResult sr = new SearchResult();
        try {
            Collection contentList = viewList(null, getSearchClasses(), query, null, Boolean.FALSE, "date", true, start, rows, ContentManager.USE_CASE_VIEW, userId);
            // retrieve with content
            contentList = viewListWithContents(contentList);
            for (Iterator i=contentList.iterator(); i.hasNext();) {
                ContentObject c = (ContentObject)i.next();
                SearchResultItem item = new SearchResultItem();
                String title = formatSearchTitle(c);
                String body = formatSearchSummary(c.getSummary());
                Map valueMap = new HashMap();
                valueMap.put(SearchableModule.SEARCH_PROPERTY_KEY, c.getId());
                valueMap.put(SearchableModule.SEARCH_PROPERTY_MODULE_CLASS, getClass().getName());
                valueMap.put(SearchableModule.SEARCH_PROPERTY_TITLE, title);
                valueMap.put(SearchableModule.SEARCH_PROPERTY_DESCRIPTION, body);
                item.setValueMap(valueMap);
                sr.add(item);
            }
            // retrieve count
            int count = viewCount(null, getSearchClasses(), query, null, Boolean.FALSE, ContentManager.USE_CASE_VIEW, userId);
            sr.setTotalSize(count);
            return sr;
        }
        catch (Exception e) {
            log.error("Error retrieving search results: " + e.toString(), e);
            throw new QueryException("Error retrieving search results: " + e.toString());
        }
    }

    public SearchResult searchFullText(String query, int start, int rows, String userId) throws QueryException {
        SearchResult sr = new SearchResult();
        try {
            Application application = Application.getInstance();
            SecurityService sec = (SecurityService)application.getService(SecurityService.class);
            User user = sec.getUser(userId);

            // form query
            String[] cs = getSearchClasses();
            if (cs != null) {
                StringBuffer qb = new StringBuffer();
                qb.append("(" + query + ") AND (");
                for (int i=0; i<cs.length; i++) {
                    if (i > 0) {
                        qb.append(" OR ");
                    }
                    qb.append("className:" + cs[i]);
                }
                qb.append(")");
                query = qb.toString();
            }

            // perform search
            Collection contentList = search(query, null, start, rows, user);
            for (Iterator i=contentList.iterator(); i.hasNext();) {
                ContentObject c = (ContentObject)i.next();
                SearchResultItem item = new SearchResultItem();
                String title = formatSearchTitle(c);
                String body = (String)c.getProperty("hilite");
                if (body == null) {
                	body = formatSearchSummary(c.getSummary());
                }
                Map valueMap = new HashMap();
                valueMap.put(SearchableModule.SEARCH_PROPERTY_KEY, c.getId());
                valueMap.put(SearchableModule.SEARCH_PROPERTY_MODULE_CLASS, getClass().getName());
                valueMap.put(SearchableModule.SEARCH_PROPERTY_TITLE, title);
                valueMap.put(SearchableModule.SEARCH_PROPERTY_DESCRIPTION, body);
                item.setValueMap(valueMap);
                sr.add(item);
            }
            int count = searchCount(query, user);
            sr.setTotalSize(count);
            return sr;
        }
        catch (Exception e) {
            log.error("Error retrieving search results: " + e.toString(), e);
            throw new QueryException("Error retrieving search results: " + e.toString());
        }
    }

    public boolean isSearchSupported() {
        return true;
    }

    public boolean isFullTextSearchSupported() {
        return true;
    }

    public static String formatSearchSummary(String s) throws ContentException {
        try {
            if (s == null) {
                return null;
            }
            Parser parser = new Parser(new NodeReader(new StringReader(s), s.length()));
            TextExtractingVisitor visitor = new TextExtractingVisitor();
            parser.visitAllNodesWith(visitor);
            String cleanText = ParserUtils.removeEscapeCharacters(visitor.getExtractedText());
            if (cleanText != null && cleanText.length() > 200) {
                cleanText = cleanText.substring(0, 200) + "...";
            }
            return cleanText;
        } catch (Exception e) {
            Log.getLog(ContentPublisher.class).debug("Unable to filter string: " + e.getMessage());
            return "";
        }
    }

    protected static SimpleDateFormat sdf = FormatUtil.getInstance().getDateFormat(FormatUtil.LONG_DATE);

    public static String formatSearchTitle(ContentObject c) {
         return c.getName() + " - " + sdf.format(c.getDate());
    }

}
