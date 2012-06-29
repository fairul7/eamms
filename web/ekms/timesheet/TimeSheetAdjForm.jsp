<%@ page import="com.tms.portlet.taglibs.PortalServerUtil"%>
 <%--
  Created by IntelliJ IDEA.
  User: oilai
  Date: Apr 26, 2005
  Time: 11:44:08 AM
  To change this template use File | Settings | File Templates.
--%>
<%@include file="/common/header.jsp"%>

<x:config>
    <page  name="tsAdjPage">
        <com.tms.collab.timesheet.ui.TimeSheetAdjForm name="tsAdjForm"/>
    </page>
</x:config>

<c:if test="${forward.name=='updated'}">
    <script>
        alert('<fmt:message key="timesheet.message.added"/>');
        window.opener.location.reload();
        window.close();
    </script>
</c:if>
<c:if test="${forward.name=='fail'}">
    <script>
        alert('<fmt:message key="timesheet.message.adjustmentfail"/>');
        window.close();
    </script>
</c:if>
<c:if test="${forward.name=='cancel_form_action'}">
    <script>
        window.close();
    </script>
</c:if>
<c:if test="${forward.name=='select'}">
    <script>
        alert('<fmt:message key="timesheet.message.selectduration"/>');
    </script>
</c:if>

<c:if test="${!empty param.id}" >
    <x:set name="tsAdjPage.tsAdjForm" property="id" value="${param.id}" />
</c:if>


<jsp:include page="/ekms/init.jsp"/>
<c-rt:set var="stylesheet" value="<%= PortalServerUtil.PROPERTY_STYLESHEET %>"/>
<modules:portalserverutil name="portal.server" property="${stylesheet}" var="ekmsCSS"/>
<link rel="stylesheet" href="<c:out value="${ekmsCSS}"/>">

<html>
<head>
</head>

<body>
<table cellpadding="4" cellspacing="1" width="100%" border="0">
<tr>
    <td class="calendarHeader"><fmt:message key="timesheet.label.adjform"/> </td>
</tr>
<tr>
    <td><x:display name="tsAdjPage.tsAdjForm" /> </td>
</tr>
</table>
</body>

</html>