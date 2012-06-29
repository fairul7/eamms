<%@ page import="com.tms.cms.core.model.ContentManager"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<x:permission permission="com.tms.cms.ManageSubscribeContent" module="com.tms.cms.core.model.ContentManager" url="noPermission.jsp" />
<%
    if (!Boolean.valueOf(Application.getInstance().getProperty(ContentManager.APPLICATION_PROPERTY_CONTENT_SUBSCRIPTION)).booleanValue()) {
        response.sendRedirect(response.encodeRedirectURL("contentSummary.jsp"));
    }
%>

<x:config>
<page name="contentSubscription">
    <portlet name="contentSubscriptionPortlet" text="<fmt:message key='security.label.subscriptions'/>" width="100%" permanent="true">
        <com.tms.cms.core.ui.EditContentSubscriptionForm name="contentSubscriptionForm" width="100%" />
    </portlet>
</page>
</x:config>

<c:if test="${forward.name == 'success' || forward.name == 'cancel_form_action'}">
    <c:redirect url="contentView.jsp"/>
</c:if>

<%@ include file="/cmsadmin/includes/headerAdmin.jsp" %>

<jsp:include page="/cmsadmin/includes/headerContent.jsp" flush="true" />

<table cellpadding="0" cellspacing="0" border="0" style="text-align: left; width: 100%;">
  <tbody>
    <tr>
      <td style="vertical-align: top; width: 250px;">

        <jsp:include page="/cmsadmin/includes/sideContentTree.jsp" flush="true"/>

      </td>
      <td style="vertical-align: top;">
        <table cellpadding="2" cellspacing="2" border="0" style="text-align: left; width: 100%;">
          <tbody>
            <tr>
              <td style="vertical-align: top;">
                <br>
                <%--Content Path--%>
                <x:display name="cms.contentPath"/> <p>

                  <%-- Content Listing --%>
                  <x:display name="contentSubscription.contentSubscriptionPortlet"/>
                <p> <br>
                  <br>
                  <br>
                  <br>
                  <br>
                  <br>
                  <br>
              </td>
            </tr>
          </tbody>
        </table></td>
    </tr>
  </tbody>
</table>


<jsp:include page="/cmsadmin/includes/footerContent.jsp" flush="true" />

<%@ include file="/cmsadmin/includes/footerAdmin.jsp" %>
