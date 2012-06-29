<%@ include file="/common/header.jsp" %>

<x:permission permission="com.tms.cms.ManageContent" module="com.tms.cms.core.model.ContentManager" url="noPermission.jsp" />

<x:config>
<page name="contentMapping">
        <com.tms.cms.core.ui.ContentPath name="contentPath" displayTitle="description">
            <forward name="selection" url="contentView.jsp" redirect="false"/>
        </com.tms.cms.core.ui.ContentPath>
        <com.tms.cms.taxonomy.ui.TaxonomyMappingForm name="taxonomyMapping" width="100%">
        <forward name="removed" url="contentMapping.jsp" redirect="true"/>
        </com.tms.cms.taxonomy.ui.TaxonomyMappingForm>
</page>
</x:config>


<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" flush="true"/>
<c:set var="bodyTitle" scope="request"><fmt:message key="general.label.contentManagement"/></c:set>
<jsp:include page="/ekms/includes/bodyHeader.jsp" flush="true"/>

                <%--Content Path--%>
                <x:display name="contentMapping.contentPath"/> <p>

                  <%-- Content Tree --%>
                  <x:display name="contentMapping.taxonomyMapping"/>
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


