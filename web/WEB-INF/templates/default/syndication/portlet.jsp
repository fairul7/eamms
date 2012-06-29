<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>


<c:set var="form" value="${widget}"/>


<jsp:include page="../form_header.jsp" flush="true"/>


<table width="100%">
    <tr><td align="right">
        <x:display name="${form.btnUpdate.absoluteName}" />
    </td></tr>
    <c:choose>
        <c:when test="${empty form.itemsList}">
            <tr><td><fmt:message key='syndication.label.no.item' /></td></tr>
        </c:when>
        <c:otherwise>
            <c:set var="channelName" value=""/>

            <ul>
                <c:forEach var="rssItem" items="${form.itemsList}" varStatus="status">
                    <c:if test="${channelName != rssItem.channelName}">

<c:set var="currentCount" value="1" />

                        <c:if test="${status.count > 1}">
                            <tr><td><hr></td></tr>
                        </c:if>
                        <tr><td align="center">
                        <c:if test="${!empty rssItem.imageLink}"><img align="absbottom" src="<c:out value="${rssItem.imageLink}" />"> - </c:if><b><c:out value="${rssItem.channelName}" /></b></td></tr>
                    </c:if>                    

<c:if test="${currentCount lt 6}">
<c:set var="currentCount" value="${currentCount+1}" />
                    <tr><td align="left"><li><a href="<c:out value="${rssItem.link}" />" title="<c:out value="${rssItem.summary}" />" style="cursor:hand" target="_blank"><c:out value="${rssItem.title}" /></a>
                    <c:if test="${!empty rssItem.pubDate}"> - <c:out value="${rssItem.pubDate}" /> </c:if>
                    </li></td></tr>
                    <c:set var="channelName" value="${rssItem.channelName}"/>
</c:if>

                </c:forEach>
            </ul>



        </c:otherwise>
    </c:choose>
</table>

<jsp:include page="../form_footer.jsp" flush="true"/>


