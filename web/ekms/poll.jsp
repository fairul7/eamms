<%@ page import="kacang.runtime.filter.PresenceFilter"%>
<%@ include file="/common/header.jsp" %>
<c-rt:set var="kick" value="<%= PresenceFilter.ATTRIBUTE_KICK %>"/>
<c:if test="${! empty requestScope[kick]}">
    <c:redirect url="/ekms/kick.jsp"/>
</c:if>
<html>
<head>
  <meta http-equiv="Refresh" CONTENT="600">
</head>
</html>