<%@ include file="/common/header.jsp" %>

<jsp:include page="includes/header.jsp" flush="true"  />

<table width="100%">
<tr>
<td>

<%-- TDK: Content --%>
<x:template
    name="content"
    type="com.tms.cms.tdk.DisplayContentObject"
    properties="id=${param.id}&ignorePermission=true" />

</td>
</tr>
</table>

<jsp:include page="includes/footer.jsp" flush="true"  />

