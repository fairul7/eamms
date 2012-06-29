<%@ include file="/common/header.jsp" %>


<x:config>
    <page name="fms">        
            <com.tms.fms.department.ui.FMSUnitTable name="table" />        
    </page>
</x:config>

<c:if test="${!empty param.id}">
	<c:redirect url="fmsUnitSetup.jsp?id=${param.id}"/> 
</c:if>

<c:if test="${forward.name == 'Add New'}" >
  <c:redirect url="fmsUnitSetup.jsp"/> 
</c:if>

<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" flush="true"/>
<c:set var="bodyTitle" scope="request"><fmt:message key='general.label.systemAdministration'/> > <fmt:message key="fms.label.unitListing"/></c:set>
<jsp:include page="/ekms/includes/bodyHeader.jsp" flush="true"/>

    <x:display name="fms" />

<jsp:include page="/ekms/includes/bodyFooter.jsp" flush="true"/>
<jsp:include page="includes/footer.jsp" flush="true"/>
<%@include file="/ekms/includes/footer.jsp" %>
