package com.tms.cms.core.model;

import com.tms.cms.core.jobs.ContentPublishJobTask;
import com.tms.cms.core.jobs.ContentWithdrawJobTask;
import com.tms.cms.core.jobs.ContentSubscriptionTask;
import com.tms.cms.profile.model.ContentProfileModule;
import com.tms.ekms.setup.model.SetupModule;
import com.opensymphony.oscache.base.Cache;
import com.opensymphony.oscache.base.NeedsRefreshException;
import kacang.Application;
import kacang.model.DaoException;
import kacang.model.DataObjectNotFoundException;
import kacang.model.DefaultModule;
import kacang.model.Module;
import kacang.services.indexing.*;
import kacang.services.scheduling.JobSchedule;
import kacang.services.scheduling.JobTask;
import kacang.services.scheduling.JobTaskData;
import kacang.services.scheduling.SchedulingService;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.services.storage.StorageService;
import kacang.util.Log;
import kacang.util.UuidGenerator;
import org.apache.commons.collections.SequencedHashMap;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Module handler for content management, i.e. CMS back-end functionality.
 */
public class ContentManager extends DefaultModule {

    public static final String CONTENT_TREE_ROOT_ID = "com.tms.cms.section.Section_root";

    public static final String PERMISSION_MANAGE_CONTENT = "com.tms.cms.ManageContent";
    public static final String PERMISSION_FRONT_END_EDIT = "com.tms.cms.FrontEndEdit";
    public static final String PERMISSION_ACCESS_RECYCLE_BIN = "com.tms.cms.AccessRecycleBin";
    public static final String PERMISSION_MANAGE_RECYCLE_BIN = "com.tms.cms.ManageRecycleBin";
    public static final String PERMISSION_SUBSCRIBE_CONTENT = "com.tms.cms.SubscribeContent";
    public static final String PERMISSION_MANAGE_SUBSCRIBE_CONTENT = "com.tms.cms.ManageSubscribeContent";
    public static final String PERMISSION_MANAGE_CONTENT_ACL = "com.tms.cms.ManageContentAcl";
    public static final String PERMISSION_MANAGE_PROFILE = "com.tms.cms.ManageProfile";

    public static final String USE_CASE_CREATE = "Create";
    public static final String USE_CASE_CHECKOUT = "CheckOut";
    public static final String USE_CASE_SAVE = "Save";
    public static final String USE_CASE_SUBMIT = "Submit";
    public static final String USE_CASE_APPROVE = "Approve";
    public static final String USE_CASE_REJECT = "Reject";
    public static final String USE_CASE_UNDO_CHECKOUT = "UndoCheckOut";
    public static final String USE_CASE_ROLLBACK = "Rollback";
    public static final String USE_CASE_MOVE = "Move";
    public static final String USE_CASE_PUBLISH = "Publish";
    public static final String USE_CASE_PUBLISH_RECUR = "PublishRecursively";
    public static final String USE_CASE_SCHEDULED_PUBLISH = "ScheduledPublish";
    public static final String USE_CASE_SCHEDULED_PUBLISH_RECUR = "ScheduledPublishRecursively";
    public static final String USE_CASE_WITHDRAW = "Withdraw";
    public static final String USE_CASE_WITHDRAW_RECUR = "WithdrawRecursively";
    public static final String USE_CASE_ARCHIVE = "Archive";
    public static final String USE_CASE_ARCHIVE_RECUR = "ArchiveRecursively";
    public static final String USE_CASE_UNARCHIVE = "Unarchive";
    public static final String USE_CASE_UNARCHIVE_RECUR = "UnarchiveRecursively";
    public static final String USE_CASE_PREVIEW = "Preview";
    public static final String USE_CASE_PREVIEW_VERSION = "PreviewVersion";
    public static final String USE_CASE_REORDER = "Reorder";
    public static final String USE_CASE_HISTORY = "History";
    public static final String USE_CASE_DELETE = "Delete";
    public static final String USE_CASE_DELETE_RECUR = "DeleteRecursively";
    public static final String USE_CASE_UNDELETE = "Undelete";
    public static final String USE_CASE_UNDELETE_RECUR = "UndeleteRecursively";
    public static final String USE_CASE_DESTROY = "Destroy";
    public static final String USE_CASE_AUDIT_VIEW = "AuditView";
    public static final String USE_CASE_AUDIT_CLEAR = "AuditClear";
    public static final String USE_CASE_REPORT_VIEW = "ReportView";
    public static final String USE_CASE_REPORT_CLEAR = "ReportClear";
    public static final String USE_CASE_ACL_VIEW = "AclView";
    public static final String USE_CASE_ACL_UPDATE = "AclUpdate";
    public static final String USE_CASE_RELATED = "Keywords";
    public static final String USE_CASE_VIEW = "View";

    public static String INDEX_CONTENT = "cmsContentIndex";

    public static final String APPLICATION_PROPERTY_SITE_TEMPLATE = "cms.site.template";
    public static final String APPLICATION_PROPERTY_NOTIFICATION_EMAIL = "cms.notification.email";
    public static final String APPLICATION_PROPERTY_CONTENT_SUBSCRIPTION = "cms.content.subscription";
    public static final String APPLICATION_PROPERTY_VERSIONING_DISABLED = "cms.versioning.disabled";
    public static final String APPLICATION_PROPERTY_ADMIN_READER_DISABLED = "cms.admin.reader.disabled";
    public static final String APPLICATION_PROPERTY_AUDITING_DISABLED = "cms.auditing.disabled";
    public static final String APPLICATION_PROPERTY_STATISTICS_DISABLED = "cms.statistics.disabled";

    public static final String SETUP_PROPERTY_SITE_TEMPLATE = "siteTemplate";
    public static final String DEFAULT_HTML_TEMPLATE = "default";
    public static final String DEFAULT_AVANTGO_TEMPLATE = "avantgo";
    public static final String DEFAULT_WAP_TEMPLATE = "wap";

    public static final int CACHE_DURATION_SITE_TEMPLATE = 5; // 5 seconds
    protected Cache siteTemplateCache = new Cache(true, false, true);

    private Log log = Log.getLog(getClass());
    private Index indexContent;
    private Collection contentObjectClassList = new ArrayList();

//-- Initialization

    public void init() {
        // retrieve list of all content modules
        Application application = Application.getInstance();
        Map moduleMap = application.getModuleMap();
        for (Iterator i = moduleMap.values().iterator(); i.hasNext();) {
            Module module = (Module) i.next();
            if (module instanceof ContentModule) {
                ContentModule contentModule = (ContentModule) module;
                Class[] classes = contentModule.getContentObjectClasses();
                if (classes != null) {
                    contentObjectClassList.addAll(Arrays.asList(classes));
                }
            }
        }

        // init search indices
        try {
            IndexingService indexer = (IndexingService) application.getService(IndexingService.class);
            indexContent = indexer.getIndexWithName(INDEX_CONTENT);
            if (indexContent == null) {
                // make an index
                indexContent = new Index();
                indexContent.setIndexId(UuidGenerator.getInstance().getUuid());
                indexContent.setName(INDEX_CONTENT);
                indexContent.setPath(INDEX_CONTENT);

                // store index
                indexer.storeIndex(indexContent);
            }
        }
        catch (Exception e) {
            log.error("Error initializing search indices: " + e.toString(), e);
        }

        if (!isVersioningEnabled()) {
            log.info("ContentManager versioning disabled");
        }
    }


//--- Use Cases

    /**
     * Creates new content.
     * @param contentObject
     * @param user
     * @return
     * @throws com.tms.cms.core.model.InvalidKeyException
     * @throws com.tms.cms.core.model.ContentException
     */
    public ContentObject createNew(ContentObject contentObject, User user) throws InvalidKeyException, ContentException {
        return createNew(contentObject, user, true);
    }

    /**
     * Creates new content.
     * @param contentObject
     * @param user
     * @param checkPermission Set false to ignore permission checking
     * @return
     * @throws com.tms.cms.core.model.InvalidKeyException
     * @throws com.tms.cms.core.model.ContentException
     */
    public ContentObject createNew(ContentObject contentObject, User user, boolean checkPermission) throws InvalidKeyException, ContentException {

        try {
            // check permission
            ContentObject parent = null;
            ContentManagerDao contentManagerDao = (ContentManagerDao) getDao();
            if (contentObject.getParentId() != null) {
                parent = contentManagerDao.selectById(contentObject.getParentId());
                if (checkPermission && !hasPermission(parent, user.getId(), USE_CASE_CREATE))
                    throw new ContentPermissionException("No permission");
            }

            // check for valid parent

            // determine id, generate if necessary
            if (contentObject.getId() != null && contentObject.getId().trim().length() > 0) {
                // use custom id
                // check if already start with ClassName or not
                String key;
                if(contentObject.getId().startsWith(contentObject.getClass().getName())){
                    key = contentObject.getId();
                }else
                    key = ContentUtil.generateId(contentObject.getClass(), contentObject.getId());
                try {
                    // look for duplicate
                    contentManagerDao.selectById(key);
                    throw new InvalidKeyException();
                }
                catch (DataObjectNotFoundException de) {
                    contentObject.setId(key);
                }

            }
            else {
                // create unique id
                String uuid = UuidGenerator.getInstance().getUuid();
                String key = ContentUtil.generateId(contentObject.getClass(), uuid);
                contentObject.setId(key);
            }

            // determine acl id - must not be null.
            if (parent != null) {
                contentObject.setAclId(parent.getAclId());
            }
            else {
                contentObject.setAclId(contentObject.getId());
            }

            // set details
            contentObject.setClassName(contentObject.getClass().getName());
            contentObject.setVersion("1");
            contentObject.setDate(new Date());
            contentObject.setNew(true);
            contentObject.setModified(false);
            contentObject.setDeleted(false);
            contentObject.setArchived(false);
            contentObject.setPublished(false);

            // set checkout status
            contentObject.setCheckedOut(true);
            contentObject.setCheckOutDate(new Date());
            contentObject.setCheckOutUserId(user.getId());
            contentObject.setCheckOutUser(user.getUsername());

            // reset audit details
            contentObject.setSubmitted(false);
            contentObject.setSubmissionDate(null);
            contentObject.setSubmissionUser(null);
            contentObject.setSubmissionUserId(null);
            contentObject.setApproved(false);
            contentObject.setApprovalDate(null);
            contentObject.setApprovalUser(null);
            contentObject.setApprovalUserId(null);
            contentObject.setComments(null);
            
            //testing
            //if contentObject author was filled in, use the filed in by line as the author instead of the login user name
            if(contentObject.getAuthor() == null || contentObject.getAuthor().trim().length() == 0)
            	contentObject.setAuthor(user.getUsername());
            
            // call content module to create new version
            Application application = Application.getInstance();
            ContentModule module = (ContentModule) application.getModule(contentObject.getContentModuleClass());
            ContentModuleDao contentModuleDao = (ContentModuleDao) module.getDao();
            contentModuleDao.insert(contentObject);

            // create record
            contentManagerDao.insertNew(contentObject);

            // additional module-specific processing
            contentModuleDao.processNew(contentObject);

            // index object
            index(contentObject);

            // audit
            audit(contentObject.getId(), USE_CASE_CREATE, null, user);
            
            // Add in for taxonomy
            if (Application.getInstance().getProperty("com.tms.cms.taxonomy")!=null && Application.getInstance().getProperty("com.tms.cms.taxonomy").equals("true")) {
                if (contentObject.getClassName().equals(com.tms.cms.document.Document.class.getName())) {
                    com.tms.cms.document.Document doc = (com.tms.cms.document.Document)contentObject;
                    if (doc.getFilePath()!=null && !doc.getFilePath().equals("")) {
                        StorageService service = (StorageService)Application.getInstance().getService(StorageService.class);
                        com.tms.cms.taxonomy.TaxonomyAutoTagging tag = new com.tms.cms.taxonomy.TaxonomyAutoTagging();
                        tag.setContentId(doc.getId());
                        tag.setUserId(user.getId());
                        tag.setFileName(service.getRootPath()+doc.getFilePath());
                        tag.run();
                    }
                }
            }

            return contentObject;
        }
        catch (InvalidKeyException e) {
            throw e;
        }
        catch (ContentException e) {
            throw e;
        }
        catch (Exception e) {
            log.error(e.toString(), e);
            throw new ContentException(e.toString());
        }
    }

    /**
     * Check out to edit existing content.
     * @param key
     * @param user
     * @return
     * @throws com.tms.cms.core.model.ContentException
     */
    public ContentObject checkOutToEdit(String key, User user) throws ContentException {
        try {
            // retrieve content object
            ContentManagerDao contentManagerDao = (ContentManagerDao) getDao();
            ContentObject contentObject = contentManagerDao.selectById(key);

            // check permission
            if (!hasPermission(contentObject, user.getId(), USE_CASE_CHECKOUT))
                throw new ContentPermissionException("No permission");

            // call content module to retrieve previous version
            Application application = Application.getInstance();
            Class moduleClass = ContentUtil.getModuleClassFromKey(contentObject.getId());
            ContentModule module = (ContentModule) application.getModule(moduleClass);
            ContentModuleDao contentModuleDao = (ContentModuleDao) module.getDao();
            ContentObject fullObject = contentModuleDao.selectByVersion(key, contentObject.getVersion());
            ContentUtil.copyStatusAttributes(fullObject, contentObject);

            // check if already checked out
            if (contentObject.isCheckedOut()) {
                if (!user.getId().equals(contentObject.getCheckOutUserId())) {
                    throw new ContentCheckedOutException("Content " + contentObject.getId() + " already checked out");
                }
                else {
                    return fullObject;
                }
            }
            // check if already submitted
            else if (contentObject.isSubmitted()) {
                throw new ContentSubmittedException("Content " + contentObject.getId() + " already submitted");
            }
            else {
                String username = user.getUsername();

                // generate and set new version id
                String previousVersion = fullObject.getVersion();
                String nextVersionId = ContentUtil.generateNextVersionId(contentObject);
                fullObject.setVersion(nextVersionId);

                // set checkout status
                fullObject.setCheckedOut(true);
                fullObject.setCheckOutDate(new Date());
                fullObject.setCheckOutUserId(user.getId());
                fullObject.setCheckOutUser(username);

                // reset audit details
                fullObject.setSubmitted(false);
                fullObject.setSubmissionDate(null);
                fullObject.setSubmissionUser(null);
                fullObject.setSubmissionUserId(null);
                fullObject.setApproved(false);
                fullObject.setApprovalDate(null);
                fullObject.setApprovalUser(null);
                fullObject.setApprovalUserId(null);
                fullObject.setComments(null);

                // call content module to insert new version
                contentModuleDao.insert(fullObject);

                // update record
                contentManagerDao.insertVersion(fullObject);

                // audit
                audit(contentObject.getId(), USE_CASE_CHECKOUT, nextVersionId, user);

                // handle profile
                Application app = Application.getInstance();
                if (ContentProfileModule.isProfiledContent(fullObject.getClassName())) {
                    ContentProfileModule profileMod = (ContentProfileModule)app.getModule(ContentProfileModule.class);
                    profileMod.checkOutProfileData(fullObject, previousVersion);
                }

                return fullObject;
            }
        }
        catch (ContentException e) {
            throw e;
        }
        catch (Exception e) {
            log.error(e.toString(), e);
            throw new ContentException(e.toString());
        }
    }

    /**
     * Save checked out content without creating a new version or submitting for approval.
     * @param contentObject
     * @param user
     * @return
     * @throws com.tms.cms.core.model.ContentException
     */
    public ContentObject save(ContentObject contentObject, User user) throws ContentException {
        try {
            // check permission
            if (!hasPermission(contentObject, user.getId(), USE_CASE_SAVE))
                throw new ContentPermissionException("No permission");

            // check prerequisites
            ContentManagerDao contentManagerDao = (ContentManagerDao) getDao();
            if (contentObject.isSubmitted()) {
                throw new ContentSubmittedException("Content " + contentObject.getId() + " already submitted");
            }
            else if (!contentObject.isCheckedOut() || !user.getId().equals(contentObject.getCheckOutUserId())
                    || contentObject.isApproved()) {
                throw new ContentCheckedOutException("Content " + contentObject.getId() + " not checked out");
            }

            // check version to make sure it matches
            ContentObject oldVersion = contentManagerDao.selectById(contentObject.getId());
            if (!oldVersion.getVersion().equals(contentObject.getVersion())) {
                throw new ContentException("Content " + contentObject.getId() + " version mismatch");
            }

            // update date 'modified'
			contentObject.setDate(new Date());
			
            // call content module to update current version
            Application application = Application.getInstance();
            Class moduleClass = ContentUtil.getModuleClassFromKey(contentObject.getId());
            ContentModule module = (ContentModule) application.getModule(moduleClass);
            ContentModuleDao contentModuleDao = (ContentModuleDao) module.getDao();
            contentModuleDao.update(contentObject);

            // update record
            contentManagerDao.updateVersion(contentObject);

            // reindex object
            unindex(contentObject);
            index(contentObject);

            // audit
            audit(contentObject.getId(), USE_CASE_SAVE, contentObject.getVersion(), user);

            return contentObject;
        }
        catch (ContentException e) {
            throw e;
        }
        catch (Exception e) {
            log.error(e.toString(), e);
            throw new ContentException(e.toString());
        }
    }

    /**
     * Submit for approval.
     * @param key
     * @param user
     * @throws com.tms.cms.core.model.ContentException
     */
    public ContentObject submitForApproval(String key, User user) throws ContentException {
        return submitForApproval(key, user, true);
    }

    /**
     * Submit for approval.
     * @param key
     * @param user
     * @param checkPermission Set false to ignore permission checking
     * @throws com.tms.cms.core.model.ContentException
     */
    public ContentObject submitForApproval(String key, User user, boolean checkPermission) throws ContentException {
        try {
            // retrieve content object
            ContentManagerDao contentManagerDao = (ContentManagerDao) getDao();
            ContentObject contentObject = contentManagerDao.selectById(key);

            // check permission
            if (checkPermission && !hasPermission(contentObject, user.getId(), USE_CASE_SUBMIT))
                throw new ContentPermissionException("No permission");

            // check prerequisites
            if (contentObject.isSubmitted()) {
                throw new ContentSubmittedException("Content " + contentObject.getId() + " already submitted");
            }
            else if (!contentObject.isCheckedOut() || !user.getId().equals(contentObject.getCheckOutUserId())
                    || contentObject.isApproved()) {
                throw new ContentCheckedOutException("Content " + contentObject.getId() + " not checked out");
            }

            // update flags
            contentObject.setCheckedOut(false);
            contentObject.setCheckOutDate(null);
            contentObject.setCheckOutUser(null);
            contentObject.setCheckOutUserId(null);
            contentObject.setSubmitted(true);
            contentObject.setSubmissionDate(new Date());
            contentObject.setSubmissionUser(user.getUsername());
            contentObject.setSubmissionUserId(user.getId());
            contentObject.setApproved(false);
            contentObject.setApprovalDate(null);
            contentObject.setApprovalUser(null);
            contentObject.setApprovalUserId(null);

            // update record
            contentManagerDao.updateVersion(contentObject);

            // audit
            audit(contentObject.getId(), USE_CASE_SUBMIT, contentObject.getVersion(), user);

            return contentObject;
        }
        catch (ContentException e) {
            throw e;
        }
        catch (Exception e) {
            log.error(e.toString(), e);
            throw new ContentException(e.toString());
        }
    }

    /**
     * Approve submission.
     * @param contentObject
     * @param user
     * @throws com.tms.cms.core.model.ContentException
     */
    public void approve(ContentObject contentObject, User user) throws ContentException {
        approve(contentObject,  user, true);
    }

    /**
     * Approve submission.
     * @param contentObject
     * @param user
     * @param checkPermission Set false to ignore permission checking
     * @throws com.tms.cms.core.model.ContentException
     */
    public void approve(ContentObject contentObject, User user, boolean checkPermission) throws ContentException {
        try {
            // check permission
            if (checkPermission && !hasPermission(contentObject, user.getId(), USE_CASE_APPROVE))
                throw new ContentPermissionException("No permission");

            // check prerequisites
            if (contentObject.isCheckedOut()) {
                throw new ContentCheckedOutException("Content " + contentObject.getId() + " is checked out");
            }
            if (!contentObject.isSubmitted()) {
                throw new ContentSubmittedException("Content " + contentObject.getId() + " not submitted");
            }

            // update flags
            contentObject.setNew(false);
            contentObject.setModified(true);
            contentObject.setApproved(true);
            contentObject.setApprovalDate(new Date());
            contentObject.setApprovalUser(user.getUsername());
            contentObject.setApprovalUserId(user.getId());
            contentObject.setSubmitted(false);
            contentObject.setDate(contentObject.getApprovalDate());

            ContentManagerDao contentManagerDao = (ContentManagerDao) getDao();
            boolean versioning = isVersioningEnabled();

            // check version to make sure it matches
            ContentObject oldVersion = contentManagerDao.selectById(contentObject.getId());
            if (!oldVersion.getVersion().equals(contentObject.getVersion())) {
                throw new ContentException("Content " + contentObject.getId() + " version mismatch");
            }

            // update record
            contentManagerDao.updateVersion(contentObject);

            // audit
            audit(contentObject.getId(), USE_CASE_APPROVE, contentObject.getVersion(), user);

            // publish new version if already published
            if (contentObject.isPublished()) {
                republish(contentObject.getId(), false, user);
            }
            if (!versioning) {
                Application application = Application.getInstance();
                Class moduleClass = ContentUtil.getModuleClassFromKey(contentObject.getId());
                ContentModule module = (ContentModule) application.getModule(moduleClass);
                ContentModuleDao contentModuleDao = (ContentModuleDao) module.getDao();

                // retrieve history
                String key = contentObject.getId();
                Collection history = contentManagerDao.selectVersions(key, null, false, 0, -1);

                // delete older versions
                float current = Float.parseFloat(contentObject.getVersion());
                for (Iterator i = history.iterator(); i.hasNext();) {
                    ContentObject temp = (ContentObject) i.next();
                    float ver = Float.parseFloat(temp.getVersion());
                    if (ver < current) {
                        contentModuleDao.delete(key, temp.getVersion());
                        contentManagerDao.deleteVersion(key, temp.getVersion());
                    }
                }

            }

        }
        catch (ContentException e) {
            throw e;
        }
        catch (Exception e) {
            log.error(e.toString(), e);
            throw new ContentException(e.toString());
        }
    }

    /**
     * Reject submission.
     * @param contentObject
     * @param user
     * @throws com.tms.cms.core.model.ContentException
     */
    public void reject(ContentObject contentObject, User user) throws ContentException {
        try {
            // check permission
            if (!hasPermission(contentObject, user.getId(), USE_CASE_REJECT))
                throw new ContentPermissionException("No permission");

            // check prerequisites
            if (!contentObject.isSubmitted()) {
                throw new ContentSubmittedException("Content " + contentObject.getId() + " not submitted");
            }

            // update flags
            contentObject.setApproved(false);
            contentObject.setApprovalDate(null);
            contentObject.setApprovalUser(null);
            contentObject.setApprovalUserId(null);
            contentObject.setSubmitted(false);

            ContentManagerDao contentManagerDao = (ContentManagerDao) getDao();
            boolean versioning = isVersioningEnabled();

            // check version to make sure it matches
            ContentObject oldVersion = contentManagerDao.selectById(contentObject.getId());
            if (!oldVersion.getVersion().equals(contentObject.getVersion())) {
                throw new ContentException("Content " + contentObject.getId() + " version mismatch");
            }

            // update record
            contentManagerDao.updateVersion(contentObject);

            if (!versioning) {
                Application application = Application.getInstance();
                Class moduleClass = ContentUtil.getModuleClassFromKey(contentObject.getId());
                ContentModule module = (ContentModule) application.getModule(moduleClass);
                ContentModuleDao contentModuleDao = (ContentModuleDao) module.getDao();

                // retrieve history
                String key = contentObject.getId();
                Collection history = contentManagerDao.selectVersions(key, null, false, 0, -1);

                // update first version
                float current = Float.parseFloat(contentObject.getVersion());
                Iterator historyIterator = history.iterator();

                // rollback to previous
                if (historyIterator.hasNext()) {
                    ContentObject temp = (ContentObject) historyIterator.next();
                    if (historyIterator.hasNext()) {
                        temp = (ContentObject) historyIterator.next();
                        float ver = Float.parseFloat(temp.getVersion());
                        if (ver < current) {

                            // delete version
                            contentModuleDao.delete(key, contentObject.getVersion());
                            contentManagerDao.deleteVersion(key, contentObject.getVersion());

                            // call content module to retrieve required version
                            contentObject = contentManagerDao.selectByVersion(key, temp.getVersion());
                            ContentObject fullObject = contentModuleDao.selectByVersion(key, temp.getVersion());
                            ContentUtil.copyStatusAttributes(fullObject, contentObject);


                            // update status
                            fullObject.setCheckedOut(false);
                            fullObject.setCheckOutDate(null);
                            fullObject.setCheckOutUser(null);
                            fullObject.setCheckOutUserId(null);
                            fullObject.setSubmitted(false);
                            fullObject.setSubmissionDate(null);
                            fullObject.setSubmissionUser(null);
                            fullObject.setSubmissionUserId(null);
                            fullObject.setApproved(false);
                            fullObject.setApprovalDate(null);
                            fullObject.setApprovalUser(null);
                            fullObject.setApprovalUserId(null);

                            contentObject = fullObject;
                            contentManagerDao.updateVersion(contentObject);

                            // check for withdrawal
                            if (contentObject.isPublished()) {
                                ContentPublisher publisher = (ContentPublisher) application.getModule(ContentPublisher.class);
                                ContentObject po = ((ContentPublisherDao) publisher.getDao()).selectById(key);
                                float pv = Float.parseFloat(po.getVersion());
                                if (ver < pv) {
                                    withdraw(key, false, user);
                                }
                            }

                            // reindex object
                            unindex(contentObject);
                            index(contentObject);
                        }
                    }
                }

                // delete older versions
                while (historyIterator.hasNext()) {
                    ContentObject temp = (ContentObject) historyIterator.next();
                    float ver = Float.parseFloat(temp.getVersion());
                    if (ver < current) {
                        contentModuleDao.delete(key, temp.getVersion());
                        contentManagerDao.deleteVersion(key, temp.getVersion());
                    }
                }

            }

            // audit
            audit(contentObject.getId(), USE_CASE_REJECT, contentObject.getVersion(), user);
        }
        catch (ContentException e) {
            throw e;
        }
        catch (Exception e) {
            log.error(e.toString(), e);
            throw new ContentException(e.toString());
        }
    }

    /**
     * Undo check-out.
     * @param key
     * @param user
     * @return
     * @throws com.tms.cms.core.model.ContentException
     */
    public ContentObject undoCheckOut(String key, User user) throws ContentException {
        try {

            // retrieve object
            ContentManagerDao dao = (ContentManagerDao) getDao();
            ContentObject contentObject = dao.selectById(key);

            // check permission
            if (!user.getId().equals(contentObject.getCheckOutUserId()) && !hasPermission(contentObject, user.getId(), USE_CASE_UNDO_CHECKOUT)) {
                throw new ContentPermissionException("No permission");
            }

            // check checkout status
            if (!contentObject.isCheckedOut()) {
                throw new ContentCheckedOutException("Content " + contentObject.getId() + " not checked out");
            }

            // retrieve and delete current version
            Collection history = dao.selectVersions(key, null, false, 0, -1);
            if (history.size() < 1) {
                throw new ContentException("Invalid version history size: " + history.size());
            }
            else if (history.size() == 1) {
                throw new ContentException("Only one version, cannot undo checkout");
            }

            Iterator iterator = history.iterator();
            iterator.next(); // current version
            ContentObject prev = (ContentObject) iterator.next();

            // get previous version
            ContentObject previousVersion = dao.selectByVersion(key, prev.getVersion());

            // call content module to retrieve previous version
            Class moduleClass = ContentUtil.getModuleClassFromKey(contentObject.getId());
            Application application = Application.getInstance();
            ContentModule module = (ContentModule) application.getModule(moduleClass);
            ContentModuleDao contentModuleDao = (ContentModuleDao) module.getDao();
            ContentObject fullObject = contentModuleDao.selectByVersion(key, prev.getVersion());
            ContentUtil.copyStatusAttributes(fullObject, previousVersion);

            // delete current version
            dao.deleteVersion(key, contentObject.getVersion());

            // update record
            fullObject.setCheckedOut(false);
            fullObject.setCheckOutDate(null);
            fullObject.setCheckOutUser(null);
            fullObject.setCheckOutUserId(null);
            if (previousVersion.getApprovalDate() != null) {
                fullObject.setApproved(true);
            }
            dao.updateVersion(fullObject);

            // call content module to delete version
            contentModuleDao.delete(key, contentObject.getVersion());

            // reindex object
            unindex(fullObject);
            index(fullObject);

            // audit
            audit(contentObject.getId(), USE_CASE_UNDO_CHECKOUT, contentObject.getVersion(), user);

            // remove profile data
            Application app = Application.getInstance();
            if (ContentProfileModule.isProfiledContent(fullObject.getClassName())) {
                ContentProfileModule profileMod = (ContentProfileModule)app.getModule(ContentProfileModule.class);
                profileMod.deleteProfileData(contentObject.getId(), contentObject.getVersion());
            }

            return fullObject;
        }
        catch (ContentException e) {
            throw e;
        }
        catch (Exception e) {
            log.error(e.toString(), e);
            throw new ContentException(e.toString());
        }
    }

    /**
     * Rollback to a previous version.
     * @param key
     * @param version
     * @param user
     * @return
     * @throws kacang.model.DataObjectNotFoundException
     * @throws com.tms.cms.core.model.ContentException
     */
    public ContentObject rollback(String key, String version, User user) throws DataObjectNotFoundException, ContentException {
        try {
            // retrieve content object
            ContentManagerDao contentManagerDao = (ContentManagerDao) getDao();
            ContentObject contentObject = contentManagerDao.selectById(key);

            // check permission
            if (!hasPermission(contentObject, user.getId(), USE_CASE_ROLLBACK))
                throw new ContentPermissionException("No permission");

            // retrieve history
            Application application = Application.getInstance();
            Collection history = contentManagerDao.selectVersions(key, null, false, 0, -1);

            // call content module to retrieve required version
            contentObject = contentManagerDao.selectByVersion(key, version);
            Class moduleClass = ContentUtil.getModuleClassFromKey(contentObject.getId());
            ContentModule module = (ContentModule) application.getModule(moduleClass);
            ContentModuleDao contentModuleDao = (ContentModuleDao) module.getDao();
            ContentObject fullObject = contentModuleDao.selectByVersion(key, version);
            ContentUtil.copyStatusAttributes(fullObject, contentObject);

            // determine whether profile exists
            Application app = Application.getInstance();
            boolean profileExists = ContentProfileModule.isProfiledContent(fullObject.getClassName());

            // delete newer versions
            float requested = Float.parseFloat(version);
            for (Iterator i = history.iterator(); i.hasNext();) {
                ContentObject temp = (ContentObject) i.next();
                float current = Float.parseFloat(temp.getVersion());
                if (requested < current) {
                    contentModuleDao.delete(key, temp.getVersion());
                    contentManagerDao.deleteVersion(key, temp.getVersion());

                    // remove profile data
                    if (profileExists) {
                        try {
                            ContentProfileModule profileMod = (ContentProfileModule)app.getModule(ContentProfileModule.class);
                            profileMod.deleteProfileData(contentObject.getId(), temp.getVersion());
                        }
                        catch (Exception e) {
                            ;
                        }
                    }

                }
            }

            // update status
            fullObject.setCheckedOut(false);
            fullObject.setCheckOutDate(null);
            fullObject.setCheckOutUser(null);
            fullObject.setCheckOutUserId(null);
            fullObject.setSubmitted(true);
            fullObject.setApproved(false);
            fullObject.setApprovalDate(null);
            fullObject.setApprovalUser(null);
            fullObject.setApprovalUserId(null);
            contentManagerDao.updateVersion(fullObject);

            // check for withdrawal
            if (fullObject.isPublished()) {
                ContentPublisher publisher = (ContentPublisher) application.getModule(ContentPublisher.class);
                ContentObject po = ((ContentPublisherDao) publisher.getDao()).selectById(key);
                float current = Float.parseFloat(po.getVersion());
                if (requested < current) {
                    withdraw(key, false, user);
                }
            }

            // reindex object
            unindex(fullObject);
            index(fullObject);

            // audit
            audit(contentObject.getId(), USE_CASE_ROLLBACK, version, user);

            return fullObject;
        }
        catch (DataObjectNotFoundException e) {
            throw e;
        }
        catch (ContentException e) {
            throw e;
        }
        catch (Exception e) {
            log.error(e.toString(), e);
            throw new ContentException(e.toString());
        }
    }

    /**
     * Move to another point in the Content Tree.
     * @param key
     * @param newParentId
     * @param user
     * @throws com.tms.cms.core.model.ContentException
     */
    public void move(String key, String newParentId, User user) throws ContentException {
        try {
            // check that object not pointing to itself
            if (key.equals(newParentId))
                throw new ContentException("Invalid parent");

            // retrieve content object
            ContentManagerDao contentManagerDao = (ContentManagerDao) getDao();
            ContentObject contentObject = contentManagerDao.selectById(key);

            // check that parent exists
            ContentObject newParent = viewPath(newParentId, user);

            // check appropriate parent class
            Class parentClass = Class.forName(newParent.getClassName());
            Class coClass = Class.forName(contentObject.getClassName());
            Collection allowedClassList = Arrays.asList(getAllowedParentClasses(coClass));
            if (!allowedClassList.contains(parentClass)) {
                throw new ContentException("Invalid parent class");
            }

            // check permission
            if (!hasPermission(contentObject, user.getId(), USE_CASE_MOVE))
                throw new ContentPermissionException("No permission");
            if (!hasPermission(newParent, user.getId(), USE_CASE_MOVE))
                throw new ContentPermissionException("No permission to parent");

            // make sure no endless recursion
            ContentObject tmp = newParent;
            while (tmp != null) {
                if (tmp.getId().equals(key)) {
                    throw new ContentException("Invalid parent");
                }
                tmp = tmp.getParent();
            }

            // determine new acl id - must not be null.
            boolean propagateAcl = false;
            if (!contentObject.getId().equals(contentObject.getAclId())) { // no specified acl, so inherit
                contentObject.setAclId(newParent.getAclId());
                propagateAcl = true;
            }

            // set approve flag and update
            ContentManagerDao dao = (ContentManagerDao) getDao();
            contentObject.setParentId(newParentId);
            contentObject.setOrdering(null);
            dao.updateStatus(contentObject);

            // propagate Acl
            if (propagateAcl) {
                ContentObject root = viewTree(contentObject.getId(), null, null, user);
                propagateAclId(dao, root, contentObject.getAclId(), false);
            }

            // audit
            audit(contentObject.getId(), USE_CASE_MOVE, newParentId, user);
        }
        catch (ContentException e) {
            throw e;
        }
        catch (Exception e) {
            log.error(e.toString(), e);
            throw new ContentException(e.toString());
        }
    }

    /**
     * Publish a ContentObject based on its startTime and endTime.
     * @param contentObject
     * @param recursive
     * @param user
     * @throws ContentException
     */
    public void publishOnSchedule(ContentObject contentObject, boolean recursive, User user) throws ContentException {
        try {
            // check permission
            if (!hasPermission(contentObject, user.getId(), USE_CASE_PUBLISH))
                throw new ContentPermissionException("No permission");

            // update status
            ContentManagerDao contentManagerDao = (ContentManagerDao) getDao();
            contentManagerDao.updateStatus(contentObject);

            if (contentObject.getStartDate() != null) {
                SchedulingService scheduler = (SchedulingService) Application.getInstance().getService(SchedulingService.class);

                // schedule publish
                JobSchedule jobSched = new JobSchedule(contentObject.getId(), JobSchedule.SECONDLY);
                jobSched.setGroup(USE_CASE_PUBLISH);
                jobSched.setStartTime(contentObject.getStartDate());
                jobSched.setRepeatCount(0);

                JobTask jobTask = new ContentPublishJobTask();
                jobTask.setName(contentObject.getId());
                jobTask.setGroup(USE_CASE_PUBLISH);
                jobTask.setDescription(contentObject.getName());

                JobTaskData jobTaskData = jobTask.getJobTaskData();
                jobTaskData.put("id", contentObject.getId());
                jobTaskData.put("userId", user.getId());
                jobTaskData.put("recursive", recursive);
                jobTask.setJobTaskData(jobTaskData);

                scheduler.deleteJobTask(jobTask);
                scheduler.scheduleJob(jobTask, jobSched);

                if (recursive) {
                    audit(contentObject.getId(), USE_CASE_SCHEDULED_PUBLISH_RECUR, contentObject.getVersion(), user);
                }
                else {
                    audit(contentObject.getId(), USE_CASE_SCHEDULED_PUBLISH, contentObject.getVersion(), user);
                }

                // schedule withdrawal
                if (contentObject.getEndDate() != null) {
                    jobSched = new JobSchedule(contentObject.getId(), JobSchedule.SECONDLY);
                    jobSched.setGroup(USE_CASE_WITHDRAW);
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(contentObject.getEndDate());
                    jobSched.setStartTime(cal.getTime());
                    jobSched.setRepeatCount(0);

                    jobTask = new ContentWithdrawJobTask();
                    jobTask.setName(contentObject.getId());
                    jobTask.setGroup(USE_CASE_WITHDRAW);
                    jobTask.setDescription(contentObject.getName());

                    jobTaskData = jobTask.getJobTaskData();
                    jobTaskData.put("id", contentObject.getId());
                    jobTaskData.put("userId", user.getId());
                    jobTaskData.put("recursive", recursive);
                    jobTask.setJobTaskData(jobTaskData);

                    scheduler.deleteJobTask(jobTask);
                    scheduler.scheduleJob(jobTask, jobSched);
                }

                // update start and end date
                Map propertyMap = new HashMap();
                propertyMap.put("startDate", contentObject.getStartDate());
                propertyMap.put("endDate", contentObject.getEndDate());
                contentManagerDao.updateStatus(new String[] { contentObject.getId() }, propertyMap);

            }
            else {
                publish(contentObject.getId(), recursive, user);
            }
        }
        catch (ContentException e) {
            throw e;
        }
        catch (Exception e) {
            log.error(e.toString(), e);
            throw new ContentException(e.toString());
        }
    }

    /**
     * Publish a ContentObject.
     * @param key
     * @param recursive
     * @param user
     * @throws ContentException
     */
    public void publish(String key, boolean recursive, User user) throws ContentException {
        publish(key, recursive, user, true);
    }

    /**
     * Publish a ContentObject.
     * @param key
     * @param recursive
     * @param user
     * @param checkPermission Set false to ignore permission checking
     * @throws ContentException
     */
    public void publish(String key, boolean recursive, User user, boolean checkPermission) throws ContentException {
        try {
            // retrieve content object
            ContentManagerDao contentManagerDao = (ContentManagerDao) getDao();
            ContentObject contentObject = contentManagerDao.selectById(key);

            // check permission
            if (checkPermission && !hasPermission(contentObject, user.getId(), USE_CASE_PUBLISH))
                throw new ContentPermissionException("No permission");

            // publish
            if (recursive) {
                if (checkPermission) {
                    contentObject = viewTreeWithOrphans(contentObject.getId(), null, false, USE_CASE_PUBLISH, user);
                }
                else {
                    contentObject = viewTree(contentObject.getId(), null, null, user);
                }
                audit(contentObject.getId(), USE_CASE_PUBLISH_RECUR, contentObject.getVersion(), user);
            }
            else {
                audit(contentObject.getId(), USE_CASE_PUBLISH, contentObject.getVersion(), user);
            }
            propagatePublish(contentObject, recursive, false, checkPermission, user);
        }
        catch (ContentException e) {
            throw e;
        }
        catch (Exception e) {
            log.error(e.toString(), e);
            throw new ContentException(e.toString());
        }
    }

    /**
     * Republish a ContentObject (publish latest version of content only if it is already published - otherwise ignore).
     * @param key
     * @param recursive
     * @param user
     * @throws ContentException
     */
    public void republish(String key, boolean recursive, User user) throws ContentException {
        republish(key, recursive, user, true);
    }

    /**
     * Republish a ContentObject (publish latest version of content only if it is already published - otherwise ignore).
     * @param key
     * @param recursive
     * @param user
     * @param checkPermission Set false to ignore permission checking
     * @throws ContentException
     */
    public void republish(String key, boolean recursive, User user, boolean checkPermission) throws ContentException {
        try {
            // retrieve content object
            ContentManagerDao contentManagerDao = (ContentManagerDao) getDao();
            ContentObject contentObject = contentManagerDao.selectById(key);

            // check permission
            if (!hasPermission(contentObject, user.getId(), USE_CASE_PUBLISH))
                throw new ContentPermissionException("No permission");

            // publish
            if (recursive) {
                if (checkPermission) {
                    contentObject = viewTreeWithOrphans(contentObject.getId(), null, false, USE_CASE_PUBLISH, user);
                }
                else {
                    contentObject = viewTree(contentObject.getId(), null, null, user);
                }
                audit(contentObject.getId(), USE_CASE_PUBLISH_RECUR, contentObject.getVersion(), user);
            }
            else {
                audit(contentObject.getId(), USE_CASE_PUBLISH, contentObject.getVersion(), user);
            }
            propagatePublish(contentObject, recursive, true, checkPermission, user);
        }
        catch (ContentException e) {
            throw e;
        }
        catch (Exception e) {
            log.error(e.toString(), e);
            throw new ContentException(e.toString());
        }
    }

    /**
     * Recursive method to publish a ContentObject.
     * @param contentObject
     * @param recursive
     * @param onlyPublishedContent
     * @param user
     * @throws ContentException
     */
    protected void propagatePublish(ContentObject contentObject, boolean recursive, boolean onlyPublishedContent, boolean checkPermission, User user) throws ContentException {
        try {
            // check prerequisites
            boolean versioning = isVersioningEnabled();
            String publishedVersion = contentObject.getPublishVersion();
            if (contentObject.isApproved() // must be approved
                    && (!versioning || !contentObject.isPublished() || !contentObject.getVersion().equals(publishedVersion)) // must be a different version
                    && (!onlyPublishedContent || contentObject.isPublished()) // republish only published content
            ) {
                // update status
                ContentManagerDao dao = (ContentManagerDao) getDao();
                contentObject.setPublished(true);
                contentObject.setPublishDate(new Date());
                contentObject.setPublishUserId(user.getId());
                contentObject.setPublishUser(user.getUsername());
                contentObject.setPublishVersion(contentObject.getVersion());
                dao.updateFullStatus(contentObject);

                // publish
                Application application = Application.getInstance();
                ContentPublisher publisher = (ContentPublisher) application.getModule(ContentPublisher.class);
                publisher.publish(contentObject, user);

                // schedule subscription notification
                if (Boolean.valueOf(application.getProperty(ContentManager.APPLICATION_PROPERTY_CONTENT_SUBSCRIPTION)).booleanValue()) {
                    SchedulingService scheduler = (SchedulingService) Application.getInstance().getService(SchedulingService.class);
                    JobSchedule jobSched = new JobSchedule(contentObject.getId(), JobSchedule.SECONDLY);
                    jobSched.setGroup(USE_CASE_PUBLISH + "_Subscription");
                    jobSched.setStartTime(new Date());
                    jobSched.setRepeatCount(0);
                    JobTask jobTask = new ContentSubscriptionTask();
                    jobTask.setName(contentObject.getId());
                    jobTask.setGroup(USE_CASE_PUBLISH + "_Subscription");
                    jobTask.setDescription(contentObject.getName());
                    JobTaskData jobTaskData = jobTask.getJobTaskData();
                    jobTaskData.put("id", contentObject.getId());
                    jobTaskData.put("version", publishedVersion); // previous published version
                    jobTaskData.put("userId", user.getId());
                    jobTask.setJobTaskData(jobTaskData);
                    scheduler.scheduleJob(jobTask, jobSched);
                }
            }

            // propagate?
            if (recursive) {
                for (Iterator i = contentObject.getChildren().iterator(); i.hasNext();) {
                    ContentObject tmpObj = (ContentObject) i.next();
                    propagatePublish(tmpObj, true, onlyPublishedContent, checkPermission, user);
                }
            }
        }
        catch (Exception e) {
            log.error(e.toString(), e);
            throw new ContentException(e.toString());
        }
    }

    /**
     * Withdraw a ContentObject.
     * @param key
     * @param recursive
     * @param user
     * @throws ContentException
     */
    public void withdraw(String key, boolean recursive, User user) throws ContentException {
        withdraw(key, recursive, user, true);
    }

    /**
     * Withdraw a ContentObject.
     * @param key
     * @param recursive
     * @param user
     * @param checkPermission Set false to ignore permission checking
     * @throws ContentException
     */
    public void withdraw(String key, boolean recursive, User user, boolean checkPermission) throws ContentException {
        try {
            // retrieve content object
            ContentManagerDao contentManagerDao = (ContentManagerDao) getDao();
            ContentObject contentObject = contentManagerDao.selectById(key);

            // check permission
            if (checkPermission && !hasPermission(contentObject, user.getId(), USE_CASE_WITHDRAW))
                throw new ContentPermissionException("No permission");

            // withdraw
            if (recursive) {
                if (checkPermission) {
                    contentObject = viewTreeWithOrphans(contentObject.getId(), null, false, USE_CASE_WITHDRAW, user);
                }
                else {
                    contentObject = viewTree(contentObject.getId(), null, null, user);
                }
                audit(contentObject.getId(), USE_CASE_WITHDRAW_RECUR, contentObject.getPublishVersion(), user);
            }
            else {
                audit(contentObject.getId(), USE_CASE_WITHDRAW, contentObject.getPublishVersion(), user);
            }
            propagateWithdraw(contentObject, recursive, checkPermission, user);
        }
        catch (ContentException e) {
            throw e;
        }
        catch (Exception e) {
            log.error(e.toString(), e);
            throw new ContentException(e.toString());
        }
    }

    /**
     * Recursive method to withdraw a ContentObject.
     * @param contentObject
     * @param recursive
     * @param user
     * @param checkPermission Set false to ignore permission checking
     * @throws ContentException
     */
    protected void propagateWithdraw(ContentObject contentObject, boolean recursive, boolean checkPermission, User user) throws ContentException {
        try {
            // check prerequisites
            if (contentObject.isPublished()) {

                // update status
                ContentManagerDao dao = (ContentManagerDao) getDao();
                contentObject.setPublished(false);
                contentObject.setPublishDate(null);
                contentObject.setPublishUserId(null);
                contentObject.setPublishUser(null);
                contentObject.setPublishVersion(null);
                contentObject.setStartDate(null);
                contentObject.setEndDate(null);
                dao.updateFullStatus(contentObject);

                // withdraw
                Application application = Application.getInstance();
                ContentPublisher publisher = (ContentPublisher) application.getModule(ContentPublisher.class);
                publisher.withdraw(contentObject, user);
            }

            // propagate?
            if (recursive) {
                for (Iterator i = contentObject.getChildren().iterator(); i.hasNext();) {
                    ContentObject tmpObj = (ContentObject) i.next();
                    propagateWithdraw(tmpObj, true, checkPermission, user);
                }
            }
        }
        catch (Exception e) {
            log.error(e.toString(), e);
            throw new ContentException(e.toString());
        }
    }

    /**
     * Archive a ContentObject.
     * @param key
     * @param recursive
     * @param user
     * @throws ContentException
     */
    public void archive(String key, boolean recursive, User user) throws ContentException {
        try {
            // retrieve content object
            ContentManagerDao contentManagerDao = (ContentManagerDao) getDao();
            ContentPublisher cp = (ContentPublisher)Application.getInstance().getModule(ContentPublisher.class);
            ContentObject contentObject = contentManagerDao.selectById(key);

            // check permission
            if (!hasPermission(contentObject, user.getId(), USE_CASE_ARCHIVE))
                throw new ContentPermissionException("No permission");

            // archive
            if (recursive) {
                contentObject = viewTreeWithOrphans(contentObject.getId(), null, false, USE_CASE_ARCHIVE, user);
                audit(contentObject.getId(), USE_CASE_ARCHIVE_RECUR, null, user);
            }
            else {
                audit(contentObject.getId(), USE_CASE_ARCHIVE, null, user);
            }            
            propagateArchive(contentObject, recursive, user);
            cp.unindex(contentObject);
            cp.index(contentObject);
        }
        catch (ContentException e) {
            throw e;
        }
        catch (Exception e) {
            log.error(e.toString(), e);
            throw new ContentException(e.toString());
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
                ContentManagerDao dao = (ContentManagerDao) getDao();
                contentObject.setArchived(true);
                dao.updateStatus(contentObject);

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
     * Unarchive a ContentObject.
     * @param key
     * @param recursive
     * @param user
     * @throws ContentException
     */
    public void unarchive(String key, boolean recursive, User user) throws ContentException {
        try {
            // retrieve content object
            ContentManagerDao contentManagerDao = (ContentManagerDao) getDao();
            ContentPublisher cp = (ContentPublisher)Application.getInstance().getModule(ContentPublisher.class);
            ContentObject contentObject = contentManagerDao.selectById(key);

            // check permission
            if (!hasPermission(contentObject, user.getId(), USE_CASE_UNARCHIVE))
                throw new ContentPermissionException("No permission");

            // archive
            if (recursive) {
                contentObject = viewTreeWithOrphans(contentObject.getId(), null, false, USE_CASE_UNARCHIVE, user);
                audit(contentObject.getId(), USE_CASE_UNARCHIVE_RECUR, null, user);
            }
            else {
                audit(contentObject.getId(), USE_CASE_UNARCHIVE, null, user);
            }
            
            propagateUnarchive(contentObject, recursive, user);
            cp.unindex(contentObject);
            cp.index(contentObject);
            
        }
        catch (ContentException e) {
            throw e;
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
                ContentManagerDao dao = (ContentManagerDao) getDao();
                contentObject.setArchived(false);
                dao.updateStatus(contentObject);

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

    /**
     * Similar to view, except that this action is audited.
     * @param key
     * @param user
     * @return
     * @throws kacang.model.DataObjectNotFoundException
     * @throws com.tms.cms.core.model.ContentException
     */
    public ContentObject preview(String key, User user) throws DataObjectNotFoundException, ContentException {
        ContentObject co = view(key, user);

        // audit
        audit(key, USE_CASE_PREVIEW, null, user);

        return co;
    }

    /**
     * Similar to view, except that this action is audited.
     * @param key
     * @param version
     * @param user
     * @return
     * @throws kacang.model.DataObjectNotFoundException
     * @throws com.tms.cms.core.model.ContentException
     */
    public ContentObject previewVersion(String key, String version, User user) throws DataObjectNotFoundException, ContentException {
        ContentObject co = viewVersion(key, version, user);

        // audit
        audit(key, USE_CASE_PREVIEW_VERSION, version, user);

        return co;
    }

    /**
     *
     * @param key
     * @param childKeys Array to represent child content objects in the desired sequence. Objects not in this array will be considered unordered.
     * @param user
     * @throws com.tms.cms.core.model.ContentException
     */
    public void reorder(String key, String[] childKeys, User user) throws ContentException {
        try {
            if (childKeys == null) {
                return;
            }

            // retrieve content object
            ContentManagerDao contentManagerDao = (ContentManagerDao) getDao();
            ContentObject contentObject = contentManagerDao.selectById(key);

            // check permission
            if (!hasPermission(contentObject, user.getId(), USE_CASE_REORDER))
                throw new ContentPermissionException("No permission");

            // get children and form key map
            Collection children = viewList(null, null, null, key, null, null, null, null, null, null, null, null, false, 0, -1, null, user);
            Map keyMap = new HashMap();
            int max = childKeys.length * 10;
            for (int i = 0; i < childKeys.length; i++) {
                keyMap.put(childKeys[i], new Integer(max - (i * 10)).toString());
            }

            // iterate thru children and update ordering
            ContentManagerDao dao = (ContentManagerDao) getDao();
            for (Iterator it = children.iterator(); it.hasNext();) {
                ContentObject child = (ContentObject) it.next();
                if (keyMap.containsKey(child.getId())) {
                    child.setOrdering((String) keyMap.get(child.getId()));
                    dao.updateStatus(child);
                }
                else if (child.getOrdering() != null) {
                    child.setOrdering(null);
                    dao.updateStatus(child);
                }
            }

            // audit
            audit(contentObject.getId(), USE_CASE_REORDER, null, user);

        }
        catch (ContentException e) {
            throw e;
        }
        catch (Exception e) {
            log.error(e.toString(), e);
            throw new ContentException(e.toString());
        }
    }


//-- Keyword Relations

    public void updateRelated(ContentObject contentObject, User user) throws ContentException {
        try {
            // check permission
            if (!hasPermission(contentObject, user.getId(), USE_CASE_RELATED))
                throw new ContentPermissionException("No permission");

            // update
            String keywords = contentObject.getKeywords();
            Collection keywordList = ContentUtil.keywordStringToCollection(keywords);
            String[] keywordArray = (String[]) keywordList.toArray(new String[0]);
            ContentManagerDao dao = (ContentManagerDao) getDao();
            dao.updateRelated(contentObject, keywordArray);

            // audit
            audit(contentObject.getId(), USE_CASE_RELATED, null, user);
        }
        catch (ContentException e) {
            throw e;
        }
        catch (Exception e) {
            log.error("updateRelated: " + e.toString(), e);
            throw new ContentException(e.toString());
        }
    }

    public Collection viewRelated(String key, String[] classes, String sort, boolean desc, int start, int rows, User user) throws ContentException {
        try {
            // retrieve content object
            ContentManagerDao contentManagerDao = (ContentManagerDao) getDao();
            ContentObject contentObject = contentManagerDao.selectById(key);

            // check permission
            if (!hasPermission(contentObject, user.getId(), USE_CASE_PREVIEW))
                throw new ContentPermissionException("No permission");

            // retrieve
            return contentManagerDao.selectRelated(key, classes, sort, desc, start, rows);
        }
        catch (ContentException e) {
            throw e;
        }
        catch (Exception e) {
            log.error("viewRelated: " + e.toString());
            throw new ContentException(e.getMessage());
        }
    }

    public int viewRelatedCount(String key, String[] classes, User user) throws ContentException {
        try {
            // retrieve content object
            ContentManagerDao contentManagerDao = (ContentManagerDao) getDao();
            ContentObject contentObject = contentManagerDao.selectById(key);

            // check permission
            if (!hasPermission(contentObject, user.getId(), USE_CASE_PREVIEW))
                throw new ContentPermissionException("No permission");

            // retrieve
            return contentManagerDao.selectRelatedCount(key, classes);
        }
        catch (ContentException e) {
            throw e;
        }
        catch (Exception e) {
            log.error("viewRelatedCount: " + e.toString());
            throw new ContentException(e.getMessage());
        }
    }

    public Collection viewKeywords(String search, String sort, boolean desc, int start, int rows) throws ContentException {
        try {
            ContentManagerDao contentManagerDao = (ContentManagerDao) getDao();
            return contentManagerDao.selectKeywords(search, sort, desc, start, rows);
        }
        catch (Exception e) {
            log.error("viewKeywords: " + e.toString());
            throw new ContentException(e.getMessage());
        }
    }

    public int viewKeywordsCount(String search) throws ContentException {
        try {
            ContentManagerDao contentManagerDao = (ContentManagerDao) getDao();
            return contentManagerDao.selectKeywordsCount(search);
        }
        catch (Exception e) {
            log.error("viewKeywordsCount: " + e.toString());
            throw new ContentException(e.getMessage());
        }
    }

    public void createKeywords(String[] keywords) throws ContentException {
        try {
            ContentManagerDao contentManagerDao = (ContentManagerDao) getDao();
            contentManagerDao.insertKeywords(keywords);
        }
        catch (Exception e) {
            log.error("insertKeywords: " + e.toString());
            throw new ContentException(e.getMessage());
        }
    }

    public void deleteKeywords(String[] keywords) throws ContentException {
        try {
            ContentManagerDao contentManagerDao = (ContentManagerDao) getDao();
            contentManagerDao.deleteKeywords(keywords);
        }
        catch (Exception e) {
            log.error("deleteKeywords: " + e.toString());
            throw new ContentException(e.getMessage());
        }
    }


//-- Security Methods

    /**
     * Defines the default array of content management Roles.
     * @return
     */
    public String[] getRoleArray() {
         return new String[] {"manager", "editor", "author", "reader"};
    }

    /**
     *
     * @param contentObject
     * @param userId
     * @param permission
     * @return true if the user has the specified permission for the content object.
     * @throws com.tms.cms.core.model.ContentException
     */
    public boolean hasPermission(ContentObject contentObject, String userId, String permission) throws ContentException {
        try {
            // retrieve content object
            ContentManagerDao dao = (ContentManagerDao) getDao();

            // retrieve user principals
            SecurityService security = (SecurityService) Application.getInstance().getService(SecurityService.class);
            Collection principalList = security.getUserPrincipalIds(userId);
            String[] principalArray = (String[]) principalList.toArray(new String[0]);

            // retrieve object
            Collection results = dao.selectByPermission(principalArray, permission, new String[]{contentObject.getId()}, null, null, null, null, null, null, null, null, null, null, null, null, false, null, false, 0, 1);
            boolean hasPerm = (results.size() == 1);

/*
            // check for other conditions

            // edit permission
            if (USE_CASE_CHECKOUT.equals(permission) && !userId.equals(contentObject.getCheckOutUserId()) && (contentObject.isCheckedOut() || contentObject.isSubmitted())) {
                hasPerm = false;
            }

            // undo checkout permission
            else if (USE_CASE_UNDO_CHECKOUT.equals(permission) && userId.equals(contentObject.getCheckOutUserId()) && !"1".equals(contentObject.getVersion())) {
                hasPerm = true;
            }
            else if (USE_CASE_UNDO_CHECKOUT.equals(permission) && (!contentObject.isCheckedOut() || "1".equals(contentObject.getVersion()))) {
                hasPerm = false;
            }

            // approve permission
            else if (USE_CASE_APPROVE.equals(permission) && !contentObject.isSubmitted()) {
                hasPerm = false;
            }

            // reject permission
            else if (USE_CASE_REJECT.equals(permission) && !contentObject.isSubmitted()) {
                hasPerm = false;
            }

            // publish permission
            else if (USE_CASE_PUBLISH.equals(permission) && !contentObject.isPublished() && !contentObject.isApproved()) {
                hasPerm = false;
            }

            // withdraw permission
            else if (USE_CASE_WITHDRAW.equals(permission) && !contentObject.isPublished()) {
                hasPerm = false;
            }

            // archive permission
            else if (USE_CASE_ARCHIVE.equals(permission) && !contentObject.isPublished() && !contentObject.isApproved()) {
                hasPerm = false;
            }

            // unarchive permission
            else if (USE_CASE_UNARCHIVE.equals(permission) && !contentObject.isArchived()) {
                hasPerm = false;
            }

*/
            return hasPerm;
        }
        catch (Exception e) {
            log.error(e.toString(), e);
            throw new ContentException(e.toString());
        }
    }

    /**
     *
     * @param key
     * @param userId
     * @return A Collection of permission ID Strings.
     * @throws ContentException
     */
    public Collection viewPermissions(String key, String userId) throws ContentException {
        try {
            // retrieve content object
            ContentManagerDao dao = (ContentManagerDao) getDao();

            // retrieve user principals
            SecurityService security = (SecurityService) Application.getInstance().getService(SecurityService.class);
            Collection principalList = security.getUserPrincipalIds(userId);
            String[] principalArray = (String[]) principalList.toArray(new String[0]);

            // retrieve object
            ContentObject contentObject = dao.selectById(key);
            Collection results = dao.selectPermissionsByUser(principalArray, key);
            Collection permissions = new ArrayList();
            for (Iterator i = results.iterator(); i.hasNext();) {
                Map tmpMap = (Map) i.next();
                String permissionId = (String) tmpMap.get("permissionId");
                if (permissionId != null) {
                    permissions.add(permissionId);
                }
            }

            // check prerequisites

            // create permission
            Class coClass;
            try {
                coClass = Class.forName(contentObject.getClassName());
            }
            catch (Exception e) {
                coClass = contentObject.getClass();
            }
            Class[] allowedClasses = getAllowedClasses(coClass);
            if (allowedClasses == null || allowedClasses.length == 0) {
                permissions.remove(ContentManager.USE_CASE_CREATE);
            }


            // edit permission
            if (!userId.equals(contentObject.getCheckOutUserId()) && (contentObject.isCheckedOut() || contentObject.isSubmitted() || contentObject.isArchived())) {
                permissions.remove(ContentManager.USE_CASE_CHECKOUT);
            }

            // undo checkout permission
            if (userId.equals(contentObject.getCheckOutUserId()) && !"1".equals(contentObject.getVersion())) {
                if (!permissions.contains(ContentManager.USE_CASE_UNDO_CHECKOUT))
                    permissions.add(ContentManager.USE_CASE_UNDO_CHECKOUT);
            }
            else if (!contentObject.isCheckedOut() || "1".equals(contentObject.getVersion())) {
                permissions.remove(ContentManager.USE_CASE_UNDO_CHECKOUT);
            }

            // approve permission
            if (!contentObject.isSubmitted()) {
                permissions.remove(ContentManager.USE_CASE_APPROVE);
            }

            // publish permission
            if (!contentObject.isPublished() && !contentObject.isApproved()) {
                permissions.remove(ContentManager.USE_CASE_PUBLISH);
            }

            // archive permission
            if (!contentObject.isArchived() && !contentObject.isPublished() && !contentObject.isApproved()) {
                permissions.remove(ContentManager.USE_CASE_ARCHIVE);
            }

            // delete permission
            if (CONTENT_TREE_ROOT_ID.equals(contentObject.getId())) {
                permissions.remove(ContentManager.USE_CASE_DELETE);
            }

            return permissions;
        }
        catch (Exception e) {
            log.error(e.toString(), e);
            throw new ContentException(e.toString());
        }
    }

    /**
     * Retrieves the ACL for a content object.
     * @param key
     * @param roleArray
     * @param user
     * @return A Collection of ContentAcl objects.
     * @throws ContentException
     */
    public Collection viewAcl(String key, String[] roleArray, User user) throws ContentException {
        try {
            // retrieve content object
            ContentManagerDao contentManagerDao = (ContentManagerDao) getDao();
            ContentObject contentObject = contentManagerDao.selectById(key);

/*
            // check permission
            if (!hasPermission(contentObject, user.getId(), USE_CASE_ACL_VIEW))
                throw new ContentPermissionException("No permission");
*/

            ContentManagerDao dao = (ContentManagerDao) getDao();
            return dao.selectRolePrincipals(key, roleArray, null);
        }
/*
        catch (ContentException e) {
            throw e;
        }
*/
        catch (Exception e) {
            log.error("Unable to retrieve ACL for content " + key, e);
            throw new ContentException(e.toString());
        }
    }

    /**
     * Retrieves a Map of ContentObjects for a principal (user or group) with properties specifying the roles for each object.
     * The roles include those inherited from groups the user belongs to.
     * Each content object will have properties like "user_[roleName]" for user specific role and "group_[roleName]"
     * for group specific roles e.g. contentObject.getProperty("group_manager") will not be null if the
     * user belongs to a group that has "manager" role. A property "aclUserRole" will also be set to Boolean.TRUE if
     * the user has a user specific role for the content. Similarly, "aclGroupRole" will be set for
     * group inherited roles.
     * @param classes
     * @param roleArray
     * @param principalId
     * @return A Map of id=ContentObject.
     */
    public Map viewAclByPrincipal(String[] classes, String[] roleArray, String principalId) throws ContentException {
        try {
            Application app = Application.getInstance();
            SecurityService security = (SecurityService)app.getService(SecurityService.class);

            // determine user groups
            Collection principalIdList = security.getUserPrincipalIds(principalId);
            String[] principalIdArray = (String[])principalIdList.toArray(new String[0]);

            // get acls for user and associated groups
            ContentManagerDao dao = (ContentManagerDao) getDao();
            Collection aclList = dao.selectRolePrincipals(null, roleArray, principalIdArray);

            // get content list
            Collection coList = viewList(null, classes, null, null, null, null, null, null, null, Boolean.FALSE, null, "name", false, 0, -1, null, null);

            // put content objects in map (lookup table)
            Map contentMap = new SequencedHashMap();
            for (Iterator i=coList.iterator(); i.hasNext();) {
                ContentObject co = (ContentObject)i.next();
                contentMap.put(co.getId(), co);
            }

            // populate map with acl
            for (Iterator i=aclList.iterator(); i.hasNext();) {
                ContentAcl acl = (ContentAcl)i.next();
                String id = acl.getObjectId();
                String role = acl.getRole();
                String prinId = acl.getPrincipalId();
                ContentObject co = (ContentObject)contentMap.get(id);
                if (co != null) {
                    if (principalId.equals(prinId)) {
                        // user acl
                        co.setProperty("aclUserRole", Boolean.TRUE);
                        co.setProperty("user_" + role, principalId);
                    }
                    else {
                        // group acl
                        co.setProperty("aclGroupRole", Boolean.TRUE);
                        // append to existing
                        String tmp = (String)co.getProperty("group_" + role);
                        if (tmp == null) {
                            tmp = "";
                        }
                        tmp += "," + prinId;
                        co.setProperty("group_" + role, tmp);
                    }
                    contentMap.put(co.getId(), co);
                }
            }

            return contentMap;
        }
        catch (Exception e) {
            log.error("Unable to retrieve ACL for principal " + principalId, e);
            throw new ContentException(e.toString());
        }

    }

    public void inheritAcl(String key, boolean recursive, User user) throws ContentException {
        try {
            // retrieve content object
            ContentManagerDao contentManagerDao = (ContentManagerDao) getDao();
            ContentObject contentObject = contentManagerDao.selectById(key);

            // check permission
            if (!hasPermission(contentObject, user.getId(), USE_CASE_ACL_UPDATE))
                throw new ContentPermissionException("No permission");

            // locate id to inherit from
            if (contentObject.getParentId() == null)
                return;
            contentObject = viewPath(key, user);
            ContentObject tmp = contentObject.getParent();
            String aclId = null;
            while (tmp != null) {
                aclId = tmp.getAclId();
                if (aclId != null)
                    break;
                tmp = tmp.getParent();
            }
            if (aclId == null) {
                throw new ContentException("Permissions cannot be inherited, ACL not found");
            }

            // update
            ContentManagerDao dao = (ContentManagerDao) getDao();
            contentObject.setAclId(aclId);
            dao.updateStatus(contentObject);
            dao.updateRolePrincipals(key, new ContentAcl[0]);

            // propogate
            ContentObject root = viewTree(key, null, null, user);
            propagateAclId(dao, root, aclId, recursive);

            audit(contentObject.getId(), USE_CASE_ACL_UPDATE, null, user);
        }
        catch (Exception e) {
            log.error(e.toString(), e);
            throw new ContentException(e.toString());
        }
    }

    public void updateAcl(String key, ContentAcl[] aclArray, boolean recursive, User user) throws ContentException {
        try {
            // retrieve content object
            ContentManagerDao contentManagerDao = (ContentManagerDao) getDao();
            ContentObject contentObject = contentManagerDao.selectById(key);

            // check permission
            if (!hasPermission(contentObject, user.getId(), USE_CASE_ACL_UPDATE)) {
                // check for global permission
                SecurityService security = (SecurityService) Application.getInstance().getService(SecurityService.class);
                if (!security.hasPermission(user.getId(), ContentManager.PERMISSION_MANAGE_CONTENT_ACL, getClass().getName(), null)) {
                    throw new ContentPermissionException("No permission");
                }
            }

            // update
            ContentManagerDao dao = (ContentManagerDao) getDao();
            contentObject.setAclId(contentObject.getId());
            dao.updateStatus(contentObject);
            dao.updateRolePrincipals(key, aclArray);

            // propogate
            ContentObject root = viewTree(contentObject.getId(), null, null, user);
            propagateAclId(dao, root, contentObject.getAclId(), recursive);

            audit(contentObject.getId(), USE_CASE_ACL_UPDATE, null, user);
        }
        catch (ContentException e) {
            throw e;
        }
        catch (Exception e) {
            log.error(e.toString(), e);
            throw new ContentException(e.toString());
        }
    }

    public void updateAcl(String key, String principalId, String role, User user) throws ContentException {
        try {
            // retrieve content object
            ContentManagerDao contentManagerDao = (ContentManagerDao) getDao();
            ContentObject contentObject = contentManagerDao.selectById(key);


            // get ACL for object
            Collection aclList = null;
            if (contentObject.getId().equals(contentObject.getAclId())) {
                aclList = contentManagerDao.selectRolePrincipals(key, getRoleArray(), null);
            }
            else {
                aclList = contentManagerDao.selectRolePrincipals(contentObject.getAclId(), getRoleArray(), null);
            }

            // remove duplicate ACL, ensure manager role is set
            boolean isManager = false;
            for (Iterator i=aclList.iterator(); i.hasNext();) {
                ContentAcl tmp = (ContentAcl)i.next();
                if (key.equals(tmp.getObjectId()) && principalId.equals(tmp.getPrincipalId())) {
                    if ("manager".equals(tmp.getRole())) {
                        isManager = true;
                    }
                    else {
                        i.remove();
                    }
                }
                tmp.setObjectId(key);
            }

            if (!isManager && role != null && role.trim().length() > 0) {
                // add new ACL
                ContentAcl acl = new ContentAcl();
                acl.setObjectId(key);
                acl.setPrincipalId(principalId);
                acl.setRole(role);
                aclList.add(acl);

            }

            // update
            ContentAcl[] aclArray = (ContentAcl[])aclList.toArray(new ContentAcl[0]);
            updateAcl(key, aclArray, false, user);
        }
        catch (Exception e) {
            Log.getLog(getClass()).error("Error updating acl for content: " + key, e);
            throw new ContentException(e.toString());
        }

    }

    protected void propagateAclId(ContentManagerDao dao, ContentObject contentObject, String aclId, boolean recursive) {
        // get children
        Collection keyList = new ArrayList();
        for (Iterator i = contentObject.getChildren().iterator(); i.hasNext();) {
            ContentObject child = (ContentObject) i.next();
            if (!child.getAclId().equals(aclId) && (recursive || !child.getId().equals(child.getAclId()))) { // no specified acl, so inherit
                keyList.add(child.getId());
                child.setAclId(aclId);
            }
        }
        // do update
        if (keyList.size() > 0) {
            try {
                Map propertyMap = new HashMap();
                propertyMap.put("aclId", aclId);
                String[] keyArray = (String[]) keyList.toArray(new String[0]);
                dao.updateStatus(keyArray, propertyMap);
                dao.deleteRolePrincipals(keyArray);
            }
            catch (DaoException e) {
                log.error("propagateAclId: " + e.toString());
            }
        }
        // propagate
        for (Iterator i = contentObject.getChildren().iterator(); i.hasNext();) {
            ContentObject child = (ContentObject) i.next();
            if (recursive || !child.getId().equals(child.getAclId())) { // no specified acl, so propagate
                propagateAclId(dao, child, contentObject.getAclId(), recursive);
            }
        }
    }

//-- View Methods

    public ContentObject view(String key, User user) throws DataObjectNotFoundException, ContentException {
        try {
            // retrieve content object
            ContentManagerDao contentManagerDao = (ContentManagerDao) getDao();
            ContentObject contentObject = contentManagerDao.selectById(key);

            // check permission
/*
            if (!hasPermission(contentObject, user.getId(), USE_CASE_PREVIEW))
                throw new ContentPermissionException("No permission");
*/

            // retrieve module-specific object
            Application application = Application.getInstance();
            Class moduleClass = ContentUtil.getModuleClassFromKey(key);
            ContentModule module = (ContentModule) application.getModule(moduleClass);
            ContentModuleDao contentModuleDao = (ContentModuleDao) module.getDao();
            ContentObject fullObject = contentModuleDao.selectByVersion(contentObject.getId(), contentObject.getVersion());

            // copy values from object to fullObject
            ContentUtil.copyStatusAttributes(fullObject, contentObject);

            // retrieve keywords
            Collection kwList = contentManagerDao.selectKeywordsById(key);
            fullObject.setKeywords(ContentUtil.keywordCollectionToString(kwList));

            return fullObject;
        }
        catch (DataObjectNotFoundException e) {
            throw e;
        }
        catch (Exception e) {
            log.error(e.toString(), e);
            throw new ContentException(e.toString());
        }
    }

    public ContentObject viewVersion(String key, String version, User user) throws DataObjectNotFoundException, ContentException {
        try {
            // retrieve content object
            ContentManagerDao contentManagerDao = (ContentManagerDao) getDao();
            ContentObject contentObject = contentManagerDao.selectById(key);

            // check permission
            if (!hasPermission(contentObject, user.getId(), USE_CASE_PREVIEW_VERSION))
                throw new ContentPermissionException("No permission");

            // retrieve module-specific object
            Application application = Application.getInstance();
            Class moduleClass = ContentUtil.getModuleClassFromKey(key);
            ContentModule module = (ContentModule) application.getModule(moduleClass);
            ContentModuleDao contentModuleDao = (ContentModuleDao) module.getDao();
            ContentObject fullObject = contentModuleDao.selectByVersion(contentObject.getId(), version);

            // copy values from object to fullObject
            ContentUtil.copyStatusAttributes(fullObject, contentObject);

            return fullObject;
        }
        catch (DataObjectNotFoundException e) {
            throw e;
        }
        catch (ContentException e) {
            throw e;
        }
        catch (Exception e) {
            log.error(e.toString(), e);
            throw new ContentException(e.toString());
        }
    }

    public ContentObject viewPath(String key, User user) throws DataObjectNotFoundException, ContentException {
        try {
            // retrieve content object
            ContentManagerDao contentManagerDao = (ContentManagerDao) getDao();
            ContentObject contentObject = contentManagerDao.selectById(key);

            // check permission

            // get path to parent
            ContentObject tmpObject = contentObject;
            while (tmpObject.getParentId() != null && tmpObject.getParentId().trim().length() > 0) {
                ContentObject parent = contentManagerDao.selectById(tmpObject.getParentId());
                tmpObject.setParent(parent);
                tmpObject = parent;
            }

            return contentObject;
        }
        catch (DataObjectNotFoundException e) {
            throw e;
        }
        catch (Exception e) {
            log.error(e.toString(), e);
            throw new ContentException(e.toString());
        }
    }

    /**
     *
     * @param key Content ID for the root of the tree.
     * @param classes Array of class names required, null for all classes.
     * @param permission If not null, only content objects whereby users have the appropriate permission are returned.
     * @param user
     * @return A DefaultContentObject that represents the root of the content tree.
     * @throws kacang.model.DataObjectNotFoundException
     * @throws com.tms.cms.core.model.ContentException
     */
    public ContentObject viewTree(String key, String[] classes, String permission, User user) throws DataObjectNotFoundException, ContentException {
        try {
            // retrieve content object
            ContentManagerDao contentManagerDao = (ContentManagerDao) getDao();
            ContentObject root = contentManagerDao.selectById(key);

            // check permission

            // retrieve list of all objects
            Collection objects = viewList(null, classes, null, null, null, null, null, null, null, Boolean.FALSE, null, null, false, 0, -1, permission, user);
            Map objectMap = new SequencedHashMap();
            for (Iterator j = objects.iterator(); j.hasNext();) {
                ContentObject co = (ContentObject) j.next();
                if (co.getId() != null && co.getId().trim().length() > 0) {
                	objectMap.put(co.getId(), co);
                }
            }
            objectMap.put(root.getId(), root);

            // traverse list and populate tree
            for (Iterator i = objectMap.values().iterator(); i.hasNext();) {
                ContentObject co = (ContentObject) i.next();
                if (co.getParentId() != null) {
                    ContentObject parent = (ContentObject) objectMap.get(co.getParentId());
                    if (parent != null) {
                        if (!parent.containsChild(co))
                            parent.addChild(co);
                        co.setParent(parent);
                    }
                }
            }

            return root;
        }
        catch (DataObjectNotFoundException e) {
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
     * @param includeContents to return content objects complete with the "contents" property
     * @param permission If not null, only content objects whereby users have the appropriate permission are returned.
     * @param user
     * @return A DefaultContentObject that represents the root of the content tree.
     * @throws kacang.model.DataObjectNotFoundException
     * @throws com.tms.cms.core.model.ContentException
     */
    public ContentObject viewTreeWithOrphans(String key, String[] classes, boolean includeContents, String permission, User user) throws DataObjectNotFoundException, ContentException {
        try {
            // get root content object
            ContentManagerDao dao = (ContentManagerDao) getDao();
            ContentObject root = dao.selectById(key);

            // retrieve list of all objects
            Collection objects = viewList(null, classes, null, null, null, null, null, null, null, Boolean.FALSE, null, null, false, 0, -1, null, user);
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
            Collection validObjects = (!includeContents) ?
                    viewList(null, classes, null, null, null, null, null, null, null, Boolean.FALSE, null, null, false, 0, -1, permission, user) :
                    viewListWithContents(null, classes, null, null, null, null, null, null, null, Boolean.FALSE, null, null, false, 0, -1, permission, user);
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
        catch (DataObjectNotFoundException e) {
            throw e;
        }
        catch (Exception e) {
            log.error(e.toString(), e);
            throw new ContentException(e.toString());
        }
    }

    public ContentObject viewTreeWithContents(String key, String[] classes, String permission, User user) throws DataObjectNotFoundException, ContentException {
        try {
            // retrieve content object
            ContentManagerDao contentManagerDao = (ContentManagerDao) getDao();
            ContentObject root = contentManagerDao.selectById(key);

            // check permission

            // retrieve list of all objects
            Collection objects = viewListWithContents(null, classes, null, null, null, null, null, null, null, Boolean.FALSE, null, null, false, 0, -1, permission, user);
            Map objectMap = new SequencedHashMap();
            for (Iterator j = objects.iterator(); j.hasNext();) {
                ContentObject co = (ContentObject) j.next();
                if (co.getId() != null && co.getId().trim().length() > 0) {
                	objectMap.put(co.getId(), co);
                }
            }
            objectMap.put(root.getId(), root);

            // traverse list and populate tree
            for (Iterator i = objectMap.values().iterator(); i.hasNext();) {
                ContentObject co = (ContentObject) i.next();
                if (co.getParentId() != null) {
                    ContentObject parent = (ContentObject) objectMap.get(co.getParentId());
                    if (parent != null) {
                        if (!parent.containsChild(co))
                            parent.addChild(co);
                        co.setParent(parent);
                    }
                }
            }

            return root;
        }
        catch (DataObjectNotFoundException e) {
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
     * @param name A search string to filter by in the content name.
     * @param parentId The ID for the parent content object.
     * @param classes An array of class names denoting the specific content object classes that are required.
     * @param checkedOut Boolean.TRUE for checked out content, Boolean.FALSE for those not checked out, null for any.
     * @param submitted Boolean.TRUE for submitted content, Boolean.FALSE for those not submitted, null for any.
     * @param approved Boolean.TRUE for approved content, Boolean.FALSE for those not approved, null for any.
     * @param archived Boolean.TRUE for archived content, Boolean.FALSE for those not archived, null for any.
     * @param published Boolean.TRUE for published content, Boolean.FALSE for those not published, null for any.
     * @param deleted Boolean.TRUE for deleted content, Boolean.FALSE for those not deleted, null for any.
     * @param checkOutUserId If not null, returns only content checked out by this user.
     * @param sort Property to sort by, defaults to sorting by ordering followed by name.
     * @param desc true if sorted in descending order.
     * @param start Starting index of the results to return.
     * @param rows Number of results to return
     * @param permission If not null, only content objects whereby users have the appropriate permission are returned.
     * @param user The current user requesting for the listing.
     * @return A Collection of DefaultContentObject objects.
     * @throws ContentException
     */
    public Collection viewList(String[] keys, String[] classes, String name, String parentId, Boolean checkedOut, Boolean submitted, Boolean approved, Boolean archived, Boolean published, Boolean deleted, String checkOutUserId, String sort, boolean desc, int start, int rows, String permission, User user) throws ContentException {
        return viewListByDate(keys, classes, name, parentId, null, null, checkedOut, submitted, approved, archived, published, deleted, checkOutUserId, sort, desc, start, rows, permission, user);
    }

    /**
     * Returns a Collection of Content Objects based on criteria that are specified through the
     * method parameters. This method does not return the objects' contents property.
     * For any of the parameters, setting the value to null will ignore that criteria.
     * @param keys An array of content IDs to retrieve specifying the subset to retrieve.
     * @param name A search string to filter by in the content name.
     * @param parentId The ID for the parent content object.
     * @param classes An array of class names denoting the specific content object classes that are required.
     * @param fromDate Starting date to filter by, null for any
     * @param toDate End date to filter by, null for any
     * @param checkedOut Boolean.TRUE for checked out content, Boolean.FALSE for those not checked out, null for any.
     * @param submitted Boolean.TRUE for submitted content, Boolean.FALSE for those not submitted, null for any.
     * @param approved Boolean.TRUE for approved content, Boolean.FALSE for those not approved, null for any.
     * @param archived Boolean.TRUE for archived content, Boolean.FALSE for those not archived, null for any.
     * @param published Boolean.TRUE for published content, Boolean.FALSE for those not published, null for any.
     * @param deleted Boolean.TRUE for deleted content, Boolean.FALSE for those not deleted, null for any.
     * @param checkOutUserId If not null, returns only content checked out by this user.
     * @param sort Property to sort by, defaults to sorting by ordering followed by name.
     * @param desc true if sorted in descending order.
     * @param start Starting index of the results to return.
     * @param rows Number of results to return
     * @param permission If not null, only content objects whereby users have the appropriate permission are returned.
     * @param user The current user requesting for the listing.
     * @return A Collection of DefaultContentObject objects.
     * @throws ContentException
     */
    public Collection viewListByDate(String[] keys, String[] classes, String name, String parentId, Date fromDate, Date toDate, Boolean checkedOut, Boolean submitted, Boolean approved, Boolean archived, Boolean published, Boolean deleted, String checkOutUserId, String sort, boolean desc, int start, int rows, String permission, User user) throws ContentException {
        try {
            ContentManagerDao dao = (ContentManagerDao) getDao();

            if (permission != null) {
                // retrieve user principals
                SecurityService security = (SecurityService) Application.getInstance().getService(SecurityService.class);
                Collection principalList = security.getUserPrincipalIds(user.getId());
                String[] principalArray = (String[]) principalList.toArray(new String[0]);

                // retrieve list of objects
                Collection results = dao.selectByPermission(principalArray, permission, keys, classes, name, parentId, fromDate, toDate, checkedOut, submitted, approved, archived, published, deleted, checkOutUserId, false, sort, desc, start, rows);
                return results;
            }
            else {
                // retrieve list of objects
                Collection results = dao.selectByCriteria(keys, classes, name, parentId, fromDate, toDate, checkedOut, submitted, approved, archived, published, deleted, checkOutUserId, false, sort, desc, start, rows);
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
     * method parameters. This method returns the objects complete with their contents property.
     * For any of the parameters, setting the value to null will ignore that criteria.
     * @param keys An array of content IDs to retrieve specifying the subset to retrieve.
     * @param name A search string to filter by in the content name.
     * @param parentId The ID for the parent content object.
     * @param classes An array of class names denoting the specific content object classes that are required.
     * @param checkedOut Boolean.TRUE for checked out content, Boolean.FALSE for those not checked out, null for any.
     * @param submitted Boolean.TRUE for submitted content, Boolean.FALSE for those not submitted, null for any.
     * @param approved Boolean.TRUE for approved content, Boolean.FALSE for those not approved, null for any.
     * @param archived Boolean.TRUE for archived content, Boolean.FALSE for those not archived, null for any.
     * @param published Boolean.TRUE for published content, Boolean.FALSE for those not published, null for any.
     * @param deleted Boolean.TRUE for deleted content, Boolean.FALSE for those not deleted, null for any.
     * @param checkOutUserId If not null, returns only content checked out by this user.
     * @param sort Property to sort by, defaults to sorting by ordering followed by name.
     * @param desc true if sorted in descending order.
     * @param start Starting index of the results to return.
     * @param rows Number of results to return
     * @param permission If not null, only content objects whereby users have the appropriate permission are returned.
     * @param user The current user requesting for the listing.
     * @return A Collection of DefaultContentObject objects.
     * @throws ContentException
     */
    public Collection viewListWithContents(String[] keys, String[] classes, String name, String parentId, Boolean checkedOut, Boolean submitted, Boolean approved, Boolean archived, Boolean published, Boolean deleted, String checkOutUserId, String sort, boolean desc, int start, int rows, String permission, User user) throws ContentException {
        try {
            ContentManagerDao dao = (ContentManagerDao) getDao();

            if (permission != null) {
                // retrieve user principals
                SecurityService security = (SecurityService) Application.getInstance().getService(SecurityService.class);
                Collection principalList = security.getUserPrincipalIds(user.getId());
                String[] principalArray = (String[]) principalList.toArray(new String[0]);

                // retrieve list of objects
                Collection results = dao.selectByPermission(principalArray, permission, keys, classes, name, parentId, null, null, checkedOut, submitted, approved, archived, published, deleted, checkOutUserId, true, sort, desc, start, rows);
                return results;
            }
            else {
                // retrieve list of objects
                Collection results = dao.selectByCriteria(keys, classes, name, parentId, null, null, checkedOut, submitted, approved, archived, published, deleted, checkOutUserId, true, sort, desc, start, rows);
                return results;
            }
        }
        catch (Exception e) {
            log.error(e.toString(), e);
            throw new ContentException(e.toString());
        }
    }

    /**
     * Returns the number of Content Objects based on criteria that are specified through the
     * method parameters.
     * For any of the parameters, setting the value to null will ignore that criteria.
     * @param keys An array of content IDs to retrieve specifying the subset to retrieve.
     * @param name A search string to filter by in the content name.
     * @param parentId The ID for the parent content object.
     * @param classes An array of class names denoting the specific content object classes that are required.
     * @param checkedOut Boolean.TRUE for checked out content, Boolean.FALSE for those not checked out, null for any.
     * @param submitted Boolean.TRUE for submitted content, Boolean.FALSE for those not submitted, null for any.
     * @param approved Boolean.TRUE for approved content, Boolean.FALSE for those not approved, null for any.
     * @param archived Boolean.TRUE for archived content, Boolean.FALSE for those not archived, null for any.
     * @param published Boolean.TRUE for published content, Boolean.FALSE for those not published, null for any.
     * @param deleted Boolean.TRUE for deleted content, Boolean.FALSE for those not deleted, null for any.
     * @param checkOutUserId If not null, returns only content checked out by this user.
     * @param permission If not null, only content objects whereby users have the appropriate permission are returned.
     * @param user The current user requesting for the listing.
     * @return A Collection of DefaultContentObject objects.
     * @throws ContentException
     */
    public int viewCount(String[] keys, String[] classes, String name, String parentId, Boolean checkedOut, Boolean submitted, Boolean approved, Boolean archived, Boolean published, Boolean deleted, String checkOutUserId, String permission, User user) throws ContentException {
        return viewCountByDate(keys, classes, name, parentId, null, null, checkedOut, submitted, approved, archived, published, deleted, checkOutUserId, permission, user);
    }

    /**
     * Returns the number of Content Objects based on criteria that are specified through the
     * method parameters.
     * For any of the parameters, setting the value to null will ignore that criteria.
     * @param keys An array of content IDs to retrieve specifying the subset to retrieve.
     * @param name A search string to filter by in the content name.
     * @param parentId The ID for the parent content object.
     * @param classes An array of class names denoting the specific content object classes that are required.
     * @param fromDate Starting date to filter by, null for any
     * @param toDate End date to filter by, null for any
     * @param checkedOut Boolean.TRUE for checked out content, Boolean.FALSE for those not checked out, null for any.
     * @param submitted Boolean.TRUE for submitted content, Boolean.FALSE for those not submitted, null for any.
     * @param approved Boolean.TRUE for approved content, Boolean.FALSE for those not approved, null for any.
     * @param archived Boolean.TRUE for archived content, Boolean.FALSE for those not archived, null for any.
     * @param published Boolean.TRUE for published content, Boolean.FALSE for those not published, null for any.
     * @param deleted Boolean.TRUE for deleted content, Boolean.FALSE for those not deleted, null for any.
     * @param checkOutUserId If not null, returns only content checked out by this user.
     * @param permission If not null, only content objects whereby users have the appropriate permission are returned.
     * @param user The current user requesting for the listing.
     * @return A Collection of DefaultContentObject objects.
     * @throws ContentException
     */
    public int viewCountByDate(String[] keys, String[] classes, String name, String parentId, Date fromDate, Date toDate, Boolean checkedOut, Boolean submitted, Boolean approved, Boolean archived, Boolean published, Boolean deleted, String checkOutUserId, String permission, User user) throws ContentException {
        try {
            ContentManagerDao dao = (ContentManagerDao) getDao();

            if (permission != null) {
                // retrieve user principals
                SecurityService security = (SecurityService) Application.getInstance().getService(SecurityService.class);
                Collection principalList = security.getUserPrincipalIds(user.getId());
                String[] principalArray = (String[]) principalList.toArray(new String[0]);

                // retrieve list of objects
                int total = dao.selectCountByPermission(principalArray, permission, keys, classes, name, parentId, fromDate, toDate, checkedOut, submitted, approved, archived, published, deleted, checkOutUserId);
                return total;
            }
            else {
                // retrieve list of objects
                int total = dao.selectCountByCriteria(keys, classes, name, parentId, fromDate, toDate, checkedOut, submitted, approved, archived, published, deleted, checkOutUserId);
                return total;
            }
        }
        catch (Exception e) {
            log.error(e.toString(), e);
            throw new ContentException(e.toString());
        }
    }

    /**
     *
     * @param key
     * @param sort
     * @param desc
     * @param start
     * @param rows
     * @param user
     * @return A Collection of DefaultContentObject objects.
     * @throws ContentException
     */
    public Collection viewHistory(String key, String sort, boolean desc, int start, int rows, User user) throws ContentException {
        try {
            // retrieve content object
            ContentManagerDao contentManagerDao = (ContentManagerDao) getDao();
            ContentObject contentObject = contentManagerDao.selectById(key);

            // check permission
            if (!hasPermission(contentObject, user.getId(), USE_CASE_HISTORY))
                throw new ContentPermissionException("No permission");

            // retrieve list of objects
            Collection results = contentManagerDao.selectVersions(key, sort, desc, start, rows);
            return results;
        }
        catch (ContentException e) {
            throw e;
        }
        catch (Exception e) {
            log.error(e.toString(), e);
            throw new ContentException(e.toString());
        }
    }

    public int viewHistoryCount(String key, User user) throws ContentException {
        try {
            // retrieve content object
            ContentManagerDao contentManagerDao = (ContentManagerDao) getDao();
            ContentObject contentObject = contentManagerDao.selectById(key);

            // check permission
            if (!hasPermission(contentObject, user.getId(), USE_CASE_HISTORY))
                throw new ContentPermissionException("No permission");

            // retrieve list of objects
            return contentManagerDao.selectVersionsCount(key);
        }
        catch (ContentException e) {
            throw e;
        }
        catch (Exception e) {
            log.error(e.toString(), e);
            throw new ContentException(e.toString());
        }
    }

    /**
     *
     * @param permissionId
     * @param keys
     * @param user
     * @return A Map of contentID(String)=number of children(Integer)
     * @throws ContentException
     */
    public Map viewChildrenCount(String permissionId, String[] keys, Boolean checkedOut, Boolean submitted, Boolean approved, Boolean archived, Boolean published, Boolean deleted, User user) throws ContentException {
        try {
            // retrieve user principals
            SecurityService security = (SecurityService) Application.getInstance().getService(SecurityService.class);
            Collection principalList = security.getUserPrincipalIds(user.getId());
            String[] principalArray = (String[]) principalList.toArray(new String[0]);

            // retrieve count
            ContentManagerDao contentManagerDao = (ContentManagerDao) getDao();
            return contentManagerDao.selectChildrenCount(principalArray, permissionId, keys, checkedOut, submitted, approved, archived, published, deleted);
        }
        catch (Exception e) {
            log.error(e.toString(), e);
            throw new ContentException(e.toString());
        }

    }

//-- Removal methods

    /**
     * Deletes a Content Object, but not permanently. The content is marked as deleted.
     * @param key
     * @param recursive
     * @param user
     * @throws ContentException
     */
    public void delete(String key, boolean recursive, User user) throws ContentException {
        try {
            // retrieve content object
            ContentManagerDao dao = (ContentManagerDao) getDao();
            ContentObject contentObject = dao.selectById(key);

            // check for root
            if (CONTENT_TREE_ROOT_ID.equals(key)) {
                throw new ContentException("Content tree root cannot be deleted");
            }

            // check permission
            if (!hasPermission(contentObject, user.getId(), USE_CASE_DELETE))
                throw new ContentPermissionException("No permission");

            // set delete flag and update
            if (recursive) {
                contentObject = viewTreeWithOrphans(contentObject.getId(), null, false, USE_CASE_DELETE, user);
                audit(contentObject.getId(), USE_CASE_DELETE_RECUR, null, user);
            }
            else {
                audit(contentObject.getId(), USE_CASE_DELETE, null, user);
            }
            propagateDelete(contentObject, recursive, user);
        }
        catch (ContentException e) {
            throw e;
        }
        catch (Exception e) {
            log.error(e.toString(), e);
            throw new ContentException(e.toString());
        }
    }

    protected void propagateDelete(ContentObject contentObject, boolean recursive, User user) throws ContentException {
        try {
            // check permission
            if (!contentObject.isDeleted()) {
                if (contentObject.isPublished()) {
                    // withdraw
                    withdraw(contentObject.getId(), false, user);
                }

                ContentManagerDao dao = (ContentManagerDao) getDao();
                contentObject.setDeleted(true);
                contentObject.setPublished(false);
                contentObject.setPublishDate(null);
                contentObject.setPublishUserId(null);
                contentObject.setPublishUser(null);
                contentObject.setPublishVersion(null);
                contentObject.setStartDate(null);
                contentObject.setEndDate(null);
                if (!"1".equals(contentObject.getVersion())) {
                    contentObject.setCheckedOut(false);
                    contentObject.setCheckOutDate(null);
                    contentObject.setCheckOutUser(null);
                    contentObject.setCheckOutUserId(null);
                }
                dao.updateFullStatus(contentObject);

                // unindex object
                unindex(contentObject);
            }

            // propagate?
            if (recursive) {
                for (Iterator i = contentObject.getChildren().iterator(); i.hasNext();) {
                    ContentObject tmpObj = (ContentObject) i.next();
                    propagateDelete(tmpObj, true, user);
                }
            }
        }
        catch (Exception e) {
            log.error(e.toString(), e);
            throw new ContentException(e.toString());
        }
    }

    /**
     * Restores previously deleted content. A Content Object can be undeleted
     * if the user has the global PERMISSION_MANAGE_RECYCLE_BIN permission,
     * or has content-specific USE_CASE_DESTROY permission
     * @param keys
     * @param recursive
     * @param user
     * @throws ContentException
     */
    public void undelete(String[] keys, boolean recursive, User user) throws ContentException {
        try {
            if (keys == null || keys.length == 0) {
                return;
            }

            // check user permission
            Application app = Application.getInstance();
            SecurityService security = (SecurityService)app.getService(SecurityService.class);
            boolean manageRecycleBin = security.hasPermission(user.getId(), ContentManager.PERMISSION_MANAGE_RECYCLE_BIN, getClass().getName(), null);

            for (int i=0; i<keys.length; i++) {
                String key = keys[i];

                // retrieve content object
                ContentManagerDao dao = (ContentManagerDao) getDao();
                ContentObject contentObject = dao.selectById(key);

                // check permission
                if (!manageRecycleBin && !hasPermission(contentObject, user.getId(), USE_CASE_UNDELETE))
                    throw new ContentPermissionException("No permission");

                // unset delete flag and update
                if (recursive) {
                    if (!manageRecycleBin) {
                        contentObject = viewTreeWithOrphans(contentObject.getId(), null, false, USE_CASE_UNDELETE, user);
                    }
                    else {
                        contentObject = viewTree(contentObject.getId(), null, null, user);
                    }
                    audit(contentObject.getId(), USE_CASE_UNDELETE_RECUR, null, user);
                }
                else {
                    audit(contentObject.getId(), USE_CASE_UNDELETE, null, user);
                }
                propagateUndelete(contentObject, manageRecycleBin, recursive, user);
            }
        }
        catch (ContentException e) {
            throw e;
        }
        catch (Exception e) {
            log.error(e.toString(), e);
            throw new ContentException(e.toString());
        }
    }

    protected void propagateUndelete(ContentObject contentObject, boolean manageRecycleBin, boolean recursive, User user) throws ContentException {
        try {
            // check permission
            if (contentObject.isDeleted()) {
                ContentManagerDao dao = (ContentManagerDao) getDao();
                contentObject.setDeleted(false);
                dao.updateStatus(contentObject);

                // index object
                index(contentObject);
            }

            // propagate?
            if (recursive) {
                for (Iterator i = contentObject.getChildren().iterator(); i.hasNext();) {
                    ContentObject tmpObj = (ContentObject) i.next();
                    propagateUndelete(tmpObj, manageRecycleBin, true, user);
                }
            }
        }
        catch (Exception e) {
            log.error(e.toString(), e);
            throw new ContentException(e.toString());
        }
    }

    /**
     * Destroys content permanently. A Content Object can be destroyed
     * only if the user has the global PERMISSION_MANAGE_RECYCLE_BIN permission.
     * @param keys
     * @param user
     * @throws DataObjectNotFoundException
     * @throws ContentException
     */
    public void destroy(String[] keys, User user) throws DataObjectNotFoundException, ContentException {
        try {
            if (keys == null || keys.length == 0) {
                return;
            }

            // check user permission
            Application app = Application.getInstance();
            SecurityService security = (SecurityService)app.getService(SecurityService.class);
            boolean manageRecycleBin = security.hasPermission(user.getId(), ContentManager.PERMISSION_MANAGE_RECYCLE_BIN, getClass().getName(), null);
            if (!manageRecycleBin) {
                return;
            }

            for (int i=0; i<keys.length; i++) {
                String key = keys[i];

                // retrieve content object
                ContentManagerDao dao = (ContentManagerDao) getDao();
                ContentObject contentObject = dao.selectById(key);

                // check for root
                if (CONTENT_TREE_ROOT_ID.equals(key)) {
                    throw new ContentException("Content tree root cannot be destroyed");
                }

/*
                // check permission
                if (!hasPermission(contentObject, user.getId(), USE_CASE_DESTROY))
                    throw new ContentPermissionException("No permission");
*/

                // get object
                if (true) {
                    contentObject = viewTree(contentObject.getId(), null, null, user);
                    audit(contentObject.getId(), USE_CASE_DESTROY, null, user);
                }
                propagateDestroy(contentObject, true, user);
            }
        }
        catch (ContentException e) {
            throw e;
        }
        catch (Exception e) {
            log.error(e.toString(), e);
            throw new ContentException(e.toString());
        }
    }

    protected void propagateDestroy(ContentObject contentObject, boolean recursive, User user) throws ContentException {
        try {
/*
            // check permission
            if (hasPermission(contentObject, user.getId(), USE_CASE_DESTROY)) {
*/
                if (contentObject.isPublished()) {
                    // withdraw
                    withdraw(contentObject.getId(), false, user);
                }

                // delete
                ContentManagerDao dao = (ContentManagerDao) getDao();
                dao.delete(contentObject.getId());
                Application application = Application.getInstance();
                Class moduleClass = ContentUtil.getModuleClassFromKey(contentObject.getId());
                ContentModule module = (ContentModule) application.getModule(moduleClass);
                ContentModuleDao contentModuleDao = (ContentModuleDao) module.getDao();
                contentModuleDao.delete(contentObject.getId());

                // remove acl
                dao.updateRolePrincipals(contentObject.getId(), new ContentAcl[0]);

                // unindex object
                unindex(contentObject);

                // remove profile data
                Application app = Application.getInstance();
                if (ContentProfileModule.isProfiledContent(contentObject.getClassName())) {
                    ContentProfileModule profileMod = (ContentProfileModule)app.getModule(ContentProfileModule.class);
                    profileMod.deleteProfileData(contentObject.getId(), null);
                }

//            }

            // propagate?
            if (recursive) {
                for (Iterator i = contentObject.getChildren().iterator(); i.hasNext();) {
                    ContentObject tmpObj = (ContentObject) i.next();
                    propagateDestroy(tmpObj, true, user);
                }
            }
        }
        catch (Exception e) {
            log.error(e.toString(), e);
            throw new ContentException(e.toString());
        }
    }

//--- Indexing and Searching

    public void index(ContentObject obj) throws ContentException {
        try {
            // wrap content object
            ContentObjectIndexingWrapper wrapper = new ContentObjectIndexingWrapper(obj, false);

            // do index
            Application application = Application.getInstance();
            IndexingService indexer = (IndexingService) application.getService(IndexingService.class);
            indexer.indexDataObject(indexContent, wrapper, new String[]{"className", "date", "archived","author","parentId","name","fileName"});
        }
        catch (Exception e) {
            throw new ContentException("Unable to index object: " + e.toString());
        }
    }

    public void unindex(ContentObject obj) throws ContentException {
        try {
            Application application = Application.getInstance();
            IndexingService indexer = (IndexingService) application.getService(IndexingService.class);
            indexer.unindexDataObject(indexContent, obj.getId());
        }
        catch (Exception e) {
            throw new ContentException("Unable to unindex object: " + e.toString());
        }
    }

    public void reindexTree(String key, User user) throws ContentException {
        try {
            ContentObject contentObject = viewTree(key, null, USE_CASE_PUBLISH, user);
            propagateReindex(contentObject);
        }
        catch (Exception e) {
            throw new ContentException("Unable to reindex object: " + e.toString());
        }
    }

    private void propagateReindex(ContentObject contentObject) throws ContentException {
        try {
        	unindex(contentObject);
            index(contentObject);
            for (Iterator i = contentObject.getChildren().iterator(); i.hasNext();) {
                ContentObject child = (ContentObject) i.next();
                propagateReindex(child);
            }
        }
        catch (Exception e) {
            throw new ContentException("Unable to reindex object: " + e.toString());
        }
    }

    /**
     *
     * @param query
     * @param sort
     * @param start
     * @param rows
     * @param user
     * @return A Collection of DefaultContentObject objects.
     * @throws ContentException
     */
    public Collection search(String query, String sort, int start, int rows, User user) throws ContentException {
        try {
            Collection results = new ArrayList();

            // get search results
            Application application = Application.getInstance();
            IndexingService indexer = (IndexingService) application.getService(IndexingService.class);
            String sortField = ("date".equals(sort)) ? "date" : null;
            SearchResult rs = indexer.searchIndex(indexContent, query, null, sortField, start, rows);

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
        catch (Exception e) {
            log.error(e.toString(), e);
            throw new ContentException(e.toString());
        }

    }

    public int searchCount(String query, User user) throws ContentException {
        try {
            // get search results
            Application application = Application.getInstance();
            IndexingService indexer = (IndexingService) application.getService(IndexingService.class);
            int count = indexer.searchIndexCount(indexContent, query, null);
            return count;
        }
        catch (Exception e) {
            log.error(e.toString(), e);
            throw new ContentException(e.toString());
        }

    }



//-- Audit and Statistics

    public void audit(String key, String event, String param, User user) {
        // save
        ContentAuditor auditor = (ContentAuditor) Application.getInstance().getModule(ContentAuditor.class);
        auditor.audit(key, event, param, user);
    }

    public Collection viewAudit(String key, Date startDate, Date endDate, String sort, boolean desc, int start, int rows, User user) throws ContentException {
        try {
            if (key != null) {
                // retrieve content object
                ContentManagerDao contentManagerDao = (ContentManagerDao) getDao();
                ContentObject contentObject = contentManagerDao.selectById(key);

                // check permission
                if (!hasPermission(contentObject, user.getId(), USE_CASE_AUDIT_VIEW))
                    throw new ContentPermissionException("No permission");
            }

            // retrieve
            ContentAuditor auditor = (ContentAuditor) Application.getInstance().getModule(ContentAuditor.class);
            return auditor.viewAudit(key, startDate, endDate, sort, desc, start, rows);
        }
        catch (ContentException e) {
            throw e;
        }
        catch (Exception e) {
            log.error("viewAudit: " + e.toString());
            throw new ContentException(e.getMessage());
        }
    }

    public int viewAuditCount(String key, Date startDate, Date endDate, User user) throws ContentException {
        try {
            if (key != null) {
                // retrieve content object
                ContentManagerDao contentManagerDao = (ContentManagerDao) getDao();
                ContentObject contentObject = contentManagerDao.selectById(key);

                // check permission
                if (!hasPermission(contentObject, user.getId(), USE_CASE_AUDIT_VIEW))
                    throw new ContentPermissionException("No permission");
            }

            // retrieve
            ContentAuditor auditor = (ContentAuditor) Application.getInstance().getModule(ContentAuditor.class);
            return auditor.viewAuditCount(key, startDate, endDate);
        }
        catch (ContentException e) {
            throw e;
        }
        catch (Exception e) {
            log.error("viewAuditCount: " + e.toString());
            throw new ContentException(e.getMessage());
        }
    }

    public void clearAudit(String key, Date startDate, Date endDate, User user) throws ContentException {
        try {
            if (key != null) {
                // retrieve content object
                ContentManagerDao contentManagerDao = (ContentManagerDao) getDao();
                ContentObject contentObject = contentManagerDao.selectById(key);

                // check permission
                if (!hasPermission(contentObject, user.getId(), USE_CASE_AUDIT_CLEAR))
                    throw new ContentPermissionException("No permission");
            }

            // clear
            ContentAuditor auditor = (ContentAuditor) Application.getInstance().getModule(ContentAuditor.class);
            auditor.clearAudit(key, startDate, endDate);

            // audit
            audit(key, USE_CASE_AUDIT_CLEAR, null, user);
        }
        catch (ContentException e) {
            throw e;
        }
        catch (Exception e) {
            log.error("clearAudit: " + e.toString());
            throw new ContentException(e.getMessage());
        }
    }

    public Collection viewReport(String key, Date startDate, Date endDate, String sort, boolean desc, int start, int rows, User user) throws ContentException {
        try {
            if (key != null) {
                // retrieve content object
                ContentManagerDao contentManagerDao = (ContentManagerDao) getDao();
                ContentObject contentObject = contentManagerDao.selectById(key);

                // check permission
                if (!hasPermission(contentObject, user.getId(), USE_CASE_REPORT_VIEW))
                    throw new ContentPermissionException("No permission");
            }

            // retrieve
            ContentReporter reporter = (ContentReporter) Application.getInstance().getModule(ContentReporter.class);
            return reporter.viewAudit(key, startDate, endDate, sort, desc, start, rows);
        }
        catch (ContentException e) {
            throw e;
        }
        catch (Exception e) {
            log.error("viewReport: " + e.toString());
            throw new ContentException(e.getMessage());
        }
    }

    public int viewReportCount(String key, Date startDate, Date endDate, User user) throws ContentException {
        try {
            if (key != null) {
                // retrieve content object
                ContentManagerDao contentManagerDao = (ContentManagerDao) getDao();
                ContentObject contentObject = contentManagerDao.selectById(key);

                // check permission
                if (!hasPermission(contentObject, user.getId(), USE_CASE_REPORT_VIEW))
                    throw new ContentPermissionException("No permission");
            }

            // retrieve
            ContentReporter reporter = (ContentReporter) Application.getInstance().getModule(ContentReporter.class);
            return reporter.viewAuditCount(key, startDate, endDate);
        }
        catch (ContentException e) {
            throw e;
        }
        catch (Exception e) {
            log.error("viewReportCount: " + e.toString());
            throw new ContentException(e.getMessage());
        }
    }

    public void clearReport(String key, Date startDate, Date endDate, User user) throws ContentException {
        try {
            if (key != null) {
                // retrieve content object
                ContentManagerDao contentManagerDao = (ContentManagerDao) getDao();
                ContentObject contentObject = contentManagerDao.selectById(key);

                // check permission
                if (!hasPermission(contentObject, user.getId(), USE_CASE_REPORT_CLEAR))
                    throw new ContentPermissionException("No permission");
            }

            // clear
            ContentReporter reporter = (ContentReporter) Application.getInstance().getModule(ContentReporter.class);
            reporter.clearAudit(key, startDate, endDate);

            // report
            audit(key, USE_CASE_REPORT_CLEAR, null, user);
        }
        catch (ContentException e) {
            throw e;
        }
        catch (Exception e) {
            log.error("clearReport: " + e.toString());
            throw new ContentException(e.getMessage());
        }
    }


//--- Template Methods

    public String getSiteTemplate(String contentType, String clientType) {
        String siteTemplate = null;

        // read from cache
        String cacheKey = contentType + "_" + clientType;
        try {
            siteTemplate = (String) siteTemplateCache.getFromCache(cacheKey, CACHE_DURATION_SITE_TEMPLATE);
        }
        catch (NeedsRefreshException e) {
        }
        if (siteTemplate != null) {
            return siteTemplate;
        }

        // get site template
        if ("text/vnd.wap.wml".equals(contentType)) {
            siteTemplate = DEFAULT_WAP_TEMPLATE;
        }
        else {
            // load from setup
            Application application = Application.getInstance();
            try {
                SetupModule setup = (SetupModule) application.getModule(SetupModule.class);
                siteTemplate = setup.get(SETUP_PROPERTY_SITE_TEMPLATE);
            }
            catch (Exception e) {
            }
            if (siteTemplate == null || siteTemplate.trim().length() == 0) {
                // not specified in setup,
                siteTemplate = application.getProperty(APPLICATION_PROPERTY_SITE_TEMPLATE);
            }
            if (siteTemplate == null || siteTemplate.trim().length() == 0) {
                siteTemplate = DEFAULT_HTML_TEMPLATE;
            }
        }

        // store in cache and return
        siteTemplateCache.putInCache(cacheKey, siteTemplate);
        return siteTemplate;
    }

    public String getContentPageTemplateById(String key) throws ContentException {
        try {
            ContentManagerDao contentManagerDao = (ContentManagerDao) getDao();
            return contentManagerDao.selectTemplateById(key);
        }
        catch (Exception e) {
            log.error("getContentPageTemplateById: " + e.toString());
            throw new ContentException(e.getMessage());
        }
    }

    public Collection getContentByTemplate(String template) throws ContentException {
        try {
            ContentManagerDao contentManagerDao = (ContentManagerDao) getDao();
            return contentManagerDao.selectByTemplate(template);
        }
        catch (Exception e) {
            log.error("getContentPageTemplateById: " + e.toString());
            throw new ContentException(e.getMessage());
        }
    }

    public void updateContentPageTemplate(String template, String[] keys) throws ContentException {
        try {
            ContentManagerDao contentManagerDao = (ContentManagerDao) getDao();
            contentManagerDao.updateTemplate(template, keys);
        }
        catch (Exception e) {
            log.error("updateContentPageTemplate: " + e.toString());
            throw new ContentException(e.getMessage());
        }
    }


//--- Notification Subscription Methods

    /**
     * Subscribes a user to specified content so that the user is notified of
     * new or updated content.
     * @param userId
     * @param keys
     * @throws ContentException
     */
    public void subscribeByUser(String userId, String[] keys) throws ContentException {
        try {
            ContentManagerDao dao = (ContentManagerDao) getDao();
            dao.insertSubscriptionByUser(userId, keys);
        }
        catch (Exception e) {
            log.error("Unable to subscribe to content: " + e.getMessage(), e);
            throw new ContentException(e.getMessage());
        }
    }

    /**
     * Unsubscribes a user from specified content.
     * @param userId
     * @param keys Use null or empty array to remove all subscriptions.
     * @throws ContentException
     */
    public void unsubscribeByUser(String userId, String[] keys) throws ContentException {
        try {
            ContentManagerDao dao = (ContentManagerDao) getDao();
            dao.deleteSubscriptionByUser(userId, keys);
        }
        catch (Exception e) {
            log.error("Unable to unsubscribe to content: " + e.getMessage(), e);
            throw new ContentException(e.getMessage());
        }

    }

    /**
     * Subscribes a content to specified users so that the user is notified of
     * new or updated content.
     * @param contentId
     * @param userIds
     * @throws ContentException
     */
    public void subscribeByContent(String contentId, String[] userIds) throws ContentException {
        try {
            ContentManagerDao dao = (ContentManagerDao) getDao();
            dao.insertSubscriptionByContent(contentId, userIds);
        }
        catch (Exception e) {
            log.error("Unable to subscribe to content: " + e.getMessage(), e);
            throw new ContentException(e.getMessage());
        }
    }

    /**
     * Unsubscribes a content from specified users.
     * @param contentId
     * @param userIds Use null or empty array to remove all subscriptions.
     * @throws ContentException
     */
    public void unsubscribeByContent(String contentId, String[] userIds) throws ContentException {
        try {
            ContentManagerDao dao = (ContentManagerDao) getDao();
            dao.deleteSubscriptionByContent(contentId, userIds);
        }
        catch (Exception e) {
            log.error("Unable to unsubscribe to content: " + e.getMessage(), e);
            throw new ContentException(e.getMessage());
        }

    }

    /**
     *
     * @param userId
     * @param contentId
     * @return null If the user is not subscribed to the content
     * @throws ContentException
     */
    public ContentSubscription getSubscription(String userId, String contentId) throws ContentException {
        try {
            if (userId == null || contentId == null) {
                return null;
            }
            ContentManagerDao dao = (ContentManagerDao) getDao();
            Collection results = dao.selectSubscriptions(userId, contentId, 0, 1);
            if (results.size() > 0) {
                return (ContentSubscription)results.iterator().next();
            }
            else {
                return null;
            }
        }
        catch (Exception e) {
            log.error("Unable to retrieve subscription for user " + userId + " and content " + contentId, e);
            throw new ContentException(e.getMessage());
        }
    }

    /**
     *
     * @param userId
     * @param start
     * @param rows
     * @return A Collection of ContentSubscription objects.
     * @throws ContentException
     */
    public Collection getSubscriptionsByUser(String userId, int start, int rows) throws ContentException {
        try {
            if (userId == null) {
                return new ArrayList();
            }
            ContentManagerDao dao = (ContentManagerDao) getDao();
            return dao.selectSubscriptions(userId, null, start, rows);
        }
        catch (Exception e) {
            log.error("Unable to retrieve subscription for user: " + userId, e);
            throw new ContentException(e.getMessage());
        }
    }

    public int getSubscriptionsCountByUser(String userId) throws ContentException {
        try {
            if (userId == null) {
                return 0;
            }
            ContentManagerDao dao = (ContentManagerDao) getDao();
            return dao.selectSubscriptionsCount(userId, null);
        }
        catch (Exception e) {
            log.error("Unable to retrieve subscription for user: " + userId, e);
            throw new ContentException(e.getMessage());
        }
    }

    /**
     *
     * @param contentId
     * @param start
     * @param rows
     * @return A Collection of ContentSubscription objects.
     * @throws ContentException
     */
    public Collection getSubscriptionsByContent(String contentId, int start, int rows) throws ContentException {
        try {
            if (contentId == null) {
                return new ArrayList();
            }
            ContentManagerDao dao = (ContentManagerDao) getDao();
            return dao.selectSubscriptions(null, contentId, start, rows);
        }
        catch (Exception e) {
            log.error("Unable to retrieve subscription for user: " + contentId, e);
            throw new ContentException(e.getMessage());
        }
    }


//--- Utility Methods

    public final static String PREFIX_ALLOWED_CLASSES = "cms.allowed.classes.";

    /**
     * Returns an Array of Class objects representing the ContentObjects that are allowed to be
     * added as a child to parentClass.
     * @param parentClass Use null to return an array of all available content object classes.
     * @return
     */
    public Class[] getAllowedClasses(Class parentClass) {
        Class[] classes = null;
        try {
            if (parentClass != null) {

                // look in config
                String propertyName = PREFIX_ALLOWED_CLASSES + parentClass.getName();
                String config = Application.getInstance().getProperty(propertyName);
                if (config != null) {
                    StringTokenizer st = new StringTokenizer(config, ";,\n ");
                    Collection classList = new ArrayList();
                    while (st.hasMoreTokens()) {
                        try {
                            Class clazz = Class.forName(st.nextToken());
                            classList.add(clazz);
                        }
                        catch (Exception e) {
                        }
                    }
                    classes = (Class[]) classList.toArray(new Class[0]);
                }

                if (classes == null) {
                    // look in content module
                    ContentObject co = (ContentObject) parentClass.newInstance();
                    ContentModule mod = (ContentModule) Application.getInstance().getModule(co.getContentModuleClass());
                    String[] classNames = mod.getAllowedClassNames(parentClass);
                    if (classNames != null) {
                        Collection classList = new ArrayList();
                        for (int i = 0; i < classNames.length; i++) {
                            try {
                                Class clazz = Class.forName(classNames[i]);
                                classList.add(clazz);
                            }
                            catch (Exception e) {
                            }
                        }
                        classes = (Class[]) classList.toArray(new Class[0]);
                    }
                }

                if (classes == null) {
                    // ContentManager determines
                    classes = new Class[0];
                }
            }
        }
        catch (Exception e) {
        }
        if (classes == null) {
            // look in configuration

            // else return all content objects
            classes = (Class[]) contentObjectClassList.toArray(new Class[0]);
        }
        return classes;
    }

    /**
     * Returns an Array of Class objects representing the ContentObject classes that the specified child
     * can be added to.
     * @param childClass The child class.
     * @return
     */
    public Class[] getAllowedParentClasses(Class childClass) {

        if (childClass == null) {
            return new Class[0];
        }

        Collection result = new ArrayList();

        for (Iterator i = contentObjectClassList.iterator(); i.hasNext();) {
            Class parentClass = (Class) i.next();
            Collection classList = Arrays.asList(getAllowedClasses(parentClass));
            if (classList.contains(childClass) && !result.contains(parentClass)) {
                result.add(parentClass);
            }
        }
        return (Class[]) result.toArray(new Class[0]);
    }

    /**
     * Based on the application property "cms.versioning" defined in kacang-config-xx.xml
     * When versioning is disabled, when content is checked out a new version is temporarily
     * created. Once approved or rejected, the changes are saved to the first version.
     * For compability reasons, if more than one version already exists all are removed leaving
     * the latest version.
     * By default (if the property is not specified), versioning is enabled.
     * @return true if versioning is enabled.
     */
    public boolean isVersioningEnabled() {
        Application app = Application.getInstance();
        String val = app.getProperty(APPLICATION_PROPERTY_VERSIONING_DISABLED);
        boolean versioning = !Boolean.valueOf(val).booleanValue();
        return versioning;
    }

}
