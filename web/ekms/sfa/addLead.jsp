<%@ include file="/common/header.jsp" %>

<x:config >
    <page name="addleadpage">
        <com.tms.crm.sales.ui.LeadAddForm name="addleadform"/>
    </page>
</x:config>

<c:if test="${forward.name == 'added'}">
	 <script>
        alert("<fmt:message key='sfa.message.newleadadded'/>!");
		location = "leadTable.jsp"
    </script>
</c:if>

<c:if test="${forward.name == 'cancel'}">
    <c:redirect url="/ekms/sfa/leadTable.jsp" /> 
</c:if> 


<%@include file="/ekms/includes/header.jsp"%>
<jsp:include page="includes/header.jsp" />
<table cellpadding="4" cellspacing="1" class="sfaBackground" width="100%">
    <tr valign="top">
        <td align="left" valign="top" class="sfaHeader">

     <fmt:message key='sfa.message.leads'/> > <fmt:message key='sfa.message.lead.addNewLead'/>
             </td>
             </tr>
             <tr>
             <td class="sfaRow">

             <x:display name="addleadpage.addleadform" />

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







