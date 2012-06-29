<%@ include file="/common/header.jsp" %>

<x:permission permission="com.tms.cms.ManageKeywords" module="com.tms.cms.core.model.ContentManager" url="noPermission.jsp" />

<x:config>
<page name="contentRelated">
    <portlet name="keywordsPortlet" text="<fmt:message key='general.label.keywords'/>" width="100%" permanent="true">
        <com.tms.cms.core.ui.RelatedContentPanel name="relatedContentPanel" width="100%">
            <forward name="submit" url="contentRelated.jsp" redirect="true"/>
            <forward name="cancel_form_action" url="contentView.jsp" redirect="true"/>
        </com.tms.cms.core.ui.RelatedContentPanel>
    </portlet>
    <portlet name="relatedContentPortlet" text="<fmt:message key='cms.label.relatedContent'/>" width="100%" permanent="true">
        <com.tms.cms.core.ui.RelatedContentTable name="relatedContentTable" width="100%">
            <forward name="selection" url="contentView.jsp" redirect="false"/>
        </com.tms.cms.core.ui.RelatedContentTable>
    </portlet>
</page>
</x:config>

<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" flush="true"/>
<c:set var="bodyTitle" scope="request"><fmt:message key="general.label.contentManagement"/></c:set>
<jsp:include page="/ekms/includes/bodyHeader.jsp" flush="true"/>

                <%--Content Path--%>
                <x:display name="cms.contentPath"/> <p>

                  <%-- Related Content --%>
                  <x:display name="contentRelated.keywordsPortlet"/>
                  <p>
                  <x:display name="contentRelated.relatedContentPortlet"/>
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

