<%@include file="/common/header.jsp"%>

<%@ page import="com.tms.sam.po.ui.ViewMyRequest"%>

<c-rt:set var="forwardBack" value="<%= ViewMyRequest.FORWARD_BACK %>" />
<c:if test="${forward.name eq forwardBack}">
    <c:redirect url="myRequest.jsp" />
</c:if>



<%@include file="/ekms/includes/header.jsp" %>

<x:config>
	<page name="viewRequestPg">
		<com.tms.sam.po.ui.ViewMyRequest name="ViewMyRequest"/>
	</page>
</x:config>

<script language = "javascript">
    function confirmWithdraw(){
        input_box = confirm("<fmt:message key='po.message.confirmWithdraw'/>");
        if (input_box==true)
        {   
            return true;
        }
        else
        {
            return false;
        }
    }
</script>


<c:if test="${!empty param.id}">
	<x:set name="viewRequestPg.ViewMyRequest" property="ppID" value="${param.id}" />
</c:if>

<x:display name="viewRequestPg.ViewMyRequest"/>

<%@include file="/ekms/includes/footer.jsp" %>