package com.tms.cms.webdav;

import kacang.Application;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.util.Log;
import kacang.util.Encryption;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.Hashtable;
import java.util.StringTokenizer;

/**
 * An implementation of HTTP DIGEST
 * Authentication (see RFC 2069).
 */
public class DigestAuthenticator {
    

    /**
     * Indicates that no once tokens are used only once.
     */
    private static final int TIMEOUT_INFINITE = Integer.MAX_VALUE;


    /**
     * The MD5 helper object for this class.
     */
    private static final MD5Encoder md5Encoder = new MD5Encoder();


    // ----------------------------------------------------------- Constructors

    public DigestAuthenticator() {
        super();
        try {
            if (md5Helper == null)
                md5Helper = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            Log.getLog(getClass()).error(e);
            throw new IllegalStateException();
        }
    }


    // ----------------------------------------------------- Instance Variables


    /**
     * MD5 message digest provider.
     */
    private static MessageDigest md5Helper;


    /**
     * No once hashtable.
     */
    private Hashtable nOnceTokens = new Hashtable();


    /**
     * No once expiration (in millisecond). A shorter amount would mean a
     * better security level (since the token is generated more often), but at
     * the expense of a bigger server overhead.
     */
    private long nOnceTimeout = TIMEOUT_INFINITE;


    /**
     * Private key.
     */
    private String key = "cmf.cms";


    /**
     * Authenticate the user making this request.
     * Return <code>true</code> if any specified
     * constraint has been satisfied, or <code>false</code> if we have
     * created a response challenge already.
     *
     * @param request Request we are processing
     * @param response Response we are creating
     *
     * @exception IOException if an input/output error occurs
     */
    public boolean authenticate(HttpServletRequest request,
                                HttpServletResponse response)
        throws IOException {

        // Have we already authenticated someone?
        String remoteUser = request.getRemoteUser();
        if (remoteUser != null) {
            return true;
        }

        // Validate any credentials already included with this request
        String authorization = request.getHeader("authorization");
        if (authorization != null) {
            User user  = findPrincipal(request, authorization);
            if (user != null) {
                String username = parseUsername(authorization);
                register(request, response, user,
                         "DIGEST",
                         username, null);
                return (true);
            }
        }

        // Send an "unauthorized" response and an appropriate challenge
        sendChallenge(request, response);

        return (false);

    }


    /**
     * Sends an "unauthorized" response and an appropriate challenge.
     */
    public void sendChallenge(HttpServletRequest request, HttpServletResponse response)  {
        // generate a nOnce token (that is a token which is supposed
        // to be unique).
        String nOnce = generateNOnce(request);

        setAuthenticateHeader(request, response, null, nOnce);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }

    // -------------------------------------------------------- Private Methods

    
    /**
     * Parse the specified authorization credentials, and return the
     * associated Principal that these credentials authenticate (if any)
     * from the specified Realm.  If there is no such Principal, return
     * <code>null</code>.
     *
     * @param request HTTP servlet request
     * @param authorization Authorization credentials from this request
     */
    protected User findPrincipal(HttpServletRequest request,
                                           String authorization) {

        Log log = Log.getLog(getClass());

        //System.out.println("Authorization token : " + authorization);
        // Validate the authorization credentials format
        if (authorization == null)
            return (null);
        if (!authorization.startsWith("Digest "))
            return (null);
        authorization = authorization.substring(7).trim();


        StringTokenizer commaTokenizer =
            new StringTokenizer(authorization, ",");

        String username = null;
        String realmName = null;
        String nOnce = null;
        String nc = null;
        String cnonce = null;
        String qop = null;
        String uri = null;
        String response = null;
        String method = request.getMethod();

        while (commaTokenizer.hasMoreTokens()) {
            String currentToken = commaTokenizer.nextToken();
            int equalSign = currentToken.indexOf('=');
            if (equalSign < 0)
                return null;
            String currentTokenName =
                currentToken.substring(0, equalSign).trim();
            String currentTokenValue =
                currentToken.substring(equalSign + 1).trim();
            if ("username".equals(currentTokenName))
                username = removeQuotes(currentTokenValue);
            if ("realm".equals(currentTokenName))
                realmName = removeQuotes(currentTokenValue);
            if ("nonce".equals(currentTokenName))
                nOnce = removeQuotes(currentTokenValue);
            if ("nc".equals(currentTokenName))
                nc = currentTokenValue;
            if ("cnonce".equals(currentTokenName))
                cnonce = removeQuotes(currentTokenValue);
            if ("qop".equals(currentTokenName))
                qop = removeQuotes(currentTokenValue);
            if ("uri".equals(currentTokenName))
                uri = removeQuotes(currentTokenValue);
            if ("response".equals(currentTokenName))
                response = removeQuotes(currentTokenValue);
        }

        if ( (username == null) || (realmName == null) || (nOnce == null)
             || (uri == null) || (response == null) )
            return null;

        // Second MD5 digest used to calculate the digest :
        // MD5(Method + ":" + uri)
        String a2 = method + ":" + uri;

        String md5a2 = md5Encoder.encode(md5Helper.digest(a2.getBytes()));

        log.debug("Digest : " + response);
        log.debug("************ Digest info");
        log.debug("Username:" + username);
        log.debug("ClientDigest:" + response);
        log.debug("nOnce:" + nOnce);
        log.debug("nc:" + nc);
        log.debug("cnonce:" + cnonce);
        log.debug("qop:" + qop);
        log.debug("realmName:" + realmName);
        log.debug("md5a2:" + md5a2);

        User user = null;
        try {
            // parse username
            StringTokenizer st = new StringTokenizer(username, "\\/");
            String parsedUsername = username;
            while (st.hasMoreTokens()) {
                parsedUsername = st.nextToken();
            }

            // get user
            Application application = Application.getInstance();
            SecurityService security = (SecurityService)application.getService(SecurityService.class);
            Collection users = security.getUsersByUsername(parsedUsername);
            if (users.size() > 0)
                user = (User)users.iterator().next();
            else
                return null;
                
            // First MD5 Digest
            String digestValue = username + ":" + realmName + ":" + Encryption.decrypt((String)user.getProperty("weakpass"), user.getId()); // decrypt weakpass?
            byte[] digest =
                md5Helper.digest(digestValue.getBytes());
            String md5a1 = md5Encoder.encode(digest);
            if (md5a1 == null)
                return null;
                
            // Server Digest
            String serverDigestValue = md5a1 + ":" + nOnce + ":" + nc + ":"
                + cnonce + ":" + qop + ":" + md5a2;
            String serverDigest =
                md5Encoder.encode(md5Helper.digest(serverDigestValue.getBytes()));
    
            log.debug("Server digest : " + serverDigest);
    
            // Compare Digests
            if (serverDigest.equals(response))
                return user;
            else
                return null;
        }
        catch(Exception e) {
            log.error("Error finding principal " + username, e);
            return null;
        }
    }

    /**
     * Parse the username from the specified authorization string.  If none
     * can be identified, return <code>null</code>
     *
     * @param authorization Authorization string to be parsed
     */
    protected String parseUsername(String authorization) {

        //System.out.println("Authorization token : " + authorization);
        // Validate the authorization credentials format
        if (authorization == null)
            return (null);
        if (!authorization.startsWith("Digest "))
            return (null);
        authorization = authorization.substring(7).trim();

        StringTokenizer commaTokenizer =
            new StringTokenizer(authorization, ",");

        while (commaTokenizer.hasMoreTokens()) {
            String currentToken = commaTokenizer.nextToken();
            int equalSign = currentToken.indexOf('=');
            if (equalSign < 0)
                return null;
            String currentTokenName =
                currentToken.substring(0, equalSign).trim();
            String currentTokenValue =
                currentToken.substring(equalSign + 1).trim();
            if ("username".equals(currentTokenName))
                return (removeQuotes(currentTokenValue));
        }

        return (null);

    }


    /**
     * Register an authenticated Principal and authentication type in our
     * request, in the current session (if there is one), and with our
     * SingleSignOn valve, if there is one.  Set the appropriate cookie
     * to be returned.
     *
     * @param request The servlet request we are processing
     * @param response The servlet response we are generating
     * @param authType The authentication type to be registered
     * @param username Username used to authenticate (if any)
     * @param password Password used to authenticate (if any)
     */
    protected void register(HttpServletRequest request, HttpServletResponse response,
                            User user, String authType,
                            String username, String password) {

        try {
            Application application = Application.getInstance();
            SecurityService security = (SecurityService)application.getService(SecurityService.class);
            security.login(request, user.getUsername(), Encryption.decrypt((String)user.getProperty("weakpass"), user.getId())); // decrypt password?
        }
        catch (Exception e) {
            Log.getLog(getClass()).error("Unable to register user: " + e.toString());
        }

    }

    /**
     * Removes the quotes on a string.
     */
    protected static String removeQuotes(String quotedString) {
        if (quotedString.length() > 2) {
            return quotedString.substring(1, quotedString.length() - 1);
        } else {
            return new String();
        }
    }


    /**
     * Generate a unique token. The token is generated according to the
     * following pattern. NOnceToken = Base64 ( MD5 ( client-IP ":"
     * time-stamp ":" private-key ) ).
     *
     * @param request HTTP Servlet request
     */
    protected String generateNOnce(HttpServletRequest request) {
        long currentTime = System.currentTimeMillis();

        String nOnceValue = request.getRemoteAddr() + ":" +
            currentTime + ":" + key;

        byte[] buffer = md5Helper.digest(nOnceValue.getBytes());
        nOnceValue = md5Encoder.encode(buffer);

        // Updating the value in the no once hashtable
        nOnceTokens.put(nOnceValue, new Long(currentTime + nOnceTimeout));

        return nOnceValue;
    }


    /**
     * Generates the WWW-Authenticate header.
     * <p>
     * The header MUST follow this template :
     * <pre>
     *      WWW-Authenticate    = "WWW-Authenticate" ":" "Digest"
     *                            digest-challenge
     *
     *      digest-challenge    = 1#( realm | [ domain ] | nOnce |
     *                  [ digest-opaque ] |[ stale ] | [ algorithm ] )
     *
     *      realm               = "realm" "=" realm-value
     *      realm-value         = quoted-string
     *      domain              = "domain" "=" <"> 1#URI <">
     *      nonce               = "nonce" "=" nonce-value
     *      nonce-value         = quoted-string
     *      opaque              = "opaque" "=" quoted-string
     *      stale               = "stale" "=" ( "true" | "false" )
     *      algorithm           = "algorithm" "=" ( "MD5" | token )
     * </pre>
     *
     * @param request HTTP Servlet request
     * @param nOnce nonce token
     */
    protected void setAuthenticateHeader(HttpServletRequest request,
                                       HttpServletResponse response,
                                       String realmName,
                                       String nOnce) {

        // Get the realm name
        if (realmName == null) {
            realmName = request.getServerName();
            if (request.getServerPort() != 80) {
                realmName += ":" + request.getServerPort();
            }
        }

        byte[] buffer = md5Helper.digest(nOnce.getBytes());

        String authenticateHeader = "Digest realm=\"" + realmName + "\", "
            +  "qop=\"auth\", nonce=\"" + nOnce + "\", " + "opaque=\""
            + md5Encoder.encode(buffer) + "\"";
        // System.out.println("Authenticate header value : "
        //                   + authenticateHeader);
        response.setHeader("WWW-Authenticate", authenticateHeader);

    }

    
}
