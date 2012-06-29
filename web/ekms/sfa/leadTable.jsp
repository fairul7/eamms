<%@ include file="/common/header.jsp" %>



<x:config>
    <page name="leadtablepage">
        <com.tms.crm.sales.ui.LeadTable name="leadtable"/>
    </page>
</x:config>

<c:if test="${!empty param.id}" >
    <c:redirect url="/ekms/sfa/leadView.jsp?leadId=${param.id}" />
</c:if>



<%@include file="/ekms/includes/header.jsp"%>
<jsp:include page="includes/header.jsp" />
<table cellpadding="4" cellspacing="1" class="sfaBackground" width="100%">
    <tr valign="top">
        <td align="left" valign="top" class="sfaHeader">
            <fmt:message key='sfa.message.leads'/> > <fmt:message key='sfa.message.leadListing'/>

        </td>
    </tr>
    <tr>
    <td class="sfaRow">	<x:display name="leadtablepage.leadtable"></x:display></td>
    </tr>
    <tr>
    <td class="sfaFooter">
                         &nbsp;
    </td>
    </tr>

</table>
<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp"%>
