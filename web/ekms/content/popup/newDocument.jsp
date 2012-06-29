<%@ page import="com.tms.portlet.taglibs.PortalServerUtil"%>
<%@ include file="/common/header.jsp" %>

<x:config>
    <page name="newDocument">
        <com.tms.cms.portlet.AddContentPopup name="form" type="com.tms.cms.document.Document"/>
    </page>
</x:config>

<c:if test="${forward.name == 'save'}">
    <script>
    <!--
        alert("<fmt:message key='cms.label.documentSaved'/>");
        window.close();
    //-->
    </script>
</c:if>

<c:if test="${forward.name == 'submit'}">
    <script>
    <!--
        alert("<fmt:message key='cms.label.documentSubmittedForApproval'/>");
        window.close();
    //-->
    </script>
</c:if>

<c:if test="${forward.name == 'approve'}">
    <script>
    <!--
        alert("<fmt:message key='cms.label.documentApprovedAndPublished'/>");
        window.close();
    //-->
    </script>
</c:if>

<c:if test="${!empty param['button*newDocument.form.containerForm.cancel_form_action']}">
    <script>
    <!--
        window.close();
    //-->
    </script>
</c:if>

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
    <td height="22" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont">
      &nbsp;<fmt:message key='cms.label.newDocument'/>
    </font></b></td>
    <td align="right" class="contentTitleFont">&nbsp;</td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" class="contentBgColor">
        <x:display name="newDocument.form"/>
    </td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td>
  </tr>
</table>
</body>
</html>

