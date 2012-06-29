<%@ include file="/common/header.jsp" %>

<x:permission permission="com.tms.cms.ManageContent" module="com.tms.cms.core.model.ContentManager" url="noPermission.jsp" />

<x:config>
	<page name="indexPg">
		<com.tms.cms.reindex.ui.ReindexForm name="reindex"/>
	</page>
</x:config>

<c:choose>
	<c:when test="${ forward.name == 'success' }">
		<script>
			alert("<fmt:message key='siteadmin.message.reindexSuccess'/>");
		</script>
	</c:when>
	<c:when test="${ forward.name == 'nonselect' }">
		<script>
			alert("<fmt:message key='siteadmin.message.selectContentForReindex'/>");
		</script>
	</c:when>
	<c:when test="${ forward.name == 'error' }">
		<script>
			alert("<fmt:message key='siteadmin.message.reindexFail'/>");
		</script>
	</c:when>
</c:choose>

<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" flush="true"/>
<c:set var="bodyTitle" scope="request"><fmt:message key='general.label.systemAdministration'/> > <fmt:message key="siteadmin.label.systemSettings"/> > <fmt:message key='siteadmin.label.reindex'/></c:set>
<jsp:include page="/ekms/includes/bodyHeader.jsp" flush="true"/>
<b><fmt:message key="siteadmin.label.reindexNote"/></b>
<br/>
<br/>
	<x:display name="indexPg.reindex"/>
                  <br>
                  <br>
                  <br>
                  <br>
                  <br>
                  <br>

<jsp:include page="/ekms/includes/bodyFooter.jsp" flush="true"/>
<jsp:include page="includes/footer.jsp" flush="true"/>
<%@include file="/ekms/includes/footer.jsp" %>
