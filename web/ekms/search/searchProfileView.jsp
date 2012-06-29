<%@ page import="com.tms.ekms.search.ui.SearchProfileDisplay,
                 com.tms.ekms.search.ui.SearchWidget,
                 kacang.services.indexing.SearchableModule,
                 java.util.StringTokenizer"%>
<%@ include file="/common/header.jsp" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%-- Event Handling --%>
<c-rt:set var="searchEvent" value="<%= SearchWidget.EVENT_SEARCH_RESULT %>" />
<c-rt:set var="searchKey" value="<%= SearchableModule.SEARCH_PROPERTY_KEY %>" />
<c-rt:set var="searchModule" value="<%= SearchableModule.SEARCH_PROPERTY_MODULE_CLASS %>" />
<c-rt:set var="searchObject" value="<%= SearchableModule.SEARCH_PROPERTY_OBJECT_CLASS %>" />
<c:if test="${param.et == searchEvent}">
    <c:choose>
        <c:when test="${param[searchModule] == 'com.tms.collab.directory.model.SecuritySearchModule'}">
            <c:redirect url="/ekms/addressbook/udContactList.jsp?id=${param[searchKey]}" />
        </c:when>
        <c:when test="${param[searchModule] == 'com.tms.collab.directory.model.AddressBookModule'}">
            <c:redirect url="/ekms/addressbook/abViewContact.jsp?id=${param[searchKey]}" />
        </c:when>
        <c:when test="${param[searchModule] == 'com.tms.collab.directory.model.DirectoryModule'}">
            <c:redirect url="/ekms/addressbook/bdViewContact.jsp?id=${param[searchKey]}" />
        </c:when>
        <c:when test="${param[searchModule] == 'com.tms.collab.directory.model.CompanyModule'}">
            <c:redirect url="/ekms/addressbook/bdViewCompany.jsp?id=${param[searchKey]}" />
        </c:when>
        <c:when test="${param[searchModule] == 'com.tms.collab.messaging.model.MessagingModule'}">
            <c:redirect url="/ekms/messaging/readMessage.jsp?messageId=${param[searchKey]}" />
        </c:when>
        <c:when test="${param[searchModule] == 'com.tms.collab.calendar.model.CalendarModule'}">
            <c:redirect url="/ekms/calendar/calendar.jsp?cn=calendarPage.calendarView&et=select&eventId=${param[searchKey]}" />
        </c:when>
        <c:when test="${param[searchModule] == 'com.tms.collab.forum.model.ForumModule'}">
            <c:choose>
                <c:when test="${param[searchObject] == 'com.tms.collab.forum.model.Forum'}">
                    <c:redirect url="/ekms/forums/forumTopicList.jsp?forumId=${param[searchKey]}" />
                </c:when>
                <c:when test="${param[searchObject] == 'com.tms.collab.forum.model.Thread'}">
                    <c:redirect url="/ekms/forums/forumMessageList.jsp?threadId=${param[searchKey]}" />
                </c:when>
                <c:when test="${param[searchObject] == 'com.tms.collab.forum.model.Message'}">
                    <%
                        StringTokenizer tokenizer = new StringTokenizer(request.getParameter(SearchableModule.SEARCH_PROPERTY_KEY), ",");
                        String threadId = "";
                        String messageId = "";
                        while(tokenizer.hasMoreTokens())
                        {
                            if("".equals(threadId))
                                threadId = tokenizer.nextToken();
                            else
                                messageId = tokenizer.nextToken();
                        }
                        pageContext.setAttribute("threadId", threadId);
                        pageContext.setAttribute("messageId", messageId);
                    %>
                    <c:redirect url="/ekms/forums/forumMessageList.jsp?threadId=${threadId}&messageId=${messageId}" />
                </c:when>
            </c:choose>
        </c:when>
        <c:when test="${param[searchModule] == 'com.tms.cms.article.ArticleModule'}">
            <c:redirect url="/ekms/content/content.jsp?id=${param[searchKey]}" />
        </c:when>
        <c:when test="${param[searchModule] == 'com.tms.cms.document.DocumentModule'}">
            <c:redirect url="/ekms/content/content.jsp?id=${param[searchKey]}" />
        </c:when>
    </c:choose>
</c:if>
<%-- END: Event Handling --%>

<%@ include file="/ekms/includes/header.jsp" %>
<x:template type="com.tms.ekms.search.ui.SearchProfileDisplay" properties="profileId=${param.profileId}" />
<%@ include file="/ekms/includes/footer.jsp" %>

