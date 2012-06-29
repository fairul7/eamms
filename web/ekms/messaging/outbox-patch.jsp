<%@page contentType = "text/html" %>
<%@page language = "java" %>
<%@page import = "javax.sql.DataSource,
                  kacang.Application,
                  com.tms.collab.messaging.model.MessagingModule,
                  com.tms.collab.messaging.model.MessagingDao"%>
<%@page import = "java.sql.Connection" %>
<%@page import = "java.sql.PreparedStatement" %>
<%@page import = "java.sql.ResultSet" %>
<%@page import = "java.util.ArrayList" %>
<%@page import = "java.util.HashSet" %>
<%@page import = "java.util.Iterator" %>
<%@page import = "java.util.List" %>
<%@page import = "java.util.Set" %>
<%@page import = "kacang.util.UuidGenerator" %>
<%@page import = "java.io.PrintWriter" %>
<%@page import = "java.io.StringWriter"%>
<%@page import = "com.tms.collab.messaging.model.Folder" %>


<%--
#
# -----------------------------------------------------------------------------
# 
#	PURPOSE:
#	=======
#	
#	Db Migration for Outbox implementation. Will create an OUTBOX Folder
#   in emlfolder table for every user that doesn't have one yet.
#
#
# ------------------------------------------------------------------------------
#    
--%>
<%
		String stackTrace = null;
        DataSource dataSource = null;
        Connection conn = null;
        PreparedStatement pstmt1 = null;
        PreparedStatement pstmt2 = null;
        PreparedStatement pstmt3 = null;
        ResultSet rs1 = null;
        ResultSet rs2 = null;
	    Set allUserIds = new HashSet(); // userIds without OUTBOX
	    Set userIdsWithOutbox = new HashSet();
		int totalUsers = 0;        

        try {
            Application app = Application.getInstance();
            MessagingModule mm = (MessagingModule)app.getModule(MessagingModule.class);
            MessagingDao dao = (MessagingDao)mm.getDao();
            dataSource = dao.getDataSource();

        	conn = dataSource.getConnection();
        
        	// insert folder
        	pstmt1 = conn.prepareStatement(
                "insert into emlFolder " +
                "(folderId, userId, name, specialFolder, diskUsage, parentId) " +
                "values (?, ?, ?, ?, ?, ?)");
        
        	// find all user id who does not have an outbox special folder
        	pstmt2 = conn.prepareStatement(
        		"select distinct user.id as userId from security_user as user where user.id <> 'anonymous'");
        		
        
            // find all user id who have an outbox special folder
            pstmt3 = conn.prepareStatement(
                 "select distinct user.id as userId from security_user as user "+
                 "inner join emlFolder as folder on folder.userId = user.id "+
                 "where user.id <> 'anonymous' and folder.specialFolder = '1' "+
                 "and folder.name in ('"+Folder.FOLDER_OUTBOX+"')");
            
            
        	rs1 = pstmt2.executeQuery();
        	while(rs1.next()) {
        		String userId = rs1.getString("userId");
        		allUserIds.add(userId);
        	}
            totalUsers = allUserIds.size();

            rs2 = pstmt3.executeQuery();
            while(rs2.next()) {
            	String userId = rs2.getString("userId");
				userIdsWithOutbox.add(userId);
                allUserIds.remove(userId);
            }
        

        	for (Iterator i = allUserIds.iterator(); i.hasNext(); ) {
        		String userId = (String) i.next();
            
        		pstmt1.setString(1, UuidGenerator.getInstance().getUuid());
        		pstmt1.setString(2, userId);
        		pstmt1.setString(3, Folder.FOLDER_OUTBOX);
        		pstmt1.setBoolean(4, true);
        		pstmt1.setInt(5, 0);
        		pstmt1.setString(6, null);
            
        		pstmt1.executeUpdate();
        	}
        }
        catch(Exception e) {
            StringWriter writer = null;
            PrintWriter pw = null;
        	try {
        	    writer = new StringWriter();
                pw = new PrintWriter(writer);
                e.printStackTrace(pw);
                stackTrace = writer.toString();
            }
            finally {
            	if (pw != null) { pw.close(); }
                if (writer != null) { writer.close(); }
            }
        }
        finally {
        	if (rs1 != null) { rs1.close(); }
            if (rs2 != null) { rs2.close(); }
            if (pstmt1 != null) { pstmt1.close(); }
            if (pstmt2 != null) { pstmt2.close(); }
            if (pstmt3 != null) { pstmt3.close(); }
            if (conn != null) { conn.close(); }
        }

   if (stackTrace == null) {
		out.println("OK<br/><br/>");
   }
   else {
        out.println("Exception<br/>");
        out.println(stackTrace+"<br/><br/><br/>");
   }

   out.println("STATISTICS<br/>");
   out.println("==========<br/>");
   out.println("<br/>");
   out.println("Total User Ids:"+totalUsers+"<br/>");
   out.println("Total User (added Outbox Folder):"+allUserIds.size()+"<br/>");
   if (allUserIds.size() > 0) { out.println("<ul>"); }
   for (Iterator i = allUserIds.iterator(); i.hasNext(); ) {
      out.println("<li>"+(String)i.next()+"</li>");
   }
   if (allUserIds.size() > 0) { out.println("</ul>"); }
   out.println("Total User (already have Outbox Folder):"+userIdsWithOutbox.size());
   if (userIdsWithOutbox.size() > 0) { out.println("<ul>"); }
   for (Iterator i = userIdsWithOutbox.iterator(); i.hasNext(); ) {
	  out.println("<li>"+(String)i.next()+"</li>");
   }
   if (userIdsWithOutbox.size() > 0) { out.println("</ul>"); }
%>




