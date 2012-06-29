<%@ page import="com.tms.crm.sales.ui.SourceForm" %>

<%@include file="/common/header.jsp" %>

<x:config>
    <page name="jsp_source2">
     	<com.tms.crm.sales.ui.SourceForm name="form1" type="Edit" width="100%" />
    </page>
</x:config>

<c-rt:set var="forward_cancel" value="<%= SourceForm.FORWARD_CANCEL %>"/>

<c:choose>
	<c:when test="${not empty(param.sourceID)}">
		<c:set var="sourceID" value="${param.sourceID}" />
	</c:when>
	<c:otherwise>
		<c:set var="sourceID" value="${widgets['jsp_source2.form1'].sourceID}" />
	</c:otherwise>
</c:choose>
<x:set name="jsp_source2.form1" property="sourceID" value="${sourceID}" />
<c:if test="${forward.name == 'sourceUpdated'}">
	<c:redirect url="setup_source.jsp"/>
</c:if>
<c:if test="${forward.name == 'sourceDuplicate'}"><script>
	<!--
		alert ("<fmt:message key='sfa.message.updateRecordErrorDuplicated'/>.");
		location = "setup_source.jsp";
	//-->
	</script>
</c:if>
<c:if test="${forward.name == forward_cancel}">
    <c:redirect url="setup_source.jsp"/>
</c:if>


    <%@include file="/ekms/includes/header.jsp"%>
    <jsp:include page="includes/header.jsp" />
        <table cellpadding="4" cellspacing="1" class="sfaBackground" width="100%">
            <tr valign="top">
                <td align="left" valign="top" class="sfaHeader">
                    <fmt:message key='sfa.message.setup'/> > <fmt:message key='sfa.message.editSource'/>
                     </tr>
    <tr>
    <td class="sfaRow">
	<x:display name="jsp_source2.form1"></x:display>
</td></tr>

    <tr>
        <td class="sfaFooter">
            &nbsp;
        </td>
    </tr>
    </table>

<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp"%>
