<%@ include file="/common/header.jsp" %>
<%@page import="com.tms.crm.sales.ui.*" %>


<x:config>
    <page name="jsp_opportunityEdit">
     	<com.tms.crm.sales.ui.OpportunityForm name="form1" type="Edit" />
    </page>
</x:config>


<c-rt:set var="cancel" value="<%=OpportunityForm.FORWARD_CANCEL%>"  />

<c:if test="${forward.name == cancel}" >
    <c:redirect url="/ekms/sfa/opportunity_details.jsp?opportunityID=${widgets['jsp_opportunityEdit.form1'].opportunityID}" />
</c:if>


<x:display name="jsp_opportunityEdit.form1"></x:display>

