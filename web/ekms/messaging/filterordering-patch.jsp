<%@page contentType = "text/html" %>
<%@page language = "java" %>

<%@page import = "java.io.IOException" %>
<%@page import = "java.io.PrintWriter" %>
<%@page import = "java.io.StringWriter" %>
<%@page import = "java.sql.Connection" %>
<%@page import = "java.sql.PreparedStatement" %>
<%@page import = "java.sql.ResultSet" %>
<%@page import = "java.sql.SQLException" %>
<%@page import = "java.util.ArrayList" %>
<%@page import = "java.util.Iterator" %>
<%@page import = "java.util.List" %>
<%@page import = "javax.sql.DataSource" %>
<%@page import = "com.tms.collab.messaging.model.Filter" %>
<%@page import = "com.tms.collab.messaging.model.MessagingDao" %>
<%@page import = "com.tms.collab.messaging.model.MessagingModule" %>
<%@page import = "kacang.Application" %>
<%@page import = "java.util.Map" %>
<%@page import = "java.util.HashMap" %>



<%
List stackTraces = new ArrayList();
Connection conn = null;
PreparedStatement pstmt = null;
PreparedStatement pstmt2 = null;
ResultSet rst = null;
ResultSet rst2 =null;
Map usersAffected = new HashMap(); 


try {
    Application app = Application.getInstance();
    MessagingModule mm = (MessagingModule)app.getModule(MessagingModule.class);
    MessagingDao dao = (MessagingDao)mm.getDao();
    DataSource dataSource = dao.getDataSource();

    // get users with filter(s) configured
    List usersWithFilter = new ArrayList();
    conn = dataSource.getConnection();
    pstmt = conn.prepareStatement(
            "select distinct usr.id as usrId from security_user as usr " + 
            "inner join emlfilter as filter " + 
            "where filter.userID = usr.id ");
    rst = pstmt.executeQuery();
    while(rst.next()) {
        usersWithFilter.add(rst.getString("usrId"));
    }
    
    
    
    // add order number to filters of each user
    for (Iterator i = usersWithFilter.iterator(); i.hasNext(); ) {
        String userId = (String) i.next();
        
        try {
            pstmt2 = conn.prepareStatement(
                "SELECT filterId as filterId FROM " + 
                "emlFilter WHERE userId=? ORDER BY name ASC");
            pstmt2.setString(1, userId);
            rst2 = pstmt2.executeQuery();
            
            int count = 1;
            while(rst2.next()) {
                String fId = rst2.getString("filterId");
                Filter f = mm.getFilter(fId);
                if (f.getFilterOrder() <= 0) {
                    f.setFilterOrder(count);
                    dao.updateFilter(f);
                    
                    if (usersAffected.get(userId) == null) {
                        List fids = new ArrayList();
                        fids.add(fId+" having order of "+count);
                        usersAffected.put(userId, fids);
                    }
                    else {
                        List fids = (List) usersAffected.get(userId);
                        fids.add(fId+" having order of "+count);
                    }
                }
                count = count + 1;
            }
        }
        finally {
            if (pstmt2 != null) {
                pstmt2.close();
            }
            if (rst2 != null) {
                rst2.close();
            }
            pstmt2= null;
            rst2= null;
        }
    }
}
catch(Exception e) {
    StringWriter writer = null;
    PrintWriter pw = null;
    try {
        writer = new StringWriter();
        pw = new PrintWriter(writer);
        e.printStackTrace(pw);
        stackTraces.add(writer.toString());
    }
    finally {
        if (pw != null) { pw.close(); }
        if (writer != null) {
            try {
                writer.close(); 
            }
            catch(IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }
}
finally {
    if (conn != null) { 
        try {
            conn.close(); 
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
    }
    if (pstmt != null) { pstmt.close(); }
    if (rst != null) { rst.close(); }
}


if (stackTraces.size() <= 0) {
    out.println("OK<br/>");
}
else {
    out.println("Exception<br/>");
    for (Iterator i = stackTraces.iterator(); i.hasNext(); ) {
        String stackTrace = (String) i.next();
		out.println(stackTrace+"<br/>");
    }
}


out.println("STATISTICS<br/>");
out.println("==========<br/>");
out.println("<table border=1>");
out.println("<tr>");
out.println("<td>User</td>");
out.println("<td>Filter <-> order</td>");
out.println("</tr>");

for (Iterator i = usersAffected.entrySet().iterator(); i.hasNext(); ) {
    Map.Entry entry = (Map.Entry) i.next();

	String usr = (String) entry.getKey();
	List fs = (List) entry.getValue();

	
	for (Iterator ii = fs.iterator(); ii.hasNext(); ) {
		String f = (String) ii.next();
		out.println("<tr>");
		out.println("<td>"+usr+"</td>");
		out.println("<td>"+f+"</td>");
		out.println("</tr>");
	}

	
	
}

out.println("</table>");


%>

