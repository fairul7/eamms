<%@ include file="/common/header.jsp" %>
<%@ page import="kacang.Application, java.util.*, java.sql.*, com.tms.fms.transport.model.*, com.tms.fms.setup.model.*"%>

<x:config>
    <page name="transport">         
            <com.tms.fms.transport.ui.TransAdminListing name="table" />        
    </page>
</x:config>


<c:if test="${!empty param.id}">
	<c:redirect url="viewAllRequest.jsp?id=${param.id}&view=pending"/> 
</c:if>

<c:if test="${forward.name == 'Add New'}" >
  <c:redirect url="addNewRequest.jsp"/> 
</c:if>

<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" flush="true"/>
<c:set var="bodyTitle" scope="request"><fmt:message key="fms.tran.status.pending"/></c:set>
<jsp:include page="/ekms/includes/bodyHeader.jsp" flush="true"/>

    <x:display name="transport" />

<jsp:include page="/ekms/includes/bodyFooter.jsp" flush="true"/>
<jsp:include page="includes/footer.jsp" flush="true"/>
<%@include file="/ekms/includes/footer.jsp" %>