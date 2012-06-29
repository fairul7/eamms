<%@ page import="com.tms.portlet.taglibs.PortalServerUtil"%>
 <%--
  Created by IntelliJ IDEA.
  User: oilai
  Date: Apr 28, 2005
  Time: 9:32:23 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ include file="/common/header.jsp" %>

<x:config>
    <page name="tsUserViewPage">
        <com.tms.collab.timesheet.ui.TimeSheetUserView name="tsUserView"/>
    </page>
</x:config>

<c:if test="${!empty param.userid}">
    <x:set name="tsUserViewPage.tsUserView" property="userid" value="${param.userid}"/>
</c:if>

<jsp:include page="/ekms/init.jsp"/>
<c-rt:set var="stylesheet" value="<%= PortalServerUtil.PROPERTY_STYLESHEET %>"/>
<modules:portalserverutil name="portal.server" property="${stylesheet}" var="ekmsCSS"/>
<link rel="stylesheet" href="<c:out value="${ekmsCSS}"/>">

<!-- display page -->
<%@include file="/ekms/includes/header.jsp" %>
<x:display name="tsUserViewPage.tsUserView"></x:display>
<%@include file="/ekms/includes/footer.jsp" %>