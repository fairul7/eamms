<%@ page import="com.tms.cms.core.model.ContentManager"%>
<%@ include file="/common/header.jsp" %>

<x:permission permission="com.tms.cms.ManageContent" module="com.tms.cms.core.model.ContentManager" url="noPermission.jsp" />
<%
    if (!Boolean.valueOf(Application.getInstance().getProperty(ContentManager.APPLICATION_PROPERTY_CONTENT_SUBSCRIPTION)).booleanValue()) {
        response.sendRedirect(response.encodeRedirectURL("contentSummary.jsp"));
    }
%>

<x:config>
<page name="contentSubscription">
    <portlet name="contentSubscriptionPortlet" text="<fmt:message key='security.label.subscriptions'/>" width="100%" permanent="true">
        <com.tms.cms.core.ui.EditContentSubscriptionForm name="contentSubscriptionForm" width="100%" >
        <forward name="cancel_form_action" url="contentView.jsp" redirect="true"/>
        </com.tms.cms.core.ui.EditContentSubscriptionForm>
    </portlet>
</page>
</x:config>

<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" flush="true"/>
<c:set var="bodyTitle" scope="request"><fmt:message key="general.label.contentManagement"/></c:set>
<jsp:include page="/ekms/includes/bodyHeader.jsp" flush="true"/>

                <%--Content Path--%>
                <x:display name="cms.contentPath"/> <p>

                  <%-- Content Tree --%>
                  <x:display name="contentSubscription.contentSubscriptionPortlet"/>
                <p> <br>
                  <br>
                  <br>
                  <br>
                  <br>
                  <br>
                  <br>

<jsp:include page="/ekms/includes/bodyFooter.jsp" flush="true"/>
<jsp:include page="includes/footer.jsp" flush="true"/>
<%@include file="/ekms/includes/footer.jsp" %>

