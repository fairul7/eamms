package com.tms.ldap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import kacang.Application;
import kacang.services.security.User;
import kacang.util.Log;

import org.apache.commons.collections.SequencedHashMap;


/**
 * Created by IntelliJ IDEA.
 * User: oilai
 * Date: Nov 28, 2005
 * Time: 10:19:32 AM
 * To change this template use File | Settings | File Templates.
 */
public class LdapSyncBean {
    protected String factory = "com.sun.jndi.ldap.LdapCtxFactory";
    protected String url;
    protected String principal;
    protected String credentials;
    protected String context;
    protected String filter;
    protected int limit;

    public LdapSyncBean() {
    	
    	setUrl(Application.getInstance().getProperty("AD.Url"));
    	setPrincipal(Application.getInstance().getProperty("AD.Principal"));
    	setCredentials(Application.getInstance().getProperty("AD.Credentials"));
    	setContext(Application.getInstance().getProperty("AD.Context"));
    }
    
    public String getFactory()
    {
        return factory;
    }

    public void setFactory(String factory)
    {
        this.factory = factory;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public String getPrincipal()
    {
        return principal;
    }

    public void setPrincipal(String principal)
    {
        this.principal = principal;
    }

    public String getCredentials()
    {
        return credentials;
    }

    public void setCredentials(String credentials)
    {
        this.credentials = credentials;
    }

    public String getContext()
    {
        return context;
    }

    public void setContext(String context)
    {
        this.context = context;
    }

    public String getFilter()
    {
        return filter;
    }

    public void setFilter(String filter)
    {
        this.filter = filter;
    }

    public int getLimit()
    {
        return limit;
    }

    public void setLimit(int limit)
    {
        this.limit = limit;
    }
    
    public boolean ADUserExist(String username){
    	
    return checkADuser(username);
    	
    }

    public boolean checkADuser(String username){
    	boolean exist= false;
    	
    	String factory = getFactory();
        String url = getUrl();
        String principal = getPrincipal();
        String credentials = getCredentials();
        String context = getContext();

        
        // Set up the environment for creating the initial context
        Hashtable env = new Hashtable(11);
        env.put(Context.INITIAL_CONTEXT_FACTORY, factory);
        env.put(Context.PROVIDER_URL, url);
        env.put(Context.SECURITY_PRINCIPAL, principal);
        env.put(Context.SECURITY_CREDENTIALS, credentials);
        //env.put(Context.SECURITY_AUTHENTICATION,"simple");//"DIGEST-MD5");

        DirContext ctx = null;
        try
        {
            // Create initial context
            ctx = new InitialDirContext(env);
            NamingEnumeration answer;
            int limit = 0;
            String filter = "(&(objectCategory=Person)(sAMAccountName="+username+"))";
            SearchControls controls = new SearchControls();
            controls.setCountLimit(limit);
            controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
            try{

            answer = ctx.search(context, filter, controls);
            while(answer.hasMoreElements()) {
            	try {
	                SearchResult sr = (SearchResult)answer.next();
	                Attributes attribs = sr.getAttributes();
	               
	                        
	                String department = null; 
	
	                Attribute aUsername = attribs.get("sAMAccountName");       
	                Attribute aDivision = attribs.get("department");
	                
	                  
	                if (aUsername != null) {
	                    username = aUsername.get().toString().toLowerCase();
	                    exist=true;
	                }
	                if (aDivision != null) {
	                    department = aDivision.get().toString();
	                }
	               
            	} 
            	catch(Exception er){}
            	
            }
            } 
        	catch(Exception er){}
                 
           
        }
        catch (Exception e)
        {
            Log.getLog(getClass()).error("Error retrieving LDAP users", e);
            throw new SecurityException("Error retrieving LDAP users: " + e.getMessage());
        }
        finally
        {
            try
            {
                if (ctx != null)
                    ctx.close();
            }
            catch (NamingException e) { ; }
        }
        
    	return exist;
    	
    }
    public Map getLdapUserMap() throws SecurityException {

        Map userMap = new SequencedHashMap();

        String factory = getFactory();
        String url = getUrl();
        String principal = getPrincipal();
        String credentials = getCredentials();
        String context = getContext();

        
        // Set up the environment for creating the initial context
        Hashtable env = new Hashtable(11);
        env.put(Context.INITIAL_CONTEXT_FACTORY, factory);
        env.put(Context.PROVIDER_URL, url);
        env.put(Context.SECURITY_PRINCIPAL, principal);
        env.put(Context.SECURITY_CREDENTIALS, credentials);
        //env.put(Context.SECURITY_AUTHENTICATION,"simple");//"DIGEST-MD5");

        DirContext ctx = null;
        try
        {
            // Create initial context
            ctx = new InitialDirContext(env);
            searchContext(ctx, context, userMap);
                 
            return userMap;
        }
        catch (Exception e)
        {
            Log.getLog(getClass()).error("Error retrieving LDAP users", e);
            throw new SecurityException("Error retrieving LDAP users: " + e.getMessage());
        }
        finally
        {
            try
            {
                if (ctx != null)
                    ctx.close();
            }
            catch (NamingException e) { ; }
        }
    }

    protected void searchContext(DirContext ctx, String context, Map userMap) throws NamingException
    {
        NamingEnumeration answer;
        if (filter != null)
        {

            SearchControls controls = new SearchControls();

            controls.setCountLimit(limit);

            controls.setSearchScope(SearchControls.SUBTREE_SCOPE);

            answer = ctx.search(context, filter, controls);

        }
        else
        {

            String filter = "(objectCategory=Person)";
            SearchControls controls = new SearchControls();
            controls.setCountLimit(limit);
            controls.setSearchScope(SearchControls.SUBTREE_SCOPE);

            answer = ctx.search(context, filter, controls);

        }
        while(answer.hasMoreElements())
        {
            SearchResult sr = (SearchResult)answer.next();
            Attributes attribs = sr.getAttributes();
            String name = null;
            String username = null;
            String firstName = null;
            String lastName = null;
            String email = null;
            String address = null;
            String postcode = null;
            String state = null;
            String telOffice = null;
            String telMobile = null;
            String country=null;
            String city=null;
            String fax = null;
            String department = null;
            String designation = null;

            /*String country = null;
         String telHome = null;
         String city = null;
         String fax = null;*/
            //Log.getLog(getClass()).info("HERE 10 --------");
            Attribute aName = attribs.get("displayName");
            Attribute aFirstName = attribs.get("givenName");
            Attribute aLastName = attribs.get("sn");
            Attribute aUsername = attribs.get("sAMAccountName");
            Attribute aMail = attribs.get("mail");
            Attribute aAddress = attribs.get("streetAddress");
            Attribute aCity = attribs.get("l");
            Attribute aState = attribs.get("st");
            Attribute aPostalCode = attribs.get("postalCode");
            Attribute aCountry = attribs.get("co");
            Attribute aTelephoneNumber = attribs.get("telephoneNumber");
            Attribute aMobile = attribs.get("mobile");
            Attribute aFax = attribs.get("facsimileTelephoneNumber");
            Attribute aDivision = attribs.get("department");
            Attribute aDesignation = attribs.get("title");
            //Log.getLog(getClass()).info("HERE 11 --------");
            /*Attribute aCountry = attribs.get("");
         Attribute aTelephoneHome = attribs.get("");
         Attribute aCity = attribs.get("");
         Attribute aFax = attribs.get("");*/

            if (aName != null)
                name = aName.get().toString();
            if (aUsername != null)
                username = aUsername.get().toString().toLowerCase();
            if (aFirstName != null)
                firstName = aFirstName.get().toString();
            if (aLastName != null)
                lastName = aLastName.get().toString();
            if (firstName == null)
                firstName = (lastName == null) ? name : lastName;
            if (aMail != null)
                email = aMail.get().toString();

            if (aAddress != null)
                address = aAddress.get().toString();
            if (aState != null)
                state = aState.get().toString();
            if (aPostalCode != null)
                postcode = aPostalCode.get().toString();
            if (aTelephoneNumber != null)
                telOffice = aTelephoneNumber.get().toString();
            if (aMobile != null)
                telMobile = aMobile.get().toString();

            if (aCity != null) {
                city = aCity.get().toString();
            }
            if (aCountry != null) {
                country = aCountry.get().toString();
            }
            if (aFax !=null) {
                fax = aFax.get().toString();
            }
            if (aDivision != null) {
                department = aDivision.get().toString();
            }
            if (aDesignation != null) {
                designation = aDesignation.get().toString();
            }

            //TODO: Add departmental information into other information modules
            Log.getLog(getClass()).info("user="+username+" , department="+department);
            User user = new User();
            user.setUsername(username);
            user.setProperty("firstName", firstName);
            user.setProperty("lastName", lastName);
            user.setProperty("email1", email);
            user.setProperty("address", address);
            user.setProperty("state", state);
            user.setProperty("postcode", postcode);
            user.setProperty("telOffice", telOffice);
            user.setProperty("telMobile", telMobile);
            user.setProperty("city",city);
            user.setProperty("country",country);
            user.setProperty("fax",fax);
            user.setProperty("department",department);
            user.setProperty("designation",designation);
            userMap.put(username, user);

        }
    }
    
    
}
