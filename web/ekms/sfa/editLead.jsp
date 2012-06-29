<%@ include file="/common/header.jsp" %>

<x:config >
    <page name="editleadpage">
        <com.tms.crm.sales.ui.LeadEditForm name="editleadform"/>
    </page>
</x:config>

<c:if test="${! empty param.leadId}" >
    <x:set name="editleadpage.editleadform" property="id" value="${param.leadId}" />
</c:if>

<c:if test="${forward.name=='updated' || forward.name=='cancel' }" >
    <c:set var="leadId" value="${widgets['editleadpage.editleadform'].id}"/>
    <c:redirect url="/ekms/sfa/leadView.jsp?leadId=${leadId}"/>
</c:if>


<%@include file="/ekms/includes/header.jsp"%>
<jsp:include page="includes/header.jsp" />
<table cellpadding="4" cellspacing="1" class="sfaBackground" width="100%">
    <tr valign="top">
        <td align="left" valign="top" class="sfaHeader">

            Edit Lead
             </td>
             </tr>
             <tr>
             <td class="sfaRow">

             <x:display name="editleadpage.editleadform" />

        </td>
    </tr>
    <tr>
    <td class="sfaFooter">
                         &nbsp;
    </td>
    </tr>

</table>
<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp"%>







