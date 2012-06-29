<%@ include file="/common/header.jsp" %>

<x:permission permission="com.tms.cms.ManageContent" module="com.tms.cms.core.model.ContentManager" url="noPermission.jsp" />

<x:config>
<page name="contentEdit">
    <portlet name="editContentPortlet" text="<fmt:message key='general.label.editContent'/>" width="100%" permanent="true">
        <com.tms.cms.core.ui.EditContentObjectPanel name="editContentPanel" width="100%">
            <forward name="save" url="contentView.jsp" redirect="true"/>
            <forward name="submit" url="contentView.jsp" redirect="true"/>
            <forward name="approve" url="contentView.jsp" redirect="true"/>
			<forward name="undo" url="contentView.jsp" redirect="true"/>
            <forward name="cancel_form_action" url="contentView.jsp" redirect="true"/>
        </com.tms.cms.core.ui.EditContentObjectPanel>
    </portlet>
</page>
</x:config>

<c:if test="${forward.name=='failed'}">
    <script>
        alert("Unable to perform the operation at this time. Please try again later.");
        document.location="<c:url value="/ekms/cmsadmin/contentView.jsp"/>";
    </script>
</c:if>

<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" flush="true"/>
<c:set var="bodyTitle" scope="request"><fmt:message key="general.label.contentManagement"/></c:set>
<jsp:include page="/ekms/includes/bodyHeader.jsp" flush="true"/>

                <%--Content Path--%>
                <x:display name="cms.contentPath"/> <p>

                  <%-- Content Tree --%>
                  <x:display name="contentEdit.editContentPortlet"/>
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


