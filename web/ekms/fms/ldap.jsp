<%@ include file="/common/header.jsp" %>
<%@ page import="kacang.Application, java.util.*, java.sql.*, com.tms.ldap.LdapSyncBean, org.apache.commons.collections.SequencedHashMap, javax.naming.Context, javax.naming.directory.*, javax.naming.NamingEnumeration"%>


<%
	Map userMap = new SequencedHashMap();

	String factory = "com.sun.jndi.ldap.LdapCtxFactory";
	String url = Application.getInstance().getProperty("AD.Url");
	String principal = Application.getInstance().getProperty("AD.Principal");
	String credentials = Application.getInstance().getProperty("AD.Credentials");
	String context = Application.getInstance().getProperty("AD.Context");
	
	// Set up the environment for creating the initial context
    Hashtable env = new Hashtable(11);
    env.put(Context.INITIAL_CONTEXT_FACTORY, factory);
    env.put(Context.PROVIDER_URL, url);
    env.put(Context.SECURITY_PRINCIPAL, principal);
    env.put(Context.SECURITY_CREDENTIALS, credentials);
    //env.put(Context.SECURITY_AUTHENTICATION,"simple");//"DIGEST-MD5");
    
    DirContext ctx = null;
        try {
            // Create initial context
            ctx = new InitialDirContext(env);
            //searchContext(ctx, context, userMap);
            
            NamingEnumeration answer;
            int limit = 0;
            //String filter = "(&(objectCategory=Person)(sAMAccountName=fairulABC))";
            String filter = "(objectCategory=Person)";
            SearchControls controls = new SearchControls();
            controls.setCountLimit(limit);
            controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
            try{
            answer = ctx.search(context, filter, controls);
            int index=1;
            while(answer.hasMoreElements()) {
            	try {
	                SearchResult sr = (SearchResult)answer.next();
	                Attributes attribs = sr.getAttributes();
	               
	                String username = "";        
	                String department = ""; 
					String nick = "";
					String name = "";
					String mail = "";
					String memberOf = "";
					
	                Attribute aUsername = attribs.get("sAMAccountName");       
	                Attribute aDivision = attribs.get("department");
	                Attribute aNickname = attribs.get("mailNickname");
	                Attribute aName = attribs.get("name");
	                Attribute aMail = attribs.get("mail");
	                //Attribute aMemberOf = attribs.get("memberOf");
	                
	                //memberOf 
	                
	                  
	                if (aUsername != null){
	                    username = aUsername.get().toString().toLowerCase();
	                }
	                if (aDivision != null) {
	                    department = aDivision.get().toString();
	                }
	                try{
		                if(aNickname != null){
		                	nick = aNickname.get().toString();
		                }
		                if(aName != null){
		                	name = aName.get().toString();
		                }	                
		                if(aMail != null){
		                	mail = aMail.get().toString();
		                }
		                //if(aMemberOf != null){
		                	//memberOf = aMemberOf.get().toString();
		                //}
		                
	                }catch(Exception e){System.out.println("Error AD"+e);}
	                
	                out.println("<b>"+index+"</b> Username="+username+" | mailNickname ="+aNickname+" | name="+name+" | mail="+mail+ "<br>");
            	} catch(Exception er){}
            	index++;
            }
            
            }catch(Exception er){}
            
        } catch (Exception e) {
            
            throw new SecurityException("Error retrieving LDAP users: " + e.getMessage());
        }
        finally {
            try {
                if (ctx != null)
                ctx.close();
            }
            catch (Exception e) { ; }
        }

		
%>

