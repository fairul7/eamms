<%@include file="/common/header.jsp"%>

<%@ page import="com.tms.sam.po.ui.PrePurchaseApprovalForm,
                kacang.Application,
				kacang.services.security.SecurityService,
				com.tms.sam.po.model.PrePurchaseModule"
%>

<%
	
	PrePurchaseModule pModule = (PrePurchaseModule) Application.getInstance().getModule(PrePurchaseModule.class);
	
	String status = pModule.getStatus(request.getParameter("id"));
	
	if (status.equals("Quoted")) {
		request.setAttribute("Accessible", "true");
	}
	else {
		request.setAttribute("Accessible", "false");
	}

%>

<c:choose>
    <c:when test="${Accessible eq 'false'}">
        <c:redirect url="viewRequestBO.jsp?status=nonPO&id=${param.id}"/>
    </c:when>
    <c:otherwise>
      	<c:redirect url="budgetApproval.jsp?id=${param.id}"/>
    </c:otherwise>
</c:choose>