<%@include file="/common/header.jsp"%>

<x:permission var="isAuthorized" module="com.tms.collab.myfolder.model.MyFolderModule" permission="com.tms.collab.myfolder.quota"/>
<c:if test="${!isAuthorized}">
    <c:redirect url="noPermission.jsp"/>
</c:if>

<%@include file="/ekms/includes/header.jsp" %>

<x:config>

	<page name="quotaPg">
		<com.tms.collab.myfolder.ui.QuotaForm name="quotaForm"  />		
		<com.tms.collab.myfolder.ui.QuotaTable name="quotaTable" width="100%" />
	</page>
	
</x:config>

<c:set var="bodyTitle" scope="request"><fmt:message key="mf.label.mfQuota"/></c:set>
<jsp:include page="/ekms/includes/bodyHeader.jsp" flush="true"/>

<p>&nbsp;</p>
<div align="center"><x:display name="quotaPg.quotaForm"/></div>
<x:display name="quotaPg.quotaTable"/>

<jsp:include page="/ekms/includes/bodyFooter.jsp" flush="true"/>
<jsp:include page="includes/footer.jsp" flush="true"/>
<%@include file="/ekms/includes/footer.jsp" %>