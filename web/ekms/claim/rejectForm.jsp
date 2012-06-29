<%@ page import="com.tms.portlet.taglibs.PortalServerUtil"%>
<%@ include file="/common/header.jsp" %>

<x:config>
    <page name="jsp_rejectForm">
          <com.tms.hr.claim.ui.ClaimRejectForm name="form"/>
    </page>
</x:config>

<x:permission permission="com.tms.hr.claim.model.Access" module="com.tms.hr.claim.model.ClaimConfigModule" url="noPermission.jsp">

<c:if test="${!empty param.id}">
    <x:set name="jsp_rejectForm.form" property="id" value="${param.id}"/>
</c:if>
<c:if test="${!empty param.apptype}">
    <x:set name="jsp_rejectForm.form" property="approveType" value="${param.apptype}"/>
    <c:set var="apptype" value="${param.apptype}"/>
</c:if>


<c:if test="${forward.name=='approverRejectSuccess'}">
    <script>
        alert("Successfully rejected the expenses");
        window.opener.location="approver_approve.jsp";
        window.close();
    </script>
</c:if>
<c:if test="${forward.name=='assessorRejectSuccess'}">
    <script>
        alert("Successfully rejected the expenses");
        window.opener.location="approver_process.jsp";
        window.close();
    </script>
</c:if>
<c:if test="${forward.name=='rejectFail'}">
    <script>
        alert("Fail to reject the expenses.");
        window.close();
    </script>
</c:if>
<c:if test="${forward.name=='cancel'}">
    <script>
        window.close();
    </script>
</c:if>

<jsp:include page="/ekms/init.jsp"/>
<c-rt:set var="stylesheet" value="<%= PortalServerUtil.PROPERTY_STYLESHEET %>"/>
<modules:portalserverutil name="portal.server" property="${stylesheet}" var="ekmsCSS"/>
<link rel="stylesheet" href="<c:out value="${ekmsCSS}"/>">

<html>
<head>

</head>
<body>
<table width="100%" border="0" cellspacing="0" cellpadding="5">
    <tr valign="middle">
        <td height="22" bgcolor="#003366" class="contentTitleFont">
        <b><font color="#FFCF63" class="contentTitleFont">Reject Expenses</font></b>
        </td>
        <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
    </tr>
    <tr>
        <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">
        <img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10">
        </td>
    </tr>
    <tr>
        <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">
            <x:display name="jsp_rejectForm.form"/>
        </td>
    </tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"></td></tr>
</table>

    </x:permission>

</body>
</html>