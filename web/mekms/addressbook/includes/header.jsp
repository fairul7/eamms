<%@ page import="com.tms.collab.messaging.model.Util,
                 com.tms.collab.directory.model.DirectoryModule"%>
<%@ include file="/ekms/includes/header.jsp" %>
<table width="100%" border="0" cellspacing="0" cellpadding="1">
    <tr valign="top">
        <td width="20%" nowrap>
            <table width="100%" border="0" cellspacing="0" cellpadding="1" class="menuBgOutline">
                <tr>
                    <td>
                        <table width="100%" border="0" cellpadding="1" cellspacing="0" class="menuBgBackground">
                            <tr>
                                <td>
                                    <table width="100%" border="0" cellpadding="0" cellspacing="0" class="menuBgColor">
                                        <%-- Business Directory Heading --%>
                                        <tr>
                                            <td colspan="3" valign="middle" bgcolor="666666" class="menuHeader" height="25">
                                                <table cellpadding="0" cellspacing="0" width="100%">
                                                    <tr>
                                                        <td class="menuHeader" width="5%"><img src="<c:url value="/ekms/images/system/blank.gif"/>" width="1" height="1"></td>
                                                        <td class="menuHeader" ><fmt:message key='addressbook.label.menu.businessDirectory'/></td>
                                                        <td class="menuHeader" width="18"><img src="<c:url value="/ekms/images/arrowdown.gif"/>"></td>
                                                    </tr>
                                                </table>
                                            </td>
                                        </tr>
                                        <%-- contacts --%>
                                        <TR><TD COLSPAN="2" ALIGN="CENTER" BGCOLOR="#1059A5" CLASS="menuBgColorHighlight"><IMG SRC="images/system/blank.gif" WIDTH="5" HEIGHT="1"></TD></TR>
                                        <TR>
                                            <TD HEIGHT="28" WIDTH="20%" ALIGN="CENTER"><IMG SRC="<c:url value="/ekms/images/folder.gif"/>" WIDTH="15" HEIGHT="17"></TD>
                                            <TD HEIGHT="28" WIDTH="80%"><a href="bdContactList.jsp?cn=bdFolderTree.tree&id=&companyId="><FONT COLOR="#FFFFFF" CLASS="menuFont"><b><fmt:message key='addressbook.label.menu.contacts'/></b></FONT></a></TD>
                                        </TR>
                                        <TR>
                                            <TD colspan="2"><img src="<c:url value="/ekms/images/blank.gif"/>" align="left" width="15" height="0" border="0">
                                                <jsp:include page="/ekms/addressbook/includes/bdFolderTree.jsp" flush="true" />
                                            </TD>
                                        </TR>
                                        <TR><TD COLSPAN="2" ALIGN="CENTER" BGCOLOR="#000000" CLASS="menuBgColorShadow"><IMG SRC="<c:url value="/ekms/images/blank.gif"/>" WIDTH="5" HEIGHT="1"></TD></TR>
                                        <%-- companies --%>
                                        <TR><TD COLSPAN="2" ALIGN="CENTER" BGCOLOR="#1059A5" CLASS="menuBgColorHighlight"><IMG SRC="images/system/blank.gif" WIDTH="5" HEIGHT="1"></TD></TR>
                                        <TR>
                                            <TD HEIGHT="28" WIDTH="20%" ALIGN="CENTER"><IMG SRC="<c:url value="/ekms/"/>images/folder.gif" WIDTH="15" HEIGHT="17"></TD>
                                            <TD HEIGHT="28" WIDTH="80%" nowrap><a href="bdCompanyList.jsp"><FONT COLOR="#FFFFFF" CLASS="menuFont"><b><fmt:message key='addressbook.label.menu.registeredCompanies'/></b></FONT></a></TD>
                                        </TR>
                                        <TR><TD COLSPAN="2" ALIGN="CENTER" BGCOLOR="#000000" CLASS="menuBgColorShadow"><IMG SRC="<c:url value="/ekms/images/blank.gif"/>" WIDTH="5" HEIGHT="1"></TD></TR>
                                        <TR><TD COLSPAN="2" ALIGN="CENTER" BGCOLOR="#1059A5" CLASS="menuBgColorHighlight"><IMG SRC="<c:url value="/ekms/images/blank.gif"/>" WIDTH="5" HEIGHT="1"></TD></TR>
                                        <%-- pending contacts and companies --%>
                                        <x:template name="count" type="com.tms.collab.directory.ui.DirectoryPendingCount" body="custom">
                                            <x:permission module="<%= DirectoryModule.class.getName() %>" permission="<%= DirectoryModule.PERMISSION_MANAGE_CONTACTS %>">
                                                <TR>
                                                    <TD HEIGHT="28" WIDTH="20%" ALIGN="CENTER"><IMG SRC="<c:url value="/ekms/"/>images/folder.gif" WIDTH="15" HEIGHT="17"></TD>
                                                    <TD HEIGHT="28" WIDTH="80%" nowrap>
                                                        <a href="bdContactApprovalList.jsp"><FONT COLOR="#FFFFFF" CLASS="menuFont"><b><fmt:message key='addressbook.label.menu.pendingContacts'/></b></FONT></a>
                                                        (<c:out value="${count.pendingContactCount}"/>)
                                                    </TD>
                                                </TR>
                                                <TR><TD COLSPAN="2" ALIGN="CENTER" BGCOLOR="#000000" CLASS="menuBgColorShadow"><IMG SRC="<c:url value="/ekms/images/blank.gif"/>" WIDTH="5" HEIGHT="1"></TD></TR>
                                                <TR><TD COLSPAN="2" ALIGN="CENTER" BGCOLOR="#1059A5" CLASS="menuBgColorHighlight"><IMG SRC="<c:url value="/ekms/images/blank.gif"/>" WIDTH="5" HEIGHT="1"></TD></TR>
                                            </x:permission>
                                            <x:permission module="<%= DirectoryModule.class.getName() %>" permission="<%= DirectoryModule.PERMISSION_MANAGE_COMPANIES %>">
                                                <TR>
                                                    <TD HEIGHT="28" WIDTH="20%" ALIGN="CENTER"><IMG SRC="<c:url value="/ekms/"/>images/folder.gif" WIDTH="15" HEIGHT="17"></TD>
                                                    <TD HEIGHT="28" WIDTH="80%" nowrap>
                                                        <a href="bdCompanyApprovalList.jsp"><FONT COLOR="#FFFFFF" CLASS="menuFont"><b><fmt:message key='addressbook.label.menu.pendingCompanies'/></b></FONT></a>
                                                        (<c:out value="${count.pendingCompanyCount}"/>)
                                                    </TD>
                                                </TR>
                                                <TR><TD COLSPAN="2" ALIGN="CENTER" BGCOLOR="#000000" CLASS="menuBgColorShadow"><IMG SRC="<c:url value="/ekms/images/blank.gif"/>" WIDTH="5" HEIGHT="1"></TD></TR>
                                                <TR><TD COLSPAN="2" ALIGN="CENTER" BGCOLOR="#1059A5" CLASS="menuBgColorHighlight"><IMG SRC="<c:url value="/ekms/images/blank.gif"/>" WIDTH="5" HEIGHT="1"></TD></TR>
                                            </x:permission>
                                        </x:template>
                                        <%-- folders --%>
                                        <x:permission module="<%= DirectoryModule.class.getName() %>" permission="<%= DirectoryModule.PERMISSION_MANAGE_FOLDERS %>">
                                            <TR>
                                                <TD HEIGHT="28" WIDTH="20%" ALIGN="CENTER"><IMG SRC="<c:url value="/ekms/"/>images/folder.gif" WIDTH="15" HEIGHT="17"></TD>
                                                <TD HEIGHT="28" WIDTH="80%" nowrap><a href="bdFolderList.jsp"><FONT COLOR="#FFFFFF" CLASS="menuFont"><b><fmt:message key='addressbook.label.menu.folders'/></b></FONT></a></TD>
                                            </TR>
                                            <TR><TD COLSPAN="2" ALIGN="CENTER" BGCOLOR="#000000" CLASS="menuBgColorShadow"><IMG SRC="<c:url value="/ekms/images/blank.gif"/>" WIDTH="5" HEIGHT="1"></TD></TR>
                                            <TR><TD COLSPAN="2" ALIGN="CENTER" BGCOLOR="#1059A5" CLASS="menuBgColorHighlight"><IMG SRC="<c:url value="/ekms/images/blank.gif"/>" WIDTH="5" HEIGHT="1"></TD></TR>
                                        </x:permission>
                                        <%-- options --%>
                                        <x:permission module="<%= DirectoryModule.class.getName() %>" permission="<%= DirectoryModule.PERMISSION_MANAGE_CONTACTS %>">
                                            <TR>
                                                <TD HEIGHT="28" WIDTH="20%" ALIGN="CENTER"><IMG SRC="<c:url value="/ekms/images/folder.gif"/>" WIDTH="15" HEIGHT="17"></TD>
                                                <TD HEIGHT="28" WIDTH="80%" nowrap>
                                                    <a href="bdOptions.jsp"><FONT COLOR="#FFFFFF" CLASS="menuFont"><b><fmt:message key='addressbook.label.menu.options'/></b></FONT></a>
                                                </TD>
                                            </TR>
                                        </x:permission>
                                        <%-- Intranet Users Heading --%>
                                        <TR><TD COLSPAN="2" ALIGN="CENTER" BGCOLOR="#1059A5" CLASS="menuBgColorHighlight"><IMG SRC="images/system/blank.gif" WIDTH="5" HEIGHT="1"></TD></TR>
                                        <tr>
                                            <td colspan="3" valign="middle" bgcolor="666666" class="menuHeader" height="25">
                                                <table cellpadding="0" cellspacing="0" width="100%">
                                                    <tr>
                                                        <td class="menuHeader" width="5%"><img src="<c:url value="/ekms/images/system/blank.gif"/>" width="1" height="1"></td>
                                                        <td class="menuHeader" ><fmt:message key='addressbook.label.menu.intranetUsers'/></td>
                                                        <td class="menuHeader" width="18"><img src="<c:url value="/ekms/images/arrowdown.gif"/>"></td>
                                                    </tr>
                                                </table>
                                            </td>
                                        </tr>
                                        <%-- users --%>
                                        <TR>
                                            <TD HEIGHT="28" WIDTH="20%" ALIGN="CENTER"><IMG SRC="<c:url value="/ekms/images/folder.gif"/>" WIDTH="15" HEIGHT="17"></TD>
                                            <TD HEIGHT="28" WIDTH="80%">
                                                <a href="udContactList.jsp"><FONT COLOR="#FFFFFF" CLASS="menuFont"><b><fmt:message key='addressbook.label.menu.users'/></b></FONT></a>
                                            </TD>
                                        </TR>
                                        <TR><TD COLSPAN="2" ALIGN="CENTER" BGCOLOR="#1059A5" CLASS="menuBgColorHighlight"><IMG SRC="images/system/blank.gif" WIDTH="5" HEIGHT="1"></TD></TR>
                                    <%-- Personal Address Book Heading --%>
                                        <tr>
                                            <td colspan="3" valign="middle" bgcolor="666666" class="menuHeader" height="25">
                                                <table cellpadding="0" cellspacing="0" width="100%">
                                                    <tr>
                                                        <td class="menuHeader" width="5%"><img src="<c:url value="/ekms/images/system/blank.gif"/>" width="1" height="1"></td>
                                                        <td class="menuHeader" ><fmt:message key='addressbook.label.menu.personalContacts'/></td>
                                                        <td class="menuHeader" width="18"><img src="<c:url value="/ekms/images/arrowdown.gif"/>"></td>
                                                    </tr>
                                                </table>
                                            </td>
                                        </tr>
                                        <%-- contacts --%>
                                        <TR><TD COLSPAN="2" ALIGN="CENTER" BGCOLOR="#1059A5" CLASS="menuBgColorHighlight"><IMG SRC="images/system/blank.gif" WIDTH="5" HEIGHT="1"></TD></TR>
                                        <TR>
                                            <TD HEIGHT="28" WIDTH="20%" ALIGN="CENTER"><IMG SRC="<c:url value="/ekms/images/folder.gif"/>" WIDTH="15" HEIGHT="17"></TD>
                                            <TD HEIGHT="28" WIDTH="80%">
                                                <a href="abContactList.jsp?cn=abFolderTree.tree&id="><FONT COLOR="#FFFFFF" CLASS="menuFont"><b><fmt:message key='addressbook.label.menu.contacts'/></b></FONT></a>
                                            </TD>
                                        </TR>
                                        <TR>
                                            <TD colspan="2"><img src="<c:url value="/ekms/images/blank.gif"/>" align="left" width="15" height="0" border="0">
                                                <jsp:include page="/ekms/addressbook/includes/abFolderTree.jsp" flush="true" />
                                            </TD>
                                        </TR>
                                        <TR><TD COLSPAN="2" ALIGN="CENTER" BGCOLOR="#000000" CLASS="menuBgColorShadow"><IMG SRC="<c:url value="/ekms/images/blank.gif"/>" WIDTH="5" HEIGHT="1"></TD></TR>
                                        <%-- folders --%>
                                        <TR><TD COLSPAN="2" ALIGN="CENTER" BGCOLOR="#1059A5" CLASS="menuBgColorHighlight"><IMG SRC="images/system/blank.gif" WIDTH="5" HEIGHT="1"></TD></TR>
                                        <TR>
                                            <TD HEIGHT="28" WIDTH="20%" ALIGN="CENTER"><IMG SRC="<c:url value="/ekms/images/folder.gif"/>" WIDTH="15" HEIGHT="17"></TD>
                                            <TD HEIGHT="28" WIDTH="80%" nowrap>
                                                <a href="abFolderList.jsp"><FONT COLOR="#FFFFFF" CLASS="menuFont"><b><fmt:message key='addressbook.label.menu.folders'/></b></FONT></a>
                                            </TD>
                                        </TR>
                                        <TR><TD COLSPAN="2" ALIGN="CENTER" BGCOLOR="#000000" CLASS="menuBgColorShadow"><IMG SRC="<c:url value="/ekms/images/blank.gif"/>" WIDTH="5" HEIGHT="1"></TD></TR>
                                        <%-- options --%>
                                        <TR><TD COLSPAN="2" ALIGN="CENTER" BGCOLOR="#1059A5" CLASS="menuBgColorHighlight"><IMG SRC="images/system/blank.gif" WIDTH="5" HEIGHT="1"></TD></TR>
                                        <TR>
                                            <TD HEIGHT="28" WIDTH="20%" ALIGN="CENTER"><IMG SRC="<c:url value="/ekms/images/folder.gif"/>" WIDTH="15" HEIGHT="17"></TD>
                                            <TD HEIGHT="28" WIDTH="80%" nowrap>
                                                <a href="abOptions.jsp"><FONT COLOR="#FFFFFF" CLASS="menuFont"><b><fmt:message key='addressbook.label.menu.options'/></b></FONT></a>
                                            </TD>
                                        </TR>
                                        <TR><TD COLSPAN="2" ALIGN="CENTER" BGCOLOR="#000000" CLASS="menuBgColorShadow"><IMG SRC="<c:url value="/ekms/images/blank.gif"/>" WIDTH="5" HEIGHT="1"></TD></TR>
                                        <TR><TD COLSPAN="2" ALIGN="CENTER" BGCOLOR="#1059A5" CLASS="menuBgColorHighlight"><IMG SRC="<c:url value="/ekms/images/blank.gif"/>" WIDTH="5" HEIGHT="1"></TD></TR>
                                        <tr><td colspan="3" height="28" align="center">&nbsp;</td></tr>
                                    </table>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
            </table>
        </td>
        <td><img src="<c:url value="/ekms/images/blank.gif"/>" width="1" height="1"></td>
        <td width="80%" valign="top">
            <table cellpadding="5" cellspacing="0" width="100%">
                <tr>
                    <td>
