package com.tms.util.security;

import kacang.stdui.*;
import kacang.Application;
import kacang.ui.Forward;
import kacang.ui.Event;
import kacang.util.Log;
import kacang.util.UuidGenerator;
import kacang.util.Encryption;
import kacang.model.DaoQuery;
import kacang.services.security.SecurityService;
import kacang.services.security.SecurityException;
import kacang.services.security.User;

import java.util.Collection;
import java.util.Map;
import java.util.ArrayList;
import java.util.Iterator;

public class LdapImportTable extends Table {

    protected String url; // ldap://omega-1:389
    protected String principal;
    protected String credentials;
    protected String context; // CN=Users,DC=shoppe,DC=com,DC=my
    protected String message;

    public LdapImportTable() {
    }

    public LdapImportTable(String name) {
        super(name);
    }

    public void init() {
        setModel(new LdapImportTableModel());
        setSortable(false);
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public class LdapImportTableModel extends TableModel {

        protected int ldapUserCount;

        public LdapImportTableModel() {
            addColumn(new TableColumn("username", "Username"));
            addColumn(new TableColumn("firstName", "First Name"));
            addColumn(new TableColumn("lastName", "Last Name"));
            addColumn(new TableColumn("email1", "Email"));
            addFilter(new TableFilter("username", null));
            addAction(new TableAction("import", "Import", "Please confirm user import?"));
        }

        public Collection getTableRows() {

            Collection userList = new ArrayList();
            ldapUserCount = 0;
            message = "";

            if (url == null || url.trim().length() == 0 || context == null || context.trim().length() == 0) {
                return userList;
            }

            try {
                // set ldap settings
                LdapConnectorBean ldap = new LdapConnectorBean();
                ldap.setUrl(url);
                ldap.setPrincipal(principal);
                ldap.setCredentials(credentials);
                ldap.setContext(context);

                // get ldap users
                String filterValue = (String)getFilterValue("username");
                Map ldapUserMap;
                if (filterValue != null && filterValue.trim().length() > 0) {
                    String query = "(&(|(sAMAccountName=*" + filterValue + "*)(givenName=*" + filterValue + "*)(sn=*" + filterValue + "*)(displayName=*" + filterValue + "*)(mail=*" + filterValue + "*))(objectCategory=Person))";
                    ldap.setFilter(query);
                    ldapUserMap = ldap.getLdapUserMap();
                }
                else {
                    ldapUserMap = ldap.getLdapUserMap();
                }

                // get current users
                Application app = Application.getInstance();
                SecurityService security = (SecurityService)app.getService(SecurityService.class);
                Collection currentUserList = security.getUsers(new DaoQuery(), 0, -1, "username", false);

                // filter away current users
                for (Iterator i=currentUserList.iterator(); i.hasNext();) {
                    User user = (User)i.next();
                    if (ldapUserMap.containsKey(user.getUsername())) {
                        ldapUserMap.remove(user.getUsername());
                    }
                }

                // get paged list and total count
                ArrayList newUserList = new ArrayList(ldapUserMap.values());
                ldapUserCount = newUserList.size();
                int start = getStart();
                if (start > ldapUserCount) {
                    start = 0;
                }
                int end = start + getPageSize();
                if (end > ldapUserCount) {
                    end = ldapUserCount;
                }
                userList = newUserList.subList(start, end);

            }
            catch (Exception e) {
                Log.getLog(getClass()).error("Error retrieving LDAP users", e);
                message = "Error retrieving LDAP users: " + e.toString();
            }

            return userList;
        }

        public int getTotalRowCount() {
            return ldapUserCount;
        }

        public String getTableRowKey() {
            return "username";
        }

        public Forward processAction(Event event, String action, String[] selectedKeys) {
            if ("import".equals(action)) {
                // import user
                try {
                    // set ldap settings
                    LdapConnectorBean ldap = new LdapConnectorBean();
                    ldap.setUrl(url);
                    ldap.setPrincipal(principal);
                    ldap.setCredentials(credentials);
                    ldap.setContext(context);

                    // get ldap users
                    Map ldapUserMap = ldap.getLdapUserMap();

                    // get security service
                    Application app = Application.getInstance();
                    SecurityService security = (SecurityService)app.getService(SecurityService.class);

                    // create users
                    UuidGenerator uuid = UuidGenerator.getInstance();
                    for (int i=0; i<selectedKeys.length; i++) {
                        String username = selectedKeys[i];
                        User user = (User)ldapUserMap.get(username);
                        if (user != null) {
                            try {
                                String userId = uuid.getUuid();
                                String password = username;
                                String encryptedPassword = Encryption.encrypt(password);
                                user.setId(userId);
                                user.setPassword(encryptedPassword);
                                user.setProperty("weakpass", username);
                                user.setProperty("active", "1");
                                security.addUser(user, true);
                            }
                            catch (SecurityException e) {
                                Log.getLog(getClass()).error("Error importing LDAP user " + user + ": " + e.toString());
                            }
                        }
                    }

                }
                catch (Exception e) {
                    Log.getLog(getClass()).error("Error importing LDAP users", e);
                }
            }
            return super.processAction(event, action, selectedKeys);
        }

    }

}
