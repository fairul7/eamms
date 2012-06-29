<%@ page import="com.tms.portlet.taglibs.PortalServerUtil,
                 com.tms.fms.facility.ui.UserCompetencyAdd"%><html>
<%@ include file="/common/header.jsp" %>
<x:config>
    <page name="wormsAddUserCompetency">
        <com.tms.fms.facility.ui.UserCompetencyAdd name="form"/>
    </page>
</x:config>
<c-rt:set var="forward_success" value="<%= UserCompetencyAdd.FORWARD_SUCCESSFUL %>"/>
<c-rt:set var="forward_cancel" value="<%= UserCompetencyAdd.FORWARD_CANCEL %>"/>

<c:if test="${forward.name == 'forwardToFMS'}">
    <script>
        alert("<fmt:message key='project.message.competencyAdded'/>");
        window.opener.location="<c:url value="manpowerAdd.jsp"/>";
        window.close();
    </script>
</c:if>
<c:if test="${forward.name == forward_cancel}">
    <script>window.close();</script>
</c:if>
<head>
    <title><fmt:message key='project.label.addingUserPersonalization'/></title>
    <c-rt:set var="stylesheet" value="<%= PortalServerUtil.PROPERTY_STYLESHEET %>"/>
    <modules:portalserverutil name="portal.server" property="${stylesheet}" var="ekmsCSS"/>
    <link rel="stylesheet" href="<c:out value="${ekmsCSS}"/>">
</head>
<body class="portletRow">
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr valign="middle">
            <td height="22" bgcolor="#003366" class="contentTitleFont"><b>&nbsp;<fmt:message key='project.label.addingCompetency'/></font></b></td>
            <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
        </tr>
        <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td></tr>
        <tr><td colspan="2" valign="TOP"><x:display name="wormsAddUserCompetency.form" /></td></tr>
        <tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"> </td></tr>
    </table>
</body>
</html>
