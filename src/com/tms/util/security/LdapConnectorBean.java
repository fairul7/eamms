package com.tms.util.security;

import kacang.util.Log;
import kacang.services.security.User;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.*;
import java.util.Hashtable;
import java.util.Map;
import java.io.Serializable;

import org.apache.commons.collections.SequencedHashMap;

public class LdapConnectorBean implements Serializable {

    protected String factory = "com.sun.jndi.ldap.LdapCtxFactory";
    protected String url; // ldap://omega-1:389
    protected String principal;
    protected String credentials;
    protected String context; // CN=Users,DC=shoppe,DC=com,DC=my
    protected String filter;
    protected int limit;

    public String getFactory() {
        return factory;
    }

    public void setFactory(String factory) {
        this.factory = factory;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPrincipal() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }

    public String getCredentials() {
        return credentials;
    }

    public void setCredentials(String credentials) {
        this.credentials = credentials;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    /**
     * Please set all the properties first
     * @return A Map of username String=User objects
     */
    public Map getLdapUserMap() throws SecurityException {

        Map userMap = new SequencedHashMap();

        String factory = getFactory();
        String url = getUrl();
        String principal = getPrincipal();
        String credentials = getCredentials();
        String context = getContext();

        // Set up the environment for creating the initial context
        Hashtable env = new Hashtable(11);
/*
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, "ldap://omega-1:389");
        env.put(Context.SECURITY_PRINCIPAL, "CN=kahliang,CN=Users,DC=shoppe,DC=com,DC=my");
        env.put(Context.SECURITY_CREDENTIALS, "");
*/
        env.put(Context.INITIAL_CONTEXT_FACTORY, factory);
        env.put(Context.PROVIDER_URL, url);
        env.put(Context.SECURITY_PRINCIPAL, principal);
        env.put(Context.SECURITY_CREDENTIALS, credentials);

        DirContext ctx = null;
        try {
            // Create initial context
            ctx = new InitialDirContext(env);

            searchContext(ctx, context, userMap);

            return userMap;
        }
        catch (Exception e) {
            Log.getLog(getClass()).error("Error retrieving LDAP users", e);
            throw new SecurityException("Error retrieving LDAP users: " + e.getMessage());
        }
        finally {
            try {
                // Close the context when we're done
                if (ctx != null) {
                    ctx.close();
                }
            }
            catch (NamingException e) {
                ;
            }
        }

    }

    protected void searchContext(DirContext ctx, String context, Map userMap) throws NamingException {
        // Search for objects that have those matching attributes
        NamingEnumeration answer;
        if (filter != null) {
            SearchControls controls = new SearchControls();
            controls.setCountLimit(limit);
            controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
            answer = ctx.search(context, filter, controls);
        }
        else {
            String filter = "(objectCategory=Person)";
            SearchControls controls = new SearchControls();
            controls.setCountLimit(limit);
            controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
            answer = ctx.search(context, filter, controls);
        }
        while(answer.hasMoreElements()) {
            SearchResult sr = (SearchResult)answer.next();
            Attributes attribs = sr.getAttributes();

            String name = null;
            String username = null;
            String firstName = null;
            String lastName = null;
            String email = null;

            Attribute aName = attribs.get("displayName");
            Attribute aFirstName = attribs.get("givenName");
            Attribute aLastName = attribs.get("sn");
            Attribute aUsername = attribs.get("sAMAccountName");
            Attribute aMail = attribs.get("mail");
            if (aName != null)
                name = aName.get().toString();
            if (aUsername != null)
                username = aUsername.get().toString();
            if (aFirstName != null)
                firstName = aFirstName.get().toString();
            if (aLastName != null)
                lastName = aLastName.get().toString();
            if (firstName == null)
                firstName = (lastName == null) ? name : lastName;
            if (aMail != null)
                email = aMail.get().toString();

            User user = new User();
            user.setUsername(username);
            user.setProperty("firstName", firstName);
            user.setProperty("lastName", lastName);
            user.setProperty("email1", email);

            userMap.put(username, user);
        }
    }


}
