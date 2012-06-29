<%@ page import="com.tms.portlet.taglibs.PortalServerUtil"%>
 <%--
  Created by IntelliJ IDEA.
  User: oilai
  Date: Apr 26, 2005
  Time: 10:52:23 AM
  To change this template use File | Settings | File Templates.
--%>
<%@include file="/common/header.jsp"%>

<x:config>
    <page  name="tsDetPage">
        <com.tms.collab.timesheet.ui.TimeSheetDet name="tsDet"/>
    </page>
</x:config>

<c:if test="${forward.name=='edit'}">
    <c:redirect url="TimeSheetEdit.jsp?id=${param.id}" />
</c:if>
<c:if test="${forward.name=='adjust'}">
    <c:redirect url="TimeSheetAdjForm.jsp?id=${param.id}" />
</c:if>
<c:if test="${forward.name=='close'}">
    <script>
        window.close();
    </script>
</c:if>

<c:if test="${!empty param.id}" >
    <x:set name="tsDetPage.tsDet" property="id" value="${param.id}" />
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
    <td><x:display name="tsDetPage.tsDet" /> </td>
</tr>
</table>
</body>

</html>