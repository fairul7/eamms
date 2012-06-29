<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<jsp:useBean id="menu" class="org.apache.commons.collections.SequencedHashMap" />
<jsp:useBean id="icons" class="org.apache.commons.collections.SequencedHashMap" />

<%
	kacang.Application app = kacang.Application.getInstance();
    menu.put("/cmsadmin/content", app.getMessage("cms.label.content", "Content"));
    menu.put("/cmsadmin/interactive", app.getMessage("cms.label.interactive", "Interactive"));
    menu.put("/cmsadmin/security", app.getMessage("cms.label.userManagement", "User Management"));
    menu.put("/cmsadmin/siteadmin", app.getMessage("cms.label.siteAdmin", "Site Admin"));

    icons.put("/cmsadmin/content", "../images/contenticon.gif");
    icons.put("/cmsadmin/interactive", "../images/taskicon.gif");
    icons.put("/cmsadmin/security", "../images/usericon.gif");
%>



<HTML>
<HEAD>
<TITLE><fmt:message key='cms.label.mainTitle'/></TITLE>
    <link rel="stylesheet" href="../styles/style.css">
</HEAD>
<BODY leftmargin="0" topmargin="0" marginheight="0" marginwidth="0">

<TABLE width="100%" cellpadding=0 cellspacing=0 border=0 bgcolor="#333333" background="../images/bannerbg.gif">
<TR>
	<TD width="407"><img src="../images/banner.gif"></TD>
	<TD valign="top" align="right">
                <span>
                <TABLE cellpadding="0" border="0" cellspacing="0">
                <TR>
                    <TD valign="top"><img src="../images/arrowup.gif"><a href="../security/profile.jsp" class="topmenu"><fmt:message key='cms.label.myProfile'/></a></TD>
<%--
                    <TD valign="top"><img src="../images/arrowup.gif"><a href="#" class="topmenu"><fmt:message key='cms.label.systemSetup'/>System Setup</a></TD>
                    <TD valign="top"><img src="../images/arrowup.gif"><a href="#" class="topmenu"><fmt:message key='cms.label.help'/>Help</a></TD>
--%>
                    <TD valign="middle"><img width="20" src="../images/clear.gif"></TD>
                </TR>
                </TABLE>
                </span>
	</TD>
	<TD bgcolor="#CCCCCC" width="1"><img src="../images/clear.gif" width="1"></TD>
	<TD align="right" valign="bottom">
                <span>
                <TABLE cellpadding="10" border="0" cellspacing="0">
                  <TR>
                    <TD valign="top">
                        <font class="logon"><fmt:message key='cms.label.username'/> : </font><font class="logon2"><c:out value="${currentUser.username}"/></font>
                        <font class="logon">[ <a href="../logout.jsp" class="logon"><fmt:message key='cms.label.logout'/></a>]</font>
                    </TD>
                  </TR>
                </TABLE>
                </span>
	</TD>
</TR>
</TABLE>
<img src="../images/clear.gif" height="1"><br>

<TABLE cellpadding=0 cellspacing=0 border=0 bgcolor="#E2E2E2" height="28">
  <TR>

<%
    for (java.util.Iterator i=menu.keySet().iterator(); i.hasNext();) {
        String uri = (String)i.next();
        String label = (String)menu.get(uri);
        String path = request.getServletPath();
        String color = (path.startsWith(uri)) ? "#336699" : "#6699CC";
        String icon = (icons.containsKey(uri)) ? (String)icons.get(uri) : "../images/taskicon.gif";
%>
    <TD valign="middle" nowrap bgcolor="<%= color %>" width="35"><img src="<%= icon %>"></TD>
    <TD bgcolor="<%= color %>"><a href="../..<%= uri %>/index.jsp" target="_top" class="menulink"><%= label %></a></TD>
    <TD bgcolor="<%= color %>" valign="middle" width="25"><img src="../images/downarrowa.gif"></TD>
    <TD bgcolor="#E2E2E2"><img src="../images/clear.gif" width="2"></TD>
<%
    }
%>

  </TR>
</TABLE>


<TABLE width="100%" cellpadding="0" height="2" bgcolor="#336699" border="0" cellspacing="0">
      <TD height="2" valign="middle"><img height="2" src="../images/clear.gif"></TD>
      <TD height="2" valign="middle"><img height="2" src="../images/clear.gif"></TD>
      <TD height="2" align="right" valign="middle"><img height="2" src="../images/clear.gif"></TD>
    </TR>
</TABLE>

<div></div>

<IFRAME SRC="<%= request.getContextPath() %>/common/poll.jsp" WIDTH="0" HEIGHT="0" FRAMEBORDER="0" MARGINHEIGHT="0" MARGINWIDTH="0"></IFRAME>

</BODY>
</HTML>
