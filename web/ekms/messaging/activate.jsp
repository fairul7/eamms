<%@include file="includes/taglib.jsp" %>
<%@ page import="com.tms.collab.messaging.ui.ActivateForm,
                 com.tms.collab.messaging.model.MessagingModule"%>

<x:config>
    <page name="activatePage">
        <com.tms.collab.messaging.ui.ActivateForm name="form"/>
    </page>
</x:config>

<c-rt:set var="forwardSuccess" value="<%= ActivateForm.FORWARD_SUCCESS %>"/>
<c:if test="${forward.name eq forwardSuccess}">
    <c:redirect url="activateSuccess.jsp" />
</c:if>

<c-rt:set var="forwardError" value="<%= ActivateForm.FORWARD_ERROR %>" />
<c:if test="${forward.name eq forwardError}">
    <c:redirect url="error.jsp" />
</c:if>

<%@include file="includes/headerActivate.jsp" %>


<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr valign="MIDDLE">
    <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont">
      &nbsp;<fmt:message key='messaging.label.intranetMessagingAccountActivation'/>
    </font></b></td>
    <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">
        <blockquote>
            <fmt:message key='messaging.label.completeformmes'/>
            <b><c:out value="${currentUser.username}" />@<%= MessagingModule.INTRANET_EMAIL_DOMAIN %></b>
            <br><br>
        </blockquote>
    </td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP">
        <x:display name="activatePage.form" />
    </td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor">
      <img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"> </td>
  </tr>
</table>


<%@include file="includes/footerActivate.jsp" %>
