<%@ include file="/common/header.jsp" %>

<x:config >
    <page name="jsp_reportbyproduct">
        <com.tms.crm.sales.ui.ReportByProductForm name="form1"/>
    </page>
</x:config>

<c:if test="${forward.name == 'view'}" >

   <script>
        window.open("reportByProduct.jsp");
    </script>
</c:if>

<%@include file="/ekms/includes/header.jsp"%>
<jsp:include page="includes/header.jsp" />
<table cellpadding="4" cellspacing="1" class="sfaBackground" width="100%">
    <tr valign="top">
        <td align="left" valign="top" class="sfaHeader">

        <fmt:message key='sfa.message.salesOpportunityReportByProduct'/>
        </td>
    </tr>

    <tr>
    <td class="sfaRow">
    <x:display name="jsp_reportbyproduct.form1" ></x:display>
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


