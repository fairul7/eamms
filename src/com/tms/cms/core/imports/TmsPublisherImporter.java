package com.tms.cms.core.imports;

import com.tms.cms.article.Article;
import com.tms.cms.core.model.*;
import com.tms.cms.page.Page;
import com.tms.cms.section.Section;
import kacang.Application;
import kacang.model.DaoException;
import kacang.model.DataSourceFactory;
import kacang.runtime.config.ConfigException;
import kacang.runtime.config.Configuration;
import kacang.services.security.*;
import kacang.util.JdbcUtil;
import kacang.util.Log;

import javax.sql.DataSource;
import java.io.File;
import java.io.StringReader;
import java.net.URLDecoder;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import org.htmlparser.Parser;
import org.htmlparser.NodeReader;
import org.htmlparser.util.ParserUtils;
import org.htmlparser.util.ParserException;
import org.htmlparser.visitors.TextExtractingVisitor;

public class TmsPublisherImporter {

    private Log log = Log.getLog(getClass());

    public static final String USE_CASE_IMPORT = "Import";

    public void init() throws ConfigException {
        String classesPath = new Object().getClass().getResource("/").getPath();
        String strPath = "";
        File classesFile = new File(classesPath);
        File configDir = new File(classesFile, "../conf/");
        strPath = URLDecoder.decode(configDir.getAbsolutePath());
        Configuration.init(strPath);
    }

    /**
     * Imports sections, articles and pages.
     * @param dataSourceName
     * @param targetRootId
     * @param userId
     */
    public void importContent(String dataSourceName, String targetRootId, String userId) {

        log.info("--- Starting content import from tmsPUBLISHER datasource " + dataSourceName);

        try {


            // get source and module
            DataSource dataSource = DataSourceFactory.getInstance().getDataSource(dataSourceName);
            Application application = Application.getInstance();
            ContentManager contentManager = (ContentManager) application.getModule(ContentManager.class);
            ContentManagerDao contentManagerDao = (ContentManagerDao) contentManager.getDao();

            // get user
            User user = ((SecurityService) application.getService(SecurityService.class)).getUser(userId);

            // get target root
            Section targetRoot = (Section) contentManager.view(targetRootId, user);

            // select sections
            log.info("-- Importing sections --");
            Collection sectionList = selectSections(dataSource);

            // insert sections
            int count=1;
            for (Iterator i = sectionList.iterator(); i.hasNext(); count++) {
                Section section = (Section) i.next();

                log.debug("Importing section " + section.getId() + "... ");
                if (count % 100 == 0) {
                    log.info("Imported " + count + " sections");
                }

                try {
                    // set id
                    section.setId(ContentUtil.generateId(Section.class, section.getId()));

                    // set parent
                    if (section.getParentId() == null || section.getParentId().trim().length() == 0) {
                        section.setParentId(targetRootId);
                    }
                    else {
                        section.setParentId(ContentUtil.generateId(Section.class, section.getParentId()));
                    }
                    section.setAclId(targetRoot.getAclId());

                    // set details
                    section.setSummary("");
                    section.setDescription("");
                    section.setClassName(Section.class.getName());
                    section.setDate(new Date());
                    section.setVersion("1");
                    section.setNew(false);
                    section.setModified(false);
                    section.setDeleted(false);
                    section.setArchived(false);
                    section.setPublished(false);
                    section.setName(filterHtml(section.getName()));

                    // set checkout status
                    section.setCheckedOut(false);
                    section.setCheckOutDate(null);
                    section.setCheckOutUserId(null);
                    section.setCheckOutUser(null);

                    // set audit details
                    section.setSubmitted(false);
                    section.setSubmissionDate(new Date());
                    section.setSubmissionUser(user.getUsername());
                    section.setSubmissionUserId(user.getId());
                    section.setApproved(true);
                    section.setApprovalDate(new Date());
                    section.setApprovalUser(user.getUsername());
                    section.setApprovalUserId(user.getId());
                    section.setComments(null);

                    // call content module to create new version
                    ContentModule module = (ContentModule) application.getModule(section.getContentModuleClass());
                    ContentModuleDao contentModuleDao = (ContentModuleDao) module.getDao();
                    contentModuleDao.insert(section);

                    // create record
                    contentManagerDao.insertNew(section);

                    // additional module-specific processing
                    contentModuleDao.processNew(section);

                    // index object
                    contentManager.index(section);

                    // audit
                    contentManager.audit(section.getId(), USE_CASE_IMPORT, null, user);

                    log.debug("Section " + section.getId() + " imported");
                }
                catch (Exception e) {
                    log.error("Error importing " + section.getId() + ": " + e.toString());
                }
            }
            sectionList = null;

            // select articles
            log.info("-- Importing articles --");
            Collection articleList = selectArticles(dataSource);

            // insert articles
            count = 1;
            for (Iterator i = articleList.iterator(); i.hasNext(); count++) {
                Article article = (Article) i.next();

                log.debug("Importing article " + article.getId() + "... ");
                if (count % 100 == 0) {
                    log.info("Imported " + count + " articles");
                }

                try {
                    // set id
                    article.setId(ContentUtil.generateId(Article.class, article.getId()));

                    // set parent
                    if (article.getParentId() == null || article.getParentId().trim().length() == 0) {
                        article.setParentId(targetRootId);
                    }
                    else {
                        article.setParentId(ContentUtil.generateId(Section.class, article.getParentId()));
                    }
                    article.setAclId(targetRoot.getAclId());

                    // set details
                    article.setDescription("");
                    article.setClassName(Article.class.getName());
                    article.setVersion("1");
                    article.setNew(false);
                    article.setModified(false);
                    article.setDeleted(false);
                    article.setPublished(false);

                    // set checkout status
                    article.setCheckedOut(false);
                    article.setCheckOutDate(null);
                    article.setCheckOutUserId(null);
                    article.setCheckOutUser(null);

                    // set audit details
                    article.setSubmitted(false);
                    article.setSubmissionDate(new Date());
                    article.setSubmissionUser(user.getUsername());
                    article.setSubmissionUserId(user.getId());
                    article.setApproved(true);
                    article.setApprovalDate(new Date());
                    article.setApprovalUser(user.getUsername());
                    article.setApprovalUserId(user.getId());
                    article.setComments(null);

                    // call content module to create new version
                    ContentModule module = (ContentModule) application.getModule(article.getContentModuleClass());
                    ContentModuleDao contentModuleDao = (ContentModuleDao) module.getDao();
                    contentModuleDao.insert(article);

                    // create record
                    contentManagerDao.insertNew(article);

                    // additional module-specific processing
                    contentModuleDao.processNew(article);

                    // index object
                    contentManager.index(article);

                    // audit
                    contentManager.audit(article.getId(), USE_CASE_IMPORT, null, user);

                    log.debug("Article " + article.getId() + " imported");
                }
                catch (Exception e) {
                    log.error("Error importing " + article.getId() + ": " + e.toString());
                }
            }
            articleList = null;

            // select keywords
            log.info("-- Importing keywords --");
            Collection keywordList = selectKeywords(dataSource);

            // insert keywords
            Iterator k = keywordList.iterator();
            DefaultContentObject contentObject;
            String objId = null;
            String keywords = "";
            count = 1;
            while (k.hasNext()) {
                contentObject = (DefaultContentObject) k.next();
                String keyword = (String) contentObject.getProperty("keyword");

                log.debug("Importing keyword " + keyword + " for " + contentObject.getId());
                if (count % 100 == 0) {
                    log.info("Imported " + count + " keywords");
                }
                count++;

                if (objId == null || objId.equals(contentObject.getId())) {
                    objId = contentObject.getId();
                    keywords += keyword + ",";
                }
                else {
                    DefaultContentObject dco = new DefaultContentObject();
                    dco.setId(ContentUtil.generateId(Article.class, objId));
                    dco.setKeywords(keywords);
                    contentManager.updateRelated(dco, user);
                    objId = contentObject.getId();
                    keywords = keyword + ",";
                }
            }
            DefaultContentObject dco = new DefaultContentObject();
            dco.setId(ContentUtil.generateId(Article.class, objId));
            dco.setKeywords(keywords);
            keywordList = null;

            // create Pages section
            String pageSectionId;
            Section pageSection = new Section();
            pageSection.setParentId(targetRootId);
            pageSection.setName("Pages");
            pageSection.setDescription("Pages imported from tmsPUBLISHER");
            try {
                pageSection = (Section) contentManager.createNew(pageSection, user);
                pageSectionId = pageSection.getId();
            }
            catch (Exception e) {
                pageSectionId = targetRootId;
                pageSection = targetRoot;
                log.error("Error creating Pages section: " + e.toString());
            }

            // select pages
            log.info("-- Importing pages --");
            Collection pageList = selectPages(dataSource);

            // insert pages
            count=1;
            for (Iterator i = pageList.iterator(); i.hasNext(); count++) {
                Page page = (Page) i.next();

                log.debug("Importing page " + page.getId() + "... ");
                if (count % 100 == 0) {
                    log.info("Imported " + count + " pages");
                }

                try {
                    // set id
                    page.setId(ContentUtil.generateId(Page.class, page.getId()));

                    // set parent
                    if (page.getParentId() == null || page.getParentId().trim().length() == 0) {
                        page.setParentId(pageSectionId);
                    }
                    else {
                        page.setParentId(ContentUtil.generateId(Section.class, page.getParentId()));
                    }
                    page.setAclId(pageSection.getAclId());

                    // set details
                    page.setContents("");
                    page.setDescription("");
                    page.setClassName(Page.class.getName());
                    page.setDate(new Date());
                    page.setVersion("1");
                    page.setNew(false);
                    page.setModified(false);
                    page.setDeleted(false);
                    page.setArchived(false);
                    page.setPublished(false);

                    // set checkout status
                    page.setCheckedOut(false);
                    page.setCheckOutDate(null);
                    page.setCheckOutUserId(null);
                    page.setCheckOutUser(null);

                    // set audit details
                    page.setSubmitted(false);
                    page.setSubmissionDate(new Date());
                    page.setSubmissionUser(user.getUsername());
                    page.setSubmissionUserId(user.getId());
                    page.setApproved(true);
                    page.setApprovalDate(new Date());
                    page.setApprovalUser(user.getUsername());
                    page.setApprovalUserId(user.getId());
                    page.setComments(null);

                    // call content module to create new version
                    ContentModule module = (ContentModule) application.getModule(page.getContentModuleClass());
                    ContentModuleDao contentModuleDao = (ContentModuleDao) module.getDao();
                    contentModuleDao.insert(page);

                    // create record
                    contentManagerDao.insertNew(page);

                    // additional module-specific processing
                    contentModuleDao.processNew(page);

                    // index object
                    contentManager.index(page);

                    // audit
                    contentManager.audit(page.getId(), USE_CASE_IMPORT, null, user);

                    log.debug("Page " + page.getId() + " imported");
                }
                catch (Exception e) {
                    log.error("Error importing " + page.getId() + ": " + e.toString());
                }
            }
            pageList = null;

        }
        catch (Exception e) {
            log.error("Error importing tmsPUBLISHER content: " + e.toString(), e);
        }

        log.info("--- Completed content import from tmsPUBLISHER datasource " + dataSourceName);

    }

    /**
     * Imports users and groups.
     * @param dataSourceName The datasource name for the tmsPUBLISHER source.
     */
    public void importPrincipals(String dataSourceName) {

        log.info("--- Starting principal import from tmsPUBLISHER datasource " + dataSourceName);

        try {
            DataSource dataSource = DataSourceFactory.getInstance().getDataSource(dataSourceName);
            Application application = Application.getInstance();
            SecurityService security = (SecurityService) application.getService(SecurityService.class);

            // select users
            Collection userList = selectUsers(dataSource);

            // insert users
            int count=1;
            for (Iterator i = userList.iterator(); i.hasNext(); count++) {
                User user = (User) i.next();

                if (count % 100 == 0) {
                    log.info("Imported " + count + " users");
                }

                user.setProperty("weakpass", user.getProperty("clearPassword"));
                try {
                    security.addUser(user, true);
                }
                catch (Exception e) {
                    log.error("Error importing user " + user.getId() + " (" + user.getUsername() + "): " + e.toString());
                }
            }

            // select groups
            Collection groupList = selectGroups(dataSource);

            // insert groups
            for (Iterator i = groupList.iterator(); i.hasNext();) {
                Group group = (Group) i.next();
                try {
                    security.addGroup(group, true);
                }
                catch (Exception e) {
                    log.error("Error importing group " + group.getId() + " (" + group.getGroupName() + "): " + e.toString());
                }
            }

            // select user-groups
            Collection userGroupList = selectUserGroups(dataSource);

            // insert user-groups
            UserGroup[] ugArray = (UserGroup[]) userGroupList.toArray(new UserGroup[0]);
            SecurityDao sdao = (SecurityDao) security.getDao();
            sdao.storeUserGroups(ugArray);
        }
        catch (DaoException e) {
            e.printStackTrace();
        }
        catch (SQLException e) {
            log.error("Error importing tmsPUBLISHER principals: " + e.toString(), e);
        }

        log.info("--- Completed principal import from tmsPUBLISHER datasource " + dataSourceName);

    }

    public String filterHtml(String s) throws ParserException {
        String cleanText = "";
        try {
            if (!s.trim().equals("")) {
                Parser parser = new Parser(new NodeReader(new StringReader(s), s.length()));
                TextExtractingVisitor visitor = new TextExtractingVisitor();
                parser.visitAllNodesWith(visitor);
                cleanText = ParserUtils.removeEscapeCharacters(visitor.getExtractedText());
            }

            return cleanText;
        } catch (ParserException e) {
            throw e;
        }
    }

    /**
     * @return A Collection of Section objects.
     */
    protected Collection selectSections(DataSource dataSource) throws SQLException {
        Connection con = null;
        try {
            con = dataSource.getConnection();
            Collection sectionList = JdbcUtil.getInstance().select(con,
                    "SELECT sectionID AS id, parentSectionID AS parentId, name, [order] AS ordering FROM Sections",
                    Section.class, null, 0, -1);
            return sectionList;
        }
        catch (SQLException e) {
            throw e;
        }
        finally {
            if (con != null) {
                try {
                    con.close();
                }
                catch (SQLException e) {
                    ;
                }
            }
        }
    }

    /**
     * @return A Collection of Article objects.
     */
    protected Collection selectArticles(DataSource dataSource) throws DaoException, SQLException {
        Connection con = null;
        try {
            con = dataSource.getConnection();
            Collection articleList = JdbcUtil.getInstance().select(con,
                    "SELECT articleID AS id, sectionID AS parentId, headline AS name, byline AS author, subhead AS description, lede AS summary, story AS contents, weight AS ordering, userID AS submissionUserId, date, isArchive AS archived FROM Articles",
                    Article.class, null, 0, -1);
            return articleList;
        }
        catch (SQLException e) {
            throw e;
        }
        finally {
            if (con != null) {
                try {
                    con.close();
                }
                catch (SQLException e) {
                    ;
                }
            }
        }
    }

    /**
     * @return A Collection of Article objects.
     */
    protected Collection selectPages(DataSource dataSource) throws DaoException, SQLException {
        Connection con = null;
        try {
            con = dataSource.getConnection();
            Collection pageList = JdbcUtil.getInstance().select(con,
                    "SELECT p.pageID AS id, p.title AS name, p.contents AS summary, s.sectionID AS parentId FROM Pages p LEFT OUTER JOIN Sections s ON p.name = s.pageID",
                    Page.class, null, 0, -1);
            return pageList;
        }
        catch (SQLException e) {
            throw e;
        }
        finally {
            if (con != null) {
                try {
                    con.close();
                }
                catch (SQLException e) {
                    ;
                }
            }
        }
    }

    /**
     * @return A Collection of DefaultContentObject objects with properties id and keyword set.
     */
    protected Collection selectKeywords(DataSource dataSource) throws DaoException, SQLException {
        Connection con = null;
        try {
            con = dataSource.getConnection();
            Collection keywordList = JdbcUtil.getInstance().select(con,
                    "SELECT DISTINCT articleID AS id, k.name AS keyword FROM keywords k, articleskeywords ak WHERE k.keywordID = ak.keywordID ORDER BY articleID",
                    DefaultContentObject.class, null, 0, -1);
            return keywordList;
        }
        catch (SQLException e) {
            throw e;
        }
        finally {
            if (con != null) {
                try {
                    con.close();
                }
                catch (SQLException e) {
                    ;
                }
            }
        }
    }

    /**
     * @return A Collection of User objects.
     */
    protected Collection selectUsers(DataSource dataSource) throws DaoException, SQLException {
        Connection con = null;
        try {
            con = dataSource.getConnection();
            Collection userList = JdbcUtil.getInstance().select(con,
                    "SELECT userID AS id, userName AS username, password, clearPassword, firstName, lastName, isActive AS active FROM Users",
                    User.class, null, 0, -1);
            return userList;
        }
        catch (SQLException e) {
            throw e;
        }
        finally {
            if (con != null) {
                try {
                    con.close();
                }
                catch (SQLException e) {
                    ;
                }
            }
        }
    }

    /**
     * @return A Collection of Group objects.
     */
    protected Collection selectGroups(DataSource dataSource) throws DaoException, SQLException {
        Connection con = null;
        try {
            con = dataSource.getConnection();
            Collection groupList = JdbcUtil.getInstance().select(con,
                    "SELECT groupID AS id, name AS groupName FROM Groups",
                    Group.class, null, 0, -1);
            return groupList;
        }
        catch (SQLException e) {
            throw e;
        }
        finally {
            if (con != null) {
                try {
                    con.close();
                }
                catch (SQLException e) {
                    ;
                }
            }
        }
    }

    /**
     * @return A Collection of UserGroup objects.
     */
    protected Collection selectUserGroups(DataSource dataSource) throws DaoException, SQLException {
        Connection con = null;
        try {
            con = dataSource.getConnection();
            Collection userGroupList = JdbcUtil.getInstance().select(con,
                    "SELECT userID AS userId, groupID AS groupId FROM UsersGroups",
                    UserGroup.class, null, 0, -1);
            return userGroupList;
        }
        catch (SQLException e) {
            throw e;
        }
        finally {
            if (con != null) {
                try {
                    con.close();
                }
                catch (SQLException e) {
                    ;
                }
            }
        }
    }


    public static void main(String[] args) {

        if (args == null || args.length < 2) {
            System.out.println("Imports content and/or principals from tmsPUBLISHER");
            System.out.println("To import content and principals: ");
            System.out.println("java com.tms.cms.core.imports.TmsPublisherImporter all dataSourceName targetRootId userId");
            System.out.println("To import only content: ");
            System.out.println("java com.tms.cms.core.imports.TmsPublisherImporter content dataSourceName targetRootId userId");
            System.out.println("To import only principals: ");
            System.out.println("java com.tms.cms.core.imports.TmsPublisherImporter principals dataSourceName");
        }
        else {
            try {
                TmsPublisherImporter importer = new TmsPublisherImporter();
                importer.init();

                if ("all".equals(args[0])) {
                    importer.importPrincipals(args[1]);
                    importer.importContent(args[1], args[2], args[3]);
                }
                else if ("content".equals(args[0])) {
                    importer.importContent(args[1], args[2], args[3]);
                }
                else if ("principals".equals(args[0])) {
                    importer.importPrincipals(args[1]);
                }
            }
            catch (ConfigException e) {
                System.out.println("Error initializing configuration: " + e.toString());
                e.printStackTrace();
            }
        }
    }

}
