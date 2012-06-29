<%@ include file="/common/header.jsp" %>
<%@ page import="kacang.Application" %>

<x:permission permission="com.tms.cms.ManageContent" module="com.tms.cms.core.model.ContentManager" url="noPermission.jsp" />
<x:permission permission="com.tms.cms.AccessRecycleBin" module="com.tms.cms.core.model.ContentManager" var="recycleBin" />

    
<x:config>
<page name="contentView">
    <portlet name="viewContentPortlet" columns="2" text="<fmt:message key='cms.label.contentDetails'/>" width="100%" permanent="true">
        <com.tms.cms.core.ui.ViewContentObjectPanel name="viewContentObjectPanel"/>
        <com.tms.cms.core.ui.ContentOptionsPanel name="contentOptionsPanel" width="100%">
            <forward name="Create" url="contentAdd.jsp" redirect="true"/>
            <forward name="CheckOut" url="contentEdit.jsp" redirect="true"/>
            <forward name="Preview" url="contentPreview.jsp" redirect="true"/>
            <forward name="UndoCheckOut" url="contentUndoCheckOut.jsp" redirect="true"/>
            <forward name="Move" url="contentMove.jsp" redirect="true"/>
            <forward name="Keywords" url="contentRelated.jsp" redirect="true"/>
            <forward name="Reorder" url="contentReorder.jsp" redirect="true"/>
            <forward name="Subscriptions" url="contentSubscription.jsp" redirect="true"/>
            <forward name="Approve" url="contentApprove.jsp" redirect="true"/>
            <forward name="Publish" url="contentPublish.jsp" redirect="true"/>
            <forward name="Archive" url="contentArchive.jsp" redirect="true"/>
            <forward name="Delete" url="contentDelete.jsp" redirect="true"/>
            <forward name="AclView" url="contentSecurity.jsp" redirect="true"/>
            <forward name="History" url="contentHistory.jsp" redirect="true"/>
            <forward name="AuditView" url="contentAudit.jsp" redirect="true"/>
            <forward name="ReportView" url="contentStatistics.jsp" redirect="true"/>
            <forward name="Taxonomy" url="contentMapping.jsp" redirect="true"/>
        </com.tms.cms.core.ui.ContentOptionsPanel>
    </portlet>
    <portlet name="contentChildrenPortlet" text="<fmt:message key='cms.label.contentChildren'/>" width="100%" permanent="true">
        <com.tms.cms.core.ui.ContentChildrenTable name="contentChildrenTable" width="100%">
            <forward name="selection" url="contentView.jsp" redirect="false"/>
            <forward name="move" url="contentMove.jsp" redirect="false"/>
        </com.tms.cms.core.ui.ContentChildrenTable>
    </portlet>
    <com.tms.cms.core.ui.ContentPath name="contentPath" displayTitle="description">
    	<forward name="selection" url="contentView.jsp" redirect="false"/>
	</com.tms.cms.core.ui.ContentPath>
</page>
</x:config>

<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" flush="true"/>
<c:set var="bodyTitle" scope="request"><fmt:message key="general.label.contentManagement"/></c:set>
<jsp:include page="/ekms/includes/bodyHeader.jsp" flush="true"/>

                <%--Content Path--%>
                <%
                	if (Application.getInstance().getWidget(request, "cms") == null){
                %>
                	<x:display name="contentView.contentPath"/> <p>
                <%
                	}else{
				%>
                	<x:display name="cms.contentPath"/> <p>
                <%	} %>

                  <%--Content Details--%>
                  <x:display name="contentView.viewContentPortlet"/>
                  <c:if test="${widgets['contentView.viewContentPortlet.viewContentObjectPanel'].isDeleted}">
                  		<c:if test="${!recycleBin}">
	                  		<script>
						        document.location="<c:url value="/ekms/cmsadmin/contentNotFound.jsp"/>";
						    </script>
						</c:if>
                  </c:if>
                  <x:display name="contentView.contentChildrenPortlet"/>
                  <br>
                  <br>
                  <br>
                  <br>
                  <br>
                  <br>

<jsp:include page="/ekms/includes/bodyFooter.jsp" flush="true"/>
<jsp:include page="includes/footer.jsp" flush="true"/>
<%@include file="/ekms/includes/footer.jsp" %>
