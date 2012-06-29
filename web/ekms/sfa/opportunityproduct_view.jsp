<%@ include file="/common/header.jsp" %>
<%@page import="java.util.*, com.tms.crm.sales.ui.*, kacang.ui.WidgetManager" %>

<x:config>
    <page name="jsp_opportunityproductView">
     	<com.tms.crm.sales.ui.OpportunityProductForm name="form1" type="View" width="100%" />
    </page>
</x:config>


<x:set name="jsp_opportunityproductView.form1" property="opportunityID" value="${param.opportunityID}" />
<x:set name="jsp_opportunityproductView.form1" property="productSeq" value="${param.productSeq}" />


<%@include file="/ekms/includes/header.jsp"%>
<jsp:include page="includes/header.jsp" />
<table cellpadding="4" cellspacing="1" class="sfaBackground" width="100%">
    <tr valign="top">
        <td align="left" valign="top" class="sfaHeader">
            <fmt:message key='sfa.message.viewOpportunityProduct'/>
    </td>
    </tr>
    <tr>
    <td class="sfaRow">


	<x:display name="jsp_opportunityproductView.form1"></x:display>
    </td>
    </tr>
    <tr>
    <td class="sfaRow">

	<a href="opportunityproduct_edit.jsp?opportunityID=<c:out value="${param.opportunityID}"/>&productSeq=<c:out value="${param.productSeq}"/>"><fmt:message key='sfa.message.edit'/></a>
	<br>
	
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
