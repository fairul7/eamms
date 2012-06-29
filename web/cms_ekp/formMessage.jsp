<%@ include file="/common/header.jsp" %>



<x:config>
    <page name="formMessagePage">
	        <com.tms.collab.formwizard.ui.FormMessagePanel name="msgPanel"/>
    </page>
</x:config>


<c:if test="${!empty param.formId}">
	<x:set name="formMessagePage.msgPanel" property="formId" value="${param.formId}"/>
</c:if>

<div class="siteBodyHeader">
    Submission Message
</div>

<jsp:include page="includes/header.jsp" flush="true"  />

<x:display name="formMessagePage.msgPanel" ></x:display>

<jsp:include page="includes/footer.jsp" flush="true"  />