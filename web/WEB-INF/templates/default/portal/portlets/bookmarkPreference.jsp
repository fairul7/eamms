<%@ page import="com.tms.portlet.portlets.bookmark.BookmarkPreference,
                 java.util.Map"%>
<%@include file="/common/header.jsp" %>
<c-rt:set var="event_delete" value="<%= BookmarkPreference.EVENT_TYPE_DELETE %>"/>
<c-rt:set var="delete_key" value="<%= BookmarkPreference.DELETE_KEY %>"/>
<table cellpadding="3" cellspacing="1" width="100%">
    <jsp:include page="../../form_header.jsp" flush="true"/>
    <tr><td class="bookmarkHeader" colspan="2"><fmt:message key='portlet.message.registeredBookmarkLinks'/></td></tr>
    <tr>
        <td class="bookmarkRow" colspan="2">
            <c:choose>
                <c:when test="${empty widget.links}">
                    <fmt:message key='portlet.message.noItemsFound'/>
                </c:when>
                <c:otherwise>
                    <table cellpadding="1" cellspacing="1" width="100%">
                        <c:forEach var="link" items="${widget.links}">
                            <tr>

                                <%
                                    Map.Entry link = (Map.Entry) pageContext.getAttribute("link");

                                    String sLink = link.getKey().toString().replaceAll("&","~");
                                    pageContext.setAttribute("sLink",sLink);

                                %>
                                <td class="bookmarkRow">[<x:event name="${widget.absoluteName}" type="${event_delete}" param="${delete_key}=${sLink}"><fmt:message key='portlet.message.delete'/></x:event>]</td>
                                <td class="bookmarkRow"><c:out value="${link.value}"/> (http://<c:out value="${link.key}"/>)</td>
                            </tr>
                        </c:forEach>
                    </table>
                </c:otherwise>
            </c:choose>
        </td>
    </tr>
    <tr><td class="bookmarkRow" colspan="2">&nbsp;</td></tr>
    <tr><td class="bookmarkHeader" colspan="2"><fmt:message key='portlet.message.newLink'/></td></tr>
    <tr>
        <td class="bookmarkRowLabel" align="right"><fmt:message key='portlet.message.label'/> </td>
        <td class="bookmarkRow"><x:display name="${widget.linkLabel.absoluteName}"/></td>
    </tr>
    <tr>
        <td class="bookmarkRowLabel" align="right"><fmt:message key='portlet.message.link'/> </td>
        <td class="bookmarkRow"><x:display name="${widget.link.absoluteName}"/></td>
    </tr>
    <tr>
        <td class="bookmarkRow" align="right">&nbsp;</td>
        <td class="bookmarkRow">
            <x:display name="${widget.submit.absoluteName}"/>
            <x:display name="${widget.cancel.absoluteName}"/>
        </td>
    </tr>
    <jsp:include page="../../form_footer.jsp" flush="true"/>
</table>
