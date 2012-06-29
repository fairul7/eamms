<%@ page import="com.tms.portlet.taglibs.PortalServerUtil"%>
<%@ include file="/common/header.jsp" %>

<x:config>
    <page name="newArticle">
        <com.tms.cms.portlet.AddContentPopup name="form" type="com.tms.cms.article.Article"/>
    </page>
</x:config>

<c:if test="${forward.name == 'save'}">
    <script>
    <!--
        alert("<fmt:message key='cms.label.articleSaved'/>");
        window.close();
    //-->
    </script>
</c:if>

<c:if test="${forward.name == 'submit'}">
    <script>
    <!--
        alert("<fmt:message key='cms.label.articleSubmittedForApproval'/>");
        window.close();
    //-->
    </script>
</c:if>

<c:if test="${forward.name == 'approve'}">
    <script>
    <!--
        alert("<fmt:message key='cms.label.articleApprovedAndPublished'/>");
        window.close();
    //-->
    </script>
</c:if>

<c:if test="${!empty param['button*newArticle.form.containerForm.cancel_form_action']}">
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
    <td height="22"  class="contentTitleFont"><b><font class="contentTitleFont">
      &nbsp;<fmt:message key='cms.label.newArticle'/>
    </font></b></td>
    <td align="right" class="contentTitleFont">&nbsp;</td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" class="contentBgColor">
        <x:display name="newArticle.form"/>
    </td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td>
  </tr>
</table>
</body>
</html>

