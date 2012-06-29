<%@ include file="/common/header.jsp" %>

<c:set var="form" value="${widget}"/>

<jsp:include page="../../form_header.jsp" flush="true"/>

<table cellpadding="0" cellspacing="0" border="0" width="100%" >
<tr>
    <td align="right" style="vertical-align: top; width: 150px;" class="classRowLabel">
    	<fmt:message key="cms.label_com.tms.cms.section.Section"/>&nbsp;
    </td>
    <td>
    	<x:display name="${form.childMap.sectionSelectBox.absoluteName}" />
     </td>
</tr>


<x:display name="${form.childMap.contentObjectForm.absoluteName}" />

<x:display name="${form.childMap.saveButton.absoluteName}" />
<x:display name="${form.childMap.submitButton.absoluteName}" />
<x:display name="${form.childMap.approveButton.absoluteName}" />
<x:display name="${form.childMap['cancel_form_action'].absoluteName}" />


<jsp:include page="../../form_footer.jsp" flush="true"/>


