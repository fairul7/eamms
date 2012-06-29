<%@ page import="com.tms.portlet.taglibs.PortalServerUtil"%>
<c-rt:set var="footer_property" value="<%= PortalServerUtil.PROPERTY_FOOTER %>"/>
<modules:portalserverutil name="portal.server" property="${footer_property}" var="ekmsFooter"/>
<c:if test="${!empty ekmsHeader}">
    <c:import url="${ekmsFooter}"/>
</c:if>
<%--

    <table width="100%" border="0" cellpadding="0" cellspacing="0">
        <tr bgcolor="#B5CFE7"><td colspan="3" class="titleMenuTop"><img src="<c:url value="/ekms/"/>images/blank.gif" width="5" height="2"></td></tr>
        <tr bgcolor="#639ACE">
            <td Valign="middle" class="titleMenu">
                <table border="0" cellspacing="0" cellpadding="0">
                    <tr align="CENTER">
                        <td nowrap><b>&nbsp;&nbsp;<a href="<c:url value="/ekms/"/>"><font color="#FFFFFF" class="titleMenuFont">My Dashboard</font></A></b></td>
                        <td><font color="#FFFFFF" class="titleMenuFont">&nbsp;&nbsp;|&nbsp;&nbsp;</font></td>
                        <td><b><a href="<c:url value="/ekms/"/>portalserver/personalize.jsp"><font color="#FFFFFF" class="titleMenuFont">Personalize</font></A></b></td>
                        <td><font color="#FFFFFF" class="titleMenuFont">&nbsp;&nbsp;</font></td>
                    </tr>
                </table>
            </td>            <td colspan="2" align="right" Valign="middle" class="titleMenu">
                <table border="0" cellspacing="0" cellpadding="0">
                    <tr align="CENTER">
                        <td><b><a href="<c:url value="/ekms/"/>content"><font color="#FFFFFF" class="titleMenuFont">Knowledge Base</font></A></b></td>
                        <td><font color="#FFFFFF" class="titleMenuFont">&nbsp;&nbsp;|&nbsp;&nbsp;</font></td>
                        <td><b><a href="<c:url value="/ekms/"/>messaging/index.jsp"><font color="#FFFFFF" class="titleMenuFont">Messaging</font></A></b></td>
                        <td><font color="#FFFFFF" class="titleMenuFont">&nbsp;&nbsp;|&nbsp;&nbsp;</font></td>
                        <td><b><a href="<c:url value="/ekms/"/>calendar/calendar.jsp"><font color="#FFFFFF"class="titleMenuFont">Calendaring</font></A></b></td>
                        <td><font color="#FFFFFF" class="titleMenuFont">&nbsp;&nbsp;|&nbsp;&nbsp;</font></td>
                        <td><b><a href="<c:url value="/ekms/"/>addressbook"><font   color="#FFFFFF" class="titleMenuFont">Address Book</font></A></b></td>
                        <td><font color="#FFFFFF" class="titleMenuFont">&nbsp;&nbsp;|&nbsp;&nbsp;</font></td>
                        <td><b><a href="<c:url value="/ekms/"/>forums/forums.jsp"><font color="#FFFFFF" class="titleMenuFont">Forums</font></A></b></td>
                        <td height="22">&nbsp;&nbsp;</td>
                    </tr>
                </table>
            </td>
        </tr>
        <tr bgcolor="#00557B"><td colspan="3" class="titleMenuBottom"><img src="<c:url value="/ekms/"/>images/blank.gif" width="5" height="3"></td></tr>
    </table>
</body>
</html>--%>
