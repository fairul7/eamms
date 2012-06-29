<%@ page import="com.tms.collab.messaging.model.Util"%>
<%--<%@ include file="/common/header.jsp" %>--%>
<%
    // redirect to activate page if user's intranet account DOES NOT EXIST
    if(!Util.hasIntranetAccount(request)) {
        response.sendRedirect("activate.jsp");
    }
%>
<x:template type="com.tms.collab.messaging.ui.MessagingController" />
<%@ include file="/ekms/includes/header.jsp" %>
<table width="100%" border="0" cellspacing="0" cellpadding="1">
    <tr valign="top">
        <td width="17%" valign="top" align="center">
            <table width="100%" border="0" cellspacing="0" cellpadding="1" class="menuBgOutline">
                <tr>
                    <td>
                        <table width="100%" border="0" cellpadding="1" cellspacing="0" class="menuBgBackground">
                            <tr>
                                <td>
                                    <table width="100%" border="0" cellpadding="0" cellspacing="0" class="menuBgColor">
                                     <tr>
                                            <td colspan="3" valign="middle" bgcolor="666666" class="menuHeader" height="25">
                                                <table cellpadding="0" cellspacing="0" width="100%">
                                                    <tr>
                                                        <td class="menuHeader" width="5%"><img src="<c:url value="/ekms/images/system/blank.gif"/>" width="1" height="1"></td>
                                                        <td class="menuHeader" ><fmt:message key='messaging.label.messaging'/></td>
                                                        <td class="menuHeader" width="18"><img src="<c:url value="/ekms/images/arrowdown.gif"/>"></td>
                                                    </tr>
                                                </table>
                                            </td>
                                        </tr>
<%--
                                        <tr>
                                            <td colspan="3" valign="middle" bgcolor="666666" class="menuHeader" height="25">
                                                <table cellpadding="0" cellspacing="0" width="100%">
                                                    <tr>
                                                        <td class="menuHeader" width="5%"><img src="<c:url value="/ekms/images/system/blank.gif"/>" width="1" height="1"></td>
                                                        <td class="menuHeader" ><fmt:message key='messaging.label.messaging'/></td>
                                                        <td class="menuHeader" width="18"><img src="<c:url value="/ekms/images/arrowdown.gif"/>"></td>
                                                    </tr>
                                                </table>
                                            </td>
                                        </tr>
                                        <%-- row start --%>
                                    <%--
--%>
                                        <%-- row end --%>

                                        <TR><TD COLSPAN="2" ALIGN="CENTER" BGCOLOR="#1059A5" CLASS="menuBgColorHighlight"><IMG SRC="images/system/blank.gif" WIDTH="5" HEIGHT="1"></TD></TR>
                                        <TR>
                                            <TD HEIGHT="28" WIDTH="20%" ALIGN="CENTER"><IMG SRC="<c:url value="/ekms/"/>images/folder.gif" WIDTH="15" HEIGHT="17"></TD>
                                            <TD HEIGHT="28" WIDTH="80%" nowrap><a href="composeMessage.jsp"><FONT COLOR="#FFFFFF" CLASS="menuFont"><b><fmt:message key='messaging.label.composeMessage'/></b></FONT></a></TD>
                                        </TR>
                                        <TR><TD COLSPAN="2" ALIGN="CENTER" BGCOLOR="#000000" CLASS="menuBgColorShadow"><IMG SRC="<c:url value="/ekms/"/>images/blank.gif" WIDTH="5" HEIGHT="1"></TD></TR>
                                        <TR><TD COLSPAN="2" ALIGN="CENTER" BGCOLOR="#1059A5" CLASS="menuBgColorHighlight"><IMG SRC="<c:url value="/ekms/"/>images/blank.gif" WIDTH="5" HEIGHT="1"></TD></TR>
                                        <TR>
                                            <TD HEIGHT="28" WIDTH="20%" ALIGN="CENTER"><IMG SRC="<c:url value="/ekms/"/>images/folder.gif" WIDTH="15" HEIGHT="17"></TD>
                                            <TD HEIGHT="28" WIDTH="80%"><a href="checkEmail.jsp"><FONT COLOR="#FFFFFF" CLASS="menuFont"><b><fmt:message key='messaging.label.downloadPOP3Email'/></b></FONT></a></TD>
                                        </TR>
                                        <TR><TD COLSPAN="2" ALIGN="CENTER" BGCOLOR="#000000" CLASS="menuBgColorShadow"><IMG SRC="<c:url value="/ekms/"/>images/blank.gif" WIDTH="5" HEIGHT="1"></TD></TR>
                                        <TR><TD COLSPAN="2" ALIGN="CENTER" BGCOLOR="#1059A5" CLASS="menuBgColorHighlight"><IMG SRC="<c:url value="/ekms/"/>images/blank.gif" WIDTH="5" HEIGHT="1"></TD></TR>
                                        <TR>
                                            <TD HEIGHT="28" WIDTH="20%" ALIGN="CENTER"><IMG SRC="<c:url value="/ekms/"/>images/folder.gif" WIDTH="15" HEIGHT="17"></TD>
                                            <TD HEIGHT="28" WIDTH="80%"><a href="options.jsp"><FONT COLOR="#FFFFFF" CLASS="menuFont"><b><fmt:message key='messaging.label.options'/></b></FONT></a></TD>
                                        </TR>
<%--Inbox--%>

                                        <TR><TD COLSPAN="2" ALIGN="CENTER" BGCOLOR="#1059A5" CLASS="menuBgColorHighlight"><IMG SRC="images/system/blank.gif" WIDTH="5" HEIGHT="1"></TD></TR>
                                        <tr>
                                            <td colspan="3" valign="middle" bgcolor="666666" class="menuHeader" height="25">
                                                <table cellpadding="0" cellspacing="0" width="100%">
                                                    <tr>
                                                        <td class="menuHeader" width="5%"><img src="<c:url value="/ekms/images/system/blank.gif"/>" width="1" height="1"></td>
                                                        <td class="menuHeader" ><fmt:message key='messaging.label.folders'/></td>
                                                        <td class="menuHeader" width="18"><img src="<c:url value="/ekms/images/arrowdown.gif"/>"></td>
                                                    </tr>
                                                </table>
                                            </td>
                                        </tr>

                                        <TR><TD COLSPAN="2" ALIGN="CENTER" BGCOLOR="#1059A5" CLASS="menuBgColorHighlight"><IMG SRC="images/system/blank.gif" WIDTH="5" HEIGHT="1"></TD></TR>
                                        <TR>
                                            <TD HEIGHT="28" WIDTH="20%" ALIGN="CENTER"><IMG SRC="<c:url value="/ekms/"/>images/folder.gif" WIDTH="15" HEIGHT="17"></TD>
                                            <%
                                                pageContext.setAttribute("mails",new Integer(Util.countFolderUnreadMails(request,"Inbox")));
                                            %>
                                            <TD HEIGHT="28" WIDTH="80%"><a href="messageList.jsp?folder=Inbox"><FONT COLOR="#FFFFFF" CLASS="menuFont"><b><fmt:message key='messaging.label.inbox'/></b></FONT></a> <c:if test="${mails != -1}">(<c:out value="${mails}"/>)</c:if></TD>
                                        </TR>
                                        <TR><TD COLSPAN="2" ALIGN="CENTER" BGCOLOR="#000000" CLASS="menuBgColorShadow"><IMG SRC="<c:url value="/ekms/"/>images/blank.gif" WIDTH="5" HEIGHT="1"></TD></TR>
                                        <%--Draft--%>
                                        <TR><TD COLSPAN="2" ALIGN="CENTER" BGCOLOR="#1059A5" CLASS="menuBgColorHighlight"><IMG SRC="images/system/blank.gif" WIDTH="5" HEIGHT="1"></TD></TR>
                                        <TR>
                                            <TD HEIGHT="28" WIDTH="20%" ALIGN="CENTER"><IMG SRC="<c:url value="/ekms/"/>images/folder.gif" WIDTH="15" HEIGHT="17"></TD>
                                            <%
                                                pageContext.setAttribute("mails",new Integer(Util.countFolderUnreadMails(request,"Draft")));
                                            %>
                                            <TD HEIGHT="28" WIDTH="80%"><a href="messageList.jsp?folder=Draft"><FONT COLOR="#FFFFFF" CLASS="menuFont"><b><fmt:message key='messaging.label.draft'/></b></FONT></a> <c:if test="${mails != -1}">(<c:out value="${mails}"/>)</c:if></TD>
                                        </TR>
                                        <TR><TD COLSPAN="2" ALIGN="CENTER" BGCOLOR="#000000" CLASS="menuBgColorShadow"><IMG SRC="<c:url value="/ekms/"/>images/blank.gif" WIDTH="5" HEIGHT="1"></TD></TR>
                                        <%--Sent--%>
                                        <TR><TD COLSPAN="2" ALIGN="CENTER" BGCOLOR="#1059A5" CLASS="menuBgColorHighlight"><IMG SRC="images/system/blank.gif" WIDTH="5" HEIGHT="1"></TD></TR>
                                        <TR>
                                            <TD HEIGHT="28" WIDTH="20%" ALIGN="CENTER"><IMG SRC="<c:url value="/ekms/"/>images/folder.gif" WIDTH="15" HEIGHT="17"></TD>
                                            <%
                                                pageContext.setAttribute("mails",new Integer(Util.countFolderUnreadMails(request,"Sent")));
                                            %>
                                            <TD HEIGHT="28" WIDTH="80%"><a href="messageList.jsp?folder=Sent"><FONT COLOR="#FFFFFF" CLASS="menuFont"><b><fmt:message key='messaging.label.sent'/></b></FONT></a> <%--<c:if test="${mails != -1}">(<c:out value="${mails}"/>)</c:if>--%></TD>
                                        </TR>
                                        <TR><TD COLSPAN="2" ALIGN="CENTER" BGCOLOR="#000000" CLASS="menuBgColorShadow"><IMG SRC="<c:url value="/ekms/"/>images/blank.gif" WIDTH="5" HEIGHT="1"></TD></TR>
                                        <%--Quick Messages--%>
                                        <TR><TD COLSPAN="2" ALIGN="CENTER" BGCOLOR="#1059A5" CLASS="menuBgColorHighlight"><IMG SRC="images/system/blank.gif" WIDTH="5" HEIGHT="1"></TD></TR>
                                        <TR>
                                            <TD HEIGHT="28" WIDTH="20%" ALIGN="CENTER"><IMG SRC="<c:url value="/ekms/"/>images/folder.gif" WIDTH="15" HEIGHT="17"></TD>
                                            <%
                                                pageContext.setAttribute("mails",new Integer(Util.countFolderUnreadMails(request,"Quick Messages")));
                                            %>
                                            <TD HEIGHT="28" WIDTH="80%"><a href="messageList.jsp?folder=Quick%20Messages"><FONT COLOR="#FFFFFF" CLASS="menuFont"><b><fmt:message key='messaging.label.quickMessages'/></b></FONT></a> <c:if test="${mails != -1}">(<c:out value="${mails}"/>)</c:if></TD>
                                        </TR>
                                        <TR><TD COLSPAN="2" ALIGN="CENTER" BGCOLOR="#000000" CLASS="menuBgColorShadow"><IMG SRC="<c:url value="/ekms/"/>images/blank.gif" WIDTH="5" HEIGHT="1"></TD></TR>
                                        <%--Trash--%>
                                        <TR><TD COLSPAN="2" ALIGN="CENTER" BGCOLOR="#1059A5" CLASS="menuBgColorHighlight"><IMG SRC="images/system/blank.gif" WIDTH="5" HEIGHT="1"></TD></TR>
                                        <TR>
                                            <TD HEIGHT="28" WIDTH="20%" ALIGN="CENTER"><IMG SRC="<c:url value="/ekms/"/>images/folder.gif" WIDTH="15" HEIGHT="17"></TD>
                                            <%
                                                pageContext.setAttribute("mails",new Integer(Util.countFolderUnreadMails(request,"Trash")));
                                            %>
                                            <TD HEIGHT="28" WIDTH="80%"><a href="messageList.jsp?folder=Trash"><FONT COLOR="#FFFFFF" CLASS="menuFont"><b><fmt:message key='messaging.label.trash'/></b></FONT></a> <c:if test="${mails != -1}">(<c:out value="${mails}"/>)</c:if><FONT COLOR="#FFFFFF" CLASS="menuFont">[<a href="emptyTrash.jsp" onclick="if(confirm('Empty your trash folder?')) return true; else return false;"><FONT COLOR="#FFFFFF" CLASS="menuFont"><fmt:message key='messaging.label.empty'/></font></a>]</font></TD>
                                        </TR>
                                        <TR><TD COLSPAN="2" ALIGN="CENTER" BGCOLOR="#000000" CLASS="menuBgColorShadow"><IMG SRC="<c:url value="/ekms/"/>images/blank.gif" WIDTH="5" HEIGHT="1"></TD></TR>
                                        <%--Personal Folders--%>
                                        <c:forEach var="folder" items="${folders}">
                                        <c:set var="folderId" value="${folder.folderId}"/>
                                            <c:if test="${!folder.specialFolder}">
                                                <TR><TD COLSPAN="2" ALIGN="CENTER" BGCOLOR="#1059A5" CLASS="menuBgColorHighlight"><IMG SRC="images/system/blank.gif" WIDTH="5" HEIGHT="1"></TD></TR>
                                                <TR>
                                                <%
                                                    pageContext.setAttribute("mails",new Integer(Util.countFolderUnreadMailsByFolderId(request,(String)pageContext.getAttribute("folderId"))));
                                            %>

                                                    <TD HEIGHT="28" WIDTH="20%" ALIGN="CENTER"><IMG SRC="<c:url value="/ekms/"/>images/folder.gif" WIDTH="15" HEIGHT="17"></TD>
                                                    <TD HEIGHT="28" WIDTH="80%"><a href="messageList.jsp?folderId=<c:out value="${folder.folderId}" />"><FONT COLOR="#FFFFFF" CLASS="menuFont"><b><c:out value="${folder.name}" /></b></FONT></a> <c:if test="${mails != -1}">(<c:out value="${mails}"/>)</c:if></TD>
                                                </TR>
                                                <TR><TD COLSPAN="2" ALIGN="CENTER" BGCOLOR="#000000" CLASS="menuBgColorShadow"><IMG SRC="<c:url value="/ekms/"/>images/blank.gif" WIDTH="5" HEIGHT="1"></TD></TR>
                                            </c:if>

                                        </c:forEach>
                                        <TR><TD COLSPAN="2" ALIGN="CENTER" BGCOLOR="#1059A5" CLASS="menuBgColorHighlight"><IMG SRC="<c:url value="/ekms/"/>images/blank.gif" WIDTH="5" HEIGHT="1"></TD></TR>
                                        <tr><td colspan="3" height="28" align="center">&nbsp;</td></tr>
                                    </table>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
            </table>
        </td>
        <td><img src="<c:url value="/ekms/" />images/blank.gif" width="1" height="1"></td>
        <td valign="top">
            <table cellpadding="5" cellspacing="0" width="100%">
                <tr>
                    <td>
