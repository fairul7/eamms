package com.tms.cms.webdav;

import com.tms.cms.core.model.ContentException;
import com.tms.cms.core.model.ContentManager;
import com.tms.cms.core.model.ContentObject;
import com.tms.cms.core.model.DefaultContentObject;
import com.tms.cms.image.Image;
import com.tms.cms.image.ImageModule;
import com.tms.cms.section.Section;
import kacang.Application;
import kacang.model.DataObjectNotFoundException;
import kacang.runtime.upload.FormFile;
import kacang.runtime.upload.TempFile;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.services.storage.StorageFile;
import kacang.services.storage.StorageService;
import kacang.services.storage.StorageServlet;
import kacang.util.Log;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.net.URLDecoder;

/**
 * Servlet which adds support for WebDAV level 2. All the basic HTTP requests
 * are handled by the StorageServlet.
 */
public class ContentWebdavServlet extends StorageServlet {


    // -------------------------------------------------------------- Constants


    private static final String METHOD_HEAD = "HEAD";
    private static final String METHOD_PROPFIND = "PROPFIND";
    private static final String METHOD_PROPPATCH = "PROPPATCH";
    private static final String METHOD_MKCOL = "MKCOL";
    private static final String METHOD_COPY = "COPY";
    private static final String METHOD_MOVE = "MOVE";
    private static final String METHOD_LOCK = "LOCK";
    private static final String METHOD_UNLOCK = "UNLOCK";
    private static final String METHOD_OPTIONS = "OPTIONS";


    Log log = Log.getLog(getClass());

    /**
     * Default depth is infite.
     */
    private static final int INFINITY = 3; // To limit tree browsing a bit


    /**
     * PROPFIND - Specify a property mask.
     */
    private static final int FIND_BY_PROPERTY = 0;


    /**
     * PROPFIND - Display all properties.
     */
    private static final int FIND_ALL_PROP = 1;


    /**
     * PROPFIND - Return property names.
     */
    private static final int FIND_PROPERTY_NAMES = 2;


    /**
     * Create a new lock.
     */
    private static final int LOCK_CREATION = 0;


    /**
     * Refresh lock.
     */
    private static final int LOCK_REFRESH = 1;


    /**
     * Default lock timeout value.
     */
    private static final int DEFAULT_TIMEOUT = 3600;


    /**
     * Maximum lock timeout.
     */
    private static final int MAX_TIMEOUT = 604800;


    /**
     * Default namespace.
     */
    protected static final String DEFAULT_NAMESPACE = "DAV:";

    /**
     * MD5 message digest provider.
     */
    protected static MessageDigest md5Helper;


    /**
     * The MD5 helper object for this class.
     */
    protected static final MD5Encoder md5Encoder = new MD5Encoder();

    /**
     * Simple date format for the creation date ISO representation (partial).
     */
    protected static final SimpleDateFormat creationDateFormat =
            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

    /**
     * The set of SimpleDateFormat formats to use in getDateHeader().
     */
    protected static final SimpleDateFormat formats[] = {
        new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.US),
        new SimpleDateFormat("EEEEEE, dd-MMM-yy HH:mm:ss zzz", Locale.US),
        new SimpleDateFormat("EEE MMMM d HH:mm:ss yyyy", Locale.US)
    };

    /**
     * Array containing the safe characters set.
     */
    protected static BitSet safeCharacters;

    protected static final char[] hexadecimal =
            {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
             'A', 'B', 'C', 'D', 'E', 'F', '-', '_', '.', '*', '/'};

    // ----------------------------------------------------- Static Initializer
    static {
        creationDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));

        safeCharacters = new BitSet(256);
        int i;
        for (i = 'a'; i <= 'z'; i++) {
            safeCharacters.set(i);
        }
        for (i = 'A'; i <= 'Z'; i++) {
            safeCharacters.set(i);
        }
        for (i = '0'; i <= '9'; i++) {
            safeCharacters.set(i);
        }
        safeCharacters.set('-');
        safeCharacters.set('_');
        safeCharacters.set('.');
        safeCharacters.set('*');
        safeCharacters.set('/');
    }

    // ----------------------------------------------------- Instance Variables


    /**
     * Repository of the locks put on single resources.
     * <p>
     * Key : path <br>
     * Value : LockInfo
     */
    private Hashtable resourceLocks = new Hashtable();


    /**
     * Repository of the lock-null resources.
     * <p>
     * Key : path of the collection containing the lock-null resource<br>
     * Value : Vector of lock-null resource which are members of the
     * collection. Each element of the Vector is the path associated with
     * the lock-null resource.
     */
    private Hashtable lockNullResources = new Hashtable();


    /**
     * Vector of the heritable locks.
     * <p>
     * Key : path <br>
     * Value : LockInfo
     */
    private Vector collectionLocks = new Vector();


    /**
     * The debugging detail level for this servlet.
     */
    protected int debug = 0;


    /**
     * Secret information used to generate reasonably secure lock ids.
     */
    private String secret = "com.tms.cms.webdav";

    /**
     * Implementation for DIGEST authentication.
     */
    private DigestAuthenticator authenticator;

    // --------------------------------------------------------- Public Methods


    /**
     * Initialize this servlet.
     */
    public void init() throws ServletException {
        super.init();

        // Set properties from the initialization parameters
        String value = null;
        try {
            value = getServletConfig().getInitParameter("debug");
            debug = Integer.parseInt(value);
        }
        catch (Throwable t) {
            ;
        }
        try {
            value = getServletConfig().getInitParameter("secret");
            if (value != null)
                secret = value;
        }
        catch (Throwable t) {
            ;
        }

        // Load the MD5 helper used to calculate signatures.
        try {
            md5Helper = MessageDigest.getInstance("MD5");
        }
        catch (NoSuchAlgorithmException e) {
            log.error(e.toString(), e);
            throw new IllegalStateException();
        }

        // Instantiate a DIGEST Authenticator
        try {
            String ntlm = getServletConfig().getInitParameter("ntlm");
            if (Boolean.valueOf(ntlm).booleanValue()) {
                authenticator = new NtlmAuthenticator();
            }
            else {
                authenticator = new DigestAuthenticator();
            }
        }
        catch (Throwable t) {
            ;
        }

    }


    // ------------------------------------------------------ Protected Methods


    /**
     * Return JAXP document builder instance.
     */
    protected DocumentBuilder getDocumentBuilder() throws ServletException {
        DocumentBuilder documentBuilder = null;
        try {
            documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        }
        catch (ParserConfigurationException e) {
            throw new ServletException("webdavservlet.jaxpfailed");
        }
        return documentBuilder;
    }


    /**
     * Handles the special WebDAV methods.
     */
    protected void service(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String method = req.getMethod();

        // display request
        if (debug > 0) {
            log.debug("=== " + method + " " + req.getPathInfo() + "===");
            Enumeration e = req.getHeaderNames();
            while (e.hasMoreElements()) {
                String name = (String) e.nextElement();
                String value = req.getHeader(name);
                log.debug(name + ": " + value);
            }
/*
            if (debug > 999) {
                InputStream in = req.getInputStream();
                byte[] b = new byte[4096];
                int len = in.read(b);
                while (len > 0) {
                    System.out.print(new String(b, 0, len));
                    len = in.read(b);
                }
            }
*/
        }

        if (method.equals(METHOD_OPTIONS)) {
            doOptions(req, resp);
            return;
        }

        // Authenticate User
        if (!authenticator.authenticate(req, resp))
            return;

        // Handle Request
        if (method.equals(METHOD_PROPFIND)) {
            doPropfind(req, resp);
        }
        else if (method.equals(METHOD_PROPPATCH)) {
            doProppatch(req, resp);
        }
        else if (method.equals(METHOD_MKCOL)) {
            doMkcol(req, resp);
        }
        else if (method.equals(METHOD_COPY)) {
            doCopy(req, resp);
        }
        else if (method.equals(METHOD_MOVE)) {
            doMove(req, resp);
        }
        else if (method.equals(METHOD_LOCK)) {
            doLock(req, resp);
        }
        else if (method.equals(METHOD_UNLOCK)) {
            doUnlock(req, resp);
        }
        else {
            // DefaultServlet processing
            super.service(req, resp);
        }

    }


    /**
     * Check if the conditions specified in the optional If headers are
     * satisfied.
     *
     * @param request The servlet request we are processing
     * @param response The servlet response we are creating
     * @param co ContentObject
     * @return boolean true if the resource meets all the specified conditions,
     * and false if any of the conditions is not satisfied, in which case
     * request processing is stopped
     */
    protected boolean checkIfHeaders(HttpServletRequest request,
                                     HttpServletResponse response,
                                     ContentObject co)
            throws IOException {

        com.tms.cms.document.Document doc = (com.tms.cms.document.Document) co;
        String eTag = getETag(doc, true);
        long lastModified = doc.getDate().getTime();

        StringTokenizer commaTokenizer;

        String headerValue;

        // Checking If-Match
        headerValue = request.getHeader("If-Match");
        if (headerValue != null) {
            if (headerValue.indexOf('*') == -1) {

                commaTokenizer = new StringTokenizer(headerValue, ",");
                boolean conditionSatisfied = false;

                while (!conditionSatisfied && commaTokenizer.hasMoreTokens()) {
                    String currentToken = commaTokenizer.nextToken();
                    if (currentToken.trim().equals(eTag))
                        conditionSatisfied = true;
                }

                // If none of the given ETags match, 412 Precodition failed is
                // sent back
                if (!conditionSatisfied) {
                    response.sendError
                            (HttpServletResponse.SC_PRECONDITION_FAILED);
                    return false;
                }

            }
        }

        // Checking If-Modified-Since
        headerValue = request.getHeader("If-Modified-Since");
        if (headerValue != null) {

            // If an If-None-Match header has been specified, if modified since
            // is ignored.
            if (request.getHeader("If-None-Match") == null) {

                Date date = null;

                // Parsing the HTTP Date
                for (int i = 0; (date == null) && (i < formats.length); i++) {
                    try {
                        date = formats[i].parse(headerValue);
                    }
                    catch (ParseException e) {
                        ;
                    }
                }

                if ((date != null)
                        && (lastModified <= (date.getTime() + 1000))) {
                    // The entity has not been modified since the date
                    // specified by the client. This is not an error case.
                    response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
                    return false;
                }

            }

        }

        // Checking If-None-Match
        headerValue = request.getHeader("If-None-Match");
        if (headerValue != null) {

            boolean conditionSatisfied = false;

            if (!headerValue.equals("*")) {

                commaTokenizer = new StringTokenizer(headerValue, ",");

                while (!conditionSatisfied && commaTokenizer.hasMoreTokens()) {
                    String currentToken = commaTokenizer.nextToken();
                    if (currentToken.trim().equals(eTag))
                        conditionSatisfied = true;
                }

            }
            else {
                conditionSatisfied = true;
            }

            if (conditionSatisfied) {

                // For GET and HEAD, we should respond with
                // 304 Not Modified.
                // For every other method, 412 Precondition Failed is sent
                // back.
                if (("GET".equals(request.getMethod()))
                        || ("HEAD".equals(request.getMethod()))) {
                    response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
                    return false;
                }
                else {
                    response.sendError
                            (HttpServletResponse.SC_PRECONDITION_FAILED);
                    return false;
                }
            }

        }

        // Checking If-Unmodified-Since
        headerValue = request.getHeader("If-Unmodified-Since");
        if (headerValue != null) {

            Date date = null;

            // Parsing the HTTP Date
            for (int i = 0; (date == null) && (i < formats.length); i++) {
                try {
                    date = formats[i].parse(headerValue);
                }
                catch (ParseException e) {
                    ;
                }
            }

            if ((date != null) && (lastModified > date.getTime())) {
                // The entity has not been modified since the date
                // specified by the client. This is not an error case.
                response.sendError
                        (HttpServletResponse.SC_PRECONDITION_FAILED);
                return false;
            }

        }

        // TODO : Checking the WebDAV If header
        return true;

    }

    /**
     * Get the ETag value associated with a file.
     *
     * @param co File object
     * @param strong True if we want a strong ETag, in which case a checksum
     * of the file has to be calculated
     */
    protected String getETagValue(ContentObject co, boolean strong) {
        // FIXME : Compute a strong ETag if requested, using an MD5 digest
        // of the file contents
        return co.getId().toString() + "_" + co.getVersion();
    }


    /**
     * Get the ETag associated with a file.
     *
     * @param co File object
     * @param strong True if we want a strong ETag, in which case a checksum
     * of the file has to be calculated
     */
    protected String getETag(ContentObject co, boolean strong) {
        if (strong)
            return "\"" + getETagValue(co, strong) + "\"";
        else
            return "W/\"" + getETagValue(co, strong) + "\"";
    }

    /**
     * GET Method.
     */
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        super.doGet(req, resp);
    }

    /** Retrieves a StorageFile for the requested file.
     * @param request servlet request
     * @param response servlet response
     * @param path the absolute path to the requested file
     */
    protected StorageFile processFile(HttpServletRequest request, HttpServletResponse response, String path) throws Exception {

        try {
            // Get requested document
            ContentObject co = lookupContentObject(request, response, path);
            if (!(co instanceof com.tms.cms.document.Document))
                throw new FileNotFoundException();
            com.tms.cms.document.Document doc = (com.tms.cms.document.Document) co;
            path = doc.getFilePath();

            // Check If-headers
//             if (!checkIfHeaders(request, response, doc)) {
//                 return null;
//             }

            // ETag header
            response.setHeader("ETag", getETag(co, true));

            // Last-Modified header
            response.setDateHeader("Last-Modified", co.getDate().getTime());

            return super.processFile(request, response, path);
        }
        catch (FileNotFoundException e) {
            throw e;
        }
        catch (DataObjectNotFoundException e) {
            throw new FileNotFoundException();
        }
        catch (Exception e) {
            log.error(e.toString(), e);
            throw new FileNotFoundException(e.toString());
        }

    }

    /** Retrieves the path to the requested file.
     * @param request servlet request
     * @param response servlet response
     */
    protected String processPath(HttpServletRequest request, HttpServletResponse response) {
        String uri = request.getRequestURI();
        String servletPath = request.getServletPath();
        int servletPathLen = (servletPath != null) ? servletPath.length() : 0;
        String path = uri.substring(servletPathLen);
        if (path == null) {
            path = request.getParameter("name");
        }
        else {
            try {
                path = URLDecoder.decode(path, "UTF8");
            }
            catch (UnsupportedEncodingException e) {
                log.error("Unsupported encoding for path " + path, e);;
            }
        }
        return (path != null) ? path : "";
    }

    /**
     * Returns the ContentObject for the specified path.
     */
    public static ContentObject lookupContentObject(HttpServletRequest req, HttpServletResponse resp, String path) throws ContentException, DataObjectNotFoundException {

        // get current user
        User user = lookupUserInSession(req, resp);

        try {
            // get latest version
            Application application = Application.getInstance();
            ContentManager contentManager = (ContentManager) application.getModule(ContentManager.class);
            String[] classes = new String[]{Section.class.getName(), com.tms.cms.document.Document.class.getName(), Image.class.getName()};

            // get root
            ContentObject root = contentManager.view(ContentManager.CONTENT_TREE_ROOT_ID, user);

/*
            ContentObject root = contentManager.viewTree(ContentManager.CONTENT_TREE_ROOT_ID, classes, ContentManager.USE_CASE_VIEW, user);
            ContentObject co = root;
            boolean found = true;

            // look for the object
            if (path == null || path.trim().length() == 0 || "/".equals(path))
                return root;

            StringTokenizer st = new StringTokenizer(path, "/");
            String name;
            if (st.hasMoreTokens()) {
                name = st.nextToken();
                if (name.equals(root.getName())) {
                    found = true;
                }
            }
            while (st.hasMoreTokens() && found) {
                found = false;
                name = st.nextToken();
                if (co instanceof Section) {
                    Collection children = co.getChildren();
                    for (Iterator i = children.iterator(); i.hasNext();) {
                        ContentObject child = (ContentObject) i.next();
                        if (name.equals(child.getName())) {
                            co = child;
                            found = true;
                            break;
                        }
                    }
                }
            }

            if (!found) {
                throw new DataObjectNotFoundException();
            }

            return co;
*/
            // if path empty, return root
            if (path == null || path.trim().length() == 0 || "/".equals(path)) {
                return root;
            }

            // remove http://
            if (path.startsWith("http://")) {
                int i=path.indexOf("/webdav/");
                if (i > 0) {
                    path = path.substring(i + "/webdav/".length());
                }
            }

            // look for the object
            ContentObject current = root;
            StringTokenizer st = new StringTokenizer(path, "/");
            String name;
            while (st.hasMoreTokens()) {
                name = st.nextToken();
                // look for object
                Collection list = contentManager.viewList(null, classes, name, current.getId(), null, null, null, Boolean.FALSE, null, Boolean.FALSE, null, null, false, 0, -1, ContentManager.USE_CASE_VIEW, user);
                boolean found = false;
                for (Iterator i=list.iterator(); i.hasNext() && !found;) {
                    ContentObject tmp = (ContentObject)i.next();
                    if (name.equals(tmp.getName())) {
                        current = tmp;
                        found = true;
                    }
                }
                if (!found) {
                    throw new DataObjectNotFoundException();
                }
            }

            if (current != root) {
                return contentManager.view(current.getId(), user);
            }
            else {
                throw new DataObjectNotFoundException();
            }
        }
        catch(DataObjectNotFoundException e) {
            throw e;
        }
        catch (Exception e) {
            throw new ContentException("Unable to lookup content: " + e.toString());
        }
    }

    /**
     * Returns the children for the specified content.
     */
    public static Collection lookupContentChildren(HttpServletRequest req, HttpServletResponse resp, String id) throws ContentException {

        // get current user
        User user = lookupUserInSession(req, resp);

        try {
            // get latest version
            Application application = Application.getInstance();
            ContentManager contentManager = (ContentManager) application.getModule(ContentManager.class);
            String[] classes = new String[]{Section.class.getName(), com.tms.cms.document.Document.class.getName(), Image.class.getName()};

            // look for the children
            return contentManager.viewListWithContents(null, classes, null, id, null, null, null, Boolean.FALSE, null, Boolean.FALSE, null, null, false, 0, -1, ContentManager.USE_CASE_VIEW, user);
        }
        catch (Exception e) {
            throw new ContentException("Unable to lookup content: " + e.toString());
        }
    }

    /**
     * Returns the user ID of the user in session.
     */
    public static User lookupUserInSession(HttpServletRequest req, HttpServletResponse resp) throws ContentException {
        try {
            Application application = Application.getInstance();
            SecurityService security = (SecurityService) application.getService(SecurityService.class);
            User user = security.getCurrentUser(req);
            return user;
        }
        catch (Exception e) {
            throw new ContentException(e.toString());
        }
    }

    /**
     * OPTIONS Method.
     */
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String path = processPath(req, resp);

        resp.addHeader("DAV", "1,2");

        String methodsAllowed = null;

        try {
            ContentObject co = lookupContentObject(req, resp, path);
            methodsAllowed = "OPTIONS, GET, HEAD, POST, DELETE, TRACE, "
                    + "PROPFIND, PROPPATCH, COPY, MOVE, LOCK, UNLOCK";
            if (co instanceof Section) {
                methodsAllowed += ", PUT";
            }

            resp.addHeader("Allow", methodsAllowed);
            resp.addHeader("MS-Author-Via", "DAV");

        }
        catch (Exception e) {
            methodsAllowed = "OPTIONS, MKCOL, PUT, LOCK";
            resp.addHeader("Allow", methodsAllowed);
            return;
        }

    }


    /**
     * PROPFIND Method.
     */
    protected void doPropfind(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String path = processPath(req, resp);

//         if (debug > 99)
//             System.out.print("PROPFIND: " + path + " ");

        // Properties which are to be displayed.
        Vector properties = null;
        // Propfind depth
        int depth = INFINITY;
        // Propfind type
        int type = FIND_ALL_PROP;

        String depthStr = req.getHeader("Depth");

//         if (debug > 99)
//             System.out.print("depthStr [" + depthStr + "]: ");

        if (depthStr == null) {
            depth = INFINITY;
        }
        else {
            if (depthStr.equals("0")) {
                depth = 0;
            }
            else if (depthStr.equals("1")) {
                depth = 1;
            }
            else if (depthStr.equals("infinity")) {
                depth = INFINITY;
            }
        }

        Node propNode = null;

        DocumentBuilder documentBuilder = getDocumentBuilder();

        try {
            InputStream in = req.getInputStream();
            if (in.available() > 0) {

                Document document = documentBuilder.parse(in);

                // Get the root element of the document
                Element rootElement = document.getDocumentElement();
                NodeList childList = rootElement.getChildNodes();

                for (int i = 0; i < childList.getLength(); i++) {
                    Node currentNode = childList.item(i);
                    switch (currentNode.getNodeType()) {
                        case Node.TEXT_NODE:
                            break;
                        case Node.ELEMENT_NODE:
                            if (currentNode.getNodeName().endsWith("prop")) {
                                type = FIND_BY_PROPERTY;
                                propNode = currentNode;
                            }
                            if (currentNode.getNodeName().endsWith("propname")) {
                                type = FIND_PROPERTY_NAMES;
                            }
                            if (currentNode.getNodeName().endsWith("allprop")) {
                                type = FIND_ALL_PROP;
                            }
                            break;
                    }
                }

            }
        }
        catch (Exception e) {
            // Most likely there was no content : we use the defaults.
            log.debug("WebDAV XML parsing error", e);
        }

//         if (debug > 99)
//             System.out.print("type [" + type + "]: ");

        if (type == FIND_BY_PROPERTY) {
            properties = new Vector();
            NodeList childList = propNode.getChildNodes();

            for (int i = 0; i < childList.getLength(); i++) {
                Node currentNode = childList.item(i);
                switch (currentNode.getNodeType()) {
                    case Node.TEXT_NODE:
                        break;
                    case Node.ELEMENT_NODE:
                        String nodeName = currentNode.getNodeName();
                        String propertyName = null;
                        if (nodeName.indexOf(':') != -1) {
                            propertyName = nodeName.substring
                                    (nodeName.indexOf(':') + 1);
                        }
                        else {
                            propertyName = nodeName;
                        }
                        // href is a live property which is handled differently
                        properties.addElement(propertyName);
                        break;
                }
            }

//             if (debug > 99)
//                 System.out.print("properties [" + properties + "]: ");

        }

        boolean exists = true;
        ContentObject co = null;
        try {
            co = lookupContentObject(req, resp, path);
        }
        catch (Exception e) {
            exists = false;
            int slash = path.lastIndexOf('/');
            if (slash != -1) {
                String parentPath = path.substring(0, slash);
                Vector currentLockNullResources =
                        (Vector) lockNullResources.get(parentPath);
                if (currentLockNullResources != null) {
                    Enumeration lockNullResourcesList =
                            currentLockNullResources.elements();
                    while (lockNullResourcesList.hasMoreElements()) {
                        String lockNullPath = (String)
                                lockNullResourcesList.nextElement();
                        if (lockNullPath.equals(path)) {
                            resp.setStatus(WebdavStatus.SC_MULTI_STATUS);
                            resp.setContentType("text/xml; charset=UTF-8");
                            // Create multistatus object
                            XMLWriter generatedXML = new XMLWriter(resp.getWriter());
                            generatedXML.writeXMLHeader();
                            generatedXML.writeElement(null, "multistatus" + generateNamespaceDeclarations(), XMLWriter.OPENING);
                            parseLockNullProperties(req, resp, generatedXML, lockNullPath, type, properties);
                            generatedXML.writeElement(null, "multistatus", XMLWriter.CLOSING);
                            generatedXML.sendData();
                            return;
                        }
                    }
                }
            }
        }

        if (!exists) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, path);
            return;
        }

        resp.setStatus(WebdavStatus.SC_MULTI_STATUS);

        resp.setContentType("text/xml; charset=UTF-8");

        // Create multistatus object
        XMLWriter generatedXML = new XMLWriter(resp.getWriter());
        generatedXML.writeXMLHeader();

        generatedXML.writeElement(null, "multistatus" + generateNamespaceDeclarations(), XMLWriter.OPENING);

        if (depth == 0) {
            parseProperties(req, co, generatedXML, path, type, properties);
        }
        else {
            boolean firstNode = true;

            // The stack always contains the object of the current level
            Stack stack = new Stack();
            stack.push(co);

            // Stack of the objects one level below
            Stack stackBelow = new Stack();

            while ((!stack.isEmpty()) && (depth >= 0)) {

                co = (ContentObject) stack.pop();
                if (co instanceof DefaultContentObject) {
                    try {
                        co = ((DefaultContentObject)co).getContentObject();
                    }
                    catch (Exception e) {
                        log.error("Error getting content from stack: " + e.toString());
                        return;
                    }
                }
                if (!firstNode) {
                    parseProperties(req, co, generatedXML, path, type, properties);
                }
                else {
                    firstNode = false;
                }

                if ((Section.class.getName().equals(co.getClassName())) && (depth > 0)) {
                    try {
                        Collection children = lookupContentChildren(req, resp, co.getId());
                        for (Iterator i = children.iterator(); i.hasNext();) {
                            ContentObject child = (ContentObject) i.next();
                            stackBelow.push(child);
                        }
                    }
                    catch (Exception e) {
                        resp.sendError
                                (HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                                        path);
                        return;
                    }

                    // Displaying the lock-null resources present in that
                    // collection
                    String lockPath = path;
                    if (lockPath.endsWith("/"))
                        lockPath = lockPath.substring(0, lockPath.length() - 1);
                    Vector currentLockNullResources = (Vector) lockNullResources.get(lockPath);
                    if (currentLockNullResources != null) {
                        Enumeration lockNullResourcesList =
                                currentLockNullResources.elements();
                        while (lockNullResourcesList.hasMoreElements()) {
                            String lockNullPath = (String)
                                    lockNullResourcesList.nextElement();
                            parseLockNullProperties(req, resp, generatedXML, lockNullPath, type, properties);
                        }
                    }

                }

                if (stack.isEmpty()) {
                    depth--;
                    stack = stackBelow;
                    stackBelow = new Stack();
                }

//                generatedXML.sendData();

            }
        }

        generatedXML.writeElement(null, "multistatus",
                XMLWriter.CLOSING);

        if (debug > 99)
            log.debug(generatedXML.toString());

        generatedXML.sendData();

    }


    /**
     * PROPPATCH Method.
     */
    protected void doProppatch(HttpServletRequest req,
                               HttpServletResponse resp)
            throws ServletException, IOException {

        if (isLocked(req, resp)) {
            resp.sendError(WebdavStatus.SC_LOCKED);
            return;
        }

        resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);

    }


    /**
     * MKCOL Method.
     */
    protected void doMkcol(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        if (isLocked(req, resp)) {
            resp.sendError(WebdavStatus.SC_LOCKED);
            return;
        }

        try {
            // get path and name
            String path = processPath(req, resp);
            int i = (path.endsWith("/")) ? path.lastIndexOf("/", path.length()-2) : path.lastIndexOf("/");
            String name = path.substring(i + 1);
            if (name.endsWith("/")) {
                name = name.substring(0, name.length()-1);
            }
            if (i > 0) {
                path = path.substring(0, i);
            }
            else {
                path = "/";
            }

            // get current user
            User user = lookupUserInSession(req, resp);

            // get parent
            Application application = Application.getInstance();
            ContentManager contentManager = (ContentManager) application.getModule(ContentManager.class);
            ContentObject parent = null;
            try {
                parent = lookupContentObject(req, resp, path);

                // check permission
                if (!contentManager.hasPermission(parent, user.getId(), ContentManager.USE_CASE_CREATE)) {
                    authenticator.sendChallenge(req, resp);
                    return;
                }
            }
            catch (ContentException e) {
                resp.setStatus(WebdavStatus.SC_CONFLICT);
                return;
            }

            // create new section
            Section co = new Section();
            co.setName(name);
            co.setParentId(parent.getId());
            contentManager.createNew(co, user);

            // auto approve section
            try {
                ContentObject tmp = contentManager.submitForApproval(co.getId(), user);
                contentManager.approve(tmp, user);
//                contentManager.publish(tmp.getId(), false, user);
            }
            catch (ContentException e) {
                // ignore
            }

            resp.setStatus(WebdavStatus.SC_CREATED);
        }
        catch (Exception e) {
            log.error(e.toString(), e);
            resp.sendError(WebdavStatus.SC_INTERNAL_SERVER_ERROR, e.toString());
        }
    }


    /**
     * DELETE Method.
     */
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        try {
            // get path and name
            String path = processPath(req, resp);

            // get current user
            User user = lookupUserInSession(req, resp);

            // get object
            Application application = Application.getInstance();
            ContentManager contentManager = (ContentManager) application.getModule(ContentManager.class);
            ContentObject co = lookupContentObject(req, resp, path);

            // check permission
            if (!contentManager.hasPermission(co, user.getId(), ContentManager.USE_CASE_DELETE)) {
                authenticator.sendChallenge(req, resp);
                return;
            }

            // check for lock
            if (isLocked(req, resp, path, co, user.getId())) {
                resp.sendError(WebdavStatus.SC_LOCKED);
                return;
            }

            // delete object
            contentManager.delete(co.getId(), true, user);

            resp.setStatus(WebdavStatus.SC_OK);
        }
        catch (Exception e) {
            log.error(e.toString(), e);
            resp.sendError(WebdavStatus.SC_INTERNAL_SERVER_ERROR, e.toString());
        }

    }


    /**
     * Process a PUT request for the specified resource.
     *
     * @param req The servlet request we are processing
     * @param resp The servlet response we are creating
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet-specified error occurs
     */
    protected void doPut(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        try {
            // get path and name
            String path = processPath(req, resp);

            // check for existing object
            boolean exists = true;
            ContentObject current = null;
            try {
                current = lookupContentObject(req, resp, path);
//                resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
//                return;
            }
            catch (Exception e) {
                exists = false;
            }

            // get name and parent path
            String parentPath = path;
            int i = parentPath.lastIndexOf("/");
            String name = parentPath.substring(i + 1);
            if (i > 0) {
                parentPath = parentPath.substring(0, i);
            }
            else {
                parentPath = "/";
            }

            // get current user
            User user = lookupUserInSession(req, resp);

            // get parent
            ContentObject parent = null;
            try {
                parent = lookupContentObject(req, resp, parentPath);
            }
            catch (Exception e) {
                resp.setStatus(WebdavStatus.SC_CONFLICT);
                return;
            }

            Application application = Application.getInstance();
            ContentManager contentManager = (ContentManager) application.getModule(ContentManager.class);
            if (!exists) {
                // check for lock
                if (isLocked(req, resp)) {
                    resp.sendError(WebdavStatus.SC_LOCKED);
                    return;
                }

                // check permission
                if (!contentManager.hasPermission(parent, user.getId(), ContentManager.USE_CASE_CREATE)) {
                    authenticator.sendChallenge(req, resp);
                    return;
                }

                // construct content
                com.tms.cms.document.Document co;
                FormFile formFile = new TempFile(req.getInputStream());
                formFile.setFileName(name);
                formFile.setFileSize(req.getContentLength());
                formFile.setContentType(getServletContext().getMimeType(name));
                StorageFile file = new StorageFile("/", formFile);
                if (Arrays.asList(ImageModule.IMAGE_CONTENT_TYPES).contains(file.getContentType())) {
                    // image
                    co = new com.tms.cms.image.Image();
                }
                else {
                    // document
                    co = new com.tms.cms.document.Document();
                }

                // create content
                co.setName(name);
                co.setParentId(parent.getId());
                co.setStorageFile(file);
                co.setFileName(file.getName());
                co.setFilePath(file.getAbsolutePath());
                co.setFileSize(file.getSize());
                co.setContentType(file.getContentType());
                contentManager.createNew(co, user);

                // auto approve content
                try {
                    ContentObject tmp = contentManager.submitForApproval(co.getId(), user);
                    contentManager.approve(tmp, user);
//                    contentManager.publish(tmp.getId(), false, user);
                }
                catch (ContentException e) {
                    // ignore
                }

                // set status
                resp.setStatus(WebdavStatus.SC_CREATED);
            }
            else {
                // check for lock
                if (isLocked(req, resp, path, current, user.getId())) {
                    resp.sendError(WebdavStatus.SC_LOCKED);
                    return;
                }

                // check permission
                if (!contentManager.hasPermission(current, user.getId(), ContentManager.USE_CASE_CHECKOUT)) {
                    authenticator.sendChallenge(req, resp);
                    return;
                }

                // checkout current object
                current = contentManager.checkOutToEdit(current.getId(), user);

                // save content
                FormFile formFile = new TempFile(req.getInputStream());
                formFile.setFileName(name);
                formFile.setFileSize(req.getContentLength());
                formFile.setContentType(getServletContext().getMimeType(name));
                StorageFile file = new StorageFile("/", formFile);
                com.tms.cms.document.Document doc = (com.tms.cms.document.Document) current;
                doc.setStorageFile(file);
                doc.setFileName(file.getName());
                doc.setFilePath(file.getAbsolutePath());
                doc.setFileSize(file.getSize());
                doc.setContentType(file.getContentType());
                contentManager.save(doc, user);

                // auto approve content
                try {
                    ContentObject tmp = contentManager.submitForApproval(doc.getId(), user);
                    contentManager.approve(tmp, user);
//                    contentManager.publish(tmp.getId(), false, user);
                }
                catch (ContentException e) {
                    // ignore
                }
                
                // set status
                resp.setStatus(WebdavStatus.SC_OK);
            }


            // Removing any lock-null resource which would be present
            lockNullResources.remove(path);
        }
        catch (Exception e) {
            log.error(e.toString(), e);
            resp.sendError(WebdavStatus.SC_INTERNAL_SERVER_ERROR, e.toString());
        }


    }

    /**
     * COPY Method.
     */
    protected void doCopy(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        try {
            // get current user
            User user = lookupUserInSession(req, resp);

            // Parsing destination header
            String destinationPath = req.getHeader("Destination");
            if (destinationPath == null) {
                resp.sendError(WebdavStatus.SC_BAD_REQUEST);
                return;
            }
            int protocolIndex = destinationPath.indexOf("://");
            if (protocolIndex >= 0) {
                // if the Destination URL contains the protocol, we can safely
                // trim everything upto the first "/" character after "://"
                int firstSeparator =
                        destinationPath.indexOf("/", protocolIndex + 4);
                if (firstSeparator < 0) {
                    destinationPath = "/";
                }
                else {
                    destinationPath = destinationPath.substring(firstSeparator);
                }
            }
            else {
                String hostName = req.getServerName();
                if ((hostName != null) && (destinationPath.startsWith(hostName))) {
                    destinationPath = destinationPath.substring(hostName.length());
                }
                int portIndex = destinationPath.indexOf(":");
                if (portIndex >= 0) {
                    destinationPath = destinationPath.substring(portIndex);
                }
                if (destinationPath.startsWith(":")) {
                    int firstSeparator = destinationPath.indexOf("/");
                    if (firstSeparator < 0) {
                        destinationPath = "/";
                    }
                    else {
                        destinationPath =
                                destinationPath.substring(firstSeparator);
                    }
                }
            }
            String contextPath = req.getContextPath();
            if ((contextPath != null) &&
                    (destinationPath.startsWith(contextPath))) {
                destinationPath = destinationPath.substring(contextPath.length());
            }
            String pathInfo = req.getPathInfo();
            if (pathInfo != null) {
                String servletPath = req.getServletPath();
                if ((servletPath != null) &&
                        (destinationPath.startsWith(servletPath))) {
                    destinationPath = destinationPath
                            .substring(servletPath.length());
                }
            }
            destinationPath = RequestUtil.URLDecode(normalize(destinationPath), "UTF8");

            // get destination name and parent path
            int lastIndex = destinationPath.lastIndexOf("/");
            String parentPath = destinationPath.substring(0, lastIndex);

            // get source path
            String path = processPath(req, resp);
            if (destinationPath.equals(path)) {
                resp.sendError(WebdavStatus.SC_FORBIDDEN);
                return;
            }

            // parse overwrite header
            boolean overwrite = true;
            String overwriteHeader = req.getHeader("Overwrite");
            if (overwriteHeader != null) {
                if (overwriteHeader.equalsIgnoreCase("T")) {
                    overwrite = true;
                }
                else {
                    overwrite = false;
                }
            }

            // Do the work
            ContentObject co = lookupContentObject(req, resp, path);
            Section parent = (Section) lookupContentObject(req, resp, parentPath);

            // check permission
            Application application = Application.getInstance();
            ContentManager contentManager = (ContentManager) application.getModule(ContentManager.class);
            if (!contentManager.hasPermission(parent, user.getId(), ContentManager.USE_CASE_CREATE)) {
                authenticator.sendChallenge(req, resp);
                return;
            }

            boolean result = copyContentObject(req, resp, user, co, parent, destinationPath, overwrite);
            List objQueue = new ArrayList();
            if (result)
                objQueue.add(co);
            while (objQueue.size() > 0) {
                ContentObject obj = (ContentObject) objQueue.get(0);
                objQueue.remove(0);
                if (Section.class.getName().equals(co.getClassName())) {
                    Collection children = lookupContentChildren(req, resp, co.getId());
                    for (Iterator i = children.iterator(); i.hasNext();) {
                        ContentObject child = (ContentObject) i.next();
                        destinationPath += "/" + child.getName();
                        result = copyContentObject(req, resp, user, child, (Section) obj, destinationPath, overwrite);
                        if (result)
                            objQueue.add(child);
                    }
                }
            }

            resp.setStatus(WebdavStatus.SC_CREATED);
        }
        catch (Exception e) {
            log.error(e.toString(), e);
            resp.sendError(WebdavStatus.SC_INTERNAL_SERVER_ERROR, e.toString());
        }
    }

    /**
     * Copies a ContentObject to a specified destination.
     */
    protected boolean copyContentObject(HttpServletRequest req, HttpServletResponse resp, User user, ContentObject co, Section parent, String destinationPath, boolean overwrite) throws ContentException {
        try {
            // get destination name and parent path
            int lastIndex = destinationPath.lastIndexOf("/");
            String destName = destinationPath.substring(lastIndex + 1);

            // Check for existing destination
            Application application = Application.getInstance();
            ContentManager contentManager = (ContentManager) application.getModule(ContentManager.class);
            ContentObject dest = null;
            boolean exists = true;
            try {
                dest = lookupContentObject(req, resp, destinationPath);
            }
            catch (Exception e) {
                exists = false;
            }

            // check for lock
            if (isLocked(req, resp, destinationPath, dest, user.getId())) {
                //resp.sendError(WebdavStatus.SC_LOCKED);
                return false;
            }

            if (overwrite) {
                // Delete destination resource, if it exists
                if (exists) {
                    try {
                        contentManager.delete(dest.getId(), true, user);
                    }
                    catch (Exception e) {
                        //resp.setStatus(WebdavStatus.SC_NO_CONTENT);
                    }
                }
                else {
                    resp.setStatus(WebdavStatus.SC_CREATED);
                }

            }
            else {
                // If the destination exists, then it's a conflict
                if (exists) {
                    resp.sendError(WebdavStatus.SC_PRECONDITION_FAILED);
                    return false;
                }
            }

            // construct content
            if (co instanceof com.tms.cms.document.Document) {
                com.tms.cms.document.Document doc = (com.tms.cms.document.Document) co;
                StorageService storage = (StorageService)application.getService(StorageService.class);
                InputStream in = storage.getInputStream(new StorageFile(doc.getFilePath()));
                StorageFile sf = new StorageFile(destName);
                sf.setInputStream(in);
                sf.setParentDirectoryPath("/");
                doc.setStorageFile(sf);
                doc.setFileName(sf.getName());
                doc.setFilePath(sf.getAbsolutePath());
                doc.setFileSize(sf.getSize());
                doc.setContentType(sf.getContentType());
            }

            // create content
            co.setName(destName);
            co.setId(null);
            co.setParentId(parent.getId());
            contentManager.createNew(co, user);

            // auto approve content
            try {
                ContentObject tmp = contentManager.submitForApproval(co.getId(), user);
                contentManager.approve(tmp, user);
//                contentManager.publish(tmp.getId(), false, user);
            }
            catch (ContentException e) {
                // ignore
            }

            return true;
        }
        catch (Exception e) {
            throw new ContentException(e.toString());
        }
    }

    /**
     * MOVE Method.
     */
    protected void doMove(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        try {
            // get current user
            User user = lookupUserInSession(req, resp);

            // Parsing destination header
            String destinationPath = req.getHeader("Destination");
            if (destinationPath == null) {
                resp.sendError(WebdavStatus.SC_BAD_REQUEST);
                return;
            }
            int protocolIndex = destinationPath.indexOf("://");
            if (protocolIndex >= 0) {
                // if the Destination URL contains the protocol, we can safely
                // trim everything upto the first "/" character after "://"
                int firstSeparator =
                        destinationPath.indexOf("/", protocolIndex + 4);
                if (firstSeparator < 0) {
                    destinationPath = "/";
                }
                else {
                    destinationPath = destinationPath.substring(firstSeparator);
                }
            }
            else {
                String hostName = req.getServerName();
                if ((hostName != null) && (destinationPath.startsWith(hostName))) {
                    destinationPath = destinationPath.substring(hostName.length());
                }
                int portIndex = destinationPath.indexOf(":");
                if (portIndex >= 0) {
                    destinationPath = destinationPath.substring(portIndex);
                }
                if (destinationPath.startsWith(":")) {
                    int firstSeparator = destinationPath.indexOf("/");
                    if (firstSeparator < 0) {
                        destinationPath = "/";
                    }
                    else {
                        destinationPath =
                                destinationPath.substring(firstSeparator);
                    }
                }
            }
            String contextPath = req.getContextPath();
            if ((contextPath != null) &&
                    (destinationPath.startsWith(contextPath))) {
                destinationPath = destinationPath.substring(contextPath.length());
            }
            String pathInfo = req.getPathInfo();
            if (pathInfo != null) {
                String servletPath = req.getServletPath();
                if ((servletPath != null) &&
                        (destinationPath.startsWith(servletPath))) {
                    destinationPath = destinationPath
                            .substring(servletPath.length());
                }
            }
            destinationPath = RequestUtil.URLDecode(normalize(destinationPath), "UTF8");

            // get destination name and parent path
            int lastIndex = destinationPath.lastIndexOf("/");
            String destName = destinationPath.substring(lastIndex + 1);
            String parentPath = destinationPath.substring(0, lastIndex);

            // get source path
            String path = processPath(req, resp);
            if (destinationPath.equals(path)) {
                resp.sendError(WebdavStatus.SC_FORBIDDEN);
                return;
            }

            // parse overwrite header
            boolean overwrite = true;
            String overwriteHeader = req.getHeader("Overwrite");
            if (overwriteHeader != null) {
                if (overwriteHeader.equalsIgnoreCase("T")) {
                    overwrite = true;
                }
                else {
                    overwrite = false;
                }
            }

            // Check for existing destination
            Application application = Application.getInstance();
            ContentManager contentManager = (ContentManager) application.getModule(ContentManager.class);
            ContentObject dest = null;
            boolean exists = true;
            try {
                dest = lookupContentObject(req, resp, destinationPath);
            }
            catch (Exception e) {
                exists = false;
            }

            // check for lock
            if (isLocked(req, resp, destinationPath, dest, user.getId())) {
                resp.sendError(WebdavStatus.SC_LOCKED);
                return;
            }

            if (overwrite) {
                // Delete destination resource, if it exists
                if (exists) {
                    try {
                        // check permission
                        if (!contentManager.hasPermission(dest, user.getId(), ContentManager.USE_CASE_DELETE)) {
                            authenticator.sendChallenge(req, resp);
                            return;
                        }

                        contentManager.delete(dest.getId(), true, user);
                    }
                    catch (Exception e) {
                        //resp.setStatus(WebdavStatus.SC_NO_CONTENT);
                    }
                }
                else {
                    resp.setStatus(WebdavStatus.SC_CREATED);
                }

            }
            else {
                // If the destination exists, then it's a conflict
                if (exists) {
                    resp.sendError(WebdavStatus.SC_PRECONDITION_FAILED);
                    return;
                }
            }

            // Do the work
            ContentObject co = lookupContentObject(req, resp, path);
            if (!destName.equals(co.getName())) {
                // check permission
                if (!contentManager.hasPermission(co, user.getId(), ContentManager.USE_CASE_CHECKOUT)) {
                    authenticator.sendChallenge(req, resp);
                    return;
                }

                // Rename
                co = contentManager.checkOutToEdit(co.getId(), user);
                co.setName(destName);

                // save content
                try {
                    contentManager.save(co, user);

                    // auto approve content
                    ContentObject tmp = contentManager.submitForApproval(co.getId(), user);
                    contentManager.approve(tmp, user);
//                contentManager.publish(tmp.getId(), false, user);
                }
                catch (ContentException e) {
                    // ignore
                }

            }
            else {
                // check permission
                if (!contentManager.hasPermission(co, user.getId(), ContentManager.USE_CASE_MOVE)) {
                    authenticator.sendChallenge(req, resp);
                    return;
                }

                // Move
                Section parent = (Section) lookupContentObject(req, resp, parentPath);
                contentManager.move(co.getId(), parent.getId(), user);
            }
            resp.setStatus(WebdavStatus.SC_CREATED);
        }
        catch (Exception e) {
            log.error(e.toString(), e);
            resp.sendError(WebdavStatus.SC_INTERNAL_SERVER_ERROR, e.toString());
        }
    }

    /**
     * LOCK Method.
     */
    protected void doLock(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        try {
            LockInfo lock = new LockInfo();

            // Parsing lock request

            // Parsing depth header

            String depthStr = req.getHeader("Depth");

            if (depthStr == null) {
                lock.depth = INFINITY;
            }
            else {
                if (depthStr.equals("0")) {
                    lock.depth = 0;
                }
                else {
                    lock.depth = INFINITY;
                }
            }

            // Parsing timeout header

            int lockDuration = DEFAULT_TIMEOUT;
            String lockDurationStr = req.getHeader("Timeout");
            if (lockDurationStr == null) {
                lockDuration = DEFAULT_TIMEOUT;
            }
            else {
                if (lockDurationStr.startsWith("Second-")) {
                    lockDuration =
                            (new Integer(lockDurationStr.substring(7))).intValue();
                }
                else {
                    if (lockDurationStr.equalsIgnoreCase("infinity")) {
                        lockDuration = MAX_TIMEOUT;
                    }
                    else {
                        try {
                            lockDuration =
                                    (new Integer(lockDurationStr)).intValue();
                        }
                        catch (NumberFormatException e) {
                            lockDuration = MAX_TIMEOUT;
                        }
                    }
                }
                if (lockDuration == 0) {
                    lockDuration = DEFAULT_TIMEOUT;
                }
                if (lockDuration > MAX_TIMEOUT) {
                    lockDuration = MAX_TIMEOUT;
                }
            }
            lock.expiresAt = System.currentTimeMillis() + (lockDuration * 1000);

            int lockRequestType = LOCK_CREATION;

            Node lockInfoNode = null;

            DocumentBuilder documentBuilder = getDocumentBuilder();

            try {
                Document document = documentBuilder.parse(new InputSource
                        (req.getInputStream()));

                // Get the root element of the document
                Element rootElement = document.getDocumentElement();
                lockInfoNode = rootElement;
            }
            catch (Exception e) {
                log.debug("Error parsing XML: " + e.toString());
                lockRequestType = LOCK_REFRESH;
            }

            if (lockInfoNode != null) {

                // Reading lock information

                NodeList childList = lockInfoNode.getChildNodes();
                StringWriter strWriter = null;
                DOMWriter domWriter = null;

                Node lockScopeNode = null;
                Node lockTypeNode = null;
                Node lockOwnerNode = null;

                for (int i = 0; i < childList.getLength(); i++) {
                    Node currentNode = childList.item(i);
                    switch (currentNode.getNodeType()) {
                        case Node.TEXT_NODE:
                            break;
                        case Node.ELEMENT_NODE:
                            String nodeName = currentNode.getNodeName();
                            if (nodeName.endsWith("lockscope")) {
                                lockScopeNode = currentNode;
                            }
                            if (nodeName.endsWith("locktype")) {
                                lockTypeNode = currentNode;
                            }
                            if (nodeName.endsWith("owner")) {
                                lockOwnerNode = currentNode;
                            }
                            break;
                    }
                }

                if (lockScopeNode != null) {

                    childList = lockScopeNode.getChildNodes();
                    for (int i = 0; i < childList.getLength(); i++) {
                        Node currentNode = childList.item(i);
                        switch (currentNode.getNodeType()) {
                            case Node.TEXT_NODE:
                                break;
                            case Node.ELEMENT_NODE:
                                String tempScope = currentNode.getNodeName();
                                if (tempScope.indexOf(':') != -1) {
                                    lock.scope = tempScope.substring
                                            (tempScope.indexOf(':') + 1);
                                }
                                else {
                                    lock.scope = tempScope;
                                }
                                break;
                        }
                    }

                    if (lock.scope == null) {
                        // Bad request
                        resp.setStatus(WebdavStatus.SC_BAD_REQUEST);
                    }

                }
                else {
                    // Bad request
                    resp.setStatus(WebdavStatus.SC_BAD_REQUEST);
                }

                if (lockTypeNode != null) {

                    childList = lockTypeNode.getChildNodes();
                    for (int i = 0; i < childList.getLength(); i++) {
                        Node currentNode = childList.item(i);
                        switch (currentNode.getNodeType()) {
                            case Node.TEXT_NODE:
                                break;
                            case Node.ELEMENT_NODE:
                                String tempType = currentNode.getNodeName();
                                if (tempType.indexOf(':') != -1) {
                                    lock.type =
                                            tempType.substring(tempType.indexOf(':') + 1);
                                }
                                else {
                                    lock.type = tempType;
                                }
                                break;
                        }
                    }

                    if (lock.type == null) {
                        // Bad request
                        resp.setStatus(WebdavStatus.SC_BAD_REQUEST);
                    }

                }
                else {
                    // Bad request
                    resp.setStatus(WebdavStatus.SC_BAD_REQUEST);
                }

                if (lockOwnerNode != null) {

                    childList = lockOwnerNode.getChildNodes();
                    for (int i = 0; i < childList.getLength(); i++) {
                        Node currentNode = childList.item(i);
                        switch (currentNode.getNodeType()) {
                            case Node.TEXT_NODE:
                                lock.owner += currentNode.getNodeValue();
                                break;
                            case Node.ELEMENT_NODE:
                                strWriter = new StringWriter();
                                domWriter = new DOMWriter(strWriter, true);
                                domWriter.print(currentNode);
                                lock.owner += strWriter.toString();
                                break;
                        }
                    }

                    if (lock.owner == null) {
                        // Bad request
                        resp.setStatus(WebdavStatus.SC_BAD_REQUEST);
                    }

                }
                else {
                    lock.owner = new String();
                }

            }

            String path = processPath(req, resp);

            lock.path = path;

            // get object
            boolean exists = true;
            ContentObject co = null;
            User user = lookupUserInSession(req, resp);
            try {
                // get current user
                co = lookupContentObject(req, resp, path);

                // check permission
                Application application = Application.getInstance();
                ContentManager contentManager = (ContentManager) application.getModule(ContentManager.class);
                if (!contentManager.hasPermission(co, user.getId(), ContentManager.USE_CASE_CHECKOUT)) {
                    authenticator.sendChallenge(req, resp);
                    return;
                }
            }
            catch (DataObjectNotFoundException e) {
                exists = false;
            }
            catch (ContentException e) {
                exists = false;
            }

            // check for lock
            if (isLocked(req, resp, path, co, user.getId())) {
                resp.sendError(WebdavStatus.SC_LOCKED);
                return;
            }

            if (lockRequestType == LOCK_CREATION) {

                // Generating lock id
                String lockTokenStr = req.getServletPath() + "-" + lock.type + "-"
                        + lock.scope + "-" + req.getUserPrincipal() + "-"
                        + lock.depth + "-" + lock.owner + "-" + lock.tokens + "-"
                        + lock.expiresAt + "-" + System.currentTimeMillis() + "-"
                        + secret;

                String lockToken =
                        md5Encoder.encode(md5Helper.digest(lockTokenStr.getBytes()));
                /*
                            if ( (exists) && (object instanceof DirContext) &&
                                 (lock.depth == INFINITY) ) {

                                // Locking a collection (and all its member resources)

                                // Checking if a child resource of this collection is
                                // already locked
                                Vector lockPaths = new Vector();
                                locksList = collectionLocks.elements();
                                while (locksList.hasMoreElements()) {
                                    LockInfo currentLock = (LockInfo) locksList.nextElement();
                                    if (currentLock.hasExpired()) {
                                        resourceLocks.remove(currentLock.path);
                                        continue;
                                    }
                                    if ( (currentLock.path.startsWith(lock.path)) &&
                                         ((currentLock.isExclusive()) ||
                                          (lock.isExclusive())) ) {
                                        // A child collection of this collection is locked
                                        lockPaths.addElement(currentLock.path);
                                    }
                                }
                                locksList = resourceLocks.elements();
                                while (locksList.hasMoreElements()) {
                                    LockInfo currentLock = (LockInfo) locksList.nextElement();
                                    if (currentLock.hasExpired()) {
                                        resourceLocks.remove(currentLock.path);
                                        continue;
                                    }
                                    if ( (currentLock.path.startsWith(lock.path)) &&
                                         ((currentLock.isExclusive()) ||
                                          (lock.isExclusive())) ) {
                                        // A child resource of this collection is locked
                                        lockPaths.addElement(currentLock.path);
                                    }
                                }

                                if (!lockPaths.isEmpty()) {

                                    // One of the child paths was locked
                                    // We generate a multistatus error report

                                    Enumeration lockPathsList = lockPaths.elements();

                                    resp.setStatus(WebdavStatus.SC_CONFLICT);

                                    XMLWriter generatedXML = new XMLWriter();
                                    generatedXML.writeXMLHeader();

                                    generatedXML.writeElement
                                        (null, "multistatus" + generateNamespaceDeclarations(),
                                         XMLWriter.OPENING);

                                    while (lockPathsList.hasMoreElements()) {
                                        generatedXML.writeElement(null, "response",
                                                                  XMLWriter.OPENING);
                                        generatedXML.writeElement(null, "href",
                                                                  XMLWriter.OPENING);
                                        generatedXML
                                            .writeText((String) lockPathsList.nextElement());
                                        generatedXML.writeElement(null, "href",
                                                                  XMLWriter.CLOSING);
                                        generatedXML.writeElement(null, "status",
                                                                  XMLWriter.OPENING);
                                        generatedXML
                                            .writeText("HTTP/1.1 " + WebdavStatus.SC_LOCKED
                                                       + " " + WebdavStatus
                                                       .getStatusText(WebdavStatus.SC_LOCKED));
                                        generatedXML.writeElement(null, "status",
                                                                  XMLWriter.CLOSING);

                                        generatedXML.writeElement(null, "response",
                                                                  XMLWriter.CLOSING);
                                    }

                                    generatedXML.writeElement(null, "multistatus",
                                                          XMLWriter.CLOSING);

                                    Writer writer = resp.getWriter();
                                    writer.write(generatedXML.toString());
                                    writer.close();

                                    return;

                                }

                                boolean addLock = true;

                                // Checking if there is already a shared lock on this path
                                locksList = collectionLocks.elements();
                                while (locksList.hasMoreElements()) {

                                    LockInfo currentLock = (LockInfo) locksList.nextElement();
                                    if (currentLock.path.equals(lock.path)) {

                                        if (currentLock.isExclusive()) {
                                            resp.sendError(WebdavStatus.SC_LOCKED);
                                            return;
                                        } else {
                                            if (lock.isExclusive()) {
                                                resp.sendError(WebdavStatus.SC_LOCKED);
                                                return;
                                            }
                                        }

                                        currentLock.tokens.addElement(lockToken);
                                        lock = currentLock;
                                        addLock = false;

                                    }

                                }

                                if (addLock) {
                                    lock.tokens.addElement(lockToken);
                                    collectionLocks.addElement(lock);
                                }

                            } else {*/
                if (true) {

                    // Locking a single resource

                    // Retrieving an already existing lock on that resource
                    LockInfo presentLock = (LockInfo) resourceLocks.get(lock.path);
                    if (presentLock != null) {

                        if ((presentLock.isExclusive()) || (lock.isExclusive())) {
                            // If either lock is exclusive, the lock can't be
                            // granted
                            resp.sendError(WebdavStatus.SC_PRECONDITION_FAILED);
                            return;
                        }
                        else {
                            presentLock.tokens.addElement(lockToken);
                            lock = presentLock;
                        }

                    }
                    else {

                        lock.tokens.addElement(lockToken);
                        resourceLocks.put(lock.path, lock);

/*
                        // Checking if a resource exists at this path
                        exists = true;
                        try {
                            co = lookupContentObject(req, resp, path);
                        } catch (ContentException e) {
                            exists = false;
                        }*/
                        if (!exists) {

                            // "Creating" a lock-null resource
                            int slash = lock.path.lastIndexOf('/');
                            String parentPath = lock.path.substring(0, slash);

                            Vector lockNulls =
                                    (Vector) lockNullResources.get(parentPath);
                            if (lockNulls == null) {
                                lockNulls = new Vector();
                                lockNullResources.put(parentPath, lockNulls);
                            }

                            lockNulls.addElement(lock.path);

                        }

                    }

                    if (exists) {
                        // Check out the ContentObject
                        Application application = Application.getInstance();
                        ContentManager contentManager = (ContentManager) application.getModule(ContentManager.class);
                        try {
                            contentManager.checkOutToEdit(co.getId(), user);
                        }
                        catch (Exception e) {
                            resp.setStatus(WebdavStatus.SC_UNAUTHORIZED);
                            return;
                        }
                    }

                }

            }

            if (lockRequestType == LOCK_REFRESH) {

                String ifHeader = req.getHeader("If");
                if (ifHeader == null)
                    ifHeader = "";

                // Checking resource locks

                LockInfo toRenew = (LockInfo) resourceLocks.get(path);
                if (toRenew != null) {
                    Enumeration tokenList = null;
                    if (lock != null) {

                        // At least one of the tokens of the locks must have been given

                        tokenList = toRenew.tokens.elements();
                        while (tokenList.hasMoreElements()) {
                            String token = (String) tokenList.nextElement();
                            if (ifHeader.indexOf(token) != -1) {
                                toRenew.expiresAt = lock.expiresAt;
                                lock = toRenew;
                            }
                        }

                    }

                    // Checking inheritable collection locks

                    Enumeration collectionLocksList = collectionLocks.elements();
                    while (collectionLocksList.hasMoreElements()) {
                        toRenew = (LockInfo) collectionLocksList.nextElement();
                        if (path.equals(toRenew.path)) {

                            tokenList = toRenew.tokens.elements();
                            while (tokenList.hasMoreElements()) {
                                String token = (String) tokenList.nextElement();
                                if (ifHeader.indexOf(token) != -1) {
                                    toRenew.expiresAt = lock.expiresAt;
                                    lock = toRenew;
                                }
                            }

                        }
                    }
                }
            }

            // Set the status, then generate the XML response containing
            // the lock information
            XMLWriter generatedXML = new XMLWriter();
            generatedXML.writeXMLHeader();
            generatedXML.writeElement(null, "prop"
                    + generateNamespaceDeclarations(),
                    XMLWriter.OPENING);

            generatedXML.writeElement(null, "lockdiscovery",
                    XMLWriter.OPENING);

            lock.toXML(generatedXML, true);

            generatedXML.writeElement(null, "lockdiscovery",
                    XMLWriter.CLOSING);

            generatedXML.writeElement(null, "prop", XMLWriter.CLOSING);

            resp.setStatus(WebdavStatus.SC_OK);
            resp.setContentType("text/xml; charset=UTF-8");
            Writer writer = resp.getWriter();
            writer.write(generatedXML.toString());

            if (debug > 99)
                log.debug(generatedXML.toString());

            writer.close();
        }
        catch (Exception e) {
            log.error(e.toString(), e);
            resp.sendError(WebdavStatus.SC_INTERNAL_SERVER_ERROR, e.toString());
        }

    }


    /**
     * UNLOCK Method.
     */
    protected void doUnlock(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        if (isLocked(req, resp)) {
            resp.sendError(WebdavStatus.SC_LOCKED);
            return;
        }

        String path = processPath(req, resp);

        String lockTokenHeader = req.getHeader("Lock-Token");
        if (lockTokenHeader == null)
            lockTokenHeader = "";

        // Checking resource locks

        LockInfo lock = (LockInfo) resourceLocks.get(path);
        Enumeration tokenList = null;
        if (lock != null) {

            // At least one of the tokens of the locks must have been given

            tokenList = lock.tokens.elements();
            while (tokenList.hasMoreElements()) {
                String token = (String) tokenList.nextElement();
                if (lockTokenHeader.indexOf(token) != -1) {
                    lock.tokens.removeElement(token);
                }
            }

            if (lock.tokens.isEmpty()) {
                resourceLocks.remove(path);
                // Removing any lock-null resource which would be present
                lockNullResources.remove(path);
            }

        }

        // Checking inheritable collection locks

        Enumeration collectionLocksList = collectionLocks.elements();
        while (collectionLocksList.hasMoreElements()) {
            lock = (LockInfo) collectionLocksList.nextElement();
            if (path.equals(lock.path)) {

                tokenList = lock.tokens.elements();
                while (tokenList.hasMoreElements()) {
                    String token = (String) tokenList.nextElement();
                    if (lockTokenHeader.indexOf(token) != -1) {
                        lock.tokens.removeElement(token);
                        break;
                    }
                }

                if (lock.tokens.isEmpty()) {
                    collectionLocks.removeElement(lock);
                    // Removing any lock-null resource which would be present
                    lockNullResources.remove(path);
                }

            }
        }

        resp.setStatus(WebdavStatus.SC_NO_CONTENT);

    }


    // -------------------------------------------------------- Private Methods


    /**
     * Generate the namespace declarations.
     */
    private String generateNamespaceDeclarations() {
        return " xmlns=\"" + DEFAULT_NAMESPACE + "\"";
    }


    /**
     * Check to see if a resource is currently write locked. The method
     * will look at the "If" header to make sure the client
     * has give the appropriate lock tokens.
     *
     * @param req Servlet request
     * @return boolean true if the resource is locked (and no appropriate
     * lock token has been found for at least one of the non-shared locks which
     * are present on the resource).
     */
    private boolean isLocked(HttpServletRequest req, HttpServletResponse resp) {

        return isLocked(req, resp, null, null, null);

    }


    /**
     * Check to see if a resource is currently write locked. The method
     * will look at the "If" header to make sure the client
     * has give the appropriate lock tokens.
     *
     * @param req Servlet request
     * @return boolean true if the resource is locked (and no appropriate
     * lock token has been found for at least one of the non-shared locks which
     * are present on the resource).
     */
    private boolean isLocked(HttpServletRequest req, HttpServletResponse resp, String path, ContentObject co, String userId) {

        path = (path != null) ? path : processPath(req, resp);

        String ifHeader = req.getHeader("If");
        if (ifHeader == null)
            ifHeader = "";

        String lockTokenHeader = req.getHeader("Lock-Token");
        if (lockTokenHeader == null)
            lockTokenHeader = "";

        return isLocked(path, ifHeader + lockTokenHeader, co, userId);

    }


    /**
     * Check to see if a resource is currently write locked.
     *
     * @param path Path of the resource
     * @param ifHeader "If" HTTP header which was included in the request
     * @return boolean true if the resource is locked (and no appropriate
     * lock token has been found for at least one of the non-shared locks which
     * are present on the resource).
     */
    private boolean isLocked(String path, String ifHeader, ContentObject co, String userId) {

        // Checking resource locks

        LockInfo lock = (LockInfo) resourceLocks.get(path);
        Enumeration tokenList = null;
        if ((lock != null) && (lock.hasExpired())) {
            resourceLocks.remove(path);
        }
        else if (lock != null) {

            // At least one of the tokens of the locks must have been given

            tokenList = lock.tokens.elements();
            boolean tokenMatch = false;
            while (tokenList.hasMoreElements()) {
                String token = (String) tokenList.nextElement();
                if (ifHeader.indexOf(token) != -1)
                    tokenMatch = true;
            }
            if (!tokenMatch)
                return true;

        }

        // Checking inheritable collection locks

        Enumeration collectionLocksList = collectionLocks.elements();
        while (collectionLocksList.hasMoreElements()) {
            lock = (LockInfo) collectionLocksList.nextElement();
            if (lock.hasExpired()) {
                collectionLocks.removeElement(lock);
            }
            else if (path.startsWith(lock.path)) {

                tokenList = lock.tokens.elements();
                boolean tokenMatch = false;
                while (tokenList.hasMoreElements()) {
                    String token = (String) tokenList.nextElement();
                    if (ifHeader.indexOf(token) != -1)
                        tokenMatch = true;
                }
                if (!tokenMatch)
                    return true;

            }
        }

        // Checking checkout status

        if (co != null && userId != null && co.isCheckedOut() && !userId.equals(co.getCheckOutUserId()))
            return true;

        return false;

    }

    /**
     * Propfind helper method.
     *
     * @param generatedXML XML response to the Propfind request
     * @param path Path of the current resource
     * @param type Propfind type
     * @param propertiesVector If the propfind type is find properties by
     * name, then this Vector contains those properties
     */
    private void parseProperties(HttpServletRequest req, ContentObject co, XMLWriter generatedXML,
                                 String path, int type, Vector propertiesVector) {

        generatedXML.writeElement(null, "response", XMLWriter.OPENING);
        String status = new String("HTTP/1.1 " + WebdavStatus.SC_OK + " " + WebdavStatus.getStatusText(WebdavStatus.SC_OK));

        // Generating href element
        generatedXML.writeElement(null, "href", XMLWriter.OPENING);

        String href = req.getContextPath() + req.getServletPath();
        if (req.getPathInfo() != null) {
            href += req.getPathInfo();
        }

        if (!ContentManager.CONTENT_TREE_ROOT_ID.equals(co.getId()) && !href.endsWith("/" + co.getName())) {
            href += "/" + rewriteUrl(co.getName());
        }

        generatedXML.writeText(href);

        generatedXML.writeElement(null, "href", XMLWriter.CLOSING);

        String resourceName = co.getName();

        switch (type) {

            case FIND_ALL_PROP:

                generatedXML.writeElement(null, "propstat", XMLWriter.OPENING);
                generatedXML.writeElement(null, "prop", XMLWriter.OPENING);

                generatedXML.writeProperty(null, "creationdate", getISOCreationDate(co.getDate().getTime()));
                generatedXML.writeElement(null, "displayname", XMLWriter.OPENING);
                generatedXML.writeData(resourceName);
                generatedXML.writeElement(null, "displayname", XMLWriter.CLOSING);
                generatedXML.writeProperty(null, "getcontentlanguage",
                        Locale.getDefault().toString());
                if (co instanceof com.tms.cms.document.Document) {
                    com.tms.cms.document.Document doc = (com.tms.cms.document.Document) co;
                    generatedXML.writeProperty(null, "getlastmodified", new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z").format(doc.getDate()));
                    generatedXML.writeProperty(null, "getcontentlength", String.valueOf(doc.getFileSize()));
                    generatedXML.writeProperty(null, "getcontenttype", doc.getContentType());
                    generatedXML.writeProperty(null, "getetag", getETagValue(co, true));
                    generatedXML.writeElement(null, "resourcetype", XMLWriter.NO_CONTENT);
                }
                else {
                    generatedXML.writeElement(null, "resourcetype", XMLWriter.OPENING);
                    generatedXML.writeElement(null, "collection", XMLWriter.NO_CONTENT);
                    generatedXML.writeElement(null, "resourcetype", XMLWriter.CLOSING);
                }

                generatedXML.writeProperty(null, "source", "");

                String supportedLocks = "<lockentry>"
                        + "<lockscope><exclusive/></lockscope>"
                        + "<locktype><write/></locktype>"
                        + "</lockentry>" + "<lockentry>"
                        + "<lockscope><shared/></lockscope>"
                        + "<locktype><write/></locktype>"
                        + "</lockentry>";
                generatedXML.writeElement(null, "supportedlock", XMLWriter.OPENING);
                generatedXML.writeText(supportedLocks);
                generatedXML.writeElement(null, "supportedlock", XMLWriter.CLOSING);

                generateLockDiscovery(path, generatedXML);

                generatedXML.writeElement(null, "prop", XMLWriter.CLOSING);
                generatedXML.writeElement(null, "status", XMLWriter.OPENING);
                generatedXML.writeText(status);
                generatedXML.writeElement(null, "status", XMLWriter.CLOSING);
                generatedXML.writeElement(null, "propstat", XMLWriter.CLOSING);

                break;

            case FIND_PROPERTY_NAMES:

                generatedXML.writeElement(null, "propstat", XMLWriter.OPENING);
                generatedXML.writeElement(null, "prop", XMLWriter.OPENING);

                generatedXML.writeElement(null, "creationdate", XMLWriter.NO_CONTENT);
                generatedXML.writeElement(null, "displayname", XMLWriter.NO_CONTENT);
                if (!(co instanceof Section)) {
                    generatedXML.writeElement(null, "getcontentlanguage", XMLWriter.NO_CONTENT);
                    generatedXML.writeElement(null, "getcontentlength", XMLWriter.NO_CONTENT);
                    generatedXML.writeElement(null, "getcontenttype", XMLWriter.NO_CONTENT);
                    generatedXML.writeElement(null, "getetag", XMLWriter.NO_CONTENT);
                    generatedXML.writeElement(null, "getlastmodified", XMLWriter.NO_CONTENT);
                }
                generatedXML.writeElement(null, "resourcetype", XMLWriter.NO_CONTENT);
                generatedXML.writeElement(null, "source", XMLWriter.NO_CONTENT);
                generatedXML.writeElement(null, "lockdiscovery", XMLWriter.NO_CONTENT);

                generatedXML.writeElement(null, "prop", XMLWriter.CLOSING);
                generatedXML.writeElement(null, "status", XMLWriter.OPENING);
                generatedXML.writeText(status);
                generatedXML.writeElement(null, "status", XMLWriter.CLOSING);
                generatedXML.writeElement(null, "propstat", XMLWriter.CLOSING);

                break;

            case FIND_BY_PROPERTY:

                Vector propertiesNotFound = new Vector();

                // Parse the list of properties

                generatedXML.writeElement(null, "propstat", XMLWriter.OPENING);
                generatedXML.writeElement(null, "prop", XMLWriter.OPENING);

                Enumeration properties = propertiesVector.elements();

                while (properties.hasMoreElements()) {

                    String property = (String) properties.nextElement();

                    if (property.equals("creationdate")) {
                        generatedXML.writeProperty(null, "creationdate", getISOCreationDate(co.getDate().getTime()));
                    }
                    else if (property.equals("displayname")) {
                        generatedXML.writeElement(null, "displayname", XMLWriter.OPENING);
                        generatedXML.writeData(resourceName);
                        generatedXML.writeElement(null, "displayname", XMLWriter.CLOSING);
                    }
                    else if (property.equals("getcontentlanguage")) {
                        if (co instanceof Section) {
                            propertiesNotFound.addElement(property);
                        }
                        else {
                            generatedXML.writeProperty(null, "getcontentlanguage", Locale.getDefault().toString());
                        }
                    }
                    else if (property.equals("getcontentlength")) {
                        if (co instanceof Section) {
                            propertiesNotFound.addElement(property);
                        }
                        else if (co instanceof com.tms.cms.document.Document) {
                            generatedXML.writeProperty(null, "getcontentlength", (String.valueOf(((com.tms.cms.document.Document) co).getFileSize())));
                        }
                    }
                    else if (property.equals("getcontenttype")) {
                        if (co instanceof Section) {
                            propertiesNotFound.addElement(property);
                        }
                        else if (co instanceof com.tms.cms.document.Document) {
                            generatedXML.writeProperty(null, "getcontenttype", ((com.tms.cms.document.Document) co).getContentType());
                        }
                    }
                    else if (property.equals("getetag")) {
                        if (co instanceof Section) {
                            propertiesNotFound.addElement(property);
                        }
                        else if (co instanceof com.tms.cms.document.Document) {
                            generatedXML.writeProperty(null, "getetag", getETagValue(co, true));
                        }
                    }
                    else if (property.equals("getlastmodified")) {
                        if (co instanceof Section) {
                            propertiesNotFound.addElement(property);
                        }
                        else if (co instanceof com.tms.cms.document.Document) {
                            generatedXML.writeProperty(null, "getlastmodified", new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z").format(co.getDate()));
                        }
                    }
                    else if (property.equals("resourcetype")) {
                        if (co instanceof Section) {
                            generatedXML.writeElement(null, "resourcetype", XMLWriter.OPENING);
                            generatedXML.writeElement(null, "collection", XMLWriter.NO_CONTENT);
                            generatedXML.writeElement(null, "resourcetype", XMLWriter.CLOSING);
                        }
                        else {
                            generatedXML.writeElement(null, "resourcetype", XMLWriter.NO_CONTENT);
                        }
                    }
                    else if (property.equals("source")) {
                        generatedXML.writeProperty(null, "source", "");
                    }
                    else if (property.equals("supportedlock")) {
                        supportedLocks = "<lockentry>"
                                + "<lockscope><exclusive/></lockscope>"
                                + "<locktype><write/></locktype>"
                                + "</lockentry>" + "<lockentry>"
                                + "<lockscope><shared/></lockscope>"
                                + "<locktype><write/></locktype>"
                                + "</lockentry>";
                        generatedXML.writeElement(null, "supportedlock", XMLWriter.OPENING);
                        generatedXML.writeText(supportedLocks);
                        generatedXML.writeElement(null, "supportedlock", XMLWriter.CLOSING);
                    }
                    else if (property.equals("lockdiscovery")) {
                        if (!generateLockDiscovery(path, generatedXML))
                            propertiesNotFound.addElement(property);
                    }
                    else {
                        propertiesNotFound.addElement(property);
                    }

                }

                generatedXML.writeElement(null, "prop", XMLWriter.CLOSING);
                generatedXML.writeElement(null, "status", XMLWriter.OPENING);
                generatedXML.writeText(status);
                generatedXML.writeElement(null, "status", XMLWriter.CLOSING);
                generatedXML.writeElement(null, "propstat", XMLWriter.CLOSING);

                Enumeration propertiesNotFoundList = propertiesNotFound.elements();

                if (propertiesNotFoundList.hasMoreElements()) {

                    status = new String("HTTP/1.1 " + WebdavStatus.SC_NOT_FOUND
                            + " " + WebdavStatus.getStatusText
                            (WebdavStatus.SC_NOT_FOUND));

                    generatedXML.writeElement(null, "propstat", XMLWriter.OPENING);
                    generatedXML.writeElement(null, "prop", XMLWriter.OPENING);

                    while (propertiesNotFoundList.hasMoreElements()) {
                        generatedXML.writeElement(null, (String) propertiesNotFoundList.nextElement(), XMLWriter.NO_CONTENT);
                    }

                    generatedXML.writeElement(null, "prop", XMLWriter.CLOSING);
                    generatedXML.writeElement(null, "status", XMLWriter.OPENING);
                    generatedXML.writeText(status);
                    generatedXML.writeElement(null, "status", XMLWriter.CLOSING);
                    generatedXML.writeElement(null, "propstat", XMLWriter.CLOSING);

                }

                break;

        }

        generatedXML.writeElement(null, "response", XMLWriter.CLOSING);

    }


    /**
     * Propfind helper method. Dispays the properties of a lock-null resource.
     *
     * @param generatedXML XML response to the Propfind request
     * @param path Path of the current resource
     * @param type Propfind type
     * @param propertiesVector If the propfind type is find properties by
     * name, then this Vector contains those properties
     */
    private void parseLockNullProperties(HttpServletRequest req, HttpServletResponse resp,
                                         XMLWriter generatedXML,
                                         String path, int type,
                                         Vector propertiesVector) {

        // Exclude any resource in the /WEB-INF and /META-INF subdirectories
        // (the "toUpperCase()" avoids problems on Windows systems)
        if (path.toUpperCase().startsWith("/WEB-INF") ||
                path.toUpperCase().startsWith("/META-INF"))
            return;

        // Retrieving the lock associated with the lock-null resource
        LockInfo lock = (LockInfo) resourceLocks.get(path);

        if (lock == null)
            return;

        generatedXML.writeElement(null, "response", XMLWriter.OPENING);
        String status = new String("HTTP/1.1 " + WebdavStatus.SC_OK + " "
                + WebdavStatus.getStatusText
                (WebdavStatus.SC_OK));

        // Generating href element
        generatedXML.writeElement(null, "href", XMLWriter.OPENING);

        String absoluteUri = req.getRequestURI();
        String relativePath = processPath(req, resp);
        String toAppend = path.substring(relativePath.length());
        if (!toAppend.startsWith("/"))
            toAppend = "/" + toAppend;

        generatedXML.writeText(absoluteUri + toAppend);

        generatedXML.writeElement(null, "href", XMLWriter.CLOSING);

        String resourceName = path;
        int lastSlash = path.lastIndexOf('/');
        if (lastSlash != -1)
            resourceName = resourceName.substring(lastSlash + 1);

        switch (type) {

            case FIND_ALL_PROP:

                generatedXML.writeElement(null, "propstat", XMLWriter.OPENING);
                generatedXML.writeElement(null, "prop", XMLWriter.OPENING);

                generatedXML.writeProperty
                        (null, "creationdate",
                                getISOCreationDate(lock.creationDate.getTime()));
                generatedXML.writeElement
                        (null, "displayname", XMLWriter.OPENING);
                generatedXML.writeData(resourceName);
                generatedXML.writeElement
                        (null, "displayname", XMLWriter.CLOSING);
                generatedXML.writeProperty(null, "getcontentlanguage",
                        Locale.getDefault().toString());
                generatedXML.writeProperty(null, "getlastmodified",
                        lock.creationDate + "");
                generatedXML.writeProperty
                        (null, "getcontentlength", String.valueOf(0));
                generatedXML.writeProperty(null, "getcontenttype", "");
                generatedXML.writeProperty(null, "getetag", "");
                generatedXML.writeElement(null, "resourcetype",
                        XMLWriter.OPENING);
                generatedXML.writeElement(null, "lock-null", XMLWriter.NO_CONTENT);
                generatedXML.writeElement(null, "resourcetype",
                        XMLWriter.CLOSING);

                generatedXML.writeProperty(null, "source", "");

                String supportedLocks = "<lockentry>"
                        + "<lockscope><exclusive/></lockscope>"
                        + "<locktype><write/></locktype>"
                        + "</lockentry>" + "<lockentry>"
                        + "<lockscope><shared/></lockscope>"
                        + "<locktype><write/></locktype>"
                        + "</lockentry>";
                generatedXML.writeElement(null, "supportedlock",
                        XMLWriter.OPENING);
                generatedXML.writeText(supportedLocks);
                generatedXML.writeElement(null, "supportedlock",
                        XMLWriter.CLOSING);

                generateLockDiscovery(path, generatedXML);

                generatedXML.writeElement(null, "prop", XMLWriter.CLOSING);
                generatedXML.writeElement(null, "status", XMLWriter.OPENING);
                generatedXML.writeText(status);
                generatedXML.writeElement(null, "status", XMLWriter.CLOSING);
                generatedXML.writeElement(null, "propstat", XMLWriter.CLOSING);

                break;

            case FIND_PROPERTY_NAMES:

                generatedXML.writeElement(null, "propstat", XMLWriter.OPENING);
                generatedXML.writeElement(null, "prop", XMLWriter.OPENING);

                generatedXML.writeElement(null, "creationdate",
                        XMLWriter.NO_CONTENT);
                generatedXML.writeElement(null, "displayname",
                        XMLWriter.NO_CONTENT);
                generatedXML.writeElement(null, "getcontentlanguage",
                        XMLWriter.NO_CONTENT);
                generatedXML.writeElement(null, "getcontentlength",
                        XMLWriter.NO_CONTENT);
                generatedXML.writeElement(null, "getcontenttype",
                        XMLWriter.NO_CONTENT);
                generatedXML.writeElement(null, "getetag",
                        XMLWriter.NO_CONTENT);
                generatedXML.writeElement(null, "getlastmodified",
                        XMLWriter.NO_CONTENT);
                generatedXML.writeElement(null, "resourcetype",
                        XMLWriter.NO_CONTENT);
                generatedXML.writeElement(null, "source",
                        XMLWriter.NO_CONTENT);
                generatedXML.writeElement(null, "lockdiscovery",
                        XMLWriter.NO_CONTENT);

                generatedXML.writeElement(null, "prop", XMLWriter.CLOSING);
                generatedXML.writeElement(null, "status", XMLWriter.OPENING);
                generatedXML.writeText(status);
                generatedXML.writeElement(null, "status", XMLWriter.CLOSING);
                generatedXML.writeElement(null, "propstat", XMLWriter.CLOSING);

                break;

            case FIND_BY_PROPERTY:

                Vector propertiesNotFound = new Vector();

                // Parse the list of properties

                generatedXML.writeElement(null, "propstat", XMLWriter.OPENING);
                generatedXML.writeElement(null, "prop", XMLWriter.OPENING);

                Enumeration properties = propertiesVector.elements();

                while (properties.hasMoreElements()) {

                    String property = (String) properties.nextElement();

                    if (property.equals("creationdate")) {
                        generatedXML.writeProperty
                                (null, "creationdate",
                                        getISOCreationDate(lock.creationDate.getTime()));
                    }
                    else if (property.equals("displayname")) {
                        generatedXML.writeElement
                                (null, "displayname", XMLWriter.OPENING);
                        generatedXML.writeData(resourceName);
                        generatedXML.writeElement
                                (null, "displayname", XMLWriter.CLOSING);
                    }
                    else if (property.equals("getcontentlanguage")) {
                        generatedXML.writeProperty
                                (null, "getcontentlanguage",
                                        Locale.getDefault().toString());
                    }
                    else if (property.equals("getcontentlength")) {
                        generatedXML.writeProperty
                                (null, "getcontentlength", (String.valueOf(0)));
                    }
                    else if (property.equals("getcontenttype")) {
                        generatedXML.writeProperty
                                (null, "getcontenttype", "");
                    }
                    else if (property.equals("getetag")) {
                        generatedXML.writeProperty(null, "getetag", "");
                    }
                    else if (property.equals("getlastmodified")) {
                        generatedXML.writeProperty
                                (null, "getlastmodified",
                                        lock.creationDate + "");
                    }
                    else if (property.equals("resourcetype")) {
                        generatedXML.writeElement(null, "resourcetype",
                                XMLWriter.OPENING);
                        generatedXML.writeElement(null, "lock-null",
                                XMLWriter.NO_CONTENT);
                        generatedXML.writeElement(null, "resourcetype",
                                XMLWriter.CLOSING);
                    }
                    else if (property.equals("source")) {
                        generatedXML.writeProperty(null, "source", "");
                    }
                    else if (property.equals("supportedlock")) {
                        supportedLocks = "<lockentry>"
                                + "<lockscope><exclusive/></lockscope>"
                                + "<locktype><write/></locktype>"
                                + "</lockentry>" + "<lockentry>"
                                + "<lockscope><shared/></lockscope>"
                                + "<locktype><write/></locktype>"
                                + "</lockentry>";
                        generatedXML.writeElement(null, "supportedlock",
                                XMLWriter.OPENING);
                        generatedXML.writeText(supportedLocks);
                        generatedXML.writeElement(null, "supportedlock",
                                XMLWriter.CLOSING);
                    }
                    else if (property.equals("lockdiscovery")) {
                        if (!generateLockDiscovery(path, generatedXML))
                            propertiesNotFound.addElement(property);
                    }
                    else {
                        propertiesNotFound.addElement(property);
                    }

                }

                generatedXML.writeElement(null, "prop", XMLWriter.CLOSING);
                generatedXML.writeElement(null, "status", XMLWriter.OPENING);
                generatedXML.writeText(status);
                generatedXML.writeElement(null, "status", XMLWriter.CLOSING);
                generatedXML.writeElement(null, "propstat", XMLWriter.CLOSING);

                Enumeration propertiesNotFoundList = propertiesNotFound.elements();

                if (propertiesNotFoundList.hasMoreElements()) {

                    status = new String("HTTP/1.1 " + WebdavStatus.SC_NOT_FOUND
                            + " " + WebdavStatus.getStatusText
                            (WebdavStatus.SC_NOT_FOUND));

                    generatedXML.writeElement(null, "propstat", XMLWriter.OPENING);
                    generatedXML.writeElement(null, "prop", XMLWriter.OPENING);

                    while (propertiesNotFoundList.hasMoreElements()) {
                        generatedXML.writeElement
                                (null, (String) propertiesNotFoundList.nextElement(),
                                        XMLWriter.NO_CONTENT);
                    }

                    generatedXML.writeElement(null, "prop", XMLWriter.CLOSING);
                    generatedXML.writeElement(null, "status", XMLWriter.OPENING);
                    generatedXML.writeText(status);
                    generatedXML.writeElement(null, "status", XMLWriter.CLOSING);
                    generatedXML.writeElement(null, "propstat", XMLWriter.CLOSING);

                }

                break;

        }

        generatedXML.writeElement(null, "response", XMLWriter.CLOSING);

    }


    /**
     * Print the lock discovery information associated with a path.
     *
     * @param path Path
     * @param generatedXML XML data to which the locks info will be appended
     * @return true if at least one lock was displayed
     */
    private boolean generateLockDiscovery
            (String path, XMLWriter generatedXML) {

        LockInfo resourceLock = (LockInfo) resourceLocks.get(path);
        Enumeration collectionLocksList = collectionLocks.elements();

        boolean wroteStart = false;

        if (resourceLock != null) {
            wroteStart = true;
            generatedXML.writeElement(null, "lockdiscovery",
                    XMLWriter.OPENING);
            resourceLock.toXML(generatedXML);
        }

        while (collectionLocksList.hasMoreElements()) {
            LockInfo currentLock =
                    (LockInfo) collectionLocksList.nextElement();
            if (path.startsWith(currentLock.path)) {
                if (!wroteStart) {
                    wroteStart = true;
                    generatedXML.writeElement(null, "lockdiscovery",
                            XMLWriter.OPENING);
                }
                currentLock.toXML(generatedXML);
            }
        }

        if (wroteStart) {
            generatedXML.writeElement(null, "lockdiscovery",
                    XMLWriter.CLOSING);
        }
        else {
            return false;
        }

        return true;

    }


    /**
     * Get creation date in ISO format.
     */
    private String getISOCreationDate(long creationDate) {
        StringBuffer creationDateValue = new StringBuffer(creationDateFormat.format(new Date(creationDate)));
        /*
        int offset = Calendar.getInstance().getTimeZone().getRawOffset()
            / 3600000; // FIXME ?
        if (offset < 0) {
            creationDateValue.append("-");
            offset = -offset;
        } else if (offset > 0) {
            creationDateValue.append("+");
        }
        if (offset != 0) {
            if (offset < 10)
                creationDateValue.append("0");
            creationDateValue.append(offset + ":00");
        } else {
            creationDateValue.append("Z");
        }
         */
        return creationDateValue.toString();
    }

    /**
     * URL rewriter.
     *
     * @param path Path which has to be rewiten
     */
    protected String rewriteUrl(String path) {

        /**
         * Note: This code portion is very similar to URLEncoder.encode.
         * Unfortunately, there is no way to specify to the URLEncoder which
         * characters should be encoded. Here, ' ' should be encoded as "%20"
         * and '/' shouldn't be encoded.
         */

        int maxBytesPerChar = 10;
        StringBuffer rewrittenPath = new StringBuffer(path.length());
        ByteArrayOutputStream buf = new ByteArrayOutputStream(maxBytesPerChar);
        OutputStreamWriter writer = null;
        try {
            writer = new OutputStreamWriter(buf, "UTF8");
        }
        catch (Exception e) {
            log.error(e.toString(), e);
            writer = new OutputStreamWriter(buf);
        }

        for (int i = 0; i < path.length(); i++) {
            int c = (int) path.charAt(i);
            if (safeCharacters.get(c)) {
                rewrittenPath.append((char) c);
            }
            else {
                // convert to external encoding before hex conversion
                try {
                    writer.write(c);
                    writer.flush();
                }
                catch (IOException e) {
                    buf.reset();
                    continue;
                }
                byte[] ba = buf.toByteArray();
                for (int j = 0; j < ba.length; j++) {
                    // Converting each byte in the buffer
                    byte toEncode = ba[j];
                    rewrittenPath.append('%');
                    int low = (int) (toEncode & 0x0f);
                    int high = (int) ((toEncode & 0xf0) >> 4);
                    rewrittenPath.append(hexadecimal[high]);
                    rewrittenPath.append(hexadecimal[low]);
                }
                buf.reset();
            }
        }

        return rewrittenPath.toString();

    }

    /**
     * Return a context-relative path, beginning with a "/", that represents
     * the canonical version of the specified path after ".." and "." elements
     * are resolved out.  If the specified path attempts to go outside the
     * boundaries of the current context (i.e. too many ".." path elements
     * are present), return <code>null</code> instead.
     *
     * @param path Path to be normalized
     */
    protected String normalize(String path) {

        if (path == null)
            return null;

        // Create a place for the normalized path
        String normalized = path;

        /*
         * Commented out -- already URL-decoded in StandardContextMapper
         * Decoding twice leaves the container vulnerable to %25 --> '%'
         * attacks.
         *
         * if (normalized.indexOf('%') >= 0)
         *     normalized = RequestUtil.URLDecode(normalized, "UTF8");
         */

        if (normalized == null)
            return (null);

        if (normalized.equals("/."))
            return "/";

        // Normalize the slashes and add leading slash if necessary
        if (normalized.indexOf('\\') >= 0)
            normalized = normalized.replace('\\', '/');
        if (!normalized.startsWith("/"))
            normalized = "/" + normalized;

        // Resolve occurrences of "//" in the normalized path
        while (true) {
            int index = normalized.indexOf("//");
            if (index < 0)
                break;
            normalized = normalized.substring(0, index) +
                    normalized.substring(index + 1);
        }

        // Resolve occurrences of "/./" in the normalized path
        while (true) {
            int index = normalized.indexOf("/./");
            if (index < 0)
                break;
            normalized = normalized.substring(0, index) +
                    normalized.substring(index + 2);
        }

        // Resolve occurrences of "/../" in the normalized path
        while (true) {
            int index = normalized.indexOf("/../");
            if (index < 0)
                break;
            if (index == 0)
                return (null);  // Trying to go outside our context
            int index2 = normalized.lastIndexOf('/', index - 1);
            normalized = normalized.substring(0, index2) +
                    normalized.substring(index + 3);
        }

        // Return the normalized path that we have completed
        return (normalized);

    }



    // --------------------------------------------------  LockInfo Inner Class


    /**
     * Holds a lock information.
     */
    private class LockInfo {


        // -------------------------------------------------------- Constructor


        /**
         * Constructor.
         *
         */
        public LockInfo() {

        }


        // ------------------------------------------------- Instance Variables


        String path = "/";
        String type = "write";
        String scope = "exclusive";
        int depth = 0;
        String owner = "";
        Vector tokens = new Vector();
        long expiresAt = 0;
        Date creationDate = new Date();


        // ----------------------------------------------------- Public Methods


        /**
         * Get a String representation of this lock token.
         */
        public String toString() {

            String result = "Type:" + type + "\n";
            result += "Scope:" + scope + "\n";
            result += "Depth:" + depth + "\n";
            result += "Owner:" + owner + "\n";
            result += "Expiration:" +
                    new Date(expiresAt) + "\n";
            Enumeration tokensList = tokens.elements();
            while (tokensList.hasMoreElements()) {
                result += "Token:" + tokensList.nextElement() + "\n";
            }
            return result;

        }


        /**
         * Return true if the lock has expired.
         */
        public boolean hasExpired() {
            return (System.currentTimeMillis() > expiresAt);
        }


        /**
         * Return true if the lock is exclusive.
         */
        public boolean isExclusive() {

            return (scope.equals("exclusive"));

        }


        /**
         * Get an XML representation of this lock token. This method will
         * append an XML fragment to the given XML writer.
         */
        public void toXML(XMLWriter generatedXML) {
            toXML(generatedXML, false);
        }


        /**
         * Get an XML representation of this lock token. This method will
         * append an XML fragment to the given XML writer.
         */
        public void toXML(XMLWriter generatedXML, boolean showToken) {

            generatedXML.writeElement(null, "activelock", XMLWriter.OPENING);

            generatedXML.writeElement(null, "locktype", XMLWriter.OPENING);
            generatedXML.writeElement(null, type, XMLWriter.NO_CONTENT);
            generatedXML.writeElement(null, "locktype", XMLWriter.CLOSING);

            generatedXML.writeElement(null, "lockscope", XMLWriter.OPENING);
            generatedXML.writeElement(null, scope, XMLWriter.NO_CONTENT);
            generatedXML.writeElement(null, "lockscope", XMLWriter.CLOSING);

            generatedXML.writeElement(null, "depth", XMLWriter.OPENING);
            if (depth == INFINITY) {
                generatedXML.writeText("Infinity");
            }
            else {
                generatedXML.writeText("0");
            }
            generatedXML.writeElement(null, "depth", XMLWriter.CLOSING);

            generatedXML.writeElement(null, "owner", XMLWriter.OPENING);
            generatedXML.writeText(owner);
            generatedXML.writeElement(null, "owner", XMLWriter.CLOSING);

            generatedXML.writeElement(null, "timeout", XMLWriter.OPENING);
            long timeout = (expiresAt - System.currentTimeMillis()) / 1000;
            generatedXML.writeText("Second-" + timeout);
            generatedXML.writeElement(null, "timeout", XMLWriter.CLOSING);

            generatedXML.writeElement(null, "locktoken", XMLWriter.OPENING);
            if (showToken) {
                Enumeration tokensList = tokens.elements();
                while (tokensList.hasMoreElements()) {
                    generatedXML.writeElement(null, "href", XMLWriter.OPENING);
                    generatedXML.writeText("opaquelocktoken:"
                            + tokensList.nextElement());
                    generatedXML.writeElement(null, "href", XMLWriter.CLOSING);
                }
            }
            else {
                generatedXML.writeElement(null, "href", XMLWriter.OPENING);
                generatedXML.writeText("opaquelocktoken:dummytoken");
                generatedXML.writeElement(null, "href", XMLWriter.CLOSING);
            }
            generatedXML.writeElement(null, "locktoken", XMLWriter.CLOSING);

            generatedXML.writeElement(null, "activelock", XMLWriter.CLOSING);

        }


    }

}

;


// --------------------------------------------------------  WebdavStatus Class


/**
 * Wraps the HttpServletResponse class to abstract the
 * specific protocol used.  To support other protocols
 * we would only need to modify this class and the
 * WebDavRetCode classes.
 *
 * @author              Marc Eaddy
 * @version             1.0, 16 Nov 1997
 */
class WebdavStatus {


    // ----------------------------------------------------- Instance Variables


    /**
     * This Hashtable contains the mapping of HTTP and WebDAV
     * status codes to descriptive text.  This is a static
     * variable.
     */
    private static Hashtable mapStatusCodes = new Hashtable();


    // ------------------------------------------------------ HTTP Status Codes


    /**
     * Status code (200) indicating the request succeeded normally.
     */
    public static final int SC_OK = HttpServletResponse.SC_OK;


    /**
     * Status code (201) indicating the request succeeded and created
     * a new resource on the server.
     */
    public static final int SC_CREATED = HttpServletResponse.SC_CREATED;


    /**
     * Status code (202) indicating that a request was accepted for
     * processing, but was not completed.
     */
    public static final int SC_ACCEPTED = HttpServletResponse.SC_ACCEPTED;


    /**
     * Status code (204) indicating that the request succeeded but that
     * there was no new information to return.
     */
    public static final int SC_NO_CONTENT = HttpServletResponse.SC_NO_CONTENT;


    /**
     * Status code (301) indicating that the resource has permanently
     * moved to a new location, and that future references should use a
     * new URI with their requests.
     */
    public static final int SC_MOVED_PERMANENTLY =
            HttpServletResponse.SC_MOVED_PERMANENTLY;


    /**
     * Status code (302) indicating that the resource has temporarily
     * moved to another location, but that future references should
     * still use the original URI to access the resource.
     */
    public static final int SC_MOVED_TEMPORARILY =
            HttpServletResponse.SC_MOVED_TEMPORARILY;


    /**
     * Status code (304) indicating that a conditional GET operation
     * found that the resource was available and not modified.
     */
    public static final int SC_NOT_MODIFIED =
            HttpServletResponse.SC_NOT_MODIFIED;


    /**
     * Status code (400) indicating the request sent by the client was
     * syntactically incorrect.
     */
    public static final int SC_BAD_REQUEST =
            HttpServletResponse.SC_BAD_REQUEST;


    /**
     * Status code (401) indicating that the request requires HTTP
     * authentication.
     */
    public static final int SC_UNAUTHORIZED =
            HttpServletResponse.SC_UNAUTHORIZED;


    /**
     * Status code (403) indicating the server understood the request
     * but refused to fulfill it.
     */
    public static final int SC_FORBIDDEN = HttpServletResponse.SC_FORBIDDEN;


    /**
     * Status code (404) indicating that the requested resource is not
     * available.
     */
    public static final int SC_NOT_FOUND = HttpServletResponse.SC_NOT_FOUND;


    /**
     * Status code (500) indicating an error inside the HTTP service
     * which prevented it from fulfilling the request.
     */
    public static final int SC_INTERNAL_SERVER_ERROR =
            HttpServletResponse.SC_INTERNAL_SERVER_ERROR;


    /**
     * Status code (501) indicating the HTTP service does not support
     * the functionality needed to fulfill the request.
     */
    public static final int SC_NOT_IMPLEMENTED =
            HttpServletResponse.SC_NOT_IMPLEMENTED;


    /**
     * Status code (502) indicating that the HTTP server received an
     * invalid response from a server it consulted when acting as a
     * proxy or gateway.
     */
    public static final int SC_BAD_GATEWAY =
            HttpServletResponse.SC_BAD_GATEWAY;


    /**
     * Status code (503) indicating that the HTTP service is
     * temporarily overloaded, and unable to handle the request.
     */
    public static final int SC_SERVICE_UNAVAILABLE =
            HttpServletResponse.SC_SERVICE_UNAVAILABLE;


    /**
     * Status code (100) indicating the client may continue with
     * its request.  This interim response is used to inform the
     * client that the initial part of the request has been
     * received and has not yet been rejected by the server.
     */
    public static final int SC_CONTINUE = 100;


    /**
     * Status code (405) indicating the method specified is not
     * allowed for the resource.
     */
    public static final int SC_METHOD_NOT_ALLOWED = 405;


    /**
     * Status code (409) indicating that the request could not be
     * completed due to a conflict with the current state of the
     * resource.
     */
    public static final int SC_CONFLICT = 409;


    /**
     * Status code (412) indicating the precondition given in one
     * or more of the request-header fields evaluated to false
     * when it was tested on the server.
     */
    public static final int SC_PRECONDITION_FAILED = 412;


    /**
     * Status code (413) indicating the server is refusing to
     * process a request because the request entity is larger
     * than the server is willing or able to process.
     */
    public static final int SC_REQUEST_TOO_LONG = 413;


    /**
     * Status code (415) indicating the server is refusing to service
     * the request because the entity of the request is in a format
     * not supported by the requested resource for the requested
     * method.
     */
    public static final int SC_UNSUPPORTED_MEDIA_TYPE = 415;


    // -------------------------------------------- Extended WebDav status code


    /**
     * Status code (207) indicating that the response requires
     * providing status for multiple independent operations.
     */
    public static final int SC_MULTI_STATUS = 207;
    // This one colides with HTTP 1.1
    // "207 Parital Update OK"


    /**
     * Status code (418) indicating the entity body submitted with
     * the PATCH method was not understood by the resource.
     */
    public static final int SC_UNPROCESSABLE_ENTITY = 418;
    // This one colides with HTTP 1.1
    // "418 Reauthentication Required"


    /**
     * Status code (419) indicating that the resource does not have
     * sufficient space to record the state of the resource after the
     * execution of this method.
     */
    public static final int SC_INSUFFICIENT_SPACE_ON_RESOURCE = 419;
    // This one colides with HTTP 1.1
    // "419 Proxy Reauthentication Required"


    /**
     * Status code (420) indicating the method was not executed on
     * a particular resource within its scope because some part of
     * the method's execution failed causing the entire method to be
     * aborted.
     */
    public static final int SC_METHOD_FAILURE = 420;


    /**
     * Status code (423) indicating the destination resource of a
     * method is locked, and either the request did not contain a
     * valid Lock-Info header, or the Lock-Info header identifies
     * a lock held by another principal.
     */
    public static final int SC_LOCKED = 423;


    // ------------------------------------------------------------ Initializer


    static {
        // HTTP 1.0 tatus Code
        addStatusCodeMap(SC_OK, "OK");
        addStatusCodeMap(SC_CREATED, "Created");
        addStatusCodeMap(SC_ACCEPTED, "Accepted");
        addStatusCodeMap(SC_NO_CONTENT, "No Content");
        addStatusCodeMap(SC_MOVED_PERMANENTLY, "Moved Permanently");
        addStatusCodeMap(SC_MOVED_TEMPORARILY, "Moved Temporarily");
        addStatusCodeMap(SC_NOT_MODIFIED, "Not Modified");
        addStatusCodeMap(SC_BAD_REQUEST, "Bad Request");
        addStatusCodeMap(SC_UNAUTHORIZED, "Unauthorized");
        addStatusCodeMap(SC_FORBIDDEN, "Forbidden");
        addStatusCodeMap(SC_NOT_FOUND, "Not Found");
        addStatusCodeMap(SC_INTERNAL_SERVER_ERROR, "Internal Server Error");
        addStatusCodeMap(SC_NOT_IMPLEMENTED, "Not Implemented");
        addStatusCodeMap(SC_BAD_GATEWAY, "Bad Gateway");
        addStatusCodeMap(SC_SERVICE_UNAVAILABLE, "Service Unavailable");
        addStatusCodeMap(SC_CONTINUE, "Continue");
        addStatusCodeMap(SC_METHOD_NOT_ALLOWED, "Method Not Allowed");
        addStatusCodeMap(SC_CONFLICT, "Conflict");
        addStatusCodeMap(SC_PRECONDITION_FAILED, "Precondition Failed");
        addStatusCodeMap(SC_REQUEST_TOO_LONG, "Request Too Long");
        addStatusCodeMap(SC_UNSUPPORTED_MEDIA_TYPE, "Unsupported Media Type");
        // WebDav Status Codes
        addStatusCodeMap(SC_MULTI_STATUS, "Multi-Status");
        addStatusCodeMap(SC_UNPROCESSABLE_ENTITY, "Unprocessable Entity");
        addStatusCodeMap(SC_INSUFFICIENT_SPACE_ON_RESOURCE,
                "Insufficient Space On Resource");
        addStatusCodeMap(SC_METHOD_FAILURE, "Method Failure");
        addStatusCodeMap(SC_LOCKED, "Locked");
    }


    // --------------------------------------------------------- Public Methods


    /**
     * Returns the HTTP status text for the HTTP or WebDav status code
     * specified by looking it up in the static mapping.  This is a
     * static function.
     *
     * @param   nHttpStatusCode [IN] HTTP or WebDAV status code
     * @return  A string with a short descriptive phrase for the
     *                  HTTP status code (e.g., "OK").
     */
    public static String getStatusText(int nHttpStatusCode) {
        Integer intKey = new Integer(nHttpStatusCode);

        if (!mapStatusCodes.containsKey(intKey)) {
            return "";
        }
        else {
            return (String) mapStatusCodes.get(intKey);
        }
    }


    // -------------------------------------------------------- Private Methods


    /**
     * Adds a new status code -> status text mapping.  This is a static
     * method because the mapping is a static variable.
     *
     * @param   nKey    [IN] HTTP or WebDAV status code
     * @param   strVal  [IN] HTTP status text
     */
    private static void addStatusCodeMap(int nKey, String strVal) {
        mapStatusCodes.put(new Integer(nKey), strVal);
    }


}

;


