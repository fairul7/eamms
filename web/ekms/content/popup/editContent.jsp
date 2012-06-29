<%@ page import="com.tms.portlet.taglibs.PortalServerUtil"%>
<%@ include file="/common/header.jsp" %>

<x:config>
    <page name="editContent">
        <com.tms.cms.portlet.EditContentPopup name="form" />
    </page>
</x:config>

<c:if test="${!empty param.id}">
    <x:set name="editContent.form" property="id" value="${param.id}"/>
</c:if>

<c:choose>
<c:when test="${forward.name == 'save'}">
    <script>
    <!--
        alert("<fmt:message key='cms.label.contentSaved'/>");
        window.close();
    //-->
    </script>
</c:when>

<c:when test="${forward.name == 'submit'}">
    <script>
    <!--
        alert("<fmt:message key='cms.label.contentSubmittedForApproval'/>");
        window.close();
    //-->
    </script>
</c:when>

<c:when test="${forward.name == 'approve'}">
    <script>
    <!--
        alert("<fmt:message key='cms.label.contentApprovedandPublished'/>");
        window.close();
    //-->
    </script>
</c:when>

<c:when test="${forward.name == 'undo'}">
    <script>
    <!--
        window.close();
    //-->
    </script>
</c:when>

<c:when test="${!empty param['button*editContent.form.containerForm.cancel_form_action']}">
    <script>
    <!--
        window.close();
    //-->
    </script>
</c:when>

<c:otherwise>
<html>
<head>
    <jsp:include page="/ekms/init.jsp" flush="true"/>
    <c-rt:set var="stylesheet" value="<%= PortalServerUtil.PROPERTY_STYLESHEET %>"/>
    <modules:portalserverutil name="portal.server" property="${stylesheet}" var="ekmsCSS"/>
    <link rel="stylesheet" href="<c:out value="${ekmsCSS}"/>">
</head>
<body onload="this.focus()">
<table width="100%" border="0" cellspacing="0" cellpadding="5">
  <tr valign="MIDDLE">
    <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont">
      &nbsp;<fmt:message key='cms.label.editContent'/>
    </font></b></td>
    <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">
        <x:display name="editContent.form"/>
        <c:if test="${empty widgets['editContent.form.containerForm'].children}">
            <fmt:message key='cms.label.contentUnavailable/CheckedOut'/>
        </c:if>
    </td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td>
  </tr>
</table>
</body>
</html>

</c:otherwise>
</c:choose>

