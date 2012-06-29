<%@ include file="/common/header.jsp" %>

<x:permission permission="com.tms.cms.ManageKeywords" module="com.tms.cms.core.model.ContentManager" url="noPermission.jsp" />

<x:config>
<page name="contentKeywords">
    <portlet name="contentKeywordsPortlet" text="<fmt:message key='cms.label.contentKeywords'/>" width="100%" permanent="true">
        <com.tms.cms.core.ui.ContentKeywordsPanel name="contentKeywordsPanel" width="100%" />
        <com.tms.cms.core.ui.ContentKeywordsTable name="contentKeywordsTable" width="100%">
            <forward name="deleted" url="contentKeywords.jsp"/>
        </com.tms.cms.core.ui.ContentKeywordsTable>
    </portlet>
</page>
</x:config>

<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" flush="true"/>
<c:set var="bodyTitle" scope="request"><fmt:message key="general.label.administration"/></c:set>
<jsp:include page="/ekms/includes/bodyHeader.jsp" flush="true"/>

                  <%-- Content Keywords --%>
                  <x:display name="contentKeywords.contentKeywordsPortlet"/>
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


