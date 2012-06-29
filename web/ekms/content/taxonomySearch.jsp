<%@include file="/common/header.jsp"%>
<x:config>
    <page name="tax_page">
        <com.tms.cms.taxonomy.ui.TaxonomySearch name="tax_search"/>
    </page>
</x:config>

<c:if test="${!empty param.page}">
	<x:set name="tax_page.tax_search" property="page" value="${param.page}"/>
</c:if>
<c:if test="${empty param.page}">
	<x:set name="tax_page.tax_search" property="page" value="1"/>
</c:if>
	

<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" flush="true" />


<table width="100%" border="0" cellspacing="3" cellpadding="0">

	<tr><td style="color:black; font-family:Verdana; font-size:11pt"><b>&nbsp;<fmt:message key="taxonomy.label.advancesearch" /></b></td></tr>

	<tr><td><x:display name="tax_page.tax_search"/></td></tr>

</table>


<jsp:include page="includes/footer.jsp" flush="true" />
<%@include file="/ekms/includes/footer.jsp" %>