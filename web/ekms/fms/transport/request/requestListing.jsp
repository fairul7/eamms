<%@ include file="/common/header.jsp" %>
<%@ page import="kacang.Application, java.util.*, java.sql.*, com.tms.fms.transport.model.*, com.tms.fms.setup.model.*"%>

<x:config>
    <page name="transport">         
            <com.tms.fms.transport.ui.TransportListing name="table" />        
    </page>
</x:config>


<%
	String requestId = null;
	boolean isEdit = false;
	boolean isViewCancel = false;
	boolean isOutsource = false;
	boolean isReject = false;
	
	requestId = request.getParameter("id");
	
	TransportRequest  TR = new TransportRequest();
	TransportModule TM = (TransportModule) Application.getInstance().getModule(TransportModule.class);
	SetupModule SM = (SetupModule) Application.getInstance().getModule(SetupModule.class);
	
	
	if(requestId != null){
	try{	
		TR = TM.selectTransportRequest(requestId);
		if(SM.DRAFT_STATUS.equals(TR.getStatus()))
			isEdit = true;
		
		if(SM.PENDING_STATUS.equals(TR.getStatus()))
			isViewCancel = true;
		
		if(SM.OUTSOURCED_STATUS.equals(TR.getStatus()))
			isOutsource = true;
		
		if(SM.REJECTED_STATUS.equals(TR.getStatus()))
			isReject = true;
		
	}catch(Exception e){System.out.println(e);}
	}
	
%>	 


<% if(isEdit){ %>
	<c:redirect url="editRequest.jsp?id=${param.id}"/> 
<%} %>

<% if(isViewCancel){ %>
	<c:redirect url="viewRequest.jsp?id=${param.id}&status=cancelable"/> 
<%} %>

<% if(isOutsource){ %>
	<c:redirect url="viewRequest.jsp?id=${param.id}&status=outsource"/> 
<%} %>

<% if(isReject){ %>
	<c:redirect url="editRequest.jsp?id=${param.id}&status=reject"/> 
<%} %>

<c:if test="${!empty param.id}">
	<c:redirect url="viewRequest.jsp?requestId=${param.id}"/> 
</c:if>

<c:if test="${forward.name == 'Add New'}" >
  <c:redirect url="addNewRequest.jsp"/> 
</c:if>

<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" flush="true"/>
<c:set var="bodyTitle" scope="request"><fmt:message key='fms.tran.myRequest'/> - <fmt:message key="com.tms.fms.transport.transportRequest"/></c:set>
<jsp:include page="/ekms/includes/bodyHeader.jsp" flush="true"/>

    <x:display name="transport" />

<jsp:include page="/ekms/includes/bodyFooter.jsp" flush="true"/>
<jsp:include page="includes/footer.jsp" flush="true"/>
<%@include file="/ekms/includes/footer.jsp" %>