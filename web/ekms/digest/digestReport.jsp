<%@ include file="/common/header.jsp" %>
<x:permission permission="com.tms.cms.digest.ManageDigest" module="com.tms.cms.digest.model.DigestModule" url="noPermission.jsp"/>
<x:config>
    <page name="report">
        <com.tms.cms.digest.ui.DigestReport name="digestreport"/>
    </page>
</x:config>

<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" flush="true"/>
<c:set var="bodyTitle" scope="request"><fmt:message key="digest.label.digestFormat"/> Report</c:set>
<jsp:include page="/ekms/includes/bodyHeader.jsp" flush="true"/>

<x:display name="report.digestreport"/>

<jsp:include page="/ekms/includes/bodyFooter.jsp" flush="true"/>
<jsp:include page="includes/footer.jsp" flush="true"/>
<%@include file="/ekms/includes/footer.jsp" %>