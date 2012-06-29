<%@ include file="/common/header.jsp" %>



<x:config>
    <page name="formMessagePage">
	        <com.tms.collab.formwizard.ui.FormMessagePanel name="msgPanel"/>
    </page>
</x:config>


<c:if test="${!empty param.formId}">
	<x:set name="formMessagePage.msgPanel" property="formId" value="${param.formId}"/>
</c:if>

<jsp:include page="includes/header.jsp" flush="true"  />

<div class="siteBodyHeader">
    Form Submitted
</div>

<p>
<x:display name="formMessagePage.msgPanel" ></x:display>
<p>

<jsp:include page="includes/footer.jsp" flush="true"  />