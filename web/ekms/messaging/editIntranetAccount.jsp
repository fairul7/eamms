<%@include file="includes/taglib.jsp" %>
 <%@ page import="com.tms.collab.messaging.ui.EditIntranetAccountForm,
                 kacang.ui.WidgetManager,
                 com.tms.collab.messaging.model.MessagingModule"%>

<x:config>
    <page name="editIntranetAccountPage">
        <com.tms.collab.messaging.ui.EditIntranetAccountForm name="form"/>
    </page>
</x:config>

<c-rt:set var="forwardError" value="<%= EditIntranetAccountForm.FORWARD_ERROR %>" />
<c:if test="${forward.name eq forwardError}">
    <c:redirect url="error.jsp" />
</c:if>

<%@include file="includes/header.jsp" %>

<c-rt:set var="forwardSuccess" value="<%= EditIntranetAccountForm.FORWARD_SUCCESS %>"/>
<c:if test="${forward.name eq forwardSuccess}">
    <script><!--
        alert('<fmt:message key='messaging.label.intranetmessagingoptionssaved'/>!');
    //--></script>
</c:if>

<%
    EditIntranetAccountForm form = (EditIntranetAccountForm) WidgetManager.getWidgetManager(request).getWidget("editIntranetAccountPage.form");
    pageContext.setAttribute("form", form);
%>


<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr valign="MIDDLE">
    <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont">
      &nbsp;<fmt:message key='messaging.label.options'/> > <fmt:message key='messaging.label.editIntranetMessagingOptions'/>
    </font></b></td>
    <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">
        <blockquote>
            <fmt:message key='messaging.label.yourintranetmessagingaddressis'/>
                <b class="highlight"><c:out value="${form.intranetAccount.intranetUsername}" />@<%= MessagingModule.INTRANET_EMAIL_DOMAIN %></b>
            <br><br>
        </blockquote>
    </td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP">
        <x:display name="editIntranetAccountPage.form" />
    </td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor">
      <img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"> </td>
  </tr>
</table>

<%@include file="includes/footer.jsp" %>
