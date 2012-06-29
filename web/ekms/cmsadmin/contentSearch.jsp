<%@ include file="/common/header.jsp" %>

<x:permission permission="com.tms.cms.ManageContent" module="com.tms.cms.core.model.ContentManager" url="noPermission.jsp" />

<x:config>
<page name="contentSearch">
    <tabbedpanel name="contentSearchPanel" text="<fmt:message key='cms.label.contentFullTextSearch'/>" width="100%" permanent="true">
        <panel name="searchPanel" text="<fmt:message key='cms.label.simpleSearch'/>">
            <com.tms.cms.core.ui.ContentSearchTable name="contentSearchTable" width="100%">
                <forward name="selection" url="contentView.jsp"/>
            </com.tms.cms.core.ui.ContentSearchTable>
        </panel>
        <panel name="advancedSearchPanel" text="<fmt:message key='cms.label.advancedSearch'/>">
            <com.tms.cms.core.ui.ContentSearchPanel name="contentSearchPanel" width="100%">
                <forward name="selection" url="contentView.jsp"/>
            </com.tms.cms.core.ui.ContentSearchPanel>
        </panel>
    </tabbedpanel>
</page>
</x:config>

<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" flush="true"/>
<c:set var="bodyTitle" scope="request"><fmt:message key="cms.label.fullTextSearch"/></c:set>
<jsp:include page="/ekms/includes/bodyHeader.jsp" flush="true"/>

                  <%--Content Search Results--%>
                  <x:display name="contentSearch.contentSearchPanel"/>
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


