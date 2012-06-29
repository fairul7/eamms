package com.tms.cms.core.model;

import java.io.File;
import java.io.FilenameFilter;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;

import kacang.Application;
import kacang.model.DaoQuery;
import kacang.model.operator.DaoOperator;
import kacang.model.operator.OperatorEquals;
import kacang.model.operator.OperatorIn;
import kacang.services.security.Group;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.util.Log;
import kacang.util.Mailer;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.SequencedHashMap;

import com.tms.util.MailUtil;

public class ContentUtil {

    public static final String SITE_TEMPLATE_PREFIX = "cms_";
    public static final String PAGE_TEMPLATE_PREFIX = "content_";
    public static final String PAGE_TEMPLATE_SUFFIX = ".jsp";
    public static final String REQUEST_KEY_PREVIEW_MODE = "com.tms.cms.Preview";
    public static final String SESSION_KEY_EDIT_MODE = "com.tms.cms.EditMode";
    public static final String SESSION_KEY_CMSADMIN_MODE = "com.tms.cms.CmsAdminMode";

//--- Utility Methods

    public static String generateId(Class clazz, String uid) {
        return clazz.getName() + "_" + uid;
    }

    public static Class getModuleClassFromKey(String key) throws InvalidKeyException {
        try {
            String objectClassName = getClassNameFromKey(key);
            Class objectClass = Class.forName(objectClassName);
            ContentObject co = (ContentObject)objectClass.newInstance();
            return co.getContentModuleClass();
        }
        catch(Exception e) {
            throw new InvalidKeyException(key);
        }
    }

    public static String getClassNameFromKey(String key) throws InvalidKeyException {
        try {
            StringTokenizer st = new StringTokenizer(key, "_");
            String className = st.nextToken();
            return className;
        }
        catch(Exception e) {
            throw new InvalidKeyException(key);
        }
    }

    public static String getIdFromKey(String key) throws InvalidKeyException {
        try {
            StringTokenizer st = new StringTokenizer(key, "_");
            st.nextToken();
            String uid = st.nextToken();
            return uid;
        }
        catch(Exception e) {
            throw new InvalidKeyException(key);
        }
    }

    public static String generateNextVersionId(ContentObject object) throws InvalidKeyException {
        try {
            String versionId = object.getVersion();
            int currentVersion = Integer.parseInt(versionId);
            String nextVersionId = new Integer(++currentVersion).toString();
            return nextVersionId;
        }
        catch(Exception e) {
            throw new InvalidKeyException();
        }
    }

    public static String formatText(String content, String keywords) {
        int index = -2;

        if (keywords != null) {
            Collection keywordList = new ArrayList();

            // tokenize keywords

            boolean phrase = false;
            String kw = "";
            StringTokenizer st = new StringTokenizer(keywords, " ");
            while(st.hasMoreTokens()) {
                kw += st.nextToken();
                if (kw.equalsIgnoreCase("AND") || kw.equalsIgnoreCase("OR") || kw.equalsIgnoreCase("NOT"))
                    continue;
                if (kw.startsWith("\"")) {
                    phrase = true;
                }
                else if (phrase && kw.endsWith("\"")) {
                    phrase = false;
                    kw = kw.substring(1, kw.length()-1);
                    keywordList.add(kw);
                    kw = "";
                }
                else {
                    keywordList.add(kw);
                    kw = "";
                }
            }

            String lowerContent = content.toLowerCase();
            for (Iterator i=keywordList.iterator(); i.hasNext();) {
                String tmp = (String)i.next();
                int tmpIndex = lowerContent.indexOf(tmp.toLowerCase());
                if (tmpIndex >= 0 && (index < 0 || tmpIndex < index)) {
                    index = tmpIndex;
                }
            }
        }
        if (index < 0)
            index = 0;

        int summaryLength = 255;
        int endIndex = index + summaryLength;
        if (endIndex > content.length())
            endIndex = content.length();
        content = content.substring(index, endIndex) + "...";
        return content;
    }

    /**
     * Copies all matching attributes from object to fullObject.
     * @param fullObject
     * @param object
     */
    public static void copyAttributes(ContentObject fullObject, ContentObject object) throws InvocationTargetException, IllegalAccessException {
        BeanUtils.copyProperties(fullObject, object);
    }

    /**
     * Copies status attributes from object to fullObject.
     * @param fullObject
     * @param object
     */
    public static void copyStatusAttributes(ContentObject fullObject, ContentObject object) {
        fullObject.setNew(object.isNew());
        fullObject.setModified(object.isModified());
        fullObject.setDeleted(object.isDeleted());
        fullObject.setArchived(object.isArchived());
        fullObject.setPublished(object.isPublished());
        fullObject.setApproved(object.isApproved());
        fullObject.setApprovalUser(object.getApprovalUser());
        fullObject.setApprovalUserId(object.getApprovalUserId());
        fullObject.setApprovalDate(object.getApprovalDate());
        fullObject.setCheckedOut(object.isCheckedOut());
        fullObject.setCheckOutDate(object.getCheckOutDate());
        fullObject.setCheckOutUser(object.getCheckOutUser());
        fullObject.setCheckOutUserId(object.getCheckOutUserId());
        fullObject.setSubmitted(object.isSubmitted());
        fullObject.setSubmissionDate(object.getSubmissionDate());
        fullObject.setSubmissionUser(object.getSubmissionUser());
        fullObject.setSubmissionUserId(object.getSubmissionUserId());
        fullObject.setComments(object.getComments());
        fullObject.setClassName(object.getClassName());
        fullObject.setAclId(object.getAclId());
        fullObject.setParentId(object.getParentId());
        fullObject.setOrdering(object.getOrdering());
        fullObject.setTemplate(object.getTemplate());
        fullObject.setStartDate(object.getStartDate());
        fullObject.setEndDate(object.getEndDate());
        fullObject.setPublishDate(object.getPublishDate());
        fullObject.setPublishUser(object.getPublishUser());
        fullObject.setPublishUserId(object.getPublishUserId());
        fullObject.setPublishVersion(object.getPublishVersion());
        fullObject.setDate(object.getDate());
    }

    public static boolean hasPermission(ContentObject contentObject, String userId, String permission) throws ContentException {
        ContentManager contentManager = (ContentManager)Application.getInstance().getModule(ContentManager.class);
        return contentManager.hasPermission(contentObject, userId, permission);
    }

    public static Collection keywordStringToCollection(String keywords) {
        Collection keywordList = new ArrayList();
        if (keywords != null) {
            StringTokenizer st = new StringTokenizer(keywords, ",");
            while(st.hasMoreTokens()) {
                String kw = st.nextToken().trim();
                if (!keywordList.contains(kw)) {
                    keywordList.add(kw);
                }
            }
        }
        return keywordList;
    }

    public static String keywordCollectionToString(Collection keywordList) {
        StringBuffer buffer = new StringBuffer();
        for (Iterator i=keywordList.iterator(); i.hasNext();) {
            String kw = (String)i.next();
            kw = kw.trim();
            buffer.append(kw);
            if (i.hasNext())
                buffer.append(", ");
        }
        return buffer.toString();
    }

    public static String getPageTemplateUrl(String siteTemplate, String pageUrl) {
        StringBuffer url = new StringBuffer();
        url.append("/");
        url.append(SITE_TEMPLATE_PREFIX);
        url.append(siteTemplate);
        if (pageUrl != null && !pageUrl.startsWith("/")) {
            url.append("/");
        }
        if (pageUrl != null) {
            url.append(pageUrl);
        }
        return url.toString();
    }

    /**
     * Retrieves a Map of available site templates from the file system.
     * @return A Map of name=label pairs.
     */
    public static Map getAvailableSiteTemplates() {
        Map templateMap = new SequencedHashMap();
        // read from directory
        Application application = Application.getInstance();
        File contextRoot = new File(application.getApplicationRealPath());
        File[] templateDirArray = contextRoot.listFiles(new FilenameFilter() {
            public boolean accept(File file, String s) {
                return file.isDirectory() && s.startsWith(SITE_TEMPLATE_PREFIX);
            }
        });
        for (int i=0; i<templateDirArray.length; i++) {
            File templateDir = templateDirArray[i];
            String templateName = templateDir.getName().substring(4);
            String templateKey = "cms.label.templateName_" + templateName; 
            String templateLabel = application.getMessage(templateKey, templateName);
            templateMap.put(templateName, templateLabel);
        }
        return templateMap;
    }

    public static Map getAvailablePageTemplates() {
        Map optionMap = new SequencedHashMap();

        // get site template
        ContentManager cm = (ContentManager)Application.getInstance().getModule(ContentManager.class);
        String siteTemplate = cm.getSiteTemplate("text/html", null);

        // look for files in template directory
        try {
            File siteTemplateDir = new File(Application.getInstance().getApplicationRealPath(), ContentUtil.getPageTemplateUrl(siteTemplate, null));
            File[] files = siteTemplateDir.listFiles();
            for (int i=0; i<files.length; i++) {
                String fileName = files[i].getName();
                if (fileName.startsWith(PAGE_TEMPLATE_PREFIX) && fileName.endsWith(PAGE_TEMPLATE_SUFFIX)) {
                    optionMap.put(fileName, fileName);
                }
            }
        }
        catch (Exception e) {
            Log.getLog(ContentUtil.class).error("Error retrieving available page templates", e);;
        }
        return optionMap;
    }

    /**
     * Converts absolute URLs into relative URLs.
     */
    public static String makeRelative(HttpServletRequest request, String content) {
        if (content == null)
            return null;

        // convert absolute (http://domain:port) to relative
        String url = (request.getServerPort() == 80) ?
                "http://" + request.getServerName() + request.getContextPath() + "/cms/" :
                "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/cms/";
        StringBuffer buffer = new StringBuffer(content);
        int j=buffer.toString().indexOf(url);
        while (j>=0) {
            buffer.replace(j, j+url.length(), "");
            j = buffer.toString().indexOf(url);
        }

        // convert web server relative (/context/) to relative (added to support IE in HTMLArea 3)
        url = "src=\"" + request.getContextPath() + "/cms";
        buffer = new StringBuffer(content);
        j=buffer.toString().indexOf(url);
        while (j>=0) {
            buffer.replace(j, j+url.length(), "src=\"");
            j = buffer.toString().indexOf(url);
        }
        
        //xinha(new rich text box) added / when adding a hyperlink and link it to our own content.
        int tempIndex = buffer.toString().indexOf("\"/content.jsp");
        while(tempIndex >=0){
        	buffer.replace(tempIndex+1, tempIndex+2, "");
        	tempIndex = buffer.toString().indexOf("\"/content.jsp", tempIndex + 13);
        }

        return buffer.toString();
    }

    /**
     * Converts relative URLs into absolute URLs.
     */
    public static String makeAbsolute(HttpServletRequest request, String content) {
        if (content == null)
            return null;

        String url = (request.getServerPort() == 80) ?
                "http://" + request.getServerName() + request.getContextPath() + "/cms/" :
                "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/cms/";
        StringBuffer buffer = new StringBuffer(content);
        
        //this bug trigger by using document "Copy URL", paste the URL into the summary of any newly created content, approve
        //then check out and edit the article, approve the article - the url will be corrupted 
        String url2 = (request.getServerPort() == 80) ?
                "http://" + request.getServerName() + request.getContextPath():
                "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();

        int faultPath = buffer.toString().indexOf("/cms/documentstorage/");
        
        while (faultPath>=0) {
            if (faultPath < url2.length() || !buffer.substring(faultPath-url2.length(), faultPath).equals(url2)) {
                buffer.insert(faultPath, url2);
                faultPath = buffer.toString().indexOf("/cms/documentstorage/", faultPath + url2.length() + 2);
            }
            else {
            	faultPath = buffer.toString().indexOf("/cms/documentstorage/", faultPath + 2);
            }
        }
        
        int j=buffer.toString().indexOf("documentstorage/");
        while (j>=0) {
            if (j < url.length() || !buffer.substring(j-url.length(), j).equals(url)) {
                buffer.insert(j, url);
                j = buffer.toString().indexOf("documentstorage/", j + url.length() + 2);
            }
            else {
                j = buffer.toString().indexOf("documentstorage/", j + 2);
            }
        }
        
        int l = buffer.toString().indexOf("cms/storage/");
        int temp = l;
        while ( l >= 0){
        	if (l < url2.length() || !buffer.substring(l-url2.length(), l).equals(url2)) {
                buffer.insert(l-1, url2);
                l = buffer.toString().indexOf("/cms/documentstorage/", l + url2.length() + 2);
            }
            else {
            	l = buffer.toString().indexOf("/cms/documentstorage/", l + 2);
            }
        }
        
        if(temp < 0){
        	
        	int k = buffer.toString().indexOf("storage/");
            
            
            while (k >= 0) {
                if (k > 8 && buffer.toString().substring(k-8, k).equals("document")) {
                    k = buffer.toString().indexOf("storage/", k + 2);
                }
                else if (k < url.length() || !buffer.substring(k-url.length(), k).equals(url)) {
                    buffer.insert(k, url);
                    k = buffer.toString().indexOf("storage/", k + url.length() + 2);
                }
                else {
                    k = buffer.toString().indexOf("storage/", k + 2);
                }
            }
        	
        }
        
        //this is to solve case there we have url like /http://localhost:9080....
        //happens when you copy the url straight from document link, no the copy url link
        int m = buffer.toString().indexOf(url);
        while(m>0){
        	if(buffer.toString().charAt(m-1) == '/'){
        		buffer.deleteCharAt(m-1);
        	}else{
        		m = buffer.toString().indexOf(url, m + 2);
        	}
        }
        
        return buffer.toString();
    }

    /**
     * Sets a flag in the request to indicate content preview mode.
     * @param request
     */
    public static void setPreviewRequest(HttpServletRequest request) {
        request.setAttribute(REQUEST_KEY_PREVIEW_MODE, Boolean.TRUE);
    }

    /**
     *
     * @param request
     * @return true if the request is in preview mode.
     */
    public static boolean isPreviewRequest(HttpServletRequest request) {
        Boolean b = (Boolean)request.getAttribute(REQUEST_KEY_PREVIEW_MODE);
        return (b != null && b.booleanValue());
    }

    /**
     * Sets a flag in the request to indicate front-end edit mode.
     * @param request
     */
    public static void setEditModeRequest(HttpServletRequest request, boolean editMode) {
        request.getSession().setAttribute(SESSION_KEY_EDIT_MODE, new Boolean(editMode));
    }

    /**
     *
     * @param request
     * @return true if the request is in front-end edit mode.
     */
    public static boolean isEditModeRequest(HttpServletRequest request) {
        if (request.getSession(false) == null)
            return false;
        Boolean b = (Boolean)request.getSession().getAttribute(SESSION_KEY_EDIT_MODE);
        return (b != null && b.booleanValue());
    }

    /**
     *
     * @param key Content ID
     * @param permissionId
     * @param user The user initiating the action, whose email is excluded
     * @return Returns a comma-separated String of emails for the users with the permission for a specified content.
     */
    public static String getUserEmails(String key, String permissionId, User user) {
        try {
            SecurityService security = (SecurityService)Application.getInstance().getService(SecurityService.class);
            ContentManager cm = (ContentManager)Application.getInstance().getModule(ContentManager.class);
            ContentManagerDao dao = (ContentManagerDao)cm.getDao();

            // get group ids for the permission
            Collection principalMapList = dao.selectPermissionPrincipals(key, permissionId);

            // get active groups
            Collection groupList = new ArrayList();
            Collection groupIdList = new ArrayList();
            for (Iterator i=principalMapList.iterator(); i.hasNext();) {
                Map principalMap = (Map)i.next();
                String principalId = (String)principalMap.get("principalId");
                groupIdList.add(principalId);
            }
            DaoQuery props = new DaoQuery();
            props.addProperty(new OperatorIn("id", groupIdList.toArray(), DaoOperator.OPERATOR_AND));
            props.addProperty(new OperatorEquals("active", "1", DaoOperator.OPERATOR_AND));
            groupList = security.getGroups(props, 0, -1, null, false);

            // get user emails
            Collection emailList = new ArrayList();
            for (Iterator i=groupList.iterator(); i.hasNext();) {
                Group group = (Group)i.next();
                Collection userList = security.getGroupUsers(group.getId());
                for (Iterator j=userList.iterator(); j.hasNext();) {
                    User u = (User)j.next();
                    String email = (String)u.getProperty("email1");
                    if (u.isActive() && !emailList.contains(email) && Mailer.isValidEmail(email)) {
                        emailList.add(email);
                    }
                }
            }
            if (user.getProperty("email1") != null) {
                emailList.remove(user.getProperty("email1"));
            }

            // convert to String
            StringBuffer buffer = new StringBuffer();
            for (Iterator i=emailList.iterator(); i.hasNext();) {
                String email = (String)i.next();
                email = email.trim();
                buffer.append(email);
                if (i.hasNext())
                    buffer.append(",");
            }
            return buffer.toString();
        }
        catch (Exception e) {
            Log.getLog(ContentUtil.class).error("Unable to retrieve user emails: " + e.toString(), e);
            return "";
        }
    }

    public static boolean isContentTreeMode(HttpServletRequest request) {
        Object obj = request.getSession().getAttribute(SESSION_KEY_CMSADMIN_MODE);
        boolean isTreeMode = (obj != null) ? Boolean.valueOf(obj.toString()).booleanValue() : false;
        return isTreeMode;
    }

    public static void setContentTreeMode(HttpServletRequest request, boolean mode) {
        request.getSession().setAttribute(SESSION_KEY_CMSADMIN_MODE, new Boolean(mode));
    }

    /**
     * Sends notification of approved/rejected content to other approvers and the person who submitted the content
     * @param contentObject
     * @param user The user that approved/rejected the content
     * @param approved
     */
    public static void sendApprovalNotification(ContentObject contentObject, User user, boolean approved) {
        try {
            Application application = Application.getInstance();
            if (Boolean.valueOf(application.getProperty(ContentManager.APPLICATION_PROPERTY_NOTIFICATION_EMAIL)).booleanValue()) {

                String emails = "";

                // notify other approvers
                String approverEmails = ContentUtil.getUserEmails(contentObject.getId(), ContentManager.USE_CASE_APPROVE, user);
                if (approverEmails != null && Mailer.isValidEmail(approverEmails)) {
                    emails += approverEmails;
                }

                // notify user who submitted the content
                String submissionUserId = contentObject.getSubmissionUserId();
                if (submissionUserId != null && !submissionUserId.equals(user.getId())) {
                    SecurityService security = (SecurityService)Application.getInstance().getService(SecurityService.class);
                    User submissionUser = security.getUser(submissionUserId);
                    String submissionEmail = (String)submissionUser.getProperty("email1");
                    if (submissionEmail != null && Mailer.isValidEmail(submissionEmail)) {
                    	if(emails.trim().length() > 0)
                    		emails += "," + submissionEmail;
                    	else
                    		emails += submissionEmail;
                    }
                }

                if (emails.trim().length() > 0) {
                    String[] subjectArgs = new String[] {contentObject.getName()};
                    String subject = (approved) ?
                            application.getMessage("cms.message.email.approved.subject", subjectArgs) :
                            application.getMessage("cms.message.email.rejected.subject", subjectArgs);
                    String[] contentArgs = new String[] {contentObject.getName(), user.getUsername(), contentObject.getComments(), (String)user.getProperty("firstName"), (String)user.getProperty("lastName") };
                    String content = (approved) ?
                            application.getMessage("cms.message.email.approved.content", contentArgs) :
                            application.getMessage("cms.message.email.rejected.content", contentArgs);
                    MailUtil.sendEmail(null, true, null, emails, subject, content);
                }
            }
        }
        catch (Exception e) {
            Log.getLog(ContentUtil.class).error("Error sending approval notification for content " + contentObject + ": " + e.toString());
        }
    }


}
