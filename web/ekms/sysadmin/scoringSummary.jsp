<%@ include file="/common/header.jsp" %>
<html>
<body>
<x:cache key="<%= "scoringCache_" + request.getParameter("scoringSummaryMonth") + request.getParameter("scoringSummaryYear") %>" scope="application" time="300">
<x:template type="com.tms.ekms.statistics.ui.ScoringSummary"/>
<c-rt:set var="currentDate" value="<%= new java.util.Date() %>"/>
<fmt:message key="sfa.message.lastUpdated"/>: <fmt:formatDate pattern="${globalDatetimeLong}" value="${currentDate}"/>
</x:cache>
</body>
</html>
