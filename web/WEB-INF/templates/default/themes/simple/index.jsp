<%@ page import="com.tms.portlet.theme.themes.DefaultTheme,
                 com.tms.portlet.Entity,
                 java.io.PrintWriter"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="crt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>
<c:set var="themeManager" value="${widget}" scope="request"/>
<script language="javascript" src="<c:url value="/ekms/includes/portlet.js" />"></script>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr>
        <td width="24%" valign="top">
            <%-- Announcements Spot --%>
            <table width="100%" border="0" cellspacing="3" cellpadding="0">
                <tr valign="top">
                    <td>
                    <table width="100%" border="0" cellspacing="0" cellpadding="0">
                        <tr valign="middle">
                            <td height="22" bgcolor="003366" class="contentTitleFont" nowrap>
                                <b><font color="#FFCF63" class="contentTitleFont">&nbsp;Announcements</font></b>
                            </td>
                            <td align="right" bgcolor="003366" class="contentTitleFont"></td>
                        </tr>
                        <tr>
                            <td colspan="2" valign="TOP" bgcolor="EFEFEF" class="contentBgColor">
                                <table cellpadding="3" cellspacing="0" width="100%">
                                    <tr><td bgcolor="EFEFEF" class="contentBgColor"><img src="images/blank.gif" width="5" height="7"></td></tr>
                                    <tr>
                                        <td bgcolor="EFEFEF" class="contentBgColor">
                                            <x:template type="TemplateDisplaySpot" properties="id=com.tms.cms.spot.Spot_DesktopAnnouncement" />
                                        </td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                    </table>
                    </td>
                </tr>
            </table>

            <crt:set var="leftColumn" value="<%= DefaultTheme.COLUMN_LEFT_LABEL %>"/>
            <c:forEach items="${requestScope.themeManager.portlets[leftColumn]}" var="item" varStatus="status">
                <c:set var="columnStatus" value="${status}" scope="request"/>
                <c:set var="entity" value="${item}" scope="request"/>
                <table width="100%" border="0" cellspacing="3" cellpadding="0">
                    <tr align="center" valign="top">
                        <td>
                            <crt:set var="path" value="<%= DefaultTheme.DEFAULT_PORTLET_ROOT %>" />
                            <c:set var="path" value="${path}${item.portlet.portletClass}"/>
                            <jsp:include page="<%= DefaultTheme.DEFAULT_PORTLET_HEADER %>"/>
                            <% try { %>
                            <jsp:include page="<%= (String) pageContext.getAttribute(\"path\") %>"/>
                            <% } catch (Exception e) { out.print((String) "!!! " + pageContext.getAttribute("path")); } %>
                            <jsp:include page="<%= DefaultTheme.DEFAULT_PORTLET_FOOTER %>"/>
                        </td>
                    </tr>
                </table>
                <c:set var="currentCount" value="${requestScope.currentCount+1}" scope="request" />
                <br>
            </c:forEach>
        </td>
        <td width="1%">&nbsp;</td>
        <td width="50%" valign="TOP">
            <crt:set var="centerColumn" value="<%= DefaultTheme.COLUMN_CENTER_LABEL %>"/>
            <c:forEach items="${requestScope.themeManager.portlets[centerColumn]}" var="item" varStatus="status" >
                <c:set var="columnStatus" value="${status}" scope="request"/>
                <c:set var="entity" value="${item}" scope="request"/>
                <table width="100%" border="0" cellspacing="3" cellpadding="0">
                    <tr valign="TOP">
                        <td>
                            <crt:set var="path" value="<%= DefaultTheme.DEFAULT_PORTLET_ROOT %>" />
                            <c:set var="path" value="${path}${item.portlet.portletClass}"/>
                            <jsp:include page="<%= DefaultTheme.DEFAULT_PORTLET_HEADER %>"/>
                            <% try { %>
                            <jsp:include page="<%= (String) pageContext.getAttribute(\"path\") %>"/>
                            <% } catch (Exception e) { out.print((String) "!!! " + pageContext.getAttribute("path")); } %>
                            <jsp:include page="<%= DefaultTheme.DEFAULT_PORTLET_FOOTER %>"/>
                        </td>
                    </tr>
                </table>
                <c:set var="currentCount" value="${requestScope.currentCount+1}" scope="request" />
                <br>
            </c:forEach>
        </td>
        <td width="1%">&nbsp;</td>
        <td width="24%" valign="TOP">
            <crt:set var="rightColumn" value="<%= DefaultTheme.COLUMN_RIGHT_LABEL %>"/>
            <c:forEach items="${requestScope.themeManager.portlets[rightColumn]}" var="item" varStatus="status" >
                <c:set var="columnStatus" value="${status}" scope="request"/>
                <c:set var="entity" value="${item}" scope="request"/>
                <table width="100%" border="0" cellspacing="3" cellpadding="0">
                    <tr valign="TOP">
                        <td>
                            <crt:set var="path" value="<%= DefaultTheme.DEFAULT_PORTLET_ROOT %>" />
                            <c:set var="path" value="${path}${item.portlet.portletClass}"/>
                            <jsp:include page="<%= DefaultTheme.DEFAULT_PORTLET_HEADER %>"/>
                            <% try { %>
                            <jsp:include page="<%= (String) pageContext.getAttribute(\"path\") %>"/>
                            <% } catch (Exception e) { out.print((String) "!!! " + pageContext.getAttribute("path")); } %>
                            <jsp:include page="<%= DefaultTheme.DEFAULT_PORTLET_FOOTER %>"/>
                        </td>
                    </tr>
                </table>
                <c:set var="currentCount" value="${requestScope.currentCount+1}" scope="request" />
                <br>
            </c:forEach>
        </td>
    </tr>
</table>